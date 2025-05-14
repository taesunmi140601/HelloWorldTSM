/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 3. 26.     	손현진            최초 생성
 *
 * </pre>
 */
let pay = 0;

document.addEventListener("DOMContentLoaded", function() {
	const priceDisplay = document.getElementById("priceDisplay");
	// JSP EL 변수는 페이지 로딩 시 값이 치환됨
	// 결제 방식 변경 시 가격 정보 갱신
	const paymentRadios = document
		.getElementsByName("amount");
	paymentRadios.forEach(function(radio) {
		radio.addEventListener("change", function() {
			if (this.id === "monthly") {
				priceDisplay.innerHTML = this.value
					+ "원 <span>(부가세 별도)</span>";
				pay = this.value;
			} else if (this.id === "yearly") {
				priceDisplay.innerHTML = this.value
					+ "원 <span>(부가세 별도)</span>";
				pay = this.value;
			}
		});
	});
});
	
	IMP.init('imp17236348');
	function requestPay() {
		// 2) 빌링키 발급을 위한 request_pay
		IMP.request_pay({
			pg: 'tosspayments',           // 테스트 PG
			pay_method: 'card',
			merchant_uid: 'order_' + new Date().getTime(),
			name: '정기결제용 카드 등록',
			amount:pay,                   // 0원 결제 → 카드등록만
			customer_uid: 'my_customer_' + new Date().getTime() // 빌링키로 사용할 값
		}, function(rsp) {
			// 3) 콜백
			if (rsp.success) {
				// billingKey 발급 성공
				// rsp.customer_uid = 실제 billingKey
				alert('카드 등록 성공!\n고객 UID: ');

				// 4) AJAX로 서버에 billingKey 전달
				var xhr = new XMLHttpRequest();
				xhr.open('POST', '/sep/payment/saveBillingKey', true);
				xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');
				xhr.onload = function() {
					if (xhr.status === 200) {
						alert('정기결제, 그룹웨어 계정 등록 성공!\n');
						document.getElementById("scheduleForm").submit();
						location.href = "/sep";
					} else {
						alert('서버 통신 에러: ' + xhr.status);
					}
				};
				xhr.send(JSON.stringify({
					customerUid: rsp.customer_uid,
					impUid: rsp.imp_uid,
					payMethod: rsp.pay_method,
					amount:pay
				}));

			} else {
				// 실패
				alert('카드 등록 실패: ' + rsp.error_msg);
			}
		});
	}


