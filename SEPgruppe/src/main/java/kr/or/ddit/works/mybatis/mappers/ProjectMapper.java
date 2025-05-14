package kr.or.ddit.works.mybatis.mappers;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.works.organization.vo.EmployeeVO;
import kr.or.ddit.works.project.vo.ProjectParticipantVO;
import kr.or.ddit.works.project.vo.ProjectVO;

/**
 * 공유프로젝트 매퍼
 * @author JYS
 * @since 2025. 3. 17.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 17.     	JYS	          최초 생성
 *
 * </pre>
 */
@Mapper
public interface ProjectMapper {
	/**
	 * 일반사원 - 프로젝트 조회
	 * @return 
	 */
	public List<ProjectVO> selectListAllProject(String empId);
	
	/**
	 * 일반사원 - 프로젝트 상세조회
	 * @return 
	 */
	public List<ProjectVO> selectProjectDetail(@Param("projectNo") long projectNo, @Param("ppVO") ProjectParticipantVO ppVO);
	
	/**
	 * 일반사원 - 프로젝트 생성
	 * @return 
	 */
	public void insertProject(ProjectVO project);
	
	/**
	 * 특정 프로젝트에 특정 사원이 이미 참여자인지 확인
     * 존재하면 project_participant_no 반환, 없으면 null
	 * @param projectNo
	 * @param empId
	 * @return
	 */
	public Long selectParticipantNo(@Param("projectNo") Long projectNo, @Param("empId") String empId);
	
	 /**
     * 프로젝트 참여자 추가
     * @param empId 사원 ID
     */
	public void insertParticipant(ProjectVO.Participant participant);
	
	/**
     * 프로젝트 참여자 여러명 추가
     * @param empId 사원 ID
     */
	public void insertParticipants(List<ProjectVO.Participant> participants);
	
	/**
	 * 일반사원 - 프로젝트 수정
	 * @return 
	 */
	public int updateProject(ProjectVO project);
	
	public int updateProjectStatus(@Param("projectNo") long projectNo, @Param("projectStatus") String projectStatus);
	
	
	public List<EmployeeVO> selectListAllEmployee(String companyNo);
	
	public int deleteProjects(List<Long> projectNos);
	
	public List<ProjectVO> selectProjectsCreatedBy(String empId);
    
	public List<ProjectVO> selectProjectsJoinedBy(String empId);
    
	public List<ProjectVO> selectCompletedProjectsBy(String empId);
	
	public List<Map<String, Object>> selectMyProjectTasks(@Param("empId") String empId);
}
