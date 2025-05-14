package kr.or.ddit.works.project.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.works.alarm.controller.GWAlarmController;
import kr.or.ddit.works.organization.vo.EmployeeVO;
import kr.or.ddit.works.project.service.ProjectService;
import kr.or.ddit.works.project.vo.ProjectParticipantVO;
import kr.or.ddit.works.project.vo.ProjectVO;
import kr.or.ddit.works.project.vo.ProjectVO.Participant;
import kr.or.ddit.works.schedule.service.ScheduleService;
import kr.or.ddit.works.schedule.vo.ScheduleVO;

/**
 * 공유 프로젝트 컨트롤러
 * @author JYS
 * @since 2025. 3. 14.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 14.     	JYS	          최초 생성
 *
 * </pre>
 */
@Controller
@RequestMapping("/{companyNo}/project")
//@RequestMapping("/a001/project")
public class ProjectController {
	
	 @Autowired
	 private ProjectService projectService;
	 
	 @Autowired 
	 private ScheduleService scheduleService;
	 
	 @Autowired
	 private GWAlarmController gwAlarmController;
	
	 @InitBinder
	 public void initBinder(WebDataBinder binder) {
	     // 날짜 포맷 처리
	     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	     dateFormat.setLenient(false); // 엄격한 날짜 형식 체크
	     binder.registerCustomEditor(Date.class, "projectStartTime", new CustomDateEditor(dateFormat, true));
	     binder.registerCustomEditor(Date.class, "projectEndTime", new CustomDateEditor(dateFormat, true));
	     
	     // participants 필드를 String[]에서 ProjectVO.Participant로 변환하는 간단한 처리
	     binder.registerCustomEditor(List.class, "participants", new CustomCollectionEditor(List.class) {
	         @Override
	         protected Object convertElement(Object element) {
	             // 새로 추가한 생성자를 사용
	             return new ProjectVO.Participant((String) element, "");
	         }
	     });
	 }

    /** 일반 사원 - 사원 개인의 공유 프로젝트 목록 */
	 @GetMapping("")
	    public String selectListAllProject(
	            @PathVariable("companyNo") String companyNo,
	            @RequestParam(value = "projectNo", required = false) Long projectNo,
	            Principal principal,  // 로그인한 사용자 정보
	            Model model) {
	        
	        // 로그인한 사용자의 ID 가져오기
	        String empId = principal.getName();  // 또는 다른 방법으로 사용자 ID를 가져올 수 있습니다.
	        
	        // 사용자의 프로젝트 목록 조회
	        List<ProjectVO> projectsOwned = projectService.getProjectsCreatedBy(empId);
	        List<ProjectVO> projectsJoined = projectService.getProjectsJoinedBy(empId);
	        List<ProjectVO> projectsCompleted = projectService.getCompletedProjectsBy(empId);
	        // 모델에 프로젝트 목록 추가
	        model.addAttribute("projectsOwned", projectsOwned);
	        model.addAttribute("projectsJoined", projectsJoined);
	        model.addAttribute("projectsCompleted", projectsCompleted);
	        model.addAttribute("companyNo", companyNo);
	        model.addAttribute("selectedProjectNo", projectNo);
	        
	        // 프로젝트 목록 페이지로 이동
	        return "gw:project/projectList"; 
	    }
	
	 /** 일반 사원 - 프로젝트 상세 보기 */
	 @GetMapping("/{projectNo}")
	 public String selectProjectDetail(@PathVariable("companyNo") String companyNo, 
	                                    @PathVariable("projectNo") long projectNo,
	                                    ProjectParticipantVO ppVO,
	                                    Model model) {
	     // 단일 프로젝트 정보 가져오기
	     ProjectVO projectDetails = projectService.selectProjectDetail(projectNo, ppVO);

	     model.addAttribute("companyNo", companyNo); // 회사 번호 추가
	     model.addAttribute("projectDetails", projectDetails); // 프로젝트 상세 정보 추가

	     return "gw:project/projectDetail"; // JSP로 이동
	 }
	 
	// 프로젝트 생성 폼
	 @GetMapping("/new")
	    public String insertProjectFormUI(@PathVariable("companyNo") String companyNo, Model model, Principal principal) {
	        String empId = principal.getName();
	        model.addAttribute("empId", empId);

	        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	        model.addAttribute("currentDate", currentDate);

	        List<EmployeeVO> employees = projectService.selectListAllEmployee(companyNo);
	        model.addAttribute("employees", employees);

	        return "gw:project/projectForm";
	    }

	 /** 일반 사원 - 프로젝트 생성 */
	 @PostMapping("/new")
	 public String insertProject(@PathVariable("companyNo") String companyNo,
             @ModelAttribute ProjectVO project,
             @RequestParam(value = "participants", required = false) List<String> participants,
             Principal principal) {

			String empId = principal.getName();
			project.setEmpId(empId);  // 프로젝트 생성 시 empId 설정
			
			// 참여자 목록 생성
			List<ProjectVO.Participant> participantList = new ArrayList<>();
				if (participants != null) {
				for (String participantId : participants) {
				participantList.add(new ProjectVO.Participant(participantId, ""));  // empId를 participantId로 변경
				}
				
			}
			
			project.setParticipants(participantList);
			
			projectService.insertProject(project);  // 프로젝트 삽입
			
			if (project.getProjectStartTime() == null || project.getProjectEndTime() == null) {
			    throw new IllegalArgumentException("프로젝트 시작일 또는 종료일이 비어있습니다.");
			}
			
			ScheduleVO schedule = new ScheduleVO();
			schedule.setScheduleTypeNo(4L); // 프로젝트 일정 (타입 ID는 네 시스템 기준에 따라 설정)
			schedule.setEmpId(empId);
			schedule.setSchdlNm("[프로젝트] " + project.getProjectTitle());  // 일정 이름
			schedule.setSchdlCn(project.getProjectDesc());                 // 설명은 프로젝트 설명 재활용
			schedule.setSchdlBgngYmd(project.getProjectStartTime());
			schedule.setSchdlEndYmd(project.getProjectEndTime());
			schedule.setSchdlLocation("사내");  // 필요에 따라 바꿔줘
			schedule.setSchdlStatus("확정");   // 기본값
			schedule.setSchdlCreateDate(new Date());
			schedule.setProjectNo(project.getProjectNo());
//			schedule.setNotifyTime(LocalDate.now().toString()); // 알림시간도 임의로

			scheduleService.createSchedule(schedule); // 스케줄 등록
			
			// 참여자 추가
			for (ProjectVO.Participant participant : participantList) {
			gwAlarmController.sendProjectAlarm(project, participant.getEmpId());
			}
			
			return "redirect:/" + companyNo + "/project";
			}
    
	    
	    
	 	/** 일반사원 - 프로젝트 수정  */
	    @GetMapping("{projectNo}/edit")
	    public String updateProjectFormUI(@PathVariable("companyNo") String companyNo) {
	    	return "gw:project/projectEdit";
	    }
	    
	    /** 일반사원 - 프로젝트 수정 */
	    @PutMapping("{projectNo}/edit")
	    public String updateProject(@PathVariable("companyNo") String companyNo,
	    		 					@PathVariable("projectNo") String projectNo) {
	    	return "redirect:/" + companyNo + "/project/" + projectNo;
	    }
	    
	    @PostMapping("deleteSelected")
	    public String deleteSelectedProjects(@RequestParam("projectNos") List<Long> projectNos,
	    									 RedirectAttributes redirectAttributes,
	                                         @PathVariable("companyNo") String companyNo) {
	    	
	    	int scheduleDeletedCount = scheduleService.deleteSchedulesByProjectNos(projectNos);
	    	
	    	int deletedCount = projectService.deleteProjects(projectNos);
	        
	        if (deletedCount > 0) {
	            redirectAttributes.addFlashAttribute("deleteSuccess", "총 " + deletedCount + "건의 프로젝트가 삭제되었습니다. (일정 " + scheduleDeletedCount + "건 포함)");
	        } else {
	            redirectAttributes.addFlashAttribute("deleteFail", "삭제할 수 있는 프로젝트가 없습니다.");
	        }

	        return "redirect:/" + companyNo + "/project";
	    }
	    
    @PutMapping(value = "{projectNo}/status", consumes = "application/json")
    @ResponseBody
    public Map<String, Object> updateProjectStatusAjax(@PathVariable("projectNo") long projectNo,
                                                       @RequestBody Map<String, String> body) {
        String projectStatus = body.get("projectStatus");
        int updated = projectService.updateProjectStatus(projectNo, projectStatus);

        Map<String, Object> result = new HashMap<>();
        result.put("success", updated > 0);
        return result;
    }
}
