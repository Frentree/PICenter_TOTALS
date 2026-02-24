package com.org.iopts.repository;

import com.org.iopts.dto.request.TargetSearchRequest;
import com.org.iopts.dto.response.DmzResponse;
import com.org.iopts.dto.response.TargetResponse;
import com.org.iopts.dto.response.UserResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Target MyBatis Mapper
 */
@Mapper
public interface TargetMapper {

    /**
     * Search targets with pagination
     */
    List<TargetResponse> selectTargetList(@Param("request") TargetSearchRequest request,
                                          @Param("offset") int offset,
                                          @Param("limit") int limit);

    /**
     * Count total targets matching search criteria
     */
    long countTargetList(@Param("request") TargetSearchRequest request);

    /**
     * Get target detail by ID
     */
    TargetResponse selectTargetDetail(@Param("targetId") Long targetId);

    /**
     * Get users assigned to a target
     */
    List<UserResponse> selectTargetUsers(@Param("targetId") Long targetId);

    /**
     * Assign user to target
     */
    void insertTargetUser(@Param("targetId") Long targetId,
                          @Param("userNo") String userNo,
                          @Param("regUserNo") String regUserNo);

    /**
     * Unassign user from target
     */
    void deleteTargetUser(@Param("targetId") Long targetId,
                          @Param("userNo") String userNo);

    /**
     * Check if target-user assignment exists
     */
    int countTargetUser(@Param("targetId") Long targetId,
                        @Param("userNo") String userNo);

    /**
     * Get server list with pagination
     */
    List<TargetResponse> selectServerList(@Param("offset") int offset,
                                          @Param("limit") int limit);

    /**
     * Count total servers
     */
    long countServerList();

    /**
     * Get top files for a server target
     */
    List<Map<String, Object>> selectServerTopFiles(@Param("targetId") Long targetId,
                                                   @Param("topN") int topN);

    /**
     * Get DMZ list
     */
    List<DmzResponse> selectDmzList();

    /**
     * Insert DMZ info
     */
    void insertDmz(@Param("dmzIp") String dmzIp,
                    @Param("memo") String memo);

    /**
     * Delete DMZ entries by IDs
     */
    void deleteDmzByIds(@Param("dmzIds") List<Long> dmzIds);

    /**
     * Get group list (tree structure)
     */
    List<Map<String, Object>> selectGroupList();

    /**
     * Add a new group
     */
    void insertGroup(@Param("groupName") String groupName,
                     @Param("parentId") String parentId,
                     @Param("regUserNo") String regUserNo);

    /**
     * Update group name
     */
    void updateGroup(@Param("groupId") String groupId,
                     @Param("groupName") String groupName);

    /**
     * Delete a group
     */
    void deleteGroup(@Param("groupId") String groupId);

    /**
     * Check if group has child groups
     */
    int countChildGroups(@Param("groupId") String groupId);

    /**
     * Get agent version list
     */
    List<Map<String, Object>> selectAgentVersionList();

    /**
     * Get exception list with pagination
     */
    List<Map<String, Object>> selectExceptionList(@Param("offset") int offset,
                                                  @Param("limit") int limit);

    /**
     * Count total exceptions
     */
    long countExceptionList();
}
