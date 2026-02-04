<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%@ include file="../../include/header.jsp"%>
<style>
h4 {
	margin : 5px 0;
	font-size: 0.8vw;
}
#faqGrid * *{
	overflow: hidden; 
	white-space: nowrap; 
	text-overflow: ellipsis; 
}
.ui-jqgrid tr.ui-row-ltr td{
	cursor: pointer;
}
#insertfaqTitle::placeholder{
	color: #9E9E9E;
}
@media screen and (-ms-high-contrast: active), (-ms-high-contrast: none) {
	.list_sch{
		top: 41px !important;
	}
}
</style>
	<section id="section">
		<div class="container">
			<h3>FAQ</h3>
			<div class="content magin_t25">
				<div class="grid_top">
					<div class="searchBox" style="float: left;">
						<table class="user_info narrowTable" id="navGridSearchDiv"  style="width: 310px;">
							<tbody>
								<tr id="searchTextBox">
									<th style="text-align: center; border-radius: 0.25rem;" class="searchName">
										<select id="searchFilter"></select>
									</th>
	             			     	<td id="defaultSearchTextBox">
	                					<input type="text" style="width: 205px; padding-left: 5px;" size="10" class="searchContent" id="searchContent"  placeholder="검색어를 입력하세요.">	
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
						<div id="searchConditionsContainer" style="float: left;" ></div>
	                    <div style="float: right; margin-left: 3px; margin-bottom: 7px;">
	                    	<button type="button" name="button" class="btn_down" id="faqInsert">등록</button>
	                    </div>
	                    <div style="float: right;">
							<h5 style="width: 24px; height: 24px; margin-top: 2px; background-size : 100%; background-image: url('${pageContext.request.contextPath}/resources/assets/images/pi_list.png');" id="tableShow">&nbsp;</h5>
						</div>
	                </div>
				</div>
				<div class="left_box2" style="height: auto; min-height: 635px; overflow: hidden; width:59vw; margin-top: 10px;">
					<table id="faqGrid"></table>
					<div id="faqGridPager"></div>
				</div>
			</div>
		</div>
	</section>
	<div id="faqPopup" class="popup_layer" style="display:none">
	<div class="popup_box" style="height: 510px; width: 1140px; left: 38%; top: 50%; background: #f9f9f9; padding: 10px;">
	<img class="CancleImg" id="btnCancleFAQPopup" src="${pageContext.request.contextPath}/resources/assets/images/cancel.png">
		<div class="popup_top" style="background: #f9f9f9;">
			<h1 style="color: #222; box-shadow: none; padding: 0;">FAQ 등록</h1>
		</div>
		<div class="popup_content">
			<div class="content-box" style="width: 1120px; height: 480px; background: #fff; border: 1px solid #c8ced3;">
				<table class="faq_popup_tbl">
					<colgroup>
						<col width="15%">
						<col width="*">
					</colgroup>
					<tbody>
						<tr>
							<th>제목</th>
							<td><input type="text" id="insertfaqTitle" value="" class="edt_sch" style="width: 990px; font-size: 12px;" placeholder="제목을 입력하세요."></td>
						</tr>
						<tr>
							<th>내용</th>
							<td>
								<textarea rows="10" id="insertfaqContent" style="width: 990px; height: 400px; margin-top: 10px; font-size: 12px; resize: none;" placeholder="내용을 입력하세요."></textarea>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="popup_btn">
			<div class="btn_area">
				<button type="button" id="btnfaqSave">등록</button>
				<button type="button" id="btnfaqCancel">취소</button>
			</div>
		</div>
	</div>
</div>
	
	<div id="popup_manageSchedule" class="popup_layer" style="display:none;">
		<div class="popup_box" id="popup_box" style="height: 60%; width: 60%; left: 40%; top: 33%; right: 40%; ">
		</div>
	</div>
	<%@ include file="../../include/footer.jsp"%>

<script type="text/javascript"> 
var colModel = [];
GridName = "#faqGrid";

$(document).ready(function () {
	
	$("#tableCustomData").css("width","119px"); 
	$(document).click(function(e){
		$("#taskWindow").hide();
	});
	
	var gridWidth = $("#faqGrid").parent().width();
	var gridHeight = 555;
	
	colModel.push({ label: '제목', 		ndex: 'FAQ_TITLE', 				name: 'FAQ_TITLE',				width: 50, align: 'left',  formatter: htmlTag});
	colModel.push({ label: '내용', 		index: 'FAQ_CONTENT', 			name: 'FAQ_CONTENT',			width: 100, align: 'left', formatter: htmlTag});
	colModel.push({ label: '작성자', 		index: 'USER_NAME',				name: 'USER_NAME',				width: 10, align: 'center'});
	colModel.push({ label: '작성일', 		index: 'FAQ_CREATE_DT', 		name: 'FAQ_CREATE_DT', 			width: 25, align: 'center', type:"3_0"});
	colModel.push({ label: '게시글 번호', 	index: 'FAQ_NO', 				name: 'FAQ_NO', 				width: 0,  hidden:true});
	colModel.push({ label: '작성자 사번', 	index: 'USER_NO', 				name: 'USER_NO', 				width: 0,  hidden:true});
	GridSearchTypeChk();
	searchListAppend(); 
	$("#faqGrid").jqGrid({
		//url: 'data.json',
		datatype: "local",
	   	mtype : "POST",
	   	ajaxGridOptions : {
			type    : "POST",
			async   : true
		},
		colModel: colModel,
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: gridWidth,
		height: gridHeight,
		loadonce: true, // this is just for the demo
	   	autowidth: true,
		shrinkToFit: true,
		rownumbers : false, // 행번호 표시여부
		rownumWidth : 30, // 행번호 열의 너비	
		rowNum:25,
		rowList:[25,50,100],			
		pager: "#faqGridPager",
		//jqgrid의 특성상 - rowpos의 이벤트는 onSelectCell, beforeEditCell 다 해주어야 함
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	  	
	  	},
	  	ondblClickRow: function(nRowid, icol, cellcontent, e){
	  		
	  	},
	  	onCellSelect: function(rowid,icol,cellcontent,e) {
        	$("#pathWindow").hide();
            $("#taskWindow").hide();
            
            e.stopPropagation();
            var id = $(this).getCell(rowid, 'FAQ_NO');
            
            if (id != "0") {
                var pop_url = "${getContextPath}/popup/faqDetail";
            	var winWidth = 1200;
            	var winHeight = 980;
            	var popupOption= "width="+winWidth+", height="+winHeight + ", left=0, top=0, scrollbars=no, resizable=no, location=no"; 	
            	//var pop = window.open(pop_url,"detail",popupOption);
            	var pop = window.open(pop_url + "?id=" + id,id,popupOption);
            	/* popList.push(pop);
            	sessionUpdate(); */
            	
            	//pop.check();
            	
            	/* var newForm = document.createElement('form');
            	newForm.method='POST', 'GET';
            	newForm.action=pop_url + "/" + id;
            	newForm.name='newForm';
            	//newForm.target='detail';
            	newForm.target=id;
            	
            	var input_id = document.createElement('input');
            	input_id.setAttribute('type','hidden');
            	input_id.setAttribute('name','id');
            	input_id.setAttribute('value',id);
            	
            	newForm.appendChild(input_id);
            	document.body.appendChild(newForm);
            	newForm.submit();
            	
            	document.body.removeChild(newForm); */
            }
            else {
            	getLowPath(id);
            }
        },
		loadComplete: function(data) {
			automaticCompletion(null);
			var ids = $("#faqGrid").getDataIDs() ;
			$('.ui-jqgrid-hdiv').css('height', '35px')
	    },
	    gridComplete : function() {
	    }
	});
	
	//조회  
	var postData = {};
	$("#faqGrid").setGridParam({
		url:"<%=request.getContextPath()%>/faq/faqList", 
		postData : postData, 
		datatype:"json" 
		}).trigger("reloadGrid");
	
	
	// 엔터 입력시 발생하는 이벤트
	$('#title, #titcont, #writer').keyup(function(e) {
		if (e.keyCode == 13) {
		    $("#btnSearch").click();
	    }        
	});
	
	// 공지사항 등록 팝업
	$("#faqInsert").click(function(e){
		$("#faqPopup").show();
	})
});

$("#btnfaqSave").click(function(){
	// 등록
	var postData = {
		faq_title : $("#insertfaqTitle").val(),
		faq_con : $("#insertfaqContent").val(),
		user_no : '${memberInfo.USER_NO}'
	};
	
	console.log(postData);
	
	$.ajax({
		type: "POST",
		url: "/faq/faqInsert",
		async : false,
		data : postData,
	    success: function (resultMap) {
	    	console.log(resultMap);
	        if (resultMap.resultCode != 0) {
		        alert("FAQ 등록 실패 : " + resultMap.resultMessage);
		    } else if (resultMap.resultCode == 0) {
		    	alert("FAQ 등록 성공");
		    	$("#faqPopup").hide();
		    	
		    	// 화면 동적 변화 
		    	var postData = {};
		    	$("#faqGrid").setGridParam({
		    		url:"<%=request.getContextPath()%>/faq/faqList", 
		    		postData : postData, 
		    		datatype:"json" 
		    		}).trigger("reloadGrid");
		    	
		    	$("#insertfaqContent").val("");
		    	$("#insertfaqTitle").val("");
		    }
	    },
	    error: function (request, status, error) {
			alert("ERROR : " + error);
	    }
	});
});

$("#btnfaqCancel").click(function(){
	$("#faqPopup").hide();
	
	$("#insertfaqContent").val("");
	$("#insertfaqTitle").val("");
});

$("#btnCancleFAQPopup").click(function(){
	$("#faqPopup").hide();
	
	$("#insertfaqContent").val("");
	$("#insertfaqTitle").val("");
});



</script>
</body>

</html>