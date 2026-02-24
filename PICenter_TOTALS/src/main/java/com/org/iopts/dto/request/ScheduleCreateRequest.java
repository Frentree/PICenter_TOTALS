package com.org.iopts.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Schedule Create Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleCreateRequest {

    @Schema(description = "스케줄명", example = "일간 전체 스캔")
    @NotBlank(message = "Schedule name is required")
    private String scheduleName;

    @Schema(description = "스케줄 유형 (DAILY/WEEKLY/MONTHLY)", example = "DAILY")
    private String scheduleType;

    @Schema(description = "Cron 표현식", example = "0 0 2 * * ?")
    private String cronExpression;

    @Schema(description = "정책 ID", example = "POL001")
    private String policyId;

    @Schema(description = "대상 그룹 ID", example = "GRP001")
    private String targetGroupId;

    @Schema(description = "설명", example = "매일 새벽 2시 전체 스캔 실행")
    private String description;
}
