package com.org.iopts.service.impl;

import com.org.iopts.dto.response.GroupTreeResponse;
import com.org.iopts.exception.CustomException;
import com.org.iopts.exception.ErrorCode;
import com.org.iopts.repository.GroupMapper;
import com.org.iopts.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Group Service Implementation
 *
 * Handles group tree building from flat list, statistics retrieval,
 * and target-to-group reassignment.
 *
 * pi_groups table columns:
 *   GROUP_ID (PK auto_increment), GROUP_NAME, PARENT_ID, SORT_ORDER, REG_DT
 *
 * pi_targets table relevant columns:
 *   TARGET_ID, GROUP_ID, NAME, PLATFORM, TARGET_USE, SEARCH_STATUS
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupServiceImpl implements GroupService {

    private final GroupMapper groupMapper;

    /**
     * Get group tree structure.
     * Fetches flat group list from DB and builds a hierarchical tree in-memory.
     */
    @Override
    public List<GroupTreeResponse> getGroupTree() {
        log.debug("getGroupTree");

        List<Map<String, Object>> flatList = groupMapper.selectGroupList();

        if (flatList == null || flatList.isEmpty()) {
            return Collections.emptyList();
        }

        // Convert flat rows to GroupTreeResponse objects
        List<GroupTreeResponse> allNodes = flatList.stream()
                .map(this::mapToGroupTreeResponse)
                .collect(Collectors.toList());

        // Build tree: index by groupId, then attach children
        Map<String, GroupTreeResponse> nodeMap = new LinkedHashMap<>();
        for (GroupTreeResponse node : allNodes) {
            nodeMap.put(node.getGroupId(), node);
        }

        List<GroupTreeResponse> roots = new ArrayList<>();
        for (GroupTreeResponse node : allNodes) {
            String parentId = node.getParentGroupId();
            if (parentId == null || parentId.isEmpty() || !nodeMap.containsKey(parentId)) {
                // This is a root node
                roots.add(node);
            } else {
                // Attach to parent
                GroupTreeResponse parent = nodeMap.get(parentId);
                parent.getChildren().add(node);
            }
        }

        return roots;
    }

    /**
     * Get group statistics (target counts, status breakdown)
     */
    @Override
    public Map<String, Object> getGroupStats(String groupId) {
        log.debug("getGroupStats - groupId: {}", groupId);

        Map<String, Object> stats = groupMapper.selectGroupStats(groupId);
        if (stats == null) {
            throw new CustomException(ErrorCode.GROUP_NOT_FOUND);
        }

        return stats;
    }

    /**
     * Move targets to a different group
     */
    @Override
    @Transactional
    public void moveTargetsToGroup(String groupId, List<Long> targetIds) {
        log.info("moveTargetsToGroup - groupId: {}, targetIds count: {}", groupId, targetIds.size());

        // Verify group exists
        Map<String, Object> group = groupMapper.selectGroupDetail(groupId);
        if (group == null) {
            throw new CustomException(ErrorCode.GROUP_NOT_FOUND);
        }

        if (targetIds == null || targetIds.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_PARAMETER, "Target IDs are required");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("targetIds", targetIds);

        int result = groupMapper.updateTargetGroup(params);
        if (result == 0) {
            throw new CustomException(ErrorCode.TARGET_NOT_FOUND, "No targets found to move");
        }

        log.info("moveTargetsToGroup success - groupId: {}, moved: {}", groupId, result);
    }

    /**
     * Convert a Map row to GroupTreeResponse.
     *
     * pi_groups has no GROUP_LEVEL column; tree depth is determined
     * by the parent-child relationships built in getGroupTree().
     */
    private GroupTreeResponse mapToGroupTreeResponse(Map<String, Object> row) {
        return GroupTreeResponse.builder()
                .groupId(row.get("groupId") != null ? row.get("groupId").toString() : null)
                .groupName(row.get("groupName") != null ? row.get("groupName").toString() : null)
                .parentGroupId(row.get("parentGroupId") != null ? row.get("parentGroupId").toString() : null)
                .sortOrder(toInteger(row.get("sortOrder")))
                .targetCount(toLongSafe(row.get("targetCount")))
                .children(new ArrayList<>())
                .build();
    }

    /**
     * Safely convert Object to Integer.
     * Handles both Number and String types (degree is varchar in DB).
     */
    private Integer toInteger(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(value.toString().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Safely convert Object to Long.
     * Handles both Number and String types.
     */
    private Long toLongSafe(Object value) {
        if (value == null) {
            return 0L;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(value.toString().trim());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
}
