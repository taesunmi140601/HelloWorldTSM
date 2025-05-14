package kr.or.ddit.works.consultation.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 상담요청(CONSULTATION_REQUESTS) VO
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
public class ConsultationRequestsVO implements Serializable{

	private Long consultationNo;     		 //상담 요청 고유 번호
	private String contactId;      			 //고객사 아이디
	private String consultationDate;      	 //요청 일자
	private String consultationType;      	 //상담 유형(예: 구독 문의, 결제 문제 등)
	private String consultationStatus;       //진행 상태예: 대기, 진행 중, 완료)
}
