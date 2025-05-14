<!--
 * == 개정이력(Modification Information) ==
 *
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 12.     	JYS            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<security:authentication property="principal.realUser" var="realUser"/> <!-- Provider 시큐리티 정보 -->

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/groupware/css/notice/noticeDetail.css" />
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">

<div class="noticeDetailContainer">
	<div class="page-header">
		<h3 class="fw-bold mb-3">공지사항</h3>
		<ul class="breadcrumbs mb-3">
			<!-- HOME -->
			<li class="nav-home">
				<a href="<c:url value='/${companyNo}/groupware'/>">
					<i class="icon-home"></i>
				</a>
			</li>
			<!-- '>' 표시 아이콘 -->
			<li class="separator">
				<i class="icon-arrow-right"></i>
			</li>
			<!-- 분류 -->
			<li class="nav-item">
				<a href="<c:url value='/${companyNo}/notice'/>">공지사항</a>
			</li>
		</ul>
	</div>

	<div class="notice-container">
		<!-- 버튼 영역 -->
		<c:if test="${realUser.userId eq detailNotice.empId }">
			<div class="button-group">
				<c:url var="updateUrl" value="/${companyNo}/notice/${noticeNo}/editForm"/>
				<button type="button" class="btn btn-sm btn-info" onclick="location.href='${updateUrl}'">Update</button>
			</div>
		</c:if>

		<!-- 분류 -->
		<div class="notice-info"><strong>${detailNotice.noticeCategory}</strong></div>

		<!-- 제목 -->
		<div class="notice-title">${detailNotice.noticeTitle}</div>

		<!-- 작성자, 날짜, 조회수 -->
		<div class="notice-info">
			<span>${detailNotice.empNm} ${detailNotice.positionName}</span>
			<span>${detailNotice.noticeCreatedAt} | 조회수: ${detailNotice.noticeViewCount}</span>
		</div>

		<!-- 본문 -->
		<div class="notice-content">
			${detailNotice.noticeContent}
		</div>

		<!-- 첨부파일 -->
		<div>
			<c:forEach var="file" items="${detailNotice.file}">
				<c:if test="${not empty file.attachOrgFileName}">
					<div style="display: flex; align-items: center; margin-bottom: 6px;">
						<!-- 파일명 + 크기 -->
						<div style="min-width: 250px;">
							${file.attachOrgFileName}
							(<c:choose>
								<c:when test="${file.attachFileSize == 0}">
									1MB
								</c:when>
								<c:otherwise>
									<fmt:formatNumber value="${file.attachFileSize / 1024 / 1024}" type="number" maxFractionDigits="2"/>MB
								</c:otherwise>
							</c:choose>)
						</div>
						<!-- 다운로드 버튼 -->
						<c:set var="filePath" value="${file.attachFilePath}" />
						<c:choose>
							<c:when test="${not empty file.attachOrgFileName}">
								<a href="#"
								   class="btn btn-link btn-primary"
								   style="margin-left: 10px;"
								   data-file-no="${file.attachFileNo}"
								   onclick="downloadFile(event, this)">
									<i class="bi bi-download"></i>
								</a>
							</c:when>
							<c:otherwise>
								<button class="btn btn-secondary btn-sm" disabled>파일 없음</button>
							</c:otherwise>
						</c:choose>
					</div>
				</c:if>
			</c:forEach>
		</div>
	</div>
</div>

<!-- 모달 -->
<div class="modal fade" id="errorModal" tabindex="-1" aria-labelledby="errorModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header bg-danger text-white">
				<h5 class="modal-title" id="errorModalLabel">에러 발생</h5>
				<button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
			</div>
			<div class="modal-body" id="errorModalBody">
				<!-- 여기에 메시지가 동적으로 들어감 -->
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-danger" data-bs-dismiss="modal">확인</button>
			</div>
		</div>
	</div>
</div>

<script>
function showErrorModal(message) {
	const modalBody = document.getElementById('errorModalBody');
	modalBody.textContent = message;

	const errorModal = new bootstrap.Modal(document.getElementById('errorModal'));
	errorModal.show();
}

function downloadFile(event, element) {
	event.preventDefault();

	const fileNo = element.dataset.fileNo;
	const companyNo = '${companyNo}';
	const noticeNo = '${noticeNo}';
	const downloadUrl = "/sep/" + companyNo + "/notice/" + noticeNo + "/download?attachFileNo=" + fileNo;

	// HEAD 요청으로 파일 존재 여부 확인
	fetch(downloadUrl, { method: 'HEAD' })
		.then(response => {
			if (response.ok) {
				window.location.href = downloadUrl;
			} else if (response.status === 404) {
				showErrorModal("❗ 파일을 찾을 수 없습니다. 관리자에게 문의하세요.");
			} else {
				showErrorModal("⚠️ 파일 다운로드 중 오류가 발생했습니다. (" + response.status + ")");
			}
		})
		.catch(error => {
			console.error("에러 발생:", error);
			showErrorModal("🚫 서버에 접속할 수 없습니다. 네트워크 상태를 확인하세요.");
		});
}
</script>
