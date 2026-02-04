package com.org.iopts.mail.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

import com.org.iopts.group.vo.DashScheduleServerVo;
import com.org.iopts.group.vo.GroupPCTargetVo;
import com.org.iopts.group.vo.GroupTargetVo;
import com.org.iopts.group.vo.GroupTomsVo;
import com.org.iopts.group.vo.GroupTreeServerVo;
import com.org.iopts.group.vo.GroupTreeVo;
import com.org.iopts.group.vo.PCGroupVo;
import com.org.iopts.group.vo.SchedulePCTargetVo;
import com.org.iopts.group.vo.ScheduleServerNotTargetVo;
import com.org.iopts.group.vo.ScheduleServerVo;
import com.org.iopts.group.vo.ScheduleUserVo;
import com.org.iopts.mail.vo.MailVo;
import com.org.iopts.mail.vo.UserVo;

public interface MailDAO {

	List<Map<String,  String>> serverGroupMail(Map<String, Object> map);

	List<MailVo> serverGroupMailUser(Map<String, Object> map);

	List<UserVo> selectUserList(Map<String, Object> userMap);

	List<UserVo> selectSendUserList(HashMap<String, Object> params);

	List<MailVo> selectDate();
	
	void templateInsert(Map<String, Object> input);
	
	String selectTemplate(String mailFlag);

	void templateUpdate(Map<String, Object> input);
	/*List<UserVo> selectApprovalList(HashMap<String, Object> params);
*/
	Map<String, Object> selectMailUser();

	Map<String, Object> selectTargetDetail(Map<String, Object> mailConMap);

	// 메일 템플릿 목록 조회
	List<Map<String, Object>> selectTemplateList();

	// 메일 템플릿 상세 조회
	Map<String, Object> selectTemplateDetail(int idx);

	// 메일 템플릿 삭제
	void deleteTemplate(int idx);

	// LG생건 메일용: 서버별 검출건수 조회
	Map<String, Object> selectTargetSummaryLg(Map<String, Object> map);

	// LG생건 메일용: 수신자별 담당 서버 목록 조회
	List<Map<String, Object>> selectMailUserTargetLg(Map<String, Object> map);

	// 메일 템플릿 수정 (IDX로)
	void updateTemplateById(Map<String, Object> params);

	// 메일 템플릿 등록 (sendmail_N 형식)
	void insertSendmailTemplate(Map<String, Object> params);

	// user_no로 이메일 조회
	String selectUserEmailByUserNo(String userNo);

	// user_no 목록으로 이메일 목록 조회
	List<Map<String, Object>> selectUserEmailsByUserNos(List<String> userNos);

	// 서버별 담당자(정/부) 조회
	Map<String, Object> selectTargetManagerLg(Map<String, Object> map);

	// 메일 발송 이력 저장
	void insertMailHistory(Map<String, Object> params);

	// 메일 대상 서버 저장
	void insertMailTarget(Map<String, Object> params);

	// 메일 대상 서버 일괄 저장
	void insertMailTargetBatch(Map<String, Object> params);

	// 메일 발송 이력 목록 조회
	List<Map<String, Object>> selectMailHistoryList(Map<String, Object> params);

	// 메일 발송 이력 상세 조회
	Map<String, Object> selectMailHistoryDetail(int mailIdx);

	// 메일 대상 서버 목록 조회
	List<Map<String, Object>> selectMailTargetList(int mailIdx);
}
