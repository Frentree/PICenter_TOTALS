package com.org.iopts.repository;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Statistics MyBatis Mapper
 *
 * Provides SQL operations for statistical data retrieval including
 * overall statistics, detection breakdowns, top true/false positives,
 * datatype distributions, and monthly trend analysis.
 * Namespace: com.org.iopts.repository.StatisticsMapper
 */
@Mapper
public interface StatisticsMapper {

    /**
     * Select overall statistics (total detections, true/false positive counts, rates)
     */
    Map<String, Object> selectOverallStatistics(Map<String, Object> params);

    /**
     * Select detection statistics grouped by processing status
     */
    List<Map<String, Object>> selectDetectionStatistics(Map<String, Object> params);

    /**
     * Select top 5 targets/groups by true positive count
     */
    List<Map<String, Object>> selectTruePositiveTop5(Map<String, Object> params);

    /**
     * Select top 5 targets/groups by false positive count
     */
    List<Map<String, Object>> selectFalsePositiveTop5(Map<String, Object> params);

    /**
     * Select detection statistics grouped by datatype
     */
    List<Map<String, Object>> selectDatatypeStatistics(Map<String, Object> params);

    /**
     * Select monthly detection statistics for trend analysis
     */
    List<Map<String, Object>> selectMonthlyStatistics(Map<String, Object> params);
}
