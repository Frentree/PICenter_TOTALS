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
	#btn_view_server{
		margin : 0px 5px 3px 5px!important;
		padding : 0px 10px !important;
	}
</style>
<body>
	<div id="stepContents1" class="step_content" style="border-top: 1px solid #aca49c;  background: #f9f9f9;  width: 100%; height: 90%; background: #f9f9f9;">
		<div class="step_content_cell fl_l padd_r" style="width: 95%; overflow-y: auto; margin-left: 25px;">
		
			<div class="select_location sch_left" style="height: 50px; min-height: 50px;background: #fff; border-bottom: 0px; border-radius: 0;">
				<div style="position: absolute; right: 10px; top: 10px; padding-top: 0px; font-size: 14px; font-weight: bold;">
				호스트명 : <input type="text" id="searchHost" value="" class="edt_sch" style="width: 180px; margin-bottom: 2px;">
				<button id="btnSearch" class="btn_sch" style="margin-top: 2px;">검색</button>
				</div>
			</div>
			<div id="div_all" class="select_location" style="overflow-y: auto; width: 100%; height: 456px;background: #ffffff; border-radius: 0;">
				<div id="groupJstree">
				</div>
			</div>
			<div id="div_search" class="select_location" style="overflow-y: auto; height: 460px;background: #ffffff; display: none;">
				<table id="Tbl_search" class="tbl_input" id="location_table">
					<tbody>
					</tbody>
				</table>
			</div>
			<div style = "float:right; padding: 5px 0px 0px 0px">
				<c:if test="${flag==1}"><button class="btn_down" type="button" id="btnSaveHostList" name="btnSaveHostList">확인</button></c:if> 
				<c:if test="${flag==3}"><button class="btn_down" type="button" id="btnSaveManager" name="btnSaveManager">수정</button></c:if> 
				<button class="btn_down" type="button" id="btnCancleHostList" name="btnCancleHostList">닫기</button>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
var typeChk = "${type}";
var flag =  "${flag}";
var insa_code = '${insa_code}';
$(function(){
	var plug = ["search"];
	if (flag == 1 || flag ==3 ) plug.push("checkbox");
	//else plug = '["search"]';
	
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
		'plugins' : plug,
	}).bind('select_node.jstree', function(evt, data, x) {
		/* console.log(data.node.data);
		console.log('data.node.id');
		console.log(data.node.id); */
    	
    }).bind("dblclick.jstree", function (event) {
    	
    	/* var node = $(event.target).closest("li");
    	var data = node.data("jstree"); 
    	
    	var js_node = $('#groupJstree').jstree(true).get_node(node[0].id);
    	

    	// Do some action
    	var type = js_node.data.type;
    	var name = js_node.data.name;	
    	var id = js_node.data.target_id;
    	var ap = js_node.data.ap;
    	
    	if(type == 1){
    		$(opener.document).find("#SCH_TARGET").val(name);
    		$(opener.document).find("#sch_host_list").val(id);
    		$(opener.document).find("#sch_ap_list").val(ap);
    		window.close();
    	} else {
    		name = js_node.data.name;
    	}   */
    }).bind("loaded.jstree", function (event) {
    	
    	var scanList = JSON.parse('${serverList}');
    	var groupList = JSON.parse('${groupList}');
    	
    	for(var i=0;i<scanList.length;i++){
    		$('#groupJstree').jstree('select_node', scanList[i]);
    	}
    	
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
	
	$('#searchHost').keyup(function (e) {
    	var v = $('#searchHost').val();
    	if (e.keyCode == 13) {
        	
        	if(to) { clearTimeout(to); }
            to = setTimeout(function () {
              $('#groupJstree').jstree(true).search(v);
            }, 250);
        }
    });
	
	$('#btnCancleHostList').on('click', function(){
		window.close();
	});
	
	$('#btnSaveHostList').on('click', function(){
		
		var cnt = 0;
		
		if($("#groupJstree").jstree("get_checked",true).length == 0){
			alert("대상을 선택해주세요.");
			return;
		}
		
		var name = "";
		var host_name = "";
		var id = [];
		var ap = [];
		var count = 0
		var not_CNT = 0;
		var serverList =  [];
		var serverObject = {};
		$.each($("#groupJstree").jstree("get_checked",true),function(){
			
			if(this.data != null && (this.data.type == 1 || this.data.type == 3)){
				if(count == 0){
					host_name = this.text;
					count ++;	
				}
				cnt++;
				console.log(this); 
				console.log(this.data.type == 3); 
				if(this.data.type == 3){
					serverList.push(this.data.type + "_"+  this.id+"_"+this.data.ap);
				}else {
					serverList.push(this.data.type + "_"+ this.id+"_"+ this.data.target_id+"_"+this.data.ap);
				}
				
			} else { 
				++not_CNT; 
				return true;
			}
			
		});
		
		
		if(cnt > 1){
			name = host_name + " 외 " + (id.length - 1) + "건"
		} else name = host_name;
		$(opener.document).find("#view_server").val(JSON.stringify(serverList));
		$(opener.document).find("#view_server_cnt").html(cnt+"대 "+' <button id="btn_view_server" class="btn_down" type="button" style="height: 20px;" onclick="viewServer(1)">수정</button> ');
		/* $(opener.document).find("#sch_host_list").val(id.join()); 
		$(opener.document).find("#sch_ap_list").val(ap.join()); */
		console.log(serverList); 
// 		window.close();
	});
	
	$('#btnSaveManager').on('click', function(){
		//alert(insa_code);
		var cnt = 0;
		
		/* if($("#groupJstree").jstree("get_checked",true).length == 0){
			alert("대상을 선택해주세요.");
			return;
		} */
		
		var name = "";
		var host_name = "";
		var id = [];
		var ap = [];
		var count = 0
		var not_CNT = 0;
		var serverList =  [];
		var serverObject = {};
		$.each($("#groupJstree").jstree("get_checked",true),function(){
			
			if(this.data == null || this.data.type != 1){
				++not_CNT;
				return true;
			} else {
				if(count == 0){
					host_name = this.text;
					count ++;	
				}
				cnt++;
				/* var server_id = this.id.split("_"); */
				/* serverObject = {};
				serverObject["target_id"]=server_id[2];
				serverObject["ap_no"]=server_id[1];
				serverList.push(serverObject); */
				
				serverList.push(this.id);

			}
			
		});
		//alert(serverList);
		var postData = {
				insa_code : insa_code,
				serverList : JSON.stringify(serverList)
			};
		
		var message = "수정하시겠습니까?";
		
		if (confirm(message)) {
			$.ajax({
				type: "POST",
				url: "/target/insertServiceTarget",
				async : false,
				data : postData,
			    success: function (resultMap) {
			        if (resultMap.resultCode == 0) {
			        	alert("수정하였습니다.");
			        	<%-- var postData = {};
			        	$("#targetGrid").setGridParam({url:"<%=request.getContextPath()%>/search/getProfile", postData : postData, datatype:"json" }).trigger("reloadGrid"); --%>
			        	search_policy();
			        	initDetails();
				    } else {
				        alert("실패하였습니다.");
				    } 
			    },
			    error: function (request, status, error) {
					alert("Server Error : " + error);
			        console.log("ERROR : ", error);
			    }
			});
		}
		opener.search_Manager();
		window.close();
	});
	
});

</script>
</html>
