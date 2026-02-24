package com.org.iopts.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Notice Create/Update Request DTO
 *
 * Maps to pi_notice table columns:
 *   NOTICE_TITLE, NOTICE_CON, NOTICE_CHK, NOTICE_ALERT, NOTICE_FILE_ID
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeRequest {

    @Schema(description = "공지사항 제목", example = "시스템 점검 안내")
    @NotBlank(message = "Title is required")
    private String title;

    @Schema(description = "공지사항 내용", example = "2026년 3월 1일 시스템 점검이 예정되어 있습니다.")
    @NotBlank(message = "Content is required")
    private String content;

    @Schema(description = "공지 확인 여부", example = "Y")
    private String noticeChk;

    @Schema(description = "공지 알림 여부", example = "Y")
    private String noticeAlert;

    @Schema(description = "공지 첨부파일 ID", example = "12")
    private String noticeFileId;
}
