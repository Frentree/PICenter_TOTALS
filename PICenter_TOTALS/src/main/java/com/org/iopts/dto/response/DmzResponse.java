package com.org.iopts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DMZ Response DTO
 * Maps to pi_dmz table: idx(PK auto int), dmz_ip(varchar50), memo(text)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DmzResponse {

    private Long dmzId;
    private String dmzIp;
    private String memo;
}
