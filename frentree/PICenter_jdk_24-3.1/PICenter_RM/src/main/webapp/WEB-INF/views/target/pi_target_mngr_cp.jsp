<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
 
<%@ include file="../../include/header.jsp"%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.14.3/xlsx.full.min.js"></script>
<style>
.ui-state-hover td{
	background: #dadada !important;
}
.user_info th{
	width: 110px; 
}
.ui-jqgrid tr.ui-row-ltr td{
	cursor: pointer;
}
#pcAdminPopup .ui-jqgrid tr.ui-row-ltr td{
	cursor: default;
}
#left_datatype th, #left_datatype td {
	padding: 0;
}
.popup_tbl tbody td {
	height: 45px;
}
.selectBoxClick option:disabled {
	color: #D9D9D9;
}
.ui-widget-header{
	background: #f9f9f9;
	border: 0;
}

</style>
<section>
	<div class="container minMenu">
		<h3 id= "headerText" style="display: inline; top: 25px;">대상조회 및 현행화</h3>
		<p id="commit" style="position: absolute; top: 32px; left: 228px; font-size: 13px; color: #9E9E9E;">관리 대상 서버의 상세 정보, 담당자 정보를 확인하고 필요 시 수정 할 수 있습니다.</p>
		<div class="content magin_t35">
			<div class="left_area2" style=" height: 100%; float: left; width: 314px;">
				<table class="user_info narrowTable" style="width: 96%;">
					<tbody>
						<tr>
							<th style="text-align: center; border-radius: 0.25rem;">대상조회</th>
								<td>
                					<input type="text" style="width: 205px; padding-left: 5px;" size="10" id="targetSearch" placeholder="호스트명을 입력하세요.">
                			 	</td>
                				<td>
                					<input type="button" name="button" class="btn_look_approval" id="btn_sch_target" style="margin-top: 5px;">
                				</td>
           				</tr>
					</tbody>
				</table>
				<div class="left_box2" style="max-height: 92%; width: 100%; margin-top: 11px;">
					<div id="jstree" class="select_location" style="overflow-y: auto; overflow-x: auto; height: 100%; width:96%; background: #ffffff; border: 1px solid #c8ced3; white-space:nowrap;"></div>
				</div>
			</div>
			
			<div class="grid_top" style="float:right; width: 82%;">
				<div class="searchBox" style="float: left;">
					<table class="user_info narrowTable" id="navGridSearchDiv"  style="width: 310px;">
						<tbody>
							<tr id="searchTextBox">
								<th style="text-align: center; border-radius: 0.25rem;" class="searchName">
									<select id="searchFilter"></select>
								</th>
		             			<td id="defaultSearchTextBox">
		                			<input type="text" style="width: 205px; padding-left: 5px;" size="10"  class="searchContent" id="searchContent"  placeholder="검색어를 입력하세요.">	
		                		</td>
				                <td> 
				                	<input type="button" name="button" class="navGridSearchBtn" style="margin-top: 5px;">
				                </td>
							</tr> 
						</tbody>
					</table>
				</div>
				<div id="searchFilterBox" class ="searchFilterBox" style="display:inline-block;width:849px;position:absolute;"></div> 
				<div class="list_sch" style="height: 39px; margin-top: 10px; float: right;" >
					<div class="sch_area">
						<div id="searchConditionsContainer" style="float: left;"></div>
						<div style="float: right; margin-left: 3px; margin-bottom: 7px;">
							<button type="button" id="btnInsertTargetUser" class="btn_down">담당자 일괄 등록</button>
							<button type="button" id="btnDownloadServerExel" class="btn_down">다운로드</button>
						</div>  
						<div style="float: right;">
							<h5 style="width: 24px; height: 24px; margin-top: 2px; background-size : 100%; background-image: url('${pageContext.request.contextPath}/resources/assets/images/pi_list.png');" id="tableShow">&nbsp;</h5>
						</div>
					</div>
				</div> 
				<div class="left_box2" id="serverGridBox" style=" height: 677px; max-height: 677px; bottom: 17px; overflow-x: auto; ">
   					<table id="serverGrid"></table>
   				 	<div id="serverGridPager"></div>
   				</div>
			</div>
		</div>
	</div>
</section>
<%@ include file="../../include/footer.jsp"%>

<!-- 대상(서버/DB) 상세 조회 팝업 -->
<div id="targetPopup" class="popup_layer" style="display:none">
	<div class="popup_box" style="height: 331px; width: 505px; left: 54%; top: 61%; padding: 10px; background: #f9f9f9;">
	<img class="CancleImg" id="btnCancleTargetPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 style="color: #222; padding: 0; box-shadow: none;">상세정보</h1>
			<p style="position: absolute; top: 12px; left: 88px; font-size: 12px; color: #9E9E9E;">담당하고 있는 서버 정보가 맞는지 확인 후 수정하시기 바랍니다.</p>
		</div>
		<div class="popup_content">
			<div class="content-box" style="height: 246px; background: #fff; border: 1px solid #c8ced3;">
				<table class="popup_tbl">
					<colgroup>
						<col width="30%">
						<col width="70%">
					</colgroup>
					<tbody>
						<tr>
							<th>그룹명</th>
							<td><input type="text" id="targetGroup" value="" class="edt_sch" style="width: 285px; padding-left: 10px; background-color: rgba(210, 210, 210, 0.35);" readonly></td>
						</tr>
						<tr>
							<th>호스트명</th>
							<td>
								<input type="text" id="targetHost" value="" class="edt_sch" style="width: 285px; padding-left: 10px; background-color: rgba(210, 210, 210, 0.35);" readonly>
								<input type="hidden" id="targetHostId" value="" class="edt_sch" style="width: 285px; padding-left: 10px; background-color: rgba(210, 210, 210, 0.35);" readonly>
								<input type="hidden" id="targetHostApNo" value="" class="edt_sch" style="width: 285px; padding-left: 10px; background-color: rgba(210, 210, 210, 0.35);" readonly>
							</td>
						</tr>
						<tr>
							<th>서비스명</th>
							<td><input type="text" id="targetService" value="" class="edt_sch" style="width: 285px; padding-left: 10px; background-color: rgba(210, 210, 210, 0.35);" readonly></td>
						</tr>
						<tr>
							<th>IP</th>
							<td><input type="text" id="targetIP" value="" class="edt_sch" style="width: 285px; padding-left: 10px; background-color: rgba(210, 210, 210, 0.35);" readonly></td>
						</tr>
						<tr>
							<th>담당자</th>
							<td><input type="text" id="targetmngr" value="" class="edt_sch" style="width: 218px; padding-left: 10px;" readonly>
								<input type="hidden" id="targetmngrNm" value="" class="edt_sch" style="width: 218px; padding-left: 10px;" readonly>
								<input type="hidden" id="targetmngrId" value="" class="edt_sch" style="width: 218px; padding-left: 10px;" readonly>
								<button type="button" id="targetServiceManagerBtn" style="width: 63px; height: 27px;">조회</button></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area" style="padding: 10px 0; margin: 0;">
				<button type="button" id="btntargetSave">저장</button>
				<button type="button" id="btntargetCancel">취소</button>
			</div>
		</div>
	</div>
</div>

<!-- 담당자 리스트 조회 하면-->
<div id="selectUserListPopup" class="popup_layer" style="display:none"> 
	<div class="popup_box" style="height: 290px; width: 885px; padding: 10px; background: #f9f9f9; left: 39%; top: 52%;">
	<img class="CancleImg" id="btnCancleServerMngrPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png" style="z-index: 999;">
		<div class="popup_top" style="background: #f9f9f9; display: block;">
			<h1 style="color: #222; padding: 0; box-shadow: none;">업무 담당자</h1>
		</div>
		<div class="popup_content" style="height: 240px;">
			<div class="content-box" id="div_mngr_list" style="height: auto; background: #fff; border: 1px solid #c8ced3;">
				<table id="mngrListGrid"></table>
				<div id="mngrListGridPager"></div>
			</div>
		</div>
		<div class="popup_btn" style="height: 45px;">
			<div id="acesssBtn" class="btn_area" style="padding: 10px 0; margin: 0;">
				<button type="button" id="btnServerMngrListSave" style="margin-top: 10px;" >저장</button>
			</div>
		</div>
	</div>
</div>

<!-- 담당자 중복 조회 화면 -->
<div id="adminPopup" class="popup_layer" style="display:none">
	<div class="popup_box" style="height: 200px; width: 758px; padding: 10px; background: #f9f9f9; left: 46%; top: 53%">
	<img class="CancleImg" id="btnCancleadminPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png" style="z-index: 999;">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 style="color: #222; padding: 0; box-shadow: none;" id="serverMngrNm">담당자</h1>
		</div>
		<div class="popup_content">
			<div class="content-box" id="div_update_user" style="height: auto; background: #fff; border: 1px solid #c8ced3; padding: 5px 10px 10px 10px">
				<div style="right: 0px; padding-top: 0px; font-size: 14px; font-weight: bold;">
					소속 : <input type="text" id="searchGroup" value="" class="edt_sch" style="width: 125px; margin-bottom: 3px;" onKeypress="javascript:if(event.keyCode==13) fnManagerSearch()">
					담당자 : <input type="text" id="searchUser" value="" class="edt_sch" style="width: 125px; margin-bottom: 3px;" onKeypress="javascript:if(event.keyCode==13) fnManagerSearch()">
					<button id="btnManagerSearch" class="btn_sch" style ="position: relative; margin-top:10px">검색</button>
				</div>
				<table id="targetUserGrid"></table>
				<div id="targetUserGridPager"></div>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area" style="padding: 10px 0; margin: 0;">
				<button type="button" id="btnTargetManagerInsert">추가</button>
				<button type="button" id="btnTargetManagerDelete">삭제</button>
				<button type="button" id="btnAdminClose">닫기</button>
			</div>
		</div>
	</div>
</div>

<!-- 담당자 조회 > 선택 에 보여지는 화면 -->
<div id="taskWindow" class="ui-widget-content" style="position:absolute; left: 10px; top: 10px; touch-action: none; width: 150px; z-index: 999; 
	border-top: 2px solid #222222; box-shadow: 2px 2px 5px #ddd; display:none">
	<ul>
		<li  class="status status-completed status-scheduled status-paused status-stopped status-failed">
			<button id="updateMngrBtn">조회 </button></li>
		<li class="status status-completed status-scheduled status-scanning status-paused status-stopped">
			<button id="deleteMngrBtn">삭제</button></li>
	</ul>
</div>

<!-- 담당자 일괄 등록 팝업 -->
<div id="insertExcelPopup" class="popup_layer" style="display:none">
	<div class="popup_box" style="width: 725px; height: 540px; padding: 10px; background: #f9f9f9; left: 46%; top: 51%;">
		<img class="CancleImg" id="btnCancleExcelPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 style="color: #222; padding: 0; box-shadow: none;">담당자 일괄 등록</h1>
			<p style="position: absolute; top: 14px; left: 155px; font-size: 12px; color: #9E9E9E;">PIC에서 제공하는 형식이 아닌 다른 형식으로 업로드시 생성이 불가합니다.</p>
		</div>
		<div class="popup_content">
			<div class="content-box" style="background: #fff; width: 100%; height:457px; border: 1px solid #c8ced3; overflow: auto;">
				<table class="popup_tbl" style="width: 100%;">
					<colgroup>
						<col width="15%">
						<col width="85%">
					</colgroup>
					<tbody>
						<tr>
							<th>파일 다운로드</th>
							<td>
								<form id="btnDownLoadXlsx" action="<%=request.getContextPath()%>/download/downloadExcel" method="post" style="display: inline; width: 49px; padding: 0px;">
									<input id="downloadFile" type="hidden" name="filename" value="PIC_담당자_일괄등록.xlsx">
									<input id="downloadRealFile" type="hidden" name="realfilename" value="target_user_insert_ver1.xlsx">
									<input type="submit" id="btnExcelDown" value="다운로드"> 
								</form>
							</td>
						</tr>
					</tbody>
				</table>
				<table class="popup_tbl2" style="width: 100%;">
					<colgroup>
						<col width="15%">
						<col width="85%">
					</colgroup>
					<tbody >
						<tr>
							<th>
								파일 업로드
							</th>
							<td>
								<button type="button" id="clickimportBtn">파일선택</button>
								<input type="file" id="importExcel" name="importExcel" style="width: 955px; padding-left: 10px; display: none; ">
								<input type="text" id="importExcelNm" style="width: 460px; font-size: 12px; padding: 0 0 0 5px; margin: 0 0 0 7px;" readonly="">
							</td>
						</tr>
					</tbody>
				</table>
				<div class="content-table" style="width: 100%; height: 340px; padding: 0;">
					<table class="popup_tbl" style="width: 100%;">
						<colgroup>
							<col width="25%">
							<col width="30%">
							<col width="25%">
							<col width="*">
							<col width="5%">
						</colgroup>
						<tbody id="import_targetUserList_excel">
							<tr height="45px;">
								<th>호스트명</th>
								<th>호스트 아이피</th>
								<th>담당자 사번</th>
								<th>담당자 구분</th>
							</tr>
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

<script type="text/javascript">

var mngrNameList = "${mngrNameList}".split('[{').join('').split('}]').join('');
mngrNameList = mngrNameList.split('}, {'); 
var colModel = [];
GridName = "#serverGrid";
var jstreeList = ${userGroupList};
var resetFomatter = null;

$(document).ready(function () {
	
	serverNode(); // node
	fn_drawserverGrid(); // main grid
	fn_targetMngrGrid(); // 담당자 조회 grid
	
	$(document).click(function(e){
		$("#taskWindow").hide();
		$("#tableCustomData").hide();
	});
	
});

function serverNode(){
	$('#jstree').jstree({
		// List of active plugins
		"core" : {
		    "animation" : 0,
		    "check_callback" : true,
			"themes" : { "stripes" : false },
			"data" : jstreeList 
		},
		"types" : {
			    "#" : {
			      "max_children" : 1,
			      "max_depth" : 4,
			      "valid_children" : ["root"]
			    },
			    "default" : {
			      "valid_children" : ["default","file"]
			    },
			    "file" : {
			      "icon" : "glyphicon glyphicon-file",
			      "valid_children" : []
			    }
		},
		'plugins' : ["search"]
	})
    .bind('select_node.jstree', function(evt, data, x) {
    	
    }).bind("loaded.jstree", function(e, data) {
    	var nodes = $('#jstree').jstree(true).get_json('#', { 'flat': true });
    	
    	var textMap = new Map();
   	    nodes.forEach(function(node) {
   	        // 대소문자 무시 및 양쪽 공백 제거하여 키 생성
   	        var key = node.text.trim().toLowerCase();
   	        if (!textMap.has(key)) {
   	            textMap.set(key, node.text);
   	        }
   	    });

   	    // Map 객체의 값들로 배열을 만들고 정렬
   	    var selectColData = Array.from(textMap.values());
        selectColData.sort(); 
         
    	$("#targetSearch").autocomplete({
    		source : selectColData,
    		select : function(event, ui){
    		},
    		focus : function(event, ui){ 
    			 $(".ui-menu-item").removeClass("pic-grid-focus");
    			 
    			 $(".ui-menu-item").each(function() {
    				 $(".ui-menu-item").removeClass("pic-grid-focus ui-state-active");
    				 $(".ui-menu-item-wrapper:contains('" + ui.item.label + "')").closest(".ui-menu-item").addClass("pic-grid-focus");
    				 $(".ui-menu-item-wrapper").removeClass("ui-state-active"); 
    		        });
    			return false;
    		}
    	});
    });
}

// 대상 상세 조회 저장
$("#btntargetSave").click(function(){
	
};

// 대상 상세 조회 닫기 및 취소
$("#btntargetCancel #btnCancleTargetPopup").click(function(){ 
	$("#targetPopup").hide();
});
</script>

</body>
</html>