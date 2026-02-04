package com.org.iopts.exception.dao;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface piManageSearchApprovalListDAO {

	List<HashMap<String, Object>> searchApprovalListData(HashMap<String, Object> params);

	List<HashMap<String, Object>> selectProcessGroupPath(HashMap<String, Object> params);

	List<HashMap<String, Object>> searchApprovalAllListData(HashMap<String, Object> params);

}
