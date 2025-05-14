<!-- 
 * 전자결재 홈
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 18.     	JYS            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"  %>
<h4>전자결재 홈</h4>

<!-- 결재 대기 문서 -->
<div class="container">
	<div id="divTitle"><p>결재 대기 문서</p></div>
	<c:choose>
		<c:when test="${empty waitingDocs}">
			<p>결재할 문서가 없습니다.</p>
		</c:when> 
		<c:otherwise>
			<div class="row">
				<c:forEach var="doc" items="${waitingDocs}" varStatus="status">
					<c:if test="${status.index < 3}">
						<div class="col-md-4">
							<div class="card" data-doc-no="${doc.aprvlDocNo}" style="width: 100%; min-height: 100px; display: flex; flex-direction: column;"> <!-- card가 부모 div의 100% 너비를 차지하게 함 -->
								<div class="card-body">
									<h5 class="card-title">
										<span>${not empty doc.isEmergency ? '🚨' : ''}</span>
										<span class="badge badge-lg" style="background-color: ${doc.statusColor}; color: white; font-weight: bold;">
											${doc.statusName}
										</span>
									</h5>
									<table class="cardTable">
										<tr>
											<th>제목</th>
											<td>${doc.aprvlDocTitle}</td>
										</tr>
										<tr>
											<th>결재양식&nbsp;&nbsp;</th>
											<td>${doc.docFrmName}</td>
										</tr>
										<tr>
											<th>기안자</th>
											<td>${doc.drafterName}</td>
										</tr>
									</table>
								</div>
							</div>
						</div>
					</c:if>
				</c:forEach>
			</div>
		</c:otherwise>
	</c:choose>
</div>

<!-- 기안 진행 문서와 완료 문서 -->
<div class="container">
		<!-- 기안 진행 문서 -->
		<div>
			<h6>기안 진행 문서</h6>
			<table class="table table-hover">
				<thead>
					<tr>
						<th>기안일</th>
						<th>결재양식</th>
						<th>긴급</th>
						<th>제목</th>
						<th>첨부</th>
						<th>결재상태</th>
					</tr>
				</thead>
				<tbody class="table-group-divider">
					<c:choose>
						<c:when test="${empty draftWithDrawnDocs}">
							<tr>
								<td colspan="6" style="text-align: center;">결재 진행 중인 문서가 없습니다.</td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach var="doc" items="${draftWithDrawnDocs}">
								<tr data-doc-no="${doc.aprvlDocNo}">
									<td>${fn:substring(doc.submitDate, 0, 10)}</td>
									<td>${doc.docFrmName}</td>
									<td>${doc.isEmergency}</td>
									<td>${doc.aprvlDocTitle}</td>
									<td></td>
									<td>
										<span class="badge" style="background-color: ${doc.statusColor}; color: white;">
											${doc.statusName}
										</span>
									</td>
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>

		<!-- 결재 완료 문서 -->
		<div>
			<h6>결재 완료 문서</h6>
			<table class="table table-hover">
				<thead>
					<tr>
						<th>기안일</th>
						<th>결재양식</th>
						<th>긴급</th>
						<th>제목</th>
						<th>첨부</th>
						<th>결재상태</th>
					</tr>
				</thead>
				<tbody class="table-group-divider">
					<c:choose>
						<c:when test="${empty approvedDocs}">
							<tr>
								<td colspan="7" style="text-align: center;">결재 완료 문서가 없습니다.</td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach var="doc" items="${approvedDocs}">
								<tr data-doc-no="${doc.aprvlDocNo}">
									<td>${fn:substring(doc.submitDate, 0, 10)}</td>
									<td>${doc.docFrmName}</td>
									<td>${doc.isEmergency}</td>
									<td>${doc.aprvlDocTitle}</td>
									<td></td>
									<td>
										<span class="badge" style="background-color: ${doc.statusColor}; color: white;">
											${doc.statusName}
										</span>
									</td>
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>
	
</div>
<script src="${pageContext.request.contextPath }/resources/groupware/js/approval/apprHome.js"></script>