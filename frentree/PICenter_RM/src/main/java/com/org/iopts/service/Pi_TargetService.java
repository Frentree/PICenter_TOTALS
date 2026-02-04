package com.org.iopts.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.org.iopts.dto.Pi_TargetVO;

import jakarta.servlet.http.HttpServletRequest;

public interface Pi_TargetService {
	
	public List<Map<String, Object>> selectTarget() throws Exception;
	
	public List<Map<String, Object>> selectTargetManagement() throws Exception;
	
	public List<Map<String, Object>> selectTargetList(HttpServletRequest request) throws Exception;
	
	public List<Map<String, Object>> selectTargetUser(HttpServletRequest request) throws Exception;	
	
	// Targets Insert
	int insertTarget(List<Pi_TargetVO> list) throws Exception;
	
	public List<Map<String, Object>> selectUserTargetList(HttpServletRequest request) throws Exception;
	
	public List<Map<String, Object>> selectServerList(HttpServletRequest request) throws Exception;

	public List<Map<String, Object>> selectTargetUserList(HttpServletRequest request) throws Exception;

	public List<Map<String, Object>> selectServerFileTopN(HttpServletRequest request);

	public List<Map<String, Object>> selectAdminServerFileTopN(HttpServletRequest request);	
	
	
	/**
	 * DMZ List 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	
	
	/**
	 * DMZ List Delete
	 * @param params
	 * @throws Exception
	 */

	public HashMap<String, Object> selectTargetById(HashMap<String, Object> params) throws Exception;

	public List<Map<String, Object>> selectGroupList(Map<String, Object> map) throws Exception;
	
	public List<Map<String, Object>> selectUserGroupList(Map<String, Object> map) throws Exception;

	public List<Map<String, Object>> getTargetList(Map<String, Object> map) throws Exception;
	
	public List<Map<String, Object>> selectNoticeList(Map<String, Object> map) throws Exception;

	public Map<String, Object> getGroupDetails(Map<String, Object> map) throws Exception;
	
	public List<Map<String, Object>> selectRmTargetList(HttpServletRequest request);
	
	public List<Map<String, Object>> selectPCTargetUser(HttpServletRequest request);

	public List<Map<String, Object>> getExceptionList(HttpServletRequest request) throws Exception;

	public List<Map<String, Object>> selectSKTManagerList(HttpServletRequest request) throws Exception;
	
	public List<Map<String, Object>> selectAddSKTManagerList(HttpServletRequest request) throws Exception;
	
	public List<Map<String, Object>> selectPcManagerList(HttpServletRequest request) throws Exception;

	public List<Map<String, Object>> selectVersionList(HttpServletRequest request) throws Exception;
	
	public List<Map<String, Object>> searchPCTargetUser(HttpServletRequest request);
	
}
