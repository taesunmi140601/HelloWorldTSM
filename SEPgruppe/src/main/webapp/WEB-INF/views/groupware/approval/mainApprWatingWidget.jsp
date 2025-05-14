<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 4. 14.     	JSW            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="card-body">
  <h4 class="card-title">결재 대기 문서</h4>
  <table class="table table-sm table-hover">
    <thead>
      <tr>
        <th>기안일</th>
        <th>양식</th>
        <th>긴급</th>
        <th>제목</th>
        <th>기안자</th>
        <th>상태</th>
      </tr>
    </thead>
    <tbody>
      <c:choose>
        <c:when test="${empty waitingDocs}">
          <tr><td colspan="6" class="text-center">결재할 문서가 없습니다.</td></tr>
        </c:when>
        <c:otherwise>
          <c:forEach var="doc" items="${waitingDocs}">
            <tr class="waitingDocRow" data-doc-no="${doc.aprvlDocNo}" data-doc-type="${doc.docFrmName}" style="cursor: pointer;">
              <td>${fn:substring(doc.submitDate, 0, 10)}</td>
              <td>${doc.docFrmName}</td>
              <td>${doc.isEmergency}</td>
              <td>${doc.aprvlDocTitle}</td>
              <td>${doc.drafterName}</td>
              <td><span class="badge" style="background-color: ${doc.statusColor}; color: white;">${doc.statusName}</span></td>
            </tr>
          </c:forEach>
        </c:otherwise>
      </c:choose>
    </tbody>
  </table>
</div>
<script>
document.addEventListener("DOMContentLoaded", function () {
document.querySelectorAll(".waitingDocRow").forEach(row => {
	  row.addEventListener("click", () => {
	  	const docNo = row.getAttribute("data-doc-no");
	    const docType = row.getAttribute("data-doc-type");
	    const encodedDocType = encodeURIComponent(docType);
	    const contextPath = "${pageContext.request.contextPath}";
	    
	    location.href = contextPath + `/${companyNo}/apprHome/aprvlDocDetail/` + docNo + "?docType=" + encodedDocType;
	  });
	});
});
</script>