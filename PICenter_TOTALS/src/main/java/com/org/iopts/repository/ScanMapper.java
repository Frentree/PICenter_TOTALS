package com.org.iopts.repository;

import com.org.iopts.dto.response.LocationResponse;
import com.org.iopts.dto.response.ScheduleResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Scan MyBatis Mapper
 */
@Mapper
public interface ScanMapper {

    /**
     * Get schedule list with pagination
     */
    List<ScheduleResponse> selectScheduleList(@Param("offset") int offset,
                                              @Param("limit") int limit);

    /**
     * Count total schedules
     */
    long countScheduleList();

    /**
     * Get schedule detail by ID
     */
    ScheduleResponse selectScheduleDetail(@Param("scheduleId") String scheduleId);

    /**
     * Insert a new schedule
     */
    void insertSchedule(@Param("scheduleName") String scheduleName,
                        @Param("scheduleType") String scheduleType,
                        @Param("cronExpression") String cronExpression,
                        @Param("policyId") String policyId,
                        @Param("targetGroupId") String targetGroupId,
                        @Param("description") String description,
                        @Param("regUserNo") String regUserNo);

    /**
     * Update an existing schedule
     */
    void updateSchedule(@Param("scheduleId") String scheduleId,
                        @Param("scheduleName") String scheduleName,
                        @Param("scheduleType") String scheduleType,
                        @Param("cronExpression") String cronExpression,
                        @Param("policyId") String policyId,
                        @Param("targetGroupId") String targetGroupId,
                        @Param("description") String description,
                        @Param("modUserNo") String modUserNo);

    /**
     * Delete a schedule by ID
     */
    void deleteSchedule(@Param("scheduleId") String scheduleId);

    /**
     * Change schedule status (enable/disable)
     */
    void updateScheduleStatus(@Param("scheduleId") String scheduleId,
                              @Param("statusYn") Character statusYn);

    /**
     * Update last run time for a schedule
     */
    void updateScheduleLastRun(@Param("scheduleId") String scheduleId);

    /**
     * Insert scan history record
     */
    void insertScanHistory(@Param("scheduleId") String scheduleId,
                           @Param("userNo") String userNo);

    /**
     * Get scan history with pagination
     */
    List<Map<String, Object>> selectScanHistory(@Param("offset") int offset,
                                                @Param("limit") int limit,
                                                @Param("targetId") String targetId);

    /**
     * Count total scan history records
     */
    long countScanHistory(@Param("targetId") String targetId);

    /**
     * Get location list
     */
    List<LocationResponse> selectLocationList();

    /**
     * Get datatype list
     */
    List<Map<String, Object>> selectDatatypeList();

    /**
     * Get policy list with pagination
     */
    List<Map<String, Object>> selectPolicyList(@Param("offset") int offset,
                                               @Param("limit") int limit);

    /**
     * Count total policies
     */
    long countPolicyList();

    /**
     * Insert a new policy
     */
    void insertPolicy(@Param("policyName") String policyName,
                      @Param("datatypeIds") String datatypeIds,
                      @Param("locationIds") String locationIds,
                      @Param("description") String description,
                      @Param("useYn") Character useYn,
                      @Param("regUserNo") String regUserNo);

    /**
     * Update an existing policy
     */
    void updatePolicy(@Param("policyId") Long policyId,
                      @Param("policyName") String policyName,
                      @Param("datatypeIds") String datatypeIds,
                      @Param("locationIds") String locationIds,
                      @Param("description") String description,
                      @Param("useYn") Character useYn,
                      @Param("modUserNo") String modUserNo);

    /**
     * Delete a policy by ID
     */
    void deletePolicy(@Param("policyId") Long policyId);

    /**
     * Check if policy is referenced by any schedule
     */
    int countSchedulesByPolicyId(@Param("policyId") Long policyId);
}
