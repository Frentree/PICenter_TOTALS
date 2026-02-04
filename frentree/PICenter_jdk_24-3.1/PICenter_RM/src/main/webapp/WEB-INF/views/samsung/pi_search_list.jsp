<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../../include/header.jsp"%>
<style>
	.ui-jqgrid tr.ui-row-ltr td{
		cursor: pointer;
		white-space: nowrap;
	}
	.clickGrid{
		background: #dadada
	}
</style> 
<!-- 검출관리 -->

<!-- section -->
<section>
	<!-- container -->
	<div class="container">
	<%-- <%@ include file="../../include/menu2.jsp"%> --%>
		<!-- content -->
<%-- 		<h3 style="display: inline; top: 25px;">${picSession.version}</h3> --%>
		<h3 style="display: inline; top: 25px;">조치계획 승인요청</h3>
		<p style="position: absolute; top: 33px; left: 221px; font-size: 13px; color: #9E9E9E;">처리 진행 상태(오탐)를 확인 하실수 있습니다.</p>
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
                    	<button type="button" name="button" class="btn_down" id="btnDeleteItem">항목 삭제</button>
						<button type="button" name="button" class="btn_down" id="btnApprovalRequest">결재 요청</button>
						<button type="button" name="button" class="btn_down" id="btnDownloadExel">다운로드</button>
                    </div>
                    <div style="float: right;">
						<h5 style="width: 24px; height: 24px; margin-top: 2px; background-size : 100%; background-image: url('${pageContext.request.contextPath}/resources/assets/images/pi_list.png');" id="tableShow">&nbsp;</h5>
					</div>
                </div>
			</div>
			<div class="left_box2" style="overflow: hidden; max-height: 635px; height: 635px; margin-top: 10px">
  					<table id="processGrid"></table>
  					<div id="Pages"></div>
			</div>
		</div>
	</div>
	<!-- container --
<!-- 팝업창 시작 : 정탐/오탐 결재요청 -->
<div id="ApprovalPopup" class="popup_layer" style="display:none">
	<div class="popup_box" style="width: 800px; height: 425px; top: 48%; left: 47%; padding: 10px; background: #f9f9f9;">
	<img class="CancleImg" id="btnCancleApprovalPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 id="approvalRegis" style="color: #222; padding: 0; box-shadow: none;">승인 요청 등록</h1>
			<p style="position: absolute; top: 13px; left: 138px; font-size: 12px; color: #9E9E9E;"></p>
		</div>
		<div class="popup_content">
			<div class="content-box" style="height: 530px; background: #fff; border: 1px solid #c8ced3;">
				<!-- <h2>세부사항</h2>  -->
				<table class="popup_tbl">
					<colgroup>
						<col width="130">
						<col width="*">
						<col width="130">
					</colgroup>
					<tbody>
						<tr>
							<th>요청자</th>
							<td colspan="2">
								<input type="text" id="reg_user_name" value="" class="edt_sch" style="border: 0px solid #cdcdcd; " readonly>
								<input type="text" id="reg_user_no" value="" class="edt_sch" style="border: 0px solid #cdcdcd; display:none;">
							</td>
						</tr>
						<tr>
							<th>요청일자</th>
							<td colspan="2"><input type="text" id="regdate" value="" class="edt_sch" style="border: 0px solid #cdcdcd;" readonly></td>
						</tr>
						<tr>
							<th rowspan="2">의견</th>
							<td colspan="2">
								<textarea id="comment" class="edt_sch" placeholder="" style="border: 1px solid #cdcdcd; width: 100%; height: 120px; margin-top: 5px; margin-bottom: 5px; resize: none;"></textarea>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<span style="font-size: 12px; color: #9E9E9E;">저장 시 전자 결재 문서가 생성되며 작성한 의견은 결재자가 참고하는 내용이니<br> 상세히 작성 바랍니다.</span>
							</td>
						</tr>
						<tr>
							<th>전자결재 기안자</th>
                            <td class="btn_area" style="padding-left: 30px !important; text-align: right;" colspan="2">
								<button type="button" id="jqgridUpBtn" style="margin: 0; padding: 0 15px; width: 100px;">▲</button>
								<button type="button" id="jqgridDownBtn" style="margin: 0; padding: 0 15px; width: 100px;">▼</button>
								<button type="button" id="btnUserSelectPopup" style="margin: 0; padding: 0 15px; width: 100px;">추가</button>
							</td>
						</tr>
						<tr>
							<th></th>
							<td colspan="2">
								<table id="approvalUserGrid"></table>
  								<div id="approvalUserPages"></div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>  
		</div>  
		<div class="popup_btn">
			<div class="btn_area" style="padding: 10px 0;">
<!-- 				<p style="position: absolute; bottom: 17px; right: 148px; font-size: 12px; color: #2C4E8C;">전자결재 문서가 생성됩니다.</p> -->
				<button type="button" id="btnApprovalSave" value="W">저장</button>
				<button type="button" id="btnApprovalCancel">취소</button>
			</div>
		</div>
	</div>
</div>
<!-- 팝업창 종료 -->
<!-- 결재 요청 전 안내 팝업-->
<div id="approvalAlert" class="popup_layer" style="display:none">
	<div class="popup_box" style="width: 800px; height: 425px; top: 48%; left: 47%; padding: 10px; background: #f9f9f9;">
	<h1 id="approvalAlertNm" style="color: #222; padding: 0; box-shadow: none;"></h1>
		<div class="popup_content">
			<div class="content-box" id="approvalAlertContent"  style="height: 503px; background: #fff; border: 1px solid #c8ced3; white-space: pre-wrap; overflow-y:auto; ">
				  
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area" style="padding: 10px 0;">
<!-- 			<p style="position: absolute; bottom: 17px; right: 291px; font-size: 12px; color: red; text-align : center;">미 확인시 결재가 진행되지 않습니다.</p> -->
				<button type="button" id="btnApprovalAlert" >확인</button>
			</div>
		</div>
	</div>
</div>
<!-- 팝업창 종료 -->

<!-- 팝업창 시작 담당자 지정 -->
<div id="userSelect" class="popup_layer" style="display:none;">
	<div class="popup_box" style="height: 200px; padding: 10px; background: #f9f9f9;">
	<img class="CancleImg" id="btnCancleUserSelect" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9;"> 
			<h1 style="color: #222; padding: 0; box-shadow: none; float: left; padding-right: 5px;">결재자 지정</h1>
			<c:forEach var="status" items="${approvalStatusList}">
				<input type="radio" name="apporvalUser" value="${status.STATUS}" style="width: 12px; font-size: 12px">${status.NAME}
			</c:forEach>
			
		</div>
		<div class="popup_content">
			<div class="content-box" style="height: 425px; background: #fff; border: 1px solid #c8ced3;">
				<table style="padding-bottom: 10px;">
					<tbody>
					<tr>
						<th style="text-align: center; width:100px">팀명</th>
						<td><input type="text" style="width: 186px; font-size: 12px; padding-left: 5px;" size="10" id="searchTeamName" placeholder="팀명을 입력하세요"></td>
                           <th style="text-align: center; width:100px">기안자</th>
						<td><input type="text" style="width: 186px; font-size: 12px; padding-left: 5px;" size="10" id="searchName" placeholder="기안자를 입력하세요."></td>
                           <td rowspan="3" >
                          		<input type="button" name="button" class="btn_look_approval" id="popBtnSearch" style="margin-left: 10px;">
                          	</td>
					</tr>
					</tbody>
				</table>
  				<table id="userGrid"></table>
   				<div id="userGridPager"></div>
			</div>
		</div>
		<!-- <div class="popup_btn">
			<div class="btn_area" style="padding: 10px 0;">
				<button type="button" id="btnUserSelect">선택</button>
				<button type="button" id="btnUserCancel">취소</button>
			</div>
		</div> -->
	</div>
</div>
<!-- 팝업창 종료 -->

<!-- 팝업창 시작 정탐/오탐 신청 내역-->
<div id="insertPathExcepPopup" class="popup_layer" style="display:none">
	<div class="popup_box" style="height: 542px; top: 49%; left: 44%; padding: 10px; background: #f9f9f9; width:890px;">
	<img class="CancleImg" id="btnCancleInsertPathExcepPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 id="groupName" style="color: #222; padding: 0; box-shadow: none;"></h1>
		</div> 
		<div class="popup_content">
			<div class="content-box" style="height: 455px; background: #fff; border: 1px solid #c8ced3;">
				<!-- <h2>세부사항</h2>  -->
				<table class="popup_tbl">
					<colgroup>
						<col width="130">
						<col width="*">
					</colgroup>
					<tbody> 
						<tr>
							<th style="border-bottom: 1px solid #cdcdcd;">내용</th>
							<td style="border-bottom: 1px solid #cdcdcd;">
								<div style="overflow-y: auto; height: 280px;">
									<table style="border: 0px solid #cdcdcd; width: 696px; height: 266px; margin-top: 5px; margin-bottom: 5px; resize: none; " >
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
<!-- 						<tr> -->
<!-- 							<th style="border-bottom: 1px solid #cdcdcd;">등록서버</th> -->
<!-- 							<td style="border-bottom: 1px solid #cdcdcd;"> -->
<!-- 								<input type="text" id="regisServer" value="" class="edt_sch" style=" border: 0px solid #cdcdcd;" readonly> -->
<!-- 							</td>    -->
<!-- 						</tr> --> 
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

</section>

<body data-context-path="<%= request.getContextPath() %>">
    <script src="/resources/assets/js/navGrid2.js"></script>
</body>  
<!-- section -->
<%@ include file="../../include/footer.jsp"%>
<script>
var resetFomatter = null;
var beforeCheckGrid = null;
var beforeCheckStatus = true;
var colModel = [];
GridName = "#processGrid";
requestUrl = "${getContextPath}/approval/searchProcessList" ; 
$(document).ready(function () {

	// 그리드 다운로드
	$("#btnDownloadExel").click(function(){
		downLoadExcel();
	});
	
    // 상급자 설정
    var boss_user_name = "${teamManager.USER_NAME}"; 
    var boss_jikguk = "${teamManager.JIKGUK}"; 
    var boss_user_no = "${teamManager.USER_NO}";

    $("#ok_user_no").val(boss_user_no);
    $("#ok_user_name").val(boss_user_name + " " + boss_jikguk);

    // SelectList를 선택하면 선택된 화면으로 이동한다.
    $("#selectList").change(function () {
        location.href = $("#selectList").val();
    });

    // 검색
    $("#statusList").change(function() {
        fn_search();
    });

    // 검색
    $("#btnSearch").click(function() {
    	fn_search();
    });

    $("#btnRescan").click(function() {
    	fn_rescan();
    });
    
    $("#popBtnSearch").click(function() {
    	popSearch();
    });
    
    $('#searchTeamName, #searchName').keyup(function(e) {
		if (e.keyCode == 13) {
			popSearch();
	    }        
	});

 	// 결재 요청전 항목 삭제 버튼
    $("#btnDeleteItem").click(function() {
    	delItem();
	});
    
    // 결재 요청 버튼
    $("#btnApprovalRequest").click(function() {
    	reqApproval();
	});

	// 결재 요청 > 취소
	$("#btnApprovalCancel").click(function (e) {
		$("#ApprovalPopup").hide();
		$("#comment").val("");
	});
	
	$("#btnCancleApprovalPopup").click(function (e) {
		$("#ApprovalPopup").hide();
		$("#comment").val("");
	});

	// 확인
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

	// 결재 요청 - 담당자 선택
	$("#btnUserSelectPopup").click(function (e) {
	    $("#userSelect").show();
	    searchAppUserSelect();
	});

	// 결재 요청 - 담당자 선택 취소
	$("#btnUserCancel").on("click", function(e) {
		$("#searchName").val("");
        $("#searchTeamName").val("");
        
		$("#userSelect").hide();
	});
	
	$("#btnCancleUserSelect").on("click", function(e) {
		$("#searchName").val("");
        $("#searchTeamName").val("");
        
        $("input:radio[name='apporvalUser']:input[value='approval_8']").prop("checked", true);
        
        $("#userGrid").clearGridData();
        
		$("#userSelect").hide();
	});

	// 결재 요청 - 저장
	$("#btnApprovalSave").click(function(e) {
		saveApproval();
	});
	
    loadJqGrid($("#processGrid"));
});

function loadApprovalUserGrid(){

	var gridWidth = $("#approvalUserGrid").parent().width();
	
	$("#approvalUserGrid").jqGrid({
<%-- 		url: "<%=request.getContextPath()%>/user/approvalList", --%>
		datatype: "local",
		//data: temp,
	   	mtype : "POST",
	   	colNames:['구분', '구분','구분2', '이름', '사번', '직책', '소속','필수', '필수', '', '', '메일'],
		colModel: [
			{ index: 'NAME', 			name: 'NAME',			width: 50, 	align: 'center'},
			{ index: 'STATUS', 			name: 'STATUS',			width: 50, 	align: 'center', hidden:true},
			{ index: 'STATUS2', 		name: 'STATUS2',		width: 50, 	align: 'center', hidden:true},
			{ index: 'USER_NAME', 		name: 'USER_NAME',		width: 50, 	align: 'center'},
			{ index: 'USER_NO',			name: 'USER_NO',		width: 0, 	hidden:true},
			{ index: 'JIKGUK', 			name: 'JIKGUK',			width: 50, 	align: 'center'},
			{ index: 'COM', 			name: 'COM', 			width: 50, 	align: 'center'},
			{ index: 'CERTAINLY', 		name: 'CERTAINLY', 		width: 50, 	align: 'center', formatter:statusChk},
			{ index: 'IDX', 			name: 'IDX', 			width: 40,  align: 'center', hidden:true},
			{ index: 'IDX2', 			name: 'IDX2', 			width: 30,  align: 'center', formatter:deletBtn},
			{ index: 'GIDX', 			name: 'GIDX', 			width: 30,  align: 'center', formatter:changeData, hidden:true},
			{ index: 'USER_EMAIL', 		name: 'USER_EMAIL', 			width: 40,  align: 'center', hidden:true}
		],
		loadonce:true,
	   	autowidth: true,
		shrinkToFit: true,
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: gridWidth,
		height: 125,
		rownumbers : false, // 행번호 표시여부
		rownumWidth : 35, // 행번호 열의 너비	
		rowNum:20,
		rowList:[25,50,100],
	   	//editurl: 'clientArray',
	   	//cellEdit : true,
	   	//cellsubmit: 'clientArray',
	   	//multiselect:true,
		pager: "#approvalUserPages",
		//jqgrid의 특성상 - rowpos의 이벤트는 onSelectCell, beforeEditCell 다 해주어야 함
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	

	  		if(beforeCheckStatus){
		  		/* 이전 체크된 값 제거 */
		  		if(beforeCheckGrid != null){
			  		$("#approvalUserGrid #"+beforeCheckGrid).removeClass("clickGrid");
		  		}
		  		
		  		/* 같은 그리드 선택 시 선택 취소*/
		  		if(beforeCheckGrid == rowid){
			  		$("#approvalUserGrid #"+rowid).removeClass("clickGrid");
			  		beforeCheckGrid = null;
		  		}else{
			  		$("#approvalUserGrid #"+rowid).addClass("clickGrid");
			  		beforeCheckGrid = rowid;
		  		}
	  		}else{
	  			beforeCheckStatus = true;
	  		}
	  		
	  	},
	  	onCellSelect : function(rowid){
	  	},
	  	afterSaveCell : function(rowid,name,val,iRow,ICol){ // 로우 데이터 변경하고 엔터치거나 다른 셀 클릭했을때 발동
        },
	  	afterSaveRow : function(rowid,name,val,iRow,ICol){ // 로우 데이터 변경하고 엔터치거나 다른 셀 클릭했을때 발동
        },
		loadComplete: function(data) {
			
			console.log(data);
			var ids = $("#approvalUserGrid").getDataIDs() ;
            $.each(ids, function(idx, rowId) {
                rowData = $("#approvalUserGrid").getRowData(rowId, true) ;
                var approval_status = "";
                
                if(rowData.STATUS == "approval_6"){ // 합의자
                	approval_status = "H";
                }else if(rowData.STATUS == "approval_7"){ // 통보자
                	approval_status = 'Y'
                }else if(rowData.STATUS == "approval_8"){ // 결재자
                	approval_status = 'B'
                }
                $("#approvalUserGrid").setCell(rowId, 'STATUS2', approval_status);
            });
                
			//console.log(data);
	    },
	    gridComplete : function() {
	    }
	});
	
	var postData = {};
	$("#approvalUserGrid").setGridParam({
		url:"<%=request.getContextPath()%>/setting/approvalList", 
		postData : postData, 
		datatype:"json" 
		}).trigger("reloadGrid");
}

$("#jqgridUpBtn").click(function(){
	
	var rowCnt = $("#approvalUserGrid").jqGrid("getRowData").length;
	var totalRowData = $("#approvalUserGrid").getRowData();
	var beforeRowId = 0;
	
	var rowId = $("#approvalUserGrid").jqGrid("getGridParam", "selrow");
	
	if(rowId){
		var totalRows = $("#approvalUserGrid").jqGrid("getGridParam", 'reccount');
		var row = $("#approvalUserGrid").find("#"+rowId);
		var rowData = $("#approvalUserGrid").jqGrid("getRowData", rowId);
		var index = $("#approvalUserGrid").jqGrid("getInd", rowId);
		var prevRowId = $("#approvalUserGrid").jqGrid("getDataIDs")[index -2];
		
		if(prevRowId){

			for(a=0; a < rowCnt ; a++){
				if(rowId == totalRowData[a].GIDX){
					beforeRowId = a-1;
					break;
				}
			}
			var beforeRowData = $("#approvalUserGrid").jqGrid("getRowData", totalRowData[beforeRowId].GIDX);
			
			console.log(rowData);
			console.log(beforeRowData);
			
			if(rowData.STATUS != beforeRowData.STATUS){
				alert(rowData.NAME + "는 " + beforeRowData.NAME+"보다 먼저 올 수 없습니다.");
				return;
			}
			
			if(rowData.STATUS == "H" && rowData.CERTAINLY == "Y" && beforeRowData.CERTAINLY != "Y"){
				alert("필수 합의자는 다른 합의자보다 먼저 올 수 없습니다.");
				return;
			}
			
			$("#approvalUserGrid").jqGrid('delRowData', rowId);
			$("#approvalUserGrid").jqGrid('addRowData', rowId, rowData, 'before', prevRowId);
			
			var prevRow = row.prev();
			if (prevRow.length){
				row.insertBefore(prevRow);
			}
			
		}
	}
	
	/* Row 선택 유지*/
	beforeCheckStatus=false;
	$("#approvalUserGrid").jqGrid("setSelection", rowId, true);
	$("#approvalUserGrid #"+rowId).addClass("clickGrid");
});
$("#jqgridDownBtn").click(function(){
	
	var rowCnt = $("#approvalUserGrid").jqGrid("getRowData").length;
	var totalRowData = $("#approvalUserGrid").getRowData();
	var beforeRowId = 0;
	
	var rowId = $("#approvalUserGrid").jqGrid("getGridParam", "selrow");
	
	if(rowId){
		var totalRows = $("#approvalUserGrid").jqGrid("getGridParam", 'reccount');
		var row = $("#approvalUserGrid").find("#"+rowId);
		var rowData = $("#approvalUserGrid").jqGrid("getRowData", rowId);
		var index = $("#approvalUserGrid").jqGrid("getInd", rowId);
		var nextRowId = $("#approvalUserGrid").jqGrid("getDataIDs")[index];

		if(nextRowId){
			
			for(a=0; a < rowCnt ; a++){
				if(rowId == totalRowData[a].GIDX){
					beforeRowId = a+1;
					break;
				} 
			}
			var beforeRowData = $("#approvalUserGrid").jqGrid("getRowData", totalRowData[beforeRowId].GIDX);
			
			if(rowData.STATUS != beforeRowData.STATUS){
				alert(rowData.NAME + "는 " + beforeRowData.NAME+"보다 나중에 올 수 없습니다.");
				return;
			}
			
			if(rowData.STATUS == "H" && rowData.CERTAINLY != "Y" && beforeRowData.CERTAINLY == "Y"){
				alert("필수 합의자는 다른 합의자보다 먼저 올 수 없습니다.");
				return;
			}
			
			$("#approvalUserGrid").jqGrid('delRowData', rowId);
			$("#approvalUserGrid").jqGrid('addRowData', rowId, rowData, 'after', nextRowId);
			
			var prevRow = row.prev();
			if (prevRow.length){
				row.insertBefore(prevRow);
			}
			
			
		}
	}
	
	/* Row 선택 유지*/
	$("#approvalUserGrid").jqGrid("setSelection", rowId, true);
	$("#approvalUserGrid #"+rowId).addClass("clickGrid");
});

$("#btnApprovalSavee").click(function() {
	var rowData = $("#approvalUserGrid").getRowData();
});


// 정탐/오탐 리스트 그리드 조회
function loadJqGrid(oGrid)
{
    // 정탐/오탐 리스트 그리드
   
    colModel.push({label: 'IDX',				index: 'IDX',             		name: 'IDX',             		width: 10,  hidden:true}); 
    colModel.push({label: '호스트', 	    		index: 'NAME',           		name: 'NAME',             		width: 80,  align: 'center', formatter: hostName, type:10, searchType : false});
    colModel.push({label: '호스트 CNT', 			index: 'NAME_CNT',       		name: 'NAME_CNT',         		width: 80, 	hidden:true});
    colModel.push({label: '조치자',				index: 'USER_NAME',         	name: 'USER_NAME',         		width: 50, 	align: 'center'}); 
    colModel.push({label: '조치자 사번',			index: 'USER_NO',         		name: 'USER_NO',         		width: 50, 	align: 'center', type:99, hidden:true}); 
    colModel.push({label: '문서명',				index: 'FILENAME',        		name: 'FILENAME',        		width: 200, align: 'left'});   
    colModel.push({label: '문서 저장일',			index: 'REGDATE',         		name: 'REGDATE',         		width: 100, align: 'center', type:"3_0", searchType : false});
    colModel.push({label: '문서 기안일',			index: 'OKDATE',          		name: 'OKDATE',          		width: 100, align: 'center', type:"3_1", searchType : false});
    colModel.push({label: '승인 상태',				index: 'APPROVAL_STATUS', 		name: 'APPROVAL_STATUS', 		width: 50,  align: 'center', formatter:formatColor});
    colModel.push({label: '구분',					index: 'CHARGE_USER_FLAGE', 	name: 'CHARGE_USER_FLAGE', 		width: 50,  align: 'center', formatter:formatName});
    colModel.push({label: '상태1',				index: 'STATUS', 		  		name: 'STATUS', 		   		width: 50,  align: 'center',  hidden:true}); 
    colModel.push({label: '비고',					index: 'NOTE',            		name: 'NOTE',           		width: 130, align: 'left'});
    colModel.push({label: 'BASIS',				index: 'NOTEPAD',         		name: 'NOTEPAD',           		width: 10,  hidden:true});
    colModel.push({label: 'ADD_CONTENT',		index: 'ADD_CONTENT',     		name: 'ADD_CONTENT',     		width: 10,  hidden:true});
    colModel.push({label: 'OK_USER_NO',			index: 'OK_USER_NO',      		name: 'OK_USER_NO',      		width: 10,  hidden:true});
    colModel.push({label: 'LEVEL',				index: 'LEVEL',           		name: 'LEVEL',           		width: 100, align: 'left', hidden:true});
    colModel.push({label: 'TARGET_ID',			index: 'TARGET_ID',       		name: 'TARGET_ID',       		width: 100, align: 'left', hidden:true});
    colModel.push({label: 'AP_NO',				index: 'AP_NO',       	  		name: 'AP_NO',  	       		width: 100, align: 'left', hidden:true});
    colModel.push({label: '결재 구분',				index: 'APPROVAL_FLAG',   		name: 'APPROVAL_FLAG',   		width: 100, align: 'left', hidden:true});
    colModel.push({label: '결재 구분(FLAG)',		index: 'APPROVAL_GROUP_FLAG',   name: 'APPROVAL_GROUP_FLAG',   	width: 100, align: 'left', hidden:true});
    GridSearchTypeChk();
	searchListAppend(); 
	setSelectDate();  
    
	var oPostDt = {};
	
    oPostDt["dayKey"] = $("#searchDay").val(); 
    oPostDt["fromDate"] = $("#fromDate").val();
    oPostDt["toDate"]   = $("#toDate").val(); 
	
    oGrid.jqGrid({

    	url: "${getContextPath}/approval/searchProcessList", 
        postData: oPostDt,
        datatype: "json",
        mtype: "POST",
        async: true,
        contentType: "application/json; charset=UTF-8",
        colModel: colModel,
        loadonce: true,
        viewrecords: true, // show the current page, data rang and total records on the toolbar
        width: oGrid.parent().width(),
        height: 555,
        multiselect: true,
        shrinkToFit: true, 
        rownumbers: false,              // 행번호 표시여부
        rownumWidth: 75,                // 행번호 열의 너비    
        rowNum: 25,
        rowList: [25,50,100,500],       
        pager: "#Pages",
        onCellSelect: function(nRowid, icol, cellcontent, e) {
			
        	if(icol != 0){
        		//팝업 호출 전 data clear
            	$("#excepPath tr[name=excepPathAddTr]").remove();
            	$("#reason").val("");
            	
                // 테이블에서 path_ex_group_name 가져와서 넣어줘야함
                var oPostDt = {};
                oPostDt["data_processing_group_idx"] = oGrid.getCell(nRowid, 'IDX');
                var tid = oGrid.getCell(nRowid, 'TARGET_ID');
                
                var url ="";
                if(oGrid.getCell(nRowid, 'APPROVAL_FLAG') == 1) { // 정/오탐
                	url = "${getContextPath}/approval/selectProcessPath";
                }else{ // 경로 예외
                	url = "${getContextPath}/detection/selectProcessPath";
                }
                
                $.ajax({
                    type: "POST",
                    url: url,
                    async: true,
                    data: JSON.stringify(oPostDt),
                    contentType: 'application/json; charset=UTF-8',
                    success: function (searchList) {
                        var arr = [];
                        var getPathex = [];
                        var exPathex = "";
                        var beforeID = null;
                        console.log(searchList)
                        if (searchList.length > 0) {
                            $.each(searchList, function (i, s) {
                            	var excepath = "";
                            	
                            	if(oGrid.getCell(nRowid, 'APPROVAL_FLAG') == 1) { // 정/오탐
                            		arr.push(s);
                                    getPathex.push(arr[i].PATH);
                                    var reason = arr[0].FLAG
                                	if(beforeID == null || s.TARGET_ID != beforeID){ 
                                		excepath += "===================================================================================================== <br>" 
                                		excepath += "호스트 : "+s.NAME ;
                                		if(s.CONNECTED_IP != null){
                                			excepath += "(" + s.CONNECTED_IP +")";
                                		}
                                		excepath += "<br>";
                                	}
                                    
                                    beforeID = s.TARGET_ID 
                                    var tid = s.TARGET_ID;
                                    $("#excepPath").append(
                                    		"<tr name='excepPathAddTr' style='border:none;'>"+
                                    			"<th style='padding:0px; background: transparent; text-align: left;'>"+excepath +
                                    				"경로 : <a style=\"color: blue; cursor: pointer;\" onclick=\"showDetail('"+tid+"', "+s.FID+", "+s.hash_id+","+s.AP_NO+","+nRowid+");\">"+getPathex[i]+"</a>"+
                                    			"</th>" +  
                                   			"</tr>"
                                   	);
                                    $("#reason").val(reason);
                                     
                                    
                            	}else{
                            		var firstExcepathId = searchList[0].TARGET_ID + "_" + searchList[0].AP_NO;
                            		arr.push(s);
                            		
                            		var ExcepathId = s.TARGET_ID + "_"+s.AP_NO
                            		if(firstExcepathId == ExcepathId) exPathex += ("<br> &nbsp; - " +  arr[i].hash_id);
                            		
                                    getPathex.push(arr[i].hash_id);
                                    var reason = arr[0].FLAG
                                    var BasisName = arr[0].NOTEPAD
                                    
                                    if(beforeID == null || s.TARGET_ID != beforeID){ 
                                		excepath += "호스트 : "+s.NAME ;
                                		if(s.CONNECTED_IP != null){
                                			excepath += "(" + s.CONNECTED_IP +")";
                                		}
                                		excepath += "<br>";
                                	}

                                    if((i+1)==searchList.length){
                                    	 excepath += "===================================================================================================== <br>" 
                                    	 $("#excepPath").append(
 	                                    		"<tr name='excepPathAddTr' style='border:none;'>"+
 	                                    			"<th style='padding:0px; background: transparent; text-align: left;'>"+excepath+" 예외 경로 " + exPathex +"</th>" +  
 	                                   			"</tr>"
 	                                   	);
                                    }else{ 
	                                    $("#excepPath").append(
	                                    		"<tr name='excepPathAddTr' style='border:none;'>"+
	                                    			"<th style='padding:0px; background: transparent; text-align: left;'>" + excepath +"</th>" +  
	                                   			"</tr>"
	                                   	);
                                    }
                                    
                                    beforeID = s.TARGET_ID
                                    $("#reason").val(reason);
                                    $("#BasisName").html(BasisName);
                            	}
                            });
                            
                            
                        }
                        return;
                    },
                    error: function (request, status, error) {
                        alert("실패 하였습니다.");
                    }
               });

                var detailName = oGrid.getCell(nRowid, 'FILENAME');
                var serverName = oGrid.getCell(nRowid, 'NAME');  
                var BasisName  = oGrid.getCell(nRowid, 'NOTEPAD');

                $("#BasisName").html(BasisName);
                $("#groupName").html(detailName);
                $("#regisServer").val(serverName);
                $("#insertPathExcepPopup").show();
        	}
        	
        },
        beforeSelectRow: function(nRowid, e) {
        	if (e.target.type !== "checkbox") {
        		return false;
        	} 
        },loadComplete: function(data) {
			automaticCompletion(9);
		}
    });
}

//결재 요청 전 항목 삭제
function delItem() {
	
	var aRowIdx = []; // 삭제할 항목의 idx를 저장하는 배열
	var aRowStatus = []; // 삭제할 항목의 status를 저장하는 배열
	var bProcessChk = false;
    var aSelRows = $("#processGrid").getGridParam('selarrrow');      //체크된 row id들을 배열로 반환

    for (var i = 0; i < aSelRows.length; i += 1)
    {
    	aRowIdx.push($("#processGrid").getCell(aSelRows[i], 'IDX'));
        aRowStatus.push($("#processGrid").getCell(aSelRows[i], 'APPROVAL_STATUS'));
        
        var status = $("#processGrid").getCell(aSelRows[i], 'APPROVAL_STATUS');

        // 결재요청이 이루어진 경우 변경불가
        if (!isNull(status)) {
        	bProcessChk = true;
        }
        
    }
    
 	// 이미 결재 요청된 항목 있으면 삭제 불가 알림
    if (bProcessChk) {
        alert("이미 요청된 항목은 삭제할 수 없습니다.");
        return;
    }

    // 체크된 항목 없으면 알림
    if (aRowIdx.length == 0){
    	alert("삭제할 항목을 선택하세요");
    	return;
    }
	
	var delChk = confirm("선택한 항목을 삭제 하시겠습니까?");
	if(delChk) {
		
		// IDX 가져와서 pi_data_proccesing, pi_data_proccesing_group 각각 삭제 
		var oPostDt = {};
		oPostDt["idxList"]  = aRowIdx;
		$.ajax({
		    type: "POST",
		    url: "${getContextPath}/approval/deleteItem",
		    async: true,
		    data: JSON.stringify(oPostDt),
		    contentType: 'application/json; charset=UTF-8',
		    success: function (result) {
		
				if (result.resultCode != "0") {
					alert(result.resultMessage + "삭제를 실패 했습니다.");
					return;
				}
		        alert("삭제 처리 되었습니다.");
		
			  var oPostDt = { USER_NO : '${memberInfo.USER_NO}'};
			
			  $("#processGrid").clearGridData();
			  
			  $("#processGrid").setGridParam({
				  url: "${getContextPath}/samsung/searchProcessList",
			      postData: $("#processGrid").getGridParam('postData'), 
			      datatype: "json"
			  }).trigger("reloadGrid");
			        return;
		    },
		    error: function (request, status, error) {
		        alert("삭제를 실패 했습니다.");
		    }
		});
	}
}

function approvalAlert(beforeflag){
	 var postData = {approval_type : beforeflag};
	 
	 console.log(postData)
	 $.ajax({
		 type: "POST",
         url: "${getContextPath}/setting/approvalAlert",
         async: true,
         data: postData,
		 dataType: "json",
         success: function (searchList) {
        	 
        	 if(searchList.size > 0){
        		 $("#approvalAlertNm").html(searchList.NAME); // 안내 제목
            	 $("#approvalAlertContent").html(searchList.CON); // 안내 내용
           		 $("#approvalAlert").show();
        	 }else{
        		 $("#ApprovalPopup").show();
        		 loadApprovalUserGrid();
        	 }
         },
         error: function (request, status, error) { 
             alert("실패 하였습니다.");
         }
    });
	 
	
}  

$("#btnApprovalAlert").click(function(){
	
	var content = $("#approvalAlertContent");
	var hasScroll = content[0].scrollHeight > content[0].clientHeight;
	console.log("scrollTop", scrollTop);
	console.log("scrollHeight", content[0].scrollHeight );
	console.log("clientHeight", content[0].clientHeight );
	
	if(hasScroll){  
		var scrollTop = content.scrollTop();
		var scrollVal = content[0].scrollHeight  - content[0].clientHeight - 100;
		
	    if (scrollTop < scrollVal) {
	    	alert("전체 내용을 확인 시 결재 진행이 가능합니다.");
	    	return true;
	    } 
	}
	
	$("#approvalAlert").hide();  
	$("#ApprovalPopup").show();
	loadApprovalUserGrid();
	 
});

// 정탐/오탐 리스트 결재 요청 팝업 호출
function reqApproval()
{
	
    var bDeletion = false;
    var userCheck = false;
    var aDeletion = [];
    var beforeflag = null;
    var aSelRows = $("#processGrid").getGridParam('selarrrow');      //체크된 row id들을 배열로 반환

    for (var i = 0; i < aSelRows.length; i += 1)
    {
        aDeletion.push($("#processGrid").getRowData(aSelRows[i]));
        var status = $("#processGrid").getCell(aSelRows[i], 'APPROVAL_STATUS');
        var user_no = $("#processGrid").getCell(aSelRows[i], 'USER_NO');
        var approval_flag = $("#processGrid").getCell(aSelRows[i], 'APPROVAL_GROUP_FLAG');
        var login_user_no = "${memberInfo.USER_NO}"; 
        
        if(beforeflag != null && beforeflag != approval_flag){
        	 alert("처리구분이 일치하지 않습니다.");
             return;
        }else{
        	beforeflag = approval_flag;  
        }
        
        
		if(user_no != login_user_no){
			userCheck = true;
		}
        
        // 결재요청이 이루어진 경우 변경불가
        if (!isNull(status)) {
        	bDeletion = true;
        }
    }

    if (bDeletion) {
        alert("이미 처리된 항목이 있습니다.");
        return;
    }

    if(userCheck){
    	alert("다른 사용자에 의해 조치된 파일이 포함되어 있습니다.");
    	return;
    }
    
    if (aDeletion.length == 0) {
        alert("결재요청 항목을 선택하세요.");
        return;
    }

    var oToday = getToday();
 
    $("#reg_user_no").val("${memberInfo.USER_NO}");
    $("#reg_user_name").val("${memberInfo.USER_NAME}");
    $("#regdate").val(oToday.substring(0,4) + "-" + oToday.substring(4,6) + "-" + oToday.substring(6,8));
    $("#comment").focus();
    
    approvalAlert(beforeflag);
}

// 정탐/오탐 리스트 결재 요청 내용 저장
function saveApproval()
{
    var aIdxList = [];
    var aExcepScope = [];
    var aSelRow = $("#processGrid").getGridParam('selarrrow');      //체크된 row id들을 배열로 반환

    for (var i = 0; i < aSelRow.length; i += 1)
    {
        aIdxList.push($("#processGrid").getCell(aSelRow[i], 'IDX'));
        aExcepScope.push($("#processGrid").getCell(aSelRow[i], 'OWNER'));
    }

    var sApprType = $('#btnApprovalSave').val();
    var oDate = new Date();
    var sToday = getFormatDate(oDate).replace(/[^0-9]/g, "");
    var sDocuSeq;
    var ApprovalStatus_B = true;

    $.ajax({
        type: "POST",
        url: "${getContextPath}/approval/selectDocuNum",
        async : false,
        data : { "today": sToday },
        datatype: "json",
        success: function (result) {
            sDocuSeq = ""+result.SEQ;
        }
    });
    
	if($("#comment").val().trim() == ""){
    	alert("사유를 적어주세요.");
    	return false;
    }
    
	var rowData = $("#approvalUserGrid").getRowData();
	
	for(i=0; i<rowData.length; i++){
		if(rowData[i].STATUS == "approval_8"){
			ApprovalStatus_B = false;
			break;
		}
	}
	
	if(ApprovalStatus_B){
		alert("결재자를 지정하십시오");
    	return false;
	}
	var oPostDt = {};
    oPostDt["ok_user_no"] = $("#ok_user_no").val();
    oPostDt["doc_seq"]    = sDocuSeq;
    oPostDt["idxList"]    = aIdxList;
    oPostDt["apprType"]   = sApprType;
    oPostDt["comment"]    = $("#comment").val();
    oPostDt["today"]      = sToday.substring(2);
    oPostDt["approvalList"]      = JSON.stringify(rowData);
    oPostDt["approval_flag"]      = $("#processGrid").getCell(aSelRow[0], 'APPROVAL_FLAG');
    
    var confirmCheck = confirm('해당 내용으로 등록하시겠습니까?'); 
	 
	if(confirmCheck == true){
		
		var url = "${getContextPath}/approval/registProcessCharge";
		
		if("${picSession.version.client}" == "samsung"){
			 url = "${getContextPath}/samsung/registProcessCharge";
		}
		
	    $.ajax({
	
	        type: "POST",
	        url: url,
	        async : false,
	        data : JSON.stringify(oPostDt),
	        contentType: 'application/json; charset=UTF-8',
	
	        success: function (result) {
	
	            alert("결재 요청을 등록 하였습니다."); 
	            /* approvalSendMail(); */
	            
	            var oPostDt = { USER_NO : '${memberInfo.USER_NO}'};
	
	            $("#processGrid").clearGridData();
	            $("#processGrid").setGridParam({
	            	url: "${getContextPath}/approval/searchProcessList",
	                postData: $("#processGrid").getGridParam('postData'), 
	                datatype: "json"
	            }).trigger("reloadGrid");
	
	            $("#deletionRegistPopup").hide();
	            $("input:radio[name=trueFalseChk]").prop("checked",false);
	            $("input:radio[name=processing_flag]").prop("checked",false);
	            $("#selecetProcessPopup").val();
	            $("#comment").val("");
	            $("#ok_user_no").val("");
	            $("#ok_user_name").val("");
	            return;
	        },
	        error: function (request, status, error) {
	            alert("결재 요청에 실패 하였습니다.");
	            console.log("ERROR : ", error);
	
	            $("input:radio[name=trueFalseChk]").prop("checked",false);
	            $("input:radio[name=processing_flag]").prop("checked",false);
	            $("#selecetProcessPopup").val();
	            $("#comment").val("");
	            $("#ok_user_no").val("");
	            $("#ok_user_name").val("");
	            return;
	        }
	    });

	    $("#ApprovalPopup").hide();
	    
	} 

}

function approvalSendMail(){
	
	var aIdxList = [];
	var aSelRow = $("#processGrid").getGridParam('selarrrow');      //체크된 row id들을 배열로 반환

    for (var i = 0; i < aSelRow.length; i += 1)
    {
        aIdxList.push($("#processGrid").getCell(aSelRow[i], 'IDX'));
    }
	
	var oPostDt = {};
    oPostDt["ok_user_no"] = $("#ok_user_no").val();
    oPostDt["idxList"]    = aIdxList;
    oPostDt["comment"]    = $("#comment").val().trim().replace(/\n\r?/g,"\n<br>")
//    oPostDt["comment"]    = $("#comment").val();
	
	$.ajax({
        type: "POST",
        url: "${getContextPath}/mail/approvalSendMail",
        async : false,
        data : JSON.stringify(oPostDt),
        contentType: 'application/json; charset=UTF-8',
        success: function (result) {
            if (result.resultCode == 0) {
                alert(result.resultMessage);
            } else if (result.resultCode == -1) {
                alert(result.resultMessage);
            }else{
            	alert(result.resultMessage);
            }
        },
        error: function (request, status, error) {
            alert("결재 요청에 실패 하였습니다.");
            return;
        }
    }); 
	
}


// 담당자 조회
function searchAppUserSelect()
{
	$("#userGrid").jqGrid({
        url: null,
         datatype: "json",
         mtype: "POST",
         ajaxGridOptions: {
             type  : "POST",
             async : true
         },
         colNames:['ID','성명','팀코드','소속', '이메일', '직급'],
 		colModel: [
 			{ index: 'USER_NO', 	name: 'USER_NO', 	width: 80, align: "center"},
 			{ index: 'USER_NAME', 	name: 'USER_NAME', 	width: 80, align: "center"},
 			{ index: 'INSA_CODE', 	name: 'INSA_CODE', 	width: 100, align: "left", hidden:true},
 			{ index: 'TEAM_NAME', 	name: 'TEAM_NAME', 	width: 100, align: "center" },
 			{ index: 'USER_EMAIL', 	name: 'USER_EMAIL', width: 120, align: "left"},
 			{ index: 'JIKGUK', 		name: 'JIKGUK', 	width: 120, align: "left", hidden:true}
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
         onSelectRow : function(nRowid,celname,value,iRow,iCol) {    
         },
         afterEditCell: function(nRowid, cellname, value, iRow, iCol){
         },
         afterSaveCell : function(nRowid,name,val,iRow,ICol){
         },
         afterSaveRow : function(nRowid,name,val,iRow,ICol){
         },
         ondblClickRow: function(nRowid,iRow,iCol) {
         	
         	var rowCnt = $("#approvalUserGrid").jqGrid("getRowData").length;
         	var rowData = $("#approvalUserGrid").getRowData();

         	var sUserNm = $("#userGrid").getCell(nRowid, 'USER_NAME'); 
     		var sJikguk = $("#userGrid").getCell(nRowid, 'JIKGUK'); 
     		var sUserNo = $("#userGrid").getCell(nRowid, 'USER_NO');
     		var sTeamNm = $("#userGrid").getCell(nRowid, 'TEAM_NAME');
     		var sEmail = $("#userGrid").getCell(nRowid, 'USER_EMAIL');
     		var sStatus = $("input[name=apporvalUser]:checked").val();
				var regUserNo = $("#reg_user_no").val();
				
				var checkedradio = $("input:radio[name=apporvalUser]:checked").val();
				
				if(!checkedradio) {
					alert("구분을 지정해주세요.");
					return;
				}
     		
				var status_name = "";
				var status_value = "";
				var retrun_status = false;
				var rowPosition = "last";
				var rowPositionID = null;
	  			
				for(a=0; a < rowCnt ; a++){
					if(sStatus==rowData[a].STATUS){
						rowPositionID = Number(rowData[a].GIDX);
						break;
					}
				}
				
				if(sUserNo == regUserNo){
					retrun_status = true
				}
				
				
				for(a=0; a < rowCnt ; a++){
					if(sUserNo==rowData[a].USER_NO){
						retrun_status = true;
						break;
					}
				}
				
				if(retrun_status){
					alert("이미 선택된 사용자 입니다.");
					return;
				}
				
				var approvalStatusList = '${approvalStatusList}'.split('[{').join('').split('}]').join('');
				approvalStatusList = approvalStatusList.split('}, {');
				
				console.log(approvalStatusList);
				for(var i = 0; approvalStatusList.length > i; i++){ // str 배열만큼 for돌림
					
					var row = approvalStatusList[i].split(', ');
					var STATUS = row[0].split('STATUS=').join(''); //변수 TABLE_NAME은 TB_NAME1, 다음 반복문에서는 TB_NAME2
					var NAME = row[1].split('NAME=').join(''); // 변수 CHK Y, 다음 반복문에서는 N
					
					if(sStatus == STATUS){
						status_name = NAME;
					}
				}
				
				console.log(approvalStatusList)
				if(sStatus=="approval_6"){
	  				rowPosition = 'before';
	  				status_name = "합의자";
                	approval_status = "H";
	  			}else if(sStatus=="approval_7"){
	  				rowPosition = 'before';
	  				status_name = "통보자";
                	approval_status = 'Y';
	  			}else if(sStatus=="approval_8"){
	  				rowPosition = 'first';
	  				status_name = "결재자";
                	approval_status = 'B';
	  			}else{
	  				approval_status = 'Y';
	  			}
				
     		var mydata = [  
     			    {
     			    	NAME: status_name, 
     			    	STATUS: sStatus , 
     			    	STATUS2: approval_status , 
     			    	USER_NAME: sUserNm, 
     			    	USER_NO: sUserNo, 
     			    	JIKGUK: sJikguk,   
     			    	COM:sTeamNm, 
     			    	CERTAINLY:"", 
     			    	IDX:"", 
     			    	IDX2: "",
     			    	GIDX: rowCnt + 1,
     			    	USER_EMAIL: sEmail
     			    }
     			];
     		
     		console.log("sJikguk", sJikguk);
     		console.log("mydata", mydata);  
     		
     		if(rowPosition == "first" ){
       			$("#approvalUserGrid").jqGrid('addRowData', rowCnt + 1, mydata[0], rowPosition);
       			
			}else if(rowPositionID == null || rowPositionID == ""){
				rowPosition = "first";
       			$("#approvalUserGrid").jqGrid('addRowData', rowCnt + 1, mydata[0], rowPosition);
			}else{
       			$("#approvalUserGrid").jqGrid('addRowData', rowCnt + 1, mydata[0], rowPosition, rowPositionID);
			}
     		
             
             $("#searchName").val("");
             $("#searchTeamName").val("");
             
             $("input:radio[name='apporvalUser']:input[value='B']").prop("checked", true);
             $("#userGrid").clearGridData();
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
function fn_rescan()
{
	var aGridListIds = $("#processGrid").getDataIDs();         // grid의 id 값을 배열로 가져옴
	var aGroupList = [];
	var bChecked, oRowDt, sGroupId;

	for (var i = 0; i < aGridListIds.length; i++) 
	{
		// checkbox checked 여부 판단
		bChecked = $("input:checkbox[id='jqg_processGrid_"+aGridListIds[i]+"']").is(":checked");
	    if (bChecked) {
	        oRowDt = $("#processGrid").getRowData(aGridListIds[i]);     // 해당 id의 row 데이터를 가져옴
	        sGroupId = oRowDt.IDX;

	        // 결재 상태가 승인 완료인 경우만 처리 가능
	        if (oRowDt.STATUS != "E") {
	            alert("결재가 완료되지 않은 항목이 있습니다.");
	            return;
	        }
	        if (aGroupList.indexOf(sGroupId) < 0) 
	            aGroupList.push(sGroupId);
	    }
	}

    if (aGroupList.length == 0) {
    	alert("재검출 항목을 선택하세요.");
        return;
    }

    $.ajax({
        type: "POST",
        url: "${getContextPath}/approval/selectScanPolicy",
        async : false,
        data : JSON.stringify(oPostDt),
        contentType: 'application/json; charset=UTF-8',
        success: function (result) {
        	oScan = result[0];
        },
        error: function (request, status, error) {
            alert("재검색 스캔 조회를 실패 하였습니다.");
            return;
        }
    });

    var oPostDt = {};
    oPostDt["groupList"] = aGroupList;

    $.ajax({
        type: "POST",
        url: "${getContextPath}/approval/selectReScanTarget",
        async : false,
        data : JSON.stringify(oPostDt),
        contentType: 'application/json; charset=UTF-8',
        success: function (result) {
        	oTarget = result;
        },
        error: function (request, status, error) {
            alert("Target 정보 조회를 실패 하였습니다.");
            return;
        }
    });

    var now = new Date();
    var hour = now.getHours(); 
    if (hour.length == 1) { 
    	hour = "0" + hour; 
    	}
   	var minute = now.getMinutes(); if (minute.length == 1) { minute = "0" + minute; }
   	var second = now.getSeconds(); if (second.length == 1) { second = "0" + second; }

   	var nowTime = hour+""+minute+second;
    var scheduleData = {};  // Scan Data Mater Json

    // 레이블 넣기
    // 레이블이 중복되면 리콘에서 에러로 나옴. (Response Code : 409)
    // 레이블명을 바꾸는데 아래처럼 뒤에 날자를 붙이는 방법이 있음.
    // scheduleData.label = oScan.SCHEDULE_LABEL + "-"  + getDateTime();
    
	if (oScan == null) {
		alert("재검출 정책이 설정 되어 있지 않습니다.\n관리자에게 문의 하십시오")
	} else {
		scheduleData.label = oScan.SCHEDULE_LABEL;
		scheduleData.label = oScan.SCHEDULE_LABEL + "_" + today + "_" + "${memberInfo.USER_NO}" + "_" + nowTime
   
    var sTargetId = "";
    var aTarget = [];
    var nRow;
    for (var i = 0; i < oTarget.length; i += 1) {
    	if (sTargetId != oTarget[i].TARGET_ID) {
	    	nRow = aTarget.push({id: oTarget[i].TARGET_ID, locations: []});
	    	sTargetId = oTarget[i].TARGET_ID;
	    }

    	aTarget[nRow - 1].locations.push({id: oTarget[i].LOCATION_ID, subpath: oTarget[i].PATH});
    }

	// target 넣기
    scheduleData.targets = aTarget;

    // profile(Datatype) 넣기
    var profileArr = (oScan.DATATYPE_ID+"").split(",");
    scheduleData.profiles = profileArr;

    // 실행 주기 넣기 - 시작시간
    var startDate = "";
    var thisDateTime = getDateTime(null, "mi", 5);
    startDate = thisDateTime.substring(0,4) + "-"
            + thisDateTime.substring(4,6) + "-" 
            + thisDateTime.substring(6,8) + " " 
            + thisDateTime.substring(8,10) + ":" 
            + thisDateTime.substring(10,12); // + ":" + thisDateTime.substring(12,14); 
    scheduleData.start = startDate;

    // 실행 주기 넣기 - 실행주기
    scheduleData.repeat_days = 0;
    scheduleData.repeat_months = 0;

    // CPUs
    scheduleData.cpu = oScan.SCHEDULE_CPU;

    // Throughput
    scheduleData.throughput = +oScan.SCHEDULE_DATA;

    // memory
    scheduleData.memory = +oScan.SCHEDULE_MEMORY;

    // Pause
    var pause = {};
    pause.start = +oScan.SCHEDULE_PAUSE_FROM; 
    pause.end   = +oScan.SCHEDULE_PAUSE_TO;
    pause.days  = +oScan.SCHEDULE_PAUSE_DAYS;
    scheduleData.pause = pause;

    // 스캔 로그
    scheduleData.trace = new Boolean(oScan.SCHEDULE_TRACE);

    scheduleData.timezone = "Default";
    scheduleData.capture = false;
    var postData = {scheduleData : JSON.stringify(scheduleData)};

    var message = "재검출을 요청 하시겠습니까?";
    if (confirm(message)) {
        $.ajax({
            type: "POST",
            url: "${getContextPath}/scan/registSchedule",
            async : false,
            data : postData,
            success: function (resultMap) {
                if (resultMap.resultCode == 201) {
                    alert("재검출 요청이 정상 적용 되었습니다.");
                    return;
                }
                if (resultMap.resultCode == 409) {
                    alert("재검출 요청이 실패 되었습니다.\n\n스캔 스케줄명이 중복 되었습니다.");
                    return;
                }
                if (resultMap.resultCode == 422) {
                    alert("재검출 요청이 실패 되었습니다.\n\n스케줄 시작시간을 확인 하십시오.");
                    return;
                }
                alert("재검출 요청이 실패 되었습니다.\n관리자에게 문의 하십시오");
            },
            error: function (request, status, error) {
                alert("Server Error : " + error);
                console.log("ERROR : ", error);
            }
        });
    }
	}
}

function popSearch(){
	
	var postData = {
			team_nm : $("#searchTeamName").val(),
			user_nm : $("#searchName").val()
		};
	
	$("#userGrid").setGridParam({
		url:"<%=request.getContextPath()%>/popup/selectUserList", 
		postData : postData, 
		datatype:"json" 
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

function downLoadExcel()
{
	resetFomatter = "downloadClick";
	
	$("#processGrid").jqGrid("exportToCsv",{
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
        fileName : "정탐/오탐_리스트_" + today + ".csv",
        mimetype : "text/csv; charset=utf-8",
        event : resetFomatter,
        returnAsString : false
    });
	
	resetFomatter = null;
	$("#processGrid").jqGrid("showCol",["CHK"]);
}

/* 20201214 추가 */
$("#taskWindowClose").click(function(e){
	$("#taskWindow").hide();
});
$("#pathWindowClose").click(function(e){
	$("#pathWindow").hide();
});

function showDetail(tid, fid, id, ap_no, rowid){
	
	$("#pathWindow").hide();
	$("#taskWindow").hide();
	
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

function getLowPath(id, tid, ap_no){
	
	var pop_url = "${getContextPath}/popup/lowPath";
	var winWidth = 1142;
	var winHeight = 365;
	var popupOption= "width="+winWidth+", height="+winHeight + ", left=0, top=0, scrollbars=no, resizable=no, location=no"; 	
	//var pop = window.open(pop_url,"lowPath",popupOption);
	var pop = window.open(pop_url,id,popupOption);
	/* popList.push(pop);
	sessionUpdate() */;
	
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

var formatColor = function(cellvalue, options, rowObject) {
	var status = cellvalue;
	
	if(resetFomatter == "downloadClick"){
		status = cellvalue;
	}else{
		if(status == "대기"){
			status = '<label style="color:green">'+status+'</label>'
		}else if(status == "반려"){
			status = '<label style="color:red">'+status+'</label>'
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

var hostName = function(cellvalue, options, rowObject) {
	var name = rowObject.NAME;
	var name_cnt = rowObject.NAME_CNT;
	
	if(name_cnt > 0 ){
		name += " 외 " + name_cnt + "건";
	}
	return name;
}

function deleteApprovalUser(idx){
	$("#approvalUserGrid").delRowData(idx);
}

function statusChk(cellvalue, options, rowObject) {
	console.log(rowObject);
	var result = "";
	
	if(rowObject.IDX != '' && rowObject.IDX != null){
		result = "Y";
	}
	return result;
}

function deletBtn(cellvalue, options, rowObject) {
	
	var result = "";
	
	if(rowObject.IDX == '' || rowObject.IDX == null){
		result = "<button type='button' name='button'"+
		"style='border: 0 none; background-color: transparent; width: 43px; cursor : pointer; margin:0;'"
		+" onclick='deleteApprovalUser("+options.rowId+");'>삭제</button>";
	}
	
	  
	return result;
}

function changeData(cellvalue, options, rowObject) {
	return options.rowId;
}

</script>
</body>
</html>