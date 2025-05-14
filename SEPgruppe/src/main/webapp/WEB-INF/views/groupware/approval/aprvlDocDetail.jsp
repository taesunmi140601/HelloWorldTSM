<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 4. 11.     	JYS            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script src="https://cdnjs.cloudflare.com/ajax/libs/html2canvas/1.4.1/html2canvas.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>

<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/groupware/css/approval/apprForm.css" >
<div>
	<button type="button" class="btn" onclick="handleWithdraw()">
		<i class="fas fa-check"></i>회수
	</button>
	<button type="button" class="btn" data-status="A">
		<i class="fas fa-check"></i>승인
	</button>
	<button type="button" class="btn" data-status="R">
		<i class="fas fa-times-circle"></i>반려
	</button>
	<button type="button" class="btn" data-status="H">
		<i class="fas fa-hourglass-half"></i>보류
	</button>
	<button type="button" class="btn" onclick="downloadPDF()">
		<i class="fa fa-save"></i>다운로드
	</button>
	<button type="button" class="btn" onclick="history.back()">
		이전
	</button>
</div>

<table>
	<tbody>
		<tr>
			<td><input type="hidden" id="currentUserId" value="${loginEmpId}" /></td>
			<td>
				<div id="aprvlDocDetailDiv" data-doc-no="${aprvlDocNo }">
					${aprvlDocContents }
				</div>
			</td>
		</tr>
		<tr>
			<td>
				<div id="attachFileDiv">
					첨부파일
				</div>
			</td>
		</tr>
	</tbody>
</table>


<!-- 결재요청 결과 / 임시저장 성공 여부 알람 토스트 -->
<div id="apprResToast" class="toast align-items-center border-0" role="alert" aria-live="assertive" aria-atomic="true">
  <div class="d-flex">
    <div class="toast-body">	
    	<!-- 알람 내용 -->
    </div>
    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
  </div>
</div>


<script src="${pageContext.request.contextPath }/resources/groupware/js/approval/apprHome.js"></script>
<script src="${pageContext.request.contextPath }/resources/groupware/js/approval/aprvlDocDetail.js"></script>
