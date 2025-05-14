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
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<security:authentication property="principal.realUser" var="realUser"/>

    <title>프로젝트 생성</title>
    <style>
    .formContainer {
    	padding: 40px;
    }
    </style>
<div class="formContainer">
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
    <div class="container mt-5">
        <h2>프로젝트 생성</h2> <button type="button" class="btn btn-outline-secondary btn-sm" id="mecro">자동완성</button>
        <form action="${pageContext.request.contextPath}/${companyNo}/project/new" method="post">
            <div class="form-group">
                <label for="creator">생성자</label>
                <input type="text" class="form-control" id="creator" name="creator" value="${empId}" readonly>
            </div>
            <div class="form-group">
                <label for="projectTitle">프로젝트 제목</label>
                <input type="text" class="form-control" id="projectTitle" name="projectTitle" required>
            </div>
            <div class="form-group">
                <label for="projectStartTime">시작일</label>
                <input type="date" class="form-control" id="projectStartTime" name="projectStartTime" value="${currentDate}" required>
            </div>
            <div class="form-group">
                <label for="projectEndTime">종료일</label>
                <input type="date" class="form-control" id="projectEndTime" name="projectEndTime" required>
            </div>
            <div class="form-group">
                <label for="projectDesc">프로젝트 설명</label>
                <textarea class="form-control" id="projectDesc" name="projectDesc" rows="5"></textarea>
            </div>
            <div class="form-group">
                <label for="projectStatus">프로젝트 상태</label>
                <select class="form-control" id="projectStatus" name="projectStatus" required>
                    <option value="">선택하세요</option>
                    <option value="진행중">진행중</option>
                    <option value="완료">완료</option>
                    <option value="중단">중단</option>
                </select>
            </div>
            <div class="form-group">
                <label for="participants">프로젝트 참여자</label>
                <select multiple class="form-control" id="participants" name="participants" required>
				    <c:forEach var="employee" items="${employees}">
				        <option value="${employee.empId}">${employee.empNm} (${employee.empId})</option>
				    </c:forEach>
				</select>
            </div>
            <button type="submit" class="btn btn-outline-success">프로젝트 생성</button>
            <a href="${pageContext.request.contextPath}/${companyNo}/project" class="btn btn-outline-danger">취소</a>
        </form>
    </div>
</div>
    <script>
	    $(document).ready(function() {
	        $('#participants').select2({
	            placeholder: "참여자를 검색하세요",
	            width: '100%',
	            allowClear: true
	        });
	    });
    </script>
<script src="${pageContext.request.contextPath }/resources/groupware/js/project/projectForm.js"></script>