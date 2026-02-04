<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%@ include file="../../include/header.jsp"%>
<style>
h4 {
	margin : 5px 0;
	font-size: 0.8vw;
}
#remediationGrid * *{
	overflow: hidden;
	white-space: nowrap; 
	text-overflow: ellipsis; 
}
@media screen and (-ms-high-contrast: active), (-ms-high-contrast: none) {
	.list_sch{
		top: 41px !important;
	}
}
.content-box {
	padding: 10px !important;
}
</style>
	<section id="section">
		<div class="container"> 
			<h3>조치 이력</h3>
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
						style="height: auto; min-height: 665px; overflow: hidden; width: 100%; margin-top: 10px;">
						<table id="remediationGrid"></table>
						<div id="remediationGridPager"></div>
					</div>
				</div>
			</div>
		</div>
	</section>

	<%@ include file="../../include/footer.jsp"%>

<script type="text/javascript"> 
var colModel = [];
GridName = "#remediationGrid";

$(document).ready(function () {

	var gridWidth = $("#remediationGrid").parent().width();
	var gridHeight = 561;
	 
	// 그리드 컬럼 정의
	colModel.push({label: "DETAIL_ID",  		index: "DETAIL_ID", 		name: "DETAIL_ID",			width: 0, 		align: "center", 	hidden:true});
	colModel.push({label: "조치 유형",  			index: "ACTION_KR", 		name: "ACTION_KR",			width: 100, 	align: "center"});
	colModel.push({label: "대상",  				index: "HOST_NAME", 		name: "HOST_NAME",			width: 150, 	align: "center"});
	colModel.push({label: "원본 경로",  			index: "ORIGINAL_PATH", 	name: "ORIGINAL_PATH",		width: 300, 	align: "left"});
	colModel.push({label: "변경 경로",  			index: "NEW_PATH", 			name: "NEW_PATH",			width: 200, 	align: "left"});
	colModel.push({label: "상태",  				index: "STATUS", 			name: "STATUS",				width: 80, 		align: "center", 	formatter:formatStatus});
	colModel.push({label: "오류 메시지",  		index: "ERROR_MESSAGE", 	name: "ERROR_MESSAGE",		width: 200, 	align: "left"});
	colModel.push({label: "처리자",  				index: "USER_NAME", 		name: "USER_NAME",			width: 100, 	align: "center"});
	colModel.push({label: "요청 일시",  			index: "REQUEST_DATE", 		name: "REQUEST_DATE",		width: 150, 	align: "center", 	type:3});
	colModel.push({label: "처리 일시",  			index: "PROCESS_DATE", 		name: "PROCESS_DATE",		width: 150, 	align: "center", 	type:3});
	colModel.push({label: "TARGET_ID",  		index: "TARGET_ID", 		name: "TARGET_ID",			width: 0, 		align: "center", 	hidden:true});
	colModel.push({label: "HASH_ID",  			index: "HASH_ID", 			name: "HASH_ID",			width: 0, 		align: "center", 	hidden:true});
	colModel.push({label: "USER_NO",  			index: "USER_NO", 			name: "USER_NO",			width: 0, 		align: "center", 	hidden:true});
	colModel.push({label: "ACTION",  			index: "ACTION", 			name: "ACTION",				width: 0, 		align: "center", 	hidden:true});
	
	GridSearchTypeChk();
	searchListAppend(); 
	
	$("#remediationGrid").jqGrid({
		url: "<%=request.getContextPath()%>/remediation/remediationHistory",
		datatype: "json",
	   	mtype : "POST",
	   	ajaxGridOptions : {
			type    : "POST",
			async   : true
		},
		colModel: colModel,
		viewrecords: true,
		width: gridWidth,
		height: gridHeight,
		loadonce: true,
	   	autowidth: true,
		shrinkToFit: true,
		rownumbers : true,
		rownumWidth : 30,
		rowNum:25,
		rowList:[25,50,100],			
		pager: "#remediationGridPager",
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	  	
	  	},
	  	ondblClickRow: function(nRowid, icol, cellcontent, e){
	  	},
	  	onCellSelect: function(rowid,icol,cellcontent,e) {
        },
		loadComplete: function(data) {
			automaticCompletion(null);
		},
		gridComplete : function() {
		}
	});
	
	// 검색
	$("#btnSearch").click(function(e){
		var postData = {
			action : $("#select_action").val(),
			target_name : $("#target_name").val(),
			user_name : $("#user_name").val(),
			status : $("#select_status").val(),
			fromDate : $("#fromDate").val(),
			toDate : $("#toDate").val()
		};

		$("#remediationGrid").setGridParam({
			url:"<%=request.getContextPath()%>/remediation/remediationHistory", 
			postData : postData, 
			datatype:"json" 
		}).trigger("reloadGrid");
	});
	
	// 다운로드
	$("#btnDownloadExel").click(function(){
		btnDownloadExel();
	});
	
	setSelectDate();
	
	// 초기 로드
	var postData = {
		fromDate : $("#fromDate").val(),
		toDate : $("#toDate").val()
	};
	$("#remediationGrid").setGridParam({
		url:"<%=request.getContextPath()%>/remediation/remediationHistory", 
		postData : postData, 
		datatype:"json" 
	}).trigger("reloadGrid");
});

function btnDownloadExel() {
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1;
	var yyyy = today.getFullYear();
	if(dd<10) {
	    dd='0'+dd;
	} 
	if(mm<10) {
	    mm='0'+mm;
	} 
	today = yyyy + "" + mm + dd;  
	
	$("#remediationGrid").jqGrid("exportToCsv",{
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
        fileName : "조치_이력_" + today + ".csv",
        mimetype : "text/csv; charset=utf-8",
        returnAsString : false
    });
}

var formatStatus = function(cellvalue, options, rowObject) {
	switch(cellvalue) {
		case 'SUCCESS': 
			return '<span style="color: #28a745;">성공</span>';
		case 'FAILED': 
			return '<span style="color: #dc3545;">실패</span>';
		case 'PENDING': 
			return '<span style="color: #ffc107;">대기</span>';
		default: 
			return cellvalue;
	}
};

function setSelectDate() {
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

function getFormatDate(oDate) {
    var nYear = oDate.getFullYear();
    var nMonth = (1 + oDate.getMonth());
    nMonth = ('0' + nMonth).slice(-2);
    var nDay = oDate.getDate();
    nDay = ('0' + nDay).slice(-2);
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