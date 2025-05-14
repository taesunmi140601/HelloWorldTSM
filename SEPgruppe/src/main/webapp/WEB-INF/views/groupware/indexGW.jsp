<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/groupware/css/indexGW.css" />
<security:authentication property="principal.realUser" var="realUser"/> <!-- Provider 시큐리티 정보 -->
<title>${realUser.companyNo } - 메인페이지</title>

<!-- 위젯 전체 레이아웃 -->
<div id="widgetContainer">
  <div id="leftColumn" class="column">
  <c:if test="${empty leftWidgets}">
    <div class="placeholder">위젯을 여기에 드래그하세요</div>
  </c:if>
    <c:forEach var="widget" items="${leftWidgets}">
      <div class="widget-box" data-widget-id="${widget.widgetId}" id="${widget.widgetId}-widget">
        <c:choose>
          <c:when test="${widget.widgetId eq 'dclz'}">
            <c:import url="/${companyNo}/dclz/main-panel" />
          </c:when>
          <c:when test="${widget.widgetId eq 'schedule'}">
            <c:import url="/${companyNo}/widget/schedule" />
          </c:when>
          <c:when test="${widget.widgetId eq 'notice'}">
            <c:import url="/${companyNo}/widget/notice" />
          </c:when>
          <c:when test="${widget.widgetId eq 'approval-waiting'}">
            <c:import url="/${companyNo}/widget/approval-waiting" />
          </c:when>
          <c:when test="${widget.widgetId eq 'project-task'}">
		  	<c:import url="/${companyNo}/widget/project-task" />
		  </c:when>
        </c:choose>
      </div>
    </c:forEach>
  </div>

  <div id="rightColumn" class="column">
    <c:forEach var="widget" items="${rightWidgets}">
    	<c:if test="${empty rightWidgets}">
	    	<div class="placeholder">위젯을 여기에 드래그하세요</div>
	  	</c:if>
      <div class="widget-box" data-widget-id="${widget.widgetId}" id="${widget.widgetId}-widget">
        <c:choose>
          <c:when test="${widget.widgetId eq 'dclz'}">
            <c:import url="/${companyNo}/dclz/main-panel" />
          </c:when>
          <c:when test="${widget.widgetId eq 'schedule'}">
            <c:import url="/${companyNo}/widget/schedule" />
          </c:when>
          <c:when test="${widget.widgetId eq 'notice'}">
            <c:import url="/${companyNo}/widget/notice" />
          </c:when>
          <c:when test="${widget.widgetId eq 'approval-waiting'}">
            <c:import url="/${companyNo}/widget/approval-waiting" />
          </c:when>
          <c:when test="${widget.widgetId eq 'project-task'}">
		  	<c:import url="/${companyNo}/widget/project-task" />
		  </c:when>
        </c:choose>
      </div>
    </c:forEach>
  </div>
</div>
<lottie-player
    id="background-lottie"
    src="${pageContext.request.contextPath}/resources/groupware/images/Animation - 1744694190711.json"
    background="transparent"
    speed="1"
    loop
    autoplay
    style="position: fixed; bottom: 0; left: 85%; transform: translateX(-50%); width: 300px; height: 300px; z-index: -1;">
</lottie-player>    

<script>
	const contextPath = "${pageContext.request.contextPath}";
	const companyNo = "${companyNo}";
	const empId = "${realUser.empId}";
	
	
	new Sortable(document.getElementById('leftColumn'), {
		  group: 'widgets', // 그룹 이름 동일하게 설정
		  animation: 150,
		  ghostClass: 'sortable-ghost',
		  draggable: '.widget-box',
		  onEnd: saveWidgetOrder
		});

		new Sortable(document.getElementById('rightColumn'), {
		  group: 'widgets', // 같은 그룹이면 cross-column 이동 가능
		  animation: 150,
		  ghostClass: 'sortable-ghost',
		  draggable: '.widget-box',
		  onEnd: saveWidgetOrder
		});
		
		function saveWidgetOrder() {
			const columns = ['leftColumn', 'rightColumn'];
			const widgetData = [];

			columns.forEach(columnId => {
				const column = document.getElementById(columnId);
				const widgets = column.querySelectorAll('.widget-box');

				widgets.forEach((widget, index) => {
					const widgetId = widget.dataset.widgetId;

					widgetData.push({
						empId: empId,
						widgetId: widgetId,
						positionNo: index,
						columnName: columnId
					});
				});
			});

			// 서버로 저장 요청
			fetch(contextPath + `/${companyNo}/widget/save`, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(widgetData)
			})
			.then(response => response.json())
			.then(result => {
				if (result.success) {
					Swal.fire({
						toast: true,
						position: 'top',
						icon: 'success',
						title: '위젯 위치 저장 완료!',
						showConfirmButton: false,
						timer: 1500,
					});
				}
			})
			.catch(error => {
				Swal.fire({
					toast: true,
					position: 'top',
					icon: 'error',
					title: '위젯 저장 실패',
					showConfirmButton: false,
					timer: 1500,
				});
			});
		}
</script>

<script src="${pageContext.request.contextPath }/resources/groupware/js/indexGW.js"></script>
<script src='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.15/index.global.min.js'></script>
<script src="https://cdn.jsdelivr.net/npm/@fullcalendar/interaction@6.1.15/index.global.min.js"></script>
<script src="https://unpkg.com/lottie-web@latest/build/player/lottie.min.js"></script>
<script src="https://unpkg.com/@lottiefiles/lottie-player@latest/dist/lottie-player.js"></script>