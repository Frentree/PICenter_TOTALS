package com.org.iopts.service.impl;

import com.org.iopts.dto.response.ChartDataResponse;
import com.org.iopts.dto.response.DashboardResponse;
import com.org.iopts.dto.response.SystemCurrentResponse;
import com.org.iopts.exception.CustomException;
import com.org.iopts.exception.ErrorCode;
import com.org.iopts.repository.DashboardMapper;
import com.org.iopts.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dashboard Service Implementation
 *
 * Migrated from legacy Pi_DashServiceImpl / HomeController dashboard logic.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final DashboardMapper dashboardMapper;

    @Override
    public DashboardResponse getDashboardInfo(String groupId, String startDate, String endDate) {
        log.info("Getting dashboard info - groupId: {}, startDate: {}, endDate: {}", groupId, startDate, endDate);

        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        Map<String, Object> result = dashboardMapper.selectDashboardInfo(params);
        if (result == null) {
            log.warn("No dashboard info found for params: {}", params);
            return DashboardResponse.builder()
                    .totalTargets(0L)
                    .totalDetections(0L)
                    .totalProcessed(0L)
                    .totalUnprocessed(0L)
                    .truePositiveCount(0L)
                    .falsePositiveCount(0L)
                    .build();
        }

        return DashboardResponse.builder()
                .totalTargets(toLong(result.get("totalTargets")))
                .totalDetections(toLong(result.get("totalDetections")))
                .totalProcessed(toLong(result.get("totalProcessed")))
                .totalUnprocessed(toLong(result.get("totalUnprocessed")))
                .truePositiveCount(toLong(result.get("truePositiveCount")))
                .falsePositiveCount(toLong(result.get("falsePositiveCount")))
                .lastScanDate(result.get("lastScanDate") != null ? result.get("lastScanDate").toString() : null)
                .build();
    }

    @Override
    public List<Map<String, Object>> getDatatypeStatus(String groupId) {
        log.info("Getting datatype status - groupId: {}", groupId);

        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);

        List<Map<String, Object>> result = dashboardMapper.selectDatatypeStatus(params);
        return result != null ? result : new ArrayList<>();
    }

    @Override
    public SystemCurrentResponse getSystemCurrent() {
        log.info("Getting system current status");

        Map<String, Object> result = dashboardMapper.selectSystemCurrent();
        if (result == null) {
            log.warn("No system current data found");
            return SystemCurrentResponse.builder()
                    .totalAgents(0L)
                    .activeAgents(0L)
                    .inactiveAgents(0L)
                    .totalScans(0L)
                    .runningScans(0L)
                    .scheduledScans(0L)
                    .systemStatus("UNKNOWN")
                    .build();
        }

        return SystemCurrentResponse.builder()
                .totalAgents(toLong(result.get("totalAgents")))
                .activeAgents(toLong(result.get("activeAgents")))
                .inactiveAgents(toLong(result.get("inactiveAgents")))
                .totalScans(toLong(result.get("totalScans")))
                .runningScans(toLong(result.get("runningScans")))
                .scheduledScans(toLong(result.get("scheduledScans")))
                .systemStatus(result.get("systemStatus") != null ? result.get("systemStatus").toString() : "NORMAL")
                .lastUpdateTime(result.get("lastUpdateTime") != null ? result.get("lastUpdateTime").toString() : null)
                .build();
    }

    @Override
    public List<Map<String, Object>> getPathCurrent(String groupId) {
        log.info("Getting path current - groupId: {}", groupId);

        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);

        List<Map<String, Object>> result = dashboardMapper.selectPathCurrent(params);
        return result != null ? result : new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getDashboardDetections(String groupId, int limit) {
        log.info("Getting dashboard detections - groupId: {}, limit: {}", groupId, limit);

        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("limit", limit > 0 ? limit : 10);

        List<Map<String, Object>> result = dashboardMapper.selectDashboardDetections(params);
        return result != null ? result : new ArrayList<>();
    }

    @Override
    public ChartDataResponse getPieChartData(String groupId, String startDate, String endDate) {
        log.info("Getting pie chart data - groupId: {}, startDate: {}, endDate: {}", groupId, startDate, endDate);

        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        List<Map<String, Object>> rawData = dashboardMapper.selectPieChartData(params);
        if (rawData == null) {
            rawData = new ArrayList<>();
        }

        List<Map<String, Object>> labels = new ArrayList<>();
        List<Map<String, Object>> datasets = new ArrayList<>();

        for (Map<String, Object> row : rawData) {
            Map<String, Object> label = new HashMap<>();
            label.put("name", row.get("label"));
            labels.add(label);

            Map<String, Object> dataset = new HashMap<>();
            dataset.put("value", row.get("value"));
            dataset.put("label", row.get("label"));
            datasets.add(dataset);
        }

        return ChartDataResponse.builder()
                .chartType("pie")
                .labels(labels)
                .datasets(datasets)
                .build();
    }

    @Override
    public ChartDataResponse getBarChartData(String groupId, String startDate, String endDate) {
        log.info("Getting bar chart data - groupId: {}, startDate: {}, endDate: {}", groupId, startDate, endDate);

        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        List<Map<String, Object>> rawData = dashboardMapper.selectBarChartData(params);
        if (rawData == null) {
            rawData = new ArrayList<>();
        }

        List<Map<String, Object>> labels = new ArrayList<>();
        List<Map<String, Object>> datasets = new ArrayList<>();

        for (Map<String, Object> row : rawData) {
            Map<String, Object> label = new HashMap<>();
            label.put("name", row.get("label"));
            labels.add(label);

            Map<String, Object> dataset = new HashMap<>();
            dataset.put("totalCount", row.get("totalCount"));
            dataset.put("truePositiveCount", row.get("truePositiveCount"));
            dataset.put("falsePositiveCount", row.get("falsePositiveCount"));
            dataset.put("processedCount", row.get("processedCount"));
            dataset.put("label", row.get("label"));
            datasets.add(dataset);
        }

        return ChartDataResponse.builder()
                .chartType("bar")
                .labels(labels)
                .datasets(datasets)
                .build();
    }

    @Override
    public List<Map<String, Object>> getGridData(String groupId, String startDate, String endDate) {
        log.info("Getting grid data - groupId: {}, startDate: {}, endDate: {}", groupId, startDate, endDate);

        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        List<Map<String, Object>> result = dashboardMapper.selectGridData(params);
        return result != null ? result : new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getRankingData(String groupId, int limit) {
        log.info("Getting ranking data - groupId: {}, limit: {}", groupId, limit);

        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("limit", limit > 0 ? limit : 10);

        List<Map<String, Object>> result = dashboardMapper.selectRankingData(params);
        return result != null ? result : new ArrayList<>();
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
