package com.org.iopts.controller;

import com.org.iopts.dto.response.ApiResponse;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Tag(name = "Search", description = "Search Management API")
@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "Get search registration list", description = "Get paginated search registration list")
    @GetMapping("/registrations")
    public ApiResponse<PageResponse<Map<String, Object>>> getRegistrationList(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Search keyword") @RequestParam(required = false) String keyword,
            @Parameter(description = "Search keyword (alias)") @RequestParam(required = false) String searchKeyword) {
        log.info("Get search registrations: page={}, size={}", page, size);
        String effectiveKeyword = (keyword != null && !keyword.isEmpty()) ? keyword : searchKeyword;
        PageResponse<Map<String, Object>> response = searchService.getRegistrationList(page, size, effectiveKeyword);
        return ApiResponse.success(response);
    }

    @Operation(summary = "Create search registration", description = "Register a new search task")
    @PostMapping("/registrations")
    public ApiResponse<Void> createRegistration(
            @RequestBody Map<String, Object> request,
            Authentication authentication) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Create search registration by user: {}", userNo);
        searchService.createRegistration(request, userNo);
        return ApiResponse.success();
    }

    @Operation(summary = "Delete search registration", description = "Delete a search registration")
    @DeleteMapping("/registrations/{registId}")
    public ApiResponse<Void> deleteRegistration(
            @PathVariable Long registId) {
        log.info("Delete search registration: {}", registId);
        searchService.deleteRegistration(registId);
        return ApiResponse.success();
    }

    @Operation(summary = "Get search result list", description = "Get paginated search result list")
    @GetMapping("/results")
    public ApiResponse<PageResponse<Map<String, Object>>> getResultList(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Search keyword") @RequestParam(required = false) String keyword,
            @Parameter(description = "Search keyword (alias)") @RequestParam(required = false) String searchKeyword,
            @Parameter(description = "Status filter") @RequestParam(required = false) String status,
            @Parameter(description = "Start date (yyyy-MM-dd)") @RequestParam(required = false) String startDate,
            @Parameter(description = "End date (yyyy-MM-dd)") @RequestParam(required = false) String endDate) {
        log.info("Get search results: page={}, size={}", page, size);
        String effectiveKeyword = (keyword != null && !keyword.isEmpty()) ? keyword : searchKeyword;
        PageResponse<Map<String, Object>> response = searchService.getResultList(page, size, effectiveKeyword, status, startDate, endDate);
        return ApiResponse.success(response);
    }

    @Operation(summary = "Get search result detail", description = "Get search result detail")
    @GetMapping("/results/{resultId}")
    public ApiResponse<Map<String, Object>> getResultDetail(
            @PathVariable Long resultId) {
        log.info("Get search result detail: {}", resultId);
        Map<String, Object> response = searchService.getResultDetail(resultId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "Get search status summary", description = "Get search status summary counts")
    @GetMapping("/status")
    public ApiResponse<Map<String, Object>> getSearchStatus() {
        log.info("Get search status");
        Map<String, Object> response = searchService.getSearchStatus();
        return ApiResponse.success(response);
    }

    // ==================== Data Type (Profile) API ====================

    @Operation(summary = "Get data type list", description = "개인정보 유형 목록 조회")
    @GetMapping("/data-types")
    public ApiResponse<List<Map<String, Object>>> getDataTypeList(
            @Parameter(description = "Search keyword") @RequestParam(required = false) String keyword) {
        log.info("Get data type list: keyword={}", keyword);
        List<Map<String, Object>> response = searchService.getDataTypeList(keyword);
        return ApiResponse.success(response);
    }

    @Operation(summary = "Create data type", description = "개인정보 유형 생성")
    @PostMapping("/data-types")
    public ApiResponse<Void> createDataType(
            @RequestBody Map<String, Object> request,
            Authentication authentication) {
        String userNo = (String) authentication.getPrincipal();
        log.info("Create data type by user: {}", userNo);
        searchService.createDataType(request, userNo);
        return ApiResponse.success();
    }

    @Operation(summary = "Update data type", description = "개인정보 유형 수정")
    @PutMapping("/data-types/{stdId}")
    public ApiResponse<Void> updateDataType(
            @PathVariable String stdId,
            @RequestBody Map<String, Object> request,
            Authentication authentication) {
        String userNo = (String) authentication.getPrincipal();
        request.put("stdId", stdId);
        log.info("Update data type: stdId={}, user={}", stdId, userNo);
        searchService.updateDataType(request, userNo);
        return ApiResponse.success();
    }

    @Operation(summary = "Delete data type", description = "개인정보 유형 삭제")
    @DeleteMapping("/data-types/{stdId}")
    public ApiResponse<Void> deleteDataType(@PathVariable String stdId) {
        log.info("Delete data type: stdId={}", stdId);
        searchService.deleteDataType(stdId);
        return ApiResponse.success();
    }
}
