package com.org.iopts.service.impl;

import com.org.iopts.dto.request.PathExceptionRequest;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.dto.response.PathExceptionResponse;
import com.org.iopts.exception.CustomException;
import com.org.iopts.exception.ErrorCode;
import com.org.iopts.repository.ExceptionMapper;
import com.org.iopts.service.ExceptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Exception Service Implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExceptionServiceImpl implements ExceptionService {

    private final ExceptionMapper exceptionMapper;

    /**
     * Get path exception list with pagination
     */
    @Override
    public PageResponse<PathExceptionResponse> getPathExceptionList(int page, int size) {
        log.info("Fetching path exception list - page: {}, size: {}", page, size);

        Map<String, Object> params = new HashMap<>();
        params.put("offset", page * size);
        params.put("limit", size);

        List<PathExceptionResponse> content = exceptionMapper.selectPathExceptionList(params);
        long totalElements = exceptionMapper.selectPathExceptionListCount(params);

        return PageResponse.of(content, page, size, totalElements);
    }

    /**
     * Create a new path exception
     */
    @Override
    @Transactional
    public void createPathException(PathExceptionRequest request, String userNo) {
        log.info("Creating path exception - path: {}, type: {}, userNo: {}",
                request.getExceptionPath(), request.getExceptionType(), userNo);

        Map<String, Object> params = new HashMap<>();
        params.put("exceptionPath", request.getExceptionPath());
        params.put("exceptionType", request.getExceptionType());
        params.put("description", request.getDescription());
        params.put("targetId", request.getTargetId());
        params.put("regUser", String.valueOf(userNo));

        int result = exceptionMapper.insertPathException(params);
        if (result == 0) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR,
                    "Failed to create path exception");
        }

        log.info("Path exception created successfully - path: {}", request.getExceptionPath());
    }

    /**
     * Delete (soft-delete) a path exception
     */
    @Override
    @Transactional
    public void deletePathException(Long exceptionId) {
        log.info("Deleting path exception - exceptionId: {}", exceptionId);

        int result = exceptionMapper.deletePathException(exceptionId);
        if (result == 0) {
            throw new CustomException(ErrorCode.NOT_FOUND,
                    "Path exception not found: " + exceptionId);
        }

        log.info("Path exception deleted successfully - exceptionId: {}", exceptionId);
    }
}
