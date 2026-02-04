<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="nowDate" class="java.util.Date" />
<%@ include file="../../include/header_rm.jsp"%>
<style>
.DeletionRegistT tr td {
	height: 20px;
	color: #9E9E9E;
	padding: 0 0 0 2px;
}

input {
	height: 27px;
}

#addProcessing * {
	margin-top: 4px;
}

#title_process_true span, #title_process_false span {
	font-size: 12px;
	color: #9E9E9E;
	padding-left: 5px;
}

.true_processing_comment span, .false_processing_comment span {
	font-size: 12px;
	color: #9E9E9E;
}

.true_processing_hint, .false_processing_hint {
	display: inline-block;
	position: relative;
	top: 2px;
}

.ui-jqgrid tr.ui-row-ltr td {
	cursor: pointer;
}

#approvalStatusNm {
	text-decoration: underline;
}

#approvalStatusNm:hover {
	font-weight: bold;
	cursor: pointer;
}

#path_detail_true::placeholder, #path_detail_false::placeholder {
	color: #9E9E9E;
}

#userHelpImg {
	background-size: auto;
}

@media screen and (-ms-high-contrast: active) , ( -ms-high-contrast :
	none) {
	.path_comment_div {
		position: relative;
		display: inline-block;
		bottom: 12px !important;
	}
	.path_comment {
		top: 0 !important;
	}
}


.custom-checkbox {
	text-align: center;
	padding: 0;
}

.custom-checkbox input[type="checkbox"] {
	margin: 0;
}


</style>

<!-- 검출관리 -->
<section>
	<div class="container">
		<h3 class="detection_list_title" style="display: inline; top: 25px;">결과 조회</h3>

		<div class="content magin_t35">
			<div class="grid_top" style="padding-left: 335px;">
				<table class="user_info" id="sch_detail" style="display: inline-table; width: 420px;">
					<caption>검색 결과 조회</caption>
					<tbody>
						<tr>
							<th style="text-align: center; width: 100px; border-radius: 0.25rem;">상세조회</th>
							<td>
								<input type="text" id="searchLocation" value="" class="edt_sch"
									style="width: 300px; height: 26.5px; padding-left: 5px;"
									placeholder="대상을 지정 후 경로 또는 파일명을 입력하세요.">
								<input type="hidden" id="hostSelect" value="">
								<input type="hidden" id="ap_no" value="">
								<input type="hidden" id="onedriveChk" value="">
							</td>
							<td>
								<input type="button" name="button" class="btn_route" id="btnSearch">
							</td>
						</tr>
					</tbody>
				</table>
				<div class="list_sch">
					<div class="sch_area" id="sch_area" style="margin-top: 7px;">
						<button type="button" name="button" class="btn_down" id="btnDownloadPCExcel" style="margin-top: 6px;">다운로드</button>
					</div>
				</div>
			</div>
			
			<div class="left_area2" style="position: absolute; top: 75px; height: 90%;">
				<table class="user_info narrowTable" style="width: 320px;">
					<tbody>
						<tr>
							<th style="text-align: center; border-radius: 0.25rem;">대상조회</th>
							<td>
								<input type="text" style="width: 205px; padding-left: 5px;" size="10"
									id="targetSearch" placeholder="호스트명을 입력하세요.">
							</td>
							<td>
								<input type="button" name="button" class="btn_look_approval" 
									id="btn_sch_target" style="margin-top: 5px;">
							</td>
						</tr>
					</tbody>
				</table>
				<div class="left_box2" style="max-height: 680px;">
					<div id="jstree" class="select_location"
						style="overflow-y: auto; overflow-x: auto; height: 659px; margin-top: 11px; background: #ffffff; border: 1px solid #c8ced3; white-space: nowrap;">
					</div>
				</div>
			</div>
			
			<div class="grid_top_PC" style="overflow: hidden; margin-left: 335px; height: 660px; max-height: 660px; margin-top: 10px;">
				<div id="targetPCGrid" class="ag-theme-balham" style="height: 660px; width: 100%;"></div>
				<div id="targetPCGridPager" style="height: 75px;"></div>
			</div>
		</div>
	</div>
</section>

<%
String browser = "";
String userAgent = request.getHeader("User-Agent");
%>

<!-- 팝업창 시작 하위 로케이션 상세정보 -->
<%
if (userAgent != null && (userAgent.indexOf("Trident") > 0 || userAgent.indexOf("MSIE") > 0)) {
%>
<div id="pathWindow" style="position: absolute; left: 300px; top: 350px; touch-action: none; width: 60%; height: 365px; z-index: 999; display: none; min-width: 35%; min-height: 200px; overflow-y: auto;" class="ui-widget-content">
	<table class="mxWindow" style="width: 100%; height: 100%;">
		<tbody>
			<tr>
				<td class="mxWindowTitle" style="cursor: grab; touch-action: none;">
					<table style="width: 100%; height: 36px;">
						<colgroup>
							<col width="*">
							<col width="30px">
						</colgroup>
						<tr>
							<td style="color: #ffffff; text-align: left; padding-left: 8px;"><h2>하위 경로 정보</h2></td>
							<td style="display: inline-block; padding-top: 6px; cursor: pointer;">
								<img src="${pageContext.request.contextPath}/resources/assets/images/close.gif" title="Close" id="pathWindowClose">
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td class="mxWindowPane">
					<div class="mxWindowPane" style="width: 100%; height: 88%; position: absolute; overflow: auto;">
						<table border="1" style="width: 100%; height: 100%;">
							<tbody>
								<tr>
									<td style="width: 100%; height: 100%;">
										<div id="pathContent" style="overflow-y: auto; height: 100%; padding: 5px 5px;">&nbsp;</div>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<%
} else {
%>
<div id="pathWindow" style="position: absolute; left: 300px; top: 350px; touch-action: none; width: 60%; height: 365px; z-index: 999; display: none; min-width: 35%; min-height: 200px;" class="ui-widget-content">
	<table class="mxWindow" style="width: 100%; height: 100%;">
		<tbody>
			<tr>
				<td class="mxWindowTitle" style="cursor: grab; touch-action: none;">
					<table style="width: 100%; height: 100%;">
						<colgroup>
							<col width="*">
							<col width="30px">
						</colgroup>
						<tr>
							<td style="color: #ffffff; text-align: left; padding-left: 8px;"><h2>하위 경로 정보</h2></td>
							<td style="display: inline-block; padding-top: 6px; cursor: pointer;">
								<img src="${pageContext.request.contextPath}/resources/assets/images/close.gif" title="Close" id="pathWindowClose">
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td class="mxWindowPane">
					<div class="mxWindowPane" style="width: 100%; height: 100%;">
						<table border="1" style="width: 100%; height: 100%;">
							<tbody>
								<tr>
									<td style="width: 100%; height: 100%;">
										<div id="pathContent" style="overflow-y: auto; height: 100%; padding: 5px 5px;">&nbsp;</div>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<%
}
%>
<!-- 팝업창 종료 -->

<!-- 팝업창 시작 개인정보검출 상세정보 -->
<%
if (userAgent != null && (userAgent.contains("Trident") || userAgent.contains("MSIE"))) {
%>
<div id="taskWindow" style="position: absolute; left: 340px; top: 350px; touch-action: none; width: 70%; height: 365px; z-index: 999; display: none; min-width: 30%; min-height: 200px;" class="ui-widget-content">
	<table class="mxWindow" style="width: 100%; height: 100%;">
		<tbody>
			<tr>
				<td class="mxWindowTitle" style="cursor: grab; touch-action: none;">
					<table style="width: 100%; height: 36px;">
						<colgroup>
							<col width="*">
							<col width="30px">
						</colgroup>
						<tr>
							<td style="color: #ffffff; text-align: left; padding-left: 8px;"><h2>개인정보검출 상세정보</h2></td>
							<td style="display: inline-block; padding-top: 6px; cursor: pointer;">
								<img src="${pageContext.request.contextPath}/resources/assets/images/close.gif" title="Close" id="taskWindowClose">
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td class="mxWindowPane">
					<div class="mxWindowPane" style="width: 100%; height: 100%;">
						<table border="1" style="width: 100%; height: 100%;">
							<tbody>
								<tr>
									<td id="matchCount" style="width: 195px; min-width: 195px; max-width: 195px; height: 50px; padding: 5px;">&nbsp;</td>
									<td style="width: 100%; height: 100%;" rowspan="2">
										<div id="bodyContents" style="background: white; overflow: scroll; height: 315px; padding: 5px 5px;">&nbsp;</div>
									</td>
								</tr>
								<tr>
									<td>
										<div id="matchData" style="background: white; overflow: scroll; height: 265px; padding: 5px">&nbsp;</div>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<%
} else {
%>
<div id="taskWindow" style="position: absolute; left: 650px; top: 350px; touch-action: none; width: 50%; height: 300px; z-index: 999; display: none; min-width: 30%; min-height: 200px;" class="ui-widget-content">
	<table class="mxWindow" style="width: 100%; height: 100%;">
		<tbody>
			<tr>
				<td class="mxWindowTitle" style="cursor: grab; touch-action: none;">
					<table style="width: 100%; height: 100%;">
						<colgroup>
							<col width="*">
							<col width="30px">
						</colgroup>
						<tr>
							<td style="color: #ffffff; text-align: left; padding-left: 8px;"><h2>개인정보검출 상세정보</h2></td>
							<td style="display: inline-block; padding-top: 6px; cursor: pointer;">
								<img src="${pageContext.request.contextPath}/resources/assets/images/close.gif" title="Close" id="taskWindowClose">
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td class="mxWindowPane">
					<div class="mxWindowPane" style="width: 100%; height: 100%;">
						<table border="1" style="width: 100%; height: 100%;">
							<tbody>
								<tr>
									<td id="matchCount" style="width: 195px; min-width: 195px; max-width: 195px; height: 50px; padding: 5px;">&nbsp;</td>
									<td style="width: 100%; height: 100%;" rowspan="2">
										<div id="bodyContents" style="overflow-y: auto; height: 100%; padding: 5px 5px;">&nbsp;</div>
									</td>
								</tr>
								<tr>
									<td>
										<div id="matchData" style="overflow-y: auto; height: 100%; padding: 5px">&nbsp;</div>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<%
}
%>
<!-- 팝업창 종료 -->

<!-- 나머지 팝업창들 (처리, 정탐처리, 오탐처리, 정탐, 오탐, 신청내역) -->
<div id="deletionRegistPopup" class="popup_layer" style="display: none">
	<div class="popup_box" style="width: 1200px; height: 600px; left: 37%; top: 47%; background: #f9f9f9; padding: 10px;">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 id="title_process" style="color: #222; box-shadow: none; padding: 0;"></h1>
		</div>
		<div class="popup_content">
			<div class="content-box" style="height: 500px; background: #fff; border: 1px solid #c8ced3;">
				<table class="popup_tbl">
					<colgroup>
						<col width="130">
						<col width="*">
					</colgroup>
					<tbody>
						<tr>
							<th style="text-align: center;">검출경로</th>
							<td style="padding: 5px;">
								<div id="path_exception_div" style="width: 100%; height: 400px; overflow: auto; layout: fixed;">
									<table id="path_exception" style="text-align: center; width: 100%;">
										<tbody></tbody>
									</table>
								</div>
							</td>
						</tr>
						<tr>
							<th style="text-align: center;">사유</th>
							<td>
								<label class="answerLabel">
									<input type="radio" class="answerRadio" name="trueFalseChk" id="selectReasonTrue" value="" class="edt_sch" style="position: relative; border: 0px solid #cdcdcd; width: 40px; height: 50px; margin-right: 20px;">
								</label>
								<label class="wrongLabel">
									<input type="radio" class="wrongRadio" name="trueFalseChk" id="selectReasonFalse" value="" class="edt_sch" style="position: relative; border: 0px solid #cdcdcd; width: 40px; height: 50px;">
								</label>
								<input type="hidden" id="selectedDate" value="">
								<input type="hidden" id="group_id" value="">
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area" style="padding-right: 0;">
				<button type="button" id="btnDeletionSave">저장</button>
				<button type="button" id="btnDeletionCancel">취소</button>
			</div>
		</div>
	</div>
</div>

<!-- 정탐/오탐 신청 내역 팝업 -->
<div id="insertPathExcepPopup" class="popup_layer" style="display: none">
	<div class="popup_box" style="height: 570px; top: 52%; left: 50%; padding: 10px; background: #f9f9f9;">
		<img class="CancleImg" id="btnCancleInsertPathExcepPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 id="groupName" style="color: #222; padding: 0; box-shadow: none;"></h1>
		</div>
		<div class="popup_content">
			<div class="content-box" style="height: 500px; background: #fff; border: 1px solid #c8ced3;">
				<table class="popup_tbl">
					<colgroup>
						<col width="130">
						<col width="*">
					</colgroup>
					<tbody>
						<tr>
							<th style="border-bottom: 1px solid #cdcdcd;">이름</th>
							<td style="border-bottom: 1px solid #cdcdcd;">
								<div style="overflow-y: auto; height: 280px;">
									<table style="border: 0px solid #cdcdcd; width: 430px; height: 266px; margin-top: 5px; margin-bottom: 5px; resize: none;">
										<tbody>
											<tr id="excepPath" style="border: none;"></tr>
										</tbody>
									</table>
								</div>
							</td>
						</tr>
						<tr>
							<th style="border-bottom: 1px solid #cdcdcd;">판단근거</th>
							<td style="border-bottom: 1px solid #cdcdcd;">
								<table style="border: 0px solid #cdcdcd; width: 430px; height: 90px; margin-top: 5px; margin-bottom: 5px; resize: none;">
									<tbody>
										<tr id="BasisName" style="border: none;"></tr>
									</tbody>
								</table>
							</td>
						</tr>
						<tr>
							<th style="border-bottom: 1px solid #cdcdcd;">사유</th>
							<td style="border-bottom: 1px solid #cdcdcd;">
								<input type="text" name="trueFalseChk" id="reason" value="" class="edt_sch" style="border: 0px solid #cdcdcd;" readonly>
							</td>
						</tr>
						<tr>
							<th style="border-bottom: 1px solid #cdcdcd;">등록서버</th>
							<td style="border-bottom: 1px solid #cdcdcd;">
								<input type="text" id="regisServer" value="" class="edt_sch" style="border: 0px solid #cdcdcd;" readonly>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area" style="padding: 10px 0; margin: 0;">
				<button type="button" id="btnCheck">확인</button>
			</div>
		</div>
	</div>
</div>

<%@ include file="../../include/footer.jsp"%>

<script type="text/javascript">
// 전역 변수들
var resetFomatter = null;
var SEQ = null;
var approval_status = null;
var targetPCGridApi = null;
// var targetPCColumnApi = null;

$(function() {
	var grade = ${memberInfo.USER_GRADE};
	
	$('#jstree').jstree({
		"core" : {
		    "animation" : 0,
		    "check_callback" : true,
			"themes" : { "stripes" : false },
			"data" : ${userGroupList}
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
		'search': {
	        'case_insensitive': false,
	        'show_only_matches' : true,
	        "show_only_matches_children" : true
	    },
		'plugins' : ["search"],
	})
    .bind('select_node.jstree', function(evt, data, x) {
    	var id = data.node.id;
    	var type = data.node.data.type;
    	var ap = data.node.data.ap;
    	var name = data.node.data.name;
    	var parents = data.node.parents;
    	
    	if(type == 1){
    		var id = id;
    		var name = data.node.text;
    		var ap_no = ap;
    		
    		if(data.node.parent == "onedirve"){
    			$("#onedriveChk").val(1);
    		} else {
    			$("#onedriveChk").val(0);
    		}
    		
    		$("#targetSearch").val(name);
    		$("#targetSearch").text(name);
    		$("#hostSelect").val(id);
    		$("#ap_no").val(ap_no);
    		
			findByPopPC();
    	}
    });
});


function checkboxRenderer(params) {
	var value = params.data.ID;
	var rowId = params.node.id;
	var isChecked = params.data.LEVEL == "0" ? "checked" : "";
	
	return '<div class="custom-checkbox">' +
		   '<input id="gridChk_' + rowId + '" type="checkbox" name="gridChk" value="' + value + '" ' +
		   'data-rowid="' + rowId + '" onchange="gridClick(event, ' + value + ')" ' + isChecked + '>' +
		   '</div>';
}

function pathRenderer(params) {
	return params.data.CHK == "1" ? ' > ' : '';
}

function fileNameRenderer(params) {
	var path = params.value;
	if(path && path.indexOf("_decrypted") != -1){
		return path.replaceAll("_decrypted", "");
	}
	return path || '';
}

function ownerRenderer(params) {
	return params.value || '';
}


function approvalStatusRenderer(params) {
	var approvalStatusNm = params.data.APPROVAL_STATUS_PRINT_NAME;
	if(approvalStatusNm != "" && approvalStatusNm != null){
		if (resetFomatter != "downloadClick") {
			return '<span id="approvalStatusNm" style="text-decoration: underline; cursor: pointer;">' + approvalStatusNm + '</span>';
		} else {
			return approvalStatusNm;
		}
	} else {
		return "";
	}
}


function createTimeRenderer(params) {
	var cellvalue = params.value;
	if(resetFomatter == "downloadClick"){
		return cellvalue;
	} else {
		return cellvalue ? '<p title="' + cellvalue + '">' + cellvalue.substr(0,10) + '</p>' : '';
	}
}

function numberFormatter(params) {
	if (params.value == null || params.value == undefined || params.value === '') {
		return '0';
	}
	return parseInt(params.value).toLocaleString();
}

// AG Grid 초기화
function loadTargetPCGrid() {
	var patternCnt = ${patternCnt};
	var pattern = '${pattern}'.split('[{').join('').split('}]').join('');
	pattern = pattern.split('}, {');

	// 컬럼 정의
	var columnDefs = [
	    {
	        headerName: '',
	        field: 'SUBFILE',
	        width: 35,
	        cellRenderer: pathRenderer,
	        sortable: false,
	        pinned: 'left',
	        filter: false  
	    },
	    {
	        headerName: '경로',
	        field: 'SHORTNAME',
	        width: 420,
	        cellRenderer: fileNameRenderer,
	        pinned: 'left',
	        filterType: 'string' ,
	        headerClass: 'right-filter-icon' ,
	        cellStyle: { textAlign: 'left' }
	    },
	    {
	        headerName: '소유주',
	        field: 'OWNER',
	        width: 150,
	        cellRenderer: ownerRenderer,
	        pinned: 'left',
	        filterType: 'string' ,
	        headerClass: 'right-filter-icon'  
	    },
	    {
	        headerName: '검출일',
	        field: 'CREATE_DT',
	        width: 100,
	        cellRenderer: createTimeRenderer,
	        pinned: 'left',
	        filterType: 'date' ,
	        headerClass: 'right-filter-icon'  
	    },
	    {
	        headerName: '기안일',
	        field: 'APPROVAL_DT',
	        width: 100,
	        hide: true
	    },
	    {
	        headerName: '합계',
	        field: 'TYPE',
	        width: 110,
	        type: 'rightAligned',
	        valueFormatter: numberFormatter,
	        pinned: 'left',
	        filterType: 'number' ,
	        headerClass: 'right-filter-icon' 
	    }
	];
	
	// 동적 패턴 컬럼도 숫자 필터 추가
	for(var i = 0; i < pattern.length; i++){
	    var row = pattern[i].split(', ');
	    var ID = row[0].split('ID=').join('');
	    var PATTERN_NAME = row[1].split('PATTERN_NAME=').join('');
	    var data_id = PATTERN_NAME.split('=')[1];
	    
	    columnDefs.push({
	        headerName: ID.split('=')[1],
	        field: data_id,
	        width: 110,
	        type: 'rightAligned',
	        valueFormatter: numberFormatter,
	        filterType: 'number', 
	        headerClass: 'right-filter-icon' 
	    });
	}
	

	// 숨겨진 컬럼들
	var hiddenColumnsData = [
		'APPROVAL_STATUS', 'PROCESSING_FLAG', 'PROCESSING_FLAG_NAME', 'LINK_APPROVAL_STATUS', 'LINK_PROCESSING_FLAG',
		'ID', 'PID', 'LEVEL', 'USER_NO', 'TEAM', 'USER_NAME', 'HOST', 'FILENAME', 'NOTEPAD', 'IDX',
		'POLICY_ID', 'ENABLE', 'POLICY_NM', 'DATATYPE_ID', 'RRN', 'RRN_CNT', 'RRN_DUP', 'FOREIGNER', 
		'FOREIGNER_CNT', 'FOREIGNER_DUP', 'DRIVER', 'DRIVER_CNT', 'DRIVER_DUP', 'PASSPORT', 'PASSPORT_CNT', 
		'PASSPORT_DUP', 'ACCOUNT', 'ACCOUNT_CNT', 'ACCOUNT_DUP', 'CARD', 'CARD_CNT', 'CARD_DUP', 'PHONE', 
		'PHONE_CNT', 'PHONE_DUP', 'MOBILE_PHONE', 'MOBILE_PHONE_CNT', 'MOBILE_PHONE_DUP', 'LOCAL_PHONE', 
		'LOCAL_PHONE_CNT', 'LOCAL_PHONE_DUP', 'EMAIL', 'EMAIL_CNT', 'EMAIL_DUP', 'RECENT'
	];
	
	// 숨겨진 컬럼들을 컬럼 정의에 추가
	hiddenColumnsData.forEach(function(fieldName) {
		columnDefs.push({
			headerName: fieldName,
			field: fieldName,
			width: 1,
			hide: true
		});
	});

	// Grid 옵션 설정
	var gridOptions = {
		theme: 'legacy', 		
	    columnDefs: applyColumnFilters(columnDefs), // 필터 자동 적용
	    localeText: getAgGridKoreanLocale(),       // 한글 로케일		
		defaultColDef: {
			sortable: true,
			filter: true,
			cellStyle: { textAlign: 'center' },
			headerClass: 'ag-header-cell-center'
		},
	    localeText: {
	        ...getAgGridKoreanLocale(),
	        page: '',
	        of: '/',
	        to: '-',
	    },
    	overlayNoRowsTemplate: '<span class="ag-overlay-no-rows-center">대상을 선택하고 조회해주세요</span>',
    
		pagination: true,
		paginationPageSize: 1000,
		paginationPageSizeSelector: [1000, 2000, 2500, 5000],
		rowHeight: 35,
		headerHeight: 40,
		animateRows: true,
		cellSelection: false,
		suppressCellFocus: true,
		onCellClicked: function(event) {
		    $("#pathWindow").hide();
		    $("#taskWindow").hide();
		
		    if (event.colDef.field == 'CHK') return;
		
		    event.event.stopPropagation();
		
		    var rowData = event.data;
		    var colId = event.colDef.field;
		    var isChk = rowData.CHK_C;
		    var id = rowData.ID;
		    var tid = rowData.PID;
		    var processing = rowData.PROCESSING_FLAG;
		    var ap_no = $('#ap_no').val();
		
		    if (isChk == "0" && colId != undefined && colId != null) {
	            var pop_url = "${getContextPath}/popup/detectionDetail?id=" + id + "&tid=" + tid + "&ap_no=" + ap_no;
	            var winWidth = 1142;
	            var winHeight = 365;
	            var popupOption = "width=" + winWidth + ", height=" + winHeight + ", left=0, top=0, scrollbars=yes, resizable=no, location=no";
	            var pop = window.open(pop_url, id, popupOption);
		    } else {
		    	getLowPath(id, tid, ap_no);
		    }
		},
				
		// 그리드 준비 완료 시 이벤트
		onGridReady: function(params) {
			targetPCGridApi = params.api;
			
			params.api.setGridOption('rowData', []);
		},
		
		onRowDataUpdated: function(params) {
			// 데이터 로드 완료 후 처리
			var allRowData = [];
			params.api.forEachNode(function(node) {
				allRowData.push(node.data);
			});
			
			allRowData.forEach(function(rowData, index) {
				var decrypted = '_decrypted';
				var shortName = rowData['SHORTNAME'];
			});
		}
	};

	var gridDiv = document.getElementById('targetPCGrid');
	
	targetPCGridApi = agGrid.createGrid(gridDiv, gridOptions);
}

// 검색 함수
function fnSearchFindSubpathPC() {
	if (targetPCGridApi) {
		targetPCGridApi.setGridOption('rowData', []);
	}
	
	var oPostDt = {};
	oPostDt["target_id"] = $("#hostSelect").val();
	oPostDt["location"] = $("#searchLocation").val();
	oPostDt["ap_no"] = $("#ap_no").val();
	oPostDt["onedriveChk"] = $("#onedriveChk").val();
	oPostDt["processingFlag"] = $("select[name='sch_processingFlag']").val()
	
	// 전체 선택 체크박스 해제
	$('#allChkTargetGrid').prop('checked', false);

	$.ajax({
		url: "${getContextPath}/manage/selectFindSubpath",
		type: "POST",
		data: oPostDt,
		dataType: "json",
		success: function(response) {
	        targetPCGridApi.setGridOption('rowData', response);
	        
	        // 데이터가 없을 때 메시지 변경
	        if (response.length === 0) {
	            targetPCGridApi.setGridOption('overlayNoRowsTemplate', 
	                '<span class="ag-overlay-no-rows-center">검색된 데이터가 없습니다</span>');
	            targetPCGridApi.showNoRowsOverlay();		// 데이터가 없을 때 불러오는 오버레이
	        }
		},
		error: function(xhr, status, error) {
			console.error("데이터 로드 실패:", error);
			alert("데이터를 불러오는데 실패했습니다.");
		}
	});
}

// 엑셀 다운로드 함수
function downLoadPCExcel() {
	if (!targetPCGridApi) {
		console.warn('targetPCGridApi가 초기화되지 않았습니다.');
		return;
	}
	
	resetFomatter = "downloadClick";
	
	var hostname = $("#targetSearch").val().split("(");
	var nameList = hostname[0].split(":");
	var name = "";
	
	for(var i = 0; i < nameList.length; i++){
		if(i == nameList.length - 1){
			name += nameList[i];
		} else {
			name += nameList[i] + "-";
		}
	}

	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth() + 1;
	var yyyy = today.getFullYear();
	if(dd < 10) {
		dd = '0' + dd;
	}
	if(mm < 10) {
		mm = '0' + mm;
	}
	today = yyyy + "" + mm + dd;
	
	// SUBFILE과 히든 컬럼을 제외한 모든컬럼
	var visibleColumns = [];
	targetPCGridApi.getColumns().forEach(function(column) {
		var colDef = column.getColDef();
		if (colDef.field !== 'SUBFILE' && !colDef.hide) {
			visibleColumns.push(colDef.field);
		}
	});
	
	targetPCGridApi.exportDataAsCsv({
		fileName: "검출_리스트_" + name + "_" + today + ".csv",
		columnSeparator: ',',
		suppressQuotes: false,
		columnKeys: visibleColumns
	});
	
	resetFomatter = null;
}

// 전체 선택 함수
// function fn_allChkTargetGrid(chk, e) {
// 	e = e || event;
// 	e.stopPropagation ? e.stopPropagation() : e.cancelBubble = true;
	
// 	if (!targetPCGridApi) return;
	
// 	var allRowData = [];
// 	targetPCGridApi.forEachNode(function(node) {
// 		allRowData.push(node.data);
// 	});
	
// 	if ($(chk).is(":checked")) {
// 		allRowData.forEach(function(rowData, index) {
// 			if(rowData.APPROVAL_STATUS != 'E'){
// 				rowData.LEVEL = "0";
// 				var rowNode = targetPCGridApi.getRowNode(index);
// 				if (rowNode) {
// 					rowNode.setData(rowData);
// 				}
// 			}
// 		});
// 	} else {
// 		// 해제
// 		allRowData.forEach(function(rowData, index) {
// 			if(rowData.APPROVAL_STATUS != 'E'){
// 				rowData.LEVEL = "1";
// 				var rowNode = targetPCGridApi.getRowNode(index);
// 				if (rowNode) {
// 					rowNode.setData(rowData);
// 				}
// 			}
// 		});
// 	}
// }


// function gridClick(e, id) {
// 	var e = e || window.event;
// 	var target = e.target || e.srcElement;
	
// 	if (!targetPCGridApi) return;
	
// 	var rowNode = null;
// 	targetPCGridApi.forEachNode(function(node) {
// 		if (node.data.ID == id) {
// 			rowNode = node;
// 		}
// 	});
	
// 	if (rowNode) {
// 		var rowData = rowNode.data;
// 		if ($(target).is(":checked")) {
// 			rowData.LEVEL = "0";
// 		} else {
// 			rowData.LEVEL = "1";
// 		}
// 		rowNode.setData(rowData);
// 	}
// }

function findByPopPC() {
	$("#taskWindow").hide();
	$("#pathWindow").hide();
	if (targetPCGridApi) {
		targetPCGridApi.setGridOption('rowData', []);
	}
	$("#searchLocation").val("");
	fnSearchFindSubpathPC();
}

function getLowPath(id, tid, ap_no) {
	var pop_url = "${getContextPath}/popup/lowPath?hash_id=" + id + "&tid=" + tid + "&ap_no=" + ap_no;
	var winWidth = 1142;
	var winHeight = 365;
	var popupOption = "width=" + winWidth + ", height=" + winHeight + ", left=0, top=0, scrollbars=no, resizable=no, location=no";
	var pop = window.open(pop_url, id, popupOption);
}


$(document).ready(function () {
	loadTargetPCGrid();
	
	$("#btnDownloadPCExcel").click(function(){
		downLoadPCExcel();
	});

	$("#taskWindowClose").click(function(e){
		$("#taskWindow").hide();
	});
	
	$("#pathWindowClose").click(function(e){
		$("#pathWindow").hide();
	});

	$("#btnSearch").click(function(e){
		fnSearchFindSubpathPC();
	});

	$("#searchLocation").keyup(function(e){
		if (e.keyCode == 13) {
			fnSearchFindSubpathPC();
		}
	});
	
	$("#taskWindow").draggable({
		containment: '.content',
		cancel : '.mxWindowPane'
	});
	
	$("#pathWindow").draggable({
		containment: '.content',
		cancel : '.mxWindowPane'
	});
	
	var to = true;
	$('#btn_sch_target').on('click', function(){
		var v = $('#targetSearch').val();
		console.log(v);
		
		if(to) { clearTimeout(to); }
		to = setTimeout(function () {
			$('#jstree').jstree(true).search(v);
		}, 250);
	});
	
	$('#targetSearch').keyup(function (e) {
		var v = $('#targetSearch').val();
		if (e.keyCode == 13) {
			if(to) { clearTimeout(to); }
			to = setTimeout(function () {
				$('#jstree').jstree(true).search(v);
			}, 250);
		}
	});
});
</script>
		