package com.org.iopts.otp.dao;

import java.sql.SQLException;
import java.util.Map;

public interface OTPDAO {

	Map<String, Object> selectMember(Map<String, Object> searchMap) throws SQLException;

	int UserOTPInsert(Map<String, Object> searchMap) throws SQLException;

	
}
