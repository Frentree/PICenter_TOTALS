package com.org.iopts.service;

import java.io.Reader;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.org.iopts.dao.Pi_DashDAO;
import com.org.iopts.dto.Pi_AgentVO;
import com.org.iopts.exception.dao.piDetectionListDAO;
import com.org.iopts.util.ReconUtil;
import com.org.iopts.util.SessionUtil;

import com.google.gson.JsonArray;

@Service
public class Pi_DashServiceImpl implements Pi_DashService {
	private static Logger logger = LoggerFactory.getLogger(Pi_DashServiceImpl.class);
	@Inject
	private Pi_DashDAO dao;
	
	@Inject
	private piDetectionListDAO detectionDao;
	
	@Inject
	private Pi_TargetService targetservice;
	
	@Override
	public List<Pi_AgentVO> selectDashMenu() throws Exception {
		// TODO Auto-generated method stub
		String user_id = SessionUtil.getSession("memberSession", "USER_NO");
		logger.info("user_id=============================" + user_id);
		return dao.selectDashMenu(user_id);
	}

	@Override
	public Map<String, Object> selectDashInfo(HttpServletRequest request, String api_ver) throws Exception {
		String target_id = request.getParameter("target_id");
		int ap_no = Integer.parseInt(request.getParameter("ap_no"));
		
		logger.info("target_id :: " + target_id);
		logger.info("ap_no :: " + ap_no);
		if(target_id == null) {
			target_id = "";
		}
		Map<String, Object> map = new HashMap<>();
		map.put("target_id", target_id);
		map.put("ap_no", ap_no);
		
//		Map<String, Object> returnMap = dao.selectDashInfo(target_id);
		Map<String, Object> returnMap = dao.selectDashInfo(map);
		
		/**
		 * 여기서 리콘을 연결해서
		 * API 호출 : https://172.30.1.58:8339/beta/targets/6486283965405758392/consolidated
		 * 여기서 나오는 값증에 제일 위에 값 하나를 가져와서
		 */
		
		ReconUtil reconUtil = new ReconUtil();
		Map<String, Object> httpsResponse = null;
		
		Properties properties = new Properties();
		String resource = "/property/config.properties";
		Reader reader = Resources.getResourceAsReader(resource);
		
		properties.load(reader);
		
		String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no+1)) ;
		String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no+1)) ;
		String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no+1)) ;
		
		try {
			logger.info("ap_no :: " + ap_no);
			logger.info("recon_url :: " + recon_url);
			logger.info("recon_id :: " + recon_id);
			logger.info("recon_password :: " + recon_password);
			httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + api_ver + "/targets/" + target_id + "/isolated", "GET", null);
			JsonArray jsonArray = new Gson().fromJson(httpsResponse.get("HttpsResponseData").toString(), JsonArray.class);
			logger.info("getMatchObjects jsonArray : " + jsonArray);
			JsonArray arr = jsonArray.getAsJsonArray();
			String lastDate = arr.get(0).getAsString();

			long unixTime = Long.parseLong(lastDate) * 1000;
			Date date = new Date(unixTime);
			returnMap.put("REGDATE", date);
		}catch (NullPointerException e) {
			// TODO: handle exception
			e.printStackTrace();
			returnMap.put("REGDATE", "-");
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	    logger.info("returnMap chk : " + returnMap);
		return returnMap;
	}
	
	@Override
	public Map<String, Object> selectlastScanDate(HttpServletRequest request) {
		logger.info("selectlastScanDate");
		String target_id = request.getParameter("target_id");
		logger.info("target_id check : "+ target_id);
		return dao.selectlastScanDate(target_id);
	}
	
	/*
	@Override
	public List<Object> selectDatatype(HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		String user_id = SessionUtil.getSession("memberSession", "USER_NO");
		String target_id = request.getParameter("target_id");
		String days = request.getParameter("days");
		String date = null;
		
		Map<String, Object> type = new HashMap<>();
		List<Map<String, Object>> Data = null;
		Map<String, Object> real_data = new HashMap<>();
		List<Object> returnData = new ArrayList<>();
		int search_day = 0;
		int search_month = 0;
		type.put("user_id", user_id);
		if(target_id == null || target_id.equals("")) {
			
			if(days.equals("days")) {
				search_day = 7;
				type.put("date", search_day);
				Data = dao.selectDatatypeAll_day(type);
				logger.info("Data check1 : " + Data);
			} else if(days.equals("month")) {
				search_day = 30;
				type.put("date", search_day);
				Data = dao.selectDatatypeAll_day(type);
				logger.info("Data check2 : " + Data);
			} else if(days.equals("three_month")) {
				search_day = 90;
				type.put("date", search_day);
				Data = dao.selectDatatypeAll_day(type);
				logger.info("Data check3 : " + Data);
			} else if(days.equals("six_month")) {
				search_month = 6;
				Data = dao.selectDatatypeAll(user_id);
			} else {
				search_month = 12;
				Data = dao.selectDatatypeAll(user_id);
			}
			
		} else {
			type.put("target_id", target_id);
			if(days.equals("days")) {
				search_day = 7;
				type.put("date", search_day);
				Data = dao.selectDatatype_days(type);
			} else if(days.equals("month")) {
				search_day = 30;
				type.put("date", search_day);
				Data =  dao.selectDatatype_days(type);
			} else if(days.equals("three_month")) {
				search_day = 90;
				type.put("date", search_day);
				Data = dao.selectDatatype_days(type);
			} else if(days.equals("six_month")) {
				search_month = 6;
				type.put("date", search_day);
				Data = dao.selectDatatype(target_id);
			} else {	// 1년
				search_month = 12;
				Data = dao.selectDatatype(target_id);
			}
		}
		for(int i = 0; i < Data.size(); i++) { 
			Pi_TotalDatetypeVO vo = new Pi_TotalDatetypeVO();
			vo.setType1(Data.get(i).get("TYPE1").toString());
			vo.setType2(Data.get(i).get("TYPE2").toString());
			vo.setType3(Data.get(i).get("TYPE3").toString());
			vo.setType4(Data.get(i).get("TYPE4").toString());
			vo.setType5(Data.get(i).get("TYPE5").toString());
			vo.setType6(Data.get(i).get("TYPE6").toString());
			vo.setTotal(Data.get(i).get("TOTALS").toString());
			vo.setRegdate(Data.get(i).get("REGDATE").toString());
			
			real_data.put((String) Data.get(i).get("REGDATE").toString(), vo);
			logger.info("real_data chk : "+ real_data.toString());
		}
		
		if(search_month == 0) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
			Date time = new Date();
			Calendar time_set = Calendar.getInstance();
			time_set.setTime(time);
			time_set.add(time_set.DATE, -search_day);
			for(int i = 0; i <= search_day; i++) {
				date = format.format(time_set.getTime());
				
				logger.info("date chk : "+ date);
				logger.info("real chk : " + real_data.get(date));
				
				
				Pi_TotalDatetypeVO vo = new Pi_TotalDatetypeVO();
				if(real_data.get(date) != null) {
					returnData.add(real_data.get(date));
				} else if(real_data.get(date) == null){
					vo.setType1("0");
					vo.setType2("0");
					vo.setType3("0");
					vo.setType4("0");
					vo.setType5("0");
					vo.setType6("0");
					vo.setTotal("0");
					vo.setRegdate(date);
					returnData.add(vo);
				}
				time_set.add(time_set.DATE, 1);
			}
		} else {
			SimpleDateFormat format = new SimpleDateFormat("yyyy.MM");
			Date time = new Date();
			Calendar time_set = Calendar.getInstance();
			time_set.setTime(time);
			time_set.add(time_set.MONTH, (-search_month+1));
			for(int i = 0; i < search_month; i++) {
				date = format.format(time_set.getTime());
				Pi_TotalDatetypeVO vo = new Pi_TotalDatetypeVO();
				if(real_data.get(date) != null) {
					returnData.add(real_data.get(date));
				} else if(real_data.get(date) == null){
					vo.setType1("0");
					vo.setType2("0");
					vo.setType3("0");
					vo.setType4("0");
					vo.setType5("0");
					vo.setType6("0");
					vo.setTotal("0");
					vo.setRegdate(date);
					returnData.add(vo);
				}
				time_set.add(time_set.MONTH, 1);
			}
		}
		
		return returnData;
	}*/

	@Override
	public Map<String,Object> selectDatatype(HttpServletRequest request) throws Exception {
		logger.info("selectDatatype");
		
		Map<String, Object> map = new HashMap<>();
		
		// 7일
		String days = request.getParameter("days");
		String target_id = request.getParameter("target_id");
		
		logger.info("chk : " + days + " , " + target_id);
		
		Map<String, Object> type = new HashMap<String, Object>();
		List<Map<String, Object>> Data = null;
		int search_day = 0;
		int search_month = 0;
		List<Integer> patternList = null;
		
		try {
			// PICenter 에서 받아오는 개인정보 유형 갯수
			patternList = detectionDao.queryCustomDataTypesCnt();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		type.put("patternList", patternList);
		
		if(target_id == null || target_id.equals("")) {
			
			if(days.equals("days")) {
				search_day = 7;
				type.put("date", search_day);
				logger.info("type chk : " + type.toString());
				Data = dao.selectDatatypeAll_day(type);
				logger.info("Data check1 : " + Data);
			} else if(days.equals("month")) {
				search_day = 30;
				type.put("date", search_day);
				Data = dao.selectDatatypeAll_day(type);
			} else if(days.equals("three_month")) {
				search_day = 90;
				type.put("date", search_day);
				Data = dao.selectDatatypeAll_day(type);
			} else if(days.equals("six_month")) {
				search_month = 6;
				type.put("date", search_month);
				Data = dao.selectDatatypeAll(type);
			} else {
				search_month = 12;
				type.put("date", search_month);
				Data = dao.selectDatatypeAll(type);
			}
			
		} 
		else {
			type.put("target_id", target_id);
			if(days.equals("days")) {
				search_day = 7;
				type.put("date", search_day);
				logger.info("type chk : " + type.toString());
				Data = dao.selectDatatype_days(type);
				logger.info("Data check1 : " + Data);
				
			} else if(days.equals("month")) {
				search_day = 30;
				type.put("date", search_day);
				Data =  dao.selectDatatype_days(type);
			} else if(days.equals("three_month")) {
				search_day = 90;
				type.put("date", search_day);
				Data = dao.selectDatatype_days(type);
			} else if(days.equals("six_month")) {
				search_month = 6;
				type.put("date", search_month);
				Data = dao.selectDatatype(type);
			} else {	// 1년
				search_month = 12;
				type.put("date", search_month);
				Data = dao.selectDatatype(type);
			}
		}
		
		map.put("Data", Data);
		map.put("DataTypes", detectionDao.queryCustomDataTypes());
		return map;
	}
	
	@Override
	public Map<String, Object> selectDatatypeManager(HttpServletRequest request) throws Exception {
		logger.info("selectDatatypeManager");
		
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> type = new HashMap<String, Object>();
		
		// 7일
		String days = request.getParameter("days");
		String target_id = request.getParameter("target_id");
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String insa_code = SessionUtil.getSession("memberSession", "INSA_CODE");
		
		type.put("user_no", user_no);
		type.put("insa_code", insa_code);
		
		List<Map<String,Object>> chkList = dao.selectChkManagerTargetId(type);
		List<String> targetList = new ArrayList<>();
		
		for (Map<String, Object> chkMap : chkList) {
			targetList.add((String) chkMap.get("TARGET_ID"));
		}
		
		logger.info("chk : " + days + " , " + target_id);
		
		List<Map<String, Object>> Data = null;
		int search_day = 0;
		int search_month = 0;
		
		List<Integer> patternList = detectionDao.queryCustomDataTypesCnt2(); 
		
		List<Map<String, Integer>> datatypesList = new ArrayList<>();
		Map<String, Integer> datatypesMap = new HashMap<>();
		
		for(int i=0 ; i< patternList.size() ; i++) {
			datatypesMap = new HashMap<>(); 
			datatypesMap.put("type", patternList.get(i));
			datatypesMap.put("type_cnt", i);
			
			datatypesList.add(datatypesMap);
		}
		type.put("datatypesList", datatypesList);
		type.put("patternList", patternList);
		type.put("user_no", user_no);
		type.put("insa_code", insa_code);
		type.put("targetList", targetList);
		
		if(target_id == null || target_id.equals("")) {
			
			if(days.equals("days")) {
				search_day = 7;
				type.put("date", search_day);
				type.put("user_no", user_no);
				Data = dao.selectDatatypeAll_day_manager(type);
			} else if(days.equals("month")) {
				search_day = 30;
				type.put("date", search_day);
				type.put("user_no", user_no);
				Data = dao.selectDatatypeAll_day_manager(type);
			} else if(days.equals("three_month")) {
				search_day = 90;
				type.put("date", search_day);
				type.put("user_no", user_no);
				Data = dao.selectDatatypeAll_day_manager(type);
			} else if(days.equals("six_month")) {
				search_month = 6;
				type.put("date", search_month);
				type.put("user_no", user_no);
				Data = dao.selectDatatypeAll_manager(type);
			} else {
				search_month = 12;
				type.put("date", search_month);
				type.put("user_no", user_no);
				Data = dao.selectDatatypeAll_manager(type);
			}
			
		} 
		else {
			type.put("target_id", target_id);
			if(days.equals("days")) {
				search_day = 7;
				type.put("date", search_day);
				Data = dao.selectDatatype_days_manager(type);
			} else if(days.equals("month")) {
				search_day = 30;
				type.put("date", search_day);
				Data =  dao.selectDatatype_days_manager(type);
			} else if(days.equals("three_month")) {
				search_day = 90;
				type.put("date", search_day);
				Data = dao.selectDatatype_days_manager(type);
			} else if(days.equals("six_month")) {
				search_month = 6;
				type.put("date", search_month);
				Data = dao.selectDatatype_manager(type);
			} else {	// 1년
				search_month = 12;
				type.put("date", search_month);
				Data = dao.selectDatatype_manager(type);
			}
		}
		
		map.put("Data", Data);
		map.put("DataTypes", detectionDao.queryCustomDataTypes());
		return map;
	}
	
	@Override
	public Map<String, Object> selectDatatypes(HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		List<String> datatype = new ArrayList<String>();
		datatype.add("RRN");
		datatype.add("FOREIGNER");
		datatype.add("DRIVER");
		datatype.add("PASSPORT");
		datatype.add("ACCOUNT_NUM");
		datatype.add("CARD_NUM");
		datatype.add("PHONE_NUM");
		datatype.add("MOBILE_PHONE");
		String user_id = SessionUtil.getSession("memberSession", "USER_NO");
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		for(int i = 0; i < datatype.size(); i++) {
			data.put("datatype", datatype.get(i));
			data.put("user_id", user_id);
			result.put(datatype.get(i), dao.selectDatatypes(data));
		}
		return result;
	}

	@Override
	public List<Object> selectSystemCurrent(HttpServletRequest request) {
		logger.info("selectSystemCurrent check");
		String id = request.getParameter("id");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		
		Map<String, Object> input = new HashMap<>();
		input.put("id", id);
		input.put("fromDate", fromDate);
		input.put("toDate", toDate);
		
		return dao.selectSystemCurrent(input);
	}
	
	@Override
	public List<Object> selectSystemCurrentPC(HttpServletRequest request) {
		logger.info("selectSystemCurrentPC check");
		String id = request.getParameter("id");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		
		Map<String, Object> input = new HashMap<>();
		input.put("id", id);
		input.put("fromDate", fromDate);
		input.put("toDate", toDate);
		
		return dao.selectSystemCurrentPC(input);
	}
	
	@Override
	public List<Object> selectSystemCurrentManager(HttpServletRequest request) {
		logger.info("selectSystemCurrentManager check");
		
		Map<String, Object> input = new HashMap<>();
		String user_no = request.getParameter("user_no");
		String insa_code = request.getParameter("insa_code");
		input.put("user_no", user_no);
		input.put("insa_code", insa_code);
		
		List<Map<String,Object>> chkList = dao.selectChkManagerTargetId(input);
		List<String> targetList = new ArrayList<>();
		
		for (Map<String, Object> chkMap : chkList) {
			targetList.add((String) chkMap.get("TARGET_ID"));
		}
		
		input.put("user_no", user_no);
		input.put("insa_code", insa_code);
		input.put("targetList", targetList);
		
		return dao.selectSystemCurrentManager(input);
	}
	
	@Override
	public List<Object> selectSystemCurrentService(HttpServletRequest request) {
		logger.info("selectSystemCurrentService check");
		String id = request.getParameter("id");
		String user_no = request.getParameter("user_no");
		
		Map<String, Object> input = new HashMap<>();
		input.put("id", id);
		input.put("user_no", user_no);
		
		return dao.selectSystemCurrentService(input);
	}
	
	@Override
	public List<Object> selectServerExcelDownload(HttpServletRequest request) {
		logger.info("selectExcelDownload check");
		Map<String, Object> input = new HashMap<>();
		List<Object> resultList = new ArrayList<>();
		try {
			String id = request.getParameter("id");
			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			
			input.put("id", id);
			input.put("fromDate", fromDate);
			input.put("toDate", toDate);
			
			// PICenter 에서 받아오는 개인정보 유형 갯수
			List<Integer> patternList = detectionDao.queryCustomDataTypesCnt2(); 
			input.put("patternList", patternList); 
			
			List<Map<String, Integer>> datatypesList = new ArrayList<>();
			Map<String, Integer> datatypesMap = new HashMap<>();
			
			for(int i=0 ; i< patternList.size() ; i++) {
				datatypesMap = new HashMap<>();
				datatypesMap.put("type", patternList.get(i));
				datatypesMap.put("type_cnt", i);
				
				datatypesList.add(datatypesMap);
			}
			
			input.put("datatypesList", datatypesList);
			
			resultList = dao.selectServerExcelDownload(input);
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("error ::  " + e.getLocalizedMessage());
		} 
		
		return resultList;
	}
	
	@Override
	public List<Object> selectServerExcelDownloadList(HttpServletRequest request, List<String> targetList) throws Exception {
		
		String ap = request.getParameter("ap");
		String id = request.getParameter("id");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		
		Map<String, Object> input = new HashMap<>();
		
		logger.info(targetList.toString());
		
		input.put("targetList", targetList);
		input.put("ap", ap);
		input.put("id", id);
		input.put("fromDate", fromDate);
		input.put("toDate", toDate);
		
		logger.info("input >> " + input);
		
		return dao.selectServerExcelDownloadList(input);
	}
	
	@Override
	public List<Object> selectPCExcelDownload(HttpServletRequest request) {
		logger.info("selectPCExcelDownload check");
		String id = request.getParameter("id");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		
		Map<String, Object> input = new HashMap<>();
		input.put("id", id);
		input.put("fromDate", fromDate);
		input.put("toDate", toDate);
		
		return dao.selectPCExcelDownload(input);
	}
	
	@Override
	public List<Object> selectPCExcelDownloadList(HttpServletRequest request, List<String> targetList) throws Exception {
		
		String ap = request.getParameter("ap");
		String id = request.getParameter("id");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		
		Map<String, Object> input = new HashMap<>();
		
		logger.info(targetList.toString());
		
		input.put("targetList", targetList);
		input.put("ap", ap);
		input.put("id", id);
		input.put("fromDate", fromDate);
		input.put("toDate", toDate);
		
		logger.info("input >> " + input);
		
		return dao.selectPCExcelDownloadList(input);
	}
	
	@Override
	public List<Object> selectPathCurrent(HttpServletRequest request) {
		logger.info("selectPathCurrent check");
		return dao.selectPathCurrent();
	}
	
	@Override
	public List<Object> selectNotAction_group(HttpServletRequest request) {
		logger.info("selectNotAction_group check");
		return null;
	}

	@Override
	public Map<String, Object> selectDashDataDetectionList(HttpServletRequest request) throws Exception {
		logger.info("selectDashDataDetectionList");
		
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("fromDate", fromDate);
		map.put("toDate", toDate);
		
		return dao.selectDashDataDetectionList(map);
	}
	
	@Override
	public Map<String, Object> selectDashDataDetectionServerList(HttpServletRequest request) throws Exception {
		logger.info("selectDashDataDetectionServerList");
		
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("fromDate", fromDate);
		map.put("toDate", toDate);
		
		return dao.selectDashDataDetectionServerList(map);
	}
	
	@Override
	public Map<String, Object> selectDashDataDetectionPCList(HttpServletRequest request) throws Exception {
		logger.info("selectDashDataDetectionPCList");
		
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("fromDate", fromDate);
		map.put("toDate", toDate);
		
		return dao.selectDashDataDetectionPCList(map);
	}
	
	@Override
	public Map<String, Object> selectDashDataCompleteList(HttpServletRequest request) throws Exception {
		logger.info("selectDashDataCompleteList");
		String target_id = request.getParameter("target_id");
		String id = request.getParameter("id");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("target_id", target_id);
		map.put("id", id);
		return dao.selectDashDataCompleteList(map);
	}
	
	@Override
	public Map<String, Object> selectDashDataDetectionItemList(HttpServletRequest request) throws Exception {
		
		String ap_no = request.getParameter("ap_no");
		String target_id = request.getParameter("target_id");
		String id = request.getParameter("id");
		
		Map<String, Object> input = new HashMap<>();
		
		input.put("ap_no", ap_no);
		input.put("target_id", target_id);
		input.put("id", id);
		
		return dao.selectDashDataDetectionItemList(input);
	}
	
	@Override
	public Map<String, Object> selectDashPersonalServerDetectionItemList(HttpServletRequest request, List<String> targetList) throws Exception {
		
		String ap = request.getParameter("ap");
		String id = request.getParameter("id");
		
		Map<String, Object> input = new HashMap<>();
		
		logger.info(targetList.toString());
		
		input.put("targetList", targetList);
		input.put("ap", ap);
		input.put("id", id);
		
		logger.info("input >> " + input);
		
		return dao.selectDashPersonalServerDetectionItemList(input);
	}
	
	@Override
	public Map<String, Object> selectDashPersonalServerComplete(HttpServletRequest request, List<String> targetList) throws Exception {
		
		String ap = request.getParameter("ap");
		String id = request.getParameter("id");
		
		Map<String, Object> input = new HashMap<>();
		
		input.put("targetList", targetList);
		input.put("ap", ap);
		input.put("id", id);
		
		logger.info("input >> " + input);
		
		return dao.selectDashPersonalServerComplete(input);
	}
	
	@Override
	public List<Object> selectDashDataRank(HttpServletRequest request) {
		logger.info("selectDashDataRank check");
		
		String id = request.getParameter("id");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");

		Map<String, Object> input = new HashMap<>();
		input.put("id", id);
		input.put("fromDate", fromDate);
		input.put("toDate", toDate);
		
		return dao.selectDashDataRank(input);
	}
	
	@Override
	public List<Map<String, Object>> selectDashPersonalServerRank(HttpServletRequest request, List<String> targetList) {
		
		String id = request.getParameter("id");
		
		Map<String, Object> input = new HashMap<>();
		
		String targetArr = (String) request.getParameter("targetList");
		
		JsonParser targetPar = new JsonParser();
		JsonArray targetJArr = targetPar.parse(targetArr).getAsJsonArray();
		
		for (JsonElement ele : targetJArr) {
			targetList.add(ele.getAsString());
		}

		logger.info(targetList.toString());
		
		input.put("targetList", targetList);
		input.put("id", id);
		
		logger.info("inpur >> " +  input);
		
		return dao.selectDashPersonalServerRank(input);
	}
	
	@Override
	public List<Map<String, Object>> selectDashPersonalPCRank(HttpServletRequest request, List<String> targetList) {
		
		String id = request.getParameter("id");
		
		Map<String, Object> input = new HashMap<>();
		
		String targetArr = (String) request.getParameter("targetList");
		
		JsonParser targetPar = new JsonParser();
		JsonArray targetJArr = targetPar.parse(targetArr).getAsJsonArray();
		
		for (JsonElement ele : targetJArr) {
			targetList.add(ele.getAsString());
		}

		logger.info(targetList.toString());
		
		input.put("targetList", targetList);
		input.put("id", id);
		
		logger.info("inpur >> " +  input);
		
		return dao.selectDashPersonalPCRank(input);
	}
	
	@Override
	public List<Object> selectDashPersonalManagerRank(HttpServletRequest request) {
		logger.info("selectDashPersonalManagerRank check");
		
		Map<String, Object> input = new HashMap<>();
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String insa_code = SessionUtil.getSession("memberSession", "INSA_CODE");
		input.put("user_no", user_no);
		input.put("insa_code", insa_code);
		
		List<Map<String,Object>> chkList = dao.selectChkManagerTargetId(input);
		List<String> targetList = new ArrayList<>();
		
		for (Map<String, Object> chkMap : chkList) {
			targetList.add((String) chkMap.get("TARGET_ID"));
		}

		input.put("user_no", user_no);
		input.put("insa_code", insa_code);
		input.put("targetList", targetList);
		
		return dao.selectDashPersonalManagerRank(input);
	}
	
	@Override
	public List<Object> selectDashDataImple(HttpServletRequest request) {
		logger.info("selectDashDataImple check");
		return dao.selectDashDataImple();
	}
	
	@Override
	public List<Object> selectDashDataImpleManager(HttpServletRequest request) {
		logger.info("selectDashDataImpleManager check");
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		
		Map<String, Object> input = new HashMap<>();
		input.put("user_no", user_no);
		return dao.selectDashDataImpleManager(input);
	}
	
	@Override
	public List<Map<String, Object>> selectDashPersonalServerImple(HttpServletRequest request, List<String> targetList) throws Exception {
		
		String ap = request.getParameter("ap");
		String id = request.getParameter("id");
		
		Map<String, Object> input = new HashMap<>();
		
		String targetArr = (String) request.getParameter("targetList");

		JsonParser targetPar = new JsonParser();
		JsonArray targetJArr = targetPar.parse(targetArr).getAsJsonArray();
		
		for (JsonElement ele : targetJArr) {
			targetList.add(ele.getAsString());
		}
		
		input.put("targetList", targetList);
		input.put("ap", ap);
		input.put("id", id);
		
		return dao.selectDashPersonalServerImple(input);
		
	}
	
	@Override
	public List<Map<String, Object>> selectDashDataPersonalServer(HttpServletRequest request) throws Exception {
		
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		
		Map<String, Object> input = new HashMap<>();
		
		input.put("fromDate", fromDate);
		input.put("toDate", toDate);
		
		return dao.selectDashDataPersonalServer(input);
		
	}
	
	@Override
	public List<Map<String, Object>> selectDashDataPersonalPC(HttpServletRequest request) throws Exception {
		
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		
		Map<String, Object> input = new HashMap<>();
		
		input.put("fromDate", fromDate);
		input.put("toDate", toDate);
		
		return dao.selectDashDataPersonalPC(input);
		
	}
	
	@Override
	public Map<String, Object> selectDashDataPersonalServerCount(HttpServletRequest request) throws Exception {
		logger.info("selectDatatype");
		Map<String, Object> map = new HashMap<String, Object>();
		return dao.selectDashDataPersonalServerCount(map);
	}
	
	@Override
	public List<Map<String, Object>> selectDashDataPersonalServerCircle(HttpServletRequest request, List<String> targetList) {
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		String ap = request.getParameter("ap");
		/*int Id = Integer.parseInt(request.getParameter("groupId"));*/
		
		Map<String, Object> input = new HashMap<>();
		
		input.put("targetList", targetList);
		input.put("id", id);
		input.put("type", type);
		input.put("ap", ap);
		
		logger.info("input >> " + input);
		
		return dao.selectDashDataPersonalServerCircle(input);
	}
	
	@Override
	public List<Map<String, Object>> selectDashDataPersonalPCCircle(HttpServletRequest request, List<String> targetList) {
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		String ap = request.getParameter("ap");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		/*int Id = Integer.parseInt(request.getParameter("groupId"));*/
		
		Map<String, Object> input = new HashMap<>();
		
		input.put("targetList", targetList);
		input.put("id", id);
		input.put("type", type);
		input.put("ap", ap);
		input.put("fromDate", fromDate);
		input.put("toDate", toDate);
		
		logger.info("input >> " + input);
		
		return dao.selectDashDataPersonalPCCircle(input);
	}
	
	@Override
	public List<Map<String, Object>> selectDashDataTodoApproval(HttpServletRequest request) throws Exception {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		 
		Map<String, Object> input = new HashMap<>();
		
		input.put("user_no", user_no);
		return dao.selectDashDataTodoApproval(input);
	}
	
	@Override
	public List<Map<String, Object>> selectDashPieDraw(HttpServletRequest request) throws Exception {
		List<Map<String, Object>> resultList = new ArrayList<>();
		
		try {
			String user_no = SessionUtil.getSession("memberSession", "USER_NO");
			String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
			
			Map<String, Object> input = new HashMap<>();
			
			input.put("user_no", user_no);
			input.put("user_grade", user_grade);
			
			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			input.put("fromDate", fromDate);
			input.put("toDate", toDate);
			
			resultList = dao.selectDashPieDraw(input);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("e :: " +e);
		}
		
		return resultList;
	}
	
	@Override
	public List<Map<String, Object>> selectDashGridDrawData(HttpServletRequest request) throws Exception {
		
		List<Map<String, Object>> resultList = new ArrayList<>();
				
		try {
			String user_no = SessionUtil.getSession("memberSession", "USER_NO");
			String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
			
			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			
			Map<String, Object> input = new HashMap<>();
			
			input.put("user_no", user_no);
			input.put("user_grade", user_grade);
			input.put("fromDate", fromDate);
			input.put("toDate", toDate);
			
			resultList = dao.selectDashGridDrawData(input);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("e :: " +e);
		}
		
		return resultList;
	}
	
	@Override
	public List<Map<String, Object>> selectDashBarDrawData(Map<String, Object>request) throws Exception {
		
		List<Map<String, Object>> dashList = new ArrayList<>();
		List<Map<String, Object>> resultList = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Map<String, Long>> dateToPatternSum = new HashMap<>();
		
		try {
			
			List<Map<String, Object>> targetList = (List<Map<String, Object>>) request.get("target_list");
		    String days = String.valueOf(request.get("days"));
		    String[] dayList = days.split("_");
		    
			Map<String, Object> input = new HashMap<>();
			input.put("targetList", targetList);
			
			int targetSize  = targetList.size();
			
			int day = Integer.parseInt(dayList[0]); // 0 : 일자,
			String dayStatus = dayList[1]; // 1: 구분(일, 월, 년), 
			int dayType = Integer.parseInt(dayList[2]); // 2:구분(조회기준 1:일, 2:월)
			
			input.put("dayStatus", dayStatus);
			input.put("days", day);
			
			LocalDate current = LocalDate.now();
			LocalDate oneDayAgo = null;
			 
			if(dayStatus.equals("year")) {
				oneDayAgo = current.minusYears(day);
			}else if(dayStatus.equals("month")) {
				oneDayAgo = current.minusMonths(day);
			}else {
				oneDayAgo = current.minusDays(day);
			}
			
			if(dayType == 1) {
				if (targetSize > 0) dashList = dao.selectDashBarDrawDate(input);
				
				for (Map<String, Object> dayMap : dashList) {
					String dateKey = (String) dayMap.get("report_timestamp");
		            Object dataTypesObj = dayMap.get("data_types");
					
		            List<Map<String, Object>> dataTypes;
                    if (dataTypesObj instanceof String) {
                        dataTypes = objectMapper.readValue((String) dataTypesObj, objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));
                    } else {
                        dataTypes = (List<Map<String, Object>>) dataTypesObj;
                    }
		            
                    Map<String, Long> patternSum = dateToPatternSum.computeIfAbsent(dateKey, k -> new HashMap<>());
                    
                    for (Map<String, Object> dataType : dataTypes) {
                        String patternNm = (String) dataType.get("PATTERN_NM");
                        Long matchCnt = ((Number) dataType.get("MATCH_CNT")).longValue();

                        // PATTERN_NM별 MATCH_CNT 합산
                        patternSum.merge(patternNm, matchCnt, Long::sum);
                    }
				
				}
				
				for (LocalDate date = oneDayAgo; !date.isAfter(current); date = date.plusDays(1)) {
		            Map<String, Object> map = new HashMap<>();
		            String date_key = date.getYear() + "-" +String.format("%02d", date.getMonthValue()) + "-"  + String.format("%02d", date.getDayOfMonth());

		            map.put("date_key", date_key);
		            map.put("target_id", "");
		            map.put("ap_no", 0);

		            // dashList에서 해당 date_key에 맞는 데이터 찾기
		            for (Map<String, Object> dayMap : dashList) {
		                if (dayMap.get("report_timestamp").equals(date_key)) {
		                    map.put("target_id", dayMap.get("target_id"));
		                    map.put("ap_no", dayMap.get("ap_no"));
		                    break;
		                }
		            }

		            // 합산된 data_types 추가
		            Map<String, Long> patternSum = dateToPatternSum.getOrDefault(date_key, new HashMap<>());
		            map.put("data_types", patternSum);

		            resultList.add(map);
		        }
				
			}else {
				if (targetSize > 0)  dashList = dao.selectDashBarDrawMonthDate(input);
				
				for (Map<String, Object> dayMap : dashList) {
					String dateKey = (String) dayMap.get("report_timestamp");
		            Object dataTypesObj = dayMap.get("data_types");
					
		            List<Map<String, Object>> dataTypes;
                    if (dataTypesObj instanceof String) {
                        dataTypes = objectMapper.readValue((String) dataTypesObj, objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));
                    } else {
                        dataTypes = (List<Map<String, Object>>) dataTypesObj;
                    }
		            
                    Map<String, Long> patternSum = dateToPatternSum.computeIfAbsent(dateKey, k -> new HashMap<>());
                    
                    for (Map<String, Object> dataType : dataTypes) {
                        String patternNm = (String) dataType.get("PATTERN_NM");
                        Long matchCnt = ((Number) dataType.get("MATCH_CNT")).longValue();

                        // PATTERN_NM별 MATCH_CNT 합산
                        patternSum.merge(patternNm, matchCnt, Long::sum);
                    }
				
				}
				
				for (LocalDate date = oneDayAgo; !date.isAfter(current); date = date.plusMonths(1)) {
		            Map<String, Object> map = new HashMap<>();
		            String dateKey = date.getYear() + "-" + String.format("%02d", date.getMonthValue());

		            map.put("date_key", dateKey);
		            map.put("target_id", "");
		            map.put("ap_no", 0);

		            // dashList에서 해당 date_key에 맞는 데이터 찾기
		            for (Map<String, Object> dayMap : dashList) {
		                if (dayMap.get("report_timestamp").equals(dateKey)) {
		                    map.put("target_id", dayMap.get("target_id"));
		                    map.put("ap_no", dayMap.get("ap_no"));
		                    break;
		                }
		            }

		            // 합산된 data_types 추가
		            Map<String, Long> patternSum = dateToPatternSum.getOrDefault(dateKey, new HashMap<>());
		            map.put("data_types", patternSum);

		            resultList.add(map);
		        }
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("e :: " +e);
		}
		
		return resultList;
	}
	
	@Override
	public Map<String, Object> selectDetectionTable(HttpServletRequest request) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<>();
		try {
			String user_no = SessionUtil.getSession("memberSession", "USER_NO");
			String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
			
			Map<String, Object> input = new HashMap<>();
			input.put("user_no", user_no);
			input.put("user_grade", user_grade);
			
			resultMap = dao.selectDetectionTable(input);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("e :: " +e);
		}
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> selectApprovalTable(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			String user_no = SessionUtil.getSession("memberSession", "USER_NO");
			String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
			
			Map<String, Object> input = new HashMap<>();
			input.put("user_no", user_no);
			input.put("user_grade", user_grade);
			
			resultMap = dao.selectApprovalTable(input);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("e :: " +e);
		}
		
		return resultMap;
	}
	
	@Override
	public List<Object> selectDashPCJstreePopup(HttpServletRequest request) throws Exception {
		
		String target_id = request.getParameter("target_id");
		String id = request.getParameter("id");
		Map<String, Object> input = new HashMap<>();
		
		input.put("target_id", target_id);
		input.put("id", id);
		return dao.selectDashPCJstreePopup(input);
	}
	
	// 관리자 dashboard pc progress
	@Override
	public List<Object> selectSystemCurrentProgressPC(HttpServletRequest request) {
		logger.info("selectSystemCurrentProgressPC check");
		String id = request.getParameter("id");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		
		Map<String, Object> input = new HashMap<>();
		input.put("id", id);
		input.put("fromDate", fromDate);
		input.put("toDate", toDate);
		
		return dao.selectSystemCurrentProgressPC(input);
	}
	
	@Override
	public List<Object> selectSystemCurrentProgressOneDrive(HttpServletRequest request) {
		logger.info("selectSystemCurrentProgressOneDrive check");
		String id = request.getParameter("id");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		
		Map<String, Object> input = new HashMap<>();
		input.put("id", id);
		input.put("fromDate", fromDate);
		input.put("toDate", toDate);
		
		return dao.selectSystemCurrentProgressOneDrive(input);
	}
	
	@Override
	public List<Map<String, Object>> selectDashPersonalProgressPc(HttpServletRequest request, List<String> targetList) {
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		String ap = request.getParameter("ap");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		
		Map<String, Object> input = new HashMap<>();
		
		input.put("targetList", targetList);
		input.put("id", id);
		input.put("type", type);
		input.put("ap", ap);
		input.put("fromDate", fromDate);
		input.put("toDate", toDate);
		
		logger.info("input >> " + input);
		
		return dao.selectDashPersonalProgressPc(input);
	}
	
	@Override
	public List<Map<String, Object>> selectDashPersonalProgressOneDrive(HttpServletRequest request, List<String> targetList) {
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		String ap = request.getParameter("ap");
		String date = request.getParameter("date");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		
		Map<String, Object> input = new HashMap<>();
		
		input.put("targetList", targetList);
		input.put("id", id);
		input.put("type", type);
		input.put("ap", ap);
		input.put("date", date);
		input.put("fromDate", fromDate);
		input.put("toDate", toDate);
		
		logger.info("input >> " + input);
		
		return dao.selectDashPersonalProgressOneDrive(input);
	}
	

	@Override
	public Map<String, Object> selectNoticePop(Map<String, Object> map) throws Exception {
		return dao.selectNoticePop(map);
	}

	@Override
	public Map<String, Object> selectDashAllData(HttpServletRequest request) throws Exception {
		
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String target_id = request.getParameter("target_id");
		String ap_no = request.getParameter("ap_no");
		
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> requestMap = new HashMap<>();
		
		try {
			List<Map<String, Object>> dashList = dao.selectDashAllData(requestMap);
			
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "조회 성공");
		} catch (Exception e) {
			logger.info("error :: " + e.getLocalizedMessage());
			 e.printStackTrace();
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "데이터 조회에 실패 하였습니다.");
		}
		
		
		
		
		
		
		// TODO Auto-generated method stub
		return null;
	}
}
