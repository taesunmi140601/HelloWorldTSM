package kr.or.ddit.works.subscription.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;

import kr.or.ddit.security.CustomUserDetailService;
import kr.or.ddit.security.RealUserWrapper;
import kr.or.ddit.works.company.vo.CompaniesVO;
import kr.or.ddit.works.company.vo.CompanyDivisionVO;
import kr.or.ddit.works.login.vo.AllUserVO;
import kr.or.ddit.works.mybatis.mappers.CompanyMapper;
import kr.or.ddit.works.mybatis.mappers.EmployeeMapper;
import kr.or.ddit.works.organization.vo.EmployeeVO;
import kr.or.ddit.works.subscription.service.PaymentService;
import kr.or.ddit.works.subscription.service.SubScriptionService;
import kr.or.ddit.works.subscription.vo.BillingKeyVO;
import kr.or.ddit.works.subscription.vo.PaymentsVO;
import kr.or.ddit.works.subscription.vo.SubscriptionPlansVO;
import kr.or.ddit.works.subscription.vo.SubscriptionsVO;

/**
 * 자동결제 및 환불 컨트롤
 */
@Controller
@RequestMapping("/payment")
public class PaymentsController {
	
	@Inject
	private SubScriptionService subService;
	@Inject
	private PaymentService paymentService;
	@Inject
	private EmployeeMapper empMapper;
	@Inject
	private CompanyMapper companyMapper;
	@Autowired
	private CustomUserDetailService userDetailsService;
	
	

	// [예시] 전체 결제 내역 목록
	@GetMapping("")
	public String selectListAllPayment(Model model) {
		List<PaymentsVO> paymentList = paymentService.paymentList();
		model.addAttribute("paymentList", paymentList);
		return "sep:admin/subscription/paymentList";
	}
	
	// [예시] 결제 상세
	@GetMapping("/{paymentNo}")
	public String selectPaymentDetail(@PathVariable("paymentNo") String paymentNo) {
		// 상세 조회 로직
		return "sep:admin/subscription/paymentDetail";
	}
	
	// [예시] 환불 목록
	@GetMapping("/refund")
	public String selectListAllRefund() {
		return "sep:admin/subscription/refundList";
	}
	
	// [예시] 환불 상세
	@GetMapping("/refund/{refundId}")
	public String selectRefundDetail(@PathVariable("refundId") String refundId) {
		return "sep:admin/subscription/refundDetail";
	}
	
	/**
	 * 구독 플랜 안내 페이지
	 * - what 파라미터로 플랜타입을 받아 DB 조회
	 */
	@GetMapping("/subPayment")
	public String paymentForm(@RequestParam("what") String planType, Model model,
			Authentication authentication) {
		CompaniesVO company = getCompany(authentication);
		SubscriptionPlansVO plan = subService.planOne(planType);

		model.addAttribute("plan", plan);
		model.addAttribute("company", company);
		
		// -> /WEB-INF/views/sep/payment/paymentForm.jsp
		return "sep:payment/paymentForm";
	}
	
	private CompaniesVO getCompany(Authentication authentication) {
		Object principal = authentication.getPrincipal();
		RealUserWrapper user = (RealUserWrapper) principal;
		AllUserVO allUser = user.getRealUser();
		CompaniesVO company = (CompaniesVO) allUser;
		return company;
	}

	/**
	 * (B) 정기결제 요청 (자동 결제)
	 * - 이미 발급된 billingKey(customerUid)를 사용
	 */
	@PostMapping(value ="/schedule", produces = "text/plain; charset=UTF-8")
	@ResponseBody
	public String schedulePayment(
	        @RequestParam(required = false, defaultValue = "0") String scheduleDate,
	        @RequestParam(required = false, defaultValue = "0") long amount,
	        @RequestParam String planType,
	        Authentication authentication
	) {
	    try {
	    	String contactId = authentication.getName();
	    	
	    	LocalDate today = LocalDate.now();
	        LocalDateTime localDateTime = LocalDateTime.of(today, LocalTime.of(17, 40));
	        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
	        long scheduleTimestamp = zonedDateTime.toEpochSecond();
	        
	    	// 2) 가맹점 주문번호
	        String merchantUid = "order_" + System.currentTimeMillis();
	        BillingKeyVO billing = paymentService.selectBilling(contactId);
	        
	        String customerUid = billing.getBillingKey();
	        
	        PaymentsVO payment = new PaymentsVO();
	        payment.setPaymentNo(merchantUid);
	        payment.setPaymentDate(today.toString());
	        payment.setContactId(contactId);
	        payment.setPaymentAmount(amount);
	        payment.setPaymentStatus("결제 완료");
	        
	        // 3) 스케줄 등록 요청
	        JsonNode result = paymentService.requestSchedulePayment(
	        		customerUid, merchantUid, scheduleTimestamp, amount, planType, payment);
	        SubscriptionsVO subscription = new SubscriptionsVO();
	        subscription.setContactId(contactId);
	        subscription.setPaymentStatus(payment.getPaymentStatus());
	        subscription.setSubscriptionStart(today.toString());
	        subscription.setSubscriptionEnd(today.plusMonths(1).toString());
	        subscription.setPlanType(planType);
	        subscription.setBillingDate("매월 "+String.valueOf(today.getDayOfMonth())+"일");
	        subscription.setBillingKeyId(billing.getBillingKeyId());
	        subService.insertSubscription(subscription);
	        
	        int code = result.get("code").asInt();
	        if (code == 0) {
	            // 등록 성공
	            JsonNode response = result.get("response");
	            // response 안에 schedules 배열이 있음
	            insertEmpAdmin(contactId, authentication);
	            return "스케줄 등록 성공: " + response.toString();
	        } else {
	            // 등록 실패
	            String message = result.get("message").asText();
	            return "스케줄 등록 실패: " + message;
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        return "서버 에러: " + e.getMessage();
	    }
	}

	private void insertEmpAdmin(String contactId,Authentication authentication) {
		CompaniesVO company = getCompany(authentication);
		CompanyDivisionVO companyDivision = new CompanyDivisionVO();
		companyDivision.setCompanyNo(company.getBusinessRegNo());
		companyDivision.setContactId(company.getContactId());
		companyMapper.insertCompanyDivision(companyDivision);
		
		EmployeeVO member = new EmployeeVO();
		member.setEmpId(contactId + "_admin");
		member.setCompanyNo(company.getBusinessRegNo());
		member.setEmpNo(String.valueOf(company.getBusinessRegNo()));
		member.setEmpNm(company.getCompanyName());
		member.setEmpPw("admin");
		member.setEmpZip(company.getCompanyZip());
		member.setEmpAdd1(company.getCompanyAdd1());
		member.setEmpAdd2(company.getCompanyAdd2());
		empMapper.insertEmployee(member);
		companyMapper.updateCompanyAdmin(member.getEmpId(), contactId);
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(contactId);

	    UsernamePasswordAuthenticationToken authToken =
	        new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

	    SecurityContextHolder.getContext().setAuthentication(authToken);
	}

	/**
	 * (C) [결제창 사용] 빌링키 저장
	 * - 결제창에서 등록된 customer_uid를 AJAX로 전달받아 DB(또는 세션)에 저장
	 */
	@PostMapping("/saveBillingKey")
	@ResponseBody
	public String saveBillingKey(@RequestBody Map<String, Object> requestData, Authentication authentication) {
		String customerUid = (String) requestData.get("customerUid");
		String userId = authentication.getName();
		
		if(paymentService.selectBilling(userId) != null) {
			return "BillingKey 이미 있음: " + customerUid;
		}
		
		BillingKeyVO billing = new BillingKeyVO();
		billing.setBillingKey(customerUid);
		billing.setContactId(userId);
		
		if (customerUid == null) {
			return "customerUid가 존재하지 않습니다.";
		}
		// 실제로는 DB에 저장해야 함
		paymentService.saveBilling(billing);
		
		return "BillingKey 저장 완료: " + customerUid;
	}
	
}
