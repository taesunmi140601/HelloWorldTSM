package kr.or.ddit.works.company.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kr.or.ddit.works.company.vo.CompaniesVO;

/**
 * 회사 서비스
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

public interface CompanyService {

	public boolean existsByContactId(String contactId);

	public boolean existsByBusinessRegNo(String businessRegNo);

	public void insertCompany(CompaniesVO company);

	public int MailAuth(String mail);

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
	public boolean updateCompany(CompaniesVO member);

	public boolean authenticateMember(String contactId, String contactPw);
	
	public List<CompaniesVO> companyList();
}
