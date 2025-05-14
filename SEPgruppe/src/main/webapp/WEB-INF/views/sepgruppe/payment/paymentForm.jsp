<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<section class="explore-section section-padding" id="section_2">
  <title>구독 서비스 결제</title>
  <style>
    .paymentContainer { max-width: 800px; margin: 40px auto; background: #fff; padding: 20px; border-radius: 5px; }
    h1, h2, h3 { margin-bottom: 15px; }
    .plan-info, .payment-form { margin-bottom: 30px; }
    .plan-info p { margin: 5px 0; }
    .form-group { margin-bottom: 15px; }
    label { display: block; margin-bottom: 5px; }
    input[type="text"], input[type="number"], input[type="password"] { width: 100%; padding: 8px; box-sizing: border-box; }
    button { padding: 10px 20px; background: #007bff; color: #fff; border: none; border-radius: 3px; cursor: pointer; }
    button:hover { background: #0056b3; }
  </style>
	<body>
		<div class="paymentContainer">
			<h1>구독 서비스 결제</h1>

			<!-- 구독 플랜 정보 영역 -->
			<div class="plan-info">
				<h2>구독 플랜 정보</h2>
				<p>
					플랜명: <strong>${plan.planType}</strong>
				</p>
				<p>
					가용 인원: <strong>${plan.maximumPeople}명</strong>
				</p>
				<p>
					월 결제 가격: <strong>${plan.monthlyPrice}원</strong>
				</p>
				<p>
					연 결제 가격: <strong>${plan.annualPrice}원</strong> (15% 할인 적용)
				</p>
				<!-- 선택된 결제 방식에 따라 결제 금액이 변경될 수 있도록 JavaScript 활용 -->
			</div>

			<!-- 결제 정보 입력 폼 영역 -->
			<div class="payment-form">
				<h2>구독 정보 입력</h2>
				<form id="scheduleForm" action="<c:url value='/payment/schedule'/>"
					method="post">
					<!-- 구독 플랜 선택 (필요 시 이미 선택된 플랜 정보를 hidden 필드로 전달) -->
					<input type="hidden" name="planType" value="${plan.planType}" />

					<div class="row mb-3">
						<label for="companyName" class="col-md-4 col-lg-3 col-form-label">고객사명</label>
						<div class="col-mb-8 col-lg-9">
							<div>${company.companyName }</div>
						</div>
					</div>

					<div class="row mb-3">
						<label for="contactId" class="col-md-4 col-lg-3 col-form-label">고객사 아이디</label>
						<div class="col-md-8 col-lg-9">
							<div>${company.contactId}</div>
						</div>
					</div>

					<!-- 결제 방식 선택 -->
					<div class="form-group">
						<label>결제 방식 선택</label> 
						<label> 
							<input type="radio" id="monthly" name="amount" value="${plan.monthlyPrice}" checked> 월 결제
						</label> 
						<label> 
							<input type="radio" id="yearly" name="amount" value="${plan.annualPrice}"> 연 결제 (15% 할인)
						</label>
					</div>

					<!-- 결제 금액 표시 영역 (선택된 결제 방식에 따라 JavaScript로 갱신) -->
					<div class="form-group">
						<label>결제 금액</label>
						<div id="priceDisplay">
							${plan.monthlyPrice}원 <span>(부가세 별도)</span>
						</div>
					</div>
					
					<p class="btn btn-primary" onclick="requestPay()">정기결제</p>
				</form>
			</div>
		</div>
		<script src="https://cdn.iamport.kr/js/iamport.payment-1.2.0.js"></script>
		
		<script src="${pageContext.request.contextPath }/resources/sepgruppe/js/subscription/paymentForm.js"></script>
	</body>
</section>