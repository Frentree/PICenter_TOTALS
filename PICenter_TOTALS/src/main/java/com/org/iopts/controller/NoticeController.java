package com.org.iopts.controller;

import com.org.iopts.dto.request.NoticeRequest;
import com.org.iopts.dto.response.ApiResponse;
import com.org.iopts.dto.response.NoticeResponse;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Notice Controller
 *
 * Provides REST API endpoints for notice CRUD operations.
 *
 * Legacy endpoint mappings:
 *   GET    /api/v1/notices              <- /noticeList
 *   GET    /api/v1/notices/{noticeId}   <- /noticeDetail
 *   POST   /api/v1/notices              <- /noticeInsert
 *   PUT    /api/v1/notices/{noticeId}   <- /noticeUpdate
 *   DELETE /api/v1/notices/{noticeId}   <- /noticeDelete
 */
@Slf4j
@Tag(name = "Notice", description = "Notice API")
@RestController
@RequestMapping("/api/v1/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * Get paginated notice list
     *
     * Legacy: GET /noticeList
     * New: GET /api/v1/notices
     */
    @Operation(summary = "Get notice list", description = "Get paginated notice list with optional keyword search")
    @GetMapping
    public ApiResponse<PageResponse<NoticeResponse>> getNoticeList(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Search keyword (title or content)", example = "점검") @RequestParam(required = false) String searchKeyword) {

        log.debug("GET /api/v1/notices - page: {}, size: {}, searchKeyword: {}", page, size, searchKeyword);
        PageResponse<NoticeResponse> result = noticeService.getNoticeList(page, size, searchKeyword);
        return ApiResponse.success(result);
    }

    /**
     * Get notice detail
     *
     * Legacy: GET /noticeDetail
     * New: GET /api/v1/notices/{noticeId}
     */
    @Operation(summary = "Get notice detail", description = "Get notice detail by notice ID (increments view count)")
    @GetMapping("/{noticeId}")
    public ApiResponse<NoticeResponse> getNoticeDetail(
            @Parameter(description = "Notice ID", example = "1") @PathVariable Long noticeId) {

        log.debug("GET /api/v1/notices/{}", noticeId);
        NoticeResponse result = noticeService.getNoticeDetail(noticeId);
        return ApiResponse.success(result);
    }

    /**
     * Create a new notice
     *
     * Legacy: POST /noticeInsert
     * New: POST /api/v1/notices
     */
    @Operation(summary = "Create notice", description = "Create a new notice")
    @PostMapping
    public ApiResponse<Void> createNotice(
            @Valid @RequestBody NoticeRequest request,
            Authentication authentication) {

        String userNo = (String) authentication.getPrincipal();
        log.info("POST /api/v1/notices - title: {}, by: {}", request.getTitle(), userNo);
        noticeService.createNotice(request, userNo);
        return ApiResponse.success();
    }

    /**
     * Update an existing notice
     *
     * Legacy: POST /noticeUpdate
     * New: PUT /api/v1/notices/{noticeId}
     */
    @Operation(summary = "Update notice", description = "Update an existing notice")
    @PutMapping("/{noticeId}")
    public ApiResponse<Void> updateNotice(
            @Parameter(description = "Notice ID", example = "1") @PathVariable Long noticeId,
            @Valid @RequestBody NoticeRequest request,
            Authentication authentication) {

        String userNo = (String) authentication.getPrincipal();
        log.info("PUT /api/v1/notices/{} - by: {}", noticeId, userNo);
        noticeService.updateNotice(noticeId, request, userNo);
        return ApiResponse.success();
    }

    /**
     * Delete a notice
     *
     * Legacy: POST /noticeDelete
     * New: DELETE /api/v1/notices/{noticeId}
     */
    @Operation(summary = "Delete notice", description = "Delete a notice (logical delete)")
    @DeleteMapping("/{noticeId}")
    public ApiResponse<Void> deleteNotice(
            @Parameter(description = "Notice ID", example = "1") @PathVariable Long noticeId) {

        log.info("DELETE /api/v1/notices/{}", noticeId);
        noticeService.deleteNotice(noticeId);
        return ApiResponse.success();
    }
}
