package com.org.iopts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Schedule Response DTO
 * Maps to pi_schedules: schedule_id(PK varchar50), schedule_next_scan(varchar50), etc.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponse {

    private String scheduleId;
    private String scheduleName;
    private String scheduleType;
    private String cronExpression;
    private String policyId;
    private String targetGroupId;
    private String statusYn;
    private String description;
    private String lastRunDt;
    private String nextRunDt;
    private String regDt;
    private String regUser;
    private String modDt;
    private String policyName;
}
