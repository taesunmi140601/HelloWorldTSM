package kr.or.ddit.works.organization.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.or.ddit.works.organization.vo.DclzStatusVO;

/**
 *
 * @author JSW
 * @since 2025. 3. 18.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 18.     	JSW	          최초 생성
 *
 * </pre>
 */
public interface DclzStatusService {
	public List<DclzStatusVO> selectDclzStatus(String empId);
	public void insertCheckIn(String empId);
	public void updateCheckOut(String empId);
	public DclzStatusVO selectTodayDclzStatus(String empId);
	public String getWeeklyTotalHours(String empId);
	public String getThisWeekTotalTime(String empId);
}
