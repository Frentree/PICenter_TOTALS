package com.org.iopts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dashboard Response DTO
 *
 * Contains aggregated dashboard information including target counts,
 * detection counts, processing status, and true/false positive breakdown.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private Long totalTargets;

    private Long totalDetections;

    private Long totalProcessed;

    private Long totalUnprocessed;

    private Long truePositiveCount;

    private Long falsePositiveCount;

    private String lastScanDate;
}
