package com.org.iopts.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Group Create/Update Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupRequest {

    @Schema(description = "그룹명", example = "개발팀 서버")
    @NotBlank(message = "Group name is required")
    private String groupName;

    @Schema(description = "상위 그룹 ID", example = "GRP001")
    private String parentGroupId;
}
