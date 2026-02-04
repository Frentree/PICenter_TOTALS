package com.org.iopts.service;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.org.iopts.dto.MemberVO;

public interface Pi_UserService {
	
	public Map<String, Object> selectMember(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> changeUser(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> selectSSOMember(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> accountMemberSSO(HttpServletRequest request) throws Exception;
	
	public List<Map<String, Object>> selectTeamMember(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> selectTeamManager() throws Exception;
	
	public void insertMemberLog(Map<String, Object> userLog) throws Exception;
	
	public List<Map<String, Object>> selectUserLogList(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> changeAuthCharacter(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> selectNotice() throws Exception;
	
	// 공지사항 목록 조회
	public List<Map<String, Object>> noticeList(HttpServletRequest request);
	
	//공지사항 검색 목록 조회
	public List<Map<String, Object>> noticeSearchList(HttpServletRequest request);
	
	// 공지사항 등록
	public Map<String, Object> noticeInsert(HttpServletRequest request) throws Exception;
	
	public void noticeAlert(HttpServletRequest request) throws Exception;

	// 공지사항 수정
	public Map<String, Object> noticeUpdate(HttpServletRequest request) throws Exception;
	
	// 공지사항 삭제
	public Map<String, Object> noticeDelete(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> selectDownload() throws Exception;
	
	public List<Map<String, Object>> downloadList(HttpServletRequest request);
	
	public List<Map<String, Object>> downloadSearchList(HttpServletRequest request);
	
	public Map<String, Object> downloadInsert(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> downloadUpdate(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> downloadDelete(HttpServletRequest request) throws Exception;
	
	public String selectAccessIP() throws Exception;
	
	public void changeNotice(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> changeAccessIP(HttpServletRequest request) throws Exception;
	
	public void logout(HttpServletRequest request) throws Exception;
	
	public List<Map<String, Object>> selectManagerList(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> changeManagerList(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> changeUserData(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> managerResetPwd(HttpServletRequest request) throws Exception;
	
	public void userLock(HttpServletRequest request) throws Exception;
	
	public void userDelete(HttpServletRequest request) throws Exception;
	
	public List<Map<String, Object>> selectTeamCode() throws Exception;
	
	public Map<String, Object> chkDuplicateUserNo(HttpServletRequest request) throws Exception;
	
	public void createTeam(HttpServletRequest request) throws Exception;
	
	public void createUser(HttpServletRequest request) throws Exception;
	
	/**
	 * PI_ACCOUNT_INFO 테이블에서  OFFICE 정보 가져오기 (중복제거 및 NULL 제외)
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectAccountOfficeList() throws Exception;

	void unlockAccount(HttpServletRequest request);

	public Map<String, Object> getLicenseDetail() throws Exception;

	public List<Map<String, Object>> getLogFlagList() throws Exception;
	
	public List<Map<String, Object>> getGradeFlagList() throws Exception;

	public Map<String, Object> getLicenseDetail(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> changeResetPwd(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> submitSmsLogin(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> authSMSResend(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> authSMSCancel(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> sumApproval() throws Exception;

	public Map<String, Object> reset_sms_code(HttpServletRequest request);

	public Map<String, Object> changeUserSettingData(HttpServletRequest request) throws Exception;

	public List<Map<String, Object>> selectMemberTeam(HttpServletRequest request) throws Exception;
	
	public List<Map<String, Object>> selectPCAdmin(HttpServletRequest request) throws Exception;

	public Map<String, Object> lockMemberRequest(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> unlockMemberRequest(HttpServletRequest request) throws Exception;
	
	public List<Map<String, Object>> selectLockManagerList(HttpServletRequest request) throws Exception;
	
	public String selectSMSFlag() throws Exception;
	
	public void updateSMSFlag(HttpServletRequest request) throws Exception;
	
	public List<Map<String, Object>> selectbatchData(String string) throws Exception;
	
//	public Map<String, Object> updateBatchSchedule(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> getversion() throws Exception;

	public Map<String, Object> selectAccountPolicy() throws Exception;
	
	public Map<String, Object> saveAccountPolicy(HttpSession session, HttpServletRequest request) throws Exception;
	
	public List<Map<String, Object>> selectLicense() throws Exception;

	public List<Map<String, Object>> selectUserGradeList() throws Exception;
	
}
