package com.org.iopts.service; 

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.ibatis.io.Resources;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.opencsv.CSVReader;
import com.org.iopts.dao.Pi_TargetDAO;
import com.org.iopts.dao.Pi_UserDAO;
import com.org.iopts.dto.Pi_TargetVO;
import com.org.iopts.dto.Pi_Target_ManageVO;
import com.org.iopts.popup.dao.PopupDAO;
import com.org.iopts.util.DataUtil;
import com.org.iopts.util.ReconUtil;
import com.org.iopts.util.ServletUtil;
import com.org.iopts.util.SessionUtil;

//import au.com.bytecode.opencsv.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@PropertySource("classpath:/property/config.properties")

@Service
public class Pi_TargetServiceImpl implements Pi_TargetService {

	private static final Logger logger = LoggerFactory.getLogger(Pi_TargetServiceImpl.class);
	
	@Value("${recon.id}")
	private String recon_id;

	@Value("${recon.password}")
	private String recon_password;

	@Value("${recon.url}")
	private String recon_url;
	
	@Value("${recon.api.version}")
	private String api_ver;

	@Resource(name = "Pi_TargetDAO")
	private Pi_TargetDAO dao;
	
	@Value("${saveAttchPath}")
	private String saveAttchPath;

	@Inject
	private Pi_UserDAO userDao;
	
	@Inject
	private PopupDAO pop_dao;

	Map<String, Object> readerMap = new HashMap<String, Object>();

	@Override
	public List<Map<String, Object>> selectTargetManagement() throws Exception {
		// TODO Auto-generated method stub
		return dao.selectTargetManage();
	}

	@Override
	public List<Map<String, Object>> selectTargetList(HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub

		String host = request.getParameter("host");

		return dao.selectTargetList(host);
	}

	@Override
	public List<Map<String, Object>> selectTarget() throws Exception {
		// TODO Auto-generated method stub
		return dao.selectTargetManage();
	}

	@Override
	public List<Map<String, Object>> selectTargetUser(HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		String target = request.getParameter("target");
		String insa_code = SessionUtil.getSession("memberSession", "INSA_CODE");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("target", target);
		map.put("insa_code", insa_code);
		map.put("ap_no", request.getParameter("ap_no"));

		return dao.selectTargetUser(map);
	}

	@Override
	@Transactional
	public void registTargetUser(HttpServletRequest request) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		String target = request.getParameter("target");
		String userList = request.getParameter("userList");
		String ap_no = request.getParameter("ap_no");

		JsonArray userArray = new Gson().toJsonTree(userList).getAsJsonArray();

		HashMap<String, Object> sqlMap = new HashMap<>();
		sqlMap.put("target_id", target);

		Map<String, Object> targetMap = dao.selectTargetById(sqlMap);

		if (userArray.size() != 0) {
		    for (int i = 0; i < userArray.size(); i++) {
		        JsonObject userMap = userArray.get(i).getAsJsonObject();
				map.put("target", target);
				map.put("user_no", userMap.get("userNo").getAsString());
				map.put("ap_no", ap_no);

				// User Log 남기기
				String user_no = SessionUtil.getSession("memberSession", "USER_NO");
				ServletUtil servletUtil = new ServletUtil(request);
				String clientIP = servletUtil.getIp();
				Map<String, Object> userLog = new HashMap<>();
				userLog.put("user_no", user_no);
				userLog.put("menu_name", "CHANGE PERSON IN CHARGE");
				userLog.put("user_ip", clientIP);
				userLog.put("logFlag", "4");

				if (userMap.get("chk").getAsString().equals("1")) {
				    dao.registTargetUser(map);
				    userLog.put("job_info",
				            "타겟담당자등록 - " + targetMap.get("NAME") + "[" + userMap.get("userName").getAsString() + "]");
				} else {
				    dao.deleteTargetUser(map);
				    userLog.put("job_info",
				            "타겟담당자삭제 - " + targetMap.get("NAME") + "[" + userMap.get("userName").getAsString() + "]");
				}

				userDao.insertLog(userLog);
			}
		}

	}

	/**
	 * Targets Insert
	 * 
	 * @throws Exception
	 */
	@Override
	@Transactional
	public int insertTarget(List<Pi_TargetVO> list) throws Exception {
		return dao.insertTarget(list);
	}

	@Override
	public List<Map<String, Object>> selectUserTargetList(HttpServletRequest request) throws Exception {

		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		String host = request.getParameter("host");

		Map<String, Object> searchMap = new HashMap<String, Object>();
		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();

		searchMap.put("user_no", user_no);
		searchMap.put("user_grade", user_grade);
		searchMap.put("host", host);
		map = dao.selectUserTargetList(searchMap);
		return map;
	}

	@Override
	public List<Map<String, Object>> selectServerList(HttpServletRequest request) throws Exception {

		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		String host = request.getParameter("host");

		Map<String, Object> searchMap = new HashMap<String, Object>();
		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();

		searchMap.put("user_no", user_no);
		searchMap.put("user_grade", user_grade);
		searchMap.put("host", host);
		map = dao.selectServerList(searchMap);
		return map;
	}

	@Override
	public List<Map<String, Object>> selectTargetUserList(HttpServletRequest request) throws Exception {
		List<Map<String, Object>> resultList =  new ArrayList<>();
		try {
			Map<String, Object> resultMap = new HashMap<>();
			
			resultMap.put("target_id", request.getParameter("target_id"));
			resultMap.put("ap_no", request.getParameter("ap_no"));
			resultMap.put("mnger_status", request.getParameter("mnger_status"));
			
			logger.info("resultMap :: " + resultMap);
			
			resultList = dao.selectTargetUserList(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error Log ::: " + e);
		}
		return resultList; 
	}

	@Override
	public List<Map<String, Object>> selectServerFileTopN(HttpServletRequest request) {
		String target = request.getParameter("target");
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");

		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("target_id", target);
		searchMap.put("user_no", user_no);

		logger.info(searchMap.toString());
		return dao.selectServerFileTopN(searchMap);
	}

	@Override
	public List<Map<String, Object>> selectAdminServerFileTopN(HttpServletRequest request) {
		String target = request.getParameter("target");

		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("target_id", target);

		logger.info(searchMap.toString());
		return dao.selectAdminServerFileTopN(searchMap);
	}

	// DMZ List 조회
	@Override
	public List<Map<String, Object>> selectDmzList(HashMap<String, Object> params, HttpServletRequest request)
			throws Exception {
		// SCH_DMZ_IP & SCH_MEMO
		String sch_dmz_ip = request.getParameter("SCH_DMZ_IP");
		String sch_memo = request.getParameter("SCH_MEMO");

		if (sch_dmz_ip != null && !"".equals(sch_dmz_ip)) {
			logger.info("DMZ :: " + sch_dmz_ip);
			params.put("SCH_DMZ_IP", sch_dmz_ip);
		}
		if (sch_memo != null && !"".equals(sch_memo)) {
			logger.info("MEMO :: " + sch_memo);
			params.put("SCH_MEMO", sch_memo);
		}

		return dao.selectDmzList(params);
	}

	// DMZ Info Save
	@Transactional
	public void saveDmzInfo(HashMap<String, Object> params) throws Exception {
		String[] ipArr = ((String) params.get("DMZ_IP")).split("\\.");

		if (ipArr[2].equals("*") && ipArr[3].equals("*")) { // ip C,D Class *.*
			String dmzIpAB = ipArr[0] + "." + ipArr[1] + ".";
			for (int i = 0; i <= 255; i++) {
				System.out.println("===C,D Class==> " + dmzIpAB + i + ".");
				params.put("DMZ_IP", dmzIpAB + i + ".");
				dao.saveDmzInfoAstr(params);
			}
		} else if (ipArr[3].equals("*")) { // ip D Class *
			String dmzIpABC = ipArr[0] + "." + ipArr[1] + "." + ipArr[2] + ".";
			params.put("DMZ_IP", dmzIpABC);
			dao.saveDmzInfoAstr(params);
		} else { // ip 단일 입력 경우
			dao.saveDmzInfo(params);
		}

		System.out.println("종~료");
	}

	// DMZ List Delete
	public void deleteDmzList(HashMap<String, Object> params) throws Exception {
		dao.deleteDmzList(params);
	}

	@Override
	public HashMap<String, Object> selectTargetById(HashMap<String, Object> params) throws Exception {
		return dao.selectTargetById(params);
	}

	@Override
	public List<Map<String, Object>> selectGroupList(Map<String, Object> map) throws Exception {
		return dao.selectGroupList(map);
	}

	@Override
	public List<Map<String, Object>> selectUserGroupList(Map<String, Object> map) throws Exception {
		return dao.selectUserGroupList(map);
	}

	/*@Override
	public List<Map<String, Object>> selectNoticeList(Map<String, Object> map) throws Exception {
		return dao.selectNoticeList(map);
	}
*/
	@Override
	public List<Map<String, Object>> selectNoticeList(Map<String, Object> map) throws Exception {
		
		List<Map<String, Object>> noticeList = dao.selectNoticeList(map);
		List<Map<String, Object>> resultList = new ArrayList<>();	
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		for(int i=0 ; i < noticeList.size() ; i++) {
			resultMap = new HashMap<String, Object>();
			String noticeID = noticeList.get(i).get("NOTICE_ID").toString();
			String regdate = noticeList.get(i).get("REGDATE").toString();
			String noticeTitle = noticeList.get(i).get("NOTICE_TITLE").toString();
			noticeTitle = replaceParameter(noticeTitle);
			
			resultMap.put("NOTICE_ID", noticeID);
			resultMap.put("REGDATE", regdate);
			resultMap.put("NOTICE_TITLE", noticeTitle);
			
			resultList.add(resultMap);
		}
		return resultList;
	}
	
	@Override
	public List<Map<String, Object>> getTargetList(Map<String, Object> map) throws Exception {
		return dao.getTargetList(map);
	}

	@Override
	public Map<String, Object> getGroupDetails(Map<String, Object> map) throws Exception {
		return dao.getGroupDetails(map);
	}

	@Override
	public void updateGroupDetails(Map<String, Object> map) throws Exception {
		dao.updateGroupDetails(map);
	}

	@Override
	public void addNewGroup(HttpServletRequest request, Map<String, Object> map) throws Exception {
		dao.addNewGroup(map);

		// User Log 남기기
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "ADD TARGET GROUP");
		userLog.put("user_ip", clientIP);
		userLog.put("logFlag", "5");

		userLog.put("job_info", "그룹등록 - " + map.get("name"));

		userDao.insertLog(userLog);
	}

	@Override
	public void deleteGroup(HttpServletRequest request, Map<String, Object> map) throws Exception {
		dao.deleteGroup(map);

		// User Log 남기기
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "DELETE TARGET GROUP");
		userLog.put("user_ip", clientIP);
		userLog.put("logFlag", "5");

		userLog.put("job_info", "그룹삭제 - " + request.getParameter("name"));

		userDao.insertLog(userLog);
	}

	@Override
	public void deleteGroupIdx_target(HttpServletRequest request, Map<String, Object> map) throws Exception {
		dao.deleteGroupIdx_target(map);
	}

	@Override
	public void pushTargetToGroup(HttpServletRequest request, Map<String, Object> map) throws Exception {
		dao.pushTargetToGroup(map);

		// User Log 남기기
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		Map<String, Object> userLog = new HashMap<String, Object>();
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "MANAGE TARGET GROUP");
		userLog.put("user_ip", clientIP);
		userLog.put("logFlag", "5");

		if (map.get("idx") == null || "".equals(map.get("idx"))) {
			userLog.put("job_info", "그룹 내 타겟 해제 - " + request.getParameter("name"));
		} else {
			userLog.put("job_info", "그룹 내 타겟 추가 - " + request.getParameter("name"));
		}

		userDao.insertLog(userLog);
	}

	// Timestamp time
	private StringBuffer getMenuTreeString(String time) throws Exception {
		StringBuffer menuString = new StringBuffer(2048);
		String[] sel_menu_id = null;
		String menu_id = ""; // 조직코드
		String menu_nm = ""; // 조직명
		String[] menu_type = null; // 조직Type

		long menu_level = 0; // 조직Level이 아닌 조회된 계층의 Level

		int div_cnt = 0; // <div> Tag Count

		String javascript_1 = "";
		String javascript_2 = "";
		String javascript_3 = "expandsub('1'); \r";
		
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
	}
	
	@Override
	public List<Map<String, Object>> selectServerTargetUser(HttpServletRequest request) throws Exception {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String insa_code = SessionUtil.getSession("memberSession", "INSA_CODE");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		logger.info("1. flag :: "+request.getParameter("flag"));
		String flag =  request.getParameter("flag")==null? "user":request.getParameter("flag");
		
		logger.info("id :: "+id);
		logger.info("flag :: "+flag);

		Map<String, Object> searchMap = new HashMap<String, Object>();
		List<Object> resultList =  new ArrayList<>();
		
		List<Map<Object, Object>> mngrList = dao.selectMngrNameList();
		

		if(flag.equals("insa")) { 
			searchMap.put("flag","insa");
			searchMap.put("insa_code", insa_code);
			searchMap.put("id", id);
			// Group
			
			if(id.equals("Group")) {searchMap.put("id", "");}
			//id == insaGroup
			else if(id.indexOf("_")==-1) { //존재하지않으면
				searchMap.put("insa_code", id);
				searchMap.put("id", "");
			}
			
			//id == insaGroup_idx
			else if(id.indexOf("_")!=-1) { // _ 존재하면
				logger.info("id :: "+id.split("_")[1]);
				searchMap.put("id", id.split("_")[1]);
			}
		
		}else if(flag.equals("pic")) {
			searchMap.put("flag","user");
			searchMap.put("target_id", id);
			searchMap.put("type", type);
			searchMap.put("id", "picenter");
			
			searchMap.put("pic_id", id.replace("IDX_", ""));
			 
			List<Map<String, Object>> idxList = dao.selectPICGroupIDList(searchMap);
			
			searchMap.put("idxList", idxList);
			
			List<Map<String, Object>> targetIDList = dao.selectPICGroupTargetIDList(searchMap);
			
			for (Map<String, Object> map : targetIDList) {
				resultList.add(map.get("IDX"));
			}
			searchMap.put("idList", resultList);
		}
		else { 
			searchMap.put("flag","user");
			
			if(type!= null && type.equals("98")) {
				List<Map<String, Object>> idxList = dao.selectNoGroupIdx(); 
				for (Map<String, Object> map : idxList) {
					resultList.add(map.get("IDX"));
				}
				searchMap.put("idList", resultList);
			}else if(id!=null && !id.equals("")) { //그룹의 target_id 받아오기
				List<Map<String, Object>> idxList = dao.selectGroupIdx(id);
				for (Map<String, Object> map : idxList) {
					resultList.add(map.get("IDX"));
				}
				searchMap.put("idList", resultList); 
			}
			searchMap.put("id", id);
		}
		
		searchMap.put("type", type);
		searchMap.put("user_no", user_no);
		searchMap.put("user_grade", user_grade);
		
		searchMap.put("mngrList", mngrList);
		searchMap.put("mngrSizeList", null);
		
		List<Map<String, Object>> returnObject = new ArrayList<>();
		returnObject = dao.selectServerTargetUser(searchMap);
		return returnObject;
	}
	
	@Override
	public List<Map<String, Object>> selectPCTargetUserName(HttpServletRequest request) {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		String id = request.getParameter("id");
		String parent = request.getParameter("parent");
		
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("user_no", user_no);
		searchMap.put("user_grade", user_grade);
		searchMap.put("id", id);
		searchMap.put("parent", parent);
		
		return dao.selectPCTargetUserName(searchMap);
	}
	@Override
	public List<Map<String, Object>> selectPCTargetUser(HttpServletRequest request) {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		String target_id = request.getParameter("target_id");
		String test = request.getParameter("test");
		String ap_no = request.getParameter("ap_no");
		String id = request.getParameter("id");
		String node = request.getParameter("node");
		
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("target_id", target_id);
		searchMap.put("ap_no", ap_no);
		searchMap.put("user_no", user_no);
		searchMap.put("user_grade", user_grade);
		searchMap.put("node", node);
		searchMap.put("id", id);
		
		logger.info(searchMap.toString());
		return dao.selectPCTargetUser(searchMap);
	}
	
	@Override
	public List<Map<String, Object>> selectPCTargetUserData(HttpServletRequest request) {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		String id = request.getParameter("id");
		
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("id", id);
		searchMap.put("user_no", user_no);
		searchMap.put("user_grade", user_grade);
		
		logger.info(searchMap.toString());
		return dao.selectPCTargetUserData(searchMap);
	}

	// 검색
	@Override
	public List<Map<String, Object>> searchServerTargetUser(HttpServletRequest request) {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		String insa_code = SessionUtil.getSession("memberSession", "INSA_CODE");
		String groupNm = request.getParameter("groupNm");
		String hostNm = request.getParameter("hostNm");
		String serviceNm = request.getParameter("serviceNm");
		String userIP = request.getParameter("userIP");
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String type = "";
		
		List<Object> resultList =  new ArrayList<>();
		Map<String, Object> searchMap = new HashMap<String, Object>();
		
		if(id!=null && !id.equals("")) {
			List<Map<String, Object>> idxList = dao.selectGroupIdx(id);
			for (Map<String, Object> map : idxList) {
				resultList.add(map.get("IDX"));
			}
			searchMap.put("idList", resultList);
		}
		
		List<Map<Object, Object>> mngrSizeList = new ArrayList<>();
		Map<Object, Object> mngrSizeMap = new HashMap<>();
		List<Map<Object, Object>> mngrList = dao.selectMngrNameList();
		Map<Object, Object> mngrMap = new HashMap<>();
		
		for(int i =0 ; i< mngrList.size() ; i++) {
			
			if(request.getParameter(i+"server") != null) {
				mngrSizeMap = new HashMap<>();
				mngrMap = mngrList.get(i);
				String mngrKey = request.getParameter(i+"server").toString();
				mngrSizeMap.put("server", mngrKey);
				
				logger.info(mngrKey);
				logger.info(request.getParameter(mngrKey));
				mngrSizeMap.put("server_user", request.getParameter(mngrKey));
				
				if(!request.getParameter(mngrKey).equals("")) mngrSizeList.add(mngrSizeMap);
			}
		}
		
		logger.info(mngrSizeList.toString());
		
		searchMap.put("insa_code", insa_code);
		searchMap.put("groupNm", groupNm);
		searchMap.put("hostNm", hostNm);
		searchMap.put("serviceNm", serviceNm);
		searchMap.put("userIP", userIP);
		searchMap.put("user_no", user_no);
		searchMap.put("user_grade", user_grade);
		searchMap.put("id", id.equals("Group")?"":id);
		searchMap.put("name", name);
		searchMap.put("type", type);
		searchMap.put("mngrList", mngrList);
		searchMap.put("mngrSizeList", mngrSizeList);
		
		return dao.selectServerTargetUser(searchMap);
	}
	
	@Override
	public List<Map<String, Object>> searchPCTargetUser(HttpServletRequest request) {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		String groupNm = request.getParameter("groupNm");
		String hostNm = request.getParameter("hostNm");
		String serviceNm = request.getParameter("serviceNm");
		String userIP = request.getParameter("userIP");
		String id = request.getParameter("id");
		String target_id = request.getParameter("target_id");
		String name = request.getParameter("name");

		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("groupNm", groupNm);
		searchMap.put("hostNm", hostNm);
		searchMap.put("serviceNm", serviceNm);
		searchMap.put("user_no", user_no);
		searchMap.put("user_grade", user_grade);
		searchMap.put("userIP", userIP);
		searchMap.put("id", id);
		searchMap.put("target_id", target_id);
		searchMap.put("name", name);

		logger.info(searchMap.toString());
		return dao.selectPCTargetUser(searchMap);
	}

	@Override
	public List<Map<String, Object>> getExceptionList(HttpServletRequest request) throws Exception {
		return dao.getExceptionList();
	}
	
	@Override
	public List<Map<String, Object>> exceptionSearchList(HttpServletRequest request) {
		
		List<Map<String, Object>> resultList = new ArrayList<>();
		try {
			
			Map<String, Object> map = new HashMap<>();
			map.put("net", request.getParameter("net"));
			map.put("group", request.getParameter("group"));
			map.put("host", request.getParameter("host"));
			map.put("service", request.getParameter("service"));
			map.put("req", request.getParameter("req"));
			map.put("reg", request.getParameter("reg"));
			map.put("path", request.getParameter("path"));
			map.put("exception_content", request.getParameter("exception_content"));
			logger.info("map :: " + map);
			resultList = dao.exceptionSearchList(map);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	
	@Override
	public List<Map<String, Object>> selectSKTManagerList(HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		String user_no = request.getParameter("user_no");
		String user_name = request.getParameter("user_name");
		String team_name = request.getParameter("team_name");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_no", user_no);
		map.put("user_name", user_name);
		map.put("team_name", team_name);

		return dao.selectSKTManagerList(map);
	}
	
	@Override
	public List<Map<String, Object>> selectAddSKTManagerList(HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		String user_name = request.getParameter("user_name");
		String team_name = request.getParameter("team_name");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_name", user_name);
		map.put("team_name", team_name);

		return dao.selectAddSKTManagerList(map);
	}
	
	@Override
	public Map<String, Object> insertSKTManager(Map<String, Object> map) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		
		// 사용자의 담당팀 중복 체크
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap = dao.selectChkSKTManager(map);
		
		try {
			if ((userMap == null) || (userMap.size() == 0)) {
				// 해당 중간관리자의 담당 그룹 상위 체크 리스트
				List<Map<String, Object>> upGroupList = dao.selectUpGroupUser(map);
				
				// 해당 팀에 소속되어있는 모든 PC 중간관리자 부여
				List<Map<String, Object>> groupUserList = dao.selectGroupUser(map);
				if(upGroupList.size() < 1) {	// 해당 상위 그룹이 없음
					// 해당 중간 관리자의 담당 그룹 하위 체크 리스트
					List<Map<String, Object>> downGroupList = dao.selectDownGroupUser(map);
					Map<String, Object> groupMap = new HashMap<String, Object>();
					
					// 하위 그룹 사용 안함으로 변경
					for(int i=0 ; i < downGroupList.size() ; i++) {
						
						String downInsaCode = downGroupList.get(i).get("ID").toString();
						String downUserNo = downGroupList.get(i).get("USER_NO").toString();
						
						groupMap.put("INSA_CODE", downInsaCode);
	            		groupMap.put("USER_NO", downUserNo);
	            		groupMap.put("ENABLE", "N");
	            		
	            		dao.insertSKTManager(groupMap);
					}
					
					map.put("ENABLE", "Y");
					dao.insertSKTManager(map);
				} else {						// 상위 그룹이 존재
					map.put("ENABLE", "N");
					dao.insertSKTManager(map);
				}
				
				for(int i=0 ; i < groupUserList.size() ; i++) {
					Map<String, Object> groupUserMap = new HashMap<String, Object>();
					
					String groupUserInsaCode = map.get("INSA_CODE").toString();
					String groupUserNo = map.get("USER_NO").toString();
					String ap = groupUserList.get(i).get("AP_NO").toString();
					String target_id = groupUserList.get(i).get("TARGET_ID").toString();
					
					groupUserMap.put("INSA_CODE", groupUserInsaCode);
					groupUserMap.put("USER_NO", groupUserNo);
					groupUserMap.put("AP_NO", ap);
					groupUserMap.put("TARGET_ID", target_id);
					
					dao.insertSKTManagerUser(groupUserMap);
				}
				
				resultMap.put("resultCode", 0);
				resultMap.put("resultMeassage", "중간관리자 지정 성공");
			}else {
				resultMap.put("resultCode", -2);
				resultMap.put("resultMeassage", map.get("USER_NAME").toString() + "님은 이미 " + map.get("TEAM_NAME").toString() + "의 중간관리자 입니다.");
				
			}
			// dao.insertSKTManager(map);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("resultCode", -1);
			resultMap.put("resultMeassage", "중간관리자 지정 실패");
		}
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> deleteSKTManager(Map<String, Object> map) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		
		// TODO Auto-generated method stub
		try {
			dao.deleteSKTManager(map);
			dao.deleteSKTManagerUser(map);
		}catch (Exception e) {
			// TODO: handle exception
			resultMap.put("resultCode", -1);
			resultMap.put("resultMeassage", "중간관리자 삭제 실패");
		}
		
		resultMap.put("resultCode", 0);
		resultMap.put("resultMeassage", "중간관리자 삭제 성공");
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> insertSKTManagerList(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		
		String resulList = request.getParameter("resulList");
		JsonArray jsonarry = new Gson().toJsonTree(resulList).getAsJsonArray();
		
		int chk = 0;
		try {
			// resultList를 담을 map
			Map<String, Object> map = new HashMap<String, Object>();
			// 사용자의 담당팀 중복 체크
			Map<String, Object> userMap = new HashMap<String, Object>();
			
			for(int i=0; i < jsonarry.size(); i++) {
				JsonObject jsonObject = jsonarry.get(i).getAsJsonObject();
				
				String user_no = jsonObject.get("user_no").getAsString();
				String insa_code = jsonObject.get("insa_code").getAsString();
				
				map.put("USER_NO", user_no);
				map.put("INSA_CODE", insa_code);
				
				userMap = dao.selectChkSKTManager(map);
				
				if ((userMap == null) || (userMap.size() == 0)) {
					// 해당 중간관리자의 담당 그룹 상위 체크 리스트
					List<Map<String, Object>> upGroupList = dao.selectUpGroupUser(map);
					
					// 해당 팀에 소속되어있는 모든 PC 중간관리자 부여
					List<Map<String, Object>> groupUserList = dao.selectGroupUser(map);
					if(upGroupList.size() < 1) {	// 해당 상위 그룹이 없음
						// 해당 중간 관리자의 담당 그룹 하위 체크 리스트
						List<Map<String, Object>> downGroupList = dao.selectDownGroupUser(map);
						Map<String, Object> groupMap = new HashMap<String, Object>();
						
						// 하위 그룹 사용 안함으로 변경
						for(int j=0 ; j < downGroupList.size() ; j++) {
							
							String downInsaCode = downGroupList.get(j).get("ID").toString();
							String downUserNo = downGroupList.get(j).get("USER_NO").toString();
							
							groupMap.put("INSA_CODE", downInsaCode);
		            		groupMap.put("USER_NO", downUserNo);
		            		groupMap.put("ENABLE", "N");
		            		
		            		dao.insertSKTManager(groupMap);
						}
						
						map.put("ENABLE", "Y");
						dao.insertSKTManager(map);
					} else {						// 상위 그룹이 존재
						map.put("ENABLE", "N");
						dao.insertSKTManager(map);
					}
					
					for(int j=0 ; j < groupUserList.size() ; j++) {
						Map<String, Object> groupUserMap = new HashMap<String, Object>();
						
						String groupUserInsaCode = map.get("INSA_CODE").toString();
						String groupUserNo = map.get("USER_NO").toString();
						String ap = groupUserList.get(j).get("AP_NO").toString();
						String target_id = groupUserList.get(j).get("TARGET_ID").toString();
						
						groupUserMap.put("INSA_CODE", groupUserInsaCode);
						groupUserMap.put("USER_NO", groupUserNo);
						groupUserMap.put("AP_NO", ap);
						groupUserMap.put("TARGET_ID", target_id);
						
						dao.insertSKTManagerUser(groupUserMap);
					}
					
					resultMap.put("resultCode", 0);
					resultMap.put("resultMeassage", "중간관리자 지정 성공");
				}else {
					++chk;
					/*resultMap.put("resultCode", -2);
					resultMap.put("resultMeassage", map.get("USER_NAME").toString() + "님은 이미 " + map.get("TEAM_NAME").toString() + "의 중간관리자 입니다.");*/
				}
			}
			resultMap.put("resultMapSize", jsonarry.size());
			resultMap.put("resultValue", chk);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return resultMap;
	}
	
	@Override
	public Map<String, Object> updateSKTManagerGrade(Map<String, Object> map) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			dao.updateSKTManagerGrade(map);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return resultMap;
	}
	
	@Override
	public List<Map<String, Object>> selectPcManagerList(HttpServletRequest request) throws Exception {
		return dao.selectPCManagerList();
	}

	@Override
	public List<Map<String, Object>> selectVersionList(HttpServletRequest request) throws Exception {
		return dao.selectVersionList();
	}
	
	private String replaceParameter(String param) {
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
	public List<Map<String, Object>> apServerList(HttpServletRequest request) throws Exception {
		return dao.apServerList();
	}
	
	@Override
	public List<Map<String, Object>> selectMngrList(HttpServletRequest request) {
		
		List<Map<String, Object>> resulList = new ArrayList<>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		/*String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");*/
		String target_id = request.getParameter("target_id");
		String ap_no = request.getParameter("ap_no");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("target_id", target_id);
		map.put("ap_no", ap_no);
		logger.info(map.toString());
		
		List<Map<String, Object>> targetMngrList = dao.selectMngrList(map);
		
		if(targetMngrList.size() > 0) {
			String SERVICE_MNGR3_NO = (String) targetMngrList.get(0).get("SERVICE_MNGR3_NO");
			String SERVICE_MNGR3_NM = (String) targetMngrList.get(0).get("SERVICE_MNGR3_NM");
			String SERVICE_MNGR3_TEAM = (String) targetMngrList.get(0).get("SERVICE_MNGR3_TEAM");
			
			map.put("NUM", "3");
			map.put("USER_NO", SERVICE_MNGR3_NO);
			map.put("USER_NAME", SERVICE_MNGR3_NM);
			map.put("USER_SOSOK", SERVICE_MNGR3_TEAM);
			resulList.add(map);
			
			String SERVICE_MNGR4_NO = (String) targetMngrList.get(0).get("SERVICE_MNGR4_NO");
			String SERVICE_MNGR4_NM = (String) targetMngrList.get(0).get("SERVICE_MNGR4_NM");
			String SERVICE_MNGR4_TEAM = (String) targetMngrList.get(0).get("SERVICE_MNGR4_TEAM");
			
			map = new HashMap<String, Object>();
			map.put("NUM", "4");
			map.put("USER_NO", SERVICE_MNGR4_NO);
			map.put("USER_NAME", SERVICE_MNGR4_NM);
			map.put("USER_SOSOK", SERVICE_MNGR4_TEAM);
			resulList.add(map);
			
			String SERVICE_MNGR5_NO = (String) targetMngrList.get(0).get("SERVICE_MNGR5_NO");
			String SERVICE_MNGR5_NM = (String) targetMngrList.get(0).get("SERVICE_MNGR5_NM");
			String SERVICE_MNGR5_TEAM = (String) targetMngrList.get(0).get("SERVICE_MNGR5_TEAM");
			
			map = new HashMap<String, Object>();
			map.put("NUM", "5");
			map.put("USER_NO", SERVICE_MNGR5_NO);
			map.put("USER_NAME", SERVICE_MNGR5_NM);
			map.put("USER_SOSOK", SERVICE_MNGR5_TEAM);
			resulList.add(map);
			
		}else {
			for(int i=3 ; i < 6 ; i++) {
				map = new HashMap<String, Object>();
				map.put("NUM", i);
				map.put("USER_NO", null);
				map.put("USER_NAME", null);
				map.put("USER_SOSOK", null);
				resulList.add(map);
			}
		}
		return resulList;
	}

	@Override
	@Transactional
	public List<Map<String, Object>> selectLicenseDetail(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String toDate = request.getParameter("toDate");
		String fromDate = request.getParameter("fromDate");
		
		map.put("toDate", toDate);
		map.put("fromDate", fromDate);
		
		return dao.selectLicenseDetail(map);
	}

	
	@Override
	public List<Map<String, Object>> selectInaccessibleList(HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		String host_name = request.getParameter("host_name");
		String path = request.getParameter("path");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("host_name", host_name);
		map.put("path", path);

		return dao.selectInaccessibleList(map);
	}
	
	@Override
	public List<Map<String, Object>> selectPaaSList(HttpServletRequest request) throws Exception {
		String host_name = request.getParameter("host_name");
		String service_name = request.getParameter("service_name");
		String user_name = request.getParameter("user_name");
		String cs_path = request.getParameter("cs_path");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("host_name", host_name);
		map.put("service_name", service_name);
		map.put("user_name", user_name);
		map.put("cs_path", cs_path);
		
		return dao.selectPaaSList(map);
	}
	
	@Override
	public Map<String, Object> updateChkStatus(HashMap<String, Object> params) throws Exception {
		
		logger.info("regResultList ::: " + params.get("regResultList"));
		logger.info("chkResultList ::: " + params.get("chkResultList"));
		
		ReconUtil reconUtil = new ReconUtil();
		Map<String, Object> httpsResponse = null;
		Map<String, Object> map = new HashMap<String,Object>();
		
		Map<String, Object> resultMap = new HashMap<String,Object>();
		
		int notAddCnt = 0;
		int notDelCnt = 0;
		
//		등록 check List
		List<HashMap<String, String>> regResultList = (List<HashMap<String, String>>)params.get("regResultList");
		
//		확인 check List
		List<HashMap<String, String>> chkResultList = (List<HashMap<String, String>>)params.get("chkResultList");
		
		
		// 등록
		for (HashMap<String, String> rmap : regResultList) {
			
			logger.info("==================");
			logger.info( rmap.get("ap_no").getClass().getName());
			try {
			String target_id = rmap.get("target_id");
			String agent_id = rmap.get("agent_id");
			String path = rmap.get("path");
//			int ap_no = rmap.get("ap_no");
			int ap_no = Integer.parseInt(rmap.get("ap_no"));
			
			
			map = new HashMap<String,Object>();
			
			String location_id = null;
			
				
				Properties properties = new Properties();
				String resource = "/property/config.properties";
				Reader reader = Resources.getResourceAsReader(resource);
				properties.load(reader);
				
				this.recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no+1)) ;
				this.recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no+1)) ;
				this.recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no+1)) ;
				this.api_ver = properties.getProperty("recon.api.version");
				
				if (rmap.get("rowStatus").equals("Y")) { // 신규 등록
				    JsonObject jObject = new JsonObject();
				    jObject.addProperty("protocol", "mount");
				    jObject.addProperty("proxy_id", agent_id);
				    jObject.addProperty("path", path);

				    String data = jObject.toString();

				    httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + this.api_ver + "/targets/" + target_id + "/locations", "POST", data);

				    String HttpsResponseDataMessage = "";
				    try {
				        HttpsResponseDataMessage = httpsResponse.get("HttpsResponseDataMessage").toString();
				    } catch (NullPointerException e) {
				        logger.error(e.toString());
				    }
				    logger.info("HttpsResponseDataMessage : " + HttpsResponseDataMessage);

				    int resultCode = Integer.parseInt(httpsResponse.get("HttpsResponseCode").toString());
				    logger.info("resultCode : " + resultCode);

				    if (resultCode == 201) {
				        JsonObject resultObject = new Gson().fromJson(HttpsResponseDataMessage, JsonObject.class);
				        logger.info("getMatchObjects jsonObject : " + resultObject);

				        location_id = resultObject.get("id").getAsString();

				        map.put("location_id", location_id);
				        map.put("path", path);
				        map.put("target_id", target_id);
				        map.put("agent_id", agent_id);
				        map.put("reg_status", "Y");
				        map.put("chk_status", "Y");
				    } else if (resultCode == 404) {
				        ++notAddCnt;

				        map.put("path", path);
				        map.put("target_id", target_id);
				        map.put("agent_id", agent_id);
				        map.put("chk_status", "Y");
				    }
				} else { // 삭제
				    location_id = rmap.get("location_id");

				    httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + this.api_ver + "/targets/" + target_id + "/locations/" + location_id, "DELETE", null);

				    int resultCode = Integer.parseInt(httpsResponse.get("HttpsResponseCode").toString());
				    logger.info("resultCode : " + resultCode);

				    if (resultCode == 204) {
				        map.put("location_id", location_id);
				        map.put("path", path);
				        map.put("target_id", target_id);
				        map.put("agent_id", agent_id);
				        map.put("reg_status", "N");
				    } else {
				        ++notDelCnt;
				    }
				}
				if(map.size() > 0)	dao.updateChkStatus(map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// 확인 여부
		for (HashMap<String, String> cmap : chkResultList) {
			
			String target_id = cmap.get("target_id");
			String agent_id = cmap.get("agent_id");
			String path = cmap.get("path");
			String status = cmap.get("rowStatus");
			int ap_no = Integer.parseInt(cmap.get("ap_no"));
			
			try {
				
				map = new HashMap<String,Object>();
				map.put("path", path);
				map.put("target_id", target_id);
				map.put("agent_id", agent_id);
				map.put("chk_status", status);
				if(map.size() > 0)	dao.updateChkStatus(map);
				
			} catch (Exception e) {
				e.printStackTrace();
				
				map = new HashMap<String,Object>();
				map.put("path", path);
				map.put("target_id", target_id);
				map.put("agent_id", agent_id);
				map.put("chk_status", status.equals("Y") ? "N" : "Y");
				if(map.size() > 0)	dao.updateChkStatus(map);
			}

		}
		
//		String id = request.getParameter("id");
//		String path = request.getParameter("path");
//		String value = request.getParameter("value");
//		
		
//		map.put("id", id);
//		map.put("path", path);
//		map.put("value", value);
		
//		dao.updateChkStatus(map);
		
		resultMap.put("totalSize_reg", regResultList);
		resultMap.put("totalSize_reg", chkResultList);
		resultMap.put("notDelCnt", notDelCnt);
		resultMap.put("notAddCnt", notAddCnt);
		
		
		return resultMap;
	}

	@Override
	public List<Map<Object, Object>> selectMngrNameList() throws Exception {
		return dao.selectMngrNameList();
	}
	
	@Override
	public void updateCS_Path_Mngr(HttpServletRequest request) throws Exception {
		
		String user_no = request.getParameter("user_no");
		String uid = request.getParameter("uid");
		String host_name = request.getParameter("host_name");
		String cs_path = request.getParameter("cs_path");
		String container_id = request.getParameter("container_id");
		    
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_no", user_no);
		map.put("uid", uid);
		map.put("host_name", host_name);
		map.put("cs_path", cs_path);
		map.put("container_id", container_id);
		
		try {
			dao.updateCS_Path_Mngr(map);
			dao.updateFind_Mngr(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public List<Map<String, Object>> selectGroupManagerList(HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		String insa_code = request.getParameter("insa_code");
		String team_name = request.getParameter("team_name");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isna_code", insa_code);
		map.put("team_name", team_name);

		return dao.selectAddGroupManagerList(map);
	}

	@Override
	public Map<String, Object> insertServiceTarget(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("resultCode", -1);
		resultMap.put("resultMessage", "FAILED");
		
		try {
			String insa_code = request.getParameter("insa_code").toString();
			String serverStr = request.getParameter("serverList");
			
			Map<String, Object> serviceMap = new HashMap<>();
			serviceMap.put("insa_code",insa_code);
			logger.info("serverStr :: "+serverStr);
			
			JsonArray serverArr = new JsonArray();
			serverArr = new Gson().fromJson(serverStr, JsonArray.class);

			dao.deleteServiceUser(serviceMap);
			for (int i = 0; i < serverArr.size(); i++) {
			    int idx = Integer.parseInt(serverArr.get(i).getAsString());
			    serviceMap.put("idx", idx);
			    dao.insertServiecUser(serviceMap);
			}
			
		} catch (Exception e) {
			logger.info("error :: "+e.getMessage());
			e.printStackTrace();
		}
		
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> insertExcelTargetUserList(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		List<HashMap<String, Object>> userList = null;
		
		String resulList = request.getParameter("resulList");
		JsonArray jsonarry = JsonParser.parseString(resulList).getAsJsonArray();

		int ap_no = 0;  
		try {
		    Map<String, Object> map = new HashMap<>();
		    Map<String, Object> userMap = new HashMap<>();
		    for (int i = 0; i < jsonarry.size(); i++) {
		    	try {
		    		List<Map<String, Object>> finalMngrList = new ArrayList<>();
		    		JsonObject jsonObject = jsonarry.get(i).getAsJsonObject();
		    		String host_name = jsonObject.get("host_name").getAsString();
		    		JsonArray mngrNameList = jsonObject.get("mngrNameList").getAsJsonArray();
		    		Map<String, Object> targetList = dao.selectMngrTargetId(host_name); 
		    		for (int j = 0; j < mngrNameList.size(); j++) {
		    			JsonObject mngrObject = mngrNameList.get(j).getAsJsonObject();
		    			for (String key : mngrObject.keySet()) {
		    				String value = mngrObject.get(key).getAsString();
		    				String[] mngrUser = value.split(",");
		    				for (String user : mngrUser) {
		    					Map<String, Object> finalMngr = new HashMap<>();
		    					finalMngr.put("user_no", user);
		    					finalMngr.put("userFlag", key);
		    					
		    					Map<String, Object> userInfo = pop_dao.selectUser(finalMngr);
		    					String userName = "";
		                        if (userInfo != null && userInfo.get("USER_NAME") != null) {
		                            userName = userInfo.get("USER_NAME").toString();
		                        } else {
		                            break;
		                        }
		                        finalMngr.put("user_name", userName);
		                        finalMngrList.add(finalMngr);
							}
		    				
		    				if (!finalMngrList.isEmpty()) {
		    		            Map<String, Object> insertParam = new HashMap<>();
		    		            insertParam.put("target_id", targetList.get("target_id"));
		    		            insertParam.put("ap_no", targetList.get("ap_no"));
		    		            insertParam.put("mngrList", finalMngrList);
		    		            pop_dao.updateTargetUser(insertParam);
		    		        }
		    			}
		    		}  
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			resultMap.put("resultCode", 0);
			resultMap.put("resultMeassage", "담당자 일괄 등록 성공");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("resultCode", -1);
			resultMap.put("resultMeassage", "담당자 일괄 등록 실패");
		}
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> insertExcelPathAccountUser(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		List<HashMap<String, Object>> userList = null;
		
		String resulList = request.getParameter("resulList");
		JsonArray jsonarry = JsonParser.parseString(resulList).getAsJsonArray();
		
		int ap_no = 0;  
		try {
			Map<String, Object> map = new HashMap<>();
			Map<String, Object> userMap = new HashMap<>();
			
			logger.info("jsonarry :::::  " + jsonarry);  
			for (int i = 0; i < jsonarry.size(); i++) {
				try {
					List<Map<String, Object>> finalMngrList = new ArrayList<>();
					JsonObject jsonObject = jsonarry.get(i).getAsJsonObject();
					String hostName = jsonObject.get("hostName").getAsString();
					String hashId = jsonObject.get("hashId").getAsString();
					String mngrKey = jsonObject.get("mngrKey").getAsString();
					String userNo = jsonObject.get("userNo").getAsString();
					
					Map<String, Object> findMap = new HashMap<>();
					findMap.put("hostName", hostName);
					findMap.put("hashId", hashId);
					
					Map<String, Object> targetList = dao.selectPathTargetId(findMap); 
					
					Map<String, Object> finalMngr = new HashMap<>();
					finalMngr.put("user_no", userNo);
					finalMngr.put("userFlag", mngrKey);
					
					Map<String, Object> userInfo = pop_dao.selectUser(finalMngr);
					
					String userName = "";
					if (userInfo != null && userInfo.get("USER_NAME") != null) {
						userName = userInfo.get("USER_NAME").toString();
						
						finalMngr.put("user_name", userName);
						finalMngrList.add(finalMngr);
						
						//서버 담당자가 아닌 사용자가 있을 경우, 서버 담당자 매핑 추가 
						if (!finalMngrList.isEmpty()) {
							Map<String, Object> insertParam = new HashMap<>();
							insertParam.put("target_id", targetList.get("target_id"));
							insertParam.put("ap_no", targetList.get("ap_no"));
							insertParam.put("mngrList", finalMngrList);
							pop_dao.updateTargetUser(insertParam);
							
							insertParam.put("userNo", userNo);
							insertParam.put("hashId", hashId);
							dao.updatePathAccountUser(insertParam);
							
						}
					} else {
						break;
					}
					logger.info("finalMngrList ::: " + finalMngrList);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			resultMap.put("resultCode", 0);
			resultMap.put("resultMeassage", "담당자 일괄 등록 성공");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("resultCode", -1);
			resultMap.put("resultMeassage", "담당자 일괄 등록 실패");
		}
		
		return resultMap;
	}
	
	
}

