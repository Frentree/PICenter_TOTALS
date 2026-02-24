package com.org.iopts.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Login Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @Schema(description = "사용자 ID", example = "frentree")
    @NotBlank(message = "Username is required")
    private String username;

    @Schema(description = "비밀번호", example = "fren1212!")
    @NotBlank(message = "Password is required")
    private String password;
}
