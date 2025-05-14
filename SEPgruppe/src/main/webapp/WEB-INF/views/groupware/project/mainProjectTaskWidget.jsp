<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 4. 15.     	JSW            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<div class="project-task-widget">
  <h5 class="widget-title mb-3">내 프로젝트 일감</h5>
  <table class="table table-sm table-hover">
    <thead class="table-light">
      <tr>
        <th>프로젝트 제목</th>
        <th>일감 제목</th>
        <th>우선순위</th>
        <th>담당자</th>
        <th>마감일</th>
      </tr>
    </thead>
    <tbody>
       <c:if test="${not empty taskList}">
	    <c:forEach var="task" items="${taskList}">
	      <tr style="cursor: pointer;" onclick="location.href='${pageContext.request.contextPath}/${companyNo}/task/${task.taskNo}'">
	        <td>${task.projectTitle}</td>
	        <td>${task.taskTitle}</td>
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
	        <td>
	        	<fmt:formatDate value="${task.taskEndDate}" pattern="yyyy-MM-dd" />
	        </td>
	      </tr>
	    </c:forEach>
	  </c:if>
	
	  <c:if test="${empty taskList}">
	    <tr>
	      <td colspan="5" class="text-center text-muted">담당 일감이 없습니다.</td>
	    </tr>
	  </c:if>
    </tbody>
  </table>
</div>

<script>
	const taskList = "${taskList}";
	console.log(taskList);
</script>