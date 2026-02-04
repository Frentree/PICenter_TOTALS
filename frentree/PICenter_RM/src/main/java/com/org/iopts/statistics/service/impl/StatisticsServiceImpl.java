  package com.org.iopts.statistics.service.impl;

  import java.sql.SQLException;
  import java.util.ArrayList;
  import java.util.Collections;
  import java.util.Comparator;
  import java.util.HashMap;
  import java.util.List;
  import java.util.Map;

  import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;
  import org.springframework.beans.factory.annotation.Value;
  import org.springframework.stereotype.Service;
  import org.springframework.transaction.annotation.Transactional;

  import com.google.gson.Gson;
  import com.google.gson.JsonArray;
  import com.google.gson.JsonElement;
  import com.google.gson.JsonObject;
  import com.org.iopts.dao.Pi_Custom_PatternDAO;
  import com.org.iopts.dto.Pi_Custom_PatternVO;
  import com.org.iopts.exception.dao.piDetectionListDAO;
  import com.org.iopts.statistics.dao.StatisticsDAO;
  import com.org.iopts.statistics.service.StatisticsService;

  import jakarta.inject.Inject;
  import jakarta.servlet.http.HttpServletRequest;

  @Service
  @Transactional
  public class StatisticsServiceImpl implements StatisticsService{

        private static Logger logger = LoggerFactory.getLogger(StatisticsServiceImpl.class);

        @Value("${recon.api.version}")
        private String api_ver;

        @Inject
        private StatisticsDAO dao;

        @Inject
        private Pi_Custom_PatternDAO cpdao;

        @Inject
        private piDetectionListDAO detectionDao;

        @Override
        public Map<String, Object> statisticsList(HttpServletRequest request) {
                // 패턴 파라미터 가져오기
                Map<String, Object> params = getPatternParameters();
                Map<String, String> patternNameMap = (Map<String, String>) params.get("patternNameMap");
                List<Map<String, Object>> patternInfoList = (List<Map<String, Object>>) params.get("patternInfoList");

                // 날짜 파라미터 추가
                String toDate = request.getParameter("toDate");
                String fromDate = request.getParameter("fromDate");
                params.put("toDate", toDate);
                params.put("fromDate", fromDate);

                List<Map<String, Object>> resultData = dao.statisticsList(params);
                Gson gson = new Gson();

                for(Map<String, Object> map : resultData) {
                        String type = "";
                        String max = "";
                        String two_max = "";
                        int max_result = 0;
                        int two_max_result = 0;

                        // DATA_TYPES JSON 파싱하여 패턴별 합계 계산
                        Map<Integer, Integer> patternSumMap = new HashMap<>();
                        String dataTypes = map.get("DATA_TYPES") != null ? map.get("DATA_TYPES").toString() : "";

                        if (!dataTypes.isEmpty()) {
                                String[] dataTypeArr = dataTypes.split("\\|\\|\\|");
                                for (String dt : dataTypeArr) {
                                        if (dt != null && !dt.trim().isEmpty()) {
                                                try {
                                                        JsonArray jsonArray = gson.fromJson(dt.trim(), JsonArray.class);
                                                        for (JsonElement element : jsonArray) {
                                                                JsonObject obj = element.getAsJsonObject();
                                                                int patternNum = obj.get("PATTERN_NUM").getAsInt();
                                                                int matchCnt = obj.get("MATCH_CNT").getAsInt();
                                                                patternSumMap.put(patternNum, patternSumMap.getOrDefault(patternNum, 0) + matchCnt);
                                                        }
                                                } catch (Exception e) {
                                                        logger.error("JSON parsing error: " + e.getMessage());
                                                }
                                        }
                                }
                        }

                        // 패턴별 값을 map에 추가하고 최댓값 찾기
                        for (Map<String, Object> patternInfo : patternInfoList) {
                                Integer patternIdx = (Integer) patternInfo.get("PATTERN_IDX");
                                String enName = (String) patternInfo.get("PATTERN_EN_NAME");
                                int value = patternSumMap.getOrDefault(patternIdx, 0);
                                map.put(enName, value);

                                if (value > max_result) {
                                        two_max_result = max_result;
                                        max_result = value;
                                } else if (value > two_max_result && value < max_result) {
                                        two_max_result = value;
                                }
                        }

                        // 최댓값과 같은 패턴들 찾기
                        for (String enName : patternNameMap.keySet()) {
                                if (map.containsKey(enName)) {
                                        int value = (int) Double.parseDouble(map.get(enName).toString());

                                        if (value == max_result && max_result > 0) {
                                                if (max.equals("")) {
                                                        max = patternNameMap.get(enName);
                                                } else if (!max.contains(",")) {
                                                        max += ", " + patternNameMap.get(enName);
                                                }
                                        }
                                }
                        }

                        type = max;

                        // 두 번째 최댓값 처리
                        if (two_max_result != 0 && !max.contains(",")) {
                                for (String enName : patternNameMap.keySet()) {
                                        if (map.containsKey(enName)) {
                                                int value = (int) Double.parseDouble(map.get(enName).toString());

                                                if (value == two_max_result) {
                                                        two_max = ", " + patternNameMap.get(enName);
                                                        break;
                                                }
                                        }
                                }
                                type += two_max;
                        }

                        map.put("TYPE", type);
                }

                logger.info("resultData >> " + resultData);

                // 결과 맵 구성
                Map<String, Object> result = new HashMap<>();
                result.put("data", resultData);
                result.put("patternNameMap", patternNameMap);

                return result;
        }

        // 전체 서버 점검 결과 (의심 건수) 조회
        @Override
        public Map<String, Object> manageList(HttpServletRequest request) {

                String toDate = request.getParameter("toDate");
                String fromDate = request.getParameter("fromDate");

                // 패턴 파라미터 가져오기
                Map<String, Object> params = getPatternParameters();

                // 날짜 파라미터 추가
                params.put("toDate", toDate);
                params.put("fromDate", fromDate);

                List<Map<String, Object>> resultData = dao.manageList(params);

                // 결과 맵 구성
                Map<String, Object> result = new HashMap<>();
                result.put("data", resultData);
                result.put("patternNameMap", params.get("patternNameMap"));
                result.put("patternInfoList", params.get("patternInfoList")); // 순서 정보 추가

                return result;
        }

        @Override
        public List<Map<String, Object>> manageBarList(HttpServletRequest request) {

                String toDate = request.getParameter("toDate");
                String fromDate = request.getParameter("fromDate");

                Map<String, Object> result = new HashMap<String, Object>();
                result.put("toDate", toDate);
                result.put("fromDate", fromDate);

                List<Integer> patternList = null;

                try {
                        // PICenter 에서 받아오는 개인정보 유형 갯수
                        patternList = detectionDao.queryCustomDataTypesCnt();
                } catch(SQLException e) {
                        logger.error("error :: " , e.toString());
                } catch(Exception e) {
                        logger.error("error :: " , e.toString());
                }
                result.put("patternList", patternList);

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
        public Map<String, Object> totalStatistics(HttpServletRequest request) {
                // 패턴 파라미터 가져오기 (DAO 호출용)
                Map<String, Object> params = getPatternParameters();

                // DAO 호출해서 데이터 가져오기
                List<Map<String, Object>> resultData = dao.totalStatistics(params);

                // 결과 맵 구성
                Map<String, Object> result = new HashMap<>();
                result.put("data", resultData);
                result.put("patternNameMap", params.get("patternNameMap"));
                result.put("patternColorMap", params.get("patternColorMap")); // 색상 매핑 추가
                result.put("patternInfoList", params.get("patternInfoList")); // 순서 정보 추가

                return result;
        }

        @Override
        public List<Map<String,Object>> statisticsPolicyList(HttpServletRequest request) {

                // 패턴 파라미터 가져오기
                Map<String, Object> params = getPatternParameters();
                List<Map<String, Object>> patternInfoList = (List<Map<String, Object>>) params.get("patternInfoList");

                Map<String, Object> dbResult = dao.statisticsPolicyList(params);
                HashMap<Integer, Integer> patternSums = new HashMap<>();

                String dataTypesStr = dbResult != null && dbResult.get("DATA_TYPES") != null
                                ? dbResult.get("DATA_TYPES").toString() : "";

                if (!dataTypesStr.isEmpty()) {
                        Gson gson = new Gson();
                        try {
                                String[] jsonStrings = dataTypesStr.split("\\|\\|\\|");
                                for (String jsonStr : jsonStrings) {
                                        if (jsonStr != null && !jsonStr.trim().isEmpty()) {
                                                JsonArray arr = gson.fromJson(jsonStr, JsonArray.class);
                                                for (JsonElement item : arr) {
                                                        JsonObject obj = item.getAsJsonObject();
                                                        int patternNum = obj.get("PATTERN_NUM").getAsInt();
                                                        int matchCnt = obj.get("MATCH_CNT").getAsInt();
                                                        if (patternSums.containsKey(patternNum)) {
                                                                patternSums.put(patternNum, patternSums.get(patternNum) + matchCnt);
                                                        } else {
                                                                patternSums.put(patternNum, matchCnt);
                                                        }
                                                }
                                        }
                                }
                        } catch (Exception e) {
                                logger.error("JSON parsing error: " + dataTypesStr, e);
                        }
                }

                List<Map<String, Object>> resultList = new ArrayList<>();

                // 패턴별 결과 생성
                for (Map<String, Object> patternInfo : patternInfoList) {
                        Integer patternIdx = (Integer) patternInfo.get("PATTERN_IDX");
                        String krName = (String) patternInfo.get("PATTERN_KR_NAME");
                        int matchCount = patternSums.getOrDefault(patternIdx, 0);

                        Map<String, Object> resultMap = new HashMap<>();
                        resultMap.put("DATA_TYPE", krName);
                        resultMap.put("MATCH_COUNT", matchCount);
                        resultList.add(resultMap);
                }

                // 내림차순 정렬 (높은 값부터)
                Collections.sort(resultList, new Comparator<Map<String, Object>>() {
                        @Override
                        public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                                Integer count1 = (Integer) o1.get("MATCH_COUNT");
                                Integer count2 = (Integer) o2.get("MATCH_COUNT");
                                return count2.compareTo(count1);
                        }
                });

                // TOP 5만 반환
                List<Map<String, Object>> resultList2 = new ArrayList<>();
                int maxSize = Math.min(5, resultList.size());

                for(int i = 0; i < maxSize; i++) {
                        resultList2.add(resultList.get(i));
                }

                return resultList2;
        }

        @Override
        public List<HashMap<String, Object>> excelDown(HashMap<String, Object> params) throws Exception {
                return dao.excelDown(params);
        }

        /**
         * 패턴 정보를 가져와서 파라미터 맵을 생성하는 메서드
         */
        private Map<String, Object> getPatternParameters() {
                List<Pi_Custom_PatternVO> patternMappings = new ArrayList<>();
                try {
                        patternMappings = cpdao.selectCustomPattern();
                        if (patternMappings == null) {
                                patternMappings = new ArrayList<>();
                        }
                } catch (Exception e) {
                        logger.error("selectCustomPattern error: ", e);
                        patternMappings = new ArrayList<>();
                }

                List<Integer> patternList = new ArrayList<>();
                Map<String, String> patternNameMap = new HashMap<>();
                Map<String, String> patternColorMap = new HashMap<>(); // 색상
                List<Map<String, Object>> patternInfoList = new ArrayList<>(); // 추가

                for (Pi_Custom_PatternVO pattern : patternMappings) {
                        if (pattern.getPattern_idx() != 0) {
                                patternList.add(pattern.getPattern_idx());
                        }

                        if (pattern.getPattern_en_name() != null && pattern.getPattern_kr_name() != null) {
                                patternNameMap.put(pattern.getPattern_en_name(), pattern.getPattern_kr_name());

                                if (pattern.getColor_code() != null) {
                                        patternColorMap.put(pattern.getPattern_en_name(), pattern.getColor_code());
                                }

                                // 패턴 정보를 맵으로 추가 (XML에서 사용)
                                Map<String, Object> patternInfo = new HashMap<>();
                                patternInfo.put("PATTERN_IDX", pattern.getPattern_idx());
                                patternInfo.put("PATTERN_EN_NAME", pattern.getPattern_en_name());
                                patternInfo.put("PATTERN_KR_NAME", pattern.getPattern_kr_name());
                                patternInfo.put("COLOR_CODE", pattern.getColor_code()); // 색상 코드 추가
                                patternInfoList.add(patternInfo);
                        }
                }

                Map<String, Object> params = new HashMap<>();
                params.put("patternList", patternList);
                params.put("patternMappings", patternMappings);
                params.put("patternNameMap", patternNameMap);
                params.put("patternColorMap", patternColorMap); // 색상 매핑 추가
                params.put("patternInfoList", patternInfoList); // XML에서 사용할 패턴 정보

                logger.info("Pattern parameters created - patternList size: " + patternList.size());

                return params;
        }
  }