package com.org.iopts.controller;

import com.org.iopts.dto.response.ApiResponse;
import com.org.iopts.dto.response.GroupTreeResponse;
import com.org.iopts.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Group Controller
 *
 * Provides REST API endpoints for group tree retrieval, statistics,
 * and target group reassignment.
 *
 * Legacy endpoint mappings:
 *   GET  /api/v1/groups/tree               <- /groupTree
 *   GET  /api/v1/groups/{groupId}/stats    <- /groupStats
 *   PUT  /api/v1/groups/{groupId}/targets  <- /moveTargetGroup
 */
@Slf4j
@Tag(name = "Group", description = "Group API")
@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    /**
     * Get group tree
     *
     * Legacy: GET /groupTree
     * New: GET /api/v1/groups (alias: /tree)
     */
    @Operation(summary = "Get group tree", description = "Get hierarchical group tree with target counts")
    @GetMapping({"", "/tree"})
    public ApiResponse<List<GroupTreeResponse>> getGroupTree() {

        log.debug("GET /api/v1/groups/tree");
        List<GroupTreeResponse> result = groupService.getGroupTree();
        return ApiResponse.success(result);
    }

    /**
     * Get group statistics
     *
     * Legacy: GET /groupStats
     * New: GET /api/v1/groups/{groupId}/stats
     */
    @Operation(summary = "Get group stats", description = "Get group statistics including target counts and status breakdown")
    @GetMapping("/{groupId}/stats")
    public ApiResponse<Map<String, Object>> getGroupStats(
            @Parameter(description = "Group ID", example = "GRP001") @PathVariable String groupId) {

        log.debug("GET /api/v1/groups/{}/stats", groupId);
        Map<String, Object> result = groupService.getGroupStats(groupId);
        return ApiResponse.success(result);
    }

    /**
     * Move targets to group
     *
     * Legacy: POST /moveTargetGroup
     * New: PUT /api/v1/groups/{groupId}/targets
     */
    @Operation(summary = "Move targets to group", description = "Reassign targets to a different group")
    @PutMapping("/{groupId}/targets")
    public ApiResponse<Void> moveTargetsToGroup(
            @Parameter(description = "Target group ID", example = "GRP001") @PathVariable String groupId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "이동할 대상 ID 목록",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "array", example = "[1, 2, 3]")
                    )
            )
            @RequestBody List<Long> targetIds) {

        log.info("PUT /api/v1/groups/{}/targets - count: {}", groupId, targetIds.size());
        groupService.moveTargetsToGroup(groupId, targetIds);
        return ApiResponse.success();
    }
}
