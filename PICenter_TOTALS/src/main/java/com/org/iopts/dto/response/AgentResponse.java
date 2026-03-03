package com.org.iopts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Agent Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentResponse {

    private Long agentId;
    private String targetId;
    private String agentVersion;
    private String agentStatus;
    private String agentIp;
    private Integer agentPort;
    private LocalDateTime lastConnDt;
    private LocalDateTime regDt;
    private String targetName;
}
