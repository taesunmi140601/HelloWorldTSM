<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 26.     	KKM            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/frappe-gantt@latest/dist/frappe-gantt.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/groupware/css/project/task.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/groupware/css/project/ganttchartTaskList.css" />
    <title>간트 차트</title>
<div class="ganttContainer">
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
    <div class="biggestContainer mb-3">
        <ul class="nav nav-tabs nav-line nav-color-secondary" id="line-tab" role="tablist">
            <li class="nav-item">
                <a class="nav-link" id="line-project-tab" href="${pageContext.request.contextPath}/${companyNo}/project" role="tab" aria-controls="pills-home" aria-selected="true">목록</a>
            </li>
            <li class="nav-item">
		        <a class="nav-link" id="line-outline-tab" href="${pageContext.request.contextPath}/${companyNo}/project/${projectNo}" role="tab" aria-controls="pills-outline" aria-selected="true">개요</a>
		    </li>
            <li class="nav-item">
                <a class="nav-link" id="line-work-tab" href="${pageContext.request.contextPath}/${companyNo}/task?projectNo=${projectNo}" role="tab" aria-controls="pills-work" aria-selected="true">일감</a>
            </li>
            <li class="nav-item">
                <a class="nav-link active" id="line-gantt-tab" href="${pageContext.request.contextPath}/${companyNo}/ganttcharttask?projectNo=${projectNo}" role="tab" aria-controls="pills-gantt" aria-selected="true">간트차트</a>
            </li>
        </ul>
    </div>

    <div class="content">
        <div id="gantt" style="width: 100%; height: 600px;"></div>
    </div>
</div>    

    <script src="https://cdn.jsdelivr.net/npm/frappe-gantt@0.6.1/dist/frappe-gantt.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/groupware/js/project/ganttchartTaskList.js"></script>
    <script>
        	document.addEventListener('DOMContentLoaded', function () {
        	    let gantTaskList = [
        	      <c:forEach var="task" items="${gantTaskList}" varStatus="status">
        	      {
        	        id: "${task.taskNo}",
        	        name: "${fn:escapeXml(task.taskTitle)}",
        	        start_date: "${fn:substring(task.taskStartDate, 0, 10)}",
        	        end_date: "${fn:substring(task.taskEndDate, 0, 10)}",
        	        progress: ${task.progress != null ? task.progress : 0}
        	      }<c:if test="${!status.last}">,</c:if>
        	      </c:forEach>
        	    ];

        	    console.log("📦 gantTaskList 전달됨:", gantTaskList);
        	    initializeGantt(gantTaskList);
        	  });
    </script>
		    