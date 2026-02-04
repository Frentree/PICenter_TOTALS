
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta charset="utf-8">
<title>공지사항 상세정보</title>
<link rel="icon" href="${pageContext.request.contextPath}/resources/assets/images/favicon.ico" type="image/x-icon">

<link
	href="${pageContext.request.contextPath}/resources/assets/css/ui.fancytree.css"
	rel="stylesheet" type="text/css" />
<link
	href="${pageContext.request.contextPath}/resources/assets/css/font-awesome.min.css"
	rel="stylesheet" type="text/css" />

<!-- Publish JS -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/assets/js/jquery-3.3.1.js"></script>
<script type="text/ecmascript"
	src="${pageContext.request.contextPath}/resources/assets/js/jquery-ui-PIC.js"
	type="text/javascript"></script>

<script type="text/ecmascript"
	src="${pageContext.request.contextPath}/resources/assets/tree/jquery.fancytree.ui-deps.js"></script>
<script type="text/ecmascript"
	src="${pageContext.request.contextPath}/resources/assets/tree/jquery.fancytree.js"></script>

<script type="text/ecmascript"
	src="${pageContext.request.contextPath}/resources/assets/jqgrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript"
	src="${pageContext.request.contextPath}/resources/assets/jqgrid/i18n/grid.locale-kr.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/assets/js/wickedpicker.js"></script>

<link rel="stylesheet" type="text/css" media="screen"
	href="${pageContext.request.contextPath}/resources/assets/css/wickedpicker.min.css" />

<!-- Application Common Functions  -->
<script type="text/ecmascript"
	src="${pageContext.request.contextPath}/resources/assets/js/common.js"></script>

<!-- Publish CSS -->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/assets/css/reset-PIC.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/assets/css/design-PIC.css" />
<%@ include file="../../include/session.jsp"%>
<style>
body{
	width: auto;
}
@media screen and (-ms-high-contrast: active), (-ms-high-contrast: none) {
	body{
		width: auto !important;
	}
}
h3 {
	font-size: 22px;
}
.container{
	margin: 0;
}
#noticePop {
	background-color: #23282c;
	color: white;
	height: 60px;
	line-height: 60px;
	padding: 0 20px;
}
#noticeTitle {
	padding-top: 1px;
	padding-left: 1px;
}
#btnNoticeUpdate, #btnNoticeDelete,
#btnNoticeSave, #btnNoticeCancel,
#btnNoticeTop {
	font-size: 12px;
	border: 1px solid #cdcdcd;
}
#Content{
	width: 997px;
	height: 377px;
	font-size: 12px;
	margin-top: 12px;
	resize: none;
}
#noticeUpdateCon{
	width: 997px;
	height: 377px;
	font-size: 12px;
	margin-top: 12px;
	resize: none;
	display: none;
}
</style>
</head>
<body>
	<div class="container" style="width: 100% !important; height: 100% !important; padding: 0;">
		<h3 style="padding-top: 5px; padding-left: 10px;">공지사항</h3>
		<div id="content magin_t25">
			<div class="notice_popup_content" style="height: 615px;">
				<table class="notice_popup_tbl">
					<colgroup>
						<col width="15%">
						<col width="*">
					</colgroup>
					<tbody>
						<tr id="notice_CHK_tbl" style="display: none;">
							<th>중요</th>
							<td>
								<c:if test="${noticeMap.NOTICE_CHK == 1 }">
									<input type="checkbox" id="notice_CHK" value="1">
								</c:if>
								<c:if test="${noticeMap.NOTICE_CHK == 0 }">
									<input type="checkbox" id="notice_CHK" value="0" checked>
								</c:if>
							</td>
						</tr>
						<tr>
							<th>제목</th>
							<td>
								<p id="noticeTitle">${noticeMap.NOTICE_TITLE}</p>
								<input type="hidden" id="noticeUpdateTitle" value="${noticeMap.NOTICE_TITLE}">
							</td>
						</tr>
						<tr>
							<th>작성자</th>
							<td>${noticeMap.USER_NAME}</td>
						</tr>
						<tr>
							<th>작성일</th>
							<td>${noticeMap.REGDATE}</td>
						</tr>
						<tr>
							<th>첨부파일</th>
							<td>
								<c:if test="${fileChk eq 1}">
									<div id ="selectFile">
										<img style="float: left; padding-top: 5px;" alt="" src="<%=request.getContextPath()%>/resources/assets/images/file.png">
										<form id="downloadFileForm" action="<%=request.getContextPath()%>/download/file" method="post">
											<input id="downloadFile" type="hidden" name="filename" value="${noticeMap.FILE_NAME}">
											<input id="downloadRealFile" type="hidden" name="realfilename" value="${noticeMap.REAL_FILE_NAME}">
											<input type="submit" style="background-color: white; font-size: 12px; border:0px; cursor:pointer;" value="${noticeMap.FILE_NAME}"> 
										</form>
									</div>
								</c:if>
								<form id="uploadFileForm" method="post" enctype="multipart/form-data" style="display: none;">
									<input type="file" id="uploadFile" name="uploadFile" style="width: 997px; padding-left: 10px; display: none;" />
									<button type="button" id="clickFileBtn" style="padding: 5px 5px 5px 0; font-size: 12px;">파일 수정</button>
									<input type="text" id="updateFileNm" style="width: 941px; font-size: 12px;" readonly>
									<input type="hidden" id="updateFileNum" value="${noticeMap.NOTICE_FILE_ID}">
									
								</form>
							</td>
						</tr>
						<tr>
							<th>내용</th>
							<td>
								<textarea id="Content" readonly rows="8">${noticeMap.NOTICE_CON}</textarea>
								<textarea id="noticeUpdateCon" rows="8">${noticeMap.NOTICE_CON}</textarea>
							</td>
						</tr>
					</tbody>
				</table>
					<div class="popup_btn" style="position: relative; right: 15px; float: left;">
						<div id="insertbtn" class="btn_area" style="padding: 43px 0;">
							<button type="button" id="btnNoticePopNotToDay">하루동안 보지 않기</button>
						</div> 
					</div>
					<div class="popup_btn" style="position: relative; left: 15px; float: right;">
						<div id="insertbtn" class="btn_area" style="padding: 43px 0;">
							<button type="button" id="btnNoticePopClose">닫기</button>
						</div> 
					</div>
			</div>
			<progress id="progressBar" value="0" max="100" style="width:98%; display: none; position: absolute; margin-left: 8px; margin-top: 6px;"></progress>
		</div>
	</div>
</body>
<script type="text/javascript">

$("#btnFileDownload").click(function(){
	var postData = {
		filename : $("#downloadFile").val(),
		realfilename : $("#downloadRealFile").val(),
    };
	
	$.ajax({
		type: "POST",
		url: "/download/file",
		async : false,
		data : postData,
	    cache: false,
	    xhrFields: {
	        responseType: "blob",
	    },
	    success: function (resultMap) {
	    	
	    },
	    error: function (request, status, error) {
			alert("ERROR : " + error);
	    }
	}).done(function (blob, status, xhr) {
        // check for a filename
        var fileName = "";
        var disposition = xhr.getResponseHeader("Content-Disposition");

        if (disposition && disposition.indexOf("attachment") !== -1) {
            var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
            var matches = filenameRegex.exec(disposition);

            if (matches != null && matches[1]) {
                fileName = decodeURI(matches[1].replace(/['"]/g, ""));
            }
        }

        // for IE
        if (window.navigator && window.navigator.msSaveOrOpenBlob) {
            window.navigator.msSaveOrOpenBlob(blob, fileName);
        } else {
            var URL = window.URL || window.webkitURL;
            var downloadUrl = URL.createObjectURL(blob);

            if (fileName) {
                var a = document.createElement("a");

                // for safari
                if (a.download === undefined) {
                    window.location.href = downloadUrl;
                } else {
                    a.href = downloadUrl;
                    a.download = fileName;
                    document.body.appendChild(a);
                    a.click();
                }
            } else {
                window.location.href = downloadUrl;
            }
        }
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

function ReplaceHtmlTag(cellvalue) {
	
	var result = cellvalue;
	if(cellvalue != null) {
		result = result.replaceAll("&amp;","&");
		result = result.replaceAll("&lt;", "<");
		result = result.replaceAll("&gt;", ">");
		result = result.replaceAll("&quot;", "\"");
	}
	return result;
}

$("#btnNoticePopClose").click(function(){
	window.close();
});

/* setCookie function */
function setCookie(cname, value, expire) {
   var todayValue = new Date();
   // 오늘 날짜를 변수에 저장

   todayValue.setDate(todayValue.getDate() + expire);
   document.cookie = cname + "=" + encodeURI(value) + "; expires=" + todayValue.toGMTString() + "; path=/;";
}


$("#btnNoticePopNotToDay").click(function(){
	setCookie("notice_popup", "end" , 1);
	window.close();
});


</script>
</html>
