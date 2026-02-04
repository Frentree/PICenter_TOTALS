<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%@ include file="../../include/header.jsp"%>
<style>
h4 {
	margin : 5px 0;
	font-size: 0.8vw;
}
</style>
 
	<!-- section -->
	<section id="section">
		<!-- container -->
		<div class="container">
			<h3>개인정보 유형</h3>
			<div class="content magin_t25">
				<div class="grid_top">
					<table class="user_info" style="display: inline-table; width: 510px; position: relative; bottom: 32px;">
						<caption>개인정보 유형</caption>
						<tbody>
							<tr>
								<th style="text-align: center; width:40px; border-radius: 0.25rem;">유형 명</th>
								<td style="width:300px;">
									<input type="text" id="searchLocation" value="" class="edt_sch" style="width: 93%; height: 26.5px; margin: 5px 0; padding-left:5px;" placeholder="개인정보 유형명을 입력하세요.">
									<input type="button" name="button" class="btn_route" style="margin-top: 10px; margin-right: 5px;" id="btnSearch">
								</td>
							</tr>
						</tbody>
					</table>
					<div class="legend" style="margin-left: 499px; margin-top: 5px; border-radius: 0.25rem;">
						<div class="legend_info" >
							<p style="padding-left: 13px;">범례</p>
							<table class="legend_tbl">
								<colgroup>
									<col width="20%">
									<col width="*">
								</colgroup>
								<tbody id="legend_detail">
								<tr>
									<th>주민등록번호</th>
								</tr>
								<tr>
									<td style="padding-left: 25px;">
										1.<input type='checkbox' id='test_chk1' name='chk_dataType' class='chk_lock' value='rrn' style="margin-top: 2px; margin-left: 5px;" disabled="disabled">
										2.<input type='checkbox' id='test_chk2' style="margin-top: 2px; margin-left: 5px;"  disabled="disabled">
										3.<input type='text' id='test_cnt' size='4' disabled='disabled' style="margin-top: 2px; margin-left: 5px;">
									</td>
								</tr>
								</tbody>
							</table>
						</div>
						<div class="legend_description">
							<p style="padding-top: 6px;">1. 임계치 사용여부 체크</p>
							<p>2. 중복제거 사용여부 체크</p>
							<p>3. 임계치 사용 시 임계값 입력</p>
						</div>
					</div>
					<div class="list_sch" style="margin-top: 50px;">
						<div class="sch_area">
							<button type="button" class="btn_down" id="btnGridCsvDown" class="btn_new">다운로드</button> 
							<button type="button" class="btn_down" id="btnDataTypeBtn" class="btn_new">개인정보 유형 생성</button>
						</div>
					</div>
				</div>
				<div class="left_box2" style="height: auto; min-height: 630px; overflow-x: auto; margin-top: 5px;">
					<table id="targetGrid" ></table>
					<div id="targetGridPager"></div>
				</div>
			</div>
		</div>
	</section>
	<div id="taskWindow" class="ui-widget-content" style="position:absolute; left: 10px; top: 10px; touch-action: none; width: 150px; z-index: 999; 
		border-top: 2px solid #222222; box-shadow: 2px 2px 5px #ddd; display:none">
		<ul>
			<li  class="status status-completed status-scheduled status-paused status-stopped status-failed">
				<button id="updateBtn">수정 </button></li>
			<li class="status status-completed status-scheduled status-scanning status-paused status-stopped">
				<button id="deleteBtn">삭제</button></li>
		</ul>
	</div>
	
	<div id="popup_manageSchedule" class="popup_layer" style="display:none;">
		<div class="popup_box" id="popup_box" style="height: 60%; width: 60%; left: 40%; top: 33%; right: 40%; ">
		</div>
	</div>
	<%@ include file="../../include/footer.jsp"%>

<script type="text/javascript"> 

var isCreateProfile = true;
var colModel = [];
GridName = "#targetGrid";
var oGrid = $("#targetGrid");
var pattern = '${pattern}'.split('[{').join('').split('}]').join('');
pattern = pattern.split('}, {');

function change(ele, row) {
	if(ele.checked){
		$("#"+ele.value+"_cnt"+row).val("0");
		$("#"+ele.value+"_cnt"+row).prop('disabled', false);
	}else {
		$("#"+ele.value+"_cnt"+row).val("");
		$("#"+ele.value+"_cnt"+row).prop('disabled', true);
	}
}

function change2(ele, row) {
	if(ele.checked){
		$("#recent_"+row).val("1");
		$("#recent_"+row).prop('disabled', false);
	}else {
		$("#recent_"+row).val("");
		$("#recent_"+row).prop('disabled', true);
	}
}
 
function numberChange(ele) {
	ele.value = ele.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');
}

function createProfile(rowID){
	var rowData = $("#targetGrid").getRowData(rowID);
	var scheduleData = {};  // Scan Data Mater Json

	var datatype_name = $("#datatypeName"+rowID).val();
	if (isNull(datatype_name)) {
		alert("저장할 유형명을 입력하세요.");
		return;
	}
	
	var extension = $("#chkExtension"+rowID).val();
	var extensions = "";
	if(!isNull(extension)) {
		extension = extension.split(",");
		extensions = extension.join(",");
	}else {
		extensions = "";
	}
	
	// profile(Datatype) 넣기
	var datatypeArr = new Array(); 
	var cntArr = new Array(); 
	var dupArr = new Array(); 
	var chkArr = new Array(); 
    $('[name=chk_dataType'+rowID+']').each(function(i, element){
	    var id = $(element).val();
	    datatypeArr.push(id);
	    if (element.checked) {
		    cntArr.push($("#" + id+"_cnt"+rowID).val());
			dupArr.push(($("#dup_" + id).is(":checked"))?1:0)
			chkArr.push(1);
	    }else{
		    cntArr.push(0);
			dupArr.push(0);
			chkArr.push(0);
	    }
    });

	if (datatypeArr.length == 0) {
		alert("개인정보 유형을 선택하세요.");
		return;
	}
	
	
	var profileArr = datatypeArr.toString();
	var profileCntArr = cntArr.toString();
	var profileDupArr = dupArr.toString();
	var profileChkArr = chkArr.toString();
	console.log("profileArr : " + profileArr);
	
	var ocr = $("#chkOcr"+rowID).is(":checked") ? "1" : "0";
	var recent = $("#chkRecent"+rowID).is(":checked") ? $("#recent_"+rowID).val() : "0";
	var capture = "1";
	
	var postData = {
		datatype_name : datatype_name,
		profileArr : profileArr, 
		cntArr : profileCntArr, 
		dupArr : profileDupArr, 
		chkArr : profileChkArr, 
		ocr : ocr,
		capture : capture,
		recent: recent,
		datatype_id: rowData.DATATYPE_ID,
		extension: extensions
	};
	
	var tttt = JSON.stringify(postData);
	console.log("postData 확인 : ",postData);
	
	var message = "개인정보 유형을 만드시겠습니까?";
	if (confirm(message)) {
		$.ajax({
			type: "POST",
			url: "/search/insertProfile",
			async : false,
			data : postData,
		    success: function (resultMap) {
		        if (resultMap.resultCode == 201) {
		        	alert("개인정보유형 생성되었습니다.");
		        	
		        } else {
			        alert("데이터 유형 변경이 실패 되었습니다.\n관리자에게 문의 하십시오");
			    }
		        isCreateProfile = true;
		        var postData = {};
	        	$("#targetGrid").setGridParam({url:"<%=request.getContextPath()%>/search/getProfile", postData : postData, datatype:"json" }).trigger("reloadGrid");
		    	
		    },
		    error: function (request, status, error) {
				alert("Server Error : " + error);
		        console.log("ERROR : ", error);
		    }
		});
	}
}

function updateProfile(rowID){
	var rowData = $("#targetGrid").getRowData(rowID);
	var scheduleData = {};  // Scan Data Mater Json

	var datatype_name = $("#datatypeName"+rowID).val();
	if (isNull(datatype_name)) {
		alert("저장할 유형명을 입력하세요.");
		return;
	}
	
	var extension = $("#updateChkExtension"+rowID).val();
	var extensions = "";
	if(!isNull(extension)) {
		extension = extension.split(",");
		extensions = extension.join(",");
	}else {
		extensions = "";
	}

	// profile(Datatype) 넣기
	var datatypeArr = new Array(); 
	var cntArr = new Array(); 
	var dupArr = new Array(); 
	var chkArr = new Array(); 
    $('[name=chk_dataType'+rowID+']').each(function(i, element){
	    var id = $(element).val();
	    datatypeArr.push(id);
	    if (element.checked) {
		    cntArr.push($("#" + id+"_cnt"+rowID).val());
			dupArr.push(($("#dup_" + id).is(":checked"))?1:0);
			chkArr.push(1);
	    }else{
	    	cntArr.push(0);
			dupArr.push(0);
			chkArr.push(0);
	    }
    });

	if (datatypeArr.length == 0) {
		alert("개인정보 유형을 선택하세요.");
		return;
	}
	
	var profileArr = datatypeArr.toString();
	var profileCntArr = cntArr.toString();
	var profileDupArr = dupArr.toString();
	var profileChkArr = chkArr.toString();
	console.log("profileArr : " + profileArr);
	
	var ocr = $("#chkOcr"+rowID).is(":checked") ? "1" : "0";
	var recent = $("#chkRecent"+rowID).is(":checked") ? $("#recent_"+rowID).val() : "0";
	var capture = "1";
	
	var postData = {
		datatype_name : datatype_name,
		profileArr : profileArr, 
		cntArr : profileCntArr, 
		dupArr : profileDupArr, 
		chkArr : profileChkArr, 
		ocr : ocr,
		recent : recent,
		capture : capture,
		datatype_id: rowData.DATATYPE_ID,
		std_id: rowData.STD_ID,
		extension: extensions
	};
	
	var tttt = JSON.stringify(postData);
	
	var message = "개인정보 유형을 변경하시겠습니까?";
	if (confirm(message)) {
		$.ajax({
			type: "POST",
			url: "/search/updateProfile",
			async : false,
			data : postData,
		    success: function (resultMap) {
		        if (resultMap.resultCode == 201) {
		        	alert("데이터 유형 변경하였습니다.");
		        	var postData = {};
		        	$("#targetGrid").setGridParam({url:"<%=request.getContextPath()%>/search/getProfile", postData : postData, datatype:"json" }).trigger("reloadGrid");
			    } else {
			        alert("데이터 유형 변경이 실패 되었습니다.\n관리자에게 문의 하십시오");
			    }
		    },
		    error: function (request, status, error) {
				alert("Server Error : " + error);
		        console.log("ERROR : ", error);
		    }
		});
	}
}



function createProfileCencel(rowId) {
	var postData = {};
	$("#targetGrid").setGridParam({url:"<%=request.getContextPath()%>/search/getProfile", postData : postData, datatype:"json" }).trigger("reloadGrid");

	isCreateProfile = true;
}

function cancelProfile(rowId) {
	var postData = {};
	$("#targetGrid").setGridParam({url:"<%=request.getContextPath()%>/search/getProfile", postData : postData, datatype:"json" }).trigger("reloadGrid");
	isCreateProfile = true;
}

$(document).ready(function () {

	$(document).click(function(e){
		$("#taskWindow").hide();
	});
	
	$("#btnDataTypeBtn").click(function(){
		var rowId = $("#targetGrid").getGridParam("reccount");
		
		if(!isCreateProfile){
			alert("진행 중인 내역이 있습니다");
			return true;
		}
		isCreateProfile = false;
		
		var rowData = {
			"DATATYPE_ID": " ",
			"DATATYPE_LABEL_COPY": "<input type='text' name='datatypeName"+rowId+"' id='datatypeName"+rowId+"' value='' style='width: 100%;' placeholder='개인정보 유형명을 입력하세요.'>",
			"DATATYPE_LABEL": " "};
			
			 for(var i = 0; pattern.length > i; i++){
				 
             	var row = pattern[i].split(', ');
         		var ID = row[0].split('ID=').join(''); //변수 TABLE_NAME은 TB_NAME1, 다음 반복문에서는 TB_NAME2
         		var PATTERN_NAME = row[1].split('PATTERN_NAME=').join(''); // 변수 CHK Y, 다음 반복문에서는 N
         		var data_id = PATTERN_NAME.split('=')[1];
         		
         		var custom_nm = data_id;
         		var custom_nm_dup = data_id+"_DUP";
         		var custom_nm_cnt = data_id+"_CNT";
         		var custom_nm_chk = data_id+"_CHK";

         		rowData[custom_nm_chk] = "<input type='checkbox' id='chk_'"+custom_nm+" name='chk_dataType"+rowId+"' class='chk_lock' onchange='change(this,"+rowId+")' value='"+custom_nm+"'>&nbsp;"
    			+ "<input type='checkbox' id='dup_"+custom_nm+"'>&nbsp;"
                + "<input type='text' id='"+custom_nm+"_cnt"+rowId+"' disabled='disabled' size='3' oninput='numberChange(this)'>",
         		rowData[custom_nm] =" ";
         		rowData[custom_nm_cnt] = " ";
			 };
			
			 rowData["OCR_CHK"] = "<input type='checkbox' id='chkOcr"+rowId+"' name='chkOcr"+rowId+"' class='chk_lock' />" ;
			 rowData["OCR"] = " "; 
			 rowData["VOICE"] = "0"; 
			 rowData["RECENT_CHK"] = "<input type='checkbox' id='chkRecent"+rowId+"' name='chkRecent"+rowId+"' class='chk_lock' onchange='change2(this,"+rowId+")'/> &nbsp;"
			 + "<input type='text' id='recent_"+rowId+"' disabled='disabled' size='3' oninput='numberChange(this)'>";
			 rowData["RECENT"] = " ";
			 rowData["EXTENSION"] = "<input type='text' name='chkExtension"+rowId+"' id='chkExtension"+rowId+"' value='' style='width: 100%;'>";
			 rowData["BUTTON"] = "<button type='button' class='gridSubSelBtn' name='updateProfileBtn' onclick='createProfile("+rowId+")'>생성</button>"
				+"<button type='button' class='gridSubSelBtn' name='cancelProfileBtn' onclick='createProfileCencel("+rowId+")'>취소</button>";
			 
		$("#targetGrid").addRowData(rowId+1, rowData, 'first');
		//window.location = "${pageContext.request.contextPath}/scan/pi_datatype_insert";
	});
	
	$("#taskWindow").click(function(e){
		e.stopPropagation();
	});
	$("#taskWindowClose").click(function(){
		$("#taskWindow").hide();
	});
	
  	$("#viewWindow").draggable({
 	    containment: '.content',
 	 	cancel : '.popup_content, .popup_btn'
	});
	
	$("#viewWindowClose").click(function(){
		$("#viewWindow").hide();
	});
	
	
	$("#vieDetailsClose").click(function(){
		$("#viewDetails").hide();
	})

	var ifConnect = function(cellvalue, options, rowObject) {
		console.log('cellvalue BEFORE :: ' + cellvalue);
		if(cellvalue == null || cellvalue == ''){
			cellvalue = '<target deleted>'
		}
		console.log('cellvalue AFTER :: ' + cellvalue);
		return cellvalue.indexOf("<") >= 0 ? '< ' + cellvalue.replace("<","").replace(">","") + ' >' : cellvalue;
	};
	
	var createView = function(cellvalue, options, rowObject) {
		//return '<img src="/resources/assets/images/img_check.png" style="cursor: pointer;" name="gridSubSelBtn" class="gridSubSelBtn" value=" 선택 "></a>';
		return "<button type='button' class='gridSubSelBtn' name='gridSubSelBtn'>선택</button>"; 
	};
	
	$("#fromDate").datepicker({
		changeYear : true,
		changeMonth : true,
		dateFormat: 'yy-mm-dd',
		onSelect: function(dateText) {
			$("#btnSearchScan").click();
		}
	});
	$("#toDate").datepicker({
		changeYear : true,
		changeMonth : true,
		dateFormat: 'yy-mm-dd',
		onSelect: function(dateText) {
			$("#btnSearchScan").click();
		}
	});

	targetGridLoad();
	
	// 초기 조회
	var postData = {};
	$("#targetGrid").setGridParam({url:"<%=request.getContextPath()%>/search/getProfile", postData : postData, datatype:"json" }).trigger("reloadGrid");
	
	// 버튼 Action 설정
	$("#btnSearchScan").click(function() {

		var idx = 1;		
		var searchType = ['scheduled'];
		$("input[name=chk_schedule]:checked").each(function(i, elem) {
			searchType[idx++] = $(elem).val();	
		});

		var postData = {};

		$("#targetGrid").setGridParam({url:"<%=request.getContextPath()%>/search/getProfile", postData : postData, datatype:"json" }).trigger("reloadGrid");
    });

	$("#searchHost").keyup(function(e){
		if (e.keyCode == 13) {
			$("#btnSearchScan").click();
		}
	});
	
	$("#updateBtn").click(function(){
		
		$("#taskWindow").hide();
		
		if(!isCreateProfile){
			alert("진행 중인 내역이 있습니다");
			return true;
		}
		isCreateProfile = false;
		var row = $("#targetGrid").getGridParam( "selrow" );
		
		var rowData = $("#targetGrid").getRowData(row);
		
		$("#targetGrid").setCell(row, "DATATYPE_LABEL_COPY", "<input type='text' name='datatypeName"+row+"' id='datatypeName"+row+"' value='"+ rowData.DATATYPE_LABEL +"' style='width: 100%;'>");

		var text = "";
		
		console.log(rowData);
		 for(var i = 0; pattern.length > i; i++){
         	
         	var patternRow = pattern[i].split(', ');
     		var ID = patternRow[0].split('ID=').join(''); //변수 TABLE_NAME은 TB_NAME1, 다음 반복문에서는 TB_NAME2
     		var PATTERN_NAME = patternRow[1].split('PATTERN_NAME=').join(''); // 변수 CHK Y, 다음 반복문에서는 N
     		var data_id = PATTERN_NAME.split('=')[1];
         	
     		var custom_nm = data_id;
     		var custom_nm_dup = data_id+"_DUP";
     		var custom_nm_cnt = data_id+"_CNT";
     		var custom_nm_chk = data_id+"_CHK";  
     		var custom_nm_chk_copy = data_id+"_CHK_COPY";  
     		 
     		text = "<input type='checkbox' id='chk_'"+custom_nm+" name='chk_dataType"+row+"' class='chk_lock' onchange='change(this,"+row+")' value='"+custom_nm+"' "
    		text += ((rowData[custom_nm_chk_copy] == 1)? "checked" : "") + ">&nbsp;"
    		text += "<input type='checkbox' id='dup_"+custom_nm+"' ";
    		text += ((rowData[custom_nm_dup] == 1)? "checked" : "") + ">&nbsp;"
    		text += "<input type='text' id='"+custom_nm+"_cnt"+row+"' size='3' oninput='numberChange(this)' "
    		text += (rowData[custom_nm_chk_copy] == 1)? "value='"+ rowData[custom_nm_cnt] +"'" : "disabled"
    		text += ">"
    		$("#targetGrid").setCell(row, custom_nm_chk, text);
         }
		  
		if(rowData.OCR == 1){
			$("#targetGrid").setCell(row, "OCR_CHK", "<input type='checkbox' id='chkOcr"+row+"' name='chkOcr"+row+"' class='chk_lock' checked/>");
		} else {
			$("#targetGrid").setCell(row, "OCR_CHK", "<input type='checkbox' id='chkOcr"+row+"' name='chkOcr"+row+"' class='chk_lock' />");
		}
		
		var recent_text = "";
		if(rowData.RECENT > 0){
			recent_text = "<input type='checkbox' id='chkRecent"+row+"' name='chkRecent"+row+"' class='chk_lock' onchange='change2(this,"+row+")' checked/>&nbsp;";
		} else {
			recent_text =  "<input type='checkbox' id='chkRecent"+row+"' name='chkRecent"+row+"' class='chk_lock' onchange='change2(this,"+row+")' />&nbsp;";
		}
		
		recent_text += "<input type='text' id='recent_" + row + "' size='3' oninput='numberChange(this)' "
		recent_text += (rowData.RECENT > 0)? "value='"+ rowData.RECENT +"'" : "disabled"
		recent_text += ">"
			$("#targetGrid").setCell(row, "RECENT_CHK", recent_text);
		
		$("#targetGrid").setCell(row, "EXTENSION", "<input type='text' id='updateChkExtension"+row+"' name='chkEx"+row+"' value='"+rowData.EXTENSION+"' style='width: 100%; text-overflow: clip;' />");
			
		$("#targetGrid").setCell(row, "BUTTON", "<button type='button' class='gridSubSelBtn' name='updateProfileBtn' onclick='updateProfile("+row+")'>수정</button>"
				+"<button type='button' class='gridSubSelBtn' name='cancelProfileBtn' onclick='cancelProfile("+row+")'>취소</button>");
		
	});
	
	
	$("#deleteBtn").click(function(){
		$("#taskWindow").hide();
		var row = $("#targetGrid").getGridParam( "selrow" );
		var id = $("#targetGrid").getCell(row, 'STD_ID');
		var name = $("#targetGrid").getCell(row, 'DATATYPE_LABEL');
		
		var postData = {datatype_id: id, datatype_label : name};
		
		var message = "정말 삭제하시겠습니까?";
		if (confirm(message)) {
			$.ajax({
				type: "POST",
				url: "/search/deleteProfile",
				async : false,
				data : postData,
			    success: function (resultMap) {
			    	console.log("asdf");
			    	console.log(resultMap);
			        if (resultMap.resultCode == 0) {
			        	alert("삭제를 완료하였습니다.");
			        	var postData = {};
			        	$("#targetGrid").setGridParam({url:"<%=request.getContextPath()%>/search/getProfile", postData : postData, datatype:"json" }).trigger("reloadGrid");
				    } else if(resultMap.resultCode == -9) {
				        alert("삭제 할 수 없는 정책 입니다.");
				    } else {
				    	alert("삭제를 실패하였습니다.");
				    }
			    },
			    error: function (request, status, error) {
					alert("Server Error : " + error);
			        console.log("ERROR : ", error);
			    }
			});
		}
	});
	
	$("#btnDownloadExel").click(function(){
		downLoadExcel();
	});
	
});

var today = new Date();
var dd = today.getDate();
var mm = today.getMonth()+1; //January is 0!
var yyyy = today.getFullYear();

if(dd<10) {
    dd='0'+dd
} 

if(mm<10) {
    mm='0'+mm
} 

today = yyyy + "" + mm + dd;

function downLoadExcel()
{
    oGrid.jqGrid("exportToCsv",{
        separator: ",",
        separatorReplace : "", // in order to interpret numbers
        quote : '"', 
        escquote : '"', 
        newLine : "\r\n", // navigator.userAgent.match(/Windows/) ? '\r\n' : '\n';
        replaceNewLine : " ",
        includeCaption : true,
        includeLabels : true,
        includeGroupHeader : true,
        includeFooter: true,
        fileName : "스케줄리스트_" + today + ".csv",
        mimetype : "text/csv; charset=utf-8",
        returnAsString : false
    })
}

/**
 * id : schedule ID, 
 * task : 변경할 Task 
 * row : 그리드의 변경 Row ID
 * Task 변경 규칙 
 * 	- deactivate -->  deactivated
 * 	- modify --> 스캐줄 정의 및 등록 화면으로 이동, 
 * 	- skip --> scheduled
 * 	- pause --> paused
 * 	- restart --> scheduled
 * 	- stop --> scheduled
 * 	- cancel --> cancelled
 * 	- reactivate --> scheduled
 */
 
function targetGridLoad(){
	 
		
	var gridWidth = $("#targetGrid").parent().width();
	var gridHeight = 555;
 
	var patternCnt = '${patternCnt}';
	var colNames = [];
	
	colNames.push('아이디','통괄아이디','개인정보유형명', '타입 유형명카피');
	
	for(var i=0; i < patternCnt ; i++){
		var row = pattern[i].split(', ');
		var ID = row[0].split('ID=').join(''); //변수 TABLE_NAME은 TB_NAME1, 다음 반복문에서는 TB_NAME2
		var PATTERN_NAME = row[1].split('PATTERN_NAME=').join(''); // 변수 CHK Y, 다음 반복문에서는 N
		var data_id = PATTERN_NAME.split('=')[1];
		
		colNames.push(ID.split('=')[1]);
		colNames.push(ID.split('=')[1]+" 체크");
		colNames.push(ID.split('=')[1]+" 체크_copy");
		colNames.push(ID.split('=')[1]+" 임계치");
		colNames.push(ID.split('=')[1]+" 중복");
	} 
	colNames.push('이미지검사', 'OCR체크', '증분검사CHK', '증분검사', '확장자', 'VOICE', '작업');
	
	colModel.push({ index: 'DATATYPE_ID', 				name: 'DATATYPE_ID',				width:1, align:'center', hidden:true});
	colModel.push({ index: 'STD_ID', 						name: 'STD_ID',				width:1, align:'center', hidden:true});
	colModel.push({ index: 'DATATYPE_LABEL_COPY',			name: 'DATATYPE_LABEL_COPY',		width: 400, align: 'left'});
	colModel.push({ index: 'DATATYPE_LABEL',				name: 'DATATYPE_LABEL',				width: 150, align: 'left', hidden: true});
	
	for(var i = 0; pattern.length > i; i++){
		
		var row = pattern[i].split(', ');
		var ID = row[0].split('ID=').join(''); //변수 TABLE_NAME은 TB_NAME1, 다음 반복문에서는 TB_NAME2
		var PATTERN_NAME = row[1].split('PATTERN_NAME=').join(''); // 변수 CHK Y, 다음 반복문에서는 N
		var data_id = PATTERN_NAME.split('=')[1];
		
		colModel.push({ index: data_id+"_CHK", 				name: data_id+"_CHK", 				width: 150, align: 'left', sortable: false });
		colModel.push({ index: data_id, 					name: data_id, 						width: 40, align: 'left', hidden: true});
		colModel.push({ index: data_id+"_CHK_COPY", 		name: data_id+"_CHK_COPY", 			width: 40, align: 'left', hidden: true});
		colModel.push({ index: data_id+"_CNT", 				name: data_id+"_CNT", 				width: 40, align: 'left', hidden: true});
		colModel.push({ index: data_id+"_DUP", 				name: data_id+"_DUP", 				width: 40, align: 'left', hidden: true});
	}; 
	  
	colModel.push({ index: 'OCR_CHK', 					name: 'OCR_CHK', 					width: 100, align: 'center', sortable: false});  
	colModel.push({ index: 'OCR', 						name: 'OCR', 						width: 40, align: 'left', hidden: true });
	colModel.push({ index: 'RECENT', 						name: 'RECENT', 					width: 40, align: 'left', hidden: true });
	colModel.push({ index: 'RECENT_CHK', 					name: 'RECENT_CHK', 				width: 100, align: 'center'});
	colModel.push({ index: 'EXTENSION', 					name: 'EXTENSION', 					width: 150, align: 'left'});
	colModel.push({ index: 'VOICE', 						name: 'VOICE', 						width: 40, align: 'center', hidden: true});
	colModel.push({ index: 'BUTTON', 						name: 'BUTTON',						width: 150, align: 'center', sortable: false });
	
	$("#targetGrid").jqGrid({
		//url: 'data.json',
		datatype: "local",
	   	mtype : "POST",
	   	ajaxGridOptions : {
			type    : "POST",
			async   : true
		},
		colNames:colNames,
		colModel:colModel,
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: gridWidth,
		height: gridHeight,
		loadonce: true, // this is just for the demo
	   	autowidth: true,
		shrinkToFit: false,
		rownumbers : false, // 행번호 표시여부
		rownumWidth : 30, // 행번호 열의 너비	
		rowNum:20,
	   	rowList:[20,50,100],			
		pager: "#targetGridPager",
		//jqgrid의 특성상 - rowpos의 이벤트는 onSelectCell, beforeEditCell 다 해주어야 함
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	  	
	  	},
	  	ondblClickRow: function(nRowid, icol, cellcontent, e){
	  		
	  	},
		loadComplete: function(data) {
			var ids = $("#targetGrid").getDataIDs();
            $.each(ids, function(idx, rowId) {
                rowData = $("#targetGrid").getRowData(rowId, true);
                $("#targetGrid").setCell(rowId, 'DATATYPE_LABEL_COPY', rowData.DATATYPE_LABEL);
                
                var html = ""; 
                for(var i = 0; pattern.length > i; i++){
                	var row = pattern[i].split(', ');
            		var ID = row[0].split('ID=').join(''); //변수 TABLE_NAME은 TB_NAME1, 다음 반복문에서는 TB_NAME2
            		var PATTERN_NAME = row[1].split('PATTERN_NAME=').join(''); // 변수 CHK Y, 다음 반복문에서는 N
            		var data_id = PATTERN_NAME.split('=')[1];
                	
            		var custom_nm = data_id;
             		var custom_nm_dup = data_id+"_DUP";
             		var custom_nm_cnt = data_id+"_CNT";
             		var custom_nm_chk = data_id+"_CHK";
             		
					var change_col = rowData[custom_nm_chk];
					var chk_grid_copy = data_id+"_CHK_COPY";
             		
	                $("#targetGrid").jqGrid('setCell', rowId, chk_grid_copy, change_col);
            		
            		html = "<input type='checkbox' disabled='disabled' "+((rowData[custom_nm_chk] == 1)?"checked='checked'":"")+">&nbsp;";
	            	html += "<input type='checkbox' disabled='disabled' "+((rowData[custom_nm_dup] == 1)?"checked='checked'":"")+">&nbsp;";
	            	html += (rowData[custom_nm_chk] == 1)? rowData[custom_nm_cnt] : '';
	            	
	            	$("#targetGrid").setCell(rowId, custom_nm_chk, html);
                }
                
                html = "<input type='checkbox' disabled='disabled' "+((rowData.OCR == 1)?"checked='checked'":"")+">&nbsp;";
                $("#targetGrid").setCell(rowId, 'OCR_CHK', html);

                var recent_html = ((rowData.RECENT > 0)? "checked='checked'" : "" );
                
                html = "<input type='checkbox' disabled='disabled' "+recent_html+" >&nbsp;";
            	html += (rowData.RECENT > 0)? rowData.RECENT : '';
                $("#targetGrid").setCell(rowId, 'RECENT_CHK', html);
                
                $("#targetGrid").setCell(rowId, 'BUTTON', "<button type='button' class='gridSubSelBtn' name='gridSubSelBtn' style='margin-left: 7px'>선택</button>");
            });
			
			$(".gridSubSelBtn").on("click", function(e) {
		  		e.stopPropagation();
				
				$("#targetGrid").setSelection(event.target.parentElement.parentElement.id);
				
				var offset = $(this).parent().offset();
				$("#taskWindow").css("left", (offset.left - $("#taskWindow").width()) + 45 + "px");
				// $("#taskWindow").css("left", (offset.left - $("#taskWindow").width() + $(this).parent().width()) + "px");
				$("#taskWindow").css("top", offset.top + $(this).height() + "px");

				var bottomLimit = $(".left_box2").offset().top + $(".left_box2").height();
				var taskBottom = Number($("#taskWindow").css("top").replace("px","")) + $("#taskWindow").height();

				if (taskBottom > bottomLimit) { 
					$("#taskWindow").css("top", Number($("#taskWindow").css("top").replace("px","")) - (taskBottom - bottomLimit) + "px");
				}
				$("#taskWindow").show();
			}); 
			$('.ui-jqgrid-hdiv').css('height', '35px') 
			
			automaticCompletion2("searchLocation", "DATATYPE_LABEL_COPY");
	    },
	    gridComplete : function() {
	    }
	});
}

 

function createCheckbox(cellvalue, options, rowObject) {
	var rowID = options['rowId'];
	var checkboxID = "gridChk" + rowID;
	
	if(rowObject[options.colModel['index']] == "1" && options.colModel['index'] == "OCR")
		return "<img src='${pageContext.request.contextPath}/resources/assets/images/img_check.png' />";
	
	if (rowObject[options.colModel['index']] == "1")
		return "<img src='${pageContext.request.contextPath}/resources/assets/images/img_check.png' /><p>"+ rowObject[options.colModel['index'] + "_CNT"] +"</p>";
		//return "<input type='checkbox' id='" + checkboxID + "' value='" + rowID + "' checked disabled>"; 
	else 
		return "";
		//return "<input type='checkbox' id='" + checkboxID + "' value='" + rowID + "' disabled>";
}

function deleteSchedule(){
	$.ajax({
		type: "POST",
		url: "/scan/deleteSchedule",
		async : false,
		data : {
			schedule_id: $('#pop_manageSchedule_schedule_id').val()
		},
		dataType: "text",
	    success: function (resultMap) {
	    	var data = JSON.parse(resultMap);
	    	if(data.resultCode == '0'){
		    	setManageSchedulePop($('#pop_manageSchedule_schedule_id').val());
				alert('스케줄을 삭제 하였습니다.');
	    	} else {
	    		alert('스케줄을 실패하는데 실패하였습니다.');
	    	}
	    },
	    error: function (request, status, error) {
	    	alert("Recon Server에 접속할 수 없습니다.");
	        console.log("ERROR : ", error);
	    }
	});
}

function setSchedule(){
	var weekday = ['mon', 'tue', 'wed', 'thu', 'fri', 'sat', 'sun']
	var startweek = $('#start_weekday').val();
	var starttime = $('#start_time').val();
	var endweek = $('#end_weekday').val();
	var endtime = $('#end_time').val();
	
	if(startweek == endweek && starttime == endtime){
		var key = weekday[startweek] + '_' + (starttime < 10 ? '0'+String(starttime) : starttime);
        $('td[data-key="'+key+'"]').data('value', true);
        $('td[data-key="'+key+'"]').attr('data-value', true);
        $('td[data-key="'+key+'"]').attr('style', 'text-align: center; background: #4dff4d');
        $('td[data-key="'+key+'"]').text('ON');	
        
        $('#start_weekday').val('0').attr('selected', 'selected');
    	$('#start_time').val('0').attr('selected', 'selected');
    	$('#end_weekday').val('0').attr('selected', 'selected');
    	$('#end_time').val('0').attr('selected', 'selected');
    	return;
	}
	
	var week = Number(startweek);
	var time = Number(starttime);
	var flag = true;
	while(flag){
		var key = weekday[week] + '_' + (time < 10 ? '0'+String(time) : time);
        $('td[data-key="'+key+'"]').data('value', true);
        $('td[data-key="'+key+'"]').attr('data-value', true);
        $('td[data-key="'+key+'"]').attr('style', 'text-align: center; background: #4dff4d');
        $('td[data-key="'+key+'"]').text('ON');	
        time = (time + 1) % 24;
        if(time == 0){
        	week = (week+1) % 7;
        }
        if(endweek == week && endtime == time){
        	flag = false;
        }
	}
	
	$('#start_weekday').val('0').attr('selected', 'selected');
	$('#start_time').val('0').attr('selected', 'selected');
	$('#end_weekday').val('0').attr('selected', 'selected');
	$('#end_time').val('0').attr('selected', 'selected');
	
}

$('#searchLocation').keyup(function(e) {
	if (e.keyCode == 13) {
	    $("#btnSearch").click();
    }        
});

$("#btnSearch").click(function(e){
	var postData = {name : $('#searchLocation').val()};
	$("#targetGrid").setGridParam({url:"<%=request.getContextPath()%>/search/getProfile", postData : postData, datatype:"json" }).trigger("reloadGrid");
})


// csv 다운
$("#btnGridCsvDown").click(function(){
	
	var workbook = new ExcelJS.Workbook();
	var worksheet = workbook.addWorksheet("개인정보유형");
	
	var gridData = $("#targetGrid").jqGrid('getRowData');
	var headers = [];
	
	var pattern = '${pattern}'.split('[{').join('').split('}]').join('');
		pattern = pattern.split('}, {');
	 
	var patternCnt = '${patternCnt}';
	
	headers.push({header: '개인정보유형명', key: 'DATATYPE_LABEL', width: 40, style: { font: {size: 10}, alignment: {horizontal: 'center'}}});
	headers.push({header: '이미지 검사', 	key: 'OCR', width: 10, style: { font: {size: 10}, alignment: {horizontal: 'center'}}});
	headers.push({header: '증분검사', 		key: 'RECENT', width: 10, style: { font: {size: 10}, alignment: {horizontal: 'center'}}});
	headers.push({header: '확장자', 		key: 'EXTENSION', width: 10, style: { font: {size: 10}, alignment: {horizontal: 'center'}}});
	
	for(var p=0; p < patternCnt ; p++){
		var row = pattern[p].split(', ');
		var ID = row[0].split('ID=').join(''); //변수 TABLE_NAME은 TB_NAME1, 다음 반복문에서는 TB_NAME2
		var PATTERN_NAME = row[1].split('PATTERN_NAME=').join(''); // 변수 CHK Y, 다음 반복문에서는 N
		var data_id = PATTERN_NAME.split('=')[1];

		headers.push({header: ID.split('=')[1], key: data_id+"_CNT", width: 10, style: { font: {size: 10}, alignment: {horizontal: 'center'}}});
		headers.push({header: ID.split('=')[1]+"1", key: data_id+"_DUP", width: 10, style: { font: {size: 10}, alignment: {horizontal: 'center'}}});
     }
	
	worksheet.columns = headers;
	
	var excelRowData = {};
	var excelRow = [];
	
	for(var p=0; p < patternCnt ; p++){
		var row = pattern[p].split(', ');
		var ID = row[0].split('ID=').join(''); //변수 TABLE_NAME은 TB_NAME1, 다음 반복문에서는 TB_NAME2
		var PATTERN_NAME = row[1].split('PATTERN_NAME=').join(''); // 변수 CHK Y, 다음 반복문에서는 N
		var data_id = PATTERN_NAME.split('=')[1];
 		
		excelRowData[data_id+"_CNT"] = "임계치";
		excelRowData[data_id+"_DUP"] = "중복검사";
	}
	excelRow.push(excelRowData);
	worksheet.addRows(excelRow);
	
	gridData.forEach(function(row) {
		
		for(var p=0; p < patternCnt ; p++){
			var rows = pattern[p].split(', ');
			var ID = rows[0].split('ID=').join(''); //변수 TABLE_NAME은 TB_NAME1, 다음 반복문에서는 TB_NAME2
			var PATTERN_NAME = rows[1].split('PATTERN_NAME=').join(''); // 변수 CHK Y, 다음 반복문에서는 N
			var data_id = PATTERN_NAME.split('=')[1];
			
			var data_cnt = data_id+"_CNT";
			var data_dup = data_id+"_DUP";
			
			if(row[data_cnt] == null || row[data_cnt] == "") row[data_cnt] = 0;
			
			if(row[data_dup] == 1){
				row[data_dup] = "O";
			}else {
				row[data_dup] = "X";
			}
		}
		
		console.log(row["OCR"]);
		if(row["OCR"] == "1"){
			row["OCR"] ="O";
			row["OCR_CHK"] ="O";
		}else{
			row["OCR"] ="X";
			row["OCR_CHK"] ="X"
		}
		 
		worksheet.addRow(row);
	});

	worksheet.mergeCells(1,1,2,1); 
	worksheet.mergeCells(1,2,2,2);
	worksheet.mergeCells(1,3,2,3);
	worksheet.mergeCells(1,4,2,4);
	
	for(var p=5; p < headers.length ; p+=2){
		worksheet.mergeCells(1,p,1,p+1);
	}
	 
	worksheet.getRow(1).font = {
		size: 10,
        bold: true
    };
	worksheet.getRow(2).font = { 
		size: 10,
        bold: true
    };
	worksheet.getColumn(1).font = {
		size: 10,
        bold: true
    }; 
	
	worksheet.getCell('A1').alignment = { horizontal: 'center', vertical: 'middle' };
	worksheet.getCell('B1').alignment = { horizontal: 'center', vertical: 'middle' };
	worksheet.getCell('C1').alignment = { horizontal: 'center', vertical: 'middle' };
	worksheet.getCell('D1').alignment = { horizontal: 'center', vertical: 'middle' };
	
	
	workbook.xlsx.writeBuffer().then(function(buffer) {
        saveAs(new Blob([buffer], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"}), "PICenter_개인정보유형.xlsx");
    });
	
});

</script>
</body>

</html>