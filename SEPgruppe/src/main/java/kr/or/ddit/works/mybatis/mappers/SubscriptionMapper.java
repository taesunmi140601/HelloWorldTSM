package kr.or.ddit.works.mybatis.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.works.subscription.vo.SubscriptionPlansVO;
import kr.or.ddit.works.subscription.vo.SubscriptionsVO;

/**
 * 구독 매퍼
 * @author JYS
 * @since 2025. 3. 16.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 16.     	JYS	          최초 생성
 *
 * </pre>
 */
@Mapper
public interface SubscriptionMapper {
	public List<SubscriptionPlansVO> planList();
	
	public SubscriptionPlansVO planOne(String planType);
	
	public int insertSubscription(SubscriptionsVO subscription);
	
	public SubscriptionsVO selectSubscription(@Param("contactId") String contactId);
	
	public List<SubscriptionsVO> subscriptionList();

	void updatePlan(SubscriptionPlansVO plan);
	
}
