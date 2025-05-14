package kr.or.ddit.works.mybatis.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.paging.PaginationInfo;
import kr.or.ddit.paging.SimpleCondition;
import kr.or.ddit.works.organization.vo.DepartmentVO;
import kr.or.ddit.works.organization.vo.EmployeeVO;
import kr.or.ddit.works.organization.vo.OrganizationVO;


/**
 * 사원 매퍼
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
@Mapper
public interface EmployeeMapper {
	
	/** 전체 사원 조회 (페이징 포함) */
	 public List<OrganizationVO> selectAllEmployees(@Param("companyNo") String companyNo, 
             							@Param("paging") PaginationInfo<OrganizationVO> paging,
             							@Param("simple") SimpleCondition simpleCondition);
	 
    /** 전체 사원 수 조회 (페이징용) */
    public int countAllEmployees(@Param("companyNo") String companyNo,
    						@Param("simple") SimpleCondition simpleCondition);
    
    public int countActiveEmployees(@Param("companyNo") String companyNo);
	
	/**
	 * 마이페이지 정보 수정
	 * @param member 로그인된 아이디
	 * @return
	 */
	public int updateEmployee(EmployeeVO member);
	
	// 사원(들) 직위 및 부서 수정
	public int updateEmployeesPosition(@Param("empIds") List<String> empIds, @Param("positionCd") String positionCd);
	public int updateEmployeesDepartment(@Param("empIds") List<String> empIds, @Param("deptCd") String deptCd);
	
	/**
	 * 사원(들) 삭제
	 * @param empIds
	 * @param companyNo
	 * @return
	 */
	public int deleteEmployees(@Param("empIds") List<String> empIds, @Param("companyNo") String companyNo);

	/**
	 * 회원정보 조회
	 * @param empId
	 * @return
	 */
	public EmployeeVO selectEmployee(@Param("empId") String empId);

	/**
	 * 마이페이지 이미지 삭제
	 * @param empId
	 * @return
	 */
	public int deleteImage(@Param("empId") String empId);
	
	public int insertEmployee(EmployeeVO member);
	
	List<DepartmentVO> selectDepartments(@Param("companyNo") String companyNo);
	
	String selectLastEmpNo(@Param("companyNo") String companyNo);
	
	public int updateDeptCd(@Param("empId") String empId, @Param("deptCd") String deptCd);
	
	public int clearDeptCd(@Param("empId") String empId);
}
