package kr.or.ddit.works.organization.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.works.organization.vo.DepartmentVO;

/**
 * 
 * @author JSW
 * @since 2025. 3. 31.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 31.     	JSW	          최초 생성
 *
 * </pre>
 */
public interface DepartmentService {
	public List<DepartmentVO> selectListAllDepartment();
	public int addDepartment(DepartmentVO dept);
	public int deleteDepartment(String companyNo, String deptCd);
	public int updateDepartmentField(DepartmentVO dept);
	
	public List<DepartmentVO> parseExcel(MultipartFile file, String companyNo) throws Exception;
	
	public int bulkInsertDepartments(List<DepartmentVO> deptList);
}
