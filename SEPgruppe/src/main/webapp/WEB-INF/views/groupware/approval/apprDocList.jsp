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
<!DOCTYPE html>


<table  id="datatablesSimple" class="table">
	<thead>
		<tr>
			<th><input type="checkbox"></th>
			<th>기안 번호</th>
			<th>제목</th>
			<th>기안자</th>
			<th>양식명</th>
			<th>기안 날짜</th>
		</tr>
	</thead>
	<tbody class="table-group-divider">
		<c:forEach var="docForm" items="${docForms }">
			<tr id="${docForm.docFrmNo }" class="docRow">
				<td><input type="checkbox"></td>
				<td>${docForm.docFolderVo.docFolderName }</td>
				<td>${docForm.docFrmName }</td>
				<td>${docForm.docFrmContent }</td>
			</tr>
		</c:forEach>
	</tbody>
</table>