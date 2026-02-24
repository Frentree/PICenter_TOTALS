package com.org.iopts.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Policy Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyRequest {

    @Schema(description = "정책명", example = "기본 개인정보 탐지 정책")
    @NotBlank(message = "Policy name is required")
    private String policyName;

    @Schema(description = "데이터타입 ID 목록 (콤마 구분)", example = "1,2,3")
    private String datatypeIds;

    @Schema(description = "위치 ID 목록 (콤마 구분)", example = "1,2")
    private String locationIds;

    @Schema(description = "정책 설명", example = "주민번호, 카드번호, 이메일 탐지")
    private String description;

    @Schema(description = "사용 여부", example = "Y")
    private Character useYn;
}
