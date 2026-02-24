package com.org.iopts.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Schedule Entity - Maps to PI_SCHEDULES table
 * MyBatis only (no JPA annotations)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {

    private Long scheduleId;
    private String scheduleName;
    private String scheduleType;
    private String cronExpression;
    private String policyId;
    private String targetGroupId;
    private Character statusYn;
    private String description;
    private LocalDateTime lastRunDt;
    private LocalDateTime nextRunDt;
    private LocalDateTime regDt;
    private String regUser;
    private LocalDateTime modDt;
}
