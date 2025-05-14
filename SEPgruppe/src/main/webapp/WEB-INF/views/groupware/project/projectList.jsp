<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 14.     	JYS            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<security:authentication property="principal.realUser" var="realUser"/>
<link href="${pageContext.request.contextPath }/resources/groupware/css/project/projectList.css" rel="stylesheet">
    <script>
        function filterProjects(status) {
            var table, tr, td, i;
            table = document.getElementById("projectTable");
            tr = table.getElementsByTagName("tr");

            for (i = 1; i < tr.length; i++) { // skip header row
                td = tr[i].getElementsByTagName("td")[6]; // 상태 컬럼
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
<!--     <div class="card-body mb-3"> -->
<!--         <ul class="nav nav-tabs nav-line nav-color-secondary" id="line-tab" role="tablist"> -->
<!--             <li class="nav-item"> -->
<%--                 <a class="nav-link" id="line-project-tab" href="${pageContext.request.contextPath}/${companyNo}/project" role="tab" aria-controls="pills-project" aria-selected="true">목록</a> --%>
<!--             </li> -->
<!--         </ul> -->
<!--     </div> -->
<div class="projectContainer">
	<div class="page-header">
	    <h3 class="fw-bold mb-3">공유 프로젝트</h3>
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
	            <a href="<c:url value='/${companyNo}/project'/>">Project</a>
	        </li>
	    </ul>
	</div>


	<div class="card-body">
		<ul class="nav nav-tabs nav-line nav-color-secondary" id="line-tab" role="tablist">
			<li class="nav-item">
				<a class="nav-link active" id="line-home-tab" data-bs-toggle="pill" href="#line-home" role="tab" aria-controls="pills-home" aria-selected="true">내가 생성한 프로젝트</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" id="line-profile-tab" data-bs-toggle="pill" href="#line-profile" role="tab" aria-controls="pills-profile" aria-selected="false">참여중인 프로젝트</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" id="line-contact-tab" data-bs-toggle="pill" href="#line-contact" role="tab" aria-controls="pills-contact" aria-selected="false">내가 참여한 프로젝트</a>
			</li>
		</ul>
	</div>


    <div class="buttonspart mb-4">
        <a href="${pageContext.request.contextPath}/${companyNo}/project/new" class="btn btn-secondary btn-sm" id="addPj">
        <span class="projt"><i class="fa fa-plus"></i> 프로젝트 생성</span></a>
        
        <form id="deleteForm" method="post" action="${pageContext.request.contextPath}/${companyNo}/project/deleteSelected">
	        <button type="submit" class="btn btn-secondary btn-sm" id="deletePj">
	        <span class="projt"><i class="fa fa-trash"></i> 선택 삭제</span></button>
	    </form>
        <div class="input-icon">
            <input type="text" class="form-control" placeholder="프로젝트명 검색..." />
            <span class="input-icon-addon">
                <i class="fa fa-search"></i>
            </span>
        </div>
    </div>

    <div class="tab-content" id="pills-tabContent">
        <div class="tab-pane fade show active" id="line-home" role="tabpanel" aria-labelledby="line-home-tab">
<!--         <h2>내가 생성한 프로젝트</h2> -->
		<table class="table table-striped mb-4">
		    <thead>
		        <tr>
		        	<th><input type="checkbox" id="checkAll"></th>
		            <th>번호</th>
		            <th>제목</th>
		            <th>시작일</th>
		            <th>종료일</th>
		            <th>상태</th>
		        </tr>
		    </thead>
		    <tbody>
		        <c:forEach var="project" items="${projectsOwned}">
		            <tr>
		            	<td><input type="checkbox" class="projectChk" name="projectNos" value="${project.projectNo}"></td>
		                <td>${project.projectNo}</td>
		                <td>
		                    <a href="${pageContext.request.contextPath}/${companyNo}/project/${project.projectNo}">
		                        ${project.projectTitle}
		                    </a>
		                </td>
		                <td><fmt:formatDate value="${project.projectStartTime}" pattern="yyyy-MM-dd" /></td>
		                <td><fmt:formatDate value="${project.projectEndTime}" pattern="yyyy-MM-dd" /></td>
		                <td>
		                  <c:choose>
						    <c:when test="${project.projectStatus eq '진행중'}">
						      <span class="badge badge-primary">진행중</span>
						    </c:when>
						    <c:when test="${project.projectStatus eq '종료'}">
						      <span class="badge badge-black">종료</span>
						    </c:when>
						  </c:choose>
						</td> 
		            </tr>
		        </c:forEach>
		    </tbody>
		</table>
		</div>	
		
		<div class="tab-pane fade" id="line-profile" role="tabpanel" aria-labelledby="line-profile-tab">
<!-- 		<h2>참여 중인 프로젝트</h2> -->
		<table class="table table-striped mb-4">
		    <thead>
		        <tr>
		            <th>번호</th>
		            <th>제목</th>
		            <th>시작일</th>
		            <th>종료일</th>
		            <th>상태</th>
		        </tr>
		    </thead>
		    <tbody>
		        <c:forEach var="project" items="${projectsJoined}">
		            <tr>
		                <td>${project.projectNo}</td>
		                <td>
		                    <a href="${pageContext.request.contextPath}/${companyNo}/project/${project.projectNo}">
		                        ${project.projectTitle}
		                    </a>
		                </td>
		                <td><fmt:formatDate value="${project.projectStartTime}" pattern="yyyy-MM-dd"/></td>
		                <td><fmt:formatDate value="${project.projectEndTime}" pattern="yyyy-MM-dd"/></td>
		                <td>
		                  <c:choose>
						    <c:when test="${project.projectStatus eq '진행중'}">
						      <span class="badge badge-primary">진행중</span>
						    </c:when>
						    <c:when test="${project.projectStatus eq '종료'}">
						      <span class="badge badge-black">종료</span>
						    </c:when>
						  </c:choose>
						</td> 
		            </tr>
		        </c:forEach>
		    </tbody>
		</table>
		</div>
		
		<div class="tab-pane fade" id="line-contact" role="tabpanel" aria-labelledby="line-contact-tab">
<!-- 		<h2>내가 참여한 프로젝트</h2> -->
		<table class="table table-striped mb-4">
		    <thead>
		        <tr>
		            <th>번호</th>
		            <th>제목</th>
		            <th>시작일</th>
		            <th>종료일</th>
		            <th>상태</th>
		        </tr>
		    </thead>
		    <tbody>
		        <c:forEach var="project" items="${projectsCompleted}">
		            <tr>
		                <td>${project.projectNo}</td>
		                <td>
		                    <a href="${pageContext.request.contextPath}/${companyNo}/project/${project.projectNo}">
		                        ${project.projectTitle}
		                    </a>
		                </td>
		                <td><fmt:formatDate value="${project.projectStartTime}" pattern="yyyy-MM-dd"/></td>
		                <td><fmt:formatDate value="${project.projectEndTime}" pattern="yyyy-MM-dd"/></td>
		                <td>
		                  <c:choose>
						    <c:when test="${project.projectStatus eq '진행중'}">
						      <span class="badge badge-primary">진행중</span>
						    </c:when>
						    <c:when test="${project.projectStatus eq '종료'}">
						      <span class="badge badge-black">종료</span>
						    </c:when>
						  </c:choose>
						</td> 
		            </tr>
		        </c:forEach>
		    </tbody>
		</table>
		</div>
	</div>	
        <c:if test="${not empty deleteSuccess}">
		    <div id="delete-msg" data-success="${deleteSuccess}"></div>
		</c:if>
		
		<c:if test="${not empty deleteFail}">
		    <div id="delete-msg" data-fail="${deleteFail}"></div>
		</c:if>
</div>		
	
	
    <script src="${pageContext.request.contextPath }/resources/groupware/js/project/projectList.js"></script>