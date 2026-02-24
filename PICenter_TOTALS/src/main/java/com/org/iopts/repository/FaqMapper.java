package com.org.iopts.repository;

import com.org.iopts.dto.response.FaqResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * FAQ MyBatis Mapper
 *
 * pi_faq table columns:
 *   faq_no (PK auto), faq_title, faq_content, faq_create_dt, faq_status, faq_user_sosok, user_no
 *
 * Namespace: com.org.iopts.repository.FaqMapper
 */
@Mapper
public interface FaqMapper {

    /**
     * Select paginated FAQ list with optional search keyword
     */
    List<FaqResponse> selectFaqList(Map<String, Object> params);

    /**
     * Count total FAQs matching filters (for pagination)
     */
    long selectFaqListCount(Map<String, Object> params);

    /**
     * Insert a new FAQ
     */
    int insertFaq(Map<String, Object> params);

    /**
     * Update an existing FAQ
     */
    int updateFaq(Map<String, Object> params);

    /**
     * Delete a FAQ by faqId (physical delete)
     */
    int deleteFaq(@Param("faqId") Long faqId);
}
