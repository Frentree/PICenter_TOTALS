<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:useBean id="nowDate" class="java.util.Date" />


<%@ include file="../../include/header.jsp"%>
<style>
h4 {
	margin : 5px 0;
	font-size: 0.8vw;
}
#downloadGrid * *{
	overflow: hidden; 
	white-space: nowrap; 
	text-overflow: ellipsis; 
}
.ui-jqgrid tr.ui-row-ltr td{
	cursor: pointer;
}
@media screen and (-ms-high-contrast: active), (-ms-high-contrast: none) {
	.list_sch{
		top: 41px !important;
	}
}
</style>
	<section id="section">  
		<div class="container">
			<h3>다운로드 대기열</h3>
			<div class="content magin_t25">
				<div class="searchBox" style="float: left;">
					<table class="user_info narrowTable" id="navGridSearchDiv"  style="width: 310px;">
						<tbody>
							<tr id="searchTextBox">
								<th style="text-align: center; border-radius: 0.25rem;" class="searchName">
									<select id="searchFilter"></select>
								</th>
             			     	<td id="defaultSearchTextBox">
                					<input type="text" style="width: 205px; padding-left: 5px;" size="10"  class="searchContent" id="searchContent"  placeholder="검색어를 입력하세요.">	
                			 	</td>
<!-- 				                		<td> -->
<!-- 				                    		 <input type="checkbox" name="button" class="navGridNullChk" style="margin-top: 5px;"> -->
<!-- 				                    		 <span style="vertical-align: middle;">Null</span> -->
<!-- 				                    	</td>  -->
		                		<td> 
		                    		 <input type="button" name="button" class="navGridSearchBtn" style="margin-top: 5px;">
		                    	</td>
							</tr> 
						</tbody>
					</table>
				</div>
				<div id="searchFilterBox" class ="searchFilterBox" style="display:inline-block;width:849px;position:absolute;"></div> 
				<div class="list_sch" style="height: 39px; margin-top: 10px; float: right;" >
					<div class="sch_area" >
						<div id="searchConditionsContainer" style="float: left;" ></div>
						<div style="float: right; margin-left: 3px; margin-bottom: 7px;">
						</div>  
						<div style="float: right;">
							<h5 style="width: 24px; height: 24px; margin-top: 2px; background-size : 100%; background-image: url('${pageContext.request.contextPath}/resources/assets/images/pi_list.png');" id="tableShow">&nbsp;</h5>
						</div>
					</div>
				</div> 
				<div class="left_box2" style="height: auto; min-height: 667px; overflow: hidden; width:59vw; margin-top: 10px;">
					<table id="downloadGrid"></table>
					<div id="downloadGridPager"></div>
				</div>
			</div>
		</div>
	</section>
	<%@ include file="../../include/footer.jsp"%>
	
	<div id="DatePopup" class="popup_layer" style="display:none">
	<div class="popup_box" style="height: 200px; width: 840px; top: 55%; left:46%; padding: 10px; background: #f9f9f9;">
	<img class="CancleImg" id="btnCancleDatePopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 style="color: #222; padding: 0; box-shadow: none;">불가 서버</h1>
		</div>
		<div class="popup_content">
			<div class="content-box" id="div_update_user" style="height: 365px; background: #fff; border: 1px solid #c8ced3;">
				<table class="popup_tbl">
					<colgroup>
						<col width="20%">
						<col width="*">
					</colgroup>
					<tbody>
						 <tr>
							<th>구분</th>
							<td>
								<label id="dataStatus"></label>
							</td>
						</tr>
						 <tr>
							<th>관련 내용</th>
							<td>
								<div id="dataCon" style="overflow-y: auto; height: 290px;"></div>
							</td>
						</tr>
						
					</tbody>
				</table>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area" style="padding: 10px 0; margin: 0;">
				<button type="button" id="btnDateCancel">닫기</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript"> 
GridName = "#downloadGrid";
var colModel = [];
requestUrl = "${getContextPath}/download/downloadList" ; 
$(document).ready(function () {
	console.log("pi_file_down");
	var gridWidth = $("#downloadGrid").parent().width();
	var gridHeight = 590; 
	
	
	$("#tableCustomData").css("right","5%");
	$("#tableCustomData").css("top","130px");
	
	colModel.push({label : '1', 			index: 'IDX', 				name: 'IDX',				width: 1, 	align: 'center',	hidden:true, type:1});
	colModel.push({label : '파일명', 			index: 'FILE_NAME', 		name: 'FILE_NAME',			width: 100,	align: 'center',	formatter: htmlTag});
	colModel.push({label : '서버 파일명',		index: 'REAL_FILE_NAME',	name: 'REAL_FILE_NAME',		width: 50, 	align: 'left',		hidden:true});
	colModel.push({label : '위치', 			index: 'PAGE_NAME', 		name: 'PAGE_NAME',			width: 50, 	align: 'center', 	type:1});
	colModel.push({label : '위치_url', 		index: 'URL ',				name: 'URL',				width: 10, 	align: 'center',  	hidden:true});
	colModel.push({label : '진행 상태', 		index: 'PROCESS_DATA', 		name: 'PROCESS_DATA', 		width: 25, 	align: 'center',  	hidden:true});
	colModel.push({label : '상태', 			index: 'FILE_STATUS', 		name: 'FILE_STATUS', 		width: 25, 	align: 'center',	hidden:true});
	colModel.push({label : '상태', 			index: 'STATUS_NAME', 		name: 'STATUS_NAME', 		width: 25, 	align: 'center'});
	colModel.push({label : '시작 일', 			index: 'CREDATE', 			name: 'CREDATE', 			width: 50,  align: 'center', 	type:3, sortable: true});
	colModel.push({label : '만료 일',			index: 'ENDDATE', 			name: 'ENDDATE', 			width: 50,  align: 'center', 	type:9, sortable: true});
	colModel.push({label : '비고',			index: 'NOTE', 				name: 'NOTE', 				width: 1,  align: 'center',		hidden:true});
	colModel.push({label : '비고',			index: 'BTN', 				name: 'BTN', 				width: 20,  align: 'center',	formatter: createModel, search: false, searchrules: { hidden: true }});
	colModel.push({label : ' ',				index: 'DOWN', 				name: 'DOWN', 				width: 20,  align: 'center',	formatter: createView, search: false, searchrules: { hidden: true }, type:0});
	
	GridSearchTypeChk();
	searchListAppend(); 
	
	$("#downloadGrid").jqGrid({
		//url: 'data.json',
		datatype: "local",
	   	mtype : "POST",
	   	ajaxGridOptions : {
			type    : "POST",
			async   : true
		}, 
		colModel: colModel,
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: gridWidth,
		height: gridHeight,
		loadonce: true, // this is just for the demo
	   	autowidth: true,  
		shrinkToFit: true,
		rownumbers : false, // 행번호 표시여부
		rownumWidth : 30, // 행번호 열의 너비	
		rowNum:25,
		rowList:[25,50,100],		
		pager: "#downloadGridPager",
		cmTemplate: {sortable: false},
		//jqgrid의 특성상 - rowpos의 이벤트는 onSelectCell, beforeEditCell 다 해주어야 함
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	  	
	  	},
	  	ondblClickRow: function(nRowid, icol, cellcontent, e){
	  	},
	  	onCellSelect: function(rowid,icol,cellcontent,e) {
	  		
            e.stopPropagation();

            var page_name = $(this).getCell(rowid, 'PAGE_NAME');
	  		var url = $(this).getCell(rowid, 'URL');
	  		var file_name = $(this).getCell(rowid, 'FILE_NAME');
	  		var real_file_name = $(this).getCell(rowid, 'REAL_FILE_NAME');
	  		var note = $(this).getCell(rowid, 'NOTE');
            
			if(icol == 3) {
 	  			location.href=url;
 	  		}

        },
		loadComplete: function(data) {
			automaticCompletion(null);
			
			$(".gridSubSelBtn").on("click", function(e) {
				console.log("page_name", $(this));
			});
			
			var page_name = $("#downloadGrid").find("[aria-describedby='downloadGrid_PAGE_NAME']");
			page_name.css("text-decoration", "underline");
			
			page_name.mouseover(function(e){
				$(this).css("text-decoration", "underline");
				$(this).css("font-weight", "bold");
				$(this).css("cursor", "pointer");
			});
			page_name.mouseleave(function(e){
				$(this).css("text-decoration", "underline");
				$(this).css("font-weight", "normal");
			});
			
			$('.ui-jqgrid-hdiv').css('height', '35px');
	    },
	    gridComplete : function() {
	    }
	});

	//조회
	var postData = {};
	$("#downloadGrid").setGridParam({
		url:"<%=request.getContextPath()%>/download/downloadList", 
		postData : postData, 
		datatype:"json" 
		}).trigger("reloadGrid");
	
	// 엔터 입력시 발생하는 이벤트
	$('#title, #titcont, #writer').keyup(function(e) {
		if (e.keyCode == 13) {
		    $("#btnSearch").click();
	    }        
	});
	
	$("#SCH_D_P_C_G_REGDATE_CHK").change(function() {
    	if($(this).is(":checked") == true) {
    		// 달력 활성
    		$("#fromDate").datepicker('enable');
    		$("#toDate").datepicker('enable');
    		$("#fromDate").css({'background-color':'#FFFFFF'});
    		$("#toDate").css({'background-color':'#FFFFFF'});
    	} else {
    		// 달력 비활성
    		$("#fromDate").datepicker('disable');
    		$("#toDate").datepicker('disable');
    		$("#fromDate").css({'background-color':'#F0F0F0'});
    		$("#toDate").css({'background-color':'#F0F0F0'});
    	}
    });
	
	// 검색 조회
	$("#btnSearch").click(function(e){
		
		if($("#fromDate").val() > $("#toDate").val()){
	        alert("입력한 끝 날짜가 시작 날짜 보다 빠릅니다.");
	        return;
	    }
		
		var postData = {
				title : $("#title").val(),
				titcont : $("#titcont").val(),
				writer : $("#writer").val(),
				fromDate : $("#fromDate").val(),
				toDate : $("#toDate").val(),
				regdateChk : $("#SCH_D_P_C_G_REGDATE_CHK").is(":checked") ? "Y" : "N"
						};
		$("#downloadGrid").setGridParam({
			url:"<%=request.getContextPath()%>/user/downloadSearchList", 
			postData : postData, 
			datatype:"json" 
			}).trigger("reloadGrid");
	})
	
	// 공지사항 등록 팝업
	$("#downloadInsert").click(function(e){
		$("#downloadPopup").show();
	});
	
	$("#clickFileBtn").click(function(){
		$("#uploadFile").click();
	});
	
	$("#uploadFile").change(function(){
		var checkFileNm = $("#uploadFile").val();
		
		var filelength = checkFileNm.lastIndexOf('\\');
		var fileNm = checkFileNm.substring(filelength+1, checkFileNm.length);
		
		$("#updateFileNm").val(fileNm);
	});
	
	setSelectDate();
});
var count = 0;
$("#btnDownloadSave").click(function(){
	if($("#insertDownloadTitle").val().trim() == "") {
		alert("제목을 입력해주세요.");
		return;
	}
	
	if($("#insertDownloadContent").val() == "") {
		alert("내용을 입력해주세요.");
		return;
	}
	
	var form = $('#uploadFileForm')[0]
    var data = new FormData(form);
	$.ajax({
	        type: "POST",
	        enctype: 'multipart/form-data',
	        url: "/download/downloadUpload",
	        data: data,
	        processData: false,
	        contentType: false,
	        //cache: false,
	        //timeout: 600000,
	         xhr: function() { //XMLHttpRequest 재정의 가능
				var xhr = $.ajaxSettings.xhr();
				xhr.upload.onprogress = function(e) { //progress 이벤트 리스너 추가
					var percent = e.loaded * 100 / e.total;
					setProgress(percent);
				};
				return xhr;
			},
	        success: function (result) {
	        	$('#btnDownloadSave').prop('disabled', false);
	        	if(result.resultCode != 0){
	        		if(result.resultCode == -2){
	        			alert("부적절한 파일 업로드 시도: " + result.resultMassage);
	        		} else {
	        			alert(result.resultMassage);
	        		}
	        		return;
	        	}
	        	
	        	var postData = {
	        		download_title : $("#insertDownloadTitle").val(),
	        		download_con : $("#insertDownloadContent").val(),
	        		user_no : '${memberInfo.USER_NO}',
	        		download_number : result.fileNumber
	        	};
	        		
	       		$.ajax({
	      			type: "POST",
	       			url: "/user/downloadInsert",
	      			async : false,
	       			data : postData,
	       		    success: function (resultMap) {
	       		    	console.log(resultMap);
	       		        if (resultMap.resultCode != 0) {
	       			        alert("게시물 등록 실패 : " + resultMap.resultMessage);
	       			    } else if (resultMap.resultCode == 0) {
	       			    	alert("게시물 등록 성공");
	       			    	$("#downloadPopup").hide();
	       			    	
	       			    	var postData = {};
	       			    	$("#downloadGrid").setGridParam({
	       			    		url:"<%=request.getContextPath()%>/user/downloadList", 
	       			    		postData : postData, 
	       			    		datatype:"json" 
	       			    		}).trigger("reloadGrid");
	       			    	
	       			    	$("#insertDownloadContent").val("");
	       			    	$("#insertDownloadTitle").val("");
	       			    	$("#uploadFile").val("");
	       			    	$("#updateFileNm").val("");
	       			    }
	       		    },
	       		    error: function (request, status, error) {
	       				alert("ERROR : " + error);
	       		    }
	       		}); 

	            setProgress(0 / 1);
	        	
	        },
	        error: function (e) {
	            $('#btnDownloadSave').prop('disabled', false);
	            alert("파일 업로드 실패하였습니다.");
	            setProgress(0 / 1);
	            return;
	        }
	        
	    });
});

$("#btnDownloadCancel").click(function(){
	$("#downloadPopup").hide();
	
	$("#insertDownloadContent").val("");
	$("#insertDownloadTitle").val("");
	$("#updateFileNm").val("");
   	$("#uploadFile").val("");
});

$("#btnCancleDownloadPopup").click(function(){
	$("#downloadPopup").hide();
	
	$("#insertDownloadContent").val("");
	$("#updateFileNm").val("");
	$("#insertDownloadTitle").val("");
   	$("#uploadFile").val("");
});

// 날짜
function setSelectDate() 
{
    $("#fromDate").datepicker({
        changeYear : true,
        changeMonth : true,
        dateFormat: 'yy-mm-dd'
    });

    $("#toDate").datepicker({
        changeYear : true,
        changeMonth : true,
        dateFormat: 'yy-mm-dd'
    });

    var oToday = new Date();
    $("#toDate").val(getFormatDate(oToday));

    var oFromDate = new Date(oToday.setDate(oToday.getDate() - 30));
    $("#fromDate").val(getFormatDate(oFromDate));
}
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

var $progressBar = $("#progressBar");
function setProgress(per) {
	$progressBar.val(per);
}


function createFile(cellvalue, options, rowObject) {
	var rowID = options['rowId'];
	var checkboxID = "gridChk" + rowID;
	if (rowObject['FILE_CHK'] == "1"){
		return "<img href='#' src='<%=request.getContextPath()%>/resources/assets/images/file.png' />";
	}
	else { 
		return '';
	}
	
}

function fileStatus(cellvalue, options, rowObject) {
	var status = " ("+rowObject.PROCESS_DATA+")";
	switch (cellvalue) {
	case "completed": 	status =("완료" + status);  break;
	case "error": 		status =("에러" + status);  break;
	case "down": 		status =("진행중" + status);  break;
	case "wait": 		status =("대기" + status);  break;
	default: status = ""; break;
	}
	return status;
}


var createModel = function(cellvalue, options, rowObject) {
	
	if(rowObject.NOTE != null && rowObject.NOTE != ' '){
		return "<button type='button' class='ExcelDrowError' name='ExcelDrowError' onclick='btnDetailPop("+options.rowId+");'>조회</button>"; 
	}else{
		return ""; 
	}
};

function btnDetailPop(rowId){
	var selRowData = $("#downloadGrid").jqGrid("getRowData",rowId);
	var dataCon = "";
	
	var dataConList = selRowData.NOTE;
	 
	$("#dataStatus").html(selRowData.PAGE_NAME);
	$("#dataCon").html(dataConList);
	$("#DatePopup").show();
}; 

var createView = function(cellvalue, options, rowObject) {
	
	var html = "<button type='button' class='gridSubSelBtn' name='gridSubSelBtn'";  
	var status = rowObject.FILE_STATUS;
	
	if(status == "completed"){
		html +="onclick='btnExcelDown("+options.rowId+");' >다운로드</button>";
		
	}else{
		html +="style='background: rgb(228, 228, 228); color:#ADAAAA;' disabled>다운로드</button>";
	}
	
	return html;
};


function btnExcelDown(rowid){
	
	var page_name = $("#downloadGrid").getCell(rowid, 'PAGE_NAME');
	var url = $("#downloadGrid").getCell(rowid, 'URL');
	var file_name = $("#downloadGrid").getCell(rowid, 'FILE_NAME');
	var real_file_name = $("#downloadGrid").getCell(rowid, 'REAL_FILE_NAME');
	var note = $("#downloadGrid").getCell(rowid, 'NOTE');
	
	var filename = file_name;
	var realfilename = real_file_name;
	
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
}


$("#btnDateCancel, #btnCancleDatePopup").click(function() {
	$("#DatePopup").hide();
});

</script>
</body>

</html>