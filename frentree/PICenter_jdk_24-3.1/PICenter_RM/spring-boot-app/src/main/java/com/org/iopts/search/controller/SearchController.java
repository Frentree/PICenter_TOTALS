package com.org.iopts.search.controller;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.iopts.exception.service.piDetectionListService;
import com.org.iopts.group.service.GroupService;
import com.org.iopts.search.service.SearchService;
import com.org.iopts.service.Pi_TargetService;
import com.org.iopts.service.Pi_UserService;
import com.org.iopts.setting.service.impl.Pi_SetServiceImpl;
import com.org.iopts.util.SessionUtil;

@Controller
@RequestMapping(value = "/search")
@Configuration
@PropertySource("classpath:/property/config.properties")
public class SearchController {

	private static Logger logger = LoggerFactory.getLogger(SearchController.class);

	@Value("${recon.api.version}")
	private String api_ver;
	
	@Inject
	private SearchService searchService;
	
	@Inject
	private Pi_TargetService targetservice;
	
	@Inject
	private piDetectionListService detectionListService;
	
	@Inject
	private Pi_SetServiceImpl set_service;
	
	@Inject
	private GroupService groupService;
	
	@Inject
	private Pi_UserService userService;
	
	private String requestUrl = null;
	private String retrunUrl  = null;

	
	@RequestMapping(value = "/getProfile", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> getProfile(HttpServletRequest request, Model model){
		logger.info("getProfile");
		
		List<Map<String, Object>> resultMap = null;
		try {
			resultMap = searchService.getProfile(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultMap;
		
	}
	
	@RequestMapping(value = "/policy", method = {RequestMethod.GET, RequestMethod.POST})
	public String pi_search_list (Model model) throws Exception 
	{
		logger.info("policy page");
		Map<String, Object> member = SessionUtil.getSession("memberSession");
		model.addAttribute("memberInfo", member);
		
		model.addAttribute("menuKey", "search");
		model.addAttribute("menuItem", "reportAppr");
		return "/search/policy";
	}
	
	@RequestMapping(value = "/data_type", method = {RequestMethod.GET, RequestMethod.POST})
	public String data_type (Model model, HttpServletRequest request) throws Exception 
	{
		logger.info("data_type page");
		
		requestUrl = "/search/data_type";
		
		try {
			Map<String, Object> pageGrade = set_service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				set_service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				Map<String, Object> member = SessionUtil.getSession("memberSession");
				model.addAttribute("memberInfo", member);
				
				List<Map<String, Object>> patternList = detectionListService.queryCustomDataTypes();
				model.addAttribute("pattern", patternList);
				model.addAttribute("patternCnt", patternList.size());
				
				model.addAttribute("menuKey", "search");
				model.addAttribute("menuItem", "reportAppr");
			}else {
				retrunUrl = "/error/notPageGrade";
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
		}
		
		return retrunUrl;
	}
	
	@RequestMapping(value = "/insertProfile", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> insertProfile(HttpServletRequest request, Model model){
		logger.info("insertProfile");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = searchService.insertProfile(request);
		}
		catch (Exception e) {
			resultMap.put("resultCode", -1);
			logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return resultMap;
		}
		
		return resultMap;
		
	}
	
	@RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> updateProfile(HttpServletRequest request, Model model){
		logger.info("updateProfile");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = searchService.updateProfile(request);
		}
		catch (Exception e) {
			resultMap.put("resultCode", -1);
			logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return resultMap;
		}
		
		return resultMap;
		
	}
	
	@RequestMapping(value = "/deleteProfile", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> deleteProfile(HttpServletRequest request, Model model){
		logger.info("deleteProfile");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = searchService.deleteProfile(request);
		}
		catch (Exception e) {
			resultMap.put("resultCode", -1);
			logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return resultMap;
		}
		
		return resultMap;
		
	}
	
	@RequestMapping(value = "/search_list", method = {RequestMethod.GET, RequestMethod.POST})
	public String search_list (Model model, HttpServletRequest request) throws Exception 
	{
		logger.info("search_list page");
		
		requestUrl = "/search/search_list";
		
		try {
			Map<String, Object> pageGrade = set_service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				set_service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				Calendar cal = new GregorianCalendar(Locale.KOREA);
			    SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
			    String curDate = fm.format(cal.getTime());

			    cal.setTime(new Date());
			    cal.add(Calendar.MONTH, -3);	     
			    String befDate = fm.format(cal.getTime());

				model.addAttribute("befDate", befDate);
				model.addAttribute("curDate", curDate);

				model.addAttribute("menuKey", "scanMenu");
				model.addAttribute("menuItem", "scanMgr");
				
				List<Map<String, Object>> patternList = detectionListService.queryCustomDataTypes();
				model.addAttribute("pattern", patternList);
				model.addAttribute("patternCnt", patternList.size());
				
			}else {
				retrunUrl = "/error/notPageGrade";
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
		}
		
		return retrunUrl;
	}
	
	@RequestMapping(value = "/search_day", method = {RequestMethod.GET, RequestMethod.POST})
	public String search_day (Model model, HttpServletRequest request) throws Exception 
	{
		logger.info("search_list page");
		
		requestUrl = "/search/search_day";
		
		try {
			Map<String, Object> pageGrade = set_service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				set_service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				Calendar cal = new GregorianCalendar(Locale.KOREA);
			    SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
			    cal.add(Calendar.DATE, -1);
			    String curDate = fm.format(cal.getTime());

			    cal.setTime(new Date());
			    cal.add(Calendar.DATE, -8);	     
			    String befDate = fm.format(cal.getTime());

				model.addAttribute("befDate", befDate);
				model.addAttribute("curDate", curDate);

				model.addAttribute("menuKey", "scanMenu");
				model.addAttribute("menuItem", "scanMgr");
				
				List<Map<String, Object>> patternList = detectionListService.queryCustomDataTypes();
				model.addAttribute("pattern", patternList);
				model.addAttribute("patternCnt", patternList.size());
				
			}else {
				retrunUrl = "/error/notPageGrade";
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
		}
		logger.info("returnUrl ;; "+retrunUrl);
		return retrunUrl;
	}
	
	@RequestMapping(value = "/search_daylist", method = {RequestMethod.GET, RequestMethod.POST})
	public String search_daylist (Model model, HttpServletRequest request) throws Exception 
	{
		logger.info("search_daylist page");
		
		requestUrl = "/search/search_daylist";
		
		try {
			Map<String, Object> pageGrade = set_service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				set_service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				Map<String, Object> map = new HashMap<>();
				
				String groupList = groupService.selectPICenterServer(map, request); 
				model.addAttribute("groupList", groupList); 
				
			}else {
				retrunUrl = "/error/notPageGrade";
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
		}
		logger.info("returnUrl ;; "+retrunUrl);
		return retrunUrl;
	}
	
	@RequestMapping(value = "/monthGrpahData", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> monthGrpahData(HttpServletRequest request, Model model){
		logger.info("monthGrpahData");
		
		Map<String, Object> resultMap = null;
		try {
			resultMap = searchService.monthGrpahData(request);
		} catch (Exception e) {
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "월 별 검색 사용량 조회 실패");
			e.printStackTrace();
			
			return resultMap;
		}
		
		return resultMap;
		
	}
	
	
	@RequestMapping(value = "/serverGrpahData", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> serverGrpahData(HttpServletRequest request, Model model){
		logger.info("serverGrpahData");
		
		Map<String, Object> resultMap = null;
		try {
			resultMap = searchService.serverGrpahData(request);
		} catch (Exception e) {
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "검색 사용량 조회 실패");
			e.printStackTrace();
			
			return resultMap;
		}
		
		return resultMap;
		
	}
	
	@RequestMapping(value = "/serverGrpahData2", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> serverGrpahData2(HttpServletRequest request, Model model){
		logger.info("serverGrpahData2");
		
		Map<String, Object> resultMap = null;
		try {
			resultMap = searchService.serverGrpahData2(request);
		} catch (Exception e) {
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "검색 사용량 조회 실패");
			e.printStackTrace();
			
			return resultMap;
		}
		
		return resultMap;
		
	}
	
	@RequestMapping(value = "/groupGrpahData", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> groupGrpahData(HttpServletRequest request, Model model){
		logger.info("groupGrpahData");
		
		Map<String, Object> resultMap = null;
		try {
			resultMap = searchService.groupGrpahData(request);
		} catch (Exception e) {
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "검색 사용량 조회 실패");
			e.printStackTrace();
			
			return resultMap;
		}
		
		return resultMap;
		
	}
	
	@RequestMapping(value = "/selectDataTypeId", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> selectDataTypeId(HttpServletRequest request, Model model){
		logger.info("selectDataTypeId");
		
		Map<String, Object> resultMap = null;
		try {
			resultMap = searchService.selectDataTypeId(request);
		} catch (Exception e) {
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "스캔 실패");
			e.printStackTrace();
			
			return resultMap;
		}
		
		return resultMap;
		
	}
	
	// schedule 등록 화면 호출
	@RequestMapping(value = "/search_regist", method = RequestMethod.GET)
	public String pi_scan_regist(HttpServletRequest request, Locale locale, Model model) throws Exception {
		logger.info("search_regist");
		
		requestUrl = "/search/search_regist";
		
		try {
			Map<String, Object> pageGrade = set_service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				set_service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				model.addAttribute("menuKey", "scanMenu");
				model.addAttribute("menuItem", "scanMgr");
				
				Map<String, Object> map = new HashMap<>();
				try {
					String serverGroup = groupService.selectPICenterServerDate(map, request);
					
					logger.info("serverGroup >>> " + serverGroup);
					model.addAttribute("serverGroupList", serverGroup);
					
					List<Map<String, Object>> patternList = detectionListService.queryCustomDataTypes();
					model.addAttribute("pattern", patternList);
					model.addAttribute("patternCnt", patternList.size());
					
				}catch (Exception e) {
					logger.error("error ::: " + e.getMessage());
				}
				
				Calendar cal = new GregorianCalendar(Locale.KOREA);
				SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
				String today = fm.format(cal.getTime());
				
				model.addAttribute("today", today);
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
	
	// schedule 등록 화면 호출
	@RequestMapping(value = "/nas_search_regist", method = RequestMethod.GET)
	public String search_NAS(HttpServletRequest request, Locale locale, Model model) throws Exception {
		logger.info("search_NAS");
		
		requestUrl = "/search/nas_search_regist";
		
		try {
			Map<String, Object> pageGrade = set_service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				set_service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				model.addAttribute("menuKey", "scanMenu");
				model.addAttribute("menuItem", "scanMgr");
				
				Map<String, Object> map = new HashMap<>();
				try {
					String serverGroup = groupService.selectNASList(map, request).toString();
					model.addAttribute("serverGroupList", serverGroup);
					
					List<Map<String, Object>> patternList = detectionListService.queryCustomDataTypes();
					model.addAttribute("pattern", patternList);
					model.addAttribute("patternCnt", patternList.size());
					
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				Calendar cal = new GregorianCalendar(Locale.KOREA);
				SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
				String today = fm.format(cal.getTime());
				
				model.addAttribute("today", today);
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
	
	// schedule 등록 화면 호출
	@RequestMapping(value = "/db_search_regist", method = RequestMethod.GET)
	public String pi_db_scan_regist(HttpServletRequest request, Locale locale, Model model) throws Exception {
		logger.info("db_search_regist");
		
		requestUrl = "/search/db_search_regist";
		
		try {
			Map<String, Object> pageGrade = set_service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				set_service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				model.addAttribute("menuKey", "scanMenu");
				model.addAttribute("menuItem", "scanMgr");
				
				Map<String, Object> map = new HashMap<>();
				try {
					String serverGroup = groupService.selectDBGroupList(map, request).toString();
					
					logger.info("serverGroup >>> " + serverGroup);
					model.addAttribute("serverGroupList", serverGroup);
					
					List<Map<String, Object>> patternList = detectionListService.queryCustomDataTypes();
					model.addAttribute("pattern", patternList);
					model.addAttribute("patternCnt", patternList.size());
					
				}catch (Exception e) {
					logger.error("error ::: " + e.getMessage());
				}
				
				Calendar cal = new GregorianCalendar(Locale.KOREA);
				SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
				String today = fm.format(cal.getTime());
				
				model.addAttribute("today", today);
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
	
	@RequestMapping(value="/search_policy", method={RequestMethod.GET})
	public String pi_scan_rescan(Locale locale, Model model, HttpServletRequest request) throws Exception {
		logger.info("search_policy");
		
		requestUrl = "/search/search_policy";
		
		try {
			Map<String, Object> pageGrade = set_service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				set_service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				model.addAttribute("menuKey", "scanMenu");
				model.addAttribute("menuItem", "rescan");
				
				List<Map<String, Object>> patternList = detectionListService.queryCustomDataTypes();
				model.addAttribute("pattern", patternList);
				model.addAttribute("patternCnt", patternList.size());
				
			}else {
				retrunUrl = "/error/notPageGrade";
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
		}
		
		return retrunUrl;
    }
	
	@RequestMapping(value="/search_net", method={RequestMethod.GET, RequestMethod.POST})
	public String search_net(Locale locale, Model model) throws Exception {
		logger.info("search_net");
		
		return "search/search_net";
    }
	
	
	
	@RequestMapping(value = "/getPolicy", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> getPolicy(HttpServletRequest request, Model model){
		logger.info("getPolicy");
		
		List<Map<String, Object>> resultList = new ArrayList<>();
		
		try {
			resultList = searchService.getPolicy(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultList;
		
	}
	
	@RequestMapping(value = "/deletePolicy", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> deletePolicy(HttpServletRequest request, Model model){
		logger.info("deletePolicy");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = searchService.deletePolicy(request);
		}
		catch (Exception e) {
			resultMap.put("resultCode", -1);
			logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return resultMap;
		}
		
		return resultMap;
		
	}
	
	@RequestMapping(value = "/modifyPolicy", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> modifyPolicy(HttpServletRequest request, Model model){
		logger.info("modifyPolicy");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = searchService.modifyPolicy(request);
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "CLEAR");
		}
		catch (Exception e) {
			resultMap.put("resultCode", -1);
			logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return resultMap;
		}
		
		return resultMap;
		
	}
	
	@RequestMapping(value = "/insertPolicy", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> insertPolicy(HttpServletRequest request, Model model){
		logger.info("insertPolicy");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = searchService.insertPolicy(request);
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "CLEAR");
		}
		catch (Exception e) {
			resultMap.put("resultCode", -1);
			logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return resultMap;
		}
		
		return resultMap;
		
	}
	
	@RequestMapping(value = "/search_status", method = {RequestMethod.GET, RequestMethod.POST})
	public String search_status (Model model, HttpServletRequest request) throws Exception 
	{
		logger.info("search_status page");
		
		requestUrl = "/search/search_status";
		
		try {
			Map<String, Object> pageGrade = set_service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				set_service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				Map<String, Object> member = SessionUtil.getSession("memberSession");
				model.addAttribute("memberInfo", member);
				
				Calendar cal = new GregorianCalendar(Locale.KOREA);
			    SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
			    String curDate = fm.format(cal.getTime());

			    cal.setTime(new Date());
			    cal.add(Calendar.MONTH, -1);	     
			    String befDate = fm.format(cal.getTime());

				model.addAttribute("befDate", befDate);
				model.addAttribute("curDate", curDate);

				model.addAttribute("menuKey", "scanMenu");
				model.addAttribute("menuItem", "scanMgr");
				
				List<Map<String, Object>> patternList = detectionListService.queryCustomDataTypes();
				model.addAttribute("pattern", patternList);
				model.addAttribute("patternCnt", patternList.size());
				
				List<Map<Object, Object>> mngrNameList = targetservice.selectMngrNameList();
				model.addAttribute("mngrNameList", mngrNameList);
				
			}else {
				retrunUrl = "/error/notPageGrade";
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
		}
		
		return retrunUrl;
	}
	
	
	@RequestMapping(value = "/getStatusList", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> getStatusList(HttpServletRequest request, Model model){
		logger.info("getStatusList");
		
		List<Map<String, Object>> resultList = new ArrayList<>();
		
		try {
			resultList = searchService.getStatusList(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultList;
	}
	
	@RequestMapping(value = "/getUserList", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> getUserList(HttpServletRequest request, Model model){
		logger.info("getUserList");
		
		List<Map<String, Object>> resultList = new ArrayList<>();
		
		try {
			resultList = searchService.getUserList(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultList;
	}
	
	@RequestMapping(value="/registSchedule", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> registSchedule(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("registSchedule");
		
		Map<String, Object> schedulList = searchService.registSchedule(request, api_ver);
		
//		logger.info(request.getParameter("scheduleArr"));
		return schedulList;
    }
	
	@RequestMapping(value="/registPCSchedule", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> registPCSchedule(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("registPCSchedule");
		
		Map<String, Object> schedulList = searchService.registPCSchedule(request, api_ver);
		
//		logger.info(request.getParameter("scheduleArr"));
		return schedulList;
    }
	
	/*
	 * 검색 그룹 리스트
	 */	
	@RequestMapping(value="/registScheduleGroup", method={RequestMethod.POST})
    public @ResponseBody List<Map<String, Object>> registScheduleGroup(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("registScheduleGroup");
		
		List<Map<String, Object>> schedulList = searchService.registScheduleGroup(request);
		
		return schedulList;
    }
	
	/*
	 * 일자별 검색 현황
	 */	
	@RequestMapping(value="/registDaySchedule", method={RequestMethod.POST})
	public @ResponseBody List<Map<String, Object>> registDaySchedule(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("registDaySchedule");

		List<Map<String, Object>> schedulList = searchService.registDaySchedule(request);
		
		return schedulList;
	}
	
//	스케줄 일시정지 등록
	@RequestMapping(value="/schStopUpdate", method={RequestMethod.POST})
	public @ResponseBody Map<String, Object> schStopUpdate(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("schStopUpdate");
		
		Map<String, Object> schedulMap = new HashMap<>();
		
		try {
			schedulMap = searchService.schStopUpdate(request);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("error ::: " + e.getLocalizedMessage());
			schedulMap.put("resultMessage", "ERROR");
			schedulMap.put("resultCode", -1);
		}
		
		return schedulMap;
	}
	/*
	 * 검색 타겟 리스트
	 */	
	@RequestMapping(value="/registScheduleTargets", method={RequestMethod.POST})
    public @ResponseBody List<Map<String, Object>> registScheduleTargets(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("registScheduleTargets");
		
		List<Map<String, Object>> schedulList = searchService.registScheduleTargets(request);
		
		return schedulList;
    }
	
	
	/*
	 * 검색 상태 변경
	 */	
	@RequestMapping(value="/changeSchedule", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> changeSchedule(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("changeSchedule");
		
		Map<String, Object> schedulList = new HashMap<String, Object>();
		try {
			schedulList = searchService.changeSchedule(request, api_ver);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			schedulList.put("resultCode", -1);
			logger.error("Error: " + e.getMessage(), e);
			schedulList.put("resultMessage", "처리중 에러가 발생하였습니다.");
		}
		
		return schedulList;
    }
	
	/*
	 * 그룹 검색 상태 변경
	 */	
	@RequestMapping(value="/changeScheduleAll", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> changeScheduleAll(@RequestParam(value="tasks[]") List<String> taskList,HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("changeScheduleAll");
		
		Map<String, Object> schedulList = new HashMap<String, Object>();
		try {
			schedulList = searchService.changeScheduleAll(taskList, request, api_ver);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			schedulList.put("resultCode", -1);
			logger.error("Error: " + e.getMessage(), e);
			schedulList.put("resultMessage", "처리중 에러가 발생하였습니다.");
		}
		
		return schedulList;
    }
	
	/*
	 * 그룹 검색 상태 완료로 변경
	 */	
	@RequestMapping(value="/completedSchedule", method={RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody Map<String, Object> completedSchedule(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("completedSchedule");
		
		Map<String, Object> schedulList = new HashMap<String, Object>();
		try {
			schedulList = searchService.completedSchedule(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			schedulList.put("resultCode", -1);
			logger.error("Error: " + e.getMessage(), e);
			schedulList.put("resultMessage", "처리중 에러가 발생하였습니다.");
		}
		
		return schedulList;
    }
	
	/*
	 * 그룹 검색 상태 변경
	 */	
	@RequestMapping(value="/selectScanDataTypes", method={RequestMethod.POST})
    public @ResponseBody List<Map<String, Object>> selectScanDataTypes(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("selectScanDataTypes");
		
		List<Map<String, Object>> schedulList = new ArrayList<>();
		try {
			schedulList = searchService.selectScanDataTypes(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		
		return schedulList;
    }
	
	@RequestMapping(value="/selectSKTScanDataTypes", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> selectSKTScanDataTypes(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("selectSKTScanDataTypes");
		
		Map<String, Object> schedulList = new HashMap<>();
		try {
			schedulList = searchService.selectSKTScanDataTypes(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		
		return schedulList;
    }

	/*
	 * 그룹 검색 상태 변경
	 */	
	@RequestMapping(value="/confirmApply", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> confirmApply(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("confirmApply");
		
		Map<String, Object> schedulList = new HashMap<String, Object>();
		try {
			schedulList = searchService.confirmApply(request, api_ver);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			schedulList.put("resultCode", -1);
			logger.error("Error: " + e.getMessage(), e);
			schedulList.put("resultMessage", "처리중 에러가 발생하였습니다.");
		}
		
		return schedulList;
    }
	
	@RequestMapping(value = "/updateSchedule", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> updateSchedule(HttpServletRequest request, Model model){
		logger.info("updateSchedule");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = searchService.updateSchedule(request);
		}
		catch (Exception e) {
			resultMap.put("resultCode", -1);
			logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return resultMap;
		}
		
		return resultMap;
		
	}
	
	@RequestMapping(value = "/updateReconSchedule", method = RequestMethod.POST)
	public @ResponseBody void updateReconSchedule(HttpServletRequest request, Model model){
		logger.info("updateReconSchedule");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			searchService.updateReconSchedule(request);
		}
		catch (Exception e) {
			resultMap.put("resultCode", -1);
			logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
		}
		
		
	}
	
	@RequestMapping(value = "/selectNetHost", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> selectNetHost(HttpServletRequest request, Model model){
		logger.info("selectNetHost");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> map = searchService.selectNetHost(request);
			resultMap.put("resultData", map);
			resultMap.put("resultDataSize", map.size());

			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "select Net Host Sucess!");
		}
		catch (Exception e) {
			resultMap.put("resultCode", -1);
			logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
		}

		return resultMap;
		
	}
	
	@RequestMapping(value="/getScanDetails", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> getScanDetails(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("getScanDetails");
		
		Map<String, Object> schedulList = searchService.getScanDetails(request, api_ver);
		
		return schedulList;
    }
	
	@RequestMapping(value="/getScanPercentage", method={RequestMethod.POST})
	public @ResponseBody Map<String, Object> getScanPercentage(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("getScanPercentage");
		
		Map<String, Object> schedulList = searchService.getScanPercentage(request, api_ver);
		
		return schedulList;
	}
	
	
	@RequestMapping(value="/insertNetPolicy", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> insertNetPolicy(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("insertNetPolicy");
		
		Map<String, Object> schedulList = searchService.insertNetPolicy(request);
		
//		logger.info(request.getParameter("scheduleArr"));
		return schedulList;
    }
	
	@RequestMapping(value="/insertOneDrivePolicy", method={RequestMethod.POST})
	public @ResponseBody Map<String, Object> insertOneDrivePolicy(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("insertOneDrivePolicy");
		
		Map<String, Object> schedulList = searchService.insertOneDrivePolicy(request);
		
		return schedulList;
	}
	
	@RequestMapping(value="/updateNetPolicy", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> updateNetPolicy(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("updateNetPolicy");
		
		Map<String, Object> schedulList = searchService.updateNetPolicy(request);
		
//		logger.info(request.getParameter("scheduleArr"));
		return schedulList;
    }
	
	@RequestMapping(value="/updateOneDrivePolicy", method={RequestMethod.POST})
	public @ResponseBody Map<String, Object> updateOneDrivePolicy(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("updateOneDrivePolicy");
		
		Map<String, Object> schedulList = searchService.updateOneDrivePolicy(request);
		
//		logger.info(request.getParameter("scheduleArr"));
		return schedulList;
	}
	
	@RequestMapping(value="/selectPCPolicy", method={RequestMethod.POST})
    public @ResponseBody List<Map<String, Object>> selectPCPolicy(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("selectPCPolicy");
		
		List<Map<String, Object>> schedulList = new ArrayList<>();
		try {
			schedulList = searchService.selectPCPolicy(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		
		return schedulList;
    }
	
	@RequestMapping(value="/selectPCPolicyTime", method={RequestMethod.POST})
    public @ResponseBody List<Map<String, Object>> selectPCPolicyTime(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("selectPCPolicyTime");
		
		List<Map<String, Object>> schedulList = new ArrayList<>();
		try {
			schedulList = searchService.selectPCPolicyTime(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		
		return schedulList;
    }
	
	@RequestMapping(value = "/deletePCPolicy", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> deletePCPolicy(HttpServletRequest request, Model model){
		logger.info("deletePCPolicy");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			searchService.deletePCPolicy(request);
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "CLEAR");
		}
		catch (Exception e) {
			resultMap.put("resultCode", -1);
			logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return resultMap;
		}
		
		return resultMap;
		
	}
	
	@RequestMapping(value="/selectPCSearchStatus", method={RequestMethod.POST})
    public @ResponseBody List<Map<String, Object>> selectPCSearchStatus(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("selectPCSearchStatus");
		
		List<Map<String, Object>> schedulList = searchService.selectPCSearchStatus(request);
		
		return schedulList;
    }
	
	
	@RequestMapping(value = "/selectUserSearchCount", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> selectUserSearchCount(HttpServletRequest request, Model model){
		logger.info("selectUserSearchCount");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = searchService.selectUserSearchCount(request);
		}
		catch (Exception e) {
			resultMap.put("resultCode", -1);
			logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return resultMap;
		}
		
		return resultMap;
		
	}
	
	// 네트워크 
	@RequestMapping(value = { "/search_netList" }, method = RequestMethod.GET)
	public String search_netList(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response)
			throws Exception {
		return "/search/search_netList";
	}
	
	
	@RequestMapping(value = "/netList", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> netList(HttpServletRequest request, Model model){
		logger.info("netList Controller");
		
		List<Map<String, Object>> resultMap = null;
		try {
			resultMap = searchService.netList(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultMap;
		
	}
	
	@RequestMapping(value = "/insertNetIp", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> insertNetIp(HttpServletRequest request, Model model){
		logger.info("insertNetIp");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> netIpMap = new HashMap<String, Object>();
		try {
			netIpMap = searchService.insertNetIp(request);
		}
		catch (Exception e) {
			resultMap.put("resultCode", -1);
			logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return resultMap;
		}
		
		return netIpMap;
		
	}
	
	@RequestMapping(value = "/updateNetIp", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> updateNetIp(HttpServletRequest request, Model model){
		logger.info("updateNetIp");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> netIpMap = new HashMap<String, Object>();
		try {
			netIpMap = searchService.updateNetIp(request);
		}
		catch (Exception e) {
			resultMap.put("resultCode", -1);
			logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return resultMap;
		}
		
		return netIpMap;
		
	}
	
	@RequestMapping(value = "/deleteNetIp", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> deleteNetIp(HttpServletRequest request, Model model){
		logger.info("deleteNetIp");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			searchService.deleteNetIp(request);
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "네트워크 삭제 성공");
		}
		catch (Exception e) {
			resultMap.put("resultCode", -1);
			logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return resultMap;
		}
		
		return resultMap;
		
	}
	
	@RequestMapping(value = "/insertNetListIp", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody Map<String,Object> insertNetListIp(HttpServletRequest request, Model model, HttpServletResponse response,  @RequestParam HashMap<String, Object> params){
            logger.info("insertNetListIp");

            Map<String, Object> map = new HashMap<String, Object>();
            Map<String,Object> result = new HashMap<String,Object>();
            int resultCode = 0;
            int resultValue = 0;
            
			try {
				resultValue = searchService.insertNetListIp(params, request);
				result = searchService.insertNetList(params, request);
			        
			}
			catch (Exception e) {
				e.printStackTrace();
				
				result.put("resultValue", resultValue);
				result.put("resultCode", -1);
				result.put("resultMessage", "insert 실패");
			}
            
            result.put("resultValue", resultValue);

            return result;

    }
	
	@RequestMapping(value = "/insertGlobalFilter", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody Map<String,Object> insertGlobalFilter(HttpServletRequest request, Model model, HttpServletResponse response,  @RequestParam HashMap<String, Object> params){
		logger.info("insertGlobalFilter");
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> result = new HashMap<String,Object>();
		int resultCode = 0;
		int resultValue = 0;
		
		try {
			resultValue = searchService.insertFilterList(params, request);
			/*result = searchService.insertNetList(params, request);*/
			
		}
		catch (Exception e) {
			e.printStackTrace();
			
			result.put("resultValue", resultValue);
			result.put("resultCode", -1);
			result.put("resultMessage", "insert 실패");
		}
		
		result.put("resultValue", resultValue);
		
		return result;
		
	}

	@RequestMapping(value = "/getStopSch", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getStopSch(HttpServletRequest request, Model model){
		logger.info("getStopSch");
		
		Map<String, Object> resultMap = null;
		try {
			resultMap = searchService.getStopSch(request);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("select Stop Sch :::: " + e.getLocalizedMessage());
		}
		
		return resultMap;
		
	}
	
	@RequestMapping(value = "/addNextTarget", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody Map<String,Object> addNextTarget(HttpServletRequest request, Model model, HttpServletResponse response,  @RequestParam HashMap<String, Object> params){
		logger.info("addNextTarget");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			map = searchService.addNextTarget(request);
			return map;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
		
	}
	
	@RequestMapping(value = "/addTarget", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody Map<String,Object> addTarget(HttpServletRequest request, Model model, HttpServletResponse response,  @RequestParam HashMap<String, Object> params){
		logger.info("addTarget");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			map = searchService.addTarget(request);
			return map;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
		
	}
	
	@RequestMapping(value = "/dropTarget", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody Map<String,Object> dropTarget(HttpServletRequest request, Model model, HttpServletResponse response,  @RequestParam HashMap<String, Object> params){
		logger.info("dropTarget");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			map = searchService.dropTarget(request);
			return map;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
		
	}
	
	@RequestMapping(value="/selectDBStatus", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> selectDBStatus(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("selectDBStatus");
	    
	    Map<String, Object> resultMap = new HashMap<>();
	    
	    try {
	    	Map<String, Object> map = searchService.selectDBStatus(request);
	    	
	        resultMap.put("PROTOCOL", map.get("PROTOCOL"));
	        resultMap.put("CREDENTIAL_ID", map.get("CREDENTIAL_ID"));
	        resultMap.put("PROXY_ID", map.get("PROXY_ID"));
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    
	    return resultMap;
    }
	
	@RequestMapping(value = "/addDBStatus", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody Map<String,Object> addDBStatus(HttpServletRequest request, Model model, HttpServletResponse response,  @RequestParam HashMap<String, Object> params){
		logger.info("addDBStatus");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			map = searchService.addDBStatus(request);
			return map;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
		
	}
}