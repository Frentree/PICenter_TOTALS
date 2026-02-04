package com.org.iopts.util;

import java.io.Reader;
import java.util.Properties;
import org.apache.ibatis.io.Resources;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class DecryptingPropertyUtil extends PropertyPlaceholderConfigurer {
    private static final String KEY_PROPERTY_NAME = "user.key";
    private static final String CONFIG_PATH = "property/config.properties";
    private static String ENCRYPTION_KEY;

    static {
        try (Reader reader = Resources.getResourceAsReader(CONFIG_PATH)) {
            Properties props = new Properties();
            props.load(reader);
            System.out.println(KEY_PROPERTY_NAME);
            ENCRYPTION_KEY = props.getProperty(KEY_PROPERTY_NAME);
            if (ENCRYPTION_KEY == null || ENCRYPTION_KEY.isEmpty()) {
                throw new IllegalArgumentException("암호화 키가 누락되었습니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException("키 로딩 실패: " + CONFIG_PATH, e);
        }
    }

    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if ("jdbc.password".equals(propertyName) && propertyValue != null) {
            try {
                return decrypt(propertyValue);
            } catch (Exception e) {
                throw new RuntimeException("복호화 실패: " + propertyName, e);
            }
        }
        return propertyValue;
    }

    private String decrypt(String encryptedValue) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(ENCRYPTION_KEY);
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        return encryptor.decrypt(encryptedValue);
    }

    public static String decryptValue(String encryptedValue) {
        try {
            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            encryptor.setPassword(ENCRYPTION_KEY);
            encryptor.setAlgorithm("PBEWithMD5AndDES");
            return encryptor.decrypt(encryptedValue);
        } catch (Exception e) {
            throw new RuntimeException("복호화 실패", e);
        }
    }
}