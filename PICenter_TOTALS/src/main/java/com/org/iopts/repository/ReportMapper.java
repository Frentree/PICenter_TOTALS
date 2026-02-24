package com.org.iopts.repository;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Report MyBatis Mapper
 *
 * Provides SQL operations for report data retrieval including
 * summary reports, monthly reports, detailed per-target reports,
 * and Excel export data queries.
 * Namespace: com.org.iopts.repository.ReportMapper
 */
@Mapper
public interface ReportMapper {

    /**
     * Select report summary (aggregate counts across all targets)
     */
    Map<String, Object> selectReportSummary(Map<String, Object> params);

    /**
     * Select monthly report data for trend analysis
     */
    List<Map<String, Object>> selectMonthlyReport(Map<String, Object> params);

    /**
     * Select detailed report data per target (paginated)
     */
    List<Map<String, Object>> selectDetailedReport(Map<String, Object> params);

    /**
     * Count total records for detailed report pagination
     */
    long selectDetailedReportCount(Map<String, Object> params);

    /**
     * Select target-specific report data
     */
    List<Map<String, Object>> selectTargetReport(Map<String, Object> params);

    /**
     * Select data for Excel export
     */
    List<Map<String, Object>> selectExcelData(Map<String, Object> params);
}
