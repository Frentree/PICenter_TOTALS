package com.org.iopts.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.iopts.service.Pi_ExceptionService;
import com.org.iopts.service.Pi_TargetService;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Handles requests for the application home page.
 */

@Controller
@RequestMapping(value="/exception")
@Configuration
@PropertySource("classpath:/property/config.properties")
public class ExceptionController {

	private static Logger logger = LoggerFactory.getLogger(ExceptionController.class);
	
	@Value("${recon.api.version}")
	private String api_ver;
 
	@Inject
	private Pi_TargetService targetservice;

	@Inject
	private Pi_ExceptionService exceptionService;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = { "/pi_exception_regist" }, method = {RequestMethod.GET, RequestMethod.POST})
	public String pi_exception_regist(Locale locale, Model model, HttpServletRequest request) throws Exception {

		logger.info("/exception/pi_exception_regist");
		
		String target_id = request.getParameter("target_id");
		model.addAttribute("dashclick", target_id);
		
		model.addAttribute("menuKey", "exceptionMenu");
		model.addAttribute("menuItem", "exceptionRegist");
		
		List<Map<String, Object>> targetList = targetservice.selectUserTargetList(request);
		model.addAttribute("targetList", targetList);
		
		return "exception/pi_exception_regist";
	}

	@RequestMapping(value = { "/pi_exception_list" }, method = RequestMethod.GET)
	public String pi_exception_list(Locale locale, Model model, HttpServletRequest request) throws Exception {

		logger.info("/exception/pi_exception_list");

		model.addAttribute("menuKey", "exceptionMenu");
		model.addAttribute("menuItem", "exceptionList");

		Calendar cal = new GregorianCalendar(Locale.KOREA);
	    SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
	    String toDate = fm.format(cal.getTime());

	    cal.setTime(new Date());
	    cal.add(Calendar.MONTH, -1);	     
	    String fromDate = fm.format(cal.getTime());

		model.addAttribute("fromDate", fromDate);
		model.addAttribute("toDate", toDate);

		List<Map<String, Object>> targetList = targetservice.selectUserTargetList(request);
		model.addAttribute("targetList", targetList);
		
		return "exception/pi_exception_list";
	}

	@RequestMapping(value = { "/pi_exception_appr" }, method = RequestMethod.GET)
	public String pi_exception_appr(Locale locale, Model model, HttpServletRequest request) throws Exception {

		logger.info("/exception/pi_exception_appr");

		model.addAttribute("menuKey", "exceptionMenu");
		model.addAttribute("menuItem", "exceptionAppr");

		Calendar cal = new GregorianCalendar(Locale.KOREA);
	    SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
	    String toDate = fm.format(cal.getTime());

	    cal.setTime(new Date());
	    cal.add(Calendar.MONTH, -1);	     
	    String fromDate = fm.format(cal.getTime());

		model.addAttribute("fromDate", fromDate);
		model.addAttribute("toDate", toDate);

		List<Map<String, Object>> targetList = targetservice.selectUserTargetList(request);
		model.addAttribute("targetList", targetList);
		
		return "exception/pi_exception_approve";
	}
	
	@RequestMapping(value = { "/pi_exception_deletion" }, method = RequestMethod.GET)
	public String pi_exception_deletion(Locale locale, Model model, HttpServletRequest request) throws Exception {

		logger.info("/exception/pi_exception_deletion");

		model.addAttribute("menuKey", "exceptionMenu");
		model.addAttribute("menuItem", "exceptionDeletion");

		Calendar cal = new GregorianCalendar(Locale.KOREA);
	    SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
	    String toDate = fm.format(cal.getTime());

	    cal.setTime(new Date());
	    cal.add(Calendar.MONTH, -1);	     
	    String fromDate = fm.format(cal.getTime());

		model.addAttribute("fromDate", fromDate);
		model.addAttribute("toDate", toDate);

		List<Map<String, Object>> targetList = targetservice.selectUserTargetList(request);
		model.addAttribute("targetList", targetList);
		
		return "exception/pi_exception_deletion";
	}

	@RequestMapping(value="/selectExceptionList", method={RequestMethod.POST})
    public @ResponseBody List<Map<String, Object>> selectExceptionList(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("selectExceptionList");

		return exceptionService.selectExceptionList(request);
    }

	@RequestMapping(value="/selectExceptionApprList", method={RequestMethod.POST})
    public @ResponseBody List<Map<String, Object>> selectExceptionApprList(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("selectExceptionApprList");

		return exceptionService.selectExceptionApprList(request);
    }
	
	@RequestMapping(value="/selectDeletionList", method={RequestMethod.POST})
    public @ResponseBody List<Map<String, Object>> selectDeletionList(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("selectDeletionList");

		return exceptionService.selectDeletionList(request);
    }

	@RequestMapping(value="/selectFindSubpath", method={RequestMethod.POST})
    public @ResponseBody List<Map<String, Object>> selectFindSubpath(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("selectFindSubpath");

		List<Map<String, Object>> findSubpathList = exceptionService.selectFindSubpath(request);
		return findSubpathList;
    }

	@RequestMapping(value="/getMatchObjects", method={RequestMethod.POST})
    public @ResponseBody Map<String, Object> getMatchObjects(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("getMatchObjects");
		
		Map<String,Object> map = exceptionService.getMatchObjects(request, api_ver);
		return map;
    }
	
	// select download data
	@RequestMapping(value="/getDownloadData", method={RequestMethod.POST})
	@ResponseBody
	public List<Map<String, Object>> getDownloadData(@RequestBody HashMap<String, Object> params) throws Exception {
		logger.info("getDownloadData START");

		HashMap<String, Object> targetInfo = targetservice.selectTargetById(params);
		List<Map<String, Object>> dataList = exceptionService.selectDownloadList(params, targetInfo);
		
		return dataList;
	}
}
