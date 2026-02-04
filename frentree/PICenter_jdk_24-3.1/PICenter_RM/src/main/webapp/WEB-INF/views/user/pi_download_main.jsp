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
			<h3>프로그램 다운로드</h3>
			<div class="content magin_t25">
				<div class="grid_top">
					<div class="searchBox" style="float: left;">
						<table class="user_info narrowTable" id="navGridSearchDiv"  style="width: 310px;">
							<tbody>
								<tr id="searchTextBox">
									<th style="text-align: center; border-radius: 0.25rem;" class="searchName">
										<select id="searchFilter"></select>
									</th>
	             			     	<td id="defaultSearchTextBox">
	                					<input type="text" style="width: 205px; padding-left: 5px;" size="10" id="searchContent"  placeholder="검색어를 입력하세요.">	
	                			 	</td>
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
						<c:if test="${memberInfo.USER_GRADE == 9}">
							<div style="float: right; margin-left: 3px; margin-bottom: 7px;">
								<button type="button" name="button" class="btn_down" id="downloadInsert">등록</button>
							</div>
						</c:if>
						<div style="float: right;">
							<h5 style="width: 24px; height: 24px; margin-top: 2px; background-size : 100%; background-image: url('${pageContext.request.contextPath}/resources/assets/images/pi_list.png');" id="tableShow">&nbsp;</h5>
						</div>
	                </div>
				</div>
				<div class="left_box2" style="height: auto; min-height: 635px; overflow: hidden; width:59vw; margin-top: 10px;">
					<table id="downloadGrid"></table>
					<div id="downloadGridPager"></div>
				</div>
			</div>
		</div>
	</section>
	<div id="downloadPopup" class="popup_layer" style="display:none">
	<div class="popup_box" style="height: 540px; width: 1200px; left: 38%; top: 50%; background: #f9f9f9; padding: 10px;">
	<img class="CancleImg" id="btnCancleDownloadPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 style="color: #222; box-shadow: none; padding: 0;">게시물 등록</h1>
		</div>
		<div class="popup_content">
			<div class="content-box" style="height: 485px; background: #fff; border: 1px solid #c8ced3;">
				<table class="faq_popup_tbl">
					<colgroup>
						<col width="15%">
						<col width="*">
					</colgroup>
					<tbody>
						<tr>
							<th>제목</th>
							<td><input type="text" id="insertDownloadTitle" value="" class="edt_sch" style="width: 1024px; font-size: 12px;" placeholder="제목을 입력하세요."></td>
						</tr>
						<tr>
							<th>첨부파일</th>
							<td>
								<form id="uploadFileForm" method="post" enctype="multipart/form-data">
									<input type="file" id="uploadFile" name="uploadFile" style="width: 1024px; font-size: 12px; display: none;" />
									<button type="button" id="clickFileBtn" style="padding: 5px 5px 5px 0; font-size: 12px;">파일 선택</button>
									<input type="text" id="updateFileNm" style="width: 968px; font-size: 12px;" readonly>
								</form>
							</td>
						</tr>
						<tr>
							<th>내용</th>
							<td>
								<textarea rows="10" id="insertDownloadContent" style="width: 1024px; height: 361px; margin: 10px 0; font-size: 12px; resize: none;" placeholder="내용을 입력하세요."></textarea>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="popup_btn" style="height: 45px;">
		<progress id="progressBar" value="0" max="100" style="width:100%"></progress>
			<div class="btn_area">
				<button type="button" id="btnDownloadSave" style="font-size: 12px;">등록</button>
				<button type="button" id="btnDownloadCancel" style="font-size: 12px;">취소</button>
			</div>
		</div>
	</div>
</div>
	
	<%@ include file="../../include/footer.jsp"%>

<script type="text/javascript"> 
var colModel = [];
GridName = "#downloadGrid";
$(document).ready(function () {

	$(document).click(function(e){
		$("#taskWindow").hide();
	});
	
	$(".navGridSearchBtn").click(function(e){
		url = null;
		navGridSearchBtn(url);
	});
	
	$("#searchContent").keyup(function(e){
		if (e.keyCode == 13) { 
			url = null;
			navGridSearchBtn(url);
		}
	});
	
	var gridWidth = $("#downloadGrid").parent().width();
	var gridHeight = 555;
	
	colModel.push({ label: ' ', 			index : 'NUM', 				name: 'NUM', 				width : 5, 	align : 'center', hidden:true });
	colModel.push({ label: '제목', 			index: 'DOWNLOAD_TITLE', 	name: 'DOWNLOAD_TITLE',		width: 50, 	align: 'left', formatter: htmlTag});
	colModel.push({ label: '내용', 			index: 'DOWNLOAD_CON', 		name: 'DOWNLOAD_CON',		width: 100, align: 'left', formatter: htmlTag});
	colModel.push({ label: '파일 ID', 		index: 'DOWNLOAD_ID', 		name: 'DOWNLOAD_ID',		width: 100, align: 'left',  hidden:true});
	colModel.push({ label: '파일명', 			index: 'REAL_FILE_NAME', 	name: 'REAL_FILE_NAME',		width: 100, align: 'left',  hidden:true});
	colModel.push({ label: '실제 저장파일명', 	index: 'FILE_NAME', 		name: 'FILE_NAME',			width: 100, align: 'left',  hidden:true});
	colModel.push({ label: '작성자', 			index: 'USER_NAME',			name: 'USER_NAME',			width: 10, 	align: 'center'});
	colModel.push({ label: '첨부파일', 		index: 'FILE_CHK', 			name: 'FILE_CHK',			width: 10, 	align: 'center', formatter: createFile});
	colModel.push({ label: '작성일', 			index: 'REGDATE', 			name: 'REGDATE', 			width: 25, 	align: 'center', type:3});
	colModel.push({ label: '게시글 번호', 		index: 'IDX', 				name: 'IDX', 				width: 0,  	hidden:true});
	colModel.push({ label: '작성자 사번', 		index: 'USER_NO', 			name: 'USER_NO', 			width: 0,  	hidden:true});
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
	  		// 팝업
        	$("#pathWindow").hide();
            $("#taskWindow").hide();
            
            e.stopPropagation();
            var id = $(this).getCell(rowid, 'IDX');
            var download_title = $(this).getCell(rowid, 'DOWNLOAD_TITLE');
            var download_con = $(this).getCell(rowid, 'DOWNLOAD_CON');
            var user_no = $(this).getCell(rowid, 'USER_NO');
            
            if (id != "0") {
                var pop_url = "${getContextPath}/popup/downloadDetail";
            	var winWidth = 1260;
            	var winHeight = 760;
            	var popupOption= "width="+winWidth+", height="+winHeight + ", left=0, top=0, scrollbars=no, resizable=no, location=no"; 	
            	//var pop = window.open(pop_url,"detail",popupOption);
            	var pop = window.open(pop_url + "?id=" + id,id,popupOption);
            	/* popList.push(pop);
            	sessionUpdate(); */
            	//pop.check();
            	
            	/* var newForm = document.createElement('form');
            	newForm.method='POST', 'GET';
            	newForm.action=pop_url + "/" + id;
            	newForm.name='newForm';
            	//newForm.target='detail';
            	newForm.target=id;
            	
            	var input_id = document.createElement('input');
            	input_id.setAttribute('type','hidden');
            	input_id.setAttribute('name','id');
            	input_id.setAttribute('value',id);

            	newForm.appendChild(input_id);
            	document.body.appendChild(newForm);
            	newForm.submit();
            	
            	document.body.removeChild(newForm); */
            }
            else {
            	getLowPath(id);
            }
        },
		loadComplete: function(data) {
			automaticCompletion(null);
			$('.ui-jqgrid-hdiv').css('height', '35px');
	    },
	    gridComplete : function() {
	    }
	});

	//조회
	var postData = {};
	$("#downloadGrid").setGridParam({
		url:"<%=request.getContextPath()%>/user/downloadList", 
		postData : postData, 
		datatype:"json" 
		}).trigger("reloadGrid");
	
	// 엔터 입력시 발생하는 이벤트
	$('#title, #titcont, #writer').keyup(function(e) {
		if (e.keyCode == 13) {
		    $("#btnSearch").click();
	    }        
	});
	
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


</script>
</body>

</html>