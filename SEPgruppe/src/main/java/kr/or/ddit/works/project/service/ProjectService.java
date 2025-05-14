package kr.or.ddit.works.project.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import kr.or.ddit.works.organization.vo.EmployeeVO;
import kr.or.ddit.works.project.vo.ProjectParticipantVO;
import kr.or.ddit.works.project.vo.ProjectVO;

/**
 * 공유프로젝트 서비스
 * @author JYS
 * @since 2025. 3. 16.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 16.     	JYS	          최초 생성
 *
 * </pre>
 */
public interface ProjectService {
	 /**
     * 일반사원 - 프로젝트 조회
     * @param empId 로그인한 사용자의 ID
     * @return 프로젝트 목록
     */
	public List<ProjectVO> selectListAllProject(String empId);
	
	public List<ProjectVO> getProjectsCreatedBy(String empId);
    public List<ProjectVO> getProjectsJoinedBy(String empId);
    public List<ProjectVO> getCompletedProjectsBy(String empId);
	
	/**
	 * 일반사원 - 프로젝트 상세조회
	 * @return 
	 */
	public ProjectVO selectProjectDetail(long projectNo, ProjectParticipantVO ppVO);
	
	/**
	 * 일반사원 - 프로젝트 생성
	 * @return 
	 */
	public void insertProject(ProjectVO project);
	
	/**
	 * 프로젝트 참여자 추가
	 * @return 
	 */
	public void insertParticipant(long projectNo, String empId);
	
	/**
	 * 프로젝트 참여자 여러명 추가
	 * @return 
	 */
	public void insertParticipants(long projectNo, List<ProjectVO.Participant> participants);
	
	/**
	 * 일반사원 - 프로젝트 수정
	 * @return 
	 */
	public boolean updateProject(ProjectVO project);
	
	public int updateProjectStatus(long projectNo, String projectStatus);
	
	public List<EmployeeVO> selectListAllEmployee(String companyNo);
	
	public int deleteSchedulesByProjectNos(List<Long> projectNos);
	public int deleteProjects(List<Long> projectNos);
	
}
