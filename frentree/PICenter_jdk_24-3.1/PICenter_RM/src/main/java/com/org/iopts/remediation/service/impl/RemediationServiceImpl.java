package com.org.iopts.remediation.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.org.iopts.remediation.dao.piRemediationDAO;
import com.org.iopts.remediation.service.RemediationService;
import com.org.iopts.util.SessionUtil;

@Service("remediationService")
public class RemediationServiceImpl implements RemediationService {

	private static Logger log = LoggerFactory.getLogger(RemediationServiceImpl.class);

	@Inject
	private piRemediationDAO remediationDAO;

	@Override
	public List<HashMap<String, Object>> selectRemediationHistory(HashMap<String, Object> params) throws Exception {
	    String user_no = SessionUtil.getSession("memberSession", "USER_NO");
	    String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
	    
	    log.info("selectRemediationHistory :: params = " + params);
	    
	    if(!"9".equals(user_grade)) {
	        params.put("user_no", user_no);
	        List<String> targetIds = remediationDAO.selectUserTargetIds(params);
	        
	        if(targetIds == null || targetIds.isEmpty()) {
	            // 접근 가능한 target이 없으면 리턴
	            return new ArrayList<>();
	        }
	        
	        params.put("target_ids", targetIds);
	    }
	    
	    return remediationDAO.selectRemediationHistory(params);
	}
}