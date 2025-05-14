<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 20.     	KKM            회의실 목록 페이지 생성 및 수정
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <meta charset="UTF-8">
    <title>${companyNo}의 회의실 목록</title>
<!--     <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"> -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<!--     <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script> -->
    <script src="https://unpkg.com/lottie-web@latest/build/player/lottie.min.js"></script>
    <script src="https://unpkg.com/@lottiefiles/lottie-player@latest/dist/lottie-player.js"></script>
	<script>
	    // JSP에서 서버측 변수를 JavaScript 변수로 전달
	    var contextPath = "${pageContext.request.contextPath}";
	    var companyNo = "${companyNo}";
	</script>
	
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/groupware/css/meetingroom/meetingroomList.css">
	<div class="roomContainer">
	<div class="page-header">
	    <!-- 상단 메뉴 -->
	    <h3 class="fw-bold mb-3">예약관리</h3>
	    <ul class="breadcrumbs mb-3">
	        <li class="nav-home">
	            <a href="<c:url value='/${realUser.companyNo }/groupware'/>">
	                <i class="icon-home"></i>
	            </a>
	        </li>
	        <li class="separator">
	            <i class="icon-arrow-right"></i>
	        </li>
	        <li class="nav-item">
	            <a href="<c:url value='/${realUser.companyNo }/webhard' />">회의실 목록</a>
	        </li>
	    </ul>
	</div>	
    <div class="container d-flex justify-content-center">
    	<div class="meetingroom-wrapper">
	    	<div class="meetingroom-header">
<%-- 	    	    <img src="${pageContext.request.contextPath}/resources/groupware/images/meetingroom.png" alt="room" class="room"> --%>
	    	      <lottie-player
				    src="${pageContext.request.contextPath}/resources/groupware/css/survey/Animation - 1744681128928.json"
				    background="transparent"
				    speed="1"
				    style="width: 400px; height: 400px;"
				    loop
				    autoplay>
				  </lottie-player>
		        <h2>${companyNo}의 회의실 목록</h2>
		    </div>    
	        <table class="table table-bordered">
	            <thead>
	                <tr>
	                    <th>회의실 번호</th>
	                    <th>회의실 이름</th>
	                    <th>회의실 위치</th>
	                    <th>예약 가능 여부</th>
	                    <th>수용 인원</th>
	                </tr>
	            </thead>
	            <tbody>
	                <c:forEach var="room" items="${meetingRooms}">
	                    <tr>
	                        <td>
							    <a href="javascript:void(0);" 
							       onclick="loadRoomDetail(${room.roomNo})">
							        ${room.roomNo}
							    </a>
							</td>
	                        <td>${room.roomNm}</td>
	                        <td>${room.roomLocation}</td>
	                        <td>${room.roomAvailability}</td>
	                        <td>${room.roomCapacity}</td>
	                    </tr>
	                </c:forEach>
	            </tbody>
	        </table>
	
	        <div class="text-center">
	            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#addRoomModal">회의실 추가하기</button>
	        </div>
	        
	        <!-- 회의실 추가 모달 -->
	        <div class="modal fade" id="addRoomModal" tabindex="-1" role="dialog" aria-labelledby="addRoomModalLabel" aria-hidden="true">
	            <div class="modal-dialog" role="document">
	                <div class="modal-content">
	                    <div class="modal-header">
	                        <h5 class="modal-title" id="addRoomModalLabel">회의실 추가하기</h5>
	                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	                            <span aria-hidden="true">&times;</span>
	                        </button>
	                    </div>
	                    <form action="${pageContext.request.contextPath}/${companyNo}/meetingroom" method="post">
	                        <div class="modal-body">
	                            <div class="form-group">
	                                <label for="roomNm">회의실 이름</label>
	                                <input type="text" class="form-control" id="roomNm" name="roomNm" required>
	                            </div>
	                            <div class="form-group">
	                                <label for="roomLocation">회의실 위치</label>
	                                <input type="text" class="form-control" id="roomLocation" name="roomLocation" required>
	                            </div>
	                            <div class="form-group">
	                                <label for="roomAvailability">예약 가능 여부</label>
	                                <select class="form-control" id="roomAvailability" name="roomAvailability" required>
	                                    <option value="가능">가능</option>
	                                    <option value="불가능">불가능</option>
	                                </select>
	                            </div>
	                            <div class="form-group">
	                                <label for="roomCapacity">수용 인원</label>
	                                <input type="number" class="form-control" id="roomCapacity" name="roomCapacity" required>
	                            </div>
	                        </div>
	                        <div class="modal-footer">
	                            <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
	                            <button type="submit" class="btn btn-primary">추가</button>
	                        </div>
	                    </form>
	                </div>
	            </div>
	        </div>
	        
	        <!-- 회의실 상세 조회 -->
			<div class="modal fade" id="roomDetailModal" tabindex="-1" role="dialog" aria-labelledby="roomDetailModalLabel" aria-hidden="true">
			    <div class="modal-dialog" role="document">
			        <div class="modal-content">
			            <div class="modal-header">
			                <h5 class="modal-title" id="roomDetailModalLabel">회의실 상세 정보</h5>
			                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	                            <span aria-hidden="true">&times;</span>
	                        </button>
			            </div>
			            <div class="modal-body" id="roomDetailContent">
			                <!-- AJAX로 로딩한 내용이 들어옵니다 -->
			            </div>
			            <div class="modal-footer">
			                <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
			            </div>
			        </div>
			    </div>
			</div>
			
        </div>
    </div>
</div>    

<script src="${pageContext.request.contextPath}/resources/groupware/js/meetingroom/meetingroomList.js"></script>
