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
<section id="section" >
	<!-- container -->
	<div class="container_main" style="height: 801px;">
		<div id="pic_main_dashboard">
			<div id="left-div" style="float: left;">
				<h4 style="position: relative; color: #0c4da2; margin-top: 0px; padding-bottom: 5px;">검색 스케줄 현황</h4>
				<table class="sch_target_tbl" style="margin-bottom: 5px;">
					<tbody>
						<tr> 
							<td style="width: 293px;">
								<input type="date" id="fromDate" style="text-align: center; width: 47%; height: 30px; padding-left: 5px; border: 1px solid #c8ced3;"
										readonly="readonly" value="${fromDate}"> 
								<span style="width: 10%; margin-right: 3px; color: #000">~</span> 
								<input type="date" id="toDate" style="text-align: center; width: 47%; height: 30px; padding-left: 5px; border: 1px solid #c8ced3;"
										readonly="readonly" value="${toDate}">
								<input type="hidden" id="oldDate" readonly="readonly" value="">
							</td>
							<td>
								<button type="button" id="btn_sch_target2" class="btn_sch_target" style="margin: 5px;"></button>
							</td>
						</tr>
					</tbody>
				</table>
				<div class="left_box" style="height: 313px; width: 351px;">
					<div id="div_all" class="select_location"
						style="overflow-y: auto; overflow-x: auto; height: 100%; background: #ffffff; white-space: nowrap; border: none;">
						<div id="div_List"></div>
					</div>
				</div>
				<h4 style="position: relative; color: #0c4da2; margin-top: 13px;">자산 현황</h4>
				<table class="sch_target_tbl">
					<tbody>
						<tr>
							<td><input type="text" name="targetSearch" class=""
								id="targetSearch" placeholder="호스트명, IP, 그룹을 기입하세요"
								style="width: 293px; height: 30px; padding-left: 5px; border: 1px solid #c8ced3;">
							</td>
							<td>
								<button type="button" id="btn_sch_target" class="btn_sch_target" style="margin: 5px;"></button>
							</td>
						</tr>
					</tbody>
				</table>
				<div class="left_box" style="height: 313px; width: 351px;">
					<div id="jstree" class="left_box"
						style="height: 300px; width: 339px; padding: 0; border: none;"></div>
				</div>
				
			</div>
			<div id="right-div" style="float: left; width: 1430px;">
				<div id="top-div" style="margin-top: 9px;">
					<div class="top_area_left">
						<div class="todo_box_list" style="background: #0c4da2;">
							<p style="color: #fff;">
								개인정보 조치<br> (미완료 누적)
							</p>
							<p id="todo_result_list">건</p>
						</div>
						<div class="todo_box_approval"
							style="margin: 1px 27px;background: #0c4da2;">
							<p style="color: #fff;">
								결재 신청/관리<br> &nbsp;
							</p>
							<p id="todo_result_approval">건</p>
						</div>
						<div class="todo_box_schedule"
							style="background: #0c4da2;">
							<p style="color: #fff;">
								스케줄 현황<br> &nbsp;
							</p>
							<p id="todo_result_schedule">건</p>
						</div>
					</div>
					<div class="top_area_right">
						<h4 style="margin-top: 0px; padding-bottom: 6px;">공지사항</h4>
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
				</div>
				<div id="middle-div">
					<div class="chart_area" style="position: absolute; top: 115px;">
						<div class="chart_left" style="margin-left: 20px;">
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
						<div class="chart_center" style="margin-left: 27px;">
							<h4 id="serverRank" style="color: #0c4da2; display:inline-block; margin-top: 23px; padding-bottom: 5px;">개인정보 보유 서버순위</h4>
							<div class="left_box_dash_rank" >
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
					</div>
					<div>
						<div class="chart_right">
							<h4 style="color: #0c4da2; margin-top: 23px; padding-bottom: 5px;" id="tbl_host_name">검출 현황</h4>
							<div class="left_box_dash_detection" style="width: 400px; height: 146px;">
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
							<h4 style="color: #0c4da2; display:inline-block; padding-bottom: 5px;" id="complete_name">조치 현황</h4>
							<div class="left_box_dash_complete"style="width: 400px; height: 92px;">
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
											</table>
										</div>
									</li>
								</ul>
							</div>
									
						</div>
					</div>
				</div>
			</div>
		</div>
	</div> 
	<div class="lg_add_graph">
		<div style="border: 1px solid #c8ced3; border-radius: 0.25rem;">
			<div id="lisk_serach" style="margin-right: 10px; margin-bottom: 8px;">
				<h4 style=" color: #0c4da2; margin: 12px 5px 5px 8px; float: left;">일자별 검색 현황</h4>
				<table class="user_info approvalTh" style="width: 21%; margin-top: 7px;" id="totalTable">
					<tbody>
						<tr>
							<td style="width: 22.3vw;">
								<input type="month" id="fromGraphDate" class="allDateSearch" style="text-align: center;  width:45%;" readonly="readonly" value="${fromDate}" >
								<span class="allDateSearch">~</span>
								<input type="month" id="toGraphDate" style="text-align: center;  width:45%;" readonly="readonly" value="${toDate}">
								<input type="button" name="button" class="btn_look_approval" id="btnSearch" style="margin-top: 7px; margin-right: 4px;">
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="mid_box" style="border: 1px solid #c8ced3; width: 95%; border-radius: 0.25rem; height: 500px; margin-left: 49px;">
				<div id="total_bar_graph" style="width: 100%; height: 100%;"></div>
			</div>
			<div class="mid_box" style="border: 1px solid #c8ced3; width: 95%; border-radius: 0.25rem; height: 500px; margin: 41px 49px;">
				<div id="server_bar_graph" style="width: 100%; height: 100%;"></div>
			</div>
			<div class="mid_box" style="border: 1px solid #c8ced3; width: 95%; border-radius: 0.25rem; height: 500px; margin: 41px 49px;">
				<div id="group_bar_graph" style="width: 100%; height: 100%;"></div>
			</div>
			<div class="mid_box" style="border: 1px solid #c8ced3; width: 95%; border-radius: 0.25rem; height: 500px; margin: 41px 49px;">
				<div id="group_bar_graph2" style="width: 100%; height: 100%;"></div>
			</div>
		</div>
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
			
			var data = $("#div_List").jstree(true)._model.data;
			
			tableDataDraw(data); // 검출 현황
			circleGraph(data); // Pie 그래프
			//console.log("data ", data); 
			drawrankGrid(data); // 개인정보 보유 서버 순위 
			
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
// 			tableDataDraw(data); // 검출 현황
// 			circleGraph(data); // Pie 그래프
// 			drawrankGrid(data); // 개인정보 보유 서버 순위
		})
	    .bind('select_node.jstree', function(evt, data, x) {
	    	//console.log("data",data);
	    	
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
				//console.log(id);
				$.ajax({
					type: "POST",
					url: "/SelectTargetDash",
					////async : false,
					data : postData,
					dataType: "json",
				    success: function (resultMap) {
				    	//console.log(resultMap);
				    	//console.log("ddde"); 
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

var ch_target_id = "";
function change_target_id(target_id) {
	ch_target_id = target_id;
}

$(document).ready(function() {
	monthGrpahData(); 
	circleGraph();
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
	        	    	console.log("성공");
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
    
    
    setSelectGraphDate();
    
    });
    
    
    var to = true;
    $('#btn_sch_target').on('click', function(){
    	
    	if($("#fromDate").val() > $("#toDate").val()){
			alert("입력한 끝 날짜가 시작 날짜 보다 빠릅니다.");
			return;
		}

        var v = $('#targetSearch').val();
    	//console.log(v);
    	
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
	

var aut = "manager";

function drawrankGrid(data){
	$("#rankGrid").clearGridData();
	var mydata = [];
	var rowCnt  = 0;
	var targets = [];
	
	$.each(data, function(key, val){
		if(val.parent == null) return true;
		var type = val.data.type ;
		if(type == 0 || type=="#")  return true;
		
		//console.log("type", type)
		
		var item = {
		        SERVER: val.text, 
		        FILE: Number(val.data.PATH_CNT),
		        COUNT: Number(val.data.TOTAL)
		    };
		
		//console.log("item", item)
		var targets_name = val.data.AP_NO+"_"+val.data.TARGET_ID;
		if(!targets.includes(targets_name)){ 
			mydata.push(item);
			targets.push(targets_name);
		}
	});
	

	$("#rankGrid").jqGrid('setGridParam', { data: mydata }).trigger('reloadGrid');
	
	fn_drawrankGrid(mydata);
}


function fn_drawrankGrid(mydata) {
	
	var gridWidth = $("#rankGrid").parent().width();
	
	//console.log(mydata);	
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
	//console.log("tableDataDraw");
	//console.log("data");
	//console.log(data);
	var total = 0; // 검출 파일 수
	var incomplete = 0; // 미조치 파일 수
	var complete = 0; // 조치 파일 수
	var inaccess = 0; // 검출 불가 파일 수
	
	var own = 0; // 보유 등록 파일 수
	var fal = 0; // 오탐 등록 파일 수
	var del = 0; // 삭제 파일 수
	  
	$.each(data, function(key, val){
		//console.log('val');
		//console.log(val);
		
		if(val.parent == null) return true;
	
		var type = val.data.type ;
		
		if(type == 0) return true;
		
		total += (val.data.PATH_CNT==null?0:val.data.PATH_CNT);
		//incomplete += val.data.TRUE_CNT==null?0:val.data.TRUE_CNT; 
		inaccess += val.data.INACCESS_CNT==null?0:val.data.INACCESS_CNT;
		
		own += val.data.TRUE_CNT==null?0:val.data.TRUE_CNT;
		fal += val.data.FALSE_CNT==null?0:val.data.FALSE_CNT;
		//console.log(" total ::" ,total);
		//console.log(" own ::" , own);
		//console.log(" fal ::" , fal);
		//console.log(" inaccess ::" ,inaccess );
// 		del += val.data.; 
	})
	
	complete = (own+fal);
	incomplete = (total - complete);
	
	
	$("#total_tbl").html(total.toLocaleString('ko-KR'));
	$("#incomplete_tbl").html(incomplete.toLocaleString('ko-KR'));
	$("#complete_tbl").html(complete.toLocaleString('ko-KR'));
	$("#inaccess_tbl").html(inaccess.toLocaleString('ko-KR'));
	
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

function monthGrpahData(){
	LGGraph(null)
	LGServerGraph(null)
	LGGraph2(null)
	LGGraph3(null)
}

function LGGraph(data){
	
	var chartDom = document.getElementById('total_bar_graph');
	var myChart = echarts.init(chartDom);
	var option;

	option = {
			 textStyle: {
					fontFamily: 'NotoSansR',
					fontSize: 14
				},
		    title: {
		        text: '월별 전체 검출 현황',
		        left: 'center'
		    },
		    tooltip: {
		        trigger: 'axis',
		        axisPointer: {
		            type: 'shadow'
		        }
		    },
		    legend: {
		        data: ['전월 대비 검출 증감량', '금월 검출 현황'],
		        bottom: 50,  // 범례를 그래프 하단에 배치
		        left: 'center'  // 범례를 가운데 정렬
		    },
		    xAxis: {
		        type: 'category',
		        data: ['2023-10', '2023-11', '2023-12', '2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10'],
		        axisLabel: {
		            rotate: 45
		        }
		    },
		    yAxis: {
		        type: 'value'
		    },
		    series: [
		        {
		            name: '전월 대비 검출 증감량',
		            type: 'bar',
		            data: [43.2, 15.3, 84.3, -45.1, -8.4, 44.3, 68.4, 32, 51.6, 51, -77.71, 0, 64.58],
		            itemStyle: {
		                color: function (params) {
		                    return params.value >= 0 ? '#038FDA' : '#CC0F2E' ;  // 양수는 파란색, 음수는 빨간색
		                }
		            },
		            label: {
		                show: true,
		                position: 'top',
		                formatter: '{c}%'
		            }
		        },
		        {
		            name: '금월 검출 현황',
		            type: 'bar',
		            data: [6515, 7492, 13807, 7578, 6941, 10017, 16869, 22267, 33758, 50975, 11368, 11367, 18708],  // 예시 데이터
		            itemStyle: {
		                color: '#CC0F2E'  // 두 번째 데이터는 회색
		            },
		            label: {
		                show: true,
		                position: 'top',
		                formatter: function (value) {
		                    return value.data.toLocaleString('ko-KR'); // 천 단위 쉼표 적용
		                  }
		            }
		        }
		    ],
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '15%',
		        containLabel: true
		    },
		    dataZoom: [
		        {
		            type: 'slider', // X축 슬라이더 줌
		            start: 0,
		            end: 100,
		            xAxisIndex: 0
		        },
		        {
		            type: 'inside', // X축 내부 스크롤 줌
		            start: 0,
		            end: 100,
		            xAxisIndex: 0
		        },
		    ]
		};


	option && myChart.setOption(option);
}
function LGServerGraph(data){
	
	var chartDom = document.getElementById('server_bar_graph');
	var myChart = echarts.init(chartDom);
	var option;

	option = {
			 textStyle: {
					fontFamily: 'NotoSansR',
					fontSize: 14
				},
		    title: {
		        text: '월별 전체 검출 현황(OS)',
		        left: 'center'
		    },
		    tooltip: {
		        trigger: 'axis',
		        axisPointer: {
		            type: 'shadow'
		        }
		    },
		    legend: {
		        data: ['전월 대비 검출 증감량(서버)', '금월 검출 현황(서버)','전월 대비 검출 증감량(DB)', '금월 검출 현황(DB)'],
		        bottom: 50,  // 범례를 그래프 하단에 배치
		        left: 'center'  // 범례를 가운데 정렬
		    },
		    xAxis: {
		        type: 'category',
		        data: ['2023-10', '2023-11', '2023-12', '2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10'],
		        axisLabel: {
		            rotate: 45
		        }
		    },
		    yAxis: {
		        type: 'value'
		    },
		    series: [
		        {
		            name: '전월 대비 검출 증감량(서버)',
		            type: 'bar',
		            data: [43.2, -30.60, 144.43, -45.1, -20.46, 58.18, 65.05, 34.13, 86.67,53.89, -82.30, -1.01, 82.55],
		            itemStyle: {
		                color: function (params) {
		                    return params.value >= 0 ? '#038FDA' : '#CC0F2E' ;  // 양수는 파란색, 음수는 빨간색
		                }
		            },
		            label: {
		                show: true,
		                position: 'inside',
		                rotate: 90,  // 글자를 90도 회전시킴
		                color: '#000000',  // 글자 색상을 검정색으로 설정
		                formatter: '{c}%'
		            }
		        },
		        { 
		            name: '금월 검출 현황(서버)',
		            type: 'bar',
		            data: [6515, 4521, 11051, 6059, 4819, 7623, 12582, 16876, 31503, 48482, 8579, 8492, 15502],  // 예시 데이터
		            itemStyle: {
		                color: '#CC0F2E'  // 두 번째 데이터는 회색
		            },
		            label: {
		                show: true,
		                position: 'inside',
		                rotate: 90,  // 글자를 90도 회전시킴
		                color: '#000000',  // 글자 색상을 검정색으로 설정
		                formatter: function (value) {
		                    return value.data.toLocaleString('ko-KR'); // 천 단위 쉼표 적용
		                  }
		            }
		        },
		        {
		            name: '전월 대비 검출 증감량(DB)',
		            type: 'bar',
		            data: [0, 100, -7.24, -44.96, 40.34, 12.45, 79.07, 25.75, -58.17, 10.55, 11.87, 3.08, 11.51],
		            itemStyle: {
		                color: function (params) {
		                    return params.value >= 0 ? '#DAD303' : '#0FCC8A' ;  // 양수는 파란색, 음수는 빨간색
		                }
		            },
		            label: {
		                show: true,
		                position: 'inside',
		                rotate: 90,  // 글자를 90도 회전시킴
		                color: '#000000',  // 글자 색상을 검정색으로 설정
		                formatter: '{c}%'
		            }
		        },
		        {
		            name: '금월 검출 현황(DB)',
		            type: 'bar',
		            data: [0, 2971, 2756, 1517, 2129, 2394, 4287, 5391, 2255, 2493, 2789, 2875, 3206],  // 예시 데이터
		            itemStyle: {
		                color: '#0FCC8A'  // 두 번째 데이터는 회색
		            },
		            label: {
		                show: true,
		                position: 'inside',
		                rotate: 90,  // 글자를 90도 회전시킴
		                color: '#000000',  // 글자 색상을 검정색으로 설정
		                formatter: function (value) {
		                    return value.data.toLocaleString('ko-KR'); // 천 단위 쉼표 적용
		                  }
		            }
		        }
		    ],
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '15%',
		        containLabel: true
		    },
		    dataZoom: [
		        {
		            type: 'slider', // X축 슬라이더 줌
		            start: 0,
		            end: 100,
		            xAxisIndex: 0
		        },
		        {
		            type: 'inside', // X축 내부 스크롤 줌
		            start: 0,
		            end: 100,
		            xAxisIndex: 0
		        },
		    ]
		};


	option && myChart.setOption(option);
}
function LGGraph2(data){
	
	var chartDom = document.getElementById('group_bar_graph');
	var myChart = echarts.init(chartDom);
	var option;

	option = {
			 textStyle: {
					fontFamily: 'NotoSansR',
					fontSize: 14
				},
		    title: {
		        text: '그룹별 검출 현황(월)',
		        left: 'center'
		    },
		    tooltip: {
		        trigger: 'axis',
		        axisPointer: {
		            type: 'shadow'
		        }
		    },
		    legend: {
		        data: ['보안운영팀', '신기술검증팀','정보보호팀', '보안사업팀', '클라우드기술팀'],
		        bottom: 50,  // 범례를 그래프 하단에 배치
		        left: 'center'  // 범례를 가운데 정렬
		    },
		    xAxis: {
		        type: 'category',
		        data: ['2023-10', '2023-11', '2023-12', '2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10'],
		        axisLabel: {
		            rotate: 45
		        }
		    },
		    yAxis: {
		        type: 'value'
		    },
		    series: [
		        {
		            name: '보안운영팀',
		            type: 'bar',
		            data: [1300, 1400, 2500, 1500, 1400, 2000, 3200, 5234, 7845, 10200, 2560, 2450, 4320],  // 예시 데이터
		            itemStyle: {
		                color: '#CC0F2E'  // 두 번째 데이터는 회색
		            },
		            label: {
		                show: true,
		                position: 'inside',
		                rotate: 90,  // 글자를 90도 회전시킴
		                color: '#000000',  // 글자 색상을 검정색으로 설정
		                formatter: function (value) {
		                    return value.data.toLocaleString('ko-KR');  // 값에 쉼표 적용
		                  }
		            }
		        },
		        {
		            name: '신기술검증팀',
		            type: 'bar',
		            data: [1500, 1600, 3200, 1800, 1550, 2500, 4000, 4870, 6230, 12650, 1890, 1980, 2850],  // 예시 데이터
		            itemStyle: {
		                color: '#FF7F27'  // 두 번째 데이터는 회색
		            },
		            label: {
		                show: true,
		                position: 'inside',
		                rotate: 90,  // 글자를 90도 회전시킴
		                color: '#000000',  // 글자 색상을 검정색으로 설정
		                formatter: function (value) {
		                    return value.data.toLocaleString('ko-KR');  // 값에 쉼표 적용
		                  }
		            }
		        },
		        {
		            name: '정보보호팀',
		            type: 'bar',
		            data: [1750, 1850, 2700, 1720, 1800, 3500, 4360, 5125, 9850, 3145, 3100, 3750],  // 예시 데이터
		            itemStyle: {
		                color: '#22B14C'  // 두 번째 데이터는 회색
		            },
		            label: {
		                show: true,
		                position: 'inside',
		                rotate: 90,  // 글자를 90도 회전시킴
		                color: '#000000',  // 글자 색상을 검정색으로 설정
		                formatter: function (value) {
		                    return value.data.toLocaleString('ko-KR');  // 값에 쉼표 적용
		                  }
		            }
		        },
		        {
		            name: '보안사업팀',
		            type: 'bar',
		            data: [950, 1100, 1850, 1250, 1150, 2300, 2800, 6215, 8920, 11725, 1750, 1587, 4560],  // 예시 데이터
		            itemStyle: {
		                color: '#00A2E8'  // 두 번째 데이터는 회색
		            },
		            label: {
		                show: true,
		                position: 'inside',
		                rotate: 90,  // 글자를 90도 회전시킴
		                color: '#000000',  // 글자 색상을 검정색으로 설정
		                formatter: function (value) {
		                    return value.data.toLocaleString('ko-KR');  // 값에 쉼표 적용
		                  }
		            }
		        },
		        {
		            name: '클라우드기술팀',
		            type: 'bar',
		            data: [1015, 1542, 3557, 1308, 1141, 1417, 2369, 3588, 6638, 6560, 2023, 1250, 3228],  // 예시 데이터
		            itemStyle: {
		                color: '#3F48CC'  // 두 번째 데이터는 회색
		            },
		            label: {
		                show: true,
		                position: 'inside',
		                rotate: 90,  // 글자를 90도 회전시킴
		                color: '#000000',  // 글자 색상을 검정색으로 설정
		                formatter: function (value) {
		                    return value.data.toLocaleString('ko-KR');  // 값에 쉼표 적용
		                  }
		            }
		        }
		    ],
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '15%',
		        containLabel: true
		    },
		    dataZoom: [
		        {
		            type: 'slider', // X축 슬라이더 줌
		            start: 0,
		            end: 100,
		            xAxisIndex: 0
		        },
		        {
		            type: 'inside', // X축 내부 스크롤 줌
		            start: 0,
		            end: 100,
		            xAxisIndex: 0
		        },
		    ]
		};


	option && myChart.setOption(option);
}
function LGGraph3(data){
	
	var chartDom = document.getElementById('group_bar_graph2');
	var myChart = echarts.init(chartDom);
	var option;

	option = {
			 textStyle: {
					fontFamily: 'NotoSansR',
					fontSize: 14
				},
		    title: {
		        text: '그룹별 검출 현황(그룹)',
		        left: 'center'
		    },
		    tooltip: {
		        trigger: 'axis',
		        axisPointer: {
		            type: 'shadow'
		        }
		    },
		    legend: {
		    	data: ['2023-10', '2023-11', '2023-12', '2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10'],
		    	bottom: 50,  // 범례를 그래프 하단에 배치
		        left: 'center'  // 범례를 가운데 정렬
		    },
		    xAxis: {
		        type: 'category',
		        data: ['보안운영팀', '신기술검증팀','정보보호팀', '보안사업팀', '클라우드기술팀'],
		        axisLabel: {
		            rotate: 45
		        }
		    },
		    yAxis: {
		        type: 'value'
		    },
		    series: [
		        {
		            name: '2023-10',
		            type: 'bar',
		            data: [1300, 1500, 1750, 950, 1015],
		            itemStyle: {
		                color: '#CC0F2E'  // 두 번째 데이터는 회색
		            },
		            label: {
		                show: true,
		                position: 'inside',
		                rotate: 90,  // 글자를 90도 회전시킴
		                color: '#000000',  // 글자 색상을 검정색으로 설정
		                formatter: function (value) {
		                    return value.data.toLocaleString('ko-KR');  // 값에 쉼표 적용
		                  }
		            }
		        },
		        {
		            name: '2023-11',
		            type: 'bar',
		            data: [1400, 1600, 1850, 1100, 1542],
		            itemStyle: {
		                color: '#FF7F27'  // 두 번째 데이터는 회색
		            },
		            label: {
		                show: true,
		                position: 'inside',
		                rotate: 90,  // 글자를 90도 회전시킴
		                color: '#000000',  // 글자 색상을 검정색으로 설정
		                formatter: function (value) {
		                    return value.data.toLocaleString('ko-KR');  // 값에 쉼표 적용
		                  }
		            }
		        },
		        {
		            name: '2023-12',
		            type: 'bar',
		            data: [2500, 3200, 2700, 1850, 3557],
		            itemStyle: {
		                color: '#22B14C'  // 두 번째 데이터는 회색
		            },
		            label: {
		                show: true,
		                position: 'inside',
		                rotate: 90,  // 글자를 90도 회전시킴
		                color: '#000000',  // 글자 색상을 검정색으로 설정
		                formatter: function (value) {
		                    return value.data.toLocaleString('ko-KR');  // 값에 쉼표 적용
		                  }
		            }
		        },
		        {
		            name: '2024-01',
		            type: 'bar',
		            data: [1500, 1800, 1720, 1250, 1308],
		            itemStyle: {
		                color: '#00A2E8'  // 두 번째 데이터는 회색
		            },
		            label: {
		                show: true,
		                position: 'inside',
		                rotate: 90,  // 글자를 90도 회전시킴
		                color: '#000000',  // 글자 색상을 검정색으로 설정
		                formatter: function (value) {
		                    return value.data.toLocaleString('ko-KR');  // 값에 쉼표 적용
		                  }
		            }
		        },
		        {
		            name: '2024-02',
		            type: 'bar',
		            data: [1400, 1550, 1700, 1150, 1141],
		            itemStyle: {
		                color: '#3F48CC'  // 두 번째 데이터는 회색
		            },
		            label: {
		                show: true,
		                position: 'inside',
		                rotate: 90,  // 글자를 90도 회전시킴
		                color: '#000000',  // 글자 색상을 검정색으로 설정
		                formatter: function (value) {
		                    return value.data.toLocaleString('ko-KR');  // 값에 쉼표 적용
		                  }
		            }
		        },
		        {
		            name: '2024-03',
		            type: 'bar',
		            data: [2000, 2500, 1800, 2300, 1417],
		            itemStyle: {
		                color: '#A349A4'  // 두 번째 데이터는 회색
		            },
		            label: {
		                show: true,
		                position: 'inside',
		                rotate: 90,  // 글자를 90도 회전시킴
		                color: '#000000',  // 글자 색상을 검정색으로 설정
		                formatter: function (value) {
		                    return value.data.toLocaleString('ko-KR');  // 값에 쉼표 적용
		                  }
		            }
		        },
		        {
		            name: '2024-04',
		            type: 'bar',
		            data: [3200, 4000, 3500, 2800, 2369],
		            itemStyle: {
		                color: '#6BD089'  // 두 번째 데이터는 회색
		            },
		            label: {
		                show: true,
		                position: 'inside',
		                rotate: 90,  // 글자를 90도 회전시킴
		                color: '#000000',  // 글자 색상을 검정색으로 설정
		                formatter: function (value) {
		                    return value.data.toLocaleString('ko-KR');  // 값에 쉼표 적용
		                  }
		            }
		        },
		        {
		            name: '2024-05',
		            type: 'bar',
		            data: [5234, 4870, 4360, 6215, 3588],
		            itemStyle: {
		                color: '#007AAE'  // 두 번째 데이터는 회색
		            },
		            label: {
		                show: true,
		                position: 'inside',
		                rotate: 90,  // 글자를 90도 회전시킴
		                color: '#000000',  // 글자 색상을 검정색으로 설정
		                formatter: function (value) {
		                    return value.data.toLocaleString('ko-KR');  // 값에 쉼표 적용
		                  }
		            }
		        },
		        {
		            name: '2024-06',
		            type: 'bar',
		            data: [7845, 6230, 5125, 8920, 6638],
		            itemStyle: {
		                color: '#FFB27D'  // 두 번째 데이터는 회색
		            },
		            label: {
		                show: true,
		                position: 'inside',
		                rotate: 90,  // 글자를 90도 회전시킴
		                color: '#000000',  // 글자 색상을 검정색으로 설정
		                formatter: function (value) {
		                    return value.data.toLocaleString('ko-KR');  // 값에 쉼표 적용
		                  }
		            }
		        },
		        {
		            name: '2024-07',
		            type: 'bar',
		            data: [10200, 12650, 9850, 11725, 6560],
		            itemStyle: {
		                color: '#232B99'  // 두 번째 데이터는 회색
		            },
		            label: {
		                show: true,
		                position: 'inside',
		                rotate: 90,  // 글자를 90도 회전시킴
		                color: '#000000',  // 글자 색상을 검정색으로 설정
		                formatter: function (value) {
		                    return value.data.toLocaleString('ko-KR');  // 값에 쉼표 적용
		                  }
		            }
		        },
		        {
		            name: '2024-08',
		            type: 'bar',
		            data: [2560, 1890, 3145, 1750, 2023],
		            itemStyle: {
		                color: '#3F48CC'  // 두 번째 데이터는 회색
		            },
		            label: {
		                show: true,
		                position: 'inside',
		                rotate: 90,  // 글자를 90도 회전시킴
		                color: '#000000',  // 글자 색상을 검정색으로 설정
		                formatter: function (value) {
		                    return value.data.toLocaleString('ko-KR');  // 값에 쉼표 적용
		                  }
		            }
		        },
		        {
		            name: '2024-09',
		            type: 'bar',
		            data: [2450, 1980, 3100, 1587, 1250],
		            itemStyle: {
		                color: '#138535'  // 두 번째 데이터는 회색
		            },
		            label: {
		                show: true,
		                position: 'inside',
		                rotate: 90,  // 글자를 90도 회전시킴
		                color: '#000000',  // 글자 색상을 검정색으로 설정
		                formatter: function (value) {
		                    return value.data.toLocaleString('ko-KR');  // 값에 쉼표 적용
		                  }
		            }
		        },
		        {
		            name: '2024-10',
		            type: 'bar',
		            data: [4320, 2850, 3750, 4560, 3228],
		            itemStyle: {
		                color: '#7A297B'  // 두 번째 데이터는 회색
		            },
		            label: {
		                show: true,
		                position: 'inside',
		                rotate: 90,  // 글자를 90도 회전시킴
		                color: '#000000',  // 글자 색상을 검정색으로 설정
		                formatter: function (value) {
		                    return value.data.toLocaleString('ko-KR');  // 값에 쉼표 적용
		                  }
		            }
		        },
		        
		    ],
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '15%',
		        containLabel: true
		    },
		    dataZoom: [
		        {
		            type: 'slider', // X축 슬라이더 줌
		            start: 0,
		            end: 100,
		            xAxisIndex: 0
		        },
		        {
		            type: 'inside', // X축 내부 스크롤 줌
		            start: 0,
		            end: 100,
		            xAxisIndex: 0
		        },
		    ]
		};


	option && myChart.setOption(option);
}

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
	
	var targetList = [];
	
	$.each(data, function(key, val){
		
		if(val.parent == null) return true;
		//console.log('val.data');
		var targets = val.data.AP_NO+"_"+val.data.TARGET_ID;
		//console.log(val.data.AP_NO+"_"+val.data.TARGET_ID);
	
		var type = val.data.type ;
		if(type == 0)  return true;
		if(!targetList.includes(targets)){
		
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
					console.log(val.data);
					++server;
				}
				++total_server;	
			}else if(val.data.SERVER_TYPE == "DB"){
				if(val.data.SEARCH_STATUS == "completed"){
					++db;
				}
				++total_db
			}  
			targetList.push(targets);
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
	$(".circle_server_complete_cnt").html((server) + "대");
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

function setSelectGraphDate()  
{	
    $("#fromGraphDate").datepicker({
        changeYear : true, 
        changeMonth : true, 
        dateFormat: 'yy-mm'
    });

    $("#toGraphDate").datepicker({
        changeYear : true,
        changeMonth : true,
        dateFormat: 'yy-mm'
    });

    var oToday = new Date();
    $("#toGraphDate").val(getFormatDate2(oToday));
    console.log(getFormatDate2(oToday))
    var oFromDate = new Date(oToday.setDate(oToday.getDate() - 30));
    $("#fromGraphDate").val(getFormatDate2(oFromDate));
    console.log(getFormatDate2(oFromDate)) 
}
function getFormatDate2(oDate)
{
    var nYear = oDate.getFullYear();           // yyyy 
    var nMonth = (1 + oDate.getMonth());       // M 
    nMonth = ('0' + nMonth).slice(-2);         // month 두자리로 저장 
      // day 두자리로 저장

    return nYear + '-' + nMonth;
}
</script>
</body>
</html>