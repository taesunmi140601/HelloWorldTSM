<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 4. 11.     	JSW            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/groupware/css/dclz/mainDclz-widget.css" />

  <div class="dclz-date" id="current-date"></div>
  <div class="dclz-clock" id="current-time">--:--:--</div>
  <div class="dclz-status">
    <div class="info-row">
      <span class="info-label">출근시간</span>
      <span class="info-value clock-in">${todayDclzStatus.attendDate != null ? todayDclzStatus.attendDate : '미등록'}</span>
    </div>
    <div class="info-row">
      <span class="info-label">퇴근시간</span>
      <span class="info-value clock-out">${todayDclzStatus.lvffcDate != null ? todayDclzStatus.lvffcDate : '미등록'}</span>
    </div>
    <div class="info-row">
      <span class="info-label">주간 누적 근무시간</span>
      <span class="info-value weekly-time">${weeklyTotalHours}</span>
    </div>
  </div>
  <div class="dclz-buttons-wrap">
    <div class="dclz-buttons">
      <button id="start" class="btn btn-outline-primary">출근하기</button>
      <button id="end" class="btn btn-outline-primary">퇴근하기</button>
    </div>
    <button class="btn btn-outline-secondary status-btn">근무상태변경</button>
  </div>

<script src="${pageContext.request.contextPath }/resources/groupware/js/dclz/myDclz-widget.js"></script>