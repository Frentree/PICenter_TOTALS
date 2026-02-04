<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% response.setHeader("Pragma", "no-cache"); response.setHeader("Cache-Control", "no-cache"); response.setHeader("Cache-Control", "no-store"); response.setDateHeader("Expires", 0L); %>
<!DOCTYPE html>
<html lang="ko">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta charset="utf-8" />
<!-- <title>pimc.sktelecom.com</title> -->
<title>${pic_version.header_txt}</title>

<link rel="icon" href="data:,">

<link href="${pageContext.request.contextPath}/resources/assets/css/ui.fancytree-sktPIC.css" rel="stylesheet" type="text/css" /> 
<link href="${pageContext.request.contextPath}/resources/assets/css/font-awesome.min.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/jquery-3.7.1.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/js/jquery-ui.js" type="text/javascript"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/tree/jquery.fancytree-all-deps.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/common.js"></script>
<!-- Publish JS -->

<!-- AG Grid JavaScript -->
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/ag-grid-community.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/echart/echarts-all.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/echart/echarts.min.js"></script>


<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/jstree.min.js"></script>


<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/resources/assets/css/jquery-ui-sktPIC.css" />
<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/resources/assets/css/ui.jqgrid-sktPIC.css" />



<!-- Publish CSS -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/assets/css/reset-sktPIC.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/assets/css/design-sktPIC.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/assets/css/style.min.css" />



<!-- AG Grid CSS -->
<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/resources/assets/css/ag-grid.css" />
<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/resources/assets/css/ag-theme-balham.css" />

<style>
#questionIcon:hover{
	cursor: pointer;
}
@media screen and (-ms-high-contrast: active) , ( -ms-high-contrast : none) {
	.popup_tbl{
		table-layout: fixed;
	}
}

/* #sessionUpdate{
	cursor:pointer; 
	border: 0px solid window;
	background-color: window;
	color : blue;
	text-decoration: underline;
	text-underline-position:under
} */
</style>
</head>

<script type="text/javascript">

var isCreateUser = true;
var userGridApi = null;

var passwordRules = /(?=.*\d{1,50})(?=.*[~`!@#$%\^&*()-+=]{1,50})(?=.*[a-zA-Z]{1,50}).{8,50}$/;
var phoneRules = /^\d{3}\d{3,4}\d{4}$/;
var idRules = /^[a-z|A-Z|0-9|]+$/;
var emailRules = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/;
var popList = [];

$(document).ready(function () {
	
	window.onbeforeunload = function() {
		for(i=0 ; i < popList.length ; i++){
    		popList[i].close();
         }
	};
	
	
    var ntime = performance.timing;
    var request = ntime.responseStart - ntime.requestStart; // 요청 소요 시간
    
	Loading();	
// 	setTimeout("closeLoading()", request);
	setTimeout(closeLoading, request);
	
	// 로그인 사용자 정보
	var user_upper = "${memberInfo.USER_NO}".toUpperCase();
	
	$("#user_Information").text("${memberInfo.USER_NAME}("+user_upper+")");

	var list = $(".gnb_menu li a");
	list.each(function(i, element) {
		$(element).removeClass("on")
	});

	if ("#${menuKey}".length != 1) {
		$("#${menuKey}").addClass('on');
		$("#${menuKey}").removeAttr("href")
	};	
	
	$("#btnLogout").on("click", function(e) {
		Logout();
	});
	$("#btnPopClose").on("click", function(e) {
		Logout();
	});
	/* $("#btnsessionClose").on("click", function(e) {
		$("#sessionUpdatePop").hide();
	}); */
	function Logout(){
		var postData = {};
		$.ajax({
			type: "POST",
			url: "/logout",
			async : false,
			data : postData,
		    success: function (resultMap) {
		    	window.location = "${pageContext.request.contextPath}/";
		    },
		    error: function (request, status, error) {
				alert("ERROR : " + error);
		        console.log("ERROR : ", error);
		    }
		});
		
		for(i=0 ; i < popList.length ; i++){
    		popList[i].close();
         }
	};
	
	// 사용자이동 button click event
	$("#btnMoveUserPage").click(function() {
		// 사용자 MainPage 이동
		document.location.href = "<%=request.getContextPath()%>/detection/pi_detection_regist";
	});
	
	$("nav > ul > li").mouseenter(function(){
		$(this).children(".gnb_sub_menu_dash").stop().slideDown(400);
		$(this).children(".gnb_sub_menu_policy").stop().slideDown(400);
	    $(this).children(".gnb_sub_menu_target").stop().slideDown(400);
	    $(this).children(".gnb_sub_menu_search").stop().slideDown(400);
	    $(this).children(".gnb_sub_menu_detection").stop().slideDown(400);
	    $(this).children(".gnb_sub_menu_community").stop().slideDown(400);
	    $(this).children(".gnb_sub_menu_user").stop().slideDown(400);
	});
	
	$("nav > ul > li").mouseleave(function(){
		$(this).children(".gnb_sub_menu_dash").stop().slideUp(200);
	    $(this).children(".gnb_sub_menu_policy").stop().slideUp(200);
	    $(this).children(".gnb_sub_menu_target").stop().slideUp(200);
	    $(this).children(".gnb_sub_menu_search").stop().slideUp(200);
	    $(this).children(".gnb_sub_menu_detection").stop().slideUp(200);
	    $(this).children(".gnb_sub_menu_community").stop().slideUp(200);
	    $(this).children(".gnb_sub_menu_user").stop().slideUp(200);
	});
	
	
	var changeUserSettingNM = "";
	var changeUserSettingpassword = "";
	var checkUserSettingpassword = "";
	
	function fn_drawUserGrid() {
		var columnDefs = [
			{
				headerName: '아이디',
				field: 'USER_NO',
				flex: 1,
				cellStyle: { textAlign: 'center' },
				filterType: 'string',
			    editable: function(params) {
			        return params.data.isNewRow; 
			    }
			},
			{
				headerName: '사용자명',
				field: 'USER_NM',
				flex: 1,
				cellStyle: { textAlign: 'center' },
				filterType: 'string',
			    editable: function(params) {
			        return params.data.isNewRow;
			    }
			},
			{
				headerName: '마지막 패스워드 변경일',
				field: 'PWD_UPT_DT',
				flex: 1.55,
				cellStyle: { textAlign: 'center' },
				filterType: 'date'
			},
			{
				headerName: '마지막 접속 시간',
				field: 'LOGINDATE',
				flex: 1.3,
				cellStyle: { textAlign: 'center' },
				filterType: 'date'
			},
			{
				headerName: '계정 잠금 여부',
				field: 'LOCK',
				flex: 1.15,
				cellStyle: { textAlign: 'center' },
				cellRenderer: lockStatusRenderer,
				filterType: 'string'
			},
			{
				headerName: '비밀번호 초기화',
				field: 'PASSWORD_REST',
				flex: 1.1,
				cellStyle: { textAlign: 'center' },
				cellRenderer: passwordResetRenderer,
				sortable: false,
				filter: false
			},
			{
				headerName: '',
				field: 'BUTTON',
				width: 150,
				cellStyle: { textAlign: 'center' },
				cellRenderer: buttonRenderer,
				sortable: false,
				filter: false
			}
		];

		// Grid 옵션 설정
		var gridOptions = {
			theme: 'legacy',
			columnDefs: applyColumnFilters(columnDefs),
			localeText: getAgGridKoreanLocale(),
			components: {
				booleanFilter: BooleanFilter
			},
			defaultColDef: {
				sortable: true,
				filter: true,
				resizable: true,
				cellStyle: { textAlign: 'center' },
				headerClass: 'ag-header-cell-center'
			},
		    localeText: {
		        ...getAgGridKoreanLocale(),
		        page: '',
		        of: '/',
		        to: '-',
		    },
			pagination: true,
			paginationPageSize: 10,
			paginationPageSizeSelector: [10, 20, 50],
			rowHeight: 35,
			headerHeight: 40,
			animateRows: true,
			
			onGridReady: function(params) {
				userGridApi = params.api;
			}
		};
		
		// AG Grid 생성
		var gridDiv = document.getElementById('userGrid');
		userGridApi = agGrid.createGrid(gridDiv, gridOptions);
	}

	
	$("#user_setting").click(function () {
	    if (!userGridApi) {
	        fn_drawUserGrid();
	    }
		// 데이터 로드
		$.ajax({
		    url: "<%=request.getContextPath()%>/user/selectCreateUser",
		    type: "POST",
		    data: {},
		    dataType: "json",
		    success: function(response) {
		        // ERROR 필드 체크
		        if (response.length > 0 && response[0].ERROR) {
		            alert(response[0].ERROR);
		            if (response[0].ERROR == "로그인 정보가 없습니다.") {
		                location.href = "${pageContext.request.contextPath}/";
		            }
		            return;
		        }
		        
		        if (userGridApi && Array.isArray(response)) {
		            userGridApi.setGridOption('rowData', response);
		    		$("#userSettingDatePopup").show();
		        }
		    },
		    error: function(xhr, status, error) {
		        console.error("데이터 로드 실패:", error);
		    }
		});
	
	});
	
	$("#btnUserSettingDateCancel, #btnCancleUserSettingDatePopup").click(function () {
	    // 생성 중인 행이 있다면 원본 데이터로 복원
	    if (!isCreateUser) {
	        $.ajax({
	            url: "<%=request.getContextPath()%>/user/selectCreateUser",
	            type: "POST",
	            data: {},
	            dataType: "json",
	            success: function(response) {
	                if (userGridApi && Array.isArray(response)) {
	                    userGridApi.setGridOption('rowData', response);
	                }
	            }
	        });
	    }
	    
	    isCreateUser = true;
	    
	    $("#changeUserSettingNM").val("${memberInfo.USER_NAME}");
	    $("#changeUserSettingpassword").val("");
	    $("#checkUserSettingpassword").val("");
	    
	    $("#userSettingDatePopup").hide();
	});
	
	$("#credateUser").click(function () {
	    
	    if(!isCreateUser){
	        alert("생성 중인 계정이 존재합니다.");
	        return;
	    }
	    
	    isCreateUser = false;
	    
	    // AG Grid에서 현재 데이터 가져오기
	    var currentData = [];
	    userGridApi.forEachNode(function(node) {
	        currentData.push(node.data);
	    });
	    
	    // 새로운 행 데이터 생성
	    var newRowData = {
	        USER_NO: '',
	        USER_NM: '',
	        PWD_UPT_DT: '',
	        LOGINDATE: '',
	        LOCK: '',
	        PASSWORD_REST: '',
	        BUTTON: 'CREATE_MODE',
	        isNewRow: true
	    };
	    
	    // 새로운 데이터를 맨 아래에 추가
	    var updatedData = [...currentData, newRowData];
	    userGridApi.setGridOption('rowData', updatedData);
	    
	    // 새로운 행의 첫 번째 셀(아이디)을 편집 모드로 시작
	    setTimeout(function() {
	        var newRowIndex = updatedData.length - 1;
	        userGridApi.startEditingCell({
	            rowIndex: newRowIndex,
	            colKey: 'USER_NO'
	        });
	    }, 100);
	});
	
	/* 로그인 정책 설정 Function Start */
	// 팝업 실행
	$("#changePassChange").change(function (){
		if($(this).is(":checked")){
    		$("#changePassChangeDay").prop("disabled", false);
		} else {
    		$("#changePassChangeDay").prop("disabled", true);
		}
	});
	
	$("#changePassUse").change(function (){
		if($(this).is(":checked")){
    		$("#changePassUseDay").prop("disabled", false);
		} else {
    		$("#changePassUseDay").prop("disabled", true);
		}
	});
	
	$("#changePassLock").change(function (){
		if($(this).is(":checked")){
    		$("#changePassLockCnt").prop("disabled", false);
		} else {
    		$("#changePassLockCnt").prop("disabled", true);
		}
	});
	
	$("#createLoginPolicy").click(function () { 
		$.ajax({
			type: "POST",
			url: "/user/selectAccountPolicy",
			async : false,
			data : {}, 
		    success: function (resultMap) {
		    	if(resultMap.enable == 1) {
		    		$("#changePassEnable").prop("checked", true);
		    		if(resultMap.changes == 1) {
			    		$("#changePassChange").prop("checked", true);
			    		$("#changePassChangeDay").val(resultMap.change_val);
			    	} else {
			    		$("#changePassChange").prop("checked", false);
			    		$("#changePassChangeDay").val(0);
			    		$("#changePassChangeDay").prop("disabled", true);
			    	}
			    	
			    	if(resultMap.uses == 1) {
			    		$("#changePassUse").prop("checked", true);
			    		$("#changePassUseDay").val(resultMap.use_val);
			    	} else {
			    		$("#changePassUse").prop("checked", false);
			    		$("#changePassUseDay").val(0); 
			    		$("#changePassUseDay").prop("disabled", true);
			    	}
			    	
			    	if(resultMap.locks == 1) {
			    		$("#changePassLock").prop("checked", true);
			    		$("#changePassLockCnt").val(resultMap.lock_val);
			    	} else {
			    		$("#changePassLock").prop("checked", false);
			    		$("#changePassLockCnt").val(0);
			    		$("#changePassLockCnt").prop("disabled", true);
			    	}
			    	
			    	if(resultMap.include == 1) {
			    		$("#includePassID").prop("checked", true);
			    	} else {
			    		$("#includePassID").prop("checked", false);
			    	}
		    	} else {
		    		$("#changePassEnable").prop("checked", false);
		    		
		    		$("#changePassChange").prop("checked", false);
		    		$("#changePassChange").prop("disabled", true);
		    		$("#changePassChangeDay").val(0);
		    		$("#changePassChangeDay").prop("disabled", true);
		    		
		    		$("#changePassUse").prop("checked", false);
		    		$("#changePassUse").prop("disabled", true);
		    		$("#changePassUseDay").val(0); 
		    		$("#changePassUseDay").prop("disabled", true);
		    		
		    		$("#changePassLock").prop("checked", false);
		    		$("#changePassLock").prop("disabled", true);
		    		$("#changePassLockCnt").val(0);
		    		$("#changePassLockCnt").prop("disabled", true);
		    		
		    		$("#includePassID").prop("checked", false);
		    		$("#includePassID").prop("disabled", true);
		    	} 
		    	
				$("#passwordSettingDatePopup").show();
				
				$("#changePassEnable").change(function(){
					if($("#changePassEnable").is(":checked")) {
						if(resultMap.changes == 1) {
							$("#changePassChange").prop("checked", true);
							$("#changePassChange").prop("disabled", false);
				    		$("#changePassChangeDay").val(resultMap.change_val);
				    		$("#changePassChangeDay").prop("disabled", false);
						}else {
							$("#changePassChange").prop("checked", false);
							$("#changePassChange").prop("disabled", false);
				    		$("#changePassChangeDay").val(0);
				    		$("#changePassChangeDay").prop("disabled", true);
						}
						
						if(resultMap.uses == 1) {
				    		$("#changePassUse").prop("checked", true);
				    		$("#changePassUse").prop("disabled", false);
				    		$("#changePassUseDay").val(resultMap.use_val);
				    		$("#changePassUseDay").prop("disabled", false);
				    	} else {
				    		$("#changePassUse").prop("checked", false);
				    		$("#changePassUse").prop("disabled", false);
				    		$("#changePassUseDay").val(0); 
				    		$("#changePassUseDay").prop("disabled", true);
				    	}
						
						if(resultMap.locks == 1) {
				    		$("#changePassLock").prop("checked", true);
				    		$("#changePassLock").prop("disabled", false);
				    		$("#changePassLockCnt").val(resultMap.lock_val);
				    		$("#changePassLockCnt").prop("disabled", false);
				    	} else {
				    		$("#changePassLock").prop("checked", false);
				    		$("#changePassLock").prop("disabled", false);
				    		$("#changePassLockCnt").val(0);
				    		$("#changePassLockCnt").prop("disabled", true);
				    	}
						
						if(resultMap.include == 1) {
				    		$("#includePassID").prop("checked", true);
				    		$("#includePassID").prop("disabled", false);
				    	} else {
				    		$("#includePassID").prop("checked", false);
				    		$("#includePassID").prop("disabled", false);
				    	}
			    		
					}else {
						$("#changePassChange").prop("checked", false);
						$("#changePassChange").prop("disabled", true);
			    		$("#changePassChangeDay").val(0);
			    		$("#changePassChangeDay").prop("disabled", true);
			    		
			    		$("#changePassUse").prop("checked", false);
			    		$("#changePassUse").prop("disabled", true);
			    		$("#changePassUseDay").val(0); 
			    		$("#changePassUseDay").prop("disabled", true);
			    		
			    		$("#changePassLock").prop("checked", false);
			    		$("#changePassLock").prop("disabled", true);
			    		$("#changePassLockCnt").val(0);
			    		$("#changePassLockCnt").prop("disabled", true);
			    		
			    		$("#includePassID").prop("checked", false);
			    		$("#includePassID").prop("disabled", true);
					}
				});
		    },
		    error: function (request, status, error) { 
	    		alert("로그인 정책을 불러오는데 실패하였습니다.");
		        console.log("ERROR : ", error);
		    }
		});
		
	});
	
	// 팝업 종료
	$("#btnCanclePassSettingDatePopup, #btnPassSettingDateCancel").click(function () {
		$("#passwordSettingDatePopup").hide();		
	});
	
	// 저장
	$("#btnPassSettingDateSave").click(function () { 
		var enable = $("#changePassEnable").is(":checked");
		
		var change = $("#changePassChange").is(":checked");
		var changeVal = $("#changePassChangeDay").val();
		
		var use = $("#changePassUse").is(":checked");
		var useVal = $("#changePassUseDay").val();
		
		var lock = $("#changePassLock").is(":checked");
		var lockVal = $("#changePassLockCnt").val();
		
		var include = $("#includePassID").is(":checked");
		
		var postData = {
			"enable" : enable ? 1 : 0,
			"change" : change ? 1 : 0,
			"changeVal" : change ? changeVal : 0,
			"use" : use ? 1 : 0,
			"useVal" : use ? useVal : 0,
			"lock" : lock ? 1 : 0,
			"lockVal" : lock ? lockVal : 0,
			"include" : include ? 1 : 0,
		};
		
		$.ajax({
			type: "POST",
			url: "/user/saveAccountPolicy",
			async : false,
			data : postData,
		    success: function (resultMap) {
		    	console.log(resultMap);
		    	if(resultMap.resultCode == 200){
		    		alert("로그인 정책이 설정되었습니다.");
		    		$("#passwordSettingDatePopup").hide();
		    	} else {
		    		alert("로그인 정책 설정이 실패하였습니다.");
		    		return;
		    	}
		    	
		    },
		    error: function (request, status, error) { 
	    		alert("로그인 정책 설정이 실패하였습니다.");
		        console.log("ERROR : ", error);
		    }
		});
		
	});
	
	/* 로그인 정책 설정 Function End */
	
	$("#btnUserSettingDateSave").click(function () {
		
		changeUserSettingNM = $("#changeUserSettingNM").val().trim();
		changeUserSettingpassword = $("#changeUserSettingpassword").val().trim();
		checkUserSettingpassword = $("#checkUserSettingpassword").val().trim();
		
		$("#changeUserSettingNM").change(function () {
			if (isNull(changeUserSettingNM)) {
				$("#changeUserSettingNM").focus();
				alert("이름을 입력하십시오.");
				return;
			}
		});
		
		if (!isNull(changeUserSettingpassword) || !isNull(checkUserSettingpassword) ) {
			if (changeUserSettingpassword != checkUserSettingpassword) {
				$("#checkUserSettingpassword").focus();
				alert("비밀번호와 비밀번호확인이 일치하지 않습니다.");
				return;
			}
	
			if (!passwordRules.test(changeUserSettingpassword)) {
				$("#changeUserSettingpassword").focus();
				alert("비밀번호는 숫자/영문자/특수문자를 1개 이상, 8자 이상입력하십시오.");
				return;
			}
		}
		
		var postData = {
			changeUserSettingNM : changeUserSettingNM,
			changeUserSettingpassword : changeUserSettingpassword,
			checkUserSettingpassword : checkUserSettingpassword
		};
		
		$.ajax({
			type: "POST",
			url: "/user/userSetting",
			async : false,
			data : postData,
		    success: function (resultMap) {
		    	console.log(resultMap);
		    	if(resultMap.resultCode == -1){
		    		alert(resultMap.resultMessage);
		    		return;
		    	}
		    	
		    	if(resultMap.resultCode == 12){
		    		alert("수정이 불가한 계정입니다.");
		    		return;
		    	}
		    	var postData = {};
		    	
		    	if(changeUserSettingpassword != "" && checkUserSettingpassword != ""){
		    		
	                alert("사용자 정보가 수정 되었습니다. 로그인을 다시 진행해주세요.");
		    		$.ajax({
						type: "POST",
						url: "/logout",
						async : false,
						data : postData,
					    success: function (resultMap) {
					    	window.location = "${pageContext.request.contextPath}/";
					    },
					    error: function (request, status, error) {
							alert("ERROR : " + error);
					        console.log("ERROR : ", error);
					    }
					});
		    		
		    	}else {
			    	alert("사용자 정보가 수정 되었습니다.");
			    	location.reload();
		    	}
		    	
		    },
		    error: function (request, status, error) { 
		    	alert("사용자 정보 수정 실패 : " + error);
		        console.log("ERROR : ", error);
		    }
		});
	});
	
	var questionIconHover = document.getElementById('questionIcon');
	
	questionIconHover.addEventListener("mouseover", function () {
		$("#passwordQuestion").show();
	});
	
	questionIconHover.addEventListener("mouseout", function () {
		$("#passwordQuestion").hide();
	});
	
	$(window).scroll(function(){
	    $('#header').css({left: 0 - $(this).scrollLeft()});
   	})

   	$("#changeUser").click(function() {
   		userListWindows("changeUser");
   	});
	
	$("#btnCancleUserSettingDatePopup").on("click", function(e) {
		isCreateUser = true	
		
		$("#changeUserSettingNM").val("${memberInfo.USER_NAME}");
		$("#changeUserSettingpassword").val("");
		$("#checkUserSettingpassword").val("");
		
		$("#userSettingDatePopup").hide();
	});
	
	$("#btnCanclePopClose").on("click", function(e) {
		Logout();
	});
   	
});

function lockStatusRenderer(params) {
    var value = params.value;
    if (value == "N") {
        return "활성화";
    } else if (value == "Y") {
        return "비활성화";
    } else {
        return " ";
    }
}

// 비밀번호 초기화 버튼 렌더러
function passwordResetRenderer(params) {
    var userNo = params.data.USER_NO;
    
    if (userNo == "frentree") {
        return " ";
    }
    
    if (!params.value || params.value == "") {
    	return '<div class="btn_area" style="text-align: center; margin: 0px; padding: 0px;"> <button type="button" class="gridSubSelBtn" style="vertical-align: inherit;" onclick="updateUser(\'' + userNo + '\', \'pwd_reset\')">초기화</button></div>';
    } else {
        return " ";
    }
}

// 버튼 렌더러 (삭제/생성/취소)
function buttonRenderer(params) {
    var userNo = params.data.USER_NO;
    
    if (userNo == "frentree") {
        return " ";
    }
    
    // 새로운 행인 경우
    if (params.data.isNewRow || params.data.BUTTON === 'CREATE_MODE') {
        return '<div class="btn_area" style="text-align: center; margin: 0px; padding: 0px;">' +
               '<button type="button" class="gridSubSelBtn" style="vertical-align: inherit; margin-right: 5px;" onclick="createNewUser(' + params.node.rowIndex + ')">생성</button>' +
               '<button type="button" class="gridSubSelBtn" style="vertical-align: inherit;" onclick="cancelNewUser(' + params.node.rowIndex + ')">취소</button>' +
               '</div>';
    }
    
    // 일반 행인 경우
    return '<div class="btn_area" style="text-align: center; margin: 0px; padding: 0px;">' +
           '<button type="button" class="gridSubSelBtn" style="vertical-align: inherit;" onclick="updateUser(\'' + userNo + '\', \'delete_user\')">삭제</button>' +
           '</div>';
}
function popClose(){
	$("#popClose").show();
};

function updateUser(user_no, type){
	
	var postData = {"userNo" : user_no};
	
	if(type == "pwd_reset"){ // 초기화
		
		var chk = confirm("\"" +user_no +"\"의 비밀번호를 초기화 하시겠습니까?");
		if(chk){
			$.ajax({
				type: "POST",
				url: "/user/pwd_reset",
				async : false,
				data : postData,
			    success: function (resultMap) {
			    	alert("해당 계정의 비밀번호가 초기화 되었습니다.");
			    	
			    	// AG Grid 데이터 새로고침
			    	$.ajax({
						url: "<%=request.getContextPath()%>/user/selectCreateUser",
						type: "POST",
						data: {},
						dataType: "json",
						success: function(response) {
							if (userGridApi && Array.isArray(response)) {
								userGridApi.setGridOption('rowData', response);
							}
						}
					});
			    },
			    error: function (request, status, error) {
					alert("ERROR : " + error);
			        console.log("ERROR : ", error);
			    }
			}); 
		}
	}else if(type == "delete_user"){ // 계정 삭제
		
		var chk = confirm("\"" +user_no +"\" 를 삭제하시겠습니까?");
		
		if(chk){
			$.ajax({
				type: "POST",
				url: "/user/userDelete",
				async : false,
				data : postData,
			    success: function (resultMap) {
			    	alert("계정이 삭제되었습니다.");
			    	
			    	$.ajax({
						url: "<%=request.getContextPath()%>/user/selectCreateUser",
						type: "POST",
						data: {},
						dataType: "json",
						success: function(response) {
							if (userGridApi && Array.isArray(response)) {
								userGridApi.setGridOption('rowData', response);
							}
						}
					});
			    },
			    error: function (request, status, error) {
					alert("ERROR : " + error);
			        console.log("ERROR : ", error);
			    }
			});
		}
	}
	
};

<%-- var SetTime= <%= session.getMaxInactiveInterval() %>; --%>
<%-- <%
int setTime = (session != null) ? session.getMaxInactiveInterval() : 0;
%>
// 세션 유지시간 클라이언트 로직에 필요하여 의도적으로 노출함
// 보안 취약점 아님, 서버 세션 정책 정보 외 유저 정보 포함되지 않음
// nosec: intentional exposure of non-sensitive session timeout
var SetTime = <%= setTime %>; --%>

function Loading() {
    var maskHeight = $(document).height();
    var maskWidth  = window.document.body.clientWidth;
     
    var mask       = "<div id='mask' style='position:absolute; z-index:9000; background-color:#000000; display:none; left:0; top:0;'></div>";
    var loadingImg ='';
     
    loadingImg +=" <div id='loadingImg'>";
    loadingImg +=" <img src='${pageContext.request.contextPath}/resources/assets/images/spinner.gif' style='position:absolute; z-index:9500; text-align:center; display:block; top:650%; left:42%;'/>";
    loadingImg += "</div>";  
 
    $('body')
        .append(mask)
 
    $('#mask').css({
            'width' : maskWidth,
            'height': maskHeight,
            'opacity' :'0.3'
    });
    
    $('#mask').show();
  
    $('.container_header').append(loadingImg);
    $('#loadingImg').show();
}

function closeLoading() {
    $('#mask, #loadingImg').hide();
    $('#mask, #loadingImg').remove(); 
}

var createView1 = function(cellvalue, options, rowObject) {
	var user_no = rowObject.USER_NO;
	
	if(user_no == "frentree") {
		return " ";
	}
	
	if(cellvalue == "" || cellvalue == null){
		return "<button type='button' class='gridSubSelBtn' name='gridSubSelBtn' onclick='updateUser(\""+user_no+"\",\"pwd_reset\")'>초기화</button>"; 
	}else{
		return " "; 
	}
};
var createView2 = function(cellvalue, options, rowObject) {
	var user_no = rowObject.USER_NO;
	
	if(user_no == "frentree") {
		return " ";
	}
	
	if(cellvalue == "" || cellvalue == null){
		return "<button type='button' class='gridSubSelBtn' name='gridSubSelBtn'  onclick='updateUser(\""+user_no+"\",\"delete_user\")'>삭제</button>"; 
	}else{
		return "<button type='button' class='gridSubSelBtn' name='createNewUser'  onclick='createNewUser()'>저장</button>"
        		+"<button type='button' class='gridSubSelBtn' name='cancelNewUser' onclick='cancelNewUser()'>취소</button>";
	}
};
var locked = function(cellvalue, options, rowObject) {
	var lock = rowObject.LOCK;
	
	if(lock == "N") {
		return "활성화";
	}else if(lock == "Y") {
		return "비활성화";
	}else {
		return  " ";
	}
};
function createNewUser(rowIndex) {
    var rowNode = userGridApi.getDisplayedRowAtIndex(rowIndex);
    
    var createUserId = '';
    var createUserNM = '';
    
    var userIdInput = $('#createUserId');
    var userNMInput = $('#createUserNM');
    
    if (userIdInput) {
        createUserId = userIdInput.value.trim();
    }
    if (userNMInput) {
        createUserNM = userNMInput.value.trim();
    }
    
    if (!createUserId) {
        createUserId = rowNode.data.USER_NO ? rowNode.data.USER_NO.trim() : '';
    }
    if (!createUserNM) {
        createUserNM = rowNode.data.USER_NM ? rowNode.data.USER_NM.trim() : '';
    }
    
    // 나머지 검증 로직은 동일...
    if (isNull(createUserId)) {
        alert("아이디를 입력하십시오.");
        return;
    }
    
    var postData = {"user_no": createUserId, "user_nm": createUserNM};
    
    $.ajax({
        type: "POST",
        url: "/user/chkDuplicateUserNo",
        async: false,
        data: postData,
        success: function (resultMap) {
            var chk = resultMap.UserMap.EXISTUSERCNT;
            if(chk > 0) {
                alert("중복된 아이디 입니다.");
                return;
            } else {
                $.ajax({
                    type: "POST",
                    url: "/user/createUser",
                    async: false,
                    data: postData,
                    success: function (resultMap) {
                        alert("계정이 생성 되었습니다.");
                        cancelNewUser(rowIndex);
                    },
                    error: function (request, status, error) {
                        alert("ERROR : " + error);
                        console.log("ERROR : ", error);
                    }
                }); 
            }
        },
        error: function (request, status, error) {
            alert("ERROR : " + error);
            console.log("ERROR : ", error);
        }
    }); 
}

function cancelNewUser(rowIndex) {
    isCreateUser = true;
    
    $.ajax({
        url: "<%=request.getContextPath()%>/user/selectCreateUser",
        type: "POST",
        data: {},
        dataType: "json",
        success: function(response) {
            if (userGridApi && Array.isArray(response)) {
                userGridApi.setGridOption('rowData', response);
            }
        },
        error: function(xhr, status, error) {
            console.error("데이터 로드 실패:", error);
        }
    });
}
function createNewUser(rowIndex) {
    var rowNode = userGridApi.getDisplayedRowAtIndex(rowIndex);
    var rowData = rowNode.data;
    
    var createUserId = rowData.USER_NO ? rowData.USER_NO.trim() : '';
    var createUserNM = rowData.USER_NM ? rowData.USER_NM.trim() : '';
    
    if (isNull(createUserId)) {
        alert("아이디를 입력하십시오.");
        return;
    }
    
    if (!idRules.test(createUserId)) {
        alert("아이디는 영문/숫자만 입력가능합니다.");
        return;
    }
    
    if (isNull(createUserNM)) {
        alert("사용자명을 입력하십시오.");
        return;
    }
    
    var postData = {"user_no": createUserId, "user_nm": createUserNM};
    
    $.ajax({
        type: "POST",
        url: "/user/chkDuplicateUserNo",
        async: false,
        data: postData,
        success: function (resultMap) {
            var chk = resultMap.UserMap.EXISTUSERCNT;
            if(chk > 0) {
                alert("중복된 아이디 입니다.");
                return;
            } else {
                $.ajax({
                    type: "POST",
                    url: "/user/createUser",
                    async: false,
                    data: postData,
                    success: function (resultMap) {
                        alert("계정이 생성 되었습니다.");
                        cancelNewUser(rowIndex);
                    },
                    error: function (request, status, error) {
                        alert("ERROR : " + error);
                        console.log("ERROR : ", error);
                    }
                }); 
            }
        },
        error: function (request, status, error) {
            alert("ERROR : " + error);
            console.log("ERROR : ", error);
        }
    }); 
}


function userListWindows(info){
	
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
	
	newForm.appendChild(data);
	document.body.appendChild(newForm);
	newForm.submit();
	
	document.body.removeChild(newForm);
}

</script>

<body>
<!-- wrap -->
<div class="wrap">

	<!-- header -->
	<header id="header">
		<!-- container -->
		<div class="container_header">
			<!-- logo -->
			<div class="img_logo">
				<a href="<%=request.getContextPath()%>/"><h1>개인정보 검출관리 센터</h1></a>
				<div class="logo_text">
					<div><b style="font-size:17px;">${pic_version.header_nm}</b></div>
				<div style="position: absolute; top: 20px; font-size: 12px;">${pic_version.header_txt}</div>
				</div>
			</div>
			
			<!-- nav -->
			<nav id="nav">
				<!-- gnb -->
				<h2 class="ir">업무메뉴</h2>
			  <ul class="gnb_menu clear">
			    <c:forEach items="${headerList}" var="item" varStatus="status">
			    	<%-- <li><a href="#">${!status.first}</a></li> --%>
		    		<c:choose>
			    		<c:when test="${item.SUB_NO == 0}">
			    			<c:if test="${!status.first}">
					    		</li>
					    		</ul>
					    		</li>
					    	</c:if>
					    	<c:choose>
					    		<c:when test="${item.HEADER_NO == 1}">
					    			<li>
						    			<a href="<%=request.getContextPath()%>/" style="width: 160px;">${item.NAME}</a>
				    					<ul class="gnb_sub_menu_dash" style="background-color: rgb(255, 255, 255);">
			    					<li>
					    		</c:when>
					    		<c:when test="${item.HEADER_NO == 2}">
					    			<li>
					    			<a href="javascript:void(0);" style="width: 160px;">${item.NAME}</a>
			    					<ul class="gnb_sub_menu_policy" style="background-color: rgb(255, 255, 255);">
			    					<li>
					    		</c:when>
					    		<c:when test="${item.HEADER_NO == 3}">
					    			<li>
					    			<a href="javascript:void(0);" style="width: 160px;">${item.NAME}</a>
			    					<ul class="gnb_sub_menu_target" style="background-color: rgb(255, 255, 255);">
			    					<li>
					    		</c:when>
					    		<c:when test="${item.HEADER_NO == 4}">
					    			<li>
					    			<a href="javascript:void(0);" style="width: 160px;">${item.NAME}</a>
			    					<ul class="gnb_sub_menu_target" style="background-color: rgb(255, 255, 255);">
			    					<li>
					    		</c:when>
					    	</c:choose>
			    		</c:when>
			    		<c:otherwise>
			    			<a href="${item.URL}" >${item.NAME}</a>
			    		</c:otherwise>
		    		</c:choose>
		    		<c:if test="${status.end}">
			    		</li>
			    		</ul>
			    		</li>
			    	</c:if>
			    </c:forEach>
			  </ul>
			</nav>
			<!-- nav -->
			<div class="user">
				
					<!-- <p class="memberInfo">IT 보안모듈 홍길동 과장</p> -->
					<!-- <p class="memberInfo" style="float: left; margin-left: 40px; font-size: 12px;" id="sessionTimeCheck">(30:00)</p> -->
					<c:if test="${memberInfo.USER_GRADE == 9}">
					
						<div class="member_area" style="right: 25px; width: 300px;">
							<p class="memberInfo" style="float: right; margin-right: 25px; font-size: 12px;">
		                    <img class="logOutImg Logout" src="${pageContext.request.contextPath}/resources/assets/images/setting_icon.png" style="top: -1px; right: -6px;" id="user_setting"  title="사용자 설정 및 수정">
							<p class="memberInfo" style="float: right; margin-right: 25px; font-size: 12px;" id="user_Information"></p>
						</div>	
						
						<!-- 
						<div class="member_area" style="right: 10px; top: -5px;">
								<button id="user_setting" style="vertical-align: top;">사용자 정보</button>
							</p><br>
							<p class="memberInfo" style="float: right; margin-right: 25px; font-size: 12px;" id="user_Information"></p>
						</div> -->
					</c:if>
					<c:if test="${memberInfo.USER_GRADE != 9}">
						<div class="member_area" style="right: 25px;">
							<p class="memberInfo" style="float: right; margin-right: 25px; font-size: 12px;" id="user_Information"></p>
						</div>	
					</c:if>
					<%-- <p class="memberInfo" style="float: right; margin-right: 15px;" id="user_Information">${memberInfo.USER_NAME}(${memberInfo.USER_NO})</p> --%>
				<img class="logOutImg Logout" src="${pageContext.request.contextPath}/resources/assets/images/logout.png" id="btnLogout" title="Log-Out">
				<!-- 보안관리자(9), 서비스운영자/관리자(4,5), 서버운영자(6), 직책자(7)  -->
			</div>
		</div>
		
		<!-- container -->
	</header>
	<!-- header -->
	
<div id="userSettingDatePopup" class="popup_layer" style="display:none">
	<div class="popup_box" style="height: 390px; width: 935px; padding: 10px; background: #f9f9f9; top: 53%; left: 42%;">
		<img class="CancleImg" id="btnCancleUserSettingDatePopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 style="color: #222; padding: 0; box-shadow: none;">사용자 설정 및 수정</h1>
		</div>
		<div class="popup_content">
			<div class="content-box" id="div_update_user" style="height: 418px; background: #fff; border: 1px solid #c8ced3;">
				<table class="popup_tbl">
					<colgroup>
						<col width="20%">
						<col width="30%">
						<col width="20%">
						<col width="30%">
					</colgroup>
					<tbody>
						 <tr>
							<th>사용자 ID</th>
							<td>${memberInfo.USER_NO}
							</td>
							<th>사용자명</th>
							<td>	
								<input type="text" id="changeUserSettingNM" style="width: 175px; padding-left:10px; text-align: left;" value="${memberInfo.USER_NAME}" >
							</td>
						</tr>
						<tr>
							<th>
								비밀번호변경
								<img alt="" src="${pageContext.request.contextPath}/resources/assets/images/question_icon.png" style="width: 15px; position: absolute; top: 114px; left: 142px;" id="questionIcon">
							</th>
							<td>	
								<input type="password" id="changeUserSettingpassword" style="width: 175px; padding-left:10px; text-align: left;" value="" >
								
								<div id="passwordQuestion"style="height: 62px; width: 222px;padding: 10px;background: rgb(255,255,255); box-shadow: rgb(221 221 221) 2px 2px 5px;
																 border: 1px solid #cdcdcd; top: 41%; left: -3px; position: absolute; z-index: 999; display: none;">
									<p style="color: #55555;">
										 - 8자리 이상 <br>
										 - 숫자/영문자/특수문자를 1개 이상
									</p>
								</div>
							</td>
							<th>비밀번호확인</th>
							<td>	
								<input type="password" id="checkUserSettingpassword" style="width: 175px; padding-left:10px; text-align: left;" value="" >
							</td>
						</tr>
					</tbody>
				</table>
				<div class="left_box2" id="userGridBox" style="overflow: hidden; height: 306px; padding-top: 15px;">
					<div class="btn_area" style="padding: 10px 0; margin: 0;">
						<button type="button" id="createLoginPolicy">로그인 정책 설정</button> 
						<button type="button" id="credateUser">생성</button>
					</div>
					<div id="userGrid" class="ag-theme-balham" style="height: 245px; width: 100%;"></div>
   				</div>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area" style="padding: 10px 0; margin: 0;">
				<button type="button" id="btnUserSettingDateSave">저장</button>
				<button type="button" id="btnUserSettingDateCancel">취소</button>
			</div>
		</div>
	</div>
</div>

<div id="passwordSettingDatePopup" class="popup_layer" style="display:none">
	<div class="popup_box" style="height: 320px; width: 400px; padding: 10px; background: #f9f9f9; top: 63%; left: 55%;">
		<img class="CancleImg" id="btnCanclePassSettingDatePopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 style="color: #222; padding: 0; box-shadow: none;">로그인 정책 설정</h1>
		</div>
		<div class="popup_content">
			<div class="content-box" id="div_update_user" style="height: 240px; background: #fff; border: 1px solid #c8ced3;">
				<table class="popup_tbl">
					<colgroup>
						<col width="25%">
						<col width="30%">
					</colgroup>
					<tbody>
						<tr>
							<th>활성화 여부</th>
							<td>
								<input type="checkbox" id="changePassEnable" style="padding-left:10px; text-align: left;">
							</td>
						</tr>
						<tr>
							<th>패스워드 변경일</th>  
							<td>
								<input type="checkbox" id="changePassChange" style="padding-left:10px; text-align: left;" checked="checked">&nbsp;
								<input type="number" id="changePassChangeDay" style="width:60px; padding-left:10px; text-align: left;" value="" size="10">&nbsp;&nbsp;일 
							</td>
						</tr>
						<tr>
							<th>장기 미사용 계정 잠김</th>  
							<td>
								<input type="checkbox" id="changePassUse" style="padding-left:10px; text-align: left;" checked="checked">&nbsp;
								<input type="number" id="changePassUseDay" style="width:60px; padding-left:10px; text-align: left;" value="" size="10">&nbsp;&nbsp;일 
							</td>
						</tr>
						<tr>
							<th>계정 잠김 <br>(패스워드 틀린 횟수)</th>  
							<td>
								<input type="checkbox" id="changePassLock" style="padding-left:10px; text-align: left;" checked="checked">&nbsp;
								<input type="number" id="changePassLockCnt" style="width:60px; padding-left:10px; text-align: left;" value="" size="10">&nbsp;&nbsp;회
							</td>
						</tr>
						<!-- <tr>
							<th>패스워드 중복<br>(이전 패스워드 중복 사용 횟수)</th>  
							<td>
								<input type="checkbox" id="changePassDupl" style="padding-left:10px; text-align: left;" checked="checked">&nbsp;
								<input type="number" id="changePassDuplCnt" style="width:60px; padding-left:10px; text-align: left;" value="" size="10">&nbsp;&nbsp;회
							</td>
						</tr> -->
						<tr>
							<th>패스워드 계정 미포함</th>  
							<td>
								<input type="checkbox" id="includePassID" style="padding-left:10px; text-align: left;">&nbsp;
							</td>
						</tr> 
					</tbody>
				</table>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area" style="padding: 10px 0; margin: 0;">
				<button type="button" id="btnPassSettingDateSave">저장</button>
				<button type="button" id="btnPassSettingDateCancel">취소</button>
			</div>
		</div>
	</div>
</div>

<div id="popClose" class="popup_layer" style="display:none">
	<div class="popup_box" style="height: 103px; width: 400px; padding: 10px; background: #f9f9f9; top: 71%;">
	<img class="CancleImg" id="btnCanclePopClose" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 style="color: #222; padding: 0; box-shadow: none;">세션 만료 안내</h1>
		</div>
		<div class="popup_content">
			<div class="content-box" style="height: 41px; background: #fff; border: 1px solid #c8ced3; font-size: 13px;">
				세션이 만료 되어 다시 로그인해 주세요.
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area" style="padding: 7px 0; margin: 0;">
				<button type="button" id="btnPopClose">확인</button>
			</div>
		</div>
	</div>
</div>
