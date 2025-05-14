package kr.or.ddit.works.subscription.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kr.or.ddit.works.subscription.vo.SubscriptionPlansVO;
import kr.or.ddit.works.subscription.vo.SubscriptionsVO;

/**
 * 구독 서비
 * @author JYS
 * @since 2025. 3. 14.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 14.     	JYS	          최초 생성
 *
 * </pre>
 */
public interface SubScriptionService {
	
	public List<SubscriptionPlansVO> readPlanList();
	
	public SubscriptionPlansVO planOne(String planType);
	
	public int insertSubscription(SubscriptionsVO subscription);
	
	public SubscriptionsVO selectSubscription(String contactId);
	
	public List<SubscriptionsVO> subscriptionList();

	public void updatePlanInfo(SubscriptionPlansVO plan); // SJH
}
