<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ include file="../../include/header_rm.jsp"%>
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
	  height: 482px;
	}
	#chart_bottom_div {
	  width: 100%;
	  height: 600px;
	}
	.inputHidden{
		border:none;
		border-right:0px; 
		border-top:0px; 
		boder-left:0px; 
		boder-bottom:0px;
	}
</style>

		<!-- section -->
		<section>
			<!-- container -->
			<div class="container" style="height: 1561px;">
			<h3>요약 보고서</h3>

				<!-- content -->
				<div class="content magin_t25" style="height: 631px;">
				<h3 style="padding: 5px 0px;">전체 대상</h3>
					<div class="content_left" style="height: 554px; margin-top: 10px;">
						<h3 style="display:inline; padding: 0;" id="titleTotal"></h3>
						<div class="left_box" style="height:505px; width:100%; margin-top: 10px;">
							<ul>
								<li style="list-style:none; font-size: 12px;" class="totalStatistics"></li>
							</ul> 
							<div id="chart_top_div"></div>
							<script type="text/javascript">
								<!-- 바 그래프  -->
								function one_graph_main(result) {

								    var chartData = [];
								    var patternNames = [];
								    var chartColors = [];
								    var defaultColors = ['#F0620A', '#6F037F', '#11088D', '#CC0F2E', '#FBCD1F', '#0297A2', '#68A62F', '#0570AB', '#FF69B4', '#32CD32', '#FFA500', '#8A2BE2', '#20B2AA', '#FF6347'];

								    // DB에서 받은 패턴 매핑 정보 사용
								    var patternNameMap = result.patternNameMap || {};
								    var patternColorMap = result.patternColorMap || {};
								    var patternInfoList = result.patternInfoList || []; 

								    for (var i = 0; i < patternInfoList.length; i++) {
								        var pattern = patternInfoList[i];
								        var patternKey = pattern.PATTERN_EN_NAME;
								        
								        // result.data에서 해당 패턴의 값 찾기
								        var patternValue = 0;
								        $.each(result.data, function(key, value){
								            $.each(value, function(dataKey, dataValue){
								                if(dataKey === patternKey && dataKey !== "PATH_CNT" && dataKey !== "TOTAL" && dataKey !== "COUNT_TARGET_ID" && dataKey !== "TARGET_ID") {
								                    patternValue = dataValue || 0; // 0이어도 포함
								                    return false; // 찾았으면 루프 종료
								                }
								            });
								            if(patternValue !== undefined) return false; // 외부 루프도 종료
								        });
								        
								        // 모든 패턴을 차트에 추가 (0건 포함)
								        chartData.push({
								            value: patternValue,
								            name: pattern.PATTERN_KR_NAME
								        });
								        patternNames.push(pattern.PATTERN_KR_NAME);
								        
								        // 색상 추가 (DB에서 가져온 색상 우선, 없으면 기본 색상)
								        var color = pattern.COLOR_CODE || defaultColors[i % defaultColors.length];
								        chartColors.push(color);
								    }

								    var echartdoughnut = echarts.init(document.getElementById('chart_top_div'));
								    echartdoughnut.setOption({
								        tooltip : {
								            trigger: 'item',
								            formatter: "{b} <br>Value: {c}<br> Percent Value: {d}%"
								        },
								        legend: {
								            data: patternNames, // 인덱스 순서대로 정렬된 패턴 한글명들 
								            bottom: '1%',
								            orient: 'vertical',
								            align: 'left',
								            height: '50px'
								        },
								        textStyle: {
								            fontFamily: 'NotoSansR',
								            fontSize: 14
								        },
								        series : [
								            {
								                name: '시스템현황',
								                type: 'pie',
								                radius : ['40%', '70%'],
								                color: chartColors, // 인덱스 순서대로 정렬된 색상
								                center: ['50%', '48%'],
								                data: chartData, // 인덱스 순서대로 정렬된 차트 데이터
								                itemStyle: {
								                    borderRadius: 30,
								                    borderColor: '#fff',
								                    borderWidth: 2,
								                    emphasis: {
								                        shadowBlur: 10,
								                        shadowOffsetX: 0,
								                        shadowColor: 'rgba(0, 0, 0, 0.5)'
								                    }
								                }
								            }
								        ]
								    });

								    if(result.data.length < 1){
								        $('#chart_top_div').append("<div class='totalStatistics-box'>기간내에 검출된 개인정보가 없습니다.</div>");
								    }

								    $(".totalStatistics").html("");
								    $("#total_detection").html("");
								    $("#titleTotal").html("");

								    var total_detection = "<div id='spanid' style='display:inline-block; position:absolute; top: 406px; transform: translateX(-50%) translateY(-50%); text-align:center;'>";
								    total_detection += '<h4 style="margin: 0; font-size: 24px;" class="circle_cnt" >총 검출 대상 <br> ' + result.data[0].COUNT_TARGET_ID.toLocaleString('ko-KR') + '</h4></div>';

								    $("#total_detection").append(total_detection);

								    var contentlist = result.data[0].COUNT_TARGET_ID.toLocaleString('ko-KR') + "대 서버(약 "+ result.data[0].PATH_CNT.toLocaleString('ko-KR') + "개 파일, "
								        contentlist += result.data[0].TOTAL.toLocaleString('ko-KR') +"건)내 개인정보 확인";
								    $("#titleTotal").append(contentlist);
								}
								</script>
							<p style="font-weight:bold; text-align: center;" id="total_detection"></p>
						</div>
					</div>
					<div class="grid_top" style="float: right;">
						<h3 style="padding: 0; margin-top:9px;">검출 대상 TOP5</h3>
						<div class="left_box2" style="width: 863px !important; overflow: hidden;">
		   					<div id="topNGrid" class="ag-theme-balham" style="height: 225px; width: 100%;"></div>
		   				</div>
					</div>
					<div class="grid_top" style="float: right;">
						<h3 style="padding: 0; margin-top:12px;">개인정보 유형 TOP5</h3>
						<div class="left_box2" style="width: 863px !important; overflow: hidden;">
		   					<div id="middelNGrid" class="ag-theme-balham" style="height: 225px; width: 100%;"></div>
		   				</div>
					</div>
				</div>
				<div class="content magin_t25" style="height: 798px;">
					 <div class="grid_top" style="width: 100%; float: right; margin: 10px 0;">
					 <h3 style="padding: 0px 0px 5px 0px;">전체 대상 점검 결과</h3>
						 <table class="user_info approvalTh" style="width: 27%; min-width: 240px; margin-bottom: 6px; display: inline-block;">
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
						<div class="sch_area" style="display: inline; float: right; margin-top: 12px;">
							<button type="button" name="button" class="btn_down" id="btnDownLoadExcel">다운로드</button>
						</div>
						<div class="left_box2" style="height: 265px; overflow: hidden;">
		   					<div id="totalNGrid" class="ag-theme-balham" style="height: 265px; width: 100%;"></div>
		   				</div>
					</div> 
					<div class="chart_box" style="width: 100%; height: 345px; margin: 10px 0; background: #fff;">
							<div id=bar_graph_left style="height: 100%; width: 50%; float: left;"></div>
								<script type="text/javascript">
								
							    // 천 단위 쉼표 추가 함수
							    function addCommas(num) {
							        return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
							    }
							    
								<!-- 바 그래프  -->
								function bar_graph_left(result, status, ansyn) {

								    var total = [];
								    var month = [];

								    $.each(result, function(key, value){
								        $.each(value, function(key, value){
								            if(key == "TOTAL") total.push(value);
								            if(key == "MONTH") month.push(value);
								        });
								    });

								    var echartbar = echarts.init(document.getElementById('bar_graph_left'));
								    echartbar.setOption({
								        tooltip: {
								            trigger: 'axis',
								            axisPointer: {
								                type: 'shadow'
								            },
								            // tooltip에 쉼표 적용
								            formatter: function (params) {
								                var result = params[0].name + '<br/>';
								                params.forEach(function(item) {
								                    result += item.marker + ' ' + item.seriesName + ': ' + addCommas(item.value) + '<br/>';
								                });
								                return result;
								            }
								        },
								        // 상단 옵션 데이터 종류
								        legend : {
								            bottom: 10,
								            data : [ '총 검출 건수'],
								        },
								        textStyle: {
								            fontFamily: 'NotoSansR',
								            fontSize: 14
								        },
								        xAxis: {
								            type: 'category',
								            data: month,
								            axisLabel: {
								                interval: 0
								            }
								        },
								        yAxis: {
								            type: 'value',
								            boundaryGap: [0, 0.01],
								            axisLabel: {
								                formatter: function (value) {
								                    return addCommas(value);
								                }
								            }
								        },
								        series: [
								            {
								                name: '총 검출 건수',
								                type: 'bar',
								                barWidth: 30,
								                color: '#006eb6',
								                data: total,
								                itemStyle: {
								                    normal: {
								                        label: {
								                            show: true,
								                            position: 'top',
								                            color: '#000',
								                            formatter: function(params) {
								                                return addCommas(params.value);
								                            }
								                        }
								                    }
								                }
								            }
								        ]
								    });
								}
								</script>
							<div id="bar_graph_right" style="height: 100%; width: 50%; float: right;"></div>
							<script type="text/javascript">
								<!-- 바 그래프  -->
								function bar_graph_right(result, status, ansyn) {
									
									var total = [];
									var month = [];
									var path_cnt = [];
									
									$.each(result, function(key, value){
									    $.each(value, function(key, value){
									    	if(key == "TOTAL") total.push(value);
									        if(key == "MONTH") month.push(value);
									        if(key == "PATH_CNT") path_cnt.push(value);
									        
									    });
									    
									});
									
									var echartbar = echarts.init(document.getElementById('bar_graph_right'));
									echartbar.setOption({
									    tooltip: {
									        trigger: 'axis',
									        axisPointer: {
									            type: 'shadow'
									        }
									    },
									 	// 상단 옵션 데이터 종류
									    legend : {
											bottom: 10,
											data : [ '검출 파일 수'],
										},
									    textStyle: {
											fontFamily: 'NotoSansR',
											fontSize: 14
										},
										xAxis: {
									    	type: 'category',
									        data: month,
									        axisLabel: {
									        	interval: 0
									        }
									    },
									    yAxis: {
									        type: 'value',
									        boundaryGap: [0, 0.01]
									    },
									    series: [
									        {
									        	name: '검출 파일 수',
									            type: 'bar',
									            barWidth: 30,
                                                color :'#dc143c', 
									            data: path_cnt,
									            itemStyle: {
									                normal: {
								                        label: {
								                            show: true,
								                            position: 'top',
								                            color: '#000',
								                            formatter: function(params) {
								                                return addCommas(params.value);
								                            }
								                        }
									                }
									            }
									        }
									       
									    ]
									});
								}
								</script>
					</div>
				</div>
			</div>
			<!-- container -->
		</section>
		<!-- section -->
		
<%@ include file="../../include/footer.jsp"%>

<!-- 팝업창 - 상세정보 시작 -->
<div id="statisticsPopup" class="ui-widget-content" style="position:absolute; left: 10px; top: 10px;  touch-action: none; width: 500px; z-index: 101; box-shadow: 0 2px 5px #ddd; display:none;">
	<div class="progress_container">
	<img class="CancleImg" id="btnCancleStatisticsPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="progress_top">
				<h1>상세 보기</h1>
		</div>
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
								<td>
									<input type="text" style="width: 295px; padding-left: 5px;" size="10" id="total" class="inputHidden">
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
				<div class="content-table" style="width: 478px; !important; height: 205px; background: #fff; border: 1px solid #c8ced3; border-top: none;">
					<table class="popup_tbl" style="border-top : none;">
					<colgroup>
							<col width="50%">
							<col width="*">
						</colgroup>
						<tbody id="details_detail">
						</tbody>
					</table>
				</div> 
			</div>
		<button id="btnstatisticsClose" style="margin-top: 5px; margin-left : 424px;" >닫기</button>
	</div>
</div>

<script type="text/javascript">

// AG Grid 전역변수
var resetFomatter = null;
var topNGridApi = null;
var middelNGridApi = null;
var totalNGridApi = null;

// 공통 렌더러 함수들
function numberRenderer(params) {
    var value = params.value;
    if (resetFomatter == "downloadClick") {
        return value || '0';
    }
    if (value == null || value === '') return '0';
    
    return Number(value).toLocaleString();
}

function nullCheckRenderer(params) {
    var value = params.value;
    if (resetFomatter == "downloadClick") {
        return value || '';
    }
    return (value == "" || value == null) ? '-' : value;
}

// 월 출력 렌더러
function monthRenderer(params) {
    var cellvalue = params.value;
    var index = cellvalue.indexOf("월");
    
    if(index != -1){        
        var printCon = cellvalue.substring(5, index+1);
        return printCon;
    }else{
        return cellvalue;
    }
}

$(document).ready(function () {
	
	fn_drawTopNGrid();
	fn_drawMiddleNGrid();
	fn_drawTotalNGrid();
	
	// 전체 서버 점검 결과(의심건수)
	var oToday = new Date();
	$("#toDate").val(getFormatDate(oToday));
	var toDate = $("#toDate").val();
	var oFromDate = new Date(oToday.setDate(oToday.getDate() - 30));
	$("#fromDate").val(getFormatDate(oFromDate));
	var fromDate = $("#fromDate").val();
	
	var postData = {
			toDate : toDate,
			fromDate : fromDate
	};
	
	$.ajax({
		type: "POST",
		url: "/statistics/totalStatistics",
		async : false,
		data : postData,
		dataType : "json",
	    success: function(response) {
	        one_graph_main(response);
	    }, 

	    error: function (request, status, error) {
	        console.log("ERROR : ", error);
	        console.log("ERROR : ", request);
	        console.log("ERROR : ", status);
	    }
	});
	
	$.ajax({
		type: "POST",
		url: "/statistics/manageBarList",
		async : false,
		data : postData,
		dataType : "json",
	    success: bar_graph_left,
	    error: function (request, status, error) {
	        console.log("ERROR : ", error);
	        console.log("ERROR : ", request);
	        console.log("ERROR : ", status);
	    }
	}); 
	
	$.ajax({
		type: "POST",
		url: "/statistics/manageBarList",
		async : false,
		data : postData,
		dataType : "json",
	    success: bar_graph_right,
	    error: function (request, status, error) {
	        console.log("ERROR : ", error);
	        console.log("ERROR : ", request);
	        console.log("ERROR : ", status);
	    }
	}); 
	
	setSelectDate(); 
	
	$("#btnstatisticsClose").click(function(){ 
		$("#statisticsPopup").hide();
	});
	
	$("#btnCancleStatisticsPopup").click(function(){ 
		$("#statisticsPopup").hide();
	});
});

$("#btnDownLoadExcel").click(function(){
	downLoadExcel();
});

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

function fn_drawTopNGrid() {
    var oToday = new Date();
    $("#toDate").val(getFormatDate(oToday));
    var toDate = $("#toDate").val();
    var oFromDate = new Date(oToday.setDate(oToday.getDate() - 30));
    $("#fromDate").val(getFormatDate(oFromDate));
    var fromDate = $("#fromDate").val();
    
    var postData = {
            toDate : toDate,
            fromDate : fromDate
    };
    
    // AJAX로 먼저 패턴 정보 가져와서 그리드 생성
    $.ajax({
        type: "POST",
        url: "<%=request.getContextPath()%>/statistics/statisticsList",
        async: false,
        data: postData,
        dataType: "json",
        success: function(response) {
            var patternNameMap = response.patternNameMap || {};
            var resultData = response.data || [];
            
            // 동적 컬럼 생성
            var columnDefs = [
                {
                    headerName: '호스트명',
                    field: 'HOST_NAME',
                    flex: 1,
                    cellStyle: { textAlign: 'center' },
                    filterType: 'string'
                },
                {
                    headerName: '검출 파일 수',
                    field: 'PATH_CNT',
                    flex: 1,
                    cellStyle: { textAlign: 'center' },
                    cellRenderer: numberRenderer,
                    filterType: 'number'
                },
                {
                    headerName: '검출 개인정보 수',
                    field: 'TOTAL',
                    flex: 1,
                    cellStyle: { textAlign: 'center' },
                    cellRenderer: numberRenderer,
                    filterType: 'number'
                },
                {
                    headerName: '검출 개인정보 유형',
                    field: 'TYPE',
                    flex: 1,
                    cellStyle: { textAlign: 'center' },
                    filterType: 'string'
                }
            ];
            
            // 패턴별 hidden 컬럼 동적 추가
            for (var patternKey in patternNameMap) {
                columnDefs.push({
                    headerName: patternNameMap[patternKey],
                    field: patternKey,
                    width: 0,
                    hide: true
                });
            }

            // Grid 옵션 설정
            var gridOptions = {
                theme: 'legacy',
                columnDefs: applyColumnFilters(columnDefs),
                localeText: getAgGridKoreanLocale(),
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
                overlayNoRowsTemplate: '<span class="ag-overlay-no-rows-center">데이터가 없습니다</span>',
                rowHeight: 35,
                headerHeight: 40,
                animateRows: true,
                // AJAX 선 실행 시
                rowData: resultData,
                
                suppressCellFocus: true,
                onGridReady: function(params) {
                    topNGridApi = params.api;
                },
                
                onRowClicked: function(event) {
                    var clickY = event.event.pageY;
                    var clickX = event.event.pageX;
                    
                    $("#details_detail").html("");
                    
                    var rowData = event.data;
                    var PATH_CNT = Number(rowData.PATH_CNT).toLocaleString('ko-KR');
                    var TOTAL = Number(rowData.TOTAL).toLocaleString('ko-KR');
                    
                    $("#total").val(TOTAL);
                    
                    var content = "";
                    var content_th = '<th style="text-align: left;">';
                    
                    // 동적으로 각 패턴별 값 추출 및 표시
                    for (var patternKey in patternNameMap) {
                        var value = rowData[patternKey];
                        if (value && Number(value) > 0) {
                            var formattedValue = Number(value).toLocaleString('ko-KR');
                            var patternKrName = patternNameMap[patternKey];
                            content += '<tr>' + content_th + patternKrName + ' </th><td>' + formattedValue + '</td></tr>';
                        }
                    }
                    
                    $("#details_detail").append(content);

                    // 팝업 위치 설정
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
                }
            };

            // AG Grid 생성
            var gridDiv = document.getElementById('topNGrid');
            topNGridApi = agGrid.createGrid(gridDiv, gridOptions);
        },
        error: function (request, status, error) {
            console.log("ERROR : ", error);
        }
    });
}

function downLoadExcel(){
    if (!totalNGridApi) {
        console.warn('totalNGridApi가 초기화되지 않았습니다.');
        return;
    }
    
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
    
    resetFomatter = "downloadClick";
    
    // 보이는 컬럼만 다운로드
    var visibleColumns = [];
    totalNGridApi.getColumns().forEach(function(column) {
        var colDef = column.getColDef();
        if (!colDef.hide) {
            visibleColumns.push(colDef.field);
        }
    });
    
    totalNGridApi.exportDataAsCsv({
        fileName: "요약_보고서_" + today + ".csv",
        columnSeparator: ",",
        suppressQuotes: false,
        columnKeys: visibleColumns
    });
    
    resetFomatter = null;
}

function fn_drawMiddleNGrid() {
    var columnDefs = [
        {
            headerName: '개인정보 유형',
            field: 'DATA_TYPE',
            flex: 1,
            cellStyle: { textAlign: 'center' },
            filterType: 'string'
        },
        {
            headerName: '검출 개인정보 수',
            field: 'MATCH_COUNT',
            flex: 1,
            cellStyle: { textAlign: 'center' },
            cellRenderer: numberRenderer,
            filterType: 'number'
        }
    ];

    // Grid 옵션 설정
    var gridOptions = {
        theme: 'legacy',
        columnDefs: applyColumnFilters(columnDefs),
        localeText: getAgGridKoreanLocale(),
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
        overlayNoRowsTemplate: '<span class="ag-overlay-no-rows-center">데이터가 없습니다</span>',
        rowHeight: 35,
        headerHeight: 40,
        animateRows: true,
        suppressCellFocus: true,
        onGridReady: function(params) {
            middelNGridApi = params.api;
        }
    };

    // AG Grid 생성
    var gridDiv = document.getElementById('middelNGrid');
    middelNGridApi = agGrid.createGrid(gridDiv, gridOptions);
    
    // TOP5 전체서비스 데이터 로드
    var oToday = new Date();
    $("#toDate").val(getFormatDate(oToday));
    var toDate = $("#toDate").val();

    var oFromDate = new Date(oToday.setDate(oToday.getDate() - 30));
    $("#fromDate").val(getFormatDate(oFromDate));
    var fromDate = $("#fromDate").val();
    
    var postData = {
            toDate : toDate,
            fromDate : fromDate
    };
    
    $.ajax({
        url: "<%=request.getContextPath()%>/statistics/statisticsPolicyList",
        type: "POST",
        data: postData,
        dataType: "json",
        success: function(response) {
            if (middelNGridApi && Array.isArray(response)) {
                middelNGridApi.setGridOption('rowData', response);
            }
        },
        error: function(xhr, status, error) {
            console.error("데이터 로드 실패:", error);
        }
    });
}

function fn_drawTotalNGrid(fromDate, toDate) {
    if (!fromDate || !toDate) {
        var oToday = new Date();
        toDate = getFormatDate(oToday);
        var oFromDate = new Date(oToday.setDate(oToday.getDate() - 30));
        fromDate = getFormatDate(oFromDate);
        
        $("#toDate").val(toDate);
        $("#fromDate").val(fromDate);
    }
    
    var postData = {
            toDate : toDate,
            fromDate : fromDate
    }; 
    
    $.ajax({
        type: "POST",
        url: "<%=request.getContextPath()%>/statistics/manageList",
        async: false,
        data: postData,
        dataType: "json",
        success: function(response) {
            var patternNameMap = response.patternNameMap || {};
            var patternInfoList = response.patternInfoList || [];
            
            var columnDefs = [
                {
                    headerName: '월',
                    field: 'MONTH',
                    width: 80,
                    cellStyle: { textAlign: 'center' },
                    cellRenderer: monthRenderer,
                    filterType: 'string'
                },
                {
                    headerName: '검색 대상',
                    field: 'SEARCH',
                    width: 80,
                    cellStyle: { textAlign: 'center' },
                    cellRenderer: numberRenderer,
                    filterType: 'number'
                },
                {
                    headerName: '검출 대상',
                    field: 'SUCCEED',
                    width: 80,
                    cellStyle: { textAlign: 'center' },
                    cellRenderer: numberRenderer,
                    filterType: 'number'
                },
                {
                    headerName: '검출 파일 수',
                    field: 'PATH_CNT',
                    width: 100,
                    cellStyle: { textAlign: 'center' },
                    cellRenderer: numberRenderer,
                    filterType: 'number'
                },
                {
                    headerName: '개인정보 유형',
                    headerClass: 'group-header-center', 
                    children: []
                },
                {
                    headerName: '총 검출 건수',
                    field: 'TOTAL',
                    width: 100,
                    cellStyle: { textAlign: 'center' },
                    cellRenderer: numberRenderer,
                    filterType: 'number'
                }
            ];
            
            // 패턴 컬럼들을 개인정보 유형 그룹에 추가
            var personalInfoGroup = columnDefs.find(col => col.headerName == '개인정보 유형');
            
            for (var i = 0; i < patternInfoList.length; i++) {
                var pattern = patternInfoList[i];
                personalInfoGroup.children.push({
                    headerName: pattern.PATTERN_KR_NAME,
                    field: pattern.PATTERN_EN_NAME,
                    width: 70,
                    cellStyle: { textAlign: 'center' },
                    cellRenderer: numberRenderer,
                    filterType: 'number'
                });
            }

            personalInfoGroup.children = applyColumnFilters(personalInfoGroup.children);

            // Grid 옵션 설정
            var gridOptions = {
                theme: 'legacy',
                columnDefs: applyColumnFilters(columnDefs),
                localeText: getAgGridKoreanLocale(),
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
                suppressCellFocus: true,
                overlayNoRowsTemplate: '<span class="ag-overlay-no-rows-center">기간을 설정하고 검색해주세요</span>',
                
                rowHeight: 35,
                headerHeight: 40,
                animateRows: true,
                rowData: response.data,
                
                onGridReady: function(params) {
                    totalNGridApi = params.api;
                }
            };

            var gridDiv = document.getElementById('totalNGrid');
            totalNGridApi = agGrid.createGrid(gridDiv, gridOptions);
            totalNGridApi.sizeColumnsToFit();
        },
        error: function (request, status, error) {
            console.log("ERROR : ", error);
        }
    });
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

$("#btnSearch").click(function(e){
    
    if($("#fromDate").val() > $("#toDate").val()){
        alert("입력한 끝 날짜가 시작 날짜 보다 빠릅니다.");
        return;
    }
    
    var fromDate = $("#fromDate").val();
    var toDate = $("#toDate").val();
    var postData = {
        toDate : toDate,
        fromDate : fromDate
    };
   
    $.ajax({
        type: "POST",
        url: "<%=request.getContextPath()%>/statistics/manageList",
        async: false,
        data: postData,
        dataType: "json",
        success: function(response) {
            if (totalNGridApi && Array.isArray(response.data)) {
                totalNGridApi.setGridOption('rowData', response.data);
            }
        },
        error: function (request, status, error) {
            console.log("ERROR : ", error);
        }
    });
    
    // 막대 그래프
    $.ajax({
        type: "POST",
        url: "/statistics/manageBarList",
        async : false,
        data : postData,
        dataType : "json",
        success: bar_graph_left,
        error: function (request, status, error) {
            console.log("ERROR : ", error);
        }
    });
    
    $.ajax({
        type: "POST",
        url: "/statistics/manageBarList",
        async : false,
        data : postData,
        dataType : "json",
        success: bar_graph_right,
        error: function (request, status, error) {
            console.log("ERROR : ", error);
        }
    });
});

</script>
	<!-- wrap -->
</body>
</html>

