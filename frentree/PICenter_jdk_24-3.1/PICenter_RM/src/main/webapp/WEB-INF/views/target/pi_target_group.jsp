<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="../../include/header.jsp"%>
 
<style>
@media screen and (-ms-high-contrast: active), (-ms-high-contrast: none) {
	.mxWindowPane .user_info td, .user_info th{
		width: 26% !important;
	}
}
 </style>
		<!-- section -->
		<section>
			<!-- container -->
			<div class="container" >
			<h3>대상 그룹 관리</h3>
			<%-- <%@ include file="../../include/menu.jsp"%> --%>
				<!-- content -->
				<div class="content magin_t25">
					<div class="left_area2" style="width: 557px;">
						<h3 style="top: 5px; padding: 0; display: inline;" id="name_toms">자산 현황</h3>
						<h3 style="top: 5px; padding: 0; display: inline;" id="acc_cnt"></h3>
						<table class="user_info narrowTable" style="width: 250px; min-width: 0; float: right;">	
							<caption>타겟정보</caption>
                   			<tbody>
                   				<tr>
                   					 <th style="text-align: center; width: 0px; border-radius: 0.25rem;">호스트명</th>
	                   			     <td style="width:0%">
                       				 <input type="text" style="width: 147px; padding-left: 5px;" size="10" id="assetHostsSeach" placeholder="호스트명을 입력하세요.">
	                      			 </td>
	                      			 <td style="width:0%;">
                           			 <input type="button" name="button" class="btn_look_approval" id="assetHostsBtn" style="margin-top: 5px;">
                           			</td>
                   				</tr>
                   			</tbody>
						</table> 
						<div class="left_box2" id="grid_h1" style="overflow: hidden; max-height: 666px; height: 666px; margin-top: 20px;">
		   					 <div class="select_location" style="overflow-y: auto; overflow-x: auto; height: 100%; background: #ffffff;">
		   					 	<div id="group_tree"></div>
		   					 </div> 
						</div>
					</div>
					<div class="left_area2" style="width: 539px; margin-left:67px">
						<h3 style="top: 5px; padding: 0; display: inline;" id="name_target">타겟 리스트</h3>
						<table class="user_info narrowTable" style="width: 140px; min-width: 0; margin-top: 5px; border: none; float: right;">	
							<caption>타겟정보</caption>
                   			<tbody>
                   				<tr>
                                   <td style="width: 100%; padding: 0 !important;"><input type="button" class="btn_target_group" id="selTargetReset" value="초기화" style="margin-left: 20px;"></td>
<!--                                    <td style="width: 100%; padding: 0 !important;"><input type="button" class="btn_target_group" id="movTargetGrp" value="그룹이동" style="margin-left: 20px;"></td> -->
                                   <td style="width: 100%; padding: 0 !important;"><input type="button" class="btn_target_group" id="addTarget" value="저장" style="margin-left: 20px;"></td>
                   				</tr>
                   			</tbody>
						</table>
						<input type="hidden" id="group_id" value=""/>
						<input type="hidden" id="group_name" value=""/>
						<div class="left_box2" id="grid_h1" style="overflow: hidden; max-height: 666px; height: 666px; margin-top: 20px;">
		   					<div id="div_target" class="select_location" style="overflow-y: auto; overflow-x: auto; height: 100%;background: #ffffff; padding: 0 0 0 0">
								 
                                 <!-- <table class="tbl_input" id="tbl_target"> -->
                                 <table> 
                                   <colgroup>
                                     <col width="*">
                                     <col width="100px">
                                    </colgroup>
									<tbody id="add_target">
                                    </tbody>
								</table> 
							</div>
						</div>
					</div>
					<div class="grid_top" style="width: 539px; float: right; display: inline-block;">
							<h3 style="top: 5px; padding: 0; display: inline;">사용자 그룹</h3>
							
							<table class="user_info narrowTable" style="width: 250px; min-width: 0; float: right;">	
							<caption>타겟정보</caption>
                   			<tbody>
                   				<tr>
                   					 <th style="text-align: center; width: 0px; border-radius: 0.25rem;">호스트명</th>
	                   			     <td style="width:0%">
                       				 <input type="text" style="width: 147px; padding-left: 5px;" size="10" id="userGroupSearch" placeholder="호스트명을 입력하세요.">
	                      			 </td>
	                      			 <td style="width:0%;">
                           			 <input type="button" name="button" class="btn_look_approval" id="userGroupBtn" style="margin-top: 5px;">
                           	</td>
                   				</tr>
                   			</tbody>
						</table> 
						<div id="div_noGroup" class="select_location" style="overflow-y: auto; height: 666px; margin-top: 20px; background: #ffffff; padding: 0 0 0 0">
							<!-- <table id="tbl_noGroup" class="tbl_input" id="location_table">
								<tbody>
								</tbody>
							</table> -->
							<div id="add_group_tree"></div>
						</div>
	   				</div>
				</div>
			</div>
			<!-- container -->
		</section>
		<!-- section -->
		
		<div id="sendMalDetail" class="popup_layer" style="display:none">
		<div class="popup_box" style="height: auto; width: 950px; padding: 10px; background: #f9f9f9; left: 50%; top: 50%; transform: translate(-50%, -50%); margin: 0;">
		<img class="CancleImg" id="btnCancleSendMailDetail" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png" style="z-index: 999;">
			<div class="popup_top" style="background: #f9f9f9;">
				<h1 style="color: #222; padding: 0; box-shadow: none;" id="mailPopTitle">메일 발송</h1>
			</div>
			<div class="popup_content">
				<div class="content-box" id="div_update_user" style="height: auto; background: #fff; border: 1px solid #c8ced3;">
					<table class="popup_tbl">
						<colgroup>
	                        <col width="100">
	                        <col width="*">
	                    </colgroup>
						<tbody id="popup_details">
							<!-- 발신자 (단일 선택) -->
							<tr>
								<th style="border-bottom: 1px solid #cdcdcd;">발신자</th>
								<td style="padding-left: 10px; border-bottom: 1px solid #cdcdcd; padding: 8px 10px;">
									<div style="display: flex; align-items: center; gap: 10px;">
										<div id="mailSenderDisplay" style="flex: 1; height: 30px; line-height: 30px; border: 1px solid #ddd; border-radius: 4px; padding: 0 10px; background: #fafafa;">
											<span class="empty-msg" style="color: #999; font-size: 12px;">발신자를 선택하세요</span>
										</div>
										<button type="button" id="btnSelectSender" style="width: 80px; height: 32px; cursor: pointer;">선택</button>
									</div>
									<input type="hidden" id="mailSenderData" value="">
								</td>
							</tr>
							<!-- 수신자 (다중 선택) -->
							<tr>
								<th style="border-bottom: 1px solid #cdcdcd;">수신자</th>
								<td style="padding-left: 10px; border-bottom: 1px solid #cdcdcd; padding: 8px 10px;">
									<div style="display: flex; align-items: center; gap: 10px;">
										<div id="mailReceiverList" style="flex: 1; min-height: 32px; max-height: 80px; overflow-y: auto; border: 1px solid #ddd; border-radius: 4px; padding: 5px 10px; background: #fafafa; line-height: 22px;">
											<span class="empty-msg" style="color: #999; font-size: 12px;">선택된 수신자가 없습니다</span>
										</div>
										<button type="button" id="btnSelectReceiver" style="width: 80px; height: 32px; cursor: pointer;">선택</button>
									</div>
									<input type="hidden" id="mailReceiverData" value="">
								</td>
							</tr>
							<!-- 참조 (다중 선택) -->
							<tr>
								<th style="border-bottom: 1px solid #cdcdcd;">참조</th>
								<td style="padding-left: 10px; border-bottom: 1px solid #cdcdcd; padding: 8px 10px;">
									<div style="display: flex; align-items: center; gap: 10px;">
										<div id="mailCcList" style="flex: 1; min-height: 32px; max-height: 80px; overflow-y: auto; border: 1px solid #ddd; border-radius: 4px; padding: 5px 10px; background: #fafafa; line-height: 22px;">
											<span class="empty-msg" style="color: #999; font-size: 12px;">선택된 참조자가 없습니다</span>
										</div>
										<button type="button" id="btnSelectCc" style="width: 80px; height: 32px; cursor: pointer;">선택</button>
									</div>
									<input type="hidden" id="mailCcData" value="">
								</td>
							</tr>
							<!-- 템플릿 선택 -->
							<tr>
								<th style="border-bottom: 1px solid #cdcdcd;">템플릿</th>
								<td style="padding-left: 10px; border-bottom: 1px solid #cdcdcd; padding: 8px 10px;">
									<select id="mailTemplate" style="width: 350px; height: 32px; padding: 0 5px;">
										<option value="">템플릿을 선택하세요</option>
									</select>
									<button type="button" id="btnManageTemplate" style="margin-left: 10px; height: 32px; padding: 0 15px; cursor: pointer;">관리</button>
								</td>
							</tr>
							<!-- 제목 -->
							<tr>
								<th style="border-bottom: 1px solid #cdcdcd;">제목</th>
								<td style="padding-left: 10px; border-bottom: 1px solid #cdcdcd; padding: 8px 10px;">
									<span style="font-weight: bold;">[PICenter]</span>
									<input type="text" id="mailTitle" style="width: 750px; padding: 5px 10px; border: 1px solid #ddd; border-radius: 4px;">
								</td>
							</tr>
							<!-- 내용 -->
							<tr>
								<th>내용</th>
								<td style="padding: 10px;">
									<div id="mailType" style="display: none;"></div>
									<div id="mailContentHidden" style="display: none;"></div>
 									<textarea rows="8" cols="114" id="mailContent" style="width: 100%; height: 280px; padding: 10px; resize: none; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box;"></textarea>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="popup_btn">
				<div class="btn_area" style="padding: 10px 0; margin: 0;">
					<button type="button" id="btnSave">발송</button>
					<button type="button" id="btnClose">닫기</button>
				</div>
			</div>
		</div>
	</div>

	<%@ include file="../../include/footer.jsp"%>

<!-- 템플릿 관리 팝업 -->
<div id="templateManagePopup" class="popup_layer" style="display:none; z-index: 1000;">
	<div class="popup_box" style="height: auto; width: 800px; padding: 10px; background: #f9f9f9; left: 50%; top: 50%; transform: translate(-50%, -50%); margin: 0;">
		<img class="CancleImg" id="btnCancleTemplateManage" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png" style="z-index: 999;">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 style="color: #222; padding: 0; box-shadow: none;">템플릿 관리</h1>
		</div>
		<div class="popup_content">
			<div class="content-box" style="height: auto; background: #fff; border: 1px solid #c8ced3; padding: 15px;">
				<!-- 템플릿 목록 -->
				<div style="margin-bottom: 15px;">
					<div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
						<span style="font-weight: bold;">템플릿 목록</span>
						<button type="button" id="btnNewTemplate" style="height: 28px; padding: 0 15px; cursor: pointer;">새 템플릿</button>
					</div>
					<div id="templateListArea" style="max-height: 150px; overflow-y: auto; border: 1px solid #ddd; border-radius: 4px;">
						<table style="width: 100%; border-collapse: collapse;">
							<thead>
								<tr style="background: #f5f5f5;">
									<th style="padding: 8px; border-bottom: 1px solid #ddd; text-align: left; width: 50%;">제목</th>
									<th style="padding: 8px; border-bottom: 1px solid #ddd; text-align: center; width: 25%;">선택</th>
									<th style="padding: 8px; border-bottom: 1px solid #ddd; text-align: center; width: 25%;">삭제</th>
								</tr>
							</thead>
							<tbody id="templateListBody">
								<!-- 템플릿 목록이 여기에 동적으로 추가됨 -->
							</tbody>
						</table>
					</div>
				</div>
				<!-- 템플릿 편집 영역 -->
				<div style="border-top: 1px solid #ddd; padding-top: 15px;">
					<div style="margin-bottom: 10px;">
						<label style="font-weight: bold; display: block; margin-bottom: 5px;">제목</label>
						<input type="hidden" id="templateIdx" value="">
						<input type="text" id="templateName" style="width: 100%; height: 32px; padding: 0 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box;">
					</div>
					<div style="margin-bottom: 10px;">
						<label style="font-weight: bold; display: block; margin-bottom: 5px;">내용</label>
						<textarea id="templateCon" style="width: 100%; height: 200px; padding: 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; resize: none;"></textarea>
					</div>
				</div>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area" style="padding: 10px 0; margin: 0;">
				<button type="button" id="btnSaveTemplate">저장</button>
				<button type="button" id="btnCloseTemplateManage">닫기</button>
			</div>
		</div>
	</div>
</div>
	
<div id="modGroupPopup" class="popup_layer" style="display:none">
	<div class="popup_box" style="height: 200px; width: 400px;">
		<div class="popup_top">
			<h1>그룹 정보 변경</h1>
		</div>
		<div class="popup_content">
			<div class="content-box" style="height: 200px;">
				<!-- <h2>세부사항</h2>  -->
				<!-- <textarea id="accessIP" class="edt_sch" style="border: 1px solid #cdcdcd; width: 380px; height: 130px; margin-top: 5px; margin-bottom: 5px; padding: 10px; resize: none;"></textarea> -->
				<table id="modify_tbl" class="tbl_input">
					<tbody>
						<tr>
							<th>그룹명</th>
						</tr>
						<tr>
							<td style="height:20px; vertical-align: baseline;">
								<input type="text" id="groupName_pop" class="edt_sch" style="border: 1px solid #cdcdcd; width: 340px; margin-top: 5px; margin-bottom: 5px; padding: 10px; resize: none; height: 10px;"/>
							</td>
						</tr>
						<tr>
							<th>
								<p style="text-align:center;">
									<input type="checkbox" name="remediation" value="X" />없음<input type="checkbox" style="margin-left: 20px;" name="remediation" value="0" />삭제<input type="checkbox" style="margin-left: 20px;" name="remediation" value="1" />암호화<input type="checkbox" style="margin-left: 20px;" name="remediation" value="2" />마스킹
								</p>
							</th>
						</tr>
					</tbody>	
				</table>
				<input type="hidden" id="groupIdx_ori" value=""/>
				<input type="hidden" id="groupName_ori" value=""/>
				<input type="hidden" id="remediation_ori" value=""/>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area">
				<button type="button" id="btnModGroupSave">저장</button>
				<button type="button" id="btnModGroupCancel">취소</button>
			</div>
		</div>
	</div>
</div>	


<!-- 팝업창 - 그룹이동 시작 -->
<div id="groupChangePopup" class="popup_layer" style="display:none;">
	<div class="popup_box" style="height: 200px; width: 400px; padding: 10px; background: #f9f9f9;">
	<img class="CancleImg" id="btnCancleGroupChangePopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 style="color: #222; padding: 0; box-shadow: none;">그룹 이동</h1>
		</div>
		<div class="popup_content">
			<div class="content-box" style="height: 80px; background: #fff; border: 1px solid #c8ced3;">
				<!-- <h2>세부사항</h2>  -->
				<table class="popup_tbl">
					<colgroup>
						<col width="100%">
						<%-- <col width="*"> --%>
					</colgroup>
					<tbody>
						<tr>
							<td>
							<select name="selGroup" id="selGroup" style="width: 95%;">
						 		<option value="">미분류</option>
						 		<c:forEach items="${targetGroups}" var="item">
						 			<option value="${item.IDX}">${item.NAME}</option>
						 		</c:forEach>
						 	</select>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area" style="padding: 10px 0; margin: 0;">
				<button type="button" id="btnGroupChangeSave">저장</button>
				<button type="button" id="btnGroupChangeCancel">취소</button>
			</div>
		</div>
	</div>
</div>
<!-- 팝업창 - 그룹이동 종료 -->
	
<script type="text/javascript">

var oGrid = $("#userGrid");

$(document).ready(function () {
	//$('#tbl_target').html(setGroupTarget([], 1, 1, ''));
	
	var cnt_html = "(" +  "${acc_cnt.PIC_CNT}" + "/" + "${acc_cnt.ACC_CNT}" + ")";
	
	$("#acc_cnt").html(cnt_html);
	
	$('#group_tree').jstree({
		// List of active plugins
		"core" : {
		    "animation" : 0,
		    "check_callback" : true,
			"themes" : { "stripes" : false },
			"data" : ${targetGroup},
		},
		"types" : {
		    "#" : {
		      "max_children" : 1,
		      "valid_children" : ["root"]
		    },
		    "default" : {
		      "valid_children" : ["default","file"]
		    },
		    "file" : {
		      "icon" : "glyphicon glyphicon-file",
		      "valid_children" : []
		    }
		},
		'search': {
	        'case_insensitive': false,
	        'show_only_matches' : true,
	        "show_only_matches_children" : true
	    },
		'plugins' : [/* "contextmenu" */,"search",
			"types",
			"unique",
			"checkbox",
			"changed"],
	})
    .bind('select_node.jstree', function(evt, data, x) {
    	var id = data.node.id;
    	//var type = data.node.data.type;
    	//var ap = data.node.data.ap;
    	var name =  data.node.text;
    	//$('#jstree').jstree(true).refresh();
    	
    }).bind("loaded.jstree", function(e, data) {
    	var nodes = $('#group_tree').jstree(true).get_json('#', { 'flat': true });
    	
    	var textMap = new Map();
   	    nodes.forEach(function(node) {
   	        // 대소문자 무시 및 양쪽 공백 제거하여 키 생성
   	        var key = node.text.trim().toLowerCase();
   	        if (!textMap.has(key)) {
   	            textMap.set(key, node.text);
   	        }
   	    });

   	    // Map 객체의 값들로 배열을 만들고 정렬
   	    var selectColData = Array.from(textMap.values());
        selectColData.sort(); 
         
    	$("#assetHostsSeach").autocomplete({
    		source : selectColData,
    		select : function(event, ui){
    		},
    		focus : function(event, ui){ 
    			 $(".ui-menu-item").removeClass("pic-grid-focus");
    			 
    			 $(".ui-menu-item").each(function() {
    				 $(".ui-menu-item").removeClass("pic-grid-focus ui-state-active");
    				 $(".ui-menu-item-wrapper:contains('" + ui.item.label + "')").closest(".ui-menu-item").addClass("pic-grid-focus");
    				 $(".ui-menu-item-wrapper").removeClass("ui-state-active"); 
    		        });
    			return false;
    		}
    	});
    }).on("changed.jstree", function (e, data) {
		//console.log(data);
	    //  console.log(data.changed.selected); // newly selected
	    //  console.log(data.changed.deselected); // newly deselected
	    
	    if(data.changed.selected.length != 0){
	    	var html = "";
	    	$.each($("#group_tree").jstree("get_checked",true),function(){
	    		var type = this.data.type;
	    		if(type == 1){
	    			console.log(this.data);
		    		$('#tr_'+this.id).remove();
	    			html += '<tr id="tr_'+this.id+'" data-treeid="'+this.id+'" data-targetname="'+this.data.name+'">';
	    			html += "<th style='text-align:left'> - " + this.data.name + "</th>";
	    			html += '<td>';
	    			html += "<button type='button' name='button' style='cursor:pointer; color:#1973ba; float:right;' onclick='fnCheckRemove(this);'>remove</button></td>";
	    			html += '</tr>';
	    		}
	    	});
	    	$('#add_target').append(html);
	    } else if(data.changed.deselected.length != 0) {
	    	$.each(data.changed.deselected, function(){
	    		var id = this.toString();
	    		$('#tr_'+id).remove();
	    	});
	    }
	});
	
console.log(${creUserGroup});
	$('#add_group_tree').jstree({
		// List of active plugins
		"core" : {
		    "animation" : 0,
		    "check_callback" : true,
			"themes" : { "stripes" : false },
			"data" : ${creUserGroup},
		},
		'contextmenu' : { 
			"items" : { 
				"create" : {
					"separator_before" : false, 
					"separator_after" : true, 
					"label" : "하위 그룹추가", 
					"action" : function(obj){
						 
    					var idx = $('#add_group_tree').jstree('get_selected');
						var node = $('#add_group_tree').jstree(true).get_node(idx);
    					
    					var type = node.data.type;
    					
    					if(type != 0) {
    						alert("그룹에만 추가하실수 있습니다.");
    						return;
    					}
    					
    					$("#add_group_tree").jstree(true).create_node(idx, null, "last", function (node) {
					        this.edit(node, '', function(e, result){
					        	
					        	if(e.text.trim() == ''){
					        		$('#add_group_tree').jstree(true).delete_node(node.id);
					        		return;
					        	}
					        	
					        	var idx =node.parent;
					        	if(idx == 'group'){
					        		idx = 0;
					        	}
					        	var postData ={
					            	"idx": idx,
					            	"name": e.text,
					            }
					            	
					            $.ajax({
					        		type: "POST",
					        		url: "/group/insertUserCreateGroup",
					        		//async : false,
					        		data : postData,
					        		dataType: "json",
					        	    success: function (resultMap) {
					        	    	console.log(resultMap);
					        	    	
					        	    	if(resultMap.resultCode == -1){
					                		return;
					                	}
					        	    	
					                	var data = JSON.parse(resultMap.data);
					                	$('#add_group_tree').jstree(true).settings.core.data = data;
					                	$('#add_group_tree').jstree(true).refresh();
					        	    },
					        	    error: function (request, status, error) {
					        	    	alert("그룹 추가에 실패하였습니다.");
					        	        console.log("ERROR : ", error);
					        		    }
					        	});
					        });
					    });
    					/* 
				    	console.log(idx);
						$('#addUserCreateGroupPopup').show(); */
					}, 
				}, 
				"update" : { 
					"separator_before" : false, 
					"separator_after" : true, 
					"label" : "수정", 
					"action" : function(obj){
    					var idx = $('#add_group_tree').jstree('get_selected');
    					var node = $('#add_group_tree').jstree(true).get_node(idx);
    					var name = node.text;
    					
    					var type = node.data.type;
    					
    					if(node.id == 'group'){
    						alert("최상위 경로는 변경이 불가능 합니다.");
    						return;
    					}
    					
    					if(type != 0) {
    						alert("그룹만 변경하실수 있습니다.");
    						return;
    					}
    					
    					$('#add_group_tree').jstree(true).edit(idx, node.text, function(e, result){
    						if(result == false){
    							alert("같은 이름의 그룹이 존재합니다.");
    						}
    						var postData ={
    						   	"idx": e.id,
    						   	"name": e.text,
    						   }
    						$.ajax({
    							type: "POST",
    							url: "/group/updateUserCreateGroup",
   								//async : false,
   								data : postData,
   								dataType: "json",
   							    success: function (resultMap) {
   							    	console.log(resultMap);
   							    	
   							    	if(resultMap.resultCode == -1){
   							    		alert("업데이트 실패하였습니다.")
   					    	    		return;
   					    	    	}
   							    	
   							    },
   							    error: function (request, status, error) {
   							    	alert("그룹 업데이트 실패하였습니다.");
   							        console.log("ERROR : ", error);
   							    }
   							});
    					});
						//$("#modGroupPopup").show();
					},
				},
				"delete" : { 
					"separator_before" : false, 
					"separator_after" : true, 
					"label" : "삭제", 
					"action" : function(obj){
    					var idx = $('#add_group_tree').jstree('get_selected');
    					var node = $('#add_group_tree').jstree(true).get_node(idx[0]);
    					var groupArr = [];
    					var serverArr = [];
    					
						if(idx.length < 1){
							alert("그룹을 선택해 주세요.");
							return;
						}
						
						if(node.id == 'group'){
    						alert("최상위 그룹은 삭제가 불가능 합니다.");
    						return;
    					}
						
						var msg = "";
						if(node.parent == 'group'){
							msg = confirm("그룹을 삭제하시겠습니까?");
    					}else{
							msg = confirm("선택한 대상을 삭제하시겠습니까?");
    						
    					}
    					if(msg){
    						
    						// 선택한 삭제될 그룹
    						$.each(idx, function(index, element) {
    							node = $('#add_group_tree').jstree(true).get_node(element);

    							var type = node.data.type;
    							var id = node.id;
    							
    							if(type == 0){	// 삭제 목록이 그룹일경우
									groupArr.push(id);
								} else {		// 삭제 목록이 타겟일 경우
									var position = id.indexOf("_", 0);

									serverArr.push({
										"groupID": id.substring(0, position),
										"serverID": id.substring(position+1)
									});
								}
    							
    							// 선택한 삭제될 그룹의 하위 그룹
    							$.each(node.children, function(index, data) {
    								var nodes = $('#add_group_tree').jstree(true).get_node(data);
    								var types = nodes.data.type;
    								var idxs = nodes.id;
    								
    								if(types == 0){		// 삭제 목록이 그룹일경우
    									groupArr.push(idxs);
    								} 
    							});
    							
    						});
    						
    						var postData = {
   								"groupArr": JSON.stringify(groupArr),
   								"serverArr": JSON.stringify(serverArr),
    						};
    							
							$.ajax({
    							type: "POST",
    							url: "/group/deleteUserCreateGroup",
   								//async : false,
   								data : postData,
   								//dataType: "json",
   							    success: function (resultMap) {
   							    	console.log(resultMap);
   							    	
   							    	if(resultMap.resultCode == -1){
   							    		alert("업데이트 실패하였습니다.")
   					    	    		return;
   					    	    	}

				                	var data = JSON.parse(resultMap.data);
				                	$('#add_group_tree').jstree(true).settings.core.data = data;
				                	$('#add_group_tree').jstree(true).refresh();
				                	
				                	alert("서버/그룹 삭제가 완료되었습니다.")
   							    	
   							    },
   							    error: function (request, status, error) {
   							    	alert("그룹 삭제에 실패하였습니다.");
   							        console.log("ERROR : ", error);
   							    }
   							});
    						
    						
        					//$('#add_group_tree').jstree(true).delete_node(idx);
    					}

						//$("#modGroupPopup").show();
					},
				},
				"mail" : {
					"separator_before" : false,
					"separator_after" : true,
					"label" : "메일 발송",
					"action" : function(obj){
						var idx = $('#add_group_tree').jstree('get_selected');
						var node = $('#add_group_tree').jstree(true).get_node(idx[0]);

						if(idx.length < 1){
							alert("그룹을 선택해 주세요.");
							return;
						}

						if(node.id == 'group'){
							alert("최상위 그룹은 메일 발송이 불가합니다.");
							return;
						}

						$("#mailType").val("1");
						$("#mailTitle").val("");
						$("#mailContent").val("");
						$("#mailTemplate").val("");
						$("#mailPopTitle").text("메일 발송");
						loadMailTemplateList();
						$("#sendMalDetail").show();
					}
				}
			}
		},
		'search': {
	        'case_insensitive': false,
	        'show_only_matches' : true,
	        "show_only_matches_children" : true
	    },
		'plugins' : [ 
			"contextmenu",
			"search",
			"types",
			"unique",],
	})
    .bind('select_node.jstree', function(evt, data, x) {
    	var id = data.node.id;
    	//var type = data.node.data.type;
    	var ap = data.node.data.ap;
    	var name =  data.node.text;
    	//$('#jstree').jstree(true).refresh();
    	
    }).bind("loaded.jstree", function(e, data) {
    	var nodes = $('#add_group_tree').jstree(true).get_json('#', { 'flat': true });
    	
    	var textMap = new Map();
   	    nodes.forEach(function(node) {
   	        // 대소문자 무시 및 양쪽 공백 제거하여 키 생성
   	        var key = node.text.trim().toLowerCase();
   	        if (!textMap.has(key)) {
   	            textMap.set(key, node.text);
   	        }
   	    });

   	    // Map 객체의 값들로 배열을 만들고 정렬
   	    var selectColData = Array.from(textMap.values());
        selectColData.sort(); 
         
    	$("#userGroupSearch").autocomplete({
    		source : selectColData,
    		select : function(event, ui){
    		},
    		focus : function(event, ui){ 
    			 $(".ui-menu-item").removeClass("pic-grid-focus");
    			 
    			 $(".ui-menu-item").each(function() {
    				 $(".ui-menu-item").removeClass("pic-grid-focus ui-state-active");
    				 $(".ui-menu-item-wrapper:contains('" + ui.item.label + "')").closest(".ui-menu-item").addClass("pic-grid-focus");
    				 $(".ui-menu-item-wrapper").removeClass("ui-state-active"); 
    		        });
    			return false;
    		}
    	});
    });
	
	
	$('#btnSave').click(function(){

		var idx = $('#add_group_tree').jstree('get_selected');
		var node = $('#add_group_tree').jstree(true).get_node(idx[0]);
		var assetnosch = [];
		var apnosch = [];
		var groupArr = [];
		var serverArr = [];

		var chkArray = new Array();
		var mailReceiverData = $("#mailReceiverData").val();
		if(mailReceiverData && mailReceiverData.trim().length > 0) {
			chkArray = mailReceiverData.split(',').filter(function(v) { return v.trim() !== ''; });
		} else {
			$('input:checkbox[name=receiveUser]:checked').each(function() {
				chkArray.push(this.value);
			});
		}

		var detailCon  = $("#mailContent").val().trim().replace(/\n\r?/g,"\n<br>")  ;

	 	$.each(idx, function(index, element) {
			node = $('#add_group_tree').jstree(true).get_node(element);

			console.log(node);

			// 그룹 선택 시
			if(node.data != null && node.data.type == 0 ){
				$.each(node.children, function(index, data) {
					var childNode = $('#add_group_tree').jstree(true).get_node(node.children[index]);
					if(childNode && childNode.data && childNode.data.target_id){
						assetnosch.push(childNode.data.target_id);
						apnosch.push(childNode.data.ap_no != null ? childNode.data.ap_no : 0);
					}

				});

			}else{ // 타겟 선택 시
				if(node.data && node.data.target_id){
					assetnosch.push(node.data.target_id);
					apnosch.push(node.data.ap_no != null ? node.data.ap_no : 0);
				}
			}
		});
	 	
	 	if(chkArray.length == 0){
			alert("수신자가 지정되지 않았습니다.");
			return;
		}
		
		if($("#mailTitle").val() == null || $("#mailTitle").val() == ""){
			alert("제목을 입력해주세요.");
			return;
		}
		if($("#mailContent").val() == null || $("#mailContent").val() == ""){
			alert("내용을 입력해주세요.");
			return;
		}
	 	
	 	var postData = {
			"assetnosch": JSON.stringify(assetnosch),
			"apnosch": JSON.stringify(apnosch),
			"detailCon" : detailCon,
			"mailTitle" : $("#mailTitle").val(),
			"mailType" : $("#mailType").val(),
			"receiveUser" : JSON.stringify(chkArray),
			"mailSender" : $("#mailSenderData").val(),
			"mailReceiver" : $("#mailReceiverData").val(),
			"mailCc" : $("#mailCcData").val(),
		};
	 		
		 var send_msg = confirm("메일 발송 하시겠습니까?");
		 	
		 console.log(postData);
		if(send_msg){
			
		 	$.ajax({
				type: "POST",
				url: "/mail/serverGroupMail",
					//async : false,
					data : postData,
					//dataType: "json",
				    success: function (resultMap) {
				    	console.log(resultMap);
					    	
				        if (resultMap.resultCode != 0) {
					        alert(resultMap.resultMessage);
					        return;
					    } 
					        
						if (resultMap.resultCode == 0) {
					    	alert(resultMap.resultMessage);
					    }
						
						$("#mailContent").val("");
	                	$("#mailTitle").val("");
	                	$("#mailContentHidden").val("");
	                	$("#mailSenderData").val("");
	                	$("#mailSenderDisplay").text("선택된 발신자 없음");
	                	$("#mailReceiverData").val("");
	                	$("#mailReceiverList").html("");
	                	$("#mailCcData").val("");
	                	$("#mailCcList").html("");
	                	$("#sendMalDetail").hide();
	               	
				    },
				    error: function (request, status, error) {
				    	alert("메일 발송에 실패하였습니다.");
				        console.log("ERROR : ", error);
				    }
			}); 
		}
	 		
	});
	
	/* $('#btnSave').click(function(){
		var sdfds  = "</p><p>"+ $("#mailContent").val().trim().replace(/\n\r?/g,"</p><p>") +"</p>";
		
		console.log($("#mailContent").val());
		console.log(sdfds);
		
		
		
	}); */
	
	//선택 그룹 초기화
	$('#selTargetReset').click(function(){
		$('#group_tree').jstree().uncheck_all(true);
	});
	
	var treeArr = [];
	
	// 그룹이동
    $('#movTargetGrp').click(function(){
    	treeArr = [];
    	var selectTarget = $("#add_target").children("tr");
    	
    	if(selectTarget.length == 0){
    		alert("그룹 이동을 할 서버를 선택해주세요.");
    		return;
    	}
		
		$.each($("#group_tree").jstree("get_checked",true),function(){
			var id = this.data.target_id;
			var ap = this.data.ap;
			if(id != null && id != "") {
				treeArr.push({ target_id: id, ap_no: ap });
			}
		});
		
		$('#group_id').val(treeArr);
    	
    	$('#groupChangePopup').show();
    	
    });

	//그룹 이동 저장
	$('#btnGroupChangeSave').click(function(){
		var selectTarget = $("#add_target").children("tr");	
		console.log(treeArr);
		// 그룹이동 아이디
		var group_id = $("#selGroup option:selected").val();
		
		$('#groupChangePopup').hide();
		
		$.ajax({
			type: "POST",
			url: "/group/moveTargetGroup",
			//async : false,
			data : {
				group_id: group_id,
				treeArr: JSON.stringify(treeArr)
			},
		    success: function (resultMap) {
		    	console.log(resultMap);
		    	
		    	if(resultMap.resultCode == -1){
    	    		return;
    	    	}
		    	
    	    	var data = JSON.parse(resultMap.data);
    	    	$('#group_tree').jstree(true).settings.core.data = data;

    			$('#group_tree').jstree().uncheck_all(true);
    	    	$('#group_tree').jstree(true).refresh();
    	    	
    	    	treeArr = [];
		    },
		    error: function (request, status, error) {
		    	alert("그룹 변경 실패하였습니다.");
		        console.log("ERROR : ", error);
		        treeArr = [];
		    }
		});
	});
	
	// 그룹이동
    $('#movNotTargetGrp').click(function(){
    	treeArr = [];
    	$.each($("#not_group_tree").jstree("get_checked",true),function(){
    		var type = this.data.type;
    		
    		if(type == 1){
    			treeArr.push(this.id);
    		}
    	});
    	
    	if(treeArr.length == 0){
    		alert("그룹 이동을 할 서버를 선택해주세요.");
    		return;
    	}
    	

		$('#group_id').val(treeArr);
    	$('#groupChangePopup').show();
    	
    });
	
	
	// 그룹 이동 취소
	$('#btnGroupChangeCancel').click(function(){
		$('#groupChangePopup').hide();
	});
	
	$('#btnCancleGroupChangePopup').click(function(){
		$('#groupChangePopup').hide();
	});
    
    $('#addTarget').click(function(){
    	treeArr = [];
    	var groupArr = [];
    	var selectTarget = $("#add_target").children("tr");
    	
    	if(selectTarget.length == 0){
    		alert("그룹 이동을 할 서버를 선택해주세요.");
    		return;
    	}
    	
		// 서버 선택 아이디 추가
		$.each($("#group_tree").jstree("get_checked",true),function(){
			var id = this.data.target_id;
			var ap = this.data.ap;
			if(id != null && id != ""){
				treeArr.push({ target_id: id, ap_no: ap });
			}
			
		});
    	
		var idx = $('#add_group_tree').jstree('get_selected');
		var node = $('#add_group_tree').jstree(true).get_node(idx[0]);
    	
    	if(node.id == 'group'){
			alert("최상위 그룹에 추가는 불가능 합니다.");
			return;
		}
		if(idx.length < 1){
			alert("그룹을 선택해 주세요.");
			return;
		}
		
		// 서버가 추가될 선택한 그룹
		$.each(idx, function(index, element) {
			node = $('#add_group_tree').jstree(true).get_node(element);
			var name = node.text;
			var type = node.data.type;
			
			if(type == 0){
				groupArr.push(node.id);
			}
		});
		
		console.log(groupArr);
		
		var msg = confirm(groupArr.length + "개의 그룹에 저장하시겠습니까?");
		
		if(msg){
			$.ajax({
				type: "POST",
				url: "/group/insertUserTargets",
				//async : false,
				data : {
					"groupArr": JSON.stringify(groupArr),
					"treeArr": JSON.stringify(treeArr)
				},
			    success: function (resultMap) {
			    	console.log(resultMap);
			    	
			    	if(resultMap.resultCode == -1){
			    		alert("그룹 저장에 문제가 발생하였습니다.")
	    	    		return; 
	    	    	}
			    	location.reload(true); 
			    	
			    },
			    error: function (request, status, error) {
			    	alert("서버 저장이 실패하였습니다.");
			        console.log("request : ", request);
			        console.log("ERROR : ", error);
			        treeArr = [];
	    	    	groupArr = [];
			    }
			});
		}
		
    });
    
});

/* 자산현황 호스트 검색 시작 */
var to = true;
$('#assetHostsBtn').on('click', function(){
    var v = $('#assetHostsSeach').val();
	
	if(to) { clearTimeout(to); }
    to = setTimeout(function () {
      $('#group_tree').jstree(true).search(v);
    }, 250);
});

$('#assetHostsSeach').keyup(function (e) {
	var v = $('#assetHostsSeach').val();
	if (e.keyCode == 13) {   
		$(".ui-autocomplete").hide(); 
    	if(to) { clearTimeout(to); }
        to = setTimeout(function () {
          $('#group_tree').jstree(true).search(v);
        }, 250);
    }
});
/* 자산현황 호스트 검색 종료 */

// 발신자 선택 버튼 클릭
$('#btnSelectSender').on('click', function(){
    window.open('/popup/userList?info=mail_sender', 'userListPopup', 'width=750,height=550,scrollbars=yes');
});

// 수신자 선택 버튼 클릭
$('#btnSelectReceiver').on('click', function(){
    window.open('/popup/userList?info=mail_receiver', 'userListPopup', 'width=750,height=550,scrollbars=yes');
});

// 참조 선택 버튼 클릭
$('#btnSelectCc').on('click', function(){
    window.open('/popup/userList?info=mail_cc', 'userListPopup', 'width=750,height=550,scrollbars=yes');
});

// ============== 템플릿 관리 기능 ==============

// 템플릿 목록 로드
function loadMailTemplateList() {
    $.ajax({
        type: "POST",
        url: "/mail/mailTemplateList",
        success: function(resultMap) {
            if(resultMap.resultCode == 0) {
                var list = resultMap.data;
                var html = '';
                
                // 메일 발송 모달의 select 옵션 갱신
                var selectHtml = '<option value="">템플릿을 선택하세요</option>';
                
                if(list.length == 0) {
                    html = '<tr><td colspan="3" style="padding: 20px; text-align: center; color: #999;">등록된 템플릿이 없습니다.</td></tr>';
                } else {
                    for(var i = 0; i < list.length; i++) {
                        var item = list[i];
                        html += '<tr data-idx="' + item.IDX + '">';
                        html += '<td style="padding: 8px; border-bottom: 1px solid #eee;">' + item.NAME + '</td>';
                        html += '<td style="padding: 8px; border-bottom: 1px solid #eee; text-align: center;">';
                        html += '<button type="button" class="btnSelectTemplate" data-idx="' + item.IDX + '" style="padding: 3px 10px; cursor: pointer;">선택</button>';
                        html += '</td>';
                        html += '<td style="padding: 8px; border-bottom: 1px solid #eee; text-align: center;">';
                        html += '<button type="button" class="btnDeleteTemplate" data-idx="' + item.IDX + '" style="padding: 3px 10px; cursor: pointer; color: #d9534f;">삭제</button>';
                        html += '</td>';
                        html += '</tr>';
                        
                        selectHtml += '<option value="' + item.IDX + '">' + item.NAME + '</option>';
                    }
                }
                
                $('#templateListBody').html(html);
                $('#mailTemplate').html(selectHtml);
            }
        },
        error: function(request, status, error) {
            console.log("ERROR: ", error);
        }
    });
}

// 템플릿 관리 팝업 열기
$('#btnManageTemplate').on('click', function() {
    loadMailTemplateList();
    $('#templateIdx').val('');
    $('#templateName').val('');
    $('#templateCon').val('');
    $('#templateManagePopup').show();
});

// 템플릿 관리 팝업 닫기
$('#btnCloseTemplateManage, #btnCancleTemplateManage').on('click', function() {
    $('#templateManagePopup').hide();
});

// 새 템플릿 버튼
$('#btnNewTemplate').on('click', function() {
    $('#templateIdx').val('');
    $('#templateName').val('');
    $('#templateCon').val('');
});

// 템플릿 선택 (목록에서)
$(document).on('click', '.btnSelectTemplate', function() {
    var idx = $(this).data('idx');
    $.ajax({
        type: "POST",
        url: "/mail/mailTemplate",
        data: { idx: idx },
        success: function(resultMap) {
            if(resultMap.resultCode == 0) {
                var data = resultMap.data;
                $('#templateIdx').val(data.IDX);
                $('#templateName').val(data.NAME);
                $('#templateCon').val(data.CON);
            }
        },
        error: function(request, status, error) {
            console.log("ERROR: ", error);
        }
    });
});

// 템플릿 삭제
$(document).on('click', '.btnDeleteTemplate', function() {
    var idx = $(this).data('idx');
    if(confirm('템플릿을 삭제하시겠습니까?')) {
        $.ajax({
            type: "POST",
            url: "/mail/deleteMailTemplate",
            data: { idx: idx },
            success: function(resultMap) {
                if(resultMap.resultCode == 0) {
                    alert(resultMap.resultMessage);
                    loadMailTemplateList();
                    $('#templateIdx').val('');
                    $('#templateName').val('');
                    $('#templateCon').val('');
                } else {
                    alert(resultMap.resultMessage);
                }
            },
            error: function(request, status, error) {
                alert('삭제에 실패하였습니다.');
            }
        });
    }
});

// 템플릿 저장 (등록/수정)
$('#btnSaveTemplate').on('click', function() {
    var idx = $('#templateIdx').val();
    var name = $('#templateName').val().trim();
    var con = $('#templateCon').val().trim();
    
    if(name == '') {
        alert('제목을 입력하세요.');
        return;
    }
    if(con == '') {
        alert('내용을 입력하세요.');
        return;
    }
    
    var url = idx ? '/mail/updateMailTemplate' : '/mail/insertMailTemplate';
    var data = { name: name, con: con };
    if(idx) data.idx = idx;
    
    $.ajax({
        type: "POST",
        url: url,
        data: data,
        success: function(resultMap) {
            if(resultMap.resultCode == 0) {
                alert(resultMap.resultMessage);
                loadMailTemplateList();
                $('#templateIdx').val('');
                $('#templateName').val('');
                $('#templateCon').val('');
            } else {
                alert(resultMap.resultMessage);
            }
        },
        error: function(request, status, error) {
            alert('저장에 실패하였습니다.');
        }
    });
});

// 템플릿 적용 버튼 (메일 발송 모달에서)
$('#mailTemplate').on('change', function() {
    var idx = $(this).val();
    if(!idx) {
        // 템플릿을 선택하세요 선택 시 제목/내용 비우기
        $('#mailTitle').val('');
        $('#mailContent').val('');
        return;
    }
    $.ajax({
        type: "POST",
        url: "/mail/mailTemplate",
        data: { idx: idx },
        success: function(resultMap) {
            if(resultMap.resultCode == 0) {
                var data = resultMap.data;
                $('#mailTitle').val(data.NAME);
                $('#mailContent').val(data.CON);
            }
        },
        error: function(request, status, error) {
            alert('템플릿 적용에 실패하였습니다.');
        }
    });
});

// ============== 템플릿 관리 기능 끝 ==============

// 수신자/참조자 삭제 기능
$(document).on('click', '.remove-mail-user', function(e) {
    e.preventDefault();
    var type = $(this).data('type');
    var userNo = String($(this).data('user-no'));

    // 해당 태그 제거
    $(this).parent('.mail-user-tag').remove();

    // hidden input에서 해당 유저 제거
    if(type === 'receiver') {
        var existingData = $('#mailReceiverData').val();
        var existingList = existingData ? existingData.split(',').filter(function(x) { return x !== '' && x !== userNo; }) : [];
        $('#mailReceiverData').val(existingList.join(','));
    } else if(type === 'cc') {
        var existingData = $('#mailCcData').val();
        var existingList = existingData ? existingData.split(',').filter(function(x) { return x !== '' && x !== userNo; }) : [];
        $('#mailCcData').val(existingList.join(','));
    }
});

$('#btnClose').on('click', function(){
    $("#mailTitle").val("");
    $("#mailContentHidden").val("");
    $("#mailContent").val("");
    $("#mailSenderData").val("");
    $("#mailSenderDisplay").text("선택된 발신자 없음");
    $("#mailReceiverData").val("");
    $("#mailReceiverList").html("");
    $("#mailCcData").val("");
    $("#mailCcList").html("");
    
    $("#sendMalDetail").hide();
    
});

$('#btnCancleSendMailDetail').on('click', function(){
    $("#mailTitle").val("");
    $("#mailContentHidden").val("");
    $("#mailContent").val("");
    $("#mailSenderData").val("");
    $("#mailSenderDisplay").text("선택된 발신자 없음");
    $("#mailReceiverData").val("");
    $("#mailReceiverList").html("");
    $("#mailCcData").val("");
    $("#mailCcList").html("");
    
    $("#sendMalDetail").hide();
    
});
/* 사용자 그룹 검색 시작 */
var to = true;
$('#userGroupBtn').on('click', function(){
    var v = $('#userGroupSearch').val();
	
	if(to) { clearTimeout(to); }
    to = setTimeout(function () {
      $('#add_group_tree').jstree(true).search(v);
    }, 250);
});

$('#userGroupSearch').keyup(function (e) {
	var v = $('#userGroupSearch').val();
	if (e.keyCode == 13) {
		$(".ui-autocomplete").hide();
    	if(to) { clearTimeout(to); }
        to = setTimeout(function () {
          $('#add_group_tree').jstree(true).search(v);
        }, 250);
    }
});
/* 사용자 그룹 검색 종료 */

function fn_search() {
	var host = $("#txt_host").val();
	
	if(host != null && host != ''){
		getSearchData(host);
	} else {
		$("#div_all").show()
		$("#div_search").hide()
	}
}

 

function fnCheckRemove(element) {
	var locationTR = $(element).parent("td").parent("tr")[0];
	var id = locationTR.id.substring(3);
	
	// remove 클릭시 jstree 체크박스 해제
	$('#group_tree').jstree("uncheck_node", id);
	$(locationTR).remove();
}
</script>
<style>
.ui-icon-pencil, .ui-icon-plusthick, .ui-icon-minusthick{
	cursor:pointer;
}
</style>
</body>
</html>