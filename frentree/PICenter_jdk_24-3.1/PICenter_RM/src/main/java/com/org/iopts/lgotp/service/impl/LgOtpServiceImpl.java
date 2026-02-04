package com.org.iopts.lgotp.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreammirae.gt.radius.client.GptwrAuthService;
import com.org.iopts.dao.Pi_UserDAO;
import com.org.iopts.lgotp.service.LgOtpService;
import com.org.iopts.service.Pi_UserService;
import com.org.iopts.util.ServletUtil;

@Service
@Transactional
public class LgOtpServiceImpl implements LgOtpService {

	private static Logger logger = LoggerFactory.getLogger(LgOtpServiceImpl.class);

	@Inject
	private Pi_UserDAO userDao;

	@Inject
	private Pi_UserService userService;

	@Value("${pic_version}")
	private String pic_version;

	@Value("${otp.lgotp.test.mode:false}")
	private String testModeStr;

	@Value("${otp.lgotp.test.code:123456}")
	private String testCode;

	@Value("${otp.lgotp.server.ip}")
	private String otpServerIp;

	@Value("${otp.lgotp.server.port}")
	private String otpServerPort;

	@Value("${otp.lgotp.shared.secret}")
	private String otpSharedSecret;

	@Value("${otp.lgotp.retry.cnt:1}")
	private String otpRetryCnt;

	@Value("${otp.lgotp.timeout:30}")
	private String otpTimeout;

	@Value("${otp.lgotp.nasid:null}")
	private String otpNasId;

	@Override
	public Map<String, Object> checkUserExists(String userNo, HttpServletRequest request) throws Exception {
		logger.info("LG OTP Step1: checkUserExists - userNo={}", userNo);

		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			HttpSession session = request.getSession();
			Map<String, Object> memberMap = userDao.selectUserByUserNo(userNo);

			if (memberMap == null || memberMap.isEmpty()) {
				resultMap.put("resultCode", -1);
				resultMap.put("resultMessage", "등록되지 않은 사용자입니다.\n관리자에게 문의해주세요.");
				logger.warn("LG OTP Step1 Failed: User not found - userNo={}", userNo);
				return resultMap;
			}
			
			if(Integer.parseInt(memberMap.get("LOCK_STATUS").toString()) == 1) { 
				resultMap.put("resultCode", -9);
				resultMap.put("resultMessage", "장기 미사용으로 인해 계정이 잠겼습니다.\n관리자에게 문의하시기 바랍니다.");
				return resultMap;
			}else if(Integer.parseInt(memberMap.get("LOCK_STATUS").toString()) == 2) {
				resultMap.put("resultCode", -8);
				resultMap.put("resultMessage", "장기 미사용으로 인해 계정이 잠겼습니다.\n관리자에게 문의하시기 바랍니다.");
				return resultMap;
			}
			
			
			// 로그 기록
			ServletUtil servletUtil = new ServletUtil(request);
			Map<String, Object> userLog = new HashMap<String, Object>();
			userLog.put("user_no", userNo);
			userLog.put("menu_name", "LOGIN_LG_OTP");
			userLog.put("user_ip", servletUtil.getIp());
			userLog.put("logFlag", "0");

			// USER_ID가 frentree인 경우 OTP 없이 바로 로그인
			String userId = (String) memberMap.get("USER_ID");
			if ("frentree".equals(userId)) {
				logger.info("frentree user - OTP bypass login");

				// 세션 설정
				session.setAttribute("memberSession", memberMap);

				Map<String, Object> picMap = new HashMap<String, Object>();
				Map<String, Object> requestMap = new HashMap<String, Object>();
				requestMap.put("version", pic_version);
				Map<String, Object> version = userDao.getversion(requestMap);
				picMap.put("version", version);
				session.setAttribute("picSession", picMap);

				// 헤더 설정
				setHeader(session, memberMap);

				userLog.put("job_info", "LG_OTP_Bypass_frentree");
				userDao.insertLog(userLog);

				resultMap.put("resultCode", 100); // frentree 전용 코드
				resultMap.put("resultMessage", "로그인 성공");

				// 권한 7은 결재 목록 페이지로 이동
				String userGrade = String.valueOf(memberMap.get("USER_GRADE"));
				if ("7".equals(userGrade)) {
					resultMap.put("redirectUrl", "/approval/pi_search_approval_list");
				} else {
					resultMap.put("redirectUrl", "/picenter_target");
				}

				logger.info("frentree login success - OTP bypassed");
				return resultMap;
			}

			// 세션에 사용자 정보 백업
			session.setAttribute("lg_otp_user_backup", memberMap);
			session.setAttribute("lg_otp_step1_success", "Y");
			session.setAttribute("lg_otp_input_userid", userNo);

			userLog.put("job_info", "LG_OTP_Step1_UserExists");
			userDao.insertLog(userLog);

			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "사용자 확인 완료 - OTP 번호를 입력해주세요.");
			logger.info("LG OTP Step1 Success: user_no={}", userNo);

		} catch (Exception e) {
			logger.error("LG OTP Step1 Error: userNo={}", userNo, e);
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "사용자 확인 중 오류가 발생했습니다.");
		}

		return resultMap;
	}

	@Override
	public Map<String, Object> authenticateOTP(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// Step1 완료 여부 확인
		String step1Success = (String) session.getAttribute("lg_otp_step1_success");
		if (!"Y".equals(step1Success)) {
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "사용자 확인을 먼저 진행해주세요.");
			return resultMap;
		}

		// 백업된 사용자 정보 가져오기
		@SuppressWarnings("unchecked")
		Map<String, Object> memberMap = (Map<String, Object>) session.getAttribute("lg_otp_user_backup");
		String inputUserNo = (String) session.getAttribute("lg_otp_input_userid");

		if (memberMap == null) {
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "세션 정보가 만료되었습니다. 다시 로그인해주세요.");
			return resultMap;
		}

		// USER_ID 가져오기
		String userId = (String) memberMap.get("USER_ID");
		if (userId == null) {
			userId = (String) memberMap.get("user_id");
		}

		// OTP 코드 가져오기
		String otpCode = request.getParameter("otp");
		if (otpCode == null || otpCode.trim().isEmpty()) {
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "OTP 코드를 입력해주세요.");
			return resultMap;
		}

		// IP 정보 가져오기
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();

		// OTP 인증
		String authResultCode;
		int authFailCnt = 0;
		String authResultMsg;

		boolean testMode = "true".equalsIgnoreCase(testModeStr);

		if (testMode) {
			// 테스트 모드
			logger.info("LG OTP Test Mode: user={}, testCode={}", userId, testCode);
			if (otpCode.equals(testCode)) {
				authResultCode = "0";
				authResultMsg = "OTP 인증 성공 (테스트 모드)";
				logger.info("LG OTP Test Mode Success");
			} else {
				authResultCode = "6001";
				authFailCnt = 1;
				authResultMsg = "OTP 인증 실패 (테스트 모드)";
				logger.warn("LG OTP Test Mode Failed: expected={}, actual={}", testCode, otpCode);
			}
		} else {
			// 실제 OTP 서버 인증
			logger.info("LG OTP Auth Start: user={}, ip={}, port={}", userId, otpServerIp, otpServerPort);
			String authResult = GptwrAuthService.auth(
					otpServerIp,
					otpServerPort,
					otpSharedSecret,
					userId,
					otpCode,
					otpRetryCnt,
					otpTimeout,
					"null".equals(otpNasId) ? null : otpNasId
			);
			logger.info("LG OTP Auth Result: {}", authResult);

			String[] resultParts = authResult.split("#");
			authResultCode = "6040";
			authResultMsg = "OTP 인증 실패";

			if (resultParts.length >= 3) {
				authResultCode = resultParts[0];
				authFailCnt = Integer.parseInt(resultParts[1]);
				authResultMsg = resultParts[2];
			}
		}

		// 로그 기록용 Map
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", inputUserNo);
		userLog.put("menu_name", "LOGIN_LG_OTP");
		userLog.put("user_ip", clientIP);
		userLog.put("logFlag", "0");

		// 인증 성공
		if ("0".equals(authResultCode)) {

			// 최고관리자(USER_GRADE=9) IP 체크
			if(!inputUserNo.equals("frentree")) {
				if (memberMap.get("USER_GRADE") != null && memberMap.get("USER_GRADE").equals("9")) {
					String accessIP = (String) memberMap.get("ACCESS_IP");

					if(accessIP == null || accessIP.equals("")) {
						resultMap.put("resultCode", -2);
						resultMap.put("resultMessage", "최고관리자의 접근IP가 없습니다.");

						userLog.put("job_info", "Log-In Fail(Access IP is Null)");
						userDao.insertLog(userLog);
						logger.warn("LG OTP Step2 Failed: Access IP is Null - user_no={}", inputUserNo);
						return resultMap;
					}

					String[] accessIPs = accessIP.split(",");
					if (!Arrays.asList(accessIPs).contains(clientIP)) {
						resultMap.put("resultCode", -3);
						resultMap.put("resultMessage", "등록되지 않은 최고관리자 접근IP입니다.");
						userLog.put("job_info", "Log-In Fail(Access IP is Invalid)");
						userDao.insertLog(userLog);
						logger.warn("LG OTP Step2 Failed: Access IP is Invalid - user_no={}, clientIP={}", inputUserNo, clientIP);
						return resultMap;
					}
				}
			}
			
			// 세션 설정
			session.setAttribute("memberSession", memberMap);

			Map<String, Object> picMap = new HashMap<String, Object>();
			Map<String, Object> requestMap = new HashMap<String, Object>();
			requestMap.put("version", pic_version);
			Map<String, Object> version = userDao.getversion(requestMap);
			picMap.put("version", version);
			session.setAttribute("picSession", picMap);

			// 헤더 설정
			setHeader(session, memberMap);

			// 임시 세션 정리
			session.removeAttribute("lg_otp_user_backup");
			session.removeAttribute("lg_otp_step1_success");
			session.removeAttribute("lg_otp_input_userid");

			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "OTP 인증 성공");

			// 권한 7은 결재 목록 페이지로 이동
			String userGrade = String.valueOf(memberMap.get("USER_GRADE"));
			if ("7".equals(userGrade)) {
				resultMap.put("redirectUrl", "/approval/pi_search_approval_list");
			} else {
				resultMap.put("redirectUrl", "/picenter_target");
			}

			userLog.put("job_info", "LG_OTP_Success");
			userDao.insertLog(userLog);
			logger.info("LG OTP Step2 Success: user_no={}", inputUserNo);

		} else {
			// 인증 실패
			resultMap.put("resultCode", Integer.parseInt(authResultCode));
			resultMap.put("resultMessage", authResultMsg);
			resultMap.put("authFailCnt", authFailCnt);

			userLog.put("job_info", "LG_OTP_Fail_" + authResultCode);
			userDao.insertLog(userLog);
			logger.warn("LG OTP Step2 Failed: user_no={}, code={}, msg={}", inputUserNo, authResultCode, authResultMsg);
		}

		return resultMap;
	}

	private void setHeader(HttpSession session, Map<String, Object> memberMap) {
		try {
			List<Map<String, Object>> headerList = userDao.setHeader(memberMap);
			Map<String, Object> loginPolicyMap = userDao.selectAccountPolicy();

			session.setAttribute("loginPolicyMap", loginPolicyMap);
			session.setAttribute("headerList", headerList);

			Map<String, Object> pageData = userDao.setPageData(memberMap);

			logger.info("pageData :: " + pageData.toString());
			logger.info("main :: " + pageData.get("MAIN"));
			String mainYn = pageData.get("MAIN").toString();
			session.setAttribute("mainYn", mainYn);
			if ("Y".equals(mainYn)) {
				session.setAttribute("defaultPage", "/picenter_target");
			} else {
				session.setAttribute("defaultPage", pageData.get("URL").toString());
			}
		} catch (Exception e) {
			logger.error("setHeader Error", e);
		}
	}
}
