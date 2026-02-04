package com.org.iopts.mockup.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;


public interface MockupService {
	List<Map<String, Object>> allTargetList(HttpServletRequest request) throws Exception;
	 
	 
	 Map<String, Object> getServerDataPivot(HttpServletRequest request) throws Exception;
	 List<Map<String, Object>> getServerColumns(HttpServletRequest request) throws Exception;

	 
}
