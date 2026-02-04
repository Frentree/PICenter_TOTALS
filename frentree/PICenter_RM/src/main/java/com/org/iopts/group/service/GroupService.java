package com.org.iopts.group.service;

import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;

import jakarta.servlet.http.HttpServletRequest;

public interface GroupService {
	//서버 & PC 전체 데이타
	String selectUserGroupList(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	String selectUserListGroup(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	//서버 그룹 데이터
	String selectServerGroupList(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	//서버 그룹 데이터
	JsonArray selectDashSeverDept(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	// 서버 + PC 그룹데이터
	JsonArray selectDashDeptList(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	//PC 그룹 데이터
	JsonArray selectPCGroup(Map<String, Object> map, HttpServletRequest request) throws Exception;

	//그룹 데이터
	List<Map<String, Object>> selectGroup(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	//skt Toms 미그룹데이터
	//JSONArray selectTomsNotGroup(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	JsonArray selectUserCreateGroup(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	//skt 사용자 그룹 서버 저장
	JsonArray insertUserTargets(Map<String, Object> map, HttpServletRequest request) throws Exception;

	// Host 검색 데이터
	String selectUserHostGroupList(Map<String, Object> map, HttpServletRequest request) throws Exception;

	//SubPath
	JsonArray selectSubPath(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	String userReportGroupList(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	String userReportHostList(Map<String, Object> map, HttpServletRequest request) throws Exception;
}
