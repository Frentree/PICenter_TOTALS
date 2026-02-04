package com.org.iopts.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 전역 예외 처리기
 * 모든 예외를 error.jsp로 리다이렉트하여 서버 정보 노출 방지
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * 404 에러 처리
	 */
	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleNotFound(NoHandlerFoundException ex, HttpServletRequest request) {
		logger.warn("404 Not Found: " + request.getRequestURI());
		return "error/error";
	}

	/**
	 * 모든 예외 처리
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleException(Exception ex, HttpServletRequest request) {
		logger.error("서버 오류 발생 - URI: " + request.getRequestURI() + ", 에러: " + ex.getMessage());
		return "error/error";
	}
}
