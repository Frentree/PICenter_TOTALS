package com.org.iopts.popup.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.iopts.exception.service.piDetectionListService;
import com.org.iopts.group.service.GroupService;
import com.org.iopts.popup.service.PopupService;
import com.org.iopts.service.Pi_TargetService;
import com.org.iopts.util.SessionUtil;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Handles requests for the application home page.
 */

@Controller
@RequestMapping(value = "/popup")
public class PopupController {

	private static Logger logger = LoggerFactory.getLogger(PopupController.class);
	
	@Inject
	private PopupService service;
	
	@Inject
	private Pi_TargetService targetservice;
	
	@Inject
	private GroupService groupService;
	
	
	@Inject 
	piDetectionListService detectionService;
	
	
	
	/*@RequestMapping(value = "/lowPath", method = {})
	public String lowPath_get(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		logger.info("lowPath");
		logger.info("hash_id :: " + request.getParameter("hash_id"));
		logger.info("tid :: " + request.getParameter("tid"));
		logger.info("ap_no :: " + request.getParameter("ap_no"));
		
//		model.addAttribute("id", request.getParameter("hash_id"));
//		model.addAttribute("ap_no", request.getParameter("ap_no"));
		Map<String, Object> map = new HashMap<>();
		
		model.addAttribute("subPath", groupService.selectSubPath(map, request));
		
		return "popup/lowPath";
	}*/
	
	@RequestMapping(value = "/lowPath", method = {RequestMethod.GET, RequestMethod.POST})
	public String lowPath(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		logger.info("lowPath");
		/*logger.info("hash_id :: " + request.getParameter("hash_id"));
		logger.info("tid :: " + request.getParameter("tid"));
		logger.info("ap_no :: " + request.getParameter("ap_no"));*/
		
		Map<String, Object> map = new HashMap<>();
		
		model.addAttribute("subPath", groupService.selectSubPath(map, request));
		
		return "popup/lowPath";
	}
	
	/*@RequestMapping(value = "/detectionDetail", method = {})
	public String detectionDetail_get(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		logger.info("detectionDetail  1");
		logger.info("hash_id :: " + request.getParameter("id"));
		logger.info("ap_no :: " + request.getParameter("ap_no"));
		
//		model.addAttribute("id", request.getParameter("id"));
//		model.addAttribute("ap_no", request.getParameter("ap_no"));
		
		return "popup/detectionDetail";
	}*/
	
	@RequestMapping(value = "/detectionDetail", method = {RequestMethod.GET,RequestMethod.POST})
	public String detectionDetail(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		logger.info("detectionDetail");
		logger.info("hash_id :: " + request.getParameter("id"));
		logger.info("tid :: " + request.getParameter("tid")); 
		logger.info("ap_no :: " + request.getParameter("ap_no"));
		
		model.addAttribute("id", request.getParameter("id"));
		model.addAttribute("tid", request.getParameter("tid"));
		model.addAttribute("ap_no", request.getParameter("ap_no"));
		

		List<Map<String, Object>> patternList = detectionService.queryMatchDetail();
		model.addAttribute("pattern", patternList);
		
		return "popup/detectionDetail";
	}
	
	@RequestMapping(value = "/targetList", method = {RequestMethod.GET,RequestMethod.POST})
	public String targetList(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		

		Map<String, Object> map = new HashMap<>();
		map.put("noGroup", "Y");

		try {
		    String userGroupList = groupService.selectUserHostGroupList(map, request);
		    model.addAttribute("userGroupList", userGroupList);
		} catch (RuntimeException e) {
		    logger.error("Failed to retrieve user host group list", e);
		    model.addAttribute("userGroupList", "");
		    model.addAttribute("errorMessage", "사용자 그룹 목록 조회 중 오류가 발생했습니다.");
		}

		Map<String, Object> member = SessionUtil.getSession("memberSession");
		model.addAttribute("memberInfo", member);
		
		
		return "popup/targetList";
	}
	
	@SuppressWarnings("finally")
	@RequestMapping(value = "/getTargetList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getTargetList(HttpServletRequest request, Model model){
		
		logger.info("getTargetList");
		
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			String aut = request.getParameter("aut");
			String host = request.getParameter("host");
			String noGroup = request.getParameter("noGroup");
			
			logger.info("noGroup :: " + noGroup);
			
			Map<String, Object> map = new HashMap<>();
			if("noGroup".equals(noGroup)) {
				map.put("noGroup", "Y");
			} else if("noGroupPC".equals(noGroup)) {
				map.put("noGroup", "P");
			}else {
				if(!"".equals(host) && host != null) {
					map.put("host", host);
				} else {
					map.put("group_id", request.getParameter("group_id"));
				}
			}
			if("manager".equals(aut)) {
				//map.put("group_id", request.getParameter("group_id"));
			}
			if("user".equals(aut)) {
				String user_no = SessionUtil.getSession("memberSession", "USER_NO");
				map.put("user_no", user_no);
			}
			
			logger.info(map.toString());
			List<Map<String, Object>> targetList  = service.getTargetList(map);
			
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "SUCCESS");
			resultMap.put("resultData", targetList);
		} catch (NullPointerException e){
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", e.getMessage());
			logger.error("error :: ",e.toString());
		} catch (Exception e){
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", e.getMessage());
			logger.error("error :: ",e.toString());
		} finally {
			return resultMap;
		}
		
	}
	
	@RequestMapping(value = "groupList", method = {RequestMethod.POST})
	public String groupList(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		logger.info("groupList");
		String refererHeader = request.getHeader("referer");
		String[] referer = refererHeader != null ? refererHeader.split("/") : new String[0];
		String aut = "manage".equals(referer[3])? "manager": "user";
		model.addAttribute("aut", aut);
		
		Map<String, Object> map = new HashMap<>();
		map.put("noGroup", "Y");
		if("manager".equals(aut)) {
			//map.put("group_id", request.getParameter("group_id"));
		}
		if("user".equals(aut)) {
			String user_no = SessionUtil.getSession("memberSession", "USER_NO");
			map.put("user_no", user_no);
		}
		
		List<Map<String, Object>> groupList = targetservice.selectGroupList(map);
		model.addAttribute("groupList", groupList);
		
		List<Map<String, Object>> userGroupList = targetservice.selectUserGroupList(map);
		model.addAttribute("userGroupList", userGroupList);
		
		List<Map<String, Object>> noticeList = targetservice.selectNoticeList(map);
		model.addAttribute("noticeList", noticeList);

		List<Map<String, Object>> noGroupList = targetservice.getTargetList(map);
		logger.info("noGroupSize :: " + noGroupList.size());
		model.addAttribute("noGroupSize", noGroupList.size());
		
		return "popup/groupList";
	}
	

	@RequestMapping(value = "policyList", method = {RequestMethod.GET, RequestMethod.POST})
	public String policyList(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		logger.info("policyList");
		
		/*Map<String, Object> map = new HashMap<>();
		map.put("noGroup", "Y");
		if("manager".equals(aut)) {
		
			//map.put("group_id", request.getParameter("group_id"));
		}
		if("user".equals(aut)) {
			String user_no = SessionUtil.getSession("memberSession", "USER_NO");
			map.put("user_no", user_no);
		}
		
		List<Map<String, Object>> groupList = service.selectGroupList(map);
		model.addAttribute("groupList", groupList);
		
		
		List<Map<String, Object>> noGroupList = service.getTargetList(map);
		model.addAttribute("noGroupSize", noGroupList.size());*/
		
		return "popup/policyList";
	} 
	
	@RequestMapping(value = "/userList", method = {RequestMethod.GET, RequestMethod.POST})
	public String userList(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		logger.info("userList");
		/*String[] referer = request.getHeader("referer").split("\\/");
		String aut = "manage".equals(referer[3])? "manager": "user";
		model.addAttribute("aut", aut);*/
		Map<String, Object> member = SessionUtil.getSession("memberSession");
		String info = request.getParameter("info");
		model.addAttribute("memberInfo", member);
		model.addAttribute("info", info);
		
		
		return "popup/userList";
	}
	
	@RequestMapping(value = "/reportGroupList", method = {RequestMethod.GET,RequestMethod.POST})
	public String reportGroupList(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String typeChk = request.getParameter("typeChk");

		Map<String, Object> map = new HashMap<>();
		map.put("noGroup", "Y");
		map.put("typeChk", typeChk);

		try {
			String userReportGroupList = groupService.userReportGroupList(map, request);
			model.addAttribute("userReportGroupList", userReportGroupList);
		}catch (RuntimeException e) {
			logger.error("error :: "+e.getMessage());
			//model.addAttribute("userReportGroupList", "");
		}

		Map<String, Object> member = SessionUtil.getSession("memberSession");
		model.addAttribute("memberInfo", member);
		
		return "popup/reportGroupList";
	}
	
	@RequestMapping(value = "/reportHostList", method = {RequestMethod.GET,RequestMethod.POST})
	public String reportHostList(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String typeChk = request.getParameter("typeChk");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("noGroup", "Y");
		map.put("typeChk", typeChk);

		try {
			String userReportHostList = groupService.userReportHostList(map, request);
			model.addAttribute("userReportHostList", userReportHostList);
		} catch (RuntimeException e) {
		    logger.error("Failed to retrieve user report host list", e);
		    model.addAttribute("userReportHostList", ""); // 대체 값 설정
		}
		Map<String, Object> member = SessionUtil.getSession("memberSession");
		model.addAttribute("memberInfo", member);
		
		return "popup/reportHostList";
	}
	
	
	@RequestMapping(value = "/helpDetail", method = {RequestMethod.GET, RequestMethod.POST})
	public String helpDetail(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		logger.info("helpDetail");
		String id = request.getParameter("id");
		model.addAttribute("id", id);

		return "popup/helpDetail";
	}
	
}
