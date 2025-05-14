<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 18.     	손현진            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- Login Form -->
<section class="section-padding">

<div class="container">
    <form id="findIdForm">
        <div class="sepimg">
            <img src="${pageContext.request.contextPath}/resources/sepgruppe/images/LOGO.png" class="img-thumbnail" id="sep">
            <h5>회원가입 시 입력한 이름과 이메일 주소를 입력해주세요.</h4>
        </div>
        
        <div class="form-floating col-6 mx-auto mb-3">
        <input type="text" class="form-control" id="floatingId" name="contactId" placeholder="아이디">
        <label for="floatingId">아이디</label>
    </div>
        <div class="form-floating col-6 mx-auto mb-3">
        <input type="text" class="form-control" id="floatingName" name="contactNm" placeholder="이름">
        <label for="floatingName">이름</label>
    </div>
    <div class="form-floating col-6 mx-auto mb-3">
            <div class="input-group">
                <input type="email" class="form-control" name="contactEmail" id="contactEmail" placeholder="이메일" required>
                <button type="button" class="btn btn-success" id="sendAuthCode">인증번호 발송</button>
            </div>
            <span id="emailError" class="text-danger"></span>
        </div>

        <!-- 인증번호 입력 및 확인 -->
        <div class="form-floating col-6 mx-auto mb-3">
            <div class="input-group">
                <input type="text" class="form-control" id="authCode" placeholder="인증번호">
                <button type="button" class="btn btn-success" id="verifyAuthCode">확인</button>
            </div>
            <span id="authResult" class="text-danger"></span>
        </div>
        
    <div class="d-grid gap-2 col-6 mx-auto mb-3">
        <button class="btn btn-success" type="button" id="findPwBtn">계정 확인</button>
    </div>
         
    </form>
    <div id="resultMessage" class="text-center mt-3"></div>
</div>
</section>

<script src="${pageContext.request.contextPath}/resources/sepgruppe/js/login/findForm.js" />
