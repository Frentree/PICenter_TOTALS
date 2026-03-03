package com.org.iopts.controller;

import com.org.iopts.dto.request.FaqRequest;
import com.org.iopts.dto.response.ApiResponse;
import com.org.iopts.dto.response.FaqResponse;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.service.FaqService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * FAQ Controller
 *
 * Provides REST API endpoints for FAQ CRUD operations.
 *
 * Legacy endpoint mappings:
 *   GET    /api/v1/faqs            <- /faqList
 *   POST   /api/v1/faqs            <- /faqInsert
 *   PUT    /api/v1/faqs/{faqId}    <- /faqUpdate
 *   DELETE /api/v1/faqs/{faqId}    <- /faqDelete
 */
@Slf4j
@Tag(name = "FAQ", description = "FAQ API")
@RestController
@RequestMapping("/api/v1/faqs")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    /**
     * Get paginated FAQ list
     *
     * Legacy: GET /faqList
     * New: GET /api/v1/faqs
     */
    @Operation(summary = "Get FAQ list", description = "Get paginated FAQ list with optional category filter")
    @GetMapping
    public ApiResponse<PageResponse<FaqResponse>> getFaqList(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Category filter", example = "계정관리") @RequestParam(required = false) String category) {

        log.debug("GET /api/v1/faqs - page: {}, size: {}, category: {}", page, size, category);
        PageResponse<FaqResponse> result = faqService.getFaqList(page, size, category);
        return ApiResponse.success(result);
    }

    /**
     * Get FAQ detail
     *
     * New: GET /api/v1/faqs/{faqId}
     */
    @Operation(summary = "Get FAQ detail", description = "Get FAQ detail by FAQ ID")
    @GetMapping("/{faqId}")
    public ApiResponse<FaqResponse> getFaqDetail(
            @Parameter(description = "FAQ ID", example = "1") @PathVariable Long faqId) {

        log.debug("GET /api/v1/faqs/{}", faqId);
        FaqResponse response = faqService.getFaqDetail(faqId);
        return ApiResponse.success(response);
    }

    /**
     * Create a new FAQ
     *
     * Legacy: POST /faqInsert
     * New: POST /api/v1/faqs
     */
    @Operation(summary = "Create FAQ", description = "Create a new FAQ")
    @PostMapping
    public ApiResponse<Void> createFaq(
            @Valid @RequestBody FaqRequest request,
            Authentication authentication) {

        String userNo = (String) authentication.getPrincipal();
        log.info("POST /api/v1/faqs - question: {}, by: {}", request.getQuestion(), userNo);
        faqService.createFaq(request, userNo);
        return ApiResponse.success();
    }

    /**
     * Update an existing FAQ
     *
     * Legacy: POST /faqUpdate
     * New: PUT /api/v1/faqs/{faqId}
     */
    @Operation(summary = "Update FAQ", description = "Update an existing FAQ")
    @PutMapping("/{faqId}")
    public ApiResponse<Void> updateFaq(
            @Parameter(description = "FAQ ID", example = "1") @PathVariable Long faqId,
            @Valid @RequestBody FaqRequest request,
            Authentication authentication) {

        String userNo = (String) authentication.getPrincipal();
        log.info("PUT /api/v1/faqs/{} - by: {}", faqId, userNo);
        faqService.updateFaq(faqId, request, userNo);
        return ApiResponse.success();
    }

    /**
     * Delete a FAQ
     *
     * Legacy: POST /faqDelete
     * New: DELETE /api/v1/faqs/{faqId}
     */
    @Operation(summary = "Delete FAQ", description = "Delete a FAQ (logical delete)")
    @DeleteMapping("/{faqId}")
    public ApiResponse<Void> deleteFaq(
            @Parameter(description = "FAQ ID", example = "1") @PathVariable Long faqId) {

        log.info("DELETE /api/v1/faqs/{}", faqId);
        faqService.deleteFaq(faqId);
        return ApiResponse.success();
    }
}
