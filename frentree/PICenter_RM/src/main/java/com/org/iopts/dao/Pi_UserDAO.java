package com.org.iopts.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Resource;

@Repository
public class Pi_UserDAO {

	static final Logger logger = LoggerFactory.getLogger(Pi_UserDAO.class);

	@Autowired
	@Resource(name = "sqlSession")
	private SqlSession sqlSession;

	static final String Namespace = "com.org.iopts.mapper.UserMapper";

	public Map<String, Object>  selectMember(Map<String, Object> map) throws Exception {

		return sqlSession.selectOne(Namespace + ".selectMember", map);
	}
	
	// 초기화 인증번호 발송
	public Map<String, Object> selectMemberNum(Map<String, Object> map) throws Exception {
		
		return sqlSession.selectOne(Namespace + ".selectMemberNum", map);
	}
	
	// 인증번호 확인
	public Map<String, Object> updateUserPwd(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne(Namespace + ".updateUserPwd", map);
	}

	// 초기화 비밀번호 변경
	public int changeResetPwd(Map<String, Object> map) {
		return sqlSession.insert(Namespace + ".changeResetPwd", map);
	}

	public Map<String, Object>  selectSSOMember(Map<String, Object> map) throws Exception {

		return sqlSession.selectOne(Namespace + ".selectSSOMember", map);
	}

	public Map<String, Object>  selectTeamManager(String boss_name) throws Exception {

		return sqlSession.selectOne(Namespace + ".selectTeamManager", boss_name);
	}
	
	public void insertLog(Map<String, Object> userLog) throws Exception {
		
		sqlSession.insert(Namespace + ".insertLog", userLog);
	}

	public List<Map<String, Object>> selectUserLogList(Map<String, Object> search) throws Exception {
		
		return sqlSession.selectList(Namespace + ".selectUserLogList",search);
	}

	public int changeAuthCharacter(Map<String, Object> map) throws Exception {
		
		return sqlSession.insert(Namespace + ".updatePassword", map);
	}
	
	public int updateLoginDate(Map<String, Object> map) throws Exception {
		
		return sqlSession.insert(Namespace + ".updateLoginDate", map);
	}
	
	// 장기 미사용 아이디 Lock
	public int updateLoginDateLock(Map<String, Object> map) throws Exception {
		
		return sqlSession.update(Namespace + ".updateLoginDateLock", map);
	}


	public String selectAccessIP(String user_no) throws Exception {

		return sqlSession.selectOne(Namespace + ".selectAccessIP", user_no);
	}
	
	public Map<String, Object> selectNotice() throws Exception {

		return sqlSession.selectOne(Namespace + ".selectNotice");
	}
	
	// 공지사항 목록 조회
	public List<Map<String, Object>> noticeList() {
		return sqlSession.selectList(Namespace + ".noticeList");
	}
	
	// 공지사항 등록
	public void noticeInsert(Map<String, Object> input) {
		sqlSession.insert(Namespace + ".noticeInsert", input);
	}
	

	public void noticeDelete(Map<String, Object> delete) {
		sqlSession.delete(Namespace + ".noticeDelete", delete);
	}

	
	public void noticeUpdate(Map<String, Object> update) throws Exception {
		sqlSession.update(Namespace + ".noticeUpdate", update);
	}
	
	public void changeNotice(Map<String, Object> input) throws Exception {
		
		sqlSession.insert(Namespace + ".changeNotice", input);
	}
	
	public List<Map<String, Object>> selectManagerList() throws Exception {
		
		return sqlSession.selectList(Namespace + ".selectManagerList");
	}
	
	public List<Map<String, Object>> selectCreateUser(Map<String, Object> map) throws Exception {
		
		return sqlSession.selectList(Namespace + ".selectCreateUser", map);
	}
	
	public void userDelete(Map<String, Object> map) throws Exception {
		
		sqlSession.delete(Namespace+".userDelete", map);
	}
	
	public void userPwdReset(Map<String, Object> map) throws Exception {
		
		sqlSession.delete(Namespace+".userPwdReset", map);
	}

	public Map<String, Object> chkDuplicateUserNo(String userNo) throws Exception {
		
		return sqlSession.selectOne(Namespace + ".chkDuplicateUserNo", userNo);
	}
	
	public void createUser(Map<String, Object> map) throws Exception {

		sqlSession.insert(Namespace+".createUser", map);
	}
	
	public Map<String, Object> sumApproval(Map<String, String> map) {
		return sqlSession.selectOne(Namespace + ".sumApproval", map);
	}
	
	public List<Map<String, Object>> selectAccountOfficeList() throws Exception {
		return sqlSession.selectList(Namespace + ".selectAccountOfficeList");
	}

	public boolean updateFailedCount(Map<String, String> map) {
		return (sqlSession.update(Namespace + ".updateFailedCount", map) > 0);
	}

	/*public void createAccountInfo(Map<String, Object> map) throws Exception{
		sqlSession.insert(Namespace+".createAccountInfo", map);
	}*/

	
	public void updateSMSCode(Map<String, Object> map) {
		sqlSession.update(Namespace + ".updateSMSCode", map);
	}
	
	public List<Map<String, Object>> setHeader(Map<String, Object> map) {
		
		return sqlSession.selectList(Namespace + ".setHeader", map);
	}

	public Map<String, Object> setPageData(Map<String, Object> map) {
		return sqlSession.selectOne(Namespace + ".setPageData", map);
	}
	
	public void setSmsCode(Map<String, Object> map) throws Exception {
		
		sqlSession.update(Namespace + ".setSmsCode", map);
	}

	public void reset_sms_code(Map<String, Object> map) {
		sqlSession.update(Namespace + ".reset_sms_code", map);
	}

	public void changeUserSettingDate(Map<String, Object> map) throws Exception {
		
		sqlSession.update(Namespace+".changeUserSettingDate", map);
	}

	public Map<String, Object> selectChangeMember(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne(Namespace + ".selectChangeMember", map);
	}

	public List<Map<String, Object>> selectMemberTeam() {
		return sqlSession.selectList(Namespace + ".selectMemberTeam");
	}
	
	public List<Map<String, Object>> selectPCAdmin(Map<String, Object> searchMap) {
		return sqlSession.selectList(Namespace + ".selectPCAdmin", searchMap);
	}
	
	public int lockMemberRequest(Map<String, Object> requestMap) {
		return sqlSession.update(Namespace+".lockMemberRequest", requestMap);
	}

	public int unlockMemberRequest(Map<String, Object> requestMap) {
		return sqlSession.update(Namespace+".unlockMemberRequest", requestMap);
	}

	public Map<String, Object> selectAccoutnLockMember(Map<String, Object> requestMap)  throws Exception {
		return sqlSession.selectOne(Namespace + ".selectAccoutnLockMember", requestMap);
	}

	public List<Map<String, String>> selectAdminMember(Map<String, Object> requestMap) throws Exception {
		return sqlSession.selectList(Namespace + ".selectAdminMember", requestMap);
	}

	public Map<String, Object> getversion(Map<String, Object> requestMap) {
		return sqlSession.selectOne(Namespace + ".getversion", requestMap);
	}
	
	public Map<String, Object> selectAccountPolicy() { 
		return sqlSession.selectOne(Namespace + ".selectAccountPolicy");
	}
	
	public void saveAccountPolicy(Map<String, Object> map) throws Exception {
		sqlSession.update(Namespace+".saveAccountPolicy", map);
	}
	
	public int selectPwdDuplicate(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne(Namespace + ".selectPwdDuplicate", map);
	}
	
	public void insertPwdDuplicate(Map<String, Object> map) throws Exception {
		sqlSession.update(Namespace+".insertPwdDuplicate", map);
	}

	public Map<String, Object> selectApNoData(int ap_no) throws Exception {
		return sqlSession.selectOne(Namespace + ".selectApNoData", ap_no);
	}

	public String selectLastAccessTime(String user_no) {
		return sqlSession.selectOne(Namespace + ".selectLastAccessTime", user_no);
	}
	
}
