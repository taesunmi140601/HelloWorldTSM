package kr.or.ddit.works.approval.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 결재선 APPROVAL_LINE VO
 * 
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
@Data
public class AprvlLineVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long aprvlLineNo;      //결재선번호
	private String aprvlDocNo;      //문서번호
	private String empId;      //사원 아이디
	private Long aprvlTurn;      //결재순번
	private String aprvlDate;      //결재일
	private String aprvlStatus;      //결재상태
	private String rejectRsn;      //반려사유

	private String empName;
	
	private AprvlDocVO aprvlDoc; // 결재 문서 has a 관계
	private AprvlLineStatusVO aprvlLineStatus;
}
