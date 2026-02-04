package com.org.iopts.detection.service.impl;
 
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Iterator;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import jakarta.inject.Inject;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.InternetAddress;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.io.DataOutputAsStream;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.org.iopts.dao.Pi_DetectionDAO;
import com.org.iopts.detection.dao.piApprovalDAO;
import com.org.iopts.detection.service.piApprovalService;
import com.org.iopts.detection.vo.patternVo;
import com.org.iopts.exception.dao.piDetectionListDAO;
import com.org.iopts.setting.dao.Pi_SetDAO;
import com.org.iopts.setting.service.impl.Pi_SetServiceImpl;
import com.org.iopts.util.ReconUtil;
import com.org.iopts.util.SessionUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;

import java.io.InputStream;
import java.net.URL;

@Service("approvalService")
@Transactional
public class piApprovalServiceImple implements piApprovalService {
	
	private static Logger logger = LoggerFactory.getLogger(piApprovalServiceImple.class);
	
	@Inject
	private piDetectionListDAO detectionlistdao;
	
	@Inject
	private piApprovalDAO approvalDao;
	
	@Inject
	private Pi_SetDAO setDao;
	
	@Value("${approval.url}")
	private String apiurl;
	
	@Value("${recon.api.version}")
    private String api_ver;
	
	@Value("${send.host}")
	private String send_host;
	
	@Value("${send.port}")
	private String send_port;
	
	@Value("${user.key}")
	private String key;
	
	@Value("${picenter.ssl.enable}")
	private String mailEnable;
	@Value("${picenter.auth}")
	private String mailAuth;
	
	Pi_SetServiceImpl set_service = new Pi_SetServiceImpl();
	
	private static final String ALGORITHM = "AES";

	@Override
	public List<HashMap<String, Object>> searchProcessList(HashMap<String, Object> params) throws Exception {

		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		params.put("user_no", user_no);			// 사용자
		params.put("user_grade", user_grade);			// 사용자
		logger.info("params :::: " + params);
		
		if(params.get("NAME")!=null) {
			String[] nameList = params.get("NAME").toString().split(",");
			params.put("nameList", nameList);
		}
		List<HashMap<String, Object>> resultList =  new ArrayList<HashMap<String,Object>>();
		
		try {
			resultList = approvalDao.searchProcessList(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultList;
	}

	@Override
	public List<HashMap<String, Object>> selectTeamMember(HttpServletRequest request) throws Exception {
		HashMap<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("insa_code", SessionUtil.getSession("memberSession", "INSA_CODE"));
		searchMap.put("user_no", SessionUtil.getSession("memberSession", "USER_NO"));
		
		return approvalDao.selectTeamMember(searchMap);
	}
	
	// 기안자 검색
	@Override
	public List<HashMap<String, Object>> searchTeamMember(HttpServletRequest request) throws Exception {
		HashMap<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("insa_code", SessionUtil.getSession("memberSession", "INSA_CODE"));
		searchMap.put("user_no", SessionUtil.getSession("memberSession", "USER_NO"));
		searchMap.put("searchTeamName", request.getParameter("searchTeamName"));
		searchMap.put("searchName", request.getParameter("searchName"));
		
		logger.info("searchMap >> " + searchMap);
		
		return approvalDao.searchTeamMember(searchMap);
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
	public List<HashMap<String, Object>> selectProcessPath(HashMap<String, Object> params) throws Exception {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		params.put("user_no", user_no);
		params.put("user_grade", user_grade);
		
		List<HashMap<String, Object>> resultList = approvalDao.selectProcessPath(params);
		for(int i=0 ; i <resultList.size() ; i++) {
			HashMap<String, Object> resultMap = resultList.get(i);
			
			resultMap.put("NOTEPAD", set_service.replaceParameter((String) resultMap.get("NOTEPAD")));
			resultMap.put("BASIS", set_service.replaceParameter((String) resultMap.get("BASIS")));
			
			resultList.set(i, resultMap);
		}

		return resultList;
	}

	@Override
	public HashMap<String, Object>  registProcessCharge(HttpServletRequest request, HashMap<String, Object> params) throws Exception{		
		ReconUtil reconUtil = new ReconUtil();
		Map<String, Object> httpsResponse = null;
		JsonObject jsonObject = null;
		
		List<HashMap<String, Object>> resultList = new ArrayList<>();
		HashMap<String, Object> mapData =  new HashMap<String, Object>();
		List<patternVo> pattern_list = new ArrayList();
		
		String match_data ="";
		String match_detail = "";
		String mask_data = "";
		String resultData = "";
		Gson gson = new Gson();
		
//		결재에 들어갈 변수
		String detectionData =""; 
		String detailData = "";
		String detailDataPatternName = "";
		String agent_nm =""; 
		String agent_ip =""; 
		String service_nm =""; 
		String path ="";
		
		int matchLimit = 10;
		boolean mail_send = true;
		
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
					dataMap.put("P_ID", matchData.get("P_ID"));
					
					dataMap.put("METAS", matchData.get("METAS"));
					dataMap.put("CHUNKS", matchData.get("CHUNKS"));
					dataMap.put("MATCHS", matchData.get("MATCHS"));
					
					apiDataList.add(dataMap);
				}else {
					++max_cnt;
				}
			}
			
			// 결재 제목 및 내용
			Map<String, Object> contentList = setDao.selectContentList("approval");
			
			String approvalTitle = "";
			String approvalCon = "";
			
			// 결재 내용 정보가 없을 시 메일 발송하지 않음 
			if(contentList != null && contentList.size() > 0) {
				approvalTitle = contentList.get("NAME").toString();
				approvalCon = contentList.get("CON").toString();
			}else {
				mail_send = false;
			}
			
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
				String p_id = aList.get("P_ID").toString();
				boolean subpath = Integer.parseInt(aList.get("subpath").toString()) == 1 ? true : false;
				
				if(mail_send) {
				
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
					if(subpath) {
						detailData ="<a href=\"http://"+request.getLocalAddr()+":"+request.getLocalPort()+"/popup/lowPath?hash_id="+p_id+"&ap_no="+ap_no+"&tid="+target_id+"\" onclick=\"window.open(this.href, '_blank', 'width=1142, height=365'); return false; \">상세내용 바로가기</a>";
					}else {
						detailData ="<a href=\"http://"+request.getLocalAddr()+"/popup/approvalDetail?id="+p_id+"&ap_no="+ap_no+"&tid="+target_id+"\" onclick=\"window.open(this.href, '_blank', 'width=1142, height=365'); return false; \">상세내용 바로가기</a>";
					}
					
					detailDataPatternName ="";
					detectionData ="";
					 
					// 커스텀 데이터
					pattern_list = approvalDao.customPatternList();
					
					try {
					    if (chunksBytes != null) {
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
			}
			
			
			long currentTimeMills = Instant.now().toEpochMilli();
			String IFIDVALUE = "PIC"+currentTimeMills; // 유닉스타임 밀리세컨드
			
			params.put("index", (index+1));
			params.put("IFIDVALUE", IFIDVALUE);
			approvalDao.insertProcessingSeq(params); // 기안자 정보 저장
			
//			// 결재 정보 생성
			approvalDao.registProcessCharge(params);
//			
			String comment = (String) params.get("comment");
			JsonArray resultArray = gson.fromJson(approvalList, JsonArray.class);

			// 기안자 정보
			String approvalUser = "12|00|A|2023-11-02 17:00:52|" + user_no + "|" + user_name + "| |\n";
			// PICenter 생성 문서명
			String approvalNo = params.get("today") + "_" + params.get("user_no") + "_" + params.get("doc_seq");
			// 결재 라인 사용자 정보
			String approvalData = "";
			int CHARGE_ID = approvalDao.selectChargeId(params);
			List<String> receivermail = new ArrayList<>();

			if (resultArray.size() != 0) {
			    for (int i = 0; i < resultArray.size(); i++) {
			        JsonObject resultMap = resultArray.get(i).getAsJsonObject();
			        receivermail.add(resultMap.get("USER_EMAIL").getAsString());

			        HashMap<String, Object> userListMap = new HashMap<>();
			        
			        Object ok_user_no = resultMap.get("USER_NO");
					String ok_user_noStr = (ok_user_no instanceof JsonElement)
							? ((JsonElement) ok_user_no).getAsString()
									: String.valueOf(ok_user_no);
					userListMap.put("ok_user_no", ok_user_noStr);
					
					Object status = resultMap.get("STATUS");
					String statusStr = (status instanceof JsonElement)
					        ? ((JsonElement) status).getAsString()
					        : String.valueOf(status);
					userListMap.put("CHARGE_USER_FLAGE", statusStr);
			        
			        userListMap.put("CHARGE_ID", CHARGE_ID);
			        userListMap.put("CHARGE_USER_CNT", i + 1);
			        userListMap.put("user_no", user_no);
			        userListMap.put("IFIDVALUE", IFIDVALUE);
			        userListMap.put("approval_type", "1");

			        logger.info("userListMap :: " + userListMap);
			        approvalDao.insertProcessUserList(userListMap);

			        String num = (i + 1 > 8) ? "0" + (i + 1) : String.valueOf(i + 1);
			    }
			}
			if(mail_send) {
				Map<String, Object> mailMap = new HashMap<>();
				
				try {
					mailMap = approvalDao.selectMailUser();
				} catch (Exception e) {
					logger.info("error ::: " + e.getMessage());
				}
				
				Properties prop = new Properties();
				prop.put("mail.smtp.host", send_host); 
				prop.put("mail.smtp.port", send_port); 
				prop.put("mail.smtp.auth", mailAuth); 
				prop.put("mail.smtp.ssl.enable", mailEnable); 
				String title = "";
				String content = "";
				String sendmail = mailMap.get("COM").toString();
				String passwd = set_service.encrypt(1, mailMap.get("PWD").toString(), key);
//				logger.info("resultList :: " + resultList);
				
				title = "[PICenter] " + approvalTitle;
				
				content += "";
				content += "<body style=\"font-family: 'Noto Sans KR', sans-serif; padding: 0; margin: 0;\">";
				content += "<table style=\"border: 1px solid #ccc;\" border=\"0\" width=\"1400\" cellspacing=\"0\" align=\"center\">";
				content += "<tbody>";
				content += "<tr> <td align=\"center\">";
				content += "<table style=\"border-bottom: 2px solid #000;\" border=\"0\" width=\"1500\" cellspacing=\"0\" cellpadding=\"0\">";
				content += "<tbody><tr><td>";
				content += "<img src=\"cid:wori_logo\" style=\"width: 100%;\" />";
				content += "</td></tr></tbody></table>";
				content += "<table border=\"0\" width=\"1300\" cellspacing=\"0\" cellpadding=\"0\">";
				content += "<tbody><tr>";
				content += "<td style=\"height: 90px;\" align=\"center\" valign=\"bottom\">";
				content += "<img src=\"cid:mail_logo\" style=\"vertical-align: bottom;\" width=\"59\" height=\"60\" />";
				content += "</td></tr><tr>";
				content += "<td style=\"font-size: 13px; color: #999; font-weight: bold; height: 22px; letter-spacing: -0.7px; line-height: 13px;\"align=\"center\" valign=\"bottom\">";
				content += "개인정보 검출관리센터(PICenter)에서 발송되는 안내 메일입니다.</td>";
				content += "</tr><tr><td style=\"height: 28px;\">&nbsp;</td></tr></tbody></table>";
				content += "<table style=\"border: 1px solid #cccccc; padding-top: 30px; height: 239px;\" border=\"0\" width=\"1300\" cellspacing=\"0\" cellpadding=\"0\">";
				content += "<tbody><tr style=\"height: 84px;\"><td style=\"padding-left: 30px; height: 84px; width: 0px;\">&nbsp;</td>";
				content += "<td style=\"font-size: 13px; font-weight: bold; height: 84px; letter-spacing: -0.5px;\">";
				content += "<table style=\"height: 16px; padding-top: 13px;\" border=\"0\" width=\"1250\" cellspacing=\"0\" cellpadding=\"0\">";
				content += "<tbody><tr><td align=\"left\" width=\"30\"style=\"font-size: 13px; color: #999; font-weight: 700; line-height: 15px;\">수신자</td>";
				content += "<td style=\"font-size: 13px; color: #222222; font-weight: bold; height: 16px; width: 586px;\"align=\"left\">"+user_name+"</td>";
				content += "</tr></tbody></table>";
				content += "<table style=\"padding: 10px 0 20px 0;\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
				content += "<tbody><tr>";
				content += "<td style=\"font-size: 13px; color: #999; font-weight: bold; line-height: 15px;\" align=\"left\" width=\"60\">소속부서</td>";
				content += "<td style=\"font-size: 13px; color: #222; font-weight: bold;\" width=\"575\">프렌트리</td>";
				content += "</tr></tbody></table>";
				content += "<pre style=\"font-family: 'Noto Sans KR', sans-serif;\">";
				content += approvalCon;
				content += "</pre></td></tr><tr>";
				content += "<tr style=\"height: 35px;\"><td style=\"padding-left: 30px; height: 35px; width: 0px;\">&nbsp;</td></tr>";
				content += "<tr style=\"height: 42px;\"><td style=\"padding-left: 30px; height: 42px; width: 0px;\">&nbsp;</td>";
				content += "<td style=\"height: 42px; font-size: 13px; color: #999999; font-weight: bold; line-height: 13px; width: 590px;\">문서 번호</td></tr>";
				content += "<tr style=\"height: 22px;\"><td style=\"padding-left: 30px; height: 22px; width: 0px;\">&nbsp;</td>";
				content += "<td style=\"font-size: 13px;  font-weight: bold; line-height: 13px; width: 590px;\"> - "+approvalNo+"</td></tr>";
				content += "<tr style=\"height: 42px;\"><td style=\"padding-left: 30px; height: 42px; width: 0px;\">&nbsp;</td>";
				content += "<td style=\"height: 42px; font-size: 13px; color: #999999; font-weight: bold; line-height: 13px; width: 590px;\">기안 의견</td></tr>";
				content += "<tr style=\"height: 22px;\"><td style=\"padding-left: 30px; height: 22px; width: 0px;\">&nbsp;</td>";
				content += "<td style=\"font-size: 13px;  font-weight: bold; line-height: 13px; width: 590px;\"> - "+comment+"</td></tr>";
				content += "<tr style=\"height: 42px;\"><td style=\"padding-left: 30px; height: 42px; width: 0px;\">&nbsp;</td>";
				content += "<td style=\"height: 42px; font-size: 13px; color: #999999; font-weight: bold; line-height: 13px; width: 590px;\">검출 결과</td></tr>";
//				content += "<tr style=\"height: 22px;\">";
				content += "<tr style=\"height: 42px;\"><td style=\"padding-left: 30px; height: 42px; width: 0px;\">&nbsp;</td>";
				content += "<td style=\"height: 42px; font-size: 13px; font-weight: bold; line-height: 13px; width: 590px;\">상세내용의 경우 PICenter 로그인 후 조회 가능합니다.</td></tr>";
				content += "<tr style=\"height: 22px;\">";
				content += "<td style=\"padding-left: 30px; height: 22px; width: 0px;\">&nbsp;</td>";
				content += "<td style=\"height: 22px;\">";
				content += "<table style='width: 1248px; border: 1px solid #DFDFE6; border-collapse: collapse; font-size: 14px; font-family: Noto SansKR; table-layout: fixed;'>";
				content += "<tbody> ";
				content += "<colgroup><col width=\"10%\"><col width=\"10%\"><col width=\"10%\"><col width=\"20%\"><col width=\"25%\"><col width=\"25%\"></colgroup>";
				content += "<tr>";
				content += "<td style=\"font-weight: bold; border: 1px solid #cccccc;\">호스트명</td>";
				content += "<td style=\"font-weight: bold; border: 1px solid #cccccc;\">호스트IP</td>";
				content += "<td style=\"font-weight: bold; border: 1px solid #cccccc;\">업무명</td>";
				content += "<td style=\"font-weight: bold; border: 1px solid #cccccc;\">경로</td>";
				content += "<td style=\"font-weight: bold; border: 1px solid #cccccc;\">검출 결과</td>";
				content += "<td style=\"font-weight: bold; border: 1px solid #cccccc;\">검출 결과 상세</td>";
				for(int i = 0; i < resultList.size(); i++) {
					content += "</tr><tr><td style=\"border: 1px solid #cccccc;\">"+agent_nm+"</td>";
					content += "<td style=\"border: 1px solid #cccccc;\">"+agent_ip+"</td>";
					content += "<td style=\"border: 1px solid #cccccc;\">"+service_nm+"</td>";
	//				content += "<td style=\"border: 1px solid #cccccc;\">"+resultList.get(i).get("path")+"</td>";
					content += "<td style=\"border: 1px solid #cccccc;\"><div style=\"overflow-y: auto; height: 200px;\">"+resultList.get(i).get("path")+"</div></td>";
					content += "<td style=\"border: 1px solid #cccccc;\"><div style=\"overflow-y: auto; height: 200px; \">"+resultList.get(i).get("match_detail")+"</div></td>";
					content += "<td style=\"border: 1px solid #cccccc;\"><div style=\"overflow-y: auto; height: 200px; \">"+resultList.get(i).get("detectionData")+"</div></td>";
					content += "<td style=\"border: 1px solid #cccccc;\"></td>";
					content += "<td style=\"border: 1px solid #cccccc;\"></td>";
				}
				content += "</tr></tbody></table>";
				if(max_cnt > 0) {
					content += "<b>&nbsp; +"+max_cnt+" </b><br><br>";
					content += "<b>자세한 내용은 개인정보검출관리센터(PICenter)에서 확인 가능합니다.</b><br><br>";
				}
				content += "</td><td style=\"padding-left: 30px; height: 22px; width: 0px;\">&nbsp;</td></tr><tr style=\"height: 12px;\">";
				content += "<td style=\"padding-left: 30px; padding-top: 16px; height: 12px; width: 0px;\">&nbsp;</td>";
				content += "<td style=\"height: 12px; font-size: 13px; color: #999999; font-weight: bold; line-height: 13px; width: 590px;\">상세정보 확인</td></tr>";
				content += "<tr style=\"height: 40px;\"> <td style=\"padding-left: 30px; height: 40px; width: 0px;\">&nbsp;</td> <td style=\"height: 20px; width: 590px;\">";
				content += "<p style=\"margin: 5px 0 0 0;\"> <span style=\"background-color: #e6e6e6; font-size: 13px; font-weight: bold;\">";
				content += "개인정보검출관리센터(PICenter)바로가기(<a href=\"http://192.168.0.16:8080\" target=\"_blank\">http://192.168.0.16:8080</a>)";
				content += "</span></p></td></tr>";
				content += "<tr style=\"height: 30px;\"><td style=\"height: 30px; width: 590px;\">&nbsp;</td></tr>";
				content += "<tr style=\"height: 30px;\"><td style=\"padding-left: 30px; height: 30px; width: 0px;\">&nbsp;</td><td style=\"height: 30px; width: 590px;\">&nbsp;</td></tr></tbody>";
				content += "</table>";
				content += "<table border=\"0\" width=\"1400\" cellspacing=\"0\" cellpadding=\"0\"><tbody><tr>";
				content += "<td style=\"height: 80px;\">&nbsp;</td></tr></tbody></table>";
				content += "<table style=\"border-top: 1px solid #ccc; width:100%;\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
				content += "<tbody><tr><td bgcolor=\"#e6e6e6\" height=\"86\"><table>";
				content += "<tbody><tr><td style=\"width: 32%;\" ></td>";
				content += "<td style=\"letter-spacing: -0.7px; font-size: 13px; color: #000000; font-weight: bold;\"align=\"center\" height=\"42\" width=\"701\">";
				content += "Personal Information Center</td></tr></tbody></table></td></tr></tbody></table>";
				content += "<table border=\"0\" width=\"1400\" cellspacing=\"0\" cellpadding=\"0\"><tbody><tr></tr></tbody></table></tr></tbody></table></body>";
	
				Session session = Session.getDefaultInstance(prop, new Authenticator() {
				    protected PasswordAuthentication getPasswordAuthentication() {
				        return new PasswordAuthentication(sendmail, passwd);
				    }
				});

				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress(sendmail));

				// 수신자 메일 주소
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(receivermail.get(0)));
//				for (int m = 0; m < receivermail.size(); m++) {
//				    message.addRecipient(Message.RecipientType.TO, new InternetAddress(receivermail.get(m)));
//				}
	  
	        // Subject 
	        message.setSubject(title); //메일 제목을 입력
	        
	        MimeBodyPart messageBodyPart = new MimeBodyPart();
	        messageBodyPart.setContent(content, "text/html; charset=UTF-8");
	        
	     // 첫 번째 이미지 첨부 설정
	        MimeBodyPart imagePart1 = new MimeBodyPart();
	        InputStream imageStream1 = new URL("http://localhost:"+request.getLocalPort()+"/resources/assets/images/mail_title.png").openStream();
	        DataSource fds1 = new ByteArrayDataSource(imageStream1, "image/png");
	        imagePart1.setDataHandler(new DataHandler(fds1));
	        imagePart1.setHeader("Content-ID", "<wori_logo>");
	        imagePart1.setDisposition(MimeBodyPart.INLINE);

	        // 두 번째 이미지 첨부 설정
	        MimeBodyPart imagePart2 = new MimeBodyPart();
	        InputStream imageStream2 = new URL("http://localhost:"+request.getLocalPort()+"/resources/assets/images/mail_icon.png").openStream();
	        DataSource fds2 = new ByteArrayDataSource(imageStream2, "image/png");
	        imagePart2.setDataHandler(new DataHandler(fds2));
	        imagePart2.setHeader("Content-ID", "<mail_logo>");
	        imagePart2.setDisposition(MimeBodyPart.INLINE);
	
	        // 메일 본문과 이미지 파트를 결합
	        MimeMultipart multipart = new MimeMultipart("related");
	        multipart.addBodyPart(messageBodyPart);
	        multipart.addBodyPart(imagePart1);
	        multipart.addBodyPart(imagePart2);
	        
	        message.setContent(multipart);
	         
	        // send the message
	        Transport.send(message); ////전송
		}
			
		}catch (Exception e) {
			e.printStackTrace();
			params.put("resultCode", -1);
			
			return params;
		}
		
		params.put("resultCode", 0);
		return params;
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

	@Override
	public HashMap<String, Object>  registProcessCharge2(HttpServletRequest request, HashMap<String, Object> params) throws Exception
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
		
		boolean mail_send = true;
		
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
					
					String groupID = mapCount.get(i).get("DATA_PROCESSING_GROUP_IDX").toString();
					if(!pathGroupId.equals(groupID)) { // 같지 않을때 이전데이터 저장
						
						List<HashMap<String, Object>> hostList = approvalDao.selectApprovalHost(pathGroupId);
						dataMap.put("reson_status", reson_status);
						dataMap.put("exPath",exPath+"<br>");
						dataMap.put("reson", reson);
						dataMap.put("host_size", host_size);
						dataMap.put("hostList", hostList);
						apiDataList.add(dataMap);
						
						pathGroupId = mapCount.get(i).get("DATA_PROCESSING_GROUP_IDX").toString();
						groupChargId.add(pathGroupId);
						exPath = mapCount.get(i).get("HASH_ID").toString()+"<br>";  
						host_size = 1;
						
					}else if((i+1) == mapCount.size() || i ==51){
						
						List<HashMap<String, Object>> hostList = approvalDao.selectApprovalHost(pathGroupId);
						exPath+= mapCount.get(i).get("HASH_ID").toString(); // 예외 경로
						
						dataMap.put("reson_status", reson_status);
						dataMap.put("exPath", exPath+"<br>");
						dataMap.put("reson", reson);
						dataMap.put("host_size", host_size);
						dataMap.put("hostList", hostList);
						apiDataList.add(dataMap);
						
					}else { // 이전과 같은 구분
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
			

			String approvalTitle = "";
			String approvalCon = "";
			
			// 결재 내용 정보가 없을 시 메일 발송하지 않음 
			if(contentList != null && contentList.size() > 0) {
				approvalTitle = contentList.get("NAME").toString();
				approvalCon = contentList.get("CON").toString();
			}else {
				mail_send = false;
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
			
			List<String> receivermail = new ArrayList<>();
			
			if (resultArray.size() != 0) {
//					결재 사용자 정보
				
				for (int i = 0; i < resultArray.size(); i++) {
					JsonObject resultMap = resultArray.get(i).getAsJsonObject();
					receivermail.add(resultMap.get("USER_EMAIL").getAsString());
					
					HashMap<String, Object> userListMap =  new HashMap<String, Object>();
					
					Object status = resultMap.get("STATUS");
					String statusStr = (status instanceof JsonElement)
					        ? ((JsonElement) status).getAsString()
					        : String.valueOf(status);
					userListMap.put("CHARGE_USER_FLAGE", statusStr);
					
					Object ok_user_no = resultMap.get("USER_NO");
					String ok_user_noStr = (ok_user_no instanceof JsonElement)
							? ((JsonElement) ok_user_no).getAsString()
									: String.valueOf(ok_user_no);
					userListMap.put("ok_user_no", ok_user_noStr);
					
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
					
					approvalData +=  resultMap.get("STATUS2")+ " | " + resultMap.get("USER_NAME") + "(" + resultMap.get("USER_NO") + ") +'<br>";
					
				}
			}

			for(int g = 0 ; g < groupChargId.size() ; g++) {
				HashMap<String, Object> groupMap =  new HashMap<String, Object>();
				groupMap.put("CHARGE_ID", CHARGE_ID);
				groupMap.put("groupChargId", groupChargId.get(g));
				groupMap.put("comment", comment);
				
				approvalDao.insertGroupChageId(groupMap);
			}
			
			
			if(mail_send) {
				Map<String, Object> mailMap = new HashMap<>();
				
				try {
					mailMap = approvalDao.selectMailUser();
				} catch (Exception e) {
					logger.info("error ::: " + e.getMessage());
				}
				
				Properties prop = new Properties();
				prop.put("mail.smtp.host", send_host); 
				prop.put("mail.smtp.port", send_port); 
				prop.put("mail.smtp.auth", mailAuth); 
				prop.put("mail.smtp.ssl.enable", mailEnable); 
				
				String title = "";
				String content = "";
				String sendmail = mailMap.get("COM").toString();
				String passwd = AESDecrypt(mailMap.get("PWD").toString());
				
//			logger.info("resultList :: " + resultList);
				
				title = "[PICenter] " + approvalTitle;
//			receivermail = "gkwldls@frentree.com";
				
				content += "";
				content += "<body style=\"font-family: 'Noto Sans KR', sans-serif; padding: 0; margin: 0;\">";
				content += "<table style=\"border: 1px solid #ccc;\" border=\"0\" width=\"1400\" cellspacing=\"0\" align=\"center\">";
				content += "<tbody>";
				content += "<tr> <td align=\"center\">";
				content += "<table style=\"border-bottom: 2px solid #000;\" border=\"0\" width=\"1500\" cellspacing=\"0\" cellpadding=\"0\">";
				content += "<tbody><tr><td>";
				content += "<img src=\"cid:wori_logo\" style=\"width: 100%;\" />";
				content += "</td></tr></tbody></table>";
				content += "<table border=\"0\" width=\"1300\" cellspacing=\"0\" cellpadding=\"0\">";
				content += "<tbody><tr>";
				content += "<td style=\"height: 90px;\" align=\"center\" valign=\"bottom\">";
				content += "<img src=\"cid:mail_logo\" style=\"vertical-align: bottom;\" width=\"59\" height=\"60\" />";
				content += "</td></tr><tr>";
				content += "<td style=\"font-size: 13px; color: #999; font-weight: bold; height: 22px; letter-spacing: -0.7px; line-height: 13px;\"align=\"center\" valign=\"bottom\">";
				content += "개인정보 검출관리센터(PICenter)에서 발송되는 안내 메일입니다.</td>";
				content += "</tr><tr><td style=\"height: 28px;\">&nbsp;</td></tr></tbody></table>";
				content += "<table style=\"border: 1px solid #cccccc; padding-top: 30px; height: 239px;\" border=\"0\" width=\"1300\" cellspacing=\"0\" cellpadding=\"0\">";
				content += "<tbody><tr style=\"height: 84px;\"><td style=\"padding-left: 30px; height: 84px; width: 0px;\">&nbsp;</td>";
				content += "<td style=\"font-size: 13px; font-weight: bold; height: 84px; letter-spacing: -0.5px;\">";
				content += "<table style=\"height: 16px; padding-top: 13px;\" border=\"0\" width=\"1250\" cellspacing=\"0\" cellpadding=\"0\">";
				content += "<tbody><tr><td align=\"left\" width=\"30\"style=\"font-size: 13px; color: #999; font-weight: 700; line-height: 15px;\">수신자</td>";
				content += "<td style=\"font-size: 13px; color: #222222; font-weight: bold; height: 16px; width: 586px;\"align=\"left\">"+user_name+"</td>";
				content += "</tr></tbody></table>";
				content += "<table style=\"padding: 10px 0 20px 0;\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
				content += "<tbody><tr>";
				content += "<td style=\"font-size: 13px; color: #999; font-weight: bold; line-height: 15px;\" align=\"left\" width=\"60\">소속부서</td>";
				content += "<td style=\"font-size: 13px; color: #222; font-weight: bold;\" width=\"575\">프렌트리</td>";
				content += "</tr></tbody></table>";
				content += "<pre style=\"font-family: 'Noto Sans KR', sans-serif;\">";
				content += approvalCon;
				content += "</pre></td></tr><tr>";
				content += "<tr style=\"height: 35px;\"><td style=\"padding-left: 30px; height: 35px; width: 0px;\">&nbsp;</td></tr>";
				content += "<tr style=\"height: 42px;\"><td style=\"padding-left: 30px; height: 42px; width: 0px;\">&nbsp;</td>";
				content += "<td style=\"height: 42px; font-size: 13px; color: #999999; font-weight: bold; line-height: 13px; width: 590px;\">문서 번호</td></tr>";
				content += "<tr style=\"height: 22px;\"><td style=\"padding-left: 30px; height: 22px; width: 0px;\">&nbsp;</td>";
				content += "<td style=\"font-size: 13px;  font-weight: bold; line-height: 13px; width: 590px;\"> - "+approvalNo+"</td></tr>";
				content += "<tr style=\"height: 42px;\"><td style=\"padding-left: 30px; height: 42px; width: 0px;\">&nbsp;</td>";
				content += "<td style=\"height: 42px; font-size: 13px; color: #999999; font-weight: bold; line-height: 13px; width: 590px;\">기안 의견</td></tr>";
				content += "<tr style=\"height: 22px;\"><td style=\"padding-left: 30px; height: 22px; width: 0px;\">&nbsp;</td>";
				content += "<td style=\"font-size: 13px;  font-weight: bold; line-height: 13px; width: 590px;\"> - "+comment+"</td></tr>";
				content += "<tr style=\"height: 42px;\"><td style=\"padding-left: 30px; height: 42px; width: 0px;\">&nbsp;</td>";
				content += "<td style=\"height: 42px; font-size: 13px; color: #999999; font-weight: bold; line-height: 13px; width: 590px;\">검출 결과</td></tr>";
				content += "<tr style=\"height: 22px;\">";
				content += "<td style=\"padding-left: 30px; height: 22px; width: 0px;\">&nbsp;</td>";
				content += "<td style=\"height: 22px;\">";
				content += "<table style='width: 1248px; border: 1px solid #DFDFE6; border-collapse: collapse; font-size: 14px; font-family: Noto SansKR; table-layout: fixed;'>";
				content += "<tbody> ";
				content += "<colgroup><col width=\"15%\"><col width=\"10%\"><col width=\"10%\"><col width=\"25%\"><col width=\"15%\"><col width=\"35%\"></colgroup>";
				content += "<tr>";
				content += "<td style=\"font-weight: bold; border: 1px solid #cccccc;\">호스트명</td>";
				content += "<td style=\"font-weight: bold; border: 1px solid #cccccc;\">호스트IP</td>";
				content += "<td style=\"font-weight: bold; border: 1px solid #cccccc;\">업무명</td>";
				content += "<td style=\"font-weight: bold; border: 1px solid #cccccc;\">예외 경로</td>";
				content += "<td style=\"font-weight: bold; border: 1px solid #cccccc;\">구분</td>";
				content += "<td style=\"font-weight: bold; border: 1px solid #cccccc;\">상세 사유</td>";
				
				
				for(int i = 0; i < apiDataList.size(); i++) {
					int hostSize = Integer.parseInt(apiDataList.get(i).get("host_size").toString());
					List<Map<String, Object>> list = (List<Map<String, Object>>) apiDataList.get(i).get("hostList");
					for(int h = 0; h < list.size() ; h++) {
						content += "</tr><tr><td style=\"border: 1px solid #cccccc;\">"+list.get(h).get("NAME")+"</td>";
						content += "<td style=\"border: 1px solid #cccccc;\">"+list.get(h).get("AGENT_CONNECTED_IP")+"</td>";
						content += "<td style=\"border: 1px solid #cccccc;\">"+list.get(h).get("SERVICE_NM")+"</td>";
						if(h==0) {
							content += "<td style=\"border: 1px solid #cccccc;\" rowspan="+list.size()+">"+apiDataList.get(i).get("exPath")+"</td>";
							content += "<td style=\"border: 1px solid #cccccc;\" rowspan="+list.size()+">"+apiDataList.get(i).get("reson_status")+"</td>";
							content += "<td style=\"border: 1px solid #cccccc;\" rowspan="+list.size()+">"+apiDataList.get(i).get("reson")+"</td>";
						}else {
							content += "<td style=\"border: 1px solid #cccccc;\" ></td>";
							content += "<td style=\"border: 1px solid #cccccc;\" ></td>";
							content += "<td style=\"border: 1px solid #cccccc;\" ></td>";
						}
						
						
					}
				}
				content += "</tr></tbody></table>";
				if(max_cnt > 0) {
					content += "<b>&nbsp; +"+max_cnt+" </b><br><br>";
					content += "<b>자세한 내용은 개인정보검출관리센터(PICenter)에서 확인 가능합니다.</b><br><br>";
				}
				content += "</td><td style=\"padding-left: 30px; height: 22px; width: 0px;\">&nbsp;</td></tr><tr style=\"height: 12px;\">";
				content += "<td style=\"padding-left: 30px; padding-top: 16px; height: 12px; width: 0px;\">&nbsp;</td>";
				content += "<td style=\"height: 12px; font-size: 13px; color: #999999; font-weight: bold; line-height: 13px; width: 590px;\">상세정보 확인</td></tr>";
				content += "<tr style=\"height: 40px;\"> <td style=\"padding-left: 30px; height: 40px; width: 0px;\">&nbsp;</td> <td style=\"height: 20px; width: 590px;\">";
				content += "<p style=\"margin: 5px 0 0 0;\"> <span style=\"background-color: #e6e6e6; font-size: 13px; font-weight: bold;\">";
				content += "개인정보검출관리센터(PICenter)바로가기(<a href=\"http://192.168.0.16:8080\" target=\"_blank\">http://192.168.0.16:8080</a>)";
				content += "</span></p></td></tr>";
				content += "<tr style=\"height: 30px;\"><td style=\"height: 30px; width: 590px;\">&nbsp;</td></tr>";
				content += "<tr style=\"height: 30px;\"><td style=\"padding-left: 30px; height: 30px; width: 0px;\">&nbsp;</td><td style=\"height: 30px; width: 590px;\">&nbsp;</td></tr></tbody>";
				content += "</table>";
				content += "<table border=\"0\" width=\"1400\" cellspacing=\"0\" cellpadding=\"0\"><tbody><tr>";
				content += "<td style=\"height: 80px;\">&nbsp;</td></tr></tbody></table>";
				content += "<table style=\"border-top: 1px solid #ccc; width:100%;\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
				content += "<tbody><tr><td bgcolor=\"#e6e6e6\" height=\"86\"><table>";
				content += "<tbody><tr><td style=\"width: 32%;\" ></td>";
				content += "<td style=\"letter-spacing: -0.7px; font-size: 13px; color: #000000; font-weight: bold;\"align=\"center\" height=\"42\" width=\"701\">";
				content += "Personal Information Center</td></tr></tbody></table></td></tr></tbody></table>";
				content += "<table border=\"0\" width=\"1400\" cellspacing=\"0\" cellpadding=\"0\"><tbody><tr></tr></tbody></table></tr></tbody></table></body>";
				
				logger.info("test >>>  " + content);  
				
				Session session = Session.getDefaultInstance(prop, new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(sendmail, passwd);
					}
				});
				
				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress(sendmail));
				
				//수신자메일주소
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(receivermail.get(0)));
//        for(int m = 0; m < receivermail.size() ; m ++) {
//	        	message.addRecipient(Message.RecipientType.TO, new InternetAddress(receivermail.get(m))); 
//        }
				
				// Subject
				message.setSubject(title); //메일 제목을 입력
				
				MimeBodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setContent(content, "text/html; charset=UTF-8");
				
				// 첫 번째 이미지 첨부 설정
				MimeBodyPart imagePart1 = new MimeBodyPart();
				InputStream imageStream1 = new URL("http://localhost:"+request.getLocalPort()+"/resources/assets/images/mail_title.png").openStream();
				DataSource fds1 = new ByteArrayDataSource(imageStream1, "image/png");
				imagePart1.setDataHandler(new DataHandler(fds1));
				imagePart1.setHeader("Content-ID", "<wori_logo>");
				imagePart1.setDisposition(MimeBodyPart.INLINE);
				
				// 두 번째 이미지 첨부 설정
				MimeBodyPart imagePart2 = new MimeBodyPart();
				InputStream imageStream2 = new URL("http://localhost:"+request.getLocalPort()+"/resources/assets/images/mail_icon.png").openStream();
				DataSource fds2 = new ByteArrayDataSource(imageStream2, "image/png");
				imagePart2.setDataHandler(new DataHandler(fds2));
				imagePart2.setHeader("Content-ID", "<mail_logo>");
				imagePart2.setDisposition(MimeBodyPart.INLINE);
				
				// 메일 본문과 이미지 파트를 결합
				MimeMultipart multipart = new MimeMultipart("related");
				multipart.addBodyPart(messageBodyPart);
				multipart.addBodyPart(imagePart1);
				multipart.addBodyPart(imagePart2);
				
				message.setContent(multipart);
				
				// send the message
				Transport.send(message); ////전송
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
	public void updateProcessStatus(HashMap<String, Object> params, Map<String, Object> chargeMap) throws Exception
	{
		logger.info("updateProcessStatus 로그체크 1");

		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		List<String> idxList = (List<String>)params.get("idxList");

		/*Object charge_id = chargeMap.get("data_processing_charge_id");
		if(charge_id == null || "".equals(charge_id.toString())) {
			charge_id = approvalDao.selectDataProcessingChargeId(params);
		}*/
		
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
	
	@Override
	public void approvalPlus(HashMap<String, Object> params) throws Exception
	{
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		params.put("user_no", user_no);

		approvalDao.approvalPlus(params);
	}

	/**
	 * 정탐/오탐 결재 리스트
	 */
	@Override
	public List<HashMap<String, Object>> searchApprovalAllListData(HashMap<String, Object> params) throws Exception {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");

		params.put("user_no", user_no);

		return approvalDao.searchApprovalAllListData(params);
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
	 * 결재 리스트 - 조회
	 */
	@Override
	public List<HashMap<String, Object>> selectProcessGroupPath(HashMap<String, Object> params) throws Exception
	{
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String,Object>>();

		String charge_id = (String)params.get("CHARGE_ID_LIST");
		int approval_type = Integer.parseInt(params.get("approval_type").toString());
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
		
		logger.info("approval_type :: " +approval_type);
		
		if(approval_type == 2) { // 경로 예외
			resultList = approvalDao.selectProcessGroupPath2(params);
		}else {
			resultList = approvalDao.selectProcessGroupPath(params);
		}
		
		

		return resultList;
	}
	
	/**
	 * 경로예외 결재 리스트 - 조회
	 */
	@Override
	public List<HashMap<String, Object>> selectProcessGroupPath2(HashMap<String, Object> params) throws Exception
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

		return approvalDao.selectProcessGroupPath2(params);
	}

	/**
	 * 정탐/오탐 결재 리스트 - 결재
	 */
	@Override
	public void updateProcessApproval(HashMap<String, Object> params) throws Exception
	{
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String charge_id = (String)params.get("chargeIdList");

		List<String> chargeIdList = new ArrayList<String>();
		if(charge_id != null && !"".equals(charge_id)) {
			StringTokenizer st = new StringTokenizer(charge_id, ",");
			while(st.hasMoreTokens()) {
				chargeIdList.add(st.nextToken());
			}
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> group = null;
		for (int i = 0; i < chargeIdList.size(); i++) 
		{
			map.put("chargeIdList", chargeIdList.get(i));
			map.put("user_no",  user_no);
			map.put("apprType", params.get("apprType"));
			map.put("reason",   params.get("reason"));

			group = approvalDao.selectDataProcessingGroupId(map);

			approvalDao.updateProcessApproval(map);
			approvalDao.updateProceesApprovalUser(map);
			for (int j = 0; j < group.size(); j++) {
				map.put("group_id", group.get(j).get("GROUP_ID"));
				approvalDao.updateDataProcessing(map);
			}
		}
		
	}

	/**
	 * 오탐 2차 결재 리스트 - 결재
	 */
	@Override
	public void updateProcessApprovalAdminTwo(HashMap<String, Object> params) throws Exception {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String charge_id = (String)params.get("chargeIdList");
		String excepter = (String)params.get("excepterList");

		List<String> chargeIdList = new ArrayList<String>();
		if(charge_id != null && !"".equals(charge_id)) {
			StringTokenizer st = new StringTokenizer(charge_id, ",");
			while(st.hasMoreTokens()) {
				chargeIdList.add(st.nextToken());
			}
		}
		
		List<String> excepterList = new ArrayList<>();
		if(excepter != null && !"".equals(excepter)) {
			StringTokenizer st = new StringTokenizer(excepter, ",");
			while(st.hasMoreTokens()) {
				excepterList.add(st.nextToken());
			}
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> group = null;
		for (int i = 0; i < chargeIdList.size(); i++) 
		{
			map.put("chargeIdList", chargeIdList.get(i));
			map.put("apprType", params.get("apprType"));
			map.put("user_no",  user_no);
			map.put("reason",   params.get("reason"));

			group = approvalDao.selectDataProcessingGroupId(map);

			approvalDao.updateProcessApproval(map);
			for (int j = 0; j < group.size(); j++) {
				map.put("group_id", group.get(j).get("GROUP_ID"));
				approvalDao.updateDataProcessing(map);
			}
		}
	}

	/**
	 * 정탐/오탐 결재 리스트 - 재검색 스캔정보
	 */
	@Override
	public List<HashMap<String, Object>> selectScanPolicy() throws Exception
	{
		return approvalDao.selectScanPolicy();
	}

	/**
	 * 정탐/오탐 결재 리스트 - 재검색 선택 Target 정보
	 */
	@Override
	public List<HashMap<String, Object>> selectReScanTarget(HashMap<String, Object> params) throws Exception
	{
		List<String> group_list = (List<String>)params.get("groupList");
		params.put("group_list", group_list);

		return approvalDao.selectReScanTarget(params);
	}

	@Override
	public void deleteItem(HashMap<String, Object> params) throws Exception {
		
		List<String> idxList = (List<String>)params.get("idxList");

		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < idxList.size(); i++) 
		{
			map.put("idx", idxList.get(i));
			approvalDao.deleteItem(map);
		}
		
	}

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
	
	public static String processData(String input) {
        StringBuilder result = new StringBuilder();
        boolean insideNonAsciiSequence = false;

        for (char ch : input.toCharArray()) {
            if (ch > 127) { // 비 ASCII 문자를 만났을 때
                if (!insideNonAsciiSequence) { // 처음 비 ASCII 문자 그룹을 만났을 때만 '=' 추가
                    result.append("=");
                    insideNonAsciiSequence = true;
                }else {
                	result.append(ch);
                }
            } else { // ASCII 문자를 만났을 때
                result.append(ch);
                insideNonAsciiSequence = false; // ASCII 문자가 나오면 비 ASCII 상태 플래그 초기화
            }
        }

        return result.toString();
    }
	
	
}