<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%@ include file="../../include/header.jsp"%>
<style>
section {
	padding: 0 65px 0 45px;
}

header {
	background: none;
}

h4 {
	margin: 20px 0 0 1px;
	font-size: 16px;
	font-weight: normal;
}

.circle_server_total_cnt, .circle_server_complete_cnt,
	.circle_db_total_cnt, .circle_db_complete_cnt {
	font-size: 14px !important;
	padding: 0 !important;
}

#todo_result_list, #todo_result_approval, #todo_result_schedule{
	text-align: right;
	font-size: 20px;
	color: #fff;
}

.ui-widget.ui-widget-content{
	border: none;
}
.ui-jqgrid-hdiv {
	border: 1px solid #c8ced3;
}
.ui-jqgrid tr.jqgrow td{
	height: 33.7px;
}
.left_area{
	top: 0;
}
#circleGraph canvas[data-zr-dom-id="zr_0"]{
	cursor: default;
}
@media screen and (-ms-high-contrast: active) , ( -ms-high-contrast :
	none) {
	body{
		height: auto !important;
	}
}

@font-face {
      font-family: "NotoSansKR-Light";
      src: url("../resources/assets/fonts/NotoSansKR-Light.otf");
}
</style>

<!-- section -->
<section id="section">
	<!-- container -->
	<div class="container_main2 grid-stack">
		<!-- left list-->
		<c:set var="serverCnt" value="${fn:length(aList)}" />
<!-- 		검색 스케줄 현황 -->   
		<div id="searchSchudleTree" class="grid-stack-item" gs-x="0" gs-y="0" gs-w="8" gs-h="38">
			<div class="grid-stack-item-content" style="display: flex; flex-direction: column; height: 95%;"> 
				<h4 style="color: #0c4da2; margin-top: 0px; ">검색 스케줄 현황</h4>
				<table  class="sch_target_tbl" style="margin-bottom: 5px;">
					<tbody>
						<tr>
							<td id ="searchSchduleTable">
								<input type="date" id="fromDate" style="text-align: center; height: 30px; padding-left: 5px; border: 1px solid #c8ced3;"
										readonly="readonly" value="${fromDate}"> 
								<span style="width: 10%; margin-right: 3px; color: #000">~</span> 
								<input type="date" id="toDate" style="text-align: center; height: 30px; padding-left: 5px; border: 1px solid #c8ced3;"
										readonly="readonly" value="${toDate}">
								<input type="hidden" id="oldDate" readonly="readonly" value="">
							</td>
							<td id ="searchSchduleBtn" style="display: none;"> 
							<button type="button" id="btn_sch_search" class="btn_sch_target" style="margin: 5px; height: 25px;"></button>
							</td>
						</tr>
					</tbody>
				</table> 
				 <div class="left_box" style="flex-grow: 1;">
					<div id="div_all" class="select_location"
						style="overflow-y: auto; overflow-x: auto; height: 100%; background: #ffffff; white-space: nowrap; border: none;">
						<div id="div_List"></div>
					</div>
				</div>
			</div>
		</div>
		<div id="targetListTree" class="grid-stack-item" gs-x="0" gs-y="40" gs-w="8" gs-h="38">
			<div class="grid-stack-item-content" style="display: flex; flex-direction: column; height: 95%;"> 
				<h4 style="position: relative; color: #0c4da2; margin-top: 0px; ">자산 현황</h4>
				<table class="sch_target_tbl" style="width: 100%;">
					<colgroup>
						<col width="*"> 
						<col width="50">
					</colgroup>
					<tbody>
						<tr>
							<td>
								<input type="text" name="targetSearch" class=""
								id="targetSearch" placeholder="호스트명, IP, 그룹을 기입하세요"
								style="height: 30px; width:100%; padding-left: 5px; border: 1px solid #c8ced3;">
							</td>
							<td>
								<button type="button" id="btn_sch_target" class="btn_sch_target"
									style="margin: 5px;"></button>
							</td>
						</tr>
					</tbody>
				</table>
				<div class="left_box" style="flex-grow: 1;">
					<div id="jstree" class="left_box"
						style=" padding: 0; border: none;"></div>
				</div>
			</div>
		</div> 
		
		<div class="grid-stack-item" gs-x="9" gs-y="3" gs-h="9" gs-w="5" style="padding: 0 5px;">
			<div class="todo_box_list" style="background: #0c4da2; width: 100%; height: 100%;">
				<div class="grid-stack-item-content">
					<p style="color: #fff;"> 
						결재 신청/관리<br> &nbsp;
					</p> 
					<p id="todo_result_list" style="bottom: 7px;position: absolute;right: 15px;">건</p>
				</div>
			</div>
		</div>
		<div class="grid-stack-item" gs-x="14" gs-y="3" gs-h="9" gs-w="5"  style="padding: 0 5px;">
			<div class="todo_box_approval" style="background: #0c4da2; width: 100%; height: 100%;">
				<div class="grid-stack-item-content">
					<p style="color: #fff;"> 
						결재 신청/관리<br> &nbsp;
					</p> 
					<p id="todo_result_approval" style="bottom: 7px;position: absolute;right: 15px;">건</p>
				</div>
			</div>
		</div>
		<div class="grid-stack-item" gs-x="19" gs-y="3" gs-h="9" gs-w="5"  style="padding: 0 5px;">
			<div class="todo_box_schedule" style="background: #0c4da2; width: 100%; height: 100%;">
				<p style="color: #fff;"> 
					스케줄 현황<br> &nbsp;
				</p>
				<p id="todo_result_schedule" style="bottom: 7px;position: absolute;right: 15px;">건</p>
			</div>
		</div>

		<div class="top_area_right grid-stack-item" gs-x="31" gs-y="0" gs-w="9" gs-h="16">
			<h4 style="margin-top: 31px; padding-bottom: 6px;">공지사항</h4>
			<div class="location_notice_area">
				<a href="<%=request.getContextPath()%>/user/pi_notice_main">more</a>
			</div>
				<c:forEach items="${noticeList}" var="list">
					<table class="squareBox" style="table-layout: fixed;">
						<tr>
							<td id="notice_title" style="cursor: pointer; width: 300px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">
								${list.NOTICE_TITLE} 
								<input type="hidden" id="notice_detail"	value="${list.NOTICE_ID}">
							</td>
							<td id="notice_date" style="font-size: 12px; text-align: right;">${list.REGDATE}</td>
						</tr>
					</table>
				</c:forEach> 
		</div> 
			<div class="chart_left grid-stack-item" gs-x="9" gs-y="12" gs-w="11" gs-h="66" style="margin: 0px;" gs-no-resize="true" >
				<h4 id="systemStatus" style="position: relative; display: inline-block; color: #0c4da2; margin-top: 23px; padding-bottom: 5px;">서버 검출현황 요약</h4>
				<div class="left_box"
					style="width: 466px; height: 600px; margin-right: 5px; padding: 0 20px;">
					<ul>
						<li style="width: 444px; padding: 0;">
							<div class="chart_box"
								style="height: 470px; padding: 0; border: none; background: #fff;">
								<div id=circleGraph
									style="height: 100%; width: 448px; float: left;"></div>
								<div id="circleData" style="display: none;"></div>
							</div>
							<div class="chart_box" style="background: #fff; border: 0 none;">
								<p style="height: 42px; margin-left: 13px; font-size: 14px; line-height: 47px;">
									<label id="topStatus">파일서버</label> 검출진행률
									<span class="circle_server_percent" style="font-size: 28px; color: #0078D7;"></span> 총 
									<span	class="circle_server_total_cnt"></span> 중 
									<span class="circle_server_complete_cnt"></span> 완료
								</p>
								<progress class="file_progress" value="" max="100"></progress>
								<p style="height: 42px; margin-left: 13px; font-size: 14px; line-height: 47px;">
									<label id="bottomStatus">DB서버</label> 검출진행률 
									<span class="circle_db_percent" style="font-size: 28px; color: #0078D7;"></span> 총 
									<span class="circle_db_total_cnt"></span> 중 
									<span class="circle_db_complete_cnt"></span> 완료
								</p>
								<progress class="db_progress" value="0" max="100"></progress>
							</div>
						</li>
					</ul>
				</div>
			</div>
			<div class="chart_center grid-stack-item" gs-x="20" gs-y="2" gs-w="11" gs-h="66" style="margin: 0px;" >
				<h4 id="serverRank" style="color: #0c4da2; display:inline-block; margin-top: 23px; padding-bottom: 5px;">개인정보 보유 서버순위</h4>
				<div class="left_box_dash_rank"
					style="width: 468px; height: 600px; padding: 10px 0;">
					<ul>
						<li class="tagetBox"
							style="width: 456px; margin-left: 10px;">
							<div class="grid_top" style="float: right;">
								<div class="left_box2"
									style="overflow: hidden; max-height: 575px;">
									<table id="rankGrid"></table>
								</div>
							</div>
						</li>
					</ul>
				</div>
				<div id="excelData" style="display: none;"></div>
			</div>
			<div class="grid-stack-item" gs-x="31" gs-y="18" gs-w="9" gs-h="18">
				<h4 style="color: #0c4da2; margin-top: 0px; padding-bottom: 5px;" id="tbl_host_name">검출 현황</h4>
				<div class="left_box_dash_detection"
					style="width: 400px; height: 146px;">
					<ul>
						<li class="tagetBox" style="width: 388px; margin-top: 5px; margin-left: 5px;">
							<div class="chart_box" style="background: #fff; border: 0 none;">
								<table id="rrn_detection_tbl" class="squareBox">
									<tr>
										<td>검출 파일수</td>
										<td id="total_tbl" style="text-align: right;"></td>
									</tr>
									<tr>
										<td>미조치 파일수</td>
										<td id="incomplete_tbl" style="text-align: right;"></td>
									</tr>
									<tr>
										<td>조치 파일수</td>
										<td id="complete_tbl" style="text-align: right;"></td>
									</tr>
									<tr>
										<td style="border-bottom: none;">검출불가 파일수</td>
										<td  id="inaccess_tbl" style="text-align: right; border-bottom: none;"></td>
									</tr>
								</table> 
							</div>
						</li>
					</ul>
				</div>  
			</div>
			<div class="grid-stack-item" gs-x="31" gs-y="37" gs-w="9" gs-h="18">
				<h4 style="color: #0c4da2; display:inline-block; padding-bottom: 5px;" id="complete_name">조치 현황</h4>
				<div class="left_box_dash_complete"
					style="width: 400px; height: 92px;">
					<ul>
						<li class="tagetBox" style="width: 388px; margin-top: 5px; margin-left: 5px;">
							<div class="chart_box" style="background: #fff; border: 0 none;">
								<table id="rrn_complete_tbl" class="squareBox">
									<tr>
										<td>보유등록 파일수</td>
										<td id="own_tbl" style="text-align: right;"></td>
									</tr>
									<tr>
										<td style="border-bottom: 0px;">오탐등록 파일수</td>
										<td id="fal_tbl" style="text-align: right; border-bottom: 0px;"></td>
									</tr> 
	<!-- 									<tr> -->
	<!-- 										<td style="border-bottom: none;">삭제 파일수</td> -->
	<!-- 										<td id="del_tbl" style="text-align: right; border-bottom: none;"></td> -->
	<!-- 									</tr> -->
								</table>
							</div>
						</li>
					</ul>
				</div>
			</div>
		<div class="clear"></div>
	</div>
</section>
<%@ include file="../../include/footer.jsp"%>

<script type="text/javascript">

var deptList = ${deptList};
var userGroupList = ${userGroupList};
$(function() {
// 	검색 스케줄 현황
	   $('#div_List').jstree({
			// List of active plugins
		   "core" : {
			    "animation" : 0,
			    "check_callback" : true,
				"themes" : { "stripes" : false },
				"data" : deptList,
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
			'plugins' : ["search"],
		}).bind("loaded.jstree", function(e, data) {
			
// 			var data = $("#div_List").jstree(true)._model.data;
			
// 			tableDataDraw(data); // 검출 현황
// 			circleGraph(data); // Pie 그래프
// 			drawrankGrid(data); // 개인정보 보유 서버 순위
			
		}).bind('select_node.jstree', function(evt, data, x) {
			
			if(data.node.data.type == 0){
				var pdata=[];
				$.each(data.node.children_d, function(key, val){
					var nodeData = $("#div_List").jstree(true).get_node(val);
					pdata.push(nodeData);
				});
				
				tableDataDraw(pdata); // 검출 현황
				drawrankGrid(pdata); // 개인정보 보유 서버 순위
				circleGraph(pdata); // Pie 그래프
			}else{
				tableDataDraw(data); // 검출 현황
				drawrankGrid(data); // 개인정보 보유 서버 순위
				circleGraph(data); // Pie 그래프
			}
			
			
			
			
		}); // div_List tree 종료
	    
	    $('#jstree').jstree({
			// List of active plugins
			"core" : {
			    "animation" : 0,
			    "check_callback" : true,
				"themes" : { "stripes" : false },
				"data" : userGroupList,
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
		}).bind("loaded.jstree", function(e, data) {
			tableDataDraw(data); // 검출 현황
			circleGraph(data); // Pie 그래프
			drawrankGrid(data); // 개인정보 보유 서버 순위
		})
	    .bind('select_node.jstree', function(evt, data, x) {
	    	console.log("data",data);
	    	
	    	if(data.parent != "#"){
	    		
	    		var id = data.node.id;
				var target_id = data.node.data.target_id;
				var type = data.node.data.type;
				var ap = data.node.data.ap;
				var text = null;
				var connected = null;
				
				var postData = {
		         		fromDate : $("#fromDate").val(),
		         		toDate : $("#toDate").val(),
		         		target_id : target_id,
		         		id : id,
		         		type : type
		         	}
				console.log(id);
				$.ajax({
					type: "POST",
					url: "/SelectTargetDash",
					////async : false,
					data : postData,
					dataType: "json",
				    success: function (resultMap) {
				    	console.log(resultMap);
				    	console.log("ddde"); 
				    	tableDataDraw(resultMap.data); // 검출 현황
						drawrankGrid(resultMap.data); // 개인정보 보유 서버 순위
						circleGraph(resultMap.data); // Pie 그래프
				    },
				    error: function (request, status, error) {
				        console.log("ERROR : ", error);
				    }
				});
	    		
	    	}
	    	
			
			
			
			
	    	
	    }); // jstree tree 종료
	
});


function btnDownload(){
    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth()+1;
    var yyyy = today.getFullYear();
    
    if(dd<10) dd='0'+dd;
    if(mm<10) mm='0'+mm;

    today = yyyy + "" + mm + dd;
    exportTableToCsv("excelTable", "검출현황_요약_" + today);
};

function exportTableToCsv(tableId, filename) {
	
	if (filename == null || typeof filename == undefined) filename = tableId;
    filename += ".csv";
 
    var BOM = "\uFEFF";
    
    var table = document.getElementById(tableId);
    var csvString = BOM;
    for (var rowCnt = 0; rowCnt < table.rows.length; rowCnt++) {
        var rowData = table.rows[rowCnt].cells;
        for (var colCnt = 0; colCnt < rowData.length; colCnt++) {
            var columnData = rowData[colCnt].innerHTML;
            if (columnData == null || columnData.length == 0) {
                columnData = "".replace(/"/g, '""');
            }
            else {
                columnData = columnData.toString().replace(/"/g, '""'); // escape double quotes
            }
            csvString = csvString + '"' + columnData + '",';
        }
        csvString = csvString.substring(0, csvString.length - 1);
        csvString = csvString + "\r\n";
    }
    
    csvString = csvString.substring(0, csvString.length - 1);
    
    // IE 10, 11, Edge Run
    if (window.navigator && window.navigator.msSaveOrOpenBlob) {
 
        var blob = new Blob([decodeURIComponent(csvString)], {
            type: 'text/csv;charset=utf8'
        });
 
        window.navigator.msSaveOrOpenBlob(blob, filename);
 
    } else if (window.Blob && window.URL) {
        // HTML5 Blob
        var blob = new Blob([csvString], { type: 'text/csv;charset=utf8' });
        var csvUrl = URL.createObjectURL(blob);
        var a = document.createElement('a');
        a.setAttribute('style', 'display:none');
        a.setAttribute('href', csvUrl);
        a.setAttribute('download', filename);
        document.body.appendChild(a);
 
        a.click()
        a.remove();
    } else {
        // Data URI
        var csvData = 'data:application/csv;charset=utf-8,' + encodeURIComponent(csvString);
        var blob = new Blob([csvString], { type: 'text/csv;charset=utf8' });
        var csvUrl = URL.createObjectURL(blob);
        var a = document.createElement('a');
        a.setAttribute('style', 'display:none');
        a.setAttribute('target', '_blank');
        a.setAttribute('href', csvData);
        a.setAttribute('download', filename);
        document.body.appendChild(a);
        a.click()
        a.remove();
    }
    
} // exportTableToCsv 종료

function circleGraph(data) {
	
	var total = [];
	var error = [];
	var searched = [];
	var wait = [];
	var complete = [];
	var scancomp = [];
	var pause = [];
	
	var total_cnt = 0;
	var error_cnt = 0;
	var searched_cnt = 0;
	var wait_cnt = 0;
	var complete_cnt = 0;
	var scancomp_cnt = 0;
	var pause_cnt = 0;
	var stopped_cnt = 0;
	
	var total_server = 0;
	var server = 0;
	var total_db = 0;
	var db = 0;
	
	$.each(data, function(key, val){
		
		if(val.parent == null) return true;
		
		var type = val.data.type ;
		if(type == 0)  return true;
		
		switch (val.data.SEARCH_STATUS) {
		  case "error": 	 ++error_cnt; break;
		  case "scanning": 	 ++searched_cnt; break; 
		  case "stopped": 	 ++complete_cnt; break; 
		  case "notscanned": ++wait_cnt; break;
		  case "scancomp":   ++scancomp_cnt; break;
		  case "paused":  	 ++pause_cnt; break;
		  case "completed":   ++complete_cnt; break;
		  default: console.log(val.data.SEARCH_STATUS);
		}
		
		if(val.data.SERVER_TYPE == "SERVER") {
			if(val.data.SEARCH_STATUS == "completed"){
				++server;
			}
			++total_server;	
		}else if(val.data.SERVER_TYPE == "DB"){
			if(val.data.SEARCH_STATUS == "completed"){
				++db;
			}
			++total_db
		}  
		
	});
	
	total_cnt = error_cnt+searched_cnt+wait_cnt+scancomp_cnt+pause_cnt+complete_cnt;
    
	var echartdoughnut = echarts.init(document.getElementById('circleGraph'));
	echartdoughnut.setOption({
	    tooltip : {
	        trigger: 'item',
	        formatter: "{a} <br/>{b} : {c} ({d}%)"
	    }, 
	    legend: {
	        data:['오류','검색중','결과없음','검출','미검색','일시정지'],
	        padding: [0, 100, 30, 100],
	        bottom: 0
	    },
		textStyle: {
			fontFamily: 'NotoSansR',
			fontSize: 14
		},
	    series : [
	        {
	            name: '시스템현황',
	            type: 'pie',
	            radius : ['30%', '60%'],
	            color : ['#CC0F2E', '#68A62F', '#11088D', '#038FDA', '#6F037F', '#FBCD1F'],
	            center: ['49%', '40%'],
	            data:[
			         {value:error_cnt, name:'오류'},
			         {value:searched_cnt, name:'검색중'},
			         {value:wait_cnt, name:'미검색'},
			         {value:scancomp_cnt, name:'결과없음'},
			         {value:pause_cnt, name:'일시정지'},
			         {value:complete_cnt, name:'검출'}
	            ],
	            itemStyle: { 
	                emphasis: {
	                    shadowBlur: 10,
	                    shadowOffsetX: 0,
	                    shadowColor: 'rgba(0, 0, 0, 0.5)'
	                }
	            }
	        }
	    ]
	});
	
	var server_progress = 0;
	
	if (total_server != 0 && server != 0){
		server_progress = Math.round((server/total_server)*100);
	}
	
	
	// 서버
	$(".circle_server_total_cnt").html((total_server) + "대"); 
	$(".circle_server_complete_cnt").html(server + "대");
	$(".file_progress").val(server_progress);

	var db_progress = 0;
	if (total_db != 0 && db != 0 ){
		db_progress = Math.round((db/total_db)*100);
	}
	
	// DB
	$(".circle_db_total_cnt").html(total_db + "대");
	$(".circle_db_complete_cnt").html(db+" 대");
	$(".db_progress").val(db_progress); 
	var totalDiv;
	totalDiv = "<div id='spanid' style='display:inline-block; position:absolute; left: 221px; top: 188px; transform: translateX(-50%) translateY(-50%); text-align:center;'>";
	totalDiv += '<h4 style="margin: 0;" class="circle_cnt" >총 대상수<br>'+total_cnt+'  </h4></div>';
	
	if ($("#circleGraph").find("#spanid").length > 0) {
	    $("#spanid").remove();
	}
	
	$("#circleGraph").append(totalDiv);
};

var ch_target_id = "";
function change_target_id(target_id) {
	ch_target_id = target_id;
}

$(document).ready(function() {
	
	var targetDiv = $("#searchSchudleTree"); // 대상 요소 선택

    // MutationObserver 생성
    var observer = new MutationObserver(function(mutations) {
        mutations.forEach(function(mutation) {
            if (mutation.type === "attributes" && mutation.attributeName === "gs-w") {
                var gs_w = parseInt(targetDiv.attr("gs-w")); // gs-w 속성 값을 정수로 변환
                console.log("gs_w", gs_w)
                if (gs_w > 7 ) {
                	$("#searchSchduleTable").show(); 
                    $("#searchSchduleBtn").hide();
                } else {
                	$("#searchSchduleTable").hide();
                    $("#searchSchduleBtn").show();
                }
            }
        });
    });

    // targetDiv 요소의 속성 변화를 감지
    observer.observe(targetDiv[0], {
        attributes: true // 속성 변화를 감지
    });
	
    var gs_w = parseInt($("#searchSchudleTree").attr("gs-w")); // gs-w 속성 값을 정수로 변환
  
    if (gs_w <= 7) {
        $("#searchSchduleTable").hide();
        $("#searchSchduleBtn").show();
    }else {
        $("#searchSchduleTable").show();
        $("#searchSchduleBtn").hide();
    }
	
// 	대시보드 데이터 조회
// 	$.ajax({
// 			type: "POST",
// 			url: "/dash_all_data",
// 			////async : false,
// 			data : postData,
// 			dataType: "json",
// 		    success: function (resultMap) {
// 		    	$("#todo_result_list").html(resultMap.LIST_TOTAL + "건");
// 		    	$("#todo_result_list").css("color", "#FFF");
// 		    },
// 		    error: function (request, status, error) {
// 		        console.log("ERROR : ", error);
// 		    }
// 		});

	
	
// 	공지사항 팝업
	if("${noticePop}" != "" && "${noticePop}" != null){
		
		var id = "${noticePop.NOTICE_ID}";
		
		var result = getCookie('notice_popup');
		if (result == "end") {
		} else {
			var winRef;	
			var notice_url = "${getContextPath}/popup/noticePop";
			var winWidth = 1260;
	    	var winHeight = 750; 
			var popupOption= "width="+winWidth+", height="+winHeight + ", scrollbars=no, resizable=no"; 	
			window.open(notice_url+ "?id=" + id,id,popupOption);
		}
		
	}
	
	// 공지사항 팝업 실행 및 쿠키
	function getCookie(name) {
		   var cookieName = name + "=";
		   var x = 0;
		   while ( x <= document.cookie.length ) {
		      var y = (x+cookieName.length);
		      if ( document.cookie.substring( x, y ) == cookieName) {
		         if ((lastChrCookie=document.cookie.indexOf(";", y)) == -1)
		            lastChrCookie = document.cookie.length;
		         return decodeURI(document.cookie.substring(y, lastChrCookie));
		      }
		      x = document.cookie.indexOf(" ", x ) + 1;
		      if ( x == 0 )
		         break;
		      }
		   return "";
	}
	
	setSelectDate();
	
	var postData = {
		user_no : '${memberInfo.USER_NO}',
		fromDate : $("#fromDate").val(),
		toDate : $("#toDate").val()
	};
	

	var pattern = '${pattern}'.split('[{').join('').split('}]').join('');
	pattern = pattern.split('}, {');
	
	$(".todo_box_list").click(function() { // 개인정보 조치
		document.location.href = "<%=request.getContextPath()%>/approval/pi_search_list";
	});
	$(".todo_box_approval").click(function() { // 결재 신청 관리
		document.location.href = "<%=request.getContextPath()%>/approval/pi_search_approval_list";
	});
	$(".todo_box_schedule").click(function() { // 스케줄 현황
		document.location.href = "<%=request.getContextPath()%>/search/search_list";
	});
	
	var oToday = new Date();
	$("#toDate").val(getFormatDate(oToday));

	var oOldeDate = new Date(oToday.setDate(oToday.getDate() - 91));
	$("#oldDate").val(getFormatDate(oOldeDate));
	
	var dashData = {
       	user_no : '${memberInfo.USER_NO}',
       	fromDate : $("#fromDate").val(),
     	toDate : $("#toDate").val()
    };
	
// 	개인정보 조지
	$.ajax({
		type: "POST",
		url: "/dash_dataTodoList",
		////async : false,
		data : postData,
		dataType: "json",
	    success: function (resultMap) {
	    	$("#todo_result_list").html(resultMap.LIST_TOTAL + "건");
	    	$("#todo_result_list").css("color", "#FFF");
	    },
	    error: function (request, status, error) {
	        console.log("ERROR : ", error);
	    }
	});
	
// 	결재 신청/관리
	$.ajax({
		type: "POST",
		url: "/dash_dataTodoApproval",
		////async : false,
		data : postData,
		dataType: "json",
	    success: function (resultMap) {
	    	$("#todo_result_approval").html(resultMap.APPROVAL_TOTAL + "건");
	    	$("#todo_result_approval").css("color", "#FFF");
	    },
	    error: function (request, status, error) {
	        console.log("ERROR : ", error);
	    }
	});
	
// 	스케줄 현황
	$.ajax({
		type: "POST",
		url: "/dash_dataTodoSchedule",
		////async : false,
		data : dashData,
		dataType: "json",
	    success: function (resultMap) {
	    	$("#todo_result_schedule").html(resultMap.SCHEDULE_TOTAL + "건");
	    	$("#todo_result_schedule").css("color", "#FFF");
	    },
	    error: function (request, status, error) {
	        console.log("ERROR : ", error);
	    }
	});
	
	// 검색 스케줄 현황 tree
// 	$.ajax({
// 		type: "POST",
// 		url: "/group/dashListDept",
// 		////async : false,
// 		data: postData,
// 	    success: function (result) {
// 	    	if(result.resultCode == -1){
// 	    		return;
// 	    	}
	    	
// 	    	$('#div_List').jstree(true).settings.core.data = result.data;
// 	    	$('#div_List').jstree(true).refresh();

// // 			var data = $("#div_List").jstree(true)._model.data;
			
// 			tableDataDraw(result.data); // 검출 현황
// 			circleGraph(result.data); // Pie 그래프
// 			drawrankGrid(result.data); // 개인정보 보유 서버 순위
	    	
// 	    },
// 	    error: function (request, status, error) {
// 	        console.log("ERROR : ", error)
// 	    }
// 	});
	
	$("#fromDate").datepicker({
        changeYear : true,
        changeMonth : true,
        dateFormat: 'yy-mm-dd',
        onSelect: function(dateText) {
         	var postData = {
         		fromDate : $("#fromDate").val(),
         		toDate : $("#toDate").val(),
         	}
         	$.ajax({
        		type: "POST",
        		url: "/group/dashListDept",
        		////async : false,
        		data: postData,
        	    success: function (result) {
        	    	if(result.resultCode == -1){
        	    		return;
        	    	}
        	    	
        	    	$('#div_List').jstree(true).settings.core.data = result.data;
        	    	$('#div_List').jstree(true).refresh();
        	    	
        	    	tableDataDraw(result.data); // 검출 현황
        			circleGraph(result.data); // Pie 그래프
        			drawrankGrid(result.data); // 개인정보 보유 서버 순위
        	    	
        	    },
        	    error: function (request, status, error) {
        	        console.log("ERROR : ", error)
        	    }
        	});
        	
	    }
        
    });

    $("#toDate").datepicker({
        changeYear : true,
        changeMonth : true,
        dateFormat: 'yy-mm-dd',
        onSelect: function(dateText) {
	        console.log("Selected date: " + dateText + "; input's current value: " + this.value);
	        var postData = {
	         		fromDate : $("#fromDate").val(),
	         		toDate : $("#toDate").val(),
	         	}
	         	$.ajax({
	        		type: "POST",
	        		url: "/group/dashListDept",
	        		////async : false,
	        		data: postData,
	        	    success: function (result) {
	        	    	if(result.resultCode == -1){
	        	    		return;
	        	    	}
	        	    	
	        	    	$('#div_List').jstree(true).settings.core.data = result.data;
	        	    	$('#div_List').jstree(true).refresh();
	        	    	
	        	    },
	        	    error: function (request, status, error) {
	        	        console.log("ERROR : ", error)
	        	    }
	        	});
	        	
	    }
    });
    
    var to = true;
    $('#btn_sch_target').on('click', function(){
    	
    	if($("#fromDate").val() > $("#toDate").val()){
			alert("입력한 끝 날짜가 시작 날짜 보다 빠릅니다.");
			return;
		}

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
	
}); // ready 종료

var aut = "manager";

function drawrankGrid(data){
	$("#rankGrid").clearGridData();
	var mydata = [];
	var rowCnt  = 0;
	
	$.each(data, function(key, val){
		if(val.parent == null) return true;
		
		var type = val.data.type ;
		if(type == 0)  return true;
		
		var item = {
		        SERVER: val.text, 
		        FILE: Number(val.data.PATH_CNT),
		        COUNT: Number(val.data.TOTAL)
		    };
		mydata.push(item);
		
	});
	

	$("#rankGrid").jqGrid('setGridParam', { data: mydata }).trigger('reloadGrid');
	
	fn_drawrankGrid(mydata);
}


function fn_drawrankGrid(mydata) {
	
	var gridWidth = $("#rankGrid").parent().width();
	
	console.log(mydata);	
	$("#rankGrid").jqGrid({
		datatype: "local",
		data: mydata, 
	   	mtype : "POST",
		colNames:['서버','검출파일수(컬럼수)','검출건수'],
		colModel: [
			{ index: 'SERVER', 	name: 'SERVER', 	width: 130, align: 'center'},
			{ index: 'FILE', 	name: 'FILE',  width: 70, align: 'right', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, sortable: true, sorttype: 'number' },
			{ index: 'COUNT', 	name: 'COUNT', width: 70, align: 'right', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, sortable: true, sorttype: 'number' }
		],
		loadonce:true,
		sortname: 'COUNT', // 기본적으로 FILE 컬럼을 기준으로 정렬
	    sortorder: 'desc', // 내림차순으로 정렬
	   	autowidth: true,
		shrinkToFit: true,
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: gridWidth,
		height: 540,
		rownumbers : false, // 행번호 표시여부
		rownumWidth : 35, // 행번호 열의 너비	
		rowNum:50,
		rowList:[50,100,200],
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	
	  	},
	  	afterSaveCell : function(rowid,name,val,iRow,ICol){ // 로우 데이터 변경하고 엔터치거나 다른 셀 클릭했을때 발동
        },
	  	afterSaveRow : function(rowid,name,val,iRow,ICol){ // 로우 데이터 변경하고 엔터치거나 다른 셀 클릭했을때 발동
        },
		loadComplete: function(data) {
	    },
	    gridComplete : function() {
	    }
	});
};

function tableDataDraw(data){
	var total = 0; // 검출 파일 수
	var incomplete = 0; // 미조치 파일 수
	var complete = 0; // 조치 파일 수
	var inaccess = 0; // 검출 불가 파일 수
	
	var own = 0; // 보유 등록 파일 수
	var fal = 0; // 오탐 등록 파일 수
	var del = 0; // 삭제 파일 수
	  
	$.each(data, function(key, val){
		
		if(val.parent == null) return true;
	
		var type = val.data.type ;
		
		if(type == 0) return true;
		
		total += val.data.PATH_CNT;
		incomplete += val.data.TRUE_CNT; 
		inaccess += val.data.INACCESS_CNT;
		
		own += val.data.TRUE_CNT;
		fal += val.data.FALSE_CNT;
// 		del += val.data.; 
	})
	
	complete = (own+fal);
	incomplete = (total - complete);
	
	$("#total_tbl").html(total);
	$("#incomplete_tbl").html(incomplete);
	$("#complete_tbl").html(complete);
	$("#inaccess_tbl").html(inaccess);
	
	$("#own_tbl").html(own);
	$("#fal_tbl").html(fal);
	
	
	
};

function getFormatDate(oDate){
    var nYear = oDate.getFullYear();           // yyyy 
    var nMonth = (1 + oDate.getMonth());       // M 
    nMonth = ('0' + nMonth).slice(-2);         // month 두자리로 저장 

    var nDay = oDate.getDate();                // d 
    nDay = ('0' + nDay).slice(-2);             // day 두자리로 저장

    return nYear + '-' + nMonth + '-' + nDay;
};

function setSelectDate(){
    var oToday = new Date();
    $("#toDate").val(getFormatDate(oToday));

    var oFromDate = new Date(oToday.setDate(oToday.getDate() - 91));
    $("#fromDate").val(getFormatDate(oFromDate));
};


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

$(document).on("click", "#notice_title", function (e){
	var id = $(this).children("#notice_detail").val();
	
	if (id != "0") {
        var pop_url = "${getContextPath}/popup/noticeDetail";
    	var winWidth = 1260;
    	var winHeight = 750;
    	var popupOption= "width="+winWidth+", height="+winHeight + ", left=0, top=0, scrollbars=no, resizable=no, location=no"; 	
    	var pop = window.open(pop_url + "?id=" + id,id,popupOption);
    }
    else {
    	getLowPath(id);
    }
});




</script>
</body>
</html>