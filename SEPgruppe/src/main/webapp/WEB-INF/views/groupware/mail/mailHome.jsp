<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 28.     	KMJ            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<security:authentication property="principal.realUser" var="realUser"/>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
<link href="${pageContext.request.contextPath }/resources/groupware/css/mail/mailList.css" rel="stylesheet">

<div class="mail-container">
    <!-- 왼쪽 메뉴 -->
    <div class="mailSidebar">
        <h3>📬 메일함</h3>
        <button id="writeMailBtn" class="btn btn-primary">메일 쓰기</button>
        <a href="#" class="menu-item" data-url="/${companyNo}/mail/favorites/list">즐겨찾기</a>
	    <a href="#" class="menu-item" data-url="/${companyNo}/mail/list">받은 메일함</a>
	    <a href="#" class="menu-item" data-url="/${companyNo}/mail/mySentMail">보낸 메일함</a>
	    <a href="#" class="menu-item" data-url="/${companyNo}/mail/trash">휴지통</a>
    </div>
    <!-- 오른쪽 메일 리스트 -->
    <div class="content-area">
		<c:if test="${empty mailList}">
		    <div class="no-mail">📭 받은 메일이 없습니다.</div>
		</c:if>
		
		<c:if test="${not empty mailList}">
		    <table class="table table-head-bg-primary mt-4" id="customDatatable">
		        <thead>
		            <tr>
		                <th scope="col">선택</th>
		                <th scope="col">즐찾</th>
                        <th scope="col">발신자</th>
                        <th scope="col">제목</th>
                        <th scope="col">받은 날짜</th>
		            </tr>
		        </thead>
		        <tbody>
		            <c:forEach var="mail" items="${mailList}">
		                <tr>
		                	<td><input type="checkbox" name="deleteMail"></td>
		                	<td><i class="fa-regular fa-star favorite-icon" data-mail-id="${mail.mailId}" data-fav="${mail.mailFav}" style="cursor: pointer;"></i>
							</td>
		                    <td class="mail-from">${mail.fromEmail}</td>
		                    <td class="mail-subject">
							  <span class="mail-link" data-mail-id="${mail.mailId}" style="cursor:pointer; color:#2980b9; text-decoration:underline;">
							    ${mail.mailSubject}
							  </span>
							</td>
		                    <td class="mail-date">
								<fmt:formatDate value="${mail.mailDate}" pattern="yyyy-MM-dd HH:mm:ss" />
							</td>
<%-- 							<td class="mail-date">${mail.formattedDate}</td> --%>
		                </tr>
		            </c:forEach>
		        </tbody>
		    </table>
		</c:if>
	</div>	
</div>

	<script src="${pageContext.request.contextPath }/resources/groupware/js/mail/mailList.js"></script>

<script>
$(function () {
	  $('#customDatatable').DataTable({
	    language: {
	      search: "검색:",
	      lengthMenu: "_MENU_개씩 보기",
	      info: "_TOTAL_건 중 _START_부터 _END_까지 표시 중",
	      infoEmpty: "메일 없음",
	      zeroRecords: "검색 결과가 없습니다.",
	      paginate: {
	        next: "다음",
	        previous: "이전"
	      }
	    }
	  });
	});
</script>
<script>
var contextPath = "${pageContext.request.contextPath}";
var companyNo = "${realUser.companyNo}";
var empId = "${realUser.empId}";
</script>
