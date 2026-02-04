package com.org.iopts.report.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.org.iopts.exception.dao.piDetectionListDAO;
import com.org.iopts.mockup.dao.MockupDAO;
import com.org.iopts.report.dao.piSummaryDAO;
import com.org.iopts.report.service.piSummaryService;
import com.org.iopts.util.ReconUtil;
import com.org.iopts.util.SessionUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service("piSummaryService")
@Transactional
public class piSummaryServiceImple implements piSummaryService {
	
	private static Logger log = LoggerFactory.getLogger(piSummaryServiceImple.class);

	@Inject
	private piSummaryDAO summaryDao;
	
	@Inject
	private piDetectionListDAO detectionDao;

	@Inject
	private MockupDAO mockupDAO;
	 
	 @Value("${recon.api.version}")
    private String api_ver;
    
    @Value("${recon.id}")
	private String recon_id; 

	@Value("${recon.password}")
	private String recon_password;

	@Value("${recon.url}")
	private String recon_url;
	
	@Value("${excel.file.path}")
	private String excel_path;
	
	@Value("${excel.save.path}")
	private String excel_down;


	@Override
	public List<HashMap<String, Object>> searchSummaryList(HashMap<String, Object> params) throws Exception {
		int date = (Integer.parseInt(params.get("SCH_DATE").toString()));
		String ap = params.get("AP_NO").toString();
		log.info("ap : " + ap);
		
		// PICenter 에서 받아오는 개인정보 유형 갯수
		List<Integer> patternList = detectionDao.queryCustomDataTypesCnt2();
		params.put("patternList", patternList);

		// 동적 컬럼 (관리자, 운영자 등) 조회
		List<Map<String, Object>> serverColumns = mockupDAO.getServerColumns();
		params.put("serverColumns", serverColumns);

		List<Map<String, Integer>> datatypesList = new ArrayList<>();
		Map<String, Integer> datatypesMap = new HashMap<>();
		
		for(int i=0 ; i< patternList.size() ; i++) {
			datatypesMap = new HashMap<>();
			datatypesMap.put("type", patternList.get(i));
			datatypesMap.put("type_cnt", i);
			
			datatypesList.add(datatypesMap);
		}
		params.put("datatypesList", datatypesList);
		
		if(date == 0) {
			return summaryDao.searchSummaryList(params);
		}else {
			return summaryDao.searchSummaryRegDateList(params);
		}
		
		//변경
		/*String path = params.get("SCH_PATH").toString();
		path = path.replaceAll("\\\\", "\\\\\\\\");
		params.put("SCH_PATH", path);
		return summaryDao.searchSummaryList(params);*/
	}
	
	//	@Override
	public List<HashMap<String, Object>> searchDataProcessingFlag() throws Exception {
		return summaryDao.searchDataProcessingFlag();
	}

	@Override
	public List<HashMap<String, Object>> getMonthlyReport(HashMap<String, Object> params) throws Exception {
		
		log.info(params.get("year")+"");
		log.info(params.get("month")+"");
		
		String yyyymm = String.valueOf(params.get("year")) + String.valueOf(params.get("month"));
		return summaryDao.getMonthlyReport(yyyymm);
	}

	@Override
	public List<Map<String, Object>> selectPersonNotCom() throws Exception {
		return summaryDao.selectPersonNotCom();
	}
	
	@Override
	public List<Map<String, Object>> selectTeamNotCom() throws Exception {
		return summaryDao.selectTeamNotCom();
	}

	@Override
	public List<Map<String, Object>> selectOwnerList(Map<String, Object> map) throws Exception {
		return summaryDao.selectOwnerList(map);
	}
	
	@Override
	public Map<String, Object> getExcelDownCNT(HashMap<String, Object> params) throws Exception {
		
		int ap_no = 0;

		Map<String, Object> result = new HashMap<>();
		
		List<Map<String, Object>> resultList = new ArrayList<>();
		List<Map<String, Object>> chunksResultList = new ArrayList<>();
		List<Map<String, Object>> matchResultList = new ArrayList<>();
		List<Map<String, Object>> metasResultList = new ArrayList<>();
		
		Map<String, Object> resultMap =  new HashMap<String, Object>();
		Map<String, Object> chunksResultMap =  new HashMap<String, Object>();
		Map<String, Object> matchResultMap =  new HashMap<String, Object>();
		Map<String, Object> metasResultMap =  new HashMap<String, Object>();
		
		String tid = (String) params.get("tid");
		String gid = (String) params.get("gid");
		String reg_date = (String) params.get("sch_SDATE");
		String end_date = (String) params.get("sch_EDAT");
		
		String[] groupArr = gid.split(",");
		String[] targetArr = tid.split(",");
		HashMap<String, Object> groupMap =  new HashMap<String, Object>();
		HashMap<String, Object> targetMap =  new HashMap<String, Object>();
		
		List<String> targetList = new ArrayList<>();
		List<String> groupList = new ArrayList<>();
		List<Map<String, Object>> groupList2 = new ArrayList<>();
		Gson gson = new Gson();
		
		targetMap.put("sch_SDATE", reg_date);
		targetMap.put("sch_EDAT", end_date);
		
		if (gid != null && !gid.equals("")) {
		    JsonArray groupJArr = new Gson().toJsonTree(groupArr).getAsJsonArray();
		    for (int i = 0; i < groupJArr.size(); i++) {
		        String groupID = groupJArr.get(i).getAsString();
		        groupList.add(groupID);
		    }
		    groupMap.put("groupList", groupList);
		}
		if (tid != null && !tid.equals("")) {
		    JsonArray targetJArr = new Gson().toJsonTree(targetArr).getAsJsonArray();
		    for (int i = 0; i < targetJArr.size(); i++) {
		        String targetID = targetJArr.get(i).getAsString();
		        targetList.add(targetID);
		    }
		    groupMap.put("targetList", targetList);
		}
		
		if(groupMap.size() > 0) {
			groupList2 = summaryDao.getGroupID(groupMap);
			targetList = new ArrayList<>();
			if(groupList2.size() > 0) {
				for(int i=0; i<groupList2.size() ; i++) {
					if(groupList2.get(i) != null) {
						String targetId = (String)groupList2.get(i).get("TARGET_ID");
						targetList.add(targetId);
					}
				}
				targetMap.put("targetList", targetList);
			}else {
				result.put("fileName", "NoFile");
				result.put("rowLength", 0);
				return result;
			}
		}
	

		JsonObject jsonObject = null;
		List<Map<String, Object>> targetObject = null;
		List<Map<String, Object>> agent_mngr = null;
		Map<String, Object> targetList3 = null;
		Map<String, Object> daoMap = new HashMap<String, Object>();
		
		daoMap.put("reg_date", reg_date);
		daoMap.put("end_date", end_date);
		
		try {
			if(groupMap.size() > 0) {
				daoMap.put("targetList", targetList);
				targetObject = summaryDao.getTargetByNode(daoMap);
			}else {
				targetObject = summaryDao.getTargetByNode(daoMap);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(int i=0 ; i < targetObject.size() ; i++) {
			
			Map<String, Object> userMap = new HashMap<String, Object>();
			
			String target_id = (String) targetObject.get(i).get("TARGET_ID");
			String hash_id = (String) targetObject.get(i).get("HASH_ID");
			String fid = (String) targetObject.get(i).get("INFO_ID");
			String host_name = (String) targetObject.get(i).get("NAME");
			/*String group_name = (String) targetObject.get(i).get("GROUP_NAME");*/
			String agent_ip = (String) targetObject.get(i).get("AGENT_CONNECTED_IP");
			String agent_connected = (String) targetObject.get(i).get("AGENT_CONNECTED");
			String modified_date = (String) targetObject.get(i).get("MODIFIED_DATE");
			String owner = (String) targetObject.get(i).get("ACCOUNT");
			String chk = (String) targetObject.get(i).get("CHK");
			String path = (String) targetObject.get(i).get("PATH");
			String apNo = targetObject.get(i).get("AP_NO").toString();
			
			userMap.put("target_id", target_id);
			userMap.put("ap_no", apNo);
			
			String service_mngr = "";
			String service_mngr2 = "";
			
			agent_mngr = summaryDao.getMngrUser(userMap);
			
			if(agent_mngr.size() > 0) {
				log.info("agent_mngr >>> " + agent_mngr.toString());
				service_mngr = (String) agent_mngr.get(0).get("SERVICE_MNGR");
				service_mngr2 = (String) agent_mngr.get(0).get("SERVICE_MNGR2");
			}
			
			ReconUtil reconUtil = new ReconUtil();
			Map<String, Object> httpsResponse = null;
			JsonParser parser = new JsonParser();
			
			Properties properties = new Properties();
			String resource = "/property/config.properties";
			Reader reader = Resources.getResourceAsReader(resource);
			
			boolean infoIDNull = false;
			
			if(chk.equals(">"))  infoIDNull = true; // 하위경로 존재 O
			else infoIDNull = false; // 하위경로 존재 X

			resultMap =  new HashMap<String, Object>();
			resultMap.put("fid", fid);
			resultMap.put("hash_id", hash_id);
			resultMap.put("target_id", target_id);
			resultMap.put("host_name", host_name);
			resultMap.put("agent_ip", agent_ip);
			resultMap.put("agent_connected", agent_connected);
			resultMap.put("CHK", chk);
			resultMap.put("owner", owner);
			resultMap.put("path", path);
			resultMap.put("service_mngr", service_mngr);
			resultMap.put("service_mngr2", service_mngr2);
			
			if(infoIDNull) { // 하위 경로 존재 O
				log.info("IT CONTAINS SUB PATH.(FID ID IS NULL)");
				
				resultMap.put("modified_dt", modified_date);
				
				Map<String, Object> infoID = summaryDao.getInfoId(resultMap);
				
				if(infoID.get("FID") != null) {
					resultMap.put("fid", infoID.get("FID"));
					fid = infoID.get("FID").toString();
					
					resultMap.put("subpath_hash_id", infoID.get("HASH_ID"));
					
					targetList3= summaryDao.getsubpathTotal(resultMap);
					
					int type1 = Integer.parseInt(targetList3.get("TYPE1").toString());
					int type2 = Integer.parseInt(targetList3.get("TYPE2").toString());
					int type3 = Integer.parseInt(targetList3.get("TYPE3").toString());
					int type4 = Integer.parseInt(targetList3.get("TYPE4").toString());
					int type5 = Integer.parseInt(targetList3.get("TYPE5").toString());
					int type6 = Integer.parseInt(targetList3.get("TYPE6").toString());
					int type7 = Integer.parseInt(targetList3.get("TYPE7").toString());
					int type8 = Integer.parseInt(targetList3.get("TYPE8").toString());
					int total = type1 + type2 + type3 + type4 + type5 + type6 + type7 + type8;
					
					resultMap.put("TYPE1", type1);
					resultMap.put("TYPE2", type2);
					resultMap.put("TYPE3", type3);
					resultMap.put("TYPE4", type4);
					resultMap.put("TYPE5", type5);
					resultMap.put("TYPE6", type6);
					resultMap.put("TYPE7", type7);
					resultMap.put("TYPE8", type8);
					resultMap.put("TYPE",total);
				}
			}
			
			properties.load(reader);
			
			String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no+1)) ;
			String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no+1)) ;
			String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no+1)) ;
		
			try {
				httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + api_ver + "/targets/" + target_id + "/matchobjects/" + fid + "?details=true", "GET", null);
				
				if((Integer) httpsResponse.get("HttpsResponseCode") == 200) {
					
					jsonObject = gson.fromJson(httpsResponse.get("HttpsResponseData").toString(), JsonObject.class);

					JsonArray matches = jsonObject.getAsJsonArray("matches");
					JsonArray chunks = jsonObject.getAsJsonArray("chunks");
					JsonArray metas = jsonObject.getAsJsonArray("metas");
					
					if(!infoIDNull) {
						log.info("IT DOES NOT INCLUDE SUB PATH.(FID ID IS NOT NULL)");
						resultMap.put("path", jsonObject.get("path"));
					}
					matchResultList = new ArrayList<>();
					chunksResultList = new ArrayList<>();
					metasResultList = new ArrayList<>();	
					
					for (int j = 0; j < matches.size(); j++) {
					    matchResultMap = new HashMap<String, Object>();

					    JsonObject M_resultObject = matches.get(j).getAsJsonObject();

					    matchResultMap.put("match_CON", M_resultObject.get("content").getAsString());
					    matchResultMap.put("match_offset", M_resultObject.get("offset").getAsString());
					    matchResultMap.put("match_length", M_resultObject.get("length").getAsString());
					    matchResultMap.put("match_Type", M_resultObject.get("data_type").getAsString());

					    matchResultList.add(matchResultMap);
					}

					for (int j = 0; j < chunks.size(); j++) {
					    chunksResultMap = new HashMap<String, Object>();

					    JsonObject C_resultObject = chunks.get(j).getAsJsonObject();

					    chunksResultMap.put("chunks_CON", C_resultObject.get("content").getAsString());
					    chunksResultMap.put("chunks_length", C_resultObject.get("length").getAsString());
					    chunksResultMap.put("chunks_offset", C_resultObject.get("offset").getAsString());

					    chunksResultList.add(chunksResultMap);
					}

					for (int j = 0; j < metas.size(); j++) {
					    metasResultMap = new HashMap<String, Object>();

					    JsonObject M_resultObject = metas.get(j).getAsJsonObject();

					    if (M_resultObject.get("label").getAsString().equals("File Owner")) {
					        resultMap.put("owner", M_resultObject.get("value").getAsString());
					    } else if (M_resultObject.get("label").getAsString().equals("File Modified")) {
					        String modifiedDT = M_resultObject.get("value").getAsString();
							int Month = 0;
							if(modifiedDT != null && !modifiedDT.equals("")) {
				    			
				    			if(modifiedDT.toUpperCase().contains("JAN")) {
				    				Month = 1;
				    			}else if(modifiedDT.toUpperCase().contains("FEB")) {
				    				Month = 2;
				    			}else if(modifiedDT.toUpperCase().contains("MAR")) {
				    				Month = 3;
				    			}else if(modifiedDT.toUpperCase().contains("APR")) {
				    				Month = 4;
				    			}else if(modifiedDT.toUpperCase().contains("MAY")) {
				    				Month = 5;
				    			}else if(modifiedDT.toUpperCase().contains("JUN")) {
				    				Month = 6;
				    			}else if(modifiedDT.toUpperCase().contains("JUL")) {
				    				Month = 7;
				    			}else if(modifiedDT.toUpperCase().contains("AUG")) {
				    				Month = 8;
				    			}else if(modifiedDT.toUpperCase().contains("SEP")) {
				    				Month = 9;
				    			}else if(modifiedDT.toUpperCase().contains("OCT")) {
				    				Month = 10;
				    			}else if(modifiedDT.toUpperCase().contains("NOV")) {
				    				Month = 11;
				    			}else {
				    				Month = 12;
				    			}
				    			String day =  modifiedDT.substring(5, 7);
				    			String year =  modifiedDT.substring(8, 12);
				    			String time =  modifiedDT.substring(13);
				    			
				    			modifiedDT = year + "-" +Month + "-" + day + " " + time;
				    			
				    		}else {
				    			modifiedDT = "0000-00-00 00:00";
				    		}
							
							resultMap.put("Modified_Date", modifiedDT);
							
					    } else if (M_resultObject.get("label").getAsString().equals("File Created")) {
					        String created_DT = M_resultObject.get("value").getAsString();
					        int Month = 0;
					        if (created_DT != null && !created_DT.equals("")) {
					            if (created_DT.toUpperCase().contains("JAN")) {
					                Month = 1;
					            } else if (created_DT.toUpperCase().contains("FEB")) {
					                Month = 2;
					            } else if (created_DT.toUpperCase().contains("MAR")) {
					                Month = 3;
					            } else if (created_DT.toUpperCase().contains("APR")) {
					                Month = 4;
					            } else if (created_DT.toUpperCase().contains("MAY")) {
					                Month = 5;
					            } else if (created_DT.toUpperCase().contains("JUN")) {
					                Month = 6;
					            } else if (created_DT.toUpperCase().contains("JUL")) {
					                Month = 7;
					            } else if (created_DT.toUpperCase().contains("AUG")) {
					                Month = 8;
					            } else if (created_DT.toUpperCase().contains("SEP")) {
					                Month = 9;
					            } else if (created_DT.toUpperCase().contains("OCT")) {
					                Month = 10;
					            } else if (created_DT.toUpperCase().contains("NOV")) {
					                Month = 11;
					            } else {
					                Month = 12;
					            }
					            String day = created_DT.substring(5, 7);
					            String year = created_DT.substring(8, 12);
					            String time = created_DT.substring(13);

					            created_DT = year + "-" + Month + "-" + day + " " + time;
					        } else {
					            created_DT = "0000-00-00 00:00";
					        }

					        resultMap.put("Created_Date", created_DT);
					    } else {
					        metasResultMap.put("metas_type", M_resultObject.get("label").getAsString());
					        metasResultMap.put("metas_val", M_resultObject.get("value").getAsString());
					        metasResultList.add(metasResultMap);
					    }
					}
					
					resultMap.put("chunksResultList", chunksResultList);
					resultMap.put("matchResultList", matchResultList);
					resultMap.put("metasResultList", metasResultList);
					resultList.add(resultMap);
					
				}else if((Integer) httpsResponse.get("HttpsResponseCode") == 404) {
					log.info("Recon path Delete :: Response Code 404");
				}
				
			} catch (Exception e) {
				log.error("Detail api Export Error :::  " + e);
				e.printStackTrace();
			}
		}
		
		JsonArray jsonArray = new Gson().toJsonTree(resultList).getAsJsonArray();

		OutputStream out = null;
		String fileName = "";
		
		// 현재날짜
		Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyMMddHHmm");

		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		
		String fileDate = sf.format(date);
		
		fileName = excel_path + user_no +"_detailReport_" + fileDate + ".txt"; 
		
		try {
			File file = new File(fileName);
			
			 // 2. 파일 존재여부 체크 및 생성
            if (!file.exists()) {
                file.createNewFile();
            }
			 out = new FileOutputStream(fileName);
			 //byte[] bytes = resultList.toString().getBytes();
			 // 3. Buffer를 사용해서 File에 write할 수 있는 BufferedWriter 생성
            FileWriter fw = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fw);
 
            // 4. 파일에 쓰기
            writer.write(jsonArray.toString());
 
            // 5. BufferedWriter close
            writer.close();
			 
		} catch (IOException e) {
			// TODO: handle exception
			log.info("File writer Error :: ");
			e.printStackTrace();
		} catch (Exception e) {
			log.info("Detail api Export Error :: " + e);
			e.printStackTrace();
		}
		result.put("fileName", fileName);
		result.put("rowLength", jsonArray.size());
		
		return result;
	}
	
	@Override
	public Map<String, Object> reportDetailBatch(HttpServletRequest request, HashMap<String, Object> params) throws Exception {
		Map<String, Object> result = new HashMap<>();
        String scriptPath = excel_down ; // 셸 스크립트 경로

        JsonObject jsonObject = new JsonObject();
        
        String flag = params.get("flag").toString();
        String report_flag = "xlsx"; 
        String os = System.getProperty("os.name").toLowerCase();
        
        try {
        	// 문자열 필드
        	jsonObject.addProperty("target_id", params.get("target_id") != null ? params.get("target_id").toString() : null);
        	jsonObject.addProperty("ap_no", params.get("ap_no") != null ? params.get("ap_no").toString() : null);
        	jsonObject.addProperty("group_id", params.get("group_id") != null ? params.get("group_id").toString() : null);
        	jsonObject.addProperty("sch_status", params.get("sch_status") != null ? params.get("sch_status").toString() : null);
        	jsonObject.addProperty("sch_processing_flag", params.get("sch_processing_flag") != null ? params.get("sch_processing_flag").toString() : null);
        	jsonObject.addProperty("sch_SDATE", params.get("sch_SDATE") != null ? params.get("sch_SDATE").toString() : null);
        	jsonObject.addProperty("sch_EDAT", params.get("sch_EDAT") != null ? params.get("sch_EDAT").toString() : null);
        	jsonObject.addProperty("filename", params.get("filename") != null ? params.get("filename").toString() : null);
        	jsonObject.addProperty("real_file_name", params.get("real_file_name") != null ? params.get("real_file_name").toString() : null);
        	jsonObject.addProperty("user_no", SessionUtil.getSession("memberSession", "USER_NO") != null ? SessionUtil.getSession("memberSession", "USER_NO").toString() : null);

        	// 배열 필드 (group_id_list, col_list, type_list)
        	String[] arrayFields = {"group_id_list", "col_list", "type_list"};
        	for (String field : arrayFields) {
        	    if (params.get(field) != null) {
        	        try {
        	            JsonArray array = JsonParser.parseString(params.get(field).toString()).getAsJsonArray();
        	            jsonObject.add(field, array);
        	        } catch (Exception e) {
        	            jsonObject.add(field, null);
        	        }
        	    } else {
        	        jsonObject.add(field, null);
        	    }
        	}               
		} catch (Exception e) {
			log.error("error :: " + e);
			e.printStackTrace();
		}
        
        log.info("jsonObject ::: " + jsonObject);
        
        try {
        	ProcessBuilder processBuilder = new ProcessBuilder();
        	
        	if(os.contains("win")) {
        		processBuilder = new ProcessBuilder(scriptPath,("\""+ jsonObject.toString().replace("\"", "\\\"") + "\""), flag, report_flag);
        	}else {
        		processBuilder = new ProcessBuilder(scriptPath, jsonObject.toString(), flag);
        	}
            Process process = processBuilder.start();

            ExecutorService executorService = Executors.newFixedThreadPool(2);
            
            StringBuilder output = new StringBuilder(); 
            StringBuilder errorOutput = new StringBuilder();
            
            // 표준 출력 처리
            executorService.submit(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                    	output.append(line).append("\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // 에러 출력 처리
            executorService.submit(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                    	errorOutput.append(line).append("\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // 프로세스에 입력이 필요할 경우
            try (OutputStream outputStream = process.getOutputStream()) {
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 프로세스 종료 대기
            int exitCode = process.waitFor();
            result.put("exitCode", exitCode); // 응답 코드 :: 0 이 아니면 전부 오류
            result.put("output", output.toString()); // batch log
//            result.put("errorOutput", errorOutput.toString()); // error 로그

            // ExecutorService 종료
            executorService.shutdown();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            log.error("error :: " + e.getLocalizedMessage());
        }

        return result;
	}
	
	
	// 상세 보고서
		@Override
		public Map<String, Object> reportDetailData(HttpServletRequest request, HashMap<String, Object> params) throws Exception {
			String tid = (String) params.get("TARGET_ID");
			int ap_no = Integer.parseInt(params.get("AP_NO").toString());
			//String status = (String) params.get("SCH_DATE");
			String reg_date = (String) params.get("sch_SDATE");
			String end_date = (String) params.get("sch_EDAT");
			String HostName = "";
			
			Map<String, Object> resultMap = new HashMap<>();
			Map<String, Object> result = new HashMap<>();
			
			Map<String, Object> summaryMap = new HashMap<>();
			
			List<Map<String, Object>> resultList = new ArrayList<>();
			List<Map<String, Object>> excelList = new ArrayList<>();
			List<Map<String, Object>> infoResultList = new ArrayList<>();
			List<Map<String, Object>> metasResultList = new ArrayList<>();
			List<Map<String, Object>> dataResultList = new ArrayList<>();
			
			Map<String, Object> infoResultMap =  new HashMap<String, Object>();
			Map<String, Object> metasResultMap =  new HashMap<String, Object>();
			Map<String, Object> dataResultMap =  new HashMap<String, Object>();
			
			ReconUtil reconUtil = new ReconUtil();
			Map<String, Object> httpsResponse = null;
			Map<String, Object> httpsResponse_summary = null;
			
			JsonParser parser = new JsonParser();
			
			StringBuffer sb = new StringBuffer();
			Gson gson = new Gson();
			int timeStamp = 0;
			JsonArray jsonArry = null;
			
			List<Map<String, Object>> dataTypeResultList = new ArrayList<>();
			Map<String, Object> dataTypeResultMap =  new HashMap<String, Object>();
			
			JsonObject jsonObject = null;
			Map<String, Object> targetMap = null;
			
			
			summaryMap = new HashMap<>();
			summaryMap.put("TARGET_ID", tid);
			summaryMap.put("AP_NO", ap_no);
			summaryMap.put("sch_SDATE", reg_date);
			summaryMap.put("sch_EDAT", end_date);
			
			try {
				// 기간내 검색 기록이 있는지
				List<Map<String, Object>> searchData = summaryDao.getTargetByNode(summaryMap);
				
				for (Map<String, Object> map : searchData) {
					resultMap = new HashMap<>();
					String target_id = (String) map.get("TARGET_ID");
					String hash_id = (String) map.get("HASH_ID");
					String fid = (String) map.get("INFO_ID");
					ap_no = Integer.parseInt(map.get("AP_NO").toString());
					String host_name = (String) map.get("NAME");
					String agent_ip = (String) map.get("AGENT_CONNECTED_IP");
					String agent_connected = (String) map.get("AGENT_CONNECTED");
					String Modified_Date = (String) map.get("MODIFIED_DATE");
					String Created_Date = (String) map.get("CREDATE_DATE");
					String owner = (String) map.get("ACCOUNT");
					String chk = (String) map.get("CHK");
					String path = (String) map.get("PATH");
					
					resultMap.put("target_id", target_id);
					resultMap.put("ap_no", ap_no);
					resultMap.put("hash_id", hash_id);
					resultMap.put("Modified_Date", Modified_Date);
					resultMap.put("Created_Date", Created_Date);
					resultMap.put("host_name", host_name);
					resultMap.put("agent_ip", agent_ip);
					resultMap.put("agent_connected", agent_connected);
					resultMap.put("CHK", chk);
					resultMap.put("path", path);
					
					Map<String, Object> infoID = summaryDao.getInfoId(resultMap);
					
					resultMap.put("fid", infoID.get("FID"));
					if(infoID.get("FID") != null) {
						
						infoResultList = new ArrayList<>();
						metasResultList = new ArrayList<>();
						dataResultList = new ArrayList<>();
						
						JsonArray personal_data = (JsonArray) parser.parse((String) infoID.get("PERSONAL_DATA"));
						JsonArray personal_info = (JsonArray) parser.parse((String) infoID.get("PERSONAL_INFO"));
						JsonArray personal_metas = (JsonArray) parser.parse((String) infoID.get("PERSONAL_METAS"));
						
						for (JsonElement dArr : personal_data) {
							JsonObject dObject = dArr.getAsJsonObject();
							
							dataResultMap =  new HashMap<String, Object>();
							dataResultMap.put("IDX", dObject.get("IDX").getAsString());
							dataResultMap.put("DATA", dObject.get("DATA").getAsString());
							
							dataResultList.add(dataResultMap);
						}
						for (JsonElement iArr : personal_info) {
							JsonObject iObject = iArr.getAsJsonObject();
							
							infoResultMap =  new HashMap<String, Object>();
							infoResultMap.put("IDX", iObject.get("IDX").getAsString());
							infoResultMap.put("TYPE", iObject.get("TYPE").getAsString());
							infoResultMap.put("DATA", iObject.get("DATA").getAsString());
							infoResultMap.put("DATA_TYPE", iObject.get("DATA_TYPE").getAsString());
							
							infoResultList.add(infoResultMap);
						}
						for (JsonElement mArr : personal_metas) {
							JsonObject mObject = mArr.getAsJsonObject();
							
							 
							if(Boolean.parseBoolean(mObject.get("is_aggregate").getAsString())) {
								metasResultMap =  new HashMap<String, Object>();
								metasResultMap.put("LABEL", mObject.get("label").getAsString());
								metasResultMap.put("VALUE", mObject.get("value").getAsString());
								metasResultMap.put("TYPE", mObject.get("TYPE").getAsString());
								
								metasResultList.add(metasResultMap);
							}else if(mObject.get("label").getAsString().equals("File Owner")){
								resultMap.put("owner", mObject.get("value").getAsString());
							}else if(mObject.get("label").getAsString().equals("File Modified")){ 
								String modifiedDT = mObject.get("value").getAsString();
								int Month = 0;
								if(modifiedDT != null && !modifiedDT.equals("")) {
					    			
					    			if(modifiedDT.toUpperCase().contains("JAN")) {
					    				Month = 1;
					    			}else if(modifiedDT.toUpperCase().contains("FEB")) {
					    				Month = 2;
					    			}else if(modifiedDT.toUpperCase().contains("MAR")) {
					    				Month = 3;
					    			}else if(modifiedDT.toUpperCase().contains("APR")) {
					    				Month = 4;
					    			}else if(modifiedDT.toUpperCase().contains("MAY")) {
					    				Month = 5;
					    			}else if(modifiedDT.toUpperCase().contains("JUN")) {
					    				Month = 6;
					    			}else if(modifiedDT.toUpperCase().contains("JUL")) {
					    				Month = 7;
					    			}else if(modifiedDT.toUpperCase().contains("AUG")) {
					    				Month = 8;
					    			}else if(modifiedDT.toUpperCase().contains("SEP")) {
					    				Month = 9;
					    			}else if(modifiedDT.toUpperCase().contains("OCT")) {
					    				Month = 10;
					    			}else if(modifiedDT.toUpperCase().contains("NOV")) {
					    				Month = 11;
					    			}else {
					    				Month = 12;
					    			}
					    			String day =  modifiedDT.substring(5, 7);
					    			String year =  modifiedDT.substring(8, 12);
					    			String time =  modifiedDT.substring(13);
					    			
					    			modifiedDT = year + "-" + (Month > 9 ? Month : "0" + Month)  + "-" + day + " " + time;
					    			
					    		}else {
					    			modifiedDT = "0000-00-00 00:00";
					    		}
								
								resultMap.put("Modified_Date", modifiedDT);
							}else if(mObject.get("label").getAsString().equals("File Created")){ 
								String created_DT = mObject.get("value").getAsString();
								int Month = 0;
								if(created_DT != null && !created_DT.equals("")) {
					    			
					    			if(created_DT.toUpperCase().contains("JAN")) {
					    				Month = 1;
					    			}else if(created_DT.toUpperCase().contains("FEB")) {
					    				Month = 2;
					    			}else if(created_DT.toUpperCase().contains("MAR")) {
					    				Month = 3;
					    			}else if(created_DT.toUpperCase().contains("APR")) {
					    				Month = 4;
					    			}else if(created_DT.toUpperCase().contains("MAY")) {
					    				Month = 5;
					    			}else if(created_DT.toUpperCase().contains("JUN")) {
					    				Month = 6;
					    			}else if(created_DT.toUpperCase().contains("JUL")) {
					    				Month = 7;
					    			}else if(created_DT.toUpperCase().contains("AUG")) {
					    				Month = 8;
					    			}else if(created_DT.toUpperCase().contains("SEP")) {
					    				Month = 9;
					    			}else if(created_DT.toUpperCase().contains("OCT")) {
					    				Month = 10;
					    			}else if(created_DT.toUpperCase().contains("NOV")) {
					    				Month = 11;
					    			}else {
					    				Month = 12;
					    			}
					    			String day =  created_DT.substring(5, 7);
					    			String year =  created_DT.substring(8, 12);
					    			String time =  created_DT.substring(13);
					    			
					    			created_DT = year + "-" + (Month > 9 ? Month : "0"+Month)  + "-" + day + " " + time;
					    			
					    		}else {
					    			created_DT = "0000-00-00 00:00";
					    		}
								
								resultMap.put("Created_Date", created_DT);
							}
						}
						
						
						resultMap.put("dataResultList", dataResultList);
						resultMap.put("infoResultList", infoResultList);
						resultMap.put("metasResultList", metasResultList);
						
						resultList.add(resultMap);
					}
				}
				
				
			}catch (Exception e) {
				// TODO: handle exception
				e.getStackTrace();
				log.error("Detail Error :: " + e.toString());
			}
			
			result.put("resultData", resultList);
			result.put("resultHost", HostName);
			result.put("resultCode", "200");
			
			return result;
		}
		
		//상세 보고서
		@Override
		public List<Map<String, Object>> detectionReport(HttpServletRequest request, HashMap<String, Object> params) throws Exception {
			Map<String, Object> result = new HashMap<>();
			List<Map<String, Object>> result1 = new ArrayList<>();
			Map<String, Object> excelMap = new HashMap<>();
			List<Map<String, Object>> resultList = new ArrayList<>();
			try {
				Map<String, Object> detectionMap = new HashMap<>();
				
				String NameChk = null;
				
				String gridDetectionData = (String) params.get("gridDetectionData");
				JsonArray resultArray = new Gson().fromJson(gridDetectionData, JsonArray.class);

				log.info("resultArray ::: " + resultArray);
				JsonParser parser = new JsonParser();
				
				List<Map<String, Object>> infoResultList = new ArrayList<>();
				List<Map<String, Object>> locationResultList = new ArrayList<>();
				List<Map<String, Object>> dataResultList = new ArrayList<>();
		
				Map<String, Object> infoResultMap =  new HashMap<String, Object>();
				Map<String, Object> locationResultMap =  new HashMap<String, Object>();
				Map<String, Object> dataResultMap =  new HashMap<String, Object>();
				
				if (resultArray.size() != 0) {
					for (int i = 0; i < resultArray.size(); i++) {
						
						infoResultList = new ArrayList<>();
						locationResultList = new ArrayList<>();
						dataResultList = new ArrayList<>();
						
						JsonObject resultMap = resultArray.get(i).getAsJsonObject();
					    detectionMap = new HashMap<>();

					    String name = resultMap.get("HOST").getAsString();
					    String ap_no = resultMap.get("AP_NO").getAsString();
					    String target_id = resultMap.get("TID").getAsString();
					    String hash_id = resultMap.get("ID").getAsString();
					    String subFile = resultMap.get("SUBFILE").getAsString();
					    String path = resultMap.get("SHORTNAME").getAsString();
						
						detectionMap.put("target_id", target_id);
						detectionMap.put("hash_id", hash_id);
						detectionMap.put("ap_no", ap_no);
						
						List<Map<String, Object>> infoID = new ArrayList<>();
						try {
							infoID = summaryDao.getInfoId2(detectionMap);
							
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						
						for (Map<String, Object> map : infoID) {
							excelMap = new HashMap<>();
							if(map.get("FID") != null) {
								excelMap.put("HostName", name);
								excelMap.put("path", map.get("FULL_NAME"));
								
								try {
									JsonArray personal_data = (JsonArray) parser.parse((String) map.get("PERSONAL_DATA")); // 검출 데이터 만 저장 (row 기준)
									JsonArray personal_info = (JsonArray) parser.parse((String) map.get("PERSONAL_INFO")); // 주변 데이터
									JsonArray personal_location = (JsonArray) parser.parse((String) map.get("PERSONAL_LOCATION")); // 기준점

									for (JsonElement dArr : personal_data) {
										JsonObject dObject = dArr.getAsJsonObject();
										
										dataResultMap =  new HashMap<String, Object>();
										dataResultMap.put("IDX", dObject.get("IDX").getAsString());
										dataResultMap.put("DATA", dObject.get("DATA").getAsString());
										
										dataResultList.add(dataResultMap);
									}
									
									for (JsonElement iArr : personal_info) {
										JsonObject iObject = iArr.getAsJsonObject();
										
										infoResultMap =  new HashMap<String, Object>();
										infoResultMap.put("IDX", iObject.get("IDX").getAsString());
										infoResultMap.put("TYPE", iObject.get("TYPE").getAsString());
										infoResultMap.put("DATA", iObject.get("DATA").getAsString());
										infoResultMap.put("DATA_TYPE", iObject.get("DATA_TYPE").getAsString());
										
										infoResultList.add(infoResultMap);
									}
									
									for (JsonElement lArr : personal_location) {
										JsonObject lObject = lArr.getAsJsonObject();
										
										locationResultMap =  new HashMap<String, Object>();
										locationResultMap.put("IDX", lObject.get("IDX").getAsString());
										locationResultMap.put("LENGTH", lObject.get("LENGTH").getAsString());
										locationResultMap.put("Offset", lObject.get("Offset").getAsString());
										
										locationResultList.add(locationResultMap);
									}
									
									excelMap.put("locationResultList", locationResultList);
									excelMap.put("infoResultList", infoResultList);
									excelMap.put("dataResultList", dataResultList);
									
									resultList.add(excelMap);
								} catch (Exception e) {
									e.printStackTrace();
									log.error("map :: " + map);
								}
								
								
							}
						}
						
						if(NameChk != null &&!NameChk.equals(name)) { // 호스트 별로 다른 시트로 구분
							
							result = new HashMap<>();
							result.put("HostName", NameChk);
							result.put("resultList", resultList);
							result1.add(result);
							resultList = new ArrayList<>();
							
						}else if(resultArray.size() == (i+1)) {
							result = new HashMap<>();
							
							result1.add(result);
							result.put("HostName", name);
							result.put("resultList", resultList);
						}
						NameChk = name;
					}
				}
			} catch (Exception e) {
				e.printStackTrace(); 
			}
			
			return result1;
		}
		
		private String numberFormat(String dataSize) {
			String fomatSize = "";
			
			Double size = Double.parseDouble(dataSize);
			
			if(size > 0) {
				
				String[] s = {"bytes", "KB", "MB", "GB", "TB", "PB" };
				
				int idx = (int) Math.floor(Math.log(size) / Math.log(1024));
	            DecimalFormat df = new DecimalFormat("#,###.##");
	            double ret = ((size / Math.pow(1024, Math.floor(idx))));
	            fomatSize = df.format(ret) + " " + s[idx];
			}
			
			return fomatSize;
		}
		
		// 상세 보고서
			@Override
		public List<Map<String, Object>> reportTargetList(HttpServletRequest request, HashMap<String, Object> params) throws Exception {
			List<Map<String, Object>> result = new ArrayList<>();
			log.info(params.toString());
				
			try {
				result = summaryDao.getDetailReportServers(params);
			} catch (Exception e) {
				// TODO: handle exception
				log.info("Error : " + e.toString());
			}
				
			return result;
		}
}