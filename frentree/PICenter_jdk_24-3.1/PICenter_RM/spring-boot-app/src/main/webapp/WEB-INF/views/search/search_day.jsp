<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="../../include/header.jsp"%>
<style>
	#targetGrid_SCHEDULE_ID2{
		border-right: none;
	}
	.user_info th {
		width: 100px;
	}
/* 	.ui-jqgrid tr.ui-row-ltr td{
		cursor: pointer;
	} */
	.stop_btn_css {
	background-color: #e4e4e4;
	}
	.stop_lable{
		border: 1px solid #c8ced3;
		padding: 3px 11px;
		border-radius: 4px;
		color: rgb(170, 170, 170);
	} 
</style>
		<!-- section -->
		<section>
			<!-- container -->
			<div class="container">
			<%-- <%@ include file="../../include/menu.jsp"%> --%>
				<h3 style="display: inline; top: 25px;">일자별 검색 현황</h3>
				<p class="container_comment" style="position: absolute; top: 32px; left: 210px; font-size: 13px; color: #9E9E9E;">일자별 개인정보 검출 스케줄 및 결과를 확인 하실수 있습니다.</p>
				<!-- content -->
				<div class="content magin_t35" style="height: 740px;">
					<div class="list_sch" style="height: 39px; margin-top: 10px;">
						<div class="sch_area">
							<div id="searchConditionsContainer" style="float: left;"></div>
							<div style="float: right; margin-left: 3px; margin-bottom: 7px;">
							<button type="button" id="btnScanRegist" class="btn_down">신규스캔등록</button>
							<button type="button" id="btnDownLoadExcel" class="btn_down">다운로드</button>
							</div>
							<div style="float: right;">
								<h5
									style="width: 24px; height: 24px; margin-top: 2px; background-size : 100%; background-image: url('${pageContext.request.contextPath}/resources/assets/images/pi_list.png');"
									id="tableShow">&nbsp;</h5>
							</div>
							<div class="btn_grid_Search"
								style="float: left; margin: 6px 3px 0 0;">&nbsp;</div>
						</div>
					</div>
					<div class="grid_top" style="width: 100%; padding-top:10px;">
						<div class="left_box2" style="height: auto; min-height: 630px; overflow: hidden; width:59vw;">
			    			<table id="targetGrid"></table>
			    			<div id="targetGridPager"></div>
						</div>
					</div>
				</div>
			</div>
			<!-- container -->
		</section>
		<!-- section -->
<!-- 그룹 선택 버튼 클릭 팝업 -->
<div id="taskGroupWindow" class="ui-widget-content" style="position:absolute; left: 10px; top: 10px; touch-action: none; width: 150px; z-index: 999; 
	border-top: 2px solid #2f353a; box-shadow: 0 2px 5px #ddd; display:none">
	<ul>
		<li class="status">
			<button id="pauseScheduleAll">전체 일시정지</button></li>
		<li class="status">
			<button id="restartScheduleAll">전체 재개</button></li>
		<li class="status">
			<button id="stopScheduleAll">전체 정지</button></li>
		<!-- <li class="status">
			<button id="cancelScheduleAll">전체 취소</button></li> -->
		<!-- <li class="status">
			<button id="completeScheduleAll">전체 완료</button></li> -->
		<li  class="schmanager">
			<button id="manageSchedule">스케줄 관리</button></li>
		<!-- <li class="status">
			<button id="confirmScheduleAll">확인</button></li> -->
	</ul>
</div>
<!-- 그룹 선택 버튼 클릭 팝업 종료 -->
	
<div id="viewWindow" class="ui-widget-content" style="position:absolute; left: 700px; top: 300px; touch-action: none; 
		border: 1px solid #1F3546; max-width: 900px; z-index: 999; display:none">
	<div class="popup_top">
		<h1>스케줄 세부사항</h1>
	</div>
	<div class="popup_content">
		<div class="content-box" style="width: 900px;">
			<h2>세부사항</h2>
			<table class="popup_tbl">
				<colgroup>
					<col width="20%">
					<col width="*">
				</colgroup>
				<tbody>
					<tr>
						<th>스캔명</th>
						<td id="scanLabel"></td>
					</tr>
					<tr>
						<th>스캔시간</th>
						<td id="scanNextScan"></td>
					</tr>
					<tr>
						<th>주기</th>
						<td id="scanRepeat"></td>
					</tr>
					<tr>
						<th>CPU</th>
						<td id="scanCPU"></td>
					</tr>
					<tr>
						<th>Throughput</th>
						<td id="scanThroughput"></td>
					</tr>
					<tr>
						<th>Memory 제한</th>
						<td id="scanMemory"></td>
					</tr>
					<tr>
						<th>개인정보 유형</th>
						<td id="scanDataType"></td>
					</tr>
				</tbody>
			</table>
			<h2 id="targets" style="padding-top: 10px;">targets</h2>
			<table class="popup_tbl">
				<colgroup>
					<col width="20%">
					<col width="*">
				</colgroup>
				<tbody id="targetBody">
					<tr>
						<th>서버이름</th>
						<td id="scanName"></td>
					</tr>
					<tr>
						<th>스캔경로</th>
						<td id="scanLocations"></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<div class="popup_btn">
		<div class="btn_area" style="border: 1px solid #efefef;">
			<button type="button" id="viewWindowClose" style="font-weight: inherit; font-size: 12px; padding: 0 15px;">확인</button>
		</div>
	</div>
</div>

<div id="viewDetails" class="popup_layer" style="display:none"> 
	<div class="ui-widget-content" id="popup_datatype" style="position:absolute; height: 425px; left: 27%; top: 25%; touch-action: none; max-width: 920px; z-index: 999; background: #f9f9f9;">
	<img class="CancleImg" id="btnCancleViewDetails" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_container">
			<div class="popup_top" style="background: #f9f9f9;">
				<h1 style="color: #222; box-shadow: none; padding: 0;">스케줄 세부사항</h1>
			</div>
			<div class="popup_content">
				<div class="content-box" style="width: 900px !important; background: #fff; border: 1px solid #c8ced3;height: 342px;">
					<table class="popup_tbl">
						<colgroup>
							<col width="20%">
							<col width="*">
						</colgroup>
						<tbody>
							<tr>
								<th>스캔명</th>
								<td id="details_label"></td>
							</tr>
							<tr>
								<th>개인정보 유형명</th>
								<td id="details_datatype"></td>
							</tr>
						</tbody>
					</table>
					<table class="popup_tbl">
						<colgroup>
							<col width="20%">
							<col width="*">
						</colgroup>
						<tbody>
							<!-- <tr>
								<th>정책 시작 시간</th>
								<td id="search_start_time"></td>
							</tr> -->
							<tr>
								<th style="height: 80px;">개인정보 유형</th>
								<td id="datatype_area">
							</tr>
							<!-- <tr>
								<th>검출시 동작</th>
								<td><input type="radio" name="action" value="0">즉시 삭제 
									<input type="radio" name="action" value="1">즉시 암호화 
									<input type="radio" name="action" value="2">익일 삭제 
									<input type="radio" name="action" value="3">익일 암호화 
									<input type="radio" name="action" value="4">선택 안함</td>
							</tr> -->
							<tr>
								<th>주기</th>
								<td id="cycle">
							</tr>
						</tbody>
					</table>
				</div>
				<%-- <div class="content-table" style="background: #fff; border: 1px solid #c8ced3; border-top: none;">
					<table class="popup_tbl">
						<colgroup>
							<col width="55%">
							<col width="15%">
							<col width="15%">
							<col width="15%">
						</colgroup>
						<tbody id="details_detail">
						</tbody>
					</table>
				</div> --%>
			</div>
			
			<div class="popup_btn">
				<div class="btn_area" style="padding: 10px 2px; margin: 0;">
					<!-- <p id="comment" style="position: absolute; bottom: 17px; right: 168px; font-size: 12px; color: #2C4E8C; text-align : center;">검색하는 파일 용량이 클 때, 진행률 변동이 없을 수 있습니다. 문의가 필요하시면 보안운영팀에 연락 바랍니다.</p> -->
					<button type="button" id="viewDetailsClose" style="font-weight: inherit; font-size: 12px; padding: 0 15px;">닫기</button>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- 진행율 팝업 -->
<div id="progressDetails" class="ui-widget-content" style="position:absolute; left: 10px; top: 10px; touch-action: none; width: 500px; z-index: 999; 
	box-shadow: 0 2px 5px #ddd; display:none;">
	<img class="CancleImg" id="btnCancleProgressDetails" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
	<div class="progress_container">
		<div class="progress_top" style="line-height: 20px;">
				<h1>검색 상세 현황</h1>
		</div>
		<input id="taskTargetid" type="hidden" val="" />
		<input id="taskApno" type="hidden" val="" />
		<input id="taskScheduleid" type="hidden" val="" />
		<input id="taskIndex" type="hidden" val="" />
		<ul style="background: #fff; border: 1px solid #c8ced3">
			<li class="status status-completed status-scheduled status-paused status-stopped status-failed" id="scanningDetailsName"></li>
			<li class="status status-completed status-scheduled status-paused status-stopped status-failed" id="scanningDetails"></li>
		</ul>
		<button id="confirmProgress" style="margin-top: 5px; margin-left : 424px;" >닫기</button>
	</div>
</div>

<div id="taskWindow" class="ui-widget-content" style="position:absolute; left: 10px; top: 10px; touch-action: none; width: 150px; z-index: 999; 
	border-top: 2px solid #2f353a; box-shadow: 0 2px 5px #ddd; display:none">
	<input id="taskTargetid" type="hidden" val="" />
	<input id="taskApno" type="hidden" val="" />
	<input id="taskScheduleid" type="hidden" val="" />
	<input id="taskIndex" type="hidden" val="" />
	<ul>
		<!-- <li class="status status-completed status-scheduled status-scanning status-paused status-stopped status-cancelled status-deactivated status-failed">
			<button id="viewSchedule" >보기</button></li> -->
		<!-- <li  class="status status-completed status-scheduled status-paused status-stopped status-failed">
			<button id="deactivateSchedule">비활성화</button></li> -->
			<!-- 
		<li class="status status-scheduled status-scanning">
			<button id="modifySchedule" >수정</button></li>
			 -->
		<!-- <li class="status status-completed status-scheduled status-scanning status-paused status-stopped status-queued">
			<button id="skipSchedule">스킵</button></li> -->
		<li class="status status-scanning">
			<button id="pauseSchedule" style="display: none;">일시정지</button></li>
		<li class="status status-paused">
			<button id="resumeSchedule" style="display: none;">재개</button></li>
		<!-- <li class="status status-completed status-stopped status-failed">
			<button id="restartSchedule">재시작</button></li> -->
		<!-- <li class="status status-scanning status-queued">
			<button id="stopSchedule">정지</button></li> -->
		<li class="status status-completed status-scheduled status-scanning status-paused status-stopped status-failed status-deactivated status-queued">
			<button id="cancelSchedule" style="display: none;">정지</button>
			<button id="stopSchedule" style="display: none;">정지</button>
		</li>
		<!-- <li  class="status status-deactivated">
			<button id="reactivateSchedule">활성화</button></li> -->
		<li  class="datatype_btn">
			<button id="statusDataType">개인정보 유형</button></li>
		<!-- <li  class="status status-completed status-scheduled status-scanning status-paused status-stopped status-failed status-deactivated">
			<button id="manageSchedule">스케줄 관리</button></li> -->
		<li class="status status-completed status-scheduled status-scanning status-paused status-stopped status-cancelled status-deactivated status-failed status-queued">
			<button id="confirmSchedule" >닫기</button></li>
	</ul>
</div>

<div id="popup_manageSchedule" class="popup_layer" style="display:none;">
	<div class="popup_box" id="popup_box" style="height: 60%; width: 60%; padding: 10px; background: #f9f9f9; left: 37%; top: 44%; right: 40%; ">
	</div>
</div>	


<div id="popup_lbl_insertSchedule" class="popup_layer" style="display:none;">
	<div class="popup_box" style="height: 385px; width: 731px; padding: 10px; background: #f9f9f9; left: 47%; top: 58%;">
	<img class="CancleImg" id="btnCancleInsertSchedule" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: none;">
			<h1 style="color: #222; padding: 0; box-shadow: none;">스케줄 관리</h1> 
			<button type="button" style="position: absolute; top: 8px; left: 109px;" 
					name="button" class="btn_down" id="schedule_stop_pop" onclick="scheduleAdd();">추가</button>
<!-- 			<p style="position: absolute; top: 14px; left: 109px; font-size: 12px; color: #9E9E9E;">스캔 일시정지 시간을 등록 할 수 있습니다</p>  -->
		</div> 
		<div class="popup_insertSchedule_content" style="padding: 5px 10px 10px 10px;  margin-top:10px; background: #fff; border: 1px solid #c8ced3; height: 297px;">
			<table style="border-bottom: 2px solid #2f353a;">
				<tr style="height: 20px;"> 
					<td colspan="5">기본 정지 시간</td>
				</tr>
				<tr style="height: 40px; border-bottom: 2px solid #2f353a;">
					<td><input type="checkbox" id="stop_scheudle" disabled="disabled"></td>
					<td style="padding-left: 10px;"> 정지 시간 :
						<label id="start_time" class="stop_lable"></label>
					</td>
					<td style="padding-left: 10px;">  ~ </td>
					<td style="padding-left: 10px;"> 시작 시간 :
						<label id="end_time" class="stop_lable"></label>
					</td>
					<td style="padding-left: 10px;">요일 :      
						<button class="btn_down stop_btn" type="button" id="stop_sun" value="0" name="btnSave" style="width: auto;" >일</button>
						<button class="btn_down stop_btn" type="button" id="stop_mon" value="1" name="btnSave" style="width: auto;" >월</button>
						<button class="btn_down stop_btn" type="button" id="stop_tue" value="2" name="btnSave" style="width: auto;" >화</button>
						<button class="btn_down stop_btn" type="button" id="stop_wed" value="3" name="btnSave" style="width: auto;" >수</button>
						<button class="btn_down stop_btn" type="button" id="stop_thu" value="4" name="btnSave" style="width: auto;" >목</button>
						<button class="btn_down stop_btn" type="button" id="stop_fri" value="5" name="btnSave" style="width: auto;" >금</button>
						<button class="btn_down stop_btn" type="button" id="stop_sat" value="6" name="btnSave" style="width: auto;" >토</button>
					</td>
				</tr >
			</table>
			<table id="stopTable">
				<tr style="height: 20px;">
					<td colspan="5" style="padding-top :5px; ">수동 정지 시간</td>
				</tr>
				<tr style="height: 40px;">
					<td><input type="checkbox" id="stop_scheudle1" class="stop_sch"></td>
					<td style="padding-left: 10px;"> 정지 시간 :
						<select name="start_time1" id="start_time1" disabled="disabled">
							<option value="" selected="selected">00</option>
						</select>
					</td>
					<td style="padding-left: 10px;">  ~ </td>
					<td style="padding-left: 10px;"> 시작 시간 :
						<select name="end_time1" id="end_time1" disabled="disabled">
							<option value="" selected="selected">00</option>
						</select>
					</td>
					<td style="padding-left: 10px;">요일 : 
						<button class="btn_down stop_btn1" type="button" id="stop_sun_1" name="btnSave1" style="width: auto;">일</button>
						<button class="btn_down stop_btn1" type="button" id="stop_mon_1" name="btnSave1" style="width: auto;">월</button>
						<button class="btn_down stop_btn1" type="button" id="stop_tue_1" name="btnSave1" style="width: auto;">화</button>
						<button class="btn_down stop_btn1" type="button" id="stop_wed_1" name="btnSave1" style="width: auto;">수</button>
						<button class="btn_down stop_btn1" type="button" id="stop_thu_1" name="btnSave1" style="width: auto;">목</button>
						<button class="btn_down stop_btn1" type="button" id="stop_fri_1" name="btnSave1" style="width: auto;">금</button>
						<button class="btn_down stop_btn1" type="button" id="stop_sat_1" name="btnSave1" style="width: auto;">토</button>
						<input type="checkbox" style="display: none;" name="stop_chk_box_1" id="check_sun_1" value="sun">
						<input type="checkbox" style="display: none;" name="stop_chk_box_1" id="check_mon_1" value="mon">
						<input type="checkbox" style="display: none;" name="stop_chk_box_1" id="check_tue_1" value="tue">
						<input type="checkbox" style="display: none;" name="stop_chk_box_1" id="check_wed_1" value="wed">
						<input type="checkbox" style="display: none;" name="stop_chk_box_1" id="check_thu_1" value="thu">
						<input type="checkbox" style="display: none;" name="stop_chk_box_1" id="check_fri_1" value="fri">
						<input type="checkbox" style="display: none;" name="stop_chk_box_1" id="check_sat_1" value="sat">
					</td>
				</tr>  
			</table>
		</div>
		<div class="popup_btn"> 
			<div class="btn_area">
				<button type="button" id="popup_done_insSchedule">저장</button>
				<button type="button" id="popup_cancel_insSchedule">취소</button>
			</div>
		</div>
	</div>
</div>

<!-- 개인 정보 유형 버튼 클릭 팝업 -->
<div id="SKTScheduleDataTypePopup" class="popup_layer" style="display:none"> 
	<div class="popup_box" style="height: 290px; width: 885px; padding: 10px; background: #f9f9f9; left: 44%; top: 62%;">
	<img class="CancleImg" id="btnCancleSKTScheduleDataTypePopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9; display: block;">
			<h1 style="color: #222; padding: 0; box-shadow: none;">개인정보 유형</h1>
		</div>
		<div class="popup_content" style="height: 240px;">
			<table class="user_info" style="width: 100%; height: 100%; margin-top: 10px; float: left;">
				<caption>정책 상세 정보</caption>
				<colgroup>
					<col width="15%">
					<col width="5%">
					<col width="80%">
				</colgroup>
				<tbody>
					<tr>
						<th style="border-bottom: 1px solid #c8ced3; border-radius: 0.25rem 0 0 0;">개인정보 유형 명</th>
						<td>-</td>
						<td id="left_datatype_name"></td>
					</tr>
					<tr>
						<th style="border-bottom: 1px solid #c8ced3;">개인정보유형</th>
						<td>-</td>
						<!-- <td id="left_datatype_name"></td> -->
						<td><table id="left_datatype" style="width: 90%;"></table></td>
					</tr>
					<tr>
						<th>검출시 동작</th>
						<td>-</td>
						<td id="action">
							<input type="checkbox" name="action" value="0" checked="checked"/>선택없음&nbsp;
							<input type="checkbox" name="action" value="1"/>즉시 삭제&nbsp;
							<input type="checkbox" name="action" value="2"/>즉시 암호화&nbsp;
							<input type="checkbox" name="action" value="3"/>익일 삭제&nbsp;
							<input type="checkbox" name="action" value="4"/>익일 암호화
						</td>
					</tr>
				</tbody>
			</table> 
		</div>
		<div class="popup_btn" style="height: 45px;">
			<div id="acesssBtn" class="btn_area" style="padding: 10px 0; margin: 0;">
				<button type="button" id="btnDataTypePopupClose" style="margin-top: 10px;" >닫기</button>
			</div>
		</div>
	</div>
</div>	
	
<div id="tableCustomData" class="ui-widget-content"
	style="position: absolute; right: 9%; top: 148px; touch-action: none; width: 165px; z-index: 999; border-top: 2px solid #222222; box-shadow: 2px 2px 5px #ddd; display: none">
	<table id="gridListTd">
	</table>
</div>
<%@ include file="../../include/footer.jsp"%>
 
<script type="text/javascript"> 

var oGrid = $("#targetGrid");
var schStopChk = 1;

var colModel = [];
GridName = "#targetGrid";

var pattern = '${pattern}'.split('[{').join('').split('}]').join('');
pattern = pattern.split('}, {');

$(document).ready(function () {

	//var timer = setInterval(setScanStatus, 10000);
	$(document).click(function(e){
		$("#taskGroupWindow").hide();
		//$("#taskWindow").hide();
		//$("#viewDetails").hide(); 
	});
	$("#taskWindow").click(function(e){
		e.stopPropagation();
	});
	$("#taskWindowClose").click(function(){
		$("#taskWindow").hide();
	});

  	$("#viewWindow").draggable({
 	    containment: '.content',
 	 	cancel : '.popup_content, .popup_btn'
	});
	
	$("#viewWindowClose").click(function(){
		$("#viewWindow").hide();
	});
	
	$("#btnDataTypePopupClose").click(function(e){
		$("#SKTScheduleDataTypePopup").hide();
	});
	
	$("#btnCancleSKTScheduleDataTypePopup").click(function(e){
		$("#SKTScheduleDataTypePopup").hide();
	});
	
	$("#viewDetailsClose").click(function(){
		
		$("#taskTargetid").val("");
		$("#taskApno").val("");
		$("#taskScheduleid").val("");
		$("#taskIndex").val("");
		$("#taskWindow").hide();
		$("#viewDetails").hide();
		$("#progressDetails").hide();
	})
	
	$("#btnCancleViewDetails").click(function(){
		
		$("#taskTargetid").val("");
		$("#taskApno").val("");
		$("#taskScheduleid").val("");
		$("#taskIndex").val("");
		$("#taskWindow").hide();
		$("#viewDetails").hide();
		$("#progressDetails").hide();
	})

	var ifConnect = function(cellvalue, options, rowObject) {
		if(cellvalue == null || cellvalue == ''){
			cellvalue = '<target deleted>'
		}
		return cellvalue.indexOf("<") >= 0 ? '< ' + cellvalue.replace("<","").replace(">","") + ' >' : cellvalue;
	};
	
	var createType = function(cellvalue, options, rowObject) {
		//return '<img src="/resources/assets/images/img_check.png" style="cursor: pointer;" name="gridSubSelBtn" class="gridSubSelBtn" value=" 선택 "></a>';
		return cellvalue == 0 ? "서버" : cellvalue == 1 ? "PC" : cellvalue == 2 ? "OneDrive" : cellvalue == 3 ? "DB" : "범위"; 
	};
	
	var createView = function(cellvalue, options, rowObject) {
		//return '<img src="/resources/assets/images/img_check.png" style="cursor: pointer;" name="gridSubSelBtn" class="gridSubSelBtn" value=" 선택 "></a>';
		var status = rowObject.STATUS;
		var result = "";
		
		if(status == 1){
			result = "<button type='button' style='padding-top: 4px; padding-bottom:4px;' class='gridSubSelBtn' name='gridSubSelBtn'>선택</button>";
		} else if (status == 2){
			result = "<p>완료한 스케줄입니다</p>"
		} else {
		}
		
		
		return result; 
	};
	
	$("#btnDownLoadExcel").click(function(){
		downLoadExcel();
	}); 
	
	$("#fromDate").datepicker({
		changeYear : true,
		changeMonth : true,
		dateFormat: 'yy-mm-dd',
		onSelect: function(dateText) {
			fn_search();
		}
	});
	$("#toDate").datepicker({
		changeYear : true,
		changeMonth : true,
		dateFormat: 'yy-mm-dd',
		onSelect: function(dateText) {
			fn_search();
		}
	});
	
	fn_drawTargetGrid();

	// 초기 조회
	var searchType = ['scheduled'];
	var postData = {
			fromDate : $("#fromDate").val(),
			toDate : $("#toDate").val(),
			searchType : JSON.stringify(searchType)};
	$("#targetGrid").setGridParam({url:"<%=request.getContextPath()%>/search/registDaySchedule", postData : postData, datatype:"json" }).trigger("reloadGrid");
	
	// 버튼 Action 설정
	$("#btnSearch").click(function() {
		fn_search();
    });
	
	$("#statusFlag").change(function(){
		fn_search();
	});

	$("#title").keyup(function(e){
		if (e.keyCode == 13) {
			fn_search();
		}
	});
	
	$("#hostname").keyup(function(e){
		if (e.keyCode == 13) {
			fn_search()
		}
	});
	
	$("#path").keyup(function(e){
		if (e.keyCode == 13) {
			fn_search()
		}
	});
	
	$("#chk_completed, #chk_deactivated, #chk_cancelled, #chk_stopped, #chk_failed").change(function() {
		$("#btnSearchScan").click();
	});
	
	
	$("#deactivateSchedule").click(function(){
		$("#taskWindow").hide();
		
		//changeSchedule("deactivate");
	});
	$("#modifySchedule").click(function(){
		$("#taskWindow").hide();
		
		//changeSchedule("modify");
	});
	$("#skipSchedule").click(function(){
		$("#taskWindow").hide();
		
		//changeSchedule("skip");
	});
	$("#pauseSchedule").click(function(){
		$("#taskWindow").hide();
		
		//changeSchedule("pause");
	});
	
	$("#resumeSchedule").click(function(){
		$("#taskWindow").hide();
		//changeSchedule(id, "restart", row);
		//changeSchedule("resume");
	});
	
	$("#restartSchedule").click(function(){
		$("#taskWindow").hide();
		//changeSchedule(id, "restart", row);
		//changeSchedule("restart");
	});
	$("#stopSchedule").click(function(){
		$("#taskWindow").hide();
		
		//changeSchedule("stop");
	});
	$("#cancelSchedule").click(function(){
		$("#taskWindow").hide();
       //changeSchedule("cancel");
	});
	$("#reactivateSchedule").click(function(){
		$("#taskWindow").hide();
		
		//changeSchedule("reactivate");
	});
	
	$("#confirmSchedule").click(function(){
		$("#taskWindow").hide();
	});
	
	$("#confirmProgress").click(function(){
		$("#progressDetails").hide();
	});
	
	$("#btnCancleProgressDetails").click(function(){
		$("#progressDetails").hide();
	});
	
	$("#btnScanRegist").click(function(){
		//window.location = "${pageContext.request.contextPath}/scan/pi_scan_regist";
		window.location = "${pageContext.request.contextPath}/search/search_regist";
	});
	
	$("#btnDownloadExel").click(function(){
		downLoadExcel();
	});
	
	// 스케줄관리 click
	$("#manageSchedule").click(function(){
		var row = $("#targetGrid").getGridParam( "selrow" );
		var id = $("#targetGrid").getCell(row, 'SCHEDULE_GROUP_ID');
		var name = $("#targetGrid").getCell(row, 'SCHEDULE_GROUP_NAME');
		// 스케줄관리 pop 생성
		setManageSchedulePop(id, name);
	});
	
	$("#pauseScheduleAll").click(function(){
		var statusList = ["scanning"];
		changeScheduleAll(statusList, "pause");
	});
	$("#restartScheduleAll").click(function(){
		var statusList = ["paused"];
		changeScheduleAll(statusList, "resume");
	});
	$("#stopScheduleAll").click(function(){
		var statusList = ["completed", "scheduled", "scanning", "paused", "stopped", "failed", "deactivated"];
		
		changeScheduleAll(statusList, "cancel");
	});
	$("#cancelScheduleAll").click(function(){
		var statusList = ["completed", "scheduled", "scanning", "paused", "stopped", "failed", "deactivated"];
		changeScheduleAll(statusList, "cancel");
	});
	// 전체 완료 버튼 클릭
	$("#completeScheduleAll").click(function(){
		var rowid = $("#targetGrid").jqGrid('getGridParam', "selrow" );  
	    var group_id =$("#targetGrid").getCell(rowid, 'SCHEDULE_GROUP_ID');
	 	
	 	var postData = {
	 		groupid : group_id,
	 	};
	 	var message = "대상 스케줄을 완료 처리하시겠습니까?";
	 	if (confirm(message)) {
		 	$.ajax({
		 		type: "POST",
		 		url: "/search/completedSchedule",
		 		async : false,
		 		data : postData,
		 	    success: function (resultMap) {
	
		 	        if (resultMap.resultCode != 0) {
		 	        	return;
		 		    }
		             	
		 	     	//$("#targetGrid").jqGrid('setCell', row, 'SCHEDULE_STATUS', changedTask);
	
		 	     	alert("대상 스케줄을 완료처리 하였습니다.");
		 	     	$("#targetGrid").setGridParam({url:"<%=request.getContextPath()%>/search/registScheduleGroup", postData : postData, datatype:"json" }).trigger("reloadGrid");
		 	       
		 	    },
		 	    error: function (request, status, error) {
		 			alert("ERROR");
		 	        console.log("ERROR : ", error);
		 	    }
		 	});
	 	}
	});
	
});

var today = new Date();
var dd = today.getDate();
var mm = today.getMonth()+1; //January is 0!
var yyyy = today.getFullYear();

if(dd<10) {
    dd='0'+dd
} 

if(mm<10) {
    mm='0'+mm
} 

today = yyyy + "" + mm + dd;

function downLoadExcel()
{
    oGrid.jqGrid("exportToCsv",{
        separator: ",",
        separatorReplace : "", // in order to interpret numbers
        quote : '"', 
        escquote : '"', 
        newLine : "\r\n", // navigator.userAgent.match(/Windows/) ? '\r\n' : '\n';
        replaceNewLine : " ",
        includeCaption : true,
        includeLabels : true,
        includeGroupHeader : true,
        includeFooter: true,
        fileName : "스케줄리스트_" + today + ".csv",
        mimetype : "text/csv; charset=utf-8",
        returnAsString : false
    })
}

/**
 * id : schedule ID, 
 * task : 변경할 Task 
 * row : 그리드의 변경 Row ID
 * Task 변경 규칙 
 * 	- deactivate -->  deactivated
 * 	- modify --> 스캐줄 정의 및 등록 화면으로 이동, 
 * 	- skip --> scheduled
 * 	- pause --> paused
 * 	- restart --> scheduled
 * 	- stop --> scheduled
 * 	- cancel --> cancelled
 * 	- reactivate --> scheduled
 */
function changeSchedule(task)
{

	var id = $("#taskScheduleid").val();
	var apno = $("#taskApno").val();
	var targetid = $("#taskTargetid").val();
	var index = $("#taskIndex").val();
	
	var postData = {
		ap_no : apno,
		target_id : targetid,
		id : id, 
		task : task
	};
	$.ajax({
		type: "POST",
		url: "/search/changeSchedule",
		async : false,
		data : postData,
	    success: function (resultMap) {

	        if ((resultMap.resultCode != 0) && (resultMap.resultCode != 200) && (resultMap.resultCode != 204)) {
		        alert("FAIL : " + resultMap.resultMessage);
	        	return;
		    }
	     	// 작업변경이 성공하면 상태도 변경 해 준다.
	     	var changedTask = "scheduled";
	     	switch (task) {
	     	case "deactivate" :
	     		changedTask = "deactivated";
	     		break;
	     	case "skip" :
	     		changedTask = "scheduled";
	     		break;
	     	case "pause" :
	     		changedTask = "pause";
	     		break;
	     	case "restart" :
	     		changedTask = "scheduled";
	     		break;
	     	case "stop" :
	     		break;
	     	case "cancel" :
	     		changedTask = "cancelled";
	     		break;
	     	case "reactivate" :
	     		changedTask = "scheduled";
	     		break;
     		default :
	     		changedTask = "scheduled";
     		break;
	     	}
	     	
	     	var rowid = $("#targetGrid").jqGrid('getGridParam', "selrow" );  
	     	
	     	var group_id =$("#targetGrid").getCell(rowid, 'SCHEDULE_GROUP_ID');
            
            var postData = {
            	group_id : group_id,
    	  	};
	    	
            $.ajax({
				type: "POST",
				url: "/search/registScheduleTargets",
				async : false,
				data : postData,
			    success: function (result) {
			    	console.log(result);
			    	
			    	var details = "";
			    	details += "<tr style=\"height: 45px;\">";
			    	details += "	<th>자산명</th>";
			    	details += "	<th>상태</th>";
			    	details += "	<th>진행율</th>"; 
			    	//details += "	<th>설정</th>";
			    	details += "</tr>";
			    	$.each(result, function(index, item) {

				    	var status = item.SCHEDULE_STATUS;
			    		var staus = setStatusKR(status, status);
			    		
			    		details += "<tr style=\"height: 45px;\">";
				    	details += "	<td style=\"text-align: left; padding-left: 0;\">"+item.NAME+"</td>";
				    	details += "	<td id=\"status_"+index+"\" style=\"text-align: center; padding-left: 0;\">"+staus+"</td>";
				    	if(item.SCHEDULE_STATUS == 'scanning'){
				    		details += "<td style=\"text-align: center; padding-left: 0;\" data-indx="+index+" data-targetid="+item.TARGET_ID+" data-policyid="+item.POLICY_ID+" data-apno="+item.AP_NO+" data-datatypeid="+item.DATATYPE_ID+" data-id="+item.RECON_SCHEDULE_ID+" data-status="+item.SCHEDULE_STATUS+" >" ;
				    		details += "<img src=\"${pageContext.request.contextPath}/resources/assets/images/scanning.gif\" style=\"margin-left: 5px; margin-top: 4px; cursor:pointer;\" class=\"statusChoiseBtn\" width=\"15px;\">"+"</td>";
				    		//details += "<button type='button' style=\"text-align: center; width: 54px; margin: 3px 17px 3px 42px; font-size: 0.6vw;\" class=\"statusChoiseBtn\">선택</button>"+"</td>";
				    	}else{
				    		details += "<td style=\"text-align: center; height: 25px; padding-left: 0;\">"+""+"</td>"
				    	}
				    	//details += "	<td style=\"text-align: center;\" data-indx="+index+" data-targetid="+item.TARGET_ID+" data-policyid="+item.POLICY_ID+" data-apno="+item.AP_NO+" data-datatypeid="+item.DATATYPE_ID+" data-id="+item.RECON_SCHEDULE_ID+" data-status="+item.SCHEDULE_STATUS+"><button type='button' style=\"text-align: center; width: 54px; margin: 3px 0 3px -20px; font-size: 11px;\" class=\"targetChoiseBtn\">선택</button></td>";
				    	details += "</tr>";
			    	});
			    	$("#details_detail").html(details);
			    },
			    error: function (request, status, error) {
			    	alert("정보를 불러오는데 실패하였습니다.");
			        console.log("ERROR : ", error);
			    }
	  		});


        	/* 검색 주기 확인 */
        	$(".targetChoiseBtn").on("click", function(e) {
        		var targetid = $(this).parent().data('targetid');
        		var ap_no = $(this).parent().data('apno');
        		var id = $(this).parent().data('id');
        		var status = $(this).parent().data('status');
        		var index = $(this).parent().data('index');
        		
        		//$("#targetGrid").setSelection(targetid);
        		
        		$("#taskTargetid").val(targetid);
        		$("#taskApno").val(ap_no);
        		$("#taskScheduleid").val(id);
        		$("#taskIndex").val(index);
        		
        		// 조건에 따라 Option 변경
        		var status = ".status-"+status;
        		$(".status").css("display", "none"); 
        		$(status).css("display", "block");
        		$(".manage-schedule").css("display", "block");
        		
        		var offset = $(this).parent().offset();
        		$("#taskWindow").css("left", (offset.left - $("#taskWindow").width()) + "px");
        		// $("#taskWindow").css("left", (offset.left - $("#taskWindow").width() + $(this).parent().width()) + "px");
        		$("#taskWindow").css("top", offset.top + $(this).height() + "px");

        		var bottomLimit = $(".left_box2").offset().top + $(".left_box2").height();
        		var taskBottom = Number($("#taskWindow").css("top").replace("px","")) + $("#taskWindow").height();

        		if (taskBottom > bottomLimit) { 
        			$("#taskWindow").css("top", Number($("#taskWindow").css("top").replace("px","")) - (taskBottom - bottomLimit) + "px");
        		} 
        		
        		if(status == '.status-scheduled'){ // 대기일때는 취소만
					$("#cancelSchedule").show();
					$("#stopSchedule").hide();
					$("#pauseSchedule").hide();
					$("#resumeSchedule").hide();
				}else if(status == '.status-paused'){
					$("#stopSchedule").hide();
					$("#cancelSchedule").show();
					$("#pauseSchedule").hide();
					$("#resumeSchedule").show();
				}else if(status == '.status-scanning'){
					$("#stopSchedule").show();
					$("#cancelSchedule").hide();
					$("#pauseSchedule").show();
					$("#resumeSchedule").hide();
				}else{
					$("#stopSchedule").show();
					$("#cancelSchedule").hide();
					$("#pauseSchedule").hide();
					$("#resumeSchedule").hide();
				}
        		
        		$("#taskWindow").show();
        	});
        	/* 검색 주기 확인 종료 */
        	
	     	/* 초기화  */
	     	$("#taskScheduleid").val("");
	    	$("#taskApno").val("");
	    	$("#taskTargetid").val("");
	    	$("#taskIndex").val("");	     	
	     	//$("#targetGrid").jqGrid('setCell', row, 'SCHEDULE_STATUS', changedTask);

	     	alert("스캔 스케줄의 상태가 변경되었습니다.");
		    
	    },
	    error: function (request, status, error) {
			alert("ERROR");
	        console.log("ERROR : ", error);
	    }
	});
}

 /**
  * id : schedule ID, 
  * task : 변경할 Task 
  * row : 그리드의 변경 Row ID
  * Task 변경 규칙 
  * 	- deactivate -->  deactivated
  * 	- modify --> 스캐줄 정의 및 등록 화면으로 이동, 
  * 	- skip --> scheduled
  * 	- pause --> paused
  * 	- restart --> scheduled
  * 	- stop --> scheduled
  * 	- cancel --> cancelled
  * 	- reactivate --> scheduled
  */
 function changeScheduleAll(tasks, task)
 {
    var rowid = $("#targetGrid").jqGrid('getGridParam', "selrow" );  
    var group_id =$("#targetGrid").getCell(rowid, 'SCHEDULE_GROUP_ID');
    
    console.log(tasks);
 	
 	var postData = {
 		groupid : group_id,
 		task : task,
 		tasks : tasks
 	};
 	$.ajax({
 		type: "POST",
 		url: "/search/changeScheduleAll",
 		async : false,
 		data : postData,
 	    success: function (resultMap) {

 	        if ((resultMap.resultCode != 0) && (resultMap.resultCode != 200) && (resultMap.resultCode != 204)) {
 		        alert("FAIL : " + resultMap.resultMessage);
 	        	return;
 		    }
             	
 	     	//$("#targetGrid").jqGrid('setCell', row, 'SCHEDULE_STATUS', changedTask);

 	     	alert("스캔 스케줄의 상태가 변경되었습니다.");
 	     	$("#targetGrid").setGridParam({url:"<%=request.getContextPath()%>/search/registScheduleGroup", postData : postData, datatype:"json" }).trigger("reloadGrid");
 	       
 	    },
 	    error: function (request, status, error) {
 			alert("ERROR");
 	        console.log("ERROR : ", error);
 	    }
 	});
 }


function setManageSchedulePop(id, name){
	$("#taskWindow").hide();
	
	
	console.log(id); 
	var html = "";
	
	$(".stop_btn1").attr("disabled", true);
	$(".stop_btn1").css("color", "#AAAAAA");
	$(".stop_btn1").css("pointer-events", "none");
	
	$.ajax({
		type: "POST",
		url: "/search/getStopSch",
		async : false,
		data : {
			sch_id : id
		},
	    success: function (result) {
	    	var chkList = 0;
		    if(result.resultCode == 0){
		    	
			    var schList = result.resultMessage;
			    if(schList == null){
			    	$("#stop_scheudle").prop("checked", false);
			    	$("#start_time").html("00");
		    		$("#end_time").html("00");
		    		$(".stop_btn").css("background-color", "#fff");
			    }else{
				    for(var s = 0 ; s < schList.length ; s++){
				    	
				    	if(schList[s].ACTIVE_STATUS == "02"){
				    		
					    	$("#stop_scheudle").prop("checked", true);
					    	
					    	var start_time = schList[s].PAUSE_TIME;
					    	
					    	if(start_time <10){
					    		start_time = "0"+start_time;
					    	}
					    	
				    		$("#start_time").html(start_time);
				    		$("#end_time").html(schList[s].START_TIME);
	
				    		var weekDayList = schList[s].WEEK_DAY.split(",");
				    		var buttons = document.querySelectorAll('.stop_btn');
				    		for(var w=0; w < weekDayList.length ; w++ ){
				    			 $("#stop_"+weekDayList[w]).css("background-color", "#e4e4e4");
				    		}
				    		$(".stop_btn").attr("disabled", true);
				    		$(".stop_btn").css("color", "#AAAAAA");
				    		$(".stop_btn").css("pointer-events", "none");	
				    		
				    		
				    	}else if(schList[s].ACTIVE_STATUS == "00"){
				    		++chkList;
				    		if(chkList != 1){
					    		scheduleAdd(); // 화면 추가
				    		}
				    		$("#stop_scheudle"+chkList).prop("checked", true);
				    		
// 				    		화면 그리기
  							var time_html = "";
				    		for(var i=0 ; i < 24 ; i++){
				    			var time = i;
				    			if(i < 10) time = "0"+i;
				    			
				    			time_html += "<option value=\""+time+"\">"+time+"</option>";
				    		}
							
				    		var startId = "start_time"+chkList;
				    	    var stopId = "end_time"+chkList;
				    	    var stopClass = "stop_btn"+chkList;
							
				    	    $("#"+startId).html(time_html);
					    	$("#"+stopId).html(time_html);
							
							$("select[name="+startId+"]").attr("disabled", false);
					    	$("select[name="+stopId+"]").attr("disabled", false);
					    	$('.'+stopClass).attr("disabled", false);
					    	$('.'+stopClass).css("color", "#000");
					    	$('.'+stopClass).css("pointer-events", "auto");
							
							
				    		if(start_time <10){
					    		start_time = "0"+start_time;
					    	}
				    		$("#start_time"+chkList).val(schList[s].PAUSE_TIME);
				    		$("#end_time"+chkList).val(schList[s].START_TIME);
				    		
				    		var weekDayList = schList[s].WEEK_DAY.split(",");
				    		
				    		for(var w=0; w < weekDayList.length ; w++ ){
				    			 $("#stop_"+weekDayList[w]+"_"+chkList).css("background-color", "#e4e4e4");
				    			 $("#check_"+weekDayList[w]+"_"+chkList).prop("checked", true);
				    		}
				    		
				    	}
				    }
			    }
			    
		    }else{
		    	alert("정보를 불러오는데 실패하였습니다.");
		    	return;
		    }
		 	
	    },
	    error: function (request, status, error) {
	    	alert("정보를 불러오는데 실패하였습니다.");
	    }
		});
	
	
// 	$(".stop_btn").css("color", "#AAAAAA");
// 	$(".stop_btn").css("pointer-events", "none");
	 
	$("#popup_lbl_insertSchedule").show();
	
	$(document).on('click', '.stop_sch', function() {
		var chkId = $(this).attr('id'); // 클릭된 버튼의 id 값을 가져옵니다.
		scheduleAction(chkId);
	});
}

// 스케줄 일시정지 table 추가
function scheduleAdd(){
	++schStopChk;
	if(schStopChk > 5) {
		alert("수동 스케줄 등록은 5번까지 등록이 가능합니다.");
		return;
	}
	 var table_html = "";
	 	table_html =  "<tr style=\"height:40px;\"><td><input type=\"checkbox\" id=\"stop_scheudle"+schStopChk+"\" class=\"stop_sch\"></td>\n";
		table_html += "<td style=\"padding-left: 10px;\"> 정지 시간 :\n";
		table_html += "<select name=\"start_time"+schStopChk+"\" id=\"start_time"+schStopChk+"\" disabled=\"disabled\">\n";
		table_html += "<option value=\"\" selected=\"selected\">00</option></select></td>\n";
		table_html += "<td style=\"padding-left: 10px;\">  ~ </td>\n";
		table_html += "<td style=\"padding-left: 10px;\"> 시작 시간 :\n";
		table_html += "<select name=\"end_time"+schStopChk+"\" id=\"end_time"+schStopChk+"\" disabled=\"disabled\">\n";
		table_html += "<option value=\"\" selected=\"selected\">00</option></select></td>\n";
		table_html += "<td style=\"padding-left: 10px;\">요일 : \n";
		table_html += "<button class=\"btn_down stop_btn"+schStopChk+"\" type=\"button\" id=\"stop_sun_"+schStopChk+"\" name=\"btnSave" + schStopChk + "\" style=\"width: auto;\">일</button>\n";
		table_html += "<button class=\"btn_down stop_btn"+schStopChk+"\" type=\"button\" id=\"stop_mon_"+schStopChk+"\" name=\"btnSave" + schStopChk + "\" style=\"width: auto;\">월</button>\n";
		table_html += "<button class=\"btn_down stop_btn"+schStopChk+"\" type=\"button\" id=\"stop_tue_"+schStopChk+"\" name=\"btnSave" + schStopChk + "\" style=\"width: auto;\">화</button>\n";
		table_html += "<button class=\"btn_down stop_btn"+schStopChk+"\" type=\"button\" id=\"stop_wed_"+schStopChk+"\" name=\"btnSave" + schStopChk + "\" style=\"width: auto;\">수</button>\n";
		table_html += "<button class=\"btn_down stop_btn"+schStopChk+"\" type=\"button\" id=\"stop_thu_"+schStopChk+"\" name=\"btnSave" + schStopChk + "\" style=\"width: auto;\">목</button>\n";
		table_html += "<button class=\"btn_down stop_btn"+schStopChk+"\" type=\"button\" id=\"stop_fri_"+schStopChk+"\" name=\"btnSave" + schStopChk + "\" style=\"width: auto;\">금</button>\n";
		table_html += "<button class=\"btn_down stop_btn"+schStopChk+"\" type=\"button\" id=\"stop_sat_"+schStopChk+"\" name=\"btnSave" + schStopChk + "\" style=\"width: auto;\">토</button>\n";
		table_html += "<input type=\"checkbox\" style=\"display: none;\" name=\"stop_chk_box_"+schStopChk+"\" id=\"check_sun_"+schStopChk+"\" value=\"sun\" >\n";
		table_html += "<input type=\"checkbox\" style=\"display: none;\" name=\"stop_chk_box_"+schStopChk+"\" id=\"check_mon_"+schStopChk+"\" value=\"mon\" >\n";
		table_html += "<input type=\"checkbox\" style=\"display: none;\" name=\"stop_chk_box_"+schStopChk+"\" id=\"check_tue_"+schStopChk+"\" value=\"tue\" >\n";
		table_html += "<input type=\"checkbox\" style=\"display: none;\" name=\"stop_chk_box_"+schStopChk+"\" id=\"check_wed_"+schStopChk+"\" value=\"wed\" >\n";
		table_html += "<input type=\"checkbox\" style=\"display: none;\" name=\"stop_chk_box_"+schStopChk+"\" id=\"check_thu_"+schStopChk+"\" value=\"thu\" >\n";
		table_html += "<input type=\"checkbox\" style=\"display: none;\" name=\"stop_chk_box_"+schStopChk+"\" id=\"check_fri_"+schStopChk+"\" value=\"fri\" >\n";
		table_html += "<input type=\"checkbox\" style=\"display: none;\" name=\"stop_chk_box_"+schStopChk+"\" id=\"check_sat_"+schStopChk+"\" value=\"sat\" >\n";
		table_html += "</td></tr>"
	
	$("#stopTable").append(table_html);
		
	$(".stop_btn"+schStopChk).attr("disabled", true);
	$(".stop_btn"+schStopChk).css("color", "#AAAAAA");
	$(".stop_btn"+schStopChk).css("pointer-events", "none");	
}

function scheduleAction(chkId){
	
	console.log(chkId);
    var startId = chkId.replace('stop_scheudle', 'start_time');
    var stopId = chkId.replace('stop_scheudle', 'end_time');
    var stopClass = chkId.replace('stop_scheudle', 'stop_btn');
	
    var chk = $("#"+chkId).is(':checked');
    
    var time_html = "";
    
	$("#"+chkId).one('change', function() {
		if ($("#"+startId).children('option').length <= 1) {
			for(var i=0 ; i < 24 ; i++){
    			var time = i;
    			if(i < 10) time = "0"+i;
    			
    			time_html += "<option value=\""+time+"\">"+time+"</option>";
    		}
			
			if(chk){
		    	$("#"+startId).html(time_html);
		    	$("#"+stopId).html(time_html);
			}
		}
		
		if(chk){
	    	$("select[name="+startId+"]").attr("disabled", false);
	    	$("select[name="+stopId+"]").attr("disabled", false);
	    	$('.'+stopClass).attr("disabled", false);
	    	$('.'+stopClass).css("color", "#000");
	    	$('.'+stopClass).css("pointer-events", "auto");
	    	
	    }else{
	    	$('.'+stopClass).attr("disabled", true);
	    	$('.'+stopClass).css("color", "#AAAAAA");
	    	$('.'+stopClass).css("pointer-events", "none");
	    	$("select[name="+startId+"]").attr("disabled", true);
	    	$("select[name="+stopId+"]").attr("disabled", true);
	    }
		
    });
	
	$(document).on('click', '.'+stopClass, function() {
		var buttonId = $(this).attr('id'); // 클릭된 버튼의 id 값을 가져옵니다.
	    $(this).toggleClass('stop_btn_css');
	    var checkBoxId = buttonId.replace('stop_', 'check_');
	    // 해당 체크박스의 선택 상태를 토글
	    $('#' + checkBoxId).prop('checked', function(i, val) {
	        return !val;
	    });
	});
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


function saveSchedule(){
	//alert($('td[data-value="true"]').length);
	var checkedKey = '';
	var stopDate = $('#stopDate').val();
	var stopHour = $('#stop_hour').val();
	var stopMinute = $('#stop_minute').val();
	
	$('td[data-value=true]').each(function(index, key){
		
		console.log(key);
	
		if(index != 0){
			checkedKey += ',';
		}
		checkedKey += $(key).data('key');
		
	});
	
	
	if(stopMinute < 10 && stopMinute != '' && stopMinute != 00) {
		stopMinute = '0' + stopMinute;
	}
	console.log('checkedKey : ' + checkedKey);
	console.log('stopDate : ' + stopDate);
	console.log('stopTime : ' + stopHour + ":" + stopMinute);
	
	if(checkedKey == null || checkedKey == ''){
		alert('스케줄을 선택하여 주세요.');
		return false;
	}
	
	$.ajax({
		type: "POST",
		url: "/scan/manageSchedule",
		async : false,
		data : {
			checkedKey: checkedKey,
			schedule_id: $('#pop_manageSchedule_schedule_id').val(),
			schedule_name: $('#pop_manageSchedule_schedule_name').val(),
			stopDate: stopDate,
			stopTime: stopHour + ":" + stopMinute
		},
		dataType: "text",
	    success: function (resultMap) {
		    alert("스케줄이 저장되었습니다.");
			$('#popup_manageSchedule').hide();
	    },
	    error: function (request, status, error) {
	    	alert("Recon Server에 접속할 수 없습니다.");
	        console.log("ERROR : ", error);
	    }
	});
	
}

function deleteSchedule(){
	$.ajax({
		type: "POST",
		url: "/scan/deleteSchedule",
		async : false,
		data : {
			schedule_id: $('#pop_manageSchedule_schedule_id').val(),
			schedule_name: $('#pop_manageSchedule_schedule_name').val()
		},
		dataType: "text",
	    success: function (resultMap) {
	    	var data = JSON.parse(resultMap);
	    	if(data.resultCode == '0'){
		    	setManageSchedulePop($('#pop_manageSchedule_schedule_id').val());
				alert('스케줄을 삭제 하였습니다.');
	    	} else {
	    		alert('스케줄을 실패하는데 실패하였습니다.');
	    	}
	    },
	    error: function (request, status, error) {
	    	alert("Recon Server에 접속할 수 없습니다.");
	        console.log("ERROR : ", error);
	    }
	});
}

// 스케줄 수동 입력 관련 script 
$('#popup_done_insSchedule').click(function(){
	var test_table = document.getElementById('stopTable');
	var row_count = test_table.rows.length;
	var chk = true;
	
	for(var r=1; r <  row_count ; r++){
	}

	var row = $("#targetGrid").getGridParam( "selrow" );
	var id = $("#targetGrid").getCell(row, 'SCHEDULE_GROUP_ID');
	var recon_id = $("#targetGrid").getCell(row, 'RECON_SCHEDULE_ID');
	var schedule_name = $("#targetGrid").getCell(row, 'SCHEDULE_GROUP_NAME');
	
	var stop_chk_list = [];
	var pauseList = {};
	
	for(var r=1; r <  row_count ; r++){
		
		if (chk && $("#stop_scheudle"+r).prop("checked") == true) {
			chk = false;
		}
		
		if ($("#stop_scheudle"+r).prop("checked") == true) {
				pauseList = {};
				pauseList.start = $("#start_time"+r).val();
				pauseList.stop = $("#end_time"+r).val();
		
				var selectStopDay = $('input[name="stop_chk_box_'+r+'"]:checked').map(function() {
		            return this.value; // 선택된 체크박스의 값
		        }).get();
				
				
				if(selectStopDay.length == 0){
					alert("검색 정지 요일을 선택해주세요." + r);
					return;
				}
				pauseList.day = selectStopDay;
				console.log(pauseList);
				stop_chk_list.push(pauseList);
		}
	}
	
	if(chk){
		alert("선택된 일시정지 스케줄이 없습니다.");
		return;
	}
	
	var postData = {
	  		sch_id : id,
	  		recon_id : recon_id,
	  		schedule_name : schedule_name,
	  		stopList : JSON.stringify(stop_chk_list)
		};
	
	
	if(confirm("일시정지 스케줄을 등록하시겠습니까?")){
		$.ajax({
			type: "POST",
			url: "/search/schStopUpdate",
			async : false,
			data : postData,
		    success: function (result) {
			 	
		    	if(result.resultCode == 0){
		    		alert("스케줄 등록이 완료되었습니다.");
		            $('#popup_lbl_insertSchedule').hide();
		    	}
		    },
		    error: function (request, status, error) {
		    	alert("정책 정보를 갖고 올수 없습니다.");
		        console.log("ERROR : ", error);
		    }
	  	});
	}
});

$('#popup_cancel_insSchedule').click(function(){
	$('#popup_lbl_insertSchedule').hide();
});

$('#btnCancleInsertSchedule').click(function(){
	$('#popup_lbl_insertSchedule').hide();
});

function setSchedule(){
	
	

	
}
function createCheckbox(cellvalue, options, rowObject) {
	//'completed':'완료','deactivated':'비활성화','cancelled':'취소','stopped':'중지','failed':'실패','scheduled':'예약','paused':'일시정지','scanning':'스캔중'
	var arr_disable = ['completed', 'cancelled', 'pending'];
	var status = rowObject['SCHEDULE_STATUS'];
	
	if (arr_disable.indexOf(status) > 0)
		return "<input type='checkbox' name='chkbox' data-id='" + rowObject['SCHEDULE_ID'] + "' data-status='" + status + "' disabled='disabled'>"; 
	else 
		return "<input type='checkbox' name='chkbox' data-id='" + rowObject['SCHEDULE_ID'] + "' data-status='" + status + "'>";
}

function executeChecked(action){
	var obj = {};
	var nextStepFlag = true;
	if('start' == action){
		var arr_able = ['stopped', 'failed', 'paused', 'deactivated'];
		$('input:checkbox[name=chkbox]:checked').each(function(){
			var id = $(this).data('id');
			var status = $(this).data('status');
			if(arr_able.indexOf(status) < 0){
				nextStepFlag = false;
			} else {
				obj[id] = status;
			}
		});
	} else if ('pause' == action){
		var arr_able = ['pending', 'scanning', 'scheduled'];
		$('input:checkbox[name=chkbox]:checked').each(function(){
			var id = $(this).data('id');
			var status = $(this).data('status');
			if(arr_able.indexOf(status) < 0){
				nextStepFlag = false;
			} else {
				obj[id] = status;
			}
		});
	}
	
	if(!nextStepFlag) {
		var action_kr = ('start' == action)? '시작': '정지';
		alert(action_kr + '할 수 없는 상태의 스케줄이 체크되었습니다.\n체크된 항목들을 확인 부탁드립니다.');
		return;
	}
	
	$.ajax({
		type: "POST",
		url: "/scan/executeChecked",
		async : false,
		contentType : 'application/json; charset=UTF-8',
		data : JSON.stringify(obj),
		dataType: "json",
	    success: function (resultMap) {
			console.log(resultMap);
			var action_kr = ('start' == action)? '시작': '정지';
			if('00' == resultMap.resultCode){
				alert('성공적으로 '+action_kr + ' 되었습니다.');
				location.reload();
			} else {
				alert(action_kr + '이 실패하였습니다.');
			}
	    },
	    error: function (request, status, error) {
	    	alert("Recon Server에 접속할 수 없습니다.");
	        console.log("ERROR : ", error);
	    }
	});
}

function setScanStatus(){
	var rows = $("#targetGrid").getDataIDs();
	$.each(rows, function(index, rowid) {
		var rowData = $("#targetGrid").getRowData(rowid);
		var id = rowData["SCHEDULE_ID"];
		var status = rowData["SCHEDULE_STATUS"];
		var ap_no = rowData["AP_NO"];
		status = setStatusEN(status);
		if('completed' == status || 'cancelled' == status){
			status = setStatusKR(status, status);
			$("#targetGrid").jqGrid('setCell',rowid,'SCHEDULE_STATUS',status);
		}else{
			setStatusExecute(rowid, status, id, ap_no);
		}

	});

}

function setStatusExecute(rowid, status, id, ap_no){
	var result = "";
	var postData = {
			id : id, 
			ap_no : ap_no
			};
	
	$.ajax({
		type: "POST",
		url: "/scan/getDetails",
		async : false,
		data : postData,
	    success: function (result) {
	    	if(result.resultCode == '404'){
	    		var idx = 1;		
	    		var searchType = ['scheduled'];
	    		$("input[name=chk_schedule]:checked").each(function(i, elem) {
	    			searchType[idx++] = $(elem).val();	
	    		});

	    		var postData = {
	    				fromDate : $("#fromDate").val(),
	    				toDate : $("#toDate").val(),
	    				hostName : $("#searchHost").val(),
	    				searchType : JSON.stringify(searchType)
	    		};

	    		$("#targetGrid").setGridParam({url:"<%=request.getContextPath()%>/scan/pi_scan_schedule", postData : postData, datatype:"json" }).trigger("reloadGrid");
	    	} else {
		    	$.each(result.resultData, function(index, item) {
		    		var stat = "";
		    		if(index == 0){
		    			console.log(item)
		    			stat = setStatusKR(item.status, status);
		    			if(item.percentage != ''){
		    				stat = stat+'\n'+item.percentage;
		    				console.log(stat)
		    			}
		    			result = stat;
		    			$("#targetGrid").jqGrid('setCell',rowid,'SCHEDULE_STATUS',result);
						$("#targetGrid").jqGrid('setCell',rowid,'SCHEDULE_STAT',setStatusEN(result));
		    		}
		    	});
	    	}
	    },
	    error: function (request, status, error) {
	        console.log("ERROR : ", error);
	        result = setStatusKR(status, status);
	        $("#targetGrid").jqGrid('setCell',rowid,'SCHEDULE_STATUS',result);
	    }
	});
}

function setStatusKR(status, status_ori){
	//'completed':'완료','deactivated':'비활성화','cancelled':'취소','stopped':'중지','failed':'실패','scheduled':'예약','paused':'일시정지','scanning':'스캔중'
	var result = status_ori;
	/* if('completed' == status){
		result = "완료";
	}else if('deactivated' == status){
		result = "비활성화";
	}else if('cancelled' == status){
		result = "취소";
	}else if('stopped' == status){
		result = "중지";
	}else if('failed' == status){
		result = "실패";
	}else if('scheduled' == status){
		result = "예약";
	}else if('paused' == status || 'autopaused' == status){
		result = "일시정지";
	}else if('scanning' == status){
		result = "스캔중";
	}else if('error' == status){
		result = "중지";
	}else if('queued' == status){
		result = "대기중";
	}else if('scheduled' == status){
		result = "예정";
	}else if('notscanned' == status){
		result = "미검색";
	} */
	if(status == 'cancelled'){
		result = '검색정지';
/* 		result = '검색취소'; */
	} else if(status == 'scheduled'){
		result = '검색대기';
	}else if(status == 'failed'){
		result = '검색실패';
	}else if(status == 'scanning'){
		result = '검색중';
	}else if(status == 'paused'){
		result = '검색일시정지';
	}else if(status == 'completed'){
		result = '검색완료';
	}else if(status == 'stopped'){
		result = '검색정지';
	}else if(status == 'stalled'){
		result = '검색멈춤';
	}else if(status == 'paused'){
		result = '검색정지';
	}else if(status == 'deactivated'){
		result = '검색비활성';
	}else if(status == 'queued'){
		result = '검색대기';
	}else if(status == 'interrupted'){
		result = '검색중단';
	}else if(status == null){
		result = '';
	}
	
	return result;
}

function setStatusEN(status){
	//'completed':'완료','deactivated':'비활성화','cancelled':'취소','stopped':'중지','failed':'실패','scheduled':'예약','paused':'일시정지','scanning':'스캔중'
	var result = status;
	/* if(status.indexOf("완료") >= 0){
		result = "completed";
	}else if(status.indexOf("비활성화") >= 0){
		result = "deactivated";
	}else if(status.indexOf("취소") >= 0){
		result = "cancelled";
	}else if(status.indexOf("중지") >= 0){
		result = "stopped";
	}else if(status.indexOf("실패") >= 0){
		result = "failed";
	}else if(status.indexOf("예약") >= 0){
		result = "scheduled";
	}else if(status.indexOf("일시정지") >= 0){
		result = "paused";
	}else if(status.indexOf("스캔중") >= 0){
		result = "scanning";
	}else if(status.indexOf("대기중") >= 0){
		result = "queued";
	}else if(status.indexOf("예정") >= 0){
		result = "schduled";
	}else if(status.indexOf("미검색") >= 0){
		result = "notscanned";
	} */
	
	return result;
}

/* 검색 시작 시간 확인 */
function setStartdtm(rowData){
	var start_dtm = rowData.START_DTM;
	var start_ymd = '';
	var start_hour = '';
	var start_minutes = '';
	if(start_dtm != null && start_dtm != ''){
		start_ymd = start_dtm.substr(0,10);
		start_hour = start_dtm.substr(11,2);
		start_minutes = start_dtm.substr(14,2);
	}
	
	var html = "";
	html += "<input type='date' id='start_ymd' style='text-align: center; height: 27px;' value='"+start_ymd+"' disabled='disabled'>&nbsp;";

	html += "<select name=\"start_hour\" id=\"start_hour\" disabled='disabled'>"
	for(var i=0; i<24; i++){
		var hour = (parseInt(i)+1);
		var str_hour = (parseInt(hour) < 10) ? '0'+hour : hour
		html += "<option value=\""+str_hour+"\" "+(hour == parseInt(start_hour)? 'selected': '')+">"+str_hour+"</option>"
	}
	html += "</select> : "

	html += "<select name=\"start_minutes\" id=\"start_minutes\" disabled='disabled'>"
	for(var i=0; i<60; i++){
		var minutes = parseInt(i)
		var str_minutes = (parseInt(minutes) < 10) ? '0'+minutes : minutes
		html += "<option value=\""+str_minutes+"\" "+(minutes == parseInt(start_minutes)? 'selected': '')+">"+str_minutes+"</option>"
	}
	html += "</select>"
	
	return html;
}
/* 검색 시작 시간 확인 종료 */


/* 검색 정책 데이터유형 확인 */
function setDatatype(rowData){
	
	var html = ""; 
	if(rowData != "" && rowData != null){
		if(rowData.DATATYPE != null && rowData.DATATYPE != ""){
			
			var dataList = JSON.parse(rowData.DATATYPE);
			html = "<table style=\"width: 90%;\">";
			var trCnt = 0;
			
			 for(var i = 0; pattern.length > i; i++){
			
				 var cnt = 0;
				 var dup = 0;
				 var stats = 0;
				 
				 if(trCnt == 0){
					 html == "<tr>";
				 }
				 
				var row = pattern[i].split(', ');
				var ID = row[0].split('ID=').join(''); //변수 TABLE_NAME은 TB_NAME1, 다음 반복문에서는 TB_NAME2
				var PATTERN_NAME = row[1].split('PATTERN_NAME=').join(''); // 변수 CHK Y, 다음 반복문에서는 N
				var data_id = PATTERN_NAME.split('=')[1];
				var PATTERN_KR_NAME = ID.split('=')[1];
				
				html +="	<th style=\"width: 18%;\">"+PATTERN_KR_NAME+"</th>";
				
				for(j = 0 ; j < dataList.length ; j++){
					
					var typename = dataList[j].TYPE;
					
					if(data_id==typename){
						cnt = dataList[j]._CNT;
						dup = dataList[j]._DUP; 
						chk = dataList[j]._CHK; 
						stats = 1;
					
						break;
					}
				}
				html +="	<td style=\"width: 15%;\"><input type='checkbox' disabled='disabled' "+((chk == 1)?"checked='checked'":"")+">&nbsp;";
				html +="	<input type='checkbox' disabled='disabled' "+((dup == 1)?"checked='checked'":"")+">&nbsp;";
				html += 	(chk==1)? cnt : '';
				html += "	</td>";
					
					++trCnt;
				 if(trCnt % 3 == 0){
					 html += "</tr>";
					 trCnt = 0;
				 }
			}
			 
			html += "	<th>증분검사</th>";
			html += "	<td><input type='checkbox' disabled='disabled' "+((rowData.RECENT > 0 )?"checked='checked'":"")+">&nbsp;";
			html += "	" + ((rowData.RECENT > 0 )? rowData.RECENT :"") ;
			html += "	</td>";
			html += "</tr>";
			html += "</table>";
		}else{
			html = "";
		}
	}
	
	return html;
}
/* 검색 정책 데이터유형 확인 종료 */


/* 검색 주기 확인 */
function setCycle(rowData){
	var cycle = rowData.CYCLE;
	
	var html = "";
	html += "<select name=\"cycle\" id=\"cycle\" disabled=\"disabled\">"
	html += "	<option value=\"0\" "+((cycle == 0)? 'selected': '')+">한번만</option>"
	html += "	<option value=\"1\" "+((cycle == 1)? 'selected': '')+">매일</option>"
	html += "	<option value=\"2\" "+((cycle == 2)? 'selected': '')+">매주</option>"
	html += "	<option value=\"3\" "+((cycle == 3)? 'selected': '')+">매월</option>"
	html += "</select>"
	
	return html;
}
/* 검색 주기 확인 종료 */
function downLoadExcel()
{
	
	oGrid.jqGrid("hideCol",["CHK"]);

	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1;
	var yyyy = today.getFullYear();
	if(dd<10) {
	    dd='0'+dd
	} 

	if(mm<10) {
	    mm='0'+mm
	} 

	today = yyyy + "" + mm + dd;
	console.log(today);
	
	oGrid.jqGrid("exportToCsv",{
        separator: ",",
        separatorReplace : "", // in order to interpret numbers
        quote : '"', 
        escquote : '"', 
        newLine : "\r\n", // navigator.userAgent.match(/Windows/) ? '\r\n' : '\n';
        replaceNewLine : " ",
        includeCaption : true,
        includeLabels : true,
        includeGroupHeader : true,
        includeFooter: true,
        fileName : "검색_스케줄_" + today + ".csv",
        mimetype : "text/csv; charset=utf-8",
        returnAsString : false
    });
	$("#serverGrid").jqGrid("showCol",["CHK"]);
} 

function fn_drawTargetGrid(){
	
	var gridWidth = $("#targetGrid").parent().width();
	var gridHeight = 555;
	var patternCnt = '${patternCnt}';
	
	var colNames = ['','policy_id','schedule_id' ,'스케줄명','호스트명','target_id','ap_no','경로','스캔 상태', '검색 등록일', '검색 시작일','검색 완료일','검출 경로수', '총 검출수'];
	colModel = [
		{ index: 'SCHEDULE_GROUP_ID', 		name: 'SCHEDULE_GROUP_ID',		width: 80, align: 'center',hidden:true},
		{ index: 'POLICY_ID', 		name: 'POLICY_ID',		width: 80, align: 'center',hidden:true},
		{ index: 'SCHEDULE_ID', 		name: 'SCHEDULE_ID',		width: 80, align: 'center',hidden:true},
		{ index: 'SCHEDULE_GROUP_NAME', 		name: 'SCHEDULE_GROUP_NAME',		width: 150, align: 'center'},
		{ index: 'NAME', 					name: 'NAME',					width: 150, align: 'center'},
		{ index: 'TARGET_ID', 		name: 'TARGET_ID',		width: 80, align: 'center',hidden:true},
		{ index: 'AP_NO', 		name: 'AP_NO',		width: 80, align: 'center',hidden:true},
		{ index: 'DESCRIPTION',						name: 'DESCRIPTION',			width: 300, align: 'left'},
		{ index: 'STATUS',						name: 'STATUS', 					width: 100, align: 'center' , formatter:formatScheduleStatus/* , cellattr: backGroundCss */},
		{ index: 'REGDATE',						name: 'REGDATE', 					width: 150, align: 'center' },
		{ index: 'START_TIME',					name: 'START_TIME', 					width: 150, align: 'center'} ,
		{ index: 'END_TIME',					name: 'END_TIME', 				width: 150, align: 'center'},
		{ index: 'PATH_CNT',					name: 'PATH_CNT', 				width: 100, align: 'center', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, sortable: true, sorttype: 'number' },
		{ index: 'TOTAL',				name: 'TOTAL', 			width: 100, align: 'center', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, sortable: true, sorttype: 'number' }
	]
	
	for(var i=0; i < patternCnt ; i++){
		var row = pattern[i].split(', ');
		var ID = row[0].split('ID=').join(''); //변수 TABLE_NAME은 TB_NAME1, 다음 반복문에서는 TB_NAME2
		var PATTERN_NAME = row[1].split('PATTERN_NAME=').join(''); // 변수 CHK Y, 다음 반복문에서는 N
		var data_id = PATTERN_NAME.split('=')[1];
		
		colNames.push(ID.split('=')[1]);
	}
	
	for(var i = 0; pattern.length > i; i++){ // str 배열만큼 for돌림
		
		var row = pattern[i].split(', ');
		var ID = row[0].split('ID=').join(''); //변수 TABLE_NAME은 TB_NAME1, 다음 반복문에서는 TB_NAME2
		var PATTERN_NAME = row[1].split('PATTERN_NAME=').join(''); // 변수 CHK Y, 다음 반복문에서는 N
		var data_id = PATTERN_NAME.split('=')[1];
		
		colModel.push({index: data_id,           name: data_id,          width: 100,  align: 'center',  formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: '0'}});
	}
	

	$("#targetGrid").jqGrid({
		//url: 'data.json',
		datatype: "local", 
	   	mtype : "POST",
	   	ajaxGridOptions : {
			type    : "POST",
			async   : true
		},
		colNames:colNames,
		colModel:colModel,
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: gridWidth,
		height: gridHeight,
		loadonce: true, // this is just for the demo
	   	autowidth: true,
		shrinkToFit: false,
		rownumbers : false, // 행번호 표시여부
		rownumWidth : 30, // 행번호 열의 너비	
		rowNum:30,
	   	rowList:[30,50,100],			
		pager: "#targetGridPager",
		//jqgrid의 특성상 - rowpos의 이벤트는 onSelectCell, beforeEditCell 다 해주어야 함
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	
	  	},
		loadComplete: function(data) {
		
			$(".gridSubSelBtn").on("click", function(e) {
		  		e.stopPropagation();
		  		/* var rowid = event.target.parentElement.parentElement.id;
		  		
		  		var group_id = data[(rowid-1)];
		  		console.log(group_id); */
		  		
				$("#targetGrid").setSelection(event.target.parentElement.parentElement.id);
		  		var type = $("#targetGrid").getCell(event.target.parentElement.parentElement.id, 'TYPE');
				// 조건에 따라 Option 변경
				// var status = ".status-" + $("#targetGrid").getCell(event.target.parentElement.parentElement.id, 'SCHEDULE_STAT');
				$(".status").css("display", "none"); 
				$(".status").css("display", "block");
				$(".manage-schedule").css("display", "block");
				
				/* if(data[(rowid-1)].TYPE == 0){
					$(".schmanager").css("display", "block");
				} else {
					$(".schmanager").css("display", "none"); 
				} */
				if(type == '서버'){
					$(".schmanager").css("display", "block");
				} else {
					$(".schmanager").css("display", "none"); 
				}

				var offset = $(this).parent().offset();
				$("#taskGroupWindow").css("left", (offset.left - $("#taskGroupWindow").width()) + 55 + "px");
				// $("#taskWindow").css("left", (offset.left - $("#taskWindow").width() + $(this).parent().width()) + "px");
				$("#taskGroupWindow").css("top", offset.top + $(this).height() + "px");

				var bottomLimit = $(".left_box2").offset().top + $(".left_box2").height();
				var taskBottom = Number($("#taskGroupWindow").css("top").replace("px","")) + $("#taskGroupWindow").height();

				if (taskBottom > bottomLimit) { 
					$("#taskGroupWindow").css("top", Number($("#taskGroupWindow").css("top").replace("px","")) - (taskBottom - bottomLimit) + "px");
				}
				$("#taskGroupWindow").show();
			});
			//setScanStatus();
			
			var name = $("#targetGrid").find("[aria-describedby='targetGrid_NAME']");
			
			name.css("text-decoration", "underline");
			name.mouseover(function(e){
				$(this).css("text-decoration", "underline");
				$(this).css("font-weight", "bold");
				$(this).css("cursor", "pointer");
			});
			name.mouseleave(function(e){
				$(this).css("text-decoration", "underline");
				$(this).css("font-weight", "normal");
			});
		
			
			/* var schedule_name = $("#targetGrid").find("[aria-describedby='targetGrid_SCHEDULE_GROUP_NAME']");
			schedule_name.css("text-decoration", "underline");
			schedule_name.mouseover(function(e){
				$(this).css("text-decoration", "underline");
				$(this).css("font-weight", "bold");
				$(this).css("cursor", "pointer");
			});
			schedule_name.mouseleave(function(e){
				$(this).css("text-decoration", "underline");
				$(this).css("font-weight", "normal");
			}); */
			
			$("#targetGrid").find("[aria-describedby='targetGrid_SCHEDULE_GROUP_NAME']").each(function(){
				 var schedule_name = $(this);
				 if(schedule_name.text() === 'Recon Scan'){
					
				 }else {
					 schedule_name.css("text-decoration", "underline");
					 schedule_name.mouseover(function(e){
						$(this).css("text-decoration", "underline");
						$(this).css("font-weight", "bold");
						$(this).css("cursor", "pointer");
					});
					schedule_name.mouseleave(function(e){
						$(this).css("text-decoration", "underline");
						$(this).css("font-weight", "normal");
					});
				 }
			});
			
			$(".scanning_href").on("click", function(e) {
				e.stopPropagation();
		  		
				$("#targetGrid").setSelection(event.target.parentElement.parentElement.id);
				
				var ap_no = $("#targetGrid").getCell(event.target.parentElement.parentElement.id, 'AP_NO');
				var target_id = $("#targetGrid").getCell(event.target.parentElement.parentElement.id, 'TARGET_ID');
				var id = $("#targetGrid").getCell(event.target.parentElement.parentElement.id, 'SCHEDULE_ID');
				var status = $("#targetGrid").getCell(event.target.parentElement.parentElement.id, 'SCHEDULE_STATUS2');
				
				var rowData = $("#targetGrid").getRowData(event.target.parentElement.parentElement.id);
				var postData = {
					id : id,
					ap_no : ap_no,
				};
				
				$.ajax({
					type: "POST",
					url: "/search/getScanDetails",
					async : false,
					data : postData,
				    success: function (resultMap) {
				    	
				    	if(resultMap.resultCode == 0) {
					    	$.each(resultMap.resultData, function(index, item){
					    		var detailsContent = item.status + "(" + item.percentage + " 'File path";
					    			detailsContent += item.currentlyFile + ")";
					    		
					    		$("#scanningDetailsName").css("display", "block");
					    		$("#scanningDetailsName").html(item.name);
					    		$("#scanningDetails").html(detailsContent);
					    	});
				    	}else {
				    		$("#scanningDetailsName").css("display", "none");
				    		$("#scanningDetails").html("스캔 준비 중 입니다. <br /> 잠시만 기다려 주시면 곧 스캔이 시작됩니다.");
				    	}
				        
				    },
				    error: function (request, status, error) {
						alert("ERROR");
				        console.log("ERROR : ", error);
				    }
				});			
				
				$("#taskTargetid").val(target_id);
				$("#taskApno").val(ap_no);
				$("#taskScheduleid").val(id);
				
				var offset = $(this).parent().offset();
				$("#progressDetails").css("left", "645px");
				$("#progressDetails").css("top", offset.top + $(this).height() + 16 + "px");

				var bottomLimit = $(".left_box2").offset().top + $(".left_box2").height();
				var taskBottom = Number($("#progressDetails").css("top").replace("px","")) + $("#progressDetails").height();

				if (taskBottom > bottomLimit) { 
					$("#progressDetails").css("top", Number($("#progressDetails").css("top").replace("px","")) - (taskBottom - bottomLimit) + "px");
				} 
				$("#progressDetails").show();
				
			});
	    },
	    gridComplete : function() {},
	    onCellSelect : function(rowid, icol, cellcontent, e) {
	    	if(icol == 3 ){
	    		var target_id = $(this).getCell(rowid, 'TARGET_ID');
	    		var ap_no = $(this).getCell(rowid, 'AP_NO');
	    		var host =  $(this).getCell(rowid, 'NAME');
	    		var host =  $(this).getCell(rowid, 'NAME');
	    		
	    		var form = document.createElement("form");
           	 	form.setAttribute("charset", "UTF-8");
                form.setAttribute("method", "POST"); 
                form.setAttribute("action", "/manage/pi_detection_list"); 
                
                var hiddenField = document.createElement("input");

                hiddenField.setAttribute("type", "hidden");
                hiddenField.setAttribute("name", "targetid");
                hiddenField.setAttribute("value", target_id);
                form.appendChild(hiddenField);
                
                hiddenField = document.createElement("input");
                hiddenField.setAttribute("type", "hidden");
                hiddenField.setAttribute("name", "apno");
                hiddenField.setAttribute("value", ap_no);
                form.appendChild(hiddenField);
                
                hiddenField = document.createElement("input");
                hiddenField.setAttribute("type", "hidden");
                hiddenField.setAttribute("name", "host");
                hiddenField.setAttribute("value", host);
                form.appendChild(hiddenField);

                document.body.appendChild(form);
                form.submit();
	    	
	    	}
	    	if(icol != 2 ){ 
	    		$("#viewDetails").hide();
			  	$("#taskWindow").hide();
	    		return;
	    	}
	    	var p_id = $(this).getCell(rowid, 'POLICY_ID');
	    	
	    	if(p_id == null ||  p_id == ""){
	    		return;
	    	}
	    	
	    	
	    	var group_id = $(this).getCell(rowid, 'SCHEDULE_GROUP_ID');
	    	var group_name = $(this).getCell(rowid, 'SCHEDULE_GROUP_NAME');
            var policyid = $(this).getCell(rowid, 'POLICY_ID');
            
			var postData = {
		  		policyid : policyid,
				scheduleUse : 1
			};
		  	
		  	$.ajax({
				type: "POST",
				url: "/search/getPolicy",
				async : false,
				data : postData,
			    success: function (result) {
				   	/* var start_dtm = setStartdtm(result[0]);
					$('#search_start_time').html(start_dtm); */
				   	
				 	// 개인정보 유형 
					var datatype = setDatatype(result[0]);
					$('#details_label').html(group_name);
					$('#datatype_area').html(datatype);
					
					$("#details_datatype").html(result[0].TYPE);
				   	
				   	var cycle = setCycle(result[0]);
					$('#cycle').html(cycle);
			    },
			    error: function (request, status, error) {
			    	alert("정책 정보를 갖고 올수 없습니다.");
			        console.log("ERROR : ", error);
			    }
		  	});
		  		
		  	$("#viewDetails").show();
		  	
	    }
	});
	
}
function fn_search(){
	var searchType = ['scheduled'];
	
	if($("#fromDate").val() > $("#toDate").val()){
		alert("입력한 끝 날짜가 시작 날짜 보다 빠릅니다.");
		return;
	}

	var postData = {
		fromDate : $("#fromDate").val(),
		toDate : $("#toDate").val(),
		title : $("#title").val(), /*스케줄명*/
		host : $("#hostname").val(), /* 호슽명 */
		path : $("#path").val(), /* 경로 */
		sch_type : $("#sch_type").val(),
		status_flag : $("#statusFlag").val(), /* 상태 */
		searchType : JSON.stringify(searchType)
	};

	$("#targetGrid").setGridParam({url:"<%=request.getContextPath()%>/search/registDaySchedule", postData : postData, datatype:"json" }).trigger("reloadGrid");
}

var formatScheduleStatus = function(cellvalue, options, rowObject) {
	var status = cellvalue;
	if(cellvalue == 'completed'){
		status = '완료';
/* 		status = '취소'; */
	} else if(cellvalue == 'cancelled'){
		status = '취소';
	}else if(cellvalue == 'failed'){
		status = '실패';
	}else if(cellvalue == 'scanning'){
		status = '<label class=\"scanning_href\" style=\"text-decoration: underline; cursor:pointer;\">진행중</label>';
	}else if(cellvalue == 'scheduled'){
		status = '대기';
	}else if(cellvalue == 'stopped'){
		status = '중지';
	}else if(cellvalue == 'paused'){
		status = '일시정지';
	}else if(cellvalue == 'stalled'){
		status = '멈춤';
	}else if(cellvalue == 'deactivated'){
		status = '비활성';
	}else if(cellvalue == 'deactivated'){
		status = '비활성';
	}else if(cellvalue == null){
		status = '';
	}
	
	return status; 
};
function backGroundCss (rowId, tv, rowObject, cm, rdata) {
	return 'style="background-color: #f9f9f9;"'
}

</script>


</body>
</html>