package com.org.iopts.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FAQ Create/Update Request DTO
 *
 * Maps to pi_faq table columns:
 *   FAQ_TITLE, FAQ_CON
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaqRequest {

    @Schema(description = "질문 (FAQ 제목)", example = "비밀번호를 변경하려면 어떻게 하나요?")
    @NotBlank(message = "Question is required")
    private String question;

    @Schema(description = "답변 (FAQ 내용)", example = "사용자 설정 > 비밀번호 변경 메뉴에서 변경할 수 있습니다.")
    @NotBlank(message = "Answer is required")
    private String answer;
}
