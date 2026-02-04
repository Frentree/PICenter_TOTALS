package com.org.iopts.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.org.iopts.dto.Pi_AgentVO;
import com.org.iopts.dto.Pi_TargetVO;
import com.org.iopts.exception.service.piDetectionListService;
import com.org.iopts.group.service.GroupService;
import com.org.iopts.service.Pi_AgentService;
import com.org.iopts.service.Pi_DashService;
import com.org.iopts.service.Pi_TargetService;
import com.org.iopts.service.Pi_UserService;
import com.org.iopts.setting.service.impl.Pi_SetServiceImpl;
import com.org.iopts.util.ReconUtil;
import com.org.iopts.util.ServletUtil;
import com.org.iopts.util.SessionUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Handles requests for the application home page.
 */

@Controller
@Configuration
@PropertySource("classpath:/property/config.properties")
public class HomeController {

	private static Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Value("${recon.id}")
	private String recon_id;

	@Value("${recon.password}")
	private String recon_password;

	@Value("${recon.url}")
	private String recon_url;

	@Value("${recon.api.version}")
	private String api_ver;
	
	@Value("${pic.main.jsp}")
	private String mainJsp;

	@Inject
	private Pi_UserService service;

	@Inject
	private Pi_TargetService targetservice;

	@Inject
	private Pi_AgentService agetnservice;

	@Inject
	private Pi_DashService dashservice;
	
	@Inject
	private GroupService groupservice;
	
	@Inject
	private Pi_SetServiceImpl set_service;
	
	@Inject
	private piDetectionListService detection_service;
	
	private String requestUrl = null;
	private String retrunUrl  = null;

	@RequestMapping(value = { "/" }, method = {RequestMethod.GET, RequestMethod.POST})
	public String home(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response)
			throws Exception {
		
		Map<String, Object> version = service.getversion();	
		model.addAttribute("picenter_data", version);
		
//		return "main";
		return mainJsp;
	}
	
	@RequestMapping(value = { "/timeout" }, method = {RequestMethod.GET, RequestMethod.POST})
	public String timeout(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response)
			throws Exception {
		
		Map<String, Object> version = service.getversion();	
		model.addAttribute("picenter_data", version);
		
//		return "main";
		return mainJsp;
	}
	@RequestMapping(value = { "/login" }, method = {RequestMethod.GET, RequestMethod.POST})
	public String loginpage(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response)
			throws Exception {
		
		Map<String, Object> version = service.getversion();
		model.addAttribute("picenter_data", version);
		
//		return "main";
		return "login";
	}
	

	@RequestMapping(value = { "/mailImg" }, method = {RequestMethod.GET, RequestMethod.POST})
	public String mailImg(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response)
			throws Exception {
		logger.info("/mailImg");
		
		return "mailImg";
	}
	
	@RequestMapping(value = { "/sso_link" }, method = {RequestMethod.GET, RequestMethod.POST})
	public String sso_link(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response)
			throws Exception {
		logger.info("/sso_link");
		
		
		String userid = request.getParameter("userid");
		
		model.addAttribute("userid", userid);
		return "sso_link";
	}


	@RequestMapping(value = { "/main" }, method = RequestMethod.GET)
	public String main(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response)
			throws Exception {
		return "main";
	}

	// 송명수씨 작업 내역
	@RequestMapping(value = { "/main1" }, method = RequestMethod.GET)
	public String home1(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response)
			throws Exception {
		return "main1";
	}

	@RequestMapping(value = { "/main2" }, method = RequestMethod.GET)
	public String home2(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response)
			throws Exception {
		return "main2";
	}

	@RequestMapping(value = { "/main3" }, method = RequestMethod.GET)
	public String home3(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response)
			throws Exception {
		return "main3";
	}

	@RequestMapping(value = { "/pie" }, method = RequestMethod.GET)
	public String pie(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response)
			throws Exception {
		return "pie";
	}

	@RequestMapping(value = { "/demo" }, method = RequestMethod.GET)
	public String demo(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response)
			throws Exception {
		return "demo";
	}
	
	@RequestMapping(value = { "/faq" }, method = RequestMethod.GET)
	public String faq(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		
		requestUrl = "/faq";
		try {
			Map<String, Object> pageGrade = set_service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				set_service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
			}else {
				retrunUrl = "/error/notPageGrade";
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
		}
		
		return retrunUrl;
	}

	/**
	 * 여기 다가 입력된 pi_user 테이블에 user_id 확인해서 등록된 사용자가 아니면 로그인 안되게 해 주세요. 로그인이 성공하면
	 * pi_userlog 테이블에 이력 쌓아 주세요. job_info 칼럼에 : LOGIN(SUCCESS), LOGIN(FAIL)
	 */
	@RequestMapping(value = "/memberLogin", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> login(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("/managerLogin");

		// 사용자번호 + 비밀번호로 로그인 하는 경우
		Map<String, Object> memberMap = new HashMap<String, Object>();
		try {
			memberMap = service.selectMember(request);
			String result = memberMap.get("resultCode").toString();
			memberMap.put("resultCode", result);
			memberMap.put("resultMessage", memberMap.get("resultMessage"));
			
		} catch (Exception e) {
			memberMap.put("resultCode", -100);
//			e.printStackTrace();
		}
		Map<String, Object> noticeMap = service.selectNotice();
		model.addAttribute("noticeMap", noticeMap);
		model.addAttribute("user_grade", memberMap.get("user_grade"));

		return memberMap;
	}
	
	@RequestMapping(value = "/changeUser", method = { RequestMethod.POST,  RequestMethod.GET })
	public @ResponseBody Map<String, Object> changeUser(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("/changeUser");

		// 사용자번호 + 비밀번호로 로그인 하는 경우
		Map<String, Object> memberMap = new HashMap<String, Object>();
		try {
			memberMap = service.changeUser(request);
			String result = memberMap.get("resultCode").toString();
			
			memberMap.put("resultCode", result);
			memberMap.put("resultMessage", memberMap.get("resultMessage"));
			if(result.equals("-9")) {
				return memberMap;
			}
			
		} catch (Exception e) {
			memberMap.put("resultCode", -100);
			e.printStackTrace();
		}
		Map<String, Object> noticeMap = service.selectNotice();
		model.addAttribute("noticeMap", noticeMap);
		model.addAttribute("memberInfo", memberMap.get("member"));
		model.addAttribute("user_grade", memberMap.get("user_grade"));

		return memberMap;
	}
	
	@RequestMapping(value = "/accountMemberSSO", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> accountMemberSSO(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("/accountMemberSSO");
		
		Map<String, Object> memberMap = new HashMap<String, Object>();
		try {
			memberMap = service.accountMemberSSO(request);

			String result = memberMap.get("resultCode").toString();
			
			memberMap.put("resultCode", result);
			memberMap.put("resultMessage", memberMap.get("resultMessage"));
		} catch (Exception e) {
			memberMap.put("resultCode", -100);
			e.printStackTrace();
		}
		model.addAttribute("user_grade", memberMap.get("user_grade"));
		
		return memberMap;
	}

	@RequestMapping(value = "/session_quit", method = { RequestMethod.GET, RequestMethod.POST })
	public String sessionQuit(Model model, HttpSession session) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		resultMap.put("resultCode", 1);
		resultMap.put("resultMessage", "Log-Out");

		return "session_quit";
	}
	
	@RequestMapping(value = "/submitSmsLogin", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> submitSmsLogin(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("/submitSmsLogin");
		
		// 사용자번호 + 비밀번호로 로그인 하는 경우
		Map<String, Object> memberMap = new HashMap<String, Object>(); 
		try {
			memberMap = service.submitSmsLogin(request);
		}
		catch(Exception e) {
			memberMap.put("resultCode", -100);
			memberMap.put("resultMessage", "시스템오류입니다.\n관리자에게 문의하세요.");
			e.printStackTrace();
		}
		
		model.addAttribute("memberInfo", memberMap.get("member"));
		model.addAttribute("user_grade", memberMap.get("user_grade"));
		
		return memberMap;
	}
	
	
	// 초기화 비밀번호 변경
	@RequestMapping(value = "/changeResetPwd", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> changeResetPwd(HttpSession session, HttpServletRequest request,
			Model model, HttpServletResponse response) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = service.changeResetPwd(request);
		} catch (Exception e) {
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "시스템오류입니다.\n관리자에게 문의하세요.");
			e.printStackTrace();
		}

		//model.addAttribute("memberInfo", resultMap.get("member"));

		return resultMap;
	}
	
	// 비밀번호 초기화 취소 
	@RequestMapping(value = "/reset_sms_code", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> reset_sms_code(HttpSession session, HttpServletRequest request,
			Model model, HttpServletResponse response) throws Exception {
		logger.info("reset_sms_code");

		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = service.reset_sms_code(request);
		} catch (Exception e) {
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "시스템오류입니다.\n관리자에게 문의하세요.");
			e.printStackTrace();
		}

		return resultMap;
	}
	
	@RequestMapping(value = "/sms_login", method = {RequestMethod.GET, RequestMethod.POST})
	public String smsLogin(Model model, HttpSession session) 
	{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		resultMap.put("resultCode", 1);
		resultMap.put("resultMessage", "Log-Out");

		Map<String, Object> map = new HashMap<>();
		map.put("noGroup", "Y");
		try {
			List<Map<String, Object>> userGroupList = targetservice.selectUserGroupList(map);
			model.addAttribute("userGroupList", userGroupList);
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return "sms_login";
	}
	
	@RequestMapping(value = "/sms_login_resend", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> smsLoginResend(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("/sms_login_resend");
		
		// 사용자번호 + 비밀번호로 로그인 하는 경우
		Map<String, Object> memberMap = new HashMap<String, Object>(); 
		try {
			memberMap = service.authSMSResend(request);
		}
		catch(Exception e) {
			memberMap.put("resultCode", -100);
			memberMap.put("resultMessage", "시스템오류입니다.\n관리자에게 문의하세요.");
			e.printStackTrace();
		}
		
		

		return memberMap;
	}
	@RequestMapping(value = "/sms_login_cancel", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> smsLoginCancel(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("/sms_login_cancel");
		
		// 사용자번호 + 비밀번호로 로그인 하는 경우
		Map<String, Object> memberMap = new HashMap<String, Object>(); 
		try {
			memberMap = service.authSMSCancel(request);
		}
		catch(Exception e) {
			memberMap.put("resultCode", -100);
			memberMap.put("resultMessage", "시스템오류입니다.\n관리자에게 문의하세요.");
			e.printStackTrace();
		}

		return memberMap;
	}
	
	@RequestMapping(value = "/NCLoginTestENC", method = {RequestMethod.GET, RequestMethod.POST})
	public String NCLoginTestENC(Model model, HttpSession session) 
	{
		Map<String, Object> resultMap = new HashMap<String, Object>();

		resultMap.put("resultCode", 1);
		resultMap.put("resultMessage", "Log-Out");

		return "NCLoginTestENC";
	}

	@RequestMapping(value = "/NCLoginTestEndVerify", method = RequestMethod.POST)
	public String NCLoginTestEndVerify(Model model, HttpSession session, HttpServletRequest request) {
		model.addAttribute("ticket", request.getParameter("ticket"));
		model.addAttribute("ssoid", request.getParameter("ssoid"));

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", 1);
		resultMap.put("resultMessage", "Log-Out");

		return "NCLoginTestEndVerify";
	}

	@RequestMapping(value = "/changeAuthCharacter", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> changeAuthCharacter(HttpSession session, HttpServletRequest request,
			Model model, HttpServletResponse response) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = service.changeAuthCharacter(request);
		} catch (Exception e) {
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "시스템오류입니다.\n관리자에게 문의하세요.");
			e.printStackTrace();
		}

		model.addAttribute("memberInfo", resultMap.get("member"));

		return resultMap;
	}
	
	@RequestMapping(value = "/loginSSO", method = { RequestMethod.POST,  RequestMethod.GET })
	public @ResponseBody Map<String, Object> loginSSO(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("/loginSSO");

		// 사용자번호 + 비밀번호로 로그인 하는 경우
		Map<String, Object> memberMap = new HashMap<String, Object>();
		try {
			memberMap = service.selectSSOMember(request);
			/*
			 * EmailVO p=new EmailVO(1); p.setSendder("sender.."); p.setReciver("receiver");
			 * 
			 * NhEmailUtil e=new NhEmailUtil("e:/temp/email/",p); e.sendEmail();
			 */
			String result = memberMap.get("resultCode").toString();
			/*if(!"0".equals(result)) {
				memberMap.put("resultCode", -1);
			}*/
			memberMap.put("resultCode", result);
			memberMap.put("resultMessage", memberMap.get("resultMessage"));
		} catch (Exception e) {
			memberMap.put("resultCode", -100);
			//memberMap.put("resultMessage", "시스템오류입니다.\n관리자에게 문의하세요.");
			e.printStackTrace();
		}
		Map<String, Object> noticeMap = service.selectNotice();
		model.addAttribute("noticeMap", noticeMap);
		model.addAttribute("memberInfo", memberMap.get("member"));
		model.addAttribute("user_grade", memberMap.get("user_grade"));

		return memberMap;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> logout(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {

		service.logout(request);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", 0);
		resultMap.put("resultMessage", "Log-Out");

		return resultMap;
	}

	
	
	@RequestMapping(value = "/picenter", method = RequestMethod.GET)
	public String main(Locale locale, Model model, HttpServletRequest request) throws Exception {

		logger.info("Start picenter");
		logger.info("dash_menu");

		requestUrl = "/picenter";
		try {
			Map<String, Object> pageGrade = set_service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				set_service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
				Map<String, Object> map = new HashMap<>();
				map.put("noGroup", "Y");

				try {
//					자산 정보
					String userGroupList = groupservice.selectAccountServer(map, request);
					model.addAttribute("userGroupList", userGroupList);

//					스케줄 현황 노드
					try {
						JsonArray deptList = groupservice.selectDashDeptList(map,request);
						model.addAttribute("deptList", deptList);
					} catch (Exception e) {
						e.printStackTrace();
					}

//					공지사항 list
					List<Map<String, Object>> noticeList = targetservice.selectNoticeList(map);
					model.addAttribute("noticeList", noticeList);

//					공지사항 팝업
					Map<String, Object> noticePop = dashservice.selectNoticePop(map);
					model.addAttribute("noticePop", noticePop);

				}catch (Exception e) {
					logger.error(e.toString());
				}
			}else {
				retrunUrl = "/error/notPageGrade";
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
		}

		return retrunUrl;
	}
	
	@RequestMapping(value = "/picenter_manager", method = RequestMethod.GET)
	public String main_skt_manager(Locale locale, Model model, HttpServletRequest request) throws Exception {
 
		logger.info("dash_menu");
		
		requestUrl = "/picenter_manager";
		
		try {
			Map<String, Object> pageGrade = set_service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				set_service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
				Map<String, Object> map = new HashMap<>();
				map.put("noGroup", "Y");
				
				try {
					String userGroupList = groupservice.selectAccountServer(map, request);
					model.addAttribute("userGroupList", userGroupList);
					
					List<Map<String, Object>> noticeList = targetservice.selectNoticeList(map);
					model.addAttribute("noticeList", noticeList);
					
					List<Map<String, Object>> patternList = detection_service.queryCustomDataTypes();
					model.addAttribute("patternList", patternList);
					
					
					Map<String, Object> noticePop = dashservice.selectNoticePop(map);
					model.addAttribute("noticePop", noticePop);
				}catch (Exception e) {
					logger.error(e.toString());
				}
				
			}else {
				retrunUrl = "/error/notPageGrade";
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
		}
		
		return retrunUrl;
	}
	
	@RequestMapping(value = "/picenter_server", method = RequestMethod.GET)
	public String main_skt_server(Locale locale, Model model, HttpServletRequest request) throws Exception {

		logger.info("dash_menu");

		requestUrl = "/picenter_server";
		
		try {
			Map<String, Object> pageGrade = set_service.checkPageGrade(requestUrl);
			if(pageGrade != null) {
				retrunUrl = pageGrade.get("RETRUN_URL").toString();
				model.addAttribute("header_no", pageGrade.get("HEADER_NO"));
				set_service.checkPageLog(request, pageGrade.get("HEADER_NAME").toString());
				Map<String, Object> map = new HashMap<>();
				map.put("noGroup", "Y");
				
				try {
					String userGroupList = groupservice.selectAccountServer(map, request);
					model.addAttribute("userGroupList", userGroupList);
					
					List<Map<String, Object>> noticeList = targetservice.selectNoticeList(map);
					model.addAttribute("noticeList", noticeList);
					
					Map<String, Object> noticePop = dashservice.selectNoticePop(map);
					model.addAttribute("noticePop", noticePop);
					
				}catch (Exception e) {
					logger.error(e.toString());
				}
			}else {
				retrunUrl = "/error/notPageGrade";
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
		}
		
		return retrunUrl;
	}
	
	@RequestMapping(value = "/header", method = RequestMethod.GET)
	public String header(Locale locale, Model model) throws Exception {

		logger.info("header");

		HttpServletRequest request = null;
		if (RequestContextHolder.getRequestAttributes() != null) {
		    request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		} else {
		    // 요청이 없는 경우 처리 (예: 비웹 컨텍스트)
		    throw new IllegalStateException("No HttpServletRequest found in the current context");
		}
		ServletUtil u = new ServletUtil(request);
		u.getIp();
		
		Map<String, Object> loginPolicyMap = new HashMap<>();
		loginPolicyMap = service.selectAccountPolicy();
		
		model.addAttribute("loginPolicyMap", loginPolicyMap);
		
		return "header";
	}

	@RequestMapping(value = "/footer", method = RequestMethod.GET)
	public String footer(Locale locale, Model model) throws Exception {

		logger.info("footer");

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		ServletUtil u = new ServletUtil(request);
		u.getIp();

		return "footer";
	}

	@RequestMapping(value = "/dash_main", method = RequestMethod.GET)
	public String dash_main(Locale locale, Model model) throws Exception {

		logger.info("dash_main");
		List<Map<String, Object>> targetList = targetservice.selectTargetManagement();
		List<Pi_AgentVO> dashList = null;

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		String target_id = request.getParameter("target_id"); // jsp의 자바스크립트를 통해 받아온 호스트 이름
		logger.info("dash_main_target : " + target_id);

		if (targetList != null && targetList.size() != 0) {
			if (target_id != null) {
				dashList = agetnservice.dashAgent_Info(target_id); // 값을 받으면 DB에 호스트 결과를 받아 옴
				model.addAttribute("target_id", target_id);
			} else {
				dashList = agetnservice.dashAgent_Info((String) targetList.get(0).get("TARGET_ID")); // 메뉴에서 클릭시 맨처음
																										// 타겟리스트의 에이전트
																										// 내용을 받아옴
				model.addAttribute("target_id", (String) targetList.get(0).get("TARGET_ID"));
			}
			model.addAttribute("targetList", targetList);
			if (dashList != null) {
				model.addAttribute("agentList", dashList);
			}
		}

		return "dashboard/dash_main";
	}

	@RequestMapping(value = "/pi_targetInfo", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> pi_targetInfo(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("pi_targetInfo");
		Map<String, Object> targetInfo = dashservice.selectDashInfo(request, api_ver);

		return targetInfo;
	}

	@RequestMapping(value = "/pi_datatype", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> pi_datatype(HttpSession session, HttpServletRequest request,
			Model model, HttpServletResponse response) throws Exception {
		logger.info("pi_datatype");
		Map<String, Object> datatypeList = dashservice.selectDatatype(request);

		//List<Map<String,Object>> datatypeList = dashservice.selectDatatype(request);
		logger.info(datatypeList.toString());
		return datatypeList;
	}

	@RequestMapping(value = "/pi_datatypes", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> pi_datatypes(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("pi_datatypes");
		Map<String, Object> datatypesList = dashservice.selectDatatypes(request);
		return datatypesList;
	}
	
	@RequestMapping(value = "/pi_datatype_manager", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> pi_datatype_manager(HttpSession session, HttpServletRequest request,
			Model model, HttpServletResponse response) throws Exception {
		logger.info("pi_datatype_manager");
		Map<String, Object> datatypeList = dashservice.selectDatatypeManager(request);

		//List<Map<String,Object>> datatypeList = dashservice.selectDatatype(request);
		logger.info(datatypeList.toString());
		return datatypeList;
	}

	@RequestMapping(value = "/pi_systemcurrent", method = { RequestMethod.POST })
	public @ResponseBody List<Object> selectSystemCurrent(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("pi_systemcurrent");
		List<Object> datatypesList = dashservice.selectSystemCurrent(request);
		return datatypesList;
	}

	@RequestMapping(value = "/pi_systemcurrent_manager", method = { RequestMethod.POST })
	public @ResponseBody List<Object> selectSystemCurrentManager(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("pi_systemcurrent_manager");
		List<Object> datatypesList = dashservice.selectSystemCurrentManager(request);
		return datatypesList;
	}
	
	@RequestMapping(value = "/pi_systemcurrent_service", method = { RequestMethod.POST })
	public @ResponseBody List<Object> selectSystemCurrentService(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("pi_systemcurrent_service");
		List<Object> datatypesList = dashservice.selectSystemCurrentService(request);
		return datatypesList;
	}
	
	@RequestMapping(value = "/pi_pathcurrent", method = { RequestMethod.POST })
	public @ResponseBody List<Object> selectPathCurrent(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("pi_pathcurrent");
		List<Object> datatypesList = dashservice.selectPathCurrent(request);
		return datatypesList;
	}
	
	@RequestMapping(value = "/pi_server_excelDownload", method = { RequestMethod.POST })
	public @ResponseBody List<Object> selectServerExcelDownload(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("pi_server_excelDownload");
		List<Object> datatypesList = dashservice.selectServerExcelDownload(request);
		return datatypesList;
	}
	
	@RequestMapping(value = "/pi_server_excelDownloadList", method = { RequestMethod.POST })
	public @ResponseBody List<Object> selectServerExcelDownloadList(@RequestParam(value="targetList[]") List<String> targetList,
			HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("pi_server_excelDownloadList");
		List<Object> selectServerExcelDownloadList = dashservice.selectServerExcelDownloadList(request, targetList);
		return selectServerExcelDownloadList;
	}
	
	@RequestMapping(value = "/pi_pc_excelDownload", method = { RequestMethod.POST })
	public @ResponseBody List<Object> selectPCExcelDownload(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("pi_pc_excelDownload");
		List<Object> datatypesList = dashservice.selectPCExcelDownload(request);
		return datatypesList;
	}
	
	@RequestMapping(value = "/pi_pc_excelDownloadList", method = { RequestMethod.POST })
	public @ResponseBody List<Object> selectPCExcelDownloadList(@RequestParam(value="targetList[]") List<String> targetList,
			HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("pi_pc_excelDownloadList");
		List<Object> selectPCExcelDownloadList = dashservice.selectPCExcelDownloadList(request, targetList);
		return selectPCExcelDownloadList;
	}

	@RequestMapping(value = "/selectlastScanDate", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> selectlastScanDate(HttpSession session, HttpServletRequest request,
			Model model, HttpServletResponse response) {
		logger.info("selectlastScanDate");
		Map<String, Object> scanDate = dashservice.selectlastScanDate(request);

		return scanDate;
	}

	@RequestMapping(value = "/selectNotAction_group", method = { RequestMethod.POST })
	public @ResponseBody List<Object> selectNotAction_group(HttpSession session, HttpServletRequest request,
			Model model, HttpServletResponse response) throws Exception {
		logger.info("selectNotAction_group");
		List<Object> datatypesList = dashservice.selectNotAction_group(request);
		return datatypesList;
	}
	
	@RequestMapping(value = "/dash_dataDetectionList", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> selectDashDataDetectionList(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("selectDashDataDetectionList");
		Map<String, Object> dashDataDetectionList = dashservice.selectDashDataDetectionList(request);
		return dashDataDetectionList;
	}
	
	@RequestMapping(value = "/dash_dataDetectionServerList", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> selectDashDataDetectionServerList(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("selectDashDataDetectionServerList");
		Map<String, Object> dashDataDetectionList = dashservice.selectDashDataDetectionServerList(request);
		return dashDataDetectionList;
	}
	
	@RequestMapping(value = "/dash_dataDetectionPCList", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> selectDashDataDetectionPCList(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("selectDashDataDetectionPCList");
		Map<String, Object> dashDataDetectionList = dashservice.selectDashDataDetectionPCList(request);
		return dashDataDetectionList;
	}
	
	@RequestMapping(value = "/dash_dataCompleteList", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> selectDashDataCompleteList(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("selectDashDataCompleteList");
		Map<String, Object> dashDataCompleteList = dashservice.selectDashDataCompleteList(request);
		return dashDataCompleteList;
	}
	
	@RequestMapping(value = "/dash_dataDetectionItemList", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> selectDashDataDetectionItemList(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("selectDashDataDetectionItemList");
		Map<String, Object> dashDataDetectionItemList = dashservice.selectDashDataDetectionItemList(request);
		return dashDataDetectionItemList;
	}
	
	@RequestMapping(value = "/dash_personal_server_detectionItemList", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> selectDashPersonalServerDetectionItemList(@RequestParam(value="targetList[]") List<String> targetList,
			HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("selectDashPersonalServerDetectionItemList");
		Map<String, Object> DashPersonalServerDetectionItemList = dashservice.selectDashPersonalServerDetectionItemList(request, targetList);
		return DashPersonalServerDetectionItemList;
	}
	
	@RequestMapping(value = "/dash_personal_server_complete", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> selectDashPersonalServerComplete(@RequestParam(value="targetList[]") List<String> targetList,
			HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("selectDashPersonalServerComplete");
		Map<String, Object> DashPersonalServerComplete = dashservice.selectDashPersonalServerComplete(request, targetList);
		return DashPersonalServerComplete;
	}
	
	@RequestMapping(value = "/dash_dataRank", method = { RequestMethod.POST })
	public @ResponseBody List<Object> selectDashDataRank(HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("selectDashDataRank");
		List<Object> datatypesList = dashservice.selectDashDataRank(request);
		return datatypesList;
	}
	
	@RequestMapping(value = "/dash_personal_server_rank", method = { RequestMethod.POST })
	public @ResponseBody List<Map<String, Object>> selectDashPersonalServerRank( 
			HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("dash_personal_server_rank");
		List<String> targetList = new ArrayList<>();
		logger.info("targetList >> " + targetList);
		
		List<Map<String, Object>> datatypesList = dashservice.selectDashPersonalServerRank(request, targetList);
		targetList.clear();
		return datatypesList;
	}
	
	@RequestMapping(value = "/dash_personal_pc_rank", method = { RequestMethod.POST })
	public @ResponseBody List<Map<String, Object>> selectDashPersonalPCRank( 
			HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("dash_personal_pc_rank");
		List<String> targetList = new ArrayList<>();
		logger.info("targetList >> " + targetList);
		
		List<Map<String, Object>> datatypesList = dashservice.selectDashPersonalPCRank(request, targetList);
		targetList.clear();
		return datatypesList;
	}
	
	@RequestMapping(value = "/dash_personal_manager_rank", method = { RequestMethod.POST })
	public @ResponseBody List<Object> selectDashPersonalManagerRank(HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("selectDashPersonalManagerRank");
		List<Object> datatypesList = dashservice.selectDashPersonalManagerRank(request);
		return datatypesList;
	}
	
	@RequestMapping(value = "/dash_dataImple", method = { RequestMethod.POST })
	public @ResponseBody List<Object> selectDashDataImple(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("selectDashDataImple");
		List<Object> datatypesList = dashservice.selectDashDataImple(request);
		return datatypesList;
	}
	
	@RequestMapping(value = "/dash_dataImple_manager", method = { RequestMethod.POST })
	public @ResponseBody List<Object> selectDashDataImpleManager(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("selectDashDataImpleManager");
		List<Object> datatypesList = dashservice.selectDashDataImpleManager(request);
		return datatypesList;
	}
	
	@RequestMapping(value = "/dash_personal_server_imple", method = { RequestMethod.POST })
	public @ResponseBody List<Map<String, Object>> selectDashPersonalServerImple(HttpSession session, HttpServletRequest request, 
			Model model, HttpServletResponse response) throws Exception {
		logger.info("selectDashPersonalServerImple");
		
		List<String> targetList = new ArrayList<>();
		List<Map<String, Object>> datatypesList = dashservice.selectDashPersonalServerImple(request, targetList);
		targetList.clear();
		return datatypesList;
	}
	
	@RequestMapping(value = "/dash_personal_server", method = { RequestMethod.POST })
	public @ResponseBody List<Map<String, Object>> selectDashDataPersonalServer(HttpServletRequest request, Model model) throws Exception{
		logger.info("selectDashDataPersonalServer");
		
		List<Map<String, Object>> dashDataPersonalServer = new ArrayList<>();
		
		dashDataPersonalServer = dashservice.selectDashDataPersonalServer(request);
		
		return dashDataPersonalServer;
	}
	
	@RequestMapping(value = "/dash_personal_PC", method = { RequestMethod.POST })
	public @ResponseBody List<Map<String, Object>> selectDashDataPersonalPC(HttpServletRequest request, Model model) throws Exception{
		logger.info("selectDashDataPersonalPC");
		
		List<Map<String, Object>> dashDataPersonalPC = new ArrayList<>();
		
		dashDataPersonalPC = dashservice.selectDashDataPersonalPC(request);
		
		return dashDataPersonalPC;
	}
	
	@RequestMapping(value = "/dash_all_data", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> selectDashAllData(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("dash_all_data");
		Map<String, Object>  dashDatatypes = dashservice.selectDashAllData(request);
		return dashDatatypes;
	}
	
	@RequestMapping(value = "/dash_dataTodoApproval", method = { RequestMethod.POST })
	public @ResponseBody List<Map<String, Object>> selectDashDataTodoApproval(HttpSession session, HttpServletRequest request, Model model,
		HttpServletResponse response) throws Exception {
		logger.info("selectDashDataTodoApproval");
		List<Map<String, Object>> dashDatatypes = dashservice.selectDashDataTodoApproval(request);
		return dashDatatypes;
	}
	
	@RequestMapping(value = "/dashPieDraw", method = { RequestMethod.POST })
	public @ResponseBody List<Map<String, Object>> selectDashPieDraw(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("selectDashPieDraw");
		List<Map<String, Object>> dashDatatypes = dashservice.selectDashPieDraw(request);
		return dashDatatypes;
	}
	
	@RequestMapping(value = "/dashGridDrawData", method = { RequestMethod.POST })
	public @ResponseBody List<Map<String, Object>> selectDashGridDrawData(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("selectDashGridDrawData");
		List<Map<String, Object>> dashDatatypes = dashservice.selectDashGridDrawData(request);
		return dashDatatypes;
	}
	@RequestMapping(value = "/dashBarDrawData",  method={ RequestMethod.POST }, produces="application/json; charset=UTF-8")
	public @ResponseBody List<Map<String, Object>> selectDashBarDrawData(@RequestBody Map<String, Object> request) {
		logger.info("selectDashBarDrawData");
		
		List<Map<String, Object>> dashDatatypes = null;
		try {
			dashDatatypes = dashservice.selectDashBarDrawData(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dashDatatypes;
	}
	
	@RequestMapping(value = "/detectionTable", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> selectDetectionTable(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("selectDetectionTable");
		Map<String, Object> dashDatatypes = dashservice.selectDetectionTable(request);
		return dashDatatypes;
	}
	
	@RequestMapping(value = "/approvalTable", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> selectApprovalTable(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("selectApprovalTable");
		Map<String, Object> dashDatatypes = dashservice.selectApprovalTable(request);
		return dashDatatypes;
	}
	
	@RequestMapping(value = "/dash_PC_jstree_popup", method = { RequestMethod.POST })
	public @ResponseBody List<Object> selectDashPCJstreePopup(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("selectDashPCJstreePopup");
		List<Object> dashDatatypes = dashservice.selectDashPCJstreePopup(request);
		return dashDatatypes;
	}
	
	
	@RequestMapping(value = "/dashMainNode", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object>selectDashMainNode(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("selectDashMainNode");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("noGroup", "Y");
//			스케줄 현황 노드
			try {
				JsonArray deptList = groupservice.selectDashDeptList(map,request);
				resultMap.put("deptList", deptList);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}catch (Exception e) {
			logger.error(e.toString());
		}
		
		return resultMap;
	}
	
	@RequestMapping(value = "/sessionUpdate", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> sessionCheck(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("sessionUpdate");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultCode", -1);
		map.put("resultMessage", "세션 연장");
		
		return map;
	}
	
	// 관리자 dashboard pc progress
	@RequestMapping(value = "/pi_systemcurrent_progress_pc", method = { RequestMethod.POST })
	public @ResponseBody List<Object> selectSystemCurrentProgressPC(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("pi_systemcurrent_progress_pc");
		List<Object> datatypesList = dashservice.selectSystemCurrentProgressPC(request);
		return datatypesList;
	}
	
	@RequestMapping(value = "/pi_systemcurrent_progress_oneDrive", method = { RequestMethod.POST })
	public @ResponseBody List<Object> selectSystemCurrentProgressOneDrive(HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("pi_systemcurrent_progress_oneDrive");
		List<Object> datatypesList = dashservice.selectSystemCurrentProgressOneDrive(request);
		return datatypesList;
	}
	
	@RequestMapping(value = "/dash_personal_progress_pc", method = { RequestMethod.POST })
	public @ResponseBody List<Map<String, Object>> selectDashPersonalProgressPc(@RequestParam(value="targetList[]") List<String> targetList, 
			HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("selectDashPersonalProgressPc");
		
		logger.info("targetList >> " + targetList);
		
		List<Map<String, Object>> datatypesList = dashservice.selectDashPersonalProgressPc(request, targetList);
		
		return datatypesList;
	}
	
	@RequestMapping(value = "/dash_personal_progress_oneDrive", method = { RequestMethod.POST })
	public @ResponseBody List<Map<String, Object>> selectDashPersonalProgressOneDrive(@RequestParam(value="targetList[]") List<String> targetList, 
			HttpSession session, HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {
		logger.info("selectDashPersonalProgressOneDrive");
		
		logger.info("targetList >> " + targetList);
		
		List<Map<String, Object>> datatypesList = dashservice.selectDashPersonalProgressOneDrive(request, targetList);
		
		return datatypesList;
	}

	@RequestMapping(value = "/lockMemberRequest", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> lockMemberRequest(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("/lockMemberRequest");
		
		// 사용자번호 + 비밀번호로 로그인 하는 경우
		Map<String, Object> memberMap = new HashMap<String, Object>(); 
		try {
			memberMap = service.lockMemberRequest(request);
		}
		catch(Exception e) {
			memberMap.put("resultCode", -4);
			memberMap.put("resultMessage", "시스템오류입니다.\n관리자에게 문의하세요.");
			e.printStackTrace();
		}
		
		return memberMap;
	}
	
	@RequestMapping(value = "/unlockMemberRequest", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> unlockMemberRequest(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("/unlockMemberRequest");
		
		// 사용자번호 + 비밀번호로 로그인 하는 경우
		Map<String, Object> memberMap = new HashMap<String, Object>(); 
		try {
			memberMap = service.unlockMemberRequest(request);
		}
		catch(Exception e) {
			memberMap.put("resultCode", -4);
			memberMap.put("resultMessage", "시스템오류입니다.\n관리자에게 문의하세요.");
			e.printStackTrace();
		}
		
		return memberMap;
	}
	
	@RequestMapping(value = "/unlockMemberRequestManager", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> unlockMemberRequestManager(HttpSession session, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		logger.info("/unlockMemberRequestManager");
		
		// 사용자번호 + 비밀번호로 로그인 하는 경우
		Map<String, Object> memberMap = new HashMap<String, Object>(); 
		try {
			memberMap = service.unlockMemberRequest(request);
		}
		catch(Exception e) {
			memberMap.put("resultCode", -4);
			memberMap.put("resultMessage", "시스템오류입니다.\n관리자에게 문의하세요.");
			e.printStackTrace();
		}
		
		return memberMap;
	}
	
	@RequestMapping(value = "/SelectTargetDash", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody Map<String,Object> insertListProfile(HttpServletRequest request, Model model, HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		logger.info("SelectTargetDash");

		Map<String,Object> result = new HashMap<>();
		try {
			JsonArray deptList = groupservice.SelectTargetDash(request);
			result.put("data", deptList);
			result.put("resultCode", 1);
		}
		catch (Exception e) {
			result.put("resultCode", -1);
			e.printStackTrace();
		}

		return result;

	}

	// 대시보드 2 (새 대시보드)
	@RequestMapping(value = "/picenter_target", method = RequestMethod.GET)
	public String picenterTarget(Locale locale, Model model, HttpServletRequest request) throws Exception {
		logger.info("picenter_target");

		HttpSession session = request.getSession();
		String defaultPage = (String) session.getAttribute("defaultPage");
		model.addAttribute("defaultPage", defaultPage);

		return "dashboard/dash_main_PIC2";
	}
}