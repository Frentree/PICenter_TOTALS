package com.org.iopts.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Path Exception Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PathExceptionRequest {

    /** Exception path pattern */
    @Schema(description = "예외 경로 패턴", example = "/home/user/temp/*")
    @NotBlank(message = "Exception path is required")
    private String exceptionPath;

    /** Exception type (DIRECTORY, FILE, PATTERN) */
    @Schema(description = "예외 유형 (DIRECTORY/FILE/PATTERN)", example = "DIRECTORY")
    private String exceptionType;

    /** Description of the exception */
    @Schema(description = "설명", example = "임시 파일 경로 예외")
    private String description;

    /** Target ID */
    @Schema(description = "타겟 ID", example = "1")
    private String targetId;
}
