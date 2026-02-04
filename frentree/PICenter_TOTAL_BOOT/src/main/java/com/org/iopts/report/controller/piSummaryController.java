package com.org.iopts.report.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.iopts.exception.service.piDetectionListService;
import com.org.iopts.report.service.piSummaryService;
import com.org.iopts.service.Pi_UserService;
import com.org.iopts.util.SessionUtil;

import jakarta.inject.Inject;

@Controller
@RequestMapping(value = "/report")
@Configuration
@PropertySource("classpath:/property/config.properties") 
public class piSummaryController {

	private static Logger log = LoggerFactory.getLogger(piSummaryController.class);

	@Inject
	private Pi_UserService userService;

	@Inject
	private piSummaryService service;
	
	@Inject piDetectionListService detectionService;

	/*
	 * 
	 */
	@RequestMapping(value = "/pi_report_summary", method = {RequestMethod.GET, RequestMethod.POST})
	public String pi_search_list (Model model) throws Exception 
	{
		Map<String, Object> member = SessionUtil.getSession("memberSession");
		model.addAttribute("memberInfo", member);
		
		model.addAttribute("menuKey", "exceptionMenu");
		model.addAttribute("menuItem", "reportAppr");

		try {
			List<Map<String, Object>> patternList = detectionService.queryCustomDataTypes();
			model.addAttribute("pattern", patternList);
		}catch (RuntimeException e) {
			log.error(e.toString());
			//model.addAttribute("errorMessage", "데이터 조회 중 오류가 발생했습니다.");
		}
		
		// 연도 구하기
		List<String> yearList = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR)-9;
		for(int i=0; i<10; i++) {
			yearList.add((year+i)+"");
		}
		model.addAttribute("monthly_year", yearList);
		model.addAttribute("monthly_month", (calendar.get(Calendar.MONTH)+1)+"");
		
		//return "/report/pi_report_summary"; 
		return "/report/pi_report_summary_rm";
	}
	@RequestMapping(value = "/pi_target_summary", method = {RequestMethod.GET, RequestMethod.POST})
	public String pi_target_summary (Model model) throws Exception 
	{
		log.info("pi_target_summary");
		
		Map<String, Object> member = SessionUtil.getSession("memberSession");
		model.addAttribute("memberInfo", member);
		
		model.addAttribute("menuKey", "exceptionMenu");
		model.addAttribute("menuItem", "reportAppr");

		try {
			List<Map<String, Object>> patternList = detectionService.queryCustomDataTypes();
			model.addAttribute("pattern", patternList);
		} catch (RuntimeException e) {
		    log.error("Failed to query custom data types for model", e);
		    //model.addAttribute("pattern", Collections.emptyList()); // 대체 값 설정
		}
		
		// 연도 구하기
		List<String> yearList = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR)-9;
		for(int i=0; i<10; i++) {
			yearList.add((year+i)+"");
		}
		model.addAttribute("monthly_year", yearList);
		model.addAttribute("monthly_month", (calendar.get(Calendar.MONTH)+1)+"");
		
		//return "/report/pi_report_summary"; 
		return "/report/pi_target_summary_rm";
	}
	
	@RequestMapping(value = "/pi_report_manager", method = {RequestMethod.GET, RequestMethod.POST})
	public String pi_report_manager (Model model) throws Exception 
	{
		Map<String, Object> member = SessionUtil.getSession("memberSession");
		model.addAttribute("memberInfo", member);
		
		model.addAttribute("menuKey", "exceptionMenu");
		model.addAttribute("menuItem", "reportMana");

		// 연도 구하기
		List<String> yearList = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR)-9;
		for(int i=0; i<10; i++) {
			yearList.add((year+i)+"");
		}
		model.addAttribute("monthly_year", yearList);
		model.addAttribute("monthly_month", (calendar.get(Calendar.MONTH)+1)+"");
		
		//return "/report/pi_report_manager";		
		return "/report/pi_report_manager";		
	}
	
	@RequestMapping(value = "/pi_report_manager2", method = {RequestMethod.GET, RequestMethod.POST})
	public String pi_report_manager2 (Model model) throws Exception 
	{
		Map<String, Object> member = SessionUtil.getSession("memberSession");
		model.addAttribute("memberInfo", member);
		 
		model.addAttribute("menuKey", "exceptionMenu");
		model.addAttribute("menuItem", "reportMana");

		// 연도 구하기
		List<String> yearList = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR)-9;
		for(int i=0; i<10; i++) { 
			yearList.add((year+i)+"");
		}
		model.addAttribute("monthly_year", yearList);
		model.addAttribute("monthly_month", (calendar.get(Calendar.MONTH)+1)+"");
		
		return "/report/pi_report_manager";		
	}

	// 
	@RequestMapping(value="/searchSummaryList", method={RequestMethod.POST}) 
	@ResponseBody
	public List<HashMap<String, Object>> searchSummaryList(Model model, @RequestParam HashMap<String, Object> params) throws Exception 
	{
		params.put("user_no", SessionUtil.getSession("memberSession", "USER_NO"));			// 사용자
		
		List<HashMap<String, Object>> searchList = service.searchSummaryList(params);

		return searchList;
	} 
	
	// select download data
	@RequestMapping(value="/getMonthlyReport", method= {RequestMethod.POST})
	@ResponseBody
	public List<HashMap<String, Object>> getMonthlyReport(@RequestBody HashMap<String, Object> params) throws Exception 
	{
		log.info("getDownloadData START");
		
		List<HashMap<String, Object>> reportList = service.getMonthlyReport(params);
		
		return reportList;
	}
	
	@RequestMapping(value="/searchTargetSummaryReport", method={RequestMethod.POST})
	@ResponseBody
	public List<HashMap<String, Object>> searchTargetSummaryReport(Model model, @RequestParam HashMap<String, Object> params) throws Exception 
	{
		params.put("user_no", SessionUtil.getSession("memberSession", "USER_NO"));			// 사용자
		
		List<HashMap<String, Object>> searchList = null;
		
		searchList = service.searchTargetSummaryReport(params);

		return searchList;
	}
}