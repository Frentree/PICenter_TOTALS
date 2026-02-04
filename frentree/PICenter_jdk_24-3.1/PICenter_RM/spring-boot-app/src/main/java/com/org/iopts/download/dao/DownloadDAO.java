package com.org.iopts.download.dao;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface DownloadDAO {
	int selectDownloadIndex() throws Exception;
	
	void insertDownload(Map<String, Object> map) throws Exception;

	int selectFileDownloadIndex() throws Exception;

	void insertFileDownload(Map<String, Object> map) throws Exception;

	int selectExcelDownloadIndex() throws SQLException;
	
	List<Map<String, Object>> downloadList(Map<String, Object> map) throws Exception;

	void insertFileDownLoadStatus(Map<String, Object> downMap) throws SQLException;
}
