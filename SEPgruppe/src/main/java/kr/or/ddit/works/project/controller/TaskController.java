package kr.or.ddit.works.project.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.works.alarm.controller.GWAlarmController;
import kr.or.ddit.works.mybatis.mappers.TaskMapper;
import kr.or.ddit.works.project.service.TaskService;
import kr.or.ddit.works.project.vo.ProjectParticipantVO;
import kr.or.ddit.works.project.vo.ProjectVO;
import kr.or.ddit.works.project.vo.TaskVO;
import kr.or.ddit.works.schedule.service.ScheduleService;
import kr.or.ddit.works.schedule.vo.ScheduleVO;
import lombok.extern.slf4j.Slf4j;

/**
 * 공유 프로젝트 일감 컨트롤러
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
@Slf4j
@Controller
@RequestMapping("/{companyNo}/task")
public class TaskController {
	
	@Autowired
	private TaskService taskService;
	
	 @Autowired
	 private TaskMapper taskMapper;
	
	 @Autowired
	 private ScheduleService scService; 
	 
	 @Autowired
	 private GWAlarmController gwAlarmController;
	/** 일반 사원 - 모든 일감 목록 */
	 @GetMapping("")
	 public String selectListProjectTask(
		        @PathVariable("companyNo") String companyNo,
		        @RequestParam(required = false) Long projectNo,
		        Model model) {  // Principal 매개변수 제거

		    // 모든 일감 목록 조회
		 	List<TaskVO> tasks = taskService.getTasksByProjectNo(projectNo);
		 	model.addAttribute("tasks", tasks);
		 	model.addAttribute("companyNo", companyNo);
		 	model.addAttribute("projectNo", projectNo);

		    // 🔽 프로젝트 생성자 ID 조회 후 전달
		    if (projectNo != null) {
		        ProjectVO project = taskService.getProjectByNo(projectNo);
		        if (project != null) {
		            model.addAttribute("projectEmpId", project.getEmpId()); // 생성자 ID
		            model.addAttribute("projectTitle", project.getProjectTitle());
		        }
		    }
		 	
		    // 일감 목록 페이지로 이동
		    return "gw:task/taskList"; 
	    }
	
	 /** 특정 일감 상세 조회 */
	 @GetMapping("/{taskNo}")
	 public String selectTaskDetail(
	         @PathVariable("companyNo") String companyNo,
	         @PathVariable("taskNo") long taskNo, // 타입을 long으로 변경
	         @RequestParam(value = "projectNo", required = false) Long projectNo,
	         Model model) {
	     // 특정 일감 상세 정보 조회
	     TaskVO taskDetail = taskService.selectTaskDetail(taskNo); // 서비스에서 상세 정보 조회
	     
	     if (taskDetail.getProjectNo() == 0 || taskDetail.getProjectNo() == null) {
	    	    taskDetail.setProjectNo(projectNo);
	     }
	     

	     // 모델에 일감 상세 정보 및 회사 번호 추가
	     model.addAttribute("taskDetail", taskDetail);
	     model.addAttribute("companyNo", companyNo); // 회사 번호 추가
	     model.addAttribute("projectNo", projectNo);

	     // 일감 상세 페이지로 이동
	     return "gw:task/taskDetail";
	 }
	 
	 
	 @GetMapping("/new")
	    public String insertTaskFormUI(
	            @PathVariable("companyNo") String companyNo,
	            @RequestParam(value = "projectNo", required = false) Long projectNo,
	            Model model,
	            Principal principal) {

	        String empId = principal.getName();
	        List<ProjectVO> projects = taskService.selectAllProjects(empId);
	        model.addAttribute("projects", projects);
	        model.addAttribute("companyNo", companyNo);

	        if (projectNo != null) {
	            List<ProjectParticipantVO> participants = taskService.selectListAllParticipants(projectNo);
	            ProjectVO project = taskService.getProjectByNo(projectNo); // 프로젝트 정보 조회

	            model.addAttribute("participants", participants);
	            model.addAttribute("selectedProjectNo", projectNo);
	            model.addAttribute("selectedProjectTitle", project.getProjectTitle());
	            
	            // 프로젝트 생성자(PL) 정보 전달
	            model.addAttribute("plEmpId", project.getEmpId());
	            model.addAttribute("plEmpNm", project.getEmpNm());
	        } else {
	            model.addAttribute("participants", null);
	            model.addAttribute("selectedProjectNo", null);
	            model.addAttribute("selectedProjectTitle", null);
	        }
	        return "gw:task/taskForm";
	    }

		@PostMapping("/new")
		public String insertTask(
		        @PathVariable("companyNo") String companyNo,
		        @ModelAttribute TaskVO task,
		        @RequestParam(value = "projectNo", required = false) Long projectNo,
		        @RequestParam("empId") String empId,
		        @RequestParam(value = "uploadFiles", required = false) List<MultipartFile> uploadFiles
		) {
		    if (projectNo != null) {
		        task.setProjectNo(projectNo);
		    }
		    task.setEmpId(empId);
		    task.setUploadFiles(uploadFiles);
//		    ScheduleVO
		    ProjectVO project = taskService.getProjectByNo(projectNo);
		    if (project != null) {
		        task.setProjectEmpId(project.getEmpId());
		    }
		    
		    Long participantNo = taskService.getProjectParticipantNo(projectNo, empId);
		    task.setProjectParticipantNo(participantNo);

		    boolean isInserted = taskService.insertTask(task);
//		    scService.createSchedule();
		    if (isInserted) {
		        long taskNo = taskMapper.getLastInsertedTaskNo();
		        gwAlarmController.sendTaskAlarm(task, empId, taskNo);
		        return "redirect:/" + companyNo + "/task/" + taskNo;
		    } else {
		        return "redirect:/" + companyNo + "/task/new";
		    }
		}

		/** 특정 프로젝트의 참여자 목록 조회 */
		@GetMapping("/new/participants/{projectNo}")
		@ResponseBody
		public List<ProjectParticipantVO> getParticipants(@PathVariable("projectNo") long projectNo) {
		    log.debug("Fetching participants for projectNo: {}", projectNo); // 확인용 로그
		    List<ProjectParticipantVO> participant = taskService.selectListAllParticipants(projectNo);
		    return participant;
		}
		
		// 일감 상세페이지 안에서 삭제
		@PostMapping("/{taskNo}/delete")
		public String deleteTaskViaPost(
		        @PathVariable("companyNo") String companyNo,
		        @PathVariable("taskNo") long taskNo,
		        @RequestParam("projectNo") Long projectNo,
		        RedirectAttributes redirectAttributes,
		        Principal principal) {
			
			String loginEmpId = principal.getName();
		    TaskVO task = taskService.selectTaskDetail(taskNo);

		    // 권한 체크: 삭제 권한이 없으면 리다이렉트 + 실패 메시지
		    if (!loginEmpId.equals(task.getEmpId()) && !loginEmpId.equals(task.getProjectEmpId())) {
		        redirectAttributes.addFlashAttribute("deleteFail", "삭제 권한이 없습니다.");
		        return "redirect:/" + companyNo + "/task?projectNo=" + projectNo;
		    }
			
		    taskService.deleteTask(taskNo);
		    
		    redirectAttributes.addFlashAttribute("deleteSuccess", true);

		    // 삭제 후, 해당 프로젝트의 일감 목록으로 이동
		    return "redirect:/" + companyNo + "/task?projectNo=" + projectNo;
		}
		
		// 일감 리스트에서 삭제 
		@PostMapping("ajax/delete")
		@ResponseBody
		public ResponseEntity<?> deleteTasksAjax(
		        @PathVariable String companyNo,
		        @RequestBody Map<String, Object> payload,
		        Principal principal
		        ) {
			List<String> taskNoStrings = (List<String>) payload.get("taskNos");
			String loginEmpId = principal.getName();

		    if (taskNoStrings == null || taskNoStrings.isEmpty()) {
		        return ResponseEntity.badRequest().body("삭제할 항목이 없습니다.");
		    }

		    List<Long> taskNos = new ArrayList<>();
		    for (String no : taskNoStrings) {
		        try {
		            taskNos.add(Long.parseLong(no));
		        } catch (NumberFormatException e) {
		            return ResponseEntity.badRequest().body("잘못된 일감 번호 형식입니다.");
		        }
		    }

		    for (Long taskNo : taskNos) {
		        TaskVO task = taskService.selectTaskDetail(taskNo);
		        if (task == null) continue;

		        // 담당자 or 프로젝트 생성자인 경우만 삭제
		        if (loginEmpId.equals(task.getEmpId()) || loginEmpId.equals(task.getProjectEmpId())) {
		            taskService.deleteTask(taskNo);
		        } else {
		            // 삭제 권한 없음 → 403 응답
		            return ResponseEntity.status(403).body("일감 삭제 권한이 없습니다.");
		        }
		    }

		    return ResponseEntity.ok("삭제 완료");
		}
		
		// 수정 폼 이동
		@GetMapping("/{taskNo}/edit")
		public String editTaskForm(
		        @PathVariable("companyNo") String companyNo,
		        @PathVariable("taskNo") Long taskNo,
		        Model model
		) {
		    TaskVO task = taskService.selectTaskDetail(taskNo);
		    if (task == null) {
		        return "redirect:/" + companyNo + "/task"; // 혹은 에러 페이지
		    }
		    ProjectVO project = taskService.getProjectByNo(task.getProjectNo());
		    
		    List<ProjectParticipantVO> participants = taskService.selectListAllParticipants(task.getProjectNo());

		    model.addAttribute("task", task);
		    model.addAttribute("companyNo", companyNo);
		    model.addAttribute("mode", "edit");  // JSP에서 수정 모드로 표시
		    model.addAttribute("selectedProjectNo", task.getProjectNo());
		    model.addAttribute("selectedProjectTitle", project != null ? project.getProjectTitle() : "");
		    model.addAttribute("participants", participants);
		    
		    return "gw:task/taskForm";
		}
		
		@PostMapping("/{taskNo}/edit")
		public String updateTask(
		        @PathVariable("companyNo") String companyNo,
		        @PathVariable("taskNo") long taskNo,
		        @ModelAttribute TaskVO task,
		        RedirectAttributes redirectAttributes,
		        Principal principal
		) {
			task.setTaskNo(taskNo);
			
		    String empId = principal.getName();
		    TaskVO original = taskService.selectTaskDetail(task.getTaskNo());

		    if (!empId.equals(original.getEmpId()) && !empId.equals(original.getProjectEmpId())) {
		        redirectAttributes.addFlashAttribute("error", "수정 권한이 없습니다.");
		        return "redirect:/" + companyNo + "/task/" + task.getTaskNo();
		    }

		    taskService.updateTask(task);
		    return "redirect:/" + companyNo + "/task/" + task.getTaskNo();
		}

}
