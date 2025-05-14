<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 15.     	KMJ            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<link href="${pageContext.request.contextPath }/resources/groupware/css/schedule/schedule.css" rel="stylesheet">
    <script src="https://unpkg.com/lottie-web@latest/build/player/lottie.min.js"></script>
    <script src="https://unpkg.com/@lottiefiles/lottie-player@latest/dist/lottie-player.js"></script>
	<title>${companyNo } - 일정관리</title>

	<script>
		let companyNo = "${companyNo}";
		let contextPath ="${pageContext.request.contextPath}";
	
		let clist = [
			<c:forEach var="schedule" items="${scheduleList}" varStatus="status" >
			{
// 				title: "${schedule.schdlCn}",
				start: "<fmt:formatDate value='${schedule.schdlBgngYmd}' pattern='yyyy-MM-dd' />",
				end: "<fmt:formatDate value='${schedule.schdlEndYmd}' pattern='yyyy-MM-dd' />",
				schdlNm: "${schedule.schdlNm}",
				schdlCn: "${schedule.schdlCn}",
				schdlLocation: "${schedule.schdlLocation}",
				createDate: "${schedule.schdlCreateDate}",
				schdlStatus: "${schedule.schdlStatus}",
 				scheduleTypeNo : "${schedule.scheduleTypeNo}",
				url: "${pageContext.request.contextPath}/{companyNo}/schedule/scheduleDetail.do?what=${schedule.schdlNo}",
//  				url1:"${schedule.schdlNo}",
 				groupId:"<c:out value="${status.index}" />"

			},
			
			</c:forEach>
		];	
		
 	    let typeList = [
	        <c:forEach var="schdl" items="${scheduleType}" varStatus="status">
	        {
	            scheduleTypeNo : "${schdl.scheduleTypeNo}",
	            scheduleTypeNm : "<c:out value="${schdl.scheduleTypeNm}"/>"
	        },
	        </c:forEach>
	    ]; 
// 		document.addEventListener('DOMContentLoaded', function() {
// 			var calendarEl = document.getElementById('calendar');
// 			let draggableEl = document.getElementById('external-events'); // 드래그해서 이벤트 추가하는 요소
	
// 			var calendar = new FullCalendar.Calendar(calendarEl, {
// 				plugins: [ interactionPlugin ], // 인터랙션 플러그인 추가
// 				initialView: 'dayGridMonth',
// 				locale: 'ko',
// 				editable: true, // 캘린더 내에서 이벤트 이동 가능
// 		        droppable: true, // 외부 이벤트 드롭 가능
// 				events: clist,
// 				eventClick: function(detail) {
					
// 					var idx_c = info.event['groupId'];
// 					var detailData = clist[idx_c];
//  					console.log(clist[idx_c].title);
// 					$("#modalId").text(detailData.url1); //아이디
// 					$("#modalNm").text(detailData.schdlNm); //일정명
// 					$("#modalCn").text(detailData.schdlCn); //일정설명
// 					$("#modalStart").text(detailData.start); //일정시작일
//  					$("#modalEnd").text(detailData.end); //일정종료일
//  					$("#modalLocation").text(detailData.schdlLocation); //일정장소
//  					$("#modalCD").text(detailData.createDate); //최초등록일시
//  					$("#modalStatus").text(detailData.schdlStatus); //일정상태
//  					$("#modalUd").text("HELLO"); //일정수정일시
//  					$("#modalAttend").text("HELLO"); //참석여부

//  					$("#scheduleModal").modal('show');
// 				}
	
// 			});
// 			calendar.render();
// 		});
    </script> 
<!-- 모달창 -->
<div class="modal fade" id="scheduleModal" tabindex="-1" aria-labelledby="scheduleModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="modalTitle">일정</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
<!--         <p><strong>아이디:</strong> <span id="modalId"></span></p> -->
<!--         <p><strong>일정명:</strong> <span id="modalNm"></span></p> -->
<!--         <p><strong>일정설명:</strong> <span id="modalCn"></span></p> -->
<!--         <p><strong>일정시작일:</strong> <span id="modalStart"></span></p> -->
<!--         <p><strong>일정종료일:</strong> <span id="modalEnd"></span></p> -->
<!--         <p><strong>일정장소:</strong> <span id="modalLocation"></span></p> -->
<!--         <p><strong>최초등록일시:</strong> <span id="modalCD"></span></p> -->
<!--         <p><strong>일정상태:</strong> <span id="modalStatus"></span></p> -->
<!--         <p><strong>일정수정일시:</strong> <span id="modalUd"></span></p> -->
<!--         <p><strong>참석여부:</strong> <span id="modalAttend"></span></p> -->
      </div>
    </div>
  </div>
</div>


<!-- <div class="container"> -->
	<div class="page-header">
	    <h3 class="fw-bold mb-3">일정관리</h3>
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
	            <a href="<c:url value='/${companyNo}/schedule'/>">Schedule</a>
	        </li>
	    </ul>
	</div>

	<div class="biggestContainer">
	    <div class="content">
	    		<div id="calendarLegend" class="mb-4">
	        <div class="legendItem" data-type="개인">
	            <span class="legendColor" style="background-color: red;"></span> 개인 일정
	        </div>
	        <div class="legendItem" data-type="부서">
	            <span class="legendColor" style="background-color: green;"></span> 부서 일정
	        </div>
	        <div class="legendItem" data-type="사내">
	            <span class="legendColor" style="background-color: lightblue;"></span> 사내 일정
	        </div>
	        <div class="legendItem" data-type="프로젝트">
	            <span class="legendColor" style="background-color: orange;"></span> 프로젝트 일정
	        </div>
		</div>
	        <div id='calendar'></div>
	    </div>
	</div>
	 
<!-- </div> -->
	<script src="${pageContext.request.contextPath }/resources/groupware/js/schedule/scheduleList.js"></script>
	<script src='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.15/index.global.min.js'></script>
	<script src="https://cdn.jsdelivr.net/npm/@fullcalendar/interaction@6.1.15/index.global.min.js"></script>
