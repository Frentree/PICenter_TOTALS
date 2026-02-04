<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta charset="utf-8">
<title>개인정보검출 상세정보</title>

<link href="${pageContext.request.contextPath}/resources/assets/css/ui.fancytree.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resources/assets/css/font-awesome.min.css" rel="stylesheet" type="text/css" />

<!-- Publish JS -->
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/jquery-3.7.1.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/js/jquery-ui-sktPIC.js" type="text/javascript"></script>

<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/js/jquery-ui.js" type="text/javascript"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/tree/jquery.fancytree-all-deps.min.js"></script>
<!-- AG Grid JavaScript -->
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/ag-grid-community.min.js"></script>


<!-- Application Common Functions  -->
<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/js/common.js"></script>

<!-- Publish CSS -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/assets/css/reset-sktPIC.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/assets/css/design-sktPIC.css" />

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
		//if (userAgent.indexOf("Trident") > 0 || userAgent.indexOf("MSIE") > 0) {
		if (userAgent != null && (userAgent.contains("Trident") || userAgent.contains("MSIE"))) {
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
		setContent('${id}','${tid}', '${ap_no}');
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

function setContent(id, tid, ap_no){
	var oPostDt = {
			"id": id, 
			"tid": tid,
			"ap_no": ap_no};
   $.ajax({
       type: "POST",
       url: "/exception/getMatchObjects",
       async : true,
       data : oPostDt,
       success: function (resultMap) {
    	   
           if (resultMap.resultCode != "0") {
               alert(resultMap.resultMessage);
               window.close();
           }
           
           var pattern = '${pattern}'.split('[{').join('').split('}]').join('');
       		pattern = pattern.split('}, {');
           
           var totalDetectionCnt = 0;
           var detectionCnt = 0;
           var chunksLeng = 0;
           var matchsLeng = 0;
           var cutData = true;
           
           var totalCnt = 0;
           var bodyContents = "";
           var path = resultMap.resultData.path.split("#");
           
           if(path[path.length -1].indexOf("_decrypted") == -1){
        	   path[path.length -1] = path[path.length -1];
	       } else {
	    	   path[path.length -1] = path[path.length -1].replaceAll("_decrypted","");
	       }
           
           bodyContents += "<strong>" + path[path.length -1] + "</strong></br>";
           var metas = resultMap.resultData.metas;
           metas.sort(function(a, b) { // 오름차순
               return a.label < b.label ? -1 : a.label > b.label ? 1 : 0;
           });

           for (var i = 0; i < metas.length; i++) {
               var meta = metas[i];
               
               if (meta.is_aggregate == true) {
                   var tmp = Number(meta.value);
                   totalCnt = totalCnt + tmp;
                   bodyContents += "<strong>" + meta.label + " : </strong>" + meta.value.format() + "</br>";
               }
               else {
                   bodyContents += "<strong>" + meta.label + " : </strong>" + meta.value + "</br>";
               }
           }

           bodyContents += "</br>";
           if (!isNull(resultMap.resultData.omitted)) {
               var omitted = resultMap.resultData.omitted[0];
               bodyContents += "<i style='color:red;'>" + omitted.length.format() + " bytes omitted</i></br></br>";
           }

           $("#bodyContents").html(bodyContents);
           
			var matchs = resultMap.resultData.matches;
				matchsLeng = matchs.length;
			var chunks = resultMap.resultData.chunks;
				chunksLeng = chunks.length; 
			var chunk_check = "<b style='color:red;'>";
			var chunk_check2 = "</b>"
			var match_cnt = 0;
           
           /*
               신용카드 : Type1 - like 아래 이외의 기타...
               주민등록번호 : Type2 - like South Korean RRN
               외국인등록번호 : Type3 - like South Korean Foreigner Number
               여권번호 : Type4 - like South Korean Passport
               운전면허 번호 : Type5 - like South Korean Driver License Number
               전화/휴대폰 : Type6 - like South Korean Phone Number
               사업자등록번호 : Type7 - like South Korean Taxpayer Identification Number
               법인등록번호 : Type8 - like South Korean Corporation Registration Number
               계좌번호 : Type9 - like Account Number
           */
           var matchData = "";
           if(resultMap.resultData.matches != null){
        	   matchData = "National ID (" + resultMap.resultData.matches.length + ")</br>";
           }
           
           /* var matchType = ["주민등록번호","외국인등록번호","여권번호","운전면허 번호","전화/휴대폰","사업자등록번호","법인등록번호","계좌번호","이메일","신용카드","기타"];
           var matchCount = [0,0,0,0,0,0,0,0,0,0,0];
           var matchContents = ["","","","","","","","","","",""]; */
           
           var matchType = [];
           var matchCount = [];
           var matchContents = [];
           var matchDataTypes = [];
           var matchID = [];
           var matchMask = [];
           
           // 현재 아이디
           for(var i = 0; pattern.length > i; i++){ // str 배열만큼 for돌림
       		var row = pattern[i].split(', ');
       		var PATTERN_NAME = row[0].split('PATTERN_NAME=').join(''); // 변수 CHK Y, 다음 반복문에서는 N
       		var MASK_CNT = row[1].split('MASK_CNT=').join(''); // 변수 CHK Y, 다음 반복문에서는 N
       		var ID = row[2].split('ID=').join(''); //변수 TABLE_NAME은 TB_NAME1, 다음 반복문에서는 TB_NAME2
       		var PATTERN_CODE = row[3].split('PATTERN_CODE=').join(''); // 변수 CHK Y, 다음 반복문에서는 N
       		
   			matchType.push(PATTERN_NAME);
       		matchCount.push(0);
       		matchContents.push("");
       		matchID.push(parseInt(ID)-1);
       		matchMask.push(parseInt(MASK_CNT));
    		
       		matchDataTypes.push(PATTERN_CODE); 
       		
       	}

           var content_cnt = 0;
           var mask_content = "";
           var result_content;
           
           if(chunks != null) {
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
            			   
            			   var matchIndex = -1;
                           var data_type = matchs[content_cnt].data_type;
                           var mask_content = "";
                           
                           for(var j = 0; j < matchID.length; j++){
                        	   var type= matchDataTypes[j].split(",");
                        	   
                        	   // 해당 개인정보 항목이 여러개 일경우
                        	   if(type.length > 0){
                        		   var chk = 0;
                        		   
                        		   for(var t = 0; t < type.length; t++){
                        			   if (data_type.toUpperCase().indexOf(type[t].toUpperCase()) > -1){
                        				   matchIndex = matchID[j];
                        				   mask_content = pi_detection.substring(0, con_length-matchMask[j]) + "#".repeat(matchMask[j]);
                        				   chk = 1;
                        				   break;
                        			   }
                        		   } 
                        		   if(chk == 1) break;
                        		   
                        	   } else {
                        		   if (data_type.toUpperCase().indexOf(matchDataTypes[j].toUpperCase()) > -1){
                               	    matchIndex = matchID[j];
                               	    
                               	    // 이메일만 예외
                               	    if(data_type.toUpperCase().indexOf("Email".toUpperCase()) > -1) {
                               	    	var mail_chk = pi_detection.indexOf("@");
                                    	   	
                                     	   if (!String.prototype.repeat) {
                                     		  String.prototype.repeat = function(count) {
                                     		    'use strict';
                                     		    if (this == null) {
                                     		      throw new TypeError('can\'t convert ' + this + ' to object');
                                     		    }
                                     		    var str = '' + this;
                                     		    count = +count;
                                     		    if (count != count) {
                                     		      count = 0;
                                     		    }
                                     		    if (count < 0) {
                                     		      throw new RangeError('repeat count must be non-negative');
                                     		    }
                                     		    if (count == Infinity) {
                                     		      throw new RangeError('repeat count must be less than infinity');
                                     		    }
                                     		    count = Math.floor(count);
                                     		    if (str.length == 0 || count == 0) {
                                     		      return '';
                                     		    }
                                     		    // Ensuring count is a 31-bit integer allows us to heavily optimize the
                                     		    // main part. But anyway, most current (August 2014) browsers can't handle
                                     		    // strings 1 << 28 chars or longer, so:
                                     		    if (str.length * count >= 1 << 28) {
                                     		      throw new RangeError('repeat count must not overflow maximum string size');
                                     		    }
                                     		    var maxCount = str.length * count;
                                     		    count = Math.floor(Math.log(count) / Math.log(2));
                                     		    while (count) {
                                     		       str += str;
                                     		       count--;
                                     		    }
                                     		    str += str.substring(0, maxCount - str.length);
                                     		    return str;
                                     		  }
                                     		}
                                     	   	
                                     	   	if(mail_chk > 2){
                                     	   //	if(mail_chk != -1){	
                                           		mask_content = pi_detection.substring(0, 2) + "#".repeat(mail_chk-2) + pi_detection.substring(mail_chk, con_length);
                                     	   	} else {
                                     	   		mask_content = pi_detection;
                                     	   	}
                               	    } else mask_content = pi_detection.substring(0, con_length-matchMask[j]) + "#".repeat(matchMask[j]);
                               	    
                               	    break; 
                                  } else {
                               	   matchIndex = 10;
                                  }
                        	   }
                        	   
                        	    
                           }
            			   
            			   
            			    /* if (data_type.toUpperCase().indexOf("South Korean RRN".toUpperCase()) > -1){
                         	    matchIndex = 0;
                         	    mask_content = pi_detection.substring(0, con_length-6) + "######";
                            }
                            else if (data_type.toUpperCase().indexOf("South Korean Foreigner Number".toUpperCase()) > -1){
                         	    matchIndex = 1;
                          	    mask_content = pi_detection.substring(0, con_length-6) + "######";
                            }
                            else if (data_type.toUpperCase().indexOf("South Korean Passport".toUpperCase()) > -1){
                         	    matchIndex = 2;
                           	    mask_content = pi_detection.substring(0, con_length-4) + "####";
                            }
                            else if (data_type.toUpperCase().indexOf("South Korean Driver License Number".toUpperCase()) > -1){
                         	    matchIndex = 3;
    						    mask_content = pi_detection.substring(0, con_length-6) + "######";
                            }
                            else if (data_type.toUpperCase().indexOf("Phone Number".toUpperCase()) > -1){
                         	   	matchIndex = 4;
    							mask_content = pi_detection.substring(0, con_length-4) + "####";
                            }
                            else if (data_type.toUpperCase().indexOf("South Korean Taxpayer Identification Number".toUpperCase()) > -1){
                         	    matchIndex = 5;
                             	mask_content = pi_detection.substring(0, con_length-4) + "####";
                            }
                            else if (data_type.toUpperCase().indexOf("South Korean Corporation Registration Number".toUpperCase()) > -1){
                         	   	matchIndex = 6;
                            	mask_content = pi_detection.substring(0, con_length-4) + "####";
                            }
                            else if (data_type.toUpperCase().indexOf("Account Number".toUpperCase()) > -1){
                         	   	matchIndex = 7;
                            	mask_content = pi_detection.substring(0, con_length-6) + "######";
                            }
                            else if (data_type.toUpperCase().indexOf("Email".toUpperCase()) > -1){
                         	   	matchIndex = 8;
                         	   	var mail_chk = pi_detection.indexOf("@");
                         	   	
                         	   if (!String.prototype.repeat) {
                         		  String.prototype.repeat = function(count) {
                         		    'use strict';
                         		    if (this == null) {
                         		      throw new TypeError('can\'t convert ' + this + ' to object');
                         		    }
                         		    var str = '' + this;
                         		    count = +count;
                         		    if (count != count) {
                         		      count = 0;
                         		    }
                         		    if (count < 0) {
                         		      throw new RangeError('repeat count must be non-negative');
                         		    }
                         		    if (count == Infinity) {
                         		      throw new RangeError('repeat count must be less than infinity');
                         		    }
                         		    count = Math.floor(count);
                         		    if (str.length == 0 || count == 0) {
                         		      return '';
                         		    }
                         		    // Ensuring count is a 31-bit integer allows us to heavily optimize the
                         		    // main part. But anyway, most current (August 2014) browsers can't handle
                         		    // strings 1 << 28 chars or longer, so:
                         		    if (str.length * count >= 1 << 28) {
                         		      throw new RangeError('repeat count must not overflow maximum string size');
                         		    }
                         		    var maxCount = str.length * count;
                         		    count = Math.floor(Math.log(count) / Math.log(2));
                         		    while (count) {
                         		       str += str;
                         		       count--;
                         		    }
                         		    str += str.substring(0, maxCount - str.length);
                         		    return str;
                         		  }
                         		}
                         	   	
                         	   	if(mail_chk > 2){
                         	   //	if(mail_chk != -1){	
                               		mask_content = pi_detection.substring(0, 2) + "#".repeat(mail_chk-2) + pi_detection.substring(mail_chk, con_length);
                         	   	} else {
                         	   		mask_content = pi_detection;
                         	   	}
                            }
                            else if (
                                  data_type.toUpperCase().indexOf("VISA") > -1 || 
                                  data_type.toUpperCase().indexOf("MAESTRO") > -1 || 
                                  data_type.toUpperCase().indexOf("PRIVATE LABEL CARD") > -1 || 
                                  data_type.toUpperCase().indexOf("DINERS CLUB") > -1 || 
                                  data_type.toUpperCase().indexOf("JCB") > -1 || 
                                  data_type.toUpperCase().indexOf("LASER") > -1 || 
                                  data_type.toUpperCase().indexOf("MASTERCARD") > -1 || 
                                  data_type.toUpperCase().indexOf("CHINA UNION PAY") > -1 || 
                                  data_type.toUpperCase().indexOf("DISCOVER") > -1 || 
                                  data_type.toUpperCase().indexOf("TROY") > -1 || 
                                  data_type.toUpperCase().indexOf("AMERICAN") > -1  
                               ){
                                matchIndex = 9;

                             	mask_content = pi_detection.substring(0, con_length-6) + "######";
                            }
                            else  matchIndex = 10; */
            			    
                            result_content[i] = result_content[i].replace(new RegExp(String(matchs[content_cnt].content).replace("+","\\+"),'gi'), "" + chunk_check + String(mask_content).replace("+","\+") + chunk_check2);

                            matchCount[matchIndex] = matchCount[matchIndex] + 1; 
                            
                           	matchContents[matchIndex] = matchContents[matchIndex] + mask_content + "</br>";

                            if (i > 999) break; 
            		   } else {
            			   break;
            		   }
            		   
            	   }
            	   
               } 
               
               if (totalCnt == 1) $("#matchCount").html(totalCnt.format() + " Match");
               else $("#matchCount").html(totalCnt.format() + " Matches");
               
               var matchSum = 0;
               
               var matchData = "<ul>";
               for (var i = 0; i < matchType.length; i++) {
                   if (matchCount[i] == 0) continue;
                   matchData += "<li class='matchTypeList'><a href='javascript:void(0)'><img style='margin-right: 3px; margin-bottom: 3px;' src='${pageContext.request.contextPath}/resources/assets/images/arrow_deactive.png'>";
                   if (matchs.length > 1000){
                       matchData += matchType[i] + "(" + matchCount[i].format() + "+)" + "</a>";
                       matchSum += matchCount[i];
                   }else {
                       matchData += matchType[i] + "(" + matchCount[i].format() + ")" + "</a>";
                       matchSum += matchCount[i];
                   }   
                   matchData += "<ul style='padding: 0px 3px 3px 3px; display:none'><li style='margin-left: 20px;'>" + matchContents[i] + "</li></ul></li>";
               } 
               
               totalDetectionCnt = matchSum;
               if (totalCnt > 1000) {
                   matchData += "<li style='padding-top: 3px;'><img style='vertical-align: middle;padding-left: 5px;' src='${pageContext.request.contextPath}/resources/assets/images/alert.png'>";
                   matchData += "<span style='padding-left: 12px !important;'>+" + (totalCnt - 1000).format() +" more</span></li>";
               }else if(totalCnt > matchSum) {
            	   matchData += "<li style='padding-top: 3px;'><img style='vertical-align: middle;padding-left: 5px;' src='${pageContext.request.contextPath}/resources/assets/images/alert.png'>";
                   matchData += "<span style='padding-left: 12px !important;'>+" + (totalCnt - matchSum).format() +" more</span></li>";
               }
               matchData += "</ul>"
                   
               $("#matchData").html(matchData);            	   
            	   
               for (var i = 0; i < result_content.length; i++) {
   				var content = result_content[i].split("\n");
   				++detectionCnt;
   				 for(var j=0; j< content.length; j++){
					var text = content[j];
					
					if(cutData){
						if(text.indexOf(chunk_check) > -1) {
	               			var flag = true;
	               			while(flag){
	   		             		$("#bodyContents").append( document.createTextNode( text.substr( 0, text.indexOf(chunk_check) ) ) );
	   		             		text = text.substr( text.indexOf(chunk_check) );
	   		             		$("#bodyContents").append( text.substr( 0, ( text.indexOf(chunk_check2)+chunk_check2.length ) ) );
	   		             		text = text.substr( (text.indexOf(chunk_check2) + chunk_check2.length) );
	               			
	               				/* if(text.indexOf(chunk_check) < 0){
	               					$("#bodyContents").append( document.createTextNode(text) );
	               					flag = false;
	               				} */
	               				
	               				
		   		             	if(text.indexOf(chunk_check) < 0){
		                            
		                            if (totalDetectionCnt > 999 && matchsLeng == 1000 && chunksLeng == 1000 && detectionCnt == 1000){ // 1000개가 넘고, 주변 데이터 없는 경우
		                                    $("#bodyContents").append("<br><b style=\"color:rgb(51, 153, 255);\">Remaining data truncated</b>" );
		                                    cutData = false;
		                                    flag = false;
		                            }else if(totalCnt > 999 && matchsLeng == 1000 && chunksLeng < 1000 && totalDetectionCnt > chunksLeng){ // 1000개가 넘고, 주변 데이터 있는 경우
		                                if(detectionCnt > matchsLeng ){
		                                    $("#bodyContents").append("<br><b style=\"color:rgb(51, 153, 255);\">Remaining data truncated</b>" );
		                                    cutData = false;
		                                    flag = false;
		                                }else if(detectionCnt > chunksLeng && totalDetectionCnt > chunksLeng ){
		                                    $("#bodyContents").append("<br><b style=\"color:rgb(51, 153, 255);\">Remaining data truncated</b>" );
		                                    cutData = false;
		                                    flag = false;
		                                }else if(detectionCnt >= chunksLeng ){
		                                    $("#bodyContents").append("<br><b style=\"color:rgb(51, 153, 255);\">Remaining data truncated</b>" );
		                                    cutData = false;
		                                    flag = false;
		                                }else {
		                                    $("#bodyContents").append( document.createTextNode(text) );
		                                    flag = false;
		                                }
		                            }else{
		                                $("#bodyContents").append( document.createTextNode(text));
		                                flag = false;
		                                
		                            }
		                        }
	               			}
	                   		$("#bodyContents").append('</br>');
	                   	
	               		} else {
	               			$("#bodyContents").append( document.createTextNode(text) );
	               			$("#bodyContents").append('</br>');
	               			 
	               		}
					}
      		   			
    	   		 } 
   			}
           } else {
        	   $("#bodyContents").append("<b style='color:blue;'>Remaining data truncated</b>");
           }
           
           $(".matchTypeList>a").click(function() {
               if ($(this).next('ul').is(':visible')) {
                   $(this).children("img").attr("src", '${pageContext.request.contextPath}/resources/assets/images/arrow_deactive.png');
                   $(this).next('ul').hide();
               } else {
                   $(this).children("img").attr("src", '${pageContext.request.contextPath}/resources/assets/images/arrow_active.png');
                   $(this).next('ul').show();
               }
           });

           $("#taskWindow").show();
       },
       beforeSend: function(){
    	   Loading();
       },
       complete: function () {
    	   closeLoading();
       },
       error: function (request, status, error) {
           alert("Recon Server에 접속할 수 없습니다.");
       }
   });
}
</script>
</html>
