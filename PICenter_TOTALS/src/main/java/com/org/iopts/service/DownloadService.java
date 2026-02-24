package com.org.iopts.service;

import com.org.iopts.dto.response.DownloadResponse;
import com.org.iopts.dto.response.PageResponse;

/**
 * Download Service Interface
 *
 * Provides operations for download CRUD and file retrieval.
 *
 * pi_download table columns:
 *   DOWNLOAD_ID (PK auto_increment), USER_NO, DOWNLOAD_TITLE, DOWNLOAD_CON,
 *   DOWNLOAD_FILE_ID, REGDATE
 *
 * Legacy mappings:
 *   - getDownloadList -> /downloadList
 *   - createDownload  -> /downloadInsert
 *   - getDownloadFile -> /fileDown
 *   - deleteDownload  -> /downloadDelete
 */
public interface DownloadService {

    /**
     * Get paginated download list
     *
     * @param page page number (0-based)
     * @param size page size
     * @return paginated download response
     */
    PageResponse<DownloadResponse> getDownloadList(int page, int size);

    /**
     * Create a new download record
     *
     * @param title          download title
     * @param content        download description/content
     * @param downloadFileId file ID reference
     * @param userNo         user who created the record
     */
    void createDownload(String title, String content, String downloadFileId, String userNo);

    /**
     * Get download detail by downloadId
     *
     * @param downloadId download primary key
     * @return download detail
     */
    DownloadResponse getDownloadDetail(Long downloadId);

    /**
     * Delete a download record (physical delete)
     *
     * @param downloadId download primary key
     */
    void deleteDownload(Long downloadId);
}
