package kr.or.ddit.works.subscription.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import kr.or.ddit.works.company.vo.CompaniesVO;
import lombok.Data;

/**
 * 구독 테이블 VO (SUBSCRIPTIONS)
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
 *  2025. 4. 10.     	SJH	          수정중
 *
 * </pre>
 */
@Data
public class SubscriptionsVO implements Serializable {
	
	private Long subscriptionNo;      		//구독 번호
	private String contactId;      			//고객사 아이디
	private String paymentStatus;      		//결제 상태(성공, 실패 등)
	private String subscriptionStart;      	//서비스 시작일
	private String subscriptionEnd;      	//서비스 종료일
	private String subscriptionsActive;     //활성화 여부(Y/N)
	private String planType;      			//구독 플랜 번호
	private Long billingKeyId;
	private String billingDate;
	
	// ✅ 추가: 날짜 포맷 출력을 위한 파싱된 필드
	private Date subscriptionStartDate;
	private Date subscriptionEndDate;
	
	private List<PaymentsVO> payment;
	private CompaniesVO company;
	private SubscriptionPlansVO subscriptionPlan;
	private BillingKeyVO billingKey;
	
	
	
}
