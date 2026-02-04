<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="../../include/header.jsp"%>
<style>
.user_info th {
	width: 17%;
}
#left_datatype th, #left_datatype td {
	padding: 0;
}
.ui-jqgrid tr.ui-row-ltr td{
	cursor: pointer;
}
@media screen and (-ms-high-contrast: active), (-ms-high-contrast: none) {
	.sch_area{
		top: 0px !important;
		left: 5px !important;
	}
	.list_sch{
		right: 1px !important;
		top: -34px !important;
	}
}
</style>

<section>
	<!-- container -->
	<div class="container">
		<h3>검색 정책</h3>
		<!-- content -->
		<div class="content magin_t25">
			<div class="searchBox" style="float: left;">
				<table class="user_info narrowTable" id="navGridSearchDiv"  style="width: 310px;">
					<tbody>
						<tr id="searchTextBox">
							<th style="text-align: center; border-radius: 0.25rem;" class="searchName">
								<select id="searchFilter"></select>
							</th>
            			     	<td id="defaultSearchTextBox">
               					<input type="text" style="width: 205px; padding-left: 5px;" size="10" class="searchContent" id="searchContent"  placeholder="검색어를 입력하세요.">	
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
						<button type="button" class="btn_down" id="btn_new" class="btn_new">신규정책 생성</button>
					</div>  
					<div style="float: right;">
						<h5 style="width: 24px; height: 24px; margin-top: 2px; background-size : 100%; background-image: url('${pageContext.request.contextPath}/resources/assets/images/pi_list.png');" id="tableShow">&nbsp;</h5>
					</div>
				</div>
			</div> 
			<div class="grid_top" style="height: 295px; width: 100%; padding-top:10px;">
				<div class="left_box2" style="overflow: hidden;">
   					<table id="topNGrid"></table>
   				 	<div id="topNGridPager"></div>
   				</div>
			</div>
			<div class="grid_top" style="width: 50%; height: 419px; bottom: 50px; float: left;">
				<table class="user_info" style="width: 100%; height: 100%; float: left;">
					<caption>정책 상세 정보</caption>
					<colgroup>
						<col width="20%">
						<col width="5%">
						<col width="75%">
					</colgroup>
					<tbody>
						<tr style="height: 25%;">
							<th style="border-bottom: 1px solid #c8ced3; border-radius: 0.25rem 0 0 0;">개인정보 유형 명</th>
							<td>-</td>
							<td id="right_datatype_name"></td>
						</tr>
						<tr style="height: 75%;">
							<th>개인정보유형</th>
							<td>-</td>
							<!-- <td id="left_datatype_name"></td> -->
							<td><table id="left_datatype" style="width: 90%;"></table></td>
						</tr>
					</tbody>
				</table> 
			</div>
			
			
			<div class="grid_top" style="width: 50%; height: 419px; bottom: 50px; float: right; padding-left:20px;">
				<table class="user_info" style="width: 100%; height: 100%; float: right;">
					<caption>정책 상세 정보</caption>
					<colgroup>
						<col width="25%">
						<col width="5%">
						<col width="*">
					</colgroup>
					<tbody>
						<tr style="height: 9%;">
							<th class="borderB">정책명</th>
							<td>-</td>
							<td id="right_policy_name"  class="policyPadding"></td>
						</tr>
						<tr style="height: 9%;">
							<th class="borderB">개인정보 유형</th>
							<td>-</td>
							<td id="btn_datatype_area" class="policyPadding">
							</td>
						</tr>
						<tr style="height: 9%;">
							<th class="borderB" style="border-radius: 0.25rem 0 0 0;">정책 접근 가능 유저</th>
							<td>-</td>
							<td id="view_user" class="policyPadding">
							</td>
						</tr>
						<tr style="height: 9%;">
							<th class="borderB" style="border-radius: 0.25rem 0 0 0;">적용 서버<input type='hidden' id ="view_server"></th>
							<td>-</td>
							<td id="view_server_cnt">
							</td>
						</tr>
						<tr style="height: 9%;">
							<th class="borderB" >전체 공개</th>
							<td>-</td>
							<td style="padding:0px 5px" class="policyPadding">
								<input type="checkbox" style="display: none;" id="right_enable"/>
							</td>
						</tr>
						<tr style="height: 9%;" class="policyPadding">
							<th class="borderB">주기</th>
							<td>-</td>
							<td style="padding:0px" id="right_cycle">
							</td>
						</tr>
						<tr style="height: 9%;">
							<th class="borderB">cpu</th>
							<td>-</td>
							<td id="right_cpu" class="policyPadding">
							</td>
						</tr>
						<tr style="height: 9%;">
							<th class="borderB">memory</th>
							<td>-</td>
							<td id="right_memory" class="policyPadding">
							</td>
						</tr>
						<tr style="height: 9%;">
							<th class="borderB" >throughput</th>
							<td>-</td>
							<td id="right_throughput" class="policyPadding">
							</td> 
						</tr>
						<tr style="height: 9%;">
							<th class="borderB" >scanTraceLog</th>
							<td>-</td>
							<td class="policyPadding">
								<input type="checkbox" style="display: none;" id="right_trace"/>
							</td>
						</tr>
						<tr style="height: 10%;">
							<td colspan="3" style="text-align: right;" id="right_td_save">
							</td>
						</tr>
					</tbody>
				</table>
				<input type="hidden" id="datatype_id" value=""/>
				<input type="hidden" id="std_id" value=""/>
				<input type="hidden" id="idx" value=""/>
			</div>
		</div>
	</div>
	<!-- container -->
	
<!-- 	개인정보 유형 조회 -->
	<div id="datatype_pop" class="popup_layer" style="display:none">
		<div class="popup_box" style="height: 447px; width: 1300px; left:33%; top:50%; padding: 10px; background: #f9f9f9;">
		<img class="CancleImg" id="btnCancleDataTypePop" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
			<div class="left_box2" style="height: auto; min-height: 200px; margin-top: 40px;">
				<table id="datatypeGrid"></table>
				<div id="datatypeGridPager"></div>
			</div>
			<div class="popup_btn">
			<div class="btn_area">
				<button type="button" id="btnSaveDatatype">변경</button>
				<button type="button" id="btnCancelChangeDatatype">취소</button>
			</div>
		</div>
		</div>
	</div>
	<div id="accessUserPopup" class="popup_layer" style="display:none">
		<div class="popup_box" style="height: 200px; width: 400px; padding: 10px; background: #f9f9f9;">
		<img class="CancleImg" id="btnCancleAccessUserPopup" onClick="btnAccessCancel()" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
			<div class="popup_top" style="background: #f9f9f9;">
				<h1 style="color: #222; padding: 0; box-shadow: none;">정책 접근 가능 유저</h1>
			</div>
			<div class="popup_content">
				<div class="content-box" style="height: 180px; background: #fff; border: 1px solid #c8ced3; padding: 0 0 0 10px;">
					<!-- <h2>세부사항</h2>  -->
					<textarea id="accesUser" class="edt_sch" style="width: 100%; height: 100%; background: #fff; border: none; resize: none; overflow-y: auto;"></textarea>
				</div>
			</div>
			<div class="popup_btn">
				<div id="acesssBtn" class="btn_area" style="padding: 10px 0; margin: 0;">
					<button type="button" onClick="btnAccessSave()">저장</button>
					<button type="button" onClick="btnAccessCancel()">취소</button>
				</div>
			</div>
		</div>
	</div>
</section>

<!-- section -->
<!-- section -->
<%@ include file="../../include/footer.jsp"%>
<script>

var colModel = [];
GridName = "#topNGrid";
var openedWindows = [];
var pattern = '${pattern}'.split('[{').join('').split('}]').join('');
pattern = pattern.split('}, {');

$(document).ready(function () {
	

	fn_drawTopNGrid();
	search_policy();
	$("input:checkbox[name='action']").attr("disabled", "disabled");
	
	$("#btnUpdateUser").on("click", function(e) {
		// accessUserPopup
		$("#accessUserPopup").show();
	});
	
	$("#btnCancleAccessUserPopup").on("click", function(e) {
		// accessUserPopup
		$("#accessUserPopup").hide();
	});
	
	$(".navGridSearchBtn").click(function(e){
		url = null;
		navGridSearchBtn(url);
	});
	
});

function createRadio(cellvalue, options, rowObject) {
	//var value = options['rowId'];
	var value = rowObject['DATATYPE_ID'];
	var str = '<input type="radio" name="gridRadio" data-rowid="'+options['rowId']+'" value="' + rowObject['DATATYPE_ID'] + '" id="gridRadio_' + value + '">';
    return str;
}
function fn_drawTopNGrid() {
	
	colModel.push({ label:'개인정보유형',  		index: 'DATATYPE', 			name: 'DATATYPE', 			editable: true, hidden: true });
	colModel.push({ label:'',  				index: 'IDX', 				name: 'IDX', 				editable: true, width: 200, hidden: true });
	colModel.push({ label:'정책명',  			index: 'NAME', 				name: 'NAME', 				editable: true, width: 200 });
	colModel.push({ label:'개인정보 유형',  	index: 'TYPE', 				name: 'TYPE', 				width: 200, formatter:createType});
	colModel.push({ label:'개인정보 유형1', 	index: 'TYPE1', 			name: 'TYPE1', 				width: 200, hidden: true });
	colModel.push({ label:'datatype_id',	index: 'DATATYPE_ID', 		name: 'DATATYPE_ID', 		width: 200, hidden: true});
	colModel.push({ label:'std_id',  		index: 'STD_ID', 			name: 'STD_ID', 			width: 200, hidden: true});
	colModel.push({ label:'comment',  		index: 'COMMENT', 			name: 'COMMENT', 			width: 200, hidden: true});
	colModel.push({ label:'cycle',  		index: 'CYCLE', 			name: 'CYCLE', 				width: 200, hidden: true});
	colModel.push({ label:'action',  		index: 'ACTION', 			name: 'ACTION', 			width: 200, hidden: true});
	colModel.push({ label:'enabled',  		index: 'ENABLED', 			name: 'ENABLED', 			width: 200, hidden: true});
	colModel.push({ label:'view_user',  	index: 'VIEW_USER', 		name: 'VIEW_USER', 			width: 200, hidden: true});
   	colModel.push({ label:'start_dtm',  	index: 'START_DTM',      	name: 'START_DTM',    		width: 200, hidden: true});
	colModel.push({ label:'from_hour',  	index: 'PAUSE_FROM', 		name: 'PAUSE_FROM', 		width: 200, hidden: true});
	colModel.push({ label:'from_minu',  	index: 'PAUSE_FROM_MINU', 	name: 'PAUSE_FROM_MINU',	width: 200, hidden: true});
	colModel.push({ label:'to_hour',  		index: 'PAUSE_TO', 			name: 'PAUSE_TO', 			width: 200, hidden: true});
	colModel.push({ label:'to_minu',  		index: 'PAUSE_TO_MINU', 	name: 'PAUSE_TO_MINU', 		width: 200, hidden: true});
	colModel.push({ label:'policy_type',  	index: 'POLICY_TYPE', 		name: 'POLICY_TYPE', 		width: 200, hidden: true});
	colModel.push({ label:'recent',  		index: 'RECENT', 			name: 'RECENT', 			width: 200, hidden: true});
	colModel.push({ label:'cpu',  			index: 'RECON_CPU', 		name: 'RECON_CPU', 			width: 200, hidden: true});
	colModel.push({ label:'memory',  		index: 'RECON_MEMORY', 		name: 'RECON_MEMORY', 		width: 200, hidden: true});
	colModel.push({ label:'throughput',  	index: 'THROUGHPUT_RATE',	name: 'THROUGHPUT_RATE', 	width: 200, hidden: true});
	colModel.push({ label:'recon_trace',  	index: 'RECON_TRACE',		name: 'RECON_TRACE', 		width: 200, hidden: true});
	colModel.push({ label:'server_cnt',  	index: 'SERVER_CNT',		name: 'SERVER_CNT', 		width: 200, hidden: true});
	colModel.push({ label:'server',  		index: 'SERVER',			name: 'SERVER', 			width: 200, hidden: true});
	GridSearchTypeChk();
	searchListAppend(); 		
	var gridWidth = $("#topNGrid").parent().width();
	$("#topNGrid").jqGrid({
		datatype: "local",
	   	mtype : "POST",
		colModel: colModel,
		loadonce:true,
	   	autowidth: true,
		shrinkToFit: true,
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: gridWidth,
		height: 160,
		rownumbers : false, // 행번호 표시여부
		rownumWidth : 35, // 행번호 열의 너비	
		rowNum:25,
		rowList:[25,50,100], 
		pager: "#topNGridPager",
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {
	  		var rowData = $(this).getRowData(rowid);
	  		setDetails(rowid);
	  	},
	  	afterSaveCell : function(rowid,name,val,iRow,ICol){ // 로우 데이터 변경하고 엔터치거나 다른 셀 클릭했을때 발동
        },
	  	afterSaveRow : function(rowid,name,val,iRow,ICol){ // 로우 데이터 변경하고 엔터치거나 다른 셀 클릭했을때 발동
        },
		loadComplete: function(data) {
			automaticCompletion(null);
	    },
	    gridComplete : function() {
	    }
	});
}

function datatypeGrid(){
	var gridWidth = $("#datatypeGrid").parent().width();
	var gridHeight = 410;

	var colNames = [];
	var colModel = [];
	var patternCnt = '${patternCnt}';
	
	/* 이름 */
	colNames.push('','아이디','총아이디','개인정보유형명', '타입 유형명카피', '개인정보 유형 데이터');
	for(var i=0; i < patternCnt ; i++){
		var row = pattern[i].split(', ');
		var ID = row[0].split('ID=').join(''); //변수 TABLE_NAME은 TB_NAME1, 다음 반복문에서는 TB_NAME2
		var PATTERN_NAME = row[1].split('PATTERN_NAME=').join(''); // 변수 CHK Y, 다음 반복문에서는 N
		var data_id = PATTERN_NAME.split('=')[1];
		
		colNames.push(ID.split('=')[1]);
		colNames.push(ID.split('=')[1]+" 체크");
		colNames.push(ID.split('=')[1]+" 체크copy");
		colNames.push(ID.split('=')[1]+" 임계치");
		colNames.push(ID.split('=')[1]+" 중복");
	}
	colNames.push('이미지검사', 'OCR체크', '증분검사CHK', '증분검사', '확장자', 'VOICE', '작업');

	/* 데이터 */
	colModel.push({ index: 'CHKBOX', 		name: 'CHKBOX',		width: 20,  align: 'center', editable: true, edittype: 'checkbox', 
					editoptions: { value: '1:0' }, formatoptions: { disabled: false }, formatter: createRadio});
	colModel.push({ index: 'DATATYPE_ID', 			name: 'DATATYPE_ID',			width:1, 	align:'center', hidden:true});
	colModel.push({ index: 'STD_ID', 				name: 'STD_ID',					width:1, 	align:'center', hidden:true});
	colModel.push({ index: 'DATATYPE_LABEL_COPY',	name: 'DATATYPE_LABEL_COPY',	width: 100, align: 'left'});
	colModel.push({ index: 'DATATYPE_LABEL',		name: 'DATATYPE_LABEL',			width: 120, align: 'left', hidden: true});
	colModel.push({ index: 'DATATYPE',				name: 'DATATYPE',				width: 100, align: 'left', hidden: true});

	for(var i = 0; pattern.length > i; i++){
		var row = pattern[i].split(', ');
		var ID = row[0].split('ID=').join(''); //변수 TABLE_NAME은 TB_NAME1, 다음 반복문에서는 TB_NAME2
		var PATTERN_NAME = row[1].split('PATTERN_NAME=').join(''); // 변수 CHK Y, 다음 반복문에서는 N
		var data_id = PATTERN_NAME.split('=')[1];
		
		colModel.push({ index: data_id+"_CHK", 		name: data_id+"_CHK", 			width: 40, align: 'left', sortable: false });
		colModel.push({ index: data_id, 			name: data_id, 					width: 40, align: 'left', hidden: true});
		colModel.push({ index: data_id+"_CHK_COPY", name: data_id+"_CHK_COPY", 		width: 40, align: 'left', hidden: true});
		colModel.push({ index: data_id+"_CNT", 		name: data_id+"_CNT", 			width: 40, align: 'left', hidden: true});
		colModel.push({ index: data_id+"_DUP", 		name: data_id+"_DUP", 			width: 40, align: 'left', hidden: true});
	}

	colModel.push({ index: 'OCR_CHK', 					name: 'OCR_CHK', 					width: 40, align: 'center', sortable: false });
	colModel.push({ index: 'OCR', 						name: 'OCR', 						width: 40, align: 'left', hidden: true });
	colModel.push({ index: 'RECENT', 						name: 'RECENT', 					width: 40, align: 'left', hidden: true });
	colModel.push({ index: 'RECENT_CHK', 					name: 'RECENT_CHK', 				width: 40, align: 'center'});
	colModel.push({ index: 'EXTENSION', 					name: 'EXTENSION', 					width: 40, align: 'left'});
	colModel.push({ index: 'VOICE', 						name: 'VOICE', 						width: 40, align: 'center', hidden: true});
	colModel.push({ index: 'BUTTON', 						name: 'BUTTON',						width: 40, align: 'center', sortable: false , hidden: true});

	$("#datatypeGrid").jqGrid({
		//url: 'data.json',
		datatype: "local",
	   	mtype : "POST",
	   	ajaxGridOptions : {
			type    : "POST",
			async   : true
		},
		colNames:colNames,
		colModel:colModel,
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: gridWidth,
		height: gridHeight,
		loadonce: true, // this is just for the demo
	   	autowidth: true,
		shrinkToFit: false,
		rownumbers : false, // 행번호 표시여부
		rownumWidth : 30, // 행번호 열의 너비	
		rowNum:15,
	   	rowList:[15,30,50],			
		pager: "#datatypeGridPager",
		//jqgrid의 특성상 - rowpos의 이벤트는 onSelectCell, beforeEditCell 다 해주어야 함
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	  	
	  	},
	  	ondblClickRow: function(nRowid, icol, cellcontent, e){
	  	},
		loadComplete: function(data) {
			var ids = $("#datatypeGrid").getDataIDs();
			
	        $.each(ids, function(idx, rowId) {
	            rowData = $("#datatypeGrid").getRowData(rowId) ;
	            $("#datatypeGrid").setCell(rowId, 'DATATYPE_LABEL_COPY', rowData.DATATYPE_LABEL);
	            
	            var html = ""; 
                
                for(var i = 0; pattern.length > i; i++){
                	var row = pattern[i].split(', ');
            		var ID = row[0].split('ID=').join(''); //변수 TABLE_NAME은 TB_NAME1, 다음 반복문에서는 TB_NAME2
            		var PATTERN_NAME = row[1].split('PATTERN_NAME=').join(''); // 변수 CHK Y, 다음 반복문에서는 N
            		var data_id = PATTERN_NAME.split('=')[1];
                	
            		var custom_nm = data_id;
             		var custom_nm_dup = data_id+"_DUP";
             		var custom_nm_cnt = data_id+"_CNT";
             		var custom_nm_chk = data_id+"_CHK";
             		
             		var change_col = rowData[custom_nm_chk];
					var chk_grid_copy = data_id+"_CHK_COPY";
					
	                $("#datatypeGrid").jqGrid('setCell', rowId, chk_grid_copy, change_col);
            		
            		html = "<input type='checkbox' disabled='disabled' "+((rowData[custom_nm_chk] == 1)?"checked='checked'":"")+">&nbsp;";
	            	html += "<input type='checkbox' disabled='disabled' "+((rowData[custom_nm_dup] == 1)?"checked='checked'":"")+">&nbsp;";
	            	html += (rowData[custom_nm_chk] > 0)? rowData[custom_nm_cnt] : '';
	            	
	            	$("#datatypeGrid").setCell(rowId, custom_nm_chk, html);
                }
	            
	            html = "<input type='checkbox' disabled='disabled' "+((rowData.OCR == 1)?"checked='checked'":"")+">&nbsp;";
	            $("#datatypeGrid").setCell(rowId, 'OCR_CHK', html);
	            
	            html = "<input type='checkbox' disabled='disabled' "+((rowData.RECENT > 0)?"checked='checked'":"")+">&nbsp;";
	            html +=  (rowData.RECENT > 0) ? rowData.RECENT : '';
	            $("#datatypeGrid").setCell(rowId, 'RECENT_CHK', html);
	            
	            $("#datatypeGrid").setCell(rowId, 'BUTTON', "<button type='button' class='gridSubSelBtn' name='gridSubSelBtn'>선택</button>");
	            
	            
	            if(rowData.DATATYPE_ID == $('#datatype_id').val()){
	            	$("#gridRadio_" + rowData.DATATYPE_ID).prop('checked', true);
	            }
	        });
			
			$(".gridSubSelBtn").on("click", function(e) {
		  		e.stopPropagation();
				
				$("#datatypeGrid").setSelection(event.target.parentElement.parentElement.id);
				
				var offset = $(this).parent().offset();
				$("#taskWindow").css("left", (offset.left - $("#taskWindow").width()) + "px");
				// $("#taskWindow").css("left", (offset.left - $("#taskWindow").width() + $(this).parent().width()) + "px");
				$("#taskWindow").css("top", offset.top + $(this).height() + "px");

				var bottomLimit = $(".left_box2").offset().top + $(".left_box2").height();
				var taskBottom = Number($("#taskWindow").css("top").replace("px","")) + $("#taskWindow").height();

				if (taskBottom > bottomLimit) { 
					$("#taskWindow").css("top", Number($("#taskWindow").css("top").replace("px","")) - (taskBottom - bottomLimit) + "px");
				}
				$("#taskWindow").show();
			}); 
			
	    },
	    gridComplete : function() {
	    }
	});
}

$('#searchKey, #searchpoliy').keyup(function(e) {
	if (e.keyCode == 13) {
		 search_policy();
    }        
});

$("#btnSearch").click(function(e){
	 search_policy();
})

function search_policy(){
	var postData = {name: $('#searchKey').val(), policy_name : $('#searchpoliy').val()}
	$("#topNGrid").setGridParam({url:"<%=request.getContextPath()%>/search/getPolicy", postData : postData, datatype:"json" }).trigger("reloadGrid");
}

function setDetails(rowid){
	var policy_name = $('#topNGrid').getCell(rowid, 'NAME');
	var type = $('#topNGrid').getCell(rowid, 'TYPE1');
	var comment = $('#topNGrid').getCell(rowid, 'COMMENT');
	var enabled = $('#topNGrid').getCell(rowid, 'ENABLED');
	var action = $('#topNGrid').getCell(rowid, 'ACTION');
	var view_user = $('#topNGrid').getCell(rowid, 'VIEW_USER');
	var policy_type = $('#topNGrid').getCell(rowid, 'POLICY_TYPE');
	var scanTrace = $('#topNGrid').getCell(rowid, 'RECON_TRACE');
	
	var cpu = $('#topNGrid').getCell(rowid, 'RECON_CPU');
	var memory = $('#topNGrid').getCell(rowid, 'RECON_MEMORY');
	var throughput = $('#topNGrid').getCell(rowid, 'THROUGHPUT_RATE');
	var server_cnt = $('#topNGrid').getCell(rowid, 'SERVER_CNT');
	var server = $('#topNGrid').getCell(rowid, 'SERVER');
	//alert(policy_name)
	$('#left_policy_name').text(policy_name);
	$('#right_policy_name').text(policy_name);
	$('#right_datatype_name').text(type);
	$('#right_datatype_name').text(type);
	$('#view_server').val(server);
	
	var view_user_detail = "";
	view_user_detail = '<label style="position: relative;">서버운영 관계자</label>';

	$('#accesUser').val(view_user.replace(/ /gi, "").replace(/,/gi, "\n"));
	$('#view_user').html(view_user_detail);
	$('#right_td_save').html('<button class="btn_down" type="button" onclick="btn_modify(\''+rowid+'\')">수정/삭제</button> ');
	$('#datatype_id').val($('#topNGrid').getCell(rowid, 'DATATYPE_ID'))
	$('#std_id').val($('#topNGrid').getCell(rowid, 'STD_ID'));
	$('#idx').val($('#topNGrid').getCell(rowid, 'IDX'))
	$('#btn_datatype_area').html('')
	
	// 개인정보 유형 
	var datatype = setDatatype($('#topNGrid').getRowData(rowid));
	
	$('#left_datatype').html(datatype);
	
	var cycle = setCycle($('#topNGrid').getRowData(rowid));
	$('#right_cycle').html(cycle);
	
	$('#right_enable').attr('disabled', 'disabled');	// 전체 공개
	$('#right_trace').attr('disabled', 'disabled'); 	// ScanTrace Log 
	
	if(scanTrace == 1){
		$('#right_trace').css('display', '');
		$('#right_trace').prop('checked', 'checked');
	}else {
		$('#right_trace').css('display', '');
		$('#right_trace').prop('checked', '');
	}
	
	if(policy_type == 1) {
		if(enabled == 1){
			$('#right_enable').css('display', '');
			$('#right_enable').prop('checked', 'checked');
		}else {
			$('#right_enable').css('display', '');
			$('#right_enable').prop('checked', '');
		}
		$("#action").css('display', 'none');
	}else {
		$('#right_enable').css('display', 'none');
		$('#right_enable').prop('checked', '');
		$("#action").css('display', '');
	}
	
	if(cpu == 1){
		$("#right_cpu").html("Normal");
	}else{
		$("#right_cpu").html("Low");
	}
	
	if(memory != null && memory != ""){ 
		$("#right_memory").html(memory+" MB");
	}else{
		$("#right_memory").html("");
	} 
	
	if(throughput != null && throughput != ""){
		$("#right_throughput").html(throughput+" MBps");
	}else{
		$("#right_throughput").html("");
	}
	$('#view_server_cnt').html(server_cnt+"대 "+' <button id="btn_view_server" style="position: relative; top: -2px; left: 5px;" class="btn_down" type="button" onclick="viewServer(0)">조회</button> ');

	$("input:checkbox[name='action']").prop("checked", false);
	$("input:checkbox[name='action'][value='"+action+"']").prop("checked", true);
	$("input:checkbox[name='action']").attr("disabled", "disabled");
	
	openedWindows.forEach(function(win, index) {
        if (win && !win.closed) {
            win.close();
        }
    });
}
function getFormatDate(oDate)
{
    var nYear = oDate.getFullYear();           // yyyy 
    var nMonth = (1 + oDate.getMonth());       // M 
    nMonth = ('0' + nMonth).slice(-2);         // month 두자리로 저장 action

    var nDay = oDate.getDate();                // d 
    nDay = ('0' + nDay).slice(-2);             // day 두자리로 저장

    return nYear + '-' + nMonth + '-' + nDay;
}
function setStartdtm(rowData){
	var start_dtm = rowData.START_DTM;
	var start_ymd = '';
	var start_hour = '';
	var start_minutes = '';
	if(start_dtm != null && start_dtm != ''){
		start_ymd = start_dtm.substr(0,10);
		start_hour = start_dtm.substr(11,2);
		start_minutes = start_dtm.substr(14,2);
	} else {
		var oToday = new Date();
		start_ymd = getFormatDate(oToday);
	}
	
	var html = "";
	html += "<input type='date' id='start_ymd' style='text-align: center; height: 27px;' value='"+start_ymd+"' disabled='disabled'>&nbsp;";

	html += "<select name=\"start_hour\" id=\"start_hour\" disabled='disabled'>"
	for(var i=0; i<24; i++){
		var hour = (parseInt(i));
		html += "<option value=\""+hour+"\" "+(hour == parseInt(start_hour)? 'selected': '')+">"+hour+"</option>"
	}
	html += "</select> : "

	html += "<select name=\"start_minutes\" id=\"start_minutes\" disabled='disabled'>"
	for(var i=0; i<60; i++){
		var minutes = parseInt(i)
		html += "<option value=\""+minutes+"\" "+(minutes == parseInt(start_minutes)? 'selected': '')+">"+minutes+"</option>"
	}
	html += "</select>"
	return html;
}


function setStopdtm(rowData){
	var pause_from = rowData.PAUSE_FROM;
	var pause_from_minu = rowData.PAUSE_FROM_MINU;
	var pause_to = rowData.PAUSE_TO;
	var pause_to_minu = rowData.PAUSE_TO_MINU;
	var start_hour = '';
	var start_minutes = '';
	var stop_hour = '';
	var stop_minutes = '';
	var chk = 0;
	var sel_chk = 0;
	if(pause_from != null && pause_from != ''){
		start_hour = pause_from / 60 / 60;
		start_minutes = pause_from_minu / 60;
		stop_hour = pause_to / 60 / 60;
		stop_minutes = pause_to_minu / 60;
		sel_chk = 1;
	}
	
	if(rowData != null && rowData != ''){
		chk = 1;
	}
	
	var html = "";
	html += "<input type='checkbox' "+(sel_chk == 1 ?  'checked' : '') +" id='stop_chk'  "+(chk == 1 ?  'disabled' : '' ) +"/>&nbsp;&nbsp;시작 : &nbsp;";

	html += "<select name=\"start_hour\" id=\"from_time_hour\" disabled='disabled'>"
	for(var i=0; i<24; i++){
		var hour = (parseInt(i));
		html += "<option value=\""+hour+"\" "+(hour == parseInt(start_hour)? 'selected': '')+">"+hour+"</option>"
	}
	html += "</select> : "

	html += "<select name=\"start_minutes\" id=\"from_time_minutes\" disabled='disabled'>"
	for(var i=0; i<60; i++){
		var minutes = parseInt(i)
		html += "<option value=\""+minutes+"\" "+(minutes == parseInt(start_minutes)? 'selected': '')+">"+minutes+"</option>"
	}
	html += "</select>"
	html += "&nbsp;&nbsp;~&nbsp;&nbsp;정지 : &nbsp;";

	html += "<select name=\"start_hour\" id=\"to_time_hour\" disabled='disabled'>"
	for(var i=0; i<24; i++){
		var hour = (parseInt(i));
		html += "<option value=\""+hour+"\" "+(hour == parseInt(stop_hour)? 'selected': '')+">"+hour+"</option>"
	}
	html += "</select> : "

	html += "<select name=\"start_minutes\" id=\"to_time_minutes\" disabled='disabled'>"
	for(var i=0; i<60; i++){
		var minutes = parseInt(i)
		html += "<option value=\""+minutes+"\" "+(minutes == parseInt(stop_minutes)? 'selected': '')+">"+minutes+"</option>"
	}
	html += "</select>"

	
	return html;
}


function setCycle(rowData){
	var cycle = rowData.CYCLE;
	
	var html = "";
	html += "<select name=\"cycle\" id=\"cycle\" disabled=\"disabled\">"
	html += "	<option value=\"0\" "+((cycle == 0)? 'selected': '')+">한번만</option>"
	html += "	<option value=\"1\" "+((cycle == 1)? 'selected': '')+">매일</option>"
	html += "	<option value=\"2\" "+((cycle == 2)? 'selected': '')+">매주</option>"
	html += "	<option value=\"4\" "+((cycle == 4)? 'selected': '')+">2주마다</option>"
	html += "	<option value=\"3\" "+((cycle == 3)? 'selected': '')+">매월</option>"
	html += "	<option value=\"5\" "+((cycle == 5)? 'selected': '')+">매분기</option>"
	html += "	<option value=\"6\" "+((cycle == 6)? 'selected': '')+">6개월마다</option>"
	html += "	<option value=\"7\" "+((cycle == 7)? 'selected': '')+">매년</option>"
	html += "</select>"
	
	return html;
}

function setDatatype(rowData){

	var html = "";
	
	if(rowData != "" && rowData != null){
		if(rowData.DATATYPE != null && rowData.DATATYPE != ""){
			
			var dataList = JSON.parse(rowData.DATATYPE);
			html = "<table>";
			var trCnt = 0;
			
			 for(var i = 0; pattern.length > i; i++){
			
				 var chk = 0;
				 var cnt = 0;
				 var dup = 0;
				 var stats = 0;
				 
				 if(trCnt == 0){
					 html == "<tr>";
				 }
				 
				var row = pattern[i].split(', ');
				var ID = row[0].split('ID=').join(''); //변수 TABLE_NAME은 TB_NAME1, 다음 반복문에서는 TB_NAME2
				var PATTERN_NAME = row[1].split('PATTERN_NAME=').join(''); // 변수 CHK Y, 다음 반복문에서는 N
				var data_id = PATTERN_NAME.split('=')[1];
				var PATTERN_KR_NAME = ID.split('=')[1];
				
				html +="	<th style=\"width: 18%;\">"+PATTERN_KR_NAME+"</th>";
				stats = 0;
					for(j = 0 ; j < dataList.length ; j++){
						
						var typename = dataList[j].TYPE;
						
						if(data_id==typename){
							cnt = dataList[j]._CNT;
							dup = dataList[j]._DUP; 
							chk = dataList[j]._CHK; 
							stats = 1;
						
							break;
						}
					}
					html +="	<td style=\"width: 15%;\"><input type='checkbox' disabled='disabled' "+((chk == 1)?"checked='checked'":"")+">&nbsp;";
					html +="	<input type='checkbox' disabled='disabled' "+((dup == 1)?"checked='checked'":"")+">&nbsp;";
					html += 	(chk==1)? cnt : '';
					html += "	</td>";
					
					++trCnt;
				 if(trCnt % 3 == 0){
					 html += "</tr>";
					 trCnt = 0;
				 }
			}
		}else{
			html = "";
		}
	}
	
	return html;
}

function btn_modify(rowid){
	
	//setCss(1);
	//alert(rowid)
	var rowData = $('#topNGrid').getRowData(rowid);
	//console.log(rowData);
	//$('#left_datatype').find('input').removeAttr("disabled");
	$('#right_days').find('input').removeAttr("disabled");
	$('#cycle').removeAttr("disabled");
	$('#right_enable').removeAttr("disabled");
	$('#right_trace').removeAttr("disabled");
	
	/* $('#right_comment').html("<input type='text' id='comment' value='"+rowData['COMMENT']+"'/>"); */
	$('#right_policy_name').html("<input placeholder='정책명을 입력하세요.' style='padding-left: 5px;' type='text' id='policy_name' value='"+rowData['NAME']+"'/>");
	
	var btn_area = "<button class='btn_down' style='height:26px' type='button' onclick='modifyPolicy()'>수정</button>&nbsp;";
	btn_area += "<button class='btn_down' style='height:26px; margin-right: 2px;' type='button' onclick='deletePolicy(\""+rowData['IDX']+"\")'>삭제</button>";
	btn_area += "<button class='btn_down' style='height:26px' type='button' onclick='setDetails(\""+rowid+"\")'>취소</button>";
	$('#right_td_save').html(btn_area);
	//$('#right_start_time').find('*').removeAttr("disabled");
	//$('#right_stop_time').find('*').removeAttr("disabled");
	$("input:checkbox[name='action']").removeAttr("disabled");
	
	$('#btn_datatype_area').html('<button class="btn_down" style="font-size: 11px; height: 27px;" type="button" onclick="btn_datatype(\''+rowid+'\')">개인정보유형변경</button>');
	
	
	var btn_acess = '<button type="button" onClick="btnAccessSave()">저장</button> ';
	btn_acess += '<button type="button" onClick="btnAccessCancel('+rowid+')">취소</button>';

	$("#acesssBtn").html(btn_acess);
	$('#accesUser').removeAttr("disabled");
	 
	$('#right_cpu').html('<input type="radio" name="sch_cpu" id="cpu_low" value="0"> Low <input type="radio" id="cpu_normal" name="sch_cpu" value="1"> Normal');
	$('#right_memory').html("<input type='text'placeholder='미설정 시 기본값은 1024 MB 입니다.' id='policy_memory' style='padding-left: 5px; width:224px;'/ > MB");
	$('#right_throughput').html("<input type='text'placeholder='미설정 시 기본값은 무제한 입니다' id='policy_throughput'style='padding-left: 5px; width:224px;'/> MBps");
	
	var	view_user_content = '<div id="selectUser" style=" padding-right: 5px; float:left;">';
		view_user_content += '<label for="check_server" style="padding-right: 5px; line-height: 26px;">서버</label>' ;
	 	
	$("#view_user").html(view_user_content);
	
	$('#view_server_cnt').html(rowData['SERVER_CNT']+"대 "+' <button id = "btn_view_server" style="position: relative; top: -2px; left: 5px;" class="btn_down" type="button" onclick="viewServer(1)">수정</button> ');
	
	if(rowData.POLICY_TYPE == 1){
		$("input:radio[name='acessUser']:radio[value='server']").prop("checked", true);
		$("#action").css('display', 'none');
	}else if(rowData.POLICY_TYPE == 2){
		$("input:radio[name='acessUser']:radio[value='pc']").prop("checked", true);
		$("#action").css('display', '');
		$("#accessible_pc_btn").show();
	} 

	if(rowData.RECON_CPU == 0 ){
		$("#cpu_low").prop("checked", true);
	}else{
		$("#cpu_normal").prop("checked", true);
	}
	
	$("#policy_memory").val(rowData.RECON_MEMORY);  
	$("#policy_throughput").val(rowData.THROUGHPUT_RATE);
	
	openedWindows.forEach(function(win, index) {
        if (win && !win.closed) {
            win.close();
        }
    });
	
}

function accessible_server(){
	$("#accessible_pc_btn").hide();
	$("#accessible_pc").hide();
	$("#action").css('display', 'none');
	$("input:checkbox[name='action']").each(function() {
	      if(this.value == 0){//checked 처리된 항목의 값
	    	  this.checked = true; //checked 처리
	      } else {
	    	  this.checked = false; //checked 처리
	      }

	});
	$("#right_enable").css('display', '');
	$('#right_enable').prop('checked', '');
	$("#accessible_server").show();
}
function accessible_pc_btn(){
	$("#accessible_pc_btn").show();
	$("#accessible_pc").show();
	$("#action").css('display', '');
	$("#right_enable").css('display', 'none');
	$('#right_enable').prop('checked', '');
	$("#accessible_server").hide();
}


function btn_datatype(rowid){
	//alert(rowid);
	datatypeGrid();
	var postData = {name : ''};
	$("#datatypeGrid").setGridParam({url:"<%=request.getContextPath()%>/search/getProfile", postData : postData, datatype:"json" }).trigger("reloadGrid");
	$('#datatype_pop').show();
}

function btn_userSelect(){
	//alert(rowid);
	
	$("#accesUser").attr("disabled", "disabled");
	$("#acesssBtn").html("<button type='button' onClick='btnAccessExit()'>닫기</button>");
	$('#accessUserPopup').show();
	
}

//정책 접근 가능 유저 닫기 버튼
function btnAccessExit(){
	$("#accessUserPopup").hide();
}

//정책 접근 가능 유저 저장 버튼
function btnAccessSave(){
	$("#accessUserPopup").hide();
}

//정책 접근 가능 유저 취소 버튼
function btnAccessCancel(rowid){
	var view_user = $('#topNGrid').getCell(rowid, 'VIEW_USER');
	
	if($('#accesUser').val().trim() != ""  && view_user != null){
		$('#accesUser').val(view_user.replace(/ /gi, "").replace(/,/gi, "\n"));
	}
	
	if(view_user == null){
		$('#accesUser').val("");
	}

	$("#accessUserPopup").hide();
}

function btn_userUpdate(){
	//var view_user = $('#topNGrid').getCell(rowid, 'VIEW_USER');
	
	//console.log(view_user);
	//$('#accesUser').text(view_user.replace(/ /gi, "").replace(/,/gi, "\n"));
	
	$('#accessUserPopup').show();
}


$('#btnCancelChangeDatatype').on('click', function(){
	$('#datatype_pop').hide();
})

$('#btnCancleDataTypePop').on('click', function(){
	$('#datatype_pop').hide();
})

$('#btnSaveDatatype').on('click', function(){
	/* console.log($("input:radio[name=gridRadio]:checked").val())
	console.log($("input:radio[name=gridRadio]:checked").data('rowid')) */
	var rowid = $("input:radio[name=gridRadio]:checked").data('rowid');
	var rowData = $('#datatypeGrid').getRowData(rowid);
	var html = setDatatype(rowData)
	//console.log()
	
	$('#left_datatype').html(html);
	$('#right_datatype_name').text(rowData.DATATYPE_LABEL);
	$('#datatype_id').val(rowData.DATATYPE_ID);
	$('#std_id').val(rowData.STD_ID);
	$('#datatype_pop').hide();
})

$('#btn_new').on('click', function(){
	$('#right_policy_name').html("<input type='text'placeholder='정책명을 입력하세요.' id='policy_name' value='' style='padding-left: 5px;'/>");
	$('#left_policy_name').text('');
	$('#right_datatype_name').text('');
	$('#btn_datatype_area').html('<button class="btn_down" style="font-size: 11px; height: 27px;" type="button" onclick="btn_datatype(\'\')">개인정보유형변경</button>');
	$('#right_cpu').html('<input type="radio" name="sch_cpu" id="cpu_low" value="0" checked="checked"> Low <input type="radio" id="cpu_normal" name="sch_cpu" value="1"> Normal');
	$('#right_memory').html("<input type='text'placeholder='미설정 시 기본값은 1024 MB 입니다.' id='policy_memory' value='' style='padding-left: 5px; width:224px;'/ > MB");
	$('#right_throughput').html("<input type='text'placeholder='미설정 시 기본값은 무제한 입니다' id='policy_throughput' style='padding-left: 5px; width:224px;'/> MBps");
	
	var	view_user_content = '<div id="selectUser" style=" padding-right: 5px; float:left;">'; 
		view_user_content += '<label for="check_server" style="padding-right: 5px; line-height: 26px;">서버</label>' ;
	$('#view_user').html(view_user_content);
	$('#right_td_save').html('<button class="btn_down" type="button" style="height: 27px;" onclick="btn_insertPolicy()">생성</button> ');
	$('#datatype_id').val('');
	$('#std_id').val('');
	$("#accesUser").val('');
	$('#right_trace').removeAttr("disabled");
	$('#right_trace').css('display', '');
	$('#right_trace').prop('checked', '');
	$('#right_enable').removeAttr("disabled");
	$('#right_enable').css('display', '');
	$('#right_enable').prop('checked', '');
	var btn_acess = '<button type="button" onClick="btnAccessSave()">저장</button> ';
	btn_acess += '<button type="button" onClick="btnAccessCancel()">취소</button>';

	$("#acesssBtn").html(btn_acess);
	$('#accesUser').removeAttr("disabled");
	
	$("#action").css('display', '');
	$("input:checkbox[name='action']").removeAttr("disabled");

	$("input:checkbox[name='action']").each(function() {
	      if(this.value == 0){//checked 처리된 항목의 값
	    	  this.checked = true; //checked 처리
	      } else {
	    	  this.checked = false; //checked 처리
	      }

	});
	
	$('#left_datatype').html('');
	
	$('#right_cycle').html('');
	$('#left_disable').text('');
	
	var cycle = "";
	cycle += "<select name=\"cycle\" id=\"cycle\" >"
	cycle += "	<option value=\"0\">한번만</option>"
	cycle += "	<option value=\"1\">매일</option>"
	cycle += "	<option value=\"2\">매주</option>"
	cycle += "	<option value=\"4\">매 2주</option>"
	cycle += "	<option value=\"3\">매월</option>"
	cycle += "	<option value=\"5\">매분기</option>"
	cycle += "	<option value=\"6\">6개월마다</option>"
	cycle += "	<option value=\"7\">매년</option>"
	cycle += "</select>"
	$('#right_cycle').html(cycle);
	
	$('#view_server').val('[]');
	$('#view_server_cnt').html("0대 "+' <button id="btn_input_server" style="position: relative; top: -2px; left: 5px;" class="btn_down" type="button" onclick="viewServer(1)">입력</button> ');
})

function deletePolicy(idx){
	var datatype_id = $("#datatype_id").val();
	var policy_name = $('#policy_name').val();
	
	var postData = {
		idx: idx,
		policy_name: policy_name,
		datatype_id : datatype_id
	};
	var message = "정말 삭제하시겠습니까?";
	if (confirm(message)) {
		$.ajax({
			type: "POST",
			url: "/search/deletePolicy",
			async : false,
			data : postData,
		    success: function (resultMap) {
		        if (resultMap.resultCode == 0) {
		        	alert("삭제를 완료하였습니다.");
		        	search_policy();
		        	initDetails();
			    } else if(resultMap.resultCode == -9) {
			        alert("삭제 할 수 없는 정책 입니다.");
			    } else {
			        alert("삭제를 실패하였습니다.");
			    }
		    },
		    error: function (request, status, error) {
				alert("Server Error : " + error);
		        console.log("ERROR : ", error);
		    }
		});
	}
}

function modifyPolicy(){
	var comment = $('#comment').val();
	var policy_name = $('#policy_name').val();
	/* 
	var start_ymd = $('#start_ymd').val();
	var start_hour = $('#start_hour').val();
	var start_minutes = $('#start_minutes').val(); 
	*/
	var cycle = $('#cycle').val();
	var action = 0;
	var datatype_id = $('#datatype_id').val();
	var std_id = $('#std_id').val();
	//var pause_chk = ($('#stop_chk').is(':checked')? '1': '0');
	var pause_chk = 0;
	var enabled = ($('#right_enable').is(':checked')? '1': '0');
	var idx = $('#idx').val();
	
	//var start_dtm = start_ymd + ' ' + String(start_hour) + ':' + String(start_minutes)
	var start_dtm = "2022-01-01 23:59";
	var scanTrace = ($('#right_trace').is(':checked')? '1': '0');
	
	var from_time_hour = null;
	var from_time_minutes = null;
	var to_time_hour = null;
	var to_time_minutes = null;
	var pause_days = null;
	var userArr = new Array();
	var policy_type = null;

	var cpu = $("input[name='sch_cpu']:checked").val();
	var memory = $("#policy_memory").val();
	var throughput = $("#policy_throughput").val();
	
	if(policy_name == ""){
		alert("정책명을 입력해주세요.");
		return;
	}
	
	if(datatype_id == ""){
		alert("개인정보 유형을 선택하여주세요.");
		return;
	}
	
	if(pause_chk == 1) {
		from_time_hour = $('#from_time_hour').val() * 60 * 60;
		from_time_minutes = $('#from_time_minutes').val() * 60;
		to_time_hour = $('#to_time_hour').val() * 60 * 60;
		to_time_minutes = $('#to_time_minutes').val() * 60;
		
		var from_time = from_time_hour + from_time_minutes;
		var to_time = to_time_hour + to_time_minutes;
		
		if(from_time == to_time) {
			alert("검색 정지 시작 시간과 끝 시간을 다르게 설정해주세요");
			return;
		}
		pause_days = 62;
	}
	
	$("input:checkbox[name='action']").each(function() {
	   if(this.checked){//checked 처리된 항목의 값
	   	action = this.value;
	   } 
	});
	
	// 정책 접근 가능 유저 = 서버
	policy_type = 1;
	action = 0;
	
	//적용 서버
	var serverList = $("#view_server").val();
	
	var postData = {
		idx: idx,
		policy_name: policy_name,
		comment: comment,
		start_dtm: start_dtm,
		cycle: cycle,
		datatype_id: datatype_id,
		std_id: std_id,
		enabled: enabled,
		from_time_hour: from_time_hour,
		from_time_minutes: from_time_minutes,
		to_time_hour: to_time_hour,
		to_time_minutes: to_time_minutes,
		pause_days: pause_days,
		action: action,
		user: userArr.join(", "),
		policy_type: policy_type,
		cpu : cpu,
		memory : memory,
		scanTrace : scanTrace,
		throughput : throughput,
		serverList : serverList
	};
	 
	var message = "수정하시겠습니까?";
	
	if (confirm(message)) {
		$.ajax({
			type: "POST",
			url: "/search/modifyPolicy",
			async : false,
			data : postData,
		    success: function (resultMap) {
		        if (resultMap.resultCode == 0) {
		        	alert("수정하였습니다.");
		        	<%-- var postData = {};
		        	$("#targetGrid").setGridParam({url:"<%=request.getContextPath()%>/search/getProfile", postData : postData, datatype:"json" }).trigger("reloadGrid"); --%>
		        	search_policy();
		        	initDetails();
			    } else {
			        alert("실패하였습니다.");
			    } 
		    },
		    error: function (request, status, error) {
				alert("Server Error : " + error);
		        console.log("ERROR : ", error);
		    }
		});
	}
	
}

function btn_insertPolicy(){
	var comment = $('#comment').val();
	var policy_name = $('#policy_name').val();
	/* var start_ymd = $('#start_ymd').val();
	var start_hour = $('#start_hour').val();
	var start_minutes = $('#start_minutes').val(); */
	var cycle = $('#cycle').val();
	var action = 0;
	var datatype_id = $('#datatype_id').val();
	var std_id = $('#std_id').val();
	var enabled = ($('#right_enable').is(':checked')? '1': '0');
	var scanTrace = ($('#right_trace').is(':checked')? '1': '0');
	//var pause_chk = ($('#stop_chk').is(':checked')? '1': '0');
	var pause_chk = 0;
	var from_time_hour = null;
	var from_time_minutes = null;
	var to_time_hour = null;
	var to_time_minutes = null;
	var pause_days = null;
	var userArr = new Array();
	
	var cpu = $("input[name='sch_cpu']:checked").val();
	var memory = $("#policy_memory").val();
	var throughput = $("#policy_throughput").val();
	
	if(policy_name == ""){
		alert("정책명을 입력해주세요.");
		return;
	}
	
	if(datatype_id == ""){
		alert("개인정보 유형을 선택하여주세요.");
		return;
	}
	
	if(pause_chk == 1) {
		from_time_hour = $('#from_time_hour').val() * 60 * 60;
		from_time_minutes = $('#from_time_minutes').val() * 60;
		to_time_hour = $('#to_time_hour').val() * 60 * 60;
		to_time_minutes = $('#to_time_minutes').val() * 60;
		
		var from_time = from_time_hour + from_time_minutes;
		var to_time = to_time_hour + to_time_minutes;
		
		if(from_time == to_time) {
			alert("검색 정지 시작 시간과 끝 시간을 다르게 설정해주세요");
			return;
		}
		pause_days = 62;
	}
	
	//var start_dtm = start_ymd + ' ' + String(start_hour) + ':' + String(start_minutes)
	var start_dtm = "2022-01-01 23:59";
	
	$("input:checkbox[name='action']").each(function() {
	   if(this.checked){//checked 처리된 항목의 값
	   	action = this.value;
	   } 
	});
	
	policy_type = 1;
	action = 0;
	
	//적용 서버
	var serverList = $("#view_server").val();
	
	var postData = {
		policy_name: policy_name,
		comment: comment,
		start_dtm: start_dtm,
		cycle: cycle,
		datatype_id: datatype_id,
		std_id: std_id,
		enabled: enabled,
		from_time_hour: from_time_hour,
		from_time_minutes: from_time_minutes,
		to_time_hour: to_time_hour,
		to_time_minutes: to_time_minutes,
		pause_days: pause_days,
		action: action,
		user: userArr.join(", "),
		policy_type: policy_type,
		cpu : cpu,
		memory : memory,
		scanTrace : scanTrace,
		throughput : throughput,
		serverList : serverList
	};
	
	//console.log(postData);
	var message = "신규정책을 생성하시겠습니까?";
	if (confirm(message)) {
		$.ajax({
			type: "POST",
			url: "/search/insertPolicy",
			async : false,
			data : postData,
		    success: function (resultMap) {
		        if (resultMap.resultCode == 0) {
		        	alert("생성하였습니다.");
		        	var postData = {};
		        	$("#targetGrid").setGridParam({url:"<%=request.getContextPath()%>/search/getProfile", postData : postData, datatype:"json" }).trigger("reloadGrid");
		        	search_policy();
		        	initDetails();
			    } else {
			        alert("생성하였습니다.");
			    }
		    },
		    error: function (request, status, error) {
				alert("Server Error : " + error);
		        console.log("ERROR : ", error);
		    }
		});
	}
}

function initDetails(){
	$('#left_policy_name').text('');
	$('#left_datatype').html('');
	$('#left_disable').html('');
	 
	$('#right_cpu').html('');
	$('#right_memory').html('');
	$('#right_throughput').html('');
	
	/* $('#right_comment').html(''); */
	$('#right_policy_name').html('');
	$('#right_policy_name').html('');
	$('#right_datatype_name').html('');
	//$('#right_start_time').html('');
	//$('#right_stop_time').html('');
	$('#btn_datatype_area').html('');
	$('#right_cycle').html('');
	$('#view_user').html('');
	$('#view_server').val('');
	$('#view_server_cnt').html('');
	$('#right_enable').prop("checked", "");
	$('#right_enable').css("display", "none");
	$('#right_trace').css("display", "none");

	$('#right_td_save').html('');
	$("input:checkbox[name='action']").attr("disabled", "disabled");
	
	$("input:checkbox[name='action']").each(function() {
	      if(this.value == 0){//checked 처리된 항목의 값
	    	  this.checked = true; //checked 처리
	      } else {
	    	  this.checked = false; //checked 처리
	      }

	});
}

var createType = function(cellvalue, options, rowObject) {
	//return '<img src="/resources/assets/images/img_check.png" style="cursor: pointer;" name="gridSubSelBtn" class="gridSubSelBtn" value=" 선택 "></a>';
	return cellvalue != null ? cellvalue : "<span style=\"color:red\">미설정</span>"; 
};

$("input:checkbox[name='action']").on('change', function(){
	var value = $(this).val();
	$("input:checkbox[name='action']").prop("checked", false)
	
	$("input:checkbox[name='action'][value='"+value+"']").prop("checked", true)
})

function viewServer(flag){
	//console.log($("#view_server").val());
	var serverList = $("#view_server").val();
	console.log(serverList);
	
	var pop_url = "${getContextPath}/popup/scanHostList";
	var id = "scanHostList"
	var winWidth = 700;
	var winHeight = 580;
	var popupOption= "width="+winWidth+", height="+winHeight + ", left=0, top=0, scrollbars=no, resizable=no, location=no"; 	
	//var pop = window.open(pop_url,"lowPath",popupOption);
	var pop = window.open(pop_url,id,popupOption);
	/* popList.push(pop);
	sessionUpdate(); */
	openedWindows.push(pop);
	
	//pop.check();
	
	var newForm = document.createElement('form');
	newForm.method='POST';
	newForm.action=pop_url;
	newForm.name='newForm';
	//newForm.target='lowPath';
	newForm.target=id;
	
	var data = document.createElement('input');
	data.setAttribute('type','hidden');
	data.setAttribute('name','typeChk');
	data.setAttribute('value',0);
	newForm.appendChild(data);
	
	data = document.createElement('input');
	data.setAttribute('type','hidden');
	data.setAttribute('name','flag');
	data.setAttribute('value',flag);
	newForm.appendChild(data);
	
	data = document.createElement('input');
	data.setAttribute('type','hidden');
	data.setAttribute('name','serverList');
	data.setAttribute('value',serverList);
	newForm.appendChild(data);
	
	data = document.createElement('input');
	data.setAttribute('type','hidden');
	data.setAttribute('name','info');
	data.setAttribute('value',"search_policy");
	newForm.appendChild(data);
	
	document.body.appendChild(newForm);
	newForm.submit();
	
	document.body.removeChild(newForm);
	
	
}

/* function setCss(cssFlag){
	$(".policyPadding").css('padding','0px 5px');
	if(cssFlag ==1){
		$(".policyPadding").css('padding','0px');
	}

} */

</script>

</body>
</html>