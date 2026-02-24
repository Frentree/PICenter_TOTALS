package com.org.iopts.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DMZ Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DmzRequest {

    @Schema(description = "DMZ IP 주소", example = "192.168.1.100")
    @NotBlank(message = "DMZ IP is required")
    private String dmzIp;

    @Schema(description = "메모", example = "외부 망 연결 DMZ 서버")
    private String memo;
}
