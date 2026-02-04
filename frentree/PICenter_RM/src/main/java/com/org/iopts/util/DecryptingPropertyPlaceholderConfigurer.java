package com.org.iopts.util;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.ibatis.io.Resources;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class DecryptingPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    private static final String ENC_PREFIX = "ENC(";
    private static final String ENC_SUFFIX = ")";
    private static final String KEY_PROPERTY_NAME = "pic.pass.key";
    private static final String CONFIG_PATH = "property/config.properties";

    private static String AES_KEY;

    static {
        try (Reader reader = Resources.getResourceAsReader(CONFIG_PATH)) {
            Properties props = new Properties();
            props.load(reader);
            AES_KEY = props.getProperty(KEY_PROPERTY_NAME);

            if (AES_KEY == null || AES_KEY.length() < 16) {
                throw new IllegalArgumentException("암호화 키가 누락되었거나 길이가 부족합니다.");
            }
        } catch (NullPointerException e) {
        	throw new RuntimeException("AES 키 로딩 실패: " + CONFIG_PATH, e);
        } catch (Exception e) {
            throw new RuntimeException("AES 키 로딩 실패: " + CONFIG_PATH, e);
        }
    }

    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if (propertyValue != null && isEncrypted(propertyValue)) {
            String encryptedBase64 = propertyValue.substring(ENC_PREFIX.length(), propertyValue.length() - ENC_SUFFIX.length());
            try {
                return decrypt(encryptedBase64);
            } catch (NullPointerException e) {
            	throw new RuntimeException("복호화 실패: " + propertyName, e);
            } catch (Exception e) {
                throw new RuntimeException("복호화 실패: " + propertyName, e);
            }
        }
        return propertyValue;
    }

    private boolean isEncrypted(String value) {
        return value.startsWith(ENC_PREFIX) && value.endsWith(ENC_SUFFIX);
    }

    private String decrypt(String encryptedBase64) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(AES_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        byte[] decoded = Base64.getDecoder().decode(encryptedBase64);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted, StandardCharsets.UTF_8);
    }
    
    public static String decryptValue(String encryptedBase64) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(AES_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            byte[] decoded = Base64.getDecoder().decode(encryptedBase64);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (NullPointerException e) {
        	throw new RuntimeException("NullPointerException  :: ", e);
        } catch (Exception e) {
            throw new RuntimeException("복호화 실패", e);
        }
    }
}
