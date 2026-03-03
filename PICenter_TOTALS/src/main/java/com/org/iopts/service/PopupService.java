package com.org.iopts.service;

import com.org.iopts.dto.response.PageResponse;

import java.util.List;
import java.util.Map;

/**
 * Popup Service Interface
 *
 * Provides operations for popup data retrieval used in selection dialogs.
 *
 * Legacy mappings:
 *   - getUserListPopup   -> /popup/userList
 *   - getGroupListPopup  -> /popup/groupList
 *   - getHostListPopup   -> /popup/hostList
 *   - getTargetListPopup -> /popup/targetList
 *   - getPolicyListPopup -> /popup/policyList
 *   - getNetworkListPopup -> /popup/netList
 */
public interface PopupService {

    /**
     * Get paginated user list for popup selection
     *
     * @param page    page number (0-based)
     * @param size    page size
     * @param keyword optional search keyword
     * @return paginated user list
     */
    PageResponse<Map<String, Object>> getUserListPopup(int page, int size, String keyword);

    /**
     * Get group list for popup selection
     *
     * @param keyword optional search keyword
     * @return group list
     */
    List<Map<String, Object>> getGroupListPopup(String keyword);

    /**
     * Get paginated host list for popup selection
     *
     * @param page    page number (0-based)
     * @param size    page size
     * @param keyword optional search keyword
     * @return paginated host list
     */
    PageResponse<Map<String, Object>> getHostListPopup(int page, int size, String keyword);

    /**
     * Get paginated target list for popup selection
     *
     * @param page    page number (0-based)
     * @param size    page size
     * @param groupId optional group ID filter
     * @return paginated target list
     */
    PageResponse<Map<String, Object>> getTargetListPopup(int page, int size, String groupId);

    /**
     * Get scan policy list for popup selection
     *
     * @return policy list
     */
    List<Map<String, Object>> getPolicyListPopup();

    /**
     * Get network list for popup selection
     *
     * @return network list
     */
    List<Map<String, Object>> getNetworkListPopup();
}
