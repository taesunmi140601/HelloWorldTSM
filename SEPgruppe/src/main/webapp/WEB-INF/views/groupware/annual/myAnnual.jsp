<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 19.     	JSW            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/groupware/css/annual/myAnnual.css" />
<title>${member.companyNo} - 내 연차 내역</title>

<div class="myAnnualContainer">
<div class="page-header">
    <h3 class="fw-bold mb-3">내 연차 내역</h3>
    <ul class="breadcrumbs mb-3">
        <li class="nav-home">
            <a href="<c:url value='/${member.companyNo }/groupware'/>">
                <i class="icon-home"></i>
            </a>
        </li>
        <li class="separator">
            <i class="icon-arrow-right"></i>
        </li>
        <li class="nav-item">
            <a href="<c:url value='/${member.companyNo }/annual/myannual' />">MyAnnual</a>
        </li>
    </ul>
</div>
<h3 class="fw-bold" id="current-date"></h3>
<section>
<div class="wrap_statistics">
        <div class="wrap_ehr_data ehr_holiday_data">
            <div class="ehr_stat_data member_data">
            	<spring:eval expression="@fileInfo.attachFiles" var="attachFiles"/>
                   	<c:if test="${empty member.empImg }">
						<img src="<c:url value='${attachFiles }default/defaultImage.jpg'/>" class="avatar-img rounded">
					</c:if>
					<c:if test="${not empty member.empImg }">
						<img src="<c:url value='${attachFiles }${member.empImg}'/>" class="avatar-img rounded">
					</c:if>
                <p class="txt fw-bold">
                    ${member.empNm } ${member.positionName }
                </p>
            </div>
            <c:forEach var="annual" items="${annualList}">
            <span class="divide_bar divide_space"></span>
            <div class="ehr_stat_data month_data">
                <p class="stat_tit">발생 연차</p>
                <p class="stat_txt">
					<c:choose>
					  <c:when test="${annual.usedLeave % 1 == 0}">
					    ${annual.usedLeave.intValue()} <!-- 정수로 출력 -->
					  </c:when>
					  <c:otherwise>
					    ${annual.usedLeave} <!-- 그대로 출력 (ex. 0.5) -->
					  </c:otherwise>
					</c:choose>
				</p>
            </div>
            <span class="divide_bar"></span>
            <div class="ehr_stat_data month_data">
                <p class="stat_tit">조정 연차</p>
                <p class="stat_txt">0</p>
            </div>
            <span class="divide_bar divide_space"></span>
            <div class="ehr_stat_data">
                <p class="stat_tit">총 연차</p>
                <p class="stat_txt">
					<c:choose>
				    	<c:when test="${annual.totalLeave % 1 == 0}">
				      		${annual.totalLeave.intValue()}
				    	</c:when>
				    	<c:otherwise>
				      		${annual.totalLeave}
				    	</c:otherwise>
			  		</c:choose>
				</p>
            </div>
            <span class="divide_bar"></span>
            <div class="ehr_stat_data">
                <p class="stat_tit">사용 연차</p>
                <p class="stat_txt">
					<c:choose>
					  <c:when test="${annual.usedLeave % 1 == 0}">
					    ${annual.usedLeave.intValue()} 
					  </c:when>
					  <c:otherwise>
					    ${annual.usedLeave} 
					  </c:otherwise>
					</c:choose>
				</p>
            </div>
            <span class="divide_bar"></span>
            <div class="ehr_stat_data">
                <p class="stat_tit">잔여 연차</p>
                <p class="stat_txt">
  					<c:choose>
					  <c:when test="${annual.remainingLeave % 1 == 0}">
					    ${annual.remainingLeave.intValue()}
					  </c:when>
					  <c:otherwise>
					    ${annual.remainingLeave} 
					  </c:otherwise>
					</c:choose>              
                </p>
            </div>
            </c:forEach>
        </div>
    </div>

<p>연차 사용기간 :  
<select>
	<option>2025-03-05 ~ 2025-12-31</option> 
</select> </p>

<!-- 사용내역 섹션 -->
<div class="section-title">사용내역</div>
<table>
    <thead>
        <tr>
            <th>이름</th>
            <th>부서명</th>
            <th>추가종류</th>
            <th>연차 사용기간</th>
            <th>사용 연차</th>
            <th>내용</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="doc" items="${usedDocs}">
			<tr>
			  <td>${doc.drafterName}</td>
			  <td>${member.deptName}</td>
			  <td>연차</td>
			  <td>${doc.leaveStartDate} ~ ${doc.leaveEndDate}</td>
			  <td>${doc.usedLeave}</td>
			  <td>${doc.aprvlDocTitle}</td>
			</tr>
		</c:forEach>
		<c:if test="${empty usedDocs}">
			<tr><td colspan="6">목록이 없습니다.</td></tr>
		</c:if>
    </tbody>
</table>

<!-- 생성내역 섹션 -->
<div class="section-title">생성내역</div>
<table>
    <thead>
        <tr>
            <th>등록일</th>
            <th>사용 기간</th>
            <th>발생일수</th>
            <th>내용</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="doc" items="${draftDocs}">
			<tr>
			  <td>
			  	<fmt:parseDate value="${doc.submitDate}" pattern="yyyy-MM-dd" var="parsedDate" />
			  	<fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd" />
			  </td>
			  <td>${doc.leaveStartDate} ~ ${doc.leaveEndDate}</td>
			  <td>${doc.usedLeave}</td>
			  <td>${doc.aprvlDocTitle}</td>
			</tr>
		</c:forEach>
		<c:if test="${empty draftDocs}">
			<tr><td colspan="4">목록이 없습니다.</td></tr>
		</c:if>
    </tbody>
</table>
</section>
</div>
<script src="${pageContext.request.contextPath }/resources/groupware/js/annual/myAnnual.js" />