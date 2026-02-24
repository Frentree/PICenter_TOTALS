package com.org.iopts.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * FAQ Entity - Maps to PI_FAQ table
 * MyBatis only (no JPA annotations)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Faq {

    private Long faqId;
    private String question;
    private String answer;
    private String category;
    private Integer sortOrder;
    private Character useYn;
    private LocalDateTime regDt;
    private String regUser;
    private LocalDateTime modDt;
    private String modUser;
}
