package kr.or.ddit.works.organization.vo;

import java.io.Serializable;

import lombok.Data;


/**
 * 부서 DEPARTMENT VO
 * 
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
public class DepartmentVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String deptCd;      		//부서코드
	private String parentDeptCd;      	//상위 부서 코드
	private String deptName;      		//부서명
	private String managerEmpId;      	//부서장
	private String createAt;      		//부서 생성일
	private String companyNo;
	
	private String status;				// ajax 상태 정보 담는용 

}
