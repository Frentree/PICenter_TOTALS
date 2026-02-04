  package com.org.iopts.statistics.dao;

  import java.sql.SQLException;
  import java.util.ArrayList;
  import java.util.HashMap;
  import java.util.List;
  import java.util.Map;

  import com.org.iopts.group.vo.GroupTargetVo;
  import com.org.iopts.statistics.vo.StatisticsVo;

  public interface StatisticsDAO {

        List<Map<String, Object>> statisticsList(Map<String, Object> param);

        List<Map<String, Object>> manageList(Map<String, Object> result);

        List<Map<String, Object>> manageBarList(Map<String, Object> result);

        List<Map<String, Object>> mainChartStatistics(Map<String, Object> result);

        List<Map<String, Object>> TOPGridList(Map<String, Object> result);

        List<Map<String, Object>> totalStatistics(Map<String, Object> params);

        Map<String, Object> statisticsPolicyList(Map<String, Object> params);

        List<HashMap<String, Object>> excelDown(HashMap<String, Object> params) throws SQLException;

  }