<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<section class="section-padding">
<div id="layoutSidenav_content">
    <div class="container-fluid px-4">
	        <div>
	        	<button id="btn1Week" class="btn btn-outline-info">1주일</button>
				<button id="btn1Month" class="btn btn-outline-info">1개월</button>
				<button id="btn3Months" class="btn btn-outline-info">3개월</button>
				<button id="btn6Months" class="btn btn-outline-info">6개월</button>
			</div>
        <br>
        <div class="row">
				<div class="col-xl-6">
          <div class="card mb-4">
            <div class="card-header">
              <i class="fas fa-chart-area me-1"></i> 날짜별 활성 구독 수
            </div>
            <div class="card-body">
              <canvas id="activeLineChart" 
                      data-subscription-list='<c:out value="${subscriptionListJson}" />'
                      style="height: 100%;"></canvas>
            </div>
          </div>
        </div>
        
        <div class="col-xl-6">
                <div class="card mb-4" style="height: 510px;">
                    <div class="card-header">
                        <i class="fas fa-chart-area me-1"></i>
                        매출 현황
                        
                    </div>
                    <div class="card-body">
                        <canvas id="paymentChart" width="100%" height="40" data-payment-list='<c:out value="${paymentListJson}" />'></canvas>
                    </div>
                </div>
            </div>
        
        <!-- 활성 구독 도넛 차트 카드 -->
        <div class="col-xl-6">
          <div class="card mb-4" >
            <div class="card-header">
              <i class="fas fa-chart-pie me-1"></i> 플랜 유형별 활성 구독
            </div>
            <div class="card-body">
              <canvas id="activeDonutChart" 
                      data-subscription-list='<c:out value="${subscriptionListJson}" />'
                      style="height: 100%;"></canvas>
            </div>
          </div>
        </div>

				
            
<!--                 <div class="card mb-4"> -->
<!--                     <div class="card-header"> -->
<!--                         <i class="fas fa-chart-bar me-1"></i> -->
<!--                         신규 상담 요청 목록 -->
<!--                     </div> -->
<!--                     <div class="card-body"> -->
<%--                         <canvas id="myBarChart" width="100%" height="40"></canvas> --%>
<!--                     </div> -->
<!--                 </div> -->
        </div>

<!--         <div class="card mb-4"> -->
<!--             <div class="card-header"> -->
<!--                 <i class="fas fa-table me-1"></i> -->
<!--                 신규 가입 고객사 목록 -->
<!--             </div> -->
<!--             <div class="card-body"> -->
<!--                 <table id="datatablesSimple"> -->
<!--                     <thead> -->
<!--                         <tr> -->
<!--                             <th>전체 가입 고객사 수</th> -->
<!--                             <th>활성 구독 수</th> -->
<!--                             <th>신규 구독 신청 내역 리스트</th> -->
<!--                             <th>최근 결제 내역</th> -->
<!--                             <th>자동 결제 내역</th> -->
<!--                             <th>상담 요청 건수</th> -->
<!--                             <th>상담 요청 진행 상태</th> -->
<!--                             <th>체험 계정 활성 현황</th> -->
<!--                         </tr> -->
<!--                     </thead> -->
<!--                     <tbody> -->
<!--                         <tr> -->
<!--                             <td>데이터</td> -->
<!--                             <td>데이터</td> -->
<!--                             <td>데이터</td> -->
<!--                             <td>데이터</td> -->
<!--                             <td>데이터</td> -->
<!--                             <td>데이터</td> -->
<!--                             <td>데이터</td> -->
<!--                             <td>데이터</td> -->
<!--                         </tr> -->
<!--                     </tbody> -->
<!--                 </table> -->
<!--             </div> -->
<!--         </div> -->
    </div>
</div>
</section>

<script src="${pageContext.request.contextPath }/resources/groupware/js/provider/providerSettingForm.js"></script>
