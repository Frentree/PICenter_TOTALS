<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta charset="utf-8">
	<title>LG생활건강 메일 미리보기</title>
	<style>
		body {
			font-family: 'Noto Sans KR', sans-serif;
			padding: 0;
			margin: 0;
			background-color: #f5f5f5;
		}
		.mail-container {
			max-width: 1400px;
			margin: 20px auto;
			background: #fff;
			border: 1px solid #ccc;
		}
		.mail-header {
			border-bottom: 2px solid #e4007f;
			padding: 20px 30px;
			background: linear-gradient(135deg, #e4007f 0%, #ff6b9d 100%);
		}
		.mail-header img {
			max-width: 100%;
			height: auto;
		}
		.mail-logo-section {
			text-align: center;
			padding: 40px 0 20px 0;
		}
		.mail-logo-section img {
			width: 60px;
			height: 60px;
		}
		.mail-notice {
			text-align: center;
			font-size: 13px;
			color: #999;
			font-weight: bold;
			letter-spacing: -0.7px;
			padding-bottom: 20px;
		}
		.mail-body {
			border: 1px solid #cccccc;
			margin: 0 50px 30px 50px;
			padding: 30px;
		}
		.receiver-info {
			margin-bottom: 20px;
		}
		.receiver-info table {
			width: 100%;
		}
		.receiver-info .label {
			font-size: 13px;
			color: #999;
			font-weight: 700;
			width: 80px;
		}
		.receiver-info .value {
			font-size: 13px;
			color: #222;
			font-weight: bold;
		}
		.mail-content {
			font-family: 'Noto Sans KR', sans-serif;
			font-size: 13px;
			line-height: 1.8;
			white-space: pre-wrap;
			margin: 20px 0;
		}
		.section-title {
			font-size: 13px;
			color: #999;
			font-weight: bold;
			margin: 30px 0 15px 0;
		}
		.data-table {
			width: 100%;
			border-collapse: collapse;
			font-size: 12px;
			text-align: center;
		}
		.data-table th {
			background-color: #f8f8f8;
			font-weight: bold;
			border: 1px solid #cccccc;
			padding: 10px 5px;
		}
		.data-table td {
			border: 1px solid #cccccc;
			padding: 8px 5px;
		}
		.link-section {
			margin-top: 30px;
		}
		.link-section .label {
			font-size: 13px;
			color: #999;
			font-weight: bold;
		}
		.link-section .link {
			background-color: #e6e6e6;
			font-size: 13px;
			font-weight: bold;
			padding: 5px 10px;
			display: inline-block;
			margin-top: 10px;
		}
		.link-section a {
			color: #e4007f;
			text-decoration: none;
		}
		.mail-footer {
			background-color: #e6e6e6;
			padding: 20px 40px;
			border-top: 1px solid #ccc;
		}
		.mail-footer table {
			width: 100%;
		}
		.mail-footer .footer-logo {
			width: 120px;
		}
		.mail-footer .footer-text {
			font-size: 13px;
			color: #000;
			font-weight: bold;
			text-align: center;
		}

		/* LG생건 컬러 테마 */
		.lg-theme .mail-header {
			background: linear-gradient(135deg, #e4007f 0%, #ff6b9d 100%);
			border-bottom: 2px solid #e4007f;
		}
		.lg-theme .link-section a {
			color: #e4007f;
		}
	</style>
</head>

<body>
	<div class="mail-container lg-theme">
		<!-- 헤더 (로고 이미지) -->
		<div class="mail-header">
			<img src="${pageContext.request.contextPath}/resources/assets/images/lghnh_logo.png" alt="LG생활건강" style="height: 50px;">
			<span style="color: #fff; font-size: 24px; font-weight: bold; margin-left: 20px; vertical-align: middle;">개인정보 검출관리센터</span>
		</div>

		<!-- 로고 및 안내문구 -->
		<div class="mail-logo-section">
			<img src="${pageContext.request.contextPath}/resources/assets/images/mail_icon.png" alt="mail icon">
		</div>
		<div class="mail-notice">
			개인정보 검출관리센터(PICenter)에서 발송되는 안내 메일입니다.
		</div>

		<!-- 본문 -->
		<div class="mail-body">
			<!-- 수신자 정보 -->
			<div class="receiver-info">
				<table>
					<tr>
						<td class="label">수신자</td>
						<td class="value" id="receiverName">홍길동</td>
					</tr>
					<tr style="height: 10px;"></tr>
					<tr>
						<td class="label">소속부서</td>
						<td class="value" id="receiverDept">개인정보보호팀</td>
					</tr>
				</table>
			</div>

			<!-- 메일 본문 내용 -->
			<div class="mail-content" id="mailContent">
안녕하세요.

개인정보보호법 및 사내 개인정보관리 기준에 따라
운용 서버 내 불필요하게 저장된 개인정보 파일에 대한
정기 검출 및 삭제/보호 조치 작업을 진행 중에 있습니다.

아래 검출 현황을 확인하시고 조치해 주시기 바랍니다.
			</div>

			<!-- 패턴별 조치대상 현황 -->
			<div class="section-title">개인정보 주요 패턴별 조치대상 현황</div>
			<table class="data-table" id="patternTable">
				<thead>
					<tr>
						<th style="width: 10%;">구분</th>
						<th style="width: 10%;">합계</th>
						<th>주민번호</th>
						<th>여권번호</th>
						<th>운전면허</th>
						<th>외국인등록</th>
						<th>신용카드</th>
						<th>계좌번호</th>
						<th>휴대전화</th>
						<th>이메일</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>파일 수</td>
						<td>125</td>
						<td>45</td>
						<td>12</td>
						<td>8</td>
						<td>5</td>
						<td>20</td>
						<td>15</td>
						<td>10</td>
						<td>10</td>
					</tr>
				</tbody>
			</table>

			<!-- 서버별 조치대상 현황 -->
			<div class="section-title">담당 서버 별 조치대상 현황</div>
			<table class="data-table" id="serverTable">
				<thead>
					<tr>
						<th rowspan="2">서버명</th>
						<th rowspan="2">서버IP</th>
						<th rowspan="2" style="width: 150px;">업무명</th>
						<th colspan="2">업무 담당자</th>
						<th>파일 수</th>
						<th colspan="4">미대응건수</th>
					</tr>
					<tr>
						<th>정</th>
						<th>부</th>
						<th>합계</th>
						<th>주민번호</th>
						<th>신용카드</th>
						<th>계좌번호</th>
						<th>기타</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>WEB-SERVER-01</td>
						<td>192.168.1.100</td>
						<td>웹서비스</td>
						<td>홍길동</td>
						<td>김철수</td>
						<td>50</td>
						<td>20</td>
						<td>10</td>
						<td>15</td>
						<td>5</td>
					</tr>
					<tr>
						<td>DB-SERVER-01</td>
						<td>192.168.1.101</td>
						<td>데이터베이스</td>
						<td>이영희</td>
						<td>박민수</td>
						<td>75</td>
						<td>25</td>
						<td>10</td>
						<td>30</td>
						<td>10</td>
					</tr>
				</tbody>
			</table>

			<!-- 링크 섹션 -->
			<div class="link-section">
				<div class="label">상세정보 확인</div>
				<div class="link">
					개인정보검출관리센터(PICenter) 바로가기 (<a href="#" target="_blank">http://picenter.lghnh.com</a>)
				</div>
			</div>
		</div>

		<!-- 푸터 -->
		<div class="mail-footer">
			<table>
				<tr>
					<td class="footer-logo">
						<img src="${pageContext.request.contextPath}/resources/assets/images/lghnh_logo.png" alt="LG생활건강" style="height: 32px;">
					</td>
					<td class="footer-text">
						Personal Information Center
					</td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>
