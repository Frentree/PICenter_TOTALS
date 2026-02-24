package com.org.iopts.service;

import com.org.iopts.dto.request.DmzRequest;
import com.org.iopts.dto.request.TargetSearchRequest;
import com.org.iopts.dto.response.DmzResponse;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.dto.response.TargetResponse;
import com.org.iopts.dto.response.UserResponse;

import java.util.List;
import java.util.Map;

/**
 * Target Service Interface
 */
public interface TargetService {

    /**
     * Get paginated target list with search filters
     */
    PageResponse<TargetResponse> getTargetList(TargetSearchRequest request);

    /**
     * Get target detail by ID
     */
    TargetResponse getTargetDetail(Long targetId);

    /**
     * Get users assigned to a target
     */
    List<UserResponse> getTargetUsers(Long targetId);

    /**
     * Assign a user to a target
     */
    void assignUserToTarget(Long targetId, String userNo, String regUserNo);

    /**
     * Unassign a user from a target
     */
    void unassignUserFromTarget(Long targetId, String userNo);

    /**
     * Get paginated server list
     */
    PageResponse<TargetResponse> getServerList(int page, int size);

    /**
     * Get top files for a server target
     */
    List<Map<String, Object>> getServerTopFiles(Long targetId, int topN);

    /**
     * Get DMZ list
     */
    List<DmzResponse> getDmzList();

    /**
     * Save a new DMZ entry
     */
    void saveDmz(DmzRequest request, String regUserNo);

    /**
     * Delete DMZ entries by IDs
     */
    void deleteDmz(List<Long> dmzIds);

    /**
     * Get group list (tree structure)
     */
    List<Map<String, Object>> getGroupList();

    /**
     * Add a new group
     */
    void addGroup(String groupName, String parentId, String regUserNo);

    /**
     * Update group name
     */
    void updateGroup(String groupId, String groupName);

    /**
     * Delete a group
     */
    void deleteGroup(String groupId);

    /**
     * Get agent version list
     */
    List<Map<String, Object>> getAgentVersionList();

    /**
     * Get paginated exception list
     */
    PageResponse<Map<String, Object>> getExceptionList(int page, int size);
}
