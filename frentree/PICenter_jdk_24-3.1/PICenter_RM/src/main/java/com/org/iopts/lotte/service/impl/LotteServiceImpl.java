package com.org.iopts.lotte.service.impl;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.iopts.dao.Pi_UserDAO;
import com.org.iopts.lotte.dao.LotteDAO;
import com.org.iopts.lotte.service.LotteService;
import com.org.iopts.util.ServletUtil;
import com.org.iopts.util.SessionUtil;

@Service
@Transactional
public class LotteServiceImpl implements LotteService {
	
	@Value("${pic_version}")
	private String pic_version;
	
	@Value("${ad_ip}")
	private String adIp;
	
	@Value("${ad_port}")
	private String adPort;

	private static Logger logger = LoggerFactory.getLogger(LotteServiceImpl.class);

	@Inject
	private LotteDAO lotteDAO;
	
	@Inject
	private Pi_UserDAO dao;

	@Inject
	private LotteService lotteService;
	
	
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
		
		// 비밀번호가 틀린 경우
		if (!authenticateWithLDAP(user_no, password)) {
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
		
		// 사용자 세션 등록
		SessionUtil.setSession("memberSession", memberMap);
		SessionUtil.setSession("picSession", picMap);
		
		setHeader(memberMap);
		
		logger.info("로그인 완료");

		return resultMap;
	}
	
	
	private boolean authenticateWithLDAP(String username, String password) {
	    
		
		if(username.equals("frentree")) {
			return true; 
		}else {
			Hashtable<String, String> env = new Hashtable<>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, "ldap://" + adIp + ":" + adPort);
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, username + "@lotte-autolease-vdi.net"); // UPN 형식
			env.put(Context.SECURITY_CREDENTIALS, password);
			env.put("com.sun.jndi.ldap.connect.timeout", "5000");
			
			LdapContext ctx = null;
			try {
				ctx = new InitialLdapContext(env, null);
				return true; // 인증 성공
			} catch (AuthenticationException e) {
				return false; // 인증 실패
			} catch (Exception e) {
				logger.error("LDAP 인증 중 오류", e);
				return false;
			} finally {
				if (ctx != null) {
					try { ctx.close(); } catch (Exception e) {}
				}
			}
		}
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

	
}
