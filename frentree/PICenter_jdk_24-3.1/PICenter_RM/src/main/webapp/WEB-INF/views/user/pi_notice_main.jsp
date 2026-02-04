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
#noticeGrid * *{
	overflow: hidden; 
	white-space: nowrap; 
	text-overflow: ellipsis; 
}
.ui-jqgrid tr.ui-row-ltr td{
	cursor: pointer;
}
#insertNoticeContent::placeholder{
	color: #9E9E9E;
}
@media screen and (-ms-high-contrast: active), (-ms-high-contrast: none) {
	.list_sch{
		top: 41px !important;
	}
}
input[type="radio"] {
  display: none;
}

/* 커스텀 라디오 버튼 디자인 */
.custom-radio .radio-btn {
  height: 15px;
  width: 15px;
  border: 1px solid #555;
  display: inline-block;
  border-radius: 20%;
  margin-right: 10px;
  vertical-align: middle;
  position: relative;
}

/* 선택됐을 때의 스타일 */
.custom-radio input[type="radio"]:checked + .radio-btn::before {
  content: "";
  position: absolute;
  top: 0px;
  left: 0px;
  height: 18px;
  width: 18px;
  background-color: #0075FF;
  border-radius: 20%;
}
.custom-radio input[type="radio"]:checked + .radio-btn::before {
  content: "✔";
  color: white;
  position: absolute;
  top: 41%;
  left: 34%;
  transform: translate(-34%, -50%);
  font-size: 14px;
}

</style>
	<section id="section">
		<div class="container">
			<h3>공지사항</h3>
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
	                					<input type="text" style="width: 205px; padding-left: 5px;" size="10" class="searchContent" id="searchContent"   placeholder="검색어를 입력하세요.">	
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
								<button type="button" name="button" class="btn_down" id="noticeInsert">등록</button>
							</div>
						</c:if>
						<div style="float: right;">
							<h5 style="width: 24px; height: 24px; margin-top: 2px; background-size : 100%; background-image: url('${pageContext.request.contextPath}/resources/assets/images/pi_list.png');" id="tableShow">&nbsp;</h5>
						</div>
	                </div>
					
				</div>
				<div class="left_box2" style="height: auto; min-height: 635px; overflow: hidden; width:59vw; margin-top: 10px;">
					<table id="noticeGrid"></table>
					<div id="noticeGridPager"></div>
				</div>
			</div>
		</div>
	</section>
	<div id="noticePopup" class="popup_layer" style="display:none">
	<div class="popup_box" style="height: 540px; width: 1200px; left: 38%; top: 50%; background: #f9f9f9; padding: 10px;">
	<img class="CancleImg" id="btnCancleNoticePopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 style="color: #222; box-shadow: none; padding: 0;">공지사항 등록</h1>
		</div>
		<div class="popup_content">
			<div class="content-box" style="height: 530px; background: #fff; border: 1px solid #c8ced3;">
				<table class="faq_popup_tbl">
					<colgroup>
						<col width="15%">
						<col width="*">
					</colgroup>
					<tbody>
						<tr>
							<th>중요</th>
							<td>
								<input type="checkbox" id="notice_CHK" value="" >
							</td>
						</tr>
						<tr>
							<th>제목</th>
							<td><input type="text" id="insertNoticeTitle" value="" class="edt_sch" style="width: 1024px; font-size: 12px;" placeholder="제목을 입력하세요."></td>
						</tr>
						<tr>
							<th>첨부파일</th>
							<td>
								<form id="uploadFileForm" method="post" enctype="multipart/form-data">
									<input type="file" id="uploadFile" name="uploadFile" style="width: 990px; padding-left: 10px; display: none;" />
									<button type="button" id="clickFileBtn" style="padding: 5px 5px 5px 0; font-size: 12px;">파일 선택</button>
									<input type="text" id="updateFileNm" style="width: 968px; font-size: 12px;" readonly>
									<!-- <input type="file" id="uploadFile" name="uploadFile" style="width: 1024px; font-size: 12px;" /> -->
								</form>
							</td>
						</tr>
						<tr>
							<th>내용</th>
							<td>
								<textarea rows="10" id="insertNoticeContent" style="width: 1024px; height: 361px; margin: 10px 0; font-size: 12px; resize: none;" placeholder="내용을 입력하세요."></textarea>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="popup_btn" style="height: 45px;">
		<progress id="progressBar" value="0" max="100" style="width:100%"></progress>
			<div class="btn_area">
				<button type="button" id="btnNoticeSave" style="font-size: 12px;">등록</button>
				<button type="button" id="btnNoticeCancel" style="font-size: 12px;">취소</button>
			</div>
		</div>
	</div>
</div>
	
	<%@ include file="../../include/footer.jsp"%>

<script type="text/javascript"> 
var beforeAlertChk = "";
var colModel = [];
GridName = "#noticeGrid";

$(document).ready(function () {
	$("#tableCustomData").css("width","119px");   
	$("#tableCustomData").css("right","5%"); 
 
	$(document).click(function(e){
		$("#taskWindow").hide();
	});
	
	var gridWidth = $("#noticeGrid").parent().width();
	var gridHeight = 555;
	var gradeAlert = true;
	var U_grade = "${memberInfo.USER_GRADE}";
	
	if(U_grade == 9){
		gradeAlert = false;
	} 
	
	colModel.push({ label : " ",  			index: 'NOTICE_ALERT', 		name: 'NOTICE_ALERT',		width: 5, 	align: 'center',  formatter: alertForMat, hidden: gradeAlert, type:0});
	colModel.push({ label : " ",  			index : 'NUM', 				name: 'NUM', 				width : 5, 	align : 'center', hidden:true });
	colModel.push({ label : "제목",  			index: 'NOTICE_TITLE', 		name: 'NOTICE_TITLE',		width: 50, 	align: 'left',  formatter: htmlTag});
	colModel.push({ label : "내용",  			index: 'NOTICE_CON', 		name: 'NOTICE_CON',			width: 100, align: 'left',  formatter: htmlTag});
	colModel.push({ label : "파일ID",  		index: 'NOTICE_FILE_ID', 	name: 'NOTICE_FILE_ID',		width: 100, align: 'left',  hidden:true});
	colModel.push({ label : "파일명",  		index: 'REAL_FILE_NAME', 	name: 'REAL_FILE_NAME',		width: 100, align: 'left',  hidden:true});
	colModel.push({ label : "실제 저장파일명",  	index: 'FILE_NAME', 		name: 'FILE_NAME',			width: 100, align: 'left',  hidden:true});
	colModel.push({ label : "작성자",  		index: 'USER_NAME',			name: 'USER_NAME',			width: 10, 	align: 'center'});
	colModel.push({ label : "첨부파일",  		index: 'FILE_CHK', 			name: 'FILE_CHK',			width: 10, 	align: 'center', formatter: createFile, type:0});
	colModel.push({ label : "작성일",  		index: 'REGDATE', 			name: 'REGDATE', 			width: 25, 	align: 'center', type:"3_0"});
	colModel.push({ label : "게시글 번호",  	index: 'IDX', 				name: 'IDX', 				width: 0,  hidden:true});
	colModel.push({ label : "중요",  			index: 'NOTICE_CHK', 		name: 'NOTICE_CHK', 		width: 0,  hidden:true});
	colModel.push({ label : "작성자 사번",  	index: 'USER_NO', 			name: 'USER_NO', 			width: 0,  hidden:true});
	GridSearchTypeChk();
	searchListAppend(); 
	
	$("#noticeGrid").jqGrid({
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
		pager: "#noticeGridPager",
		cmTemplate: {sortable: false},
		//jqgrid의 특성상 - rowpos의 이벤트는 onSelectCell, beforeEditCell 다 해주어야 함
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	  	
	  	},
	  	
	  	ondblClickRow: function(nRowid, icol, cellcontent, e){
	  		
	  	},
	  	onCellSelect: function(rowid,icol,cellcontent,e) {
	  	
	  		if(icol == 0){
// 	  			changeAlertNotice(rowid);
	  			return;
	  		}
	  		
	  		// 팝업
        	$("#pathWindow").hide();
            $("#taskWindow").hide();
            
            e.stopPropagation();
            var id = $(this).getCell(rowid, 'IDX');
            var notice_title = $(this).getCell(rowid, 'NOTICE_TITLE');
            var notice_con = $(this).getCell(rowid, 'NOTICE_CON');
            var user_no = $(this).getCell(rowid, 'USER_NO');
            
            if (id != "0") {
                var pop_url = "${getContextPath}/popup/noticeDetail";
            	var winWidth = 1160;
            	var winHeight = 790;
            	var popupOption= "width="+winWidth+", height="+winHeight + ", left=0, top=0, scrollbars=no, resizable=no, location=no"; 	
            	//var pop = window.open(pop_url,"detail",popupOption);
            	var pop = window.open(pop_url + "?id=" + id,id,popupOption);
            	/* popList.push(pop);
            	sessionUpdate(); */
            	//pop.check();
            	
            	/* var newForm = document.createElement('form');
            	newForm.method='POST', 'GET';
            	//newForm.action=pop_url + "/" + id;
            	newForm.action=pop_url;
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
			var ids = $("#noticeGrid").getDataIDs();
		    $.each(
		        ids,function(idx, rowId){
		        rowData = $("#noticeGrid").getRowData(rowId);
		        if (rowData.NUM == '') {
		            $("#noticeGrid").setRowData(rowId, false, { background:"#F9F9F9" });
		        }
		    }
		    );         
			$('.ui-jqgrid-hdiv').css('height', '35px');
	    },
	    gridComplete : function() {
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

	//조회
	var postData = {};
	$("#noticeGrid").setGridParam({
		url:"<%=request.getContextPath()%>/user/noticeList", 
		postData : postData, 
		datatype:"json" 
		}).trigger("reloadGrid");
	
	// 엔터 입력시 발생하는 이벤트
	$('#title, #titcont, #writer').keyup(function(e) {
		if (e.keyCode == 13) {
		    $("#btnSearch").click();
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
		$("#noticeGrid").setGridParam({
			url:"<%=request.getContextPath()%>/user/noticeSearchList", 
			postData : postData, 
			datatype:"json" 
			}).trigger("reloadGrid");
	})
	
	// 공지사항 등록 팝업
	$("#noticeInsert").click(function(e){
		$("#noticePopup").show();
	})
	
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
$("#btnNoticeSave").click(function(){
	if($("#insertNoticeTitle").val().trim() == "") {
		alert("공지사항 제목을 입력해주세요.");
		return;
	}
	
	if($("#insertNoticeContent").val() == "") {
		alert("공지사항 내용을 입력해주세요.");
		return;
	}
	
	if($("#notice_CHK").is(":checked")){
		$("#notice_CHK").val("0");
	}else{
		$("#notice_CHK").val("1");
	}
	
	var form = $('#uploadFileForm')[0]
    var data = new FormData(form);
	$.ajax({
	        type: "POST",
	        enctype: 'multipart/form-data',
	        url: "/download/upload",
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
	        	$('#btnNoticeSave').prop('disabled', false);
	        	if(result.resultCode != 0){
	        		if(result.resultCode == -2){
	        			alert("부적절한 파일 업로드 시도: " + result.resultMassage);
	        		} else {
	        			alert(result.resultMassage);
	        		}
	        		return;
	        	}
	        	
	        	var postData = {
        			notice_chk : $("#notice_CHK").val(),
	        		notice_title : $("#insertNoticeTitle").val(),
	        		notice_con : $("#insertNoticeContent").val(),
	        		user_no : '${memberInfo.USER_NO}',
	        		file_number : result.fileNumber
	        	};
	        		
	       		$.ajax({
	      			type: "POST",
	       			url: "/user/noticeInsert",
	      			async : false,
	       			data : postData,
	       		    success: function (resultMap) {
	       		        if (resultMap.resultCode != 0) {
	       			        alert("공지사항 등록 실패 : " + resultMap.resultMessage);
	       			    } else if (resultMap.resultCode == 0) {
	       			    	alert("공지사항 등록 성공");
	       			    	$("#noticePopup").hide();
	       			    	
	       			    	var postData = {};
	       			    	$("#noticeGrid").setGridParam({
	       			    		url:"<%=request.getContextPath()%>/user/noticeList", 
	       			    		postData : postData, 
	       			    		datatype:"json" 
	       			    		}).trigger("reloadGrid");
	       			    	
	       			    	$("#insertNoticeContent").val("");
	       			    	$("#insertNoticeTitle").val("");
	       			    	$("#uploadFile").val("");
	       			    }
	       		    },
	       		    error: function (request, status, error) {
	       				alert("ERROR : " + error);
	       		    }
	       		}); 

	            setProgress(0 / 1);
	        	
	        },
	        error: function (e) {
	            $('#btnNoticeSave').prop('disabled', false);
	            alert("파일 업로드 실패하였습니다.");
	            setProgress(0 / 1);
	            return;
	        }
	        
	    });
	<%-- 
	// 등록
	var postData = {
		notice_title : $("#insertNoticeTitle").val(),
		notice_con : $("#insertNoticeContent").val(),
		user_no : '${memberInfo.USER_NO}'
	};

	
	$.ajax({
		type: "POST",
		url: "/user/noticeInsert",
		async : false,
		data : postData,
	    success: function (resultMap) {
	    	console.log(resultMap);
	        if (resultMap.resultCode != 0) {
		        alert("공지사항 등록 실패 : " + resultMap.resultMessage);
		    } else if (resultMap.resultCode == 0) {
		    	alert("공지사항 등록 성공");
		    	$("#noticePopup").hide();
		    	
		    	var postData = {};
		    	$("#noticeGrid").setGridParam({
		    		url:"<%=request.getContextPath()%>/user/noticeList", 
		    		postData : postData, 
		    		datatype:"json" 
		    		}).trigger("reloadGrid");
		    	
		    	$("#insertNoticeContent").val("");
		    	$("#insertNoticeTitle").val("");
		    }
	    },
	    error: function (request, status, error) {
			alert("ERROR : " + error);
	    }
	}); --%>
});

$("#btnNoticeCancel").click(function(){
	$("#noticePopup").hide();
	
	$("#insertNoticeContent").val("");
	$("#insertNoticeTitle").val("");
   	$("#uploadFile").val("");
   	$("#updateFileNm").val("");
});

$("#btnCancleNoticePopup").click(function(){
	$("#noticePopup").hide();
	
	$("#insertNoticeContent").val("");
	$("#insertNoticeTitle").val("");
   	$("#uploadFile").val("");
   	$("#updateFileNm").val("");
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

var alertStatus = true;
function changeAlertNotice(element, rowid) {
	  
  const checkboxes 
      = document.getElementsByName("notic_alert");
  
  checkboxes.forEach((cb) => {
    cb.checked = false;
  })
  
  element.checked = true;
  
  var postData = {
			notice_id : $("#noticeGrid").getCell(rowid, 'IDX'),
			beforeAlertChk : $("#noticeGrid").getCell(beforeAlertChk, 'IDX')
	};
  
  if(beforeAlertChk == rowid){ // 같은 값을 체크(체크 해제)
	  $("input:checkbox[id="+rowid+"]").prop("checked", false);
	  alertStatus = false;
  }
  
  
  $.ajax({
		type: "POST",
			url: "/user/noticeAlert",
		async : false,
			data : postData,
		    success: function (resultMap) {
		    	if(alertStatus){
		    	 beforeAlertChk = rowid;
		    	}else{
		    	 beforeAlertChk = "";
		    	}
		    },
		    error: function (request, status, error) {
				console.log("ERROR : " + error);
		    }
		});
  
  
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
function alertForMat(cellvalue, options, rowObject) {
	var html ="";
	
	if(rowObject.NUM != null){
		if(cellvalue == 1){
			beforeAlertChk = options.rowId;
			html = "<input type=\"checkbox\" name=\"notic_alert\" id="+options.rowId+"  onclick=\"changeAlertNotice(this,"+options.rowId+")\" checked>"
		}else{
			html = "<input type=\"checkbox\" name=\"notic_alert\" id="+options.rowId+"  onclick=\"changeAlertNotice(this, "+options.rowId+")\" >"
		}
		
	}
	
	return html;
	
}


</script>
</body>

</html>