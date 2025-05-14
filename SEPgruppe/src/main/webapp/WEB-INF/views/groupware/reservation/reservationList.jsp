<!--
 * == 개정이력(Modification Information) ==
 *
 *   수정일               수정자           수정내용
 *  ============      ============== =======================
 *  2025. 3. 14.        JYS            회의실 예약 전체 조회 페이지 생성
 *  2025. 4. 14.        SJH            예약 상세 모달 연동 추가
 *
-->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<body data-current-date="${currentDate}">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/groupware/css/reservation/reservationList.css">

	<div class="page-header">
	    <h3 class="fw-bold mb-3">예약관리</h3>
	    <ul class="breadcrumbs mb-3">
	        <li class="nav-home">
	            <a href="<c:url value='/${companyNo}/groupware'/>">
	                <i class="icon-home"></i>
	            </a>
	        </li>
	        <li class="separator">
	            <i class="icon-arrow-right"></i>
	        </li>
	        <li class="nav-item">
	            <a href="<c:url value='/${companyNo}/reservation'/>">회의실 예약</a>
	        </li>
	    </ul>
	</div>
	
	<div class="container">
		<h2 class="mt-3">회의실 예약 현황</h2>
		<h4>
			<a href="javascript:void(0);" onclick="changeDate(-1)">이전</a> <span
				id="currentDate">${currentDate}</span> <a href="javascript:void(0);"
				onclick="changeDate(1)">다음</a>
		</h4>

		<table class="table table-bordered text-center">
			<thead class="thead-light">
				<tr>
					<th>회의실</th>
					<c:forEach var="hour" begin="9" end="19">
						<th>${hour}:00</th>
					</c:forEach>
				</tr>
			</thead>
			<tbody>
				<security:authentication property="principal" var="principal" />
				<c:forEach var="room" items="${allRooms}">
					<tr>
						<td>${room.roomNm}</td>
						<c:forEach var="hour" begin="9" end="19">
							<c:set var="timeKey" value="${hour}:00" />
							<c:set var="res" value="${schedule[room.roomNo][timeKey]}" />
							<c:choose>
								<c:when test="${not empty res}">
									<td class="bg-light text-muted">
									<a href="javascript:void(0);" class="open-detail-modal"
										data-reservation-no="${res.reservationNo}"> ${res.empNm}<br />(상세보기)
									</a> <c:if test="${res.empId eq principal.realUser.userId}">
											<form
												action="${pageContext.request.contextPath}/${companyNo}/reservation/${res.reservationNo}/delete"
												method="post">
												<input type="hidden" name="${_csrf.parameterName}"
													value="${_csrf.token}" />
												<button type="submit" class="btn btn-danger btn-sm mt-1">취소</button>
											</form>
										</c:if></td>
								</c:when>
								<c:otherwise>
									<td class="bg-white" style="cursor: pointer;"
										onclick="openReservationModal('${room.roomNo}', '${hour}:00')">&nbsp;</td>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<!-- 예약 등록 모달 -->
		<div id="reservationModal" class="modal" style="display: none;">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title">예약 등록</h5>
						<button type="button" class="btn-close"
							onclick="closeReservationModal()"></button>
					</div>
					<div class="modal-body">
						<form id="reservationForm" method="post">
							<input type="hidden" id="roomNo" name="roomNo" /> <input
								type="hidden" id="reservationDate" name="reservationDate"
								value="${currentDate}" /> <input type="hidden"
								id="reservationStartTime" name="reservationStartTime" /> <input
								type="hidden" id="reservationEndTime" name="reservationEndTime" />

							<div class="form-group">
								<label for="reservationContent">예약 내용</label> <input type="text"
									class="form-control" id="reservationContent"
									name="reservationContent" required />
							</div>

							<div class="form-group">
								<label for="displayTime">예약 시간</label> <input type="text"
									class="form-control" id="displayTime" readonly />
							</div>

							<div class="modal-footer">
								<button type="submit" class="btn btn-primary">예약 등록</button>
								<button type="button" class="btn btn-secondary"
									onclick="closeReservationModal()">취소</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>

		<!-- 상세 보기 모달 삽입될 영역 -->
		<div class="modal fade" id="reservationDetailModal" tabindex="-1" role="dialog" aria-hidden="true">
			<div class="modal-dialog modal-lg" role="document">
				<div class="modal-content" id="reservationDetailModalContent">
					<!-- 여기에 reservationDetail.jsp가 fetch로 삽입됨 -->
				</div>
			</div>
		</div>
	</div>

	<!-- JS 연결 -->
	<script src="${pageContext.request.contextPath}/resources/groupware/js/reservation/reservationList.js"></script>
	<script src="${pageContext.request.contextPath}/resources/groupware/js/reservation/reservationDetail.js"></script>
</body>
