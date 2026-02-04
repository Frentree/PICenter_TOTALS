package com.org.iopts.setting.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ProtocolException;
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

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

// import org.apache.http.ParseException;
import org.apache.ibatis.io.Resources;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
import com.org.iopts.detection.vo.GlobalFilterVo;
import com.org.iopts.exception.dao.piDetectionListDAO;
import com.org.iopts.setting.dao.Pi_SetDAO;
import com.org.iopts.setting.service.Pi_SetService;
import com.org.iopts.util.ReconUtil;
import com.org.iopts.util.ServletUtil;
import com.org.iopts.util.SessionUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class Pi_SetServiceImpl implements Pi_SetService {

	@Value("${recon.id}")
	private String recon_id;

	@Value("${recon.password}")
	private String recon_password;

	@Value("${recon.url}")
	private String recon_url;
	
	@Value("${recon.api.version}")
	private String api_ver;
	
	@Value("${pic_version}")
	private String pic_version;
	
	@Value("${user.key}")
	private String key;
	
	@Value("${send.host}")
	private String send_host;
	
	@Value("${send.port}")
	private String send_port;
	
	
	@Inject
	private Pi_SetDAO dao;
	
	@Inject
	private Pi_UserDAO userDAO;
	
	@Inject
	private piDetectionListDAO detectionListDAO;
	
	private static Logger logger = LoggerFactory.getLogger(Pi_SetServiceImpl.class);

	private static final String ALGORITHM = "AES";
	
	@Override
	public List<Map<Object, Object>> selectSetting(int type) throws Exception {
		List<Map<Object, Object>> resultList = dao.selectSetting(type);
		List<Map<Object, Object>> setting = new ArrayList<>();
		
		for (Map<Object, Object> map : resultList) {
			Map<Object, Object> resultMap = new HashMap<>();
			resultMap.put("NAME", map.get("NAME"));
			resultMap.put("STATUS", map.get("STATUS"));
			resultMap.put("SUB_NAME", map.get("DETAIL_NAME"));
			resultMap.put("num", map.get("SEQ"));
			resultMap.put("IDX", map.get("IDX"));
			setting.add(resultMap);
		}
		return setting;
	}
	
	@Override
	public List<Map<Object, Object>> selectApprovalStatus() throws Exception {
		List<Map<Object, Object>> resultList = dao.selectApprovalStatus();
		return resultList;
	}
	
	@Override
	public List<Map<Object, Object>> selectApprovalStatusAll() throws Exception {
		List<Map<Object, Object>> resultList = dao.selectApprovalStatusAll();
		return resultList;
	}

	@Override
	public List<Map<String, Object>> selectbatchData(String status) throws Exception {
		return dao.selectbatchData(status);
	}
	
	@Override
	public List<Map<String, Object>> patternList(HttpServletRequest request) {
		return dao.patternList();
	}
	
	@Override
	public Map<String, Object> approvalAlert(HttpServletRequest request) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			int alert_type = Integer.parseInt(request.getParameter("approval_type").toString());
			logger.info("postData ::" + alert_type);
			
			String alert_con = "approval_alert" + alert_type;
			
			resultMap = dao.selectContentList(alert_con);
			
			if(resultMap == null) {
				resultMap = new HashMap<String, Object>();
				resultMap.put("resultCode", -9);
			}else {
				resultMap.put("resultCode", 0);
			}
			
		} catch (Exception e) {  
			e.printStackTrace();
			logger.error("e :: " + e.getLocalizedMessage());
		}
		
		
		
		return resultMap;
	}
	
	@Override
	public List<Map<String, Object>> selectGroupApprovalUser(HttpServletRequest request) {
		
		List<Map<String, Object>> resultList = new ArrayList<>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String insa_code = request.getParameter("insa_code").toString();
			logger.info("postData ::" + insa_code);
			
			resultList = dao.selectGroupApprovalUser(insa_code);
			
		} catch (Exception e) {  
			e.printStackTrace();
			logger.error("e :: " + e.getLocalizedMessage());
		}
		
		return resultList;
	}
	
	//	개인정보 유형 수정	
	@Override
	public Map<String, Object> updateCustomPattern(HttpServletRequest request) throws Exception {
		
		Map<String, Object> requestMap = new HashMap<>();
		Map<String, Object> resultMap = new HashMap<>();
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		if(!user_grade.equals("9")) {
			resultMap.put("resultCode", -9);
			resultMap.put("resultMessage", "올바르지 않은 접근");
			
			return resultMap;
		}
		
		requestMap.put("patternReconNm", request.getParameter("patternReconNm"));
		requestMap.put("patternNameKr", request.getParameter("patternNameKr"));
		requestMap.put("patternNameEr", request.getParameter("patternNameEr"));
		requestMap.put("patternMaskType", request.getParameter("patternMaskType"));
		requestMap.put("patternMaskChk", request.getParameter("patternMaskChk"));
		requestMap.put("patternMaskCnt", request.getParameter("patternMaskCnt"));
		requestMap.put("patternColor", request.getParameter("patternColor"));
		requestMap.put("patternRuleTd", request.getParameter("patternRuleTd"));
		requestMap.put("patternIDX", request.getParameter("patternIDX"));
		
		logger.info("requestMap >>>>  " + requestMap);
		
		try {
			int resultInt = dao.updateCustomPattern(requestMap);
			
			if(resultInt > 0) {
				resultMap.put("resultCode", 0);
				resultMap.put("resultMessage", "SUCCESS");
			}else {
				resultMap.put("resultCode", -2);
				resultMap.put("resultMessage", "NOT DATA");
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "ERROR");
			
			return resultMap;
		}
		return resultMap;
	}
	
	//개인정보유형 삭제	
	@Override
	public Map<String, Object> deleteCustomPattern(HttpServletRequest request) throws Exception {
		
		Map<String, Object> requestMap = new HashMap<>();
		Map<String, Object> resultMap = new HashMap<>();
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		if(!user_grade.equals("9")) {
			resultMap.put("resultCode", -9);
			resultMap.put("resultMessage", "올바르지 않은 접근");
			
			return resultMap;
		}
		requestMap.put("patternIDX", request.getParameter("patternIDX"));
		
		try {
			int resultInt = dao.deleteCustomPattern(requestMap);
			
			if(resultInt > 0) {
				resultMap.put("resultCode", 0);
				resultMap.put("resultMessage", "SUCCESS");
			}else {
				resultMap.put("resultCode", -2);
				resultMap.put("resultMessage", "NOT DATA");
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "ERROR");
			
			return resultMap;
		}
		return resultMap;
	}
	
	//	개인정보 유형 생성
	@Override
	public Map<String, Object> insertCustomPattern(HttpServletRequest request) throws Exception {
		
		Map<String, Object> requestMap = new HashMap<>();
		Map<String, Object> resultMap = new HashMap<>();
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		if(!user_grade.equals("9")) {
			resultMap.put("resultCode", -9);
			resultMap.put("resultMessage", "올바르지 않은 접근");
			
			return resultMap;
		}
		
		requestMap.put("patternReconNm", request.getParameter("patternReconNm"));
		requestMap.put("patternNameKr", request.getParameter("patternNameKr"));
		requestMap.put("patternNameEr", request.getParameter("patternNameEr"));
		requestMap.put("patternMaskType", request.getParameter("patternMaskType"));
		requestMap.put("patternMaskChk", request.getParameter("patternMaskChk"));
		requestMap.put("patternMaskCnt", request.getParameter("patternMaskCnt"));
		requestMap.put("patternColor", request.getParameter("patternColor"));
		requestMap.put("patternRuleTd", request.getParameter("patternRuleTd"));
		
		logger.info("requestMap >>>>  " + requestMap);
		
		try {
			int resultInt = dao.insertCustomPattern(requestMap);
			
			if(resultInt > 0) {
				resultMap.put("resultCode", 0);
				resultMap.put("resultMessage", "SUCCESS");
			}else {
				resultMap.put("resultCode", -1);
				resultMap.put("resultMessage", "ERROR");
				
				return resultMap;
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "ERROR");
			
			return resultMap;
		}
		return resultMap;
	}
	
	
	
	//	개인정보 유형 생성
	@Override
	public Map<String, Object> customPatternChagne(HttpServletRequest request) throws Exception {
		Map<String, Object> requestMap = new HashMap<>();
		Map<String, Object> resultMap = new HashMap<>();
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		if(!user_grade.equals("9")) {
			resultMap.put("resultCode", -9);
			resultMap.put("resultMessage", "올바르지 않은 접근");
			
			return resultMap;
		}
		JsonParser parser = new JsonParser();
		JsonArray dataArr = (JsonArray) parser.parse(request.getParameter("idxList"));
		
		try {

			for(int i=0; i<dataArr.size(); i++) {
				JsonObject obj = (JsonObject) dataArr.get(i);
				
				Map<String, Object> patternMap = new HashMap<>();
				patternMap.put("idx", obj.get("idx").toString());
				patternMap.put("cnt", obj.get("cnt").toString());
				
				dao.customPatternChagne(patternMap);
				
				
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			resultMap.put("resultCode", -3);
			resultMap.put("resultMessage", "UPDATE ERROR");
			
			return resultMap;
		}
		
		resultMap.put("resultCode", 0);
		resultMap.put("resultMessage", "SUCCESS");
		return resultMap;
	}
	
	@Override
	public List<Map<String, Object>> nameList(String status) {
		return dao.nameList(status);
	}
	
	@Override
	public Map<String, Object> nameListUpdate(HttpServletRequest request) {
		Map<String, Object> requestMap = new HashMap<>();
		Map<String, Object> resultMap = new HashMap<>();
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		if(!user_grade.equals("9")) {
			resultMap.put("resultCode", -9);
			resultMap.put("resultMessage", "올바르지 않은 접근");
			
			return resultMap;
		}
		
		requestMap.put("idx", request.getParameter("idx"));
		requestMap.put("name", request.getParameter("name"));
		requestMap.put("status", request.getParameter("status"));
		try {
			int resultInt = dao.nameListUpdate(requestMap);
			
			if(resultInt > 0) {
				resultMap.put("resultCode", 0);
				resultMap.put("resultMessage", "SUCCESS");
			}else {
				resultMap.put("resultCode", -2);
				resultMap.put("resultMessage", "NOT DATA");
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "ERROR");
			
			return resultMap;
		}
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> nameListDelete(HttpServletRequest request) throws Exception {
		
		Map<String, Object> requestMap = new HashMap<>();
		Map<String, Object> resultMap = new HashMap<>();
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		if(!user_grade.equals("9")) {
			resultMap.put("resultCode", -9);
			resultMap.put("resultMessage", "올바르지 않은 접근");
			
			return resultMap;
		}
		
		requestMap.put("idx", request.getParameter("idx"));
		requestMap.put("status", request.getParameter("status"));
		
		try {
			int resultInt = dao.nameListDelete(requestMap);
			
			if(resultInt > 0) {
				resultMap.put("resultCode", 0);
				resultMap.put("resultMessage", "SUCCESS");
			}else {
				resultMap.put("resultCode", -2);
				resultMap.put("resultMessage", "NOT DATA");
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "ERROR");
			
			return resultMap;
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> nameListCreate(HttpServletRequest request) {
		Map<String, Object> requestMap = new HashMap<>();
		Map<String, Object> resultMap = new HashMap<>();
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		if(!user_grade.equals("9")) {
			resultMap.put("resultCode", -9);
			resultMap.put("resultMessage", "올바르지 않은 접근");
			
			return resultMap;
		}
		
		requestMap.put("name", request.getParameter("name"));
		requestMap.put("status", request.getParameter("status"));
		
		try {
			int resultInt = dao.nameListCreate(requestMap);
			
			if(resultInt > 0) {
				resultMap.put("resultCode", 0);
				resultMap.put("resultMessage", "SUCCESS");
			}else {
				resultMap.put("resultCode", -1);
				resultMap.put("resultMessage", "ERROR");
				
				return resultMap;
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "ERROR");
			
			return resultMap;
		}
		
		return resultMap;
	}
	
	//개인정보유형 삭제	
	@Override
	public Map<String, Object> ConListUpdate(HttpServletRequest request) throws Exception {
		
		Map<String, Object> requestMap = new HashMap<>();
		Map<String, Object> resultMap = new HashMap<>();
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		if(!user_grade.equals("9")) {
			resultMap.put("resultCode", -9);
			resultMap.put("resultMessage", "올바르지 않은 접근");
			
			return resultMap;
		}
		requestMap.put("idx", request.getParameter("idx"));
		requestMap.put("conDetailContent", replaceParameter(request.getParameter("conDetailContent")));
		requestMap.put("conDetailNm", replaceParameter(request.getParameter("conDetailNm")));
		requestMap.put("flag", request.getParameter("flag"));
		
		try {
			int resultInt = dao.ConListUpdate(requestMap);
			
			if(resultInt > 0) {
				resultMap.put("resultCode", 0);
				resultMap.put("resultMessage", "SUCCESS");
			}else {
				resultMap.put("resultCode", -2);
				resultMap.put("resultMessage", "NOT DATA");
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "ERROR");
			
			return resultMap;
		}
		return resultMap;
	}
	
	
	@Override
	public List<Map<String, Object>> groupApprovalList(HttpServletRequest request) {
		return dao.groupApprovalList();
	}
	
	
	@Override
	public List<Map<String, Object>> approvalList(HttpServletRequest request) {
		return dao.approvalList();
	}
	
	@Override
	public Map<String, Object> updateBatchApproval(HttpServletRequest request) throws Exception {
		
		String approvalList = request.getParameter("approvalList");
		String userNo = SessionUtil.getSession("memberSession", "USER_NO");
		Gson gson = new Gson();
		
		JsonArray resultArray = gson.fromJson(approvalList, JsonArray.class);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> resultMap2 = new HashMap<String, Object>();
		resultMap.put("name", "approval");
		
		if(!SessionUtil.getSession("memberSession", "USER_GRADE").equals("9")) {
			resultMap.put("resultCode", -9);
			resultMap.put("resultMessage", "올바르지 않은 접근");
			return resultMap;
		}
		
		Map<String, Object> idx = dao.selectMailBatchMax(resultMap);
		if(!idx.get("IDX").equals("0")) {
			resultMap.put("idx", idx.get("IDX"));
			dao.updateBatchSchedule(resultMap);
			
		}
		
		try {
			if (resultArray.size() != 0) {
				for (int i = 0; i < resultArray.size(); i++) {
					
					resultMap2 = new HashMap<String, Object>();
					JsonObject map = resultArray.get(i).getAsJsonObject();

					resultMap2.put("idx", map.get("IDX2").getAsString());
					resultMap2.put("name", "approval");
					resultMap2.put("userNo", map.get("USER_NO").getAsString());
					resultMap2.put("status", map.get("STATUS").getAsString());
					resultMap2.put("com", map.get("COM").getAsString());
					dao.insertApprovalUser(resultMap2);
				}
			}
			
		} catch (Exception e) {
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "ERROR");
			
			logger.error(e.getLocalizedMessage());
		}
		
		
//		 User Log 남기기
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", userNo);
		userLog.put("menu_name", "Approval Batch Setting Update");		
		userLog.put("user_ip", clientIP);
		userLog.put("logFlag", "10");
		
		userLog.put("job_info", "결재 합의자/통보자 설정 변경");
		userDAO.insertLog(userLog);
		
		resultMap.put("resultCode", 0);
		resultMap.put("resultMessage", "SUCCESS");
		
		return resultMap;
		
	}
	
	
	
	@Override
	public List<Map<String, Object>> getProcessingFlag(String gridName) throws Exception {
		Map<String, Object> requestMap = new HashMap<>();
		
		requestMap.put("type",null);
		
		if(gridName.contains("true")) {
			requestMap.put("type", 1);
		}else if(gridName.contains("false")) {
			requestMap.put("type", 0);
		}else if(gridName.contains("exception")) {
			requestMap.put("type", 2);
		}
		
		
		logger.info(requestMap.toString());
        return dao.getProcessingFlag(requestMap);
	}

    @Override
    public void updateProcessingFlag(HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<>();
        
        int type = Integer.parseInt(request.getParameter("type").toString());
        
        
        if(type != 2) {
        	
            String comment_title= request.getParameter("comment_title").toString();
            String comment_text= request.getParameter("comment_text").toString();
            
            map.put("comment_title",comment_title );
            map.put("comment_text",comment_text );
        }
        
        int processing_flag = Integer.parseInt(request.getParameter("processing_flag").toString());
        String processing_flag_name= request.getParameter("processing_flag_name").toString();
        int date_enable = Integer.parseInt(request.getParameter("date_enable").toString());
        int comment_enable = Integer.parseInt(request.getParameter("comment_enable").toString());
        
        map.put("date_enable", date_enable);
        map.put("comment_enable",comment_enable );
        map.put("processing_flag", processing_flag);
        map.put("processing_flag_name",processing_flag_name );
        
        map.put("type",type );
        
        dao.updateProcessingFlag(map);
    }
    
    @Override
    public void deleteProcessingFlag(HttpServletRequest request) throws Exception {
    	Map<String, Object> map = new HashMap<>();
    	
    	int processing_flag = Integer.parseInt(request.getParameter("processing_flag").toString());
    	int type = Integer.parseInt(request.getParameter("type").toString());
    	
    	map.put("processing_flag", processing_flag);
    	map.put("type", type);
    	
    	dao.deleteProcessingFlag(map);
    }

    @Override
    public void insertProcessingFlag(HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<>();
        
        int type = Integer.parseInt(request.getParameter("type").toString());
        String processing_flag_name= request.getParameter("processing_flag_name").toString();
        int date_enable = Integer.parseInt(request.getParameter("date_enable").toString());
        int comment_enable = Integer.parseInt(request.getParameter("comment_enable").toString());
        
        if(type != 2) {
        	String comment_title= request.getParameter("comment_title").toString();
        	String comment_text= request.getParameter("comment_text").toString();
        	
        	map.put("comment_title",comment_title );
        	map.put("comment_text",comment_text );
        }
        
        
        map.put("processing_flag_name",processing_flag_name );
        map.put("date_enable", date_enable);
        map.put("comment_enable",comment_enable );
        map.put("type",type );
        
        dao.insertProcessingFlag(map);
        System.out.println("map :: "+map);
        
    }

    @Override
    public void updateExceptionFlag(HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<>();
        
        int path_ex_flag = Integer.parseInt(request.getParameter("path_ex_flag").toString());
        String path_ex_flag_name= request.getParameter("path_ex_flag_name").toString();
        int enable = Integer.parseInt(request.getParameter("enable").toString());
        
        map.put("path_ex_flag",path_ex_flag);
        map.put("path_ex_flag_name",path_ex_flag_name );
        map.put("enable", enable);
        
        dao.updateExceptionFlag(map);
        
    }

    @Override
    public void insertExceptionFlag(HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<>();
        
        String path_ex_flag_name= request.getParameter("path_ex_flag_name").toString();
        int enable = Integer.parseInt(request.getParameter("enable").toString());
        
        map.put("path_ex_flag_name",path_ex_flag_name );
        map.put("enable", enable);
        
        dao.insertExceptionFlag(map);
    }
	
	// 페이지 접근 세션 확인
	@Override
	public Map<String, Object> checkPageGrade(String requestUrl) throws Exception {
		Map<Object, Object> resultMap = new HashMap<>();
		
		resultMap.put("requestUrl", requestUrl);
		resultMap.put("userGrade", SessionUtil.getSession("memberSession", "USER_GRADE"));
	
		return dao.checkPageGrade(resultMap);
	}
	
	// 페이지 접속 로그 생성
	@Override
	public void checkPageLog(HttpServletRequest request, String header_name ) throws Exception {
		
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		String user_no = "";
		
		if(SessionUtil.getSession("memberSession", "USER_NO") != null) {
			user_no = SessionUtil.getSession("memberSession", "USER_NO");
		}
		
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "PAGE ACCESS");		
		userLog.put("user_ip", clientIP);
		userLog.put("job_info", "Page_Access_"+header_name);
		userLog.put("logFlag", "8");
		
		userDAO.insertLog(userLog);
		Map<Object, Object> resultMap = new HashMap<>();
	}
	
	//	크로스사이트 스크립팅 방지처리
	public String replaceParameter(String param) {
		String result = param;
		if(param != null) {
			result = result.replaceAll("&", "&amp;");
			result = result.replaceAll("<", "&lt;");
			result = result.replaceAll(">", "&gt;");
			result = result.replaceAll("\"", "&quot;");
		}
	      
		return result;
	}

	@Override
	public List<Map<String, Object>> conDataList(HttpServletRequest request) throws Exception {
		return dao.conDataList();
	}
	
	@Override
	public void deleteGroupApprovalUser(HttpServletRequest request) throws Exception {
		
		List<Map<String, Object>> resultList = new ArrayList<>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			String insa_code = request.getParameter("insa_code").toString();
			String user_no = request.getParameter("user_no").toString();
			
			String userNo = SessionUtil.getSession("memberSession", "USER_NO");
			
			resultMap.put("insa_code", insa_code);
			resultMap.put("user_no", user_no);
			
			 dao.deleteGroupApprovalUser(resultMap);
			 
			ServletUtil servletUtil = new ServletUtil(request);
			String clientIP = servletUtil.getIp();
			Map<String, Object> userLog = new HashMap<String, Object>();
			userLog.put("user_no", userNo);
			userLog.put("menu_name", "Change the approver of the team");		
			userLog.put("user_ip", clientIP);
			userLog.put("logFlag", "10");
			
			userLog.put("job_info", insa_code+"("+user_no+") 결재 권한 삭제");
			userDAO.insertLog(userLog);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :: " +e.getLocalizedMessage());
		}
	}
	
	@Override
	public void insertGroupApprovalUser(HttpServletRequest request) throws Exception {
		
		List<Map<String, Object>> resultList = new ArrayList<>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {  
			String updateInsaCode = request.getParameter("updateInsaCode").toString();
			String user_no = request.getParameter("user_no").toString();
			
			String userNo = SessionUtil.getSession("memberSession", "USER_NO");
			
			resultMap.put("name", "approval_group");
			resultMap.put("userNo", user_no);
			resultMap.put("status", updateInsaCode);
			 
			Map<String, Object> approvalUser = dao.selectTeamApprovalUserCnt(resultMap);
			logger.info("approvalUser ::: " +approvalUser.size());
			if(approvalUser == null || Integer.parseInt(approvalUser.get("cnt").toString()) == 0) dao.insertApprovalUser(resultMap);
			
			ServletUtil servletUtil = new ServletUtil(request);
			String clientIP = servletUtil.getIp();
			Map<String, Object> userLog = new HashMap<String, Object>();
			userLog.put("user_no", userNo);
			userLog.put("menu_name", "Change the approver of the team");		
			userLog.put("user_ip", clientIP);
			userLog.put("logFlag", "10");
			
			userLog.put("job_info", updateInsaCode+"("+user_no+") 결재 권한 추가");
			userDAO.insertLog(userLog);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :: " +e.getLocalizedMessage());
		}
	}
	

	@Override
	public Map<String, Object> updateBatchSchedule(HttpServletRequest request) throws Exception {
		
		String accessIP = request.getParameter("accessIP");
		String userNo = SessionUtil.getSession("memberSession", "USER_NO");
		String status = request.getParameter("status");
		String pwd = request.getParameter("pwd");
		String email = request.getParameter("email");
		Gson gson = new Gson();
		
		Map<String, Object> resultMap1 = new HashMap<String, Object>();
		
		if(!SessionUtil.getSession("memberSession", "USER_GRADE").equals("9")) {
			resultMap1.put("resultCode", -9);
			resultMap1.put("resultMessage", "올바르지 않은 접근");
			return resultMap1;
		}
		
		String resultList = request.getParameter("resultList");
		
		JsonArray resultArray = gson.fromJson(resultList, JsonArray.class);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();

		resultMap.put("name", "mail");
		resultMap.put("status", status);
		
		Map<String, Object> idx = dao.selectMailBatchMax(resultMap);
		
		try {
			if (resultArray.size() != 0) {
				for (int i = 0; i < resultArray.size(); i++) {
					
					JsonObject map = resultArray.get(i).getAsJsonObject();

					String time = map.get("time").getAsString();

					if (status.equals("W")) { // 매주
					    resultMap.put("day", map.get("week").getAsString());
					} else if (status.equals("M")) { // 매달
					    resultMap.put("day", map.get("day").getAsString());
					} else if (status.equals("D")) { // 매일
					    resultMap.put("day", null);
					}

					resultMap.put("time", time);
					resultMap.put("pwd", encrypt(0, pwd, key));
					resultMap.put("email", email);
					resultMap.put("userNo", userNo);

					logger.info("resultArray :: " + resultMap);

					dao.insertBatchSchedule(resultMap);
				}
			}
			
			resultMap.put("idx", idx.get("IDX"));
			dao.updateBatchSchedule(resultMap);
			
		} catch (Exception e) {
			resultMap.put("resultCode", -100);
			resultMap.put("resultMessage", "ERROR");
			
			logger.error(e.getLocalizedMessage());
		}
		
//		 User Log 남기기
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", userNo);
		userLog.put("menu_name", "Mail Batch Setting Update");		
		userLog.put("user_ip", clientIP);
		userLog.put("logFlag", "10");
		
		userLog.put("job_info", "메일 연동 설정 변경");
		userDAO.insertLog(userLog);
		
		resultMap1.put("resultCode", 0);
		resultMap1.put("resultMessage", "SUCCESS");
		
		return resultMap1;
	}
	
	@Override
	public Map<String, Object> cryptPWD(HttpServletRequest request) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<>();
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		String password = request.getParameter("password");
		int cryptflag = Integer.parseInt(request.getParameter("cryptflag"));
		
		if(!user_grade.equals("9")) {
			resultMap.put("resultCode", -9);
			resultMap.put("resultMessage", "올바르지 않은 접근");
			
			return resultMap;
		}
		
		try {
			String val = encrypt(cryptflag, password, key);
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", val);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("error ::: " + e.getLocalizedMessage());
		}
		
		return resultMap;
	}

	//	암호화
	private String AESEncrypt(String pwd) throws Exception {
		
		byte[] PKey = key.getBytes(); 
		byte[] encrypted = null ;
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			SecretKeySpec keySpec = new SecretKeySpec(PKey, ALGORITHM);
			logger.info("keySpec :: " + key);
			logger.info("keySpec :: " + PKey);
			logger.info("keySpec :: " + keySpec);
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
	
	
	public static String encrypt(int cryptflag, String password, String KeyNM) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		String val = null;
		encryptor.setPassword(KeyNM);
		encryptor.setAlgorithm("PBEWithMD5AndDES");
		
		if(cryptflag == 0) { // 암호화
			val = encryptor.encrypt(password);
		}else if(cryptflag == 1) { // 복호화
			val =  encryptor.decrypt(password);
		}
		
		return val;
	}
	
	@Override
	public Map<String, Object> chkPattern(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		if(!user_grade.equals("9")) {
			resultMap.put("resultCode", -9);
			resultMap.put("resultMessage", "올바르지 않은 접근");
			
			return resultMap;
		}
		
		try {
			SimpleDateFormat format = new SimpleDateFormat ("yyMMdd");
			Date time = new Date();
			String timer = format.format(time);
			
			JsonObject jObject = new JsonObject();
			JsonArray builtinsArr = new JsonArray();
			JsonArray datatypeArr = new JsonArray();
			JsonObject datatypeObject = new JsonObject();
			
			jObject.addProperty("label", timer+"_"+user_no+"_Setting_chk");
			jObject.add("builtins", builtinsArr);
			
			String expression = request.getParameter("patternRuleTd").toString().replaceAll("\\\\n", "\n").replaceAll("%s", " POSTPROCESS \"MINIMUM 1\"");
			
			datatypeObject.addProperty("label", request.getParameter("patternReconNm"));
			datatypeObject.addProperty("disabled", false);
			datatypeObject.addProperty("expression", expression);

			datatypeArr.add(datatypeObject);

			jObject.add("custom_expressions", datatypeArr);
			jObject.addProperty("ocr", false);
			jObject.addProperty("voice", false);
			jObject.addProperty("ebcdic", false);
			jObject.addProperty("suppress", true);
			jObject.addProperty("capture", true);
			
			ReconUtil reconUtil = new ReconUtil();
			Map<String, Object> httpsResponse = null;

			Properties properties = new Properties();
			String resource = "/property/config.properties";
			Reader reader = Resources.getResourceAsReader(resource);
			properties.load(reader);
			try {
				String data = jObject.toString(); 
				
				this.recon_url = properties.getProperty("recon.url");
				this.recon_id = properties.getProperty("recon.id");
				this.recon_password = properties.getProperty("recon.password") ;
				this.api_ver = properties.getProperty("recon.api.version");
				
				httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/"+ this.api_ver + "/datatypes/profiles", "POST", data);
	
				String HttpsResponseDataMessage = "";
				try {
					HttpsResponseDataMessage = httpsResponse.get("HttpsResponseDataMessage").toString();
				}catch (NullPointerException e) {
					logger.error(e.toString());
				}
				
				logger.info("HttpsResponseDataMessage : " + HttpsResponseDataMessage);
				
				int resultCode = Integer.parseInt(httpsResponse.get("HttpsResponseCode").toString());
				logger.info("resultCode : " + resultCode);
				
				if(resultCode == 201) {
					resultMap.put("resultCode", 0);
					resultMap.put("resultMessage", "올바른 개인정보 유형");
				}else {
					resultMap.put("resultCode", -1);
					resultMap.put("resultMessage", "올바르지 않은 개인정보 유형");
				}
				
			} catch (Exception e) {
				resultMap.put("resultCode", -2);
				resultMap.put("resultMessage", "올바르지 않은 개인정보 유형");
				logger.info("credate datatypes error ::: " + e);
			}
			
		} catch (Exception e) {
			resultMap.put("resultCode", -3);
			resultMap.put("resultMessage", "올바르지 않은 개인정보 유형");
			logger.info("error ::: " + e);
		}
		
		return resultMap;
	}
	
	@Override
	public List<Map<String, Object>> reconNodeSelect(HttpServletRequest request) throws Exception{
		
		ReconUtil reconUtil = new ReconUtil();
		Map<String, Object> httpsResponse = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> filterMap = new HashMap<String, Object>();
		List<Map<String, Object>> reusultList = new ArrayList<>();
		
		try {
			Properties properties = new Properties();
			String resource = "/property/config.properties";
			Reader reader = Resources.getResourceAsReader(resource);
			
			properties.load(reader);
			
			String api_ver = properties.getProperty("recon.api.version");
			
			String recon_url = properties.getProperty("recon.url");
			String recon_id = properties.getProperty("recon.id");
			String recon_password =  properties.getProperty("recon.password");
			
			httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/v1/nodeagents", "GET", null);
			JsonArray jsonArray = new Gson().fromJson(httpsResponse.get("HttpsResponseData").toString(), JsonArray.class);

			for (int i = 0; i < jsonArray.size(); i++) {
			    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
				filterMap = new HashMap<String, Object>();
				
				filterMap.put("platform", jsonObject.get("platform"));
				filterMap.put("version", jsonObject.get("version"));
				filterMap.put("filename", jsonObject.get("filename"));
				filterMap.put("download_url", jsonObject.get("download_url"));
				filterMap.put("md5_hash", jsonObject.get("md5_hash"));
				filterMap.put("sha1_hash", jsonObject.get("sha1_hash"));
				filterMap.put("sha256_hash", jsonObject.get("sha256_hash"));
				
				reusultList.add(filterMap);
			}
		} catch (Exception e) {
			logger.info("error " + e);
			e.printStackTrace();
			resultMap.put("resultCode", -1);
			logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
		}
		
		return reusultList;
	}
	
	@Override
	public List<Map<String, Object>> PICNodeSelect(HttpServletRequest request) throws Exception{
		
		ReconUtil reconUtil = new ReconUtil();
		Map<String, Object> httpsResponse = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> filterMap = new HashMap<String, Object>();
		List<Map<String, Object>> reusultList = new ArrayList<>();
		
		try {
			Properties properties = new Properties();
			String resource = "/property/config.properties";
			Reader reader = Resources.getResourceAsReader(resource);
			
			properties.load(reader);
			
			String md5 = properties.getProperty("recon.md5");
			String sha1 = properties.getProperty("recon.sha1");
			String sha256 = properties.getProperty("recon.sha256");
			 
			filterMap.put("version", "0.1");
			filterMap.put("filename", "/home/picenter/Tomcat<br>/webapps/ROOT.war");
			filterMap.put("md5_hash", md5);
			filterMap.put("sha1_hash", sha1);
			filterMap.put("sha256_hash", sha256);
			
			reusultList.add(filterMap);
		} catch (Exception e) {
			logger.info("error " + e);
			e.printStackTrace();
			resultMap.put("resultCode", -1);
			logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
		}
		
		return reusultList;
	}
	
	@Override
	public Map<String, Object> backupTables(List<String> tables, int value, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String userNo = SessionUtil.getSession("memberSession", "USER_NO");
		
	    try {
	    	Properties properties = new Properties();
		    String resource = "/property/dbpool.properties";
		    Reader reader = Resources.getResourceAsReader(resource);
		    properties.load(reader);
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		    String date = sdf.format(new Date());
		    String fileName = null;
		    String path = properties.getProperty("db.backup.path");
		    if(value == 1) {
		    	fileName = "PIC_Policy_BackUp_" + date + ".sql";
		    }else if(value == 2) {
		    	fileName = "PIC_Detection_BackUp_" + date + ".sql";
		    }else if(value == 3) {
		    	fileName = "PIC_Approval_BackUp_" + date + ".sql";
		    }
		    String filePath = path + fileName;
		    
	        String username = properties.getProperty("jdbc.username");
	        String password = properties.getProperty("jdbc.password");
	        String mysqlDumpPath = "mysqldump";
	        String databaseName = "picenter";
	        
	        String tableList = String.join(" ", tables);
	        List<String> commands = Arrays.asList(
	            "bash", "-c",
	            mysqlDumpPath + " -u " + username + " -p" + password + " " + databaseName + " " + tableList + " > " + filePath
	        );
	        
	        ProcessBuilder pb = new ProcessBuilder(commands);
	        pb.redirectErrorStream(true);
	        Process process = pb.start();

	        try (BufferedReader bfReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
	            String line;
	            while ((line = bfReader.readLine()) != null) {
	                logger.info(line);
	            }
	        }

	        int exitCode = process.waitFor();
	        logger.info("Process exited with code: " + exitCode);
	        if (exitCode == 0) {
	        	logger.info("Backup successful to file: " + filePath);
	            resultMap.put("resultCode", 0);
				resultMap.put("resultMessage", "백업에 성공하였습니다.");
				
				ServletUtil servletUtil = new ServletUtil(request);
				String clientIP = servletUtil.getIp();
				Map<String, Object> userLog = new HashMap<String, Object>();
				userLog.put("user_no", userNo);
				userLog.put("menu_name", "Backup successful to file: " + filePath);		
				userLog.put("user_ip", clientIP);
				userLog.put("logFlag", "10");
				
				userLog.put("job_info", "백업 성공");
				userDAO.insertLog(userLog);
				
				dao.insertBackUpFile(fileName);
	        } else {
	        	logger.info("Backup failed with exit code: " + exitCode);
	            resultMap.put("resultCode", -1);
				resultMap.put("resultMessage", "백업에 실패하였습니다.");
	        }
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return resultMap;
	}
	
	@Override
	public List<Map<String, Object>> rollBackList(HttpServletRequest request) {
		return dao.rollBackList();
	}
	
	@Override
    public Map<String, Object> rollBackTables(String filePath, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String userNo = SessionUtil.getSession("memberSession", "USER_NO");
		
	    try {
	    	Properties properties = new Properties();
		    String resource = "/property/dbpool.properties";
		    
	    	Reader reader = Resources.getResourceAsReader(resource);
	    	properties.load(reader);
	    	
	    	String username = properties.getProperty("jdbc.username");
	    	String password = properties.getProperty("jdbc.password");
	    	String mysqlPath = "mysql";
	    	String databaseName = "picenter";
	    	String path = properties.getProperty("db.backup.path");
	    	String rollBackPath = path + filePath;
	    	
	    	String command = "bash -c \"" + mysqlPath + " -u " + username + " -p" + password + " " + databaseName + " < '" + rollBackPath + "'\"";
	    	ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
	    	pb.redirectErrorStream(true);
	    	Process process = pb.start();
	    	
	    	try (BufferedReader bfReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
	            String line;
	            while ((line = bfReader.readLine()) != null) {
	            	logger.info(line);
	            }
	        }
	    	
	    	int exitCode = process.waitFor();
	    	logger.info("Process exited with code: " + exitCode);
	        if (exitCode == 0) {
	        	logger.info("Rollback success");
	        	resultMap.put("resultCode", 0);
				resultMap.put("resultMessage", "복원에 성공하였습니다.");
	            
	            ServletUtil servletUtil = new ServletUtil(request);
				String clientIP = servletUtil.getIp();
				Map<String, Object> userLog = new HashMap<String, Object>();
				userLog.put("user_no", userNo);
				userLog.put("menu_name", "Rollback successful");		
				userLog.put("user_ip", clientIP);
				userLog.put("logFlag", "10");
				
				userLog.put("job_info", "복원 성공");
				userDAO.insertLog(userLog);
	        } else {
	            System.out.println("Rollback failed with exit code: " + exitCode);
	            resultMap.put("resultCode", -1);
				resultMap.put("resultMessage", "복원에 실패하였습니다.");
	        }
	    	
		} catch (Exception e) {
			e.printStackTrace();
	    }
	    
	    return resultMap;
    }

	@Override
	public void userlogUpdate(HttpServletRequest request, String job_info) throws Exception {
		
//		 User Log 남기기
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", SessionUtil.getSession("memberSession", "USER_NO"));
		userLog.put("menu_name", "PICENTER_SESSION_TIME_OUT_UPDATE");		
		userLog.put("user_ip", clientIP);
		userLog.put("logFlag", "10");
		
		userLog.put("job_info", job_info);
		userDAO.insertLog(userLog);
		
	}
	
	@Override
	public Map<String, Object> systemLog(HttpServletRequest request) throws Exception {
		String csvType = request.getParameter("csvType");
		
		String csvFile = "D:\\백업\\PICenter\\05.PICenter (Samsung_ver)\\02.메리츠\\"+csvType+".csv"; // CSV 파일 경로
        List<Object[]> dataList = new ArrayList<>();
        
        Map<String, Object> resultMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
           br.readLine(); // 첫 줄(헤더) 건너뛰기
            
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                
                Object[] row = new Object[5];
                row[0] = data[0].trim();
                row[1] = Double.parseDouble(data[1].trim()); 
                row[2] = Double.parseDouble(data[2].trim()); 
                row[3] = Double.parseDouble(data[3].trim()); 
                
                // 리스트에 추가
                dataList.add(row);
            }
            
            resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", dataList);
            
        } catch (IOException e) {
            e.printStackTrace();
            resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "실패");
        }
		return resultMap;
	}

	//개인정보유형 삭제	
	@Override
	public List<Map<String, Object>> reportHeaderList(HttpServletRequest request) throws Exception {
		
		Map<String, Object> requestMap = new HashMap<>();
		List<Map<String, Object>> reusultList = new ArrayList<>();
		
		String report_flag = request.getParameter("report_flag");
		logger.info("report_flag ::: " + report_flag);
		requestMap.put("report_flag", report_flag);
		try {
			reusultList = dao.reportHeaderList(requestMap);
		} catch (Exception e) {
			logger.error(e.getMessage());
			
		}
		return reusultList;
	}

	public List<Map<String, Object>> selectApprovalGroupCnt(HttpServletRequest request)  throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		List<Map<String, Object>> reusultList = new ArrayList<>();
		try { 
			String insa_code = SessionUtil.getSession("memberSession", "INSA_CODE");
			resultMap.put("insa_code", insa_code); 
			reusultList = dao.selectApprovalGroupCnt(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :: " +e.getLocalizedMessage());
		}
		return reusultList;
	}
	
}
