package kr.or.ddit.works.project.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.works.project.service.GanttChartTaskService;
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
@RequestMapping("/{companyNo}/ganttcharttask")
public class GanttChartTaskController {

    @Autowired
    private GanttChartTaskService ganttChartTaskService;

    @GetMapping("")
    public String selectListGanttChart(
            @PathVariable("companyNo") String companyNo,
            @RequestParam(required = false) Long projectNo,
            Model model) {
    	
    	
        // 업무 목록 가져오기
        List<TaskVO> gantTaskList = ganttChartTaskService.selectListGanttChart(projectNo);

        // 날짜 포맷터
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        // 모델에 데이터 추가
        List<Map<String, Object>> gantTaskListWithDates = gantTaskList.stream()
            .map(task -> {
                Map<String, Object> taskMap = new HashMap<>();
                taskMap.put("taskNo", task.getTaskNo());
                taskMap.put("taskTitle", task.getTaskTitle());
                taskMap.put("taskStartDate", task.getTaskStartDate() != null ? sdf.format(task.getTaskStartDate()) : null);
                taskMap.put("taskEndDate", task.getTaskEndDate() != null ? sdf.format(task.getTaskEndDate()) : null);
                taskMap.put("progress", Integer.parseInt(task.getProgress()));
                return taskMap;
            })
            .collect(Collectors.toList());

        model.addAttribute("gantTaskList", gantTaskListWithDates);
        model.addAttribute("companyNo", companyNo);
        model.addAttribute("projectNo", projectNo);

        return "gw:ganttcharttask/ganttchartTaskList"; // 간트 차트 JSP 페이지로 이동
    }

}







