package com.org.iopts.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * TargetUser Entity - Maps to PI_TARGET_USER table
 * MyBatis only (no JPA annotations)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TargetUser {

    private Long id;
    private String targetId;
    private String userNo;
    private LocalDateTime regDt;
    private String regUser;
}
