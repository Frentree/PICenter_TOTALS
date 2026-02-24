package com.org.iopts.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Group Entity - Maps to PI_GROUP table
 * MyBatis only (no JPA annotations)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    private String groupId;
    private String groupName;
    private String parentGroupId;
    private Integer groupLevel;
    private Integer sortOrder;
    private Character useYn;
    private LocalDateTime regDt;
    private String regUser;
}
