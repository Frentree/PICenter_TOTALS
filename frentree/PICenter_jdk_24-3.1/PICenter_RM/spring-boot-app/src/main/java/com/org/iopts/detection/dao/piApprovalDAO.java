package com.org.iopts.detection.dao;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.org.iopts.detection.vo.patternVo;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface piApprovalDAO {

	public List<HashMap<String, Object>> searchProcessList(HashMap<String, Object> params) throws SQLException;

	public List<HashMap<String, Object>> selectTeamMember(HashMap<String, Object> searchMap) throws SQLException;
	
	public List<HashMap<String, Object>> searchTeamMember(HashMap<String, Object> searchMap);

	public Map<String, Object> selectDocuNum(HashMap<String, Object> params) throws SQLException;

	public List<HashMap<String, Object>> selectProcessPath(HashMap<String, Object> params) throws SQLException;
	
	public void registProcessCharge(HashMap<String, Object> params) throws SQLException;
	
	public void deleteProcessCharge(HashMap<String, Object> params) throws SQLException;

	public void updateProcessingGroupStatus(Map<String, Object> params) throws SQLException;
	
	public int selectChargeId(HashMap<String, Object> params)  throws SQLException;
	
	public void insertProcessUserList(HashMap<String, Object> userListMap) throws SQLException;

	public void updateProcessingStatus(Map<String, Object> params) throws SQLException;

	public void approvalPlus(HashMap<String, Object> params) throws SQLException;

	public List<HashMap<String, Object>> searchApprovalAllListData(HashMap<String, Object> params) throws SQLException;

	public List<HashMap<String, Object>> searchApprovalListData(HashMap<String, Object> params) throws SQLException;

	public List<HashMap<String, Object>> selectProcessGroupPath(HashMap<String, Object> params) throws SQLException;
	
	public List<HashMap<String, Object>> selectProcessGroupPath2(HashMap<String, Object> params) throws SQLException;

	public List<HashMap<String, Object>> selectApprovalUserList(HashMap<String, Object> params) throws SQLException;

	public List<HashMap<String, Object>> selectDataProcessingGroupId(HashMap<String, Object> params) throws SQLException;

	public void updateProcessApproval(HashMap<String, Object> params) throws SQLException;
	
	public void updateProceesApprovalUser(HashMap<String, Object> params) throws SQLException;

	public void updateDataProcessing(HashMap<String, Object> params) throws SQLException;
	
	public List<HashMap<String, Object>> selectScanPolicy() throws SQLException;

	public List<HashMap<String, Object>> selectReScanTarget(HashMap<String, Object> params) throws SQLException;

	public void deleteItem(Map<String, Object> map) throws SQLException;

	public Object selectDataProcessingChargeId(HashMap<String, Object> params);

	public HashMap<String, Object> selectProcessingSeq(Map<String, Object> params) throws SQLException;
	
	public void insertProcessingSeq(Map<String, Object> params) throws SQLException;
	
	public void updateProcessCharge(Map<String, Object> params) throws SQLException;
	
	public List<HashMap<String, Object>> selectApprovalCount(Map<String, Object> params) throws SQLException;
	
	public Map<String, Object> selectApprovalData(Map<String, Object> params) throws SQLException;

	public Map<String, Object> selectAgentData(Map<String, Object> agentMap) throws SQLException;
	
	public Map<String, Object> selectFindMatchDetail(Map<String, Object> agentMap) throws SQLException;

	public Map<String, Object> selectMailUser()  throws SQLException;

	public List<HashMap<String, Object>> selectApprovalCount2(Map<String, Object> params) throws SQLException;
	
	public List<HashMap<String, Object>> selectApprovalHost(String pathGroupId) throws SQLException;
	
	public void insertGroupChageId(HashMap<String, Object> groupMap) throws SQLException;

	public List<patternVo> customPatternList() throws SQLException;

	
}