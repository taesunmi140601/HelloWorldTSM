package kr.or.ddit.works.approval.vo;

import java.io.Serializable;
import java.util.List;

import javax.swing.text.Position;

import kr.or.ddit.works.organization.vo.DepartmentVO;
import kr.or.ddit.works.organization.vo.EmployeeVO;
import kr.or.ddit.works.organization.vo.PositionVO;
import lombok.Data;

/**
 * 자동결재선 APPROVAL_LINE_AUTO VO
 * @author JYS
 * @since 2025. 4. 2.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 4. 2.     	JYS	          최초 생성
 *
 * </pre>
 */
@Data
public class AprvlLineAutoVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String aprvlLineNo;      // 결재선 번호
    private String aprvlLineName;    // 결재선명
    private String docFrmNo;         // 양식번호
    
    private String commCodeNo;       // 결재선 타입 (결재/합의)
    private String commCodeName;    // 결재/합의
    private String aprvlTurn;     // 결재 순번
    private String deptCd;        // 부서 코드
    private String deptName;      // 부서명 (JOIN으로 가져올 수도 있음)
    private String positionCd;    // 직위 코드
    private String positionName;  // 직위명 (JOIN으로 가져올 수도 있음)
    
    private DocFormVO docFormVo;     // 문서 양식 VO
    
    private List<EmployeeVO> employeeList; // 해당 결재선에 적합한 사원

   
	
	
	
	

}
