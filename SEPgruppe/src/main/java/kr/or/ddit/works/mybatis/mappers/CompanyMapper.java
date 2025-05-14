package kr.or.ddit.works.mybatis.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.works.company.vo.CompaniesVO;
import kr.or.ddit.works.company.vo.CompanyDivisionVO;
import kr.or.ddit.works.organization.vo.EmployeeVO;

/**
 * 고객사 매퍼
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
public interface CompanyMapper {

	public int countByContactId(String contactId);

	public int countByBusinessRegNo(String businessRegNo);

	public int insertCompany(CompaniesVO company);
	
	public int insertCompanyDivision(CompanyDivisionVO companyDivision);
	
	/**
	 * 회원정보 조회
	 * @param contactId
	 * @return
	 */
	public CompaniesVO selectCompany(@Param("contactId") String contactId);

	/**
	 * 회원정보 수정
	 * @param member
	 * @return
	 */
	public int updateCompany(CompaniesVO member);
	
	public int updateCompanyAdmin(@Param("adminId") String adminId,@Param("contactId") String contactId);
	
	public List<CompaniesVO> companyList();
	
	public CompaniesVO selectCompanyNo(String companyNo);

}
