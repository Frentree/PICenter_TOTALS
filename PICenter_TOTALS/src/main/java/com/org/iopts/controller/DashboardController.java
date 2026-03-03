package com.org.iopts.controller;

import com.org.iopts.dto.response.ApiResponse;
import com.org.iopts.dto.response.ChartDataResponse;
import com.org.iopts.dto.response.DashboardResponse;
import com.org.iopts.dto.response.SystemCurrentResponse;
import com.org.iopts.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Dashboard Controller
 *
 * Migrated from legacy HomeController dashboard endpoints
 * and Pi_DashService related JSP controller logic.
 *
 * Legacy: /dash/*, /dashboard/*
 * New: /api/v1/dashboard/*
 */
@Slf4j
@Tag(name = "Dashboard", description = "Dashboard API")
@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * Get Dashboard Info
     *
     * Legacy: POST /dash/getDashInfo
     * New: GET /api/v1/dashboard/info
     */
    @Operation(summary = "Get dashboard info", description = "Get aggregated dashboard information with optional filters")
    @GetMapping("/info")
    public ApiResponse<DashboardResponse> getDashboardInfo(
            Authentication authentication,
            @Parameter(description = "Group ID filter", example = "GRP001") @RequestParam(required = false) String groupId,
            @Parameter(description = "Start date (yyyy-MM-dd)", example = "2026-01-01") @RequestParam(required = false) String startDate,
            @Parameter(description = "End date (yyyy-MM-dd)", example = "2026-12-31") @RequestParam(required = false) String endDate) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Dashboard info request by user: {}", userNo);

        DashboardResponse response = dashboardService.getDashboardInfo(groupId, startDate, endDate);
        return ApiResponse.success(response);
    }

    /**
     * Get Datatype Status
     *
     * Legacy: POST /dash/getDatatypeStatus
     * New: GET /api/v1/dashboard/datatypes
     */
    @Operation(summary = "Get datatype status", description = "Get detection counts grouped by datatype")
    @GetMapping("/datatypes")
    public ApiResponse<List<Map<String, Object>>> getDatatypeStatus(
            Authentication authentication,
            @Parameter(description = "Group ID filter", example = "GRP001") @RequestParam(required = false) String groupId) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Datatype status request by user: {}", userNo);

        List<Map<String, Object>> response = dashboardService.getDatatypeStatus(groupId);
        return ApiResponse.success(response);
    }

    /**
     * Get System Current Status
     *
     * Legacy: POST /dash/getSystemCurrent
     * New: GET /api/v1/dashboard/system-current
     */
    @Operation(summary = "Get system current status", description = "Get current system status including agents and scans")
    @GetMapping("/system-current")
    public ApiResponse<SystemCurrentResponse> getSystemCurrent(Authentication authentication) {
        String userNo = (String) authentication.getPrincipal();
        log.info("System current request by user: {}", userNo);

        SystemCurrentResponse response = dashboardService.getSystemCurrent();
        return ApiResponse.success(response);
    }

    /**
     * Get Path Current Status
     *
     * Legacy: POST /dash/getPathCurrent
     * New: GET /api/v1/dashboard/path-current
     */
    @Operation(summary = "Get path current status", description = "Get detection path current status")
    @GetMapping("/path-current")
    public ApiResponse<List<Map<String, Object>>> getPathCurrent(
            Authentication authentication,
            @Parameter(description = "Group ID filter", example = "GRP001") @RequestParam(required = false) String groupId) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Path current request by user: {}", userNo);

        List<Map<String, Object>> response = dashboardService.getPathCurrent(groupId);
        return ApiResponse.success(response);
    }

    /**
     * Get Dashboard Detections
     *
     * Legacy: POST /dash/getDashDetections
     * New: GET /api/v1/dashboard/detections
     */
    @Operation(summary = "Get dashboard detections", description = "Get recent detections for dashboard display")
    @GetMapping("/detections")
    public ApiResponse<List<Map<String, Object>>> getDashboardDetections(
            Authentication authentication,
            @Parameter(description = "Group ID filter", example = "GRP001") @RequestParam(required = false) String groupId,
            @Parameter(description = "Maximum number of results", example = "10") @RequestParam(defaultValue = "10") int limit) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Dashboard detections request by user: {}, limit: {}", userNo, limit);

        List<Map<String, Object>> response = dashboardService.getDashboardDetections(groupId, limit);
        return ApiResponse.success(response);
    }

    /**
     * Get Pie Chart Data
     *
     * Legacy: POST /dash/getPieChart
     * New: GET /api/v1/dashboard/charts/pie
     */
    @Operation(summary = "Get pie chart data", description = "Get detection breakdown by category for pie chart")
    @GetMapping("/charts/pie")
    public ApiResponse<ChartDataResponse> getPieChartData(
            Authentication authentication,
            @Parameter(description = "Group ID filter", example = "GRP001") @RequestParam(required = false) String groupId,
            @Parameter(description = "Start date (yyyy-MM-dd)", example = "2026-01-01") @RequestParam(required = false) String startDate,
            @Parameter(description = "End date (yyyy-MM-dd)", example = "2026-12-31") @RequestParam(required = false) String endDate) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Pie chart data request by user: {}", userNo);

        ChartDataResponse response = dashboardService.getPieChartData(groupId, startDate, endDate);
        return ApiResponse.success(response);
    }

    /**
     * Get Bar Chart Data
     *
     * Legacy: POST /dash/getBarChart
     * New: GET /api/v1/dashboard/charts/bar
     */
    @Operation(summary = "Get bar chart data", description = "Get detection trend over time for bar chart")
    @GetMapping("/charts/bar")
    public ApiResponse<ChartDataResponse> getBarChartData(
            Authentication authentication,
            @Parameter(description = "Group ID filter", example = "GRP001") @RequestParam(required = false) String groupId,
            @Parameter(description = "Start date (yyyy-MM-dd)", example = "2026-01-01") @RequestParam(required = false) String startDate,
            @Parameter(description = "End date (yyyy-MM-dd)", example = "2026-12-31") @RequestParam(required = false) String endDate) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Bar chart data request by user: {}", userNo);

        ChartDataResponse response = dashboardService.getBarChartData(groupId, startDate, endDate);
        return ApiResponse.success(response);
    }

    /**
     * Get Grid Data
     *
     * Legacy: POST /dash/getGridData
     * New: GET /api/v1/dashboard/charts/grid
     */
    @Operation(summary = "Get grid data", description = "Get dashboard table grid data")
    @GetMapping("/charts/grid")
    public ApiResponse<List<Map<String, Object>>> getGridData(
            Authentication authentication,
            @Parameter(description = "Group ID filter", example = "GRP001") @RequestParam(required = false) String groupId,
            @Parameter(description = "Start date (yyyy-MM-dd)", example = "2026-01-01") @RequestParam(required = false) String startDate,
            @Parameter(description = "End date (yyyy-MM-dd)", example = "2026-12-31") @RequestParam(required = false) String endDate) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Grid data request by user: {}", userNo);

        List<Map<String, Object>> response = dashboardService.getGridData(groupId, startDate, endDate);
        return ApiResponse.success(response);
    }

    /**
     * Get Ranking Data
     *
     * Legacy: POST /dash/getRankingData
     * New: GET /api/v1/dashboard/rankings
     */
    @Operation(summary = "Get ranking data", description = "Get top targets by detection count")
    @GetMapping("/rankings")
    public ApiResponse<List<Map<String, Object>>> getRankingData(
            Authentication authentication,
            @Parameter(description = "Group ID filter", example = "GRP001") @RequestParam(required = false) String groupId,
            @Parameter(description = "Maximum number of results", example = "10") @RequestParam(defaultValue = "10") int limit) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Ranking data request by user: {}, limit: {}", userNo, limit);

        List<Map<String, Object>> response = dashboardService.getRankingData(groupId, limit);
        return ApiResponse.success(response);
    }

    /**
     * Get System Agents Detail
     * New: GET /api/v1/dashboard/system-agents
     */
    @Operation(summary = "Get system agents detail", description = "Get detailed agent status including connected/disconnected/total counts")
    @GetMapping("/system-agents")
    public ApiResponse<Map<String, Object>> getSystemAgents(Authentication authentication) {
        String userNo = (String) authentication.getPrincipal();
        log.info("System agents request by user: {}", userNo);
        Map<String, Object> response = dashboardService.getSystemAgents();
        return ApiResponse.success(response);
    }

    /**
     * Get Todo Items (pending approvals, scheduled scans)
     * New: GET /api/v1/dashboard/todo
     */
    @Operation(summary = "Get todo items", description = "Get pending approval and scheduled scan counts")
    @GetMapping("/todo")
    public ApiResponse<Map<String, Object>> getTodoItems(Authentication authentication) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Todo items request by user: {}", userNo);
        Map<String, Object> response = dashboardService.getTodoItems();
        return ApiResponse.success(response);
    }

    /**
     * Get Implementation Status (remediation progress)
     * New: GET /api/v1/dashboard/implementation
     */
    @Operation(summary = "Get implementation status", description = "Get remediation implementation progress")
    @GetMapping("/implementation")
    public ApiResponse<Map<String, Object>> getImplementationStatus(Authentication authentication) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Implementation status request by user: {}", userNo);
        Map<String, Object> response = dashboardService.getImplementationStatus();
        return ApiResponse.success(response);
    }

    /**
     * Get Last Scan Information
     * New: GET /api/v1/dashboard/last-scan
     */
    @Operation(summary = "Get last scan info", description = "Get last scan date and result information")
    @GetMapping("/last-scan")
    public ApiResponse<Map<String, Object>> getLastScan(Authentication authentication) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Last scan request by user: {}", userNo);
        Map<String, Object> response = dashboardService.getLastScan();
        return ApiResponse.success(response);
    }

    /**
     * Get Completion Statistics
     * New: GET /api/v1/dashboard/completion
     */
    @Operation(summary = "Get completion stats", description = "Get completion and processing statistics")
    @GetMapping("/completion")
    public ApiResponse<Map<String, Object>> getCompletionStats(Authentication authentication) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Completion stats request by user: {}", userNo);
        Map<String, Object> response = dashboardService.getCompletionStats();
        return ApiResponse.success(response);
    }

    /**
     * Get Detection Status (file counts)
     * New: GET /api/v1/dashboard/detection-status
     */
    @Operation(summary = "Get detection status", description = "Get detection file counts (total, incomplete, complete, inaccessible)")
    @GetMapping("/detection-status")
    public ApiResponse<Map<String, Object>> getDetectionStatus(Authentication authentication) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Detection status request by user: {}", userNo);
        Map<String, Object> response = dashboardService.getDetectionStatus();
        return ApiResponse.success(response);
    }

    /**
     * Get Approval Status (owned/false-positive counts)
     * New: GET /api/v1/dashboard/approval-status
     */
    @Operation(summary = "Get approval status", description = "Get approval processing status (owned, false positive counts)")
    @GetMapping("/approval-status")
    public ApiResponse<Map<String, Object>> getApprovalStatus(Authentication authentication) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Approval status request by user: {}", userNo);
        Map<String, Object> response = dashboardService.getApprovalStatus();
        return ApiResponse.success(response);
    }
}
