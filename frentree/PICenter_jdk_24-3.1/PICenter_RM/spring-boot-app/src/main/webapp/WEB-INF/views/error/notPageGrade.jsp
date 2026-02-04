<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta charset="utf-8">
	<title>에러 페이지</title>

	<link rel="icon" href="data:,">

	<link href="${pageContext.request.contextPath}/resources/assets/css/ui.fancytree-sktPIC.css" rel="stylesheet" type="text/css" /> 
	<link href="${pageContext.request.contextPath}/resources/assets/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
	
	<!-- Publish JS -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/jquery-3.3.1.js"></script>
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/js/jquery-ui.js" type="text/javascript"></script>
	
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/echart/echarts-all.js"></script>
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/echart/echarts.min.js"></script>
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/tree/jquery.fancytree.ui-deps.js"></script>
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/tree/jquery.fancytree.js"></script>
	
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/jqgrid/jquery.jqGrid.min.js"></script>
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/jqgrid/i18n/grid.locale-kr.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/wickedpicker.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/select2.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/jstree.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/exceljs.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/fileserver.js"></script>
	
	<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/resources/assets/css/select2.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/resources/assets/css/wickedpicker.min.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/resources/assets/css/jquery-ui-PIC.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/resources/assets/css/ui.jqgrid-PIC.css" />
	
	<!-- Application Common Functions  -->
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/resources/assets/js/common.js"></script>
	
	<!-- Publish CSS -->
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/assets/css/reset-PIC.css" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/assets/css/design-PIC.css" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/assets/css/style.min.css" />
	
</head>

<body class="">
  <div class="" style="top: 25%; position: relative;">
    <div class="error_wrap">
      <div class="container" style="text-align: center; padding: 0px; background-color: white;">

          <div class="area_img"><img src="${pageContext.request.contextPath}/resources/assets/images/img_error.png" alt=""></div><BR/>
          <h1 style="padding: 20px;">페이지 접속 불가</h1>
          <p>올바르지 않는 경로로 접근하여 접속이 불가합니다.
          </p>
          <div class="btn_area magin_t1" style="text-align: center;">
            <button class = "button" onclick="main();" style="margin-right: 5px;">메인으로 가기 </button>
            <button class = "button" onclick="javascript:history.back(-1)">이전 페이지로 가기</button>
          </div>

      </div>
    </div>
  </div>
</body>

<script type="text/javascript">
function main() {
	document.location.href = "<%=request.getContextPath()%>/";
}
</script>

</html>
