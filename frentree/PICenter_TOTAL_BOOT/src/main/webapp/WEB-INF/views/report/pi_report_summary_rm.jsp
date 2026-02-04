<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ include file="../../include/header_rm.jsp"%>
<style>
#btnSearch {
	position: relative;
	top: 10px;
	right: 42px;
}
</style>

<!-- section -->
<section>
	<!-- container -->
	<div class="container">
		<h3>통합 보고서</h3>
		<!-- content -->
		<div class="content magin_t25">
            <div class="grid_top">
                <table class="user_info narrowTable" style="width: 1246px; display: inline-block;">
                    <caption>사용자정보</caption>
                    <tbody>
                        <tr>
							<th style="text-align: center; width: 100px; border-radius: 0.25rem;">호스트명</th>
							<td style="width: 255px;">
								<input type="text" style="width: 186px; padding-left: 5px;" size="20" id="SCH_TARGET" placeholder="호스트를 선택하세요" readonly="readonly">
								<input type="hidden" style="width: 186px; padding-left: 5px;" size="20" id="SCH_TARGET_ID" readonly="readonly">
								<input type="hidden" style="width: 186px; padding-left: 5px;" size="20" id="SCH_AP_NO" readonly="readonly">
								<button type="button" class="btn_down" id="btnUserSelectClear" style="font-size : 12px; font-weight:0 ; margin-bottom: 0px; padding:0; width:55px !important; height:27px !important">Clear</button>
							 </td>
							<th style="text-align: center; width: 100px; border-radius: 0.25rem;">기간별</th>
							<td>
								<input type="date" id="SCH_FROM_CREDATE" style="text-align:center; width:186px;" readonly="readonly">
								<span id="SCH_CREDATE_SPAN" style="width: 10%; margin-right: 3px; color: #000;">~</span>
                            	<input type="date" id="SCH_TO_CREDATE" style="text-align:center; width:186px;" readonly="readonly">
							</td>
                        	<th style="text-align: center; width: 100px; border-radius: 0.25rem;">경로</th>
                            <td style="width: 255px;">
                            	<input type="text" style="width: 243px; padding-left: 5px;" size="20" id="SCH_PATH" placeholder="경로를 입력하세요." >
                            </td>
                         	<td style="padding: 5px 5px 0 5px;">
								<input type="button" name="button" class="btn_look_approval" id="btnSearch" style="top: 0px; right: 0px;">
							</td>
                         </tr>
                    </tbody>
                </table>
                 <div class="list_sch">
                    <div class="sch_area" id="sch_area" style="margin-top: 12px;">
						<button type="button" name="button" class="btn_down" id="btnDownloadExel">다운로드</button>
                    </div>
                </div>
            </div>
			<div class="left_box2" style="overflow: hidden; max-height: 649px; height: 649px; margin-top: 10px">
				<div id="targetGrid" class="ag-theme-balham" style="height: 645px; width: 100%;"></div>
			</div>
		</div>
	</div>
</section>
<%@ include file="../../include/footer.jsp"%>

<script type="text/javascript">

// AG Grid 전역변수
var resetFomatter = null;
var targetGridApi = null;

$(document).ready(function () {
    // 날짜 설정
    setSelectDate();

    $("#SCH_PATH").keydown(function(e) {
		if (e.keyCode == 13)
			fn_search();
	});
    
    $("#btnSearch").on("click", function(){
    	fn_search();
    });
    
    $("#btnDownloadExel").on("click", function(){
    	fn_excel();
    });
	
    $("#btnUserSelectClear").on("click", function(e) {
        $("#SCH_TARGET").val('');
        $("#SCH_TARGET_ID").val('');
        $("#SCH_AP_NO").val('');
    });
	
    loadAgGrid();
});

// 숫자
function numberRenderer(params) {
    var value = params.value;
    if (resetFomatter == "downloadClick") {
        return value || '0';  // 다운로드 시에도 빈 값은 0으로
    }
    if (value == null || value == '') return '0';
    
    return Number(value).toLocaleString();
}


// Null 체크
function nullCheckRenderer(params) {
    var value = params.value;
    if (resetFomatter == "downloadClick") {
        return value || '';
    }
    return (value == "" || value == null) ? '-' : value;
}

function loadAgGrid() {
    var pattern = '${pattern}'.split('[{').join('').split('}]').join('');
    pattern = pattern.split('}, {');
    
    var columnDefs = [
        {
            headerName: 'TARGET_ID',
            field: 'TARGET_ID',
            width: 100,
            hide: true
        },
        {
            headerName: 'HASH_ID',
            field: 'HASH_ID',
            width: 100,
            hide: true
        },
        {
            headerName: 'AP_NO',
            field: 'AP_NO',
            width: 100,
            hide: true
        },
        {
            headerName: 'ACCOUNT',
            field: 'ACCOUNT',
            width: 100,
            hide: true
        },
        {
            headerName: 'OS',
            field: 'PLATFORM',
            width: 110,
            cellStyle: { textAlign: 'center' },
            filterType: 'string',
            pinned: 'left',
	        headerClass: 'right-filter-icon'  
	        
        },
        {
            headerName: '호스트',
            field: 'TARGET_NAME',
            width: 140,
            cellStyle: { textAlign: 'center' },
            filterType: 'string',
            pinned: 'left',
	        headerClass: 'right-filter-icon'  
        },
        {
            headerName: 'IP',
            field: 'IP',
            width: 100,
            cellStyle: { textAlign: 'center' },
            filterType: 'string',
            pinned: 'left',
	        headerClass: 'right-filter-icon'  
        },
        {
            headerName: '경로',
            field: 'PATH',
            width: 300,
            cellStyle: { textAlign: 'left' },
            filterType: 'string',
            pinned: 'left',
	        headerClass: 'right-filter-icon'  ,
	        
        },
        {
            headerName: '총개수',
            field: 'TYPE',
            width: 90,
            cellStyle: { textAlign: 'center' },
            cellRenderer: numberRenderer,
            filterType: 'number',
            pinned: 'left',
	        headerClass: 'right-filter-icon'  
        }
    ];
    
    // 동적 컬럼 추가 (패턴 기반)
    for(var i = 0; pattern.length > i; i++){
        var row = pattern[i].split(', ');
        var ID = row[0].split('ID=').join('');
        var PATTERN_NAME = row[1].split('PATTERN_NAME=').join('');
        var data_id = PATTERN_NAME.split('=')[1];
        var columnName = ID.split('=')[1];
        
        columnDefs.push({
            headerName: columnName,
            field: data_id,
            width: 110,
            cellStyle: { textAlign: 'center' },
            cellRenderer: numberRenderer,
            filterType: 'number',
	        headerClass: 'right-filter-icon'  
        });
    }
    
    // 마지막 컬럼들 추가
    columnDefs.push(
        {
            headerName: '검출일',
            field: 'CREDATE',
            width: 170,
            cellStyle: { textAlign: 'center' },
            cellRenderer: nullCheckRenderer,
            filterType: 'date',
            pinned: 'right',
	        headerClass: 'right-filter-icon'  
        },
        {
            headerName: '삭제일',
            field: 'DELDATE',
            width: 170,
            cellStyle: { textAlign: 'center' },
            cellRenderer: nullCheckRenderer,
            filterType: 'date',
            pinned: 'right',
	        headerClass: 'right-filter-icon'  
        }
    );

    // Grid 옵션 설정
    var gridOptions = {
        theme: 'legacy',
        columnDefs: applyColumnFilters(columnDefs),
        localeText: getAgGridKoreanLocale(),
        components: {
            booleanFilter: BooleanFilter
        },
	    localeText: {
	        ...getAgGridKoreanLocale(),
	        page: '',
	        of: '/',
	        to: '-',
	    },
        defaultColDef: {
            sortable: true,
            filter: true,
            resizable: true,
            cellStyle: { textAlign: 'center' },
            headerClass: 'ag-header-cell-center'
        },
        overlayNoRowsTemplate: '<span class="ag-overlay-no-rows-center">조건을 설정하여 검색해주세요</span>',
        pagination: true,
        paginationPageSize: 100,
        paginationPageSizeSelector: [100, 200, 500, 1000],
        rowHeight: 35,
        headerHeight: 40,
        animateRows: true,
        suppressCellFocus: true,
        onGridReady: function(params) {
            targetGridApi = params.api;
            
			params.api.setGridOption('rowData', []);
        },
        
        onRowClicked: function(event) {
            // 행 클릭 이벤트 처리
        }
    };

    // AG Grid 생성
    var gridDiv = document.getElementById('targetGrid');
    targetGridApi = agGrid.createGrid(gridDiv, gridOptions);
}

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

//문서 기안일
function setSelectDate() {
	$("#SCH_FROM_CREDATE").datepicker({
	    changeYear : true,
	    changeMonth : true,
	    dateFormat: 'yy-mm-dd'
	});
	
	$("#SCH_TO_CREDATE").datepicker({
	    changeYear : true,
	    changeMonth : true,
	    dateFormat: 'yy-mm-dd'
	});
	
	var oToday = new Date();
	$("#SCH_TO_CREDATE").val(getFormatDate(oToday));
	
	var oFromDate = new Date(oToday.setDate(oToday.getDate() - 30));
	$("#SCH_FROM_CREDATE").val(getFormatDate(oFromDate));
}

//검색
function fn_search(){
	
	// 검출일
	if($("#SCH_FROM_CREDATE").val() > $("#SCH_TO_CREDATE").val()){
		alert("입력한 끝 날짜가 시작 날짜 보다 빠릅니다.");
		return;
	}
	
	if (!targetGridApi) return;
	
	var postData = {};
	postData["SCH_TARGET"]				= $("#SCH_TARGET").val();
	postData["SCH_TARGET_ID"]			= $("#SCH_TARGET_ID").val();
	postData["SCH_AP_NO"]				= $("#SCH_AP_NO").val();
	postData["SCH_FROM_CREDATE"]		= $("#SCH_FROM_CREDATE").val();
	postData["SCH_TO_CREDATE"]			= $("#SCH_TO_CREDATE").val();
	postData["SCH_PATH"]				= $("#SCH_PATH").val(); 
	
	$.ajax({
        url: "${getContextPath}/report/searchSummaryList",
        type: "POST",
        data: postData,
        dataType: "json",
        success: function(response) {
            if (targetGridApi && Array.isArray(response)) {
                targetGridApi.setGridOption('rowData', response);
            }
        },
        error: function(xhr, status, error) {
            console.error("검색 실패:", error);
            alert("데이터 로드 중 오류가 발생했습니다.");
        }
    });
}

//
function getFormatDate(oDate) {
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

$('#SCH_TARGET').on('click', function(){
	var pop_url = "${getContextPath}/popup/reportHostList";
	var id = "reportGroupList"
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
	data.setAttribute('name','typeChk');
	data.setAttribute('value',"target");
	
	newForm.appendChild(data);
	document.body.appendChild(newForm);
	newForm.submit();
	
	document.body.removeChild(newForm);
});

function fn_excel() {
    if (!targetGridApi) {
        console.warn('targetGridApi가 초기화되지 않았습니다.');
        return;
    }
    
    // 데이터 유무 체크
    var rowCount = 0;
    targetGridApi.forEachNode(function(node) {
        rowCount++;
    });
    
    if (rowCount === 0) {
        alert("다운로드할 데이터가 없습니다.");
        return;
    }
    
    resetFomatter = "downloadClick";
    
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
    
    // 보이는 컬럼만 다운로드
    var visibleColumns = [];
    targetGridApi.getColumns().forEach(function(column) {
        var colDef = column.getColDef();
        if (!colDef.hide) {
            visibleColumns.push(colDef.field);
        }
    });
    
    targetGridApi.exportDataAsCsv({
        fileName: "통합_보고서_" + today + ".csv",
        columnSeparator: ",",
        suppressQuotes: false,
        columnKeys: visibleColumns,
        processCellCallback: function(params) {
            var numberFields = ['TYPE'];

            var pattern = '${pattern}'.split('[{').join('').split('}]').join('');
            pattern = pattern.split('}, {');
            for(var i = 0; pattern.length > i; i++){
                var row = pattern[i].split(', ');
                var PATTERN_NAME = row[1].split('PATTERN_NAME=').join('');
                var data_id = PATTERN_NAME.split('=')[1];
                numberFields.push(data_id);
            }
            
            if (numberFields.includes(params.column.getColId())) {
                return (params.value == null || params.value === '') ? '0' : params.value;
            }
            return params.value;
        }
    });
    
    resetFomatter = null;
}

</script>

</body>
</html>