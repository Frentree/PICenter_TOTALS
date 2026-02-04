<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%@ include file="../../include/header.jsp"%>
<style>
section {
	padding: 0 65px 0 45px;
}

header{
	background: none;
}
h4 {
	margin : 14px 0 0 1px;
	font-size: 16px;
	font-weight: normal;
}
.circle_server_total_cnt, .circle_server_complete_cnt, .circle_db_total_cnt, .circle_db_complete_cnt{
	font-size: 16px !important;
	padding: 0 !important;
}
.ui-widget.ui-widget-content{
	border: none;
}
.ui-jqgrid .ui-jqgrid-hdiv{
	border: 1px solid #c8ced3;
}
#main_graph canvas[data-zr-dom-id="zr_0"]{
	max-width: 990px !important;
	min-width: 990px !important;
	width: 990px !important;
	padding-top: 7px !important;
	padding-right: 15px !important;
}
#circleGraph canvas[data-zr-dom-id="zr_0"]{
	height: 265px !important;
	cursor: default;
}
</style>

		<!-- section -->
		<section id="section">
			<!-- container -->
			<div class="container_main">
				<div>
					<div class="top_area_left" style="margin-left: 0;">
							<h4 style="margin-top:30px; color: #0c4da2;">공지사항</h4>
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
				<div id="pic_main_dashboard"> 
					<div id="left-div" style="float: left;">   
							<h4 style="position: relative; color: #0c4da2; margin-top: 22px; margin-bottom: 5px;">자산 현황</h4>
						<div class="left_box" style="height:605px; width: 342px; margin-right: 10px;">
							<div id="jstree" class="left_box" style="height:593px; width: 329px; padding: 0; border: none;">
							</div> 
						</div>
					</div>
					<div id="meddie-div" style="float: left;">
						<div class="chart_area">
					<div class="chart_left">
						<h4 style="position: relative; color: #0c4da2; margin-top: 18px; margin-bottom: 5px;">검출현황 요약</h4>
						<div class="left_box" style="width: 413px; margin-right: 5px;">
							<ul>  
								<li style="width:380px; padding: 0;" > 
									<div class="chart_box" style="height: 265px; padding: 0; border: none; background: #fff;">
										<div id=circleGraph style="height: 100%; width: 405px; float: left;"></div>
										<script type="text/javascript"> 
										<!-- 원그래프  -->
										function circleGraph(data, status, ansyn) {
											
											var total_cnt = 0;
											var error_cnt = 0;
											var searched_cnt = 0;
											var wait_cnt = 0;
											var complete_cnt = 0;
											var scancomp_cnt = 0;
											var pause_cnt = 0;
											
											var total_server = 0;
											var server = 0;
											var total_db = 0;
											var db = 0;
											
											$.each(data, function(key, val){
												switch (val.SEARCH_STATUS) {
												  case "error": 	 ++error_cnt; break;
												  case "scanning": 	 ++searched_cnt; break;
												  case "notscanned": ++wait_cnt; break;
												  case "scancomp":   ++scancomp_cnt; break;
												  case "paused":  	 ++pause_cnt; break;
												  case "completed":   ++complete_cnt; break;
												  default: console.log(val.SEARCH_STATUS);
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
											        padding: [0, 75, 0, 75],
											        bottom: 3
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
											            center: ['50%', '43%'],
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
											
											var totalDiv;
											totalDiv = "<div id='spanid' style='display:inline-block; position:absolute; left: 202px; top: 117px; transform: translateX(-50%) translateY(-50%); text-align:center;'>";
											totalDiv += '<h4 style="margin: 0; font-size: 14px;" class="circle_cnt" >총 대상수 <br> ' + total_cnt + '</h4></div>';
											
											if ($("#circleGraph").find("#spanid").length > 0) {
											    $("#spanid").remove();
											}
											
											$("#circleGraph").append(totalDiv);
										}
										</script>
									</div>
								</li>
							</ul>
						</div>
						<h4 id="managerRank" style="color: #0c4da2; display:inline-block; margin-top: 23px; padding-bottom: 5px;">개인정보 보유 순위</h4>
						<div class="left_box_dash_rank"
							style="width: 413px; height: 276px; padding: 10px 0;">
							<ul>
								<li class="tagetBox"
									style="width: 400px; margin-left: 10px;">
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
					<div class="clear_l"></div>
				</div> 
					</div>
					<div id="right-div" style="float: left; width: 1005px;">
						<div class="chart_center" style="margin-left: 5px;">
						<h4 style="color: #0c4da2; margin-top: 16px; margin-bottom: 5px;">검출 현황</h4>
						<div class="chart_box" style="width: 992px; height: 607px; background: #fff;">
						  <div class="date" style="float: right;">
				   		   <select class="" id="days">
					        <option value="days">7일 전</option>
					        <option value="month">1개월 전</option>
					        <option value="three_month">3개월 전</option>
					        <option value="six_month">6개월 전</option>
					        <option value="year" selected>1년 전</option>
					       </select>
					       <input type="hidden" id="tree_id" value="">
						  </div>
					      <div id="main_graph" style="margin-top: 30px; height: 565px; width: 990px;"></div>
							<script type="text/javascript">
							<!-- 첫번째 개인정보 유형 그래프 -->
							function resizeGraph_One(result, status, ansyn) {
								
								var days = [];
								var names = [];
								var colors = [];
								var series = [];
								var pattern = result.DataTypes;
								
								for(var i = 0; i < result.Data.length; i++){
									days.push(result.Data[i].DAYS);
								} 
								
								for(var i = 0; i < pattern.length; i++){
									var name = pattern[i].PATTERN_NAME;
									var id = pattern[i].ID;
									var color = pattern[i].COLOR_CODE;// 0~ffffff 사이의 랜덤 색 만들기 결과는 10진수 실수
									var types = [];
									
									for(var u = 0; u < result.Data.length; u++){
										var type = result.Data[u];
										types.push(type[id]);
									}
									
									names.push(name);
									colors.push(color); 
									series.push({
										name : name,
										type : 'bar', 
										color: color,
										data : types 
									});
								}
								
								var echartPie = echarts.init(document.getElementById('main_graph'));
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
				                                        		 +'<div class="sch_area"><button type="button" name="button" class="btn_down" style="margin:4px;" onclick="btnDownload();">다운로드</button></div></div>'
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
						</script>
					    </div>
					</div>
					</div>
					 
				</div>
				<!-- chart area -->
				
			<div class="clear"></div>
		</div>
		</section>
	<%@ include file="../../include/footer.jsp"%>
<script type="text/javascript">
var id = null;
$(function() {
		$('#jstree').jstree({
			// List of active plugins
			"core" : {
			    "animation" : 0,
			    "check_callback" : true,
				"themes" : { "stripes" : false },
				"data" : ${userGroupList},
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
		})
		.bind('loaded.jstree', function(event, data) {
			var target_id = data.instance._model.data;
			var targetList = [];
			for(i in target_id){
				if(target_id[i].parent != null){
					targetList.push(target_id[i].id);
				}
			};
			fn_drawrankGrid(data);
		})
	    .bind('select_node.jstree', function(evt, data, x) {
	    	console.log(data);
	    	id = data.node.data.target_id;
	    	var connected = data.node.original.connected;
	    	
	    	var type = data.node.data.type;
	    	var ap = data.node.data.ap;
	    	var postData = {
	    			target_id: id,
                	days : $("#days").val()
            };
	    	if(type == 1){
	    		pie_graph_draw(id);
	    	};
	    });
});

$(document).ready(function() {
	
	if("${noticePop}" != "" && "${noticePop}" != null){
		
		var notice_id = "${noticePop.NOTICE_ID}";
		
		var result = getCookie('notice_popup');
		if (result == "end") {
		} else {
			var winRef;	
			var notice_url = "${getContextPath}/popup/noticePop";
			var winWidth = 1260;
	    	var winHeight = 750; 
			var popupOption= "width="+winWidth+", height="+winHeight + ", scrollbars=no, resizable=no"; 	
			window.open(notice_url+ "?id=" + notice_id,notice_id,popupOption);
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
	    user_grade : '${memberInfo.USER_GRADE}',
	    insa_code: '${memberInfo.INSA_CODE}',
     	fromDate : $("#fromDate").val(),
     	toDate : $("#toDate").val()
    }
	
	/* $.ajax({
		type: "POST",
		url: "/group/dashListDept",
		////async : false,
		data: postData,
	    success: function (result) {
	    	if(result.resultCode == -1){
	    		return;
	    	}
 			tableDataDraw(result.data); // 검출 현황
			circleGraph(result.data); // Pie 그래프
			drawrankGrid(result.data); // 개인정보 보유 서버 순위
	    	
	    },
	    error: function (request, status, error) {
	        console.log("ERROR : ", error)
	    }
	}); */
	
	$("#days").on("change", function(){
		pie_graph_draw(id);
		return;
	});
	
	pie_graph_draw(id);
	
 	$.ajax({
 		type: "POST",
 		url: "/pi_systemcurrent_manager",
 		////async : false,
 		data: postData,
 		dataType : "json",
 		success: circleGraph,
	    error: function (request, status, error) {
	        console.log("ERROR : ", error);
	    }
 	});
	
});

var aut = "manager"

function getFormatDate(oDate)
{
    var nYear = oDate.getFullYear();           // yyyy 
    var nMonth = (1 + oDate.getMonth());       // M 
    nMonth = ('0' + nMonth).slice(-2);         // month 두자리로 저장 

    var nDay = oDate.getDate();                // d 
    nDay = ('0' + nDay).slice(-2);             // day 두자리로 저장

    return nYear + '-' + nMonth + '-' + nDay;
}
function setSelectDate() 
{
	
    var oToday = new Date();
    $("#toDate").val(getFormatDate(oToday));

    var oFromDate = new Date(oToday.setDate(oToday.getDate() - 30));
    $("#fromDate").val(getFormatDate(oFromDate));
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


/* function drawrankGrid(data){
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
} */

function fn_drawrankGrid(data) {
	var gridWidth = $("#rankGrid").parent().width();
	
	var postData = {
			user_no : '${memberInfo.USER_NO}'
	};

	$("#rankGrid").jqGrid({
		url: "<%=request.getContextPath()%>/dash_personal_manager_rank",
		datatype: "json",
		postData: postData,
	   	mtype : "POST",
		colNames:['서버','검출파일수(컬럼수)','검출건수'],
		colModel: [
			{ index: 'SERVER', 	name: 'SERVER', 	width: 100, align: 'center'},
			{ index: 'FILE', 	name: 'FILE',  width: 100, align: 'right', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, sortable: true, sorttype: 'number' },
			{ index: 'COUNT', 	name: 'COUNT', width: 100, align: 'right', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, sortable: true, sorttype: 'number' }
		],
		loadonce:true,
	   	autowidth: true,
		shrinkToFit: true,
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: gridWidth,
		height: 217,
		rownumbers : false, // 행번호 표시여부
		rownumWidth : 35, // 행번호 열의 너비	
		rowNum:20,
		rowList:[25,50,100],
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
}

function pie_graph_draw(id){
	var postData = {
		days : $("#days").val(),
		target_id : id,
		user_no : '${memberInfo.USER_NO}'
	};
	
	$.ajax({
		type: "POST",
		url: "/pi_datatype_manager",
		async : false,
		data : postData,
		dataType : "json",
	    success: resizeGraph_One,
	    error: function (request, status, error) {
			alert("fail");
	        console.log("ERROR : ", error);
	    }
	});
}

</script>
</body>
</html>