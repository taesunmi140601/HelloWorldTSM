<!-- 
 * 관리자 - 전자결재 > 결재양식 (목록 조회)
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 21.     	JYS            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<h4>결재양식설정</h4>
<span>결재 양식 선택</span>
<div class="input-group">
    <input type="text" id="docSearch" class="form-control" placeholder="양식 제목을 입력하세요.">
    <button type="submit" class="btn btn-outline-primary">🔍</button>
</div>
<br><br>
<div class="row">
	<div class="col-md-4" >
	   <h6>결재 폴더 목록</h6><br>
	   	<button id="addFolderBtn" class="btn btn-outline-primary" onclick="location.href=''">폴더 추가</button>
		<button id="renameFolderBtn" class="btn btn-outline-danger" >수정</button>
		<button id="deleteFolderBtn" class="btn btn-outline-danger" >삭제</button>
		<div id= "tree">
	   		<!--ajax 응답 -->
	    </div>
	</div>
	<div class="col-md-6">
		<button class="btn btn-outline-primary" id="newDocBtn">새 양식 등록</button>
		<table class="table">
			<thead>
				<th>제목</th>
				<th>사용여부</th>
			</thead>
			<tbody id="docTableTbody" class="table-group-divider">
			
			</tbody>
		</table>
	</div>
	
</div>

<!-- 문서 상세 모달 -->
<div class="modal fade" id="docDetailModal" tabindex="-1" role="dialog" aria-labelledby="apprInfoModalLabel" aria-modal="true">
  <div class="modal-dialog modal-xl modal-dialog-centered modal-dialog-scrollable">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="apprInfoModalLabel">상세정보</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
      	<div class="container-fluid">
      		<div>
      			<table class="table">
      			  <tr>
      			  	<td colspan="2" hidden="" data-docFrmNo=""></td> 
      			  </tr>
                  <tr>
                      <td>제목</td>
                      <td><input type="text" id="docFrmName" class="form-control" /></td>
                  </tr>
                  <tr>
                      <td>양식 편집</td>
                      <td><textarea id="editor" class="form-control"></textarea></td>
                  </tr>
                  <tr>
                      <td>사용여부</td>
                      <td><input type="checkbox" id="isEnabled" /> 사용</td>
                  </tr>
              </table>
      		</div>
    	</div>
      <div class="modal-footer">
        <button type="button"  class="btn btn-secondary" data-bs-dismiss="modal"  id="docDetailCloseBtn">닫기</button>
        <button type="button" class="btn btn-primary" id="docDetailUpdateBtn">저장</button>
      </div>
    </div>
  </div>
</div>


<script src="${pageContext.request.contextPath }/resources/groupware/js/approval/apprForm_ft.js"></script>
<script src="${pageContext.request.contextPath }/resources/groupware/js/approval/docFormForm.js"></script>
