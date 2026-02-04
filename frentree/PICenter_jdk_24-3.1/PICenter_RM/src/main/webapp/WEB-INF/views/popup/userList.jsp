<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
 
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta charset="utf-8">
<title>사용자 조회</title>
<link rel="icon" href="${pageContext.request.contextPath}/resources/assets/images/favicon.ico" type="image/x-icon">

<link href="${pageContext.request.contextPath}/resources/assets/css/ui.fancytree.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resources/assets/css/font-awesome.min.css" rel="stylesheet" type="text/css" />

<!-- Publish JS -->
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/jquery-3.3.1.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/js/jquery-ui-PIC.js" type="text/javascript"></script>

<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/tree/jquery.fancytree.ui-deps.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/tree/jquery.fancytree.js"></script>

<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/jqgrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/jqgrid/i18n/grid.locale-kr.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/wickedpicker.js"></script>

<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/resources/assets/css/wickedpicker.min.css" />

<!-- Application Common Functions  -->
<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/js/common.js"></script>

<!-- Publish CSS -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/assets/css/reset-PIC.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/assets/css/design-PIC.css" />
<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/resources/assets/css/select2.css" />
<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/resources/assets/css/wickedpicker.min.css" />
<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/resources/assets/css/jquery-ui-PIC.css" />
<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/resources/assets/css/ui.jqgrid-PIC.css" />
<%@ include file="../../include/session.jsp"%>
<style>
	html, body {
		overflow-x: hidden;
		max-width: 100%;
	}
	body{
		width: auto;
	}
	.ui-widget.ui-widget-content{
		border: none;
		border-bottom: 1px solid #c8ced3;
		border-radius: unset !important;
	}
	.ui-jqgrid tr.ui-row-ltr td{
		cursor: pointer;
	}
	@media screen and (-ms-high-contrast: active), (-ms-high-contrast: none) {
		html{
			overflow: auto !important;
		}
		body{
			width: auto !important;
		}
	}
	.popup_btn {
		height: 45px;
	}
	.btn_area {
		padding: 10px 0;
		margin: 0;
		text-align: right;
	}
	.btn_area button {
		margin: 0;
		padding: 0 20px;
		font-size: 12px;
		height: 25px;
		cursor: pointer;
		border: 1px solid #c8ced3;
		background: #fff;
		border-radius: 4px;
		outline-width: 0;
		vertical-align: middle;
	}
	.btn_area button:hover {
		background: #f0f0f0;
	}
</style>
</head>
<body style="overflow: hidden;">
	<div id="stepContents1" class="step_content" style="border-top: 1px solid #aca49c; width: 100%; height: 100%; background: #f9f9f9; overflow-x: hidden;">
		<div class="step_content_cell fl_l" style="overflow-y: auto; overflow-x: hidden; padding: 0 15px; width: calc(100% - 30px); max-width: 100%; box-sizing: border-box;">
			<h1 style="color: #222; font-size: 20px; padding: 0; box-shadow: none; margin: 10px 0;">사용자 조회</h1>
		
			<div class="select_location sch_left" style="height: 50px; min-height: 50px; margin-top: 10px; width:100%; background: #fff; border: 1px solid #c8ced3; border-radius: 0; box-sizing: border-box;">
				<div style="position: absolute; top: 10px; right: 25px; padding-top: 0px; font-size: 14px; font-weight: bold;">
				소속 : <input type="text" id="searchGroup" value="" class="edt_sch" style="width: 125px; margin-bottom: 3px;" onKeypress="javascript:if(event.keyCode==13) fnSearch()">
				담당자 : <input type="text" id="searchUser" value="" class="edt_sch" style="width: 125px; margin-bottom: 3px;" onKeypress="javascript:if(event.keyCode==13) fnSearch()">
				<button id="btnSearch" class="btn_sch">검색</button>
				</div>
			</div>
			<div class="grid_top" style="width: 100%; height: calc(80% - 60px); box-sizing: border-box;">
				<div class="left_box2" style="height: auto; min-height: 343px; overflow: hidden; width:100%; border-left: 1px solid #c8ced3; border-right: 1px solid #c8ced3; box-sizing: border-box;">
			 		<table id="targetUserGrid"></table>
					<div id="targetUserGridPager"></div>
				</div>
			</div>
			<div class="popup_btn">
				<div class="btn_area">
					<button type="button" id="btnConfirm">확인</button>
					<button type="button" id="btnClose">닫기</button>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
var info = "${info}";
var isManagerMode = false; // 전역 변수로 선언(렌더링 순서 문제로 전역변수로 지정)

$(document).ready(function() {
	
	if (info && info.trim() !== "") {
		isManagerMode = info.includes("mngr_");
	} else {
		isManagerMode = false;
	}
	
	// 버튼 표시/숨김 처리
	if (isManagerMode) {
		$("#btnConfirm").show();
		$("#btnClose").show();
	} else {
		$("#btnConfirm").hide();
		$("#btnClose").hide();
	}
	
	fn_targetUserGrid();
});


function fn_targetUserGrid() {
	if ($("#targetUserGrid").length > 0) {
		try {
			$("#targetUserGrid").jqGrid('GridUnload');
		} catch(e) {
			console.log("그리드 언로드 에러:", e);
		}
	}
	
	var gridWidth = $("#targetUserGrid").parent().width();
	
	$("#targetUserGrid").jqGrid({
		url: "<%=request.getContextPath()%>/popup/selectUserList",
		datatype: "local",
	   	mtype : "POST",
		colNames:['ID','성명','팀코드','소속','직책', '이메일'],
		colModel: [
			{ index: 'USER_NO', 	name: 'USER_NO', 	width: 80, align: "center"},
			{ index: 'USER_NAME', 	name: 'USER_NAME', 	width: 80, align: "center"},
			{ index: 'INSA_CODE', 	name: 'INSA_CODE', 	width: 100, align: "left", hidden:true},
			{ index: 'TEAM_NAME', 	name: 'TEAM_NAME', 	width: 100, align: "center" },
			{ index: 'JIKGUK', 		name: 'JIKGUK', 	width: 100, align: "center"},
			{ index: 'USER_EMAIL', 	name: 'USER_EMAIL', width: 120, align: "left"},
		],
		loadonce:true,
	   	autowidth: true,
		shrinkToFit: true,
		viewrecords: true,
		width: gridWidth,
		height: 350,
		rownumbers : false,
		rownumWidth : 35,
		rowNum:20,
		rowList:[25,50,100],
		multiselect: isManagerMode, // mngr_일 때만 체크박스 활성화
		pager: "#targetUserGridPager",
	  	onSelectRow : function(rowid, status, e) {

	  		if(!isManagerMode) {
	  			processSelectedUser(rowid);
// 	  			window.close();    
	  		}
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
	    	var multiselectValue = $("#targetUserGrid").jqGrid('getGridParam', 'multiselect');
	    }
	});
}

var aut = "${aut}"

$("#btnSearch").click( function(e) {
	fnSearch(e);
});	

function fnSearch(e) {
	var searchGroup = $("#searchGroup").val();
	var searchUser = $("#searchUser").val();
	
	if (isNull($("#searchGroup").val().trim()) && isNull($("#searchUser").val().trim())) {
		alert("소속 혹은 담당자를 입력하세요");
		return;
	}
	
	var postData = {
		user_nm : searchUser,
		team_nm :searchGroup
	};
	$("#targetUserGrid").setGridParam({
		url:"<%=request.getContextPath()%>/popup/selectUserList",
		page : 1, 
		postData : postData, 
		datatype:"json" 
	}).trigger("reloadGrid");
}

// 확인 버튼 클릭 이벤트
$("#btnConfirm").click(function() {
	
	var selectedRows = $("#targetUserGrid").jqGrid('getGridParam', 'selarrrow');

	if(selectedRows.length === 0) {
		alert("사용자를 선택해주세요.");
		return;
	}
	
	// 선택된 모든 row 처리
	selectedRows.forEach(function(rowid) {
		processSelectedUser(rowid);
	});
	  
// 	window.close();
});

// 닫기 버튼 클릭 이벤트
$("#btnClose").click(function() {
	window.close();
});

// 기존 onSelectRow 로직을 함수로 분리
function processSelectedUser(rowid) {
	var user_no = $("#targetUserGrid").getCell(rowid, 'USER_NO');
	var user_name = $("#targetUserGrid").getCell(rowid, 'USER_NAME');
	var insa_code = $("#targetUserGrid").getCell(rowid, 'INSA_CODE');
	var team_name = $("#targetUserGrid").getCell(rowid, 'TEAM_NAME');
	var jikwee = $("#targetUserGrid").getCell(rowid, 'JIKGUK');
	var user_email = $("#targetUserGrid").getCell(rowid, 'USER_EMAIL');
	var user_team = user_name + '(' + team_name + ')';
	
	if(info.includes("mngr_")){
		var idList = info.split("_")[1];
		var paRowId = Number(idList)+1;
		
		$(opener.document).find("#grid_server_no"+idList).val(user_no);
		$(opener.document).find("#grid_server_nm"+idList).val(user_name);
		
		var grid = $(opener.document).find("#targetUserGrid");

		grid.jqGrid('addRowData', user_no, {
			user_no: user_no,
			user_name: user_name,
			jikwee: jikwee,
			team_name: team_name,
			user_email: user_email
		});

		$(opener.document)
			.find("#targetUserGrid")
			.find("tr[id='" + user_no + "'] input.cbox")
			.prop('checked', true);

		$(opener.document)
			.find("#targetUserGrid")
			.find("tr[id='" + user_no + "']")
			.addClass("ui-state-highlight");
		
	} else if(info == "serviceManager"){
		if(team_name != "" && team_name != null){
			$(opener.document).find("#targetServiceManager").val(user_team);
		}else{
			$(opener.document).find("#targetServiceManager").val(user_name);
		}
		
		$(opener.document).find("#targetServiceManagerName").val(user_name);
		$(opener.document).find("#targetServiceManagerID").val(user_no);
		$(opener.document).find("#targetServiceManagerTeamCode").val(insa_code);
		$(opener.document).find("#targetServiceManagerTeam").val(team_name);
		
	} else if(info == "user"){
		$(opener.document).find("#createServerUserID").val(user_no);
		$(opener.document).find("#createServerUser").val(user_name);
		$(opener.document).find("#createServerUserGroupCode").val(insa_code);
		$(opener.document).find("#createServerUserGroup").val(team_name);
		
	} else if(info == "PCManager"){
		$(opener.document).find("#PCtargetInfraID").val(user_no);
		$(opener.document).find("#PCtargetInfra").val(user_team);
		
	}else if(info == "lockMember"){
		$(opener.document).find("#LockMemberManager").val(user_name);
		
	}else if(info == "changeUser"){
		if('${memberInfo.USER_GRADE}' == '9'){
			var result = confirm(user_no+'으로 로그인 하시겠습니까?');
			if(result){
				changeLoginId(user_no);
			}
		}
	}else if(info == "approval_user"){
		var approvalMap = $(opener.document).find("#mngrDataList").val();
		var rowId = $(opener.document).find("#approvalMngrGrid").getGridParam("reccount");

		var approvalMap = approvalMap.split("'[{").join('').split("}]'").join('');
		approvalMap = approvalMap.split('}, {');
		
		var html = "<select id=\"approval_mngr_list"+(rowId+1)+"\" name=\"approval_mngr_list"+(rowId+1)+"\" onchange=\"changeApprovalStatus("+(rowId+1)+")\" style=\"width:132px; font-size: 12px; padding-left: 5px;\">";
			html += "<option value='' selected>구분</option>"; 
		
		for(var a = 0; approvalMap.length > a; a++){
			var row = approvalMap[a].split(', ');
			var status = row[0].split("STATUS=").join('')
			var name = row[1].split('NAME=').join('')
			
			html += "<option value=\""+status+"\">"+name+"</option>"; 
		}
		// 
		var rowData = {};
		rowData["NAME"] = html;
		rowData["STATUS"] = "";
		rowData["USER_NAME"] = user_name;
		rowData["USER_NO"] = user_no;
		rowData["JIKGUK"] = jikwee;
		rowData["COM"] = team_name;
		rowData["IDX"] = ""; 
		rowData["IDX2"] = "";
		
		$(opener.document).find("#approvalMngrGrid").addRowData(rowId+1, rowData, 'first');
		
	}else if(info == "PaaS") {

		$(opener.document).find("#cs_user_name").val(user_name);
		$(opener.document).find("#cs_user_no").val(user_no);
		$(opener.document).find("#cs_team_name").val(team_name);
		
	}else if(info == "GroupManager"){

		$(opener.document).find("#pcAdmin").val(user_name);
		$(opener.document).find("#pcAdminId").val(user_no);
	}else if(info == "teamIfo"){
		var user_no = $("#targetUserGrid").getCell(rowid, 'USER_NO');
		
		var updateInsaCode = $(opener.document).find("#update_insa_code").val();
		
		var postData = {
			user_no : user_no,
			updateInsaCode : updateInsaCode,
		};
		
		if(confirm("결재자를 추가하시겠습니까?" )){
			$.ajax({
				type: "POST",
				url: "/setting/insertGroupApprovalUser",
				//async : false,
				data : postData,
				dataType: "json",
			    success: function (resultMap) {
			    	postData = {};  
			    	console.log(resultMap); 
			    	if(resultMap.resultCode == 200){  
			    		if (opener && typeof opener.teamApprovalGridReload === 'function') {
			    		    opener.teamApprovalGridReload();  // 부모 함수 호출
			    		} else {
			    		    console.error('부모 함수를 찾을 수 없거나 부모 창이 닫혔습니다.');
			    		}  
			    	}else{
			    		alert("추가를 실패하였습니다. \n관리자에게 문의해주세요.");
			    	}
			    },
			    error: function (request, status, error) {
		        	console.log("ERROR : ", error);
		        	console.log("ERROR : ", request);  
		        	opener.teamApprovalGridReload();
// 			    	alert("설정 변경에 실패하였습니다.");    
			    }
			});
		}
	}else if(info == "mail_sender"){
		// 메일 발신자 선택 (단일 선택)
		$(opener.document).find("#mailSenderData").val(user_no);
		$(opener.document).find("#mailSenderDisplay").text(user_name + ' (' + team_name + ')');
	}else if(info == "mail_receiver"){
		// 메일 수신자 선택 (복수 선택)
		var existingData = $(opener.document).find("#mailReceiverData").val();
		var existingList = existingData ? existingData.split(',').filter(function(x) { return x !== ''; }) : [];

		if(existingList.indexOf(user_no) === -1) {
			existingList.push(user_no);
			$(opener.document).find("#mailReceiverData").val(existingList.join(','));

			// 표시 영역에 추가
			var displayHtml = '<span class="mail-user-tag" data-user-no="' + user_no + '" style="display:inline-block; background:#e0e0e0; padding:2px 8px; margin:2px; border-radius:3px;">' + user_name + ' (' + team_name + ') <a href="#" class="remove-mail-user" data-type="receiver" data-user-no="' + user_no + '" style="color:red; text-decoration:none; margin-left:5px;">×</a></span>';
			$(opener.document).find("#mailReceiverList").append(displayHtml);
		}
	}else if(info == "mail_cc"){
		// 메일 참조자 선택 (복수 선택)
		var existingData = $(opener.document).find("#mailCcData").val();
		var existingList = existingData ? existingData.split(',').filter(function(x) { return x !== ''; }) : [];

		if(existingList.indexOf(user_no) === -1) {
			existingList.push(user_no);
			$(opener.document).find("#mailCcData").val(existingList.join(','));

			// 표시 영역에 추가
			var displayHtml = '<span class="mail-user-tag" data-user-no="' + user_no + '" style="display:inline-block; background:#e0e0e0; padding:2px 8px; margin:2px; border-radius:3px;">' + user_name + ' (' + team_name + ') <a href="#" class="remove-mail-user" data-type="cc" data-user-no="' + user_no + '" style="color:red; text-decoration:none; margin-left:5px;">×</a></span>';
			$(opener.document).find("#mailCcList").append(displayHtml);
		}
	}

	window.close();
}

function changeLoginId(user_no){
	var postData = {
		user_no : user_no,
	};
	$.ajax({
		type: "POST",
		url: "/changeUser",
		async : false,
		data : postData,
	    success: function (resultMap) {
	    	if (resultMap.resultCode == 0) {
	    		if (resultMap.user_grade == "9"){
	    			opener.parent.location = "<%=request.getContextPath()%>/picenter_target";
		        } else if(resultMap.user_grade == "0" || resultMap.user_grade == "1" || resultMap.user_grade == "2" || resultMap.user_grade == "3" ) {
		        	opener.parent.location = "<%=request.getContextPath()%>/picenter_target";
		        } else if(resultMap.user_grade == "4" || resultMap.user_grade == "5" || resultMap.user_grade == "6" ) {
		        	opener.parent.location = "<%=request.getContextPath()%>/picenter_target";
		        } else {
		        	opener.parent.location = "<%=request.getContextPath()%>/approval/pi_search_approval_list";
		        }
	    	}else if(resultMap.resultCode == -9){
				alert("올바르지 않은 접근 입니다.");
	    	}else if(resultMap.resultCode != -100){
	    		alert(resultMap.resultMessage);
	    	}else {
	    		alert('오류로 인해 사용자 계정을 변경 하지 못하였습니다.\n잠시후 다시 시도해 주세요.');
	    	}
	    }
	});
}
</script>
</html>