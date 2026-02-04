<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!-- ===================================정탐 팝업=================================== -->

<div id="trueDeletionRegistPopup" class="popup_layer" style="display:none">
    <div class="popup_box" style="width: 1415px; height: 600px; left: 30%; top: 40%; background: #f9f9f9; padding: 10px;">
    <img class="CancleImg" id="btnCancleTrueDeletionRegistPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
        <div class="popup_top" style="background: #f9f9f9;">  
            <h1 id="title_process_true" style="color: #222; box-shadow: none; padding: 0;"></h1>
        </div>
        <div class="popup_content"> 
            <div class="content-box" style="height: 685px; background: #fff; border: 1px solid #c8ced3;">
                <!-- <h2>세부사항</h2>  -->
                <table class="popup_tbl">
                    <colgroup>
                        <col width="130">
                        <col width="*">
                    </colgroup>
                    <tbody>
                        <tr>
                            <th style="text-align: center; border-bottom: 1px solid #cdcdcd;">검출경로</th>
                            <td style="padding:5px; border-bottom: 1px solid #cdcdcd;">
                                <div id="path_exception_div_true" style="width:100%; height:330px; overflow:auto;">
                                <table id="path_exception_true" style="text-align:center; width:100%;">
                                    <tbody>
                                    </tbody>
                                </table>
                                </div>
                            </td>
                        </tr>
                        <tr>
                        	<th style="text-align: center; border-bottom: 1px solid #cdcdcd;" rowspan="2">
                        		판단근거
                        	</th>
                            <td style="padding:10px 0;">
                            	<textarea rows="5" id="path_detail_true" style="width: 100%; padding-left: 5px; font-size: 12px; resize: none;" placeholder="개인정보로 판단한 명확한 근거에 대해서 상세하게 작성 하시기 바랍니다."></textarea>
							</td>
                        </tr>
                        <tr>
                            <td style="padding:0 0 10px 0; border-bottom: 1px solid #cdcdcd; color: #9E9E9E;">
                            	<table class="DeletionRegistT">
                            		<tr> <td>예)</td> <td>- 고객센터 콜 처리 시스템으로 고객콜 인입번호 정보로 서비스를 제공함, 10일 보관 후 자동 삭제</td> </tr>
                            		<tr> <td></td><td>- 타시스템 IF 연동 로그로 3년간 보관 필요 (과금, 망쪽 장비와 연동 자료)</td> </tr>
                            		<tr> <td></td><td>- 증빙파일은 증적으로 남기는 내용이기에 삭제 불가능함</td> </tr>
                            		<tr> <td></td><td>- 해지분리된 고객에 대한 계약담당자 파일로 향후 5년간 보관(향후 원복에 필요)</td> </tr>
                            		<tr> <td></td><td>- 사용자 로그 추적 파일로 6개월 보관 필요</td> </tr>
                            		<tr> <td></td><td>- 포탈 내 각 지역별 커뮤니티에서 업무 관련 내용에 관한 첨부파일로 이력 관리 등 목적으로 3년간 보관</td> </tr>
                            	</table>
							</td>
                        </tr>
                        <tr>
                        	<th style="text-align: center;">조치계획</th>
                        	<td style="padding:5px 0;">
	                        	<div class="content-box" style="height: 90px; background: #fff; margin: 0;">
	                        	
									<select name="popup_content" id="trueDeletionAction" >
										<c:forEach var="trueL" items="${trueList}" varStatus = "status">
											<option value="${trueL.PROCESSING_FLAG}" ${status.count==1?'selected':''} >${trueL.PROCESSING_FLAG_NAME}</option>
										</c:forEach>
									</select>
									<input type="date" name="processing_flag" id="selectDateTrue" value=""  ${trueList[0].DATE_ENABLE==1?'style="text-align: center; margin-right: 10px;display:inline-block"':'style="text-align: center; margin-right: 10px;display:none"'}  readonly="readonly" >
									<div id="addTrueProcessing" style="display: inline-block;">${trueList[0].COMMENT_TITLE}<c:if test="${trueList[0].COMMENT_ENABLE==1}"><input type='text' class='edt_sch' id='comment_content' placeholder='${trueList[0].COMMENT_TEXT}' style='width: 270px; margin-left: 5px; padding-left: 5px'></c:if></div>
									<div class="true_processing_comment">
										<span>${trueList[0].BOTTOM_TEXT}</span>
									</div>
					            </div>
				            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="popup_btn">
            <div class="btn_area" style="padding-right: 0;">
                <button type="button" id="btnTrueDeletionSave">저장</button>
                <button type="button" id="btnTrueDeletionCancel">취소</button>
            </div>
        </div>
    </div>
</div>
<!-- ===================================담당자 변경 팝업=================================== -->
<div id="insertExcelPopup" class="popup_layer" style="display:none">
	<div class="popup_box" style="width: 1000px; height: 540px; padding: 10px; background: #f9f9f9; left: 38%; top: 51%;">
		<img class="CancleImg" id="btnCancleExcelPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 style="color: #222; padding: 0; box-shadow: none;">담당자 변경</h1>
			<p style="position: absolute; top: 14px; left: 113px; font-size: 12px; color: #9E9E9E;">오른쪽 상단의 [단건 다운로드]로 받은 Excel 파일만 업로드할 수 있습니다. </p>
		</div>  
		<div class="popup_content">
			<div class="content-box" style="background: #fff; width: 100%; height:457px; border: 1px solid #c8ced3; overflow: auto;">
				<table class="popup_tbl2" style="width: 100%;">
					<tbody >
						<tr>
							<th>파일 업로드</th>
							<td>
								<button type="button" id="clickimportBtn">파일선택</button>
								<input type="file" id="importExcel" name="importExcel" style="width: 955px; padding-left: 10px; display: none; ">
								<input type="text" id="importExcelNm" style="width: 460px; font-size: 12px; padding: 0 0 0 5px; margin: 0 0 0 7px;" readonly="">
							</td>
						</tr>
					</tbody>
				</table>
				<div class="content-table" style="width: 100%; height: 340px; padding: 0;">
					<table class="popup_tbl" style="width: 100%; overflow: auto;">
						<tbody id="import_targetUserList_excel">
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area" style="padding: 10px 0; margin: 0;" id="insertExcelBtn">
			</div>
		</div>
	</div>
</div>


<!-- ===================================오탐 팝업=================================== -->
<div id="falseDeletionRegistPopup" class="popup_layer" style="display:none">
    <div class="popup_box" style="width: 1415px; height: 600px; left: 30%; top: 40%; background: #f9f9f9; padding: 10px;">
    <img class="CancleImg" id="btnCancleFalseDeletionRegistPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
        <div class="popup_top" style="background: #f9f9f9;">  
            <h1 id="title_process_false" style="color: #222; box-shadow: none; padding: 0;"></h1>
        </div>
        <div class="popup_content"> 
            <div class="content-box" style="height: 710px; background: #fff; border: 1px solid #c8ced3;">
                <!-- <h2>세부사항</h2>  -->
                <table class="popup_tbl">
                    <colgroup>
                        <col width="130">
                        <col width="*">
                    </colgroup>
                    <tbody>
                        <tr>
                            <th style="text-align: center; border-bottom: 1px solid #cdcdcd;">검출경로</th>
                            <td style="padding:5px; border-bottom: 1px solid #cdcdcd;">
                                <div id="path_exception_div_false" style="width:100%; height:330px; overflow:auto;">
                                <table id="path_exception_false" style="text-align:center; width:100%;">
                                    <tbody>
                                    </tbody>
                                </table>
                                </div>
                            </td>
                        </tr>
                        <tr>
                        	<th style="text-align: center; border-bottom: 1px solid #cdcdcd;" rowspan="2">
                        		판단근거
                        	</th>
                            <td style="padding:10px 0; ">
                            	<textarea rows="5" id="path_detail_false" style="width: 100%; padding-left: 5px; font-size: 12px; resize: none;" placeholder="오탐으로 판단한 명확한 근거에 대해서 상세하게 작성해 주세요."></textarea>
							</td>
                        </tr>
                        <tr>
                            <td style="padding:0 0 10px 0; border-bottom: 1px solid #cdcdcd; color: #9E9E9E;">
                            	<table class="DeletionRegistT">
                            		<tr> <td>예)</td> <td>- 솔루션 색인 파일이며 숫자 나열로 오탐</td> </tr>
                            		<tr> <td></td><td>- 휴대폰번호가 아닌 단말기에 할당되는 별도의 고유번호로 오탐</td> </tr>
                            		<tr> <td></td><td>- 권한ID/개발자ID를 운전면허번호로 오탐</td> </tr>
                            		<tr> <td></td><td>- 개발기 연동 내역으로 실제 고객정보가 아닌 변조 데이터임</td> </tr>
                            		<tr> <td></td><td>- 프로그램 실행로그 날짜데이터 숫자로만 들어가있음</td> </tr>
                            		<tr> <td></td><td>- 솔루션 설치 매뉴얼에 포함된 공인된 메일 주소임(예: support@oralce.com, support@redhat.org)</td> </tr>
                            		<tr> <td></td><td>- 솔루션 및 소스 관련 파일로 미조치 보관</td> </tr>
                            	</table>
							</td>
                        </tr>
                        <tr>
                        	<th style="text-align: center;">조치계획</th>
                        	<td style="padding:5px;">
	                        	<div class="content-box" style="height: 90px; background: #fff; margin: 0;">
	                        	
									<select name="popup_content" id="falseDeletionAction" style="width: 87px;">
										<c:forEach var="falseL" items="${falseList}" varStatus = "status">
											<option value="${falseL.PROCESSING_FLAG}" ${status.count==1?'selected':''} >${falseL.PROCESSING_FLAG_NAME}</option>
										</c:forEach>
									</select>
									<input type="date" name="processing_flag" id="selectDateFalse" value="" ${falseList[0].DATE_ENABLE==1?'style="text-align: center; margin-right: 10px;display:inline-block"':'style="text-align: center; margin-right: 10px;display:none"'} readonly="readonly" >
									<div id="addFalseProcessing" style="display: inline-block;">${falseList[0].COMMENT_TITLE}<c:if test="${falseList[0].COMMENT_ENABLE==1}"><input type='text' class='edt_sch' id='comment_content_false' placeholder='${falseList[0].COMMENT_TEXT}' style='width: 270px; margin-left: 5px; padding-left: 5px'></c:if></div>
									<div class="false_processing_comment">
										<span>${falseList[0].BOTTOM_TEXT}</span>
									</div>
					            </div>
				            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="popup_btn">
            <div class="btn_area" style="padding-right: 0;">
                <button type="button" id="btnFalseDeletionSave">저장</button>
                <button type="button" id="btnFalseDeletionCancel">취소</button>
            </div>
        </div>
    </div>
</div>


<div id="exceptionReasonPopup" class="popup_layer" style="display:none; top: -78px;">
	<div class="ui-widget-content" id="popup_datatype" style="position:absolute; /* height: 480px; */ left: 29%; top: 21%; touch-action: none; max-width: 800px; width:800px; z-index: 999; background: #f9f9f9;">
	<img class="CancleImg" id="btnCancleExceptionReason" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_container" style="height:auto; width: 801px;">
			<!-- <p id="comment" style="position: absolute; bottom: 33px; left: 17px; font-size: 12px; color: #2C4E8C; text-align : center;">이미 등록된 IP는 저장되지 않습니다.</p> -->
			<div class="popup_top" style="background: #f9f9f9;">
				<h1 id = "title_exception" style="color: #222; box-shadow: none; padding: 0;"></h1>
			</div>
			<div class="popup_content">
				<div class="content-box" style="width: 775px !important;min-height:490px; background: #fff; border: 1px solid #c8ced3;/*   border-bottom: none; */">
					<table class="popup_tbl" >
						<colgroup>
							<col width="20%">
							<col width="*">
							<col width="20%">
						</colgroup>
						<tbody>
							<tr>
								<th>오탐 종류</th>
								<td id="details_label">
									<c:forEach var="item" items="${exceptionList}" varStatus ="status">
										<input type="radio"  value ="${item.PROCESSING_FLAG}" name="reason" style ="margin-left:${status.count==1?'0px':'12%'}; margin-right: 3px; border: 0px solid #cdcdcd;">
										${item.PROCESSING_FLAG_NAME}<!-- DB에서 불러오기 -->
									</c:forEach>
									
									
								</td>
							</tr>
							<tr>
								<th>적용대상 서버</th>
								<td id="details_datatype" style="width: 100%; padding:0px 5px 0px 10px;">
                                	<input type="hidden" id="selectedTargetId" value="">
                                	<button type="button" id="btnServerSelectPopup"  style="width: 100px;background-color:#ffffff">찾기</button>
                                	<!-- <input type="radio" class="edt_sch" name="serverChk" id="oneserver" value="" style="border: 0px solid #cdcdcd;" checked> 특정서버 -->
								</td>
								<td class="btn_area" style="padding: 0px 0px 0px 10px; text-align: left;">
                                		
								</td>
							</tr>
	                       <tr>
	                       	   <th style="vertical-align: top"></th>
	                           <td colspan="2" style="padding:0px 5px 0px 10px;">
	                               <span class="edt_sch" id="selectedExcepServer"></span>
	                                <div id = "serverContent" style="width:100%;height:135px; overflow:auto;background-color:#f9f9f9; layout:fixed; border:1px solid #cdcdcd;overflow-y:scroll">
	                                    <table id="exceptServer" style="text-align:center; table-layout:fixed; width:100%;">
	                                        <tbody>
	                                        </tbody>
	                                    </table>
	                                </div>
	                           </td>
	                        </tr>
	                        <tr>
	                            <th rowspan="2">예외처리 경로</th>
	                        </tr>
	                        <tr>
	                        	<td style="padding:0px 0px 0px 10px;">
	                        		<div class="content-box" style="height: 180px; background: #fff; border: 1px solid #c8ced3; padding: 0 0 0 10px;">
										<!-- <h2>세부사항</h2>  -->
										<textarea id="exceptPath" class="edt_sch" style="width: 100%; height: 100%; border: none; resize: none;"></textarea>
									</div>
	                            </td>
	                        </tr>
	                            
<!-- 	                            <td style="padding:0px 0px 0px 10px;"> -->
<!-- 	                                <input type="text" id="inputExceptPath" onkeyup="var e=event||window.event; pathAdd(e);" style="width:98%; padding-left:5px; display:inline-block;margin:0px" placeholder="경로를 입력하십시오"> -->
<!-- 								<td class="btn_area" style="padding: 2px 0 0 0 !important; text-align: left;display:inline-block"> -->
<!--                                 	<button type="button" id="btnAddExceptPath" onclick="fnLocationAdd();" style="margin-top: 10px; margin-right: 5px; width: 100px;">추가</button>	 -->
<!-- 								</td> -->
<!-- 	                        </tr> -->
<!-- 	                        <tr> -->
<!-- 	                        	<th></th> -->
<!-- 	                            <td colspan="2" style="padding:0px 5px 0px 10px;"> -->
<!-- 	                                <div id = "exceptContent" style="width:100%;height:135px;background-color:#f9f9f9; overflow-y:scroll; layout:fixed; border:1px solid #cdcdcd"> -->
<!-- 	                                    <table id="exceptPath" style="text-align:center; table-layout:fixed; width:100%;"> -->
<!-- 	                                        <tbody> -->
<!-- 	                                        </tbody> -->
<!-- 	                                    </table> -->
<!-- 	                                </div> -->
<!-- 	                            </td> -->
<!-- 	                        </tr> -->
							<tr>
								<th>비고</th>
								<td colspan="2" style="padding:0px 0px 0px 10px;">
									<input type="text" id="note" style="width:99%; padding-left:5px; height:30px; text-align:left;" maxlength="200" placeholder="비고 내용을 입력하십시오">
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			
			<div class="popup_btn">
				<div class="btn_area" style="padding: 10px 2px;width:780px; margin: 0;">
					<button type="button" id="btnExceptionSave" style="font-weight: inherit; font-size: 12px; padding: 0 15px;">저장</button>
					<button type="button" id="btnExceptionPopupCancel" style="font-weight: inherit; font-size: 12px; padding: 0 15px;">취소</button>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 경로 예외 팝업 시작 -->
<div id="serverSelect" class="popup_layer" style="display:none;" >
    <div class="popup_box" style="background: #f9f9f9; height: 510px; width: 500px; margin-left: -350px;margin-top:-350px !important;">
        <div class="popup_top"  style="background: #f9f9f9;">
            <h1 style="color: #222; box-shadow: none; padding: 0;margin : 10px;margin-bottom:0px;">서버 선택</h1>
        </div>
        <div class="popup_content">
            <div class="content-box" style="height: 440px;margin:0px">
                <table id="popupTargetGrid"></table>
                <div id="popupTargetGridPager"></div>
            </div>
        </div>
        <div class="popup_btn" style="margin:15px;margin-top:0px">
            <div class="btn_area">
                <button type="button" id="btnHostSelect">선택</button>
                <button type="button" id="btnHostChageSelect">선택</button>
                <button type="button" id="btnHostCancel">취소</button>
            </div>
        </div>
    </div>
</div>
<!-- 팝업창 - 비밀번호 입력  -->
<div id="encryptPasswordPopup" class="popup_layer" style="display: none">
	<div class="popup_box"
		style="height: 150px; width: 400px; left: 55%; top: 65%; padding: 10px; background: #f9f9f9;">
		<img class="CancleImg" id="btnCancleEncryptPasswordPopup"
			src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top;" style="background: #f9f9f9;">
			<h1
				style="color: #222; padding: 0; box-shadow: none; font-size: 20px; font-weight: 500;">암호화 비밀번호 입력</h1>
		</div>
		<div class="popup_content">
			<div class="content-box"
				style="height: 105px; background: #fff; border: 1px solid #c8ced3;">
				<!-- <h2>세부사항</h2>  -->
				<table class="popup_tbl">
					<colgroup>
						<col width="30%">
						<col width="*">
					</colgroup>
					<tbody>
						<tr>
							<th>비밀번호</th>
							<td><input style="width: 238px; padding-left: 10px;"
								type="password" id="encryptPassword" value="" class="edt_sch"
								placeholder="암호화 비밀번호를 입력하세요."></td>
						</tr>
						<tr>
							<th>비밀번호 확인</th>
							<td><input style="width: 238px; padding-left: 10px;"
								type="password" id="encryptPasswordConfirm" value="" class="edt_sch"
								placeholder="암호화 비밀번호를 다시 입력하세요."></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area">
				<button type="button" id="btnEncryptPassword">확인</button>
				<button type="button" id="btnEncryptPasswordCancel">취소</button>
			</div>
		</div>
	</div>
</div>
<!-- 팝업창 - 파일이동 경로 입력  -->
<div id="quarantinePopup" class="popup_layer" style="display: none">
	<div class="popup_box"
		style="height: 100px; width: 450px; left: 55%; top: 65%; padding: 10px; background: #f9f9f9;">
		<img class="CancleImg" id="btnCancleEncryptPasswordPopup"
			src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top;" style="background: #f9f9f9;">
			<h1
				style="color: #222; padding: 0; box-shadow: none; font-size: 20px; font-weight: 500;">경로 입력</h1>
		</div>
		<div class="popup_content">
			<div class="content-box"
				style="height: 155px; background: #fff; border: 1px solid #c8ced3;">
				<!-- <h2>세부사항</h2>  -->
				<table class="popup_tbl">
					<colgroup>
						<col width="30%">
						<col width="*">
					</colgroup>
					<tbody>
						<tr>
							<th>경로</th>
							<td><input style="width: 308px; height:30px; padding-left: 10px;"
								id="quarantinePath" value="" class="edt_sch"
								placeholder="파일이 이동될 경로를 입력하세요."></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area">
				<button type="button" id="btnQuarantineSave">확인</button>
				<button type="button" id="btnQuarantineCancel">취소</button>
			</div>
		</div>
	</div>
</div>