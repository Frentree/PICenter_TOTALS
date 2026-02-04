var GridName;
var requestUrl;
var rules = [];
var multiDaySearch = false;
$(document).ready(function() {
	
	// datepicker ui 설정 
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
	
	// 조회 컬럼 on,off 필터 팝업 숨김
	$(document).click(function(e){
		 if (!$(e.target).hasClass('toggle-checkbox') && !$(e.target).hasClass('toggle-label')) {
			 $("#tableCustomData").hide();
		 }
	    
	});
	
 // 검색 기준 변경 시
  $("#searchFilter").change(function () {
	  var filterList = JSON.parse($("#searchFilter").val());
	  var type = filterList.type;
	  selectBoxOption(type)
	  automaticCompletion(type);
	  
    });
  
	// 조회 컬럼 on,off ui 
	$("#tableShow").click(function(e){
		e.stopPropagation();
		var gridHtml = "";
		var gridCnt = 0
		  
		var GridModel = $(GridName).jqGrid('getGridParam', 'colModel');
		 console.log("GridModel", GridModel);
		 console.log("colModel", colModel); 
		 
		 var gridChk = false;
		try {
			$.each(colModel, function(key, value){
				
				if(!gridChk && GridModel[key].name == "cb"){
					gridChk = true;
				}else{
					// 숨김 처리가 되어있거나, type이 0인경우
					if(((!value.hidden) && value.type != 0)  || (value.hidden && value.type == 99) ){
						gridHtml += "<tr><td style=\"height: 40px; padding-right: 10px;\">";
						gridHtml +=  colModel[key].label ;
						gridHtml += "<td><td class=\"toggle-switch\">";
						gridHtml += "<input type=\"checkbox\" id=\""+value.index+"\" class=\"toggle-checkbox\"";
						if(gridChk) key += 1;
						gridHtml += (GridModel[key].hidden ? ">" : "checked=\"checked\">" );
						gridHtml += "<label for=\""+value.index+"\" class=\"toggle-label\"></label>";
						gridHtml += "<td></tr>";
					}
				}
			});
			
			// 길어질 경우를 대비하여 스크롤 생성
			if(gridCnt > 10){
				$("#tableCustomData").css("height", "405px");
				$("#tableCustomData").css("overflow-y", "auto");
			}
			
			$("#gridListTd").html(gridHtml);	
			$("#tableCustomData").show();
			
			$('.toggle-checkbox').change(function () {
				
				if ($(this).is(':checked')) {
					$(GridName).jqGrid('showCol', $(this).attr('id'));
				} else {
					$(GridName).jqGrid('hideCol', $(this).attr('id'));
				}
			});
		} catch (e) {
			console.log("e",e);
		}
		
	});
	
});

// type의 따라 검색 필터 변경
function searchListAppend(){
	var searchHtml = "";
	var addHtml = "";
	var DayHtml = "";
	var DayOptionHtml = "<th style=\"text-align: center; border-radius: 0.25rem; width: 92px;\" class=\"searchDay fixSearchDate\">";
		DayOptionHtml += "<select id=\"searchDay\" >";
		
	var firstType = "None";
	$.each(colModel, function(key, value){
		
		var dataList = colModel[key];
		
		var type = dataList.type;
		
		// 검색 기준 가장 첫번째 type 조회
		if(!dataList.hidden && firstType == "None" && type != 0) firstType = type;
		
		var searchType = (dataList.searchType==null?true:dataList.searchType); // DB 검색 유무 (false 시 DB 검색)
		var jsonOb = '{"field":"'+dataList.name+'","type":"'+type+'","searchType":"'+searchType+'"}'
		
		if(((!value.hidden) && value.type != 0 && !(typeof type === "string" && type.includes("3_"))) || (value.hidden && value.type == 99) ){
			searchHtml += "<option value='"+jsonOb+"'>"+dataList.label+"</option>";
		} 
		   
		if (type != null && type != 0 && type != 99 &&  !(typeof type === "string" && type.includes("3"))){
            if (type == 3) {  // 날짜 검색
                addHtml += "<td style=\"text-align: center; border-radius: 0.25rem; display:none;\"  id=\"dataCheckTd\" class=\"searchName dataCheckTd\">";
                addHtml += "<input type=\"date\" class=\"searchDate fromDate\" id=\"fromDate\" style=\"text-align:center; width:125px;\" >";
                addHtml += "~";
                addHtml += "<input type=\"date\" class=\"searchDate toDate\" id=\"toDate\" style=\"text-align:center; width:125px;\" ></td>";
            }else if(type == 4) { // 서버 연결 상태
				addHtml += "<td style=\"text-align: center; border-radius: 0.25rem; display:none;\"  id=\"agentConntedTd\" class=\"searchName\">";
				addHtml += "<select id=\"agentConntedChk\">"
				addHtml += "<option value=\"1\">연결</option>"
				addHtml += "<option value=\"0\">미연결</option></select></td>"
			}else if(type == 5) { // check box 
				addHtml += "<td style=\"text-align: center; border-radius: 0.25rem; display:none;\"  id=\"boxCheckTd\" class=\"searchName\">";
				addHtml += "<select id=\"boxChevvck\">"
				addHtml += "<option value=\"Y\">확인</option>"
				addHtml += "<option value=\"N\">미확인</option></select></td>"
			} else if(type == 6) { // global filter 
				addHtml += "<td style=\"text-align: center; border-radius: 0.25rem; display:none;\"  id=\"globlaFilterTd\" class=\"searchName\">";
				addHtml += "<select id=\"globlaFilterChk\">"
				addHtml += "<option value=\"Targets\">대상</option>"
				addHtml += "<option value=\"Deleted Target\">삭제된 대상</option>"
				addHtml += "<option value=\"All Targets\">전체 대상</option></select></td>"
			} else if(type == 7) {
				addHtml += "<td style=\"text-align: center; border-radius: 0.25rem; display:none;\"  id=\"approvalUserStatusTd\" class=\"searchName\">";
				addHtml += "<select id=\"approvalUserStatus\">"
				addHtml += "<option value=\"approval_8\">결재자</option>"
				addHtml += "<option value=\"approval_6\">합의자</option>"
				addHtml += "<option value=\"approval_7\">통보자</option></select></td>"
			} else {
				addHtml += "<td style=\"text-align: center; border-radius: 0.25rem; display:none;\"  class=\"searchName addSearchTextBox\">";
				addHtml += "<input type=\"text\" style=\"width: 205px; padding-left: 5px;\" size=\"10\" class=\"searchContent "+value.index+"\"  placeholder=\"검색어를 입력하세요\"></td>";
			}
		}  
		
		
		if (typeof type === "string" && type.includes("3_")) {  // 날짜 검색
			var dayList = type.split("_");   
			DayOptionHtml += "<option id=\""+dataList.name+"\" value=\""+dataList.name+"\">"+dataList.label+"</option>";
			multiDaySearch = true;
		}
	}); 
	
	 
	// type에 따른 추가 검색 화면
	$("#defaultSearchTextBox").before(addHtml);
	// 기본 검색 필터
	$("#searchFilter").append(searchHtml);
	 
	
	if(multiDaySearch){ 
		DayHtml += "<td style=\"text-align: center; border-radius: 0.25rem;\"  id=\"fixDataCheckTd\" class=\"searchName dataCheckTd\">";
		DayHtml += "<input type=\"date\" class=\"fixSearchDate fromDate\" id=\"fromDate\" style=\"text-align:center; width:125px;\" >";
		DayHtml += "~";
		DayHtml += "<input type=\"date\" class=\"fixSearchDate toDate\" id=\"toDate\" style=\"text-align:center; width:125px;\" ></td>";
		DayOptionHtml += "</select></th>"
			
		$("#searchDayBox").before(DayOptionHtml);
		$("#searchDayBox").before(DayHtml);
		
		multiDaySearch = false;
	}
	
	// DB 검색 진행 시 엔터 감지 검색 활성화
	$(".searchContent").keyup(function(e){ 
		if (e.keyCode == 13) {
			// 공란 감지
			navGridSearchBtn(); 
			$(".ui-autocomplete").hide();
		}
	});
	
	$(".navGridSearchBtn").click(function(e){
		navGridSearchBtn();  
	});
	
	 $(".fixSearchDate").change(function () {
		  multiDaySearch = true;
	  });
	
	// type에 따라 보여지는 검색 ui 변경
	selectBoxOption(firstType)
	// 삭제 서버, 전체 서버 기준
	globalFilterChk();
	
}

//공란 감지
function navGridSearchBtn(){
	
	if(multiDaySearch){ 
		if($(".searchContent").val() != "" && $(".searchContent").val() != null){
			fnSetFilterBox();
		}
		searchNavGridReload(); // 검색
	}else{
		var alertChk = searchEmptyChek(); // 공란 감지
		if(alertChk=="" || alertChk == null){
			alert("검색어를 입력하세요.");
			return true;
		}else if(alertChk == "noSearch"){ // DB 검색
			fnSetFilterBox(); //검색조건 navGrid 기준에 맞춰 저장
			searchNavGridReload(); // 검색
		}else{
			fnSetFilterBox(); //검색조건 navGrid 기준에 맞춰 저장
			setRules(); // rules저장
			searchNavGridReload(); // 검색
		}
		
	}
	
}

//공란 감지
function searchEmptyChek(){
	var searchFilter = JSON.parse($("#searchFilter").val());
	var type = searchFilter['type'] ;
	var field = searchFilter['field'];
	var searchType = searchFilter['searchType'];
	var columnType =  GridNameSelect(field);
	var val;
	
	console.log("columnType", columnType);
	
	if(columnType != null){
		if (columnType == 4){ // 에이전트 연결 상태
			val = $("#agentConntedChk").val();
		} else if (columnType == 5){ // check box(검색 불가 경로)
			val = $("#boxCheck").val();
		} else if (columnType == 6){ // global filter
			val = "global filter";  
		} else if (columnType == 7){ // global filter
			val = "approval User";  
		} else if (columnType == 3 || columnType == 9){ // global filter
			val = "day filter";
		} else if (typeof columnType === "string" && columnType.includes("3")){
			val = "date";
		} else{
			$(".searchContent").each(function (i, filter){
				if(val == null || val == ""){  
					val = $(".searchContent").eq(i).val().trim();
				}
			});
			
		}
	}else{
		$(".searchContent").each(function (i, filter){
			if(val == null || val == ""){  
				val = $(".searchContent").eq(i).val().trim();
			}
		});
	}
	
	return val;
}

//검색
function searchNavGridReload(url){ 
	if(GridsearchType && !multiDaySearch){ // DB 검색이 없을 경우
		var filters = { groupOp: "AND", rules: rules };
		
		console.log("test", JSON.stringify(filters));
		var currentFiltersObject;
		if(rules.length > 0 ) currentFiltersObject = JSON.parse(JSON.stringify(filters));
		console.log("GridName :: "+ GridName)
		$(GridName).jqGrid('setGridParam', { search: true, postData: { filters: JSON.stringify(filters) } });
		$(GridName).jqGrid('setGridParam', {
		    search: true,
		    postData: { filters: JSON.stringify(filters) },
		    page: 1 // 검색 후 첫 페이지로 초기화
		}).trigger("reloadGrid");
		
	}else{ 
		var oPostDt = {};
		$(".filterBoxContent").each(function (i, filter){
			
			var filterCon = $(filter).val().replaceAll('\\','\\\\');
			var filterList = JSON.parse(filterCon);
			
			var filterType = $(".filterType").eq(i).val();
			
			var filterBoxContent = $(".filterBoxContent").eq(i).val();
			var filterTypeList = JSON.parse(filterType);
			var filterBoxContentList = JSON.parse(filterBoxContent);
			
			if(filterTypeList.field == filterList.field && !JSON.parse(filterTypeList.searchType)){
				var searchID = filterTypeList.field;
				
				if (!oPostDt[searchID]) {
				    oPostDt[searchID] = filterBoxContentList.data;  // 리스트가 없으면 새로 생성
				}
				oPostDt[searchID] = oPostDt[searchID] + "," + filterBoxContentList.data
			}
		});
		
		if($("#searchDay").val() != null){
			oPostDt["dayKey"] = $("#searchDay").val(); 
		    oPostDt["fromDate"] = $("#fromDate").val();
		    oPostDt["toDate"]   = $("#toDate").val();
		}
		
		console.log(oPostDt);
		// 쿼리 조회 
		currentRequest= $.ajax({
			url: requestUrl, // 실제 서버 URL로 변경
			data: oPostDt, 
			method: 'POST',
			datatype: "json",
			treedatatype: 'json',
			success: function(response) {
				applySearchConditionsAjax(response);
			},
			error: function() {
				console.error('Error fetching data from server');
			}
		});
	}
	
	multiDaySearch = false;
	GridsearchType = true;
}


function applySearchConditionsAjax(data) {
	var conditions = [];
	
	var GridNames = $(GridName).jqGrid('getGridParam', 'colNames');
	var GridModel= $(GridName).jqGrid('getGridParam', 'colModel');
	// jqGrid 검색 조건으로 변환
	var filters = { groupOp: "AND", rules: rules };
	console.log("filters", filters)
	$(GridName).jqGrid('clearGridData').jqGrid('setGridParam', {search:true, data: data, postData: { filters: JSON.stringify(filters) } }).trigger("reloadGrid");
}


//검색조건 navGrid 기준에 맞춰 저장
function fnSetFilterBox(){

	var searchFilter = JSON.parse($("#searchFilter").val());
	var type = searchFilter['type'] ;
	var field = searchFilter['field'];
	var searchType = searchFilter['searchType'];
	
	if(GridsearchType){
		if (searchType != null) GridsearchType = JSON.parse(searchType);
	}
	
	var html;
	var jsonOb ='';
	var jsonTypeOb ='';
	html = "<div onselectstart='return false' class='filterBox' style='background-color:white' >";
	
	jsonOb = '{"field":"'+field+'","op":"cn","data":"';
	jsonTypeOb = '{"field":"'+field+'","type":"'+type+'","searchType":"'+searchType+'"}'
	
	var filterText = $("#searchFilter option:checked").text();
	
	var columnType =  GridNameSelect(field);
	 
	if(columnType != null && columnType != 99){
		if(columnType == 3 || columnType == 9){
			jsonOb = '[{"field":"'+field+'","op":"ge","data":"' + $("#fromDate").val() + ' 00:00:00"}';
			jsonOb += ', {"field":"'+field+'","op":"le","data":"' + $("#toDate").val() + ' 23:59:59"}]';
			filterText += " : " + $("#fromDate").val() + " ~ " +  $("#toDate").val() ;
		}else if(columnType == 4){
			jsonOb += $("#agentConntedChk").val()+'"}'
			filterText += ":"+$('#agentConntedChk option:selected').text();
		}else if(columnType == 5){
			jsonOb += $("#boxCheck").val()+'"}'
			filterText += ":"+$('#boxCheck option:selected').text();
		}else if(columnType == 6){
			var global = $("#globlaFilterChk").val();
			
			if(global != "Targets"){
				jsonOb += global+'"}'
				filterText += ":"+$('#globlaFilterChk option:selected').text();
			}else{
				jsonOb += $("#searchContent").val()+'"}'
				filterText += ":"+$("#searchContent").val();
			}  
		}else if(columnType == 7){
			var global = $("#approvalUserStatus").val();
			jsonOb += global+'"}'
			filterText += ":"+$('#approvalUserStatus option:selected').text();
			
		}else if(typeof columnType === "string" && columnType.includes("3") ){ 
			var dayList = columnType.split("_");
			jsonOb = '[{"field":"'+field+'","op":"ge","data":"' + $("#fromDate_"+dayList[1]).val() + ' 00:00:00"}';
			jsonOb += ', {"field":"'+field+'","op":"le","data":"' + $("#toDate_"+dayList[1]).val() + ' 23:59:59"}]';
			filterText += " : " + $("#fromDate_"+dayList[1]).val() + " ~ " + $("#toDate_"+dayList[1]).val()
		}else{
			jsonOb += $("."+field).val()+'"}'
			filterText += ":"+$("."+field).val();
		}
	}else{
		jsonOb += $("#searchContent").val()+'"}'
		filterText += ":"+$("#searchContent").val();
	}
	  
	if(typeof columnType === "string" && columnType.includes("3")){
		if(filterText.length>20)filterText = filterText.substring(0,20)+'...';
	}
	
	html += filterText;
	html += "<input type='hidden' class='filterBoxContent' value='"+jsonOb+"'>";
	html += "<input type='hidden' class='filterType' value='"+jsonTypeOb+"'>";
	
	html += "&nbsp;<div style='display:inline-block;cursor:pointer;font-size:medium' onclick='fnFilterRemove(this,\"0\", "+searchType+")'>X</div>"
	html += '</div>';
	
	if(type!=8) $(".searchFilterBox").append(html);
	$(".searchContent").val(""); 
	$("."+field).val("");  
}
 
function fnFilterRemove(element,flag, searchType) {
    element.parentNode.remove();
    
    GridsearchType = JSON.parse(searchType);
     
    setRules(); //변수 담기
    searchNavGridReload()
    
}

function setRules() {
	rules = [];
	$(".filterBoxContent").each(function (i, filter){
		var boxType = $(".filterType").eq(i).val();
		var filterTypeList = JSON.parse(boxType);
		
		var allData = $(GridName).jqGrid('getRowData');
		
		var filterCon = $(filter).val().replaceAll('\\','\\\\');
		console.log(filterCon);
		var filterList = JSON.parse(filterCon);
		
		if(JSON.parse(filterTypeList.searchType)){
			if(typeof filterTypeList.type === "string" &&  filterTypeList.type.includes("3")){
				rules = filterList
			}else {
				rules.push(filterList);
			}
		}
		
	});
}

function GridNameSelect(gridName){
	var columnType;
	var targetColumn = colModel.find(function(column) {
	    return column.name === gridName;
	});
	
	if (targetColumn) {
	    columnType = targetColumn.type;
	}
	
	
	return columnType;
}

function selectBoxOption(type){
	$("#defaultSearchTextBox").hide();
	$("#agentConntedTd").hide();
	$("#boxCheckTd").hide(); 
	$(".addSearchTextBox").hide();
	$("#dataCheckTd").hide();
	$("#globlaFilterTd").hide();
	$("#approvalUserStatusTd").hide();
//	$("#navGridSearchDiv").css("width", "310px");
	 console.log("type", type);
	if(type !=null && typeof type === "string" && type.includes("3")){ // 날짜 검색(고정값)
		setSelectDate();
		var dayList = type.split("_");
		$(".dataCheckTd").show();
		$("#navGridSearchDiv").css("width", "420px")
	}else if(type !=null && (type == 3 || type == 9)){ // 날짜 검색
		$("#dataCheckTd").show();
	}else if(type !=null && type == 4){ // 에이전트 연결 상태
		$("#agentConntedTd").show();
	}else if(type !=null && type == 5){ // check box
		$("#boxCheckTd").show(); 
	}else if(type !=null && type == 6){ // global filter
		$("#globlaFilterTd").show();
		var globalChk = $("#globlaFilterChk").val();
		
		if(globalChk != "Targets"){    
			$("#defaultSearchTextBox").hide();
		}else{
		  $("#defaultSearchTextBox").show();
		  }
		
	}else if(type !=null && type == 7){ // global filter
		$("#approvalUserStatusTd").show();
	}else if(type != "null" && type != null && type != "undefined" && type != 99 ){
		$(".addSearchTextBox").show();
	}else{
		$("#defaultSearchTextBox").show();
	}
	 
}

function automaticCompletion(type){ 
	if(type==null || typeof type != "string" || type != 3 || type != 4 || type != 5 || type != 6 || type != 9){
		  
		var filterList = JSON.parse($("#searchFilter").val());
		var selectOptionName = filterList.field;
		
		var dupSelectColData =  $(GridName).jqGrid("getRowData").map(function (row){
			return row[selectOptionName];
		}) 
		
		// 중복 제거
		var set = new Set(dupSelectColData); 
		var selectColData = [...set]; 
		  
		$("#searchContent").autocomplete({
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
	}else if (type == 9){ 
		
	}
	
}

function automaticCompletion2(id, colName){ 
	var dupSelectColData =  $(GridName).jqGrid("getRowData").map(function (row){
		return row[colName];
	}) 
	
	// 중복 제거
	var set = new Set(dupSelectColData); 
	var selectColData = [...set]; 
	  
	$("."+id).autocomplete({
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
	
}


function globalFilterChk(){
	$("#globlaFilterChk").change(function () {
		var globalChk = $("#globlaFilterChk").val();
		
		if(globalChk != "Targets"){    
			$("#defaultSearchTextBox").hide();
		}else{
		  $("#defaultSearchTextBox").show();
		  }
	});
}

function setSelectDate()  
{	
    $(".fromDate").datepicker({
        changeYear : true, 
        changeMonth : true, 
        dateFormat: 'yy-mm-dd'
    });

    $(".toDate").datepicker({
        changeYear : true,
        changeMonth : true,
        dateFormat: 'yy-mm-dd'
    });

    var oToday = new Date();
    $(".toDate").val(getFormatDate(oToday));
    console.log(getFormatDate(oToday))
    var oFromDate = new Date(oToday.setDate(oToday.getDate() - 30));
    $(".fromDate").val(getFormatDate(oFromDate));
    console.log(getFormatDate(oFromDate)) 
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
