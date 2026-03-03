package com.org.iopts.service;

import com.org.iopts.dto.response.ChartDataResponse;
import com.org.iopts.dto.response.DashboardResponse;
import com.org.iopts.dto.response.SystemCurrentResponse;

import java.util.List;
import java.util.Map;

/**
 * Dashboard Service Interface
 *
 * Provides operations for dashboard data retrieval including
 * aggregated info, datatype status, system current state,
 * chart data, and ranking information.
 */
public interface DashboardService {

    /**
     * Get aggregated dashboard info
     *
     * @param groupId  optional group filter
     * @param startDate optional start date filter
     * @param endDate   optional end date filter
     * @return aggregated dashboard response
     */
    DashboardResponse getDashboardInfo(String groupId, String startDate, String endDate);

    /**
     * Get detection counts grouped by datatype
     *
     * @param groupId optional group filter
     * @return list of datatype status maps
     */
    List<Map<String, Object>> getDatatypeStatus(String groupId);

    /**
     * Get current system status
     *
     * @return system current response
     */
    SystemCurrentResponse getSystemCurrent();

    /**
     * Get detection path current status
     *
     * @param groupId optional group filter
     * @return list of path status maps
     */
    List<Map<String, Object>> getPathCurrent(String groupId);

    /**
     * Get recent dashboard detections
     *
     * @param groupId optional group filter
     * @param limit   maximum number of results
     * @return list of detection maps
     */
    List<Map<String, Object>> getDashboardDetections(String groupId, int limit);

    /**
     * Get pie chart data
     *
     * @param groupId   optional group filter
     * @param startDate optional start date filter
     * @param endDate   optional end date filter
     * @return chart data response for pie chart
     */
    ChartDataResponse getPieChartData(String groupId, String startDate, String endDate);

    /**
     * Get bar chart data
     *
     * @param groupId   optional group filter
     * @param startDate optional start date filter
     * @param endDate   optional end date filter
     * @return chart data response for bar chart
     */
    ChartDataResponse getBarChartData(String groupId, String startDate, String endDate);

    /**
     * Get grid data for dashboard table
     *
     * @param groupId   optional group filter
     * @param startDate optional start date filter
     * @param endDate   optional end date filter
     * @return list of grid data maps
     */
    List<Map<String, Object>> getGridData(String groupId, String startDate, String endDate);

    /**
     * Get ranking data (top targets by detection count)
     *
     * @param groupId optional group filter
     * @param limit   maximum number of results
     * @return list of ranking data maps
     */
    List<Map<String, Object>> getRankingData(String groupId, int limit);

    /**
     * Get detailed agent status
     * @return map with connected, disconnected, total agent counts
     */
    Map<String, Object> getSystemAgents();

    /**
     * Get todo items (pending approvals, scheduled scans)
     * @return map with pending counts
     */
    Map<String, Object> getTodoItems();

    /**
     * Get remediation implementation status
     * @return map with implementation progress data
     */
    Map<String, Object> getImplementationStatus();

    /**
     * Get last scan information
     * @return map with last scan date and result
     */
    Map<String, Object> getLastScan();

    /**
     * Get completion statistics
     * @return map with completion data
     */
    Map<String, Object> getCompletionStats();

    /**
     * Get detection status (file counts)
     * @return map with find_path, processing_path, inaccess_path
     */
    Map<String, Object> getDetectionStatus();

    /**
     * Get approval status (owned/false-positive)
     * @return map with approval_file, approval_false
     */
    Map<String, Object> getApprovalStatus();
}
