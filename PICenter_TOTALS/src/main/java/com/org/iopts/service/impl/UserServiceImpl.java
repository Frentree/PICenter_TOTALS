package com.org.iopts.service.impl;

import com.org.iopts.config.JwtConfig;
import com.org.iopts.domain.User;
import com.org.iopts.dto.request.LoginRequest;
import com.org.iopts.dto.response.LoginResponse;
import com.org.iopts.exception.CustomException;
import com.org.iopts.exception.ErrorCode;
import com.org.iopts.repository.UserRepository;
import com.org.iopts.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * User Service Implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;

    /**
     * User Login
     */
    @Override
    @Transactional(noRollbackFor = CustomException.class)
    public LoginResponse login(LoginRequest request) {
        String username = request.getUsername() != null ? request.getUsername().trim() : "";
        log.info("Login attempt: userId=[{}]", username);

        if (username.isEmpty()) {
            log.warn("Login failed: empty username");
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        // Find user by userId (로그인 ID)
        User user = userRepository.findByUserId(username).orElse(null);

        if (user == null) {
            log.warn("Login failed: user not found - userId=[{}]", username);
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        log.debug("User found: userId=[{}], userNo={}, lockStatus={}, accYn={}",
                user.getUserId(), user.getUserNo(), user.getLockStatus(), user.getAccYn());

        // Check if user account is locked (lock_status != 0 means locked)
        if (user.getLockStatus() != null && user.getLockStatus() != 0) {
            log.warn("Login failed: account locked - userId=[{}]", username);
            throw new CustomException(ErrorCode.USER_LOCKED);
        }

        // Check if user is disabled (acc_yn != 'Y' means disabled)
        if (user.getAccYn() != null && !"Y".equals(user.getAccYn())) {
            log.warn("Login failed: account disabled - userId=[{}]", username);
            throw new CustomException(ErrorCode.USER_DISABLED);
        }

        // Verify password
        String storedPwd = user.getPassword();
        if (storedPwd == null || storedPwd.isEmpty()) {
            log.warn("Login failed: no password set - userId=[{}]", username);
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        if (!passwordEncoder.matches(request.getPassword(), storedPwd)) {
            log.warn("Login failed: wrong password - userId=[{}]", username);

            // Increment login fail count
            int failCnt = 0;
            try {
                failCnt = Integer.parseInt(user.getFailedCount() != null ? user.getFailedCount() : "0");
            } catch (NumberFormatException e) {
                failCnt = 0;
            }
            failCnt++;
            user.setFailedCount(String.valueOf(failCnt));

            // Lock account if fail count >= 5
            if (failCnt >= 5) {
                user.setLockStatus(1);
                user.setLockDate(LocalDateTime.now());
                log.warn("Account locked after {} failed attempts - userId=[{}]", failCnt, username);
            }

            userRepository.save(user);
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        // Reset login fail count on successful login
        user.setFailedCount("0");
        user.setLogindate(LocalDateTime.now());
        userRepository.save(user);

        // Generate tokens
        String accessToken = jwtConfig.generateToken(user.getUserNo(), user.getUserId(), user.getUserGrade());
        String refreshToken = jwtConfig.generateRefreshToken(user.getUserNo(), user.getUserId());

        log.info("Login successful: userId=[{}], userNo={}", username, user.getUserNo());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userNo(user.getUserNo())
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userGrade(user.getUserGrade())
                .expiresIn(7200000L) // 2 hours
                .build();
    }

    /**
     * User Logout
     */
    @Override
    @Transactional
    public void logout(String token) {
        log.info("Logout token: {}", token != null ? token.substring(0, Math.min(10, token.length())) : "null");
    }

    /**
     * Refresh Token
     */
    @Override
    @Transactional
    public String refreshToken(String refreshToken) {
        log.info("Refresh token request");

        if (!jwtConfig.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        }

        String userNo = jwtConfig.getUserNo(refreshToken);

        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return jwtConfig.generateToken(user.getUserNo(), user.getUserId(), user.getUserGrade());
    }

    /**
     * Get User Info by UserNo
     */
    @Override
    public LoginResponse getUserInfo(String userNo) {
        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return LoginResponse.builder()
                .userNo(user.getUserNo())
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userGrade(user.getUserGrade())
                .build();
    }
}
