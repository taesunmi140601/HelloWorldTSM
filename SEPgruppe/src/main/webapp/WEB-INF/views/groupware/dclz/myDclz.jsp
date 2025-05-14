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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<security:authentication property="principal.realUser" var="realUser"/>
<title>${realUser.companyNo} - 내 근태 현황</title>

<link href="${pageContext.request.contextPath }/resources/groupware/css/dclz/myDclz.css" rel="stylesheet" type="text/css">

<div class="mydclzContainer">
<div class="page-header">
    <h3 class="fw-bold mb-3">내 근태 현황</h3>
</div>

<div class="biggestContainer" style="display: flex; align-items: flex-start; gap: 20px;">
    <div class="dclz-container" style="flex: 0 0 280px;">
        <div class="dclz">
            <div class="dclz-date" id="current-date"></div>
            <div class="dclz-clock" id="current-time">--:--:--</div>
            <div class="dclz-status">
                <div class="info-row"><span class="info-label">출근시간</span><span class="info-value clock-in">${todayDclzStatus.attendDate != null ? todayDclzStatus.attendDate : '미등록'}</span></div>
                <div class="info-row"><span class="info-label">퇴근시간</span><span class="info-value clock-out">${todayDclzStatus.lvffcDate != null ? todayDclzStatus.lvffcDate : '미등록'}</span></div>
                <div class="info-row"><span class="info-label">주간 누적 근무시간</span><span class="info-value weekly-time" id="weekly-time">${weeklyTotalHours}</span></div>
            </div>
            <div class="dclz-buttons-wrap">
			  <div class="dclz-buttons">
			    <button id="start" class="btn btn-outline-primary">출근하기</button>
			    <button id="end" class="btn btn-outline-primary">퇴근하기</button>
			  </div>
			  <button class="btn btn-outline-secondary status-btn">근무상태변경</button>
			</div>
        </div>
    </div>
	 <div class="dclz-right-wrap">
    <div class="dclz-summary-panel">
        <div class="summary-box">
            <div class="summary-section">
                <div class="summary-label">이번주 누적</div>
                <div class="summary-value text-primary">${weeklyTotalHours}</div>
            </div>
            <div class="summary-section">
                <div class="summary-label">이번달 잔여</div>
                <div class="summary-value text-muted">${monthlyRemainTime != null ? monthlyRemainTime : '0시간 0분'}</div>
            </div>
            <div class="summary-section">
                <div class="summary-label text-muted">이번달 누적</div>
                <div class="summary-value text-muted">${monthlyTotalHours != null ? monthlyTotalHours : '0시간 0분'}</div>
            </div>
            <div class="summary-section">
                <div class="summary-label text-muted">이번달 초과</div>
                <div class="summary-value text-muted over-time">0시간 0분</div>
            </div>
        </div>
    </div>


<div class="dclz-right-panel">
    <div class="accordion" id="workAccordion" data-current-week="${currentWeekIndex}">
        <c:forEach var="weekEntry" items="${weeklyMap}" varStatus="status">
            <c:set var="weekNumber" value="${weekEntry.key}" />
            <c:set var="statusList" value="${weekEntry.value}" />
            <c:set var="weeklyTotalHours" value="${statusList[0].weeklyTotalHours}" />
            <div class="accordion-item">
                <h2 class="accordion-header">
                    <button class="accordion-button fw-bold ${status.index + 1 == currentWeekIndex ? '' : 'collapsed'}" type="button"
                            data-bs-toggle="collapse" data-bs-target="#week${weekNumber}" ${status.index + 1 == currentWeekIndex ? 'aria-expanded="true"' : 'aria-expanded="false"'}>
                        ${weekNumber} 주차 
                        <span class="text-end">
                        <c:if test="${not empty weeklyTotalHours}">
                        	 누적 근무시간: ${weeklyTotalHours}
                        </c:if>
                        <c:if test="${empty weeklyTotalHours}">
                        	 누적 근무시간: -시간 --분
                        </c:if>
                        </span>
                    </button>
                </h2>
                <div id="week${weekNumber}" class="accordion-collapse collapse ${status.index + 1 == currentWeekIndex ? 'show' : ''}">
                    <div class="accordion-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>일자</th>
                                    <th>업무 시작</th>
                                    <th>업무 종료</th>
                                    <th>근무시간</th>
                                    <th>연장 근무 시간</th>
                                    <th>근무 상태</th>
                                </tr>
                            </thead>
                            <tbody id="week-${weekNumber}-tbody">
                                <c:forEach var="status" items="${statusList}">
                                    <tr>
                                        <td>${status.workingDay}</td>
                                        <td>${status.attendDate}</td>
                                        <td>${status.lvffcDate}</td>
                                        <td>${status.workingTime}</td>
                                        <td>${status.extndWorkingHours}</td>
                                        <td>${status.workStatus}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
</div>
</div>
</div>

	<script type="text/javascript">
	  window.contextPath = "${pageContext.request.contextPath}";
	  window.companyNo = "${companyNo}";
	  window.empId = "${realUser.empId}";
	</script>
	<script src="${pageContext.request.contextPath }/resources/groupware/js/dclz/myDclz.js"></script>
	<script src="${pageContext.request.contextPath }/resources/groupware/js/common.js"></script>
	<script src="${pageContext.request.contextPath }/resources/groupware/js/dclz/Chart.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
	<script>
	var weeklyWorkData = [];
	<c:forEach var="weekEntry" items="${weeklyMap}">
	    var weekNumber = "${weekEntry.key}";
	    var rawTime = "${weekEntry.value[0].weeklyTotalHours}";

	    // 시간 + 분 정규식 파싱 (숫자만 뽑음)
	    var matches = rawTime.match(/\d+/g);
	    var hour = matches && matches.length > 0 ? parseInt(matches[0], 10) : 0;
	    var min = matches && matches.length > 1 ? parseInt(matches[1], 10) : 0;
	    var floatHour = parseFloat((hour + min / 60).toFixed(2));

	    weeklyWorkData.push({
	        week: weekNumber,
	        hours: floatHour
	    });
	</c:forEach>
	</script>
