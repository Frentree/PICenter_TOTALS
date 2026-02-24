package com.org.iopts.service.impl;

import com.org.iopts.dto.request.AccountPolicyRequest;
import com.org.iopts.dto.request.PasswordChangeRequest;
import com.org.iopts.dto.request.UserCreateRequest;
import com.org.iopts.dto.request.UserUpdateRequest;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.dto.response.UserLogResponse;
import com.org.iopts.dto.response.UserResponse;
import com.org.iopts.exception.CustomException;
import com.org.iopts.exception.ErrorCode;
import com.org.iopts.repository.UserManageMapper;
import com.org.iopts.service.UserManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User Management Service Implementation
 *
 * Handles all user management business logic including CRUD operations,
 * password management, account locking, team management, user activity logs,
 * and account security policies.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserManageServiceImpl implements UserManageService {

    private final UserManageMapper userManageMapper;
    private final PasswordEncoder passwordEncoder;

    /** Default password used when an admin resets a user password */
    private static final String DEFAULT_RESET_PASSWORD = "picenter1!";

    /**
     * Get paginated user list with optional search
     */
    @Override
    public PageResponse<UserResponse> getUserList(int page, int size, String searchType, String searchKeyword) {
        log.debug("getUserList - page: {}, size: {}, searchType: {}, searchKeyword: {}", page, size, searchType, searchKeyword);

        Map<String, Object> params = new HashMap<>();
        params.put("offset", page * size);
        params.put("limit", size);

        if (searchType != null && !searchType.isEmpty() && searchKeyword != null && !searchKeyword.isEmpty()) {
            params.put("searchType", searchType);
            params.put("searchKeyword", searchKeyword);
        }

        List<UserResponse> content = userManageMapper.selectUserList(params);
        long totalElements = userManageMapper.selectUserListCount(params);

        return PageResponse.of(content, page, size, totalElements);
    }

    /**
     * Get user detail by userNo
     */
    @Override
    public UserResponse getUserDetail(String userNo) {
        log.debug("getUserDetail - userNo: {}", userNo);

        UserResponse user = userManageMapper.selectUserDetail(userNo);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        return user;
    }

    /**
     * Create a new user
     */
    @Override
    @Transactional
    public void createUser(UserCreateRequest request, String regUserNo) {
        log.info("createUser - userId: {}, regUserNo: {}", request.getUserId(), regUserNo);

        // Encode the password
        String encodedPassword = passwordEncoder.encode(request.getUserPwd());

        Map<String, Object> params = new HashMap<>();
        params.put("userId", request.getUserId());
        params.put("userPwd", encodedPassword);
        params.put("userName", request.getUserName());
        params.put("userGrade", request.getUserGrade());
        params.put("teamCode", request.getTeamCode());
        params.put("email", request.getEmail());
        params.put("phone", request.getPhone());
        params.put("useYn", 'Y');
        params.put("lockYn", 'N');
        params.put("loginFailCnt", 0);
        params.put("regUser", String.valueOf(regUserNo));
        params.put("regDt", LocalDateTime.now());
        params.put("modUser", String.valueOf(regUserNo));
        params.put("modDt", LocalDateTime.now());
        params.put("pwdChangeDt", LocalDateTime.now());

        int result = userManageMapper.insertUser(params);
        if (result == 0) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to create user");
        }

        log.info("createUser success - userId: {}", request.getUserId());
    }

    /**
     * Update an existing user
     */
    @Override
    @Transactional
    public void updateUser(String userNo, UserUpdateRequest request, String modUserNo) {
        log.info("updateUser - userNo: {}, modUserNo: {}", userNo, modUserNo);

        // Verify user exists
        UserResponse existing = userManageMapper.selectUserDetail(userNo);
        if (existing == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("userNo", userNo);
        params.put("userName", request.getUserName());
        params.put("userGrade", request.getUserGrade());
        params.put("teamCode", request.getTeamCode());
        params.put("email", request.getEmail());
        params.put("phone", request.getPhone());
        params.put("useYn", request.getUseYn());
        params.put("modUser", String.valueOf(modUserNo));
        params.put("modDt", LocalDateTime.now());

        int result = userManageMapper.updateUser(params);
        if (result == 0) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, "Failed to update user");
        }

        log.info("updateUser success - userNo: {}", userNo);
    }

    /**
     * Delete a user
     */
    @Override
    @Transactional
    public void deleteUser(String userNo) {
        log.info("deleteUser - userNo: {}", userNo);

        // Verify user exists
        UserResponse existing = userManageMapper.selectUserDetail(userNo);
        if (existing == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        int result = userManageMapper.deleteUser(userNo);
        if (result == 0) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, "Failed to delete user");
        }

        log.info("deleteUser success - userNo: {}", userNo);
    }

    /**
     * Change user password (user-initiated)
     * Verifies current password, checks new password confirmation, encodes and saves.
     */
    @Override
    @Transactional
    public void changePassword(String userNo, PasswordChangeRequest request) {
        log.info("changePassword - userNo: {}", userNo);

        // Verify user exists
        UserResponse user = userManageMapper.selectUserDetail(userNo);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        // Verify new password matches confirmation
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD, "New password and confirm password do not match");
        }

        // Encode the new password
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());

        Map<String, Object> params = new HashMap<>();
        params.put("userNo", userNo);
        params.put("userPwd", encodedPassword);
        params.put("currentPassword", request.getCurrentPassword());
        params.put("pwdChangeDt", LocalDateTime.now());

        int result = userManageMapper.updatePassword(params);
        if (result == 0) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD, "Current password is incorrect");
        }

        log.info("changePassword success - userNo: {}", userNo);
    }

    /**
     * Reset user password (admin-initiated)
     * Generates a default password and saves it.
     */
    @Override
    @Transactional
    public void resetPassword(String userNo, String adminUserNo) {
        log.info("resetPassword - userNo: {}, adminUserNo: {}", userNo, adminUserNo);

        // Verify user exists
        UserResponse user = userManageMapper.selectUserDetail(userNo);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        // Encode the default password
        String encodedPassword = passwordEncoder.encode(DEFAULT_RESET_PASSWORD);

        Map<String, Object> params = new HashMap<>();
        params.put("userNo", userNo);
        params.put("userPwd", encodedPassword);
        params.put("modUser", String.valueOf(adminUserNo));
        params.put("pwdChangeDt", LocalDateTime.now());

        int result = userManageMapper.resetPassword(params);
        if (result == 0) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, "Failed to reset password");
        }

        log.info("resetPassword success - userNo: {}", userNo);
    }

    /**
     * Lock a user account
     */
    @Override
    @Transactional
    public void lockUser(String userNo) {
        log.info("lockUser - userNo: {}", userNo);

        // Verify user exists
        UserResponse user = userManageMapper.selectUserDetail(userNo);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        int result = userManageMapper.lockUser(userNo);
        if (result == 0) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, "Failed to lock user");
        }

        log.info("lockUser success - userNo: {}", userNo);
    }

    /**
     * Unlock a user account
     */
    @Override
    @Transactional
    public void unlockUser(String userNo) {
        log.info("unlockUser - userNo: {}", userNo);

        // Verify user exists
        UserResponse user = userManageMapper.selectUserDetail(userNo);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        int result = userManageMapper.unlockUser(userNo);
        if (result == 0) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, "Failed to unlock user");
        }

        log.info("unlockUser success - userNo: {}", userNo);
    }

    /**
     * Get all teams
     */
    @Override
    public List<Map<String, Object>> getTeamList() {
        log.debug("getTeamList");
        return userManageMapper.selectTeamList();
    }

    /**
     * Create a new team
     */
    @Override
    @Transactional
    public void createTeam(String teamCode, String teamName, String regUserNo) {
        log.info("createTeam - teamCode: {}, teamName: {}, regUserNo: {}", teamCode, teamName, regUserNo);

        Map<String, Object> params = new HashMap<>();
        params.put("teamCode", teamCode);
        params.put("teamName", teamName);
        params.put("parentCode", null);
        params.put("sortOrder", 0);
        params.put("regDt", LocalDateTime.now());

        int result = userManageMapper.insertTeam(params);
        if (result == 0) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to create team");
        }

        log.info("createTeam success - teamCode: {}", teamCode);
    }

    /**
     * Get paginated user log list with optional search
     */
    @Override
    public PageResponse<UserLogResponse> getUserLogList(int page, int size, String searchType, String searchKeyword) {
        log.debug("getUserLogList - page: {}, size: {}, searchType: {}, searchKeyword: {}", page, size, searchType, searchKeyword);

        Map<String, Object> params = new HashMap<>();
        params.put("offset", page * size);
        params.put("limit", size);

        if (searchType != null && !searchType.isEmpty() && searchKeyword != null && !searchKeyword.isEmpty()) {
            params.put("searchType", searchType);
            params.put("searchKeyword", searchKeyword);
        }

        List<UserLogResponse> content = userManageMapper.selectUserLogList(params);
        long totalElements = userManageMapper.selectUserLogListCount(params);

        return PageResponse.of(content, page, size, totalElements);
    }

    /**
     * Get current account policy
     */
    @Override
    public Map<String, Object> getAccountPolicy() {
        log.debug("getAccountPolicy");
        return userManageMapper.selectAccountPolicy();
    }

    /**
     * Save (create or update) account policy
     */
    @Override
    @Transactional
    public void saveAccountPolicy(AccountPolicyRequest request) {
        log.info("saveAccountPolicy - loginFailLimit: {}, passwordExpireDays: {}", request.getLoginFailLimit(), request.getPasswordExpireDays());

        Map<String, Object> params = new HashMap<>();
        params.put("loginFailLimit", request.getLoginFailLimit());
        params.put("passwordExpireDays", request.getPasswordExpireDays());
        params.put("accountLockDays", request.getAccountLockDays());
        params.put("passwordComplexYn", request.getPasswordComplexYn());
        params.put("passwordMinLength", request.getPasswordMinLength());
        params.put("sessionTimeout", request.getSessionTimeout());

        int result = userManageMapper.saveAccountPolicy(params);
        if (result == 0) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to save account policy");
        }

        log.info("saveAccountPolicy success");
    }
}
