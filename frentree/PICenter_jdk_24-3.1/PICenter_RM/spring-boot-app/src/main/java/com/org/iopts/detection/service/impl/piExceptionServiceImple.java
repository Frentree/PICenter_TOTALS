package com.org.iopts.detection.service.impl;

import java.io.Reader;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.iopts.detection.dao.piExceptionDAO;
import com.org.iopts.detection.service.piExceptionService;
import com.org.iopts.util.ReconUtil;
import com.org.iopts.util.SessionUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service("exceptionService")
@Transactional
public class piExceptionServiceImple implements piExceptionService {
	
	private static Logger log = LoggerFactory.getLogger(piExceptionServiceImple.class);

	@Inject
	private piExceptionDAO exceptionDao;
	
	@Value("${user.key}")
	private String key;
	
	@Override
	public List<HashMap<String, Object>> selectExceptionList(HashMap<String, Object> params) throws Exception {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		params.put("user_no", user_no);

		return exceptionDao.selectExceptionList(params);
	}

	@Override
	public List<HashMap<String, Object>> selectExeptionPath(HashMap<String, Object> params) throws Exception {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		params.put("user_no", user_no);

		return exceptionDao.selectExeptionPath(params);
	}

	@Override
	public Map<String, Object> selectDocuNum(HashMap<String, Object> params) throws Exception {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		params.put("user_no", user_no);

		Map<String, Object> memberMap = exceptionDao.selectDocuNum(params);
		log.info("selectDocuNum : " + memberMap);

		return memberMap;
	}

	@Override
	public HashMap<String, Object> registPathExceptionCharge(HashMap<String, Object> params) throws Exception
	{
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		params.put("user_no", user_no);

		exceptionDao.registPathExceptionCharge(params);

		return params;
	}

	@Override
	public void updateExcepStatus(HashMap<String, Object> params, HashMap<String, Object> chargeMap) throws Exception
	{
		log.info("updateProcessStatus 로그체크 1");

		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		List<String> idxList = (List<String>)params.get("idxList");

		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < idxList.size(); i++) 
		{
			map.put("idx", idxList.get(i));
			map.put("user_no", user_no);
			map.put("ok_user_no", params.get("ok_user_no"));
			map.put("apprType", params.get("apprType"));
			map.put("path_ex_charge_id", chargeMap.get("path_ex_charge_id"));
			map.put("comment", params.get("comment"));

			exceptionDao.updateExceptionGroupStatus(map);
		}
	}

	@Override
	public List<HashMap<String, Object>> exceptionApprovalAllListData(HashMap<String, Object> params) throws Exception {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");

		params.put("user_no", user_no);

		return exceptionDao.exceptionApprovalAllListData(params);
	}

	@Override
	public List<HashMap<String, Object>> exceptionApprovalListData(HashMap<String, Object> params) throws Exception {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");

		params.put("user_no", user_no);
		params.put("user_grade", user_grade);

		return exceptionDao.exceptionApprovalListData(params);
	}
	
	/**
	 * 경로 예외 결재 리스트 - 재검색 선택 Target 정보
	 */
	@Override
	public List<HashMap<String, Object>> selectReScanTarget(HashMap<String, Object> params) throws Exception
	{
		List<String> group_list = (List<String>)params.get("groupList");
		params.put("group_list", group_list);

		return exceptionDao.selectReScanTarget(params);
	}
	
	/**
	 * 경로 예외 재검색 - 재검색 컬럼 업데이트
	 */
	@Override
	public HashMap<String, Object> updateReScanGroup(HashMap<String, Object> params) throws Exception
	{
		List<String> group_list = (List<String>)params.get("groupList");
		params.put("group_list", group_list);
		HashMap<String, Object> resultMap = new HashMap<>();
		try {
			exceptionDao.updateReScanGroup(params);
			resultMap.put("result", "1");
		}catch (Exception e) {
			resultMap.put("result", "0");
		}
		
		return resultMap;
	}

	@Override
	public List<HashMap<String, Object>> selectExceptionGroupPath(HashMap<String, Object> params) throws Exception
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

		return exceptionDao.selectExceptionGroupPath(params);
	}

	/**
	 * 결재 리스트 - 결재
	 */
	@Override
	public void updateExcepApproval(HashMap<String, Object> params, String recon_id, String recon_password, String recon_url, String api_ver) throws Exception
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
		params.put("charge_id_list", chargeIdList);
		List<HashMap<String, Object>> exceptionList = exceptionDao.selectExceptionGroupPath(params);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < chargeIdList.size(); i++) 
		{
			map.put("chargeIdList", chargeIdList.get(i));
			map.put("user_no",  user_no);
			map.put("ex_user_no",  "ex_" + user_no);
			map.put("apprType", params.get("apprType"));
			map.put("reason",   params.get("reason"));

			exceptionDao.updateExceptionApproval(map);
			
		}
		
		if(params.get("apprType").equals("E")) {	// 결재 승인되면
			
			// 승인된 경로들을 Json 형식으로 만드는 과정
			log.info("getMatchObjects doc : " + "/" + api_ver + "/filters");
			List<String> filterData = new ArrayList<>();
			String expression = "";
			JsonArray exArray = new JsonArray();
			JsonObject exInfo = new JsonObject();
			for (int i = 0; i < exceptionList.size(); i++) {
			    if (i != 0) {
			        if (exceptionList.get(i).get("target_id").toString().equals(exceptionList.get(i-1).get("target_id").toString())) { // 타겟 아이디가 중복이면
			            expression += "|" + exceptionList.get(i).get("path_ex").toString();
			        } else { // 타겟아이디가 달라지면
			            exInfo.addProperty("expression", expression);
			            exArray.add(exInfo);
			            filterData.add(exArray.toString());
			            log.info("getMatchObjects doc : " + i + " : " + exArray.toString());
			            exInfo = new JsonObject(); // json Object 클리어
			            exArray = new JsonArray(); // json Array 클리어

			            exInfo.addProperty("apply_to", exceptionList.get(i).get("target_id").toString());
			            exInfo.addProperty("type", "exclude_prefix");
			            expression = exceptionList.get(i).get("path_ex").toString();
			        }
			    } else { // i가 0이면 추가 해야 된다.
			        exInfo.addProperty("apply_to", exceptionList.get(i).get("target_id").toString());
			        exInfo.addProperty("type", "exclude_prefix");
			        expression = exceptionList.get(i).get("path_ex").toString();
			    }

			    if ((i + 1) == exceptionList.size()) { // 반복문의 마지막 리스트
			        exInfo.addProperty("expression", expression);
			        exArray.add(exInfo);
			        log.info("getMatchObjects doc : " + i + " : " + exArray.toString());
			        filterData.add(exArray.toString());
			    }
			}
			
			/* 경로 예외 API */
			Map<String, Object> resultMap = new HashMap<String, Object>();
			ReconUtil reconUtil = new ReconUtil();
			Map<String, Object> httpsResponse = null;
			log.info("filterData.size() : " + filterData.size());
			try {
				for(int i = 0; i < filterData.size(); i++) {
					httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + api_ver + "/filters", "POST", filterData.get(i));
				}
			} catch (ProtocolException e) {
				log.error("Error: " + e.getMessage(), e);
				resultMap.put("resultCode", -1);
				resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
			}
			
			for(int i = 0; i < exceptionList.size(); i++) {  // 경로 업데이트
				
				map.put("target_id", exceptionList.get(i).get("target_id"));
				map.put("user_no",  exceptionList.get(i).get("user_no"));
				map.put("ex_user_no",  "ex_" + exceptionList.get(i).get("user_no"));
				map.put("path", exceptionList.get(i).get("path_ex"));

				exceptionDao.updateExceptionChange(map);
			}
		}
		
	}
	
	@Override
	public List<Map<String, Object>> glovalFilterDetail(HttpServletRequest request) throws Exception{
		String status = request.getParameter("status");
		String target_status = request.getParameter("target_status");
		String name = request.getParameter("host_name");
		String user_name = request.getParameter("user_name");
		String path = request.getParameter("path");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		map.put("target_status", target_status);
		map.put("name", name);
		map.put("user_name", user_name);
		map.put("path", path);
		map.put("fromDate", fromDate);
		map.put("toDate", toDate);
		map.put("key", key);
		
		return exceptionDao.glovalFilterDetail(map);
	}
	
	@Override
	public Map<String, Object> insertGlovalFilterDetail(HttpServletRequest request) throws Exception {

		JsonArray exArray = new JsonArray();
		JsonObject exInfo = new JsonObject();
		
		List<String> filterData = new ArrayList<>();
		String path_ex = request.getParameter("path_ex");
		String target_id = request.getParameter("target_id");
		String status = request.getParameter("status");
		String type = request.getParameter("type");
		String user_no = request.getParameter("user_no");
		String comment = request.getParameter("comment");
		String regExp = request.getParameter("regExp");
		
		exInfo.addProperty("type", type);
		if (type.equals("ignore_expression") && regExp.equals("Y")) {
		    exInfo.addProperty("expression", "@~" + path_ex);
		} else {
		    exInfo.addProperty("expression", path_ex);
		}
		if(!target_id.equals("all")) {
			exInfo.addProperty("apply_to", target_id);
		}
		
		exArray.add(exInfo);
		filterData.add(exArray.toString());
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ReconUtil reconUtil = new ReconUtil();
		Map<String, Object> httpsResponse = null;
		Gson gson = new Gson();
		try {
			
			int ap_no = Integer.parseInt(status);
			
			Properties properties = new Properties();
			String resource = "/property/config.properties";
			Reader reader = Resources.getResourceAsReader(resource);
			
			properties.load(reader);
			
			String api_ver = properties.getProperty("recon.api.version");
			
			String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no+1)) ;
			String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no+1)) ;
			String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no+1)) ;
			
			httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + api_ver + "/filters", "POST", exArray.toString());
			
			int resultCode = Integer.parseInt(httpsResponse.get("HttpsResponseCode").toString());
			
			if (resultCode == 201) {
			    JsonObject filters = gson.fromJson(httpsResponse.get("HttpsResponseDataMessage").toString(), JsonObject.class);

			    String filter_id = filters.get("id").getAsString();

			    Map<String, Object> filterMap = new HashMap<>();

			    filterMap.put("filter_id", filter_id);
			    if (!target_id.equals("all")) {
			        filterMap.put("target_id", target_id);
			    }
			    filterMap.put("ap_no", ap_no);
			    filterMap.put("type", type);
			    filterMap.put("path_ex", path_ex);
			    filterMap.put("create_user", user_no);
			    filterMap.put("comment", comment);
				filterMap.put("regExp", regExp);
				filterMap.put("key", key);
				
				resultMap.put("resultCode", 0);
				resultMap.put("resultMessage", "예외 처리 성공");
				
				exceptionDao.insertGlovalFilterDetail(filterMap);
			}else {
				resultMap.put("resultCode", -2);
				resultMap.put("resultMessage", "예외 처리 실패");
			}
			
			
		}catch (Exception e) {
			log.error("Error: " + e.getMessage(), e);
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> updateGlovalFilterDetail(HttpServletRequest request) throws Exception {
		
		JsonArray exArray = new JsonArray();
		JsonObject exInfo = new JsonObject();
		
		List<String> filterData = new ArrayList<>();
		String filter_id = request.getParameter("filter_id");
		String path_ex = request.getParameter("path_ex");
		String target_id = request.getParameter("target_id");
		String type = request.getParameter("type");
		String status = request.getParameter("status");
		String comment = request.getParameter("comment");
		String regExp = request.getParameter("regExp");
		
		exInfo.addProperty("expression", path_ex);
        exInfo.addProperty("type", type); 
		if(target_id != null && !target_id.equals("all") && !target_id.equals("")) {
			exInfo.addProperty("apply_to", target_id);
		}
		
		if (type.equals("ignore_expression") && regExp.equals("Y")) {
		    exInfo.addProperty("expression", "@~" + path_ex);
		} else {
		    exInfo.addProperty("expression", path_ex);
		}
		
		exArray.add(exInfo);
		filterData.add(exArray.toString());
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ReconUtil reconUtil = new ReconUtil();
		Map<String, Object> httpsResponse = null;
		
		try {
			
			int ap_no = Integer.parseInt(status);
			
			Properties properties = new Properties();
			String resource = "/property/config.properties";
			Reader reader = Resources.getResourceAsReader(resource);
			
			properties.load(reader);
			
			String api_ver = properties.getProperty("recon.api.version");
			
			String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no+1)) ;
			String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no+1)) ;
			String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no+1)) ;
			 
			httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + api_ver + "/filters/"+filter_id, "PUT", exArray.toString());		
			
			int resultCode = Integer.parseInt(httpsResponse.get("HttpsResponseCode").toString());
			log.info("resultCode : " + resultCode);
			
			if(resultCode == 204) {
				
				Map<String, Object> filterMap = new HashMap<>();
				filterMap.put("filter_id", filter_id);
				if(!target_id.equals("all")) {
					filterMap.put("target_id", target_id);
				}
				filterMap.put("ap_no", ap_no);
				filterMap.put("path_ex", path_ex);
				filterMap.put("comment", comment);
				filterMap.put("regExp", regExp);
				filterMap.put("key", key);
				
				resultMap.put("resultCode", 0);
				resultMap.put("resultMessage", "예외 처리 성공");
				
				exceptionDao.updateGlovalFilterDetail(filterMap);
			}else {
				resultMap.put("resultCode", -2);
				resultMap.put("resultMessage", "예외 처리 실패");
			}
			
			
		}catch (Exception e) {
			log.error("Error: " + e.getMessage(), e);
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
		}
		return resultMap;
	}

	public Map<String, Object> deleteGlovalFilterDetail(HttpServletRequest request) throws Exception {
		
		List<String> filterData = new ArrayList<>();
		String filter_id = request.getParameter("filter_id");
		
		int ap_no = Integer.parseInt(request.getParameter("ap_no"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ReconUtil reconUtil = new ReconUtil();
		Map<String, Object> httpsResponse = null;
		
		try {
			
			Properties properties = new Properties();
			String resource = "/property/config.properties";
			Reader reader = Resources.getResourceAsReader(resource);
			
			properties.load(reader);
			
			String api_ver = properties.getProperty("recon.api.version");
			
			String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no+1)) ;
			String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no+1)) ;
			String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no+1)) ;
			
			httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + api_ver + "/filters/"+filter_id, "DELETE", null);		
			
			int resultCode = Integer.parseInt(httpsResponse.get("HttpsResponseCode").toString());
			
			if(resultCode == 204 || resultCode == 200) {
				
				Map<String, Object> filterMap = new HashMap<>();
				
				filterMap.put("filter_id", filter_id);
				filterMap.put("ap_no", ap_no);
				
				resultMap.put("resultCode", 0);
				resultMap.put("resultMessage", "예외 삭제 성공");
				
				exceptionDao.deleteGlovalFilterDetail(filterMap);
			}else {
				resultMap.put("resultCode", -2);
				resultMap.put("resultMessage", "예외 삭제 실패");
			}
			
		}catch (Exception e) {
			log.error("Error: " + e.getMessage(), e);
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
		}
		return resultMap;
	}


}