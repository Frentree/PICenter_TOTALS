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

    /**
     * Get all registered nodes
     *
     * @return list of node maps
     */
    List<Map<String, Object>> getNodeList();

    /**
     * Save (create or update) a node
     *
     * @param request node data
     */
    void saveNode(Map<String, Object> request);

    /**
     * Delete a node
     *
     * @param nodeId node identifier
     */
    void deleteNode(String nodeId);

    /**
     * Get interlock/integration settings
     *
     * @return list of interlock setting maps
     */
    List<Map<String, Object>> getInterlockSettings();

    /**
     * Save interlock/integration settings (bulk)
     *
     * @param settings list of interlock setting maps
     */
    void saveInterlockSettings(List<Map<String, Object>> settings);

    /**
     * Save a single interlock setting
     *
     * @param request interlock setting data
     */
    void saveInterlockSetting(Map<String, Object> request);

    /**
     * Delete an interlock setting
     *
     * @param interlockId interlock identifier
     */
    void deleteInterlockSetting(String interlockId);
}
