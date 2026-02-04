package com.org.iopts.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.org.iopts.service.Pi_ExceptionServiceImpl;

import jakarta.annotation.Resource;

@Repository
public class Pi_ExceptionDAO {	

	@Autowired
    @Resource(name = "sqlSession")
    private SqlSession sqlSession;
	
	private static final String Namespace = "exception";
	
	private static Logger logger = LoggerFactory.getLogger(Pi_ExceptionServiceImpl.class);
	
	public List<Map<String, Object>> selectFindSubpath(Map<String, Object> map) throws Exception {

		logger.info("selectFindSubpath DAO");
		logger.info(map.toString());
		
		return sqlSession.selectList(Namespace+".selectFindSubpath", map);
	}
	
	public Map<String, Object> getTargetByNode(Map<String, Object> daoMap) throws Exception {

		return sqlSession.selectOne(Namespace+".getTargetByNode", daoMap);
	}
	
	public List<Map<String, Object>> selectExceptionList(Map<String, Object> map) throws Exception {

		return sqlSession.selectList(Namespace+".selectExceptionList", map);
	}
	
	public List<Map<String, Object>> selectExceptionApprList(Map<String, Object> map) throws Exception {

		return sqlSession.selectList(Namespace+".selectExceptionApprList", map);
	}
	
	public List<Map<String, Object>> selectDeletionList(Map<String, Object> map) throws Exception {

		return sqlSession.selectList(Namespace+".selectDeletionList", map);
	}


}
