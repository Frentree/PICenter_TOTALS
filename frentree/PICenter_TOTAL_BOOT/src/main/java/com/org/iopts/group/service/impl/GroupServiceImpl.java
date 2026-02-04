package com.org.iopts.group.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.iopts.group.dao.GroupDAO;
import com.org.iopts.group.service.GroupService;
import com.org.iopts.group.vo.DashScheduleServerVo;
import com.org.iopts.group.vo.GroupPCTargetVo;
import com.org.iopts.group.vo.GroupTargetVo;
import com.org.iopts.group.vo.GroupTomsVo;
import com.org.iopts.group.vo.GroupTreeServerVo;
import com.org.iopts.group.vo.GroupTreeVo;
import com.org.iopts.group.vo.PCGroupVo;
import com.org.iopts.group.vo.ScheduleServerVo;
import com.org.iopts.group.vo.SubPathVo;
import com.org.iopts.util.SessionUtil;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {

	private static Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);
	@Inject
	private GroupDAO dao;

	@Override
	public String selectUserGroupList(Map<String, Object> map, HttpServletRequest request) throws Exception {
		logger.info("selectUserGroupList");
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");

		map.put("user_no", user_no);
		map.put("user_grade", user_grade);
		// TODO Auto-generated method stub
		String icon = "";
		String name = "";
		JsonObject data = new JsonObject();

		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		
		jServerObject.addProperty("id", "target");
		jServerObject.addProperty("text", "대상");
		jServerObject.addProperty("parent", "#");
		data.addProperty("type", "0");
		jServerObject.add("data", data);
		jArr.add(jServerObject);

		/*jServerObject.put("id", "mypc");
		jServerObject.put("text", "내 PC");
		jServerObject.put("parent", "pc");
		jArr.add(jServerObject);*/
		
		/*List<GroupPCManagerVO> myPcList = null; // dao.selectMyPcList(map);
		for (GroupPCManagerVO vo : myPcList) {
			JSONObject jSTObject = new JSONObject();
			name = vo.getName();
			String mac_name = vo.getMac_name();
			String platform = vo.getPlatform();
			
			if (!vo.getAgent_connected_ip().equals("")) {
				if(platform.substring(0,5).equals("Apple")) {
					mac_name += "(" + vo.getAgent_connected_ip() + ")";
				}else {
					name += "(" + vo.getAgent_connected_ip() + ")";
				}
			}
			
			jSTObject.put("id", vo.getTARGET_ID());
			if(platform.substring(0,5).equals("Apple")) {
				jSTObject.put("text", mac_name);
			}else {
				jSTObject.put("text", name);
			}
			jSTObject.put("parent", "mypc");
			jSTObject.put("connected", vo.getConnected());
			
			if(vo.getConnected().equals("1") && vo.getConnected() != null) {
				icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
			} else {
				icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
			}
			jSTObject.put("icon", icon);
			
			jArr.add(jSTObject);
		} */
		
		/*List<GroupTargetVo> list = dao.selectUserPCTargets(map);*/
		List<GroupTargetVo> list = dao.selectUserTargets(map);
		for (GroupTargetVo vo : list) {
			name = vo.getName();
			/*String platform = vo.getPlatform();
			String mac_name = vo.getMac_name();*/
			if (!vo.getAgent_connected_ip().equals("")) {
				name += "(" + vo.getAgent_connected_ip() + ")";
			}

			if (vo.isAgent_connected() == true) {
				icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
			} else {
				icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
			}
			
			data.addProperty("ap", String.valueOf(vo.getAp_no()));
			data.addProperty("type", "1");
			JsonObject jSTObject = new JsonObject();
			
			jSTObject.addProperty("id", vo.getTarget_id());
			/*if(platform.substring(0,5).equals("Apple")) {
				jSTObject.put("text", mac_name);
			}else {
				jSTObject.put("text", name);
			}*/
			jSTObject.addProperty("text", name);
			jSTObject.addProperty("parent", "target");
			jSTObject.addProperty("icon", icon);
			jSTObject.add("data", data);
			jArr.add(jSTObject);
		}
			

		logger.info(jArr.toString());

		return jArr.toString();
	}
	
	@Override
	public String selectUserListGroup(Map<String, Object> map, HttpServletRequest request) throws Exception {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		map.put("user_no", user_no);
		map.put("user_grade", user_grade);
		// TODO Auto-generated method stub
		String icon = "";
		String name = "";
		JsonObject data = new JsonObject();
		
		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		
		if("9".equals(user_grade) || "4".equals(user_grade) || "5".equals(user_grade) || "6".equals(user_grade) || "7".equals(user_grade)) {
			jServerObject.addProperty("id", "server");
			jServerObject.addProperty("text", "서버");
			jServerObject.addProperty("parent", "#");
			jArr.add(jServerObject);

			jServerObject.addProperty("id", "pc");
			jServerObject.addProperty("text", "PC");
			jServerObject.addProperty("parent", "#");
			jArr.add(jServerObject);
		} else {
			jServerObject.addProperty("id", "pc");
			jServerObject.addProperty("text", "PC");
			jServerObject.addProperty("parent", "#");
			jArr.add(jServerObject);
		} 
		
		if (user_grade.equals("9")) {
			/*jServerObject.put("id", "onedrive");
			jServerObject.put("text", "OneDrive");
			jServerObject.put("parent", "#");
			jArr.add(jServerObject);*/
			
			jServerObject.addProperty("id", "sktoa");
			jServerObject.addProperty("text", "SKT(OA망)");
			jServerObject.addProperty("parent", "pc");
			jArr.add(jServerObject);
			
			jServerObject.addProperty("id", "sktut");
			jServerObject.addProperty("text", "SKT(유통망)");
			jServerObject.addProperty("parent", "pc");
			jArr.add(jServerObject);
			
			
			
			// 서버 리스트
			List<GroupTomsVo> groupServer = dao.selectTomsGroupList(map);
			for (GroupTomsVo vo : groupServer) {
				name = vo.getName();
				
				// 서버 타겟 오브젝트
				JsonObject jSTObject = new JsonObject();
				jSTObject.addProperty("id", vo.getIdx());
				
				// 상위 그룹 일 경우
				if (vo.getUp_idx().equals("0")) {
					jSTObject.addProperty("parent", "server");
				} else { // 하위 그룹
					jSTObject.addProperty("parent", vo.getUp_idx());
					
					data.addProperty("target_id", vo.getTarget_id());
					
					// 서버 (그룹x)
					if (vo.getTarget_id() != null && !vo.getTarget_id().equals("")) {
						icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
					} else {
						name += "(미설치)";
						icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
					}
					data.addProperty("name", vo.getName());
					jSTObject.addProperty("icon", icon);
				}
				data.addProperty("type", vo.getType());
				jSTObject.add("data", data);
				jSTObject.addProperty("text", name);
				
				jArr.add(jSTObject);
			}
			
			jServerObject.addProperty("id", "noGroup");
			jServerObject.addProperty("text", "미분류");
			jServerObject.addProperty("parent", "server");
			jArr.add(jServerObject);
			
			List<GroupTreeVo> group = dao.selectUserGroupList(map);
			/* map.put("group", ""); */
			// 그룹 pc 포함 여부
			for (GroupTreeVo vo : group) {
				JsonObject jSTObject = new JsonObject();
				
				jSTObject.addProperty("id", vo.getIdx());
				jSTObject.addProperty("text", vo.getName());
				jSTObject.addProperty("parent", vo.getUp_idx());
				jSTObject.addProperty("type", vo.getType());
				jSTObject.addProperty("connected", vo.getConnected());
				
				if (vo.getType() == 1) {
					if(!vo.getConnected().equals("") && vo.getConnected() != null) {
						icon = request.getContextPath() + "/resources/assets/images/img_pc_con.png";
					} else {
						icon = request.getContextPath() + "/resources/assets/images/img_pc_dicon.png";
					}
					jSTObject.addProperty("icon", icon);
				} 
				
				jArr.add(jSTObject);
			}
			
			// PC 그룹
			/*map.put("group", "");
			List<GroupPCTargetVo> pcTarget = dao.selectPCTarget(map);
			for (GroupPCTargetVo vo : pcTarget) {
				JSONObject jSTObject = new JSONObject();
				name = vo.getUser_name();
				if (!vo.getAgent_connected_ip().equals("")) {
					name += "(" + vo.getAgent_connected_ip() + ")";
				}

				if (vo.isAgent_connected() == true) {
					icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
				} else {
					icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
				}
				data.put("type", "1");
				jSTObject.put("data", data);

				jSTObject.put("id", vo.getTarget_id());
				jSTObject.put("text", name);
				jSTObject.put("parent", vo.getInsa_code());
				jSTObject.put("icon", icon);

				jArr.add(jSTObject);
			}*/
			
			jServerObject.addProperty("id", "noGroupPC");
			jServerObject.addProperty("text", "미분류");
			jServerObject.addProperty("parent", "pc");
			jArr.add(jServerObject);
			
			map.put("group", "pc");
			List<GroupPCTargetVo> pcTarget = dao.selectPCTarget(map);
			for (GroupPCTargetVo vo : pcTarget) {
				JsonObject jSTObject = new JsonObject();
				String platform = vo.getPlatform();
				name = vo.getName();
				String mac_name = vo.getMac_name();
				if (!vo.getAgent_connected_ip().equals("")) {
					if(platform.substring(0,5).equals("Apple")) {
						mac_name += "(" + vo.getAgent_connected_ip() + ")";
					}else {
						name += "(" + vo.getAgent_connected_ip() + ")";
					}
				}
				
				if (vo.isAgent_connected() == true) {
					icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
				} else {
					icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
				}
				
				data.addProperty("ap", vo.getAp_no());
				data.addProperty("type", "1");
				jSTObject.add("data", data);
				
				jSTObject.addProperty("id", vo.getTarget_id());
				if(platform.substring(0,5).equals("Apple")) {
					jSTObject.addProperty("text", mac_name);
				}else {
					jSTObject.addProperty("text", name);
				}
				jSTObject.addProperty("icon", icon);
				jSTObject.addProperty("parent", "noGroupPC");
				jArr.add(jSTObject);
			}
		} 
		
		/*logger.info(jArr.toString());*/
		
		return jArr.toString();
	}
	
	

	@Override
	public String selectServerGroupList(Map<String, Object> map, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		map.put("user_no", user_no);
		map.put("ap_no", 0);
		String icon = "";
		String name = "";
		JsonObject data = new JsonObject();

		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		jServerObject.addProperty("id", "server");
		jServerObject.addProperty("text", "서버");
		jServerObject.addProperty("parent", "#");
		data.addProperty("ap", 0);
		data.addProperty("type", "0");
		jServerObject.add("data", data);
		jServerObject.addProperty("parent", "#");
		jArr.add(jServerObject);
		

		if("9".equals(user_grade)) {
			
			// 서버 리스트
			List<ScheduleServerVo> groupServer = dao.selectScheduleServerList(map);
			for (ScheduleServerVo vo : groupServer) {
				name = vo.getName();
				if (!vo.getAgent_connected_ip().equals("")) {
					name += " / " + vo.getAgent_connected_ip();
					// name = "<a href='#' class='targets' data-apno='0'
					// data-targetid="+vo.getIdx()+">" + name + "</a>";
				}
				
				// 서버 타겟 오브젝트
				JsonObject jSTObject = new JsonObject();
				
				if (vo.getUp_idx().equals("0")) {
					jSTObject.addProperty("parent", "server");
				} else {
					jSTObject.addProperty("parent", vo.getUp_idx());
				}
					
				data.addProperty("ap", 0);
				data.addProperty("type", vo.getType());
				
				
				if (vo.getType() == 1) {
					if (vo.getAgent_connected_chk() == 1) {
						icon = request.getContextPath() + "/resources/assets/images/db.png";
					} else {
						if(!vo.isAgent_connected()) {
							icon = request.getContextPath() + "/resources/assets/images/server_dicon.png";
						}else {
							icon = request.getContextPath() + "/resources/assets/images/server_icon.png";
						}
					}
					
					
					if(vo.getComdate() != null && !vo.getComdate().equals("")) {
						name += " / " + vo.getComdate() ;
					}else {
						name += " / 미검색";
					}
					
					if(!vo.isAgent_connected()) {
						//name += " (연결안됨)"; 
					}
					
					jSTObject.addProperty("icon", icon);
					data.addProperty("name", vo.getName());
					data.addProperty("core", vo.getCores());
					data.addProperty("targets", vo.getTarget_id());
					data.addProperty("location", vo.getLocation_id());
					data.addProperty("agent_connected", vo.isAgent_connected());
				}

				
				jSTObject.addProperty("id", vo.getIdx());
				jSTObject.addProperty("text", name);
				jSTObject.add("data", data);
				jArr.add(jSTObject);
			}
			

			jServerObject.addProperty("id", "noGroup");
			jServerObject.addProperty("text", "미분류");
			data.addProperty("ap", 0);
			data.addProperty("type", 0);
			jServerObject.add("data", data);
			jServerObject.addProperty("parent", "server");
			jArr.add(jServerObject);
			
			jServerObject.addProperty("id", "group");
			jServerObject.addProperty("text", "그룹");
			jServerObject.addProperty("parent", "#");
			data.addProperty("ap", 0);
			data.addProperty("type", 3);
			jServerObject.add("data", data);
			jServerObject.addProperty("parent", "#");
			jArr.add(jServerObject);

			/*// 서버 미분류 그룹 추가
			List<ScheduleServerNotTargetVo> notGroup = dao.selectScheduleServerNotGroup(map);
			for (ScheduleServerNotTargetVo vo : notGroup) {
				name = vo.getName();
				if (!vo.getAgent_connected_ip().equals("")) {
					name += "(" + vo.getAgent_connected_ip() + ")";
				}

				if (!vo.getTarget_use().equals("")) {
					name += vo.getTarget_use();
				}
				if (vo.getAgent_connected_chk() == 1) {
					icon = request.getContextPath() + "/resources/assets/images/db.png";
				} else {
					icon = request.getContextPath() + "/resources/assets/images/file.png";
				}
				data.put("ap", 0);
				data.put("type", "1");
				data.put("name", vo.getName());
				data.put("location", vo.getLocation_id());
				JSONObject jSTObject = new JSONObject();
				jSTObject.put("id", vo.getTarget_id());
				jSTObject.put("text", name);
				jSTObject.put("parent", "noGroup");
				jSTObject.put("icon", icon);
				jSTObject.put("data", data);
				jArr.add(jSTObject);
			}*/
		}

		return jArr.toString();
	}


	/*
	 * 대시보드 스케줄 그룹화
	 */
	@Override
	public JsonArray selectDashSeverDept(Map<String, Object> map, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		logger.info("selectSeverDashDept");

		JsonArray jArr = new JsonArray();
		
		JsonObject jServerObject = new JsonObject();
		jServerObject.addProperty("id", "server");
		jServerObject.addProperty("text", "서버");
		
		// "data" 내부 객체 생성
		JsonObject data = new JsonObject();
		data.addProperty("type", "0");
		
		// data 객체를 jServerObject 에 추가
		jServerObject.add("data", data);
		jServerObject.addProperty("parent", "#");

		// 배열에 추가
		jArr.add(jServerObject);
		
		// 리스트와 변수 초기화
		List<DashScheduleServerVo> group = null; // dao.selectDashSeverDept(map);
		List<String> targets = new ArrayList<>();
		String parentId = "";
		int count = 0;
		String name = "";

		// type, ap_no 수정 (기존 data를 업데이트하든 새로 만들어서 교체 가능)
		data.addProperty("type", "1");
		data.addProperty("ap_no", 0);
		
		// 예시 객체 (사용 시 Gson과 무관, 그대로 유지)
		DashScheduleServerVo one = null;

		// JSONObject 제거: JSONObject jSTObject = new JSONObject();
		JsonObject jSTObject = new JsonObject(); // Gson의 JsonObject로 대체

		return jArr;
	}

	/*@Override
	public JSONArray selectDashPCDept(Map<String, Object> map, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		logger.info("selectDashPCDept");
		Map<String, Object> data = new HashMap<String, Object>();

		JSONArray jArr = new JSONArray();
		JSONObject jServerObject = new JSONObject();

		jServerObject.put("id", "pc");
		jServerObject.put("text", "PC");
		data.put("type", "0");
		jServerObject.put("data", data);
		jServerObject.put("parent", "#");
		jArr.add(jServerObject);

		List<DashScheduleServerVo> group = dao.selectDashPCDept(map);
		List<String> targets = new ArrayList<>();
		String parentId = "";

		int count = 0;
		String name = "";

		data.put("type", "1");
		data.put("ap_no", 0);
		DashScheduleServerVo one = null;
		JSONObject jSTObject = new JSONObject();

		if (group.size() != 0) {
			count = 1;
			one = group.get(0);
			name = one.getRegdate() + "_";
			targets.add(one.getTarget_id());
			jSTObject.put("parent", "pc");
			jSTObject.put("id", one.getId());
			parentId = one.getId();

		}

		for (int i = 1; i < group.size(); i++) {
			DashScheduleServerVo vo = group.get(i);

			if (!parentId.equals(vo.getId())) {
				jSTObject.put("text", name + count + "대");
				data.put("targets", targets);
				jSTObject.put("data", data);
				jArr.add(jSTObject);
				targets.clear();
				count = 0;
			}
			jSTObject = new JSONObject();
			jSTObject.put("parent", "pc");
			jSTObject.put("id", vo.getId());
			targets.add(vo.getTarget_id());
			name = vo.getRegdate() + "_";

			count++;

			parentId = vo.getId();
		}

		if (group.size() != 0) {
			jSTObject.put("text", name + count + "대");
			data.put("targets", targets);
			jSTObject.put("data", data);
			jArr.add(jSTObject);
			count = 0;
		}

		logger.info("selectDashPCDept  >>> " + jArr.toString());

		return jArr;
	}*/

	@Override
	public JsonArray selectPCGroup(Map<String, Object> map, HttpServletRequest request) throws Exception {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");

		map.put("user_no", user_no);
		map.put("user_grade", user_grade);
		// TODO Auto-generated method stub
		String icon = "";
		String name = "";
		JsonObject data = new JsonObject();

		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();

		if ("9".equals(user_grade)) {
			jServerObject.addProperty("id", "server");
			jServerObject.addProperty("text", "서버");
			jServerObject.addProperty("parent", "#");
			jArr.add(jServerObject);

			jServerObject.addProperty("id", "pc");
			jServerObject.addProperty("text", "PC");
			jServerObject.addProperty("parent", "#");
			jArr.add(jServerObject);

			jServerObject.addProperty("id", "sktoa");
			jServerObject.addProperty("text", "SKT(OA망)");
			jServerObject.addProperty("parent", "pc");
			jArr.add(jServerObject);

			jServerObject.addProperty("id", "sktut");
			jServerObject.addProperty("text", "SKT(유통망)");
			jServerObject.addProperty("parent", "pc");
			jArr.add(jServerObject);

			// 서버 리스트
			List<GroupTreeServerVo> groupServer = dao.selectServerGroupList(map);
			for (GroupTreeServerVo vo : groupServer) {
				name = vo.getName();

				if (!vo.getAgent_connected_ip().equals("")) {
					name += "(" + vo.getAgent_connected_ip() + ")";
					// name = "<a href='#' class='targets' data-apno='0'
					// data-targetid="+vo.getIdx()+">" + name + "</a>";
				}

				// 서버 타겟 오브젝트
				JsonObject jSTObject = new JsonObject();
				jSTObject.addProperty("id", vo.getIdx());
				jSTObject.addProperty("text", name);
				if (vo.getUp_idx().equals("0")) {
					jSTObject.addProperty("parent", "server");
				} else
					jSTObject.addProperty("parent", vo.getUp_idx());
				data.addProperty("ap", "0");
				data.addProperty("type", vo.getType());
				if (vo.getType() == 1) {

					if (vo.isAgent_connected() == true) {
						icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
					} else {
						icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
					}
					jSTObject.addProperty("icon", icon);
				}
				jSTObject.add("data", data);
				jArr.add(jSTObject);
			}

			jServerObject.addProperty("id", "noGroup");
			jServerObject.addProperty("text", "미분류");
			jServerObject.addProperty("parent", "server");
			jArr.add(jServerObject);
			// 서버 미분류 그룹 추가
			List<GroupTargetVo> notGroup = dao.selectNotGroup(map);
			for (GroupTargetVo vo : notGroup) {
				name = vo.getName();
				if (!vo.getAgent_connected_ip().equals("")) {
					name += "(" + vo.getAgent_connected_ip() + ")";
				}

				if (vo.isAgent_connected() == true) {
					icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
				} else {
					icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
				}
				data.addProperty("ap", "0");
				data.addProperty("type", "1");
				JsonObject jSTObject = new JsonObject();
				jSTObject.addProperty("id", vo.getTarget_id());
				jSTObject.addProperty("text", name);
				jSTObject.addProperty("parent", "noGroup");
				jSTObject.addProperty("icon", icon);
				jSTObject.add("data", data);
				jArr.add(jSTObject);
			}

			List<PCGroupVo> group = dao.selectPCGroup(map);
			/* map.put("group", ""); */
			// 그룹 pc 포함 여부
			for (PCGroupVo vo : group) {
				JsonObject jSTObject = new JsonObject();
				jSTObject.addProperty("id", vo.getInsa_code());
				jSTObject.addProperty("text", vo.getName());
				jSTObject.addProperty("parent", vo.getUp_idx());
				// jSTObject.put("icon", request.getContextPath() +
				// "/resources/assets/images/file.png");

				if (vo.getType() == 1) { // 사용자
					if (vo.getT_cnt() != 0) {
						icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
						jSTObject.addProperty("icon", icon);
					} else {
						icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
					}
					jSTObject.addProperty("icon", icon);
				}

				data.addProperty("ap", 1);
				data.addProperty("type", vo.getType());
				jSTObject.add("data", data);

				jArr.add(jSTObject);
			}

		} else {
			List<GroupTargetVo> list = dao.selectUserTargets(map);
			for (GroupTargetVo vo : list) {
				name = vo.getName();
				if (!vo.getAgent_connected_ip().equals("")) {
					name += "(" + vo.getAgent_connected_ip() + ")";
				}

				if (vo.isAgent_connected() == true) {
					icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
				} else {
					icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
				}
				data.addProperty("ap", String.valueOf(vo.getAp_no()));
				data.addProperty("type", "1");
				JsonObject jSTObject = new JsonObject();
				jSTObject.addProperty("id", vo.getTarget_id());
				jSTObject.addProperty("text", name);
				jSTObject.addProperty("parent", "#");
				jSTObject.addProperty("icon", icon);
				jSTObject.add("data", data);
				jArr.add(jSTObject);
			}

		}

		/*logger.info("selectPCGroup >> " + jArr.toString());*/

		return jArr;
	}
	
	@Override
	public JsonArray selectDashDeptList(Map<String, Object> map, HttpServletRequest request) throws Exception {
		logger.info("selectDashDeptList");
		
		JsonObject data = new JsonObject();

		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();

		jServerObject.addProperty("id", "server");
		jServerObject.addProperty("text", "서버");
		data.addProperty("type", "0");
		jServerObject.add("data", data);
		jServerObject.addProperty("parent", "#");
		jArr.add(jServerObject);
		
		jServerObject.addProperty("id", "pc");
		jServerObject.addProperty("text", "PC");
		data.addProperty("type", "0");
		jServerObject.add("data", data);
		jServerObject.addProperty("parent", "#");
		jArr.add(jServerObject);

		// 서버 리스트
		List<DashScheduleServerVo> groupSever = dao.selectDashSeverDept(map);
		JsonArray targetsSever = new JsonArray();
		String parentId = "";

		int count = 0;
		String name = "";

		data.addProperty("type", "1");
		data.addProperty("ap_no", 0);
		DashScheduleServerVo one = null;
		JsonObject jSTObjectSever = new JsonObject();

		if (groupSever.size() != 0) {
			count = 1;
			one = groupSever.get(0);
			name = one.getRegdate() + "_";
			targetsSever.add(one.getTarget_id());
			jSTObjectSever.addProperty("parent", "server");
			jSTObjectSever.addProperty("id", one.getId()+"_sever");
			parentId = one.getId();

		}

		for (int i = 1; i < groupSever.size(); i++) {
			DashScheduleServerVo vo = groupSever.get(i);

			if (!parentId.equals(vo.getId())) {
				jSTObjectSever.addProperty("text", name + count + "대");
				data.add("targets", targetsSever);
				jSTObjectSever.add("data", data);
				jArr.add(jSTObjectSever);
				targetsSever = new JsonArray();
				count = 0;
			}
			jSTObjectSever = new JsonObject();
			jSTObjectSever.addProperty("parent", "server");
			jSTObjectSever.addProperty("id", vo.getId()+"_sever");
			targetsSever.add(vo.getTarget_id());
			name = vo.getRegdate() + "_";

			count++;

			parentId = vo.getId();
		}

		if (groupSever.size() != 0) {
			jSTObjectSever.addProperty("text", name + count + "대");
			data.add("targets", targetsSever);
			jSTObjectSever.add("data", data);
			jArr.add(jSTObjectSever);
			count = 0;
		}
		
		// PC 리스트
		/*List<DashScheduleServerVo> group = dao.selectDashPCDept(map);
		List<String> targets = new ArrayList<>();

		data.put("type", "1");
		data.put("ap_no", 0);
		JSONObject jSTObject = new JSONObject();

		if (group.size() != 0) {
			count = 1;
			one = group.get(0);
			name = one.getRegdate() + "_";
			targets.add(one.getTarget_id());
			jSTObject.put("parent", "pc");
			jSTObject.put("id", one.getId()+"_pc");
			parentId = one.getId();

		}

		for (int i = 1; i < group.size(); i++) {
			DashScheduleServerVo vo = group.get(i);

			if (!parentId.equals(vo.getId())) {
				jSTObject.put("text", name + count + "대");
				data.put("targets", targets);
				jSTObject.put("data", data);
				jArr.add(jSTObject);
				targets.clear();
				count = 0;
			}
			jSTObject = new JSONObject();
			jSTObject.put("parent", "pc");
			jSTObject.put("id", vo.getId()+"_pc");
			targets.add(vo.getTarget_id());
			name = vo.getRegdate() + "_";

			count++;

			parentId = vo.getId();
		}

		if (group.size() != 0) {
			jSTObject.put("text", name + count + "대");
			data.put("targets", targets);
			jSTObject.put("data", data);
			jArr.add(jSTObject);
			count = 0;
		}*/
		
		return jArr;
		
	}

	// 그룹 데이터
	@Override
	public List<Map<String, Object>> selectGroup(Map<String, Object> map, HttpServletRequest request) throws Exception {
		return dao.selectGroup(map);
	}

	/*

	@Override
	public JSONArray selectTomsNotGroup(Map<String, Object> map, HttpServletRequest request) throws Exception {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");

		map.put("user_no", user_no);
		map.put("user_grade", user_grade);
		// TODO Auto-generated method stub
		String icon = "";
		String name = "";
		Map<String, Object> data = new HashMap<String, Object>();

		JSONArray jArr = new JSONArray();
		JSONObject jServerObject = new JSONObject();

		jServerObject.put("id", "noGroup");
		jServerObject.put("text", "미분류");
		jServerObject.put("parent", "#");

		data.put("type", 0);
		jServerObject.put("data", data);
		jArr.add(jServerObject);

		// 서버 리스트
		List<GroupTomsVo> groupServer = dao.selectTomsNotGroup(map);
		for (GroupTomsVo vo : groupServer) {
			name = vo.getName();

			// 서버 타겟 오브젝트
			JSONObject jSTObject = new JSONObject();
			jSTObject.put("id", vo.getIdx());
			jSTObject.put("parent", "noGroup");

			data.put("target_id", vo.getTarget_id());

			if (vo.getTarget_id() != null && !vo.getTarget_id().equals("")) {
				icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
			} else {
				name += "(미설치)";
				icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
			}
			data.put("name", vo.getName());
			jSTObject.put("icon", icon);
			
			
			data.put("type", vo.getType());
			jSTObject.put("data", data);
			jSTObject.put("text", name);

			jArr.add(jSTObject);
		}

		logger.info("selectTomsNotGroup >> " + jArr.toString());

		return jArr;
	}
	*/
	/*
	 * 보안 모듈이 생성한 그룹화
	 */	
	@Override
	public JsonArray selectUserCreateGroup(Map<String, Object> map, HttpServletRequest request) throws Exception {
		// 트리 아이콘 설정
		String icon = "";
		// 트리 이름 설정
		String name = "";
		JsonObject data = new JsonObject();

		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();

		jServerObject.addProperty("id", "group");
		jServerObject.addProperty("text", "그룹");
		jServerObject.addProperty("parent", "#");

		data.addProperty("type", 0);
		jServerObject.add("data", data);
		jArr.add(jServerObject);
		

		// 서버 리스트
		List<GroupTomsVo> groupServer = dao.selectTomsUserGroup(map);
		for (GroupTomsVo vo : groupServer) {
			name = vo.getName();

			// 서버 타겟 오브젝트
			JsonObject jSTObject = new JsonObject();
			jSTObject.addProperty("id", vo.getIdx());
			if (vo.getUp_idx().equals("0")) {
				jSTObject.addProperty("parent", "group");
			} else {
				jSTObject.addProperty("parent", vo.getUp_idx());

				data.addProperty("target_id", vo.getTarget_id());
				if(vo.getType() != 0) {
					jSTObject.addProperty("id", vo.getUp_idx() + "_" + vo.getIdx());
					if (vo.getTarget_id() != null && !vo.getTarget_id().equals("")) {
						icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
					} else {
						name += "(미설치)";
						icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
					}
				}
				data.addProperty("name", vo.getName());
				jSTObject.addProperty("icon", icon);
			}
			data.addProperty("type", vo.getType());
			jSTObject.add("data", data);
			jSTObject.addProperty("text", name);

			jArr.add(jSTObject);
		}

		return jArr;
	}


	/*
	 * 보안 모듈이 생성
	 */	
	@Override
	public JsonArray insertUserTargets(Map<String, Object> map, HttpServletRequest request) throws Exception {
		String groupArr = request.getParameter("groupArr");
		String treeArr = request.getParameter("treeArr");
		int result = -1;
		logger.info("Group move >> " + groupArr + ", treeArr > " + treeArr);
		
		// 선택한 toms 자산정보
		Gson gson = new Gson();
		JsonElement element = gson.toJsonTree(treeArr);
		JsonArray treeJArr = element.getAsJsonArray();

		// 선택한 group
		element = gson.toJsonTree(groupArr);
		JsonArray groupJArr = element.getAsJsonArray();
		
		for (int i = 0; i < groupJArr.size(); i++) {
		    String data = groupJArr.get(i).getAsString(); // Gson에서는 명시적 변환 필요

		    // 그룹에 저장
		    for (int j = 0; j < treeJArr.size(); j++) {
		        map.put("group_id", data);
		        map.put("toms_id", treeJArr.get(j).getAsString()); // 여기도 마찬가지

		        dao.insertUserTargets(map);
		    }
		}
		

		return selectUserCreateGroup(map, request);
	}
	
	
	
	@Override
	public String selectUserHostGroupList(Map<String, Object> map, HttpServletRequest request) throws Exception {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");

		map.put("user_no", user_no);
		map.put("user_grade", user_grade);
		// TODO Auto-generated method stub
		String icon = "";
		String name = "";
		JsonObject data = new JsonObject();

		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		
		if("9".equals(user_grade) || "4".equals(user_grade) || "5".equals(user_grade) || "6".equals(user_grade)) {
			jServerObject.addProperty("id", "server");
			jServerObject.addProperty("text", "서버");
			jServerObject.addProperty("parent", "#");
			jArr.add(jServerObject);

			jServerObject.addProperty("id", "pc");
			jServerObject.addProperty("text", "PC");
			jServerObject.addProperty("parent", "#");
			jArr.add(jServerObject);
		} else {
			jServerObject.addProperty("id", "pc");
			jServerObject.addProperty("text", "PC");
			jServerObject.addProperty("parent", "#");
			jArr.add(jServerObject);
		} 

		if ("9".equals(user_grade)) {
			jServerObject.addProperty("id", "db");
			jServerObject.addProperty("text", "DB");
			jServerObject.addProperty("parent", "#");
			jArr.add(jServerObject);
			
			jServerObject.addProperty("id", "onedirve");
			jServerObject.addProperty("text", "OneDrive");
			jServerObject.addProperty("parent", "#");
			jArr.add(jServerObject);

			jServerObject.addProperty("id", "sktoa");
			jServerObject.addProperty("text", "SKT(OA망)");
			jServerObject.addProperty("parent", "pc");
			jArr.add(jServerObject);

			jServerObject.addProperty("id", "sktut");
			jServerObject.addProperty("text", "SKT(유통망)");
			jServerObject.addProperty("parent", "pc");
			jArr.add(jServerObject);

			// 서버 리스트
			List<GroupTomsVo> groupServer = dao.selectTomsGroup(map);
			for (GroupTomsVo vo : groupServer) {
				name = vo.getName();

				// 서버 타겟 오브젝트
				JsonObject jSTObject = new JsonObject();
				jSTObject.addProperty("id", vo.getIdx());
				if(vo.getTarget_id() != null && !vo.getTarget_id().equals("")) {
					jSTObject.addProperty("id", vo.getTarget_id());
				} else {
					jSTObject.addProperty("id", vo.getIdx());
				}

				data.addProperty("ap", vo.getAp_no());
				if(vo.getPlatform().equals("Remote Access Only")) {
					
					// 상위 그룹 일 경우
					if (vo.getUp_idx().equals("0")) {
						jSTObject.addProperty("parent", "db");
					} else { // 하위 그룹
						jSTObject.addProperty("parent", "db");
						
						data.addProperty("target_id", vo.getTarget_id());
						icon = request.getContextPath() + "/resources/assets/images/db.png";
						
						data.addProperty("name", vo.getName());
						jSTObject.addProperty("icon", icon);
					}
				}else {
					// 상위 그룹 일 경우
					if (vo.getUp_idx().equals("0")) {
						jSTObject.addProperty("parent", "server");
					} else { // 하위 그룹
						jSTObject.addProperty("parent", vo.getUp_idx());
						
						data.addProperty("target_id", vo.getTarget_id());
						
						// 서버 (그룹x)
						if (vo.getTarget_id() != null && !vo.getTarget_id().equals("")) {
							icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
						} else {
							name += "(미설치)";
							icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
						}
						data.addProperty("name", vo.getName());
						jSTObject.addProperty("icon", icon);
					}
				}

				data.addProperty("type", vo.getType());
				jSTObject.add("data", data);
				jSTObject.addProperty("text", name);

				jArr.add(jSTObject);
			}

			jServerObject.addProperty("id", "noGroup");
			jServerObject.addProperty("text", "미분류");
			jServerObject.addProperty("parent", "server");
			jArr.add(jServerObject);
			jServerObject.addProperty("id", "noGroupPC");
			jServerObject.addProperty("text", "미분류");
			data.addProperty("ap", 0);
			data.addProperty("type", "0");
			jServerObject.add("data", data);
			jServerObject.addProperty("parent", "pc");
			jArr.add(jServerObject);
			
			logger.info("selectPCList");
			List<ScheduleServerVo> groupPC = dao.selectPCList(map);
			for(ScheduleServerVo vo : groupPC) {
				String platform = vo.getPlatform();
				
				if(vo.getAp_no() != 0 && platform.substring(0,5).equals("Apple") ) {
					name = vo.getMac_name();
				}else {
					name = vo.getName();
				}

				JsonObject jSTObject = new JsonObject();
				data.addProperty("ap", vo.getAp_no());
				data.addProperty("type", (vo.getType() != 1 ? 0 : 1));
				
				if (vo.getType() == 1) {
					if(vo.getUp_idx().equals("onedirve")) {
						icon = request.getContextPath() + "/resources/assets/images/onedrive16px.png";
						
					}else if (vo.getAgent_connected_chk() == 1) {
						icon = request.getContextPath() + "/resources/assets/images/db.png";
						data.addProperty("name", "onedrive");
					} else {
						icon = request.getContextPath() + "/resources/assets/images/pc_icon.png";
						data.addProperty("name", vo.getName());
					}
					
					if(!vo.isAgent_connected()) {
						name += " (연결안됨)"; 
					}
					jSTObject.addProperty("icon", icon);
					data.addProperty("location", vo.getLocation_id());
				} else if (vo.getType() == 2) {
					if (vo.getConnected() != 0) {
						icon = request.getContextPath() + "/resources/assets/images/img_pc_con.png";
					} else {
						icon = request.getContextPath() + "/resources/assets/images/img_pc_dicon.png";
					}
					jSTObject.addProperty("icon", icon);
				}
				jSTObject.addProperty("id", vo.getIdx());
				jSTObject.addProperty("text", name);
				jSTObject.addProperty("parent", vo.getUp_idx());
				jSTObject.add("data", data);
				jArr.add(jSTObject);

			}
			
			// 서버 미분류 그룹 추가
			List<GroupTargetVo> notGroup = dao.selectNotGroup(map);
			for (GroupTargetVo vo : notGroup) {
				name = vo.getName();
				if (!vo.getAgent_connected_ip().equals("")) {
					name += "(" + vo.getAgent_connected_ip() + ")";
				}

	/*			if(vo.getComdate() != null && !vo.getComdate().equals("")) {
                    name += "(" + vo.getComdate() + ")";
                }*/
				
				if (vo.isAgent_connected() == true) {
					icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
				} else {
					icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
				}
				data.addProperty("ap", "0");
				data.addProperty("type", "1");
				JsonObject jSTObject = new JsonObject();
				jSTObject.addProperty("id", vo.getTarget_id());
				jSTObject.addProperty("text", name);
				jSTObject.addProperty("parent", "noGroup");
				jSTObject.addProperty("icon", icon);
				jSTObject.add("data", data);
				jArr.add(jSTObject);
			}

			/*map.put("group", "pc");
			List<GroupPCTargetVo> pcTarget = dao.selectPCTarget(map);
			for (GroupPCTargetVo vo : pcTarget) {
				JSONObject jSTObject = new JSONObject();

				name = vo.getName();
				if (!vo.getAgent_connected_ip().equals("")) {
					name += "(" + vo.getAgent_connected_ip() + ")";
				}

				if (vo.isAgent_connected() == true) {
					icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
				} else {
					icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
				}

				data.put("ap", vo.getAp_no());
				data.put("type", "1");
				jSTObject.put("data", data);

				jSTObject.put("id", vo.getTarget_id());
				jSTObject.put("text", name);
				jSTObject.put("icon", icon);
				jSTObject.put("parent", "noGroupPC");
				jArr.add(jSTObject);
			}*/
		} else if("2".equals(user_grade) || "3".equals(user_grade)) {

			jServerObject.addProperty("id", "sktoa");
			jServerObject.addProperty("text", "SKT(OA망)");
			jServerObject.addProperty("parent", "pc");
			jArr.add(jServerObject);

			jServerObject.addProperty("id", "sktut");
			jServerObject.addProperty("text", "SKT(유통망)");
			jServerObject.addProperty("parent", "pc");
			jArr.add(jServerObject);
			
			logger.info("selectPCList");
			List<ScheduleServerVo> groupPC = dao.selectCenterAdminSchedule(map);
			for(ScheduleServerVo vo : groupPC) {
				name = vo.getName();
				
				if (!vo.getAgent_connected_ip().equals("")) {
					name += "(" + vo.getAgent_connected_ip() + ")";
				}

				JsonObject jSTObject = new JsonObject();
				data.addProperty("ap", vo.getAp_no());
				data.addProperty("type", (vo.getType() != 1 ? 0 : 1));
				
				if (vo.getType() == 1) {
					if(vo.getUp_idx().equals("onedirve")) {
						icon = request.getContextPath() + "/resources/assets/images/onedrive16px.png";
						
					}else if (vo.getAgent_connected_chk() == 1) {
						icon = request.getContextPath() + "/resources/assets/images/db.png";
						data.addProperty("name", "onedrive");
					} else {
						icon = request.getContextPath() + "/resources/assets/images/pc_icon.png";
						data.addProperty("name", vo.getName());
					}
					
					if(!vo.isAgent_connected()) {
						name += " (연결안됨)"; 
					}
					jSTObject.addProperty("icon", icon);
					data.addProperty("location", vo.getLocation_id());
				} else if (vo.getType() == 2) {
					if (vo.getConnected() != 0) {
						icon = request.getContextPath() + "/resources/assets/images/img_pc_con.png";
					} else {
						icon = request.getContextPath() + "/resources/assets/images/img_pc_dicon.png";
					}
					jSTObject.addProperty("icon", icon);
				}
				jSTObject.addProperty("id", vo.getIdx());
				jSTObject.addProperty("text", name);
				jSTObject.addProperty("parent", vo.getUp_idx());
				jSTObject.add("data", data);
				jArr.add(jSTObject);
			}
		} else {
			List<GroupTargetVo> list = dao.selectUserTargets(map);
			for (GroupTargetVo vo : list) {
				name = vo.getName();
				if (!vo.getAgent_connected_ip().equals("")) {
					name += "(" + vo.getAgent_connected_ip() + ")";
				}

				if (vo.isAgent_connected() == true) {
					icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
				} else {
					icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
				}
				data.addProperty("ap", String.valueOf(vo.getAp_no()));
				data.addProperty("type", "1");
				JsonObject jSTObject = new JsonObject();
				jSTObject.addProperty("id", vo.getTarget_id());
				jSTObject.addProperty("text", name);
				jSTObject.addProperty("parent", "#");
				jSTObject.addProperty("icon", icon);
				jSTObject.add("data", data);
				jArr.add(jSTObject);
			}

		}

		/*logger.info(jArr.toString());*/

		return jArr.toString();
	}
	

	@Override
	public JsonArray selectSubPath(Map<String, Object> map, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		logger.info("selectSubPath");
		

		JsonArray jArr = new JsonArray();

		
		String hash_id = request.getParameter("hash_id");
		String tid = request.getParameter("tid");
		String ap_no = request.getParameter("ap_no");
		String icon = "";
		
		map.put("hash_id", hash_id);
		map.put("tid", tid);
		map.put("ap_no", ap_no);
		
		List<SubPathVo> subPathVo = dao.selectSubPath(map);
		for (SubPathVo vo : subPathVo) {
			JsonObject data = new JsonObject();
			JsonObject jServerObject = new JsonObject();
			jServerObject.addProperty("id", vo.getIdx());
			jServerObject.addProperty("text", vo.getName());
			jServerObject.addProperty("parent", vo.getParent_id());
			jServerObject.addProperty("ap_no", vo.getAp_no());
			
			icon = request.getContextPath() + "/resources/assets/images/file.png";
			jServerObject.addProperty("icon", icon);
			
			if(vo.getFid() != null && !vo.getFid().equals("")) {
				data.addProperty("type", 1);
				data.addProperty("tid", vo.getTarget_id());
			} else {
				data.addProperty("type", 0);
				data.addProperty("tid", vo.getTarget_id());
			}
			jServerObject.add("data", data);
			jArr.add(jServerObject);
		}
		
		/*logger.info("selectSubPath End Json Data >>> " + jArr.toString());*/

		return jArr;
	}
	

	
	@Override
	public String userReportGroupList(Map<String, Object> map, HttpServletRequest request) throws Exception {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		map.put("user_no", user_no);
		map.put("user_grade", user_grade);
		// TODO Auto-generated method stub
		String icon = "";
		String name = "";
		JsonObject data = new JsonObject();
		
		String typeChk = map.get("typeChk").toString();
		
		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();

		if ("9".equals(user_grade)) {
			
			if(typeChk.equals("0")) {
				jServerObject.addProperty("id", "server");
				jServerObject.addProperty("text", "서버");
				jServerObject.addProperty("parent", "#");
				jArr.add(jServerObject);
			}else if(typeChk.equals("1")) {
				jServerObject.addProperty("id", "pc");
				jServerObject.addProperty("text", "PC");
				jServerObject.addProperty("parent", "#");
				jArr.add(jServerObject);
			}
			
			/*jServerObject.put("id", "db");
			jServerObject.put("text", "DB");
			jServerObject.put("parent", "#");
			jArr.add(jServerObject);
			
			jServerObject.put("id", "onedirve");
			jServerObject.put("text", "OneDrive");
			jServerObject.put("parent", "#");
			jArr.add(jServerObject);*/

			jServerObject.addProperty("id", "sktoa");
			jServerObject.addProperty("text", "SKT(OA망)");
			jServerObject.addProperty("parent", "pc");
			jArr.add(jServerObject);

			jServerObject.addProperty("id", "sktut");
			jServerObject.addProperty("text", "SKT(유통망)");
			jServerObject.addProperty("parent", "pc");
			jArr.add(jServerObject);

			// 서버 리스트
			List<GroupTomsVo> groupServer = dao.selectReportTomsGroup(map);
			for (GroupTomsVo vo : groupServer) {
				name = vo.getName();

				// 서버 타겟 오브젝트
				JsonObject jSTObject = new JsonObject();
				jSTObject.addProperty("id", vo.getIdx());
				if(vo.getTarget_id() != null && !vo.getTarget_id().equals("")) {
					jSTObject.addProperty("id", vo.getTarget_id());
				} else {
					jSTObject.addProperty("id", vo.getIdx());
				}

				data.addProperty("ap", vo.getAp_no());
				if(vo.getPlatform().equals("Remote Access Only")) {
					
					// 상위 그룹 일 경우
					if (vo.getUp_idx().equals("0")) {
						jSTObject.addProperty("parent", "db");
					} else { // 하위 그룹
						jSTObject.addProperty("parent", "db");
						
						data.addProperty("target_id", vo.getTarget_id());
						icon = request.getContextPath() + "/resources/assets/images/db.png";
						
						data.addProperty("name", vo.getName());
						jSTObject.addProperty("icon", icon);
					}
				}else {
					// 상위 그룹 일 경우
					if (vo.getUp_idx().equals("0")) {
						jSTObject.addProperty("parent", "server");
					} else { // 하위 그룹
						jSTObject.addProperty("parent", vo.getUp_idx());
						
						data.addProperty("target_id", vo.getTarget_id());
						
						// 서버 (그룹x)
						if (vo.getTarget_id() != null && !vo.getTarget_id().equals("")) {
							icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
						} else {
							name += "(미설치)";
							icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
						}
						data.addProperty("name", vo.getName());
						jSTObject.addProperty("icon", icon);
					}
				}

				data.addProperty("type", vo.getType());
				jSTObject.add("data", data);
				jSTObject.addProperty("text", name);

				jArr.add(jSTObject);
			}
			
			/*
			jServerObject.put("id", "noGroup");
			jServerObject.put("text", "미분류");
			jServerObject.put("parent", "server");
			jArr.add(jServerObject);
			jServerObject.put("id", "noGroupPC");
			jServerObject.put("text", "미분류");
			data.put("ap", 0);
			data.put("type", "0");
			jServerObject.put("data", data);
			jServerObject.put("parent", "pc");
			jArr.add(jServerObject);
			*/
			logger.info("selectPCList");
			List<ScheduleServerVo> groupPC = dao.selectReportGroupPCList(map);
			for(ScheduleServerVo vo : groupPC) {
				name = vo.getName();
				
				if (!vo.getAgent_connected_ip().equals("")) {
					name += "(" + vo.getAgent_connected_ip() + ")";
				}

				JsonObject jSTObject = new JsonObject();
				data.addProperty("ap", vo.getAp_no());
				data.addProperty("type", (vo.getType() != 1 ? 0 : 1));
				/*
				if (vo.getType() == 1) {
					if(vo.getUp_idx().equals("onedirve")) {
						icon = request.getContextPath() + "/resources/assets/images/onedrive16px.png";
						
					}else if (vo.getAgent_connected_chk() == 1) {
						icon = request.getContextPath() + "/resources/assets/images/db.png";
						data.put("name", "onedrive");
					} else {
						icon = request.getContextPath() + "/resources/assets/images/pc_icon.png";
						data.put("name", vo.getName());
					}
					
					if(!vo.isAgent_connected()) {
						name += " (연결안됨)"; 
					}
					jSTObject.put("icon", icon);
					data.put("location", vo.getLocation_id());
				} else if (vo.getType() == 2) {
					if (vo.getConnected() != 0) {
						icon = request.getContextPath() + "/resources/assets/images/img_pc_con.png";
					} else {
						icon = request.getContextPath() + "/resources/assets/images/img_pc_dicon.png";
					}
					jSTObject.put("icon", icon);
				}
				*/
				jSTObject.addProperty("id", vo.getIdx());
				jSTObject.addProperty("text", name);
				jSTObject.addProperty("parent", vo.getUp_idx());
				jSTObject.add("data", data);
				jArr.add(jSTObject);

			}
			
			// 서버 미분류 그룹 추가
			/*List<GroupTargetVo> notGroup = dao.selectNotGroup(map);
			for (GroupTargetVo vo : notGroup) {
				name = vo.getName();
				if (!vo.getAgent_connected_ip().equals("")) {
					name += "(" + vo.getAgent_connected_ip() + ")";
				}

				if (vo.isAgent_connected() == true) {
					icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
				} else {
					icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
				}
				data.put("ap", "0");
				data.put("type", "1");
				JSONObject jSTObject = new JSONObject();
				jSTObject.put("id", vo.getTarget_id());
				jSTObject.put("text", name);
				jSTObject.put("parent", "noGroup");
				jSTObject.put("icon", icon);
				jSTObject.put("data", data);
				jArr.add(jSTObject);
			}*/

			/*map.put("group", "pc");
			List<GroupPCTargetVo> pcTarget = dao.selectPCTarget(map);
			for (GroupPCTargetVo vo : pcTarget) {
				JSONObject jSTObject = new JSONObject();

				name = vo.getName();
				if (!vo.getAgent_connected_ip().equals("")) {
					name += "(" + vo.getAgent_connected_ip() + ")";
				}

				if (vo.isAgent_connected() == true) {
					icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
				} else {
					icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
				}

				data.put("ap", vo.getAp_no());
				data.put("type", "1");
				jSTObject.put("data", data);

				jSTObject.put("id", vo.getTarget_id());
				jSTObject.put("text", name);
				jSTObject.put("icon", icon);
				jSTObject.put("parent", "noGroupPC");
				jArr.add(jSTObject);
			}*/
		
		}

		/*logger.info(jArr.toString());*/

		return jArr.toString();
	}
	
	@Override
	public String userReportHostList(Map<String, Object> map, HttpServletRequest request) throws Exception {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		map.put("user_no", user_no);
		map.put("user_grade", user_grade);

		String icon = "";
		String name = "";
		JsonObject data = new JsonObject();
		
		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		jServerObject.addProperty("id", "target");
		jServerObject.addProperty("text", "대상");
		jServerObject.addProperty("parent", "#");
		jArr.add(jServerObject);

		List<ScheduleServerVo> groupPC = dao.selectRMTargetList(map);
		for(ScheduleServerVo vo : groupPC) {
			name = vo.getName();
			
			JsonObject jSTObject = new JsonObject();
			data.addProperty("ap", vo.getAp_no());
			data.addProperty("type", (vo.getType() != 1 ? 0 : 1));
			data.addProperty("name", vo.getName());
			
			if (!vo.getAgent_connected_ip().equals("")) {
				name += "(" + vo.getAgent_connected_ip() + ")";
			}
			
			if(vo.isAgent_connected()) {
				icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
			}else {
				icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
			}
			
			jSTObject.addProperty("icon", icon);
			jSTObject.addProperty("id", vo.getIdx());
			jSTObject.addProperty("text", name);
			jSTObject.addProperty("parent", "target");
			jSTObject.add("data", data);
			jArr.add(jSTObject);

		}
		
		return jArr.toString();
	}

}
