package kr.or.ddit.works.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.works.mybatis.mappers.TaskMapper;
import kr.or.ddit.works.project.vo.ProjectParticipantVO;
import kr.or.ddit.works.project.vo.ProjectVO;
import kr.or.ddit.works.project.vo.TaskVO;

/**
 * 공유 프로젝트 일감 서비스임플
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
@Service
public class TaskServiceImpl implements TaskService {
	
	@Autowired
	private TaskMapper taskMapper;
	
	@Override
	public List<TaskVO> selectListProjectTask(long projectNo) {
		return taskMapper.selectListProjectTask(projectNo);
	}
	
	@Override
    public List<TaskVO> getTasksByProjectNo(long projectNo) {
        return taskMapper.selectTasksByProjectNo(projectNo);
    }
	
	@Override
	public TaskVO selectTaskDetail(long taskNo) {
		return taskMapper.selectTaskDetail(taskNo);
	}

	@Override
	public boolean insertTask(TaskVO task) {
		return taskMapper.insertTask(task) > 0;
	}

	@Override
	public boolean updateTask(TaskVO task) {
		return taskMapper.updateTask(task) > 0;
	}

	@Override
	public boolean deleteTask(long taskNo) {
		return taskMapper.deleteTask(taskNo) > 0;
	}
	
	@Override
	public long getNextTaskNo(long currentTaskNo) { // 타입 변경
	    Integer nextTaskNo = taskMapper.getNextTaskNo(currentTaskNo);
	    return (nextTaskNo != null) ? nextTaskNo : -1;
	}
	
	@Override
	public long getPreviousTaskNo(long currentTaskNo) { // 타입 변경
	    if (currentTaskNo <= 1) {
	        return -1;
	    }
	    return taskMapper.getPreviousTaskNo(currentTaskNo);
	}
	
	@Override
	public List<ProjectVO> selectAllProjects(String empId) {
	    return taskMapper.selectAllProjects(empId);
	}
	
	@Override
	public List<ProjectParticipantVO> selectListAllParticipants(long projectNo){
		return taskMapper.selectListAllParticipants(projectNo);
	}
	
	 @Override
	 public long getLastInsertedTaskNo() {
	    return taskMapper.getLastInsertedTaskNo(); // TaskMapper에서 호출
	 }
	 
	 @Override
	 public Long getProjectParticipantNo(Long projectNo, String empId) {
	     return taskMapper.selectProjectParticipantNo(projectNo, empId);
	 }
	 
	 @Override
	 public ProjectVO getProjectByNo(Long projectNo) {
		 return taskMapper.selectProjectByNo(projectNo);
	 }	
}
