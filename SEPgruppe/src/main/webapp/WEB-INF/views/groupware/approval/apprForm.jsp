<!-- 
 * 새 결재 문서 작성
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 25.     	JYS            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/groupware/css/approval/apprForm.css" >

<div>
	<button type="button" class="btn" onclick="draftDoc()">
		<i class="fa fa-paper-plane"></i>결재요청
	</button>
	<button type="button" class="btn" data-bs-dismiss="modal" onclick="">
		<i class="fa fa-save"></i>임시저장
	</button>
	<button type="button" class="btn" data-bs-dismiss="modal" onclick="previewContent()">
		<i class="fa fa-eye"></i>미리보기
	</button>
	<button type="button" class="btn" data-bs-dismiss="modal" onclick="history.back()">
		<i class="fa fa-times-circle"></i>취소
	</button>
	<button type="button" class="btn" data-bs-toggle="modal" data-bs-target="#apprInfoModal">
		<i class="fa fa-file-alt"></i>결재정보
	</button>
</div>
<hr />
<div class="container-fluid">
	<div class="row" >
         	 <!-- 결재양식 선택 (좌측)-->
		<div id="tree-conotainer" class="col-md-3" >
			<span>결재 양식 선택</span>
			<form id="searchDoc">
				<div class="input-group">
				    <input type="text" id="docSearch" class="form-control" placeholder="양식 제목">
				    <button type="submit" class="btn btn-outline-primary">🔍</button>
				</div>
			</form>
				<div id= "tree">
	    			<!--ajax 응답 -->
	    		</div>
        </div>
        <!-- 결재양식 상세보기(우측) -->
        <div class="col" id="draftDocDiv">
        	<table class="clearfix">
        		<tr id="docFrmNameTr">
        			<td colspan="2" id="docFrmNameTd">결재 양식 이름</td>
        		</tr>
	        	<tr>
	        		<td>
		        		 <div id="aprvlDocInfoDiv">
						    <table id="aprvlDocInfoTable">
						      <tbody>
						        <tr>
						          <th>문서번호</th>
						          <td>문서번호</td>
						        </tr>
						        <tr>
						          <th>부 서</th>
						          <td>기안부서</td>
						        </tr>
						        <tr>
						          <th>시행일자</th>
						          <td>&nbsp;</td>
						        </tr>
						      </tbody>
						    </table>
						</div>
					</td>
					<td>
						
					</td>
					<td >
						<div id="checkConDiv">
							<table>
								<tr>
				        			<th>마감 일자</th>
				        			<td></td>	
				        		</tr>
				        		<tr>
				        			<th>긴급</th>
				        			<td></td>
				        		</tr>
							</table>
						</div>
						<div id="aprvlLineDiv">
						    <security:authentication property="principal.realUser" var="realUser"/>
						    <table>
						      <tr>
						        <th rowspan="3">신청</th>
						        <th><span id="positionName"></span></th>
						      </tr>
						      <tr>
						        <td><span id="empNm"></span></td>
						      </tr>
						      <tr>
						        <td><span id="today"></span></td>
						      </tr>
						    </table>
						
						    <table>
						      <tbody id="apprLineBody">
						        <tr>
						          <th rowspan="3">승인</th>
						          <th></th>
						        </tr>
						        <tr>
						          <td></td>
						        </tr>
						        <tr>
						          <td></td>
						        </tr>
						      </tbody>
						    </table>
						</div>
					</td>
	        	</tr>
        	</table>
        	
        	
        	
         	<div id="templateDetails">
         		<h3>결재 양식을 선택하세요</h3>
         		<!-- <textarea class="form-control" id="editor" name ="content" >양식을 선택하면 상세 정보가 표시됩니다.</textarea> -->
         	</div>
       	</div>
       	
    </div>
    <div>
    	<div id ="apprLineDiv" class="row">
       		<span>결재선</span>
       		<table class="table">
       			<thead>
       				<tr>
	       				<td>프로필</td>
	       				<td>이름</td>
	       				<td>직급</td>
	       				<td>부서</td>
	       				<td>기안/결재/참조/열람</td>
       				</tr>
       			</thead>
       			<tbody id="apprLineTbody">
       			
       			</tbody>
       		</table>
       	</div>
    </div>
 </div>

<!-- 결재 정보 모달 -->
<div class="modal fade" id="apprInfoModal" tabindex="-1" role="dialog" aria-labelledby="apprInfoModalLabel" aria-modal="true">
  <div class="modal-dialog modal-xl modal-dialog-centered modal-dialog-scrollable">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="apprInfoModalLabel">결재정보</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
      	<div class="container-fluid">
      		<div class="card-body">
			    <ul class="nav nav-tabs nav-line nav-color-secondary" id="line-tab" role="tablist">
			        <li class="nav-item">
			            <a class="nav-link" id="line-appr-tab" href="" role="tab" aria-controls="pills-appr" aria-selected="true">결재선</a>
			        </li>
			         <li class="nav-item">
			            <a class="nav-link" id="line-ref-tab" href="" role="tab" aria-controls="pills-ref" aria-selected="true">참조자</a>
			        </li>
			        <li class="nav-item">
			            <a class="nav-link" id="line-view-tab" href="" role="tab" aria-controls="pills-view" aria-selected="true">열람자</a>
			        </li>
			    </ul>
			</div>
    		<div class="row">
    			<div class="col-md-4" id="modal-tree-container">
    				<table>
    					<thead>
    						<tr>
    							<th>조직도</th>
    							<th>나의 결재선</th>
    						</tr>
    					</thead>
    				</table>
    				<!-- 결재선 조회 -->
			          <div id="depModalTree">
			            <!-- ajax 응답 -->
			          </div>
    			</div>
    			<div class="col-md-6">
    				<table class="table">
    					<thead>
    						<tr>
	    						<th>&nbsp;</th>
	    						<th>타입</th>
	    						<th>이름</th>
	    						<th>부서</th>
	    						<th>상태</th>
	    						<th><i class="fa fa-trash"></i></th>
    						</tr>
    					</thead>
    					<tbody id="approvalTableBody">
	    					<tr>
						      <td colspan="6">결재선</td>
						    </tr>
						 <!-- 여기에 드랍된 행들이 추가됩니다. -->
						 
						</tbody>
    				</table>
    			</div>
    		</div>
    	</div>
    	<!-- 모달 내부에서만 토스트 표시 -->
		<div id="modalToastContainer" class="position-absolute top-0 start-50 translate-middle-x p-3" style="z-index: 1050;">
		    <div id="modalDuplicateToast" class="toast align-items-center text-bg-danger border-0" role="alert" aria-live="assertive" aria-atomic="true">
		        <div class="d-flex">
		            <div class="toast-body">
		                ⚠ 이미 추가된 결재선입니다.
		            </div>
		            <button type="button" class="btn-close me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
		        </div>
		    </div>
		</div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" id="apprLineCloseBtn">닫기</button>
        <button type="button" class="btn btn-primary" id="apprLineSaveBtn">저장</button>
      </div>
    </div>
  </div>
</div>

<!-- 결재요청 결과 / 임시저장 성공 여부 알람 토스트 -->
 
<!-- 위치 조정: 오른쪽 아래에 고정 -->
<div id="apprResToast"
     class="toast align-items-center text-bg-primary border-0 position-fixed top-0 start-50 translate-middle-x mt-3"
     role="alert" aria-live="assertive" aria-atomic="true"
     data-bs-delay="2000" data-bs-autohide="true" style="z-index: 9999;">
  <div class="d-flex">
    <div class="toast-body">
      <!-- 알람 내용 -->
    </div>
    <button type="button" class="btn-close btn-close-white me-2 m-auto apprbtn"
            data-bs-dismiss="toast" aria-label="Close"></button>
  </div>
</div>

<!-- js -->
<script src="${pageContext.request.contextPath }/resources/groupware/js/approval/apprHome.js"></script>
<script src="${pageContext.request.contextPath }/resources/groupware/js/approval/apprForm_ft.js"></script>
<script src="${pageContext.request.contextPath }/resources/groupware/js/organization/depForm_ft.js"></script>


<!-- 문서양식 js -->
<script src="${pageContext.request.contextPath }/resources/groupware/js/approval/docFrmJS/vac.js"></script>

