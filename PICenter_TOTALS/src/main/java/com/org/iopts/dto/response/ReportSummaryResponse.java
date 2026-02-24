package com.org.iopts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Report Summary Response DTO
 *
 * Contains a high-level summary of report data including target counts,
 * detection counts, processing status, and top detection/target rankings.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportSummaryResponse {

    private String reportDate;

    private Long totalTargets;

    private Long scannedTargets;

    private Long totalDetections;

    private Long processedDetections;

    private Long truePositives;

    private Long falsePositives;

    private Long remediatedCount;

    private List<Map<String, Object>> topDetectionTypes;

    private List<Map<String, Object>> topTargets;
}
