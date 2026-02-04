<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%@ include file="../../include/header.jsp"%>
<style>
	#pcAdminPopup .ui-jqgrid tr.ui-row-ltr td{
		cursor: pointer;
	}
	#input_targetUserGridPager .ui-pg-input{
		margin: 2px 5px 5px;
	}
	.ui-jqgrid tr.ui-row-ltr td{
		cursor: pointer;
	}
	#sch_bar_sort *{
		text-align-last: center;
		text-align: center;
	}
</style>
		<section>
			<!-- container -->
				<div class="container" style="height: 1167px;">
					<%-- <%@ include file="../../include/menu.jsp"%> --%>
					<h3>검색 현황 관리</h3> 
					<p class="container_comment" style="position: absolute; top: 32px; left: 187px; font-size: 13px; color: #9E9E9E;"></p>
					<!-- content -->
					<div class="content magin_t25" style="height: 1071px;">
						<div id="lisk_serach" style="float: left; margin-right: 10px;">
							<table class="user_info narrowTable" style="width: 364px;">
								<tbody>
									<tr>
										<th style="text-align: center; border-radius: 0.25rem;">조회</th>
				             			     <td>
				                				 <input type="text" style="width: 274px; padding-left: 5px;" size="10" id="targetSearch" placeholder="검색어를 입력하세요.">
				                			 </td>
				                		<td>
				                    		 <input type="button" name="button" class="btn_look_approval" id="btn_sch_target" style="margin-top: 5px;">
				                    	</td>
									</tr>
								</tbody>
							</table>
						</div>
						<div>
							<table class="user_info approvalTh" style="width: 25%; min-width: 240px;" id="totalTable">
								<tbody>
									<tr>
										<td style="width: 22.3vw;">
											<input type="month" id="fromDate" class="allDateSearch" style="text-align: center;  width:194px;" readonly="readonly" value="${fromDate}" >
											<span class="allDateSearch">~</span>
											<input type="month" id="toDate" style="text-align: center;  width:194px;" readonly="readonly" value="${toDate}">
											<input type="hidden" id="date" style="text-align: center;  width:194px;" readonly="readonly" value="" >
											<input type="button" name="button" class="btn_look_approval" id="btnSearch" style="margin-top: 7px; margin-right: 4px;">
										</td>
									</tr>
								</tbody>
							</table>
						</div>
						<div class="content_left" style="width: 364px; float: left; height: 1084px;">
							<!-- <h3 style="display:inline; padding: 0;" id="title1">그룹조회</h3> -->
							<div class="left_box" style=" height: calc( 100% - 81px ); margin-top: 6px;">
								<div id="selectScanGroup"></div>
							</div>
						</div>
						<div class="content_left" style="width: 1404px; float: left; height: 1084px;">
							<div class="content_middle" style="width: 1400px; height: 341px; float: left; padding-left: 10px; margin-top: 6px;">
								<div class="mid_box" style="border: 1px solid #c8ced3; width: 1390px; height: calc( 100% - 6px ); border-radius: 0.25rem; "> 
									<input id="select_status" type="hidden">
									<!-- <div id="total_status" style="position: absolute; z-index: 10; right: 81px; top: 126px;">
										<select id="total_license_status" name="total_license_status" style="width: 81px; font-size: 12px; padding-left: 5px;">
		                            		<option value="TB" selected>TB</option>
		                                    <option value="GB" >GB</option>
		                                    <option value="byte" >byte</option>
		                            	</select>
									</div> -->
									<div id="total_bar_graph" style="width: 100%; height: 100%;"></div>
									<div id='none_total_bar' class='none_data'><!-- 대상에 사용된 라이선스가 존재하지 않습니다. --></div>
								</div>
							</div>	 
							<div class="content_middle" style="width: 1400px; height: 334px; float: left;  padding-left: 10px; ">
								<div class="mid_box" style="border: 1px solid #c8ced3; width: 1390px; height: calc( 100% - 6px ); margin-right: 6px; border-radius: 0.25rem; float: left;"> 
									<div id="top_license_graph" style="width: 100%; height: 100%;"></div>
									<div id='none_top' class='none_data'><!-- 대상에 사용된 라이선스가 존재하지 않습니다. --></div>
								</div>
							</div>	
							<div class="content_middle" style="width: 1400px; height: 341px; float: left;  padding-left: 10px;">
								<div class="mid_box" style="border: 1px solid #c8ced3; width: 1390px; height: calc( 100% - 16px ); border-radius: 0.25rem; margin: 3px 0 9px 0; "> 
									<div id="server_bar_graph" style="width: 100%; height: 100%;"></div>
									<div id='none_server' class='none_data'><!-- 대상에 사용된 라이선스가 존재하지 않습니다. --></div>
								</div>
							</div>	
						</div>
					<div> 
				</div>
			<!-- container -->
		</section>
		<!-- section -->

<%@ include file="../../include/footer.jsp"%>
<script type="text/javascript">
var group_name = "시스템 별";
var xbar = 5 ;// 세로바 가로 길이제한(스크롤)
var ybar = 8 ; // 가로바 세로 길이제한(스크롤)
$(document).ready(function() {
// 	monthGraph();
// 	monthGroupGraph();
// 	monthTargetGraph();
	
	monthGrpahData();
	serverGrpahData(null);
	groupGrpahData(null);
	
	$('#selectScanGroup').jstree({
		// List of active plugins
		"core" : {
		    "animation" : 0,
		    "check_callback" : true,
			"themes" : { "stripes" : false },
			"data" : ${groupList},
		},
		"types" : {
		    "#" : {
		      "max_children" : 1,
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
	    'checkbox' : {
	        'three_state': false
	    },
		'plugins' : [/* "contextmenu" */,"search",
			"types",
			"unique",
			/* "checkbox", */
			"changed"],
	}).bind("loaded.jstree", function(e, data) {
		
	
	}).bind('select_node.jstree', function(evt, data, x) {
		
		console.log(data);
		
		if(data.node.data.type == 0){
			
			var target_id = [];
			$.each(data.node.children_d, function(key, val){
				var nodeData = $("#selectScanGroup").jstree(true).get_node(val);
				
				console.log(nodeData);
				
				if(nodeData.data.type != 0){
					target_id.push(nodeData.data.target_id);
				}
			});
			 
			groupGrpahData(data.node.id);
			serverGrpahData2(target_id);
		}else{
			var target_id = []; 
			target_id.push(data.node.data.target_id);
			groupGrpahData(data.node.parent); 
			serverGrpahData2(target_id);
		}
		
	});
	
	setSelectDate();
});
    	
$("#btnSearch").click(function(e){
	
	var oToday = new Date();
	var month = oToday.getMonth();
	var searchType = 0;
	
	if($("#selectDay").val() == "totalLicense"){
		if($("#toDate").val() == null){
			alert("일자를 지정 후 검색해주세요.");
			return;
		}
		searchType = 1;
	}else{
		searchType = 2;
	}

	if($("#toDate").val() == "" || $("#fromDate").val() == ""){
	    alert("모든 일자를 지정 후 검색해주세요.");
	    return;
    }
	
	/* if($("#toDate").val() > getFormatDate(oToday)){
		alert("오늘 이전의 일자를 선택해주세요.");
		return;
	} */
	
	if($("#fromDate").val() > $("#toDate").val()){
		alert("입력한 끝 날짜가 시작 날짜 보다 빠릅니다.");
		return;
	}
	
	var postData = {
			toDate : $("#toDate").val(),
			fromDate : $("#fromDate").val()
	};

	monthGrpahData();
	serverGrpahData(null);
	groupGrpahData(null);
});		
    		
$("#btnExcelDownload").click(function(e){
	excelDownload();
});

function getFormatDate(oDate) {
	var nYear = oDate.getFullYear(); // yyyy 
	var nMonth = (1 + oDate.getMonth()); // M 
	nMonth = ('0' + nMonth).slice(-2); // month 두자리로 저장 

	//var nDay = oDate.getDate(); // d 
	//nDay = ('0' + nDay).slice(-2); // day 두자리로 저장

	//return nYear + '_' + nMonth + '_' + nDay;
	return nYear + '-' + nMonth;
}

function getFormatDate2(oDate) {
	var nYear = oDate.getFullYear(); // yyyy 
	var nMonth = (1 + oDate.getMonth()); // M 
	nMonth = ('0' + nMonth).slice(-2); // month 두자리로 저장 

	//var nDay = oDate.getDate(); // d 
	//nDay = ('0' + nDay).slice(-2); // day 두자리로 저장

	//return nYear + '_' + nMonth + '_' + nDay;
	return nYear + '_' + nMonth;
}

function setSelectDate() {

/* 	$("#fromDate").datepicker({
        dateFormat: 'yy-mm',
		changeMonth: true,
	    changeYear: true,
	    showButtonPanel: true,
	    onClose: function(dateText, inst) {  
            var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val(); 
            var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val(); 
            $(this).datepicker('setDate', new Date(year, month, 1));
            $(".ui-datepicker-calendar").css("display","none");
        }
    });
	
	$("#fromDate").focus(function () {
		$(".ui-datepicker-calendar").css("display","none");
		$("#ui-datepicker-div").position({
			  my: "center top",
			  at: "center bottom",
			  of: $(this)
		});
	}); */
	
	$("#fromDate").monthpicker({
		monthNames: ['01월', '02월', '03월', '04월', '05월', '06월',
        '07월', '08월', '09월', '10월', '11월', '12월'],
        monthNamesShort: ['01월', '02월', '03월', '04월', '05월', '06월', '07월', '08월', '09월', '10월', '11월', '12월'],
        //showOn: "button",
        //buttonImage: "../../Images/Goods/calendar.jpg",
        //buttonImageOnly: true,
        changeYear: false,
        yearRange: 'c-2:c+2',
        dateFormat: 'yy-mm'
        });
	
	$("#toDate").monthpicker({
		monthNames: ['01월', '02월', '03월', '04월', '05월', '06월',
        '07월', '08월', '09월', '10월', '11월', '12월'],
        monthNamesShort: ['01월', '02월', '03월', '04월', '05월', '06월', '07월', '08월', '09월', '10월', '11월', '12월'],
        //showOn: "button",
        //buttonImage: "../../Images/Goods/calendar.jpg",
        //buttonImageOnly: true,
        changeYear: false,
        yearRange: 'c-2:c+2',
        dateFormat: 'yy-mm'
        });
	 
	
	/* $("#toDate").datepicker({
        dateFormat: 'yy-mm',
		changeMonth: true,
	    changeYear: true,
	    showButtonPanel: true,
	    onClose: function(dateText, inst) {  
            var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val(); 
            var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val(); 
            $(this).datepicker('setDate', new Date(year, month, 1));
            $(".ui-datepicker-calendar").css("display","none");
        }
    });
	
	$("#toDate").focus(function () {
		$(".ui-datepicker-calendar").css("display","none");
		$("#ui-datepicker-div").position({
			  my: "center top",
			  at: "center bottom",
			  of: $(this)
		});
	}); */
/* 
    $("#toDate").datepicker({
        changeYear : true,
        changeMonth : true,
        dateFormat: 'yy-mm'
    });  */

	/*  var oToday = new Date();
	$("#toDate").val(getFormatDate(oToday));
	var toDate = $("#toDate").val();  */ 
	
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
        dateFormat: 'yyyy-mm',
		yearRange: 'c-5:c+5'
	});  
	
});


var to = true;
$('#btn_sch_target').on('click', function(){
	
    var v = $('#targetSearch').val();
	console.log(v);
	
	if(to) { clearTimeout(to); }
    to = setTimeout(function () {
      $('#selectScanGroup').jstree(true).search(v);
    }, 250);
});
$('#targetSearch').keyup(function (e) {
	var v = $('#targetSearch').val();
	if (e.keyCode == 13) {
    	
    	if(to) { clearTimeout(to); }
        to = setTimeout(function () {
          $('#selectScanGroup').jstree(true).search(v);
        }, 250);
    }
});

function monthGraph(data){
	
	var rate = [];
	var rate_cnt = [];
	var total = [];
	var day = [];
	console.log('monthGraph');
	console.log("data", data);
	
	$.each(data, function(key, val){
		var rate_data = val.TOTAL_RATE != null ? val.TOTAL_RATE : 0;
		var rate_cnt_data = val.PREVIOUS_TOTAL != null ? val.PREVIOUS_TOTAL : 0;
		var total_data = val.TOTAL != null ? val.TOTAL : 0;
		
		rate.push(rate_data);
		rate_cnt.push(rate_cnt_data);
		total.push(total_data);
		day.push(val.DATES.replace(/-01$/, ''));
		
	});

	var processedData = rate.map(value => ({
	    value: value,
	    itemStyle: {
	        color: value < 0 ? '#CC0F2E' : '#038FDA' 
	    } 
	}));
	
	console.log("total",total);
	console.log("processedData",processedData);
	
	var echartbar = echarts.init(document.getElementById('total_bar_graph'));
	echartbar.setOption({
		  tooltip: {
			    trigger: 'axis',
			    axisPointer: {
		            type: 'shadow'
		        }, 
		        formatter: function (params) {
		            var result = params.map(function (item) {
		                var valueWithAdditionalData = item.value;
		                 
		                if (item.seriesName === '전월 대비 검출 현황') {
		                    var additionalData = rate_cnt[item.dataIndex];
		                    valueWithAdditionalData += '% ('+additionalData+')';
		                } 
		                
		                
		                return item.marker + item.seriesName + ': ' + valueWithAdditionalData;
		            }).join('<br/>');
		            return result;
		        }
			  }, 
			  textStyle: {
					fontFamily: 'NotoSansR',
					fontSize: 14
				},
			  title: {
			    text: '월별 검출 현황',
			    /* subtext: '기본 단위 : TB', */
			    left: 'center'
			 },
			  legend: {
				data: ['전월 대비 검출 현황', '금월 검출 현황'],
			 	left : 0,
			 	selected: {
		            '전월 대비 검출 현황': true, 
		            '금월 검출 현황': false 
		        }
			  },
			  xAxis: [
			    {
			      type: 'category',
			      data: day,
			      axisLabel: {
		                interval:0,
		                rotate: 20, // 기울기
		                formatter: function (value) {
		                    if (value.length > 15) {
		                    value = value.substring(0, 9) + "...";
		                    }
		                    return value;
		                }
	              },
			      axisPointer: {
			        type: 'shadow'
			      }
			    }
			  ],
			  yAxis: {
			        type: 'value',
			        boundaryGap: [0, 0.01],
			        axisLabel: {
			        	type: 'value'
			          }
			    },
			  series: [ 
			    	{
				      name: '전월 대비 검출 현황',
				      type: 'bar',
				      color : ['#038FDA'],
				      barWidth: 30, 
				      data: processedData,
				      itemStyle: { 
			                normal: {
			                    label: {
			                        show: true,
			                        position: 'top',
			                        color: '#000',
			                        formatter: '{c}',
			    					fontSize: 9
			                    }
			                }
			            }
				    },
				    { 
					      name: '금월 검출 현황',
					      type: 'bar',
					      color : ['#CC0F2E'],
	                      barWidth: 30,
					      data: total,
					       itemStyle: {
				                normal: {
				                    label: {
				                        show: true,
				                        position: 'top',
				                        color: '#000',
				                        formatter: '{c}',
				    					fontSize: 9
				                    }
				                }
				            } 
				    },
			]
	});
};
function monthGroupGraph(data){
	console.log(data);
	var rate = [];
	var rate_cnt = [];
	var total = [];
	var name = [];
	
	$.each(data, function(key, val){
		var rate_data = val.TOTAL_RATE != null ? val.TOTAL_RATE : 0;
		var rate_cnt_data = val.PREVIOUS_TOTAL != null ? val.PREVIOUS_TOTAL : 0;
		var total_data = val.TOTAL != null ? val.TOTAL : 0;
		
		rate.push(rate_data);
		rate_cnt.push(rate_cnt_data);
		total.push(total_data);
		name.push(val.GROUP_NAME);
		
	});

	var processedData = rate.map(value => ({
	    value: value,
	    itemStyle: {
	        color: value < 0 ? '#CC0F2E' : '#038FDA' 
	    } 
	}));
	 
	console.log("name", name);
	
	var echartbar = echarts.init(document.getElementById('top_license_graph'));
	echartbar.setOption({
		  tooltip: {
			    trigger: 'axis',
			    axisPointer: {
		            type: 'shadow'
		        }, 
		        formatter: function (params) {
		            var result = params.map(function (item) {
		                var valueWithAdditionalData = item.value;
		                 
		                if (item.seriesName === '전월 대비 검출 현황') {
		                    var additionalData = rate_cnt[item.dataIndex];
		                    valueWithAdditionalData += '% ('+additionalData+')';
		                } 
		                
		                
		                return item.marker + item.seriesName + ': ' + valueWithAdditionalData;
		            }).join('<br/>');
		            return result;
		        }
			  }, 
			  textStyle: {
					fontFamily: 'NotoSansR',
					fontSize: 14
				},
			  title: {
			    text: '그룹 별 검출 현황',
			    /* subtext: '기본 단위 : TB', */
			    left: 'center'
			 },graphic: {
			        type: 'text',
			        left: '74%',  // 위치를 조정하는 부분
			        top: '5%',    // 위치를 조정하는 부분
			        style: {
			            text: '검출현황 기준 : 기간 내 가장 최근 검색된 월',
			            font: '13px NotoSansR',
			            fill: '#333' // 텍스트 색상
			           // 	font-family: 'NotoSansR';
			        }
			    },
			    legend: {
				data: ['전월 대비 검출 현황', '금월 검출 현황'],
			 	left : 0,
			 	selected: {
		            '전월 대비 검출 현황': true, 
		            '금월 검출 현황': false 
		        }
			  },
			  xAxis: [
			    {
			      type: 'category',
			      data: name,
			      axisLabel: {
		                interval:0,
		                rotate: 20, // 기울기
		                formatter: function (value) {
		                    if (value.length > 15) {
		                    value = value.substring(0, 9) + "...";
		                    }
		                    return value;
		                }
	              },
			      axisPointer: {
			        type: 'shadow'
			      }
			    }
			  ],
			  yAxis: {
			        type: 'value',
			        boundaryGap: [0, 0.01],
			        axisLabel: {
			        	type: 'value'
			          }
			    },
			  series: [ 
			    	{
				      name: '전월 대비 검출 현황',
				      type: 'bar',
				      color : ['#038FDA'],
				      barWidth: 30, 
				      data: processedData,
				      itemStyle: { 
			                normal: {
			                    label: {
			                        show: true,
			                        position: 'top',
			                        color: '#000',
			                        formatter: '{c}',
			    					fontSize: 9
			                    }
			                }
			            }
				    },
				    { 
					      name: '금월 검출 현황',
					      type: 'bar',
					      color : ['#CC0F2E'],
	                      barWidth: 30,
					      data: total,
					       itemStyle: {
				                normal: {
				                    label: {
				                        show: true,
				                        position: 'top',
				                        color: '#000',
				                        formatter: '{c}',
				    					fontSize: 9
				                    }
				                }
				            } 
				    },
			]
	});
};
function monthTargetGraph(data){
	console.log('monthTargetGrapgh');	
	console.log(data);	
	/*  data=[
		    {
		        "TOTAL": 24486,
		        "PATH_RATE": 35,
		        "TARGET_ID": "10954221269524029361",
		        "PATH_CNT": 654,
		        "PREVIOUS_PATH_CNT": 0,
		        "AP_NO": 1,
		        "PREVIOUS_TOTAL": 0,
		        "TOTAL_RATE": 35,
		        "ID": "2024-07-01",
		        "PREVIOUS_ID": "2024-06-01",
		        "NAME": "CATAL.CATALINAVM"
		    },
		    {
		        "TOTAL": 16194,
		        "PATH_RATE": 68,
		        "TARGET_ID": "11754877246832296031",
		        "PATH_CNT": 684,
		        "PREVIOUS_PATH_CNT": 0,
		        "AP_NO": 1,
		        "PREVIOUS_TOTAL": 0,
		        "TOTAL_RATE": 68,
		        "ID": "2024-07-01",
		        "PREVIOUS_ID": "2024-06-01",
		        "NAME": "DESKTOP-40OSTVV"
		    },
		    { 
		        "TOTAL": 129344,
		        "PATH_RATE": 23,
		        "TARGET_ID": "15068993249923171395",
		        "PATH_CNT": 7628,
		        "PREVIOUS_PATH_CNT": 0,
		        "AP_NO": 1,
		        "PREVIOUS_TOTAL": 0,
		        "TOTAL_RATE": 23,
		        "ID": "2024-07-01",
		        "PREVIOUS_ID": "2024-06-01",
		        "NAME": "DESKTOP-G13KU1L"
		    },
		    {
		        "TOTAL": 113478,
		        "PATH_RATE": 78,
		        "TARGET_ID": "2010644913634476611",
		        "PATH_CNT": 0,
		        "PREVIOUS_PATH_CNT": 0,
		        "AP_NO": 1,
		        "PREVIOUS_TOTAL": 0,
		        "TOTAL_RATE": 78,
		        "ID": "2024-07-01",
		        "PREVIOUS_ID": "2024-06-01",
		        "NAME": "CENTOS79PROXY"
		    },
		    {
		        "TOTAL": 212193,
		        "PATH_RATE": 100,
		        "TARGET_ID": "281937024237040039",
		        "PATH_CNT": 21624,
		        "PREVIOUS_PATH_CNT": 0,
		        "AP_NO": 1,
		        "PREVIOUS_TOTAL": 0, 
		        "TOTAL_RATE": 48,
		        "ID": "2024-07-01",
		        "PREVIOUS_ID": "2024-06-01",
		        "NAME": "DESKTOP-ATO33T8"
		    },
		    {
		        "TOTAL": 367656,
		        "PATH_RATE": 100,
		        "TARGET_ID": "3387307520972011341",
		        "PATH_CNT": 3507,
		        "PREVIOUS_PATH_CNT": 0,
		        "AP_NO": 1,
		        "PREVIOUS_TOTAL": 0,
		        "TOTAL_RATE": 100,
		        "ID": "2024-07-01",
		        "PREVIOUS_ID": "2024-06-01",
		        "NAME": "DESKTOP-GPJAGT4"
		    },
		    {
		        "TOTAL": 124885,
		        "PATH_RATE": 100,
		        "TARGET_ID": "8383726221968797506",
		        "PATH_CNT": 4032,
		        "PREVIOUS_PATH_CNT": 0,
		        "AP_NO": 1,
		        "PREVIOUS_TOTAL": 0,
		        "TOTAL_RATE": 100,
		        "ID": "2024-07-01",
		        "PREVIOUS_ID": "2024-06-01",
		        "NAME": "WINDB"
		    }
		]; */
	
	var x = 0;
	var bar_graph_10 = 0;
	var bar_graph_50 = 100;
	
	var rate = [];
	var rate_cnt = [];
	var total = [];
	var name = [];
	
	$.each(data, function(key, val){
		var rate_data = val.TOTAL_RATE != null ? val.TOTAL_RATE : 0;
		var rate_cnt_data = val.PREVIOUS_TOTAL != null ? val.PREVIOUS_TOTAL : 0;
		var total_data = val.TOTAL != null ? val.TOTAL : 0;
		
		rate.push(rate_data);
		rate_cnt.push(rate_cnt_data);
		total.push(total_data);
		name.push(val.NAME);
		
	});

	var processedData = rate.map(value => ({
	    value: value,
	    itemStyle: {
	        color: value < 0 ? '#CC0F2E' : '#038FDA' 
	    } 
	}));
	
	if(name.length > xbar){
		bar_graph_10 = 10; // 세로
		bar_graph_50 = 50; // 가로 
		
		if(name.length < 10){
			bar_graph_50 = 50;
		}else if(name.length < 20){
			bar_graph_50 = 30;
		}else if(name.length < 30){
			bar_graph_50 = 20;
		}else if(name.length < 40){
			bar_graph_50 = 15;
		}else if(name.length < 70){
			bar_graph_50 = 10;
		}else{
			bar_graph_50 = 5;
		}
		
	} 
	
	console.log(bar_graph_50,"bar_graph_50");
	
	var echartbar = echarts.init(document.getElementById('server_bar_graph'));
	echartbar.setOption({
		  tooltip: {
			    trigger: 'axis',
			    axisPointer: {
		            type: 'shadow'
		        }, 
		        formatter: function (params) {
		            var result = params.map(function (item) {
		                var valueWithAdditionalData = item.value;
		                 
		                if (item.seriesName === '전월 대비 검출 현황') {
		                    var additionalData = rate_cnt[item.dataIndex];
		                    valueWithAdditionalData += '% ('+additionalData+')';
		                } 
		                
		                
		                return item.marker + item.seriesName + ': ' + valueWithAdditionalData;
		            }).join('<br/>');
		            return result;
		        }
			  }, 
			  textStyle: {
					fontFamily: 'NotoSansR',
					fontSize: 14
				},
			  title: {
			    text: '서버 별 검출 현황',
			    /* subtext: '기본 단위 : TB', */
			    left: 'center'
			 },graphic: {
			        type: 'text',
			        left: '74%',  // 위치를 조정하는 부분
			        top: '5%',    // 위치를 조정하는 부분
			        style: {
			            text: '검출현황 기준 : 기간 내 가장 최근 검색된 월',
			            /* font: '14px Arial', */
			            font: '13px NotoSansR',
			            fill: '#333' // 텍스트 색상
			        }
			    }
			 ,dataZoom: [ // 가로 스크롤 (계열사 별 Bar 그래프)
				 {
			        type: 'slider',
			        xAxisIndex: 0,
			        filterMode: 'weakFilter',
			        height: bar_graph_10,
			        bottom: bar_graph_10,
			        start: 0,
			        end: 60,
			        handleSize: 0,
			        showDetail: false
			      },
			      {
			        type: 'inside',
			        id: 'insideX',
			        xAxisIndex: 0,
			        filterMode: 'weakFilter',
			        start: 0,
			        end: 26,
			        zoomOnMouseWheel: false,
			        moveOnMouseMove: true,
			        moveOnMouseWheel: true
			      }
			    ],
			  legend: {
				data: ['전월 대비 검출 현황', '금월 검출 현황'],
			 	left : 0,
			 	selected: {
		            '전월 대비 검출 현황': true, 
		            '금월 검출 현황': false 
		        }
			  },
			  xAxis: [
			    {
			      type: 'category',
			      data: name,
			      axisLabel: {
		                interval:0,
		                rotate: 20, // 기울기
		                formatter: function (value) {
		                    if (value.length > 15) {
		                    value = value.substring(0, 9) + "...";
		                    }
		                    return value;
		                }
	              },
			      axisPointer: {
			        type: 'shadow'
			      }
			    }
			  ],
			  yAxis: {
			        type: 'value',
			        boundaryGap: [0, 0.01],
			        axisLabel: {
			        	type: 'value'
			          }
			    },
			  series: [ 
			    	{
				      name: '전월 대비 검출 현황',
				      type: 'bar',
				      color : ['#038FDA'],
				      barWidth: 30, 
				      data: processedData,
				      itemStyle: { 
			                normal: {
			                    label: {
			                        show: true,
			                        position: 'top',
			                        color: '#000',
			                        formatter: '{c}',
			    					fontSize: 9
			                    }
			                }
			            }
				    },
				    { 
					      name: '금월 검출 현황',
					      type: 'bar',
					      color : ['#CC0F2E'],
	                      barWidth: 30,
					      data: total,
					       itemStyle: {
				                normal: {
				                    label: {
				                        show: true,
				                        position: 'top',
				                        color: '#000',
				                        formatter: '{c}',
				    					fontSize: 9
				                    }
				                }
				            } 
				    },
			]
	});
};

function monthGrpahData(){
	var postData = {
			toDate : $("#toDate").val(),
			fromDate : $("#fromDate").val()
	};
	
	 $.ajax({
	      type: "POST",
	      url: "/search/monthGrpahData",
	      async : false,
	      data: postData,
	      dataType: "json",
	      success: function (resultMap) {
	         if(resultMap.resultCode == -1){
	            console.log("검색 실패");             
	              return;
	           }
	         monthGraph(resultMap.resultData);
	       },
	       error: function (request, status, error) {
	           console.log("request : ", request);
	           console.log("status : ", status);
	           console.log("ERROR : ", error);
	       }
	   });
}

function serverGrpahData(target_id){
	
	var postData = {
			toDate : $("#toDate").val(),
			fromDate : $("#fromDate").val(),
	};
	 $.ajax({
	      type: "POST",
	      url: "/search/serverGrpahData",
	      async : false,
	      data: postData,
	      dataType: "json",
	      success: function (resultMap) {
	         if(resultMap.resultCode == -1){
	            console.log("검색 실패");             
	              return;
	           }
	         monthTargetGraph(resultMap.resultData);
	       },
	       error: function (request, status, error) {
	           console.log("request : ", request);
	           console.log("status : ", status);
	           console.log("ERROR : ", error);
	       }
	   });
}
function serverGrpahData2(target_id){
	
	var postData = {
			toDate : $("#toDate").val(),
			fromDate : $("#fromDate").val(),
			target_id : JSON.stringify(target_id),
	};
	 $.ajax({
	      type: "POST",
	      url: "/search/serverGrpahData2",
	      async : false,
	      data: postData,
	      dataType: "json",
	      success: function (resultMap) {
	         if(resultMap.resultCode == -1){
	            console.log("검색 실패");             
	              return;
	           }
	         monthTargetGraph(resultMap.resultData);
	       },
	       error: function (request, status, error) {
	           console.log("request : ", request);
	           console.log("status : ", status);
	           console.log("ERROR : ", error);
	       }
	   });
}

function groupGrpahData(group_id){
	var postData = {
			toDate : $("#toDate").val(),
			fromDate : $("#fromDate").val(),
			group_id : group_id
	};
	
	 $.ajax({
	      type: "POST",
	      url: "/search/groupGrpahData",
	      async : false,
	      data: postData,
	      dataType: "json",
	      success: function (resultMap) {
	         if(resultMap.resultCode == -1){
	            console.log("검색 실패");             
	              return;
	           }
	         monthGroupGraph(resultMap.resultData);
	       },
	       error: function (request, status, error) {
	           console.log("request : ", request);
	           console.log("status : ", status);
	           console.log("ERROR : ", error);
	       }
	   });
}

</script>
</body>
</html>