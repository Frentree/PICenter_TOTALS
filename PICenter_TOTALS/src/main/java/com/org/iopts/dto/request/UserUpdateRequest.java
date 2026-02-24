package com.org.iopts.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * User Update Request DTO
 *
 * Used for updating an existing user account.
 * All fields are optional; only provided fields will be updated.
 * Legacy: POST /memberUpdate (form-based)
 * New: PUT /api/v1/users/{userNo} (JSON body)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    @Schema(description = "사용자명", example = "김철수")
    @Size(max = 100)
    private String userName;

    @Schema(description = "사용자 등급", example = "MANAGER")
    private String userGrade;

    @Schema(description = "팀 코드", example = "TEAM002")
    private String teamCode;

    @Schema(description = "이메일", example = "user001@company.com")
    private String email;

    @Schema(description = "전화번호", example = "010-9876-5432")
    private String phone;

    @Schema(description = "사용 여부 (Y/N)", example = "Y")
    private Character useYn;
}
