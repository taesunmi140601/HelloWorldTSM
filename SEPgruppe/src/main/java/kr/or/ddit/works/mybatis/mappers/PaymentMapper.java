package kr.or.ddit.works.mybatis.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.works.subscription.vo.BillingKeyVO;
import kr.or.ddit.works.subscription.vo.PaymentsVO;

@Mapper
public interface PaymentMapper {
	
	public void saveBilling(BillingKeyVO billingKey);
	
	public BillingKeyVO selectBilling(String contactId);
	
	public int insertPayment(PaymentsVO payment);
	
	public List<PaymentsVO> paymentList();
	
	List<PaymentsVO> selectPaymentsBySubscriptionNo(Long subscriptionNo);
}
