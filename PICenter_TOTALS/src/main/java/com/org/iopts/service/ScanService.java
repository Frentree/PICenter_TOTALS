package com.org.iopts.service;

import com.org.iopts.dto.request.PolicyRequest;
import com.org.iopts.dto.request.ScheduleCreateRequest;
import com.org.iopts.dto.response.LocationResponse;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.dto.response.ScheduleResponse;

import java.util.List;
import java.util.Map;

/**
 * Scan Service Interface
 */
public interface ScanService {

    /**
     * Get paginated schedule list
     */
    PageResponse<ScheduleResponse> getScheduleList(int page, int size);

    /**
     * Get schedule detail by ID
     */
    ScheduleResponse getScheduleDetail(String scheduleId);

    /**
     * Create a new schedule
     */
    void createSchedule(ScheduleCreateRequest request, String regUserNo);

    /**
     * Update an existing schedule
     */
    void updateSchedule(String scheduleId, ScheduleCreateRequest request, String modUserNo);

    /**
     * Delete a schedule
     */
    void deleteSchedule(String scheduleId);

    /**
     * Change schedule status (enable/disable)
     */
    void changeScheduleStatus(String scheduleId, Character statusYn);

    /**
     * Execute a schedule immediately
     */
    void executeSchedule(String scheduleId, String userNo);

    /**
     * Get paginated scan history
     */
    PageResponse<Map<String, Object>> getScanHistory(int page, int size, String targetId);

    /**
     * Get location list
     */
    List<LocationResponse> getLocationList();

    /**
     * Get datatype list
     */
    List<Map<String, Object>> getDatatypeList();

    /**
     * Get paginated policy list
     */
    PageResponse<Map<String, Object>> getPolicyList(int page, int size);

    /**
     * Create a new policy
     */
    void createPolicy(PolicyRequest request, String regUserNo);

    /**
     * Update an existing policy
     */
    void updatePolicy(Long policyId, PolicyRequest request, String modUserNo);

    /**
     * Delete a policy
     */
    void deletePolicy(Long policyId);
}
