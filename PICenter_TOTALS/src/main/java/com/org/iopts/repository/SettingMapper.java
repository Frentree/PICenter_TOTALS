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
     * Save (insert or update) a page setting
     */
    int savePageSettings(Map<String, Object> params);
}
