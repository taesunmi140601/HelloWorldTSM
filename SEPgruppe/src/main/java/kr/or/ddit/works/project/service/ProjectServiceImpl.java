package kr.or.ddit.works.project.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.works.mybatis.mappers.ProjectMapper;
import kr.or.ddit.works.mybatis.mappers.ScheduleMapper;
import kr.or.ddit.works.organization.vo.EmployeeVO;
import kr.or.ddit.works.project.vo.ProjectParticipantVO;
import kr.or.ddit.works.project.vo.ProjectVO;

/**
 * 공유프로젝트 서비스임플
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
@Service
public class ProjectServiceImpl implements ProjectService {
	
	@Autowired
	private ProjectMapper projectMapper;
	
	@Autowired
	private ScheduleMapper scheduleMapper;
	
	 @Override
	    public List<ProjectVO> selectListAllProject(String empId) {
	        return projectMapper.selectListAllProject(empId);
	    }
	
	 @Override
	    public ProjectVO selectProjectDetail(long projectNo, ProjectParticipantVO ppVO) {
	        List<ProjectVO> projectList = projectMapper.selectProjectDetail(projectNo, ppVO);
	        return (projectList != null && !projectList.isEmpty()) ? projectList.get(0) : null;
	    }
	
	 @Override
	    public void insertProject(ProjectVO project) {
	        projectMapper.insertProject(project);  // 프로젝트 삽입 후
	        if (project.getProjectNo() == null) {
	            throw new RuntimeException("프로젝트 번호 생성 실패");
	        }
	     
	        // 2. 생성자도 참여자로 강제 추가 (중복 방지용 if 필요 시 DB에 UNIQUE 제약 있으면 생략 가능)
	        ProjectVO.Participant creator = new ProjectVO.Participant();
	        creator.setEmpId(project.getEmpId());
	        creator.setProjectNo(project.getProjectNo());
	        System.out.println(">>> 생성자 참여자 insert 직전 empId: " + creator.getEmpId());
	        System.out.println(">>> 생성자 참여자 insert 직전 projectNo: " + creator.getProjectNo());

	        projectMapper.insertParticipant(creator);

	        System.out.println(">>> insert 후 생성된 participantNo: " + creator.getProjectParticipantNo());

	        // 3. 나머지 참여자 저장
	        List<ProjectVO.Participant> participants = project.getParticipants();
	        if (participants != null && !participants.isEmpty()) {
	            for (ProjectVO.Participant participant : participants) {
	                participant.setProjectNo(project.getProjectNo());
	                projectMapper.insertParticipant(participant);
	            }
	        }
	    }
	 
	 @Override
	    public void insertParticipant(long projectNo, String empId) {
		 	Long existingNo = projectMapper.selectParticipantNo(projectNo, empId);
		    if (existingNo != null) return;
		    
		 	ProjectVO.Participant participant = new ProjectVO.Participant(empId, "");  // 사원 ID로 참가자 생성
	        participant.setProjectNo(projectNo);  // 프로젝트 번호 설정
	        projectMapper.insertParticipant(participant);  // 매퍼로 전달
	    }
	
	 @Override
	    public void insertParticipants(long projectNo, List<ProjectVO.Participant> participants) {
		 List<ProjectVO.Participant> distinctList = new ArrayList<>();
		    Set<String> uniqueEmpIds = new HashSet<>();

		    for (ProjectVO.Participant participant : participants) {
		        participant.setProjectNo(projectNo);

		        // empId 중복 방지
		        if (uniqueEmpIds.add(participant.getEmpId())) {
		            distinctList.add(participant);
		        }
		    }

		    if (!distinctList.isEmpty()) {
		        projectMapper.insertParticipants(distinctList);
		    }
	    }
	
	 @Override
	    public boolean updateProject(ProjectVO project) {
	        return projectMapper.updateProject(project) > 0;
	    }
	 
	 @Override
	 public int updateProjectStatus(long projectNo, String projectStatus) {
	     return projectMapper.updateProjectStatus(projectNo, projectStatus);
	 }
	
	 @Override
	    public List<EmployeeVO> selectListAllEmployee(String companyNo) {
	        return projectMapper.selectListAllEmployee(companyNo);
	    }
	 
	 @Override
	 public int deleteSchedulesByProjectNos(List<Long> projectNos) {
	     return scheduleMapper.deleteSchedulesByProjectNos(projectNos);
	 }

	 @Transactional
	 @Override
	 public int deleteProjects(List<Long> projectNos) {
	     // 1. 관련 스케줄 먼저 삭제
	     scheduleMapper.deleteSchedulesByProjectNos(projectNos);

	     // 2. 프로젝트 삭제
	     return projectMapper.deleteProjects(projectNos);
	 }
	
	@Override
    public List<ProjectVO> getProjectsCreatedBy(String empId) {
        return projectMapper.selectProjectsCreatedBy(empId);
    }

    @Override
    public List<ProjectVO> getProjectsJoinedBy(String empId) {
        return projectMapper.selectProjectsJoinedBy(empId);
    }
    
    @Override
    public List<ProjectVO> getCompletedProjectsBy(String empId) {
    	return projectMapper.selectCompletedProjectsBy(empId);
    }
	
	}
