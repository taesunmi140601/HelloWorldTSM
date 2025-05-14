<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 28.     	KKM            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<security:authentication property="principal.realUser" var="realUser"/>
<link href="${pageContext.request.contextPath }/resources/groupware/css/project/projectDetail.css" rel="stylesheet">
    <script src="https://unpkg.com/lottie-web@latest/build/player/lottie.min.js"></script>
    <script src="https://unpkg.com/@lottiefiles/lottie-player@latest/dist/lottie-player.js"></script>
    <meta charset="UTF-8">
<div class="projectDetailContainer">    
    <title>${projectDetails.projectTitle} - 프로젝트 상세</title>
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
	 <div class="card-body">
        <ul class="nav nav-tabs nav-line nav-color-secondary" id="line-tab" role="tablist">
             <li class="nav-item">
		        <a class="nav-link" id="line-project-tab" href="${pageContext.request.contextPath}/${companyNo}/project" role="tab" aria-controls="pills-work" aria-selected="true">목록</a>
		    </li>
             <li class="nav-item">
		        <a class="nav-link" id="line-outline-tab" href="${pageContext.request.contextPath}/${companyNo}/project/${projectNo}" role="tab" aria-controls="pills-outline" aria-selected="true">개요</a>
		    </li>
             <li class="nav-item">
		        <a class="nav-link" id="line-work-tab" href="${pageContext.request.contextPath}/${companyNo}/task?projectNo=${projectNo}" role="tab" aria-controls="pills-work" aria-selected="true">일감</a>
		    </li>
            <li class="nav-item">
                <a class="nav-link" id="line-gantt-tab" href="${pageContext.request.contextPath}/${companyNo}/ganttcharttask?projectNo=${projectNo}" role="tab" aria-controls="pills-gantt" aria-selected="true">간트차트</a>
            </li>
        </ul>
    </div>
    <div class="container mt-4">
    <div class="row">
        <!-- 왼쪽: 상세 정보 + 업무 목록 -->
        <div class="col-md-8">
            <!-- 프로젝트 상세 정보 -->
            <div id="project" class="mb-4">
                <h3>${projectDetails.projectTitle}</h3>
                상태 :
                <c:if test="${projectDetails.empId == realUser.empId}">
                    <select id="statusSelect" class="form-select-sm"
                            data-project-no="${projectDetails.projectNo}"
                            data-company-no="${companyNo}">
                        <option value="진행중" ${projectDetails.projectStatus == '진행중' ? 'selected' : ''}>진행중</option>
                        <option value="종료" ${projectDetails.projectStatus == '종료' ? 'selected' : ''}>종료</option>
                    </select>
                    <span id="statusUpdateMsg" style="margin-left:10px; color:green; font-weight:bold;"></span>
                </c:if>
                <c:if test="${projectDetails.empId != realUser.empId}">
                    ${projectDetails.projectStatus}
                </c:if>
                <p><strong>설명:</strong> ${projectDetails.projectDesc}</p>
            </div>

            <!-- 업무 목록 -->
            <div>
                <h4>업무 목록</h4>
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>업무 번호</th>
                            <th>업무 제목</th>
                            <th>진행률</th>
                            <th>우선순위</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="task" items="${projectDetails.tasks}">
                            <tr>
                                <td>${task.taskNo}</td>
                                <td>${task.taskTitle}</td>
                                <td>${task.progress}</td>
                                <td>${task.priority}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- 오른쪽: 구성원 박스 -->
        <div class="col-md-4">
            <div class="card p-3 shadow-sm">
                <h4 class="mb-3">구성원</h4>
 				 <div class="mb-2">
		            <strong class="text-primary">[PL] </strong>
		            <strong>${projectDetails.empNm}</strong> (${projectDetails.empId})
		        </div>               
                <ul class="list-group">
                    <c:forEach var="participant" items="${projectDetails.participants}">
                        <c:if test="${participant.empId != projectDetails.empId}">
	                        <li class="list-group-item">
	                            <strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${participant.empNm}</strong> (${participant.empId})
	                        </li>
	                    </c:if>
                    </c:forEach>
                </ul>
            </div>
            <lottie-player
			    id="background-lottie"
			    src="${pageContext.request.contextPath}/resources/groupware/images/Animation - 1744700242648.json"
			    background="transparent"
			    speed="1"
			    loop
			    autoplay>
			</lottie-player>    
        </div>
    </div>
</div>
</div>

    <script>
    	const contextPath = "${pageContext.request.contextPath}";
		const projectNo = "${projectNo}";
    </script>
    
    <script src="${pageContext.request.contextPath }/resources/groupware/js/project/projectDetail.js"></script>