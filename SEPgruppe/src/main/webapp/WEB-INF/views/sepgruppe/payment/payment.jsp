<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 24.     	손현진            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 <script src="https://cdn.iamport.kr/js/iamport.payment-1.2.0.js"></script>         
<section class="explore-section section-padding" id="section_2">
  <title>포트원(아임포트) 정기결제 테스트</title>
<body>
  <h1>카드 등록(빌링키 발급) 데모</h1>
  <p>아래 버튼 클릭 시 포트원 결제창이 뜨고, 0원 결제를 통해 카드 등록만 진행합니다.</p>

  <button onclick="requestPay()">카드 등록하기</button>

  <script>
    // 1) 아임포트 가맹점 식별코드
    //   - 포트원 관리자 콘솔에서 확인한 값으로 교체
    IMP.init('imp17236348'); // 예: 'imp00000000'

    function requestPay() {
      // 2) 빌링키 발급을 위한 request_pay
      IMP.request_pay({
        pg: 'tosspayments',           // 테스트 PG
        pay_method: 'card',
        merchant_uid: 'order_' + new Date().getTime(), 
        name: '정기결제용 카드 등록',
        amount: 0,                    // 0원 결제 → 카드등록만
        customer_uid: 'my_customer_' + new Date().getTime() // 빌링키로 사용할 값
      }, function(rsp) {
        // 3) 콜백
        if (rsp.success) {
          // billingKey 발급 성공
          // rsp.customer_uid = 실제 billingKey
          alert('카드 등록 성공!\n고객 UID: ' + rsp.customer_uid);

          // 4) AJAX로 서버에 billingKey 전달
          var xhr = new XMLHttpRequest();
          xhr.open('POST', '<c:url value="/payment/saveBillingKey"/>', true);
          xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');
          xhr.onload = function() {
            if (xhr.status === 200) {
              alert('서버에 빌링키 저장 완료!\n' + xhr.responseText);
            } else {
              alert('서버 통신 에러: ' + xhr.status);
            }
          };
          xhr.send(JSON.stringify({
            customerUid: rsp.customer_uid,
            impUid: rsp.imp_uid,
            payMethod: rsp.pay_method
          }));

        } else {
          // 실패
          alert('카드 등록 실패: ' + rsp.error_msg);
        }
      });
    }
  </script>
</body>
</section>
