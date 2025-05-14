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
<link href="${pageContext.request.contextPath}/resources/groupware/css/project/task.css" rel="stylesheet">

<script>
    let companyNo = "${companyNo}";
    let contextPath = "${pageContext.request.contextPath}";

    let clist = [
        <c:forEach var="task" items="${taskList}" varStatus="status">
        {
            title: "${task.taskTitle}",
            start: "${fn:substring(task.taskStartDate, 0, 10)}", // YYYY-MM-DD 형식으로 변환
            end: "${fn:substring(task.taskEndDate, 0, 10)}", // YYYY-MM-DD 형식으로 변환
            empId: "${task.empId}",
            projectTitle: "${task.projectTitle}",
            url: "${pageContext.request.contextPath}/${companyNo}/taskDetail/${task.taskNo}"
        }<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];
    
    console.log(clist); // clist 출력 확인
</script>

    <div class="biggestContainer">
        <ul class="nav nav-tabs nav-line nav-color-secondary" id="line-tab" role="tablist">
            <li class="nav-item">
                <a class="nav-link" id="line-project-tab" href="${pageContext.request.contextPath}/${companyNo}/project" role="tab" aria-controls="pills-home" aria-selected="true">프로젝트</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" id="line-work-tab" href="${pageContext.request.contextPath}/${companyNo}/task" role="tab" aria-controls="pills-work" aria-selected="true">일감</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" id="line-gantt-tab" href="${pageContext.request.contextPath}/${companyNo}/ganttcharttask" role="tab" aria-controls="pills-gantt" aria-selected="true">간트차트</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" id="line-calendar-tab" href="${pageContext.request.contextPath}/${companyNo}/calendertask" role="tab" aria-controls="pills-calendar" aria-selected="true">캘린더</a>
            </li>
        </ul>
    </div>
     <div class="content">
        <div id='calendar'></div>
 </div>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="${pageContext.request.contextPath}/resources/groupware/js/jquery-3.7.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/groupware/js/project/calenderTaskList.js"></script>
    <script src='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.15/index.global.min.js'></script>
    <script src="https://cdn.jsdelivr.net/npm/@fullcalendar/interaction@6.1.15/index.global.min.js"></script>



    