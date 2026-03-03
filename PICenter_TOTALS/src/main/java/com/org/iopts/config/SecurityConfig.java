package com.org.iopts.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Security Configuration
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Security Filter Chain
     *
     * Protected endpoints (JWT required):
     *   Phase 1: /api/v1/users/**        - User management
     *   Phase 2: /api/v1/targets/**      - Target management
     *            /api/v1/scans/**         - Scan management
     *   Phase 3: /api/v1/detections/**   - Detection management
     *            /api/v1/approvals/**     - Approval management
     *            /api/v1/exceptions/**    - Exception management
     *   Phase 4: /api/v1/dashboard/**    - Dashboard
     *            /api/v1/statistics/**    - Statistics
     *            /api/v1/reports/**       - Reports
     *   Phase 5: /api/v1/notices/**      - Notice management
     *            /api/v1/groups/**        - Group management
     *            /api/v1/downloads/**     - Download management
     *            /api/v1/faqs/**          - FAQ management
     *            /api/v1/settings/**      - Settings management
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF disable
                .csrf(AbstractHttpConfigurer::disable)

                // CORS configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Session management: stateless
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Authorization
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(
                                "/",
                                "/login",
                                "/login.html",
                                // Existing pages
                                "/picenter_dashboard", "/picenter_dashboard.html",
                                "/picenter_target", "/picenter_target.html",
                                "/picenter_detection", "/picenter_detection.html",
                                "/picenter_scan", "/picenter_scan.html",
                                "/picenter_report", "/picenter_report.html",
                                "/picenter_statistics", "/picenter_statistics.html",
                                "/picenter_user", "/picenter_user.html",
                                "/picenter_notice", "/picenter_notice.html",
                                "/picenter_setting", "/picenter_setting.html",
                                // New pages - Target sub-menus
                                "/picenter_target_group", "/picenter_target_group.html",
                                "/picenter_global_filters", "/picenter_global_filters.html",
                                // New pages - Detection sub-menus
                                "/picenter_exception", "/picenter_exception.html",
                                "/picenter_approval", "/picenter_approval.html",
                                // New pages - Scan sub-menus
                                "/picenter_search_regist", "/picenter_search_regist.html",
                                "/picenter_search_list", "/picenter_search_list.html",
                                // New pages - Report sub-menus
                                "/picenter_report_except", "/picenter_report_except.html",
                                // New pages - User sub-menus
                                "/picenter_userlog", "/picenter_userlog.html",
                                "/picenter_user_lockdown", "/picenter_user_lockdown.html",
                                // New pages - Notice sub-menus
                                "/picenter_faq", "/picenter_faq.html",
                                "/picenter_download", "/picenter_download.html",
                                // New pages - Setting sub-menus
                                "/picenter_node", "/picenter_node.html",
                                "/picenter_interlock", "/picenter_interlock.html",
                                // Data Type page
                                "/picenter_data_type", "/picenter_data_type.html",
                                "/search/data_type",
                                // Static resources
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/api/v1/auth/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/api-docs/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/error",
                                "/health"
                        ).permitAll()
                        // All other requests require authentication
                        .anyRequest().authenticated()
                )

                // Add JWT filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS Configuration
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Password Encoder - MySQL SHA2(HEX(password), 256) 호환
     * 레거시 시스템과 동일한 해싱 방식
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                try {
                    // Step 1: HEX() - 각 바이트를 대문자 2자리 hex로 변환 (MySQL HEX() 동일)
                    byte[] bytes = rawPassword.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
                    StringBuilder hexStr = new StringBuilder(bytes.length * 2);
                    for (byte b : bytes) {
                        hexStr.append(String.format("%02X", b));
                    }
                    // Step 2: SHA2(hex_string, 256) - hex 문자열의 SHA-256 해시
                    java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
                    byte[] hash = md.digest(hexStr.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8));
                    StringBuilder result = new StringBuilder(hash.length * 2);
                    for (byte b : hash) {
                        result.append(String.format("%02x", b));
                    }
                    return result.toString();
                } catch (Exception e) {
                    throw new RuntimeException("SHA-256 encoding failed", e);
                }
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encode(rawPassword).equals(encodedPassword);
            }
        };
    }
}
