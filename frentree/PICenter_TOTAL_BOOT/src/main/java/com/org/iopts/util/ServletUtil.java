package com.org.iopts.util;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ServletUtil {
	private static final Logger logger = LoggerFactory.getLogger(ServletUtil.class);
	private HttpServletRequest request;
	
	public ServletUtil(HttpServletRequest r) {
		this.request=r;
	}
	
	public String getIp() {
		 
		/*InetAddress myIP = null;
		try {
			myIP = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		String ip = request.getRemoteAddr();
		logger.info("ip = getRemoteAddr");
		
		if (ip == null) {
			ip = request.getHeader("Proxy-Client-IP");
			logger.info("ip = Proxy-Client-IP");
		}
		if (ip == null) {
            ip = request.getRemoteAddr();
            logger.info("ip = getRemoteAddr");
        }
		if (ip == null) {
			ip = request.getHeader("WL-Proxy-Client-IP"); // ������
			logger.info("ip = WL-Proxy-Client-IP");
		}
		if (ip == null) {
			if (request != null) {
			    String clientIp = request.getHeader("HTTP_CLIENT_IP");
			    if (clientIp != null) {
			        ip = clientIp;
			        logger.info("ip = HTTP_CLIENT_IP");
			    }
			}
		}
		if (ip == null && request != null) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			logger.info("ip = HTTP_X_FORWARDED_FOR");
		}
		if (ip == null && request != null) {
		    String forwardedIp = request.getHeader("X-Forwarded-For");
		    if (forwardedIp != null) {
		        ip = forwardedIp;
		        logger.info("ip = X-Forwarded-For");
		    }
		}
        
        
        return ip;
 
    }

}
