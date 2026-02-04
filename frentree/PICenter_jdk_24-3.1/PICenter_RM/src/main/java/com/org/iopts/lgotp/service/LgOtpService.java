package com.org.iopts.lgotp.service;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

public interface LgOtpService {

	Map<String, Object> checkUserExists(String userNo, HttpServletRequest request) throws Exception;

	Map<String, Object> authenticateOTP(HttpServletRequest request) throws Exception;
}
