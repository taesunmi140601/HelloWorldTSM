package kr.or.ddit.works.approval.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 결재선 즐겨찾기 번호 APPROVAL_LINE_BOOKMARK VO
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
public class AprvlListBookmarkVO implements Serializable {
	
	private Long aprvlLineBkmkNo;      //결재선즐겨찾기번호
	private String aprvlLineBkmkName;      //결재선즐겨찾기명
	private String empId;      //사원아이디
}
