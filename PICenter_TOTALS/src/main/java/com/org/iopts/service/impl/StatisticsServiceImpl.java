package com.org.iopts.service.impl;

import com.org.iopts.dto.response.StatisticsResponse;
import com.org.iopts.exception.CustomException;
import com.org.iopts.exception.ErrorCode;
import com.org.iopts.repository.StatisticsMapper;
import com.org.iopts.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Statistics Service Implementation
 *
 * Migrated from legacy Pi_DashServiceImpl statistics logic
 * and statistics.jsp related controller logic.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsMapper statisticsMapper;

    @Override
    public StatisticsResponse getOverallStatistics(String groupId, String startDate, String endDate) {
        log.info("Getting overall statistics - groupId: {}, startDate: {}, endDate: {}", groupId, startDate, endDate);

        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        Map<String, Object> result = statisticsMapper.selectOverallStatistics(params);
        if (result == null) {
            log.warn("No statistics found for params: {}", params);
            return StatisticsResponse.builder()
                    .totalDetections(0L)
                    .truePositiveCount(0L)
                    .falsePositiveCount(0L)
                    .truePositiveRate(0.0)
                    .falsePositiveRate(0.0)
                    .processedCount(0L)
                    .unprocessedCount(0L)
                    .datatypeStats(new ArrayList<>())
                    .monthlyStats(new ArrayList<>())
                    .build();
        }

        // Fetch datatype and monthly breakdowns for the composite response
        List<Map<String, Object>> datatypeStats = statisticsMapper.selectDatatypeStatistics(params);
        List<Map<String, Object>> monthlyStats = statisticsMapper.selectMonthlyStatistics(params);

        return StatisticsResponse.builder()
                .totalDetections(toLong(result.get("totalDetections")))
                .truePositiveCount(toLong(result.get("truePositiveCount")))
                .falsePositiveCount(toLong(result.get("falsePositiveCount")))
                .truePositiveRate(toDouble(result.get("truePositiveRate")))
                .falsePositiveRate(toDouble(result.get("falsePositiveRate")))
                .processedCount(toLong(result.get("processedCount")))
                .unprocessedCount(toLong(result.get("unprocessedCount")))
                .datatypeStats(datatypeStats != null ? datatypeStats : new ArrayList<>())
                .monthlyStats(monthlyStats != null ? monthlyStats : new ArrayList<>())
                .build();
    }

    @Override
    public List<Map<String, Object>> getDetectionStatistics(String groupId, String startDate, String endDate) {
        log.info("Getting detection statistics - groupId: {}, startDate: {}, endDate: {}", groupId, startDate, endDate);

        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        List<Map<String, Object>> result = statisticsMapper.selectDetectionStatistics(params);
        return result != null ? result : new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getTruePositiveTop5(String groupId) {
        log.info("Getting true positive top 5 - groupId: {}", groupId);

        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);

        List<Map<String, Object>> result = statisticsMapper.selectTruePositiveTop5(params);
        return result != null ? result : new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getFalsePositiveTop5(String groupId) {
        log.info("Getting false positive top 5 - groupId: {}", groupId);

        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);

        List<Map<String, Object>> result = statisticsMapper.selectFalsePositiveTop5(params);
        return result != null ? result : new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getDatatypeStatistics(String groupId) {
        log.info("Getting datatype statistics - groupId: {}", groupId);

        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);

        List<Map<String, Object>> result = statisticsMapper.selectDatatypeStatistics(params);
        return result != null ? result : new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getMonthlyStatistics(String startDate, String endDate) {
        log.info("Getting monthly statistics - startDate: {}, endDate: {}", startDate, endDate);

        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        List<Map<String, Object>> result = statisticsMapper.selectMonthlyStatistics(params);
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

    /**
     * Safely convert Object to Double
     */
    private Double toDouble(Object value) {
        if (value == null) {
            return 0.0;
        }
        if (value instanceof Double) {
            return (Double) value;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            log.warn("Failed to convert value to Double: {}", value);
            return 0.0;
        }
    }
}
