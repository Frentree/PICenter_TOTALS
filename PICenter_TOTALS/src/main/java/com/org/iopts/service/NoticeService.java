package com.org.iopts.service;

import com.org.iopts.dto.request.NoticeRequest;
import com.org.iopts.dto.response.NoticeResponse;
import com.org.iopts.dto.response.PageResponse;

/**
 * Notice Service Interface
 *
 * Provides operations for notice CRUD and view-count increment.
 *
 * Legacy mappings:
 *   - getNoticeList   -> /noticeList
 *   - getNoticeDetail -> /noticeDetail
 *   - createNotice    -> /noticeInsert
 *   - updateNotice    -> /noticeUpdate
 *   - deleteNotice    -> /noticeDelete
 */
public interface NoticeService {

    /**
     * Get paginated notice list with optional keyword search
     *
     * @param page          page number (0-based)
     * @param size          page size
     * @param searchKeyword search keyword (searches title and content)
     * @return paginated notice response
     */
    PageResponse<NoticeResponse> getNoticeList(int page, int size, String searchKeyword);

    /**
     * Get notice detail by noticeId (also increments view count)
     *
     * @param noticeId notice primary key
     * @return notice detail
     */
    NoticeResponse getNoticeDetail(Long noticeId);

    /**
     * Create a new notice
     *
     * @param request notice data
     * @param userNo  user who is creating the notice
     */
    void createNotice(NoticeRequest request, String userNo);

    /**
     * Update an existing notice
     *
     * @param noticeId notice primary key
     * @param request  notice data
     * @param userNo   user who is updating the notice
     */
    void updateNotice(Long noticeId, NoticeRequest request, String userNo);

    /**
     * Delete a notice (logical delete)
     *
     * @param noticeId notice primary key
     */
    void deleteNotice(Long noticeId);
}
