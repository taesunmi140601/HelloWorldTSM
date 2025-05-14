<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 26.     	SJH            최초 생성
 *
-->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<security:authentication property="principal.realUser" var="realUser"/>
<html>
<head>
    <meta charset="UTF-8">
    <title>설문 결과</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="https://unpkg.com/lottie-web@latest/build/player/lottie.min.js"></script>
    <script src="https://unpkg.com/@lottiefiles/lottie-player@latest/dist/lottie-player.js"></script>
    <link href="${pageContext.request.contextPath }/resources/groupware/css/survey/surveyResult.css" rel="stylesheet">
</head>
<body>
<div class="surveyContainer">
<div class="page-header">
    <!-- 상단 메뉴 -->
    <h3 class="fw-bold mb-3">설문/투표</h3>
    <ul class="breadcrumbs mb-3">
        <li class="nav-home">
            <a href="<c:url value='/${realUser.companyNo }/groupware'/>">
                <i class="icon-home"></i>
            </a>
        </li>
        <li class="separator">
            <i class="icon-arrow-right"></i>
        </li>
        <li class="nav-item">
            <a href="<c:url value='/${realUser.companyNo }/survey' />">설문/투표 결과</a>
        </li>
    </ul>
</div>
	<!-- ✅ 목록으로 돌아가기 버튼 추가 -->
    <div class="text-right mb-3">
        <a href="${pageContext.request.contextPath}/${companyNo}/surveyApi/list">
            <button class="btn btn-primary">목록으로</button>
        </a>
    </div>
    
<h2 id="surveyTitle">설문 제목</h2>
<p id="surveyDescription">설문 설명</p>

<!-- <select id="chartTypeSelect"> -->
<!--     <option value="pie">원형 차트</option> -->
<!--     <option value="doughnut">도넛 차트</option> -->
<!--     <option value="bar">세로 막대</option> -->
<!--     <option value="horizontalBar">가로 막대</option> -->
<!-- </select> -->
<div class="lottie-wrapper">
  <div class="lottie-box">
    <lottie-player data-type="pie"
      class="lottie-clickable"
      src="${pageContext.request.contextPath}/resources/groupware/css/survey/Animation - 1744683118726.json"
      background="transparent"
      speed="1"
      style="width: 300px; height: 300px;"
      loop autoplay>
    </lottie-player>
    <div class="lottie-text">원형차트 결과보기</div>
  </div>

  <div class="lottie-box">
    <lottie-player data-type="doughnut"
      class="lottie-clickable"
      src="${pageContext.request.contextPath}/resources/groupware/css/survey/Animation - 1744683311649.json"
      background="transparent"
      speed="1"
      style="width: 300px; height: 300px;"
      loop autoplay>
    </lottie-player>
    <div class="lottie-text">도넛차트 결과보기</div>
  </div>

  <div class="lottie-box">
    <lottie-player data-type="bar"
      class="lottie-clickable"
      src="${pageContext.request.contextPath}/resources/groupware/css/survey/Animation - 1744683389139.json"
      background="transparent"
      speed="1"
      style="width: 300px; height: 300px;"
      loop autoplay>
    </lottie-player>
    <div class="lottie-text">막대차트 결과보기</div>
  </div>

  <div class="lottie-box">
    <lottie-player data-type="horizontalBar"
      class="lottie-clickable"
      src="${pageContext.request.contextPath}/resources/groupware/css/survey/Animation - 1744683494971.json"
      background="transparent"
      speed="1"
      style="width: 300px; height: 300px;"
      loop autoplay>
    </lottie-player>
    <div class="lottie-text">가로막대 결과보기</div>
  </div>
</div>

<div id="questionList"></div>
<div id="chartContainer"></div>
</div>
<!-- ✅ 먼저 변수 정의 -->
<script>
    window.contextPath = "${pageContext.request.contextPath}";
    window.companyNo = "${companyNo}";
    window.surveyId = "${surveyId}";
</script>

<!-- ✅ 그 후에 JS 파일 로딩 -->
<script src="${pageContext.request.contextPath}/resources/groupware/js/survey/surveyResult.js"></script>

</body>
</html>
