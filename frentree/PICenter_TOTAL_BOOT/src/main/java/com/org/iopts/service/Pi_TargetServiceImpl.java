package com.org.iopts.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonArray;
import com.org.iopts.dao.Pi_TargetDAO;
import com.org.iopts.dao.Pi_UserDAO;
import com.org.iopts.dto.Pi_TargetVO;
import com.org.iopts.util.SessionUtil;

import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

@PropertySource("classpath:/property/config.properties")

@Service
public class Pi_TargetServiceImpl implements Pi_TargetService {

	private static final Logger logger = LoggerFactory.getLogger(Pi_TargetServiceImpl.class);

	@Resource(name = "Pi_TargetDAO")
	private Pi_TargetDAO dao;

	@Value("${saveAttchPath}")
	private String saveAttchPath;

	@Inject
	private Pi_UserDAO userDao;

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

		String target = request.getParameter("target");

		return dao.selectTargetUserList(target);
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

	@Override
	public List<Map<String, Object>> selectNoticeList(Map<String, Object> map) throws Exception {
		return dao.selectNoticeList(map);
	}

	@Override
	public List<Map<String, Object>> getTargetList(Map<String, Object> map) throws Exception {
		return dao.getTargetList(map);
	}

	@Override
	public Map<String, Object> getGroupDetails(Map<String, Object> map) throws Exception {
		return dao.getGroupDetails(map);
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
		
		return null;
	}
	
	@Override
	public List<Map<String, Object>> selectRmTargetList(HttpServletRequest request) {
		String target_id = request.getParameter("target_id");
		int ap_no = Integer.parseInt(request.getParameter("ap_no"));
		
		Map<String, Object> searchMap = new HashMap<String, Object>();
		
		searchMap.put("target_id", target_id);
		searchMap.put("ap_no", ap_no);
		
		logger.info("request >>> " + searchMap);
		
		return dao.selectRmTargetList(searchMap);
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
	public List<Map<String, Object>> getExceptionList(HttpServletRequest request) throws Exception {
		return dao.getExceptionList();
	}
	
	
	@Override
	public List<Map<String, Object>> selectSKTManagerList(HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		String user_no = request.getParameter("user_no");
		String user_name = request.getParameter("user_name");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_no", user_no);
		map.put("user_name", user_name);

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
	public List<Map<String, Object>> selectPcManagerList(HttpServletRequest request) throws Exception {
		return dao.selectPCManagerList();
	}

	@Override
	public List<Map<String, Object>> selectVersionList(HttpServletRequest request) throws Exception {
		return dao.selectVersionList();
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
		String name = request.getParameter("name");

		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("groupNm", groupNm);
		searchMap.put("hostNm", hostNm);
		searchMap.put("serviceNm", serviceNm);
		searchMap.put("user_no", user_no);
		searchMap.put("user_grade", user_grade);
		searchMap.put("userIP", userIP);
		searchMap.put("id", id);
		searchMap.put("name", name);

		logger.info("adsfadfafa >>> " + name);
		logger.info(searchMap.toString());
		return dao.selectPCTargetUser(searchMap);
	}
	
}

