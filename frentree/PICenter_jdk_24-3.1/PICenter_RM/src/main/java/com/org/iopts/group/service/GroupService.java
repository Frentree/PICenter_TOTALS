package com.org.iopts.group.service;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import com.org.iopts.group.vo.GroupTreeServerVo;

import com.google.gson.JsonArray;

public interface GroupService {
	//서버 & PC 전체 데이타
	String selectUserGroupList(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	String selectUserListGroup(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	// 예외관리 서버 그룹 데이터
	JsonArray selectExceptionServerList(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	// 예와관리 서버 호스트명 데이터
	JsonArray selectExceptionHostList(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	//서버 그룹 데이터
	String selectServerGroupList(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	//서버 그룹 데이터
	String selectDeptGroupList(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	//서버 그룹 데이터
	JsonArray selectDashSeverDept(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	//PC 그룹 데이터
	JsonArray selectDashPCDept(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	// 서버 + PC 그룹데이터
	JsonArray selectDashDeptList(Map<String, Object> map,  HttpServletRequest request) throws Exception;
	
	JsonArray SelectTargetDash( HttpServletRequest request) throws Exception;
	
	//PC 그룹 데이터
	JsonArray selectPCGroup(Map<String, Object> map, HttpServletRequest request) throws Exception;

	//그룹 데이터
	List<Map<String, Object>> selectGroup(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	//타겟 그룹 이동
	int moveTargetGroup(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	//skt Toms 데이터
	JsonArray selectTomsGroup(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	//skt Toms 미그룹데이터
	//JSONArray selectTomsNotGroup(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	JsonArray selectUserCreateGroup( HttpServletRequest request) throws Exception;

	//skt 그룹 생성
	JsonArray insertUserCreateGroup(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	//skt 그룹 변경
	int updateUserCreateGroup(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	//skt 그룹 삭제
	String deleteUserCreateGroup(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	//skt 사용자 그룹 서버 저장
	Map<String, Object> insertUserTargets(Map<String, Object> map, HttpServletRequest request) throws Exception;

	// Host 검색 데이터
	String selectUserHostGroupList(Map<String, Object> map, HttpServletRequest request) throws Exception;

	//SubPath
	JsonArray selectSubPath(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	// PC 망/그룹/PC 선택
	JsonArray selectNetList(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	String userReportGroupList(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	String userGroupList(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	String selectUserHostOneDriveList(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	String userReportHostList(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	String selectPopupServerGroupList(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	JsonArray selectLicenseGroup(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	
	// 삼성화재 node 정리
	String selectPICenterServer(Map<String, Object> map, HttpServletRequest request) throws Exception; // 자산 x 날짜 x
	
	String selectPICenterServerDate(Map<String, Object> map, HttpServletRequest request) throws Exception; // 자산 x 날짜 o
	
	String selectAccountServer(Map<String, Object> map, HttpServletRequest request) throws Exception; // 자산 x 날짜 o
	
	String selectPICenterServerGroup(Map<String, Object> map, HttpServletRequest request) throws Exception; // 그룹

	Object selectNASList(Map<String, Object> map, HttpServletRequest request) throws Exception;

	List<Map<String, Object>> selectMngrList()  throws Exception;

	Map<String, Object> chkAccountCnt() throws Exception;
	
	//DB
	JsonArray selectDBGroupList(Map<String, Object> map, HttpServletRequest request) throws Exception;

	String getGroupList(Map<String, Object> map, HttpServletRequest request) throws Exception;
}

