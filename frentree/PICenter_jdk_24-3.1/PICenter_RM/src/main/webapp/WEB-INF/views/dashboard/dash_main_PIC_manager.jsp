<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%@ include file="../../include/header.jsp"%>
<style>
section {
	padding: 0 45px 0 45px;
} 
header{
	background: none;
}
h4 { 
	margin : 14px 0 0 1px;
	font-size: 16px;
	font-weight: normal;
}
.container_main>div{
	float: left;
	height: 100%
}
#pic_center_dashBoard>div{
	margin-left: 20px;
	margin-top: 31px; 
} 
.todo p{   
	color: #fff;
}
#center_bottom>div{
	width: 100%;
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
</style>
<section id="section">
	<!-- 대시보드 전환 버튼 -->
	<button type="button" class="dashboard-switch-btn" id="btnSwitchDashboard">
		대시보드 변경
	</button>
	<div class="container_main">
		<div id="pic_left_dashBoard" style="width: 20%;">
			<div id="left_bottom" style="height: 100%;">  
				<h4 style="position: relative; color: #0c4da2; margin-top: 22px;">자산 현황</h4>
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
				<div class="left_box" style="height: 694px; width: 351px;">
					<div id="jstree" class="left_box"
						style="height: 100%; width: 339px; padding: 0; border: none;"></div>
				</div> 
			</div>
		</div> 
		<div id="pic_center_dashBoard" style="width: 27%;">    
			<div id="center_bottom" style="margin-top: 20px;"> 
<!-- 			pie 그래프 -->   
				<div id="center_left" style="height: 40%;"> 
					<h4 id="systemStatus" style="position: relative; display: inline-block; color: #0c4da2; margin-top:0px;  padding-bottom: 5px;">서버 검출현황 요약</h4>
					<div  style='display:inline-block; position:absolute; left: 629px; top: 202px; transform: translateX(-50%) translateY(-50%); text-align:center;'>
						<h4 style="margin: 0;" class="circle_cnt" id="spanid" >총대상수</h4>
					</div>
					<div id="dash_main_pie" style="height: 341px; border: 1px solid #c8ced3; border-radius: 0.25rem;">
						<div id="circleGraph" style="height: 100%; width: 100%; float: left;"></div>
					</div>    
				</div>  
<!-- 				grid --> 
				<div id="center_right" style=" margin-top: 20px; height: 40%;"> 
					<h4 id="serverRank" style="position: relative; display: inline-block; color: #0c4da2; margin-top:0px;  padding-bottom: 5px;">개인정보 보유 서버순위</h4>
					<div id="dash_main_grid" style="height: 100%; ">
						<table id="rankGrid"></table>
					</div> 
				</div>
			</div>
		</div>   
		<div id="pic_right_dashBoard" style="width: 51%; margin-left: 20px; "> 
			<div id="right_top" class="top_area_right" style="width: 100%; ">
				<div style="width: 392px; float: right;"> 
					<h4 style="margin-top: 0px; padding-bottom: 6px; color: #0c4da2; ">공지사항</h4>
					<div class="location_notice_area" style="right: 12px;">
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
			<div id="right_bottom" style="width: 100%; margin-top: 20px;" > 
				<h4 style="color: #0c4da2; margin-top: 16px; margin-bottom: 5px;">검출 현황</h4>  
				<div class="chart_box" style="width: 100%; height: 607px; background: #fff;">
					<div class="date" style="float: right;">
						<select class="" id="days">
							<option value="7_day_1">7일 전</option>
					        <option value="1_month_1">1개월 전</option>
					        <option value="3_month_1">3개월 전</option>
					        <option value="6_month_2">6개월 전</option>
					        <option value="1_year_2" selected>1년 전</option>
				       </select>
				       <input type="hidden" id="tree_id" value="">   
				  	</div>  
				  	<div id="main_graph2" style="margin-top: 30px; height: 90%; width: 100%;"></div>
<!-- 		      		<div id="main_graph" style="margin-top: 30px; height: 553px; width: 900px;"></div> -->
				</div>
			</div>     
		</div> 
	</div>
</section>
	<%@ include file="../../include/footer.jsp"%>
<script type="text/javascript">

var pattern = '${patternList}'.split('[{').join('').split('}]').join('');
pattern = pattern.split('}, {');
var barDataList = null; 
$(document).ready(function() {
	var userGroupList = ${userGroupList};
	// jstree 
	userGroupNode(userGroupList);
	var postData = {fromDate : null, toDate : null};
	dashPieDrawData(postData);
	
});

//자산 현황_jstree
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
		var dataArray = Object.values(data.instance._model.data); // 객체를 배열로 변환

		  var result = [];
		  dataArray.forEach(function(item) {
		    if (item.data && item.data.type === 1) {
		      result.push({
		        target_id: item.data.target_id,
		        ap: item.data.ap
		      });
		    }
		  });
		   
		  barDataList = result; 
		  dashBarDrawData(result); 
		
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

function circleGraph(data) {
	var DB_total = 0, server_total = 0, DB_completed = 0, server_completed = 0;

	var statusMap = {
	  notscanned: '미검색',
	  completed: '검출',
	  scheduled: '검색중',
	  stopped: '일시정지'
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
	            radius : ['30%', '55%'],
	            color : ['#CC0F2E', '#68A62F', '#11088D', '#038FDA', '#6F037F', '#FBCD1F'],
	            center: ['50%', '43%'],
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
		height: 305,   
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

$("#days").on("change", function(){
	dashBarDrawData(barDataList);  
	return;
});

function dashBarDrawData(dataArray){
	var postData = {
			target_list : dataArray,
			days : $("#days").val()
		};
	
	$.ajax({
		type: "POST",
		url: "/dashBarDrawData",
		contentType: "application/json; charset=utf-8", 
		async : true,
		data : JSON.stringify(postData),
		dataType: "json",
	    success: function (resultMap) {
	    	resizeGraph_One(resultMap);
	    },
	    error: function (request, status, error) {
	        console.log("ERROR : ", error);
	    }
	});
}

function resizeGraph_One(resultMap){
	var days = [];
	var names = [];
	var colors = [];
	var series = [];
	for(var i = 0; resultMap.length > i; i++){
		days.push(resultMap[i].date_key);
	}
	
	for(var i = 0; pattern.length > i; i++){ // str 배열만큼 for돌림
		var row = pattern[i].split(', ');
		var name = row[0].split('PATTERN_NAME=').join(''); 
		var id = row[1].split('ID=').join(''); 
		var color = row[2].split('COLOR_CODE=').join('');
		var types = [];
		
		types = resultMap.map(function(item) {
	        return item.data_types[name] || 0; // 해당 name이 없으면 0 반환
	    });
		 
		names.push(name);
		colors.push(color); 
		series.push({
			name : name,
			type : 'bar', 
			color: color,
			data: types
		});
	} 
	
	var echartPie = echarts.init(document.getElementById('main_graph2'));
	echartPie.setOption({
		// 제목
		title : {
			text : '개인정보 유형',
			subtext : '리스트',
			textStyle : {
				fontFamily: 'NotoSansR',
				fontSize : '13px',
				color : '#5d4fff'
			},
			show : false
		},
		tooltip : {
			trigger : 'axis',
			axisPointer : {
				type : 'shadow'
			}
		},
		grid: {
	        left: '6%',
	        containLabel: true
	    },
		// 상단 옵션 데이터 종류
		legend : {
			data : names,
			color : colors,
		},
		// 가로
		xAxis : [ {
			type : 'category',
			data : days
		} ],
		// 세로
		yAxis : [ {
			type : 'value',
			axisLable : {
				textStyle : {
					fontSize : 8
				}
			}
			
		} ], 
		series : series, 
		// 상단 차트종류 옵션 버튼
		toolbox : {
			show : true,
			bottom: 15,
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
	                    		 +'</div>'
	                			 + '<table id="chartdata" style="width:100%;text-align:center"><tbody><tr>'
	                             + '<td>날짜</td>'
	                             + '<td>' + series[0].name + '</td>'
	                             + '<td>' + series[1].name + '</td>'
	                             + '<td>' + series[2].name + '</td>'
	                             + '<td>' + series[3].name + '</td>'
	                             + '<td>' + series[4].name + '</td>'
	                             + '<td>' + series[5].name + '</td>'
	                             + '<td>' + series[6].name + '</td>'
	                             + '<td>' + series[7].name + '</td>'
	                             + '</tr>';
	                for (var i = 0, l = axisData.length; i < l; i++) {
	                    table += '<tr>'
	                             + '<td>' + axisData[i] + '</td>'
	                             + '<td>' + series[0].data[i] + '</td>'
	                             + '<td>' + series[1].data[i] + '</td>'
	                             + '<td>' + series[2].data[i] + '</td>'
	                             + '<td>' + series[3].data[i] + '</td>'
	                             + '<td>' + series[4].data[i] + '</td>'
	                             + '<td>' + series[5].data[i] + '</td>'
	                             + '<td>' + series[6].data[i] + '</td>'
	                             + '<td>' + series[7].data[i] + '</td>'
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
	});
}
		

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

// 대시보드 전환 버튼 - 새 대시보드로 이동
$("#btnSwitchDashboard").click(function() {
	location.href = "<%=request.getContextPath()%>/picenter_target";
});

</script>
</body>
</html>