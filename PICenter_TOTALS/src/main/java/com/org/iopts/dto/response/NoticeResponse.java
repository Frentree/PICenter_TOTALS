package com.org.iopts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Notice Response DTO
 *
 * Maps to pi_notice table columns:
 *   notice_id (PK auto), notice_file_id (int), user_no, notice_con (varchar2000),
 *   notice_title (text), notice_chk (int1), regdate (datetime), notice_alert (int1 default 0)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeResponse {

    private Long noticeId;
    private String userNo;
    private String title;           // mapped from notice_title
    private String content;         // mapped from notice_con
    private String noticeChk;
    private String noticeAlert;
    private String noticeFileId;
    private LocalDateTime regDt;    // mapped from REGDATE
    private String regUserName;     // joined from pi_user.USER_NAME
}
