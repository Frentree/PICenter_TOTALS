package com.org.iopts.controller;

import com.org.iopts.dto.request.ReportSearchRequest;
import com.org.iopts.dto.response.ApiResponse;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.dto.response.ReportDetailResponse;
import com.org.iopts.dto.response.ReportSummaryResponse;
import com.org.iopts.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Report Controller
 *
 * Migrated from legacy piSummaryController / piReportExceptController logic.
 *
 * Legacy: /report/*, /report/summary/*
 * New: /api/v1/reports/*
 */
@Slf4j
@Tag(name = "Reports", description = "Reports API")
@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /**
     * Get Report Summary
     *
     * Legacy: POST /report/getSummary
     * New: GET /api/v1/reports/summary
     */
    @Operation(summary = "Get report summary", description = "Get aggregated report summary with optional filters")
    @GetMapping("/summary")
    public ApiResponse<ReportSummaryResponse> getReportSummary(
            Authentication authentication,
            @Parameter(description = "Group ID filter", example = "GRP001") @RequestParam(required = false) String groupId,
            @Parameter(description = "Start date (yyyy-MM-dd)", example = "2026-01-01") @RequestParam(required = false) String startDate,
            @Parameter(description = "End date (yyyy-MM-dd)", example = "2026-12-31") @RequestParam(required = false) String endDate,
            @Parameter(description = "Host name filter") @RequestParam(required = false) String hostName,
            @Parameter(description = "From date (alias for startDate)") @RequestParam(required = false) String fromDate,
            @Parameter(description = "To date (alias for endDate)") @RequestParam(required = false) String toDate,
            @Parameter(description = "Path filter") @RequestParam(required = false) String path,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Report summary request by user: {}", userNo);

        // Map frontend aliases to backend params
        String effectiveStartDate = (startDate != null && !startDate.isEmpty()) ? startDate : fromDate;
        String effectiveEndDate = (endDate != null && !endDate.isEmpty()) ? endDate : toDate;

        ReportSummaryResponse response = reportService.getReportSummary(groupId, effectiveStartDate, effectiveEndDate, hostName, path);
        return ApiResponse.success(response);
    }

    /**
     * Get Monthly Report
     *
     * Legacy: POST /report/getMonthlyReport
     * New: GET /api/v1/reports/monthly
     */
    @Operation(summary = "Get monthly report", description = "Get monthly report data for trend analysis")
    @GetMapping("/monthly")
    public ApiResponse<List<Map<String, Object>>> getMonthlyReport(
            Authentication authentication,
            @Parameter(description = "Start date (yyyy-MM-dd)", example = "2026-01-01") @RequestParam(required = false) String startDate,
            @Parameter(description = "End date (yyyy-MM-dd)", example = "2026-12-31") @RequestParam(required = false) String endDate) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Monthly report request by user: {}", userNo);

        List<Map<String, Object>> response = reportService.getMonthlyReport(startDate, endDate);
        return ApiResponse.success(response);
    }

    /**
     * Get Detailed Report
     *
     * Legacy: POST /report/getDetailedReport
     * New: GET /api/v1/reports/detailed
     */
    @Operation(summary = "Get detailed report", description = "Get detailed per-target report with pagination")
    @GetMapping("/detailed")
    public ApiResponse<PageResponse<ReportDetailResponse>> getDetailedReport(
            Authentication authentication,
            @Parameter(description = "Report type filter", example = "SUMMARY") @RequestParam(required = false) String reportType,
            @Parameter(description = "Group ID filter", example = "GRP001") @RequestParam(required = false) String groupId,
            @Parameter(description = "Target ID filter", example = "1") @RequestParam(required = false) String targetId,
            @Parameter(description = "Start date (yyyy-MM-dd)", example = "2026-01-01") @RequestParam(required = false) String startDate,
            @Parameter(description = "End date (yyyy-MM-dd)", example = "2026-12-31") @RequestParam(required = false) String endDate,
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Detailed report request by user: {}, page: {}, size: {}", userNo, page, size);

        ReportSearchRequest request = ReportSearchRequest.builder()
                .reportType(reportType)
                .groupId(groupId)
                .targetId(targetId)
                .startDate(startDate)
                .endDate(endDate)
                .page(page)
                .size(size)
                .build();

        PageResponse<ReportDetailResponse> response = reportService.getDetailedReport(request);
        return ApiResponse.success(response);
    }

    /**
     * Get Target Report
     *
     * Legacy: POST /report/getTargetReport
     * New: GET /api/v1/reports/targets
     */
    @Operation(summary = "Get target report", description = "Get target-specific report data")
    @GetMapping("/targets")
    public ApiResponse<List<Map<String, Object>>> getTargetReport(
            Authentication authentication,
            @Parameter(description = "Group ID filter", example = "GRP001") @RequestParam(required = false) String groupId,
            @Parameter(description = "Target ID", example = "1") @RequestParam(required = false) String targetId) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Target report request by user: {}, targetId: {}", userNo, targetId);

        List<Map<String, Object>> response = reportService.getTargetReport(groupId, targetId);
        return ApiResponse.success(response);
    }

    /**
     * Get Exception Report
     *
     * New: GET /api/v1/reports/exceptions
     */
    @Operation(summary = "Get exception report", description = "Get exception report summary")
    @GetMapping("/exceptions")
    public ApiResponse<List<Map<String, Object>>> getExceptionReport(
            Authentication authentication,
            @Parameter(description = "Group ID filter") @RequestParam(required = false) String groupId,
            @Parameter(description = "Start date") @RequestParam(required = false) String startDate,
            @Parameter(description = "End date") @RequestParam(required = false) String endDate) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Exception report request by user: {}", userNo);
        List<Map<String, Object>> response = reportService.getExceptionReport(groupId, startDate, endDate);
        return ApiResponse.success(response);
    }

    /**
     * Export Excel Report
     *
     * Legacy: POST /report/excelDown
     * New: GET /api/v1/reports/export/excel
     */
    @Operation(summary = "Export Excel report", description = "Export report data as Excel file")
    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportExcel(
            Authentication authentication,
            @Parameter(description = "Report type filter", example = "SUMMARY") @RequestParam(required = false) String reportType,
            @Parameter(description = "Group ID filter", example = "GRP001") @RequestParam(required = false) String groupId,
            @Parameter(description = "Target ID filter", example = "1") @RequestParam(required = false) String targetId,
            @Parameter(description = "Start date (yyyy-MM-dd)", example = "2026-01-01") @RequestParam(required = false) String startDate,
            @Parameter(description = "End date (yyyy-MM-dd)", example = "2026-12-31") @RequestParam(required = false) String endDate) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Excel export request by user: {}", userNo);

        ReportSearchRequest request = ReportSearchRequest.builder()
                .reportType(reportType)
                .groupId(groupId)
                .targetId(targetId)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        byte[] excelData = reportService.exportExcel(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "report.xlsx");
        headers.setContentLength(excelData.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }
}
