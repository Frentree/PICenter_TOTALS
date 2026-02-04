<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
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
.grid_top {
	width: 50%;
	float: left;
	padding: 0 25px 15px 0;
}
.ui-jqgrid tr.ui-row-ltr td{
	cursor: pointer;
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
	<!-- section -->
	<section>
		<!-- container -->
		<div class="container" style="height: 1010px;">
			<h3 style="display: inline; top: 25px;" id="test_alert">서버 현황</h3>
			<!-- content -->  
			<div class="content magin_t35" style="height: 918px;">
				<h3 style="padding: 0;" id="test_alert2">System Load</h3>
				<div class="chart_box"  style="width: 100%; height: 390px; background: #fff;"> 
					<div id="bar_graph_left" style="height: 100%; width: 50%; float: left;"></div>
						<div class="chart_Grid" id="reconGridDraw" style="height: 100%; width: 50%; float: left; display: none; padding: 10px;">
						    <table id="reconGrid"></table>  
						    <div id="reconGridPager"></div> 
						    <button type="button" name="button" class="btn_down" style="margin-top: 10px; float: right;" id="ReconGridCancel">닫기</button>
						</div>
					<div id="chart_bottom_div" style="height: 100%; width: 50%; float: right;"></div>
				</div>
				<h3 style="padding: 0; margin-top: 22px;">에이전트 현황</h3>
				<div class="chart_box" style="width: 100%; height: 390px; background: #fff;">
					<div id=bar_graph_left2 style="height: 100%; width: 50%; float: left;"></div>
					<div id="chart_bottom_div2" style="height: 100%; width: 50%; float: right;"></div>
				</div> 
			</div>
		</div>
	</section>
	
	<div class="reconDatePopup" class="popup_layer" style="display:none; background: none; position: unset;" >
		<div class="popup_box" style="height: 145px;  width: 497px; padding: 10px; background: #f9f9f9; top: 554px; left: 576px; box-shadow: rgb(221, 221, 221) 2px 2px 5px;z-index: 999;border: 1px solid rgb(205, 205, 205);">
	      <img class="CancleImg" class="btnCancleReconDatePopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
	          <div class="popup_top" style="background: #f9f9f9;">
	              <h1 style="color: #222; padding: 0; box-shadow: none;" class="excelDownName">시스템 경고</h1>
	          </div>
	          <div class="popup_content">
	             <div class="content-box" style="height: 84px; background: #fff; border: 1px solid #c8ced3; font-size: 13px; padding-top: 14px;">
	             	
                  <span style="color: red; font-weight: bold;" class="reconAlaert"> 사용량이  초과했습니다.</span><br>
					지속적인 고부하는 시스템 성능 저하 및 장애를 유발할 수 있습니다.<br>
					원인 확인 및 조치가 필요합니다.<br>
	              </div>  
	          </div>
	          <div class="popup_btn">
	              <div class="btn_area">
	                  <button type="button" class="btnReconDatePopupClose">닫기</button>
	              </div>
	          </div>
          </div>  
	</div>
	<div class="picenterDatePopup" class="popup_layer" style="display:none; background: none; position: unset;" >
		<div class="popup_box" style="height: 145px;  width: 497px; padding: 10px; background: #f9f9f9; top: 554px; left: 1494px; box-shadow: rgb(221, 221, 221) 2px 2px 5px;z-index: 999;border: 1px solid rgb(205, 205, 205);">
	      <img class="CancleImg" class="btnCanclePIcenterDatePopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
	          <div class="popup_top" style="background: #f9f9f9;">
	              <h1 style="color: #222; padding: 0; box-shadow: none;" class="excelDownName">시스템 경고</h1>
	          </div>
	          <div class="popup_content">
	             <div class="content-box" style="height: 84px; background: #fff; border: 1px solid #c8ced3; font-size: 13px; padding-top: 14px;">
	             	
                  <span style="color: red; font-weight: bold;" class="reconAlaert"> 사용량이  초과했습니다.</span><br>
					지속적인 고부하는 시스템 성능 저하 및 장애를 유발할 수 있습니다.<br>
					원인 확인 및 조치가 필요합니다.<br>
	              </div>  
	          </div>
	          <div class="popup_btn">
	              <div class="btn_area">
	                  <button type="button" class="btnPICenterDatePopupClose">닫기</button>
	              </div>
	          </div>
          </div>  
	</div>
<%@ include file="../../include/footer.jsp"%>

<script type="text/javascript">

var alertData = 90;
var savedReconChartOption = null;
$(document).ready(function() {
	bar_graph_left(null, null, null);
	bar_graph_left2(null, null, null);
	bar_graph_left3(null, null, null);
	bar_graph_left4(null, null, null);
});

function bar_graph_left(result, status, ansyn) {
    var month = [];
    var cases = [];
    var path_cnt = [];
    var total = [];
    var data = [];
    
    var postData = {csvType : "recon"}; 
    $.ajax({
        type: "POST",
        url: "/setting/systemLog",
        async : false,
        data : postData,
        success: function (resultMap) {
        	data = resultMap.resultMessage;
        	
        	alertShowData("recon", data[data.length-1]);
            var echartbar = echarts.init(document.getElementById('bar_graph_left'));
            echartbar.setOption({
            	title: {
           		    text: 'Recon',
           		    left: 'left',
        	   		 textStyle: {
        	   		    fontWeight: 'bold'  // 또는 700
        	   		  }
           		    
           		  },
                  tooltip: {
                    trigger: 'axis'
                  },
                  legend: {  
                	  top: 10,
                	  data: [  
                		  { name: 'CPU', icon: 'rect' },
                		  { name: 'Disk', icon: 'rect' },
                		  { name: 'Memory', icon: 'rect' }
                	  ]
                	},
                  grid: {  
                    left: '7%', 
                    right: '7%', 
                    bottom: '20%'   
                  },
                  xAxis: {
                    data: data.map(function (item) {
                      return item[0];
                    })
                  },
                  yAxis: {},
                  toolbox: {show : true,
        				top: 0,    
        				right: 60,
        				textStyle: {
        					fontFamily: 'NotoSansR',
        					fontSize: '13px'
        				},  
        				feature : {
        					dataView : {
        						show : true,
        						readOnly : false,
        						title : '데이터',
        						optionToContent: function(opt) {
        							$("#bar_graph_left").hide();
        							$("#reconGridDraw").show();
        							GridDraw("recon", data[data.length-1]);
                                },
                                contentToOption: function () {
                                	return savedReconChartOption;
                                }
        					}, 
        					magicType : {
        						show : true,
        						title: {
        				          line: '선형차트',
        				          bar: '막대차트',
        						},
        						type : [ 'line', 'bar' ],
        					}, 
        					restore : {
        						show : true,
        						title : '복원'
        					},
        					saveAsImage : {
        						show : true,
        						title : '이미지로저장',
        						leng : ['저장']
        					},
        					   
        				}
        			},
                  dataZoom: [  
                    {
                      startValue: '2025-01-01'
                    },
                    {
                      type: 'inside'
                    }
                  ],
                  series: [
                	    {
                	      name: 'CPU',
                	      type: 'line',
                	      showSymbol: false,
                	      data: data.map(function (item) {
                	        return item[1]; 
                	      }),
                	      lineStyle: {
                	        color: '#FF0066' 
                	      }
                	    },
                	    {
                	      name: 'Disk',
                	      type: 'line',
                	      showSymbol: false,
                	      data: data.map(function (item) {
                	        return item[2]; 
                	      }),
                	      lineStyle: {
                	        color: '#0066FF'
                	      }
                	    },
                	    {
                	      name: 'Memory',
                	      type: 'line',
                	      showSymbol: false,
                	      data: data.map(function (item) {
                	        return item[3]; 
                	      }),
                	      lineStyle: {
                	        color: '#00CCCC'
                	      }
                	    }
                	  ],
                	  animation: true,
                	  animationDuration: 500
                });
            
            	savedReconChartOption = echartbar.getOption(); 
        },
        error: function (request, status, error) {
            alert("데이터 조회 불가");
        }
    });
}  
function bar_graph_left2(result, status, ansyn) {
    var month = [];
    var cases = [];
    var path_cnt = [];
	var data = [];
    
    var postData = {csvType : "picenter"};
    $.ajax({
        type: "POST",
        url: "/setting/systemLog",
        async : false,
        data : postData,
        success: function (resultMap) {
        	data = resultMap.resultMessage; 
        	
        	alertShowData("picenter", data[data.length-1]);
            
            var echartbar = echarts.init(document.getElementById('chart_bottom_div'));
            echartbar.setOption({
            	 title: {
           		    text: 'PICenter',
           		    left: 'left',
        	   		 textStyle: {
        	   		    fontWeight: 'bold'  // 또는 700
        	   		  }
           		    
           		  },
                  tooltip: {
                    trigger: 'axis'
                  },
                  legend: {  
                	  top: 10,
                	  data: [  
                		  { name: 'CPU', icon: 'rect' },
                		    { name: 'Disk', icon: 'rect' },
                		    { name: 'Memory', icon: 'rect' }
                	  ]
                	},
                  grid: {     
                    left: '7%', 
                    right: '7%', 
                    bottom: '20%'   
                  },
                  xAxis: {
                    data: data.map(function (item) {
                      return item[0];
                    })
                  },
                  yAxis: {},
                  toolbox: {show : true,
        				top: 0,    
        				right: 60,
        				textStyle: {
        					fontFamily: 'NotoSansR',
        					fontSize: '13px'
        				},  
        				feature : {
        					dataView : {
        						show : true,
        						readOnly : false,
        						title : '데이터',
        						optionToContent: function(opt) {
                                    var axisData = opt.xAxis[0].data;
                                    var series = opt.series;
                                    var table = '<div class="list_sch" style="top: 0px">' 
                                        		 +'<div class="sch_area"><button type="button" name="button" class="btn_down" style="margin:4px;" onclick="btnDownload();">다운로드</button></div></div>'
                                    			 + '<table id="chartdata" style="width:100%;text-align:center"><tbody><tr>'
                                                 + '<td>날짜</td>'
                                                 + '<td>' + series[0].name + '</td>'
                                                 + '<td>' + series[1].name + '</td>'
                                                 + '<td>' + series[2].name + '</td>'
                                                 + '</tr>';
                                    for (var i = 0, l = axisData.length; i < l; i++) {
                                        table += '<tr>'
                                                 + '<td>' + axisData[i] + '</td>'
                                                 + '<td>' + series[0].data[i] + '</td>'
                                                 + '<td>' + series[1].data[i] + '</td>'
                                                 + '<td>' + series[2].data[i] + '</td>'
                                                 + '</tr>';
                                    }
                                    table += '</tbody></table>';
                                    return table;
                                },
                                contentToOption: function () {
                                  console.log(arguments);
                                }
        					},
        					magicType : {
        						show : true,
        						title: {
        				          line: '선형차트',
        				          bar: '막대차트',
        						},
        						type : [ 'line', 'bar' ],
        					}, 
        					restore : {
        						show : true,
        						title : '복원'
        					},
        					saveAsImage : {
        						show : true,
        						title : '이미지로저장',
        						leng : ['저장']
        					},
        					   
        				}
        			},
                  dataZoom: [  
                    {
                      startValue: '2025-01-01'
                    },
                    {
                      type: 'inside'
                    }
                  ],
                  series: [
                	    {
                	      name: 'CPU',
                	      type: 'line',
                	      showSymbol: false,
                	      data: data.map(function (item) {
                	        return item[1]; 
                	      }),
                	      lineStyle: {
                	        color: '#FF0066' 
                	      }
                	    },
                	    {
                	      name: 'Disk',
                	      type: 'line',
                	      showSymbol: false,
                	      data: data.map(function (item) {
                	        return item[2]; 
                	      }),
                	      lineStyle: {
                	        color: '#0066FF'
                	      }
                	    },
                	    {
                	      name: 'Memory',
                	      type: 'line',
                	      showSymbol: false,
                	      data: data.map(function (item) {
                	        return item[3]; 
                	      }),
                	      lineStyle: {
                	        color: '#00CCCC'
                	      }
                	    }
                	  ],
                	  animation: true,
                	  animationDuration: 500
                });
        },
        error: function (request, status, error) {
            alert("데이터 조회 불가");
        }
    });
}
function bar_graph_left3(result, status, ansyn) {
    console.log("hi");
    var month = [];
    var cases = [];  
    var path_cnt = [];
    var total = [];
    
    var agent_data = [
    	{ value: 4, name: '미연결', itemStyle: { color: '#d94b4b' } },
    	  { value: 38, name: '연결', itemStyle: { color: '#91CC75' } }
    	]; 

    	var echartbar = echarts.init(document.getElementById('bar_graph_left2'));

    	echartbar.setOption({
    	  title: {
    	    text: '에이전트 통신 연결 현황',
    	    left: 'left',
    	    textStyle: {
    	      fontWeight: 'bold'
    	    }
    	  }, 
    	  tooltip: {
    	    trigger: 'item'
    	  }, 
    	  legend: {
    		  top: 'top',
    		  data: ['미연결', '연결'],
    		  formatter: function (name) {
    		    var item = agent_data.find(function(i) { return i.name === name; });
    		    return item ? name + ' (' + item.value + ')' : name;
    		  },
    		  textStyle: {
//     		    fontWeight: 'bold',
    		    color: '#000'
    		  }
    		},
    	  series: [
    	    {
    	      type: 'pie',
    	      avoidLabelOverlap: false,
    	      radius: ['30%', '70%'],
    	      itemStyle: {
    	        borderRadius: 10,
    	        borderColor: '#fff',
    	        borderWidth: 2
    	      },
    	      label: {
    	        show: true,
    	        position: 'outside',
    	        formatter: function (params) {
    	            return params.name + ' (' + params.value + ')';
    	          }
    	      },
    	      labelLine: {
    	        show: true
    	      },
    	      data: agent_data
    	    }
    	  ]
    	});
    	
    	var totalDiv;
		totalDiv = "<div id='spanid' style='display:inline-block; position:absolute; left: 441px; top: 190px; transform: translateX(-50%) translateY(-50%); text-align:center;'>";
		totalDiv += '<h4 style="margin: 0; font-size: 14px;" class="circle_cnt" >총 대상수 <br> 42 </h4></div>';
		
		if ($("#bar_graph_left2").find("#spanid").length > 0) {
		    $("#spanid").remove();
		}
		 
		$("#bar_graph_left2").append(totalDiv);
}
function bar_graph_left4(result, status, ansyn) {
    console.log("hi"); 
    var month = [];
    var cases = [];  
    var path_cnt = [];
    var total = [];
    
    var agent_data = [
    	{ value: 4, name: '미연결', itemStyle: { color: '#d94b4b' } },
    	{ value: 38, name: '연결', itemStyle: { color: '#91CC75' } }
    	]; 
  
    	var echartbar = echarts.init(document.getElementById('chart_bottom_div2'));

    	echartbar.setOption({
    	  title: {
    	    text: '에이전트 타겟 연결 현황',
    	    left: 'left',
    	    textStyle: {
    	      fontWeight: 'bold'
    	    }
    	  }, 
    	  tooltip: {
    	    trigger: 'item'
    	  }, 
    	  legend: {
    		  top: 'top',
    		  data: ['미연결', '연결'],
    		  formatter: function (name) {
    		    var item = agent_data.find(function(i) { return i.name === name; });
    		    return item ? name + ' (' + item.value + ')' : name;
    		  },
    		  textStyle: {
//     		    fontWeight: 'bold',
    		    color: '#000'
    		  }
    		},
    	  series: [
    	    {
    	      type: 'pie',
    	      avoidLabelOverlap: false, 
    	      radius: ['30%', '70%'],
    	      itemStyle: {
    	        borderRadius: 10,
    	        borderColor: '#fff',
    	        borderWidth: 2
    	      },
    	      label: {
    	        show: true,
    	        position: 'outside',
    	        formatter: function (params) {
    	            return params.name + ' (' + params.value + ')';
    	          }
    	      },
    	      labelLine: {
    	        show: true
    	      },
    	      data: agent_data
    	    }
    	  ]
    	});
    	
    	var totalDiv;
		totalDiv = "<div id='spanid' style='display:inline-block; position:absolute; left: 441px; top: 190px; transform: translateX(-50%) translateY(-50%); text-align:center;'>";
		totalDiv += '<h4 style="margin: 0; font-size: 14px;" class="circle_cnt" >총 대상수 <br> 42 </h4></div>';
		
		if ($("#chart_bottom_div2").find("#spanid").length > 0) {
		    $("#spanid").remove();
		}
		 
		$("#chart_bottom_div2").append(totalDiv);
}  

 
function alertShowData(type, dataList){
	// Cpu, memory, Disk
	for(i=1 ; i < dataList.length+1; i++){
		if (dataList[i] != null && dataList[i] > alertData){
			var html = "";  
			if(i==1) html += "CPU"
			else if(i==2) html += "Memory"
			else if(i==3) html += "Disk"  
			console.log("i",i); 
			html += " 사용량이 " + alertData+"% 초과하였습니다.";
			$(".reconAlaert").text(html);
			
			if(type=="recon"){
				$(".reconDatePopup").show();
			}else{
				$(".picenterDatePopup").show();
			}
			break; 
		}  
	}
}
function GridDraw(type, dataList){
	console.log(dataList);
	// Cpu, memory, Disk
	$("#"+type+"Grid").jqGrid({
		//url: 'data.json',
		datatype: "local",
		dataList:dataList,  
		colNames:['날짜', 'Cpu', 'Memory','Disk'],
		colModel: [
			{ index: 'DATE', 			name: 'DATE',			width: 0, 	align: 'center'},
			{ index: 'Cpu', 		name: 'Cpu',		width: 40, 	align: 'center'},
			{ index: 'Memory', 			name: 'Memory',			width: 0, 	align: 'center'},
			{ index: 'Disk', 			name: 'Disk',			width: 0, 	align: 'center'}
		],
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: $("#"+type+"Grid").parent().width(),
		height: 250,    
		loadonce: true, // this is just for the demo
	   	autowidth: true,
		shrinkToFit: true,
		rownumbers : false, // 행번호 표시여부
		rownumWidth : 30, // 행번호 열의 너비	
		rowNum:25,
		rowList:[25,50,100],		
		pager: "#reconGridPager",
		cmTemplate: {sortable: false},
		//jqgrid의 특성상 - rowpos의 이벤트는 onSelectCell, beforeEditCell 다 해주어야 함
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	  	
	  	},
	  	ondblClickRow: function(nRowid, icol, cellcontent, e){
	  	},
	  	onCellSelect: function(rowid,icol,cellcontent,e) { 
	  	},
		loadComplete: function(data) {},
	    gridComplete : function() {
	    }
	});
}
$(".btnReconDatePopupClose").click(function() {
	$(".reconDatePopup").hide();
});
$(".btnCancleReconDatePopup").click(function() {
	$(".reconDatePopup").hide();
});  
$(".btnCanclePIcenterDatePopup").click(function() {
	$(".picenterDatePopup").hide();
});
$(".btnPICenterDatePopupClose").click(function() {
	$(".picenterDatePopup").hide();
}); 
$("#ReconGridCancel").click(function() {
	if (savedReconChartOption) {
	    $("#reconGridDraw").hide();
	    $("#bar_graph_left").show();
	    
	    console.log(savedReconChartOption); 
	
	    
	    setTimeout(function () {
	      var echartbar = echarts.getInstanceByDom(document.getElementById('bar_graph_left'));
	      if (!echartbar) {
	        echartbar = echarts.init(document.getElementById('bar_graph_left'));
	      }
	      echartbar.setOption(savedReconChartOption, true);
	    }, 10);
	  }
	});
</script>
	<!-- wrap -->
</body>
</html>
