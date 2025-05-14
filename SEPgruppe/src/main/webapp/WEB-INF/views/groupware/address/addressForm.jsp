<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 31.     	SJH            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <title>주소록 등록</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/groupware/css/addressbook/addressForm.css">
	
	
<div class="addressContainer">
	<div class="page-header">
	    <h3 class="fw-bold mb-3">주소록</h3>
	    <ul class="breadcrumbs mb-3">
	        <li class="nav-home">
	            <a href="<c:url value='/${member.companyNo }/groupware'/>">
	                <i class="icon-home"></i>
	            </a>
	        </li>
	        <li class="separator">
	            <i class="icon-arrow-right"></i>
	        </li>
	        <li class="nav-item">
	            <a href="<c:url value='/${companyNo}/address'/>">Address</a>
	        </li>
	    </ul>
	</div>	
	
	<div class="address-form-wrapper">
	    <div class="image-column">
			<i class="fas fa-address-book"></i>
	    </div>
	    <div class="form-column">
	    	<div class="address-header">
	        	<h2 class="mb-4">새 주소록 등록 <button type="button" class="btn btn-outline-secondary btn-sm" id="mecro">자동완성</button></h2>   	
	    	</div>
	            <form method="post" action="${pageContext.request.contextPath}/${companyNo}/address/new">
                <table class="form-table">
                    <tr>
                        <td>
                            <label class="form-label">내부/외부 구분</label>
                            <select id="adbkIsExternal" class="form-select" name="adbkIsExternal" required>
                                <option value="">선택</option>
                                <option value="I">내부</option>
                                <option value="E">외부</option>
                            </select>
                        </td>
                        <td>
                            <label class="form-label">이름</label>
                            <input type="text" class="form-control" id="adbkName" name="adbkName" required>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="form-label">회사명</label>
                            <input type="text" class="form-control" id="adbkCompany" name="adbkCompany">
                        </td>
                        <td>
                            <label class="form-label">부서</label>
                            <input type="text" class="form-control" id="adbkDept" name="adbkDept">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="form-label">직급</label>
                            <input type="text" class="form-control" id="adbkPosition" name="adbkPosition">
                        </td>
                        <td>
                            <label class="form-label">이메일</label>
                            <input type="email" class="form-control" id="adbkEmail" name="adbkEmail">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="form-label">휴대폰번호</label>
                            <input type="text" class="form-control" id="adbkPhone" name="adbkPhone" required>
                        </td>
                        <td>
                            <label class="form-label">메모</label>
                            <textarea class="form-control" name="adbkMemo" id="adbkMemo" rows="3"></textarea>
                        </td>
                    </tr>
                </table>
	
                <div class="d-flex justify-content-end mt-3">
                    <a href="${pageContext.request.contextPath}/${companyNo}/address" class="btn btn-primary">목록</a>
                    <button type="submit" class="btn btn-secondary ms-2">등록</button>
                </div>
            </form>
        </div>
    </div>
</div>    
    <script src="${pageContext.request.contextPath }/resources/groupware/js/addressbook/addressForm.js"></script>