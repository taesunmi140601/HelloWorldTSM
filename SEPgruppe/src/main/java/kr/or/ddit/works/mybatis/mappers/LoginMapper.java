package kr.or.ddit.works.mybatis.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.works.company.vo.CompaniesVO;
import kr.or.ddit.works.login.vo.AllUserVO;

/**
 * 로그인 매퍼
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
public interface LoginMapper {

	public AllUserVO login(@Param("userId") String userId);
	
	public CompaniesVO autoLogin(@Param("userId") String userId);
	
	public CompaniesVO findAccount(CompaniesVO company);
	
	public int updateCompany(CompaniesVO company);
	
}
