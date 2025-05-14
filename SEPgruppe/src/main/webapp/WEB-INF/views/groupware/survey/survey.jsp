<!--
 * == 개정이력(Modification Information) ==
 *   
 *   수정일       			수정자           수정내용
 *  ============    ============== =======================
 *  2025. 3. 24.     	SJH            최초 생성
-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>설문</title>
    <link href="${pageContext.request.contextPath}/resources/groupware/css/survey/survey.css" rel="stylesheet">
</head>
<body>

<div class="survey-container">

	<!-- ✅ 목록으로 돌아가기 버튼 추가 -->
    <div class="text-right mb-3">
        <a href="${pageContext.request.contextPath}/${companyNo}/surveyApi/list" class="btn btn-outline-secondary btn-sm">
            <h3>← 목록으로</h3>
        </a>
    </div>
    
    <div class="survey-card">
        <h2 id="surveyTitle">설문 제목</h2>
        <p id="surveyDescription">설문 설명</p>
        <div id="questionList"></div>
        <button id="endSurveyBtn">설문 완료하기</button>
    </div>
</div>

<!-- ✅ 변수 정의 (전역 객체 사용으로 중복 방지) -->
<script>
  window.contextPath = "${pageContext.request.contextPath}";
  window.companyNo = "${companyNo}";
  window.surveyId = "${surveyId}";
  window.surveyJson = JSON.parse('${surveyJson}');
</script>

<!-- ✅ JS 파일은 반드시 그 아래에 위치 -->
<script src="${pageContext.request.contextPath}/resources/groupware/js/survey/survey.js"></script>

</body>
</html>
