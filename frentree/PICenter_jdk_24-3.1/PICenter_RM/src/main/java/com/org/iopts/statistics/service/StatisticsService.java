package com.org.iopts.statistics.service;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import com.org.iopts.statistics.vo.StatisticsVo;

public interface StatisticsService {

	List<Map<String, Object>> statisticsList(HttpServletRequest request);

	List<Map<String, Object>> manageList(HttpServletRequest request);

	List<Map<String, Object>> manageBarList(HttpServletRequest request);

	List<Map<String, Object>> mainChartStatistics(HttpServletRequest request);
	
	List<Map<String, Object>> trueGridList(HttpServletRequest request);
	
	List<Map<String, Object>> falseGridList(HttpServletRequest request);

	Map<String, Object> totalStatistics(HttpServletRequest request);

	List<Map<String, Object>> selectDataImple(HttpServletRequest request);





}
