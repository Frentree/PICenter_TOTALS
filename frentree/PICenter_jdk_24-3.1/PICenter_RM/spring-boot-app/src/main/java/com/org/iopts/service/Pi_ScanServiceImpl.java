package com.org.iopts.service;

import java.io.Reader;
import java.net.ProtocolException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.org.iopts.dao.Pi_ScanDAO;
import com.org.iopts.dao.Pi_UserDAO;
import com.org.iopts.dto.Pi_Scan_HostVO;
import com.org.iopts.dto.Pi_ScheduleVO;
import com.org.iopts.dto.co.ScheduleCo;
import com.org.iopts.util.ReconUtil;
import com.org.iopts.util.ServletUtil;
import com.org.iopts.util.SessionUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class Pi_ScanServiceImpl implements Pi_ScanService {
	
	private static Logger logger = LoggerFactory.getLogger(Pi_ScanServiceImpl.class);

	@Value("${recon.id}")
	private String recon_id;

	@Value("${recon.password}")
	private String recon_password;

	@Value("${recon.url}")
	private String recon_url;
	
	@Value("${recon.api.version}")
	private String api_ver;
	
	@Inject
	private Pi_ScanDAO dao;
	
	@Inject
	private Pi_UserDAO userDao;
	

	@Override
	public List<Map<String, Object>> selectSchedules(HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub

		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String searchType = request.getParameter("searchType");
		String hostName = request.getParameter("hostName");
		String aut = request.getParameter("aut");
		JsonArray searchArray = new Gson().toJsonTree(searchType).getAsJsonArray();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		if(fromDate == null) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
			fromDate = sdf.format(cal.getTime());
		}
		if(toDate == null) {
			Calendar cal = Calendar.getInstance();
			toDate = sdf.format(cal.getTime());
		}
		
		Map<String, Object> search = new HashMap<String,Object>();
		search.put("fromDate", fromDate);
		search.put("toDate", toDate);
		search.put("hostName", hostName);
		search.put("searchType", searchArray);
		
		logger.info("aut :: " + aut);
		if("user".equals(aut)){
			String user_no = SessionUtil.getSession("memberSession", "USER_NO");
			
			search.put("user_no", user_no);
		}
		
		logger.info(search.toString());
		
		return dao.selectSchedules(search);
	}

	@Override
	public List<Pi_ScheduleVO> selectSchedule(String schedule_status) throws Exception {
		// TODO Auto-generated method stub
		return dao.selectSchedule(schedule_status);
	}

	@Override
	public List<Pi_Scan_HostVO> selectScanHost() throws Exception {
		// TODO Auto-generated method stub
		return dao.selectScanHost();
	}

	@Override
	public Map<String, Object> changeSchedule(HttpServletRequest request, String recon_id, String recon_password, String recon_url, String api_ver) throws Exception {

		//https://172.30.1.58:8339/beta/schedules/98/Action
		String id = request.getParameter("id");
		String task = request.getParameter("task");
		logger.info("getMatchObjects doc : " + "/" + api_ver + "/schedules/" + id + "/" + task);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		ReconUtil reconUtil = new ReconUtil();
		Map<String, Object> httpsResponse = null;
		try {
			httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + api_ver + "/schedules/" + id + "/" + task, "POST", null);
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			resultMap.put("resultCode", -1);
			logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return resultMap;
		}

		int resultCode = (int) httpsResponse.get("HttpsResponseCode");
		String resultMessage = (String) httpsResponse.get("HttpsResponseMessage");
		if ((resultCode != 200) && (resultCode != 204)) {
			resultMap.put("resultCode", resultCode);
			resultMap.put("resultMessage", resultMessage);
			return resultMap;
		}
		// 작업변경이 성공하면 DB도 변경 해 준다.
     	String changedTask = "scheduled";
     	switch (task) {
     	case "deactivate" :
     		changedTask = "deactivated";
     		break;
     	case "skip" :
     		changedTask = "scheduled";
     		break;
     	case "pause" :
     		changedTask = "pause";
     		break;
     	case "restart" :
     		changedTask = "scheduled";
     		break;
     	case "stop" :
     		changedTask = "stopped";
     		break;
     	case "cancel" :
     		changedTask = "cancelled";
     		break;
     	case "reactivate" :
     		changedTask = "scheduled";
     		break;
 		default :
     		changedTask = "scheduled";
 		break;
     	}
		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("SCHEDULE_ID", id);
		inputMap.put("SCHEDULE_STATUS", changedTask);
  	 	dao.changeSchedule(inputMap);
  	 	
  	 	Map<String, Object> schedulesMap = dao.selectScheduleById(inputMap);
  	
		resultMap.put("resultCode", 0);
		resultMap.put("resultMessage", "SUCCESS");
		
		// User Log 남기기
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		Map<String, Object> userLog = new HashMap<String, Object>();
		
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "SCHEDULE CHANGE");		
		userLog.put("user_ip", clientIP);
		userLog.put("job_info", "스캔 상태 변경 > " + schedulesMap.get("schedule_label") + "[" + changedTask + "]");
		userLog.put("logFlag", "1");

		userDao.insertLog(userLog);
		
		return resultMap;
	}

	@Override
	public Map<String, Object> viewSchedule(HttpServletRequest request, String recon_id, String recon_password, String recon_url, String api_ver) throws Exception {

		//https://masterIP:8339/beta/schedules/98?details=true
		String id = request.getParameter("id");
		logger.info("getMatchObjects doc : " + "/" + api_ver + "/schedules/" + id + "?details=true");

		Map<String, Object> resultMap = new HashMap<String, Object>();
		ReconUtil reconUtil = new ReconUtil();
		Map<String, Object> httpsResponse = null;
		try {
			httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + api_ver + "/schedules/" + id + "?details=true", "GET", null);
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			resultMap.put("resultCode", -1);
			logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return resultMap;
		}

		int resultCode = (int) httpsResponse.get("HttpsResponseCode");
		String resultMessage = (String) httpsResponse.get("HttpsResponseMessage");
		if (resultCode != 200) {
			resultMap.put("resultCode", resultCode);
			resultMap.put("resultMessage", resultMessage);
			return resultMap;
		}

		JsonObject jsonObject = new Gson().fromJson(httpsResponse.get("HttpsResponseData").toString(), JsonObject.class);

		ArrayList<String> datatypeList = new ArrayList<>();
		JsonArray datatypeJson = jsonObject.getAsJsonArray("profiles");
		if (datatypeJson != null) {
		    for (int i = 0; i < datatypeJson.size(); i++) {
		        datatypeList.add(datatypeJson.get(i).getAsString());
		    }
		    Map<String, Object> datatypeMap = new HashMap<>();
		    datatypeMap.put("DATATYPE", datatypeList);

		    List<Map<String, Object>> datatypeLabelList = dao.selectDataTypes(datatypeMap);
		    jsonObject.add("profilesLabel", new Gson().toJsonTree(datatypeLabelList));
		}

		Long next_scan = jsonObject.get("next_scan").getAsLong();
		java.util.Date time = new java.util.Date(next_scan * 1000);
		jsonObject.addProperty("next_scanDate", time.toString());
		
		resultMap.put("resultData", jsonObject);
		resultMap.put("resultCode", 0);
		resultMap.put("resultMessage", "SUCCESS");
		return resultMap;
	}
	
	@Override
	public List<Map<String, Object>> selectLocationList() throws Exception {
		
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		Map<String, Object> map = new HashMap<String, Object>();		
		
		if (user_grade.equals("0")) { // 일반 사용자인 경우
			map.put("user_no", user_no);
		}
		else {
			map.put("user_no", "");
		}
		
		return dao.selectLocationList(map);
	}
	
	@Override
	public List<Map<String, Object>> selectDatatypeList() throws Exception {

		
		List<Map<String, Object>> dataTypeArr = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> map = dao.selectDatatypeList();
		
		for (int i = 0; i < map.size(); i++) {
			Map<String, Object> dataTypes = map.get(i);
			
			int version = (int) dataTypes.get("VERSION");
			String label = (String) dataTypes.get("DATATYPE_LABEL");
			int key = ((Double) dataTypes.get("RNUM")).intValue();
			
			List<Map<String, Object>> dataTypeVersions = dao.selectDatatypeVersion(label);
			Map<String, Object> dataTypeMap = new HashMap<String, Object>();

			dataTypeMap.put("KEY", key);
			dataTypeMap.put("DATATYPE_LABEL", label);
			dataTypeMap.put("VERSION", version);
			dataTypeMap.put("DATATYPE_ID", dataTypeVersions);
			
			dataTypeArr.add(dataTypeMap);
		}
		ReconUtil reconUtil = new ReconUtil();

		return dataTypeArr;
	}
	
	@Override
	public List<Map<String, Object>> selectDatatypeListMod(HttpServletRequest request) throws Exception {

		
		List<Map<String, Object>> dataTypeArr = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> map = dao.selectDatatypeList();
		List<Map<String, Object>> policy = this.viewScanPolicy(request);

		String sDatatypeId = (String) policy.get(0).get("datatype_id");
		if (sDatatypeId == null) {
			return null;
		}
		String[] aDatatypeId = sDatatypeId.split(",");

		for (int i = 0; i < map.size(); i++) 
		{
			Map<String, Object> dataTypes = map.get(i);

			int version = (int) dataTypes.get("VERSION");
			String label = (String) dataTypes.get("DATATYPE_LABEL");
			int key = ((Double) dataTypes.get("RNUM")).intValue();
			
			List<Map<String, Object>> dataTypeVersions = dao.selectDatatypeVersion(label);
			Map<String, Object> dataTypeMap = new HashMap<String, Object>();

			dataTypeMap.put("KEY", key);
			dataTypeMap.put("DATATYPE_LABEL", label);
			dataTypeMap.put("VERSION", version);
			dataTypeMap.put("DATATYPE_ID", dataTypeVersions);

			loopOut:
			for (int j = 0; j < aDatatypeId.length; j++) {
				for (int k = 0; k < dataTypeVersions.size(); k++)
				{
					if (aDatatypeId[j].equals(dataTypeVersions.get(k).get("DATATYPE_ID"))) {
						dataTypeMap.put("CHECKED", 1);
						break loopOut;
					}
				}
			}
			dataTypeArr.add(dataTypeMap);
		}

		return dataTypeArr;
	}

	@Override
	public Map<String, Object> registSchedule(HttpServletRequest request, String api_ver) throws Exception {

		logger.info("getMatchObjects doc : " + "/" + api_ver + "/schedules");
//		String scheduleData = request.getParameter("scheduleData");
		
		ReconUtil reconUtil = new ReconUtil();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Properties properties = new Properties();
		String resource = "/property/config.properties";
		Reader reader = Resources.getResourceAsReader(resource);
		
		properties.load(reader);
		
		JsonParser parser = new JsonParser();
		JsonArray dataArr = (JsonArray) parser.parse(request.getParameter("scheduleArr"));
		logger.info("size :: " + dataArr.size());
		for(int i=0; i<dataArr.size(); i++) {
			/*logger.info(dataArr.get(i).toString());*/
			
			JsonObject obj = (JsonObject) dataArr.get(i);
			int ap_no = Integer.parseInt(obj.get("ap_no").toString());
			String scheduleData = obj.get("scheduleData").toString();
			
			String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no+1)) ;
			String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no+1)) ;
			String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no+1)) ;
			
			Map<String, Object> httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url+"/"+api_ver+"/schedules", "POST", scheduleData);
			
			int resultCode = (int) httpsResponse.get("HttpsResponseCode");
			String resultMessage = (String) httpsResponse.get("HttpsResponseMessage");
			
			resultMap.put("resultCode", resultCode);
			resultMap.put("resultMessage", resultMessage);
			
			// User Log 남기기
			String user_no = SessionUtil.getSession("memberSession", "USER_NO");
			ServletUtil servletUtil = new ServletUtil(request);
			String clientIP = servletUtil.getIp();
			Map<String, Object> userLog = new HashMap<String, Object>();
			
			userLog.put("user_no", user_no);
			userLog.put("menu_name", "SCHEDULE REGIST");		
			userLog.put("user_ip", clientIP);
			//userLog.put("job_info", "스캔 등록");
			userLog.put("logFlag", "1");
			
			if(resultCode == 201) {
				userLog.put("job_info", "스캔 등록 성공");
				
				// 스캔 등록 성공 시 스케줄 리스트 업데이트
				updateScanschedule(reconUtil);
			} else if (resultCode == 409) {
				userLog.put("job_info", "스캔 등록 실패 - 스케줄명 중복");
			} else if (resultCode == 422) {
				userLog.put("job_info", "스캔 등록 실패 - 시작시간 오류");
			} else {
				userLog.put("job_info", "스캔 등록 실패 - " + resultCode);
			}
			
			userDao.insertLog(userLog);
		}
		
		return resultMap;
	}

	private void updateScanschedule(ReconUtil reconUtil) {
		logger.info("updateScanSchedule");
		String start_date = "";
		String end_date = "";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		start_date = sdf.format(cal.getTime());
		end_date = sdf.format(new Date());	
		
		
		try {
			String url =  String.format("%s/%s/schedules?details=true&completed=true&cancelled=true&stopped=true&failed=true&deactivated=true&pending=true&start_date=%s&limit=5000000&end_date=%s'",
					recon_url, api_ver,start_date, end_date);
			logger.info("url :: " + url);
			
			Map<String, Object> httpsResponse = reconUtil.getServerData(recon_id, recon_password, url, "GET", "");
			
			JsonParser parser = new JsonParser();
			
			JsonArray arr = (JsonArray) parser.parse(httpsResponse.get("HttpsResponseData").toString());
			
			for(int i=0; i<arr.size(); i++) {
				JsonObject obj = (JsonObject) arr.get(i);
				
				Map<String, Object> map = new HashMap<>();
				map.put("id", obj.get("id").toString().replaceAll("\"", ""));
				map.put("label", obj.get("label").toString().replaceAll("\"", ""));
				map.put("status", obj.get("status").toString().replaceAll("\"", ""));
				map.put("repeat_days", obj.get("repeat_days").toString().replaceAll("\"", ""));
				map.put("repeat_months", obj.get("repeat_months").toString().replaceAll("\"", ""));
				map.put("next_scan", obj.get("next_scan").toString().replaceAll("\"", ""));
				map.put("cpu", obj.get("cpu").toString().replaceAll("\"", ""));
				map.put("capture", obj.get("capture").toString().replaceAll("\"", ""));
				map.put("trace", obj.get("trace").toString().replaceAll("\"", ""));
				
				JsonArray pro_arr = (JsonArray) obj.get("profiles");
				String profiles = "";
				for(int pi=0; pi<pro_arr.size(); pi++) {
					profiles += pro_arr.get(pi).toString().replaceAll("\"", "");
					
					if(pi < (pro_arr.size()-1)) {
						profiles += ",";
					}
				}
				map.put("profiles", profiles);
				
				logger.info(obj.toString());
				logger.info("obj test :: " + obj.has("pause"));
				if(obj.has("pause")) {
					JsonArray pause_arr = (JsonArray) obj.get("pause");
					for(int pausei=0; pausei<pause_arr.size(); pausei++) {
						JsonObject pause_obj = (JsonObject) pause_arr.get(pausei);
						//{"days":62,"from":28800,"to":72000}
						
						map.put("pause_days", pause_obj.get("days").toString().replaceAll("\"", ""));
						map.put("pause_from", pause_obj.get("from").toString().replaceAll("\"", ""));
						map.put("pause_to", pause_obj.get("to").toString().replaceAll("\"", ""));
					}
				}
				
				JsonArray t_arr = (JsonArray) obj.get("targets");
				String target_id = "";
				String target_name = "";
				for(int ti=0; ti<t_arr.size(); ti++) {
					JsonObject t_obj = (JsonObject) t_arr.get(ti);
					target_id += t_obj.get("id").toString().replaceAll("\"", "");
					target_name += t_obj.get("name").toString().replaceAll("\"", "");
					if(ti < (t_arr.size()-1)) {
						target_id += ",";
						target_name += ",";
					}
				}
				map.put("target_id", target_id);
				map.put("target_name", target_name);

				logger.info("map :: " + map.toString());
				dao.updateScanSchedule(map);
			}
			
			logger.info(httpsResponse.toString());
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, Object> getProfileDetail(HttpServletRequest request, String recon_id, String recon_password, String recon_url, String api_ver) throws Exception {
		//https://172.30.1.58:8339/beta/datatypes/profiles/13973816057316668091?details=true
		
		String id = request.getParameter("datatypeId");
		logger.info("getMatchObjects doc : " + "/" + api_ver + "/datatypes/profiles/" + id + "?details=true");

		Map<String, Object> resultMap = new HashMap<String, Object>();
		ReconUtil reconUtil = new ReconUtil();
		Map<String, Object> httpsResponse = null;
		try {
			httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + api_ver + "/datatypes/profiles/" + id + "?details=true", "GET", null);
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			resultMap.put("resultCode", -1);
			logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return resultMap;
		}

		int resultCode = (int) httpsResponse.get("HttpsResponseCode");
		String resultMessage = (String) httpsResponse.get("HttpsResponseMessage");
		if (resultCode != 200) {
			resultMap.put("resultCode", resultCode);
			resultMap.put("resultMessage", resultMessage);
			return resultMap;
		}

		JsonObject jsonObject = new Gson().fromJson(httpsResponse.get("HttpsResponseData").toString(), JsonObject.class);
		
		resultMap.put("resultData", jsonObject);
		resultMap.put("resultCode", 0);
		resultMap.put("resultMessage", "SUCCESS");
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> viewScanHistory(HttpServletRequest request) {
		String target_id = request.getParameter("target_id");
		
		Map<String, Object> historyList = new HashMap<String,Object>();
		historyList.put("target_id", target_id);
		
		return dao.viewScanHistory(historyList);
	}

	@Override
	public List<Map<String, Object>> viewScanPolicy(HttpServletRequest request) {
		String idx = request.getParameter("idx");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idx", idx);
		
		return dao.viewScanPolicy(map);
	}

	@Override
	public Map<String, Object> registPolicy(HttpServletRequest request) throws Exception {
		
		String policy_name = request.getParameter("policy_name");
		String policy_version = request.getParameter("policy_version");
		String comment = request.getParameter("comment");
		String datatype = request.getParameter("datatype");
		String label = request.getParameter("label");
		
		String pauseDays = request.getParameter("pauseDays");
		String pauseMonth = request.getParameter("pauseMonth");
		String pauseFrom = request.getParameter("pauseFrom");
		String pauseTo = request.getParameter("pauseTo");
		
		String cpu = request.getParameter("cpu");
		String data = request.getParameter("data");
		String memory = request.getParameter("memory");
		
		String trace = request.getParameter("trace");
		String dmz = request.getParameter("dmz");
		String check = request.getParameter("default_check");
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("datatype", datatype);
		map.put("policy_name", policy_name);
		map.put("policy_version", policy_version);	
		
		map.put("comment", comment);
		map.put("schedule_label", label);
		
		map.put("schedule_pause_days", pauseDays);
		map.put("schedule_pause_month", pauseMonth);
		map.put("schedule_pause_from", pauseFrom);
		map.put("schedule_pause_to", pauseTo);
		
		map.put("schedule_cpu", cpu);
		map.put("schedule_data", data);
		map.put("schedule_memory", memory);
		
		map.put("schedule_trace", trace);
		map.put("dmz", dmz);
		map.put("check", check);
		
		logger.info("map check : " + map);
		
		dao.registPolicy(map);
		
		// User Log 남기기
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		Map<String, Object> userLog = new HashMap<String, Object>();

		userLog.put("user_no", user_no);
		userLog.put("menu_name", "정책 등록");		
		userLog.put("user_ip", clientIP);
		userLog.put("job_info", "정책 등록 - " + policy_name);
		userLog.put("logFlag", "3");

		userDao.insertLog(userLog);
		
		return map;
		
	}
	
	@Override
	public void updatePolicy(HttpServletRequest request) {
		
		String idx = request.getParameter("idx");
		
		String policy_name = request.getParameter("policy_name");
		String policy_version = request.getParameter("policy_version");
		String comment = request.getParameter("comment");
		String datatype = request.getParameter("datatype");
		String label = request.getParameter("label");
		
		String pauseDays = request.getParameter("pauseDays");
		String pauseMonth = request.getParameter("pauseMonth");
		String pauseFrom = request.getParameter("pauseFrom");
		String pauseTo = request.getParameter("pauseTo");
		
		String cpu = request.getParameter("cpu");
		String data = request.getParameter("data");
		String memory = request.getParameter("memory");
		
		String trace = request.getParameter("trace");
		String dmz = request.getParameter("dmz");
		String check = request.getParameter("default_check");
		//String pause = request.getParameter("pause");
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idx", idx);
		
		map.put("datatype", datatype);
		map.put("policy_name", policy_name);
		map.put("policy_version", policy_version);	
		
		map.put("comment", comment);
		map.put("schedule_label", label);
		
		map.put("schedule_pause_days", pauseDays);
		map.put("schedule_pause_month", pauseMonth);
		map.put("schedule_pause_from", pauseFrom);
		map.put("schedule_pause_to", pauseTo);
		
		map.put("schedule_cpu", cpu);
		map.put("schedule_data", data);
		map.put("schedule_memory", memory);
		
		map.put("schedule_trace", trace);
		map.put("dmz", dmz);
		map.put("check", check);
		//map.put("pause", check);
		
		
		logger.info("map check : " + map);
		
		dao.updatePolicy(map);
		
		// User Log 남기기
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		Map<String, Object> userLog = new HashMap<String, Object>();

		userLog.put("user_no", user_no);
		userLog.put("menu_name", "정책 변경");		
		userLog.put("user_ip", clientIP);
		userLog.put("job_info", "정책 변경 - " + policy_name);
		userLog.put("logFlag", "3");
		
		try {
			userDao.insertLog(userLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public List<Map<String, Object>> getProfile(HttpServletRequest request) {
		logger.info("getProfile Service!");
		String datatype_id = request.getParameter("datatype_id");
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("datatype_id", datatype_id);
		
		return dao.getProfile(map);
	}
	
	@Override
	public void deleteProfile(HttpServletRequest request) throws Exception {
		logger.info("resetDefaultPolicy request : " + request);
		
		// 로그 기록
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String password = request.getParameter("password");
		
		// User Log 남기기
		Map<String, Object> userLog = new HashMap<String, Object>();
				
		String datatype_id = request.getParameter("datatype_id");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("DATATYPE_ID", datatype_id);
		
		Map<String, Object> dataTypeMap = dao.selectDataTypeById(map);
		
		dao.deleteProfile(map);
		
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "DATATYPE DELETE");		
		userLog.put("user_ip", clientIP);
		userLog.put("job_info", "개인정보 유형 삭제 - " + dataTypeMap.get("datatype_label"));
		userLog.put("logFlag", "2");
		
		userDao.insertLog(userLog);
	}
	
	@Override
	public void resetDefaultPolicy(HttpServletRequest request) {
		logger.info("resetDefaultPolicy request : " + request);
		dao.resetDefaultPolicy(request);
	}

	@Override
	public void updateDefaultPolicy(HttpServletRequest request) {
		logger.info("updateDefaultPolicy");
		String idx = request.getParameter("idx");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idx", idx);
		dao.updateDefaultPolicy(map);
		
		// User Log 남기기
		Map<String, Object> userLog = new HashMap<String, Object>();
				
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "기본 정책 변경");		
		userLog.put("user_ip", clientIP);
		userLog.put("job_info", "기본 정책 변경 - " + idx);
		userLog.put("logFlag", "3");
		
		try {
			userDao.insertLog(userLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, Object> manageSchedule(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		
		Map<String, Object> memberMap = (Map<String, Object>) request.getSession().getAttribute("memberSession");
		
		logger.info("session : " + memberMap.toString());
		logger.info("user_no : " + memberMap.get("USER_NO").toString());
		
		String[] arr_week = {"mon", "tue", "wed", "thu", "fri", "sat", "sun"};
		
		String checkedKey = request.getParameter("checkedKey");
		String schedule_group = request.getParameter("schedule_id");
		String schedule_group_name = request.getParameter("schedule_name");
		String schedule_id = "";
		String ap_no = "";
		String stop_date = request.getParameter("stopDate");
		String stop_time = request.getParameter("stopTime");
		String active_code = "01";
		String stop_dtm = stop_date.replaceAll("-", "") + stop_time.replaceAll("\\:", "")+"00";
		
//		ACTIVE_STATUS
		logger.info("schedule_id >>>> " + schedule_group);
		
		List<Map<String, Object>> scheduleList = dao.selectScheduleGroupTargets(schedule_group);
		
		logger.info("scheduleList :::: " + scheduleList);
		Map<String, Object> schedulesMap = new HashMap<>();
		try {
			for (Map<String, Object> val : scheduleList) {
				Map<String, Object> selectMap = new HashMap<>();
				
				logger.info("data" + val.get("RECON_SCHEDULE_ID").toString() +  ",  " + val.get("AP_NO").toString());
				schedule_id = val.get("RECON_SCHEDULE_ID").toString();
				ap_no = val.get("AP_NO").toString();
				
				selectMap.put("schedule_id", schedule_id);
				selectMap.put("ap_no", ap_no);
				
				String schedule_status = dao.selectScheduleStatus(selectMap);
//			schedule_status = "paused";
				
				active_code = "00";
				if("scheduled".equals(schedule_status)) {
//				active_code = "02";
				} else if("scanning".equals(schedule_status) || "paused".equals(schedule_status)) {
					
					String current_key = new SimpleDateFormat("EEE", Locale.ENGLISH).format(new Date()).toLowerCase() + "_" + new SimpleDateFormat("HH", Locale.ENGLISH).format(new Date());
					
					if("scanning".equals(schedule_status) && !checkedKey.contains(current_key)) {
						logger.info("change Schedule from scanning to paused");
						logger.info("schedule paused connection result[" + changeScanSchedule(schedule_id, "pause") + "]");
					} else if ("paused".equals(schedule_status) && checkedKey.contains(current_key)) {
						logger.info("change Schedule from paused to scanning");
						changeScanSchedule(schedule_id, "resume");
					}
				}
				
				String[] arr_key = checkedKey.split(",");
				
				Arrays.sort(arr_key);
				
				ArrayList<Map<String, String>> schedule_data = new ArrayList<>();
				if(arr_key.length > 0) {
					for(int i=0; i<arr_key.length; i++) {
						
						String resume_data = arr_key[i];
						String[] split_data = resume_data.split("_");
						String weekday = split_data[0];
						String resume_time = split_data[1];
						
						int compare_time = Integer.parseInt(resume_time);
						
						String previous_time = weekday+"_"+String.format("%02d", compare_time-1);
						String next_time = weekday+"_"+String.format("%02d", compare_time+1);
						
						if(compare_time == 0) {
							int arr_week_idx = (Arrays.asList(arr_week).indexOf(weekday)-1 < 0) ? 6 : Arrays.asList(arr_week).indexOf(weekday)-1;
							previous_time = arr_week[arr_week_idx]+"_23";
						} else if (compare_time == 23) {
							int arr_week_idx = (Arrays.asList(arr_week).indexOf(weekday)+1 > 6 ) ? 0 : Arrays.asList(arr_week).indexOf(weekday)+1;
							next_time = arr_week[arr_week_idx]+"_00";
						}
						
						if(checkedKey.indexOf(previous_time) < 0) {
							Map<String, String> map = new HashMap<>();
							map.put("schedule_id", schedule_id);
							map.put("work_cd", "01");
							map.put("weekday", weekday);
							map.put("time", resume_data.substring(resume_data.indexOf("_")+1));
							map.put("active", active_code);
							map.put("userId", memberMap.get("USER_NO").toString());
							map.put("schedule_group", schedule_group);
							
							schedule_data.add(map);
						}
						
						if(checkedKey.indexOf(next_time) < 0) {
							Map<String, String> map = new HashMap<>();
							map.put("schedule_id", schedule_id);
							map.put("work_cd", "02");
							map.put("weekday", next_time.substring(0, next_time.indexOf("_")));
							map.put("time", next_time.substring(next_time.indexOf("_")+1));
							map.put("active", active_code);
							map.put("userId", memberMap.get("USER_NO").toString());
							map.put("schedule_group", schedule_group);
							schedule_data.add(map);
						}
						
					}
					
					Map<String, String> uptData = new HashMap<>();
					uptData.put("schedule_id", schedule_id);
					uptData.put("schedule_group", schedule_group);
					uptData.put("userId", memberMap.get("USER_NO").toString());
					uptData.put("isEmptyStop", "N");
					
					if(stop_dtm.length() == 14) {
						Map<String, String> stopMap = new HashMap<>();
						
						stopMap.put("schedule_id", schedule_id);
						stopMap.put("work_cd", "03");
						stopMap.put("active", active_code);
						stopMap.put("stop_dtm", stop_dtm);
						stopMap.put("userId", memberMap.get("USER_NO").toString());
						stopMap.put("schedule_group", schedule_group);
						schedule_data.add(stopMap);
						
						uptData.put("isEmptyStop", "Y");
					}
					
					dao.changeActiveStatus(uptData);
					dao.insertScanSchedule(schedule_data);
				}
				
				result.put("resultCode", "00");
				result.put("resultMsg", "성공");
				
				Map<String, Object> map = new HashMap<>();
				map.put("SCHEDULE_ID", schedule_id);
				
				// schedulesMap = dao.selectScheduleById(map);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		// User Log 남기기
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		Map<String, Object> userLog = new HashMap<String, Object>();

		userLog.put("user_no", user_no);
		userLog.put("menu_name", "SCAN SCHEDULE UPDATE");		
		userLog.put("user_ip", clientIP);
		userLog.put("job_info", "스캔 일정 변경 - " + schedule_group_name);
		userLog.put("logFlag", "1");

		try {
			userDao.insertLog(userLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	private boolean changeScanSchedule(String schedule_id, String action) {
		boolean result = false;
		try {
			ReconUtil reconUtil = new ReconUtil();
			String dtm = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			
			String url = this.recon_url + "/beta/schedules/" + schedule_id + "/" + action;
			String method = "POST";
			String requestData = "";
			
			Map<String, String> conHistMap = new HashMap<>();
			String seq = String.format("%03d", dao.getConnectHistSeq());
			
			conHistMap.put("id", dtm+seq);
			conHistMap.put("recon_id", this.recon_id);
			conHistMap.put("url", url);
			conHistMap.put("method", method);
			conHistMap.put("req_data", requestData);
			
			if(dao.insConnectHist(conHistMap) > 0) {
				
				Map<String, Object> resultMap = reconUtil.getServerData(this.recon_id, this.recon_password, url, "POST", null);
				conHistMap.put("rsp_cd", resultMap.get("HttpsResponseCode").toString());
				conHistMap.put("rsp_msg", resultMap.get("HttpsResponseMessage").toString());
				
				logger.info("[" + schedule_id + "] "+ action +" UPDATE DB Schedule result : " + dao.uptConnectHist(conHistMap));
				
				logger.info("[" + schedule_id + "] "+ action +" Schedule result : " + resultMap.get("HttpsResponseCode"));
				logger.info("[" + schedule_id + "] "+ action +" Schedule result : " + resultMap.get("HttpsResponseMessage"));
				if("204".equals(resultMap.get("HttpsResponseCode").toString())) {result = true;}
				
			}
			
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public Map<String, Object> getScanSchedule(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		
		String schedule_id = request.getParameter("schedule_id");
		
		logger.info("schedule_id : " + request.getParameter("schedule_id"));
		List<Map<String, String>> scheduleList_DB = new ArrayList<>();
		List<String> scheduleList_SEND = new ArrayList<>();
		
		scheduleList_DB = dao.selectScanSchedule(schedule_id);
		
		String[] arr_week = {"mon", "tue", "wed", "thu", "fri", "sat", "sun"};
		for(int std_i=0; std_i<scheduleList_DB.size(); std_i++) {
			Map<String, String> getMap = scheduleList_DB.get(std_i);
			String work_cd = getMap.get("WORK_CD");
			String weekday = getMap.get("WEEKDAY");
			String work_hour = getMap.get("WORK_HOUR");
			if("01".equals(work_cd)) {
				int hour = Integer.parseInt(work_hour);
				boolean while_flag = true;
				while(while_flag) {
					scheduleList_SEND.add(weekday+"_"+String.format("%02d", hour));
					if(hour == 23) {
						int arr_week_idx = (Arrays.asList(arr_week).indexOf(weekday)+1) % 7;
						weekday = arr_week[arr_week_idx];
						hour = 0;
					} else {
						hour++;
					}
					String next_hour = String.format("%02d", hour); 
					for(int i=0; i<scheduleList_DB.size(); i++) {
						if( "02".equals(scheduleList_DB.get(i).get("WORK_CD")) && 
							weekday.equals(scheduleList_DB.get(i).get("WEEKDAY")) &&
							next_hour.equals(scheduleList_DB.get(i).get("WORK_HOUR"))) 
						{
							while_flag = false;
							break;
						}
					}
				}
				
			} else if ("03".equals(work_cd)) {
				String stop_dtm = getMap.get("STOP_DTM");
				String stop_date = stop_dtm.substring(0, 8);
				result.put("stop_date", stop_date.substring(0,4)+"-"+stop_date.substring(4,6)+"-"+stop_date.substring(6));
				result.put("stop_hour", stop_dtm.substring(8, 10));
				result.put("stop_minute", stop_dtm.substring(10, 12));
			}
			
		}
		
		result.put("resultCode", "0");
		result.put("resultMessage", "Success");
		result.put("resultList", scheduleList_SEND);
		result.put("resultSize", scheduleList_SEND.size());
		
		return result;
	}

	@Override
	public Map<String, Object> deleteSchedule(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		
		String schedule_id = request.getParameter("schedule_id");
		String schedule_name = request.getParameter("schedule_name");
		Map<String, Object> memberMap = (Map<String, Object>) request.getSession().getAttribute("memberSession");
		
		Map<String, String> uptData = new HashMap<>();
		uptData.put("schedule_id", schedule_id);
		uptData.put("userId", memberMap.get("USER_NO").toString());
		uptData.put("isEmptyStop", "Y");
		
		logger.info("uptData :: " + uptData.toString());
		dao.changeActiveStatus2(uptData);
		
		result.put("resultCode", "0");
		result.put("resultMessage", "Success");
		
		Map<String, Object> map = new HashMap<>();
		map.put("SCHEDULE_ID", schedule_id);
		
		// Map<String, Object> schedulesMap = dao.selectScheduleById(map);
		
		// User Log 남기기
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		Map<String, Object> userLog = new HashMap<String, Object>();

		userLog.put("user_no", user_no);
		userLog.put("menu_name", "SCAN SCHEDULE DELETE");		
		userLog.put("user_ip", clientIP);
		userLog.put("job_info", "스캔 일정 삭제 - " + schedule_name);
		userLog.put("logFlag", "1");

		try {
			userDao.insertLog(userLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public Map<String, Object> executeChecked(JsonObject data, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			
			Iterator<String> keys = data.keySet().iterator();
			while(keys.hasNext()) {
				
				String schedule_id = keys.next();
				String status = data.get(schedule_id).toString();
				String action = "";
				//deactivate activate cancel skip pause resume stop restart
				//'completed':'완료','deactivated':'비활성화','cancelled':'취소','stopped':'중지','failed':'실패','scheduled':'예약','paused':'일시정지','scanning':'스캔중'
				switch (status) {
				case "completed":
					break;
				case "deactivated":
					action = "reactivate";
					break;
				case "pending":
					break;
				case "stopped":
					action = "restart";
					break;
				case "cancelled":
					break;
				case "failed":
					action = "restart";
					break;
				case "paused":
					action = "resume";
					break;
				case "scanning":
					action = "pause";
					break;
				case "scheduled":
					action = "deactivate";
					break;
				}
				logger.info("["+schedule_id+"] execute " + status + " ===> " + action);
				
				if(!"".equals(schedule_id) && !"".equals(action)) {
					boolean changeResult = changeScanSchedule(schedule_id, action);
					logger.info("["+schedule_id+"] schedule " + action + " result :: " + changeResult);
					if(changeResult) {
						
						for(int i=0; i<10; i++) {
							
							ReconUtil reconUtil = new ReconUtil();
							
							String dtm = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
							String url = this.recon_url + "/beta/schedules/" + schedule_id + "?details=true&completed=true&cancelled=true&stopped=true&failed=true&deactivated=true";
							String method = "GET";
							String requestData = "";
							
							Map<String, String> conHistMap = new HashMap<>();
							String seq = String.format("%03d", dao.getConnectHistSeq());
							
							conHistMap.put("id", dtm+seq);
							conHistMap.put("recon_id", this.recon_id);
							conHistMap.put("url", url);
							conHistMap.put("method", method);
							conHistMap.put("req_data", requestData);
							
							if(dao.insConnectHist(conHistMap) > 0) {
								
								Map<String, Object> reconResultMap = reconUtil.getServerData(this.recon_id, this.recon_password, url, method, requestData);
								conHistMap.put("rsp_cd", reconResultMap.get("HttpsResponseCode").toString());
								conHistMap.put("rsp_msg", reconResultMap.get("HttpsResponseMessage").toString());
								
								logger.info("[" + schedule_id + "] "+ action +" UPDATE DB Schedule result : " + dao.uptConnectHist(conHistMap));
								
								logger.info("[" + schedule_id + "] "+ action +" Schedule result : " + reconResultMap.get("HttpsResponseCode"));
								logger.info("[" + schedule_id + "] "+ action +" Schedule result : " + reconResultMap.get("HttpsResponseMessage"));
								logger.info("[" + schedule_id + "] "+ action +" Schedule result : " + reconResultMap.get("HttpsResponseData"));
								
								Gson gson = new Gson();
								ScheduleCo co = gson.fromJson(reconResultMap.get("HttpsResponseData").toString(), ScheduleCo.class);
								Pi_ScheduleVO vo = new Pi_ScheduleVO(co);
								
								if(vo != null) {
									String target_id = vo.getSchedule_target_id();
									String target_name = vo.getSchedule_target_name();
									logger.info(i + " [" + target_id + "] " + target_name);
									if(!"".equals(target_id) && target_id != null && !"".equals(target_name) && target_name != null) {
										dao.updateSchedule(vo);
										resultMap.put("resultCode", "00");
										resultMap.put("resultMessage", "Success");
										
										String user_no = SessionUtil.getSession("memberSession", "USER_NO");
										ServletUtil servletUtil = new ServletUtil(request);
										String clientIP = servletUtil.getIp();
										Map<String, Object> userLog = new HashMap<String, Object>();

										userLog.put("user_no", user_no);
										userLog.put("menu_name", "SCHEDULE REGIST");		
										userLog.put("user_ip", clientIP);
										userLog.put("job_info", "스캔 상태 변경 > " + vo.getSchedule_label() + "[" + action + "]");
										userLog.put("logFlag", "1");
										
										userDao.insertLog(userLog);
										
										break;
									} else {
										resultMap.put("resultCode", "89");
										resultMap.put("resultMessage", "Failed");
									}
								} else {
									resultMap.put("resultCode", "99");
									resultMap.put("resultMessage", "Failed");
								}
							}
						}
					}
						
				}
			}
			
		} catch (ProtocolException e) {
			e.printStackTrace();
			resultMap.put("resultCode", "99");
			resultMap.put("resultMessage", "Failed");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("resultCode", "99");
			resultMap.put("resultMessage", "Failed");
		}
		
		return resultMap;
	}

	@Override
	public Map<String, Object> getDetails(HttpServletRequest request) throws Exception {

		//https://masterIP:8339/beta/schedules/98?details=true
		String id = request.getParameter("id");
		int ap_no = Integer.parseInt(request.getParameter("ap_no"));
		
		Properties properties = new Properties();
		String resource = "/property/config.properties";
		Reader reader = Resources.getResourceAsReader(resource);
		
		properties.load(reader);
		
		String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no+1)) ;
		String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no+1)) ;
		String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no+1)) ;
		String api_ver = properties.getProperty("recon.api.version");
		
//		logger.info("getMatchObjects doc : " + "/beta/schedules/" + id + "?details=true");

		Map<String, Object> resultMap = new HashMap<String, Object>();
		ReconUtil reconUtil = new ReconUtil();
		Map<String, Object> httpsResponse = null;
		try {
			httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + api_ver + "/schedules/" + id + "?details=true", "GET", null);
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			resultMap.put("resultCode", -1);
			logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return resultMap;
		}

		int resultCode = (int) httpsResponse.get("HttpsResponseCode");
		String resultMessage = (String) httpsResponse.get("HttpsResponseMessage");

//		if(resultCode == 404) {
//			Map<String, Object> map = new HashMap<>();
//			map.put("id", id);
//			map.put("status", "completed");
//			map.put("ap_no", ap_no);
//			dao.updateStatus(map);
//		}
		if (resultCode != 200) {
			resultMap.put("resultCode", resultCode);
			resultMap.put("resultMessage", resultMessage);
			return resultMap;
		}

		JsonObject jsonObject = new Gson().fromJson(httpsResponse.get("HttpsResponseData").toString(), JsonObject.class);

		ArrayList<Map<String, String>> detailList = new ArrayList<>();

		JsonArray jsonArr = jsonObject.getAsJsonArray("targets");

		for (int i = 0; i < jsonArr.size(); i++) {
		    JsonObject obj = jsonArr.get(i).getAsJsonObject();

		    String name = obj.get("name").getAsString();
		    String status = "";
		    String percentage = "";
		    String currentlyFile = "";

		    JsonArray locArr = obj.getAsJsonArray("locations");

		    for (int j = 0; j < locArr.size(); j++) {
		        JsonObject locObj = locArr.get(j).getAsJsonObject();
		        status = locObj.get("status").getAsString();
		        if (!"".equals(status) && status != null) {
		            Map<String, Object> map = new HashMap<>();
		            map.put("id", id);
		            map.put("status", status);
		            map.put("ap_no", ap_no);
		            dao.updateStatus(map);
		        }
		        if (!"completed".equals(status)) {
		            if (locObj.has("message")) {
		                String message = locObj.get("message").getAsString();
		                percentage = message.substring(0, message.indexOf("%") + 1);
		                if (message.indexOf("'File path ") >= 0) {
		                    currentlyFile = message.substring(message.indexOf("'File path ") + 11, message.length() - 1);
		                } else {
		                    currentlyFile = message;
		                }
		            }
		        }
		    }

		    Map<String, String> data = new HashMap<>();
		    data.put("name", name);
		    data.put("status", status);
		    data.put("percentage", percentage);
		    data.put("currentlyFile", currentlyFile);

		    detailList.add(data);
		}
		
		
		resultMap.put("resultData", detailList);
		resultMap.put("resultCode", 0);
		resultMap.put("resultMessage", "SUCCESS");
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> registSchedule_user(HttpServletRequest request) throws Exception {

		logger.info("getMatchObjects doc : " + "/beta/schedules");
		String scheduleData = request.getParameter("scheduleData");
		ReconUtil reconUtil = new ReconUtil();
		
		JsonObject jsonObj = new JsonObject();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> policyMap = new HashMap<String, Object>();
		
		//logger.info("data :: " + scheduleData);
		
		Calendar cal = Calendar.getInstance();
		String policy = "";
		String label = "";
		JsonArray profiles = new JsonArray();
		JsonObject pause = new JsonObject();
		boolean trace = false;
		
		JsonParser parser = new JsonParser();
		JsonObject dataObj = (JsonObject) parser.parse(scheduleData);
		
		policy = dataObj.get("policy").toString().replaceAll("\"", "");
		policyMap = dao.getPolicyByIdx(policy);
		
		JsonArray targetArr = (JsonArray) dataObj.get("targets");
		if(targetArr.size() > 1) {
			label = targetArr.size() + " Targets_" + policyMap.get("SCHEDULE_LABEL") + " " + new SimpleDateFormat("MM").format(cal.getTime())+new SimpleDateFormat("dd").format(cal.getTime())+"-"+new SimpleDateFormat("ssss").format(cal.getTime());
			for(int i=0; i<targetArr.size(); i++) {
				JsonObject obj = (JsonObject) targetArr.get(i);
				logger.info("["+"]"+obj.toString());
			}
		} else if(targetArr.size() == 1) {
			JsonObject targetObj = (JsonObject) targetArr.get(0);
			
			String target_name = dao.getTargetName(targetObj.get("id").toString().replaceAll("\"", ""));
			label = target_name + "_" + policyMap.get("SCHEDULE_LABEL") + " " + new SimpleDateFormat("MM").format(cal.getTime())+new SimpleDateFormat("dd").format(cal.getTime())+"-"+new SimpleDateFormat("ssss").format(cal.getTime());
		}
		
		String[] datatype_id = policyMap.get("DATATYPE_ID").toString().split(",");
		for(String datatype : datatype_id) {
			profiles.add(datatype);
		}
		
		try {
			if(!"".equals(policyMap.get("SCHEDULE_PAUSE_FROM").toString()) && policyMap.get("SCHEDULE_PAUSE_FROM") != null) {
				pause.addProperty("start", Integer.parseInt(policyMap.get("SCHEDULE_PAUSE_FROM").toString()));
			}
			if(!"".equals(policyMap.get("SCHEDULE_PAUSE_TO").toString()) && policyMap.get("SCHEDULE_PAUSE_TO") != null) {
				pause.addProperty("end", Integer.parseInt(policyMap.get("SCHEDULE_PAUSE_TO").toString()));
			}
			if(!"".equals(policyMap.get("SCHEDULE_PAUSE_DAYS").toString()) && policyMap.get("SCHEDULE_PAUSE_DAYS") != null) {
				pause.addProperty("days", policyMap.get("SCHEDULE_PAUSE_DAYS").toString());
			}
		} catch (NullPointerException e) {
		}
		
		trace = ("true".equals(policyMap.get("SCHEDULE_TRACE").toString()))? true : false;
		
		cal.add(Calendar.MINUTE, 2);
		jsonObj.addProperty("label", label);
		jsonObj.add("targets", targetArr);
		jsonObj.add("profiles", profiles);
		jsonObj.addProperty("start", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(cal.getTime()));
		jsonObj.addProperty("repeat_days", 0);
		jsonObj.addProperty("repeat_months", 0);
		jsonObj.addProperty("cpu", policyMap.get("SCHEDULE_CPU").toString());
		if(!"".equals(policyMap.get("SCHEDULE_DATA").toString()) && policyMap.get("SCHEDULE_DATA") != null) {
			jsonObj.addProperty("throughput", Integer.parseInt(policyMap.get("SCHEDULE_DATA").toString()));
		}
		if(!"".equals(policyMap.get("SCHEDULE_MEMORY").toString()) && policyMap.get("SCHEDULE_MEMORY") != null) {
			jsonObj.addProperty("memory", Integer.parseInt(policyMap.get("SCHEDULE_MEMORY").toString()));
		}
		jsonObj.add("pause", pause);
		jsonObj.addProperty("trace", trace);
		jsonObj.addProperty("timezone", "Default");
		jsonObj.addProperty("capture", false);
		
		//Map<String, Object> httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url+"/beta/schedules", "POST", scheduleData);
		Map<String, Object> httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url+"/beta/schedules", "POST", jsonObj.toString());
		
		int resultCode = (int) httpsResponse.get("HttpsResponseCode");
		String resultMessage = (String) httpsResponse.get("HttpsResponseMessage");
		
		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMessage", resultMessage);
		
		// User Log 남기기
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		Map<String, Object> userLog = new HashMap<String, Object>();

		userLog.put("user_no", user_no);
		userLog.put("menu_name", "SCHEDULE REGIST");		
		userLog.put("user_ip", clientIP);
		//userLog.put("job_info", "스캔 등록");
		userLog.put("logFlag", "1");
		
		if(resultCode == 201) {
			userLog.put("job_info", "스캔 등록 성공");
		} else if (resultCode == 409) {
			userLog.put("job_info", "스캔 등록 실패 - 스케줄명 중복");
		} else if (resultCode == 422) {
			userLog.put("job_info", "스캔 등록 실패 - 시작시간 오류");
		} else {
			userLog.put("job_info", "스캔 등록 실패 - " + resultCode);
		}
		
		userDao.insertLog(userLog);
		
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> selectGroupList(Map<String, Object> map) throws Exception {
		return dao.selectGroupList(map);
	}

	@Override
	public List<Map<String, Object>> selectLocationList(Map<String, Object> map) throws Exception {
		try {
			
			logger.info("selectLocationList :: " + map.toString());
			String user_no = SessionUtil.getSession("memberSession", "USER_NO");
			if(map.get("aut") != null && !map.get("aut").equals("manager")) {
				logger.info((map.get("aut") != null && !map.get("aut").equals("manager")) + "");
				map.put("user_no", user_no);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("start selectLocationList dao");
		return dao.selectLocationList(map);
	}

	@Override
	public List<Map<String, Object>> getApList(HttpServletRequest request) throws Exception {
		List<Map<String, Object>> resultList = new ArrayList<>();
		
		resultList = dao.getApList();
		
		return resultList;
	}

	@Override
	public Map<String, Object> getPolicyByApno(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		try {
			
			map.put("ap_no", request.getParameter("ap_no"));
			
			resultMap = dao.getPolicyByApno(map);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultMap;
	}

	
	
}
