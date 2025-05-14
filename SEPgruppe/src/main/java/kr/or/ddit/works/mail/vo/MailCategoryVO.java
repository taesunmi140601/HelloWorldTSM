package kr.or.ddit.works.mail.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 메일 카테고리 MAIL_CATEGORY VO
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
public class MailCategoryVO implements Serializable {

	private Long mailCategoryNo;      	//메일 카테고리 번호
	private String empId;      			//사원 아이디
	private String mailCategoryNm;      //메일 카테고리명
}
