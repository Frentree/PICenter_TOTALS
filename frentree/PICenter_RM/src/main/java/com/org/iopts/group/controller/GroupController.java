package com.org.iopts.group.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.org.iopts.group.service.GroupService;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/group")
@Configuration
@PropertySource("classpath:/property/config.properties")
public class GroupController {

	private static Logger logger = LoggerFactory.getLogger(GroupController.class);

	@Value("${recon.api.version}")
	private String api_ver;
	
	@Inject
	private GroupService groupService;
	
	@RequestMapping(value = "/dashListDept", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody Map<String,Object> insertListProfile(HttpServletRequest request, Model model, HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		logger.info("dashListDept");
		
		Map<String, Object> map = new HashMap<String, Object>();
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		
		map.put("fromDate", fromDate);
		map.put("toDate", toDate);
		
		Map<String,Object> result = new HashMap<>();
		try {
			JsonArray resultGroup = groupService.selectDashDeptList(map, request);
			result.put("data", resultGroup);
			result.put("resultCode", 1);
		} catch (NullPointerException e) {
			result.put("resultCode", -1);
			logger.error("error :: "+e.toString());
		} catch (Exception e) {
			result.put("resultCode", -1);
			logger.error("error :: "+e.toString());
		}
		logger.info("dashListDept Data >>> " + result);
		
		return result;
		
	}
	
	@RequestMapping(value = "/dashServerDept", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody Map<String,Object> insertServerProfile(HttpServletRequest request, Model model, HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		logger.info("dashServerDept");
		
		Map<String, Object> map = new HashMap<String, Object>();
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		
		map.put("fromDate", fromDate);
		map.put("toDate", toDate);
		
		Map<String,Object> result = new HashMap<>();
		try {
			JsonArray resultGroup = groupService.selectDashSeverDept(map, request);
			result.put("data", resultGroup);
			result.put("resultCode", 1);
		}catch (NullPointerException e) {
			logger.error("Error occurred in selectDashSeverDept: {}", e.getMessage(), e);
			result.put("resultCode", -1);
			result.put("errorMessage", "서버 오류가 발생했습니다."); // 클라이언트에 전달할 메시지
		}catch (DataAccessException e) {
			logger.error("Error occurred in selectDashSeverDept: {}", e.getMessage(), e);
			result.put("resultCode", -1);
			result.put("errorMessage", "서버 오류가 발생했습니다."); // 클라이언트에 전달할 메시지
		}catch (Exception e) {
			logger.error("Error occurred in selectDashSeverDept: {}", e.getMessage(), e);
			result.put("resultCode", -1);
		    result.put("errorMessage", "서버 오류가 발생했습니다."); // 클라이언트에 전달할 메시지
		}
		logger.info("dashServerDept Data >>> " + result);
		
		return result;
		
	}
	
	
	/*@RequestMapping(value = "/selectTomsNotGroup", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody Map<String,Object> selectTomsNotGroup(HttpServletRequest request, Model model, HttpServletResponse response){
		//response.setCharacterEncoding("UTF-8");
		logger.info("selectTomsNotGroup");
		//logger.info("treeArr >>> " + treeArr);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		Map<String,Object> result = new HashMap<String,Object>();
		int resultCode = -1;
		
		try {
			JSONArray resultGroup = groupService.selectTomsNotGroup(map, request);
			result.put("data", resultGroup.toString());
			
		}
		catch (Exception e) {
			logger.error(e.toString());
			result.put("resultCode", -1);
		}
		result.put("resultCode", resultCode);
		logger.info("selectTomsNotGroup Data >>> " + result);
		
		return result;
		
	}*/
	
	@RequestMapping(value = "/insertUserTargets", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody Map<String,Object> insertUserTargets(HttpServletRequest request, Model model, HttpServletResponse response){
		//response.setCharacterEncoding("UTF-8");
		logger.info("insertUserTargets");
		//logger.info("treeArr >>> " + treeArr);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		Map<String,Object> result = new HashMap<String,Object>();
		int resultCode = -1;
		
		try {
			JsonArray resultGroup = groupService.insertUserTargets(map, request);
			result.put("data", resultGroup.toString());
			resultCode = 0;
		}catch (NullPointerException e) {
			logger.error(e.toString());
			result.put("resultCode", -1);
		}catch (DataAccessException e) {
			logger.error(e.toString());
			result.put("resultCode", -1);
		}catch (Exception e) {
			logger.error(e.toString());
			result.put("resultCode", -1);
		}
		result.put("resultCode", resultCode);
		logger.info("insertUserTargets Data >>> " + result);
		
		return result;
	}
}