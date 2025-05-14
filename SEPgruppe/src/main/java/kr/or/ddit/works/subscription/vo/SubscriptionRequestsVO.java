package kr.or.ddit.works.subscription.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 구독신청테이블 VO (SUBSCRIPTION_REQUESTS) 
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
@Data
public class SubscriptionRequestsVO implements Serializable {
	
	private Long requestNo;      		//신청 번호
	private String planNo;      		//구독 플랜 번호
	private String contactId;      		//고객사 아이디
	private String requestDate;     	//신청 날짜
	private String requestPeriod;      	//신청 구독 기간
	private Long requestPersonCount;    //신청 인원 수
	private String paymentMethod;      	//결제 방식
	private String requestsStatus;      //신청 상태(승인/거절 등)

}
