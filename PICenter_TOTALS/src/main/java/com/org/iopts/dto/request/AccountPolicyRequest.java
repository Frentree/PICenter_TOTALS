package com.org.iopts.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * Account Policy Request DTO
 *
 * Used for configuring account security policies such as
 * login failure limits, password expiry, and complexity requirements.
 * Legacy: POST /saveAccountPolicy (form-based)
 * New: PUT /api/v1/users/account-policy (JSON body)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountPolicyRequest {

    @Schema(description = "로그인 실패 제한 횟수", example = "5")
    private Integer loginFailLimit;

    @Schema(description = "비밀번호 만료 일수", example = "90")
    private Integer passwordExpireDays;

    @Schema(description = "계정 잠금 일수", example = "30")
    private Integer accountLockDays;

    @Schema(description = "비밀번호 복잡도 적용 여부", example = "Y")
    private Character passwordComplexYn;

    @Schema(description = "비밀번호 최소 길이", example = "8")
    private Integer passwordMinLength;

    @Schema(description = "세션 타임아웃 (분)", example = "30")
    private Integer sessionTimeout;
}
