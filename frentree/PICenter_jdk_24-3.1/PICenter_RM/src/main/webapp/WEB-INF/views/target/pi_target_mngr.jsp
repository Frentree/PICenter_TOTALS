<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
 
<%@ include file="../../include/header.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/xlsx.full.min.js"></script>
<style>
.ui-state-hover td{
	background: #dadada !important;
}
.user_info th{
	width: 110px; 
}
.ui-jqgrid tr.ui-row-ltr td{
	cursor: pointer;
}
#pcAdminPopup .ui-jqgrid tr.ui-row-ltr td{
	cursor: default;
}
#left_datatype th, #left_datatype td {
	padding: 0;
}
.popup_tbl tbody td {
	height: 45px;
}
.selectBoxClick option:disabled {
	color: #D9D9D9;
}
.ui-widget-header{
	background: #f9f9f9;
	border: 0;
}
</style>
<section>
<div class="container minMenu">
	<h3 id= "headerText" style="display: inline; top: 25px;">대상조회 및 현행화</h3>
		<p id="commit" style="position: absolute; top: 32px; left: 228px; font-size: 13px; color: #9E9E9E;">관리 대상 서버의 상세 정보, 담당자 정보를 확인하고 필요 시 수정 할 수 있습니다.</p>
		<div class="content magin_t35">
			<div id="infratbl"></div>
					<div class="left_area2" style=" height: 100%; float: left; width: 314px;" >
						<table class="user_info narrowTable" style="width: 96%;">
							<tbody>
								<tr>
									<th style="text-align: center; border-radius: 0.25rem;">대상조회</th>
			             			     <td>
			                				 <input type="text" style="width: 205px; padding-left: 5px;" size="10" id="targetSearch" placeholder="호스트명을 입력하세요.">
			                			 </td>
			                		<td>
			                    		 <input type="button" name="button" class="btn_look_approval" id="btn_sch_target" style="margin-top: 5px;">
			                    	</td>
								</tr>
							</tbody>
						</table>
						<div class="left_box2" style="max-height: 92%; width: 100%; margin-top: 11px;">
		   					<div id="jstree" class="select_location" style="overflow-y: auto; overflow-x: auto; height: 100%; width:96%; background: #ffffff; border: 1px solid #c8ced3; white-space:nowrap;">
								
							</div>
						</div>
					</div>
					<div class="grid_top" style="float:right; width: 82%;"> 
						<div class="searchBox" style="float: left;">
							<table class="user_info narrowTable" id="navGridSearchDiv"  style="width: 310px;">
								<tbody>
									<tr id="searchTextBox">
										<th style="text-align: center; border-radius: 0.25rem;" class="searchName">
											<select id="searchFilter"></select>
										</th>
		             			     	<td id="defaultSearchTextBox">
		                					<input type="text" style="width: 205px; padding-left: 5px;" size="10"  class="searchContent" id="searchContent"  placeholder="검색어를 입력하세요.">	
		                			 	</td>
				                		<td> 
				                    		 <input type="button" name="button" class="navGridSearchBtn" style="margin-top: 5px;">
				                    	</td>
									</tr> 
								</tbody>
							</table>
						</div>
						<div id="searchFilterBox" class ="searchFilterBox" style="display:inline-block;width:849px;position:absolute;"></div> 
						<div class="list_sch" style="height: 39px; margin-top: 10px; float: right;" >
							<div class="sch_area" > 
								<div id="searchConditionsContainer" style="float: left;" ></div> 
								<div style="float: right; margin-left: 3px; margin-bottom: 7px;">
									<c:if test="${memberInfo.USER_GRADE == 9}">
										<button type="button" id="btnInsertTargetUser" class="btn_down">담당자 일괄 등록</button>
									</c:if>
									<button type="button" id="btnDownloadServerExel" class="btn_down">다운로드</button>
								</div>  
								<div style="float: right;">
									<h5 style="width: 24px; height: 24px; margin-top: 2px; background-size : 100%; background-image: url('${pageContext.request.contextPath}/resources/assets/images/pi_list.png');" id="tableShow">&nbsp;</h5>
								</div>
<!-- 								<div class="btn_grid_Search" style="float: left; margin: 6px 3px 0 0 ;">&nbsp;</div>  -->
							</div>
						</div> 
						<div class="left_box2" id="serverGridBox" style=" height: 677px; max-height: 677px; bottom: 17px; overflow-x: auto; ">
		   					<table id="serverGrid"></table>
		   				 	<div id="serverGridPager"></div>
		   				</div>
					</div>
					
				</div>
			</div>
			<!-- container -->
		</section>
		<!-- section -->

	<%@ include file="../../include/footer.jsp"%>
	
<!-- 팝업창 - 상세정보 시작 -->
<div id="targetPopup" class="popup_layer" style="display:none">
	<div class="popup_box" style="height: 331px; width: 505px; left: 54%; top: 61%; padding: 10px; background: #f9f9f9;">
	<img class="CancleImg" id="btnCancleTargetPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 style="color: #222; padding: 0; box-shadow: none;">상세정보</h1>
			<p style="position: absolute; top: 12px; left: 88px; font-size: 12px; color: #9E9E9E;">담당하고 있는 서버 정보가 맞는지 확인 후 수정하시기 바랍니다.</p>
		</div>
		<div class="popup_content">
			<div class="content-box" style="height: 246px; background: #fff; border: 1px solid #c8ced3;">
				<table class="popup_tbl">
					<colgroup>
						<col width="30%">
						<col width="70%">
					</colgroup>
					<tbody>
						<tr>
							<th>그룹명</th>
							<td><input type="text" id="targetGroup" value="" class="edt_sch" style="width: 285px; padding-left: 10px; background-color: rgba(210, 210, 210, 0.35);" readonly></td>
						</tr>
						<tr>
							<th>호스트명</th>
							<td>
								<input type="text" id="targetHost" value="" class="edt_sch" style="width: 285px; padding-left: 10px; background-color: rgba(210, 210, 210, 0.35);" readonly>
								<input type="hidden" id="targetHostId" value="" class="edt_sch" style="width: 285px; padding-left: 10px; background-color: rgba(210, 210, 210, 0.35);" readonly>
								<input type="hidden" id="targetHostApNo" value="" class="edt_sch" style="width: 285px; padding-left: 10px; background-color: rgba(210, 210, 210, 0.35);" readonly>
							</td>
						</tr>
						<tr>
							<th>서비스명</th>
							<td><input type="text" id="targetService" value="" class="edt_sch" style="width: 285px; padding-left: 10px; background-color: rgba(210, 210, 210, 0.35);" readonly></td>
						</tr>
						<tr>
							<th>IP</th>
							<td><input type="text" id="targetIP" value="" class="edt_sch" style="width: 285px; padding-left: 10px; background-color: rgba(210, 210, 210, 0.35);" readonly></td>
						</tr>
						<tr>
							<th>담당자</th>
							<td><input type="text" id="targetmngr" value="" class="edt_sch" style="width: 218px; padding-left: 10px;" readonly>
								<input type="hidden" id="targetmngrNm" value="" class="edt_sch" style="width: 218px; padding-left: 10px;" readonly>
								<input type="hidden" id="targetmngrId" value="" class="edt_sch" style="width: 218px; padding-left: 10px;" readonly>
								<button type="button" id="targetServiceManagerBtn" style="width: 63px; height: 27px;">조회</button></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area" style="padding: 10px 0; margin: 0;">
<!-- 				<button type="button" id="btntargetSave">저장</button> -->
				<button type="button" id="btntargetCancel">닫기</button>
			</div>
		</div>
	</div>
</div>

<div id="selectUserListPopup" class="popup_layer" style="display:none"> 
	<div class="popup_box" style="height: 290px; width: 885px; padding: 10px; background: #f9f9f9; left: 39%; top: 52%;">
	<img class="CancleImg" id="btnCancleServerMngrPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png" style="z-index: 999;">
		<div class="popup_top" style="background: #f9f9f9; display: block;">
			<h1 style="color: #222; padding: 0; box-shadow: none;">업무 담당자</h1>
		</div>
		<div class="popup_content" style="height: 240px;">
			<div class="content-box" id="div_mngr_list" style="height: auto; background: #fff; border: 1px solid #c8ced3;">
				<table id="mngrListGrid"></table>
				<div id="mngrListGridPager"></div>
			</div>
		</div>
		<div class="popup_btn" style="height: 45px;">
			<div id="acesssBtn" class="btn_area" style="padding: 10px 0; margin: 0;">
<!-- 				<button type="button" id="btnServerMngrListSave" style="margin-top: 10px;" >저장</button> -->
				<button type="button" id="btnServerMngrListClose" style="margin-top: 10px;" >닫기</button>
			</div>
		</div>
	</div>
</div>	

<div id="adminPopup" class="popup_layer" style="display:none">
	<div class="popup_box" style="height: 200px; width: 758px; padding: 10px; background: #f9f9f9; left: 46%; top: 53%">
	<img class="CancleImg" id="btnCancleadminPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png" style="z-index: 999;">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 style="color: #222; padding: 0; box-shadow: none;" id="serverMngrNm">담당자</h1>
		</div>
		<div class="popup_content">
			<div class="content-box" id="div_update_user" style="height: auto; background: #fff; border: 1px solid #c8ced3; padding: 5px 10px 10px 10px">
				<div style="right: 0px; padding-top: 0px; font-size: 14px; font-weight: bold;">
					소속 : <input type="text" id="searchGroup" value="" class="edt_sch" style="width: 125px; margin-bottom: 3px;">
					담당자 : <input type="text" id="searchUser" value="" class="edt_sch" style="width: 125px; margin-bottom: 3px;" >
					<button id="btnManagerSearch" class="btn_sch" style ="position: relative; margin-top:10px">검색</button>
				</div>
				<table id="targetUserGrid"></table>
				<div id="targetUserGridPager"></div>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area" style="padding: 10px 0; margin: 0;">
				<button type="button" id="btnTargetManagerInsert">추가</button>
				<button type="button" id="btnTargetManagerFix">적용</button>
				<button type="button" id="btnAdminClose">닫기</button>
			</div>
		</div>
	</div>
</div>

<div id="taskWindow" class="ui-widget-content" style="position:absolute; left: 10px; top: 10px; touch-action: none; width: 150px; z-index: 999; 
	border-top: 2px solid #222222; box-shadow: 2px 2px 5px #ddd; display:none">
	<ul>
		<li  class="status status-completed status-scheduled status-paused status-stopped status-failed">
			<button id="updateMngrBtn">조회 </button></li>
<!-- 		<li class="status status-completed status-scheduled status-scanning status-paused status-stopped"> -->
<!-- 			<button id="deleteMngrBtn">삭제</button></li> -->
	</ul>
</div>

<div id="tableCustomData" class="ui-widget-content" style="position:absolute; right: 9%; top: 148px; touch-action: none; width: 165px; z-index: 999; 
		border-top: 2px solid #222222; box-shadow: 2px 2px 5px #ddd; display:none">
	<table id="gridListTd">
	</table>
</div>

<div id="insertExcelPopup" class="popup_layer" style="display:none">
	<div class="popup_box" style="width: 725px; height: 540px; padding: 10px; background: #f9f9f9; left: 46%; top: 51%;">
		<img class="CancleImg" id="btnCancleExcelPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 style="color: #222; padding: 0; box-shadow: none;">담당자 일괄 등록</h1>
			<p style="position: absolute; top: 14px; left: 155px; font-size: 12px; color: #9E9E9E;">PICenter 에서 제공하는 형식이 아닌 다른 형식으로 업로드시 생성이 불가합니다.</p>
		</div>
		<div class="popup_content">
			<div class="content-box" style="background: #fff; width: 100%; height:457px; border: 1px solid #c8ced3; overflow: auto;">
				<table class="popup_tbl" style="width: 100%;">
					<colgroup>
						<col width="15%">
						<col width="85%">
					</colgroup>
					<tbody>
						<tr>
							<th>파일 다운로드</th>
							<td>
								<button type="button" id="btnExcelDown">다운로드</button>
							</td>
						</tr>
					</tbody>
				</table>
				<table class="popup_tbl2" style="width: 100%;">
					<colgroup>
						<col width="15%">
						<col width="85%">
					</colgroup>
					<tbody >
						<tr>
							<th>파일 업로드</th>
							<td>
								<button type="button" id="clickimportBtn">파일선택</button>
								<input type="file" id="importExcel" name="importExcel" style="width: 955px; padding-left: 10px; display: none; ">
								<input type="text" id="importExcelNm" style="width: 460px; font-size: 12px; padding: 0 0 0 5px; margin: 0 0 0 7px;" readonly="">
							</td>
						</tr>
					</tbody>
				</table>
				<div class="content-table" style="width: 100%; height: 340px; padding: 0;">
					<table class="popup_tbl" style="width: 100%; overflow: auto;">
						<tbody id="import_targetUserList_excel">
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area" style="padding: 10px 0; margin: 0;" id="insertExcelBtn">
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">

var mngrNameListJson = JSON.parse('${mngrNameListJson}');

var mngrNameList = mngrNameListJson.map(function(item) {
return 'DELETE_TYPE=' + item.DELETE_TYPE + 
      ', MGNR_FLAG=' + item.MGNR_FLAG + 
      ', IDX=' + item.IDX + 
      ', NAME=' + item.NAME;
});
var currentServerInfo = {
    target_id: null,
    ap_no: null,
    server_nm: null,
    rowid: null
};
	
var colModel = [];
var cachedColModel = null; // 캐시처리를 위한 colModel
GridName = "#serverGrid";
var firstLodeSetting = false;
var jstreeList = ${userGroupList} 
var isGridInitialized = false;



$(function() {

  $('#jstree').jstree({
  	// List of active plugins
  	"core" : {
  	    "animation" : 0,
  	    "check_callback" : true,
  		"themes" : { "stripes" : false },
  		"data" : jstreeList 
  	},
  	"types" : {
  		    "#" : {
  		      "max_children" : 1,
  		      "max_depth" : 4,
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
  	'plugins' : ["search"]
  })
  .bind('select_node.jstree', function(evt, data, x) {
   	if(!isGridInitialized) {
       fn_drawserverGrid();
       isGridInitialized = true;
  	}
  	var id = data.node.id;
  	var type = data.node.original.type;
  	var text = data.node.text;
  	var connected = data.node.original.connected;
  	var parents = data.node.parents;
  	var parent = data.node.parent;
  	var children = data.node.children;
  	var user_grade = "${memberInfo.USER_GRADE}";
  	var picenter = false;
  	var server = false;
  	var insa_group = false;
  	 
  	if(parent == "#" && data.node.data.type == 99){
  		alert("최상위 그룹은 선택이 불가합니다.");
  		return true; 
  	}else{
  		server = true; 
  	}
  	  
  	if(parents.length > 1){	
  		for (var i = 0; i < parents.length; i++) {
  			if(parents[i]=="Group"){
  				insa_group = true;
  			}else if(parents[i]=="picenter"){
  				picenter = true; 
  			}else {
  				server = true; 
  			}
  		}
  	}
  	
  	var postData = {};
  	if(picenter || id == "picenter"){  // 보안 관리자가 등록한 그룹의 경우
  		$("#infratbl").css("display", "table-row");
  		
  		var selectId = "";
  		if(data.node.id!='server') selectId = data.node.id;
  		
  		postData = {id : selectId, type : data.node.type ,flag:"pic"};
  	}else if(server || id == "server"){ // 연동 그룹의 경우
  		$("#infratbl").css("display", "table-row");
  		
  		var selectId = "";
  		if(data.node.id!='server'){
  			selectId = data.node.id
  		}
  					  
  		postData = {id : selectId, type : data.node.data.type ,flag:"user"};
  	} 
  	
  	// 데이터만 변경
  	$("#serverGrid").setGridParam({
  		url:"<%=request.getContextPath()%>/target/selectServerTargetUser", 
  		postData : postData, 
  		datatype:"json" 
  	}).trigger("reloadGrid");
  	
  }).bind("loaded.jstree", function(e, data) {
  	var nodes = $('#jstree').jstree(true).get_json('#', { 'flat': true });
  	
  	var textMap = new Map();
    nodes.forEach(function(node) {
    var key = node.text.trim().toLowerCase();
    if (!textMap.has(key)) {
        textMap.set(key, node.text);
    }
  });

 	// Map 객체의 값들로 배열을 만들고 정렬
    var selectColData = Array.from(textMap.values());
    selectColData.sort(); 
       
  	$("#targetSearch").autocomplete({
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
});

  			
var resetFomatter = null;

$(document).ready(function () {
  
  $(document).click(function(e){
  	$("#taskWindow").hide();
  	$("#tableCustomData").hide();
  });
  
  // 먼저 UI 표시
  setTimeout(function() {
  	if (!isGridInitialized) {
  		fn_drawserverGrid(); 
  		isGridInitialized = true;
  	}
  	fn_targetMngrGrid();
  	$("#infratbl").html(mngrListNm()); // 한 번만
  	
  	var grade = "${userGrade}";
  	var gradeList = ["0", "1", "2", "3", "7"];
  	
  	setServer();
  }, 100);
  
  $("#btnDownloadServerExel").click(function(){
  	downLoadServerExcel();
  });
  
//   $("#btntargetSave").click(function(){
// 	    $("#btnServerMngrListSave").click(function(){  	
// 	        var mngrList = [];
	        
// 	        for(var p = 0; p < mngrNameList.length ; p++){
// 	            if(!mngrNameList[p] || typeof mngrNameList[p] !== 'string') {
// 	                continue;
// 	            }
	            
// 	            var row = mngrNameList[p].split(', ');
// 	            var IDX = row[2] ? row[2].split('IDX=').join('') : '';
// 	            var NAME = row[3] ? row[3].split('NAME=').join('') : '';
	            
// 	            var userNoValue = $("#grid_server_no"+p).val();
	            
// 	            if(userNoValue && userNoValue.trim() !== "") {
// 	                mngrList.push({user_no: userNoValue, userFlag:"server"+IDX});
// 	            }
// 	        }
	        
// 	        // 저장된 서버 정보 사용 - 수정된 부분
// 	        if(!currentServerInfo.target_id || !currentServerInfo.ap_no || !currentServerInfo.server_nm) {
// 	            alert("서버를 먼저 선택해주세요.");
// 	            return;
// 	        }
	        
// 	        var massage = "사용자를 지정하시겠습니까?";
// 	        if(confirm(massage)){
// 	            var postData = {
// 	                mngrList : JSON.stringify(mngrList),
// 	                target_id : currentServerInfo.target_id,
// 	                ap_no : currentServerInfo.ap_no,
// 	                server_nm : currentServerInfo.server_nm,
// 	            };
	            
// 	            $.ajax({
// 	                type: "POST",
// 	                url: "/popup/updateTargetUser",
// 	                async : false,
// 	                data : postData,
// 	                success: function (result) {
// 	                    if(result.resultCode != 0){
// 	                        alert(result.resultMeassage);
// 	                    } else {
// 	                        alert("사용자 지정에 완료하였습니다");
// 	                    }
// 	                },
// 	                error: function (request, status, error) {
// 	                    console.log("ERROR : ", error);
// 	                }
// 	            });  
	            
// 	            $("#targetInfraID").val("");
// 	            $("#targetServiceUserID").val("");
// 	            $("#targetServiceManagerID").val("");
	            
// 	            $("#targetPopup").hide();
// 	            $("#selectUserListPopup").hide();
// 	            setServer();
// 	        }
// 	    });

  $("#btntargetCancel").click(function(){ 
  	$("#targetPopup").hide();
  });
  
  $("#btnCancleTargetPopup").click(function(){ 
  	$("#targetPopup").hide();
  });
  
  $("#targetServiceManagerBtn").click(function() {
  	selectUserListPop();
  });
  
  $("#btnInsertTargetUser").click(function(){
  	$("#insertExcelPopup").show();
  });
  
  $("#btnCancleExcelPopup").click(function(){
  	$("#importExcel").val("");
  	$("#importExcelNm").val("");
  	
  	var details = "";
  	$("#insertExcelBtn").html(details);
  	$("#import_targetUserList_excel").html(details);
  	$("#insertExcelPopup").hide();
  });
  
  $("#btnNewPopupExcelCencel").click(function(){
  	$("#importExcel").val("");
  	$("#importExcelNm").val("");
  	
  	var details = "";
  	$("#insertExcelBtn").html(details);
  	$("#import_targetUserList_excel").html(details);
  	$("#insertExcelPopup").hide();
  });
  
  $("#userHelpIcon").on("click", function(e) {
  	var id = "target_mngr";
  	var pop_url = "${getContextPath}/popup/helpDetail";
  	var winWidth = 1140;
  	var winHeight = 655;
  	var popupOption= "width="+winWidth+", height="+winHeight + ", left=0, top=0, scrollbars=no, resizable=no, location=no"; 	
  	var pop = window.open(pop_url ,id, popupOption);
  	
  	var newForm = document.createElement('form');
  	newForm.method='POST';
  	newForm.action=pop_url;
  	newForm.name='newForm';
  	newForm.target=id;
  	
  	var data = document.createElement('input');
  	data.setAttribute('type','hidden');
  	data.setAttribute('name','id');
  	data.setAttribute('id','id');
  	data.setAttribute('value',id);
  	
  	newForm.appendChild(data);
  	document.body.appendChild(newForm);
  	newForm.submit();
  	
  	document.body.removeChild(newForm);
  });
  
  $("#tableCustomData").click(function(e){
  	e.stopPropagation();
  });   
  
});

// colModel 구성 함수 분리
function buildColModel() {
  var model = [];
  
  model.push({ label:"그룹명",  		index: "GROUP_NM", 				name: "GROUP_NM", 				width: 100, align: "center", search: true, searchrules: { hidden: false } });
  model.push({ label:"TARGET_ID",  index: "TARGET_ID", 			name: "TARGET_ID", 				width: 0, 	align: "center", hidden:true});
  model.push({ label:"AP_NO",  	index: "AP_NO", 				name: "AP_NO", 					width: 0, 	align: "center", hidden:true});
  model.push({ label:"연결 서버",  	index: "RECON_AP", 				name: "RECON_AP", 				width: 150, align: "center", search: true, searchrules: { hidden: false } });
  model.push({ label:"업무 구분",  	index: "PLATFORM", 				name: "PLATFORM", 				width: 0, 	align: "center", hidden:true});
  model.push({ label:"호스트명",  	index: "NAME", 					name: "NAME", 					width: 150, align: "center", search: true, searchrules: { hidden: false } });
  model.push({ label:"서버 연결 상태", 	index: "AGENT_CONNECTED", 		name: "AGENT_CONNECTED", 		width: 100, align: "center", formatter: con_icon, search: true, searchrules: { hidden: false }, type:4 });
  model.push({ label:"서버 연결 IP",  	index: "AGENT_CONNECTED_IP", 	name: "AGENT_CONNECTED_IP", 	width: 100, align: "center", search: true, searchrules: { hidden: false } });
  model.push({ label:"서비스명",  	index: "SERVICE_NM",            name: "SERVICE_NM",             width: 200, align: "center", search: true, searchrules: { hidden: false } });
  
  for(var mp = 0; mp < mngrNameListJson.length ; mp++){
      var item = mngrNameListJson[mp];
      var MGNR_FLAG = item.MGNR_FLAG;
      var IDX = item.IDX;
      var NAME = item.NAME;
      
      model.push({ label:NAME+"소속", index: MGNR_FLAG+"_sosok", name: MGNR_FLAG+"_sosok", width: 100, align: "center", hidden:true});
      model.push({ 
          label:NAME, 
          index: MGNR_FLAG+"_nm", 
          name: MGNR_FLAG+"_nm", 
          width: 150, 
          align: "center", 
          search: true, 
          searchrules: { hidden: false },
          formatter: function(cellvalue, options, rowObject) {
              return formatManagerName(cellvalue, rowObject, options.colModel.name);
          }
      });
      model.push({ label:NAME+"사번", index: MGNR_FLAG, name: MGNR_FLAG, width: 100, align: "center", hidden:true});
      model.push({ label:NAME+"개수", index: MGNR_FLAG+"_count", name: MGNR_FLAG+"_count", width: 50, align: "center", hidden:true});
  }
  
  return model;
}

function formatManagerName(cellvalue, rowObject, columnName) {
    var mngrFlag = columnName.replace('_nm', '');
    var countColumn = mngrFlag + '_count';
    
    var count = parseInt(rowObject[countColumn]) || 0;
    
    if (count === 0 || !cellvalue || cellvalue.trim() === '') {
        return '-';
    } else if (count === 1) {
        return cellvalue;
    } else {
        return cellvalue + ' 외 ' + (count - 1) + '명';
    }
}

function fn_drawserverGrid() {
      
  // colModel 캐싱 적용
  if (!cachedColModel) {
  	cachedColModel = buildColModel();
  }
  colModel = cachedColModel;
  
  var gridWidth = $("#serverGrid").parent().width();
  
  $("#serverGrid").jqGrid({
  	url: "<%=request.getContextPath()%>/target/selectServerTargetUser",
  	datatype: "local",
  	data: [], 
     	mtype : "POST",
      colModel : colModel,
      loadonce : true,
     	autowidth: true,
      viewrecords: true,
      autowidth: true,
      height: 589,
      shrinkToFit: false,
  	rowNum:25,
  	rowList:[25,50,100],
  	pager: "#serverGridPager",
  	rownumbers : false,
  	rownumWidth : 35,
    	onSelectRow : function(rowid,celname,value,iRow,iCol) {	
    	},
    	onCellSelect : function(rowid){
    	    $("#rowid").val(rowid);
    	    
    	    currentServerInfo.target_id = $(this).getCell(rowid, 'TARGET_ID');
    	    currentServerInfo.ap_no = $(this).getCell(rowid, 'AP_NO');
    	    currentServerInfo.server_nm = $(this).getCell(rowid, 'NAME');
    	    currentServerInfo.rowid = rowid;
    	    
    	    var GROUP = $(this).getCell(rowid, 'GROUP_NM');
    	    var HOST = $(this).getCell(rowid, 'NAME');
    	    var IP = $(this).getCell(rowid, 'AGENT_CONNECTED_IP');
    	    var SERVICE = $(this).getCell(rowid, 'SERVICE_NM');
    	    var TARGET_ID = $(this).getCell(rowid, 'TARGET_ID');
    	    var AP_NO = $(this).getCell(rowid, 'AP_NO');
    	    var mngr_cnt = 0;
    	    
    	    var server_user = null;
    	    var server_user_nm = "";
    	    
    	    for(var mn = 0; mn < mngrNameList.length ; mn++){
    	        if(!mngrNameList[mn] || typeof mngrNameList[mn] !== 'string') {
    	            continue;
    	        }
    	        
    	        var row = mngrNameList[mn].split(', ');
    	        var MGNR_FLAG = row[1].split('MGNR_FLAG=').join('');
    	        var IDX = row[2].split('IDX=').join('');
    	        var NAME = row[3].split('NAME=').join('');
    	        
    	        var mngr_user = $(this).getCell(rowid, MGNR_FLAG);
    	        var mngr_user_count = parseInt($(this).getCell(rowid, MGNR_FLAG+"_count")) || 0;
    	        
    	        // 원본 이름 데이터 가져오기 (formatter 적용 전)
    	        var mngr_user_nm_original = $(this).getCell(rowid, MGNR_FLAG+"_nm");
    	        
    	        // formatter가 적용되었는지 확인하고 원본 이름 추출
    	        if(mngr_user_nm_original && mngr_user_nm_original.includes(' 외 ')) {
    	            // "일반 사용자 외 2명" -> "일반 사용자" 추출
    	            mngr_user_nm_original = mngr_user_nm_original.split(' 외 ')[0];
    	        }

    	        if(mngr_user != null && mngr_user != " " && mngr_user != ""){
    	            mngr_cnt += mngr_user_count;
    	        }
    	        
    	        if(server_user == null || server_user == " "){
    	            if(mngr_user != null && mngr_user != " " && mngr_user != ""){
    	                server_user = mngr_user;
    	                server_user_nm = mngr_user_nm_original; // 원본 이름 사용
    	            }
    	        }
    	        
    	        $("#grid_server_no"+mn).val(mngr_user);
    	        $("#grid_server_nm"+mn).val(mngr_user_nm_original); // 원본 이름 저장
    	    }

    	    $("#targetGroup").val(GROUP);
    	    $("#targetHost").val(HOST);
    	    $("#targetHostId").val(TARGET_ID);
    	    $("#targetHostApNo").val(AP_NO);
    	    $("#targetIP").val(IP);
    	    $("#targetService").val(SERVICE);

    	    // 총 개수로 새로 포맷팅
    	    if(mngr_cnt > 1 && server_user_nm && server_user_nm.trim() !== ""){
    	        server_user_nm += " 외 " + (mngr_cnt-1) + "명";
    	    }
    	    $("#targetmngr").val(server_user_nm);
    	    $("#targetmngrNm").val(server_user_nm);
    	    $("#targetmngrId").val(server_user);
    	    
    	    $("#targetPopup").show();
    	},
    	afterSaveCell : function(rowid,name,val,iRow,ICol){
        },
    	afterSaveRow : function(rowid,name,val,iRow,ICol){
        },
  	    loadComplete: function(data) {
        },
        gridComplete : function() {
        }
    }).navGrid("#serverGridPager", 
  		{ edit: false, add: false, del: false, search: true }
    ).filterToolbar({ searchOperators: true, searchOnEnter: false, defaultSearch: "cn" });
   
    // 그리드 생성 이후 호출
    setTimeout(function() {
  	GridSearchTypeChk();
  	searchListAppend();
  	
  	setTimeout(function() {
  		if ($("#searchFilter").length > 0 && $("#searchFilter").val()) {
  			automaticCompletion(1);
  		}
  	}, 100);
  }, 50);
}

function selectUserListPop(){
	  
  $("#mngrListGrid").jqGrid('clearGridData');
  var serverGridData = $("#serverGrid").getGridParam("selrow");
  
  var mydata = [];

  for(var nl = 0; nl < mngrNameList.length ; nl++){
    // 안전성 체크 추가
    if(!mngrNameList[nl] || typeof mngrNameList[nl] !== 'string') {
        continue;
    }
    
    var row = mngrNameList[nl].split(', ');

    var DELETE_TYPE = row[0].split('DELETE_TYPE=').join('');
    var MGNR_FLAG = row[1].split('MGNR_FLAG=').join('');
    var IDX = row[2].split('IDX=').join('');
    var NAME = row[3].split('NAME=').join('');
    
    var user_nm = $("#serverGrid").getCell(serverGridData, MGNR_FLAG+"_nm");
    var user_no = $("#serverGrid").getCell(serverGridData, MGNR_FLAG);

     
    mydata.push({
      MNGR_STATUS_NM: NAME, 
      MNGR_STATUS: MGNR_FLAG, 
      DELETE_TYPE: DELETE_TYPE, 
      USER_NO: user_no, 
      USER_SOSOK: $("#serverGrid").getCell(serverGridData, MGNR_FLAG+"_sosok"), 
      USER_NAME: user_nm
    });
  }
   
  $("#grid_server_no"+nl).val(user_no);
  $("#grid_server_nm"+nl).val(user_nm);

  // 백엔드로 데이터 전송
  var targetId = $("#serverGrid").getCell(serverGridData, 'TARGET_ID'); // TARGET_ID 가져오기
  var apNo = $("#serverGrid").getCell(serverGridData, 'AP_NO'); // AP_NO 가져오기
  
  var requestData = {
    targetId: targetId,
    apNo: apNo,
    data: mydata
  };

  $.ajax({
    type: "POST",
    url: "<%=request.getContextPath()%>/popup/getUserDetailList",
    contentType: "application/json",
    data: JSON.stringify(requestData),
    success: function(response) {
    	  var groupedData = response[0].groupedData;

    	  var gridData = [];
    	  
    	  for(var nl = 0; nl < mngrNameList.length; nl++){
    	    if(!mngrNameList[nl] || typeof mngrNameList[nl] !== 'string') {
    	      continue;
    	    }
    	    
    	    var row = mngrNameList[nl].split(', ');
    	    var DELETE_TYPE = row[0].split('DELETE_TYPE=').join('');
    	    var MGNR_FLAG = row[1].split('MGNR_FLAG=').join('');
    	    var IDX = row[2].split('IDX=').join('');
    	    var NAME = row[3].split('NAME=').join('');
    	    
    	    
    	    var users = groupedData[MGNR_FLAG] || [];
     	    
    	    if(users.length > 0) {
    	    	  var userNames = [];
    	    	  var userNos = [];
    	    	  var userSosoks = [];
    	    	  
    	    	  for(var i = 0; i < users.length; i++) {
    	    	    if(userNames.indexOf(users[i].USER_NAME) === -1) {
    	    	      userNames.push(users[i].USER_NAME);
    	    	      userNos.push(users[i].USER_NO);
    	    	      userSosoks.push(users[i].TEAM_NAME);
    	    	    }
    	    	  }
    	    	  
    	    	  // 이름 표시 로직 수정: "첫번째이름 외 N명" 형태로 변경
    	    	  var displayName = userNames[0];
    	    	  if(userNames.length > 1) {
    	    	    displayName += " 외 " + (userNames.length - 1) + "명";
    	    	  }
    	    	  
    	    	  // 소속 표시 로직도 동일하게 수정
    	    	  var displaySosok = userSosoks[0];

    	    	  if(displaySosok == undefined || displaySosok == null || displaySosok == "" || displaySosok.trim() == "") {
    	    	      displaySosok = " ";
    	    	  }
    	    	  
    	    	  if(userSosoks.length > 1) {
    	    	    displaySosok += " 외 " + (userSosoks.length - 1) + "팀";
    	    	  }
    	    	  
    	    	  gridData.push({
    	    	    MNGR_STATUS_NM: NAME,
    	    	    MNGR_STATUS: MGNR_FLAG,
    	    	    DELETE_TYPE: DELETE_TYPE,
    	    	    USER_NO: userNos.join(','),
    	    	    USER_SOSOK: displaySosok,  
    	    	    USER_NAME: displayName    
    	    	  });
    	    	}
    	    else {
    	      gridData.push({
    	        MNGR_STATUS_NM: NAME,
    	        MNGR_STATUS: MGNR_FLAG,
    	        DELETE_TYPE: DELETE_TYPE,
    	        USER_NO: '',
    	        USER_SOSOK: '',
    	        USER_NAME: '지정된 담당자가 없습니다.'
    	      });
    	    }
    	  }
    	  
    	  for (var i = 0; i < gridData.length; i++) {
    	    $("#mngrListGrid").jqGrid('addRowData', i + 1, gridData[i]);
    	  }

    	  $("#selectUserListPopup").show();
    	},
    error: function(xhr, status, error) {
      console.error("에러 발생:", error);
      
      for (var i = 0; i < mydata.length; i++) {
        $("#mngrListGrid").jqGrid('addRowData', i + 1, mydata[i]);
      }
      
      $("#selectUserListPopup").show();
    }
  });
};

function userListWindows(info){
  var pop_url = "${getContextPath}/popup/userList";
  var id = "targetList"
  var winWidth = 730;
  var winHeight = 600;
  var popupOption = "width=" + winWidth + ",height=" + winHeight + ",left=0,top=0,scrollbars=no,resizable=no,location=no";
  var pop = window.open(pop_url,id,popupOption);
  
  var newForm = document.createElement('form');
  newForm.method='POST';
  newForm.action=pop_url;
  newForm.name='newForm';
  newForm.target=id;
  
  var data = document.createElement('input');
  data.setAttribute('type','hidden');
  data.setAttribute('name','info');
  data.setAttribute('value',info);
  
  newForm.appendChild(data);
  document.body.appendChild(newForm);
  newForm.submit();
  
  document.body.removeChild(newForm);
}

function setServer(){
  var postData = {id :""};
  $("#serverGrid").setGridParam({
  	url:"<%=request.getContextPath()%>/target/selectServerTargetUser", 
  	page: 1 , 
  	postData : postData, 
  	datatype:"json" 
  }).trigger("reloadGrid");
}

function gridreload(rowid){
  
  var newData = {
  		USER_NO  : $("#reachTargetMngr"+(Number(rowid)+2)+"No").val(),
  		USER_NAME : $("#reachTargetMngr"+(Number(rowid)+2)+"Nm").val(),
  		USER_SOSOK : $("#reachTargetMngr"+(Number(rowid)+2)+"Tm").val()
  };
  
  $("#mngrListGrid").jqGrid('setRowData',rowid,newData);
  $("#mngrListGrid tr#" + rowid + " td:eq(" + 1 + ")").removeAttr('style').css({ 'text-align': 'center'});
  $("#mngrListGrid tr#" + rowid + " td:eq(" + 2 + ")").removeAttr('style').removeAttr('colspan').css({ 'text-align': 'center'});
  $("#mngrListGrid tr#" + rowid + " td:eq(" + 3 + ")").removeAttr('style').css({ 'text-align': 'center'});
}

function test(){
  
 $("#serverGrid").jqGrid('searchGrid', {
     multipleSearch: true,  
     multipleGroup: false, 
     showQuery: false,
     searchOnEnter: true, 
     recreateFilter: true,
     afterShowSearch: function($form) {  
         $form.closest(".ui-jqdialog").find("table.ui-search-table").removeClass("ui-search-table");
     }
  });

  $("#serverGrid").jqGrid('setGridParam', { datatype: 'json' }).trigger('reloadGrid');
}

function fn_targetMngrGrid(){
  
  var gridWidth = $("#mngrListGrid").parent().width();
  $("#mngrListGrid").jqGrid({
  	url: "<%=request.getContextPath()%>/target/selectMngrList",
  	datatype: "local",
  	postData : {},
     	mtype : "POST",
  	colNames:['구분','구분(db)','사번','소속','성명','', ''], 
  	colModel: [
  		{ index: 'MNGR_STATUS_NM', name: 'MNGR_STATUS_NM', 	width: 140, align: "center", },
  		{ index: 'MNGR_STATUS', name: 'MNGR_STATUS', 	width: 140, align: "center", hidden:true},
  		{ index: 'USER_NO', 	name: 'USER_NO', 		width: 250, align: "center", cellattr: team_style2},
  		{ index: 'USER_SOSOK', 	name: 'USER_SOSOK', 	width: 250, align: "center", formatter: team_name, cellattr: team_style},
  		{ index: 'USER_NAME', 	name: 'USER_NAME', 		width: 250, align: "center", cellattr: team_style2},
  		{ index: 'DELETE_TYPE', name: 'DELETE_TYPE', 	width: 250, align: "center", hidden:true},
  		{ index: 'BUTTON', 		name: 'BUTTON', 		width: 100, align: "center", formatter:createView},
  	],
  	loadonce:true,
     	autowidth: false,
  	shrinkToFit: false,
  	viewrecords: true,
  	width: gridWidth,
  	height: 343,
  	rownumbers : false,
  	rownumWidth : 35,
  	rowNum:20,
  	rowList:[25,50,100],
  	pager: "#mngrListGridPager",
    	onSelectRow : function(rowid,celname,value,iRow,iCol) {	
    	},
    	onCellSelect : function(rowid){
    	},
    	afterSaveCell : function(rowid,name,val,iRow,ICol){
      },
    	afterSaveRow : function(rowid,name,val,iRow,ICol){
      },
  	loadComplete: function(data) {
      },
      gridComplete : function() {
      }
  });
}

$(document).on("click", ".gridSubSelBtn", function(e) {
    var rowid = $("#mngrListGrid").jqGrid("getGridParam","selrow");
    $("#serverMngrNm").html($("#mngrListGrid").getCell(rowid, "MNGR_STATUS_NM"));
    
    if(!currentServerInfo.target_id || !currentServerInfo.ap_no) {
        alert("서버를 먼저 선택해주세요.");
        return;
    }

    $("#searchGroup").val("");
    $("#searchUser").val("");
    
    fn_targetUserGrid(
        currentServerInfo.target_id,
        currentServerInfo.ap_no,
        $("#mngrListGrid").getCell(rowid, "MNGR_STATUS")
    );
    
    $("#adminPopup").show();
});

function downLoadServerExcel(){
  resetFomatter = "downloadClick";
  
  $("#serverGrid").jqGrid("hideCol",["CHK"]);

  var today = new Date();
  var dd = today.getDate();
  var mm = today.getMonth()+1;
  var yyyy = today.getFullYear();
  if(dd<10) {
      dd='0'+dd
  } 

  if(mm<10) {
      mm='0'+mm
  } 

  today = yyyy + "" + mm + dd;
  
  $("#serverGrid").jqGrid("exportToCsv",{
      separator: ",",
      separatorReplace : "",
      quote : '"', 
      escquote : '"', 
      newLine : "\r\n",
      replaceNewLine : " ",
      includeCaption : true,
      includeLabels : true,
      includeGroupHeader : true,
      includeFooter: true,
      fileName : "대상_조회_" + today + ".csv",
      mimetype : "text/csv; charset=utf-8",
      returnAsString : false
  });
  $("#serverGrid").jqGrid("showCol",["CHK"]);
} 

$("#updateMngrBtn").on("click", function() {
  var rowid = $("#mngrListGrid").jqGrid("getGridParam","selrow");
  $("#serverMngrNm").html($("#mngrListGrid").getCell(rowid, "MNGR_STATUS_NM"));
  $("#adminPopup").show();
  
  var serverRowid = $("#serverGrid").jqGrid("getGridParam","selrow");
  
  fn_targetUserGrid( 
  		$("#serverGrid").getCell(serverRowid, "TARGET_ID"), 
  		$("#serverGrid").getCell(serverRowid, "AP_NO"),
  		$("#mngrListGrid").getCell(rowid, "MNGR_STATUS")
  ); 
});

$("#btnTargetManagerInsert").on("click", function() {
	var rowid = $("#mngrListGrid").jqGrid("getGridParam","selrow");
    userListWindows('mngr_'+(Number(rowid) - 1));
});


$("#btnCancleadminPopup").on("click", function() {
  $("#adminPopup").hide();
});
$("#btnAdminClose").on("click", function() {
  $("#adminPopup").hide();
});

$("#deleteMngrBtn").on("click", function() {
  
  var rowid = $("#mngrListGrid").jqGrid("getGridParam","selrow");
  
  var result = confirm($("#mngrListGrid").getCell(rowid, 'MNGR_STATUS_NM') + "담당자를 제외하시겠습니까?");
  
  if(result){
  	
  	var mngrNo = $("#grid_server_nm"+(Number(rowid) - 1)).val();
  	
  	if(mngrNo == null || mngrNo == ""){
  		alert("지정된 담당자가 없습니다.");
  		return;
  	}
  	
  	$("#grid_server_nm"+(Number(rowid)- 1)).val('');
  	$("#grid_server_no"+(Number(rowid)- 1)).val('');
  	
  	var newData = {
  			USER_NO  :'',
  			USER_NAME : '지정된 담당자가 없습니다.',
  			USER_SOSOK : ''
  	};

  	$("#mngrListGrid").jqGrid('setRowData',rowid,newData);
  	$("#mngrListGrid tr#" + rowid + " td:eq(" + 1 + ")").css({ 'display': 'none'});
  	$("#mngrListGrid tr#" + rowid + " td:eq(" + 3 + ")").attr('colspan', '3');
  	$("#mngrListGrid tr#" + rowid + " td:eq(" + 2 + ")").css({ 'display': 'none'});
  	
  	$("#taskWindow").hide();
  } 
});

var aut = "manager"
var to = true;
$('#btn_sch_target').on('click', function(){

  var v = $('#targetSearch').val();
  
  if(to) { clearTimeout(to); }
  to = setTimeout(function () {
    $('#jstree').jstree(true).search(v);
  }, 250);
});

$('#targetSearch').keyup(function (e) {
  var v = $('#targetSearch').val();
  if (e.keyCode == 13) {
  	$(".ui-autocomplete").hide();
  	if(to) { clearTimeout(to); }
      to = setTimeout(function () {
        $('#jstree').jstree(true).search(v);
      }, 250);
  }
});

$("#btnServerMngrListClose").click(function(e){
  var mngr_con = 0;
  var result = null;

  $("#selectUserListPopup").hide();
});

$("#btnCancleServerMngrPopup").click(function(e){
  $("#selectUserListPopup").hide();
});




function fn_targetUserGrid(target_id, ap_no, mnger_status) {
   
    var gridWidth = $("#targetUserGrid").parent().width();
    
    $("#targetUserGrid").jqGrid({
        url: "<%=request.getContextPath()%>/target/selectTargetUserList",
        datatype: "local",
        postData : {target_id : target_id, ap_no : ap_no, mnger_status:mnger_status},
        mtype : "POST",
        colNames:['사번','','성명', '직급', '팀/부서', '이메일'],
        colModel: [
            { index: 'user_no', name: 'user_no', width: 80, align: "center", search: true, searchrules: { hidden: false }},
            { index : 'CHK', name : 'CHK', width : 30, align : 'center', hidden : true},
            { index: 'user_name', name: 'user_name', width: 80, align: "center", search: true, searchrules: { hidden: false }},
            { index: 'jikwee', name: 'jikwee', width: 80, align: "center", search: true, searchrules: { hidden: false }},
            { index: 'team_name', name: 'team_name', width: 100, align: "center", search: true, searchrules: { hidden: false }},
            { index: 'user_email', name: 'user_email', width: 120, align: "left", search: true, searchrules: { hidden: false }}
        ],
        loadonce:true,
        autowidth: false,
        shrinkToFit: false,
        viewrecords: true,
        width: gridWidth,
        height: 343,
        rownumbers : false,
        rownumWidth : 30,
        rowNum:20,
        rowList:[25,50,100],
        multiselect : true,
        pager: "#targetUserGridPager",
        onSelectRow : function(rowid,celname,value,iRow,iCol) {
        },
        onCellSelect : function(rowid){
        },
        afterSaveCell : function(rowid,name,val,iRow,ICol){
        },
        afterSaveRow : function(rowid,name,val,iRow,ICol){
        },
        loadComplete: function(data) {
            var allRowIds = $("#targetUserGrid").jqGrid('getDataIDs');
            for (var i = 0; i < allRowIds.length; i++) {
                $("#targetUserGrid").jqGrid('setSelection', allRowIds[i], false);
            }
        },
        gridComplete : function() {
        }
    })

    var postData = {target_id : target_id, ap_no : ap_no, mnger_status:mnger_status};
    $("#targetUserGrid").setGridParam({
        url: "<%=request.getContextPath()%>/target/selectTargetUserList",
        postData : postData, 
        datatype:"json"  
    }).trigger("reloadGrid");
};

function fnManagerSearch() {
    var searchGroup = $("#searchGroup").val();
    var searchUser = $("#searchUser").val();

    // 검색어가 모두 비어있으면 모든 행을 표시
    if(!searchGroup.trim() && !searchUser.trim()) {
        $("#targetUserGrid").find("tr.jqgrow").show();
        return;
    }

    $("#targetUserGrid").find("tr.jqgrow").each(function() {
        var rowId = $(this).attr('id');
        var teamName = $("#targetUserGrid").getCell(rowId, 'team_name').toLowerCase();
        var userName = $("#targetUserGrid").getCell(rowId, 'user_name').toLowerCase();

        var showRow = true;
        if(searchGroup && teamName.indexOf(searchGroup.toLowerCase()) == -1) {
            showRow = false;
        }
        if(searchUser && userName.indexOf(searchUser.toLowerCase()) == -1) {
            showRow = false;
        }

        if(showRow) {
            $(this).show();
        } else {
            $(this).hide();
        }
    });
}

$("#searchGroup").keypress(function(event) {
    if(event.keyCode == 13) {
        fnManagerSearch();
    }
});

$("#searchUser").keypress(function(event) {
    if(event.keyCode == 13) {
        fnManagerSearch();
    }
});

$("#btnManagerSearch").click(function() {
    fnManagerSearch();
});



function con_icon(cellvalue, options, rowObject) {
  
  if(resetFomatter == "downloadClick"){
  	if(cellvalue == "1"){
  		return 'Connected';
  	}else{
  		return 'Not Connected';
  	}
  }else{
  	if(cellvalue == "1"){
  		return '<img style="width:15%; padding-top: 5px;" src="${pageContext.request.contextPath}/resources/assets/images/icon_con.png">';
  	}else{
  		return '<img style="width:15%; padding-top: 5px;" src="${pageContext.request.contextPath}/resources/assets/images/icon_dicon.png">';
  	}
  }
}

function team_name(cellvalue, options, rowObject) {
  var user_no = rowObject.USER_NO;
  
  if(user_no != "" && user_no != null){
  	return cellvalue;
  }else{
  	return "지정된 담당자가 없습니다.";
  }
}

function team_style(rowId, cellvalue, rowObject, cm, rdata) {
  var user_no = rowObject.USER_NO;
  
  if(user_no != "" && user_no != null){
  }else{
  	return "colspan=3;"
  }
}

function team_style2(rowId, cellvalue, rowObject, cm, rdata) {
  var user_no = rowObject.USER_NO;
  
  if(user_no != "" && user_no != null){
  }else{
  	return "style=display:none;";
  }
}

var createView = function(cellvalue, options, rowObject) {
  var  result = "";
  if(rowObject.DELETE_TYPE == 'N'){
  	result = "";
  }else{
  	result = "<button type='button' style='padding-top: 4px; padding-bottom:4px;' class='gridSubSelBtn' name='gridSubSelBtn'>선택</button>";
  }
  
  return result; 
};

function mngr_nm(cellvalue, options, rowObject) {
  var mngr3 = rowObject.SERVICE_MNGR3_NM;
  var mngr4 = rowObject.SERVICE_MNGR4_NM;
  var mngr5 = rowObject.SERVICE_MNGR5_NM;
  
  var mngr_con = 0;
  var result = "";
  
  if(mngr3 != null) ++mngr_con;
  if(mngr4 != null) ++mngr_con;
  if(mngr5 != null) ++mngr_con;
  
  if(result == "" && mngr3 != null) result = mngr3;
  if(result == "" && mngr4 != null) result = mngr4;
  if(result == "" && mngr5 != null) result = mngr5;
  
  if(mngr_con > 1){
  	result += " 외 "+ (mngr_con-1) +" 명";
  }
  
  return result;
}

function mngrListNm (){
  var html = "";
  if(mngrNameListJson.length > 4){
      var mngrCnt = 0;
      for(var p = 0; p < mngrNameListJson.length ; p++){
          html += "<input type=\"hidden\" style=\"width: 0px;\" class=\"mngrListClass\" id=\"grid_server_nm"+p+"\" >";
          html += "<input type=\"hidden\" style=\"width: 0px;\" class=\"mngrListClass\" id=\"grid_server_no"+p+"\" >";
      }
  }else{
      for(var p = 0; p < mngrNameListJson.length ; p++){
          var item = mngrNameListJson[p];
          var IDX = item.IDX;
          var NAME = item.NAME;
          html += "<input type=\"hidden\" style=\"width: 186px; padding-left: 5px;\" class=\"mngrListClass\" size=\"10\" id=\"grid_server_nm"+p+"\">";
          html += "<input type=\"hidden\" style=\"width: 186px; padding-left: 5px;\" class=\"mngrListClass\" size=\"10\" id=\"grid_server_no"+p+"\"></td>";
      }
  }
  return html;
}

$("#clickimportBtn").click(function(){
  $("#importExcel").click();
});
$("#btnExcelDown").click(function(){
	const header = ["호스트명"];
	mngrNameListJson.map(function(item) {
		header.push(item.NAME.replace(/\s+/g, '')); 
	});
	
	const ws = XLSX.utils.aoa_to_sheet([header]);
	const wb = XLSX.utils.book_new();
	XLSX.utils.book_append_sheet(wb, ws, "담당자 등록");
	
	XLSX.writeFile(wb, "PIC_담당자_일괄등록.xlsx");
});

$("#btnTargetManagerFix").click(function(){
    var checkedRows = [];
    var uniqueRows = new Set();
    
    $("#targetUserGrid").find("tr.ui-state-highlight").each(function() {
        var rowId = $(this).attr('id');
        if(rowId && !uniqueRows.has(rowId)) {
            checkedRows.push(rowId);
            uniqueRows.add(rowId);
        }
    });

    var mngrRowId = $("#mngrListGrid").jqGrid("getGridParam","selrow");
    var mngrStatus = $("#mngrListGrid").getCell(mngrRowId, "MNGR_STATUS");

    // 담당자 구분의 IDX 찾기
    var targetIdx = '';
    for(var i = 0; i < mngrNameList.length; i++) {
        if(!mngrNameList[i] || typeof mngrNameList[i] !== 'string') {
            continue;
        }
        
        var row = mngrNameList[i].split(', ');
        var MGNR_FLAG = row[1].split('MGNR_FLAG=').join('');
        var IDX = row[2].split('IDX=').join('');
        
        if(MGNR_FLAG === mngrStatus) {
            targetIdx = IDX;
            break;
        }
    }

    // 선택된 사용자 정보를 객체로 수집
    var userInfos = [];
    
    checkedRows.forEach(function(rowId) {
        var user_no = $("#targetUserGrid").getCell(rowId, 'user_no');
        var user_name = $("#targetUserGrid").getCell(rowId, 'user_name');
        var team_name = $("#targetUserGrid").getCell(rowId, 'team_name');
        
        if(user_no && user_name) {
            userInfos.push({
                user_no: user_no,
                user_name: user_name,
                team_name: team_name || ''
            });
        }
    });

    // user_no 기준으로 중복 제거
    var uniqueUserInfos = [];
    var seenUserNos = new Set();
    
    userInfos.forEach(function(userInfo) {
        if (!seenUserNos.has(userInfo.user_no)) {
            seenUserNos.add(userInfo.user_no);
            uniqueUserInfos.push(userInfo);
        }
    });
    

    var userNames = uniqueUserInfos.map(function(info) { return info.user_name; });
    var userNos = uniqueUserInfos.map(function(info) { return info.user_no; });
    var teamNames = uniqueUserInfos.map(function(info) { return info.team_name; }).filter(function(name) { return name && name.trim() !== ''; });
    
 
    var displayName = '';
    if(userNames.length > 0) {
        displayName = userNames[0];
        if(userNames.length > 1) {
            displayName += " 외 " + (userNames.length - 1) + "명";
        }
    }
    
    var uniqueTeamNames = [...new Set(teamNames)];
    var displayTeamName = '';
    if(uniqueTeamNames.length > 0) {
        displayTeamName = uniqueTeamNames[0];
        if(uniqueTeamNames.length > 1) {
            displayTeamName += " 외 " + (uniqueTeamNames.length - 1) + "개";
        }
    }
    
    var displayUserNo = userNos.join(',');

    var mngrList = [];
    if(displayUserNo && displayUserNo.trim() !== "" && targetIdx) {
        mngrList.push({
            user_no: displayUserNo, 
            userFlag: "server" + targetIdx
        });
    }
    
    var confirmMessage = displayUserNo ? 
        "'" + displayName + "' 담당자를 저장하시겠습니까?" : 
        "담당자 지정을 해제하시겠습니까?";
    
    if(!confirm(confirmMessage)) {
        return;
    }

    // 기존 데이터 백업
    var originalData = {
        userNo: currentServerInfo.rowid ? $("#serverGrid").getCell(currentServerInfo.rowid, mngrStatus) : '',
        userName: currentServerInfo.rowid ? $("#serverGrid").getCell(currentServerInfo.rowid, mngrStatus + "_nm") : '',
        userSosok: currentServerInfo.rowid ? $("#serverGrid").getCell(currentServerInfo.rowid, mngrStatus + "_sosok") : ''
    };
    
    var setData = {
        USER_NO: displayUserNo || '',
        USER_NAME: displayName || '지정된 담당자가 없습니다.',
        USER_SOSOK: displayTeamName || ''
    };

    $("#mngrListGrid").jqGrid('setRowData', mngrRowId, setData);
    
    if(!displayName || displayName.trim() === '') {
        $("#mngrListGrid tr#" + mngrRowId + " td:eq(" + 2 + ")").css({ 'display': 'none'});
        $("#mngrListGrid tr#" + mngrRowId + " td:eq(" + 3 + ")").attr('colspan', '3');
        $("#mngrListGrid tr#" + mngrRowId + " td:eq(" + 4 + ")").css({ 'display': 'none'});
    } else {
        $("#mngrListGrid tr#" + mngrRowId + " td:eq(" + 2 + ")").css({ 'display': 'table-cell'});
        $("#mngrListGrid tr#" + mngrRowId + " td:eq(" + 3 + ")").attr("colspan", "0");
        $("#mngrListGrid tr#" + mngrRowId + " td:eq(" + 3 + ")").css({ 'display': 'table-cell'});
        $("#mngrListGrid tr#" + mngrRowId + " td:eq(" + 4 + ")").css({ 'display': 'table-cell'});
    }

    $("#grid_server_no" + (Number(mngrRowId) - 1)).val(displayUserNo || '');
    $("#grid_server_nm" + (Number(mngrRowId) - 1)).val(displayName || '');

    // 서버에 저장
    var postData = {
        mngrList: JSON.stringify(mngrList),
        userFlag: "server" + targetIdx,
        target_id: currentServerInfo.target_id,
        ap_no: currentServerInfo.ap_no,
        server_nm: currentServerInfo.server_nm
    };
    
    $.ajax({
        type: "POST",
        url: "/popup/updateTargetUser",
        async: false,
        data: postData,
        success: function(result) {
            if(result.resultCode != 0) {
                alert(result.resultMeassage);
                
                // 실패 시 롤백
                var rollbackData = {
                    USER_NO: originalData.userNo,
                    USER_NAME: originalData.userName || '지정된 담당자가 없습니다.',
                    USER_SOSOK: originalData.userSosok
                };
                $("#mngrListGrid").jqGrid('setRowData', mngrRowId, rollbackData);
                $("#grid_server_no" + (Number(mngrRowId) - 1)).val(originalData.userNo);
                $("#grid_server_nm" + (Number(mngrRowId) - 1)).val(originalData.userName);
                
                if(!originalData.userName) {
                    $("#mngrListGrid tr#" + mngrRowId + " td:eq(" + 2 + ")").css({ 'display': 'none'});
                    $("#mngrListGrid tr#" + mngrRowId + " td:eq(" + 3 + ")").attr('colspan', '3');
                    $("#mngrListGrid tr#" + mngrRowId + " td:eq(" + 4 + ")").css({ 'display': 'none'});
                }
            } else {
                alert("담당자 저장이 완료되었습니다.");
                
                // 성공 시 서버 그리드 새로고침
                $("#serverGrid").trigger("reloadGrid");
                
                if(currentServerInfo.rowid) {
                    setTimeout(function() {
                        $("#serverGrid").jqGrid('setSelection', currentServerInfo.rowid, true);
                    }, 100);
                }
                
                $("#serverGrid").setGridParam({
              		url:"<%=request.getContextPath()%>/target/selectServerTargetUser", 
              		postData : postData, 
              		datatype:"json" 
              	}).trigger("reloadGrid");
            }
        },
        error: function(request, status, error) {
            console.log("ERROR : ", error);
            alert("저장 중 오류가 발생했습니다.");

            // 에러 시 롤백
            var rollbackData = {
                USER_NO: originalData.userNo,
                USER_NAME: originalData.userName || '지정된 담당자가 없습니다.',
                USER_SOSOK: originalData.userSosok
            };
            $("#mngrListGrid").jqGrid('setRowData', mngrRowId, rollbackData);
            $("#grid_server_no" + (Number(mngrRowId) - 1)).val(originalData.userNo);
            $("#grid_server_nm" + (Number(mngrRowId) - 1)).val(originalData.userName);
            
            if(!originalData.userName) {
                $("#mngrListGrid tr#" + mngrRowId + " td:eq(" + 2 + ")").css({ 'display': 'none'});
                $("#mngrListGrid tr#" + mngrRowId + " td:eq(" + 3 + ")").attr('colspan', '3');
                $("#mngrListGrid tr#" + mngrRowId + " td:eq(" + 4 + ")").css({ 'display': 'none'});
            }
        }
    });
    
    $("#adminPopup").hide();
});

$("#importExcel").change(function(){
	var checkFileNm = $("#importExcel").val();
	var filelength = checkFileNm.lastIndexOf('\\');
	var fileNm = checkFileNm.substring(filelength+1, checkFileNm.length);
	var resulList = [];
	
	var mngrNameList = []; 
	mngrNameListJson.map(function(item) {
		mngrNameList.push({name : item.NAME.replace(/\s+/g, ''), name_key:item.MGNR_FLAG}); 
	});
	
	var CNT = 0;
	let input = event.target;
	let reader = new FileReader();
	reader.onload = function () {
		let data = reader.result;
		let workBook = XLSX.read(data, { type: 'binary' });
		var insertCellRow = 0;
		workBook.SheetNames.forEach(function (sheetName) {
			let rows = XLSX.utils.sheet_to_json(workBook.Sheets[sheetName]);
			var details = "";
			
			var user_no = "";
			var insa_code = "";
			var firstRowChk = false;
			
			details += "<tr height=\"45px;\" >";
			details +=	"<th>no</th>";
			details +=	"<th style=\"width: 166px;\">호스트명</th>";
			mngrNameList.map(function(items) {
			    details += "<th>" + items.name + "</th>";
			}); 
			details += "</tr>";
			
			var errorCnt = 0;
			// 필수 값만 확인
			if(rows[0].hasOwnProperty('호스트명')){
				$.each(rows, function(index, item) {
					var host_name = item.호스트명;
					if(host_name == null || user_no == null){
						++errorCnt;
						return true; 
					}
					 
					var mngrList = [];
					++insertCellRow;
  					details += "<tr style=\"height: 45px;\">"; 
  					details += "	<td style=\"text-align: center; padding-left: 0;\">"+insertCellRow+"</td>";
  					details += "	<td style=\"text-align: center; padding-left: 0;\">"+host_name+"</td>";
  					
  					mngrNameList.map(function(items) {
						if(item[items.name] != null){ 
							var mngrUserList = String(item[items.name]).split(",");
							var mgnrUser = mngrUserList[0]; 
							if(mngrUserList.length > 1){
								mgnrUser += "외 "+(mngrUserList.length -1)+"명"
							}
		  					details += "	<td style=\"text-align: center; padding-left: 0;\">"+mgnrUser+"</td>";
		  					var server_key = items.name_key;  
		  					mngrList.push({[server_key]: item[items.name]}) 
						}else{
		  					details += "	<td style=\"text-align: center; padding-left: 0;\"></td>";
						}
  					});
  					details += "</tr>";
  					resulList.push({"host_name" : host_name, "mngrNameList" : mngrList})
  				});
				$("#import_targetUserList_excel").html(details);
      		
	 	    	var btnCss ="<button type=\"button\" id=\"btnNewPopupExcelSave\" style=\"margin-right: 5px\" >저장</button>";
	 				btnCss +="<button type=\"button\" id=\"btnNewPopupExcelCencel\" >취소</button>";
	 				
 				$("#insertExcelBtn").html(btnCss)
  			
	  			$("#btnNewPopupExcelCencel").click(function(e){
	  				$("#importExcel").val("");
	  				$("#importExcelNm").val("");
	  				
	  				var details = "";
	  				$("#insertExcelBtn").html(details);
	  				$("#import_targetUserList_excel").html(details);
	  				$("#insertExcelPopup").hide();
	          	});
 				 
 				if(errorCnt > 0) alert("호스트명 미 기입된 값이 "+errorCnt+"건 있습니다.");
  			
	  			$('#btnNewPopupExcelSave').on('click', function(){
	  				var message = ("아래 인원들을 담당자로 지정하시겠습니까?");
	              	
	  				if (confirm(message)) {
	              		$.ajax({
	          				type: "POST",
	          				url: "/target/insertExcelTargetUserList",
	          				async : false,
	          				data : {
	          					"resulList": JSON.stringify(resulList)
	          				},
	          			    success: function (resultMap) {
	          			    	if(resultMap.resultCode == 0) {
	          			    		alert(resultMap.resultMeassage);
	          			    		
	          			    		$("#importExcel").val("");
	          						$("#importExcelNm").val("");
	          						
	          						var details = "";
	          						$("#insertExcelBtn").html(details);
	          						$("#import_targetUserList_excel").html(details);
	          						$("#insertExcelPopup").hide();
	          						
	          						setServer(); 
	          			    	}else {
	          			    		alert(resultMap.resultMeassage);
	          			    	}
	          			    },
	          			    error: function (request, status, error) {
	          			        console.log("ERROR : ", error);
	          			    }
	          			});
	              	}
	          	});
	      	}else {
	      		alert("올바른 형식의 엑셀이 아닙니다. 확인 후 다시 시도해 주세요.");
	      		return;
	      	}
			
			$("#import_targetUserList_excel").html(details);
			$("#importExcelNm").val(fileNm);
      })
  };
  reader.readAsBinaryString(input.files[0]);
});

</script>

</body>
</html>