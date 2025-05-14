<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

	<link href="${pageContext.request.contextPath }/resources/sepgruppe/css/loginBackground.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath }/resources/sepgruppe/css/loginForm.css" rel="stylesheet">

<section class="section-padding">
    <div class="container">
        <div class="row">
        <div class="row center-box">
            <div class="col-md-6 text-center">
                <h2 class="fw-bold">WELCOME TO SEPGroupee</h2>
                <div class="tabs">
                    <button class="tab active" data-target="loginFields">로그인</button>
                    <button class="tab" data-target="joinFields">회원가입</button>
                </div>

                <form id="loginForm" action="${pageContext.request.contextPath }/login/loginProcess" method="post">
                    <div id="loginFields">
                        <div class="form-floating mb-3">
                            <input type="text" class="form-control" id="floatingID" name="userId" placeholder="ID">
                            <label for="floatingID">ID</label>
                        </div>

                        <div class="form-floating mb-3">
                            <input type="password" class="form-control" name="userPw" id="floatingPassword" placeholder="Password">
                            <label for="floatingPassword">Password</label>
                        </div>

                        <div class="d-grid gap-2">
                            <button class="btn btn-success" type="submit">로그인</button>
                        </div>
                        
                        <div class="form-floating mt-3">
	                        <select id="testAccountSelect" class="form-select">
	                            <option value="">로그인 사용자 선택</option>
	                            <option value="test001|java">사원(test001)</option>
	                            <option value="company001|java">고객사 관리자(company001)</option>
	                            <option value="provider001|java">서비스 제공자(provider001)</option>
	                        </select>
	                    </div>
                    </div>
				</form>
                    <div id="joinFields" style="display: none;">
                    	<form:form method="post" modelAttribute="company" >
                        <div class="form-floating mb-3">
                            <label for="contactId" class="form-label">아이디 <span class="required">*</span></label>
                            <div class="form-floating">
                                <input type="text" class="form-control" name="contactId" id="contactId" value="${companies.contactId}" required>
                                <span id="contactIdError" class="text-danger"></span>
                                <form:errors path="contactId" class="text-danger" element="span" />
                            </div>
                        </div>

                        <div class="form-floating mb-3">
                            <label for="contactPw" class="form-label">패스워드 <span class="required">*</span></label>
                            <div class="form-floating">
                                <input type="password" class="form-control" name="contactPw" id="contactPw" required>
                            </div>
                        </div>

                        <div class="form-floating mb-3">
                            <label for="confirmPw" class="form-label">패스워드 확인 <span class="required">*</span></label>
                            <div class="form-floating">
                                <input type="password" class="form-control" name="confirmPw" id="confirmPw" required>
                                <span id="passwordMatchError" class="text-danger"></span>
                            </div>
                        </div>

                        <div class="form-floating mb-3">
                            <label for="contactNm" class="form-label">이름 <span class="required">*</span></label>
                            <div class="form-floating">
                                <input type="text" class="form-control" name="contactNm" id="contactNm" value="${companies.contactNm}" required>
                                <form:errors path="contactNm" class="text-danger" element="span" />
                            </div>
                        </div>
                        <div class="form-floating mb-3">
                            <label for="contactPhone" class="form-label">연락처 <span class="required">*</span></label>
                            <div class="form-floating">
                                <input type="text" class="form-control" name="contactPhone" id="contactPhone" value="${companies.contactPhone}" required>
                                <form:errors path="contactPhone" class="text-danger" element="span" />
                            </div>
                        </div>

                        <div class="form-floating mb-3">
                            <label for="companyZip" class="form-label">우편번호</label>
                            <div class="form-floating">
                                <div class="input-group" id="empGroup">
                                    <input type="text" id="sample6_postcode" class="form-control" name="companyZip" />
                                    <button type="button" onclick="sample6_execDaumPostcode()" class="btn btn-secondary">
                                        <i class="fa fa-map-marker">우편번호 검색</i>
                                    </button>
                                </div>
                                <span class="text-danger" id="zipError"></span>
                            </div>
                        </div>

                        <div class="form-floating mb-3">
                            <label for="companyAdd1" class="form-label">주소</label>
                            <div class="input-group">
                                <input type="text" id="sample6_address" name="companyAdd1" class="form-control" readonly /><br>
                            </div>
                        </div>

                        <div class="form-floating mb-3">
                            <label for="companyAdd2" class="form-label">상세주소</label>
                            <div class="input-group">
                                <input type="text" name="companyAdd2" id="sample6_detailAddress" class="form-control" />
                            </div>
                        </div>

                        <div class="form-floating mb-3">
                            <label for="contactEmail" class="form-label">이메일 <span class="required">*</span></label>
                            <div class="input-group">
                                <input type="text" class="form-control" name="contactEmail" id="contactEmail" required>
                                <button type="button" class="btn btn-secondary" id="sendAuthCode">인증번호 발송</button>
                            </div>
                            <span id="emailError" class="text-danger"></span>
                        </div>


                        <div class="form-floating mb-3">
                            <label for="authCode" class="form-label">인증번호</label>
                            <div class="input-group">
                                <input type="text" class="form-control" id="authCode">
                                <button type="button" class="btn btn-secondary" id="verifyAuthCode">확인</button>
                            </div>
                            <span id="authResult" class="text-danger"></span>
                        </div>

                        <div class="form-floating mb-3">
                            <label for="companyName" class="form-label">고객사명</label>
                            <div class="form-floating">
                                <input type="text" class="form-control" name="companyName" id="companyName" value="${companies.companyName}">
                                <form:errors path="companyName" class="text-danger" element="span" />
                            </div>
                        </div>

                        <div class="form-floating mb-3">
                            <label for="businessRegNo" class="form-label">사업자 등록번호</label>
                            <div class="input-group">
                                <input type="number" class="form-control" name="businessRegNo" id="businessRegNo" value="${companies.businessRegNo}">
                                <button type="button" class="btn btn-secondary" id="checkBusinessDuplicate" disabled>중복가입 확인</button>
                            </div>
                            <span id="businessRegNoError" class="text-danger d-block mt-1"></span>
                            <form:errors path="businessRegNo" class="text-danger d-block mt-1" element="span" />
                        </div>

                        <div class="form-floating col-6 mx-auto">
                         <button type="submit" class="btn btn-secondary" id="submitBtn" disabled>회원가입</button>
                      </div>
	                    <div class="d-grid gap-2 mt-2">
	                        <a class="btn btn-success" href="/sep/company/new">회원가입</a>
	                    </div>
	                   </form:form>
                   </div>

                    <div class="d-flex justify-content-between mt-2">
                        <a href="<c:url value='/login/findId'/>" class="text-decoration-none text-white">아이디 찾기</a>
                        <a href="<c:url value='/login/findPw'/>" class="text-decoration-none text-white">비밀번호 찾기</a>
                    </div>
            	</div>
            </div>
        </div>
    </div>
</section>


<!--     <div class="container"> -->
<!--         <div class="login-header"> -->
<!--             <div class="profile-icon"></div> -->
<!--             <div class="tabs"> -->
<!--                 <button class="tab active">로그인</button> -->
<!--                 <button class="tab">회원가입</button> -->
<!--             </div> -->
<!--         </div> -->
<%--         <form class="login-form" id="loginForm" action="${pageContext.request.contextPath }/login/loginProcess" method="post"> --%>
<!--             <div class="input-group"> -->
<!--                 <label for="email">이메일</label> -->
<!--                 <input type="email" id="email" value="spencec6@"> -->
<!--             </div> -->
<!--             <div class="input-group"> -->
<!--                 <label for="password">비밀번호</label> -->
<!--                 <input type="password" id="password"> -->
<!--             </div> -->
<!--             <button type="submit" class="submit-button">확인</button> -->
<!--             <div class="forgot-password"> -->
<%--                 <a href="<c:url value='/login/findId'/>">아이디를 잊으셨나요?</a> --%>
<%--                 <a href="<c:url value='/login/findPw'/>">비밀번호를 잊으셨나요?</a> --%>
<!--             </div> -->
<!--             <div class="form-floating mt-3"> -->
<!--                 <select id="testAccountSelect" class="form-select"> -->
<!--                     <option value="">로그인 사용자 선택</option> -->
<!--                     <option value="test001|java">사원(test001)</option> -->
<!--                     <option value="company001|java">고객사 관리자(company001)</option> -->
<!--                     <option value="provider001|java">서비스 제공자(provider001)</option> -->
<!--                 </select> -->
<!--             </div> -->
<%--         </form> --%>
<!--     </div> -->
    
<script src="${pageContext.request.contextPath}/resources/sepgruppe/js/login/loginForm.js" />
<script src="${pageContext.request.contextPath}/resources/sepgruppe/js/company/companyForm.js"></script>