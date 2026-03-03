/**
 * PICenter TOTALS - Table Sort Utility
 * 모든 데이터 테이블에 컬럼 클릭 정렬 기능 자동 추가
 * - 텍스트, 숫자, 날짜 자동 감지 정렬
 * - 오름차순 ↔ 내림차순 토글
 * - 동적 로딩 테이블 자동 감지 (MutationObserver)
 */
(function() {
    'use strict';

    /** 정렬 대상 테이블 selector */
    var TABLE_SELECTORS = '.data_table, .data_tbl, .rm_data_table, .grid_data_table, .det_table, .status_table';

    /** 정렬 제외 컬럼 텍스트 */
    var SKIP_COLUMNS = ['No', ''];

    /**
     * 모든 데이터 테이블에 정렬 핸들러 등록
     */
    function initTableSort() {
        var tables = document.querySelectorAll(TABLE_SELECTORS);
        tables.forEach(function(table) {
            var thead = table.querySelector('thead');
            var tbody = table.querySelector('tbody');
            if (!thead || !tbody) return;

            var headers = thead.querySelectorAll('th');
            headers.forEach(function(th, colIndex) {
                var text = th.textContent.trim();

                // No, 빈 컬럼, 체크박스 컬럼 제외
                if (SKIP_COLUMNS.indexOf(text) >= 0) return;
                if (th.querySelector('input[type="checkbox"]')) return;

                // 중복 바인딩 방지
                if (th.dataset.sortBound === '1') return;
                th.dataset.sortBound = '1';

                th.classList.add('sortable');
                th.addEventListener('click', function() {
                    sortByColumn(table, colIndex, th);
                });
            });
        });
    }

    /**
     * 컬럼 기준 테이블 정렬
     */
    function sortByColumn(table, colIndex, clickedTh) {
        var tbody = table.querySelector('tbody');
        if (!tbody) return;

        var rows = Array.from(tbody.querySelectorAll('tr'));

        // 빈 메시지 행, colspan 전체 행 제외
        var dataRows = rows.filter(function(row) {
            var cells = row.querySelectorAll('td');
            if (cells.length <= 1) return false;
            if (row.querySelector('.empty_msg, .grid_empty')) return false;
            if (row.querySelector('td[colspan]')) return false;
            return true;
        });

        if (dataRows.length === 0) return;

        // 정렬 방향 결정 (토글)
        var isAsc = clickedTh.classList.contains('sort_asc');
        var dir = isAsc ? 'desc' : 'asc';

        // 같은 테이블의 모든 헤더 정렬 표시 초기화
        table.querySelectorAll('thead th').forEach(function(th) {
            th.classList.remove('sort_asc', 'sort_desc');
        });
        clickedTh.classList.add(dir === 'asc' ? 'sort_asc' : 'sort_desc');

        // 컬럼 데이터 타입 감지
        var colType = detectColumnType(dataRows, colIndex);

        // 정렬 실행
        dataRows.sort(function(rowA, rowB) {
            var cellA = rowA.querySelectorAll('td')[colIndex];
            var cellB = rowB.querySelectorAll('td')[colIndex];
            if (!cellA || !cellB) return 0;

            var a = getCellSortValue(cellA);
            var b = getCellSortValue(cellB);

            // 빈값 처리 (빈값은 항상 뒤로)
            if (a === '' && b === '') return 0;
            if (a === '') return 1;
            if (b === '') return -1;

            var result = 0;

            if (colType === 'number') {
                var numA = parseNumber(a);
                var numB = parseNumber(b);
                result = numA - numB;
            } else if (colType === 'date') {
                var dateA = parseDate(a);
                var dateB = parseDate(b);
                result = dateA - dateB;
            } else {
                result = a.localeCompare(b, 'ko');
            }

            return dir === 'asc' ? result : -result;
        });

        // 정렬된 행 다시 붙이기
        dataRows.forEach(function(row) {
            tbody.appendChild(row);
        });
    }

    /**
     * 셀의 정렬용 값 추출
     * - 아이콘만 있는 셀: title 또는 class 기반 값 반환
     * - badge/status 셀: 텍스트 내용 반환
     * - 일반 셀: textContent 반환
     */
    function getCellSortValue(cell) {
        var text = cell.textContent.trim();

        // 텍스트가 비어있으면 아이콘/badge 등의 속성으로 대체
        if (text === '' || text.length === 0) {
            // 연결 상태 아이콘 (.icon_con / .icon_dicon)
            var icon = cell.querySelector('.icon_con, .icon_dicon');
            if (icon) {
                return icon.classList.contains('icon_con') ? '1_연결' : '0_미연결';
            }
            // title 속성이 있으면 사용
            var titled = cell.querySelector('[title]');
            if (titled) {
                return titled.getAttribute('title');
            }
            // badge 텍스트
            var badge = cell.querySelector('.badge, .status_badge');
            if (badge) {
                return badge.textContent.trim();
            }
        }

        return text;
    }

    /**
     * 컬럼 데이터 타입 자동 감지
     */
    function detectColumnType(rows, colIndex) {
        var numCount = 0;
        var dateCount = 0;
        var sampleSize = Math.min(rows.length, 10);

        for (var i = 0; i < sampleSize; i++) {
            var cell = rows[i].querySelectorAll('td')[colIndex];
            if (!cell) continue;
            var val = getCellSortValue(cell);
            if (val === '' || val === '-') continue;

            if (!isNaN(parseNumber(val))) {
                numCount++;
            }
            if (/^\d{4}[-\/]\d{2}[-\/]\d{2}/.test(val)) {
                dateCount++;
            }
        }

        if (dateCount > sampleSize * 0.5) return 'date';
        if (numCount > sampleSize * 0.5) return 'number';
        return 'string';
    }

    /**
     * 숫자 파싱 (쉼표, %, 건, 개 등 단위 제거)
     */
    function parseNumber(str) {
        var cleaned = str.replace(/[,%건개회개EA]/g, '').replace(/,/g, '').trim();
        return parseFloat(cleaned);
    }

    /**
     * 날짜 파싱
     */
    function parseDate(str) {
        var d = new Date(str.replace(/\//g, '-'));
        return isNaN(d.getTime()) ? 0 : d.getTime();
    }

    /**
     * 초기 설정 + MutationObserver 등록
     */
    function setup() {
        initTableSort();

        // 동적 테이블 로딩 감지
        var debounceTimer = null;
        var observer = new MutationObserver(function() {
            clearTimeout(debounceTimer);
            debounceTimer = setTimeout(initTableSort, 300);
        });

        observer.observe(document.body, {
            childList: true,
            subtree: true
        });
    }

    // DOM 준비 후 실행
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', setup);
    } else {
        setup();
    }

    // 외부에서 수동 호출 가능하도록 전역 노출
    window.initTableSort = initTableSort;

})();
