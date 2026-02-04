package com.org.iopts.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.org.iopts.dto.Pi_AgentVO;
import com.org.iopts.dto.Pi_Custom_PatternVO;

import jakarta.annotation.Resource;

@Repository
public class Pi_Custom_PatternDAO {

	private static final Logger logger = LoggerFactory.getLogger(Pi_Custom_PatternDAO.class);

	@Autowired
    @Resource(name = "sqlSession")
    private SqlSession sqlSession;

	private static final String Namespace = "customPatternMapper";
	
	public List<Pi_Custom_PatternVO> selectCustomPattern() throws Exception {
		return sqlSession.selectList(Namespace+".selectCustomPattern");
	}


}
