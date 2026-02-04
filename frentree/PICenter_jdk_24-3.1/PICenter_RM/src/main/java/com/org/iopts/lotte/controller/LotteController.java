package com.org.iopts.lotte.controller;

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
import com.org.iopts.lotte.service.LotteService;

@Controller
@RequestMapping(value = "/lotte")
public class LotteController {

	private static Logger logger = LoggerFactory.getLogger(LotteController.class);

	@Inject
	private LotteService lotteService;
	
	@RequestMapping(value = "/memberLogin", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> login(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("/managerLogin");

		// 사용자번호 + 비밀번호로 로그인 하는 경우
		Map<String, Object> memberMap = new HashMap<String, Object>();
		try {
			memberMap = lotteService.selectMember(request);
			String result = memberMap.get("resultCode").toString();
			memberMap.put("resultCode", result);
			memberMap.put("resultMessage", memberMap.get("resultMessage"));
			
		} catch (Exception e) {
			memberMap.put("resultCode", -100);
//			e.printStackTrace();
		}
		model.addAttribute("user_grade", memberMap.get("user_grade"));

		return memberMap;
	}

}
