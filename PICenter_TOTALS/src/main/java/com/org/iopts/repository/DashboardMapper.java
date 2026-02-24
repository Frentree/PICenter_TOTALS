package com.org.iopts.repository;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Dashboard MyBatis Mapper
 *
 * Provides SQL operations for dashboard data retrieval including
 * aggregated info, datatype status, system current state, path analysis,
 * detection lists, and various chart data queries.
 * Namespace: com.org.iopts.repository.DashboardMapper
 */
@Mapper
public interface DashboardMapper {

    /**
     * Select aggregated dashboard info (target counts, detection counts, processing status)
     */
    Map<String, Object> selectDashboardInfo(Map<String, Object> params);

    /**
     * Select detection counts grouped by datatype
     */
    List<Map<String, Object>> selectDatatypeStatus(Map<String, Object> params);

    /**
     * Select current system status (agents, scans, system health)
     */
    Map<String, Object> selectSystemCurrent();

    /**
     * Select detection path current status
     */
    List<Map<String, Object>> selectPathCurrent(Map<String, Object> params);

    /**
     * Select recent dashboard detections with limit
     */
    List<Map<String, Object>> selectDashboardDetections(Map<String, Object> params);

    /**
     * Select pie chart data (detection breakdown by category)
     */
    List<Map<String, Object>> selectPieChartData(Map<String, Object> params);

    /**
     * Select bar chart data (detection trend over time)
     */
    List<Map<String, Object>> selectBarChartData(Map<String, Object> params);

    /**
     * Select grid data for dashboard table display
     */
    List<Map<String, Object>> selectGridData(Map<String, Object> params);

    /**
     * Select ranking data (top targets/groups by detection count)
     */
    List<Map<String, Object>> selectRankingData(Map<String, Object> params);
}
