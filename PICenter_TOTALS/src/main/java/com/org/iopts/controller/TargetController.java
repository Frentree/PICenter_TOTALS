package com.org.iopts.controller;

import com.org.iopts.dto.request.DmzRequest;
import com.org.iopts.dto.request.TargetSearchRequest;
import com.org.iopts.dto.request.TargetUserRequest;
import com.org.iopts.dto.response.ApiResponse;
import com.org.iopts.dto.response.DmzResponse;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.dto.response.TargetResponse;
import com.org.iopts.dto.response.UserResponse;
import com.org.iopts.service.TargetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Target Management Controller
 * Migrated from legacy TargetController
 */
@Slf4j
@Tag(name = "Target Management", description = "Target Management API")
@RestController
@RequestMapping("/api/v1/targets")
@RequiredArgsConstructor
public class TargetController {

    private final TargetService targetService;

    /**
     * Get target list with search and pagination
     *
     * Legacy: POST /getTargetList
     * New: GET /api/v1/targets
     */
    @Operation(summary = "Get target list", description = "Search targets with filters and pagination")
    @GetMapping
    public ApiResponse<PageResponse<TargetResponse>> getTargetList(
            @ModelAttribute TargetSearchRequest request) {
        log.info("Get target list: searchType={}, keyword={}", request.getSearchType(), request.getSearchKeyword());
        PageResponse<TargetResponse> response = targetService.getTargetList(request);
        return ApiResponse.success(response);
    }

    /**
     * Get target detail
     *
     * Legacy: POST /getTargetDetail
     * New: GET /api/v1/targets/{targetId}
     */
    @Operation(summary = "Get target detail", description = "Get target detail by target ID")
    @GetMapping("/{targetId}")
    public ApiResponse<TargetResponse> getTargetDetail(
            @Parameter(description = "Target ID", example = "1") @PathVariable Long targetId) {
        log.info("Get target detail: targetId={}", targetId);
        TargetResponse response = targetService.getTargetDetail(targetId);
        return ApiResponse.success(response);
    }

    /**
     * Get users assigned to a target
     *
     * Legacy: POST /getTargetUserList
     * New: GET /api/v1/targets/{targetId}/users
     */
    @Operation(summary = "Get target users", description = "Get users assigned to a target")
    @GetMapping("/{targetId}/users")
    public ApiResponse<List<UserResponse>> getTargetUsers(
            @Parameter(description = "Target ID", example = "1") @PathVariable Long targetId) {
        log.info("Get target users: targetId={}", targetId);
        List<UserResponse> response = targetService.getTargetUsers(targetId);
        return ApiResponse.success(response);
    }

    /**
     * Assign user to target
     *
     * Legacy: POST /addTargetUser
     * New: POST /api/v1/targets/{targetId}/users
     */
    @Operation(summary = "Assign user to target", description = "Assign a user to a target")
    @PostMapping("/{targetId}/users")
    public ApiResponse<Void> assignUserToTarget(
            @Parameter(description = "Target ID", example = "1") @PathVariable Long targetId,
            @Valid @RequestBody TargetUserRequest request,
            Authentication authentication) {
        String regUserNo = (String) authentication.getPrincipal();
        log.info("Assign user to target: targetId={}, userNo={}", targetId, request.getUserNo());
        targetService.assignUserToTarget(targetId, request.getUserNo(), regUserNo);
        return ApiResponse.success();
    }

    /**
     * Unassign user from target
     *
     * Legacy: POST /deleteTargetUser
     * New: DELETE /api/v1/targets/{targetId}/users/{userNo}
     */
    @Operation(summary = "Unassign user from target", description = "Remove a user assignment from a target")
    @DeleteMapping("/{targetId}/users/{userNo}")
    public ApiResponse<Void> unassignUserFromTarget(
            @Parameter(description = "Target ID", example = "1") @PathVariable Long targetId,
            @Parameter(description = "User number", example = "1") @PathVariable String userNo) {
        log.info("Unassign user from target: targetId={}, userNo={}", targetId, userNo);
        targetService.unassignUserFromTarget(targetId, userNo);
        return ApiResponse.success();
    }

    /**
     * Get server list
     *
     * Legacy: POST /getServerList
     * New: GET /api/v1/targets/servers
     */
    @Operation(summary = "Get server list", description = "Get paginated server list")
    @GetMapping("/servers")
    public ApiResponse<PageResponse<TargetResponse>> getServerList(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size) {
        log.info("Get server list: page={}, size={}", page, size);
        PageResponse<TargetResponse> response = targetService.getServerList(page, size);
        return ApiResponse.success(response);
    }

    /**
     * Get top files for a server
     *
     * Legacy: POST /getServerTopFiles
     * New: GET /api/v1/targets/servers/{targetId}/top-files
     */
    @Operation(summary = "Get server top files", description = "Get top detection files for a server target")
    @GetMapping("/servers/{targetId}/top-files")
    public ApiResponse<List<Map<String, Object>>> getServerTopFiles(
            @Parameter(description = "Target ID", example = "1") @PathVariable Long targetId,
            @Parameter(description = "Top N count", example = "10") @RequestParam(defaultValue = "10") int topN) {
        log.info("Get server top files: targetId={}, topN={}", targetId, topN);
        List<Map<String, Object>> response = targetService.getServerTopFiles(targetId, topN);
        return ApiResponse.success(response);
    }

    /**
     * Get DMZ list
     *
     * Legacy: POST /getDmzList
     * New: GET /api/v1/targets/dmz
     */
    @Operation(summary = "Get DMZ list", description = "Get all DMZ entries")
    @GetMapping("/dmz")
    public ApiResponse<List<DmzResponse>> getDmzList() {
        log.info("Get DMZ list");
        List<DmzResponse> response = targetService.getDmzList();
        return ApiResponse.success(response);
    }

    /**
     * Save DMZ entry
     *
     * Legacy: POST /saveDmz
     * New: POST /api/v1/targets/dmz
     */
    @Operation(summary = "Save DMZ entry", description = "Create a new DMZ entry")
    @PostMapping("/dmz")
    public ApiResponse<Void> saveDmz(
            @Valid @RequestBody DmzRequest request,
            Authentication authentication) {
        String regUserNo = (String) authentication.getPrincipal();
        log.info("Save DMZ: ip={}, memo={}", request.getDmzIp(), request.getMemo());
        targetService.saveDmz(request, regUserNo);
        return ApiResponse.success();
    }

    /**
     * Delete DMZ entries
     *
     * Legacy: POST /deleteDmz
     * New: DELETE /api/v1/targets/dmz
     */
    @Operation(summary = "Delete DMZ entries", description = "Delete DMZ entries by IDs")
    @DeleteMapping("/dmz")
    public ApiResponse<Void> deleteDmz(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "삭제할 DMZ ID 목록",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "array", example = "[1, 2, 3]")
                    )
            )
            @RequestBody List<Long> dmzIds) {
        log.info("Delete DMZ: ids={}", dmzIds);
        targetService.deleteDmz(dmzIds);
        return ApiResponse.success();
    }

    /**
     * Get group list
     *
     * Legacy: POST /getGroupList
     * New: GET /api/v1/targets/groups
     */
    @Operation(summary = "Get group list", description = "Get group tree structure")
    @GetMapping("/groups")
    public ApiResponse<List<Map<String, Object>>> getGroupList() {
        log.info("Get group list");
        List<Map<String, Object>> response = targetService.getGroupList();
        return ApiResponse.success(response);
    }

    /**
     * Add group
     *
     * Legacy: POST /addGroup
     * New: POST /api/v1/targets/groups
     */
    @Operation(summary = "Add group", description = "Add a new group")
    @PostMapping("/groups")
    public ApiResponse<Void> addGroup(
            @Parameter(description = "Group name", example = "개발팀 서버") @RequestParam String groupName,
            @Parameter(description = "Parent group ID", example = "GRP001") @RequestParam(required = false) String parentId,
            Authentication authentication) {
        String regUserNo = (String) authentication.getPrincipal();
        log.info("Add group: name={}, parentId={}", groupName, parentId);
        targetService.addGroup(groupName, parentId, regUserNo);
        return ApiResponse.success();
    }

    /**
     * Update group
     *
     * Legacy: POST /updateGroup
     * New: PUT /api/v1/targets/groups/{groupId}
     */
    @Operation(summary = "Update group", description = "Update group name")
    @PutMapping("/groups/{groupId}")
    public ApiResponse<Void> updateGroup(
            @Parameter(description = "Group ID", example = "GRP001") @PathVariable String groupId,
            @Parameter(description = "Group name", example = "개발팀 서버") @RequestParam String groupName) {
        log.info("Update group: groupId={}, name={}", groupId, groupName);
        targetService.updateGroup(groupId, groupName);
        return ApiResponse.success();
    }

    /**
     * Delete group
     *
     * Legacy: POST /deleteGroup
     * New: DELETE /api/v1/targets/groups/{groupId}
     */
    @Operation(summary = "Delete group", description = "Delete a group")
    @DeleteMapping("/groups/{groupId}")
    public ApiResponse<Void> deleteGroup(
            @Parameter(description = "Group ID", example = "GRP001") @PathVariable String groupId) {
        log.info("Delete group: groupId={}", groupId);
        targetService.deleteGroup(groupId);
        return ApiResponse.success();
    }

    /**
     * Get agent version list
     *
     * Legacy: POST /getAgentVersionList
     * New: GET /api/v1/targets/versions (alias: /agent-versions)
     */
    @Operation(summary = "Get agent versions", description = "Get list of agent versions")
    @GetMapping({"/versions", "/agent-versions"})
    public ApiResponse<List<Map<String, Object>>> getAgentVersionList() {
        log.info("Get agent version list");
        List<Map<String, Object>> response = targetService.getAgentVersionList();
        return ApiResponse.success(response);
    }

    /**
     * Get exception list
     *
     * Legacy: POST /getExceptionList
     * New: GET /api/v1/targets/exceptions
     */
    @Operation(summary = "Get exception list", description = "Get paginated exception list")
    @GetMapping("/exceptions")
    public ApiResponse<PageResponse<Map<String, Object>>> getExceptionList(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size) {
        log.info("Get exception list: page={}, size={}", page, size);
        PageResponse<Map<String, Object>> response = targetService.getExceptionList(page, size);
        return ApiResponse.success(response);
    }
}
