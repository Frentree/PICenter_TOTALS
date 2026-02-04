package com.org.iopts.mockup.dao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface MockupDAO {
	List<Map<String, Object>> allTargetList(Map<String, Object> params) throws Exception;
	List<Map<String, Object>> getServerColumns() throws Exception;
	List<Map<String, Object>> customPatterns() throws Exception;
	List<Map<String, Object>> getAllServerDataWithDetails(Map<String, Object> params) throws Exception;
}
