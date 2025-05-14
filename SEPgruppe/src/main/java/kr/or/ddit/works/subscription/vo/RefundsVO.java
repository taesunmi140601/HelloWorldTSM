package kr.or.ddit.works.subscription.vo;

import java.io.Serializable;

import lombok.Data;


/**
 * 환불내역 테이블 VO (REFUNDS)
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
public class RefundsVO implements Serializable {
	
	private Long refundNo;      		//환불 요청 번호
	private Long paymentNo;      		//결제 번호
	private String refundDate;      	//환불 요청 날짜
	private Long refundAmount;      	//환불 금액
	private String refundStatus;      	//환불 상태
	private String reason;      		//환불 사유
	private String processedDate;      	//환불 처리 날짜
	private String planNo;      		//구독 플랜 번호

}
