package com.org.iopts.exception.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.iopts.exception.service.piDetectionListService;
import com.org.iopts.group.service.GroupService;
import com.org.iopts.service.Pi_UserService;
import com.org.iopts.util.SessionUtil;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/manage")
public class piDetectionListController {
	
	private static Logger log = LoggerFactory.getLogger(piDetectionListController.class);
	
	@Autowired Pi_UserService userService;
	@Autowired piDetectionListService service;
	
	@Inject
	private GroupService groupService; 

	/*@RequestMapping(value = "/pi_detection_list", method = {RequestMethod.GET, RequestMethod.POST})
	public String pi_detection_list (Locale locale, HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception 
	{
		log.info("관리자화면 - 예외처리관리 - 검출리스트");

		model.addAttribute("menuKey", "exceptionMenu");
		model.addAttribute("menuItem", "detectionList");
		
		List<Map<String, Object>> targetList = service.selectUserTargetList(request);
		model.addAttribute("targetList", targetList);

		Map<String, Object> teamManager = userService.selectTeamManager();
		model.addAttribute("teamManager", teamManager);	

		Map<String, Object> member = SessionUtil.getSession("memberSession");
		model.addAttribute("memberInfo", member);

		log.info("mamber : " + member);

		return "/manage/pi_detection_list2";
	}*/
	
	@RequestMapping(value = "/pi_detection_list2", method = {RequestMethod.GET, RequestMethod.POST})
	public String pi_detection_list (Locale locale, HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception 
	{
		log.info("관리자화면 - 예외처리관리 - 검출리스트");
		
		model.addAttribute("menuKey", "exceptionMenu");
		model.addAttribute("menuItem", "detectionList");
		
//		List<Map<String, Object>> targetList = service.selectUserTargetList(request);
//		model.addAttribute("targetList", targetList);
		
		/*Map<String, Object> teamManager = userService.selectTeamManager();
		model.addAttribute("teamManager", teamManager);*/	
		
		Map<String, Object> member = SessionUtil.getSession("memberSession");
		model.addAttribute("memberInfo", member);
		
		log.info("mamber : " + member);
		
		return "/manage/pi_detection_list";
	}
	
	@RequestMapping(value = "/pi_detection_list", method = {RequestMethod.GET, RequestMethod.POST})
	public String pi_detection_list_rm (Locale locale, HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception 
	{
		log.info("관리자화면 - 예외처리관리 - 검출리스트");
		
		model.addAttribute("menuKey", "exceptionMenu");
		model.addAttribute("menuItem", "detectionList");
		
		
		Map<String, Object> map = new HashMap<>();
		map.put("noGroup", "Y");

		String userGroupList = groupService.selectUserGroupList(map, request);
		model.addAttribute("userGroupList", userGroupList);
		List<Map<String, Object>> patternList = service.queryCustomDataTypes();
		model.addAttribute("pattern", patternList);
		model.addAttribute("patternCnt", patternList.size());
		
		Map<String, Object> member = SessionUtil.getSession("memberSession");
		model.addAttribute("memberInfo", member);
		
		String target_id = request.getParameter("targetid");
		String apno = request.getParameter("apno");
		String host = request.getParameter("host");
		
		model.addAttribute("target_id", target_id);
		model.addAttribute("apno", apno);
		model.addAttribute("host", host);
		
		log.info("mamber : " + member);
		
		return "/manage/pi_detection_list_rm";
	}
	
	@RequestMapping(value = { "/statistics" }, method = RequestMethod.GET)
	public String statistics(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response)
			throws Exception {

		List<Map<String, Object>> patternList = service.queryCustomDataTypes();
		model.addAttribute("pattern", patternList);
		
		return "/manage/statistics";
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
	
	// 검출리스트 하위경로 불러오는 쿼리
	@RequestMapping(value="/subpathSelect", method={RequestMethod.POST})
	@ResponseBody
	public List<HashMap<String, Object>> subpathSelect(@RequestParam HashMap<String, Object> params) throws Exception 
	{
		log.info("subpathSelect");
		List<HashMap<String, Object>> map = service.subpathSelect(params);
		
		return map;
	}
	
	@RequestMapping(value = "/pi_detection_approval_list", method = {RequestMethod.GET, RequestMethod.POST})
	public String pi_detection_approval_list (Locale locale, HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception 
	{
		Map<String, Object> member = SessionUtil.getSession("memberSession");
		model.addAttribute("memberInfo", member);
		String idx = request.getParameter("idx");
		
		model.addAttribute("idx", idx);
		
		return "/manage/pi_detection_approval_list";
	}
	
	// 오탐 처리 
	@RequestMapping(value="/approval_data", method={RequestMethod.POST}, produces="application/json; charset=UTF-8")
	public @ResponseBody List<HashMap<String, Object>> approval_data(@RequestParam HashMap<String, Object> params) throws Exception 
	{
		log.info("approval_data 시작");
		return service.personalApprovalData(params);
	}
}
