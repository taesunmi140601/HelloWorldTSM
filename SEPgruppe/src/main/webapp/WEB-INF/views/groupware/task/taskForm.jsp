<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 27.     	KKM            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
    <c:choose>
	    <c:when test="${mode == 'edit'}">
	    	<title>일감 수정</title>
	    </c:when>
	    <c:otherwise>
	    	<title>일감 등록</title>
	    </c:otherwise>
	</c:choose>
    <div class="container">
    <c:choose>
        <c:when test="${mode == 'edit'}">
        	<h1>일감 수정</h1>
        </c:when>
        <c:otherwise>
        	<h1>일감 등록</h1> <button type="button" class="btn btn-outline-secondary btn-sm" id="mecro">자동완성</button>
        </c:otherwise>
    </c:choose>	
        <c:choose>
		  <c:when test="${mode == 'edit'}">
		    <form action="${pageContext.request.contextPath}/${companyNo}/task/${task.taskNo}/edit" method="post" enctype="multipart/form-data">
		  </c:when>
		  <c:otherwise>
		    <form action="${pageContext.request.contextPath}/${companyNo}/task/new" method="post" enctype="multipart/form-data">
		  </c:otherwise>
		</c:choose>
            <input type="hidden" id="companyNo" value="${companyNo}" />
            <input type="hidden" id="contextPath" value="${pageContext.request.contextPath}" />
			<c:if test="${mode == 'edit'}">
			    <input type="hidden" name="taskNo" value="${task.taskNo}" />
			</c:if>
            <c:if test="${not empty selectedProjectNo}">
			    <div class="form-group">
			        <label>프로젝트</label>
			        <input type="text" class="form-control" value="${selectedProjectTitle}" readonly />
			        <input type="hidden" name="projectNo" value="${selectedProjectNo}" />
			    </div>
			</c:if>

            <div class="form-group">
                <label for="taskTitle">업무 제목</label>
                <input type="text" id="taskTitle" name="taskTitle" class="form-control" required 
                 value="${mode == 'edit' ? task.taskTitle : ''}" />
            </div>

            <div class="form-group">
                <label for="taskContent">업무 내용</label>
                <textarea id="taskContent" name="taskContent" class="form-control" rows="5" required>
                 ${mode == 'edit' ? task.taskContent : ''}</textarea>
            </div>

            <div class="form-group">
                <label for="taskStartDate">업무 시작일자</label>
                <input type="date" id="taskStartDate" name="taskStartDate" class="form-control" required
                 value="<fmt:formatDate value='${task.taskStartDate}' pattern='yyyy-MM-dd' />" />
            </div>
            <div class="form-group">
                <label for="taskEndDate">업무 마감일자</label>
                <input type="date" id="taskEndDate" name="taskEndDate" class="form-control" required 
                 value="<fmt:formatDate value='${task.taskEndDate}' pattern='yyyy-MM-dd' />" />
            </div>

            <div class="form-group">
                <label for="priority">우선순위</label>
                <select id="priority" name="priority" class="form-control">
                    <option value="낮음" <c:if test="${mode == 'edit' && task.priority == '낮음'}">selected</c:if>>낮음</option>
                    <option value="보통" <c:if test="${mode == 'edit' && task.priority == '보통'}">selected</c:if>>보통</option>
                    <option value="높음" <c:if test="${mode == 'edit' && task.priority == '높음'}">selected</c:if>>높음</option>
                    <option value="긴급" <c:if test="${mode == 'edit' && task.priority == '긴급'}">selected</c:if>>긴급</option>
                    <option value="즉시" <c:if test="${mode == 'edit' && task.priority == '즉시'}">selected</c:if>>즉시</option>
                </select>
            </div>
            
            <div class="form-group">
			    <label for="progress">진척도 (%)</label>
			    <select id="progress" name="progress" class="form-control" required>
			        <c:forEach begin="0" end="100" step="10" var="p">
			            <option value="${p}" <c:if test="${mode == 'edit' && task.progress == p}">selected</c:if>>${p}%</option>
			        </c:forEach>
			    </select>
			</div>

            <div class="form-group">
                <label for="empId">담당자 선택</label>
                <select id="empId" name="empId" class="form-control" required>
                    <option value="">선택하세요</option>
                    <!-- 1. PL 먼저 출력 -->
			        <c:if test="${not empty plEmpId}">
			            <option value="${plEmpId}"
			                <c:if test="${mode == 'edit' && task.empId == plEmpId}">selected</c:if>>
			                ${plEmpId} (${plEmpNm})
			            </option>
			        </c:if>
			
			        <!-- 2. 참여자 목록 출력 (단, PL은 제외) -->
			        <c:forEach var="participant" items="${participants}">
			            <c:if test="${participant.empId != plEmpId}">
			                <option value="${participant.empId}"
			                    <c:if test="${mode == 'edit' && task.empId == participant.empId}">selected</c:if>>
			                    ${participant.empId} (${participant.empNm})
			                </option>
			            </c:if>
			        </c:forEach>
                </select>
            </div>
			
<!-- 			<div class="form-group"> -->
<!-- 			    <label for="fileUpload">첨부 파일</label> -->
<!-- 			    <input type="file" id="fileUpload" name="uploadFiles" class="form-control" multiple> -->
<!-- 			</div> -->

            <button type="submit" class="btn btn-outline-success">
		        <c:choose>
		            <c:when test="${mode == 'edit'}">수정</c:when>
		            <c:otherwise>등록</c:otherwise>
		        </c:choose>
		    </button>
		    <c:choose>
		        <c:when test="${mode == 'edit'}">
            		<a href="${pageContext.request.contextPath}/${companyNo}/task/${task.taskNo}" class="btn btn-outline-danger">취소</a>
		        </c:when>
		        <c:otherwise>
		            <a href="${pageContext.request.contextPath}/${companyNo}/task?projectNo=${selectedProjectNo}" class="btn btn-outline-danger">취소</a>
		        </c:otherwise>
		    </c:choose>	
        </form>
    </div>
    <script>
    	const contextPath = "${pageContext.request.contextPath}";
    	const companyNo = "${companyNo}";
    </script>
<script src="${pageContext.request.contextPath }/resources/groupware/js/task/taskForm.js"></script>