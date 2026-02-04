<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%@ include file="../../include/header.jsp"%>


		<!-- 업무타이틀(location)
		<div class="banner">
			<div class="container">
				<h2 class="ir">업무명 및 현재위치</h2>
				<div class="title_area">
					<h3>타겟 관리</h3>
					<p class="location">사용자 관리 > 접속로그관리</p>
				</div>
			</div>
		</div>
		<!-- 업무타이틀(location)-->

		<!-- section -->
		<section id="section">
		<div class="container">
			<h3>로그관리</h3>
			<div class="content magin_t25">
				<div class="grid_top">
					<div class="searchBox" style="float: left;">
						<table class="user_info narrowTable" id="navGridSearchDiv"  style="width: 712px;">
							<tbody>
								<tr id="searchTextBox">
									<th style="text-align: center; border-radius: 0.25rem; width: 92px; " class="searchName">
										<select id="searchFilter"></select>
									</th>
	             			     	<td id="defaultSearchTextBox">
	                					<input type="text" style="width: 205px; padding-left: 5px;" size="10" class="searchContent" id="searchContent"  placeholder="검색어를 입력하세요.">	
	                			 	</td> 
	                			 	<td id="searchDayBox"> </td>
			                		<td> 
			                    		 <input type="button" name="button" class="navGridSearchBtn" style="margin-top: 5px;">
			                    	</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div id="searchFilterBox" class ="searchFilterBox" style="display:inline-block;width:849px;position:absolute;"></div>
					<div class="list_sch" style="height: 39px; margin-top: 10px; float: right;" > 
						<div id="searchConditionsContainer" style="float: left;" ></div>
	                    <div style="float: right; margin-left: 3px; margin-bottom: 7px;">
	                    	<button type="button" name="button" class="btn_down" id="btnDownloadExel">다운로드</button>
	                    </div>
	                    <div style="float: right;">
							<h5 style="width: 24px; height: 24px; margin-top: 2px; background-size : 100%; background-image: url('${pageContext.request.contextPath}/resources/assets/images/pi_list.png');" id="tableShow">&nbsp;</h5>
						</div>
	                </div>
				</div>
				<div class="left_box2" style="height: auto; min-height: 665px; overflow: hidden; width:59vw; margin-top: 10px;">
					<table id="userGrid"></table> 
					<div id="userGridPager"></div>
				</div>
			</div>
		</div>
	</section>
		 
	<%@ include file="../../include/footer.jsp"%>
	
<!-- 팝업창 - 로그 상세보기 -->
<div id="DatePopup" class="popup_layer" style="display:none">
	<div class="popup_box" style="height: 200px; width: 840px; top: 55%; left:46%; padding: 10px; background: #f9f9f9;">
	<img class="CancleImg" id="btnCancleDatePopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 style="color: #222; padding: 0; box-shadow: none;">로그 상세보기</h1>
		</div>
		<div class="popup_content">
			<div class="content-box" id="div_update_user" style="height: 365px; background: #fff; border: 1px solid #c8ced3;">
				<table class="popup_tbl">
					<colgroup>
						<col width="20%">
						<col width="*">
					</colgroup>
					<tbody>
						 <tr>
							<th>구분</th>
							<td>
								<label id="dataStatus"></label>
							</td>
						</tr>
						 <tr>
							<th>관련 내용</th>
							<td>
								<div id="dataCon" style="overflow-y: auto; height: 290px;"></div>
							</td>
						</tr>
						
					</tbody>
				</table>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area" style="padding: 10px 0; margin: 0;">
				<button type="button" id="btnDateCancel">닫기</button>
			</div>
		</div>
	</div>
</div>
	
<script type="text/javascript">
var colModel = []; 
GridName = "#userGrid";
var oGrid = $("#userGrid");
requestUrl = "${getContextPath}/user/pi_userlog_list" ; 
var resetFomatter = null;
function fn_search () {
	
    if($("#fromDate").val() > $("#toDate").val()){
        alert("입력한 끝 날짜가 시작 날짜 보다 빠릅니다.");
        return;
    }
	
	var postData = {
		userNo : $("#userNo").val(),
		userName : $("#userName").val(),
		userIP : $("#userIP").val(),
		fromDate : $("#fromDate").val(),
		toDate : $("#toDate").val(),
		logFlag : $('#selectStatus').val(),
		gradeStatus : $('#selectGradeStatus').val()
	};
	
	
}

$(document).ready(function () {
	
	var gridWidth = $("#userGrid").parent().width();
	var gridHeight = 590;
	 
	colModel.push({ label: '구분', 		index: 'USER_NAME', 		name: 'USER_NAME', 			width: 200,	align: 'center'});
	colModel.push({ label: '사용자 ID', 	index: 'USER_ID', 			name: 'USER_ID', 			width: 250,	align: 'center'});
	colModel.push({ label: '사용자명', 	index: 'USER_NAME',			name: 'USER_NAME',			width: 250, align: 'center'});
	colModel.push({ label: 'IP', 		index: 'USER_IP',			name: 'USER_IP',			width: 250, align: 'center'});
	colModel.push({ label: '사용화면', 	index: 'MENU_NAME',			name: 'MENU_NAME',			width: 200, align: 'left', hidden: true});
	colModel.push({ label: '사용화면_kr', 	index: 'KR_MENU_NAME',		name: 'KR_MENU_NAME',		width: 200, align: 'left', hidden: true});
	colModel.push({ label: '작업내용', 	index: 'JOB_INFO',			name: 'JOB_INFO',			width: 250, align: 'center', formatter:detailHtmlTag});
	colModel.push({ label: '일자', 		index: 'REGDATE',			name: 'REGDATE', 			width: 200, align: 'center', type:"3_0", searchType : false});
	colModel.push({ label: '상세 내용', 	index: 'CONTEXT_COPY',		name: 'CONTEXT_COPY',		width: 250, align: 'center', formatter:btnHtml, exportcol:false, searchType : false});
	colModel.push({ label: 'CONTEXT', 	index: 'CONTEXT',			name: 'CONTEXT',			width: 200, align: 'left', hidden: true});
	
	GridSearchTypeChk();
	searchListAppend();
	setSelectDate();  
	
	$("#userGrid").jqGrid({
		//url: 'data.json',
		datatype: "local",
	   	mtype : "POST",
	   	ajaxGridOptions : {
			type    : "POST",
			async   : true
		},
		colModel: colModel,
		width: gridWidth,
		height: gridHeight,
		loadonce: true, // this is just for the demo
		viewrecords: true, // show the current page, data rang and total records on the toolbar
	   	autowidth: true,
		shrinkToFit: true,
		rownumbers : true, // 행번호 표시여부
		rownumWidth : 50, // 행번호 열의 너비	
		rowNum:30,
	   	rowList:[10,20,30],
	    search: true,			
		pager: "#userGridPager",
		//jqgrid의 특성상 - rowpos의 이벤트는 onSelectCell, beforeEditCell 다 해주어야 함
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	  		
	  	},
		loadComplete: function(data) {
			automaticCompletion(null)
	    },
	    gridComplete : function() {
	    }
	});

	
	$("#btnDownloadExel").click(function(){
		downLoadExcel();
	});
	
	
	
	var oPostDt = {};
	
    oPostDt["dayKey"] = $("#searchDay").val(); 
    oPostDt["fromDate"] = $("#fromDate").val();
    oPostDt["toDate"]   = $("#toDate").val(); 
	
    $("#userGrid").setGridParam({
    	url:"<%=request.getContextPath()%>/user/pi_userlog_list",
    	postData : oPostDt,
    	datatype:"json"
   	}).trigger("reloadGrid");
    
});
var today = new Date();
var dd = today.getDate();
var mm = today.getMonth()+1; //January is 0!
var yyyy = today.getFullYear();

if(dd<10) {
    dd='0'+dd
} 

if(mm<10) {
    mm='0'+mm
} 

today = yyyy + "" + mm + dd;

function downLoadExcel()
{
	resetFomatter = "downloadClick";
    oGrid.jqGrid("exportToCsv",{
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
        fileName : "접속_로그_리스트_" + today + ".csv",
        mimetype : "text/csv; charset=utf-8",
        returnAsString : false
    })
    
    resetFomatter = null;
}

function btnHtml(cellvalue, options, rowObject) {
	var html = "";
	if(resetFomatter != "downloadClick"){
		if(rowObject.CONTEXT != null && rowObject.CONTEXT != ""){
			html ="<button type='button' class='gridSubSelBtn' name='gridSubSelBtn' onclick='btnDetailPop("+options.rowId+");'>조회</button>"; 
		}
	}
	return html;
}

function detailHtmlTag(cellvalue, options, rowObject) {
	var html = "";
	
	if(resetFomatter == "downloadClick"){
		html = cellvalue.replace("&gt;", ">");
	}else{
		html = cellvalue.replace(">", "&gt;");
	}
	
	return html;
}

function btnDetailPop(rowId){
	var selRowData = $("#userGrid").jqGrid("getRowData",rowId);
	var dataCon = "";
	
	var dataConList = selRowData.CONTEXT;
	
		console.log(dataConList);
		dataConList = dataConList.split('[{').join('').split('}]').join('');
		dataConList = dataConList.split('}, {');
		
		console.log(dataConList);
		
		for(var i = 0; dataConList.length > i; i++){ // str 배열만큼 for돌림
			var row = dataConList[i]
			var key = row.split('key=');
		
			console.log(key[1]);
			dataCon += key[1];
			console.log(dataCon);
			
		}
	
	$("#dataCon").html(dataCon);
	$("#dataStatus").html(selRowData.KR_MENU_NAME);
	$("#DatePopup").show();
}; 

$("#btnDateCancel, #btnCancleDatePopup").click(function() {
	$("#DatePopup").hide();
});
</script>

</body>
</html>