
(function() {
    if (typeof jQuery !== 'undefined') {
        setupGlobalAjax();
    } else {
        document.addEventListener('DOMContentLoaded', function() {
            setupGlobalAjax();
        });
    }
    
    function setupGlobalAjax() {
		$.ajaxSetup({
		    beforeSend: function(xhr, settings) {
		        xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
		    },
		    complete: function(xhr, status) {
		        if (xhr.status === 401) {
		            alert("세션이 만료되었습니다. 다시 로그인해주세요.");
		            location.href = "/";
		        }
		    }
		});
    }
})();


var isNull = function(value) { 
	var ret = null;
	if( value == "" || value == null || value == undefined || value == "undefined" || 
			( value != null && typeof value == "object" && !Object.keys(value).length ) ) { 
		ret = true;
	}
	else {
		ret = false;
	}

	//console.log("isEmpty == " + ret);
	return ret;
}

function getToday()
{
	var date = new Date();

	return (date.getFullYear()).toString() + (("00" + (date.getMonth() + 1).toString()).slice(-2)) + ("00" + (date.getDate()).toString()).slice(-2);
}

function getDateTime(date, period, interval)
{
	if (isNull(date)) date = new Date();
	
	if (!isNull(period)) {
		if (period == "mi") date = new Date(date.getTime() + interval*60000);
	}
	
	return (date.getFullYear()).toString() + (("00" + (date.getMonth() + 1).toString()).slice(-2)) + ("00" + (date.getDate()).toString()).slice(-2)
			+ ("00" + (date.getHours()).toString()).slice(-2)
			+ ("00" + (date.getMinutes()).toString()).slice(-2)
			+ ("00" + (date.getSeconds()).toString()).slice(-2);
}

var add_minutes =  function (dt, minutes) {
    return new Date(dt.getTime() + minutes*60000);
}

function convertUnixTime2Date(t) {     
    var a = new Date(t * 1000);
    var today = new Date();
    var yesterday = new Date(Date.now() - 86400000);
    var months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    var year = a.getFullYear();
    var month = a.getMonth() + 1;
    var date = a.getDate();
    var hour = a.getHours();
    var min = a.getMinutes();
    var sec = a.getSeconds();
    if (a.setHours(0,0,0,0) == today.setHours(0,0,0,0))
        return 'today, ' + hour + ':' + min;
    else if (a.setHours(0,0,0,0) == yesterday.setHours(0,0,0,0))
        return 'yesterday, ' + hour + ':' + min;
    else if (year == today.getFullYear())
        return year + "-" + month + "-" + date + " " + hour + ":" + min;
    else
    	return year + "-" + month + "-" + date + " " + hour + ":" + min;
}

function setCookie(cookieName, value, exdays){
    var exdate = new Date();
    exdate.setDate(exdate.getDate() + exdays);
    var cookieValue = escape(value) + ((exdays==null) ? "" : "; expires=" + exdate.toGMTString());
    document.cookie = cookieName + "=" + cookieValue;
}
 
function deleteCookie(cookieName){
    var expireDate = new Date();
    expireDate.setDate(expireDate.getDate() - 1);
    document.cookie = cookieName + "= " + "; expires=" + expireDate.toGMTString();
}
 
function getCookie(cookieName) {
    cookieName = cookieName + '=';
    var cookieData = document.cookie;
    var start = cookieData.indexOf(cookieName);
    var cookieValue = '';
    if(start != -1){
        start += cookieName.length;
        var end = cookieData.indexOf(';', start);
        if(end == -1)end = cookieData.length;
        cookieValue = cookieData.substring(start, end);
    }
    return unescape(cookieValue);
}
//숫자 타입에서 쓸 수 있도록 format() 함수 추가
Number.prototype.format = function(){
    if(this==0) return 0;
 
    var reg = /(^[+-]?\d+)(\d{3})/;
    var n = (this + '');
 
    while (reg.test(n)) n = n.replace(reg, '$1' + ',' + '$2');
 
    return n;
};
 
// 문자열 타입에서 쓸 수 있도록 format() 함수 추가
String.prototype.format = function(){
    var num = parseFloat(this);
    if( isNaN(num) ) return "0";
 
    return num.format();
};

String.prototype.replaceAll = function(org, dest){
	return this.split(org).join(dest);
}

function BooleanFilter() {}

BooleanFilter.prototype.init = function(params) {
    this.params = params;
    this.filterValue = null;
    
    // 필터 UI 생성
	this.eGui = document.createElement('div');
	this.eGui.style.width = '150px'; // 최소 너비 설정
	this.eGui.innerHTML = 
	    '<div style="padding: 10px; min-width: 150px;">' +
	        '<select id="booleanFilterSelect" style="width: 100%; height: 33px; min-width: 120px; padding: 5px; border: 1px solid #ccc; border-radius: 3px; font-size: 12px;">' +
	            '<option value="">전체</option>' +
	            '<option value="연결">연결</option>' +
	            '<option value="미연결">미연결</option>' +
	        '</select>' +
	    '</div>';
    
    this.eSelect = this.eGui.querySelector('#booleanFilterSelect');
    this.eSelect.addEventListener('change', this.onFilterChanged.bind(this));
};

BooleanFilter.prototype.getGui = function() {
    return this.eGui;
};

BooleanFilter.prototype.onFilterChanged = function() {
    this.filterValue = this.eSelect.value || null;
    this.params.filterChangedCallback();
};

BooleanFilter.prototype.doesFilterPass = function(params) {
    if (!this.filterValue) return true;
    
    var cellValue = params.data.AGENT_CONNECTED === '연결' ? '연결' : '미연결';
    return cellValue === this.filterValue;
};

BooleanFilter.prototype.isFilterActive = function() {
    return this.filterValue !== null;
};

BooleanFilter.prototype.getModel = function() {
    return this.filterValue ? { value: this.filterValue } : null;
};

BooleanFilter.prototype.setModel = function(model) {
    this.filterValue = model ? model.value : null;
    if (this.eSelect) {
        this.eSelect.value = this.filterValue || '';
    }
};



function getAgGridKoreanLocale() {
    return {
        // String 필터 (포함, 포함하지않음, 공백, 공백아님만)
        contains: '포함',
        notContains: '포함하지 않음',
        blank: '공백',
        notBlank: '공백 아님',

        // Number 필터 (같음, 같지않음, 이상, 이하, 범위만)
        equals: '같음',
        notEqual: '같지 않음',
        greaterThanOrEqual: '이상',
        lessThanOrEqual: '이하',
        inRange: '범위',

        // Date 필터 (같음, 같지않음, 이전, 이후, 범위만)
        before: '이전',
        after: '이후',

        // Boolean 필터
        true: '연결',
        false: '미연결'
    };
}

// 컬럼 배열에 필터 설정을 자동 적용하는 함수 (원하는 옵션만 표시)
function applyColumnFilters(columnDefs) {
    return columnDefs.map(function(col) {
        // filterType이 지정된 경우에만 필터 설정 추가
        if (col.filterType) {
            switch(col.filterType) {
                case 'string':
                    col.filter = 'agTextColumnFilter';
                    col.filterParams = {
                        filterOptions: ['contains', 'notContains', 'blank', 'notBlank']
//						maxNumConditions: 1,          // 조건 1개만 허용
//						defaultJoinOperator: 'AND'    // 기본 연산자 지정 가능
                    };
                    break;

                case 'number':
                    col.filter = 'agNumberColumnFilter';
                    col.filterParams = {
                        filterOptions: ['equals', 'notEqual', 'greaterThanOrEqual', 'lessThanOrEqual', 'inRange']
                    };
                    break;

                case 'date':
                    col.filter = 'agDateColumnFilter';
                    col.filterParams = {
                        filterOptions: ['equals', 'notEqual', 'before', 'after', 'inRange']
                    };
                    break;

				case 'boolean':
				    col.filter = 'booleanFilter';
				    col.filterParams = {};
				    
				    col.filterValueGetter = function(params) {
				        return params.data.AGENT_CONNECTED == '연결' ? '연결' : '미연결';
				    };
				    break;
            }
            delete col.filterType;
        }
        return col;
    });
}












