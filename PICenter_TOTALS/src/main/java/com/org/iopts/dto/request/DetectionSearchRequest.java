package com.org.iopts.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Detection Search Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetectionSearchRequest {

    /** Search type (TARGET_NAME, FILE_PATH, FILE_NAME, PATTERN_NAME, DATATYPE_NAME) */
    @Schema(description = "검색 유형 (TARGET_NAME/FILE_PATH/FILE_NAME/PATTERN_NAME/DATATYPE_NAME)", example = "TARGET_NAME")
    private String searchType;

    /** Search keyword */
    @Schema(description = "검색어", example = "서버")
    private String searchKeyword;

    /** Target ID filter */
    @Schema(description = "타겟 ID", example = "1")
    private String targetId;

    /** Process status filter (PENDING, PROCESSED, APPROVED) */
    @Schema(description = "처리 상태 (PENDING/PROCESSED/APPROVED)", example = "PENDING")
    private String processStatus;

    /** Data type name filter */
    @Schema(description = "데이터타입명", example = "주민등록번호")
    private String datatypeName;

    /** Search start date (yyyy-MM-dd) */
    @Schema(description = "시작일 (yyyy-MM-dd)", example = "2026-01-01")
    private String startDate;

    /** Search end date (yyyy-MM-dd) */
    @Schema(description = "종료일 (yyyy-MM-dd)", example = "2026-12-31")
    private String endDate;

    /** Page number (0-based) */
    @Schema(description = "페이지 번호", example = "0")
    @Builder.Default
    private int page = 0;

    /** Page size */
    @Schema(description = "페이지 크기", example = "20")
    @Builder.Default
    private int size = 20;
}
