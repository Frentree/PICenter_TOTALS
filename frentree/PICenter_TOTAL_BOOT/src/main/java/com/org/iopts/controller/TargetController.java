package com.org.iopts.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.iopts.group.service.GroupService;
import com.org.iopts.service.Pi_TargetService;
import com.org.iopts.util.SessionUtil;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Handles requests for the application home page.
 */

@Controller
@RequestMapping(value = "/target")
public class TargetController {

	private static Logger logger = LoggerFactory.getLogger(TargetController.class);
	
	@Inject
	private Pi_TargetService targetservice;
	
	@Inject
	private GroupService groupService;
//	@RequestMapping(value = "/pi_target_mngr2", method = RequestMethod.GET)
//	public String pi_target_mngr2(Locale locale, Model model) throws Exception {
//		
//		logger.info("pi_target_mngr");
//		
//		model.addAttribute("menuKey", "targetMenu");
//		model.addAttribute("menuItem", "targetMgr");		
//		
//		return "target/pi_target_mngr2";
//	}

	@RequestMapping(value = "/pi_target_mngr", method = RequestMethod.GET)
	public String pi_target_mngr(Locale locale, Model model, HttpServletRequest request) throws Exception {
		String user_grade = "0";
		logger.info("pi_target_mngr");
		
		model.addAttribute("menuKey", "targetMenu");
		model.addAttribute("menuItem", "targetMgr");
		
		Map<String, Object> map = new HashMap<>();
		/*map.put("noGroup", "Y");
		
		List<Map<String, Object>> groupList = targetservice.selectGroupList(map);
		model.addAttribute("groupList", groupList);
		
		List<Map<String, Object>> userGroupList = targetservice.selectUserGroupList(map);
		model.addAttribute("userGroupList", userGroupList);
		
		List<Map<String, Object>> noticeList = targetservice.selectNoticeList(map);
		model.addAttribute("noticeList", noticeList);

		List<Map<String, Object>> noGroupList = targetservice.getTargetList(map);
		logger.info("noGroupSize :: " + noGroupList.size());
		model.addAttribute("noGroupSize", noGroupList.size());*/
		
		try {
			//JSONArray targetGroup = groupService.selectTomsGroup(map, request);
			String userGroupList = groupService.selectUserGroupList(map, request);
			
			user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
			
			model.addAttribute("userGroupList", userGroupList);
			model.addAttribute("userGrade", user_grade);
		}catch (RuntimeException e) {
			logger.error("Failed to retrieve user group list or session data", e.toString());
		}
		 
		return "target/pi_target_mngr";
	}

	@RequestMapping(value = "/pi_target_assign", method = RequestMethod.GET)
	public String pi_target_assign(Locale locale, Model model) throws Exception {

		logger.info("pi_target_assign");
		model.addAttribute("menuKey", "targetMenu");
		model.addAttribute("menuItem", "targetAssign");		
		
		Map<String, Object> map = new HashMap<>();
		map.put("noGroup", "Y");
		
		List<Map<String, Object>> groupList = targetservice.selectGroupList(map);
		model.addAttribute("groupList", groupList);
		
		List<Map<String, Object>> noGroupList = targetservice.getTargetList(map);
		logger.info("noGroupSize :: " + noGroupList.size());
		model.addAttribute("noGroupSize", noGroupList.size());

		return "target/pi_target_assign";
	}
	
	@RequestMapping(value = "/pi_target_dmz", method = RequestMethod.GET)
	public String pi_target_dmz(Locale locale, Model model) throws Exception {

		logger.info("pi_target_assign");
		model.addAttribute("menuKey", "targetMenu");
		model.addAttribute("menuItem", "targetDmz");		

		return "target/pi_target_dmz";
	}
	
	@RequestMapping(value = "/pi_target_skt_manager", method = RequestMethod.GET)
	public String pi_target_skt_manager(Locale locale, Model model, HttpServletRequest request) throws Exception {
		
		logger.info("pi_target_skt_manager");
		
		return "target/pi_target_skt_manager";
	}
	
	@RequestMapping(value = "/pi_target_pc_mngr", method = RequestMethod.GET)
	public String pi_target_pc_mngr(Locale locale, Model model, HttpServletRequest request) throws Exception {
		
		return "target/pi_target_pc_mngr";
	}

	@RequestMapping(value="/pi_target_list", method={RequestMethod.POST})
    public @ResponseBody List<Map<String, Object>> pi_target_list(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("pi_target_list");
		
		List<Map<String, Object>> targetList = targetservice.selectTargetList(request);
		
		return targetList;
    }

	@RequestMapping(value="/selectTargetUser", method={RequestMethod.POST})
    public @ResponseBody List<Map<String, Object>> selectTargetUser(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("selectTargetUser");
		
		List<Map<String, Object>> targetList = targetservice.selectTargetUser(request);
		
		return targetList;
    }

	@RequestMapping(value="/selectUserTargetList", method={RequestMethod.POST})
    public @ResponseBody List<Map<String, Object>> selectUserTargetList(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		List<Map<String, Object>> targetList = targetservice.selectUserTargetList(request);
		
		return targetList;
    }
	
	@RequestMapping(value="/selectServerList", method={RequestMethod.POST})
    public @ResponseBody List<Map<String, Object>> selectServerList(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		List<Map<String, Object>> targetList = targetservice.selectServerList(request);
		
		return targetList;
    }

	@RequestMapping(value="/selectTargetUserList", method={RequestMethod.POST})
    public @ResponseBody List<Map<String, Object>> selectTargetUserList(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		List<Map<String, Object>> targetList = targetservice.selectTargetUserList(request);
		
		return targetList;
    }
	
	@RequestMapping(value = "/selectServerFileTopN", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> selectServerFileTopN(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) {
		List<Map<String, Object>> targeList = targetservice.selectServerFileTopN(request);
		
		return targeList;
	}
	
	@RequestMapping(value = "/selectAdminServerFileTopN", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> selectAdminServerFileTopN(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) {
		List<Map<String, Object>> targeList = targetservice.selectAdminServerFileTopN(request);
		
		return targeList;
	}
	
	@RequestMapping(value = "/selectRmTargetList", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> selectRmTargetList(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) {
		
		List<Map<String, Object>> targeList = targetservice.selectRmTargetList(request);
		
		return targeList;
	}
	@RequestMapping(value = "/selectPCTargetUser", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> selectPCTargetUser(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) {
		
		List<Map<String, Object>> targeList = targetservice.selectPCTargetUser(request);
		
		return targeList;
	}
	/**
	 * DMZ List Delete
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @throws Exception
	 */
	

	@SuppressWarnings("finally")
	@RequestMapping(value = "/getGroupDetails", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getGroupDetails(HttpServletRequest request, Model model){
		
		logger.info("getGroupDetails");
		
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			String idx = request.getParameter("idx");
			
			Map<String, Object> map = new HashMap<>();
			map.put("idx", idx);
			
			logger.info(map.toString());
			Map<String, Object> targetList  = targetservice.getGroupDetails(map);
			
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "SUCCESS");
			resultMap.put("resultData", targetList);
			
		} catch (DataAccessException e){
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", e.getMessage());
		} catch (Exception e){
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", e.getMessage());
		} finally {
			return resultMap;
		}
		
	}
	
	@RequestMapping(value = "/getExceptionList", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> getExceptionList(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response)  throws Exception{
		logger.info("getExceptionList ");
		
		List<Map<String, Object>> targeList = targetservice.getExceptionList(request);
		
		return targeList;
	}
	
	
	@RequestMapping(value="/selectSKTManagerList", method={RequestMethod.POST})
    public @ResponseBody List<Map<String, Object>> selectSKTManagerList(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		List<Map<String, Object>> targetList = targetservice.selectSKTManagerList(request);
		
		logger.info("selectSKTManagerList");
		
		return targetList;
    }
	
	@RequestMapping(value="/selectAddSKTManagerList", method={RequestMethod.POST})
    public @ResponseBody List<Map<String, Object>> selectAddSKTManagerList(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		List<Map<String, Object>> targetList = targetservice.selectAddSKTManagerList(request);
		
		logger.info("selectAddSKTManagerList");
		
		return targetList;
    }
	
	@RequestMapping(value="/selectPcManagerList", method={RequestMethod.POST})
	public @ResponseBody List<Map<String, Object>> selectPcManagerList(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("selectPcManagerList");
		List<Map<String, Object>> targetList = targetservice.selectPcManagerList(request);
		
		return targetList;
	}

	@RequestMapping(value="/selectVersionList", method={RequestMethod.POST})
	public @ResponseBody List<Map<String, Object>> selectVersionList(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("selectVersionList");
		List<Map<String, Object>> targetList = targetservice.selectVersionList(request);
		
		return targetList;
	}
	
	@RequestMapping(value = "/searchPCTargetUser", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> searchPCTargetUser(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) {
		
		logger.info("searchPCTargetUser");
		
		
		List<Map<String, Object>> targeList = targetservice.searchPCTargetUser(request);
		      
		return targeList;
		
	}
	
	
}
