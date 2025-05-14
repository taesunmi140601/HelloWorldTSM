<!-- 
 * 결재문서함
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
<h4>결재 문서함</h4>
<button class="btn btn-outline-danger" >삭제</button>
<table class="table boxTable">
	<thead>
		<tr>
			<th><input type="checkbox"></th>
			<th>기안일</th>
			<th>완료일</th>
			<th>결재양식</th>
			<th>긴급</th>
			<th>제목</th>
			<th>첨부</th>
			<th>기안자</th>
			<th>문서번호</th>
			<th>결재상태</th>
		</tr>
	</thead>
	<tbody class="table-group-divider">
		<c:choose>
			<c:when test="${empty docs}">
				<tr>
					<td colspan="10" style="text-align: center;" >결재 문서가 없습니다.</td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:forEach var="doc" items="${docs }">
					<tr data-doc-no="${doc.aprvlDocNo }">
						<th><input type="checkbox"></th>
						<td>${fn:substring(doc.submitDate, 0, 10)}</td>
						<td>${doc.aprvdDate }</td>
		                <td>${doc.docFrmName}</td>
		                <td>${not empty doc.fileGroupNo ? '🚨' : ''}</td>
		                <td>${doc.aprvlDocTitle}</td>
		                <td>${not empty doc.fileGroupNo ? '📎' : ''}</td>
		                <td>${doc.drafterName}</td>
		                <td>${doc.aprvdDocNo }</td>
		                <td>
						  <span class="badge" style="background-color: ${doc.statusColor}; color: white;">
						    ${doc.statusName}
						  </span>
						</td>
					<tr>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>
<script src="${pageContext.request.contextPath }/resources/groupware/js/approval/apprHome.js"></script>