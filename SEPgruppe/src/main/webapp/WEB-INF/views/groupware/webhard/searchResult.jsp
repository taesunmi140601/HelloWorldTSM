<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 4. 4.     	KMJ            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:forEach var="file" items="${files}">
   <tr data-file-id="${file.id}">
      <td><input type="checkbox" name="deleteFile" value="${file.id}"></td>
      <td class="nav-trigger"><i class="fas fa-align-justify"></i></td>
      <td>${file.name}</td>
      <fmt:parseDate value="${file.createdTime}" pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX" var="parsedDate"/>
      <td><fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd"/></td>
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
      <td><a href="${file.webViewLink}" target="_blank">View</a></td>
   </tr>
</c:forEach>
