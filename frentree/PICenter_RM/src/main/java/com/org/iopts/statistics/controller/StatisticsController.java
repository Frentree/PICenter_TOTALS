package com.org.iopts.statistics.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.iopts.statistics.service.StatisticsService;
import com.org.iopts.util.SessionUtil;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Handles requests for the application home page.
 */

@Controller
@RequestMapping(value = "/statistics")
public class StatisticsController {

	private static Logger logger = LoggerFactory.getLogger(StatisticsController.class);
	
	@Value("${recon.api.version}")
	private String api_ver;
	
	@Inject
	private StatisticsService service;
	
	@RequestMapping(value = "/statisticsList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> statisticsList(HttpServletRequest request, Model model){
	    logger.info("statisticsList");

	    Map<String, Object> resultMap = null;
	    try {
	        resultMap = service.statisticsList(request);
	    } catch (NumberFormatException e) {
	        logger.error("error :: "+e.toString());
	    } catch (RuntimeException e) {
	        logger.error("error :: "+e.toString());
	    } catch (Exception e) {
	        logger.error("error :: "+e.toString());
	    }

	    return resultMap;
	}
    
	//  전체 서버 점검 결과(의심건수) 조회
	@RequestMapping(value = "/manageList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> manageList(HttpServletRequest request, Model model){
	    logger.info("manageList");

	    Map<String, Object> resultMap = null;
	    try {
	        resultMap = service.manageList(request);
	    } catch (RuntimeException e) {
	        logger.error("manageList 요청 처리 중 오류 발생", e);
	    } catch (Exception e) {
	        logger.error("manageList 요청 처리 중 오류 발생", e);
	        throw new RuntimeException("데이터 처리 중 오류가 발생했습니다. 관리자에게 문의하세요.");
	    }
	    return resultMap;
	}
	
	//  전체 서버 점검 결과(의심건수) 조회 - 그래프
	@RequestMapping(value = "/manageBarList", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> manageBarList(HttpServletRequest request, Model model){
		logger.info("manageBarList");
      
		List<Map<String, Object>> resultMap = null;
		try {
			resultMap = service.manageBarList(request);
		} catch (RuntimeException e) {
			logger.error("error :: "+e.toString());
		} catch (Exception e) {
			logger.error("error :: "+e.toString());
		}
		      
		return resultMap;
      
	}

	@RequestMapping(value = "/totalStatistics", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> totalStatistics(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
	    logger.info("totalStatistics");
	    
	    Map<String, Object> resultMap = service.totalStatistics(request);
	    
	    return resultMap;
	}
	
	// 개인정보 유형 top
	@RequestMapping(value = "/statisticsPolicyList", method = { RequestMethod.POST })
	public @ResponseBody List<Map<String,Object>> statisticsPolicyList(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("statisticsPolicyList");
		List<Map<String,Object>> resultMap = service.statisticsPolicyList(request);
		return resultMap;
	}
	
	// 개인정보 유형 top
	@RequestMapping(value = "/excelDown", method = { RequestMethod.POST })
	@ResponseBody
	public List<HashMap<String, Object>> excelDown(Model model, @RequestParam HashMap<String, Object> params) throws Exception 
	{
		params.put("user_no", SessionUtil.getSession("memberSession", "USER_NO"));			// 사용자
		
		List<HashMap<String, Object>> searchList = service.excelDown(params);

		return searchList;
	}
	
}
