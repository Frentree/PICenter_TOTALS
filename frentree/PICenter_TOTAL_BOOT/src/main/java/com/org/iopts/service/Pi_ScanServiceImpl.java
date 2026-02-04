package com.org.iopts.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.org.iopts.dao.Pi_ScanDAO;
import com.org.iopts.dao.Pi_UserDAO;
import com.org.iopts.dto.Pi_Scan_HostVO;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class Pi_ScanServiceImpl implements Pi_ScanService {
	
	private static Logger logger = LoggerFactory.getLogger(Pi_ScanServiceImpl.class);
	@Inject
	private Pi_ScanDAO dao;
	
	@Inject
	private Pi_UserDAO userDao;


	@Override
	public List<Pi_Scan_HostVO> selectScanHost() throws Exception {
		// TODO Auto-generated method stub
		return dao.selectScanHost();
	}
	

	@Override
	public List<Map<String, Object>> getApList(HttpServletRequest request) throws Exception {
		List<Map<String, Object>> resultList = new ArrayList<>();
		
		resultList = dao.getApList();
		
		return resultList;
	}

}
