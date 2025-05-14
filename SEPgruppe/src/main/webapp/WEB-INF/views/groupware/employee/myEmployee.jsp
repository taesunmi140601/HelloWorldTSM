<!--
 * == 개정이력(Modification Information) ==
 *
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 13.     	JYS            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<link
	href="${pageContext.request.contextPath }/resources/groupware/css/employee/myEmployee.css"
	rel="stylesheet" />
<div class="myEmployeeContainer">
	<!-- <div class="myEmployeeContainer"> -->
	<security:authentication property="principal.realUser" var="realUser" />
	<c:set var="member" value="${realUser}" />
	<c:set var="todayDclzStatus" value="${sessionScope.todayDclzStatus }" />
	<div class="page-header">
		<h3 class="fw-bold mb-3">내 인사 정보</h3>
		<ul class="breadcrumbs mb-3">
			<li class="nav-home"><a
				href="<c:url value='/${member.companyNo }/groupware'/>"> <i
					class="icon-home"></i>
			</a></li>
			<li class="separator"><i class="icon-arrow-right"></i></li>
			<li class="nav-item"><a
				href="<c:url value='/${member.companyNo }/employee/myEmployee' />">MyEmployee</a>
			</li>
		</ul>
	</div>
	<div class="ehr_basic_info">
		<div>
			<table class="type_list_box">
				<tbody>
					<tr>
						<td class="profile_image" rowspan="4">
							<spring:eval expression="@fileInfo.attachFiles" var="attachFiles" /> 
							<c:if test="${empty member.empImg }">
								<spring:eval expression="@fileInfo.attachFiles" var="attachFiles" /> 
								<img src="<c:url value='${attachFiles }default/defaultImage.jpg'/>" class="avatar-img rounded">
							</c:if> 
							<c:if test="${not empty member.empImg }">
								<spring:eval expression="@fileInfo.attachFiles" var="attachFiles" /> 
								<img src="<c:url value='${attachFiles }${member.empImg}'/>" class="avatar-img rounded">
							</c:if>
						</td>
						<th class="name">이름</th>
						<th class="team">소속</th>
						<td class="last" colspan="3">${member.companyNo }</td>
					</tr>
					<tr>
						<td class="name_txt" rowspan="3">
							<p class="txt fw-bold">${member.empNm }</p>
							<p class="eng"></p>
						</td>
						<th class="number">사번</th>
						<td class="number_txt">${member.empNo }</td>
						<th class="innum">내선번호</th>
						<td class="innum_txt last"></td>

					</tr>
					<tr>
						<th class="email">이메일</th>
						<td class="email_txt">${member.empEmail }</td>
						<th class="mobile">휴대전화</th>
						<td class="mobile_txt last">${member.empPhone}</td>
					</tr>
					<tr>
						<th class="rank">직위 / 직책</th>
						<td class="rank_txt">${member.positionName}/</td>
						<th class="phone">대표전화</th>
						<td class="phone_txt last"></td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="col-xl-20">
			<div class="card">
				<div class="card-body pt-3">
					<!-- 변경 탭 -->
					<ul class="nav justify-content-start nav-tabs nav-tabs-bordered"
						id="mypageBtn">
						<li class="nav-item">
							<button class="nav-link active" data-bs-toggle="tab"
								data-bs-target="#profile1">기본</button>
						</li>
						<li class="nav-item">
							<button class="nav-link" data-bs-toggle="tab"
								data-bs-target="#profile2">신상</button>
						</li>
					</ul>
					<!-- 기본 -->
					<div class="tab-content pt-2">
						<div class="tab-pane fade show active profile1" id="profile1">
							<table class="type_list_box">
								<tbody>
									<tr>
										<th class="regdate">입사일</th>
										<td class="regdate_txt last">${member.empRegdate }</td>
										<th class="number">사번</th>
										<td class="number_txt last">${member.empNo }</td>
									</tr>
									<tr>
										<th class="mobile">휴대전화</th>
										<td class="mobile_txt last">${member.empPhone}</td>
										<th class="gender">성별</th>
										<td class="gender_txt last">${member.empGender }</td>
									</tr>
									<tr>
										<th class="address">직원구분</th>
										<td class="address_txt">${member.positionName}</td>
										<th class="retire">상태</th>
										<td class="retire_txt last"><c:if
												test="${empty member.empRetire }">
											재직
										</c:if> <c:if test="${not empty member.empRetire }">
											퇴사
										</c:if></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<!-- 신상 -->
					<div class="tab-pane fade profile2 pt-3" id="profile2">
						<table class="type_list_box">
							<tbody>
								<tr>
									<th class="address">주소</th>
									<td class="adress_txt last" colspan="3">${member.empAdd1 }
										${member.empAdd2 }</td>
								</tr>
								<tr>
									<th class="bank">은행명</th>
									<td class="bank_txt last">${member.empBank}</td>
									<th class="depositor">예금주</th>
									<td class="depositor_txt last">${member.empDepositor }</td>
								</tr>
								<tr>
									<th class="bankno">계좌번호</th>
									<td class="bankno_txt last" colspan="3">${member.empBankno}</td>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>	