package com.org.iopts.controller;

import com.org.iopts.dto.response.ApiResponse;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.service.RemediationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Tag(name = "Remediation", description = "Remediation Management API")
@RestController
@RequestMapping("/api/v1/remediation")
@RequiredArgsConstructor
public class RemediationController {

    private final RemediationService remediationService;

    @Operation(summary = "Get remediation list", description = "Get paginated remediation history")
    @GetMapping
    public ApiResponse<PageResponse<Map<String, Object>>> getRemediationList(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Search keyword") @RequestParam(required = false) String keyword,
            @Parameter(description = "Status filter") @RequestParam(required = false) String status) {
        log.info("Get remediation list: page={}, size={}", page, size);
        PageResponse<Map<String, Object>> response = remediationService.getRemediationList(page, size, keyword, status);
        return ApiResponse.success(response);
    }

    @Operation(summary = "Get remediation detail", description = "Get remediation detail")
    @GetMapping("/{remediationId}")
    public ApiResponse<Map<String, Object>> getRemediationDetail(
            @PathVariable Long remediationId) {
        log.info("Get remediation detail: {}", remediationId);
        Map<String, Object> response = remediationService.getRemediationDetail(remediationId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "Process remediation", description = "Apply remediation action (delete/encrypt/move)")
    @PostMapping("/{findId}/process")
    public ApiResponse<Void> processRemediation(
            @PathVariable String findId,
            @RequestBody Map<String, Object> request,
            Authentication authentication) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Process remediation: findId={}, by user: {}", findId, userNo);
        remediationService.processRemediation(findId, request, userNo);
        return ApiResponse.success();
    }

    @Operation(summary = "Get remediation summary", description = "Get remediation summary statistics")
    @GetMapping("/summary")
    public ApiResponse<Map<String, Object>> getRemediationSummary() {
        log.info("Get remediation summary");
        Map<String, Object> response = remediationService.getRemediationSummary();
        return ApiResponse.success(response);
    }

    @Operation(summary = "Batch remediation", description = "Apply remediation to multiple findings")
    @PostMapping("/batch")
    public ApiResponse<Map<String, Object>> batchRemediation(
            @RequestBody Map<String, Object> request,
            Authentication authentication) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Batch remediation by user: {}", userNo);
        Map<String, Object> response = remediationService.batchRemediation(request, userNo);
        return ApiResponse.success(response);
    }
}
