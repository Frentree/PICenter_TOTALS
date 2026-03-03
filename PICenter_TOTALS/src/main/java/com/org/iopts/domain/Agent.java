package com.org.iopts.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Agent Entity - Maps to PI_AGENTS table
 * MyBatis only (no JPA annotations)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Agent {

    private Long agentId;
    private String targetId;
    private String agentVersion;
    private String agentStatus;
    private String agentIp;
    private Integer agentPort;
    private LocalDateTime lastConnDt;
    private LocalDateTime regDt;
}
