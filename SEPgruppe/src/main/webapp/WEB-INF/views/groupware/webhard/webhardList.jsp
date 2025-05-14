<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 14.     	JYS            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.*, java.util.*, javax.servlet.*, javax.servlet.http.*, org.apache.commons.fileupload.*, org.apache.commons.fileupload.disk.*, org.apache.commons.fileupload.servlet.*, com.google.api.client.auth.oauth2.Credential, com.google.api.client.googleapis.auth.oauth2.GoogleCredential, com.google.api.client.googleapis.javanet.GoogleNetHttpTransport, com.google.api.client.http.InputStreamContent, com.google.api.client.http.javanet.NetHttpTransport, com.google.api.services.drive.Drive, com.google.api.services.drive.DriveScopes, com.google.api.services.drive.model.File" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<security:authentication property="principal.realUser" var="realUser"/>

	<link href="${pageContext.request.contextPath }/resources/groupware/css/webhard/webhard.css" rel="stylesheet">

<div class="webhardContainer">
<!-- 상단 바 -->
<div class="page-header">
    <!-- 상단 메뉴 -->
    <h3 class="fw-bold mb-3">웹하드</h3>
    <ul class="breadcrumbs mb-3">
        <li class="nav-home">
            <a href="<c:url value='/${realUser.companyNo }/groupware'/>">
                <i class="icon-home"></i>
            </a>
        </li>
        <li class="separator">
            <i class="icon-arrow-right"></i>
        </li>
        <li class="nav-item">
            <a href="<c:url value='/${realUser.companyNo }/webhard' />">웹하드</a>
        </li>
    </ul>
</div>
<header class="flex items-center justify-between p-4 bg-blue-900 shadow-lg">

   <div>
		<ul class="nav nav-pills nav-secondary nav-pills-no-bd nav-pills-icons justify-content-center" id="pills-tab-with-icon" role="tablist">
			<li class="nav-item">
				<a class="nav-link active" id="pills-home-tab-icon" data-bs-toggle="pill" href="#pills-home-icon" role="tab" aria-controls="pills-home-icon" aria-selected="true">
					<i class="far fa-file"></i>
					파일추가
				</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" id="pills-profile-tab-icon" data-bs-toggle="pill" href="#pills-profile-icon" role="tab" aria-controls="pills-profile-icon" aria-selected="false">
					<i class="fas fa-trash"></i>
					파일삭제
				</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" id="downloadSelectedFiles" data-bs-toggle="pill" href="#pills-download-icon" role="tab" aria-controls="pills-download-icon" aria-selected="false">
					<i class="fas fa-download"></i>
					다운로드
				</a>
			</li>
		</ul>
<!-- 		<div class="view-toggle"> -->

<!-- 		    <button id="toggleView"> -->
<!-- 		        <span id="listIcon"><i class="fas fa-align-justify"></i></span> -->
<!-- 		        <span id="gridIcon"><i class="fas fa-th-large"></i></span> -->
<!-- 		    </button> -->
<!-- 		</div> -->

   </div>

	<!-- 파일 선택 input (숨김 처리) -->
	<input type="file" id="fileInput" style="display: none;">
</header>

<!-- 메인 컨텐츠 -->
<main class="p-6">
    <div class="bg-white text-black rounded-lg shadow-lg p-4">        
        <div class="card">
            <div class="card-body">
                <table class="table table-hover" id="datatablesSimple">
                    <thead>
                        <tr>
                            <th scope="col">선택</th>
<!--                             <th scope="col"><i class="far fa-folder-open"></th> -->
                            <th scope="col">파일명</th>
                            <th scope="col">생성날짜</th>
                            <th scope="col">파일유형</th>
                            <th scope="col">파일크기</th>
                            <th scope="col">링크</th>
                        </tr>
                    </thead>
                    <tbody id="resultBody">
                       <c:forEach var="file" items="${files}">
						<tr data-file-id="${file.id}">                        
                           	<td><input type="checkbox" name="deleteFile" value="${file.id}"></td>
<!--                                <td class="nav-trigger"><i class="fas fa-align-justify"></td> -->
<!-- 							<td class="downloadClick"><i class="fas fa-download"></i></td> -->
                               <td>${file.name}</td>
                               <fmt:parseDate value="${file.createdTime}" pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX" var="parsedDate"/>
							<td><fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd"/></td>
<%--                                 <td>${file.createdTime}</td> --%>
                               <td><span class="badge badge-black">${file.fileExtension}</span></td>
					        <c:choose>
					            <c:when test="${file.size < 1024}">
					                <td>${file.size} B</td>
					            </c:when>
					            <c:when test="${file.size < 1048576}">
					                <c:set var="sizeKB" value="${file.size / 1024}" />
					                <td><fmt:formatNumber value="${sizeKB}" pattern="#,##0.0"/> KB</td>
					            </c:when>
					            <c:when test="${file.size < 1073741824}">
					                <c:set var="sizeMB" value="${file.size / 1048576}" />
					                <td><fmt:formatNumber value="${sizeMB}" pattern="#,##0.0"/> MB</td>
					            </c:when>
					            <c:otherwise>
					                <c:set var="sizeGB" value="${file.size / 1073741824}" />
					                <td><fmt:formatNumber value="${sizeGB}" pattern="#,##0.0"/> GB</td>
					            </c:otherwise>
					        </c:choose>
<%--                                 <td>${file.size}</td> --%>
                            <td><a href="${file.webViewLink}" target="_blank">View</a></td>
                        </tr>
                      </c:forEach>
                   </tbody>
               </table>
           </div>
       </div>   
       
<c:if test="${not empty previousPageToken}">
    <a href="?pageToken=${previousPageToken}">이전 페이지</a>
</c:if>       
        
<c:if test="${not empty nextPageToken}">
    <a href="?pageToken=${nextPageToken}">다음 페이지</a>
</c:if>

<c:if test="${not empty param.pageToken}">
    <c:set var="pageToken" value="${param.pageToken}" />
</c:if>

            
    </div>
</main>
</div>

<!-- <div class="menu-container" id="myNavbar" style="display: none;"> -->
<!--     <ul> -->
<!--         <li>상세조회</li> -->
<!--         <li class="downloadClick">다운로드</li>  -->
<!--         <li>공유</li> -->
<!--         <li>수정</li> -->
<!--         <li>삭제</li> -->
<!--     </ul> -->
<!-- </div> -->

	<script src="${pageContext.request.contextPath }/resources/groupware/js/webhard/webhard.js"></script>


<script>
var contextPath = "${pageContext.request.contextPath}";
var companyNo = "${realUser.companyNo}";
var empId = "${realUser.empId}";
var fileId = "${file.id}";
</script>