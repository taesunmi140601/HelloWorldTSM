package kr.or.ddit.works.subscription.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 구독 플랜 VO (SUBSCRIPTION_PLANS)
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
 *  2025. 4. 9.     	SJH	          구독 플랜 관리 및 변경 저장 처리 추가
 *
 * </pre>
 */
@Data
public class SubscriptionPlansVO implements Serializable {
	private String planNo;      		//구독 플랜 번호
	private String planType;     		//구독 플랜 유형
	private Long monthlyPrice;      	//월간 가격
	private Long annualPrice;      		//연간 가격
	private String maintainOldPrice;    //기존 가격 유지 여부 NULL-가격 변화 없음 'Y'-기존 가격 유지 'N'-기존 가격 미유지
	private Long maximumPeople;      	//가용 인원
	private List<SubscriptionPlansVO> plans;	// 구독 플랜 리스트
	
}
