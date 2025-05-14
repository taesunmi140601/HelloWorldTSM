package kr.or.ddit.works.organization.service;

import java.util.List;

import kr.or.ddit.works.organization.vo.DepartmentVO;
import kr.or.ddit.works.organization.vo.EmployeeVO;
import kr.or.ddit.works.organization.vo.OrganizationVO;

public interface OrganizationService {
	 List<DepartmentVO> selectParentDep(String companyNo);
	 List<DepartmentVO> selectChildDep(String deptCd, String companyNo);
	 List<EmployeeVO> selectEmployee(String deptCd, String companyNo);
	 List<OrganizationVO> searchEmployees(String keyword, String companyNo);
	 List<OrganizationVO> searchByDepartment(String deptName, String keyword);
	 List<EmployeeVO> selectAllEmployees(String companyNo);
	 
	 List<DepartmentVO> selectChildDepartments(String companyNo, String parentDeptCd);
}
