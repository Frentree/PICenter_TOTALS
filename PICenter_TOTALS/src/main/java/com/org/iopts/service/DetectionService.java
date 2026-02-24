package com.org.iopts.service;

import com.org.iopts.dto.request.DetectionSearchRequest;
import com.org.iopts.dto.request.ProcessRequest;
import com.org.iopts.dto.response.DetectionDetailResponse;
import com.org.iopts.dto.response.DetectionResponse;
import com.org.iopts.dto.response.PageResponse;

import java.util.List;
import java.util.Map;

/**
 * Detection Service Interface
 */
public interface DetectionService {

    /**
     * Get detection list with search filters and pagination
     *
     * @param request search and pagination parameters
     * @return paginated detection list
     */
    PageResponse<DetectionResponse> getDetectionList(DetectionSearchRequest request);

    /**
     * Get detection detail including process history and subpaths
     *
     * @param findId detection ID
     * @return detection detail with history
     */
    DetectionDetailResponse getDetectionDetail(String findId);

    /**
     * Get subpaths for a target
     *
     * @param targetId target ID
     * @return list of subpath information
     */
    List<Map<String, Object>> getSubpaths(String targetId);

    /**
     * Process a detection (classify as true/false positive)
     *
     * @param findId  detection ID (hash_id, varchar)
     * @param request process type and comment
     * @param userNo  authenticated user ID
     */
    void processDetection(String findId, ProcessRequest request, String userNo);

    /**
     * Remediate a detection
     *
     * @param findId          detection ID (hash_id, varchar)
     * @param remediationType remediation type
     * @param userNo          authenticated user ID
     */
    void remediateDetection(String findId, String remediationType, String userNo);
}
