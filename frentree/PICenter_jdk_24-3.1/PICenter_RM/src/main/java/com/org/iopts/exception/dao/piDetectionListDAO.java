package com.org.iopts.exception.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface piDetectionListDAO {

	public List<HashMap<String, Object>> selectFindSubpath2(HashMap<String, Object> params) throws SQLException;
	
	public List<HashMap<String, Object>> selectFindSubpath3(HashMap<String, Object> params) throws SQLException;
	
	public List<HashMap<String, Object>> selectFindSubpath4(HashMap<String, Object> params) throws SQLException;
	
	public List<HashMap<String, Object>> selectDetectionApprovalList(HashMap<String, Object> params) throws SQLException;
	
	public List<Map<String, Object>> getDetectionApprovalList(Map<String, Object> map) throws Exception;
	
	public List<HashMap<String, Object>> subpathSelect(HashMap<String, Object> params) throws SQLException;

	public List<Map<String, Object>> selectUserTargetList(Map<String, Object> searchMap);

	public HashMap<String, Object> selectProcessDocuNum(HashMap<String, Object> params);

	public void registProcessGroup(HashMap<String, Object> params);

	public void registProcess(HashMap<String, Object> params);

	public String selectIdx(Map<String, Object> map);

	public void deleteDataProcessing(Map<String, Object> map);

	public int getCountProcessingGroup(Map<String, Object> map);

	public void deleteDataProcessingGroup(Map<String, Object> map);
	
	public List<HashMap<String, Object>> personalApprovalData(HashMap<String, Object> params) throws SQLException;
	
	public List<Integer> queryCustomDataTypesCnt() throws SQLException;
	
	public List<Integer> queryCustomDataTypesCnt2() throws SQLException;
	
	public List<Integer> queryCustomDetailDataTypesCnt() throws SQLException;
	
	public List<Map<String, Object>> queryCustomDataTypes() throws SQLException;
	
    public List<Map<String, Object>> queryCustomDetailDataTypes() throws SQLException;
	
	public List<Map<String, Object>> queryMatchDetail() throws SQLException;
	
	public List<Map<String, Object>> queryMatchDetail2() throws SQLException;

	public List<Map<String, Object>> queryCustomDataRules() throws SQLException;

	public List<Map<String, Object>> queryCustomDataMask() throws SQLException;

	public List<HashMap<String, Object>> selectGroupFindSubpath(HashMap<String, Object> params) throws SQLException;
	
	public List<HashMap<String, Object>> selectPICenterGroupFindSubpath(HashMap<String, Object> params) throws SQLException;

	public List<Map<String, Object>> getExceptionFlag();
	
	public Map<String, Object> getFindByHash(Map<String, Object> map) throws Exception;
	
	public Map<String, Object> getDBFindByHash(Map<String, Object> map) throws Exception;

	public List<Map<String, Object>> selectFalseMatchApi(Map<String, Object> data_pro);

	public void setRemediation(Map<String, Object> false_match) throws Exception;

	public Map<String, Object> hostPatternCnt(HashMap<String, Object> findMap) throws Exception;
	
    public void insertRemediationGroup(Map<String, Object> params);
	    
    public void insertRemediationDetail(Map<String, Object> params);
	    
    public void updateRemediationGroup(Map<String, Object> params);
    
    public void updateRemediationDetailStatus(Map<String, Object> params);
    
    public List<Map<String, Object>> selectRemediationGroupList(Map<String, Object> params);

    public List<Map<String, Object>> selectRemediationDetailList(Map<String, Object> params);	
    
    public int updateFindRemediation(Map<String, Object> params) throws Exception;
    
    public int updateSubpathRemediation(Map<String, Object> params) throws Exception;
}
