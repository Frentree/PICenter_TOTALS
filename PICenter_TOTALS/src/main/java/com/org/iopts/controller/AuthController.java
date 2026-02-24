package com.org.iopts.controller;

import com.org.iopts.dto.request.LoginRequest;
import com.org.iopts.dto.response.ApiResponse;
import com.org.iopts.dto.response.LoginResponse;
import com.org.iopts.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller
 * Migrated from legacy HomeController.login()
 */
@Slf4j
@Tag(name = "Authentication", description = "Authentication API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * User Login
     *
     * Legacy: POST /memberLogin
     * New: POST /api/v1/auth/login
     */
    @Operation(summary = "User login", description = "Login with username and password")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login request: {}", request.getUsername());
        LoginResponse response = userService.login(request);
        return ApiResponse.success(response);
    }

    /**
     * User Logout
     *
     * Legacy: POST /logout
     * New: POST /api/v1/auth/logout
     */
    @Operation(summary = "User logout", description = "Logout current user")
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        String token = extractToken(request);
        userService.logout(token);
        return ApiResponse.success();
    }

    /**
     * Refresh Token
     *
     * New endpoint for JWT refresh
     */
    @Operation(summary = "Refresh token", description = "Get new access token using refresh token")
    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refresh(@RequestHeader("Authorization") String authorization) {
        String refreshToken = authorization.replace("Bearer ", "");
        String newAccessToken = userService.refreshToken(refreshToken);
        return ApiResponse.success(LoginResponse.builder()
                .accessToken(newAccessToken)
                .build());
    }

    /**
     * Get Current User Info
     *
     * Legacy: GET /changeUser
     * New: GET /api/v1/auth/me
     */
    @Operation(summary = "Get current user info", description = "Get current authenticated user information")
    @GetMapping("/me")
    public ApiResponse<LoginResponse> getCurrentUser(HttpServletRequest request) {
        String userNo = getUserNoFromRequest(request);
        LoginResponse response = userService.getUserInfo(userNo);
        return ApiResponse.success(response);
    }

    /**
     * Extract token from Authorization header
     */
    private String extractToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }

    /**
     * Get userNo from SecurityContext (set by JwtAuthenticationFilter)
     */
    private String getUserNoFromRequest(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof String) {
            return (String) authentication.getPrincipal();
        }
        throw new RuntimeException("User not authenticated");
    }
}
