<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta charset="utf-8">
<title>범위 조회</title>
<link rel="icon" href="${pageContext.request.contextPath}/resources/assets/images/favicon.ico" type="image/x-icon">

<link href="${pageContext.request.contextPath}/resources/assets/css/ui.fancytree.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resources/assets/css/font-awesome.min.css" rel="stylesheet" type="text/css" />

<!-- Publish JS -->
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/jquery-3.3.1.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/js/jquery-ui-PIC.js" type="text/javascript"></script>

<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/tree/jquery.fancytree.ui-deps.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/tree/jquery.fancytree.js"></script>

<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/jqgrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/jqgrid/i18n/grid.locale-kr.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/wickedpicker.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/jstree.min.js"></script>

<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/resources/assets/css/wickedpicker.min.css" />

<!-- Application Common Functions  -->
<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/js/common.js"></script>

<!-- Publish CSS -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/assets/css/reset-PIC.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/assets/css/design-PIC.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/assets/css/style.min.css" />

</head>
<style>
	body{
		width: auto;
	}
	@media screen and (-ms-high-contrast: active), (-ms-high-contrast: none) {
		body{
			width: auto !important;
		}
	}
</style>
<body>
	<div id="stepContents1" class="step_content" style="border-top: 1px solid #aca49c;  background: #f9f9f9;  width: 100%; height: 100%; background: #f9f9f9;">
		<div class="step_content_cell fl_l padd_r" style="width: 95%; overflow-y: auto; margin-left: 25px;">
		
			<div class="select_location sch_left" style="height: 50px; min-height: 50px;background: #fff; border-bottom: 0px; border-radius: 0;">
				<div style="position: absolute; right: 10px; top: 10px; padding-top: 0px; font-size: 14px; font-weight: bold;">
				호스트명 : <input type="text" id="searchHost" value="" class="edt_sch" style="width: 180px; margin-bottom: 2px;">
				<button id="btnSearch" class="btn_sch" style="margin-top: 2px;">검색</button>
				</div>
			</div>
			<div id="div_all" class="select_location" style="overflow-y: auto; width: 100%; height: 460px;background: #ffffff; border-radius: 0;">
				<div id="groupJstree">
				</div>
			</div>
			<div class="btn_area" style="padding: 10px 2px; margin: 0;">
				<button type="button" id="btnExceptionSave" style="font-weight: inherit; font-size: 12px; padding: 0 15px;">추가</button>
				<button type="button" id="btnExceptionPopupCancel" style="font-weight: inherit; font-size: 12px; padding: 0 15px;">취소</button>
			</div>
			
		</div>
	</div>
</body>
<script type="text/javascript">
var typeChk = "${type}";
$(function(){
	$('#groupJstree').jstree({
		// List of active plugins
	   "core" : {
		    "animation" : 0,
		    "check_callback" : true,
			"themes" : { "stripes" : false },
			"data" : ${userReportHostList}
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
		'search': {
	        'case_insensitive': false,
	        'show_only_matches' : true,
	        "show_only_matches_children" : true
	    },
		'plugins' : ["checkbox","search"],
	}).bind('select_node.jstree', function(evt, data, x) {
    	
    }).bind("dblclick.jstree", function (event) {
    	
    	 var sTag = "";

     	var node = $(event.target).closest("li");
     	
     	var js_node = $('#groupJstree').jstree(true).get_node(node[0].id);
     	console.log(js_node.data.type);  
     	if(js_node.data.type == 1 || js_node.data.type == 3){
     		
     		
     		var type = js_node.data.type;
	     	var name = js_node.data.name;
	     	var id = js_node.data.target_id;
	     	var ap = js_node.data.ap;
	     	
	     	
     		var tr_id = $(opener.document).find('.server_Target_Id').map(function() {
         	    return $(this).val();
         	}).get();

     		
     		var value = ap+"_"+id;
     		var valueChk = false;
     		
     		for(i=0 ; i<tr_id.length ; i++){
     			if(tr_id[i] == value){
     				valueChk = true;
     				break;
     			}
     		}
 
     		if(valueChk){
     			alert("이미 선택한 호스트입니다.");
     			return;
     		}
     		
	      	// Host html 입력
	 		sTag += "<tr style='border-bottom:1px solid #cdcdcd'>";
	 		sTag += "    <th class='server_Host_Name' style='padding:5px 2px; background: #ffffff; overflow:hidden; text-align:left;border-bottom:1px solid #cdcdcd'>" + name + "</th>";
	 		sTag += "    <td style='padding:2px; background: #ffffff; height:23px; width:30px;border-bottom:1px solid #cdcdcd'>";
	 		sTag += "       <img class ='' src='${pageContext.request.contextPath}/resources/assets/images/cancel.png' style='width:40%; display:inline; cursor: pointer' onclick='fnServerRemove(this);'>";
	 		sTag += "		<input type='hidden' class='server_Target_Id' value='"+ap+"_"+id+"'>";//dtObj.AP_NO+"_"+dtObj.TARGET_ID
	 		sTag += "    </td>";
	      	sTag += "</tr>";
	      	
	      	$(opener.document).find("#exceptServer").append(sTag);
	      	window.close();
	      	
     	}else{
     		alert("서버만 선택 가능합니다.");
     		return;
     	}
     	// Do some action
    });
	
	var to = true;
	$('#btnSearch').on('click', function(){
		
	    var v = $('#searchHost').val();
		console.log(v);
		
		if(to) { clearTimeout(to); }
	    to = setTimeout(function () {
	      $('#groupJstree').jstree(true).search(v);
	    }, 250);
	});
	
	$('#btnExceptionSave').on('click', function(){
		
		
		console.log($("#groupJstree").jstree("get_checked").length);
		
		if($("#groupJstree").jstree("get_checked").length == 0){
			alert("서버를 선택해주세요.");
			return true;
		}
		var valueChk = false;
		var sTag = "";
		$.each($("#groupJstree").jstree("get_checked",true),function(){
			
			var data = this.data;
 
			if( this.data != null && (this.data.type == 1 || this.data.type == 3)){
				
				
				var ap = this.data.ap;
				var id = this.data.target_id;
				var name = this.data.name;
				
				var tr_id = $(opener.document).find('.server_Target_Id').map(function() {
	         	    return $(this).val();
	         	}).get();
	     		var value = ap+"_"+id;
	     		for(i=0 ; i<tr_id.length ; i++){
	     			if(tr_id[i] == value){
	     				valueChk = true;
	     				break;
	     			}
	     		}
	     		sTag += "<tr style='border-bottom:1px solid #cdcdcd'>";
		 		sTag += "    <th class='server_Host_Name' style='padding:5px 2px; background: #ffffff; overflow:hidden; text-align:left;border-bottom:1px solid #cdcdcd'>" + name + "</th>";
		 		sTag += "    <td style='padding:2px; background: #ffffff; height:23px; width:30px;border-bottom:1px solid #cdcdcd'>";
		 		sTag += "       <img class ='' src='${pageContext.request.contextPath}/resources/assets/images/cancel.png' style='width:40%; display:inline; cursor: pointer' onclick='fnServerRemove(this);'>";
		 		sTag += "		<input type='hidden' class='server_Target_Id' value='"+ap+"_"+id+"'>";//dtObj.AP_NO+"_"+dtObj.TARGET_ID
		 		sTag += "    </td>";
		      	sTag += "</tr>";
		      	
			}
		
		});
		
		if(valueChk){
 			alert("이미 선택된 호스트가 포함되어 있습니다.");
 			return;
 		}
		
		if(confirm("선택한 서버를 추가하시겠습니까?")){
			$(opener.document).find("#exceptServer").append(sTag);
			window.close();
		}else{
			sTag = "";
		}
		
	});
	
	
	$('#btnExceptionPopupCancel').on('click', function(){
		window.close();
	});
	
	$('#searchHost').keyup(function (e) {
    	var v = $('#searchHost').val();
    	if (e.keyCode == 13) {
        	
        	if(to) { clearTimeout(to); }
            to = setTimeout(function () {
              $('#groupJstree').jstree(true).search(v);
            }, 250);
        }
    });
});

</script>
</html>
