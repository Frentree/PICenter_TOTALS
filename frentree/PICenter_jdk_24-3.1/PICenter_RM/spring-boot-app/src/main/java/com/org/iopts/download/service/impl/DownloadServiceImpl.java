package com.org.iopts.download.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.iopts.download.dao.DownloadDAO;
import com.org.iopts.download.service.DownloadService;
import com.org.iopts.util.SessionUtil;

@Service
@Transactional
public class DownloadServiceImpl implements DownloadService{

	private static Logger logger = LoggerFactory.getLogger(DownloadServiceImpl.class);

	@Value("${recon.id}")
	private String recon_id;

	@Value("${recon.password}")
	private String recon_password;

	@Value("${recon.url}")
	private String recon_url;
	
	@Value("${recon.api.version}")
	private String api_ver;
	
	@Inject
	private DownloadDAO dao;


	@Override
	public int selectDownloadIndex(Map<String, Object> map, HttpServletRequest request) throws Exception {
		logger.info("selectDownloadIndex");
		
		return dao.selectDownloadIndex();
	}
	
	@Override
	public int insertDownload(Map<String, Object> map, HttpServletRequest request) throws Exception {
		logger.info("insertDownload");
		String userNo = SessionUtil.getSession("memberSession", "USER_NO");
		map.put("user_no", userNo);
		
		dao.insertDownload(map);
		int result = -1;
		try {
			result = Integer.parseInt(map.get("NOTICE_FILE_ID").toString());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		
		return result;
	}
	
	@Override
	public int selectFileDownloadIndex(Map<String, Object> map, HttpServletRequest request) throws Exception {
		logger.info("selectFileDownloadIndex");
		
		return dao.selectFileDownloadIndex();
	}
	
	@Override
	public int insertFileDownload(Map<String, Object> map, HttpServletRequest request) throws Exception {
		logger.info("insertFileDownload");
		String userNo = SessionUtil.getSession("memberSession", "USER_NO");
		map.put("user_no", userNo);
		
		dao.insertFileDownload(map);
		int result = -1;
		try {
			result = Integer.parseInt(map.get("DOWNLOAD_FILE_ID").toString());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		
		return result;
	}
	
	@Override
	public Map<String, Object> downLoadFileInformation(HttpServletRequest request, HashMap<String, Object> params) throws Exception {
		Map<String, Object> downMap = new HashMap<>();
		
		int host_cnt = 0;
		  
		int selectDownloadIndex =  dao.selectExcelDownloadIndex();
		String report_flag =  "xlsx";   
		
    	downMap.put("user_no", SessionUtil.getSession("memberSession", "USER_NO"));
        downMap.put("file_name",  params.get("filename"));
        downMap.put("page_name",  params.get("page_name"));
        downMap.put("url",  params.get("url"));
        downMap.put("host_cnt",  host_cnt);
    	downMap.put("real_file_name", selectDownloadIndex+"."+report_flag );
    	
    	dao.insertFileDownLoadStatus(downMap); // 다운로드 대기열 데이터 추가
    	
		return downMap;
	}

	@Value("${config.excel.time}")
	private String time;
	
	@Override
	public List<Map<String, Object>> downloadList(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String userNo = SessionUtil.getSession("memberSession", "USER_NO");
		map.put("user_no", userNo);
		map.put("time", time);
		
		List<Map<String, Object>> resultMap = new ArrayList<Map<String,Object>>();
		
		try {
			resultMap = dao.downloadList(map);
		} catch (Exception e) {
			logger.error("error :: " + e);
			e.printStackTrace();
		}
		
		return resultMap;
	}
	
	

	
}
