package com.org.iopts.exception.dao;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface piManageSearchListDAO {
	
	public List<HashMap<String, Object>> searchProcessList(HashMap<String, Object> params) throws SQLException;

	public List<HashMap<String, Object>> selectProcessPath(HashMap<String, Object> params) throws SQLException;
	
}
