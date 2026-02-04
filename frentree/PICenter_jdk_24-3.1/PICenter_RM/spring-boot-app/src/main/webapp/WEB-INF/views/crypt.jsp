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
</style>

<body>

<!-- 초기화 패스워드 변경 모달 -->
<div id="frentreeChk" class="popup_layer">
	<div class="popup_box" style="height: 146px;  width: 400px; padding: 10px; top: 69%; left: 55%; background: #f9f9f9;">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 style="color: #222; padding: 0; box-shadow: none;">사용자 확인</h1>
		</div>
		<div class="popup_content">
			<div class="content-box"  style="height: 64px; background: #fff; border: 1px solid #c8ced3;">
				<!-- <h2>세부사항</h2>  -->
				<table class="popup_tbl">
					<colgroup>
						<col width="30%">
						<col width="*">
					</colgroup>
					<tbody>
						<tr>
							<th>비밀번호</th>
							<td><input style="width: 238px; padding-left: 10px;" type="password" id="frentreePW" value="" class="edt_sch"></td>
						</tr>
						
					</tbody>
				</table>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area">
				<button type="button" id="frentreeChkBtn" onclick="PWDChk()">확인</button>
			</div>
		</div>
	</div>
</div>
<div id="frentreeCrypt" class="popup_layer" style="display:none">
	<div class="popup_box" style="height: 200px;  width: 400px; padding: 10px; top: 65%; left: 55%; background: #f9f9f9;">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 style="color: #222; padding: 0; box-shadow: none;">암/복호화 조회</h1>
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
							<th>구분</th>
							<td>
								<select name="cryptflag">
									<option value="0">암호화</option>
									<option value="1">복호화</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>비밀번호</th>
							<td>
								<input style="width: 238px; padding-left: 10px;" type="text" id="chkPWD" value="" class="edt_sch">
							</td>
						</tr>
						<tr>
							<td colspan="2"><lable id="EDCrypt"></lable></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area">
				<button type="button" id="frentreeCryptBtn">저장</button>
			</div>
		</div>
	</div>
</div>
</body>

<script type="text/javascript">

var passwordRules = /(?=.*\d{1,50})(?=.*[~`!@#$%\^&*()-+=]{1,50})(?=.*[a-zA-Z]{1,50}).{8,50}$/;
var ipRules = /^(1|2)?\d?\d([.](1|2)?\d?\d){3}$/;
var intervalID;
var invalidPassword = 0;
var user_grade = 0;
var position = '';

$(document).ready(function () {
});

$('#frentreePW').keyup(function(e) {
	if (e.keyCode == 13){PWDChk()}; 
		
});

function PWDChk(){
	if (isNull($("#frentreePW").val().trim())) {
		$("#frentreePW").focus();
		alert("페이지 사용을 위한 비밀번호를 입력하세요.");
		return;
	}
	
	var postData = {
			password : $("#frentreePW").val()
		};
		
	$.ajax({
		type: "POST",
		url: "/setting/cryptPWDChk",
		async : false,
		data : postData,
		success: function (resultMap) {
			if(resultMap.resultCode == 0){
				$("#frentreeChk").hide();
				$("#frentreeCrypt").show();
				
			}else{
		       alert("비밀번호가 일치 하지 않습니다.");
			}
		},
		error: function (request, status, error) {
		    console.log("계정 해제 신청 접수 실패: ", error);
		    removeSession();
		}
	});
	
};

$(document).on('change', '.selectBoxClick', function() {
    
});


$('#frentreeCryptBtn').click(function() {
	if (isNull($("#chkPWD").val().trim())) {
		$("#chkPWD").focus();
		alert("비밀번호를 입력하세요.");
		return;
	}
	
	var postData = {
			password : $("#chkPWD").val(),
			cryptflag : $("select[name='cryptflag']").val()
		};
		
	$.ajax({
		type: "POST",
		url: "/setting/cryptPWD",
		async : false,
		data : postData,
		success: function (resultMap) {
			if(resultMap.resultCode == 0){
				$("#EDCrypt").html(resultMap.resultMessage);
			}else{
		       alert("암/복호화 과정에서 오류가 발생했습니다.");
			}
		},
		error: function (request, status, error) {
			alert("암/복호화 과정에서 오류가 발생했습니다.");
			console.log("error ", error)
		}
	});
});

</script>
</html>
