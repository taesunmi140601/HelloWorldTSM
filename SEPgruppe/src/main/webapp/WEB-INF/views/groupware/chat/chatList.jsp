
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>채팅방 목록</title>
</head>
<body>
<div class="chat-list-container">
    <div class="chat-list-header">참여중인 채팅방</div>
    <security:authentication property="principal.realUser" var="realUser"/>
    <ul class="chat-room-list">
        <c:forEach items="${rooms}" var="room">
            <li class="chat-room-row" data-roomid="${room.roomId}">
                <div class="chat-room-avatar">
                    <img src="/sep/resources/groupware/images/profile.png" alt="Avatar">
                </div>
                <div class="chat-room-info">
                    <c:set var="roomNameStripped">
					    ${fn:replace(fn:replace(room.chatRoom.roomName, realUser.empNm, ''), ',', '')}
					</c:set>
					<div class="chat-room-name" data-roomname="${roomNameStripped}">
					    ${roomNameStripped}
					</div>
                    <div class="chat-last-message">${room.chatRoom.message.msgContent}</div>
                </div>
                <div class="chat-last-time"
				     data-senddate="${fn:substring(room.chatRoom.message.sendDate, 0, 10)}"
				     data-sendtime="${room.chatRoom.message.sendTime}">
				     계산 중...
				</div>
            </li>
        </c:forEach>
    </ul>
</div>
</body>
</html>
