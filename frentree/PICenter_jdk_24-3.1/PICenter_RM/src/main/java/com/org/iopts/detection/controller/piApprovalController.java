package com.org.iopts.detection.controller;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.iopts.detection.service.piApprovalService;
import com.org.iopts.service.Pi_UserService;
import com.org.iopts.setting.service.impl.Pi_SetServiceImpl;
import com.org.iopts.util.SessionUtil;

@Controller
@RequestMapping(value = "/approval")
public class piApprovalController {

	private static Logger log = LoggerFactory.getLogger(piApprovalController.class);

	@Inject
	private Pi_UserService userService;

	@Inject
	private piApprovalService service;

	@Inject
	private Pi_SetServiceImpl set_service;
	
	private String requestUrl = null;
	private String retrunUrl  = null;

	/*
	 * 정탐/오탐 리스트
	 */
	@RequestMapping(value = "/pi_search_list", method = {RequestMethod.GET, RequestMethod.POST})
	public String pi_search_list (Model model, HttpServletRequest request) throws Exception 
	{
		requestUrl = "/approval/pi_search_list";
		
		try {
			Map<String, Object> pageGrade = set_service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				set_service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
				
				List<Map<String, Object>> approvalList = set_service.approvalList(request);
				List<Map<Object, Object>> approvalStatusList = set_service.selectApprovalStatusAll();
				List<Map<String, Object>>  approvalGroupCnt = set_service.selectApprovalGroupCnt(request);

				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				model.addAttribute("menuKey", "detectionMenu");
				model.addAttribute("menuItem", "approvalList");
				model.addAttribute("approvalList", approvalList); 
				model.addAttribute("approvalStatusList", approvalStatusList); 
				model.addAttribute("approvalGroupCnt", approvalGroupCnt); 
				
			}else {
				retrunUrl = "/error/notPageGrade";
			}
		} catch (Exception e) {
			log.error("error ::: " + e.getMessage());
			e.printStackTrace();
		}
		
		return retrunUrl;
	}
	
	@RequestMapping(value = "/pi_search_list_skt", method = {RequestMethod.GET, RequestMethod.POST})
	public String pi_search_list_skt (Model model) throws Exception 
	{
		model.addAttribute("menuKey", "detectionMenu");
		model.addAttribute("menuItem", "approvalList");

		Map<String, Object> member = SessionUtil.getSession("memberSession");
		model.addAttribute("memberInfo", member);

		Map<String, Object> teamManager = userService.selectTeamManager();
		model.addAttribute("teamManager", teamManager);	

		return "/detection/pi_search_list_skt";		
	}
	
	/*
	 * 정탐/오탐 결재 리스트
	 */
	@RequestMapping(value = "/pi_search_approval_list2", method = {RequestMethod.GET, RequestMethod.POST})
	public String pi_search_approval_list2 (Model model) throws Exception 
	{
		model.addAttribute("menuKey", "detectionMenu");
		model.addAttribute("menuItem", "approvalList");
		
		Map<String, Object> member = SessionUtil.getSession("memberSession");
		model.addAttribute("memberInfo", member);
		
		return "/detection/pi_search_approval_list2";		
	}
	
	@RequestMapping(value = "/pi_search_approval_list", method = {RequestMethod.GET, RequestMethod.POST})
	public String pic_search_approval_list (Model model, HttpServletRequest request) throws Exception 
	{
		
		requestUrl = "/approval/pi_search_approval_list";
		
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
			log.error("error ::: " + e.getMessage());
		}
		
		return retrunUrl;
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
	

	// 결재자 선택 - user_grade 0 이상
	@RequestMapping(value="/selectTeamMember", method={RequestMethod.POST})
	@ResponseBody
    public List<HashMap<String, Object>> selectTeamMember(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception 
	{
		List<HashMap<String, Object>> teamMemberList = new ArrayList<HashMap<String,Object>>();
		
	//	teamMemberList = service.selectTeamMember(request);

		return teamMemberList;
    }
	
	@RequestMapping(value="/searchTeamMember", method={RequestMethod.POST})
	@ResponseBody
	public List<HashMap<String, Object>> searchTeamMember(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception 
	{
		List<HashMap<String, Object>> teamMemberList = service.searchTeamMember(request);
		
		return teamMemberList;
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
	    public Map<String, Object> registProcessCharge(@RequestBody HashMap<String, Object> params, HttpServletRequest request) throws Exception 
		{
			Map<String, Object> result = new HashMap<String, Object>();
			HashMap<String, Object> chargeMap = new HashMap<String, Object>();

			try {

				if(params.get("approval_flag").equals("1")) { // 정오탐
					chargeMap = service.registProcessCharge(request, params);
					
					int resultCode = Integer.parseInt(chargeMap.get("resultCode").toString());
					
					if(resultCode != -1) {
						service.updateProcessStatus(params, chargeMap);

						result.put("resultCode", 0);
						result.put("resultMessage", "SUCCESS");
					}
				}else { // 경로 예외
					chargeMap = service.registProcessCharge2(request, params);
					
					int resultCode = Integer.parseInt(chargeMap.get("resultCode").toString());
					
					if(resultCode != -1) {
//						service.updateProcessStatus(params, chargeMap);

						result.put("resultCode", 0);
						result.put("resultMessage", "SUCCESS");
					}
				}
			}
			catch (Exception e) {
				log.error("Error: " + e.getMessage(), e);
				result.put("resultCode", -1);
				result.put("resultMessage", "처리중 에러가 발생하였습니다.");

				return result;
			}

			return result;
	    }

	@RequestMapping(value="/selectProcessPath", method={RequestMethod.POST}, produces="application/json; charset=UTF-8")
	@ResponseBody
    public List<HashMap<String, Object>> selectProcessPath(Model model, @RequestBody HashMap<String, Object> params) throws Exception 
	{
		Map<String, Object> member = SessionUtil.getSession("memberSession");
		model.addAttribute("memberInfo", member);

		List<HashMap<String, Object>> searchList = service.selectProcessPath(params);
		model.addAttribute("searchList", searchList);

		return searchList;
    }

	// 정탐/오탐 결재 리스트
	@RequestMapping(value="/searchApprovalAllListData", method={RequestMethod.POST})
	@ResponseBody
	public List<HashMap<String, Object>> searchApprovalAllListData(Model model, @RequestParam HashMap<String, Object> params) throws Exception 
	{
		Map<String, Object> member = SessionUtil.getSession("memberSession");
		model.addAttribute("memberInfo", member);

		List<HashMap<String, Object>> searchList = service.searchApprovalAllListData(params);

		return searchList;
	}

	// 정탐/오탐 결재 리스트
	@RequestMapping(value="/searchApprovalListData", method={RequestMethod.POST})
	@ResponseBody
	public List<HashMap<String, Object>> searchApprovalListData(Model model, @RequestParam HashMap<String, Object> params) throws Exception 
	{
		Map<String, Object> member = SessionUtil.getSession("memberSession");
		model.addAttribute("memberInfo", member);

		List<HashMap<String, Object>> searchList = service.searchApprovalListData(params);

		return searchList; 
	}

	// 정탐/오탐 결재 리스트 - 조회
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
	
	// 정탐/오탐 결재 리스트 - 조회
		@RequestMapping(value="/selectProcessGroupPath2", method=RequestMethod.POST, produces="application/json; charset=UTF-8")
		@ResponseBody
		public HashMap<String, Object> selectProcessGroupPath2(Model model, @RequestBody HashMap<String, Object> params) throws Exception 
		{
			Map<String, Object> member = SessionUtil.getSession("memberSession");
			model.addAttribute("memberInfo", member);

			List<HashMap<String, Object>> searchList = service.selectProcessGroupPath2(params);
			List<HashMap<String, Object>> approvalList = service.selectApprovalUserList(params);
			
			HashMap<String, Object> resultMap = new HashMap<>();
			
			resultMap.put("searchList", searchList);
			resultMap.put("approvalList", approvalList);
			return resultMap;
		}

	// 정탐/오탐 결재 리스트 - 결재
	@RequestMapping(value="/updateProcessApproval", method={RequestMethod.POST}, produces="application/json; charset=UTF-8")
	@ResponseBody
    public Map<String, Object> updateProcessApproval(@RequestBody HashMap<String, Object> params) throws Exception 
	{
		Map<String, Object> result = new HashMap<String, Object>();

		try {
			service.updateProcessApproval(params);
		}
		catch (Exception e) {
			log.error("Error: " + e.getMessage(), e);
			result.put("resultCode", -1);
			result.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return result;
		}

		result.put("resultCode", 0);
		result.put("resultMessage", "SUCCESS");

		return result;
    }

	// 오탐 2차 결재 리스트 - 결재
	@RequestMapping(value="/updateProcessApprovalAdminTwo", method={RequestMethod.POST}, produces="application/json; charset=UTF-8")
	@ResponseBody
    public Map<String, Object> updateProcessApprovalAdminTwo(@RequestBody HashMap<String, Object> params) throws Exception
	{
		Map<String, Object> result = new HashMap<String, Object>();

		try {
			service.updateProcessApprovalAdminTwo(params);
		}
		catch (Exception e) {
			log.error("Error: " + e.getMessage(), e);
			result.put("resultCode", -1);
			result.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return result;
		}

		result.put("resultCode", 0);
		result.put("resultMessage", "SUCCESS");

		return result;
    }
	
	// 정탐/오탐 결재 리스트 - 재검색 스캔정보
	@RequestMapping(value="/selectScanPolicy", method=RequestMethod.POST, produces="application/json; charset=UTF-8")
	@ResponseBody
	public List<HashMap<String, Object>> selectScanPolicy() throws Exception 
	{
		List<HashMap<String, Object>> searchList = service.selectScanPolicy();

		return searchList;
	}

	// 정탐/오탐 결재 리스트 - 재검색 선택 Target 정보
	@RequestMapping(value="/selectReScanTarget", method=RequestMethod.POST, produces="application/json; charset=UTF-8")
	@ResponseBody
	public List<HashMap<String, Object>> selectReScanTarget(@RequestBody HashMap<String, Object> params) throws Exception 
	{
		List<HashMap<String, Object>> searchList = service.selectReScanTarget(params);

		return searchList;
	}
	
	// 결재 요청전 항목 삭제
	@RequestMapping(value = "/deleteItem", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteItem (@RequestBody HashMap<String, Object> params) throws Exception
	{
		log.info("deleteItem");
		Map<String, Object> result = new HashMap<String, Object>();

		try {
			service.deleteItem(params);
		}
		catch (Exception e) {
			log.error("Error: " + e.getMessage(), e);
			result.put("resultCode", -1);
			result.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return result;
		}

		result.put("resultCode", 0);
		result.put("resultMessage", "SUCCESS");

		return result;
	}

}