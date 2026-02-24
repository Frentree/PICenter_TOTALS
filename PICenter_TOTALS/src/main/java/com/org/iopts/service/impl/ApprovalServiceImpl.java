package com.org.iopts.service.impl;

import com.org.iopts.dto.request.ApprovalRequest;
import com.org.iopts.dto.response.ApprovalResponse;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.exception.CustomException;
import com.org.iopts.exception.ErrorCode;
import com.org.iopts.repository.ApprovalMapper;
import com.org.iopts.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Approval Service Implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApprovalServiceImpl implements ApprovalService {

    private final ApprovalMapper approvalMapper;

    /**
     * Get approval list with pagination and optional status filter
     */
    @Override
    public PageResponse<ApprovalResponse> getApprovalList(int page, int size, String status) {
        log.info("Fetching approval list - page: {}, size: {}, status: {}", page, size, status);

        Map<String, Object> params = new HashMap<>();
        params.put("status", status);
        params.put("offset", page * size);
        params.put("limit", size);

        List<ApprovalResponse> content = approvalMapper.selectApprovalList(params);
        long totalElements = approvalMapper.selectApprovalListCount(params);

        return PageResponse.of(content, page, size, totalElements);
    }

    /**
     * Approve an approval request
     */
    @Override
    @Transactional
    public void approve(Long approvalId, ApprovalRequest request, String userNo) {
        log.info("Approving request - approvalId: {}, userNo: {}", approvalId, userNo);

        ApprovalResponse approval = approvalMapper.selectApprovalDetail(approvalId);
        if (approval == null) {
            throw new CustomException(ErrorCode.NOT_FOUND, "Approval not found: " + approvalId);
        }

        if (!"PENDING".equals(approval.getApprovalStatus())) {
            throw new CustomException(ErrorCode.INVALID_PARAMETER,
                    "Approval is not in PENDING status: " + approval.getApprovalStatus());
        }

        Map<String, Object> params = new HashMap<>();
        params.put("approvalId", approvalId);
        params.put("approvalStatus", "APPROVED");
        params.put("approvalUser", String.valueOf(userNo));
        params.put("approvalComment", request.getApprovalComment());

        approvalMapper.updateApprovalStatus(params);
        log.info("Approval approved successfully - approvalId: {}", approvalId);
    }

    /**
     * Reject an approval request
     */
    @Override
    @Transactional
    public void reject(Long approvalId, ApprovalRequest request, String userNo) {
        log.info("Rejecting request - approvalId: {}, userNo: {}", approvalId, userNo);

        ApprovalResponse approval = approvalMapper.selectApprovalDetail(approvalId);
        if (approval == null) {
            throw new CustomException(ErrorCode.NOT_FOUND, "Approval not found: " + approvalId);
        }

        if (!"PENDING".equals(approval.getApprovalStatus())) {
            throw new CustomException(ErrorCode.INVALID_PARAMETER,
                    "Approval is not in PENDING status: " + approval.getApprovalStatus());
        }

        Map<String, Object> params = new HashMap<>();
        params.put("approvalId", approvalId);
        params.put("approvalStatus", "REJECTED");
        params.put("approvalUser", String.valueOf(userNo));
        params.put("approvalComment", request.getApprovalComment());

        approvalMapper.updateApprovalStatus(params);
        log.info("Approval rejected successfully - approvalId: {}", approvalId);
    }

    /**
     * Cancel an approval request
     */
    @Override
    @Transactional
    public void cancel(Long approvalId, String userNo) {
        log.info("Cancelling request - approvalId: {}, userNo: {}", approvalId, userNo);

        ApprovalResponse approval = approvalMapper.selectApprovalDetail(approvalId);
        if (approval == null) {
            throw new CustomException(ErrorCode.NOT_FOUND, "Approval not found: " + approvalId);
        }

        if (!"PENDING".equals(approval.getApprovalStatus())) {
            throw new CustomException(ErrorCode.INVALID_PARAMETER,
                    "Approval is not in PENDING status: " + approval.getApprovalStatus());
        }

        Map<String, Object> params = new HashMap<>();
        params.put("approvalId", approvalId);
        params.put("approvalStatus", "CANCELLED");
        params.put("approvalUser", String.valueOf(userNo));
        params.put("approvalComment", "Cancelled by user");

        approvalMapper.updateApprovalStatus(params);
        log.info("Approval cancelled successfully - approvalId: {}", approvalId);
    }
}
