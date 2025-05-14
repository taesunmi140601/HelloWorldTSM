<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 18.     	KMJ            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<link href="${pageContext.request.contextPath }/resources/groupware/css/schedule/schedule.css" rel="stylesheet">

<table class="table">
        <tr>
        	<th>선택</th>
			<td><input type="radio" name="schdlEdit" value="${schedule.schdlNo}"></td>
		</tr>
        <tr>		
            <th>일정순번</th>
			<td>${schedule.schdlNo}</td>
		</tr>
		<tr>
            <th>아이디</th>
            <td>${schedule.empId}</td>	
		</tr>
		<tr>
            <th>일정명</th> 
			<td>${schedule.schdlNm}</td>
		</tr>
		<tr>
            <th>일정설명</th>
			<td>${schedule.schdlCn}</td>
		</tr>
		<tr>
            <th>일정시작일</th>
			<td><fmt:formatDate value="${schedule.schdlBgngYmd}" pattern="yyyy-MM-dd" /></td>
		</tr>
		<tr>
            <th>일정종료일</th>
			<td><fmt:formatDate value="${schedule.schdlEndYmd}" pattern="yyyy-MM-dd" /></td>
		</tr>
		<tr>
            <th>일정장소</th>
			<td>${schedule.schdlLocation}</td>
		</tr>
		<tr>
            <th>등록일시</th>
			<td><fmt:formatDate value="${schedule.schdlCreateDate}" pattern="yyyy-MM-dd HH:mm:ss (E)" /></td>
		</tr>
		<tr>
            <th type="hidden">일정상태</th>
			<td type="hidden">${schedule.schdlStatus}</td>
		</tr>
		<tr>
            <th type="hidden">수정일시</th>
			<td type="hidden" id="schdlUpdateDate">${schedule.schdlUpdateDate}</td>
        </tr>
</table>
<button type="button" id="editSchdl" class="btn btn-success" >수정</button>
<button type="button" id="deleteSchdl" class="btn btn-danger" >삭제</button>
