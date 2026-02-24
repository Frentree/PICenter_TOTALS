package com.org.iopts.service;

import com.org.iopts.dto.request.LoginRequest;
import com.org.iopts.dto.response.LoginResponse;

/**
 * User Service Interface
 */
public interface UserService {

    /**
     * User Login
     */
    LoginResponse login(LoginRequest request);

    /**
     * User Logout
     */
    void logout(String token);

    /**
     * Refresh Token
     */
    String refreshToken(String refreshToken);

    /**
     * Get User Info by UserNo
     */
    LoginResponse getUserInfo(String userNo);
}
