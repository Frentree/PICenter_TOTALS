package com.org.iopts.controller;

import com.org.iopts.dto.response.ApiResponse;
import com.org.iopts.dto.response.StatisticsResponse;
import com.org.iopts.service.StatisticsService;
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
 * Statistics Controller
 *
 * Migrated from legacy statistics.jsp and related controller logic.
 *
 * Legacy: /manage/statistics, /dash/getStatistics
 * New: /api/v1/statistics/*
 */
@Slf4j
@Tag(name = "Statistics", description = "Statistics API")
@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    /**
     * Get Overall Statistics
     *
     * Legacy: POST /dash/getOverallStatistics
     * New: GET /api/v1/statistics (alias: /overall)
     */
    @Operation(summary = "Get overall statistics", description = "Get aggregated statistics with optional filters")
    @GetMapping({"", "/overall"})
    public ApiResponse<StatisticsResponse> getOverallStatistics(
            Authentication authentication,
            @Parameter(description = "Group ID filter", example = "GRP001") @RequestParam(required = false) String groupId,
            @Parameter(description = "Start date (yyyy-MM-dd)", example = "2026-01-01") @RequestParam(required = false) String startDate,
            @Parameter(description = "End date (yyyy-MM-dd)", example = "2026-12-31") @RequestParam(required = false) String endDate) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Overall statistics request by user: {}", userNo);

        StatisticsResponse response = statisticsService.getOverallStatistics(groupId, startDate, endDate);
        return ApiResponse.success(response);
    }

    /**
     * Get Detection Statistics
     *
     * Legacy: POST /dash/getDetectionStatistics
     * New: GET /api/v1/statistics/detections
     */
    @Operation(summary = "Get detection statistics", description = "Get detection statistics grouped by datatype and status")
    @GetMapping("/detections")
    public ApiResponse<List<Map<String, Object>>> getDetectionStatistics(
            Authentication authentication,
            @Parameter(description = "Group ID filter", example = "GRP001") @RequestParam(required = false) String groupId,
            @Parameter(description = "Start date (yyyy-MM-dd)", example = "2026-01-01") @RequestParam(required = false) String startDate,
            @Parameter(description = "End date (yyyy-MM-dd)", example = "2026-12-31") @RequestParam(required = false) String endDate) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Detection statistics request by user: {}", userNo);

        List<Map<String, Object>> response = statisticsService.getDetectionStatistics(groupId, startDate, endDate);
        return ApiResponse.success(response);
    }

    /**
     * Get True Positive Top 5
     *
     * Legacy: POST /dash/getTruePositiveTop5
     * New: GET /api/v1/statistics/true-positives
     */
    @Operation(summary = "Get true positive top 5", description = "Get top 5 targets by true positive count")
    @GetMapping("/true-positives")
    public ApiResponse<List<Map<String, Object>>> getTruePositiveTop5(
            Authentication authentication,
            @Parameter(description = "Group ID filter", example = "GRP001") @RequestParam(required = false) String groupId) {
        String userNo = (String) authentication.getPrincipal();
        log.info("True positive top 5 request by user: {}", userNo);

        List<Map<String, Object>> response = statisticsService.getTruePositiveTop5(groupId);
        return ApiResponse.success(response);
    }

    /**
     * Get False Positive Top 5
     *
     * Legacy: POST /dash/getFalsePositiveTop5
     * New: GET /api/v1/statistics/false-positives
     */
    @Operation(summary = "Get false positive top 5", description = "Get top 5 targets by false positive count")
    @GetMapping("/false-positives")
    public ApiResponse<List<Map<String, Object>>> getFalsePositiveTop5(
            Authentication authentication,
            @Parameter(description = "Group ID filter", example = "GRP001") @RequestParam(required = false) String groupId) {
        String userNo = (String) authentication.getPrincipal();
        log.info("False positive top 5 request by user: {}", userNo);

        List<Map<String, Object>> response = statisticsService.getFalsePositiveTop5(groupId);
        return ApiResponse.success(response);
    }

    /**
     * Get Datatype Statistics
     *
     * Legacy: POST /dash/getDatatypeStatistics
     * New: GET /api/v1/statistics/datatypes
     */
    @Operation(summary = "Get datatype statistics", description = "Get detection statistics grouped by datatype")
    @GetMapping("/datatypes")
    public ApiResponse<List<Map<String, Object>>> getDatatypeStatistics(
            Authentication authentication,
            @Parameter(description = "Group ID filter", example = "GRP001") @RequestParam(required = false) String groupId) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Datatype statistics request by user: {}", userNo);

        List<Map<String, Object>> response = statisticsService.getDatatypeStatistics(groupId);
        return ApiResponse.success(response);
    }

    /**
     * Get Monthly Statistics
     *
     * Legacy: POST /dash/getMonthlyStatistics
     * New: GET /api/v1/statistics/monthly
     */
    @Operation(summary = "Get monthly statistics", description = "Get monthly detection statistics for trend analysis")
    @GetMapping("/monthly")
    public ApiResponse<List<Map<String, Object>>> getMonthlyStatistics(
            Authentication authentication,
            @Parameter(description = "Start date (yyyy-MM-dd)", example = "2026-01-01") @RequestParam(required = false) String startDate,
            @Parameter(description = "End date (yyyy-MM-dd)", example = "2026-12-31") @RequestParam(required = false) String endDate) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Monthly statistics request by user: {}", userNo);

        List<Map<String, Object>> response = statisticsService.getMonthlyStatistics(startDate, endDate);
        return ApiResponse.success(response);
    }
}
