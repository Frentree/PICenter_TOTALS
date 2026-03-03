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

    /**
     * Select detailed agent status (connected/disconnected/total)
     */
    Map<String, Object> selectSystemAgents();

    /**
     * Select todo items (pending approvals, scheduled scans)
     */
    Map<String, Object> selectTodoItems();

    /**
     * Select implementation/remediation progress
     */
    Map<String, Object> selectImplementationStatus();

    /**
     * Select last scan information
     */
    Map<String, Object> selectLastScan();

    /**
     * Select completion statistics
     */
    Map<String, Object> selectCompletionStats();

    /**
     * Select detection file status (total, processed, inaccessible)
     */
    Map<String, Object> selectDetectionStatus();

    /**
     * Select approval processing status (owned, false positive)
     */
    Map<String, Object> selectApprovalStatus();
}
