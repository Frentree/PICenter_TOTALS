<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%@ include file="../../include/header.jsp"%>
<style>
section {
	padding: 0 45px 0 45px;
} 

header {
	background: none;
}

h4 {
	margin: 20px 0 0 1px;
	font-size: 16px;
	font-weight: normal;
}
.container_main>div{
	float: left;
	height: 100%
}
#pic_center_dashBoard>div{
	margin-left: 22px;
	margin-top: 31px;
} 
.todo p{   
	color: #fff;
}
#center_bottom>div{ 
	float: left;
	width: 48%;
}
</style>

<!-- section -->
<section id="section" >
	<!-- container -->
	<div class="container_main">
		<div id="pic_left_dashBoard" style="width: 20%;">
<!-- 				검색 스케줄 현황 -->
			<div id="left_top" style="height: 45%;">
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
								<button type="button" id="btn_sch_list" class="btn_sch_target" style="margin: 5px;"></button>
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
			</div>  
<!-- 			자산 현황 -->
			<div id="left_bottom" style="height: 45%;">
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
		</div>
		<div id="pic_center_dashBoard" style="width: 58%;">
			<div id="center_top">
				<div class="todo_box_list todo" style="background: #0c4da2;">
					<p>개인정보 조치<br> (미완료 누적)</p>
					<p id="todo_result_list" style="float: right;">건</p>
				</div>
				<div class="todo_box_approval todo" style="margin: 1px 27px;background: #0c4da2;">
					<p>결재 신청/관리<br> &nbsp;</p>
					<p id="todo_result_approval" style="float: right;">건</p>
				</div>
				<div class="todo_box_schedule todo" style="background: #0c4da2;">
					<p>스케줄 현황<br> &nbsp; </p>
					<p id="todo_result_schedule" style="float: right;">건</p>
				</div>
			</div>
			<div id="center_bottom" style="margin-top: 20px;"> 
<!-- 			pie 그래프 -->  
				<div id="center_left">   
					<h4 id="systemStatus" style="position: relative; display: inline-block; color: #0c4da2; margin-top:0px;  padding-bottom: 5px;">서버 검출현황 요약</h4>
					<div  style='display:inline-block; position:absolute; left: 639px; top: 350px; transform: translateX(-50%) translateY(-50%); text-align:center;'>
						<h4 style="margin: 0;" class="circle_cnt" id="spanid" >총대상수</h4>
					</div>
					<div id="dash_main_pie" style="height: 615px; border: 1px solid #c8ced3; border-radius: 0.25rem;">
						<div id="circleGraph" style="height: 78%; width: 100%; float: left;"></div>   
						<div class="chart_box" style="background: #fff; border: 0 none; padding: 0 15px; height: 22%;"> 
							<p style="height: 42px; margin-left: 13px; font-size: 14px; line-height: 47px; ">
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
					</div>   
				</div> 
<!-- 				grid -->
				<div id="center_right" style="margin-left: 22px;"> 
					<h4 id="serverRank" style="position: relative; display: inline-block; color: #0c4da2; margin-top:0px;  padding-bottom: 5px;">개인정보 보유 서버순위</h4>
					<div id="dash_main_grid" style="height: 642px; ">
						<table id="rankGrid"></table>
					</div> 
				</div>
			</div> 
		</div>
		<div id="pic_right_dashBoard" style="width: 22%;">
			<div id="right_top" class="top_area_right" >
				<h4 style="margin-top: 0px; padding-bottom: 6px; color: #0c4da2; ">공지사항</h4>
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
			<div id="right_bottom">
				<div id="detection_list">
					<h4 style="color: #0c4da2; margin-top: 8px; padding-bottom: 5px;" id="tbl_host_name">검출 현황</h4>
					<div class="left_box_dash_detection" style=" height: 146px;">
						<ul>
							<li class="tagetBox" style=" margin: 5px 5px 0 5px;">
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
				<div id="processing_list">
					<h4 style="color: #0c4da2; display:inline-block; padding-bottom: 5px;" id="complete_name">조치 현황</h4>
					<div class="left_box_dash_complete"style="height: 92px;">
						<ul>
							<li class="tagetBox" style="margin: 5px 5px 0 5px;">
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
</section>
<%@ include file="../../include/footer.jsp"%>

<script type="text/javascript">

$(document).ready(function() {
	
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
	
	$("#fromDate").datepicker({
        changeYear : true,
        changeMonth : true,
        dateFormat: 'yy-mm-dd'
    });

    $("#toDate").datepicker({
        changeYear : true,
        changeMonth : true,
        dateFormat: 'yy-mm-dd'
    });
    setSelectDate();
	
    // 검출현황, 조치현황
	approvalTable();
	detectionTable();
	
	var deptList = ${deptList};
	var userGroupList = ${userGroupList};
	// jstree 
	userGroupNode(userGroupList);
	searchScheduleNode(deptList);
	
	// 바로 가기 건수 표기
	shortcut();
	
	// pie 그래프, grid
	var postData = {fromDate : $("#fromDate").val(), toDate : $("#toDate").val()};
	dashPieDrawData(postData);
	
	
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
	
});

// 바로가기
$(".todo_box_list").click(function() { // 개인정보 조치
	document.location.href = "<%=request.getContextPath()%>/approval/pi_search_list";
});
$(".todo_box_approval").click(function() { // 결재 신청 관리
	document.location.href = "<%=request.getContextPath()%>/approval/pi_search_approval_list";
});
$(".todo_box_schedule").click(function() { // 스케줄 현황
	document.location.href = "<%=request.getContextPath()%>/search/search_list";
});

//검색 스케줄 현황_jstree
function searchScheduleNode(deptList){
	$('#div_List').jstree({
		"core": {
			"animation" : 0,
			"check_callback" : true,
			"themes" : { "stripes" : false },
			"data": deptList
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
		}
	}).bind("loaded.jstree", function(e, data) {
	}).bind('select_node.jstree', function(evt, data, x) {
	});
	
}

// 자산 현황_jstree
function userGroupNode(userGroupList){
	$('#jstree').jstree({ 
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
	}).bind('select_node.jstree', function(evt, data, x) {
    });
}
// 자산현황 검색 
var to = true;
$('#btn_sch_target').on('click', function(){

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


// 상단 바로가기
function shortcut(){
	// 결재 이력 조회
	
	var resultList = 0;
	var resultApproval = 0;
	var resultSchedule = 0;
	
	
	$.ajax({
		type: "POST",
		url: "/dash_dataTodoApproval",
		////async : false,
		data : {},
		dataType: "json",
	    success: function (resultMap) {
	    	if(resultMap.length > 0){ 
	    		for(i=0 ; i < resultMap.length ; i++){
	    			if(resultMap[i].APPROVAL_STATUS == "NOT_APPROVAL")  resultList = resultMap[i].APPROVAL_CNT;
	    			if(resultMap[i].APPROVAL_STATUS == "WAIT_APPROVAL") resultApproval = resultMap[i].APPROVAL_CNT;
	    			if(resultMap[i].APPROVAL_STATUS == "SCHEDULED") resultSchedule = resultMap[i].APPROVAL_CNT;
	    		}
	    	}
	    	
	    	$("#todo_result_list").html(resultList + "건");  
	    	$("#todo_result_approval").html(resultApproval + "건");
	    	$("#todo_result_schedule").html(resultSchedule + "건");
	    },
	    error: function (request, status, error) {
	        console.log("ERROR : ", error);
	    }
	});
}

// 서버 검출현황 요약, 개인정보 보유 서버순위
function dashPieDrawData(dataList){
	
	$.ajax({
		type: "POST",
		url: "/dashPieDraw",
		////async : false,
		data : dataList,
		dataType: "json",
	    success: function (resultMap) {
	    	circleGraph(resultMap); 
	    },
	    error: function (request, status, error) {
	        console.log("ERROR : ", error);
	    }
	});
	
	$.ajax({
		type: "POST",
		url: "/dashGridDrawData",
		////async : false,
		data : dataList,
		dataType: "json",
	    success: function (resultMap) {
	    	fn_drawrankGrid(resultMap); 
	    },
	    error: function (request, status, error) {
	        console.log("ERROR : ", error);
	    }
	});
}
function searchDashPieDrawData(dataList){
	
	$.ajax({
		type: "POST",
		url: "/dashPieDraw",
		////async : false,
		data : dataList,
		dataType: "json",
	    success: function (resultMap) {
	    	circleGraph(resultMap); 
	    },
	    error: function (request, status, error) {
	        console.log("ERROR : ", error);
	    }
	});
	
	$("#rankGrid").setGridParam({
        url: "${getContextPath}/dashGridDrawData", 
        postData: dataList, 
        datatype: "json",
        treedatatype: 'json'
    }).trigger("reloadGrid");
}

function circleGraph(data) {
	var DB_total = 0, server_total = 0, DB_completed = 0, server_completed = 0;

	var statusMap = {
	  error: '오류',
	  scanning: '검색중',
	  scheduled: '미검색',
	  notscanned: '결과없음',
	  stopped: '일시정지',
	  completed: '검출'
	};

	var result = [
	  { value: 0, name: '오류' },
	  { value: 0, name: '검색중' },
	  { value: 0, name: '미검색' },
	  { value: 0, name: '결과없음' },
	  { value: 0, name: '일시정지' },
	  { value: 0, name: '검출' }
	];

	$.each(data, function(key, val) {
	  if (val.platform === "DB") {
	    DB_total += Number(val.total_cnt);
	    if (val.search_status === "completed") DB_completed += Number(val.total_cnt);
	  } else {
	    server_total += Number(val.total_cnt);
	    if (val.search_status === "completed") server_completed += Number(val.total_cnt);
	  }

	  var name = statusMap[val.search_status];
	  if (name) {
	    var target = result.find(function(r) { return r.name === name; });
	    if (target) target.value += Number(val.total_cnt);
	  }
	});
	
	// 총 대상수 표기
	$("#spanid").html("총 대상수<br>"+(DB_total+server_total))
	
	// 서버 프로그래스바
	var server_progress = 0;
	if (server_total != 0 && server_completed != 0){
		server_progress = Math.round((server_completed/server_total)*100);
	}
	$(".circle_server_total_cnt").html((server_total) + "대"); 
	$(".circle_server_complete_cnt").html((server_completed) + "대");
	$(".file_progress").val(server_progress);

	// DB 프로그래스바
	var db_progress = 0;    
	if (DB_total != 0 && DB_completed != 0 ){
		db_progress = Math.round((DB_completed/DB_total)*100);
	}  
	$(".circle_db_total_cnt").html(DB_total + "대");
	$(".circle_db_complete_cnt").html(DB_completed+" 대");
	$(".db_progress").val(db_progress); 
	
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
	            data:result,
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
			{ index: 'target_name', 	name: 'target_name', 		width: 130,	align: 'center'},
			{ index: 'match_locations', name: 'match_locations',	width: 70, 	align: 'right', formatter:'integer', 
				formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, sortable: true, sorttype: 'number' },
			{ index: 'match', 			name: 'match', 				width: 70, 	align: 'right', formatter:'integer', 
				formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, sortable: true, sorttype: 'number' }
		],
		loadonce:true,  
		sortname: 'match', // 기본적으로 FILE 컬럼을 기준으로 정렬
	    sortorder: 'desc', // 내림차순으로 정렬
	   	autowidth: true,
		shrinkToFit: true,
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: gridWidth,
		height: 579,
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


// 검출 현황
function detectionTable(){
	$.ajax({
		type: "POST",
		url: "/detectionTable",
		////async : false,
		data : {},
		dataType: "json",
	    success: function (resultMap) {
	    	var find_path = resultMap.find_path;
	    	var complete_path = resultMap.processing_path;
	    	var incomplete_path = Number(find_path) - Number(complete_path)
	    	$("#total_tbl").html(find_path.toLocaleString('ko-KR'));
	    	$("#incomplete_tbl").html(incomplete_path.toLocaleString('ko-KR'));
	    	$("#complete_tbl").html(complete_path.toLocaleString('ko-KR'));
	    	$("#inaccess_tbl").html(resultMap.inaccess_path.toLocaleString('ko-KR'));
	    },
	    error: function (request, status, error) {
	        console.log("ERROR : ", error);
	    }
	});
}

// 조치 현황
function approvalTable(){
	$.ajax({
		type: "POST",
		url: "/approvalTable",
		////async : false,
		data : {},
		dataType: "json",
	    success: function (resultMap) { 
	    	$("#fal_tbl").html(resultMap.approval_false.toLocaleString('ko-KR'));
	    	$("#own_tbl").html(resultMap.approval_file.toLocaleString('ko-KR'));
	    },
	    error: function (request, status, error) {
	        console.log("ERROR : ", error);
	    }
	});
}

// 검색 버튼
$("#btn_sch_list").click(function() { // 개인정보 조치
	
	var postData = {fromDate : $("#fromDate").val(), toDate : $("#toDate").val()}; 
	$.ajax({
		type: "POST",  
		url: "/dashMainNode",
		////async : false,
		data : postData,
		dataType: "json",
	    success: function (resultMap) {
	    	console.log(resultMap);    
	    	console.log(resultMap.deptList);  
	    	$('#div_List').jstree(true).refresh();  
	    	$('#div_List').jstree(true).settings.core.data = resultMap.deptList;
	    },
	    error: function (request, status, error) {
	        console.log("ERROR : ", error);
	    }
	});
	
	// pie 그래프
	searchDashPieDrawData(postData);
});

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

    var oFromDate = new Date(oToday.setDate(oToday.getDate() - 31));
    $("#fromDate").val(getFormatDate(oFromDate));
};


</script>
</body>
</html>