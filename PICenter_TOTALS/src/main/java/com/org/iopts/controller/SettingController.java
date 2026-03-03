package com.org.iopts.controller;

import com.org.iopts.dto.response.ApiResponse;
import com.org.iopts.service.SettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Setting Controller
 *
 * Provides REST API endpoints for page settings management.
 *
 * Legacy endpoint mappings:
 *   GET /api/v1/settings/pages  <- /getPageSettings
 *   PUT /api/v1/settings/pages  <- /savePageSettings
 */
@Slf4j
@Tag(name = "Settings", description = "Settings API")
@RestController
@RequestMapping("/api/v1/settings")
@RequiredArgsConstructor
public class SettingController {

    private final SettingService settingService;

    /**
     * Get page settings
     *
     * Legacy: GET /getPageSettings
     * New: GET /api/v1/settings/pages
     */
    @Operation(summary = "Get page settings", description = "Get all page settings")
    @GetMapping("/pages")
    public ApiResponse<List<Map<String, Object>>> getPageSettings() {

        log.debug("GET /api/v1/settings/pages");
        List<Map<String, Object>> result = settingService.getPageSettings();
        return ApiResponse.success(result);
    }

    /**
     * Save page settings
     *
     * Legacy: POST /savePageSettings
     * New: PUT /api/v1/settings/pages
     */
    @Operation(summary = "Save page settings", description = "Create or update page settings")
    @PutMapping("/pages")
    public ApiResponse<Void> savePageSettings(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "페이지 설정 목록",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "[{\"pageId\": \"dashboard\", \"pageSize\": 20, \"useYn\": \"Y\"}, {\"pageId\": \"detection\", \"pageSize\": 50, \"useYn\": \"Y\"}]"
                            )
                    )
            )
            @RequestBody List<Map<String, Object>> settings) {

        log.info("PUT /api/v1/settings/pages - count: {}", settings != null ? settings.size() : 0);
        settingService.savePageSettings(settings);
        return ApiResponse.success();
    }

    /**
     * Get node list
     *
     * New: GET /api/v1/settings/nodes
     */
    @Operation(summary = "Get node list", description = "Get all registered nodes")
    @GetMapping("/nodes")
    public ApiResponse<List<Map<String, Object>>> getNodeList() {
        log.debug("GET /api/v1/settings/nodes");
        List<Map<String, Object>> result = settingService.getNodeList();
        return ApiResponse.success(result);
    }

    /**
     * Save node
     *
     * New: POST /api/v1/settings/nodes
     */
    @Operation(summary = "Save node", description = "Create or update a node")
    @PostMapping("/nodes")
    public ApiResponse<Void> saveNode(@RequestBody Map<String, Object> request) {
        log.info("POST /api/v1/settings/nodes");
        settingService.saveNode(request);
        return ApiResponse.success();
    }

    /**
     * Update node
     *
     * New: PUT /api/v1/settings/nodes/{nodeId}
     */
    @Operation(summary = "Update node", description = "Update an existing node")
    @PutMapping("/nodes/{nodeId}")
    public ApiResponse<Void> updateNode(@PathVariable String nodeId, @RequestBody Map<String, Object> request) {
        log.info("PUT /api/v1/settings/nodes/{}", nodeId);
        request.put("nodeId", nodeId);
        settingService.saveNode(request);
        return ApiResponse.success();
    }

    /**
     * Delete node
     *
     * New: DELETE /api/v1/settings/nodes/{nodeId}
     */
    @Operation(summary = "Delete node", description = "Delete a node")
    @DeleteMapping("/nodes/{nodeId}")
    public ApiResponse<Void> deleteNode(@PathVariable String nodeId) {
        log.info("DELETE /api/v1/settings/nodes/{}", nodeId);
        settingService.deleteNode(nodeId);
        return ApiResponse.success();
    }

    /**
     * Get interlock settings
     *
     * New: GET /api/v1/settings/interlock
     */
    @Operation(summary = "Get interlock settings", description = "Get interlock/integration settings")
    @GetMapping("/interlock")
    public ApiResponse<List<Map<String, Object>>> getInterlockSettings() {
        log.debug("GET /api/v1/settings/interlock");
        List<Map<String, Object>> result = settingService.getInterlockSettings();
        return ApiResponse.success(result);
    }

    /**
     * Create interlock setting
     *
     * New: POST /api/v1/settings/interlock
     */
    @Operation(summary = "Create interlock setting", description = "Create a new interlock setting")
    @PostMapping("/interlock")
    public ApiResponse<Void> createInterlockSetting(@RequestBody Map<String, Object> request) {
        log.info("POST /api/v1/settings/interlock");
        settingService.saveInterlockSetting(request);
        return ApiResponse.success();
    }

    /**
     * Update interlock setting
     *
     * New: PUT /api/v1/settings/interlock/{interlockId}
     */
    @Operation(summary = "Update interlock setting", description = "Update an existing interlock setting")
    @PutMapping("/interlock/{interlockId}")
    public ApiResponse<Void> updateInterlockSetting(@PathVariable String interlockId, @RequestBody Map<String, Object> request) {
        log.info("PUT /api/v1/settings/interlock/{}", interlockId);
        request.put("interlockId", interlockId);
        settingService.saveInterlockSetting(request);
        return ApiResponse.success();
    }

    /**
     * Delete interlock setting
     *
     * New: DELETE /api/v1/settings/interlock/{interlockId}
     */
    @Operation(summary = "Delete interlock setting", description = "Delete an interlock setting")
    @DeleteMapping("/interlock/{interlockId}")
    public ApiResponse<Void> deleteInterlockSetting(@PathVariable String interlockId) {
        log.info("DELETE /api/v1/settings/interlock/{}", interlockId);
        settingService.deleteInterlockSetting(interlockId);
        return ApiResponse.success();
    }
}
