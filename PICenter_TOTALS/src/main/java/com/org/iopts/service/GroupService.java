package com.org.iopts.service;

import com.org.iopts.dto.response.GroupTreeResponse;

import java.util.List;
import java.util.Map;

/**
 * Group Service Interface
 *
 * Provides operations for group tree retrieval, stats, and target assignment.
 *
 * Legacy mappings:
 *   - getGroupTree       -> /groupTree
 *   - getGroupStats      -> /groupStats
 *   - moveTargetsToGroup -> /moveTargetGroup
 */
public interface GroupService {

    /**
     * Get group tree structure (builds tree from flat list)
     *
     * @return hierarchical group tree with target counts
     */
    List<GroupTreeResponse> getGroupTree();

    /**
     * Get group statistics (target counts, status breakdown)
     *
     * @param groupId group primary key
     * @return map of statistics
     */
    Map<String, Object> getGroupStats(String groupId);

    /**
     * Move targets to a different group
     *
     * @param groupId   target group to move targets into
     * @param targetIds list of target IDs to move
     */
    void moveTargetsToGroup(String groupId, List<Long> targetIds);
}
