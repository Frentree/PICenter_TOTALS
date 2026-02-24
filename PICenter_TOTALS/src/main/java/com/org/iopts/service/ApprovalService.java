package com.org.iopts.service;

import com.org.iopts.dto.request.ApprovalRequest;
import com.org.iopts.dto.response.ApprovalResponse;
import com.org.iopts.dto.response.PageResponse;

/**
 * Approval Service Interface
 */
public interface ApprovalService {

    /**
     * Get approval list with pagination and optional status filter
     *
     * @param page   page number (0-based)
     * @param size   page size
     * @param status optional approval status filter
     * @return paginated approval list
     */
    PageResponse<ApprovalResponse> getApprovalList(int page, int size, String status);

    /**
     * Approve an approval request
     *
     * @param approvalId approval ID
     * @param request    approval comment
     * @param userNo     authenticated user ID
     */
    void approve(Long approvalId, ApprovalRequest request, String userNo);

    /**
     * Reject an approval request
     *
     * @param approvalId approval ID
     * @param request    rejection comment
     * @param userNo     authenticated user ID
     */
    void reject(Long approvalId, ApprovalRequest request, String userNo);

    /**
     * Cancel an approval request
     *
     * @param approvalId approval ID
     * @param userNo     authenticated user ID
     */
    void cancel(Long approvalId, String userNo);
}
