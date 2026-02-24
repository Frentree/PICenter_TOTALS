package com.org.iopts.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Approval Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApprovalResponse {

    /** Approval ID */
    private Long approvalId;

    /** Detection find ID (hash_id from pi_find, text/varchar) */
    private String findId;

    /** Approval status (PENDING, APPROVED, REJECTED, CANCELLED) */
    private String approvalStatus;

    /** Approval type */
    private String approvalType;

    /** User who requested approval */
    private String requestUser;

    /** Request comment */
    private String requestComment;

    /** User who approved/rejected */
    private String approvalUser;

    /** Approval comment */
    private String approvalComment;

    /** Request date/time */
    private LocalDateTime requestDt;

    /** Approval date/time */
    private LocalDateTime approvalDt;

    /** Target name (joined from pi_targets) */
    private String targetName;

    /** File path (joined from pi_find) */
    private String filePath;
}
