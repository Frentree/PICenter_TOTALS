package com.org.iopts.service.impl;

import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.exception.CustomException;
import com.org.iopts.exception.ErrorCode;
import com.org.iopts.repository.SearchMapper;
import com.org.iopts.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Search Service Implementation
 *
 * Handles search registration and result management business logic.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchServiceImpl implements SearchService {

    private final SearchMapper searchMapper;

    @Override
    public PageResponse<Map<String, Object>> getRegistrationList(int page, int size, String keyword) {
        log.debug("getRegistrationList - page: {}, size: {}, keyword: {}", page, size, keyword);

        Map<String, Object> params = new HashMap<>();
        params.put("offset", page * size);
        params.put("limit", size);
        if (keyword != null && !keyword.isEmpty()) {
            params.put("keyword", keyword);
        }

        List<Map<String, Object>> content = searchMapper.selectRegistrationList(params);
        long totalElements = searchMapper.countRegistrationList(params);

        return PageResponse.of(content, page, size, totalElements);
    }

    @Override
    @Transactional
    public void createRegistration(Map<String, Object> request, String userNo) {
        log.info("createRegistration - userNo: {}", userNo);

        // Map frontend field names to mapper field names
        if (request.containsKey("target") && !request.containsKey("targetId")) {
            request.put("targetId", request.get("target"));
        }
        if (request.containsKey("searchCondition") && !request.containsKey("startedAt")) {
            request.put("startedAt", request.get("searchCondition"));
        } else if (request.containsKey("searchName") && !request.containsKey("startedAt")) {
            request.put("startedAt", request.get("searchName"));
        }

        // Look up ap_no for the target
        if (!request.containsKey("apNo") || request.get("apNo") == null) {
            String targetId = (String) request.get("targetId");
            if (targetId != null) {
                String apNo = searchMapper.selectApNoByTargetId(targetId);
                request.put("apNo", apNo != null ? apNo : "1");
            } else {
                request.put("apNo", "1");
            }
        }

        request.put("userNo", userNo);
        int result = searchMapper.insertRegistration(request);
        if (result == 0) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to create search registration");
        }

        log.info("createRegistration success");
    }

    @Override
    @Transactional
    public void deleteRegistration(Long registId) {
        log.info("deleteRegistration - registId: {}", registId);

        int result = searchMapper.deleteRegistration(registId);
        if (result == 0) {
            throw new CustomException(ErrorCode.NOT_FOUND, "Search registration not found: " + registId);
        }

        log.info("deleteRegistration success - registId: {}", registId);
    }

    @Override
    public PageResponse<Map<String, Object>> getResultList(int page, int size, String keyword, String status, String startDate, String endDate) {
        log.debug("getResultList - page: {}, size: {}, keyword: {}, status: {}", page, size, keyword, status);

        Map<String, Object> params = new HashMap<>();
        params.put("offset", page * size);
        params.put("limit", size);
        if (keyword != null && !keyword.isEmpty()) {
            params.put("keyword", keyword);
        }
        if (status != null && !status.isEmpty()) {
            params.put("status", status);
        }
        if (startDate != null && !startDate.isEmpty()) {
            params.put("startDate", startDate);
        }
        if (endDate != null && !endDate.isEmpty()) {
            params.put("endDate", endDate);
        }

        List<Map<String, Object>> content = searchMapper.selectResultList(params);
        long totalElements = searchMapper.countResultList(params);

        return PageResponse.of(content, page, size, totalElements);
    }

    @Override
    public Map<String, Object> getResultDetail(Long resultId) {
        log.debug("getResultDetail - resultId: {}", resultId);

        Map<String, Object> result = searchMapper.selectResultDetail(resultId);
        if (result == null) {
            throw new CustomException(ErrorCode.NOT_FOUND, "Search result not found: " + resultId);
        }

        return result;
    }

    @Override
    public Map<String, Object> getSearchStatus() {
        log.debug("getSearchStatus");
        return searchMapper.selectSearchStatus();
    }

    @Override
    public List<Map<String, Object>> getDataTypeList(String keyword) {
        log.debug("getDataTypeList - keyword: {}", keyword);
        Map<String, Object> params = new HashMap<>();
        if (keyword != null && !keyword.isEmpty()) {
            params.put("keyword", keyword);
        }
        List<Map<String, Object>> result = searchMapper.selectDataTypeList(params);
        return result != null ? result : new ArrayList<>();
    }

    @Override
    @Transactional
    public void createDataType(Map<String, Object> request, String userNo) {
        log.info("createDataType - userNo: {}", userNo);
        request.put("createUser", userNo);
        if (!request.containsKey("apNo")) {
            request.put("apNo", 0);
        }
        // 필수 컬럼 기본값 설정
        if (!request.containsKey("datatype") || request.get("datatype") == null) {
            request.put("datatype", request.getOrDefault("datatypeLabel", ""));
        }
        // OCR, RECENT, CAPTURE 컬럼은 정수형 (0/1) → Y/N 문자열을 변환
        request.put("ocr", toIntFlag(request.getOrDefault("ocr", request.getOrDefault("ocrYn", "N"))));
        request.put("recent", toIntFlag(request.getOrDefault("recent", request.getOrDefault("recentYn", "N"))));
        request.put("capture", toIntFlag(request.getOrDefault("capture", "N")));
        // DATATYPE_ID, STD_ID 자동 생성
        if (!request.containsKey("datatypeId") || request.get("datatypeId") == null) {
            String maxId = searchMapper.selectMaxDatatypeId();
            int nextNum = 1;
            if (maxId != null && maxId.startsWith("DT_")) {
                try { nextNum = Integer.parseInt(maxId.substring(3)) + 1; } catch (NumberFormatException e) { nextNum = (int)(System.currentTimeMillis() % 100000); }
            }
            request.put("datatypeId", "DT_" + nextNum);
        }
        if (!request.containsKey("stdId") || request.get("stdId") == null) {
            String maxStd = searchMapper.selectMaxStdId();
            int nextStd = 1;
            if (maxStd != null) {
                try { nextStd = Integer.parseInt(maxStd) + 1; } catch (NumberFormatException e) { nextStd = (int)(System.currentTimeMillis() % 100000); }
            }
            request.put("stdId", String.valueOf(nextStd));
        }
        int result = searchMapper.insertDataType(request);
        if (result == 0) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "데이터 유형 생성에 실패했습니다.");
        }
    }

    @Override
    @Transactional
    public void updateDataType(Map<String, Object> request, String userNo) {
        log.info("updateDataType - stdId: {}, userNo: {}", request.get("stdId"), userNo);
        request.put("createUser", userNo);
        int result = searchMapper.updateDataType(request);
        if (result == 0) {
            throw new CustomException(ErrorCode.NOT_FOUND, "데이터 유형을 찾을 수 없습니다.");
        }
    }

    @Override
    @Transactional
    public void deleteDataType(String stdId) {
        log.info("deleteDataType - stdId: {}", stdId);
        // 정책에서 사용 중인지 확인
        int policyCount = searchMapper.countPolicyByDataType(stdId);
        if (policyCount > 0) {
            throw new CustomException(ErrorCode.INVALID_PARAMETER, "삭제 할 수 없는 정책 입니다. 사용 중인 스캔 정책이 있습니다.");
        }
        int result = searchMapper.deleteDataType(stdId);
        if (result == 0) {
            throw new CustomException(ErrorCode.NOT_FOUND, "데이터 유형을 찾을 수 없습니다.");
        }
    }

    /**
     * Y/N 또는 1/0 값을 정수(0/1)로 변환
     */
    private int toIntFlag(Object value) {
        if (value == null) return 0;
        String s = value.toString().trim();
        if ("Y".equalsIgnoreCase(s) || "1".equals(s) || "true".equalsIgnoreCase(s)) return 1;
        return 0;
    }
}
