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
	padding: 0 25px 22px 0;
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
.hashDiv{
	border: 1px solid #c8ced3;
	border-radius: 0.25rem;
}
#Recon_hash{
	border: 1px solid #c8ced3;
	border-radius: 0.25rem;
}
.hashDiv table tr{
	height: 45px;
}
#Recon_hash>table tr{
	height: 45px;
} 
#ReconHashGrid tr td{
	height: 28px;
}
</style>
<section>
	<div class="container">
		<h3>무결성 점검</h3>
		<div class="content magin_t25" id="interlock" >
			<div style="height: 180px;">
				<div id="PIC_hash" class="hashDiv" style="width: 49.5%; float: left;">
					<table>
						<colgroup>
							<col width="10%">
							<col width="5%"> 
							<col width="20%">
							<col width="10%">
							<col width="55%">
						</colgroup>
						<tr>
							<th>platfrom</th>
							<th>version</th>
							<th>filename</th>
							<th colspan="2">Hash</th>
						</tr>
					</table>
					<table class="popup_tbl">
						<tr style="height: 30px;">
							<td rowspan="3" >PICenter</td>
							<td rowspan="3">3.1</td>
							<td rowspan="3" style="width: 162px;">${picNodeMap.filename}</td>
							<td>md5_hash</td>
							<td>${picNodeMap.md5_hash}</td> 
						</tr>
						<tr style="height: 30px;">
							<td>sha1_hash</td>
							<td>${picNodeMap.sha1_hash}</td>
						</tr>
						<tr style="height: 30px;">
							<td>sha256_hash</td>
							<td>${picNodeMap.sha256_hash}</td>
						</tr>
					</table>
				</div>
				<div id="Recon_hash" class="hashDiv" style="width: 49.5%; float: right; padding-right: 10px;"> 
					<table>
						<colgroup>
							<col width="10%">
							<col width="5%">
							<col width="20%">
							<col width="10%">
							<col width="55%">
						</colgroup>
						<tr>
							<th>platfrom</th>
							<th>version</th>
							<th>filename</th>
							<th colspan="2">Hash</th>
						</tr> 
					</table>
					<table class="popup_tbl">
						<tr style="height: 30px;">
							<td rowspan="3" style="">Recon</td>
							<td rowspan="3">2.10.0</td>
							<td rowspan="3" style="width: 162px;" > ${reconNodeMap.filename}</td>
							<td>md5_hash</td>
							<td>${reconNodeMap.md5_hash}</td>
						</tr>
						<tr style="height: 30px;">
							<td>sha1_hash</td>
							<td>${reconNodeMap.sha1_hash}</td>
						</tr>
						<tr style="height: 30px;">
							<td>sha256_hash</td>
							<td>${reconNodeMap.sha256_hash}</td>
						</tr>
					</table>
				</div>
			</div>
			 
			<div id="Recon_hash" style="width:100%;  margin-top: 80px; height: 450px;"> 
				<table style="width: 100%;">
					<colgroup>
						<col width="20%">
						<col width="5%">
						<col width="30%">
						<col width="10%"> 
						<col width="35%">
					</colgroup>
					<tr>
						<th>platfrom</th>
						<th>version</th>
						<th>filename</th>
						<th colspan="2">Hash</th>
					</tr>
				</table> 
				<div style="overflow-y: auto; height: 402px; border-top:2px solid #2f353a;">
					<table  id="ReconHashGrid" style="overflow-y: auto; ">
					</table>
				</div>
			</div> 
		</div>
	</div>
</section>
<!-- section -->
<%@ include file="../../include/footer.jsp"%>
<script type="text/javascript">
$(document).ready(function () {
	ReconGrid();
});


function ReconGrid(){
	
	var gridWidth = $("#ReconHashGrid").parent().width(); 
	var gridHeight = 380;
	
	$.ajax({
		type: "POST",
		url: "/setting/reconNodeSelect",
		async : false,
		data : {},
	    success: function (result) {
	    	
	    	var hash_html = "<colgroup>";
				hash_html += "<col width=\"20%\">";
				hash_html += "<col width=\"5%\">"; 
				hash_html += "<col width=\"30%\">";
				hash_html += "<col width=\"10%\">";
				hash_html += "<col width=\"35%\">";
				hash_html += "</colgroup>"; 
	    	for(i=0 ; i < result.length; i++ ){
	    		hash_html += ("<tr style=\"height: 23px;\"><td rowspan=\"3\">"+result[i].platform+"</td>");
	    		hash_html += ("<td rowspan=\"3\">"+result[i].version+"</td>");
	    		hash_html += ("<td rowspan=\"3\">"+result[i].filename+"</td>");
	    		hash_html += ("<td>md5_hash</td><td>");
	    		hash_html += (result[i].md5_hash+"</td></tr><tr style=\"height: 10px;\"><td>sha1_hash</td><td>");
	    		hash_html += (result[i].sha1_hash+"</td></tr><tr style=\"height: 10px;\"><td>sha256_hash</td><td>");
	    		hash_html += (result[i].sha256_hash+"</td></tr><tr style=\"height: 10px;\"></tr>");
	    	}
	    	
	    	$("#ReconHashGrid").html(hash_html);
	    	console.log(result);
		
	    },
	    error: function (request, status, error) {
	        console.log("ERROR : ", error);
	    }
  	});
};
function PICGrid(){
	
	var gridWidth = $("#PICHashGrid").parent().width();
	var gridHeight = 300;
	
	$("#PICHashGrid").jqGrid({
		//url: 'data.json',
		datatype: "local",
	   	mtype : "POST",
	   	ajaxGridOptions : {
			type    : "POST",
			async   : true
		}, 
		colNames:['OS', '버전', '파일명', 'down', 'MD5_HASH', 'SHA1_HASH', 'SHA256_HASH'],
		colModel: [      
			{ index: 'platform', 				name: 'platform',				width: 10, 	align: 'left'},
			{ index: 'version', 				name: 'version',				width: 10, 	align: 'center'},
			{ index: 'filename', 				name: 'filename',				width: 10, 	align: 'left'},
			{ index: 'download_url', 			name: 'download_url',			width: 10, 	align: 'left', hidden:true},
			{ index: 'md5_hash', 				name: 'md5_hash',				width: 10, 	align: 'left'},
			{ index: 'sha1_hash', 				name: 'sha1_hash',				width: 10, 	align: 'left'},
			{ index: 'sha256_hash', 			name: 'sha256_hash',			width: 10, 	align: 'left'}
		],
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
		pager: "#ReconHashGridPager",
		//jqgrid의 특성상 - rowpos의 이벤트는 onSelectCell, beforeEditCell 다 해주어야 함
	  	onSelectRow : function(rowid,celname,value,iRow,iCol) {	  	
	  	},
	  	ondblClickRow: function(nRowid, icol, cellcontent, e){
	  	},
	  	onCellSelect: function(rowid,icol,cellcontent,e) {
        	$("#pathWindow").hide();
            
            e.stopPropagation();
            var host_name = $(this).getCell(rowid, 'host_name');
            var expression = $(this).getCell(rowid, 'expression');
            var target_id = $(this).getCell(rowid, 'id');
        },
		loadComplete: function(data) {
			var ids = $("#filtersGrid").getDataIDs() ;
            $.each(ids, function(idx, rowId) {
                rowData = $("#filtersGrid").getRowData(rowId, true) ;
                $("#filtersGrid").setCell(rowId, 'type2', rowData.type);
            });
			 
			$(".gridSubSelBtn").on("click", function(e) {
		  		e.stopPropagation();
				$("#filtersGrid").setSelection(event.target.parentElement.parentElement.id);
		
				var offset = $(this).parent().offset();
				$("#taskGroupWindow").css("left", (offset.left - $("#taskGroupWindow").width()) + 55 + "px");
				$("#taskGroupWindow").css("top", offset.top + $(this).height() + "px");
		
				var bottomLimit = $(".left_box2").offset().top + $(".left_box2").height();
				var taskBottom = Number($("#taskGroupWindow").css("top").replace("px","")) + $("#taskGroupWindow").height();
		
				if (taskBottom > bottomLimit) { 
					$("#taskGroupWindow").css("top", Number($("#taskGroupWindow").css("top").replace("px","")) - (taskBottom - bottomLimit) + "px");
				}
				$("#taskGroupWindow").show();
			});
		},
		gridComplete : function() {
		}
	});
	
var postData = {};
	
	$("#PICHashGrid").setGridParam({
		url:"<%=request.getContextPath()%>/setting/PICNodeSelect", 
		postData : postData, 
		datatype:"json" 
		}).trigger("reloadGrid");
}
</script>
</body>
</html>