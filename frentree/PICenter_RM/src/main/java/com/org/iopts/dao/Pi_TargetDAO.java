package com.org.iopts.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.org.iopts.dto.Pi_TargetVO;

@Repository("Pi_TargetDAO")
public class Pi_TargetDAO {

	private static final Logger logger = LoggerFactory.getLogger(Pi_TargetDAO.class);

	@Autowired
    private SqlSession sqlSession;

	private static final String Namespace = "target";
	
	public List<Map<String, Object>> selectTargetManage() throws Exception {
		
		return sqlSession.selectList(Namespace+".selectTargetManage");
	}

	public List<Map<String, Object>> selectTargetList(String host) throws Exception {
		
		return sqlSession.selectList(Namespace+".selectTargetList", host);
	}

	public List<Map<String, Object>> selectTargetUser(Map<String, Object> map) throws Exception {
		
		return sqlSession.selectList(Namespace+".selectTargetUser", map);
	}

	public void deleteTargetUser(Map<String, Object> map) throws Exception {
		
		sqlSession.delete(Namespace+".deleteTargetUser", map);
	}
	
	public void registTargetUser(Map<String, Object> map) throws Exception {
		sqlSession.insert(Namespace+".registTargetUser", map);
	}
	
	public void registTargetUserBySelect(Map<String, Object> map) throws Exception {
		sqlSession.insert(Namespace+".registTargetUserBySelect", map);
	}
	
	public int insertTarget(List<Pi_TargetVO> list) throws Exception {
		int ret = 0;
		ret = sqlSession.insert(Namespace+".insertTargets", list);
		return ret;
	}

	public List<Map<String, Object>> selectUserTargetList(Map<String, Object> searchMap) throws Exception {
		
		return sqlSession.selectList(Namespace+".selectUserTargetList", searchMap);
	}
	
	public List<Map<String, Object>> selectServerList(Map<String, Object> searchMap) throws Exception {
		
		return sqlSession.selectList(Namespace+".selectServerList", searchMap);
	}

	public List<Map<String, Object>> selectTargetUserList(String target) throws Exception {
		
		return sqlSession.selectList(Namespace+".selectTargetUserList", target);
	}

	public List<Map<String, Object>> selectServerFileTopN(Map<String, Object> searchMap) {

		return sqlSession.selectList(Namespace + ".selectServerFileTopN", searchMap);
	}

	public List<Map<String, Object>> selectAdminServerFileTopN(Map<String, Object> searchMap) {
		return sqlSession.selectList(Namespace + ".selectAdminServerFileTopN", searchMap);
	}
	
	// DMZ Info Save
	public void saveDmzInfo(HashMap<String, Object> params) {
		sqlSession.insert(Namespace+".saveDmzInfo", params);
	}

	public HashMap<String, Object> selectTargetById(HashMap<String, Object> params) {
		return sqlSession.selectOne(Namespace+".selectTargetById", params);
	}
	
	public List<Map<String, Object>> selectAllUseTarget() {
		return sqlSession.selectList(Namespace+".selectAllUseTarget");
	}

	public List<Map<String, Object>> selectGroupList(Map<String, Object> map) throws SQLException {
		return sqlSession.selectList(Namespace+".selectGroupList", map);
	}

	public List<Map<String, Object>> getTargetList(Map<String, Object> map) {
		return sqlSession.selectList(Namespace+".getTargetList", map);
	}

	public Map<String, Object> getGroupDetails(Map<String, Object> map) {
		return sqlSession.selectOne(Namespace+".getGroupDetails", map);
	}

	public List<Map<String, Object>> selectAllUseTarget(Map<String, Object> dbMap) {
		return sqlSession.selectList(Namespace+".selectAllUseTarget", dbMap);
	}
	
	public List<Map<String, Object>> selectUserGroupList(Map<String, Object> map) {
		return sqlSession.selectList(Namespace+".selectUserGroupList", map);
	}
	
	public List<Map<String, Object>> selectNoticeList(Map<String, Object> map) {
		return sqlSession.selectList(Namespace+".selectNoticeList", map);
	}
	
	public List<Map<String, Object>> selectRmTargetList(Map<String, Object> searchMap) {
		return sqlSession.selectList(Namespace + ".selectRmTargetList", searchMap);
	}
	
	public List<Map<String, Object>> selectPCTargetUser(Map<String, Object> searchMap) {
		return sqlSession.selectList(Namespace + ".selectPCTargetUser", searchMap);
	}
	

	public List<Map<String, Object>> getExceptionList() throws Exception {
		return sqlSession.selectList(Namespace+".getExceptionList");
	}
	
	public List<Map<String, Object>> selectSKTManagerList(Map<String, Object> map) {
		return sqlSession.selectList(Namespace + ".selectSKTManagerList", map);
	}
	
	public List<Map<String, Object>> selectAddSKTManagerList(Map<String, Object> map) {
		return sqlSession.selectList(Namespace + ".selectAddSKTManagerList", map);
	}

	public List<Map<String, Object>> selectPCManagerList() {
		return sqlSession.selectList(Namespace + ".selectPCManagerList");
	}

	public List<Map<String, Object>> selectVersionList() {
		return sqlSession.selectList(Namespace + ".selectVersionList");
	}
	

}
