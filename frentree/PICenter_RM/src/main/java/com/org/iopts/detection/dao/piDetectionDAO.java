package com.org.iopts.detection.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface piDetectionDAO {

	public List<HashMap<String, Object>> selectFindSubpath(HashMap<String, Object> params) throws SQLException;
	
	public HashMap<String, Object> selectDMZTime() throws SQLException;
	
	public void registPathCharge(HashMap<String, Object> insertExcepMap) throws SQLException;
	
	public List<HashMap<String, Object>> selectTeamMember(HashMap<String, Object> params) throws SQLException;
	
	public void registChange(HashMap<String, Object> insertExcepMap) throws SQLException;

	public List<HashMap<String, Object>> selectHashId(HashMap<String, Object> params) throws SQLException;

	public List<HashMap<String, Object>> selectFindSubpath2(HashMap<String, Object> params) throws SQLException;
	
	public List<HashMap<String, Object>> subpathSelect(HashMap<String, Object> params) throws SQLException;


	
}
