var opMap = { "eq": "같다","ne": "같지 않다","lt": "작다","le": "작거나 같다","gt": "크다","ge": "크거나 같다","bw": "로 시작한다","bn": "로 시작하지 않는다","in": "내에 있다",
			"ni": "내에 있지 않다","ew": "로 끝난다","en": "로 끝나지 않는다","cn": "내에 존재한다","nc": "내에 존재하지 않는다","nu": 'is null',"nn": 'is not null',"bt": 'between'};
			
var retrunOpMap = {	"같다": "eq","같지 않다": "ne","작다": "lt","작거나 같다": "le","크다": "gt","크거나 같다": "ge","로 시작한다": "bw","로 시작하지 않는다": "bn","내에 있다": "in",
					"내에 있지 않다": "ni","로 끝난다": "ew","로 끝나지 않는다": "en","내에 존재한다": "cn","내에 존재하지 않는다": "nc","is null": "nu","is not null": "nn","between": "bt"};

var GridName;

$(document).ready(function() {
	
	$(document).click(function(e){
		 if (!$(e.target).hasClass('toggle-checkbox') && !$(e.target).hasClass('toggle-label')) {
			 $("#tableCustomData").hide();
		 }
	    
	});
	
	$("#tableShow").click(function(e){
		e.stopPropagation();
		var gridHtml = "";
		var gridCnt = 0
		
		try {
			$.each(colModel, function(key, value){
				if((!value.hidden) && value.type != 0 ){
					gridHtml += "<tr><td style=\"height: 40px; padding-right: 10px;\">";
					gridHtml +=  colModel[key].label ;
					gridHtml += "<td><td class=\"toggle-switch\">";
					gridHtml += "<input type=\"checkbox\" id=\""+value.index+"\" class=\"toggle-checkbox\"";
					gridHtml += (colModel[key].hidden ? ">" : "checked=\"checked\">" );
					gridHtml += "<label for=\""+value.index+"\" class=\"toggle-label\"></label>";
					gridHtml += "<td></tr>";
				}
			});
			
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
	
	$(".btn_grid_Search").click(function(e){
		
		try {
			// TODO: handle exception
			$(GridName).jqGrid('searchGrid', {
				multipleSearch: true,  
				multipleGroup: true, 
				showQuery: false,
				searchOnEnter: true, 
				recreateFilter: true,  // 검색 팝업 재생성 보장
				sopt: ['eq', 'ne', 'lt', 'le', 'gt', 'ge', 'bw', 'bn',  'ew', 'en', 'cn', 'nc', 'nu', 'nn', 'bt'],
				afterShowSearch: function($form) {  
					$form.closest(".ui-jqdialog").find("table.ui-search-table").removeClass("ui-search-table");
			}, 
			onSearch: function () {
				// 검색 조건을 가져와서 화면에 표시
				var postData = $(GridName).jqGrid('getGridParam', 'postData');
				var filters = JSON.parse(postData.filters);
				var colModel = $(GridName).jqGrid('getGridParam', 'colModel');
				var searchConditionsContainer = $("#searchConditionsContainer");
				
				// 이전 검색 조건 제거
				searchConditionsContainer.empty(); 
				
				filters.rules.forEach(function(rule) {
					var colName = colModel.find(function(col) {
						return col.index === rule.field;
					}).label;
					
					var opName = opMap[rule.op] || rule.op; // 한국어로 변환된 연산자명
					
					var displayValue = rule.data;
					var priceMap = {};
					if (rule.field === 'AGENT_CONNECTED') {
						priceMap = {"1": "연결","0": "미연결"};
						displayValue = priceMap[rule.data] || rule.data;
					}else if (rule.field === 'CHK_STATUS') {
						priceMap = {"Y": "확인","0": "미확인"};
						displayValue = priceMap[rule.data] || rule.data;
					}else if (rule.field === 'REG_STATUS') {
						priceMap = {"Y": "등록","0": "미등록"};
						displayValue = priceMap[rule.data] || rule.data;
					}
					
					// 검색 조건 상자 생성
					var conditionBox = $('<div class="search-condition-box"></div>').text(colName + "(" + opName + "): " + displayValue);
//                 var conditionBox = $('<div class="search-condition-box"></div>').text(rule.field + ": " + rule.data);
					var closeButton = $('<button type="button" class="close-button">&times;</button>');
					
					// 닫기 버튼 클릭 이벤트
					closeButton.on('click', function() {
						conditionBox.remove();
						// 필요한 경우 조건을 다시 적용 
						applySearchConditions();
					});
					
					conditionBox.append(closeButton);
					searchConditionsContainer.append(conditionBox);
				});
			} 
			
		});
			
		} catch (e) {
			console.log("e :: ",e)
		}
		
		
		var currentFilters = $(GridName).jqGrid('getGridParam', 'postData').filters;
		console.log("currentFilters", currentFilters);
		var currentFiltersObject;
		if(currentFilters != null ) currentFiltersObject = JSON.parse(currentFilters);
		 
		console.log("currentFiltersObject", currentFiltersObject ); 
	
		
		$(GridName).jqGrid('setGridParam', { postData: { filters: JSON.stringify(currentFiltersObject) } }).trigger("reloadGrid");
	
	});
	 
//검색 조건 적용 함수
	function applySearchConditions() {
		var conditions = [];
		$("#searchConditionsContainer .search-condition-box").each(function() {
			var text = $(this).text().replace('×', '').trim();
			// 조건 파싱 로직 추가 필요
			conditions.push(text);
		});
		
		var GridNames = $(GridName).jqGrid('getGridParam', 'colNames');
		var GridModel= $(GridName).jqGrid('getGridParam', 'colModel');
		
		// jqGrid 검색 조건으로 변환
		var postData = $(GridName).jqGrid('getGridParam', 'postData');
		
		var filters = { groupOp: JSON.parse(postData.filters).groupOp, rules: [] };
		conditions.forEach(function(condition) {
			var parts = condition.split("("); 
			
			var field = GridModel.find(function(col)  {
				return col.label === parts[0];
			}).index; 
			parts = parts[1].split("): "); 
			var data = parts[1];
			
			var op = retrunOpMap[parts[0]] || null; 
			
			if(op!= null){
				filters.rules.push({ field: field, op: op, data: data });
			}
			
		});
		
		// 검색 조건을 jqGrid에 적용
		$(GridName).jqGrid('setGridParam', { postData: { filters: JSON.stringify(filters) } }).trigger("reloadGrid");
	}
	
});
