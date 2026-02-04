package com.org.iopts.samsung.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;

public interface piSamsungDAO {

	public Map<String, Object> checkMemberGrade(Map<String, Object> searchMap) throws SQLException;

	public Map<String, Object> SSOSelectMember(Map<String, Object> searchMap) throws SQLException;
	
}