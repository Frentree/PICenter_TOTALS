package com.org.iopts.service.impl;

import com.org.iopts.dto.response.DownloadResponse;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.exception.CustomException;
import com.org.iopts.exception.ErrorCode;
import com.org.iopts.repository.DownloadMapper;
import com.org.iopts.service.DownloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Download Service Implementation
 *
 * Handles download management including listing, creation, and deletion.
 *
 * pi_download table columns:
 *   DOWNLOAD_ID (PK auto_increment), USER_NO, DOWNLOAD_TITLE, DOWNLOAD_CON,
 *   DOWNLOAD_FILE_ID, REGDATE
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DownloadServiceImpl implements DownloadService {

    private final DownloadMapper downloadMapper;

    /**
     * Get paginated download list
     */
    @Override
    public PageResponse<DownloadResponse> getDownloadList(int page, int size) {
        log.debug("getDownloadList - page: {}, size: {}", page, size);

        Map<String, Object> params = new HashMap<>();
        params.put("offset", page * size);
        params.put("limit", size);

        List<DownloadResponse> content = downloadMapper.selectDownloadList(params);
        long totalElements = downloadMapper.selectDownloadListCount(params);

        return PageResponse.of(content, page, size, totalElements);
    }

    /**
     * Create a new download record
     */
    @Override
    @Transactional
    public void createDownload(String title, String content, String downloadFileId, String userNo) {
        log.info("createDownload - title: {}, by userNo: {}", title, userNo);

        Map<String, Object> params = new HashMap<>();
        params.put("userNo", userNo);
        params.put("title", title);
        params.put("content", content);
        // download_file_id is int column - convert empty string to null
        params.put("downloadFileId", (downloadFileId != null && !downloadFileId.isEmpty()) ? Integer.parseInt(downloadFileId) : null);

        int result = downloadMapper.insertDownload(params);
        if (result == 0) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to create download record");
        }

        log.info("createDownload success - title: {}", title);
    }

    /**
     * Get download detail by downloadId
     */
    @Override
    public DownloadResponse getDownloadDetail(Long downloadId) {
        log.debug("getDownloadDetail - downloadId: {}", downloadId);

        DownloadResponse download = downloadMapper.selectDownloadDetail(downloadId);
        if (download == null) {
            throw new CustomException(ErrorCode.DOWNLOAD_NOT_FOUND);
        }

        return download;
    }

    /**
     * Delete a download record (physical delete since pi_download has no USE_YN column)
     */
    @Override
    @Transactional
    public void deleteDownload(Long downloadId) {
        log.info("deleteDownload - downloadId: {}", downloadId);

        DownloadResponse existing = downloadMapper.selectDownloadDetail(downloadId);
        if (existing == null) {
            throw new CustomException(ErrorCode.DOWNLOAD_NOT_FOUND);
        }

        int result = downloadMapper.deleteDownload(downloadId);
        if (result == 0) {
            throw new CustomException(ErrorCode.DOWNLOAD_NOT_FOUND, "Failed to delete download");
        }

        log.info("deleteDownload success - downloadId: {}", downloadId);
    }
}
