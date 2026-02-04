package com.org.iopts.mail.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import com.org.iopts.group.vo.GroupTreeServerVo;
import com.org.iopts.mail.vo.MailVo;
import com.org.iopts.mail.vo.UserVo;

import com.google.gson.JsonArray;

public interface MailService {

	Map<String, List<String>> serverGroupMail(HttpServletRequest request) throws Exception;

	Map<String, Object> serverGroupMailContent(HttpServletRequest request) throws Exception;

	List<UserVo> approvalSendMail(HashMap<String, Object> params) throws Exception;

	Map<String, Object> templateInsert(HttpServletRequest request) throws Exception;

	// LG생건 전용 메일 발송
	Map<String, Object> serverGroupMailLg(HttpServletRequest request) throws Exception;

	// 메일 템플릿 목록 조회
	List<Map<String, Object>> getTemplateList() throws Exception;

	// 메일 템플릿 상세 조회
	Map<String, Object> getTemplateDetail(int idx) throws Exception;

	// 메일 템플릿 삭제
	void deleteTemplate(int idx) throws Exception;

	// 메일 템플릿 등록
	void insertTemplate(Map<String, Object> params) throws Exception;

	// 메일 템플릿 수정
	void updateTemplate(Map<String, Object> params) throws Exception;
}
