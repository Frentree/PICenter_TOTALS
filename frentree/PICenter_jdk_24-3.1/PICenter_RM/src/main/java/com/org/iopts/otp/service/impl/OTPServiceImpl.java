package com.org.iopts.otp.service.impl;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.org.iopts.dao.Pi_UserDAO;
import com.org.iopts.otp.dao.OTPDAO;
import com.org.iopts.otp.service.OTPService;
import com.org.iopts.util.ServletUtil;
import com.org.iopts.util.SessionUtil;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

@Service
@Transactional
public class OTPServiceImpl implements OTPService{

	private static Logger logger = LoggerFactory.getLogger(OTPServiceImpl.class);
	
	@Inject
	private OTPDAO dao;
	
	@Inject
	private Pi_UserDAO userDao;
	
	@Value("${pic_version}")
	private String pic_version;

	@Override
	public Map<String, Object> OTPUserCheck(HttpServletRequest request) throws Exception {
		// Session clear
		SessionUtil.closeSession("memberSession");
		SessionUtil.closeSession("picSession");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		String user_no = request.getParameter("user_no");
		String password = request.getParameter("password");
		
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("user_no", user_no);
		searchMap.put("password", password);
		
		// User Log 남기기
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "LOGIN");		
		userLog.put("user_ip", clientIP);
		userLog.put("job_info", "OTP_User_Check_Success_"+user_no);
		userLog.put("logFlag", "0");
		
		Map<String, Object> requestMap = new HashMap<String, Object>();

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> memberMap = dao.selectMember(searchMap);
		Map<String, String> uptMap = new HashMap<>();
		Map<String, Object> picMap = new HashMap<String, Object>();
		
		Map<String, Object> authMap = userDao.selectAccountPolicy();
		
		// 사용자번호가 틀린 경우
		if ((memberMap == null) || (memberMap.size() == 0)) {
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", user_no + "는 존재하지 않는 ID 입니다.");
			
			userLog.put("job_info", "Log-In Fail(Invalid User No)");
			userDao.insertLog(userLog);
			return resultMap;
		}
		
		// 계정이 잠긴 경우
		if(memberMap.get("LOCK_ACCOUNT").equals("Y")) {
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "계정이 잠겼습니다. 관리자에게 문의해주세요.");
			return resultMap;	
		}
		
		int pwd_fail_chk = (int) authMap.get("locks");
		
		// 비밀번호가 틀린 경우
		if(pwd_fail_chk == 1) {
			int pwd_fail_cnt = (int) authMap.get("lock_val");
			
			if (!(memberMap.get("PASSWORD").equals("Y"))) {
				userLog.put("job_info", "Log-In Fail(Invalid Password)");
				userDao.insertLog(userLog);
				
				int failed_count = (Integer.parseInt(memberMap.get("FAILED_COUNT").toString())+1);

				resultMap.put("resultCode", -1);
				if(failed_count >= pwd_fail_cnt) {
					resultMap.put("resultMessage", "계정이 잠겼습니다. 관리자에게 문의해주세요.");
				}else {
					resultMap.put("resultMessage", "비밀번호를 " + failed_count + "회 틀리셨습니다. " + pwd_fail_cnt + "회이상 틀릴시 계정이 잠깁니다.");
				}
				
				uptMap.put("userNo", user_no);
				uptMap.put("failed_count", String.valueOf(failed_count));
				
				if(failed_count < pwd_fail_cnt) {
					uptMap.put("userNo", user_no);
					uptMap.put("failed_count", String.valueOf(failed_count));
					uptMap.put("lockYn", "N");
				} else { 
					uptMap.put("userNo", user_no);
					uptMap.put("failed_count", "0");
					uptMap.put("lockYn", "Y");
				}
				
				logger.info("password error count :: " + failed_count);
				logger.info("lock account :: " + userDao.updateFailedCount(uptMap));
				return resultMap;			
			} 
		}else {
			if (!(memberMap.get("PASSWORD").equals("Y"))) {
				resultMap.put("resultCode", -1);
				resultMap.put("resultMessage", "아이디/패스워드가 존재하지 않습니다. 다시 확인 해주세요.");
				userLog.put("job_info", "Log-In Fail(Invalid Password)");
				userDao.insertLog(userLog);
				
				return resultMap;	
			}
		}
		
		// 패스워드 변경일
		if(memberMap.get("CHANGE_CHK").equals("Y")) {
			int change = (int) authMap.get("change_val");
			resultMap.put("resultCode", -9);
			resultMap.put("resultMessage", "패스워드 변경일이 " + change + "일을 지났습니다. 패스워드를 변경해주세요.");
			return resultMap;	
		}
		
		// 장기 미사용
		if(memberMap.get("USE_CHK").equals("Y")) {
			resultMap.put("resultCode", -10);
			resultMap.put("resultMessage", "장기 미사용으로 계정이 잠겼습니다. 관리자에게 문의해주세요.");
			
			userDao.updateLoginDateLock(searchMap);
			
			return resultMap;	
		}
		
		Date logindate = (Date) memberMap.get("LOGINDATE");
		Date toDay =  new Date(); // 오늘날짜
		
		Object str = memberMap.get("USER_GRADE");
		System.out.println("user_grade 확인 = "+str);
		
		if(!user_no.equals("frentree")) {
			if ((memberMap.get("USER_GRADE").equals("9"))) {
				
				String accessIP = (String) memberMap.get("ACCESS_IP");
				
				if(accessIP.equals("")) {
					//resultMap.put("resultCode", -3);
					resultMap.put("resultCode", -2);
					resultMap.put("resultMessage", "최고관리자의 접근IP가 없습니다.");
					
					userLog.put("job_info", "Log-In Fail(Access IP is Null)");
					userDao.insertLog(userLog);
					return resultMap;			
				}
				
				String[] accessIPs = accessIP.split(",");
				if(!user_no.equals("frentree")) {
					if (!Arrays.asList(accessIPs).contains(clientIP)) {
						resultMap.put("resultCode", -3);
						resultMap.put("resultMessage", "등록되지 않은 최고관리자 접근IP입니다.");
						userLog.put("job_info", "Log-In Fail(Access IP is Invalid)");
						userDao.insertLog(userLog);
						return resultMap;			
					}
				}
			}
		}
		
		userDao.insertLog(userLog);
		userDao.updateLoginData(userLog);
		
		uptMap.put("userNo", user_no);
		uptMap.put("failed_count", "0");
		uptMap.put("lockYn", "N");
		uptMap.put("logindate", "Y");
		logger.info("lock account :: " + userDao.updateFailedCount(uptMap));
        if(memberMap.get("SECRET_KEY") == null) { // 등록된 key가 없는 경우
            Map<String, Object> OTPMap = UserOTPCreate(user_no);
            resultMap.put("secretKey", OTPMap.get("secretKey"));
            resultMap.put("qrUrl", OTPMap.get("qrUrl"));
        }else {
            resultMap.put("secretKey", memberMap.get("SECRET_KEY"));
            resultMap.put("qrUrl", null);
        }
		resultMap.put("resultCode", 0);
		resultMap.put("resultMessage", "로그인 성공");
		resultMap.put("user_grade", str);
		resultMap.put("member", memberMap);
		logger.info("resultMap 확인 = "+ resultMap);

		return resultMap;
	}
	
	@Override
	public Map<String, Object> UserOTPInsert(HttpServletRequest request) throws Exception {
		
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String user_no = request.getParameter("user_no");
		String otpskey = request.getParameter("otpskey");
		
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("user_no", user_no);
		searchMap.put("otpskey", otpskey);
		
		// User Log 남기기
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "LOGIN");		
		userLog.put("user_ip", clientIP);
		userLog.put("job_info", "OTP_User_Create_Success_"+user_no);
		userLog.put("logFlag", "0");
		
		int resultInt = 0;
		try {
			 resultInt = dao.UserOTPInsert(searchMap);
			 
			 if(resultInt > 0 ) {
				resultMap.put("resultCode", 0);
				resultMap.put("resultMessage", "User key Update");
				userDao.insertLog(userLog);
			 }else {
				resultMap.put("resultCode", -1);
				resultMap.put("resultMessage", "User key Update Error");
			 }
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("SQL Insert Error ::: " + e.getLocalizedMessage());
		}
				
		return resultMap;
	}

	@Override
	public Map<String, Object> selectMember(HttpServletRequest request) throws Exception {

		// Session clear
		SessionUtil.closeSession("memberSession");
		SessionUtil.closeSession("picSession");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		String user_no = request.getParameter("user_no");
		String password = request.getParameter("password");
		String optKey = request.getParameter("optKey");
		
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("user_no", user_no);
		searchMap.put("password", password);
		searchMap.put("optKey", optKey);
		
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
		
		boolean sKeyChk = false;
		
		try {
			
			if( memberMap.get("SECRET_KEY") == null ) {
				resultMap.put("resultCode", -1);
				resultMap.put("resultMessage", "등록되지 않은 사용자");
				userLog.put("job_info", "Log-In Fail(SECRET_KEY is Null)");
				userDao.insertLog(userLog);
				return resultMap;
			}
			searchMap.put("selectKey", memberMap.get("SECRET_KEY"));
			sKeyChk = UserOTPKey(searchMap);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		if(!sKeyChk) {
			resultMap.put("resultCode", 1);
			resultMap.put("resultMessage", "올바르지 않는 인증 키");
			userLog.put("job_info", "Log-In Fail(Invalid OTP Key)");
			userDao.insertLog(userLog);
			return resultMap;
		}

		// 최고관리자(USER_GRADE=9) IP 체크
		if(!user_no.equals("frentree")) {
			if ((memberMap.get("USER_GRADE").equals("9"))) {

				String accessIP = (String) memberMap.get("ACCESS_IP");

				if(accessIP == null || accessIP.equals("")) {
					resultMap.put("resultCode", -2);
					resultMap.put("resultMessage", "최고관리자의 접근IP가 없습니다.");

					userLog.put("job_info", "Log-In Fail(Access IP is Null)");
					userDao.insertLog(userLog);
					return resultMap;
				}

				String[] accessIPs = accessIP.split(",");
				if (!Arrays.asList(accessIPs).contains(clientIP)) {
					resultMap.put("resultCode", -3);
					resultMap.put("resultMessage", "등록되지 않은 최고관리자 접근IP입니다.");
					userLog.put("job_info", "Log-In Fail(Access IP is Invalid)");
					userDao.insertLog(userLog);
					return resultMap;
				}
			}
		}

		requestMap.put("version", pic_version);
		Map<String, Object> version = userDao.getversion(requestMap);
		picMap.put("version", version);
		
		resultMap.put("resultCode", 0);
		resultMap.put("resultMessage", "로그인 성공");
		resultMap.put("user_grade", memberMap.get("USER_GRADE"));
		resultMap.put("member", memberMap);

		// 권한 7은 결재 목록 페이지로 이동
		String userGrade = String.valueOf(memberMap.get("USER_GRADE"));
		if ("7".equals(userGrade)) {
			resultMap.put("redirectUrl", "/approval/pi_search_approval_list");
		} else {
			resultMap.put("redirectUrl", "/picenter_target");
		}

		logger.info("resultMap 확인 = "+ resultMap);

		// 사용자 세션 등록
		SessionUtil.setSession("memberSession", memberMap);
		SessionUtil.setSession("picSession", picMap);

		setHeader(memberMap);
		logger.info("로그인 완료");

		return resultMap;
	}

	private Map<String, Object> UserOTPCreate(String user_no) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        final GoogleAuthenticatorKey key = gAuth.createCredentials();

        String userName = user_no; // 사용자 이메일 또는 사용자 이름
        String host = pic_version; // 애플리케이션 또는 회사 도메인

        String qrUrl = GoogleAuthenticatorQRGenerator.getOtpAuthURL(host, userName, key);
        String secretKey = key.getKey();
        
        try {
        	String qrCodeData = "otpauth://totp/" + userName + "?secret=" + secretKey + "&issuer=" + host;
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, 2500, 2500);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();

            // Base64 인코딩으로 전송
            String base64Image = Base64.getEncoder().encodeToString(pngData);
            qrUrl = base64Image;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("error ::: " + e.getLocalizedMessage());
		}
                
        Map<String, Object> OTPMap = new HashMap<String, Object>();
        OTPMap.put("qrUrl", qrUrl);
        OTPMap.put("secretKey", secretKey);
        
        return OTPMap;
        
    }

	private boolean UserOTPKey(Map<String, Object> requestMap) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        String secretKey = requestMap.get("selectKey").toString(); // 사용자별 비밀키
        int verificationCode = Integer.parseInt(requestMap.get("optKey").toString()); // 사용자가 입력한 OTP 코드

        boolean isCodeValid = gAuth.authorize(secretKey, verificationCode);
        
        logger.info("isCodeValid ::: " + isCodeValid);
        
        return isCodeValid;
    }
	
	private void setHeader(Map<String, Object> memberMap) {
		List<Map<String, Object>> headerList = new ArrayList<>();
		Map<String, Object> loginPolicyMap = new HashMap<>();
		
		headerList = userDao.setHeader(memberMap);
		loginPolicyMap  = userDao.selectAccountPolicy();
		
		SessionUtil.setSession("loginPolicyMap", loginPolicyMap);
		SessionUtil.setSession("headerList", headerList);
		
		Map<String, Object> pageData = userDao.setPageData(memberMap);
		
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
