<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<security:authentication property="principal" var="principal" />

<div class="selected-user">
    <span>To: <span class="name">${toName}</span></span>
    <div class="dropdown">
        <button class="btn btn-outline-primary dropdown-toggle" type="button" id="menuToggleBtn" data-bs-toggle="dropdown" aria-expanded="false">
            <i class="fas fa-bars"></i>
        </button>

        <!-- 드롭다운 메뉴 항목 -->
        <ul class="dropdown-menu" aria-labelledby="menuToggleBtn">
            <li style="padding-left: 10px;">
                <a class="btn btn-outline-info" href="#">
                    <i class="fas fa-arrow-circle-left"></i> 뒤로가기
                </a>
            </li>
            <li style="padding-left: 10px;">
                <a class="btn btn-outline-danger" href="#" id="leaveRoomBtn">
                    <i class="fas fa-arrow-circle-left"></i> 방 나가기
                </a>
            </li>
        </ul>
    </div>
</div>

<div class="chat-container">
    <!-- lastTime 변수를 초기화 (페이지 범위) -->
    <ul class="chat-box chatContainerScroll" id="chatBox">
        <c:set var="lastDate" value="" scope="page" />
        <c:set var="lastTime" value="" scope="page" />

        <c:forEach var="msg" items="${messageList}">
            <li class="${msg.senderEmpId eq principal.realUser.userId ? 'chat-right' : 'chat-left'}">
                <div class="chat-hour">
                    <c:set var="currentDateOnly" value="${fn:substring(msg.sendDate, 0, 10)}" />
                    <c:if test="${currentDateOnly ne lastDate}">
                        ${currentDateOnly} ${msg.sendTime}
                        <c:set var="lastDate" value="${currentDateOnly}" scope="page" />
                        <c:set var="lastTime" value="${msg.sendTime}" scope="page" />
                    </c:if>
                    <c:if test="${currentDateOnly eq lastDate and msg.sendTime ne lastTime}">
                        ${msg.sendTime}
                        <c:set var="lastTime" value="${msg.sendTime}" scope="page" />
                    </c:if>
                </div>

                <div class="chat-avatar">
                    <img src="/sep/resources/groupware/images/profile.png" alt="Avatar">
                    <div class="chat-name">
                        (${msg.organization.deptName}) ${msg.organization.empNm} ${msg.organization.positionName}
                    </div>
                </div>

                <div class="chat-text">${msg.msgContent}</div>
            </li>
        </c:forEach>
    </ul>

    <!-- 메시지 입력 영역 -->
    <div class="chat-input-wrapper">
        <textarea class="chat-input" rows="1" placeholder="메시지를 입력하세요..." id="messageInput"></textarea>
        <button id="sendBtn" type="button" class="btn btn-outline-info">
            <i class="fas fa-paper-plane"></i>
        </button>
    </div>
</div>
