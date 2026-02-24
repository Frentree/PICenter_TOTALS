package com.org.iopts.service;

import com.org.iopts.dto.request.AccountPolicyRequest;
import com.org.iopts.dto.request.PasswordChangeRequest;
import com.org.iopts.dto.request.UserCreateRequest;
import com.org.iopts.dto.request.UserUpdateRequest;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.dto.response.UserLogResponse;
import com.org.iopts.dto.response.UserResponse;

import java.util.List;
import java.util.Map;

/**
 * User Management Service Interface
 *
 * Provides operations for user CRUD, password management,
 * account locking, team management, user logs, and account policies.
 *
 * Legacy mappings:
 *   - getUserList       -> /memberList
 *   - getUserDetail     -> /memberDetail
 *   - createUser        -> /memberInsert
 *   - updateUser        -> /memberUpdate
 *   - deleteUser        -> /memberDelete
 *   - changePassword    -> /changePwd
 *   - resetPassword     -> /resetPwd
 *   - lockUser          -> /lockUser
 *   - unlockUser        -> /unlockUser
 *   - getTeamList       -> /teamList
 *   - createTeam        -> /teamInsert
 *   - getUserLogList    -> /userLogList
 *   - getAccountPolicy  -> /getAccountPolicy
 *   - saveAccountPolicy -> /saveAccountPolicy
 */
public interface UserManageService {

    /**
     * Get paginated user list with optional search
     *
     * @param page          page number (0-based)
     * @param size          page size
     * @param searchType    search field (userId, userName, teamCode, etc.)
     * @param searchKeyword search value
     * @return paginated user response
     */
    PageResponse<UserResponse> getUserList(int page, int size, String searchType, String searchKeyword);

    /**
     * Get user detail by userNo
     *
     * @param userNo user primary key
     * @return user detail
     */
    UserResponse getUserDetail(String userNo);

    /**
     * Create a new user
     *
     * @param request   user creation data
     * @param regUserNo user who is creating this account
     */
    void createUser(UserCreateRequest request, String regUserNo);

    /**
     * Update an existing user
     *
     * @param userNo    target user primary key
     * @param request   update data
     * @param modUserNo user who is performing the update
     */
    void updateUser(String userNo, UserUpdateRequest request, String modUserNo);

    /**
     * Delete a user
     *
     * @param userNo target user primary key
     */
    void deleteUser(String userNo);

    /**
     * Change user password (user-initiated)
     *
     * @param userNo  target user primary key
     * @param request password change data
     */
    void changePassword(String userNo, PasswordChangeRequest request);

    /**
     * Reset user password (admin-initiated)
     *
     * @param userNo      target user primary key
     * @param adminUserNo admin who is resetting the password
     */
    void resetPassword(String userNo, String adminUserNo);

    /**
     * Lock a user account
     *
     * @param userNo target user primary key
     */
    void lockUser(String userNo);

    /**
     * Unlock a user account
     *
     * @param userNo target user primary key
     */
    void unlockUser(String userNo);

    /**
     * Get all teams
     *
     * @return list of team maps (teamCode, teamName, etc.)
     */
    List<Map<String, Object>> getTeamList();

    /**
     * Create a new team
     *
     * @param teamCode  team code
     * @param teamName  team name
     * @param regUserNo user who is creating the team
     */
    void createTeam(String teamCode, String teamName, String regUserNo);

    /**
     * Get paginated user log list with optional search
     *
     * @param page          page number (0-based)
     * @param size          page size
     * @param searchType    search field (userId, logType, etc.)
     * @param searchKeyword search value
     * @return paginated user log response
     */
    PageResponse<UserLogResponse> getUserLogList(int page, int size, String searchType, String searchKeyword);

    /**
     * Get current account policy
     *
     * @return account policy map
     */
    Map<String, Object> getAccountPolicy();

    /**
     * Save (create or update) account policy
     *
     * @param request account policy data
     */
    void saveAccountPolicy(AccountPolicyRequest request);
}
