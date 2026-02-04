<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta charset="utf-8">
<title>개인정보검출 상세정보</title>
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

<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/resources/assets/css/wickedpicker.min.css" />

<!-- Application Common Functions  -->
<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/js/common.js"></script>

<!-- Publish CSS -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/assets/css/reset-PIC.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/assets/css/design-PIC.css" />

</head>

<%@ include file="../../include/session.jsp"%>
<style>
	body{
		width: auto;
	}
	@media screen and (-ms-high-contrast: active), (-ms-high-contrast: none) {
		body{
			width: auto !important;
		}
		#bodyContents{
			height: 315px !important;
		}
		#matchData{
			height: 265px !important;
		}
	}
</style>
<body>
	<div class="wrap" style="min-height: 364px !important;">
		<!-- 팝업창 시작 개인정보검출 상세정보 -->
		<%
		String browser = "";
		String userAgent = request.getHeader("User-Agent");
		
		%>
		
		<!-- 팝업창 시작 하위 로케이션 상세정보 -->
		<%
		if (userAgent.indexOf("Trident") > 0 || userAgent.indexOf("MSIE") > 0) {
		%>
			<div id="taskWindow" style="position:absolute; touch-action: none; width: 100%; height: 95%; z-index: 999; min-width: 30%; min-height: 200px; background: #f9f9f9;" class="ui-widget-content">
			<table class="mxWindow" style="position: absolute; left: 15px; width: 97%; height: 100%;">
			<tbody>
				<tr>
					<td class="mxWindowTitle" style="touch-action: none; background: #fff; padding: 0;">
						<table style="width: 100%; height: 36px;">
							<colgroup>
								<col width="*">
								<col width="30px">
							</colgroup>
							<tr>
								<td style="color: #000; text-align: left; background: #f9f9f9;">
									<h2 style="width: 1110px;">개인정보검출 상세정보</h2>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td class="mxWindowPane">
						<div class="mxWindowPane" style="width: 100%;height: 100%;">
							<table style="width: 100%;height: 100%;">
							<tbody>
								<tr>
									<td id="matchCount" style="background: #fff; width: 245px; min-width: 245px; max-width: 195px; height: 50px; padding: 5px; border: 1px solid #c8ced3;">&nbsp;</td>
									<td style="width: 100%; height: 100%; border: 1px solid #c8ced3; border-left: none;" rowspan="2">
										<div id="bodyContents" style="background: #fff; overflow-y:auto;height: 100%; padding: 5px 5px;">&nbsp;</div>
									</td>
								</tr>
								<tr>
									<td style="border: 1px solid #c8ced3; border-top: none;">
										<div id="matchData" style="background: #fff; overflow-y:auto;height: 100%; padding: 5px;">&nbsp;</div>
									</td>
								</tr>
							</tbody>
							</table>
						</div>
					</td>
				</tr>
			</tbody>
			</table>
		</div>
		<%
		} else {
		%>
		<div id="taskWindow" style="position:absolute; touch-action: none; width: 100%; height: 95%; z-index: 999; min-width: 30%; min-height: 200px; background: #f9f9f9;" class="ui-widget-content">
			<table class="mxWindow" style="position: absolute; left: 15px; width: 97%; height: 100%;">
			<tbody>
				<tr>
					<td class="mxWindowTitle" style="touch-action: none; background: #fff; padding: 0;">
						<table style="width: 100%; height: 100%;">
							<colgroup>
								<col width="*">
								<col width="30px">
							</colgroup>
							<tr>
								<td style="color: #000; text-align: left; background: #f9f9f9;">
									<h2 style="width: 1110px;">개인정보검출 상세정보</h2>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td class="mxWindowPane">
						<div class="mxWindowPane" style="width: 100%;height: 100%;">
							<table style="width: 100%;height: 100%;">
							<tbody>
								<tr>
									<td id="matchCount" style="background: #fff; width: 245px; min-width: 245px; max-width: 195px; height: 50px; padding: 5px; border: 1px solid #c8ced3;">&nbsp;</td>
									<td style="width: 100%; height: 100%; border: 1px solid #c8ced3; border-left: none;" rowspan="2">
										<div id="bodyContents" style="background: #fff; overflow-y:auto;height: 100%; padding: 5px 5px;">&nbsp;</div>
									</td>
								</tr>
								<tr>
									<td style="border: 1px solid #c8ced3; border-top: none;">
										<div id="matchData" style="background: #fff; overflow-y:auto;height: 100%; padding: 5px;">&nbsp;</div>
									</td>
								</tr>
							</tbody>
							</table>
						</div>
					</td>
				</tr>
			</tbody>
			</table>
		</div>
		<%
		}
		%>
		<!-- 팝업창 종료 -->
	</div>
</body>
<script type="text/javascript">
$(document).ready(function(){
	if('${id}' != null && '${ap_no}' != null && '${id}' != '' && '${ap_no}' != ''){
		setContent('${id}','${tid}', '${ap_no}', '${type}');
	}
});

function Loading() {
    var maskHeight = $(document).height();
    var maskWidth  = window.document.body.clientWidth;
     
    var mask       = "<div id='mask' style='position:absolute; z-index:9000; background-color:#000000; display:none; left:0; top:0;'></div>";
    var loadingImg ='';
     
    loadingImg +=" <div id='loadingImg'>";
    loadingImg +=" <img src='${pageContext.request.contextPath}/resources/assets/images/spinner.gif' style='position:absolute; z-index:9500; text-align:center; display:block; top:20%; left:40%;'/>";
    loadingImg += "</div>";  
 
    $('body')
        .append(mask)
 
    $('#mask').css({
            'width' : maskWidth,
            'height': maskHeight,
            'opacity' :'0.3'
    });
    
    $('#mask').show();
  
    $('#taskWindow').append(loadingImg);
    $('#loadingImg').show();
}

function closeLoading() {
    $('#mask, #loadingImg').hide();
    $('#mask, #loadingImg').remove(); 
}

function setContent(id, tid, ap_no, type){
	
	var pattern = '${pattern}'.split('[{').join('').split('}]').join('');
	pattern = pattern.split('}, {');
	var patternCnt = '${patternCnt}';
	
	var oPostDt = {
			"id": id, 
			"tid": tid,
			"ap_no": ap_no,
			"type" : type};
   $.ajax({
       type: "POST",
       url: "/exception/getMatchObjects3",
       async : true,
       data : oPostDt,
       success: function (resultMap) {
    	   
    	   console.log(resultMap);   // 235
    	   var matchList = [];
    	   var metasCount = [];
    	   var totalCnt = 0;
           var bodyContents = "";
    	   
    	   for(var cp=0; cp < patternCnt ; cp++){
        	   metasCount.push(0)
    	   }
    	   
           if (resultMap.resultCode != "0") {
               alert(resultMap.resultMessage);
               window.close();
           }
           
           var metas = JSON.parse(resultMap.metas);
           var omitted = JSON.parse(resultMap.omittes);

           bodyContents = "<strong>" + resultMap.path + " </strong></br>";
           
           for (var i = 0; i < metas.length; i++) {
               var meta = metas[i];
               
               var metasIndex = -1;
               var metasContent = 0;
               
               if (meta.is_aggregate == true) {
                   var tmp = Number(meta.value);
                   totalCnt = totalCnt + tmp;
                   bodyContents += "<strong>" + meta.label + " : </strong>" + meta.value.format() + "</br>";
               }
               else {
                   bodyContents += "<strong>" + meta.label + " : </strong>" + meta.value + "</br>";
               }
               
               for(var p=0; p < patternCnt ; p++){
            	   
            	   var row = pattern[p].split(', ');
            	   var PATTERN_NAME = row[0].split('PATTERN_NAME=').join('');
            	   var PATTERN_NAME_EN = row[1].split('PATTERN_NAME_EN=').join('');
            	   var MASK_CNT = row[2].split('MASK_CNT=').join('');
            	   var MASK_TYPE = row[3].split('MASK_TYPE=').join('');
            	   var ID = row[4].split('ID=').join('');
            	   var MASK_CHK = row[6].split('MASK_CHK=').join('');

            	   if(meta.label.toUpperCase().indexOf(PATTERN_NAME_EN.toUpperCase()) > -1){
            		   metasIndex = p;
                	   metasContent = meta.value;
                	   
                	   if(metasIndex >= 0){
    		               metasCount[metasIndex] = parseInt(metasContent) +   metasCount[metasIndex]; 
    	               }
            	   }
            	   
	               
	               
               }
               
           }
           
           bodyContents += "</br>";
           
           $("#bodyContents").html(bodyContents);
           
           var chunk_check = "<b style='color:red;'>";
           var chunk_check2 = "</b>"
           var match_cnt = 0;
           var matchData = "";
           var matchType = [];
           var matchCount = [];
           var matchContents = [];
           var patternCode = [];
           var content_cnt = 0;
           var mask_content = "";
           var result_content;
           
//            마스킹
           var mask_chk = []; // 마스킹 위치
           var mask_cnt = []; // 마스킹 수
           var mask_type = []; // 마스킹 구분자(ex @)
           
           if(matchs != null){ 
        	   matchData = "National ID (" + matchs.length + ")</br>";
           }
           
           for(var mp=0; mp < patternCnt ; mp++){
        	   var row = pattern[mp].split(', ');
        	   
        	   var PATTERN_NAME = row[0].split('PATTERN_NAME=').join('');
        	   var PATTERN_NAME_EN = row[1].split('PATTERN_NAME_EN=').join('');
        	   var MASK_CNT = row[2].split('MASK_CNT=').join('');
        	   var MASK_TYPE = row[3].split('MASK_TYPE=').join('');
        	   var ID = row[4].split('ID=').join('');
        	   var MASK_CHK = row[6].split('MASK_CHK=').join('');
        	   
        	   matchType.push(PATTERN_NAME); 
        	   matchCount.push(0);
        	   matchContents.push("");
        	   patternCode.push(PATTERN_NAME_EN.toUpperCase());
        	   
        	   mask_chk.push(MASK_CHK);
        	   mask_cnt.push(MASK_CNT);
        	   mask_type.push(MASK_TYPE);
        	   
           }
           
           if(resultMap.matchs != null) {
        	   var chunks = JSON.parse(resultMap.chunks);
               var matchs = JSON.parse(resultMap.matchs);
        	   
        	   result_content = new Array(chunks.length);
        	   
               for(var i = 0; i < chunks.length; i++){
            	   var mask_chunks = chunks[i]; 
            	   var chunk_offset = chunks[i].offset;
            	   var chunk_length = chunks[i].length;
            	   
            	   result_content[i] = chunks[i].content;
            	   
            	   for(content_cnt; content_cnt < matchs.length; content_cnt++){
            		   var content_offset = matchs[content_cnt].offset;
            		   var check_offset = (parseInt(chunk_offset) <= parseInt(content_offset)) && (parseInt(content_offset) <= (parseInt(chunk_offset) + parseInt(chunk_length)));
            		   if(check_offset) {
            			   var con_length = matchs[content_cnt].length;
            			   var pi_detection = matchs[content_cnt].content;
            			   console.log("pi_detection", pi_detection);
            			   
            			   var matchIndex = patternCode.indexOf(matchs[content_cnt].data_type.toUpperCase());
                           var data_type = matchs[content_cnt].data_type;
                           var mask_content = pi_detection;
                           if(mask_type[matchIndex] != null && mask_type[matchIndex] != "N"){
                        	   mask_content = detectionData(mask_type[matchIndex], mask_cnt[matchIndex], mask_chk[matchIndex], pi_detection);
                           }
            			   
                           result_content[i] = result_content[i].replaceAll(matchs[content_cnt].content,chunk_check + mask_content + chunk_check2);
                           result_content[i] = result_content[i].replaceAll("</b></b>", "</b>");
                           
                           matchCount[matchIndex] = matchCount[matchIndex] + 1; 
                           matchContents[matchIndex] = matchContents[matchIndex] + mask_content + "</br>";

                           if (i > 999) break; 
            		   } else {
            			   break;
            		   }
            	   }
               } 
               
               for (var i = 0; i < result_content.length; i++) {
   					var content = result_content[i].split("\n");
   					
	   				 if (!isNull(omitted)) {
	   					var omitte = omitted[i];
		   				 if (!isNull(omitte)) {
		        		    var omittedData = '<i style=\'color:red;\'> '+omitte.length.format()+' bytes omitted</i></br>';
		        		    $("#bodyContents").append(omittedData);
		   				 }
	   				 }
        		    
   				 	for(var j=0; j< content.length; j++){
   					
   						var text = content[j];
   					
      		   			if(text.indexOf(chunk_check) > -1) {
               			var flag = true;
               			while(flag){
   		             		$("#bodyContents").append( document.createTextNode( text.substr( 0, text.indexOf(chunk_check) ) ) );
   		             		text = text.substr( text.indexOf(chunk_check) );
   		             		$("#bodyContents").append( text.substr( 0, ( text.indexOf(chunk_check2)+chunk_check2.length ) ) );
   		             		text = text.substr( (text.indexOf(chunk_check2) + chunk_check2.length) );
               			
               				if(text.indexOf(chunk_check) < 0){
               					$("#bodyContents").append( document.createTextNode(text) );
               					flag = false;
               				}
               			}
                   		$("#bodyContents").append('</br>');
                   	
	               		} else {
	               			$("#bodyContents").append( document.createTextNode(text) );
	               			$("#bodyContents").append('</br>');
	               			 
	               		}
    	   			} 
   				}
           } else {
        	   $("#bodyContents").append("<b style='color:blue;'>Remaining data truncated</b>");
           }
           
           if (totalCnt == 1) $("#matchCount").html(totalCnt.format() + " Match");
           else $("#matchCount").html(totalCnt.format() + " Matches");
           
           var matchData = "<ul>";

//            console.log("matchContents", matchContents);
//            console.log("matchType", matchType);
//            console.log("metasCount", metasCount);
           
           for (var i = 0; i < matchType.length; i++) {
        	   if (metasCount[i] > 0) {
					matchData += "<li class='matchTypeList'><a href='javascript:void(0)'><img style='margin-right: 3px; margin-bottom: 3px;' src='${pageContext.request.contextPath}/resources/assets/images/arrow_deactive.png'>";
					matchData += matchType[i] + "(" + metasCount[i].format() + ")" + "</a>";
					
					matchData += "<ul style='padding: 0px 3px 3px 3px; display:none'><li style='margin-left: 20px;'>" + matchContents[i] ;
					
					if(matchCount[i] < metasCount[i]){
						matchData += "<img style='vertical-align: middle; padding-left: 5px; padding-bottom: 4px;' src='${pageContext.request.contextPath}/resources/assets/images/alert.png'>";
						matchData += "<span style='padding-left: 2px !important; display: inline-block; margin-top: 5px;'>+" + (metasCount[i] - matchCount[i]).format() +" more</span></li>";
					}
					matchData += "</li></ul></li>";
        	   }
           }
           
           matchData += "</ul>";
           $("#matchData").html(matchData);
           $(".matchTypeList>a").click(function() {
               if ($(this).next('ul').is(':visible')) {
                   $(this).children("img").attr("src", '${pageContext.request.contextPath}/resources/assets/images/arrow_deactive.png');
                   $(this).next('ul').hide();
               } else {
                   $(this).children("img").attr("src", '${pageContext.request.contextPath}/resources/assets/images/arrow_active.png');
                   $(this).next('ul').show();
               }
           });
           
       },
       beforeSend: function(){
    	   Loading();
       },
       complete: function () {
    	   closeLoading();
       },
       error: function (request, status, error) {
           alert("올바르지 않은 접근입니다.");
       }
   });
}

// 23.04.19 앞 4자리 제외 전체 마스킹
function maskString(str, end) {
  const prefix = str.substring(0, 4);
  const maskedLength = end - 4;
  const mask = '#'.repeat(maskedLength);
  return prefix + mask + str.substring(end);
}

function detectionData(maskType, maskCnt, maskChk,  pi_detection){
// maskType = "T, B"
// maskCnt = 0 ..
// maskChk = @

	var resultData = pi_detection;
	console.log("maskType", maskType);
	console.log("resultData", resultData);
	if(maskType == "T"){ // 앞글자 마스킹
		
		if(maskChk!=""){
			var splitData = pi_detection.split(maskChk);
			resultData = "";
			for (var i = 0; i < (splitData.length-1); i++) {
				if(splitData[i].length >= maskCnt){
					const prefix = splitData[i].slice(0, -maskCnt);
					const mask = '#'.repeat(maskCnt);
					resultData += prefix + mask + maskChk;	
				}else{
					resultData += splitData[i] + maskChk;
				}
				
			}
			resultData += splitData[(splitData.length-1)];
			
		}else{
			const prefix = pi_detection.substring(maskCnt);
			const mask = '#'.repeat(maskCnt);
			resultData = mask+prefix;
		}
	}else if(maskType == "B"){ // 뒷글자 마스킹
		
		if(maskChk!=""){
			var splitData = pi_detection.split(maskChk);
			resultData = splitData[0];
			for (var i = 1; i < splitData.length; i++) {
				if(splitData[i].length >= maskCnt){
					const prefix = splitData[i].substring(maskCnt);
					const mask = '#'.repeat(maskCnt);
					 
					resultData += maskChk + mask + prefix;
				}else{
					resultData += maskChk + splitData[i] ;
				}
			}
		}else{
			const prefix = pi_detection.slice(0, -maskCnt);
			const mask = '#'.repeat(maskCnt);
			resultData = prefix + mask;	
		}
		
	}

	console.log("resultData", resultData);
	return resultData;
}

function excape(con){
	
	/* var output = con.replace(".", "\\.").replace("*", "\\*").replace("+", "\\+").output.replace("?", "\\?")
				.replace("[", "\\[").replace("^", "\\^").replace("$", "\\$").replace("{", "\\{").replace("}", "\\}")
				.replace("|", "\\|").replace("(", "\\(").replace(")", " ").replace("\\", "\\\\"); */ 

	var output = con.replace(/\./g, "\\.")
             .replace(/\*/g, "\\*")
             .replace(/\+/g, "\\+")
             .replace(/\?/g, "\\?")
             .replace(/\[/g, "\\[")
             .replace(/\^/g, "\\^")
             .replace(/\$/g, "\\$")
             .replace(/\{/g, "\\{")
             .replace(/\}/g, "\\}")
             .replace(/\|/g, "\\|")
             .replace(/\(/g, "\\(")
             .replace(/\)/g, "\\)")
             .replace(/\\/g, "\\\\"); 

return output;
}
function reExcape(con){
	
	var output = con.replace(".", "\.").replace("*", "\*").replace("+", "\+").replace("?", "\?").replace("[", "\[")
				.replace("^", "\^").replace("$", "\$").replace("{", "\{").replace("}", "\}").replace("|", "\|")
				.replace("(", "\(").replace(")", " \)").replace("\\", "\\"); // 백슬래시를 이스케이프하여 그대로 보여줌


return output;
}
</script>
</html>
