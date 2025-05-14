package kr.or.ddit.works.mybatis.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.works.project.vo.ProjectParticipantVO;
import kr.or.ddit.works.project.vo.ProjectVO;
import kr.or.ddit.works.project.vo.TaskVO;

/**
 * 공유 프로젝트 일감 매퍼
 * @author KKM
 * @since 2025. 3. 25.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 25.     	KKM	          최초 생성
 *
 * </pre>
 */
@Mapper
public interface TaskMapper {
	/**
	 * 일반사원 - 일감 조회
	 * @return 
	 */
	public List<TaskVO> selectListAllTask(@Param("projectNo") long projectNo);
	
	public List<TaskVO> selectListProjectTask(@Param("projectNo") long projectNo);
	
	public List<TaskVO> selectTasksByProjectNo(@Param("projectNo") long projectNo);
	
	/**
	 * 일반사원 - 일감 상세 조회
	 * @return 
	 */
	public TaskVO selectTaskDetail(@Param("taskNo")long taskNo);
	/**
	 * 일반사원 - 일감 생성
	 * @return 
	 */
	public int insertTask(TaskVO task);
	/**
	 * 일반사원 - 일감 수정
	 * @return 
	 */
	public int updateTask(TaskVO task);
	
	/**
	 * 일반사원 - 일감 삭제
	 * @return 
	 */
	public int deleteTask(long taskNo);
	
	public Integer getNextTaskNo(long currentTaskNo);
	
	public int getPreviousTaskNo(long currentTaskNo);
	
	public List<ProjectVO> selectAllProjects(String empId);
	
	public List<ProjectParticipantVO> selectListAllParticipants(long projectNo);
	
	public ProjectVO selectProjectByNo(Long projectNo);
	
	public Long selectProjectParticipantNo(@Param("projectNo") Long projectNo, @Param("empId") String empId);
	
	/**
	 * 마지막 일감 번호 조회
	 * @return 
	 */
	public long getLastInsertedTaskNo();
}
