package com.org.iopts.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;

/**
 * XSS 공격 방지를 위한 HttpServletRequest Wrapper
 * 모든 파라미터 값에서 XSS 위험 문자를 이스케이프 처리
 */
public class XssRequestWrapper extends HttpServletRequestWrapper {

    public XssRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        
        if (value == null) {
            return null;
        }
        
        // JSON 배열이나 객체 형식이면 필터링 제외
        String trimmed = value.trim();
        if ((trimmed.startsWith("[") && trimmed.endsWith("]")) || 
            (trimmed.startsWith("{") && trimmed.endsWith("}"))) {
            return value;
        }
        
        return cleanXss(value);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }
        String[] cleanValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null) {
                String trimmed = values[i].trim();
                // JSON 형식이면 필터링 제외
                if ((trimmed.startsWith("[") && trimmed.endsWith("]")) || 
                    (trimmed.startsWith("{") && trimmed.endsWith("}"))) {
                    cleanValues[i] = values[i];
                } else {
                    cleanValues[i] = cleanXss(values[i]);
                }
            } else {
                cleanValues[i] = null;
            }
        }
        return cleanValues;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> originalMap = super.getParameterMap();
        Map<String, String[]> cleanMap = new HashMap<>();

        for (Map.Entry<String, String[]> entry : originalMap.entrySet()) {
            String[] values = entry.getValue();
            String[] cleanValues = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                cleanValues[i] = cleanXss(values[i]);
            }
            cleanMap.put(entry.getKey(), cleanValues);
        }
        return cleanMap;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return cleanXss(value);
    }

    /**
     * XSS 위험 문자를 HTML 엔티티로 이스케이프 처리
     */
    private String cleanXss(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        // HTML 특수문자 이스케이프
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case '\'':
                    sb.append("&#x27;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '/':
                    sb.append("&#x2F;");
                    break;
                default:
                    sb.append(c);
            }
        }

        String result = sb.toString();

        // 추가적인 XSS 패턴 제거 (대소문자 무시)
        result = result.replaceAll("(?i)javascript:", "");
        result = result.replaceAll("(?i)vbscript:", "");
        result = result.replaceAll("(?i)onload", "");
        result = result.replaceAll("(?i)onerror", "");
        result = result.replaceAll("(?i)onclick", "");
        result = result.replaceAll("(?i)onmouseover", "");
        result = result.replaceAll("(?i)onfocus", "");
        result = result.replaceAll("(?i)onblur", "");
        result = result.replaceAll("(?i)onsubmit", "");
        result = result.replaceAll("(?i)onchange", "");
        result = result.replaceAll("(?i)ondblclick", "");
        result = result.replaceAll("(?i)onkeydown", "");
        result = result.replaceAll("(?i)onkeyup", "");
        result = result.replaceAll("(?i)onkeypress", "");
        result = result.replaceAll("(?i)onmousedown", "");
        result = result.replaceAll("(?i)onmouseup", "");
        result = result.replaceAll("(?i)onmousemove", "");
        result = result.replaceAll("(?i)onmouseout", "");
        result = result.replaceAll("(?i)expression\\s*\\(", "");
        result = result.replaceAll("(?i)eval\\s*\\(", "");

        return result;
    }
}
