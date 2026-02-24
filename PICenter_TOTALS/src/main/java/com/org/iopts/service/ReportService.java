package com.org.iopts.service;

import com.org.iopts.dto.request.ReportSearchRequest;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.dto.response.ReportDetailResponse;
import com.org.iopts.dto.response.ReportSummaryResponse;

import java.util.List;
import java.util.Map;

/**
 * Report Service Interface
 *
 * Provides operations for report generation including
 * summary reports, monthly reports, detailed per-target reports,
 * target-specific reports, and Excel export.
 */
public interface ReportService {

    /**
     * Get report summary
     *
     * @param groupId   optional group filter
     * @param startDate optional start date filter
     * @param endDate   optional end date filter
     * @return report summary response
     */
    ReportSummaryResponse getReportSummary(String groupId, String startDate, String endDate);

    /**
     * Get monthly report data
     *
     * @param startDate optional start date filter
     * @param endDate   optional end date filter
     * @return list of monthly report maps
     */
    List<Map<String, Object>> getMonthlyReport(String startDate, String endDate);

    /**
     * Get detailed report per target (paginated)
     *
     * @param request report search request with filters and pagination
     * @return paginated report detail response
     */
    PageResponse<ReportDetailResponse> getDetailedReport(ReportSearchRequest request);

    /**
     * Get target-specific report data
     *
     * @param groupId  optional group filter
     * @param targetId target identifier
     * @return list of target report maps
     */
    List<Map<String, Object>> getTargetReport(String groupId, String targetId);

    /**
     * Export report data as Excel
     *
     * @param request report search request with filters
     * @return Excel file as byte array
     */
    byte[] exportExcel(ReportSearchRequest request);
}
