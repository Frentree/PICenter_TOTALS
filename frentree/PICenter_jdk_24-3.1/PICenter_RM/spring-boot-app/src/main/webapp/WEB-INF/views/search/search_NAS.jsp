<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%@ include file="../../include/header.jsp"%>
<style>
.mxWindowPane .user_info td, .user_info th{
	height: 37.5px;
}
.user_info, .user_info_pc, .user_info_server{
	table-layout: fixed;
}
.user_info th {
	width: 18%;
}
.ui-jqgrid tr.ui-row-ltr td{
	cursor: pointer;
}
.stop_btn_css {
	background-color: #e4e4e4;
}
#datatype_area th, #datatype_area td {
	padding: 0;
}
@media screen and (-ms-high-contrast: active), (-ms-high-contrast: none) {
	.user_info_pc{
		bottom: 414px !important;
	}
	.content_bottom{
		margin-top: 40px !important;
	}
	#policyBox{
		margin-top: 16px !important;
	}
	#scheduleNm, #scheduleSKTNm{
		width: 230px !important;
	}
}
</style>
		<!-- section -->
		<section>
			<!-- container -->
			<div class="container">
			<h3 style="display: inline; top: 25px;">NAS검색 실행</h3>
			<!-- <p class="container_comment" style="position: absolute; top: 32px; left: 147px; font-size: 13px; color: #9E9E9E;">개인정보 검출을 위해 대상 서버 선택 후 정책리스트에서 정책을 선택 후 실행 메뉴를 클릭 하시길 바랍니다.</p> -->

				<!-- content -->
				<div class="content magin_t35">
					
					<div class="content_top">
						<h3 style="display: inline; top: 5px; padding: 0;"><input type="radio" style="margin-bottom: 5px;" name="category" id="category_server" value="server" checked="checked"/> 서버</h3>
						<p style="position: absolute; top: 90px; left: 147px; font-size: 13px; color: #9E9E9E;">호스트명 [연결 서버] | 서버 IP | 마지막 검색 완료 시간</p>
						<table class="user_info_server narrowTable" style="display: inline-block; width: 280px; min-width: 0; float: right;">	
							<caption>타겟정보</caption>
	                  			<tbody>
	                  				<tr>
                  					 <th style="text-align: center; width: 0px; border-radius: 0.25rem;">검색어</th>
	                   			     <td style="width:50%">
	                      				 <input type="text" style="width: 147px; padding-left: 5px;" size="10" id="assetServerHostsSeach" placeholder="검색어를 입력하세요.">
	                      			 </td>
	                      			 <td style="width:10%;">
	                          			 <input type="button" name="button" class="btn_look_approval" id="assetServerHostsBtn" style="margin-top: 5px;">
	                          			</td>
	                  				</tr>
	                  			</tbody>
						</table> 
						<div class="left_box" style="height:673px; width: 100%; margin-top: 10px;" id="div_server" >
							<div id="server_tree">
							</div>
						</div>
					</div>
					<input type="hidden" id="datatype_id" value=""/>
					<input type="hidden" id="std_id" value=""/>
					<input type="hidden" id="idx" value=""/>
					<div class="grid_top" style="width: 50%; height: 311px; float: right; padding-left: 20px;">
						<h3 class="policy_list" style="padding: 0; top: 6px;">정책 리스트</h3>
						<div class="left_box2"
							style="overflow: hidden; margin-top: 4px; height: 92%;">
							<table id="searchGrid"></table>
							<div id="searchGridPager"></div>
						</div>
						<h3 class="policy_info" style="padding: 0; top: 0px; height: 28px;">정책 상세 정보</h3>
						<table class="user_info"
							style="width: 100%; margin-top: 8px; margin-bottom: 10px; height: 314px;">
							<caption>정책 상세 정보</caption>
							<colgroup>
								<col width="15%">
								<col width="3%">
								<col width="*">
							</colgroup>
							<tbody>
								<tr>
									<th class="borderB" style="border-top-left-radius: 0.25rem;">정책명</th>
									<td>-</td>
									<td id="policy_name"></td>
								</tr>
								<tr>
									<th class="borderB">검색 시작 시간</th>
									<td>-</td>
									<td id="start_time"></td>
								</tr> 
								<tr>  
									<th class="borderB" rowspan="2" style="height: 75px;">검색 정지 시간</th>
									<td rowspan="2">-</td>
									<td id="stop_time"></td>
								</tr>
								<tr> 
									<td id="stop_day" style="display: none;">
										<button class="btn_down stop_btn" type="button" id="stop_sun" name="btnSave" style="width: auto;">일</button>
										<input type="checkbox" style="display: none;" name="stop_chk_box"; value="sun"; id="check_sun">
										<button class="btn_down stop_btn" type="button" id="stop_mon" name="btnSave" style="width: auto;">월</button>
										<input type="checkbox" style="display: none;" name="stop_chk_box" value="mon"; id="check_mon">
										<button class="btn_down stop_btn" type="button" id="stop_tue" name="btnSave" style="width: auto;">화</button>
										<input type="checkbox" style="display: none;" name="stop_chk_box" value="tue"; id="check_tue">
										<button class="btn_down stop_btn" type="button" id="stop_wed" name="btnSave" style="width: auto;">수</button>
										<input type="checkbox" style="display: none;" name="stop_chk_box" value="wed"; id="check_wed">
										<button class="btn_down stop_btn" type="button" id="stop_thu" name="btnSave" style="width: auto;">목</button>
										<input type="checkbox" style="display: none;" name="stop_chk_box" value="thu"; id="check_thu">
										<button class="btn_down stop_btn" type="button" id="stop_fri" name="btnSave" style="width: auto;">금</button>
										<input type="checkbox" style="display: none;" name="stop_chk_box" value="fri"; id="check_fri">
										<button class="btn_down stop_btn" type="button" id="stop_sat" name="btnSave" style="width: auto;">토</button>
										<input type="checkbox" style="display: none;" name="stop_chk_box" value="sat"; id="check_sat">
									</td>
								</tr>
								<tr>
									<th class="borderB" style="height: 81px;">개인정보 유형</th>
									<td>-</td>
									<td>
										<div id="datatype_area" style="height: 77px; overflow-y: auto; "></div>
									</td>
								</tr>
								<tr id="policyAction" style="display: none;">
									<th class="borderB">검출시 동작</th>
									<td>-</td>
									<td id="action"><input type="checkbox" name="action"
										value="0" checked="checked" />선택없음&nbsp; <input type="checkbox"
										name="action" value="1" />즉시 삭제&nbsp; <input type="checkbox"
										name="action" value="2" />즉시 암호화&nbsp; <input type="checkbox"
										name="action" value="3" />익일 삭제&nbsp; <input type="checkbox"
										name="action" value="4" />익일 암호화</td>
								</tr>
								<tr>
									<th class="borderB" >주기</th>
									<td>-</td>
									<td id="cycle">
								</tr>
								<tr style="height: 30px; border-bottom: none;">
									<th class="borderB" >scanTrace</th>
									<td>-</td>
									<td class="policyPadding">
										<input type="checkbox" style="display: none;" id="right_trace"/>
									</td>
								</tr>
								<tr style="display: none;">
									<th class="borderB" style="height: 121px;">cpu</th>
									<td>-</td>
									<td id="datatype_CPU">
								</tr>
								<tr style="display: none;">
									<th class="borderB" style="height: 121px;">memory</th>
									<td>-</td>
									<td id="datatype_memory">
								</tr>
								<tr style="display: none;">
									<th class="borderB" style="height: 121px;">throughput</th>
									<td>-</td>
									<td id="datatype_throughput">
								</tr>
							</tbody>
						</table>
						<div class="grid_top" style="width: 100%;">
							<button class="btn_down" type="button" id="btnStartScan"
								name="btnSave" style="width: 100%;">실행</button>
						</div>
					</div>
				</div>
			</div>

	<!-- 팝업창 - 스케줄명 입력  -->
	<div id="scheduleNamePopup" class="popup_layer" style="display:none">
		<div class="popup_box" style="height: 150px;  width: 400px; left: 55%;top: 65%; padding: 10px; background: #f9f9f9;">
		<img class="CancleImg" id="btnCancleScheduleNamePopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
			<div class="popup_top;" style="background: #f9f9f9;">
				<h1 style="color: #222; padding: 0; box-shadow: none; font-size: 20px; font-weight: 500;">스케줄명 입력</h1>
			</div>
			<div class="popup_content">
				<div class="content-box" style="height: 65px; background: #fff; border: 1px solid #c8ced3;">
					<!-- <h2>세부사항</h2>  -->
					<table class="popup_tbl">
						<colgroup>
							<col width="30%">
							<col width="*">
						</colgroup>
						<tbody>
							<tr>
								<th>스케줄명</th>
								<td><input style="width: 238px; padding-left: 10px;" type="text" id="scheduleNm" value="" class="edt_sch" placeholder="스케줄명을 입력하세요."></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="popup_btn">
				<div class="btn_area">
					<button type="button" id="btnScheduleName">확인</button>
					<button type="button" id="btnScheduleNameCancel">취소</button>
				</div>
			</div>
		</div>
	</div>
	<!-- 팝업창 - 스케줄명 입력  -->
	
	<!-- 팝업창 - 스케줄명 입력(정직원 즉시실행)  -->
	<div id="scheduleSKTNamePopup" class="popup_layer" style="display:none">
		<div class="popup_box" style="height: 150px;  width: 400px; left: 55%;top: 65%; padding: 10px; background: #f9f9f9;">
		<img class="CancleImg" id="btnCancleScheduleSKTNamePopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
			<div class="popup_top;" style="background: #f9f9f9;">
				<h1 style="color: #222; padding: 0; box-shadow: none; font-size: 20px; font-weight: 500;">스케줄명 입력</h1>
			</div>
			<div class="popup_content">
				<div class="content-box" style="height: 65px; background: #fff; border: 1px solid #c8ced3;">
					<!-- <h2>세부사항</h2>  -->
					<table class="popup_tbl">
						<colgroup>
							<col width="30%">
							<col width="*">
						</colgroup>
						<tbody>
							<tr>
								<th>스케줄명</th>
								<td><input style="width: 238px; padding-left: 10px;" type="text" id="scheduleSKTNm" value="" class="edt_sch" placeholder="스케줄명을 입력하세요."></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="popup_btn">
				<div class="btn_area">
					<button type="button" id="btnSKTScheduleName">확인</button>
					<button type="button" id="btnSKTScheduleNameCancel">취소</button>
				</div>
			</div>
		</div>
	</div>
	<!-- 팝업창 - 스케줄명 입력  -->
		</section>
		<!-- section -->
<%@ include file="../../include/footer.jsp"%>

<script type="text/javascript">
var aut = "manager"
var pattern = '${pattern}'.split('[{').join('').split('}]').join('');
pattern = pattern.split('}, {');

$(document).ready(function () {
	fn_drawSerchGrid();
	var postData = {scheduleUse : 2}
	$("#searchGrid").setGridParam({url:"<%=request.getContextPath()%>/search/getPolicy", postData : postData, datatype:"json" }).trigger("reloadGrid");
	

	$("input:checkbox[name='action']").attr("disabled", "disabled");
	
	var grade = ${memberInfo.USER_GRADE};
	var gradeChk = [0, 1, 2, 3, 7];
	
	var helpIcon = '<img alt="" src="${pageContext.request.contextPath}/resources/assets/images/help_icon_1.png" style="width: 24px; position: absolute; top: 30px; left: 722px; cursor: pointer;" id="userHelpIcon">';
	$(".container_comment").after(helpIcon);
	
	$("#userHelpIcon").on("click", function(e) {
		/* $("#userHelpPopup").show(); */
		var id = "search_regist";
		var pop_url = "${getContextPath}/popup/helpDetail";
    	var winWidth = 1140;
    	var winHeight = 655;
    	var popupOption= "width="+winWidth+", height="+winHeight + ", left=0, top=0, scrollbars=no, resizable=no, location=no"; 	
    	//var pop = window.open(pop_url,"detail",popupOption);
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
	
	$(document).on('click', '.stop_btn', function() {
		
		var buttonId = $(this).attr('id'); // 클릭된 버튼의 id 값을 가져옵니다.
	    $(this).toggleClass('stop_btn_css');
	    var checkBoxId = buttonId.replace('stop_', 'check_');
	     
	    // 해당 체크박스의 선택 상태를 토글
	    $('#' + checkBoxId).prop('checked', function(i, val) {
	        return !val;
	    });
	});
	
});
$(function() {

	var resetting_server = false;
	var grade = ${memberInfo.USER_GRADE};
	var gradeChk = [0, 1, 2, 3];
	$('#server_tree').jstree({
	// List of active plugins
		"ui": {
                    "select_limit": -1,
                    "select_multiple_modifier": "alt", //중복 선택시 사용할 키
                    "selected_parent_close": "select_parent",
                    "initially_select": ["phtml_2"]
                },


		"core" : {
		    "animation" : 0,
		    "check_callback" : true,
			"themes" : { "stripes" : false },
			"data" : ${serverGroupList}
		},
		"types" : {
            "types": {
            	"disabled" : { 
                  "check_node" : false, 
                  "uncheck_node" : false 
                } 
            }
        },
        'search': {
	        'case_insensitive': false,
	        'show_only_matches' : true,
	        "show_only_matches_children" : true
	    },
		'plugins' : ["checkbox", "changed", "search"],
	}).bind('select_node.jstree', function(evt, data, x) {
		$("#category_server").prop("checked", true);
		$("#category_pc").prop("checked", false);
		
		var id = data.node.id;
		var type = data.node.data.type;
		var parents = data.node.parents;
		
		var check = '';
		
		var pLength = parents.length;
		
		if(pLength < 2){
			check = id;
		} else {
			check = parents[(pLength -2)];
		}
		var sel_node;
		if(check == 'group'){
			sel_node = $('#server_tree').jstree(true).get_node('server');
		} else {
			sel_node = $('#server_tree').jstree(true).get_node('group');
		}
		
	    data.instance.deselect_node( [sel_node.children] );
		
		if(gradeChk.indexOf(grade) == -1){
			$(".policy_list").css('display', 'block').css('padding','0');
			$(".left_box2").css('display', 'block');
			$(".policy_info").css('display', 'block');
			$(".user_info").css('display', 'table');
			$("#btnStartScan").css('display', 'block').css('position', 'initial');
		}
		$("#policyAction").hide();
	 }).bind("loaded.jstree", function(e, data) {
	    	var nodes = $('#server_tree').jstree(true).get_json('#', { 'flat': true });
	    	
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
	          
	    	$("#assetServerHostsSeach").autocomplete({
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
		 
	 });
});

function fn_drawSerchGrid() {

	var colNames = [];
	var colModel = [];
	var patternCnt = '${patternCnt}';
	
	var gridWidth = $("#searchGrid").parent().width();
	/* 이름 */
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
		colNames.push('개인정보 데이터','','정책명','개인정보 유형', 'datatype_id','sdt_id', 'comment', 'cycle', 'action', 'enabled', 'start_dtm', 'from_hour', 'from_minu', 'to_hour', 'to_minu', 'recent', 'recon_cpu', 'recon_memory', 'throughput', 'server', 'recon_trace');
	
	/* 데이터 */
	for(var i = 0; pattern.length > i; i++){
		
		var row = pattern[i].split(', ');
		var ID = row[0].split('ID=').join(''); //변수 TABLE_NAME은 TB_NAME1, 다음 반복문에서는 TB_NAME2
		var PATTERN_NAME = row[1].split('PATTERN_NAME=').join(''); // 변수 CHK Y, 다음 반복문에서는 N
		var data_id = PATTERN_NAME.split('=')[1];
		
		colModel.push({ index: data_id+"_CHK", 		name: data_id+"_CHK", 			width: 40, align: 'left', hidden: true});
		colModel.push({ index: data_id, 			name: data_id, 					width: 40, align: 'left', hidden: true});;
		colModel.push({ index: data_id+"_CHK_COPY", name: data_id+"_CHK_COPY", 		width: 40, align: 'left', hidden: true});
		colModel.push({ index: data_id+"_CNT", 		name: data_id+"_CNT", 			width: 40, align: 'left', hidden: true});
		colModel.push({ index: data_id+"_DUP", 		name: data_id+"_DUP", 			width: 40, align: 'left', hidden: true});
	}
	
		colModel.push({ index: 'DATATYPE', 			name: 'DATATYPE', 	editable: true, width: 200, hidden: true });
		colModel.push({ index: 'IDX', 			name: 'IDX', 	editable: true, width: 200, hidden: true });
		colModel.push({ index: 'NAME', 			name: 'NAME', 	editable: true, width: 200 });
		colModel.push({ index: 'TYPE', 			name: 'TYPE', 	width: 200});
		colModel.push({ index: 'DATATYPE_ID', 	name: 'DATATYPE_ID', 	width: 200, hidden: true});
		colModel.push({ index: 'STD_ID', 			name: 'STD_ID', 	width: 200, hidden: true});
		colModel.push({ index: 'COMMENT', 		name: 'COMMENT', 	width: 200, hidden: true});
		colModel.push({ index: 'CYCLE', 			name: 'CYCLE', 	width: 200, hidden: true});
		colModel.push({ index: 'ACTION', 			name: 'ACTION', 	width: 200, hidden: true});
		colModel.push({ index: 'ENABLED', 		name: 'ENABLED', 	width: 200, hidden: true});
		colModel.push({ index: 'START_DTM', 		name: 'START_DTM', 	width: 200, hidden: true});
		colModel.push({ index: 'PAUSE_FROM', 		name: 'PAUSE_FROM', 	width: 200, hidden: true});
		colModel.push({ index: 'PAUSE_FROM_MINU', name: 'PAUSE_FROM_MINU', 	width: 200, hidden: true});
		colModel.push({ index: 'PAUSE_TO', 		name: 'PAUSE_TO', 	width: 200, hidden: true});
		colModel.push({ index: 'PAUSE_TO_MINU', 	name: 'PAUSE_TO_MINU', 	width: 200, hidden: true});
		colModel.push({ index: 'RECENT', 			name: 'RECENT', 	width: 200, hidden: true});
		colModel.push({ index: 'RECON_CPU', 		name: 'RECON_CPU', 	width: 200, hidden: true});
		colModel.push({ index: 'RECON_MEMORY', 		name: 'RECON_MEMORY', 	width: 200, hidden: true});
		colModel.push({ index: 'THROUGHPUT_RATE', 	name: 'THROUGHPUT_RATE', 	width: 200, hidden: true});
		colModel.push({ index: 'SERVER', 			name: 'SERVER', 	width: 200, hidden: true});
		colModel.push({ index: 'RECON_TRACE', 		name: 'RECON_TRACE', 	width: 60, hidden: true});
	
	
	$("#searchGrid").jqGrid({
		datatype: "local",
	   	mtype : "POST",
		colNames:colNames,
		colModel: colModel,
		loadonce:true,
	   	autowidth: true,
		shrinkToFit: true,
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: gridWidth,
		height: 200,
		rownumbers : false, // 행번호 표시여부
		rownumWidth : 35, // 행번호 열의 너비	
		rowNum:25,
		rowList:[25,50,100],
		pager: "#searchGridPager",
		//jqgrid의 특성상 - rowpos의 이벤트는 onSelectCell, beforeEditCell 다 해주어야 함
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {
	  		var rowData = $(this).getRowData(rowid);
	  		
	  		setDetails(rowid);
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
}

function setDetails(rowid){
	var policy_name = $('#searchGrid').getCell(rowid, 'NAME');
	var type = $('#searchGrid').getCell(rowid, 'TYPE');
	var comment = $('#searchGrid').getCell(rowid, 'COMMENT');
	var enabled = $('#searchGrid').getCell(rowid, 'ENABLED');
	var action = $('#searchGrid').getCell(rowid, 'ACTION');
	
	var cpu = $('#searchGrid').getCell(rowid, 'RECON_CPU');
	var memory = $('#searchGrid').getCell(rowid, 'RECON_MEMORY');
	var throughput = $('#searchGrid').getCell(rowid, 'THROUGHPUT_RATE');
    var scanList = JSON.parse($('#searchGrid').getCell(rowid, 'SERVER'));
    var scanTrace = JSON.parse($('#searchGrid').getCell(rowid, 'RECON_TRACE'));
	
	//alert(policy_name)
	$('#policy_name').text(policy_name);
	$('#datatype_name').text(type);
	$('#datatype_id').val($('#searchGrid').getCell(rowid, 'DATATYPE_ID'))
	$('#std_id').val($('#searchGrid').getCell(rowid, 'STD_ID'))
	$('#idx').val($('#searchGrid').getCell(rowid, 'IDX'))
	$('#datatype_area').html('')
	
	$('.stop_btn').removeClass('stop_btn_css');
	$('input[name="stop_chk_box"]').prop('checked', false);
	$("#stop_day").show(); 
	
	var start_dtm = setStartdtm($('#searchGrid').getRowData(rowid))
	$('#start_time').html(start_dtm);
	
	$("#start_ymd").datepicker({
		changeYear : true,
		changeMonth : true,
		dateFormat: 'yy-mm-dd'
	});
	
	$("#datatype_throughput").val(throughput);
	$("#datatype_memory").val(memory);
	$("#datatype_CPU").val((cpu == 1 ? 'normal' : 'low'));
	
	if(scanTrace == 1){
		$('#right_trace').css('display', '');
		$('#right_trace').prop('checked', 'checked');
	}else {
		$('#right_trace').css('display', '');
		$('#right_trace').prop('checked', '');
	}

	var stop_dtm = setStopdtm($('#searchGrid').getRowData(rowid))
	$('#stop_time').html(stop_dtm);
	
	// 개인정보 유형 
	var datatype = setDatatype($('#searchGrid').getRowData(rowid))
	$('#datatype_area').html(datatype);
	
	var cycle = setCycle($('#searchGrid').getRowData(rowid));
	$('#cycle').html(cycle);
	
	$("input:checkbox[name='action']").prop("checked", false);
	$("input:checkbox[name='action'][value='"+action+"']").prop("checked", true);

	for(var i=0;i<scanList.length;i++){
        $('#server_tree').jstree('select_node', scanList[i]);
    }
	
}

function setStartdtm(rowData){
	var today = new Date();
	today.setMinutes(today.getMinutes() + 7);
	var hours = today.getHours();
	var minutes = today.getMinutes();
	const year = today.getFullYear(); 
	const month = today.getMonth() + 1; 
	const date = today.getDate();

	var start_dtm = rowData.START_DTM;
	var start_ymd = '';
	var start_hour = '';
	var start_minutes = '';
	
	var html = "";
	html += "<input type='date' id='start_ymd' min style='text-align: center; height: 27px;' readonly='readonly' value='"+
	(year+"-"+(month >= 10 ? month : '0' + month) + "-" + (date >= 10 ? date : '0' + date))+"' >&nbsp;";
	
	html += "<select name=\"start_hour\" id=\"start_hour\" >"
	for(var i=0; i<24; i++){
		var hour = (parseInt(i));
		var str_hour = (parseInt(hour) < 10) ? '0'+hour : hour
		html += "<option value=\""+hour+"\" "+(hour == parseInt(hours)? 'selected': '')+">"+(hour < 10 ? "0" + hour : hour)+"</option>"
	}
	html += "</select> : "

	html += "<select name=\"start_minutes\" id=\"start_minutes\" >"
	for(var i=0; i<60; i++){
		var minute = parseInt(i)
		var str_minutes = (parseInt(minutes) < 10) ? '0'+minute : minute
		html += "<option value=\""+minute+"\" "+(minute == parseInt(minutes)? 'selected': '')+">"+(minute < 10 ? "0" + minute : minute)+"</option>"
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
	start_hour = 9;
	stop_hour = 18;
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
	//html += "<input type='checkbox' "+(sel_chk == 1 ?  'checked' : '') +" id='stop_chk'  "+(chk == 1 ?  'disabled' : '' ) +"/>&nbsp;&nbsp;시작 : &nbsp;";
	html += "<input type='checkbox' id='stop_chk' />&nbsp;&nbsp;시작 : &nbsp;";

	//html += "<select name=\"start_hour\" id=\"from_time_hour\" disabled='disabled'>"
	html += "<select name=\"start_hour\" id=\"from_time_hour\">"
	for(var i=0; i<24; i++){
		var hour = (parseInt(i));
		html += "<option value=\""+hour+"\" "+(hour == parseInt(start_hour)? 'selected': '')+">"+(hour < 10 ? "0" + hour : hour)+"</option>"
	}
	html += "</select> : "

	html += "<select name=\"start_minutes\" id=\"from_time_minutes\">"
	for(var i=0; i<60; i++){
		var minutes = parseInt(i)
		html += "<option value=\""+minutes+"\" "+(minutes == parseInt(start_minutes)? 'selected': '')+">"+(minutes < 10 ? "0" + minutes : minutes)+"</option>"
	}
	html += "</select>"
	html += "&nbsp;&nbsp;~&nbsp;&nbsp;정지 : &nbsp;";

	html += "<select name=\"start_hour\" id=\"to_time_hour\">"
	for(var i=0; i<24; i++){
		var hour = (parseInt(i));
		html += "<option value=\""+hour+"\" "+(hour == parseInt(stop_hour)? 'selected': '')+">"+(hour < 10 ? "0" + hour : hour)+"</option>"
	}
	html += "</select> : "

	html += "<select name=\"start_minutes\" id=\"to_time_minutes\">"
	for(var i=0; i<60; i++){
		var minutes = parseInt(i)
		html += "<option value=\""+minutes+"\" "+(minutes == parseInt(stop_minutes)? 'selected': '')+">"+(minutes < 10 ? "0" + minutes : minutes)+"</option>"
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
			html = "<table style=\"width: 90%;\">";
			var trCnt = 0;
			
			 for(var i = 0; pattern.length > i; i++){
			
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
			 
			html += "	<th>증분검사</th>";
			html += "	<td><input type='checkbox' disabled='disabled' "+((rowData.RECENT > 0 )?"checked='checked'":"")+">&nbsp;";
			html += "	" + ((rowData.RECENT > 0 )? rowData.RECENT :"") ;
			html += "	</td>"
			html += "</tr>"
			html += "</table>"
		}else{
			html = "";
		} 
	}
	
	return html;
}

function selectGroup() {
	
	var pop_url = "${getContextPath}/popup/groupList";
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
	data.setAttribute('name','hash_id');
	data.setAttribute('value',id);
	
	newForm.appendChild(data);
	document.body.appendChild(newForm);
	newForm.submit();
	
	document.body.removeChild(newForm);
	
}

/* 망 선택 팝업 */
function selectNet() {
	
	var pop_url = "${getContextPath}/popup/netList";
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
	data.setAttribute('name','hash_id');
	data.setAttribute('value',id);
	
	newForm.appendChild(data);
	document.body.appendChild(newForm);
	newForm.submit();
	
	document.body.removeChild(newForm);
	
}
/* 망 선택 팝업 종료 */


/* 타겟리스트 팝업 */
function selectHost() {
	
	var pop_url = "${getContextPath}/popup/targetList";
	var id = "targetList"
	var winWidth = 700;
	var winHeight = 570;
	var popupOption= "width="+winWidth+", height="+winHeight + ", left=0, top=0, scrollbars=no, resizable=no, location=no"; 	
	//var pop = window.open(pop_url,"lowPath",popupOption);
	var pop = window.open(pop_url,id,popupOption);
	/* popList.push(pop);
	sessionUpdate(); */
	
	//pop.check();
	
	var newForm = document.createElement('form');
	newForm.method='POST';
	newForm.action=pop_url;
	newForm.name='newForm';
	//newForm.target='lowPath';
	newForm.target=id;
	
	var data = document.createElement('input');
	data.setAttribute('type','hidden');
	data.setAttribute('name','hash_id');
	data.setAttribute('value',id);
	
	newForm.appendChild(data);
	document.body.appendChild(newForm);
	newForm.submit();
	
	document.body.removeChild(newForm);
	
}

/* 타겟리스트 팝업 종료 */
function findByPop(){
	console.log('selected')
}

function getDateFormat(date) {
    var month = date.getMonth() + 1;
    var day = date.getDate();
    var hour = date.getHours();
    var minute = date.getMinutes();
    var second = date.getSeconds();

    month = month >= 10 ? month : '0' + month;
    day = day >= 10 ? day : '0' + day;
    hour = hour >= 10 ? hour : '0' + hour;
    minute = minute >= 10 ? minute : '0' + minute;
    second = second >= 10 ? second : '0' + second;

    return date.getFullYear() + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second;
}

$('input:radio[name=category]').on('click', function(){
	var radio = $('input:radio[name=category]');
	
	var radioBtnCheck = $("input[name='category']:checked").val();
		
	if(radioBtnCheck == "pc"){
		$("#policyAction").show();
	}else{
		$("#policyAction").hide();
	}
	
	var id = $(this).val();
	$.each(radio, function(key, value){
		//console.log($(value).val());
		var chk_id = $(value).val();
		if (id == $(value).val()) {
			$('#'+chk_id+'_tree').on('select_node.jstree', function (e) {
			        e.preventDefault(); 
			        e.stopImmediatePropagation();
			}); 
		} else {
			$('#'+chk_id+'_tree').on('select_node.jstree', function (e) {
			        e.preventDefault(); 
			        e.stopImmediatePropagation();
			}); 
		}
	});
})

/* 검색 실행 */
$('#btnStartScan').on('click', function(){
	
	var category_id = $('input:radio[name=category]:checked').val();
	/* var tr_arr = $('input:checkbox[data-dataid='+category_id+']:checked');
	
	if(tr_arr.length == 0){
		alert("검색 실행할 서버 및 pc를 선택하여주세요.");
		return;
	}
	 */
	 if(category_id == "server" && $("#server_tree").jstree("get_checked",true).length == 0 ){
		 alert("검색 실행할 대상을 선택하여주세요.");
		 return;
	 }else if(category_id == "pc"&& $("#pc_tree").jstree("get_checked",true).length == 0 ){
		 alert("검색 실행할 대상을 선택하여주세요.");
		 return;
	 }
	if($('#datatype_id').val() == ""){
		alert("검색 실행할 정책을 선택하여주세요.");
		return;
	}
	
	$("#scheduleNm").val("");
	$("#scheduleNamePopup").show();
	
});


function getFormatDate(oDate)
{
    var nYear = oDate.getFullYear();           // yyyy 
    var nMonth = (1 + oDate.getMonth());       // M 
    nMonth = ('0' + nMonth).slice(-2);         // month 두자리로 저장 

    var nDay = oDate.getDate();                // d 
    nDay = ('0' + nDay).slice(-2);             // day 두자리로 저장

    return nYear+nMonth+nDay;
}

$("#btnScheduleName").on("click", function(e) {
	if($("#scheduleNm").val() == ""){
		alert("스케줄명을 입력해주세요");
		return;	
	}

	var scanLabel = "";
	
	var location = "";
	var period = $("select[name='cycle']").val();
	var repeat_days = 0;
	var repeat_months = 0;
	var datatypes = [];
	var action = 0;
	
	/*
	0 == 한번만
	1 == 매일
	2 == 매주
	4 == 매 2주
	3 == 매월
	5 == 매분기
	6 == 6개월마다
	7 == 매년
	*/
	
	switch(period){
		case "0":
			repeat_days = 0;
			repeat_months = 0;
			break;
		case "1":
			repeat_days = 1;
			repeat_months = 0;
			break;
		case "2":
			repeat_days = 7;
			repeat_months = 0;
			break;
		case "4": //매2주
			repeat_days = 14;
			repeat_months = 0;
			break;
		case "3":
			repeat_days = 0;
			repeat_months = 1;
			break;
		case "5": //매분기
			repeat_days = 0;
			repeat_months = 3;
			break;
		case "6": //6개월 마다
			repeat_days = 0;
			repeat_months = 6;
			break;
		case "7": //매년
			repeat_days = 0;
			repeat_months = 12;
			break;
		default:
			repeat_days = 0;
			repeat_months = 0;
			break;
	}
	
	var cpu = $("#datatype_CPU").val();
	var throughput = $("#datatype_throughput").val();
	var memory = $("#datatype_memory").val();
// 	var trace = true;
	var trace = ($('#right_trace').is(':checked')? true: false);
	var timezone = "Default";
	var capture = true;
	var pause_chk = ($('#stop_chk').is(':checked')? '1': '0');
	// minimum, balanced, maximum
	var match_detail = "minimum";
	
	var pause = {};
	if(pause_chk == 1) {
		pause.start = ($("#from_time_hour").val()* 60 * 60) + ($("#from_time_minutes").val()*60);
		pause.end = ($("#to_time_hour").val()* 60 * 60) + ($("#to_time_minutes").val() * 60);
		
		if(pause.start == pause.end) {
			alert("검색 정지 시작 시간과 끝 시간을 다르게 설정해주세요");
			return;
		}
		pause.days = 127;
	}
	
	var start_ymd = $('#start_ymd').val();
	var start_hour = $('#start_hour').val();
	var start_minutes = $('#start_minutes').val();
	
	var start = start_ymd.toString() + ' ' + start_hour.toString() + ':' + start_minutes.toString();
	var category_id = $('input:radio[name=category]:checked').val();
	
	// 타겟 별 개별 실행
	var scheduleArr = new Array();
	
	var thisDateTime = getDateTime(null, "mi", 1);
	var nowDate = thisDateTime.substring(0,4) + "-"
			+ thisDateTime.substring(4,6) + "-" 
			+ thisDateTime.substring(6,8) + " " 
			+ thisDateTime.substring(8,10) + ":" 
			+ thisDateTime.substring(10,12) + ":" 
			+ thisDateTime.substring(12,14);
			

			
	if(category_id == "server"){
		$.each($("#server_tree").jstree("get_checked",true),function(){
			var type = this.data.type;
			var id = this.id;
			
			console.log(this.data);
			
			if(type == 2){
				//var target_id = $(element).data("targetid");
				//var locationid = $(element).data("locationid");
				var name = this.data.name;
				var connectedIp = this.data.connected_ip;
				var targetid = this.data.target_id;
				var locationid = this.data.location_id;
				var ap_no = this.data.ap;
				var core = this.data.core;
				var data = {};
				var targetsArr = new Array();
				var locationArr = new Array();
				
// 				// 보완관리자 외의 서버 담당자들 pause 08시 ~ 22시
// 				if(grade == 4 || grade == 5 || grade == 6) {
// 					var pauseArr = new Array();
					
// 					var pause = {};
// 					pause.days = 62;
// 					pause.start = 28800;
// 					pause.end = 79200;
					
// 					pauseArr.push(pause);
					
// 					data.pause = pauseArr;
// 				}
				
				var targets = {};
				var locations = {};
				
				data.label = name+"_"+nowDate;
				
				targets.id = targetid;
				
				locations.id = locationid;
				locationArr.push(locations);
				targets.locations = locationArr;
				targetsArr.push(targets);
				
				// target 등록
				data.targets = targetsArr;
				
				var profileArr = new Array();
				
				var postData = {
						datatype_id : $('#datatype_id').val(),
						std_id : $('#std_id').val(),
						ap_no  : this.data.ap
					};
				
				$.ajax({
					type: "POST",
					url: "/search/selectDataTypeId",
					async : false,
					data : postData,
				    success: function (resultMap) {
				    	
				    	if(resultMap.resultCode == 1){
				    		$('#datatype_id').val(resultMap.DATATYPE_ID);
					    	$('#std_id').val(resultMap.STD_ID);
				    	}else{
				    		alert("정책 조회에 실패하였습니다. \n관리자에게 문의 하십시오");
				    	}
				    },
				    error: function (request, status, error) {
						alert("Server Error : " + error);
						$("#scheduleNamePopup").hide();
				        console.log("ERROR : ", error);
				    }
				});
				profileArr.push($('#datatype_id').val())
				
				// target 등록
				data.profiles = profileArr;
				data.start = start;
				data.repeat_days = repeat_days;
				data.repeat_months = repeat_months;
				data.cpu = cpu;
				data.throughput = throughput;
				data.memory = memory;
				data.pause = pause;
				data.trace = trace;
				data.timezone = timezone;
				data.capture = capture;
				
				var param = {
					scheduleData : data,
					ap_no : ap_no,
					target_id : targetid,
					location_id: this.data.location_id,
					name : name,
					connectedIp: connectedIp
				}
				
				scheduleArr.push(param);
				
			}
		});
	} 
	/*
	var tr_arr = $('input:checkbox[data-dataid='+category_id+']:checked');
	
	
	 
	$.each(tr_arr, function(idx, element){
		//var target_id = $(element).data("targetid");
		//var locationid = $(element).data("locationid");
		
		var data = {};
		
		data.label = $(element).data("name")+"_"+nowDate;
		
		var targetsArr = new Array();
		var locationArr = new Array();
		
		var targets = {};
		var locations = {};
		targets.id = $(element).data("targetid");
		
		locations.id = $(element).data("locationid");
		locationArr.push(locations);
		targets.locations = locationArr;
		targetsArr.push(targets);
		
		// target 등록
		data.targets = targetsArr;
		
		var profileArr = new Array();
		profileArr.push($('#datatype_id').val())
		
		// target 등록
		data.profiles = profileArr;
		data.start = start;
		data.repeat_days = repeat_days;
		data.repeat_months = repeat_months;
		data.cpu = cpu;
		data.throughput = throughput;
		data.memory = memory;
		data.pause = pause;
		data.trace = trace;
		data.timezone = timezone;
		data.capture = capture;
		
		var param = {
			scheduleData : data,
			ap_no : $(element).data("apno"),
			target_id : $(element).data("targetid"),
		}
		
		scheduleArr.push(param);
	});*/
	var postData = {
		scheduleArr : JSON.stringify(scheduleArr),
		type : (category_id == 'server') ? 0 : (category_id == 'pc') ? 1 : 2,
		schedule_name : $("#scheduleNm").val(),
		policy_id : $('#idx').val(),
		datatype_id : $('#datatype_id').val(),
		std_id : $('#std_id').val(),
		action : action,
	};
	
	var message = "신규 스캔 스케줄을 등록하시겠습니까?";
	
	if (confirm(message)) {
		$.ajax({
			type: "POST",
			url: "/search/registSchedule",
			async : false,
			data : postData,
		    success: function (resultMap) {
				console.log(resultMap);
		        if (resultMap.resultCode == 201) {
			        alert("신규 스캔 스케줄이 등록되었습니다.");
			        window.location = "${pageContext.request.contextPath}/search/search_list";
			        $("#scheduleNamePopup").hide();
		        	return;
			    }
		        if (resultMap.resultCode == 409) {
			        alert("신규 스캔 스케줄 등록이 실패 되었습니다.\n\n스캔 스케줄명이 중복 되었습니다.");
			        $("#scheduleNamePopup").hide();
		        	return;
			    }
		        if (resultMap.resultCode == 422) {
		        	alert("신규 스캔 스케줄 등록이 실패 되었습니다.\n\n스케줄 시작시간을 확인 하십시요.");
		        	$("#scheduleNamePopup").hide();
		        	return;
			    }
		        alert("신규 스캔 스케줄이 등록이 실패 되었습니다.\n관리자에게 문의 하십시요");
		    },
		    error: function (request, status, error) {
				alert("Server Error : " + error);
				$("#scheduleNamePopup").hide();
		        console.log("ERROR : ", error);
		    }
		});
	}	 
});

$("#btnSKTScheduleName").on("click", function(e) {
	var category_id = $('input:radio[name=category]:checked').val();
	var date = new Date();
	var repeat_days = 0;
	var repeat_months = 0;
	var datatypes = [];
	var pause = {};
	date.setMinutes(date.getMinutes() + 5);
	
	var start = getDateFormat(date);
	var scheduleArr = new Array();
	var dataArr = new Array();
	
	var thisDateTime = getDateTime(null, "mi", 1);
	var nowDate = thisDateTime.substring(0,4) + "-"
		+ thisDateTime.substring(4,6) + "-" 
		+ thisDateTime.substring(6,8) + " " 
		+ thisDateTime.substring(8,10) + ":" 
		+ thisDateTime.substring(10,12) + ":" 
		+ thisDateTime.substring(12,14);
		
	if($("#scheduleSKTNm").val() == ""){
		alert("스케줄명을 입력해주세요.");
		return;
	}
	
	if(category_id == "pc"){
		
		$.each($("#pc_tree").jstree("get_checked",true),function(){
			
			var type = this.data.type;
			var id = this.id;
			var location = this.data.location;
			
			var idSplit = id.split('_onedrive');
			if(type == 1){
				
				net_type = this.data.net_type;
				var name = this.data.name;
				var locationid = this.data.location;
				var targetid = this.data.targets;
				var ap_no = this.data.ap;
				var datatype = "";
				
				var cpu = $("#datatype_CPU").val();
				var throughput = $("#datatype_throughput").val();
				var memory = $("#datatype_memory").val();
				
				var trace = true;
				var timezone = "Default";
				var capture = true;
				
				var data = {};
				var targetsArr = new Array();
				var locationArr = new Array();
				
				var targets = {};
				var locations = {};
				
				data.label = name+"_"+nowDate;
				
				targets.id = targetid;
				
				$.ajax({
					type: "POST",
					url: "/search/selectSKTScanDataTypes",
					async : false,
					data : {
						target_id: targetid,
						location_id : location,
	    				ap_no: ap_no,
	    				net_type : net_type
					},
				    success: function (resultMap) {
				    	if(resultMap.TRACE == "Y"){
				    		/* if(dayLabel == resultMap.DAY || resultMap.DAY == 9){
					    		trace = true;
				    		}else {
				    			trace = false;
				    		} */
				    		trace = true;
				    	}else{
				    		trace = false;
				    	}
				    	datatype = resultMap.DATATYPE_ID;
				    	if(datatype != "" && datatype != null){
				    		dataArr.push({
					    		"std_id" :resultMap.STD_ID,
					    		"policy_id" :resultMap.POLICY_ID,
					    		"action" :resultMap.ACTION,
					    		"datatype_id" :resultMap.DATATYPE_ID,
					    		"drm_status" :resultMap.DRM_STATUS
					    	});
				    		
				    	}
				    },
				    error: function (request, status, error) {
						alert("DataType Error : " + error);
				    }
				});
				
				if(datatype == "" || datatype == null){
					return true;
				}
				
				locations.id = locationid;
				locationArr.push(locations);
				targets.locations = locationArr;
				targetsArr.push(targets);
				
				// target 등록
				data.targets = targetsArr;
				
				var profileArr = new Array();
				profileArr.push(datatype);
				
				// target 등록
				data.profiles = profileArr;
				data.start = start;
				data.repeat_days = repeat_days;
				data.repeat_months = repeat_months;
				data.cpu = cpu;
				data.throughput = throughput;
				data.pause = pause;
				data.trace = trace;
				data.timezone = timezone;
				data.capture = capture;
				if(net_type == 3) {
					data.memory = memory;
				}
				
				var param = {
					scheduleData : data,
					ap_no : ap_no,
					target_id : targetid,
					location_id: this.data.location_id,
		    		"trace" : trace
				}
				
				scheduleArr.push(param);
				
				
							
			}
		});
		
		if(scheduleArr.length == 0){
			alert("선택하신 대상에 검색 정책이 설정되어있지 않습니다.");
			return;
		}
		
		var postData = {
			scheduleArr : JSON.stringify(scheduleArr),
			dataArr : JSON.stringify(dataArr),
			type : 1,
			net_type : net_type,
			schedule_name : $("#scheduleSKTNm").val(),
		};
		
		console.log("==================");
		console.log(postData);
		var message = "신규 스캔 스케줄을 등록하시겠습니까?";
		
		if (confirm(message)) {
			$.ajax({
				type: "POST",
				url: "/search/registPCSchedule",
				async : false,
				data : postData,
			    success: function (resultMap) {
					console.log(resultMap);
			        if (resultMap.resultCode == 201) {
				        alert("신규 스캔 스케줄이 등록되었습니다.");
				        window.location = "${pageContext.request.contextPath}/search/search_list";
				        $("#scheduleNamePopup").hide();
			        	return;
				    }
			        if (resultMap.resultCode == 409) {
				        alert("신규 스캔 스케줄 등록이 실패 되었습니다.\n\n스캔 스케줄명이 중복 되었습니다.");
				        $("#scheduleNamePopup").hide();
			        	return;
				    }
			        if (resultMap.resultCode == 422) {
			        	alert("신규 스캔 스케줄 등록이 실패 되었습니다.\n\n스케줄 시작시간을 확인 하십시요.");
			        	$("#scheduleNamePopup").hide();
			        	return;
				    }
			        alert("신규 스캔 스케줄이 등록이 실패 되었습니다.\n관리자에게 문의 하십시요");
			    },
			    error: function (request, status, error) {
					alert("Server Error : " + error);
					$("#scheduleNamePopup").hide();
			        console.log("ERROR : ", error);
			    }
			});
		}	 
	}
	
});

$("#btnScheduleNameCancel").on("click", function(e) {
	$("#scheduleNamePopup").hide();
});

$("#btnCancleScheduleNamePopup").on("click", function(e) {
	$("#scheduleNamePopup").hide();
});

$("#btnSKTScheduleNameCancel").on("click", function(e) {
	$("#scheduleSKTNamePopup").hide();
});

$("#btnCancleScheduleSKTNamePopup").on("click", function(e) {
	$("#scheduleSKTNamePopup").hide();
});

/* 검색 실행 종료 */

function btn_active(){
	$('#start_time').find('*').removeAttr("disabled");
	$('#cycle').find('*').removeAttr("disabled");
}

function scheduleStart(data){
	
	
}

var to = true;
$('#assetServerHostsBtn').on('click', function(){
    var v = $('#assetServerHostsSeach').val();
	
	if(to) { clearTimeout(to); }
    to = setTimeout(function () {
      $('#server_tree').jstree(true).search(v);
    }, 250);
});

$('#assetServerHostsSeach').keyup(function (e) {
	var v = $('#assetServerHostsSeach').val();
	if (e.keyCode == 13) {
		$(".ui-autocomplete").hide();
    	if(to) { clearTimeout(to); }
        to = setTimeout(function () {
          $('#server_tree').jstree(true).search(v);
        }, 250);
    }
});

$('#assetPCHostsBtn').on('click', function(){
    var v = $('#assetPCHostsSeach').val();
	
	if(to) { clearTimeout(to); }
    to = setTimeout(function () {
      $('#pc_tree').jstree(true).search(v);
    }, 250);
});

$('#assetPCHostsSeach').keyup(function (e) {
	var v = $('#assetPCHostsSeach').val();
	if (e.keyCode == 13) {
    	
    	if(to) { clearTimeout(to); }
        to = setTimeout(function () {
          $('#pc_tree').jstree(true).search(v);
        }, 250);
    }
});

$("input:checkbox[name='action']").on('change', function(){
	var value = $(this).val();
	$("input:checkbox[name='action']").prop("checked", false)
	
	$("input:checkbox[name='action'][value='"+value+"']").prop("checked", true)
});

$("#category_pc").on('change', function(){
	
	$(".container_comment").html("개인정보 검출을 위한 대상 장비 및 검출정책 선택 후 실행바랍니다.");
	$(".policy_list").css('display', 'none');
	$(".left_box2").css('display', 'none');
	$(".policy_info").css('display', 'none');
	$(".user_info").css('display', 'none');
	$("#btnStartScan").css('display', 'none');
	
	$("#userHelpIcon").css('left', '507px');
	
});

$("#category_server").on('change', function(){
	
	$(".container_comment").html("개인정보 검출을 위해 대상 서버 선택 후 정책리스트에서 정책을 선택 후 실행 메뉴를 클릭 하시길 바랍니다.");
	$(".policy_list").css('display', 'block').css('padding','0');
	$(".left_box2").css('display', 'block');
	$(".policy_info").css('display', 'block');
	$(".user_info").css('display', 'table');
	$("#btnStartScan").css('display', 'block').css('position', 'initial');
	
	$("#userHelpIcon").css('left', '742px');
	
});

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
	<!-- wrap -->
</body>
</html>
