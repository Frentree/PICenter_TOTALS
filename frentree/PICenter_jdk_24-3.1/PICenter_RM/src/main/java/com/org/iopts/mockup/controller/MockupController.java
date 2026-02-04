package com.org.iopts.mockup.controller;

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
import com.org.iopts.mockup.service.MockupService;
import com.org.iopts.search.service.SearchService;
import com.org.iopts.service.Pi_TargetService;
import com.org.iopts.service.Pi_UserService;
import com.org.iopts.setting.service.impl.Pi_SetServiceImpl;
import com.org.iopts.util.SessionUtil;

@Controller
@RequestMapping(value = "/mock")
@Configuration
@PropertySource("classpath:/property/config.properties")
public class MockupController {

	private static final Logger logger = LoggerFactory.getLogger(MockupController.class);

	@Value("${recon.api.version}")
	private String api_ver;
	
	@Inject
	private MockupService mockupService;
	
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

	
	// 전체대상 왼쪽 위 차트
	@RequestMapping(value = "/allTargetList", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> allTargetList(HttpServletRequest request){
		
		List<Map<String, Object>> resultMap = null;
		try {
			resultMap = mockupService.allTargetList(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultMap;
	}
	@RequestMapping(value = "/getServerColumns", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> getServerColumns(HttpServletRequest request){
		
		List<Map<String, Object>> resultMap = null;
		try {
			resultMap = mockupService.getServerColumns(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultMap;
	}
	
	
	
	@RequestMapping(value = "/getServerDataPivot", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getServerDataPivot(HttpServletRequest request){
	    Map<String, Object> allData = new HashMap<>();

	    try {
	    	allData = mockupService.getServerDataPivot(request);
	    } catch (Exception e) {
	        logger.error("Error: " + e.getMessage(), e);
	        allData.put("error", "처리중 에러가 발생하였습니다.");
	        allData.put("customPatterns", new ArrayList<>());
	        allData.put("gridData", new ArrayList<>());
	        allData.put("serverTypes", new HashMap<>());
	        allData.put("dbTypes", new HashMap<>());
	        allData.put("serverColumns", new ArrayList<>());
	    }

	    return allData;
	}

}