package com.org.iopts.lotte.dao;

import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.org.iopts.dto.MemberVO;

@Repository
public class LotteDAO {

	static final Logger logger = LoggerFactory.getLogger(LotteDAO.class);

	@Autowired
	@Resource(name = "sqlSession")
	private SqlSession sqlSession;

	static final String Namespace = "com.org.iopts.mapper.LotteMapper";

	
	
}
