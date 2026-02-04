package com.org.iopts.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.Base64.Encoder;

import javax.net.ssl.HttpsURLConnection;

import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.org.iopts.setting.service.impl.Pi_SetServiceImpl;

public class RemediationUtil {
	private static final Logger logger = LoggerFactory.getLogger(ReconUtil.class);
    private static final String KEY_PROPERTY_NAME = "user.key";
    private static final String CONFIG_PATH = "property/config.properties";
    private static String ENCRYPTION_KEY;

    static {
        loadEncryptionKey();
    }

    private static void loadEncryptionKey() {
        try (Reader reader = Resources.getResourceAsReader(CONFIG_PATH)) {
            Properties props = new Properties();
            props.load(reader);
            ENCRYPTION_KEY = props.getProperty(KEY_PROPERTY_NAME);
            if (ENCRYPTION_KEY == null || ENCRYPTION_KEY.isEmpty()) {
                logger.error("Encryption key missing: {}", KEY_PROPERTY_NAME);
                throw new IllegalArgumentException("암호화 키가 누락되었습니다: " + KEY_PROPERTY_NAME);
            }
            logger.info("Encryption key loaded from {}", CONFIG_PATH);
        } catch (Exception e) {
            logger.error("Failed to load encryption key from {}", CONFIG_PATH, e);
            throw new RuntimeException("키 로딩 실패: " + CONFIG_PATH, e);
        }
    }
	
	public Map<String, Object> getServerDataLee(String recon_id, String recon_password, String sURL, String requestMethod, String requestData) throws ProtocolException {
		
		logger.info(recon_id);
		logger.info(recon_password);
		recon_password = Pi_SetServiceImpl.encrypt(1, recon_password, ENCRYPTION_KEY);
		logger.info(sURL);
		logger.info(requestMethod);
		logger.info(requestData);
		

        Map<String, Object> resultMap = new HashMap<String,Object>();
		String usercredentials = recon_id+":"+recon_password;        
		
		
		String[] array=new String[6];
		
		array[0]="-k";
		array[1]="-X";
		array[2]="GET";
		array[3]="-u";
		array[4]=usercredentials;
		array[5]=sURL;
		
		String json_string =new ICurl().opt(array).exec(null);
		
		System.out.println(json_string);


		resultMap.put("HttpsResponseCode", 200);
		resultMap.put("HttpsResponseMessage", "OK");
		resultMap.put("HttpsResponseData", json_string);
		
		
        return resultMap; 
    }
	
	
	public Map<String, Object> getServerData(String recon_id, String recon_password, String sURL, String requestMethod, String requestData) throws ProtocolException {
		
		logger.info(recon_id);
		logger.info(recon_password);
		recon_password = Pi_SetServiceImpl.encrypt(1, recon_password, ENCRYPTION_KEY);
		logger.info(sURL);
		logger.info(requestMethod);
		logger.info(requestData);
		
		
		//for localhost testing only
		javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
	    new javax.net.ssl.HostnameVerifier() {
	        public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
	        	return true;
	        }
	    });

        Map<String, Object> resultMap = new HashMap<String,Object>();
		String usercredentials = recon_id+":"+recon_password;        
		// Base64 인코딩 ///////////////////////////////////////////////////
		Encoder encoder = Base64.getEncoder();        
		String basicAuth = encoder.encodeToString(usercredentials.getBytes());

		URL url = null;
		HttpsURLConnection httpsCon = null;
		try {
			url = new URL(sURL);
			httpsCon = (HttpsURLConnection) url.openConnection();					
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (requestMethod.equals("GET")) {
			httpsCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			httpsCon.setRequestProperty("User-Agent", "Mozilla/5.0");
			httpsCon.setRequestProperty("Accept", "application/json");
			httpsCon.setRequestProperty("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
			httpsCon.setRequestProperty("Accept-Encoding", "gzip, deflate, br"); 
			httpsCon.setRequestProperty("Authorization", "Basic " + basicAuth);
		}
		else {
			httpsCon.setRequestProperty("Content-Type", "application/json; charset=utf8");
			httpsCon.setRequestProperty("Accept", "application/json");
			httpsCon.setRequestProperty("User-Agent", "Mozilla/5.0");
			httpsCon.setRequestProperty("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
			httpsCon.setRequestProperty("Authorization", "Basic " + basicAuth);
			httpsCon.setConnectTimeout(5000);
			httpsCon.setReadTimeout(15000);
		}

		httpsCon.setUseCaches(false);
		httpsCon.setDoInput(true);
		httpsCon.setDoOutput(true);
		httpsCon.setRequestMethod(requestMethod);

		if (requestMethod.equals("POST") || requestMethod.equals("PUT"))
		{
		    BufferedReader reader = null;
		    StringBuilder results = new StringBuilder();
		    String line;
		    
		    try {
		        if (requestData != null) {
		            OutputStreamWriter wr = new OutputStreamWriter(httpsCon.getOutputStream(), StandardCharsets.UTF_8);
		            wr.write(requestData);
		            wr.flush();
		            
		            // ========== 응답 코드 먼저 확인 ==========
		            int responseCode = httpsCon.getResponseCode();
		            
		            // 성공(2xx)이면 일반 스트림, 실패(4xx, 5xx)면 에러 스트림
		            InputStream inputStream;
		            if (responseCode >= 200 && responseCode < 300) {
		                inputStream = httpsCon.getInputStream();
		            } else {
		                inputStream = httpsCon.getErrorStream();
		            }
		            
		            if (inputStream != null) {
		                reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		                while ((line = reader.readLine()) != null) {
		                    results.append(line);
		                }
		                reader.close();  
		            }
		            
		            
		            resultMap.put("HttpsResponseCode", responseCode);
		            resultMap.put("HttpsResponseMessage", httpsCon.getResponseMessage());
		            resultMap.put("HttpsResponseDataMessage", results.toString());
		        }
		        
		        logger.info("resultMap :: " + resultMap.toString());
		        httpsCon.disconnect();
		        return resultMap;
		        
		    } catch (IOException e) {
		        logger.error("POST/PUT 요청 중 예외 발생", e);
		        
		        try {
		            // 응답 코드 가져오기 시도
		            int errorCode = httpsCon.getResponseCode();
		            String errorMessage = httpsCon.getResponseMessage();
		            
		            logger.info("Error Code :: " + errorCode);
		            logger.info("Error Message :: " + errorMessage);
		            
		            resultMap.put("HttpsResponseCode", errorCode);
		            resultMap.put("HttpsResponseMessage", errorMessage);
		            
		        } catch (IOException ex) {
		            logger.error("응답 코드 조회 실패", ex);
		            resultMap.put("HttpsResponseCode", 500);
		            resultMap.put("HttpsResponseMessage", "서버 연결 오류: " + e.getMessage());
		        }
		        
		        httpsCon.disconnect();
		        return resultMap;
		        
		    } catch (Exception e) {
		        logger.error("예기치 않은 오류", e);
		        resultMap.put("HttpsResponseCode", 500);
		        resultMap.put("HttpsResponseMessage", "예기치 않은 오류: " + e.getMessage());
		        httpsCon.disconnect();
		        return resultMap;
		    }
		}

		if (requestMethod.equals("DELETE"))
		{
			BufferedReader reader = null;
			StringBuilder results = new StringBuilder();
	        String line;
	        
			try {
				
				logger.info(httpsCon.getResponseCode() + " Code");
	        	logger.info(httpsCon.getResponseMessage().toString());
	        	
	        	resultMap.put("HttpsResponseCode", httpsCon.getResponseCode());
				resultMap.put("HttpsResponseMessage", httpsCon.getResponseMessage());

				logger.info("resultMap :: " + resultMap.toString());
		        httpsCon.disconnect();
		        return resultMap;
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		BufferedReader reader = null;
		StringBuilder results = new StringBuilder();
        String line;
        
        try {
        	logger.info(httpsCon.getResponseCode() + " Code");
        	logger.info(httpsCon.getResponseMessage().toString());
        	
        	if(httpsCon.getResponseCode() == 401) {
        		resultMap.put("HttpsResponseCode", httpsCon.getResponseCode());
        		resultMap.put("HttpsResponseMessage", "개인정보 검출관리(PICenter) 서버 연결에 문제가 있습니다. 관리자에게 연락 주시기 바랍니다.");
        		
        		return resultMap;
        	}else if(httpsCon.getResponseCode() == 404) {
        		resultMap.put("HttpsResponseCode", httpsCon.getResponseCode());
        		resultMap.put("HttpsResponseMessage", "개인정보 검출관리(PICenter) 서버에 결과가 더이상 존재하지 않습니다. 검출 대상서버에 파일이 삭제/암호화 된 이후 검색이 수행 된 경우 이런 증상이 발생할 수 있습니다. 추가 문의사항은 관리자에게 연락 주시기 바랍니다.");
        		/*resultMap.put("HttpsResponseMessage", "검출 서버에 결과가 더 이상 존재하지 않습니다. 파일이 삭제/암호화 된 이후 검색이 수행 된 경우 이런 증상이 발생할 수 있습니다.");*/
        		
        		return resultMap;
        	}else if(httpsCon.getResponseCode() == 422) {
        		resultMap.put("HttpsResponseCode", httpsCon.getResponseCode());
        		resultMap.put("HttpsResponseMessage", "개인정보 검출관리(PICenter) 서버 연결에 문제가 있습니다. 관리자에게 연락 주시기 바랍니다.");
        		/*resultMap.put("HttpsResponseMessage", "검출 서버에 결과가 더 이상 존재하지 않습니다. 파일이 삭제/암호화 된 이후 검색이 수행 된 경우 이런 증상이 발생할 수 있습니다.");*/
        		
        		return resultMap;
        	}
        }catch (Exception e) {
			// TODO: handle exception
		}
        
        try {
        	if (httpsCon.getContentEncoding().equals("gzip")) {
        		logger.info("=================================================");
        		try {
        			InputStream httpStream = httpsCon.getInputStream();
        			InputStream gzipInputStream = new GZIPInputStream(httpStream);
        			int len;
        			byte buffer[] = new byte[1024];
        			
        			try {
        				while ((len = gzipInputStream.read(buffer)) != -1) {
        					results.append(new String(buffer, 0, len, "UTF-8"));
        				}
        			} catch (IOException e) {
        				// TODO Auto-generated catch block
        				//e.printStackTrace();
        			}
        			
        		} catch (Exception e) {
        			// TODO Auto-generated catch block
//					e.printStackTrace();
        			resultMap.put("HttpsResponseCode", 404);
        			resultMap.put("HttpsResponseMessage", "Resource not found.");
        			return resultMap;
        		}
        	}
        	else {
        		try {
        			reader = new BufferedReader(new InputStreamReader(httpsCon.getInputStream()));
        			while ((line = reader.readLine()) != null) {
        				results.append(line);
        			}
        		} catch (Exception e) {
        			// TODO Auto-generated catch block
//					e.printStackTrace();
        			resultMap.put("HttpsResponseCode", 404);
        			resultMap.put("HttpsResponseMessage", "Resource not found.");
        			return resultMap; 
        		}
        	}
        } catch (Exception e1) {
			resultMap.put("HttpsResponseCode", 404);
			/*resultMap.put("HttpsResponseMessage", "Resource not found.");*/
			resultMap.put("HttpsResponseMessage", "개인정보 검출관리(PICenter) 서버 연결에 문제가 있습니다. 관리자에게 연락 주시기 바랍니다.");
			
	        return resultMap;
		}
		
		logger.info("---------------------------------------------");
        
        httpsCon.disconnect();

		resultMap.put("HttpsResponseCode", 200);
		resultMap.put("HttpsResponseMessage", "OK");
		resultMap.put("HttpsResponseData", results.toString());
        return resultMap; 
    }

}
