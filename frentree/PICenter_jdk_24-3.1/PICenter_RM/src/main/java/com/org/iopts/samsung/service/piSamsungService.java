package com.org.iopts.samsung.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

public interface piSamsungService {
	
	public Map<String, Object> selectMember(HttpServletRequest request) throws Exception;
	
	// 로그인 등급 체크
	public Map<String, Object> checkMemberGrade(HttpServletRequest request) throws Exception;
	
	// SSO 자동 로그인(삼성화재)
	public Map<String, Object> SSOSelectMember(HttpServletRequest request) throws Exception;
	
	// 정탐/오탐 리스트 > 결재 요청 팝업 결재 아이디 Seq 조회
	public Map<String, Object> selectDocuNum(HashMap<String, Object> params) throws Exception;

	// 정탐/오탐 결재 리스트에 추가
	public HashMap<String, Object> registProcessCharge(HashMap<String, Object> params) throws Exception;
	
	public HashMap<String, Object> registProcessCharge2(HashMap<String, Object> params) throws Exception;

	// 정탐/오탐 리스트 정보 수정
	public void updateProcessStatus(HashMap<String, Object> params, Map<String, Object> chargeMap) throws Exception;

	// 정탐/오탐 결재 리스트
	public List<HashMap<String, Object>> searchApprovalListData(HashMap<String, Object> params) throws Exception;

	// 정탐/오탐 결재 리스트 - 조회
	public List<HashMap<String, Object>> selectProcessGroupPath(HashMap<String, Object> params) throws Exception;
	
	// 결재자 조회
	public List<HashMap<String, Object>> selectApprovalUserList(HashMap<String, Object> params) throws Exception;

	// 정탐/오탐 리스트 조회
	public List<HashMap<String, Object>> searchProcessList(HashMap<String, Object> params) throws Exception;


}