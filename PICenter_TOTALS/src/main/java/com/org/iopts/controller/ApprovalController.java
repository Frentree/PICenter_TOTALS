package com.org.iopts.controller;

import com.org.iopts.dto.request.ApprovalRequest;
import com.org.iopts.dto.response.ApiResponse;
import com.org.iopts.dto.response.ApprovalResponse;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.service.ApprovalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Approval Management Controller
 * Migrated from legacy approval handling in piDetectionListController
 */
@Slf4j
@Tag(name = "Approval Management", description = "PI Approval Management API")
@RestController
@RequestMapping("/api/v1/approvals")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    /**
     * Get Approval List
     *
     * Legacy: POST /getApprovalList
     * New: GET /api/v1/approvals
     */
    @Operation(summary = "Get approval list", description = "Get paginated approval list with optional status filter")
    @GetMapping
    public ApiResponse<PageResponse<ApprovalResponse>> getApprovalList(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Approval status filter", example = "PENDING") @RequestParam(required = false) String status) {

        log.info("Get approval list - page: {}, size: {}, status: {}", page, size, status);
        PageResponse<ApprovalResponse> response = approvalService.getApprovalList(page, size, status);
        return ApiResponse.success(response);
    }

    /**
     * Approve Request
     *
     * Legacy: POST /approveRequest
     * New: PUT /api/v1/approvals/{approvalId}/approve
     */
    @Operation(summary = "Approve request", description = "Approve a pending approval request")
    @PutMapping("/{approvalId}/approve")
    public ApiResponse<Void> approve(
            @Parameter(description = "Approval ID", example = "1") @PathVariable Long approvalId,
            @RequestBody ApprovalRequest request,
            Authentication authentication) {

        String userNo = (String) authentication.getPrincipal();
        log.info("Approve request - approvalId: {}, userNo: {}", approvalId, userNo);

        approvalService.approve(approvalId, request, userNo);
        return ApiResponse.success();
    }

    /**
     * Reject Request
     *
     * Legacy: POST /rejectRequest
     * New: PUT /api/v1/approvals/{approvalId}/reject
     */
    @Operation(summary = "Reject request", description = "Reject a pending approval request")
    @PutMapping("/{approvalId}/reject")
    public ApiResponse<Void> reject(
            @Parameter(description = "Approval ID", example = "1") @PathVariable Long approvalId,
            @RequestBody ApprovalRequest request,
            Authentication authentication) {

        String userNo = (String) authentication.getPrincipal();
        log.info("Reject request - approvalId: {}, userNo: {}", approvalId, userNo);

        approvalService.reject(approvalId, request, userNo);
        return ApiResponse.success();
    }

    /**
     * Cancel Request
     *
     * Legacy: POST /cancelRequest
     * New: PUT /api/v1/approvals/{approvalId}/cancel
     */
    @Operation(summary = "Cancel request", description = "Cancel a pending approval request")
    @PutMapping("/{approvalId}/cancel")
    public ApiResponse<Void> cancel(
            @Parameter(description = "Approval ID", example = "1") @PathVariable Long approvalId,
            Authentication authentication) {

        String userNo = (String) authentication.getPrincipal();
        log.info("Cancel request - approvalId: {}, userNo: {}", approvalId, userNo);

        approvalService.cancel(approvalId, userNo);
        return ApiResponse.success();
    }
}
