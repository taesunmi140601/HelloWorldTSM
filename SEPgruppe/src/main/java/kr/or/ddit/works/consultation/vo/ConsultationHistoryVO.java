package kr.or.ddit.works.consultation.vo;

import java.io.Serializable;

import lombok.Data;

/**
 *  상담 히스토리(CONSULTATION_HISTORY) VO
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
public class ConsultationHistoryVO implements Serializable {
	
	private Long historyNo;      			//상담 히스토리 번호
	private Long consultationNo;      		//상담 요청 고유 번호
	private String consultationDate;      	//상담 일자
	private String consultationContent;     //상담 내용

}
