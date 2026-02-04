package com.org.iopts.remediation.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface piRemediationDAO {
	
	/**
	 * 조치 이력 목록 조회 (DETAIL 테이블)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<HashMap<String, Object>> selectRemediationHistory(HashMap<String, Object> params) throws SQLException;
	
	List<String> selectUserTargetIds(HashMap<String, Object> params) throws Exception;
}