<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%@ include file="../../include/header.jsp"%>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/anychart/anychart-base.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/anychart/anychart-exports.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/anychart/anychart-ui.min.js"></script>
<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/resources/assets/css/anychart-font.min.css" />
<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/resources/assets/css/anychart-ui.min.css" />
<style>
	.wrap{
	  height: 180%;
	}
	#chart_top_div {
	  width: 100%;
	  height: 634px;
	}
	#chart_bottom_div {
	  width: 100%;
	  height: 600px;
	}
	@media screen and (-ms-high-contrast: active), (-ms-high-contrast: none) {
		.ui-jqgrid-hdiv{
			width: 854px !important;
		}
 		.container{
 			width: 1900px !important;
 			height: 1880px !important;
 		}
 		.container_header{
 			width: 1900px !important;
 		}
 		.footer_area{
 			width: 1900px !important;
 		}
	}
	
	.inputHidden{
		border:none;
		border-right:0px; 
		border-top:0px; 
		boder-left:0px; 
		boder-bottom:0px;
	}
	
	.ui-th-ltr, .ui-jqgrid .ui-jqgrid-htable th.ui-th-ltr {
	    border-right: 1px solid #ddd;
	    border-bottom: 1px solid #ddd;
	}
</style>

<section>
	<div class="container" style="height: 1825px;">
	<h3>보고서</h3> 
		<div class="content magin_t25" style="height: 1745px; width: 100%;">
			<table class="user_info approvalTh" style="width: 27%; min-width: 240px; margin-bottom: 10px;">
				<tbody>
					<tr>
						<th style="text-align: center; padding: 10px 3px; width: 15%; border-radius: 0.25rem;">검색기간</th>
						<td style="width: 22.3vw;">
							<input type="date" id="fromDate" style="text-align: center;  width:175px;" readonly="readonly" value="${fromDate}" >
							<span style="width: 10%; margin-right: 3px; color: #000">~</span>
							<input type="date" id="toDate" style="text-align: center;  width:175px;" readonly="readonly" value="${toDate}" >
							<input type="button" name="button" class="btn_look_approval" id="btnSearch" style="margin-top: 7px;">
						</td>
					</tr>
				</tbody>
			</table>
			<div class="content_left">
				<h3 style="display:inline; padding: 0;" id="titleTotal"></h3>
				<!-- 183대 서버(약2만개 파일, 49억 건)내 개인정보 정탐 확인 -->
				<div class="left_box" style="height:697px; width:100%; margin-top: 10px;">
					<ul>
						<li style="list-style:none; font-size: 12px;" class="totalStatistics"></li>
					</ul>
					<div id="chart_top_div"></div>
					<p style="font-weight:bold; text-align: center;" id="total_detection"></p>
					<!-- [ 862대(Swing, Swing 외) 서버 검출 결과('20.02월~11월까지) ] -->
				</div>
			</div>
			<div class="grid_top" style="float: right;">
				<h3 style="padding: 0;">TOP5 (전체서비스)</h3>
				<div class="left_box2" style="width: 863px !important; overflow: hidden;">
   					<table id="topNGrid"></table>
   				</div>
			</div>
			<div class="grid_top" style="float: right; margin: 23px 0;">
				<h3 style="padding: 0;">TOP5 (정탐 기준)</h3>
				<div class="left_box2" style="width: 863px !important; overflow: hidden;">
   					<table id="middleNGrid"></table>
   				</div>
			</div>
			<div class="grid_top" style="float: right;">
				<h3 style="padding: 0;">TOP5 (오탐 기준)</h3>
				<div class="left_box2" style="width: 863px !important; overflow: hidden;">
   					<table id="bottomNGrid"></table>
   				</div>
			</div>
			<div class="grid_top" style="width: 100%; float: right; margin: 10px 0;">
				<h3 style="padding: 0;">전체 서버 점검 결과(의심 건수)</h3>
				<div class="left_box2" style="overflow: hidden;">
  					<table id="totalNGrid"></table>
  				</div>
			</div> 
			<div class="chart_box" style="width: 100%; height: 345px; margin: 10px 0; background: #fff;">
				<div id=bar_graph_left style="height: 100%; width: 50%; float: left;"></div>
				<div id="chart_bottom_div" style="height: 100%; width: 50%; float: right;"></div>
			</div>
			<div class="grid_top" style="width: 100%; bottom: 238px; float: right;">
				<h3 style="padding: 0;">이행점검 현황</h3>
				<div class="left_box2" style="height: 235px; overflow: hidden;">
   					<table id="lowestNGrid"></table>
   				</div>
			</div>
		</div>
	</div>
</section>
<%@ include file="../../include/footer.jsp"%>

<!-- 팝업창 - 상세정보 시작 -->
<div id="statisticsPopup" class="ui-widget-content" style="position:absolute; left: 10px; top: 10px;  touch-action: none; width: 500px; z-index: 101; box-shadow: 0 2px 5px #ddd; display:none;">
	<div class="progress_container">
	<img class="CancleImg" id="btnCancleStatisticsPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="progress_top"><h1>상세 보기</h1></div>
		<div class="popup_content" style="height: 100px;">
			<div class="content-box" style="width: 478px; !important; height: 103px; background: #fff; border: 1px solid #c8ced3; border-bottom: none; padding: 5px;">
				<table class="popup_tbl" style="border-top: 0px;">
					<colgroup>
						<col width="21%">
						<col width="*">
					</colgroup>
					<tbody>
						<tr>
							<th>검출 개인정보 수</th>
							<td id="total"> 
							</td>
						</tr>
					</tbody>
				</table>
				<table class="popup_tbl">
					<colgroup>
						<col width="30%">
						<col width="*">
					</colgroup>
					<tbody>
						<tr>
							<th >검출 개인정보 상세 건수</th>
							<td id="datatype_area"></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="content-table" style="width: 478px; !important; height: 200px; background: #fff; border: 1px solid #c8ced3; border-top: none;">
				<table class="popup_tbl" style="border-top : none;">
					<colgroup>
						<col width="50%">
						<col width="*">
					</colgroup>
					<tbody id="details_detail"></tbody>
				</table>
			</div> 
		</div>
		<button id="btnstatisticsClose" style="margin-top: 5px; margin-left : 424px;" >닫기</button>
	</div>
</div>

<script type="text/javascript">
var pattern = '${pattern}'.split('[{').join('').split('}]').join('');
pattern = pattern.split('}, {');
$(document).ready(function () {
	var oToday = new Date();
	$("#toDate").val(getFormatDate(oToday));
	var oFromDate = new Date(oToday.setDate(oToday.getDate() - 30));
	$("#fromDate").val(getFormatDate(oFromDate));
	
	fn_drawTopNGrid(); // TOP5전체 서비스
	fn_drawMiddleNGrid(); // 정탐 TOP5
	fn_drawBottomNGrid(); // 오탐 TOP5
	fn_drawTotalNGrid(); // 전체 서버 점검 결과(의심 건수)
	fn_lowestNGrid() // 이행 점검현황
	
	setSelectDate(); 
});
$("#btnSearch").click(function(e){
	
	if($("#fromDate").val() > $("#toDate").val()){
		alert("입력한 끝 날짜가 시작 날짜 보다 빠릅니다.");
		return;
	}
	
	var postData = {
			toDate : $("#toDate").val(),
			fromDate : $("#fromDate").val()
	};
	  
	anyChartPie($("#toDate").val(), $("#fromDate").val()); // pie 그래프  
	// TOP5전체 서비스
	$("#topNGrid").setGridParam({
		url:"<%=request.getContextPath()%>/statistics/statisticsList", 
		postData : postData, 
		datatype:"json" 
		}).trigger("reloadGrid");
	
	// 정탐 TOP5
	$("#middleNGrid").setGridParam({
		url:"<%=request.getContextPath()%>/statistics/trueGridList", 
		postData : postData, 
		datatype:"json" 
	}).trigger("reloadGrid");
	
	// 오탐 TOP5
	$("#bottomNGrid").setGridParam({
		url:"<%=request.getContextPath()%>/statistics/falseGridList", 
		postData : postData, 
		datatype:"json" 
	}).trigger("reloadGrid");
	
	// 전체 서버 점검 결과(의심 건수)
	$("#totalNGrid").setGridParam({
		url:"<%=request.getContextPath()%>/statistics/manageList", 
		postData : postData, 
		datatype:"json" 
	}).trigger("reloadGrid");
	
	// 이행 점검현황
	$("#lowestNGrid").setGridParam({
		url:"<%=request.getContextPath()%>/statistics/dataImple", 
		postData : postData,
		datatype:"json" 
	}).trigger("reloadGrid");  
});
anychart.onDocumentReady(function () {
	var oToday = new Date();
	$("#toDate").val(getFormatDate(oToday));
	var toDate = $("#toDate").val();

	var oFromDate = new Date(oToday.setDate(oToday.getDate() - 30));
	$("#fromDate").val(getFormatDate(oFromDate));
	var fromDate = $("#fromDate").val(); 
	
	anyChartPie(toDate, fromDate);
	
});  

function anyChartPie(toDate, fromDate){
	var postData = {
			toDate : toDate,
			fromDate : fromDate
		};

	var chart_top = null;
	
	var chart_top_Data = [];
	var cahrt_top_color_code = [];
	  
	$("#chart_top_div").html("");
	$(".totalStatistics").html("");
	$("#total_detection").html("");
	$("#titleTotal").html("");
	var content = "";
	$.ajax({
		type: "POST",
		url: "/statistics/totalStatistics",
		async : false,
		data : postData,
		dataType : "json",
	    success: function(resultMap){
	    	for(var i = 0; pattern.length > i; i++){ // str 배열만큼 for돌림
	    		var row = pattern[i].split(', ');
	    		var PATTERN_NAME = row[0].split('PATTERN_NAME=').join('');
	    		var ID = row[1].split('ID=').join('');
	    		var COLOR_CODE = row[2].split('COLOR_CODE=').join('');
	    		var PERCENTAGE = ID+"_PERCENTAGE";
	    		
	    		var value = resultMap[ID]; 
	    		cahrt_top_color_code.push(COLOR_CODE);
	    		chart_top_Data.push({x: PATTERN_NAME, value: value});
	    		 
	    		content += PATTERN_NAME + ": " + resultMap[ID].toLocaleString('ko-KR') + "(" + resultMap[PERCENTAGE] + ") ";
	    		   
	    		if(i != 0 && i % 4 == 0) {
	    			content += "<br>"; 
	    		}
	    		    
	    	}
	    	
	    	chart_top = anychart.pie3d(chart_top_Data);
	    	
			$(".totalStatistics").append(content);
			
			var total_detection = "[" + resultMap.COUNT_TARGET_ID.toLocaleString('ko-KR') +"대 서버 검출 결과]";
	    	$("#total_detection").append(total_detection); 
	    	
	    	var contentlist = resultMap.COUNT_TARGET_ID.toLocaleString('ko-KR') + "대 서버(약 "; 
	    		contentlist += resultMap.TOTAL.toLocaleString('ko-KR') +"건)내 개인정보 정탐 확인";
	    	$("#titleTotal").append(contentlist);
	    	
	    	chart_top.palette(cahrt_top_color_code);
	    	chart_top.legend(false);
	    	 
	    	chart_top.labels().position("outside");
	    	chart_top.connectorLength("65");
	    	chart_top.labels().fontSize(14);
	    	chart_top.labels().hAlign("center");
	    	chart_top.labels().useHtml(true);
	    	chart_top.labels().format("<b>{%x}<br/></b>" + "{%PercentValue}{decimalsCount:1}%");
	    	chart_top.container('chart_top_div');
	    	chart_top.draw();
	    	
	    },
	    error: function (request, status, error) {
	        console.log("ERROR : ", error);
	        console.log("ERROR : ", request);
	        console.log("ERROR : ", status);
	    }
	});
}

function fn_drawTopNGrid() {
	var gridWidth = $("#topNGrid").parent().width();
	var colModel = [];
	
	colModel.push({label: '서비스명', 			index: 'SERVICE_NM', 			name: 'SERVICE_NM', 	width: 100, align: 'center' });
	colModel.push({label: '호스트명', 			index: 'TARGET_NAME', 			name: 'TARGET_NAME', 	width: 100, align: 'center' });
	colModel.push({label: '검출 파일 수', 		index: 'PATH_CNT', 				name: 'PATH_CNT', 		width: 100, align: 'center', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, sortable: true, sorttype: 'number' });
	colModel.push({label: '검출 개인정보 수', 	index: 'TOTAL', 				name: 'TOTAL',  		width: 100, align: 'center', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, sortable: true, sorttype: 'number' });
	for(var i = 0; pattern.length > i; i++){ // str 배열만큼 for돌림
		var row = pattern[i].split(', ');
		var pattern_name = row[0].split('PATTERN_NAME=').join('');
		var data_id = row[1].split('ID=').join('');
		var color_code = row[2].split('COLOR_CODE=').join('');
		colModel.push({index: data_id,           name: data_id,          width: 110,  align: 'center',  formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, hidden: true});
	}
	
	$("#topNGrid").jqGrid({
		datatype: "local",
	   	mtype : "POST",
        colModel : colModel,
		loadonce:true,
	   	autowidth: true,
		shrinkToFit: true,
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: gridWidth,
		height: 154,
		rownumbers : false, // 행번호 표시여부
		rownumWidth : 35, // 행번호 열의 너비	
		rowNum:20,
		rowList:[25,50,100],
	   	//editurl: 'clientArray',
	   	//cellEdit : true,
	   	//cellsubmit: 'clientArray',
	   	//multiselect:true,
		//jqgrid의 특성상 - rowpos의 이벤트는 onSelectCell, beforeEditCell 다 해주어야 함
	  	onCellSelect : function(rowid,icol,cellcontent,e){
	  		
	  		var clickY = e.pageY;
	  		var clickX = e.pageX;
	  		
	  		$("#details_detail").html("");
	  		$("#rowid").val(rowid);
	  		
	  		var SERVICE_NM = $(this).getCell(rowid, 'SERVICE_NM');
	  		var NAME = $(this).getCell(rowid, 'NAME');
	  		var PATH_CNT = $(this).getCell(rowid, 'PATH_CNT');
	  		var TOTAL = $(this).getCell(rowid, 'TOTAL');
	  		var TYPE = {};
	  		
	  		var content = '';
	  		var content_th = '<th style="text-align: left;">';
	  		
	  		for(var i = 0; pattern.length > i; i++){ 
	  			var row = pattern[i].split(', ');
	  			var pattern_name = row[0].split('PATTERN_NAME=').join('');
	  			var data_id = row[1].split('ID=').join('');
	  			var color_code = row[2].split('COLOR_CODE=').join('');
	  			
	  			var type_data = $(this).getCell(rowid, data_id);
	  			
	  			if(type_data != 0) {
		  			type_data = Number(type_data).toLocaleString('ko-KR');
	  		        content += '<tr>' + content_th + pattern_name + '</th><td>' + type_data + '</td></tr>';
	  		    }
	  		}
	  		 
	  		PATH_CNT = Number(PATH_CNT).toLocaleString('ko-KR');
	  		TOTAL = Number(TOTAL).toLocaleString('ko-KR');
	  		$("#total").html(TOTAL);  
	  		$("#details_detail").append(content);

	  		if(clickX > 1350){
	  			if(clickX <= 1483){
		  			$("#statisticsPopup").css("left", "805px");
		  			$("#statisticsPopup").css("top", clickY + "px");
	  			}else if(clickX <= 1655){
	  				$("#statisticsPopup").css("left", "969px");
	  				$("#statisticsPopup").css("top", clickY + "px");
	  			}else{
	  				$("#statisticsPopup").css("left", "1132px");
	  				$("#statisticsPopup").css("top", clickY + "px");
	  			}
	  			
	  		}else if (clickX < 1350){
	  			if(clickX > 1146){
	  				$("#statisticsPopup").css("left", "1307px");
	  				$("#statisticsPopup").css("top", clickY + "px");
	  			}else if(clickX > 981){
	  				$("#statisticsPopup").css("left", "1144px");
	  				$("#statisticsPopup").css("top", clickY + "px");
	  			}
	  		} 

			$("#statisticsPopup").show();
	  	},
	  	afterSaveCell : function(rowid,name,val,iRow,ICol){ // 로우 데이터 변경하고 엔터치거나 다른 셀 클릭했을때 발동
        },
	  	afterSaveRow : function(rowid,name,val,iRow,ICol){ // 로우 데이터 변경하고 엔터치거나 다른 셀 클릭했을때 발동
        },
		loadComplete: function(data) {
			//console.log(data);
	    },
	    gridComplete : function() {
	    }
	});
	
	var postData = {
			toDate : $("#toDate").val(),
			fromDate : $("#fromDate").val()
	};
	 
	$("#topNGrid").setGridParam({
		url:"<%=request.getContextPath()%>/statistics/statisticsList", 
		postData : postData, 
		datatype:"json" 
		}).trigger("reloadGrid");
}

function fn_drawMiddleNGrid() {
	var gridWidth = $("#middleNGrid").parent().width();
	var colModel = [];
	
	colModel.push({label:'서비스명', 		index: 'SERVICE_NM',	name: 'SERVICE_NM', 	width: 100, align: 'center' });
	colModel.push({label:'호스트명', 		index: 'NAME', 			name: 'NAME', 			width: 100, align: 'center' });
	colModel.push({label:'검출 파일 수', 	index: 'PATH_CNT',		name: 'PATH_CNT', 		width: 100, align: 'center', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, sortable: true, sorttype: 'number' });
	colModel.push({label:'검출 개인정보 수',	index: 'TOTAL', 		name: 'TOTAL',  		width: 100, align: 'center', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, sortable: true, sorttype: 'number' });
	for(var i = 0; pattern.length > i; i++){ // str 배열만큼 for돌림
		var row = pattern[i].split(', ');
		var pattern_name = row[0].split('PATTERN_NAME=').join('');
		var data_id = row[1].split('ID=').join('');
		
		colModel.push({label:pattern_name,	index: data_id,	name: data_id,	width: 110,  align: 'center',  formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, hidden: true});
	}
	
	$("#middleNGrid").jqGrid({
		datatype: "local",
	/* 	data : temp, */
	   	mtype : "POST",  
        colModel : colModel,
		loadonce:true,
	   	autowidth: true,
		shrinkToFit: true,
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: gridWidth,
		height: 154,
		rownumbers : false, // 행번호 표시여부
		rownumWidth : 35, // 행번호 열의 너비	
		rowNum:20,
		rowList:[25,50,100],
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	
	  	},
	  	onCellSelect : function(rowid,icol,cellcontent,e){
	  		
	  		var clickY = e.pageY;
	  		var clickX = e.pageX;
	  		
	  		$("#details_detail").html("");
	  		$("#rowid").val(rowid);
	  		
	  		var SERVICE = $(this).getCell(rowid, 'SERVICE');
	  		var NAME = $(this).getCell(rowid, 'NAME');
	  		var PATH_CNT = $(this).getCell(rowid, 'PATH_CNT');
	  		var TOTAL = $(this).getCell(rowid, 'TOTAL');
			var TYPE = {};
	  		
	  		var content = '';
	  		var content_th = '<th style="text-align: left;">';
	  		
	  		for(var i = 0; pattern.length > i; i++){ 
	  			var row = pattern[i].split(', ');
	  			var pattern_name = row[0].split('PATTERN_NAME=').join('');
	  			var data_id = row[1].split('ID=').join('');
	  			var color_code = row[2].split('COLOR_CODE=').join('');
	  			
	  			var type_data = $(this).getCell(rowid, data_id);
	  			
	  			if(type_data != 0) {
		  			type_data = Number(type_data).toLocaleString('ko-KR');
	  		        content += '<tr>' + content_th + pattern_name + '</th><td>' + type_data + '</td></tr>';
	  		    }
	  		}
	  		
	  		PATH_CNT = Number(PATH_CNT).toLocaleString('ko-KR');
	  		TOTAL = Number(TOTAL).toLocaleString('ko-KR');
	  		
	  		$("#total").text(TOTAL);

	  		$("#details_detail").append(content);

	  		if(clickX > 1350){
	  			if(clickX <= 1483){
		  			$("#statisticsPopup").css("left", "805px");
		  			$("#statisticsPopup").css("top", clickY + "px");
	  			}else if(clickX <= 1655){
	  				$("#statisticsPopup").css("left", "969px");
	  				$("#statisticsPopup").css("top", clickY + "px");
	  			}else{
	  				$("#statisticsPopup").css("left", "1132px");
	  				$("#statisticsPopup").css("top", clickY + "px");
	  			}
	  			
	  		}else if (clickX < 1350){
	  			if(clickX > 1146){
	  				$("#statisticsPopup").css("left", "1307px");
	  				$("#statisticsPopup").css("top", clickY + "px");
	  			}else if(clickX > 981){
	  				$("#statisticsPopup").css("left", "1144px");
	  				$("#statisticsPopup").css("top", clickY + "px");
	  			}
	  		} 

			$("#statisticsPopup").show();
	  		
	  	},
	  	afterSaveCell : function(rowid,name,val,iRow,ICol){ // 로우 데이터 변경하고 엔터치거나 다른 셀 클릭했을때 발동
        },
	  	afterSaveRow : function(rowid,name,val,iRow,ICol){ // 로우 데이터 변경하고 엔터치거나 다른 셀 클릭했을때 발동
        },
		loadComplete: function(data) {
			//console.log(data);
	    },
	    gridComplete : function() {
	    }
	});
	
	var postData = {
			toDate : $("#toDate").val(),
			fromDate : $("#fromDate").val()
	};
	$("#middleNGrid").setGridParam({
		url:"<%=request.getContextPath()%>/statistics/trueGridList", 
		postData : postData, 
		datatype:"json" 
	}).trigger("reloadGrid"); 
}

function fn_drawBottomNGrid() {
	var gridWidth = $("#bottomNGrid").parent().width();
	var colModel = [];
	
	colModel.push({label:'서비스명', 		index: 'SERVICE_NM',	name: 'SERVICE_NM', 	width: 100, align: 'center' });
	colModel.push({label:'호스트명', 		index: 'NAME', 			name: 'NAME', 			width: 100, align: 'center' });
	colModel.push({label:'검출 파일 수', 	index: 'PATH_CNT',		name: 'PATH_CNT', 		width: 100, align: 'center', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, sortable: true, sorttype: 'number' });
	colModel.push({label:'검출 개인정보 수',	index: 'TOTAL', 		name: 'TOTAL',  		width: 100, align: 'center', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, sortable: true, sorttype: 'number' });
	for(var i = 0; pattern.length > i; i++){ // str 배열만큼 for돌림
		var row = pattern[i].split(', ');
		var pattern_name = row[0].split('PATTERN_NAME=').join('');
		var data_id = row[1].split('ID=').join('');
		
		colModel.push({label:pattern_name,	index: data_id,	name: data_id,	width: 110,  align: 'center',  formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, hidden: true});
	}
	
	$("#bottomNGrid").jqGrid({
		datatype: "local",
		/* data : temp, */
	   	mtype : "POST",
        colModel : colModel,
		loadonce:true,
	   	autowidth: true,
		shrinkToFit: true,
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: gridWidth,
		height: 154,
		rownumbers : false, // 행번호 표시여부
		rownumWidth : 35, // 행번호 열의 너비	
		rowNum:20,
		rowList:[25,50,100],
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	
	  	},
	  	onCellSelect : function(rowid,icol,cellcontent,e){
	  		
	  		var clickY = e.pageY;
	  		var clickX = e.pageX;
	  		
	  		$("#details_detail").html("");
	  		$("#rowid").val(rowid);
	  		
	  		var SERVICE = $(this).getCell(rowid, 'SERVICE');
	  		var NAME = $(this).getCell(rowid, 'NAME');
	  		var PATH_CNT = $(this).getCell(rowid, 'PATH_CNT');
	  		var TOTAL = $(this).getCell(rowid, 'TOTAL');
			var TYPE = {};
	  		
	  		var content = '';
	  		var content_th = '<th style="text-align: left;">';
	  		
	  		for(var i = 0; pattern.length > i; i++){ 
	  			var row = pattern[i].split(', ');
	  			var pattern_name = row[0].split('PATTERN_NAME=').join('');
	  			var data_id = row[1].split('ID=').join('');
	  			var color_code = row[2].split('COLOR_CODE=').join('');
	  			
	  			var type_data = $(this).getCell(rowid, data_id);
	  			
	  			if(type_data != 0) {
		  			type_data = Number(type_data).toLocaleString('ko-KR');
	  		        content += '<tr>' + content_th + pattern_name + '</th><td>' + type_data + '</td></tr>';
	  		    }
	  		}
	  		
	  		PATH_CNT = Number(PATH_CNT).toLocaleString('ko-KR');
	  		TOTAL = Number(TOTAL).toLocaleString('ko-KR');
	  		
			$("#total").text(TOTAL);
	  		
	  		$("#details_detail").append(content);
	  		
	  		if(clickX > 1350){
	  			if(clickX <= 1483){
		  			$("#statisticsPopup").css("left", "805px");
		  			$("#statisticsPopup").css("top", clickY + "px");
	  			}else if(clickX <= 1655){
	  				$("#statisticsPopup").css("left", "969px");
	  				$("#statisticsPopup").css("top", clickY + "px");
	  			}else{
	  				$("#statisticsPopup").css("left", "1132px");
	  				$("#statisticsPopup").css("top", clickY + "px");
	  			}
	  			
	  		}else if (clickX < 1350){
	  			if(clickX > 1146){
	  				$("#statisticsPopup").css("left", "1307px");
	  				$("#statisticsPopup").css("top", clickY + "px");
	  			}else if(clickX > 981){
	  				$("#statisticsPopup").css("left", "1144px");
	  				$("#statisticsPopup").css("top", clickY + "px");
	  			}
	  		} 

			$("#statisticsPopup").show();
	  	},
	  	afterSaveCell : function(rowid,name,val,iRow,ICol){ // 로우 데이터 변경하고 엔터치거나 다른 셀 클릭했을때 발동
        },
	  	afterSaveRow : function(rowid,name,val,iRow,ICol){ // 로우 데이터 변경하고 엔터치거나 다른 셀 클릭했을때 발동
        },
		loadComplete: function(data) {
			//console.log(data);
	    },
	    gridComplete : function() {
	    }
	});
	
	var oToday = new Date();
	$("#toDate").val(getFormatDate(oToday));
	var toDate = $("#toDate").val();
	var oFromDate = new Date(oToday.setDate(oToday.getDate() - 30));
	$("#fromDate").val(getFormatDate(oFromDate));
	var fromDate = $("#fromDate").val();
	
	var postData = {
			toDate : $("#toDate").val(),
			fromDate : $("#fromDate").val()
	};
	$("#bottomNGrid").setGridParam({
		url:"<%=request.getContextPath()%>/statistics/falseGridList", 
		postData : postData, 
		datatype:"json" 
	}).trigger("reloadGrid");
}

function fn_drawTotalNGrid() {
	var gridWidth = $("#totalNGrid").parent().width();
	var colModel = [];
	
	colModel.push({label:'월',  			index: 'month_day', 		name: 'month_day', 			width: 50, align: 'center' });
	colModel.push({label:'검출 서버',  	index: 'host_cnt', 			name: 'host_cnt', 			width: 50, align: 'center', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, sortable: true, sorttype: 'number' });
	colModel.push({label:'검출 파일 수', 	index: 'match_locations', 	name: 'match_locations', 	width: 50, align: 'center', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, sortable: true, sorttype: 'number' });
	
	for(var i = 0; pattern.length > i; i++){  
		var row = pattern[i].split(', ');
		var pattern_name = row[0].split('PATTERN_NAME=').join('');
		var data_id = row[1].split('ID=').join('');
		var color_code = row[2].split('COLOR_CODE=').join('');
		 
		colModel.push({label: pattern_name,	index: data_id,	name: data_id,	width: 50,  align: 'center',  formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: '0'}});
	}
	colModel.push({label:'검출 개인정보 수',  	index: 'match', 			name: 'match', 	width: 50, align: 'center', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, sortable: true, sorttype: 'number' });
	  
	$("#totalNGrid").jqGrid({ 
		datatype: "local",
		/* data : temp,  */
	   	mtype : "POST",
        colModel : colModel, 
		loadonce:true,
	   	autowidth: true,
		shrinkToFit: true,
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: gridWidth,
		height: 195,
		rownumbers : false, // 행번호 표시여부
		rownumWidth : 35, // 행번호 열의 너비	
		rowNum:20,
		rowList:[25,50,100],
	   	//editurl: 'clientArray',
	   	//cellEdit : true,
	   	//cellsubmit: 'clientArray',
	   	//multiselect:true,
		//jqgrid의 특성상 - rowpos의 이벤트는 onSelectCell, beforeEditCell 다 해주어야 함
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	
	  	},
	  	afterSaveCell : function(rowid,name,val,iRow,ICol){ // 로우 데이터 변경하고 엔터치거나 다른 셀 클릭했을때 발동
        },
	  	afterSaveRow : function(rowid,name,val,iRow,ICol){ // 로우 데이터 변경하고 엔터치거나 다른 셀 클릭했을때 발동
        },
		loadComplete: function(data) {
			
			if(data.page == null){
				bar_graph_left(data);   
				bar_graph_right(data);
			}
	    },
	    gridComplete : function(data) { 
	    	
	    }
	});
	
	var postData = {
			toDate : $("#toDate").val(),
			fromDate : $("#fromDate").val()
	};    
	$("#totalNGrid").setGridParam({
		url:"<%=request.getContextPath()%>/statistics/manageList", 
		postData : postData, 
		datatype:"json" 
	}).trigger("reloadGrid");
}
	
$("#btnstatisticsClose").click(function(){ 
	$("#statisticsPopup").hide();
});

$("#btnCancleStatisticsPopup").click(function(){ 
	$("#statisticsPopup").hide();
});

// 검출 개인정보 수
function bar_graph_left(resultList) {
	
	var month_day = [];
    var host_cnt = [];
    var match_locations = [];
    var total = [];
    
    $.each(resultList, function(key, value){
    	$.each(value, function(key, value){
            if(key == "host_cnt") host_cnt.push(value);
            if(key == "match_locations") match_locations.push(value);
            if(key == "month_day") month_day.push(value);
            if(key == "match") total.push(value);
        });
    });
    
    var echartbar = echarts.init(document.getElementById('chart_bottom_div'));
    echartbar.setOption({
    	legend : {
            bottom: 10,
            data : ['검출 개인정보 수'],
        },
		tooltip: { 
			trigger: 'axis',
			axisPointer: {
				type: 'shadow'
			}
		},
		xAxis: [
				{
				type: 'category',
				data: month_day,
				axisTick: {
					alignWithLabel: true
					}
			}
		],
		yAxis: [
				{
					type: 'value'
				}
			],
		series: [
			{
				name: '검출 개인정보 수',
				type: 'bar',
                color :'#006eb6',
                barMaxWidth: 50,   
				data: total,
				 itemStyle: {
                     normal: {
                         label: {
                             show: true,
                             position: 'top',
                             color: '#000',
                             formatter: function (params) {
                                 return params.value.toLocaleString('ko-KR');
                              } 
                         }
                     }
                 }
			},
		] 
    });
}

// 검출 서버, 검출 파일 수
function bar_graph_right(resultList) {
	
	var month_day = [];
    var host_cnt = [];
    var match_locations = [];
    var total = [];
    
    $.each(resultList, function(key, value){
    	$.each(value, function(key, value){
            if(key == "host_cnt") host_cnt.push(value);
            if(key == "match_locations") match_locations.push(value);
            if(key == "month_day") month_day.push(value);
            if(key == "match") total.push(value);
        });
    });
    
	
	var echartbar = echarts.init(document.getElementById('bar_graph_left'));
    echartbar.setOption({
    	legend : {
            bottom: 10,
            data : ['검출 서버', '검출 파일 수'],
        },
		tooltip: {
			trigger: 'axis',
			axisPointer: {
				type: 'shadow'
			}
		},
		xAxis: [
				{
				type: 'category',
				data: month_day,
				axisTick: {
					alignWithLabel: true
					}
			}
		],
		yAxis: [
				{
					type: 'value'
				}
			],
		series: [
			{
				name: '검출 서버',
				type: 'bar',
				color :'#006eb6', 
				barMaxWidth: 50, 
				data: host_cnt,
				 itemStyle: {
                     normal: {
                         label: {
                             show: true,
                             position: 'top',
                             color: '#000',
                             formatter: function (params) {
                                return params.value.toLocaleString('ko-KR');
                             }
                         }
                     }
                 }
			},
			{
				name: '검출 파일 수', 
				type: 'bar',
				color :'#dc143c', 
				barMaxWidth: 50, 
				data: match_locations, 
				 itemStyle: {
                     normal: {
                         label: {
                             show: true,
                             position: 'top',
                             color: '#000',
                             formatter: function (params) {
                                 return params.value.toLocaleString('ko-KR');
                              }
                         }
                     }
                 }
			}
		]
    });
}

// 이행 점검 현황
function fn_lowestNGrid() {
	var grid_Width = $("#lowestNGrid").parent().width();
	
	$("#lowestNGrid").jqGrid({
		datatype: "local",
		/* data : temp,  */
	   	mtype : "POST",
		colNames:['호스트명','현재 검출파일','현재 검출건','최초 검출파일','최초 검출건','최초 검출일','증감률','이행점검 횟수','최근 실행일자'],
		colModel: [
			{ index: 'target_name', 			name: 'target_name', 			width: 100, align: 'center' },
			{ index: 'max_match_locations', 	name: 'max_match_locations',  	width: 100, align: 'right', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, sortable: true, sorttype: 'number' },
			{ index: 'max_match', 				name: 'max_match', 				width: 100, align: 'right', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, sortable: true, sorttype: 'number'},
			{ index: 'min_match_locations', 	name: 'min_match_locations', 	width: 100, align: 'right', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, sortable: true, sorttype: 'number' },
			{ index: 'min_match', 				name: 'min_match', 				width: 100, align: 'right', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, sortable: true, sorttype: 'number' },
			{ index: 'min_report_id', 			name: 'min_report_id', 			width: 100, align: 'center' },
			{ index: 'rate', 					name: 'rate', 					width: 100, align: 'right' , sorttype: 'number' },
			{ index: 'serach_cnt', 				name: 'serach_cnt', 			width: 100, align: 'right', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, sortable: true, sorttype: 'number' },
			{ index: 'max_report_id', 			name: 'max_report_id', 			width: 100, align: 'center'}
		], 
		loadonce:true,
	   	autowidth: true,
		shrinkToFit: true,
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: grid_Width,
		height: 195,
		rownumbers : false, // 행번호 표시여부
		rownumWidth : 35, // 행번호 열의 너비	
		rowNum:20,
		rowList:[25,50,100],
	   	//editurl: 'clientArray',
	   	//cellEdit : true,
	   	//cellsubmit: 'clientArray',
	   	//multiselect:true, 
		//jqgrid의 특성상 - rowpos의 이벤트는 onSelectCell, beforeEditCell 다 해주어야 함
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	
	  	},
	  	afterSaveCell : function(rowid,name,val,iRow,ICol){ // 로우 데이터 변경하고 엔터치거나 다른 셀 클릭했을때 발동
        },
	  	afterSaveRow : function(rowid,name,val,iRow,ICol){ // 로우 데이터 변경하고 엔터치거나 다른 셀 클릭했을때 발동
        },
	    gridComplete : function() {
	    }
	});
	
	var postData = {
			toDate : $("#toDate").val(),
			fromDate : $("#fromDate").val()
	}; 
	$("#lowestNGrid").setGridParam({
		url:"<%=request.getContextPath()%>/statistics/dataImple", 
		postData : postData,
		datatype:"json" 
	}).trigger("reloadGrid"); 
	 
}
function getFormatDate(oDate) {
	var nYear = oDate.getFullYear(); // yyyy 
	var nMonth = (1 + oDate.getMonth()); // M 
	nMonth = ('0' + nMonth).slice(-2); // month 두자리로 저장 

	var nDay = oDate.getDate(); // d 
	nDay = ('0' + nDay).slice(-2); // day 두자리로 저장

	return nYear + '-' + nMonth + '-' + nDay;
}
function setSelectDate() {
	$("#fromDate").datepicker({
		changeYear : true,
		changeMonth : true,
		dateFormat : 'yy-mm-dd'
	});

	$("#toDate").datepicker({
		changeYear : true,
		changeMonth : true,
		dateFormat : 'yy-mm-dd' 
	});
     
	var oToday = new Date();
	$("#toDate").val(getFormatDate(oToday)); 
   
	var oFromDate = new Date(oToday.setDate(oToday.getDate() - 30));
	$("#fromDate").val(getFormatDate(oFromDate));
}
</script>
	<!-- wrap -->
</body>
</html>
