package com.org.iopts.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Group MyBatis Mapper
 *
 * Namespace: com.org.iopts.repository.GroupMapper
 */
@Mapper
public interface GroupMapper {

    /**
     * Select all groups (flat list for tree building)
     */
    List<Map<String, Object>> selectGroupList();

    /**
     * Select single group detail by groupId
     */
    Map<String, Object> selectGroupDetail(@Param("groupId") String groupId);

    /**
     * Select group statistics (target counts, etc.)
     */
    Map<String, Object> selectGroupStats(@Param("groupId") String groupId);

    /**
     * Insert a new group
     */
    int insertGroup(Map<String, Object> params);

    /**
     * Update target group assignment
     */
    int updateTargetGroup(Map<String, Object> params);
}
