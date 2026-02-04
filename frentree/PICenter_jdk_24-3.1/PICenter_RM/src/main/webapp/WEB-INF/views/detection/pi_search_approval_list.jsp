<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../../include/header.jsp"%>
<style>
	.ui-jqgrid tr.ui-row-ltr td{
		cursor: pointer;
		white-space: nowrap;
	}
</style>
<!-- 검출관리 -->

<!-- section -->
<section>
	<!-- container -->
	<div class="container">
		<%-- <%@ include file="../../include/menu2.jsp"%> --%>
		<!-- content -->
		<h3 style="display: inline; top: 25px;">결재 진행현황</h3>
		<p style="position: absolute; top: 33px; left: 184px; font-size: 13px; color: #9E9E9E;">전자결재 진행 상태를 확인 하실수 있습니다.</p>
		<div class="content magin_t35">
			<div class="grid_top">  
				<div class="searchBox" style="float: left;">
					<table class="user_info narrowTable" id="navGridSearchDiv"  style="width: 712px;">
						<tbody>
							<tr id="searchTextBox">
								<th style="text-align: center; border-radius: 0.25rem; width: 92px; " class="searchName">
									<select id="searchFilter"></select>
								</th>
             			     	<td id="defaultSearchTextBox">
                					<input type="text" style="width: 205px; padding-left: 5px;" size="10" class="searchContent" id="searchContent"  placeholder="검색어를 입력하세요.">	
                			 	</td> 
                			 	<td id="searchDayBox"> </td>
		                		<td> 
		                    		 <input type="button" name="button" class="navGridSearchBtn" style="margin-top: 5px;">
		                    	</td>
							</tr> 
						</tbody>
					</table>
				</div>
				<div id="searchFilterBox" class ="searchFilterBox" style="display:inline-block;width:849px;position:absolute;"></div>
				<div class="list_sch" style="height: 39px; margin-top: 10px; float: right;" > 
					<div id="searchConditionsContainer" style="float: left;" ></div>
                    <div style="float: right; margin-left: 3px; margin-bottom: 7px;">
                    	<c:if test="${memberInfo.USER_GRADE != '0' and picSession.version.client != 'samsung'}">
								<button type="button" name="button" class="btn_down" id="btnApproveAll">일괄 결재</button> 
								<button type="button" name="button" class="btn_down" id="btnApprove">결재</button>
						</c:if>
						<button type="button" name="button" class="btn_down" id="btnDownloadExel">다운로드</button>
                    </div>
                    <div style="float: right;">
						<h5 style="width: 24px; height: 24px; margin-top: 2px; background-size : 100%; background-image: url('${pageContext.request.contextPath}/resources/assets/images/pi_list.png');" id="tableShow">&nbsp;</h5>
					</div>
                </div>
			</div>
			<div class="left_box2" style="overflow: hidden; max-height: 635px; height: 635px; margin-top: 10px">
				<table id="processApproveGrid"></table>
				<div id="processApproveGridPager"></div>
			</div>
		</div>
	</div>


	<!-- 팝업창 시작 결재 신청 확인 -->
	<div id="selecetProcessPopup" class="popup_layer" style="display: none">
		<div class="popup_box" style="height: 468px; width: 1000px; top: 52%; left: 40%; padding: 10px; background: #f9f9f9;">
			<div class="popup_top" style="background: #f9f9f9;">
			<h1  style="color: #222; padding: 0; box-shadow: none;">결재 신청 확인</h1>
			</div> 
			<div class="popup_content">     
				<div class="content-box" style="height: 379px; background: #fff; border: 1px solid #c8ced3; overflow-y: auto;">
					<table class="popup_tbl" style="table-layout: fixed;">
						<colgroup>
							<col width="130">
							<col width="*">
						</colgroup>
						<tbody>
							<tr>
								<th style="background-color: #f9f9f9;">이름</th>
								<td style="padding: 5px;">
									<textarea id="excepPath" name="excepPath" style="border: 0px solid #cdcdcd; width: 100%; height: 230px; resize: none;" readonly="true"></textarea>
								</td>
							</tr>
							<tr>
								<th style="background-color: #f9f9f9;">결재</th>
								<td><input type="radio" name="trueFalseChk" id="permit" value="E" class="edt_sch" style="border: 0px solid #cdcdcd;">승인 <input type="radio" name="trueFalseChk" id="reject" value="D" class="edt_sch" style="margin-left: 100px; border: 0px solid #cdcdcd;">반려 <input type="hidden" id="selectedDate" value=""></td>
							</tr>
							<tr>
								<th style="background-color: #f9f9f9;">사유</th>
								<td style="padding: 5px;"><textarea id="reason" class="edt_sch" style="border: 0px solid #cdcdcd; width: 100%; height: 50px; margin-top: 5px; margin-bottom: 5px; resize: none;"></textarea></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="popup_btn">
				<div class="btn_area">
					<button type="button" id="btnSave">저장</button>
					<button type="button" id="btnCancel">취소</button>
				</div>
			</div>
		</div>
	</div>
	<!-- 팝업창 종료  -->

	<!-- 팝업창 시작 정탐/오탐 신청 내역-->
	<div id="insertPathPopup" class="popup_layer" style="display: none">
		<div class="popup_box" style="height: 530px; width: 1000px; top: 52%; left: 40%; padding: 10px; background: #f9f9f9;">
		<img class="CancleImg" id="btnCancleInsertPathPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
			<div class="popup_top" style="background: #f9f9f9;">
				<h1 id="groupName" style="color: #222; padding: 0; box-shadow: none;"></h1>
			</div>
			<div class="popup_content">
				<div class="content-box" style="height: 460px; background: #fff; border: 1px solid #c8ced3; overflow-y: auto;">
					<!-- <h2>세부사항</h2>  -->
					<table class="popup_tbl" style="table-layout: fixed;">
						<colgroup>
							<col width="130">
							<col width="*">
						</colgroup>
						<tbody>
							<tr>
								<th style="border-bottom: 1px solid #cdcdcd;">내용</th>
								<td style="padding: 5px; border-bottom: 1px solid #cdcdcd;">
									<div id="processPath" name="processPath" style="overflow-y: auto; height: 150px;"></div>
								</td>
							</tr>
							<tr>
								<th style="border-bottom: 1px solid #cdcdcd;">비고</th>
								<td style="padding: 5px; border-bottom: 1px solid #cdcdcd;"><textarea id="processNote" name="processNote" style="border: 0px solid #cdcdcd; width: 100%; height: 150px; resize: none; overflow-y: auto;" readonly="true"></textarea></td>
							</tr>
							<tr>
								<th id="comment_th">결재자의견</th>
								<td style="padding: 5px;"><textarea id="comment_td" name="comment_td" style="border: 0px solid #cdcdcd; width: 100%; height: 100px; resize: none; overflow-y: auto;" readonly="true"></textarea></td>
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
	<!-- 팝업창 종료  -->


	<!-- 팝업창 시작 개인정보검출 상세정보 -->
	<%
		String browser = "";
		String userAgent = request.getHeader("User-Agent");
	%>
	<%
		if (userAgent.indexOf("Trident") > 0 || userAgent.indexOf("MSIE") > 0) {
	%>
	<div id="pathWindow" style="position: absolute; left: 400px; top: 350px; touch-action: none; width: 60%; height: 365px; z-index: 999; display: none; min-width: 35%; min-height: 200px;" class="ui-widget-content">
		<table class="mxWindow" style="width: 100%; height: 100%;">
			<tbody>
				<tr>
					<td class="mxWindowTitle" style="background: #006EB6; cursor: grab; touch-action: none;">
						<table style="width: 100%; height: 36px;">
							<colgroup>
								<col width="*">
								<col width="30px">
							</colgroup>
							<tr>
								<td style="color: #ffffff; text-align: left; padding-left: 20px;"><h2>하위 경로 정보</h2></td>
								<td style="display: inline-block; padding-top: 6px; cursor: default;"><img src="${pageContext.request.contextPath}/resources/assets/images/close.gif" title="Close" id="pathWindowClose"></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td class="mxWindowPane">
						<div class="mxWindowPane" style="width: 100%; height: 100%;">
							<table border="1" style="width: 100%; height: 100%;">
								<tbody>
									<tr>
										<td style="width: 100%; height: 100%;">
											<div id="pathContent" style="overflow-y: auto; height: 100%; padding: 5px 5px;">&nbsp;</div>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<%
		} else {
	%>
	<div id="pathWindow" style="position: absolute; left: 400px; top: 350px; touch-action: none; width: 60%; height: 365px; z-index: 999; display: none; min-width: 35%; min-height: 200px;" class="ui-widget-content">
		<table class="mxWindow" style="width: 100%; height: 100%;">
			<tbody>
				<tr>
					<td class="mxWindowTitle" style="background: #006EB6; cursor: grab; touch-action: none;">
						<table style="width: 100%; height: 100%;">
							<colgroup>
								<col width="*">
								<col width="30px">
							</colgroup>
							<tr>
								<td style="color: #ffffff; text-align: left; padding-left: 20px;"><h2>하위 경로 정보</h2></td>
								<td style="display: inline-block; padding-top: 6px; cursor: default;"><img src="${pageContext.request.contextPath}/resources/assets/images/close.gif" title="Close" id="pathWindowClose"></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td class="mxWindowPane">
						<div class="mxWindowPane" style="width: 100%; height: 100%;">
							<table border="1" style="width: 100%; height: 100%;">
								<tbody>
									<tr>
										<td style="width: 100%; height: 100%;">
											<div id="pathContent" style="overflow-y: auto; height: 100%; padding: 5px 5px;">&nbsp;</div>
										</td>
									</tr>

								</tbody>
							</table>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<%
		}
	%>
	<%
		if (userAgent.indexOf("Trident") > 0 || userAgent.indexOf("MSIE") > 0) {
	%>
	<div id="taskWindow" style="position: absolute; left: 340px; top: 350px; touch-action: none; width: 70%; height: 365px; z-index: 999; display: none; min-width: 30%; min-height: 200px;" class="ui-widget-content">
		<table class="mxWindow" style="width: 100%; height: 100%;">
			<tbody>
				<tr>
					<td class="mxWindowTitle" style="background: #006EB6; cursor: grab; touch-action: none;">
						<table style="width: 100%; height: 36px;">
							<colgroup>
								<col width="*">
								<col width="30px">
							</colgroup>
							<tr>
								<td style="color: #ffffff; text-align: left; padding-left: 20px;"><h2>개인정보검출 상세정보</h2></td>
								<td style="display: inline-block; padding-top: 6px; cursor: default;"><img src="${pageContext.request.contextPath}/resources/assets/images/close.gif" title="Close" id="taskWindowClose"></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td class="mxWindowPane">
						<div class="mxWindowPane" style="width: 100%; height: 100%;">
							<table border="1" style="width: 100%; height: 100%;">
								<tbody>
									<tr>
										<td id="matchCount" style="width: 335px; min-width: 335px; max-width: 335px; height: 50px; padding: 5px;">&nbsp;</td>
										<td style="width: 100%; height: 100%;" rowspan="2">
											<div id="bodyContents" style="background: white; overflow: scroll; height: 315px; padding: 5px 5px;">&nbsp;</div>
										</td>
									</tr>
									<tr>
										<td>
											<div id="matchData" style="background: white; overflow: scroll; height: 265px; padding: 5px">&nbsp;</div>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<%
		} else {
	%>
	<div id="taskWindow" style="position: absolute; left: 650px; top: 350px; touch-action: none; width: 50%; height: 300px; z-index: 999; display: none; min-width: 30%; min-height: 200px;" class="ui-widget-content">
		<table class="mxWindow" style="width: 100%; height: 100%;">
			<tbody>
				<tr>
					<td class="mxWindowTitle" style="background: #006EB6; cursor: grab; touch-action: none;">
						<table style="width: 100%; height: 100%;">
							<colgroup>
								<col width="*">
								<col width="30px">
							</colgroup>
							<tr>
								<td style="color: #ffffff; text-align: left; padding-left: 20px;"><h2>개인정보검출 상세정보</h2></td>
								<td style="display: inline-block; padding-top: 6px; cursor: default;"><img src="${pageContext.request.contextPath}/resources/assets/images/close.gif" title="Close" id="taskWindowClose"></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td class="mxWindowPane">
						<div class="mxWindowPane" style="width: 100%; height: 100%;">
							<table border="1" style="width: 100%; height: 100%;">
								<tbody>
									<tr>
										<td id="matchCount" style="width: 35%; height: 50px; padding: 5px;">&nbsp;</td>
										<td style="width: 65%; height: 100%;" rowspan="2">
											<div id="bodyContents" style="overflow-y: auto; height: 100%; padding: 5px 5px;">&nbsp;</div>
										</td>
									</tr>
									<tr>
										<td>
											<div id="matchData" style="overflow-y: auto; height: 100%; padding: 5px">&nbsp;</div>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<%
		}
	%>

	<!-- container -->
</section>
<!-- section -->
<%@ include file="../../include/footer.jsp"%>
<script>
var colModel = [];
GridName = "#processApproveGrid";
requestUrl = "${getContextPath}/approval/searchApprovalListData" ; 
	var bApprovalAll = false;
	var oApprovalAll = {};
	var resetFomatter = null;
	
	$(document).ready(function() {
		$("#btnApproval").on("click", function(e) {
			$.ajax({
				type : "POST",
				url : "/approvalTest",
				async : false,
				success : function(resultMap) {
					console.log(resultMap);
				},
				error : function(request, status, error) {
					alert("인증번호 인증 실패하셨습니다. ");
				}
			});
		});

		// 그리드 다운로드
		$("#btnDownloadExel").click(function() {
			
			console.log($("#processApproveGrid").getGridParam('selarrrow'));
			downLoadExcel();
		});

		// 날짜 설정
		setSelectDate();

		$("#selectList").change(function() {
			location.href = $("#selectList").val();
		});

		// 조회조건 담당자 inputbox Keydown Event (사용자 조회)
		$("#schPath, #schUserNm").keydown(function(e) {
			if (e.keyCode == 13)
				fn_search();
		});

		// 검색
		$("#btnSearch").click(function() {
			fn_search();
		});

		// 검색
		$("#statusList").change(function() {
			fn_search();
		});

		// 
		$("#btnCheck").click(function(e) {
			$("#insertPathPopup").hide();
			$("#processPath").val();
			$("#pathWindow").hide();
			$("#taskWindow").hide();
		});
		
		$("#btnCancleInsertPathPopup").click(function(e) {
			$("#insertPathPopup").hide();
		});

		// 일괄 결재 버튼
		$("#btnApproveAll").click(function(e) {
			reqApprovalAll();
		});

		// 결재 버튼
		$("#btnApprove").click(function(e) {
			reqApproval();
		});

		// 결재 취소
		$("#btnCancel").click(function(e) {
			bApprovalAll = false;
			oApprovalAll = {};
			$("#excepPath").val('');
			$("#reason").val('');
			$("#selecetProcessPopup").hide();
		});

		// 결재 - 저장
		$("#btnSave").click(function(e) {

			var radioChk = $("input:radio[name=trueFalseChk]:checked").val();

			if (!radioChk) {
				alert("결재 방안을 선택하세요");
			} else {
				if (bApprovalAll) {
					saveApprovalAll();
				} else {
					saveApproval();
				}
				bApprovalAll = false;
				oApprovalAll = {};
				$("#excepPath").val('');
				$("#reason").val('');
				$("#selecetProcessPopup").hide();
			}
		});
		
		loadJqGrid($("#processApproveGrid"));
	});

	//정탐/오탐 결재 리스트 그리드 조회
	function loadJqGrid(oGrid) {
		// 정탐/오탐 결재 리스트 그리드
// 		var oPostDt = {};
// 		oPostDt["target_id"] = $("#hostSelect").val();
// 		oPostDt["status"] = $("select[name='statusList']").val();
// 		oPostDt["user_nm"] = $("#schUserNm").val();
// 		oPostDt["path"] = $("#schPath").val();
// 		oPostDt["fromDate"] = $("#fromDate").val();
// 		oPostDt["toDate"] = $("#toDate").val();

		colModel.push({label :' ', 				index: 'CHK', 					name: 'CHK', 					width: 30, 	align: 'center', hidden: true}); 
		colModel.push({label :'문서명', 			index: 'PATH', 					name: 'PATH', 					width: 100, align: 'left'});
		colModel.push({label :'기안자', 			index: 'USER_NAME', 			name: 'USER_NAME',				width: 50, 	align: 'center'});
		colModel.push({label :'요청자', 			index: 'OK_USER_NAME', 			name: 'OK_USER_NAME', 			width: 50,	align: 'center', hidden: true});
		colModel.push({label :'결재자', 			index: 'ADMIN_USER_NAME', 		name: 'ADMIN_USER_NAME', 		width: 50, 	align: 'center'});
		colModel.push({label :'문서기안일', 		index: 'REGDATE', 				name: 'REGDATE', 				width: 100, align: 'center', type:"3_0"});
		colModel.push({label :'결재일', 			index: 'OKDATE', 				name: 'OKDATE', 				width: 100, align: 'center', type:"3_1"});
		colModel.push({label :'승인 상태', 		index: 'APPROVAL_STATUS', 		name: 'APPROVAL_STATUS', 		width: 50, 	align: 'center', formatter : formatColor});
		colModel.push({label :'구분',	 			index: 'APPROVAL_PIC_STATUS', 	name: 'APPROVAL_PIC_STATUS', 	width: 50,	align: 'center'});  
		colModel.push({label :'결재 구분', 		index: 'CHARGE_USER_FLAGE', 	name: 'CHARGE_USER_FLAGE', 		width: 50,	align: 'center', formatter : formatName, type:7});
		colModel.push({label :'비고', 			index: 'NOTE', 					name: 'NOTE', 					width: 250, align: 'left'});
		colModel.push({label :'CHARGE_ID', 		index: 'CHARGE_ID', 			name: 'CHARGE_ID', 				width: 10, 	hidden : true});
		colModel.push({label :'OK_USER_NO', 	index: 'OK_USER_NO', 			name: 'OK_USER_NO', 			width: 100, hidden : true});
		colModel.push({label :'AP_NO', 			index: 'AP_NO', 				name: 'AP_NO', 					width: 10, 	hidden : true});
		colModel.push({label :'TARGET_ID', 		index: 'TARGET_ID', 			name: 'TARGET_ID', 				width: 100, hidden : true});
		colModel.push({label :'APPROVAL_TYPE', 	index: 'APPROVAL_TYPE', 		name: 'APPROVAL_TYPE', 			width: 100, hidden : true});
		GridSearchTypeChk();
		searchListAppend(); 
		setSelectDate();  
		
		var oPostDt = {};
		
	    oPostDt["dayKey"] = $("#searchDay").val(); 
	    oPostDt["fromDate"] = $("#fromDate").val();
	    oPostDt["toDate"]   = $("#toDate").val();
		
		oGrid.jqGrid({
			url : "${getContextPath}/approval/searchApprovalListData",
			postData : oPostDt,
			datatype : "json",
			mtype : "POST",
			async : true,
			contentType : "application/json; charset=UTF-8",
			colModel : colModel,
			loadonce : true,
			viewrecords : true, // show the current page, data rang and total records on the toolbar
			width : oGrid.parent().width(),
			height : 555,
			multiselect : true,
			autowidth : true,
			shrinkToFit : true,
			rownumbers : false, // 행번호 표시여부
			rownumWidth : 75, // 행번호 열의 너비  
			rowNum : 50,
			rowList : [ 50, 250, 1000 ],
			pager : "#processApproveGridPager",
			onCellSelect : function(rowid, icol, cellcontent, e) {
				if (icol != 0) {
					// 팝업 호출 전 data clear
					$("#processPath").val("");
					$("#processPath").html("");
					$("#processNote").val("");
					$("#processNote").val(oGrid.getCell(rowid, 'NOTE'));
					// 테이블에서 path_ex_group_name 가져와서 넣어줘야함 
				 	var oPostDt = {};
					oPostDt["CHARGE_ID_LIST"] = oGrid.getCell(rowid,'CHARGE_ID'); 
	
					var url = "";
					 
					console.log(oGrid.getCell(rowid, 'APPROVAL_TYPE'));
					
					if(oGrid.getCell(rowid, 'APPROVAL_TYPE') == 2) {
						url = "${getContextPath}/approval/selectProcessGroupPath2";
					}else{
						url = "${getContextPath}/approval/selectProcessGroupPath";
					}
					
					oPostDt["approval_type"] = oGrid.getCell(rowid, 'APPROVAL_TYPE');
					var oJson = JSON.stringify(oPostDt);
	
					$.ajax({
							url : url,
							type : "POST",
							async : true,
							data : oJson,
							contentType : 'application/json; charset=UTF-8',
							success : function(resultMap) {
								var searchList = resultMap.searchList;
								var approvalList = resultMap.approvalList;
								var return_reason = "";
								
								if (approvalList.length > 0) {
									
									if (approvalList.APPROVAL_STATUS == 'E') { // 승인
										$('#comment_th').text('승인 의견');
									} else if (approvalList.APPROVAL_STATUS == 'D') { // 반려
										$('#comment_th').text('반려 의견');
									}
									
									return_reason = "";
									
									$.each(approvalList,function(i, s) {
										var a_status = null;
										switch(s.APPROVAL_STATUS){
											case 'W': a_status = " / 대기"; break;
											case 'D': a_status = " / 반려"; break;
											case 'E': a_status = " / 승인"; break;
											default : a_status = null; break;
										}
										
										if(s.CHARGE_USER_FLAGE == "통보자"){
											a_status = "\n"; 
										}  
											return_reason += s.USER_NAME + "("+ s.CHARGE_USER_FLAGE + ") " + a_status ;
										if(s.CHARGE_USER_FLAGE != "통보자"){
											return_reason +=  s.COMMENT == null ? "\n" : (" : " +  s.COMMENT)+ "\n";
										}
									});
								}
									$('#comment_td').text(return_reason);
								if (searchList.length > 0) {
									var sChargeId = null;
									var sText = "";
									var TargetList ="";
									var path_ex_list= "";
									
									$.each(searchList,function(i, s) {
										 
										if(oGrid.getCell(rowid, 'APPROVAL_TYPE') == 2) {
											if (sChargeId != s.CHARGE_ID) {
												
												if(sChargeId!=null){
													$("#processPath").append(path_ex_list + "<br><br>"); 
												}
												
												$("#processPath").append(document.createTextNode(s.data_processing_name));
												if (s.NOTEPAD != "" && s.NOTEPAD != null) {
													$("#processPath").append("(");
													$("#processPath").append(document.createTextNode(s.NOTEPAD));
													$("#processPath").append(")");
												}
												$("#processPath").append("<br/>");
												
												sChargeId = s.CHARGE_ID;
											}
											
											$("#processPath").append(s.NAME+ "<br>");  
											path_ex_list = s.PATH.replace("\n", "<br>");
											
											console.log(i)  
											if(searchList.length == (i+1)) $("#processPath").append("<br>"+path_ex_list + "<br><br>"); 
											
											
										}else{
											if (sChargeId != s.CHARGE_ID) {
												$("#processPath").append(document.createTextNode(s.data_processing_name));
												if (s.NOTEPAD != "" && s.NOTEPAD != null) {
													$("#processPath").append("(");
													$("#processPath").append(document.createTextNode(s.NOTEPAD));
													$("#processPath").append(")");
												}
												$("#processPath").append("<br/>");
											}
											sTarget_id = s.TARGET_ID;
											$("#processPath").append("&nbsp;&nbsp;<a style=\"color: blue; cursor: pointer;\" onclick=\"showDetail("
																	+ s.FID
																	+ ", "
																	+ s.ID
																	+ ", "
																	+ s.AP_NO
																	+ ", "
																	+ rowid
																	+ ");\">"
																	+ s.PATH
																	+ "<br/></a>"
																	+ "<input type='hidden' id='"+s.ID+"' value='"+sTarget_id+"'>");
											sChargeId = s.CHARGE_ID;
										}
										
										
									});
									
									
								}
								return;
							},
							error : function(request, status, error) {
								alert("실패 하였습니다.");
							}
							});
	
					var detailName = oGrid.getCell(rowid, 'PATH');
					$("#groupName").html(detailName);
					$("#insertPathPopup").show();
				}
	
			},
			beforeSelectRow : function(nRowid, e) {
				if (e.target.type !== "checkbox") {
					return false;
				}
			},
			ondblClickRow : function(rowid, icol, cellcontent, e) {
	
			},
		//         onCellSelect: function(rowid, icol, cellcontent, e) {
		//             if (status) {
		//                 var cbs = $("tr.jqgrow > td > input.cbox:disabled", oGrid[0]);
		//                 cbs.removeAttr("checked");
	
		//                 oGrid[0].p.selarrrow = oGrid.find("tr.jqgrow:has(td > input.cbox:checked)").map(function() { return this.id; }).get();
		//             }
		//         },
		//         loadComplete: function(data) {
		//             var idArry = oGrid.jqGrid('getDataIDs'); //grid의 id 값을 배열로 가져옴
	
		//             for(var i=0 ; i < idArry.length; i++){
		//                 var ret =  oGrid.getRowData(idArry[i]); // 해당 id의 row 데이터를 가져옴
	
		//                 if(ret.APPROVAL_STATUS != 'W'){ //해당 row의 특정 컬럼 값이 1이 아니면 multiselect checkbox disabled 처리
		//                    //해당 row의 checkbox disabled 처리 "jqg_list_" 이 부분은 grid에서 자동 생성
		//                    $("#jqg_processApproveGrid_"+idArry[i]).attr("disabled", true); 
		//                 }
		//             }
		//         },
		//         beforeSelectRow: function (rowid, e) {
		//             var $myGrid = $(this),
		//                 i = $.jgrid.getCellIndex($(e.target).closest('td')[0]),
		//                 cm = $myGrid.jqGrid('getGridParam', 'colModel');
		//             return (cm[i].name === 'cb');
		//         }
		});
	}

	//일괄 결재
	function reqApprovalAll() {

		$.ajax({
			type : "POST",
			url : "${getContextPath}/approval/searchApprovalAllListData",
			async : true,
			success : function(searchList) {

				if (searchList.length > 0) {
					var sGroupId = undefined;
					var sText = "";
					
					console.log(searchList);
					$.each(searchList, function(i, s) {

						if (sGroupId != s.GROUP_ID) {
							sText += "\r\n- (" + s.CHARGE_NAME + ") "
									+ s.data_processing_name + "\r\n";
						}

						sText += s.PATH + "\r\n";
						sGroupId = s.GROUP_ID;
					});
					sText = sText.replace(/^\/r\/n/g, "");
					$("#excepPath").val(sText);
				}

				bApprovalAll = true;
				oApprovalAll = searchList;

				if (oApprovalAll.length <= 0) {
					alert("처리할 결재가 존재 하지 않습니다.");
					return;
				}

				$("#selecetProcessPopup").show();
				return;
			},
			error : function(request, status, error) {
				alert("실패 하였습니다.");
			}
		});
	}

	function saveApprovalAll() {
		var aChargeId = [];
		for (var i = 0; i < oApprovalAll.length; i += 1) {
			if (aChargeId.indexOf(oApprovalAll[i]["CHARGE_ID"]) < 0)
				aChargeId.push(oApprovalAll[i]["CHARGE_ID"]);
		}

		var sReason = $("#reason").val();
		var apprType = $("input:radio[name=trueFalseChk]:checked").val();

		if (!apprType) {
			alert("사유를 선택하세요.");
			return false;
		}

		var oPostDt = {};
		oPostDt["chargeIdList"] = aChargeId.join(",");
		oPostDt["reason"] = sReason
		oPostDt["apprType"] = apprType;

		var oJson = JSON.stringify(oPostDt);

		$.ajax({
			url : "${getContextPath}/approval/updateProcessApproval",
			type : "POST",
			async : false,
			data : oJson,
			contentType : 'application/json; charset=UTF-8',
			success : function(result) {

				if (result.resultCode != "0") {
					alert(result.resultCode + "("+ result.resultMessage + "} 처리 등록을 실패 하였습니다.");
					return;
				}

				alert("처리를 등록 하였습니다.");

				$("#processApproveGrid").setGridParam({
					url : "${getContextPath}/approval/searchApprovalListData",
					postData : $("#processApproveGrid")
							.getGridParam('postData'),
					datatype : "json"
				}).trigger("reloadGrid");

				$("#deletionRegistPopup").hide();
				$("input:radio[name=trueFalseChk]").prop("checked",false);
				return;
			},
			error : function(request, status, error) {
				alert("처리 등록을 실패 하였습니다.");
				$("input:radio[name=trueFalseChk]").prop("checked",false);
				return;
			}
		});

		bApprovalAll = false;
		oApprovalAll = {};
		$("#excepPath").val('');
	}

	// 결재 리스트 조회
	function reqApproval() {
		var aSelRow = $("#processApproveGrid").getGridParam('selarrrow'); // 체크된 row id들을 배열로 반환

		var sStatus = "";
		var bException = false;
		var aChargeId = [];
		var sOkUserNo = "";
		var approval_pic_status = "";
		var sUserNo = '${memberInfo.USER_NO}';

		console.log(aSelRow);
		for (var i = 0; i < aSelRow.length; i += 1) {
			aChargeId.push($("#processApproveGrid").getCell(aSelRow[i],'CHARGE_ID'));
			sStatus = $("#processApproveGrid").getCell(aSelRow[i],'APPROVAL_STATUS');
			sOkUserNo = $("#processApproveGrid").getCell(aSelRow[i],'OK_USER_NO');
			approval_type = $("#processApproveGrid").getCell(aSelRow[i],'APPROVAL_TYPE');
			
			console.log("sOkUserNo", sOkUserNo);
			console.log("sUserNo",sUserNo);

			if ((sStatus == "D") || (sStatus == "E")) {
				bException = true;
			}
			
			sOkUserNo = $("#processApproveGrid").getCell(aSelRow[i],'OK_USER_NO');
			if (sOkUserNo != sUserNo) {
				alert("올바른 항목을 선택하세요.");
				return;
			}
			
			
		}

		if (bException) { 
			alert("이미 처리된 항목이 존재합니다.");
			return;
		}
		
		var oPostDt = {};
		oPostDt["CHARGE_ID_LIST"] = aChargeId.join(",");
		oPostDt["approval_type"] = approval_type;
		var oJson = JSON.stringify(oPostDt);
		
		$.ajax({
			url : "${getContextPath}/approval/selectProcessGroupPath",
			type : "POST",
			async : true,
			data : oJson,
			contentType : 'application/json; charset=UTF-8',
			success : function(searchList) {

				console.log("searchList", searchList);
				console.log("searchList", searchList.length);
				if (searchList.searchList.length > 0) {
					var sChargeId = "";
					var sText = "";
					
					$.each(searchList.searchList, function(i, s) {
						if (sChargeId != s.CHARGE_ID) {
							sText += "\r\n- " + s.data_processing_name+ "\r\n";
						}

						sText += s.PATH + "\r\n";
						sChargeId = s.CHARGE_ID;
					});
					sText = sText.replace(/^\/r\/n/g, "");
					console.log("sText", sText);
					$("#excepPath").val(sText);
				}
				return;
			},
			error : function(request, status, error) {
				alert("실패 하였습니다.");
			}
		});

		$("#selecetProcessPopup").show();

		if (aChargeId.length == 0) {
			alert("결재 요청된 건만 결재할 수 있습니다.");
			return;
		}

	}
	// 결재 저장
	function saveApproval() {
		var sStatus = "";
		var bException = false;
		var aChargeId = [];

		var aSelRow = $("#processApproveGrid").getGridParam('selarrrow'); //체크된 row id들을 배열로 반환

		for (var i = 0; i < aSelRow.length; i += 1) {
			aChargeId.push($("#processApproveGrid").getCell(aSelRow[i],'CHARGE_ID'));
			sStatus = $("#processApproveGrid").getCell(aSelRow[i],'APPROVAL_STATUS');

			if ((sStatus == "D") || (sStatus == "E")) {
				bException = true;
			}
		}

		if (bException) {
			alert("이미 처리된 항목이 존재합니다.");
			return;
		}
		if (aChargeId.length == 0) {
			alert("결재요청 항목을 선택하세요.");
			return;
		}

		var sReason = $("#reason").val();
		var apprType = $("input:radio[name=trueFalseChk]:checked").val();

		if (!apprType) {
			alert("사유를 선택하세요.");
			return false;
		}

		var oPostDt = {};
		oPostDt["chargeIdList"] = aChargeId.join(",");
		oPostDt["reason"] = sReason
		oPostDt["apprType"] = apprType;

		var oJson = JSON.stringify(oPostDt);

		$.ajax({
			url : "${getContextPath}/approval/updateProcessApproval",
			type : "POST",
			async : false,
			data : oJson,
			contentType : 'application/json; charset=UTF-8',
			success : function(result) {
				if (result.resultCode != "0") {
					alert(result.resultCode + "("+ result.resultMessage+ "} 처리 등록을 실패 하였습니다.");
					return;
				}
				console.log(apprType);
				alert("정상적으로 " + (apprType == 'E' ? '승인' : '반려')+ " 처리 되었습니다.");
				$("#processApproveGrid").setGridParam({
					url : "${getContextPath}/approval/searchApprovalListData",
					postData : $("#processApproveGrid")
							.getGridParam('postData'),
					datatype : "json"
				}).trigger("reloadGrid");
				$("#deletionRegistPopup").hide();
				$("input:radio[name=trueFalseChk]").prop("checked",false);
				return;
			},
			error : function(request, status, error) {
				alert("처리 등록을 실패 하였습니다.");
				$("input:radio[name=trueFalseChk]").prop("checked",false);
				return;
			}
		});
	}
	//문서 기안일
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

		var oFromDate = new Date(oToday.setDate(oToday.getDate() - 90));
		$("#fromDate").val(getFormatDate(oFromDate));
	}

	$(function() {
		$.datepicker.setDefaults({
			closeText : "확인",
			currentText : "오늘",
			prevText : '이전 달',
			nextText : '다음 달',
			monthNames : [ '1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월','9월', '10월', '11월', '12월' ],
			monthNamesShort : [ '1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월','9월', '10월', '11월', '12월' ],
			dayNames : [ '일', '월', '화', '수', '목', '금', '토' ],
			dayNamesShort : [ '일', '월', '화', '수', '목', '금', '토' ],
			dayNamesMin : [ '일', '월', '화', '수', '목', '금', '토' ],
			yearRange : 'c-5:c+5'
		});
	});

	//검색
	function fn_search(obj) {
		if ($("#fromDate").val() > $("#toDate").val()) {
			alert("입력한 끝 날짜가 시작 날짜 보다 빠릅니다.");
			return;
		}
		// 정탐/오탐 리스트 그리드
		var oPostDt = {};
		oPostDt["target_id"] = $("#hostSelect").val();
		oPostDt["status"] = $("select[name='statusList']").val();
		oPostDt["user_nm"] = $("#schUserNm").val();
		oPostDt["path"] = $("#schPath").val();
		oPostDt["fromDate"] = $("#fromDate").val();
		oPostDt["toDate"] = $("#toDate").val();

		$("#processApproveGrid").clearGridData();
		$("#processApproveGrid").setGridParam({
			url : "${getContextPath}/approval/searchApprovalListData",
			postData : oPostDt,
			datatype : "json"
		}).trigger("reloadGrid");
	}

	//
	function getFormatDate(oDate) {
		var nYear = oDate.getFullYear(); // yyyy 
		var nMonth = (1 + oDate.getMonth()); // M 
		nMonth = ('0' + nMonth).slice(-2); // month 두자리로 저장 

		var nDay = oDate.getDate(); // d 
		nDay = ('0' + nDay).slice(-2); // day 두자리로 저장

		return nYear + '-' + nMonth + '-' + nDay;
	}

	function downLoadExcel() {
		resetFomatter = "downloadClick";

		var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth() + 1; //January is 0!
		var yyyy = today.getFullYear();
		if (dd < 10) {
			dd = '0' + dd
		}

		if (mm < 10) {
			mm = '0' + mm
		}

		today = yyyy + "" + mm + dd;
		console.log(today);

		$("#processApproveGrid").jqGrid("exportToCsv", {
			separator : ",",
			separatorReplace : "", // in order to interpret numbers
			quote : '"',
			escquote : '"',
			newLine : "\r\n", // navigator.userAgent.match(/Windows/) ? '\r\n' : '\n';
			replaceNewLine : " ",
			includeCaption : true,
			includeLabels : true,
			includeGroupHeader : true,
			includeFooter : true,
			fileName : "정탐/오탐_검출_리스트_" + today + ".csv",
			mimetype : "text/csv; charset=utf-8",
			event : resetFomatter,
			returnAsString : false
		});
	}

	$("#taskWindowClose").click(function(e) {
		$("#taskWindow").hide();
	});

	$("#pathWindowClose").click(function(e) {
		$("#pathWindow").hide();
	});

	function showDetail(fid, id, ap_no, rowid) {
		$("#taskWindow").hide();
		$("#pathWindow").hide();
		var tid = $("#"+id+"").val();
		
		if (fid == "0") {
			var pop_url = "${getContextPath}/popup/approvalDetail";
			var winWidth = 1142;
			var winHeight = 365;
			var popupOption = "width=" + winWidth + ", height=" + winHeight + ", scrollbars=no, resizable=no, location=no";
			//var pop = window.open(pop_url,"detail",popupOption);
			var pop = window.open(pop_url, id, popupOption);
			/* popList.push(pop);
			sessionUpdate() */;

			//pop.check();

			var newForm = document.createElement('form');
			newForm.method = 'POST';
			newForm.action = pop_url;
			newForm.name = 'newForm';
			//newForm.target='detail';
			newForm.target = id;

			var input_id = document.createElement('input');
			input_id.setAttribute('type', 'hidden');
			input_id.setAttribute('name', 'id');
			input_id.setAttribute('value', id);
			
			var input_tid = document.createElement('input');
			input_tid.setAttribute('type','hidden');
			input_tid.setAttribute('name','tid');
			input_tid.setAttribute('value',tid);

			var input_ap = document.createElement('input');
			input_ap.setAttribute('type', 'hidden');
			input_ap.setAttribute('name', 'ap_no');
			input_ap.setAttribute('value', ap_no);

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

	function getLowPath(id, tid, ap_no) {

		var pop_url = "${getContextPath}/popup/lowPath";
		var winWidth = 1142;
		var winHeight = 365;
		var popupOption = "width=" + winWidth + ", height=" + winHeight
				+ ", scrollbars=no, resizable=no, location=no";
		//var pop = window.open(pop_url,"lowPath",popupOption);
		var pop = window.open(pop_url, id, popupOption);
		/* popList.push(pop);
		sessionUpdate(); */

		//pop.check();
		var newForm = document.createElement('form');
		newForm.method = 'POST';
		newForm.action = pop_url;
		newForm.name = 'newForm';
		//newForm.target='lowPath';
		newForm.target = id;

		var input_id = document.createElement('input');
		input_id.setAttribute('type', 'hidden');
		input_id.setAttribute('name', 'hash_id');
		input_id.setAttribute('value', id);
		
		var input_tid = document.createElement('input');
		input_tid.setAttribute('type','hidden');
		input_tid.setAttribute('name','tid');
		input_tid.setAttribute('value',tid);

		var input_ap = document.createElement('input');
		input_ap.setAttribute('type', 'hidden');
		input_ap.setAttribute('name', 'ap_no');
		input_ap.setAttribute('value', ap_no);

		newForm.appendChild(input_id);
		newForm.appendChild(input_tid);
		newForm.appendChild(input_ap);
		document.body.appendChild(newForm);
		newForm.submit();

		document.body.removeChild(newForm);

	}
	
	var formatColor = function(cellvalue, options, rowObject) {
		var status = "";
		
		console.log(cellvalue);
		
		if (cellvalue == 'E') {
			status = "승인";
		} else if (cellvalue == 'D') {
			status = "반려";
		} else if (cellvalue == 'W') {
			status = "대기";
		}

		if (resetFomatter != "downloadClick") {
			if (status == "대기") {
				status = '<label style="color:green">' + status + '</label>'
			} else if (status == "반려") {
				status = '<label style="color:red">' + status + '</label>'
			}
		}

		return status;
	};
	
	var formatName = function(cellvalue, options, rowObject) {
		var status = "";
		if (cellvalue == 'approval_6') {
			status = "합의자";
		} else if (cellvalue == 'approval_8') {
			status = "결재자";
		} 

		return status;
	};
</script>
</body>
</html>