package com.org.iopts.controller;

import java.util.ArrayList;
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

import com.org.iopts.service.Pi_UserService;
import com.org.iopts.util.SessionUtil;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Handles requests for the application home page.
 */

@Controller
@RequestMapping(value = "/user")
public class UserController {

	private static Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Inject
	private Pi_UserService service;
	
	@RequestMapping(value = "/pi_notice_main", method = RequestMethod.GET)
	public String pi_user_notice(Locale locale, Model model) throws Exception {
		logger.info("pi_notice_main");
		Map<String, Object> noticeMap = new HashMap<String, Object>();
		return "user/pi_notice_main";
	}
	
	@RequestMapping(value = "/pi_license_main", method = RequestMethod.GET)
	public String pi_license_main(Locale locale, Model model) throws Exception {
		logger.info("pi_license_main");
		
		Map<String, Object> resultMap = new HashMap<>();
//		resultMap = service.getLicenseDetail();
		model.addAttribute("menuKey", "userMenu");
		model.addAttribute("menuItem", "licenseMain");
		/*model.addAttribute("resultCode", resultMap.get("resultCode").toString());
		
		if("00".equals(resultMap.get("resultCode").toString())) {
			model.addAttribute("company", resultMap.get("company"));
			model.addAttribute("expire", resultMap.get("expire"));
			model.addAttribute("summaryList", resultMap.get("summaryList"));
			model.addAttribute("targetList", resultMap.get("targetList"));
		}*/
		return "user/pi_license_main";
	}
	
	@RequestMapping(value="/pi_userlog_list", method={RequestMethod.POST})
    public @ResponseBody List<Map<String, Object>> pi_userlog_list(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("pi_userlog_list");
		List<Map<String, Object>> userlogList = service.selectUserLogList(request);
		
		return userlogList;
    }
	
	@RequestMapping(value="/selectManagerList", method={RequestMethod.POST})
    public @ResponseBody List<Map<String, Object>> selectManagerList(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {

		logger.info("selectManagerList");
		List<Map<String, Object>> userlogList = service.selectManagerList(request);
		
		return userlogList;
    }
	
	@RequestMapping(value="/selectCreateUser", method={RequestMethod.POST})
	public @ResponseBody List<Map<String, Object>> selectCreateUser(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
	    logger.info("selectCreateUser");
	    List<Map<String, Object>> resultList = service.selectCreateUser(request);

	    if (!resultList.isEmpty() && resultList.get(0).containsKey("ERROR")) {
	        String errorMessage = (String) resultList.get(0).get("ERROR");
	        
	        if ("로그인 정보가 없습니다.".equals(errorMessage)) {
	            response.setStatus(401);
	        } else if ("관리자 권한이 필요합니다.".equals(errorMessage)) {
	            response.setStatus(403);
	        } else {
	            response.setStatus(400);
	        }
	    }
	    
	    return resultList;
	}
	
	
	@RequestMapping(value="/userDelete", method={RequestMethod.POST})
	public @ResponseBody Map<String, Object> userDelete(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("userDelete");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		boolean error = false;
		try {
			service.userDelete(request);	
		} catch (DataAccessException e) {
			map.put("resultCode", -1);
			map.put("resultMessage", e.getMessage());
			logger.error("Failed to delete user setting data", e);
			//return map;
			error = true;
		} catch (Exception e) {
			map.put("resultCode", -1);
			map.put("resultMessage", e.getMessage());
			logger.error("Failed to delete user setting data", e);
			//return map;
			error = true;
		}
		if(error) return map;
		
		map.put("resultCode", 0);
		map.put("resultMessage", "SUCCESS");
		return map;
	}
	
	
	@RequestMapping(value="/pwd_reset", method={RequestMethod.POST})
	public @ResponseBody Map<String, Object> pwd_reset(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("pwd_reset");
		
		Map<String, Object> map = new HashMap<String, Object>();

		boolean error = false;
		try {
			service.pwd_reset(request);	
		} catch (DataAccessException e) {
			map.put("resultCode", -1);
			map.put("resultMessage", e.getMessage());
			logger.error("Failed to password reset", e);
			error = true;
		} catch (Exception e) {
			map.put("resultCode", -1);
			map.put("resultMessage", e.getMessage());
			logger.error("Failed to password reset", e);
			error = true;
		}
		if(error) return map;
		
		map.put("resultCode", 0);
		map.put("resultMessage", "SUCCESS");
		return map;
	}
	
	
	

	@RequestMapping(value="/chkDuplicateUserNo", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> chkDuplicateUserNo(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {

		logger.info("chkDuplicateUserNo");
		Map<String, Object> dupUserNo = service.chkDuplicateUserNo(request);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultCode", 0);
		map.put("resultMessage", "SUCCESS");
		map.put("UserMap", dupUserNo);
		
		return map;
    }

	@RequestMapping(value="/createUser", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> createUser(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("createUser");

		Map<String, Object> map = new HashMap<String, Object>();
		boolean error = false;
		try {
			service.createUser(request);	
		}
		catch (DataAccessException e) {
			map.put("resultCode", -1);
			map.put("resultMessage", e.getMessage());
			logger.error("Failed to create user", e);
			error = true;
			//return map;
		}
		catch (Exception e) {
			map.put("resultCode", -1);
			map.put("resultMessage", e.getMessage());
			logger.error("Failed to create user", e);
			error = true;
			//return map;
		}
		if(error) return map;

		map.put("resultCode", 0);
		map.put("resultMessage", "SUCCESS");
		return map;
    }
	
	// 사용자 정보 수정(개인)
	@RequestMapping(value="/userSetting", method={RequestMethod.POST})
	public @ResponseBody Map<String, Object> userSetting(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("userSetting");
		
		String userNo = "";
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(SessionUtil.getSession("memberSession") != null) {
			userNo =  SessionUtil.getSession("memberSession", "USER_NO");
			
		}else {
			map.put("resultCode", -1);
			map.put("resultMessage","MemberSession Null");
			return map;
		}
		
		if("frentree".equals(userNo)) {
			map.put("resultCode", 12);
			map.put("resultMessage", "계정 변경 금지 계정");
			return map;
		}else {
			boolean error = false;
			try {
				map = service.changeUserSettingData(request);
			} catch (DataAccessException e) {
				map.put("resultCode", -1);
				map.put("resultMessage", e.getMessage());
				logger.error("Failed to change user setting data", e);
				error = true;
			} catch (Exception e) {
				map.put("resultCode", -1);
				map.put("resultMessage", e.getMessage());
				logger.error("Failed to change user setting data", e);
				error = true;
			}
			if(error) return map;
		}
		
		return map;
	}
	
	@RequestMapping(value = "/pi_license", method = RequestMethod.GET)
	public String pi_license(Locale locale, Model model) throws Exception {
		logger.info("pi_license");
		
		List<Map<String, Object>> resultMap = new ArrayList<>();
		model.addAttribute("menuKey", "userMenu");
		model.addAttribute("menuItem", "licenseMain");
		
		try {
			resultMap = service.selectLicense();
			
		/*	String licenseList = service.selectLicense();*/
			model.addAttribute("licenseData", resultMap);
			model.addAttribute("licenseSize", resultMap.size());
		} catch (DataAccessException e) {
			logger.error("Failed to select license data", e);
		} catch (Exception e) {
			logger.error("Failed to select license data", e);
		}
		
		return "user/pi_license";
	}
	
	@RequestMapping(value = "/selectLicense", method = RequestMethod.POST)
	public List<Map<String, Object>> selectLicense(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("selectLicense");
		
		List<Map<String, Object>> resultMap = new ArrayList<>();
		
		try {
			resultMap = service.selectLicense();
		} catch (DataAccessException e) {
			logger.info("error :: "+e.toString());
		} catch (Exception e) {
			logger.info("error :: "+e.toString());
		}
		
		return resultMap;
	}
	

	@RequestMapping(value = "/selectAccountPolicy", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> selectAccountPolicy(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("selectAccountPolicy");
		
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			resultMap = service.selectAccountPolicy();
		} catch (DataAccessException e) {
			logger.info("error :: "+e.toString());
		} catch (Exception e) {
			logger.error("Failed to select account policy", e);
		}
		
		return resultMap;
	} 
	
	@RequestMapping(value = "/saveAccountPolicy", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveAccountPolicy(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("saveAccountPolicy");
		
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			resultMap = service.saveAccountPolicy(request);
		} catch (DataAccessException e) {
			logger.error("Failed to save account policy", e);
		} catch (Exception e) {
			logger.error("Failed to save account policy", e);
		}
		
		return resultMap;
	} 
	
	
}
