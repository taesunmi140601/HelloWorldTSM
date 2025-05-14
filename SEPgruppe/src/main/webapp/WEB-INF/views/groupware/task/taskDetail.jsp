<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 25.     	KKM            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<security:authentication property="principal.realUser" var="realUser"/>
	<meta charset="UTF-8">
    <title>업무 상세 정보</title>
    <link href="${pageContext.request.contextPath }/resources/groupware/css/project/taskDetail.css" rel="stylesheet">
<div class="taskdetailContainer">
	<div class="page-header">
	    <h3 class="fw-bold mb-3">공유 프로젝트</h3>
	    <ul class="breadcrumbs mb-3">
	        <li class="nav-home">
	            <a href="<c:url value='/${member.companyNo }/groupware'/>">
	                <i class="icon-home"></i>
	            </a>
	        </li>
	        <li class="separator">
	            <i class="icon-arrow-right"></i>
	        </li>
	        <li class="nav-item">
	            <a href="<c:url value='/${companyNo}/project'/>">Project</a>
	        </li>
	    </ul>
	</div>
    <div class="card-body mb-3">
        <ul class="nav nav-tabs nav-line nav-color-secondary" id="line-tab" role="tablist">
            <li class="nav-item">
                <a class="nav-link" id="line-project-tab" href="${pageContext.request.contextPath}/${companyNo}/project" role="tab" aria-controls="pills-project" aria-selected="true">목록</a>
            </li>
            <li class="nav-item">
		        <a class="nav-link" id="line-outline-tab" href="${pageContext.request.contextPath}/${companyNo}/project/${taskDetail.projectNo}" role="tab" aria-controls="pills-outline" aria-selected="true">개요</a>
		    </li>
            <li class="nav-item">
                <a class="nav-link" id="line-work-tab" href="${pageContext.request.contextPath}/${companyNo}/task?projectNo=${taskDetail.projectNo}" role="tab" aria-controls="pills-work" aria-selected="true">일감</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" id="line-gantt-tab" href="${pageContext.request.contextPath}/${companyNo}/ganttcharttask?projectNo=${taskDetail.projectNo}" role="tab" aria-controls="pills-gantt" aria-selected="true">간트차트</a>
            </li>
        </ul>
    </div>

    <div class="container">
        <h1>업무 상세 정보</h1>
        
        <div class="container mt-4">
		    <div class="card shadow-sm border-0 rounded-3">
		        <div class="card-header text-black">
         			<h4 class="mb-0">${taskDetail.taskTitle}</h4>
		        </div>
		        <div class="card-body">
		            <p class="card-text"><strong>업무 내용:</strong>${taskDetail.taskContent}</p>
					<p><strong>우선순위 : </strong>
					<span class="badge 
					        <c:choose>
					            <c:when test="${taskDetail.priority == '즉시'}">bg-danger text-white</c:when>
					            <c:when test="${taskDetail.priority == '긴급'}">bg-warning text-dark</c:when>
					            <c:when test="${taskDetail.priority == '높음'}">bg-info text-dark</c:when>
					            <c:when test="${taskDetail.priority == '보통'}">bg-light text-dark</c:when>
					            <c:when test="${taskDetail.priority == '낮음'}">bg-success text-white</c:when>
					        </c:choose>
					    ">
					        ${taskDetail.priority}
					    </span></p>
		            <p><strong>진행률:</strong> ${taskDetail.progress}%</p>
		            <div class="progress mb-3" style="height: 20px;">
		                <div class="progress-bar 
		                            <c:choose>
		                                <c:when test="${taskDetail.progress >= 70}">bg-success</c:when>
		                                <c:when test="${taskDetail.progress >= 40}">bg-warning</c:when>
		                                <c:when test="${taskDetail.progress >= 30}">bg-danger</c:when>
		                                <c:otherwise>bg-danger</c:otherwise>
		                            </c:choose>"
		                    role="progressbar"
		                    style="width: ${taskDetail.progress}%;">
		                    ${taskDetail.progress}%
		                </div>
		            </div>
		
		            <p><strong>업무 시작일:</strong> <fmt:formatDate value="${taskDetail.taskStartDate}" pattern="yyyy-MM-dd" /></p>
		            <p><strong>업무 마감일:</strong> <fmt:formatDate value="${taskDetail.taskEndDate}" pattern="yyyy-MM-dd" /></p>
		            <p><strong>담당자:</strong> ${taskDetail.empNm}</p>
		            <p><strong>업무 생성일:</strong> <fmt:formatDate value="${taskDetail.taskCreateDate}" pattern="yyyy-MM-dd" /></p>
		        </div>
		    </div>
		</div>
	</div>	
	        <!-- 삭제 버튼 추가 -->
			<form id="deleteTaskForm" action="${pageContext.request.contextPath}/${companyNo}/task/${taskNo}/delete" method="post">
			    <input type="hidden" name="_method" value="delete"/>
			    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			    <input type="hidden" name="projectNo" value="${taskDetail.projectNo}"/>
        		<c:if test="${realUser.empId == taskDetail.empId || realUser.empId == taskDetail.projectEmpId}">
        		 <a href="${pageContext.request.contextPath}/${companyNo}/task/${taskDetail.taskNo}/edit" class="btn btn-secondary">
			        <i class="fa fa-edit"></i> 수정
			    </a>
			    <button type="button" class="btn btn-danger" onclick="confirmDelete('${taskNo}',
			        '${taskDetail.projectNo}',
			        '${companyNo}',
			        '${_csrf.parameterName}',
			        '${_csrf.token}')">
			        <i class="fa fa-trash"></i> 삭제하기</button>
				</c:if>
			</form>
		<script>
			const contextPath = '${pageContext.request.contextPath}';
			const companyNo = '${companyNo}';
			const taskNo = '${taskNo}';
			const csrfHeaderName = '${_csrf.headerName}';
		  	const csrfToken = '${_csrf.token}';
		</script>
<script src="${pageContext.request.contextPath }/resources/groupware/js/task/taskDetail.js"></script>