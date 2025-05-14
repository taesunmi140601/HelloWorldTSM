<!-- 
 * 자동결재선 수정 폼
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 28.     	JYS            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/groupware/css/approval/apprLineAutoDetail.css" >

<!-- 서버에서 받은 결재선 JSON 데이터  -->
<script type="application/json" id="aprvlLineJsonData">
${json}
</script>

<h4>자동 결재선 수정</h4>
<div>
	<div id="autoLineNameDiv">
		<table id="autoLineNameTable" class="table">
			<tr>
				<th>자동결재선 명</th>
				<td>
					<c:forEach var="line" items="${lines }">
						<input type="hidden" id="autoLineNo" value="${line.aprvlLineNo }">
						<input id="autoLineName" value="${line.aprvlLineName }" readonly></input> 
					</c:forEach>
				</td>
			</tr>
			<tr>
				<th>결재양식</th>
				<td>
					<c:forEach var="line" items="${lines }">
						<input class="docFrmNo" data-doc-frm-no="${line.docFrmNo }" value="${line.docFrmName }" readonly></input> 
					</c:forEach>
				</td>
			</tr>
		</table>
	</div>
	<div id="autoLineSetDiv">
		<table id="autoLineSetTable" class="table">
			<thead>
				<tr>
					<th colspan="4">결재선 설정</th>
					<td align="right">
						<button class="btn" id="apprLineAddBtn"><i class="fa fa-plus"></i>&nbsp;결재자 추가</button>
						<button class="btn" id="apprLineChangeBtn"><i class="fa fa-sort"></i>&nbsp;순서 바꾸기</button>
					</td>
					
				</tr>
				<tr id="centerTr">
					<th>순서</th>
					<th>타입</th>
					<th>부서</th>
					<th>직급</th>
					<th>삭제</th>
				</tr>
			</thead>
			<tbody id="autoLineSetTbody">
					
			</tbody>
		</table>
	</div>
</div>

<div class="button-container">
	<button id="apprLineUpdateBtn" class="btn btn-secondary apprbtn">저장</button>
	<button id="apprLineDeleteBtn" class="btn btn-secondary apprbtn">삭제</button>
	<button class="btn btn-outline-secondary apprbtn" onclick="history.back()">취소</button>
</div>


<!-- 부트스트랩 토스트 -->
<div id="toastContainer" class="position-absolute top-0 start-50 translate-middle-x p-3" style="z-index: 1050;">
    <div id="liveToast" class="toast align-items-center text-white bg-danger border-0" role="alert" aria-live="assertive" aria-atomic="true" data-bs-delay="2000">
        <div class="d-flex">
            <div class="toast-body">
                결재자가 존재하지 않습니다.
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto apprbtn" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath }/resources/groupware/js/approval/apprLineAutoForm.js"></script> 
