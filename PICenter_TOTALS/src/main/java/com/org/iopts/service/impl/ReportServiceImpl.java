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
import java.util.concurrent.ConcurrentHashMap;
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

    /** 인메모리 캐시 - 보고서 집계 쿼리 성능 개선 */
    private final ConcurrentHashMap<String, Object[]> cache = new ConcurrentHashMap<>();
    private static final long CACHE_TTL_MS = 5 * 60 * 1000L;

    @SuppressWarnings("unchecked")
    private <T> T getFromCache(String key) {
        Object[] entry = cache.get(key);
        if (entry != null && System.currentTimeMillis() < (long) entry[1]) {
            return (T) entry[0];
        }
        return null;
    }

    private void putToCache(String key, Object data) {
        cache.put(key, new Object[]{data, System.currentTimeMillis() + CACHE_TTL_MS});
    }

    @Override
    public ReportSummaryResponse getReportSummary(String groupId, String startDate, String endDate, String hostName, String path) {
        log.info("Getting report summary - groupId: {}, startDate: {}, endDate: {}, hostName: {}", groupId, startDate, endDate, hostName);

        String cacheKey = "reportSummary:" + groupId + ":" + startDate + ":" + endDate + ":" + hostName + ":" + path;
        ReportSummaryResponse cachedResp = getFromCache(cacheKey);
        if (cachedResp != null) {
            log.debug("Cache hit for reportSummary");
            return cachedResp;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        if (hostName != null && !hostName.isEmpty()) {
            params.put("hostName", hostName);
        }
        if (path != null && !path.isEmpty()) {
            params.put("path", path);
        }

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

        ReportSummaryResponse resp = ReportSummaryResponse.builder()
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
        putToCache(cacheKey, resp);
        return resp;
    }

    @Override
    public List<Map<String, Object>> getMonthlyReport(String startDate, String endDate) {
        log.info("Getting monthly report - startDate: {}, endDate: {}", startDate, endDate);

        String cacheKey = "reportMonthly:" + startDate + ":" + endDate;
        List<Map<String, Object>> cached = getFromCache(cacheKey);
        if (cached != null) {
            log.debug("Cache hit for monthlyReport");
            return cached;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        List<Map<String, Object>> result = reportMapper.selectMonthlyReport(params);
        result = result != null ? result : new ArrayList<>();
        putToCache(cacheKey, result);
        return result;
    }

    @Override
    public PageResponse<ReportDetailResponse> getDetailedReport(ReportSearchRequest request) {
        log.info("Getting detailed report - request: {}", request);

        String cacheKey = "reportDetailed:" + request.getReportType() + ":" + request.getGroupId()
                + ":" + request.getTargetId() + ":" + request.getStartDate() + ":" + request.getEndDate()
                + ":" + request.getPage() + ":" + request.getSize();
        PageResponse<ReportDetailResponse> cached = getFromCache(cacheKey);
        if (cached != null) {
            log.debug("Cache hit for detailedReport");
            return cached;
        }

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

        PageResponse<ReportDetailResponse> result = PageResponse.of(content, request.getPage(), request.getSize(), totalCount);
        putToCache(cacheKey, result);
        return result;
    }

    @Override
    public List<Map<String, Object>> getTargetReport(String groupId, String targetId) {
        log.info("Getting target report - groupId: {}, targetId: {}", groupId, targetId);

        String cacheKey = "reportTargets:" + groupId + ":" + targetId;
        List<Map<String, Object>> cached = getFromCache(cacheKey);
        if (cached != null) {
            log.debug("Cache hit for targetReport");
            return cached;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("targetId", targetId);

        List<Map<String, Object>> result = reportMapper.selectTargetReport(params);
        result = result != null ? result : new ArrayList<>();
        putToCache(cacheKey, result);
        return result;
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
                .targetId(row.get("targetId") != null ? row.get("targetId").toString() : null)
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

    @Override
    public List<Map<String, Object>> getExceptionReport(String groupId, String startDate, String endDate) {
        log.info("Getting exception report - groupId: {}, startDate: {}, endDate: {}", groupId, startDate, endDate);

        String cacheKey = "reportException:" + groupId + ":" + startDate + ":" + endDate;
        List<Map<String, Object>> cached = getFromCache(cacheKey);
        if (cached != null) {
            log.debug("Cache hit for exceptionReport");
            return cached;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        List<Map<String, Object>> result = reportMapper.selectExceptionReport(params);
        result = result != null ? result : new ArrayList<>();
        putToCache(cacheKey, result);
        return result;
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
