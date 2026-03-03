package com.org.iopts.controller;

import com.org.iopts.dto.response.ApiResponse;
import com.org.iopts.dto.response.DownloadResponse;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.service.DownloadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Download Controller
 *
 * Provides REST API endpoints for download management.
 *
 * pi_download table columns:
 *   DOWNLOAD_ID (PK auto_increment), USER_NO, DOWNLOAD_TITLE, DOWNLOAD_CON,
 *   DOWNLOAD_FILE_ID, REGDATE
 *
 * Legacy endpoint mappings:
 *   GET    /api/v1/downloads                    <- /downloadList
 *   POST   /api/v1/downloads                    <- /downloadInsert
 *   GET    /api/v1/downloads/{downloadId}       <- /downloadDetail
 *   DELETE /api/v1/downloads/{downloadId}       <- /downloadDelete
 */
@Slf4j
@Tag(name = "Download", description = "Download API")
@RestController
@RequestMapping("/api/v1/downloads")
@RequiredArgsConstructor
public class DownloadController {

    private final DownloadService downloadService;

    /**
     * Get paginated download list
     *
     * Legacy: GET /downloadList
     * New: GET /api/v1/downloads
     */
    @Operation(summary = "Get download list", description = "Get paginated download list")
    @GetMapping
    public ApiResponse<PageResponse<DownloadResponse>> getDownloadList(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size) {

        log.debug("GET /api/v1/downloads - page: {}, size: {}", page, size);
        PageResponse<DownloadResponse> result = downloadService.getDownloadList(page, size);
        return ApiResponse.success(result);
    }

    /**
     * Create a new download record
     *
     * Legacy: POST /downloadInsert
     * New: POST /api/v1/downloads
     */
    @Operation(summary = "Create download", description = "Create a new download record")
    @PostMapping
    public ApiResponse<Void> createDownload(
            @RequestBody Map<String, String> request,
            Authentication authentication) {

        String userNo = (String) authentication.getPrincipal();
        String title = request.get("title");
        String content = request.get("content");
        String downloadFileId = request.get("downloadFileId");
        log.info("POST /api/v1/downloads - title: {}, by: {}", title, userNo);

        downloadService.createDownload(title, content, downloadFileId, userNo);
        return ApiResponse.success();
    }

    /**
     * Get download detail
     *
     * New: GET /api/v1/downloads/{downloadId}
     */
    @Operation(summary = "Get download detail", description = "Get download detail by download ID")
    @GetMapping("/{downloadId}")
    public ApiResponse<DownloadResponse> getDownloadDetail(
            @Parameter(description = "Download ID", example = "1") @PathVariable Long downloadId) {

        log.debug("GET /api/v1/downloads/{}", downloadId);
        DownloadResponse result = downloadService.getDownloadDetail(downloadId);
        return ApiResponse.success(result);
    }

    /**
     * Delete a download
     *
     * Legacy: POST /downloadDelete
     * New: DELETE /api/v1/downloads/{downloadId}
     */
    @Operation(summary = "Delete download", description = "Delete a download record")
    @DeleteMapping("/{downloadId}")
    public ApiResponse<Void> deleteDownload(
            @Parameter(description = "Download ID", example = "1") @PathVariable Long downloadId) {

        log.info("DELETE /api/v1/downloads/{}", downloadId);
        downloadService.deleteDownload(downloadId);
        return ApiResponse.success();
    }
}
