package com.org.iopts.popup.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PopupDAO {

	List<Map<String, Object>> selectGroupList(Map<String, Object> map) throws SQLException;

	List<Map<String, Object>> selectNoGroupList() throws SQLException;

	List<Map<String, Object>> getTargetList(Map<String, Object> map) throws SQLException;
	
	void updateTargetUser(Map<String, Object> map) throws SQLException;

	void updateTargetUserlog(Map<String, Object> map);

	void updateUserGrade(Map<String, Object> map);
}
