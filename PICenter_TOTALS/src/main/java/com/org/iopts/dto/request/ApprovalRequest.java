package com.org.iopts.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Approval Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalRequest {

    /** Approval comment */
    @Schema(description = "승인/거절 코멘트", example = "승인합니다")
    private String approvalComment;
}
