package com.org.iopts.exception.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.iopts.exception.dao.piManageSearchListDAO;
import com.org.iopts.exception.service.piManageSearchListService;
import com.org.iopts.util.SessionUtil;

import jakarta.inject.Inject;

@Service
@Transactional
public class piManageSearchListServiceImple implements piManageSearchListService {
	
	@Inject
	private piManageSearchListDAO dao;

	@Override
	public List<HashMap<String, Object>> searchProcessList(HashMap<String, Object> params) throws Exception {

		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		params.put("user_no", user_no);			// 사용자

		return dao.searchProcessList(params);
	}
	
	@Override
	public List<HashMap<String, Object>> selectProcessPath(HashMap<String, Object> params) throws Exception {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		params.put("user_no", user_no);

		return dao.selectProcessPath(params);
	}
	
}
