<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 21.     	KMJ            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:url value="/${companyNo}/schedule" var="Cschedule"></c:url>
<form:form id="scheduleEdit" modelAttribute="schedule" method="post" enctype="multipart/form-data">
  <input type="hidden" name="_method" value="PUT" />
  <table class="table">
    <tr>
      <th>일정 순번</th>
      <td><input type="text" id="schdlNo" name="schdlNo" class="form-control" 
      		value="${schedule.schdlNo}" readonly/>
          <form:errors path="schdlNo" class="text-danger" element="span"/></td>
    </tr>
    <tr>
      <th>아이디</th>
      <td><input type="text" id="empId" name="empId" class="form-control" 
      		value="${schedule.empId}" readonly />
          <form:errors path="empId" class="text-danger" element="span"/></td>
     </tr>
     <tr>
       <th>일정 유형</th>
       <td>    
       	<select id="scheduleTypeNo" name="scheduleTypeNo" class="form-control">
		<option value=0>--항목선택--</option>
		<option value=1>개인</option>
		<option value=2>부서</option>
		<option value=3>사내</option>
		<option value=4>프로젝트</option>
		</select>
           <form:errors path="scheduleTypeNo" class="text-danger" element="span"/></td>
    </tr>
    <!-- schdlTypeNm 저장 필드 -->
	<input type="hidden" id="schdlTypeNm" name="schdlTypeNm">
    <tr>
      <th>일정명</th>
      <td><input type="text" id="schdlNm" name="schdlNm" class="form-control" 
      		value="${schedule.schdlNm}" />
          <form:errors path="schdlNm" class="text-danger" element="span"/></td>
    </tr>
    <tr>
      <th>일정설명</th>
      <td><input type="text" id="schdlCn" name="schdlCn" class="form-control" 
      		value="${schedule.schdlCn}" />
          <form:errors path="schdlCn" class="text-danger" element="span"/></td>
    </tr>
    <tr>
      <th>일정 시작일</th>
      <td><input type="date" name="schdlBgngYmd" id="schdlBgngYmd" class="form-control" required />
          <form:errors path="schdlBgngYmd" class="text-danger" element="span"/></td>
    </tr>
    <tr>
      <th>일정 종료일</th>
      <td><input type="date" name="schdlEndYmd" id="schdlEndYmd" class="form-control" required />
          <form:errors path="schdlEndYmd" class="text-danger" element="span"/></td>
    </tr>
    <tr>
      <th>일정 장소</th>
      <td><input type="text" id="schdlLocation" name="schdlLocation" class="form-control" 
      		value="${schedule.schdlLocation}" />
          <form:errors path="schdlLocation" class="text-danger" element="span"/></td>
    </tr>
    <tr>
      <th>최초 등록 일시</th>
      <td><input type="text" id="schdlCreateDate" name="schdlCreateDate" class="form-control" 
      		readonly />
          <form:errors path="schdlCreateDate" class="text-danger" element="span"/></td>
    </tr>
    <tr>
      <th>알람 시간</th>
      <td><input type="date" id="notifyTime" name="notifyTime" class="form-control" 
      		value="${schedule.notifyTime}" />
          <form:errors path="notifyTime" class="text-danger" element="span"/></td>
     </tr>
     <tr>
       <th>참석 여부</th>
       <td>    
       	<select id="schdlStatus" name="schdlStatus" class="form-control">
		<option value="0">--항목선택--</option>
		<option value="1">참석</option>
		<option value="2">불참</option>
		<option value="3">미정</option>
		</select>
           <form:errors path="schdlStatus" class="text-danger" element="span"/></td>
    </tr>
  </table>
  <div class="modal-footer">
    <button type="submit" id="schdlEdit" class="btn btn-success">일정 수정</button>
  </div>
</form:form>

