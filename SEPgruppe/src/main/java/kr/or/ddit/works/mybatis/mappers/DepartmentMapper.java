package kr.or.ddit.works.mybatis.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.works.organization.vo.DepartmentVO;

/**
 * 부서 매퍼
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
public interface DepartmentMapper {

	public List<DepartmentVO> selectListAllDepartment();
	public int insertDepartment(DepartmentVO dept);
	public int deleteDepartment(@Param("companyNo") String companyNo, @Param("deptCd") String deptCd);
	// 부서 관리에서의 수정
	public int updateDepartmentField(DepartmentVO dept);
	public String selectManagerDeptCd(@Param("deptCd") String deptCd, @Param("companyNo") String companyNo);
	
	public DepartmentVO selectDepartmentByCode(@Param("deptCd") String deptCd, @Param("companyNo") String companyNo);
	// 일괄등록에서의 수정
	public int updateDepartment(DepartmentVO dept);
}
