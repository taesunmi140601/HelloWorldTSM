<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 24.     	KKM            최초 생성
 *  2025. 4.  2.     	SJH           수정 중
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/groupware/css/reservation/reservationDetail.css">

<!-- 모달 헤더 -->
<div class="modal-header">
	<h5 class="modal-title">예약 상세 정보</h5>
	<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
</div>

<!-- 모달 본문 -->
<div class="modal-body">
	<table class="table table-bordered">
		<tr>
			<th>예약일</th>
			<td>
				시작: <fmt:formatDate value="${reservationStartDate}" pattern="yyyy-MM-dd HH:mm" /><br />
				종료: <fmt:formatDate value="${reservationEndDate}" pattern="yyyy-MM-dd HH:mm" />
			</td>
		</tr>
		<tr>
			<th>예약자</th>
			<td>${reservation.empNm}</td>
		</tr>
		<tr>
			<th>예약 내용</th>
			<td>${reservation.reservationContent}</td>
		</tr>
	</table>

	<c:if test="${isOwner}">
	<h5>예약된 회의 수정</h5>
	<hr>
		<form method="post" id="reservationUpdateForm"
			action="${pageContext.request.contextPath}/${companyNo}/reservation/update">
			<input type="hidden" name="reservationNo" value="${reservation.reservationNo}" />
			<input type="hidden" name="roomNo" value="${reservation.roomNo}" />

			<div class="form-group">
				<label for="reservationDate">날짜</label>
				<input type="date" class="form-control" name="reservationDate" value="${reservationDateFormatted}" required />
			</div>

			<div class="form-group">
				<label for="reservationStartTime">시작 시간</label>
				<select id="reservationStartTime" name="reservationStartTime" class="form-control sel_start_time" data-value="${reservationStartTimeFormatted}" required></select>
			</div>

			<div class="form-group">
				<label for="reservationEndTime">종료 시간</label>
				<select id="reservationEndTime" name="reservationEndTime" class="form-control sel_end_time" data-value="${reservationEndTimeFormatted}" required></select>
			</div>

			<div class="form-group">
				<label for="reservationContent">예약 내용</label>
				<input type="text" class="form-control" name="reservationContent" value="${reservation.reservationContent}" required />
			</div>

			<div class="d-flex justify-content-between mt-4">
				<button type="submit" class="btn btn-danger"
						formaction="${pageContext.request.contextPath}/${companyNo}/reservation/${reservation.reservationNo}/delete"
						formmethod="post"
						formnovalidate
						form="reservationUpdateForm"
						name="_method"
						value="delete">삭제</button>
			
				<button type="submit" class="btn btn-primary">수정</button>
			</div>
		</form>
	</c:if>
</div>

<!-- JS 연결 -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>