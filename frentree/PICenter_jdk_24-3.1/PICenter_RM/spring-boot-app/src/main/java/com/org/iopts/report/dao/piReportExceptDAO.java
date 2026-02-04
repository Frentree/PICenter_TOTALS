package com.org.iopts.report.dao;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface piReportExceptDAO {

	public List<HashMap<String, Object>> searchExceptionList(HashMap<String, Object> params) throws SQLException;
}