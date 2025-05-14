package kr.or.ddit.works.company.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 회사분류 COMPANY_DIVISION VO
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
public class CompanyDivisionVO implements Serializable {
	
	private String companyNo;      //고객사번호
	private String contactId;      //고객사 아이디
	

}
