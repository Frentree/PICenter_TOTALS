package com.org.iopts.popup.service;

import java.util.List;
import java.util.Map;

public interface PopupService {

	List<Map<String, Object>> selectGroupList(Map<String, Object> map) throws Exception;

	List<Map<String, Object>> selectNoGroupList() throws Exception;

	List<Map<String, Object>> getTargetList(Map<String, Object> map) throws Exception;
	
}
