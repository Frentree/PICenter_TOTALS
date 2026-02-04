package com.org.iopts.report.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.org.iopts.exception.dao.piDetectionListDAO;
import com.org.iopts.report.dao.piSummaryDAO;
import com.org.iopts.report.service.piSummaryService;

import jakarta.inject.Inject;

@Service("piSummaryService")
@Transactional
public class piSummaryServiceImple implements piSummaryService {
	
	private static Logger log = LoggerFactory.getLogger(piSummaryServiceImple.class); 

	@Inject 
	private piSummaryDAO summaryDao;
	
	@Inject
	private piDetectionListDAO detectionDao; 

	@Override
	public List<HashMap<String, Object>> searchSummaryList(HashMap<String, Object> params) throws Exception {
		
		String path = params.get("SCH_PATH").toString();
		path = path.replaceAll("\\\\", "\\\\\\\\");
		params.put("SCH_PATH", path);
		
		try {
			// PICenter 에서 받아오는 개인정보 유형 갯수
			List<Integer> patternList = detectionDao.queryCustomDataTypesCnt(); 
			params.put("patternList", patternList); 
		} catch (RuntimeException e) {
		    log.error("Failed to query custom data types count", e);
		    //params.put("patternList", Collections.emptyList()); // 대체 값 설정
		}
		
		List<HashMap<String, Object>>  resultList = new ArrayList<HashMap<String,Object>>();
		try {
		    resultList = summaryDao.searchSummaryList(params);
		    List<Map<String, Object>> datatypeList = detectionDao.queryCustomDataTypes2();
		    resultList = getSumDataType(resultList, datatypeList);
		} catch (RuntimeException e) {
		    log.error("Failed to search summary list or process data types", e);
		    //resultList = Collections.emptyList(); // 대체 값 설정
		}
		
		return resultList;
	}
	
	@Override
	public List<HashMap<String, Object>> getMonthlyReport(HashMap<String, Object> params) throws Exception {
		
		log.info(params.get("year")+"");
		log.info(params.get("month")+"");
		
		String yyyymm = String.valueOf(params.get("year")) + String.valueOf(params.get("month"));
		return summaryDao.getMonthlyReport(yyyymm);
	}
	
	private List<HashMap<String, Object>> getSumDataType(List<HashMap<String, Object>> findList, List<Map<String, Object>> datatypesList) {
	    // 시작 시간
	    long startTime = System.nanoTime();

	    // datatypesList를 Map으로 변환하여 패턴 코드 검색 최적화
	    Map<Integer, String> patternToTypeMap = datatypesList.stream()
	            .collect(Collectors.toMap(
	                    datatype -> (int) datatype.get("ID"),
	                    datatype -> "TYPE" + datatype.get("ID").toString()
	            ));

	    // 결과를 저장할 Map
	    Map<String, HashMap<String, Object>> resultMap = new HashMap<>();

	    // findList 순회
	    findList.forEach(item -> {
	        // 고유 Key 생성
	        String key = generateKey(item);
	        int patter_idx = (int)item.get("PATTERN_IDX");
	        long matchCount = Long.parseLong(item.get("MATCH_COUNT").toString());

	        // 결과를 갱신할 Map 가져오기
	        HashMap<String, Object> result = resultMap.computeIfAbsent(key, k -> {
	            HashMap<String, Object> newItem = new HashMap<>(item);
	            newItem.put("TYPE", 0L); // 기본 TYPE 초기화
	            return newItem;
	        });

	        // 패턴 매칭과 결과 갱신
	        patternToTypeMap.forEach((PATTERN_IDX, type) -> {
	            if (patter_idx==PATTERN_IDX) {
	                long currentTypeValue = (long) result.getOrDefault(type, 0L);
	                result.put(type, currentTypeValue + matchCount);

	                long currentTotalTypeValue = (long) result.getOrDefault("TYPE", 0L);
	                result.put("TYPE", currentTotalTypeValue + matchCount);
	            }
	        });
	    });

	    // 필터링과 정렬 병합 처리
	    List<HashMap<String, Object>> filteredList = resultMap.values().stream()
	            .filter(value -> (long) value.getOrDefault("TYPE", 0L) > 0)
	            .sorted(Comparator.comparing((HashMap<String, Object> a) -> (String) a.get("TARGET_NAME"))
	                    .thenComparing(a -> (String) a.get("PATH")))
	            .collect(Collectors.toList());

	    return filteredList;
	}

	// 고유 키 생성 메서드
	private String generateKey(HashMap<String, Object> item) {
	    return item.get("TARGET_ID") + "_" + item.get("AP_NO") + "_" + item.get("HASH_ID");
	}
	
	@Override
	public List<HashMap<String, Object>> searchTargetSummaryReport(HashMap<String, Object> params) throws Exception {

		Gson gson = new Gson();
		//log.info("searchSummaryList : " + params.get("SCH_DMZ_SELECT").toString());
		//log.info("object :: " + params.get("SCH_OBJECT").toString());
		//String target = params.get("SCH_OBJECT").toString();
		  
		/*String path = params.get("SCH_PATH").toString();
		path = path.replaceAll("\\\\", "\\\\\\\\"); 
		params.put("SCH_PATH", path);*/
		String apArr = (String) params.get("SCH_AP"); 
		String targetArr = (String) params.get("SCH_TARGET");
		log.info("apArr :: "+apArr);
		log.info("targetArr :: "+targetArr); 
		log.info("targetArr :: "+targetArr.length());
		List<Map<String, Object>> targetList = new ArrayList<>();
		 
		if (targetArr != null && !targetArr.isEmpty() && JsonParser.parseString(targetArr).getAsJsonArray().size() > 0) {
			JsonArray targetJArr = JsonParser.parseString(targetArr).getAsJsonArray();
		    JsonArray apJArr = gson.toJsonTree(apArr).getAsJsonArray();
			for(int i = 0; i < targetJArr.size(); i++) {
				String targetID = targetJArr.get(i).getAsString();;
				int ap_no = Integer.parseInt(apJArr.get(i).toString());
				Map<String, Object> targetMap = new HashMap<>();
				targetMap.put("target_id", targetID);
				targetMap.put("ap_no", ap_no);
				
				targetList.add(targetMap); 
			}
			params.put("targetList", targetList);
		}
		
		List<Integer> patternList = detectionDao.queryCustomDataTypesCnt(); 
		params.put("patternList", patternList); 
		 
		List<Map<String, Integer>> datatypesList = new ArrayList<>();
		Map<String, Integer> datatypesMap = new HashMap<>();
		
		for(int i=0 ; i< patternList.size() ; i++) {
			datatypesMap = new HashMap<>();
			datatypesMap.put("type", patternList.get(i));
			datatypesMap.put("type_cnt", i);
			
			datatypesList.add(datatypesMap);
		}
		params.put("datatypesList", datatypesList);
		
		return summaryDao.searchTargetSummaryReport(params);
	}
	
}