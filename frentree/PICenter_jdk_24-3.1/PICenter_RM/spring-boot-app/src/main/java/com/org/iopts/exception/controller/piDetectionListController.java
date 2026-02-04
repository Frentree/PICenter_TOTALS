package com.org.iopts.exception.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.org.iopts.detection.service.piDetectionService;
import com.org.iopts.exception.service.piDetectionListService;
import com.org.iopts.group.service.GroupService;
import com.org.iopts.mockup.dao.MockupDAO;
import com.org.iopts.service.Pi_TargetService;
import com.org.iopts.service.Pi_UserService;
import com.org.iopts.setting.service.impl.Pi_SetServiceImpl;
import com.org.iopts.util.SessionUtil;

import com.google.gson.JsonArray;
@Controller
@RequestMapping(value = "/manage")
public class piDetectionListController {
	
	private static Logger log = LoggerFactory.getLogger(piDetectionListController.class);
	
	@Autowired Pi_UserService userService;
	@Autowired piDetectionListService service;

	@Inject
	private GroupService groupService;

	@Inject
	private Pi_SetServiceImpl set_service;

	@Autowired
	private MockupDAO mockupDAO;
	
	private String requestUrl = null;
	private String retrunUrl  = null;

	@RequestMapping(value = "/pi_detection_list", method = {RequestMethod.GET, RequestMethod.POST})
	public String pic_detection_list (Locale locale, HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception 
	{
		log.info("관리자화면 - 예외처리관리 - 검출리스트");
		
		requestUrl = "/manage/pi_detection_list";
		
		try {
			Map<String, Object> pageGrade = set_service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				set_service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				model.addAttribute("menuKey", "exceptionMenu");
				model.addAttribute("menuItem", "detectionList");
				
				String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
				Map<String, Object> map = new HashMap<>();
				map.put("noGroup", "Y");
				Gson gson = new Gson();

				try {
					String userGroupList = groupService.selectPICenterServer(map, request);
					log.info("userGroupList ::: " + userGroupList);
					model.addAttribute("userGroupList", userGroupList);
//					List<Map<String, Object>> flagList = set_service.getProcessingFlag("2");
//			        model.addAttribute("flagList",flagList);
					
			        List<Map<String, Object>> patternList = service.queryCustomDetailDataTypes();
			        model.addAttribute("pattern", patternList);
			        model.addAttribute("patternCnt", patternList.size());

			        // 서버 컬럼 정보 조회 (관리자, 운영자 등 동적 컬럼)
			        List<Map<String, Object>> serverColumns = mockupDAO.getServerColumns();
			        model.addAttribute("serverColumns", serverColumns);
			        model.addAttribute("serverColumnsJson", new Gson().toJson(serverColumns));     
			        
			        if(user_grade.equals("9") || user_grade.equals("5")) {
			        	List<Map<Object, Object>> setMap = set_service.selectSetting(2);
			        	model.addAttribute("setMap", setMap);
			        }
			        
					
				}catch (Exception e) {
					log.error(e.toString());
				}
				
				Map<String, Object> member = SessionUtil.getSession("memberSession");
				model.addAttribute("memberInfo", member);
				
				String target_id = request.getParameter("targetid");
				String apno = request.getParameter("apno");
				String host = request.getParameter("host");
				
				model.addAttribute("target_id", target_id);
				model.addAttribute("apno", apno);
				model.addAttribute("host", host);
				
				/* 조치계획 요청*/
				List<Map<String,Object>> falseList = set_service.getProcessingFlag("false");
				List<Map<String,Object>> trueList = set_service.getProcessingFlag("true");
				List<Map<String,Object>> exceptionList = set_service.getProcessingFlag("exception");
				
				JsonArray jsonArray = new JsonArray();
				
				model.addAttribute("falseList", falseList);
				model.addAttribute("jsonFalseList", new Gson().toJsonTree(falseList).getAsJsonArray());
				model.addAttribute("trueList", trueList);
				model.addAttribute("jsonTrueList", new Gson().toJsonTree(trueList).getAsJsonArray());
				model.addAttribute("exceptionList", exceptionList);
				model.addAttribute("jsonExceptionList", new Gson().toJsonTree(exceptionList).getAsJsonArray());			
			}else {
				retrunUrl = "/error/notPageGrade";
			}
		} catch (Exception e) {
			log.error("error ::: " + e.getMessage());
		}
		
		return retrunUrl;
	}
	
	@RequestMapping(value = { "/statistics" }, method = RequestMethod.GET)
	public String statistics(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		requestUrl = "/manage/statistics";
		
		try {
			Map<String, Object> pageGrade = set_service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				set_service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
				
				List<Map<String, Object>> patternList = service.queryCustomDataTypes();
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				model.addAttribute("pattern", patternList);
				model.addAttribute("patternCnt", patternList.size());
				
				
				
			}else {
				retrunUrl = "/error/notPageGrade";
			}
		} catch (Exception e) {
			log.error("error ::: " + e.getMessage());
		}
		
		return retrunUrl;
	}

	
	// 검출리스트 내용 불러오는 쿼리
	@RequestMapping(value="/selectUserTargetList", method={RequestMethod.POST})
    public @ResponseBody List<Map<String, Object>> selectUserTargetList(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception 
	{
		List<Map<String, Object>> targetList = service.selectUserTargetList(request);
		
		return targetList;
    }
	
	// 검출리스트 내용 불러오는 쿼리
	@RequestMapping(value="/selectFindSubpath", method={RequestMethod.POST})
	@ResponseBody
    public List<HashMap<String, Object>> selectFindSubpath(@RequestParam HashMap<String, Object> params) throws Exception 
	{
		log.info("selectFindSubpath");

		List<HashMap<String, Object>> findSubpathList = service.selectFindSubpath(params);

		return findSubpathList;
    }
	
	// 그룹 선택 시 검출 리스트 조회
	@RequestMapping(value="/selectGroupFindSubpath", method={RequestMethod.POST})
	@ResponseBody
	public List<HashMap<String, Object>> selectGroupFindSubpath(@RequestBody HashMap<String, Object> params) throws Exception 
	{
		log.info("selectGroupFindSubpath");
		
		List<HashMap<String, Object>> findSubpathList = service.selectGroupFindSubpath(params);
		
		return findSubpathList;
	}
	
	// PIcenter 그룹 선택 시 검출 리스트 조회
	@RequestMapping(value="/selectPICenterGroupFindSubpath", method={RequestMethod.POST})
	@ResponseBody
	public List<HashMap<String, Object>> selectPICenterGroupFindSubpath(@RequestBody HashMap<String, Object> params) throws Exception 
	{
		log.info("selectGroupFindSubpath");
		
		List<HashMap<String, Object>> findSubpathList = service.selectPICenterGroupFindSubpath(params);
		
		return findSubpathList;
	}
	
	// 검출리스트 하위경로 불러오는 쿼리
	@RequestMapping(value="/subpathSelect", method={RequestMethod.POST})
	@ResponseBody
	public List<HashMap<String, Object>> subpathSelect(@RequestParam HashMap<String, Object> params) throws Exception 
	{
		log.info("subpathSelect");
		List<HashMap<String, Object>> map = service.subpathSelect(params);
		
		return map;
	}
	
	// 오탐 처리 
	@RequestMapping(value="/registProcess", method={RequestMethod.POST}, produces="application/json; charset=UTF-8")
	public @ResponseBody HashMap<String, Object> registProcess(@RequestBody HashMap<String, Object> params) throws Exception 
	{
		log.info("registProcess 시작");
		log.info("params :: " + params.toString());
		
		HashMap<String, Object> GroupMap = new HashMap<String, Object>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		try {
			map = service.selectProcessDocuNum(params);
			params.put("group_id", map.get("SEQ"));
			log.info("map :: " + map.toString());
			
			GroupMap = service.registProcessGroup(params);
			service.registProcess(params, GroupMap);
			map.put("resultCode", "0");
			map.put("resultMessage", "SUCCESS");
			
		} catch (Exception e) {
			log.error("Error: " + e.getMessage(), e);
			map.put("resultCode", "-1");
			map.put("resultMessage", "처리중 에러가 발생하였습니다.");
		}

		return map;
	}

	// 오탐 해제 처리
	@RequestMapping(value="/cancelApproval", method={RequestMethod.POST}, produces="application/json; charset=UTF-8")
	public @ResponseBody HashMap<String, Object> cancelApproval(@RequestBody HashMap<String, Object> params) throws Exception
	{
		log.info("cancelApproval 시작");
		log.info("params :: " + params.toString());
		log.info(""+params.get("deletionList").getClass());

		HashMap<String, Object> map = new HashMap<String, Object>();

		try {
			service.cancelApproval(params);
			map.put("resultCode", "0");
			map.put("resultMessage", "SUCCESS");
		} catch (Exception e) {
			log.error("Error: " + e.getMessage(), e);
			map.put("resultCode", "-1");
			map.put("resultMessage", "처리중 에러가 발생하였습니다.");
		}

		return map;
	}
	
	@RequestMapping(value = "/pi_detection_approval_list", method = {RequestMethod.GET, RequestMethod.POST})
	public String pi_detection_approval_list (Locale locale, HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception 
	{
		requestUrl = "/manage/pi_detection_approval_list";
		
		try {
			Map<String, Object> pageGrade = set_service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				set_service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
				
				Map<String, Object> member = SessionUtil.getSession("memberSession");
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				model.addAttribute("memberInfo", member);
				String idx = request.getParameter("idx");
				
				model.addAttribute("idx", idx);
				
			}else {
				retrunUrl = "/error/notPageGrade";
			}
		} catch (Exception e) {
			log.error("error ::: " + e.getMessage());
		}
		
		return retrunUrl;
	}
	
	@RequestMapping(value="/selectDetectionApprovalList", method={RequestMethod.POST})
	@ResponseBody
    public List<HashMap<String, Object>> selectDetectionApprovalList(@RequestParam HashMap<String, Object> params) throws Exception 
	{
		log.info("selectDetectionApprovalList");

		List<HashMap<String, Object>> DetectionApprovalList = service.selectDetectionApprovalList(params);

		return DetectionApprovalList;
    }
	
	@RequestMapping(value = "/getDetectionApprovalList", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> getDetectionApprovalList(HttpServletRequest request, Model model)
	{
		log.info("getDetectionApprovalList");
		
		List<Map<String, Object>> resultList = new ArrayList<>();
		
		try {
			resultList = service.getDetectionApprovalList(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultList;
	}
	
	// 오탐 처리 
	@RequestMapping(value="/approval_data", method={RequestMethod.POST}, produces="application/json; charset=UTF-8")
	public @ResponseBody List<HashMap<String, Object>> approval_data(@RequestParam HashMap<String, Object> params) throws Exception 
	{
		log.info("approval_data 시작");
		return service.personalApprovalData(params);
	}
	

	@RequestMapping(value="/registRemdiation", method={RequestMethod.POST}, produces="application/json; charset=UTF-8")
	public @ResponseBody HashMap<String, Object> registAdminFalseMatch(HttpServletRequest request, @RequestBody HashMap<String, Object> params) throws Exception {
	    log.info("registAdminFalseMatch 시작");
	    log.info("params :: " + params.toString());

	    HashMap<String, Object> map = new HashMap<String, Object>();

	    try {
	        String flag = params.get("flag").toString();

	        // flag에 따라 다른 메서드 호출
	        if("quarantine".equals(flag)) {
	            service.registQuarantine(request, params);
	        } else {
	            // delete, encrypt는 기존 로직
	            service.registRemdiation(request, params);
	        }

	        map.put("resultCode", "0");
	        map.put("resultMessage", "SUCCESS");

	    } catch (Exception e) {
	        log.error("처리 등록 실패: " + e.getMessage(), e);
	        map.put("resultCode", "-1");
	        map.put("resultMessage", "처리중 에러가 발생하였습니다.");
	    }

	    return map;
	}
}
