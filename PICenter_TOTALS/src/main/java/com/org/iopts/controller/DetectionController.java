package com.org.iopts.controller;

import com.org.iopts.dto.request.DetectionSearchRequest;
import com.org.iopts.dto.request.ProcessRequest;
import com.org.iopts.dto.response.ApiResponse;
import com.org.iopts.dto.response.DetectionDetailResponse;
import com.org.iopts.dto.response.DetectionResponse;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.service.DetectionService;
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
 * Detection Management Controller
 * Migrated from legacy ScanController / piDetectionListController
 */
@Slf4j
@Tag(name = "Detection Management", description = "PI Detection Management API")
@RestController
@RequestMapping("/api/v1/detections")
@RequiredArgsConstructor
public class DetectionController {

    private final DetectionService detectionService;

    /**
     * Get Detection List
     *
     * Legacy: POST /getDetectionList
     * New: GET /api/v1/detections
     */
    @Operation(summary = "Get detection list", description = "Search and retrieve paginated detection list")
    @GetMapping
    public ApiResponse<PageResponse<DetectionResponse>> getDetectionList(
            @Parameter(description = "Search type", example = "fileName") @RequestParam(required = false) String searchType,
            @Parameter(description = "Search keyword", example = "test") @RequestParam(required = false) String searchKeyword,
            @Parameter(description = "Target ID filter", example = "1") @RequestParam(required = false) String targetId,
            @Parameter(description = "Process status filter", example = "DETECTED") @RequestParam(required = false) String processStatus,
            @Parameter(description = "Data type name filter", example = "주민등록번호") @RequestParam(required = false) String datatypeName,
            @Parameter(description = "Start date (yyyy-MM-dd)", example = "2026-01-01") @RequestParam(required = false) String startDate,
            @Parameter(description = "End date (yyyy-MM-dd)", example = "2026-12-31") @RequestParam(required = false) String endDate,
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size) {

        DetectionSearchRequest request = DetectionSearchRequest.builder()
                .searchType(searchType)
                .searchKeyword(searchKeyword)
                .targetId(targetId)
                .processStatus(processStatus)
                .datatypeName(datatypeName)
                .startDate(startDate)
                .endDate(endDate)
                .page(page)
                .size(size)
                .build();

        PageResponse<DetectionResponse> response = detectionService.getDetectionList(request);
        return ApiResponse.success(response);
    }

    /**
     * Get Detection Detail
     *
     * Legacy: POST /getDetectionDetail
     * New: GET /api/v1/detections/{findId}
     */
    @Operation(summary = "Get detection detail", description = "Get detection detail with process history and subpaths")
    @GetMapping("/{findId}")
    public ApiResponse<DetectionDetailResponse> getDetectionDetail(
            @Parameter(description = "Detection ID (hash_id)", example = "abc123") @PathVariable String findId) {

        log.info("Get detection detail - findId: {}", findId);
        DetectionDetailResponse response = detectionService.getDetectionDetail(findId);
        return ApiResponse.success(response);
    }

    /**
     * Get Subpaths for a Target
     *
     * Legacy: POST /getLowPath
     * New: GET /api/v1/detections/subpaths/{targetId}
     */
    @Operation(summary = "Get subpaths", description = "Get subpath list for a target")
    @GetMapping("/subpaths/{targetId}")
    public ApiResponse<List<Map<String, Object>>> getSubpaths(
            @Parameter(description = "Target ID", example = "server01") @PathVariable String targetId) {

        log.info("Get subpaths - targetId: {}", targetId);
        List<Map<String, Object>> response = detectionService.getSubpaths(targetId);
        return ApiResponse.success(response);
    }

    /**
     * Process Detection
     *
     * Legacy: POST /processDetection
     * New: POST /api/v1/detections/{findId}/process
     */
    @Operation(summary = "Process detection", description = "Classify detection as true positive or false positive")
    @PostMapping("/{findId}/process")
    public ApiResponse<Void> processDetection(
            @Parameter(description = "Detection ID (hash_id)", example = "abc123") @PathVariable String findId,
            @Valid @RequestBody ProcessRequest request,
            Authentication authentication) {

        String userNo = (String) authentication.getPrincipal();
        log.info("Process detection - findId: {}, type: {}, userNo: {}",
                findId, request.getProcessType(), userNo);

        detectionService.processDetection(findId, request, userNo);
        return ApiResponse.success();
    }

    /**
     * Remediate Detection
     *
     * Legacy: POST /remediateDetection
     * New: POST /api/v1/detections/{findId}/remediate
     */
    @Operation(summary = "Remediate detection", description = "Apply remediation to a detection")
    @PostMapping("/{findId}/remediate")
    public ApiResponse<Void> remediateDetection(
            @Parameter(description = "Detection ID (hash_id)", example = "abc123") @PathVariable String findId,
            @Parameter(description = "Remediation type", example = "DELETE") @RequestParam String remediationType,
            Authentication authentication) {

        String userNo = (String) authentication.getPrincipal();
        log.info("Remediate detection - findId: {}, type: {}, userNo: {}",
                findId, remediationType, userNo);

        detectionService.remediateDetection(findId, remediationType, userNo);
        return ApiResponse.success();
    }
}
