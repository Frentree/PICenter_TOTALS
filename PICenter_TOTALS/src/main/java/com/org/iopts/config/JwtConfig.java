package com.org.iopts.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT Configuration and Utility
 */
@Slf4j
@Component
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generate JWT Token
     */
    public String generateToken(String userNo, String userId, String userGrade) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userNo", userNo);
        claims.put("userId", userId);
        claims.put("userGrade", userGrade);

        return Jwts.builder()
                .claims(claims)
                .subject(userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Generate Refresh Token
     */
    public String generateRefreshToken(String userNo, String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userNo", userNo);
        claims.put("userId", userId);
        claims.put("type", "refresh");

        return Jwts.builder()
                .claims(claims)
                .subject(userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Validate Token
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Get Claims from Token
     */
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Get UserNo from Token
     */
    public String getUserNo(String token) {
        Claims claims = getClaims(token);
        return claims.get("userNo", String.class);
    }

    /**
     * Get UserId from Token
     */
    public String getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    /**
     * Get UserGrade from Token
     */
    public String getUserGrade(String token) {
        Claims claims = getClaims(token);
        return claims.get("userGrade", String.class);
    }
}
