<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%@ include file="../../include/header.jsp"%>
<style>
.ui-jqgrid tr.ui-row-ltr td {
	cursor: pointer;
}

.ui-jqgrid td select {
	padding: 0;
}

.grid_top {
	width: 50%;
	float: right;
	padding: 0 22px 22px 0;
}

textarea {
	white-space: pre-wrap;
}

@media screen and (-ms-high-contrast: active) , ( -ms-high-contrast :
	none) {
	.popup_tbl td input[type="date"] {
		width: 160px !important;
	}
}

.editable-text {
	height: 213px;
	border-radius: 4px;
	border: 1px solid #c8ced3;
}

.clickGrid{
		background: #dadada
	}
</style>
<section>
	<div class="container">
		<h3>통합 관리</h3>
		<div class="content magin_t25" id="interlock" >
			<!-- user info -->
			<!-- 				[{mail=Y}, {approval=Y}, {parttern=Y}, {mailCon=Y}] -->
			<c:forEach var="set" items="${setMap}">
				<c:if test="${set.NAME == 'mail' && set.STATUS == 'Y'}">
					<div class="grid_top" >
						<h3 style="padding: 0; display: inline;">${set.SUB_NAME}</h3>
						<button type="button" name="button" style="margin-bottom: 8px;"
							class="btn_down" id="btnScheduleSave">적용</button>
						<div style="border: 1px solid #c8ced3; border-radius: 0.25rem; height: 274px;">
							<div style="height: 185px;">
								<table class="user_info approvalTh"
									style="width: 100%; border: 0px solid;">
									<colgroup>
										<col width="20%">
										<col width="80%">
									</colgroup>
									<tbody class="insertStatusData">
										<tr>
											<th style="text-align: center; padding:0; width: 100px; height: 37px; border-radius: 0.25rem;">발송
												메일 계정
											</th>
											<td><input type="text"
												id="com_date" style="padding-left: 5px; width: 50%;"
												placeholder="메일을 입력해주세요."></td>
										</tr>
										<tr>
											<th
												style="text-align: center; padding:0; width: 100px; height: 37px; border-radius: 0.25rem;">발송
												메일 비밀번호
											</th>
											<td><input
												type="password" id="pwd"
												style="padding-left: 5px; width: 50%;"
												placeholder="계정 비밀번호를 입력해주세요."></td>
										</tr>
										<tr>
											<th	style="text-align: center; width: 100px; padding: 0;" id="selectStatusTh">
												메일 발송 주기
											</th>
											<td>
												<select id="selectStatus_0" name="selectStatus_0" onchange="changeSelectStatus()">
														<option value="D">일간</option>
														<option value="W">주간</option>
														<option value="M">월간</option>
												</select> <select id="selectWeek_0" name="selectWeek_0" style="display: none;" onchange="changeSelectDetail('Week')">
														<!-- 요일 선택 -->
														<option value="2">월요일</option>
														<option value="3">화요일</option>
														<option value="4">수요일</option>
														<option value="5">목요일</option>
														<option value="6">금요일</option>
														<option value="7">토요일</option>
														<option value="1">일요일</option>
												</select> 
												<select id="selectDay_0" name="selectDay_0" style="display: none;" onchange="changeSelectDetail('Day')">
														<!-- 일자 선택 -->
												</select> <select id="selectTime_0" name="selectTime_0"
													onchange="changeSelectDetail('Time')">
												</select>
												<button type="button" name="button" class="btn_down"
													id="btnScheduleAdd">추가</button>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
							<div>
								<table class="user_info approvalTh"
									style="width: 100%; border: 0px solid;">
									<colgroup>
										<col width="20%">
										<col width="80%">
									</colgroup>
									<tbody>
										<tr>
											<th	style="text-align: center; width: 100px; padding: 0;" id="selectStatusTh">
												메일 전송 주기
											</th>
											<td>
												<label id="scheduleResult">
												아직 등록된 주기가 없습니다.
												</label> 
												<input type="hidden" id="beforeScheduleResult" value="">
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</c:if>
				
				<c:if test="${set.NAME == 'mailCon' && set.STATUS == 'Y'}">
					<!-- 	            	전송 내용 관리 -->
					<div class="grid_top">
						<h3 style="padding: 0; display: inline;">${set.SUB_NAME}</h3>
<!-- 						<button type="button" name="button" style="margin-bottom: 8px;"	class="btn_down" id="credateConDetail">추가</button> -->
						<div class="left_box2" style="overflow: hidden; max-height: 276px; height: 276px; margin-top: 8px;">
							<table id="conGrid"></table>
							<div id="conGridPager"></div>
						</div>
					</div>
				</c:if>
				
				<c:if test="${set.NAME == 'approval' && set.STATUS == 'Y'}">
					<!-- 	            	결재자 관리 -->
					<div class="grid_top">
						<h3 style="padding: 0; display: inline;">${set.SUB_NAME}</h3>
						<button type="button" name="button" class="btn_down"
							style="margin-bottom: 8px;"
							onclick="gridNameAdd('approval')">추가</button>
						<div class="left_box2" style="overflow: hidden; max-height: 276px; height: 276px;">
							<table id="approvalGrid"></table>
							<div id="approvalGridPager"></div>
						</div>
					</div>
				</c:if>
				
				<c:if test="${set.NAME == 'group_approval_mngr' && set.STATUS == 'Y'}">
					<input type="hidden" id="approvalMngrStatus" value="Y">
					<!-- 	            	팀별 결재자 관리 -->
					<div class="grid_top">
						<h3 style="padding: 0; display: inline;">${set.SUB_NAME}</h3>
<!-- 						<button type="button" name="button" style="margin-bottom: 8px;"	class="btn_down" id="credateConDetail">추가</button> -->
						<div class="left_box2" style="overflow: hidden; max-height: 276px; height: 276px; margin-top: 8px;">
							<table id="gorupApprovalMngrGrid"></table>
							<div id="gorupApprovalMngrGridPager"></div>
						</div>
					</div>
				</c:if>
				<c:if test="${set.NAME == 'approval_mngr' && set.STATUS == 'Y'}">
					<input type="hidden" id="approvalMngrStatus" value="Y">
					<!-- 	            	필수 결재자 관리 -->
					<div class="grid_top">
						<h3 style="padding: 0; display: inline;">${set.SUB_NAME}</h3>
						<button type="button" name="button" class="btn_down"
							style="margin-bottom: 8px;" id="btnSearchConSensualPop"
							onclick="userListWindows2('approval_user','${approvalMap}')">추가</button>
<!-- 						<button type="button" name="button" class="btn_down" -->
<!-- 							style="margin-bottom: 8px;" id="btnSearchConNotifierPop" -->
<!-- 							onclick="userListWindows('approval_Y')">통보자 추가</button> --> 
						<button type="button" name="button" class="btn_down"
							style="margin-bottom: 8px;" id="btnApprovalMngrListSave">적용</button>
						<input type="hidden" id="mngrDataList" value="'${approvalMap}'">
						<div class="left_box2"
							style="overflow: hidden; max-height: 276px; height: 276px;">
							<table id="approvalMngrGrid"></table>
							<div id="approvalMngrGridPager"></div>
						</div>
					</div>
				</c:if>
				
				<c:if test="${set.NAME == 'parttern' && set.STATUS == 'Y'}">
					<%-- 	                    		<caption>개인정보 유형</caption> --%>
					<div class="grid_top">
						<div style="float: left;">
							<h3 style="padding: 0; display: inline;">${set.SUB_NAME}</h3>
							<button type="button" name="button" class="btn_down" style="padding: 0 15px; margin-bottom:8px;" onclick="customPatternModel()">추가</button>
						</div>
						<div style="float: right; margin-bottom:8px;">
							<button type="button" name="button" class="btn_down" onclick="customPatternSeq()">순서변경</button>
							<button type="button" class="btn_down" id="jqgridUpBtn" style="margin: 0;" >▲</button>
							<button type="button" class="btn_down" id="jqgridDownBtn" style="margin: 0;">▼</button>
						</div>
						<div class="left_box2"
							style="overflow: hidden; max-height: 276px; height: 276px;">
							<table id="patternGrid"></table>
							<div id="patternGridPager"></div>
						</div>
					</div>
				</c:if>
				<c:if test="${set.NAME == 'mngr' && set.STATUS == 'Y'}">
<!-- 				담당자 관리 -->
					<div class="grid_top">
						<h3 style="padding: 0; display: inline;">${set.SUB_NAME}</h3>
						<button type="button" name="button" class="btn_down"style="margin-bottom: 8px;"
							onclick="gridNameAdd('server')">추가</button>
						<div class="left_box2" style="overflow: hidden; max-height: 276px; height: 276px;">
							<table id="mngrGird"></table>
							<div id="mngrGirdPager"></div>
						</div>
					</div>
				</c:if>
				<c:if test="${set.NAME == 'sesssion_time' && set.STATUS == 'Y'}">
<!--                 세션 관리 -->
                    <div class="grid_top">
                        <h3 style="padding: 0; display: inline;">${set.SUB_NAME}</h3>
                        <button type="button" name="button" class="btn_down"style="margin-bottom: 8px;"onclick="userSesstionUpdate()">적용</button>
						<div style="border: 1px solid #c8ced3; border-radius: 0.25rem; height: 274px;">
						<div style="height: 185px;">
							<table class="user_info approvalTh" style="width: 100%; border: 0px solid;">
							<colgroup>
								<col width="20%">
								<col width="80%">
							</colgroup>
							<tbody> 
								<tr>
									<th style="text-align: center; width: 100px; padding: 0;">세션 타임아웃 설정</th>
									<td>
										<input type="number" id="session" style="padding-left: 5px; width: 225px;" placeholder="변경할 세션을 '초'기준으로 입력해주세요."> 
										<span style="position: relative; top: 1px;">초</span>
									</td> 
								</tr>
								<tr>
									<th style="text-align: center; width: 100px; padding: 0;">현재 설정</th>
									<td>
										<label id="session_html"> </label>
									</td>
								</tr>
							</tbody> 
							</table>         
						</div>
						<!-- <div> 
							<h4>&nbsp; 현재 설정</h4>
							<label id="session_html"> </label>
                       </div> -->
                   </div> 
                    </div>
                </c:if>
                <c:if test="${set.NAME == 'db_backup' && set.STATUS == 'Y'}">
<!--                 백업 관리 -->
                    <div class="grid_top">
                        <h3 style="padding: 0; display: inline;">${set.SUB_NAME}</h3> 
                        <button type="button" name="button" class="btn_down"style="margin-bottom: 8px;" id="tableBackUp">백업</button>
                        <button type="button" name="button" class="btn_down"style="margin-bottom: 8px;" id="tableBackUpSetting">설정</button>
                        <div class="left_box2" style="overflow: hidden; max-height: 276px; height: 276px;">
                            <table id="backUpGrid"></table>
							<div id="backUpPager"></div>
                        </div>
                    </div>
                </c:if>
                <c:if test="${set.NAME == 'db_rollback' && set.STATUS == 'Y'}">
<!--                 복원 관리 -->
                    <div class="grid_top">
                        <h3 style="padding: 0; display: inline;">${set.SUB_NAME}</h3>
                        <button type="button" name="button" class="btn_down" style="margin-bottom: 8px;" id="rollBackFileBtn">복원</button> 
                        <div class="left_box2" style="overflow: hidden; max-height: 276px; height: 276px;">
                            <table id="rollBackGrid"></table>
							<div id="rollBackPager"></div>
                        </div>
                    </div>
                </c:if>
			</c:forEach> 
		</div>
	</div>
</section>
<!-- section -->
<%@ include file="../../include/footer.jsp"%>

<!-- 팀별 담당자 관리 -->
<div id="btnTeamApproval" class="popup_layer" style="display: none;">
	<div class="ui-widget-content" style="position: absolute; height: 402px; left: 27%; top: 28%; touch-action: none; max-width: 920px; z-index: 999; background: #f9f9f9;">
		<img class="CancleImg" id="btnCancleBtnTeamApproval"
			src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_container">
			<div class="popup_top" style="background: #f9f9f9;">
				<h1 style="color: #222; box-shadow: none; padding: 0;" id="approvalTeamName">팀별 결재자 수정</h1>
			</div>
			<div class="popup_content">
				<div class="content-box"
					style="width: 900px !important; height: 304px; background: #fff; border: 1px solid #c8ced3;">
					<table id="teamApprovalrGrid"></table>
					<div id="teamApprovalrGridPager"></div>
				</div>
				<div class="popup_btn">
					<div class="btn_area" style="padding: 10px 2px; margin: 0;">
						<input type="hidden" id="update_insa_code">
						<button type="button" id="btnTeamApprovalPopUpdate" style="font-weight: inherit; font-size: 12px; padding: 0 15px;" >추가</button>
						<button type="button" id="btnTeamApprovalClose" style="font-weight: inherit; font-size: 12px; padding: 0 15px;">닫기</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- 개인정보 상세 보기 팝업 -->
<div id="btnPatternPop" class="popup_layer" style="display: none;">
	<div class="ui-widget-content" id="popup_datatype"
		style="position: absolute; height: 500px; left: 27%; top: 18%; touch-action: none; max-width: 920px; z-index: 999; background: #f9f9f9;">
		<img class="CancleImg" id="btnCanclebtnPatternPop"
			src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_container">
			<div class="popup_top" style="background: #f9f9f9;">
				<h1 style="color: #222; box-shadow: none; padding: 0;"
					id="patternNm">개인정보 유형 수정</h1>
			</div>
			<div class="popup_content">
				<div class="content-box"
					style="width: 900px !important; height: 417px; background: #fff; border: 1px solid #c8ced3;">
					<table class="popup_tbl">
						<colgroup>
							<col width="20%">
							<col width="*">
							<col width="20%">
							<col width="*">
							<col width="3%">
						</colgroup>
						<tr>
							<th>개인정보 검출 유형 명</th>
							<td colspan="3" id="patternReconNmTd">
								<label id="patternReconNm2" style="width: 100%;"></label>
								<input type="text" id="patternReconNm" style="width: 100%; display: none;">  
								<input type="hidden" id="patternIDX" style="width: 100%;"></td>
						</tr>
						<tr>
							<th>영문 개인정보 유형 명</th>
							<td><input type="text" id="patternNameEr"
								style="width: 100%;"></td>
							<th>한글 개인정보 유형 명</th>
							<td><input type="text" id="patternNameKr"
								style="width: 100%;"></td>
						</tr>
						<tr>
							<th>마스킹 위치</th>
							<td><select id="patternMaskType" name="patternMaskType"
								style="width: 100%;">
									<option value="N" selected="selected">없음</option>
									<option value="T">처음(앞)</option>
									<option value="B">마지막(뒤)</option>
							</select></td>
							<th>마스킹 기준</th>
							<td><input type="text" id="patternMaskChk"
								style="width: 100%;" placeholder=" 기준이 될 마스킹 위치를 입력해주세요"></td>
						</tr>
						<tr>
							<th>마스킹 개수</th>
							<td><input type="number" id="patternMaskCnt"
								style="width: 100%;"></td>
							<th>개인정보 색상</th>
							<td><input type="text" id="patternColor" style="width: 50%;">
								<div style="width: 40%; float: right; margin-top: 7px;"
									id="bakPatternColor">&nbsp;</div></td>
						</tr>
						<tr>
							<th>개인정보 규칙<br> <span style="font-size: 11px;">(임계치,
									중복 제거 작성 위치는 <br> <span
									style="font-weight: bold; color: #DC143C;">"%s"</span> 로
									구분됩니다.)
							</span>
							</th>
							<td colspan="3" id="patternRuleTd">
								<textarea id="patternRule" style="width: 100%; height: 200px; resize: none;" onchange="patternChange();"></textarea>
							</td>
						</tr>
					</table>
				</div>
				<div class="popup_btn">
					<div class="btn_area" style="padding: 10px 2px; margin: 0;">
						<p id="comment"
							style="position: absolute; bottom: 17px; right: 247px; font-size: 12px; color: #2C4E8C; text-align: center;">개인정보
							유형을 자주 수정할 시 올바른 데이터가 조회되지 않을 수도 있습니다.</p>
						<button type="button" id="btnPatternPopchk" style="font-weight: inherit; font-size: 12px; padding: 0 15px;">확인</button>
						<button type="button" id="btnPatternPopUpdate" style="font-weight: inherit; font-size: 12px; padding: 0 15px;" >수정</button>
						<button type="button" id="btnPatternPopSave" style="font-weight: inherit; font-size: 12px; padding: 0 15px; display: none;">저장</button>
						<button type="button" id="btnPatternPopClose" style="font-weight: inherit; font-size: 12px; padding: 0 15px;">닫기</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 개인정보 상세보기 팝업 종료 -->
<!-- 개인정보 선택 팝업-->
<div id="taskWindow" class="ui-widget-content"
	style="position: absolute; left: 10px; top: 10px; touch-action: none; width: 150px; z-index: 999; border-top: 2px solid #222222; box-shadow: 2px 2px 5px #ddd; display: none">
	<ul>
		<li class="status">
			<button id="updateBtn">조회</button>
		</li>
		<li class="status">
			<button id="deleteBtn">삭제</button>
		</li>
	</ul>
</div>
<!-- 명칭 지정 팝업-->
<div id="nameTaskWindow" class="ui-widget-content"
	style="position: absolute; left: 10px; top: 10px; touch-action: none; width: 150px; z-index: 999; border-top: 2px solid #222222; box-shadow: 2px 2px 5px #ddd; display: none">
	<input type="hidden" id="nameGridStatus">	
	<ul>
		<li class="status">
			<button id="nameDataUpdated">수정</button>
		</li>
		<li class="status">
			<button id="nameDataDel">삭제</button>
		</li>
	</ul>
</div>
<!-- 명칭 지정 팝업-->
<div id="conTaskWindow" class="ui-widget-content"
	style="position: absolute; left: 10px; top: 10px; touch-action: none; width: 150px; z-index: 999; border-top: 2px solid #222222; box-shadow: 2px 2px 5px #ddd; display: none">
	<input type="hidden" id="nameGridStatus">	
	<ul>
		<li class="status">
			<button id="conDataUpdated">수정</button>
		</li>
		<li class="status">
			<button id="conDataDel">삭제</button>
		</li>
	</ul>
</div>
<!-- 명칭 상세보기 팝업 -->
<div id="btnConDetailPop" class="popup_layer" style="display: none;">
	<div class="ui-widget-content" id="popup_datatype" 
		style="position: absolute; height: 483px; left: 26%; top: 18%; touch-action: none; max-width: 911px; z-index: 999; background: #f9f9f9;">
		<img class="CancleImg" id="btnCanclebtnConDetailPop"
			src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_container" style="width: 913px;">
			<div class="popup_top" style="background: #f9f9f9;">
				<h1 style="color: #222; box-shadow: none; padding: 0;" id="templateHeader">전송 내용 수정</h1>
			</div>
			<div class="popup_content">
				<div class="content-box"
					style="width: 893px !important; height: 400px; background: #fff; border: 1px solid #c8ced3;">
					<table class="popup_tbl">
						<colgroup>
							<col width="20%">
							<col width="*">
						</colgroup>
						<tr>
							<th>구분</th>
							<td>
								<span id="conDetailStatus" style="padding:0 6px;"></span>
							</td>
						</tr>
						<tr>
							<th>제목</th>
							<td>
								<span id="version_nm" style="padding:0 7px;">[${picSession.version.version_nm}]</span>
								<input type="text" id="conDetailNm" >
								<input type="hidden" id="conDetailIdx" style="width: 100%;"></td>
						</tr>
						<tr>
							<th>내용</th>
							<td>
								<textarea id="conDetailContent"	style="width: 100%; height: 285px; resize: none;"></textarea>
							</td>
						</tr>
					</table>
				</div>
				<div class="popup_btn">
					<div class="btn_area" style="padding: 10px 2px; margin: 0;">
						<button type="button" id="btnConDetailPopUpdate" style="font-weight: inherit; font-size: 12px; padding: 0 15px;">수정</button>
						<button type="button" id="btnConDetailPopClose" style="font-weight: inherit; font-size: 12px; padding: 0 15px;">닫기</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- 백업 설정 팝업-->
<div id="btnBackUpSettingPop" class="popup_layer" style="display: none;">
	<div class="ui-widget-content" id="popup_backUpSetting"
		style="position: absolute; height: 240px; left: 40%; top: 33%; touch-action: none; z-index: 999; background: #f9f9f9;">
		<img class="CancleImg" id="btnCanclebtnBackUpSettingPop"
			src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_container" style="width: 395px;">
			<div class="popup_top" style="background: #f9f9f9;">
				<h1 style="color: #222; box-shadow: none; padding: 0;" id="settingNm">백업 설정</h1>
			</div>
			<div class="popup_content">
				<div class="content-box" style="width: 375px !important; height: 155px; background: #fff; border: 1px solid #c8ced3;">
					<table class="popup_tbl">
						<colgroup>
							<col width="25%">
							<col width="*">
						</colgroup>
						<tr>
							<th>최대 백업 수</th>
							<td>
								<input type="number" id="maxBackUp" style="width: 156px; padding-left: 5px;">  
							</td>
						</tr>
						<tr>
							<th>백업 시작 시간</th>
							<td id="start_time"></td>
						</tr> 
						<tr>
							<th>주기</th>
							<td>
								<select name="cycle" id="cycle">
									<option value="1">매일</option>
									<option value="2">매주</option>
									<option value="3">매월</option>
							</select>
							</td>
						</tr>
					</table>
				</div>
				<div class="popup_btn">
					<div class="btn_area" style="padding: 10px 0; margin: 0;">
						<button type="button" id="btnBackUpSettingPopSave" style="font-weight: inherit; font-size: 12px; padding: 0 15px;">저장</button>
						<button type="button" id="btnBackUpSettingPopClose" style="font-weight: inherit; font-size: 12px; padding: 0 15px;">닫기</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">

var beforeCheckGrid = null;
var beforeCheckStatus = true;
var isCredateNameGrid = true;

$(document).ready(function () {
	patternGrid(); // 개인정보 유형 grid
	mailFun(); // 메일 시작 설정
	groupApprovalMngrGrid(); // 팀별 결재자 grid
	approvalMngrGrid(); // 필수 결재자 grid
	approvalGrid(); // 결재 라인 grid
	mngrGird(); // 담당자 명 grid
	conGrid(); // 전송 content grid(메일, 결재 내용)
	sessionTime(); // 세션 설정(화면)
	backUpGrid(); // DB 백업
	rollBackGrid(); // DB 복원
	
	var setMap = '${setMap}';
	var results = setMap.split('},');
	var resultsSize = 0;
	for(r=0 ; r < results.length ; r++){
		if(results[r].includes("=Y,")){
			++resultsSize;
		}
	}
	  
	if(resultsSize%2 != 1 ){ ++resultsSize;}   
	$("#interlock").css("height", (resultsSize*185)+"px"); 
	
	$(document).click(function(e){
		$("#taskWindow").hide();
		$("#nameTaskWindow").hide();
		$("#conTaskWindow").hide();
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
    
});

/* ----------------------------------- 개인정보 유형 시작 ----------------------------------- */
function patternGrid(){
	$("#patternGrid").jqGrid({
<%-- 		url: "<%=request.getContextPath()%>/setting/patternList", --%>
		datatype: "local",
	   	mtype : "POST",
	   	ajaxGridOptions : {
			type    : "POST",
			async   : true
		},
		colNames:['', '순서', '구분', '개인정보 유형 명','염문 유형명',  '표기 색상', '표기 색상', '마스킹 개수', '마스킹 기준', '마스킹 위치', '마스킹 순서', '','', '개인정보 패턴'],
		colModel: [
			{ index: 'PATTERN_IDX', 		name: 'PATTERN_IDX', 		width : 5, align : 'center', hidden:true},
			{ index: 'PATTERN_CNT', 		name: 'PATTERN_CNT',		width: 10, align: 'left', hidden:true},
			{ index: 'PATTERN_KR_NAME', 	name: 'PATTERN_KR_NAME',	width: 20, align: 'left'},
			{ index: 'PATTERN_CODE', 		name: 'PATTERN_CODE',		width: 50, align: 'left'}, 
			{ index: 'PATTERN_EN_NAME', 	name: 'PATTERN_EN_NAME',	width: 0, align: 'left', hidden:true},
			{ index: 'COLOR_CODE', 			name: 'COLOR_CODE',			width: 40, align: 'left', hidden:true},
			{ index: 'COLOR', 				name: 'COLOR',				width: 40, align: 'left',  formatter: dataColor},
			{ index: 'MASK_CNT', 			name: 'MASK_CNT',			width: 0, align: 'center', hidden:true},
			{ index: 'MASK_CHK',			name: 'MASK_CHK',			width: 0, align: 'center', hidden:true},
			{ index: 'MASK_TYPE', 			name: 'MASK_TYPE', 			width: 0, align: 'center', hidden:true},
			{ index: 'MASK_DETAIL', 		name: 'MASK_DETAIL', 		width: 35, align: 'center', formatter: maskType},
			{ index: 'BUTTON', 				name: 'BUTTON', 			width: 20, align: 'center'},
			{ index: 'PATTERN_UPDATED', 	name: 'PATTERN_UPDATED', 	width: 20, align: 'center', hidden:true},
			{ index: 'PATTERN_RULE',		name: 'PATTERN_RULE',		width: 0, align: 'center', hidden:true}
		],
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: $("#patternGrid").parent().width(),
		height: 200,
		loadonce: true, // this is just for the demo
	   	autowidth: true,
		shrinkToFit: true,
		rownumbers : false, // 행번호 표시여부
		rownumWidth : 30, // 행번호 열의 너비	
		rowNum:25,
		rowList:[25,50,100],		
		pager: "#patternGridPager",
		cmTemplate: {sortable: false},
		//jqgrid의 특성상 - rowpos의 이벤트는 onSelectCell, beforeEditCell 다 해주어야 함
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	  	
	  		if(beforeCheckStatus){
		  		/* 이전 체크된 값 제거 */
		  		if(beforeCheckGrid != null){
			  		$("#patternGrid #"+beforeCheckGrid).removeClass("clickGrid");
		  		}
		  		
		  		/* 같은 그리드 선택 시 선택 취소*/
		  		if(beforeCheckGrid == rowid){
			  		$("#patternGrid #"+rowid).removeClass("clickGrid");
			  		beforeCheckGrid = null;
		  		}else{
			  		$("#patternGrid #"+rowid).addClass("clickGrid");
			  		beforeCheckGrid = rowid;
		  		}
	  		}else{
	  			beforeCheckStatus = true;
	  		}
	  	},
	  	ondblClickRow: function(nRowid, icol, cellcontent, e){
	  		
	  	},
	  	onCellSelect: function(rowid,icol,cellcontent,e) {
	    },
		loadComplete: function(data) {
			var ids = $("#patternGrid").getDataIDs() ;
            $.each(ids, function(idx, rowId) { 
            	$("#patternGrid").setCell(rowId, 'BUTTON', "<button type='button' class='gridSubSelBtn' name='gridSubSelBtn' style='margin-left: 7px'>선택</button>");
    			$(".gridSubSelBtn").on("click", function(e) {
    		  		e.stopPropagation();
    				
    				$("#patternGrid").setSelection(event.target.parentElement.parentElement.id);
    				
    				var offset = $(this).parent().offset();
    				$("#taskWindow").css("left", (offset.left - $("#taskWindow").width()) + 30 + "px");
    				// $("#taskWindow").css("left", (offset.left - $("#taskWindow").width() + $(this).parent().width()) + "px");
    				$("#taskWindow").css("top", (offset.top + $(this).height() - 3)+ "px"); 

    				var bottomLimit = $(".left_box2").offset().top + $(".left_box2").height();
    				var taskBottom = Number($("#taskWindow").css("top").replace("px","")) + $("#taskWindow").height();
    				
    				$("#taskWindow").show();
    			}); 
            });
	    },
	    gridComplete : function() {
	    } 
	});
	 
	var postData = {};
	$("#patternGrid").setGridParam({
		url:"<%=request.getContextPath()%>/setting/patternList", 
		postData : postData, 
		datatype:"json" 
		}).trigger("reloadGrid");
	
}

function patternChange(){
	$("#btnPatternPopSave").css("background", "rgb(228, 228, 228)");
	$("#btnPatternPopUpdate").css("background", "rgb(228, 228, 228)");
	
	$("#btnPatternPopchk").show();
	$("#btnPatternPopSave").prop("disabled", true);
// 	$("#btnPatternPopUpdate").prop("disabled", true);
}

// 개인정보 유형 팝업 표시
$("#updateBtn").click(function(){
	$("#taskWindow").hide();
	var row = $("#patternGrid").getGridParam("selrow");
	 var data = $('#patternGrid').getRowData(row);
	 
	 var PATTERN_IDX = data.PATTERN_IDX;
	 var PATTERN_KR_NAME = data.PATTERN_KR_NAME;
	 var PATTERN_EN_NAME = data.PATTERN_EN_NAME;
	 var COLOR_CODE = data.COLOR_CODE;
	 var MASK_CNT = data.MASK_CNT;
	 var MASK_CHK = data.MASK_CHK;
	 var MASK_TYPE = data.MASK_TYPE;
	 var PATTERN_RULE = data.PATTERN_RULE;
	 var PATTERN_CODE = data.PATTERN_CODE;
	 
	 $("#btnPatternPopchk").show();
	$("#btnPatternPopSave").css("background", "#e4e4e4");
	$("#btnPatternPopUpdate").css("background", "#e4e4e4");
	$("#btnPatternPopSave").prop("disabled", true);
// 	$("#btnPatternPopUpdate").prop("disabled", true);

	 
	 $("#patternNm").html(PATTERN_KR_NAME +" 유형 수정");
	 $("#patternColor").val(COLOR_CODE);
	 $("#bakPatternColor").css("background-color", COLOR_CODE);
	 $("#patternRuleTd").html('<textarea id="patternRule" style="width: 100%; height:200px; resize: none;" onchange="patternChange();"></textarea>');
	 
// 	 if(status != "Y"){
// 		 $("#patternReconNmTd").html(PATTERN_CODE);
// 		 $("#patternRuleTd").html(PATTERN_RULE);
// 	 }else{
// 		 $("#patternReconNmTd").html('<input type="text" id="patternReconNm" style="width: 100%;"><input type="hidden" id="patternIDX" style="width: 100%;">');
// 		 $("#patternRuleTd").html('<textarea id="patternRule" style="width: 100%; height:200px; resize: none;"></textarea>');
// 	 }

	 $("#patternReconNm").val(PATTERN_CODE);
	 $("#patternReconNm2").html(PATTERN_CODE);
	 $("#patternNameKr").val(PATTERN_KR_NAME);
	 $("#patternNameEr").val(PATTERN_EN_NAME);
	 $("#patternMaskCnt").val(MASK_CNT);
	 $("#patternMaskChk").val(MASK_CHK); 
	 $("#patternRule").val(PATTERN_RULE);
	 $("#patternIDX").val(PATTERN_IDX);
 

	$("#patternReconNm2").show();
	$("#patternReconNm").css("display", "none");
	$("#btnPatternPopUpdate").show();
	$("#btnPatternPopSave").hide(); 
	$("#patternRule").show();
	 
// 	 if(status != "Y"){
// 		 $("#patternReconNm").hide();
// 		 $("#patternRule").hide();
// 	 }else{
// 		 $("#patternReconNm").show();
// 		$("#btnPatternPopUpdate").show();
// 		$("#btnPatternPopSave").hide(); 
// 		 $("#patternRule").show();
// 	 }
	 
	 $("select[name=patternMaskType]").val(MASK_TYPE).prop("selected", true);
	 
	 $("#btnPatternPop").show();
});

$("#deleteBtn").click(function(){
	var row = $("#patternGrid").getGridParam("selrow");
	 var data = $('#patternGrid').getRowData(row);
	var postData = {patternIDX : data.PATTERN_IDX};
	
	if(data.PATTERN_UPDATED == "N"){
		alert("삭제할 수 없는 개인정보입니다. 관리자에게 문의해주세요.");
		return true;
	}
	
	if(confirm("유형을 삭제 시 더이상 조회/검출 하지 않습니다. \n"+data.PATTERN_KR_NAME+"를 삭제하시겠습니까?")){
		$.ajax({
			type: "POST",
			url: "/setting/deleteCustomPattern",
			//async : false,
			data : postData,
			dataType: "json",
		    success: function (resultMap) {
		    		postData = {};
		    	if(resultMap.resultCode == 0){
		    		$("#patternGrid").setGridParam({
		    			url:"<%=request.getContextPath()%>/setting/patternList", 
		    			postData : postData, 
		    			datatype:"json" 
		    			}).trigger("reloadGrid");
		    		
		    		alert("개인정보 유형이 삭제되었습니다.");
		    		$("#btnPatternPop").hide();
		    	}else{
		    		alert("삭제를 실패했습니다. \n 관리자에게 문의해주세요.");
		    	}
		    },
		    error: function (request, status, error) {
		    	alert("설정 저장을 실패하였습니다.");
	        	console.log("ERROR : ", error);
		    }
		});
	}
});

$("#btnTeamApprovalPopUpdate").click(function(){
	
	var pop_url = "${getContextPath}/popup/userList";
	var id = "targetList"
	var winWidth = 700;
	var winHeight = 570;
	var popupOption= "width="+winWidth+", height="+winHeight + ", left=0, top=0, scrollbars=no, resizable=no, location=no"; 	
	var pop = window.open(pop_url,id,popupOption);
	/* popList.push(pop);
	sessionUpdate(); */
	
	var newForm = document.createElement('form');
	newForm.method='POST';
	newForm.action=pop_url;
	newForm.name='newForm';
	newForm.target=id;
	
	var data = document.createElement('input');
	data.setAttribute('type','hidden');
	data.setAttribute('name','info');
	data.setAttribute('value','teamIfo');
	
	newForm.appendChild(data);
	document.body.appendChild(newForm);
	newForm.submit();
	
	document.body.removeChild(newForm);
});

$("#btnCancleBtnTeamApproval, #btnTeamApprovalClose").click(function(){
	$("#btnTeamApproval").hide();
});
$("#btnPatternPopClose, #btnCanclebtnPatternPop").click(function(){
	$("#patternReconNm").val("");
	$("#patternReconNm2").val("");
	$("#patternIDX").val("");
	$("#patternNameEr").val("");
	$("#patternNameKr").val("");
	$("#patternMaskType").val(""); // check
	$("#patternMaskChk").val("");
	$("#patternMaskCnt").val("");
	$("#patternColor").val("");
	$("#patternRule").val("");
	$("#bakPatternColor").css("background-color", "");
	
	$("#patternNm").html("개인정보 유형 수정");
	$("#btnPatternPop").hide();
});
$("#btnConDetailPopClose, #btnCanclebtnConDetailPop").click(function(){
	$("#conDetailNm").val("");
	$("#conDetailIdx").val("");
	$("#conDetailContent").val("");
	
	$("#btnConDetailPop").hide();
});

$("#patternColor").change( function(){
	
	var bColor = $("#patternColor").val();
	$("#bakPatternColor").css("background-color", bColor);
	
});
  
function teamApprovalGridReload(){
	
	var updateInsaCode = $("#update_insa_code").val();
	
	var postData = {insa_code : updateInsaCode};
	$("#update_insa_code").val(updateInsaCode);  
	$("#teamApprovalrGrid").setGridParam({
		url:"<%=request.getContextPath()%>/setting/selectGroupApprovalUser", 
		postData : postData, 
		datatype:"json" 
		}).trigger("reloadGrid");
	
	$("#gorupApprovalMngrGrid").setGridParam({  
		url:"<%=request.getContextPath()%>/setting/groupApprovalList", 
		postData : {}, 
		datatype:"json" 
		}).trigger("reloadGrid");
}

function textlength(cellvalue, options, rowObject) {
	
	var result = rowObject.CON;
	if(result != null && result.length > 23){
		result = result.substr(0, 23) + " ... ";
	}
	return result;
}
function dataColor(cellvalue, options, rowObject) {
	
	var result = cellvalue;
	
	var COLOR_CODE = rowObject.COLOR_CODE;
	
	if( COLOR_CODE != null){
		
		result =  COLOR_CODE +"<div style=\"width: 54%; float:right; background-color : "+ COLOR_CODE+"; \">&nbsp;</div>"
	}else{
		 result = "지정된 색상이 없습니다.";
	}
	return result;
}

function maskType(cellvalue, options, rowObject) {
	
	var cnt = rowObject.MASK_CNT;
	var chk = rowObject.MASK_CHK;
	
	var result = "";
	
	if(rowObject.MASK_TYPE =="T"){
		result = "앞 " + cnt + " 자리";
	}else if(rowObject.MASK_TYPE =="B"){
		result = "뒤 " + cnt + " 자리";;		
	}
	
	if(chk != null && chk != ""){
		result +=" ( 기준 : " + chk + ")";
	}
	return result;
}

function btnCreate(cellvalue, options, rowObject) {
	if(cellvalue == 'Y'){
		return "<button type='button' style='padding-top: 4px; padding-bottom:4px;' class='gridSubSelBtn' onClick='btnPatternPop("+options.rowId+", \"Y\")'>선택</button>";
	}else {
		return "<button type='button' style='padding-top: 4px; padding-bottom:4px;' class='gridSubSelBtn' onClick='btnPatternPop("+options.rowId+", \"N\")'>선택</button>";
	}
}

// 조회 순서 변경
$("#jqgridUpBtn").click(function(){
	
	var rowCnt = $("#patternGrid").jqGrid("getRowData").length;
	var totalRowData = $("#patternGrid").getRowData();
	var beforeRowId = 0;
	
	var rowId = $("#patternGrid").jqGrid("getGridParam", "selrow");
	
	if(rowId){
		var totalRows = $("#patternGrid").jqGrid("getGridParam", 'reccount');
		var row = $("#patternGrid").find("#"+rowId);
		var rowData = $("#patternGrid").jqGrid("getRowData", rowId);
		var index = $("#patternGrid").jqGrid("getInd", rowId);
		var prevRowId = $("#patternGrid").jqGrid("getDataIDs")[index -2];
		
		if(prevRowId){

			for(a=0; a < rowCnt ; a++){
				if(rowId == totalRowData[a].GIDX){
					beforeRowId = a-1;
					break;
				}
			}
			var beforeRowData = $("#patternGrid").jqGrid("getRowData", totalRowData[beforeRowId].GIDX);
			
			$("#patternGrid").jqGrid('delRowData', rowId);
			$("#patternGrid").jqGrid('addRowData', rowId, rowData, 'before', prevRowId);
			
			var prevRow = row.prev();
			if (prevRow.length){
				row.insertBefore(prevRow);
			}
		}
	}
	
	/* Row 선택 유지*/
	beforeCheckStatus=false;
	$("#patternGrid").jqGrid("setSelection", rowId, true);
	$("#patternGrid #"+rowId).addClass("clickGrid");
});
$("#jqgridDownBtn").click(function(){
	
	var rowCnt = $("#patternGrid").jqGrid("getRowData").length;
	var totalRowData = $("#patternGrid").getRowData();
	var beforeRowId = 0;
	
	var rowId = $("#patternGrid").jqGrid("getGridParam", "selrow");
	
	if(rowId){
		var totalRows = $("#patternGrid").jqGrid("getGridParam", 'reccount');
		var row = $("#patternGrid").find("#"+rowId);
		var rowData = $("#patternGrid").jqGrid("getRowData", rowId);
		var index = $("#patternGrid").jqGrid("getInd", rowId);
		var nextRowId = $("#patternGrid").jqGrid("getDataIDs")[index];

		if(nextRowId){
			
			for(a=0; a < rowCnt ; a++){
				if(rowId == totalRowData[a].GIDX){
					beforeRowId = a+1;
					break;
				}
			}
			var beforeRowData = $("#patternGrid").jqGrid("getRowData", totalRowData[beforeRowId].GIDX);
			
			$("#patternGrid").jqGrid('delRowData', rowId);
			$("#patternGrid").jqGrid('addRowData', rowId, rowData, 'after', nextRowId);
			
			var prevRow = row.prev();
			if (prevRow.length){
				row.insertBefore(prevRow);
			}
		} 
	}
	
	/* Row 선택 유지*/
	beforeCheckStatus=false;
	$("#patternGrid").jqGrid("setSelection", rowId, true);
	$("#patternGrid #"+rowId).addClass("clickGrid");
});

/* ----------------------------------- 개인정보 유형 끝 ----------------------------------- */
/* ----------------------------------- 메일 스케줄 시작 ----------------------------------- */

function addSelectOption(num){
	
// 	일자 선택
	var day_content = "";
	for(i = 1; i < 32; i++){
		day_content += "<option value=\""+i+"\" >"+i+"</option>"
	}
	$("#selectDay_"+num).append(day_content);
	
// 	시간 선택
	var time_content = "";
	for(i = 0; i < 24; i++){
		time_content += "<option value=\""+i+"\" >";
		if(i < 10){
			time_content += "0"+i+"시</option>"
		}else{
			time_content += i+"시</option>"
		}
	}
	$("#selectTime_"+num).append(time_content);
	
}

function mailFun(){
	addSelectOption(0);
	
	var scheduleResult = "";
	var resultList = "${resultList}";
	var resultSize = "${resultSize}";
	
	var result = "${resultList[0]}";

	$("#com_date").val("${resultList[0].COM}");
	$("#selectStatus_0").val("${resultList[0].STATUS}").prop("selected", true);
	$("#selectWeek_0").val("${resultList[0].DAY}").prop("selected", true);
	$("#selectDay_0").val("${resultList[0].DAY}").prop("selected", true);
	$("#selectTime_0").val("${resultList[0].TIME}").prop("selected", true);
	
	var status = $("#selectStatus_0 option:selected").val();
	
	var resultDay = [];
	var resultTime = [];
	
	resultDay.push("${resultList[0].DAY}");
	resultTime.push("${resultList[0].TIME}")
	
	for(r=0 ; r < resultSize ; r++){
		
		if(r==1){
			chk_num = r-1
			$("#btnScheduleAdd").click();
			$("#selectWeek_1").val("${resultList[1].DAY}").prop("selected", true);
			$("#selectDay_1").val("${resultList[1].DAY}").prop("selected", true);
			$("#selectTime_1").val("${resultList[1].TIME}").prop("selected", true);
			
			resultDay.push("${resultList[1].DAY}");
			resultTime.push("${resultList[1].TIME}")
			
		}else if(r==2){
			chk_num = r-1
			
			$("#btnScheduleAdd").click();
			$("#selectWeek_2").val("${resultList[2].DAY}").prop("selected", true);
			$("#selectDay_2").val("${resultList[2].DAY}").prop("selected", true);
			$("#selectTime_2").val("${resultList[2].TIME}").prop("selected", true);
			
			resultDay.push("${resultList[2].DAY}");
			resultTime.push("${resultList[2].TIME}")
		}
	}

	chk_num = resultSize-1;
	
	switch(status) {
	    case "D": scheduleResult += "매일 "; break;
	    case "W": scheduleResult += "매주 "; break;
	    case "M": scheduleResult += "매달 "; break;
	}

	for(i=0 ; i < resultSize ; i++){
		
		if(i > 2){
			break;
		}
		
		if(status == "D"){
			$("#selectWeek_0").hide();
			$("#selectDay_0").hide();
			
			scheduleResult += resultTime[i] + "시"
			if(i < resultTime.length-1 ) scheduleResult += " / "
			
		}else if(status == "W"){
			$("#selectWeek_0").show();
			$("#selectDay_0").hide();
			
			for(i=0 ; i < resultDay.length ; i++){
				
				switch(resultDay[i]) {
				    case "2": scheduleResult += "월요일 "; break;
				    case "3": scheduleResult += "화요일 "; break;
				    case "4": scheduleResult += "수요일 "; break;
				    case "5": scheduleResult += "목요일 "; break;
				    case "6": scheduleResult += "금요일 "; break;
				    case "7": scheduleResult += "토요일 "; break;
				    case "1": scheduleResult += "일요일 "; break;
				}
				
	   			scheduleResult += resultTime[i] +"시"
	   			if(i < resultDay.length-1 ) scheduleResult += " / "
			}

		}else if(status == "M"){
			$("#selectWeek_0").hide();
			$("#selectDay_0").show();
			
			scheduleResult += resultDay[i] + "일 "
			scheduleResult += resultTime[i] + "시"
			if(i < resultDay.length-1 ) scheduleResult += " / "
		}
	}
	
	$("#beforeScheduleResult").val(scheduleResult);
	var beforeresult = $("#beforeScheduleResult").val();
	
	if(beforeresult == " "){
		$("#scheduleResult").html("아직 등록된 주기가 없습니다.");
	}else{
		$("#scheduleResult").html(beforeresult);
	}
}

/* 메일 데이터 저장 */
$("#btnScheduleSave").click(function() {
	
	var resultList = [];
	var resultDay = [];
	var resultTime = [];
	
	var status = $("#selectStatus_0 option:selected").val();
	
	for(i=0 ; i < chk_num+1 ; i++){
		var  data = {
			"week" : $("#selectWeek_"+i+" option:selected").val(),
			"day" : $("#selectDay_"+i+" option:selected").val(),
			"time" : $("#selectTime_"+i+" option:selected").val()
			
		}
		resultList.push(data);
	}
	
	
	if( $("#com_date").val() == ""){
		$("#com_date").focus();
		alert("전송할 계정을 입력하십시오.");
		return;
	}else{
		if(!$("#com_date").val().includes("@")){
			$("#com_date").focus();
			alert("올바른 메일 주소를 입력하십시오.");
			return;
		}
	}
	
	if( $("#pwd").val() == ""){
		$("#pwd").focus();
		alert("비밀번호를 입력하십시오.");
		return;
	}
	
	if(chk_num!=0){
		for(i=0 ; i < chk_num+1 ; i++){
			for(j=0 ; j < chk_num+1 ; j ++){
				if(i!=j){
					if(status =="D"){
						if(resultList[i].time == resultList[j].time){
							alert("동일한 시간이 존재합니다.");
							return;
						}
					}else if(status == "W"){
						if(resultList[i].week == resultList[j].week){
							if(resultList[i].time == resultList[j].time){
								alert("동일한 시간이 존재합니다.");
								return;
							}
						}
					}else if(status == "M"){
						if(resultList[i].day == resultList[j].day){
							if(resultList[i].time == resultList[j].time){
								alert("동일한 시간이 존재합니다.");
								return;
							}
						}
					}
				}
			}
		}
	}
	
	var postData = {
			status : status,
			resultList : JSON.stringify(resultList),
			pwd : $("#pwd").val(),
			email : $("#com_date").val()
			
		};
	
	$.ajax({
		type: "POST",
		url: "/setting/updateBatchSchedule",
		//async : false,
		data : postData,
		dataType: "json",
	    success: function (resultMap) {
	    	if(resultMap.resultCode == 0){
	    		alert("메일 설정이 변경되었습니다.");
	    		location.reload();
	    	}else{
	    		alert("설정 수정을 실패하였습니다. \n 관리자에게 문의해주세요.");
	    	}
	    },
	    error: function (request, status, error) {
	    	alert("설정 수정을 실패하였습니다.");
        	console.log("ERROR : ", error);
	    }
	});
});


/* 메일 주기 추가 */
$("#btnScheduleAdd").click(function() {
	++chk_num;
	if(chk_num > 2){
		alert("최대 3개까지 가능합니다.");
		--chk_num;
		return;
	}else{
		var content = "<tr class=\"addTr\"  id=\"createTr_"+chk_num+"\">"+
		"<td>"+
		"<select id=\"selectWeek_"+chk_num+"\" style=\"margin-right : 3px; margin-left : 49px; display: none;\" name=\"selectWeek_"+chk_num+"\" onchange=\"changeSelectDetail('Week')\">"+
			"<option value=\"2\" >월요일</option>"+
			"<option value=\"3\" >화요일</option>"+
			"<option value=\"4\" >수요일</option>"+
			"<option value=\"5\" >목요일</option>"+
			"<option value=\"6\" >금요일</option>"+
			"<option value=\"7\" >토요일</option>"+
			"<option value=\"1\" >일요일</option>"+
		"</select>"+
		"<select id=\"selectDay_"+chk_num+"\" style=\"margin-right : 3px; display: none;\" name=\"selectDay_"+chk_num+"\" onchange=\"changeSelectDetail('Day')\">"+
		"</select>"+
		"<select id=\"selectTime_"+chk_num+"\" style=\"margin-right : 3px; \" name=\"selectTime_"+chk_num+"\" onchange=\"changeSelectDetail('Time')\">"+
		"</select>"+
		"<button type='button' name='button' style='cursor:pointer; color:#1973ba;' onclick='fnCheckRemove("+chk_num+");'>remove</button>"+
		"</td>"+
		"</tr>";

		$(".insertStatusData").append(content);
		
		if(chk_num != 0){
		addSelectStatus(chk_num);
		}else{
		changeSelectStatus();
		}
		
		addSelectOption(chk_num);
		
		$("#selectStatusTh").attr("rowspan", chk_num+1);
	}
	

});

/* 추가 주기 저장 */
function fnCheckRemove(num) {

	var resultWeek = "";
	var resultDay = "";
	var resultTime = "";
	
	for(i=1; i< chk_num+1; i++){
		
		
		if(i != num){
				resultWeek = $("#selectWeek_"+i+" option:selected").val();
				resultDay = $("#selectDay_"+i+" option:selected").val();
				resultTime = $("#selectTime_"+i+" option:selected").val();
		}
	}
	
	--chk_num;
	$(".addTr").remove();
	
	for(k=1 ; k < chk_num+1 ; k++){
		
		var content = "<tr class=\"addTr\"  id=\"createTr_"+k+"\">"+
		"<td style=\"padding: 5px 5px 0 5px;\">"+
		"<select id=\"selectWeek_"+k+"\" style=\"margin-right : 3px; margin-left : 49px; display: none;\" name=\"selectWeek_"+k+"\" onchange=\"changeSelectDetail('Week')\">"+
			"<option value=\"2\" >월요일</option>"+
			"<option value=\"3\" >화요일</option>"+
			"<option value=\"4\" >수요일</option>"+
			"<option value=\"5\" >목요일</option>"+
			"<option value=\"6\" >금요일</option>"+
			"<option value=\"7\" >토요일</option>"+
			"<option value=\"1\" >일요일</option>"+
		"</select>"+
		"<select id=\"selectDay_"+k+"\" style=\"margin-right : 3px; display: none;\" name=\"selectDay_"+k+"\" onchange=\"changeSelectDetail('Day')\">"+
		"</select>"+
		"<select id=\"selectTime_"+k+"\" style=\"margin-right : 3px; \" name=\"selectTime_"+k+"\" onchange=\"changeSelectDetail('Time')\">"+
		"</select>"+
		"<button type='button' name='button' style='cursor:pointer; color:#1973ba;' onclick='fnCheckRemove("+k+");'>remove</button>"+
		"</td>"+
		"</tr>";
		
		$(".insertStatusData").append(content);
		addSelectStatus(k);
		addSelectOption(k);
		
		$("#selectWeek_"+k).val(resultWeek).prop("selected", true);
		$("#selectDay_"+k).val(resultDay).prop("selected", true);
		$("#selectTime_"+k).val(resultTime).prop("selected", true);
	}
	changeSelectDetail("remove");
}

function changeApprovalStatus(id){
	var mngr_val = $("#approval_mngr_list"+id).val();
	$("#approvalMngrGrid").setCell(id, 'STATUS', mngr_val);
}

/* 메일 월,주,일 선택 변경 */
function changeSelectStatus(){
	chk_num = 0
	$(".addTr").remove();
	var status = $("#selectStatus_0 option:selected").val();

	var beforeresult = $("#beforeScheduleResult").val()
	
	if(beforeresult == " "){
		$("#scheduleResult").html("아직 등록된 주기가 없습니다.");
	}else{
		$("#scheduleResult").html(beforeresult);
	}
	
	if(status == "D"){
		$("#selectWeek_0").hide();
		$("#selectDay_0").hide();
	}else if(status == "W"){
		$("#selectWeek_0").show();
		$("#selectDay_0").hide();
	}else if(status == "M"){
		$("#selectWeek_0").hide();
		$("#selectDay_0").show();
	}
	
	addSelectOption(0);
	$("#selectWeek_0").val(1).prop("selected", true);
	$("#selectDay_0").val(1).prop("selected", true);
	$("#selectTime_0").val(1).prop("selected", true);
}

/* 데이터 */
function changeSelectDetail(chk){
	
	var scheduleResult = $("#beforeScheduleResult").val();
	
	var status = $("#selectStatus_0 option:selected").val();
	
	scheduleResult += " -> <label style=\"color:blue; \">";
	
	var resultWeek = [];
	var resultDay = [];
	var resultTime = [];
	
	for(i=0 ; i < chk_num+1 ; i++){
		resultWeek.push($("#selectWeek_"+i+" option:selected").val());
		resultDay.push($("#selectDay_"+i+" option:selected").val());
		resultTime.push($("#selectTime_"+i+" option:selected").val());
	}
	
	
	switch(status) {
    case "D": scheduleResult += "매일 "; break;
    case "W": scheduleResult += "매주 "; break;
    case "M": scheduleResult += "매달 "; break;
}
	
	for(i=0 ; i < chk_num+1 ; i++){
		
		if(status == "D"){

			scheduleResult += resultTime[i] + "시"
    		if(i < resultTime.length-1 ) scheduleResult += " / "
    		
		}else if(status == "W"){
    		
    		switch(resultWeek[i]) {
    		 	case "2": scheduleResult += "월요일 "; break;
			    case "3": scheduleResult += "화요일 "; break;
			    case "4": scheduleResult += "수요일 "; break;
			    case "5": scheduleResult += "목요일 "; break;
			    case "6": scheduleResult += "금요일 "; break;
			    case "7": scheduleResult += "토요일 "; break;
			    case "1": scheduleResult += "일요일 "; break;
			}
    		
    		scheduleResult += resultTime[i] +"시"
   			if(i < resultDay.length-1 ) scheduleResult += " / "
    		
		}else if(status == "M"){
			
			scheduleResult += resultDay[i] + "일 "
			scheduleResult += resultTime[i] + "시"
			if(i < resultDay.length-1 ) scheduleResult += " / "
		
		}
	}
	
	$("#scheduleResult").html(scheduleResult+"</label>");
	
}

function addSelectStatus(num){
	var status = $("#selectStatus_0 option:selected").val();
	
	if(status == "D"){
		$("#selectWeek_"+num).hide();
		$("#selectDay_"+num).hide();
		$("#selectTime__"+num).css("margin-left", "49px");
	}else if(status == "W"){
		$("#selectWeek_"+num).show();
		$("#selectDay_"+num).hide();
		$("#selectDay_"+num).css("margin-left", "49px");
	}else if(status == "M"){
		$("#selectWeek_"+num).hide();
		$("#selectDay_"+num).show();
		$("#selectDay_"+num).css("margin-left", "49px;");
	}
}


/* ----------------------------------- 메일 스케줄 끝 ----------------------------------- */
/* ----------------------------------- 필수 결재자 시작 ----------------------------------- */
function approvalMngrGrid(){
	$("#approvalMngrGrid").jqGrid({
		//url: 'data.json',
		datatype: "local",
	   	mtype : "POST",
	   	ajaxGridOptions : {
			type    : "POST",
			async   : true
		},
		colNames:['구분', '구분', '이름', '사번', '직책', '소속', ' ', ''],
		colModel: [
			{ index: 'NAME', 			name: 'NAME',	width: 50, 	align: 'center'},
			{ index: 'STATUS', 			name: 'STATUS',			width: 50, 	align: 'center', hidden:true},
			{ index: 'USER_NAME', 		name: 'USER_NAME',		width: 50, 	align: 'center'},
			{ index: 'USER_NO',			name: 'USER_NO',		width: 0, 	hidden:true},
			{ index: 'JIKGUK', 			name: 'JIKGUK',			width: 50, 	align: 'center'},
			{ index: 'COM', 			name: 'COM', 			width: 90, 	align: 'center'},
			{ index: 'IDX', 			name: 'IDX', 			width: 30,  align: 'center', formatter: deletBtn},
			{ index: 'IDX2', 			name: 'IDX2', 			width: 30,  align: 'center', hidden:true}
		],
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: $("#noticeGrid").parent().width(),
		height: 200,
		loadonce: true, // this is just for the demo
	   	autowidth: true,
		shrinkToFit: true,
		rownumbers : false, // 행번호 표시여부
		rownumWidth : 30, // 행번호 열의 너비	
		rowNum:25,
		rowList:[25,50,100],		
		pager: "#approvalMngrGridPager",
		cmTemplate: {sortable: false},
		//jqgrid의 특성상 - rowpos의 이벤트는 onSelectCell, beforeEditCell 다 해주어야 함
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	  	
	  	},
	  	ondblClickRow: function(nRowid, icol, cellcontent, e){
	  	},
	  	onCellSelect: function(rowid,icol,cellcontent,e) { 
	  	},
		loadComplete: function(data) {
			var ids = $("#approvalMngrGrid").getDataIDs() ;
			
			 $.each(ids, function(idx, rowId) {
				 var mngrGridData = $("#approvalMngrGrid").getRowData(rowId) ;
				 
				var approvalMap = '${approvalMap}'.split('[{').join('').split('}]').join('');
				approvalMap = approvalMap.split('}, {');
				
				var html = "<select id=\"approval_mngr_list"+rowId+"\" name=\"approval_mngr_list"+rowId+"\" onchange=\"changeApprovalStatus("+rowId+")\" style=\"width:132px; font-size: 12px; padding-left: 5px;\">";
				 
				for(var a = 0; approvalMap.length > a; a++){
					var row = approvalMap[a].split(', ');
					var status = row[0].split('STATUS=').join('')
					var name = row[1].split('NAME=').join('')
					
					html += "<option value='"+status+"'>"+name+"</option>"; 
				}
				html += "</select>";
				
				$("#approvalMngrGrid").setCell(rowId, 'NAME', html);
				$("#approval_mngr_list"+rowId).val(mngrGridData.STATUS);
				$("#approval_mngr_list"+rowId).val(mngrGridData.STATUS).prop('selected', true);
				 
			 });
		},
	    gridComplete : function() {
	    }
	});
	
	var postData = {};
	$("#approvalMngrGrid").setGridParam({
		url:"<%=request.getContextPath()%>/setting/approvalList", 
		postData : postData, 
		datatype:"json" 
		}).trigger("reloadGrid");
}; 

function deleteGroupApprovalUser(user_no, insa_code, idx){
	
	var postData={user_no:user_no, insa_code:insa_code}; 
	
	if(confirm("결재자에서 제외하시겠습니까?" )){
		$.ajax({
			type: "POST",
			url: "/setting/deleteGroupApprovalUser",
			//async : false,
			data : postData,
			dataType: "json",
		    success: function (resultMap) {
		    	postData = {};
		    	if(resultMap.resultCode == 200){  
		    		$("#teamApprovalrGrid").delRowData(idx);
		    		alert("결재자에서 제외되었습니다.");
		    	}else{
		    		alert("삭제를 실패했습니다. \n관리자에게 문의해주세요.");
		    	}
		    },
		    error: function (request, status, error) {
		    	alert("설정 변경에 실패하였습니다.");
	        	console.log("ERROR : ", error);
		    }
		});
	}
	
}
function selectGroupApprovalUser(idx){
	
	 $("#btnTeamApproval").show();
	
	var gridData = $("#gorupApprovalMngrGrid").getRowData(idx, true) ;
	$("#approvalTeamName").html(gridData.team_name+ " 결재자 등록"); 
	
	$("#teamApprovalrGrid").jqGrid({
		//url: 'data.json',
		datatype: "local",
	   	mtype : "POST",
	   	ajaxGridOptions : {
			type    : "POST",
			async   : true
		},
		colNames:['사번', '사용자명', '인사코드', '직위', '직급', ' '],
		colModel: [ 
			{ index: 'user_no', 		name: 'user_no',	width: 50, 	align: 'center'},
			{ index: 'user_name',		name: 'user_name',	width: 50, 	align: 'center'},
			{ index: 'insa_code',		name: 'insa_code',	width: 50, 	align: 'center', hidden:true},
			{ index: 'jikwee',			name: 'jikwee',		width: 50, 	align: 'center', hidden:true},
			{ index: 'jikguk', 			name: 'jikguk',		width: 50, 	align: 'center'}, 
			{ index: 'IDX', 			name: 'IDX', 		width: 30,  align: 'center', formatter: groupApprvalDeleteBtn}
		],
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: $("#teamApprovalrGrid").parent().width(),
		height: 200,
		loadonce: true, // this is just for the demo
	   	autowidth: true,
		shrinkToFit: true,
		rownumbers : false, // 행번호 표시여부
		rownumWidth : 30, // 행번호 열의 너비	
		rowNum:10,
		rowList:[10,20,30],		
		pager: "#teamApprovalrGridPager",
		cmTemplate: {sortable: false},
		//jqgrid의 특성상 - rowpos의 이벤트는 onSelectCell, beforeEditCell 다 해주어야 함
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	  	
	  	},
	  	ondblClickRow: function(nRowid, icol, cellcontent, e){
	  	},
	  	onCellSelect: function(rowid,icol,cellcontent,e) { 
	  	},
		loadComplete: function(data) {
		},
	    gridComplete : function() {
	    }
	});
	
	var postData = {insa_code : gridData.insa_code}
	$("#update_insa_code").val(gridData.insa_code);  
	$("#teamApprovalrGrid").setGridParam({
		url:"<%=request.getContextPath()%>/setting/selectGroupApprovalUser", 
		postData : postData, 
		datatype:"json" 
		}).trigger("reloadGrid");
}

function deleteApprovalUser(idx){
	$("#approvalMngrGrid").delRowData(idx);
}


function groupApprvalBtn(cellvalue, options, rowObject) {
	return "<button type='button' style='padding-top: 4px; padding-bottom:4px;' class='gridSubSelBtn' name='gridSubSelBtn' onclick=\"selectGroupApprovalUser("+options.rowId+");\">수정</button>";
}
    
function groupApprvalDeleteBtn(cellvalue, options, rowObject) {   
	return "<button type='button' style='padding-top: 4px; padding-bottom:4px;' class='gridSubSelBtn' name='gridSubSelBtn' onclick=\"deleteGroupApprovalUser('"+rowObject.user_no+"', '"+rowObject.insa_code+"', "+options.rowId+");\">삭제</button>";
} 
 
function deletBtn(cellvalue, options, rowObject) {
	return "<button type='button' style='padding-top: 4px; padding-bottom:4px;' class='gridSubSelBtn' name='gridSubSelBtn' onclick=\"deleteApprovalUser("+options.rowId+");\">삭제</button>";
}
/* 결제 데이터 저장 */
$("#btnApprovalMngrListSave").click(function() {
	var rowData = $("#approvalMngrGrid").getRowData();
	var saveChk = confirm("아래의 내용으로 적용하시겠습니까?");  
	var postData = {approvalList : JSON.stringify(rowData)}
	 
	if(saveChk){
		
// 		var approvalList = [];
		
// 		for(var a=0 ; a<rowData.length ; a++){
// 			var data = {
// 						IDX2 : rowData[a].IDX2,
// 						USER_NO : rowData[a].USER_NO,
// 						STATUS : $(),
// 						COM:
// 					};

// 		}
		
		$.ajax({
			type: "POST",
			url: "/setting/updateBatchApproval",
			//async : false,
			data : postData,
			dataType: "json",
		    success: function (resultMap) {
		    	if(resultMap.resultCode == 0){
		    		alert("합의자/통보자 설정이 변경되었습니다.");
// 		    		location.reload();
		    	}else{
		    		alert("설정 수정을 실패하였습니다. \n 관리자에게 문의해주세요.");
		    	}
		    },
		    error: function (request, status, error) {
		    	alert("설정 수정을 실패하였습니다.");
	        	console.log("ERROR : ", error);
		    }
		});
	}
});

/* ----------------------------------- 필수 결재자 끝 ----------------------------------- */
/* ----------------------------------- 팀별 결재자 시작 ----------------------------------- */
function groupApprovalMngrGrid(){
	$("#gorupApprovalMngrGrid").jqGrid({
		//url: 'data.json',
		datatype: "local",
	   	mtype : "POST",
	   	ajaxGridOptions : {
			type    : "POST",
			async   : true
		},
		colNames:['insa_code', '팀명', '팀명', '결재자 수', ' '],
		colModel: [
			{ index: 'insa_code', 			name: 'insa_code',		width: 50, 	align: 'center', hidden:true},
			{ index: 'team_name',			name: 'team_name',		width: 50, 	align: 'center'},
			{ index: 'fullname',			name: 'fullname',		width: 50, 	align: 'center', hidden:true},
			{ index: 'user_cnt', 			name: 'user_cnt',		width: 50, 	align: 'center'}, 
			{ index: 'IDX', 				name: 'IDX', 			width: 30,  align: 'center', formatter: groupApprvalBtn}
		],
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: $("#noticeGrid").parent().width(),
		height: 200,
		loadonce: true, // this is just for the demo
	   	autowidth: true,
		shrinkToFit: true,
		rownumbers : false, // 행번호 표시여부
		rownumWidth : 30, // 행번호 열의 너비	
		rowNum:25,
		rowList:[25,50,100],		
		pager: "#gorupApprovalMngrGridPager",
		cmTemplate: {sortable: false},
		//jqgrid의 특성상 - rowpos의 이벤트는 onSelectCell, beforeEditCell 다 해주어야 함
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	  	
	  	},
	  	ondblClickRow: function(nRowid, icol, cellcontent, e){
	  	},
	  	onCellSelect: function(rowid,icol,cellcontent,e) { 
	  	},
		loadComplete: function(data) {
		},
	    gridComplete : function() {
	    }
	});
	
	var postData = {};
	$("#gorupApprovalMngrGrid").setGridParam({
		url:"<%=request.getContextPath()%>/setting/groupApprovalList", 
		postData : postData, 
		datatype:"json" 
		}).trigger("reloadGrid");
}; 
/* ----------------------------------- 팀별 결재자 끝 ----------------------------------- */
/* ----------------------------------- 내용 관리 사직 ----------------------------------- */
function conGrid(){
	$("#conGrid").jqGrid({
		//url: 'data.json',
		datatype: "local",
	   	mtype : "POST",
	   	ajaxGridOptions : {
			type    : "POST",
			async   : true
		},
		colNames:['구분', '구분flag', '제목', '내용', '내용', '상태', '', ''],
		colModel: [
			{ index: 'FLAG_CON',	name: 'FLAG_CON',		width: 20, 	align: 'center'},
			{ index: 'FLAG',		name: 'FLAG',			width: 20, 	align: 'center', hidden:true},
			{ index: 'NAME', 		name: 'NAME',			width: 30, 	align: 'center'},
			{ index: 'CON', 		name: 'CON',			width: 40, 	align: 'center', hidden:true},
			{ index: 'CON_COPY', 	name: 'CON_COPY',		width: 40, 	align: 'center', formatter: textlength},
			{ index: 'FLAG_STATUS', name: 'FLAG_STATUS',	width: 0, 	align: 'center', hidden:true},
			{ index: 'IDX', 		name: 'IDX',			width: 0, 	align: 'center', hidden:true},
			{ index: 'BUTTON', 		name: 'BUTTON', 		width: 15,  align: 'center'}
		], 
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: $("#noticeGrid").parent().width(),
		height: 200,
		loadonce: true, // this is just for the demo
	   	autowidth: true,
		shrinkToFit: true,
		rownumbers : false, // 행번호 표시여부
		rownumWidth : 30, // 행번호 열의 너비	
		rowNum:25,
		rowList:[25,50,100],		
		pager: "#conGridPager",
		cmTemplate: {sortable: false},
		//jqgrid의 특성상 - rowpos의 이벤트는 onSelectCell, beforeEditCell 다 해주어야 함
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	  	
	  	},
	  	ondblClickRow: function(nRowid, icol, cellcontent, e){
	  	},
	  	onCellSelect: function(rowid,icol,cellcontent,e) { 
	  	},
		loadComplete: function(data) {
			var ids = $("#conGrid").getDataIDs() ;
			$.each(ids, function(idx, rowId) {
				var gridData = $("#conGrid").getRowData(rowId, true) ;
           		$("#conGrid").setCell(rowId, 'CON_COPY', gridData.CON);
           		$("#conGrid").setCell(rowId, 'BUTTON', "<button type='button' class='gridSubSelConBtn' name='gridSubSelConBtn' style='margin-left: 7px'>선택</button>");
           		$(".gridSubSelConBtn").on("click", function(e) {
    		  		e.stopPropagation();
    				
    				$("#conGrid").setSelection(event.target.parentElement.parentElement.id);
    				
    				var offset = $(this).parent().offset();
    				$("#conTaskWindow").css("left", (offset.left - $("#conTaskWindow").width()) + 30 + "px");
    				// $("#taskWindow").css("left", (offset.left - $("#taskWindow").width() + $(this).parent().width()) + "px");
    				$("#conTaskWindow").css("top", (offset.top + $(this).height() - 3)+ "px"); 

    				var bottomLimit = $(".left_box2").offset().top + $(".left_box2").height();
    				var taskBottom = Number($("#conTaskWindow").css("top").replace("px","")) + $("#conTaskWindow").height();
    				
    				$("#conTaskWindow").show();
    			}); 

			});
		},
	    gridComplete : function() {
	    }
	});
	
	var postData = {};
	$("#conGrid").setGridParam({
		url:"<%=request.getContextPath()%>/setting/conDataList", 
		postData : postData, 
		datatype:"json" 
		}).trigger("reloadGrid");
};

function backUpGrid(){
	
	var data = [
		{Title: "정책", VALUE: "1"},
		{Title: "검출 결과", VALUE: "2"},
		{Title: "결재", VALUE: "3"},
	];
	
	$("#backUpGrid").jqGrid({
		//url: 'data.json',
		datatype: "local",
	   	mtype : "POST",
	   	data: data,
	   	ajaxGridOptions : {
			type    : "POST",
			async   : true
		},
		colNames:['', '분류','value'],
		colModel: [
			{ index: 'CHKBOX',	name: 'CHKBOX',	width: 5, 	align: 'center', formatter: createRadio},
			{ index: 'Title',	name: 'Title',	width: 100, 	align: 'center'},
			{ index: 'VALUE',	name: 'VALUE',	width: 100, 	align: 'center', hidden: true}
		], 
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: $("#backUpGrid").parent().width(),
		height: 200,
		loadonce: true, // this is just for the demo
	   	autowidth: true,
		shrinkToFit: true,
		rownumbers : false, // 행번호 표시여부
		rownumWidth : 100, // 행번호 열의 너비	
		rowNum:100,
		rowList:[100,200,500],		
		pager: "#backUpPager",
		cmTemplate: {sortable: false},
		//jqgrid의 특성상 - rowpos의 이벤트는 onSelectCell, beforeEditCell 다 해주어야 함
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	  	
	  	},
	  	ondblClickRow: function(nRowid, icol, cellcontent, e){
	  	},
	  	onCellSelect: function(rowid,icol,cellcontent,e) { 
	  	},
		loadComplete: function(data) {
		},
	    gridComplete : function() {
	    }
	});
	
}

function rollBackGrid(){
	
	$("#rollBackGrid").jqGrid({
		//url: 'data.json',
		datatype: "local",
	   	mtype : "POST",
	   	ajaxGridOptions : {
			type    : "POST",
			async   : true
		},
		colNames:['', 'IDX', '파일명'],
		colModel: [
			{ index: 'CHKBOX',	name: 'CHKBOX',	width: 5, 	align: 'center', formatter: createRollbackRadio},
			{ index: 'IDX',		name: 'IDX',	width: 100, 	align: 'center', hidden: true},
			{ index: 'NAME',	name: 'NAME',	width: 100, 	align: 'center'}
		], 
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: $("#rollBackGrid").parent().width(),
		height: 200,
		loadonce: true, // this is just for the demo
	   	autowidth: true,
		shrinkToFit: true,
		rownumbers : false, // 행번호 표시여부
		rownumWidth : 100, // 행번호 열의 너비	
		rowNum:100,
		rowList:[100,200,500],		
		pager: "#rollBackPager",
		cmTemplate: {sortable: false},
		//jqgrid의 특성상 - rowpos의 이벤트는 onSelectCell, beforeEditCell 다 해주어야 함
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	  	
	  	},
	  	ondblClickRow: function(nRowid, icol, cellcontent, e){
	  	},
	  	onCellSelect: function(rowid,icol,cellcontent,e) { 
	  	},
		loadComplete: function(data) {
		},
	    gridComplete : function() {
	    }
	});
	
	var postData = {};
    $("#rollBackGrid").setGridParam({
        url:"<%=request.getContextPath()%>/setting/rollBackList", 
        postData : postData, 
        datatype:"json" 
    }).trigger("reloadGrid");
}

$("#conDataUpdated").click(function(){
	
	$("#conTaskWindow").hide();
	
	var row = $("#conGrid").getGridParam("selrow");
	var girdData = $("#conGrid").getRowData(row);
	var flag_status = girdData.FLAG_STATUS;
	$("#conDetailStatus").text(girdData.FLAG_CON);
	$("#conDetailNm").val(girdData.NAME);
	$("#conDetailIdx").val(girdData.IDX);
	$("#conDetailContent").val(girdData.CON);
	
	if(flag_status.indexOf("approval_alert") != -1){
		$("#templateHeader").html("안내 문구 수정")
		$("#version_nm").hide();
	}else{
		$("#templateHeader").html("전송 내용 수정")
		$("#version_nm").show();  
	}
	
	var version_nm = '${picSession.version.version_nm}';
	var conDetailNm = $("#conDetailNm");
	
	conDetailNm.prop('size', Math.max(1, 98 - (version_nm.length+2)));
	
	$("#btnConDetailPop").show();
	
}); 

$("#conDataDel").click(function(){
	$("#conTaskWindow").hide();
	
	var row = $("#conGrid").getGridParam("selrow");
	var girdData = $("#conGrid").getRowData(row);
	
	var postData = {
			idx : girdData.IDX,
			conDetailNm : null,
			conDetailContent : null,
			flag : 'N'
		};
	
	if(confirm("내용을 삭제하시면 " +girdData.FLAG_CON + "(은/는) 더이상 전송되지 않습니다. \n삭제하시겠습니까?" )){
		$.ajax({
			type: "POST",
			url: "/setting/ConListUpdate",
			//async : false,
			data : postData,
			dataType: "json",
		    success: function (resultMap) {
		    	postData = {};
		    	if(resultMap.resultCode == 0){
		    		$("#conGrid").setGridParam({
		    			url:"<%=request.getContextPath()%>/setting/conDataList", 
		    			postData : postData, 
		    			datatype:"json" 
		    			}).trigger("reloadGrid");
		    		
		    		alert("내용이 삭제되었습니다.");
		    	}else{
		    		alert("삭제를 실패했습니다. \n관리자에게 문의해주세요.");
		    	}
		    },
		    error: function (request, status, error) {
		    	alert("설정 변경에 실패하였습니다.");
	        	console.log("ERROR : ", error);
		    }
		});
	}
	
});

$("#btnConDetailPopUpdate").click(function(){
	
	var conDetailNm = $("#conDetailNm").val();
	var conDetailIdx = $("#conDetailIdx").val();
	var conDetailContent = $("#conDetailContent").val();
	
	if(conDetailNm == null || conDetailNm == ""){
		alert("메일 제목을 입력해주세요.");
		$("#conDetailNm").focus();
		return;
	}
	if(conDetailContent == null || conDetailContent == ""){
		alert("메일 내용을 입력해주세요.");
		$("#conDetailContent").focus();
		return;
	}
	
	var postData = {
			idx : conDetailIdx,
			conDetailNm : conDetailNm,
			conDetailContent : conDetailContent,
			status : null,
			flag : 'Y'
		};
	
	if(confirm("내용을 변경하시겠습니까?" )){
		$.ajax({
			type: "POST",
			url: "/setting/ConListUpdate",
			//async : false,
			data : postData,
			dataType: "json",
		    success: function (resultMap) {
		    	postData = {};
		    	if(resultMap.resultCode == 0){
		    		$("#conGrid").setGridParam({
		    			url:"<%=request.getContextPath()%>/setting/conDataList", 
		    			postData : postData, 
		    			datatype:"json" 
		    			}).trigger("reloadGrid");
		    		
		    		alert("내용이 변경되었습니다.");
		    		$("#btnConDetailPop").hide();
		    	}else{
		    		alert("수정에 실패했습니다. \n관리자에게 문의해주세요.");
		    	}
		    },
		    error: function (request, status, error) {
		    	alert("설정 변경에 실패하였습니다.");
	        	console.log("ERROR : ", error);
		    }
		});
	}
});
/* ----------------------------------- 내용 관리 끝 ----------------------------------- */
/* ----------------------------------- 결재 라인 시작 ----------------------------------- */
function approvalGrid(){
	
	var addDataStatus = true;

	if($("#approvalMngrStatus").val() != null || $("#approvalMngrStatus").val() == 'Y'){
		addDataStatus = false;
	}
		
		$("#approvalGrid").jqGrid({
			//url: 'data.json',
			datatype: "local",
		   	mtype : "POST",
		   	ajaxGridOptions : {
				type    : "POST",
				async   : true
			},
			colNames:['', '명칭', '','삭제가능 여부', '사용 여부', '',' '],
			colModel: [
				{ index: 'IDX', 			name: 'IDX',			width: 0, 	align: 'center', hidden:true},
				{ index: 'NAME_COPY', 		name: 'NAME_COPY',		width: 40, 	align: 'center'},
				{ index: 'NAME', 			name: 'NAME',			width: 0, 	align: 'center', hidden:true},
				{ index: 'DELETE', 			name: 'DELETE',			width: 0, 	align: 'center', hidden:true},
				{ index: 'ADD_DATA', 		name: 'ADD_DATA',		width: 20, 	align: 'center', hidden:addDataStatus, formatter:chooseType},
				{ index: 'ADD_DATA_COPY', 	name: 'ADD_DATA_COPY',	width: 20, 	align: 'center', hidden:true},
				{ index: 'BUTTON', 			name: 'BUTTON', 		width: 10,  align: 'center'}
			],
			viewrecords: true, // show the current page, data rang and total records on the toolbar
			width: $("#noticeGrid").parent().width(),
			height: 200,
			loadonce: true, // this is just for the demo
		   	autowidth: true,
			shrinkToFit: true,
			rownumbers : false, // 행번호 표시여부
			rownumWidth : 30, // 행번호 열의 너비	
			rowNum:25,
			rowList:[25,50,100],		
			pager: "#approvalGridPager",
			cmTemplate: {sortable: false},
			//jqgrid의 특성상 - rowpos의 이벤트는 onSelectCell, beforeEditCell 다 해주어야 함
		  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	  	
		  	},
		  	ondblClickRow: function(nRowid, icol, cellcontent, e){
		  	},
		  	onCellSelect: function(rowid,icol,cellcontent,e) { 
		  	},
			loadComplete: function(data) {
				var ids = $("#approvalGrid").getDataIDs() ;
	            $.each(ids, function(idx, rowId) { 
	            	
	            	var gridData = $("#approvalGrid").getRowData(rowId, true) ;
	                 $("#approvalGrid").setCell(rowId, 'NAME_COPY', gridData.NAME);
	            	
	            	$("#approvalGrid").setCell(rowId, 'BUTTON', "<button type='button' class='gridSubSelApprovalBtn' name='gridSubSelBtn' style='margin-left: 7px'>선택</button>");
	    			$(".gridSubSelApprovalBtn").on("click", function(e) {
	    		  		e.stopPropagation();
	    				
	    				$("#approvalGrid").setSelection(event.target.parentElement.parentElement.id);
	    				
	    				var offset = $(this).parent().offset();
	    				$("#nameTaskWindow").css("left", (offset.left - $("#nameTaskWindow").width()) + 30 + "px");
	    				// $("#taskWindow").css("left", (offset.left - $("#taskWindow").width() + $(this).parent().width()) + "px");
	    				$("#nameTaskWindow").css("top", (offset.top + $(this).height() - 3)+ "px"); 

	    				var bottomLimit = $(".left_box2").offset().top + $(".left_box2").height();
	    				var taskBottom = Number($("#nameTaskWindow").css("top").replace("px","")) + $("#nameTaskWindow").height();
	    				
	    				$("#nameGridStatus").val("approval");
	    				$("#nameTaskWindow").show();
	    			}); 
	            });
			},
		    gridComplete : function() {
		    }
		});
		
		var postData = {status : "approval"};
		$("#approvalGrid").setGridParam({
			url:"<%=request.getContextPath()%>/setting/nameList", 
			postData : postData, 
			datatype:"json" 
			}).trigger("reloadGrid");
	};
	
	var createType = function(cellvalue, options, rowObject) {
		return cellvalue != null ? cellvalue : "<span style=\"color:red\">미설정</span>"; 
	};
	

	function chooseType (cellvalue, options, rowObject) {
		var retrunVal = "";
		if(rowObject.ADD_DATA == "Y"){
			retrunVal = "사용";
		}else{
			retrunVal = "미사용";
		}
		return retrunVal 
	};
/* ----------------------------------- 결재 라인 끝 ----------------------------------- */
/* ----------------------------------- 당당자 관리 시작 ----------------------------------- */
 function mngrGird(){
		$("#mngrGird").jqGrid({
			//url: 'data.json',
			datatype: "local",
		   	mtype : "POST",
		   	ajaxGridOptions : {
				type    : "POST",
				async   : true
			},
			colNames:['', '명칭', '','삭제가능 여부', ' ','', ' '],
			colModel: [
				{ index: 'IDX', 			name: 'IDX',			width: 0, 	align: 'center', hidden:true},
				{ index: 'NAME_COPY', 		name: 'NAME_COPY',		width: 60, 	align: 'center'},
				{ index: 'NAME', 			name: 'NAME',			width: 0, 	align: 'center', hidden:true},
				{ index: 'DELETE', 			name: 'DELETE',			width: 0, 	align: 'center', hidden:true},
				{ index: 'ADD_DATA', 		name: 'ADD_DATA',		width: 20, 	align: 'center', hidden:true},
				{ index: 'ADD_DATA_COPY', 	name: 'ADD_DATA_COPY',	width: 20, 	align: 'center', hidden:true},
				{ index: 'BUTTON', 			name: 'BUTTON', 		width: 10,  align: 'center'}
			],
			viewrecords: true, // show the current page, data rang and total records on the toolbar
			width: $("#noticeGrid").parent().width(),
			height: 200,
			loadonce: true, // this is just for the demo
		   	autowidth: true,
			shrinkToFit: true,
			rownumbers : false, // 행번호 표시여부
			rownumWidth : 30, // 행번호 열의 너비	
			rowNum:25,
			rowList:[25,50,100],		
			pager: "#mngrGirdPager",
			cmTemplate: {sortable: false},
			//jqgrid의 특성상 - rowpos의 이벤트는 onSelectCell, beforeEditCell 다 해주어야 함
		  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	  	
		  	},
		  	ondblClickRow: function(nRowid, icol, cellcontent, e){
		  	},
		  	onCellSelect: function(rowid,icol,cellcontent,e) { 
		  	},
		  	loadComplete: function(data) {
				var ids = $("#mngrGird").getDataIDs() ;
	            $.each(ids, function(idx, rowId) { 
	            	
	            	 var gridData = $("#mngrGird").getRowData(rowId, true) ;
	                 $("#mngrGird").setCell(rowId, 'NAME_COPY', gridData.NAME);
	                 $("#mngrGird").setCell(rowId, 'ADD_DATA_COPY', gridData.ADD_DATA);
	            	
	            	$("#mngrGird").setCell(rowId, 'BUTTON', "<button type='button' class='gridSubSelServerBtn' name='gridSubSelServerBtn' style='margin-left: 7px'>선택</button>");
	    			$(".gridSubSelServerBtn").on("click", function(e) {
	    		  		e.stopPropagation();
	    				
	    				$("#mngrGird").setSelection(event.target.parentElement.parentElement.id);
	    				
	    				var offset = $(this).parent().offset();
	    				$("#nameTaskWindow").css("left", (offset.left - $("#nameTaskWindow").width()) + 30 + "px");
	    				// $("#taskWindow").css("left", (offset.left - $("#taskWindow").width() + $(this).parent().width()) + "px");
	    				$("#nameTaskWindow").css("top", (offset.top + $(this).height() - 3)+ "px"); 

	    				var bottomLimit = $(".left_box2").offset().top + $(".left_box2").height();
	    				var taskBottom = Number($("#nameTaskWindow").css("top").replace("px","")) + $("#nameTaskWindow").height();
	    				
	    				$("#nameGridStatus").val("server");
	    				$("#nameTaskWindow").show();
	    			}); 
	            });
			},
		    gridComplete : function() {
		    }
		});
		
		var postData = {status : "server"};
		$("#mngrGird").setGridParam({
			url:"<%=request.getContextPath()%>/setting/nameList", 
			postData : postData, 
			datatype:"json" 
			}).trigger("reloadGrid");
	};
	
/* ----------------------------------- 당당자 관리 끝 ----------------------------------- */
/* ----------------------------------- 개인정보 저장 시작 ----------------------------------- */
$("#btnPatternPopUpdate").click(function(){
	
	var patternIDX = $("#patternIDX").val();
	var patternReconNm = $("#patternReconNm").val();
	var patternNameKr = $("#patternNameKr").val();
	var patternNameEr = $("#patternNameEr").val();
	var patternMaskType = $("#patternMaskType").val();
	var patternMaskChk = $("#patternMaskChk").val();
	var patternMaskCnt = $("#patternMaskCnt").val();
	var patternColor = $("#patternColor").val();
	var patternRuleTd = $("#patternRule").val();
	
	if(patternReconNm == null || patternReconNm ==""){
		alert("개인정보 검출 유혐 명이 입력되지 않았습니다.");
		$("#patternReconNm").focus();
		return;
	}
	if(patternRule == null || patternRule ==""){
		alert("개인정보 규칙이 입력되지 않았습니다.");
		$("#patternRule").focus();
		return;
	}
	if(patternNameKr == null || patternNameKr ==""){
		alert("한글 개인정보 유형 명이 입력되지 않았습니다.");
		$("#patternNameKr").focus();
		return;
	}
	if(patternNameEr == null || patternNameEr ==""){
		alert("영문 개인정보 유형 명이 입력되지 않았습니다.");
		$("#patternNameEr").focus();
		return;
	} 
	
	if(patternMaskCnt =="" || patternMaskCnt == null ){
		patternMaskCnt = 0;
	}
	
	var postData = {
			patternIDX : patternIDX,
			patternReconNm : patternReconNm,
			patternNameKr : patternNameKr,
			patternNameEr : patternNameEr,
			patternMaskType : patternMaskType,
			patternMaskChk : patternMaskChk,
			patternMaskCnt : patternMaskCnt,
			patternColor : patternColor,
			patternRuleTd : patternRuleTd
	};
	
	if(confirm("올바른 개인정보 유형이 아닐 경우 검출이 안될 수도 있습니다. \n아래의 내용으로 수정하시겠습니까?")){
		$.ajax({
			type: "POST",
			url: "/setting/updateCustomPattern",
			//async : false,
			data : postData,
			dataType: "json",
		    success: function (resultMap) {
		    	postData = {};
		    	if(resultMap.resultCode == 0){
		    		$("#patternGrid").setGridParam({
		    			url:"<%=request.getContextPath()%>/setting/patternList", 
		    			postData : postData, 
		    			datatype:"json" 
		    			}).trigger("reloadGrid");
		    		
		    		alert("개인정보 유형이 저장되었습니다.");
		    		$("#btnPatternPop").hide();
		    	}else{
		    		alert("설정 저장을 실패하였습니다. \n 관리자에게 문의해주세요.");
		    	}
		    },
		    error: function (request, status, error) {
		    	alert("설정 저장을 실패하였습니다.");
	        	console.log("ERROR : ", error);
		    }
		});
	}
});


function customPatternModel(){
	$("#patternNm").html("개인정보 유형 생성");
	$("#btnPatternPopSave").show();
	$("#btnPatternPopUpdate").hide();
	$("#btnPatternPop").show();
	$("#patternReconNm").show();
	$("#patternReconNm2").hide();
	
	$("#patternReconNm").val("");
	$("#patternReconNm2").val("");
	$("#patternIDX").val("");
	$("#patternNameEr").val("");
	$("#patternNameKr").val("");
	$("#patternMaskType").val(""); // check
	$("#patternMaskChk").val("");
	$("#patternMaskCnt").val("");
	$("#patternColor").val("");
	$("#patternRule").val("");
	$("#bakPatternColor").css("background-color", "");
	 
	$("#btnPatternPopchk").show();
	$("#btnPatternPopSave").css("background", "#e4e4e4");
	$("#btnPatternPopUpdate").css("background", "#e4e4e4");
	$("#btnPatternPopSave").prop("disabled", true);
	$("#btnPatternPopUpdate").prop("disabled", true);
} 

function customPatternSeq(){
	 var allRowId = $("#patternGrid").jqGrid('getDataIDs');     //체크된 row id들을 배열로 반환
	 
	 var idxList = [];
	 var cntList = [];
	 
	 for(s=0; s < allRowId.length ; s++){
		 idxList.push({ idx: $("#patternGrid").getCell(allRowId[s], 'PATTERN_IDX'), cnt: s });
	 }
	 
	 var postData = {idxList : JSON.stringify(idxList)};
	 
	 if(confirm("조회 순서를 변경하시겠습니까?")){
		 $.ajax({
				type: "POST",
				url: "/setting/customPatternChagne",
				//async : false,
				data : postData,
				dataType: "json",
			    success: function (resultMap) {
			    		postData = {};
			    	if(resultMap.resultCode == 0){
			    		$("#patternGrid").setGridParam({
			    			url:"<%=request.getContextPath()%>/setting/patternList", 
			    			postData : postData, 
			    			datatype:"json" 
			    			}).trigger("reloadGrid");
			    		
			    		alert("조회 순서가 변경되었습니다.");
			    		$("#btnPatternPop").hide();
			    	}else{
			    		alert("변경에 실패하였습니다. \n관리자에게 문의해주세요.");
			    	}
			    },
			    error: function (request, status, error) {
			    	alert("변경을 실패하였습니다.");
		        	console.log("ERROR : ", error);
			    }
			});
	 }
}

// 개인정보 유형 생성
$("#btnPatternPopSave").click(function(){
	var patternReconNm = $("#patternReconNm").val();
	var patternNameKr = $("#patternNameKr").val();
	var patternNameEr = $("#patternNameEr").val();
	var patternMaskType = $("#patternMaskType").val();
	var patternMaskChk = $("#patternMaskChk").val();
	var patternMaskCnt = $("#patternMaskCnt").val();
	var patternColor = $("#patternColor").val();
	var patternRuleTd = $("#patternRule").val();
	
	if(patternReconNm == null || patternReconNm == "" ){
		alert("개인정보 검출 유혐 명이 입력되지 않았습니다.");
		$("#patternReconNm").focus();
		return;
	}
	if(patternRule == null || patternRule == ""){
		alert("개인정보 규칙이 입력되지 않았습니다.");
		$("#patternRule").focus();
		return;
	}
	if(patternNameKr == null || patternNameKr == ""){
		alert("한글 개인정보 유형 명이 입력되지 않았습니다.");
		$("#patternNameKr").focus();
		return;
	}
	if(patternNameEr == null || patternNameEr == ""){
		alert("영문 개인정보 유형 명이 입력되지 않았습니다.");
		$("#patternNameEr").focus();
		return;
	}
	
	if(patternMaskCnt =="" || patternMaskCnt == null ){
		patternMaskCnt = 0;
	}
	
	var postData = {
			patternReconNm : patternReconNm,
			patternNameKr : patternNameKr,
			patternNameEr : patternNameEr,
			patternMaskType : patternMaskType,
			patternMaskChk : patternMaskChk,
			patternMaskCnt : patternMaskCnt,
			patternColor : patternColor,
			patternRuleTd : patternRuleTd
	};
	
	$.ajax({
		type: "POST",
		url: "/setting/insertCustomPattern",
		//async : false,
		data : postData,
		dataType: "json",
	    success: function (patternResultMap) {
	    		postData = {};
	    	if(patternResultMap.resultCode == 0){
	    		$("#patternGrid").setGridParam({
	    			url:"<%=request.getContextPath()%>/setting/patternList", 
	    			postData : postData, 
	    			datatype:"json" 
	    			}).trigger("reloadGrid");
	    		
	    		alert("개인정보 유형이 생성되었습니다.");
	    		$("#btnPatternPop").hide();
	    	}else{
	    		alert("설정 수정을 실패하였습니다. \n 관리자에게 문의해주세요.");
	    	}
	    },
	    error: function (request, status, error) {
	    	alert("설정 수정을 실패하였습니다.");
        	console.log("ERROR : ", error);
	    }
	});
});


$("#btnPatternPopchk").click(function(){
	var patternReconNm = $("#patternReconNm").val();
	var patternNameKr = $("#patternNameKr").val();
	var patternNameEr = $("#patternNameEr").val();
	var patternMaskType = $("#patternMaskType").val();
	var patternMaskChk = $("#patternMaskChk").val();
	var patternMaskCnt = $("#patternMaskCnt").val();
	var patternColor = $("#patternColor").val();
	var patternRuleTd = $("#patternRule").val();
	
	if(patternReconNm == null || patternReconNm == "" ){
		alert("개인정보 검출 유혐 명이 입력되지 않았습니다.");
		$("#patternReconNm").focus();
		return;
	}
	if(patternRule == null || patternRule == ""){
		alert("개인정보 규칙이 입력되지 않았습니다.");
		$("#patternRule").focus();
		return;
	}
	if(patternNameKr == null || patternNameKr == ""){
		alert("한글 개인정보 유형 명이 입력되지 않았습니다.");
		$("#patternNameKr").focus();
		return;
	}
	if(patternNameEr == null || patternNameEr == ""){
		alert("영문 개인정보 유형 명이 입력되지 않았습니다.");
		$("#patternNameEr").focus();
		return;
	}
	
	var postData = {
			patternReconNm : patternReconNm,
			patternNameKr : patternNameKr,
			patternNameEr : patternNameEr,
			patternMaskType : patternMaskType,
			patternMaskChk : patternMaskChk,
			patternMaskCnt : patternMaskCnt,
			patternColor : patternColor,
			patternRuleTd : patternRuleTd
	};
	
	$.ajax({
		type: "POST",
		url: "/setting/chkPattern",
		//async : false,
		data : postData,
		dataType: "json",
	    success: function (resultMap) {
	    	if(resultMap.resultCode == 0){
	    		$("#btnPatternPopSave").css("background", "#fff");
	    		$("#btnPatternPopUpdate").css("background", "#fff");
	    		
	    		$("#btnPatternPopchk").hide();
	    		$("#btnPatternPopSave").prop("disabled", false);
	    		$("#btnPatternPopUpdate").prop("disabled", false);
	    	}else{
	    		alert("올바르지 않은 유형입니다.");
	    	}
	    },
	    error: function (request, status, error) {
	    	alert("올바르지 않은 유형입니다.");
        	console.log("ERROR : ", error);
	    }
	});
});

/* ----------------------------------- 결재자, 담당자 관리 시작 ----------------------------------- */
// 명칭 추가
function gridNameAdd(gridStatus){
	if(!isCredateNameGrid){
		alert("진행 중인내역이 있습니다");
		return true;
	}
	
	var Jgrid = null;
	
	if(gridStatus == "approval"){
		Jgrid = $('#approvalGrid');
	}else if(gridStatus == "server"){
		Jgrid = $('#mngrGird');
	}
	
	isCredateNameGrid = false;
	
	var rowId = Jgrid.getGridParam("reccount");
	
	var rowData = {};
	
	 rowData["IDX"] = "";
	 rowData["NAME_COPY"] = "<input type='text' name='datatypeName"+rowId+"' id='datatypeName"+rowId+"' value='' style='width: 100%;' placeholder='명칭을 입력하세요.'>";
	 rowData["NAME"] = "";
	 rowData["DELETE"] = "";
	 rowData["ADD_DATA"] = "";
	 rowData["ADD_DATA_COPY"] = ""; 
	 rowData["BUTTON"] = "<button type='button' class='gridSubSelBtn' name='updateNameListBtn' onclick='careteNameList("+rowId+",\"" +gridStatus+"\")'>저장</button>"
		+"<button type='button' class='gridSubSelBtn' name='cancelNameListBtn' onclick='cancelNameList(\"" + gridStatus + "\")''>취소</button>";
	
	Jgrid.addRowData(rowId+1, rowData, 'first');
}

// 명칭 수정
$("#nameDataUpdated").click(function(){
	
	if(!isCredateNameGrid){
		alert("진행 중인내역이 있습니다");
		return true;
	}
	
	isCredateNameGrid = false;
	$("#nameTaskWindow").hide();
	
	var gridStatus = $("#nameGridStatus").val();
	var Jgrid = null;
	
	if(gridStatus == "approval"){
		Jgrid = $('#approvalGrid');
	}else if(gridStatus == "server"){
		Jgrid = $('#mngrGird');
	}
	
	var row = Jgrid.getGridParam("selrow");
	var data = Jgrid.getRowData(row);
	
	Jgrid.setCell(row, "NAME_COPY", "<input type='text' name='datatypeName"+row+"' id='datatypeName"+row+"' value='"+ data.NAME +"' style='width: 100%;'>");
	Jgrid.setCell(row, "BUTTON", "<button type='button' class='gridSubSelBtn' name='updateNameListBtn' onclick='updateNameList("+row+")'>수정</button>"
			+"<button type='button' class='gridSubSelBtn' name='cancelNameListBtn' onclick='cancelNameList(\"" + gridStatus + "\")'>취소</button>");
	
});

// 명칭 삭제
$("#nameDataDel").click(function(){
	
	$("#nameTaskWindow").hide();
	
	var gridStatus = $("#nameGridStatus").val();
	var Jgrid = null;
	
	if(gridStatus == "approval"){
		Jgrid = $('#approvalGrid');
	}else if(gridStatus == "server"){
		Jgrid = $('#mngrGird');
	}
	
	var row = Jgrid.getGridParam("selrow");
	var girdData = Jgrid.getRowData(row);
	
	if(girdData.DELETE == "N"){
		alert("삭제할 수 없는 명칭입니다. 관리자에게 문의해주세요.");
		return true;
	}
	
	var postData = {
			idx : girdData.IDX,
			status : gridStatus
		};
	
	if(confirm("명칭을 삭제하시겠습니까?")){
		$.ajax({
			type: "POST",
			url: "/setting/nameListDelete",
			//async : false,
			data : postData,
			dataType: "json",
		    success: function (resultMap) {
		    	postData = {status : gridStatus};
		    	if(resultMap.resultCode == 0){
		    		Jgrid.setGridParam({
		    			url:"<%=request.getContextPath()%>/setting/nameList", 
		    			postData : postData, 
		    			datatype:"json" 
		    			}).trigger("reloadGrid");
		    		
		    		alert("명칭이 삭제되었습니다.");
		    		$("#btnPatternPop").hide();
		    	}else{
		    		alert("삭제를 실패했습니다. \n 관리자에게 문의해주세요.");
		    	}
		    },
		    error: function (request, status, error) {
		    	alert("설정 저장을 실패하였습니다.");
	        	console.log("ERROR : ", error);
		    }
		});
	}	
});

function careteNameList(rowID, gridStatus){

	var Jgrid = null;
	
	if(gridStatus == "approval"){
		Jgrid = $('#approvalGrid');
	}else if(gridStatus == "server"){
		Jgrid = $('#mngrGird');
	}
	
	var datatype_name = $("#datatypeName"+rowID).val();
	if (isNull(datatype_name)) {
		alert("명칭을 입력하세요");
		$("#datatypeName"+rowID).focus();
		return;
	}
	
	var postData = {
			name :  $("#datatypeName"+rowID).val(), 
			status : gridStatus
		};
	if (confirm("명칭을 추가하시겠습니까?")) {
		$.ajax({
			type: "POST",
			url: "/setting/nameListCreate",
			async : false,
			data : postData,
			success: function (resultMap) {
		    	postData = {status : gridStatus};
		    	if(resultMap.resultCode == 0){
		    		Jgrid.setGridParam({
		    			url:"<%=request.getContextPath()%>/setting/nameList", 
		    			postData : postData, 
		    			datatype:"json" 
		    			}).trigger("reloadGrid");
		    		
		    		isCredateNameGrid = true;
		    		alert("명칭이 추가되었습니다.");
		    	}else{
		    		alert("설정 저장을 실패하였습니다. \n 관리자에게 문의해주세요.");
		    	}
		    },
		    error: function (request, status, error) {
		    	alert("설정 저장을 실패하였습니다.");
	        	console.log("ERROR : ", error);
		    }
		});
	}
}
function updateNameList(rowID){
	var gridStatus = $("#nameGridStatus").val();
	var Jgrid = null;
	
	if(gridStatus == "approval"){
		Jgrid = $('#approvalGrid');
	}else if(gridStatus == "server"){
		Jgrid = $('#mngrGird');
	}
	
	var girdData = Jgrid.getRowData(rowID);

	var datatype_name = $("#datatypeName"+rowID).val();
	if (isNull(datatype_name)) {
		alert("명칭을 입력하세요");
		$("#datatypeName"+rowID).focus();
		return;
	}

	var postData = {
		idx : girdData.IDX,
		name :  $("#datatypeName"+rowID).val(), 
		status : gridStatus
	};
	
	var message = "명칭을 변경하시겠습니까?";
	if (confirm(message)) {
		$.ajax({
			type: "POST",
			url: "/setting/nameListUpdate",
			async : false,
			data : postData,
			success: function (resultMap) {
		    	postData = {status : gridStatus};
		    	if(resultMap.resultCode == 0){
		    		Jgrid.setGridParam({
		    			url:"<%=request.getContextPath()%>/setting/nameList", 
		    			postData : postData, 
		    			datatype:"json" 
		    			}).trigger("reloadGrid");
		    		
		    		isCredateNameGrid = true;
		    		alert("명칭이 변경되었습니다.");
		    	}else{
		    		alert("설정 저장을 실패하였습니다. \n 관리자에게 문의해주세요.");
		    	}
		    },
		    error: function (request, status, error) {
		    	alert("설정 저장을 실패하였습니다.");
	        	console.log("ERROR : ", error);
		    }
		});
	}
};

function cancelNameList(gridStatus) {
	var Jgrid = null;
	
	if(gridStatus == "approval"){
		Jgrid = $('#approvalGrid');
	}else if(gridStatus == "server"){
		Jgrid = $('#mngrGird');
	}
	
	var postData = {status :gridStatus};
	Jgrid.setGridParam({url:"<%=request.getContextPath()%>/setting/nameList", postData : postData, datatype:"json" }).trigger("reloadGrid");

	isCredateNameGrid = true;
	$("#nameGridStatus").val("");
};

function userListWindows2(info, approvalMap){
	
	var pop_url = "${getContextPath}/popup/userList";
	var id = "targetList"
	var winWidth = 700;
	var winHeight = 570;
	var popupOption= "width="+winWidth+", height="+winHeight + ", left=0, top=0, scrollbars=no, resizable=no, location=no"; 	
	var pop = window.open(pop_url,id,popupOption);
	/* popList.push(pop);
	sessionUpdate(); */
	
	var newForm = document.createElement('form');
	newForm.method='POST';
	newForm.action=pop_url;
	newForm.name='newForm';
	newForm.target=id;
	
	var data = document.createElement('input');
	data.setAttribute('type','hidden');
	data.setAttribute('name','info');
	data.setAttribute('value',info);

	var data2 = document.createElement('input');
	data2.setAttribute('type','hidden');
	data2.setAttribute('name','approvalMap');
	data2.setAttribute('value',approvalMap);
	
	newForm.appendChild(data);
	newForm.appendChild(data2);
	document.body.appendChild(newForm);
	newForm.submit();
	
	document.body.removeChild(newForm);
}

/* ----------------------------------- 결재자, 담당자 관리 끝 ----------------------------------- */
/* ----------------------------------- 세션 설정 시작 ----------------------------------- */
 function sessionTime(){
	
	 var s_hours = "${session.hours}";
		var s_min = "${session.minutes}";
		var s_sec = "${session.seconds}";
		var timeoutInSeconds = "${session.timeoutInSeconds}";
		
		var html = "&nbsp;&nbsp; - ";
		
		if(s_hours != 0){
			if(s_hours < 10)  s_hours = "0"+s_hours;
			html += s_hours+" 시 ";
		}
		if(s_min != 0){
			if(s_min < 10)  s_min = "0"+s_min;
			html += s_min+" 분 ";
		}
		if(s_sec != 0){
			if(s_sec < 10)  s_sec = "0"+s_sec;
			html += s_sec+" 초";
		} 
		
		$("#session_html").html(timeoutInSeconds+" 초");
	
	
	 var sec_content = "";
	 var time_content = "";
	for(i = 0; i < 24; i++){
		time_content += "<option value=\""+i+"\" >";
		if(i < 10){
			time_content += "0"+i+"</option>"
		}else{
			time_content += i+"</option>"
		}
	}
	
	for(i = 0; i < 60; i++){
		sec_content += "<option value=\""+i+"\" >";
		if(i < 10){
			sec_content += "0"+i+"</option>"
		}else{
			sec_content += i+"</option>"
		}
	} 
	 
	$("#sessionHour").append(time_content);
	$("#sessionMin").append(sec_content);
	$("#sessionSec").append(sec_content);
	
	
}
 function userSesstionUpdate(){
	
// 	 var hour = $("#sessionHour").val();
// 	 var min = $("#sessionMin").val();
// 	 var sec = $("#sessionSec").val();

	 var session = $("#session").val();

	 
	 if(session == null){
	 	alert("변경 시간을 입력해주세요.");
		return; 		 
	 }else{
		 var postData = {
				 session : session
		 };
		 if(confirm("세션 타임아웃을 변경하시겠습니까?")){
			 $.ajax({
					type: "POST",
					url: "/setting/userSesstionUpdate",
					//async : false,
					data : postData,
					dataType: "json",
				    success: function (resultMap) {
				    	if(resultMap.resultCode == 0){
				    		alert(resultMap.resultMessage);
				    		location.reload();
				    	}else{
				    		alert("설정 수정을 실패하였습니다. \n 관리자에게 문의해주세요.");
				    	}
				    },
				    error: function (request, status, error) {
				    	alert("설정 수정을 실패하였습니다.");
			        	console.log("ERROR : ", error);
				    }
				});
		}
	 }
}
 
/* ----------------------------------- 세션 설정 끝 ----------------------------------- */
/* ----------------------------------- DB 백업/복원 시작 ----------------------------------- */

$("#tableBackUp").click(function(e){
	var rowid = $("input:radio[name=gridRadio]:checked").data('rowid');
	var tableNames = [];
	
	if(rowid == 1) {
		tableNames.push('pi_datatypes_user');
		tableNames.push('pi_scan_policy');
	}else if(rowid == 2) {
		tableNames.push('pi_find');
		tableNames.push('pi_summary');
		tableNames.push('pi_subpath');
	}else if(rowid == 3) {
		tableNames.push('pi_data_processing');
		tableNames.push('pi_data_processing_charge_group');
		tableNames.push('pi_data_processing_group');
		tableNames.push('pi_data_processing_user');
	}
	
	var postData = {
		value: rowid,
		tables: tableNames
	}
	
    $.ajax({
        type: "POST",
        url: "/setting/backupTables",
        contentType: "application/json",
        data: JSON.stringify(postData),
        success: function(resultMap) {
        	if (resultMap.resultCode == 0) {
                alert("백업에 성공하였습니다.");
                $("#backUpGrid").trigger("reloadGrid");
                var postData = {};
                $("#rollBackGrid").setGridParam({
                    url:"<%=request.getContextPath()%>/setting/rollBackList", 
                    postData : postData, 
                    datatype:"json" 
                }).trigger("reloadGrid");
            } else {
                alert("백업에 실패하였습니다.");
            }
        },
        error: function (request, status, error) {
	    	alert("ERROR");
        	console.log("ERROR : ", error);
	    }
    });
});

$("#rollBackFileBtn").click(function(e){
	var rowid = $("input:radio[name=gridRollBackRadio]:checked").data('rowid');
	var rowData = $("#rollBackGrid").jqGrid('getRowData', rowid);
	
	var idx = rowData.IDX;
    var name = rowData.NAME;
    
    var postData = {
    	fileName : name
    }
    
    $.ajax({
       type: "POST",
       url: "/setting/rollBackTables",
       contentType: "application/json",
       data: JSON.stringify(postData),
	   dataType: "json",
       success: function(resultMap) {
    	   if (resultMap.resultCode == 0) {
    		   alert("복원에 성공하였습니다.");
    		   var postData = {};
    		    $("#rollBackGrid").setGridParam({
    		        url:"<%=request.getContextPath()%>/setting/rollBackList", 
    		        postData : postData, 
    		        datatype:"json" 
    		    }).trigger("reloadGrid");
    	   }else {
	           alert("복원에 실패하였습니다.");
    	   }
       },
       error: function (request, status, error) {
    	   alert("ERROR");
           console.log("ERROR : ", error);
       }
   });
});

function createRadio(cellvalue, options, rowObject) {
	//var value = options['rowId'];
	var value = rowObject['VALUE'];
	var str = '<input type="radio" name="gridRadio" data-rowid="'+options['rowId']+'" value="' + value + '" id="gridRadio_' + value + '">';
    return str;
}

function createRollbackRadio(cellvalue, options, rowObject) {
	//var value = options['rowId'];
	var value = rowObject['IDX'];
	var str = '<input type="radio" name="gridRollBackRadio" data-rowid="'+options['rowId']+'" value="' + value + '" id="gridRollBackRadio' + value + '">';
    return str;
}

$("#tableBackUpSetting").click(function(e){
	var today = new Date();
    
    var start_dtm = setStartdtm(today)
	$('#start_time').html(start_dtm);
    
	$("#maxBackUp").val('');
	$("#btnBackUpSettingPop").show();
});

$("#btnCanclebtnBackUpSettingPop").click(function(e){
	$("#btnBackUpSettingPop").hide();
});

$("#btnBackUpSettingPopClose").click(function(e){
	$("#btnBackUpSettingPop").hide();
});

function setStartdtm(rowData){
	var today = new Date();
	var hours = today.getHours();
	var minutes = today.getMinutes();
	const year = today.getFullYear(); 
	const month = today.getMonth() + 1; 
	const date = today.getDate();

	var start_dtm = rowData.START_DTM;
	var start_ymd = '';
	var start_hour = '';
	var start_minutes = '';
	
	var html = "";
	html += "<input type='date' id='start_ymd' min style='text-align: center; height: 27px;' readonly='readonly' value='"+
	(year+"-"+(month >= 10 ? month : '0' + month) + "-" + (date >= 10 ? date : '0' + date))+"' >&nbsp;";
	
	html += "<select name=\"start_hour\" id=\"start_hour\" >"
	for(var i=0; i<24; i++){
		var hour = (parseInt(i));
		var str_hour = (parseInt(hour) < 10) ? '0'+hour : hour
		html += "<option value=\""+hour+"\" "+(hour == parseInt(hours)? 'selected': '')+">"+(hour < 10 ? "0" + hour : hour)+"</option>"
	}
	html += "</select> : "

	html += "<select name=\"start_minutes\" id=\"start_minutes\" >"
	for(var i=0; i<60; i++){
		var minute = parseInt(i)
		var str_minutes = (parseInt(minutes) < 10) ? '0'+minute : minute
		html += "<option value=\""+minute+"\" "+(minute == parseInt(minutes)? 'selected': '')+">"+(minute < 10 ? "0" + minute : minute)+"</option>"
	}
	html += "</select>"
	
	return html;
}

/* ----------------------------------- DB 백업/복원 끝 ----------------------------------- */

</script>

</body>
</html>