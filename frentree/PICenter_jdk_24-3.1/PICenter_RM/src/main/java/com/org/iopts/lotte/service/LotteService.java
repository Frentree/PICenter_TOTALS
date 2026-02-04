package com.org.iopts.lotte.service;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

public interface LotteService {

	Map<String, Object> selectMember(HttpServletRequest request) throws Exception;


}
