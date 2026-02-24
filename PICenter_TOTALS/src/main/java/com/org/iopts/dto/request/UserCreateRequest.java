package com.org.iopts.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * User Create Request DTO
 *
 * Used for creating a new user account.
 * Legacy: POST /memberInsert (form-based)
 * New: POST /api/v1/users (JSON body)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {

    @Schema(description = "사용자 ID", example = "user001")
    @NotBlank(message = "User ID is required")
    @Size(max = 50)
    private String userId;

    @Schema(description = "비밀번호", example = "Pass1234!")
    @NotBlank(message = "Password is required")
    private String userPwd;

    @Schema(description = "사용자명", example = "홍길동")
    @NotBlank(message = "User name is required")
    @Size(max = 100)
    private String userName;

    @Schema(description = "사용자 등급 (ADMIN/MANAGER/USER)", example = "USER")
    private String userGrade;

    @Schema(description = "팀 코드", example = "TEAM001")
    private String teamCode;

    @Schema(description = "이메일", example = "user001@company.com")
    private String email;

    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phone;
}
