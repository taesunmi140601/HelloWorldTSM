<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 26.     	JYS            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>문서 미리보기</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath }/resources/groupware/css/approval/apprForm.css" >
</head>
<body>
  <div class="doc-container" id="previewContentArea">
    <!-- 여기에 localStorage 값이 들어감 -->
  </div>

  <script>
    document.addEventListener("DOMContentLoaded", () => {
      const content = localStorage.getItem("ckeditor_content");
      const previewArea = document.querySelector("#previewContentArea");

      if (!content) {
        previewArea.innerHTML = "<p>미리보기 데이터가 없습니다.</p>";
        return;
      }

      previewArea.innerHTML = content;

      // 필요하다면 로컬스토리지 제거
      // localStorage.removeItem("ckeditor_content");
    });
  </script>
</body>
</html>
