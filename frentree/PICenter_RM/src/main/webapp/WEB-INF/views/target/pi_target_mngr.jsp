<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ include file="../../include/header_rm.jsp"%>
<style>
.ui-state-hover td{
	background: #dadada !important;
}
.user_info th{
	width: 110px;
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
</style>
		<!-- 업무타이틀(location)
		<div class="banner">
			<div class="container">
				<h2 class="ir">업무명 및 현재위치</h2>
				<div class="title_area">
					<h3>타겟 관리</h3>
					<p class="location">타겟 관리 > 타겟 관리</p>
				</div>
			</div>
		</div>
		<!-- 업무타이틀(location)-->

		<!-- section -->
		<section>
			<!-- container -->
			<div class="container minMenu">
			<%-- <%@ include file="../../include/menu.jsp"%> --%>
				<h3 id= "headerText" style="display: inline; top: 25px;">대상조회</h3>
				<!-- content -->
				<div class="content magin_t35">
					<table class="user_info narrowTable" style="margin-left: 15px; width: 33%; display: inline-block;">
                    <caption>검색 현황</caption>
                    <tbody>
                        <tr>
                            <!-- <th style="text-align: center; background-color: #d6e4ed; width:4vw; font-size:.85vw">업무구분</th>
                            <td style="width:7vw;">
                                <select id="selectList" name="selectList" style="width:6.5vw;">
                                    <option value="/report/pi_report_summary" selected>통합 보고서</option>
                                    <option value="/report/pi_report_exception">예외/오탐수용 보고서</option>
                                </select>
                            </td> -->
                            <th class="user_info_th" style="text-align: center;">호스트명</th>
                            <td class="user_info_td">
                                <input type="text" style="width: 186px; padding-left: 5px;" size="10" id="hostNm" placeholder="호스트명을 입력하세요.">
                            </td>
                            <th class="user_info_th" style="text-align: center;">IP</th>
							<td class="user_info_td">
								<input type="text" style="width: 186px; padding-left: 5px;" id="userIP" placeholder="IP를 입력하세요">
							</td>
                           	<td>
                           		<input type="button" name="button" class="btn_look_approval" id="serach_btn" style="margin-top: 2px;">
                           	</td>
                        </tr>
                    </tbody>
                	</table>
					<div class="left_area2" style="height: 96%;">
						<table class="user_info narrowTable" style="width: 320px;">
						<tbody>
							<tr>
								<th style="text-align: center; border-radius: 0.25rem;">대상조회</th>
		             			     <td>
		                				 <input type="text" style="width: 205px; padding-left: 5px;" size="10" id="targetSearch" placeholder="호스트명을 입력하세요.">
		                			 </td>
		                		<td>
		                    		 <input type="button" name="button" class="btn_look_approval" id="btn_sch_target" style="margin-top: 5px;">
		                    	</td>
								<!-- <td><input type="text" name="targetSearch" class=""
									id="targetSearch" placeholder="호스트명을 기입하세요"
									style="width: 263px; height: 30px; padding-left: 5px; border: 1px solid #c8ced3;">
								</td>
								<td>
									<button type="button" id="btn_sch_target" class="btn_sch_target"
										style="margin: 5px;"></button>
								</td> -->
							</tr>
						</tbody>
						</table>
						<div class="left_box2" style="max-height: 690px;">
		   					<div id="jstree" class="select_location" style="overflow-y: auto; overflow-x: auto; margin-top: 11px; height: 659px; background: #ffffff; border: 1px solid #c8ced3; white-space:nowrap;">
								
							</div>
							<div id="div_search" class="select_location" style="overflow-y: auto; height: 470px;background: #ffffff; display: none;">
								<table id="Tbl_search" class="tbl_input" id="location_table">
									<tbody>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					
					<div class="grid_top" style="margin-left: 335px;">
						<div class="list_sch" style="position: relative; bottom: 26px;">
							<div class="sch_area">
								<!-- <button type="button" id="btnUploadExel" class="btn_down">업로드</button> -->
								<button type="button" id="btnDownloadPCExel" class="btn_down">다운로드</button>
							</div>
						</div> 
		   				<div class="left_box2" id="PCGridBox" style="position: relative; height: 659px; max-height: 690px; bottom: 19px; overflow: hidden;">
							<div id="PCGrid" class="ag-theme-balham" style="height: 658px; width: 100%;"></div>
		   				</div>
					</div>
					
				</div>
			</div>
			<!-- container -->
		</section>
		<!-- section -->

	<%@ include file="../../include/footer.jsp"%>
	

<script type="text/javascript">
//그리드 전역변수
var resetFomatter = null;
var PCGridApi = null;
var PCColumnApi = null;

$(function() {
 $('#jstree').jstree({
     "core": {
         "animation": 0,
         "check_callback": true,
         "themes": { "stripes": false },
         "data": ${userGroupList}
     },
     "types": {
         "#": {
             "max_children": 1,
             "max_depth": 4,
             "valid_children": ["root"]
         },
         "default": {
             "valid_children": ["default", "file"]
         },
         "file": {
             "icon": "glyphicon glyphicon-file",
             "valid_children": []
         }
     },
     'search': {
         'case_insensitive': false,
         'show_only_matches': true,
         "show_only_matches_children": true
     },
     'plugins': ["search"]
 }).bind('select_node.jstree', function(evt, data, x) {
     var id = data.node.id;
     var type = data.node.data.type;
     var text = data.node.text;
     var ap_no = data.node.data.ap;
     var postData = {};

     
     if (type != null) {
         if (type == 0) { // 호스트
             postData = { target_id: "all" };
         } else if (type == 1) { // 전체
             postData = { target_id: id, ap_no: ap_no };
         }
         
         $.ajax({
             url: "<%=request.getContextPath()%>/target/selectRmTargetList",
             type: "POST",
             data: postData,
             dataType: "json",
             success: function(response) {
                 if (PCGridApi && Array.isArray(response)) {
                     PCGridApi.setGridOption('rowData', response);
                 }
             },
             error: function(xhr, status, error) {
                 console.error("데이터 로드 실패:", error);
             }
         });
     }
 });
});

//렌더링 함수
function nullCheckRenderer(params) {
	var value = params.value;
	if (resetFomatter == "downloadClick") {
	    return value || '';
	}
	return (value == "" || value == null) ? '-' : value;
}

function connectionIconRenderer(params) {
	var value = params.value;
	if (resetFomatter == "downloadClick") {
	    return value;
	}
	
	if (value == "연결") {
	    return '<img style="padding-top: 5px;" src="${pageContext.request.contextPath}/resources/assets/images/icon_con.png">';
	} else {
	    return '<img style="padding-top: 5px;" src="${pageContext.request.contextPath}/resources/assets/images/icon_dicon.png">';
	}
}

//AG Grid 초기화 함수
function fn_drawPCGrid() {
    var columnDefs = [
        {
            headerName: '호스트ID',
            field: 'TARGET_ID',
            width: 140,
            hide: true
        },
        {
            headerName: '호스트명',
            field: 'NAME',
            width: 530, 
            cellStyle: { textAlign: 'center' },
            filterType: 'string',
	        headerClass: 'right-filter-icon'           
        },
        {
            headerName: '서버 연결 상태',
            field: 'TARGET_USE',
            width: 100,
            hide: true
        },
        {
            headerName: 'IP',
            field: 'AGENT_CONNECTED_IP',
            width: 290, 
            cellStyle: { textAlign: 'center' },
            filterType: 'string',
	        headerClass: 'right-filter-icon'   
        },
        {
            headerName: '상태',
            field: 'AGENT_CONNECTED',
            width: 85,
            cellRenderer: connectionIconRenderer,
            cellStyle: { textAlign: 'center' },
            filterType: 'boolean',
	        headerClass: 'right-filter-icon'   
        },
        {
            headerName: '버전',
            field: 'AGENT_VERSION',
            width: 85,
            cellRenderer: nullCheckRenderer,
            cellStyle: { textAlign: 'center' },
            filterType: 'string',
	        headerClass: 'right-filter-icon'   
        },
        {
            headerName: '에이전트 설치 일',
            field: 'AGENT_CONNECTED_START_DT',
            width: 410, 
            cellRenderer: nullCheckRenderer,
            cellStyle: { textAlign: 'center' },
            filterType: 'date',
	        headerClass: 'right-filter-icon'   
        },
        {
            headerName: '시작일',
            field: 'AGENT_STARTED',
            width: 140,
            hide: true
        },
        {
            headerName: '마지막 연결 시점',
            field: 'AGENT_CONNECTED_END_DT',
            width: 140,
            cellRenderer: nullCheckRenderer,
            hide: true
        }
    ];

	// Grid 옵션 설정
	var gridOptions = {
	    theme: 'legacy',
	    columnDefs: applyColumnFilters(columnDefs), // 필터 자동 적용
	    localeText: getAgGridKoreanLocale(),       // 한글 로케일	
	    components: {
	        booleanFilter: BooleanFilter  
	    },
	    defaultColDef: {
	        sortable: true,
	        filter: true,
	        resizable: true,
	        cellStyle: { textAlign: 'center' }, 
	        headerClass: 'ag-header-cell-center'
	    },    
	    localeText: {
	        ...getAgGridKoreanLocale(),
	        page: '',
	        of: '/',
	        to: '-',
	    },
	    pagination: true,
	    paginationPageSize: 25,
	    paginationPageSizeSelector: [25, 50, 100],
	    rowHeight: 35,
	    headerHeight: 40,
	    animateRows: true,
	    suppressCellFocus: true,
	    onGridReady: function(params) {
	        PCGridApi = params.api;
	        PCColumnApi = params.api;
	    },
	    
	    // 행 선택 이벤트
	    onRowClicked: function(event) {
	
	    }
	};

	// AG Grid 생성
	var gridDiv = document.getElementById('PCGrid');
	PCGridApi = agGrid.createGrid(gridDiv, gridOptions);
}

//데이터 로드 함수
function setPC() {
	if (!PCGridApi) return;
	
	var postData = { id: "" };
	
	$.ajax({
	    url: "<%=request.getContextPath()%>/target/selectPCTargetUser",
	    type: "POST",
	    data: postData,
	    dataType: "json",
	    success: function(response) {
	        if (PCGridApi && Array.isArray(response)) {
	            PCGridApi.setGridOption('rowData', response);
	        }
	    },
	    error: function(xhr, status, error) {
	        console.error("데이터 로드 실패:", error);
	    }
	});
}

//검색 함수
function searchTarget() {
	if (!PCGridApi) return;
	
	var postData = {
	    groupNm: $("#groupNm").val(),
	    hostNm: $("#hostNm").val(),
	    serviceNm: $("#serviceNm").val(),
	    userIP: $("#userIP").val(),
	    infraUser: $("#infraUser").val(),
	    serviceUser: $("#serviceUser").val(),
	    serviceManager: $("#serviceManager").val()
	};
	
	$.ajax({
	    url: "<%=request.getContextPath()%>/target/searchPCTargetUser",
	    type: "POST",
	    data: postData,
	    dataType: "json",
	    success: function(response) {
	        if (PCGridApi && Array.isArray(response)) {
	            PCGridApi.setGridOption('rowData', response);
	        }
	    },
	    error: function(xhr, status, error) {
	        console.error("검색 실패:", error);
	    }
	});
}

//엑셀 다운로드 함수
function downLoadPCExcel() {
	if (!PCGridApi) {
	    console.warn('PCGridApi가 초기화되지 않았습니다.');
	    return;
	}
	
	// 데이터 유무 체크
	var rowCount = 0;
	PCGridApi.forEachNode(function(node) {
	    rowCount++;
	});
	
	if (rowCount === 0) {
	    alert("다운로드할 데이터가 없습니다.");
	    return;
	}
	
	resetFomatter = "downloadClick";
	
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth() + 1;
	var yyyy = today.getFullYear();
	if (dd < 10) {
	    dd = '0' + dd;
	}
	if (mm < 10) {
	    mm = '0' + mm;
	}
	today = yyyy + "" + mm + dd;
	
	// 보이는 컬럼만 다운로드(숨겨진 컬럼이 많아서 추가)
	var visibleColumns = [];
	PCGridApi.getColumns().forEach(function(column) {
	    var colDef = column.getColDef();
	    if (!colDef.hide) {
	        visibleColumns.push(colDef.field);
	    }
	});
	
	PCGridApi.exportDataAsCsv({
	    fileName: "대상_조회_" + today + ".csv",
	    columnSeparator: ',',
	    suppressQuotes: false,
	    columnKeys: visibleColumns
	});
	
	resetFomatter = null;
}

$(document).ready(function() {
	fn_drawPCGrid();
	setPC();
	
	$("#btnDownloadPCExel").click(function() {
	    downLoadPCExcel();
	});
	
	// 검색 버튼 이벤트
	$("#serach_btn").on("click", function() {
	    searchTarget();
	});
	
	// 검색 입력 엔터 이벤트
	$('#hostNm, #userIP').keyup(function(e) {
	    if (e.keyCode == 13) {
	        searchTarget();
	    }
	});
	
	var to = true;
	$('#btn_sch_target').on('click', function() {
	    var v = $('#targetSearch').val();
	    
	    if (to) { 
	   	 clearTimeout(to); 
	  	 }
	    to = setTimeout(function() {
	        $('#jstree').jstree(true).search(v);
	    }, 250);
	});
	
	$('#targetSearch').keyup(function(e) {
	    var v = $('#targetSearch').val();
	    if (e.keyCode == 13) {
	        if (to) { clearTimeout(to); }
	        to = setTimeout(function() {
	            $('#jstree').jstree(true).search(v);
	        }, 250);
	    }
	});
});



</script>


</body>
</html>