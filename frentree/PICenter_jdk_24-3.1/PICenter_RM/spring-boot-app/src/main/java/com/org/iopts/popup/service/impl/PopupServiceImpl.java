package com.org.iopts.popup.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.iopts.dao.Pi_UserDAO;
import com.org.iopts.popup.dao.PopupDAO;
import com.org.iopts.popup.service.PopupService;
import com.org.iopts.util.ServletUtil;
import com.org.iopts.util.SessionUtil;

@Service
@Transactional
public class PopupServiceImpl implements PopupService {

	private static Logger log = LoggerFactory.getLogger(PopupServiceImpl.class);
	
	@Inject
	private PopupDAO dao;
	
	@Inject
	private Pi_UserDAO userDao;

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
	
	@Override
	public List<Map<String, Object>> getUserTargetList(Map<String, Object> map) throws Exception {
		return dao.getUserTargetList(map);
	}

	// 공지사항 상세보기
	@Override
	public Map<String, Object> noticeDetail(int map) throws Exception {
		
		Map<String, Object> noticemap = dao.noticeDetail(map);
		String notice_title = noticemap.get("NOTICE_TITLE").toString();
			notice_title = replaceParameter(notice_title);
			notice_title = notice_title.replaceAll("\r\n", "<br>");
		String notice_con = noticemap.get("NOTICE_CON").toString();
			notice_con = replaceParameter(notice_con);
			notice_con = notice_con.replaceAll("\r\n", "<br>");
		
		noticemap.put("NOTICE_CON", notice_con);
		noticemap.put("NOTICE_TITLE", notice_title);
		
		return noticemap;
	}
	
	// faq 상세보기
	@Override
	public Map<String, Object> faqDetail(int map) throws Exception {
		
		Map<String, Object> faqmap = dao.faqDetail(map);
		String faq_title = faqmap.get("FAQ_TITLE").toString();
			faq_title = replaceParameter(faq_title);
			faq_title = faq_title.replaceAll("\r\n", "<br>");
		String faq_con = faqmap.get("FAQ_CONTENT").toString();
			faq_con = replaceParameter(faq_con);
			faq_con = faq_con.replaceAll("\r\n", "<br>");
		
		faqmap.put("FAQ_CONTENT", faq_con);
		faqmap.put("FAQ_TITLE", faq_title);
		
		return faqmap;
	}
	
	@Override
	public Map<String, Object> downloadDetail(int map) throws Exception {
		
		Map<String, Object> downloadmap = dao.downloadDetail(map);
		String faq_title = downloadmap.get("DOWNLOAD_TITLE").toString();
			faq_title = replaceParameter(faq_title);
			faq_title = faq_title.replaceAll("\r\n", "<br>");
		String faq_con = downloadmap.get("DOWNLOAD_CON").toString();
			faq_con = replaceParameter(faq_con);
			faq_con = faq_con.replaceAll("\r\n", "<br>");
		
			downloadmap.put("DOWNLOAD_CON", faq_con);
			downloadmap.put("DOWNLOAD_TITLE", faq_title);
		
		return downloadmap;
	}

	@Override
	public List<Map<String, Object>> selectUserList(Map<String, Object> map) throws Exception {
		map.put("user_no", SessionUtil.getSession("memberSession", "USER_NO"));
		return dao.selectUserList(map);
	}

	@Override
	public Map<String, Object> updateTargetUser(HttpServletRequest request, Map<String, Object> map) throws Exception {
	    Map<String, Object> resultMap = new HashMap<>();

	    String targetId = map.get("target_id").toString();
	    String apNo = map.get("ap_no").toString();
	    String userFlag = (String) map.get("userFlag"); // 별도로 받은 userFlag
	    List<Map<String, Object>> mngrList = (List<Map<String, Object>>) map.get("mngrList");

	    try {
	        // 해당 userFlag 삭제 (항상 실행)
	        Map<String, Object> deleteParam = new HashMap<>();
	        deleteParam.put("target_id", targetId);
	        deleteParam.put("ap_no", apNo);
	        deleteParam.put("user_flag", userFlag);
	        dao.deleteTargetUserByFlag(deleteParam);

	        List<Map<String, Object>> finalMngrList = new ArrayList<>();

	        // 담당자가 있을 때만 추가
	        for (Map<String, Object> mngr : mngrList) {
	            String userNo = (String) mngr.get("user_no");
	            String mngrUserFlag = (String) mngr.get("userFlag");

	            if (userNo != null && !userNo.trim().isEmpty()) {
	                String[] userNoArray = userNo.split(",");

	                for (String singleUserNo : userNoArray) {
	                    singleUserNo = singleUserNo.trim();
	                    if (!singleUserNo.isEmpty()) {
	                        Map<String, Object> finalMngr = new HashMap<>();
	                        finalMngr.put("user_no", singleUserNo);
	                        finalMngr.put("userFlag", mngrUserFlag);

	                        Map<String, Object> userInfo = dao.selectUser(finalMngr);
	                        String userName = "";
	                        if (userInfo != null && userInfo.get("USER_NAME") != null) {
	                            userName = userInfo.get("USER_NAME").toString();
	                        } else {
	                            break;
	                        }
	                        finalMngr.put("user_name", userName);
	                        finalMngrList.add(finalMngr);
	                    }
	                }
	            }
	        }

	        // 새로운 담당자 추가 (있을 때만)
	        if (!finalMngrList.isEmpty()) {
	            Map<String, Object> insertParam = new HashMap<>();
	            insertParam.put("target_id", targetId);
	            insertParam.put("ap_no", apNo);
	            insertParam.put("mngrList", finalMngrList);
	            dao.updateTargetUser(insertParam);
	        }

	        resultMap.put("resultCode", 0);
	        resultMap.put("resultMessage", "사용자 지정 완료");

	    } catch (Exception e) {
	        e.printStackTrace();
	        resultMap.put("resultCode", -1);
	        resultMap.put("resultMessage", "사용자 지정 실패");
	    }

	    return resultMap;
	}
	
	@Override
	public Map<String, Object> updatePCTargetUser(Map<String, Object> map) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			dao.updatePCTargetUser(map);
		}catch (Exception e) {
			// TODO: handle exception
			resultMap.put("resultCode", -1);
			resultMap.put("resultMeassage", "사용자 지정 실패");
		}
		
		resultMap.put("resultCode", 0);
		resultMap.put("resultMeassage", "사용자 지정 성공");
		
		return resultMap;
	}

	@Override
	public Map<String, Object> updateTargetUserlog(Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			dao.updateTargetUserlog(map);
			
			dao.updateUserGrade(map);
		}catch (Exception e) {
			resultMap.put("resultCode", -1);
			resultMap.put("resultMeassage", "사용자 지정 실패");
		}
		
		resultMap.put("resultCode", 0);
		resultMap.put("resultMeassage", "사용자 지정 성공");
		
		return resultMap;
	}
	
	@Override
	public List<Map<String, Object>> getUserDetailList(String targetId, String apNo, List<Map<String, Object>> params) {
	    List<Map<String, Object>> returnObject = new ArrayList<>();

	    try {
	        Map<String, Object> queryParams = new HashMap<>();
	        queryParams.put("target_id", targetId);
	        queryParams.put("ap_no", apNo);
	        
	        List<Map<String, Object>> allDbData = dao.selectTargetUserList(queryParams);

	        Map<String, List<Map<String, Object>>> groupedByUserFlag = new HashMap<>();

	        for (Map<String, Object> data : allDbData) {
	            String userFlag = (String) data.get("USERFLAG");
	            userFlag = userFlag != null ? userFlag : "";

	            if (!groupedByUserFlag.containsKey(userFlag)) {
	                groupedByUserFlag.put(userFlag, new ArrayList<>());
	            }
	            groupedByUserFlag.get(userFlag).add(data);
	        }

	        Map<String, Object> finalResult = new HashMap<>();
	        finalResult.put("groupedData", groupedByUserFlag);
	        finalResult.put("totalCount", allDbData.size());

	        returnObject.add(finalResult);

	    } catch (Exception e) {
	        log.error("getUserDetailList 처리 중 오류 발생: ", e);
	        return new ArrayList<>();
	    }

	    return returnObject;
	}


	
	public List<Map<String, Object>> selectNetPolicy(HttpServletRequest request) throws Exception {
		log.info("policyNm :: " + request.getParameter("policyNm"));
		List<Map<String, Object>> resultList = new ArrayList<>();
		try {
			
			Map<String, Object> map = new HashMap<>();
			map.put("policyNm", request.getParameter("policyNm"));
			resultList = dao.selectNetPolicy(map);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultList;
	}

	private String replaceParameter(String param) {
		String result = param;
		if(param != null) {
			result = result.replaceAll("&", "&amp;");
			result = result.replaceAll("<", "&lt;");
			result = result.replaceAll(">", "&gt;");
			result = result.replaceAll("\"", "&quot;");
		}
	      
	return result;
	}

	@Override
	public List<Map<String, Object>> approvalTeamUserList(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> approvalList = new ArrayList<>();
		try {
			String user_nm = request.getParameter("user_nm");
			String team_nm = request.getParameter("team_nm");
			String insa_code = SessionUtil.getSession("memberSession", "INSA_CODE");
			
			log.info("user_nm ::: " +user_nm);
			log.info("user_nm ::: " +team_nm);
			map.put("user_nm", user_nm);
			map.put("team_nm", team_nm);
			map.put("insa_code", insa_code);
			map.put("user_no", SessionUtil.getSession("memberSession", "USER_NO"));
			approvalList = dao.approvalTeamUserList(map);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error ::: " +e.getLocalizedMessage());
		}
		
		
//		return dao.selectUserList(map);
		return approvalList;
	}

}
