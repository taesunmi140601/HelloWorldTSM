package kr.or.ddit.works.subscription.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.works.mybatis.mappers.SubscriptionMapper;
import kr.or.ddit.works.subscription.vo.SubscriptionPlansVO;
import kr.or.ddit.works.subscription.vo.SubscriptionsVO;

/**
 * 구독 서비스 임플
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
@Service
public class SubScriptionServiceImpl implements SubScriptionService {
	@Autowired
	private SubscriptionMapper dao;
	
	@Override
	public List<SubscriptionPlansVO> readPlanList() {
		return dao.planList();
	}

	@Override
	public SubscriptionPlansVO planOne(String planType) {
		return dao.planOne(planType);
	}

	@Override
	public int insertSubscription(SubscriptionsVO subscription) {
		return dao.insertSubscription(subscription);
	}

	@Override
	public SubscriptionsVO selectSubscription(String contactId) {
		return dao.selectSubscription(contactId);
	}

	@Override
	public List<SubscriptionsVO> subscriptionList() {
		return dao.subscriptionList();
	}

	@Override
	public void updatePlanInfo(SubscriptionPlansVO plan) {
	    dao.updatePlan(plan);
	}
}
