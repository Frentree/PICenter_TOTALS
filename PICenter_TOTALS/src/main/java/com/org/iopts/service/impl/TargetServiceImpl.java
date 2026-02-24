package com.org.iopts.service.impl;

import com.org.iopts.dto.request.DmzRequest;
import com.org.iopts.dto.request.TargetSearchRequest;
import com.org.iopts.dto.response.DmzResponse;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.dto.response.TargetResponse;
import com.org.iopts.dto.response.UserResponse;
import com.org.iopts.exception.CustomException;
import com.org.iopts.exception.ErrorCode;
import com.org.iopts.repository.TargetMapper;
import com.org.iopts.service.TargetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Target Service Implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TargetServiceImpl implements TargetService {

    private final TargetMapper targetMapper;

    @Override
    public PageResponse<TargetResponse> getTargetList(TargetSearchRequest request) {
        log.debug("Getting target list: searchType={}, keyword={}, page={}, size={}",
                request.getSearchType(), request.getSearchKeyword(),
                request.getPage(), request.getSize());

        int offset = request.getPage() * request.getSize();
        List<TargetResponse> content = targetMapper.selectTargetList(request, offset, request.getSize());
        long totalElements = targetMapper.countTargetList(request);

        return PageResponse.of(content, request.getPage(), request.getSize(), totalElements);
    }

    @Override
    public TargetResponse getTargetDetail(Long targetId) {
        log.debug("Getting target detail: targetId={}", targetId);

        TargetResponse target = targetMapper.selectTargetDetail(targetId);
        if (target == null) {
            throw new CustomException(ErrorCode.TARGET_NOT_FOUND);
        }
        return target;
    }

    @Override
    public List<UserResponse> getTargetUsers(Long targetId) {
        log.debug("Getting users for target: targetId={}", targetId);

        // Verify target exists
        TargetResponse target = targetMapper.selectTargetDetail(targetId);
        if (target == null) {
            throw new CustomException(ErrorCode.TARGET_NOT_FOUND);
        }

        return targetMapper.selectTargetUsers(targetId);
    }

    @Override
    @Transactional
    public void assignUserToTarget(Long targetId, String userNo, String regUserNo) {
        log.info("Assigning user {} to target {}", userNo, targetId);

        // Check if assignment already exists
        int count = targetMapper.countTargetUser(targetId, userNo);
        if (count > 0) {
            throw new CustomException(ErrorCode.INVALID_PARAMETER, "User is already assigned to this target");
        }

        targetMapper.insertTargetUser(targetId, userNo, regUserNo);
        log.info("User {} assigned to target {} successfully", userNo, targetId);
    }

    @Override
    @Transactional
    public void unassignUserFromTarget(Long targetId, String userNo) {
        log.info("Unassigning user {} from target {}", userNo, targetId);

        // Check if assignment exists
        int count = targetMapper.countTargetUser(targetId, userNo);
        if (count == 0) {
            throw new CustomException(ErrorCode.NOT_FOUND, "User is not assigned to this target");
        }

        targetMapper.deleteTargetUser(targetId, userNo);
        log.info("User {} unassigned from target {} successfully", userNo, targetId);
    }

    @Override
    public PageResponse<TargetResponse> getServerList(int page, int size) {
        log.debug("Getting server list: page={}, size={}", page, size);

        int offset = page * size;
        List<TargetResponse> content = targetMapper.selectServerList(offset, size);
        long totalElements = targetMapper.countServerList();

        return PageResponse.of(content, page, size, totalElements);
    }

    @Override
    public List<Map<String, Object>> getServerTopFiles(Long targetId, int topN) {
        log.debug("Getting top {} files for target: targetId={}", topN, targetId);

        // Verify target exists
        TargetResponse target = targetMapper.selectTargetDetail(targetId);
        if (target == null) {
            throw new CustomException(ErrorCode.TARGET_NOT_FOUND);
        }

        return targetMapper.selectServerTopFiles(targetId, topN);
    }

    @Override
    public List<DmzResponse> getDmzList() {
        log.debug("Getting DMZ list");
        return targetMapper.selectDmzList();
    }

    @Override
    @Transactional
    public void saveDmz(DmzRequest request, String regUserNo) {
        log.info("Saving DMZ: ip={}, memo={}", request.getDmzIp(), request.getMemo());

        targetMapper.insertDmz(
                request.getDmzIp(),
                request.getMemo()
        );
        log.info("DMZ saved successfully: ip={}", request.getDmzIp());
    }

    @Override
    @Transactional
    public void deleteDmz(List<Long> dmzIds) {
        log.info("Deleting DMZ entries: ids={}", dmzIds);

        if (dmzIds == null || dmzIds.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_PARAMETER, "DMZ IDs must not be empty");
        }

        targetMapper.deleteDmzByIds(dmzIds);
        log.info("DMZ entries deleted successfully: count={}", dmzIds.size());
    }

    @Override
    public List<Map<String, Object>> getGroupList() {
        log.debug("Getting group list");
        return targetMapper.selectGroupList();
    }

    @Override
    @Transactional
    public void addGroup(String groupName, String parentId, String regUserNo) {
        log.info("Adding group: name={}, parentId={}", groupName, parentId);
        targetMapper.insertGroup(groupName, parentId, regUserNo);
        log.info("Group added successfully: name={}", groupName);
    }

    @Override
    @Transactional
    public void updateGroup(String groupId, String groupName) {
        log.info("Updating group: groupId={}, name={}", groupId, groupName);
        targetMapper.updateGroup(groupId, groupName);
        log.info("Group updated successfully: groupId={}", groupId);
    }

    @Override
    @Transactional
    public void deleteGroup(String groupId) {
        log.info("Deleting group: groupId={}", groupId);

        // Check for child groups
        int childCount = targetMapper.countChildGroups(groupId);
        if (childCount > 0) {
            throw new CustomException(ErrorCode.INVALID_PARAMETER, "Cannot delete group with child groups");
        }

        targetMapper.deleteGroup(groupId);
        log.info("Group deleted successfully: groupId={}", groupId);
    }

    @Override
    public List<Map<String, Object>> getAgentVersionList() {
        log.debug("Getting agent version list");
        return targetMapper.selectAgentVersionList();
    }

    @Override
    public PageResponse<Map<String, Object>> getExceptionList(int page, int size) {
        log.debug("Getting exception list: page={}, size={}", page, size);

        int offset = page * size;
        List<Map<String, Object>> content = targetMapper.selectExceptionList(offset, size);
        long totalElements = targetMapper.countExceptionList();

        return PageResponse.of(content, page, size, totalElements);
    }
}
