<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ include file="../../include/header_rm.jsp"%>
<style>
section {
	padding: 0 65px 0 45px;
}

header {
	background: none;
}

h4 {
	margin: 14px 0 0 1px;
	font-size: 16px;
	font-weight: normal;
}

.circle_server_total_cnt, .circle_server_complete_cnt,
	.circle_db_total_cnt, .circle_db_complete_cnt {
	font-size: 16px !important;
	padding: 0 !important;
}

.ui-widget.ui-widget-content {
	border: none;
}

#main_graph canvas[data-zr-dom-id="zr_0"] {
	max-width: 990px !important;
	min-width: 990px !important;
	width: 990px !important;
	padding-top: 7px !important;
	padding-right: 15px !important;
}

#circleGraph canvas[data-zr-dom-id="zr_0"] {
	height: 265px !important;
	cursor: default;
}
</style>

<!-- section -->
<section id="section">
	<!-- container -->
	<div class="container_main">
		<!-- left list-->
		<c:set var="serverCnt" value="${fn:length(aList)}" />
		<div class="left_area">
			<h4
				style="position: relative; color: #FF7F00; margin-top: 19px; margin-bottom: 5px;">대상
				현황</h4>
			<table class="user_info narrowTable" style="width: 351px;">
				<tbody>
					<tr>
						<th style="text-align: center; border-radius: 0.25rem;">대상조회</th>
						<td><input type="text"
							style="width: 221px; padding-left: 5px;" size="10"
							id="targetSearch" placeholder="호스트명을 입력하세요."></td>
						<td><input type="button" name="button"
							class="btn_look_approval" id="btn_sch_target"
							style="margin-top: 5px;"></td>
					</tr>
				</tbody>
			</table>
			<div class="left_box"
				style="margin-top: 11px; height: 655px; width: 351px;">
				<div id="jstree" class="left_box"
					style="height: 643px; width: 329px; padding: 0; border: none;">
				</div>
			</div>
		</div>

		<!-- chart area -->
		<div class="chart_area">
			<div class="chart_left">
				<h4
					style="position: relative; color: #FF7F00; margin-top: 35px; margin-bottom: 5px;">검출
					현황 요약</h4>
				<div class="left_box" style="width: 413px; margin-right: 5px;">
					<ul>
						<li style="width: 380px; padding: 0;">
							<div class="chart_box"
								style="height: 265px; padding: 0; border: none; background: #fff;">
								<div id=circleGraph
									style="height: 100%; width: 405px; float: left;"></div>
								<script type="text/javascript"> 
										<!-- 원그래프  -->
										function circleGraph(result, status, ansyn) {
											
											var total = [];
											var error = [];
											var searched = [];
											var wait = [];
											var complete = [];
											var scancomp = [];
											var pause = [];
											
											$.each(result, function(key, value){
											    $.each(value, function(key, value){
											        if(key == "TOTAL") total.push(value);
											        if(key == "ERROR") error.push(value);
											        if(key == "SEARCHED") searched.push(value);
											        if(key == "WAIT") wait.push(value);
											        if(key == "COMPLETE") complete.push(value);
											        if(key == "SCANCOMP") scancomp.push(value);
											        if(key == "PAUSE") pause.push(value);
											        
											    });
											}); 
										
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
													         {value:error, name:'오류'},
													         {value:searched, name:'검색중'},
													         {value:wait, name:'미검색'},
													         {value:complete, name:'결과없음'},
													         {value:pause, name:'일시정지'},
													         {value:scancomp, name:'검출'}
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
											$(".circle_server_total_cnt").html(result[0].TOTAL + "대");
											$(".circle_server_complete_cnt").html(result[0].PROGRESS_COMPLETE + "대");
											$(".circle_server_percent").html(result[0].PERCENT);
											$(".file_progress").val(result[0].VALUE);
											var totalDiv;
											totalDiv = "<div id='spanid' style='display:inline-block; position:absolute; left: 202px; top: 117px; transform: translateX(-50%) translateY(-50%); text-align:center;'>";
											totalDiv += '<h4 style="margin: 0; font-size: 14px;" class="circle_cnt" >총 대상수 <br> ' + total + '</h4></div>';
											
											$("#circleGraph").append(totalDiv);
										}
										</script>
							</div>
						</li>
					</ul>
				</div>
				<h4 id="managerRank"
					style="color: #FF7A00; display: inline-block; margin-top: 23px; padding-bottom: 5px;">개인정보 보유 순위</h4>
				<div class="left_box_dash_rank"
					style="width: 413px; height: 376px; padding: 10px 0;">
					<ul>
						<li class="tagetBox" style="width: 400px; margin-left: 10px;">
							<div class="grid_top" style="float: right; width: 100%;">
								<div class="left_box2"
									style="overflow: hidden; max-height: 575px; width: 100%;">
									<div id="rankGrid" class="ag-theme-balham"
										style="height: 360px; width: 100%;"></div>
								</div>
							</div>
						</li>
					</ul>
				</div>
				<div id="excelData" style="display: none;"></div>
			</div>
			<div class="chart_center" style="margin-left: 5px;">
				<h4 id="rm_title"
					style="color: #FF7F00; margin-top: 35px; margin-bottom: 5px;">검출
					현황 - 전체</h4>
				<div class="chart_box"
					style="width: 992px; height: 705px; background: #fff;">
					<div class="date" style="float: right;">
						<select class="" id="days">
							<option value="days">7일 전</option>
							<option value="month">1개월 전</option>
							<option value="three_month">3개월 전</option>
							<option value="six_month">6개월 전</option>
							<option value="year" selected>1년 전</option>
						</select> <input type="hidden" id="tree_id" value="">
					</div>
					<div id="main_graph"
						style="margin-top: 30px; height: 665px; width: 990px;"></div>
					<script type="text/javascript">
							<!-- 첫번째 개인정보 유형 그래프 -->
							function resizeGraph_One(result, status, ansyn) {
								var days = [];
								var names = [];
								var colors = [];
								var series = [];
								
								for(var i = 0; i < result.Data.length; i++){
									days.push(result.Data[i].DAYS);
								} 
								
								for(var i = 0; i < result.DataTypes.length; i++){
									var name = result.DataTypes[i].PATTERN_NAME;
									var id = result.DataTypes[i].ID;
									var color = result.DataTypes[i].COLOR_CODE;
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
									legend : {
										data : names,
										color : colors,
									},
									xAxis : [ {
										type : 'category',
										data : days
									} ],
									yAxis : [ {
										type : 'value',
										axisLable : {
											textStyle : {
												fontSize : 8
											}
										}
									} ],
									series : series,
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
				                                    console.log(series.length);
				                                    var table = '<div class="list_sch" style="top: 0px">' 
				                                        		 +'<div class="sch_area"><button type="button" name="button" class="btn_down" style="margin:4px;" onclick="btnDownload();">다운로드</button></div></div>'
				                                    			 + '<table id="chartdata" style="width:100%;text-align:center"><tbody><tr>'
				                                                 + '<td>날짜</td>';
	                                                 for(var i = 0; i < series.length; i++){  
	                                                	 table += '<td>' + series[i].name + '</td>';
	                                                 }          
	                                                 table += '</tr>';
				                                    for (var i = 0, l = axisData.length; i < l; i++) {
				                                        table += '<tr>'
			                                                 + '<td>' + axisData[i] + '</td>';
			                                             for(var u = 0; u < series.length; u++){
		                                                	 table += '<td>' + series[u].data[i] + '</td>';
		                                                 }         
				                                                 table += '</tr>';
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
			<div class="clear_l"></div>
		</div>
		<div class="clear"></div>
	</div>
</section>
<div id="pcDetailPopup" class="popup_layer" style="display: none">
	<div class="popup_box"
		style="height: 200px; width: 850px; padding: 10px; background: #f9f9f9; left: 43%;">
		<img class="CancleImg" id="btnCanclePCDetailPopup"
			src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 style="color: #222; padding: 0; box-shadow: none;">상세 조회</h1>
		</div>
		<div class="popup_content">
			<div class="content-box" id="div_update_user"
				style="height: auto; background: #fff; border: 1px solid #c8ced3;">
				<table class="popup_tbl">
					<colgroup>
						<col width="20%">
						<col width="25%">
						<col width="15%">
						<col width="20%">
						<col width="20%">
					</colgroup>
					<tbody id="popup_details">
					</tbody>
				</table>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area" style="padding: 10px 0; margin: 0;">
				<button type="button" id="btnClose">닫기</button>
			</div>
		</div>
	</div>
</div>
<%@ include file="../../include/footer.jsp"%>
<script type="text/javascript">

// AG Grid 전역변수
var rankGridApi = null;

// 숫자 렌더러 (천단위 콤마)
function numberRenderer(params) {
    var value = params.value;
    if (value == null || value === '') return '0';
    
    return Number(value).toLocaleString();
}

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
			'search': {
		        'case_insensitive': false,
		        'show_only_matches' : true,
		        "show_only_matches_children" : true
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
	    	var id = data.node.id;
	    	var connected = data.node.original.connected;
	    	var type = data.node.data.type;
	    	var ap = data.node.data.ap;
	    	var text = data.node.text;
	    	
	    	var postData = {
    			target_id: id,
               	days : $("#days").val()
            }
	    	
	    	if(id == "target") {
	    		$("#rm_title").html("검출 현황 - 전체")
	    		$.ajax({
		    		type: "POST",
		    		url: "/pi_datatype", 
		    		async : false,
		    		data : postData,
		    		dataType : "json",
		    	    success: resizeGraph_One,
		    	    error: function (request, status, error) {
		    	        console.log("ERROR : ", error);
		    	    } 
		    	});
	    	}
	    	
	    	if(type == 1){
	    		var postData = {
        			target_id: id,
                   	days : $("#days").val(),
                   	ap_no : ap
                }
	    		
	    		$("#rm_title").html("검출 현황 - " + text);
	    		$.ajax({
	    			type: "POST",
	    			url: "/pi_datatype_manager",
	    			async : false,
	    			data : postData,
	    			dataType : "json",
	    		    success: resizeGraph_One,
	    		    error: function (request, status, error) {
	    		        console.log("ERROR : ", error);
	    		    }
	    		});
	    	}
	    });
});

function btnDownload(){
	exportTableToCsv("chartdata", "data");
}

function exportTableToCsv(tableId, filename) {
    if (filename == null || typeof filename == undefined)
        filename = tableId;
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
}

var ch_target_id = "";
function change_target_id(target_id) {
	ch_target_id = target_id;
}

$(document).ready(function() {
	setSelectDate();
	var postData = {
       	user_no : '${memberInfo.USER_NO}',
     	fromDate : $("#fromDate").val(),
     	toDate : $("#toDate").val()
    }
	$.ajax({
		type: "POST",
		url: "/pi_systemcurrent_manager",
		data: postData,
		dataType : "json",
	    success: circleGraph,
	    error: function (request, status, error) {
	        console.log("ERROR : ", error);
	    }
	});

	$("#days").on("change", function(){
		var idx = $('#jstree').jstree('get_selected');
		
		if(idx.length > 0) {
			var node = $('#jstree').jstree(true).get_node(idx);
			var id = node.id;
			var type = node.data.type;
			var ap = node.data.ap;
			
			if(type == 0) {
				var postData = {
					days : $("#days").val(),
					target_id : id,
					user_no : '${memberInfo.USER_NO}'
				};
				
				$.ajax({
					type: "POST",
					url: "/pi_datatype",
					async : false,
					data : postData,
					dataType : "json",
				    success: resizeGraph_One,
				    error: function (request, status, error) {
				        console.log("ERROR : ", error);
				    }
				});
			}else {
				var postData = {
					days : $("#days").val(),
					target_id : id,
					ap_no : ap,
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
				        console.log("ERROR : ", error);
				    }
				});
			}
		} else {
			var postData = {
				days : $("#days").val(),
				target_id : id,
				user_no : '${memberInfo.USER_NO}'
			};
			
			$.ajax({
				type: "POST",
				url: "/pi_datatype",
				async : false,
				data : postData,
				dataType : "json",
			    success: resizeGraph_One,
			    error: function (request, status, error) {
			        console.log("ERROR : ", error);
			    }
			});
		}
	});
	
	var postData = {
		days : $("#days").val(),
		user_no : '${memberInfo.USER_NO}'
	};
	
	$.ajax({
		type: "POST",
		url: "/pi_datatype",
		async : false,
		data : postData,
		dataType : "json",
	    success: resizeGraph_One,
	    error: function (request, status, error) {
	        console.log("ERROR : ", error);
	    }
	});
	
    var to = true;
    $('#btn_sch_target').on('click', function(){

    	if($("#fromDate").val() > $("#toDate").val()){
			alert("입력한 끝 날짜가 시작 날짜 보다 빠릅니다.");
			return;
		}
    	
        var v = $('#targetSearch').val();
    	
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

    $("#btnClose").click(function() {
    	$("#pcDetailPopup").hide();
    });
	
    $("#btnCanclePCDetailPopup").click(function() {
    	$("#pcDetailPopup").hide();
    });
});
   
function datatype(result, type) {
   	if(result.length != 0){
   		for(var i = 0; i < result.length; i++){
   			var addRow = "";
   			if(result[i].IP != null){
   				addRow += "<tr><td>" + result[i].NAME + "<br>(" + result[i].IP + ")</td>"
   			} else {
   				addRow += "<tr><td>" + result[i].NAME + "<br>(연결안됨)</td>"
   			}
   			
   			var regexp = /\B(?=(\d{3})+(?!\d))/g;
   			var type1 = String(result[i].TYPE1).replace(regexp, ',');
   			
   			addRow += '<td style="text-align: right;">' + type1 + '</td>'
   			
   			$("#" + type + "_tbl").append(addRow);
   		}
   	}
}

function exception(target_id) {
	var form = document.createElement("form");
	var input   = document.createElement("input");
	input.type   = "hidden";
	input.name  = "target_id";
	input.value  = target_id;
	form.action = "<%=request.getContextPath()%>/exception/pi_exception_regist";
	form.method = "post";
	
	form.appendChild(input);
	document.body.appendChild(form);
	form.submit();
}

var aut = "manager"

$("#personal_server").on("click", function() {
	
	var postData = {
			fromDate : $("#fromDate").val(),
			toDate : $("#toDate").val(),
    }
   	$.ajax({
   		type: "POST",
   		url: "/dash_personal_server",
   		data : postData,
   		dataType : "JSON",
   		success: function (resultMap) {
            $("#personalServer").nextAll().html("");
            
            var addRow  = "";
            $.each(resultMap, function(index, item) {
            	
            	addRow += '<tr class="server_result" style="display:none;" data-uptidx="personal_server" data-level="2" data-mother="server" data-targetcnt="0" data-id="server">' 
                       		+'<th><p id="server_result_p'+ item.SCHEDULE_GROUP_ID +'" class="personal_server_tit" style="cursor:pointer; margin-left:20px;" >' + item.RESULT + "" ;
                    addRow += '</p><input type="hidden" id="groupId" value="'+ item.SCHEDULE_GROUP_ID  + '">';
                 	addRow += '</th></tr>';
            });
            
            $("#personalServer").after(addRow);
            
      	},
   	});
});

$("#personal_PC").on("click", function() {
	
	var postData = {
			fromDate : $("#fromDate").val(),
			toDate : $("#toDate").val(),
    }
   	$.ajax({
   		type: "POST",
   		url: "/dash_personal_PC",
   		data : postData,
   		dataType : "JSON",
   		success: function (resultMap) {
            $("#personalPC").nextAll().html("");
            
            var addRow  = "";
            $.each(resultMap, function(index, item) {
            	
               addRow += '<tr class="pc_result" style="display:none;" data-uptidx="personal_PC" data-level="2" data-mother="PC" data-targetcnt="0" data-id="PC">' 
                        +'<th><p id="pc_result_p'+ item.SCHEDULE_GROUP_ID +'" class="sta_tit" style="cursor:pointer; margin-left:20px;" >' + item.RESULT + "";
               addRow += '</p></th></tr>';
            });
            
            $("#personalPC").after(addRow);
            
      	},
   	});
});
	
$(document).on("click", ".personal_server_tit", function (e){
   	var id = $(this).attr('id');
   	var postData = {
   		id : id,
   		groupId : $("#groupId").val(),
   		fromDate : $("#fromDate").val(),
		toDate : $("#toDate").val(),
    }
   	$.ajax({
   		type: "POST",
   		url: "/dash_personal_server_circle",
   		data : postData,
   		dataType : "JSON",
   		success: circleGraph,
	    error: function (request, status, error) {
	        console.log("ERROR : ", error);
	    }
   	});
});	

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

function fn_drawrankGrid(data) {
	var postData = {
		user_no : '${memberInfo.USER_NO}'
	};

	// 컬럼 정의
	var columnDefs = [
		{
			headerName: '대상',
			field: 'NAME',
			flex: 1.2,
			cellStyle: { textAlign: 'center' },
			filterType: 'string'
		},
		{
			headerName: '검출파일수(컬럼수)',
			field: 'FILE',
			flex: 1.2,
			cellStyle: { textAlign: 'center' },
			cellRenderer: numberRenderer,
			filterType: 'number'
		},
		{
			headerName: '검출건수',
			field: 'COUNT',
			flex: 0.8,
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
	    paginationNumberFormatter: function(params) {
	        return params.value.toLocaleString();
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
		overlayNoRowsTemplate: '<span class="ag-overlay-no-rows-center">데이터가 없습니다</span>',
		suppressCellFocus: true,
		pagination: true,
		paginationPageSize: 20,
		paginationPageSizeSelector: [20, 25, 50],
		rowHeight: 35,
		headerHeight: 40,
		animateRows: true,
		
		onGridReady: function(params) {
			rankGridApi = params.api;
			
			// 데이터 로드
			$.ajax({
				url: "<%=request.getContextPath()%>/dash_dataRank",
				type: "POST",
				data: postData,
				dataType: "json",
				success: function(response) {
					if (rankGridApi && Array.isArray(response)) {
						rankGridApi.setGridOption('rowData', response);
					}
				},
				error: function(xhr, status, error) {
					console.error("데이터 로드 실패:", error);
				}
			});
		},
		
		onRowClicked: function(event) {
			// 행 선택 이벤트 처리
		}
	};

	// AG Grid 생성
	var gridDiv = $('#rankGrid').get(0);
	rankGridApi = agGrid.createGrid(gridDiv, gridOptions);  
}

</script>
</body>
</html>