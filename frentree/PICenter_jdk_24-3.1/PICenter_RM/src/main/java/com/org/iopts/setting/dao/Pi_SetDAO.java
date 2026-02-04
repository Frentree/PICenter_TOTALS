package com.org.iopts.setting.dao;

import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.org.iopts.dto.MemberVO;

@Repository
public class Pi_SetDAO {

	static final Logger logger = LoggerFactory.getLogger(Pi_SetDAO.class);

	@Autowired
	@Resource(name = "sqlSession")
	private SqlSession sqlSession;

	static final String Namespace = "com.org.iopts.mapper.SetMapper";

	public List<Map<Object, Object>> selectSetting(int type) throws Exception {
		return sqlSession.selectList(Namespace + ".selectSetting", type);
	}
	
	public List<Map<Object, Object>> selectApprovalStatus() throws Exception {
		return sqlSession.selectList(Namespace + ".selectApprovalStatus");
	}
	
	public List<Map<Object, Object>> selectApprovalStatusAll() throws Exception {
		return sqlSession.selectList(Namespace + ".selectApprovalStatusAll");
	}

	public List<Map<String, Object>> patternList() {
		return sqlSession.selectList(Namespace + ".patternList");
	}

	public List<Map<String, Object>> selectbatchData(String status) {
		return sqlSession.selectList(Namespace + ".selectbatchData");
	}

	public Map<String, Object> checkPageGrade(Map<Object, Object> resultMap) {
		return sqlSession.selectOne(Namespace + ".checkPageGrade",resultMap);
	}

	public int updateCustomPattern(Map<String, Object> requestMap) {
		return sqlSession.update(Namespace + ".updateCustomPattern",requestMap);
	}

	public int deleteCustomPattern(Map<String, Object> requestMap) {
		return sqlSession.delete(Namespace + ".deleteCustomPattern",requestMap);
	}
	
	public int insertCustomPattern(Map<String, Object> requestMap) {
		return sqlSession.insert(Namespace + ".insertCustomPattern",requestMap);
	}

	public void customPatternChagne(Map<String, Object> patternMap) {
		sqlSession.update(Namespace + ".customPatternChagne",patternMap);
	}

	public List<Map<String, Object>> nameList(String string) {
		return sqlSession.selectList(Namespace + ".nameList", string);
	}

	public int nameListUpdate(Map<String, Object> requestMap) {
		return sqlSession.update(Namespace + ".nameListUpdate",requestMap);
	}

	public int nameListDelete(Map<String, Object> requestMap) {
		return sqlSession.delete(Namespace + ".nameListDelete",requestMap);
	}

	public int nameListCreate(Map<String, Object> requestMap) {
		return sqlSession.insert(Namespace + ".nameListCreate",requestMap);
	}
	
	public List<Map<String, Object>> conDataList() {
		return sqlSession.selectList(Namespace + ".conDataList");
	}

	public int ConListUpdate(Map<String, Object> requestMap) {
		return sqlSession.update(Namespace + ".ConListUpdate",requestMap);
	}

	public Map<String, Object> selectContentList(String string) {
		return sqlSession.selectOne(Namespace + ".selectContentList", string);
	}
	
	public List<Map<String, Object>> selectGroupApprovalUser(String string) {
		return sqlSession.selectList(Namespace + ".selectGroupApprovalUser", string);
	}

	public List<Map<String, Object>> groupApprovalList() {
		return sqlSession.selectList(Namespace + ".groupApprovalList");
	}
	
	public List<Map<String, Object>> approvalList() {
		return sqlSession.selectList(Namespace + ".approvalList");
	}
//	
	public Map<String, Object> selectMailBatchMax(Map<String, Object> map) {
		return sqlSession.selectOne(Namespace + ".selectMailBatchMax", map);
	}
	
	public void updateBatchSchedule(Map<String, Object> resultMap) {
		sqlSession.update(Namespace+".updateBatchSchedule", resultMap);
	}
	
	public void insertApprovalUser(Map<String, Object> map) {
		sqlSession.insert(Namespace + ".insertApprovalUser", map);
	}
	
	
	public List<Map<String, Object>> getApprovalFlag() {
		return sqlSession.selectList(Namespace + ".getTrueFalseFlag");
	}
	
	public List<Map<String, Object>> getProcessingFlag(Map<String, Object> requestMap) {
		return sqlSession.selectList(Namespace + ".getProcessingFlag", requestMap);
	}

	public void updateProcessingFlag(Map<String, Object> map) throws Exception{
		sqlSession.update(Namespace + ".updateProcessingFlag", map);
	}
	
	public void deleteProcessingFlag(Map<String, Object> map) throws Exception{
		sqlSession.delete(Namespace + ".deleteProcessingFlag", map);
	}

	public void insertProcessingFlag(Map<String, Object> map) throws Exception {
		sqlSession.insert(Namespace + ".insertProcessingFlag", map);
	}

	public void updateProcessingFlagEnable(Map<String, Object> map) throws Exception {
		sqlSession.update(Namespace + ".updateProcessingFlagEnable", map);
	}

	public List<Map<String, Object>> getExceptionFlag() {
		return sqlSession.selectList(Namespace + ".getExceptionFlag");
	}

	public void updateExceptionFlag(Map<String, Object> map) throws Exception{
		sqlSession.update(Namespace + ".updateExceptionFlag", map);
	}
	
	public void deleteGroupApprovalUser(Map<String, Object> map) throws Exception{
		sqlSession.update(Namespace + ".deleteGroupApprovalUser", map);
	}
	
	public void insertGroupApprovalUser(Map<String, Object> map) throws Exception{
		sqlSession.update(Namespace + ".insertGroupApprovalUser", map);
	}

	public void insertExceptionFlag(Map<String, Object> map) {
		sqlSession.insert(Namespace + ".insertExceptionFlag", map);
	}
	
	public void insertBatchSchedule(Map<String, Object> resultMap) {
		sqlSession.insert(Namespace+".insertBatchSchedule", resultMap);
		
	}
	
	public List<Map<String, Object>> rollBackList() {
		return sqlSession.selectList(Namespace + ".rollBackList");
	}

	public int getSessionTimeout() {
		return sqlSession.selectOne(Namespace + ".getSessionTimeout");
	}

	public void insertBackUpFile(String fileName) {
		sqlSession.insert(Namespace+".insertBackUpFile", fileName);
	}

	public Map<String, Object> approvalAlert(HttpServletRequest request) {
		return sqlSession.selectOne(Namespace + ".approvalAlert");
	}

	public List<Map<String, Object>> reportHeaderList(Map<String, Object> requestMap) {
		System.out.println(requestMap);
		return sqlSession.selectList(Namespace + ".reportHeaderList", requestMap);
	}
	
	public Map<String, Object> selectTeamApprovalUserCnt(Map<String, Object> resultMap) {
		return sqlSession.selectOne(Namespace + ".selectTeamApprovalUserCnt", resultMap);
	}

	public List<Map<String, Object>> selectApprovalGroupCnt(Map<String, Object> resultMap) {
		return sqlSession.selectList(Namespace + ".selectApprovalGroupCnt", resultMap);
	}  
	
}
