package com.org.iopts.popup.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.iopts.popup.dao.PopupDAO;
import com.org.iopts.popup.service.PopupService;

import jakarta.inject.Inject;

@Service
@Transactional
public class PopupServiceImpl implements PopupService {

	private static Logger log = LoggerFactory.getLogger(PopupServiceImpl.class);
	
	@Inject
	private PopupDAO dao;

	@Override
	public List<Map<String, Object>> selectGroupList(Map<String, Object> map) throws Exception {
		return dao.selectGroupList(map);
	}

	@Override
	public List<Map<String, Object>> selectNoGroupList() throws Exception {
		return dao.selectNoGroupList();
	}

	@Override
	public List<Map<String, Object>> getTargetList(Map<String, Object> map) throws Exception {
		return dao.getTargetList(map);
	}

}
