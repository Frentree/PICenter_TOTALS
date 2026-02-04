package com.org.iopts.lgotp.controller;

import java.util.HashMap;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.iopts.lgotp.service.LgOtpService;

@Controller
@RequestMapping(value = "/lg/otp")
public class LgOtpController {

	private static Logger logger = LoggerFactory.getLogger(LgOtpController.class);

	@Inject
	private LgOtpService lgOtpService;

	@RequestMapping(value = "/login", method = { RequestMethod.GET })
	public String loginPage(Model model) {
		logger.info("LG OTP Login Page");
		return "loginOTP_LG";
	}

	@RequestMapping(value = "/checkUserExists", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> checkUserExists(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("LG OTP Step1: Check User Exists");

		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			String user_no = request.getParameter("user_no");
			resultMap = lgOtpService.checkUserExists(user_no, request);
		} catch (Exception e) {
			logger.error("LG OTP Step1 Error", e);
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "사용자 확인 중 오류가 발생했습니다.");
			e.printStackTrace();
		}

		return resultMap;
	}

	@RequestMapping(value = "/authenticateOTP", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> authenticateOTP(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("LG OTP Step2: Authenticate OTP");

		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			resultMap = lgOtpService.authenticateOTP(request);
		} catch (Exception e) {
			logger.error("LG OTP Step2 Error", e);
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "OTP 인증 중 오류가 발생했습니다.");
			e.printStackTrace();
		}

		return resultMap;
	}
}
