<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%@ include file="../../include/header.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/xlsx.full.min.js"></script>
<style>
h4 {
	margin : 5px 0;
	font-size: 0.8vw;
}
#filtersGrid * *{
	overflow: hidden;
	white-space: nowrap; 
	text-overflow: ellipsis; 
}
#insertfiltersTitle::placeholder{
	color: #9E9E9E;
}
@media screen and (-ms-high-contrast: active), (-ms-high-contrast: none) {
	.list_sch{
		top: 41px !important;
	}
}
.content-box {
	padding: 10px !important;
}
#netPolicyBody th {
	height: 41px;
}
</style>
	<section id="section">
		<div class="container"> 
			<h3>예외 관리</h3>
			<div class="content magin_t25">
				<div class="sch_area">    
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
						<div id="searchConditionsContainer" style="float: left;"></div>
						<div style="float: right; margin-left: 3px; margin-bottom: 7px;">
							<button type="button" class="btn_down" id="filtersInsert" class="btn_new">예외 등록</button>
							<button type="button" id="btnDownloadExel" class="btn_down">다운로드</button>
						</div>
						<div style="float: right;">
							<h5
								style="width: 24px; height: 24px; margin-top: 2px; background-size : 100%; background-image: url('${pageContext.request.contextPath}/resources/assets/images/pi_list.png');"
								id="tableShow">&nbsp;</h5>
						</div>
					</div>
					<div class="list_sch" style="height: 39px; margin-top: 10px;">
					</div>
					<div class="left_box2"
						style="height: auto; min-height: 665px; overflow: hidden; width: 59vw; margin-top: 10px;">
						<table id="filtersGrid"></table>
						<div id="filtersGridPager"></div>
					</div>
				</div>
			</div>
		</div>
	</section>
	
	<!------------------ 예외 등록 및 수정 팝업 ------------------>
	<div id="filtersPopup" class="popup_layer" style="display:none">
		<div class="popup_box" style="width: 440px; height: 200px; padding: 10px; background: #f9f9f9; left: 54%; top: 56%;">
		<img class="CancleImg" id="btnCanclefiltersPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
			<div class="popup_top" style="background: #f9f9f9;">
				<h1 style="color: #222; padding: 0; box-shadow: none;" id="popUpName"></h1>
			</div>
			<div class="popup_content">
				<input type="hidden" id="popUpStatus" value="">
				<div class="content-box" style="background: #fff; width: 420px; height:390px; border: 1px solid #c8ced3; overflow: auto;">
					<table class="user_info" style="width: 100%; border: none;">
						<tbody id="netPolicyBody">
							<tr>
								<th>범위</th>
								<td colspan="3" id="updateApNoList">
									<select id="selectInsertList" name="selectInsertList" style="width: 285px;">
										<c:forEach items="${apServerList}" var="item" varStatus="status">
											<option value="${item.AP_NO}">${item.NETWORK}</option>
										</c:forEach>
									</select>
									<input type="hidden" id="updateApNo">
								</td>
								<td colspan="3" id="updateApNoName"></td>
							</tr>
							<tr>
								<th>구분</th>
								<td colspan="3" id="filtersTable">
									<input type="radio" value="exclude_expression" name="selectFilters" > 경로 예외 &nbsp;
									<input type="radio" value="ignore_expression" name="selectFilters"> 패턴 예외
								</td>
								<td colspan="3" id="filtersTableName"></td>
							</tr>
							<tr id="regExp" style="display: none;">
								<th>정규식</th>
								<td>
									<input type="checkbox" name="btnRegExp" id="btnRegExp" value="Y">
								</td>
							</tr>
							<tr>
								<th>대상</th>
								<td colspan="2" id="selectedExcepServer"></td>
								<td class="btn_area" style="text-align: right;">
	                                <button type="button" id="btnGroupSerachPopup" style="margin-bottom: 0px;  width: 73px;">조회</button>
	                                <input type="hidden" id="insert_target_id" value=""> 
	                                <input type="hidden" id="update_filter_id" value=""> 
	                                <input type="hidden" id="inputCreateUser" value=""> 
	                            </td>
							</tr>
							<tr>
								<th style="vertical-align: top;">경로</th>
								<td colspan="3" >
									<textarea rows="11" cols="40" id="inputExcepPath" style="width: 285px; height: 90px; white-space: pre; resize: none; color: #000"></textarea>
								</td>
							</tr>
							<tr>
								<th style="vertical-align: top;">비고</th>
								<td colspan="3" >
									<textarea rows="11" cols="40" id="inputComment" style="width: 285px; height: 90px; white-space: pre; resize: none; color: #000"></textarea>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="popup_btn">
				<div  class="btn_area" style="padding: 10px 0; margin: 0;">
					<button type="button" style="display: none;" id="btnfiltersSave">등록</button>
					<button type="button" style="display: none;" id="btnUpdateFilterPopupSave">등록</button>
					<button type="button" id="btnfiltersCancel">취소</button>
				</div>
			</div>
		</div>
	</div>
	<!------------------ 예외 등록 및 수정 팝업 종료 ------------------>
	
	<!------------------ 예외 일괄 등록  팝업 ------------------>
	<div id="filtersAllPopup" class="popup_layer" style="display:none">
		<div class="popup_box" style="width: 1200px; height: 540px; padding: 10px; background: #f9f9f9; left: 33%; top: 51%;">
		<img class="CancleImg" id="btnCancleExcelPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
			<div class="popup_top" style="background: #f9f9f9;">
				<h1 style="color: #222; padding: 0; box-shadow: none;">예외 일괄 등록</h1>
				<p style="position: absolute; top: 6px; left: 140px; font-size: 12px; color: #9E9E9E;">예외 등록은 최대 50개까지 가능합니다.</p>
				<p style="position: absolute; top: 19px; left: 140px; font-size: 12px; color: #9E9E9E;">PICenter에서 제공하는 형식이 아닌 다른 형식으로 업로드시 생성이 불가합니다.</p>
			</div>
			<div class="popup_content">
				<div class="content-box" style="background: #fff; width: 100%; height:457px; border: 1px solid #c8ced3; overflow: auto;">
					<table class="popup_tbl" style="width: 100%;">
						<colgroup>
							<col width="10%">
							<col width="90%">
						</colgroup>
						<tbody>
							<tr>
								<th>파일 다운로드</th>
								<td>
									<form id="btnDownLoadXlsx" action="<%=request.getContextPath()%>/download/downloadExcel" method="post" style="width: 49px; padding: 0px;">
										<input id="downloadFile" type="hidden" name="filename" value="PICenter_예외_관리.xlsx">
										<input id="downloadRealFile" type="hidden" name="realfilename" value="global_filters_ver1.xlsx">
										<input type="submit" id="btnExcelDown" value="다운로드"> 
									</form>
								</td>
							</tr>
						</tbody>
					</table>
					<table class="popup_tbl2" style="width: 100%;">
						<colgroup>
							<col width="10%">
							<col width="90%">
						</colgroup>
						<tbody >
							<tr>
								<th>
									파일 업로드
								</th>
								<td>
									<button type="button" id="clickimportBtn">파일선택</button>
									<input type="file" id="importExcel" name="importExcel" style="width: 955px; padding-left: 10px; display: none; ">
									<input type="text" id="importExcelNm" style="width: 925px; font-size: 12px; margin: 0 0 0 7px;" readonly="">
								</td>
							</tr>
						</tbody>
					</table>
					<div class="content-table" style="width: 100%; height: 340px; padding: 0;">
						<table class="popup_tbl" style="width: 100%;">
							<colgroup>
								<col width="2%">
								<col width="18%">
								<col width="20%">
								<col width="60%">
							</colgroup>
							<tbody id="import_filters_excel">
								<tr height="45px;" >
									<th></th>
									<th>범위</th>
									<th>대상</th>
									<th>경로</th>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="popup_btn">
				<div class="btn_area" style="padding: 10px 0; margin: 0;" id="filterBtn">
				</div>
			</div>
		</div>
	</div>
	<!------------------ 예외 일괄 등록 팝업 종료 ------------------>
	
	<!------------------ 예외 조회 팝업 ------------------>
	<div id="glovalFilterDetailPop" class="popup_layer" style="display:none">
		<div class="popup_box" style="width: 440px; height: 200px; padding: 10px; background: #f9f9f9; left: 54%; top: 56%;">
		<img class="CancleImg" id="btnCancleDetailFilterPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
			<div class="popup_top" style="background: #f9f9f9;">
				<h1 style="color: #222; padding: 0; box-shadow: none;">예외 상세정보</h1>
			</div>
			<div class="popup_content">
				<div class="content-box" style="background: #fff; height:390px; width: 420px; border: 1px solid #c8ced3; overflow: auto;">
					<table class="user_info" style="width: 100%; border: none;">
						<tbody id="netPolicyBody">
							<tr>
								<th>범위</th>
								<td colspan="3" id="filter_range"></td>
							</tr>
							<tr>
								<th>구분</th>
								<td colspan="3" id="filter_type"></td> 
							</tr>
							<tr id="filter_regExp_tr" style="display: none">
								<th>정규식</th>
								<td colspan="3" id="filter_regExp">
									<input type="checkbox" name="btnFilterRegExp" id="btnFilterRegExp" disabled>
								</td> 
							</tr>
							<tr>
								<th>대상</th>
								<td colspan="3" id="filter_id"></td>
							</tr>
							<tr>
								<th style="vertical-align: top;">경로</th>
								<td colspan="3" id="filter_path">
									<textarea rows="11" cols="40" id="filter_path_list" style="width:285px; height: 90px; resize: none; color: #000" readonly="readonly">
									</textarea>
								</td>
							</tr>
							<tr>
								<th style="vertical-align: top;">비고</th>
								<td colspan="3" id="filter_path">
									<textarea rows="11" cols="40" id="filter_comment" style="width:285px; height: 90px; resize: none; color: #000" readonly="readonly">
									</textarea>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="popup_btn">
				<div class="btn_area" style="padding: 10px 0; margin: 0;">
					<button type="button" id="btnDetailFilterPopup" >닫기</button>
				</div>
			</div>
		</div>
	</div>
	<!------------------ 예외 조회 팝업 종료 ------------------>

	<!-- 그룹 선택 버튼 클릭 팝업 -->
	<div id="taskGroupWindow" class="ui-widget-content" style="position:absolute; left: 10px; top: 10px; touch-action: none; width: 150px; z-index: 999; 
		border-top: 2px solid #2f353a; box-shadow: 0 2px 5px #ddd; display:none">
		<ul>
			<li class="status">
				<button id="globalFilterShow">조회</button></li>
			<li class="status">
				<button id="globalFilterUpdate">수정</button></li>
			<li class="status">
				<button id="glbalFilterDelete">삭제</button></li>
			<li class="status">
				<button id="closeFilterPop">닫기</button></li>
		</ul>
	</div>
		<div id="popup_manageSchedule" class="popup_layer" style="display:none;">
		<div class="popup_box" id="popup_box" style="height: 60%; width: 60%; left: 40%; top: 33%; right: 40%; ">
		</div>
	</div>
	<%@ include file="../../include/footer.jsp"%>

<script type="text/javascript"> 
var colModel = [];
GridName = "#filtersGrid";
$(document).ready(function () {

	var gridWidth = $("#filtersGrid").parent().width();
	var gridHeight = 561;
	 
	$(document).click(function(e){
		$("#taskGroupWindow").hide();
	});
	
	colModel.push({label: "filter_id",  	index: "FILTER_ID", 	name: "FILTER_ID",		width: 0, 		align: "left", 		hidden:true});
	colModel.push({label: "target_id",  	index: "TARGET_ID", 	name: "TARGET_ID",		width: 0, 		align: "left", 		hidden:true});
	colModel.push({label: "AP_NO",  		index: "AP_NO", 		name: "AP_NO",			width: 0, 		align: "center", 	hidden:true});
	colModel.push({label: "범위",  			index: "NETWORK", 		name: "NETWORK",		width: 75, 		align: "center"});
	colModel.push({label: "대상",  			index: "NAME", 			name: "NAME",			width: 150, 	align: "center", 	formatter:formatName, type:6});
	colModel.push({label: "타입",  			index: "TYPE",			name: "TYPE",			width: 100, 	align: "center", 	formatter:filterType});
	colModel.push({label: "create_user",  	index: "CREATE_USER",	name: "CREATE_USER",	width: 100, 	align: "center", 	hidden: true});
	colModel.push({label: "경로",  			index: "PATH", 			name: "PATH",			width: 350, 	align: "left",		formatter:formatPath});
	colModel.push({label: "생성자",  			index: "USER_NAME",		name: "USER_NAME",		width: 100, 	align: "center"});
	colModel.push({label: "생성일",  			index: "CREDATE", 		name: "CREDATE", 		width: 100, 	align: "center", type:3});
	colModel.push({label: "regdate",  		index: "REGDATE", 		name: "REGDATE", 		width: 100, 	align: "center", 	hidden: true});
	colModel.push({label: "비고",  			index: "COMMENT", 		name: "COMMENT",		width: 250, 	align: "left"});
	colModel.push({label: "regExp",  		index: "REGEXPFLAG", 	name: "REGEXPFLAG",		width: 100, 	align: "left", 		hidden: true});
	colModel.push({label: " ",  			index: "VIEW", 			name: "VIEW", 			width: 70, 		align: "center", 	formatter:createView, exportcol : false, type:0});
	GridSearchTypeChk();
	searchListAppend(); 
	$("#filtersGrid").jqGrid({
		url: "<%=request.getContextPath()%>/excepter/glovalFilterDetail",
		datatype: "json",
	   	mtype : "POST",
	   	ajaxGridOptions : {
			type    : "POST",
			async   : true
		},
		colModel: colModel,
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: gridWidth,
		height: gridHeight,
		loadonce: true, // this is just for the demo
	   	autowidth: true,
		shrinkToFit: true,
		rownumbers : false, // 행번호 표시여부
		rownumWidth : 30, // 행번호 열의 너비	
		rowNum:25,
		rowList:[25,50,100],			
		pager: "#filtersGridPager",
		//jqgrid의 특성상 - rowpos의 이벤트는 onSelectCell, beforeEditCell 다 해주어야 함
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	  	
	  	},
	  	ondblClickRow: function(nRowid, icol, cellcontent, e){
	  	},
	  	onCellSelect: function(rowid,icol,cellcontent,e) {
        },
		loadComplete: function(data) {
			var ids = $("#filtersGrid").getDataIDs() ;
            $.each(ids, function(idx, rowId) {
                rowData = $("#filtersGrid").getRowData(rowId, true) ;
                $("#filtersGrid").setCell(rowId, 'type2', rowData.type);
            });
			 
			$(".gridSubSelBtn").on("click", function(e) {
		  		e.stopPropagation();
				$("#filtersGrid").setSelection(event.target.parentElement.parentElement.id);
		
				var offset = $(this).parent().offset();
				$("#taskGroupWindow").css("left", (offset.left - $("#taskGroupWindow").width()) + 55 + "px");
				$("#taskGroupWindow").css("top", offset.top + $(this).height() + "px");
		
				var bottomLimit = $(".left_box2").offset().top + $(".left_box2").height();
				var taskBottom = Number($("#taskGroupWindow").css("top").replace("px","")) + $("#taskGroupWindow").height();
		
				if (taskBottom > bottomLimit) { 
					$("#taskGroupWindow").css("top", Number($("#taskGroupWindow").css("top").replace("px","")) - (taskBottom - bottomLimit) + "px");
				}
				$("#taskGroupWindow").show();
			});
			
			automaticCompletion(null);
		},
		gridComplete : function() {
		}
	});
	
	// 엔터 입력시 발생하는 이벤트
	// 대소문자 구분함
	$('#host_name, #path').keyup(function(e) {
		if (e.keyCode == 13) {
		    $("#btnSearch").click();
	    }        
	});
	
	// 대상 선택 팝업
	$('#btnGroupSerachPopup').on('click', function(){
		var popStatus = $("#popUpStatus").val();
		openPop(popStatus);
	});
	
	// 검색 조회
	$("#btnSearch").click(function(e){
		
		if( $("select[name='selectList']").val() == null){
			alert("범위가 선택 되지 않았습니다.");
			return;
		}
		
		var postData = {
			status : $("select[name='selectList']").val(),
			target_status : $("select[name='selectTargetList']").val(),
			host_name : $("#host_name").val(),
			user_name : $("#user_name").val(),
			path : $("#path").val(),
			fromDate : $("#fromDate").val(),
			toDate : $("#toDate").val()
		};

		$("#filtersGrid").setGridParam({
			url:"<%=request.getContextPath()%>/excepter/glovalFilterDetail", 
			postData : postData, 
			datatype:"json" 
		}).trigger("reloadGrid");
	})
	 
	// 등록 팝업
	$("#filtersInsert").click(function(e){
		$("#popUpName").html("예외 등록");
		$("#popUpStatus").val("insert");
		$("#inputCreateUser").val("${memberInfo.USER_NO}");
		
		$(":radio[name='selectFilters'][value='exclude_expression']").prop('checked', true);
		// 예외 수정 화면과 변경(저장 버튼)
		$("#btnUpdateFilterPopupSave").hide();
		$("#btnfiltersSave").show();
		$("#regExp").css('display', 'none');
		
		// 예외 수정 화면과 변경(서버)
		$("#updateApNoName").hide();
		$("#updateApNoList").show();
		
		$("#filtersTable").show();
		$("#filtersTableName").hide();
		
		$("#filtersPopup").show();
	})
	
	// 일괄 등록 팝업
	/* $("#filtersAllInsert").click(function(e){
		$("#filtersAllPopup").show();
	}) */
	
	setSelectDate();
	
	var postData = {
		fromDate : $("#fromDate").val(),
		toDate : $("#toDate").val()
	}
	$("#filtersGrid").setGridParam({
		url:"<%=request.getContextPath()%>/excepter/glovalFilterDetail", 
		postData : postData, 
		datatype:"json" 
	}).trigger("reloadGrid");
	
	
	$("#selectTargetList").change(function() {
		var target_stauts = $("select[name='selectTargetList']").val();
		if(target_stauts == "Target") {
			$("#host_name").removeAttr("disabled");
		}else {
			$("#host_name").attr("disabled", "disabled");
		}
	});
	
	$("input:radio[name='selectFilters']").change(function() {
		var status = $("input:radio[name='selectFilters']:checked").val();
		if(status == "ignore_expression") {
			$("#regExp").css("display", "");
		}else {
			$("#regExp").css("display", "none");
		}
	});
	
	$("#btnDownloadExel").click(function(){
		btnDownloadExel();
	});
});

function btnDownloadExel()
{
// 	resetFomatter = "downloadClick";
	
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
	
	$("#filtersGrid").jqGrid("exportToCsv",{
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
        fileName : "예외_관리_" + today + ".csv",
        mimetype : "text/csv; charset=utf-8",
        returnAsString : false
    });
	$("#filtersGrid").jqGrid("showCol",["CHK"]);
} 

function openPop(info){
	var apno = "";
	
	if(info == "insert"){
		apno = $("select[name='selectInsertList']").val();
	}else if(info == "update"){
		apno = $("#updateApNo").val();
	}
	
	if(apno == "" || apno == null){
		alert("범위가 선택 되지 않았습니다.");
		return;
	}
	
	var pop_url = "${getContextPath}/popup/filterUserList";
	
	var id = "filterUser"
	var winWidth = 700;
	var winHeight = 565;
	var popupOption= "width="+winWidth+", height="+winHeight + ", left=0, top=0, scrollbars=no, resizable=no, location=no"; 	
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
	
	var ap_no = document.createElement('input');
	ap_no.setAttribute('type','hidden');
	ap_no.setAttribute('name','apno');
	ap_no.setAttribute('id','apno');
	ap_no.setAttribute('value',apno);
	
	newForm.appendChild(data);
	newForm.appendChild(ap_no);
	
	document.body.appendChild(newForm);
	newForm.submit();
	
	document.body.removeChild(newForm);
}


// 예외 등록
$("#btnfiltersSave").on("click", function(e) {
    fnSaveExceptionRegist();
});

// 예외 수정
$("#btnUpdateFilterPopupSave").on("click", function(e) {
	fnUpdateExceptionRegist();
});

$("#btnfiltersCancel").click(function(){
	$("#filtersPopup").hide();
	clearText();
});

// 조회 버튼 클릭
$("#globalFilterShow").click(function(){
	
	var row = $("#filtersGrid").getGridParam("selrow");
	
	var filter_id = $("#filtersGrid").getCell(row, "FILTER_ID");
	var host_name = $("#filtersGrid").getCell(row, "NAME");
	var expression = $("#filtersGrid").getCell(row, "PATH");
	var ap_no = $("#filtersGrid").getCell(row, "AP_NO");
	var network = $("#filtersGrid").getCell(row, "NETWORK");
	var type = $("#filtersGrid").getCell(row, "TYPE");
	var user_name = $("#filtersGrid").getCell(row, "USER_NAME");
	var comment = $("#filtersGrid").getCell(row, "COMMENT");
	var regExpFlag = $("#filtersGrid").getCell(row, "REGEXPFLAG");
	
    /* if(type == "exclude_expression"){
    	type = "경로 예외";
	}else if(type == "ignore_expression"){
		type = "패턴 예외";
	} */
    
	if(type == "패턴 예외") {
		$("#filter_regExp_tr").css('display', '');
	}else {
		$("#filter_regExp_tr").css('display', 'none');
	}
	
    if(regExpFlag == "Y"){
    	$("#btnFilterRegExp").prop("checked", true);
    }else {
    	$("#btnFilterRegExp").prop("checked", false);
    }
    
	$("#filter_range").html(network);
	$("#filter_type").html(type); 
	$("#filter_id").html(host_name);
	var path = expression.replaceAll(", ", "\n");
	$("#filter_path_list").val(path);
	$("#filter_comment").val(comment);
	
	$("#popUpStatus").val('insert');
	$("#glovalFilterDetailPop").show();
});

// 수정 버튼 클릭
$("#globalFilterUpdate").click(function(){
	
	var row = $("#filtersGrid").getGridParam("selrow");
	
	var filter_id = $("#filtersGrid").getCell(row, "FILTER_ID");
	var target_id = $("#filtersGrid").getCell(row, "TARGET_ID");
	var host_name = $("#filtersGrid").getCell(row, "NAME");
	var expression = $("#filtersGrid").getCell(row, "PATH");
	var ap_no = $("#filtersGrid").getCell(row, "AP_NO");
	var network = $("#filtersGrid").getCell(row, "NETWORK");
	var type = $("#filtersGrid").getCell(row, "TYPE");
	var user_name = $("#filtersGrid").getCell(row, "USER_NAME");
	var comment = $("#filtersGrid").getCell(row, "COMMENT");
	var regExpFlag = $("#filtersGrid").getCell(row, "REGEXPFLAG");
	
    /* if(type == "exclude_expression"){
    	type = "경로 예외";
	}else if(type == "ignore_expression"){
		type = "패턴 예외";
	} */
	
	$("#popUpName").html("예외 수정");
	
	if(type == "패턴 예외") {
		$("#regExp").css('display', '');
	}else {
		$("#regExp").css('display', 'none');
	}
	
    if(regExpFlag == "Y"){
    	$("#btnRegExp").prop("checked", true);
    }else {
    	$("#btnRegExp").prop("checked", false);
    }
	
	// $("#selectInsertList").val(status).prop("selected", true);
	// 수정은 서버 변경 불가
	$("#updateApNo").val(ap_no);
	$("#updateApNoList").hide(); 
	$("#filtersTable").hide();
	
	$("#updateApNoName").html(network);
	$("#updateApNoName").show();
	 
	$("#filtersTableName").html(type);
	$("#filtersTableName").show();
	
	$("#selectedExcepServer").html(host_name);
	$("#insert_target_id").val(target_id);
	$("#update_filter_id").val(filter_id);
	
	$("#btnfiltersSave").hide();
	$("#btnUpdateFilterPopupSave").show();
	
	var path = expression.replaceAll(", ", "\n");
	$("#inputExcepPath").val(path);
	$("#inputComment").val(comment);
	
	$(":radio[name='selectFilters'][value='"+type+"']").prop('checked', true);
	
	$("#popUpStatus").val('update');
	$("#filtersPopup").show();
});

$("#btnCancleUpdateFilterPopup").click(function(){
	$("#glovalFilterUpdatePop").hide();
});
$("#btnUpdateFilterPopup").click(function(){
	$("#glovalFilterUpdatePop").hide();
});


$("#btnCancleDetailFilterPopup").click(function(){
	$("#glovalFilterDetailPop").hide();
});
$("#btnDetailFilterPopup").click(function(){
	$("#glovalFilterDetailPop").hide();
});

$("#closeFilterPop").click(function(){
	$("#taskGroupWindow").hide();
});

$("#btnCanclefiltersPopup").click(function(){
	$("#filtersPopup").hide();
	clearText();
});

$("#btnCancleDetailfiltersPopup").click(function(){
	$("#filterDetailPopup").hide();
});

$("#btnCancleExcelPopup").click(function(){
	setNewSetting2();
});

$("#glbalFilterDelete").click(function(){
	
	var result = confirm("해당 예외를 삭제하시겠습니까?");
	
	var row = $("#filtersGrid").getGridParam("selrow");
	var filter_id = $("#filtersGrid").getCell(row, "FILTER_ID");
	var ap_no = $("#filtersGrid").getCell(row, "AP_NO");
	var network = $("#filtersGrid").getCell(row, "NETWORK");
	
	if(result){
		var oPostDt = {};
	    oPostDt["filter_id"] = filter_id;
	    oPostDt["ap_no"] = ap_no;
		
		$.ajax({
			type: "POST",
			url: "${getContextPath}/excepter/deleteGlovalFilterDetail",
			async : false,
			data : oPostDt,
			datatype: "json",
			success: function (result) {
				if(result.resultCode == 0){
			    	alert("경로 예외 삭제가 완료되었습니다.");
				}else{
					alert("경로 예외 삭제가 실패되었습니다.");
				} 
				
				$("#filtersPopup").hide();
				clearText();
				var postData = {
					fromDate : $("#fromDate").val(),
					toDate : $("#toDate").val()
				}
				$("#filtersGrid").setGridParam({
					url:"<%=request.getContextPath()%>/excepter/glovalFilterDetail", 
					postData : postData, 
					datatype:"json" 
				}).trigger("reloadGrid");
			}
		});
	}
	
});

function fnSaveExceptionRegist() 
{
	var pathList = "";
	var path = $("#inputExcepPath").val();
	if(!isNull(path)) path = path.split("\n");
	
	if($("#selectedExcepServer").html() == null || $("#selectedExcepServer").html() == "" ){
		alert("적용 대상을 선택하세요.");
		return;
	}; 
	
	if( $("select[name='selectInsertList']").val() == null){
		alert("범위가 선택 되지 않았습니다.");
		return;
	}
	
	if( $("input:radio[name=\"selectFilters\"]:checked").val() == null){
		alert("구분이 선택되지 않았습니다.");
		return;
	}
	
	// 입력된 예외경로 없을시 진행 불가
	if (isNull(path)) {
		alert("예외처리 경로를 입력하세요");
		$("#inputExcepPath").focus();
		return false;
	}
	
	for(i=0 ; i < path.length ; i++){
		if(i == path.length -1){
			pathList += path[i];
		}else{
			pathList += path[i] + "|";
		}
	}
	
	var result = confirm("해당 예외를 등록하시겠습니까?");
	
	if(result){
		var oPostDt = {};
	    oPostDt["status"] = $("select[name='selectInsertList']").val();
	    oPostDt["path_ex"] = pathList;
	    oPostDt["target_id"] = $("#insert_target_id").val();
	    oPostDt["type"] = $("input:radio[name=\"selectFilters\"]:checked").val();
	    oPostDt["comment"] = $("#inputComment").val();
	    oPostDt["user_no"] = $("#inputCreateUser").val();
	    
	    var regExp = $('input[name="btnRegExp"]:checked').val();
	    if(regExp != "Y") {
	    	regExp = "N"
	    }
	    
	    oPostDt["regExp"] = regExp;
	    
	 	$.ajax({
			type: "POST",
			url: "${getContextPath}/excepter/insertGlovalFilterDetail",
			async : false,
			data : oPostDt,
			datatype: "json",
			success: function (result) {
				if(result.resultCode == 0){
			    	alert("경로 예외가 완료되었습니다.");
				}else{
					alert("경로 예외가 실패되었습니다.");
				} 
				$("#filtersPopup").hide();
				clearText();
				
				var postData = {
					fromDate : $("#fromDate").val(),
					toDate : $("#toDate").val()
				}
				$("#filtersGrid").setGridParam({
					url:"<%=request.getContextPath()%>/excepter/glovalFilterDetail", 
					postData : postData, 
					datatype:"json" 
				}).trigger("reloadGrid");
			}
		});  
	}
	
}

$("#clickimportBtn").click(function(){
	$("#importExcel").click();
});

$("#importExcel").change(function(){
	var checkFileNm = $("#importExcel").val();
	var filelength = checkFileNm.lastIndexOf('\\');
	var fileNm = checkFileNm.substring(filelength+1, checkFileNm.length);
	var resulList = [];
	var CNT = 0;
	let input = event.target;
    let reader = new FileReader();
    reader.onload = function () {
        let data = reader.result;
        let workBook = XLSX.read(data, { type: 'binary' });
        /* workBook.SheetNames.forEach(function (sheetName) { */
            let rows = XLSX.utils.sheet_to_json(workBook.Sheets["예외 등록"]);
            var details = "";
            var expUrl = /^(1|2)?\d?\d([.](1|2)?\d?\d){3}$/;
            var excel_length = 0;
            if(rows < 1){
            	alert("올바른 시트가 존재하지 않습니다. 확인 후 다시 시도해 주세요.");
            	return;
            }
      
            
            var total_data = rows.length;
	    	
	    	details += "<tr height=\"45px;\" >";
	    	details +=  "<th></th>";
	    	details +=  "<th>범위</th>";
	    	details +=	"<th>대상</th>";
	    	details +=	"<th>경로</th>";
	    	details += "</tr>";
	    	
	    	if(
	    			rows[0].hasOwnProperty('범위')	
    			 &&	rows[0].hasOwnProperty('대상')	
	    		 &&	rows[0].hasOwnProperty('경로') 		
	    	){
	    		$.each(rows, function(index, item) {
	    			++CNT
	    			if(CNT > 1 && CNT < 52) {
	    				var ap_nm = item.범위;
						var target_id = item.대상;
						var path = item.경로;
						var num = CNT-1;
						
						details += "<tr style=\"height: 45px;\">";
						details += "	<td style=\"text-align: center; padding-left: 0;\">"+num+"</td>";
						details += "	<td style=\"text-align: center; padding-left: 0;\">"+ap_nm+"</td>";
						details += "	<td style=\"text-align: center; padding-left: 0;\">"+target_id+"</td>";
						details += "	<td style=\"text-align: left; padding-left: 0;\">"+path.replaceAll('|', ', ')+"</td>";
						details += "</tr>";
						
						var type; 
						
						resulList.push({"ap_nm" : ap_nm, "target_id" : target_id, "path" : path})
	    			}else if(CNT == 52){
	    				alert("예외 등록은 최대 50개까지 가능합니다.\n초과된 예외는 등록되지 않습니다.");
	    			}
				});
	    		
	    		
		    	var btnCss ="<button type=\"button\" id=\"btnNewPopupExcelSave\" style=\"margin-right: 5px\" >저장</button>";
					btnCss +="<button type=\"button\" id=\"btnNewPopupExcelCencel\" >취소</button>";
				$("#filterBtn").html(btnCss)
				
				$("#btnNewPopupExcelCencel").click(function(e){
					$("#importExcel").val("");
					$("#importExcelNm").val("");
					
					var details = "";
					$("#filterBtn").html(details); 
					$("#import_filters_excel").html(details);
					$("#filtersAllPopup").hide();
	        	});
				
				$('#btnNewPopupExcelSave').on('click', function(){
					
	            	var msg = confirm("예외를 등록 하시겠습니까?");
	            	
	            	if(msg){
	            		$.ajax({
	        				type: "POST",
	        				url: "/search/insertGlobalFilter",
	        				//async : false,
	        				data : {
	        					"resulList": JSON.stringify(resulList)
	        				},
	        			    success: function (resultMap) {
	        			    	alert(resultMap.resultMessage);
	        			    	
	        			    	if(resultMap.ressultCode == 0){
	        			    		var postData = null;
		        		        	$("#netGrid").setGridParam({
		        			    		url:"<%=request.getContextPath()%>/search/netList",
		        			    		postData : postData, 
		        			    		datatype:"json" 
		        		    		}).trigger("reloadGrid");
	        			    	}else{
	        			    		return;
	        			    	}
	        			    	
	        			    
	        			    },
	        			    error: function (request, status, error) {
	        			    	alert("서버 저장이 실패하였습니다.");
	        			        console.log("ERROR : ", error);
	        			        treeArr = [];
	        	    	    	groupArr = [];
	        			    }
	        			});
	            	}
            	});
	    		
	    	}else {
	    		alert("올바른 형식의 엑셀이 아닙니다. 확인 후 다시 시도해 주세요.");
	    		return;
	    	}
	    	
	    	 $("#import_filters_excel").html(details);
	         $("#importExcelNm").val(fileNm);
	    	
       /*  }) */
    };
    reader.readAsBinaryString(input.files[0]);
});

function fnUpdateExceptionRegist() 
{
	var pathList = "";
	var path = $("#inputExcepPath").val();
	if(!isNull(path)) path = path.split("\n");
	
	if($("#selectedExcepServer").html() == null || $("#selectedExcepServer").html() == "" ){
		alert("적용 대상을 선택하세요.");
		return;
	}else if($("#selectedExcepServer").html() == "삭제된 대상"){
		alert("대상을 확인 할 수 없습니다.");
		return;
	};

	// 입력된 예외경로 없을시 진행 불가
	if (isNull(path)) {
		alert("예외처리 경로를 입력하세요");
		$("#inputExcepPath").focus();
		return false;
	}
	
	for(i=0 ; i < path.length ; i++){
		if(i == path.length -1){
			pathList += path[i];
		}else{
			pathList += path[i] + "|";
		}
	}
	
	var result = confirm("해당 예외를 수정하시겠습니까?");
	
	if(result) {
		var oPostDt = {};
	    oPostDt["filter_id"] = $("#update_filter_id").val();
	    oPostDt["path_ex"] = pathList;
	    oPostDt["target_id"] = $("#insert_target_id").val();
	    
	    var type = "";
	    
	    if($("#filtersTableName").html() == "경로 예외") {
	    	type = "exclude_expression";
	    }else if($("#filtersTableName").html() == "패턴 예외") {
	    	type = "ignore_expression";
	    }
	    
	    oPostDt["type"] = type;
	    oPostDt["status"] = $("#updateApNo").val();
	    oPostDt["comment"] = $("#inputComment").val();
	    
	    var regExp = $('input[name="btnRegExp"]:checked').val();
	    if(regExp != "Y") {
	    	regExp = "N"
	    }
	    
	    oPostDt["regExp"] = regExp;
	    
	 	$.ajax({
			type: "POST",
			url: "${getContextPath}/excepter/updateGlovalFilterDetail",
			async : false,
			data : oPostDt,
			datatype: "json",
			success: function (result) {
				if(result.resultCode == 0){
			    	alert("경로 예외가 완료되었습니다.");
				}else{
					alert("경로 예외가 실패되었습니다.");
				} 
				$("#filtersPopup").hide();
				
				var postData = {
					fromDate : $("#fromDate").val(),
					toDate : $("#toDate").val()
				};
				$("#filtersGrid").setGridParam({
					url:"<%=request.getContextPath()%>/excepter/glovalFilterDetail", 
					postData : postData, 
					datatype:"json" 
				}).trigger("reloadGrid");
			}
		});  
	}
	
}


function fnLocationAdd(element, e) 
{
    if (e.keyCode != 13) return;
    if (isNull($(element).val())) return;;

    var excepPath = $(element).val();
    var sTag = "";

    sTag += "<tr style='border:none;'>";
    sTag += "    <th style='padding:2px; background: transparent; overflow:hidden; text-align:left;'>" + excepPath + "</th>";
    sTag += "    <td style='padding:0px; background: transparent; height:23px; width:30px;'>";
    sTag += "        <input type='button' value='X' name='button' style='color:#ba1919; border:0 none; background-color:transparent; cursor:pointer; float:center; height:23px;' onclick='fnLocationRemove(this);'>";
    sTag += "    </td>";
    sTag += "</tr>";

    $("#excepPath").append(sTag);
    $(element).val("");
}

function fnLocationRemove(element) 
{
    var excepPathRmv = $(element).parent("td").parent("tr")[0];
    $(excepPathRmv).remove();
}

function clearText(){
	
	// 범위
	$("#selectInsertList").val(0).prop("selected", true);
	
	// 대상
	$("#selectedExcepServer").html("");
	$("#insert_target_id").val("");
	
	// 경로
	$("#inputExcepPath").val("");
	// 비고
	$("#inputComment").val("");
	
};

var formatType = function(cellvalue, options, rowObject) {
	var status = cellvalue;

	if(cellvalue == "GROUP"){
		status = '그룹';
	}else if(cellvalue == "TARGET"){
		status = '서버';
	}else if(status == "ALL"){
		status = '전체';
	}
	
	return status; 
};

var createView = function(cellvalue, options, rowObject) {
	result = "<button type='button' style='padding-top: 4px; padding-bottom:4px;' class='gridSubSelBtn' name='gridSubSelBtn'>선택</button>";
	return result; 
};

var formatName = function(cellvalue, options, rowObject) {
	
	if(cellvalue == "All Targets"){
		cellvalue = "전체 대상";
	}else if(cellvalue == "Deleted Target"){
		cellvalue = "삭제된 대상";
	}else {
		cellvalue = cellvalue;
	}
	
	return cellvalue;
};

var filterType = function(cellvalue, options, rowObject) {
	var typeValue = "";
	
	if(cellvalue == "exclude_expression"){
		typeValue = "경로 예외";
	}else if(cellvalue == "ignore_expression"){
		typeValue = "패턴 예외";
	}else{
		typeValue = cellvalue;
	}
	
	return typeValue;
};

var formatPath = function(cellvalue, options, rowObject) {
	var forresult = null;
	if (cellvalue != null){
		cellvalue.replaceAll("|", ", ");
	}else{
		cellvalue = forresult;
	}
	return cellvalue
};

function setNewSetting2() {
	
	$("#importExcel").val("");
	$("#importExcelNm").val("");
	
	var details = "";
	$("#filterBtn").html(details);
	$("#import_filters_excel").html(details);
	$("#filtersAllPopup").hide();
	
};

function setSelectDate() 
{
    $("#fromDate").datepicker({
        changeYear : true,
        changeMonth : true,
        dateFormat: 'yy-mm-dd'
    });

    $("#toDate").datepicker({
        changeYear : true,
        changeMonth : true,
        dateFormat: 'yy-mm-dd'
    });

    var oToday = new Date();
    $("#toDate").val(getFormatDate(oToday));

    var oFromDate = new Date(oToday.setDate(oToday.getDate() - 30));
    $("#fromDate").val(getFormatDate(oFromDate));
}

function getFormatDate(oDate)
{
    var nYear = oDate.getFullYear();           // yyyy 
    var nMonth = (1 + oDate.getMonth());       // M 
    nMonth = ('0' + nMonth).slice(-2);         // month 두자리로 저장 

    var nDay = oDate.getDate();                // d 
    nDay = ('0' + nDay).slice(-2);             // day 두자리로 저장

    return nYear + '-' + nMonth + '-' + nDay;
}

$(function() { 
	$.datepicker.setDefaults({ 
		closeText: "확인", 
		currentText: "오늘", 
		prevText: '이전 달', 
		nextText: '다음 달', 
		monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'], 
		monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'], 
		dayNames: ['일', '월', '화', '수', '목', '금', '토'], 
		dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'], 
		dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'], 
		yearRange: 'c-5:c+5'
	}); 
});

</script>
</body>

</html>