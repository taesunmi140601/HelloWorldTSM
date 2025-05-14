<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일       			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 15.      	JYS            최초 생성
 *
-->
<!-- 
 * @author SJH
 * @since 2025. 3. 25.
 */
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>설문 리스트</title>

    <!-- ✅ Bootstrap + DataTables CSS -->
    <link href="${pageContext.request.contextPath}/resources/groupware/css/surveyList.css" rel="stylesheet">
    <link href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css" rel="stylesheet">
</head>
<body class="p-4">

	<!-- 설문 생성 -->
	<div style="text-align: right; margin-bottom: 2rem;">
	  <a href="${pageContext.request.contextPath}/${companyNo}/survey/create"
	     style="
	        display: inline-block;
	        color: #0d6efd;
	        font-size: 1.1rem;
	        font-weight: 600;
	        padding: 0.75rem 2.5rem;
	        border: 1px solid #b6d4fe;
	        border-radius: 12px;
	        min-width: 180px;
	        text-align: center;
	        text-decoration: none;
	        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.06);
	        transition: all 0.2s ease;
	     "
	     onmouseover="this.style.backgroundColor='#d6e9ff'; this.style.color='#084298';"
	     onmouseout="this.style.backgroundColor='white'; this.style.color='#0d6efd';">
	    <h3 style="margin: 0;">+ 설문 생성</h3>
	  </a>
	</div>





    <!-- 응답 할 수 있는 설문 -->
    <div class="card mb-4">
        <div class="card-header fw-bold"><h3>최근 생성된 설문</h3></div>
        <div class="card-body">
            <table id="recentSurveyTable" class="table table-bordered">
			    <thead>
				  <tr>
				    <th>NO</th>
				    <th>제목</th>
				    <th>응답 수</th>
				    <th>생성일</th>
				    <th>생성자</th>
				  </tr>
				</thead>
				<tbody>
				  <c:forEach var="survey" items="${recentSurveyList}" varStatus="vs">
				    <tr>
				      <td>${vs.index + 1}</td>
				      <td>
				        <a href="${pageContext.request.contextPath}/${companyNo}/surveyApi/surveys/${survey.surveyId}/details">
				          ${survey.title}
				        </a>
				      </td>
				      <td>
				        <c:out value="${responseCounts[survey.surveyId]}" />
				      </td>
				      <td>
				        <c:choose>
				          <c:when test="${not empty survey.dateCreated}">
				            ${fn:substring(survey.dateCreated, 0, 10)}
				          </c:when>
				          <c:otherwise>
				            -
				          </c:otherwise>
				        </c:choose>
				      </td>
				      <td>${survey.creator}</td>
				    </tr>
				  </c:forEach>
				</tbody>
			</table>
        </div>
    </div>

    <!-- ✅ 설문 결과 확인 -->
    <div class="card mb-4">
        <div class="card-header fw-bold"><h3>설문 결과 확인</h3></div>
        <div class="card-body">
            <table id="closedSurveyTable" class="table table-bordered">
			    <thead>
			        <tr>
			            <th>NO</th>
			            <th>제목</th>
			            <th>응답 수</th>
			            <th>생성일</th>   
        				<th>생성자</th>   
			        </tr>
			    </thead>
			    <tbody>
				  <c:forEach var="survey" items="${recentSurveyList}" varStatus="vs">
				    <tr>
				      <td>${vs.index + 1}</td>
				      <td>
				        <a href="${pageContext.request.contextPath}/${companyNo}/surveyApi/surveys/${survey.surveyId}/results">
				          ${survey.title}
				        </a>
				      </td>
				      <td>
				        <c:out value="${responseCounts[survey.surveyId]}" />
				      </td>
				      <td>
				        <c:choose>
				          <c:when test="${not empty survey.dateCreated}">
				            ${fn:substring(survey.dateCreated, 0, 10)}
				          </c:when>
				          <c:otherwise>
				            -
				          </c:otherwise>
				        </c:choose>
				      </td>
				      <td>${survey.creator}</td>
				    </tr>
				  </c:forEach>
				</tbody>
			</table>
        </div>
    </div>

    <!-- ✅ JS: jQuery + Bootstrap + DataTables -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>

    <script>
    $(document).ready(function () {
        // ✅ 공통 DataTable 설정
        const defaultDataTableOptions = {
            language: {
                search: "검색:",
                lengthMenu: "_MENU_ 항목 보기",
                info: "총 _TOTAL_개 중 _START_ ~ _END_",
                paginate: {
                    previous: "이전",
                    next: "다음"
                },
                zeroRecords: "일치하는 설문이 없습니다"
            }
        };

        // ✅ 여러 테이블에 반복 적용
        ['#recentSurveyTable', '#closedSurveyTable'].forEach(function (selector) {
            $(selector).DataTable(defaultDataTableOptions);
        });
    });
    </script>
</body>
</html>
