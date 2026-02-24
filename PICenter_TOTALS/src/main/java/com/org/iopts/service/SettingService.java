package com.org.iopts.service;

import java.util.List;
import java.util.Map;

/**
 * Setting Service Interface
 *
 * Provides operations for page settings retrieval and persistence.
 *
 * pi_page_settings table columns:
 *   PAGE_ID (PK auto_increment), PAGE_NAME, PAGE_URL, GRADE_NO,
 *   USE_YN, SORT_ORDER, REG_DT
 *
 * Legacy mappings:
 *   - getPageSettings  -> /getPageSettings
 *   - savePageSettings -> /savePageSettings
 */
public interface SettingService {

    /**
     * Get all active page settings (USE_YN = 'Y')
     *
     * @return list of page setting maps (pageId, pageName, pageUrl, gradeNo, useYn, sortOrder, regDt)
     */
    List<Map<String, Object>> getPageSettings();

    /**
     * Save (create or update) page settings
     *
     * @param settings list of setting maps (pageName, pageUrl, gradeNo, useYn, sortOrder)
     */
    void savePageSettings(List<Map<String, Object>> settings);
}
