package com.org.iopts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Group Tree Response DTO
 *
 * Maps to pi_groups table columns:
 *   GROUP_ID (PK auto_increment), GROUP_NAME, PARENT_ID, SORT_ORDER, REG_DT
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupTreeResponse {

    private String groupId;
    private String groupName;
    private String parentGroupId;   // mapped from PARENT_ID
    private Integer sortOrder;
    private LocalDateTime regDt;
    @Builder.Default
    private List<GroupTreeResponse> children = new ArrayList<>();
    private Long targetCount;
}
