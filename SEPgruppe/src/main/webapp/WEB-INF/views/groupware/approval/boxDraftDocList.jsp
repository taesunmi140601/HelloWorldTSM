<!-- 
 * 기안문서함
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 24.     	JYS            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"  %>

<h4>기안문서함</h4>

<button class="btn btn-outline-danger" ><i class="fa fa-times-circle"></i>상신취소</button>
<table class="table boxTable">
	
	<thead class="table-light">
		<tr>
			<th><input type="checkbox"/></th>
			<th>결재양식</th>
			<th>긴급</th>
			<th>제목</th>
			<th>첨부</th>
			<th>결재상태</th>
			<th>기안일</th>
		</tr>
	</thead>
	<tbody class="table-group-divider">
		<c:choose>
			<c:when test="${empty docs}">
				<tr>
					<td colspan="7" style="text-align: center;">결재 진행 중인 문서가 없습니다.</td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:forEach var="doc" items="${docs }">
					<tr data-doc-no="${doc.aprvlDocNo }">
						<td>
						  <input type="checkbox" />
						  <span style="display: none;">선택</span>
						</td>
		                <td>${doc.docFrmName}</td>
		                <td>${not empty doc.fileGroupNo ? '🚨' : ''}</td>
		                <td>${doc.aprvlDocTitle}</td>
		                <td>${not empty doc.fileGroupNo ? '📎' : ''}</td>
		                <td>
						  <span class="badge badge-lg " style="background-color: ${doc.statusColor}; color: white;">
						    ${doc.statusName}
						  </span>
						</td>
		                <td>${fn:substring(doc.submitDate, 0, 10)}</td>
					</tr>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>

<!-- 결재요청 결과 / 임시저장 성공 여부 알람 토스트 -->
<div id="apprResToast" class="toast align-items-center border-0" role="alert" aria-live="assertive" aria-atomic="true">
  <div class="d-flex">
    <div class="toast-body">	
    	<!-- 알람 내용 -->
    </div>
    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
  </div>
</div>

<%-- <script src="${pageContext.request.contextPath }/resources/groupware/js/approval/apprForm_ft.js"></script>  --%>
<script src="${pageContext.request.contextPath }/resources/groupware/js/approval/apprHome.js"></script>