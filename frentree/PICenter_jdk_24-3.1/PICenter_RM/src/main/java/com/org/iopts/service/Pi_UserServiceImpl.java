package com.org.iopts.service;

import java.io.IOException;
import java.io.Reader;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random; 
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import jakarta.inject.Inject;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.InternetAddress;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

// import org.apache.http.ParseException;
import org.apache.ibatis.io.Resources;
import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.org.iopts.csp.comm.Config;
import com.org.iopts.csp.comm.CspUtil;
import com.org.iopts.csp.comm.vo.HeaderVo;
import com.org.iopts.csp.comm.vo.ResultVo;
import com.org.iopts.dao.Pi_ScanDAO;
import com.org.iopts.dao.Pi_TargetDAO;
import com.org.iopts.dao.Pi_UserDAO;
import com.org.iopts.exception.dao.piDetectionListDAO;
import com.org.iopts.setting.service.impl.Pi_SetServiceImpl;
import com.org.iopts.util.ReconUtil;
import com.org.iopts.util.ServletUtil;
import com.org.iopts.util.SessionUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class Pi_UserServiceImpl implements Pi_UserService {

	@Value("${recon.id}")
	private String recon_id;

	@Value("${recon.password}")
	private String recon_password;

	@Value("${recon.url}")
	private String recon_url;
	
	@Value("${recon.api.version}")
	private String api_ver;
	
	@Value("${recon.license}")
	private int license;
	
	@Value("${pic_version}")
	private String pic_version;
	
	@Value("${user.key}")
	private String key;
	
	@Value("${send.host}")
	private String send_host;
	
	@Value("${send.port}")
	private String send_port;
	
	@Value("${ldapUrl}")
	private String ldapUrl;
	
	Pi_SetServiceImpl set_service = new Pi_SetServiceImpl();

	private static Logger logger = LoggerFactory.getLogger(Pi_UserServiceImpl.class);
	private Map<String, HttpSession> activeSessions = new ConcurrentHashMap<>();
	
	private static final String ALGORITHM = "AES";

	@Inject
	private Pi_UserDAO dao;
	
	@Inject
	private Pi_ScanDAO scanDao;
	
	@Inject
	private Pi_TargetDAO targetDao;
	
	@Inject
	private piDetectionListDAO detectionListDAO;


	@Override
	public Map<String, Object> getversion() throws Exception {
		Map<String, Object> requestMap = new HashMap<String, Object>();
		requestMap.put("version", pic_version);
		
		Map<String, Object> sessionMap = new HashMap<>();
		
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> version = dao.getversion(requestMap);
		
		map.put("version", version);
		
		sessionMap.put("picenter_data", map);
		
		return map;
	}
	
	@Override
	public Map<String, Object> selectMember(HttpServletRequest request) throws Exception {
		logger.info("selectMember");
		// Session clear
		SessionUtil.closeSession("memberSession");
		SessionUtil.closeSession("picSession");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		String user_no = request.getParameter("user_no"); //입력 ID
		String password = request.getParameter("password"); //입력 PW
		
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("user_no", user_no);
		searchMap.put("password", password);
		
		// User Log 남기기
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "LOGIN");		
		userLog.put("user_ip", clientIP);
		userLog.put("job_info", "Log-In Success_"+user_no);
		userLog.put("logFlag", "0");
		
		Map<String, Object> requestMap = new HashMap<String, Object>();

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> memberMap = dao.selectMember(searchMap);
		Map<String, String> uptMap = new HashMap<>();
		Map<String, Object> picMap = new HashMap<String, Object>();
		
		requestMap.put("version", pic_version);
		Map<String, Object> version = dao.getversion(requestMap);
		picMap.put("version", version);
		
		Map<String, Object> authMap = dao.selectAccountPolicy();
		
		// 사용자번호가 틀린 경우
		if ((memberMap == null) || (memberMap.size() == 0)) {
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", user_no + "는 존재하지 않는 ID 입니다.");
			
			userLog.put("job_info", "Log-In Fail(Invalid User No)");
			dao.insertLog(userLog);
			return resultMap;
		}
		
		boolean pwd_fail_chk = ((int) authMap.get("locks")) != 0; //비밀번호 실패 잠금
		int pwd_fail_cnt = (int) authMap.get("lock_val"); //최대 비밀번호 실패 횟수
		
		// 비밀번호 실패로 인한 잠금
		if(pwd_fail_chk && memberMap.get("LOCK_ACCOUNT").equals("Y")) {
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "비밀번호 "+pwd_fail_cnt+"회 오류로 인해 10분간 사용하실수 없습니다.");
			return resultMap;	
		}
		
		//비밀번호 설정이 되어있지 않을경우
		if(memberMap.get("PASSWORD_CHECK").equals("X") && memberMap.get("ACCESS_IP").equals("")) {
			
			resultMap.put("resultCode", -8);
			resultMap.put("resultMessage", "설정된 비밀번호가 존재하지 않습니다.");
			
			userLog.put("job_info", "Log-In Fail(PassWord,Access IP Null)");
			dao.insertLog(userLog);
			return resultMap;		
			
		}
		// 비밀번호가 틀린 경우
		if (!(memberMap.get("PASSWORD").equals("Y"))) {
			int failed_count = (Integer.parseInt(memberMap.get("FAILED_COUNT").toString())+1);
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "비밀번호가 존재하지 않습니다. 다시 확인 해주세요."+((pwd_fail_chk)==true?" \n로그인 실패 횟수 :"+(failed_count):""));

			userLog.put("job_info", "Log-In Fail(Invalid Password)");
			dao.insertLog(userLog);
			
			if(pwd_fail_chk) { //비밀번호 실패 잠금
				uptMap.put("userNo", user_no);
				uptMap.put("failed_count", String.valueOf(failed_count));
				uptMap.put("lockYn", "N");
				if(failed_count >= pwd_fail_cnt) {
					uptMap.put("lockYn", "Y");
				}
			} else {
				uptMap.put("userNo", user_no);
				uptMap.put("failed_count", "0");
				uptMap.put("lockYn", "Y");
			}
			uptMap.put("logindate", "N");
			logger.info("password error count :: " + failed_count);
			//logger.info("지나감");
			logger.info("uptMap :: "+uptMap);
			logger.info("lock account :: " + dao.updateFailedCount(uptMap));
			return resultMap;			
		} 
		uptMap.put("userNo", user_no);
		uptMap.put("failed_count", "0");
		dao.updateFailedCount(uptMap);
		
		
//		//장기 미사용 계정 잠금
		if(memberMap.get("USE_CHK").toString().equals("Y")) { 
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "장기 미사용으로 인해 계정이 잠겼습니다.\n관리자에게 문의하시기 바랍니다.");
			
			uptMap.put("userNo", user_no);
			uptMap.put("failed_count", "0");
			uptMap.put("lockYn", "Y");
			logger.info("lock account :: " + dao.updateFailedCount(uptMap));
			return resultMap;
		}
		
		//초기화된 비밀번호
		if(Integer.parseInt(memberMap.get("PWD_RESET_STATUS").toString()) == 1) { 
			resultMap.put("resultCode", -5);
			resultMap.put("resultMessage", "초기화된 비밀번호를 변경해주세요.");
			return resultMap;
		}
		
		
		//비밀번호 만료일 체크 - 시작
		if(memberMap.get("CHANGE_CHK").toString().equals("Y")) { 
			resultMap.put("resultCode", -5);
			resultMap.put("resultMessage", "비밀번호 사용일수가 "+(int) authMap.get("change_val")+"일이 지났습니다. 비밀번호를 변경해주세요.");
			return resultMap;
		}
		
		
		Date logindate = (Date) memberMap.get("LOGINDATE");
		Date toDay =  new Date(); // 오늘날짜
		
		Object str = memberMap.get("USER_GRADE");
		System.out.println("user_grade 확인 = "+str);
		
		if(!user_no.equals("frentree")) {
			if ((memberMap.get("USER_GRADE").equals("9"))) { // 관리자이면
				
				String accessIP = (String) memberMap.get("ACCESS_IP");
				
				if(accessIP.equals("")) {
					//resultMap.put("resultCode", -3);
					resultMap.put("resultCode", -2);
					resultMap.put("resultMessage", "최고관리자의 접근IP가 없습니다.");
					
					userLog.put("job_info", "Log-In Fail(Access IP is Null)");
					dao.insertLog(userLog);
					return resultMap;			
				}
				
				String[] accessIPs = accessIP.split(",");
				if(!user_no.equals("frentree")) {
					if (!Arrays.asList(accessIPs).contains(clientIP)) {
						resultMap.put("resultCode", -3);
						resultMap.put("resultMessage", "등록되지 않은 최고관리자 접근IP입니다.");
						userLog.put("job_info", "Log-In Fail(Access IP is Invalid)");
						dao.insertLog(userLog);
						return resultMap;			
					}
				}
			}
		}
		

		
		
		dao.insertLog(userLog);
		dao.updateLoginData(userLog);
		
		uptMap.put("userNo", user_no);
		uptMap.put("failed_count", "0");
		uptMap.put("lockYn", "N");
		uptMap.put("logindate", "Y");
		logger.info("lock account :: " + dao.updateFailedCount(uptMap));
		
		resultMap.put("resultCode", 0);
		resultMap.put("user_no", user_no);
		resultMap.put("resultMessage", "로그인 성공");
		resultMap.put("user_grade", str);
		resultMap.put("member", memberMap);

		// 권한 7은 결재 목록 페이지로 이동
		if ("7".equals(str)) {
			resultMap.put("redirectUrl", "/approval/pi_search_approval_list");
		} else {
			resultMap.put("redirectUrl", "/picenter_target");
		}

		logger.info("resultMap 확인 = "+ resultMap);
		
		HttpSession session = request.getSession();
		
		HttpSession existingSession = activeSessions.get(user_no);

//        if (existingSession != null && !existingSession.equals(session)) {
            // 기존 세션이 존재하고, 새로운 세션과 다를 경우 기존 세션 무효화
//        	WebSocketSessionInvalidateHandler handler = new WebSocketSessionInvalidateHandler();
//            handler.sendMessageToUser(user_no, "다른 곳에서 로그인하여 세션이 종료되었습니다.");

            // 기존 세션 무효화
//            existingSession.invalidate();
//        }
//
//        // 현재 세션을 activeSessions에 등록 (새로운 세션을 저장)
//        activeSessions.put(user_no, session);
		
		// 사용자 세션 등록
		SessionUtil.setSession("memberSession", memberMap);
		SessionUtil.setSession("picSession", picMap);
		
		setHeader(memberMap);
		
		logger.info("로그인 완료");

		return resultMap;
	}
	
	@Override
	public Map<String, Object> changeUser(HttpServletRequest request) throws Exception 
	{
		// Session clear
		String previous_user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		String user_no = request.getParameter("user_no");
//		String user_no = previous_user_no;
		Map<String, Object> requestMap = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> picMap = new HashMap<String, Object>();

		logger.info("login user grade :::: " + user_grade);
		
		if(!user_grade.equals("9")) {
			resultMap.put("resultCode", -9);
			resultMap.put("resultMessage", "올바르지 않은 접근");
			return resultMap;
		}
		
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("user_no", user_no);
		
		// User Log 남기기
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "LOGIN");		
		userLog.put("user_ip", clientIP);
		userLog.put("job_info", previous_user_no + " > " + user_no + "(사용자 변경)");
		userLog.put("logFlag", "0");
		requestMap.put("version", pic_version);
		Map<String, Object> version = dao.getversion(requestMap);
		picMap.put("version", version);

		
		Map<String, Object> memberMap = dao.changeUser(searchMap);
		logger.info(""+memberMap);
		// 사용자번호가 틀린 경우
		if ((memberMap == null) || (memberMap.size() == 0)) {
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", user_no + "는 존재하지 않는 ID 입니다.");
			
			userLog.put("job_info", "Log-In Fail(Invalid User No)");
			dao.insertLog(userLog);
			return resultMap;
		}
		
		// ip 체크
		logger.info((String) memberMap.get("USER_GRADE"));
		logger.info((String) memberMap.get("ACCESS_IP"));
		
		
		if(!previous_user_no.equals("frentree")) {
			if ((memberMap.get("USER_GRADE").equals("9"))) {
				
				String accessIP = (String) memberMap.get("ACCESS_IP");
				
				if(accessIP.equals("")) {
					//resultMap.put("resultCode", -3);
					resultMap.put("resultCode", -2);
					resultMap.put("resultMessage", "최고관리자의 접근IP가 없습니다.");
					
					userLog.put("job_info", "Log-In Fail(Access IP is Null)");
					dao.insertLog(userLog);
					return resultMap;			
				}
				
				String[] accessIPs = accessIP.split(",");
				if (!Arrays.asList(accessIPs).contains(clientIP)) {
					resultMap.put("resultCode", -3);
					resultMap.put("resultMessage", "등록되지 않은 최고관리자 접근IP입니다.");
					userLog.put("job_info", "Log-In Fail(Access IP is Invalid)");
					dao.insertLog(userLog);
					return resultMap;			
				}
			} 
		}

		List<Map<String, Object>> patternList = detectionListDAO.queryCustomDataTypes();
		picMap.put("version", version);
		picMap.put("pattern", patternList);
		picMap.put("patternCnt", patternList.size());
		
		Object str = memberMap.get("USER_GRADE");
		System.out.println("user_grade 확인 = "+str);
		
		dao.insertLog(userLog);
		
		resultMap.put("resultCode", 0);
		resultMap.put("resultMessage", "로그인 성공");
		resultMap.put("user_grade", str);
		resultMap.put("member", memberMap);
		logger.info("resultMap 확인 = "+ resultMap);

		// 사용자 세션 등록
		SessionUtil.closeSession("memberSession");
		SessionUtil.closeSession("picSession");
		SessionUtil.setSession("memberSession", memberMap);
		SessionUtil.setSession("picSession", picMap);
		
		setHeader(memberMap);

		return resultMap;
	}
	
	@Override
	public Map<String, Object> accountMemberSSO(HttpServletRequest request) throws Exception {
		
		// Session clear
		SessionUtil.closeSession("memberSession");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		String user_no = request.getParameter("user_no");

		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("user_no", user_no);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> memberMap = dao.selectMember(searchMap);
		
		String user_grade = (String) memberMap.get("USER_GRADE");
		
		Date logindate = (Date) memberMap.get("LOGINDATE");
		Date toDay =  new Date(); // 오늘날짜
		int request_status = (Integer)memberMap.get("LOCK_STATUS");
		
		if (!(memberMap.get("USER_GRADE").equals("9"))) {
			if(request_status == 2) {
				resultMap.put("resultCode", -9);
				resultMap.put("resultMessage", "잠금 해제가 완료되지 않은 계정입니다.");
				return resultMap;
			}
			
			if(logindate != null && logindate.before(toDay)) {
				resultMap.put("resultCode", -8);
				resultMap.put("resultMessage", "장기 미 사용하여 자동 잠금처리 되었습니다. \n관리자한테 요청해주세요.");
				
				dao.updateLockMember(searchMap);
				
				return resultMap;
			}else {
				dao.updatemember(searchMap);
			}
		}
		return resultMap;
	}

	private void setHeader(Map<String, Object> memberMap) {
		List<Map<String, Object>> headerList = new ArrayList<>();
		Map<String, Object> loginPolicyMap = new HashMap<>();
		
		headerList = dao.setHeader(memberMap);
		loginPolicyMap  = dao.selectAccountPolicy();
		
		SessionUtil.setSession("loginPolicyMap", loginPolicyMap);
		SessionUtil.setSession("headerList", headerList);
		
		Map<String, Object> pageData = dao.setPageData(memberMap);
		
		logger.info("pageData :: " + pageData.toString());
		logger.info("main :: " + pageData.get("MAIN"));
		String mainYn = pageData.get("MAIN").toString();
		SessionUtil.setSession("mainYn", mainYn);
		if ("Y".equals(mainYn)) {
			SessionUtil.setSession("defaultPage", "/picenter_target");
		} else {
			SessionUtil.setSession("defaultPage", pageData.get("URL").toString());
		}
	}

	@Override
	public Map<String, Object> selectSSOMember(HttpServletRequest request) throws Exception 
	{
		// Session clear
		SessionUtil.closeSession("memberSession");
		SessionUtil.closeSession("picSession");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		String user_no = request.getParameter("user_no");
		Map<String, Object> requestMap = new HashMap<String, Object>();
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("user_no", user_no);
		
		// User Log 남기기
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "LOGIN");		
		userLog.put("user_ip", clientIP);
		userLog.put("job_info", "SSO Log-In Success_" + user_no);
		userLog.put("logFlag", "0");

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> memberMap = dao.selectSSOMember(searchMap);
		logger.info(""+memberMap);
		// 사용자번호가 틀린 경우
		if ((memberMap == null) || (memberMap.size() == 0)) {
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", user_no + "는 존재하지 않는 ID 입니다.");
			
			userLog.put("job_info", "Log-In Fail(Invalid User No)");
			dao.insertLog(userLog);
			return resultMap;
		}

		/*Date logindate = (Date) memberMap.get("LOGINDATE");
		Date toDay =  new Date(); // 오늘날짜
		int request_status = (Integer)memberMap.get("LOCK_STATUS");
		
		if(request_status == 2) {
			resultMap.put("resultCode", -9);
			resultMap.put("resultMessage", "장기 미 사용으로 자동 잠금 처리 해제가 완료되지 않은 계정입니다.");
			return resultMap;
		}
		
		if(logindate != null && logindate.before(toDay)) {
			resultMap.put("resultCode", -8);
			resultMap.put("resultMessage", "장기 미 사용하여 자동 잠금처리 되었습니다. \n관리자한테 요청해주세요.");
			dao.updateLockMember(searchMap);
			return resultMap;
		}else {
			dao.updatemember(searchMap);
		}*/
		
		// ip 체크
		logger.info((String) memberMap.get("USER_GRADE"));
		logger.info((String) memberMap.get("ACCESS_IP"));
		if ((memberMap.get("USER_GRADE").equals("9"))) {

			String accessIP = (String) memberMap.get("ACCESS_IP");
			
			if(accessIP.equals("")) {
				//resultMap.put("resultCode", -3);
				resultMap.put("resultCode", -2);
				resultMap.put("resultMessage", "최고관리자의 접근IP가 없습니다.");
				
				userLog.put("job_info", "Log-In Fail(Access IP is Null)");
				dao.insertLog(userLog);
				return resultMap;			
			}

			String[] accessIPs = accessIP.split(",");
			if(!user_no.equals("frentree")) {
				if (!Arrays.asList(accessIPs).contains(clientIP)) {
					resultMap.put("resultCode", -3);
					resultMap.put("resultMessage", "등록되지 않은 최고관리자 접근IP입니다.");
					userLog.put("job_info", "Log-In Fail(Access IP is Invalid)");
					dao.insertLog(userLog);
					return resultMap;			
				}
			}
			

		} 
		
		// 관리자/일반사용자 분리
//		if ((memberMap.get("USER_GRADE").equals("0"))) {
//			resultMap.put("user_grade", );
//		}
		
		// 계정 만료일이 지난 경우
		
		Object str = memberMap.get("USER_GRADE");
		System.out.println("user_grade 확인 = "+str);
		
		dao.insertLog(userLog);
		
		Map<String, Object> picMap = new HashMap<String, Object>();
		requestMap.put("version", pic_version);
		Map<String, Object> version = dao.getversion(requestMap);
		List<Map<String, Object>> patternList = detectionListDAO.queryCustomDataTypes();
		picMap.put("version", version);
		picMap.put("pattern", patternList);
		picMap.put("patternCnt", patternList.size());
		
		resultMap.put("resultCode", 0);
		resultMap.put("resultMessage", "로그인 성공");
		resultMap.put("user_grade", str);
		resultMap.put("member", memberMap);
		logger.info("resultMap 확인 = "+ resultMap);

		// 사용자 세션 등록
		SessionUtil.setSession("memberSession", memberMap);
		SessionUtil.setSession("picSession", picMap);
		
		setHeader(memberMap);

		return resultMap;
	}

	@Override
	public List<Map<String, Object>> selectTeamMember(HttpServletRequest request) throws Exception {

		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("insa_code", SessionUtil.getSession("memberSession", "INSA_CODE"));
		searchMap.put("user_no", SessionUtil.getSession("memberSession", "USER_NO"));
		
		return dao.selectTeamMember(searchMap);
	}
	
	@Override
	public Map<String, Object> selectTeamManager() throws Exception {

		String boss_name = SessionUtil.getSession("memberSession", "USER_NO");
		
		return dao.selectTeamManager(boss_name);
	}
	
	@Override
	@Transactional
	public Map<String, Object> changeAuthCharacter(HttpServletRequest request) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		ServletUtil servletUtil = new ServletUtil(request);
		
		String oldPassword = request.getParameter("oldPassword");
		String newPasswd = request.getParameter("newPasswd");
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_id = SessionUtil.getSession("memberSession", "USER_ID");

		
		Map<String, Object> authMap = dao.selectAccountPolicy();
		
		
		Map<String, Object> map = new HashMap<>();
		int enable = (int) authMap.get("enable");
		int count = 0 ;
		int inc_res = 0;
		
		map = new HashMap<>();
		map.put("userNo",  user_no);
		map.put("changeUserSettingpassword",  newPasswd);
		map.put("beforeUserPassWord",  oldPassword);
		map.put("pwd_change", Integer.parseInt(authMap.get("again_val").toString()));
		
		//Userlog 남기기
		String clientIP = servletUtil.getIp();
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "PASSWORD CHANGE");
		userLog.put("user_ip", clientIP);
		userLog.put("logFlag", "6");
		
		
		if(enable == 1) {
			if(newPasswd  != null) {
				//String passwordRule = "(?=.*\\d)(?=.*[~`!@#$%\\^&*()\\-+=])(?=.*[a-zA-Z]).{1,50}";
				String passwordRule = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~@#$!%*?&])[a-zA-Z\\d~@#$!%*?&]{1,50}$";
				//logger.info(changeUserSettingpassword);
				Pattern pattern = Pattern.compile(passwordRule);
				Matcher matcher = pattern.matcher(newPasswd);
				boolean isValid = matcher.matches();
				
				//System.out.println("Password : "+changeUserSettingpassword +" , isValid :: "+isValid);
				
				int complication = (int) authMap.get("complication");
				int length_chk = (int) authMap.get("length_chk");
				int length_val = (int) authMap.get("length_val");
				
				
				if(length_chk==1 && newPasswd.length()<length_val) {
					resultMap.put("resultCode", -1);
					resultMap.put("resultMessage", "비밀번호는 "+length_val+"자 이상 입력하십시오.");
					
					userLog.put("job_info", "Password Change Fail");
					dao.insertLog(userLog);
					return resultMap;
				}
				if(complication==1 && !isValid) {
					resultMap.put("resultCode", -1);
					resultMap.put("resultMessage", "비밀번호는 숫자/영문자/특수문자를 1개 이상 입력하십시오.");
					
					userLog.put("job_info", "Password Change Fail");
					dao.insertLog(userLog);
					return resultMap;
				}
				
			
			
				int include = (int) authMap.get("include");
				
				// 계정에 패스워드가 들어갈 경우
				if(include == 1) {
					logger.info("include :: "+include);
					
					if(newPasswd.toLowerCase().contains(user_id.toLowerCase())) {
						inc_res = 1;
						logger.info("inc_res  :: "+inc_res);
					}
				}
				
				if((int)authMap.get("agains")==1) {
					count = dao.selectPwdDuplicate(map); //이전 비밀번호중 동일한 비밀번호 count
					logger.info("count :: "+count);
				}
			}
		}
		
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("userNo", user_no);
		searchMap.put("oldPassword", oldPassword);
		searchMap.put("newPasswd", newPasswd);
		
		int ret = -2; 
		if(count==0 && inc_res==0) ret = dao.changeAuthCharacter(searchMap); //비밀번호 변경

		if (ret == 1) {
			dao.insertPwdDuplicate(map);
			
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "비밀번호가 변경되었습니다.");
			userLog.put("job_info", "Password Change Success");
			
			dao.insertLog(userLog);
		}
		else {
			resultMap.put("resultCode", -2);
			resultMap.put("resultMessage", "현재 비밀번호가 다릅니다.");
			userLog.put("job_info", "Password Change Fail");
			
			if(count>0){
				resultMap.put("resultCode", -3);
				resultMap.put("resultMessage", "이전 비밀번호("+authMap.get("again_val")+"회)를 사용하실 수 없습니다.");
			}
			if(inc_res>0){
				resultMap.put("resultCode", -3);
				resultMap.put("resultMessage", "패스워드에 계정이 포함되어있습니다. 다시 확인해주세요.");
				
			}
			
			dao.insertLog(userLog);
		}
		
		return resultMap;
	}
	
	@Override
	public List<Map<String, Object>> selectUserLogList(HttpServletRequest request) throws Exception {

		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String userNo = request.getParameter("userNo");
		String userName = request.getParameter("userName");
		String userIP = request.getParameter("userIP");
		String logFlag = request.getParameter("logFlag");

		
		Map<String, Object> search = new HashMap<String, Object>();
		search.put("userNo", userNo);
		search.put("userName", userName);  
		search.put("userIP", userIP);
		search.put("fromDate", fromDate);
		search.put("toDate", toDate);
		search.put("logFlag", logFlag);
		  
		if(request.getParameter("CONTEXT_COPY")!=null) {
			String[] contextList = request.getParameter("CONTEXT_COPY").toString().split(",");
			search.put("contextList", contextList);
		}
		
		List<Map<String, Object>> resultList= dao.selectUserLogList(search);
		for(int i=0 ; i <resultList.size() ; i++) {
			Map<String, Object> resultMap = resultList.get(i);
			
			resultMap.put("JOB_INFO", set_service.replaceParameter((String) resultMap.get("JOB_INFO")));
			
			resultList.set(i, resultMap);
		}
		

		return resultList;
	}

	@Override
	@Transactional
	public void insertMemberLog(Map<String, Object> userLog) throws Exception {
		
		dao.insertLog(userLog);
	}
	
	@Override
	public String selectAccessIP() throws Exception {

		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String accessIP = dao.selectAccessIP(user_no);

		return accessIP;
	}
	
	@Override
	public List<Map<String, Object>> selectUserGradeList() throws Exception {
		
		List<Map<String, Object>> resultList = new ArrayList<>();
		try {
			resultList = dao.selectUserGradeList();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		return resultList;
	}

	@Override
	public Map<String, Object> selectNotice() throws Exception {

		return dao.selectNotice();
	}
	
	// 공지사항 목록 조회
	@Override
	public List<Map<String, Object>> noticeList(HttpServletRequest request) {
		return dao.noticeList();
	}

	// 공지사항 검색 목록 조회
	@Override
	public List<Map<String, Object>> noticeSearchList(HttpServletRequest request) {
		logger.info("noticeSearchList service");
		
		logger.info("title :: " + request.getParameter("title"));
		logger.info("titcont :: " + request.getParameter("titcont"));
		logger.info("writer :: " + request.getParameter("writer"));
		logger.info("fromDate :: " + request.getParameter("fromDate"));
		logger.info("toDate :: " + request.getParameter("toDate"));
		logger.info("regdateChk :: " + request.getParameter("regdateChk"));
		
		List<Map<String, Object>> resultList = new ArrayList<>();
		try {
			
			Map<String, Object> map = new HashMap<>();
			map.put("title", request.getParameter("title"));
			map.put("titcont", request.getParameter("titcont"));
			map.put("writer", request.getParameter("writer"));
			map.put("fromDate", request.getParameter("fromDate"));
			map.put("toDate", request.getParameter("toDate"));
			map.put("regdateChk", request.getParameter("regdateChk"));
			logger.info("map :: " + map);
			resultList = dao.getStatusList(map);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return resultList;
	}
	
	// 공지사항 등록
	@Override
	public Map<String, Object> noticeInsert(HttpServletRequest request) throws Exception {
		String userNo = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(!user_grade.equals("9")) {
			resultMap.put("resultCode", -9);
			resultMap.put("resultMessage", "올바르지 않은 접근");
			return resultMap;
		}
		
		try {
			String noticeTitle = request.getParameter("notice_title");
			String noticeCon = request.getParameter("notice_con");
			String notice_file_id = request.getParameter("file_number");
			String CHK = request.getParameter("notice_chk"); 
			
			Map<String, Object> input = new HashMap<String, Object>();
			input.put("userNo", userNo);
			input.put("noticeTitle", noticeTitle);
			input.put("noticeCon", noticeCon);
			input.put("notice_file_id", notice_file_id);
			input.put("CHK", CHK);
			
			
			logger.info("input >> " + input);
			
			dao.noticeInsert(input);
			
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "SUCCESS");
		} catch (Exception e) {
			logger.error(e.getMessage());
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "ERROR");
			return resultMap;
		}
		
		return resultMap;
	}
	
//	공지사항 알람 등록 해제
	@Override
	public void noticeAlert(HttpServletRequest request) throws Exception {
		String notice_id = request.getParameter("notice_id");
		String beforeAlertChk = request.getParameter("beforeAlertChk");
		
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("beforeAlertChk", beforeAlertChk);
		
		input.put("status", 0);
		dao.noticeAlert(input); // 등록 취소
		
		if(!beforeAlertChk.equals(notice_id)) { 
			input.put("status", 1);
			input.put("notice_id", notice_id);
			dao.noticeAlert(input); // 등록
		}
		
		
	}
	
	// 공지사항 수정
	@Override
	public Map<String, Object> noticeUpdate(HttpServletRequest request) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String userNo =  SessionUtil.getSession("memberSession", "USER_NO");
		String noticeTitle = request.getParameter("notice_title");
		String noticeCon = request.getParameter("notice_con");
		String IDX = request.getParameter("notice_id");
		String CHK = request.getParameter("notice_chk");
		String file_number = request.getParameter("file_number");
		
		
		Map<String, Object> update = new HashMap<String, Object>();
		update.put("userNo", userNo);
		update.put("noticeTitle", noticeTitle);
		update.put("noticeCon", noticeCon);
		update.put("IDX", IDX);
		update.put("CHK", CHK);
		update.put("file_number", file_number);
		
		logger.info("update >> " + update);
		
		int result = dao.noticeUpdate(update);
		
		if(result > 0) {
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "공지사항 등록 성공");
		}else {
			resultMap.put("resultCode", -2);
			resultMap.put("resultMessage", "공지사항 등록 실패");
			logger.info("올바른 공지사항을 찾을 수 없음.");
		}

		return resultMap;
		
	}

	// 공지사항 삭제
	@Override
	public Map<String, Object> noticeDelete(HttpServletRequest request) throws Exception {
		
		String userNo = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(!user_grade.equals("9")) {
			resultMap.put("resultCode", -9);
			resultMap.put("resultMessage", "올바르지 않은 접근");
			return resultMap;
		}
		try {
			String IDX = request.getParameter("notice_id");

			Map<String, Object> delete = new HashMap<String, Object>();
			delete.put("userNo", userNo);
			delete.put("IDX", IDX);
			
			logger.info("delete >> " + delete);
			
			dao.noticeDelete(delete);		
			
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "SUCCESS");
		} catch (Exception e) {
			logger.error(e.getMessage());
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "ERROR");
			return resultMap;
		}
		
		return resultMap;
	}
	
	@Transactional
	public void changeNotice(HttpServletRequest request) throws Exception {

		String userNo = SessionUtil.getSession("memberSession", "USER_NO");
		String noticeTitle = request.getParameter("notice_title");
		String noticeCon = request.getParameter("notice_con");
		String noticeChk = request.getParameter("rbChk");

		Map<String, Object> input = new HashMap<String, Object>();
		input.put("userNo", userNo);
		input.put("noticeTitle", noticeTitle);
		input.put("noticeCon", noticeCon);
		input.put("noticeChk", noticeChk);
		
		dao.changeNotice(input);
	}
	
	@Override
	public Map<String, Object> selectDownload() throws Exception {

		return dao.selectDownload();
	}
	
	@Override
	public List<Map<String, Object>> downloadList(HttpServletRequest request) {
		return dao.downloadList();
	}

	@Override
	public List<Map<String, Object>> downloadSearchList(HttpServletRequest request) {
		logger.info("downloadSearchList service");
		
		logger.info("title :: " + request.getParameter("title"));
		logger.info("titcont :: " + request.getParameter("titcont"));
		logger.info("writer :: " + request.getParameter("writer"));
		logger.info("fromDate :: " + request.getParameter("fromDate"));
		logger.info("toDate :: " + request.getParameter("toDate"));
		logger.info("regdateChk :: " + request.getParameter("regdateChk"));
		
		List<Map<String, Object>> resultList = new ArrayList<>();
		try {
			
			Map<String, Object> map = new HashMap<>();
			map.put("title", request.getParameter("title"));
			map.put("titcont", request.getParameter("titcont"));
			map.put("writer", request.getParameter("writer"));
			map.put("fromDate", request.getParameter("fromDate"));
			map.put("toDate", request.getParameter("toDate"));
			map.put("regdateChk", request.getParameter("regdateChk"));
			logger.info("map :: " + map);
			resultList = dao.getStatusDownloadList(map);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	@Override
	public Map<String, Object> downloadInsert(HttpServletRequest request) throws Exception {
		String userNo = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(!user_grade.equals("9")) {
			resultMap.put("resultCode", -9);
			resultMap.put("resultMessage", "올바르지 않은 접근");
			return resultMap;
		}
		try {
			String downloadTitle = request.getParameter("download_title");
			String downloadCon = request.getParameter("download_con");
			String download_file_id = request.getParameter("download_number");
			
			Map<String, Object> input = new HashMap<String, Object>();
			input.put("userNo", userNo);
			input.put("downloadTitle", downloadTitle);
			input.put("downloadCon", downloadCon);
			input.put("download_file_id", download_file_id);
			
			logger.info("input >> " + input);
			
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "SUSSE");
			
			dao.downloadInsert(input);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "ERROR");
			return resultMap;
		}
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> downloadUpdate(HttpServletRequest request) throws Exception {
		
		String userNo = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(!user_grade.equals("9")) {
			resultMap.put("resultCode", -9);
			resultMap.put("resultMessage", "올바르지 않은 접근");
			return resultMap;
		}
		
		try {
			String downloadTitle = request.getParameter("download_title");
			String downloadCon = request.getParameter("download_con");
			String IDX = request.getParameter("download_id");
			String file_number = request.getParameter("file_number");
			
			Map<String, Object> update = new HashMap<String, Object>();
			update.put("userNo", userNo);
			update.put("downloadTitle", downloadTitle);
			update.put("downloadCon", downloadCon);
			update.put("IDX", IDX);
			update.put("file_number", file_number);
			
			logger.info("update >> " + update);
			
			dao.downloadUpdate(update);
			
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "SUCCESS");
		} catch (Exception e) {
			logger.error(e.getMessage());
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "ERROR");
			return resultMap;
		}
		
		return resultMap;
	}

	@Override
	public Map<String, Object> downloadDelete(HttpServletRequest request) throws Exception {
		
		String userNo = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(!user_grade.equals("9")) {
			resultMap.put("resultCode", -9);
			resultMap.put("resultMessage", "올바르지 않은 접근");
			return resultMap;
		}
		
		try {
			String IDX = request.getParameter("download_id");
			
			Map<String, Object> delete = new HashMap<String, Object>();
			delete.put("userNo", userNo);
			delete.put("IDX", IDX);
			
			logger.info("delete >> " + delete);
			
			dao.downloadDelete(delete);	
			
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "SUCCESS");
		} catch (Exception e) {
			logger.error(e.getMessage());
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "ERROR");
			return resultMap;
		}
		
		return resultMap;
	}
	
	@Override
	@Transactional
	public Map<String, Object> changeAccessIP(HttpServletRequest request) throws Exception {

		String accessIP = request.getParameter("accessIP");
		String userNo = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(!user_grade.equals("9")) {
			resultMap.put("resultCode", -9);
			resultMap.put("resultMessage", "올바르지 않은 접근");
			return resultMap;
		}
		
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("userNo", userNo);
		input.put("accessIP", accessIP);
		
		dao.changeAccessIP(input);
		
		// User Log 남기기
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", userNo);
		userLog.put("menu_name", "MANAGE ACCESS IP");		
		userLog.put("user_ip", clientIP);
		userLog.put("logFlag", "6");
		
		userLog.put("job_info", "접근 ip 변경");
		
		dao.insertLog(userLog);
		
		resultMap.put("resultCode", 0);
		resultMap.put("resultMessage", "SUCCESS");
		
		return resultMap;
	}

	@Override
	@Transactional
	public void logout(HttpServletRequest request) throws Exception {
		
		HttpSession session = request.getSession(true); 
		if (session.getAttribute("memberSession") != null ) {
			// User Log 남기기 (session이 살아있는 경우)
			ServletUtil servletUtil = new ServletUtil(request);
			String clientIP = servletUtil.getIp();
			String user_no = SessionUtil.getSession("memberSession", "USER_NO");
			
			Map<String, Object> userLog = new HashMap<String, Object>();
			userLog.put("user_no", user_no);
			userLog.put("menu_name", "LOGOUT");		
			userLog.put("user_ip", clientIP);
			userLog.put("job_info", "Log-Out");
			userLog.put("logFlag", "0");
			
			SessionUtil.closeSession("memberSession");
			
			dao.insertLog(userLog);
		}
	}
	
	@Override
	public List<Map<String, Object>> selectManagerList(HttpServletRequest request) throws Exception {
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		List<Map<String, Object>> resultList =  new ArrayList<Map<String, Object>>();
		if(!user_grade.equals("9")) {
			return resultList;
		}
		
		resultList = dao.selectManagerList();
		
		return resultList;
	}

	@Override
	@Transactional
	public Map<String, Object> changeManagerList(HttpServletRequest request) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String userList = request.getParameter("userList");
		
		// User Log 남기기
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		Gson gson = new Gson();
		
		if(!user_grade.equals("9")) {
			resultMap.put("resultCode", -9);
			resultMap.put("resultMessage", "Not User Grade 9");
			
			return resultMap;
		}
		
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		Map<String, Object> userLog = new HashMap<String, Object>();
		
		JsonArray userArray = gson.fromJson(userList, JsonArray.class);

		if (userArray.size() != 0) {
		    for (int i = 0; i < userArray.size(); i++) {
		        JsonObject userMap = userArray.get(i).getAsJsonObject();
		        String userNo = userMap.get("userNo").getAsString();
		        int userGrade = Integer.parseInt(userMap.get("userGrade").getAsString());
		        String userNm = userMap.get("userNm").getAsString();

		        map.put("userNo", userNo);
		        map.put("userGrade", userGrade);
		        dao.changeManagerList(map);

		        userLog.put("user_no", user_no);
		        userLog.put("menu_name", "CHANGE USER DATA");
		        userLog.put("user_ip", clientIP);
		        userLog.put("logFlag", "6");

		        String grade = (userGrade == 0) ? "일반사용자" : 
		                      (userGrade == 1) ? "구성원(SKT정직원)" : 
		                      (userGrade == 2) ? "중간관리자(검색)" : 
		                      (userGrade == 3) ? "중간관리자(조회)" : 
		                      (userGrade == 4) ? "인프라 담당자" : 
		                      (userGrade == 5) ? "서비스담당자" : 
		                      (userGrade == 6) ? "서비스관리자" : 
		                      (userGrade == 7) ? "직책자" : "보안관리자";

		        userLog.put("job_info", "사용자 권한 변경 - " + userNm + " (" + userNo + ") (" + grade + ")");

		        dao.insertLog(userLog);
		    }
		}
		resultMap.put("resultCode", 0);
		resultMap.put("resultMessage", "SUCCESS");
		
		return resultMap;
	}
	
	@Override
	@Transactional
	public Map<String, Object> changeUserData(HttpServletRequest request) throws Exception {
		
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		String sso_no = request.getHeader("SSO_SABUN");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(!user_grade.equals("9")) {
			resultMap.put("resultCode", -9);
			resultMap.put("resultMessage", "Not User Grade 9");
			
			return resultMap;
		}
		
		try {
			
			String phoneRules = "^\\d{3}\\d{3,4}\\d{4}$";
//			String emailRules = "^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z]+)*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z]+)*\\.[a-zA-Z]{2,}$/";
//			String emailRules = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$/";
			String nameRules =  "^[ㄱ-ㅎ가-힣a-zA-Z0-9\\s]+$";
			
			String userNo = request.getParameter("userNo");
			String userNm = request.getParameter("userNm");
			String insaCode = request.getParameter("insaCode");
			//String userPwd = request.getParameter("userPwd");
			String changePhoneNM = request.getParameter("changePhoneNM");
			String accountToDate = request.getParameter("accountToDate");
			String changeEmail = request.getParameter("changeEmail");
			String accountFormDate = request.getParameter("accountFormDate");
			String popupChangeAccessIP = request.getParameter("popupChangeAccessIP");
			String lock_email = request.getParameter("lock_email");
	
			boolean phone_isValid = true;
			
			if(changePhoneNM.equals("") || changePhoneNM == null || changePhoneNM.equals("")) {
				phone_isValid = true;
			}else {
				phone_isValid = changePhoneNM.matches(phoneRules);
			}
			
//			Pattern pattern = Pattern.compile(emailRules);
			
//			boolean email_isValid = changeEmail.matches(emailRules);
			boolean name_isValid = userNm.matches(nameRules);
			
			if(phone_isValid&&name_isValid) {
				map.put("userNo", set_service.replaceParameter(userNo));
				map.put("userNm", set_service.replaceParameter(userNm));
				map.put("insaCode", set_service.replaceParameter(insaCode));
				map.put("changeEmail", set_service.replaceParameter(changeEmail));
				map.put("changePhoneNM", set_service.replaceParameter(changePhoneNM));
				map.put("accountToDate", accountToDate);
				map.put("accountFormDate", accountFormDate);
				map.put("popupChangeAccessIP", set_service.replaceParameter(popupChangeAccessIP));
				map.put("lock_email", set_service.replaceParameter(lock_email));
				
				dao.changeUserDate(map);
				
				// User Log 남기기
				String user_no = SessionUtil.getSession("memberSession", "USER_NO");
				ServletUtil servletUtil = new ServletUtil(request);
				String clientIP = servletUtil.getIp();
				Map<String, Object> userLog = new HashMap<String, Object>();
				userLog.put("user_no", user_no);
				userLog.put("menu_name", "CHANGE USER DATA");		
				userLog.put("user_ip", clientIP);
				userLog.put("logFlag", "6");
				
				userLog.put("job_info", "사용자 정보 변경 - " + userNo);
				
				dao.insertLog(userLog);
				
				resultMap.put("resultCode", 0);
				resultMap.put("resultMessage", "SUCCESS");
			}else {
				resultMap.put("resultCode",-12);
				resultMap.put("resultMessage", "일치하지 않는 정규식");
				
				logger.info("phone_isValid(" + changePhoneNM +")  ::::  " +  phone_isValid);
				logger.info("name_isValid(" + userNm +")  ::::  " +  name_isValid);
//				logger.info("email_isValid(" + changeEmail +")  ::::  " +  email_isValid);
				
				return resultMap;
			}
			
		} catch (Exception e) {
			logger.error("error message :::: " + e.getLocalizedMessage());
			logger.error("error message :::: " + e.getMessage());
			
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage","error");
			
			return resultMap;
		}
		
		
		
		return resultMap;
	}
	
	// 관리자가 사용자 비밀번호 초기화
	@Override
	public Map<String, Object> managerResetPwd(HttpServletRequest request) throws Exception {
		
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(!user_grade.equals("9")) {
			resultMap.put("resultCode", -9);
			resultMap.put("resultMessage", "Not User Grade 9");
			
			return resultMap;
		}
		
		// Session clear
		String user_email = request.getParameter("user_email");
		String user_no = request.getParameter("user_no");
		String user_name = request.getParameter("user_name");
		
		Map<String, Object> mailMap = new HashMap<>();
		
		try {
			mailMap = dao.selectMailUser();
		} catch (Exception e) {
			logger.info("error ::: " + e.getMessage());
		}
		
		Map<String, Object> resultList = new HashMap<String, Object>();
		
		// 영문 + 숫자 랜덤 생성 (8자리)
		String rndPwd = "";
		for (int i = 0; i < 8; i++) {
			int rndVal = (int) (Math.random() * 62);
			if (rndVal < 10) {
				rndPwd += rndVal;
			} else if (rndVal > 35) {
				rndPwd += (char) (rndVal + 61);
			} else {
				rndPwd += (char) (rndVal + 55);
			}
		}
		
		Map<String, Object> resetMap = new HashMap<String, Object>();
		resetMap.put("user_no", user_no);
		resetMap.put("user_email", user_email);
		resetMap.put("password", rndPwd);
		
		/*// User Log 남기기
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", user_no);
		userLog.put("user_ip", clientIP);
		userLog.put("ManagerChangePwd", "0");*/
		
		logger.info("rndPwd ========> " + rndPwd);
		
		if (!user_email.equals("")) {
			
			Properties prop = new Properties();
			prop.put("mail.smtp.host", send_host); 
			prop.put("mail.smtp.port", send_port); 
			prop.put("mail.smtp.auth", "true"); 
			prop.put("mail.smtp.ssl.enable", "true"); 
			
			String title = "";
			String receivermail = "";
			String content = "";
			String sendmail = mailMap.get("COM").toString();
			String passwd = Pi_SetServiceImpl.encrypt(1, mailMap.get("PWD").toString(), key); 
			
			logger.info("passwd ::: " + passwd);
			try {
				
				Session session = Session.getDefaultInstance(prop, new Authenticator() {
				    protected PasswordAuthentication getPasswordAuthentication() {
				        return new PasswordAuthentication(sendmail, passwd);
				    }
				});

				title = "[PICenter] 비밀번호 초기화 안내 메일";
				receivermail = user_email;
				content = user_name + "(" + user_no + ")님의 패스워드가 다음과 같이 초기화 되었습니다. [" + rndPwd + "]";

				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress(sendmail));

				// 수신자 메일 주소
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(receivermail));

	            // Subject
	            message.setSubject(title); //메일 제목을 입력
	            // Text
	            message.setContent(content,"text/html; charset=UTF-8");    // 메일 내용을 입력
	            
	            // send the message
	            Transport.send(message); ////전송
				
	            dao.resetPwd(resetMap);
	            
			} catch (Exception e) {
				logger.info("error :: " + e.getLocalizedMessage());

				resultList.put("resultMessage", "사용자의 이메일 주소가 등록되어있지 않습니다. 관리자에게 문의해주세요.");
				resultList.put("resultCode", -10);
			}
			
		}else {
			resultList.put("resultMessage", "사용자의 이메일 주소가 등록되어있지 않습니다. 관리자에게 문의해주세요.");
			resultList.put("resultCode", -10);
			
			return resultList;
		}

		resultList.put("resultMessage", "SUCCESS");
		resultList.put("resultCode", 0);
		
		return resultList;
	}

	@Override
	@Transactional
	public void userLock(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String userNo = request.getParameter("userNo");
		map.put("userNo", userNo);
		
		dao.userLock(map);
		
		// User Log 남기기
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "LOCK USER");		
		userLog.put("user_ip", clientIP);
		userLog.put("logFlag", "6");
		
		userLog.put("job_info", "사용자 계정 잠금 - " + userNo);
		
		dao.insertLog(userLog);
	}
	
	@Override
	@Transactional
	public void userDelete(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String userNo = request.getParameter("userNo");
		map.put("userNo", userNo);
		
		dao.userDelete(map);
		
		// User Log 남기기
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "DELETE USER");		
		userLog.put("user_ip", clientIP);
		userLog.put("logFlag", "6");
		
		userLog.put("job_info", "사용자 삭제 - " + userNo);
		
		dao.insertLog(userLog);
	}
	
	@Override
	public void createTeam(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String teamName = request.getParameter("teamName");
		String teamCode = request.getParameter("teamCode");
		
		map.put("teamName", teamName);
		map.put("teamCode", teamCode);
		dao.createTeam(map);
		
		// User Log 남기기
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "CREATE TEAM");		
		userLog.put("user_ip", clientIP);
		userLog.put("logFlag", "6");
		
		userLog.put("job_info", "팀 생성 - " + teamName);
		
		dao.insertLog(userLog);
	}
	
	@Override
	public List<Map<String, Object>> selectTeamCode() throws Exception {
		
		return dao.selectTeamCode();
	}
	
	@Override
	public Map<String, Object> chkDuplicateUserNo(HttpServletRequest request) throws Exception {
		
		String userNo = request.getParameter("userNo");
		
		return dao.chkDuplicateUserNo(userNo);
	}

	@Override
	@Transactional
	public void createUser(HttpServletRequest request) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		String userNo = request.getParameter("userNo");
		String password = request.getParameter("password");
		String jikwee = request.getParameter("jikwee");
		String team = request.getParameter("team");
		String jikguk = request.getParameter("jikguk");
		String userName = request.getParameter("userName");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		
		String userEmail = request.getParameter("userEmail");
		String userPhone = request.getParameter("userPhone");
		
		map.put("userNo", userNo);
		map.put("userName", userName);
		map.put("password", password);
		map.put("jikwee", jikwee);
		map.put("jikguk", jikguk);
		map.put("team", team);
		map.put("grade", "0");
		map.put("startDate", startDate);
		map.put("endDate", endDate);

		map.put("userEmail", userEmail);
		map.put("userPhone", userPhone);
		
		dao.createUser(map);
		/*dao.createAccountInfo(map);*/
		
		// User Log 남기기
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "CREATE USER");		
		userLog.put("user_ip", clientIP);
		userLog.put("logFlag", "6");
		
		userLog.put("job_info", "사용자 등록 - " + userName);
		
		dao.insertLog(userLog);
	}
	
	@Override
	public List<Map<String, Object>> selectAccountOfficeList() throws Exception {
		return dao.selectAccountOfficeList();
	}

	@Override
	public void unlockAccount(HttpServletRequest request) {
		Map<String, String> uptMap = new HashMap<String, String>();
		String userNo = request.getParameter("userNo");
		uptMap.put("userNo", userNo);
		uptMap.put("failed_count", "0");
		uptMap.put("lockYn", "N");
		uptMap.put("logindate", "N");
		logger.info("lock account :: " + uptMap.toString());
		logger.info("lock account :: " + dao.updateFailedCount(uptMap));
		
		// User Log 남기기
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "UNLOCK USER");		
		userLog.put("user_ip", clientIP);
		userLog.put("logFlag", "6");
		
		userLog.put("job_info", "사용자 잠금 해제 - " + userNo);
		
		try {
			dao.insertLog(userLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, Object> getLicenseDetail() throws Exception {
		
		logger.info("getLicenseDetail START");
		Map<String, Object> resultMap = new HashMap<>();
		try {
			
			int ap_no = 0;
			
			Properties properties = new Properties();
			String resource = "/property/config.properties";
			Reader reader = Resources.getResourceAsReader(resource);
			
			properties.load(reader);
			
			String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no+1)) ;
			String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no+1)) ;
			String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no+1)) ;
			
			String requestUrl = recon_url + "/"+api_ver+"/licenses" ;
			Map<String, Object> connectionData = new HashMap<>();
			connectionData = connectRecon(requestUrl, this.recon_id, this.recon_password, "GET", "");
			
			String company = "";
			String expire = "";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			JsonParser parser = new JsonParser();
			JsonObject obj = (JsonObject) parser.parse(connectionData.get("resultData").toString());
			
			company = obj.get("company").toString().replace("\"", "");
			Long linuxTm = Long.parseLong(obj.get("expires").toString().replaceAll("\"", ""));
			Date date = new Date();
			date.setTime(linuxTm*1000);
			expire = sdf.format(date);
			
			resultMap.put("company", company);
			resultMap.put("expire", expire);
			
			logger.info("company ::: " + company);
			logger.info("expire ::: " + expire);
			
			Map<String, Object> dbMap = new HashMap<>();
			dbMap.put("ap_no", 0);
			List<Map<String, String>> targetList = new ArrayList<Map<String,String>>();
			List<Map<String, Object>> dbTargetList = targetDao.selectAllUseTarget(dbMap);
			
			for(Map<String, Object> dbTarget : dbTargetList) {
				Map<String, String> map = new HashMap<>();
				map.put("name", dbTarget.get("NAME").toString());
				map.put("type", "할당되지 않음");
				targetList.add(map);
			}
			
			JsonArray targetsArray = new JsonArray();
			if(obj.has("targets")) {
				targetsArray = (JsonArray) parser.parse(obj.get("targets").toString().replaceAll("\"", ""));
				for(int i=0; i<targetsArray.size(); i++) {
					JsonObject targetObj = (JsonObject) parser.parse(targetsArray.get(i).toString().replaceAll("\"", ""));
					String name = targetObj.get("name").toString().replace("\"", "");
					String type = targetObj.get("type").toString().replace("\"", "");
					for(int index=0; index<targetList.size(); index++) {
						if(name.equals(targetList.get(index).get("name"))) {
							targetList.get(index).put("type", type);
							break;
						}
					}
				}
			}
			
			List<Map<String, String>> summaryList = new ArrayList<>();
			
			JsonArray summaryArray = (JsonArray) parser.parse(obj.get("summary").toString());
			for(int i=0; i<2; i++) {
				Map<String, String> summary = new HashMap<>();
				if(i == 0) {
					summary.put("type", "Master Server");
					summary.put("total", "1/1");
				} else {
					summary.put("type", "Server");
					for(int index=0; index<summaryArray.size(); index++) {
						JsonObject summaryObj = (JsonObject) parser.parse(summaryArray.get(index).toString());
						String type = summaryObj.get("type").toString().replace("\"", "");
						if("server".equals(type)) {
							summary.put("current", targetsArray.size()+"");
							summary.put("total", dbTargetList.size()+"");
						}
					}
				}
				summaryList.add(summary);
				
			}
			
			resultMap.put("resultCode", "00");
			resultMap.put("resultMsg", "success");
			resultMap.put("summaryList", summaryList);
			resultMap.put("targetList", targetList);
		} catch (Exception e) {
			resultMap.put("resultCode", "99");
			e.printStackTrace();
		}
		return resultMap;
	}
	
	@Override
	public Map<String, Object> getLicenseDetail(HttpServletRequest request) throws Exception{
		
		Map<String, Object> resultMap = new HashMap<>();
		int ap_no = Integer.parseInt(request.getParameter("ap_num"));
		
		Properties properties = new Properties();
		String resource = "/property/config.properties";
		Reader reader = Resources.getResourceAsReader(resource);
		
		properties.load(reader);
		
		String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no+1)) ;
		String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no+1)) ;
		String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no+1)) ;
		
		try {
			String requestUrl = recon_url + "/"+api_ver+"/licenses" ;
			logger.info("requestUrl :: " + requestUrl);
			Map<String, Object> connectionData = new HashMap<>();
			connectionData = connectRecon(requestUrl, recon_id, recon_password, "GET", "");
			
			String company = "";
			String expire = "";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			JsonParser parser = new JsonParser();
			JsonObject obj = (JsonObject) parser.parse(connectionData.get("resultData").toString());
			
			company = obj.get("company").toString().replace("\"", "");
			Long linuxTm = Long.parseLong(obj.get("expires").toString().replaceAll("\"", ""));
			Date date = new Date();
			date.setTime(linuxTm*1000);
			expire = sdf.format(date);
			
			resultMap.put("company", company);
			resultMap.put("expire", expire);
			
			logger.info("company ::: " + company);
			logger.info("expire ::: " + expire);
			
			Map<String, Object> dbMap = new HashMap<>();
			dbMap.put("ap_no", ap_no);
			
			List<Map<String, String>> targetList = new ArrayList<Map<String,String>>();
			List<Map<String, Object>> dbTargetList = targetDao.selectAllUseTarget(dbMap);
			
			
			for(Map<String, Object> dbTarget : dbTargetList) {
				Map<String, String> map = new HashMap<>();
				map.put("name", dbTarget.get("NAME").toString());
				map.put("type", "할당되지 않음");
				targetList.add(map);
			}
			
			JsonArray targetsArray = new JsonArray();
			if(obj.has("targets")) {
				targetsArray = (JsonArray) parser.parse(obj.get("targets").toString().replaceAll("\"", ""));
				for(int i=0; i<targetsArray.size(); i++) {
					JsonObject targetObj = (JsonObject) parser.parse(targetsArray.get(i).toString().replaceAll("\"", ""));
					String name = targetObj.get("name").toString().replace("\"", "");
					String type = targetObj.get("type").toString().replace("\"", "");
					for(int index=0; index<targetList.size(); index++) {
						if(name.equals(targetList.get(index).get("name"))) {
							targetList.get(index).put("type", type);
							break;
						}
					}
				}
			}
			
			List<Map<String, String>> summaryList = new ArrayList<>();
			
			JsonArray summaryArray = (JsonArray) parser.parse(obj.get("summary").toString());
			for(int i=0; i<2; i++) {
				Map<String, String> summary = new HashMap<>();
				if(i == 0) {
					summary.put("type", "Master Server");
					summary.put("total", "1/1");
				} else {
					summary.put("type", "Server");
					for(int index=0; index<summaryArray.size(); index++) {
						JsonObject summaryObj = (JsonObject) parser.parse(summaryArray.get(index).toString());
						String type = summaryObj.get("type").toString().replace("\"", "");
						if("server".equals(type)) {
							summary.put("current", targetsArray.size()+"");
							summary.put("total", dbTargetList.size()+"");
						}
					}
				}
				summaryList.add(summary);
				
			}
			
			resultMap.put("resultCode", "00");
			resultMap.put("resultMsg", "success");
			resultMap.put("summaryList", summaryList);
			resultMap.put("targetList", targetList);
			resultMap.put("dbTargetList", dbTargetList);
		} catch (Exception e) {
			resultMap.put("resultCode", "99");
			e.printStackTrace();
		}
//		logger.info("["+ap_no+"]"+resultMap.toString());
		return resultMap;
	}
	
	
	public Map<String, Object> connectRecon(String requestUrl, String recon_id, String recon_password, String method, String requestData) {
		Map<String, Object> resultMap = new HashMap<>();
		
		ReconUtil reconUtil = new ReconUtil();
		
		String dtm = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		
		try {
			
			Map<String, String> conHistMap = new HashMap<>();
			String seq = String.format("%03d", scanDao.getConnectHistSeq());
			
			conHistMap.put("id", dtm+seq);
			conHistMap.put("recon_id", this.recon_id);
			conHistMap.put("url", requestUrl);
			conHistMap.put("method", method);
			conHistMap.put("req_data", requestData);
			
			logger.info(conHistMap.toString());
			if(scanDao.insConnectHist(conHistMap) > 0) {
				
				Map<String, Object> reconResultMap = reconUtil.getServerData(recon_id, recon_password, requestUrl, method, requestData);
				conHistMap.put("rsp_cd", reconResultMap.get("HttpsResponseCode").toString());
				conHistMap.put("rsp_msg", reconResultMap.get("HttpsResponseMessage").toString());
				
				logger.info("UPDATE DB Schedule result : " + scanDao.uptConnectHist(conHistMap));
				
				logger.info("Get License information result Code : " + reconResultMap.get("HttpsResponseCode"));
				logger.info("Get License information result Message : " + reconResultMap.get("HttpsResponseMessage"));
				logger.info("Get License information result ResponseData : " + reconResultMap.get("HttpsResponseData"));
				
				resultMap.put("resultCode", reconResultMap.get("HttpsResponseCode"));
				resultMap.put("resultMsg", reconResultMap.get("HttpsResponseMessage"));
				resultMap.put("resultData", reconResultMap.get("HttpsResponseData"));
				
			}
		} catch (Exception e) {
			logger.error("Connection error: " + e.getMessage(), e);
			resultMap.put("resultCode", "99");
			resultMap.put("resultMsg", "connection error");
			resultMap.put("resultData", "처리중 에러가 발생하였습니다.");
		}
		return resultMap;
	}
	

	@Override
	public List<Map<String, Object>> getLogFlagList() throws Exception {
		return dao.getLogFlagList();
	}
	
	@Override
	public List<Map<String, Object>> getGradeFlagList() throws Exception {
		return dao.getGradeFlagList();
	}
	

    // 비밀번호 초기화 취소
    @Override
    public Map<String, Object> reset_sms_code(HttpServletRequest request) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        ServletUtil servletUtil = new ServletUtil(request);
        
        String user_no = request.getParameter("user_no");
        String user_name = request.getParameter("user_name");
        
        Map<String, Object> searchMap = new HashMap<String, Object>();
        searchMap.put("user_no", user_no);
        searchMap.put("user_name", user_name);
        
        dao.reset_sms_code(searchMap);

        return resultMap;
    }
	
	@Override
	public Map<String, Object> changeResetPwd(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ServletUtil servletUtil = new ServletUtil(request);
		
		String oldPassword = request.getParameter("oldPassword");
		String newPasswd = request.getParameter("newPasswd");
		String user_no = request.getParameter("user_no");
		String sso_no = request.getHeader("SSO_SABUN");
		String sso_id = request.getHeader("SSO_LOGINID");
		
		Map<String, Object> authMap = dao.selectAccountPolicy();
		int enable = (int) authMap.get("enable");
		int count = 0 ;
		int inc_res = 0;
		
		String passwordRule = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~@#$!%*?&])[a-zA-Z\\d~@#$!%*?&]{1,50}$";
		//String passwordRule = "(?=.*\\d)(?=.*[~`!@#$%\\^&*()\\-+=])(?=.*[a-zA-Z]).{1,50}";
		
		Map<String, Object> map = new HashMap<>();
		map.put("userId",  user_no);
		map.put("changeUserSettingpassword",  newPasswd);
		map.put("beforeUserPassWord",  oldPassword);
		map.put("pwd_change", Integer.parseInt(authMap.get("again_val").toString())); //이전 비밀번호 저장
		
		// User Log 남기기
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "PASSWORD CHANGE");		
		userLog.put("user_ip", servletUtil.getIp());
		userLog.put("logFlag", "6");
		
		if(enable == 1) {
			int complication = (int) authMap.get("complication");
			int length_chk = (int) authMap.get("length_chk");
			int length_val = (int) authMap.get("length_val");
			
			if(newPasswd != null) {
				Pattern pattern = Pattern.compile(passwordRule);
				Matcher matcher = pattern.matcher(newPasswd);
				boolean isValid = matcher.matches();
				
				if(length_chk==1 && newPasswd.length()<length_val) {
					resultMap.put("resultCode", -1);
					resultMap.put("resultMessage", "비밀번호는 "+length_val+"자 이상 입력하십시오.");
					
					userLog.put("job_info", "Password Change Fail");
					
					dao.insertLog(userLog);
					return resultMap;
				}
				
				if(complication==1 &&!isValid) {
					resultMap.put("resultCode", -1);
					resultMap.put("resultMessage", "비밀번호는 숫자/영문자/특수문자를 1개 이상 입력하십시오.");
					userLog.put("job_info", "Password Change Fail");
					
					dao.insertLog(userLog);
					return resultMap;
				}
			}
			
			
			int include = (int) authMap.get("include");
			
			// 계정에 패스워드가 들어갈 경우
			if(include == 1) {
				logger.info("include :: "+include);
				
				if(newPasswd.toLowerCase().contains(user_no.toLowerCase())) {
					inc_res = 1;
					logger.info("inc_res  :: "+inc_res);
				}
			}
			
			if((int)authMap.get("agains")==1) {
				count = dao.selectPwdDuplicate(map); //이전 비밀번호중 동일한 비밀번호 count
				logger.info("count :: "+count);
			}
		}
		
		
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("user_no", user_no);
		searchMap.put("oldPassword", oldPassword);
		searchMap.put("newPasswd", newPasswd);
		
//		if((sso_no != null && !sso_no.equals(user_no)) || (sso_id != null && !sso_no.equals(sso_id))) {
//			resultMap.put("resultCode", -7);
//			resultMap.put("resultMessage", "사용자 정보가 다릅니다.");
//			return resultMap;
//		}
		int ret = 0;
		if(count==0 && inc_res==0) ret = dao.changeResetPwd(searchMap);

		

		if (ret == 1) {
			dao.insertPwdDuplicate(map);
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "비밀번호가 변경되었습니다. 다시 로그인 해주세요");
			userLog.put("job_info", "Password Change Success");
			
			dao.insertLog(userLog);
		}
		else {
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "현재 비밀번호가 다릅니다.");
			userLog.put("job_info", "Password Change Fail");
			
			dao.insertLog(userLog);
			
			if(count>0){
				resultMap.put("resultCode", -3);
				resultMap.put("resultMessage", "이전 비밀번호("+authMap.get("again_val")+"회)를 사용하실 수 없습니다.");
			}
			if(inc_res>0){
				resultMap.put("resultCode", -3);
				resultMap.put("resultMessage", "패스워드에 계정이 포함되어있습니다. 다시 확인해주세요.");
			}
			
			
		}
		SessionUtil.closeSession("memberSession");
		SessionUtil.closeSession("picSession");
		return resultMap;
	}

	/**
     * 전달된 파라미터에 맞게 난수를 생성한다
     * @param len : 생성할 난수의 길이
     * @param dupCd : 중복 허용 여부 (1: 중복허용, 2:중복제거)
     * 
     * Created by 닢향
     * http://niphyang.tistory.com
     */
    public String numberGen(int len, int dupCd) {
        
        Random rand = new Random();
        String numStr = ""; //난수가 저장될 변수
        
        for(int i=0;i<len;i++) {
            
            //0~9 까지 난수 생성
            String ran = Integer.toString(rand.nextInt(10));
            
            if(dupCd==1) {
                //중복 허용시 numStr에 append
                numStr += ran;
            }else if(dupCd==2) {
                //중복을 허용하지 않을시 중복된 값이 있는지 검사한다
                if(!numStr.contains(ran)) {
                    //중복된 값이 없으면 numStr에 append
                    numStr += ran;
                }else {
                    //생성된 난수가 중복되면 루틴을 다시 실행한다
                    i-=1;
                }
            }
        }
        return numStr;
    }
    
    @Override
	public Map<String, Object> submitSmsLogin(HttpServletRequest request) throws Exception {

		// Session clear
		SessionUtil.closeSession("memberSession");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		String user_no = request.getParameter("user_no");
		String sms_code = request.getParameter("sms_code");
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("user_no", user_no);
		searchMap.put("sms_code", sms_code);
		
		logger.info("sms_code ========> " + sms_code);

		
		// User Log 남기기
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "LOGIN");		
		userLog.put("user_ip", clientIP);
		userLog.put("job_info", "Log-In Success_"+user_no );
		userLog.put("logFlag", "0");

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> memberMap = dao.selectSmsMember(searchMap);
		Map<String, String> uptMap = new HashMap<>();
		
		logger.info("memeber ========> " + memberMap);

		// 사용자번호가 틀린 경우
		if ((memberMap == null) || (memberMap.size() == 0)) {
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "인증번호가 틀렸습니다.");
			
			userLog.put("job_info", "Log-In Fail(Invalid User No)");
			dao.insertLog(userLog);
			return resultMap;
		} else {
			
			searchMap.put("smsCode", null);
			dao.updateSMSCode(searchMap);
		}
		// 계정이 잠긴 경우
		if(memberMap.get("LOCK_ACCOUNT").equals("Y")) {
			resultMap.put("resultCode", -2);
			resultMap.put("resultMessage", "비밀번호 5회 오류도 10분간 사용하실수 없습니다.\n관리자한테 요청해주세요.");
			return resultMap;	
		}

		// ip 체크
		logger.info((String) memberMap.get("USER_GRADE"));
		logger.info((String) memberMap.get("ACCESS_IP"));
		if ((memberMap.get("USER_GRADE").equals("9"))) {

			String accessIP = (String) memberMap.get("ACCESS_IP");
			
			if(accessIP.equals("")) {
				resultMap.put("resultCode", -3);
				resultMap.put("resultMessage", "최고관리자의 접근IP가 없습니다.");
				
				userLog.put("job_info", "Log-In Fail(Access IP is Null)");
				dao.insertLog(userLog);
				return resultMap;			
			}

			String[] accessIPs = accessIP.split(",");
			if (!Arrays.asList(accessIPs).contains(clientIP)) {
				resultMap.put("resultCode", -3);
				resultMap.put("resultMessage", "등록되지 않은 최고관리자 접근IP입니다.");
				userLog.put("job_info", "Log-In Fail(Access IP is Invalid)");
				dao.insertLog(userLog);
				return resultMap;			
			}

		} 
		
		// 관리자/일반사용자 분리
//		if ((memberMap.get("USER_GRADE").equals("0"))) {
//			resultMap.put("user_grade", );
//		}
		
		// 계정 만료일이 지난 경우
		
		Object str = memberMap.get("USER_GRADE");
		System.out.println("user_grade 확인 = "+str);
		
		dao.insertLog(userLog);
		
		uptMap.put("userNo", user_no);
		uptMap.put("failed_count", "0");
		uptMap.put("lockYn", "N");
		uptMap.put("logindate", "N");
		logger.info("lock account :: " + dao.updateFailedCount(uptMap));
		
		resultMap.put("resultCode", 0);
		resultMap.put("resultMessage", "로그인 성공");
		resultMap.put("user_grade", str);
		resultMap.put("member", memberMap);
		logger.info("resultMap 확인 = "+ resultMap);
		
		// 사용자 세션 등록
		SessionUtil.setSession("memberSession", memberMap);
		
		setHeader(memberMap);

		return resultMap;
	}
    
    @Override
	public Map<String, Object> authSMSResend(HttpServletRequest request) throws Exception {

		
		String user_no = request.getParameter("user_no");
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("user_no", user_no);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String smsCode = numberGen(6, 1);
		searchMap.put("smsCode", smsCode);
		
		dao.updateSMSCode(searchMap);
		
		resultMap.put("resultCode", 0);
		resultMap.put("resultMessage", "SMS 인증번호 재발송");
		logger.info("resultMap 확인 = "+ resultMap);
		
		//SessionUtil.setSession("memberSession", memberMap);

		return resultMap;
	}
    
    @Override
   	public Map<String, Object> authSMSCancel(HttpServletRequest request) throws Exception {

   		
   		String user_no = request.getParameter("user_no");
   		Map<String, Object> searchMap = new HashMap<String, Object>();
   		searchMap.put("user_no", user_no);
   		
   		Map<String, Object> resultMap = new HashMap<String, Object>();
   		
   		searchMap.put("smsCode", null);
   		
   		dao.updateSMSCode(searchMap);
   		
   		resultMap.put("resultCode", 0);
   		resultMap.put("resultMessage", "SMS 인증 취소");
   		logger.info("resultMap 확인 = "+ resultMap);
   		
   		//SessionUtil.setSession("memberSession", memberMap);

   		return resultMap;
   	}
    
	@Override
	public Map<String, Object> sumApproval() throws Exception {
		Map<String, Object> result = new HashMap<>();
		SessionUtil.getSession("memberSession", "USER_NO");
		
		Map<String, String> map = new HashMap<>();
		map.put("userNo", SessionUtil.getSession("memberSession", "USER_NO"));
		result = dao.sumApproval(map);
		
		return result;
	}
    
 	
 	@Override
	@Transactional
	public Map<String, Object> changeUserSettingData(HttpServletRequest request) throws Exception {
 		
 		Map<String, Object> resultMap = new HashMap<String, Object>();
 		try {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> authMap = dao.selectAccountPolicy();
		
		
		String userNo =  SessionUtil.getSession("memberSession", "USER_NO");
		String user_id = SessionUtil.getSession("memberSession", "USER_ID");
		
		//String passwordRule = "(?=.*\\d)(?=.*[~`!@#$%\\^&*()\\-+=])(?=.*[a-zA-Z]).{1,50}";
		String passwordRule = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~@#$!%*?&])[a-zA-Z\\d~@#$!%*?&]{1,50}$";
		String changeUserSettingNM = request.getParameter("changeUserSettingNM");
		String changeUserSettingPhoneNM = request.getParameter("changeUserSettingPhoneNM");
		String beforeUserPassWord = request.getParameter("beforeUserPassWord");
		String changeUserSettingEmail = request.getParameter("changeUserSettingEmail");
		String changeUserSettingpassword = request.getParameter("changeUserSettingpassword");
		
		//Userlog 남기기
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", userNo);
		userLog.put("menu_name", "CHANGE USER DATA");		
		userLog.put("user_ip", clientIP);
		userLog.put("logFlag", "6");
		
		map.put("userNo", SessionUtil.getSession("memberSession", "USER_NO"));
		map.put("changeUserSettingNM", changeUserSettingNM);
		map.put("changeUserSettingPhoneNM", changeUserSettingPhoneNM);
		map.put("changeUserSettingEmail", changeUserSettingEmail);
		map.put("beforeUserPassWord", beforeUserPassWord);
		map.put("changeUserSettingpassword", changeUserSettingpassword);
		
		logger.info("map :: "+map.toString());
		
		int enable = (int) authMap.get("enable");
		
		if(enable == 1) {
				//비밀번호가 바뀌었을 경우
				if(changeUserSettingpassword != null &&!changeUserSettingpassword.equals("")) {
					//logger.info(changeUserSettingpassword);
					Pattern pattern = Pattern.compile(passwordRule);
					Matcher matcher = pattern.matcher(changeUserSettingpassword);
					boolean isValid = matcher.matches();
					
					//System.out.println("Password : "+changeUserSettingpassword +" , isValid :: "+isValid);
					
					int complication = (int) authMap.get("complication");
					int length_chk = (int) authMap.get("length_chk");
					int length_val = (int) authMap.get("length_val");
					
					if(length_chk==1 && changeUserSettingpassword.length()<length_val) {
						resultMap.put("resultCode", -1);
						resultMap.put("resultMessage", "비밀번호는 "+length_val+"자 이상 입력하십시오.");
						
						userLog.put("job_info", "사용자 정보 변경 실패 - "+userNo);
						dao.insertLog(userLog);
						return resultMap;
					}
					if(complication==1 && !isValid) {
						resultMap.put("resultCode", -1);
						resultMap.put("resultMessage", "비밀번호는 숫자/영문자/특수문자를 1개 이상 입력하십시오.");
						
						userLog.put("job_info", "사용자 정보 변경 실패 - "+userNo);
						dao.insertLog(userLog);
						return resultMap;
					}
				
				int include = (int) authMap.get("include");
				// 계정에 패스워드가 들어갈 경우
				if(include == 1) {
					logger.info("include :: "+include);
					if(changeUserSettingpassword.toLowerCase().contains(user_id.toLowerCase())) {
						resultMap.put("resultCode", -3);
						resultMap.put("resultMessage", "패스워드에 계정이 포함되어있습니다. 다시 확인해주세요.");
						
						userLog.put("job_info", "사용자 정보 변경 실패 - "+userNo);
						dao.insertLog(userLog);
						return resultMap;
					}
				}
				
				
				if((int)authMap.get("agains")==1) {
					map.put("pwd_change", Integer.parseInt(authMap.get("again_val").toString()));
					int count = dao.selectPwdDuplicate(map);
					//logger.info("count :: "+count);
					if(count>0){
						resultMap.put("resultCode", -3);
						resultMap.put("resultMessage", "이전 비밀번호("+authMap.get("again_val")+"회)를 사용하실 수 없습니다.");
						
						userLog.put("job_info", "사용자 정보 변경 실패 - "+userNo);
						dao.insertLog(userLog);
						return resultMap;
					}
				}
			}
		}
		
		
		int resultInt = dao.changeUserSettingDate(map);
		
		if(resultInt > 0) {
			resultMap.put("resultCode", 0);
			if(changeUserSettingpassword == null || changeUserSettingpassword.equals("")) resultMap.put("resultCode", 1);
			resultMap.put("resultMessage", "회원 정보 수정 성공"); 
			
			userLog.put("job_info", "사용자 정보 변경 성공 - "+userNo);
			dao.insertLog(userLog);
			dao.insertPwdDuplicate(map);
			
		}else { // 회원 정보 불일치
			resultMap.put("resultCode", -2);
			resultMap.put("resultMessage", "이전 비밀번호 불일치");
			
			userLog.put("job_info", "사용자 정보 변경 실패 - "+userNo);
			dao.insertLog(userLog);
			return resultMap;
		}
		
		
 		} catch (Exception e) {
			e.printStackTrace();
		}
		
 		return resultMap;
	}
 	
 	@Override
	public List<Map<String, Object>> selectMemberTeam(HttpServletRequest request) throws Exception {
 		return dao.selectMemberTeam();
	}
 	
 	@Override
	public List<Map<String, Object>> selectPCAdmin(HttpServletRequest request) throws Exception {
 		String user_no = request.getParameter("user_no");
 		String[] user_nos = user_no.split(",");
 		Map<String, Object> searchMap = new HashMap<>();
 		
 		searchMap.put("user_no", user_nos);
 		
 		return dao.selectPCAdmin(searchMap);
	}
	
	/*@Override
	public Map<String, Object> unlockMemberRequest(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String user_no = request.getParameter("user_no");
		String user_name = request.getParameter("user_name");
		String user_phone = request.getParameter("user_phone");
		int lock_staus = Integer.parseInt(request.getParameter("lock_staus"));
		String unlock_reson = request.getParameter("unlock_reson");
		Map<String, Object> requestMap = new HashMap<String, Object>();
		
		String userNo = SessionUtil.getSession("memberSession", "USER_NO");
		String userName = SessionUtil.getSession("memberSession", "USER_NAME");
		
		requestMap.put("user_no", user_no);
		requestMap.put("lock_staus", lock_staus);
		requestMap.put("unlock_reson", unlock_reson);
		requestMap.put("userNo", userNo);
		
		int ret = dao.unlockMemberRequest(requestMap);
		List<Map<String, String>> adminMap = dao.selectAdminMember(requestMap);
		String phone = "";
		String title = "";
		
		if (ret == 1) {
			
			if(lock_staus == 2) {
				resultMap.put("resultCode", 0);
				resultMap.put("resultMessage", " 사용자의 계정 잠금 해제 신청이 등록 되었습니다.");
				
			}else {
				
				if(!user_name.equals(userName)) {
					
					phone = user_phone.replaceAll("-", "");
					title = "[PIMC] 계정 잠금 해제 \n" + "["+user_name+"님의 계정 잠금이 해제 되었습니다.]";
					title = "[PIMC] "+user_name+"("+user_no+")님의 계정잠금 해제가 정상 처리되었습니다.";
					
					if(!phone.substring(0,3).equals("010")) {
						resultMap.put("resultMessage", "계정잠김은 해제 했으나, 해당 전화번호로 문자 발송이 불가합니다.");
						resultMap.put("resultCode", -10);
						
						logger.info("사용자 전화번호가 지역번호로 등록됨. : 해제 안내 문자 발송 실패_" +user_name + "_" + user_no);
						
					}else {
						String[][] paramLt =
							{{"CONSUMER_ID","C00561"},{"RPLY_PHON_NUM","0264008842"},{"TITLE",title},{"PHONE",phone}}; 
						getVo(paramLt);
					}
					
					
					
						
				}
				
				String admin_user = userName;
				String user = user_name;
				
				for (Map<String, String> map : adminMap) {
					
					String userPhone = map.get("USER_PHONE");
					
					phone = userPhone.replaceAll("-", "");
					title = "[PIMC] 계정 잠금 해제 \n" + "["+admin_user+"님이 "+user+"님의 잠금을 해제 하였습니다.]";
					title = "[PIMC] "+user+"("+user_no+")님의 계정잠금 해제가 정상 처리되었습니다.";
					
					if(!phone.substring(0,3).equals("010")) {
						resultMap.put("resultMessage", "계정잠김은 해제 했으나, 해당 전화번호로 문자 발송이 불가합니다.");
						resultMap.put("resultCode", -10);
						
						logger.info("관리자의 전화번호가 지역번호로 등록됨. : 해제 안내 문자 발송 실패_" +map.get("USER_NO"));
						
					}else {
						String[][] paramLt =
							{{"CONSUMER_ID","C00561"},{"RPLY_PHON_NUM","0264008842"},{"TITLE",title},{"PHONE",phone}}; 
						getVo(paramLt);
					}
					
				}
				
				resultMap.put("resultCode", 0);
				resultMap.put("resultMessage", " 사용자의 계정 잠금 해제가 완료 되었습니다.");
			}
		}
		
		return resultMap;
	}*/
 	
 	@Override
 	public Map<String, Object> lockMemberRequest(HttpServletRequest request) throws Exception {
 		Map<String, Object> resultMap = new HashMap<String, Object>();
 		
 		String user_no = request.getParameter("user_no");
 		int lock_staus = Integer.parseInt(request.getParameter("lock_staus"));
 		String unlock_reson = request.getParameter("unlock_reson");
 		Map<String, Object> requestMap = new HashMap<String, Object>();
 		
 		requestMap.put("user_no", user_no);
 		requestMap.put("lock_staus", lock_staus);
 		requestMap.put("unlock_reson", unlock_reson);
 		 
 		int ret = dao.lockMemberRequest(requestMap);
 		List<Map<String, String>> adminMap = dao.selectAdminMember(requestMap);
 		
 		if (ret == 1) {
 			
 			resultMap.put("resultCode", 0);
 			resultMap.put("resultMessage", " 계정 잠금 해제 신청이 완료되었습니다.");
 		}
 		return resultMap;
 	}
 	
	@Override
	public Map<String, Object> unlockMemberRequest(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String user_no = request.getParameter("user_no");
		String user_name = request.getParameter("user_name");
		String user_phone = request.getParameter("user_phone");
		int lock_staus = Integer.parseInt(request.getParameter("lock_staus"));
		String unlock_reson = request.getParameter("unlock_reson");
		Map<String, Object> requestMap = new HashMap<String, Object>();
		
		String userNo = SessionUtil.getSession("memberSession", "USER_NO");
		String userName = SessionUtil.getSession("memberSession", "USER_NAME");
		
		requestMap.put("user_no", user_no);
		requestMap.put("lock_staus", lock_staus);
		requestMap.put("unlock_reson", unlock_reson);
		requestMap.put("userNo", userNo);
		
		int ret = dao.unlockMemberRequest(requestMap);
		List<Map<String, String>> adminMap = dao.selectAdminMember(requestMap);
		
		if (ret == 1) {
			
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", " 사용자의 계정 잠금 해제가 완료되었습니다.");
		}
		return resultMap;
	}
	
	@Override
	public List<Map<String, Object>> selectLockManagerList(HttpServletRequest request) throws Exception {
		
		String lock_status = request.getParameter("sch_aut");
		String user_no = request.getParameter("sch_id");
		String user_name = request.getParameter("sch_userName");
		String team_name = request.getParameter("sch_teamName");
		
		Map<String, Object> requestMap = new HashMap<String, Object>();
		
		requestMap.put("lock_status", lock_status);
		requestMap.put("user_no", user_no);
		requestMap.put("user_name", user_name);
		requestMap.put("team_name", team_name);
		
		return dao.selectLockManagerList(requestMap);
	}
	
	@Override
	public String selectSMSFlag() throws Exception {

		return dao.selectSMSFlag();
	}
	
	@Override
	public void updateSMSFlag(HttpServletRequest request) throws Exception {
		
		Map<String, Object> update = new HashMap<String, Object>();
		
		dao.updateSMSFlag(update);
		
	}

	@Override
	public List<Map<String, Object>> selectbatchData(String status) throws Exception {
		return dao.selectbatchData(status);
	}
	
//	@Override
//	public Map<String, Object> updateBatchSchedule(HttpServletRequest request) throws Exception {
//		
//		String accessIP = request.getParameter("accessIP");
//		String userNo = SessionUtil.getSession("memberSession", "USER_NO");
//		String status = request.getParameter("status");
//		String pwd = request.getParameter("pwd");
//		String email = request.getParameter("email");
//		
//		Map<String, Object> resultMap1 = new HashMap<String, Object>();
//		
//		if(!SessionUtil.getSession("memberSession", "USER_GRADE").equals("9")) {
//			resultMap1.put("resultCode", -9);
//			resultMap1.put("resultMessage", "올바르지 않은 접근");
//			return resultMap1;
//		}
//		
//		String resultList = request.getParameter("resultList");
//		
//		JSONArray resultArray = JSONArray.fromObject(resultList);
//		
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//
//		resultMap.put("name", "mail");
//		resultMap.put("status", status);
//		
//		Map<String, Object> idx = dao.selectMailBatchMax(resultMap);
//		
//		try {
//			if (resultArray.size() != 0) {
//				for (int i = 0; i < resultArray.size(); i++) {
//					
//					JSONObject map = (JSONObject) resultArray.get(i);
//					
//					String time = map.getString("time");
//					
//					if(status.equals("W")) { // 매주
//						resultMap.put("day", map.getString("week"));
//					}else if(status.equals("M")) { // 매달
//						resultMap.put("day", map.getString("day"));
//					}else if(status.equals("D")) { // 매일
//						resultMap.put("day", null);
//					}
//					
//					resultMap.put("time", time);
//					resultMap.put("pwd", AESEncrypt(pwd));
//					resultMap.put("email", email);
//					resultMap.put("userNo", userNo);
//					
//					logger.info("resultArray :: " + resultMap);
//					
//					dao.insertBatchSchedule(resultMap);
//				}
//			}
//			
//			resultMap.put("idx", idx.get("IDX"));
//			dao.updateBatchSchedule(resultMap);
//			
//		} catch (Exception e) {
//			resultMap.put("resultCode", -100);
//			resultMap.put("resultMessage", "ERROR");
//			
//			logger.error(e.getLocalizedMessage());
//		}
//		
////		 User Log 남기기
//		ServletUtil servletUtil = new ServletUtil(request);
//		String clientIP = servletUtil.getIp();
//		Map<String, Object> userLog = new HashMap<String, Object>();
//		userLog.put("user_no", userNo);
//		userLog.put("menu_name", "Mail Batch Setting Update");		
//		userLog.put("user_ip", clientIP);
//		userLog.put("logFlag", "10");
//		
//		userLog.put("job_info", "메일 연동 설정 변경");
//		dao.insertLog(userLog);
//		
//		resultMap1.put("resultCode", 0);
//		resultMap1.put("resultMessage", "SUCCESS");
//		
//		return resultMap1;
//	}
	
	//	암호화
	private String AESEncrypt(String pwd) throws Exception {
		
		byte[] PKey = key.getBytes(); 
		byte[] encrypted = null ;
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			SecretKeySpec keySpec = new SecretKeySpec(PKey, ALGORITHM);
			
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			encrypted = cipher.doFinal(pwd.getBytes());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Base64.getEncoder().encodeToString(encrypted);
	}
	
	// 복호화
	private String AESDecrypt(String pwd) throws Exception {
		
		byte[] PKey = key.getBytes(); 
		byte[] decrypted = null ;
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			SecretKeySpec keySpec = new SecretKeySpec(PKey, ALGORITHM);
			
			cipher.init(Cipher.DECRYPT_MODE, keySpec);
			byte[] decodedBytes = Base64.getDecoder().decode(pwd);
			decrypted = cipher.doFinal(decodedBytes);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(decrypted);
	}

 	@Override
	public Map<String, Object> selectAccountPolicy() throws Exception {
 		
		Map<String, Object> resultMap = new HashMap<>();
		
 		try {
 			resultMap = dao.selectAccountPolicy();
		} catch (Exception e) {
			e.printStackTrace();
		}
 		
		return resultMap;
	} 
 	
 	@Override
 	public Map<String, Object> saveAccountPolicy(HttpSession session, HttpServletRequest request) throws Exception {
 		
 		Map<String, Object> resultMap = new HashMap<>();
 		
 		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		// Session clear
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		
		//User Log 남기기
		Map<String, Object> userLog = new HashMap<String, Object>();
		List<Map<String, Object>> userLogList = new ArrayList<>();
		Map<String, Object> LogMap = new HashMap<String, Object>();
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "LOGIN");		
		userLog.put("user_ip", clientIP);
 		
 		try {
 			String enable = request.getParameter("enable");
 			String complication = request.getParameter("complication");
 			String lengthChk = request.getParameter("lengthChk");
 			String lengthVal = request.getParameter("lengthVal");
 			String change = request.getParameter("change");
 			String changeVal =request.getParameter("changeVal");
 			String use= request.getParameter("use");
 			String useVal =  request.getParameter("useVal");
 			String lock = request.getParameter("lock");
 			String lockVal = request.getParameter("lockVal") ;
 			String include = request.getParameter("include");
 			String agains = request.getParameter("agains");
 			String againVal = request.getParameter("againVal");
 			
 			resultMap.put("enable", enable);
 			resultMap.put("complication", complication);
 			resultMap.put("lengthChk", lengthChk); 
 			resultMap.put("lengthVal", lengthVal);
 	 		resultMap.put("change", change); 
 	 		resultMap.put("changeVal", changeVal);
 	 		resultMap.put("use", use);
 	 		resultMap.put("useVal", useVal);
 	 		resultMap.put("lock", lock); 
 	 		resultMap.put("lockVal", lockVal);
 	 		resultMap.put("include", include);
 	 		resultMap.put("agains", agains); 
 	 		resultMap.put("againVal", againVal);
 			
 			dao.saveAccountPolicy(resultMap);
 			
 			userLog.put("job_info", "Login Policy Update Success");
 			userLog.put("logFlag", "6");
 			
 			String userLogCon = "로그인 정책 ";
 			if (enable.equals("0")) userLogCon += "비활성화";
 			else { 
 				userLogCon += "활성화<br>";
 				userLogCon +="=================================================<br>";
 				
 				userLogCon += "패스워드 복잡도 ";
 				userLogCon += complication.equals("0")?"비활성화<br>": " 활성화<br>";
 			
 				userLogCon += "패스워드 최소길이 ";
 				userLogCon += lengthChk.equals("0")?"비활성화<br>": ": "+" "+lengthVal+"자<br>";
 			
 				userLogCon += "패스워드 변경주기 ";
 				userLogCon += change.equals("0")?"비활성화<br>": ": "+" "+changeVal+"일<br>";
 				
 				userLogCon += "이전 패스워드 사용 금지 ";
 				userLogCon += agains.equals("0")?"비활성화<br>": ": "+" "+againVal+"회<br>";
 				
 				userLogCon += "장기 미사용 계정 잠김 ";
 				userLogCon += lock.equals("0")?"비활성화<br>": ": "+" "+lockVal+"일<br>";
 				
 				userLogCon += "계정 잠김(임계값) ";
 				userLogCon += lock.equals("0")?"비활성화<br>": ": "+" "+lockVal+"일<br>";
 				
 				userLogCon += "패스워드 계정 미포함 ";
 				userLogCon += include.equals("0")?"비활성화<br>": " 활성화<br>";
 				
 			
 			}
 			LogMap.put("key", userLogCon);
			userLogList.add(LogMap);
			userLog.put("context", userLogList.toString());
 			
 			resultMap.put("resultCode", 200);
 			
 			Map<String, Object> loginPolicyMap =  dao.selectAccountPolicy();
 			// 세션 정보 업데이트
 			session.setAttribute("loginPolicyMap", loginPolicyMap);
 			
 		} catch (Exception e) {
 			e.printStackTrace();
 			userLog.put("job_info", "Login Policy Update Fail");
 		}
 		dao.insertLog(userLog);
 		return resultMap;
 	}
 	
 	@Override
	public List<Map<String, Object>> selectLicense() throws Exception {
 		
		ReconUtil reconUtil = new ReconUtil();
		Map<String, Object> httpsResponse = null;
		
		JsonObject result = new JsonObject();
		
		Map<String, Object> resultMap = new HashMap<>();
		List<Map<String, Object>> resultList = new ArrayList<>();
 		try {
 			Properties properties = new Properties();
			String resource = "/property/config.properties";
			Reader reader = Resources.getResourceAsReader(resource);
			
			properties.load(reader);
			
			int ap_count = Integer.parseInt((properties.getProperty("recon.count") == null) ? "0" : properties.getProperty("recon.count"));

			String company = "";
			String expire = "";
			String total = "";
			String usage = "";
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			for (int i = 0; i < ap_count; i++) {
			    resultMap = new HashMap<>();
			    this.recon_url = (i == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (i + 1));
			    this.recon_id = (i == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (i + 1));
			    this.recon_password = (i == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (i + 1));
			    this.api_ver = properties.getProperty("recon.api.version");

			    httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + this.api_ver + "/licenses", "GET", null);

			    int resultCode = Integer.parseInt(httpsResponse.get("HttpsResponseCode").toString());
			    String resultMessage = httpsResponse.get("HttpsResponseMessage").toString();
			    logger.info("resultMessage"+ resultMessage);
			    JsonObject jsonObject = new Gson().fromJson(httpsResponse.get("HttpsResponseData").toString(), JsonObject.class);

			    ArrayList<Map<String, String>> detailList = new ArrayList<>();

			    company = jsonObject.get("company").getAsString();
			    expire = jsonObject.get("expires").getAsString();
			    Long linuxTm = Long.parseLong(expire.replace("\"", ""));
			    Date date = new Date();
			    date.setTime(linuxTm * 1000);
			    expire = sdf.format(date);

			    JsonArray summary = jsonObject.getAsJsonArray("summary");

			    JsonObject summaryObject = summary.get(0).getAsJsonObject();

			    Map<String, Object> apMap = dao.selectApNoData(i);

			    total = summaryObject.get("total").getAsString();
			    usage = summaryObject.get("usage").getAsString();

			    resultMap.put("status", apMap.get("NETWORK"));
			    resultMap.put("server", apMap.get("NAME"));
			    resultMap.put("company", company);
			    resultMap.put("expire", expire);
			    resultMap.put("total", numberFormat(total));
			    resultMap.put("usage", numberFormat(usage));

			    resultList.add(resultMap);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
 		
		return resultList;
	}
 	
 	private String numberFormat(String dataSize) {
		String fomatSize = "";
		
		Double size = Double.parseDouble(dataSize);
		
		logger.info("size >>>>" + size);
		
		if(size > 0) {
			
			String[] s = {"bytes", "KB", "MB", "GB", "TB"};
			
			int idx = (int) Math.floor(Math.log(size) / Math.log(license));
			if(idx > 4) idx = 4;
			
            DecimalFormat df = new DecimalFormat("#,###.##");
            double ret = ((size / Math.pow(license, Math.floor(idx))));
            fomatSize =  df.format(ret) + " " + s[idx];
		}
		
		return fomatSize;
	}
}
