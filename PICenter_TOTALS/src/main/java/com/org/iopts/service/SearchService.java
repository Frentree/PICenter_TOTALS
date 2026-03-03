package com.org.iopts.service;

import com.org.iopts.dto.response.PageResponse;

import java.util.List;
import java.util.Map;

/**
 * Search Service Interface
 *
 * Provides operations for search registration and result management.
 *
 * Legacy mappings:
 *   - getRegistrationList -> /searchRegistList
 *   - createRegistration  -> /searchRegistInsert
 *   - deleteRegistration  -> /searchRegistDelete
 *   - getResultList       -> /searchResultList
 *   - getResultDetail     -> /searchResultDetail
 *   - getSearchStatus     -> /searchStatus
 */
public interface SearchService {

    /**
     * Get paginated search registration list
     *
     * @param page    page number (0-based)
     * @param size    page size
     * @param keyword optional search keyword
     * @return paginated registration list
     */
    PageResponse<Map<String, Object>> getRegistrationList(int page, int size, String keyword);

    /**
     * Create a new search registration
     *
     * @param request registration data
     * @param userNo  user who created the registration
     */
    void createRegistration(Map<String, Object> request, String userNo);

    /**
     * Delete a search registration
     *
     * @param registId registration ID to delete
     */
    void deleteRegistration(Long registId);

    /**
     * Get paginated search result list
     *
     * @param page    page number (0-based)
     * @param size    page size
     * @param keyword optional search keyword
     * @param status  optional status filter
     * @return paginated result list
     */
    PageResponse<Map<String, Object>> getResultList(int page, int size, String keyword, String status, String startDate, String endDate);

    /**
     * Get search result detail
     *
     * @param resultId result ID
     * @return result detail map
     */
    Map<String, Object> getResultDetail(Long resultId);

    /**
     * Get search status summary counts
     *
     * @return status summary map
     */
    Map<String, Object> getSearchStatus();

    /**
     * Get data type (profile) list
     */
    List<Map<String, Object>> getDataTypeList(String keyword);

    /**
     * Create a new data type (profile)
     */
    void createDataType(Map<String, Object> request, String userNo);

    /**
     * Update a data type (profile)
     */
    void updateDataType(Map<String, Object> request, String userNo);

    /**
     * Delete a data type (profile) - soft delete
     */
    void deleteDataType(String stdId);
}
