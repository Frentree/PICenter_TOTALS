package com.org.iopts.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Report Search Request DTO
 *
 * Used for report queries with type, group, target, and date range filtering.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportSearchRequest {

    @Schema(description = "리포트 유형 (SUMMARY/MONTHLY/DETAIL)", example = "SUMMARY")
    private String reportType;

    @Schema(description = "그룹 ID", example = "GRP001")
    private String groupId;

    @Schema(description = "타겟 ID", example = "1")
    private String targetId;

    @Schema(description = "시작일", example = "2026-01-01")
    private String startDate;

    @Schema(description = "종료일", example = "2026-12-31")
    private String endDate;

    @Schema(description = "페이지 번호", example = "0")
    @Builder.Default
    private int page = 0;

    @Schema(description = "페이지 크기", example = "20")
    @Builder.Default
    private int size = 20;
}
