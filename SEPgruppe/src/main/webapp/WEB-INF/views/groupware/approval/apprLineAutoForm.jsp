<!-- 
 * 자동결재선 추가 폼
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

<style>
    #autoLineSetTbody {
        display: block;
        height: 200px; /* 고정 높이 */
        overflow-y: auto; /* 스크롤 생성 */
        width: 100%;
    }

    #autoLineSetTable thead, #autoLineSetTable tbody tr {
        display: table;
        width: 100%;
        table-layout: fixed;
    }

    .button-container {
        /* position: fixed; */
        bottom: 50px;
        width: 100%;
        text-align: center;
        background-color: white;
        padding: 10px 0;
    }
    
    #centerTr, #autoLineSetTbody {
    	text-align: center; /* 가로(수평) 중앙 정렬 */
    	vertical-align: middle; /* 세로(수직) 중앙 정렬 */
    }
    
    option {
    	text-align:center;
    }

</style>

<div>
	<caption>(*)표시는 <strong>필수입력</strong> 사항입니다.</caption>
	<div id="autoLineNameDiv">
		<table id="autoLineNameTable" class="table">
			<tr>
				<th><span style="color:red;">*</span>자동결재선 명</th>
				<td><input type="text" id="autoLineName" placeholder="자동결재선 명 입력" required></td>
			</tr>
			<tr>
				<th>결재양식</th>
				<td>
					<select id="docFrmNo">
						<option value="none">====  양식명  ====</option>
						<c:if test="${not empty apprDocList}">
						    <c:forEach var="apprDoc" items="${apprDocList}">
						        <option value="${apprDoc.docFrmNo}">${apprDoc.docFrmName}</option>
						    </c:forEach>
						</c:if>
					</select>
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
				<!-- 결재자 추가 버튼 클릭 시 행이 동적으로 생성 -->
			</tbody>
		</table>
	</div>
</div>

<div class="button-container">
	<button id="apprLineSaveBtn" class="btn btn-secondary ">저장</button>
	<button class="btn btn-outline-secondary" onclick="history.back()">취소</button>
</div>


<!-- 부트스트랩 토스트 -->
<div id="toastContainer" class="position-absolute top-0 start-50 translate-middle-x p-3" style="z-index: 1050;">
    <div id="liveToast" class="toast align-items-center text-white bg-danger border-0" role="alert" aria-live="assertive" aria-atomic="true" data-bs-delay="2000">
        <div class="d-flex">
            <div class="toast-body">
                결재자가 존재하지 않습니다.
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath }/resources/groupware/js/approval/apprLineAutoForm.js"></script>