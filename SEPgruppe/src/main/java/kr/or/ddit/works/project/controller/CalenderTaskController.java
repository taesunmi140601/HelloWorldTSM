package kr.or.ddit.works.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.ddit.works.project.service.CalenderTaskService;
import kr.or.ddit.works.project.vo.TaskVO;
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
@RequestMapping("/{companyNo}/calendertask")
public class CalenderTaskController {
	
	@Autowired
	private CalenderTaskService calenderTaskService;
	
	 @GetMapping("")
	 public String selectListCalenderTask(
		        @PathVariable("companyNo") String companyNo,
		        Model model) {
		 List<TaskVO> taskList = calenderTaskService.selectListCalenderTask();
		 
		 if (taskList == null || taskList.isEmpty()) {
			 System.out.println("일감이 없습니다");
		 } else {
			 System.out.println("일감이 있습니다");
		 }
		 
		 System.out.println("====================================");
		 for(int i=0;i<taskList.size();i++) {
			 System.out.println(taskList.get(i));
			 
		 }
		 
		 model.addAttribute("taskList", taskList);
		 model.addAttribute("companyNo", companyNo);
		 return "gw:calendertask/calenderTaskList";
	 }
}
