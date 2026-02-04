package com.org.iopts.group.service.impl;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.iopts.dao.Pi_TargetDAO;
import com.org.iopts.group.dao.GroupDAO;
import com.org.iopts.group.service.GroupService;
import com.org.iopts.group.vo.DashScheduleServerVo;
import com.org.iopts.group.vo.GroupNetListVo;
import com.org.iopts.group.vo.GroupPCManagerVO;
import com.org.iopts.group.vo.GroupPCTargetVo;
import com.org.iopts.group.vo.GroupTargetVo;
import com.org.iopts.group.vo.GroupTomsVo;
import com.org.iopts.group.vo.GroupTreeServerVo;
import com.org.iopts.group.vo.GroupTreeVo;
import com.org.iopts.group.vo.targetServiceUserVo;
import com.org.iopts.group.vo.LicenseGroupVo;
import com.org.iopts.group.vo.LicenseGroupsVo;
import com.org.iopts.group.vo.PCGroupVo;
import com.org.iopts.group.vo.SchedulePCTargetVo;
import com.org.iopts.group.vo.ScheduleServerNotTargetVo;
import com.org.iopts.group.vo.ScheduleServerVo;
import com.org.iopts.group.vo.ScheduleUserVo;
import com.org.iopts.group.vo.SubPathVo;
import com.org.iopts.setting.service.impl.Pi_SetServiceImpl;
import com.org.iopts.util.SessionUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {

	private static Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

	@Value("${recon.id}")
	private String recon_id;

	@Value("${recon.password}")
	private String recon_password;

	@Value("${recon.url}")
	private String recon_url;

	@Value("${recon.api.version}")
	private String api_ver;

	@Inject
	private GroupDAO dao;
	
	@Inject
	private Pi_TargetDAO targetDAO;
	
	Pi_SetServiceImpl set_service = new Pi_SetServiceImpl();

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
		Map<String, Object> data = new HashMap<String, Object>();

		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		
		jServerObject.addProperty("id", "server");
		jServerObject.addProperty("text", "서버");
		jServerObject.addProperty("parent", "#");
		jArr.add(jServerObject);
		
		if(user_no != null && user_no != "") {
			// 서버 리스트
//			List<GroupTomsVo> groupServer = dao.selectTomsGroup(map);
//			for (GroupTomsVo vo : groupServer) {
//				name = vo.getName();
//				
//				// 서버 타겟 오브젝트
//				JsonObject jSTObject = new JsonObject();
//				jSTObject.addProperty("id", vo.getIdx());
//				
//				// 상위 그룹 일 경우
//				if (vo.getUp_idx().equals("0")) {
//					jSTObject.addProperty("parent", "server");
//				} else { // 하위 그룹
//					jSTObject.addProperty("parent", vo.getUp_idx());
//					
//					
//					
//					if(vo.getType() == 1) {
//						
//						data.put("target_id", vo.getTarget_id());
//						
//						if(vo.isAgent_connected()) {
//							icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
//							if (!vo.getAgent_connected_ip().equals("")) {
//								name += " (" + vo.getAgent_connected_ip() + ")";
//							}
//						} else {
//							icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
//						}
//						data.put("name", vo.getName());
//						jSTObject.addProperty("icon", icon);
//					}
//				}
//				data.put("type", vo.getType());
//				jSTObject.add("data", gson.toJsonTree(data));
//				jSTObject.addProperty("text", name);
//				
//				jArr.add(jSTObject);
//			}
			
			jServerObject.addProperty("id", "noGroup");
			jServerObject.addProperty("text", "미분류");
			jServerObject.addProperty("parent", "server");
			jArr.add(jServerObject);
		}
		

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
		Map<String, Object> data = new HashMap<String, Object>();
		
		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		
		jServerObject.addProperty("id", "server");
		jServerObject.addProperty("text", "서버");
		jServerObject.addProperty("parent", "#");
		jArr.add(jServerObject);

		if (user_grade.equals("9")) {
			
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
					
					data.put("target_id", vo.getTarget_id());

					if(vo.getType() != 0 ) {
						
						if (!vo.getAgent_connected_ip().equals("")) {
							name += " (" + vo.getAgent_connected_ip() + ")";
						}
						
						if(vo.isAgent_connected()) {
							icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
						} else {
							icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
						}
						
						jSTObject.addProperty("icon", icon);
						
						if (vo.getTarget_id() != null && !vo.getTarget_id().equals("")) {
							name += " [0" + (vo.getAp_no()+1)+"]"; 
						}
						
					}
					
					data.put("name", vo.getName());
					
				}
				Gson gson = new Gson();
				data.put("type", vo.getType());
				jSTObject.add("data", gson.toJsonTree(data));
				jSTObject.addProperty("text", name);
				
				jArr.add(jSTObject);
			}
			
			jServerObject.addProperty("id", "noGroup");
			jServerObject.addProperty("text", "미분류");
			jServerObject.addProperty("parent", "server");
			jArr.add(jServerObject);
			
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
		map.put("user_grade", user_grade);
		map.put("ap_no", 0);
		String icon = "";
		String name = "";
		Map<String, Object> data = new HashMap<String, Object>();

		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		jServerObject.addProperty("id", "server");
		jServerObject.addProperty("text", "서버");
		jServerObject.addProperty("parent", "#");
		data.put("ap", 0);
		data.put("type", "0");
		Gson gson = new Gson();
		jServerObject.add("data", gson.toJsonTree(data));
		jServerObject.addProperty("parent", "#");
		jArr.add(jServerObject);
		
		jServerObject.addProperty("id", "noGroup");
		jServerObject.addProperty("text", "미분류");
		data.put("ap", 0);
		data.put("type", 0);
		jServerObject.add("data", gson.toJsonTree(data));
		jServerObject.addProperty("parent", "server");
		jArr.add(jServerObject);
		
		// 서버 리스트
		List<ScheduleServerVo> groupServer = dao.selectScheduleServerList(map);
		for (ScheduleServerVo vo : groupServer) {
			name = set_service.replaceParameter(vo.getName());
			
			if(vo.getTarget_id() !=null && !vo.getTarget_id().equals("")) {
				name += " [0" + (vo.getAp_no() + 1) +"]";
			}
			
			if (!vo.getAgent_connected_ip().equals("")) {
				name += " | " + vo.getAgent_connected_ip();
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
				
			data.put("connected_ip", vo.getAgent_connected_ip());
			data.put("ap", vo.getAp_no());
			data.put("type", vo.getType());
			
			
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
					name += " | " + vo.getComdate() ;
				}else {
					name += " | 미검색";
				}
				
				if(!vo.isAgent_connected()) {
					//name += " (연결안됨)"; 
				}
				
				jSTObject.addProperty("icon", icon);
				data.put("name", vo.getName());
				data.put("core", vo.getCores());
				data.put("targets", vo.getTarget_id());
				data.put("location", vo.getLocation_id());
				data.put("agent_connected", vo.isAgent_connected());
			}

			
			jSTObject.addProperty("id", vo.getIdx());
			jSTObject.addProperty("text", name);
			jSTObject.add("data", gson.toJsonTree(data));
			jArr.add(jSTObject);
		}
/*		
		jServerObject.addProperty("id", "group");
		jServerObject.addProperty("text", "그룹");
		jServerObject.addProperty("parent", "#");
		data.put("ap", 0);
		data.put("type", 3);
		jServerObject.add("data", gson.toJsonTree(data));
		jServerObject.addProperty("parent", "#");
		jArr.add(jServerObject);
		*/
		return jArr.toString();
	}

	@Override
	public String selectDeptGroupList(Map<String, Object> map, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		map.put("user_no", user_no);
		map.put("ap_no", 1);
		map.put("user_grade", user_grade);
		String icon = "";
		String name = "";
		Map<String, Object> data = new HashMap<String, Object>();

		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		Gson gson = new Gson();
		jServerObject.addProperty("id", "pc");
		jServerObject.addProperty("text", "PC");
		data.put("type", 0);
		data.put("net_type", 99);
		jServerObject.add("data", gson.toJsonTree(data));
		jServerObject.addProperty("parent", "#");
		jArr.add(jServerObject);
		
/*		jServerObject.addProperty("id", "onedrive");
		jServerObject.addProperty("text", "OneDrive");
		data.put("type", 0);
		jServerObject.add("data", gson.toJsonTree(data));
		jServerObject.addProperty("parent", "#");
		jArr.add(jServerObject);*/
		

		if (user_grade.equals("9")) {
			/*jServerObject.addProperty("id", "sktoa_onedrive");
			jServerObject.addProperty("text", "OA망");
			data.put("type", 0);
			jServerObject.add("data", gson.toJsonTree(data));
			jServerObject.addProperty("parent", "onedrive");
			jArr.add(jServerObject);
			
			jServerObject.addProperty("id", "sktut_onedrive");
			jServerObject.addProperty("text", "VDI");
			data.put("type", 0);
			jServerObject.add("data", gson.toJsonTree(data));
			jServerObject.addProperty("parent", "onedrive");
			jArr.add(jServerObject);
			
			jServerObject.addProperty("id", "noGroup_onedrive");
			jServerObject.addProperty("text", "미분류");
			data.put("ap", 0);
			data.put("type", "0");
			jServerObject.add("data", gson.toJsonTree(data));
			jServerObject.addProperty("parent", "onedrive");
			jArr.add(jServerObject);*/
			
			/*// 그룹 망 추가
			jServerObject.addProperty("id", "net");
			jServerObject.addProperty("text", "망");
			data.put("ap", 0);
			data.put("type", 4);
			//jServerObject.addProperty("state", "{'checkbox_disabled' : true}");
			jServerObject.add("data", gson.toJsonTree(data));
			jServerObject.addProperty("parent", "#");
			jArr.add(jServerObject);*/
			
			jServerObject.addProperty("id", "onedrive");
			jServerObject.addProperty("text", "OneDrive");
			data.put("ap", 0);
			data.put("type", 0);
			data.put("net_type", 99);
			//jServerObject.addProperty("state", "{'checkbox_disabled' : true}");
			jServerObject.add("data", gson.toJsonTree(data));
			jServerObject.addProperty("parent", "#");
			jArr.add(jServerObject);
/*
			// 그룹 망 추가
			jServerObject.addProperty("id", "type1");
			jServerObject.addProperty("text", "OA(SOC)");
			data.put("ap", 0);
			data.put("type", 3);
			jServerObject.add("data", gson.toJsonTree(data));
			jServerObject.addProperty("parent", "net");
			icon = request.getContextPath() + "/resources/assets/images/net.png";
			jServerObject.addProperty("icon", icon);
			jArr.add(jServerObject);
			
			jServerObject.addProperty("id", "type2");
			jServerObject.addProperty("text", "OA(N-SOC)");
			data.put("ap", 0);
			data.put("type", 3);
			jServerObject.add("data", gson.toJsonTree(data));
			jServerObject.addProperty("parent", "net");
			icon = request.getContextPath() + "/resources/assets/images/net.png";
			jServerObject.addProperty("icon", icon);
			jArr.add(jServerObject);
			
			jServerObject.addProperty("id", "type3");
			jServerObject.addProperty("text", "유통망(서비스 ACE/TOP지점) - 검색 진행 불가(IP대역대 확인중)");
			data.put("ap", 0);
			data.put("type", 3);
			jServerObject.add("data", gson.toJsonTree(data));
			jServerObject.addProperty("parent", "net");
			icon = request.getContextPath() + "/resources/assets/images/net.png";
			jServerObject.addProperty("icon", icon);
			jArr.add(jServerObject);
			
			jServerObject.addProperty("id", "type4");
			jServerObject.addProperty("text", "유통망(대리점) - 검색 진행 불가(IP대역대 확인중)");
			data.put("ap", 0);
			data.put("type", 3);
			jServerObject.add("data", gson.toJsonTree(data));
			jServerObject.addProperty("parent", "net");
			icon = request.getContextPath() + "/resources/assets/images/net.png";
			jServerObject.addProperty("icon", icon);
			jArr.add(jServerObject);
			
			jServerObject.addProperty("id", "type5");
			jServerObject.addProperty("text", "유통망(F&U/미납센터, PS&M본사) - 검색 진행 불가(IP대역대 확인중)");
			data.put("ap", 0);
			data.put("type", 3);
			jServerObject.add("data", gson.toJsonTree(data));
			jServerObject.addProperty("rules", "{'multiple': false}");
			jServerObject.addProperty("parent", "net");
			icon = request.getContextPath() + "/resources/assets/images/net.png";
			jServerObject.addProperty("icon", icon);
			jArr.add(jServerObject);
			
			jServerObject.addProperty("id", "type6");
			jServerObject.addProperty("text", "VDI - 검색 진행 불가(IP대역대 확인중)");
			data.put("ap", 0);
			data.put("type", 3);
			jServerObject.add("data", gson.toJsonTree(data));
			jServerObject.addProperty("rules", "{'multiple': false}");
			jServerObject.addProperty("parent", "net");
			icon = request.getContextPath() + "/resources/assets/images/net.png";
			jServerObject.addProperty("icon", icon);
			jArr.add(jServerObject);
			
			jServerObject.addProperty("id", "type7");
			jServerObject.addProperty("text", "VDI(SOC)");
			data.put("ap", 0);
			data.put("type", 3);
			jServerObject.add("data", gson.toJsonTree(data));
			jServerObject.addProperty("parent", "net");
			icon = request.getContextPath() + "/resources/assets/images/net.png";
			jServerObject.addProperty("icon", icon);
			jArr.add(jServerObject);*/
			
			
			logger.info("selectSchedulePCList");
			List<ScheduleServerVo> groupPC = dao.selectSchedulePCList(map);

			for(ScheduleServerVo vo : groupPC) {
				
				String id = vo.getIdx();
				String platform = vo.getPlatform();
				
				/*if(vo.getAp_no() != 0 && platform.substring(0,5).equals("Apple") ) {
					name = vo.getMac_name();
				}else {
					name = vo.getName();
				}*/
				name = vo.getName();
				if (!vo.getAgent_connected_ip().equals("")) {
					name += " / " + vo.getAgent_connected_ip();
				}

				JsonObject jSTObject = new JsonObject();
				data.put("ap", vo.getAp_no());
				data.put("type", (vo.getType() != 1 ? 0 : 1));
				
				if (vo.getType() == 1) {
					if(vo.getUp_idx().equals("onedrive")) {
						id += "_" + vo.getLocation_id();
						icon = request.getContextPath() + "/resources/assets/images/onedrive16px.png";
						data.put("net_type", 3);
					}else if (vo.getAgent_connected_chk() == 1) {
						icon = request.getContextPath() + "/resources/assets/images/db.png";
						data.put("net_type", 2);
					} else {
						icon = request.getContextPath() + "/resources/assets/images/pc_icon.png";
						data.put("net_type", 1);
					}
					
					if(!vo.isAgent_connected() && !vo.getUp_idx().equals("onedrive")) {
						name += " (연결안됨)"; 
					}
					jSTObject.addProperty("icon", icon);
					data.put("name", vo.getName());
					data.put("targets", vo.getTarget_id());
					data.put("location", vo.getLocation_id());
				} else if (vo.getType() == 2) {
					if (vo.getConnected() != 0) {
						icon = request.getContextPath() + "/resources/assets/images/img_pc_con.png";
					} else {
						icon = request.getContextPath() + "/resources/assets/images/img_pc_dicon.png";
					}
					jSTObject.addProperty("icon", icon);
				}
				jSTObject.addProperty("id", id);
				jSTObject.addProperty("text", name);
				jSTObject.addProperty("parent", vo.getUp_idx());
				jSTObject.add("data", gson.toJsonTree(data));
				
				jArr.add(jSTObject);

			}
			
			/*logger.info("selectScheduleOneDriveList");
			List<ScheduleServerVo> groupOneDrive = dao.selectScheduleOneDriveList(map);

			for(ScheduleServerVo vo : groupOneDrive) {
				name = vo.getName();
				
				String id = vo.getIdx();
				
				if (!vo.getAgent_connected_ip().equals("")) {
					name += " (" + vo.getAgent_connected_ip() + ")";
				}

				JsonObject jSTObject = new JsonObject();
				data.put("ap", vo.getAp_no());
				data.put("type", (vo.getType() != 1 ? 0 : 1));
				
				if (vo.getType() == 1) {
					id += "_" + vo.getLocation_id();
					icon = request.getContextPath() + "/resources/assets/images/onedrive16px.png";
					
					if(!vo.isAgent_connected()) {
						name += " (연결안됨)"; 
					}
					jSTObject.addProperty("icon", icon);
					data.put("name", vo.getName());
					data.put("targets", vo.getTarget_id());
					data.put("location", vo.getLocation_id());
				} else if (vo.getType() == 2) {
					if (vo.getConnected() != 0) {
						icon = request.getContextPath() + "/resources/assets/images/img_pc_con.png";
					} else {
						icon = request.getContextPath() + "/resources/assets/images/img_pc_dicon.png";
					}
					jSTObject.addProperty("icon", icon);
				}
				jSTObject.addProperty("id", id+"_onedrive");
				jSTObject.addProperty("text", name);
				jSTObject.addProperty("parent", vo.getUp_idx()+"_onedrive");
				jSTObject.add("data", gson.toJsonTree(data));
				
				jArr.add(jSTObject);

			}*/
			
			
			/*List<GroupTreeVo> group = dao.selectUserGroupList(map);
			 map.put("group", ""); 
			// 그룹 pc 포함 여부
			for (GroupTreeVo vo : group) {
				JsonObject jSTObject = new JsonObject();
				jSTObject.addProperty("id", vo.getIdx());
				jSTObject.addProperty("text", vo.getName());
				data.put("type", "0");
				jSTObject.add("data", gson.toJsonTree(data));
				jSTObject.addProperty("parent", vo.getUp_idx());
				// jSTObject.addProperty("icon", request.getContextPath() +
				// "/resources/assets/images/file.png");
				jArr.add(jSTObject);
			}

			// 서버 미분류 그룹 추가
			map.put("group", "");
			List<SchedulePCTargetVo> pcTarget = dao.selectSchedulePCTarget(map);
			for (SchedulePCTargetVo vo : pcTarget) {
				JsonObject jSTObject = new JsonObject();
				name = vo.getUser_name();
				if (!vo.getAgent_connected_ip().equals("")) {
					name += " (" + vo.getAgent_connected_ip() + ")";
				}

				if (!vo.getTarget_use().equals("")) {
					name += vo.getTarget_use();
				}

				icon = request.getContextPath() + "/resources/assets/images/file.png";

				data.put("ap", vo.getAp_no());
				data.put("type", "1");
				data.put("name", vo.getName());
				data.put("location", vo.getLocation_id());

				jSTObject.add("data", gson.toJsonTree(data));

				jSTObject.addProperty("id", vo.getTarget_id());
				jSTObject.addProperty("text", name);
				jSTObject.addProperty("parent", vo.getInsa_code());
				jSTObject.addProperty("icon", icon);

				jArr.add(jSTObject);
			}

			jServerObject.addProperty("id", "noGroupPC");
			jServerObject.addProperty("text", "미분류");
			jServerObject.addProperty("parent", "pc");
			jArr.add(jServerObject);

			map.put("group", "pc");
			pcTarget = dao.selectSchedulePCTarget(map);
			for (SchedulePCTargetVo vo : pcTarget) {
				JsonObject jSTObject = new JsonObject();

				name = vo.getName();
				if (!vo.getAgent_connected_ip().equals("")) {
					name += " (" + vo.getAgent_connected_ip() + ")";
				}

				if (!vo.getTarget_use().equals("")) {
					name += vo.getTarget_use();
				}

				icon = request.getContextPath() + "/resources/assets/images/file.png";

				data.put("ap", vo.getAp_no());
				data.put("type", "1");
				data.put("name", vo.getName());
				data.put("location", vo.getLocation_id());

				jSTObject.add("data", gson.toJsonTree(data));

				jSTObject.addProperty("id", vo.getTarget_id());
				jSTObject.addProperty("text", name);
				jSTObject.addProperty("icon", icon);
				jSTObject.addProperty("parent", "noGroupPC");
				jArr.add(jSTObject);
			}*/
		} else if(user_grade.equals("2") || user_grade.equals("3")) {

			logger.info("selectCenterAdminSchedule");
			
			jServerObject.addProperty("id", "mypc");
			jServerObject.addProperty("text", "내 PC");
			jServerObject.addProperty("parent", "pc");
			jArr.add(jServerObject);
			
			List<GroupPCManagerVO> myPcList = dao.selectMyPcList(map);
			for (GroupPCManagerVO vo : myPcList) {
				JsonObject jSTObject = new JsonObject();
				name = vo.getName();
				String mac_name = vo.getMac_name();
				String platform = vo.getPlatform();
				
				/*if (!vo.getAgent_connected_ip().equals("")) {
					if(platform.substring(0,5).equals("Apple")) {
						mac_name += " (" + vo.getAgent_connected_ip() + ")";
					}else {
						name += " (" + vo.getAgent_connected_ip() + ")";
					}
				}*/
				name = vo.getName();
				if (!vo.getAgent_connected_ip().equals("")) {
					name += " / " + vo.getAgent_connected_ip();
				}
				
				jSTObject.addProperty("id", vo.getTARGET_ID());
				/*if(platform.substring(0,5).equals("Apple")) {
					jSTObject.addProperty("text", mac_name);
				}else {
					jSTObject.addProperty("text", name);
				}*/
				jSTObject.addProperty("text", name);
				jSTObject.addProperty("parent", "mypc");
				jSTObject.addProperty("connected", vo.getConnected());
				
				if(vo.getConnected().equals("1") && vo.getConnected() != null) {
					icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
				} else {
					icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
				}
				jSTObject.addProperty("icon", icon);
				
				jArr.add(jSTObject);
			}
			
			List<ScheduleServerVo> groupPC = dao.selectCenterAdminSchedule(map);

			for(ScheduleServerVo vo : groupPC) {
				name = vo.getName();
				
				String id = vo.getIdx();
				
				if (!vo.getAgent_connected_ip().equals("")) {
					name += " / " + vo.getAgent_connected_ip();
				}

				JsonObject jSTObject = new JsonObject();
				data.put("ap", vo.getAp_no());
				data.put("type", (vo.getType() != 1 ? 0 : 1));
				
				if (vo.getType() == 1) {
					if(vo.getUp_idx().equals("onedrive")) {
						id += "_" + vo.getLocation_id();
						icon = request.getContextPath() + "/resources/assets/images/onedrive16px.png";
					}else if (vo.getAgent_connected_chk() == 1) {
						icon = request.getContextPath() + "/resources/assets/images/db.png";
					} else {
						icon = request.getContextPath() + "/resources/assets/images/pc_icon.png";
					}
					
					if(!vo.isAgent_connected()) {
						name += " (연결안됨)"; 
					}
					jSTObject.addProperty("icon", icon);
					data.put("name", vo.getName());
					data.put("targets", vo.getTarget_id());
					data.put("location", vo.getLocation_id());
				} else if (vo.getType() == 2) {
					if (vo.getConnected() != 0) {
						icon = request.getContextPath() + "/resources/assets/images/img_pc_con.png";
					} else {
						icon = request.getContextPath() + "/resources/assets/images/img_pc_dicon.png";
					}
					jSTObject.addProperty("icon", icon);
				}
				jSTObject.addProperty("id", id);
				jSTObject.addProperty("text", name);
				jSTObject.addProperty("parent", vo.getUp_idx());
				jSTObject.add("data", gson.toJsonTree(data));
				jArr.add(jSTObject);

			}
			
			
		} else {
			List<ScheduleUserVo> list = dao.selectScheduleUser(map);
			for (ScheduleUserVo vo : list) {
				name = vo.getName();
				String platform = vo.getPlatform();
				
				/*if(vo.getAp_no() != 0 && platform.substring(0,5).equals("Apple") ) {
					name = vo.getMac_name();
				}else {
					name = vo.getName();
				}*/
				
				if (!vo.getAgent_connected_ip().equals("")) {
					name += " / " + vo.getAgent_connected_ip();
				}

				if (vo.getAgent_connected_chk() == 1) {
					icon = request.getContextPath() + "/resources/assets/images/db.png";
				} else {
					icon = request.getContextPath() + "/resources/assets/images/pc_icon.png";
				}
				
				if(!vo.isAgent_connected()) {
					name += " (연결안됨)"; 
				}
				
				data.put("ap", vo.getAp_no());
				data.put("type", 1);
				data.put("name", vo.getName());
				data.put("location", vo.getLocation_id());
				data.put("targets", vo.getTarget_id());
				JsonObject jSTObject = new JsonObject();
				jSTObject.addProperty("id", vo.getTarget_id());
				jSTObject.addProperty("text", name);
				jSTObject.addProperty("parent", "pc");
				jSTObject.addProperty("icon", icon);
				jSTObject.add("data", gson.toJsonTree(data));
				jArr.add(jSTObject);
			}
		}
		return jArr.toString();
	}

	@Override
	public JsonArray selectExceptionServerList(Map<String, Object> map, HttpServletRequest request) throws Exception {

		String name = "";
		Map<String, Object> data = new HashMap<String, Object>();

		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		jServerObject.addProperty("id", "server");
		jServerObject.addProperty("text", "서버");
		jServerObject.addProperty("parent", "#");
		jArr.add(jServerObject);
		Gson gson = new Gson();
		// 서버 리스트
		List<GroupTreeServerVo> groupServer = dao.selectExceptionServerList(map);
		for (GroupTreeServerVo vo : groupServer) {
			name = vo.getName();

			// 서버 타겟 오브젝트
			JsonObject jSTObject = new JsonObject();
			jSTObject.addProperty("id", vo.getIdx());
			jSTObject.addProperty("text", name);
			if (vo.getUp_idx().equals("0")) {
				jSTObject.addProperty("parent", "server");
			} else
				jSTObject.addProperty("parent", vo.getUp_idx());
			data.put("ap", "0");
			data.put("type", vo.getType());
			jSTObject.add("data", gson.toJsonTree(data));
			jArr.add(jSTObject);
		}

		if (!"N".equals(map.get("noGroup"))) {
			jServerObject.addProperty("id", "noGroup");
			jServerObject.addProperty("text", "미분류");
			jServerObject.addProperty("parent", "server");
			data.put("type", 1);
			jServerObject.add("data", gson.toJsonTree(data));
			jArr.add(jServerObject);
		}

		return jArr;
	}

	@Override
	public JsonArray selectExceptionHostList(Map<String, Object> map, HttpServletRequest request) throws Exception {

		String name = "";
		Map<String, Object> data = new HashMap<String, Object>();

		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();

		String noGroupCheck = (String) map.get("test");

		List<GroupTreeServerVo> groupServer = null;

		if (noGroupCheck.equals("noGroup")) {
			groupServer = dao.selectExceptionNoGroupHostList(map);
		} else {
			// 서버 리스트
			groupServer = dao.selectExceptionHostList(map);
		}
		Gson gson = new Gson();
		for (GroupTreeServerVo vo : groupServer) {
			name = vo.getName();

			// 서버 타겟 오브젝트
			JsonObject jSTObject = new JsonObject();
			jSTObject.addProperty("id", vo.getIdx());
			jSTObject.addProperty("text", name);
			data.put("ap", "0");
			data.put("type", vo.getType());
			jSTObject.add("data", gson.toJsonTree(data));
			jArr.add(jSTObject);
		}

		return jArr;
	}

	/*
	 * 대시보드 스케줄 그룹화
	 */
	@Override
	public JsonArray selectDashSeverDept(Map<String, Object> map, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		logger.info("selectSeverDashDept");
		Map<String, Object> data = new HashMap<String, Object>();
		Gson gson = new Gson();
		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();

		jServerObject.addProperty("id", "server");
		jServerObject.addProperty("text", "서버");
		data.put("type", "0");
		jServerObject.add("data", gson.toJsonTree(data));
		jServerObject.addProperty("parent", "#");
		jArr.add(jServerObject);
		List<Map<String, String>> group = new ArrayList<>();
		
		try {
			group = dao.selectDashSeverDept(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("group :: " + group);
		List<String> targets = new ArrayList<>();
		String parentId = "";

		int count = 0;
		String name = "";

		data.put("type", "1");
		data.put("ap_no", 0);
		DashScheduleServerVo one = null;
		JsonObject jSTObject = new JsonObject();

		String beforeID = group.get(0).get("ID");
		String beforeDay = group.get(0).get("REGDATE");

		// 날짜 구분을 위한 map 생성
		for (Map<String,String> pMap : group) {
			++count;
			if(!beforeID.equals(pMap.get("ID"))) {
				data = new HashMap<String, Object>();
				jSTObject = new JsonObject();
				
				jSTObject.addProperty("parent", "server");
				jSTObject.addProperty("id", beforeID);
				logger.info("beforeId :: "+beforeID+", beforeDay::  "+beforeDay);
				jSTObject.addProperty("text", beforeDay +"_"+beforeID +" 대");
				data.put("type", 0);
				jSTObject.add("data", gson.toJsonTree(data));
				jArr.add(jSTObject);
				
				count = 0;
				beforeDay = pMap.get("REGDATE");
				beforeID = pMap.get("ID");

				
				logger.info("parent Group data :::: " + jArr);
			}
		}
		
		// jstree에 데이터 저장
		for (Map<String,String> jsMap : group) {
			data = new HashMap<String, Object>();
			jSTObject = new JsonObject();
			
			jSTObject.addProperty("parent", jsMap.get("ID"));
			jSTObject.addProperty("id", jsMap.get("TARGET_ID"));
			jSTObject.addProperty("text", jsMap.get("NAME")+"[0" + (Integer.parseInt(jsMap.get("AP_NO")) + 1)+"]");
			data.put("type", 1);
			
			// dash board 에 생성할 데이터
			data.put("TARGET_ID", 1);
			data.put("AP_NO", 1);
			data.put("PATH_CNT", 1);
			data.put("TOTAL", 1);
			data.put("INACCESS_CNT", 1);
			data.put("TRUE_CNT", 1);
			data.put("FALSE_CNT", 1);
			data.put("SEARCH_STATUS", 1);
			jSTObject.add("data", gson.toJsonTree(data));
			jArr.add(jSTObject);
			
			logger.info("Group data :::: " + jArr);
			
		}
		

		/*logger.info("selectSeverDashDept  >>> " + jArr.toString());*/

		return jArr;
	}

	@Override
	public JsonArray selectDashPCDept(Map<String, Object> map, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		logger.info("selectDashPCDept");
		Map<String, Object> data = new HashMap<String, Object>();

		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		Gson gson = new Gson();

		jServerObject.addProperty("id", "pc");
		jServerObject.addProperty("text", "PC");
		data.put("type", "0");
		jServerObject.add("data", gson.toJsonTree(data));
		jServerObject.addProperty("parent", "#");
		jArr.add(jServerObject);

		List<DashScheduleServerVo> group = dao.selectDashPCDept(map);
		List<String> targets = new ArrayList<>();
		String parentId = "";

		int count = 0;
		String name = "";

		data.put("type", "1");
		data.put("ap_no", 0);
		DashScheduleServerVo one = null;
		JsonObject jSTObject = new JsonObject();

		if (group.size() != 0) {
			count = 1;
			one = group.get(0);
			name = one.getRegdate() + "_";
			targets.add(one.getTarget_id());
			jSTObject.addProperty("parent", "pc");
			jSTObject.addProperty("id", one.getId());
			parentId = one.getId();

		}

		for (int i = 1; i < group.size(); i++) {
			DashScheduleServerVo vo = group.get(i);

			if (!parentId.equals(vo.getId())) {
				jSTObject.addProperty("text", name + count + "대");
				data.put("targets", targets);
				jSTObject.add("data", gson.toJsonTree(data));
				jArr.add(jSTObject);
				targets.clear();
				count = 0;
			}
			jSTObject = new JsonObject();
			jSTObject.addProperty("parent", "pc");
			jSTObject.addProperty("id", vo.getId());
			targets.add(vo.getTarget_id());
			name = vo.getRegdate() + "_";

			count++;

			parentId = vo.getId();
		}

		if (group.size() != 0) {
			jSTObject.addProperty("text", name + count + "대");
			data.put("targets", targets);
			jSTObject.add("data", gson.toJsonTree(data));
			jArr.add(jSTObject);
			count = 0;
		}

		/*logger.info("selectDashPCDept  >>> " + jArr.toString());*/

		return jArr;
	}

	@Override
	public JsonArray selectPCGroup(Map<String, Object> map, HttpServletRequest request) throws Exception {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");

		map.put("user_no", user_no);
		map.put("user_grade", user_grade);
		// TODO Auto-generated method stub
		String icon = "";
		String name = "";
		Map<String, Object> data = new HashMap<String, Object>();

		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		Gson gson = new Gson();

		if (user_grade.equals("9")) {
			jServerObject.addProperty("id", "server");
			jServerObject.addProperty("text", "서버");
			jServerObject.addProperty("parent", "#");
			jArr.add(jServerObject);

			jServerObject.addProperty("id", "pc");
			jServerObject.addProperty("text", "PC");
			jServerObject.addProperty("parent", "#");
			jArr.add(jServerObject);

			jServerObject.addProperty("id", "sktoa");
			jServerObject.addProperty("text", "OA망");
			jServerObject.addProperty("parent", "pc");
			jArr.add(jServerObject);

			jServerObject.addProperty("id", "sktut");
			jServerObject.addProperty("text", "VDI");
			jServerObject.addProperty("parent", "pc");
			jArr.add(jServerObject);

			// 서버 리스트
			List<GroupTreeServerVo> groupServer = dao.selectServerGroupList(map);
			for (GroupTreeServerVo vo : groupServer) {
				name = vo.getName();

				if (!vo.getAgent_connected_ip().equals("")) {
					name += " (" + vo.getAgent_connected_ip() + ")";
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
				data.put("ap", "0");
				data.put("type", vo.getType());
				if (vo.getType() == 1) {

					if (vo.isAgent_connected() == true) {
						icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
					} else {
						icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
					}
					jSTObject.addProperty("icon", icon);
				}
				jSTObject.add("data", gson.toJsonTree(data));
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
					name += " (" + vo.getAgent_connected_ip() + ")";
				}

				if (vo.isAgent_connected() == true) {
					icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
				} else {
					icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
				}
				data.put("ap", "0");
				data.put("type", "1");
				JsonObject jSTObject = new JsonObject();
				jSTObject.addProperty("id", vo.getTarget_id());
				jSTObject.addProperty("text", name);
				jSTObject.addProperty("parent", "noGroup");
				jSTObject.addProperty("icon", icon);
				jSTObject.add("data", gson.toJsonTree(data));
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
				// jSTObject.addProperty("icon", request.getContextPath() +
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

				data.put("ap", 1);
				data.put("type", vo.getType());
				jSTObject.add("data", gson.toJsonTree(data));

				jArr.add(jSTObject);
			}

		} else {
			List<GroupTargetVo> list = dao.selectUserTargets(map);
			for (GroupTargetVo vo : list) {
				name = vo.getName();
				if (!vo.getAgent_connected_ip().equals("")) {
					name += " (" + vo.getAgent_connected_ip() + ")";
				}

				if (vo.isAgent_connected() == true) {
					icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
				} else {
					icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
				}
				data.put("ap", String.valueOf(vo.getAp_no()));
				data.put("type", "1");
				JsonObject jSTObject = new JsonObject();
				jSTObject.addProperty("id", vo.getTarget_id());
				jSTObject.addProperty("text", name);
				jSTObject.addProperty("parent", "server");
				jSTObject.addProperty("icon", icon);
				jSTObject.add("data", gson.toJsonTree(data));
				jArr.add(jSTObject);
			}

		}

		/*logger.info("selectPCGroup >> " + jArr.toString());*/

		return jArr;
	}
	
	
	@Override
	public JsonArray selectDashDeptList(Map<String, Object> map, HttpServletRequest request) throws Exception {
		logger.info("selectDashDeptList");
		Map<String, Object> data = new HashMap<String, Object>();
		Gson gson = new Gson();
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		if(user_grade.equals("9")) {
			map.put("fromDate", fromDate);
			map.put("toDate", toDate);
		}
		
		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		jServerObject.addProperty("id", "server");
		jServerObject.addProperty("text", "서버");
		jServerObject.addProperty("parent", "#");
		jArr.add(jServerObject);
		
		List<Map<String, String>> group = new ArrayList<>();
		List<Map<String, String>> DayList = new ArrayList<>();
		
		map.put("user_grade", user_grade);
		map.put("user_no", SessionUtil.getSession("memberSession", "USER_NO"));
		JsonObject jSTObject = new JsonObject();
		
		try {
			group = dao.selectDashSeverDept(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try { 
			DayList = dao.selectDayDashSeverDept(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(DayList.size() > 0) {
			for (Map<String,String> pMap : DayList) {
				data = new HashMap<String, Object>();
				jSTObject = new JsonObject();
				jSTObject.addProperty("parent", "server");
				jSTObject.addProperty("id", pMap.get("DATE_KEY")); 
				jSTObject.addProperty("text", pMap.get("DATE_NM") +"_"+String.valueOf(pMap.get("HOST_CNT"))+"대");
				data.put("type", 1);
				jSTObject.add("data", gson.toJsonTree(data));
				jArr.add(jSTObject);
			}
		}
		String icon = request.getContextPath() + "/resources/assets/images/server_icon.png"; 
		if(group.size() > 0) {
			for (Map<String,String> pMap : group) {
				int ap_no = Integer.parseInt(String.valueOf(pMap.get("ap_no")));
				data = new HashMap<String, Object>();
				jSTObject = new JsonObject();
				jSTObject.addProperty("parent", pMap.get("DATE_KEY"));
				jSTObject.addProperty("id", pMap.get("DATE_KEY") +"_"+pMap.get("target_id")); 
				jSTObject.addProperty("text", pMap.get("name")+"[0"+ (ap_no+1)+"]" );
				data.put("ap_no", pMap.get("ap_no"));
				data.put("target_id", pMap.get("target_id"));
				data.put("type", 1);
				jSTObject.add("data", gson.toJsonTree(data));
				jSTObject.addProperty("icon", icon);
				jArr.add(jSTObject);
			}
		}
		
		logger.info("jArr :: " + jArr.toString());
		return jArr;
		
	}
	
	@Override
	public JsonArray SelectTargetDash(HttpServletRequest request) throws Exception {
		logger.info("SelectTargetDash");
		Map<String, Object> data = new HashMap<String, Object>();
		
		Map<String, Object> map = new HashMap<String, Object>();
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String id = request.getParameter("id");
		String target_id = request.getParameter("target_id");
		int type = Integer.parseInt(request.getParameter("type"));
		Gson gson = new Gson();
		
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		if(user_grade.equals("9")) {
			map.put("fromDate", fromDate);
			map.put("toDate", toDate);
		}
		
		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		
		jServerObject.addProperty("id", "server");
		jServerObject.addProperty("text", "서버");
		data.put("type", "0");
		List<Map<String, String>> group = new ArrayList<>();
		List<Object> resultList =  new ArrayList<>();
		
		map.put("group_id", id);
		map.put("target_id", target_id);
		map.put("type", type);
		map.put("user_grade", user_grade);
		map.put("user_no", SessionUtil.getSession("memberSession", "USER_NO"));
		
		if(type == 0) {
			List<Map<String, Object>> idxList = targetDAO.selectGroupIdx(id);
			for (Map<String, Object> idMap : idxList) {
				resultList.add(idMap.get("TARGET_ID"));
			}
			map.put("idList", resultList);
		}
		
		try {
			group = dao.SelectTargetDash(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(group.size() != 0) {
			data.put("total_server", group.get(0).get("TOTAL_SERVER"));
			data.put("total_db", group.get(0).get("TOTAL_DB"));
			jServerObject.add("data", gson.toJsonTree(data));
			jServerObject.addProperty("parent", "#");
			jArr.add(jServerObject);
			
			logger.info("group :: " + group);
			
			int count = 0;
			int totalCount = 0;
			
			data.put("type", "1");
			data.put("ap_no", 0);
			JsonObject jSTObject = new JsonObject();
			
			String beforeID = group.get(0).get("ID");
			String beforeDay = group.get(0).get("REGDATE");
			
			// 날짜 구분을 위한 map 생성
			for (Map<String,String> pMap : group) {
				++count;
				++totalCount;
				
				if(!beforeID.equals(pMap.get("ID")) || group.size() == totalCount) {
					data = new HashMap<String, Object>();
					jSTObject = new JsonObject();
					
					jSTObject.addProperty("parent", "server");
					jSTObject.addProperty("id", beforeID);
					jSTObject.addProperty("text", beforeDay +"_"+count +" 대");
					data.put("type", 0);
					jSTObject.add("data", gson.toJsonTree(data));
					jArr.add(jSTObject);
					
					count = 0;
					beforeDay = pMap.get("REGDATE");
					beforeID = pMap.get("ID");
				}
				
			}
			
			// jstree에 데이터 저장
			for (Map<String,String> jsMap : group) {
				data = new HashMap<String, Object>();
				jSTObject = new JsonObject();
				
				jSTObject.addProperty("parent", jsMap.get("ID"));
				jSTObject.addProperty("id", jsMap.get("TARGET_ID"));
				
				
				String jsName = jsMap.get("NAME")+" [0";
				jsName  += (Integer.parseInt(String.valueOf(jsMap.get("AP_NO")))  + 1)+ "]";
				
				jSTObject.addProperty("text", jsName);
				jSTObject.addProperty("icon", request.getContextPath() + "/resources/assets/images/file.png");
				data.put("type", 1);
				
				// dash board 에 생성할 데이터
				data.put("TARGET_ID", jsMap.get("TARGET_ID"));
				data.put("AP_NO", jsMap.get("AP_NO"));
				data.put("PATH_CNT", jsMap.get("PATH_CNT"));
				data.put("TOTAL", jsMap.get("TOTAL"));
				data.put("INACCESS_CNT", jsMap.get("INACCESS_CNT"));
				data.put("TRUE_CNT", jsMap.get("TRUE_CNT"));
				data.put("FALSE_CNT", jsMap.get("FALSE_CNT"));
				data.put("SEARCH_STATUS", jsMap.get("SEARCH_STATUS"));
				data.put("SERVER_TYPE", jsMap.get("SERVER_TYPE"));
				jSTObject.add("data", gson.toJsonTree(data));
				jArr.add(jSTObject);
				
			}
		}else {
			jServerObject.add("data", gson.toJsonTree(data));
			jServerObject.addProperty("parent", "#");
			jArr.add(jServerObject);
		}
		return jArr;
	}

	// 그룹 데이터
	@Override
	public List<Map<String, Object>> selectGroup(Map<String, Object> map, HttpServletRequest request) throws Exception {
		return dao.selectGroup(map);
	}

	// 그룹 데이터
	@Override
	public int moveTargetGroup(Map<String, Object> map, HttpServletRequest request) throws Exception {
		String group_id = (String) request.getParameter("group_id");
		String treeArr = request.getParameter("treeArr");
		int result = -1;
		//String[] treeArr = request.getParameterValues("treeArr");
		logger.info("Group move >> " + group_id + ", treeArr > " + treeArr);
		Gson gson = new Gson();
		JsonArray treeJArr =  gson.fromJson(treeArr, JsonArray.class);
		List<String> dataArr = new ArrayList<>();

		for (int i = 0; i < treeJArr.size(); i++) {
		    logger.info("Size >> " + treeJArr.size());
		    
		    String data = treeJArr.get(i).getAsString();
		    dataArr.add(data);
		}
		
		map.put("groupId", group_id);
		map.put("treeArr", dataArr);
		try {
			dao.updateTomsGroup(map);
			result = 1;
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return result;
	}

	@Override
	public JsonArray selectTomsGroup(Map<String, Object> map, HttpServletRequest request) throws Exception {
		/*String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");

		map.put("user_no", user_no);
		map.put("user_grade", user_grade);*/
		// TODO Auto-generated method stub
		String icon = "";
		String name = "";
		Map<String, Object> data = new HashMap<String, Object>();
		Gson gson = new Gson();
		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();

		jServerObject.addProperty("id", "server");
		jServerObject.addProperty("text", "서버");
		jServerObject.addProperty("parent", "#");

		data.put("type", 0);
		jServerObject.add("data", gson.toJsonTree(data));
		jArr.add(jServerObject);
		
		jServerObject.addProperty("id", "noGroup");
		jServerObject.addProperty("text", "미분류");
		jServerObject.addProperty("parent", "server");

		data.put("type", 0);
		jServerObject.add("data", gson.toJsonTree(data));
		jArr.add(jServerObject);

		// 서버 리스트
		List<GroupTomsVo> groupServer = dao.selectTomsGroup(map);
		for (GroupTomsVo vo : groupServer) {
			name = vo.getName();

			// 서버 타겟 오브젝트
			JsonObject jSTObject = new JsonObject();
			jSTObject.addProperty("id", vo.getIdx());
			if (vo.getUp_idx().equals("0")) {
				jSTObject.addProperty("parent", "server");
			} else {
				jSTObject.addProperty("parent", vo.getUp_idx());

				if(vo.getType() == 1) {
					data.put("target_id", vo.getTarget_id());
					
					if (!vo.getAgent_connected_ip().equals("")) {
						name += " (" + vo.getAgent_connected_ip() + ")";
					}
					
					if (vo.getTarget_id() != null && !vo.getTarget_id().equals("")) {
						icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
						name += " [0" + (vo.getAp_no()+1)+"]"; 
					} else {
						name += "(미설치)";
						icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
					}
					jSTObject.addProperty("icon", icon);
				}
				

				data.put("name", vo.getName());
			}
			data.put("type", vo.getType());
			jSTObject.add("data", gson.toJsonTree(data));
			jSTObject.addProperty("text", name);

			jArr.add(jSTObject);
		}
//
//
//		
//
//		// 서버 리스트
//		List<GroupTomsVo> groupNotServer = dao.selectTomsNotGroup(map);
//		for (GroupTomsVo vo : groupNotServer) {
//			name = vo.getName();
//
//			// 서버 타겟 오브젝트
//			JsonObject jSTObject = new JsonObject();
//			jSTObject.addProperty("id", vo.getIdx());
//			jSTObject.addProperty("parent", "noGroup");
//
//			data.put("target_id", vo.getTarget_id());
//
//			if (vo.getTarget_id() != null && !vo.getTarget_id().equals("")) {
//				icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
//			} else {
//				name += "(미설치)";
//				icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
//			}
//			data.put("name", vo.getName());
//			jSTObject.addProperty("icon", icon);
//			
//			
//			data.put("type", vo.getType());
//			jSTObject.add("data", gson.toJsonTree(data));
//			jSTObject.addProperty("text", name);
//
//			jArr.add(jSTObject);
//		}
		/*logger.info("selectTomsGroup >> " + jArr.toString());*/

		return jArr;
	}
	/*

	@Override
	public JsonArray selectTomsNotGroup(Map<String, Object> map, HttpServletRequest request) throws Exception {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");

		map.put("user_no", user_no);
		map.put("user_grade", user_grade);
		// TODO Auto-generated method stub
		String icon = "";
		String name = "";
		Map<String, Object> data = new HashMap<String, Object>();

		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();

		jServerObject.addProperty("id", "noGroup");
		jServerObject.addProperty("text", "미분류");
		jServerObject.addProperty("parent", "#");

		data.put("type", 0);
		jServerObject.add("data", gson.toJsonTree(data));
		jArr.add(jServerObject);

		// 서버 리스트
		List<GroupTomsVo> groupServer = dao.selectTomsNotGroup(map);
		for (GroupTomsVo vo : groupServer) {
			name = vo.getName();

			// 서버 타겟 오브젝트
			JsonObject jSTObject = new JsonObject();
			jSTObject.addProperty("id", vo.getIdx());
			jSTObject.addProperty("parent", "noGroup");

			data.put("target_id", vo.getTarget_id());

			if (vo.getTarget_id() != null && !vo.getTarget_id().equals("")) {
				icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
			} else {
				name += "(미설치)";
				icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
			}
			data.put("name", vo.getName());
			jSTObject.addProperty("icon", icon);
			
			
			data.put("type", vo.getType());
			jSTObject.add("data", gson.toJsonTree(data));
			jSTObject.addProperty("text", name);

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
	public JsonArray selectUserCreateGroup( HttpServletRequest request) throws Exception {
		// 트리 아이콘 설정
		String icon = "";
		// 트리 이름 설정
		String name = "";
		Map<String, Object> data = new HashMap<String, Object>();

		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		Gson gson = new Gson();

		jServerObject.addProperty("id", "group");
		jServerObject.addProperty("text", "그룹");
		jServerObject.addProperty("parent", "#");

		data.put("type", 0);
		jServerObject.add("data", gson.toJsonTree(data));
		jArr.add(jServerObject);
		

		// 서버 리스트
		List<GroupTomsVo> groupServer = dao.selectTomsUserGroup();
		for (GroupTomsVo vo : groupServer) {
			name = set_service.replaceParameter(vo.getName());
			icon= "";
			// 서버 타겟 오브젝트
			JsonObject jSTObject = new JsonObject();
			jSTObject.addProperty("id", vo.getIdx());
			if (vo.getUp_idx().equals("IDX_0")) {
				jSTObject.addProperty("parent", "group");
			} else {
				jSTObject.addProperty("parent", vo.getUp_idx());

				data.put("target_id", vo.getTarget_id());
				data.put("ap_no", vo.getAp_no());
				if(vo.getType() != 0) {

					jSTObject.addProperty("id", vo.getUp_idx() + "_" + vo.getIdx());
					if (vo.getTarget_id() != null && !vo.getTarget_id().equals("")) {
						name += " [0" + (vo.getAp_no()+1) + "]";
					} else {
						name += "(미설치)";
					}
					
					if(vo.isAgent_connected()) {
						icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
					} else {
						icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
					}
				}
				
				data.put("name", name);
				jSTObject.addProperty("icon", icon);
			}
			data.put("type", vo.getType());
			jSTObject.add("data", gson.toJsonTree(data));
			jSTObject.addProperty("text", name);

			jArr.add(jSTObject);
		}

		return jArr;
	}

	/*
	 * 보안 모듈이 생성
	 */	
	@Override
	public JsonArray insertUserCreateGroup(Map<String, Object> map, HttpServletRequest request) throws Exception {
		
		String name = (String) request.getParameter("name");
		String idx = request.getParameter("idx");
		
		logger.info("NAME >>> " + name + ", IDX >>> " +idx.toString());
		
		map.put("name", name);
		map.put("up_idx", idx.replace("IDX_", ""));
		
		dao.insertServerGroup(map);
		

		return selectUserCreateGroup( request);
	}
	
	/*
	 * 보안 모듈이 변경
	 */	
	@Override
	public int updateUserCreateGroup(Map<String, Object> map, HttpServletRequest request) throws Exception {
		
		int resultCode = -1;
		
		String name = (String) request.getParameter("name");
		String idx = request.getParameter("idx");
		try {
			logger.info("NAME >>> " + name + ", IDX >>> " +idx.toString());
			
			map.put("name", name);
			map.put("idx", idx.toString().replace("IDX_", ""));
			
			dao.updateServerGroup(map);
			resultCode = 0;
			
		} catch (Exception e) {
			logger.error(e.toString());
		}

		return resultCode;
	}
	
	/*
	 * 보안 모듈이 생성한 그룹 삭제
	 */	
	@Override
	public String deleteUserCreateGroup(Map<String, Object> map, HttpServletRequest request) throws Exception {
		
		String groupArr = (String) request.getParameter("groupArr");
		String serverArr = (String) request.getParameter("serverArr");
		
		logger.info("NAME >>> " + groupArr.toString() + ", IDX >>> " +serverArr.toString());
		Gson gson = new Gson();
		
		try {
			// 선택한 서버 삭제
			JsonArray serverJArr = gson.fromJson(serverArr, JsonArray.class);

			for (int i = 0; i < serverJArr.size(); i++) {
			    logger.info("Size >> " + serverJArr.size()); 
			    JsonObject serverObj = serverJArr.get(i).getAsJsonObject();
			    
			    String[] dataList = serverObj.get("serverID").getAsString().split("_");
			    
			    // 개개인 서버 삭제
			    map.put("groupID", dataList[0]);
			    map.put("serverID", dataList[3]);
			     
			    dao.deleteUserServer(map);
			}
			
			//선택한 그룹 삭제 
			JsonArray groupJArr = gson.fromJson(groupArr, JsonArray.class);
			List<String> groupList = new ArrayList<>();

			for (int i = 0; i < groupJArr.size(); i++) {
			    logger.info("Size >> " + groupJArr.size());
			    String groupID = groupJArr.get(i).getAsString();
			     
			    groupList.add(groupID.replace("IDX_", ""));
			}
			map.put("groupList", groupList);
			
			// 그룹 삭제에 그룹이 선택 안된 경우  
			
			logger.info(groupList.size() + " < 사이즈 ");
			if(groupList.size() != 0) {
				dao.deleteUserServerGroup(map);
				dao.deleteUserGroup(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return selectUserCreateGroup(request).toString();
	}
	

	/*
	 * 보안 모듈이 생성
	 */	
	@Override
	public Map<String, Object> insertUserTargets(Map<String, Object> map, HttpServletRequest request) throws Exception {
		String groupArr = request.getParameter("groupArr");
		String treeArr = request.getParameter("treeArr");
		int result = 0;
		logger.info("Group move >> " + groupArr + ", treeArr > " + treeArr);
		Gson gson = new Gson();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			 // 선택한 toms 자산정보
			JsonArray treeJArr = gson.fromJson(treeArr, JsonArray.class);;
	        JsonArray groupJArr = gson.fromJson(groupArr, JsonArray.class);
			
	        logger.info("groupJArr ::: " + groupJArr);
	        logger.info("treeArr ::: " + treeArr);
	        
	        
			for (int i = 0; i < groupJArr.size(); i++) {
				String data = groupJArr.get(i).getAsString();
				// 그룹에 저장
				for (int j = 0; j < treeJArr.size(); j++) {
					try {
						int group_id = Integer.parseInt(data.replace("IDX_", ""));
						map.put("group_id", group_id);
						
						JsonObject treeJObect = treeJArr.get(j).getAsJsonObject();
						
						map.put("target_id", treeJObect.get("target_id").getAsString());
						map.put("ap_no", treeJObect.get("ap_no").getAsString());
						
						logger.info("map :: " + map); 
						dao.insertUserTargets(map);
					} catch (Exception e) {
						e.printStackTrace();
						result = -1;
					}
				}
			}
			
			logger.info("result ::: " + result);
			
			resultMap.put("resultCode", result);  
//			
//			try {
//				if(result == 0 ) { 
//					JsonArray dataArray = selectUserCreateGroup(request);
//					JsonObject singleData = dataArray.get(0).getAsJsonObject();
//				    resultMap.put("data", singleData); 
//				}else {
//					resultMap.put("data", null);
//				}   
//				
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
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
		Map<String, Object> data = new HashMap<String, Object>();

		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		Gson gson = new Gson();
		
		jServerObject.addProperty("id", "server");
		jServerObject.addProperty("text", "서버");
		jServerObject.addProperty("parent", "#");
		jArr.add(jServerObject);
	

		try {
			if (user_grade.equals("9")) {
				
				jServerObject.addProperty("id", "noGroup");
				jServerObject.addProperty("text", "미분류");
				jServerObject.addProperty("parent", "server");
				jArr.add(jServerObject);
				
				jServerObject.addProperty("id", "db");
				jServerObject.addProperty("text", "DB");
				jServerObject.addProperty("parent", "#");
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

					data.put("ap", vo.getAp_no());
					if(vo.getPlatform().equals("Remote Access Only")) {
						
						// 상위 그룹 일 경우
						if (vo.getUp_idx().equals("0")) {
							jSTObject.addProperty("parent", "db");
						} else { // 하위 그룹
							if(vo.getType() ==1) {
								jSTObject.addProperty("parent", "db");
								
								data.put("target_id", vo.getTarget_id());
								icon = request.getContextPath() + "/resources/assets/images/db.png";
								
								data.put("name", vo.getName());
								jSTObject.addProperty("icon", icon);
							}
						}
					}else {
						// 상위 그룹 일 경우
						if (vo.getUp_idx().equals("0")) {
							jSTObject.addProperty("parent", "server");
						} else { // 하위 그룹
							jSTObject.addProperty("parent", vo.getUp_idx());
							if(vo.getType() == 1) {
								data.put("target_id", vo.getTarget_id());
								
								if(vo.isAgent_connected()) {
									icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
									if (!vo.getAgent_connected_ip().equals("")) {
										name += " (" + vo.getAgent_connected_ip() + ")";
									}
								} else {
									icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
								}
								data.put("name", vo.getName());
								jSTObject.addProperty("icon", icon);
							}
						}
					}
					data.put("type", vo.getType());
					jSTObject.add("data", gson.toJsonTree(data));
					jSTObject.addProperty("text", name);

					jArr.add(jSTObject);
				} 
				
				
			} else if (user_grade.equals("4") || user_grade.equals("5") || user_grade.equals("6")) {
				
				List<GroupTargetVo> list = dao.selectUserServerTargets(map);
				for (GroupTargetVo vo : list) {
					name = vo.getName();
					String platform = vo.getPlatform();
					
					if (!vo.getAgent_connected_ip().equals("")) {
						name += " (" + vo.getAgent_connected_ip() + ")";
					}

					if (vo.isAgent_connected() == true) {
						icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
					} else {
						icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
					}
					data.put("ap", String.valueOf(vo.getAp_no()));
					data.put("type", "1");
					JsonObject jSTObject = new JsonObject();
					jSTObject.addProperty("id", vo.getTarget_id());
					jSTObject.addProperty("text", name);
					jSTObject.addProperty("parent", vo.getUp_idx());
					jSTObject.addProperty("icon", icon);
					jSTObject.add("data", gson.toJsonTree(data));
					jArr.add(jSTObject);
				}
			} else {
				List<GroupTargetVo> list = dao.selectUserTargets(map);
				for (GroupTargetVo vo : list) {
					name = vo.getName();
					if (!vo.getAgent_connected_ip().equals("")) {
						name += " (" + vo.getAgent_connected_ip() + ")";
					}

					if (vo.isAgent_connected() == true) {
						icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
					} else {
						icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
					}
					data.put("ap", String.valueOf(vo.getAp_no()));
					data.put("type", "1");
					JsonObject jSTObject = new JsonObject();
					
					jSTObject.addProperty("id", vo.getTarget_id());
					jSTObject.addProperty("text", name);
					jSTObject.addProperty("parent", "server");
					jSTObject.addProperty("icon", icon);
					jSTObject.add("data", gson.toJsonTree(data));
					jArr.add(jSTObject);
				}

			}
		} catch (Exception e) {
			logger.error("Select Group Error :: " + e);
		}

		logger.info(jArr.toString());
		
		return jArr.toString();
	}
	
	@Override
	public String selectUserHostOneDriveList(Map<String, Object> map, HttpServletRequest request) throws Exception {
		logger.info("selectUserHostOneDriveList");
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		Gson gson = new Gson();
		
		List<GroupNetListVo> netList = null;
		
		netList = dao.selectNetOneDriveList(map);
		
		jServerObject.addProperty("id", "onedirve");
		jServerObject.addProperty("text", "OneDrive");
		jServerObject.addProperty("parent", "#");
		jArr.add(jServerObject);
		
		for (int i = 0; i < netList.size(); i++) {
			GroupNetListVo vo = netList.get(i);
			String id = vo.getId();
			String location_id = vo.getLocation_id();
			jServerObject.addProperty("text", vo.getName());
			data.put("name", vo.getOnedrive_name());
			data.put("type", vo.getType());
			jServerObject.add("data", gson.toJsonTree(data));
			jServerObject.addProperty("target", id);
			jServerObject.addProperty("id", location_id);
			jServerObject.addProperty("parent", "onedirve");
			jServerObject.addProperty("icon", request.getContextPath() + "/resources/assets/images/onedrive16px.png");
			jArr.add(jServerObject);
		}
		
		return jArr.toString();
		
	}

	@Override
	public JsonArray selectSubPath(Map<String, Object> map, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		logger.info("selectSubPath");
		JsonArray jArr = new JsonArray();
		Gson gson = new Gson();

		String hash_id = request.getParameter("hash_id");
		String tid = request.getParameter("tid");
		String ap_no = request.getParameter("ap_no");

		// 입력 파라미터 로그 (디버깅용)
		System.out.println("hash_id: " + hash_id + ", tid: " + tid + ", ap_no: " + ap_no);

		map.put("hash_id", hash_id);
		map.put("tid", tid);
		map.put("ap_no", ap_no);

		if (hash_id != null && !hash_id.isEmpty() && tid != null && !tid.isEmpty() && ap_no != null && !ap_no.isEmpty()) {
		    List<SubPathVo> subPathVo = dao.selectSubPath(map);
		    
		    // 반환된 데이터 로그 (디버깅용)
		    for (SubPathVo vo : subPathVo) {
		        JsonObject jServerObject = new JsonObject(); // 매번 새 객체 생성
		        Map<String, Object> data = new HashMap<>(); // 매번 새 맵 생성

		        jServerObject.addProperty("id", vo.getIdx());
		        jServerObject.addProperty("text", vo.getName().trim()); // 공백 제거
		        jServerObject.addProperty("parent", vo.getParent_id());
		        jServerObject.addProperty("ap_no", vo.getAp_no());

		        String icon = request.getContextPath() + "/resources/assets/images/file.png";
		        jServerObject.addProperty("icon", icon);

		        if (vo.getFid() != null && !vo.getFid().isEmpty()) {
		            data.put("type", 1);
		            data.put("tid", vo.getTarget_id());
		        } else {
		            data.put("type", 0);
		            data.put("tid", vo.getTarget_id());
		        }
		        jServerObject.add("data", gson.toJsonTree(data));
		        jArr.add(jServerObject);
		    }
		} else {
		    logger.info("Invalid parameters: hash_id, tid, or ap_no is null or empty");
		}

		return jArr;
	}
	

	@Override
	public JsonArray selectNetList(Map<String, Object> map, HttpServletRequest request) throws Exception {
		logger.info("selectNetList");
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		int netid = Integer.parseInt(map.get("netid").toString());

		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		Gson gson = new Gson();
		
		List<GroupNetListVo> netList = null;
		
		switch (netid) {
		case 0: // 망
			// 서버 리스트
			netList = dao.selectNetList(map);
			
			for (int i = 0; i < netList.size(); i++) {
				GroupNetListVo vo = netList.get(i);
				jServerObject.addProperty("id", "TYPE" + vo.getId());
				jServerObject.addProperty("text", vo.getName());
				data.put("type", vo.getType());
				data.put("name", vo.getName());
				jServerObject.add("data", gson.toJsonTree(data));
				jServerObject.addProperty("parent", vo.getParent());
				jServerObject.addProperty("icon", request.getContextPath() + "/resources/assets/images/net.png");
				jArr.add(jServerObject);
			}
			
			break;
			
		case 1: // 그룹
			jServerObject.addProperty("id", "pc");
			jServerObject.addProperty("text", "PC");
			data.put("type", 99);
			jServerObject.add("data", gson.toJsonTree(data));
			jServerObject.addProperty("parent", "#");
			jArr.add(jServerObject);

			jServerObject.addProperty("id", "sktoa");
			jServerObject.addProperty("text", "OA망");
			data.put("type", 99);
			jServerObject.add("data", gson.toJsonTree(data));
			jServerObject.addProperty("parent", "pc");
			jArr.add(jServerObject);

			jServerObject.addProperty("id", "sktut");
			jServerObject.addProperty("text", "VDI");
			data.put("type", 99);
			jServerObject.add("data", gson.toJsonTree(data));
			jServerObject.addProperty("parent", "pc");
			jArr.add(jServerObject);

					
			netList = dao.selectGroupList(map);
			
			for (int i = 0; i < netList.size(); i++) {
				GroupNetListVo vo = netList.get(i);
				jServerObject.addProperty("id", vo.getId());
				jServerObject.addProperty("text", vo.getName());
				data.put("type", vo.getType());
				data.put("name", vo.getName());
				jServerObject.add("data", gson.toJsonTree(data));
				jServerObject.addProperty("parent", vo.getParent());
				jArr.add(jServerObject);
			}
			
			break;
					
		case 2: // PC
			netList = dao.selectNetPCList(map);
			
			for (int i = 0; i < netList.size(); i++) {
				GroupNetListVo vo = netList.get(i);
				
				String name = vo.getName();
				String platform = vo.getPlatform();
				
				jServerObject.addProperty("id", vo.getId());
				
				data.put("name", name);
				
				if (!vo.getAgent_connected_ip().equals("")) {
					name += " (" + vo.getAgent_connected_ip() + ")";
				}else {
					name += " (연결안됨)";
				}
				
				jServerObject.addProperty("text", name);
				
				data.put("type", vo.getType());
				
				jServerObject.add("data", gson.toJsonTree(data));
				jServerObject.addProperty("parent", vo.getParent());
				jServerObject.addProperty("icon", request.getContextPath() + "/resources/assets/images/pc_icon.png");
				jArr.add(jServerObject);
			}
			
			break;
			
		case 3: // OneDrive
			netList = dao.selectNetOneDriveList(map);
			
			for (int i = 0; i < netList.size(); i++) {
				GroupNetListVo vo = netList.get(i);
				String id = vo.getId();
				String location_id = vo.getLocation_id();
				jServerObject.addProperty("text", vo.getName());
				data.put("name", vo.getName());
				data.put("type", vo.getType());
				jServerObject.add("data", gson.toJsonTree(data));
				jServerObject.addProperty("target", id);
				jServerObject.addProperty("id", location_id);
				jServerObject.addProperty("parent", vo.getParent());
				jServerObject.addProperty("icon", request.getContextPath() + "/resources/assets/images/onedrive16px.png");
				jArr.add(jServerObject);
			}
			
			break;

		default:
			break;
		}
		
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
		Map<String, Object> data = new HashMap<String, Object>();
		
		logger.info("typeChk :: " +  map.get("typeChk").toString());  
		 
		String typeChk = map.get("typeChk").toString();
		
		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		Gson gson = new Gson();

		if (user_grade.equals("9")) {
			
			jServerObject = new JsonObject();    
			jServerObject.addProperty("id", "picenter");
			jServerObject.addProperty("text", "사용자 그룹");
			jServerObject.addProperty("parent", "#");
			jArr.add(jServerObject);

			// 서버 리스트
			List<GroupTomsVo> groupServer = dao.selectReportTomsGroup(map);
			for (GroupTomsVo vo : groupServer) {
				name = vo.getName();
				icon = "";
				
				JsonObject jSTObject = new JsonObject();
				data = new HashMap<String, Object>();
				  
				jSTObject.addProperty("id", vo.getIdx());
				jSTObject.addProperty("parent", vo.getUp_idx());
				
				data.put("type", vo.getType());
				jSTObject.add("data", gson.toJsonTree(data));
				jSTObject.addProperty("text", name);
				
				jArr.add(jSTObject);
				
			}
			
			try {
				List<GroupTomsVo> PICGroupList = dao.selectTomsUserGroup();
				for (GroupTomsVo vo : PICGroupList) {
					name = vo.getName();
					
					JsonObject jSTObject = new JsonObject();
					data = new HashMap<String, Object>();
					
					jSTObject.addProperty("id", vo.getIdx());
					// 상위 그룹인 경우
					if (vo.getUp_idx().equals("IDX_0")) {
						jSTObject.addProperty("parent", "picenter");
					}else {
						jSTObject.addProperty("parent", vo.getUp_idx());
					}  
					
					data.put("type", vo.getType());
					jSTObject.add("data", gson.toJsonTree(data));
					jSTObject.addProperty("text", name);
					
					jArr.add(jSTObject);
				}
			}catch (Exception e) {
				e.printStackTrace();
				logger.error("error :: " + e.getLocalizedMessage());
			}
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
		// TODO Auto-generated method stub
		String icon = "";
		String name = "";
		Map<String, Object> data = new HashMap<String, Object>();
		
		String typeChk = map.get("typeChk").toString();
		
		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		Gson gson = new Gson();

		if (user_grade.equals("9")) {
			
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
			
			if(typeChk.equals("0")) {
				
				// 서버 리스트
				List<GroupTomsVo> groupServer = dao.selectReportServerList(map);
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
					
					data.put("ap", vo.getAp_no());
					if(vo.getPlatform().equals("Remote Access Only")) {
						
						// 상위 그룹 일 경우
						if (vo.getUp_idx().equals("0")) {
							jSTObject.addProperty("parent", "db");
						} else { // 하위 그룹
							jSTObject.addProperty("parent", "db");
							
							if(vo.getType() == 1) {
								data.put("target_id", vo.getTarget_id());
								icon = request.getContextPath() + "/resources/assets/images/db.png";
								
								data.put("name", vo.getName());
								jSTObject.addProperty("icon", icon);
							}
							
						}
					}else {
						// 상위 그룹 일 경우
						if (vo.getUp_idx().equals("0")) {
							jSTObject.addProperty("parent", "server");
						} else { // 하위 그룹
							jSTObject.addProperty("parent", vo.getUp_idx());
							
							if(vo.getType() == 1) {
								data.put("target_id", vo.getTarget_id());
								
								// 서버 (그룹x)
								if (vo.getTarget_id() != null && !vo.getTarget_id().equals("")) {
									icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
								} else {
									name += "(미설치)";
									icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
								}
								data.put("name", vo.getName());
								jSTObject.addProperty("icon", icon);
							}
							
						}
					}
					
					data.put("type", vo.getType());
					jSTObject.add("data", gson.toJsonTree(data));
					jSTObject.addProperty("text", name);
					
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
					/*if (!vo.getAgent_connected_ip().equals("")) {
					name += " (" + vo.getAgent_connected_ip() + ")";
				}*/
					
					/*if(vo.getComdate() != null) {
					name += " (" + vo.getComdate() + ")";
				}*/
					
					if (vo.isAgent_connected() == true) {
						icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
					} else {
						icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
					}
					data.put("ap", "0");
					data.put("type", "1");
					JsonObject jSTObject = new JsonObject();
					jSTObject.addProperty("id", vo.getTarget_id());
					jSTObject.addProperty("text", name);
					jSTObject.addProperty("parent", "noGroup");
					jSTObject.addProperty("icon", icon);
					jSTObject.add("data", gson.toJsonTree(data));
					// jArr.add(jSTObject);
				}
			}else {
				jServerObject.addProperty("id", "noGroupPC");
				jServerObject.addProperty("text", "미분류");
				data.put("ap", 0);
				data.put("type", "0");
				jServerObject.add("data", gson.toJsonTree(data));
				jServerObject.addProperty("parent", "pc");
				jArr.add(jServerObject);
				
				logger.info("selectPCList");
				List<ScheduleServerVo> groupPC = dao.selectPCList(map);
				for(ScheduleServerVo vo : groupPC) {
					name = vo.getName();
					String platform = vo.getPlatform();
					
					/*if(vo.getAp_no() != 0 && platform.substring(0,5).equals("Apple") ) {
						name = vo.getMac_name();
					}else {
						name = vo.getName();
					}*/
					if (!vo.getAgent_connected_ip().equals("")) {
						name += " (" + vo.getAgent_connected_ip() + ")";
					}else {
						name += " (연결안됨)";
					}

					JsonObject jSTObject = new JsonObject();
					data.put("ap", vo.getAp_no());
					data.put("type", (vo.getType() != 1 ? 0 : 1));
					
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
						
						/*if(!vo.isAgent_connected()) {
							name += " (연결안됨)"; 
						}*/
						jSTObject.addProperty("icon", icon);
						data.put("location", vo.getLocation_id());
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
					jSTObject.add("data", gson.toJsonTree(data));
					jArr.add(jSTObject);

				}
			}
			
			// 서버 미분류 그룹 추가
			/*List<GroupTargetVo> notGroup = dao.selectNotGroup(map);
			for (GroupTargetVo vo : notGroup) {
				name = vo.getName();
				if (!vo.getAgent_connected_ip().equals("")) {
					name += " (" + vo.getAgent_connected_ip() + ")";
				}

				if (vo.isAgent_connected() == true) {
					icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
				} else {
					icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
				}
				data.put("ap", "0");
				data.put("type", "1");
				JsonObject jSTObject = new JsonObject();
				jSTObject.addProperty("id", vo.getTarget_id());
				jSTObject.addProperty("text", name);
				jSTObject.addProperty("parent", "noGroup");
				jSTObject.addProperty("icon", icon);
				jSTObject.add("data", gson.toJsonTree(data));
				jArr.add(jSTObject);
			}*/

			/*map.put("group", "pc");
			List<GroupPCTargetVo> pcTarget = dao.selectPCTarget(map);
			for (GroupPCTargetVo vo : pcTarget) {
				JsonObject jSTObject = new JsonObject();

				name = vo.getName();
				if (!vo.getAgent_connected_ip().equals("")) {
					name += " (" + vo.getAgent_connected_ip() + ")";
				}

				if (vo.isAgent_connected() == true) {
					icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
				} else {
					icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
				}

				data.put("ap", vo.getAp_no());
				data.put("type", "1");
				jSTObject.add("data", gson.toJsonTree(data));

				jSTObject.addProperty("id", vo.getTarget_id());
				jSTObject.addProperty("text", name);
				jSTObject.addProperty("icon", icon);
				jSTObject.addProperty("parent", "noGroupPC");
				jArr.add(jSTObject);
			}*/
		
		}

		/*logger.info(jArr.toString());*/

		return jArr.toString();
	}
	
	@Override
	public String userGroupList(Map<String, Object> map, HttpServletRequest request) throws Exception {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		map.put("user_no", user_no);
		map.put("user_grade", user_grade);
		// TODO Auto-generated method stub
		String icon = "";
		String name = "";
		Map<String, Object> data = new HashMap<String, Object>();
				
		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		Gson gson = new Gson();

		if (user_grade.equals("9")) {
			jServerObject.addProperty("id", "group");
			jServerObject.addProperty("text", "부서");
			jServerObject.addProperty("parent", "#");
			jArr.add(jServerObject);
			
			List<GroupTomsVo> groupUser = dao.selectUserGroup(map);
			for (GroupTomsVo vo : groupUser) {
				name = vo.getName();

				// 서버 타겟 오브젝트
				JsonObject jSTObject = new JsonObject();
				jSTObject.addProperty("id", vo.getIdx());
				
				if (vo.getUp_idx().equals("#")) {
					jSTObject.addProperty("parent", "group");
				} else { // 하위 그룹
					jSTObject.addProperty("parent", vo.getUp_idx());

					data.put("name", vo.getName());
				}
				
				data.put("type", vo.getType());
				jSTObject.add("data", gson.toJsonTree(data));
				jSTObject.addProperty("text", name);

				jArr.add(jSTObject);
			}
		}
			
		return jArr.toString();
	}
	
	@Override
	public String selectPopupServerGroupList(Map<String, Object> map, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		logger.info("map >>>> " + map);
		
		map.put("user_no", user_no);
		String icon = "";
		String name = "";
		Map<String, Object> data = new HashMap<String, Object>();

		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		Gson gson = new Gson();
		
		jServerObject.addProperty("id", "all");
		jServerObject.addProperty("text", "전체");
		jServerObject.addProperty("parent", "#");
		data.put("ap", 0);
		data.put("type", "0");
		jServerObject.add("data", gson.toJsonTree(data));
		jArr.add(jServerObject);
		
		
		List<ScheduleServerVo> serverList = new ArrayList<>();
		
		try {
			/*int is_server = dao.selectServer(map);*/
			
			serverList = dao.selectScheduleUserGroup1(map);
			/*
			if(is_server != 3) { // OneDrive가 아닌 경우
				serverList = dao.selectScheduleUserGroup1(map);
			}else {
				serverList = dao.selectScheduleUserGroup2(map);
			}
			*/
			
			for (ScheduleServerVo vo : serverList) {
				jServerObject = new JsonObject();
				jServerObject.addProperty("id", vo.getId());
				jServerObject.addProperty("text", vo.getName());
				data.put("type", "1");
				data.put("ap", vo.getAp_no());
				data.put("name", vo.getName());
				data.put("is_server", vo.getType());
				jServerObject.add("data", gson.toJsonTree(data));
				jServerObject.addProperty("parent", "all");
				
				
				if(vo.getType() == 0) { // PC
					if(vo.isAgent_connected()) {
						icon = request.getContextPath() + "/resources/assets/images/server_icon.png";
					}else {
						icon = request.getContextPath() + "/resources/assets/images/server_dicon.png";
					}
					
				}else if(vo.getType() == 1) { // 서버
					icon = request.getContextPath() + "/resources/assets/images/pc_icon.png";
					
				}else if(vo.getType() == 2) {
					
				}else if(vo.getType() == 3) { // OneDrive
					icon = request.getContextPath() + "/resources/assets/images/onedrive16px.png";
				}
				
				jServerObject.addProperty("icon", icon);
				jArr.add(jServerObject);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jArr.toString();
	}
	
	@Override
	public JsonArray selectLicenseGroup(Map<String, Object> map, HttpServletRequest request) throws Exception {

		String icon = "";
		String name = "";
		Map<String, Object> data = new HashMap<String, Object>();
		Gson gson = new Gson();
		
        String toDate = request.getParameter("toDate");
        String fromDate = request.getParameter("fromDate");
        map.put("toDate", toDate);
        map.put("fromDate", fromDate);

		Map<String, Object> serverMap = new HashMap<String, Object>();

		serverMap.put("toDate", toDate);
		serverMap.put("fromDate", fromDate);

		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		
		jServerObject.addProperty("id", "server");
		jServerObject.addProperty("text", "전체");
		jServerObject.addProperty("parent", "#");

		data.put("type", 0);
		data.put("parent_name", "");
		jServerObject.add("data", gson.toJsonTree(data));
		jArr.add(jServerObject);
		
		jServerObject.addProperty("id", "group");
		jServerObject.addProperty("text", "그룹");
		jServerObject.addProperty("parent", "server");

		jServerObject.add("data", gson.toJsonTree(data));
		jArr.add(jServerObject);
		
		// 서버 리스트
		List<LicenseGroupsVo> group = dao.selectLicenseList(map);
		
		for (LicenseGroupsVo vo : group) {
			name = vo.getName();

			// 서버 타겟 오브젝트
			JsonObject jSTObject = new JsonObject();
			jSTObject.addProperty("id", vo.getIdx()); 
			
			jSTObject.addProperty("parent", vo.getUp_idx().equals("0") ? "group" : vo.getUp_idx());
			
			String byteDt = "";
			String accByteDt = "";
			
			if(vo.getType() != 0) {
				icon = request.getContextPath() + "/resources/assets/images/server_icon.png";
				
				byteDt = String.valueOf(vo.getLicense_usage());
				accByteDt = String.valueOf(vo.getDiff_license_usage());
				jSTObject.addProperty("icon", icon);
				
			}else {
				byteDt = byteDataCompare(vo.getIdx(), group) + "";
				accByteDt = byteAccDataCompare(vo.getIdx(), group) + "";
			}
			
			
			data.put("type",vo.getType());
			data.put("team",vo.getTeam());
			data.put("usage", byteDt);
			data.put("diff_usage", accByteDt);
			data.put("parent_name", vo.getParent_group());
			
			name += numberFormat(accByteDt);
			
			jSTObject.addProperty("icon", icon);
			jSTObject.add("data", gson.toJsonTree(data));
			jSTObject.addProperty("text", name);

			jArr.add(jSTObject);
		}

		data = new HashMap<String, Object>();
		jServerObject.addProperty("id", "noGroup");
		jServerObject.addProperty("text", "미그룹");
		jServerObject.addProperty("parent", "server");

		data.put("type", 0);
		data.put("parent_name", "");
		jServerObject.add("data", gson.toJsonTree(data));
		jArr.add(jServerObject);
		

		jServerObject.addProperty("id", "deleteServer");
		jServerObject.addProperty("text", "삭제된서버"); 
		jServerObject.addProperty("parent", "server");

		data.put("type", 0);
		data.put("parent_name", "");
		jServerObject.add("data", gson.toJsonTree(data));
		jArr.add(jServerObject);
		
		return jArr;
	}
	
	// node : 자산 x, 날짜 o
	@Override
	public String selectPICenterServerDate(Map<String, Object> map, HttpServletRequest request) throws Exception {
		logger.info("selectPICenterServerDate");
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		String insa_code = SessionUtil.getSession("memberSession", "INSA_CODE");
		
		map.put("user_no", user_no);
		map.put("user_grade", user_grade);
		map.put("insa_code", insa_code);
		
		String icon = "";
		String name = "";
		Map<String, Object> data = new HashMap<String, Object>();

		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		Gson gson = new Gson();
		
		List<GroupTomsVo> groupServer = dao.selectPICenterServer(map);
		for (GroupTomsVo vo : groupServer) {
			name = vo.getName();
			
			JsonObject jSTObject = new JsonObject();
			data = new HashMap<String, Object>();
			
			jSTObject.addProperty("id", vo.getIdx());
			jSTObject.addProperty("parent", vo.getUp_idx());
			if(vo.getType() == 1) {// 대상인 경우
				data.put("target_id", vo.getTarget_id());
				data.put("location_id", vo.getLocation_id());
				data.put("ap", vo.getAp_no());
				data.put("core", vo.getCore());
				
				// 연결 상태 확인
				if(vo.isAgent_connected()) {
					icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
				} else {
					icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
				}
				
				
				// 연결 ip 표기
				if (!vo.getAgent_connected_ip().equals("")) {
					name += " (" + vo.getAgent_connected_ip() + ")";
				}
				
				// 마지막 검색 시간
				if(vo.getComdate() != null) {
					name += " (" + vo.getComdate() + ")";
				}
				
				data.put("name", vo.getName());
				jSTObject.addProperty("icon", icon);
				
				
				// 서버 구분을 위한 ip 표시
				name += " [0" + (vo.getAp_no()+1) + "]";
			}
			
			data.put("type", vo.getType());
			jSTObject.add("data", gson.toJsonTree(data));
			jSTObject.addProperty("text", name);
			
			jArr.add(jSTObject); 
		}
		
		if(user_grade.equals("9")) {
			jServerObject.addProperty("id", "picenter");
			jServerObject.addProperty("text", "사용자 그룹");
			data.put("type", "#");
			jServerObject.addProperty("parent", "#");
			jArr.add(jServerObject);
			
			try {
				List<GroupTomsVo> PICGroupList = dao.selectTomsUserGroup();
				for (GroupTomsVo vo : PICGroupList) {
					name = vo.getName();
					
					JsonObject jSTObject = new JsonObject();
					data = new HashMap<String, Object>();
					
					jSTObject.addProperty("id", vo.getIdx());
					// 상위 그룹인 경우
					if (vo.getUp_idx().equals("IDX_0")) {
						jSTObject.addProperty("parent", "picenter");
					}else {
						jSTObject.addProperty("parent", vo.getUp_idx());
						if(vo.getType() == 3) {// 대상인 경우
							data.put("target_id", vo.getTarget_id());
							data.put("ap", vo.getAp_no());
							
							// 연결 상태 확인
							if(vo.isAgent_connected()) {
								icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
							} else {
								icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
							}
							
							// 연결 ip 표기
							if (!vo.getAgent_connected_ip().equals("")&&!vo.getAgent_connected_ip().equals(" ")) {
								name += " (" + vo.getAgent_connected_ip() + ")";
							}
							
							data.put("name", vo.getName());
							jSTObject.addProperty("icon", icon);
							
							 
							// 서버 구분을 위한 ip 표시
							name += " [0" + (vo.getAp_no()+1) + "]";
						}
					}
					
					data.put("type", vo.getType());
					jSTObject.add("data", gson.toJsonTree(data));
					jSTObject.addProperty("text", name);
					
					jArr.add(jSTObject);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return jArr.toString();
	}
	
	// node : 자산 x, 날짜 x
	@Override
	public String selectPICenterServer(Map<String, Object> map, HttpServletRequest request) throws Exception {
		logger.info("selectPICenterServer");
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String insa_code = SessionUtil.getSession("memberSession", "INSA_CODE");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		map.put("user_no", user_no);
		map.put("user_grade", user_grade);
		map.put("insa_code", insa_code);
		
		String icon = "";
		String name = "";
		Map<String, Object> data = new HashMap<String, Object>();

		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		Gson gson = new Gson();

		List<GroupTomsVo> groupServer = dao.selectPICenterServer(map);
		for (GroupTomsVo vo : groupServer) {
			name = vo.getName();
			icon = "";
			
			JsonObject jSTObject = new JsonObject();
			data = new HashMap<String, Object>(); 
			
			jSTObject.addProperty("id", vo.getIdx());

			jSTObject.addProperty("parent", vo.getUp_idx());
			if(vo.getType() == 1) {// 대상인 경우
				data.put("target_id", vo.getTarget_id());
				data.put("ap", vo.getAp_no());
				
				// 연결 상태 확인
				if(vo.isAgent_connected()) {
					icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
				} else {
					icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
				}
				
				// 연결 ip 표기
				if (!vo.getAgent_connected_ip().equals("")&&!vo.getAgent_connected_ip().equals(" ")) {
					name += " (" + vo.getAgent_connected_ip() + ")";
				}
				
				data.put("name", vo.getName());
				jSTObject.addProperty("icon", icon);
				
				
				// 서버 구분을 위한 ip 표시
				name += "[0" + (vo.getAp_no()+1) + "]";
			}
		
			data.put("type", vo.getType());
			jSTObject.add("data", gson.toJsonTree(data));
			jSTObject.addProperty("text", name);
			
			jArr.add(jSTObject); 
		}
		if(user_grade.equals("9")) {
			jServerObject.addProperty("id", "picenter");
			jServerObject.addProperty("text", "사용자 그룹");
			data.put("type", "#");
			jServerObject.addProperty("parent", "#");
			jArr.add(jServerObject);
			
			try {
				List<GroupTomsVo> PICGroupList = dao.selectTomsUserGroup();
				for (GroupTomsVo vo : PICGroupList) {
					name = vo.getName();
					
					JsonObject jSTObject = new JsonObject();
					data = new HashMap<String, Object>();
					
					jSTObject.addProperty("id", vo.getIdx());
					// 상위 그룹인 경우
					if (vo.getUp_idx().equals("IDX_0")) {
						jSTObject.addProperty("parent", "picenter");
					}else {
						jSTObject.addProperty("parent", vo.getUp_idx());
						if(vo.getType() == 3) {// 대상인 경우
							data.put("target_id", vo.getTarget_id());
							data.put("ap", vo.getAp_no());
							
							// 연결 상태 확인
							if(vo.isAgent_connected()) {
								icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
							} else {
								icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
							}
							
							// 연결 ip 표기
							if (!vo.getAgent_connected_ip().equals("")&&!vo.getAgent_connected_ip().equals(" ")) {
								name += " (" + vo.getAgent_connected_ip() + ")";
							}
							
							data.put("name", vo.getName());
							jSTObject.addProperty("icon", icon);
							
							
							// 서버 구분을 위한 ip 표시
							name += " [0" + (vo.getAp_no()+1) + "]";
						}
					}
					
					data.put("type", vo.getType());
					jSTObject.add("data", gson.toJsonTree(data));
					jSTObject.addProperty("text", name);
					
					jArr.add(jSTObject);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jArr.toString();
	}
	
	// 자산
	@Override
	public String selectAccountServer(Map<String, Object> map, HttpServletRequest request) throws Exception {
		logger.info("selectAccountServer");
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		String insa_code = SessionUtil.getSession("memberSession", "INSA_CODE");
		
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		
		map.put("user_no", user_no);
		map.put("user_grade", user_grade);
		map.put("insa_code", insa_code);
		map.put("fromDate", fromDate);
		map.put("toDate", toDate);
		
		String icon = "";
		String name = "";
		Map<String, Object> data = new HashMap<String, Object>();
		
		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		Gson gson = new Gson();
		
//		jServerObject.addProperty("id", "server");
//		jServerObject.addProperty("text", "서버");
//		jServerObject.addProperty("parent", "#");
//		jArr.add(jServerObject);
//		
//		jServerObject.addProperty("id", "noGroup");
//		jServerObject.addProperty("text", "미그룹");
//		jServerObject.addProperty("parent", "#");
//		jArr.add(jServerObject);
		
		List<GroupTomsVo> groupServer = dao.selectAccountServer(map);
		for (GroupTomsVo vo : groupServer) {
			name = vo.getName();
			icon = "";
			
			JsonObject jSTObject = new JsonObject();
			data = new HashMap<String, Object>();
			
			jSTObject.addProperty("id", vo.getIdx());


			jSTObject.addProperty("parent", vo.getUp_idx());
			
			if(vo.getType() == 1) {// 대상인 경우
				data.put("target_id", vo.getTarget_id());
				data.put("ap", vo.getAp_no());
				
				// 연결 상태 확인
				if(vo.isAgent_connected()) {
					icon = request.getContextPath() + "/resources/assets/images/icon_con.png";
				} else {
					icon = request.getContextPath() + "/resources/assets/images/icon_dicon.png";
				}
				
				// 연결 ip 표기
				if (!vo.getAgent_connected_ip().equals("")) {
					name += " (" + vo.getAgent_connected_ip() + ")";
				}
				
				if(vo.getTarget_id() == null || vo.getTarget_id().equals("")) {
					name += " [미 설치]";
				}else {
					// 서버 구분을 위한 ip 표시
					name += " [0" + (vo.getAp_no()+1) + "]";
				}
				data.put("name", vo.getName());
				jSTObject.addProperty("icon", icon);
			}
			data.put("type", vo.getType());
			jSTObject.add("data", gson.toJsonTree(data));
			jSTObject.addProperty("text", name);
			
			jArr.add(jSTObject);
			
		}
		
		return jArr.toString();
	}
	
	@Override
	public String selectPICenterServerGroup(Map<String, Object> map, HttpServletRequest request) throws Exception {
		logger.info("selectPICenterServerGroup");
		
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		map.put("user_no", user_no);
		map.put("user_grade", user_grade);
		
		String icon = "";
		String name = "";
		Map<String, Object> data = new HashMap<String, Object>();
		
		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		Gson gson = new Gson();
		
		jServerObject.addProperty("id", "server");
		jServerObject.addProperty("text", "서버");
		jServerObject.addProperty("parent", "#");
		jArr.add(jServerObject);
		
		List<GroupTomsVo> groupServer = dao.selectPICenterServerGroup(map);
		for (GroupTomsVo vo : groupServer) {
			name = vo.getName();
			
			JsonObject jSTObject = new JsonObject();
			jSTObject.addProperty("id", vo.getIdx());
			
			if (vo.getUp_idx().equals("0"))  jSTObject.addProperty("parent", "server");
			else  jSTObject.addProperty("parent", vo.getUp_idx());
				
			data.put("type", vo.getType());
			jSTObject.add("data", gson.toJsonTree(data));
			jSTObject.addProperty("text", name);

			jArr.add(jSTObject);
		}
		
		return jArr.toString();
	}
		
	
	private long byteDataCompare(String idx, List<LicenseGroupsVo> licentscList) {
		long result = 0;
		
		for(LicenseGroupsVo vo : licentscList) {
			if(!vo.getIdx().equals(idx) && vo.getUp_idx().equals("0")) {
				continue;
			} 
			
			if(vo.getIdx().equals(idx)) {
				result += vo.getLicense_usage();
			} else if(vo.getUp_idx().equals(idx) && !vo.getUp_idx().equals("0")) {
				result += byteDataCompare(vo.getIdx(), licentscList);
			}
		}
		
		return result;
	}
	
	private long byteAccDataCompare(String idx, List<LicenseGroupsVo> licentscList) {
		long result = 0;
		
		for(LicenseGroupsVo vo : licentscList) {
			if(!vo.getIdx().equals(idx) && vo.getUp_idx().equals("0")) {
				continue;
			} 
			
			if(vo.getIdx().equals(idx)) {
				result += vo.getDiff_license_usage();
			} else if(vo.getUp_idx().equals(idx) && !vo.getUp_idx().equals("0")) {
				result += byteAccDataCompare(vo.getIdx(), licentscList);
			}
		}
		
		return result;
	}
	
	private long byteData(String idx, List<LicenseGroupVo> licentscList) {
		long result = 0;
		
		for(LicenseGroupVo vo : licentscList) {
			if(!vo.getIdx().equals(idx) && vo.getUp_idx().equals("0")) { 
				continue;
			}
			
			if(vo.getIdx().equals(idx)) {
				result += Long.parseLong(vo.getData_usage());
			} else if(vo.getUp_idx().equals(idx) && !vo.getUp_idx().equals("0")) {
				result += byteData(vo.getIdx(), licentscList);
			}
		}
		
		return result;
	}
	
	private String numberFormat(String dataSize) {
		String fomatSize = "";
		
		Double size = Double.parseDouble(dataSize);
		
		if(size > 0) {
			
			String[] s = {"bytes", "KB", "MB", "GB", "TB", "PB" };
			
			int idx = (int) Math.floor(Math.log(size) / Math.log(1000));
            DecimalFormat df = new DecimalFormat("#,###.##");
            double ret = ((size / Math.pow(1000, Math.floor(idx))));
            fomatSize = "("+ df.format(ret) + " " + s[idx] +")";
		}
		
		return fomatSize;
	}
	
	private String numberFormater(Long dataSize) {
		String fomatSize = "";
		
		Double size = (double) dataSize;
		
		if(size > 0) {
			
			String[] s = {"bytes", "KB", "MB", "GB", "TB", "PB" };
			
			int idx = (int) Math.floor(Math.log(size) / Math.log(1000));
			DecimalFormat df = new DecimalFormat("#,###.##");
			double ret = ((size / Math.pow(1000, Math.floor(idx))));
			fomatSize = "("+ df.format(ret) + " " + s[idx] +")";
		}
		
		return fomatSize;
	}
	
	private String sumName(String idx, String up_idx, List<LicenseGroupVo> groupList) {
		String result = "";
		
		for(LicenseGroupVo vo : groupList) {
			
			if(vo.getIdx().equals(idx)) {
				if(result.equals("")) {
					result += vo.getName() ;
				}else {
					result += " / "+ vo.getName();
				}
			}else if(vo.getIdx().equals(up_idx)) {
				if(result.equals("")) {
					result += sumName(vo.getIdx(), vo.getUp_idx(),groupList);
				}else {
					result = sumName(vo.getIdx(), vo.getUp_idx(),groupList) + "/" + result;
				}
			}
		}
		return result;
	}
	
	private String sumName2(String idx, String up_idx, List<LicenseGroupVo> groupList, String name) {
		String result = "";
		
		for(LicenseGroupVo vo : groupList) {
			if(vo.getIdx().equals(idx) && vo.getIdx().equals("0") && vo.getUp_idx().equals("0")) {
				continue;
			}
			
			if(vo.getIdx().equals(up_idx) && !vo.getIdx().equals("0") ) {
				
				if(result.equals("")) {
					result = sumName(vo.getIdx(), vo.getUp_idx(), groupList);
				}else {
					result = sumName(vo.getIdx(), vo.getUp_idx(), groupList) + " / " + result;
				}
			}
			
		}
		
		return result;
	}
	
	public JsonArray selectNASList(Map<String, Object> map, HttpServletRequest request) throws Exception {
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_grade = SessionUtil.getSession("memberSession", "USER_GRADE");
		
		map.put("user_no", user_no);
		map.put("user_grade", user_grade);
		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		Gson gson = new Gson();
		
		try {
			List<Map<String,Object>> list = dao.selectNASList(map);

			String icon = "";
			String name = "";
			
			for (Map<String, Object> data : list) {
				
				jServerObject = new JsonObject();  
				
				name = data.get("NAME").toString();
				
				String up_idx = (String) data.get("UP_IDX");
				int type = Integer.parseInt(data.get("TYPE").toString());
				
				if(type == 1) {
					
					if (data.get("TARGET_ID")!= null && !data.get("TARGET_ID").equals("")) {
						name += " [0" + (Integer.parseInt(data.get("AP_NO").toString())+1) +"]"; 
					}
					
					if (!data.get("AGENT_CONNECTED_IP").equals("")) {
						name += " | " + data.get("AGENT_CONNECTED_IP");
					}
					
					icon =request.getContextPath() + "/resources/assets/images/server_icon.png";
				} else if(type == 2 ) {
					
					if(data.get("COMDATE") != null && ! data.get("COMDATE").equals("")) {
						name += " | " + data.get("COMDATE") ;
					}else {
						name += " | 미검색";
					}
					
					icon =request.getContextPath() + "/resources/assets/images/file.png";
				}
				
				jServerObject.addProperty("icon", icon);
				data.put("type", type);
				data.put("ap", data.get("AP_NO"));
				data.put("name", data.get("NAME"));
				data.put("target_id", data.get("TARGET_ID"));
				data.put("location_id", data.get("LOCATION_ID"));
				data.put("agent_connected_ip", data.get("AGENT_CONNECTED_IP"));
				
				String id = data.get("IDX").toString();
				
				jServerObject.addProperty("id", id);
				jServerObject.addProperty("text", name);
				jServerObject.add("data", gson.toJsonTree(data));
				jServerObject.addProperty("parent", up_idx);

				jArr.add(jServerObject);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		 
		logger.info("jArr :: " + jArr);
		
		return jArr;
	}

	@Override
	public List<Map<String, Object>> selectMngrList() throws Exception {
		return dao.selectMngrList();
	}

	@Override
	public Map<String, Object> chkAccountCnt() throws Exception {
		return dao.chkAccountCnt();
	}
	
	public JsonArray selectDBGroupList(Map<String, Object> map, HttpServletRequest request) throws Exception {
		JsonArray jArr = new JsonArray();
		JsonObject jServerObject = new JsonObject();
		Gson gson = new Gson();
		
		jServerObject.addProperty("id", "db");
		jServerObject.addProperty("text", "DB");
		jServerObject.addProperty("parent", "#");
		jArr.add(jServerObject);
		
		try {
			List<Map<String,Object>> list = dao.selectDBServers(map);

			String icon = request.getContextPath();
			
			for (Map<String, Object> data : list) {
				jServerObject = new JsonObject(); 
				int type = Integer.parseInt(data.get("TYPE").toString());
				
				
				data.put("type", type);
				data.put("ap", data.get("AP_NO"));
				data.put("name", data.get("NAME"));
				data.put("is_server", 1);
				data.put("target_id", data.get("UP_IDX"));
				data.put("location_id", data.get("ID"));
				String id = data.get("ID").toString();
				
				if(type == 0) {
					jServerObject.addProperty("icon", icon + "/resources/assets/images/server_icon.png");
				} else {
					// 로케이션 아이디가 동일한 경우가 많음
					id = data.get("UP_IDX") + "_" + data.get("ID");
					jServerObject.addProperty("icon", icon + "/resources/assets/images/db.png");
				} 
				
				jServerObject.addProperty("id", id);
				jServerObject.addProperty("text", data.get("NAME").toString());
				jServerObject.add("data", gson.toJsonTree(data));
				jServerObject.addProperty("parent", data.get("UP_IDX").toString());

				jArr.add(jServerObject);
			}
			
		}catch (Exception e) {
			logger.error("error " + e.getMessage()); 
		}
		
		
		return jArr;
	}

	@Override
	public String getGroupList(Map<String, Object> map, HttpServletRequest request) throws Exception {
		JsonArray jArr = new JsonArray();
		List<GroupTomsVo> groupServer = dao.selectPICenterServerGroup(map);
		for (GroupTomsVo vo : groupServer) {
			jArr.add(vo.getIdx());
		}
		jArr.add("noGroup");
		
		return jArr.toString();
	}
}
