package com.org.iopts.service;

import com.org.iopts.dto.request.PathExceptionRequest;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.dto.response.PathExceptionResponse;

/**
 * Exception Service Interface
 */
public interface ExceptionService {

    /**
     * Get path exception list with pagination
     *
     * @param page page number (0-based)
     * @param size page size
     * @return paginated path exception list
     */
    PageResponse<PathExceptionResponse> getPathExceptionList(int page, int size);

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
