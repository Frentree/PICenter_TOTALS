package com.org.iopts.repository;

import com.org.iopts.dto.response.ApprovalResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Approval MyBatis Mapper
 * Maps to ApprovalMapper.xml
 */
@Mapper
public interface ApprovalMapper {

    /**
     * Select approval list with pagination and optional status filter
     */
    List<ApprovalResponse> selectApprovalList(Map<String, Object> params);

    /**
     * Count total approvals matching filter
     */
    long selectApprovalListCount(Map<String, Object> params);

    /**
     * Select approval detail by approval ID
     */
    ApprovalResponse selectApprovalDetail(@Param("approvalId") Long approvalId);

    /**
     * Update approval status (approve, reject, cancel)
     */
    int updateApprovalStatus(Map<String, Object> params);
}
