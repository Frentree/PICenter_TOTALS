package com.org.iopts.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.org.iopts.dto.Pi_Scan_HostVO;

import jakarta.annotation.Resource;

@Repository
public class Pi_ScanDAO {	

	private static final Logger logger = LoggerFactory.getLogger(Pi_ScanDAO.class);

	@Autowired
    @Resource(name = "sqlSession")
    private SqlSession sqlSession;

	private static final String Namespace = "ScanMapper";
	
	public List<Pi_Scan_HostVO> selectScanHost() throws Exception {
		return sqlSession.selectList(Namespace+".selectScanHost");
	}

	public int getConnectHistSeq() {
		return sqlSession.selectOne(Namespace + ".getConnectHistSeq");
	}
	
	public int insConnectHist(Map<String, String> map) {
		return sqlSession.insert(Namespace+".insConnectHist", map);
	}

	public boolean uptConnectHist(Map<String, String> map) {
		return (sqlSession.update(Namespace+".uptConnectHist", map) > 0);
	}

	public String getTargetName(String target_id) {
		return sqlSession.selectOne(Namespace + ".getTargetName", target_id);
	}

	public List<Map<String, Object>> selectGroupList(Map<String, Object> map) throws Exception {
		return sqlSession.selectList(Namespace + ".selectGroupList", map);
	}
	
	public List<Map<String, Object>> getApList() {
		return sqlSession.selectList(Namespace + ".getApList");
	}

}