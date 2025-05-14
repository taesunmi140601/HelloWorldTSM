<!-- 
 * 참조/열람 문서함
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
<!DOCTYPE html>
<h4>참조/열람 문서함</h4>
<button class="btn btn-outline-danger" >삭제</button>
전체/참조/열람
<table class="table boxTable" id="datatablesSimple">
	<thead>
		<tr>
			<th><input type="checkbox"></th>
			<th>기안일</th>
			<th>완료일</th>
			<th>결재양식</th>
			<th>구분</th>
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
			<c:when test="${empty awaitDoc}">
				<tr>
					<td colspan="11" style="text-align: center;" >결재 문서가 없습니다.</td>
				</tr>
			</c:when>
			<c:otherwise>
				<%-- <c:forEach var="" items="">
					<tr>
						<td><th><input type="checkbox"></th></td>
					</tr>
				</c:forEach> --%>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>