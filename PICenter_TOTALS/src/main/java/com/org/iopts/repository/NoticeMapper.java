package com.org.iopts.repository;

import com.org.iopts.dto.response.NoticeResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Notice MyBatis Mapper
 *
 * pi_notice table columns:
 *   notice_id (PK auto), notice_file_id (int), user_no, notice_con (varchar2000),
 *   notice_title (text), notice_chk (int1), regdate (datetime), notice_alert (int1 default 0)
 *
 * Namespace: com.org.iopts.repository.NoticeMapper
 */
@Mapper
public interface NoticeMapper {

    /**
     * Select paginated notice list with optional search
     */
    List<NoticeResponse> selectNoticeList(Map<String, Object> params);

    /**
     * Count total notices matching search filters (for pagination)
     */
    long selectNoticeListCount(Map<String, Object> params);

    /**
     * Select single notice detail by noticeId
     */
    NoticeResponse selectNoticeDetail(@Param("noticeId") Long noticeId);

    /**
     * Insert a new notice
     */
    int insertNotice(Map<String, Object> params);

    /**
     * Update an existing notice
     */
    int updateNotice(Map<String, Object> params);

    /**
     * Delete a notice by noticeId (physical delete)
     */
    int deleteNotice(@Param("noticeId") Long noticeId);

    /**
     * Update view count - no-op placeholder since pi_notice has no VIEW_COUNT column
     */
    int updateViewCount(@Param("noticeId") Long noticeId);
}
