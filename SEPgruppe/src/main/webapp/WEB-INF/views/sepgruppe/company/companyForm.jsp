<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<link
   href="${pageContext.request.contextPath}/resources/sepgruppe/TopicListing-1.0.0/css/bootstrap.min.css"
   rel="stylesheet">
<link
   href="${pageContext.request.contextPath}/resources/sepgruppe/css/clientForm.css"
   rel="stylesheet">

<!-- 회원가입 Form (tiles 타지 않음) -->
<form:form method="post" modelAttribute="company" >
   <div id="joinForm">
      <div class="sepJoinimg">
         <img
            src="${pageContext.request.contextPath}/resources/sepgruppe/images/LOGO.png"
            class="img-thumbnail" id="sepJoin">
      </div>

		<div class="form-group col-6 mx-auto">
			<label for="contactId" class="form-label">아이디 <span
				class="required">*</span></label>
			<div class="form-floating">
				<input type="text" class="form-control" name="contactId"
					id="contactId" value="${companies.contactId}" required> <span
					id="contactIdError" class="text-danger"></span>
				<form:errors path="contactId" class="text-danger" element="span" />
			</div>
		</div>
		
		<div class="form-group col-6 mx-auto">
			<label for="contactPw" class="form-label">패스워드 <span
				class="required">*</span></label>
			<div class="form-floating">
				<input type="password" class="form-control" name="contactPw"
					id="contactPw" required>
			</div>
		</div>

		<div class="form-group col-6 mx-auto">
			<label for="confirmPw" class="form-label">패스워드 확인 <span
				class="required">*</span></label>
			<div class="form-floating">
				<input type="password" class="form-control" name="confirmPw"
					id="confirmPw" required> <span id="passwordMatchError"
					class="text-danger"></span>
			</div>
		</div>

		<div class="form-group col-6 mx-auto">
			<label for="contactNm" class="form-label">이름 <span
				class="required">*</span></label>
			<div class="form-floating">
				<input type="text" class="form-control" name="contactNm"
					id="contactNm" value="${companies.contactNm}" required>
				<form:errors path="contactNm" class="text-danger" element="span" />
			</div>
		</div>
		<div class="form-group col-6 mx-auto">
			<label for="contactPhone" class="form-label">연락처 <span
				class="required">*</span></label>
			<div class="form-floating">
				<input type="text" class="form-control" name="contactPhone"
					id="contactPhone" value="${companies.contactPhone}" required>
				<form:errors path="contactPhone" class="text-danger" element="span" />
			</div>
		</div>
		
		<div class="form-group col-6 mx-auto">
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

		<div class="form-group col-6 mx-auto">
			<label for="companyAdd1" class="form-label">주소</label>
			<div class="input-group">
				<input type="text" id="sample6_address" name="companyAdd1" class="form-control" readonly /><br>
			</div>
		</div>

		<div class="form-group col-6 mx-auto">
			<label for="companyAdd2" class="form-label">상세주소</label>
			<div class="input-group">
				<input type="text" name="companyAdd2" id="sample6_detailAddress" class="form-control" />
			</div>
		</div>
		
		<div class="form-group col-6 mx-auto">
            <label for="contactEmail" class="form-label">이메일 <span class="required">*</span></label>
            <div class="input-group">
                <input type="text" class="form-control" name="contactEmail" id="contactEmail" required>
                <button type="button" class="btn btn-secondary" id="sendAuthCode">인증번호 발송</button>
            </div>
            <span id="emailError" class="text-danger"></span>
        </div>
        
		
		<!-- 인증번호 입력 및 확인 -->
        <div class="form-group col-6 mx-auto">
            <label for="authCode" class="form-label">인증번호</label>
            <div class="input-group">
                <input type="text" class="form-control" id="authCode">
                <button type="button" class="btn btn-secondary" id="verifyAuthCode">확인</button>
            </div>
            <span id="authResult" class="text-danger"></span>
        </div>

		<div class="form-group col-6 mx-auto">
			<label for="companyName" class="form-label">고객사명</label>
			<div class="form-floating">
				<input type="text" class="form-control" name="companyName"
					id="companyName" value="${companies.companyName}">
				<form:errors path="companyName" class="text-danger" element="span" />
			</div>
		</div>

		<div class="form-group col-6 mx-auto mb-3">
			<label for="businessRegNo" class="form-label">사업자 등록번호</label>
			<div class="input-group">
				<input type="number" class="form-control" name="businessRegNo"
					id="businessRegNo" value="${companies.businessRegNo}">
				<button type="button" class="btn btn-secondary"
					id="checkBusinessDuplicate" disabled>중복가입 확인</button>
			</div>
			<span id="businessRegNoError" class="text-danger d-block mt-1"></span>
    		<form:errors path="businessRegNo" class="text-danger d-block mt-1" element="span" />
		</div>

		<div class="form-floating col-6 mx-auto">
         <button type="submit" class="btn btn-secondary" id="submitBtn" disabled>회원가입</button>
      </div>
   </div>
</form:form>




<script src="${pageContext.request.contextPath}/resources/sepgruppe/js/company/companyForm.js"></script>
<!-- Daum Address API -->
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="${pageContext.request.contextPath }/resources/daumaddress/daumAPI.js"></script>

