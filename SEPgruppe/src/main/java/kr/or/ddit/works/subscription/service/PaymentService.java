package kr.or.ddit.works.subscription.service;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import kr.or.ddit.works.subscription.vo.BillingKeyVO;
import kr.or.ddit.works.subscription.vo.PaymentsVO;

public interface PaymentService {

	public String getAccessToken() throws IOException;
	
	public JsonNode requestSchedulePayment(String customerUid, String merchantUid,
			long scheduleTimestamp, long amount, String planType, PaymentsVO payment) throws IOException;
	
	public void saveBilling(BillingKeyVO billingKey);
	
	public BillingKeyVO selectBilling(String contactId);
	
	public List<PaymentsVO> paymentList();

	public List<PaymentsVO> selectPaymentsBySubscriptionNo(Long subscriptionNo);
}
