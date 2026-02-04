package com.org.iopts.samsung.service.impl;
 
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.ibatis.io.Resources;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.org.iopts.dao.Pi_UserDAO;
import com.org.iopts.detection.dao.piApprovalDAO;
import com.org.iopts.detection.vo.patternVo;
import com.org.iopts.exception.dao.piDetectionListDAO;
import com.org.iopts.samsung.dao.piSamsungDAO;
import com.org.iopts.samsung.service.piSamsungService;
import com.org.iopts.service.Pi_UserServiceImpl;
import com.org.iopts.setting.dao.Pi_SetDAO;
import com.org.iopts.util.ReconUtil;
import com.org.iopts.util.ServletUtil;
import com.org.iopts.util.SessionUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service("samsungService")
@Transactional
public class piSamsungServiceImple implements piSamsungService {
	
	private static Logger logger = LoggerFactory.getLogger(piSamsungServiceImple.class);
	
	@Inject
	private piSamsungDAO dao;
	
	@Inject
	private Pi_SetDAO setDao;
	
	@Inject
	private Pi_UserDAO userDao;
	
	@Inject
	private piDetectionListDAO detectionlistdao;
	
	@Value("${approval.url}")
	private String apiurl;
	
	@Value("${recon.api.version}")
    private String api_ver;
	
	@Value("${pic_version}")
	private String pic_version;
	
	@Value("${picenter.url}")
	private String picenter_url;
	
	@Value("${user.key}")
	private String key;
	
	@Inject
	private piApprovalDAO approvalDao;
	
	
	
//	삼성화재 관리자 로그인 
	@Override
	public Map<String, Object> selectMember(HttpServletRequest request) throws Exception {

		// Session clear
		SessionUtil.closeSession("memberSession");
		SessionUtil.closeSession("picSession");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		String user_no = request.getParameter("user_no");
		String password = request.getParameter("password");
		
		String userNo = request.getHeader("SSO_SABUN");
		
		if(userNo == null ) {
			userNo = user_no;
		}
		
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("user_no", user_no);
		searchMap.put("password", password);
		
		// User Log 남기기
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", userNo);
		userLog.put("menu_name", "LOGIN");		
		userLog.put("user_ip", clientIP);
		userLog.put("job_info", "Log-In Success_"+userNo);
		userLog.put("logFlag", "0");
		
		Map<String, Object> requestMap = new HashMap<String, Object>();

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> memberMap = userDao.selectMember(searchMap);
		Map<String, String> uptMap = new HashMap<>();
		Map<String, Object> picMap = new HashMap<String, Object>();
		
		Map<String, Object> authMap = userDao.selectAccountPolicy();
		
		requestMap.put("version", pic_version);
		Map<String, Object> version = userDao.getversion(requestMap);
		picMap.put("version", version);
		
		
		// 사용자번호가 틀린 경우
		if ((memberMap == null) || (memberMap.size() == 0)) {
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", user_no + "는 존재하지 않는 ID 입니다.");
			
			userLog.put("job_info", "Log-In Fail(Invalid User No)");
			userDao.insertLog(userLog);
			return resultMap;
		}
		
		userNo = memberMap.get("USER_NO").toString();
		
		if (!(memberMap.get("USER_GRADE").equals("9"))) {
			resultMap.put("resultCode", -6);
			resultMap.put("resultMessage", "잘못된 접근 입니다.");
			
			userLog.put("job_info", "Log-In Fail(User Grade != 9)");
			userDao.insertLog(userLog);
			return resultMap;
		}
		
		boolean pwd_fail_chk = ((int) authMap.get("locks")) != 0;
		int pwd_fail_cnt = (int) authMap.get("lock_val");
		
		// 계정이 잠긴 경우
		if(pwd_fail_chk && memberMap.get("LOCK_ACCOUNT").equals("Y")) {
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "비밀번호 "+pwd_fail_cnt+"회 오류로 인해 10분간 사용하실수 없습니다.");
			return resultMap;	
		}
		
		
		if(memberMap.get("PASSWORD_CHECK").equals("X") && memberMap.get("ACCESS_IP").equals("")) {
			
			if(request.getHeader("SSO_SABUN") != null &&!request.getHeader("SSO_SABUN").equals(user_no) && !request.getHeader("SSO_LOGINID").equals(user_no)) {
				resultMap.put("resultCode", -9);
				resultMap.put("resultMessage", "최초 로그인은 드림포탈 로그인 후 동일한 세션의 브라우저로 로그인해 주시기 바랍니다.");
			}else {
				resultMap.put("resultCode", -8);
				resultMap.put("resultMessage", "설정된 비밀번호가 존재하지 않습니다.");
			}
			
			userLog.put("job_info", "Log-In Fail(PassWord,Access IP Null)");
			userDao.insertLog(userLog);
			return resultMap;		
			
		}else if(memberMap.get("PASSWORD_CHECK").equals("X")) {
			
			if(request.getHeader("SSO_SABUN") != null && !request.getHeader("SSO_SABUN").equals(user_no) && !request.getHeader("SSO_LOGINID").equals(user_no)) {
				resultMap.put("resultCode", -9);
				resultMap.put("resultMessage", "최초 로그인은 드림포탈 로그인 후 동일한 세션의 브라우저로 로그인해 주시기 바랍니다.");
			}else {
				resultMap.put("resultCode", -7);
				resultMap.put("resultMessage", "설정된 비밀번호가 존재하지 않습니다.");
			}

			userLog.put("job_info", "Log-In Fail(PassWord is Null)");
			userDao.insertLog(userLog);
			return resultMap;		
		}
//		
//		//장기 미사용 계정 잠금
//		if(memberMap.get("USE_CHK").toString().equals("Y")) { 
//			resultMap.put("resultCode", -1);
//			resultMap.put("resultMessage", "장기 미사용으로 인해 계정이 잠겼습니다.\n관리자에게 문의하시기 바랍니다.");
//			
//			uptMap.put("userNo", user_no);
//			uptMap.put("failed_count", "0");
//			uptMap.put("lockYn", "Y");
//			logger.info("lock account :: " + userDao.updateFailedCount(uptMap));
//			return resultMap;
//		}
		
		if(Integer.parseInt(memberMap.get("PWD_RESET_STATUS").toString()) == 1) {
			resultMap.put("resultCode", -5);
			resultMap.put("resultMessage", "초기화된 비밀번호");
			return resultMap;
		}
		//비밀번호 만료일 체크 - 시작
		if(memberMap.get("CHANGE_CHK").toString().equals("Y")) { 
			resultMap.put("resultCode", -5);
			resultMap.put("resultMessage", "비밀번호 사용일수가 "+(int) authMap.get("change_val")+"일이 지났습니다. 비밀번호를 변경해주세요.");
			return resultMap;
		}
		
		
		// 비밀번호가 틀린 경우
		if (!(memberMap.get("PASSWORD").equals("Y"))) {
            int failed_count = (Integer.parseInt(memberMap.get("FAILED_COUNT").toString())+1);
			resultMap.put("resultCode", -1);
            resultMap.put("resultMessage", "비밀번호가 존재하지 않습니다. 다시 확인 해주세요."+((pwd_fail_chk)==true?" \n로그인 실패 횟수 :"+(failed_count):""));

			userLog.put("job_info", "Log-In Fail(Invalid Password)");
			userDao.insertLog(userLog);
			
			if(pwd_fail_chk) {
				if(failed_count < pwd_fail_cnt) {
					uptMap.put("userNo", userNo);
					uptMap.put("failed_count", String.valueOf(failed_count));
					uptMap.put("lockYn", "N");
					if(failed_count >= pwd_fail_cnt) {
						uptMap.put("lockYn", "Y");
					}
				} else {
					resultMap.put("resultMessage", "비밀번호 5회 오류도 10분간 사용하실수 없습니다.");
					uptMap.put("userNo", userNo);
					uptMap.put("failed_count", "0");
					uptMap.put("lockYn", "Y");
				}
			}
			
			logger.info("password error count :: " + failed_count);
			logger.info("lock account :: " + userDao.updateFailedCount(uptMap));
			return resultMap;			
		}
		
		Date logindate = (Date) memberMap.get("LOGINDATE");
		Date toDay =  new Date(); // 오늘날짜
		
		Object str = memberMap.get("USER_GRADE");
		System.out.println("user_grade 확인 = "+str);
		
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
			if(!userNo.equals("frentree")) {
				if (!Arrays.asList(accessIPs).contains(clientIP)) {
					resultMap.put("resultCode", -3);
					resultMap.put("resultMessage", "등록되지 않은 최고관리자 접근IP입니다.");
					userLog.put("job_info", "Log-In Fail(Access IP is Invalid)");
					userDao.insertLog(userLog);
					return resultMap;			
				}
			}
		}
		
		
		userDao.insertLog(userLog);
		userDao.updateLoginData(userLog);
		
		uptMap.put("userNo", userNo);
		uptMap.put("failed_count", "0");
		uptMap.put("lockYn", "N");
		logger.info("lock account :: " + userDao.updateFailedCount(uptMap));
		
		resultMap.put("resultCode", 0);
		resultMap.put("resultMessage", "로그인 성공");
		resultMap.put("user_grade", str);
		resultMap.put("member", memberMap);
		logger.info("resultMap 확인 = "+ resultMap);
		
		// 사용자 세션 등록
		SessionUtil.setSession("memberSession", memberMap);
		SessionUtil.setSession("picSession", picMap);
		
		setHeader(memberMap);
		
		logger.info("로그인 완료");

		return resultMap;
	}
	
	@Override
	public Map<String, Object> checkMemberGrade(HttpServletRequest request) throws Exception {
		
		// Session clear
		SessionUtil.closeSession("memberSession");
		SessionUtil.closeSession("picSession");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		
		String user_no = request.getHeader("SSO_SABUN");
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("user_no", user_no);
		
		Map<String, Object> map = dao.checkMemberGrade(searchMap);
		
		// User Log 남기기
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "LOGIN_SSO");		
		userLog.put("user_ip", clientIP);
		userLog.put("job_info", "Log-In Success_"+user_no);;
		userLog.put("logFlag", "0");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if ((map == null) || (map.size() == 0)) {
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "등록되지 않은 사용자 입니다.");
			
			userLog.put("job_info", "Log-In Fail(Unregistered Users)");
			userDao.insertLog(userLog);
			return resultMap;
		}
		
		Object str = map.get("USER_GRADE");
		System.out.println("user_grade 확인 = "+str);
		
		if(!str.equals("9")) { // 보안관리자의 경우 로그인 페이지로 이동
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "로그인 성공");
			resultMap.put("user_grade", str);
		}else {
			userLog.put("menu_name", "LOGIN");
			
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "로그인 성공");
			resultMap.put("user_grade", str);
		}
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> SSOSelectMember(HttpServletRequest request) throws Exception {

		// Session clear
		SessionUtil.closeSession("memberSession");
		SessionUtil.closeSession("picSession");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		String user_no = request.getHeader("SSO_SABUN");
		String sso_ip = request.getHeader("IV-REMOTE-ADDRESS");
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("user_no", user_no);
		
		// User Log 남기기
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "SSO LOGIN");
		userLog.put("user_ip", clientIP);
		userLog.put("job_info", "SSO Log-In Success"+user_no);
		userLog.put("logFlag", "0");
		
		Map<String, Object> requestMap = new HashMap<String, Object>();

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> memberMap = dao.SSOSelectMember(searchMap);
		
		if (sso_ip != null) {
			
			String[] accessIPs = sso_ip.split(",");
			if (!Arrays.asList(accessIPs).contains(clientIP)) {
				resultMap.put("resultCode", -3);
				resultMap.put("resultMessage", "등록되지 않은 접근IP입니다.");
				userLog.put("job_info", "Log-In Fail(Access IP is Invalid)");
				userDao.insertLog(userLog);
				return resultMap;			
			}
		}else {
			resultMap.put("resultCode", -2);
			resultMap.put("resultMessage", "접근가능 IP가 없습니다.");
			
			userLog.put("job_info", "Log-In Fail(Access IP is Null)");
			userDao.insertLog(userLog);
			return resultMap;			
		}
		
				
		Map<String, String> uptMap = new HashMap<>();
		
		Map<String, Object> picMap = new HashMap<String, Object>();
		logger.info("pic_version >>>> " + pic_version);
		requestMap.put("version", pic_version);
		Map<String, Object> version = userDao.getversion(requestMap);
		picMap.put("version", version);
		
		
		Date logindate = (Date) memberMap.get("LOGINDATE");
		Date toDay =  new Date(); // 오늘날짜
		
		Object str = memberMap.get("USER_GRADE");
		
		userDao.insertLog(userLog);
		userDao.updateLoginData(userLog);
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
	public Map<String, Object> selectDocuNum(HashMap<String, Object> params) throws Exception {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		params.put("user_no", user_no);

		Map<String, Object> memberMap = approvalDao.selectDocuNum(params);
		logger.info("selectDocuNum : " + memberMap);

		return memberMap;
		
	}

	@Override
	public HashMap<String, Object>  registProcessCharge(HashMap<String, Object> params) throws Exception
	{		
//		String approvalList = request.getParameter("approvalList");
		
		ReconUtil reconUtil = new ReconUtil();
		Map<String, Object> httpsResponse = null;
		JSONParser parser = new JSONParser();
		JsonObject jsonObject = null;
		
		List<HashMap<String, Object>> resultList = new ArrayList<>();
		HashMap<String, Object> mapData =  new HashMap<String, Object>();
		List<patternVo> pattern_list = new ArrayList();
		
		String match_data ="";
		String match_detail = "";
		String mask_data = "";
		String resultData = "";
		
//		결재에 들어갈 변수
		String detectionData =""; 
		String detailData = "";
		String detailDataPatternName = "";
		String agent_nm =""; 
		String agent_ip =""; 
		String service_nm =""; 
		String path ="";
		
		Gson gson = new Gson();
		
		int matchLimit = 100;
		
		try {
			String user_no = SessionUtil.getSession("memberSession", "USER_NO");
			String approvalList = (String) params.get("approvalList");
			
			params.put("user_no", user_no);
			
			// 기안자 정보
			Map<String, Object> map = approvalDao.selectProcessingSeq(params);
			int index = Integer.parseInt(map.get("ID").toString());
			String approvalID = map.get("ID_LENGTH").toString();
			String user_name = map.get("USER_NAME").toString();
			String regdate = map.get("REGDATE").toString();
			
			// 결재 상태 구분
			List<HashMap<String, Object>> mapCount = approvalDao.selectApprovalCount(params);
			
			Map<String, Object> matchData = new HashMap<>();
			HashMap<String, Object> dataMap = new HashMap<>();
			List<HashMap<String, Object>> apiDataList = new ArrayList<>();
			
			int max_cnt = 0;
			for(int i=0 ; i < mapCount.size() ; i++) {
				dataMap = new HashMap<>();
				
				if(i < 51) {
					dataMap.put("target_id", mapCount.get(i).get("TARGET_ID"));
					dataMap.put("hash_id", mapCount.get(i).get("HASH_ID"));
					dataMap.put("ap_no", mapCount.get(i).get("AP_NO"));
					dataMap.put("key", key);
					
					// api 데이터 조회
					matchData = approvalDao.selectApprovalData(dataMap);
					dataMap.put("fid", matchData.get("FID"));
					dataMap.put("name", mapCount.get(i).get("NAME"));
					dataMap.put("hashId", matchData.get("HASH_ID"));
					dataMap.put("path", matchData.get("PATH"));
					dataMap.put("subpath", matchData.get("SUBPATH"));
					
					dataMap.put("METAS", matchData.get("METAS"));
					dataMap.put("CHUNKS", matchData.get("CHUNKS"));
					dataMap.put("MATCHS", matchData.get("MATCHS"));
					
					
					apiDataList.add(dataMap);
				}else {
					++max_cnt;
				}
			}
			
			Map<String, Object> contentList = setDao.selectContentList("approval");
			
			logger.info("contentList :::: " + contentList);
			
			String approvalTitle = contentList.get("NAME").toString();
			String approvalCon = contentList.get("CON").toString();
			
			logger.info("apiDataList >>>> " + apiDataList.size());
//			logger.info("apiDataList ::: " + apiDataList);
			
			for (HashMap<String, Object> aList : apiDataList) {
				
				if(aList.get("fid").toString() == null) {
					continue;
				}
				
				
				byte[] chunksBytes = null;
				byte[] matchsBytes = null;
				
				HashMap<String, Object> agentMap = new HashMap<>();
				HashMap<String, Object> findMap = new HashMap<>();
				
				int ap_no = Integer.parseInt(aList.get("ap_no").toString());
				String target_id = aList.get("target_id").toString();
				String fid = aList.get("fid").toString();
				String hash_id = aList.get("hashId").toString();
				boolean subpath = Integer.parseInt(aList.get("hashId").toString()) == 1 ? true : false;
				
				
				// aList에서 Blob 객체 가져오기
			    Object chunksObj = aList.get("CHUNKS");
			    Object matchsObj = aList.get("MATCHS");

			    // CHUNKS 처리
			    if (chunksObj instanceof Blob) {
			        Blob chunksBlob = (Blob) chunksObj;
			        chunksBytes = chunksBlob.getBytes(1, (int) chunksBlob.length());
			    } else if (chunksObj instanceof byte[]) {
			        chunksBytes = (byte[]) chunksObj;
			    }

			    // MATCHS 처리
			    if (matchsObj instanceof Blob) {
			        Blob matchsBlob = (Blob) matchsObj;
			        matchsBytes = matchsBlob.getBytes(1, (int) matchsBlob.length());
			    } else if (matchsObj instanceof byte[]) {
			        matchsBytes = (byte[]) matchsObj;
			    }
			      
				String metasString = null;
				
				if( aList.get("METAS") != null && aList.containsKey("METAS")) {
					metasString = aList.get("METAS").toString();
				} 
				
				path = aList.get("path").toString();
				
				agentMap.put("ap_no", ap_no+"");
				agentMap.put("target_id", target_id);
						
				Map<String, Object> agentResultMap = approvalDao.selectAgentData(agentMap);
				
				agent_ip = agentResultMap.get("AGENT_CONNECTED_IP").toString();
				service_nm = agentResultMap.get("SERVICE_NM").toString();
				agent_nm = agentResultMap.get("NAME").toString();
				
				findMap.put("target_id", target_id); 
				findMap.put("hash_id", hash_id);
				findMap.put("ap_no", ap_no);
				findMap.put("fid", fid); 
				detailData ="";
				detectionData ="";

				if(subpath) {
					detailData ="<a href=\"https://picenter.samsungfire.com:8443/popup/lowPath?hash_id="+hash_id+"&ap_no="+ap_no+"&tid="+target_id+"\" onclick=\"window.open(this.href, '_blank', 'width=1142, height=365'); return false; \">상세내용 바로가기</a>";
				}else {
					detailData ="<a href=\"https://picenter.samsungfire.com:8443/popup/approvalDetail?id="+hash_id+"&ap_no="+ap_no+"&tid="+target_id+"\" onclick=\"window.open(this.href, '_blank', 'width=1142, height=365'); return false; \">상세내용 바로가기</a>";
				}
				
				
				detailDataPatternName ="";
				detectionData ="";
				
				// 커스텀 데이터
				pattern_list = approvalDao.customPatternList();
			
				try {
					if(chunksBytes != null) {
				        JsonArray chunks = new JsonArray();
				        JsonArray matchs = new JsonArray();
				        JsonArray metas = new JsonArray();

				        if (metasString != null) {
				            metas = gson.fromJson(metasString, JsonArray.class);
				            for (JsonElement jsonElement : metas) {
				                JsonObject meta = jsonElement.getAsJsonObject();

				                if (Boolean.parseBoolean(meta.get("is_aggregate").getAsString())) {
				                    String metaLabel = meta.get("label").getAsString();
				                    for (patternVo pattern : pattern_list) {
				                        if (pattern.getPattern_code().toUpperCase().equals(metaLabel.toUpperCase())) {
				                            int patternCnt = meta.get("value").getAsInt();
				                            pattern.setPattern_cnt(patternCnt);
				                            detailDataPatternName += pattern.getPattern_kr_name() + " : " + patternCnt + "건 <br>";
				                        }
									}
								}
							}
						}
						
				     // 검출 데이터
				        if (matchsBytes != null) {
				            matchs = gson.fromJson(new String(matchsBytes, StandardCharsets.UTF_8), JsonArray.class);
				            for (JsonElement jsonElement : matchs) {
				                String content = "";
				                JsonObject match = jsonElement.getAsJsonObject();
				                for (patternVo pattern : pattern_list) {
				                    if (pattern.getPattern_code().toUpperCase().equals(match.get("data_type").getAsString().toUpperCase())) {											
										String mask_con =  match.get("content").toString();
										String maskType = pattern.getMask_type();
										
										if(maskType != null && maskType != "N") {
											mask_con = detectionData(maskType, pattern.getMask_cnt(), pattern.getMask_chk(), mask_con);
										}
										
										if(pattern.getPattern_data() != null) {
											content = pattern.getPattern_data() + "\n" + mask_con;
										}else {
											content = mask_con;
										}
										
										if(pattern.getMatchLimitCnt() < matchLimit) {
											pattern.setPattern_data(content);
											pattern.setMatchLimitCnt(pattern.getMatchLimitCnt()+1);
										}
									} 
								}
								
							}
							
							for (patternVo pattern : pattern_list) {
								if(pattern.getPattern_cnt() > 0) {
									detectionData +=  pattern.getPattern_kr_name() + " [" + pattern.getPattern_cnt() + "]<br>";
									
									if( pattern.getPattern_data() != null) {
										detectionData +=  pattern.getPattern_data() + "<br>";
									}
								}
							}
						}
						
						mapData =  new HashMap<String, Object>();
						mapData.put("path", path);
						mapData.put("match_detail", detailDataPatternName+detailData);
						mapData.put("detectionData", detectionData);
						resultList.add(mapData);
						
					}
					
				} catch (Exception e) {
					logger.error("error ::: " + e.getMessage());
					e.printStackTrace();
				}
			
			}
			
			long currentTimeMills = Instant.now().toEpochMilli();
			String IFIDVALUE = "PIC"+currentTimeMills; // 유닉스타임 밀리세컨드
			
			params.put("index", (index+1));
			params.put("IFIDVALUE", IFIDVALUE);
			approvalDao.insertProcessingSeq(params); // 기안자 정보 저장
			
			// 결재 정보 생성
			approvalDao.registProcessCharge(params);
			
			String comment = (String) params.get("comment");
			JsonArray resultArray = gson.fromJson(approvalList, JsonArray.class);
			
			// 기안자 정보
			String approvalUser = "12|00|A|2023-11-02 17:00:52|"+user_no+"|"+user_name+"| |\n";
			
			// PICenter 생성 문서명
			String approvalNo = params.get("today") + "_" + params.get("user_no")+ "_" + params.get("doc_seq");
			
			// 결재 라인 사용자 정보
			String approvalData = "";
			
			int CHARGE_ID = approvalDao.selectChargeId(params);
			
			if (resultArray.size() != 0) {
//					결재 사용자 정보
				
				for (int i = 0; i < resultArray.size(); i++) {
					JsonObject resultMap = resultArray.get(i).getAsJsonObject();
					
					
					HashMap<String, Object> userListMap =  new HashMap<String, Object>();
					
					Object status = resultMap.get("STATUS");
					String statusStr = (status instanceof JsonElement)
					        ? ((JsonElement) status).getAsString()
					        : String.valueOf(status);
					userListMap.put("CHARGE_USER_FLAGE", statusStr);
					
					Object status2 = resultMap.get("STATUS2");
					String statusStr2 = (status2 instanceof JsonElement)
							? ((JsonElement) status2).getAsString()
									: String.valueOf(status2);
					
					Object ok_user_no = resultMap.get("USER_NO");
					String ok_user_noStr = (ok_user_no instanceof JsonElement)
							? ((JsonElement) ok_user_no).getAsString()
									: String.valueOf(ok_user_no);
					userListMap.put("ok_user_no", ok_user_noStr);
					
					Object ok_user_name = resultMap.get("USER_NAME");
					String ok_user_nameStr = (ok_user_name instanceof JsonElement)
							? ((JsonElement) ok_user_name).getAsString()
									: String.valueOf(ok_user_name);
					
					
					userListMap.put("CHARGE_ID", CHARGE_ID);
					userListMap.put("CHARGE_USER_CNT", i+1);
					userListMap.put("user_no", user_no);
					userListMap.put("IFIDVALUE", IFIDVALUE);
			        userListMap.put("approval_type", "1");
			        
					approvalDao.insertProcessUserList(userListMap);
					
					String num = "0";
					
					if(i>8) {
						num = "0"+(i+1);
					}else {
						num = ""+(i+1);
					}
					if(i == resultArray.size()-1) {
						approvalData += "12|"+num+"|"+statusStr2+"| |"+ok_user_noStr+"|"+ok_user_nameStr+"| |";
					}else {
						approvalData += "12|"+num+"|"+statusStr2+"| |"+ok_user_noStr+"|"+ok_user_nameStr+"| |\n";
					}
					
				}
			}
			
//			logger.info("resultList :: " + resultList);
			
			StringBuilder body = new StringBuilder();
			
			body.append("<!DOCTYPE html>");
			body.append("<head><title>"+approvalTitle+"</title>");
			body.append("<style>");
			body.append(".approval { border: 1px solid #DFDFE6; padding: 5px; }");
			body.append(".approval  div{ overflow: hidden; height: 100px; word-wrap:break-word; text-align: left;}");
			body.append(".result { text-align: right; border: 1px solid #DFDFE6; padding: 5px; }");
			body.append(".font_color { color: blue;}");
			body.append(".font_family {font-size:14px; white-space: pre-wrap;}");
			body.append("</style>");
			body.append("</head>");
			body.append("<body>");
			body.append("<div class='approval_div'>");
			body.append("<br><pre style=\"font-family: 'Noto Sans KR', sans-serif;\">&nbsp;"+approvalCon.replace("\n", "<br>")+"</pre><br>");
			body.append("<b><p>&nbsp;1) 문서번호</p></b>");
			body.append("<p style='padding-left: 17px;'>- " + approvalNo + "</p>");
			body.append("<b><p>&nbsp;2) 기안 의견 </p></b>");
			body.append("<p style='padding-left: 17px;'>- " + comment + "</p>");
			body.append("<br>");
			body.append("<b>검출 결과 </b>");
			body.append("<b>상세내용의 경우 PICenter 로그인 후 조회 가능합니다.</b>");
			body.append("<div style='padding-top:20px;'>");
			body.append("<table style='width: 1500px; border: 1px solid #DFDFE6; border-collapse: collapse; font-size: 14px; font-family: Noto SansKR; table-layout: fixed;'>");
			body.append("<colgroup><col width=\"10%\"><col width=\"10%\"><col width=\"10%\"><col width=\"20%\"><col width=\"25%\"><col width=\"25%\"></colgroup>");
			body.append("<thead><tr><th class='approval'>호스트명</th><th class='approval'>호스트 IP</th><th class='approval'>업무명</th><th class='approval'>경로</th><th class='approval'>상세정보</th><th class='approval'>검출 내역</th></tr></thead>");
			for(int i = 0; i < resultList.size(); i++) {
				body.append("<tr>");
				body.append("<td class='approval'>"+agent_nm+"</td>");
				body.append("<td class='approval'>"+agent_ip+"</td>");
				body.append("<td class='approval'>"+service_nm+"</td>");
				body.append("<td class='approval result'><div style=\"overflow-y: auto;\">>"+resultList.get(i).get("path")+"</div></td>");
				body.append("<td class='approval result'><div style=\"overflow-y: auto;\">"+ resultList.get(i).get("match_detail")+"</div></td>");
				body.append("<td class='approval result'><div style=\"overflow-y: auto;\">"+ resultList.get(i).get("detectionData")+"</div></td>");
				body.append("</tr>");
			}
			body.append("</table>");
			if(max_cnt > 0) {
				body.append("<b>&nbsp; +"+max_cnt+" </b><br><br>");
				body.append("<b>자세한 내용은 개인정보검출관리센터(PICenter)에서 확인 가능합니다.</b><br><br>");
			}
			body.append("<b>상세내용 : <a href='https://picenter.samsungfire.com:8443' target='_blank'><span style='color: #0000FF;'>개인정보검출관리센터(PICenter) 바로가기</span></a></b>");
			body.append("<p style=\"font-size: 13px; color: #999999; font-weight: bold; line-height: 13px;\">[결과 관리 > 조치계획 승인요청]</p>");
			body.append("</div>");
			body.append("</div>");
			body.append("</body>");
			body.append("</html>");
			
			String userid = user_no; // 기안자
			String systemid = "recon"; // 시스템 아이디
			String businessid = "picenter_1"; // 프로젝트 아이디
			String bodytype = "html"; // 고정
			String title = "[PICenter] "+approvalTitle; // 전자결재 제목
			String regidoc = "Y"; // 뭘까
			String legacyout = // 전자 결재 내용
					"<?xml version=\"1.0\" encoding=\"euc-kr\" ?>" +
					"<MYSINGLE>\n" + 
					"<LINE>" + 
					"<![CDATA[11|PIC"+currentTimeMills+"\n" + 
					approvalUser+ 
					approvalData+"]]>" + 
					"</LINE>" + 
					"<BODY>" + 
					"<![CDATA["
					+ body + "]]>" + 
					"</BODY>" + 
					"</MYSINGLE>";
			logger.info("html ::::: " + legacyout);
			
			String result = "userid="+userid+"&"+
					"systemid="+systemid+"&"+
					"businessid="+businessid+"&"+
					"bodytype="+bodytype+"&"+
					"title="+URLEncoder.encode(title, "EUC-KR")+"&"+
					"regidoc="+regidoc+"&"+
					"legacyout="+URLEncoder.encode(legacyout, "EUC-KR");
			
//			String result_check = "userid="+userid+"&"+
//					"systemid="+systemid+"&"+
//					"businessid="+businessid+"&"+
//					"bodytype="+bodytype+"&"+
//					"title="+approvalTitle+"&"+
//					"regidoc="+regidoc+"&"+
//					"legacyout="+legacyout;
			
			
//			logger.info("result :: " + result);
//			logger.info("result_check :: " + result_check);
			URL url = new URL(apiurl);
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			
			httpCon.setRequestMethod("POST");
			httpCon.setDoOutput(true);
			
			OutputStream out = httpCon.getOutputStream();
			
			out.write(result.getBytes("utf-8"));
			out.close();
			
			// 서버 응답 코드
			int responseCode = httpCon.getResponseCode();
			logger.info("responseCode ::: " + responseCode);
			
			// 전자결재 반환 값
			try(BufferedReader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream()))) {
				StringBuilder response = new StringBuilder();
				logger.info("Body");
				logger.info(response.toString());
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			params.put("resultCode", -1);
			
			return params;
		}
		
		params.put("resultCode", 0);
		return params;
	}

	
	@Override
	public HashMap<String, Object>  registProcessCharge2(HashMap<String, Object> params) throws Exception
	{		
//		String approvalList = request.getParameter("approvalList");
		
		ReconUtil reconUtil = new ReconUtil();
		Map<String, Object> httpsResponse = null;
		JsonObject jsonObject = null;
		
		List<HashMap<String, Object>> resultList = new ArrayList<>();
		HashMap<String, Object> mapData =  new HashMap<String, Object>();
		
		String match_data ="";
		String match_detail = "";
		String mask_data = "";
		String resultData = "";
		
//		결재에 들어갈 변수
		String detectionData =""; 
		String[] detectionDataList = null;
		String agent_nm =""; 
		String agent_ip =""; 
		String service_nm =""; 
		String path ="";
		
		Gson gson = new Gson();
		
		try {
			String user_no = SessionUtil.getSession("memberSession", "USER_NO");
			String approvalList = (String) params.get("approvalList");
			
			params.put("user_no", user_no);
			
			// 기안자 정보
			Map<String, Object> map = approvalDao.selectProcessingSeq(params);
			int index = Integer.parseInt(map.get("ID").toString());
			String approvalID = map.get("ID_LENGTH").toString();
			String user_name = map.get("USER_NAME").toString();
			String regdate = map.get("REGDATE").toString();
			
			// 결재 상태 구분
			List<HashMap<String, Object>> mapCount = approvalDao.selectApprovalCount2(params);
			
			Map<String, Object> matchData = new HashMap<>();
			HashMap<String, Object> dataMap = new HashMap<>();
			List<HashMap<String, Object>> apiDataList = new ArrayList<>();
			
			List<String> groupChargId = new ArrayList<>();
			int max_cnt = 0;
			String pathGroupId = mapCount.get(0).get("DATA_PROCESSING_GROUP_IDX").toString();
			groupChargId.add(pathGroupId);
			String firstId = mapCount.get(0).get("TARGET_ID") + "_"+mapCount.get(0).get("AP_NO");
			String hostName = "";
			String server_nm = "";
			String exPath = "";
			String reson_status = "";
			String reson = "";
			int host_size = 0;
			
			for(int i=0 ; i < mapCount.size() ; i++) {
				dataMap = new HashMap<>();
					
				if(i < 51) {
					
					logger.info("check0");
					String groupID = mapCount.get(i).get("DATA_PROCESSING_GROUP_IDX").toString();
					if(!pathGroupId.equals(groupID)) { // 같지 않을때 이전데이터 저장
						logger.info("check1");
						
						List<HashMap<String, Object>> hostList = approvalDao.selectApprovalHost(pathGroupId);
						dataMap.put("reson_status", reson_status);
						dataMap.put("exPath", exPath+"<br>");
						dataMap.put("reson", reson);
						dataMap.put("host_size", host_size);
						dataMap.put("hostList", hostList);
						apiDataList.add(dataMap);  
						
						pathGroupId = mapCount.get(i).get("DATA_PROCESSING_GROUP_IDX").toString();
						groupChargId.add(pathGroupId);
						exPath = mapCount.get(i).get("HASH_ID").toString()+"<br>";  
						host_size = 1;
						
					}else if((i+1) == mapCount.size() || i ==51){
						logger.info("check2");
						
						List<HashMap<String, Object>> hostList = approvalDao.selectApprovalHost(pathGroupId);
						exPath+= mapCount.get(i).get("HASH_ID").toString()+"<br>"; // 예외 경로
						
						dataMap.put("reson_status", reson_status);
						dataMap.put("exPath", exPath+"<br>");
						dataMap.put("reson", reson);
						dataMap.put("host_size", host_size);
						dataMap.put("hostList", hostList);
						apiDataList.add(dataMap);
						
					}else { // 이전과 같은 구분
						logger.info("check3");  
						exPath+= mapCount.get(i).get("HASH_ID").toString()+"<br>"; // 예외 경로
						reson_status = mapCount.get(i).get("PROCESSING_FLAG_NAME").toString(); // 예외 구분
						reson = mapCount.get(i).get("NOTEPAD").toString(); // 사유
						++host_size;
					}
					reson_status = mapCount.get(i).get("PROCESSING_FLAG_NAME").toString();
					reson = mapCount.get(i).get("NOTEPAD").toString();
					
				}else {
					++max_cnt;
				}
			}
			
			Map<String, Object> contentList = setDao.selectContentList("approval_ex");
			
//			logger.info("contentList :::: " + contentList);
			
			String approvalTitle = contentList.get("NAME").toString();
			String approvalCon = contentList.get("CON").toString();
			
			long currentTimeMills = Instant.now().toEpochMilli();
			String IFIDVALUE = "PIC"+currentTimeMills; // 유닉스타임 밀리세컨드
			
			params.put("index", (index+1));
			params.put("IFIDVALUE", IFIDVALUE);
			approvalDao.insertProcessingSeq(params); // 기안자 정보 저장
			
			// 결재 정보 생성
			approvalDao.registProcessCharge(params);
			
			String comment = (String) params.get("comment");
			JsonArray resultArray = gson.fromJson(approvalList, JsonArray.class);
//			JsonArray resultArray = new Gson().toJsonTree(approvalList).getAsJsonArray();
			
			// 기안자 정보
			String approvalUser = "12|00|A|2023-11-02 17:00:52|"+user_no+"|"+user_name+"| |\n";
			
			// PICenter 생성 문서명
			String approvalNo = params.get("today") + "_" + params.get("user_no")+ "_" + params.get("doc_seq");
			
			// 결재 라인 사용자 정보
			String approvalData = "";
			
			int CHARGE_ID = approvalDao.selectChargeId(params);
			
			if (resultArray.size() != 0) {
//					결재 사용자 정보
				
				for (int i = 0; i < resultArray.size(); i++) {
					JsonObject resultMap = resultArray.get(i).getAsJsonObject();
					
					HashMap<String, Object> userListMap =  new HashMap<String, Object>();
					
					Object status = resultMap.get("STATUS");
					String statusStr = (status instanceof JsonElement)
					        ? ((JsonElement) status).getAsString()
					        : String.valueOf(status);
					userListMap.put("CHARGE_USER_FLAGE", statusStr);
					
					Object status2 = resultMap.get("STATUS2");
					String statusStr2 = (status2 instanceof JsonElement)
							? ((JsonElement) status2).getAsString()
									: String.valueOf(status2);
					
					Object ok_user_no = resultMap.get("USER_NO");
					String ok_user_noStr = (ok_user_no instanceof JsonElement)
							? ((JsonElement) ok_user_no).getAsString()
									: String.valueOf(ok_user_no);
					userListMap.put("ok_user_no", ok_user_noStr);
					
					Object ok_user_name = resultMap.get("USER_NAME");
					String ok_user_nameStr = (ok_user_name instanceof JsonElement)
							? ((JsonElement) ok_user_name).getAsString()
									: String.valueOf(ok_user_name);
					
					userListMap.put("CHARGE_ID", CHARGE_ID);
					userListMap.put("CHARGE_USER_CNT", i+1);
					userListMap.put("user_no", user_no);
					userListMap.put("IFIDVALUE", IFIDVALUE);
					userListMap.put("approval_type", "2");
					approvalDao.insertProcessUserList(userListMap);
					
					String num = "0";
					
					if(i>8) {
						num = "0"+(i+1);
					}else {
						num = ""+(i+1);
					}
					if(i == resultArray.size()-1) {
						approvalData += "12|"+num+"|"+statusStr2+"| |"+ok_user_noStr+"|"+ok_user_nameStr+"| |";
					}else {
						approvalData += "12|"+num+"|"+statusStr2+"| |"+ok_user_noStr+"|"+ok_user_nameStr+"| |\n";
					}
					
				}
			}
			
			for(int g = 0 ; g < groupChargId.size() ; g++) {
				HashMap<String, Object> groupMap =  new HashMap<String, Object>();
				groupMap.put("CHARGE_ID", CHARGE_ID);
				groupMap.put("groupChargId", groupChargId.get(g));
				groupMap.put("comment", comment);
				
				approvalDao.insertGroupChageId(groupMap);
			}
			
			StringBuilder body = new StringBuilder();
			
			body.append("<!DOCTYPE html>");
			body.append("<head><title>"+approvalTitle+"</title>");
			body.append("<style>");
			body.append(".approval { border: 1px solid #DFDFE6; padding: 5px; }");
			body.append(".approval  div{ overflow: hidden; height: 100px; word-wrap:break-word; text-align: left;}");
			body.append(".result { text-align: right; border: 1px solid #DFDFE6; padding: 5px; }");
			body.append(".font_color { color: blue;}");
			body.append(".font_family {font-size:14px; white-space: pre-wrap;}");
			body.append("</style>");
			body.append("</head>");
			body.append("<body>");
			body.append("<div class='approval_div'>");
			body.append("<br><pre style=\"font-family: 'Noto Sans KR', sans-serif;\">&nbsp;"+approvalCon.replace("\n", "<br>")+"</pre><br>");
			body.append("<b><p>&nbsp;1) 문서번호</p></b>");
			body.append("<p style='padding-left: 17px;'>- " + approvalNo + "</p>");
			body.append("<b><p>&nbsp;2) 기안 의견 </p></b>");
			body.append("<p style='padding-left: 17px;'>- " + comment + "</p>");
			body.append("<br>");
			body.append("<b>검출 결과 </b>");
			body.append("<div style='padding-top:20px;'>");
			body.append("<table style='width: 1500px; border: 1px solid #DFDFE6; border-collapse: collapse; font-size: 14px; font-family: Noto SansKR; table-layout: fixed;'>");
			body.append("<colgroup><col width=\"15%\"><col width=\"10%\"><col width=\"10%\"><col width=\"25%\"><col width=\"15%\"><col width=\"35%\"></colgroup>");
			body.append("<thead><tr><th class='approval'>호스트명</th><th class='approval'>호스트 IP</th><th class='approval'>업무명</th><th class='approval'>예외경로</th><th class='approval'>사유</th><th class='approval'>상세 사유</th></tr></thead>");
			
			logger.info("apiDataList :: " + apiDataList);
			for(int i = 0; i < apiDataList.size(); i++) {  
				int hostSize = Integer.parseInt(apiDataList.get(i).get("host_size").toString());
				List<Map<String, Object>> list = (List<Map<String, Object>>) apiDataList.get(i).get("hostList");
				for(int h = 0; h < list.size() ; h++) {
					body.append("<tr>");
					body.append("<td class='approval'>"+list.get(h).get("NAME")+"</td>");
					body.append("<td class='approval'>"+list.get(h).get("AGENT_CONNECTED_IP")+"</td>");
					body.append("<td class='approval'>"+list.get(h).get("SERVICE_NM")+"</td>");
					if(h == 0) {
						body.append("<td class='approval' rowspan="+list.size()+">"+apiDataList.get(i).get("exPath")+"</td>");
						body.append("<td class='approval' rowspan="+list.size()+">"+apiDataList.get(i).get("reson_status") +"</td>");
						body.append("<td class='approval' rowspan="+list.size()+">"+apiDataList.get(i).get("reson")+"</td>");
						body.append("</tr>");
					}else {
						
						body.append("</tr>");
					}
				}
				
				
			}
			body.append("</table>");
			if(max_cnt > 0) {
				body.append("<b>&nbsp; +"+max_cnt+" </b><br><br>");
				body.append("<b>자세한 내용은 개인정보검출관리센터(PICenter)에서 확인 가능합니다.</b><br><br>");
			}
			body.append("<b>상세내용 : <a href='https://picenter.samsungfire.com:8443' target='_blank'><span style='color: #0000FF;'>개인정보검출관리센터(PICenter) 바로가기</span></a></b>");
			body.append("<p style=\"font-size: 13px; color: #999999; font-weight: bold; line-height: 13px;\">[결과 관리 > 조치계획 승인요청]</p>");
			body.append("</div>");
			body.append("</div>");
			body.append("</body>");
			body.append("</html>");
			
//			logger.info("body" + body);
			
			String userid = user_no; // 기안자
			String systemid = "recon"; // 시스템 아이디
			String businessid = "picenter_1"; // 프로젝트 아이디
			String bodytype = "html"; // 고정
			String title = "[PICenter] "+ approvalTitle; // 전자결재 제목
			String regidoc = "Y"; // 뭘까
			String legacyout = // 전자 결재 내용
					"<?xml version=\"1.0\" encoding=\"euc-kr\" ?>" +
					"<MYSINGLE>\n" + 
					"<LINE>" + 
					"<![CDATA[11|PIC"+currentTimeMills+"\n" + 
					approvalUser+ 
					approvalData+"]]>" + 
					"</LINE>" + 
					"<BODY>" + 
					"<![CDATA["
					+ body + "]]>" + 
					"</BODY>" + 
					"</MYSINGLE>";
			logger.info("html ::::: " + legacyout);
			
			String result = "userid="+userid+"&"+
					"systemid="+systemid+"&"+
					"businessid="+businessid+"&"+
					"bodytype="+bodytype+"&"+
					"title="+URLEncoder.encode(title, "EUC-KR")+"&"+
					"regidoc="+regidoc+"&"+
					"legacyout="+URLEncoder.encode(legacyout, "EUC-KR");
			
//			String result_check = "userid="+userid+"&"+
//					"systemid="+systemid+"&"+
//					"businessid="+businessid+"&"+
//					"bodytype="+bodytype+"&"+
//					"title="+title+"&"+
//					"regidoc="+regidoc+"&"+
//					"legacyout="+legacyout;
//			
//			
			logger.info("result :: " + result);
//			logger.info("result_check :: " + result_check);
//			
			URL url = new URL(apiurl);
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			
			httpCon.setRequestMethod("POST");
			httpCon.setDoOutput(true);
			
			OutputStream out = httpCon.getOutputStream();
			
			out.write(result.getBytes("utf-8"));
			out.close();
			
			// 서버 응답 코드
			int responseCode = httpCon.getResponseCode();
			logger.info("responseCode ::: " + responseCode);
			
			// 전자결재 반환 값
			try(BufferedReader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream()))) {
				StringBuilder response = new StringBuilder();
				logger.info("Body");
				logger.info(response.toString());
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			params.put("resultCode", -1);
			
			return params;
		}
		
		params.put("resultCode", 0);
		return params;
	}

	private String maskString(String pi_detection, int con_length, int type) {
		
		String prefix = "";
		int maskedLength = 0;
		if(type == 1) {
			prefix = pi_detection.substring(0, 4);
			maskedLength = 4;
		}else if (type == 2) {
			prefix = pi_detection.substring(con_length-8, con_length);
			maskedLength = 8;
		}
		
		String mask = "";
		for(int i = 0 ; i < maskedLength ; i++) {
			mask += "#";
		}
		  
		return prefix + mask + pi_detection.substring(con_length);
	}


	@Override
	public void updateProcessStatus(HashMap<String, Object> params, Map<String, Object> chargeMap) throws Exception
	{
		logger.info("updateProcessStatus 로그체크 1");

		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		List<String> idxList = (List<String>)params.get("idxList");

		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < idxList.size(); i++) 
		{
			map.put("idx", idxList.get(i));
			map.put("user_no", user_no);
			map.put("ok_user_no", params.get("ok_user_no"));
			map.put("apprType", params.get("apprType"));
			map.put("data_processing_charge_id", chargeMap.get("DATA_PROCESSING_CHARGE_ID"));
			map.put("data_processing_group_id", params.get("groupId"));
			map.put("comment", params.get("comment"));

			approvalDao.updateProcessingGroupStatus(map);
			approvalDao.updateProcessingStatus(map);
		}
	}
	
	
	/**
	 * 정탐/오탐 결재 리스트
	 */
	@Override
	public List<HashMap<String, Object>> searchApprovalListData(HashMap<String, Object> params) throws Exception {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");

		params.put("user_no", user_no);
		params.put("user_grade", user_grade);

		return approvalDao.searchApprovalListData(params);
	}
	
	/**
	 * 정탐/오탐 결재 리스트 - 조회
	 */
	@Override
	public List<HashMap<String, Object>> selectProcessGroupPath(HashMap<String, Object> params) throws Exception
	{
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");

		String charge_id = (String)params.get("CHARGE_ID_LIST");
		List<String> charge_id_list = new ArrayList<String>();
		if(charge_id != null && !"".equals(charge_id)) {
			StringTokenizer st = new StringTokenizer(charge_id,",");
			while(st.hasMoreTokens()) {
				charge_id_list.add(st.nextToken());
			}
		}

		params.put("user_no", user_no);
		params.put("user_grade", user_grade);
		params.put("charge_id_list", charge_id_list);

		return approvalDao.selectProcessGroupPath(params);
	}
	
	/**
	 * 정탐/오탐 결재 리스트 - 결재자 조회
	 */
	@Override
	public List<HashMap<String, Object>> selectApprovalUserList(HashMap<String, Object> params) throws Exception
	{
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		String charge_id = (String)params.get("CHARGE_ID_LIST");
		List<String> charge_id_list = new ArrayList<String>();
		if(charge_id != null && !"".equals(charge_id)) {
			StringTokenizer st = new StringTokenizer(charge_id,",");
			while(st.hasMoreTokens()) {
				charge_id_list.add(st.nextToken());
			}
		}
		
		params.put("user_no", user_no);
		params.put("user_grade", user_grade);
		params.put("charge_id_list", charge_id_list);
		
		return approvalDao.selectApprovalUserList(params);
	}
	
	@Override
	public List<HashMap<String, Object>> searchProcessList(HashMap<String, Object> params) throws Exception {

		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		params.put("user_no", user_no);			// 사용자
		params.put("user_grade", user_grade);			// 사용자

		return approvalDao.searchProcessList(params);
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
	
	
	private String detectionData(String maskType, int maskCnt, String maskChk, String pi_detection) {
		String resultData = pi_detection;

	    if (maskType.equals("T")) { // 앞글자 마스킹
	    	 if (maskChk != null && !maskChk.equals("")) {
	    		String regex =  escapeSpecialRegexChars(maskChk);
	         	String[] splitData = pi_detection.split(regex);

	         	StringBuilder resultBuilder = new StringBuilder(splitData[0]);

	            for (int i = 0; i < splitData.length - 1; i++) {
	                if (splitData[i].length() >= maskCnt) {
	                    String prefix = splitData[i].substring(0, splitData[i].length() - maskCnt);
	                    String mask = repeat('#', maskCnt);
	                    resultBuilder.append(prefix).append(mask).append(maskChk);
	                } else {
	                    resultBuilder.append(splitData[i]).append(maskChk);
	                }
	            }
	            resultBuilder.append(splitData[splitData.length - 1]);
	            resultData = resultBuilder.toString();
	        } else {
	            String prefix = pi_detection.substring(maskCnt);
	            String mask = repeat('#', maskCnt);
	            resultData = mask + prefix;
	        }
	    } else if (maskType.equals("B")) { // 뒷글자 마스킹
	        if (maskChk != null && !maskChk.equals("")) {
	        	
	        	String regex =  escapeSpecialRegexChars(maskChk);
	        	String[] splitData = pi_detection.split(regex);

	        	StringBuilder resultBuilder = new StringBuilder(splitData[0]);

	            for (int i = 1; i < splitData.length; i++) {
	                if (splitData[i].length() >= maskCnt) {
	                    String prefix = splitData[i].substring(maskCnt);
	                    String mask = repeat('#', maskCnt);
	                    resultBuilder.append(maskChk).append(mask).append(prefix);
	                } else {
	                    resultBuilder.append(maskChk).append(splitData[i]);
	                }
	            }
	            resultData = resultBuilder.toString();
	        } else {
	            String prefix = pi_detection.substring(0, pi_detection.length() - maskCnt);
	            String mask = repeat('#', maskCnt);
	            resultData = prefix + mask;
	        }
	    }

	    return resultData;
	}
	
	private static String escapeSpecialRegexChars(String str) {
        return str.replaceAll("([\\\\\\.\\[\\{\\(\\*\\+\\?\\^\\$\\|])", "\\\\$1");
    }

	public static String repeat(char ch, int count) {
	    StringBuilder builder = new StringBuilder();
	    for (int i = 0; i < count; i++) {
	        builder.append(ch);
	    }
	    return builder.toString();
	}
}