package com.org.iopts.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Notice Entity - Maps to PI_NOTICE table
 * MyBatis only (no JPA annotations)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notice {

    private Long noticeId;
    private String title;
    private String content;
    private String noticeType;
    private Character topFixYn;
    private Integer viewCount;
    private Character useYn;
    private LocalDateTime regDt;
    private String regUser;
    private LocalDateTime modDt;
    private String modUser;
}
