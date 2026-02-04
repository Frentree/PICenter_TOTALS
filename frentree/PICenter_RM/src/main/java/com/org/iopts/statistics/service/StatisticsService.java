package com.org.iopts.statistics.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

public interface StatisticsService {

    Map<String, Object> statisticsList(HttpServletRequest request);

    Map<String, Object> manageList(HttpServletRequest request);

    List<Map<String, Object>> manageBarList(HttpServletRequest request);

    List<Map<String, Object>> mainChartStatistics(HttpServletRequest request);

    Map<String, Object> totalStatistics(HttpServletRequest request);

    List<Map<String,Object>> statisticsPolicyList(HttpServletRequest request);

    List<HashMap<String, Object>> excelDown(HashMap<String, Object> params) throws Exception;

}
