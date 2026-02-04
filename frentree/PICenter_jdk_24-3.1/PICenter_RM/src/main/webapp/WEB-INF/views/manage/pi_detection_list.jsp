<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:useBean id="nowDate" class="java.util.Date" />
<%@ include file="../../include/header.jsp"%> 
<%@ include file="../../views/manage/pi_detection_popUp.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/detailReport.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/xlsx.full.min.js"></script>
<style>
	.DeletionRegistT tr td{
		height: 20px;
		color: #9E9E9E;
		padding: 0 0 0 2px;
	}
	.detectionBtn{
		margin-top: 4px;
		color: #ffffff;
		background: #2f353a;
	}
	input {
		height: 27px;
	}
	#addProcessing *{
		margin-top: 4px;
	}
	#title_process_true span, #title_process_false span{
		font-size: 12px;
		color: #9E9E9E;
		padding-left: 5px;
	}
	.true_processing_comment span, .false_processing_comment span{
		font-size: 12px;
		color: #9E9E9E;
	}
	.true_processing_hint, .false_processing_hint{
		display: inline-block;
		position: relative;
		top: 2px;
	}
	.ui-jqgrid tr.ui-row-ltr td{
		cursor: pointer;
	}
	#approvalStatusNm{
		text-decoration: underline;
	}
	#approvalStatusNm:hover{
		font-weight: bold;
		cursor: pointer;
	}
	#path_detail_true::placeholder, #path_detail_false::placeholder{
		color: #9E9E9E;
	}
	#userHelpImg{
		background-size: auto;
	}
    @media screen and (-ms-high-contrast: active) , ( -ms-high-contrast : none) {
		/* .host_comment{
			position: absolute !important;
			top: 92px !important;
			left: auto !important;
		} */
		.path_comment_div{
			position: relative;
			display: inline-block;
			bottom: 12px !important;
		}
		.path_comment{
			top: 0 !important;
		}
	}
</style>
<!-- 검출관리 -->

<!-- section -->
<section>
    <!-- container -->
    <div class="container">
        <!-- content -->   
         <h3 class="detection_list_title" style="display: inline; top: 25px;">결과조회/조치계획</h3>
        <p class="detection_list_txt" style="position: absolute; top: 33px;  font-size: 12px; color: #9E9E9E;">
       		개인정보 검출 결과를 확인 하실수 있습니다. 대상을 선택 후 검출 내역을 확인하여 정탐,오탐을 선택 하시기 바랍니다.
       	</p>
        
        <div class="content magin_t35">
            <div class="grid_top" style="padding-left: 335px;"> 
                <table class="user_info" id="sch_detail" style="display: inline-table; width: 745px;">
                    <caption>검색 결과 조회</caption>
                    <tbody>
                        <tr>
                            <th style="text-align: center; width:100px; border-radius: 0.25rem;">상세조회</th>
                            <td>
                                <input type="text" id="searchLocation" value="" class="edt_sch" style="width: 254px; height: 26.5px; padding-left:5px;" placeholder="대상을 지정 후 경로 또는 파일명을 입력하세요.">
                                <input type="hidden" id="hostSelect" value="">
                            	<input type="hidden" id="ap_no" value="">
                            	<input type="hidden" id="onedriveChk" value="">
                            </td>
                            <th class="sch_flag grade_hidden" style="text-align: center; width:100px; border-radius: 0.25rem;">처리상태</th>
                            <td class="sch_flag grade_hidden">
                            	<select id="sch_processingFlag" name="sch_processingFlag" style="width: 80px; font-size: 12px; padding-left: 5px;">
                            		<option value="">전체</option> 
                                    <option value="0" >처리</option>
                                    <option value="1" selected>미처리</option>
                            	</select>
                            </td>
                            <th class="sch_flag grade_hidden" style="text-align: center; width:100px; border-radius: 0.25rem;">개인정보유형</th>
                            <td class="sch_flag grade_hidden">
                            	<select id="sch_patternFlag" name="sch_patternFlag" style="width: 123px; font-size: 12px; padding-left: 5px;">
<%--                             <c:forEach var="set" items="${setMap}"> --%>
                            		<option value="all">전체</option>
                            	<c:forEach var="pattern" items="${pattern}">
                                    <option value="${pattern.ID}" >${pattern.PATTERN_NAME}</option>
                            	</c:forEach>
                            	</select>
                            </td>
                           	<td>
                           		<input type="button" name="button" class="btn_route" id="btnSearch">
                           	</td>
                        </tr>
                    </tbody>
                </table>
                <div class="list_sch">
                    <div class="sch_area" id="sch_area" style="margin-top: 7px;">
	                    <c:forEach var="set" items="${setMap}">
							<c:if test="${set.NAME == 'update_account' && set.STATUS == 'Y'}">
		         	            <label class="DeletionRegistBtn">  
		         	            	<button type="button" class="btn_down detectionBtn" id="accountRadio">담당자 변경</button>
		         	            </label>
							</c:if> 
							<c:if test="${set.NAME == 't_processing' && set.STATUS == 'Y'}">
		         	            <label class="DeletionRegistBtn">
		         	            	<button type="button" class="btn_down detectionBtn" id="answerRadio" style="background: #0070C0;"> 정탐</button>
		         	            </label>
							</c:if> 
							<c:if test="${set.NAME == 'f_processing' && set.STATUS == 'Y'}">
		                        <label class="DeletionRegistBtn">
		                        	<button type="button" class="btn_down detectionBtn" id="wrongRadio" style="background: #f02533;"> 오탐</button>
		                        </label>
							</c:if>
							<c:if test="${set.NAME == 'exception' && set.STATUS == 'Y'}">
		                        <label class="DeletionRegistBtn"> 
		                        	<button type="button" class="btn_down detectionBtn" id="exceptionRadio" > 경로 예외 </button>
		                        </label>
							</c:if> 
							<c:if test="${set.NAME == 'removal' && set.STATUS == 'Y'}">
								<label class="DeletionRegistBtn">  
									<button type="button" class="btn_down detectionBtn" id="btnRemoval"> 삭제 </button>
		                        </label>
							</c:if> 
							<c:if test="${set.NAME == 'enciphering' && set.STATUS == 'Y'}">
								<label class="DeletionRegistBtn">  
									<button type="button" class="btn_down detectionBtn" id="btnEnciphering" > 암호화 </button>
		                        </label>
							</c:if>
							<c:if test="${set.NAME == 'quarantine' && set.STATUS == 'Y'}">
								<label class="QuarantineRegistBtn">  
									<button type="button" class="btn_down detectionBtn" id="btnQuarantine" > 파일이동 </button>
		                        </label>
							</c:if>
						</c:forEach>
                        <input type="hidden" id="selectedDate" class="DeletionRegistBtn" value="">
                        <input type="hidden" id="group_id" class="DeletionRegistBtn" value="">
<!--                         <select id="sch_patternFlag" name="sch_patternFlag" style="width: 186px; font-size: 12px; padding-left: 5px;"> -->
                        <!-- 다운로드 기능 주석처리
                        <select id="btnDownloadExcel" name="btnDownloadExcel" style="width: 111px; font-size: 12px; margin-top:6px; padding-left: 5px;">
                        	<option value="simple">단건 다운로드</option>
                        	<option value="detail">상세 다운로드</option>
                        </select>
						<button type="button" name="button" class="btn_down" id="btnDownloadExcelBtn" style="margin-top: 6px;">다운로드</button>
						-->
                    </div> 
                </div>
            </div>
            <div class="left_area2" style="position: absolute; top: 75px; height: 90%;">
            <table class="user_info narrowTable" style="width: 320px;">
				<tbody>
					<tr>
						<th style="text-align: center; border-radius: 0.25rem;">대상조회</th>
             			     <td>
                				 <input type="text" style="width: 205px; padding-left: 5px;" size="10" id="targetSearch" placeholder="호스트명을 입력하세요.">
                			 </td>
                		<td>
                    		 <input type="button" name="button" class="btn_look_approval" id="btn_sch_target" style="margin-top: 5px;">
                    	</td>
					</tr>
				</tbody>
			</table>
				<div class="left_box2" style="max-height: 680px;">
   					<div id="jstree" class="select_location" style="overflow-y: auto; overflow-x: auto; height: 659px; margin-top: 11px; background: #ffffff; border: 1px solid #c8ced3; white-space:nowrap;">
						
					</div>
				</div>
			</div>
            <div class="grid_top_Server" style="overflow: hidden; margin-left: 335px; height: 660px; max-height: 660px; margin-top: 10px;">
               <table id="targetGrid"></table>
               <div id="targetGridPager"></div>
            </div>
        </div>
    </div>
    <!-- container -->
</section>
<!-- section -->


<!-- 팝업창 시작 : 오탐 처리 -->

<!-- 팝업창 종료 -->

<!-- 팝업창 시작 정탐-->
<div id="resultTrue" class="popup_layer" style="display:none;">
    <div class="popup_box" style=" width: 350px; margin-left: -180px; height: 340px; top: 60%; padding: 10px; background: #f9f9f9;">
        <div class="popup_top" style="background: #f9f9f9;">
            <h1 style="color: #222; box-shadow: none; padding: 0;">정탐조치</h1>
        </div>
        <div class="popup_content" style="height: 200px;">
            <div class="content-box" style="height: 190px; background: #fff; border: 1px solid #c8ced3;">
                <input type="radio" name="processing_flag" value=1 class="edt_sch" style="border: 0px solid #cdcdcd;">&nbsp;삭제<br/>
                
                <input type="radio" name="processing_flag" value=2 id="processingCheck2" class="edt_sch" style="border: 0px solid #cdcdcd;">&nbsp;법제도 
                <input type="text"  class="edt_sch" id="legalSystem" style="margin-left: 21px;"><br/>
                
                <input type="radio" name="processing_flag" value=3 id="processingCheck3" class="edt_sch" style="border: 0px solid #cdcdcd;">&nbsp;삭제 주기 &nbsp;
                <input type="text"  class="edt_sch" id="deleteCycle"><br/>
                             삭제예정일 : <input type="date" id="selectDateTrue" value="${befDate}" style="text-align: center; margin-left: 12px;" readonly="readonly"> 
            </div>
        </div>
        <div class="popup_btn">
            <div class="btn_area" style="padding-right: 0;">
                <button type="button" id="btnResultTrueSave">확인</button>
                <button type="button" id="btnResultTrueCancel">취소</button>
            </div>
        </div>
    </div>
</div>
<!-- 팝업창 종료 -->

<div id="detailExceldownloadPop" class="popup_layer" style="display:none;">
	    <div class="popup_box" style="height: 200px; padding: 10px; background: #f9f9f9; left: 50%; top: 55%;">
	        <div class="popup_top" style="background: #f9f9f9;">
	            <h1 style="color: #222; padding: 0; box-shadow: none;">상세 보고서 다운로드</h1>
	            <p style="position: absolute; top: 11px; right:191px;  font-size: 12px; color: #9E9E9E;">
		       		상세보고서의 경우 "미처리" 항목만 출력됩니다.
		       	</p>
	        </div>  
	        <div class="popup_content">
		        <div class="content-box" style="height: 355px; background: #fff; padding: 0;">
		        	<table class="popup_tbl">  
		        		<colgroup>
							<col width="20%"> 
							<col width="*">
						</colgroup>
		        		<tbody>
		        			<tr>
			        			<th>파일명</sth>
		        				<td>
		        					<input type="text" id="report_file_name" name="report_file_name" style="width:250px;" placeholder=" 파일명 미 기입시 기본 파일명으로 제공됩니다.">
		        				</td>
		        			</tr> 
		        			<tr>
			        			<th>기간</sth>
		        				<td>
									<input type="date" id="fromDate" style="text-align: center;  width:175px;" readonly="readonly" value="${fromDate}" >
									<span style="width: 10%; margin-right: 3px; color: #000">~</span>
									<input type="date" id="toDate" style="text-align: center;  width:175px;" readonly="readonly" value="${toDate}" >
		        				</td>
		        			</tr>
		        		</tbody>
		        	</table> 
		        	<table class="popup_tbl" style="padding-left: 9%;"> 
		        		<colgroup>
							<col width="20%">
							<col width="*"> 
						</colgroup> 
		        		<tbody style="height: 91px; display: block;">
		        			<tr><th rowspan="3" style="vertical-align:top; padding-top: 15px;"> 구분</th></tr>
		        			<tr><td colspan="2"><input type="checkbox" class="detailReportCBox" id="pattern_con" value="pattern_con" name="detailReportType" checked> 패턴</td></tr>
		        			<tr><td colspan="2"><input type="checkbox" class="detailReportCBox" id="pattern_cnt" value="pattern_cnt" name="detailReportType" checked> 건수</td></tr>
		        		</tbody>
		        	</table>
		        	<table class="popup_tbl" style="padding-left: 7%;">
		        		<colgroup>
							<col width="20%">
							<col width="*">
						</colgroup>
		        		<tbody id="reportExtensiontd" style="overflow-y: scroll; height: 212px; display: block;">
		        			
		        		</tbody>
		        	</table>
		        </div>
	        
	        </div>
	        <div class="popup_btn">
	            <div class="btn_area">
<!-- 	                <button type="button" id="detailExceldownload">다운로드</button> -->
	                <button type="button" id="detailExceldownloadCanCel">취소</button>
	            </div>
	        </div>
	    </div>
	</div>

<!-- 팝업창 시작 오탐-->
<div id="resultFalse" class="popup_layer" style="display:none;">
    <div class="popup_box" style=" width: 350px; margin-left: -180px; height: 340px; top: 60%; padding: 10px; background: #f9f9f9;">
        <div class="popup_top" style="background: #f9f9f9;">
            <h1 style="color: #222; box-shadow: none; padding: 0;">오탐조치</h1>
        </div>
        <div class="popup_content" style="height: 200px;">
            <div class="content-box" style="height: 190px; background: #fff; border: 1px solid #c8ced3;">
                <input type="radio" name="processing_flag" value=4 class="edt_sch" style="border: 0px solid #cdcdcd;">&nbsp;시스템 파일<br/>
                <input type="radio" name="processing_flag" value=5 class="edt_sch" style="border: 0px solid #cdcdcd;">&nbsp;기타<br/>
            </div>
        </div>
        <div class="popup_btn">
            <div class="btn_area" style="padding-right: 0;">
                <button type="button" id="btnResultFalseSave">확인</button>
                <button type="button" id="btnResultFalseCancel">취소</button>
            </div>
        </div>
    </div>
</div>
<!-- 팝업창 종료 -->

<!-- 팝업창 시작 정탐/오탐 신청 내역-->
<div id="insertPathExcepPopup" class="popup_layer" style="display:none">
	<div class="popup_box" style="height: 570px; top: 52%; left: 50%; padding: 10px; background: #f9f9f9;">
	<img class="CancleImg" id="btnCancleInsertPathExcepPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 id="groupName" style="color: #222; padding: 0; box-shadow: none;"></h1>
		</div> 
		<div class="popup_content">
			<div class="content-box" style="height: 500px; background: #fff; border: 1px solid #c8ced3;">
				<!-- <h2>세부사항</h2>  -->
				<table class="popup_tbl">
					<colgroup>
						<col width="130">
						<col width="*">
					</colgroup>
					<tbody> 
						<tr>
							<th style="border-bottom: 1px solid #cdcdcd;">이름</th>
							<td style="border-bottom: 1px solid #cdcdcd;">
								<div style="overflow-y: auto; height: 280px;">
									<table style="border: 0px solid #cdcdcd; width: 430px; height: 266px; margin-top: 5px; margin-bottom: 5px; resize: none; " >
									<tbody>
										<tr id="excepPath" style="border:none;">
										</tr>
									</tbody>
									</table>
								</div>
							</td>
						</tr>
						<tr>
							<th style="border-bottom: 1px solid #cdcdcd;">판단근거</th>
							<td style="border-bottom: 1px solid #cdcdcd;">
								<table style="border: 0px solid #cdcdcd; width: 430px; height: 90px; margin-top: 5px; margin-bottom: 5px; resize: none; " >
								<tbody>
									<tr id="BasisName" style="border:none;">
									</tr>
								</tbody>
								</table>
							</td>
						</tr>
						<tr>
							<th style="border-bottom: 1px solid #cdcdcd;">사유</th>
							<td style="border-bottom: 1px solid #cdcdcd;">
								<input type="text" name="trueFalseChk" id="reason" value="" class="edt_sch" style=" border: 0px solid #cdcdcd;" readonly>
							</td>  
						</tr>
						<tr>
							<th style="border-bottom: 1px solid #cdcdcd;">등록서버</th>
							<td style="border-bottom: 1px solid #cdcdcd;">
								<input type="text" id="regisServer" value="" class="edt_sch" style=" border: 0px solid #cdcdcd;" readonly>
							</td>  
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area" style="padding: 10px 0; margin: 0;">
				<button type="button" id="btnCheck">확인</button>
			</div>
		</div>
	</div>
</div>

<div id="ExcelDownPopUp" class="popup_layer" style="display:none">
      <div class="popup_box" style="height: 145px;  width: 497px; padding: 10px; background: #f9f9f9; top: 72%; left: 53%;">
      <img class="CancleImg" id="btnCancleExcelDownPopUp" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
          <div class="popup_top" style="background: #f9f9f9;">
              <h1 style="color: #222; padding: 0; box-shadow: none;" id="excelDownName">상세 보고서 다운로드 안내</h1>
          </div>
          <div class="popup_content">
              <div class="content-box" style="height: 69px; background: #fff; border: 1px solid #c8ced3; font-size: 13px; padding-top: 14px;">
                  백그라운드에서 파일 생성이 진행 중입니다.<br>
                  <span style="color: #1E4C81; font-weight: bold;">다운로드 대기열</span>에서 파일 생성 상태 및 완료된 파일은 다운로드 받을 수 있습니다.
              </div>
          </div>
          <div class="popup_btn">
              <div class="btn_area">
                  <button type="button" id="btnExcelDownPopUpClose">닫기</button>
              </div>
          </div>
      </div>
  </div>

<!-- 팝업창 종료  -->
<!-- 경로 예외 팝업 시작 -->

<!-- 팝업창 - 비밀번호 입력  -->
<%@ include file="../../include/footer.jsp"%>
<script type="text/javascript"> 
var idxList = null;
var pattern = '${pattern}'.split('[{').join('').split('}]').join('');
pattern = pattern.split('}, {');
$(function() {
	var grade = ${memberInfo.USER_GRADE};
		$('#jstree').jstree({
			// List of active plugins
			"core" : {
			    "animation" : 0,
			    "check_callback" : true,
				"themes" : { "stripes" : false },
				"data" : ${userGroupList}
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
	    .bind('select_node.jstree', function(evt, data, x) {
	    	$(".DeletionRegistBtn").show();
    		$("#targetGrid").clearGridData();
    		$(".grid_top_PC").css("display", "none");
    		$(".grid_top_Server").css("display", "");
    		$("#userHelpIcon").css("display", "");
    		$("#userHelpPCIcon").css("display", "none");
    		$(".sch_flag").css("display", "");
    		
    		
    		console.log(data.node.data); 
    		if(data.node.data == null){
    			alert("최상위 그룹은 선택할 수 없습니다."); 
    			return true;
    		}
    		var type = data.node.data.type;
    		if(type > 90 || type==0){ // 그룹 선택 시
	    		var parent = data.node.parent;
	    		if(parent == "#") {
	    			alert("최상위 그룹은 선택할 수 없습니다.");
	    			return;
	    		}
	    		
    			$("#targetGrid").clearGridData();
	    		var type = data.node.data.type;
	    		var id = data.node.data.target_id;
		    	var ap = data.node.data.ap;
		    	var name = data.node.text;
		    	var parents = data.node.parents;
		    	var children_d = data.node.children_d;
		    	var child_list = [];
		    	
		    	for(var i=0;i<children_d.length;i++){
		    		if(children_d[i].indexOf('_')==-1){
		    			child_list.push(children_d[i])  
		    		} else {
		    			child_list.push(children_d[i].split('_')[2])
		    			};
		    	}
		    	
		    	var postDt = {};
		    	postDt["idxList"] = child_list;
		    	
		    	var oJson = JSON.stringify(postDt);
		    	
		    	var url = "${getContextPath}/manage/selectGroupFindSubpath";
		    	
		    	console.log("parents",  parents.includes('picenter')); 
		    	
		    	if(parents.includes('picenter')){
		    		url = "${getContextPath}/manage/selectPICenterGroupFindSubpath";
		    	}
		    	
		    	$.ajax({
			        type: "POST",
			        url: url,
			        async : true,
			        data : oJson,
			        contentType: 'application/json; charset=UTF-8',
			        success: function (result) {
			        	idxList = result;
			        	fnSearchFindSubpath(2, result)
			        },
			        error: function (request, status, error) {
			        	console.log(status);
			            alert("처리 등록을 실패 하였습니다.");
			
			            return;
			        }
			    });
		    	
		    	$("#targetSearch").val(name);
	    		$("#targetSearch").text(name);
		    	  
	    	}else if(type == 1 || type ==3){
	    		
	    		$("#targetGrid").clearGridData();
	    		idxList = null;
	    		var type = data.node.data.type;
	    		var id = data.node.data.target_id;
		    	var ap = data.node.data.ap;
		    	var name = data.node.data.name;
		    	var parents = data.node.parents;
		    	var checkPc = false;
	    		
	    		var id = id;
	    		var name =  data.node.text;
	    		var ap_no = ap;  
	    		        
	    		if(data.node.parent == "onedirve"){
	    			$("#onedriveChk").val(1);
	    		} else {
	    			$("#onedriveChk").val(0);
	    		}
	    		
	    		$("#targetSearch").val(name);
	    		$("#targetSearch").text(name);
	    		$("#hostSelect").val(id);
	    		$("#ap_no").val(ap_no);
	    		
	    		findByPop();
	    		
	    	}
	    }).bind("loaded.jstree", function(e, data) {
	    	var nodes = $('#jstree').jstree(true).get_json('#', { 'flat': true });
	    	
	    	var textMap = new Map();
	   	    nodes.forEach(function(node) {
	   	        // 대소문자 무시 및 양쪽 공백 제거하여 키 생성
	   	        var key = node.text.trim().toLowerCase();
	   	        if (!textMap.has(key)) {
	   	            textMap.set(key, node.text);
	   	        }
	   	    });

	   	    // Map 객체의 값들로 배열을 만들고 정렬
	   	    var selectColData = Array.from(textMap.values());
	        selectColData.sort(); 
	           
	    	$("#targetSearch"). autocomplete({
	    		source : selectColData,
	    		select : function(event, ui){
	    		},
	    		focus : function(event, ui){ 
	    			 $(".ui-menu-item").removeClass("pic-grid-focus");
	    			 
	    			 $(".ui-menu-item").each(function() {
	    				 $(".ui-menu-item").removeClass("pic-grid-focus ui-state-active");
	    				 $(".ui-menu-item-wrapper:contains('" + ui.item.label + "')").closest(".ui-menu-item").addClass("pic-grid-focus");
	    				 $(".ui-menu-item-wrapper").removeClass("ui-state-active"); 
	    		        });
	    			return false;
	    		}
		    }).on("changed.jstree", function (e, data) { });
	    	
	    });
		
		$("#detailExceldownloadCanCel").on("click", function(){
			$("#report_flag").val("xlsx");
			$("#report_file_name").val("");
			$("#detailExceldownloadPop").hide();
		});
		
		/*암호화/삭제*/
		$("#btnRemoval").click(function(){
			fnRegist("delete");
		}); 
		$("#btnEnciphering").click(function(){
			fnRegist("encrypt");
		}); 
		$("#btnQuarantine").click(function(){
			console.log("btnQuarantinebtnQuarantinebtnQuarantine");
			fnRegist("quarantine");
		}); 
		$("#btnEncryptPassword").click(function(){
			fn_registRemdiation("encrypt");
		});
		$("#btnEncryptPasswordCancel, #btnCancleEncryptPasswordPopup").click(function(){
			$("#encryptPasswordPopup").hide();
			$("#encryptPassword").val('');
			$("#encryptPasswordConfirm").val('');
			$("#quarantinePopup").hide();
			$("#quarantinePath").val('');
		});
		
		$("#btnQuarantineSave").click(function(){
			fn_registRemdiation("quarantine");
		});
		$("#btnQuarantineCancel").click(function(){
			$("#quarantinePopup").hide();
			$("#quarantinePath").val('');
		});
		

		
	    $("#encryptPassword").keyup(function(e){
	        if (e.keyCode == 13) {
	        	$("#encryptPasswordConfirm").focus();
	        }
	    });
	    $("#encryptPasswordConfirm").keyup(function(e){
	        if (e.keyCode == 13) {
	        	$("#btnEncryptPassword").click();
	        }
	    });
		
	
});

var resetFomatter = null;
var SEQ = null;
var approval_status = null;

$("#btnExcelDownPopUpClose").on("click", function(){
	$("#ExcelDownPopUp").hide();
}); 
$("#btnCancleExcelDownPopUp").on("click", function(){
	$("#ExcelDownPopUp").hide();
}); 

//검출 리스트 그리드 CheckBox 생성
function createCheckbox(cellvalue, options, rowObject) {
    var value = rowObject['HASH_ID'];
    var str = ''; 
    var rowId = options['rowId'];
 
    if (rowObject['LEVEL'] == "0") 
    	return "<input id=\"gridChk_"+rowId+"\" type=\"checkbox\" name=\"gridChk\" value="+ rowId +" data-rowid=" + rowId + " onchange=\"gridClick(event, "+rowId+")\" checked>";
    else 
    	return "<input id=\"gridChk_"+rowId+"\" type=\"checkbox\" name=\"gridChk\" value="+ rowId +" data-rowid=" + rowId + " onchange=\"gridClick(event, "+rowId+")\">";
}
 
String.prototype.replaceAll = function(org, dest){
	return this.split(org).join(dest);
}

function fnSearchFindSubpath(status, result) 
{
	$("#targetGrid").clearGridData();
    var oPostDt = {};
    oPostDt["target_id"] = $("#hostSelect").val();
    oPostDt["ap_no"]	 = $("#ap_no").val();
    oPostDt["findData"]  = status;
    oPostDt["targetList"]  = JSON.stringify(result);
    oPostDt["location"]  = $("#searchLocation").val();
    oPostDt["onedriveChk"]	 = $("#onedriveChk").val();
    oPostDt["name"]  = $("#targetSearch").val();
    oPostDt["text"]  = $("#targetSearch").text();
    oPostDt["processingFlag"]	 = $("select[name='sch_processingFlag']").val();
    oPostDt["patternFlag"]	 = $("select[name='sch_patternFlag']").val();
    
    $('#allChkTargetGrid').prop('checked', false); 

    $("#targetGrid").setGridParam({
        url: "${getContextPath}/manage/selectFindSubpath", 
        postData: oPostDt, 
        datatype: "json",
        treedatatype: 'json'
    }).trigger("reloadGrid");
}

function loadTargetGrid()
{
	var patternCnt = '${patternCnt}';
	var colNames = [];
	var colModel = [];
	
	var gridChk = "<input type='checkbox' id='allChkTargetGrid' name='allChkTargetGrid' style='margin:-5px 0 0 4px;' onclick='fn_allChkTargetGrid(this, event)' /> ";
	/* gird 데이터*/
	colModel.push({ label:gridChk,  	index: 'CHK',		 					name: 'CHK', 							width: 35, 		align: 'center', editable: true, edittype: 'checkbox', 
		classes: 'pointer', editoptions: { value: "1:0" }, formatoptions: { disabled: false }, sortable: false, formatter: createCheckbox, exportcol:false});
	colModel.push({ label:" ", 			index: 'CHK_C',      					name: 'CHK_C',     						width: 35,  	align: 'center', sortable: false, hidden:true});
    colModel.push({ label:" ",			index: 'CHKOLD',     					name: 'CHKOLD',    						width: 35,  	align: 'center', sortable: false, hidden:true});
    colModel.push({ label:" ",			index: 'LEVEL',     					name: 'LEVEL',    						width: 35,  	align: 'center', sortable: false, hidden:true});
    colModel.push({ label:" ", 			index: 'SUBFILE',    					name: 'SUBFILE',   						width: 20, 		align: 'left',   sortable: true, exportcol:false});
    colModel.push({ label:"hash_id",	index: 'HASH_ID',       				name: 'HASH_ID',      					width: 0, 		align: 'center', hidden:true});  
    colModel.push({ label:"호스트명", 		index: 'HOST_NAME',  					name: 'HOST_NAME', 						width: 180, 	align: 'center'});
    colModel.push({ label:"경 로",		index: 'PATH',       					name: 'PATH',      						width: 420, 	align: 'left',   sortable: true});
    colModel.push({ label:"담당자", 		index: 'OWNER',      					name: 'OWNER',     						width: 120, 	align: 'center', sortable: true});
    colModel.push({ label:"최근 검출일",	index: 'REGDATE_DT',       				name: 'REGDATE_DT',      				width: 150,  	align: 'center', sortable: true});
    colModel.push({ label:"합계",	 		index: 'TYPE',           				name: 'TYPE',          					width: 110,  	align: 'center',  formatter:'integer', 
    	formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, sortable: true, sorttype: 'number'});
    
	for(var i = 0; pattern.length > i; i++){ // str 배열만큼 for돌림
		var row = pattern[i].split(', ');
		var ID = row[0].split('ID=').join(''); //변수 TABLE_NAME은 TB_NAME1, 다음 반복문에서는 TB_NAME2
		var PATTERN_NAME = row[1].split('PATTERN_NAME=').join(''); 
		var data_id = row[4].split('ID=').join('');
		
		colModel.push({ label:ID.split('=')[1],  index: data_id,           name: data_id,          width: 110,  align: 'center',  formatter:'integer', 
			formatoptions:{thousandsSeparator: ",", defaultValue: '0'}, sortable: true, sorttype: 'number'});
	}
    colModel.push({ label:"계정", 		index: 'ACCOUNT',    					name: 'ACCOUNT',   						width: 120, 	align: 'center', sortable: true});
    colModel.push({ label:"처리 상태",  	index: 'APPROVAL_STATUS_PRINT_NAME', 	name: 'APPROVAL_STATUS_PRINT_NAME', 	width: 140, 	align: 'center', sortable: true, formatter: approvalNm});
    colModel.push({ label:"최초 검출일",	index: 'CREATE_DT',       				name: 'CREATE_DT',      				width: 150,  	align: 'center', sortable: true});
    colModel.push({ label:"수정일",		index: 'MODIFIED',       				name: 'MODIFIED',      					width: 150,  	align: 'center', sortable: true});
    colModel.push({ label:"기안일",		index: 'APPROVAL_DT',     				name: 'APPROVAL_DT',    				width: 150,  	align: 'center', sortable: false, hidden:true});
    
    colModel.push({ label:"target_id", 				index: 'TARGET_ID',  						name: 'TARGET_ID', 						width: 1, align: 'center', hidden:true});
   	colModel.push({ label:"ap_no", 					index: 'AP_NO',  							name: 'AP_NO', 							width: 1, align: 'center', hidden:true});
    colModel.push({ label:"처리 상태 코드",				index: 'APPROVAL_STATUS', 					name: 'APPROVAL_STATUS', 				width: 1, align: 'center', hidden:true});
    colModel.push({ label:"processing_group_id",	index: 'PROCESSING_GROUP_ID', 				name: 'PROCESSING_GROUP_ID', 			width: 1, align: 'center', hidden:true});
    colModel.push({ label:"filename",				index: 'FILENAME', 							name: 'FILENAME', 						width: 1, align: 'center', hidden:true});
    colModel.push({ label:"notepad",				index: 'NOTEPAD', 							name: 'NOTEPAD', 						width: 1, align: 'center', hidden:true});
    colModel.push({ label:"LEVEL",					index: 'LEVEL', 							name: 'LEVEL', 						width: 1, align: 'center', hidden:true});
    

    
    $("#targetGrid").jqGrid({
        datatype: "local",
        mtype : "POST",
        ajaxGridOptions : {
            type    : "POST",
            async   : true
        },
        colModel : colModel,
        loadonce : true,
	   	autowidth: true,
        viewrecords: true,
        width: $("#targetGrid").parent().width(),
        height: 585,
        shrinkToFit: false,
        pager: "#targetGridPager",
        rownumbers : false,
        rownumWidth : 75,
        jsonReader : {
            id : "ID"
        },
        rowNum:1000,
		rowList:[1000,2000,2500,5000],
		mutipageSelection: true,
        onCellSelect: function(rowid,icol,cellcontent,e) {
        	$("#pathWindow").hide();
            $("#taskWindow").hide();
            if (icol == 0) return;
            e.stopPropagation();
            var isChk = $(this).getCell(rowid, 'CHK_C');
            var isLeaf = $(this).getCell(rowid, 'LEAF');
            var id = $(this).getCell(rowid, 'HASH_ID');
            var tid = $(this).getCell(rowid, 'TARGET_ID');
            var approval_status = $(this).getCell(rowid, 'APPROVAL_STATUS');
            var ap_no = $(this).getCell(rowid, 'AP_NO');
            
            console.log("icol", icol);
            
            if(icol != 9 && approval_status != null){
            	if (isChk == "0") { // 상위
	            	var pop_url = "${getContextPath}/popup/detectionDetail";
	            	var winWidth = 1142;
	            	var winHeight = 365;
	            	var popupOption= "width="+winWidth+", height="+winHeight + ", left=0, top=0, scrollbars=yes, resizable=no, location=no";
	            	var pop = window.open(pop_url,id,popupOption); 
	            	
	            	var newForm = document.createElement('form');
	            	newForm.method='POST';
	            	newForm.action=pop_url;
	            	newForm.name='newForm';
	            	newForm.target=id;
	            	
	            	var input_id = document.createElement('input');
	            	input_id.setAttribute('type','hidden');
	            	input_id.setAttribute('name','id');
	            	input_id.setAttribute('value',id);
	            	
	            	var input_tid = document.createElement('input');
	            	input_tid.setAttribute('type','hidden');
	            	input_tid.setAttribute('name','tid');
	            	input_tid.setAttribute('value',tid);
	            	
	            	var input_ap = document.createElement('input');
	            	input_ap.setAttribute('type','hidden');
	            	input_ap.setAttribute('name','ap_no');
	            	input_ap.setAttribute('value',ap_no);
	            	
	            	newForm.appendChild(input_id);
	            	newForm.appendChild(input_tid);
	            	newForm.appendChild(input_ap);
	            	document.body.appendChild(newForm);
	            	newForm.submit();
	            	
	            	document.body.removeChild(newForm);
            		
            	}else { // 하위
            		getLowPath(id, tid, ap_no);
            	}
            	 
            }else{ // 결재 항목이 있을 경우
               	$("#excepPath tr[name=excepPathAddTr]").remove();
               	$("#reason").val("");
               	
                   // 테이블에서 path_ex_group_name 가져와서 넣어줘야함
                   var postDt = {};
                   postDt["data_processing_group_idx"] = $(this).getCell(rowid, 'PROCESSING_GROUP_ID');
                   $.ajax({
                       type: "POST",
                       url: "${getContextPath}/approval/selectProcessPath",
                       async: true,
                       data: JSON.stringify(postDt),
                       contentType: 'application/json; charset=UTF-8',
                       success: function (searchList) {
                           var arr = [];
                           var getPathex = [];

                           if (searchList.length > 0) {
                               $.each(searchList, function (i, s) {

                                   arr.push(s);
                                   getPathex.push(arr[i].PATH);
                                   var reason = arr[0].FLAG
                                   
                                   $("#excepPath").append(
                                   		"<tr name='excepPathAddTr' style='border:none;'>"+
                                   			"<th style='padding:0px; background: transparent; text-align: left;'>"+
                                   				"<a style=\"color: blue; cursor: pointer;\" onclick=\"showDetail("+s.FID+", "+s.hash_id+","+s.AP_NO+","+rowid+");\">"+getPathex[i]+"</a>"+
                                   			"</th>" + 
                                  			"</tr>"
                                  	);
                                   $("#reason").val(reason);
                               });
                           }
                           return;
                       },
                       error: function (request, status, error) {
                           alert("실패 하였습니다.");
                       }
                   });

                   var detailName = $(this).getCell(rowid, 'FILENAME');
                   var serverName = $(this).getCell(rowid, 'HOST_NAME');  
                   var BasisName  = $(this).getCell(rowid, 'NOTEPAD');

                   $("#BasisName").html(BasisName);
                   $("#groupName").html(detailName);
                   $("#regisServer").val(serverName);
                   $("#insertPathExcepPopup").show();
            	
            }
        },
        loadComplete: function(rowid,icol,cellcontent,e) {
        	var ids = $("#targetGrid").getDataIDs();
        	
        	$.each(
       	 		ids,function(idx,rowid)
       	 		{
       	 			rowData = $("#targetGrid").getRowData(rowid);
	       	 		var decrypted = '_decrypted';
	       	 		var shortName = rowData['SHORTNAME'];
       	 		}
       	 	); 
        	
        }
    });
}


function downLoadExcel()
{
	resetFomatter = "downloadClick";
	
	$("#targetGrid").jqGrid("hideCol",["CHK"]);
	$("#targetGrid").jqGrid('showCol', 'HASH_ID');
	/* \ / : * ? " < > } */
	var regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/g;
	var hostname = $("#targetSearch").val().split("(");
	
	var nameList = hostname[0].split(":");
	var name = "";
	
	
	for(i=0; i<nameList.length ; i++){
		if(i == nameList.length-1 ){
			name += nameList[i]
		}else{
			name += nameList[i] + "-"
		}		
		
	}

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
	$("#targetGrid").jqGrid("exportToCsv",{
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
        includeHeader: false,
        fileName : "검출_리스트_"+name+ "_" + today + ".csv",
        mimetype : "text/csv; charset=utf-8",
        event : resetFomatter, 
        returnAsString : false 
    });
	
	resetFomatter = null;
	
	$("#targetGrid").jqGrid('hideCol', 'HASH_ID');
	$("#targetGrid").jqGrid("showCol",["CHK"]);
} 

function downLoadExcelDetail()
{
	var ap_list = null;
	var host_list = null;
	var group_id_list = [];
	var datas = $('#jstree').jstree("get_selected",true);
	
	if(datas.length ==0){
		alert("다운로드 할 목록이 없습니다.");
		return;
	}
	
	if(datas[0].id.includes("IDX")){ // 사용자 그룹 선택 시
		var group_name = datas[0].id;
			group_name = group_name.replace("IDX_", "");
		
		if(datas[0].data.type == 0){
			for(var i=0;i<datas[0].children_d.length;i++){
				group_id_list.push(datas[0].children_d[i]);
			}
		}else{
			host_list = datas[0].data.target_id;
			ap_list = datas[0].data.ap;
		}
	}else{
		var group_name = datas[0].id;
		
		if(datas[0].data.type == 0){
			for(var i=0;i<datas[0].children_d.length;i++){
				group_id_list.push(datas[0].children_d[i]);
			}
		}else{
			host_list = datas[0].data.target_id;
			ap_list = datas[0].data.ap;
		}
		
	}
	
	var checkedValues = $("input[name='detailReportCon']:checked").map(function() {
	    return this.value;
	}).get();
	var checkedValues2 = $("input[name='detailReportType']:checked").map(function() {
	    return this.value;
	}).get();
	
	if(checkedValues.length < 1){
		alert("선택된 데이터가 없습니다.");
		return;
	}  
	
	var oPostDt = {};
	
	oPostDt["target_id"] = host_list;
	oPostDt["group_id"] = group_name;
	oPostDt["ap_no"] = ap_list; 
	oPostDt["group_id_list"] = group_id_list;
	oPostDt["col_list"] = checkedValues;
	oPostDt["type_list"] = checkedValues2;
	oPostDt["sch_SDATE"] = $("#fromDate").val();
	oPostDt["sch_EDAT"] = $("#toDate").val(); 
	oPostDt["report_flag"] = "xlsx"
	oPostDt["url"]						= '/manage/pi_detection_list' ; // 바로가기 url
	oPostDt["page_name"]				= '[결과 관리 > 결과조회/조치계획]'; // 현재 페이지 설명
	oPostDt["flag"] = "detection";
 
	var now = new Date();
	const year = now.getFullYear();
	const month = now.getMonth() + 1;
	const date = now.getDate(); 

	var report_file_name = $("#report_file_name").val();
	var reportDate = year +'' + (month >= 10 ? month : '0' + month) + '' + (date >= 10 ? date : '0' + date);
	var filename =  "결과조회/조치계획_"+ reportDate +".xlsx";
	
	if(report_file_name != null && report_file_name != ""){
		filename = report_file_name+ ".xlsx"
	}
	
	oPostDt["filename"] = filename;

	var realfilename = "";
	
	$.ajax({ // 서버에 저장할 파일 명 
        type: "POST",
        url: "${getContextPath}/download/downLoadFileInformation", 
        async : false, 
        data :  JSON.stringify(oPostDt), 
        contentType: 'application/json; charset=UTF-8',
        success: function (result) {
        	
        	realfilename =  result.real_file_name; // 서버에 저장할 파일 명
			oPostDt["real_file_name"] = realfilename;

			updateProgress(); // 보고서 다운로드 팝업 표기
			$.ajax({ // 보고서 다운로드
		        type: "POST",
		        url: "${getContextPath}/report/reportDetailBatch",
		        async : true, 
		        data : JSON.stringify(oPostDt), 
		        contentType: 'application/json; charset=UTF-8',
		        success: function (response) {
		        	
// 		        	if (false) { // 테스트 기간동안 페이지 이동을 방지하기 위해 false 처리
		        	if (response.exitCode == 0) { // 0일경우 이상 없이 완료, 0이 아닌 다른 값일 경우 에러 발생
		                
		                var form = $('<form></form>').attr({
		 	  				action: '<%=request.getContextPath()%>/download/excelDownloadfile',
		 	  				method: 'post'
		 	  			}).css('display', 'none');
		 	  			
		 	  			var inputFilename = $('<input></input>').attr({
		 	  				type: 'hidden',
		 	  				name: 'filename',
		 	  				value: filename
		 	  			});
		                  
		                var inputRealFilename = $('<input></input>').attr({
		                	type: 'hidden',
		                	name: 'realfilename',
		                	value: realfilename
		                });
		                
		                form.append(inputFilename);
		                form.append(inputRealFilename);
		                $('body').append(form);
		                form.submit();
		                form.remove();
		                
		                $("#ExcelDownPopUp").hide();
		        	}else{
		        		console.log("보고서 다운로드 에러 ::: ", response)	
		        	}
		        },
		        error: function (request, status, error) {
		        	console.log(status);
		            //alert("처리 등록을 실패 하였습니다.");
		
		            return;
		        }
		    });  // 보고서 다운로드 끝
		
        },
        error: function (request, status, error) {
        	console.log(status);
            //alert("처리 등록을 실패 하였습니다.");

            return;
        }
    });
} 

function executeDownload(resultList){
	var result = "경로,호스트,주민번호,외국인번호,여권번호,운전면허,계좌번호,카드번호,전화번호,이메일,합계\r\n";
	$.each(resultList, function(i, item){
		result +=  item.SHORTNAME + "," + item.HOST + "," + item.TYPE1 + "," + item.TYPE2 + "," + item.TYPE3 + "," + item.TYPE4 + "," + item.TYPE5 + "," + item.TYPE6 + "," + item.TYPE7 + "," + item.TYPE8 + "," + item.TYPE+"\r\n";
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
	
	var blob = new Blob(["\ufeff"+result], { type: "text/csv;charset=utf-8" });
	if(navigator.msSaveBlob){
		window.navigator.msSaveOrOpenBlob(blob, "검출_리스트_" + today + ".csv");
	} else {
		var downloadLink = document.createElement("a");
		var url = URL.createObjectURL(blob);
		downloadLink.href = url;
		downloadLink.download = "검출_리스트_" + today + ".csv";

		document.body.appendChild(downloadLink);
		downloadLink.click();
		document.body.removeChild(downloadLink);
	}
}

$(document).ready(function () {
	
	var oToday = new Date();
	$("#toDate").val(getFormatDate(oToday));
	var oFromDate = new Date(oToday.setDate(oToday.getDate() - 30));
	$("#fromDate").val(getFormatDate(oFromDate));
	
	setSelectDate(); 
	
	var ap = "${apno}";
	var grade = ${memberInfo.USER_GRADE};
	
	var helpIcon = '<img alt="" src="${pageContext.request.contextPath}/resources/assets/images/help_icon_1.png" style="width: 24px; position: absolute; top: 30px; left: 805px; cursor: pointer;" id="userHelpIcon">';
	
	$("#targetGrid").clearGridData();
	$(".grid_top_PC").css("display", "none");
	$(".grid_top_Server").css("display", "");

	if(grade == "9" || grade == "5"){
		$(".detection_list_title").text("결과조회/조치계획");
		$(".detection_list_txt").css("left", "222px");
		$(".detection_list_txt").after(helpIcon);
	}else{  
		$(".detection_list_title").text("결과조회");   
		$(".detection_list_txt").css("left", "144px");
	}
	 
	$("#btnSearchHost").click();
	
	$("#btnDownloadExcelBtn").click(function(){
		
		if($("#btnDownloadExcel").val() == "simple"){
			downLoadExcel();
		}else{ 
			$("#reportExtensiontd").html(csvReportDownContent(pattern));
			$("#detailExceldownloadPop").show();
		}
	});
	
	
	$("#detailExceldownload").click(function(){
		downLoadExcelDetail();
	});

    $("#taskWindowClose").click(function(e){
        $("#taskWindow").hide();
    });
    
    $("#pathWindowClose").click(function(e){
        $("#pathWindow").hide();
    });

    $("#btnSearch").click(function(e){

    	if(idxList != null){
	        fnSearchFindSubpath(2, idxList);
    	}else{
	        fnSearchFindSubpath(1, null);
    	}
    });

	$("#taskWindow").draggable({
		containment: '.content',
   	 	cancel : '.mxWindowPane'
   	});
	
	$("#pathWindow").draggable({
		containment: '.content',
   	 	cancel : '.mxWindowPane'
   	});
	

	var agent = navigator.userAgent.toLowerCase();
	if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ) {
	}
	else {
		$("#taskWindow").resizable({
	   	});
		
		$("#pathWindow").resizable({
	   	});
	}

    $("#searchLocation").keyup(function(e){
        if (e.keyCode == 13) {
        	if(idxList != null){
    	        fnSearchFindSubpath(2, idxList);
        	}else{
    	        fnSearchFindSubpath(1, null);
        	}
        }
    });
    
    loadTargetGrid(); 
    
    $("#hostSelect").change(function(e){
    	$("#taskWindow").hide();
    	$("#pathWindow").hide();
	    $("#targetGrid").clearGridData();
	    $("#searchLocation").val("");
    	if($(this).val() != 'all'){
	        fnSearchFindSubpath(1, null);
    	} 
    });
    
    if("${target_id}" != null){
    	$("#targetSearch").val("${host}");
    	$("#targetSearch").text("${host}");
    	$("#hostSelect").val("${target_id}");
	    $("#searchLocation").val("");
	    $("#targetGrid").clearGridData();
    	$("#ap_no").val("${apno}");

    	fnSearchFindSubpath(1, null);
    	
    } 
    
    
    // 검출 리스트 - 처리 버튼
    $("#btnDeletionRegist").on("click", function(e) {
        fnOpenDeletionRegist();
    });
    
    $("#answerRadio").on("click", function(e) {
    	fnOpenTrueDeletionRegist();
    });
    
    $("#wrongRadio").on("click", function(e) {
    	fnOpenFalseDeletionRegist();
    });
    
    // 검출리스트 - 처리 - 정탐/오탐 팝업
    $("#selectReasonTrue").click(function(){
        $("#resultTrue").show();
    
    });

    // 검출리스트 - 처리 - 정탐 팝업 - 확인버튼
    $("#btnResultTrueSave").on("click", function(e) {
        var checkedradio = $("input:radio[name=processing_flag]:checked").val();
        var selectedDate = $("#selectedDate").val();

        if ( ! checkedradio ) {
            alert("사유를 선택하세요.");
            jQuery('input[name="processing_flag"]').focus();
            return false;
        }

        $("input:radio[name=trueFalseChk]").val(checkedradio);
        $("input:radio[name=processing_flag]").prop("checked",false);
        $("#resultTrue").hide();
        return;
    });

    // 검출리스트 - 처리 - 정탐 팝업 - 취소 버튼
    $("#btnResultTrueCancel").on("click", function(e) {
        $("input:radio[name=trueFalseChk]").prop("checked",false);
        $("input:radio[name=processing_flag]").prop("checked",false);

        $("#legalSystem").val("");
        $("#deleteCycle").val("");
        
        $("#resultTrue").hide();
    });

    // 검출 리스트 - 처리 - 저장
    $("#btnDeletionSave").on("click", function(e) {
        fnSaveDeletion();
    });

    // 검출리스트 - 처리 - 취소 버튼
    $("#btnDeletionCancel").on("click", function(e) {
        $("input:radio[name=trueFalseChk]").prop("checked",false);
        $("input:radio[name=processing_flag]").prop("checked",false);
        $("#deletionRegistPopup").hide();
    });
    
    $("#btnTrueDeletionCancel").on("click", function(e) {
    	/* $("#path_detail_true").val("");
    	$("#trueDeletionAction option:eq(0)").prop("selected", true);
    	$(".true_processing_comment span").html("${trueList[0].BOTTOM_TEXT}");
    	$("#addTrueProcessing").html("");
        $("#trueDeletionRegistPopup").hide(); */
    	resetTrue();
    });
    
    $("#btnCancleTrueDeletionRegistPopup").on("click", function(e) {
    	/* $("#path_detail_true").val("");
    	$("#trueDeletionAction option:eq(0)").prop("selected", true);
    	$(".true_processing_comment span").html("${trueList[0].BOTTOM_TEXT}");
    	$("#addTrueProcessing").html("");
        $("#trueDeletionRegistPopup").hide(); */
        resetTrue();
    });
    
    $("#btnFalseDeletionCancel").on("click", function(e) {
    	/* $("#path_detail_false").val("");
    	$("#falseDeletionAction option:eq(0)").prop("selected", true);
    	$("#selectDateFalse").css("display", "inline");
    	$(".false_processing_comment span").html("${falseList[0].BOTTOM_TEXT}");
    	$("#addFalseProcessing").html("");
        $("#falseDeletionRegistPopup").hide(); */
        resetFalse();
    });
    
    $("#btnCancleFalseDeletionRegistPopup").on("click", function(e) {
    	/* $("#path_detail_false").val("");
    	$("#falseDeletionAction option:eq(0)").prop("selected", true);
    	$("#selectDateFalse").css("display", "inline");
    	$(".false_processing_comment span").html("${trueList[0].BOTTOM_TEXT}");
    	$("#addFalseProcessing").html("");
        $("#falseDeletionRegistPopup").hide(); */
        resetFalse();
    });
    
    // 해당 input 태그 클릭시 라이도 버튼 클릭 이벤트
    $("#legalSystem").focus(function(){
    	$("#processingCheck2").prop("checked",true);
    });
    
    $("#deleteCycle").focus(function(){
    	$("#processingCheck3").prop("checked",true);
    });
    
    $("#userHelpIcon").on("click", function(e) {
		/* $("#userHelpPopup").show(); */
    	var id = "detection_list";
		var pop_url = "${getContextPath}/popup/helpDetail";
    	var winWidth = 1200;
    	var winHeight = 655;
    	var popupOption= "width="+winWidth+", height="+winHeight + ", left=0, top=0, scrollbars=no, resizable=no, location=no"; 	
    	//var pop = window.open(pop_url,"detail",popupOption);
    	var pop = window.open(pop_url ,id, popupOption);
    	
    	var newForm = document.createElement('form');
    	newForm.method='POST';
    	newForm.action=pop_url;
    	newForm.name='newForm';
    	newForm.target=id;
    	
    	var data = document.createElement('input');
    	data.setAttribute('type','hidden');
    	data.setAttribute('name','id');
    	data.setAttribute('id','id');
    	data.setAttribute('value',id);
    	
    	newForm.appendChild(data);
    	document.body.appendChild(newForm);
    	newForm.submit();
    	
    	document.body.removeChild(newForm);
	});
    
    $("#userHelpPCIcon").on("click", function(e) {
		/* $("#userHelpPopup").show(); */
    	var id = "detection_list_pc";
		var pop_url = "${getContextPath}/popup/helpDetail";
    	var winWidth = 1200;
    	var winHeight = 655;
    	var popupOption= "width="+winWidth+", height="+winHeight + ", left=0, top=0, scrollbars=no, resizable=no, location=no"; 	
    	//var pop = window.open(pop_url,"detail",popupOption);
    	var pop = window.open(pop_url ,id, popupOption);
    	
    	var newForm = document.createElement('form');
    	newForm.method='POST';
    	newForm.action=pop_url;
    	newForm.name='newForm';
    	newForm.target=id;
    	
    	var data = document.createElement('input');
    	data.setAttribute('type','hidden');
    	data.setAttribute('name','id');
    	data.setAttribute('id','id');
    	data.setAttribute('value',id);
    	
    	newForm.appendChild(data);
    	document.body.appendChild(newForm);
    	newForm.submit();
    	
    	document.body.removeChild(newForm);
	});
    /* $("#btnCancleUserHelpPopup, #btnCloseUserHelpPopup").on("click", function(e) {
		$("#userHelpPopup").hide();
	}); */
    
    setselectDateTrue();
    setselectDateFalse();
    
/* <c:if test="${memberInfo.USER_GRADE == '0' || memberInfo.USER_GRADE == '1' || memberInfo.USER_GRADE == '2' || memberInfo.USER_GRADE == '3' || memberInfo.USER_GRADE == '7'}">
	$(".detection_list_title").html("결과조회")
	$(".detection_list_txt").css("left", "144px");
	$(".answerLabel").css("display", "none");
	$(".wrongLabel").css("display", "none");
	$("#btnDownloadExcel").css("margin-top", "10px");
</c:if> */

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
		$(".ui-autocomplete").hide();
    	if(to) { clearTimeout(to); }
        to = setTimeout(function () {
          $('#jstree').jstree(true).search(v);
        }, 250);
    }
});

//확인
$("#btnCheck").click(function (e) {
	$("#insertPathExcepPopup").hide();
	var tr = $("#excepPath").children();
	tr.remove();
	$("#pathWindow").hide();
	$("#taskWindow").hide();
});

$("#btnCancleInsertPathExcepPopup").click(function (e) {
	$("#insertPathExcepPopup").hide();
});

$("#report_flag").change(function(){
	var report_flag = $("#report_flag").val();
	if(report_flag == "xlsx") $("#reportExtensiontd").html(csvReportDownContent(pattern));
	if(report_flag == "pdf") $("#reportExtensiontd").html(pdfReportDownContent(pattern));
});

}); 

$(document).mouseup(function (e){
	  var LayerPopup = $("#userHelpPopup");
	  if(LayerPopup.has(e.target).length === 0){
		  $("#userHelpPopup").hide();
	  }
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

// 정탐 처리 저장 
$("#btnTrueDeletionSave").click(function(e){
	
	var oToday = new Date();
	var date = getFormatDate(oToday);
	var idx = 0;
	var aDeletionList = [];
	var aDeletionTargetList = [];
	var aDeletionData = $("#targetGrid").jqGrid('getGridParam', 'data');
	
	for (var i = 0; i < aDeletionData.length; i++) {
		var level = aDeletionData[i].LEVEL;
		var chkold = aDeletionData[i].CHKOLD;
		var target_id = aDeletionData[i].TARGET_ID;
		var ap_no = aDeletionData[i].AP_NO;
		var id = aDeletionData[i].HASH_ID;
		if (level != chkold) {
			aDeletionList.push(id);
			aDeletionTargetList.push({"tid" : target_id , "ap_no" : ap_no, "hash_id" : id});
		}
	}
	 
	/*----------------------------------------------------------*/
	 
	var tid = $("#hostSelect").val();
	var path_detail_true = $("#path_detail_true").val().trim();
	var next_date_remedi;
	var sFlag = $("#trueDeletionAction").val();
	var add_content = "";
	var sProcessFlag;
	
	 if($("#selectDateTrue").val() < date){
		alert("지정 일 현재 날짜보다 이전입니다.");
		return;
	};
	if(path_detail_true == ""){
		alert("판단근거는 반드시 작성해야 합니다. 상세하게 작성해 주세요.");
		return;
	}
	
	var trueList = JSON.parse('${jsonTrueList}');
	
	for(var i=0;i<trueList.length;i++){
		if(sFlag == trueList[i].PROCESSING_FLAG){
			 sProcessingFlag = '정탐(' + trueList[i].PROCESSING_FLAG_NAME +')';
			 if(trueList[i].DATE_ENABLE ==1 ) next_date_remedi = $("#selectDateTrue").val();
			 if(trueList[i].COMMENT_ENABLE ==1 ) {
				add_content = $("#comment_content").val();
				if(add_content.trim()=="") {
					alert("조치계획에 미입력 항목이 있습니다. 다시 확인해주세요.");
					return;
				}
			}
			
		}	
	}
	/*----------------------------------------------------------*/
	
	
	
	 var sChargeNm = sProcessingFlag+ "처리_" + <fmt:formatDate value="${nowDate}" pattern="yyyyMMdd" /> + "_" + "${memberInfo.USER_NO}" + "_";
	 
	 var oPostDt = {};
	 oPostDt["deletionTargetList"]      = aDeletionTargetList;
	 oPostDt["deletionList"]             = aDeletionList;
	 oPostDt["processing_flag"]          = sFlag;
	 oPostDt["data_processing_name"]     = sChargeNm;
	 oPostDt["next_date_remedi"]         = next_date_remedi;
	 oPostDt["ap_no"]	 				 = $("#ap_no").val();
	 oPostDt["selectDateTrue"]	 		 = $("#selectDateTrue").val();
	 oPostDt["notePad"]	 			 	 = path_detail_true.replace(/(?:\r\n|\r|\n)/g, '<br />');;
	 oPostDt["add_content"]				 = add_content;
	 oPostDt["tid"]						 = tid;
	 oPostDt["approval_type"]		   = 1;
	 
	 var oJson = JSON.stringify(oPostDt);
	 
	 var confirmCheck = confirm('해당 내용으로 등록하시겠습니까?');
	 
	 if(confirmCheck == true){
		$.ajax({
		     type: "POST",
		     url: "${getContextPath}/detection/regist_process",
		     async : false,
		     data : oJson,
		     contentType: 'application/json; charset=UTF-8',
		     success: function (result) {
		         if (result.resultCode == "1") {
		             alert("처리 등록을 실패 하였습니다.");
		             return;
		         }
	
 		        $("#targetGrid").setGridParam({
 		             url: "${getContextPath}/manage/selectFindSubpath",
		             postData: $("#targetGrid").getGridParam('postData'),
 		             datatype: "json",
 		             treedatatype: 'json'
 		         }).trigger("reloadGrid"); 
	
				alert("처리를 등록 하였습니다.");
				resetTrue();
		         return;
		     },
		     error: function (request, status, error) {
		         alert("처리 등록을 실패 하였습니다.");
	
		        resetTrue();
		         return;
		     }
		 });
	 }else{
			return;
		}
	 
});

// 오탐 처리 저장
$("#btnFalseDeletionSave").click(function(e){
	
	var oToday = new Date();
	var date = getFormatDate(oToday);
	var idx = 0;
	var aDeletionList = [];
	var aDeletionTargetList = [];
	var aDeletionData = $("#targetGrid").jqGrid('getGridParam', 'data');
	
	for (var i = 0; i < aDeletionData.length; i++) {
		var level = aDeletionData[i].LEVEL;
		var chkold = aDeletionData[i].CHKOLD;
		var target_id = aDeletionData[i].TARGET_ID;
		var ap_no = aDeletionData[i].AP_NO;
		var id = aDeletionData[i].HASH_ID;
		if (level != chkold) {
			aDeletionList.push(id);
			aDeletionTargetList.push({"tid" : target_id , "ap_no" : ap_no, "hash_id" : id})
		}
	}
	

	var tid = $("#hostSelect").val();
	var path_detail_false = $("#path_detail_false").val().trim();
	var next_date_remedi = $("#selectDateFalse").val();
/* 	var sFlag = $("#falseDeletionRegistPopup input:radio[name=popup_content]:checked").val(); */
	var sFlag = $("#falseDeletionAction").val();
	var add_content = "";
	var sProcessFlag = "";
	
	 if($("#selectDateFalse").val() < date){
		alert("지정 일 현재 날짜보다 이전입니다.");
		return;
	};

	if(path_detail_false == ""){
		alert("판단근거는 반드시 작성해야 합니다. 상세하게 작성해 주세요.");
		return;
	}
	
	
	var falseList = JSON.parse('${jsonFalseList}');
	
	for(var i=0;i<falseList.length;i++){
		if(sFlag == falseList[i].PROCESSING_FLAG){
			sProcessFlag = '오탐(' + falseList[i].PROCESSING_FLAG_NAME +')';
			 if(falseList[i].DATE_ENABLE ==1 ) next_date_remedi = $("#selectDateFalse").val();
			 if(falseList[i].COMMENT_ENABLE == 1 ) {
				add_content = $("#comment_content_false").val();
				if(add_content.trim()=="") {
					alert("조치계획에 미입력 항목이 있습니다. 다시 확인해주세요.");
					return;
				}
			}
			
		}	
	}
	/*---------------------*/
	var sChargeNm = sProcessFlag + "처리_" + <fmt:formatDate value="${nowDate}" pattern="yyyyMMdd" /> + "_" + "${memberInfo.USER_NO}" + "_";
	
	var oPostDt = {};
	oPostDt["deletionTargetList"]      = aDeletionTargetList;
	oPostDt["deletionList"]            = aDeletionList;
	oPostDt["processing_flag"]         = sFlag;
	oPostDt["data_processing_name"]    = sChargeNm;
	oPostDt["next_date_remedi"]        = next_date_remedi;
	oPostDt["ap_no"]	 			   = $("#ap_no").val();
	oPostDt["selectDateTrue"]	 	   = $("#selectDateFalse").val();
	oPostDt["notePad"]	 			   = path_detail_false.replace(/(?:\r\n|\r|\n)/g, '<br />');;
	oPostDt["add_content"]			   = add_content;
	oPostDt["tid"]					   = tid;
	oPostDt["approval_type"]		   = 1;
	
	var oJson = JSON.stringify(oPostDt);
	
	var confirmCheck = confirm('선택하신 내용을 예외 처리 하시겠습니까?');
	
	if(confirmCheck == true){
		$.ajax({
		     type: "POST",
		     url: "${getContextPath}/detection/regist_process",
		     async : false,
		     data : oJson,
		     contentType: 'application/json; charset=UTF-8',
		     success: function (result) {
	
		         if (result.resultCode == "-1") {
		             alert("처리 등록을 실패 하였습니다.");
		             return;
		         }
	
 		        $("#targetGrid").setGridParam({
 		             url: "${getContextPath}/manage/selectFindSubpath",
 		             postData: $("#targetGrid").getGridParam('postData'),
 		             datatype: "json",
		             treedatatype: 'json'
		         }).trigger("reloadGrid"); 
	
				alert("처리를 등록 하였습니다.");
				resetFalse();
		         return;
		     },
		     error: function (request, status, error) {
		         alert("처리 등록을 실패 하였습니다.");
	
		         resetFalse();
		         return;
		     }
		 });
	}else{
		return;
	}
	
});

//전체선택
function fn_allChkTargetGrid(chk, e) {
	e = e||event; 
	e.stopPropagation? e.stopPropagation() : e.cancelBubble = true; 
	
	var ids = $('#targetGrid').getDataIDs();
	if (ids.length > 0) {
		if($(chk).is(":checked")) {		// 선택시
			for(var i=0 ; i<ids.length ; i++){
				var rowid = ids[i];
				var dtObj = $('#targetGrid').getRowData(rowid);
				console.log("dtObj", dtObj);  
				console.log("rowid", rowid);   
				//if(rowObject['APPROVAL_STATUS'] == 'E'){
				if(dtObj.APPROVAL_STATUS == ''){
					$("#gridChk_"+rowid).prop('checked',true);
					$("#targetGrid").jqGrid('setCell', rowid, 'LEVEL', "0");
				}
			
			}
		} else {						// 해제시
			for(var i=0 ; i<ids.length ; i++){
				var rowid = ids[i];
				var dtObj = $('#targetGrid').getRowData(rowid);
				if(dtObj.APPROVAL_STATUS  == '')
					$("#gridChk_"+rowid).prop('checked',false);
					$("#targetGrid").jqGrid('setCell', rowid, 'LEVEL', "1");
				}
				
			}
		}
	}

$('#btnApproval').on('click', function(){
	var date = new Date();
	var aDeletionList = [];
	$('[name=gridChk]').each(function(i, item){
    	if (item.checked) {
    		var status = $('#targetGrid').getCell(item.value, 'APPROVAL_STATUS'); 

    		if(status != 'E'){
	    		aDeletionList.push(item.value);
    		}
        }
    	
    });
	
	if (aDeletionList.length == 0) {
        alert("처리 항목을 선택하세요.");
        return;
    }
	
	var sChargeNm = '오탐처리_'+date.getFullYear()+''+(date.getMonth()+1)+''+date.getDate()+'_'+"${memberInfo.USER_NO}" + "_";
	var sFlag = '5';
	var oPostDt = {};
	oPostDt["deletionList"]             = aDeletionList;
    oPostDt["processing_flag"]          = sFlag;
    oPostDt["data_processing_name"]     = sChargeNm;
   	
   	var oJson = JSON.stringify(oPostDt);
   	if(confirm('선택하신 내용을 예외 처리 하시겠습니까?')){
   		
	    $.ajax({
	        type: "POST",
	        url: "${getContextPath}/manage/registProcess",
	        async : false,
	        data : oJson,
	        contentType: 'application/json; charset=UTF-8',
	        success: function (result) {
	            if (result.resultCode != "0") {
	                alert(result.resultCode + "처리 등록을 실패 하였습니다.");
	                return;
	            }
	
	           alert("처리를 등록 하였습니다.");
	
	           fnSearchFindSubpath(1, null);
	        },
	        error: function (request, status, error) {
	        	console.log(status);
	            alert("처리 등록을 실패 하였습니다.");
	
	            return;
	        }
	    });
   	}
});

$('#btnCancelApproval').on('click', function(){
	var aDeletionList = [];
	$('[name=gridChk]').each(function(i, item){
    	if (item.checked) {
    		var status = $('#targetGrid').getCell(item.value, 'APPROVAL_STATUS'); 
			
    		if(status == 'E'){
	    		aDeletionList.push(item.value);
    		}
        }
    	
    });
	
	if (aDeletionList.length == 0) {
        alert("처리 항목을 선택하세요.");
        return;
    }
	
	var sFlag = '5';
	var oPostDt = {};
	oPostDt["target_id"]				= $("#hostSelect").val();
	oPostDt["deletionList"]             = aDeletionList;
    oPostDt["processing_flag"]          = sFlag;
    
   	var oJson = JSON.stringify(oPostDt);
   	if(confirm('선택하신 경로를 예외 처리 해제 하시겠습니까?')){
   		
	    $.ajax({
	        type: "POST",
	        url: "${getContextPath}/manage/cancelApproval",
	        async : false,
	        data : oJson,
	        contentType: 'application/json; charset=UTF-8',
	        success: function (result) {
	            if (result.resultCode != "0") {
	                alert(result.resultCode + "예외 해제 실패 하였습니다.");
	                return;
	            }
	
	           alert("예외 해제 하였습니다.");
	
	           fnSearchFindSubpath(1, null);
	        },
	        error: function (request, status, error) {
	        	console.log(status);
	            alert("처리 등록을 실패 하였습니다.");
	
	            return;
	        }
	    });
   	}
});

/* 
$("#btnSearchHost").on("click", function(){
	
	var pop_url = "${getContextPath}/popup/targetList";
	var id = "targetList"
	var winWidth = 700;
	var winHeight = 570;
	var popupOption= "width="+winWidth+", height="+winHeight + ", left=0, top=0, scrollbars=no, resizable=no, location=no"; 	
	//var pop = window.open(pop_url,"lowPath",popupOption);
	var pop = window.open(pop_url,id,popupOption);
	popList.push(pop);
	sessionUpdate();
	
	//pop.check();
	
	var newForm = document.createElement('form');
	newForm.method='POST';
	newForm.action=pop_url;
	newForm.name='newForm';
	//newForm.target='lowPath';
	newForm.target=id;
	
	var data = document.createElement('input');
	data.setAttribute('type','hidden');
	data.setAttribute('name','hash_id');
	data.setAttribute('value',id);
	
	newForm.appendChild(data);
	document.body.appendChild(newForm);
	newForm.submit();
	
	document.body.removeChild(newForm);
	
});
*/

function findByPop(){
	$("#taskWindow").hide();
	$("#pathWindow").hide();
	$("#targetGrid").clearGridData();
	$("#searchLocation").val("");
    fnSearchFindSubpath(1, null);
}

function getLowPath(id, tid, ap_no){
	
	var pop_url = "${getContextPath}/popup/lowPath";
	var winWidth = 1142;
	var winHeight = 365;
	var popupOption= "width="+winWidth+", height="+winHeight + ", left=0, top=0, scrollbars=no, resizable=no, location=no"; 	
	//var pop = window.open(pop_url,"lowPath",popupOption);
	var pop = window.open(pop_url,id,popupOption);
	/* popList.push(pop);
	sessionUpdate(); */
	
	//pop.check();
	
	var newForm = document.createElement('form');
	newForm.method='POST';
	newForm.action=pop_url;
	newForm.name='newForm';
	//newForm.target='lowPath';
	newForm.target=id;
	
	var input_id = document.createElement('input');
	input_id.setAttribute('type','hidden');
	input_id.setAttribute('name','hash_id');
	input_id.setAttribute('value',id);
	
	var input_tid = document.createElement('input');
	input_tid.setAttribute('type','hidden');
	input_tid.setAttribute('name','tid');
	input_tid.setAttribute('value',tid);
	
	var input_ap = document.createElement('input');
	input_ap.setAttribute('type','hidden');
	input_ap.setAttribute('name','ap_no');
	input_ap.setAttribute('value',ap_no);
	
	newForm.appendChild(input_id);
	newForm.appendChild(input_tid);
	newForm.appendChild(input_ap);
	document.body.appendChild(newForm);
	newForm.submit();
	
	document.body.removeChild(newForm);
	
}


//검출 리스트 - 처리 팝업 오픈
function fnOpenDeletionRegist()
{
 $.ajax({
     type: "POST",
     url: "${getContextPath}/detection/selectProcessDocuNum",
     async: false,
     datatype: "json",
     success: function (result) {
         $('#title_process').html("처리_" + <fmt:formatDate value="${nowDate}" pattern="yyyyMMdd" /> + "_" + "${memberInfo.USER_NO}" + "_" + result.SEQ);
         SEQ = result.SEQ;
     }
 });

 var aDeletionList = [];
 var aNameList = [];
 var sRowID;

 $('[name=gridChk]').each(function(i, item){
 	if (item.checked) {
         var sRowID = $(item).data("rowid");
         aDeletionList.push(item.value);

         aNameList.push($("#targetGrid").getCell(sRowID, 'SHORTNAME'));
     }
 });

 if (aDeletionList.length == 0) {
     alert("처리 항목을 선택하세요.");
     return;
 }

 //$("#deletionRegistPopup #path_exception").val(aNameList);
 var tr = $("#path_exception").children();
 tr.remove();

 for (var i = 0; i < aNameList.length; i += 1) 
 {
     sTag  = "<tr style='border:none;'>";
     sTag += "    <th style='padding:2px; background: transparent; overflow:hidden; text-align:left;'>" + aNameList[i] + "</th>";
     sTag += "</tr>";

     $("#deletionRegistPopup #path_exception").append(sTag);
 }

 $("#deletionRegistPopup").show();
}

//검출 리스트 - 정탐 처리 팝업 오픈
function fnOpenTrueDeletionRegist()
{
 $.ajax({
     type: "POST",
     url: "${getContextPath}/detection/selectProcessDocuNum",
     async: false,
     datatype: "json",
     success: function (result) {
         $('#title_process_true').html("정탐처리_" + <fmt:formatDate value="${nowDate}" pattern="yyyyMMdd" /> + "_" + "${memberInfo.USER_NO}" + "_" + result.SEQ + "<span>" + "검출된 개인정보의 정탐 판단 근거에 대해 상세하게 작성해 주시고 조치계획을 선택해 주시기 바랍니다." + "</span>");
         SEQ = result.SEQ;
     }
 });

 var idx = 0;
 var aDeletionList = [];
 var aNameList = [];
 var sRowID;
 var hiddenList = [];
 var stausCheck = "";
 var aDeletionData = $("#targetGrid").jqGrid('getGridParam', 'data');
 
 for (var i = 0; i < aDeletionData.length; i++) {
		var level = aDeletionData[i].LEVEL;
		var chkold = aDeletionData[i].CHKOLD;
		if (level != chkold) {
			var aDeletionAssign = {};
			aDeletionAssign.ID = aDeletionData[i].ID; 
			aDeletionAssign.PATH = aDeletionData[i].PATH;
			aDeletionAssign.APPROVAL_STATUS_PRINT_NAME = aDeletionData[i].APPROVAL_STATUS_PRINT_NAME;
			aDeletionList[idx++] = aDeletionAssign;
		}
	}

 if (aDeletionList.length == 0) {
     alert("처리 항목을 선택하세요.");
     return;
 }

 for (var j = 0; j < aDeletionList.length; j++ ) {
	if(aDeletionList[j].APPROVAL_STATUS_PRINT_NAME != "" || aDeletionList[j].APPROVAL_STATUS_PRINT_NAME != null){
		stausCheck = aDeletionList[j].APPROVAL_STATUS_PRINT_NAME.substr(0,2);
	}else{
		stausCheck = aDeletionList[j].APPROVAL_STATUS_PRINT_NAME;
	}
	 
	console.log(stausCheck); 
	if(stausCheck == "대기" || stausCheck == "완료" || stausCheck == "등록"){
		 alert("처리 할수 없는 항목이 포함되어 있습니다.\n확인 후 다시 선택하세요.");
	     return;
	}
 }
 
 //$("#deletionRegistPopup #path_exception").val(aNameList);
 var tr = $("#path_exception_true").children();
 tr.remove();

 for (var i = 0; i < aDeletionList.length; i += 1) 
 {
	
     sTag  = "<tr style='border:none;'>";
     sTag += "    <th style='padding:2px; background: transparent; overflow:hidden; text-align:left;'>" + aDeletionList[i].PATH + "</th>";
     sTag += "</tr>";

     $("#trueDeletionRegistPopup #path_exception_true").append(sTag);
 }
 
 $("#trueDeletionRegistPopup").show();
}




//검출 리스트 - 오탐 처리 팝업 오픈
function fnOpenFalseDeletionRegist()
{
 $.ajax({
     type: "POST",
     url: "${getContextPath}/detection/selectProcessDocuNum",
     async: false,
     datatype: "json",
     success: function (result) {
         $('#title_process_false').html("오탐처리_" + <fmt:formatDate value="${nowDate}" pattern="yyyyMMdd" /> + "_" + "${memberInfo.USER_NO}" + "_" + result.SEQ + "<span>" + "검출된 개인정보의 오탐 판단 근거에 대해 상세하게 작성해 주시고 조치계획을 선택해 주시기 바랍니다." + "</span>");
     }
 });

 var idx = 0;
 var aDeletionList = [];
 var aNameList = [];
 var sRowID;
 var hiddenList = [];
 var stausCheck ="";
 var aDeletionData = $("#targetGrid").jqGrid('getGridParam', 'data');

 for (var i = 0; i < aDeletionData.length; i++) {
		var level = aDeletionData[i].LEVEL;
		var chkold = aDeletionData[i].CHKOLD;
		if (level != chkold) {
			var aDeletionAssign = {};
			aDeletionAssign.ID = aDeletionData[i].ID;
			aDeletionAssign.SHORTNAME = aDeletionData[i].PATH;
			aDeletionAssign.APPROVAL_STATUS_PRINT_NAME = aDeletionData[i].APPROVAL_STATUS_PRINT_NAME;
			aDeletionAssign.TARGET_ID = aDeletionData[i].TARGET_ID;
			aDeletionAssign.AP_NO = aDeletionData[i].AP_NO;
			aDeletionList[idx++] = aDeletionAssign;
		}
	}
 
 /* $('[name=gridChk]').each(function(i, item){
 	if (item.checked) {
         var sRowID = $(item).data("rowid");
         aDeletionList.push(item.value);

         aNameList.push($("#targetGrid").getCell(sRowID, 'SHORTNAME'));
         hiddenList.push($("#targetGrid").getCell(sRowID, 'APPROVAL_STATUS_PRINT_NAME'));
     }
 }); */

 if (aDeletionList.length == 0) {
     alert("처리 항목을 선택하세요.");
     return;
 }
 
 for (var j = 0; j < aDeletionList.length; j++ ) {
 
    if(aDeletionList[j].APPROVAL_STATUS_PRINT_NAME != "" || aDeletionList[j].APPROVAL_STATUS_PRINT_NAME != null){
		stausCheck = aDeletionList[j].APPROVAL_STATUS_PRINT_NAME.substr(0,2);
	}else{
		stausCheck = aDeletionList[j].APPROVAL_STATUS_PRINT_NAME;
	}
	 
    if(stausCheck == "대기" || stausCheck == "완료" || stausCheck == "등록"){
		 alert("처리 할수 없는 항목이 포함되어 있습니다.\n확인 후 다시 선택하세요.");
	     return;
	}
 }

 //$("#deletionRegistPopup #path_exception").val(aNameList);
 var tr = $("#path_exception_false").children();
 tr.remove();

 for (var i = 0; i < aDeletionList.length; i += 1) 
 {
     sTag  = "<tr style='border:none;'>";
     sTag += "    <th style='padding:2px; background: transparent; overflow:hidden; text-align:left;'>" + aDeletionList[i].SHORTNAME + "</th>";
     sTag += "</tr>";

     $("#falseDeletionRegistPopup #path_exception_false").append(sTag);
 }

 $("#falseDeletionRegistPopup").show();
}

//검출리스트 - 처리 저장
function fnSaveDeletion()
{
 var aDeletionList = [];

 $('[name=gridChk]').each(function(i, item){
     if (item.checked) {
         aDeletionList.push(item.value);
     }
 });

 if (aDeletionList.length == 0) {
     alert("처리 항목을 선택하세요.");
     return;
 }

 var next_date_remedi = $("#selectedDate").val();
 var sFlag = $("#deletionRegistPopup input:radio[name=trueFalseChk]:checked").val();

 if(!sFlag || !$("#deletionRegistPopup input:radio[name=trueFalseChk]").is(":checked")) {
 	alert("사유를 선택하십시오");
 	return false;
 }
 var sProcessFlag;

 switch(sFlag) {
     case "1": sProcessFlag = '정탐(삭제)'; break;
     case "2": sProcessFlag = '정탐(법제도)'; break;
     case "3": sProcessFlag = '정탐(삭제주기)'; break;
     case "4": sProcessFlag = '오탐(시스템 파일)'; break;
     case "5": sProcessFlag = '오탐(기타)'; break;
 }
 
 var notePad = "";
 if(sFlag == 2){
	 notePad = $("#legalSystem").val();
 }else if(sFlag == 3){
	 notePad = $("#deleteCycle").val();
 }

var sChargeId;

 $('#title_process').html("처리_" + <fmt:formatDate value="${nowDate}" pattern="yyyyMMdd" /> + "_" + "${memberInfo.USER_NO}" + "_");
 var sChargeNm = sProcessFlag + document.getElementById('title_process').innerHTML;
 
 var oPostDt = {};
 oPostDt["deletionList"]             = aDeletionList;
 oPostDt["processing_flag"]          = sFlag;
 oPostDt["data_processing_name"]     = sChargeNm;
 oPostDt["next_date_remedi"]         = next_date_remedi;
 oPostDt["ap_no"]	 				 = $("#ap_no").val();
 oPostDt["selectDateTrue"]	 			 = $("#selectDateTrue").val();
 oPostDt["notePad"]	 			 	 = notePad;
 

 var oJson = JSON.stringify(oPostDt);

 /* $.ajax({
     type: "POST",
     url: "${getContextPath}/detection/registProcess",
     async : false,
     data : oJson,
     contentType: 'application/json; charset=UTF-8',
     success: function (result) {

         if (result.resultCode != "0") {
             alert(result.resultCode + "처리 등록을 실패 하였습니다.");
             return;
         }

       $("#targetGrid").setGridParam({
             url: "${getContextPath}/manage/selectFindSubpath",
             postData: $("#targetGrid").getGridParam('postData'),
             datatype: "json",
             treedatatype: 'json'
         }).trigger("reloadGrid");  

         alert("처리를 등록 하였습니다.");
         $("#legalSystem").val("");
         $("#deleteCycle").val("");

         $("#deletionRegistPopup").hide();
         $("input:radio[name=trueFalseChk]").prop("checked",false);
         $("input:radio[name=processing_flag]").prop("checked",false);
         return;
     },
     error: function (request, status, error) {
         alert("처리 등록을 실패 하였습니다.");

         $("#legalSystem").val("");
         $("#deleteCycle").val("");
         $("input:radio[name=trueFalseChk]").prop("checked",false);
         $("input:radio[name=processing_flag]").prop("checked",false);
         return;
     }
 }); */

 $("input:radio[name=trueFalseChk]").prop("checked",false);
 $("input:radio[name=processing_flag]").prop("checked",false);
 $("#deletionRegistPopup").hide();
}

//날짜
function setselectDateTrue() 
{
    $("#selectDateTrue").datepicker({
        changeYear : true,
        changeMonth : true,
        dateFormat: 'yy-mm-dd'
    });

    var oToday = new Date();
    $("#selectDateTrue").val(getFormatDate(oToday));

};
//날짜
function setselectDateFalse() 
{
    $("#selectDateFalse").datepicker({
        changeYear : true,
        changeMonth : true,
        dateFormat: 'yy-mm-dd'
    });

    var oToday = new Date();
    $("#selectDateFalse").val(getFormatDate(oToday));

};
function getFormatDate(oDate)
{
    var nYear = oDate.getFullYear();           // yyyy 
    var nMonth = (1 + oDate.getMonth());       // M 
    nMonth = ('0' + nMonth).slice(-2);         // month 두자리로 저장 

    var nDay = oDate.getDate();                // d 
    nDay = ('0' + nDay).slice(-2);             // day 두자리로 저장

    return nYear + '-' + nMonth + '-' + nDay;
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

$("#trueDeletionAction").on("change", function (){
    var optionSelected = $("#trueDeletionAction option:selected", this);
    var valueSelected = this.value;
    var trueList = JSON.parse('${jsonTrueList}');
    
    $("#addTrueProcessing").html("");
    
    for(var i = 0; i<trueList.length;i++){
    	if(valueSelected == trueList[i].PROCESSING_FLAG){
    		$(".true_processing_comment span").html(trueList[i].BOTTOM_TEXT);
    		if(trueList[i].COMMENT_ENABLE == 1)$("#addTrueProcessing").html("<p class='true_processing_hint'>"+trueList[i].COMMENT_TITLE+"</p>"+"<input type='text' class='edt_sch' id='comment_content' placeholder='"+trueList[i].COMMENT_TEXT+"' style='width: 270px; margin-left: 5px; padding-left: 5px'>");
	 		if(trueList[i].DATE_ENABLE == 1 ) $("#selectDateTrue").css("display","inline-block");
	 		else $("#selectDateTrue").css("display","none");
    	}
    }

});

function trueDeletionChange(){

}

$("#falseDeletionAction").on("change", function (){
    var optionSelected = $("#falseDeletionAction option:selected",this);
    var valueSelected = this.value;
    var falseList = JSON.parse('${jsonFalseList}');
    
    $("#addFalseProcessing").html("");
    
    for(var i = 0; i<falseList.length;i++){
    	if(valueSelected == falseList[i].PROCESSING_FLAG){
    		$(".false_processing_comment span").html(falseList[i].BOTTOM_TEXT);
    		if(falseList[i].COMMENT_ENABLE == 1){ 
    			var comment_title ="<p class='false_processing_hint'>"+falseList[i].COMMENT_TITLE+"</p>";
    			$("#addFalseProcessing").html(comment_title + "<input type='text' class='edt_sch' id='comment_content_false' placeholder='"+falseList[i].COMMENT_TEXT+"' style='width: 270px; margin-left: 5px; padding-left: 5px'>");
    		}
    		if(falseList[i].DATE_ENABLE == 1 ) $("#selectDateFalse").css("display","inline-block");
    		else $("#selectDateFalse").css("display","none");
   		}
    }
});

//검출일 툴팁
function createTime(cellvalue, options, rowObject) {
	
	var html = null;
	if(resetFomatter == "downloadClick"){
		return cellvalue;
	}else{
	    html = "<p title='"+cellvalue+"'> " + cellvalue.substr(0,10) + "</p>";
	    return html;
	}
	
}

function approvalNm (cellvalue, options, rowObject) {
	
	if(cellvalue != "" && cellvalue != null){
		if (resetFomatter != "downloadClick") {
			return "<span id='approvalStatusNm'>"+cellvalue+"</span>";	
		}else{
			return cellvalue;	
		}
		
	}else {
		return "";
	}
}

function showDetail(fid, id, ap_no, rowid){
	
	$("#pathWindow").hide();
	$("#taskWindow").hide();
	var tid = $('#targetGrid').getCell(rowid, 'PID');
	
	if (fid == "0") {
		var pop_url = "${getContextPath}/popup/detectionDetail";
		var winWidth = 1142;
		var winHeight = 365;
		var popupOption= "width="+winWidth+", height="+winHeight + ", left=0, top=0, scrollbars=no, resizable=no, location=no"; 	
		//var pop = window.open(pop_url,"detail",popupOption);
		var pop = window.open(pop_url,id,popupOption);
		/* popList.push(pop);
    	sessionUpdate(); */
		
		//pop.check();
		
		var newForm = document.createElement('form');
		newForm.method='POST';
		newForm.action=pop_url;
		newForm.name='newForm';
		//newForm.target='detail';
		newForm.target=id;
		
		var input_id = document.createElement('input');
		input_id.setAttribute('type','hidden');
		input_id.setAttribute('name','id');
		input_id.setAttribute('value',id);
		
		var input_tid = document.createElement('input');
		input_tid.setAttribute('type','hidden');
		input_tid.setAttribute('name','tid');
		input_tid.setAttribute('value',tid);
		
		var input_ap = document.createElement('input');
		input_ap.setAttribute('type','hidden');
		input_ap.setAttribute('name','ap_no');
		input_ap.setAttribute('value',ap_no);
		
		newForm.appendChild(input_id);
		newForm.appendChild(input_tid);
		newForm.appendChild(input_ap);
		document.body.appendChild(newForm);
		newForm.submit();
		
		document.body.removeChild(newForm);
	} else {
		getLowPath(id, tid, ap_no);
	}
}

function gridClick(e, id){
	var e = e || window.event;
	var target = e.target || e.srcElement;
	
	if($("#gridChk_" + id).is(":checked")){ 
		$("#targetGrid").jqGrid('setCell', target.value, 'LEVEL', "0");
	}else{
		$("#targetGrid").jqGrid('setCell', target.value, 'LEVEL', "1");
	}
}


// ====================== 경로 예외 시작 ======================
function fnOpenExceptionRegist(){
	$.ajax({
		type: "POST",
		url: "${getContextPath}/detection/selectProcessDocuNum",
		async : false,
		datatype: "json",
		success: function (result) {
			$('#title_exception').html("경로예외_" + <fmt:formatDate value="${nowDate}" pattern="yyyyMMdd" /> + "_" + "${memberInfo.USER_NO}" + "_" + result.SEQ);
		}
	});
	
	var idx = 0;
	var aDeletionList = [];
	var aNameList = [];
	var sRowID;
	var hiddenList = [];
	var stausCheck = "";
	var aDeletionData = $("#targetGrid").jqGrid('getGridParam', 'data');

	for (var i = 0; i < aDeletionData.length; i++) {
		var level = aDeletionData[i].LEVEL;
		var chkold = aDeletionData[i].CHKOLD;
		if (level != chkold) {
			var aDeletionAssign = {};
			aDeletionAssign.ID = aDeletionData[i].ID;
			aDeletionAssign.PATH = aDeletionData[i].PATH;
			aDeletionAssign.APPROVAL_STATUS_PRINT_NAME = aDeletionData[i].APPROVAL_STATUS_PRINT_NAME;
			aDeletionList[idx++] = aDeletionAssign;
		}
	}

	var pathEx = "";
	for (var i = 0; i < aDeletionList.length; i += 1){
		pathEx += aDeletionList[i].PATH + "\n";
	}
	
	$("#exceptPath").val(pathEx); 

    $("#exceptionReasonPopup").show();
    $("#inputExcepPath").focus();
}
//====================== 경로 예외 끝 ======================
$("#exceptionRadio").click(function(){
	fnOpenExceptionRegist();
	$("#exceptionReasonPopup").show();
});

// 경로 담당자 변경
$("#accountRadio").click(function(){
	$("#insertExcelPopup").show(); 
});

$("#btnCancleExcelPopup").click(function(){
	$("#importExcel").val("");
	$("#importExcelNm").val("");
	
	var details = "";
	$("#insertExcelBtn").html(details);
	$("#import_targetUserList_excel").html(details);
	$("#insertExcelPopup").hide();
});

$("#btnNewPopupExcelCencel").click(function(){
	$("#importExcel").val("");
	$("#importExcelNm").val("");
	
	var details = "";
	$("#insertExcelBtn").html(details);
	$("#import_targetUserList_excel").html(details);
	$("#insertExcelPopup").hide();
});

$("#clickimportBtn").click(function(){
  $("#importExcel").click();
});

$("#importExcel").change(function(){
	var checkFileNm = $("#importExcel").val();
	var filelength = checkFileNm.lastIndexOf('\\');
	var fileNm = checkFileNm.substring(filelength+1, checkFileNm.length);
	var resulList = [];
	
	var CNT = 0;
	let input = event.target;
	let reader = new FileReader();
	reader.onload = function () {
		let data = reader.result;
		let workBook = XLSX.read(data, { type: 'binary' });
		var insertCellRow = 0;
		workBook.SheetNames.forEach(function (sheetName) {
			let rows = XLSX.utils.sheet_to_json(workBook.Sheets[sheetName]);
			var details = "";
			
			var user_no = "";
			var insa_code = "";
			var firstRowChk = false;
			
			details += "<tr height=\"45px;\" >";
			details +=	"<th style=\"width: 50px;\">no</th>";
			details +=	"<th style=\"width: 92px;\">호스트명</th>";
			details +=	"<th style=\"width: 200px;\">경로</th>"; 
			details +=	"<th style=\"width: 69px;\">담당자 사번</th>";
			details += "</tr>";
			
			var errorCnt = 0;
			// 필수 값만 확인
			if(rows[0].hasOwnProperty('hash_id') && rows[0].hasOwnProperty('호스트명')){
				$.each(rows, function(index, item) {
										
					var userNo = item.담당자; 
					if(userNo != null ){
						++insertCellRow;
						var changeHashId = item.hash_id;	
						var changePath = item['경 로'];	
						var changeHostName = item.호스트명;	
						
						details += "<tr style=\"height: 45px;\">";   
						details += "	<td style=\"text-align: center; padding-left: 0;\">"+insertCellRow+"</td>";
						details += "	<td style=\"text-align: center; padding-left: 0;\">"+changeHostName+"</td>";
						details += "	<td style=\"text-align: left; padding-left: 0;\">"+changePath+"</td>";
						details += "	<td style=\"text-align: center; padding-left: 0;\">"+userNo+"</td>";
						
						resulList.push({"hostName" : changeHostName, "hashId" : changeHashId, "mngrKey" : 'server_33', "userNo" : userNo})
					}
				});
				
				$("#import_targetUserList_excel").html(details);
	      		
	 	    	var btnCss ="<button type=\"button\" id=\"btnNewPopupExcelSave\" style=\"margin-right: 5px\" >등록</button>";
	 				btnCss +="<button type=\"button\" id=\"btnNewPopupExcelCencel\" >취소</button>";
	 				
 				$("#insertExcelBtn").html(btnCss)
  			
	  			$("#btnNewPopupExcelCencel").click(function(e){
	  				$("#importExcel").val("");
	  				$("#importExcelNm").val("");
	  				
	  				var details = "";
	  				$("#insertExcelBtn").html(details);
	  				$("#import_targetUserList_excel").html(details);
	  				$("#insertExcelPopup").hide();
	          	});
	      	}else {
	      		alert("올바른 형식의 엑셀이 아닙니다. 확인 후 다시 시도해 주세요.");
	      		return;
	      	}
			
			$("#import_targetUserList_excel").html(details);
			$("#importExcelNm").val(fileNm);
			
			$('#btnNewPopupExcelSave').on('click', function(){
				var message = ("아래 경로들을 담당자로 지정하시겠습니까?");
              	
  				if (confirm(message)) {
  					$.ajax({
          				type: "POST",
          				url: "/target/insertExcelPathAccountUser",
          				async : false,
          				data : {
          					"resulList": JSON.stringify(resulList)
          				},
          			    success: function (resultMap) {
          			    	if(resultMap.resultCode == 0) {
          			    		alert(resultMap.resultMeassage);
          			    		
          			    		$("#importExcel").val("");
          						$("#importExcelNm").val("");
          						
          						var details = "";
          						$("#insertExcelBtn").html(details);
          						$("#import_targetUserList_excel").html(details);
          						$("#insertExcelPopup").hide();
          						
          						setServer(); 
          			    	}else {
          			    		alert(resultMap.resultMeassage);
          			    	}
          			    },
          			    error: function (request, status, error) {
          			        console.log("ERROR : ", error);
          			    }
          			});
  				}
			});
      })
  };
  reader.readAsBinaryString(input.files[0]);
});
	
$("#btnCancleExceptionReason, #btnExceptionPopupCancel").click(function(){
	$("#exceptionReasonPopup").hide();
	
    $("input:radio[name=reason]").prop("checked",false);
    $("#exceptServer tr").remove();
    $("#exceptPath tr").remove();
    $("#note").val('');
    
    $("#inputExceptPath").val('');
    $("#exceptionReasonPopup #selectedExcepServer").html('');
});


function pathAdd(e){
	if (e.keyCode != 13) return;
	fnLocationAdd();
} 
function fnLocationAdd() {
	
    if (isNull($("#inputExceptPath").val())){
    	alert("예외 처리할 경로를 입력해주세요.");
    	return;
    }

    var exceptPath = $("#inputExceptPath").val();
    var sTag = "";
    //$("#exceptContent").css("border","1px solid #cdcdcd");

    sTag += "<tr style='border-bottom:1px solid #cdcdcd'>";
    sTag += "    <th style='padding:2px; background: #ffffff; overflow:hidden; text-align:left; border-bottom:1px solid #cdcdcd'>" + exceptPath + "</th>";
    sTag += "    <td style='padding:5px 2px; background: #ffffff; height:23px; width:30px; border-bottom:1px solid #cdcdcd'>";
    //sTag += "        <input type='button' value='X' name='button' style=' border:0 none; background-color:white; cursor:pointer; float:center; height:23px;' onclick='fnLocationRemove(this);'>";
    /*<img class="CancleImg" id="btnCancleTrueDeletionRegistPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">*/
    sTag += "        <img class ='' src='${pageContext.request.contextPath}/resources/assets/images/cancel.png' style='width:40%; display:inline; cursor:pointer;' onclick='fnLocationRemove(this);'>";
    sTag += "    </td>";
    sTag += "</tr>";

    $("#exceptPath").append(sTag);
    $("#inputExceptPath").val("");
};

$("#btnServerSelectPopup").click(function(e) {
    
	selectHost(0);
});


function selectHost(typeChk) {
	var pop_url = "${getContextPath}/popup/exceptionHostList";
	var id = "reportGroupList"
	var winWidth = 700;
	var winHeight = 605;
	var popupOption= "width="+winWidth+", height="+winHeight + ", left=0, top=0, scrollbars=no, resizable=no, location=no"; 	
	//var pop = window.open(pop_url,"lowPath",popupOption);
	var pop = window.open(pop_url,id,popupOption);
	/* popList.push(pop);
	sessionUpdate(); */
	
	//pop.check();
	
	var newForm = document.createElement('form');
	newForm.method='POST';
	newForm.action=pop_url;
	newForm.name='newForm';
	//newForm.target='lowPath';
	newForm.target=id;
	
	var data = document.createElement('input');
	data.setAttribute('type','hidden');
	data.setAttribute('name','typeChk');
	data.setAttribute('value',typeChk);
	
	newForm.appendChild(data);
	document.body.appendChild(newForm);
	newForm.submit();
	
	document.body.removeChild(newForm);
}
function resetTrue(){
	
	$("#path_detail_true").val("");
	$("#trueDeletionAction option:eq(0)").prop("selected", true);
	$(".true_processing_comment span").html("${trueList[0].BOTTOM_TEXT}");
	$("#addTrueProcessing").html("");
	var addTrueProcessing = "";
	if("${trueList[0].COMMENT_ENABLE}"==1){
		addTrueProcessing = "${trueList[0].COMMENT_TITLE}" + "<input type='text' class='edt_sch' id='comment_content' placeholder='${trueList[0].COMMENT_TEXT}' style='width: 270px; margin-left: 5px; padding-left: 5px'>";
	}
	if("${trueList[0].DATE_ENABLE}"==1) $("#selectDateTrue").css('display','inline-block');
	else  $("#selectDateTrue").css('display','none');
	$("#addTrueProcessing").html(addTrueProcessing);
    $("#trueDeletionRegistPopup").hide();
    $("#comment_content").html("");
}

function resetFalse(){
	
	$("#path_detail_false").val("");
	$("#falseDeletionAction option:eq(0)").prop("selected", true);
	$(".false_processing_comment span").html("${falseList[0].BOTTOM_TEXT}");
	$("#addFalseProcessing").html("");
	var addFalseProcessing = "";
	if("${falseList[0].COMMENT_ENABLE}"==1){
		addFalseProcessing = "${falseList[0].COMMENT_TITLE}" + "<input type='text' class='edt_sch' id='comment_content_false' placeholder='${falseList[0].COMMENT_TEXT}' style='width: 270px; margin-left: 5px; padding-left: 5px'>";
	}
	if("${falseList[0].DATE_ENABLE}"==1) $("#selectDateFalse").css('display','inline-block');
	else  $("#selectDateFalse").css('display','none');
	$("#addFalseProcessing").html(addFalseProcessing);
    $("#falseDeletionRegistPopup").hide();
	$("#comment_content_false").html("");
	
}

$("#btnExceptionSave").on("click", function(e) {
    fnSaveExceptionRegist();
});

function fnSaveExceptionRegist() {
	
	// 사유 미선택시 진행 불가
	if(!$("#exceptionReasonPopup input:radio[name=reason]").is(":checked")) {
		alert("사유를 선택하세요.");
		$('input[name="reason"]').focus();
		return false;
	}
	// 사유 변수선언 및 가져오기
	var selectedRadio = $("#exceptionReasonPopup input:radio[name=reason]:checked").val();
	var sPathExFlag = selectedRadio;
	
	//적용대상 서버 가져오기
	var aServerEx = [];
	var aPathExScope = [];
	$(".server_Target_Id").each(function (i, server){ // 이미 선택된 서버 지우기
		aServerEx.push($(server).val());
	});
	if(aServerEx.length==0){
		var grade = ${memberInfo.USER_GRADE};
		if(grade==9){
			alert("서버를 선택하지 않을 경우 모든 서버에 적용됩니다.");
		}else{
			alert("적용대상 서버를 선택해주세요.");
			return;
		}
	
	}
	$("#exceptServer").find("tr").each(function(index, item) {
		aServerEx.push($(item).find(".server_Target_Id").val()); //타겟아이디
		aPathExScope.push($(item).text().trim());//호스트 네임
	});
	// 예외경로 입력 변수 선언 및 가져오기
	var aPathEx = [];
	
	var exceptPath = $("#exceptPath").val();
	if(!isNull(exceptPath)) exceptPath = exceptPath.split("\n");

	for (var i = 0; i < exceptPath.length; i++) {
		if (isNull(exceptPath[i])) continue;
		aPathEx.push(exceptPath[i]);
	}
	
	// 입력된 예외경로 없을시 진행 불가
	if (aPathEx.length <= 0) {
        alert("예외처리 경로를 입력하세요");
        $("#exceptPath").focus();
        return false;
    }
	
    var sChargeId;
    
    var pashExFlagNm = "";
   
    var exceptionList = '${exceptionList}'.split('[{').join('').split('}]').join('');
    	exceptionList = exceptionList.split('}, {');
    	
    for(var f=0 ; f < exceptionList.length ; f++){  
    	
    	var flagRow = exceptionList[f].split(', ');
    	var flag_idx = flagRow[2].split('PROCESSING_FLAG=').join(''); 
    	var flag_name = flagRow[3].split('PROCESSING_FLAG_NAME=').join(''); 
    	
    	if(sPathExFlag == flag_idx) {
    		pashExFlagNm = flag_name;
    		break;
    	}
    }
    
    
    var confirmCheck = confirm('선택하신 내용을 예외 처리 하시겠습니까?');
    if(!confirmCheck) return;
    
    
    $('#title_exception').html("경로예외("+pashExFlagNm+")_" + <fmt:formatDate value="${nowDate}" pattern="yyyyMMdd" /> + "_" + "${memberInfo.USER_NO}" + "_");
    var sChargeNm = document.getElementById('title_exception').innerHTML;
    var oPostDt = {};
    oPostDt["path_ex"] = aPathEx; // 경로
    oPostDt["path_ex_flag"] = sPathExFlag; // 사유
    oPostDt["path_ex_group_name"] = sChargeNm; // 문서명
    oPostDt["path_ex_scope"] = aPathExScope; // 호스트 명
    oPostDt["target_id"] = aServerEx; // target_id
    oPostDt["NOTE"] = $("#note").val(); // 구분 사항
    oPostDt["approval_type"]		   = 2;
    
    var oJson = JSON.stringify(oPostDt);
    
    $.ajax({

        type: "POST",
        url: "${getContextPath}/detection/registPathException",
        async: false,
        data: oJson,
        contentType: 'application/json; charset=UTF-8',
        success: function (result) {
            if (result.resultCode != 0) {
                alert("예외처리 등록을 실패 하였습니다.");
                return;
            }

            $("#targetGrid").setGridParam({
                url: "${getContextPath}/manage/selectFindSubpath", 
                postData: $("#targetGrid").getGridParam('postData'),
                datatype: "json",
                treedatatype: 'json'
            }).trigger("reloadGrid");

            alert("예외처리를 등록 하였습니다.");
            $("#exceptionReasonPopup").hide();
            
            $("input:radio[name=reason]").prop("checked",false);
            $("#exceptServer tr").remove();
            $("#exceptPath tr").remove();
            $("#note").val('');
            
            $("#inputExceptPath").val('');
            $("#exceptionReasonPopup #selectedExcepServer").html('');
            
        },
        error: function (request, status, error) {
            alert("예외처리 등록을 실패 하였습니다.");

            var tr = $("#excepPath").children();
            tr.remove();

            $("input:radio[name=reason]").prop("checked",false);
            $("#selectedTargetId").val("");
            $("#inputExceptPath").val('');
            
            $("#exceptServer tr").remove();
            $("#exceptPath tr").remove();
            $("#note").val('');

            aTargetId = [];
        }
    });

    $("#resultTrue").hide();
    var tr = $("#excepPath").children();
    tr.remove();

    $("input:radio[name=reason]").prop("checked",false);
    $("input:radio[name=serverChk]").prop("checked",false);
    $("#selectedTargetId").val("");
    $("input:radio[name=serverChk]").val("");

    aTargetId = [];
}

//경로예외 예외 처리 추가된 Row 삭제
function fnLocationRemove(element) {
    var excepPathRmv = $(element).parent("td").parent("tr")[0];
    $(excepPathRmv).remove();
}

function fnServerRemove(element){
	var exceptServerRmv = $(element).parent("td").parent("tr")[0];
	$(exceptServerRmv).remove();
}

function detailExcelDwonload(workbook, name, data) {
	
	name = data.HostName;
	
	console.log("sheet add :: ");
	console.log(data);
	var result = 0;
	var pattern = '${pattern}'.split('[{').join('').split('}]').join('');
	pattern = pattern.split('}, {');
	
	var patternCnt = '${patternCnt}';
	
	var sheet =  workbook.addWorksheet(name);
	var headers = [];
	
	var path = [];
	var pattern = [];
	var detection = [];
	var detection_detail = [];
	 
	headers.push({header: '경로', key: 'path', width: 20, style: { font: {size: 10}, alignment: {horizontal: 'center'}}});
	headers.push({header: '호스트', key: 'host', width: 20, style: { font: {size: 10}, alignment: {horizontal: 'center'}}});
	headers.push({header: '개인정보 유형', key: 'pattern', width: 25, style: {font: {size: 10}}});
	headers.push({header: '검출 내용', key: 'detection', width: 35, style: {font: {size: 10}, alignment: {horizontal: 'center'}}});
	headers.push({header: '검출 상세 내용', key: 'detection_detail', width: 50, style: { font: {size: 10}, alignment: {horizontal: 'center'}}});
	
	sheet.getRow(1).fill = {
		type: 'detection',
		pattern:'solid',
		fgColor:{ argb:'ff8080' } 
	}
	sheet.getRow(1).font = {
		size: 10,
        bold: true
    };
	
	sheet.getRow(1).alignment = {
		horizontal: 'center'
    };
	
	var excelRow = [];
	
	sheet.columns = headers;

	console.log("data.resultList" ,data.resultList);
	
	var excelList = data.resultList;
	
	var richText = [];
	
	for(var d = 0; d < excelList.length; d++){ // 현 호스트의 검출 경로 row
		var dataResultList = excelList[d].dataResultList; // 상세 검출 데이터
		var infoResultList = excelList[d].infoResultList; // 검출 데이터 
		var locationResultList = excelList[d].locationResultList; // 검출 데이터 
		
		console.log(excelList[d].path);
		
		for(var r = 0 ; r < dataResultList.length ; r++){
			var dataIdx = dataResultList[r].IDX;
			
			for(var n = 0 ; n < infoResultList.length ; n++){
				richText = [];
				
				var dataInfoIdx = infoResultList[n].IDX;
				var detection_detail_con = infoResultList[n].DATA;
				
				if(dataIdx == dataInfoIdx){ // 상세 데이터 안에 포함 된 데이터인지 확인
					var detection_con  = dataResultList[r].DATA;
					var index = locationResultList[n].Offset + locationResultList[n].LENGTH;
					
					var firstCon = detection_con.substring(0, locationResultList[n].Offset-6);
					 
					 if(firstCon != ""){
		  				   richText.push({ 
		      				   text: firstCon, font: {size : 10}, 
		      			   });
		  			   }
						 richText.push({ 
		  				   text: detection_detail_con, font: {color: {argb: 'ED1C24',theme: 1}, size : 10, bold:true}, 
		  			   });
					
						 detection_con = detection_con.substring((locationResultList[n].Offset+1));
	// 					console.log(detection_con);
					
	// 					 richText.push({ 
	// 	    				   text: detection_con, font: {color: {argb: 'ED1C24',theme: 1}, size : 10 , bold:true}, 
	// 	    			   });
				if(detection_con != ""){ 
					richText.push({ 
					 	text: detection_con, font: {size : 10}, 
					});
				}
					const rowData = {};
					rowData["path"] = excelList[d].path;
					rowData["host"] = name;  
					rowData["pattern"] = infoResultList[n].TYPE;
					rowData["detection"] =  infoResultList[n].DATA;
					rowData["detection_detail"] = dataResultList[r].DATA;
					
					excelRow.push(rowData);
					result = 1;
				}
			
			}  
		}
	}

	if(result == 1){
		sheet.addRows(excelRow);
	}
	return result;
}

function download (workbook, fileName) {
	
	const buffer = new Promise(function (resolve, reject) {
		  resolve(workbook.xlsx.writeBuffer());
	});
	
	/* 비동기 처리를 위한 작업 */
	buffer.then(function(val){
	  	var blob = new Blob([val], {
	    	type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
	    });  
	    //var blob = new Blob(["Hello, world!"], {type: "text/plain;charset=utf-8"});
	    saveAs(blob, fileName + '.xlsx');
	});
};

function reportLoading() {
    var maskHeight = $(document).height();
    var maskWidth  = window.document.body.clientWidth;
    
    var mask       = "<div id='mask' style='position:absolute; z-index:9000; background-color:#000000; display:none; left:0; top:0;'></div>";
    var loadingImg ='';
     
    loadingImg +=" <div id='loadingImg'>";
    loadingImg +=" <img src='${pageContext.request.contextPath}/resources/assets/images/spinner.gif' style='position:absolute; z-index:9500; text-align:center; display:block; top:650%; left:42%;'/>";
    loadingImg += "</div>"; 
  
    $('body').append(mask)
 
    $('#mask').css({ 
            'width' : maskWidth,
            'height': maskHeight,
            'opacity' :'0.3'
    }); 
       
    $('#mask').show(); 
  
    $('.container_header').append(loadingImg);
    $('#loadingImg').show();
}

function reportCloseLoading() {
    $('#mask, #loadingImg').hide();
    $('#mask, #loadingImg').remove(); 
}
/*삭제 및 암호화*/
function fnRegist(flag){
	var cnt = 0;
	$('[name=gridChk]').each(function(i, item){
    	if (item.checked) {
    		cnt++;
        }
    	
    });
	if (cnt == 0) {
        alert("처리 항목을 선택하세요.");
        return;
    }
	console.log(flag);
	var flags = "";
	if(flag=='encrypt'){
		flags = '암호화';
	}else if (flag=='delete'){
		flags = '삭제';
	}else if (flag=='quarantine'){
		flags = '파일이동';
	}
	
	var confirmMessage =  confirm('선택하신 내용을 '+flags+'하시겠습니까?');
	if(!confirmMessage) return;
	
	if(flag == 'delete'){
		fn_registRemdiation(flag);
	}else if (flag=='encrypt') {
		$("#encryptPasswordPopup").show(); 
		$("#encryptPassword").focus();
	}else if(flag=='quarantine'){
		$("#quarantinePopup").show(); 
		$("#quarantinePath").focus();
	}	
}

function fn_registRemdiation(flag){
	var aRegistList = [];
	var aList = [];
	var registFlag = false;
	$('[name=gridChk]').each(function(i, item){
    	if (item.checked) {
    		var registObject = {};
    		
    		var sRowID = $(item).data("rowid");
    		//var remediation_status = $('#targetGrid').getCell(sRowID, 'REMEDIATION_STATUS');
    		var hash_id = $('#targetGrid').getCell(sRowID, 'HASH_ID'); 
    		var target_id = $('#targetGrid').getCell(sRowID, 'TARGET_ID'); 
    		var ap_no = $('#targetGrid').getCell(sRowID, 'AP_NO');
    		
    		//if(remediation_status == 'False Match') {
    		//	falseMatchFlag = true;
    		//}
    		aRegistList.push(item.value);
    		registObject['hash_id'] = hash_id;
    		registObject['target_id'] = target_id;
    		registObject['ap_no'] = ap_no;
    		
    		aList.push(registObject);
        }
    	
    });
	
	if (aRegistList.length == 0) {
        alert("처리 항목을 선택하세요.");
        return;
    }
	/* if(falseMatchFlag){
		alert("이미 오탐 처리된 항목이 있습니다.");
		return;
	} */

	/* console.log(aFalseMatchList); */

	var oPostDt = {};
	oPostDt["aRegistList"]  = aRegistList;
	oPostDt["aList"]        = aList;
	oPostDt["flag"] 		= flag;
	
	
	
	
	if(flag =='encrypt') {
		flags = '암호화';
		var encryptPassword = $("#encryptPassword").val();
		var encryptPasswordConfirm = $("#encryptPasswordConfirm").val();
		if(encryptPassword.trim() =="") {alert("비밀번호를 입력해주세요."); return;}
		if(encryptPassword != encryptPasswordConfirm) {alert("비밀번호 확인이 다릅니다."); return;}
		oPostDt["password"] = encryptPassword;
	}
	
	if(flag =='delete') flags = '삭제';
	
	if(flag =='quarantine'){
		flags = '파일이동';
		var quarantinePath = $("#quarantinePath").val();
		
		if(quarantinePath.trim() =="") {
			alert("경로를 입력해 주세요.");
			return;
		}
		
		oPostDt["quarantine"] = quarantinePath;
	}
	
	
	var oJson = JSON.stringify(oPostDt);
	
	
	if(true){
		$.ajax({
	        type: "POST",
	        url: "${getContextPath}/manage/registRemdiation",
	        async : false,
	        data : oJson,
	        contentType: 'application/json; charset=UTF-8',
	        success: function (result) {
                if (result.resultCode != "0") {
                    alert(result.resultMessage || "처리 등록을 실패 하였습니다.");
     	            if(flag =='encrypt') {
    			        $("#btnCancleEncryptPasswordPopup").click();
    		        }	    	
    	            if(flag =='quarantine'){
    	             	$("#btnQuarantineCancel").click();
    	            }
                    
    	            $("#targetGrid").setGridParam({
    	                url: "${getContextPath}/manage/selectFindSubpath", 
    	                postData: $("#targetGrid").getGridParam('postData'),
    	                datatype: "json",
    	                treedatatype: 'json'
    	            }).trigger("reloadGrid");
                    return;
                }
	
	           alert("처리를 등록 하였습니다.");
	           if(flag =='encrypt') {
			       $("#btnCancleEncryptPasswordPopup").click();
		       }	    	
	           if(flag =='quarantine'){
	        	   $("#btnQuarantineCancel").click();
	           }
		    	
// 	           fnSearchFindSubpath();
	           fnSearchFindSubpath(1, null);
	            $("#targetGrid").setGridParam({
	                url: "${getContextPath}/manage/selectFindSubpath", 
	                postData: $("#targetGrid").getGridParam('postData'),
	                datatype: "json",
	                treedatatype: 'json'
	            }).trigger("reloadGrid");
	        },
	        error: function (request, status, error) {
	        	console.log(status);
	            alert("처리 등록을 실패 하였습니다.");
	
	            return;
	        }
	    });
   	}
}

/* 삭제 및 암호화 끝 */
 function updateProgress() {
	

	$("#report_flag").val("xlsx");
	$("#report_file_name").val("");
	$("#detailExceldownloadPop").hide();
	$("#ExcelDownPopUp").show();
    var $progressElement = $("#excelDownName");
    var dots = 0;

    setInterval(function() {
        dots = (dots % 3) + 1;
        var progressText = "상세 보고서 다운로드 안내" + ".".repeat(dots);
        $progressElement.text(progressText);
    }, 1000);
};

function getFormatDate(oDate) {
	var nYear = oDate.getFullYear(); // yyyy 
	var nMonth = (1 + oDate.getMonth()); // M 
	nMonth = ('0' + nMonth).slice(-2); // month 두자리로 저장 

	var nDay = oDate.getDate(); // d 
	nDay = ('0' + nDay).slice(-2); // day 두자리로 저장

	return nYear + '-' + nMonth + '-' + nDay;
}
function setSelectDate() {
	$("#fromDate").datepicker({
		changeYear : true,
		changeMonth : true,
		dateFormat : 'yy-mm-dd'
	});

	$("#toDate").datepicker({
		changeYear : true,
		changeMonth : true,
		dateFormat : 'yy-mm-dd' 
	});
     
	var oToday = new Date();
	$("#toDate").val(getFormatDate(oToday)); 
   
	var oFromDate = new Date(oToday.setDate(oToday.getDate() - 30));
	$("#fromDate").val(getFormatDate(oFromDate));
}

</script>

</body>
</html>