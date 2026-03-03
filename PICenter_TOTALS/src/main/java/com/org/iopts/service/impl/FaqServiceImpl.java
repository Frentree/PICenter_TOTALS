package com.org.iopts.service.impl;

import com.org.iopts.dto.request.FaqRequest;
import com.org.iopts.dto.response.FaqResponse;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.exception.CustomException;
import com.org.iopts.exception.ErrorCode;
import com.org.iopts.repository.FaqMapper;
import com.org.iopts.service.FaqService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FAQ Service Implementation
 *
 * Handles all FAQ business logic including CRUD operations.
 *
 * pi_faq table columns:
 *   FAQ_ID (PK auto_increment), USER_NO, FAQ_TITLE, FAQ_CON, REGDATE
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FaqServiceImpl implements FaqService {

    private final FaqMapper faqMapper;

    /**
     * Get paginated FAQ list with optional search keyword
     * (no CATEGORY column in pi_faq; the category parameter is repurposed as searchKeyword)
     */
    @Override
    public PageResponse<FaqResponse> getFaqList(int page, int size, String category) {
        log.debug("getFaqList - page: {}, size: {}, searchKeyword: {}", page, size, category);

        Map<String, Object> params = new HashMap<>();
        params.put("offset", page * size);
        params.put("limit", size);

        // Repurpose category param as searchKeyword since pi_faq has no CATEGORY column
        if (category != null && !category.isEmpty()) {
            params.put("searchKeyword", category);
        }

        List<FaqResponse> content = faqMapper.selectFaqList(params);
        long totalElements = faqMapper.selectFaqListCount(params);

        return PageResponse.of(content, page, size, totalElements);
    }

    /**
     * Get FAQ detail by ID
     */
    @Override
    public FaqResponse getFaqDetail(Long faqId) {
        log.debug("getFaqDetail - faqId: {}", faqId);
        FaqResponse faq = faqMapper.selectFaqDetail(faqId);
        if (faq == null) {
            throw new CustomException(ErrorCode.FAQ_NOT_FOUND);
        }
        return faq;
    }

    /**
     * Create a new FAQ
     */
    @Override
    @Transactional
    public void createFaq(FaqRequest request, String userNo) {
        log.info("createFaq - question: {}, by userNo: {}", request.getQuestion(), userNo);

        Map<String, Object> params = new HashMap<>();
        params.put("userNo", userNo);
        params.put("question", request.getQuestion());
        params.put("answer", request.getAnswer());

        int result = faqMapper.insertFaq(params);
        if (result == 0) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to create FAQ");
        }

        log.info("createFaq success - question: {}", request.getQuestion());
    }

    /**
     * Update an existing FAQ
     */
    @Override
    @Transactional
    public void updateFaq(Long faqId, FaqRequest request, String userNo) {
        log.info("updateFaq - faqId: {}, by userNo: {}", faqId, userNo);

        Map<String, Object> params = new HashMap<>();
        params.put("faqId", faqId);
        params.put("question", request.getQuestion());
        params.put("answer", request.getAnswer());

        int result = faqMapper.updateFaq(params);
        if (result == 0) {
            throw new CustomException(ErrorCode.FAQ_NOT_FOUND, "Failed to update FAQ");
        }

        log.info("updateFaq success - faqId: {}", faqId);
    }

    /**
     * Delete a FAQ (physical delete since pi_faq has no USE_YN column)
     */
    @Override
    @Transactional
    public void deleteFaq(Long faqId) {
        log.info("deleteFaq - faqId: {}", faqId);

        int result = faqMapper.deleteFaq(faqId);
        if (result == 0) {
            throw new CustomException(ErrorCode.FAQ_NOT_FOUND, "Failed to delete FAQ");
        }

        log.info("deleteFaq success - faqId: {}", faqId);
    }
}
