package kr.or.ddit.works.approval.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 결재선 즐겨찾기 목록 APPROVAL_LINE_BOOKMARK_LIST VO
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
public class AprvlListBookmarkListVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long aprvlLineBkmkNo;      //결재선즐겨찾기번호
	private String empId;      		   //사원아이디
}
