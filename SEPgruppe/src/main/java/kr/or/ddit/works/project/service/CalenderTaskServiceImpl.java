package kr.or.ddit.works.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.works.mybatis.mappers.CalenderTaskMapper;
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
 *
 * </pre>
 */
@Service
public class CalenderTaskServiceImpl implements CalenderTaskService {
	
	@Autowired
	private CalenderTaskMapper calenderTaskMapper;
	
	@Override
	public List<TaskVO> selectListCalenderTask() {
		return calenderTaskMapper.selectListCalenderTask();
	}

}
