package com.org.iopts.remediation.controller;

import java.util.HashMap;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.iopts.remediation.service.RemediationService;

@Controller
@RequestMapping("/remediation")
public class RemediationController {

	private static Logger log = LoggerFactory.getLogger(RemediationController.class);

	@Inject
	private RemediationService remediationService;

	/**
	 * 조치 이력 페이지
	 */
	@RequestMapping(value = "/pi_remediation_history", method = RequestMethod.GET)
	public String remediationHistoryPage(Model model) {
		log.info("remediationHistoryPage");
		return "remediation/pi_remediation_history";
	}

	/**
	 * 조치 이력 목록 조회 (DETAIL 테이블 직접 조회)
	 */
	@RequestMapping(value = "/remediationHistory", method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> remediationHistory(HttpServletRequest request) throws Exception {
		log.info("remediationHistory");

		HashMap<String, Object> result = new HashMap<>();

		try {
			// 파라미터 수집
			HashMap<String, Object> params = new HashMap<>();
			params.put("action", request.getParameter("action"));
			params.put("target_name", request.getParameter("target_name"));
			params.put("user_name", request.getParameter("user_name"));
			params.put("status", request.getParameter("status"));
			params.put("fromDate", request.getParameter("fromDate"));
			params.put("toDate", request.getParameter("toDate"));

			log.info("params :: " + params);

			// 데이터 조회
			List<HashMap<String, Object>> list = remediationService.selectRemediationHistory(params);

			// jqGrid 형식으로 반환
			result.put("rows", list);
			result.put("records", list.size());

		} catch (Exception e) {
			log.error("조치 이력 조회 실패", e);
			result.put("rows", null);
			result.put("records", 0);
		}

		return result;
	}
}