package com.org.iopts.report.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface piSummaryService {
 
	// 정탐/오탐 리스트 조회
	public List<HashMap<String, Object>> searchSummaryList(HashMap<String, Object> params) throws Exception;
	
	/**
	 * 정탐/오탐 처리 flag 조회
	 * @return
	 * @throws Exception
	 */

	public List<HashMap<String, Object>> getMonthlyReport(HashMap<String, Object> params) throws Exception;

	public List<HashMap<String, Object>> searchTargetSummaryReport(HashMap<String, Object> params) throws Exception;

}