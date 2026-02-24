package com.org.iopts.repository;

import com.org.iopts.dto.response.DownloadResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Download MyBatis Mapper
 *
 * pi_download table columns:
 *   download_id (PK auto), download_file_id (int), user_no, download_con (varchar2000),
 *   download_title (text), regdate (datetime)
 *
 * Namespace: com.org.iopts.repository.DownloadMapper
 */
@Mapper
public interface DownloadMapper {

    /**
     * Select paginated download list
     */
    List<DownloadResponse> selectDownloadList(Map<String, Object> params);

    /**
     * Count total downloads (for pagination)
     */
    long selectDownloadListCount(Map<String, Object> params);

    /**
     * Select single download detail by downloadId
     */
    DownloadResponse selectDownloadDetail(@Param("downloadId") Long downloadId);

    /**
     * Insert a new download record
     */
    int insertDownload(Map<String, Object> params);

    /**
     * Delete a download record by downloadId (physical delete)
     */
    int deleteDownload(@Param("downloadId") Long downloadId);

    /**
     * Update download count - no-op placeholder since pi_download has no DOWNLOAD_COUNT column
     */
    int updateDownloadCount(@Param("downloadId") Long downloadId);
}
