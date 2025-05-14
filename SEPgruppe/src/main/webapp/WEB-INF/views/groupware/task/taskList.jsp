<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 25.     	KKM            최초 생성
 *  2025. 4. 03.     	JSW            전체 수정중
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<security:authentication property="principal.realUser" var="realUser"/>
    <link href="${pageContext.request.contextPath}/resources/groupware/css/project/taskList.css" rel="stylesheet">
    <script>
    function filterTasks(status) {
        var table, tr, td, i;
        table = document.getElementById("taskTable");
        tr = table.getElementsByTagName("tr");

        for (i = 1; i < tr.length; i++) { // skip header row
            td = tr[i].getElementsByTagName("td")[3]; // 우선순위 컬럼
            if (td) {
                if (status === "all" || td.textContent.trim() === status) {
                    tr[i].style.display = ""; // visible
                } else {
                    tr[i].style.display = "none"; // hidden
                }
            }
        }
    }
</script>
<div class="taskContainer">
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
                <a class="nav-link" id="line-project-tab" href="${pageContext.request.contextPath}/${companyNo}/project" role="tab" aria-controls="pills-project" aria-selected="true">목록</a>
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

		        <h1>${projectTitle} - 일감</h1>
		    <div class="buttonspart">
		        <a href="${pageContext.request.contextPath}/${companyNo}/task/new?projectNo=${projectNo}" class="btn btn-secondary btn-sm">
		        <span class="task"><i class="fa fa-plus"></i> 일감 등록</span></a>
		        <button type="button" class="btn btn-secondary btn-sm" id="deleteTaskBtn">
		        <span class="task"><i class="fa fa-trash"></i> 일감 삭제</span></button>
		        <div class="input-icon">
		            <input type="text" class="form-control" placeholder="일감명 검색..." />
		            <span class="input-icon-addon"> 
		                <i class="fa fa-search"></i>
		            </span>
		        </div>
		    </div>

      <main class="task-container">
    <table class="table table-striped mt-3" id="taskTable">
        <thead>
            <tr>
                <th scope="col">
	            	<c:if test="${realUser.empId == projectEmpId}">
	               		<input type="checkbox" id="checkAll">
                	</c:if>
                </th>
                <th scope="col">업무번호</th>
                <th scope="col">제목</th>
                <th scope="col">우선순위</th>
                <th scope="col">담당자</th>
                <th scope="col">진척도</th>
                <th scope="col">마감일</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="task" items="${tasks}">
                <tr data-task-no="${task.taskNo}">
                	<td>
                        <c:if test="${task.empId == realUser.empId || task.projectEmpId == realUser.empId}">
                            <input type="checkbox" class="taskChk" value="${task.taskNo}"/>
                        </c:if>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/${companyNo}/task/${task.taskNo}">
                            ${task.taskNo}
                        </a>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/${companyNo}/task/${task.taskNo}">
                            ${task.taskTitle}
                        </a>
                    </td>
                    <td>
					    <span class="badge 
					        <c:choose>
					            <c:when test="${task.priority == '즉시'}">bg-danger text-white</c:when>
					            <c:when test="${task.priority == '긴급'}">bg-warning text-dark</c:when>
					            <c:when test="${task.priority == '높음'}">bg-info text-dark</c:when>
					            <c:when test="${task.priority == '보통'}">bg-light text-dark</c:when>
					            <c:when test="${task.priority == '낮음'}">bg-success text-white</c:when>
					        </c:choose>
					    ">
					        ${task.priority}
					    </span>
					</td>
                    <td>${task.empNm}</td>
                    <td>${task.progress}%</td>
                    <td>
                        <fmt:formatDate value="${task.taskEndDate}" pattern="yyyy-MM-dd" />
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
        </main>
</div>        
        
        <script>
        	const csrfHeaderName = window.csrfHeaderName || '_csrf_header';
        	const csrfToken = window.csrfToken || '_csrf';
        	const contextPath = '${pageContext.request.contextPath}';
        </script>
    <script src="${pageContext.request.contextPath}/resources/groupware/js/task/taskList.js"></script>
