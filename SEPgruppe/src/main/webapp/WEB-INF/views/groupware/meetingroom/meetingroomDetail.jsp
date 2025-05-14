<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 20.     	KKM            최초 생성
 *  2025. 4. 16.     	SJH            회의실 상세 목록 수정 중
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <title>${roomDetail.roomNm}의 상세 정보</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/groupware/css/meetingroom/meetingroomDetail.css">
	
	<div class="container">
	    <h2>${roomDetail.roomNm} 회의실 상세 정보</h2>
	    <table class="table table-bordered">
	        <tbody>
	            <tr>
	                <th>회의실 번호</th>
	                <td>${roomDetail.roomNo}</td>
	            </tr>
	            <tr>
	                <th>회의실 위치</th>
	                <td>${roomDetail.roomLocation}</td>
	            </tr>
	            <tr>
	                <th>예약 가능 여부</th>
	                <td>${roomDetail.roomAvailability}</td>
	            </tr>
	            <tr>
	                <th>수용 인원</th>
	                <td>${roomDetail.roomCapacity}</td>
	            </tr>
	        </tbody>
	    </table>
	    <div class="text-center">
	        <a href="${pageContext.request.contextPath}/${companyNo}/meetingroom" class="btn btn-secondary">목록으로 돌아가기</a>
	        <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#editRoomModal">수정하기</button>
	        <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#deleteRoomModal">삭제하기</button>
	    </div>
	
	    <!-- 회의실 수정 모달 -->
	    <div class="modal fade" id="editRoomModal" tabindex="-1" role="dialog" aria-labelledby="editRoomModalLabel" aria-hidden="true">
	        <div class="modal-dialog" role="document">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <h5 class="modal-title" id="editRoomModalLabel">회의실 수정하기</h5>
	                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	                        <span aria-hidden="true">&times;</span>
	                    </button>
	                </div>
	                <form action="${pageContext.request.contextPath}/${companyNo}/meetingroom/${roomDetail.roomNo}/edit" method="post">
	                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	                    <div class="modal-body">
	                        <div class="form-group">
	                            <label for="roomNm">회의실 이름</label>
	                            <input type="text" class="form-control" id="roomNm" name="roomNm" value="${roomDetail.roomNm}" required>
	                        </div>
	                        <div class="form-group">
	                            <label for="roomLocation">회의실 위치</label>
	                            <input type="text" class="form-control" id="roomLocation" name="roomLocation" value="${roomDetail.roomLocation}" required>
	                        </div>
	                        <div class="form-group">
	                            <label for="roomAvailability">예약 가능 여부</label>
	                            <select class="form-control" id="roomAvailability" name="roomAvailability" required>
	                                <option value="가능" <c:if test="${roomDetail.roomAvailability == '가능'}">selected</c:if>>가능</option>
	                                <option value="불가능" <c:if test="${roomDetail.roomAvailability == '불가능'}">selected</c:if>>불가능</option>
	                            </select>
	                        </div>
	                        <div class="form-group">
	                            <label for="roomCapacity">수용 인원</label>
	                            <input type="number" class="form-control" id="roomCapacity" name="roomCapacity" value="${roomDetail.roomCapacity}" required>
	                        </div>
	                    </div>
	                    <div class="modal-footer">
	                        <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
	                        <button type="submit" class="btn btn-primary">수정</button>
	                    </div>
	                </form>
	            </div>
	        </div>
	    </div>
	
	    <!-- 회의실 삭제 모달 -->
	    <div class="modal fade" id="deleteRoomModal" tabindex="-1" role="dialog" aria-labelledby="deleteRoomModalLabel" aria-hidden="true">
	        <div class="modal-dialog" role="document">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <h5 class="modal-title" id="deleteRoomModalLabel">회의실 삭제하기</h5>
	                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	                        <span aria-hidden="true">&times;</span>
	                    </button>
	                </div>
	                <div class="modal-body">
	                    정말로 이 회의실을 삭제하시겠습니까?
	                </div>
	                <div class="modal-footer">
	                    <form action="${pageContext.request.contextPath}/${companyNo}/meetingroom/${roomDetail.roomNo}/delete" method="post">
	                        <input type="hidden" name="_method" value="delete"/> <!-- Hidden input 추가 -->
	                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	                        <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
	                        <button type="submit" class="btn btn-danger">삭제</button>
	                    </form>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
