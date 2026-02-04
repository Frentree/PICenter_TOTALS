<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="nowDate" class="java.util.Date" />


<%@ include file="../../include/header_rm.jsp"%>
<style>
h4 {
	margin : 5px 0;
	font-size: 0.8vw;
}
#licenseGrid * *{
	overflow: hidden; 
	white-space: nowrap; 
	text-overflow: ellipsis; 
}
.ui-jqgrid tr.ui-row-ltr td{
	cursor: pointer;
}
#insertNoticeContent::placeholder{
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
			<h3>라이선스 관리</h3>
			<div class="content magin_t25" style="margin-top: 22px;">
				<div class="grid_top"> 
					<table class="user_info" style="display: inline-table; width: 525px; border: 0px solid;">
						<caption>라이선스</caption>
						<colgroup>
							<col width="10%">
							<col width="*">
						</colgroup>
						<tbody id="licenseTable">
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</section>
	<%@ include file="../../include/footer.jsp"%>

<script type="text/javascript"> 

$(document).ready(function () {
	
	var postData = {};
	
	var licenseData = "${licenseData}";
	var licenseSize = "${licenseSize}";
	
	licenseData = licenseData.split('[{').join('').split('}]').join('');
	licenseData = licenseData.split('}, {');
	
	if(licenseSize > 0){	
		var html = "";
		for(l=0 ; l < licenseSize ; l++){
			
			var row = licenseData[l].split(', ');
			console.log(row);
			var server = row[0].split('server=').join(''); 
			var total = row[1].split('total=').join(''); 
			var expire = row[2].split('expire=').join(''); 
			var usage = row[3].split('usage=').join(''); 
			var company = row[4].split('company=').join(''); 
			var status = row[5].split('status=').join(''); 
// 			status = status.split('=');
			
			console.log(server);
			console.log(total);
			console.log(expire);
			console.log(usage);
			console.log(company);
			console.log(status);
			
			var client = "${pic_version.pic_client}";
			
			html += "<tr><th style=\"font-size : 18px; font-weight: bold;\" colspan=2>"+server+" - "+ status +"</th></tr>";
			html += "<tr><th style=\"text-align: center; width: 100px; padding: 5px 5px 0 5px; border-radius: 0.25rem;\">";
			html += "라이선스 </th>";
			html += "<td style=\"padding: 5px 5px 0 5px;\">";
			if(client == null || client == ""){
				html += "<label>PICenter 2.0 RM</label></td>";
			}else{
				html += "<label>"+client+"</label></td>";
			}
			html += "</tr><tr>";
			html += "<th style=\"text-align: center; width: 100px; padding: 5px 5px 0 5px; border-radius: 0.25rem;\"> 만료일</th>";
			html += "<td style=\"padding: 5px 5px 0 5px;\"><label>"+expire+"</label></td>";
			html += "</tr><tr>";
			html += "<th style=\"text-align: center; width: 100px; padding: 5px 5px 0 5px; border-radius: 0.25rem;\"> 사용량</th>";
			html += "<td style=\"padding: 5px 5px 0 5px;\"><label>"+usage + " / " + total +"</label></td>";
			html += "</tr><tr style=\"height:20px;\"></tr>"
		}
		
		$("#licenseTable").html(html);
		
		console.log(html);
	}
	
	$(document).click(function(e){
		$("#taskWindow").hide(); 
	});
	
	var gridWidth = $("#licenseGrid").parent().width();
	var gridHeight = 555;
	
	$("#SCH_D_P_C_G_REGDATE_CHK").change(function() {
    	if($(this).is(":checked") == true) {
    		// 달력 활성
    		$("#fromDate").datepicker('enable');
    		$("#toDate").datepicker('enable');
    		$("#fromDate").css({'background-color':'#FFFFFF'});
    		$("#toDate").css({'background-color':'#FFFFFF'});
    	} else {
    		// 달력 비활성
    		$("#fromDate").datepicker('disable');
    		$("#toDate").datepicker('disable');
    		$("#fromDate").css({'background-color':'#F0F0F0'});
    		$("#toDate").css({'background-color':'#F0F0F0'});
    	}
    });

	//조회
	var postData = {};
	
	setSelectDate();
});
var count = 0;

// 날짜
function setSelectDate() 
{
    $("#fromDate").datepicker({
        changeYear : true,
        changeMonth : true,
        dateFormat: 'yy-mm-dd'
    });

    $("#toDate").datepicker({
        changeYear : true,
        changeMonth : true,
        dateFormat: 'yy-mm-dd'
    });

    var oToday = new Date();
    $("#toDate").val(getFormatDate(oToday));

    var oFromDate = new Date(oToday.setDate(oToday.getDate() - 30));
    $("#fromDate").val(getFormatDate(oFromDate));
}
function getFormatDate(oDate)
{
    var nYear = oDate.getFullYear();           // yyyy 
    var nMonth = (1 + oDate.getMonth());       // M 
    nMonth = ('0' + nMonth).slice(-2);         // month 두자리로 저장 

    var nDay = oDate.getDate();                // d 
    nDay = ('0' + nDay).slice(-2);             // day 두자리로 저장

    return nYear + '-' + nMonth + '-' + nDay;
}

$(function() { 
	$.datepicker.setDefaults({ 
		closeText: "확인", 
		currentText: "오늘", 
		prevText: '이전 달', 
		nextText: '다음 달', 
		monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'], 
		monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'], 
		dayNames: ['일', '월', '화', '수', '목', '금', '토'], 
		dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'], 
		dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'], 
		yearRange: 'c-5:c+5'
	}); 
});

var $progressBar = $("#progressBar");
function setProgress(per) {
	$progressBar.val(per);
}


function createFile(cellvalue, options, rowObject) {
	var rowID = options['rowId'];
	var checkboxID = "gridChk" + rowID;
	if (rowObject['FILE_CHK'] == "1"){
		return "<img href='#' src='<%=request.getContextPath()%>/resources/assets/images/file.png' />";
	}
	else { 
		return '';
	}
	
}


</script>
</body>

</html>