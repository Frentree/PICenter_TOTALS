package com.org.iopts.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Target Search Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TargetSearchRequest {

    @Schema(description = "검색 유형 (TARGET_NAME/TARGET_IP/AGENT_VERSION)", example = "TARGET_NAME")
    private String searchType;

    @Schema(description = "검색어", example = "서버")
    private String searchKeyword;

    @Schema(description = "그룹 ID", example = "GRP001")
    private String groupId;

    @Schema(description = "에이전트 상태 (ACTIVE/INACTIVE)", example = "ACTIVE")
    private String agentStatus;

    @Schema(description = "타겟 OS (WINDOWS/LINUX)", example = "LINUX")
    private String targetOs;

    @Schema(description = "페이지 번호 (0부터)", example = "0")
    @Builder.Default
    private int page = 0;

    @Schema(description = "페이지 크기", example = "20")
    @Builder.Default
    private int size = 20;
}
