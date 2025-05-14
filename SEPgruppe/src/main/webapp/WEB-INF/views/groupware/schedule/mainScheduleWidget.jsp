<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 4. 12.     	JSW            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<security:authentication property="principal.realUser" var="realUser" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/groupware/css/schedule/mainScheduleWidget.css" />

  <div id="fullcalendar" class="mb-3"></div>
  <a href="<c:url value="/${companyNo}/schedule"/>" class="btn btn-outline-primary btn-sm mt-2">상세 보기</a>

<script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.15/index.global.min.js"></script>
<script id="schedule-data" type="application/json">
  ${scheduleJson}
</script>

<script>
document.addEventListener("DOMContentLoaded", function () {
    const calendarEl = document.getElementById("fullcalendar");
    const scheduleMap = JSON.parse(document.getElementById("schedule-data").textContent);

    const calendar = new FullCalendar.Calendar(calendarEl, {
      initialView: "dayGridMonth",
      locale: "ko",
      headerToolbar: {
        left: "prev",
        center: "title",
        right: "next"
      },
      titleFormat: { year: 'numeric', month: '2-digit' }, // 2025.04
      height: "auto",
      events: scheduleMap,
      eventClick: function(info) {
        const event = info.event;
        const box = document.getElementById("schedulePreviewBox");
        box.innerHTML = `<p><strong>${event.extendedProps.schdlType}</strong> - ${event.title}</p>`;
      }
    });

    calendar.render();
  });
</script>