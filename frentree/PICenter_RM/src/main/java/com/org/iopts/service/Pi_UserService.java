package com.org.iopts.service;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

public interface Pi_UserService {
	
	public Map<String, Object> selectMember(HttpServletRequest request) throws Exception;
	
	public void insertMemberLog(Map<String, Object> userLog) throws Exception;
	
	public List<Map<String, Object>> selectUserLogList(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> changeAuthCharacter(HttpServletRequest request) throws Exception;
	
	public void logout(HttpServletRequest request) throws Exception;
	
	public List<Map<String, Object>> selectManagerList(HttpServletRequest request) throws Exception;
	
	public List<Map<String, Object>> selectCreateUser(HttpServletRequest request) throws Exception;
	
	public void userDelete(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> chkDuplicateUserNo(HttpServletRequest request) throws Exception;
	
	public void createUser(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> changeUserSettingData(HttpServletRequest request) throws Exception;

	public Map<String, Object> getversion() throws Exception;

	public void pwd_reset(HttpServletRequest request) throws Exception;

	public List<Map<String, Object>> selectLicense() throws Exception;
	
	public Map<String, Object> selectAccountPolicy() throws Exception;
	
	public Map<String, Object> saveAccountPolicy(HttpServletRequest request) throws Exception;

}
