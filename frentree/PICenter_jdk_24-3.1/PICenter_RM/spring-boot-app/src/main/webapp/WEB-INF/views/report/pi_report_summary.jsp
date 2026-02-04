<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%@ include file="../../include/header.jsp"%>  
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/detailReport.js"></script>
<style>
#btnSearch {
	position: relative;
	top: 10px;
	right: 42px;
}
#reportExtensionDiv tr td {
	height: 35px;
}

</style>

<!-- section -->
<section>
	<!-- container -->
	<div class="container">
		<h3>통합 보고서</h3>
		<!-- content -->
		<div class="content magin_t25">
            <div class="grid_top">
                <table class="user_info narrowTable" style="width: 1315px;">
                    <caption>사용자정보</caption>
                    <tbody>
                        <tr>
                            <!-- <th style="text-align: center; background-color: #d6e4ed; min-width:6vw; width:6vw">호스트</th> -->
                            <th style="text-align: center; width: 100px; border-radius: 0.25rem; padding: 5px 5px 0 5px;">종류</th>
                            <td style="width: 210px; padding: 5px 5px 0 5px;">
                            	<label>서버 </label>
                            </td>  
                            <th style="text-align: center; width: 100px;  padding: 5px 5px 0 5px;">처리구분</th>
                            <td style=" padding: 5px 5px 0 5px;">
                            	<select id="SCH_PROCESSING_FLAG" name="SCH_PROCESSING_FLAG" style="width:186px;">
                                    <option value="" selected>전체</option>
                                    <c:forEach items="${dataProcessingFlagList}" var="dataProcessingFlagList">
                                    	<option value="${dataProcessingFlagList.PROCESSING_FLAG}">${dataProcessingFlagList.PROCESSING_FLAG_NAME}</option>
                                    </c:forEach>
                                    <option value="-1">미처리</option>
                                </select>
                            </td>
                            <th style="text-align: center; width: 100px; padding: 5px 5px 0 5px; border-radius: 0.25rem;">기간별</th>
                            <td style="width: 400px; padding: 5px 5px 0 5px;">
                            	<select id="SCH_DATE" name="SCH_DATE" style="width: 186px; font-size: 12px; padding-left: 5px; text-align: center;">
                                    <option value="0" >최근 검출일</option>
                                    <option value="1" >기안일</option>
                            	</select>
                            </td>
                            <td style="padding: 5px 5px 0 5px;">
                           		<input type="button" name="button" class="btn_look_approval" id="btnSearch">
                           	</td>
                        </tr>
                        <tr>
                        	<th style="text-align: center; width: 100px; border-radius: 0.25rem;">호스트명</th>
                            <td style="width: 255px;">
                            	<input type="text" style="width: 186px; padding-left: 5px;" size="20" id="SCH_TARGET" placeholder="호스트명을 입력하세요." readonly="readonly">
                            	<input type="hidden" style="width: 186px; padding-left: 5px;" size="20" id="SCH_TARGET_ID" placeholder="호스트명을 입력하세요." readonly="readonly">
                            	                                    <input type="hidden" style="width: 186px; display:none;" size="10" id="AP_NO" value="" readonly="readonly">
                            	<button type="button" class="btn_down" id="btnUserSelectClear" style="font-size : 12px; font-weight:0 ; margin-bottom: 0px; padding:0; width:55px !important; height:27px !important">Clear</button>
                            </td>
                            <th style="text-align: center; width: 100px;">그룹명</th>
                            <td style="width: 255px;">
                           		<div id="GROUP_DIV">
	                            	<input type="text" style="width: 186px; padding-left: 5px; padding-right: 7px;" size="10" id="SCH_GROUP" placeholder="그룹을 선택하세요." autocomplete="off" readonly="readonly">
	                            	<input type="hidden" style="width: 186px; display:none;" size="10" id="GROUP_ID" readonly="readonly">
	                            	                                    <input type="hidden" style="width: 186px; display:none;" size="10" id="GROUP_ID_LIST" readonly="readonly">
	                            	<button type="button" class="btn_down" id="btnGroupSelectClear" style="font-size : 12px; font-weight:0 ; margin-bottom: 0px; padding:0; width:55px !important; height:27px !important">Clear</button>
                            	</div>
                            </td>
                            <th style="text-align: center; width: 100px;">
                            </th>
                            <td>
                                <input type="date" id="SCH_FROM_CREDATE" style="text-align:center; width:186px;" readonly="readonly">
                                <span id="SCH_CREDATE_SPAN" style="width: 10%; margin-right: 3px; color: #000;">~</span>
                                <input type="date" id="SCH_TO_CREDATE" style="text-align:center; width:186px;" readonly="readonly">
                            	<input type="date" id="SCH_FROM_D_P_C_G_REGDATE" style="text-align:center; width:186px; display: none;" readonly="readonly">
                            	<span id="SCH_REGDATE_SPAN" style="width: 10%; margin-right: 3px; color: #000; display: none;">~</span>
                                <input type="date" id="SCH_TO_D_P_C_G_REGDATE" style="text-align:center; width:186px; display: none;" readonly="readonly">
                            </td> 
                            <td>
                           		<button type="button" name="button" class="btn_down" id="detailExceldownloadPopUp">다운로드</button> 
                           		
                           		<input type="hidden" style="width: 186px; padding-left: 5px; padding-right: 7px;" size="10" id="sch_group_list" name="sch_group_list" >
                   				<input type="hidden" style="width: 186px; padding-left: 5px; padding-right: 7px;" size="10" id="sch_host_list"  name="sch_host_list">
                           	</td>
                         </tr>
                    </tbody>
                </table>
            </div>
			<!-- <div class="left_box2" style="overflow: hidden; max-height: 632px; height: 632px;"> -->
			<div class="left_box2" style="overflow: hidden; max-height: 627px; height: 627px; margin-top: 10px">
				<table id="targetGrid" style="width:100%"></table>
				<div id="targetGridPager"></div>
			</div>
		</div>
	</div>
	
	
	<!-- 담당자 지정 popup -->
	<div id="detailExceldownloadPop" class="popup_layer" style="display:none;">
	    <div class="popup_box" style="height: 660px; padding: 10px; background: #f9f9f9; left: 50%; top: 44%;">
	        <div class="popup_top" style="background: #f9f9f9;">
	            <h1 style="color: #222; padding: 0; box-shadow: none;">보고서 다운로드 
	            <img alt="" src="${pageContext.request.contextPath}/resources/assets/images/question_icon.png" style="width: 17px; position: absolute; top: 12px; left: 147px;" id="reportQuestionIcon">
	            </h1>
	        </div>
	        <div class="popup_content">
		        <div class="content-box" style="height: 570px; background: #fff; padding: 0;">
		        	<table class="popup_tbl">
		        		<colgroup>
							<col width="20%"> 
							<col width="*">
						</colgroup>
		        		<tbody> 
		        			<tr>   
			        			<th>유형</sth>
		        				<td>
		        					<select id="downLoad_flag" name="downLoad_flag" style="width:250px;">
		        						<c:forEach var="set" items="${setMap}">
											<c:if test="${set.STATUS == 'Y' && set.IDX != 1}">  
							                    <option value="${set.NAME}">${set.SUB_NAME}</option>
											</c:if>
											<c:if test="${set.STATUS == 'Y' && set.IDX == 1}">    
							                    <option value="${set.NAME}" selected>${set.SUB_NAME}</option>
											</c:if>
										</c:forEach>
					                </select>
		        				</td>
		        			</tr>
<!-- 		        			<tr><th></th><td>d</td></tr> -->
		        			<tr>
			        			<th>파일명</sth>
		        				<td>
		        					<input type="text" id="report_file_name" name="report_file_name" style="width:250px;" placeholder=" 파일명 미 기입시 기본 파일명으로 제공됩니다.">
		        				</td>
		        			</tr> 
		        		</tbody>
		        	</table> 
<!-- 		        	구분자 -->
		        	<table class="popup_tbl" style="padding-left: 5%;" id="deatilReportHeader"> 
		        		<colgroup> 
							<col width="20%"> 
							<col width="*">
						</colgroup>
						<tbody id="reportHeaderDate" style="overflow-y: scroll; height: 100px; display: block;">
		        		</tbody>
		        	</table>
<!-- 		        	데이터 -->
		        	<table class="popup_tbl" style="padding-left: 3%;" id="reportExtensionTable">
		        		<colgroup> 
							<col width="20%"> 
							<col width="*">
						</colgroup>
		        		<tbody id="reportExtensiontd" style="overflow-y: scroll; height: 371px; display: block;">
		        		</tbody>
		        	</table>
		        </div>
	        
	        </div>
	        <div class="popup_btn"> 
	            <div class="btn_area"> 
	                <button type="button" id="reportExcelColumnNmSave" style="display: none;">적용</button>
	                <button type="button" id="detailExceldownload">다운로드</button>
	                <button type="button" id="detailExceldownloadCanCel">취소</button>
	            </div>
	        </div>
	    </div>
	</div>
	
	<div id="userSelect" class="popup_layer" style="display:none;">
	    <div class="popup_box" style="height: 200px; padding: 10px; background: #f9f9f9; left: 50%; top: 55%;">
	        <div class="popup_top" style="background: #f9f9f9;">
	            <h1 style="color: #222; padding: 0; box-shadow: none;">담당자 지정</h1>
	        </div>
	        <div class="popup_content">
	            <div class="content-box" style="height: 355px; background: #fff; padding: 0;">
	                <table id="userGrid"></table>
	                <div id="userGridPager"></div>
	            </div>
	        </div>
	        <div class="popup_btn">
	            <div class="btn_area">
	                <button type="button" id="btnUserSelect">선택</button>
	                <button type="button" id="btnUserCancel">취소</button>
	            </div>
	        </div>
	    </div>
	</div>
	<!-- 팝업창 종료 -->
	
	<!-- 팝업창 - 월간 리포트 다운로드 시작 -->
	<div id="monthlyReportPopup" class="popup_layer" style="display:none">
		<div class="popup_box" style="height: 100px; width: 462px; left:54%; top:53%;">
			<div class="popup_top">
				<h1>월간 리포트 다운로드</h1>
			</div>
			<div class="popup_content">
				<div class="content-box" style="height: 100px;">
					<table class="popup_tbl">
						<colgroup>
							<col width="30%">
							<col width="*">
						</colgroup>
						<tbody>
							<tr>
								<th>연월 선택</th>
								<td>
									<jsp:useBean id="now" class="java.util.Date"/>
									<fmt:formatDate value="${now}" pattern="yyyy" var="now_year"/>
									<select id="monthly_year" name="monthly_year">
										<option value="" selected>선택</option>
										<c:forEach items="${monthly_year}" var="yearMap">
										<option value="${yearMap}" <c:if test="${yearMap == now_year}">selected</c:if>>${yearMap}</option>
										</c:forEach>
									</select>년&nbsp;
									<select id="monthly_month" name="monthly_month">
										<option value="1" <c:if test="${monthly_month == '1'}">selected</c:if>>1</option>
										<option value="2" <c:if test="${monthly_month == '2'}">selected</c:if>>2</option>
										<option value="3" <c:if test="${monthly_month == '3'}">selected</c:if>>3</option>
										<option value="4" <c:if test="${monthly_month == '4'}">selected</c:if>>4</option>
										<option value="5" <c:if test="${monthly_month == '5'}">selected</c:if>>5</option>
										<option value="6" <c:if test="${monthly_month == '6'}">selected</c:if>>6</option>
										<option value="7" <c:if test="${monthly_month == '7'}">selected</c:if>>7</option>
										<option value="8" <c:if test="${monthly_month == '8'}">selected</c:if>>8</option>
										<option value="9" <c:if test="${monthly_month == '9'}">selected</c:if>>9</option>
										<option value="10" <c:if test="${monthly_month == '10'}">selected</c:if>>10</option>
										<option value="11" <c:if test="${monthly_month == '11'}">selected</c:if>>11</option>
										<option value="12" <c:if test="${monthly_month == '12'}">selected</c:if>>12</option>
									</select>월&nbsp;
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="popup_btn">
				<div class="btn_area">
					<button type="button" id="btnDownloadMonthly">다운로드</button>
					<button type="button" id="btnCancelMonthly">취소</button>
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
  
</section>
<%@ include file="../../include/footer.jsp"%>

<script type="text/javascript">
// var postData = {target_id : $("#hostSelect").val()};
// var gridWidth = $("#targetGrid").parent().width();
var oGrid = $("#targetGrid");
var pattern = '${pattern}'.split('[{').join('').split('}]').join('');
pattern = pattern.split('}, {');

var processing = "${dataProcessingFlagList}".split('[{').join('').split('}]').join('');
processing = processing.split('}, {');

var patternCnt = '${patternCnt}';

// 서버 컬럼 정보 (관리자, 운영자 등 동적 컬럼)
var serverColumns = ${serverColumnsJson};

$(document).ready(function () {
	
    // 날짜 설정
    setSelectDate();

    // SelectList를 선택하면 선택된 화면으로 이동한다.
    $("#selectList").change(function () {
        location.href = $("#selectList").val();
    });

    $("#btnDownloadExel").on("click", function(){
    	//로딩
 	   var form = document.getElementById("excelDownForm");
 	   
 	   if($("#SCH_FROM_CREDATE").val() > $("#SCH_TO_CREDATE").val()){
 			alert("입력한 끝 날짜가 시작 날짜 보다 빠릅니다.");
 			return;
 		}
 		// 기안일
 		if($("#SCH_FROM_D_P_C_G_REGDATE").val() > $("#SCH_TO_D_P_C_G_REGDATE").val()){
 			alert("입력한 끝 날짜가 시작 날짜 보다 빠릅니다.");
 			return;
 		}
 		
 	   Loading2();
 	   
 	   if($("#SCH_DATE").val() == 0 ){ // 검출일
 		   
 		   var oPostDt = {};
 		   var group_list = $("#sch_group_list").val().split(",");
 		   var host_list = $("#sch_host_list").val().split(",");
 		 
 		   if($("#SCH_DATE").val() == 0 ){ // 검출일
 				oPostDt["sch_SDATE"] = $("#SCH_FROM_CREDATE").val();
 				oPostDt["sch_EDAT"] = $("#SCH_TO_CREDATE").val();
 				
 				$("#sch_SDATE").val($("#SCH_FROM_CREDATE").val());
 				$("#sch_EDAT").val($("#SCH_TO_CREDATE").val());
 		   }else if($("#SCH_DATE").val() == 1 ){ // 기안일
 				oPostDt["sch_SDATE"] = $("#SCH_FROM_D_P_C_G_REGDATE").val();
 				oPostDt["sch_EDAT"] = $("#SCH_TO_D_P_C_G_REGDATE").val();
 				
 			   $("#sch_SDATE").val($("#SCH_FROM_D_P_C_G_REGDATE").val());
 			   $("#sch_EDAT").val($("#SCH_TO_D_P_C_G_REGDATE").val());
 		   }
 		   
 			oPostDt["SCH_DATE"] = $("#SCH_DATE").val();
 			oPostDt["tid"] = $("#sch_host_list").val();
 			oPostDt["gid"] = $("#sch_group_list").val();

 			var jsonData = JSON.stringify(oPostDt);
 			var excelDownloadState = false;
 			
 	//		dataExcel();
 			
 			setTimeout(function(){
 				 $.ajax({
 					type: "POST",
 					url: "${getContextPath}/report/getExcelDownCNT",
 					async: false,
 					data: jsonData,
 					datatype: "json",
 					contentType: "application/json; charset=UTF-8",
 					success: function (result){
 						
 						 if(result.rowLength <= 0){
 							alert("데이터가 없습니다.");
 							closeLoading2();
 							return;
 						}
 						
 					 	var form = document.createElement("form");
 						form.setAttribute("method", "POST");  //Post 방식
 						form.setAttribute("action", "/report/excelDown"); //요청 보낼 주소
 						form.setAttribute("id", "result_api"); //요청 보낼 주소
 						form.setAttribute("onsubmit", "return false;");
 						
 						var hiddenAPI = document.createElement("input");
 						hiddenAPI.setAttribute("type", "hidden");
 						hiddenAPI.setAttribute("name", "detailFileName");
 				        hiddenAPI.setAttribute("value",result.fileName );
 				        form.appendChild(hiddenAPI);
 				        
 				        document.body.appendChild(form);
 				         
 				        form.submit();
 					    closeLoading2();
 				         
 				        document.body.removeChild(form);  
 						 //로딩 해제
 					},
 					error: function (request, status, error) {
 						//alert('데이터가 없습니다.');
 						alert("엑셀 파일 생성에 실패하였습니다.");
 						closeLoading2();
 						//로딩 해제
 					}
 				});
 				 
 				 
 			},1000);
 		  
 	   }   	
     });
    $("#btnDownloadMonthlyExcel").on("click", function(){
    	$('#monthlyReportPopup').show();
    });
    
    $("#btnDownloadMonthly").on("click", function(){
    	downLoadMonthlyReports();
    });

    $("#btnCancelMonthly").on("click", function(){
    	$('#monthlyReportPopup').hide();
    });
    
    $("#btnSearch").on("click", function(){
    	fn_search();
    });
    
    $('#SCH_TARGET').keyup(function(e) {
		if (e.keyCode == 13) {
			fn_search();
	    }        
	});
    
    $("#SCH_DATE").change(function() {
    	if($("#SCH_DATE option:selected").val() == 1){
    		$("#SCH_FROM_CREDATE").css('display', 'none');
    		$("#SCH_CREDATE_SPAN").css('display', 'none');
    		$("#SCH_TO_CREDATE").css('display', 'none');
    		$("#SCH_FROM_D_P_C_G_REGDATE").css('display', 'inline-block');
    		$("#SCH_REGDATE_SPAN").css('display', 'inline');
    		$("#SCH_TO_D_P_C_G_REGDATE").css('display', 'inline-block');
    	}else {
    		$("#SCH_FROM_CREDATE").css('display', 'inline-block');
    		$("#SCH_CREDATE_SPAN").css('display', 'inline');
    		$("#SCH_TO_CREDATE").css('display', 'inline-block');
    		$("#SCH_FROM_D_P_C_G_REGDATE").css('display', 'none');
    		$("#SCH_REGDATE_SPAN").css('display', 'none');
    		$("#SCH_TO_D_P_C_G_REGDATE").css('display', 'none');
    	}
    });
    
	// 조회조건 - 담당자 Find button click event
    $("#btnUserSelectPopup").click(function(e) {

        $("#userSelect").show();

        if ($("#userGrid").width() == 0) {
            $("#userGrid").jqGrid({
                url: "${getContextPath}/detection/selectTeamMember",
                datatype: "json",
                data: JSON.stringify({ type: "change" }),
                contentType: 'application/json; charset=UTF-8',

                mtype : "POST",
                ajaxGridOptions : {
                    type    : "POST",
                    async   : true
                },
                colNames:['팀명','담당자','직책', '사번'],
                colModel: [
                	{ index: 'OFFICE_NM',   name: 'OFFICE_NM',  width: 180, align: 'center' },
                	{ index: 'USER_NAME',   name: 'USER_NAME',  width: 180, align: 'center' },
                    { index: 'JIKGUK',      name: 'JIKGUK',     width: 180, align: 'center' },
                    { index: 'USER_NO',     name: 'USER_NO',    width: 180, align: 'center', hidden: true }
                ],
                id: "USER_NO",
                loadonce:true,
                viewrecords: true,
                width: 600,
                height: 280,
                autowidth: true,
                shrinkToFit: true,
                loadonce: true,
                rownumbers : false,
                rownumWidth : 75,   
                rowNum:25,
                rowList:[25,50,100],
                pager: "#userGridPager",
                onSelectRow : function(rowid,celname,value,iRow,iCol) { 
                },
                afterEditCell: function(rowid, cellname, value, iRow, iCol){
                },
                afterSaveCell : function(rowid,name,val,iRow,ICol){
                },
                afterSaveRow : function(rowid,name,val,iRow,ICol){
                },
                ondblClickRow: function(rowid,iRow,iCol) {
                    var user_name = $(this).getCell(rowid, 'USER_NAME'); 
                    var jikguk = $(this).getCell(rowid, 'JIKGUK'); 
                    var user_no = $(this).getCell(rowid, 'USER_NO');
                    
                    $("#SCH_OWNER").val(user_no);
                    $("#SCH_OWNER_NM").val(user_name + " " + jikguk);
                    
                    $("#userSelect").hide();
                },
                loadComplete: function(data) {
                },
                gridComplete : function() {
                }
            }).filterToolbar({
                  autosearch: true,
                  stringResult: true,
                  searchOnEnter: true,
                  defaultSearch: "cn"
            }); 

            $("#userGridPager_left").css("width", "10px");
            $("#userGridPager_right").css("display", "none");
        }
        else {
            $("#userGrid").setGridParam({
                url:"${getContextPath}/detection/selectTeamMember", 
                datatype:"json"
            }).trigger("reloadGrid");
        }
    });
    
    // 담당자 지정 popup - 선택 button click event
    $("#btnUserSelect").on("click", function(e) {
        var rowid = $("#userGrid").getGridParam("selrow");
        if(rowid == "" || rowid == null || rowid == "undefined" || rowid < 0) {
        	alert("담당자를 선택하십시오");
        	return false;
        }
        
        var user_name = $("#userGrid").getCell(rowid, 'USER_NAME'); 
        var jikguk = $("#userGrid").getCell(rowid, 'JIKGUK'); 
        var user_no = $("#userGrid").getCell(rowid, 'USER_NO');
		
        $("#SCH_OWNER").val(user_no);
        $("#SCH_OWNER_NM").val(user_name + " " + jikguk);
        
        $("#userSelect").hide();
    });
    
	// 담당자 지정 popup - 취소 button click event
    $("#btnUserCancel").on("click", function(e) {
        $("#SCH_OWNER").val('');
        $("#SCH_OWNER_NM").val('');
        $("#userSelect").hide();
    });
	
    // 조회조건 - Clear button click event
    $("#btnGroupSelectClear").on("click", function(e) {
        $("#SCH_GROUP").val('');
        $("#GROUP_ID").val('');
        $("#AP_NO").val('');
    });
    
    $("#btnUserSelectClear").on("click", function(e) {
        $("#SCH_TARGET").val('');
        $("#SCH_TARGET_ID").val('');
    });
	
    loadJqGrid();
    
    $("#report_flag").change(function(){
    	reportSelectBoxOption();
    	
    	
	});
    $("#downLoad_flag").change(function(){
    	reportSelectBoxOption();
    	
	});
});


  
function loadJqGrid()
{
	var oPostDt = {};
	oPostDt["owner"]    = $("#schOwner").val();
	oPostDt["filename"] = $("#schFilename").val();
	oPostDt["fromDate"] = $("#fromDate").val();
	oPostDt["toDate"]   = $("#toDate").val();

	// 동적 colNames 생성
	var colNames = ['GROUP_ID', 'TARGET_ID', 'HASH_ID', 'AP_NO', 'ACCOUNT', 'OS', '서버명', 'IP', '경로', '코멘트', '그룹명', '최근 검출일', '사번'];
	// 동적 컬럼 추가 (관리자, 운영자 등)
	for(var i = 0; i < serverColumns.length; i++) {
		if(serverColumns[i] && serverColumns[i].NAME) {
			colNames.push(serverColumns[i].NAME);
		}
	}
	// 나머지 컬럼 추가
	colNames = colNames.concat(['주민등록번호', '외국인등록번호', '여권번호', '운전면허번호', '계좌번호', '카드번호', '이메일', '휴대폰번호', '총개수',
		'최초 검출일', '삭제일', '기안일', '기안자NO', '기안자', '결재일', '결재자NO', '결재자', '개인정보 여부(정탐 오탐) 코드', '점검 결과',
		'메모내용(문서 메모내용)', '처리구분', '조치예정일']);

	// 동적 colModel 생성
	var colModel = [
		{ index: 'GROUP_ID',				name: 'GROUP_ID',				width: 100,		align: 'left',	hidden:true},
		{ index: 'TARGET_ID',				name: 'TARGET_ID',				width: 100,		align: 'left',	hidden:true},
		{ index: 'HASH_ID',					name: 'HASH_ID',				width: 100,		align: 'left',	hidden:true},
		{ index: 'AP_NO',					name: 'AP_NO',					width: 100,		align: 'left',	hidden:true},
		{ index: 'ACCOUNT',					name: 'ACCOUNT',				width: 100,		align: 'left',	hidden:true},
		{ index: 'PLATFORM',				name: 'PLATFORM',				width: 110,		align: 'center'},
		{ index: 'TARGET_NAME',				name: 'TARGET_NAME',			width: 140,		align: 'center'},
		{ index: 'IP',						name: 'IP',						width: 100,		align: 'center'},
		{ index: 'PATH',					name: 'PATH',					width: 450,		align: 'left'},
		{ index: 'COMMENTS',				name: 'COMMENTS',				width: 400,		align: 'left',	hidden:true},
		{ index: 'OFFICE_NM',				name: 'OFFICE_NM',				width: 130,		align: 'center'},
		{ index: 'REGDATE',					name: 'REGDATE',				width: 150,		align: 'center'},
		{ index: 'USER_ID',					name: 'USER_ID',				width: 100,		align: 'left',	hidden:true}
	];
	// 동적 컬럼 추가 (관리자, 운영자 등)
	for(var i = 0; i < serverColumns.length; i++) {
		if(serverColumns[i] && serverColumns[i].NAME) {
			colModel.push({ index: serverColumns[i].NAME, name: serverColumns[i].NAME, width: 100, align: 'center', sortable: true });
		}
	}
	// 나머지 컬럼 추가
	colModel = colModel.concat([
		{ index: 'TYPE1',					name: 'TYPE1',					width: 70,		align: 'right', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: ''}, sorttype: 'number'},
		{ index: 'TYPE2',					name: 'TYPE2',					width: 80,		align: 'right', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: ''}, sorttype: 'number' },
		{ index: 'TYPE3',					name: 'TYPE3',					width: 70,		align: 'right', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: ''}, sorttype: 'number' },
		{ index: 'TYPE4',					name: 'TYPE4',					width: 70,		align: 'right', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: ''}, sorttype: 'number' },
		{ index: 'TYPE5',					name: 'TYPE5',					width: 70,		align: 'right', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: ''}, sorttype: 'number' },
		{ index: 'TYPE6',					name: 'TYPE6',					width: 70,		align: 'right', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: ''}, sorttype: 'number' },
		{ index: 'TYPE7',					name: 'TYPE7',					width: 70,		align: 'right', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: ''}, sorttype: 'number' },
		{ index: 'TYPE8',					name: 'TYPE8',					width: 70,		align: 'right', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: ''}, sorttype: 'number' },
		{ index: 'TYPE',					name: 'TYPE',					width: 90,		align: 'right', formatter:'integer', formatoptions:{thousandsSeparator: ",", defaultValue: ''}, sorttype: 'number' },
		{ index: 'CREDATE',					name: 'CREDATE',				width: 170,		align: 'center'},
		{ index: 'DELDATE',					name: 'DELDATE',				width: 170,		align: 'center',	hidden:true},
		{ index: 'D_P_C_G_REGDATE',			name: 'D_P_C_G_REGDATE',		width: 170,		align: 'center'},
		{ index: 'ACCOUNT_USER_NO',			name: 'ACCOUNT_USER_NO',		width: 100,		align: 'left',	hidden:true},
		{ index: 'ACCOUNT_USER_NM',			name: 'ACCOUNT_USER_NM',		width: 80,		align: 'center'},
		{ index: 'OKDATE',					name: 'OKDATE',					width: 170,		align: 'center'},
		{ index: 'OKUSER_NO',				name: 'OKUSER_NO',				width: 100,		align: 'left',	hidden:true},
		{ index: 'OK_ACCOUNT_USER_NM',		name: 'OK_ACCOUNT_USER_NM',		width: 80,		align: 'center'},
		{ index: 'PROCESSING_FLAG',			name: 'PROCESSING_FLAG',		width: 100,		align: 'left',	hidden:true},
		{ index: 'PROCESSING_FLAG_TYPE',	name: 'PROCESSING_FLAG_TYPE',	width: 75,		align: 'center', hidden: true },
		{ index: 'D_P_C_G_REASON',			name: 'D_P_C_G_REASON',			width: 200,		align: 'left', hidden: true },
		{ index: 'PROCESSING_FLAG_NAME',	name: 'PROCESSING_FLAG_NAME',	width: 100,		align: 'center'},
		{ index: 'D_P_G_NEXT_DATE_REMEDI',	name: 'D_P_G_NEXT_DATE_REMEDI',	width: 170,		align: 'center', hidden: true}
	]);

	oGrid.jqGrid({
		//url: "${getContextPath}/report/searchSummaryList",
		datatype: "json",
	   	mtype : "POST",
	   	postData: oPostDt,
	   	contentType:"application/x-www-form-urlencoded; charset=utf-8",
	   	ajaxGridOptions : {
			type    : "POST",
			async   : true
		},
		colNames: colNames,
		colModel: colModel,
		loadonce : true,
	   	autowidth: true,
        viewrecords: true,
        width: $("#targetGrid").parent().width(),
        height: 550,    
        shrinkToFit: false,
        pager: "#targetGridPager",
        rownumbers : false,
        rownumWidth : 75,  
        jsonReader : {
            id : "ID"
        },
        rowNum:1000,
		rowList:[1000,2000,2500,5000]
	});
}


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

/* function downLoadExcel()
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
        fileName :  "통합_보고서_" + today + ".csv",
        mimetype : "text/csv; charset=utf-8",
        returnAsString : false
    })
} */
function Loading2() {
    var maskHeight = $(document).height();
    var maskWidth  = window.document.body.clientWidth;
     
    var mask       = "<div id='mask' style='position:absolute; z-index:9000; background-color:#000000; display:none; left:0; top:0;'></div>";
    var loadingImg ='';
     
    loadingImg +=" <div id='loadingImg'>";
    loadingImg +=" <img src='${pageContext.request.contextPath}/resources/assets/images/spinner.gif' style='position:absolute; z-index:9500; text-align:center; display:block; top:650%; left:42%;'/>";
    loadingImg += "</div>";  
 
    $('body')
        .append(mask)
 
    $('#mask').css({
            'width' : maskWidth,
            'height': maskHeight,
            'opacity' :'0.3'
    });
    
    $('#mask').show();
  
    $('.container_header').append(loadingImg);
    $('#loadingImg').show();
}

function closeLoading2() {
    $('#mask, #loadingImg').hide();
    $('#mask, #loadingImg').remove(); 
}
//문서 기안일
function setSelectDate() 
{
	$("#SCH_FROM_CREDATE").datepicker({
	    changeYear : true,
	    changeMonth : true,
	    dateFormat: 'yy-mm-dd'
	});
	
	$("#SCH_TO_CREDATE").datepicker({
	    changeYear : true,
	    changeMonth : true,
	    dateFormat: 'yy-mm-dd'
	});
	
	$("#SCH_FROM_D_P_C_G_REGDATE").datepicker({
	    changeYear : true,
	    changeMonth : true,
	    dateFormat: 'yy-mm-dd'
	});
	
	$("#SCH_TO_D_P_C_G_REGDATE").datepicker({
	    changeYear : true,
	    changeMonth : true,
	    dateFormat: 'yy-mm-dd'
	});
	
	var oToday = new Date();
	$("#SCH_TO_CREDATE").val(getFormatDate(oToday));
	$("#SCH_TO_D_P_C_G_REGDATE").val(getFormatDate(oToday));
	
	var oFromDate = new Date(oToday.setDate(oToday.getDate() - 30));
	$("#SCH_FROM_CREDATE").val(getFormatDate(oFromDate));
	oToday = new Date();
	oFromDate = new Date(oToday.setDate(oToday.getDate() - 30));
	$("#SCH_FROM_D_P_C_G_REGDATE").val(getFormatDate(oFromDate));
}

//검색
function fn_search() 
{
	
	// 검출일
	if($("#SCH_FROM_CREDATE").val() > $("#SCH_TO_CREDATE").val()){
		alert("입력한 끝 날짜가 시작 날짜 보다 빠릅니다.");
		return;
	}
	// 기안일
	if($("#SCH_FROM_D_P_C_G_REGDATE").val() > $("#SCH_TO_D_P_C_G_REGDATE").val()){
		alert("입력한 끝 날짜가 시작 날짜 보다 빠릅니다.");
		return;
	}
	
	
	// 정탐/오탐 리스트 그리드
	var oPostDt = {};
	oPostDt["SCH_TARGET"]				= $("#SCH_TARGET").val();
	oPostDt["SCH_TARGET_ID"]			= $("#SCH_TARGET_ID").val();
	// oPostDt["SCH_PATH"]					= $("#SCH_PATH").val();
	oPostDt["SCH_FROM_CREDATE"]			= $("#SCH_FROM_CREDATE").val();
	oPostDt["SCH_TO_CREDATE"]			= $("#SCH_TO_CREDATE").val();
	
	// oPostDt["SCH_OFFICE_CODE"]			= $("#SCH_OFFICE_CODE").val();
	oPostDt["SCH_OWNER"]				= $("#SCH_OWNER").val();
	oPostDt["SCH_PROCESSING_FLAG"]		= $("#SCH_PROCESSING_FLAG").val();
	oPostDt["SCH_FROM_D_P_C_G_REGDATE"]	= $("#SCH_FROM_D_P_C_G_REGDATE").val();
	oPostDt["SCH_TO_D_P_C_G_REGDATE"]	= $("#SCH_TO_D_P_C_G_REGDATE").val();
	oPostDt["SCH_DMZ_SELECT"]   		= $("input[name='ra_new']:checked").val();
	oPostDt["SCH_D_P_C_G_REGDATE_CHK"]	= $("#SCH_D_P_C_G_REGDATE_CHK").is(":checked") ? "Y" : "N";		// 조회조건 기안일 사용여부
	// oPostDt["SCH_OBJECT"]				= $("#SCH_OBJECT").val();
	oPostDt["SCH_GROUP"]				= $("#SCH_GROUP").val();
	oPostDt["GROUP_ID"]					= $("#GROUP_ID").val();
	oPostDt["AP_NO"]					= $("#AP_NO").val();
	oPostDt["SCH_DATE"]					= $("#SCH_DATE option:selected").val();
	
	oGrid.clearGridData();
	oGrid.setGridParam({
	    url: "${getContextPath}/report/searchSummaryList",
	    postData: oPostDt,
	    datatype: "json"
	}).trigger("reloadGrid");
}

//
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

function downLoadMonthlyReports(){
	
	var oPostDt = {};
	oPostDt["year"] = $("#monthly_year").val();
	oPostDt["month"] = $("#monthly_month").val();
	
	var jsonData = JSON.stringify(oPostDt);
	
	$.ajax({
		type: "POST",
		url: "${getContextPath}/report/getMonthlyReport",
		async: false,
		data: jsonData,
		datatype: "json",
		contentType: "application/json; charset=UTF-8",
		success: function (result){
			executeDownload(result);
		},
		error: function (request, status, error) {
			alert('데이터가 없습니다.');
		}
	});
	$('#monthlyReportPopup').hide();
}

function executeDownload(resultList){
	var result = "서버명,검출경로 수,주민등록번호,외국인등록번호,여권번호,";
		result += "운전면허,계좌번호,카드번호,전화번호,합계,";
		result += "정탐(삭제),정탐(암호화),정탐(마스킹),정탐(기타),정탐(임시보관),";
		result += "오탐(예외처리),오탐(오탐수용),오탐(기타),조치 예정,미조치\r\n";

	$.each(resultList, function(i, item){
		result += item.NAME + "," + item.PATH + "," + item.RRN + "," + item.FOREIGNER + "," + item.PASSPORT + ",";
		result += item.DRIVER + "," + item.ACCOUNT + "," + item.CARD + "," + item.PHONE + "," + item.TOTAL + ",";
		result += item.DEL + "," + item.ENCODING + "," + item.MASKING + "," + item.CONFIRM_ETC + "," + item.TEMP_STOR + ",";
		result += item.EXCEPTION + "," + item.FALSE_AGREE + "," + item.FALSE_ETC + "," + item.ACT + "," + item.ACT_NOT + "\r\n";
	});
	
	var mm = $("#monthly_month").val();
	var yyyy = $("#monthly_year").val();
	
	if(mm<10) {
		mm='0'+mm;
	}
	
	today = yyyy + "" + mm + dd;
	
	var blob = new Blob(["\ufeff"+result], {type: "text/csv;charset=utf-8" });
	if(navigator.msSaveBlob){
		window.navigator.msSaveOrOpenBlob(blob, "월간리포트_" + today + ".csv");
	} else {
		var downloadLink = document.createElement("a");
		var url = URL.createObjectURL(blob);
		downloadLink.href = url;
		downloadLink.download = "월간리포트_" + today + ".csv";
		
		document.body.appendChild(downloadLink);
		downloadLink.click();
		document.body.removeChild(downloadLink);
	}
}

/*
$('#SCH_OBJECT').on('change', function(){
	if($('#SCH_OBJECT').val() == 'group'){
		$('#SCH_TARGET').val('')
		$('#SCH_TARGET').css('display', 'none')
		$('#GROUP_DIV').css('display', 'block')
		//selectGroup();
	} else if ($('#SCH_OBJECT').val() == 'host') {
		//$('#SCH_TARGET').attr('placeholder', '호스트명을 입력해주세요.')
		$('#SCH_TARGET').css('display', 'block')
		$('#GROUP_DIV').css('display', 'none')
		$('#SCH_GROUP').val('')
		$('#GROUP_ID').val('')
	}
})
*/

$('#SCH_GROUP').on('click', function(){
	var type = 0;
	selectGroup("0");
});

$('#SCH_TARGET').on('click', function(){
	var type = 0;
	selectHost(type);
});

function selectGroup(typeChk) {
	var pop_url = "${getContextPath}/popup/reportGroupList";
	var id = "reportGroupList" 
	var winWidth = 700;
	var winHeight = 565;
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

function selectHost(typeChk) {
	var pop_url = "${getContextPath}/popup/reportHostList";
	var id = "reportGroupList"
	var winWidth = 700;
	var winHeight = 565;
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

function fn_excel() 
{
	
	// 검출일
	if($("#SCH_FROM_CREDATE").val() > $("#SCH_TO_CREDATE").val()){
		alert("입력한 끝 날짜가 시작 날짜 보다 빠릅니다.");
		return;
	}
	// 기안일
	if($("#SCH_FROM_D_P_C_G_REGDATE").val() > $("#SCH_TO_D_P_C_G_REGDATE").val()){
		alert("입력한 끝 날짜가 시작 날짜 보다 빠릅니다.");
		return;
	}
	
	
	// 정탐/오탐 리스트 그리드
	var oPostDt = {};
	oPostDt["SCH_TARGET"]				= $("#SCH_TARGET").val();
	oPostDt["SCH_TARGET_ID"]			= $("#SCH_TARGET_ID").val();
	// oPostDt["SCH_PATH"]					= $("#SCH_PATH").val();
	oPostDt["SCH_FROM_CREDATE"]			= $("#SCH_FROM_CREDATE").val();
	oPostDt["SCH_TO_CREDATE"]			= $("#SCH_TO_CREDATE").val();
	
	// oPostDt["SCH_OFFICE_CODE"]			= $("#SCH_OFFICE_CODE").val();
	oPostDt["SCH_OWNER"]				= $("#SCH_OWNER").val();
	oPostDt["SCH_PROCESSING_FLAG"]		= $("#SCH_PROCESSING_FLAG").val();
	oPostDt["SCH_FROM_D_P_C_G_REGDATE"]	= $("#SCH_FROM_D_P_C_G_REGDATE").val();
	oPostDt["SCH_TO_D_P_C_G_REGDATE"]	= $("#SCH_TO_D_P_C_G_REGDATE").val();
	oPostDt["SCH_DMZ_SELECT"]   		= $("input[name='ra_new']:checked").val();
	oPostDt["SCH_D_P_C_G_REGDATE_CHK"]	= $("#SCH_D_P_C_G_REGDATE_CHK").is(":checked") ? "Y" : "N";		// 조회조건 기안일 사용여부
	// oPostDt["SCH_OBJECT"]				= $("#SCH_OBJECT").val();
	oPostDt["SCH_GROUP"]				= $("#SCH_GROUP").val();
	oPostDt["GROUP_ID"]					= $("#GROUP_ID").val();
	oPostDt["AP_NO"]					= $("#AP_NO").val();
	oPostDt["SCH_DATE"]					= $("#SCH_DATE option:selected").val();
	
	$.ajax({
		type: "POST",
		url: "${getContextPath}/report/searchSummaryList",
		////async: false,
		data: oPostDt,
		datatype: "json",
		success: function (result){
			executeReportDownload(result);
		},
		error: function (request, status, error) {
			alert('데이터가 없습니다.');
		}
	});
}

function executeReportDownload(resultList){
	var result = "OS,담당자,서버명,IP,경로,그룹명,";
		result += "주민등록번호,외국인등록번호,여권번호,운전면허번호,계좌번호,카드번호,이메일,휴대폰번호,총개수,";
		result += "검출일,삭제일,기안일,기안자,결재일,결재자,처리구분\r\n";

	$.each(resultList, function(i, item){
		result += item.PLATFORM + "," + item.USER_NAME + "," + item.TARGET_NAME + "," + item.IP + "," + item.PATH2 + "," + item.OFFICE_NM + ",";
		result += item.TYPE1 + "," + item.TYPE2 + "," + item.TYPE3 + "," + item.TYPE4 + "," + item.TYPE5 + "," + item.TYPE6 + "," + item.TYPE7 + "," + + item.TYPE8 + "," + + item.TYPE + ",";
		result += item.CREDATE + "," + item.DELDATE + "," + item.D_P_C_G_REGDATE + "," + item.ACCOUNT_USER_NM + "," + item.OKDATE + "," + item.OK_ACCOUNT_USER_NM + "," + item.PROCESSING_FLAG_NAME + "\r\n";
	});
	
	var mm = $("#monthly_month").val();
	var yyyy = $("#monthly_year").val();
	
	if(mm<10) {
		mm='0'+mm;
	}
	
	today = yyyy + "" + mm + dd;
	
	var blob = new Blob(["\ufeff"+result], {type: "text/csv;charset=utf-8" });
	if(navigator.msSaveBlob){
		window.navigator.msSaveOrOpenBlob(blob, "통합_보고서_" + today + ".csv");
	} else {
		var downloadLink = document.createElement("a");
		var url = URL.createObjectURL(blob);
		downloadLink.href = url;
		downloadLink.download = "통합_보고서_" + today + ".csv";
		
		document.body.appendChild(downloadLink);
		downloadLink.click();
		document.body.removeChild(downloadLink);
	}
}

 
/*  */
$("#btnExcelDownPopUpClose").on("click", function(){
	$("#ExcelDownPopUp").hide();
}); 
$("#btnCancleExcelDownPopUp").on("click", function(){
	$("#ExcelDownPopUp").hide();
}); 
$("#detailExceldownloadCanCel").on("click", function(){
	$("#detailExceldownloadPop").hide();
});
$("#detailExceldownloadPopUp").on("click", function(){
	$("#detailExceldownloadPop").show();
	reportExcelData("${defaultReport}"); 
});
function reportExcelData(reportFlag){
	$("#reportExcelColumnNm").show();	
	$("#reportExcelColumnNmSave").hide();	
	var postData = {report_flag : reportFlag};
	var headerList;
	$.ajax({ // 보고서 다운로드
        type: "POST",
        url: "${getContextPath}/setting/reportHeaderList",
        async : true, 
        data : postData,   
        success: function (response) {
        	headerList = response; 
        	var headerFormat = reportModelDraw(pattern, reportFlag, processing, response);
            $("#reportExtensiontd").html(headerFormat.html);
            if(reportFlag == "simeple_server0"){
            	$("#reportHeaderDate").css("height", "189px");
            	$("#reportExtensiontd").css("height", "283px");
            }else{
            	$("#reportHeaderDate").css("height", "100px");
            	$("#reportExtensiontd").css("height", "371px");
            }
            
            if(headerFormat.html2 == ""){
            	$("#deatilReportHeader").hide(); 
            	$("#reportExtensiontd").css("height", "481px"); 
            }else{  
            	$("#deatilReportHeader").show();             	
	            $("#reportHeaderDate").html(headerFormat.html2);
            }
            
            $(".reportHedaerNmInput").hide();
        },
        error: function (request, status, error) {
            return;
        }
    });	
}
// 보고서 다운로드
$("#detailExceldownload").on("click", function(){
	if($("#SCH_FROM_CREDATE").val() > $("#SCH_TO_CREDATE").val()){
		alert("입력한 끝 날짜가 시작 날짜 보다 빠릅니다.");
		return;
	}
	
	// 기안일
	if($("#SCH_FROM_D_P_C_G_REGDATE").val() > $("#SCH_TO_D_P_C_G_REGDATE").val()){
		alert("입력한 끝 날짜가 시작 날짜 보다 빠릅니다.");
		return;
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
	
	/* 
	1. 선택된 호스트 or 보고서 다운로드 될 서버를 목록
	2. 서버별로 순차적으로 검출 경로 불러오기
	3. excel 생성
	4. 저장
	*/
	
	var ap_list = null;
	var host_list = null; 
	var oPostDt = {};
	
	var group_id_list = [];
	
	// Host 선택이 있으면
	if($("#GROUP_ID_LIST").val() != ""){
		group_id_list = $("#GROUP_ID_LIST").val().split(",");
	}
	
	oPostDt["target_id"] = $("#SCH_TARGET_ID").val();
	oPostDt["ap_no"] = $("#AP_NO").val();
	oPostDt["group_id"] = $("#GROUP_ID").val();
	oPostDt["group_id_list"] = group_id_list;
	oPostDt["col_list"] = checkedValues;
	oPostDt["type_list"] = checkedValues2;
	oPostDt["sch_status"] = $("#SCH_DATE").val();
	oPostDt["sch_processing_flag"] = $("#SCH_PROCESSING_FLAG").val();
// 	oPostDt["report_flag"] = $("#report_flag").val();
	oPostDt["url"]						= '/report/pi_report_summary' ; // 바로가기 url
	oPostDt["page_name"]				= '[결과 관리 > 통합 보고서]'; // 현재 페이지 설명
	
	if($("#SCH_DATE").val() == 0 ){ // 검출일
		oPostDt["sch_SDATE"] = $("#SCH_FROM_CREDATE").val();
		oPostDt["sch_EDAT"] = $("#SCH_TO_CREDATE").val();
		
		$("#sch_SDATE").val($("#SCH_FROM_CREDATE").val());
		$("#sch_EDAT").val($("#SCH_TO_CREDATE").val());
   }else if($("#SCH_DATE").val() == 1 ){ // 기안일
		oPostDt["sch_SDATE"] = $("#SCH_FROM_D_P_C_G_REGDATE").val();
		oPostDt["sch_EDAT"] = $("#SCH_TO_D_P_C_G_REGDATE").val();
		
	   $("#sch_SDATE").val($("#SCH_FROM_D_P_C_G_REGDATE").val());
	   $("#sch_EDAT").val($("#SCH_TO_D_P_C_G_REGDATE").val());
   }
	
	var now = new Date();
	const year = now.getFullYear();
	const month = now.getMonth() + 1;
	const date = now.getDate(); 
	
	var reportDate = year +'' + (month >= 10 ? month : '0' + month) + '' + (date >= 10 ? date : '0' + date);
	var filename =  "";
	
	var downLoad_flag = $("#downLoad_flag").val(); 
	var report_file_name = $("#report_file_name").val();
	 
	filename = "server_result_picenter_"+ reportDate;
	
	if(report_file_name != null && report_file_name != ""){
		filename = report_file_name; 
	}
	
	var realfilename = "";
	oPostDt["filename"] = filename +".xlsx";
	
	$.ajax({ // 서버에 저장할 파일 명 
        type: "POST",
        url: "${getContextPath}/download/downLoadFileInformation", 
        async : false, 
        data :  JSON.stringify(oPostDt), 
        contentType: 'application/json; charset=UTF-8',
        success: function (result) {
        	realfilename =  result.real_file_name; // 서버에 저장할 파일 명
			oPostDt["real_file_name"] = realfilename;
			oPostDt["flag"] = downLoad_flag;

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
		 	  				value: filename+".xlsx"
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
	
});

function detailExcelDwonload(workbook, name, data) {
	
	var result = 0;
	var pattern = '${pattern}'.split('[{').join('').split('}]').join('');
	pattern = pattern.split('}, {');
	
	var patternCnt = '${patternCnt}';
	
	var sheet =  workbook.addWorksheet(data[0].host_name);
	var headers = [];
	
	var matchType = [];
	var matchCount = [];
	var metasCount = [];
	var matchContents = [];
	
	headers.push({header: 'ID', key: 'id', width: 0, style: { font: {size: 10}, alignment: {horizontal: 'center'}}});
	headers.push({header: 'hash_id', key: 'hash_id', width: 0, style: { font: {size: 10}, alignment: {horizontal: 'center'}}});
	headers.push({header: 'Target_id', key: 'target_id', width: 0, style: { font: {size: 10}, alignment: {horizontal: 'center'}}});
	headers.push({header: '서버명', key: 'host', width: 20, style: { font: {size: 10}, alignment: {horizontal: 'center'}}});
	headers.push({header: 'IP', key: 'ip', width: 15, style: { font: {size: 10}, alignment: {horizontal: 'center'}}});
	headers.push({header: '연결상태', key: 'connected', width: 15, style: { font: {size: 10}, alignment: {horizontal: 'center'}}});
	headers.push({header: ' ', key: 'subpath', width: 5, style: { font: {size: 10}, alignment: {horizontal: 'center'}}});
	headers.push({header: '위치', key: 'location', width: 25, style: {font: {size: 10}}});
	headers.push({header: '파일소유자', key: 'owner', width: 10, style: {font: {size: 10}, alignment: {horizontal: 'center'}}});
	headers.push({header: '파일수정일', key: 'modified', width: 15, style: { font: {size: 10}, alignment: {horizontal: 'center'}}});
	headers.push({header: '파일생성일', key: 'credate', width: 15, style: { font: {size: 10}, alignment: {horizontal: 'center'}}});
	headers.push({header: '검출수', key: 'total', width: 8, style: { font: {size: 10}, alignment: {horizontal: 'center'}}});
	
	 for(var p=0; p < patternCnt ; p++){
  	   var row = pattern[p].split(', ');
  	   var ID = row[0].split('ID=').join(''); //변수 TABLE_NAME은 TB_NAME1, 다음 반복문에서는 TB_NAME2
  	   var PATTERN_NAME = row[0].split('PATTERN_NAME=').join(''); // 변수 CHK Y, 다음 반복문에서는 N
  	   var data_id = PATTERN_NAME.split('=')[1];

//        {header: '계좌번호', key: 'acc', width: 8, style: { font: {size: 10}, alignment: {horizontal: 'center'}}},
		headers.push({header: ID.split('=')[1], key: data_id, width: 10, style: { font: {size: 10}, alignment: {horizontal: 'center'}}});
		headers.push({header: ID.split('=')[1]+" 패턴", key: data_id+"data", width: 20, style: { font: {size: 10}, alignment: {horizontal: 'center'}}});
     }
	 
	headers.push({header: '검출내역', key: 'path', width: 30, style: { font: {size: 10}}});
	headers.push({header: '상세결과', key: 'detail', width: 35, style: { font: {size: 10}}});
	headers.push({header: '비고', key: 'note', width: 5, style: { font: {size: 10}}});
	
	
	sheet.getRow(1).fill = {
		type: 'pattern',
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
	
	sheet.columns = headers;
	
	
	var excelRow = [];
	// 하위경로 체크
    var infoIDNull = false;
	var contentList = []
	
	for(var i = 0; i < data.length; i++){
		
		matchType = [];
		matchCount = [];
		metasCount = [];
		matchContents = [];
		
		for(var p=0; p < patternCnt ; p++){
			 var row = pattern[p].split(', ');
			 var ID = row[0].split('ID=').join(''); //변수 TABLE_NAME은 TB_NAME1, 다음 반복문에서는 TB_NAME2
		  	 var PATTERN_NAME = row[1].split('PATTERN_NAME=').join(''); // 변수 CHK Y, 다음 반복문에서는 N
		  	 var data_id = PATTERN_NAME.split('=')[1];
			
			matchType.push(ID.split('=')[1]); 
			matchCount.push(0);
			metasCount.push(0);
			matchContents.push("");
		}
		
		
 	    var uri = "";
 	    
 	    var dataResultList = data[i].dataResultList;
 	    var infoResultList = data[i].infoResultList;
 	    var metasResultList = data[i].metasResultList;

 	    var type_total  = 0;
 	    
 	    
 	    if(data[i].CHK != "") infoIDNull = true;
 	    else infoIDNull = false; 
 	    
		uri = window.location.protocol + "//" + window.location.host +"/popup/detectionDetail?tid="+data[i].target_id+"&fid="+data[i].fid;
		
		 var content_cnt = 0;
         var mask_content = "";
         var result_content;
         var result_detail;
         var richText = [];
     	
		if(dataResultList!= null){
			
			result_detail = "";			
			 for(var io= 0 ; io < dataResultList.length ; io++ ){ 
				 
				 var dataIdx = dataResultList[io].IDX;
	         	
	 			if(dataResultList[io].DATA != null){
	 				result_detail += dataResultList[io].DATA + "\n'";
	 			}
	             
	        	   for(var dt= 0 ; dt < infoResultList.length ; dt++ ){ 

	        		   var infoIdx = infoResultList[dt].IDX;
	        		   
	        		   var matchIndex = -1;
	        		   
						if(infoIdx == dataIdx){
	        			   
	        			   matchIndex =  (infoResultList[dt].DATA_TYPE)-1;
	        			   mask_content = infoResultList[dt].DATA
	        			   
		        		   matchCount[matchIndex] = matchCount[matchIndex] + 1; 
		        		   matchContents[matchIndex] = matchContents[matchIndex] + mask_content + "\n";
	        		   }
	        	   }
			 }
			
		}else{
			return;
		}
		
		if(metasResultList != null){
			for(var j=0 ; j < metasResultList.length; j++) {
				var meta = metasResultList[j];
				
				var metasIndex = -1;
				var metasContent = 0;
				
				for(var mp=0; mp < patternCnt ; mp++){
					
					var row = pattern[mp].split(', ');
					var ID = row[0].split('ID=').join(''); //변수 TABLE_NAME은 TB_NAME1, 다음 반복문에서는 TB_NAME2
					var PATTERN_NAME = row[1].split('PATTERN_NAME=').join(''); // 변수 CHK Y, 다음 반복문에서는 N
					var data_id = PATTERN_NAME.split('=')[1];
					
					 if(meta.TYPE.toUpperCase().indexOf(data_id.toUpperCase()) > -1){
						 metasIndex = mp;
						 metasContent = meta.VALUE;
						 
						 type_total += parseInt(meta.VALUE);
					 }
					 if(metasIndex >= 0){
						 metasCount[metasIndex] = parseInt(metasContent);
					  }
				}
			}
		}
		
        var content_cnt = 0;
        var result_content;
        var matchData = "";
		const rowData = {};
		
		rowData["id"] = data[i].fid;
		rowData["hash_id"] = data[i].hash_id;
		rowData["target_id"] = data[i].target_id;
		rowData["host"] = data[i].host_name;
		rowData["ip"] = data[i].agent_ip;
		rowData["connected"] = data[i].agent_connected;
		rowData["subpath"] = data[i].CHK;
		rowData["location"] = data[i].path;
		rowData["owner"] = data[i].owner;
		rowData["modified"] = data[i].Modified_Date;
		rowData["credate"] = data[i].Created_Date;
		rowData["total"] = type_total.format(); 
		
		 for(var p=0; p < patternCnt ; p++){
			 var row = pattern[p].split(', ');
			 var ID = row[0].split('ID=').join(''); //변수 TABLE_NAME은 TB_NAME1, 다음 반복문에서는 TB_NAME2
			 var PATTERN_NAME = row[1].split('PATTERN_NAME=').join(''); // 변수 CHK Y, 다음 반복문에서는 N
			 var data_id = PATTERN_NAME.split('=')[1];
			 
			 rowData[data_id] =  metasCount[p].format(); 
			 rowData[data_id+"data"] = matchContents[p];
			 
		 }
		
		rowData["path"] = result_detail;
		rowData["detail"] = uri;
		rowData["note"] = "";
		
		excelRow.push(rowData);
		result = 1;
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

function reportSelectBoxOption() {
	reportExcelData($("#downLoad_flag").val());
}

</script>

</body>
</html>
