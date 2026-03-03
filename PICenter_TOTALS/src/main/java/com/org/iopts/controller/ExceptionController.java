package com.org.iopts.controller;

import com.org.iopts.dto.request.PathExceptionRequest;
import com.org.iopts.dto.response.ApiResponse;
import com.org.iopts.dto.response.PageResponse;
import com.org.iopts.dto.response.PathExceptionResponse;
import com.org.iopts.service.ExceptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Exception Management Controller
 * Migrated from legacy path exception handling
 */
@Slf4j
@Tag(name = "Exception Management", description = "PI Exception Management API")
@RestController
@RequestMapping("/api/v1/exceptions")
@RequiredArgsConstructor
public class ExceptionController {

    private final ExceptionService exceptionService;

    /**
     * Get Exception List (root endpoint)
     *
     * New: GET /api/v1/exceptions
     */
    @Operation(summary = "Get exception list", description = "Get paginated exception list with search filters")
    @GetMapping
    public ApiResponse<PageResponse<PathExceptionResponse>> getExceptionList(
            @Parameter(description = "Search keyword") @RequestParam(required = false) String searchKeyword,
            @Parameter(description = "Start date (yyyy-MM-dd)") @RequestParam(required = false) String startDate,
            @Parameter(description = "End date (yyyy-MM-dd)") @RequestParam(required = false) String endDate,
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size) {

        log.info("Get exception list - keyword: {}, startDate: {}, endDate: {}, page: {}, size: {}",
                searchKeyword, startDate, endDate, page, size);
        PageResponse<PathExceptionResponse> response = exceptionService.getPathExceptionList(
                searchKeyword, startDate, endDate, page, size);
        return ApiResponse.success(response);
    }

    /**
     * Get Path Exception List
     *
     * Legacy: POST /getPathExceptionList
     * New: GET /api/v1/exceptions/paths
     */
    @Operation(summary = "Get path exception list", description = "Get paginated path exception list")
    @GetMapping("/paths")
    public ApiResponse<PageResponse<PathExceptionResponse>> getPathExceptionList(
            @Parameter(description = "Search keyword") @RequestParam(required = false) String searchKeyword,
            @Parameter(description = "Start date (yyyy-MM-dd)") @RequestParam(required = false) String startDate,
            @Parameter(description = "End date (yyyy-MM-dd)") @RequestParam(required = false) String endDate,
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size) {

        log.info("Get path exception list - page: {}, size: {}", page, size);
        PageResponse<PathExceptionResponse> response = exceptionService.getPathExceptionList(
                searchKeyword, startDate, endDate, page, size);
        return ApiResponse.success(response);
    }

    /**
     * Create Path Exception
     *
     * Legacy: POST /insertPathException
     * New: POST /api/v1/exceptions/paths
     */
    @Operation(summary = "Create path exception", description = "Create a new path exception rule")
    @PostMapping("/paths")
    public ApiResponse<Void> createPathException(
            @Valid @RequestBody PathExceptionRequest request,
            Authentication authentication) {

        String userNo = (String) authentication.getPrincipal();
        log.info("Create path exception - path: {}, userNo: {}", request.getExceptionPath(), userNo);

        exceptionService.createPathException(request, userNo);
        return ApiResponse.success();
    }

    /**
     * Delete Path Exception
     *
     * Legacy: POST /deletePathException
     * New: DELETE /api/v1/exceptions/paths/{exceptionId}
     */
    @Operation(summary = "Delete path exception", description = "Soft-delete a path exception rule")
    @DeleteMapping("/paths/{exceptionId}")
    public ApiResponse<Void> deletePathException(
            @Parameter(description = "Exception ID", example = "1") @PathVariable Long exceptionId) {

        log.info("Delete path exception - exceptionId: {}", exceptionId);
        exceptionService.deletePathException(exceptionId);
        return ApiResponse.success();
    }
}
