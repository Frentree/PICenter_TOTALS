package com.org.iopts.controller;

import com.org.iopts.dto.request.PolicyRequest;
import com.org.iopts.dto.request.ScheduleCreateRequest;
import com.org.iopts.dto.response.ApiResponse;
import com.org.iopts.dto.response.LocationResponse;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.dto.response.ScheduleResponse;
import com.org.iopts.service.ScanService;
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
 * Scan Management Controller
 * Migrated from legacy ScanController
 */
@Slf4j
@Tag(name = "Scan Management", description = "Scan Management API")
@RestController
@RequestMapping("/api/v1/scans")
@RequiredArgsConstructor
public class ScanController {

    private final ScanService scanService;

    /**
     * Get schedule list
     *
     * Legacy: POST /getScheduleList
     * New: GET /api/v1/scans/schedules
     */
    @Operation(summary = "Get schedule list", description = "Get paginated schedule list")
    @GetMapping("/schedules")
    public ApiResponse<PageResponse<ScheduleResponse>> getScheduleList(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size) {
        log.info("Get schedule list: page={}, size={}", page, size);
        PageResponse<ScheduleResponse> response = scanService.getScheduleList(page, size);
        return ApiResponse.success(response);
    }

    /**
     * Get schedule detail
     *
     * Legacy: POST /getScheduleDetail
     * New: GET /api/v1/scans/schedules/{scheduleId}
     */
    @Operation(summary = "Get schedule detail", description = "Get schedule detail by schedule ID")
    @GetMapping("/schedules/{scheduleId}")
    public ApiResponse<ScheduleResponse> getScheduleDetail(
            @Parameter(description = "Schedule ID", example = "sched01") @PathVariable String scheduleId) {
        log.info("Get schedule detail: scheduleId={}", scheduleId);
        ScheduleResponse response = scanService.getScheduleDetail(scheduleId);
        return ApiResponse.success(response);
    }

    /**
     * Create schedule
     *
     * Legacy: POST /addSchedule
     * New: POST /api/v1/scans/schedules
     */
    @Operation(summary = "Create schedule", description = "Create a new scan schedule")
    @PostMapping("/schedules")
    public ApiResponse<Void> createSchedule(
            @Valid @RequestBody ScheduleCreateRequest request,
            Authentication authentication) {
        String regUserNo = (String) authentication.getPrincipal();
        log.info("Create schedule: name={}", request.getScheduleName());
        scanService.createSchedule(request, regUserNo);
        return ApiResponse.success();
    }

    /**
     * Update schedule
     *
     * Legacy: POST /updateSchedule
     * New: PUT /api/v1/scans/schedules/{scheduleId}
     */
    @Operation(summary = "Update schedule", description = "Update an existing scan schedule")
    @PutMapping("/schedules/{scheduleId}")
    public ApiResponse<Void> updateSchedule(
            @Parameter(description = "Schedule ID", example = "sched01") @PathVariable String scheduleId,
            @Valid @RequestBody ScheduleCreateRequest request,
            Authentication authentication) {
        String modUserNo = (String) authentication.getPrincipal();
        log.info("Update schedule: scheduleId={}", scheduleId);
        scanService.updateSchedule(scheduleId, request, modUserNo);
        return ApiResponse.success();
    }

    /**
     * Delete schedule
     *
     * Legacy: POST /deleteSchedule
     * New: DELETE /api/v1/scans/schedules/{scheduleId}
     */
    @Operation(summary = "Delete schedule", description = "Delete a scan schedule")
    @DeleteMapping("/schedules/{scheduleId}")
    public ApiResponse<Void> deleteSchedule(
            @Parameter(description = "Schedule ID", example = "sched01") @PathVariable String scheduleId) {
        log.info("Delete schedule: scheduleId={}", scheduleId);
        scanService.deleteSchedule(scheduleId);
        return ApiResponse.success();
    }

    /**
     * Change schedule status
     *
     * Legacy: POST /changeScheduleStatus
     * New: PATCH /api/v1/scans/schedules/{scheduleId}/status
     */
    @Operation(summary = "Change schedule status", description = "Enable or disable a scan schedule")
    @PatchMapping("/schedules/{scheduleId}/status")
    public ApiResponse<Void> changeScheduleStatus(
            @Parameter(description = "Schedule ID", example = "sched01") @PathVariable String scheduleId,
            @Parameter(description = "Status Y/N", example = "Y") @RequestParam Character statusYn) {
        log.info("Change schedule status: scheduleId={}, statusYn={}", scheduleId, statusYn);
        scanService.changeScheduleStatus(scheduleId, statusYn);
        return ApiResponse.success();
    }

    /**
     * Execute schedule immediately
     *
     * Legacy: POST /executeSchedule
     * New: POST /api/v1/scans/schedules/{scheduleId}/execute
     */
    @Operation(summary = "Execute schedule", description = "Execute a scan schedule immediately")
    @PostMapping("/schedules/{scheduleId}/execute")
    public ApiResponse<Void> executeSchedule(
            @Parameter(description = "Schedule ID", example = "sched01") @PathVariable String scheduleId,
            Authentication authentication) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Execute schedule: scheduleId={}", scheduleId);
        scanService.executeSchedule(scheduleId, userNo);
        return ApiResponse.success();
    }

    /**
     * Get scan history
     *
     * Legacy: POST /getScanHistory
     * New: GET /api/v1/scans/history
     */
    @Operation(summary = "Get scan history", description = "Get paginated scan history")
    @GetMapping("/history")
    public ApiResponse<PageResponse<Map<String, Object>>> getScanHistory(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Target ID filter", example = "1") @RequestParam(required = false) String targetId) {
        log.info("Get scan history: page={}, size={}, targetId={}", page, size, targetId);
        PageResponse<Map<String, Object>> response = scanService.getScanHistory(page, size, targetId);
        return ApiResponse.success(response);
    }

    /**
     * Get location list
     *
     * Legacy: POST /getLocationList
     * New: GET /api/v1/scans/locations
     */
    @Operation(summary = "Get location list", description = "Get all scan locations")
    @GetMapping("/locations")
    public ApiResponse<List<LocationResponse>> getLocationList() {
        log.info("Get location list");
        List<LocationResponse> response = scanService.getLocationList();
        return ApiResponse.success(response);
    }

    /**
     * Get datatype list
     *
     * Legacy: POST /getDatatypeList
     * New: GET /api/v1/scans/datatypes
     */
    @Operation(summary = "Get datatype list", description = "Get all data types")
    @GetMapping("/datatypes")
    public ApiResponse<List<Map<String, Object>>> getDatatypeList() {
        log.info("Get datatype list");
        List<Map<String, Object>> response = scanService.getDatatypeList();
        return ApiResponse.success(response);
    }

    /**
     * Get policy list
     *
     * Legacy: POST /getPolicyList
     * New: GET /api/v1/scans/policies
     */
    @Operation(summary = "Get policy list", description = "Get paginated policy list")
    @GetMapping("/policies")
    public ApiResponse<PageResponse<Map<String, Object>>> getPolicyList(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size) {
        log.info("Get policy list: page={}, size={}", page, size);
        PageResponse<Map<String, Object>> response = scanService.getPolicyList(page, size);
        return ApiResponse.success(response);
    }

    /**
     * Create policy
     *
     * Legacy: POST /addPolicy
     * New: POST /api/v1/scans/policies
     */
    @Operation(summary = "Create policy", description = "Create a new scan policy")
    @PostMapping("/policies")
    public ApiResponse<Void> createPolicy(
            @Valid @RequestBody PolicyRequest request,
            Authentication authentication) {
        String regUserNo = (String) authentication.getPrincipal();
        log.info("Create policy: name={}", request.getPolicyName());
        scanService.createPolicy(request, regUserNo);
        return ApiResponse.success();
    }

    /**
     * Update policy
     *
     * Legacy: POST /updatePolicy
     * New: PUT /api/v1/scans/policies/{policyId}
     */
    @Operation(summary = "Update policy", description = "Update an existing scan policy")
    @PutMapping("/policies/{policyId}")
    public ApiResponse<Void> updatePolicy(
            @Parameter(description = "Policy ID", example = "1") @PathVariable Long policyId,
            @Valid @RequestBody PolicyRequest request,
            Authentication authentication) {
        String modUserNo = (String) authentication.getPrincipal();
        log.info("Update policy: policyId={}", policyId);
        scanService.updatePolicy(policyId, request, modUserNo);
        return ApiResponse.success();
    }

    /**
     * Delete policy
     *
     * Legacy: POST /deletePolicy
     * New: DELETE /api/v1/scans/policies/{policyId}
     */
    @Operation(summary = "Delete policy", description = "Delete a scan policy")
    @DeleteMapping("/policies/{policyId}")
    public ApiResponse<Void> deletePolicy(
            @Parameter(description = "Policy ID", example = "1") @PathVariable Long policyId) {
        log.info("Delete policy: policyId={}", policyId);
        scanService.deletePolicy(policyId);
        return ApiResponse.success();
    }
}
