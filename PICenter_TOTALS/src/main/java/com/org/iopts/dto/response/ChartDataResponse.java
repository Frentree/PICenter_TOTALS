package com.org.iopts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Chart Data Response DTO
 *
 * Generic chart data structure supporting pie charts, bar charts,
 * and other visualization types with labels and datasets.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChartDataResponse {

    private String chartType;

    private List<Map<String, Object>> labels;

    private List<Map<String, Object>> datasets;
}
