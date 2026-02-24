package com.org.iopts.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dashboard Request DTO
 *
 * Used for dashboard data queries with optional group and date range filtering.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardRequest {

    @Schema(description = "그룹 ID", example = "GRP001")
    private String groupId;

    @Schema(description = "시작일", example = "2026-01-01")
    private String startDate;

    @Schema(description = "종료일", example = "2026-12-31")
    private String endDate;

    @Schema(description = "타겟 ID", example = "1")
    private String targetId;
}
