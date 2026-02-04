
function reportModelDraw(pattern, report_flag, processing, headerList){
	var html = "";
	var html2 = "";
	var htmlCnt = 1;  
	console.log(headerList); 
	console.log(pattern.length);   
	if(report_flag =="simeple_server0"){ // 요약 보고서(결재 내역 조회)
		var htmlCnt2 = 1
		for(var i = 0; pattern.length > i; i++){ 
			var row = pattern[i].split(', ');
			var pattern_name = row[0].split('PATTERN_NAME=').join(''); 
			var pattern_type = row[4].split('ID=').join(''); 
			 
			html2 += "<tr><td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id="+pattern_type+" value="+pattern_type+" name=\"detailReportType\" checked> "+pattern_name+"</td></tr>"; ++htmlCnt2;
		} 
		html2 = "<tr><th rowspan="+htmlCnt2+" style=\"vertical-align:top; padding-top: 15px;\"> 구분</th></tr>" + html2; 
	}else if(report_flag =="summary_server"){ // 상세 보고서(검출 내역 조회) 
		html2 += "<tr><th rowspan=\"3\" style=\"vertical-align:top; padding-top: 15px;\">구분</th></tr>"
		html2 += "<tr><td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id=\"pattern_con\" value=\"pattern_con\" name=\"detailReportType\" checked> 패턴</td></tr>"
		html2 += "<tr><td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id=\"pattern_cnt\" value=\"pattern_cnt\" name=\"detailReportType\" checked> 건수</td></tr>"
	}  
	for(i=0 ; i < headerList.length ; i++){
		if(headerList[i].header_type == "detailData" && report_flag =="summary_server"){ // 상세보고서(검출 상세내역 이전 조회)
			for(var p = 0; pattern.length > p; p++){ 
				var row = pattern[p].split(', ');   
				var pattern_name = row[0].split('PATTERN_NAME=').join('');
				var pattern_type = row[4].split('ID=').join('');
				
				html += "<tr><td colspan=\"2\">" +
							"<input type=\"checkbox\" class=\"detailReportCBox\" id=\""+pattern_type+"\" 	value=\""+pattern_type+"\" 	name=\"detailReportCon\" checked> "+pattern_name+"" +
							"<input  class=\"reportHedaerNmInput\" id=\""+pattern_type+"_name\" value=\""+pattern_name+"\">" +
						"</td></tr>"; ++htmlCnt;  
			}
		}
		html += "<tr><td colspan=\"2\">" +
				"<input type=\"checkbox\" class=\"detailReportCBox\" id=\"host_name\" 	value=\""+headerList[i].header_type+"\" 	name=\"detailReportCon\" checked> "+headerList[i].header_name+
				"<input class=\"reportHedaerNmInput\" id=\""+headerList[i].header_type+"_name\" value=\""+headerList[i].header_name+"\">" +
				"</td></tr>"; ++htmlCnt;
	}
	 
	if(report_flag == "simeple_server0"){ 
		for(var i = 0; processing.length > i; i++){ 
			var row = processing[i].split(', ');
			var pattern_name = row[1].split('PROCESSING_FLAG_NAME=').join('');
			var pattern_type = row[0].split('PROCESSING_FLAG=').join('');
			 
			html += "<tr> <td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id=\"PROCEEING"+pattern_type+"\" 	value=\"PROCEEING"+pattern_type+"\" 	name=\"detailReportCon\" checked> "+pattern_name+"</td></tr>"; ++htmlCnt;
		}
	}
	   
	if (report_flag =="simeple_server1"){
		for(var p = 0; pattern.length > p; p++){ 
			var row = pattern[p].split(', ');   
			var pattern_name = row[0].split('PATTERN_NAME=').join('');
			var pattern_type = row[4].split('ID=').join('');
			
			html += "<tr><td colspan=\"2\">" +
						"<input type=\"checkbox\" class=\"detailReportCBox\" id=\""+pattern_type+"\" 	value=\""+pattern_type+"\" 	name=\"detailReportCon\" checked> "+pattern_name+"" +
						"<input  class=\"reportHedaerNmInput\" id=\""+pattern_type+"_name\" value=\""+pattern_name+"\">" +
					"</td></tr>"; ++htmlCnt;  
		}
	}
	
	html = "<tr><th rowspan="+htmlCnt+" style='vertical-align : top; padding-top: 15px;'>데이터</th></tr>" + html;
	return { html, html2 };
}

function csvReportDownContent(pattern){
	var html = "";
	var htmlCnt = 0;
	html += "<tr> <td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id=\"host_name\" 	value=\"host_name\" 	name=\"detailReportCon\" checked> 호스트</td></tr>"; ++htmlCnt;
	html += "<tr> <td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id=\"connected_ip\" value=\"connected_ip\" name=\"detailReportCon\" checked> 아이피</td></tr>"; ++htmlCnt;
	html += "<tr> <td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id=\"connected\" 	value=\"connected\" 	name=\"detailReportCon\" checked> 연결상태</td></tr>"; ++htmlCnt; 
	html += "<tr> <td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id=\"path\" 		value=\"path\" 		name=\"detailReportCon\" checked> 위치</td></tr>"; ++htmlCnt;
	html += "<tr> <td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id=\"owner\" 		value=\"owner\" 		name=\"detailReportCon\" checked> 파일소유자</td></tr>"; ++htmlCnt;
	html += "<tr> <td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id=\"modifiedDT\" 	value=\"modifiedDT\" 	name=\"detailReportCon\" checked> 파일수정일</td></tr>"; ++htmlCnt;
	html += "<tr> <td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id=\"createdDT\" 	value=\"createdDT\" 	name=\"detailReportCon\" checked> 파일생성일</td></tr>"; ++htmlCnt;
	html += "<tr> <td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id=\"TotalCnt\" 	value=\"TotalCnt\" 	name=\"detailReportCon\" checked> 총 검출 수</td></tr>"; ++htmlCnt;
	
	for(var i = 0; pattern.length > i; i++){ 
		var row = pattern[i].split(', ');
//		"PATTERN_NAME=주민등록번호, PATTERN_NAME_EN=South Korean RRN, MASK_CNT=6, MASK_TYPE=B, ID=TYPE1, COLOR_CODE=#DC143C, MASK_CHK="
		var pattern_name = row[0].split('PATTERN_NAME=').join('');
		var pattern_type = row[4].split('ID=').join('');
		
		html += "<tr> <td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id=\""+pattern_type+"\" 	value=\""+pattern_type+"\" 	name=\"detailReportCon\" checked> "+pattern_name+"</td></tr>"; ++htmlCnt;
	}
	
	html += "<tr> <td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id=\"detailData\" value=\"detailData\"	name=\"detailReportCon\" checked> 검출 내역</td></tr>"; ++htmlCnt;
	html += "<tr> <td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id=\"etc\" 		  value=\"etc\" 		name=\"detailReportCon\" checked> 비고</td></tr>"; ++htmlCnt;
	++htmlCnt 
	html = "<tr><th rowspan="+htmlCnt+" style='vertical-align : top; padding-top: 15px;'>데이터</th></tr>" + html;
	
	return html;
}

function csvReportSimpleDownContent(pattern, processing){
	var html = "";
	var addHtml = "";
	var htmlCnt = 0;
	
	html += "<tr> <td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id=\"host_name\" 	value=\"host_name\" 	name=\"detailReportCon\" checked> 호스트</td></tr>"; ++htmlCnt;
	html += "<tr> <td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id=\"connected_ip\" value=\"connected_ip\" 	name=\"detailReportCon\" checked> IP</td></tr>"; ++htmlCnt;
	html += "<tr> <td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id=\"connected\" 	value=\"connected\" 	name=\"detailReportCon\" checked> 연결상태</td></tr>"; ++htmlCnt;
	html += "<tr> <td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id=\"lastScanTime\" value=\"lastScanTime\" 	name=\"detailReportCon\" 	checked> 마지막 검색 일자</td></tr>"; ++htmlCnt;
	html += "<tr> <td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id=\"patternCnt\" 	value=\"patternCnt\" 	name=\"detailReportCon\" checked> 개인정보 유형</td></tr>"; ++htmlCnt;
	
	for(var i = 0; processing.length > i; i++){ 
		var row = processing[i].split(', ');
		var pattern_name = row[1].split('PROCESSING_FLAG_NAME=').join('');
		var pattern_type = row[0].split('PROCESSING_FLAG=').join('');
		 
		html += "<tr> <td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id=\"PROCEEING"+pattern_type+"\" 	value=\"PROCEEING"+pattern_type+"\" 	name=\"detailReportCon\" checked> "+pattern_name+"</td></tr>"; ++htmlCnt;
	}
	
	++htmlCnt 
	html = "<tr><th rowspan="+htmlCnt+" style='vertical-align : top; padding-top: 15px;'>데이터</th></tr>" + html;
	
	// 개인정보 유형 추가
	htmlCnt = 1;
	for(var i = 0; pattern.length > i; i++){ 
		var row = pattern[i].split(', ');
		var pattern_name = row[0].split('PATTERN_NAME=').join(''); 
		var pattern_type = row[4].split('ID=').join(''); 
		
		addHtml += "<tr><td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id="+pattern_type+" value="+pattern_type+" name=\"detailReportType\" checked> "+pattern_name+"</td></tr>"; ++htmlCnt;
	} 
	addHtml = "<tr><th rowspan="+htmlCnt+" style=\"vertical-align:top; padding-top: 15px;\">개인정보 유형</th></tr>" + addHtml; 
	
	$("#simpleReportData").html(addHtml)
	return html;
}

function pdfReportDownContent(pattern){
	var html = "";
	var htmlCnt = 0;
	html += "<tr> <td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id=\"Total_Cnt_Pie\" 	value=\"Total_Cnt_Pie\" 	name=\"detailReportCon\" checked> 유형별 검출 현황(그래프)</td></tr>"; ++htmlCnt;
	html += "<tr> <td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id=\"Server_Top_Pie\" value=\"Server_Top_Pie\" name=\"detailReportCon\" checked> 서버 TOP10 검출 현황(그래프)</td></tr>"; ++htmlCnt;
	html += "<tr> <td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id=\"host_name\" 	value=\"host_name\" 	name=\"detailReportCon\" checked> 호스트</td></tr>"; ++htmlCnt;
	html += "<tr> <td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id=\"path\" 		value=\"path\" 		name=\"detailReportCon\" 	checked> 위치</td></tr>"; ++htmlCnt;
	
	for(var i = 0; pattern.length > i; i++){ 
		var row = pattern[i].split(', ');
//		"PATTERN_NAME=주민등록번호, PATTERN_NAME_EN=South Korean RRN, MASK_CNT=6, MASK_TYPE=B, ID=TYPE1, COLOR_CODE=#DC143C, MASK_CHK="
		var pattern_name = row[0].split('PATTERN_NAME=').join('');
		var pattern_type = row[4].split('ID=').join('');
		
		html += "<tr> <td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id=\""+pattern_type+"\" 	value=\""+pattern_type+"\" 	name=\"detailReportCon\" checked> "+pattern_name+"</td></tr>"; ++htmlCnt;
	}
	
	html += "<tr> <td colspan=\"2\"><input type=\"checkbox\" class=\"detailReportCBox\" id=\"detailData\" value=\"detailData\"	name=\"detailReportCon\" checked> 검출 내역</td></tr>"; ++htmlCnt;
	++htmlCnt 
	html = "<tr><th rowspan="+htmlCnt+" style='vertical-align : top; padding-top: 15px;'>데이터</th></tr>" + html;
	
	return html;
}
