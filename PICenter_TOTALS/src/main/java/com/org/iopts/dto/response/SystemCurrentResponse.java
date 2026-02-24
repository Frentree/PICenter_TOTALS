package com.org.iopts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * System Current Status Response DTO
 *
 * Provides real-time system status including agent counts,
 * scan counts, and overall system health.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemCurrentResponse {

    private Long totalAgents;

    private Long activeAgents;

    private Long inactiveAgents;

    private Long totalScans;

    private Long runningScans;

    private Long scheduledScans;

    private String systemStatus;

    private String lastUpdateTime;
}
