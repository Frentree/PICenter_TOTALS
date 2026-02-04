 package com.org.iopts.service;

  import java.io.Reader;
  import java.util.ArrayList;
  import java.util.Date;
  import java.util.HashMap;
  import java.util.List;
  import java.util.Map;
  import java.util.Properties;

  import org.apache.ibatis.io.Resources;
  import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;
  import org.springframework.stereotype.Service;
  import com.google.gson.Gson;
  import com.google.gson.JsonArray;
  import com.google.gson.JsonElement;
  import com.google.gson.JsonObject;
  import com.google.gson.JsonParser;

  import com.org.iopts.dao.Pi_DashDAO;
  import com.org.iopts.dto.Pi_AgentVO;
  import com.org.iopts.exception.dao.piDetectionListDAO;
  import com.org.iopts.util.DecryptingPropertyPlaceholderConfigurer;
  import com.org.iopts.util.ReconUtil;
  import com.org.iopts.util.SessionUtil;

  import jakarta.inject.Inject;
  import jakarta.servlet.http.HttpServletRequest;

  @Service
  public class Pi_DashServiceImpl implements Pi_DashService {
        private static Logger logger = LoggerFactory.getLogger(Pi_DashServiceImpl.class);
        @Inject
        private Pi_DashDAO dao;

        @Inject
        private piDetectionListDAO detectionDao;

        @Inject
        private Pi_TargetService targetservice;

        @Override
        public List<Pi_AgentVO> selectDashMenu() throws Exception {
                String user_id = SessionUtil.getSession("memberSession", "USER_NO");
                logger.info("user_id=============================" + user_id);
                return dao.selectDashMenu(user_id);
        }

        @Override
        public Map<String, Object> selectDashInfo(HttpServletRequest request, String api_ver) throws Exception {
                int ap_no;
                try {
                    String apNoStr = request.getParameter("ap_no");
                    if (apNoStr == null || apNoStr.trim().isEmpty()) {
                        ap_no = 0;
                    } else {
                        ap_no = Integer.parseInt(apNoStr);
                        if (ap_no < 0 || ap_no >= Integer.MAX_VALUE) {
                            ap_no = 0;
                        }
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid ap_no format: " + request.getParameter("ap_no"));
                    ap_no = 0;
                }

                String target_id = request.getParameter("target_id");

                logger.info("target_id :: " + target_id);
                logger.info("ap_no :: " + ap_no);
                if(target_id == null) {
                        target_id = "";
                }
                Map<String, Object> map = new HashMap<>();
                map.put("target_id", target_id);
                map.put("ap_no", ap_no);

                Map<String, Object> returnMap = dao.selectDashInfo(map);

                ReconUtil reconUtil = new ReconUtil();
                Map<String, Object> httpsResponse = null;

                Properties properties = new Properties();
                String resource = "/property/config.properties";
                Reader reader = Resources.getResourceAsReader(resource);

                properties.load(reader);

                String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no+1)) ;
                String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no+1)) ;
                String recon_password_enc = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no+1)) ;

                try {
                        logger.info("ap_no :: " + ap_no);
                        logger.info("recon_url :: " + recon_url);
                        logger.info("recon_id :: " + recon_id);
                        logger.info("recon_password :: " + recon_password_enc);
                        httpsResponse = reconUtil.getServerData(recon_id, DecryptingPropertyPlaceholderConfigurer.decryptValue(recon_password_enc), recon_url + "/" + api_ver + "/targets/" +
  target_id + "/isolated", "GET", null);
                        String jsonStr = httpsResponse.get("HttpsResponseData").toString();
                        JsonArray jsonArray = JsonParser.parseString(jsonStr).getAsJsonArray();
                        logger.info("getMatchObjects jsonObject : " + jsonArray);
                        Object[] arr = new Object[jsonArray.size()];
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonElement elem = jsonArray.get(i);
                            arr[i] = elem;
                        }
                        String lastDate = arr[0].toString();

                        long unixTime = Long.parseLong(lastDate) * 1000;
                    Date date = new Date(unixTime);
                    returnMap.put("REGDATE", date);
                }catch (NullPointerException e) {
                        logger.error("e :: "+e.toString());
                        returnMap.put("REGDATE", "-");
                }catch (Exception e) {
                        logger.error("e :: "+e.toString());
                }

            logger.info("returnMap chk : " + returnMap);
                return returnMap;
        }

        @Override
        public Map<String, Object> selectlastScanDate(HttpServletRequest request) {
                logger.info("selectlastScanDate");
                String target_id = request.getParameter("target_id");
                logger.info("target_id check : "+ target_id);
                return dao.selectlastScanDate(target_id);
        }

        @Override
        public Map<String,Object> selectDatatype(HttpServletRequest request) throws Exception {
                logger.info("Total DataType Graphe");

                Map<String, Object> map = new HashMap<>();

                String days = request.getParameter("days");
                String target_id = request.getParameter("target_id");

                logger.info("chk : " + days + " , " + target_id);

                Map<String, Object> type = new HashMap<String, Object>();
                List<Map<String, Object>> Data = null;
                List<Integer> patternList = null;
                int search_day = 0;
                int search_month = 0;

                try {
                        patternList = detectionDao.queryCustomDataTypesCnt();
                } catch (RuntimeException e) {
                        logger.error("Failed to query custom data types count", e);
                        patternList = null;
                }

                type.put("patternList", patternList);

                if("days".equals(days)) {
                        search_day = 7;
                        type.put("date", search_day);
                        Data = dao.selectDatatypeAll_day(type);
                } else if("month".equals(days)) {
                        search_day = 30;
                        type.put("date", search_day);
                        Data = dao.selectDatatypeAll_day(type);
                } else if("three_month".equals(days)) {
                        search_day = 90;
                        type.put("date", search_day);
                        Data = dao.selectDatatypeAll_day(type);
                } else if("six_month".equals(days)) {
                        search_month = 6;
                        type.put("date", search_month);
                        Data = dao.selectDatatypeAll(type);
                } else {
                        search_month = 12;
                        type.put("date", search_month);
                        Data = dao.selectDatatypeAll(type);
                }

                // DATA_TYPES JSON 파싱하여 패턴별 컬럼으로 변환
                Gson gson = new Gson();

                for (Map<String, Object> row : Data) {
                        String dataTypes = row.get("DATA_TYPES") != null ? row.get("DATA_TYPES").toString() : "";

                        Map<Integer, Integer> patternSumMap = new HashMap<>();

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

                        // patternSumMap의 각 항목을 "TYPE" + patternNum 형식으로 row에 추가
                        for (Map.Entry<Integer, Integer> entry : patternSumMap.entrySet()) {
                                row.put("TYPE" + entry.getKey(), entry.getValue());
                        }

                        // DATA_TYPES 컬럼 제거
                        row.remove("DATA_TYPES");
                }

                map.put("Data", Data);
                map.put("DataTypes", detectionDao.queryCustomDataTypes());

                return map;
        }


        @Override
        public Map<String,Object> selectDatatypeManager(HttpServletRequest request) throws Exception {
                logger.info("selectDatatypeManager");

                Map<String, Object> map = new HashMap<>();

                String days = request.getParameter("days");
                String target_id = request.getParameter("target_id");
                String ap_no = request.getParameter("ap_no");
                String user_no = SessionUtil.getSession("memberSession", "USER_NO");

                logger.info("chk : " + days + " , " + target_id + " , " + ap_no);

                Map<String, Object> type = new HashMap<String, Object>();
                List<Map<String, Object>> Data = null;
                int search_day = 0;
                int search_month = 0;

                List<Integer> patternList = null;

                try {
                        patternList = detectionDao.queryCustomDataTypesCnt();
                } catch(RuntimeException e) {
                        logger.error("Failed to query custom data types count", e);
                }
                type.put("patternList", patternList);

                if(target_id == null || target_id.equals("")) {

                        if("days".equals(days)) {
                                search_day = 7;
                                type.put("date", search_day);
                                type.put("user_no", user_no);
                                Data = dao.selectDatatypeAll_day(type);
                        } else if("month".equals(days)) {
                                search_day = 30;
                                type.put("date", search_day);
                                type.put("user_no", user_no);
                                Data = dao.selectDatatypeAll_day(type);
                        } else if("three_month".equals(days)) {
                                search_day = 90;
                                type.put("date", search_day);
                                type.put("user_no", user_no);
                                Data = dao.selectDatatypeAll_day(type);
                        } else if("six_month".equals(days)) {
                                search_month = 6;
                                type.put("date", search_month);
                                type.put("user_no", user_no);
                                Data = dao.selectDatatypeAll(type);
                        } else {
                                search_month = 12;
                                type.put("date", search_month);
                                type.put("user_no", user_no);
                                Data = dao.selectDatatypeAll(type);
                        }

                } else {
                        type.put("target_id", target_id);
                        type.put("ap_no", ap_no);
                        if("days".equals(days)) {
                                search_day = 7;
                                type.put("date", search_day);
                                Data = dao.selectDatatypeAll_day(type);
                        } else if("month".equals(days)) {
                                search_day = 30;
                                type.put("date", search_day);
                                Data = dao.selectDatatypeAll_day(type);
                        } else if("three_month".equals(days)) {
                                search_day = 90;
                                type.put("date", search_day);
                                Data = dao.selectDatatypeAll_day(type);
                        } else if("six_month".equals(days)) {
                                search_month = 6;
                                type.put("date", search_month);
                                Data = dao.selectDatatypeAll(type);
                        } else {
                                search_month = 12;
                                type.put("date", search_month);
                                Data = dao.selectDatatypeAll(type);
                        }
                }

                // DATA_TYPES JSON 파싱하여 패턴별 컬럼으로 변환
                Gson gson = new Gson();

                for (Map<String, Object> row : Data) {
                        String dataTypes = row.get("DATA_TYPES") != null ? row.get("DATA_TYPES").toString() : "";

                        Map<Integer, Integer> patternSumMap = new HashMap<>();

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

                        // patternSumMap의 각 항목을 "TYPE" + patternNum 형식으로 row에 추가
                        for (Map.Entry<Integer, Integer> entry : patternSumMap.entrySet()) {
                                row.put("TYPE" + entry.getKey(), entry.getValue());
                        }

                        // DATA_TYPES 컬럼 제거
                        row.remove("DATA_TYPES");
                }

                map.put("Data", Data);
                map.put("DataTypes", detectionDao.queryCustomDataTypes());

                return map;
        }

        @Override
        public Map<String, Object> selectDatatypes(HttpServletRequest request) throws Exception {
                List<String> datatype = new ArrayList<String>();
                datatype.add("RRN");
                datatype.add("FOREIGNER");
                datatype.add("DRIVER");
                datatype.add("PASSPORT");
                datatype.add("ACCOUNT_NUM");
                datatype.add("CARD_NUM");
                datatype.add("PHONE_NUM");
                datatype.add("MOBILE_PHONE");
                String user_id = SessionUtil.getSession("memberSession", "USER_NO");
                Map<String, Object> data = new HashMap<String, Object>();
                Map<String, Object> result = new HashMap<String, Object>();
                for(int i = 0; i < datatype.size(); i++) {
                        data.put("datatype", datatype.get(i));
                        data.put("user_id", user_id);
                        result.put(datatype.get(i), dao.selectDatatypes(data));
                }
                return result;
        }

        @Override
        public List<Object> selectSystemCurrent(HttpServletRequest request) {
                logger.info("selectSystemCurrent check");
                String id = request.getParameter("id");
                String fromDate = request.getParameter("fromDate");
                String toDate = request.getParameter("toDate");

                Map<String, Object> input = new HashMap<>();
                input.put("id", id);
                input.put("fromDate", fromDate);
                input.put("toDate", toDate);

                return dao.selectSystemCurrent(input);
        }

        @Override
        public List<Object> selectSystemCurrentPC(HttpServletRequest request) {
                logger.info("selectSystemCurrentPC check");
                String id = request.getParameter("id");
                String fromDate = request.getParameter("fromDate");
                String toDate = request.getParameter("toDate");

                Map<String, Object> input = new HashMap<>();
                input.put("id", id);
                input.put("fromDate", fromDate);
                input.put("toDate", toDate);

                return dao.selectSystemCurrentPC(input);
        }

        @Override
        public List<Object> selectSystemCurrentManager(HttpServletRequest request) {
                logger.info("selectSystemCurrentManager check");
                String id = request.getParameter("id");
                String user_no = request.getParameter("user_no");

                Map<String, Object> input = new HashMap<>();
                input.put("id", id);
                input.put("user_no", user_no);

                return dao.selectSystemCurrentManager(input);
        }

        @Override
        public List<Object> selectSystemCurrentService(HttpServletRequest request) {
                logger.info("selectSystemCurrentService check");
                String id = request.getParameter("id");
                String user_no = request.getParameter("user_no");

                Map<String, Object> input = new HashMap<>();
                input.put("id", id);
                input.put("user_no", user_no);

                return dao.selectSystemCurrentService(input);
        }

        @Override
        public List<Object> selectServerExcelDownload(HttpServletRequest request) {
                logger.info("selectExcelDownload check");
                String id = request.getParameter("id");
                String fromDate = request.getParameter("fromDate");
                String toDate = request.getParameter("toDate");

                Map<String, Object> input = new HashMap<>();
                input.put("id", id);
                input.put("fromDate", fromDate);
                input.put("toDate", toDate);

                return dao.selectServerExcelDownload(input);
        }

        @Override
        public List<Object> selectServerExcelDownloadList(HttpServletRequest request, List<String> targetList) throws Exception {

                String ap = request.getParameter("ap");
                String id = request.getParameter("id");
                String fromDate = request.getParameter("fromDate");
                String toDate = request.getParameter("toDate");

                Map<String, Object> input = new HashMap<>();

                logger.info(targetList.toString());

                input.put("targetList", targetList);
                input.put("ap", ap);
                input.put("id", id);
                input.put("fromDate", fromDate);
                input.put("toDate", toDate);

                logger.info("input >> " + input);

                return dao.selectServerExcelDownloadList(input);
        }

        @Override
        public List<Object> selectPCExcelDownload(HttpServletRequest request) {
                logger.info("selectPCExcelDownload check");
                String id = request.getParameter("id");
                String fromDate = request.getParameter("fromDate");
                String toDate = request.getParameter("toDate");

                Map<String, Object> input = new HashMap<>();
                input.put("id", id);
                input.put("fromDate", fromDate);
                input.put("toDate", toDate);

                return dao.selectPCExcelDownload(input);
        }

        @Override
        public List<Object> selectPCExcelDownloadList(HttpServletRequest request, List<String> targetList) throws Exception {

                String ap = request.getParameter("ap");
                String id = request.getParameter("id");
                String fromDate = request.getParameter("fromDate");
                String toDate = request.getParameter("toDate");

                Map<String, Object> input = new HashMap<>();

                logger.info(targetList.toString());

                input.put("targetList", targetList);
                input.put("ap", ap);
                input.put("id", id);
                input.put("fromDate", fromDate);
                input.put("toDate", toDate);

                logger.info("input >> " + input);

                return dao.selectPCExcelDownloadList(input);
        }

        @Override
        public List<Object> selectPathCurrent(HttpServletRequest request) {
                logger.info("selectPathCurrent check");
                return dao.selectPathCurrent();
        }

        @Override
        public List<Object> selectJumpUpHost(HttpServletRequest request) {
                logger.info("selectJumpUpHost check");
                return dao.selectJumpUpHost();
        }

        @Override
        public List<Object> selectNotAction_group(HttpServletRequest request) {
                logger.info("selectNotAction_group check");
                return null;
        }

        @Override
        public Map<String, Object> selectDashDataDetectionList(HttpServletRequest request) throws Exception {
                logger.info("selectDashDataDetectionList");

                String fromDate = request.getParameter("fromDate");
                String toDate = request.getParameter("toDate");

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("fromDate", fromDate);
                map.put("toDate", toDate);

                return dao.selectDashDataDetectionList(map);
        }

        @Override
        public Map<String, Object> selectDashDataDetectionServerList(HttpServletRequest request) throws Exception {
                logger.info("selectDashDataDetectionServerList");

                String fromDate = request.getParameter("fromDate");
                String toDate = request.getParameter("toDate");

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("fromDate", fromDate);
                map.put("toDate", toDate);

                return dao.selectDashDataDetectionServerList(map);
        }

        @Override
        public Map<String, Object> selectDashDataDetectionPCList(HttpServletRequest request) throws Exception {
                logger.info("selectDashDataDetectionPCList");

                String fromDate = request.getParameter("fromDate");
                String toDate = request.getParameter("toDate");

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("fromDate", fromDate);
                map.put("toDate", toDate);

                return dao.selectDashDataDetectionPCList(map);
        }

        @Override
        public Map<String, Object> selectDashDataCompleteList(HttpServletRequest request) throws Exception {
                logger.info("selectDashDataCompleteList");
                String target_id = request.getParameter("target_id");
                String id = request.getParameter("id");
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("target_id", target_id);
                map.put("id", id);
                return dao.selectDashDataCompleteList(map);
        }

        @Override
        public Map<String, Object> selectDashDataDetectionItemList(HttpServletRequest request) throws Exception {

                String ap_no = request.getParameter("ap_no");
                String target_id = request.getParameter("target_id");
                String id = request.getParameter("id");

                Map<String, Object> input = new HashMap<>();

                input.put("ap_no", ap_no);
                input.put("target_id", target_id);
                input.put("id", id);

                return dao.selectDashDataDetectionItemList(input);
        }

        @Override
        public Map<String, Object> selectDashPersonalServerDetectionItemList(HttpServletRequest request, List<String> targetList) throws Exception {

                String ap = request.getParameter("ap");
                String id = request.getParameter("id");

                Map<String, Object> input = new HashMap<>();

                logger.info(targetList.toString());

                input.put("targetList", targetList);
                input.put("ap", ap);
                input.put("id", id);

                logger.info("input >> " + input);

                return dao.selectDashPersonalServerDetectionItemList(input);
        }

        @Override
        public Map<String, Object> selectDashPersonalServerComplete(HttpServletRequest request, List<String> targetList) throws Exception {

                String ap = request.getParameter("ap");
                String id = request.getParameter("id");

                Map<String, Object> input = new HashMap<>();

                input.put("targetList", targetList);
                input.put("ap", ap);
                input.put("id", id);

                logger.info("input >> " + input);

                return dao.selectDashPersonalServerComplete(input);
        }

        @Override
        public List<Object> selectDashDataRank(HttpServletRequest request) {
                logger.info("selectDashDataRank check");

                String id = request.getParameter("id");
                String fromDate = request.getParameter("fromDate");
                String toDate = request.getParameter("toDate");

                Map<String, Object> input = new HashMap<>();
                input.put("id", id);
                input.put("fromDate", fromDate);
                input.put("toDate", toDate);

                return dao.selectDashDataRank(input);
        }

        @Override
        public List<Map<String, Object>> selectDashPersonalServerRank(HttpServletRequest request, List<String> targetList) {

                String id = request.getParameter("id");

                Map<String, Object> input = new HashMap<>();

                String targetArr = (String) request.getParameter("targetList");

                JsonParser targetPar = new JsonParser();
                JsonArray targetJArr = targetPar.parse(targetArr).getAsJsonArray();

                for (JsonElement ele : targetJArr) {
                        targetList.add(ele.getAsString());
                }

                logger.info(targetList.toString());

                input.put("targetList", targetList);
                input.put("id", id);

                logger.info("inpur >> " +  input);

                return dao.selectDashPersonalServerRank(input);
        }

        @Override
        public List<Map<String, Object>> selectDashPersonalPCRank(HttpServletRequest request, List<String> targetList) {

                String id = request.getParameter("id");

                Map<String, Object> input = new HashMap<>();

                String targetArr = (String) request.getParameter("targetList");

                JsonParser targetPar = new JsonParser();
                JsonArray targetJArr = targetPar.parse(targetArr).getAsJsonArray();

                for (JsonElement ele : targetJArr) {
                        targetList.add(ele.getAsString());
                }

                logger.info(targetList.toString());

                input.put("targetList", targetList);
                input.put("id", id);

                logger.info("inpur >> " +  input);

                return dao.selectDashPersonalPCRank(input);
        }

        @Override
        public List<Object> selectDashPersonalManagerRank(HttpServletRequest request) {
                logger.info("selectDashPersonalManagerRank check");

                String user_no = SessionUtil.getSession("memberSession", "USER_NO");

                Map<String, Object> input = new HashMap<>();
                input.put("user_no", user_no);

                return dao.selectDashPersonalManagerRank(input);
        }

        @Override
        public List<Object> selectDashDataImple(HttpServletRequest request) {
                logger.info("selectDashDataImple check");
                return dao.selectDashDataImple();
        }

        @Override
        public List<Object> selectDashDataImpleManager(HttpServletRequest request) {
                logger.info("selectDashDataImpleManager check");
                String user_no = SessionUtil.getSession("memberSession", "USER_NO");

                Map<String, Object> input = new HashMap<>();
                input.put("user_no", user_no);
                return dao.selectDashDataImpleManager(input);
        }

        @Override
        public List<Map<String, Object>> selectDashPersonalServerImple(HttpServletRequest request, List<String> targetList) throws Exception {

                String ap = request.getParameter("ap");
                String id = request.getParameter("id");

                Map<String, Object> input = new HashMap<>();

                String targetArr = (String) request.getParameter("targetList");

                JsonParser targetPar = new JsonParser();
                JsonArray targetJArr = targetPar.parse(targetArr).getAsJsonArray();

                for (JsonElement ele : targetJArr) {
                        targetList.add(ele.getAsString());
                }

                input.put("targetList", targetList);
                input.put("ap", ap);
                input.put("id", id);

                return dao.selectDashPersonalServerImple(input);

        }

        @Override
        public List<Map<String, Object>> selectDashDataPersonalServer(HttpServletRequest request) throws Exception {

                String fromDate = request.getParameter("fromDate");
                String toDate = request.getParameter("toDate");

                Map<String, Object> input = new HashMap<>();

                input.put("fromDate", fromDate);
                input.put("toDate", toDate);

                return dao.selectDashDataPersonalServer(input);

        }

        @Override
        public List<Map<String, Object>> selectDashDataPersonalPC(HttpServletRequest request) throws Exception {

                String fromDate = request.getParameter("fromDate");
                String toDate = request.getParameter("toDate");

                Map<String, Object> input = new HashMap<>();

                input.put("fromDate", fromDate);
                input.put("toDate", toDate);

                return dao.selectDashDataPersonalPC(input);

        }

        @Override
        public Map<String, Object> selectDashDataPersonalServerCount(HttpServletRequest request) throws Exception {
                logger.info("selectDatatype");
                Map<String, Object> map = new HashMap<String, Object>();
                return dao.selectDashDataPersonalServerCount(map);
        }

        @Override
        public List<Map<String, Object>> selectDashDataPersonalServerCircle(HttpServletRequest request, List<String> targetList) {
                String id = request.getParameter("id");
                String type = request.getParameter("type");
                String ap = request.getParameter("ap");

                Map<String, Object> input = new HashMap<>();

                input.put("targetList", targetList);
                input.put("id", id);
                input.put("type", type);
                input.put("ap", ap);

                logger.info("input >> " + input);

                return dao.selectDashDataPersonalServerCircle(input);
        }

        @Override
        public List<Map<String, Object>> selectDashDataPersonalPCCircle(HttpServletRequest request, List<String> targetList) {
                String id = request.getParameter("id");
                String type = request.getParameter("type");
                String ap = request.getParameter("ap");
                String fromDate = request.getParameter("fromDate");
                String toDate = request.getParameter("toDate");

                Map<String, Object> input = new HashMap<>();

                input.put("targetList", targetList);
                input.put("id", id);
                input.put("type", type);
                input.put("ap", ap);
                input.put("fromDate", fromDate);
                input.put("toDate", toDate);

                logger.info("input >> " + input);

                return dao.selectDashDataPersonalPCCircle(input);
        }

        @Override
        public Map<String, Object> selectDashDataTodoList(HttpServletRequest request) throws Exception {
                String user_no = request.getParameter("user_no");

                Map<String, Object> input = new HashMap<>();

                input.put("user_no", user_no);
                return dao.selectDashDataTodoList(input);
        }

        @Override
        public Map<String, Object> selectDashDataTodoApproval(HttpServletRequest request) throws Exception {
                String user_no = request.getParameter("user_no");

                Map<String, Object> input = new HashMap<>();

                input.put("user_no", user_no);
                return dao.selectDashDataTodoApproval(input);
        }

        @Override
        public Map<String, Object> selectDashDataTodoSchedule(HttpServletRequest request) throws Exception {
                String user_no = request.getParameter("user_no");
                String fromDate = request.getParameter("fromDate");
                String toDate = request.getParameter("toDate");

                Map<String, Object> input = new HashMap<String, Object>();

                input.put("user_no", user_no);
                input.put("fromDate", fromDate);
                input.put("toDate", toDate);
                return dao.selectDashDataTodoSchedule(input);
        }

        @Override
        public List<Object> selectDashPCJstreePopup(HttpServletRequest request) throws Exception {

                String target_id = request.getParameter("target_id");
                String id = request.getParameter("id");
                Map<String, Object> input = new HashMap<>();

                input.put("target_id", target_id);
                input.put("id", id);
                return dao.selectDashPCJstreePopup(input);
        }

        @Override
        public List<Object> selectSystemCurrentProgressPC(HttpServletRequest request) {
                logger.info("selectSystemCurrentProgressPC check");
                String id = request.getParameter("id");
                String fromDate = request.getParameter("fromDate");
                String toDate = request.getParameter("toDate");

                Map<String, Object> input = new HashMap<>();
                input.put("id", id);
                input.put("fromDate", fromDate);
                input.put("toDate", toDate);

                return dao.selectSystemCurrentProgressPC(input);
        }

        @Override
        public List<Object> selectSystemCurrentProgressOneDrive(HttpServletRequest request) {
                logger.info("selectSystemCurrentProgressOneDrive check");
                String id = request.getParameter("id");
                String fromDate = request.getParameter("fromDate");
                String toDate = request.getParameter("toDate");

                Map<String, Object> input = new HashMap<>();
                input.put("id", id);
                input.put("fromDate", fromDate);
                input.put("toDate", toDate);

                return dao.selectSystemCurrentProgressOneDrive(input);
        }

        @Override
        public List<Map<String, Object>> selectDashPersonalProgressPc(HttpServletRequest request, List<String> targetList) {
                String id = request.getParameter("id");
                String type = request.getParameter("type");
                String ap = request.getParameter("ap");
                String fromDate = request.getParameter("fromDate");
                String toDate = request.getParameter("toDate");

                Map<String, Object> input = new HashMap<>();

                input.put("targetList", targetList);
                input.put("id", id);
                input.put("type", type);
                input.put("ap", ap);
                input.put("fromDate", fromDate);
                input.put("toDate", toDate);

                logger.info("input >> " + input);

                return dao.selectDashPersonalProgressPc(input);
        }

        @Override
        public List<Map<String, Object>> selectDashPersonalProgressOneDrive(HttpServletRequest request, List<String> targetList) {
                String id = request.getParameter("id");
                String type = request.getParameter("type");
                String ap = request.getParameter("ap");
                String date = request.getParameter("date");
                String fromDate = request.getParameter("fromDate");
                String toDate = request.getParameter("toDate");

                Map<String, Object> input = new HashMap<>();

                input.put("targetList", targetList);
                input.put("id", id);
                input.put("type", type);
                input.put("ap", ap);
                input.put("date", date);
                input.put("fromDate", fromDate);
                input.put("toDate", toDate);

                logger.info("input >> " + input);

                return dao.selectDashPersonalProgressOneDrive(input);
        }

  }