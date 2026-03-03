package com.org.iopts.service.impl;

import com.org.iopts.exception.CustomException;
import com.org.iopts.exception.ErrorCode;
import com.org.iopts.repository.SettingMapper;
import com.org.iopts.service.SettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Setting Service Implementation
 *
 * Handles page settings retrieval and persistence.
 *
 * pi_page_settings table columns:
 *   PAGE_ID (PK auto_increment), PAGE_NAME, PAGE_URL, GRADE_NO,
 *   USE_YN, SORT_ORDER, REG_DT
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettingServiceImpl implements SettingService {

    private final SettingMapper settingMapper;

    /**
     * Get all active page settings
     */
    @Override
    public List<Map<String, Object>> getPageSettings() {
        log.debug("getPageSettings");
        return settingMapper.selectPageSettings();
    }

    /**
     * Save (create or update) page settings.
     * Iterates over the list and upserts each setting individually.
     */
    @Override
    @Transactional
    public void savePageSettings(List<Map<String, Object>> settings) {
        log.info("savePageSettings - count: {}", settings != null ? settings.size() : 0);

        if (settings == null || settings.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_PARAMETER, "Settings list is empty");
        }

        for (Map<String, Object> setting : settings) {
            // Map alternative field names to mapper-expected names
            if (setting.containsKey("name") && !setting.containsKey("pageName")) {
                setting.put("pageName", setting.get("name"));
            }
            if (setting.containsKey("detailName") && !setting.containsKey("pageUrl")) {
                setting.put("pageUrl", setting.get("detailName"));
            }
            if (setting.containsKey("type") && !setting.containsKey("gradeNo")) {
                setting.put("gradeNo", setting.get("type"));
            }
            if (setting.containsKey("status") && !setting.containsKey("useYn")) {
                setting.put("useYn", setting.get("status"));
            }

            // pi_setting 테이블에 PK 없음 → idx 기반 존재 여부 확인 후 INSERT/UPDATE 분기
            int result;
            Object pageId = setting.get("pageId");
            if (pageId != null && !"".equals(pageId.toString())) {
                int exists = settingMapper.countSettingByIdx(setting);
                if (exists > 0) {
                    result = settingMapper.updatePageSetting(setting);
                } else {
                    result = settingMapper.insertPageSetting(setting);
                }
            } else {
                result = settingMapper.insertPageSetting(setting);
            }
            if (result == 0) {
                throw new CustomException(ErrorCode.SETTINGS_SAVE_FAILED,
                        "Failed to save setting: " + setting.get("pageName"));
            }
        }

        log.info("savePageSettings success - count: {}", settings.size());
    }

    /**
     * Get all registered nodes (agents)
     */
    @Override
    public List<Map<String, Object>> getNodeList() {
        log.debug("getNodeList");
        return settingMapper.selectNodeList();
    }

    /**
     * Save (create or update) a node
     */
    @Override
    @Transactional
    public void saveNode(Map<String, Object> request) {
        // Map frontend field names to mapper field names
        if (request.containsKey("nodeName") && !request.containsKey("agentName")) {
            request.put("agentName", request.get("nodeName"));
        }
        if (request.containsKey("ipAddress") && !request.containsKey("connectedIp")) {
            request.put("connectedIp", request.get("ipAddress"));
        }
        if (request.containsKey("port") && !request.containsKey("apNo")) {
            request.put("apNo", request.get("port"));
        }
        log.info("saveNode - agentName: {}", request.get("agentName"));

        int result = settingMapper.saveNode(request);
        if (result == 0) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to save node");
        }

        log.info("saveNode success");
    }

    /**
     * Delete a node
     */
    @Override
    @Transactional
    public void deleteNode(String nodeId) {
        log.info("deleteNode - nodeId: {}", nodeId);

        int result = settingMapper.deleteNode(nodeId);
        if (result == 0) {
            throw new CustomException(ErrorCode.NOT_FOUND, "Node not found: " + nodeId);
        }

        log.info("deleteNode success - nodeId: {}", nodeId);
    }

    /**
     * Get interlock/integration settings
     */
    @Override
    public List<Map<String, Object>> getInterlockSettings() {
        log.debug("getInterlockSettings");
        return settingMapper.selectInterlockSettings();
    }

    /**
     * Save interlock/integration settings
     */
    @Override
    @Transactional
    public void saveInterlockSettings(List<Map<String, Object>> settings) {
        log.info("saveInterlockSettings - count: {}", settings != null ? settings.size() : 0);

        if (settings == null || settings.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_PARAMETER, "Interlock settings list is empty");
        }

        for (Map<String, Object> setting : settings) {
            int result;
            Object interlockId = setting.get("interlockId");
            if (interlockId != null && !"".equals(interlockId.toString())) {
                Map<String, Object> checkParam = new java.util.HashMap<>();
                checkParam.put("pageId", interlockId);
                int exists = settingMapper.countSettingByIdx(checkParam);
                if (exists > 0) {
                    result = settingMapper.updateInterlockSetting(setting);
                } else {
                    result = settingMapper.insertInterlockSetting(setting);
                }
            } else {
                result = settingMapper.insertInterlockSetting(setting);
            }
            if (result == 0) {
                throw new CustomException(ErrorCode.SETTINGS_SAVE_FAILED,
                        "Failed to save interlock setting: " + setting.get("name"));
            }
        }

        log.info("saveInterlockSettings success - count: {}", settings.size());
    }

    /**
     * Save a single interlock setting
     */
    @Override
    @Transactional
    public void saveInterlockSetting(Map<String, Object> request) {
        // Map frontend field names to mapper field names
        if (request.containsKey("interlockName") && !request.containsKey("settingName")) {
            request.put("settingName", request.get("interlockName"));
        }
        if (request.containsKey("interlockUrl") && !request.containsKey("settingValue")) {
            request.put("settingValue", request.get("interlockUrl"));
        }
        if (!request.containsKey("useYn")) {
            request.put("useYn", "Y");
        }
        log.info("saveInterlockSetting");

        // pi_setting 테이블에 PK 없음 → idx 기반 존재 여부 확인 후 INSERT/UPDATE 분기
        int result;
        Object interlockId = request.get("interlockId");
        if (interlockId != null && !"".equals(interlockId.toString())) {
            Map<String, Object> checkParam = new java.util.HashMap<>();
            checkParam.put("pageId", interlockId);
            int exists = settingMapper.countSettingByIdx(checkParam);
            if (exists > 0) {
                result = settingMapper.updateInterlockSetting(request);
            } else {
                result = settingMapper.insertInterlockSetting(request);
            }
        } else {
            result = settingMapper.insertInterlockSetting(request);
        }
        if (result == 0) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to save interlock setting");
        }
    }

    /**
     * Delete an interlock setting
     */
    @Override
    @Transactional
    public void deleteInterlockSetting(String interlockId) {
        log.info("deleteInterlockSetting - id: {}", interlockId);
        int result = settingMapper.deleteInterlockSetting(interlockId);
        if (result == 0) {
            throw new CustomException(ErrorCode.NOT_FOUND, "Interlock setting not found: " + interlockId);
        }
    }
}
