package com.org.iopts.samsung.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.iopts.detection.service.piApprovalService;
import com.org.iopts.samsung.service.piSamsungService;
import com.org.iopts.service.Pi_UserService;
import com.org.iopts.setting.service.impl.Pi_SetServiceImpl;
import com.org.iopts.util.SessionUtil;

@Controller
@RequestMapping(value = "/samsung")
public class piSamsungContoller {

	private static Logger logger = LoggerFactory.getLogger(piSamsungContoller.class);

	@Inject
	private Pi_UserService userService;

	@Inject
	private piSamsungService service;
	
	@Inject
	private Pi_SetServiceImpl set_service;
	
	private String requestUrl = null;
	private String retrunUrl  = null;
	
//	관리자 로그인
	@RequestMapping(value = "/memberLogin", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> login(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("/managerLogin");

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
		Map<String, Object> noticeMap = userService.selectNotice();
		model.addAttribute("noticeMap", noticeMap);
		model.addAttribute("user_grade", memberMap.get("user_grade"));

		return memberMap;
	}
	
	// SSO 접속시 등급 확인
	@RequestMapping(value = "/SSOGradeCheck", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> SSOGradeCheck(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("/SSOGradeCheck");

		// 사용자번호 + 비밀번호로 로그인 하는 경우
		Map<String, Object> memberMap = new HashMap<String, Object>();
		try {
			memberMap = service.checkMemberGrade(request);
			String result = memberMap.get("resultCode").toString();
			memberMap.put("resultCode", result);
			memberMap.put("resultMessage", memberMap.get("resultMessage"));
		} catch (Exception e) {
			memberMap.put("resultCode", -100);
			memberMap.put("resultMessage", "포탈 인증이 실패했습니다. \n관리자에게 문의하세요.");
			e.printStackTrace();
		}

		return memberMap;
	}
	
//	일반 사용자 SSO 로그인
	@RequestMapping(value = "/SSOLogin", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> SSOLogin(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("/SSOLogin");

		// SSO 자동 로그인(삼성화재)
		Map<String, Object> memberMap = new HashMap<String, Object>();
		try {
			memberMap = service.SSOSelectMember(request);
			String result = memberMap.get("resultCode").toString();
			memberMap.put("resultCode", result);
			memberMap.put("resultMessage", memberMap.get("resultMessage"));
		} catch (Exception e) {
			memberMap.put("resultCode", -100);
			memberMap.put("resultMessage", "시스템오류입니다.\n관리자에게 문의하세요.");
			e.printStackTrace();
		}

		return memberMap;
	}
	
	// 정탐/오탐 리스트
	@RequestMapping(value="/searchProcessList", method={RequestMethod.POST})
	@ResponseBody
	public List<HashMap<String, Object>> searchProcessList(Model model, @RequestParam HashMap<String, Object> params) throws Exception 
	{
		Map<String, Object> member = SessionUtil.getSession("memberSession");
		model.addAttribute("memberInfo", member);

		List<HashMap<String, Object>> searchList = service.searchProcessList(params);

		return searchList;
	}

	// 문서 번호 불러오기
	@RequestMapping(value="/selectDocuNum", method={RequestMethod.POST})
	@ResponseBody 
    public Map<String, Object> selectDocuNum(@RequestParam HashMap<String, Object> params) throws Exception 
	{
		Map<String, Object> docuNum = service.selectDocuNum(params);

		return docuNum;
    }

	
	// 결재 관리 - 정탐/오탐 리스트 - 결재 요청
	@RequestMapping(value="/registProcessCharge", method={RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
    public Map<String, Object> registProcessCharge(@RequestBody HashMap<String, Object> params) throws Exception 
	{
		Map<String, Object> result = new HashMap<String, Object>();
		HashMap<String, Object> chargeMap = new HashMap<String, Object>();

		try {

			if(params.get("approval_flag").equals("1")) { // 정오탐
				chargeMap = service.registProcessCharge(params);
				
				int resultCode = Integer.parseInt(chargeMap.get("resultCode").toString());
				
				if(resultCode != -1) {
					service.updateProcessStatus(params, chargeMap);

					result.put("resultCode", 0);
					result.put("resultMessage", "SUCCESS");
				}
			}else { // 경로 예외
				chargeMap = service.registProcessCharge2(params);
				
				int resultCode = Integer.parseInt(chargeMap.get("resultCode").toString());
				
				if(resultCode != -1) {
//					service.updateProcessStatus(params, chargeMap);

					result.put("resultCode", 0);
					result.put("resultMessage", "SUCCESS");
				}
			}
		}
		catch (Exception e) {
			logger.error("Error: " + e.getMessage(), e);
			result.put("resultCode", -1);
			result.put("resultMessage", "처리중 에러가 발생하였습니다.");

			return result;
		}

		return result;
    }

	@RequestMapping(value="/searchApprovalListData", method={RequestMethod.POST})
	@ResponseBody
	public List<HashMap<String, Object>> searchApprovalListData(Model model, @RequestParam HashMap<String, Object> params) throws Exception 
	{
		Map<String, Object> member = SessionUtil.getSession("memberSession");
		model.addAttribute("memberInfo", member);

		List<HashMap<String, Object>> searchList = service.searchApprovalListData(params);

		return searchList;
	}
	
	@RequestMapping(value="/selectProcessGroupPath", method=RequestMethod.POST, produces="application/json; charset=UTF-8")
	@ResponseBody
	public HashMap<String, Object> selectProcessGroupPath(Model model, @RequestBody HashMap<String, Object> params) throws Exception 
	{
		Map<String, Object> member = SessionUtil.getSession("memberSession");
		model.addAttribute("memberInfo", member);

		List<HashMap<String, Object>> searchList = service.selectProcessGroupPath(params);
		List<HashMap<String, Object>> approvalList = service.selectApprovalUserList(params);
		
		model.addAttribute("searchList", searchList);
		model.addAttribute("approvalList", approvalList);
		
		HashMap<String, Object> resultMap = new HashMap<>();
		
		resultMap.put("searchList", searchList);
		resultMap.put("approvalList", approvalList);
		return resultMap;
	}
	
	@RequestMapping(value = "/pi_search_list", method = {RequestMethod.GET, RequestMethod.POST})
	public String pi_search_list (Model model, HttpServletRequest request) throws Exception 
	{
		requestUrl = "/samsung/pi_search_list";
		 
		try {
			Map<String, Object> pageGrade = set_service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				set_service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				
				List<Map<String,Object>> falseList = set_service.getProcessingFlag(null);
				List<Map<Object, Object>> approvalStatusList = set_service.selectApprovalStatusAll();
				
				model.addAttribute("menuKey", "detectionMenu");
				model.addAttribute("menuItem", "approvalList");
				model.addAttribute("approvalList", falseList); 
				model.addAttribute("approvalStatusList", approvalStatusList); 
				
			}else {
				retrunUrl = "/error/notPageGrade";
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
		}
		
		return retrunUrl;
	}
	
	@RequestMapping(value = "/pi_search_approval_list", method = {RequestMethod.GET, RequestMethod.POST})
	public String pic_search_approval_list (Model model, HttpServletRequest request) throws Exception 
	{
		
		requestUrl = "/samsung/pi_search_approval_list";
		
		try {
			Map<String, Object> pageGrade = set_service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				set_service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				model.addAttribute("menuKey", "detectionMenu2");
				model.addAttribute("menuItem", "approvalList");

				Map<String, Object> member = SessionUtil.getSession("memberSession");
				model.addAttribute("memberInfo", member);
			}else {
				retrunUrl = "/error/notPageGrade";
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
		}
		
		return retrunUrl;
	}
	
	
}