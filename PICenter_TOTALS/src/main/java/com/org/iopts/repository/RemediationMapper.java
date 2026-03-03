package com.org.iopts.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Remediation MyBatis Mapper
 *
 * Provides SQL operations for remediation management.
 * Namespace: com.org.iopts.repository.RemediationMapper
 */
@Mapper
public interface RemediationMapper {

    /**
     * Select paginated remediation list
     */
    List<Map<String, Object>> selectRemediationList(Map<String, Object> params);

    /**
     * Count total remediation records matching filters
     */
    long countRemediationList(Map<String, Object> params);

    /**
     * Select remediation detail
     */
    Map<String, Object> selectRemediationDetail(@Param("remediationId") Long remediationId);

    /**
     * Update remediation process (apply action)
     */
    int updateRemediationProcess(Map<String, Object> params);

    /**
     * Select remediation summary statistics
     */
    Map<String, Object> selectRemediationSummary();
}
