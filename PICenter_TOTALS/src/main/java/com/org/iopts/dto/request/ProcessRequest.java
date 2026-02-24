package com.org.iopts.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Detection Process Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessRequest {

    /** Process type: TRUE_POSITIVE or FALSE_POSITIVE */
    @Schema(description = "처리 유형 (TRUE_POSITIVE/FALSE_POSITIVE)", example = "TRUE_POSITIVE")
    @NotBlank(message = "Process type is required")
    private String processType;

    /** Processing comment */
    @Schema(description = "처리 코멘트", example = "정탐 확인 - 주민번호 포함")
    private String processComment;
}
