package com.org.iopts.exception.service.impl;

import java.io.Reader;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.ibatis.io.Resources;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.iopts.dao.Pi_UserDAO;
import com.org.iopts.detection.controller.piDetectionController;
import com.org.iopts.detection.dao.piDetectionDAO;
import com.org.iopts.detection.dao.piExceptionDAO;
import com.org.iopts.exception.dao.piDetectionListDAO;
import com.org.iopts.exception.service.piDetectionListService;
import com.org.iopts.mockup.dao.MockupDAO;
import com.org.iopts.exception.vo.detection;
import com.org.iopts.util.ReconUtil;
import com.org.iopts.util.RemediationUtil;
import com.org.iopts.util.ServletUtil;
import com.org.iopts.util.SessionUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


@Service
@Transactional
public class piDetectionListServiceImple implements piDetectionListService {

	private static Logger log = LoggerFactory.getLogger(piDetectionListServiceImple.class);

	@Inject
	private piDetectionListDAO dao;
	
	@Inject
	private piExceptionDAO exceptionDao;
	
	@Inject
	private Pi_UserDAO userDao;

	@Inject
	private MockupDAO mockupDAO;

	@Value("${user.key}")
	private String key;
	
	/**
	 *
	 */
	@Override
	public List<HashMap<String, Object>> selectFindSubpath(HashMap<String, Object> params) throws SQLException {
		
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		params.put("user_no"  , user_no);
		String location = params.get("location").toString();
		String processingFlag = params.get("processingFlag").toString();
		String patternFlag = null;
		location = location.replaceAll("\\\\", "\\\\\\\\");

		try {
			patternFlag = params.get("patternFlag").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String onedriveChk = params.get("onedriveChk").toString();
		String name = params.get("name").toString();
		String text = params.get("text").toString();
		
		params.put("onedriveChk", onedriveChk);
		params.put("status", processingFlag);
		params.put("patternFlag", patternFlag);
		params.put("location", location);
		params.put("name", name);
		params.put("text", text);
		
		if(params.get("targetList").toString() != null) {
			
			JSONParser parser = new JSONParser();
			try {
				JSONArray jsonArray = (JSONArray) parser.parse((String) params.get("targetList"));
				params.put("targetList", jsonArray);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		
		List<HashMap<String, Object>> findList = new ArrayList<>();
		List<HashMap<String, Object>> dataList = new ArrayList<>();
		List<HashMap<String, Object>> resultList = new ArrayList<>();

		try {
			List<Map<String, Object>> patternList = dao.queryCustomDetailDataTypes();

			// 동적 컬럼 (관리자, 운영자 등) 조회
			List<Map<String, Object>> serverColumns = mockupDAO.getServerColumns();
			params.put("serverColumns", serverColumns);
			
			List<Map<String, Object>> tList = (List<Map<String, Object>>) params.get("targetList");
			if(tList != null) {
				for (Map<String, Object> map : tList) {
					params.put("searchTargetId", map.get("TARGET_ID"));
					params.put("searchApNo", map.get("AP_NO"));
					dataList = dao.selectFindSubpath4(params); // 결과조회/조치계획 list 저장
					findList.addAll(dataList);
				}
			}else {
				params.put("searchTargetId", params.get("target_id"));
				params.put("searchApNo", params.get("ap_no"));
				dataList = dao.selectFindSubpath4(params); // 결과조회/조치계획 list 저장
				findList.addAll(dataList);
			}
			
			findList.sort(Comparator.comparing((HashMap<String, Object> a) -> (String) a.get("HOST_NAME")).thenComparing(a -> (String) a.get("PATH")));
			
			Map<String, Object> matchCountMap = new HashMap<>();
			Map<String, Object> matchTypeMap = new HashMap<>();
			
			Map<String, Object> hostPatternCnt =  new HashMap<String, Object>();

			Set<String> pathKeySet = new HashSet();
			Set<String> hostKeySet = new HashSet();

			for (HashMap<String, Object> findMap : findList) {
				
				matchTypeMap = new HashMap<>();
				
				String host_key = findMap.get("TARGET_ID") + "|" + findMap.get("AP_NO");
			    String path_key = findMap.get("TARGET_ID") + "|" + findMap.get("AP_NO") + "|" + findMap.get("HASH_ID");
			    int matchCount = Integer.parseInt(findMap.get("MATCH_COUNT").toString());
			    String pattern_idx = findMap.get("PATTERN_IDX").toString();
			    
			    if(!hostKeySet.contains(host_key)) {
			    	hostKeySet.add(host_key);
			    	
			    	hostPatternCnt = dao.hostPatternCnt(findMap);
			    }
			    
			    if(matchCount > Integer.parseInt(hostPatternCnt.get(pattern_idx+"_CNT").toString())) {
			    	if (!pathKeySet.contains(path_key)) {
				        // 처음 나오는 path_key 저장
				    	resultList.add(findMap);
				        pathKeySet.add(path_key);
				        
				        matchTypeMap.put(pattern_idx, matchCount);
				        matchCountMap.put(path_key, matchTypeMap);
				    } else {
				        // 중복된 경우 MATCH_COUNT 합산
				    	matchTypeMap = (Map<String, Object>) matchCountMap.get(path_key);
				    	int matchCnt  = 0;
				    	
				    	if(matchTypeMap.containsKey(pattern_idx)) {
				    		matchTypeMap.put(pattern_idx, Integer.parseInt(matchTypeMap.get(pattern_idx).toString())+matchCount);
				    	}else {
				    		 matchTypeMap.put(pattern_idx, matchCount);
				    	}
				    	matchCountMap.put(path_key, matchTypeMap);
				    }
			    }
			}

			// 결과 출력
			for (HashMap<String, Object> map : resultList) {
			    String path_key = map.get("TARGET_ID") + "|" + map.get("AP_NO") + "|" + map.get("HASH_ID");
			    
			    Map<String, Object> gridMap = (Map<String, Object>) matchCountMap.get(path_key);
			    int Total = 0;
			    for (Map<String, Object> pattern : patternList) {
			    	
			    	String patternID = pattern.get("ID").toString();
			    	
			    	if(gridMap.containsKey(pattern.get("ID"))) {
			    		map.put(patternID, gridMap.get(patternID));
			    		Total += Integer.parseInt(gridMap.get(patternID).toString());
			    	}else {
			    		map.put(patternID.toString(), 0);
			    	}
				}
			    
			    map.put("TYPE", Total);
			}
			
		} catch(Exception e) {
			log.error(e.toString());
			e.printStackTrace();
		}
		
		return resultList;
	}
	
	private List<HashMap<String, Object>> patternSorting(List<HashMap<String, Object>> findMap) {

		List<HashMap<String, Object>> resultList = new ArrayList<>();
        Map<String, Integer> resultMap = new HashMap<>();
        
        // 리스트를 순회하며 키 조합 카운트
        for (HashMap<String, Object> map : findMap) {
            String key = map.get("TARGET_ID") + "|" + map.get("AP_NO") + "|" + map.get("HASH_ID");
            resultMap.put(key, resultMap.getOrDefault(key, 0) + 1);
        }

        // 다시 순회하며 중복된 값만 결과 리스트에 추가
        for (HashMap<String, Object> map : findMap) {
            String key =  map.get("TARGET_ID") + "|" + map.get("AP_NO") + "|" + map.get("HASH_ID");
            if (resultMap.get(key) > 1) {
                resultList.add(map);
            }
        }

        return resultList;
	}

	@Override
	public List<HashMap<String, Object>> selectGroupFindSubpath(HashMap<String, Object> params) throws SQLException {
		
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		params.put("user_no"  , user_no);
		
		List<String> deletionList = (List<String>)params.get("idxList");
		
		List<HashMap<String, Object>>findMap = new ArrayList<>();
		try {
			findMap = dao.selectGroupFindSubpath(params);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return findMap;  
	}
	
	@Override
	public List<HashMap<String, Object>> selectPICenterGroupFindSubpath(HashMap<String, Object> params) throws SQLException {
		
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		params.put("user_no"  , user_no);
		
		List<String> deletionList = (List<String>)params.get("idxList");
		
		List<HashMap<String, Object>>findMap = new ArrayList<>();
		try {
			findMap = dao.selectPICenterGroupFindSubpath(params);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return findMap;
	}
	
	@Override
	public List<HashMap<String, Object>> selectDetectionApprovalList(HashMap<String, Object> params) throws SQLException {
		
		/*
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		params.put("user_no"  , user_no);
		String location = params.get("location").toString();
		location = location.replaceAll("\\\\", "\\\\\\\\");
		params.put("location", location);
		*/	
		List<HashMap<String, Object>>findMap = dao.selectDetectionApprovalList(params);

		return findMap;
	}
	
	@Override
	public List<Map<String, Object>> getDetectionApprovalList(HttpServletRequest request) throws Exception {
		
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");

		List<Map<String, Object>> resultList = new ArrayList<>();
		try {
			
			Map<String, Object> map = new HashMap<>();
			map.put("user_no", user_no);
			map.put("user_grade", user_grade);
			map.put("idx", request.getParameter("idx"));
			map.put("selectList", request.getParameter("selectList"));
			map.put("schPath", request.getParameter("schPath"));
			
			resultList = dao.getDetectionApprovalList(map);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultList;
	}
	
	@Override
	public List<HashMap<String, Object>> subpathSelect(HashMap<String, Object> params) throws SQLException {
		
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		params.put("user_no"  , user_no);

		List<HashMap<String, Object>>findMap = dao.subpathSelect(params);

		return findMap;
	}
	

	@Override
	public List<Map<String, Object>> selectUserTargetList(HttpServletRequest request) throws Exception {

		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		String host = request.getParameter("host");
		
		Map<String, Object> searchMap = new HashMap<String, Object>();
		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
		
		searchMap.put("user_no", user_no);
		searchMap.put("user_grade", user_grade);
		searchMap.put("host", host);
		map = dao.selectUserTargetList(searchMap);	
		return map;
	}

	@Override
	public HashMap<String, Object> selectProcessDocuNum(HashMap<String, Object> params) {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		params.put("user_no", user_no);

		HashMap<String, Object> memberMap = dao.selectProcessDocuNum(params);

		return memberMap;
	}

	@Override
	public HashMap<String, Object> registProcessGroup(HashMap<String, Object> params) {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		List<String> deletionList = (List<String>)params.get("deletionList");

		params.put("user_no", user_no);
		params.put("hash_id", deletionList.get(0));
		params.put("ap_no", params.get("ap_no"));

		dao.registProcessGroup(params);

		return params;
	}

	@Override
	public void registProcess(HashMap<String, Object> params, HashMap<String, Object> groupMap) {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		List<String> deletionList = (List<String>)params.get("deletionList");

		for (int i = 0; i < deletionList.size(); i++) {
			params.put("user_no", user_no);
			params.put("hash_id", deletionList.get(i));
			params.put("data_processing_group_idx", groupMap.get("idx"));

			dao.registProcess(params);
		}
	}

	@Override
	public void cancelApproval(HashMap<String, Object> params) {
		List<String> deletionList = (List<String>)params.get("deletionList");
		for(int i=0; i<deletionList.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("target_id", params.get("target_id"));
			map.put("hash_id", deletionList.get(i));
			String key = dao.selectIdx(map);
			map.put("key", key);
			
			log.info(map.toString());
			
			if(!"".equals(key) && key != null) {
				dao.deleteDataProcessing(map);
				int group_idx_cnt = dao.getCountProcessingGroup(map);
				if(group_idx_cnt < 1) {
					dao.deleteDataProcessingGroup(map);
				}
			}
			
			
		}
	}
	

	@Override
	public List<HashMap<String, Object>> personalApprovalData(HashMap<String, Object> params) throws SQLException {
		String idx = params.get("idx").toString();
		params.put("idx", idx);

		List<HashMap<String, Object>>findMap = dao.personalApprovalData(params);

		return findMap;
	}
	
	@Override
	public List<Map<String, Object>> queryCustomDataTypes() throws SQLException {

		List<Map<String, Object>> map = dao.queryCustomDataTypes();

		return map;
	}
	
    @Override
    public List<Map<String, Object>> queryCustomDetailDataTypes() throws SQLException {
        
        List<Map<String, Object>> map = dao.queryCustomDetailDataTypes();
        
        return map;
    }
	
	@Override
	public List<Map<String, Object>> queryMatchDetail() throws SQLException {
		
		List<Map<String, Object>> map = dao.queryMatchDetail();
		
		return map;
	}
	
	@Override
    public List<Map<String, Object>> getExceptionFlag() throws Exception {
        return dao.getExceptionFlag();
    }
//	
//	// 관리자 오탐 처리 (암호화/삭제)
//	@Override
//	public void registRemdiation(HttpServletRequest request, HashMap<String, Object> params) throws Exception {
//	    
//	    List<Map<String,Object>> aFalseMatchList = (List<Map<String, Object>>)params.get("aList");
//	    Map<String,Object> resultMap = new HashMap<String, Object>();
//	    String flag = params.get("flag").toString();
//	    String user_no = SessionUtil.getSession("memberSession", "USER_NO");
//	    String password = params.get("password") == null ? "" : params.get("password").toString();
//	    
//	    for(Map<String,Object> falseMatch : aFalseMatchList) {
//	        String result = "";
//	        String errorMessage = "";
//	        
//	        String hash_id = falseMatch.get("hash_id").toString();
//	        Map<String,Object> data_pro = dao.getFindByHash(falseMatch);
//	        if (data_pro == null) {
//	            data_pro = dao.getDBFindByHash(falseMatch);
//	        }
//	        
//	        // 하위 파일 포함 모든 파일 조회
//	        data_pro.put("allow_reprocess", "Y");
//	        List<Map<String,Object>> false_match_list = dao.selectFalseMatchApi(data_pro);
//	        
//	        // ========== 디버깅 로그 ==========
//	        log.info("========== Remediation Debug ==========");
//	        log.info("HASH_ID: " + data_pro.get("HASH_ID"));
//	        log.info("FLAG: " + flag);
//	        log.info("Total rows from CTE: " + false_match_list.size());
//	        if(false_match_list.size() == 0) {
//	            log.error("❌ CTE returned NO rows!");
//	        }
//	        
//	        if(false_match_list.size() != 0) {
//	            
//	            // ========== 1. 상세 테이블 INSERT (모든 파일) ==========
//	            for(Map<String,Object> false_match : false_match_list) {
//	                Map<String, Object> detailMap = new HashMap<>();
//	                detailMap.put("target_id", data_pro.get("TARGET_ID"));
//	                detailMap.put("hash_id", false_match.get("ID"));
//	                detailMap.put("ap_no", data_pro.get("AP_NO"));
//	                detailMap.put("fid", false_match.get("FID"));
//	                detailMap.put("original_path", false_match.get("PATH"));
//	                detailMap.put("new_path", null);  // 암호화/삭제는 경로 변경 없음
//	                detailMap.put("status", "PENDING");
//	                detailMap.put("user_no", user_no);
//	                detailMap.put("action", flag);  // delete 또는 encrypt
//	                
//	                // PI_REMEDIATION_DETAIL INSERT
//	                dao.insertRemediationDetail(detailMap);
//	            }
//	            
//	            // ========== 2. API 호출 준비 ==========
//	            Map<String, Object> apiMap = new HashMap<>();
//	            JSONArray array = new JSONArray();
//	            JSONObject exInfo = new JSONObject();
//	            JSONArray idsArray = new JSONArray();
//	            
//	            exInfo.put("path", data_pro.get("PATH"));
//	            exInfo.put("sign_off", user_no + "_PICenter");
//	            if(!password.equals("")) {
//	                exInfo.put("password", password);
//	            }
//	            
//	            String uri = "/targets/" + data_pro.get("TARGET_ID") + "/locations/" + data_pro.get("LOCATION_ID") + "/remediation/" + flag;
//	            
//	            // ========== 모든 FID를 ARRAY에 추가 (FLAG 무관) ==========
//	            for(Map<String,Object> false_match : false_match_list) {
//	                if(false_match.get("FID") != null) {
//	                    idsArray.add(false_match.get("FID"));
//	                }
//	            }
//	            
//	            exInfo.put("object_ids", idsArray);
//	            
//	            apiMap.put("AP_NO", data_pro.get("AP_NO"));
//	            array.add(exInfo);
//	            apiMap.put("Data", array.toString());
//	            
//	            log.info("apiMap : " + apiMap);
//	            
//	            // ========== 3. Recon API 호출 ==========
//	            resultMap = postDataApi(apiMap, uri);
//	            
//	            log.info("resultMap : " + resultMap);
//	            
//	            // 처리 시간 기록
//	            java.util.Date processDate = new java.util.Date();
//	            
//	            if(resultMap.get("resultCode").toString().equals("202")) {
//	                // ========== 성공 처리 ==========
//	                result = "성공";
//	                
//	                // ========== 4. 상세 테이블 UPDATE (성공) ==========
//	                for(Map<String,Object> false_match : false_match_list) {
//	                    Map<String, Object> detailUpdateMap = new HashMap<>();
//	                    detailUpdateMap.put("hash_id", false_match.get("ID"));
//	                    detailUpdateMap.put("target_id", data_pro.get("TARGET_ID"));
//	                    detailUpdateMap.put("ap_no", data_pro.get("AP_NO"));
//	                    detailUpdateMap.put("status", "SUCCESS");
//	                    detailUpdateMap.put("process_date", processDate);
//	                    
//	                    dao.updateRemediationDetailStatus(detailUpdateMap);
//	                }
//	                
//	                // ========== 5. 기존 테이블 업데이트 (호환성 유지) ==========
//	                int resultCode = 1;
//	                for(Map<String,Object> false_match : false_match_list) {
//	                    try {
//	                        log.info("false_match : " + false_match);
//	                        false_match.put("type", flag);
//	                        dao.setRemediation(false_match);
//	                    } catch (Exception e) {
//	                        e.printStackTrace();
//	                        resultCode = -1;
//	                        log.info("error : " + e.getMessage());
//	                    }
//	                }
//	                
//	                if(resultCode == 1) {
//	                    data_pro.put("type", flag);
//	                    data_pro.put("ID", hash_id);
//	                    data_pro.put("FLAG", 0);
//	                    dao.setRemediation(data_pro);
//	                }
//	                
//	            } else {
//	                // ========== 실패 처리 ==========
//	                result = "실패";
//	                
//	                String responseCode = resultMap.get("resultCode") != null ? resultMap.get("resultCode").toString() : "Unknown";
//	                String responseMessage = resultMap.get("HttpsResponseMessage") != null ? resultMap.get("HttpsResponseMessage").toString() : "";
//	                String responseData = resultMap.get("HttpsResponseDataMessage") != null ? resultMap.get("HttpsResponseDataMessage").toString() : "";
//	                
//	                if(flag.equals("delete")) {
//	                    errorMessage = "삭제 처리 중 오류가 발생했습니다. [" + responseCode + "] " + responseMessage;
//	                } else {
//	                    errorMessage = "암호화 처리 중 오류가 발생했습니다. [" + responseCode + "] " + responseMessage;
//	                }
//	                
//	                log.error("Remediation failed : " + errorMessage + " : " + responseData);
//	                
//	                // ========== 6. 상세 테이블 UPDATE (실패) ==========
//	                for(Map<String,Object> false_match : false_match_list) {
//	                    Map<String, Object> detailUpdateMap = new HashMap<>();
//	                    detailUpdateMap.put("hash_id", false_match.get("ID"));
//	                    detailUpdateMap.put("target_id", data_pro.get("TARGET_ID"));
//	                    detailUpdateMap.put("ap_no", data_pro.get("AP_NO"));
//	                    detailUpdateMap.put("status", "FAILED");
//	                    detailUpdateMap.put("error_message", errorMessage + " : " + responseData);
//	                    detailUpdateMap.put("process_date", processDate);
//	                    
//	                    dao.updateRemediationDetailStatus(detailUpdateMap);
//	                }
//	            }
//	        } else {
//	            result = "실패";
//	            errorMessage = "처리할 파일을 찾을 수 없습니다.";
//	        }
//	        
//	        // ========== 로그 남기기 ==========
//	        ServletUtil servletUtil = new ServletUtil(request);
//	        String clientIP = servletUtil.getIp();
//	        
//	        String res = "";
//	        if(flag.equals("delete")) {
//	            res = "삭제";
//	        } else {
//	            res = "암호화";
//	        }
//	        
//	        Map<String, Object> userLog = new HashMap<String, Object>();
//	        userLog.put("user_no", user_no);
//	        userLog.put("menu_name", "APPROVAL REGIST");
//	        userLog.put("user_ip", clientIP);
//	        
//	        String logMessage = "[" + data_pro.get("HOST_NAME") + "]" + data_pro.get("PATH") + " " + res + " 처리 " + result;
//	        if(!password.equals("")) {
//	            logMessage += " [Password:" + password + "]";
//	        }
//	        if(!errorMessage.isEmpty()) {
//	            logMessage += " - " + errorMessage;
//	        }
//	        
//	        userLog.put("job_info", logMessage);
//	        userLog.put("logFlag", "11");
//	        
//	        log.info("userLog : " + userLog);
//	        
//	        userDao.insertLog(userLog);
//	    }
//	}
//	
	// delete, encrypt 처리 (파일이동 방식과 동일하게 수정)
	@Override
	public void registRemdiation(HttpServletRequest request, HashMap<String, Object> params) throws Exception {
	    
	    List<Map<String,Object>> aFalseMatchList = (List<Map<String, Object>>)params.get("aList");
	    Map<String,Object> resultMap = new HashMap<String, Object>();
	    String flag = params.get("flag").toString();
	    String user_no = SessionUtil.getSession("memberSession", "USER_NO");
	    String password = params.get("password") == null ? "" : params.get("password").toString();
	    
	    // ========== 처리 결과 추적용 변수 ==========
	    int totalCount = aFalseMatchList.size();
	    int successCount = 0;
	    int failureCount = 0;
	    List<String> failedFiles = new ArrayList<>();
	    Map<String, String> fileErrorMap = new HashMap<>();
	    
	    for(Map<String,Object> falseMatch : aFalseMatchList) {
	        String result = "";
	        String errorMessage = "";
	        
	        String hash_id = falseMatch.get("hash_id").toString();
	        Map<String,Object> data_pro = dao.getFindByHash(falseMatch);
	        if (data_pro == null) {
	            data_pro = dao.getDBFindByHash(falseMatch);
	        }
	        
	        String filePath = data_pro.get("PATH") != null ? data_pro.get("PATH").toString() : "";
	        
	        // ========== 이미 처리된 파일인지 체크 ==========
	        if(data_pro.get("FLAG") != null && !data_pro.get("FLAG").toString().equals("0")) {
	            log.warn("⚠️ 이미 처리된 파일입니다. HASH_ID: " + hash_id + ", FLAG: " + data_pro.get("FLAG"));
	            
	            errorMessage = "이미 처리된 파일입니다.";
	            failureCount++;
	            failedFiles.add(filePath);
	            fileErrorMap.put(filePath, errorMessage);
	            
	            // 로그만 남기고 스킵
	            ServletUtil servletUtil = new ServletUtil(request);
	            String clientIP = servletUtil.getIp();
	            
	            Map<String, Object> userLog = new HashMap<String, Object>();
	            userLog.put("user_no", user_no);
	            userLog.put("menu_name", "APPROVAL REGIST");
	            userLog.put("user_ip", clientIP);
	            userLog.put("job_info", "[" + data_pro.get("HOST_NAME") + "]" + data_pro.get("PATH") + " " + (flag.equals("delete") ? "삭제" : "암호화") + " 처리 실패 - 이미 처리된 파일");
	            userLog.put("logFlag", "11");
	            userDao.insertLog(userLog);
	            
	            continue;  // 다음 파일로
	        }
	        
	        // 하위 파일 포함 모든 파일 조회
	        data_pro.put("allow_reprocess", "Y");
	        List<Map<String,Object>> false_match_list = dao.selectFalseMatchApi(data_pro);
	        
	        // ========== 디버깅 로그 ==========
	        log.info("========== Remediation Debug ==========");
	        log.info("HASH_ID: " + data_pro.get("HASH_ID"));
	        log.info("FLAG: " + data_pro.get("FLAG"));
	        log.info("PATH: " + data_pro.get("PATH"));
	        log.info("DATA_TYPES: " + data_pro.get("DATA_TYPES"));
	        log.info("Action: " + flag);
	        log.info("Total rows from CTE: " + false_match_list.size());
	        if(false_match_list.size() == 0) {
	            log.error("❌ CTE returned NO rows!");
	        }
	        
	        // CTE 결과에서 DATA_TYPES 확인
	        if(false_match_list.size() > 0) {
	            log.info("First row DATA_TYPES: " + false_match_list.get(0).get("DATA_TYPES"));
	        }
	        
	        if(false_match_list.size() != 0) {
	            
	            // ========== 1. 상세 테이블 INSERT (최상위 파일만) ==========
	            for(Map<String,Object> false_match : false_match_list) {
	                // FLAG = 0 (최상위 파일)만 INSERT
	                if(false_match.get("FLAG") != null && false_match.get("FLAG").toString().equals("0")) {
	                    Map<String, Object> detailMap = new HashMap<>();
	                    detailMap.put("target_id", data_pro.get("TARGET_ID"));
	                    detailMap.put("hash_id", false_match.get("ID"));
	                    detailMap.put("ap_no", data_pro.get("AP_NO"));
	                    detailMap.put("fid", false_match.get("FID"));
	                    detailMap.put("original_path", false_match.get("PATH"));
	                    detailMap.put("new_path", null);  // 암호화/삭제는 경로 변경 없음
	                    detailMap.put("status", "PENDING");
	                    detailMap.put("user_no", user_no);
	                    detailMap.put("action", flag);  // delete 또는 encrypt
	                    
	                    // password 저장 (encrypt인 경우만)
	                    if(flag.equals("encrypt") && !password.equals("")) {
	                        detailMap.put("password", password);
	                    } else {
	                        detailMap.put("password", null);
	                    }
	                    
	                    // PI_REMEDIATION_DETAIL INSERT
	                    dao.insertRemediationDetail(detailMap);
	                }
	            }
	            
	            // ========== 2. API 호출 준비 ==========
	            Map<String, Object> apiMap = new HashMap<>();
	            JSONArray array = new JSONArray();
	            JSONObject exInfo = new JSONObject();
	            JSONArray idsArray = new JSONArray();
	            
	            exInfo.put("path", data_pro.get("PATH"));
	            exInfo.put("sign_off", user_no + "_PICenter");
	            if(!password.equals("")) {
	                exInfo.put("password", password);
	            }
	            
	            String uri = "/targets/" + data_pro.get("TARGET_ID") + "/locations/" + data_pro.get("LOCATION_ID") + "/remediation/" + flag;
	            
	            // ========== 모든 FID를 ARRAY에 추가 (FLAG 무관) ==========
	            String datatypeArray = "";
	            for(Map<String,Object> false_match : false_match_list) {
	                if(false_match.get("FID") != null) {
	                    idsArray.add(false_match.get("FID"));
	                }
	                
	                // data_types 수집 (원래 로직 복원)
	                if(false_match.get("DATA_TYPES") != null && datatypeArray.isEmpty()) {
	                    datatypeArray = false_match.get("DATA_TYPES").toString();
	                }
	            }
	            
	            exInfo.put("object_ids", idsArray);
	            
	            // data_types 추가 (원래 로직 복원)
	            if(!datatypeArray.isEmpty()) {
	                exInfo.put("data_types", datatypeArray);
	                log.info("data_types added: " + datatypeArray);
	            }
	            
	            apiMap.put("AP_NO", data_pro.get("AP_NO"));
	            array.add(exInfo);
	            apiMap.put("Data", array.toString());
	            
	            log.info("apiMap : " + apiMap);
	            
	            // ========== 3. Recon API 호출 ==========
	            resultMap = postDataApi(apiMap, uri);
	            
	            log.info("resultMap : " + resultMap);
	            
	            // 처리 시간 기록
	            java.util.Date processDate = new java.util.Date();
	            
	            if(resultMap.get("resultCode").toString().equals("202")) {
	                // ========== 성공 처리 (API 호출 성공 = PENDING 상태) ==========
	                result = "성공";
	                successCount++;
	                
	                // ========== 4. 상세 테이블 UPDATE (PENDING으로 저장) ==========
	                // 배치에서 remediationlogs를 확인하여 실제 처리 완료 여부를 체크
	                Map<String, Object> detailUpdateMap = new HashMap<>();
	                detailUpdateMap.put("hash_id", hash_id);
	                detailUpdateMap.put("target_id", data_pro.get("TARGET_ID"));
	                detailUpdateMap.put("ap_no", data_pro.get("AP_NO"));
	                detailUpdateMap.put("status", "PENDING");
	                detailUpdateMap.put("process_date", processDate);
	                
	                dao.updateRemediationDetailStatus(detailUpdateMap);
	                
	                // ========== 5. PI_FIND와 PI_SUBPATH REMEDIATION_STATUS 업데이트 ==========
	                try {
	                    Map<String, Object> remediationUpdate = new HashMap<>();
	                    remediationUpdate.put("ID", hash_id);
	                    remediationUpdate.put("TARGET_ID", data_pro.get("TARGET_ID"));
	                    remediationUpdate.put("AP_NO", data_pro.get("AP_NO"));
	                    remediationUpdate.put("type", flag);  // delete 또는 encrypt
	                    
	                    dao.updateFindRemediation(remediationUpdate);
	                    dao.updateSubpathRemediation(remediationUpdate);
	                    
	                    log.info("✅ REMEDIATION_STATUS updated: " + hash_id);
	                } catch (Exception e) {
	                    log.error("❌ Update failed: " + hash_id, e);
	                }
	                
	            } else {
	                // ========== 실패 처리 ==========
	                result = "실패";
	                failureCount++;
	                
	                String responseCode = resultMap.get("resultCode") != null ? resultMap.get("resultCode").toString() : "Unknown";
	                String responseMessage = resultMap.get("HttpsResponseMessage") != null ? resultMap.get("HttpsResponseMessage").toString() : "";
	                String responseData = resultMap.get("HttpsResponseDataMessage") != null ? resultMap.get("HttpsResponseDataMessage").toString() : "";
	                
	                String actionName = flag.equals("delete") ? "삭제" : "암호화";
	                
	                if(responseCode.equals("404")) {
	                    errorMessage = "파일을 찾을 수 없습니다.";
	                } else if(responseCode.equals("422")) {
	                    errorMessage = "지정된 작업을 수행할 수 없습니다.";
	                } else {
	                    errorMessage = actionName + " 처리 중 오류가 발생했습니다. [" + responseCode + "]";
	                }
	                
	                failedFiles.add(filePath);
	                fileErrorMap.put(filePath, errorMessage);
	                
	                log.error(actionName + " failed : " + errorMessage + " : " + responseData);
	                
	                // ========== 6. 상세 테이블 UPDATE (실패) ==========
	                Map<String, Object> detailUpdateMap = new HashMap<>();
	                detailUpdateMap.put("hash_id", hash_id);
	                detailUpdateMap.put("target_id", data_pro.get("TARGET_ID"));
	                detailUpdateMap.put("ap_no", data_pro.get("AP_NO"));
	                detailUpdateMap.put("status", "FAILED");
	                detailUpdateMap.put("error_message", errorMessage);
	                detailUpdateMap.put("process_date", processDate);
	                
	                dao.updateRemediationDetailStatus(detailUpdateMap);
	            }
	        }
	        
	        // 로그 남기기
	        ServletUtil servletUtil = new ServletUtil(request);
	        String clientIP = servletUtil.getIp();
	        
	        Map<String, Object> userLog = new HashMap<String, Object>();
	        userLog.put("user_no", user_no);
	        userLog.put("menu_name", "APPROVAL REGIST");
	        userLog.put("user_ip", clientIP);
	        
	        String actionName = flag.equals("delete") ? "삭제" : "암호화";
	        String logMessage = "[" + data_pro.get("HOST_NAME") + "]" + data_pro.get("PATH") + " " + actionName + " 처리 " + result;
	        if(!errorMessage.isEmpty()) {
	            logMessage += " - " + errorMessage;
	        }
	        
	        userLog.put("job_info", logMessage);
	        userLog.put("logFlag", "11");
	        
	        log.info("userLog : " + userLog);
	        
	        userDao.insertLog(userLog);
	    }
	    
	    // ========== 실패가 있으면 사용자 친화적인 예외 메시지 생성 ==========
	    if(failureCount > 0) {
	        StringBuilder message = new StringBuilder();
	        String actionName = flag.equals("delete") ? "삭제" : "암호화";
	        
	        if(successCount > 0) {
	            // 일부 성공, 일부 실패
	            message.append(successCount).append("건 성공, ")
	                   .append(failureCount).append("건 실패\n\n");
	            
	            // 실패한 파일 목록 (최대 5개)
	            message.append("[실패한 파일]\n");
	            int displayCount = Math.min(failedFiles.size(), 5);
	            for(int i = 0; i < displayCount; i++) {
	                String file = failedFiles.get(i);
	                String error = fileErrorMap.get(file);
	                // 파일명이 너무 길면 뒷부분만 표시
	                String shortFileName = file.length() > 50 ? "..." + file.substring(file.length() - 47) : file;
	                message.append("- ").append(shortFileName).append("\n  (").append(error).append(")\n");
	            }
	            if(failedFiles.size() > 5) {
	                message.append("... 외 ").append(failedFiles.size() - 5).append("건");
	            }
	        } else {
	            // 전체 실패
	            message.append(actionName).append(" 처리 실패\n\n");
	            
	            // 실패한 파일 목록 (최대 5개)
	            message.append("[실패한 파일]\n");
	            int displayCount = Math.min(failedFiles.size(), 5);
	            for(int i = 0; i < displayCount; i++) {
	                String file = failedFiles.get(i);
	                String error = fileErrorMap.get(file);
	                String shortFileName = file.length() > 50 ? "..." + file.substring(file.length() - 47) : file;
	                message.append("- ").append(shortFileName).append("\n  (").append(error).append(")\n");
	            }
	            if(failedFiles.size() > 5) {
	                message.append("... 외 ").append(failedFiles.size() - 5).append("건");
	            }
	        }
	        
	        throw new Exception(message.toString());
	    }
	}
	
	private Map<String, Object> postDataApi(Map<String, Object> map, String uri) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		
		RemediationUtil reconUtil = new RemediationUtil();
		Map<String, Object> httpsResponse = null;
		
		try {
			Properties properties = new Properties();
			String resource = "/property/config.properties";
			Reader reader = Resources.getResourceAsReader(resource);
			
			properties.load(reader);
			
			String api_ver = properties.getProperty("recon.api.version");
			String strAp = map.get("AP_NO").toString();
			
			int ap_no = Integer.parseInt(strAp);
			
			String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no+1)) ;
			String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no+1)) ;
			String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no+1)) ;
			
			httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + api_ver + uri, "POST", map.get("Data").toString());		
			
			int resultCode = Integer.parseInt(httpsResponse.get("HttpsResponseCode").toString());
			
			resultMap.put("resultCode", resultCode);
			
			if(resultCode == 400 || resultCode == 422) {
				resultMap.put("resultMessage", "예외 처리 실패");
			}
		} catch (Exception e) {
			log.error("Error: " + e.getMessage(), e);
			resultMap.put("resultCode", 400);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
		}

		return resultMap;
	}

	// 파일이동 전용 (개선됨)
	@Override
	public void registQuarantine(HttpServletRequest request, HashMap<String, Object> params) throws Exception {

	    List<Map<String,Object>> aQuarantineList = (List<Map<String, Object>>)params.get("aList");
	    List<Map<String,Object>> aRegistList = (List<Map<String, Object>>)params.get("aRegistList");
	    Map<String,Object> resultMap = new HashMap<String, Object>();
	    String flag = "quarantine";
	    String user_no = SessionUtil.getSession("memberSession", "USER_NO");
	    String quarantinePath = params.get("quarantine") == null ? "" : params.get("quarantine").toString();

	    // 경로 검증
	    if(quarantinePath.trim().isEmpty()) {
	        throw new Exception("파일이동 경로가 지정되지 않았습니다.");
	    }

	    // ========== 결과 추적용 변수 ==========
	    int successCount = 0;
	    int failureCount = 0;
	    List<String> failedFiles = new ArrayList<>();  // ✅ 실패한 파일 목록
	    List<String> successFiles = new ArrayList<>();  // ✅ 성공한 파일 목록
	    Map<String, String> fileErrorMap = new HashMap<>();  // ✅ 파일별 에러 메시지

	    // ========== 파일별 처리 ==========
	    for(Map<String,Object> quarantineItem : aQuarantineList) {
	        String result = "";
	        String errorMessage = "";
	        
	        String hash_id = quarantineItem.get("hash_id").toString();
	        Map<String,Object> data_pro = dao.getFindByHash(quarantineItem);
	        if (data_pro == null) {
	            data_pro = dao.getDBFindByHash(quarantineItem);
	        }
	        
	        String fileName = data_pro.get("PATH").toString();  // ✅ 파일명 저장
	        
	        data_pro.put("allow_reprocess", "Y");
	        List<Map<String,Object>> quarantine_list = dao.selectFalseMatchApi(data_pro);
	        
	        log.info("========== CTE Result Debug ==========");
	        log.info("HASH_ID: " + data_pro.get("HASH_ID"));
	        log.info("Total rows from CTE: " + quarantine_list.size());
	        if(quarantine_list.size() == 0) {
	            log.error("❌ CTE returned NO rows!");
	        } 

	        if(quarantine_list.size() != 0) {
	            
	            // ========== 1. 상세 테이블 INSERT ==========
	            for(Map<String,Object> quarantine_item : quarantine_list) {
	                if(quarantine_item.get("FLAG") != null && quarantine_item.get("FLAG").toString().equals("0")) {
	                    Map<String, Object> detailMap = new HashMap<>();
	                    detailMap.put("target_id", data_pro.get("TARGET_ID"));
	                    detailMap.put("hash_id", quarantine_item.get("ID"));
	                    detailMap.put("ap_no", data_pro.get("AP_NO"));
	                    detailMap.put("fid", quarantine_item.get("FID"));
	                    detailMap.put("original_path", quarantine_item.get("PATH"));
	                    detailMap.put("new_path", quarantinePath);
	                    detailMap.put("status", "PENDING");
	                    detailMap.put("user_no", user_no);
	                    detailMap.put("action", flag);
	                    
	                    dao.insertRemediationDetail(detailMap);
	                }
	            }
	            
	            // ========== 2. API 호출 ==========
	            Map<String, Object> apiMap = new HashMap<>();
	            JSONArray array = new JSONArray();
	            JSONObject exInfo = new JSONObject();
	            JSONArray idsArray = new JSONArray();
	            
	            exInfo.put("path", data_pro.get("PATH"));
	            exInfo.put("sign_off", user_no + "_PICenter");
	            exInfo.put("location", quarantinePath);

	            String uri = "/targets/" + data_pro.get("TARGET_ID") + "/locations/" + data_pro.get("LOCATION_ID") + "/remediation/" + flag;

	            for(Map<String,Object> quarantine_item : quarantine_list) {
	                if(quarantine_item.get("FID") != null) {
	                    idsArray.add(quarantine_item.get("FID"));
	                }
	            }

	            exInfo.put("object_ids", idsArray);

	            apiMap.put("AP_NO", data_pro.get("AP_NO"));
	            array.add(exInfo);
	            apiMap.put("Data", array.toString());

	            log.info("apiMap : " + apiMap);

	            resultMap = postDataApi(apiMap, uri);

	            log.info("resultMap : " + resultMap);
	            
	            java.util.Date processDate = new java.util.Date();

	            if(resultMap.get("resultCode").toString().equals("202")) {
	                // ========== 성공 처리 ==========
	                result = "성공";
	                successCount++;  // ✅ 성공 카운트
	                successFiles.add(fileName);  // ✅ 성공 파일 추가
	                
	                Map<String, Object> detailUpdateMap = new HashMap<>();
	                detailUpdateMap.put("hash_id", hash_id);
	                detailUpdateMap.put("target_id", data_pro.get("TARGET_ID"));
	                detailUpdateMap.put("ap_no", data_pro.get("AP_NO"));
	                detailUpdateMap.put("status", "PENDING");
	                
	                dao.updateRemediationDetailStatus(detailUpdateMap);
	                
	                // PI_FIND와 PI_SUBPATH 업데이트
	                try {
	                    Map<String, Object> remediationUpdate = new HashMap<>();
	                    remediationUpdate.put("ID", hash_id);
	                    remediationUpdate.put("TARGET_ID", data_pro.get("TARGET_ID"));
	                    remediationUpdate.put("AP_NO", data_pro.get("AP_NO"));
	                    remediationUpdate.put("type", flag);
	                    
	                    dao.updateFindRemediation(remediationUpdate);
	                    dao.updateSubpathRemediation(remediationUpdate);
	                    
	                    log.info("✅ REMEDIATION_STATUS updated: " + hash_id);
	                } catch (Exception e) {
	                    log.error("❌ Update failed: " + hash_id, e);
	                }
	                
	                // ========== 경로예외 등록 (기존 로직) ==========
	                if(aRegistList != null && !aRegistList.isEmpty()) {
	                    try {
	                        String target_id = data_pro.get("TARGET_ID").toString();
	                        String ap_no = data_pro.get("AP_NO").toString();
	                        int apNo = Integer.parseInt(ap_no);
	                        
	                        Map<String, Object> checkMap = new HashMap<>();
	                        checkMap.put("target_id", target_id);
	                        checkMap.put("ap_no", apNo);
	                        checkMap.put("path_ex", quarantinePath);
	                        checkMap.put("key", key);
	                        
	                        int existCount = exceptionDao.checkDuplicateFilter(checkMap);
	                        
	                        if(existCount > 0) {
	                            log.info("경로예외 중복 : 이미 등록된 경로입니다.");
	                        } else {
	                            JsonArray exArray = new JsonArray();
	                            JsonObject exceptionInfo = new JsonObject();
	                            
	                            exceptionInfo.addProperty("type", "exclude_expression");
	                            exceptionInfo.addProperty("expression", quarantinePath);
	                            exceptionInfo.addProperty("apply_to", target_id);
	                            
	                            exArray.add(exceptionInfo);
	                            
	                            Properties properties = new Properties();
	                            String resource = "/property/config.properties";
	                            Reader reader = Resources.getResourceAsReader(resource);
	                            properties.load(reader);
	                            
	                            String api_ver = properties.getProperty("recon.api.version");
	                            String recon_url = (apNo == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (apNo+1));
	                            String recon_id = (apNo == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (apNo+1));
	                            String recon_password = (apNo == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (apNo+1));
	                            
	                            RemediationUtil reconUtil = new RemediationUtil();
	                            Map<String, Object> httpsResponse = reconUtil.getServerData(
	                                recon_id, recon_password, recon_url + "/" + api_ver + "/filters", "POST", exArray.toString()
	                            );
	                            
	                            int exResultCode = Integer.parseInt(httpsResponse.get("HttpsResponseCode").toString());
	                            
	                            if (exResultCode == 201) {
	                                Gson gson = new Gson();
	                                JsonObject filters = gson.fromJson(httpsResponse.get("HttpsResponseDataMessage").toString(), JsonObject.class);
	                                String filter_id = filters.get("id").getAsString();
	                                
	                                Map<String, Object> filterMap = new HashMap<>();
	                                filterMap.put("filter_id", filter_id);
	                                filterMap.put("target_id", target_id);
	                                filterMap.put("ap_no", apNo);
	                                filterMap.put("type", "exclude_expression");
	                                filterMap.put("path_ex", quarantinePath);
	                                filterMap.put("create_user", user_no);
	                                filterMap.put("comment", "파일이동 후 자동 경로예외");
	                                filterMap.put("regExp", "N");
	                                filterMap.put("key", key); 
	                                
	                                exceptionDao.insertGlovalFilterDetail(filterMap);
	                                
	                                log.info("경로예외 등록 성공 : filter_id=" + filter_id);
	                            } else {
	                                log.error("경로예외 Recon API 호출 실패 : resultCode=" + exResultCode);
	                            }
	                        }
	                        
	                    } catch (Exception e) {
	                        log.error("경로예외 등록 실패 : " + e.getMessage());
	                        e.printStackTrace();
	                    }
	                }
	                
	            } else {
	                // ========== 실패 처리 ==========
	                result = "실패";
	                failureCount++;  // ✅ 실패 카운트
	                failedFiles.add(fileName);  // ✅ 실패 파일 추가
	                
	                String responseCode = resultMap.get("resultCode") != null ? resultMap.get("resultCode").toString() : "Unknown";
	                String responseData = resultMap.get("HttpsResponseDataMessage") != null ? resultMap.get("HttpsResponseDataMessage").toString() : "";
	                
	                if(responseCode.equals("404")) {
	                    errorMessage = "파일을 찾을 수 없습니다.";
	                } else if(responseCode.equals("422")) {
	                    errorMessage = "파일이동 처리 중 오류가 발생했습니다.";
	                } else {
	                    errorMessage = "파일이동 처리 중 오류가 발생했습니다. [" + responseCode + "]";
	                }
	                
	                fileErrorMap.put(fileName, errorMessage);  // ✅ 파일별 에러 저장
	                
	                log.error("Quarantine failed : " + errorMessage + " : " + responseData);
	                
	                Map<String, Object> detailUpdateMap = new HashMap<>();
	                detailUpdateMap.put("hash_id", hash_id);
	                detailUpdateMap.put("target_id", data_pro.get("TARGET_ID"));
	                detailUpdateMap.put("ap_no", data_pro.get("AP_NO"));
	                detailUpdateMap.put("status", "FAILED");
	                detailUpdateMap.put("error_message", errorMessage);
	                detailUpdateMap.put("process_date", processDate);
	                
	                dao.updateRemediationDetailStatus(detailUpdateMap);
	            }
	        }

	        // 로그 남기기
	        ServletUtil servletUtil = new ServletUtil(request);
	        String clientIP = servletUtil.getIp();

	        Map<String, Object> userLog = new HashMap<String, Object>();
	        userLog.put("user_no", user_no);
	        userLog.put("menu_name", "QUARANTINE REGIST");
	        userLog.put("user_ip", clientIP);
	        
	        String logMessage = "[" + data_pro.get("HOST_NAME") + "]" + data_pro.get("PATH") + " 파일이동 처리 " + result;
	        if(!errorMessage.isEmpty()) {
	            logMessage += " - " + errorMessage;
	        }
	        
	        userLog.put("job_info", logMessage);
	        userLog.put("logFlag", "12");

	        log.info("userLog : " + userLog);

	        userDao.insertLog(userLog);
	    }
	    
	    // ========== 결과에 따른 예외 메시지 생성 ==========
	    if(failureCount > 0) {
	        StringBuilder message = new StringBuilder();
	        
	        if(successCount > 0) {
	            // 일부 성공, 일부 실패
	            message.append(successCount).append("건 성공, ")
	                   .append(failureCount).append("건 실패\n\n");
	            
	            // 실패한 파일 목록 (최대 5개)
	            message.append("[실패한 파일]\n");
	            int displayCount = Math.min(failedFiles.size(), 5);
	            for(int i = 0; i < displayCount; i++) {
	                String file = failedFiles.get(i);
	                String error = fileErrorMap.get(file);
	                // 파일명이 너무 길면 뒷부분만 표시
	                String shortFileName = file.length() > 50 ? "..." + file.substring(file.length() - 47) : file;
	                message.append("- ").append(shortFileName).append("\n  (").append(error).append(")\n");
	            }
	            if(failedFiles.size() > 5) {
	                message.append("... 외 ").append(failedFiles.size() - 5).append("건");
	            }
	        } else {
	            // 전체 실패
	            message.append("파일이동 처리 실패\n\n");
	            
	            // 실패한 파일 목록 (최대 5개)
	            message.append("[실패한 파일]\n");
	            int displayCount = Math.min(failedFiles.size(), 5);
	            for(int i = 0; i < displayCount; i++) {
	                String file = failedFiles.get(i);
	                String error = fileErrorMap.get(file);
	                String shortFileName = file.length() > 50 ? "..." + file.substring(file.length() - 47) : file;
	                message.append("- ").append(shortFileName).append("\n  (").append(error).append(")\n");
	            }
	            if(failedFiles.size() > 5) {
	                message.append("... 외 ").append(failedFiles.size() - 5).append("건");
	            }
	        }
	        
	        throw new Exception(message.toString());
	    }
	}
}
