<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%@ include file="../../include/header.jsp"%>
<style>
  section { padding: 0 45px; }
  header  { background: none; }

  .list-title {
    font-size: 16px;
    font-weight: bold;
    margin: 0 0 8px 2px;
  }

  .container_main > div { width: 100%; height: 100%; }

  #dashTopDiv {
    display: flex;
    gap: 22px;
    height: 45%;
  }
  #pieGraphDiv {
    flex: 0 0 55%;
    min-width: 520px;
    position: relative;
  }
  #pathServerListDiv {
    flex: 1 1 0;
    min-width: 480px;
    display: flex;
    flex-direction: column;
  }

  #renderTargetSummaryChart  { height: 100%; width: 40%; float: left; }
  #renderDetectionTypeChart { height: 100%; width: 60%; float: left; }

  #dashBottomDiv {
    height: 55%;
    margin-top: 8px;
    display: flex;
    flex-direction: column;
  }
</style>

<section id="section">
  <div class="container_main" style="height: 807px;">
    <div id="dashTopDiv">
      <div id="pieGraphDiv">
        <div id="renderTargetSummaryChart"></div>
        <div id="renderDetectionTypeChart"></div>
      </div>
      <div id="pathServerListDiv">
        <h4 class="list-title">검출 대상 리스트</h4>
        <table id="initDetectionTargetGrid"></table>
        <div id="initDetectionTargetGridPage"></div>
      </div>
    </div>
    <div id="dashBottomDiv">
      <h4 class="list-title">대상 별 개인정보 검출 건수</h4>
      <table id="initDetectionDetailsGrid"></table>
      <div id="initDetectionTargetGridPage2"></div>
    </div>

  </div>
</section>

<%@ include file="../../include/footer.jsp"%>

<script type="text/javascript">
var globalPivotData = null; // 전역 변수로 데이터 저장
var globalServerColumns = null; // 서버 컬럼 정보 저장
var globalPiiTypes = null; // PII 타입 목록 저장
var globalCustomPatterns = null; // 커스텀 패턴 정보 저장

$(function(){
    // 한 번에 모든 데이터를 불러와서 사용
    loadSharedData();
    renderTargetSummaryChart(); // 기존 방식 유지
});

function loadSharedData() {
    // 통합된 데이터 가져오기
    $.ajax({
        url: "/mock/getServerDataPivot",
        type: "POST",
        dataType: "json",
        success: function(data) {
            
            // 글로벌 변수에 저장
            globalServerColumns = data.serverColumns;
            globalPiiTypes = data.piiTypes; // PII 타입 목록 저장 (PATTERN_CODE)
            globalCustomPatterns = data.customPatterns; // 커스텀 패턴 정보 저장
            globalPivotData = {
                rows: data.gridData
            };

            // 각 컴포넌트 초기화 (커스텀 패턴 정보 전달)
            renderDetectionTypeChart(data.serverTypes, data.dbTypes, data.customPatterns);
            initDetectionTargetGrid(data.serverColumns, globalPivotData);
            initDetectionDetailsGrid(data.serverColumns, globalPivotData, data.customPatterns);
        },
        error: function(xhr, status, error) {
            console.error("데이터 로드 실패:", error);
        }
    });
}

function initDetectionTargetGrid(serverColumns, pivotData) {
    var colModel = [
        { label:"그룹명", name:"GROUP_NM", index:"GROUP_NM", width:110, align:"center" },
        { label:"서버명", name:"SERVER_NM", index:"SERVER_NM", width:140, align:"center" },
        { label:"플랫폼(서버/DB)", name:"PLATFORM", index:"PLATFORM", width:130, align:"center" }
    ];
    
    // 동적 컬럼들 추가 (담당자 등)
    for(var i = 0; i < serverColumns.length; i++) {
        if(serverColumns[i] && serverColumns[i].NAME) { 
            colModel.push({
                label: serverColumns[i].NAME,          
                name: serverColumns[i].NAME,        
                index: serverColumns[i].NAME,        
                width: 110,
                align: "center",
                formatter: function(cellvalue, options, rowObject) {
                    if (cellvalue == null || cellvalue == undefined || cellvalue === '') {
                        return '-';
                    }
                    return cellvalue;
                }
            });
        }
    }
    
    colModel.push({ 
        label:"개인정보 건수", 
        name:"PII_CNT", 
        index:"PII_CNT", 
        width:120, 
        align:"right", 
        formatter:"integer" 
    });

    $("#initDetectionTargetGrid").jqGrid({
        datatype: "local", // local 데이터 사용
        data: pivotData.rows, // 미리 로드된 데이터 사용
        colModel: colModel,
        loadonce: true,
        autowidth: true,
        viewrecords: true,
        width: $("#initDetectionTargetGrid").parent().width(),
        height: 250,
        shrinkToFit: false,
        rowNum: 25,
        rowList: [25,50,100],
        pager: "#initDetectionTargetGridPage",
        rownumbers: true,
        rownumWidth: 35,
        loadComplete: function(data) {

        },
        gridComplete: function() {
        }
    });
}

function initDetectionDetailsGrid(serverColumns, pivotData, customPatterns) {
    var colModel = [
        { label:"그룹명", name:"GROUP_NM", index:"GROUP_NM", width:110, align:"center" },
        { label:"서버명", name:"SERVER_NM", index:"SERVER_NM", width:140, align:"center" },
        { label:"플랫폼(서버/DB)", name:"PLATFORM", index:"PLATFORM", width:130, align:"center" }
    ];

    // 동적 컬럼들 추가 (담당자 등)
    for(var i = 0; i < serverColumns.length; i++) {
        if(serverColumns[i] && serverColumns[i].NAME) {
            colModel.push({
                label: serverColumns[i].NAME,
                name: serverColumns[i].NAME,
                index: serverColumns[i].NAME,
                width: 110,
                align: "center",
                formatter: function(cellvalue, options, rowObject) {
                    if (cellvalue == null || cellvalue == undefined || cellvalue === '') {
                        return '-';
                    }
                    return cellvalue;
                }
            });
        }
    }

    // customPatterns 기준으로 PII 타입 컬럼 추가
    for(var j = 0; j < customPatterns.length; j++) {
        var pattern = customPatterns[j];
        colModel.push({
            label: pattern.PATTERN_KR_NAME, // 한글명으로 라벨 설정
            name: pattern.PATTERN_CODE, // PATTERN_CODE로 데이터 매핑
            index: pattern.PATTERN_CODE,
            width: 100,
            align: "right",
            formatter: function(cellvalue, options, rowObject) {
                if (cellvalue == null || cellvalue == undefined || cellvalue === '') {
                    return '0';
                }
                return Number(cellvalue).toLocaleString();
            }
        });
    }

    colModel.push({
        label:"개인정보 건수",
        name:"PII_CNT",
        index:"PII_CNT",
        width:120,
        align:"right",
        formatter:"integer"
    });

    $("#initDetectionDetailsGrid").jqGrid({
        datatype: "local",
        data: pivotData.rows,
        colModel: colModel,
        loadonce: true,
        autowidth: true,
        viewrecords: true,
        width: $("#initDetectionDetailsGrid").parent().width(),
        height: 320,
        shrinkToFit: false,
        rowNum: 25,
        rowList: [25,50,100],
        pager: "#initDetectionTargetGridPage2",
        rownumbers: true,
        rownumWidth: 35,
        loadComplete: function(data) {

        },
        gridComplete: function() {
        }
    });
}

function renderTargetSummaryChart() {
    var echartdoughnut = echarts.init(document.getElementById('renderTargetSummaryChart'));

    $.ajax({
        url: "/mock/allTargetList",
        type: "POST",
        dataType: "json",
        success: function(data) {

            var chartData = [];
            for(var i = 0; i < data.length; i++) {
                var item = {
                    value: data[i].COUNT,
                    name: data[i].TYPE
                };

                if(data[i].TYPE === 'DB') {
                    item.itemStyle = { color: '#4472C4' };
                } else if(data[i].TYPE === 'SERVER') {
                    item.itemStyle = { color: '#ED7D31' };
                }

                chartData.push(item);
            }

            echartdoughnut.setOption({
                title: {
                    text: '전체 대상',
                    left: 'center',
                    top: '1%',
                    textStyle: {
                        fontWeight: "bold"
                    }
                },
                tooltip: {
                    trigger: 'item'
                },
                legend: {
                    type: 'plain',
                    orient: 'horizontal',
                    left: 'center',
                    bottom: '10',
                    width: '80%',
                    selectedMode: true
                },
                series: [{
                    name: '전체 대상',
                    type: 'pie',
                    radius: '50%',
                    center: ['50%', '45%'],
                    data: chartData,
                    label: {
                        show: true,
                        position: 'inside',
                        formatter: '{c} 개'
                    },
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    },
                    top: 0,
                    bottom: 0
                }]
            });
        },
        error: function(xhr, status, error) {
            console.error("차트 데이터 로드 실패:", error);
        }
    });
}

function renderDetectionTypeChart(serverTypes, dbTypes, customPatterns) {
    var chartDom = document.getElementById('renderDetectionTypeChart');
    echarts.dispose(chartDom);
    
    var echartdoughnut = echarts.init(chartDom);

    // customPatterns 기준으로 서버 데이터 생성 (모든 항목 포함)
    var serverData = [];
    customPatterns.forEach(function(pattern) {
        var patternCode = pattern.PATTERN_CODE;
        var patternName = pattern.PATTERN_KR_NAME;
        var colorCode = pattern.COLOR_CODE;
        var value = serverTypes[patternCode] || 0;
        
        serverData.push({
            name: patternName,
            value: value,
            itemStyle: {
                color: colorCode
            }
        });
    });

    // customPatterns 기준으로 DB 데이터 생성 (모든 항목 포함)
    var dbData = [];
    customPatterns.forEach(function(pattern) {
        var patternCode = pattern.PATTERN_CODE;
        var patternName = pattern.PATTERN_KR_NAME;
        var colorCode = pattern.COLOR_CODE;
        var value = dbTypes[patternCode] || 0;
        
        dbData.push({
            name: patternName,
            value: value,
            itemStyle: {
                color: colorCode
            }
        });
    });

    // 범례 데이터 (항상 모든 customPatterns 항목 표시)
    var legendData = customPatterns.map(function(pattern) {
        return pattern.PATTERN_KR_NAME;
    });

    echartdoughnut.setOption({
        title: [
            {
                text: '검출 유형별 비율',
                left: 'center',
                top: '1%',
                textStyle: {
                    fontWeight: "bold"
                } 
            },
            {
                subtext: '서버',
                left: '25%',
                top: '65%',
                textAlign: 'center',
                subtextStyle: { color: '#000000' }
            },
            {
                subtext: 'DB',
                left: '75%',
                top: '65%',
                textAlign: 'center',
                subtextStyle: { color: '#000000' }
            }
        ],
        tooltip: {
            trigger: 'item'
        },
        legend: {
            type: 'plain',
            orient: 'horizontal',
            left: 'center',
            bottom: '10',
            width: '70%',
            selectedMode: true,
            data: legendData,
            show: true
        },
        series: [
            {
                type: 'pie',
                radius: '45%',
                center: ['25%', '40%'],
                data: serverData, // 모든 항목 포함 (값이 0인 것도)
                label: {
                    show: true,
                    position: 'inside',
                    formatter: function(params) {
                        return params.percent < 10 ? '' : params.percent.toFixed(1) + '%';
                    }
                }
            },
            {
                type: 'pie',
                radius: '45%',
                center: ['75%', '40%'],
                data: dbData, // 모든 항목 포함 (값이 0인 것도)
                label: {
                    show: true,
                    position: 'inside',
                    formatter: function(params) {
                        return params.percent < 10 ? '' : params.percent.toFixed(1) + '%';
                    }
                }
            }
        ]
    });
}
</script>
</body>
</html>
