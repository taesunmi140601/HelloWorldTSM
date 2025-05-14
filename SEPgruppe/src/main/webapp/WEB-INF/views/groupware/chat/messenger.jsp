<!--
 * == 개정이력(Modification Information) ==
 *
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 4. 1.     	손현진            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chat App - 그룹웨어</title>
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" integrity="sha512-iBBXm8fW90+nuLcSKVBc+...your-integrity-hash..." crossorigin="anonymous" />
    <link href="${pageContext.request.contextPath}/resources/groupware/css/chat/chat.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath }/resources/fancytree/skin-win8/ui.fancytree.min.css" />
    <!-- SockJS 및 STOMP.js 라이브러리 -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.0/sockjs.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
    <!-- jQuery (필요 시) -->
    <script src="${pageContext.request.contextPath }/resources/groupware/js/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    	<link href="${pageContext.request.contextPath }/resources/groupware/css/messenger/messenger.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <!-- 페이지 헤더 -->
    <div class="page-title">
        <div class="row gutters">
            <div class="col-xl-6 col-lg-6 col-md-6 col-sm-12 col-12">
                <h5 class="title" id="myId" data-id="${empId}" data-name="${userName}">Sep Messenger</h5>
            </div>
            <div class="col-xl-6 col-lg-6 col-md-6 col-sm-12 col-12"></div>
        </div>
    </div>
    <!-- 페이지 헤더 끝 -->

    <!-- 메인 컨텐츠 영역: 왼쪽 사이드바와 우측 채팅 영역 -->
    <div class="content-wrapper">
        <div class="row">
            <!-- 우측 채팅 영역 (예: 9 컬럼) -->
            <div class="col-xl-9 col-lg-9 col-md-8 col-sm-12 col-12">
                <div class="card m-0">
                    <!-- 채팅 화면 구성 (기존 코드 유지) -->
                    <div class="row no-gutters">
                        <!-- 좌측: 사용자 목록 (채팅 참여자 등) -->
                        <div class="col-xl-4 col-lg-4 col-md-4 col-sm-3 col-3">
                            <div class="users-container">
                                <div class="chat-search-box">
                                    <div class="input-group">
                                        <input class="form-control" placeholder="Search">
                                        <div class="input-group-btn">
                                            <button type="button" class="btn btn-info">
                                                <i class="fa fa-search"></i>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                                <ul class="users">
                                    <li class="person" id="orgLink">
                                    <a href="<c:url value='/${companyNo}/chat'/>">
                                        <i class="fas fa-users"> 조직도</i>
                                    </a>
                                    </li>
                                </ul>
                                <ul class="users">
                                    <li class="person" data-chat="person1">
                                    <a href="<c:url value='/chat/list'/>" id="listLink">
                                        <i class="fas fa-comments"> 채팅</i>
                                    </a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <!-- 우측: 채팅 영역 -->
                        <div class="col-xl-8 col-lg-8 col-md-8 col-sm-9 col-9 position-relative" id="depTree">
                        <div class="speech-bubble">
						  <p><strong>안녕하세요?</strong><br>
						  SEP 길잡이 월월이입니다. 채팅에 참여해보세요. 월월!</p>
                        </div>
                        <img src="${pageContext.request.contextPath }/resources/groupware/images/dog.png"
						     alt="개"
						     class="dog">
                    </div>
                    <!-- Row end -->
                </div>
            </div>
        </div>
    </div>
    <!-- Content wrapper 끝 -->
</div>
</body>
<!-- fancytree -->
<script src="${pageContext.request.contextPath }/resources/fancytree/jquery.fancytree-all-deps.min.js"></script>
<script src="${pageContext.request.contextPath }/resources/fancytree/modules/jquery.fancytree.dnd.js"></script>
<script>
	var subscribedRooms = ${roomsJson};
</script>
<script src="${pageContext.request.contextPath }/resources/groupware/js/chat/chat.js"></script>
</html>


