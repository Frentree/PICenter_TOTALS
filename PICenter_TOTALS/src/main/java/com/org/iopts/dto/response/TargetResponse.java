package com.org.iopts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Target Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TargetResponse {

    private Long targetId;
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
    private String groupName;
    private String assignedUserName;
}
