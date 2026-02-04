package com.org.iopts.report.dao;

import java.sql.SQLException; 
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public interface piSummaryDAO { 

	public List<HashMap<String, Object>> searchSummaryList(HashMap<String, Object> params) throws SQLException;
	
	public List<HashMap<String, Object>> searchPCSummaryList(HashMap<String, Object> params) throws SQLException;
	
	public List<HashMap<String, Object>> searchPCSummaryRegDateList(HashMap<String, Object> params) throws SQLException;
	 
	/**
	 * 정탐/오탐 처리 flag 조회
	 * @return
	 * @throws SQLException
	 */
	
	public List<HashMap<String, Object>> getMonthlyReport(String yyyymm) throws SQLException;

	public List<HashMap<String, Object>> searchTargetSummaryReport(HashMap<String, Object> params) throws Exception;
	
	
}