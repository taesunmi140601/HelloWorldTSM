<!-- 
 * 전자결재 환경설정 
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
<h4>전자결재 환경설정</h4>

<h6>부재/위임 설정</h6>

<button class="btn btn-outline-primary" onclick="location.href=`${pageContext.request.contextPath}/${companyNo }/approval/admin/insertAbsence`">부재 추가</button>
<button class="btn btn-outline-danger apprbtn" >삭제</button>

<table class="table">
	<thead>
		<tr>
			<th> <input type="checkbox"> </th> 
			<th>부재 시작일</th>
			<th>부재 종료일</th>
			<th>대결자</th>
			<th>부재 사유</th>
			<th>사용 여부</th>
		</tr>
	</thead>
	<tbody class="table-group-divider">
		<c:choose>
			<c:when test="${not empty absences}">
				<c:forEach var="absences" items="${absences }">
					<tr id="${docForm.docFrmNo }" class="docRow">
						<td>${docForm.docFolderVo.docFolderName }</td>
						<td>${docForm.docFrmName }</td>
						<td>${docForm.docFrmContent }</td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<td colspan="6" style="text-align: center;">저장된 부재 목록이 없습니다.<td>
				<tr>
			</c:otherwise>
		</c:choose> 
	</tbody>
</table>