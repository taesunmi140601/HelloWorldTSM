<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 4. 7.     	KMJ            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<link href="${pageContext.request.contextPath }/resources/groupware/css/mail/mailList.css" rel="stylesheet">

<div class="mail-container">
    <!-- 왼쪽 메뉴 -->
    <div class="mailSidebar">
        <h3>⭐ 즐겨찾기</h3>
        <button class="btn btn-primary">메일 쓰기</button>
        <a href="<c:url value='/${companyNo}/mail/favorites'/>" class="menu-item">즐겨찾기</a>
        <a href="<c:url value='/${companyNo}/mail'/>" class="menu-item">받은 메일함</a>
        <a href="<c:url value='/${companyNo}/mail/mySentMail'/>" class="menu-item">보낸 메일함</a>
        <a href="#" class="menu-item">휴지통</a>
    </div>
    <!-- 오른쪽 메일 리스트 -->
    <div class="content-area">
		<c:if test="${empty mailList}">
		    <div class="no-mail">📭 즐겨찾는 메일이 없습니다.</div>
		</c:if>
		
		<h2>즐겨찾기 메일 조회</h2>
		
		<c:forEach var="mail" items="${mailList}">
		    <div class="mail-item">
		        <span class="favorite-icon fa-${mail.mailFav == 1 ? 'solid' : 'regular'} fa-star"
                  data-mail-id="${mail.mailReceptionId}"
                  style="${mail.mailFav == 1 ? 'color: gold;' : ''}"></span>
		        <strong>${mail.mailSubject}</strong> - ${mail.fromEmail} / ${mail.mailDate}
		    </div>
		</c:forEach>
		
<%-- 		<c:if test="${not empty mailList}"> --%>
<!-- 		    <table class="table table-head-bg-primary mt-4" id="favoriteTable"> -->
<!--             <thead> -->
<!--                 <tr> -->
<!--                     <th scope="col">선택</th> -->
<!--                     <th scope="col">즐찾</th> -->
<!--                     <th scope="col">발신자</th> -->
<!--                     <th scope="col">제목</th> -->
<!--                     <th scope="col">받은 날짜</th> -->
<!--                 </tr> -->
<!--             </thead> -->
<!--             <tbody id="favoriteBody"> -->
<!--                 JavaScript로 동적 추가 -->
<!--             </tbody> -->
<!--         </table> -->
<%-- 		</c:if> --%>
	</div>	
</div>

<script>
const allMails = JSON.parse('${mailListJson}');
let companyNo = "${companyNo}";
</script>
	<script src="${pageContext.request.contextPath }/resources/groupware/js/mail/mailFavorite.js"></script>