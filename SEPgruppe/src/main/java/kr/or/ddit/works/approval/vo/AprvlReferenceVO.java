package kr.or.ddit.works.approval.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 결재참조 APPROVAL_REFERENCES VO
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
public class AprvlReferenceVO implements Serializable {
	
	private Long aprvlRefNo;      	//결재참조번호
	private Long aprvlDocNo;      	//기안번호
	private String empId;      		//사원 아이디
	private String aprvlChecked;    //결재확인여부

}
