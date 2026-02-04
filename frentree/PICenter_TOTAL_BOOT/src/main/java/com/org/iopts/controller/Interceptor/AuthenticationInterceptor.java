package com.org.iopts.controller.Interceptor;

import java.io.PrintWriter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.org.iopts.util.SessionUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AuthenticationInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		// XSS 보호를 위한 보안 헤더 추가
		setSecurityHeaders(response);
		
		// 모든 파라미터에 대해 XSS 검사
		validateAllParameters(request);

		logger.info("preHandle request.getRequestURI: {}", request.getRequestURI());

		String DocumentRoot = request.getContextPath();
		HttpSession session = request.getSession(true);

		if (session.getAttribute("memberSession") == null) {
			logger.info("memberSession null");

			if (isPopupAllowUrl(request.getRequestURI())) {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script language='javascript'>");
				out.println("self.close();");
				out.println("opener.popClose();");
				out.println("</script>");
				out.flush();
			}

			if (excludeUrl(request)) return true;

			logger.info("excludeUrl false");

			if (isAjaxRequest(request)) {
			    response.setStatus(401);
			    return false;
			}

			logger.info("isAjaxRequest {}", DocumentRoot);
			response.sendRedirect(DocumentRoot + "/");
			return false;
		} else {
			if (request.getRequestURI().equals("/")) {
				Object defaultPage = session.getAttribute("defaultPage");
				if (defaultPage != null) {
				    response.sendRedirect(defaultPage.toString());
				} else {
				    response.sendRedirect("/");
				}
				return false;
			} else if (
				request.getRequestURI().equals("/popup/lowPath") ||
				request.getRequestURI().equals("/popup/healthCheck") ||
				request.getRequestURI().equals("/popup/userList") ||
				request.getRequestURI().equals("/popup/helpDetail")
			) {
				return true;
			} else {
				return true;
			}
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if ((!excludeUrl(request)) && (!isAjaxRequest(request))) {
			if (modelAndView != null) {
				Map<String, Object> model = modelAndView.getModel();
				model.put("memberInfo", SessionUtil.getSession("memberSession"));
				model.put("pic_version", SessionUtil.getSession("versionSession"));
			}
		}
	}
	
	private void setSecurityHeaders(HttpServletResponse response) {
	    response.setHeader("Content-Security-Policy", 
	        "default-src 'self'; " +
	        "script-src 'self' 'unsafe-inline'; " +
	        "style-src 'self' 'unsafe-inline'; " +
	        "img-src 'self' data: blob:; " +
	        "font-src 'self' data:; " +                   
	        "worker-src 'self' blob:; " +
	        "frame-src 'none'; " +
	        "frame-ancestors 'none'; " +
	        "connect-src 'self';");
	    
	    response.setHeader("X-Frame-Options", "DENY");
	    response.setHeader("X-XSS-Protection", "1; mode=block");
//	    response.setHeader("X-Content-Type-Options", "nosniff");
//	    response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
	}
	
	// 모든 파라미터에 대해 XSS 검사 
	private void validateAllParameters(HttpServletRequest request) {
	    Map<String, String[]> parameterMap = request.getParameterMap();
	    
	    for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
	        String paramName = entry.getKey();
	        String[] values = entry.getValue();
	        
	        if (values != null && values.length > 0) {
	            for (String value : values) {
	                if (value != null && !value.isEmpty()) {
	                    if (containsXssPattern(value)) {
	                        logger.warn("XSS attempt detected in parameter '{}': {}", paramName, value);
	                        throw new SecurityException("잘못된 입력이 감지되었습니다.");
	                    }
	                }
	            }
	        }
	    }
	}

	// XSS 패턴 검사 메서드 추가
	private boolean containsXssPattern(String input) {
	    if (input == null || input.isEmpty()) {
	        return false;
	    }
	    
	    // 패턴 검사
	    String lowerInput = input.toLowerCase();
	    
	    return lowerInput.contains("<script") ||
	           lowerInput.contains("</script>") ||
	           lowerInput.contains("<iframe") ||
	           lowerInput.contains("javascript:") ||
	           lowerInput.matches(".*on\\w+\\s*=.*") ||  // onclick, onload 등
	           lowerInput.contains("<img") && lowerInput.contains("onerror") ||
	           lowerInput.contains("expression(") ||     // CSS expression
	           lowerInput.contains("vbscript:") ||
	           lowerInput.contains("<object") ||
	           lowerInput.contains("<embed");
	}
	

	private boolean isPopupAllowUrl(String uri) {
		return uri.equals("/healthCheck") ||
		       uri.equals("/popup/detectionDetail") || uri.startsWith("/popup/detectionDetail/") ||
		       uri.equals("/popup/exceptionGroupList") ||
		       uri.equals("/popup/groupList") ||
		       uri.equals("/popup/lowPath") ||
		       uri.equals("/popup/policyList") ||
		       uri.equals("/popup/targetList") ||
		       uri.equals("/popup/userList") ||
		       uri.equals("/popup/helpDetail");
	}

//	private boolean excludeUrl(HttpServletRequest request) {
//		String uri = request.getRequestURI().trim();
//		return uri.equals("/") || uri.equals("/login") || uri.equals("/loginSSO") || uri.equals("/healthCheck") ||
//		       uri.equals("/sso_link") || uri.equals("/loginsms") ||
//		       uri.equals("/submitSmsLogin") || uri.equals("/NCLoginTestENC") ||
//		       uri.equals("/NCLoginTestEndVerify") || uri.equals("/ssoLogin") || uri.equals("/session_quit") ||
//		       uri.equals("/main1") || uri.equals("/main2") || uri.equals("/main3") || uri.equals("/pie") ||
//		       uri.equals("/skt_approval") || uri.equals("/skt_contact_list") ||
//		       uri.equals("/demo") || uri.equals("/main_rm") ||
//		       uri.equals("/favicon.ico") || uri.equals("/reset_sms_code") || uri.equals("/lockMemberRequest") ||
//		       uri.equals("/piboard_skt") || uri.equals("/sms_login_resend") ||
//		       uri.equals("/sms_login_cancel") || uri.equals("/resetnum") || uri.equals("/user/SMSFlag") ||
//		       uri.equals("/resetPwd") || uri.equals("/changeResetPwd") ||
//		       uri.equals("/popup/detectionDetail") || uri.equals("/popup/userList") || uri.equals("/popup/helpDetail") ||
//		       uri.equals("/changeAuthCharacter");
//	}
	private boolean excludeUrl(HttpServletRequest request) {
	    String uri = request.getRequestURI().trim();
	    return uri.equals("/") ||                 
	           uri.equals("/login") ||              
	           uri.equals("/changeAuthCharacter") ||              
	           uri.equals("/logout") ||              
	           uri.equals("/favicon.ico") ||       
	           uri.equals("/healthCheck") ||        
	           uri.equals("/error") ||              
	           uri.startsWith("/resources/") ||    
	           uri.startsWith("/assets/") ||         
	           uri.startsWith("/css/") ||
	           uri.startsWith("/js/") ||
	           uri.startsWith("/images/");
	}

	private boolean isAjaxRequest(HttpServletRequest request) {
		return "xmlhttprequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
	}
}