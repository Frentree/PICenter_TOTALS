package com.org.iopts.repository;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Setting MyBatis Mapper
 *
 * Namespace: com.org.iopts.repository.SettingMapper
 */
@Mapper
public interface SettingMapper {

    /**
     * Select all page settings
     */
    List<Map<String, Object>> selectPageSettings();

    /**
     * Check if setting exists by idx
     */
    int countSettingByIdx(Map<String, Object> params);

    /**
     * Insert a new page setting
     */
    int insertPageSetting(Map<String, Object> params);

    /**
     * Update existing page setting by idx
     */
    int updatePageSetting(Map<String, Object> params);

    /**
     * Select all registered nodes
     */
    List<Map<String, Object>> selectNodeList();

    /**
     * Save (insert or update) a node
     */
    int saveNode(Map<String, Object> params);

    /**
     * Delete a node by agent name
     */
    int deleteNode(@org.apache.ibatis.annotations.Param("nodeId") String nodeId);

    /**
     * Select interlock/integration settings
     */
    List<Map<String, Object>> selectInterlockSettings();

    /**
     * Insert a new interlock setting
     */
    int insertInterlockSetting(Map<String, Object> params);

    /**
     * Update existing interlock setting by idx
     */
    int updateInterlockSetting(Map<String, Object> params);

    /**
     * Delete an interlock setting
     */
    int deleteInterlockSetting(@org.apache.ibatis.annotations.Param("interlockId") String interlockId);
}
