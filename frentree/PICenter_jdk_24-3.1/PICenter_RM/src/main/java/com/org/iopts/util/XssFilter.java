package com.org.iopts.util;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * XSS(Cross-Site Scripting) 공격 방지 필터
 * 모든 HTTP 요청의 파라미터를 XssRequestWrapper로 감싸서 XSS 위험 문자를 이스케이프 처리
 */
public class XssFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(XssFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("XssFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            // XSS 필터링이 필요한 요청인지 확인
            String contentType = httpRequest.getContentType();

            // JSON 요청은 별도 처리가 필요할 수 있으므로 제외 (필요시 주석 해제)
             if (contentType != null && contentType.contains("application/json")) {
                 chain.doFilter(request, response);
                 return;
             }

            // XssRequestWrapper로 감싸서 전달
            XssRequestWrapper wrappedRequest = new XssRequestWrapper(httpRequest);
            chain.doFilter(wrappedRequest, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        logger.info("XssFilter destroyed");
    }
}
