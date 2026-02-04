package com.org.iopts.util;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.io.Resources;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class SessionTimeoutFilter implements Filter {
    private static int sessionTimeoutInSeconds;
    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;
    private static final String SELECT_TIMEOUT_QUERY = "SELECT SESSION_TIME FROM PI_VERSION";
    private static final String UPDATE_TIMEOUT_QUERY = "UPDATE PI_VERSION SET SESSION_TIME = ?";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        loadDatabaseProperties();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TIMEOUT_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("=============================================================");
            System.out.println(resultSet.next());
            System.out.println(resultSet.getString("SESSION_TIME"));
            if (resultSet.next()) {
                System.out.println(resultSet.getInt("SESSION_TIME"));
                sessionTimeoutInSeconds = resultSet.getInt("SESSION_TIME");
            } else {
                sessionTimeoutInSeconds = 1800; // 기본값 30분 (1800초)
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sessionTimeoutInSeconds = 1800; // 예외 발생 시 기본값 설정
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpSession session = httpRequest.getSession();
            session.setMaxInactiveInterval(sessionTimeoutInSeconds);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 필터 종료 시 실행할 코드
    }

    public static void setSessionTimeoutInSeconds(int seconds) {
        sessionTimeoutInSeconds = seconds;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TIMEOUT_QUERY)) {
            preparedStatement.setInt(1, seconds);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("update filter error :: " + e);
        }
    }

    private void loadDatabaseProperties() {
        try {
            // Load encryption key from config.properties
            Properties configProps = new Properties();
            String configResource = "property/config.properties";
            try (Reader configReader = Resources.getResourceAsReader(configResource)) {
                configProps.load(configReader);
            }
            String encryptionKey = configProps.getProperty("user.key");
            if (encryptionKey == null || encryptionKey.isEmpty()) {
                throw new IllegalArgumentException("암호화 키가 누락되었습니다: user.key");
            }

            // Load database properties from dbpool.properties
            Properties properties = new Properties();
            String resource = "/property/dbpool.properties";
            try (Reader reader = Resources.getResourceAsReader(resource)) {
                properties.load(reader);
            }

            DB_URL = properties.getProperty("jdbc.url");
            DB_USER = properties.getProperty("jdbc.username");
            String encryptedPassword = properties.getProperty("jdbc.password");

            if (encryptedPassword != null && !encryptedPassword.isEmpty()) {
                StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
                encryptor.setPassword(encryptionKey);
                encryptor.setAlgorithm("PBEWithMD5AndDES");
                DB_PASSWORD = encryptor.decrypt(encryptedPassword);
            } else {
                System.out.println("Database password is null or empty");
                DB_PASSWORD = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}