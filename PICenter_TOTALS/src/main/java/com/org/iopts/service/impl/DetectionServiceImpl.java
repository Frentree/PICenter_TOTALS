package com.org.iopts.service.impl;

import com.org.iopts.domain.DataProcessing;
import com.org.iopts.domain.Subpath;
import com.org.iopts.dto.request.DetectionSearchRequest;
import com.org.iopts.dto.request.ProcessRequest;
import com.org.iopts.dto.response.DetectionDetailResponse;
import com.org.iopts.dto.response.DetectionResponse;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.exception.CustomException;
import com.org.iopts.exception.ErrorCode;
import com.org.iopts.repository.DetectionMapper;
import com.org.iopts.service.DetectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Detection Service Implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DetectionServiceImpl implements DetectionService {

    private final DetectionMapper detectionMapper;

    /**
     * Get detection list with search filters and pagination
     */
    @Override
    public PageResponse<DetectionResponse> getDetectionList(DetectionSearchRequest request) {
        log.info("Fetching detection list - page: {}, size: {}, status: {}",
                request.getPage(), request.getSize(), request.getProcessStatus());

        Map<String, Object> params = new HashMap<>();
        params.put("searchType", request.getSearchType());
        params.put("searchKeyword", request.getSearchKeyword());
        params.put("targetId", request.getTargetId());
        params.put("processStatus", request.getProcessStatus());
        params.put("datatypeName", request.getDatatypeName());
        params.put("startDate", request.getStartDate());
        params.put("endDate", request.getEndDate());
        params.put("offset", request.getPage() * request.getSize());
        params.put("limit", request.getSize());

        List<DetectionResponse> content = detectionMapper.selectDetectionList(params);
        long totalElements = detectionMapper.selectDetectionListCount(params);

        return PageResponse.of(content, request.getPage(), request.getSize(), totalElements);
    }

    /**
     * Get detection detail including process history and subpaths
     */
    @Override
    public DetectionDetailResponse getDetectionDetail(String findId) {
        log.info("Fetching detection detail - findId: {}", findId);

        DetectionResponse detection = detectionMapper.selectDetectionDetail(findId);
        if (detection == null) {
            throw new CustomException(ErrorCode.DETECTION_NOT_FOUND);
        }

        // Fetch process history
        List<Map<String, Object>> rawHistory = detectionMapper.selectProcessHistory(findId);
        List<DataProcessing> processHistory = rawHistory.stream()
                .map(this::mapToDataProcessing)
                .collect(Collectors.toList());

        // Fetch subpaths
        List<Map<String, Object>> rawSubpaths = detectionMapper.selectSubpaths(detection.getTargetId());
        List<Subpath> subpaths = rawSubpaths.stream()
                .map(this::mapToSubpath)
                .collect(Collectors.toList());

        return DetectionDetailResponse.builder()
                .findId(detection.getFindId())
                .targetId(detection.getTargetId())
                .targetName(detection.getTargetName())
                .filePath(detection.getFilePath())
                .fileName(detection.getFileName())
                .datatypeName(detection.getDatatypeName())
                .patternName(detection.getPatternName())
                .matchData(detection.getMatchData())
                .matchCount(detection.getMatchCount())
                .processStatus(detection.getProcessStatus())
                .processType(detection.getProcessType())
                .processUser(detection.getProcessUser())
                .processDt(detection.getProcessDt())
                .findDt(detection.getFindDt())
                .regDt(detection.getRegDt())
                .processHistory(processHistory)
                .subpaths(subpaths)
                .build();
    }

    /**
     * Get subpaths for a target
     */
    @Override
    public List<Map<String, Object>> getSubpaths(String targetId) {
        log.info("Fetching subpaths - targetId: {}", targetId);
        return detectionMapper.selectSubpaths(targetId);
    }

    /**
     * Process a detection (classify as true/false positive)
     */
    @Override
    @Transactional
    public void processDetection(String findId, ProcessRequest request, String userNo) {
        log.info("Processing detection - findId: {}, type: {}, userNo: {}",
                findId, request.getProcessType(), userNo);

        // Verify detection exists
        DetectionResponse detection = detectionMapper.selectDetectionDetail(findId);
        if (detection == null) {
            throw new CustomException(ErrorCode.DETECTION_NOT_FOUND);
        }

        String processUser = String.valueOf(userNo);

        // Insert process record
        Map<String, Object> processParams = new HashMap<>();
        processParams.put("findId", findId);
        processParams.put("processType", request.getProcessType());
        processParams.put("processResult", "PROCESSED");
        processParams.put("processUser", processUser);
        processParams.put("processComment", request.getProcessComment());
        detectionMapper.insertProcess(processParams);

        // Update detection status
        Map<String, Object> statusParams = new HashMap<>();
        statusParams.put("findId", findId);
        statusParams.put("processStatus", "PROCESSED");
        statusParams.put("processType", request.getProcessType());
        statusParams.put("processUser", processUser);
        detectionMapper.updateDetectionStatus(statusParams);

        log.info("Detection processed successfully - findId: {}", findId);
    }

    /**
     * Remediate a detection
     */
    @Override
    @Transactional
    public void remediateDetection(String findId, String remediationType, String userNo) {
        log.info("Remediating detection - findId: {}, type: {}, userNo: {}",
                findId, remediationType, userNo);

        // Verify detection exists
        DetectionResponse detection = detectionMapper.selectDetectionDetail(findId);
        if (detection == null) {
            throw new CustomException(ErrorCode.DETECTION_NOT_FOUND);
        }

        String processUser = String.valueOf(userNo);

        // Insert remediation record
        Map<String, Object> remediationParams = new HashMap<>();
        remediationParams.put("findId", findId);
        remediationParams.put("remediationType", remediationType);
        remediationParams.put("processUser", processUser);
        remediationParams.put("processComment", "Remediation: " + remediationType);
        detectionMapper.insertRemediation(remediationParams);

        // Update detection status to REMEDIATED
        Map<String, Object> statusParams = new HashMap<>();
        statusParams.put("findId", findId);
        statusParams.put("processStatus", "REMEDIATED");
        statusParams.put("processType", remediationType);
        statusParams.put("processUser", processUser);
        detectionMapper.updateDetectionStatus(statusParams);

        log.info("Detection remediated successfully - findId: {}", findId);
    }

    /**
     * Map raw Map to DataProcessing domain object
     */
    private DataProcessing mapToDataProcessing(Map<String, Object> raw) {
        return DataProcessing.builder()
                .processId(toLong(raw.get("processId")))
                .findId(raw.get("findId") != null ? raw.get("findId").toString() : null)
                .processType((String) raw.get("processType"))
                .processResult((String) raw.get("processResult"))
                .processUser((String) raw.get("processUser"))
                .processComment((String) raw.get("processComment"))
                .processDt(raw.get("processDt") instanceof LocalDateTime
                        ? (LocalDateTime) raw.get("processDt") : null)
                .build();
    }

    /**
     * Map raw Map to Subpath domain object
     */
    private Subpath mapToSubpath(Map<String, Object> raw) {
        return Subpath.builder()
                .subpathId(raw.get("subpathId") != null ? raw.get("subpathId").toString() : null)
                .targetId(raw.get("targetId") != null ? raw.get("targetId").toString() : null)
                .filePath((String) raw.get("filePath"))
                .fileName((String) raw.get("fileName"))
                .fileSize(toLong(raw.get("fileSize")))
                .fileDt(raw.get("fileDt") instanceof LocalDateTime
                        ? (LocalDateTime) raw.get("fileDt") : null)
                .build();
    }

    /**
     * Safely convert Object to Long
     */
    private Long toLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return Long.parseLong(value.toString());
    }
}
