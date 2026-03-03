package com.org.iopts.controller;

import com.org.iopts.dto.response.ApiResponse;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.service.PopupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Tag(name = "Popup", description = "Popup Data API")
@RestController
@RequestMapping("/api/v1/popups")
@RequiredArgsConstructor
public class PopupController {

    private final PopupService popupService;

    @Operation(summary = "Get user list popup", description = "Get user list for popup selection")
    @GetMapping("/users")
    public ApiResponse<PageResponse<Map<String, Object>>> getUserListPopup(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Search keyword") @RequestParam(required = false) String keyword) {
        log.info("Popup user list: page={}, size={}", page, size);
        PageResponse<Map<String, Object>> response = popupService.getUserListPopup(page, size, keyword);
        return ApiResponse.success(response);
    }

    @Operation(summary = "Get group list popup", description = "Get group list for popup selection")
    @GetMapping("/groups")
    public ApiResponse<List<Map<String, Object>>> getGroupListPopup(
            @Parameter(description = "Search keyword") @RequestParam(required = false) String keyword) {
        log.info("Popup group list");
        List<Map<String, Object>> response = popupService.getGroupListPopup(keyword);
        return ApiResponse.success(response);
    }

    @Operation(summary = "Get host list popup", description = "Get host/target list for popup selection")
    @GetMapping("/hosts")
    public ApiResponse<PageResponse<Map<String, Object>>> getHostListPopup(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Search keyword") @RequestParam(required = false) String keyword) {
        log.info("Popup host list: page={}, size={}", page, size);
        PageResponse<Map<String, Object>> response = popupService.getHostListPopup(page, size, keyword);
        return ApiResponse.success(response);
    }

    @Operation(summary = "Get target list popup", description = "Get target list for popup selection")
    @GetMapping("/targets")
    public ApiResponse<PageResponse<Map<String, Object>>> getTargetListPopup(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Group ID filter") @RequestParam(required = false) String groupId) {
        log.info("Popup target list: page={}, size={}", page, size);
        PageResponse<Map<String, Object>> response = popupService.getTargetListPopup(page, size, groupId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "Get policy list popup", description = "Get scan policy list for popup selection")
    @GetMapping("/policies")
    public ApiResponse<List<Map<String, Object>>> getPolicyListPopup() {
        log.info("Popup policy list");
        List<Map<String, Object>> response = popupService.getPolicyListPopup();
        return ApiResponse.success(response);
    }

    @Operation(summary = "Get network list popup", description = "Get network list for popup selection")
    @GetMapping("/networks")
    public ApiResponse<List<Map<String, Object>>> getNetworkListPopup() {
        log.info("Popup network list");
        List<Map<String, Object>> response = popupService.getNetworkListPopup();
        return ApiResponse.success(response);
    }
}
