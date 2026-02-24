package com.org.iopts.repository;

import com.org.iopts.dto.response.DetectionResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Detection MyBatis Mapper
 * Maps to DetectionMapper.xml
 */
@Mapper
public interface DetectionMapper {

    /**
     * Select detection list with search filters and pagination
     */
    List<DetectionResponse> selectDetectionList(Map<String, Object> params);

    /**
     * Count total detections matching search filters
     */
    long selectDetectionListCount(Map<String, Object> params);

    /**
     * Select detection detail by find ID
     */
    DetectionResponse selectDetectionDetail(@Param("findId") String findId);

    /**
     * Select process history for a detection
     */
    List<Map<String, Object>> selectProcessHistory(@Param("findId") String findId);

    /**
     * Select subpaths for a target
     */
    List<Map<String, Object>> selectSubpaths(@Param("targetId") String targetId);

    /**
     * Insert a new process record
     */
    int insertProcess(Map<String, Object> params);

    /**
     * Update detection processing status
     */
    int updateDetectionStatus(Map<String, Object> params);

    /**
     * Insert a remediation record
     */
    int insertRemediation(Map<String, Object> params);
}
