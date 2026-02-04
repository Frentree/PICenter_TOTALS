package com.org.iopts.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipException;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReconUtil 
{
	private static final Logger logger = LoggerFactory.getLogger(ReconUtil.class);
	
	public Map<String, Object> getServerDataLee(String recon_id, String recon_password, String sURL, String requestMethod, String requestData) throws ProtocolException {
		
		logger.info(recon_id);
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

		resultMap.put("HttpsResponseCode", 200);
		resultMap.put("HttpsResponseMessage", "OK");
		resultMap.put("HttpsResponseData", json_string);
		
		
        return resultMap; 
    }
	
	
	public Map<String, Object> getServerData(String recon_id, String recon_password, String sURL, String requestMethod, String requestData) throws ProtocolException {
		
		logger.info(recon_id);
		logger.info(recon_password);
		logger.info(sURL);
		logger.info(requestMethod);
		logger.info(requestData);
		
		//for localhost testing only
		javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
	    new javax.net.ssl.HostnameVerifier() {
	        public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
	        	//return true;
	        	return hostname != null && (hostname.isEmpty() || hostname.length() >= 0);
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
			logger.error("error :: "+e1.toString());
		} catch (Exception e1) {
			logger.error("error :: "+e1.toString());
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

		//if (requestData != null)
		if (requestMethod.equals("POST") || requestMethod.equals("PUT"))
		{
			//BufferedReader reader = null;
			StringBuilder results = new StringBuilder();
	        String line;
	        
			try {
				if (requestData != null) {
					/*OutputStreamWriter wr = new OutputStreamWriter(httpsCon.getOutputStream());
					wr.write(requestData);
					wr.flush();
					wr.close();
					*/
					
					try (OutputStreamWriter wr = new OutputStreamWriter(httpsCon.getOutputStream())) {
					    wr.write(requestData);
					    wr.flush();
					    wr.close();
					}
					
					
					//reader = new BufferedReader(new InputStreamReader(httpsCon.getInputStream()));
					try(BufferedReader reader = new BufferedReader(new InputStreamReader(httpsCon.getInputStream()))){
						while ((line = reader.readLine()) != null) {
						    results.append(line);
						}
						reader.close();
						
						logger.info("result :: " + results);
					}
					resultMap.put("HttpsResponseDataMessage", results);
					
				}
				/* 
					HttpResult == 201, 정상적으로 들어갈 경우,
					HttpResult == 400, JSON 컬럼이 틀릴 경우
					HttpResult == 409, 스케줄 레이블이 중복일 경우
					HttpResult == 422, JSON 내용이 틀리 거나, start 시간이 과거 시간일 경우
				*/
				resultMap.put("HttpsResponseCode", httpsCon.getResponseCode());
				resultMap.put("HttpsResponseMessage", httpsCon.getResponseMessage());
				
				logger.info("resultMap :: " + resultMap.toString());
		        httpsCon.disconnect();
		        return resultMap;
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("error :: "+e.toString());
			} catch (Exception e) {
				logger.error("error :: "+e.toString());
			}
		}

		
		//BufferedReader reader = null;
		StringBuilder results = new StringBuilder();
        //String line;
        
        try {
        	logger.info(httpsCon.getResponseCode() + " Code");
        	logger.info(httpsCon.getResponseMessage().toString());
        	
        	if(httpsCon.getResponseCode() == 401) {
        		resultMap.put("HttpsResponseCode", httpsCon.getResponseCode());
        		resultMap.put("HttpsResponseMessage", "개인정보 검출관리 센터(PICenter) 서버 연결에 문제가 있습니다. 관리자에게 연락 주시기 바랍니다.");
        		httpsCon.disconnect();
        		return resultMap;
        	}else if(httpsCon.getResponseCode() == 404) {
        		resultMap.put("HttpsResponseCode", httpsCon.getResponseCode());
        		resultMap.put("HttpsResponseMessage", "개인정보 검출관리 센터(PICenter) 서버에 결과가 더이상 존재하지 않습니다. 검출 대상서버에 파일이 삭제/암호화 된 이후 검색이 수행 된 경우 이런 증상이 발생할 수 있습니다. 추가 문의사항은 관리자에게 연락 주시기 바랍니다.");
        		/*resultMap.put("HttpsResponseMessage", "검출 서버에 결과가 더 이상 존재하지 않습니다. 파일이 삭제/암호화 된 이후 검색이 수행 된 경우 이런 증상이 발생할 수 있습니다.");*/
        		httpsCon.disconnect();
        		return resultMap;
        	}
        }catch (NullPointerException e) {
        	// TODO: handle exception
        	logger.info("e :: "+e.toString());
        }catch (Exception e) {
			// TODO: handle exception
        	logger.info("e :: "+e.toString());
		}
        
        boolean error = false;
        
        try {
        	try (InputStream httpStream = httpsCon.getInputStream()) {
	        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        	byte[] buffer = new byte[1024];
	        	int len;
	
	        	while ((len = httpStream.read(buffer)) != -1) {
	        	    baos.write(buffer, 0, len);
	        	}
	        	byte[] responseBytes = baos.toByteArray();
	
	        	// GZIP 매직 넘버 확인 (1f 8b)
	        	if (responseBytes.length >= 2 && responseBytes[0] == (byte)0x1f && responseBytes[1] == (byte)0x8b) {
	        	    logger.info("⚠️ GZIP 헤더 확인됨, footer 없이 복원 시도 중...");
	        	    
	        	    // GZIP 헤더 10바이트 스킵
	        	    int gzipHeaderLength = 10;
	        	    byte[] gzipBody = new byte[responseBytes.length - gzipHeaderLength];
	        	    System.arraycopy(responseBytes, gzipHeaderLength, gzipBody, 0, gzipBody.length);
	
	        	    Inflater inflater = new Inflater(true); // true: zlib 헤더 없음
	        	    inflater.setInput(gzipBody);
	
	        	    ByteArrayOutputStream decompressed = new ByteArrayOutputStream();
	        	    byte[] outBuf = new byte[2048];
	
	        	    try {
	        	        while (!inflater.finished() && !inflater.needsInput()) {
	        	            int count = inflater.inflate(outBuf);
	        	            if (count == 0) break;  // 안전장치
	        	            decompressed.write(outBuf, 0, count);
	        	        }
	        	        inflater.end();
	
	        	        String recoveredJson = decompressed.toString("UTF-8");
	        	        logger.info("✅ 복원 성공 일부 데이터:\n" + recoveredJson.substring(0, Math.min(recoveredJson.length(), 500)));
	        	        results.append(recoveredJson);
	
	        	    } catch (DataFormatException e) {
	        	        logger.error("❌ 복원 실패 - 압축 포맷 오류", e);
	        	        resultMap.put("HttpsResponseMessage", "압축 해제 실패 (DataFormatException)");
	        	        error = true;
	        	    }
	
	        	} else {
	        	    logger.warn("GZIP이 아님, 일반 스트림으로 처리");
	        	    results.append(new String(responseBytes, "UTF-8"));
	        	}
        	}

        } catch (OutOfMemoryError e) {
            logger.error("❌ 메모리 부족 오류 발생", e);
            resultMap.put("HttpsResponseMessage", "서버 메모리 부족");
            error = true;
        } catch (RuntimeException e) {
            logger.error("❌ 런타임 예외 발생", e);
            resultMap.put("HttpsResponseMessage", "처리 중 런타임 예외 발생");
            error = true;
        } catch (Exception e) {
            logger.error("전체 응답 처리 실패", e);
            resultMap.put("HttpsResponseCode", 500);
            resultMap.put("HttpsResponseMessage", "응답 처리 오류");
            error = true;
        }

		logger.info("---------------------------------------------");
        //logger.info(results.toString());
        
		// Close Connection
        httpsCon.disconnect();
		
//		/* 조회 contents 가져오기 */
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder = null;
//        InputSource is;
//        Document doc = null;
//        try {
//			builder = factory.newDocumentBuilder();
//		} catch (ParserConfigurationException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//			resultMap.put("HttpsResponseCode", -1);
//			resultMap.put("HttpsResponseMessage", "ParserConfigurationException");
//			resultMap.put("HttpsResponseData", null);
//	        return resultMap; 
//		}
//        logger.info(results.toString());
//        
//        is = new InputSource(new StringReader(results.toString()));
//        try {
//			doc = builder.parse(is);
//		} catch (SAXException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//			resultMap.put("HttpsResponseCode", -1);
//			resultMap.put("HttpsResponseMessage", "SAXException");
//			resultMap.put("HttpsResponseData", null);
//	        return resultMap; 
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//			resultMap.put("HttpsResponseCode", -1);
//			resultMap.put("HttpsResponseMessage", "IOException");
//			resultMap.put("HttpsResponseData", null);
//	        return resultMap; 
//		}
//        
//        /* 조회 contents 가져오기  끝 */
        if(!error) {
			resultMap.put("HttpsResponseCode", 200);
			resultMap.put("HttpsResponseMessage", "OK");
			resultMap.put("HttpsResponseData", results.toString());
        }
        return resultMap; 
    }

}
