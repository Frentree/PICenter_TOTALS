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
					<p style="padding-top: 130px;">계정명 (Employee ID)</p>
					<input type="text" id="user_id" value="" placeholder="계정명을 입력하세요."><br/>
					<input type="hidden" id="user_grade" value="">
					<button type="button" id="loginID" class="btn_login" style="margin-top: 60px;" >Login</button>
					<div class="footer_copyright">
						${picenter_data.version.footer_txt}
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- OTP 인증 팝업 -->
	<div id="lgOTPCheckPop" class="popup_layer" style="display:none">
		<div class="popup_box" style="height: 159px;  width: 400px; padding: 10px; top: 70%; left: 60%; background: #f9f9f9;">
		<img class="CancleImg" id="lgOTPCheckCancelPop" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
			<div class="popup_top" style="background: #f9f9f9;">
				<h1 style="color: #222; padding: 0; box-shadow: none;">OTP 인증</h1>
			</div>
			<div class="popup_content">
				<div class="content-box"  style="height: 69px; background: #fff; border: 1px solid #c8ced3;">
					<table class="popup_tbl" style="border: none;">
					<colgroup>
							<col width="30%">
							<col width="*">
						</colgroup>
						<tbody>
							<tr>
								<th>OTP 인증번호</th>
								<td><input style="width: 238px; padding-left: 10px;" type="number" id="OTPKey" value="" class="edt_sch" placeholder="6자리 OTP 입력"></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="popup_btn">
				<div class="btn_area">
					<button type="button" id="btnOTPKeySuesse">확인</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 계정 해제 신청 모달  -->
	<div id="lockMemberPopup" class="popup_layer" style="display:none">
		<div class="popup_box" style="height: 145px;  width: 455px; padding: 10px; background: #f9f9f9; top: 72%;">
		<img class="CancleImg" id="btnCancleLockMemberPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
			<div class="popup_top" style="background: #f9f9f9;">
				<h1 style="color: #222; padding: 0; box-shadow: none;">접근 권한 없음</h1>
			</div>
			<div class="popup_content">
				<div class="content-box" style="height: 69px; background: #fff; border: 1px solid #c8ced3; font-size: 13px; padding-top: 14px;">
					해당 시스템에 접속하기 위한 권한이 없습니다.<br>
					정보보안관리자(security@lghnh.com)에게 문의해 주시기 바랍니다.
				</div>
			</div>
			<div class="popup_btn">
				<div class="btn_area">
<!-- 					<button type="button" id="btnLockMemberRequest">신청</button> -->
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

$(document).ready(function () {
	// 보안을 위해 쿠키 사용 안 함 - 매번 사번 입력
	// $('#user_id').val(getCookie("LG_OTP_UserNo"));

	// 사번 입력시 엔터키 이벤트
	$('#user_id').keyup(function(e) {
		if (e.keyCode == 13) {
		    if ($('#user_id').val() == "") {
		    	$("#user_id").focus();
		    	return;
			}
		    $("#loginID").click();
	    }
	});

	// OTP 키 입력시 엔터
	$('#OTPKey').keyup(function(e) {
		if (e.keyCode == 13) {
			otpLogin();
	    }
	});

	// OTP 확인 버튼
	$('#btnOTPKeySuesse').click(function() {
		otpLogin();
	});

	// OTP 팝업 닫기
	$('#lgOTPCheckCancelPop').click(function() {
		$("#lgOTPCheckPop").hide();
		$("#OTPKey").val("");
	});

	// 로그인 버튼 클릭시 (Step 1: 사번 확인)
	$("#loginID").click(function(){
		if ($("#user_id").val().trim()==null) {
			$("#user_id").focus();
			alert("사번을 입력하세요");
			return;
		}

		var postData = {
			user_no : $("#user_id").val()
		};

		$.ajax({
			type: "POST",
			url: "<%=request.getContextPath()%>/lg/otp/checkUserExists",
			async : false,
			data : postData,
		    success: function (resultMap) {
		    	var resultCode = resultMap.resultCode;

		    	console.log(resultMap);

		    	if(resultCode == 100){
		    		successLogin(resultMap.redirectUrl);
		    	} else if(resultCode == 0){
		    		// 사용자 존재 확인 - OTP 팝업 표시
		    		$("#lgOTPCheckPop").show();
		    		$("#OTPKey").focus();
		    		$("#OTPKey").val("");
		    	} else if(resultMap.resultCode == -9){
	        		$("#lockMemberPopup").show();
	        	} else if(resultMap.resultCode == -8){
	        		alert("잠금해제가 완료되지 않은 계정입니다.")
	        	} else {
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

}); //$(document).ready

$("#btnLockMemberClose, btnCancleLockMemberPopup").on("click", function(e) {
	$("#lockMemberPopup").hide();
});

$("#btnAccountLockMemberClose, btnCancleLockMemberAccountPopup").on("click", function(e) {
	$("#lockMemberReson").val("");
	$("#lockMemberAccountPopup").hide();
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
		        $("#lgOTPCheckPop").hide();
				$("#OTPKey").val("");
	    	}
	    },
	    error: function (request, status, error) {
	        console.log("계정 해제 신청 접수 실패: ", error);
	        removeSession();
	    }
	});
	
}); 

// OTP 응답코드별 메시지 반환
function getOTPErrorMessage(resultCode, authFailCnt) {
	var message = "";

	switch(resultCode) {
		case 0:
			message = "OTP 인증 성공";
			break;
        case -2:
            message = "최고관리자 계정의 접근 IP가 등록되어 있지 않습니다.\n시스템 관리자에게 접근 IP 등록을 요청해주세요.";
            break;
    	case -3:
            message = "현재 접속 IP가 최고관리자 허용 IP 목록에 없습니다.\n등록된 IP에서 접속하시거나 관리자에게 문의해주세요.";
            break;
		case 6000:
			message = "OTP 인증에 실패했습니다.\n입력하신 OTP 번호를 확인해주세요.";
			break;
		case 6001:
			message = "이미 사용한 OTP 번호입니다.\n새로운 OTP 번호를 입력해주세요.";
			break;
		case 6002:
			message = "OTP 시간 보정이 필요합니다.\n관리자에게 문의해주세요.";
			break;
		case 6007:
			message = "입력값이 올바르지 않습니다.\n다시 시도해주세요.";
			break;
		case 6010:
			message = "OTP 번호는 6자리 숫자로 입력해주세요.";
			break;
		case 6011:
			message = "장기간 미사용으로 인증이 제한되었습니다.\n관리자에게 문의해주세요.";
			break;
		case 6012:
			message = "계정이 잠겨있습니다.\n관리자에게 문의해주세요.";
			break;
		case 6020:
			message = "인증 요청 처리 중 오류가 발생했습니다.\n잠시 후 다시 시도해주세요.";
			break;
		case 6023:
			message = "사용자를 찾을 수 없거나 접근 권한이 없습니다.\n관리자에게 문의해주세요.";
			break;
		case 6024:
			message = "사용 가능한 OTP 토큰이 없습니다.\n관리자에게 문의해주세요.";
			break;
		case 6025:
			message = "연속 인증 실패 횟수가 초과되었습니다.\n관리자에게 문의해주세요.";
			break;
		case 6026:
			message = "인증 정책이 설정되지 않았습니다.\n관리자에게 문의해주세요.";
			break;
		case 6029:
			message = "비상 OTP 사용 기간이 만료되었습니다.\n관리자에게 문의해주세요.";
			break;
		case 6040:
			message = "OTP 서버에 연결할 수 없습니다.\n관리자에게 문의해주세요.";
			break;
		case 9999:
			message = "OTP SDK가 설치되지 않았습니다.\n관리자에게 문의해주세요.";
			break;
		default:
			message = "OTP 인증에 실패했습니다. (오류코드: " + resultCode + ")\n관리자에게 문의해주세요.";
			break;
	}

	// 인증 실패 횟수 추가
	if (authFailCnt != null && authFailCnt > 0) {
		message += "\n\n연속 인증 실패 횟수: " + authFailCnt + "회";
	}

	return message;
}

// OTP 인증 함수 (Step 2)
function otpLogin() {
	if ($('#OTPKey').val() == "") {
	    alert("OTP 인증번호를 입력해주세요.");
    	$("#OTPKey").focus();
    	return;
	}

	var postData = {
		otp : $("#OTPKey").val()
	};

	$.ajax({
        type: "POST",
        url: "<%=request.getContextPath()%>/lg/otp/authenticateOTP",
        async : false,
        data : postData,
        success: function (resultMap) {
        	console.log("OTP Auth Result:", resultMap);

        	if (resultMap.resultCode == 0) {
        		// OTP 인증 성공
        		successLogin(resultMap.redirectUrl);
        	} else { 
        		// OTP 인증 실패 - 응답코드별 메시지 표시
        		var errorMsg = getOTPErrorMessage(resultMap.resultCode, resultMap.authFailCnt);
        		alert(errorMsg);
        		$("#OTPKey").val("");
        		$("#OTPKey").focus();
        	}
        },
        error: function (request, status, error) {
            alert("OTP 인증 중 오류가 발생했습니다.");
            console.log(error);
        }
    });
	
}

// 로그인 성공 처리
function successLogin(redirectUrl) {
	var user_no = $("#user_id").val();

	// 쿠키 삭제 (보안 - 매번 사번 입력)
	deleteCookie("LG_OTP_UserNo");

	// OTP 팝업 닫기
	$("#lgOTPCheckPop").hide();

	// 입력 필드 초기화 (보안)
	$("#user_id").val("");
	$("#OTPKey").val("");

    document.location.href = "<%=request.getContextPath()%>" + redirectUrl;
}

</script>
</html>
