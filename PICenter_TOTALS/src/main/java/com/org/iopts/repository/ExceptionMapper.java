package com.org.iopts.repository;

import com.org.iopts.dto.response.PathExceptionResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Exception MyBatis Mapper
 * Maps to ExceptionMapper.xml
 */
@Mapper
public interface ExceptionMapper {

    /**
     * Select path exception list with pagination
     */
    List<PathExceptionResponse> selectPathExceptionList(Map<String, Object> params);

    /**
     * Count total path exceptions
     */
    long selectPathExceptionListCount(Map<String, Object> params);

    /**
     * Insert a new path exception
     */
    int insertPathException(Map<String, Object> params);

    /**
     * Delete a path exception by ID
     */
    int deletePathException(@Param("exceptionId") Long exceptionId);
}
