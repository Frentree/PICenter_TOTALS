package com.org.iopts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Download Response DTO
 *
 * Maps to pi_download table columns:
 *   download_id (PK auto), download_file_id (int), user_no, download_con (varchar2000),
 *   download_title (text), regdate (datetime)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DownloadResponse {

    private Long downloadId;
    private String userNo;
    private String title;           // mapped from download_title
    private String content;         // mapped from download_con
    private String downloadFileId;
    private LocalDateTime regDt;    // mapped from REGDATE
    private String regUserName;     // joined from pi_user.USER_NAME
}
