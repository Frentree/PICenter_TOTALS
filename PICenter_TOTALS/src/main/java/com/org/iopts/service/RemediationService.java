package com.org.iopts.service;

import com.org.iopts.dto.response.PageResponse;

import java.util.Map;

/**
 * Remediation Service Interface
 *
 * Provides operations for remediation management including
 * listing, processing, and batch operations.
 *
 * Legacy mappings:
 *   - getRemediationList    -> /remediation/list
 *   - getRemediationDetail  -> /remediation/detail
 *   - processRemediation    -> /remediation/process
 *   - getRemediationSummary -> /remediation/summary
 *   - batchRemediation      -> /remediation/batch
 */
public interface RemediationService {

    /**
     * Get paginated remediation list
     *
     * @param page    page number (0-based)
     * @param size    page size
     * @param keyword optional search keyword
     * @param status  optional status filter
     * @return paginated remediation list
     */
    PageResponse<Map<String, Object>> getRemediationList(int page, int size, String keyword, String status);

    /**
     * Get remediation detail
     *
     * @param remediationId remediation ID
     * @return remediation detail map
     */
    Map<String, Object> getRemediationDetail(Long remediationId);

    /**
     * Process remediation for a finding
     *
     * @param findId  finding ID
     * @param request remediation request data
     * @param userNo  user performing the remediation
     */
    void processRemediation(String findId, Map<String, Object> request, String userNo);

    /**
     * Get remediation summary statistics
     *
     * @return summary statistics map
     */
    Map<String, Object> getRemediationSummary();

    /**
     * Batch remediation for multiple findings
     *
     * @param request batch request data containing finding IDs and action
     * @param userNo  user performing the batch remediation
     * @return result map with success/failure counts
     */
    Map<String, Object> batchRemediation(Map<String, Object> request, String userNo);
}
