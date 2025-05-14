package kr.or.ddit.works.project.service;

import java.util.List;

import kr.or.ddit.works.project.vo.TaskVO;

/**
 * 공유 프로젝트 일감 간트차트 서비스
 * @author KKM
 * @since 2025. 3. 26.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 26.     	KKM	          최초 생성
 *  2025. 4. 06.     	JSW	          수정중
 *
 * </pre>
 */
public interface GanttChartTaskService {
	
	/**
	 * 일반사원 - 일감 간트차트 조회
	 * @return 
	 */
	public List<TaskVO> selectListGanttChart(Long projectNo);
}
