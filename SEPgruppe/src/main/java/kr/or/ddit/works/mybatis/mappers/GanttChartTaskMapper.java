package kr.or.ddit.works.mybatis.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.works.project.vo.TaskVO;

/**
 * 공유 프로젝트 일감 간트차트 매퍼
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
@Mapper
public interface GanttChartTaskMapper {
	
	/**
	 * 일반사원 - 간트차트 일감 진행률 조회
	 * @return 
	 */
	public List<TaskVO> selectListGanttChart(Long projectNo);
}
