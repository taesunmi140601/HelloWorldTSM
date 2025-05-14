package kr.or.ddit.works.organization.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 직책 EMP_ROLE 테이블 VO
 * @author JYS
 * @since 2025. 3. 12.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 12.     	JYS	          최초 생성
 *
 * </pre>
 */
@Data
public class EmpRoleVO implements Serializable {
	
	private Long roleNo;      	//직책번호
	private String empId;      	//사원 아이디
	private String roleName;    //직책명

}
