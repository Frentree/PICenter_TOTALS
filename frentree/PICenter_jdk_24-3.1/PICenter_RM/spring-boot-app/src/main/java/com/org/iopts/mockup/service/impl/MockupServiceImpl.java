package com.org.iopts.mockup.service.impl;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.iopts.mockup.dao.MockupDAO;
import com.org.iopts.mockup.service.MockupService;
import com.org.iopts.util.SessionUtil;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class MockupServiceImpl implements MockupService{


	@Value("${recon.id}")
	private String recon_id;

	@Value("${recon.password}")
	private String recon_password;

	@Value("${recon.url}")
	private String recon_url;
	
	@Value("${recon.api.version}")
	private String api_ver;

	@Inject
	private MockupDAO mockupdao;
	
	private static Logger logger = LoggerFactory.getLogger(MockupServiceImpl.class);
	
	// 왼쪽 위 대쉬보드
	@Override
	public List<Map<String, Object>> allTargetList(HttpServletRequest request) throws Exception {
	    String user_no = SessionUtil.getSession("memberSession", "USER_NO");
	    String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
//	    String user_no = "2025012";
//	    String user_grade = "1";
		
	    Map<String, Object> param = new HashMap<>();

	    // 등급이 9가 아니면 user_no 필터링 추가
	    if(!"9".equals(user_grade)) {
	        param.put("userNo", user_no);
	    }		
	    return mockupdao.allTargetList(param);
	}
	

	
	@Override
	public Map<String, Object> getServerDataPivot(HttpServletRequest request) throws Exception {
	    String user_no = SessionUtil.getSession("memberSession", "USER_NO");
	    String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");

//	    String user_no = "2025012";
//	    String user_grade = "1";

	    // 1. 서버 컬럼 정보 조회
	    List<Map<String, Object>> serverColumns = mockupdao.getServerColumns();

	    List<Map<String, Object>> filteredColumns = new ArrayList<>();
	    for(Map<String, Object> col : serverColumns) {
	        if(col != null && col.get("NAME") != null && col.get("IDX") != null) {
	            filteredColumns.add(col);
	        }
	    }

	    Map<String, Object> param = new HashMap<>();
	    param.put("serverColumns", filteredColumns);

	    // 등급이 9가 아니면 USER_NO 필터링 추가
	    if(!"9".equals(user_grade)) {
	        param.put("userNo", user_no);
	    }

	    // 2. 최적화된 통합 쿼리로 모든 데이터 한 번에 조회
	    List<Map<String, Object>> pivotDatas = mockupdao.getAllServerDataWithDetails(param);

	    // 3. 커스텀 패턴 조회
	    List<Map<String, Object>> customPatterns = mockupdao.customPatterns();
	    logger.info("customPatterns 조회 결과: " + (customPatterns != null ? customPatterns.size() + "개" : "null"));

	    // 차트용 타입별 합계 계산
	    Map<String, Integer> serverTypeSum = new HashMap<>();
	    Map<String, Integer> dbTypeSum = new HashMap<>();
	    Set<String> allPiiTypes = new HashSet<>();
	    Map<String, String> codeToNameMap = new HashMap<>();

	    Gson gson = new Gson();

	    // 4. JSON 파싱 (StatisticsServiceImpl 패턴)
	    for(Map<String, Object> pivotData : pivotDatas) {
	        String platform = (String) pivotData.get("PLATFORM");
	        String dataTypesJson = (String) pivotData.get("DATA_TYPES_JSON");

	        if(dataTypesJson == null || dataTypesJson.isEmpty()) {
	            continue;
	        }

	        try {
	            String[] jsonStrings = dataTypesJson.split("\\|\\|\\|");
	            Map<String, Integer> patternSums = new HashMap<>();

	            for(String jsonStr : jsonStrings) {
	                if(jsonStr == null || jsonStr.trim().isEmpty()) continue;

	                JsonArray arr = gson.fromJson(jsonStr, JsonArray.class);
	                for(JsonElement item : arr) {
	                    JsonObject obj = item.getAsJsonObject();
	                    String patternCode = obj.get("PATTERN_CODE").getAsString();
	                    String patternNm = obj.get("PATTERN_NM").getAsString();
	                    int matchCnt = obj.get("MATCH_CNT").getAsInt();

	                    allPiiTypes.add(patternCode);

	                    if(!codeToNameMap.containsKey(patternCode)) {
	                        codeToNameMap.put(patternCode, patternNm);
	                    }

	                    patternSums.put(patternCode, patternSums.getOrDefault(patternCode, 0) + matchCnt);
	                }
	            }

	            // 개별 row에 패턴별 값 추가
	            for(Map.Entry<String, Integer> entry : patternSums.entrySet()) {
	                pivotData.put(entry.getKey(), entry.getValue());

	                // 플랫폼별 타입 합계에 누적
	                if("DB".equals(platform)) {
	                    dbTypeSum.put(entry.getKey(), dbTypeSum.getOrDefault(entry.getKey(), 0) + entry.getValue());
	                } else {
	                    serverTypeSum.put(entry.getKey(), serverTypeSum.getOrDefault(entry.getKey(), 0) + entry.getValue());
	                }
	            }

	        } catch(Exception e) {
	            logger.error("JSON 파싱 실패: " + dataTypesJson, e);
	        }

	        pivotData.remove("DATA_TYPES_JSON"); // JSON 원본 제거
	    }

	    Map<String, Object> result = new HashMap<>();

	    result.put("customPatterns", customPatterns);
	    result.put("gridData", pivotDatas);
	    result.put("serverTypes", serverTypeSum);
	    result.put("dbTypes", dbTypeSum);
	    result.put("serverColumns", filteredColumns);
	    result.put("piiTypes", new ArrayList<>(allPiiTypes));
	    result.put("codeToNameMap", codeToNameMap);

	    logger.info("최종 result customPatterns size: " + (customPatterns != null ? customPatterns.size() : "null"));
	    logger.info("최종 result gridData size: " + (pivotDatas != null ? pivotDatas.size() : "null"));

	    return result;
	}
	
	@Override
	public List<Map<String, Object>> getServerColumns(HttpServletRequest request) throws Exception {
		 return mockupdao.getServerColumns();
	}
	
}
