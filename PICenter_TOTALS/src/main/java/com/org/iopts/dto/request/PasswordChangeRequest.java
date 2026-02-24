package com.org.iopts.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Password Change Request DTO
 *
 * Used when a user changes their own password.
 * Requires current password verification.
 * Legacy: POST /changePwd (form-based)
 * New: PUT /api/v1/users/{userNo}/password (JSON body)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequest {

    @Schema(description = "현재 비밀번호", example = "OldPass123!")
    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @Schema(description = "새 비밀번호", example = "NewPass456!")
    @NotBlank(message = "New password is required")
    private String newPassword;

    @Schema(description = "새 비밀번호 확인", example = "NewPass456!")
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}
