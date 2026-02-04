package com.org.iopts.remediation.service;

import java.util.HashMap;
import java.util.List;

public interface RemediationService {
	
	/**
	 * 조치 이력 목록 조회 (DETAIL 테이블)
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectRemediationHistory(HashMap<String, Object> params) throws Exception;
}