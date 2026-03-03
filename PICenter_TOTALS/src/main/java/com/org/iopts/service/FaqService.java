package com.org.iopts.service;

import com.org.iopts.dto.request.FaqRequest;
import com.org.iopts.dto.response.FaqResponse;
import com.org.iopts.dto.response.PageResponse;

/**
 * FAQ Service Interface
 *
 * Provides operations for FAQ CRUD.
 *
 * Legacy mappings:
 *   - getFaqList  -> /faqList
 *   - createFaq   -> /faqInsert
 *   - updateFaq   -> /faqUpdate
 *   - deleteFaq   -> /faqDelete
 */
public interface FaqService {

    /**
     * Get paginated FAQ list with optional category filter
     *
     * @param page     page number (0-based)
     * @param size     page size
     * @param category optional category filter
     * @return paginated FAQ response
     */
    PageResponse<FaqResponse> getFaqList(int page, int size, String category);

    /**
     * Get FAQ detail by ID
     *
     * @param faqId FAQ primary key
     * @return FAQ response
     */
    FaqResponse getFaqDetail(Long faqId);

    /**
     * Create a new FAQ
     *
     * @param request FAQ data
     * @param userNo  user who is creating the FAQ
     */
    void createFaq(FaqRequest request, String userNo);

    /**
     * Update an existing FAQ
     *
     * @param faqId   FAQ primary key
     * @param request FAQ data
     * @param userNo  user who is updating the FAQ
     */
    void updateFaq(Long faqId, FaqRequest request, String userNo);

    /**
     * Delete a FAQ (logical delete)
     *
     * @param faqId FAQ primary key
     */
    void deleteFaq(Long faqId);
}
