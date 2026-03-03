package com.org.iopts.service.impl;

import com.org.iopts.dto.request.PolicyRequest;
import com.org.iopts.dto.request.ScheduleCreateRequest;
import com.org.iopts.dto.response.LocationResponse;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.dto.response.ScheduleResponse;
import com.org.iopts.exception.CustomException;
import com.org.iopts.exception.ErrorCode;
import com.org.iopts.repository.ScanMapper;
import com.org.iopts.service.ScanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Scan Service Implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScanServiceImpl implements ScanService {

    private final ScanMapper scanMapper;

    @Override
    public PageResponse<ScheduleResponse> getScheduleList(int page, int size, String hostName, String status) {
        log.debug("Getting schedule list: page={}, size={}, hostName={}, status={}", page, size, hostName, status);

        int offset = page * size;
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("offset", offset);
        params.put("limit", size);
        if (hostName != null && !hostName.isEmpty()) params.put("hostName", hostName);
        if (status != null && !status.isEmpty()) params.put("status", status);

        List<ScheduleResponse> content = scanMapper.selectScheduleListFiltered(params);
        long totalElements = scanMapper.countScheduleListFiltered(params);

        return PageResponse.of(content, page, size, totalElements);
    }

    @Override
    public ScheduleResponse getScheduleDetail(String scheduleId) {
        log.debug("Getting schedule detail: scheduleId={}", scheduleId);

        ScheduleResponse schedule = scanMapper.selectScheduleDetail(scheduleId);
        if (schedule == null) {
            throw new CustomException(ErrorCode.NOT_FOUND, "Schedule not found");
        }
        return schedule;
    }

    @Override
    @Transactional
    public void createSchedule(ScheduleCreateRequest request, String regUserNo) {
        log.info("Creating schedule: name={}", request.getScheduleName());

        scanMapper.insertSchedule(
                request.getScheduleName(),
                request.getScheduleType(),
                request.getCronExpression(),
                request.getPolicyId(),
                request.getTargetGroupId(),
                request.getDescription(),
                regUserNo
        );
        log.info("Schedule created successfully: name={}", request.getScheduleName());
    }

    @Override
    @Transactional
    public void updateSchedule(String scheduleId, ScheduleCreateRequest request, String modUserNo) {
        log.info("Updating schedule: scheduleId={}", scheduleId);

        // Verify schedule exists
        ScheduleResponse existing = scanMapper.selectScheduleDetail(scheduleId);
        if (existing == null) {
            throw new CustomException(ErrorCode.NOT_FOUND, "Schedule not found");
        }

        scanMapper.updateSchedule(
                scheduleId,
                request.getScheduleName(),
                request.getScheduleType(),
                request.getCronExpression(),
                request.getPolicyId(),
                request.getTargetGroupId(),
                request.getDescription(),
                modUserNo
        );
        log.info("Schedule updated successfully: scheduleId={}", scheduleId);
    }

    @Override
    @Transactional
    public void deleteSchedule(String scheduleId) {
        log.info("Deleting schedule: scheduleId={}", scheduleId);

        // Verify schedule exists
        ScheduleResponse existing = scanMapper.selectScheduleDetail(scheduleId);
        if (existing == null) {
            throw new CustomException(ErrorCode.NOT_FOUND, "Schedule not found");
        }

        scanMapper.deleteSchedule(scheduleId);
        log.info("Schedule deleted successfully: scheduleId={}", scheduleId);
    }

    @Override
    @Transactional
    public void changeScheduleStatus(String scheduleId, Character statusYn) {
        log.info("Changing schedule status: scheduleId={}, statusYn={}", scheduleId, statusYn);

        // Verify schedule exists
        ScheduleResponse existing = scanMapper.selectScheduleDetail(scheduleId);
        if (existing == null) {
            throw new CustomException(ErrorCode.NOT_FOUND, "Schedule not found");
        }

        scanMapper.updateScheduleStatus(scheduleId, statusYn);
        log.info("Schedule status changed successfully: scheduleId={}, statusYn={}", scheduleId, statusYn);
    }

    @Override
    @Transactional
    public void executeSchedule(String scheduleId, String userNo) {
        log.info("Executing schedule: scheduleId={}, userNo={}", scheduleId, userNo);

        // Verify schedule exists
        ScheduleResponse existing = scanMapper.selectScheduleDetail(scheduleId);
        if (existing == null) {
            throw new CustomException(ErrorCode.NOT_FOUND, "Schedule not found");
        }

        // Update last run time
        scanMapper.updateScheduleLastRun(scheduleId);

        // Insert scan history record
        scanMapper.insertScanHistory(scheduleId, userNo);

        log.info("Schedule execution initiated: scheduleId={}", scheduleId);
    }

    @Override
    public PageResponse<Map<String, Object>> getScanHistory(int page, int size, String targetId, String hostName, String status) {
        log.debug("Getting scan history: page={}, size={}, targetId={}", page, size, targetId);

        int offset = page * size;
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("offset", offset);
        params.put("limit", size);
        if (targetId != null && !targetId.isEmpty()) params.put("targetId", targetId);
        if (hostName != null && !hostName.isEmpty()) params.put("hostName", hostName);
        if (status != null && !status.isEmpty()) params.put("status", status);

        List<Map<String, Object>> content = scanMapper.selectScanHistoryFiltered(params);
        long totalElements = scanMapper.countScanHistoryFiltered(params);

        return PageResponse.of(content, page, size, totalElements);
    }

    @Override
    public List<LocationResponse> getLocationList() {
        log.debug("Getting location list");
        return scanMapper.selectLocationList();
    }

    @Override
    public List<Map<String, Object>> getDatatypeList() {
        log.debug("Getting datatype list");
        return scanMapper.selectDatatypeList();
    }

    @Override
    public PageResponse<Map<String, Object>> getPolicyList(int page, int size) {
        log.debug("Getting policy list: page={}, size={}", page, size);

        int offset = page * size;
        List<Map<String, Object>> content = scanMapper.selectPolicyList(offset, size);
        long totalElements = scanMapper.countPolicyList();

        return PageResponse.of(content, page, size, totalElements);
    }

    @Override
    @Transactional
    public void createPolicy(PolicyRequest request, String regUserNo) {
        log.info("Creating policy: name={}", request.getPolicyName());

        scanMapper.insertPolicy(
                request.getPolicyName(),
                request.getDatatypeIds(),
                request.getLocationIds(),
                request.getDescription(),
                request.getUseYn(),
                regUserNo
        );
        log.info("Policy created successfully: name={}", request.getPolicyName());
    }

    @Override
    @Transactional
    public void updatePolicy(Long policyId, PolicyRequest request, String modUserNo) {
        log.info("Updating policy: policyId={}", policyId);

        scanMapper.updatePolicy(
                policyId,
                request.getPolicyName(),
                request.getDatatypeIds(),
                request.getLocationIds(),
                request.getDescription(),
                request.getUseYn(),
                modUserNo
        );
        log.info("Policy updated successfully: policyId={}", policyId);
    }

    @Override
    @Transactional
    public void deletePolicy(Long policyId) {
        log.info("Deleting policy: policyId={}", policyId);

        // Check if policy is referenced by any schedule
        int refCount = scanMapper.countSchedulesByPolicyId(policyId);
        if (refCount > 0) {
            throw new CustomException(ErrorCode.INVALID_PARAMETER,
                    "Cannot delete policy referenced by " + refCount + " schedule(s)");
        }

        scanMapper.deletePolicy(policyId);
        log.info("Policy deleted successfully: policyId={}", policyId);
    }
}
