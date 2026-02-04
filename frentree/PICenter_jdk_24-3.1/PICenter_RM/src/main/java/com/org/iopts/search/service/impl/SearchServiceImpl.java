package com.org.iopts.search.service.impl;

import java.io.Reader;
import java.net.ProtocolException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.ResultMapping;
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
import com.org.iopts.dao.Pi_TargetDAO;
import com.org.iopts.dao.Pi_UserDAO;
import com.org.iopts.exception.dao.piDetectionListDAO;
import com.org.iopts.search.dao.SearchDAO;
import com.org.iopts.search.service.SearchService;
import com.org.iopts.search.vo.DataTypeVo;
import com.org.iopts.service.Pi_ScanServiceImpl;
import com.org.iopts.setting.service.impl.Pi_SetServiceImpl;
import com.org.iopts.util.ReconUtil;
import com.org.iopts.util.ServletUtil;
import com.org.iopts.util.SessionUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
@Transactional
public class SearchServiceImpl implements SearchService{

	private static Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

	@Value("${recon.id}")
	private String recon_id;

	@Value("${recon.password}")
	private String recon_password;

	@Value("${recon.url}")
	private String recon_url;
	
	@Value("${recon.api.version}")
	private String api_ver;
	
	@Inject
	private SearchDAO searchDao;
	
	@Inject
	private Pi_UserDAO userDao;
	
	@Inject
	private Pi_TargetDAO targetDao;
	
	@Inject
	private piDetectionListDAO detectiondao;
	
	Pi_SetServiceImpl set_service = new Pi_SetServiceImpl();
	
	@Override
	public Map<String, Object> insertProfile(HttpServletRequest request) throws Exception {
	    logger.info("insertProfile Service!");
	    // 로그 기록
	    ServletUtil servletUtil = new ServletUtil(request);
	    String clientIP = servletUtil.getIp();
	    String user_no = SessionUtil.getSession("memberSession", "USER_NO");
	    String password = request.getParameter("password");

	    // User Log 남기기
	    Map<String, Object> userLog = new HashMap<>();
	    List<Map<String, Object>> userLogList = new ArrayList<>();
	    Map<String, Object> LogMap = new HashMap<>();
	    String userLogCon = "";

	    Map<String, Object> map = new HashMap<>();
	    SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
	    Date time = new Date();
	    String timer = format.format(time);

	    String datatype_name = request.getParameter("datatype_name");
	    String std_id = "";

	    String profileArr = request.getParameter("profileArr");
	    String cntArr = request.getParameter("cntArr");
	    String dupArr = request.getParameter("dupArr");
	    String chkArr = request.getParameter("chkArr");
	    logger.info("dupArr :: " + dupArr);

	    String ocr = request.getParameter("ocr");
	    int recent = 0;

	    try {
	        recent = Integer.parseInt(request.getParameter("recent"));
	    } catch (Exception e) {
	        e.printStackTrace();
	        logger.error("error :: " + e);
	    }

	    String capture = request.getParameter("capture");

	    String profile[] = profileArr.split(",");
	    String profileCnt[] = cntArr.split(",");
	    String profileDup[] = dupArr.split(",");
	    String profileChk[] = chkArr.split(",");

	    String extension = request.getParameter("extension");

	    int cnt = 0;
	    int dup = 0;
	    int chk = 0;
	    String expression = "";
	    String expression_con = "";

	    logger.info("profile >>> " + profileArr);

	    JsonObject jObject = new JsonObject();
	    JsonArray builtinsArr = new JsonArray();
	    jObject.addProperty("label", timer + "_" + user_no);
	    jObject.add("builtins", builtinsArr);
	    JsonArray datatypeArr = new JsonArray();

	    List<Map<String, Object>> patternMap = detectiondao.queryCustomDataRules();
	    List<Map<String, Object>> apTypeList = searchDao.apTypeList();

	    JsonArray patternArr = new JsonArray();
	    JsonObject patternObj = new JsonObject();

	    userLogCon = "";
	    String userLogData = "";
	    for (int i = 0; i < profile.length; i++) {
	        LogMap = new HashMap<>();
	        userLogCon += userLogData;
	        userLogData = "";
	        chk = Integer.parseInt(profileChk[i]);

	        patternObj = new JsonObject();
	        patternObj.addProperty("TYPE", profile[i]);
	        patternObj.addProperty("_CHK", chk);

	        JsonObject datatypeObject = new JsonObject();
	        dup = Integer.parseInt(profileDup[i]);

	        patternObj.addProperty("_DUP", dup);
	        datatypeObject.addProperty("disabled", false);

	        logger.info("profile[i] >>> " + profile[i]);
	        if (chk == 1) {
	            userLogData += "<br>   - 임계치 : " + cnt + " 초과";
	        }

	        if (dup > 0) {
	            userLogData += " <br>   - 중복 제거 : O <br>";
	            expression_con = " POSTPROCESS 'UNIQUE'";
	        } else {
	            userLogData += " <br>   - 중복 제거 : N <br>";
	            expression_con = "";
	        }

	        cnt = Integer.parseInt(profileCnt[i]);

	        if (cnt < 1) {
	            patternObj.addProperty("_CNT", cnt);
	            expression_con += "";
	        } else {
	            patternObj.addProperty("_CNT", cnt);
	            expression_con += " POSTPROCESS 'MINIMUM " + (cnt + 1) + "'";
	        }

	        for (Map<String, Object> pMap : patternMap) {
	            if (profile[i].equals(pMap.get("ID").toString())) {
	                userLogData = "<br>" + pMap.get("PATTERN_NAME") + "  " + userLogData;
	                patternObj.addProperty("KR_NAMR", pMap.get("PATTERN_NAME") != null ? pMap.get("PATTERN_NAME").toString() : "");
	                patternArr.add(patternObj);
	                datatypeObject.addProperty("label", pMap.get("PATTERN_CODE") != null ? pMap.get("PATTERN_CODE").toString() : "");

	                logger.info("expression_con ::: " + expression_con);
	                expression = pMap.get("PATTERN_RULE").toString().replaceAll("\\\\n", "\n").replace("%s", expression_con);
	            }
	        }

	        if (chk == 1) {
	            datatypeObject.addProperty("expression", expression);
	            datatypeArr.add(datatypeObject);
	        }
	    }

	    jObject.add("custom_expressions", datatypeArr);

	    if (ocr.equals("1")) {
	        jObject.addProperty("ocr", true);
	        userLogCon += "<br><br> 이미지 검사 : O";
	    } else {
	        jObject.addProperty("ocr", false);
	    }

	    if (recent > 0) {
	        userLogCon += "<br>증분검사 : " + recent;
	    }

	    jObject.addProperty("voice", false);
	    jObject.addProperty("ebcdic", false);
	    jObject.addProperty("suppress", true);
	    jObject.addProperty("capture", true);

	    ReconUtil reconUtil = new ReconUtil();
	    Map<String, Object> httpsResponse = null;
	    Map<String, Object> resultMap = new HashMap<>();

	    try {
	        Properties properties = new Properties();
	        String resource = "/property/config.properties";
	        Reader reader = Resources.getResourceAsReader(resource);

	        properties.load(reader);

	        int ap_count = Integer.parseInt((properties.getProperty("recon.count") == null) ? "0" : properties.getProperty("recon.count"));
	        int success_count = 0;

	        for (int i = 0; i < ap_count; i++) {
	            // 서버 Data Type Profiles 생성 시 증분검사 시기 정하기
	            if (ocr.equals("1")) {
	                int OCRChk = Integer.parseInt(apTypeList.get(i).get("SET_TYPE").toString());
	                logger.info("OCRChk ::::" + OCRChk);
	                if (OCRChk == 1) {
	                    jObject.addProperty("ocr", true);
	                } else {
	                    jObject.addProperty("ocr", false);
	                }
	            } else {
	                jObject.addProperty("ocr", false);
	            }
	            logger.info("jObject :: " + jObject);

	            if (recent > 0) {
	                JsonArray arr = new JsonArray();
	                JsonObject obj = new JsonObject();

	                obj.addProperty("type", "include_recent");
	                obj.addProperty("days", recent);
	                arr.add(obj);
	                jObject.add("filters", arr);
	            }

	            if (extension != null && !extension.equals("")) {
	                JsonArray arr = new JsonArray();
	                String[] extensions = extension.split(",");

	                if (extensions.length > 0) userLogCon += "<br> 확장자 ";
	                for (String ext : extensions) {
	                    JsonObject obj = new JsonObject();
	                    obj.addProperty("type", "exclude_expression");
	                    obj.addProperty("expression", "!*." + ext.trim());
	                    arr.add(obj);

	                    userLogCon += "<br>   - " + ext.trim();
	                }
	                jObject.add("filters", arr);
	            }

	            String data = jObject.toString();

	            this.recon_url = (i == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (i + 1));
	            this.recon_id = (i == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (i + 1));
	            this.recon_password = (i == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (i + 1));
	            this.api_ver = properties.getProperty("recon.api.version");
	            httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + this.api_ver + "/datatypes/profiles", "POST", data);

	            String HttpsResponseDataMessage = "";
	            try {
	                HttpsResponseDataMessage = httpsResponse.get("HttpsResponseDataMessage") != null ? httpsResponse.get("HttpsResponseDataMessage").toString() : "";
	            } catch (NullPointerException e) {
	                logger.error(e.toString());
	            }

	            logger.info("HttpsResponseDataMessage : " + HttpsResponseDataMessage);

	            int resultCode = Integer.parseInt(httpsResponse.get("HttpsResponseCode").toString());
	            logger.info("resultCode : " + resultCode);

	            if (resultCode == 201) {
	                JsonObject resultObject = new Gson().fromJson(HttpsResponseDataMessage, JsonObject.class);
	                logger.info("getMatchObjects jsonObject : " + resultObject);

	                if (i == 0) {
	                    std_id = resultObject.get("id").getAsString();
	                }
	                map.put("datatype_id", resultObject.get("id").getAsString());
	                map.put("datatype_label", datatype_name);
	                map.put("create_user", user_no);
	                map.put("ocr", ocr);
	                map.put("recent", recent);
	                map.put("capture", capture);
	                map.put("std_id", std_id);
	                map.put("ap_no", i);
	                map.put("extension", extension);
	                // 데이터 입력전 오름 차순 정렬

	                JsonArray patternArray = new JsonArray();
	                try {
	                    List<JsonObject> list = new ArrayList<>();
	                    for (int pa = 0; pa < patternArr.size(); pa++) {
	                        list.add(patternArr.get(pa).getAsJsonObject());
	                    }
	                    Collections.sort(list, new Comparator<JsonObject>() {
	                        public int compare(JsonObject a, JsonObject b) {
	                            String valA = a.get("TYPE").getAsString();
	                            String valB = b.get("TYPE").getAsString();

	                            return valA.compareTo(valB);
	                        }
	                    });
	                    patternArray = new Gson().toJsonTree(list).getAsJsonArray();
	                } catch (Exception e) {
	                    logger.error(e.toString());
	                }

	                map.put("datatype", patternArray.toString());

	                logger.info(map.toString());

	                searchDao.insertProfile(map);
	                if (i == 0) searchDao.insertProfileList(map);

	                LogMap.put("key", userLogCon);
	                userLogList.add(LogMap);

	                userLog.put("user_no", user_no);
	                userLog.put("menu_name", "DATATYPE REGIST");
	                userLog.put("user_ip", clientIP);
	                userLog.put("job_info", "개인정보 유형 생성(성공) - " + datatype_name);
	                userLog.put("logFlag", "2");
	                userLog.put("context", userLogList.toString());

	                userDao.insertLog(userLog);
	            } else {
	                userLog.put("user_no", user_no);
	                userLog.put("menu_name", "DATATYPE REGIST");
	                userLog.put("user_ip", clientIP);
	                userLog.put("job_info", "개인정보 유형 생성(실패) ap-" + (i + 1));
	                userLog.put("logFlag", "2");
	                userLog.put("context", userLogList.toString());

	                userDao.insertLog(userLog);
	            }
	            resultMap.put("resultCode", resultCode);
	            resultMap.put("resultMessage", "Failed");
	        }

	    } catch (ProtocolException e) {
	        resultMap.put("resultCode", -1);
	        logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return resultMap;
	}

	@Override
	public List<Map<String, Object>> getProfile(HttpServletRequest request) throws Exception {
	    logger.info("getProfile Service!");
	    String datatype_id = request.getParameter("datatype_id");
	    String name = request.getParameter("name");

	    Map<String, Object> map = new HashMap<>();
	    map.put("datatype_id", datatype_id);

	    if (!"".equals(name) && name != null) {
	        map.put("name", name);
	    }
	    List<Map<String, Object>> profileList = searchDao.getProfile(map);

	    List<Map<String, Object>> resultList = new ArrayList<>();
	    List<String> keyList = new ArrayList<>();

	    HashMap<String, Object> resultMap = new HashMap<>();
	    Map<String, Object> dataTypeMap = new HashMap<>();

	    for (Map<String, Object> pMap : profileList) {
	        resultMap = new HashMap<>();

	        resultMap.put("OCR", pMap.get("OCR"));
	        resultMap.put("STD_ID", pMap.get("STD_ID"));
	        resultMap.put("RECENT", pMap.get("RECENT"));
	        resultMap.put("CAPTURE", pMap.get("CAPTURE"));
	        resultMap.put("CREATE_USER", pMap.get("CREATE_USER"));
	        resultMap.put("DATATYPE_ID", pMap.get("DATATYPE_ID"));
	        resultMap.put("DATATYPE_LABEL", set_service.replaceParameter((String) pMap.get("DATATYPE_LABEL")));
	        resultMap.put("DATATYPE", pMap.get("DATATYPE"));
	        resultMap.put("EXTENSION", pMap.get("EXTENSION"));

	        JsonArray dataType = new Gson().fromJson(pMap.get("DATATYPE").toString(), JsonArray.class);

	        for (int j = 0; j < dataType.size(); j++) {
	            dataTypeMap = new HashMap<>();
	            keyList = new ArrayList<>();

	            JsonObject dataTypeObject = dataType.get(j).getAsJsonObject();
	            Iterator<String> i = dataTypeObject.keySet().iterator();

	            resultMap.put(dataTypeObject.get("TYPE").getAsString(), "1");
	            resultMap.put(dataTypeObject.get("TYPE").getAsString() + "_CHK", dataTypeObject.get("_CHK").getAsString());
	            resultMap.put(dataTypeObject.get("TYPE").getAsString() + "_CNT", dataTypeObject.get("_CNT").getAsString());
	            if (dataTypeObject.has("_DUP") && !dataTypeObject.get("_DUP").isJsonNull()) {
	                resultMap.put(dataTypeObject.get("TYPE").getAsString() + "_DUP", dataTypeObject.get("_DUP").getAsString());
	            }
	        }
	        resultList.add(resultMap);
	    }

	    return resultList;
	}

	@Override
	public Map<String, Object> updateProfile(HttpServletRequest request) throws Exception {
	    logger.info("updateProfile Service!");

	    // 로그 기록
	    ServletUtil servletUtil = new ServletUtil(request);
	    String clientIP = servletUtil.getIp();
	    String user_no = SessionUtil.getSession("memberSession", "USER_NO");

	    // User Log 남기기
	    Map<String, Object> userLog = new HashMap<>();
	    List<Map<String, Object>> userLogList = new ArrayList<>();
	    Map<String, Object> LogMap = new HashMap<>();
	    String userLogCon = "";

	    String datatype_id = request.getParameter("datatype_id");
	    String std_id = request.getParameter("std_id");

	    Map<String, Object> map = new HashMap<>();
	    SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
	    Date time = new Date();
	    String timer = format.format(time);

	    String datatype_name = request.getParameter("datatype_name");

	    String profileArr = request.getParameter("profileArr");
	    String cntArr = request.getParameter("cntArr");
	    String dupArr = request.getParameter("dupArr");
	    String chkArr = request.getParameter("chkArr");
	    int dbSize = searchDao.getDatatypesUserSize(std_id);
	    logger.info("dbSize :: " + dbSize);

	    logger.info("cntArr :: " + cntArr);

	    String ocr = request.getParameter("ocr");

	    int recent = 0;

	    try {
	        recent = Integer.parseInt(request.getParameter("recent"));
	    } catch (Exception e) {
	        e.printStackTrace();
	        logger.error("error :: " + e);
	    }

	    String capture = request.getParameter("capture");

	    String profile[] = profileArr.split(",");
	    String profileCnt[] = cntArr.split(",");
	    String profileDup[] = dupArr.split(",");
	    String profileChk[] = chkArr.split(",");

	    int cnt = 0;
	    int dup = 0;
	    int chk = 0;
	    String expression = "";
	    String expression_con = "";

	    String extension = request.getParameter("extension");

	    JsonObject jObject = new JsonObject();
	    JsonArray builtinsArr = new JsonArray();
	    jObject.addProperty("label", timer + "_" + user_no);
	    jObject.add("builtins", builtinsArr);
	    JsonArray datatypeArr = new JsonArray();

	    List<Map<String, Object>> patternMap = detectiondao.queryCustomDataRules();
	    List<Map<String, Object>> apTypeList = searchDao.apTypeList();

	    JsonArray patternArr = new JsonArray();
	    JsonObject patternObj = new JsonObject();

	    userLogCon = "";
	    String userLogData = "";
	    for (int i = 0; i < profile.length; i++) {
	        LogMap = new HashMap<>();
	        userLogCon += userLogData;
	        userLogData = "";

	        chk = Integer.parseInt(profileChk[i]);

	        patternObj = new JsonObject();
	        patternObj.addProperty("TYPE", profile[i]);
	        patternObj.addProperty("_CHK", chk);

	        JsonObject datatypeObject = new JsonObject();
	        cnt = Integer.parseInt(profileCnt[i]);
	        dup = Integer.parseInt(profileDup[i]);

	        patternObj.addProperty("_DUP", dup);
	        datatypeObject.addProperty("disabled", false);

	        logger.info("profile[i] >>> " + profile[i]);

	        if (chk == 1) {
	            userLogData += "<br>   - 임계치 : " + cnt + " 초과";
	        }

	        if (dup > 0) {
	            userLogData += " <br>   - 중복 제거 : O <br>";
	            expression_con = " POSTPROCESS 'UNIQUE'";
	        } else {
	            userLogData += " <br>   - 중복 제거 : N <br>";
	            expression_con = "";
	        }

	        cnt = Integer.parseInt(profileCnt[i]);

	        if (cnt < 1) {
	            patternObj.addProperty("_CNT", cnt);
	            expression_con += "";
	        } else {
	            patternObj.addProperty("_CNT", cnt);
	            expression_con += " POSTPROCESS 'MINIMUM " + (cnt + 1) + "'";
	        }

	        for (Map<String, Object> pMap : patternMap) {
	            if (profile[i].equals(pMap.get("ID").toString())) {
	                userLogData = "<br>" + pMap.get("PATTERN_NAME") + "  " + userLogData;
	                patternObj.addProperty("KR_NAMR", pMap.get("PATTERN_NAME") != null ? pMap.get("PATTERN_NAME").toString() : "");
	                patternArr.add(patternObj);
	                datatypeObject.addProperty("label", pMap.get("PATTERN_CODE") != null ? pMap.get("PATTERN_CODE").toString() : "");
	                expression = pMap.get("PATTERN_RULE").toString().replaceAll("\\\\n", "\n").replaceAll("%s", expression_con);
	            }
	        }

	        if (chk == 1) {
	            datatypeObject.addProperty("expression", expression);
	            datatypeArr.add(datatypeObject);
	        }
	    }

	    jObject.add("custom_expressions", datatypeArr);

	    if (ocr.equals("1")) {
	        jObject.addProperty("ocr", true);
	        userLogCon += "<br><br> 이미지 검사 : O";
	    } else {
	        jObject.addProperty("ocr", false);
	    }

	    if (recent > 0) {
	        userLogCon += "<br>증분검사 : " + recent;
	    }

	    jObject.addProperty("voice", false);
	    jObject.addProperty("ebcdic", false);
	    jObject.addProperty("suppress", true);
	    jObject.addProperty("capture", true);

	    ReconUtil reconUtil = new ReconUtil();
	    Map<String, Object> httpsResponse = null;
	    Map<String, Object> resultMap = new HashMap<>();

	    List<DataTypeVo> datatypeList = new ArrayList<>();
	    int resultCode = 0;

	    try {
	        Properties properties = new Properties();
	        String resource = "/property/config.properties";
	        Reader reader = Resources.getResourceAsReader(resource);

	        properties.load(reader);

	        int ap_count = Integer.parseInt(properties.getProperty("recon.count") != null ? properties.getProperty("recon.count") : "0");

	        for (int i = 0; i < ap_count; i++) {
	            if (ocr.equals("1")) {
	                int OCRChk = Integer.parseInt(apTypeList.get(i).get("SET_TYPE").toString());
	                logger.info("OCRChk ::::" + OCRChk);
	                if (OCRChk == 1) {
	                    jObject.addProperty("ocr", true);
	                } else {
	                    jObject.addProperty("ocr", false);
	                }
	            } else {
	                jObject.addProperty("ocr", false);
	            }

	            // 서버 Data Type Profiles 생성 시 증분검사 시기 정하기
	            if (recent > 0) {
	                JsonArray arr = new JsonArray();
	                JsonObject obj = new JsonObject();

	                obj.addProperty("type", "include_recent");
	                obj.addProperty("days", recent);
	                arr.add(obj);
	                jObject.add("filters", arr);
	            }

	            if (extension != null && !extension.equals("")) {
	                JsonArray arr = new JsonArray();
	                String[] extensions = extension.split(",");

	                if (extensions.length > 0) userLogCon += "<br> 확장자 ";
	                for (String ext : extensions) {
	                    JsonObject obj = new JsonObject();
	                    obj.addProperty("type", "exclude_expression");
	                    obj.addProperty("expression", "!*." + ext.trim());
	                    arr.add(obj);

	                    userLogCon += "<br>   - " + ext.trim();
	                }
	                jObject.add("filters", arr);
	            }

	            String data = jObject.toString();

	            this.recon_url = (i == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (i + 1));
	            this.recon_id = (i == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (i + 1));
	            this.recon_password = (i == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (i + 1));
	            this.api_ver = properties.getProperty("recon.api.version");
	            httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + this.api_ver + "/datatypes/profiles", "POST", data);

	            String HttpsResponseDataMessage = "";
	            try {
	                HttpsResponseDataMessage = httpsResponse.get("HttpsResponseDataMessage") != null ? httpsResponse.get("HttpsResponseDataMessage").toString() : "";
	            } catch (NullPointerException e) {
	                logger.error(e.toString());
	            }

	            logger.info("HttpsResponseDataMessage : " + HttpsResponseDataMessage);

	            resultCode = Integer.parseInt(httpsResponse.get("HttpsResponseCode").toString());
	            logger.info("resultCode : " + resultCode);

	            if (resultCode == 201) {
	                JsonObject resultObject = new Gson().fromJson(HttpsResponseDataMessage, JsonObject.class);
	                logger.info("getMatchObjects jsonObject : " + resultObject);

	                datatype_id = resultObject.get("id").getAsString();

	                datatypeList.add(new DataTypeVo(datatype_id, std_id, i, datatype_name, user_no, ocr, recent, capture, extension));
	            }
	        }

	        if (datatypeList.size() == ap_count) { // 개인정보 유형이 전체 추가 된 경우
	            logger.info("DataType Profile Complete DB Update Start!");

	            for (int i = 0; i < datatypeList.size(); i++) {
	                map.put("datatype_id", datatypeList.get(i).getDatatype_id());
	                map.put("std_id", datatypeList.get(i).getStd_id());
	                map.put("ap_no", datatypeList.get(i).getAp_no());
	                map.put("datatype_label", datatypeList.get(i).getDatatype_label());
	                map.put("create_user", datatypeList.get(i).getCreate_user());
	                map.put("ocr", datatypeList.get(i).getOcr());
	                map.put("recent", datatypeList.get(i).getRecent());
	                map.put("capture", datatypeList.get(i).getCapture());
	                map.put("extension", datatypeList.get(i).getExtension());
	                // 데이터 입력전 오름 차순 정렬
	                List<JsonObject> list = new ArrayList<>();
	                for (int pa = 0; pa < patternArr.size(); pa++) {
	                    list.add(patternArr.get(pa).getAsJsonObject());
	                }
	                Collections.sort(list, new Comparator<JsonObject>() {
	                    public int compare(JsonObject a, JsonObject b) {
	                        String valA = a.get("TYPE").getAsString();
	                        String valB = b.get("TYPE").getAsString();

	                        return valA.compareTo(valB);
	                    }
	                });
	                JsonArray patternArray = new Gson().toJsonTree(list).getAsJsonArray();

	                map.put("datatype", patternArray.toString());

	                // 서버 Data Type Profiles 생성 시 증분검사 시기 정하기
	                if (recent > 0) {
	                    JsonArray arr = new JsonArray();
	                    JsonObject obj = new JsonObject();

	                    obj.addProperty("type", "include_recent");
	                    obj.addProperty("days", recent);
	                    arr.add(obj);
	                    jObject.add("filters", arr);
	                }

	                if (extension != null && !extension.equals("")) {
	                    JsonArray arr = new JsonArray();
	                    String[] extensions = extension.split(",");

	                    for (String ext : extensions) {
	                        JsonObject obj = new JsonObject();
	                        obj.addProperty("type", "exclude_expression");
	                        obj.addProperty("expression", "!*." + ext.trim());
	                        arr.add(obj);
	                    }
	                    jObject.add("filters", arr);
	                }

	                String data = jObject.toString();

	                if (i < dbSize) { // 기존에 있던 ap 서버에 update
	                    searchDao.updateProfile(map);
	                } else { // 새로운 ap 서버에 insert
	                    searchDao.insertProfile(map);
	                }
	                if (i == 0) searchDao.updateProfileList(map);
	            }

	            LogMap.put("key", userLogCon);
	            userLogList.add(LogMap);

	            userLog.put("user_no", user_no);
	            userLog.put("menu_name", "DATATYPE UPDATE");
	            userLog.put("user_ip", clientIP);
	            userLog.put("job_info", "개인정보 유형 수정(성공) - " + datatype_name);
	            userLog.put("logFlag", "2");
	            userLog.put("context", userLogList.toString());

	            userDao.insertLog(userLog);

	            resultMap.put("resultCode", 201);
	            resultMap.put("resultMessage", "SUCCESS");

	        } else {
	            userLog.put("user_no", user_no);
	            userLog.put("menu_name", "DATATYPE UPDATE");
	            userLog.put("user_ip", clientIP);
	            userLog.put("job_info", "개인정보 유형 수정(실패) - " + datatype_name);
	            userLog.put("logFlag", "2");
	            userLog.put("context", userLogList.toString());

	            userDao.insertLog(userLog);

	            resultMap.put("resultCode", resultCode);
	            resultMap.put("resultMessage", "Failed");
	        }
	        /*
	        // 기준이 되는 datatype_id 업데이트
	        searchDao.updateStandardId(map);

	        // 정책에 속해있는 datatype_id 업데이트
	        searchDao.updateDatatypeInPolicy(map);
	        */

	    } catch (ProtocolException e) {
	        resultMap.put("resultCode", -1);
	        logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
	    } catch (Exception e) {
	        resultMap.put("resultCode", -1);
	        logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
	    }

	    return resultMap;
	}

	@Override
	public Map<String, Object> deleteProfile(HttpServletRequest request) throws Exception {
		logger.info("resetDefaultPolicy request : " + request);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 로그 기록
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String password = request.getParameter("password");
		
		// User Log 남기기
		Map<String, Object> userLog = new HashMap<String, Object>();
				
		String datatype_id = request.getParameter("datatype_id");
		String datatype_label = request.getParameter("datatype_label");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("DATATYPE_ID", datatype_id);
		
		Map<String, Object> dataTypeMap = searchDao.selectDataTypeById(map);
		List<Map<String, Object>> resultList = searchDao.getPolicy(map);
		
		if(dataTypeMap != null) {
			for (int i = 0; i < resultList.size() ; i++) {
				Map<String, Object> result = resultList.get(i);
				if(result.get("DATATYPE_ID").equals(dataTypeMap.get("datatype_id"))) {
					resultMap.put("resultCode", -9);
					resultMap.put("resultMessage", "Failed");
					return resultMap;
				}
			}
		}		
		searchDao.deleteProfile(map);
		
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "DATATYPE DELETE");		
		userLog.put("user_ip", clientIP);
		userLog.put("job_info", "개인정보 유형 삭제 - " + datatype_label);
		userLog.put("logFlag", "2");
		
		userDao.insertLog(userLog);
		resultMap.put("resultCode", 0);
		resultMap.put("resultMessage", "삭제 됨");
		
		return resultMap;
		
	}

	@Override
	public List<Map<String, Object>> getPolicy(HttpServletRequest request) throws Exception {
		logger.info("getPolicy Service");

		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		List<Map<String, Object>> resultList = new ArrayList<>();
		
		logger.info("name :: " + request.getParameter("name"));
		
		Map<String, Object> map = new HashMap<>();
		if(!"".equals(request.getParameter("name")) || !"".equals(request.getParameter("policy_name"))) {
			map.put("name", request.getParameter("name"));
			map.put("policy_name", request.getParameter("policy_name"));
		}
		
		String policyid =  request.getParameter("policyid");
		String scheduleUse =  request.getParameter("scheduleUse");
		map.put("policyid", policyid);
		map.put("schedule_use", scheduleUse);
		map.put("grade", grade);
		map.put("user_no", user_no);
		
		logger.info(scheduleUse);
		
		resultList = searchDao.getPolicy(map);
		
		logger.info("resultList >>> " + resultList);
		for(int i=0; i<resultList.size(); i++) {
			Map<String, Object> result = resultList.get(i);
			
			result.put("NAME", set_service.replaceParameter((String) result.get("NAME")));;
			result.put("TYPE", set_service.replaceParameter((String) result.get("TYPE")));;
			result.put("TYPE1", set_service.replaceParameter((String) result.get("TYPE1")));;
			
			if(result.get("PAUSE_DAYS") != null && !result.get("PAUSE_DAYS").equals("")) {
				int days = Integer.parseInt(result.get("PAUSE_DAYS").toString());
				
				String str_days = String.format("%07d", Integer.parseInt(Integer.toBinaryString(days)));
				String[] arr_days = str_days.split("");
				result.put("PAUSE_DAYS", arr_days);
				resultList.set(i, result);
			}
		}
		
		return resultList;
	}

	@Override
	public Map<String, Object> deletePolicy(HttpServletRequest request) throws Exception {
		logger.info("deletePolicy Service");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		logger.info("idx :: " + request.getParameter("idx"));
		
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		Map<String, Object> userLog = new HashMap<String, Object>();
		
		String datatype_id = request.getParameter("datatype_id");
		int idx = Integer.parseInt(request.getParameter("idx").toString());
		
		Map<String, Object> map = new HashMap<>();
		map.put("DATATYPE_ID", datatype_id);
		
		if(!"".equals(request.getParameter("idx"))) {
			map.put("idx", request.getParameter("idx"));
			map.put("policy_id", request.getParameter("idx"));
			searchDao.deletePolicy(map);
			searchDao.deletePolicyServer(map);
		}
		
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "DATATYPE DELETE");		
		userLog.put("user_ip", clientIP);
		userLog.put("job_info", "검색 정책 삭제 성공 - " + request.getParameter("policy_name"));
		userLog.put("logFlag", "3");
		userLog.put("context", null);
		userDao.insertLog(userLog);
		
		resultMap.put("resultCode", 0);
		resultMap.put("resultMessage", "삭제 됨");
		
		return resultMap;
	}

	/* (non-Javadoc)
	 * @see com.org.iopts.search.service.SearchService#modifyPolicy(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public Map<String, Object> modifyPolicy(HttpServletRequest request) throws Exception {
		logger.info("modifyPolicy Service");
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("resultCode", -1);
		resultMap.put("resultMessage", "FAILED");

		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		Map<String, Object> userLog = new HashMap<String, Object>();
		List<Map<String, Object>> userLogList = new ArrayList<>();
		Map<String, Object> LogMap = new HashMap<String, Object>();
		
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idx", request.getParameter("idx"));
			map.put("policy_name", request.getParameter("policy_name"));
			map.put("comment", request.getParameter("comment"));
			map.put("start_dtm", request.getParameter("start_dtm"));
			map.put("scanTrace", request.getParameter("scanTrace"));
			map.put("cycle", request.getParameter("cycle"));
			map.put("action", request.getParameter("action"));
			map.put("datatype_id", request.getParameter("datatype_id"));
			map.put("std_id", request.getParameter("std_id"));
			map.put("enabled", request.getParameter("enabled"));
			map.put("from_time_hour", request.getParameter("from_time_hour"));
			map.put("from_time_minutes", request.getParameter("from_time_minutes"));
			map.put("to_time_hour", request.getParameter("to_time_hour"));
			map.put("to_time_minutes", request.getParameter("to_time_minutes"));
			map.put("pause_days", request.getParameter("pause_days"));
			map.put("user_no", request.getParameter("user"));
			map.put("policy_type", request.getParameter("policy_type"));
			
			map.put("cpu", request.getParameter("cpu"));
			map.put("memory", (request.getParameter("memory") == "" ? null : request.getParameter("memory")));
			map.put("throughput",  (request.getParameter("throughput") == "" ? null : request.getParameter("throughput")));
			
			searchDao.modifyPolicy(map);
			
			Map<String, Object> serverMap = new HashMap<>();
			serverMap.put("policy_id", request.getParameter("idx"));
			
			String serverStr = request.getParameter("serverList");
			JsonArray serverArr = new JsonArray();
			serverArr = new Gson().fromJson(serverStr, JsonArray.class);
			searchDao.deletePolicyServer(serverMap);
			
			for (int i = 0; i < serverArr.size(); i++) {
			    String idx = serverArr.get(i).getAsString();
			    logger.info("test ::: " + idx);
			    String[] targetList = idx.split("_");
			    
			    if(Integer.parseInt(targetList[0]) == 1 ) {
//			    1_243_7521933288018154557_1
			    	serverMap.put("in_idx", targetList[1]);
			    serverMap.put("target_id", targetList[2]);
			    serverMap.put("ap_no", targetList[3]);
			    }else if(Integer.parseInt(targetList[0]) == 3 ) {
//			    3_IDX_4_2155911029437468978_0
			    	serverMap.put("target_id", targetList[3]);
			    	serverMap.put("ap_no", targetList[4]);
			    	serverMap.put("in_idx", targetList[1]+"_"+targetList[2]+"_"+targetList[3]);
			    	
			    }
			    
			    searchDao.insertPolicyServer(serverMap);
			}
			
			String crcleNM = "";
			switch (request.getParameter("cycle")) {
				case "1": crcleNM = "매일";	break;
				case "2": crcleNM = "매주";	break;
				case "3": crcleNM = "매월";	break;
				default: crcleNM = "한번만";break;
			}
				
			
			String userLogCon = "정책명 : " + request.getParameter("policy_name")+ "<br><br>";
					userLogCon += ("주기 : " + crcleNM+ "<br>");
					userLogCon += ("CPU : " + request.getParameter("cpu")+ "<br>");
					userLogCon += ("Memory : " + (request.getParameter("memory").trim() == null || request.getParameter("memory").trim() == "" ? "1024 MB" : (request.getParameter("memory") + " MB")) + "<br>");
					userLogCon += ("Throughput : " + (request.getParameter("throughput").trim() == null || request.getParameter("throughput").trim() == "" ? "Unlimited" : (request.getParameter("throughput")) + " MBps"));
			LogMap.put("key", userLogCon);
			userLogList.add(LogMap);
			
			userLog.put("user_no", user_no);
			userLog.put("menu_name", "DATATYPE REGIST");		
			userLog.put("user_ip", clientIP);
			userLog.put("job_info", "검색 정책 수정 성공 - " + request.getParameter("policy_name"));
			userLog.put("logFlag", "3");
			userLog.put("context", userLogList.toString());
			userDao.insertLog(userLog);
			
			
			
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "SUCCESS");
			
		} catch (Exception e) {
			
			userLog.put("user_no", user_no);
			userLog.put("menu_name", "DATATYPE REGIST");		
			userLog.put("user_ip", clientIP);
			userLog.put("job_info", "검색 정책 수정 실패 - " + request.getParameter("policy_name"));
			userLog.put("logFlag", "3");
			userLog.put("context", null);
			userDao.insertLog(userLog);
			
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> insertPolicy(HttpServletRequest request) throws Exception {
		logger.info("insertPolicy Service");
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("resultCode", -1);
		resultMap.put("resultMessage", "FAILED");
		
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		Map<String, Object> userLog = new HashMap<String, Object>();
		List<Map<String, Object>> userLogList = new ArrayList<>();
		Map<String, Object> LogMap = new HashMap<String, Object>();
		
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("policy_name", request.getParameter("policy_name"));
			map.put("comment", request.getParameter("comment"));
			map.put("start_dtm", request.getParameter("start_dtm"));
			map.put("scanTrace", request.getParameter("scanTrace"));
			map.put("cycle", request.getParameter("cycle"));
			map.put("action", request.getParameter("action"));
			map.put("datatype_id", request.getParameter("datatype_id"));
			map.put("std_id", request.getParameter("std_id"));
			map.put("enabled", request.getParameter("enabled"));
			map.put("from_time_hour", request.getParameter("from_time_hour"));
			map.put("from_time_minutes", request.getParameter("from_time_minutes"));
			map.put("to_time_hour", request.getParameter("to_time_hour"));
			map.put("to_time_minutes", request.getParameter("to_time_minutes"));
			map.put("pause_days", request.getParameter("pause_days"));
			map.put("user_no", request.getParameter("user"));
			map.put("policy_type", request.getParameter("policy_type"));
			map.put("cpu", request.getParameter("cpu"));
			map.put("memory", (request.getParameter("memory") == "" ? null : request.getParameter("memory")));
			map.put("throughput",  (request.getParameter("throughput") == "" ? null : request.getParameter("throughput")));
			
			searchDao.insertPolicy(map);
			
			Map<String, Object> serverMap = new HashMap<>();
			serverMap.put("policy_id", map.get("policy_id"));
			
			String serverStr = request.getParameter("serverList");
			JsonArray serverArr = new JsonArray();
			serverArr = new Gson().fromJson(serverStr, JsonArray.class);
			searchDao.deletePolicyServer(serverMap);
			for (int i = 0; i < serverArr.size(); i++) {
			    /*JsonObject serverOb = serverArr.get(i).getAsJsonObject();
			    serverMap.put("target_id", serverOb.get("target_id").getAsString());
			    serverMap.put("ap_no", serverOb.get("ap_no").getAsString());
			    searchDao.insertPolicyServer(serverMap);*/
			    int idx = Integer.parseInt(serverArr.get(i).getAsString());
				serverMap.put("in_idx", idx);
				searchDao.insertPolicyServer(serverMap);
			}
			
			String crcleNM = "";
			switch (request.getParameter("cycle")) {
				case "1": crcleNM = "매일";	break;
				case "2": crcleNM = "매주";	break;
				case "3": crcleNM = "매월";	break;
				default: crcleNM = "한번만";break;
			}

			String userLogCon = "정책명 : " + request.getParameter("policy_name")+ "<br><br>";
					userLogCon += ("주기 : " + crcleNM+ "<br>");
					userLogCon += ("CPU : " + request.getParameter("cpu")+ "<br>");
					userLogCon += ("Memory : " + (request.getParameter("memory").trim() == null || request.getParameter("memory").trim() == "" ? "1024 MB" : (request.getParameter("memory") + " MB")) + "<br>");
					userLogCon += ("Throughput : " + (request.getParameter("throughput").trim() == null || request.getParameter("throughput").trim() == "" ? "Unlimited" : (request.getParameter("throughput")) + " MBps"));
			LogMap.put("key", userLogCon);
			userLogList.add(LogMap);
			
			userLog.put("user_no", user_no);
			userLog.put("menu_name", "DATATYPE REGIST");		
			userLog.put("user_ip", clientIP);
			userLog.put("job_info", "검색 정책 생성 성공 - " + request.getParameter("policy_name"));
			userLog.put("logFlag", "3");
			userLog.put("context", userLogList.toString());
			userDao.insertLog(userLog);
			
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "SUCCESS");
			
			
		} catch (Exception e) {

			userLog.put("user_no", user_no);
			userLog.put("menu_name", "DATATYPE REGIST");		
			userLog.put("user_ip", clientIP);
			userLog.put("job_info", "검색 정책 생성 실패 - " + request.getParameter("policy_name"));
			userLog.put("logFlag", "2");
			userLog.put("context", null);
			userDao.insertLog(userLog);
			
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> getStatusList(HttpServletRequest request) throws Exception {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		logger.info("getStatusList Service");
		
		List<Map<String, Object>> resultList = new ArrayList<>();
		try {
			List<Map<Object, Object>> mngrSizeList = new ArrayList<>();
			Map<Object, Object> mngrSizeMap = new HashMap<>();
			List<Map<Object, Object>> mngrList = targetDao.selectMngrNameList();
			Map<Object, Object> mngrMap = new HashMap<>();
			
			for(int i =0 ; i< mngrList.size() ; i++) {
				
				if(request.getParameter(i+"server") != null) {
					mngrSizeMap = new HashMap<>();
					mngrMap = mngrList.get(i);
					String mngrKey = request.getParameter(i+"server").toString();
					mngrSizeMap.put("server", mngrKey);
					
					logger.info(mngrKey);
					logger.info(request.getParameter(mngrKey));
					mngrSizeMap.put("server_user", request.getParameter(mngrKey));
					
					if(!request.getParameter(mngrKey).equals("")) mngrSizeList.add(mngrSizeMap);
				}
			}
			
			Map<String, Object> map = new HashMap<>();
			map.put("sch_group", request.getParameter("sch_group"));
			map.put("sch_host", request.getParameter("sch_host"));
			map.put("sch_svcName", request.getParameter("sch_svcName"));
			map.put("sch_policy", request.getParameter("sch_policy"));
			map.put("sch_svcManager", request.getParameter("sch_svcManager"));
			map.put("user_no", user_no);
			map.put("user_grade", user_grade);
			map.put("mngrList", mngrList);
			map.put("mngrSizeList", mngrSizeList);
			
			resultList = searchDao.getStatusList(map);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultList;
	}
	
	@Override
	public List<Map<String, Object>> getUserList(HttpServletRequest request) throws Exception {
		logger.info("insertPolicy Service");
		
		logger.info("sch_aut :: " + request.getParameter("sch_aut"));
		logger.info("sch_id :: " + request.getParameter("sch_id"));
		logger.info("sch_userName :: " + request.getParameter("sch_userName"));
		logger.info("sch_teamName :: " + request.getParameter("sch_teamName"));
		logger.info("sch_userLeave :: " + request.getParameter("sch_userLeave"));
		logger.info("sch_lockStatus :: " + request.getParameter("sch_lockStatus"));
		
		List<Map<String, Object>> resultList = new ArrayList<>();
		try {
			
			Map<String, Object> map = new HashMap<>();
			map.put("sch_aut", request.getParameter("sch_aut"));
			map.put("sch_id", request.getParameter("sch_id"));
			map.put("sch_userName", request.getParameter("sch_userName"));
			map.put("sch_teamName", request.getParameter("sch_teamName"));
			map.put("sch_userLeave", request.getParameter("sch_userLeave"));
			map.put("sch_lockStatus", request.getParameter("sch_lockStatus"));
			
			resultList = searchDao.getUserList(map);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*resultMap.put("resultCode", -1);
		resultMap.put("resultMessage", "FAILED");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("policy_name", request.getParameter("policy_name"));
			map.put("comment", request.getParameter("comment"));
			map.put("start_dtm", request.getParameter("start_dtm"));
			map.put("cycle", request.getParameter("cycle"));
			map.put("datatype_id", request.getParameter("datatype_id"));
			map.put("enabled", request.getParameter("enabled"));
			
			//searchDao.insertPolicy(map);
			
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "SUCCESS");
			
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return resultList;
	}

	@Override
	public Map<String, Object> registSchedule(HttpServletRequest request, String api_ver) throws Exception {
		logger.info("getMatchObjects doc : " + "/" + api_ver + "/schedules");
//		String scheduleData = request.getParameter("scheduleData");

		// User Log 남기기
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		
		ReconUtil reconUtil = new ReconUtil();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String type = request.getParameter("type");
		String scheduleName = request.getParameter("schedule_name");
		String policyID = request.getParameter("policy_id");
		String datatypeID = request.getParameter("datatype_id");
		String std_id = request.getParameter("std_id");
		String action = request.getParameter("action");
		
		int group_key = -1;
		Properties properties = new Properties();
		String resource = "/property/config.properties";
		Reader reader = Resources.getResourceAsReader(resource);
		
		properties.load(reader);
		
		JsonParser parser = new JsonParser();
		JsonArray dataArr = (JsonArray) parser.parse(request.getParameter("scheduleArr"));
		logger.info("size :: " + dataArr.size());
		
		Map<String, Object> map = new HashMap<>();
		map.put("type", type);
		map.put("user_no", user_no);
		map.put("scheduleName", scheduleName);
		map.put("policy_id", policyID);
		map.put("datatype_id", datatypeID);
		map.put("std_id", std_id);
		

		searchDao.insertScheduleGroup(map);
		group_key = (int) map.get("SCHEDULE_GROUP_ID");
		map.put("groupID", group_key);
		
		int count = 0;
		
		// User Log 남기기
		Map<String, Object> userLog = new HashMap<String, Object>();
		List<Map<String, Object>> userLogList = new ArrayList<>();
		Map<String, Object> LogMap = new HashMap<String, Object>();
		String userLogCon = "스케줄 명 : " + scheduleName + "<br><br>";
		String seccseUserLogCon = "=========================스캔 등록 성공=========================<br>";
		int seccseChk = 0;
		String failUserLogCon = "<br>=========================스캔 등록 실패=========================<br>";
		int failChk = 0;
		
		for(int i=0; i<dataArr.size(); i++) {
			/*logger.info(dataArr.get(i).toString());*/
			LogMap = new HashMap<>();
			
			logger.info(dataArr.toString());
			
			JsonObject obj = (JsonObject) dataArr.get(i);
			int ap_no = Integer.parseInt(obj.get("ap_no").toString());
			String scheduleData = obj.get("scheduleData").toString();
			JsonArray pauseList = (JsonArray) obj.get("pauseList");
			
			String target_id = obj.get("target_id").getAsString();
			String location_id = obj.get("location_id").getAsString();
			String drm = "";
			
			map.put("target_id", target_id);
			map.put("location_id", location_id);
			map.put("ap_no", ap_no);
			
			String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no+1)) ;
			String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no+1)) ;
			String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no+1)) ;
			Map<String, Object> httpsResponse = null;
			httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url+"/"+api_ver+"/schedules", "POST", scheduleData);
			
			int resultCode = (int) httpsResponse.get("HttpsResponseCode");
			String resultMessage = (String) httpsResponse.get("HttpsResponseMessage");
			
			resultMap.put("resultCode", resultCode);
			resultMap.put("resultMessage", resultMessage);
			
			logger.info("requestMsg >> " + resultMessage);
			
			userLog.put("user_no", user_no);
			userLog.put("menu_name", "SCHEDULE REGIST");
			userLog.put("user_ip", clientIP);
			//userLog.put("job_info", "스캔 등록");
			userLog.put("logFlag", "1");
			
			String logHost = obj.get("name").toString().replace("\"", "");
			
			if(obj.get("connectedIp") != null && obj.get("connectedIp").equals("")) {
				logHost += "(" + obj.get("connectedIp") +")";
			}
			
			logHost +=  "[" + (ap_no+1) + "]" ;
			if(resultCode == 201) {
				count ++;
				seccseUserLogCon += logHost + "<br>";
				++seccseChk;
				map.put("type", type);
				map.put("user_no", user_no);
				map.put("scheduleName", scheduleName);
				map.put("policy_id", policyID);
				map.put("datatype_id", datatypeID);
				map.put("std_id", std_id);
				map.put("action", action);
				map.put("ap_no", ap_no);
				JsonElement element = parser.parse(httpsResponse.get("HttpsResponseDataMessage").toString());
				JsonObject object = element.getAsJsonObject();
				
				String schedule_id = object.get("id").getAsString();
				map.put("reconScheduleID", schedule_id);
				// 스케줄 그룹 등록
				logger.info("mpa :: " + map);
				if( pauseList !=null ){
					if(pauseList.size() > 0) {
						insertPauseSchedule(pauseList, map, "02");
					}
				}
				
				// 스케줄 타겟 등록
				searchDao.insertScheduleTargets(map);
				
				// 스캔 등록 성공 시 스케줄 리스트 업데이트
				updateScanschedule(reconUtil, recon_url, recon_id, recon_password, ap_no, schedule_id, drm);
			} else if (resultCode == 409) {
				failUserLogCon += (logHost + "/ 사유 : 스케줄명 중복<br>");
				++failChk;
//				userLog.put("job_info", "스캔 등록 실패 - 스케줄명 중복");
			} else if (resultCode == 422) {
				failUserLogCon += (logHost + "/ 사유 : 시작시간 오류<br>");
				++failChk;
//				userLog.put("job_info", "스캔 등록 실패 - 시작시간 오류");
			} else {
				failUserLogCon += (logHost + " / 사유 : " + resultCode + "<br>");
				++failChk;
			}
		}
		
		if(seccseChk==0)seccseUserLogCon="";
		if(failChk==0)failUserLogCon="";
		userLogCon += (seccseUserLogCon + failUserLogCon);
		LogMap.put("key", userLogCon);
		userLogList.add(LogMap);
		
		userLog.put("context", userLogList.toString());
		userLog.put("job_info", "스캔 등록");
		
		userDao.insertLog(userLog);
		if(count == 0) {
			searchDao.failedSchedule(map);
		}
		
		return resultMap;
	}
	

	private void insertPauseSchedule(JsonArray pauseList, Map<String, Object> map, String type) {
		JsonElement pauseObject = pauseList.get(0);
		
		Map<String, Object> resultMap = new HashMap<>();
		
		logger.info("pauseObject :::: " + pauseObject);
		
		JsonObject pauseObjectJson = pauseObject.getAsJsonObject();
		String start = pauseObjectJson.get("start").getAsString();
		String end = pauseObjectJson.get("end").getAsString();

		JsonArray daysArray = pauseObjectJson.getAsJsonArray("day");

		for (int i = 0; i < daysArray.size(); i++) {
			resultMap = new HashMap<>();
		    String day = daysArray.get(i).getAsString();
		    logger.info(day);
		    
		    resultMap.put("schedule_group_id", map.get("groupID"));
		    resultMap.put("schedule_id", map.get("reconScheduleID"));
		    resultMap.put("active_status", type);
		    resultMap.put("start_time", end);
		    resultMap.put("pause_time", start);
		    resultMap.put("week_day", day);
		    resultMap.put("ap_no", map.get("ap_no"));
		    resultMap.put("user_no", SessionUtil.getSession("memberSession", "USER_NO"));
		    
		    try {
		    	searchDao.insertPauseSchedule(resultMap);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("insert recon stop sch ::: " + e.getLocalizedMessage());
			}
		}
	}

	@Override
	public Map<String, Object> registPCSchedule(HttpServletRequest request, String api_ver) throws Exception {
		logger.info("getMatchObjects doc : " + "/" + api_ver + "/schedules");
//		String scheduleData = request.getParameter("scheduleData");

		// User Log 남기기
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		
		ReconUtil reconUtil = new ReconUtil();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String type = request.getParameter("type");
		String scheduleName = request.getParameter("schedule_name");
		String net_type = request.getParameter("net_type");
		String policyID = "";
		String datatypeID = "";
		String std_id = "";
		String action = "";
		String drm = null;
		String trace = "";
/*		String policyID = request.getParameter("policy_id");
		String datatypeID = request.getParameter("datatype_id");
		String std_id = request.getParameter("std_id");
		String action = request.getParameter("action");
*/		
		int group_key = -1;
		Properties properties = new Properties();
		String resource = "/property/config.properties";
		Reader reader = Resources.getResourceAsReader(resource);
		
		properties.load(reader);
		
		JsonParser parser = new JsonParser();
		JsonArray scheduleArr = (JsonArray) parser.parse(request.getParameter("scheduleArr"));
		logger.info("size :: " + scheduleArr.size());
		JsonArray dataArr = (JsonArray) parser.parse(request.getParameter("dataArr"));
		
		Map<String, Object> map = new HashMap<>();
		map.put("type", type);
		map.put("user_no", user_no);
		map.put("scheduleName", scheduleName);
		map.put("policy_id", -1);
		map.put("datatype_id", -1);
		map.put("std_id", -1);
		
		logger.info("drequest.getParameter(\"dataArr\")>>>" + request.getParameter("dataArr"));
		if(net_type.equals("3")) { // OneDrive 검색 실행일 경우
			searchDao.insertScheduleGroupOneDrive(map);
		}else {
			searchDao.insertScheduleGroup(map);
		}
		group_key = (int) map.get("SCHEDULE_GROUP_ID");
		map.put("groupID", group_key);
		
		int count = 0;
		
		for(int i=0; i<scheduleArr.size(); i++) {
			/*logger.info(dataArr.get(i).toString());*/
			
			logger.info(scheduleArr.toString());
			JsonObject obj = (JsonObject) scheduleArr.get(i);
			
			JsonObject dataObj = (JsonObject) dataArr.get(i);
			logger.info("dataObj.toString() >>>" + dataObj.toString());
			
			policyID = dataObj.get("policy_id").toString();
			datatypeID = dataObj.get("datatype_id").getAsString();
			std_id = dataObj.get("std_id").getAsString();
			action = dataObj.get("action").toString();
			
			if(dataObj.get("drm_status").getAsString().equals("NONE")) {
				drm = null;
			}else {
				drm = dataObj.get("drm_status").getAsString(); 
			}
			
			int ap_no = Integer.parseInt(obj.get("ap_no").toString());
			String scheduleData = obj.get("scheduleData").toString();
			
			String target_id = obj.get("target_id").getAsString();
			String location_id = obj.get("location_id").getAsString();

			map.put("target_id", target_id);
			map.put("location_id", location_id);
			map.put("ap_no", ap_no);
			
			String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no+1)) ;
			String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no+1)) ;
			String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no+1)) ;
			Map<String, Object> httpsResponse = null;
			
			/*if(ap_no != 0) {	// PC일 경우, 기존 스캔 기동중인 항목 취소 처리
				logger.info("Previous Schedule Delete !");
				
				String schedule_id = "";
				
				List<Map<String, Object>> scheduleList = searchDao.selectSchedulePCStatus(map);
				
				for(int j = 0; j < scheduleList.size(); j++ ) {	// schedule, paused, scanning 상태 인 항목 정지
					schedule_id = scheduleList.get(j).get("SCHEDULE_ID").toString();
							
					httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + api_ver + "/schedules/" + schedule_id + "/cancel", "POST", null);
			     	updateScanschedule(reconUtil, recon_url, recon_id, recon_password, 0, schedule_id);
				}
			}*/
			
			httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url+"/"+api_ver+"/schedules", "POST", scheduleData);
			
			int resultCode = (int) httpsResponse.get("HttpsResponseCode");
			String resultMessage = (String) httpsResponse.get("HttpsResponseMessage");
			
			resultMap.put("resultCode", resultCode);
			resultMap.put("resultMessage", resultMessage);
			
			Map<String, Object> userLog = new HashMap<String, Object>();
			
			logger.info("requestMsg >> " + resultMessage);
			
			userLog.put("user_no", user_no);
			userLog.put("menu_name", "SCHEDULE REGIST");
			userLog.put("user_ip", clientIP);
			//userLog.put("job_info", "스캔 등록");
			userLog.put("logFlag", "1");
			
			if(resultCode == 201) {
				count ++;
				userLog.put("job_info", "스캔 등록 성공");
				map.put("type", type);
				map.put("user_no", user_no);
				map.put("scheduleName", scheduleName);
				map.put("policy_id", policyID);
				map.put("std_id", std_id);
				map.put("action", action);
				map.put("drm", drm);
				
				JsonElement element = parser.parse(httpsResponse.get("HttpsResponseDataMessage").toString());
				JsonObject object = element.getAsJsonObject();
				
				String schedule_id = object.get("id").getAsString();
				map.put("datatype_id", datatypeID);
				map.put("reconScheduleID", schedule_id);
				// 스케줄 그룹 등록
				logger.info("mpa :: " + map);
				
				// 스케줄 타겟 등록
				searchDao.insertScheduleTargets(map);
				
				// 스캔 등록 성공 시 스케줄 리스트 업데이트
				updateScanschedule(reconUtil, recon_url, recon_id, recon_password, ap_no, schedule_id, drm);
			} else if (resultCode == 409) {
				userLog.put("job_info", "스캔 등록 실패 - 스케줄명 중복");
			} else if (resultCode == 422) {
				userLog.put("job_info", "스캔 등록 실패 - 시작시간 오류");
			} else {
				userLog.put("job_info", "스캔 등록 실패 - " + resultCode);
			}
			
			userDao.insertLog(userLog);
		}
		
		if(count == 0) {
			searchDao.failedSchedule(map);
		}
		
		return resultMap;
	}
	
	private void updateScanschedule(ReconUtil reconUtil, String recon_url, String recon_id, String recon_password, int ap_no,String schedule_id, String drm) throws Exception {
		logger.info("updateScanSchedule");
		String start_date = "";
		String end_date = "";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		start_date = sdf.format(cal.getTime());
		cal.add(Calendar.YEAR, 1);
		end_date = sdf.format(cal.getTime());	
		
		try {
			String url =  String.format("%s/%s/schedules/%s?details=true&completed=true&cancelled=true&stopped=true&failed=true&deactivated=true&pending=true&start_date=%s&limit=5000000&end_date=%s'",
					recon_url, api_ver,schedule_id, start_date, end_date);
			logger.info("url :: " + url);
			
			Map<String, Object> httpsResponse = reconUtil.getServerData(recon_id, recon_password, url, "GET", "");
			
			JsonParser parser = new JsonParser();
			
			//JsonArray arr = (JsonArray) parser.parse(httpsResponse.get("HttpsResponseData").toString());
			
			JsonObject obj = (JsonObject) parser.parse(httpsResponse.get("HttpsResponseData").toString());
			
			Map<String, Object> map = new HashMap<>();
			map.put("id", obj.get("id").toString().replaceAll("\"", ""));
			map.put("label", obj.get("label").toString().replaceAll("\"", ""));
			map.put("status", obj.get("status").toString().replaceAll("\"", ""));
			map.put("repeat_days", obj.get("repeat_days").toString().replaceAll("\"", ""));
			map.put("repeat_months", obj.get("repeat_months").toString().replaceAll("\"", ""));
			map.put("next_scan", obj.get("next_scan").toString().replaceAll("\"", ""));
			map.put("cpu", obj.get("cpu").toString().replaceAll("\"", ""));
			map.put("capture", obj.get("capture").toString().replaceAll("\"", ""));
			map.put("trace", obj.get("trace").toString().replaceAll("\"", ""));
			map.put("ap_no", ap_no);
			
			logger.info("obj >>> " + obj);
			
			JsonArray pro_arr = (JsonArray) obj.get("profiles");
			String profiles = "";
			for(int pi=0; pi<pro_arr.size(); pi++) {
				profiles += pro_arr.get(pi).toString().replaceAll("\"", "");
				
				if(pi < (pro_arr.size()-1)) {
					profiles += ",";
				}
			}
			map.put("profiles", profiles);
			
			if(obj.has("pause")) {
				JsonArray pause_arr = (JsonArray) obj.get("pause");
				for(int pausei=0; pausei<pause_arr.size(); pausei++) {
					JsonObject pause_obj = (JsonObject) pause_arr.get(pausei);
					//{"days":62,"from":28800,"to":72000}
					
					map.put("pause_days", pause_obj.get("days").toString().replaceAll("\"", ""));
					map.put("pause_from", pause_obj.get("from").toString().replaceAll("\"", ""));
					map.put("pause_to", pause_obj.get("to").toString().replaceAll("\"", ""));
				}
			}
			
			JsonArray t_arr = (JsonArray) obj.get("targets");
			String target_id = "";
			String target_name = "";
			for(int ti=0; ti<t_arr.size(); ti++) {
				JsonObject t_obj = (JsonObject) t_arr.get(ti);
				target_id += t_obj.get("id").toString().replaceAll("\"", "");
				target_name += t_obj.get("name").toString().replaceAll("\"", "");
				if(ti < (t_arr.size()-1)) {
					target_id += ",";
					target_name += ",";
				}
			}
			map.put("target_id", target_id);
			map.put("target_name", target_name);
			
			if(drm !="" && drm != null) {
				map.put("drm", drm);
			}else {
				map.put("drm", null);
			}

			logger.info("map :: " + map.toString());
			searchDao.updateScanSchedule(map);
			
			logger.info(httpsResponse.toString());
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public List<Map<String, Object>> registScheduleGroup(HttpServletRequest request) throws Exception {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		Map<String, Object> map = new HashMap<>();
		String title = request.getParameter("title");
		String writer = request.getParameter("writer");
		String sch_type = request.getParameter("sch_type");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String dayKey =  request.getParameter("dayKey");
		
		map.put("title", title);
		map.put("writer", writer);
		map.put("sch_type", sch_type);
		map.put("fromDate", fromDate);
		map.put("toDate", toDate);
		map.put("user_no", user_no);
		map.put("user_grade", user_grade);
		map.put("dayKey", dayKey);
		map.put("user_grade", user_grade);
		List<Map<String, Object>> resultList =  searchDao.selectScheduleGroup(map);
		
		for(int i=0 ;i<resultList.size() ; i++) {
			Map<String, Object> resultMap = resultList.get(i);
			resultMap.put("SCHEDULE_GROUP_NAME", set_service.replaceParameter((String)resultMap.get("SCHEDULE_GROUP_NAME")));
			resultMap.put("POLICY_NAME", set_service.replaceParameter((String)resultMap.get("POLICY_NAME")));
		
			resultList.set(i, resultMap);
		}
		
		return resultList;
	}
	
	@Override
	public List<Map<String, Object>> registScheduleTargets(HttpServletRequest request) throws Exception {
		String group_id = request.getParameter("group_id");
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		Map<String, Object> map = new HashMap<>();
		int ap_no = 0;
		String id = "";
		String drm = "";
		ReconUtil reconUtil = new ReconUtil();

		Properties properties = new Properties();
		String resource = "/property/config.properties";
		Reader reader = Resources.getResourceAsReader(resource);
		
		properties.load(reader);
		Map<String, Object> httpsResponse = null;
		
		map.put("user_grade", user_grade);
		map.put("user_no", user_no);
		map.put("group_id", group_id);
		List<Map<String,Object>> scheduleList = searchDao.selectScheduleTargets(map);

		for (Map<String, Object> item : scheduleList) {
			String status = (String) item.get("SCHEDULE_STATUS");
			
			if(status.equals("cancelled") || status.equals("completed") || status.equals("failed")) {
				continue;
			}
			
			ap_no = Integer.parseInt(item.get("AP_NO").toString());
			id = (String) item.get("RECON_SCHEDULE_ID");
			
			String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no+1)) ;
			String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no+1)) ;
			String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no+1)) ;
			
			try {
				updateScanschedule(reconUtil, recon_url, recon_id, recon_password, ap_no, id, drm);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return searchDao.selectScheduleTargets(map);
	}
	

	@Override
	public Map<String, Object> changeSchedule(HttpServletRequest request, String api_ver) throws Exception {

		//https://172.30.1.58:8339/beta/schedules/98/Action
		String id = request.getParameter("id");
		String task = request.getParameter("task");
		int ap_no = Integer.parseInt(request.getParameter("ap_no"));
		String target_id = request.getParameter("target_id");
		
		logger.info("getMatchObjects doc : " + "/" + api_ver + "/schedules/" + id + "/" + task +", Ap_no : " + ap_no);
		
		Properties properties = new Properties();
		String resource = "/property/config.properties";
		Reader reader = Resources.getResourceAsReader(resource);
		
		properties.load(reader);
		
		String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no+1)) ;
		String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no+1)) ;
		String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no+1)) ;

		Map<String, Object> resultMap = new HashMap<String, Object>();
		ReconUtil reconUtil = new ReconUtil();
		Map<String, Object> httpsResponse = null;
		
		try {
			httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + api_ver + "/schedules/" + id + "/" + task, "POST", null);
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			resultMap.put("resultCode", -1);
			logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
			return resultMap;
		}

		int resultCode = (int) httpsResponse.get("HttpsResponseCode");
		String resultMessage = (String) httpsResponse.get("HttpsResponseMessage");
		String drm = "";
		
		if ((resultCode != 200) && (resultCode != 204)) {
			resultMap.put("resultCode", resultCode);
			resultMap.put("resultMessage", resultMessage);
			return resultMap;
		}
		// 작업변경이 성공하면 DB도 변경 해 준다.
     	String changedTask = "scheduled";
     	switch (task) {
     	case "deactivate" :
     		changedTask = "deactivated";
     		break;
     	case "skip" :
     		changedTask = "scheduled";
     		break;
     	case "pause" :
     		changedTask = "pause";
     		break;
     	case "resume" :
     		changedTask = "scanning";
     		break;
     	case "restart" :
     		changedTask = "scheduled";
     		break;
     	case "stop" :
     		changedTask = "stopped";
     		break;
     	case "cancel" :
     		changedTask = "cancelled";
     		break;
     	case "reactivate" :
     		changedTask = "scheduled";
     		break;
 		default :
     		changedTask = "scheduled";
 		break;
     	}
     	
     	updateScanschedule(reconUtil, recon_url, recon_id, recon_password, ap_no, id, drm);
  	
		resultMap.put("resultCode", 0);
		resultMap.put("resultMessage", "SUCCESS");
		
		// DB 업데이트
		Map<String, Object> scheduleMap = new HashMap<String, Object>();
		scheduleMap.put("changedTask", changedTask);
		scheduleMap.put("id", id);
		scheduleMap.put("ap_no", ap_no);
		
		searchDao.updateScheduleStatus(scheduleMap);
		
		// 상태 변경 값을 실시간으로 받아오기 위한 시간 제약(3초)
		Thread.sleep(3000);
		updateScanschedule(reconUtil, recon_url, recon_id, recon_password, ap_no, id, drm);
		
		// User Log 남기기
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		Map<String, Object> userLog = new HashMap<String, Object>();
		
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "SCHEDULE CHANGE");		
		userLog.put("user_ip", clientIP);
		userLog.put("job_info", "스캔 상태 변경 > " + changedTask + "[" + changedTask + "]");
		userLog.put("logFlag", "1");

		userDao.insertLog(userLog);
		
		return resultMap;
	}


	@Override
	public Map<String, Object> changeScheduleAll(List<String> taskList, HttpServletRequest request, String api_ver) throws Exception {

		//https://172.30.1.58:8339/beta/schedules/98/Action
		String groupid = request.getParameter("groupid");
		String task = request.getParameter("task");
		String id = "";
		String drm = "";
		int ap_no = 0;
		ReconUtil reconUtil = new ReconUtil();
		
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Properties properties = new Properties();
		String resource = "/property/config.properties";
		Reader reader = Resources.getResourceAsReader(resource);
		
		properties.load(reader);
		
		/* 스케줄 상태 재확인 (scanning, scheduled 항목에 대해 스케줄 항목을 recon 데이터로 업데이트) */
		Map<String, Object> mapa = new HashMap<>();
		List<String> tasksList = new ArrayList<>();
		tasksList.add("scheduled");
		tasksList.add("scanning");
		mapa.put("user_grade", user_grade);
		mapa.put("user_no", user_no);
		mapa.put("group_id", groupid);
		mapa.put("taskList", tasksList);
		
		if(task.equals("stop") || task.equals("cancel")) {
			mapa.put("status", 0);
			searchDao.updateScheduleGroupStatus(mapa);
		}
		
		List<Map<String,Object>> scheduleList = searchDao.selectScheduleTargets(mapa);
		
		for (Map<String, Object> item : scheduleList) {
			ap_no = Integer.parseInt(item.get("AP_NO").toString());
			id = (String) item.get("RECON_SCHEDULE_ID");
			
			String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no+1)) ;
			String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no+1)) ;
			String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no+1)) ;
			
			try {
				updateScanschedule(reconUtil, recon_url, recon_id, recon_password, ap_no, id, drm);
			}catch (Exception e) {
				resultMap.put("resultCode", -1);
				logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
				return resultMap;
			}
		}
		
		/* 변경된 상태로 스케줄 변경 */
		Map<String, Object> map = new HashMap<>();
		map.put("group_id", groupid);
		map.put("taskList", taskList);
		map.put("user_grade", user_grade);
		map.put("user_no", user_no);
		
		List<Map<String,Object>> scheduleStatusList = searchDao.selectScheduleTargets(map);
		
		Map<String, Object> httpsResponse = null;
		
		for (Map<String, Object> item : scheduleStatusList) {
			ap_no = Integer.parseInt(item.get("AP_NO").toString());
			id = (String) item.get("RECON_SCHEDULE_ID");
			
			String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no+1)) ;
			String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no+1)) ;
			String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no+1)) ;
			
			logger.info("getGroup Status Update doc : " + "/" + api_ver + "/schedules/" + id + "/" + task);
			
			try {
				httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + api_ver + "/schedules/" + id + "/" + task, "POST", null);
				
				int resultCode = (int) httpsResponse.get("HttpsResponseCode");
				String resultMessage = (String) httpsResponse.get("HttpsResponseMessage");
				
				if ((resultCode != 200) && (resultCode != 204)) {
					resultMap.put("resultCode", resultCode);
					resultMap.put("resultMessage", resultMessage);
					return resultMap;
				}
				
				updateScanschedule(reconUtil, recon_url, recon_id, recon_password, ap_no, id, drm);
			}catch (Exception e) {
				resultMap.put("resultCode", -1);
				logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
				return resultMap;
			}
		}
  	
		resultMap.put("resultCode", 0);
		resultMap.put("resultMessage", "SUCCESS");
		/*
		// User Log 남기기
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		Map<String, Object> userLog = new HashMap<String, Object>();
		
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "SCHEDULE CHANGE");		
		userLog.put("user_ip", clientIP);
		userLog.put("job_info", "스캔 상태 변경 > " + changedTask + "[" + changedTask + "]");
		userLog.put("logFlag", "1");

		userDao.insertLog(userLog);*/
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> completedSchedule(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String groupid = request.getParameter("groupid");
		
		
		Map<String, Object> map = new HashMap<>();
		map.put("group_id", groupid);
		map.put("status", 2);
		searchDao.updateScheduleGroupStatus(map);
		
		resultMap.put("resultCode", 0);
		resultMap.put("resultMessage", "SUCCESS");
		
		return resultMap;
	}
	
	// 데이터 타입 갖고오기
	@Override
	public List<Map<String, Object>> selectScanDataTypes(HttpServletRequest request) throws Exception {
		String std_id = request.getParameter("std_id");
		Map<String, Object> map = new HashMap<>();
		ReconUtil reconUtil = new ReconUtil();

		Properties properties = new Properties();
		String resource = "/property/config.properties";
		Reader reader = Resources.getResourceAsReader(resource);
		
		properties.load(reader);
		Map<String, Object> httpsResponse = null;
		
		map.put("std_id", std_id);
		
		return searchDao.selectScanDataTypes(map);
	}
	
	@Override
	public Map<String, Object> selectSKTScanDataTypes(HttpServletRequest request) throws Exception {
		String ap_no = request.getParameter("ap_no");
		String target_id = request.getParameter("target_id");
		String location_id = request.getParameter("location_id");
		String net_type = request.getParameter("net_type");
		Map<String, Object> map = new HashMap<>();
		
		map.put("ap_no", ap_no);
		map.put("target_id", target_id);
		map.put("location_id", location_id);
		map.put("net_type", net_type);
		
		return searchDao.selectSKTScanDataTypes(map);
	}

	@Override
	public Map<String, Object> confirmApply(HttpServletRequest request, String api_ver) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		
		String target_id = request.getParameter("target_id");
		String confirm = request.getParameter("confirm");
		String schedule_id = request.getParameter("schedule_id");
		String ap_no = request.getParameter("ap_no");
		
		resultMap.put("target_id", target_id);
		resultMap.put("confirm", confirm);
		resultMap.put("schedule_id", schedule_id);
		resultMap.put("ap_no", ap_no);
		
		logger.info("target_id >> " + target_id + " , confirm >>" + confirm + " , schedule_id >> " + schedule_id + ", ap_no >> " + ap_no);
		
		if(!ap_no.equals("0")) {
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "대상 서버에 적용이 불가능합니다.");
			
			return resultMap;
		}
		
		searchDao.updateConfirmApply(resultMap);
		
		if(confirm.equals("0")) {
			
			logger.info("getMatchObjects doc : " + "/" + api_ver + "/schedules/" + schedule_id + "/cancel");
			
			Properties properties = new Properties();
			String resource = "/property/config.properties";
			Reader reader = Resources.getResourceAsReader(resource);
			
			properties.load(reader);
			
			String recon_url = properties.getProperty("recon.url");
			String recon_id = properties.getProperty("recon.id");
			String recon_password = properties.getProperty("recon.password") ;
			String drm = "";
			
			ReconUtil reconUtil = new ReconUtil();
			Map<String, Object> httpsResponse = null;
			
			try {
				httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + api_ver + "/schedules/" + schedule_id + "/cancel", "POST", null);
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				resultMap.put("resultCode", -1);
				logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
				return resultMap;
			}

			int resultCode = (int) httpsResponse.get("HttpsResponseCode");
			String resultMessage = (String) httpsResponse.get("HttpsResponseMessage");
			
			if ((resultCode != 200) && (resultCode != 204)) {
				resultMap.put("resultCode", resultCode);
				resultMap.put("resultMessage", resultMessage);
				return resultMap;
			}
			// 작업변경이 성공하면 DB도 변경 해 준다.
	     	String changedTask = "cancelled";
	     	
	     	updateScanschedule(reconUtil, recon_url, recon_id, recon_password, 0, schedule_id, drm);
	  	
			resultMap.put("resultCode", 1);
			resultMap.put("resultMessage", "검색 진행을 취소하였습니다.");
			
			// User Log 남기기
			String user_no = SessionUtil.getSession("memberSession", "USER_NO");
			ServletUtil servletUtil = new ServletUtil(request);
			String clientIP = servletUtil.getIp();
			Map<String, Object> userLog = new HashMap<String, Object>();
			
			userLog.put("user_no", user_no);
			userLog.put("menu_name", "SCHEDULE CHANGE");		
			userLog.put("user_ip", clientIP);
			userLog.put("job_info", "스캔 상태 변경 > " + changedTask + "[" + changedTask + "]");
			userLog.put("logFlag", "1");

			userDao.insertLog(userLog);
			
		} else if (confirm.equals("1")) {
			resultMap.put("resultCode", 1);
			resultMap.put("resultMessage", "대상 스케줄을 확정하였습니다.");
			
		}
		
		return resultMap;
	}
	

	@Override
	public Map<String, Object> updateSchedule(HttpServletRequest request) throws Exception {

		logger.info("getMatchObjects doc : " + "/" + api_ver + "/schedules");
//		String scheduleData = request.getParameter("scheduleData");

		// User Log 남기기
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		
		ReconUtil reconUtil = new ReconUtil();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		int group_key = Integer.parseInt(request.getParameter("scheduleid"));
		
		
		Properties properties = new Properties();
		String resource = "/property/config.properties";
		Reader reader = Resources.getResourceAsReader(resource);
		
		properties.load(reader);
		
		JsonParser parser = new JsonParser();
		JsonArray dataArr = (JsonArray) parser.parse(request.getParameter("scheduleArr"));
		logger.info("size :: " + dataArr.size());
		
		Map<String, Object> map = new HashMap<>();
		
		for(int i=0; i<dataArr.size(); i++) {
			/*logger.info(dataArr.get(i).toString());*/
			
			JsonObject obj = (JsonObject) dataArr.get(i);
			int ap_no = Integer.parseInt(request.getParameter("ap_no"));;
			String scheduleData = obj.get("scheduleData").toString();
			
			String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no+1)) ;
			String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no+1)) ;
			String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no+1)) ;
			
			Map<String, Object> httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url+"/"+api_ver+"/schedules/"+group_key, "PUT", scheduleData);
			
			int resultCode = (int) httpsResponse.get("HttpsResponseCode");
			String resultMessage = (String) httpsResponse.get("HttpsResponseMessage");
			
			resultMap.put("resultCode", resultCode);
			resultMap.put("resultMessage", resultMessage);
			
			Map<String, Object> userLog = new HashMap<String, Object>();
			
			logger.info("requestMsg >> " + resultMessage);
			
			userLog.put("user_no", user_no);
			userLog.put("menu_name", "SCHEDULE UPDATE");
			userLog.put("user_ip", clientIP);
			//userLog.put("job_info", "스캔 등록");
			userLog.put("logFlag", "1");
			
			if(resultCode == 204) {
				userLog.put("job_info", "스캔쥴 변경 성공");
				map.put("target_id", obj.get("target_id").getAsString());
				
				logger.info("map :: " + map);
				
				// 스캔쥴 변경 - 없데이트
				//updateScanschedule(reconUtil, recon_url, recon_id, recon_password, ap_no, group_key+"");
			} else if (resultCode == 409) {
				userLog.put("job_info", "스캔쥴 변경 실패 - 스케줄명 중복");
			} else if (resultCode == 422) {
				userLog.put("job_info", "스캔쥴 변경 실패 - 시작시간 오류");
			} else {
				userLog.put("job_info", "스캔쥴 변경 실패 - " + resultCode);
			}
			
			userDao.insertLog(userLog);
		}
		
		return resultMap;
	}
	
	@Override
	public void updateReconSchedule(HttpServletRequest request) throws Exception {
		logger.info("getMatchObjects doc : " + "/" + api_ver + "/schedules");

		ReconUtil reconUtil = new ReconUtil();

		int group_key = Integer.parseInt(request.getParameter("scheduleid"));
		String drm = "";

		Properties properties = new Properties();
		String resource = "/property/config.properties";
		Reader reader = Resources.getResourceAsReader(resource);
		properties.load(reader);

		int ap_no = Integer.parseInt(request.getParameter("ap_no"));

		String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no+1)) ;
		String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no+1)) ;
		String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no+1)) ;

		updateScanschedule(reconUtil, recon_url, recon_id, recon_password, ap_no, group_key+"", drm);

	}
	
	@Override
	public List<Map<String, Object>> selectNetHost(HttpServletRequest request) throws Exception {
		String type = request.getParameter("type");
		
		Map<String, Object> map = new HashMap<>();
		StringBuffer ip = new StringBuffer();
		
		switch (type) {
		case "type1":	//OA(SOC)
			ip.append("150.20.18.|150.20.20.|150.20.21.|150.20.22.|150.20.23.|150.20.47.|");
			ip.append("150.20.44.|150.20.45.|150.20.19.");
			break;
		case "type2":	//OA(N-SOC)
			ip.append("150.24.95.|150.28.65.|150.28.66.|150.28.67.|150.28.68.|150.28.69.|150.28.70.|150.28.78.|150.28.79.");
			break;
		case "type3":	//유통망(서비스 ACE/TOP지점)
			
			break;
		case "type4":	//유통망(대리점)
			
			break;
		case "type5":	//유통망(F&U/미납센터, PS&M본사)
			//PS&M
			for(int i = 44; i < 49; i++) {
				ip.append("10.40."+ i + ".|");
			}
			ip.append("90.31.252.|90.31.253.|");
			ip.append("10.40.74.|10.40.75.|");
			for(int i = 55; i < 59; i++) {
				ip.append("10.40."+ i + ".|");
			}
			ip.append("10.40.7.|10.40.11.");
			break;
		case "type6":	//VDI
			// mydesk
			for(int i = 64; i < 104; i++) {
				ip.append("172.26."+ i + ".|");
			}
			ip.append("10.179.30.|10.179.31.|10.179.96.|10.179.98.|10.179.11.|10.179.13.|10.179.14.|10.179.18.|10.179.28.|10.179.97.|10.179.99.|10.179.109.|10.179.111.|10.179.113.|10.178.131.|");
			// DTVDI
			ip.append("172.26.59.|172.26.60.|172.26.61.|172.26.62.|");
			// 망분리(VDI)
			ip.append("172.26.240.|172.26.242.|172.26.244.|");
			// 고객센터
			for(int i = 1; i < 9; i++) {
				ip.append("172.26."+ i + ".|");
			}
			ip.append("172.26.9.");
			break;
		case "type7":	//VDI(SOC)
			ip.append("150.20.33.|150.20.34.|150.20.35.|150.20.37.|150.20.38.");
			break;
		default:
			break;
		}
		
		map.put("net_ip", ip.toString());
		
		
		
		return searchDao.selectNetHost(map);
	}
	
	
	@Override
	public Map<String, Object> getScanDetails(HttpServletRequest request, String api_ver) throws Exception {
	    // https://masterIP:8339/beta/schedules/98?details=true
	    String id = request.getParameter("id");
	    int ap_no = Integer.parseInt(request.getParameter("ap_no"));

	    Properties properties = new Properties();
	    String resource = "/property/config.properties";
	    Reader reader = Resources.getResourceAsReader(resource);

	    properties.load(reader);

	    logger.info("getMatchObjects doc : " + "/beta/schedules/" + id + "?details=true");

	    Map<String, Object> resultMap = new HashMap<>();
	    ReconUtil reconUtil = new ReconUtil();
	    Map<String, Object> httpsResponse = null;

	    String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no + 1));
	    String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no + 1));
	    String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no + 1));

	    try {
	        httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + api_ver + "/schedules/" + id + "?details=true", "GET", null);
	    } catch (ProtocolException e) {
	        resultMap.put("resultCode", -1);
	        logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
	        return resultMap;
	    }

	    int resultCode = (int) httpsResponse.get("HttpsResponseCode");
	    String resultMessage = (String) httpsResponse.get("HttpsResponseMessage");
	    if (resultCode != 200) {
	        resultMap.put("resultCode", resultCode);
	        resultMap.put("resultMessage", resultMessage);
	        return resultMap;
	    }

	    JsonObject jsonObject = new Gson().fromJson(httpsResponse.get("HttpsResponseData").toString(), JsonObject.class);

	    ArrayList<Map<String, String>> detailList = new ArrayList<>();

	    JsonArray jsonArr = jsonObject.getAsJsonArray("targets");

	    for (int i = 0; i < jsonArr.size(); i++) {
	        JsonObject obj = jsonArr.get(i).getAsJsonObject();

	        String name = obj.get("name").getAsString();
	        String status = "";
	        String percentage = "";
	        String currentlyFile = "";
	        JsonArray locArr = new JsonArray();
	        if (obj.has("locations")) {
	            locArr = obj.getAsJsonArray("locations");
	        }

	        for (int j = 0; j < locArr.size(); j++) {
	            JsonObject locObj = locArr.get(j).getAsJsonObject();
	            status = locObj.get("status").getAsString();

	            String message = "";
	            percentage = "";
	            currentlyFile = ""; 
	            try {
	            	String search_status = locObj.get("status").getAsString(); 
	            	if (search_status.equals("scanning")) { 
	            		message = locObj.get("message").getAsString(); 
	            		percentage = message.substring(0, message.indexOf("%") + 1);
	            		currentlyFile = message.substring(message.indexOf("'File path ") + 11, message.length() - 1);
	            	}else {
	            		currentlyFile = locObj.get("name").getAsString(); 
	            	}
	            } catch (Exception e) {    
	                logger.error(e.toString());
	                e.printStackTrace(); 
	            }
  
	            Map<String, String> data = new HashMap<>();
	            data.put("name", name);
	            data.put("status", status);
	            data.put("percentage", percentage);
	            data.put("currentlyFile", currentlyFile);

	            detailList.add(data);
	        }
	    }

	    resultMap.put("resultData", detailList);
	    resultMap.put("resultCode", 0);
	    resultMap.put("resultMessage", "SUCCESS");

	    return resultMap;
	}
	
	@Override
	public Map<String, Object> getScanPercentage(HttpServletRequest request, String api_ver) throws Exception {
		String group_id = request.getParameter("group_id");
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		Map<String, Object> map = new HashMap<>();
		int ap_no = 0;
		String id = "";
		String drm = "";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ReconUtil reconUtil = new ReconUtil();
		
		try {
			Properties properties = new Properties();
			String resource = "/property/config.properties";
			Reader reader = Resources.getResourceAsReader(resource);
			
			properties.load(reader);
			Map<String, Object> httpsResponse = null;
			
			map.put("user_grade", user_grade);
			map.put("user_no", user_no);
			map.put("group_id", group_id);
			List<Map<String,Object>> scheduleList = searchDao.selectScheduleTargets(map);
			
			ArrayList<Map<String, String>> detailList = new ArrayList<>();
			double totalPercentage = 0;
			for (Map<String, Object> item : scheduleList) {
				String status = (String) item.get("SCHEDULE_STATUS");
				
				if(!status.equals("scanning")) {
					continue;
				}
				
				ap_no = Integer.parseInt(item.get("AP_NO").toString());
				id = (String) item.get("RECON_SCHEDULE_ID");
				
				String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no+1)) ;
				String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no+1)) ;
				String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no+1)) ;
				
				try {
					httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/"+api_ver+"/schedules/" + id + "?details=true", "GET", null);
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block
					resultMap.put("resultCode", -1);
					logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
					return resultMap;
				}
				
				int resultCode = (int) httpsResponse.get("HttpsResponseCode");
				String resultMessage = (String) httpsResponse.get("HttpsResponseMessage");
				if (resultCode != 200) {
					resultMap.put("resultCode", resultCode);
					resultMap.put("resultMessage", resultMessage);
					return resultMap;
				}
				
				JsonObject jsonObject = new Gson().fromJson(httpsResponse.get("HttpsResponseData").toString(), JsonObject.class);
				
				JsonArray jsonArr = jsonObject.getAsJsonArray("targets");
				int percentage = 0;
				
				for (int i = 0; i < jsonArr.size(); i++) {
					int targetPathSize = 0;
					double targetPercentage = 0;
					JsonObject obj = jsonArr.get(i).getAsJsonObject();
					
					JsonArray locArr = new JsonArray();
					if (obj.has("locations")) {
						locArr = obj.getAsJsonArray("locations");
					}
					
					targetPathSize = locArr.size();
					for (int j = 0; j < locArr.size(); j++) {
						JsonObject locObj = locArr.get(j).getAsJsonObject();
						String message = "";
						try {
							String search_status = locObj.get("status").getAsString();
							if (search_status.equals("scanning")) {
								message = locObj.get("message").getAsString();  
								targetPercentage += Double.parseDouble(message.substring(0, message.indexOf("%")));
							}else if (search_status.equals("completed")) {
								targetPercentage += 100;
							}
						} catch (Exception e) {
							logger.error(e.toString());
							e.printStackTrace();  
						}
					} 
					totalPercentage  += targetPercentage/targetPathSize;
				}
				
			}
			double result = (scheduleList.size() != 0) ? totalPercentage / scheduleList.size() : 0.0;
			String percentage = String.format("%.2f%%", result);  
			
			logger.info("totalPercentage ::: " + totalPercentage);
			resultMap.put("resultData", percentage);
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "SUCCESS");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultMap;
	}
	
	// pc 정책 생성(네트워크, 그룹, pc)
	@Override
	public Map<String, Object> insertNetPolicy(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		int type = Integer.parseInt(request.getParameter("type")); 	// 네트워크, 그룹, pc 구분
		String rangeId = request.getParameter("rangeId"); 			// 대상의 아이디 (네트워크=net_id, 그룹= insa_code, pc=target_id)
		String rangeNm = request.getParameter("rangeNm"); 			// 이름
		String rangeType = request.getParameter("rangeType"); 		// pc일 경우 호스트 ap_no 구분 용
		String trace = request.getParameter("trace"); 				// 스캔 트레이스 로그
		String drm = request.getParameter("drm"); 					// drm status
		String policyId = request.getParameter("policyId"); 		// 검색 정책
		String[] timeArr = request.getParameterValues("timeArr"); 	// 검색 시간
		String scanday = request.getParameter("scanday"); 		// 검색 정책
		
		map.put("timeArr", timeArr);
		map.put("type", type);
		map.put("rangeId", rangeId);
		map.put("rangeNm", rangeNm);
		map.put("ap_no", rangeType);
		map.put("trace", trace);
		map.put("drm", drm);
		map.put("policyId", policyId);
		map.put("scanday", scanday);
		
		int net_id = searchDao.selectScheduleId(); // net_id 
		map.put("net_id", net_id);
		
		try {
			
			if(type == 0) { // 망 등록
				map.put("rangeId", rangeId.replace("TYPE", ""));
				searchDao.insertNetPolicy(map);
				searchDao.insertNetSchedule(map);
				searchDao.updateNetTarget(map);
			} else if(type == 1) { // 그룹 등록
				searchDao.insertNetPolicy(map);
				searchDao.insertNetSchedule(map);
				searchDao.updateNetDeptTarget(map);
			} else if(type == 2) { // PC 등록
				searchDao.insertNetPolicy(map);
				searchDao.insertNetSchedule(map);
				searchDao.updateNetTarget(map);
			} 
			
		} catch (Exception e) {
			searchDao.deleteNetPolicy(map);
			searchDao.deleteNetSchedule(map);
			
			map.put("resultCode", 400);
			map.put("resultMsg", "PC 정책 등록에 실패하였습니다.");
			
		}
		
		return map;
	}
	
	// OneDrive 정책 생성
	@Override
	public Map<String, Object> insertOneDrivePolicy(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		int type = Integer.parseInt(request.getParameter("type")); 					// 네트워크, 그룹, pc 구분
		String rangeId = request.getParameter("rangeId"); 							// 대상의 아이디 (네트워크=net_id, 그룹= insa_code, pc=target_id)
		String rangeNm = request.getParameter("rangeNm"); 							// 이름
		String rangeOneDriveNm = request.getParameter("rangeOneDriveNm"); 			// 검색 시간
		String rangeType = request.getParameter("rangeType"); 						// pc일 경우 호스트 ap_no 구분 용
		String trace = request.getParameter("trace"); 								// 스캔 트레이스 로그
		String policyId = request.getParameter("policyId"); 						// 검색 정책
		String targetId = request.getParameter("targetId"); 						// 검색 정책
		
		String[] rangeIdArr = request.getParameterValues("rangeId");
		
		map.put("type", type);
		/*map.put("rangeId", rangeId);*/
		map.put("targetId", targetId);
		map.put("rangeNm", rangeNm);
		/*map.put("rangeOneDriveNm", rangeOneDriveNm);*/
		map.put("ap_no", rangeType);
		map.put("trace", trace);
		map.put("policyId", policyId);
		
		int net_id = searchDao.selectScheduleId(); // net_id 
		map.put("net_id", net_id);
		try {
		    JsonArray reangeNameArray = new Gson().toJsonTree(rangeOneDriveNm).getAsJsonArray();
		    JsonArray reangeIdArray = new Gson().toJsonTree(rangeId).getAsJsonArray();
		    map.put("rangeIdArr", reangeIdArray);
			
			searchDao.updateNetOneDrive(map); // pi_location.net_id 업데이트
			for (int i = 0 ; i < reangeIdArray.size() ; i++) {
				map.put("rangeId", reangeIdArray.get(i).toString());
				map.put("rangeOneDriveNm", reangeNameArray.get(i).toString());
				
				searchDao.insertOneDrivePolicy(map);
			}
			
		} catch (Exception e) {
			searchDao.deleteNetPolicy(map);
			searchDao.deleteNetSchedule(map);
			
			map.put("resultCode", 400);
			map.put("resultMsg", "PC 정책 등록에 실패하였습니다.");
			
		}
		
		return map;
	}
	
	// OneDrive 정책 업데이트
	@Override
	public Map<String, Object> updateOneDrivePolicy(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
	
		String net_id = request.getParameter("net_id");
		int type = Integer.parseInt(request.getParameter("type")); 					// type == 3 (OneDrive)
		int beforeType = Integer.parseInt(request.getParameter("beforeType"));		// 기존 타입 (네트워크, 그룹, pc 구분)
		String beforeRangeId = request.getParameter("beforeRangeId");				// 기존 대상의 아이디 (네트워크=net_id, 그룹= insa_code, pc=target_id)
		String rangeId = request.getParameter("rangeId"); 							// location_id
		String rangeNm = request.getParameter("rangeNm"); 							// 이름
		String rangeOneDriveNm = request.getParameter("rangeOneDriveNm"); 			// 검색 시간
		String rangeType = request.getParameter("rangeType"); 						// pc일 경우 호스트 ap_no 구분 용
		String trace = request.getParameter("trace"); 								// 스캔 트레이스 로그
		String policyId = request.getParameter("policyId"); 		
		String targetId = request.getParameter("targetId"); 		
		
		int update_net_id = searchDao.selectScheduleId(); // net_id 
		map.put("net_id", update_net_id);
		
		map.put("type", type);
		map.put("targetId", targetId);
		map.put("beforeRangeId", beforeRangeId);
		map.put("beforeType", beforeType);
		map.put("rangeId", rangeId);
		map.put("rangeNm", rangeNm);
		map.put("ap_no", rangeType);
		map.put("policyId", policyId);
		map.put("before_net_id", net_id);
		map.put("trace", trace);
		try {

			if(type == 0) {
				map.put("rangeId", rangeId.replace("TYPE", ""));
			}
			
			// 기존에 생성된 데이터 net_id 삭제 (전과 다른 대상일 경우)
			updatePoliy(beforeRangeId, beforeType, rangeId, map);
			
			if(type == 3) {
				JsonArray reangeNameArray = new Gson().toJsonTree(rangeOneDriveNm).getAsJsonArray();
			    JsonArray reangeIdArray = new Gson().toJsonTree(rangeId).getAsJsonArray();
			    map.put("rangeIdArr", reangeIdArray);
				
				searchDao.updateNetOneDrive(map); // pi_location.net_id 업데이트
				searchDao.updateNetStatus(map);
				
				for (int i = 0 ; i < reangeIdArray.size() ; i++) {
					
					map.put("rangeId", reangeIdArray.get(i).toString());
					map.put("rangeOneDriveNm", reangeNameArray.get(i).toString());
					
					searchDao.insertOneDrivePolicy(map);
				}
			}
				
		} catch (Exception e) {
			searchDao.deleteNetPolicy(map);
			searchDao.deleteNetSchedule(map);
			
			map.put("resultCode", 400);
			map.put("resultMsg", "PC 정책 등록에 실패하였습니다.");
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> updateNetPolicy(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		int type = Integer.parseInt(request.getParameter("type"));
		int beforeType = Integer.parseInt(request.getParameter("beforeType"));
		String beforeRangeId = request.getParameter("beforeRangeId");
		String rangeId = request.getParameter("rangeId");
		String rangeNm = request.getParameter("rangeNm");
		String rangeType = request.getParameter("rangeType");
		String ap_no = request.getParameter("ap_no");
		String net_id = request.getParameter("net_id");
		String policyId = request.getParameter("policyId");
		String trace = request.getParameter("trace");
		String drm = request.getParameter("drm");
		String scanday = request.getParameter("scanday");
		String[] timeArr = request.getParameterValues("timeArr");
		
		
		map.put("timeArr", timeArr);
		map.put("type", type);
		map.put("beforeRangeId", beforeRangeId);
		map.put("beforeType", beforeType);
		map.put("rangeId", rangeId);
		map.put("rangeNm", rangeNm);
		map.put("rangeType", rangeType);
		map.put("policyId", policyId);
		map.put("net_id", net_id);
		map.put("NET_ID", net_id);
		map.put("ap_no", ap_no);
		map.put("trace", trace);
		map.put("scanday", scanday);
		map.put("drm", drm);
		
		try {
			
			if(type == 0) {
				map.put("rangeId", rangeId.replace("TYPE", ""));
			}
			
			// 기존에 생성된 데이터 net_id 삭제 (전과 다른 대상일 경우)
			updatePoliy(beforeRangeId, beforeType, rangeId, map);
			
			if(type != 3) {
				searchDao.updateNetPolicy(map); 
				searchDao.updateNetSchedule(map);
				
				if(type == 0 || type == 2) { // 네트워크, PC 업데이트
					searchDao.updateNetTarget(map); 
				} else if(type == 1) { // 그룹 업데이트
					searchDao.updateNetDeptTarget(map);
				} 
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}

	@Override
	public List<Map<String, Object>> selectPCPolicy(HttpServletRequest request) throws Exception {
		String net_nm = request.getParameter("net_nm");
		
		Map<String, Object> map = new HashMap<>();
		map.put("net_nm", net_nm);
		
		
		return searchDao.selectPCPolicy(map);
	}
	
	@Override
	public List<Map<String, Object>> selectPCPolicyTime(HttpServletRequest request) throws Exception {
		String net_id = request.getParameter("net_id");
		
		Map<String, Object> map = new HashMap<>();
		map.put("net_id", net_id);
		
		return searchDao.selectPCPolicyTime(map);
	}
	
	@SuppressWarnings("null")
	@Override
	public void deletePCPolicy(HttpServletRequest request) throws Exception {
		String net_id = request.getParameter("net_id");
		int type = Integer.parseInt(request.getParameter("type"));
		String group_id = request.getParameter("group_id");
		String ap_no = request.getParameter("ap_no");
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("NET_ID", net_id);
		map.put("group_id", group_id);
		map.put("ap_no", ap_no);
		
		try {
			// 망 삭제 일경우 (해당 망에 결러 있는 Target List 제거)
			if(type == 0) {
				map.put("UPDATE_NET", null);
				
				searchDao.deletePCPolicy(map);
				searchDao.updatePCPolicy(map);
			// 그룹 삭제 일경우 (해당 상위 그룹에 정책이 걸려 있는 항목을 찾고 상위 정책으로 변경, 상위 그룹이 없을 경우 망 정책으로 적용)
			} else if(type == 1) {
				Map<String, Object> result = searchDao.selectUpGroupPolicy(map);
				
				//상위 그룹에 망정책이 걸려있음
				if(result != null) {
					map.put("UPDATE_NET", result.get("NET_ID"));	// 기존에 걸려있던 상위 정책 으로 변경
					searchDao.updatePCPolicy(map);
					searchDao.deletePCPolicy(map);
				} else {	// 상위 그룹에 pc정책이 안걸려있으면, 망정책으로 변경
					searchDao.updateUpNetwork(map);
					searchDao.deletePCPolicy(map);
				}
				
			// PC, OneDrive 삭제 일 경우 (상위 그룹 정책 찾고 , 없으면 상위 망 검색)	
			} else if(type == 2 || type == 3) {
				Map<String, Object> result = searchDao.selectUpPCPolicy(map);
				if(result != null) {
					map.put("UPDATE_NET", result.get("NET_ID"));	// 기존에 걸려있던 상위 정책 으로 변경
					searchDao.updatePCPolicy(map);
					searchDao.deletePCPolicy(map);
				} else {	// 상위 그룹에 pc정책이 안걸려있으면, 망정책으로 변경
					searchDao.updateUpNetwork(map);
					searchDao.deletePCPolicy(map);
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			logger.error(e.toString());
		}
		
		
	}
	
	@Override
	public List<Map<String, Object>> selectPCSearchStatus(HttpServletRequest request) throws Exception {
		String net_id = request.getParameter("net_id");
		String location_id = request.getParameter("location_id");
		String type = request.getParameter("type");
		Map<String, Object> map = new HashMap<>();
		int ap_no = 0;
		String id = "";
		String drm = "";
		ReconUtil reconUtil = new ReconUtil();

		Properties properties = new Properties();
		String resource = "/property/config.properties";
		Reader reader = Resources.getResourceAsReader(resource);
		
		properties.load(reader);
		Map<String, Object> httpsResponse = null;
		
		map.put("net_id", net_id);
		map.put("location_id", location_id);
		map.put("type", type);
		List<Map<String,Object>> scheduleList = searchDao.selectScheduleTargets(map);

		for (Map<String, Object> item : scheduleList) {
			String status = (String) item.get("SCHEDULE_STATUS");
			
			if(status.equals("cancelled") || status.equals("completed") || status.equals("failed")) {
				continue;
			}
			
			ap_no = Integer.parseInt(item.get("AP_NO").toString());
			id = (String) item.get("RECON_SCHEDULE_ID");
			
			String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no+1)) ;
			String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no+1)) ;
			String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no+1)) ;
			
			try {
				updateScanschedule(reconUtil, recon_url, recon_id, recon_password, ap_no, id, drm);
			}catch (Exception e) {
				
			}
		}
		
		return searchDao.selectPCSearchStatus(map);
	}

	@Override
	public Map<String, Object> selectUserSearchCount(HttpServletRequest request) throws Exception {
		
		String user_no = request.getParameter("user_no");
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("user_no", user_no);
		
		return searchDao.selectUserSearchCount(map);
	}
	
	@Override
	public List<Map<String, Object>> netList(HttpServletRequest request) {
		
		String net_type = request.getParameter("net_type");
		String net_ip = request.getParameter("net_ip");
		String net_type_class = request.getParameter("net_type_class");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("net_type", net_type);
		map.put("net_type_class", net_type_class);
		map.put("net_ip", net_ip);
		map.put("fromDate", fromDate);
		map.put("toDate", toDate);
		
		logger.info("map >>>>>>>>> " + map);
		
		return searchDao.netList(map);
	}
	
	
	@Override
	public Map<String, Object> insertNetIp(HttpServletRequest request) throws Exception {
		
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> userLog = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			// User Log 남기기
			String user_no = SessionUtil.getSession("memberSession", "USER_NO");
			ServletUtil servletUtil = new ServletUtil(request);
			String clientIP = servletUtil.getIp();
			
			String type = request.getParameter("type");
			String type_name = request.getParameter("type_name");
			String type_class = request.getParameter("type_class");
			String ip = request.getParameter("ip");
			String mask = request.getParameter("mask");
			String mask_ip = request.getParameter("mask_ip");
			
			map.put("type", type);
			map.put("type_name", type_name);
			map.put("type_class", type_class);
			map.put("ip", ip);
			map.put("mask", mask);
			map.put("mask_ip", mask_ip);
			
			userLog.put("user_no", user_no);
			userLog.put("menu_name", "NET CREATED");
			userLog.put("user_ip", clientIP);
			userLog.put("logFlag", "9");
			userLog.put("job_info", "net_ip 생성("+ip+"/"+mask+")");
			
			int resultInt = searchDao.selectNetIpCheck(map);
		
			if(resultInt > 0) {
				resultMap.put("resultCode", -9);
				resultMap.put("resultMessage", "이미 생성된 네트워크 입니다.");
			}else {
				searchDao.insertNetIp(map);
				userDao.insertLog(userLog);
				
				resultMap.put("resultCode", 0);
				resultMap.put("resultMessage", "네트워크가 생성 되었습니다.");
			}
			
			
		}catch (Exception e) {
			// TODO: handle exception
			logger.error(e.toString());
		}
		return resultMap;
	}
	
	@Override
	public Map<String, Object> updateNetIp(HttpServletRequest request) throws Exception {
		
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> userLog = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			// User Log 남기기
			String user_no = SessionUtil.getSession("memberSession", "USER_NO");
			ServletUtil servletUtil = new ServletUtil(request);
			String clientIP = servletUtil.getIp();
			
			String type = request.getParameter("type");
			String type_name = request.getParameter("type_name");
			String type_class = request.getParameter("type_class");
			String ip = request.getParameter("ip");
			String mask = request.getParameter("mask");
			String mask_ip = request.getParameter("mask_ip");
			
			String reachNet_Ip = request.getParameter("reachNet_Ip");
			String reachNetMask = request.getParameter("reachNetMask");
			
			
			map.put("type", type);
			map.put("type_name", type_name);
			map.put("type_class", type_class);
			map.put("ip", ip);
			map.put("mask", mask);
			map.put("mask_ip", mask_ip);
			
			map.put("reachNet_Ip", reachNet_Ip);
			map.put("reachNetMask", reachNetMask);
			
			userLog.put("user_no", user_no);
			userLog.put("menu_name", "NET CREATED");
			userLog.put("user_ip", clientIP);
			userLog.put("logFlag", "9");
			userLog.put("job_info", "net_ip 수정("+ip+"/"+mask+")");
			
			searchDao.updateNetIp(map);
			userDao.insertLog(userLog);
			
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", "네트워크가 수정 되었습니다.");
			
		}catch (Exception e) {
			// TODO: handle exception
			logger.error(e.toString());
		}
		return resultMap;
	}

	@Override
	public void deleteNetIp(HttpServletRequest request) throws Exception {
		// User Log 남기기
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		
		String ip = request.getParameter("ip");
		String mask = request.getParameter("mask");
		
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> userLog = new HashMap<String, Object>();
		
		map.put("mask", mask);
		map.put("ip", ip);
		
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "NET DELETE");
		userLog.put("user_ip", clientIP);
		userLog.put("logFlag", "9");
		userLog.put("job_info", "net_ip 삭제("+ip+"/"+mask+")");
		
		try {
			searchDao.deleteNetIp(map);
			userDao.insertLog(userLog);
			
		}catch (Exception e) {
			// TODO: handle exception
			logger.error(e.toString());
		}
	}
	

	
	private void updatePoliy(String beforeRangeId, int beforeType, String rangeId, Map<String, Object> map) throws Exception {

		try {
			if(!beforeRangeId.equals(rangeId)) {
				if(beforeType == 0) { // 네트워크 였을 경우
					searchDao.deleteNetId(map);
				}else if (beforeType == 1) { // 그룹이 였을 경우
					searchDao.deleteNetGroup(map);
				}else if (beforeType == 2) { // pc 였을 경우
					searchDao.deleteNetId(map);
				}else if (beforeType == 3) { // OneDrive 였을 경우
					searchDao.deleteOneDriveNetId(map);
					/*searchDao.deleteOneDriveNetId2(map);*/
				}
			}
			
			if(beforeType != 3) {
				searchDao.deleteNetSchedule(map); // 생성된 시간 제거
			}
			
		} catch (Exception e) {
			logger.error("policy update error >>> " + e);
		}
	}
	
	@Override
	public int insertNetListIp(HashMap<String, Object> params, HttpServletRequest request) throws Exception {
		String resulList = request.getParameter("resulList");
		Map<String, Object> resultMap2 = new HashMap<String, Object>();
		
		List<Map<String, Object>> NetList = new ArrayList<>();
		
		int resultInt = 0;
		JsonArray jsonarry = new Gson().toJsonTree(resulList).getAsJsonArray();

		try {
		    for (int i = 0; i < jsonarry.size(); i++) {
		        Map<String, Object> resultMap = new HashMap<>();
		        JsonObject jsonObject = jsonarry.get(i).getAsJsonObject();

		        String ip = jsonObject.get("ip").getAsString();
		        String type = jsonObject.get("type").getAsString();
		        String detail = jsonObject.get("detail").getAsString();
		        String mask = jsonObject.get("mask").getAsString();
		        String mask_ip = jsonObject.get("mask_ip").getAsString();
				
				if(resultMap2.containsKey(mask)) {
	                List<String> existingIPs = (List<String>) resultMap2.get(mask);
	                existingIPs.add(ip);
	            } else {
	                List<String> newIPs = new ArrayList<String>();
	                newIPs.add(ip);
	                resultMap.put("mask", mask);
	                resultMap.put("IPList", newIPs);
	                
	                resultMap2.put(mask, newIPs);
	            }
				if(resultMap.size() > 0) {
					NetList.add(resultMap);
				}
			}
			
			logger.info("insert Mask List >>> " + resultMap2.keySet());
			
			for(int i = 0; i < NetList.size(); i++) {
				
				int resultNetInt = searchDao.selectNetMask(NetList.get(i));
				
				if(resultNetInt > 0) {
					resultInt = resultInt + resultNetInt;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultInt;
	}

	@Override
	public Map<String, Object> insertNetList(HashMap<String, Object> params, HttpServletRequest request) throws Exception {
		String resulList = request.getParameter("resulList");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<Map<String, Object>> NetList = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> mapList = new HashMap<String, Object>();
		
		JsonArray jsonarry = new Gson().toJsonTree(resulList).getAsJsonArray();
		
		try {
			for(int i = 0; i < jsonarry.size(); i++) {
				
				map = new HashMap<String, Object>();
				
				JsonObject jsonObject = (JsonObject) jsonarry.get(i);
				
				String ip = jsonObject.get("ip").getAsString();
				String type = jsonObject.get("type").getAsString();
				String type_name = jsonObject.get("type_name").getAsString();
				String type_class = jsonObject.get("detail").getAsString();
				String mask = jsonObject.get("mask").getAsString();
				String mask_ip = jsonObject.get("mask_ip").getAsString();
				
				map.put("ip", ip);
				map.put("type", type);
				map.put("type_name", type_name);
				map.put("type_class", type_class);
				map.put("mask", mask);
				map.put("mask_ip", mask_ip);
				
				searchDao.insertNetList(map);
			}
			
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", jsonarry.size() + "건 insert 완료");
			
			// User Log 남기기
			String user_no = SessionUtil.getSession("memberSession", "USER_NO");
			Map<String, Object> userLog = new HashMap<String, Object>();
			ServletUtil servletUtil = new ServletUtil(request);
			String clientIP = servletUtil.getIp();
			
			userLog.put("user_no", user_no);
			userLog.put("menu_name", "NETLIST INSERT");
			userLog.put("user_ip", clientIP);
			userLog.put("logFlag", "9");
			userLog.put("job_info", "네트워크 일괄 등록");
			
			userDao.insertLog(userLog);
			
		} catch (Exception e) {
			e.printStackTrace();
			
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "insert 실패");
		}
		
		return resultMap;
	}
	
	@Override
	public int insertFilterList(HashMap<String, Object> params, HttpServletRequest request) throws Exception {
		String resulList = request.getParameter("resulList");
		Map<String, Object> resultMap2 = new HashMap<String, Object>();
		
		List<Map<String, Object>> filterList = new ArrayList<>();
		
		int resultInt  = 0;
		int notServer = 0;
		JsonArray jsonarry = new Gson().toJsonTree(resulList).getAsJsonArray();
		
		try {
			
			for(int i = 0; i < jsonarry.size(); i++) {
				
				JsonArray exArray = new JsonArray();
				JsonObject exInfo = new JsonObject();
				
				ReconUtil reconUtil = new ReconUtil();
				Map<String, Object> httpsResponse = null;
				
				Map<String, Object> resultMap = new HashMap<String, Object>();
				JsonObject jsonObject = (JsonObject) jsonarry.get(i);
				
				Map<String, Object> map = new HashMap<String, Object>();
				
				String ap_nm = jsonObject.get("ap_nm").getAsString();
				String target_nm = jsonObject.get("target_id").getAsString();
				String path = jsonObject.get("path").getAsString();
				
				Map<String, Object> targetMap = new HashMap<String, Object>();
				
				resultMap.put("path", path);
				
				exInfo.addProperty("expression", path);
				exInfo.addProperty("type", "exclude_expression");
				
				map.put("ap_nm", ap_nm);
				if(target_nm.equals("전체")){
					targetMap = searchDao.selectTargetIdAll(map);
				}else {
					map.put("target_name", target_nm);
					targetMap = searchDao.selectTargetId(map);
					resultMap.put("ip", targetMap.get("TARGET_ID"));
					exInfo.addProperty("apply_to", targetMap.get("TARGET_ID") != null ? targetMap.get("TARGET_ID").toString() : null);
				}
				
				exArray.add(exInfo);
				if(targetMap != null) {
					resultMap.put("ip", targetMap.get("IP"));
					resultMap.put("ap_no", targetMap.get("AP_NO"));
				}else {
					++notServer;
					break;
				}
				
				try {
					
					int ap_no = Integer.parseInt(resultMap.get("ap_no").toString());
					
					Properties properties = new Properties();
					String resource = "/property/config.properties";
					Reader reader = Resources.getResourceAsReader(resource);
					
					properties.load(reader);
					
					String api_ver = properties.getProperty("recon.api.version");
					
					String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no+1)) ;
					String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no+1)) ;
					String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no+1)) ;
					
					httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + api_ver + "/filters", "POST", exArray.toString());		
					
					int resultCode = Integer.parseInt(httpsResponse.get("HttpsResponseCode").toString());
					
					if(resultCode == 201) {
						resultMap.put("resultCode", 0);
						resultMap.put("resultMessage", "예외 처리 성공");
					}else {
						resultMap.put("resultCode", -2);
						resultMap.put("resultMessage", "예외 처리 실패");
					}
				}catch (Exception e) {
					// TODO: handle exception
					resultMap.put("resultCode", -1);
					logger.error("Error: " + e.getMessage(), e);
			resultMap.put("resultMessage", "처리중 에러가 발생하였습니다.");
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultInt;
	}
	
	@Override
	public Map<String, Object> selectDataTypeId(HttpServletRequest request) throws Exception {
		logger.info("selectDataTypeId Service!");
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			String datatype_id = request.getParameter("datatype_id");
			String std_id = request.getParameter("std_id");
			String ap_no = request.getParameter("ap_no");
			
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("datatype_id", datatype_id);
			map.put("std_id", std_id);
			map.put("ap_no", ap_no);
			
			resultMap = searchDao.selectDataTypeId(map);
			resultMap.put("resultCode", 1);
			
		} catch (Exception e) {
			resultMap.put("resultCode", -1);
			logger.error(e.getMessage());
		}
		
		return resultMap;
	}

	@Override
	public Map<String, Object> getStopSch(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		
		
		try {
			String sch_id = request.getParameter("sch_id");
			List<Map<String, Object>> resultList  =  searchDao.getStopSch(sch_id);
			
			if(resultList.size() == 0) {
				resultMap.put("resultMessage", null);
			}else {
				resultMap.put("resultMessage", resultList);
			}
			resultMap.put("resultCode", 0);
			
		} catch (Exception e) {
			resultMap.put("resultMessage", "ERROR");
			resultMap.put("resultCode", -1);
			
			e.printStackTrace();
			logger.info(" select Stop Sch ::: " + e.getLocalizedMessage());
		}
		
		return resultMap;
	}

	@Override
	public Map<String, Object> schStopUpdate(HttpServletRequest request) throws Exception {
		String sch_id = request.getParameter("sch_id");
		String recon_id = request.getParameter("recon_id");
		String schedule_name = request.getParameter("schedule_name");
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String ap_no = null;
		
		ServletUtil servletUtil = new ServletUtil(request);
		String clientIP = servletUtil.getIp();
		int disableSch = 0;
		
		List<Map<String, Object>> reconSchId = searchDao.selectReconId(sch_id); // 이전 스케줄 조회
		
		JsonParser parser = new JsonParser();
		JsonArray stopArray = (JsonArray) parser.parse(request.getParameter("stopList"));
		
		Map<String, Object> stopMap = new HashMap<>();
		Map<String, Object> resultMap = new HashMap<>();
		
		Map<String, Object> userLog = new HashMap<String, Object>();
		List<Map<String, Object>> userLogList = new ArrayList<>();
		Map<String, Object> LogMap = new HashMap<String, Object>();
		String userLogCon = "일시정지 스케줄 <br> 스케줄 작성자 : " + user_no ;
		String userLogCon2 = null;
		stopMap.put("sch_id", sch_id);
		stopMap.put("user_no", user_no);
		
		// 이전 일시정지 스케줄 해제
		List<Map<String, Object>> exSch = searchDao.selectPausTimeData(sch_id); // 이전 스케줄 조회
		
		if(exSch.size() > 0) {
			LogMap = new HashMap<>();
			userLogCon2 = "이전 일시정지 스케줄 <br> 스케줄 작성자 : " + exSch.get(0).get("USER_NO");
			for (Map<String, Object> map : exSch) {
				userLogCon2 += "<br>스케줄 정지 요일 :";
				
				String[] day = map.get("WEEK_DAY").toString().split(",");
				String day_NM = "모든 요일";
				
				if(day.length != 6) {
					for(int d=0 ; d < day.length ; d++) {
						switch(day[d]) {
						case "sun" : day_NM = "일요일"; break;
						case "mon" : day_NM = "월요일"; break;
						case "tue" : day_NM = "화요일"; break;
						case "wed" : day_NM = "수요일"; break;
						case "thu" : day_NM = "목요일"; break;
						case "fri" : day_NM = "금요일"; break;
						case "sat" : day_NM = "토요일"; break;
						}
						
						if(d!=0) userLogCon2 += ", "+day_NM;
						else userLogCon2 += day_NM;
						
					}
				}
				userLogCon2 += "<br>정지시간 : "+ map.get("PAUSE_TIME") + " ~ 시작 시간 : " + map.get("START_TIME")+"<br>";
			}
			
			LogMap.put("key", userLogCon2);
			userLogList.add(LogMap);
			
			searchDao.updatePausTimeData(stopMap);
		}
		
		LogMap = new HashMap<>();
		LogMap.put("key", "============================================================<br>");
		userLogList.add(LogMap);
		
		for (Map<String, Object> rMap : reconSchId) {
			
			logger.info("rMap ::: " + recon_id);
			recon_id = rMap.get("RECON_SCHEDULE_ID").toString();
			ap_no = rMap.get("AP_NO").toString();
			
			
			logger.info("recon_id :: " + recon_id);
			logger.info("ap_no :: " + ap_no);
			stopMap.put("recon_id", recon_id);
			stopMap.put("ap_no", ap_no);
			logger.info("stopMap :: " + stopMap);
			
//			스케줄 등록
			for(int i=0; i<stopArray.size(); i++) {
				LogMap = new HashMap<>();
				userLogCon += "<br>스케줄 정지 요일 : ";
				JsonObject obj = (JsonObject) stopArray.get(i);
				
				JsonArray stopDayArray = obj.get("day").getAsJsonArray();

				List<String> stopDayList = new ArrayList<>();
				for (JsonElement dayElement : stopDayArray) {
				    stopDayList.add(dayElement.getAsString());
				}
				
				String day_NM = "모든 요일";
				if(stopDayArray.size() != 6) {
					int dayCNT = 0;
					for (JsonElement dayElement : stopDayArray) {
						
						switch(dayElement.getAsString()) {
						case "sun" : day_NM = "일요일"; break;
						case "mon" : day_NM = "월요일"; break;
						case "tue" : day_NM = "화요일"; break;
						case "wed" : day_NM = "수요일"; break;
						case "thu" : day_NM = "목요일"; break;
						case "fri" : day_NM = "금요일"; break;
						case "sat" : day_NM = "토요일"; break;
						}
						
						if(dayCNT!=0) userLogCon += ", "+day_NM;
						else userLogCon += day_NM;
						++dayCNT;
					}
				}
				
				userLogCon += "<br>정지시간 : "+ obj.get("start").getAsString() + " ~ 시작 시간 : " + obj.get("stop").getAsString()+"<br>";
				 
				stopMap.put("stopDay", stopDayList);
				stopMap.put("pause_time", obj.get("start").getAsString());
				stopMap.put("start_time", obj.get("stop").getAsString());
				stopMap.put("active_status", "00");
				
				try {
					
					// 신규 일시정지 스케줄 등록
					searchDao.insertPausTimeData(stopMap);
					
				} catch (SQLException s) {
					s.printStackTrace();
					logger.info("error ::: " + s.getLocalizedMessage());
					resultMap.put("resultMessage", "ERROR");
					resultMap.put("resultCode", -1);
				} 
				
				LogMap.put("key", userLogCon2);
				if(disableSch == 0) userLogList.add(LogMap);
			}
			
		}
		
		userLog.put("user_no", user_no);
		userLog.put("menu_name", "SCAN STOP SCHEDULE");		
		userLog.put("user_ip", clientIP);
		userLog.put("job_info", "일시정지 스케줄 추가 - " + schedule_name);
		userLog.put("logFlag", "1");
		userLog.put("context", userLogList.toString());
		
		userDao.insertLog(userLog);
		
		resultMap.put("resultMessage", "SUCCES");
		resultMap.put("resultCode", 0);
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> addNextTarget(HttpServletRequest request) throws Exception {
		logger.info("DB Credentials Add Service");
		int ap_no = 0;
		
		ReconUtil reconUtil = new ReconUtil();
		Map<String, Object> httpsResponse = null;
		Map<String, Object> map = new HashMap<>();

		Gson gson = new Gson();
		
		try {
			String name = request.getParameter("name");
			Properties properties = new Properties();
			String resource = "/property/config.properties";
			Reader reader = Resources.getResourceAsReader(resource);

			properties.load(reader);

			String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no + 1));
			String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no + 1));
			String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no + 1));
			JsonObject jo = new JsonObject();

			//jo.addProperty("target_name", name);
			jo.addProperty("name", name);
			jo.addProperty("group_id", searchDao.selectDefaultGroup());
			jo.addProperty("platform", "Remote Access Only");
			//jo.addProperty("path", "");
			//jo.addProperty("protocol", "mariadb");
			/*jo.addProperty("credential_id", "15395387796667219207");
			jo.addProperty("proxy_id", "4438297822689142147");
			jo.addProperty("comments", "");*/

			String data = jo.toString();
			logger.info("data : " + data);
			// DB Credentials 목록
			httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + api_ver + "/targets", "POST", data);
			int resultCode = Integer.parseInt(httpsResponse.get("HttpsResponseCode").toString());
			map.put("resultCode", resultCode);

			logger.info("resultCode : " + resultCode);

			// 프록시, DB 내용 추가
			if (resultCode == 201) {
				
			    JsonObject targets = gson.fromJson(httpsResponse.get("HttpsResponseDataMessage").toString(), JsonObject.class); 
			    		
			    List<Map<String, Object>> proxylist = searchDao.selectProxy();
			    map.put("target_id", targets.get("id").getAsString());
			    map.put("PROXY", proxylist);

			    List<Map<String, Object>> crelist = new ArrayList<>();

			    // DB Credentials 목록
			    httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + api_ver + "/credentials", "GET", null);
			    JsonArray jsonArray =  gson.fromJson(httpsResponse.get("HttpsResponseData").toString(), JsonArray.class); 

			    for (int i = 0; i < jsonArray.size(); i++) {
			        JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
			        Map<String, Object> serverMap = new HashMap<>();

			        serverMap.put("KEY", jsonObject.get("id").getAsString());
			        serverMap.put("LABEL", jsonObject.get("label").getAsString());
			        serverMap.put("ACCOUNT", jsonObject.get("username").getAsString());
			        serverMap.put("TYPE", jsonObject.get("type").getAsString());
//			        serverMap.put("CERT", jsonObject.get("cert").getAsString());

			        crelist.add(serverMap);
			    }

			    map.put("ACCOUNT", crelist);
			}
		} catch(Exception e) {
			logger.error(e.toString());
		}
		
		return map;
		
	}
	
	@Override
	public Map<String, Object> addTarget(HttpServletRequest request) throws Exception {
		logger.info("DB Target Add Service");
		int ap_no = 0;
		
		ReconUtil reconUtil = new ReconUtil();
		Map<String, Object> httpsResponse = null;
		Map<String, Object> map = new HashMap<>();
		
		try {
			String target_id = request.getParameter("target_id");
			String name = request.getParameter("name");
			String port = request.getParameter("port");
			String key = request.getParameter("key");
			String type = request.getParameter("type");
			String account = request.getParameter("account");
			String proxy = request.getParameter("proxy");

			Properties properties = new Properties();
			String resource = "/property/config.properties";
			Reader reader = Resources.getResourceAsReader(resource);

			properties.load(reader);

			String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no + 1));
			String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no + 1));
			String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no + 1));

			// probing 체크
			JsonObject probingJO = new JsonObject();

			probingJO.addProperty("target_name", name);
			probingJO.addProperty("path", key + ":" + port);
			probingJO.addProperty("protocol", type);
			probingJO.addProperty("credential_id", account);
			probingJO.addProperty("proxy_id", proxy);
			probingJO.addProperty("platform", "Remote Access Only");
			String data = probingJO.toString();
			httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + api_ver + "/targets/probing", "POST", data);
			int probingResultCode = Integer.parseInt(httpsResponse.get("HttpsResponseCode").toString());

			// Probing 성공일 경우
			if (probingResultCode == 200) {
			    // 서버 Location 생성
			    JsonObject locationJO = new JsonObject();
			    locationJO.addProperty("path", key + ":" + port);
			    locationJO.addProperty("protocol", type);
			    locationJO.addProperty("credential_id", account);
			    locationJO.addProperty("proxy_id", proxy);

			    data = locationJO.toString();
			    // DB Credentials 목록
			    httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + api_ver + "/targets/" + target_id + "/locations", "POST", data);
			    int resultCode = Integer.parseInt(httpsResponse.get("HttpsResponseCode").toString());
			    map.put("resultCode", resultCode);

			    logger.info("resultCode : " + resultCode);

			    if (resultCode == 201) {
			        try {
			            JsonObject targets = new Gson().fromJson(httpsResponse.get("HttpsResponseDataMessage").toString(), JsonObject.class);
			            String group_id = searchDao.selectDefaultGroup();
			            // Target 테이블 추가
			            Map<String, Object> targetMap = new HashMap<>();
			            targetMap.put("group_id", group_id);
			            targetMap.put("target_id", target_id);
			            targetMap.put("name", name);
			            targetMap.put("platform", "Remote Access Only");

			            searchDao.addTarget(targetMap);

			            // Location 테이블 추가
			            String location_id = targets.get("id").getAsString();

			            httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + api_ver + "/targets/" + target_id + "/locations/" + location_id, "GET", null);
			            JsonObject jsonObj = new Gson().fromJson(httpsResponse.get("HttpsResponseData").toString(), JsonObject.class);
			            map.put("locaionCode", httpsResponse.get("HttpsResponseCode").toString());

			            logger.info("Data :: " + jsonObj.toString());

			            String description = jsonObj.get("description").getAsString();
			            String locations_path = jsonObj.get("path").getAsString();
			            String credential_id = jsonObj.get("credential_id").getAsString();

			            logger.info("description :: " + description);
			            Map<String, Object> locationMap = new HashMap<>();

			            locationMap.put("group_id", group_id);
			            locationMap.put("target_id", target_id);
			            locationMap.put("location_id", location_id);
			            locationMap.put("proxy_id", proxy);
			            locationMap.put("credential_id", credential_id);
			            locationMap.put("path", locations_path);
			            locationMap.put("protocol", type);
			            locationMap.put("description", description);

			            searchDao.addLocation(locationMap);
			        } catch (Exception e) {
			            logger.error(e.toString());
			            map.put("target_id", target_id);
			            searchDao.deleteTargets(map);
			        }
			    }
			} else {
			    map.put("resultCode", probingResultCode);
			    map.put("resultMsg", httpsResponse.get("HttpsResponseDataMessage").toString());
			}
			
			
		} catch(Exception e) {
			logger.error(e.toString());
		}
		
		return map;
		
	}
	
	@Override
	public Map<String, Object> dropTarget(HttpServletRequest request) throws Exception {
		logger.info("DB Target Add Service");
		int ap_no = 0;
		
		ReconUtil reconUtil = new ReconUtil();
		Map<String, Object> httpsResponse = null;
		Map<String, Object> map = new HashMap<>();
		
		
		try {
			String target_id = request.getParameter("target_id");
			
			Properties properties = new Properties();
			String resource = "/property/config.properties";
			Reader reader = Resources.getResourceAsReader(resource);
			
			properties.load(reader);
			
			String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no+1)) ;
			String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no+1)) ;
			String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no+1));
			
			// probing 체크
			JsonObject probingJO = new JsonObject();
			
			String data = probingJO.toString();
			httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/"+api_ver+"/targets/"+target_id, "DELETE", null);
			int probingResultCode = Integer.parseInt(httpsResponse.get("HttpsResponseCode").toString());
			
			// Probing 성공일 경우
			if(probingResultCode == 200) {
				
			} else {
				map.put("resultCode", probingResultCode);
				map.put("resultMsg", httpsResponse.get("HttpsResponseDataMessage").toString());
			}
			
			
		} catch(Exception e) {
			logger.error(e.toString());
		}
		
		return map;
		
	}
	
	@Override
	public Map<String, Object> selectDBStatus(HttpServletRequest request) throws Exception {
		String target_id = request.getParameter("target_id");
		Map<String, Object> map = new HashMap<>();
		
		map.put("target_id", target_id);
		
		return searchDao.selectDBStatus(map);
	}
	
	@SuppressWarnings("null")
	@Override
	public Map<String, Object> addDBStatus(HttpServletRequest request) throws Exception {
		
		logger.info("DB Status Add Service");
		int ap_no = 0;
		
		ReconUtil reconUtil = new ReconUtil();
		Map<String, Object> httpsResponse = null;
		Map<String, Object> map = new HashMap<>();
		
		try {
			String target_id = request.getParameter("target_id");
			String name = request.getParameter("name");
			String port = request.getParameter("port");
			String key = request.getParameter("key");
			String type = request.getParameter("type");
			String account = request.getParameter("account");
			String proxy = request.getParameter("proxy");
			String schema = request.getParameter("schema");
			String table = request.getParameter("table");
			String row_limit = request.getParameter("row_limit");
			
			Properties properties = new Properties();
			String resource = "/property/config.properties";
			Reader reader = Resources.getResourceAsReader(resource);
			
			properties.load(reader);
			
			String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no+1)) ;
			String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no+1)) ;
			String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no+1));
			
			JsonObject locationJO = new JsonObject();

			if (type.equals("oracle")) {
			    if (row_limit != null && !row_limit.equals("")) {
			        locationJO.addProperty("path", "(row_limit=" + row_limit + ")/" + schema + key + ":" + port + "/" + table);
			    } else {
			        locationJO.addProperty("path", schema + key + ":" + port + "/" + table);
			    }
			} else {
			    if (row_limit != null && !row_limit.equals("")) {
			        locationJO.addProperty("path", "(row_limit=" + row_limit + ")/" + key + schema + ":" + port + "/" + table);
			    } else {
			        locationJO.addProperty("path", key + schema + ":" + port + "/" + table);
			    }
			}

			locationJO.addProperty("protocol", type);
			locationJO.addProperty("credential_id", account);
			locationJO.addProperty("proxy_id", proxy);

			String data = locationJO.toString();
			// DB Credentials 목록
			httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + api_ver + "/targets/" + target_id + "/locations", "POST", data);
			int resultCode = Integer.parseInt(httpsResponse.get("HttpsResponseCode").toString());
			map.put("resultCode", resultCode);

			logger.info("resultCode : " + resultCode);

			if (resultCode == 201) {
			    try {
			        JsonObject targets = new Gson().fromJson(httpsResponse.get("HttpsResponseDataMessage").toString(), JsonObject.class);

			        String group_id = searchDao.selectDefaultGroup();

			        // Location 테이블 추가
			        String location_id = targets.get("id").getAsString();

			        httpsResponse = reconUtil.getServerData(recon_id, recon_password, recon_url + "/" + api_ver + "/targets/" + target_id + "/locations/" + location_id, "GET", null);
			        JsonObject jsonObj = new Gson().fromJson(httpsResponse.get("HttpsResponseData").toString(), JsonObject.class);
			        map.put("locaionCode", httpsResponse.get("HttpsResponseCode").toString());

			        logger.info("Data :: " + jsonObj.toString());

			        String description = jsonObj.get("description").getAsString();
			        String locations_path = jsonObj.get("path").getAsString();
			        String credential_id = jsonObj.get("credential_id").getAsString();

			        logger.info("description :: " + description);
			        Map<String, Object> locationMap = new HashMap<>();

			        locationMap.put("group_id", group_id);
			        locationMap.put("target_id", target_id);
			        locationMap.put("location_id", location_id);
			        locationMap.put("proxy_id", proxy);
			        locationMap.put("credential_id", credential_id);
			        locationMap.put("path", locations_path);
			        locationMap.put("protocol", type);
			        locationMap.put("description", description);

			        searchDao.addLocation(locationMap);
			    } catch (Exception e) {
			        logger.error(e.toString());
			        map.put("target_id", target_id);
			        searchDao.deleteTargets(map);
			    }
			}
			
		} catch (Exception e) {
			logger.error(e.toString());
		}
		
		return map;
		
	}

	@Override
	public List<Map<String, Object>> registDaySchedule(HttpServletRequest request) throws Exception {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		
		Map<String, Object> map = new HashMap<>();
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String title = request.getParameter("title");
		String host = request.getParameter("host");
		String path = request.getParameter("path");
		String status_flag = request.getParameter("status_flag");
		
		if(path!= null) path = path.replaceAll("\\\\", "\\\\\\\\");
		
		List<Integer> patternList = detectiondao.queryCustomDataTypesCnt(); 
		map.put("patternList", patternList); 
		
		map.put("fromDate", fromDate);
		map.put("toDate", toDate);
		map.put("title", title);
		map.put("host", host);
		map.put("path", path);
		map.put("status_flag", status_flag);
		map.put("user_no", user_no);
		map.put("user_grade", user_grade);
		List<Map<String, Object>> resultList = null;
		//List<Map<String, Object>> resultList =  searchDao.selectScheduleGroup(map);
		
		
		resultList = searchDao.selectDaySchedule(map);
		
		for(int i=0 ;i<resultList.size() ; i++) {
			Map<String, Object> resultMap = resultList.get(i);
			resultMap.put("SCHEDULE_GROUP_NAME", set_service.replaceParameter((String)resultMap.get("SCHEDULE_GROUP_NAME")));
			resultMap.put("POLICY_NAME", set_service.replaceParameter((String)resultMap.get("POLICY_NAME")));
		
			resultList.set(i, resultMap);
			
			
		}
		
		return resultList;
	}

	@Override
	public Map<String, Object> monthGrpahData(HttpServletRequest request) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<>();
		 
		try {
			String toDate = request.getParameter("toDate");
			String fromDate = request.getParameter("fromDate");
			
			Map<String, Object> map = new HashMap<>();
			map.put("toDate", toDate);
			map.put("fromDate", fromDate);
			
			List<Map<String, Object>> searchList = searchDao.monthGrpahData(map);
			
			resultMap.put("resultCode", 0);
			resultMap.put("resultData", searchList);
			resultMap.put("resultMessage", "SUCCESS");
			
		} catch (Exception e) {
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "ERROR");
			logger.error("error ::: " + e);
		} 
		return resultMap;
	}
	
	@Override
	public Map<String, Object> serverGrpahData(HttpServletRequest request) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			
			List<String> targetList = new ArrayList<>();
			String toDate = request.getParameter("toDate");
			String fromDate = request.getParameter("fromDate");
			
			Map<String, Object> map = new HashMap<>();
			map.put("toDate", toDate);
			map.put("fromDate", fromDate);
			
			List<Map<String, Object>> searchList = searchDao.serverGrpahData(map);
			
			resultMap.put("resultCode", 0);
			resultMap.put("resultData", searchList);
			resultMap.put("resultMessage", "SUCCESS");
			
		} catch (Exception e) {
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "ERROR");
			logger.error("error ::: " + e);
			e.printStackTrace();
		} 
		return resultMap;
	}
	
	@Override
	public Map<String, Object> serverGrpahData2(HttpServletRequest request) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			
			List<String> targetList = new ArrayList<>();
			String toDate = request.getParameter("toDate");
			String fromDate = request.getParameter("fromDate");
			String target_id = (String) request.getParameter("target_id");
			
			Map<String, Object> map = new HashMap<>();
			map.put("toDate", toDate);
			map.put("fromDate", fromDate);
			
			logger.info("target_id ::: " + target_id);
			
			if(request.getParameter("target_id") != null && !target_id.trim().isEmpty()) {
				
				JsonParser targetPar = new JsonParser();
				JsonArray targetJArr = targetPar.parse(target_id).getAsJsonArray();
				
				for (JsonElement ele : targetJArr) {
					targetList.add(ele.getAsString());
				}
				
				map.put("target_id", targetList);
			}
			
			List<Map<String, Object>> searchList = searchDao.serverGrpahData(map);
			
			resultMap.put("resultCode", 0);
			resultMap.put("resultData", searchList);
			resultMap.put("resultMessage", "SUCCESS");
			
		} catch (Exception e) {
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "ERROR");
			logger.error("error ::: " + e);
			e.printStackTrace();
		} 
		return resultMap;
	}
	
	@Override
	public Map<String, Object> groupGrpahData(HttpServletRequest request) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			String toDate = request.getParameter("toDate");
			String fromDate = request.getParameter("fromDate");
			String group_id = request.getParameter("group_id");
			String target_id = request.getParameter("target_id");
			
			Map<String, Object> map = new HashMap<>();
			map.put("toDate", toDate);
			map.put("fromDate", fromDate);
			map.put("group_id", group_id);
			
			List<Map<String, Object>> searchList = searchDao.groupGrpahData(map);
			
			resultMap.put("resultCode", 0);
			resultMap.put("resultData", searchList);
			resultMap.put("resultMessage", "SUCCESS");
			
		} catch (Exception e) {
			resultMap.put("resultCode", -1);
			resultMap.put("resultMessage", "ERROR");
			logger.error("error ::: " + e);
		} 
		return resultMap;
	}

	
	
	
}
