package com.org.iopts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Statistics Response DTO
 *
 * Contains overall detection statistics including counts, rates,
 * datatype breakdown, and monthly trend data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsResponse {

    private Long totalDetections;

    private Long truePositiveCount;

    private Long falsePositiveCount;

    private Double truePositiveRate;

    private Double falsePositiveRate;

    private Long processedCount;

    private Long unprocessedCount;

    private List<Map<String, Object>> datatypeStats;

    private List<Map<String, Object>> monthlyStats;
}
