package com.org.iopts.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Target User Assignment Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TargetUserRequest {

    @Schema(description = "타겟 ID (Path에서 전달 시 생략 가능)", example = "1")
    private String targetId;

    @Schema(description = "사용자 번호", example = "1")
    @NotNull(message = "User number is required")
    private String userNo;
}
