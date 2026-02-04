package com.org.iopts.service;

import java.io.IOException;
import java.io.Reader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.org.iopts.dao.Pi_ScanDAO;
import com.org.iopts.dao.Pi_TargetDAO;
import com.org.iopts.dao.Pi_UserDAO;
import com.org.iopts.util.DecryptingPropertyPlaceholderConfigurer;
import com.org.iopts.util.ReconUtil;
import com.org.iopts.util.ServletUtil;
import com.org.iopts.util.SessionUtil;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@PropertySource("classpath:/property/config.properties")
@Service
public class Pi_UserServiceImpl implements Pi_UserService {
	
	@Value("${recon.api.version}")
	private String api_ver;
	
	@Value("${pic_version}")
	private String pic_version;
	
	@Value("${company_pwd}")
	private String company_pwd;
	
	@Value("${recon.license}")
	private int license;
	
	@Value("${ldapUrl}")
	private String ldapUrl;

	private static Logger logger = LoggerFactory.getLogger(Pi_UserServiceImpl.class);

	@Inject
	private Pi_UserDAO dao;
	
	@Inject
	private Pi_ScanDAO scanDao;
	
	@Inject
	private Pi_TargetDAO targetDao;
	
	@Override
	public Map<String, Object> getversion() throws Exception {
		Map<String, Object> requestMap = new HashMap<String, Object>();
		requestMap.put("version", pic_version);
		return dao.getversion(requestMap);
	}

	@Override
	public Map<String, Object> selectMember(HttpServletRequest request) throws Exception {

		// Session clear
		SessionUtil.closeSession("memberSession");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		String user_no = request.getParameter("user_no");
		String password = request.getParameter("password");
		if (password != null) {
			password = password.trim();
		}
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("user_no", user_no);
		searchMap.put("password", password);
		
		// User Log 남기기
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "LOGIN");		
		userLog.put("user_ip", clientIP);
		userLog.put("job_info", "Log-In Success");
		userLog.put("logFlag", "0");
		
		Map<String, Object> requestMap = new HashMap<String, Object>();
		requestMap.put("version", pic_version);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> memberMap = dao.selectMember(searchMap);
		Map<String, Object> versionMap = dao.getversion(requestMap);
		Map<String, String> uptMap = new HashMap<>();
		
		String lastAccessTime = dao.selectLastAccessTime(user_no);
		resultMap.put("lastAccessTime", lastAccessTime);
		resultMap.put("clientIP", clientIP);
		
		Map<String, Object> authMap = dao.selectAccountPolicy();
		
		// 사용자번호가 틀린 경우
		if ((memberMap == null) || (memberMap.size() == 0)) {
			resultMap.put("resultCode", 400);
			resultMap.put("resultMessage", user_no + "는 존재하지 않는 ID 입니다.");
			
			userLog.put("job_info", "Log-In Fail(Invalid User No)");
			dao.insertLog(userLog);
			return resultMap;
		}
		
		// 계정이 잠긴 경우
		if(memberMap.get("lock_chk").equals("Y")) {
			resultMap.put("resultCode", 400);
			resultMap.put("resultMessage", "계정이 잠겼습니다. 관리자에게 문의해주세요.");
			
			return resultMap;	
		}
		
		logger.info("memberMap :: " + memberMap.toString());
		logger.info("authMap :: " + authMap.toString());
		
		int pwd_fail_chk = (int) authMap.get("locks");
		
		// 계정 패스워드 실패
		if(pwd_fail_chk == 1 && !memberMap.get("USER_NO").toString().equals("frentree")) {
			int pwd_fail_cnt = (int) authMap.get("lock_val");
			// 비밀번호가 틀린 경우
			if (!(memberMap.get("PASSWORD").equals("Y"))) {
				userLog.put("job_info", "Log-In Fail(Invalid Password)");
				dao.insertLog(userLog);
				
				int failed_count = (Integer.parseInt(memberMap.get("FAILED_COUNT").toString())+1);

				resultMap.put("resultCode", 400);
				if(failed_count >= pwd_fail_cnt) {
					resultMap.put("resultMessage", "계정이 잠겼습니다. 관리자에게 문의해주세요.");
				}else {
					resultMap.put("resultMessage", "패스워드를 " + failed_count + "회 틀리셨습니다. " + pwd_fail_cnt + "회이상 틀릴시 계정이 잠깁니다.");
				}
				
				uptMap.put("userNo", user_no);
				uptMap.put("failed_count", String.valueOf(failed_count));
				
				if(failed_count < pwd_fail_cnt) {
					uptMap.put("lockYn", "N");
				} else { 
					uptMap.put("lockYn", "Y");
				}
				
				logger.info("password error count :: " + failed_count);
				logger.info("lock account :: " + dao.updateFailedCount(uptMap));
				return resultMap;			
			} 
		} else {
			if (!(memberMap.get("PASSWORD").equals("Y"))) {
				resultMap.put("resultCode", 400);
				resultMap.put("resultMessage", "아이디/패스워드가 존재하지 않습니다. 다시 확인 해주세요.");
				userLog.put("job_info", "Log-In Fail(Invalid Password)");
				dao.insertLog(userLog);
				
				return resultMap;	
			}
		}
		
		// 패스워드 변경일
		if(memberMap.get("CHANGE_CHK").equals("Y")) {
			int change = (int) authMap.get("change_val");
			resultMap.put("resultCode", 401);
			resultMap.put("resultMessage", "패스워드 변경일이 " + change + "일을 지났습니다. 패스워드를 변경해주세요.");
			return resultMap;	
		}
		
		// 장기 미사용
		if(memberMap.get("USE_CHK").equals("Y")) {
			resultMap.put("resultCode", 400);
			resultMap.put("resultMessage", "장기 미사용으로 계정이 잠겼습니다. 관리자에게 문의해주세요.");
			
			// 로그인 데이트 업데이트
			dao.updateLoginDateLock(searchMap);
			
			return resultMap;	
		}
		
		Object str = memberMap.get("USER_GRADE");
		
		// 로그인 데이트 업데이트
		dao.updateLoginDate(searchMap);
		
		dao.insertLog(userLog);
		
		uptMap.put("userNo", user_no);
		uptMap.put("failed_count", "0");
		uptMap.put("lockYn", "N");
		logger.info("lock account :: " + dao.updateFailedCount(uptMap));
		 
		resultMap.put("resultCode", 200);
		resultMap.put("resultMessage", "로그인 성공");
		resultMap.put("user_grade", str);
		resultMap.put("member", memberMap);
		logger.info("resultMap 확인 = "+ resultMap);
		
		// 사용자 세션 등록
		
		SessionUtil.setSession("memberSession", memberMap);
		SessionUtil.setSession("versionSession", versionMap);
		
		setHeader(memberMap);
 
		return resultMap;
	}
	
	private void setHeader(Map<String, Object> memberMap) {
		List<Map<String, Object>> headerList = new ArrayList<>();
		
		headerList = dao.setHeader(memberMap);
		
		SessionUtil.setSession("headerList", headerList);
		
		Map<String, Object> pageData = dao.setPageData(memberMap);
		
		logger.info("pageData :: " + pageData.toString());
		logger.info("main :: " + pageData.get("MAIN"));
		String mainYn = pageData.get("MAIN").toString();
		SessionUtil.setSession("mainYn", mainYn);
		SessionUtil.setSession("defaultPage", pageData.get("URL").toString());
	}

    @Override
    @Transactional
    public Map<String, Object> changeAuthCharacter(HttpServletRequest request) throws Exception {

            Map<String, Object> resultMap = new HashMap<String, Object>();
            ServletUtil servletUtil = new ServletUtil(request);
            String clientIP = servletUtil.getIp();

            String oldPassword = request.getParameter("oldPassword") == null ? "" : request.getParameter("oldPassword");
            String newPasswd = request.getParameter("newPasswd") == null ? "" : request.getParameter("newPasswd");

            // 세션에서 user_no 가져오기
            String user_no = SessionUtil.getSession("memberSession", "USER_NO");

            // 세션에 없으면 파라미터에서 가져오기 (로그인 전 비밀번호 변경)
            if(user_no == null || user_no.isEmpty()) {
                    user_no = request.getParameter("user_id");
                    logger.info("user_no from parameter :: " + user_no);
            }

            // 그래도 없으면 에러
            if(user_no == null || user_no.isEmpty()) {
                    resultMap.put("resultCode", -1);
                    resultMap.put("resultMessage", "사용자 정보를 확인할 수 없습니다.");
                    return resultMap;
            }

            logger.info("user_no :: " + user_no);

            Map<String, Object> searchMap = new HashMap<String, Object>();
            searchMap.put("user_no", user_no);
            searchMap.put("oldPassword", oldPassword);
            searchMap.put("newPasswd", newPasswd);

            // 인증 절차
            Map<String, Object> authMap = dao.selectAccountPolicy();

            // User Log 남기기
            Map<String, Object> userLog = new HashMap<String, Object>();

            // User Log 남기기
            userLog.put("user_no", user_no);
            userLog.put("menu_name", "PASSWORD CHANGE");
            userLog.put("user_ip", clientIP);
            userLog.put("logFlag", "6");
            int enable = (int) authMap.get("enable");

            String lastAccessTime = dao.selectLastAccessTime(user_no);
            resultMap.put("lastAccessTime", lastAccessTime);
            resultMap.put("clientIP", clientIP);

            if(enable == 1) {
                    int include = (int) authMap.get("include");

                    // 계정에 패스워드가 들어갈 경우
                    if(include == 1) {
                            if(newPasswd != null && user_no != null && newPasswd.toLowerCase().contains(user_no.toLowerCase())) {
                                    resultMap.put("resultCode", -1);
                                    resultMap.put("resultMessage", "패스워드에 계정이 포함되어있습니다. 다시 확인해주세요.");
                                    userLog.put("job_info", "Password Change Fail");

                                    dao.insertLog(userLog);

                                    return resultMap;
                            }
                    }
            }

            int ret = dao.changeAuthCharacter(searchMap);

            if (ret == 1) {
                    resultMap.put("resultCode", 0);
                    resultMap.put("resultMessage", "비밀번호가 변경되었습니다.");
                    userLog.put("job_info", "Password Change Success");
                    dao.insertPwdDuplicate(searchMap);
                    dao.insertLog(userLog);
            }
            else {
                    resultMap.put("resultCode", -1);
                    resultMap.put("resultMessage", "현재 비밀번호가 다릅니다.");
                    userLog.put("job_info", "Password Change Fail");

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

		return dao.selectUserLogList(search);
	}

	@Override
	@Transactional
	public void insertMemberLog(Map<String, Object> userLog) throws Exception {
		
		dao.insertLog(userLog);
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
		
		return dao.selectManagerList();
	}
	
	@Override
	public List<Map<String, Object>> selectCreateUser(HttpServletRequest request) throws Exception {
	    // 세션 체크 추가
		List<Map<String, Object>> resultList = new ArrayList<>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
	    String user_no = SessionUtil.getSession("memberSession", "USER_NO");
	    if (user_no == null || user_no.isEmpty()) {
			resultMap = new HashMap<String, Object>();
			
            resultMap.put("ERROR", "로그인 정보가 없습니다.");
            resultList.add(resultMap);
	        return resultList;
	    }
	    
	    String userGrade = SessionUtil.getSession("memberSession", "USER_GRADE");
//	    if (!"9".equals(userGrade)) {
//			resultMap = new HashMap<String, Object>();
//			
//            resultMap.put("ERROR", "관리자 권한이 필요합니다.");
//            resultList.add(resultMap);
//	        return resultList;
//	    }		
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user_no", user_no);
        
	
		
		List<Map<String, Object>> userList = dao.selectCreateUser(map);
		
		for (Map<String, Object> map2 : userList) {
			resultMap = new HashMap<String, Object>();
			
            resultMap.put("USER_NO", replaceParameter(map2.get("USER_NO").toString()));
            resultMap.put("USER_NM", replaceParameter(map2.get("USER_NM").toString()));
			resultMap.put("PWD_UPT_DT", map2.get("PWD_UPT_DT").toString());
			resultMap.put("LOGINDATE", map2.get("LOGINDATE").toString());
			resultMap.put("LOCK", map2.get("LOCK").toString());
			resultMap.put("PASSWORD_REST", "");
			resultMap.put("BUTTON", "");
			
			resultList.add(resultMap);
		}
		return resultList;
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
		
		userLog.put("job_info", "User Delete (" + userNo + ")" );
		
		dao.insertLog(userLog);
	}
	
	@Override
	@Transactional
	public void pwd_reset(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String userNo = request.getParameter("userNo");
		map.put("userNo", userNo);
		map.put("changePwd", company_pwd); 
		
		dao.userPwdReset(map);
		
		// User Log 남기기
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "RESET PWD USER");		
		userLog.put("user_ip", clientIP);
		userLog.put("logFlag", "6");
		
		userLog.put("job_info", userNo+" PassWord Initialization");
		
		dao.insertLog(userLog);
	}
	
	@Override
	public Map<String, Object> chkDuplicateUserNo(HttpServletRequest request) throws Exception {
		
		String userNo = request.getParameter("user_no");
		
		return dao.chkDuplicateUserNo(userNo);
	}

	@Override
	@Transactional
	public void createUser(HttpServletRequest request) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		String user_no = request.getParameter("user_no");
		String user_nm = request.getParameter("user_nm");
		
		map.put("user_no", user_no);
		map.put("user_nm", user_nm);
		map.put("password", company_pwd);
		
		try {
			dao.createUser(map);
		} catch (NullPointerException e) {
			logger.error("error :: "+e.toString());
		} catch (Exception e) {
			logger.error("error :: "+e.toString());
		}
		
		// User Log 남기기
		String userNo = SessionUtil.getSession("memberSession", "USER_NO");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", userNo);
		userLog.put("menu_name", "CREATE USER");		
		userLog.put("user_ip", clientIP);
		userLog.put("logFlag", "6");
		
		userLog.put("job_info", "User Create (" + user_no +")");
		
		try {
			dao.insertLog(userLog);
		} catch (RuntimeException e) {
		    logger.error("Failed to insert user log", e);
		    //throw e; // 상위 호출자가 처리할 수 있도록 예외 전파
		} catch (Exception e) {
			logger.error("Failed to insert user log", e);
		}
	}
		
 	@Override
	@Transactional
	public Map<String, Object> changeUserSettingData(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String userNo =  SessionUtil.getSession("memberSession", "USER_NO");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if("frentree".equals(userNo)) {
			resultMap.put("resultCode", 12);
			resultMap.put("resultMessage", "계정 변경 금지 계정");
			
			return resultMap;
		}
		
		String changeUserSettingNM = request.getParameter("changeUserSettingNM");
		String changeUserSettingPhoneNM = request.getParameter("changeUserSettingPhoneNM");
		String changeUserSettingEmail = request.getParameter("changeUserSettingEmail");
		String changeUserSettingpassword = request.getParameter("changeUserSettingpassword");
		
		map.put("userNo", userNo);
		map.put("user_no", userNo);
		map.put("changeUserSettingNM", changeUserSettingNM);
		map.put("changeUserSettingPhoneNM", changeUserSettingPhoneNM);
		map.put("changeUserSettingEmail", changeUserSettingEmail);
		map.put("changeUserSettingpassword", changeUserSettingpassword);
		map.put("oldPassword", changeUserSettingpassword);
		

		// 인증 절차
		Map<String, Object> authMap = dao.selectAccountPolicy();
		
		int enable = (int) authMap.get("enable"); 
		
		if(enable == 1) {
			int include = (int) authMap.get("include");
			
			// 계정에 패스워드가 들어갈 경우
			if(include == 1) {
				 String password = changeUserSettingpassword == null ? "" : changeUserSettingpassword.toLowerCase();
				 String username = userNo == null ? "" : userNo.toLowerCase();
				
				if(password.contains(username) && !username.isEmpty()) {
					resultMap.put("resultCode", -1);
					resultMap.put("resultMessage", "패스워드에 계정이 포함되어있습니다. 다시 확인해주세요.");

					return resultMap;
				}
			}
		}
		
		dao.changeUserSettingDate(map);
		dao.insertPwdDuplicate(map);
		
		Map<String, Object> requestMap = new HashMap<String, Object>();
		requestMap.put("version", pic_version);
		
		Map<String, Object> memberMap = dao.selectChangeMember(map);
		Map<String, Object> versionMap = dao.getversion(requestMap);
		
		Object str = memberMap.get("USER_GRADE");
		 
		resultMap.put("resultCode", 0);
		resultMap.put("resultMessage", "회원 정보 수정 성공");
		resultMap.put("user_grade", str);
		resultMap.put("member", memberMap);
		
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", userNo);
		userLog.put("menu_name", "UPDATE USER");		
		userLog.put("user_ip", clientIP);
		userLog.put("logFlag", "6");
		
		userLog.put("job_info", "User Update (" + userNo +")");
		
		try {
			dao.insertLog(userLog);
		} catch (RuntimeException e) {
		    logger.error("Failed to insert user log", e);
		} catch (Exception e) { 
			logger.error("Failed to insert user log", e);
		}
		SessionUtil.closeSession("memberSession");
		
		SessionUtil.setSession("memberSession", memberMap);
		SessionUtil.setSession("versionSession", versionMap);
		
		setHeader(memberMap);
		
		return resultMap;
	}
 	
 	@Override
	public List<Map<String, Object>> selectLicense() throws Exception {
 		
		ReconUtil reconUtil = new ReconUtil();
		Map<String, Object> httpsResponse = null;
		
		JsonObject result = new JsonObject();
		
		Map<String, Object> resultMap = new HashMap<>();
		List<Map<String, Object>> resultList = new ArrayList<>();
		
		String resource = "";
		
 		try {
 			Properties properties = new Properties();
			resource = "/property/config.properties";
			Reader reader = Resources.getResourceAsReader(resource);
			
			properties.load(reader);
			
			int ap_count = Integer.parseInt((properties.getProperty("recon.count") == null) ? "0" : properties.getProperty("recon.count"));

			String company = "";
			String expire = "";
			String total = "0";
			String usage = "0";
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			for(int i=0; i<ap_count; i++) {
				resultMap = new HashMap<>();
				
				// 루프 내에서 실행되므로 final 필드 사용 불가
		        // 정적 분석 경고 억제를 위해 주석 추가
		        // These fields are set once per iteration and not modified elsewhere
				String recon_url = (i == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (i+1)) ;
				String recon_id = (i == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (i+1)) ;
				//String recon_password = (i == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (i+1)) ;
				//recon_password =  DecryptingPropertyPlaceholderConfigurer.decryptValue(recon_password); 
				String recon_password_enc = (i == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (i+1)) ;
				
				httpsResponse = reconUtil.getServerData(recon_id, DecryptingPropertyPlaceholderConfigurer.decryptValue(recon_password_enc), recon_url + "/"+ this.api_ver + "/licenses", "GET", null);
				
				JsonObject jsonObject = JsonParser.parseString(httpsResponse.get("HttpsResponseData").toString()).getAsJsonObject();

				company = (jsonObject.get("company") != null) ? jsonObject.get("company").getAsString() : "";
				expire = (jsonObject.get("expires") != null) ? jsonObject.get("expires").getAsString() : "0";
				expire = sdf.format(new Date(Long.parseLong(expire) * 1000));

				Map<String, Object> apMap = dao.selectApNoData(i);

		        if (jsonObject.get("summary") != null && jsonObject.get("summary").isJsonArray()) {
		            JsonArray summaryArray = jsonObject.get("summary").getAsJsonArray();
		            if (summaryArray.size() > 0) {
		                JsonObject summaryObject = summaryArray.get(0).getAsJsonObject();
		                total = (summaryObject.get("total") != null) ? summaryObject.get("total").getAsString() : "0";
		                usage = (summaryObject.get("usage") != null) ? summaryObject.get("usage").getAsString() : "0";
		            }
		        }

				resultMap.put("status", apMap.getOrDefault("NETWORK", ""));
				resultMap.put("server", apMap.getOrDefault("NAME", ""));
				resultMap.put("company", company);
				resultMap.put("expire", expire);
				resultMap.put("total", numberFormat(total));
				resultMap.put("usage", numberFormat(usage));

				
				resultList.add(resultMap);
			} 
 		} catch (IOException e) {
 		    logger.error("Failed to load properties from " + resource, e);
		} catch (Exception e) {
			 logger.error("Failed to load properties from " + resource, e);
			//e.printStackTrace();
		}
 		
		return resultList;
	}

 	
 	@Override
	public Map<String, Object> selectAccountPolicy() throws Exception {
 		
		Map<String, Object> resultMap = new HashMap<>();
		 
 		try {
 			resultMap = dao.selectAccountPolicy();
 		} catch (RuntimeException e) {
 			logger.error("Failed to select account policy", e);
		} catch (Exception e) {
			logger.error("Failed to select account policy", e);
		}
 		
		return resultMap;
	}
 	
 	@Override
 	public Map<String, Object> saveAccountPolicy(HttpServletRequest request) throws Exception {
 		
 		Map<String, Object> resultMap = new HashMap<>();
 		
 		try {
 			resultMap.put("enable", request.getParameter("enable"));
 	 		resultMap.put("change", request.getParameter("change")); 
 	 		resultMap.put("changeVal", request.getParameter("changeVal"));
 	 		resultMap.put("use", request.getParameter("use"));
 	 		resultMap.put("useVal", request.getParameter("useVal"));
 	 		resultMap.put("lock", request.getParameter("lock")); 
 	 		resultMap.put("lockVal", request.getParameter("lockVal"));
 	 		resultMap.put("include", request.getParameter("include"));
 			
 			dao.saveAccountPolicy(resultMap);
 			
 			resultMap.put("resultCode", 200);
 		} catch (NullPointerException e) {
 			logger.info("error :: "+e.toString());
 		} catch (Exception e) {
 			logger.info("error :: "+e.toString());
 		}
 		
 		return resultMap;
 	}
 	
	// 크로스 사이트 스크립트 방지 처리 메소드
 	public static String replaceParameter(String param) {
		String result = param;
		if(param != null) {
			result = result.replaceAll("&", "&amp;");
			result = result.replaceAll("<", "&lt;");
			result = result.replaceAll(">", "&gt;");
			result = result.replaceAll("\"", "&quot;");
		}
		
		return result;
	}
 	
 	
 	private String numberFormat(String dataSize) {
		String fomatSize = "";
		
		Double size = Double.parseDouble(dataSize);
		
		logger.info("size >>>>" + size);
		
		if(size > 0) {
			
			String[] s = {"bytes", "KB", "MB", "GB", "TB"};
			
			int idx = computeIndex(size,license);
			if(idx > 4) idx = 4;
			
            DecimalFormat df = new DecimalFormat("#,###.##");
            //double ret = ((size / Math.pow(license, Math.floor(idx))));
            double ret = computeDivision(size, Math.pow(license, Math.floor(idx)));
            fomatSize =  df.format(ret) + " " + s[idx];
		}
		
		return fomatSize;
	}
 	
    public strictfp int computeIndex(double num1, double num2) {
        return (int) Math.floor(Math.log(num1) / Math.log(num2));
    }
    
    public strictfp double computeDivision(double num1, double num2) {
    	return num1/num2;
    }
 	
}
