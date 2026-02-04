package com.org.iopts.exception.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.iopts.exception.dao.piDetectionListDAO;
import com.org.iopts.exception.service.piDetectionListService;
import com.org.iopts.util.SessionUtil;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class piDetectionListServiceImple implements piDetectionListService {

	private static Logger log = LoggerFactory.getLogger(piDetectionListServiceImple.class);

	@Inject
	private piDetectionListDAO dao;
	
	@Override 
	public List<HashMap<String, Object>> selectFindSubpath(HashMap<String, Object> params) throws SQLException {
		
		String user_no = SessionUtil.getSession("memberSession", "USER_NO"); 
		params.put("user_no"  , user_no);
		String location = params.get("location").toString();
		location = location.replaceAll("\\\\", "\\\\\\\\");
		
		params.put("location", location);
	
		List<Integer> patternList = dao.queryCustomDataTypesCnt(); 
		params.put("patternList", patternList); 

		List<HashMap<String, Object>>findMap = dao.selectFindSubpath2(params);

		return findMap;
	}
	
	@Override
	public List<HashMap<String, Object>> subpathSelect(HashMap<String, Object> params) throws SQLException {
		
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		params.put("user_no"  , user_no);

		List<HashMap<String, Object>>findMap = dao.subpathSelect(params);

		return findMap;
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
	public void registProcess(HashMap<String, Object> params, HashMap<String, Object> groupMap) {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		List<String> deletionList = (List<String>)params.get("deletionList");

		for (int i = 0; i < deletionList.size(); i++) {
			params.put("user_no", user_no);
			params.put("hash_id", deletionList.get(i));
			params.put("data_processing_group_idx", groupMap.get("idx"));

			dao.registProcess(params);
		}
	}

	@Override
	public List<HashMap<String, Object>> personalApprovalData(HashMap<String, Object> params) throws SQLException {
		String idx = params.get("idx").toString();
		params.put("idx", idx); 
 
		List<HashMap<String, Object>>findMap = dao.personalApprovalData(params);

		return findMap;
	}
	
	@Override
	public List<Map<String, Object>> queryCustomDataTypes() throws SQLException {

		List<Map<String, Object>> map = dao.queryCustomDataTypes();

		return map;
	}
	
	@Override
	public List<Map<String, Object>> queryMatchDetail() throws SQLException {
		
		List<Map<String, Object>> map = dao.queryMatchDetail();
		
		return map;
	}
	
}
