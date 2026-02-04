package com.org.iopts.util;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class SessionCheckServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String checkSessionHeader = request.getHeader("X-Check-Session");

        // "X-Check-Session" 헤더가 있는 경우에는 세션을 갱신하지 않음
        if ("no-session-update".equals(checkSessionHeader)) {
            HttpSession session = request.getSession(false); // 세션 갱신 없이 가져오기
            if (session == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                Object memberSession = session.getAttribute("memberSession");
                System.out.println(memberSession);
                if (memberSession == null) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                } else {
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            }
        }
    
    }
}
