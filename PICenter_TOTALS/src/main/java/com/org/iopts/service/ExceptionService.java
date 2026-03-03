package com.org.iopts.service;

import com.org.iopts.dto.request.PathExceptionRequest;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.dto.response.PathExceptionResponse;

/**
 * Exception Service Interface
 */
public interface ExceptionService {

    /**
     * Get path exception list with pagination and search filters
     *
     * @param searchKeyword search keyword
     * @param startDate start date filter
     * @param endDate end date filter
     * @param page page number (0-based)
     * @param size page size
     * @return paginated path exception list
     */
    PageResponse<PathExceptionResponse> getPathExceptionList(String searchKeyword, String startDate, String endDate, int page, int size);

    /**
     * Create a new path exception
     *
     * @param request path exception data
     * @param userNo  authenticated user ID
     */
    void createPathException(PathExceptionRequest request, String userNo);

    /**
     * Delete (soft-delete) a path exception
     *
     * @param exceptionId exception ID to delete
     */
    void deletePathException(Long exceptionId);
}
