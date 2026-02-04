package com.org.iopts.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
/**
 * class DataUtil
 * Raonmind Common DataUtil
 * @author Raonmind
 */

@Service("DataUtil")
public class DataUtil
{
	private static final Logger logger = LoggerFactory.getLogger(DataUtil.class);
	
	public static Map<String,Object> convertJSON2MAP(JsonObject jsonObject)
	{
		if (jsonObject == null) return null;

		Map<String,Object> map = new HashMap<String,Object>();
		boolean error = false;
		try {
			for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
			    String mapKey = entry.getKey();
			    map.put(mapKey, entry.getValue());
			}
		}
		catch (JsonParseException e) {
			System.out.println("########### JSON Convert Exception ###########");
			System.out.println("json input string = " + jsonObject.toString());
			map.put("resultCode", -99999);
			map.put("resultMessage", "JSON Convert Exception");
			error = true;
		}
		
		if(!error) map.put("resultCode", 0);
        return map;
	}

	public static String makeEncFileName() throws IOException
	{
		// 파일명 변경 -- nano time으로 생성
		Long fileNameLast = System.nanoTime();
		
		String renameFile = fileNameLast.toString();

        // 파일명 암호화 
		char[] chars = renameFile.toCharArray();
		StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            hex.append(Integer.toHexString((int) chars[i]));
        }
        
        return hex.toString();
	}
	
}