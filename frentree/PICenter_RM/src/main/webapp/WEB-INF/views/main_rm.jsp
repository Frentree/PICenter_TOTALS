<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.security.SecureRandom"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
  	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta charset="utf-8">
	<title>${picVersion.header_txt}</title>
	
	<link rel="icon" href="data:,">
	
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/assets/css/reset-sktPIC.css" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/assets/css/design-sktPIC.css" />

	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/jquery-3.7.1.js"></script>

	<!-- Application Common Functions  -->
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/js/common.js"></script>
	 
</head>

<style>
	input:-ms-input-placeholder{
		color: #b1b3b5 !important;
		border-bottom: 1px solid #444 !important; 
		border-right: 1px solid #444 !important;
	}
</style>

<body>
	<!-- wrap -->
	<div class="wrap_login">
		<div class="img_logo" style="margin-top: 1px;">
			<h1 id="main_txt" style="background-size: 44%; background-position: 12%; ">
				${picVersion.version_nm}
			</h1>
		</div>
		<div class="container_login">
			<div class="login_box" style="overflow-y: auto;">
				<div class="text_box">
					<h1 style="font-size: 36px; line-height: 1;">${picVersion.version_nm}</h1>
					<p style="padding-top: 10px; padding-left: 35px; font-size: 13px;">${picVersion.main_description}</p>
				</div>
				<div class="input_box">
					<h2 style="text-align: left;">Login<span style="font-size: 14px; color: #ccc;">/로그인</span></h2>
					<p style="padding-top: 60px;">ID</p>
					<input type="text" id="user_id" value="" placeholder="아이디를 입력하세요."><br/>
					<p>PASSWORD</p>
					<input type="password" id="user_pw" value="" style="margin-bottom: 3px;" placeholder="패스워드를 입력하세요.">
					<input type="hidden" id="user_grade" value="">
					<button type="button" id="loginID" class="btn_login" style="margin-top: 51px;" >Login</button>
					<div class="footer_copyright">
						${picVersion.footer_txt}
					</div>
				</div>
			</div>
			
			<div id="passwordChangePopup" class="popup_layer" style="display:none">
			<div class="popup_box" style="height: 200px;  width: 400px; padding: 10px; background: #f9f9f9; left: 56%;top: 67%;">
				<div class="popup_top" style="background: #f9f9f9;">
					<h1 style="color: #222; padding: 0; box-shadow: none;">최초 비밀번호 변경</h1>
				</div>
				<div class="popup_content"> 
					<div class="content-box" style="height: 155px; background: #fff; border: 1px solid #c8cde3">
						<!-- <h2>세부사항</h2>  -->
						<table class="popup_tbl">
							<colgroup>
								<col width="30%">
								<col width="*">
							</colgroup>
							<tbody>
								<tr>
									<th>현재비밀번호</th>
									<td><input style="padding-left: 5px; width: 238px;" type="password" id="oldPasswd" value="" class="edt_sch"></td>
								</tr>
								<tr>
									<th>변경비밀번호</th>
									<td><input style="padding-left: 5px; width: 238px;" type="password" id="newPasswd" value="" class="edt_sch"></td>
								</tr>
								<tr>
									<th>변경비밀번호확인</th>
									<td><input style="padding-left: 5px; width: 238px;" type="password" id="newPasswd2" value="" class="edt_sch"></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<div class="popup_btn">
					<div class="btn_area" style="padding: 10px 0;">
						<button type="button" id="btnPasswordChangeSave">저장</button>
						<button type="button" id="btnPasswordChangeCancel">취소</button>
					</div>
				</div>
			</div>
		</div>
		</div>
	
</body>

<script type="text/javascript">

var passwordRules = /(?=.*\d{1,50})(?=.*[~`!@#$%\^&*()-+=]{1,50})(?=.*[a-zA-Z]{1,50}).{8,50}$/;
var intervalID;
var invalidPassword = 0;
var user_grade = 0;
var position = '';

$(document).ready(function () {
	/* background: url('${pageContext.request.contextPath}/assets/images/${picVersion.logo_url}') no-repeat center;  */

	// 쿠키에 저장된 아이디 정보
	$('#user_id').val(getCookie("PiBoardUserNo"));
	// 아이디 입력시 발생하는 이벤트
	$('#user_id').keyup(function(e) {
		// 엔터 입력시 id값이 아무것도 없으면 id입력창에 포커스
		if (e.keyCode == 13) {
		    if ($('#user_id').val() == "") {
		    	$("#user_id").focus();
		    	return;
			} // 아이디 입력되어 있으면 패스워드에 포커스
		    $("#user_pw").focus();
	    }
	});
	// 패스워드 입력시 발생하는 이벤트
	$('#user_pw').keyup(function(e) {
		// 엔터 입력시 pw값이 아무것도 없으면 pw입력창에 포커스
		if (e.keyCode == 13) {
		    if ($('#user_pw').val() == "") {
		    	$("#user_pw").focus();
		    	return;
			} // 패스워드 입력되어 있으면 ID로 로그인 버튼 클릭
		    $("#loginID").click();
	    }        
	});
	// 아이디로 로그인 버튼 클릭시 발생하는 이벤트
	$("#loginID").click(function(){
		// 패스워드 오입력 횟수 5회 미만
		if (invalidPassword >= 5) return;
		// id value 비어 있을 경우, 포커스 되며 경고창 나옴
		if (isNull($("#user_id").val().trim())) {
			$("#user_id").focus();
			alert("아이디를 입력하세요");
			return;
		}
		// pw value 비어 있을 경우, 포커스 되며 경고창 나옴
		if (isNull($("#user_pw").val().trim())) {
			$("#user_pw").focus();
			alert("패스워드를 입력하세요");
			return;
		}
		
		// pw value 비어 있을 경우, 포커스 되며 경고창 나옴
		var postData = {
			user_no : $("#user_id").val(),
			password : $("#user_pw").val()
		};
		
		$.ajax({
			type: "POST",
			url: "/login",
			async : false,
			data : postData,
		    success: function (resultMap) {
		    	var ip = resultMap.clientIP;
		    	var lastAccessTime = resultMap.lastAccessTime;
		    	
		    	if (resultMap.resultCode == 400) {
					alert(resultMap.resultMessage);
		        	return;
			    }
		    	
		    	if(resultMap.resultCode == 401) {
		    		alert(resultMap.resultMessage);
		    		setPassword();
		    	}
			    	
		        if(resultMap.resultCode == 200){
		        	user_grade = resultMap.user_grade;
		        	var id = $("#user_id").val();
		        	var password = $("#user_pw").val();
		        	if(id == password){
		        		setPassword();
		        	} else {
		        		successLogin(ip, lastAccessTime);
		        	}
		        }
			    
		    },
		    error: function (request, status, error) {
				alert("ERROR : " + error);
		        console.log("ERROR : ", error);
		    }
		});
	});
	
	$("#pw_reset").on("click", function(e) {
		$("#employeeID").val("");
		$("#name").val("");
		$("#phoneNumber").val("");
		$("#codeNumber").val("");
		$("#authNum").val("");
		$.ajax({
			type: "POST",
			url: "/user/SMSFlag",
			async : false,
			//data : postData,
		    success: function (resultMap) {
		    	 if(resultMap.result == "N"){
		    		alert("SMS인증이 비활성화 되어있습니다.");
		    	}else {
		    		$("#passwordResetPopup").show();
		    	}
		    },
		    error: function (request, status, error) {
				alert("ERROR : " + error);
		        console.log("ERROR : ", error);
		    }
		});
	});
	
}); //$(document).ready

function setPassword(){
	$("#oldPasswd").val("");
	$("#newPasswd").val("");
	$("#newPasswd2").val("");
	 
	$("#passwordChangePopup").show();
}

$("#btnPasswordChangeSave").on("click", function(e) {
	var user_id = $("#user_id").val();
	var oldPassword = $("#oldPasswd").val();
	var newPasswd = $("#newPasswd").val();
	var newPasswd2 =  $("#newPasswd2").val();

	if (oldPassword == "") {
		$("#oldPasswd").focus();
		alert("현재 패스워드를 입력하십시요");
		return;
	}
	
	if (newPasswd == "") {
		$("#newPasswd").focus();
		alert("변경 패스워드를 입력하십시요");
		return;
	}
	
	if (newPasswd2 == "") {
		$("#newPasswd2").focus();
		alert("변경 패스워드확인을 입력하십시요");
		return;
	}
	
	if (oldPassword == newPasswd) {
		$("#newPasswd").focus();
		alert("현재 패스워드와 변경 패스워드를 다르게 입력하십시요.");
		return;
	}
	
	if (newPasswd != newPasswd2) {
		$("#newPasswd").focus();
		alert("변경 패스워드와 패스워드확인이 일치하지 않습니다.");
		return;
	}
	
	if (!passwordRules.test(newPasswd)) {
		$("#newPasswd").focus();
		alert("패스워드는 숫자/영문자/특수문자를 1개 이상, 8자 이상입력하십시요.");
		return;
	}
 
	var postData = {user_id : user_id, oldPassword : oldPassword, newPasswd : newPasswd};		
	$.ajax({
		type: "POST",
		url: "/changeAuthCharacter",
		async : false,
		data : postData,
	    success: function (resultMap) {
	    	var ip = resultMap.clientIP;
	    	var lastAccessTime = resultMap.lastAccessTime;
	    	
	        if (resultMap.resultCode != 0) {
		        alert(resultMap.resultMessage);
		        removeSession();
	        	return;
		    }
			alert("패스워드가 변경되었습니다.");
	      	$("#passwordChangePopup").hide();
	    },
	    error: function (request, status, error) {
			alert("패스워드 변경실패 : " + error);
	        console.log("패스워드 변경실패 : ", error);
	        removeSession();
	    }
	});
});
$("#btnPasswordChangeCancel").on("click", function(e) {
	removeSession();
	$("#passwordChangePopup").hide();
});

var timer = null;
var isRunning = false;

function successLogin(ip, lastAccessTime){
	var user_no = $("#user_id").val();
    if ($('input:checkbox[name="save"]').is(":checked")) {
        setCookie("PiBoardUserNo", user_no, 7); // 7일 동안 쿠키 보관
    }
	else {
    	deleteCookie("PiBoardUserNo"); // 7일 동안 쿠키 보관
	}
    alert("마지막 접속 시간 : " + lastAccessTime + "\n접속 IP : " + ip);
    document.location.href = "<%=request.getContextPath()%>/";
}

function removeSession(){
	var postData = {};
	$.ajax({
		type: "POST",
		url: "/logout",
		async : false,
		data : postData,
	    success: function (resultMap) {
	    },
	    error: function (request, status, error) {
			alert("ERROR : " + error);
	        console.log("ERROR : ", error);
	    }
	});
}
</script>
</html>
