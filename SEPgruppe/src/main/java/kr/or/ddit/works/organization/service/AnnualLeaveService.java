package kr.or.ddit.works.organization.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kr.or.ddit.works.approval.vo.AprvlDocVO;
import kr.or.ddit.works.organization.vo.AnnualLeaveVO;

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
public interface AnnualLeaveService {
	public List<AnnualLeaveVO> selectAnnual(@Param("empId") String empId);
	
	public List<AprvlDocVO> getApprovedAnnualDocs(String empId);
	public List<AprvlDocVO> getPendingAnnualDocs(String empId);
	
	public int updateUsedAnnualLeave(String empId, double usedLeave);
	public int rollbackUsedAnnualLeave(String empId, double usedLeave);
}
