package com.org.iopts.controller;

import com.org.iopts.dto.request.AccountPolicyRequest;
import com.org.iopts.dto.request.PasswordChangeRequest;
import com.org.iopts.dto.request.UserCreateRequest;
import com.org.iopts.dto.request.UserUpdateRequest;
import com.org.iopts.dto.response.ApiResponse;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.dto.response.UserLogResponse;
import com.org.iopts.dto.response.UserResponse;
import com.org.iopts.service.UserManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * User Management Controller
 *
 * Provides REST API endpoints for user CRUD operations, password management,
 * account locking, team management, user activity logs, and account policies.
 *
 * Legacy endpoint mappings:
 *   GET    /api/v1/users                    <- /memberList
 *   GET    /api/v1/users/{userNo}           <- /memberDetail
 *   POST   /api/v1/users                    <- /memberInsert
 *   PUT    /api/v1/users/{userNo}           <- /memberUpdate
 *   DELETE /api/v1/users/{userNo}           <- /memberDelete
 *   PUT    /api/v1/users/{userNo}/password  <- /changePwd
 *   PUT    /api/v1/users/{userNo}/reset-password <- /resetPwd
 *   PUT    /api/v1/users/{userNo}/lock      <- /lockUser
 *   PUT    /api/v1/users/{userNo}/unlock    <- /unlockUser
 *   GET    /api/v1/users/teams              <- /teamList
 *   POST   /api/v1/users/teams              <- /teamInsert
 *   GET    /api/v1/users/logs               <- /userLogList
 *   GET    /api/v1/users/account-policy     <- /getAccountPolicy
 *   PUT    /api/v1/users/account-policy     <- /saveAccountPolicy
 */
@Slf4j
@Tag(name = "User Management", description = "User management API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserManageService userManageService;

    /**
     * Get paginated user list
     *
     * Legacy: GET /memberList
     * New: GET /api/v1/users
     */
    @Operation(summary = "Get user list", description = "Get paginated user list with optional search filters")
    @GetMapping
    public ApiResponse<PageResponse<UserResponse>> getUserList(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Search field (userId, userName, teamCode)", example = "userId") @RequestParam(required = false) String searchType,
            @Parameter(description = "Search value", example = "admin") @RequestParam(required = false) String searchKeyword) {

        log.debug("GET /api/v1/users - page: {}, size: {}, searchType: {}, searchKeyword: {}", page, size, searchType, searchKeyword);
        PageResponse<UserResponse> result = userManageService.getUserList(page, size, searchType, searchKeyword);
        return ApiResponse.success(result);
    }

    /**
     * Get user detail
     *
     * Legacy: GET /memberDetail
     * New: GET /api/v1/users/{userNo}
     */
    @Operation(summary = "Get user detail", description = "Get user detail by user number")
    @GetMapping("/{userNo}")
    public ApiResponse<UserResponse> getUserDetail(
            @Parameter(description = "User number", example = "1") @PathVariable String userNo) {

        log.debug("GET /api/v1/users/{}", userNo);
        UserResponse result = userManageService.getUserDetail(userNo);
        return ApiResponse.success(result);
    }

    /**
     * Create a new user
     *
     * Legacy: POST /memberInsert
     * New: POST /api/v1/users
     */
    @Operation(summary = "Create user", description = "Create a new user account")
    @PostMapping
    public ApiResponse<Void> createUser(
            @Valid @RequestBody UserCreateRequest request,
            Authentication authentication) {

        String userNo = (String) authentication.getPrincipal();
        log.info("POST /api/v1/users - userId: {}, by: {}", request.getUserId(), userNo);
        userManageService.createUser(request, userNo);
        return ApiResponse.success();
    }

    /**
     * Update an existing user
     *
     * Legacy: POST /memberUpdate
     * New: PUT /api/v1/users/{userNo}
     */
    @Operation(summary = "Update user", description = "Update an existing user account")
    @PutMapping("/{userNo}")
    public ApiResponse<Void> updateUser(
            @Parameter(description = "User number", example = "1") @PathVariable String userNo,
            @Valid @RequestBody UserUpdateRequest request,
            Authentication authentication) {

        String currentUserNo = (String) authentication.getPrincipal();
        log.info("PUT /api/v1/users/{} - by: {}", userNo, currentUserNo);
        userManageService.updateUser(userNo, request, currentUserNo);
        return ApiResponse.success();
    }

    /**
     * Delete a user
     *
     * Legacy: POST /memberDelete
     * New: DELETE /api/v1/users/{userNo}
     */
    @Operation(summary = "Delete user", description = "Delete a user account")
    @DeleteMapping("/{userNo}")
    public ApiResponse<Void> deleteUser(
            @Parameter(description = "User number", example = "1") @PathVariable String userNo) {

        log.info("DELETE /api/v1/users/{}", userNo);
        userManageService.deleteUser(userNo);
        return ApiResponse.success();
    }

    /**
     * Change user password (user-initiated)
     *
     * Legacy: POST /changePwd
     * New: PUT /api/v1/users/{userNo}/password
     */
    @Operation(summary = "Change password", description = "Change user password (requires current password)")
    @PutMapping("/{userNo}/password")
    public ApiResponse<Void> changePassword(
            @Parameter(description = "User number", example = "1") @PathVariable String userNo,
            @Valid @RequestBody PasswordChangeRequest request,
            Authentication authentication) {

        String currentUserNo = (String) authentication.getPrincipal();
        log.info("PUT /api/v1/users/{}/password - by: {}", userNo, currentUserNo);
        userManageService.changePassword(userNo, request);
        return ApiResponse.success();
    }

    /**
     * Reset user password (admin-initiated)
     *
     * Legacy: POST /resetPwd
     * New: PUT /api/v1/users/{userNo}/reset-password
     */
    @Operation(summary = "Reset password", description = "Reset user password to default (admin only)")
    @PutMapping("/{userNo}/reset-password")
    public ApiResponse<Void> resetPassword(
            @Parameter(description = "User number", example = "1") @PathVariable String userNo,
            Authentication authentication) {

        String adminUserNo = (String) authentication.getPrincipal();
        log.info("PUT /api/v1/users/{}/reset-password - by admin: {}", userNo, adminUserNo);
        userManageService.resetPassword(userNo, adminUserNo);
        return ApiResponse.success();
    }

    /**
     * Lock user account
     *
     * Legacy: POST /lockUser
     * New: PUT /api/v1/users/{userNo}/lock
     */
    @Operation(summary = "Lock user", description = "Lock a user account")
    @PutMapping("/{userNo}/lock")
    public ApiResponse<Void> lockUser(
            @Parameter(description = "User number", example = "1") @PathVariable String userNo) {

        log.info("PUT /api/v1/users/{}/lock", userNo);
        userManageService.lockUser(userNo);
        return ApiResponse.success();
    }

    /**
     * Unlock user account
     *
     * Legacy: POST /unlockUser
     * New: PUT /api/v1/users/{userNo}/unlock
     */
    @Operation(summary = "Unlock user", description = "Unlock a user account")
    @PutMapping("/{userNo}/unlock")
    public ApiResponse<Void> unlockUser(
            @Parameter(description = "User number", example = "1") @PathVariable String userNo) {

        log.info("PUT /api/v1/users/{}/unlock", userNo);
        userManageService.unlockUser(userNo);
        return ApiResponse.success();
    }

    /**
     * Get team list
     *
     * Legacy: GET /teamList
     * New: GET /api/v1/users/teams
     */
    @Operation(summary = "Get team list", description = "Get all teams")
    @GetMapping("/teams")
    public ApiResponse<List<Map<String, Object>>> getTeamList() {

        log.debug("GET /api/v1/users/teams");
        List<Map<String, Object>> result = userManageService.getTeamList();
        return ApiResponse.success(result);
    }

    /**
     * Create a new team
     *
     * Legacy: POST /teamInsert
     * New: POST /api/v1/users/teams
     */
    @Operation(summary = "Create team", description = "Create a new team")
    @PostMapping("/teams")
    public ApiResponse<Void> createTeam(
            @Parameter(description = "Team code", example = "TEAM001") @RequestParam String teamCode,
            @Parameter(description = "Team name", example = "개발팀") @RequestParam String teamName,
            Authentication authentication) {

        String userNo = (String) authentication.getPrincipal();
        log.info("POST /api/v1/users/teams - teamCode: {}, by: {}", teamCode, userNo);
        userManageService.createTeam(teamCode, teamName, userNo);
        return ApiResponse.success();
    }

    /**
     * Get user activity log list
     *
     * Legacy: GET /userLogList
     * New: GET /api/v1/users/logs
     */
    @Operation(summary = "Get user log list", description = "Get paginated user activity logs")
    @GetMapping("/logs")
    public ApiResponse<PageResponse<UserLogResponse>> getUserLogList(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Search field (userId, logType)", example = "userId") @RequestParam(required = false) String searchType,
            @Parameter(description = "Search value", example = "admin") @RequestParam(required = false) String searchKeyword) {

        log.debug("GET /api/v1/users/logs - page: {}, size: {}, searchType: {}, searchKeyword: {}", page, size, searchType, searchKeyword);
        PageResponse<UserLogResponse> result = userManageService.getUserLogList(page, size, searchType, searchKeyword);
        return ApiResponse.success(result);
    }

    /**
     * Get account policy
     *
     * Legacy: GET /getAccountPolicy
     * New: GET /api/v1/users/account-policy
     */
    @Operation(summary = "Get account policy", description = "Get current account security policy")
    @GetMapping("/account-policy")
    public ApiResponse<Map<String, Object>> getAccountPolicy() {

        log.debug("GET /api/v1/users/account-policy");
        Map<String, Object> result = userManageService.getAccountPolicy();
        return ApiResponse.success(result);
    }

    /**
     * Save account policy
     *
     * Legacy: POST /saveAccountPolicy
     * New: PUT /api/v1/users/account-policy
     */
    @Operation(summary = "Save account policy", description = "Create or update account security policy")
    @PutMapping("/account-policy")
    public ApiResponse<Void> saveAccountPolicy(
            @Valid @RequestBody AccountPolicyRequest request) {

        log.info("PUT /api/v1/users/account-policy");
        userManageService.saveAccountPolicy(request);
        return ApiResponse.success();
    }
}
