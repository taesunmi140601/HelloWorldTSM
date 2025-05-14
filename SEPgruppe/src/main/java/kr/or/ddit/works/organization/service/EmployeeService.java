package kr.or.ddit.works.organization.service;


import java.util.List;

import kr.or.ddit.paging.PaginationInfo;
import kr.or.ddit.paging.SimpleCondition;
import kr.or.ddit.works.organization.vo.DepartmentVO;
import kr.or.ddit.works.organization.vo.EmployeeVO;
import kr.or.ddit.works.organization.vo.OrganizationVO;

public interface EmployeeService {
	
	
	/** 전체 사원 조회 (페이징 적용) */
	public PaginationInfo<OrganizationVO> getAllEmployees(String companyNo, PaginationInfo<OrganizationVO> paging);
	
	/**
	 * 마이페이지 정보 수정
	 * @param companyNo 로그인된 고객사번호
	 * @return
	 */
	public boolean updateEmployee(EmployeeVO member);

	/**
	 * 회원정보 조회
	 * @param empId
	 * @return
	 */
	public EmployeeVO selectEmployee(String empId);
	
	/**
	 * 사원(들) 직위 및 부서 수정
	 * @param empIds
	 * @param fieldType
	 * @param value
	 * @return
	 */
	public int bulkUpdateEmployees(List<String> empIds, String fieldType, String value);
	
	/**
	 * 사원(들) 삭제
	 * @param empIds
	 * @param companyNo
	 * @return
	 */
	public int deleteEmployees(List<String> empIds, String companyNo);

	/**
	 * 마이페이지 이미지 삭제
	 * @param empId
	 * @return
	 */
	public boolean deleteImage(String empId);

	/**
	 * 마이페이지 진입 시 패스워드 인증
	 * @param member
	 * @return
	 */
	public boolean authenticateMember(String empId, String empPw);
	
	List<DepartmentVO> selectDepartments(String companyNo);
	
	public int insertEmployee(EmployeeVO member);

}
