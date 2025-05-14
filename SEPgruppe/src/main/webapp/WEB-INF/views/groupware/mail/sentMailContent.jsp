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

<head>
    <title>메일 보기</title>
</head>
<body>
    <a href="#" class="mail-back-btn" data-url="/${companyNo}/mail/mySentMail">← 보낸메일함으로 돌아가기</a>
    <div class="mail-view-container">
    	<div class="mail-body">
        ${sentMailContent.sentCotentLog}
        </div>
    	<br>
	    <c:if test="${not empty sentMailContent.attachment}">
	        <div class="attachments">
	            <h4>첨부파일</h4>
	            <ul>
	                <c:forEach var="att" items="${sentMailContent.attachment}">
	                    <li>
	                        <a href="${pageContext.request.contextPath}/${companyNo}/mail/attachmentDownload?mailId=${sentMailContent.mailId}&filename=${att.filename}">
	                            ${att.filename}
	                        </a>
	                    </li>
	                </c:forEach>
	            </ul>
	        </div>
	    </c:if>
     </div>
</body>