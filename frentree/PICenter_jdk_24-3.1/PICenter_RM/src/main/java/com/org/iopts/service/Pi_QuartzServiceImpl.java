package com.org.iopts.service;

import java.io.IOException;
import java.io.Reader;
import java.net.ProtocolException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.org.iopts.dao.Pi_QuartzDAO;
import com.org.iopts.util.ReconUtil;

@Service
public class Pi_QuartzServiceImpl implements Pi_QuartzService {

	private static Logger logger = LoggerFactory.getLogger(Pi_ExceptionServiceImpl.class);

	// schedule resume pause 기능은 정시 마다 실행 
	@Override
	public void executeScanSchedule() {
		Pi_QuartzDAO dao = new Pi_QuartzDAO();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String scan_dtm = sdf.format(date);
		logger.info("[" + scan_dtm + "] executeScanSchedule START...");
		
		Map<String, String> map = new HashMap<String, String>();
		
		map = new HashMap<String, String>();
		
		List<Map<String, String>> stopSchList = dao.getChangeScheduleList(map);
		logger.info("[" + sdf.format(date) + "] stopSchList size : " + stopSchList.size());
		
		for(int i=0; i<stopSchList.size(); i++) {
			Map<String, String> listMap = stopSchList.get(i);
			
			String schedule_id = listMap.get("RECON_ID");
			String work_cd = listMap.get("FLAG");
			String ap_no = listMap.get("AP_NO");
			
//			01 시작 , 02 정지
			String action = "";
			if("01".equals(work_cd)) {
				action = "resume";
			}else if("02".equals(work_cd)) {
				action = "pause";
			}
			
			if(!"".equals(action)) {
				changeScanSchedule(scan_dtm, schedule_id, action, dao, Integer.parseInt(ap_no));
			}
			
		}
		logger.info("[" + scan_dtm + "] executeScanSchedule END...");
	}

	// schedule stop 기능은 1분당 실행
	@Override
	public void executeStopSchedule() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String stop_dtm = sdf.format(date);
		logger.info("[" + stop_dtm + "] Session check ...");
		
	}

	private void changeScanSchedule(String dtm, String schedule_id, String action, Pi_QuartzDAO dao, int ap_no) {
		try {
			
			ReconUtil reconUtil = new ReconUtil();
			Properties properties = new Properties();
			String resource = "/property/config.properties";
			Reader reader = Resources.getResourceAsReader(resource);
			
			properties.load(reader);
			
			String recon_url = (ap_no == 0) ? properties.getProperty("recon.url") : properties.getProperty("recon.url_" + (ap_no+1)) ;
			String recon_id = (ap_no == 0) ? properties.getProperty("recon.id") : properties.getProperty("recon.id_" + (ap_no+1)) ;
			String recon_password = (ap_no == 0) ? properties.getProperty("recon.password") : properties.getProperty("recon.password_" + (ap_no+1)) ;
			
			String url = recon_url + "/beta/schedules/" + schedule_id + "/" + action;
			String method = "POST";
			String requestData = "";
			Map<String, String> conHistMap = new HashMap<>();
			String seq = String.format("%03d", dao.getConnectHistSeq());
			logger.info("dtm : " + dtm);
			logger.info("seq : " + seq);
			
			conHistMap.put("id", dtm+seq);
			conHistMap.put("recon_id", recon_id);
			conHistMap.put("url", url);
			conHistMap.put("method", method);
			conHistMap.put("req_data", requestData);
			
			if(dao.insConnectHist(conHistMap) > 0) {
				Map<String, Object> resultMap = reconUtil.getServerData(recon_id, recon_password, url, method, requestData);
				conHistMap.put("rsp_cd", resultMap.get("HttpsResponseCode").toString());
				conHistMap.put("rsp_msg", resultMap.get("HttpsResponseMessage").toString());
				
				logger.info("[" + schedule_id + "] "+ action +"UPDATE DB Schedule result : " + dao.uptConnectHist(conHistMap));
				
				logger.info("[" + schedule_id + "] "+ action +"Schedule result : " + resultMap.get("HttpsResponseCode"));
				logger.info("[" + schedule_id + "] "+ action +"Schedule result : " + resultMap.get("HttpsResponseMessage"));
				
			};
			
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
