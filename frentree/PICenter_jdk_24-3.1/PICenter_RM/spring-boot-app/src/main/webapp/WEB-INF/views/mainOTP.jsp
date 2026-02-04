<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.security.SecureRandom"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
  	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta charset="utf-8">
	<title>${picenter_data.version.header_txt}</title>
	
	<link rel="icon" href="data:,">
	
	<!-- Publish CSS -->
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/assets/css/reset-PIC.css" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/assets/css/design-PIC.css" />

	<!-- Publish JS -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/jquery-3.3.1.js"></script>

	<!-- Application Common Functions  -->
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/js/common.js"></script>
	
</head>

<style>
	input:-ms-input-placeholder{
		color: #b1b3b5 !important;
		border-bottom: 1px solid #444 !important; 
		border-right: 1px solid #444 !important;
	}
	input[type="number"]::-webkit-outer-spin-button,
	input[type="number"]::-webkit-inner-spin-button {
	    -webkit-appearance: none;
	    margin: 0;
	} 
	.img_logo h1{
		background-image: url('../../resources/assets/images/${picenter_data.version.client}_logo.png');
		background-size:36%; 
		width:378px;
		background-position: 12%;
	} 
	
</style>

<body>
	<!-- wrap -->
	<div class="wrap_login">
		<div class="img_logo" style="margin-top: 1px;">
			<h1>PIC</h1>
		</div>
		<div class="container_login">
			<div class="login_box" style="overflow-y: auto;">
				<div class="text_box">
					<h1 style="font-size: 36px; line-height: 1;">${picenter_data.version.version_nm}</h1>
					<p style="padding-top: 10px; padding-left: 25%; font-size: 13px;">${picenter_data.version.main_description}</p>
					<div class="sub_text_box">
						<p style="font-size: 13px; padding-left: 15px;">개인정보 검출관리센터</p>
						<p style="font-size: 13px; padding-left: 15px;">개인정보보호법 준수를 위해 서버 등의 시스템 내에 존재하는 <br> 불필요한 개인정보를 검출 및 관리하는 시스템 입니다.</p>
					</div>
				</div>
				<div class="input_box" style="padding-left: 39px;">
					<h2 style="text-align: left;">Login<span style="font-size: 14px; color: #ccc;">/로그인</span></h2>
					<p style="padding-top: 100px;">ID</p>
					<input type="text" id="user_id" value="" placeholder="아이디를 입력하세요."><br/>
					<p>PASSWORD</p>
					<input type="password" id="user_pw" value="" style="margin-bottom: 3px;" placeholder="패스워드를 입력하세요.">
					<input type="hidden" id="user_grade" value="">
					<button type="button" id="loginID" class="btn_login" style="margin-top: 40px;" >Login</button>
					<div class="footer_copyright">
						${picenter_data.version.footer_txt}
					</div>
				</div>
			</div>
		</div>


</div>

<!-- 초기화 패스워드 변경 모달 -->
<div id="passwordChangePopup" class="popup_layer" style="display:none">
	<div class="popup_box" style="height: 200px;  width: 400px; padding: 10px; top: 65%; left: 55%; background: #f9f9f9;">
	<img class="CancleImg" id="btnCanclePwdChangePopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 style="color: #222; padding: 0; box-shadow: none;">패스워드 변경</h1>
		</div>
		<div class="popup_content">
			<div class="content-box"  style="height: 155px; background: #fff; border: 1px solid #c8ced3;">
				<!-- <h2>세부사항</h2>  -->
				<table class="popup_tbl">
					<colgroup>
						<col width="30%">
						<col width="*">
					</colgroup>
					<tbody>
						<tr>
							<th>현재패스워드</th>
							<td><input style="width: 238px; padding-left: 10px;" type="password" id="oldPasswd" value="" class="edt_sch"></td>
						</tr>
						<tr>
							<th>변경패스워드</th>
							<td><input style="width: 238px; padding-left: 10px;" type="password" id="newPasswd" value="" class="edt_sch"></td>
						</tr>
						<tr>
							<th>변경패스워드확인</th>
							<td><input style="width: 238px; padding-left: 10px;" type="password" id="newPasswd2" value="" class="edt_sch"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area">
				<button type="button" id="btnResetPasswordChangeSave">저장</button>
				<button type="button" id="btnResetPasswordChangeCancel">취소</button>
			</div>
		</div>
	</div>
</div>
	<!-- wrap -->

<div id="memberOTPInsertPop" class="popup_layer" style="display:none">
	<div class="popup_box" style="height: 200px;  width: 250px; padding: 10px; top: 60%; left: 60%; background: #f9f9f9;">
	<img class="CancleImg" id="memberOTPInsertCancelPop" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 style="color: #222; padding: 0; box-shadow: none;">OTP 인증</h1>
			<input type="hidden" id="OTPSKEY">
		</div>
		<div class="popup_content">
			<div class="content-box"  style="height: 224px; background: #fff; border: 1px solid #c8ced3; padding: 0px;">
				<!-- <h2>세부사항</h2>  -->
				<table class="popup_tbl" style="border: none;">
					<tbody >
						<tr id="OTPURL"></tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area" style="float: left; padding-left: 10px;">
				<p style="display: inline; padding-left:10px;" id="smsTime">00:30</p>
			</div>
			<div class="btn_area">
				<button type="button" id="btnOTPSuesse">확인</button>
				<button type="button" id="btnOTPRequest" style="display: none">재요청</button>
			</div>
		</div>
	</div>
</div>
<div id="memberOTPCheckPop" class="popup_layer" style="display:none">
	<div class="popup_box" style="height: 159px;  width: 400px; padding: 10px; top: 70%; left: 60%; background: #f9f9f9;">
	<img class="CancleImg" id="memberOTPCheckCancelPop" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 style="color: #222; padding: 0; box-shadow: none;">OTP 인증</h1>
		</div>
		<div class="popup_content">
			<div class="content-box"  style="height: 69px; background: #fff; border: 1px solid #c8ced3;">
				<!-- <h2>세부사항</h2>  -->
				<table class="popup_tbl" style="border: none;">
				<colgroup>
						<col width="30%">
						<col width="*">
					</colgroup>
					<tbody>
						<tr>
							<th>인증번호</th>
							<td><input style="width: 238px; padding-left: 10px;" type="number" id="OTPKey" value="" class="edt_sch"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area">
<!-- 				<button type="button" id="btnOTPReTrun">재 등록</button> -->
				<button type="button" id="btnOTPKeySuesse">확인</button>
			</div>
		</div>
	</div>
</div>

<!-- 계정 해제 신청 모달  -->
<div id="lockMemberPopup" class="popup_layer" style="display:none">
	<div class="popup_box" style="height: 145px;  width: 400px; padding: 10px; background: #f9f9f9; top: 72%;">
	<img class="CancleImg" id="btnCancleLockMemberPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 style="color: #222; padding: 0; box-shadow: none;">계정 잠금 안내</h1>
		</div>
		<div class="popup_content">
			<div class="content-box" style="height: 69px; background: #fff; border: 1px solid #c8ced3; font-size: 13px; padding-top: 14px;">
				장기 미사용<span style="color: #0C4DA2; font-weight: bold;">(3개월)</span>하여 자동 잠금처리 되었습니다. <br> 
				계정을 이용하실 경우 신청 버튼을 통해 관리자게에 요청해주세요.
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area">
				<button type="button" id="btnLockMemberRequest">신청</button>
				<button type="button" id="btnLockMemberClose">닫기</button>
			</div>
		</div>
	</div>
</div>

<div id="lockMemberAccountPopup" class="popup_layer" style="display:none">
	<div class="popup_box" style="height: 230px;  width: 400px; padding: 10px; background: #f9f9f9; top: 68%;">
	<img class="CancleImg" id="btnCancleLockMemberAccountPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 style="color: #222; padding: 0; box-shadow: none;">계정 잠금 해제 신청</h1>
		</div>
		<div class="popup_content">
			<div class="content-box" style="height: 156px; background: #fff; border: 1px solid #c8ced3; font-size: 13px; padding-top: 14px;">
				<table class="popup_tbl">
					<colgroup>
						<col width="30%">
						<col width="*">
					</colgroup>
					<tbody>
						<tr>
							<th>아이디</th>
							<td><input type="text" id="lockMemberNo" value="" class="edt_sch" style="width: 300px; padding-left: 10px; background-color: rgba(210, 210, 210, 0.35);" readonly></td>
						</tr>
						<tr>
							<th>사유</th>
							<td>
								<textarea id="lockMemberReson" rows="4" cols="44" style="resize: none; margin-top: 7px; width: 300px;"></textarea>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area">
				<button type="button" id="btnAccountLockMemberRequest">신청</button>
				<button type="button" id="btnAccountLockMemberClose">닫기</button>
			</div>
		</div>
	</div>
</div>

</body>

<script type="text/javascript">

var passwordRules = /(?=.*\d{1,50})(?=.*[~`!@#$%\^&*()-+=]{1,50})(?=.*[a-zA-Z]{1,50}).{1,50}$/;
var ipRules = /^(1|2)?\d?\d([.](1|2)?\d?\d){3}$/;
var intervalID;
var invalidPassword = 0;
var user_grade = 0;
var position = '';
var isRunning = false;

$(document).ready(function () {
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

	$('#OTPKey').keyup(function(e) {
		if (e.keyCode == 13) {
			otpLogin();
	    }
	});
	 
	$('#btnOTPKeySuesse').click(function() {
		otpLogin();
	});
	
	$('#memberOTPCheckCancelPop').click(function() {
		$("#memberOTPCheckPop").hide();
	});
	
	
	$('#btnCancleNotPortalLoginPopup').click(function() {
		if(confirm("창을 닫으시겠습니까?")){
			window.close();
		}
	});
	$('#btnNotPortalLoginClose').click(function() {
		if(confirm("창을 닫으시겠습니까?")){
			window.close();
		}
	});
	
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
		/* if (isNull($("#sms_code").val().trim())) {
			$("#sms_code").focus();
			alert("인증요처인증번호를 입력하세요");
			return;
		} */
		
		var postData = {
			user_no : $("#user_id").val(),
			password : $("#user_pw").val(),
			sms_code : $("#sms_code").val()
		};
		
		$.ajax({
			type: "POST",
			url: "otp/OTPUserCheck",
			async : false,
			data : postData,
		    success: function (resultMap) {
		    	var loginCode = resultMap.resultCode;
		    	/* 
		    		 0 : 정상
		    		-1 : 로그인 정보 없음
		    		-2 : 접근가능 IP X
		    		-3 : 등록되지 않은 IP
		    		-6 : 관리자 아님
		    		-5 : 비밀번호 초기화
		    		-7 : 설정된 패스워드 없음
		    		-8 : 패스워드, IP 없음
		    	*/
		    	console.log(resultMap);
		    	
		    	if(loginCode == 0){
		    			$("#OTPSKEY").val(resultMap.secretKey);
		    		if(resultMap.qrUrl != null){ // OTP 신규 생성 사용자 
		    			var html = "<img id=\"QRURL\" src=\"data:image/png;base64,"+resultMap.qrUrl+"\" style=\"width:100%;\">"; 
		    			$("#OTPURL").html(html);
		    			isRunning = true;
		    			var display = $('#smsTime');
		    	    	var leftSec = 30;
		    	    	startTimer(leftSec, display);
		    			
		    			$("#memberOTPInsertPop").show(); 
		    		}else{ // 로그인 사용자
		    			$("#memberOTPCheckPop").show();
		    			$("#OTPKey").focus(); 
		    			$("#OTPKey").val(""); 
		    		}
		    		
				}else if(loginCode == -1 || loginCode == -3 || loginCode == -6 || loginCode == -2){
					alert(resultMap.resultMessage);
					return;
				}else if(loginCode == -5){
					$("#passwordChangePopup").show();
				}else if(loginCode == -9){
					alert(resultMap.resultMessage);
					setPassword();
			    }else if(loginCode == -10){
					alert(resultMap.resultMessage);
					return;
				}
		    },
		    error: function (request, status, error) {
				alert("ERROR : " + error);
				console.log("ERROR : ", error);
		    }
		});
	});
	
	$("#btnUpdateMemberDataCancel, #btnCancleMemberDataPopup").on("click", function(e) {
		
		var type = $("#updateType").val();
		var type_name = "로그인 정보"
		if(type == "IP"){
			type_name == "접근가능 IP";
		}else if(type == "PWD" ){
			type_name == "비밀번호";
		}else if(type == "ALL" ){
			type_name == "접근가능 IP / 비밀번호";
		}
		if(confirm(type_name + "가 없으면 로그인이 불가합니다.")){
			$("#updateType").val("");
			
			$("#insertPasswd").val("");
			$("#insertPasswdCheck").val("");
			$("#insertAccessIp").val("");
			
			$("#pawData").hide();
			$("#ipData").hide();
			
			$("#updateMemberData").hide();
		}
	});
	
	$("#btnCanclePwdChangePopup").on("click", function(e) {
		alert('패스워드를 변경해야 로그인 가능합니다.');
		removeSession();
		$("#oldPasswd").val("");
      	$("#newPasswd").val("");
      	$("#newPasswd2").val("");
		$("#passwordChangePopup").hide();
		
		var leftSec = 180;
		clearInterval(timer);
    	isRunning = false;
    	$("#sms_code").val("");
	});
	
	$("#btnCancleLockMemberPopup").on("click", function(e) {
		$("#lockMemberPopup").hide();
	});
	
	$("#btnCancleLockMemberAccountPopup").on("click", function(e) {
		$("#lockMemberAccountPopup").hide();
	});
	
	$("#btnResetPasswordChangeCancel").on("click", function(e) {
		alert('패스워드를 변경해야 로그인 가능합니다.');
		removeSession();
		$("#oldPasswd").val("");
      	$("#newPasswd").val("");
      	$("#newPasswd2").val("");
		$("#passwordChangePopup").hide();
		
		var leftSec = 180;
		clearInterval(timer);
    	isRunning = false;
    	$("#sms_code").val("");
	});
	
	$("#btnResetPasswordChangeSave").click(function(){
		var enable = 0;
		var complication = 0;
		var lengthVal =  0 ;
		//로그인 정책 가져오기
		$.ajax({
			type: "POST",
			url: "/user/selectAccountPolicy",
			async : false, 
			data : {}, 
		    success: function (resultMap) {
	    		complication = resultMap.complication;
	    		if(resultMap.length_chk == 1) lengthVal = resultMap.length_val;
	    		console.log(resultMap.length_chk);
	    		console.log(resultMap.length_val);
		    },
		    error: function (request, status, error) { 
	    		alert("로그인 정책을 불러오는데 실패하였습니다.");
		        console.log("ERROR : ", error);
		        return;
		    }
		});
	    
	    var oldPassword = $("#oldPasswd").val();
	    var newPasswd = $("#newPasswd").val();
	    var newPasswd2 =  $("#newPasswd2").val();
	    var user_no =  $("#user_id").val();

	    if (oldPassword == "") {
	        $("#oldPasswd").focus();
	        alert("현재 패스워드를 입력하십시오");
	        return;
	    }
	    
	    if (newPasswd == "") {
	        $("#newPasswd").focus();
	        alert("변경 패스워드를 입력하십시오");
	        return;
	    }
	    
	    if (newPasswd2 == "") {
	        $("#newPasswd2").focus();
	        alert("변경 패스워드확인을 입력하십시오");
	        return;
	    }
	    
	    if (oldPassword == newPasswd) {
	        $("#newPasswd").focus();
	        alert("현재 패스워드와 변경 패스워드를 다르게 입력하십시오.");
	        return;
	    }
	    
	    if (newPasswd != newPasswd2) {
	        $("#newPasswd").focus();
	        alert("변경 패스워드와 패스워드확인이 일치하지 않습니다.");
	        return;
	    }
	    if(enable == 1){
	    	if(newPasswd.length<lengthVal){
				alert("비밀번호는 "+lengthVal+"자 이상 입력하십시오.");
				return;
			}
	    	
		    if(complication == 1){ //패스워드 복잡도
			    if (!passwordRules.test(newPasswd)) {
			        $("#newPasswd").focus();
			        alert("패스워드는 숫자/영문자/특수문자를 1개 이상 입력하십시오.");
			        return;
			    }
		    }
	    }

	    var postData = {oldPassword : oldPassword, newPasswd : newPasswd, user_no : user_no};        
	    $.ajax({
	        type: "POST",
	        url: "/changeResetPwd",
	        async : false,
	        data : postData,
	        success: function (resultMap) {
	            if (resultMap.resultCode != 0) {
	                alert("패스워드 변경실패 : " + resultMap.resultMessage);
	                removeSession();
	                return;
	            }
	            alert("패스워드가 변경되었습니다.");
	              $("#passwordChangePopup").hide();
	              $("#oldPasswd").val("");
	              $("#newPasswd").val("");
	              $("#newPasswd2").val("");
	              successLogin("/picenter_target");
	        },
	        error: function (request, status, error) {
	            alert("패스워드 변경실패 : " + error);
	            console.log("패스워드 변경실패 : ", error);
	            removeSession();
	        }
	    });
	    
	});
	
	$("#btnLockMemberRequest").on("click", function(e) {
		var user_no = $("#user_id").val();
		
		$("#lockMemberNo").val(user_no);
		
		$("#lockMemberPopup").hide();
		$("#lockMemberAccountPopup").show();
	});
	
	 $("#btnAccountLockMemberRequest").on("click", function(e) {
		 
		var unlock_reson = $("#lockMemberReson").val().trim();
		
		if(unlock_reson == ""){
			alert("계정 잠금 해제 사유를 기입하세요.");
			return;
		}
		
		var postData = {
				user_no : $("#user_id").val(),
				lock_staus : 2,
				unlock_reson : unlock_reson
			};
		
		$.ajax({
			type: "POST",
			url: "/lockMemberRequest",
			async : false,
			data : postData,
		    success: function (resultMap) {
		    	if(resultMap.resultCode == -4){
		    		alert(resultMap.resultMessage)
		    	}else{
			        alert("계정 해제 신청이 접수되었습니다.");
			        $("#lockMemberReson").val("");
			        $("#lockMemberAccountPopup").hide();
		    	}
		    },
		    error: function (request, status, error) {
		        console.log("계정 해제 신청 접수 실패: ", error);
		        removeSession();
		    }
		});
		
	}); 
	
	$("#btnLockMemberClose").on("click", function(e) {
		$("#lockMemberPopup").hide();
	});
	
	$("#btnAccountLockMemberClose").on("click", function(e) {
		$("#lockMemberReson").val("");
		$("#lockMemberAccountPopup").hide();
	});

}); //$(document).ready

function setPassword(){
	$("#oldPasswd").val("");
	$("#newPasswd").val("");
	$("#newPasswd2").val("");
	 
	$("#passwordChangePopup").show();
}

$("#btnPasswordChangeCancel").on("click", function(e) {
	alert('최초 로그인 시 기본 설정된 패스워드를 변경해야 로그인 가능합니다.');
	removeSession();
	$("#passwordChangePopup").hide();
});
$("#memberOTPInsertCancelPop").on("click", function(e) {
	alert("완료 버튼을 클릭하셔야 OTP 등록이 완료됩니다.");
	$("#OTPURL").html("");
	$("#memberOTPInsertPop").hide();
});
$("#btnOTPRequest").on("click", function(e) {
	$("#QRURL").show();
	$("#OTPURLDiv").hide();
	
    $("#btnOTPSuesse").show();   
    $("#btnOTPRequest").hide();
	
	var display = $('#smsTime');
	var leftSec = 30;
	startTimer(leftSec, display);
});

$("#btnOTPSuesse").on("click", function(e) {
	if(confirm("OTP 등록을 완료하셨습니까?")){
		$("#OTPURL").html("");
		$("#memberOTPInsertPop").hide();
		
		var postData = {
				user_no : $("#user_id").val(),
				otpskey : $("#OTPSKEY").val()
			};
		
		$.ajax({
			type: "POST",
			url: "otp/UserOTPInsert",
			async : false,
			data : postData,
		    success: function (resultMap) {
		    	
		    	if(resultMap.resultCode == 0){
		    		$("#OTPKey").focus();
			    	$("#memberOTPInsertPop").hide();
			    	$("#memberOTPCheckPop").show();
		    	}else{
		    		alert("OTP 등록에 문제가 발생했습니다.");
		    		return true;
		    	}
		    },
		    error: function (request, status, error) {
				alert("ERROR : " + error);
		        console.log("ERROR : ", error);
		    }
		});
	}
});


var timer = null;
var isRunning = false;

function successLogin(redirectUrl){
	var user_no = $("#user_id").val();
    if ($('input:checkbox[name="save"]').is(":checked")) {
        setCookie("PiBoardUserNo", user_no, 7); // 7일 동안 쿠키 보관
    }
	else {
    	deleteCookie("PiBoardUserNo"); // 7일 동안 쿠키 보관
	}

    document.location.href = "<%=request.getContextPath()%>" + redirectUrl;
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
};
function otpLogin (){
	if ($('#OTPKey').val() == "") {
	    alert("인증번호를 입력해주세요.");
    	$("#OTPKey").focus();
    	return;
	} else{
		
		var postData = {
			user_no : $("#user_id").val(),
			password : $("#user_pw").val(),
			optKey : $("#OTPKey").val()
		};
		
		 $.ajax({
		        type: "POST",
		        url: "otp/memberLogin",
		        async : false,
		        data : postData,
		        success: function (resultMap) {
		        	if (resultMap.resultCode == 1) {
		        		alert("올바른 인증번호를 입력해주세요.");
		        		return;
		        	}else if (resultMap.resultCode == 0) {
		        		successLogin(resultMap.redirectUrl);
		        	}else{
		        		 alert("OTP 인증에 오류가 발생했습니다.");
		        	}
		        },
		        error: function (request, status, error) {
		            alert("OTP 인증에 오류가 발생했습니다.");
		            console.log(error)
		        }
		    });
	}
}
function startTimer(count, display) {
	/* alert("인증번호를 발송하였습니다."); */
	
	var minutes, seconds;
    timer = setInterval(function () {
	    minutes = parseInt(count / 60, 10);
	    seconds = parseInt(count % 60, 10);
	
	    minutes = minutes < 10 ? "0" + minutes : minutes;
	    seconds = seconds < 10 ? "0" + seconds : seconds;
	
	    display.html(minutes + ":" + seconds);
	
	    // 타이머 끝
	    if (--count < 0) {
	     clearInterval(timer);
	     $("#QRURL").hide();
	     $("#btnOTPSuesse").hide(); 
	     $("#btnOTPRequest").show();
	     
	     var timeHtml = "<div id=\"OTPURLDiv\" style=\"padding-top:75px; text-align : center;\"><h4>인증 시간이 초과되었습니다.<br>다시 진행해주세요.</h4></div>"
	      
	     if($("#OTPURL").children('div').length ==0) {
	    	 $("#OTPURL").append(timeHtml);
	     }else{
	    	 $("#OTPURLDiv").show();
	     }
	     $("#authSMSPopup").hide();
	     //display.html("시간초과");
	     
	     $('#btnAuth').removeAttr("disabled");
	     $('#loginSMS').removeAttr("disabled");
	     $('#loginSMS').css("background", "#000");
	     $('#loginSMS').css("cursor", "pointer");
	     $('#btnAuth').css("background", "#fff");
	     $('#btnAuth').css("cursor", "pointer");
	     $('#btnAuth').css("color", "#000");
	     
	     $('.btn_chk').attr("disabled","disabled");
	     isRunning = false;
   		}
	}, 1000);
     isRunning = true;
}
</script>
</html>
