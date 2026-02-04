package com.org.iopts.service;

import java.util.List;
import java.util.Map;

import com.org.iopts.dto.Pi_Scan_HostVO;

import jakarta.servlet.http.HttpServletRequest;

public interface Pi_ScanService {
	
	public List<Pi_Scan_HostVO> selectScanHost() throws Exception;

	public List<Map<String, Object>> getApList(HttpServletRequest request) throws Exception;

}