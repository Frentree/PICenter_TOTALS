package com.org.iopts.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Target Entity - Maps to PI_TARGETS table
 * MyBatis only (no JPA annotations)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Target {

    private String targetId;
    private String targetName;
    private String targetIp;
    private String targetOs;
    private String targetType;
    private String groupId;
    private String agentVersion;
    private String agentStatus;
    private Character useYn;
    private LocalDateTime regDt;
    private String regUser;
    private LocalDateTime modDt;
    private String modUser;
}
