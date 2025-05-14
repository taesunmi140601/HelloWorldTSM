<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 4. 9.     	SJH            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link href="${pageContext.request.contextPath }/resources/groupware/css/addressbook/addressList.css" rel="stylesheet">

<%
	String[] types = {"I", "E"};
%>
<div class="addressContainer">
	<div class="page-header">
	    <h3 class="fw-bold mb-3">주소록</h3>
	    <ul class="breadcrumbs mb-3">
	        <li class="nav-home">
	            <a href="<c:url value='/${member.companyNo }/groupware'/>">
	                <i class="icon-home"></i>
	            </a>
	        </li>
	        <li class="separator">
	            <i class="icon-arrow-right"></i>
	        </li>
	        <li class="nav-item">
	            <a href="<c:url value='/${companyNo}/address'/>">Address</a>
	        </li>
	    </ul>
	</div>

<c:forEach var="type" begin="0" end="1">
	<c:set var="currentType" value="${type eq 0 ? 'I' : 'E'}" />
	<c:set var="tableId" value="${type eq 0 ? 'internal-table' : 'external-table'}" />
	<c:set var="title" value="${type eq 0 ? '📘 내부 주소록' : '📗 외부 주소록'}" />


	<div class="card mb-4">
		<div class="card-header">
			<h4 class="card-title">${title}</h4>
		</div>
		<div class="card-body">
			<div class="table-responsive">
				<table id="${tableId}" class="display table table-striped table-hover">
					<thead>
						<tr>
							<th>이름</th><th>직책</th><th>부서</th><th>회사</th><th>이메일</th><th>연락처</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="addr" items="${addressList}">
							<c:if test="${addr.adbkIsExternal eq currentType}">
								<tr>
									<td>${addr.adbkName}</td><td>${addr.adbkPosition}</td><td>${addr.adbkDept}</td>
									<td>${addr.adbkCompany}</td><td>${addr.adbkEmail}</td><td>${addr.adbkPhone}</td>
								</tr>
							</c:if>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</c:forEach>
</div>
<script src="${pageContext.request.contextPath}/resources/groupware/js/addressbook/addressList.js"></script>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="https://cdn.datatables.net/1.13.4/js/jquery.dataTables.min.js"></script>
