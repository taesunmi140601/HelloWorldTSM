<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 22.     	JYS            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h4 class="head-section">새 양식 등록</h4>

<table class="table">
	<tr>
		<th>폴더명</th>
		
		<td><input id="folder" value="${docFolderName}" data-doc-folder-no="${docFolderNo}" readonly></td>
		
		<td>&nbsp;</td>
	</tr>
	<tr>
		<th>양식명</th>
		<td>
			<input type="text" id="docName">
		</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<th colspan="3">
			양식 내용
		</th>
	<tr>
	<tr>
		<td colspan="3">
			<textarea id="editor" name="content"></textarea>
		</td>
	</tr>
	
</table>
		
<br>
<button class="btn btn-outline-primary" onclick="insertDoc()">등록</button>
<button class="btn btn-outline-danger" onclick="history.back()" >취소</button>
 <script defer="defer">
document.addEventListener("DOMContentLoaded", () => {
	CKEDITOR.replace('editor', {
		versionCheck: false,
        height: '100%', // 원하는 높이(px)
        width: '100%' // 가로 크기(퍼센트 또는 px)
    });
});
</script> 
<script src="${pageContext.request.contextPath }/resources/groupware/js/approval/apprForm_ft.js"></script>
<script src="${pageContext.request.contextPath }/resources/groupware/js/approval/docFormForm.js"></script>