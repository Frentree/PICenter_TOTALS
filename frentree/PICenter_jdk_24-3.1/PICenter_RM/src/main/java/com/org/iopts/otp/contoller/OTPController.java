package com.org.iopts.otp.contoller;

import java.util.HashMap;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.iopts.otp.service.OTPService;

@Controller
@RequestMapping(value = "/otp")
@Configuration
@PropertySource("classpath:/property/config.properties")
public class OTPController {

	private static Logger logger = LoggerFactory.getLogger(OTPController.class);

	@Value("${recon.api.version}")
	private String api_ver;
	
	@Inject
	private OTPService service;
	
	
	@RequestMapping(value = "/OTPUserCheck", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> login(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("/OTPUserCheck");

		// 사용자번호 + 비밀번호로 로그인 하는 경우
		Map<String, Object> memberMap = new HashMap<String, Object>();
		try {
			memberMap = service.OTPUserCheck(request);
			String result = memberMap.get("resultCode").toString();
			memberMap.put("resultCode", result);
			memberMap.put("resultMessage", memberMap.get("resultMessage"));
		} catch (Exception e) {
			memberMap.put("resultCode", -100);
			e.printStackTrace();
		}
		return memberMap;
	}
	
	@RequestMapping(value = "/UserOTPInsert", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> UserOTPInsert(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("/UserOTPInsert");
		
		// 사용자번호 + 비밀번호로 로그인 하는 경우
		Map<String, Object> memberMap = new HashMap<String, Object>();
		try {
			memberMap = service.UserOTPInsert(request);
			String result = memberMap.get("resultCode").toString();
			memberMap.put("resultCode", result);
			memberMap.put("resultMessage", memberMap.get("resultMessage"));
		} catch (Exception e) {
			memberMap.put("resultCode", -100);
			e.printStackTrace();
		}
		return memberMap;
	}
	
	@RequestMapping(value = "/memberLogin", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> memberLogin(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("/OTPUserCheck");
		
		// 사용자번호 + 비밀번호로 로그인 하는 경우
		Map<String, Object> memberMap = new HashMap<String, Object>();
		try {
			memberMap = service.selectMember(request);
			String result = memberMap.get("resultCode").toString();
			memberMap.put("resultCode", result);
			memberMap.put("resultMessage", memberMap.get("resultMessage"));
		} catch (Exception e) {
			memberMap.put("resultCode", -100);
			e.printStackTrace();
		}
		return memberMap;
	}
}