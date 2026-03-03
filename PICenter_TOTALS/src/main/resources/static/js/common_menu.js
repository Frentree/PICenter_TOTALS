/**
 * PICenter TOTALS - 공통 메뉴 생성기
 * 모든 페이지에서 동일한 헤더/메뉴를 자동 생성
 * DB PI_HEADER_INFORMATION 테이블 구조 기반
 */
(function() {
    'use strict';

    /**
     * 메뉴 구조 정의 (PI_HEADER_INFORMATION 기준)
     * HEADER_NO 1~7
     */
    var MENU_STRUCTURE = [
        {
            id: 'menu_dashboard',
            name: 'Dashboard',
            url: '/picenter_dashboard',
            headerNo: 1,
            subMenus: []
        },
        {
            id: 'menu_policy',
            name: '정책 관리',
            url: 'javascript:void(0)',
            headerNo: 2,
            subMenus: [
                { name: '검색 정책', url: '/picenter_scan' },
                { name: '개인정보 유형', url: '/picenter_data_type' }
            ]
        },
        {
            id: 'menu_target',
            name: '대상 관리',
            url: '/picenter_target',
            headerNo: 3,
            subMenus: [
                { name: '대상조회 및 현행화', url: '/picenter_target' },
                { name: '대상 그룹 관리', url: '/picenter_target_group' },
                { name: '예외 관리', url: '/picenter_global_filters' }
            ]
        },
        {
            id: 'menu_search',
            name: '검색 관리',
            url: '/picenter_search_regist',
            headerNo: 4,
            subMenus: [
                { name: '검색 실행', url: '/picenter_search_regist' },
                { name: '검색 스케줄', url: '/picenter_scan' },
                { name: '검색 현황', url: '/picenter_search_list' }
            ]
        },
        {
            id: 'menu_result',
            name: '결과 관리',
            url: '/picenter_detection',
            headerNo: 5,
            subMenus: [
                { name: '결과조회/조치계획', url: '/picenter_detection' },
                { name: '조치계획 승인요청', url: '/picenter_exception' },
                { name: '결재 진행현황', url: '/picenter_approval' },
                { name: '통합 보고서', url: '/picenter_report' },
                { name: '통계', url: '/picenter_statistics' },
                { name: '다운로드 대기열', url: '/picenter_download' },
                { name: '조치 이력', url: '/picenter_report_except' }
            ]
        },
        {
            id: 'menu_community',
            name: '커뮤니티',
            url: '/picenter_notice',
            headerNo: 6,
            subMenus: [
                { name: '공지사항', url: '/picenter_notice' },
                { name: 'FAQ', url: '/picenter_faq' },
                { name: '프로그램 다운로드', url: '/picenter_download' }
            ]
        },
        {
            id: 'menu_setting',
            name: '설정',
            url: '/picenter_user',
            headerNo: 7,
            subMenus: [
                { name: '사용자 관리', url: '/picenter_user' },
                { name: '로그관리', url: '/picenter_userlog' },
                { name: '잠금관리', url: '/picenter_user_lockdown' },
                { name: '통합 관리', url: '/picenter_interlock' },
                { name: '무결성 체크', url: '/picenter_node' },
                { name: '시스템설정', url: '/picenter_setting' }
            ]
        }
    ];

    /**
     * 현재 페이지 URL 기반으로 활성 메뉴 판별
     */
    function getActiveMenuIndex() {
        var path = window.location.pathname.replace('.html', '');

        // 각 메뉴의 URL과 서브메뉴 URL 매칭
        for (var i = 0; i < MENU_STRUCTURE.length; i++) {
            var menu = MENU_STRUCTURE[i];
            if (menu.url === path) return i;
            for (var j = 0; j < menu.subMenus.length; j++) {
                if (menu.subMenus[j].url === path) return i;
            }
        }

        // 페이지별 추가 매핑
        var pageMap = {
            '/picenter_dashboard': 0,
            '/picenter_target': 2, '/picenter_target_group': 2,
            '/picenter_global_filters': 2,
            '/picenter_data_type': 1, '/search/data_type': 1,
            '/picenter_search_regist': 3, '/picenter_search_list': 3,
            '/picenter_scan': 3,
            '/picenter_detection': 4, '/picenter_exception': 4,
            '/picenter_approval': 4, '/picenter_report': 4,
            '/picenter_statistics': 4, '/picenter_report_except': 4,
            '/picenter_notice': 5, '/picenter_faq': 5,
            '/picenter_download': 5,
            '/picenter_user': 6, '/picenter_userlog': 6,
            '/picenter_user_lockdown': 6, '/picenter_setting': 6,
            '/picenter_interlock': 6, '/picenter_node': 6
        };

        return pageMap[path] !== undefined ? pageMap[path] : 0;
    }

    /**
     * 헤더 HTML 생성
     */
    function generateHeaderHTML() {
        var activeIdx = getActiveMenuIndex();

        var html = '<div class="header">';
        html += '  <div class="header_left">';
        html += '    <a href="/picenter_dashboard" class="logo">개인정보 검출관리 센터</a>';
        html += '  </div>';
        html += '  <div class="header_center">';
        html += '    <ul class="gnb_menu">';

        for (var i = 0; i < MENU_STRUCTURE.length; i++) {
            var menu = MENU_STRUCTURE[i];
            var isActive = (i === activeIdx);
            var liClass = isActive ? ' class="on"' : '';

            html += '      <li' + liClass + '>';
            html += '        <a href="' + menu.url + '">' + menu.name + '</a>';

            if (menu.subMenus.length > 0) {
                html += '        <ul class="gnb_sub_menu">';
                for (var j = 0; j < menu.subMenus.length; j++) {
                    var sub = menu.subMenus[j];
                    html += '          <li><a href="' + sub.url + '">' + sub.name + '</a></li>';
                }
                html += '        </ul>';
            }

            html += '      </li>';
        }

        html += '    </ul>';
        html += '  </div>';
        html += '  <div class="header_right">';
        html += '    <span class="user_info" id="userInfo"></span>';
        html += '    <button class="btn_logout" id="btnLogout">로그아웃</button>';
        html += '  </div>';
        html += '</div>';

        return html;
    }

    /**
     * 서브메뉴 hover 이벤트 바인딩
     */
    function bindSubMenuEvents() {
        var menuItems = document.querySelectorAll('.gnb_menu > li');
        for (var i = 0; i < menuItems.length; i++) {
            (function(item) {
                var sub = item.querySelector('.gnb_sub_menu');
                if (!sub) return;
                item.addEventListener('mouseenter', function() {
                    sub.style.display = 'block';
                });
                item.addEventListener('mouseleave', function() {
                    sub.style.display = 'none';
                });
            })(menuItems[i]);
        }
    }

    /**
     * 인증 처리
     */
    function setupAuth() {
        var token = localStorage.getItem('picenter_token');
        var userStr = localStorage.getItem('picenter_user');

        if (!token) {
            window.location.href = '/login.html';
            return false;
        }

        // 사용자 정보 표시
        if (userStr) {
            try {
                var user = JSON.parse(userStr);
                var userName = user.userName || user.name || '';
                var userId = user.userId || user.userNo || '';
                var infoEl = document.getElementById('userInfo');
                if (infoEl) {
                    infoEl.textContent = userName + '(' + userId + ')';
                }
            } catch (e) { /* ignore */ }
        }

        // 로그아웃 버튼
        var btnLogout = document.getElementById('btnLogout');
        if (btnLogout) {
            btnLogout.addEventListener('click', function() {
                localStorage.removeItem('picenter_token');
                localStorage.removeItem('picenter_user');
                window.location.href = '/login.html';
            });
        }

        return true;
    }

    /**
     * 공통 API 호출 헬퍼
     */
    window.piApi = {
        getToken: function() {
            return localStorage.getItem('picenter_token');
        },
        fetch: function(url, options) {
            options = options || {};
            options.headers = options.headers || {};
            options.headers['Authorization'] = 'Bearer ' + this.getToken();
            if (!options.headers['Content-Type'] && options.method && options.method !== 'GET') {
                options.headers['Content-Type'] = 'application/json';
            }
            return fetch(url, options).then(function(res) {
                if (res.status === 401 || res.status === 403) {
                    localStorage.removeItem('picenter_token');
                    localStorage.removeItem('picenter_user');
                    window.location.href = '/login.html';
                    return;
                }
                return res.json();
            });
        }
    };

    /**
     * 초기화
     */
    function init() {
        // 헤더 삽입
        var placeholder = document.getElementById('gnb-header');
        if (placeholder) {
            placeholder.innerHTML = generateHeaderHTML();
        } else {
            // placeholder가 없으면 기존 .header를 찾아서 교체
            var existingHeader = document.querySelector('.header');
            if (existingHeader) {
                existingHeader.outerHTML = generateHeaderHTML();
            }
        }

        // 서브메뉴 이벤트
        bindSubMenuEvents();

        // 인증
        setupAuth();
    }

    // DOM 준비 후 실행
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }

})();
