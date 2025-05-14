package kr.or.ddit.works.project.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.works.project.vo.ProjectParticipantVO;
import kr.or.ddit.works.project.vo.ProjectVO;
import kr.or.ddit.works.project.vo.TaskVO;

/**
 * 공유 프로젝트 일감 서비스
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
public interface TaskService {
	
	/**
	 * 일반사원 - 일감 조회
	 * @return 
	 */
	public List<TaskVO> selectListProjectTask(long projectNo);
	
	public List<TaskVO> getTasksByProjectNo(long projectNo);
	
	/**
	 * 일반사원 - 일감 상세 조회
	 * @return 
	 */
	public TaskVO selectTaskDetail(@Param("taskNo")long taskNo);
	
	/**
	 * 일반사원 - 일감 생성
	 * @return 
	 */
	public boolean insertTask(TaskVO task);
	
	/**
	 * 일감 첨부파일
	 * @return 
	 */
//	public void processFileUpload(List<MultipartFile> uploadFiles, String companyNo, Long taskNo, String empId);
	
	/**
	 * 일반사원 - 일감 수정
	 * @return 
	 */
	public boolean updateTask(TaskVO task);
	
	/**
	 * 일반사원 - 일감 삭제
	 * @return 
	 */
	public boolean deleteTask(long taskNo);
	
	/**
	 * 일반사원 - 일감 다음 번호 조회
	 * @return 
	 */
	public long getNextTaskNo(long currentTaskNo); // 타입 변경
	
	/**
	 * 일반사원 - 일감 이전 번호 조회
	 * @return 
	 */
	public long getPreviousTaskNo(long currentTaskNo);
	
	public List<ProjectVO> selectAllProjects(String empId);
	
	/**
	 * 일반사원 - 프로젝트 참여자 목록 조회
	 */
	public List<ProjectParticipantVO> selectListAllParticipants(long projectNo);
	
	 /**
     * 마지막으로 삽입된 일감 번호 조회
     * @return 마지막으로 삽입된 일감 번호
     */
	public long getLastInsertedTaskNo();
	
	public Long getProjectParticipantNo(Long projectNo, String empId);
	
    public ProjectVO getProjectByNo(Long projectNo);
}
