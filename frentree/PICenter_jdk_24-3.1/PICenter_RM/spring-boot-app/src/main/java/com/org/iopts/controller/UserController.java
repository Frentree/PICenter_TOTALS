package com.org.iopts.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.iopts.service.Pi_UserService;
import com.org.iopts.util.SessionUtil;
import com.org.iopts.setting.service.impl.Pi_SetServiceImpl;

/**
 * Handles requests for the application home page.
 */

@Controller
@RequestMapping(value = "/user")
public class UserController {

	private static Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Inject
	private Pi_UserService service;
	
	@Inject
	private Pi_SetServiceImpl set_service;
	
	private String requestUrl = null;
	private String retrunUrl  = null;
	
	@RequestMapping(value = "/pi_user_main", method = RequestMethod.GET)
	public String pi_user_main(Locale locale, Model model, HttpServletRequest request) throws Exception {
		
		requestUrl = "/user/pi_user_main";
		try {
			Map<String, Object> pageGrade = set_service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				set_service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
				
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				model.addAttribute("menuKey", "userMenu");
				model.addAttribute("menuItem", "userMain");
				
				String accessIP = service.selectAccessIP();
				model.addAttribute("accessIP", accessIP);
				
				List<Map<String, Object>> userGradeList = service.selectUserGradeList();
				model.addAttribute("userGradeList", userGradeList);
				
				
				List<Map<String, Object>> teamMap = service.selectTeamCode();
				model.addAttribute("teamMap", teamMap);
				
				Calendar cal = new GregorianCalendar(Locale.KOREA);
				SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
				String fromDate = fm.format(cal.getTime());
				
				cal.setTime(new Date());
				cal.add(Calendar.MONTH, +1);	     
				String toDate = fm.format(cal.getTime());
				
				model.addAttribute("fromDate", fromDate);
				model.addAttribute("toDate", toDate);
			}else {
				retrunUrl = "/error/notPageGrade";
			}
			
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
		}
		
		return retrunUrl;
	}
	
	@RequestMapping(value = "/pi_file_down", method = RequestMethod.GET)
	public String pi_file_down(Locale locale, Model model, HttpServletRequest request) throws Exception {
		
		requestUrl = "/user/pi_file_down";
		try {
			Map<String, Object> pageGrade = set_service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				set_service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				
				Calendar cal = new GregorianCalendar(Locale.KOREA);
			    SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
			    String fromDate = fm.format(cal.getTime());

			    cal.setTime(new Date());
			    cal.add(Calendar.MONTH, +1);	     
			    String toDate = fm.format(cal.getTime());

				model.addAttribute("fromDate", fromDate);
				model.addAttribute("toDate", toDate);
				
			}else {
				retrunUrl = "/error/notPageGrade";
			}
			
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
		}
				
		return retrunUrl;
	}
	
	@RequestMapping(value = "/pi_user_lockdown", method = RequestMethod.GET)
	public String pi_user_lockdown(Locale locale, Model model, HttpServletRequest request) throws Exception {

		logger.info("pi_user_lockdown");
		
		requestUrl = "/user/pi_user_lockdown";
		
		try {
			Map<String, Object> pageGrade = set_service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				set_service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
				
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				model.addAttribute("menuKey", "userMenu");
				model.addAttribute("menuItem", "userMain");
				
				String accessIP = service.selectAccessIP();
				model.addAttribute("accessIP", accessIP);
				
				List<Map<String, Object>> teamMap = service.selectTeamCode();
				model.addAttribute("teamMap", teamMap);
				
				Calendar cal = new GregorianCalendar(Locale.KOREA);
			    SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
			    String fromDate = fm.format(cal.getTime());

			    cal.setTime(new Date());
			    cal.add(Calendar.MONTH, +1);	     
			    String toDate = fm.format(cal.getTime());

				model.addAttribute("fromDate", fromDate);
				model.addAttribute("toDate", toDate);
			}else {
				retrunUrl = "/error/notPageGrade";
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
		}
		
		return retrunUrl;
	}

	@RequestMapping(value = "/pi_userlog_main", method = RequestMethod.GET)
	public String pi_userlog(Locale locale, Model model, HttpServletRequest request) throws Exception {

		logger.info("pi_userlog_main");
		
		requestUrl = "/user/pi_userlog_main";
		
		try {
			Map<String, Object> pageGrade = set_service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				set_service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());

				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				model.addAttribute("menuKey", "userMenu");
				model.addAttribute("menuItem", "userLog");

				Calendar cal = new GregorianCalendar(Locale.KOREA);
			    SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
			    String toDate = fm.format(cal.getTime());

			    cal.setTime(new Date());
			    cal.add(Calendar.MONTH, -1);	     
			    String fromDate = fm.format(cal.getTime());

				model.addAttribute("fromDate", fromDate);
				model.addAttribute("toDate", toDate);
				
				List<Map<String, Object>> log_flag_list = new ArrayList<Map<String,Object>>();
				List<Map<String, Object>> grade_flag_list = new ArrayList<Map<String,Object>>();
				log_flag_list = service.getLogFlagList();
				grade_flag_list = service.getGradeFlagList();
				model.addAttribute("log_flag_list", log_flag_list);
				model.addAttribute("grade_flag_list", grade_flag_list);
			}else {
				retrunUrl = "/error/notPageGrade";
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
		}
		
		return retrunUrl;
	}
	
	@RequestMapping(value = "/pi_notice_main", method = RequestMethod.GET)
	public String pi_user_notice(Locale locale, Model model, HttpServletRequest request) throws Exception {
		logger.info("pi_notice_main");
		
		requestUrl = "/user/pi_notice_main";
		
		try {
			Map<String, Object> pageGrade = set_service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				set_service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
				Map<String, Object> noticeMap = new HashMap<String, Object>();
				noticeMap = service.selectNotice();
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				model.addAttribute("Notice", noticeMap);
				model.addAttribute("menuKey", "userMenu");
				model.addAttribute("menuItem", "noticeMain");
			}else {
				retrunUrl = "/error/notPageGrade";
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
		}
		
		return retrunUrl;
	}
	
	// 공지사항 목록 조회
	@RequestMapping(value = "/noticeList", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> noticeList(HttpServletRequest request, Model model){
		logger.info("noticeList");
		
		List<Map<String, Object>> resultMap = null;
		try {
			resultMap = service.noticeList(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	// 공지사항 검색 조회
	@RequestMapping(value = "/noticeSearchList", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> noticeSearchList(HttpServletRequest request, Model model){
		logger.info("noticeSearchList");
		
		List<Map<String, Object>> resultList = new ArrayList<>();
		
		try {
			resultList = service.noticeSearchList(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultList;
		
	}

//	공지사항 대시보드 팝업
	@RequestMapping(value="/noticeAlert", method={RequestMethod.POST})
	public @ResponseBody Map<String, Object> noticeAlert(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("noticeAlert");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			service.noticeAlert(request);
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "SUCCESS");
		}
		catch(Exception e) {
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "시스템오류입니다.</br>관리자에게 문의하세요.");
			e.printStackTrace();
		}
		
		return resultMap;
	}
	
	// 공지사항 등록
	@RequestMapping(value="/noticeInsert", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> noticeInsert(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("noticeInsert");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = service.noticeInsert(request);
		}
		catch(Exception e) {
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "시스템오류입니다. \n관리자에게 문의하세요.");
			e.printStackTrace();
		}
		
		return resultMap;
    }
	
	// 공지사항 수정
	@RequestMapping(value="/noticeUpdate", method={RequestMethod.POST})
	public @ResponseBody Map<String, Object> noticeUpdate(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("noticeUpdate");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = service.noticeUpdate(request);
		}
		catch(Exception e) {
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "시스템오류입니다. \n관리자에게 문의하세요.");
			e.printStackTrace();
		}
		
		return resultMap;
	}
	
	// 공지사항 수정
	@RequestMapping(value="/noticeDelete", method={RequestMethod.POST})
	public @ResponseBody Map<String, Object> noticeDelete(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("noticeDelete");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = service.noticeDelete(request);
		}
		catch(Exception e) {
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "시스템오류입니다. \n관리자에게 문의하세요.");
			e.printStackTrace();
		}
		
		return resultMap;
	}
	
	@RequestMapping(value = "/pi_license_main", method = RequestMethod.GET)
	public String pi_license_main(Locale locale, Model model, HttpServletRequest request) throws Exception {
		logger.info("pi_license_main");
		
		requestUrl = "/user/pi_license_main";
		
		try {
			Map<String, Object> pageGrade = set_service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				set_service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
				
				List<Map<String, Object>> resultMap = new ArrayList<>();
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				model.addAttribute("menuKey", "userMenu");
				model.addAttribute("menuItem", "licenseMain");
				
				resultMap = service.selectLicense();
				
				model.addAttribute("licenseData", resultMap);
				model.addAttribute("licenseSize", resultMap.size());
				
			}else {
				retrunUrl = "/error/notPageGrade";
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
		}
		
		return retrunUrl;
	}
	
	@RequestMapping(value="/getLicenseDetail", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> getLicenseDetail(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("getLicenseDetail");
		
//		logger.info("ap_num : " + request.getParameter("ap_num"));
		Map<String, Object> resultMap = service.getLicenseDetail(request);
		
		return resultMap;
    }
	
	@RequestMapping(value="/changeNotice", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> changeNotice(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("changeNotice");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			service.changeNotice(request);
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "SUCCESS");
		}
		catch(Exception e) {
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "시스템오류입니다. \n관리자에게 문의하세요.");
			e.printStackTrace();
		}
		
		return resultMap;
    }
	
	@RequestMapping(value="/pi_userlog_list", method={RequestMethod.POST})
    public @ResponseBody List<Map<String, Object>> pi_userlog_list(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("pi_userlog_list");
		List<Map<String, Object>> userlogList = service.selectUserLogList(request);
		
		return userlogList;
    }
	
	@RequestMapping(value = "/changeAccessIP", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> changeAccessIP(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap  = service.changeAccessIP(request);
		}
		catch(Exception e) {
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "시스템오류입니다. \n관리자에게 문의하세요.");
			e.printStackTrace();
		}

		
		
		return resultMap;
	}

	@RequestMapping(value="/selectManagerList", method={RequestMethod.POST})
    public @ResponseBody List<Map<String, Object>> selectManagerList(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {

		logger.info("selectManagerList");
		List<Map<String, Object>> userlogList = service.selectManagerList(request);
		
		return userlogList;
    }
	
	@RequestMapping(value="/selectLockManagerList", method={RequestMethod.POST})
	public @ResponseBody List<Map<String, Object>> selectLockManagerList(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		
		logger.info("selectLockManagerList");
		List<Map<String, Object>> userlogList = service.selectLockManagerList(request);
		
		return userlogList;
	}

	@RequestMapping(value="/selectTeamMember", method={RequestMethod.POST})
    public @ResponseBody List<Map<String, Object>> selectTeamMember(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {

		logger.info("selectTeamMember");
		List<Map<String, Object>> teamMemberList = service.selectTeamMember(request);
		
		return teamMemberList;
    }
	
	@RequestMapping(value="/createTeam", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> createTeam(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("createTeam");

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			service.createTeam(request);	
		}
		catch (Exception e) {
			logger.error("Error: " + e.getMessage(), e);
			map.put("resultCode", -1);
			map.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return map;
		}

		map.put("resultCode", 0);
		map.put("resultMessage", "SUCCESS");
		return map;
    }

	@RequestMapping(value="/changeManagerList", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> changeManagerList(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("changeManagerList");

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = service.changeManagerList(request);
		}
		catch (Exception e) {
			logger.error("Error: " + e.getMessage(), e);
			map.put("resultCode", -1);
			map.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return map;
		}
		return map;
    }

	@RequestMapping(value="/changeUserData", method={RequestMethod.POST})
	public @ResponseBody Map<String, Object> changeUserData(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("changeUserData");

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = service.changeUserData(request);
		}
		catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			map.put("resultCode", -1);
			map.put("resultMessage", "ERROR");
			return map;
		}

		return map;
	}

	// 사용자 비밀번호 초기화
	@RequestMapping(value="/managerResetPwd", method={RequestMethod.POST})
	public @ResponseBody Map<String, Object> managerResetPwd(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("managerResetPwd");

		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			resultMap =  service.managerResetPwd(request);
		}
		catch (Exception e) {
			logger.error("Error: " + e.getMessage(), e);
			map.put("resultCode", -1);
			map.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return map;
		}

		map.put("resultCode", resultMap.get("resultCode"));
		map.put("resultMessage", resultMap.get("resultMessage"));
		return map;
	}

	@RequestMapping(value="/userLock", method={RequestMethod.POST})
	public @ResponseBody Map<String, Object> userLock(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("userLock");
		
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			service.userLock(request);
		}
		catch (Exception e) {
			logger.error("Error: " + e.getMessage(), e);
			map.put("resultCode", -1);
			map.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return map;
		}
		
		map.put("resultCode", 0);
		map.put("resultMessage", "SUCCESS");
		return map;
	}
	
	@RequestMapping(value="/userDelete", method={RequestMethod.POST})
	public @ResponseBody Map<String, Object> userDelete(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("userDelete");

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			service.userDelete(request);
		}
		catch (Exception e) {
			logger.error("Error: " + e.getMessage(), e);
			map.put("resultCode", -1);
			map.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return map;
		}

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
		try {
			service.createUser(request);	
		}
		catch (Exception e) {
			logger.error("Error: " + e.getMessage(), e);
			map.put("resultCode", -1);
			map.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return map;
		}

		map.put("resultCode", 0);
		map.put("resultMessage", "SUCCESS");
		return map;
    }

	@RequestMapping(value="/unlockAccount", method={RequestMethod.POST})
	public @ResponseBody Map<String, Object> unlockAccount(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("unlockAccount");
		
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			service.unlockAccount(request);	
		}
		catch (Exception e) {
			logger.error("Error: " + e.getMessage(), e);
			map.put("resultCode", -1);
			map.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return map;
		}

		map.put("resultCode", 0);
		map.put("resultMessage", "SUCCESS");
		return map;
	}

	@RequestMapping(value = "/pi_download_main", method = RequestMethod.GET)
	public String pi_user_download(Locale locale, Model model, HttpServletRequest request) throws Exception {
		logger.info("pi_download_main");
		
		requestUrl = "/user/pi_download_main";
		
		try {
			Map<String, Object> pageGrade = set_service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				set_service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
				
				Map<String, Object> downloadMap = new HashMap<String, Object>();
				downloadMap = service.selectDownload();
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				model.addAttribute("Notice", downloadMap);
				model.addAttribute("menuKey", "userMenu");
				model.addAttribute("menuItem", "downloadMap");
				
			}else {
				retrunUrl = "/error/notPageGrade";
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
		}
		
		return retrunUrl;
	}
	
	@RequestMapping(value = "/downloadList", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> downloadList(HttpServletRequest request, Model model){
		logger.info("downloadList");
		
		List<Map<String, Object>> resultMap = null;
		try {
			resultMap = service.downloadList(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/downloadSearchList", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> downloadSearchList(HttpServletRequest request, Model model){
		logger.info("downloadSearchList");
		
		List<Map<String, Object>> resultList = new ArrayList<>();
		
		try {
			resultList = service.downloadSearchList(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultList;
		
	}
	
	@RequestMapping(value="/downloadInsert", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> downloadInsert(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("downloadInsert");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = service.downloadInsert(request);
		}
		catch(Exception e) {
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "시스템오류입니다. \n관리자에게 문의하세요.");
			e.printStackTrace();
		}
		
		return resultMap;
    }
	
	@RequestMapping(value="/downloadUpdate", method={RequestMethod.POST})
	public @ResponseBody Map<String, Object> downloadUpdate(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("downloadUpdate");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = service.downloadUpdate(request);
		}
		catch(Exception e) {
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "시스템오류입니다. \n관리자에게 문의하세요.");
			e.printStackTrace();
		}
		
		return resultMap;
	}
	
	@RequestMapping(value="/downloadDelete", method={RequestMethod.POST})
	public @ResponseBody Map<String, Object> downloadDelete(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("downloadDelete");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = service.downloadDelete(request);
		}
		catch(Exception e) {
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "시스템오류입니다. \n관리자에게 문의하세요.");
			e.printStackTrace();
		}
		
		return resultMap;
	}

	// 사용자 정보 수정(개인)
	@RequestMapping(value="/userSetting", method={RequestMethod.POST})
	public @ResponseBody Map<String, Object> userSetting(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("userSetting");
		
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = service.changeUserSettingData(request);	
		}
		catch (Exception e) {
			map.put("resultCode", -1);
			map.put("resultMessage", "정보 수정 실패");
			logger.info(e.getMessage());
			return map;
		}
		
		return map;
	}
	
	// 사용자 등록 직급 조회
	@RequestMapping(value = "/selectMemberTeam", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> selectMemberTeam(HttpServletRequest request, Model model){
		logger.info("selectMemberTeam");
		
		List<Map<String, Object>> resultList = new ArrayList<>();
		
		try {
			resultList = service.selectMemberTeam(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultList;
		
	}
	
	// PC 중간관리자 검색
	@RequestMapping(value = "/selectPCAdmin", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> selectPCAdmin(HttpServletRequest request, Model model){
		logger.info("selectPCAdmin");
		
		List<Map<String, Object>> resultList = new ArrayList<>();
		
		try {
			resultList = service.selectPCAdmin(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultList;
		
	}
	
	@RequestMapping(value="/SMSFlag", method={RequestMethod.POST})
	public @ResponseBody Map<String, Object> selectSMSFlag() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String result = service.selectSMSFlag();
			map.put("result", result);
		}
		catch (Exception e) {
			logger.error("Error: " + e.getMessage(), e);
			map.put("resultCode", -1);
			map.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return map;
		}

		map.put("resultCode", 0);
		map.put("resultMessage", "SUCCESS");
		return map;
	}

	@RequestMapping(value="/updateSMSFlag", method={RequestMethod.POST})
	public @ResponseBody Map<String, Object> updateSMSFlag(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			service.updateSMSFlag(request);
			String result = service.selectSMSFlag();
			logger.info("result >> " + result);
			if(result.equals("Y")) {
				resultMap.put("resultCode", 0);
				resultMap.put("resultMessage", "SMS인증이 활성화 되었습니다.");
			}else if(result.equals("N")) {
				resultMap.put("resultCode", 1);
				resultMap.put("resultMessage", "SMS인증이 비활성화 되었습니다.");
			}
		}
		catch(Exception e) {
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "시스템오류입니다. \n관리자에게 문의하세요.");
			e.printStackTrace();
		}
		
		return resultMap;
	}
	
//	@RequestMapping(value="/updateBatchSchedule", method={RequestMethod.POST})
//	public @ResponseBody Map<String, Object> updateBatchSchedule(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
//		logger.info("updateBatchSchedule");
//		
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		try {
//			resultMap = service.updateBatchSchedule(request);
//		}
//		catch(Exception e) {
//			resultMap.put("resultCode", -100);
//			resultMap.put("resultMessage", "시스템오류입니다. \n관리자에게 문의하세요.");
//			e.printStackTrace();
//		}
//		
//		return resultMap;
//	}
	
	@RequestMapping(value = "/selectAccountPolicy", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> selectAccountPolicy(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("selectAccountPolicy");
		
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			resultMap = service.selectAccountPolicy();
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		
		return resultMap;
	} 
	
	@RequestMapping(value = "/saveAccountPolicy", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveAccountPolicy(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("saveAccountPolicy");
		
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			resultMap = service.saveAccountPolicy(session, request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultMap;
	} 
	
}
