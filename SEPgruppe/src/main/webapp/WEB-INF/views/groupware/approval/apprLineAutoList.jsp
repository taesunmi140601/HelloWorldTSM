<!-- 
 * 자동결재선 폼
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
	table {
		text-align:center;
	}
	
	#apprLineTr{
		cursor: pointer;
	}
</style>
<h4>자동결재선 설정</h4>
<button class="btn" onclick="location.href=`${pageContext.request.contextPath}/${companyNo }/approval/admin/apprLineAutoAdd`"><i class="fa fa-plus"></i>&nbsp;추가</button>
<button class="btn"><i class="fa fa-trash"></i>&nbsp;삭제</button>
<table	class="table">
	<thead>
		<tr>
			<th><input type="checkbox"></th>
			<th>자동결재선 명</th>
			<th>양식명</th>
			<th colspan="3">결재선</th>
		</tr>
	</thead>	
	<tbody id="apprLineAutoTbody">
		<c:choose>
			<c:when test="${empty lineList}">
				<tr>
					<td colspan="6">자동 결재선이 없습니다.</td>
				</tr>
			</c:when>
			<c:otherwise>
				<!-- 결재선 목록을 돌면서 결재선 정보를 표시 -->
				<c:forEach var="line" items="${lineList}">
					<tr id="apprLineTr" data-line-no ="${line.aprvlLineNo }" data-doc-no="${line.docFrmNo }">
						<td><input type="checkbox"></td>
						<td>${line.aprvlLineName}</td>
						<td>${line.docFrmName}</td>
					
						<td colspan="3">
						<!-- lineMap에서 해당 aprvlLineNo에 속하는 결재자 목록 가져오기 -->
						<c:forEach var="detail" items="${lineMap[line.aprvlLineNo]}" varStatus="status">
								
									<c:choose>
										<c:when test="${empty detail.deptName}" >
											
										</c:when>
										<c:otherwise>
											${detail.deptName}
										</c:otherwise>
									</c:choose>
									 ${detail.positionName}
									<c:choose>
								        <c:when test="${detail.commCodeName eq '결재'}">
								            <i class="fa fa-check-circle text-success"></i>
								        </c:when>
								        <c:when test="${detail.commCodeName eq '합의'}">
								            <i class="fa fa-handshake text-warning"></i>
								        </c:when>
								        <c:otherwise>
								            ${detail.commCodeName}
								        </c:otherwise>
								    </c:choose>
								
								 <c:if test="${!status.last}"> <!-- 마지막 반복이 아닐 때만 화살표 추가 -->
							        <span><i class="fa fa-arrow-right"></i></span>
							    </c:if>
							</c:forEach>
						</td>
					</tr>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>


<script src="${pageContext.request.contextPath }/resources/groupware/js/approval/apprLineAutoList.js"></script>
