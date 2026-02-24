package com.org.iopts.service;

import com.org.iopts.dto.response.StatisticsResponse;

import java.util.List;
import java.util.Map;

/**
 * Statistics Service Interface
 *
 * Provides operations for statistical data retrieval including
 * overall statistics, detection breakdowns, top true/false positives,
 * datatype distributions, and monthly trend analysis.
 */
public interface StatisticsService {

    /**
     * Get overall statistics
     *
     * @param groupId   optional group filter
     * @param startDate optional start date filter
     * @param endDate   optional end date filter
     * @return overall statistics response
     */
    StatisticsResponse getOverallStatistics(String groupId, String startDate, String endDate);

    /**
     * Get detection statistics grouped by datatype and processing status
     *
     * @param groupId   optional group filter
     * @param startDate optional start date filter
     * @param endDate   optional end date filter
     * @return list of detection statistics maps
     */
    List<Map<String, Object>> getDetectionStatistics(String groupId, String startDate, String endDate);

    /**
     * Get top 5 targets by true positive count
     *
     * @param groupId optional group filter
     * @return list of top 5 true positive maps
     */
    List<Map<String, Object>> getTruePositiveTop5(String groupId);

    /**
     * Get top 5 targets by false positive count
     *
     * @param groupId optional group filter
     * @return list of top 5 false positive maps
     */
    List<Map<String, Object>> getFalsePositiveTop5(String groupId);

    /**
     * Get detection statistics grouped by datatype
     *
     * @param groupId optional group filter
     * @return list of datatype statistics maps
     */
    List<Map<String, Object>> getDatatypeStatistics(String groupId);

    /**
     * Get monthly detection statistics
     *
     * @param startDate optional start date filter
     * @param endDate   optional end date filter
     * @return list of monthly statistics maps
     */
    List<Map<String, Object>> getMonthlyStatistics(String startDate, String endDate);
}
