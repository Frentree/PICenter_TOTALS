package com.org.iopts.service.impl;

import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.exception.CustomException;
import com.org.iopts.exception.ErrorCode;
import com.org.iopts.repository.RemediationMapper;
import com.org.iopts.service.RemediationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Remediation Service Implementation
 *
 * Handles remediation management business logic including
 * listing, detail retrieval, processing, and batch operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RemediationServiceImpl implements RemediationService {

    private final RemediationMapper remediationMapper;

    @Override
    public PageResponse<Map<String, Object>> getRemediationList(int page, int size, String keyword, String status) {
        log.debug("getRemediationList - page: {}, size: {}, keyword: {}, status: {}", page, size, keyword, status);

        Map<String, Object> params = new HashMap<>();
        params.put("offset", page * size);
        params.put("limit", size);
        if (keyword != null && !keyword.isEmpty()) {
            params.put("keyword", keyword);
        }
        if (status != null && !status.isEmpty()) {
            params.put("status", status);
        }

        List<Map<String, Object>> content = remediationMapper.selectRemediationList(params);
        long totalElements = remediationMapper.countRemediationList(params);

        return PageResponse.of(content, page, size, totalElements);
    }

    @Override
    public Map<String, Object> getRemediationDetail(Long remediationId) {
        log.debug("getRemediationDetail - remediationId: {}", remediationId);

        Map<String, Object> result = remediationMapper.selectRemediationDetail(remediationId);
        if (result == null) {
            throw new CustomException(ErrorCode.NOT_FOUND, "Remediation not found: " + remediationId);
        }

        return result;
    }

    @Override
    @Transactional
    public void processRemediation(String findId, Map<String, Object> request, String userNo) {
        log.info("processRemediation - findId: {}, userNo: {}", findId, userNo);

        request.put("hashId", findId);
        request.put("userNo", userNo);

        int result = remediationMapper.updateRemediationProcess(request);
        if (result == 0) {
            throw new CustomException(ErrorCode.NOT_FOUND, "Finding not found: " + findId);
        }

        log.info("processRemediation success - findId: {}", findId);
    }

    @Override
    public Map<String, Object> getRemediationSummary() {
        log.debug("getRemediationSummary");
        return remediationMapper.selectRemediationSummary();
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public Map<String, Object> batchRemediation(Map<String, Object> request, String userNo) {
        log.info("batchRemediation - userNo: {}", userNo);

        List<String> findIds = (List<String>) request.get("findIds");
        String action = (String) request.get("action");

        if (findIds == null || findIds.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_PARAMETER, "Finding IDs must not be empty");
        }

        int successCount = 0;
        int failCount = 0;

        for (String findId : findIds) {
            try {
                Map<String, Object> processRequest = new HashMap<>();
                processRequest.put("hashId", findId);
                processRequest.put("userNo", userNo);
                processRequest.put("action", action);

                int result = remediationMapper.updateRemediationProcess(processRequest);
                if (result > 0) {
                    successCount++;
                } else {
                    failCount++;
                }
            } catch (Exception e) {
                log.warn("Failed to process remediation for findId: {}", findId, e);
                failCount++;
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("totalCount", findIds.size());
        response.put("successCount", successCount);
        response.put("failCount", failCount);

        log.info("batchRemediation complete - success: {}, fail: {}", successCount, failCount);
        return response;
    }
}
