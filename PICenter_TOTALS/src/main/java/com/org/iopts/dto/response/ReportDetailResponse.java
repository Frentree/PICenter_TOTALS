package com.org.iopts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Report Detail Response DTO
 *
 * Contains detailed per-target report data including detection breakdown,
 * processing status, and individual detection records.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDetailResponse {

    private Long targetId;

    private String targetName;

    private String targetIp;

    private Long detectionCount;

    private Long processedCount;

    private Long truePositiveCount;

    private Long falsePositiveCount;

    private Long remediatedCount;

    private String lastScanDate;

    private List<Map<String, Object>> detections;
}
