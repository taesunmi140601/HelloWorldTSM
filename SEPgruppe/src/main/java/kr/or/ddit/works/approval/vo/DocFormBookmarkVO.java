package kr.or.ddit.works.approval.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * 양식 즐겨찾기 DOC_FORM_BOOKMARK VO
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
@ToString(callSuper = true)
public class DocFormBookmarkVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String docFormBkmkNo;     //양식즐겨찾기번호
	private String docFrmNo;      	  //양식번호
	private String empId;      		  //사원 아이디

}
