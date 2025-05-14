<!-- 
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
<h4>결재 대기 문서</h4>
<table class="table">
	<thead>
		<tr>
			<th>기안일</th>
			<th>결재양식</th>
			<th>긴급</th>
			<th>제목</th>
			<th>첨부</th>
			<th>기안자</th>
			<th>상태</th>
		</tr>
	</thead>
	<tbody class="table-group-divider">
		<c:choose>
			<c:when test="${empty waitingDocs}">
				<tr>
					<td colspan="7" style="text-align: center;">결재할 문서가 없습니다.</td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:forEach var="doc" items="${waitingDocs}">
		            <tr class="waitingDocTr" data-doc-no="${doc.aprvlDocNo}">
		                <td>${fn:substring(doc.submitDate, 0, 10)}</td>
		                <td>${doc.docFrmName}</td>
		                <td>${doc.isEmergency}</td>
		                <td>${doc.aprvlDocTitle}</td>
		                <td></td>
		                <td>${doc.drafterName}</td>
		                <td>
						  <span class="badge" style="background-color: ${doc.statusColor}; color: white;">
						    ${doc.statusName}
						  </span>
						</td>
		            </tr>
		        </c:forEach>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>