package com.org.iopts.download.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

public interface DownloadService {
	// 최상위 결과
	int selectDownloadIndex(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	int insertDownload(Map<String, Object> map, HttpServletRequest request) throws Exception ;

	int selectFileDownloadIndex(Map<String, Object> map, HttpServletRequest request) throws Exception;

	int insertFileDownload(Map<String, Object> map, HttpServletRequest request) throws Exception;
	
	Map<String, Object> downLoadFileInformation(HttpServletRequest request, HashMap<String, Object> params) throws Exception;
	
	List<Map<String, Object>> downloadList(HttpServletRequest request) throws Exception;
}
