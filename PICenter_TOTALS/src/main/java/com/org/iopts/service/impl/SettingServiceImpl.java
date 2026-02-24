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
            int result = settingMapper.savePageSettings(setting);
            if (result == 0) {
                throw new CustomException(ErrorCode.SETTINGS_SAVE_FAILED,
                        "Failed to save setting: " + setting.get("pageName"));
            }
        }

        log.info("savePageSettings success - count: {}", settings.size());
    }
}
