package com.org.iopts.otp.service;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

public interface OTPService {

	Map<String, Object> OTPUserCheck(HttpServletRequest request) throws Exception;

	Map<String, Object> UserOTPInsert(HttpServletRequest request) throws Exception;

	Map<String, Object> selectMember(HttpServletRequest request) throws Exception;
}
