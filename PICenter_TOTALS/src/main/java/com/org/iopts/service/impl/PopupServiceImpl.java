package com.org.iopts.service.impl;

import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.repository.PopupMapper;
import com.org.iopts.service.PopupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Popup Service Implementation
 *
 * Handles popup data retrieval for selection dialogs.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PopupServiceImpl implements PopupService {

    private final PopupMapper popupMapper;

    @Override
    public PageResponse<Map<String, Object>> getUserListPopup(int page, int size, String keyword) {
        log.debug("getUserListPopup - page: {}, size: {}, keyword: {}", page, size, keyword);

        Map<String, Object> params = new HashMap<>();
        params.put("offset", page * size);
        params.put("limit", size);
        if (keyword != null && !keyword.isEmpty()) {
            params.put("keyword", keyword);
        }

        List<Map<String, Object>> content = popupMapper.selectUserListPopup(params);
        long totalElements = popupMapper.countUserListPopup(params);

        return PageResponse.of(content, page, size, totalElements);
    }

    @Override
    public List<Map<String, Object>> getGroupListPopup(String keyword) {
        log.debug("getGroupListPopup - keyword: {}", keyword);

        Map<String, Object> params = new HashMap<>();
        if (keyword != null && !keyword.isEmpty()) {
            params.put("keyword", keyword);
        }

        return popupMapper.selectGroupListPopup(params);
    }

    @Override
    public PageResponse<Map<String, Object>> getHostListPopup(int page, int size, String keyword) {
        log.debug("getHostListPopup - page: {}, size: {}, keyword: {}", page, size, keyword);

        Map<String, Object> params = new HashMap<>();
        params.put("offset", page * size);
        params.put("limit", size);
        if (keyword != null && !keyword.isEmpty()) {
            params.put("keyword", keyword);
        }

        List<Map<String, Object>> content = popupMapper.selectHostListPopup(params);
        long totalElements = popupMapper.countHostListPopup(params);

        return PageResponse.of(content, page, size, totalElements);
    }

    @Override
    public PageResponse<Map<String, Object>> getTargetListPopup(int page, int size, String groupId) {
        log.debug("getTargetListPopup - page: {}, size: {}, groupId: {}", page, size, groupId);

        Map<String, Object> params = new HashMap<>();
        params.put("offset", page * size);
        params.put("limit", size);
        if (groupId != null && !groupId.isEmpty()) {
            params.put("groupId", groupId);
        }

        List<Map<String, Object>> content = popupMapper.selectTargetListPopup(params);
        long totalElements = popupMapper.countTargetListPopup(params);

        return PageResponse.of(content, page, size, totalElements);
    }

    @Override
    public List<Map<String, Object>> getPolicyListPopup() {
        log.debug("getPolicyListPopup");
        return popupMapper.selectPolicyListPopup();
    }

    @Override
    public List<Map<String, Object>> getNetworkListPopup() {
        log.debug("getNetworkListPopup");
        return popupMapper.selectNetworkListPopup();
    }
}
