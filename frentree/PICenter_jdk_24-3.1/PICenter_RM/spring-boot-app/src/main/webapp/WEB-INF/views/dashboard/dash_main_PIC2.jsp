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

  /* 대시보드 전환 버튼 스타일 */
  .dashboard-switch-btn {
    position: fixed;
    bottom: 30px;
    left: 50%;
    transform: translateX(-50%);
    padding: 12px 32px;
    background: #2f353a; 
    color: white;
    border: none;
    border-radius: 30px;
    cursor: pointer;
    font-size: 15px;
    font-weight: 600;
    z-index: 9999;
    box-shadow: 0 8px 24px rgba(102, 126, 234, 0.4);
    display: inline-flex;
    align-items: center;
    gap: 10px;
    letter-spacing: 0.5px;
    backdrop-filter: blur(10px);
  }

  .dashboard-switch-btn:hover {
    transform: translateX(-50%) translateY(-4px);
    box-shadow: 0 12px 32px rgba(102, 126, 234, 0.5);
    background: #2f353a;
  }
  .dashboard-switch-btn:active {
    transform: translateX(-50%) translateY(-2px);
    box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
  }

  .dashboard-switch-btn::after {
    content: '';
    position: absolute;
    inset: -2px;
    border-radius: 30px;
    padding: 2px;
    background: linear-gradient(135deg, #667eea, #764ba2);
    -webkit-mask: linear-gradient(#fff 0 0) content-box, linear-gradient(#fff 0 0);
    -webkit-mask-composite: xor;
    mask-composite: exclude;
    opacity: 0;
    transition: opacity 0.3s;
  }
  .dashboard-switch-btn:hover::after {
    opacity: 0.6;
  }

  .container_main > div { width: 100%; height: 100%; }

  #dashTopDiv {
    display: flex;
    gap: 22px;
    height: 42%;
  }
  #pieGraphDiv {
    flex: 0 0 48%;
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
    height: 58%;
    margin-top: 8px;
    display: flex;
    flex-direction: column;
  }

</style>

<section id="section">
  <!-- 대시보드 전환 버튼 -->
  <button type="button" class="dashboard-switch-btn" id="btnSwitchDashboard">
    대시보드 변경
  </button>

  <div class="container_main" style="height: 807px;">
    <div id="dashTopDiv">
      <div id="pieGraphDiv">
        <div id="renderTargetSummaryChart"></div>
        <div id="renderDetectionTypeChart"></div>
      </div>
      <div id="pathServerListDiv">
        <h4 class="list-title">검출 대상 리스트</h4>
        <div id="initDetectionTargetGrid" class="ag-theme-balham" style="height:280px; width:100%;"></div>
      </div>
    </div>
    <div id="dashBottomDiv">
      <h4 class="list-title">대상별 개인정보 검출 건수</h4>
      <!-- AG Grid로 변경 -->
      <div id="initDetectionDetailsGrid" class="ag-theme-balham" style="height:400px; width:100%;"></div>
    </div>
  </div>
</section>

<%@ include file="../../include/footer.jsp"%>

<script type="text/javascript">
var globalPivotData = null;
var globalServerColumns = null;
var globalPiiTypes = null;
var globalCustomPatterns = null;
var targetGridApi = null;  // 검출 대상 리스트 AG Grid API
var detailsGridApi = null; // 대상별 검출 건수 AG Grid API

$(function(){
    loadSharedData();
    renderTargetSummaryChart();
});

// 대시보드 전환 버튼
$("#btnSwitchDashboard").click(function() {
    var userGrade = "${memberInfo.USER_GRADE}";
    var targetUrl = "<%=request.getContextPath()%>/picenter_server";

    if (userGrade == "0" || userGrade == "7") {
        targetUrl = "<%=request.getContextPath()%>/picenter_manager";
    } else if (userGrade == "9") {
        targetUrl = "<%=request.getContextPath()%>/picenter";
    }

    location.href = targetUrl;
});

function loadSharedData() {
    $.ajax({
        url: "/mock/getServerDataPivot",
        type: "POST",
        dataType: "json",
        success: function(data) {
            console.log("=== 받은 전체 데이터 ===");
            console.log("data:", data);

            globalServerColumns = data.serverColumns || [];
            globalPiiTypes = data.piiTypes || [];
            globalCustomPatterns = data.customPatterns || [];
            globalPivotData = {
                rows: data.gridData || []
            };

            if (data.customPatterns && data.customPatterns.length > 0) {
                renderDetectionTypeChart(data.serverTypes || {}, data.dbTypes || {}, data.customPatterns);
            }

            initDetectionTargetGrid(data.serverColumns || [], globalPivotData);
            initDetectionDetailsGrid(data.serverColumns || [], globalPivotData, data.customPatterns || []);
        },
        error: function(xhr, status, error) {
            console.error("데이터 로드 실패:", error);
        }
    });
}

function initDetectionTargetGrid(serverColumns, pivotData) {
    // 기본 컬럼
    var columnDefs = [
        {
            headerName: "No",
            width: 70,
            filter: false,
            sortable: false,
            suppressSizeToFit: true,
            valueGetter: function(params) {
                return params.node.rowIndex + 1;
            }
        },
        { headerName: "그룹명", field: "GROUP_NM", flex: 1, minWidth: 100, filterType: 'string', headerClass: 'right-filter-icon' },
        { headerName: "서버명", field: "SERVER_NM", flex: 1.5, minWidth: 140, filterType: 'string', headerClass: 'right-filter-icon' },
        { headerName: "플랫폼", field: "PLATFORM", flex: 1, minWidth: 100, filterType: 'string', headerClass: 'right-filter-icon' }
    ];

    // 동적 컬럼 추가 (서버 컬럼 2개 - 업무 담당자 정/부)
    var displayColumns = serverColumns.slice(0, 2);
    displayColumns.forEach(function(col) {
        if (col && col.NAME) {
            columnDefs.push({
                headerName: col.NAME,
                field: col.NAME,
                flex: 1,
                minWidth: 130,
                filterType: 'string',
                headerClass: 'right-filter-icon',
                valueFormatter: function(params) {
                    return (params.value == null || params.value === '') ? '-' : params.value;
                }
            });
        }
    });

    // 개인정보 건수
    columnDefs.push({
        headerName: "개인정보 건수",
        field: "PII_CNT",
        flex: 1,
        minWidth: 120,
        type: 'rightAligned',
        filterType: 'number',
        headerClass: 'right-filter-icon',
        valueFormatter: function(params) {
            if (params.value == null) return '0';
            return Number(params.value).toLocaleString();
        }
    });

    // 그리드 옵션
    var gridOptions = {
        theme: 'legacy',
        columnDefs: applyColumnFilters(columnDefs),
        rowData: pivotData.rows,
        defaultColDef: {
            sortable: true,
            resizable: true,
            filter: true,
            cellStyle: { textAlign: 'center' }
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
        domLayout: 'normal',
        headerHeight: 32,
        rowHeight: 32,
        animateRows: true,
        suppressHorizontalScroll: true,
        onSortChanged: function(params) {
            params.api.refreshCells({ force: true });
        },
        onFilterChanged: function(params) {
            params.api.refreshCells({ force: true });
        },
        onPaginationChanged: function(params) {
            params.api.refreshCells({ force: true });
        }
    };

    // 기존 그리드 제거 후 생성
    var gridDiv = document.getElementById('initDetectionTargetGrid');
    gridDiv.innerHTML = '';

    // AG Grid 생성
    targetGridApi = agGrid.createGrid(gridDiv, gridOptions);
}

function initDetectionDetailsGrid(serverColumns, pivotData,  customPatterns) {

      var categoryMapping = {
          "South Korean Mobile Phone Number": "개인정보",
          "Email addresses": "개인정보",
          "Account Number": "금융정보",
          "Visa": "금융정보",
          "South Korean RRN": "고유식별정보",
          "South Korean Foreigner Number": "고유식별정보",
          "South Korean Driver License Number": "고유식별정보",
          "South Korean Passport": "고유식별정보"
      };

      var patternOrder = {
          "South Korean Mobile Phone Number": 1,
          "Email addresses": 2,
          "Account Number": 3,
          "Visa": 4,
          "South Korean RRN": 5,
          "South Korean Foreigner Number": 6,
          "South Korean Driver License Number": 7,
          "South Korean Passport": 8
      };

      // 정렬된 패턴
      var sortedPatterns = customPatterns.slice().sort(function(a, b)     
  {
          return (patternOrder[a.PATTERN_CODE] || 999) -
  (patternOrder[b.PATTERN_CODE] || 999);
      });

      // 카테고리별 컬럼 그룹 생성
      var categoryGroups = {};
      sortedPatterns.forEach(function(pattern) {
          var category = categoryMapping[pattern.PATTERN_CODE] ||
  "기타";
          var label = pattern.PATTERN_KR_NAME;

          if (!categoryGroups[category]) {
              categoryGroups[category] = [];
          }
          categoryGroups[category].push({
              headerName: label,
              field: pattern.PATTERN_CODE,
              flex: 1,
              minWidth: 100,
              type: 'rightAligned',
              filterType: 'number',
              headerClass: 'right-filter-icon',
              valueFormatter: function(params) {
                  if (params.value == null || params.value === '')        
  return '0';
                  return Number(params.value).toLocaleString();
              }
          });
      });

      // 기본 컬럼 (왼쪽 고정)
      var columnDefs = [
          {
              headerName: "No",
              width: 70,
              pinned: 'left',
              filter: false,
              sortable: false,
              suppressSizeToFit: true,
              valueGetter: function(params) {
                  return params.node.rowIndex + 1;
              }
          },
          { headerName: "그룹명", field: "GROUP_NM", pinned: 'left',  flex: 0.5, minWidth: 100, filterType: 'string', headerClass: 'right-filter-icon' },
          { headerName: "서버명", field: "SERVER_NM", pinned: 'left', flex: 1, minWidth: 140, filterType: 'string', headerClass:  'right-filter-icon' },
          { headerName: "플랫폼", field: "PLATFORM", pinned: 'left',  flex: 0.6, minWidth: 90, filterType: 'string', headerClass: 'right-filter-icon' }
      ];

      // 동적 컬럼 추가 (서버 컬럼 2개 - 업무 담당자 정/부) - 왼쪽   고정
      var displayColumns = serverColumns.slice(0, 2);
      displayColumns.forEach(function(col) {
          if (col && col.NAME) {
              columnDefs.push({
                  headerName: col.NAME,
                  field: col.NAME,
                  pinned: 'left',
                  flex: 0.8,
                  minWidth: 100,
                  filterType: 'string',
                  headerClass: 'right-filter-icon',
                  valueFormatter: function(params) {
                      return (params.value == null || params.value ===     '') ? '-' : params.value;
                  }
              });
          }
      });

      // 총 검출 건수 - 왼쪽 고정
      columnDefs.push({
          headerName: "총 검출 건수",
          field: "PII_CNT",
          pinned: 'left',
          flex: 1,
          minWidth: 120,
          type: 'rightAligned',
          filterType: 'number',
          headerClass: 'right-filter-icon',
          valueFormatter: function(params) {
              if (params.value == null) return '0';
              return Number(params.value).toLocaleString();
          }
      });

      // 개인정보 유형 그룹 (3단계 헤더) - 스크롤 영역
      var piiTypeGroup = {
          headerName: "개인정보 유형",
          headerClass: 'group-header-center',
          children: []
      };

      // 카테고리 순서대로 추가
      ["개인정보", "금융정보",  "고유식별정보"].forEach(function(category) {
          if (categoryGroups[category] &&  categoryGroups[category].length > 0) {
              piiTypeGroup.children.push({
                  headerName: category,
                  headerClass: 'group-header-center',
                  children: categoryGroups[category]
              });
          }
      });

      columnDefs.push(piiTypeGroup);

      // 그리드 옵션
      var gridOptions = {
          theme: 'legacy',
          columnDefs: applyColumnFilters(columnDefs),
          rowData: pivotData.rows,
          defaultColDef: {
              sortable: true,
              resizable: true,
              filter: true,
              cellStyle: { textAlign: 'center' },
              width: 100
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
          domLayout: 'normal',
          headerHeight: 32,
          groupHeaderHeight: 28,
          rowHeight: 32,
          animateRows: true,
          suppressHorizontalScroll: false,
          onSortChanged: function(params) {
              params.api.refreshCells({ force: true });
          },
          onFilterChanged: function(params) {
              params.api.refreshCells({ force: true });
          },
          onPaginationChanged: function(params) {
              params.api.refreshCells({ force: true });
          },
          onRowClicked: function(event) {
              // 나중에 행 클릭 시 결과조회 페이지 이동 기능 추가
          }
      };

      // 기존 그리드 제거 후 생성
      var gridDiv =
  document.getElementById('initDetectionDetailsGrid');
      gridDiv.innerHTML = '';

      // AG Grid 생성
      detailsGridApi = agGrid.createGrid(gridDiv, gridOptions);
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
                    radius: '58%',
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

    var serverData = [];
    var serverTotal = 0;
    customPatterns.forEach(function(pattern) {
        var value = serverTypes[pattern.PATTERN_CODE] || 0;
        serverTotal += value;
        serverData.push({
            name: pattern.PATTERN_KR_NAME,
            value: value,
            itemStyle: { color: pattern.COLOR_CODE }
        });
    });

    var dbData = [];
    var dbTotal = 0;
    customPatterns.forEach(function(pattern) {
        var value = dbTypes[pattern.PATTERN_CODE] || 0;
        dbTotal += value;
        dbData.push({
            name: pattern.PATTERN_KR_NAME,
            value: value,
            itemStyle: { color: pattern.COLOR_CODE }
        });
    });

    // 데이터가 없을 경우 회색 단색 표시
    var emptyData = [{ value: 1, name: '데이터 없음', itemStyle: { color: '#E0E0E0' } }];

    var legendData = customPatterns.map(function(pattern) {
        return pattern.PATTERN_KR_NAME;
    });

    echartdoughnut.setOption({
        title: [
            {
                text: '검출 유형별 비율',
                left: 'center',
                top: '1%',
                textStyle: { fontWeight: "bold" }
            },
            {
                subtext: '서버',
                left: '22%',
                top: '65%',
                textAlign: 'center',
                subtextStyle: { color: '#000000' }
            },
            {
                subtext: 'DB',
                left: '78%',
                top: '65%',
                textAlign: 'center',
                subtextStyle: { color: '#000000' }
            }
        ],
        tooltip: { trigger: 'item' },
        legend: {
            type: 'plain',
            orient: 'horizontal',
            left: 'center',
            bottom: '10',
            width: '90%',
            selectedMode: true,
            data: legendData,
            show: true
        },
        series: [
            {
                type: 'pie',
                radius: '50%',
                center: ['22%', '40%'],
                data: serverTotal > 0 ? serverData : emptyData,
                label: {
                    show: serverTotal > 0,
                    position: 'inside',
                    formatter: function(params) {
                        return params.percent < 10 ? '' : params.percent.toFixed(1) + '%';
                    }
                }
            },
            {
                type: 'pie',
                radius: '50%',
                center: ['78%', '40%'],
                data: dbTotal > 0 ? dbData : emptyData,
                label: {
                    show: dbTotal > 0,
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