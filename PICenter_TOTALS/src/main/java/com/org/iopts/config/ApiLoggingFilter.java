package com.org.iopts.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * API 요청/응답 로깅 필터
 *
 * 모든 REST API 호출을 logs/picenter-api.log에 기록
 * 형식: [METHOD] /path | IP | STATUS | 소요시간ms | 사용자
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class ApiLoggingFilter extends OncePerRequestFilter {

    private static final Logger apiLog = LoggerFactory.getLogger("API_ACCESS");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 정적 리소스는 로깅 제외
        String uri = request.getRequestURI();
        if (isStaticResource(uri)) {
            filterChain.doFilter(request, response);
            return;
        }

        long startTime = System.currentTimeMillis();
        String method = request.getMethod();
        String clientIp = getClientIp(request);
        String queryString = request.getQueryString();
        String fullPath = queryString != null ? uri + "?" + queryString : uri;

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            int status = response.getStatus();
            String user = extractUser(request);

            apiLog.info("[{}] {} | {} | {} | {}ms | {}",
                    method, fullPath, clientIp, status, duration, user);
        }
    }

    private boolean isStaticResource(String uri) {
        return uri.startsWith("/css/") ||
               uri.startsWith("/js/") ||
               uri.startsWith("/images/") ||
               uri.startsWith("/webjars/") ||
               uri.startsWith("/swagger-ui/") ||
               uri.endsWith(".html") ||
               uri.endsWith(".css") ||
               uri.endsWith(".js") ||
               uri.endsWith(".ico") ||
               uri.endsWith(".png") ||
               uri.endsWith(".jpg");
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // X-Forwarded-For에 여러 IP가 있는 경우 첫 번째만
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    private String extractUser(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            // JWT 토큰에서 사용자 정보 추출 (payload의 userId)
            try {
                String token = auth.substring(7);
                String[] parts = token.split("\\.");
                if (parts.length == 3) {
                    String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
                    // 간단한 userId 추출
                    int idx = payload.indexOf("\"userId\"");
                    if (idx >= 0) {
                        int start = payload.indexOf("\"", idx + 8) + 1;
                        int end = payload.indexOf("\"", start);
                        if (start > 0 && end > start) {
                            return payload.substring(start, end);
                        }
                    }
                }
            } catch (Exception ignored) {}
            return "authenticated";
        }
        return "anonymous";
    }
}
