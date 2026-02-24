package com.org.iopts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * FAQ Response DTO
 *
 * Maps to pi_faq table columns:
 *   faq_no (PK auto), faq_title, faq_content, faq_create_dt, faq_status, faq_user_sosok, user_no
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaqResponse {

    private Long faqId;
    private String userNo;
    private String question;        // mapped from faq_title
    private String answer;          // mapped from faq_content
    private LocalDateTime regDt;    // mapped from faq_create_dt
    private String regUserName;     // joined from pi_user.USER_NAME
}
