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
}
