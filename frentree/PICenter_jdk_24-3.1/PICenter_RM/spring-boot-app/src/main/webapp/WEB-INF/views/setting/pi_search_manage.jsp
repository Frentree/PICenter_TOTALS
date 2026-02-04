<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%@ include file="../../include/header.jsp"%>
<style>
.mxWindowPane .user_info td, .user_info th{
	height: 37.5px;
}
.user_info, .user_info_pc, .user_info_server{
	table-layout: fixed;
}
.user_info th {
	width: 18%;
}
.grid_top {
	width: 50%;
	float: left;
	padding: 0 25px 15px 0;
}
.ui-jqgrid tr.ui-row-ltr td{
	cursor: pointer;
}
#datatype_area th, #datatype_area td {
	padding: 0;
}
@media screen and (-ms-high-contrast: active), (-ms-high-contrast: none) {
	.user_info_pc{
		bottom: 414px !important;
	}
	.content_bottom{
		margin-top: 40px !important;
	}
	#policyBox{
		margin-top: 16px !important;
	}
	#scheduleNm, #scheduleSKTNm{
		width: 230px !important;
	}
	.sch_area{
		top: 0px !important;
		left: 5px !important;
	}
	.list_sch{
		right: 1px !important;
		top: -34px !important;
	}
}
</style>
	<!-- section -->
	<section>
		<!-- container -->
		<div class="container">
			<h3 style="display: inline; top: 25px;">조치계획 관리</h3>
			<p class="container_comment" style="position: absolute; top: 32px; left: 183px; font-size: 13px; color: #9E9E9E;">정/오탐 및 예외처리를 생성/수정/삭제하시기 바랍니다.</p>

			<!-- content -->
			<div class="content magin_t35" style="height: 735px;">
			<c:forEach var="set" items="${setMap}">
				<c:if test="${set.NAME == 't_processing' && set.STATUS == 'Y'}">
					<!-- 	            	정탐 -->
					<div class="grid_top" style="float: left;">
						<h3 style="padding: 0; display: inline;">${set.SUB_NAME}</h3>
						<button type="button" name="button" class="btn_down"
							style="margin-bottom: 8px;" onclick="processingGrid('true',1)">생성</button>
						<div class="left_box2" style="overflow: hidden; max-height: 310px; height: 310px;">
							<table id="trueGrid"></table>
							<div id="trueGridPager"></div>
						</div>
					</div>
				</c:if>
				<c:if test="${set.NAME == 'f_processing' && set.STATUS == 'Y'}">
					<!-- 	     오탐        -->
					<div class="grid_top" style="float: left;">
						<h3 style="padding: 0; display: inline;">${set.SUB_NAME}</h3>
						<button type="button" name="button" class="btn_down"
							style="margin-bottom: 8px;" onclick="processingGrid('false',0)">생성</button>
						<div class="left_box2" style="overflow: hidden; max-height: 310px; height: 310px;">
							<table id="falseGrid"></table>
							<div id="falseGridPager"></div>
						</div>
					</div>
				</c:if>
				<c:if test="${set.NAME == 'exception' && set.STATUS == 'Y'}">
					<!-- 	     경로 예외        -->
					<div class="grid_top" style="float: left;">
						<h3 style="padding: 0; display: inline;">${set.SUB_NAME}</h3>
						<button type="button" name="button" class="btn_down"
							style="margin-bottom: 8px;" onclick="processingGrid('exception',2)">생성</button>
						<div class="left_box2" style="overflow: hidden; max-height: 310px; height: 310px;">
							<table id="exceptionGrid"></table>
							<div id="exceptionGridPager"></div>
						</div>
					</div>
				</c:if>
			</c:forEach>
			</div>
		</div>
	</section>
<%@ include file="../../include/footer.jsp"%>

<div id="taskWindow" class="ui-widget-content" style="position:absolute; left: 10px; top: 10px; touch-action: none; width: 150px; z-index: 999; 
	border-top: 2px solid #222222; box-shadow: 2px 2px 5px #ddd; display:none">
	<ul>
		<li  class="status status-completed status-scheduled status-paused status-stopped status-failed">
			<button id="updateBtn">수정 </button></li>
		<li class="status status-completed status-scheduled status-scanning status-paused status-stopped">
			<button id="deleteBtn">삭제</button></li>
	</ul>
	<input type="hidden" id = "gridName" />
</div>


<script type="text/javascript">
var aut = "manager"
var isProcessingGrid = true;
var colModel = [];
$(document).ready(function () {
	fn_drawDetectionGrid("true"); // 정탐
	fn_drawDetectionGrid("false"); // 오탐
	fn_drawExceptionGrid(); // 경로 예외 
	
	$(document).click(function(e){
		$("#taskWindow").hide();
		$("#taskExceptionWindow").hide();
	});
});


function fn_drawDetectionGrid(gridName){
	
	var gird = "#"+gridName + "Grid";
	
	colModel = []
	colModel.push({ label:' ', 				index: 'PROCESSING_FLAG', 		name: 'PROCESSING_FLAG', 		width:0, 	hidden:true});
	colModel.push({ label:'조치계획명', 		index: 'PROCESSING_FLAG_COPY', 	name: 'PROCESSING_FLAG_COPY', 	width:50,	align:'center'});
	colModel.push({ label:'조치계획명', 		index: 'PROCESSING_FLAG_NAME',	name: 'PROCESSING_FLAG_NAME',	width:50,	hidden:true});
	colModel.push({ label:' ', 				index: 'DATE_ENABLE', 			name: 'DATE_ENABLE', 			width:20, 	hidden:true});
	colModel.push({ label:'날짜설정', 			index: 'DATE_ENABLE_CHK', 		name: 'DATE_ENABLE_CHK', 		width:20,	align:'center'});
	colModel.push({ label:' ', 				index: 'COMMENT_ENABLE', 		name: 'COMMENT_ENABLE', 		width:20 , 	hidden:true});
	colModel.push({ label:'비고여부', 			index: 'COMMENT_ENABLE_CHK', 	name: 'COMMENT_ENABLE_CHK', 	width:20 ,	align:'center'});
	colModel.push({ label:' ', 				index: 'COMMENT_TITLE', 		name: 'COMMENT_TITLE', 			width:50, 	hidden:true});
	colModel.push({ label:'비고 입력값', 		index: 'COMMENT_TITLE_COPY', 	name: 'COMMENT_TITLE_COPY', 	width:50});
	colModel.push({ label:' ', 				index: 'COMMENT_TEXT', 			name: 'COMMENT_TEXT', 			width:100, 	hidden:true});
	colModel.push({ label:'비고 입력값 설명',	index: 'COMMENT_TEXT_COPY', 	name: 'COMMENT_TEXT_COPY', 		width:100, 	hidden:false});
	colModel.push({ label:' ', 				index: 'ENABLE', 				name: 'ENABLE', 				width:10, 	hidden:true});
	colModel.push({ label:'작업', 			index: 'BUTTON', 				name: 'BUTTON',					width: 40, 	align:'center', sortable: false});
	
	
	var gridWidth = $(gird).parent().width();
	$(gird).jqGrid({
		datatype: "local",
	   	mtype : "POST",
	   	colModel:colModel,
	   	loadonce:true,
	   	autowidth: false,
		shrinkToFit: false,
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: gridWidth,
		height: 235,
		rownumbers : false, // 행번호 표시여부
		rownumWidth : 35, // 행번호 열의 너비	
		rowNum:25,
		rowList:[25,50,100],
		pager: gird+"Pager",
		onSelectRow : function(rowid,celname,value,iRow,iCol) {
	  	},
	  	afterSaveCell : function(rowid,name,val,iRow,ICol){ // 로우 데이터 변경하고 엔터치거나 다른 셀 클릭했을때 발동
        },
	  	afterSaveRow : function(rowid,name,val,iRow,ICol){ // 로우 데이터 변경하고 엔터치거나 다른 셀 클릭했을때 발동
        },
        loadComplete: function(data) {
        	var ids = $(gird).getDataIDs() ; 
        	$.each(ids, function(idx, rowId) {
				rowData = $(gird).getRowData(rowId) ;
                
                $(gird).setCell(rowId, 'PROCESSING_FLAG_COPY', rowData.PROCESSING_FLAG_NAME);
                
                var html = ""; 
            	
                html = "<input type='checkbox' disabled='disabled' "+((rowData.DATE_ENABLE == 1)?"checked='checked'":"")+">&nbsp;";
            	$(gird).setCell(rowId, 'DATE_ENABLE_CHK', html);
                
            	html = "<input type='checkbox' disabled='disabled' "+((rowData.COMMENT_ENABLE == 1)?"checked='checked'":"")+">&nbsp;";
            	$(gird).setCell(rowId, 'COMMENT_ENABLE_CHK', html);
            	
            	$(gird).setCell(rowId, 'COMMENT_TITLE_COPY', rowData.COMMENT_TITLE);
            	
            	$(gird).setCell(rowId, 'COMMENT_TEXT_COPY', rowData.COMMENT_TEXT);
            	
                $(gird).setCell(rowId, 'BUTTON', "<button type='button' class='"+gridName+"GridBtn' name='gridSubSelTrueBtn'>선택</button>");
                
                
                $("."+gridName+"GridBtn").on("click", function(e) {
    		  		e.stopPropagation();
    				$(gird).setSelection(event.target.parentElement.parentElement.id);
    				var offset = $(this).parent().offset();
    				$("#taskWindow").css("left", (offset.left - $("#taskWindow").width()) + 45 + "px");
    				$("#taskWindow").css("top", offset.top + $(this).height() + "px");

    				var bottomLimit = $(".left_box2").offset().top + $(".left_box2").height();
    				var taskBottom = Number($("#taskWindow").css("top").replace("px","")) + $("#taskWindow").height();

    				if (taskBottom > bottomLimit) { 
    					$("#taskWindow").css("top", Number($("#taskWindow").css("top").replace("px","")) - (taskBottom - bottomLimit) + "px");
    				}
    				$("#taskWindow").show();
    				$("#gridName").val(gridName);
    			});
        	});
        },
	    gridComplete : function() {
	    }
	});
	
	GridReLoad(gird, 1);
	
	
} 

function fn_drawExceptionGrid() {
	
	var gridName = "exception";
	var gird = "#exceptionGrid";
	
	
	var gridWidth = $("#exceptionGrid").parent().width();
	$("#exceptionGrid").jqGrid({
		datatype: "local",
	   	mtype : "POST",
		colNames:[
			'PATH_EX_FLAG','경로예외 사유','경로예외 사유','DATE_ENABLE','작업'
		],
		colModel: [
			{ index: 'PROCESSING_FLAG', 			name: 'PROCESSING_FLAG', 		width:10,	hidden:true},
			{ index: 'PROCESSING_FLAG_COPY', 		name: 'PROCESSING_FLAG_COPY', 	width:320,	align: 'center'},
			{ index: 'PROCESSING_FLAG_NAME', 		name: 'PROCESSING_FLAG_NAME', 	width:320,	hidden:true},
			{ index: 'ENABLE', 						name: 'ENABLE', 				width:0, 	hidden:true},
			{ index: 'BUTTON', 						name: 'BUTTON',					width: 55, 	align: 'center'},
		],
		loadonce:true,
	   	autowidth: true,
		shrinkToFit: true,
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: gridWidth,
		height: 235,
		rownumbers : false, // 행번호 표시여부
		rownumWidth : 35, // 행번호 열의 너비	
		rowNum:25,
		rowList:[25,50,100],
		pager: "#exceptionGridPager",
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {
	  	},
	  	afterSaveCell : function(rowid,name,val,iRow,ICol){ // 로우 데이터 변경하고 엔터치거나 다른 셀 클릭했을때 발동
        },
	  	afterSaveRow : function(rowid,name,val,iRow,ICol){ // 로우 데이터 변경하고 엔터치거나 다른 셀 클릭했을때 발동
        },
		loadComplete: function(data) {
			var ids = $(gird).getDataIDs() ; 
        	$.each(ids, function(idx, rowId) {
				rowData = $(gird).getRowData(rowId) ;
                
                $(gird).setCell(rowId, 'PROCESSING_FLAG_COPY', rowData.PROCESSING_FLAG_NAME);
                $(gird).setCell(rowId, 'BUTTON', "<button type='button' class='"+gridName+"GridBtn' name='gridSubSelTrueBtn'>선택</button>");
                var html = ""; 
                
                $("."+gridName+"GridBtn").on("click", function(e) {
    		  		e.stopPropagation();
    				$(gird).setSelection(event.target.parentElement.parentElement.id);
    				var offset = $(this).parent().offset();
    				$("#taskWindow").css("left", (offset.left - $("#taskWindow").width()) + 45 + "px");
    				$("#taskWindow").css("top", offset.top + $(this).height()  + "px");

    				var bottomLimit = $(".left_box2").offset().top + $(".left_box2").height();
    				var taskBottom = Number($("#taskWindow").css("top").replace("px","")) + $("#taskWindow").height();

    				if (taskBottom > bottomLimit) { 
    					$("#taskWindow").css("top", Number($("#taskWindow").css("top").replace("px",""))  + "px");
    				}
    				$("#taskWindow").show();
    				$("#gridName").val(gridName);
    			});
        	});
	    },
	    gridComplete : function() {
	    }
	});
	
	GridReLoad("#exceptionGrid", 2);
	
}

function processingGrid(gridStatus, type){
	if(!isProcessingGrid){
		alert("진행 중인내역이 있습니다");
		return true;
	}
	
	isProcessingGrid = false;
	
	var rowId = $("#"+gridStatus+"Grid").getGridParam("reccount");
	var rowData = {};
	rowData["PROCESSING_FLAG"] = "";
	rowData["PROCESSING_FLAG_COPY"] = "<input type='text' name='"+gridStatus+"FlagName"+rowId+"' id='"+gridStatus+"FlagName"+rowId+"' value='' style='width: 100%;'>";
	rowData["PROCESSING_FLAG_NAME"] = "";
	
	if(type != 2){ // 경로 예외
		rowData["DATE_ENABLE"] = "";
		rowData["DATE_ENABLE_CHK"] = "<input type='checkbox' id='"+gridStatus+"_date_enable"+rowId+"' name='"+gridStatus+"chk_date_enable"+rowId+"' class='chk_lock' value='date_enable' />";
		rowData["COMMENT_ENABLE"] = "";
		rowData["COMMENT_ENABLE_CHK"] = "<input type='checkbox' id='"+gridStatus+"Comment_enable"+rowId+"' name='"+gridStatus+"chk_comment_enable"+rowId+"' class='chk_lock' value='comment_enable' ";
		rowData["COMMENT_TITLE"] = "";
		rowData["COMMENT_TITLE_COPY"] = "<input type='text' name='"+gridStatus+"CommentTitle"+rowId+"' id='"+gridStatus+"CommentTitle"+rowId+"' value='' style='width: 100%;'>";
		rowData["COMMENT_TEXT"] = "";
		rowData["COMMENT_TEXT_COPY"] = "<input type='text' name='"+gridStatus+"CommentText"+rowId+"' id='"+gridStatus+"CommentText"+rowId+"' value='' style='width: 100%;'>";
	}
	
	rowData["ENABLE"] = "";
	rowData["BUTTON"] = "<button type='button' class='gridSubSelTrueBtn' name='updateProfileBtn' onclick='createProcessingGrid("+rowId+",\"" +gridStatus+"\")'>생성</button>"
    +"<button type='button' class='gridSubSelTrueBtn' name='cancelProfileBtn' onclick='ProcessingGridCancel("+gridStatus+")'>취소</button>"

	
	$("#"+gridStatus+"Grid").addRowData(rowId+1, rowData, 'first');
	
}


function GridReLoad(gridName, type){
	isProcessingGrid = true;
	$(gridName).setGridParam({
		url:"<%=request.getContextPath()%>/setting/getProcessingFlag", 
		postData : {type: type, gridName:gridName}, 
		datatype:"json" }).trigger("reloadGrid");
}

// 수정 화면 표기
$("#updateBtn").click(function(){
	
	if(!isProcessingGrid){
		alert("진행 중인내역이 있습니다");
		return true;
	}  
	
	isProcessingGrid = false;
	
	$("#taskWindow").hide();
	
	var gridName = $("#gridName").val();
	var grid = "#"+$("#gridName").val()+"Grid";
	var row = $(grid).getGridParam( "selrow" );
	var rowData = $(grid).getRowData(row);
	var text = "";
	
	console.log("rowData",rowData);
	console.log("row",row);
	
	

	$(grid).setCell(row, "PROCESSING_FLAG_COPY", "<input type='text' name='"+gridName+"FlagName"+row+"' id='"+gridName+"FlagName"+row+"' value='"+ rowData.PROCESSING_FLAG_NAME +"' style='width: 100%;'>");

	if(gridName != "exception"){ // 정/오탐 일 경우
		text = "<input type='checkbox' id='"+gridName+"_date_enable"+row+"' name='"+gridName+"chk_date_enable"+row+"' class='chk_lock' value='date_enable' ";
		text += ((rowData.DATE_ENABLE == 1)? "checked ='checked'" : "") + "/>";
		$(grid).setCell(row, "DATE_ENABLE_CHK", text);
		
		text = "<input type='checkbox' id='"+gridName+"Comment_enable"+row+"' name='"+gridName+"chk_comment_enable"+row+"' class='chk_lock' value='comment_enable' ";
		text += ((rowData.COMMENT_ENABLE == 1)? "checked ='checked'" : "") + "/>";
		$(grid).setCell(row, "COMMENT_ENABLE_CHK", text);
		
		$(grid).setCell(row, "COMMENT_TITLE_COPY", "<input type='text' name='"+gridName+"CommentTitle"+row+"' id='"+gridName+"CommentTitle"+row+"' value='"+ rowData.COMMENT_TITLE +"' style='width: 100%;'>");
		
		$(grid).setCell(row, "COMMENT_TEXT_COPY", "<input type='text' name='"+gridName+"CommentText"+row+"' id='"+gridName+"CommentText"+row+"' value='"+ rowData.COMMENT_TEXT +"' style='width: 100%;'>");
	}
	
	$(grid).setCell(row, "BUTTON", "<button type='button' class='gridSubSelBtn' name='updateReasonBtn' onclick='updateProcessingGrid(\""+gridName+"\",\""+row+"\")'>수정</button>"
			+"<button type='button' class='gridSubSelBtn' name='cancelReasonBtn' onclick='ProcessingGridCancel(\""+gridName+"\")'>취소</button>");
		
});
// 수정 화면 표기 끝

	
function updateProcessingGrid(gridName, rowId) {
	
	var rowData = $(gridName).getRowData(rowId);
	var gridName = $("#gridName").val();
	var grid = "#"+ $("#gridName").val() +"Grid";
	var row = $(grid).getGridParam( "selrow" );
	
	var rowData = $(grid).getRowData(row);
	var processing_flag = $("#"+ $("#gridName").val() +"Grid").getCell(row,'PROCESSING_FLAG');
	
	var type  = 0;
	switch (gridName) {
	  case "true":  type = 1; break;
	  case "false":  type = 0; break;
	  case "exception":  type = 2; break;
	}
	
	var postData = {
		processing_flag : processing_flag,
		processing_flag_name : $("#"+gridName+"FlagName"+rowId).val(),
		date_enable : $("#"+gridName+"_date_enable"+rowId).is(":checked") ? 1 : 0,
		comment_enable : $("#"+gridName+"Comment_enable"+rowId).is(":checked") ? 1 : 0,
		comment_title : $("#"+gridName+"CommentTitle"+rowId).val(),
		comment_text : $("#"+gridName+"CommentText"+rowId).val(),
		type :type
	};
	
	var message = "아래 내용으로 수정하시겠습니까?";
	
	if (confirm(message)) {
		$.ajax({
			type: "POST",
			url: "/setting/updateProcessingFlag",
			async : false,
			data : postData,
		    success: function (resultMap) {
		        if (resultMap.resultCode == 200) {
		        	isProcessingGrid = true;
		        	alert("수정되었습니다.");
		        	var postData = {}; 
		        	GridReLoad("#"+gridName+"Grid", type);
		        } else {
			        alert("수정에 실패 되었습니다.\n관리자에게 문의 하십시오");
			    }
		    },
		    error: function (request, status, error) {
				alert("Server Error : " + error);
		        console.log("ERROR : ", error);
		    }
		});
	}
}

function createProcessingGrid(rowId, gridName){
	 
	var grid = "#"+gridName + "Grid";
	var rowData = $(grid).getRowData(rowId);
	
	var type  = 0;
	switch (gridName) {
	  case "true":  type = 1; break;
	  case "false":  type = 0; break;
	  case "exception":  type = 2; break;
	}
	
	var processing_flag_name = $("#"+gridName+"FlagName"+rowId).val();
	var date_enable = $("#"+gridName+"_date_enable"+rowId).is(":checked")?1:0;
	var comment_enable = $("#"+gridName+"Comment_enable"+rowId).is(":checked")?1:0;
	var comment_title = $("#"+gridName+"CommentTitle"+rowId).val();
	var comment_text = $("#"+gridName+"CommentText"+rowId).val();
	
	
	if(processing_flag_name =="" || processing_flag_name ==null){
		alert("조치계획명을 작성해주세요."); return;
	}
	else if(comment_enable==1 &&(comment_title =="" || comment_enable ==null)){
		alert("비고 입력값을 작성해주세요."); return;
	}
	else if(comment_enable==1){
		if (comment_title =="" || comment_title ==null){
			alert("비고 입력값을 작성해주세요."); return;
		}
		else if (comment_text =="" || comment_text ==null){
			alert("비고 입력값 설명을 작성해주세요."); return;
		}
	}
	
	var type  = 0;
	switch (gridName) {
	  case "true":  type = 1; break;
	  case "false":  type = 0; break;
	  case "exception":  type = 2; break;
	}
	
	console.log("gridName", gridName);
	console.log("type", type);
	
	var postData = {
			type : type,
			processing_flag_name :processing_flag_name, 
			date_enable : date_enable, 
			comment_enable : comment_enable, 
			comment_title : comment_title,
			comment_text : comment_text
		};
	
	console.log("postData",postData);
	var message = "생성하시겠습니까?";
	
	if (confirm(message)) {
		$.ajax({
			type: "POST",
			url: "/setting/insertProcessingFlag",
			async : false,
			data : postData,
		    success: function (resultMap) {
		        if (resultMap.resultCode == 200) {
		        	isProcessingGrid = true;
		        	alert("생성하였습니다.");
		        	var postData = {};
		        	GridReLoad("#"+gridName+"Grid", type);
			    } else {
			        alert("생성이 실패 되었습니다.\n관리자에게 문의 하십시오");
			    }
		    },
		    error: function (request, status, error) {
				alert("Server Error : " + error);
		        console.log("ERROR : ", error);
		    }
		});
	}
	
}

//삭제하기
$("#deleteBtn").click(function(){
	$("#taskWindow").hide();
	var gridName = $("#gridName").val();
	var grid = "#"+ $("#gridName").val() +"Grid";
	var row = $(grid).getGridParam( "selrow" );
	
	var rowData = $(grid).getRowData(row);
	var processing_flag = $("#"+ $("#gridName").val() +"Grid").getCell(row,'PROCESSING_FLAG');
	
	var type  = 0;
	switch (gridName) {
	  case "true":  type = 1; break;
	  case "false":  type = 0; break;
	  case "exception":  type = 2; break;
	}
	var postData = {
			processing_flag : processing_flag,
			type :type
		};
	
	var message = "삭제하시겠습니까?";
	
	if (confirm(message)) {
		$.ajax({
			type: "POST",
			url: "/setting/deleteProcessingFlag",
			async : false,
			data : postData,
		    success: function (resultMap) {
		        if (resultMap.resultCode == 200) {
		        	alert("삭제하였습니다.");
		        	var postData = {}; 
		        	GridReLoad("#"+gridName+"Grid", type);
		        } else {
			        alert("삭제가 실패 되었습니다.\n관리자에게 문의 하십시오");
			    }
		    },
		    error: function (request, status, error) {
				alert("Server Error : " + error);
		        console.log("ERROR : ", error);
		    }
		});
	}
	
	
});
// 삭제 끝

function ProcessingGridCancel(gridName) {
	
	var type  = 0;
	switch (gridName) {
	  case "true":  type = 1; break;
	  case "false":  type = 0; break;
	  case "exception":  type = 2; break;
	}
	
	GridReLoad("#"+gridName+"Grid", type);
	isProcessingGrid = true;
}

</script>
	<!-- wrap -->
</body>
</html>
