<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%@ include file="../../include/header.jsp"%>
<style>
.ui-jqgrid tr.ui-row-ltr td {
	cursor: pointer;
}
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/xlsx.full.min.js"></script>
	<section id="section">
		<div class="container">
			<h3>PaaS 경로</h3>
			<div class="content magin_t25">
				<div class="list_sch" style="height: 39px; margin-top: 10px;" >
					<div class="sch_area">
						<div id="searchConditionsContainer" style="float: left;" ></div>
						<div style="float: right; margin-left: 3px; margin-bottom: 7px;">
							<button type="button" class="btn_down" id="btnDownloadExcel">다운로드</button>
						</div>
						<div style="float: right;">
							<h5 style="width: 24px; height: 24px; margin-top: 2px; background-size : 100%; background-image: url('${pageContext.request.contextPath}/resources/assets/images/pi_list.png');" id="tableShow">&nbsp;</h5>
						</div>
						<div class="btn_grid_Search" style="float: left; margin: 6px 3px 0 0 ;">&nbsp;</div>
					</div>
                </div>
				<div class="left_box2" style="height: auto; min-height: 672px; overflow: hidden; width:59vw; margin-top: 10px;">
					<table id="targetGrid"></table>
					<div id="targetGridPager"></div>
				</div>
			</div>
		</div>
	</section>

<div id="userPopup" class="popup_layer" style="display:none">
	<div class="popup_box" style="height: 385px; width: 900px; left: 42%; top: 58%; padding: 10px; background: #f9f9f9;">
	<img class="CancleImg" id="btnCancleUserPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 style="color: #222; padding: 0; box-shadow: none;">상세 정보</h1>
		</div>
		<div class="popup_content">
			<div class="content-box" style="height: 335px; background: #fff; border: 1px solid #c8ced3;">
				<table class="popup_tbl">
					<colgroup>
						<col width="15%">
						<col width="35%">
						<col width="15%">
						<col width="35%">
					</colgroup>
					<tbody>
						<tr>
							<th>노드명</th>
							<td>
								<input type="text" id="cs_host_name" value="" class="edt_sch" style="width: 285px; padding-left: 5px;" readonly>
							</td>
							<th>담당자</th>
							<td>
								<input type="text" id="cs_user_name" value="" class="edt_sch" style="width: 200px; padding-left: 5px;" readonly>
								<input type="hidden" id="uid" value="">
								<button type="button" id="cs_UserBtn" style="width: 63px; height: 27px;">수정</button>
							</td>
						</tr>
						<tr>
							<th>네임 스페이스</th>
							<td>
								<input type="text" id="cs_name_space" value="" class="edt_sch" style="width: 285px; padding-left: 5px;" readonly>
							</td>
							<th>소속</th>
							<td><input type="text" id="cs_team_name" value="" class="edt_sch" style="width: 200px; padding-left: 5px; background-color: rgba(210, 210, 210, 0.35);" readonly></td>
						</tr>
						<tr>
							<th>POD ID</th>
							<td>
								<input type="text" id="cs_pod_id" value="" class="edt_sch" style="width: 285px; padding-left: 5px;" readonly>
							</td>
							<th>사번</th>
							<td><input type="text" id="cs_user_no" value="" class="edt_sch" style="width: 200px; padding-left: 5px; background-color: rgba(210, 210, 210, 0.35);" readonly></td>
						</tr>
						<tr>
							<th>POD 명</th>
							<td>
								<input type="text" id="cs_pod_name" value="" class="edt_sch" style="width: 285px; padding-left: 5px;" readonly>
							</td>
						</tr>
						<tr>
							<th>컨테이너 ID</th>
							<td>
								<input type="text" id="cs_container_id" value="" class="edt_sch" style="width: 285px; padding-left: 5px;" readonly>
							</td>
						</tr>
						<tr>
							<th>컨테이너 명</th>
							<td>
								<input type="text" id="cs_container_name" value="" class="edt_sch" style="width: 285px; padding-left: 5px;" readonly>
							</td>
						</tr>
						<tr>
							<th>대상 경로</th>
							<td colspan="3">
								<input type="text" id="cs_path_detail" value="" class="edt_sch" style="width: 725px; padding-left: 5px;" readonly>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area" style="padding: 10px 0; margin: 0;">
				<button type="button" id="btnUserSave">저장</button>
				<button type="button" id="btnUserCancel">취소</button>
			</div>
		</div>
	</div>
</div>

<div id="tableCustomData" class="ui-widget-content" style="position:absolute; right: 9%; top: 148px; touch-action: none; width: 165px; z-index: 999; 
		border-top: 2px solid #222222; box-shadow: 2px 2px 5px #ddd; display:none">
	<table id="gridListTd">
	</table>
</div>

<%@ include file="../../include/footer.jsp"%>

<script type="text/javascript"> 
var colModel = [];
GridName = "#targetGrid";
$(document).ready(function () {

	fn_drawTargetNGrid();
	$("#targetGrid").setGridParam({url:"<%=request.getContextPath()%>/target/selectPaaSList", postData : "", datatype:"json" }).trigger("reloadGrid");
	// 엔터 입력시 발생하는 이벤트
	$('#host_name, #service_name, #user_name, #cs_path').keyup(function(e) {
		if (e.keyCode == 13) {
		    $("#btnSearch").click();
	    }        
	});
	
	$(".navGridSearchBtn").click(function(e){
		url = "<%=request.getContextPath()%>/target/selectPaaSList";
		navGridSearchBtn(url);
	});
	
	$("#searchContent").keyup(function(e){
		if (e.keyCode == 13) {
			url = "<%=request.getContextPath()%>/target/selectPaaSList";
			navGridSearchBtn(url);
		}
	});
	
	// 검색 조회
	$("#btnSearch").click(function(e){
		var postData = {
			host_name : $("#host_name").val(),
			service_name : $("#service_name").val(),
			user_name : $("#user_name").val(),
			cs_path : $("#cs_path").val()
		};

		$("#targetGrid").setGridParam({
			url:"<%=request.getContextPath()%>/target/selectPaaSList",
			postData : postData, 
			datatype:"json" 
		}).trigger("reloadGrid");
	});
	
});

var resetFomatter = null;

function fn_drawTargetNGrid() {
	var gridWidth = $("#targetGrid").parent().width();
	var gridHeight = 585;
	
	colModel.push({label:"UID",  index: 'UID', 					name: 'UID',					width: 30, 	align: 'center', hidden: true});
	colModel.push({label:"노드명",  index: 'HOST_NAME', 			name: 'HOST_NAME',				width: 30, 	align: 'center'});
	colModel.push({label:"컨테이너명",  index: 'CONTAINER_NAME', 		name: 'CONTAINER_NAME',			width: 30, 	align: 'center'});
	colModel.push({label:"CONTAINER_ID",  index: 'CONTAINER_ID', 			name: 'CONTAINER_ID',			width: 30, 	align: 'center', hidden: true});
	colModel.push({label:"담당자명",  index: 'USER_NAME', 			name: 'USER_NAME',				width: 10, 	align: 'center'});
	colModel.push({label:"부서명",  index: 'TEAM_NAME', 			name: 'TEAM_NAME',				width: 10, 	align: 'center'});
	colModel.push({label:"NAME_SPACE",  index: 'NAME_SPACE', 			name: 'NAME_SPACE',				width: 30, 	align: 'center', hidden: true});
	colModel.push({label:"POD_NAME",  index: 'POD_NAME', 				name: 'POD_NAME',				width: 30, 	align: 'center', hidden: true});
	colModel.push({label:"POD_ID",  index: 'POD_ID', 				name: 'POD_ID',					width: 30, 	align: 'center', hidden: true});
	colModel.push({label:"IMAGE_ID",  index: 'IMAGE_ID', 				name: 'IMAGE_ID',				width: 30, 	align: 'center', hidden: true});
	colModel.push({label:"사번",  index: 'USER_NO', 				name: 'USER_NO',				width: 10, 	align: 'center'});
	colModel.push({label:"대상 경로",  index: 'CS_PATH', 				name: 'CS_PATH',				width: 55, 	align: 'left'});
	GridSearchTypeChk();
	searchListAppend(); 
	$("#targetGrid").jqGrid({
		url: "<%=request.getContextPath()%>/target/selectPaaSList",
		datatype: "local",
	   	mtype : "POST",
		colModel : colModel,
		loadonce: true, // this is just for the demo
	   	autowidth: true,
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: gridWidth,
		height: gridHeight,
		shrinkToFit: true,
		pager: "#targetGridPager",
		rownumbers : false, // 행번호 표시여부
// 		multiselect: true,
		mutipageSelection: true,
		rownumWidth : 30, // 행번호 열의 너비	
		rowNum:20,  
		rowList:[25,50,100],			
		//jqgrid의 특성상 - rowpos의 이벤트는 onSelectCell, beforeEditCell 다 해주어야 함
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	  	
	  	},
	  	ondblClickRow: function(nRowid, icol, cellcontent, e){
	  	},
	  	onCellSelect: function(rowid,icol,cellcontent,e) {
	  		$("#rowid").val(rowid);
	  		var UID = $(this).getCell(rowid, 'UID');
	  		var HOST_NAME = $(this).getCell(rowid, 'HOST_NAME');
	  		var CONTAINER_NAME = $(this).getCell(rowid, 'CONTAINER_NAME');
	  		var CONTAINER_ID = $(this).getCell(rowid, 'CONTAINER_ID');
	  		var NAME_SPACE = $(this).getCell(rowid, 'NAME_SPACE');
	  		var POD_NAME = $(this).getCell(rowid, 'POD_NAME');
	  		var POD_ID = $(this).getCell(rowid, 'POD_ID');
	  		var IMAGE_ID = $(this).getCell(rowid, 'IMAGE_ID');
	  		var USER_NAME = $(this).getCell(rowid, 'USER_NAME');
	  		var TEAM_NAME = $(this).getCell(rowid, 'TEAM_NAME');
	  		var USER_NO = $(this).getCell(rowid, 'USER_NO');
	  		var CS_PATH = $(this).getCell(rowid, 'CS_PATH');
	  		
	  		$("#userPopup").show();
	  		
	  		$("#uid").val(UID);
	  		$("#cs_host_name").val(HOST_NAME);
	  		$("#cs_container_name").val(CONTAINER_NAME);
	  		$("#cs_container_id").val(CONTAINER_ID);
	  		$("#cs_name_space").val(NAME_SPACE);
	  		$("#cs_pod_name").val(POD_NAME);
	  		$("#cs_pod_id").val(POD_ID);
	  		$("#cs_user_name").val(USER_NAME);
	  		$("#cs_user_no").val(USER_NO);
	  		$("#cs_team_name").val(TEAM_NAME);
	  		$("#cs_path_detail").val(CS_PATH);
	    },
		loadComplete: function(data) {
			automaticCompletion(null);
		},
		gridComplete : function() {
		}
	});
}

$("#cs_UserBtn").click(function() {
	userListWindows("PaaS");
});

$("#btnCancleUserPopup").click(function(e){
	$("#userPopup").hide();
});

$("#btnUserCancel").click(function(e){
	$("#userPopup").hide();
});
$("#btnDownloadExcel").click(function(){
	
	$("#targetGrid").jqGrid("exportToCsv",{
        separator: ",",
        separatorReplace : "", // in order to interpret numbers
        quote : '"', 
        escquote : '"', 
        newLine : "\r\n", // navigator.userAgent.match(/Windows/) ? '\r\n' : '\n';
        replaceNewLine : " ",
        includeCaption : true,
        includeLabels : true,
        includeGroupHeader : true,
        includeFooter: true,
        includeHeader: false,
        exportHiddenColumns: false,
        fileName : "PaaS_경로.csv",
        mimetype : "text/csv; charset=utf-8",
        event : resetFomatter,
        returnAsString : false,
        customFormatter: function (options, rowId, rowData, colModel) {
            // rowData는 내보내려는 행의 데이터
            // colModel은 그리드의 열 모델 설정
            var row = '', colValue;
            for (var i = 0; i < colModel.length; i++) {
                // exportable 속성이 설정되지 않았거나 true로 설정된 열만 처리
                if (colModel[i].exportable !== false) {
                    colValue = rowData[colModel[i].name];
                    // 필요한 경우 colValue를 조작할 수 있음
                    // CSV 형식에 맞춰서 데이터를 row에 추가
                    row += '"' + colValue + '"' + (i < colModel.length - 1 ? ',' : '');
                }
            }
            return row;
        }
    });
	
	resetFomatter = null;
	$("#targetGrid").jqGrid("showCol",["CHK"]);
});

function userListWindows(info){
	var pop_url = "${getContextPath}/popup/userList";
	var id = "targetList"
	var winWidth = 700;
	var winHeight = 570;
	var popupOption= "width="+winWidth+", height="+winHeight + ", left=0, top=0, scrollbars=no, resizable=no, location=no"; 	
	var pop = window.open(pop_url,id,popupOption);
	/* popList.push(pop);
	sessionUpdate(); */
	
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


$("#btnUserSave").click(function(e){
	var postData = {
  		user_no: $("#cs_user_no").val(),
  		uid: $("#uid").val(),
  		host_name: $("#cs_host_name").val(),
  		cs_path: $("#cs_path_detail").val(),
  		container_id: $("#cs_container_id").val()
	};
	
	var massage = "담당자를 지정하시겠습니까?";
	
	if(confirm(massage)){
		$.ajax({
			type: "POST",
			url: "/target/updateCS_Path_Mngr",
			async : false,
			data : postData,
		    success: function (resultMap) {
		    	if(resultMap.resultCode != 0){
	            	alert(resultMap.resultMeassage);
	            } else {
	            	alert("담당자 지정에 완료하였습니다");
	            	$("#targetGrid").setGridParam({
	            		url:"<%=request.getContextPath()%>/target/selectPaaSList", 
	            		postData : "", 
	            		datatype:"json" 
	            	}).trigger("reloadGrid");
	            	$("#userPopup").hide();
	            	$("#host_name").val('');
	            	$("#service_name").val('');
	            	$("#user_name").val('');
	            	$("#cs_path").val('');
	            }
		    },
		    error: function (request, status, error) {
		        console.log("ERROR : ", error);
		    }
		    
		});
	}
});

</script>
</body>

</html>