package kr.or.ddit.works.mybatis.mappers;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.works.approval.vo.AprvlDocVO;
import kr.or.ddit.works.organization.vo.AnnualLeaveVO;
import kr.or.ddit.works.organization.vo.AnnualVO;

/**
 * 
 * @author JSW
 * @since 2025. 3. 19.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 19.     	JSW	          최초 생성
 *
 * </pre>
 */
@Mapper
public interface AnnualLeaveMapper {
	public List<AnnualLeaveVO> selectAnnual(@Param("empId") String empId);

	/**
	 * 전자결재 연차신청서 휴가 옵션 조회
	 * @return
	 */
	public List<AnnualVO> selectAnnualOption();
	
	public List<AprvlDocVO> selectApprovedAnnualDocs(String empId);
	public List<AprvlDocVO> selectPendingAnnualDocs(String empId);
	
	public int updateUsedAnnualLeave(Map<String, Object> param);
	public int rollbackUsedAnnualLeave(Map<String, Object> param);


}
