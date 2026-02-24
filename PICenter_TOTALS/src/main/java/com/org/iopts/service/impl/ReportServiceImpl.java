package com.org.iopts.service.impl;

import com.org.iopts.dto.request.ReportSearchRequest;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.dto.response.ReportDetailResponse;
import com.org.iopts.dto.response.ReportSummaryResponse;
import com.org.iopts.exception.CustomException;
import com.org.iopts.exception.ErrorCode;
import com.org.iopts.repository.ReportMapper;
import com.org.iopts.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Report Service Implementation
 *
 * Migrated from legacy piSummaryServiceImple / piReportExceptController logic.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final ReportMapper reportMapper;

    @Override
    public ReportSummaryResponse getReportSummary(String groupId, String startDate, String endDate) {
        log.info("Getting report summary - groupId: {}, startDate: {}, endDate: {}", groupId, startDate, endDate);

        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        Map<String, Object> result = reportMapper.selectReportSummary(params);
        if (result == null) {
            log.warn("No report summary found for params: {}", params);
            return ReportSummaryResponse.builder()
                    .totalTargets(0L)
                    .scannedTargets(0L)
                    .totalDetections(0L)
                    .processedDetections(0L)
                    .truePositives(0L)
                    .falsePositives(0L)
                    .remediatedCount(0L)
                    .topDetectionTypes(new ArrayList<>())
                    .topTargets(new ArrayList<>())
                    .build();
        }

        // Build top detection types from detailed report data
        Map<String, Object> detailParams = new HashMap<>(params);
        detailParams.put("size", 5);
        detailParams.put("offset", 0);
        List<Map<String, Object>> topTargets = reportMapper.selectDetailedReport(detailParams);

        return ReportSummaryResponse.builder()
                .reportDate(result.get("reportDate") != null ? result.get("reportDate").toString() : null)
                .totalTargets(toLong(result.get("totalTargets")))
                .scannedTargets(toLong(result.get("scannedTargets")))
                .totalDetections(toLong(result.get("totalDetections")))
                .processedDetections(toLong(result.get("processedDetections")))
                .truePositives(toLong(result.get("truePositives")))
                .falsePositives(toLong(result.get("falsePositives")))
                .remediatedCount(toLong(result.get("remediatedCount")))
                .topDetectionTypes(new ArrayList<>())
                .topTargets(topTargets != null ? topTargets : new ArrayList<>())
                .build();
    }

    @Override
    public List<Map<String, Object>> getMonthlyReport(String startDate, String endDate) {
        log.info("Getting monthly report - startDate: {}, endDate: {}", startDate, endDate);

        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        List<Map<String, Object>> result = reportMapper.selectMonthlyReport(params);
        return result != null ? result : new ArrayList<>();
    }

    @Override
    public PageResponse<ReportDetailResponse> getDetailedReport(ReportSearchRequest request) {
        log.info("Getting detailed report - request: {}", request);

        Map<String, Object> params = new HashMap<>();
        params.put("reportType", request.getReportType());
        params.put("groupId", request.getGroupId());
        params.put("targetId", request.getTargetId());
        params.put("startDate", request.getStartDate());
        params.put("endDate", request.getEndDate());
        params.put("size", request.getSize());
        params.put("offset", request.getPage() * request.getSize());

        long totalCount = reportMapper.selectDetailedReportCount(params);
        List<Map<String, Object>> rawList = reportMapper.selectDetailedReport(params);

        List<ReportDetailResponse> content;
        if (rawList == null || rawList.isEmpty()) {
            content = new ArrayList<>();
        } else {
            content = rawList.stream()
                    .map(this::mapToReportDetailResponse)
                    .collect(Collectors.toList());
        }

        return PageResponse.of(content, request.getPage(), request.getSize(), totalCount);
    }

    @Override
    public List<Map<String, Object>> getTargetReport(String groupId, String targetId) {
        log.info("Getting target report - groupId: {}, targetId: {}", groupId, targetId);

        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("targetId", targetId);

        List<Map<String, Object>> result = reportMapper.selectTargetReport(params);
        return result != null ? result : new ArrayList<>();
    }

    @Override
    public byte[] exportExcel(ReportSearchRequest request) {
        log.info("Exporting Excel report - request: {}", request);

        Map<String, Object> params = new HashMap<>();
        params.put("reportType", request.getReportType());
        params.put("groupId", request.getGroupId());
        params.put("targetId", request.getTargetId());
        params.put("startDate", request.getStartDate());
        params.put("endDate", request.getEndDate());

        List<Map<String, Object>> data = reportMapper.selectExcelData(params);
        if (data == null || data.isEmpty()) {
            log.warn("No data found for Excel export with params: {}", params);
        }

        // TODO: Implement Excel generation using Apache POI or similar library.
        //       The data variable contains the raw query results that should be
        //       written to an Excel workbook with appropriate column headers
        //       (targetName, targetIp, groupId, findPath, datatype, findDate,
        //       processYn, trueYn, remediateYn, processDate, processUser).
        return new byte[0];
    }

    /**
     * Map raw result map to ReportDetailResponse
     */
    private ReportDetailResponse mapToReportDetailResponse(Map<String, Object> row) {
        return ReportDetailResponse.builder()
                .targetId(toLong(row.get("targetId")))
                .targetName(row.get("targetName") != null ? row.get("targetName").toString() : null)
                .targetIp(row.get("targetIp") != null ? row.get("targetIp").toString() : null)
                .detectionCount(toLong(row.get("detectionCount")))
                .processedCount(toLong(row.get("processedCount")))
                .truePositiveCount(toLong(row.get("truePositiveCount")))
                .falsePositiveCount(toLong(row.get("falsePositiveCount")))
                .remediatedCount(toLong(row.get("remediatedCount")))
                .lastScanDate(row.get("lastScanDate") != null ? row.get("lastScanDate").toString() : null)
                .detections(new ArrayList<>())
                .build();
    }

    /**
     * Safely convert Object to Long
     */
    private Long toLong(Object value) {
        if (value == null) {
            return 0L;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            log.warn("Failed to convert value to Long: {}", value);
            return 0L;
        }
    }
}
