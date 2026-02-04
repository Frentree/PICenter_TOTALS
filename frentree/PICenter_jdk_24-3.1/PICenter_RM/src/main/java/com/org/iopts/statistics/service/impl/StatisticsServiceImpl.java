package com.org.iopts.statistics.service.impl;

import java.io.Reader;
import java.net.ProtocolException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.org.iopts.exception.dao.piDetectionListDAO;
import com.org.iopts.statistics.dao.StatisticsDAO;
import com.org.iopts.statistics.service.StatisticsService;
import com.org.iopts.statistics.vo.StatisticsVo;
import com.org.iopts.util.ReconUtil;
import com.org.iopts.util.ServletUtil;
import com.org.iopts.util.SessionUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
@Transactional
public class StatisticsServiceImpl implements StatisticsService{

	private static Logger logger = LoggerFactory.getLogger(StatisticsServiceImpl.class);

	@Value("${recon.id}")
	private String recon_id;

	@Value("${recon.password}")
	private String recon_password;

	@Value("${recon.url}")
	private String recon_url;
	
	@Value("${recon.api.version}")
	private String api_ver;
	
	@Inject
	private StatisticsDAO dao;
	
	@Inject
	private piDetectionListDAO detectionDao;

	@Override
	public List<Map<String, Object>> statisticsList(HttpServletRequest request) {
		
		String toDate = request.getParameter("toDate");
		String fromDate = request.getParameter("fromDate");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("toDate", toDate);
		result.put("fromDate", fromDate);
		
		try {
			// PICenter 에서 받아오는 개인정보 유형 갯수
			List<Integer> patternList = detectionDao.queryCustomDataTypesCnt2(); 
			result.put("patternList", patternList); 
			
		} catch(Exception e) {
			logger.error(e.toString());
		}
		
		List<Map<String, Object>> resultMap = dao.statisticsList(result);
		
		return resultMap;
	}

    // 전체 서버 점검 결과 (의심 건수) 조회
	@Override
	public List<Map<String, Object>> manageList(HttpServletRequest request) {
		
		String toDate = request.getParameter("toDate");
		String fromDate = request.getParameter("fromDate");
		List<Map<String, Object>> resultList = new ArrayList<>();
		List<Map<String, Object>> resultMap = new ArrayList<>();
		
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("toDate", toDate);
			result.put("fromDate", fromDate);
			resultMap  =  dao.manageList(result);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("e" + e);
		}
		
		ObjectMapper mapper = new ObjectMapper();
        Map<String, Map<String, Object>> groupedData = new HashMap<>();
		
		try {
			for (Map<String, Object> map : resultMap) {
				String monthDay = (String) map.get("month_day");
				String dataTypesJson = (String) map.get("data_types");
                long matchLocations = Long.parseLong(map.get("match_locations").toString());
                int host_cnt = Integer.parseInt(map.get("host_cnt").toString());
                long match = Long.parseLong(map.get("match").toString());
                
                List<Map<String, Object>> dataTypes = mapper.readValue(dataTypesJson, List.class);
                
                groupedData.computeIfAbsent(monthDay, k -> new HashMap<>());
                Map<String, Object> monthData = groupedData.get(monthDay);
                for (Map<String, Object> pattern : dataTypes) {
                    String patternName = (String) pattern.get("PATTERN_NM");
                    String patternNum = (String) pattern.get("PATTERN_NUM");  
                    long matchCnt = ((Number) pattern.get("MATCH_CNT")).longValue();
                    monthData.merge("TYPE"+patternNum, matchCnt, (oldValue, newValue) -> ((Number) oldValue).longValue() + matchCnt);
                }

                // match_locations, match 합계
                long currentMatchLocations = (long) monthData.getOrDefault("match_locations", 0L);
                int hostCnt = (int) monthData.getOrDefault("host_cnt", 0);
                long currentMatch = (long) monthData.getOrDefault("match", 0L);
                monthData.put("match_locations", currentMatchLocations + matchLocations);
                monthData.put("match", currentMatch + match);
                monthData.put("host_cnt", hostCnt + host_cnt); 
			}
			
			resultList = groupedData.entrySet().stream().map(entry -> {
				                Map<String, Object> map = new HashMap<>();
				                map.put("month_day", entry.getKey());
				                map.putAll(entry.getValue());
				                return map;
				            })
				            .collect(Collectors.toList());
			
			logger.info("resultList  :: " + resultList);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error ::: " + e);
		}
		
        return resultList;
	}

	@Override
	public List<Map<String, Object>> manageBarList(HttpServletRequest request) {
		
		String toDate = request.getParameter("toDate");
		String fromDate = request.getParameter("fromDate");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("toDate", toDate);
		result.put("fromDate", fromDate);
		
		List<Map<String, Object>> resultMap  =  dao.manageBarList(result);
		
        return resultMap;
	}

	@Override
	public List<Map<String, Object>> mainChartStatistics(HttpServletRequest request) {
		
		String toDate = request.getParameter("toDate");
		String fromDate = request.getParameter("fromDate");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("toDate", toDate);
		result.put("fromDate", fromDate);
		
		logger.info("mainChart >> " + result);
		
		List<Map<String, Object>> resultMap  =  dao.mainChartStatistics(result);
		
        return resultMap;
	}

	@Override
	public List<Map<String, Object>> trueGridList(HttpServletRequest request) {
		
		String toDate = request.getParameter("toDate");
		String fromDate = request.getParameter("fromDate");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("toDate", toDate);
		result.put("fromDate", fromDate);
		
		try {
			List<Integer> patternList = detectionDao.queryCustomDataTypesCnt(); 
			result.put("patternList", patternList); 
		} catch (Exception e) {
			logger.error(e.toString());
		}
		
		List<Map<String, Object>> resultMap = dao.trueGridList(result);
		
		return resultMap;
	}
	
	@Override
	public List<Map<String, Object>> falseGridList(HttpServletRequest request) {
		
		String toDate = request.getParameter("toDate");
		String fromDate = request.getParameter("fromDate");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("toDate", toDate);
		result.put("fromDate", fromDate);
		
		try {
			List<Integer> patternList = detectionDao.queryCustomDataTypesCnt(); 
			result.put("patternList", patternList); 
		} catch (Exception e) {
			logger.error(e.toString());
		}
		
		List<Map<String, Object>> resultMap = dao.falseGridList(result);
		
		return resultMap;
	}

	@Override
	public Map<String, Object> totalStatistics(HttpServletRequest request) {
		
		String toDate = request.getParameter("toDate");
		String fromDate = request.getParameter("fromDate");
		
		Map<String, Object> result = new HashMap<>();
		
		result.put("toDate", toDate);
		result.put("fromDate", fromDate);
		
		Map<String, Object> resultMap = new HashMap<>();
		
		List<Integer> patternList = new ArrayList<>();
		try {
			patternList = detectionDao.queryCustomDataTypesCnt2();
			result.put("patternList", patternList); 
		} catch (SQLException e) {
			logger.error(e.toString());
		}
		
		resultMap = dao.totalStatistics(result);
		
		if(resultMap == null) {

			Map<String, Object> map = new HashMap<>();
			int type = 0;
			String percentage = "0.0%";
			
			map.put("COUNT_TARGET_ID", type);
			map.put("COUNT_HASH_ID", type);
			map.put("TOTAL", type);
			
			for(int i=1 ; i <= patternList.size(); i++) {
				map.put("TYPE"+i, type);
				map.put("TYPE"+i+"_PERCENTAGE", percentage);
			}
			
			return map;
		}
		
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> selectDataImple(HttpServletRequest request) {
		logger.info("selectDataImple check");
		
		Map<String, Object> result = new HashMap<>();
		List<Map<String, Object>> resultList = new ArrayList<>();
		
		try {
			String toDate = request.getParameter("toDate");
			String fromDate = request.getParameter("fromDate");
			result.put("toDate", toDate);
			result.put("fromDate", fromDate);
			
			resultList = dao.selectDataImple(result);
			
			for (Map<String, Object> map : resultList) { 
				long max_match = Long.parseLong(map.get("max_match").toString()); 
				long min_match = Long.parseLong(map.get("min_match").toString());
				if(min_match ==0 && max_match ==0) {
					map.put("rate", "0.00%"); // 소수점 두자리까지 표현
				}else if (min_match == 0) {
			        map.put("rate", "100.00%"); // min_match가 0이고 max_match가 0이 아닌 경우
			    }else { 
//				(현재값 - 이전값) / 이전값 * 10
					double match_cnt = ((double)(max_match - min_match) / min_match) * 100; 
					map.put("rate", String.format("%.2f", match_cnt)+"%"); // 소수점 두자리까지 표현
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("e :: " + e);
		}
		
		return resultList;
	}
	

}
