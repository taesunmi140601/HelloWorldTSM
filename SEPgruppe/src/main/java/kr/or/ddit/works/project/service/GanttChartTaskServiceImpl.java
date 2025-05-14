package kr.or.ddit.works.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.works.mybatis.mappers.GanttChartTaskMapper;
import kr.or.ddit.works.project.vo.TaskVO;

/**
 * 공유 프로젝트 일감 캘린더 서비스임플
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
@Service
public class GanttChartTaskServiceImpl implements GanttChartTaskService {
	
	@Autowired
	private GanttChartTaskMapper  ganttChartTaskMapper;
	
	@Override
	public List<TaskVO> selectListGanttChart(Long projectNo) {
		return ganttChartTaskMapper.selectListGanttChart(projectNo);
	}

}
