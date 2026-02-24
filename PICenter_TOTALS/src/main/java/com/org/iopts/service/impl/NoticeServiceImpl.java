package com.org.iopts.service.impl;

import com.org.iopts.dto.request.NoticeRequest;
import com.org.iopts.dto.response.NoticeResponse;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.exception.CustomException;
import com.org.iopts.exception.ErrorCode;
import com.org.iopts.repository.NoticeMapper;
import com.org.iopts.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Notice Service Implementation
 *
 * Handles all notice business logic including CRUD operations.
 *
 * pi_notice table columns:
 *   NOTICE_ID (PK auto_increment), USER_NO, NOTICE_TITLE, NOTICE_CON,
 *   NOTICE_CHK, NOTICE_ALERT, NOTICE_FILE_ID, REGDATE
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeServiceImpl implements NoticeService {

    private final NoticeMapper noticeMapper;

    /**
     * Get paginated notice list with optional keyword search
     */
    @Override
    public PageResponse<NoticeResponse> getNoticeList(int page, int size, String searchKeyword) {
        log.debug("getNoticeList - page: {}, size: {}, searchKeyword: {}", page, size, searchKeyword);

        Map<String, Object> params = new HashMap<>();
        params.put("offset", page * size);
        params.put("limit", size);

        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            params.put("searchKeyword", searchKeyword);
        }

        List<NoticeResponse> content = noticeMapper.selectNoticeList(params);
        long totalElements = noticeMapper.selectNoticeListCount(params);

        return PageResponse.of(content, page, size, totalElements);
    }

    /**
     * Get notice detail by noticeId (also increments view count if available)
     */
    @Override
    @Transactional
    public NoticeResponse getNoticeDetail(Long noticeId) {
        log.debug("getNoticeDetail - noticeId: {}", noticeId);

        NoticeResponse notice = noticeMapper.selectNoticeDetail(noticeId);
        if (notice == null) {
            throw new CustomException(ErrorCode.NOTICE_NOT_FOUND);
        }

        // updateViewCount is a no-op since pi_notice has no VIEW_COUNT column
        noticeMapper.updateViewCount(noticeId);

        return notice;
    }

    /**
     * Create a new notice
     */
    @Override
    @Transactional
    public void createNotice(NoticeRequest request, String userNo) {
        log.info("createNotice - title: {}, by userNo: {}", request.getTitle(), userNo);

        Map<String, Object> params = new HashMap<>();
        params.put("userNo", userNo);
        params.put("title", request.getTitle());
        params.put("content", request.getContent());
        params.put("noticeChk", request.getNoticeChk());
        params.put("noticeAlert", request.getNoticeAlert());
        params.put("noticeFileId", request.getNoticeFileId());

        int result = noticeMapper.insertNotice(params);
        if (result == 0) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to create notice");
        }

        log.info("createNotice success - title: {}", request.getTitle());
    }

    /**
     * Update an existing notice
     */
    @Override
    @Transactional
    public void updateNotice(Long noticeId, NoticeRequest request, String userNo) {
        log.info("updateNotice - noticeId: {}, by userNo: {}", noticeId, userNo);

        // Verify notice exists
        NoticeResponse existing = noticeMapper.selectNoticeDetail(noticeId);
        if (existing == null) {
            throw new CustomException(ErrorCode.NOTICE_NOT_FOUND);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("noticeId", noticeId);
        params.put("title", request.getTitle());
        params.put("content", request.getContent());
        params.put("noticeChk", request.getNoticeChk());
        params.put("noticeAlert", request.getNoticeAlert());
        params.put("noticeFileId", request.getNoticeFileId());

        int result = noticeMapper.updateNotice(params);
        if (result == 0) {
            throw new CustomException(ErrorCode.NOTICE_NOT_FOUND, "Failed to update notice");
        }

        log.info("updateNotice success - noticeId: {}", noticeId);
    }

    /**
     * Delete a notice (physical delete since pi_notice has no USE_YN column)
     */
    @Override
    @Transactional
    public void deleteNotice(Long noticeId) {
        log.info("deleteNotice - noticeId: {}", noticeId);

        // Verify notice exists
        NoticeResponse existing = noticeMapper.selectNoticeDetail(noticeId);
        if (existing == null) {
            throw new CustomException(ErrorCode.NOTICE_NOT_FOUND);
        }

        int result = noticeMapper.deleteNotice(noticeId);
        if (result == 0) {
            throw new CustomException(ErrorCode.NOTICE_NOT_FOUND, "Failed to delete notice");
        }

        log.info("deleteNotice success - noticeId: {}", noticeId);
    }
}
