package com.org.iopts.setting.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.org.iopts.dto.MemberVO;

public interface Pi_SetService {
	
	public List<Map<Object, Object>> selectSetting(int i) throws Exception;

	public List<Map<String, Object>> patternList(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> approvalAlert(HttpServletRequest request) throws Exception;
	
	public List<Map<String, Object>> selectGroupApprovalUser(HttpServletRequest request) throws Exception;

	public List<Map<String, Object>> selectbatchData(String string) throws Exception;
	
	public Map<String, Object> checkPageGrade(String requestUrl) throws Exception;
	
	public void checkPageLog(HttpServletRequest request, String header_name) throws Exception;

	public Map<String, Object> updateCustomPattern(HttpServletRequest request)  throws Exception;
	
	public Map<String, Object> deleteCustomPattern(HttpServletRequest request)  throws Exception;
	
	public Map<String, Object> insertCustomPattern(HttpServletRequest request)  throws Exception;
	
	public Map<String, Object> customPatternChagne(HttpServletRequest request)  throws Exception;
	
	public Map<String, Object> nameListDelete(HttpServletRequest request)  throws Exception;
	
	public List<Map<String, Object>> nameList(String string) throws Exception;
	
	public Map<String, Object> nameListUpdate(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> nameListCreate(HttpServletRequest request) throws Exception;

	public List<Map<String, Object>> conDataList(HttpServletRequest request) throws Exception;
	
	public void deleteGroupApprovalUser(HttpServletRequest request) throws Exception;
	
	public void insertGroupApprovalUser(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> ConListUpdate(HttpServletRequest request)  throws Exception;
	
	public List<Map<String, Object>> approvalList(HttpServletRequest request) throws Exception;
	
	public List<Map<String, Object>> groupApprovalList(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> updateBatchApproval(HttpServletRequest request) throws Exception;

	public List<Map<Object, Object>> selectApprovalStatus() throws Exception;
	
	public List<Map<Object, Object>> selectApprovalStatusAll() throws Exception;
	
	public List<Map<String, Object>> getProcessingFlag(String gridName) throws Exception;
	
	public void updateProcessingFlag(HttpServletRequest request) throws Exception;
	
	public void deleteProcessingFlag(HttpServletRequest request) throws Exception;

	public void insertProcessingFlag(HttpServletRequest request) throws Exception;

	public void updateExceptionFlag(HttpServletRequest request) throws Exception;

	public void insertExceptionFlag(HttpServletRequest request) throws Exception;

	public Map<String, Object> updateBatchSchedule(HttpServletRequest request) throws Exception;

	public Map<String, Object> cryptPWD(HttpServletRequest request) throws Exception;

	public Map<String, Object> chkPattern(HttpServletRequest request) throws Exception;

	public List<Map<String, Object>> reconNodeSelect(HttpServletRequest request)  throws Exception;
	
	public List<Map<String, Object>> PICNodeSelect(HttpServletRequest request)  throws Exception;
	
	public Map<String, Object> backupTables(List<String> tables, int value, HttpServletRequest request) throws Exception;
	
	public List<Map<String, Object>> rollBackList(HttpServletRequest request) throws Exception;
	
	public Map<String, Object> rollBackTables(String fileName, HttpServletRequest request) throws Exception;

	public void userlogUpdate(HttpServletRequest request, String job_info) throws Exception;
	
	public List<Map<String, Object>> reportHeaderList(HttpServletRequest request)  throws Exception;
	
	public Map<String, Object> systemLog(HttpServletRequest request) throws Exception;
}
