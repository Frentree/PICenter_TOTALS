package com.org.iopts.repository;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Popup MyBatis Mapper
 *
 * Provides SQL operations for popup data retrieval.
 * Namespace: com.org.iopts.repository.PopupMapper
 */
@Mapper
public interface PopupMapper {

    /**
     * Select paginated user list for popup
     */
    List<Map<String, Object>> selectUserListPopup(Map<String, Object> params);

    /**
     * Count total users for popup pagination
     */
    long countUserListPopup(Map<String, Object> params);

    /**
     * Select group list for popup
     */
    List<Map<String, Object>> selectGroupListPopup(Map<String, Object> params);

    /**
     * Select paginated host list for popup
     */
    List<Map<String, Object>> selectHostListPopup(Map<String, Object> params);

    /**
     * Count total hosts for popup pagination
     */
    long countHostListPopup(Map<String, Object> params);

    /**
     * Select paginated target list for popup
     */
    List<Map<String, Object>> selectTargetListPopup(Map<String, Object> params);

    /**
     * Count total targets for popup pagination
     */
    long countTargetListPopup(Map<String, Object> params);

    /**
     * Select scan policy list for popup
     */
    List<Map<String, Object>> selectPolicyListPopup();

    /**
     * Select network list for popup
     */
    List<Map<String, Object>> selectNetworkListPopup();
}
