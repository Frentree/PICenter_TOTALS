package com.org.iopts.repository;

import com.org.iopts.dto.response.UserLogResponse;
import com.org.iopts.dto.response.UserResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * User Management MyBatis Mapper
 *
 * Provides SQL operations for user CRUD, password management,
 * account locking, team management, user logs, and account policies.
 * Namespace: com.org.iopts.repository.UserManageMapper
 */
@Mapper
public interface UserManageMapper {

    /**
     * Select paginated user list with optional search filters
     */
    List<UserResponse> selectUserList(Map<String, Object> params);

    /**
     * Count total users matching search filters (for pagination)
     */
    long selectUserListCount(Map<String, Object> params);

    /**
     * Select single user detail by userNo
     */
    UserResponse selectUserDetail(@Param("userNo") String userNo);

    /**
     * Insert a new user
     */
    int insertUser(Map<String, Object> params);

    /**
     * Update an existing user
     */
    int updateUser(Map<String, Object> params);

    /**
     * Delete a user by userNo (logical or physical delete)
     */
    int deleteUser(@Param("userNo") String userNo);

    /**
     * Update user password (user-initiated change)
     */
    int updatePassword(Map<String, Object> params);

    /**
     * Reset user password (admin-initiated reset)
     */
    int resetPassword(Map<String, Object> params);

    /**
     * Lock a user account
     */
    int lockUser(@Param("userNo") String userNo);

    /**
     * Unlock a user account
     */
    int unlockUser(@Param("userNo") String userNo);

    /**
     * Select all teams
     */
    List<Map<String, Object>> selectTeamList();

    /**
     * Insert a new team
     */
    int insertTeam(Map<String, Object> params);

    /**
     * Select paginated user log list with optional search filters
     */
    List<UserLogResponse> selectUserLogList(Map<String, Object> params);

    /**
     * Count total user logs matching search filters (for pagination)
     */
    long selectUserLogListCount(Map<String, Object> params);

    /**
     * Select current account policy
     */
    Map<String, Object> selectAccountPolicy();

    /**
     * Save (insert or update) account policy
     */
    int saveAccountPolicy(Map<String, Object> params);

    /**
     * Select paginated locked users
     */
    List<UserResponse> selectLockedUsers(Map<String, Object> params);

    /**
     * Count total locked users
     */
    long countLockedUsers(Map<String, Object> params);
}
