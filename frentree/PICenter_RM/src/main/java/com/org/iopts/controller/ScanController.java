package com.org.iopts.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.iopts.service.Pi_ScanService;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


/**
 * Handles requests for the application home page.
 */

@Controller
@PropertySource("classpath:/property/config.properties")
@Configuration
@RequestMapping(value = "/scan", method = {RequestMethod.POST,RequestMethod.GET})
public class ScanController {

	private static Logger logger = LoggerFactory.getLogger(ScanController.class);
	
	@Value("${recon.api.version}")
	private String api_ver;

	@Inject
	private Pi_ScanService scheduleservice;

	
	@RequestMapping(value="/getApList", method={RequestMethod.POST})
    public @ResponseBody List<Map<String, Object>> getApList(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("getApList");
		
		List<Map<String, Object>> resultList = scheduleservice.getApList(request);
		
		return resultList;
    }
}
