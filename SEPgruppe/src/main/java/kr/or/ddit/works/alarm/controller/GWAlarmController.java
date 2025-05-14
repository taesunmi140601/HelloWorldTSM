package kr.or.ddit.works.alarm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.ddit.works.alarm.service.OneSignalService;
import kr.or.ddit.works.alarm.vo.AlarmHistoryVO;
import kr.or.ddit.works.approval.vo.AprvlDocVO;
import kr.or.ddit.works.approval.vo.AprvlLineVO;
import kr.or.ddit.works.notice.vo.NoticeVO;
import kr.or.ddit.works.project.vo.ProjectVO;
import kr.or.ddit.works.project.vo.TaskVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 알람 컨트롤러
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
@RequiredArgsConstructor
@RequestMapping("/{companyNo}/alarm")
@Controller
@Slf4j
public class GWAlarmController {

	@Autowired
    private OneSignalService oneSignalService;
	
	private final SimpMessagingTemplate messagingTemplate;
	
	@GetMapping("")
	public String alarmHome(@PathVariable("companyNo") String companyNo,
				            Authentication authentication,
				            Model model) {
		
		return "gw:alarm/alarmHome";
		
	}
	
 	@PostMapping("/send/alarm")
 	public void sendAlarm(@RequestBody AlarmHistoryVO alarm) {
 		messagingTemplate.convertAndSend("/topic/alarm/" + alarm.getEmpId(), alarm);
 	}
 	
 	public void sendProjectAlarm(ProjectVO project, String receiverEmpId) {
 	    AlarmHistoryVO alarm = new AlarmHistoryVO();
 	    alarm.setAlarmNm(project.getProjectTitle());
 	    alarm.setEmpId(receiverEmpId);
 	    alarm.setAlarmCategoryNo(project.getProjectNo());

 	    // 전송 경로: /topic/project/{userId}
 	    messagingTemplate.convertAndSend("/topic/project/" + receiverEmpId, alarm);
 	}
 	
 	public void sendTaskAlarm(TaskVO task, String receiverEmpId, long taskNo) {
 		AlarmHistoryVO alarm = new AlarmHistoryVO();
 		alarm.setAlarmNm(task.getProjectTitle());
 		alarm.setAlarmContent(task.getTaskTitle());
 		alarm.setEmpId(receiverEmpId);
 		alarm.setAlarmCategoryNo(taskNo);
 		
 		messagingTemplate.convertAndSend("/topic/task/" + receiverEmpId, alarm);
 	}
 	
 	public void sendApprAlarm(AprvlDocVO aprvlDoc, AprvlLineVO receiver) {
 		AlarmHistoryVO alarm = new AlarmHistoryVO();
 		alarm.setAlarmNm(aprvlDoc.getAprvlDocTitle());
 		alarm.setEmpId(receiver.getEmpId());
 		alarm.setAlarmContent(receiver.getAprvlDocNo());
 		log.info("==========================="+receiver.getAprvlDocNo());
 		messagingTemplate.convertAndSend("/topic/appr/" + receiver.getEmpId(), alarm);
 	}
 	
 	public void sendCommunityAlarm(Long communityNo, String receiverEmpId) {
 		AlarmHistoryVO alarm = new AlarmHistoryVO();
 		alarm.setEmpId(receiverEmpId);
 		alarm.setAlarmCategoryNo(communityNo);
 		messagingTemplate.convertAndSend("/topic/community/" + receiverEmpId, alarm);
 	}
 	
 	public void sendCommunityAlarm2(Long communityNo, String receiverEmpId) {
 		AlarmHistoryVO alarm = new AlarmHistoryVO();
 		alarm.setEmpId(receiverEmpId);
 		alarm.setAlarmCategoryNo(communityNo);
 		messagingTemplate.convertAndSend("/topic/community2/" + receiverEmpId, alarm);
 	}
 	
 	public void sendNoticeAlarm(NoticeVO notice, String receiverEmpId) {
 		AlarmHistoryVO alarm = new AlarmHistoryVO();
 		alarm.setEmpId(receiverEmpId);
 		alarm.setAlarmCategoryNo((long) notice.getNoticeNo());
 		alarm.setAlarmNm(notice.getNoticeTitle());
 		messagingTemplate.convertAndSend("/topic/notice/" + receiverEmpId, alarm);
 	}
 	
 	
 	
 	
 	
 	
 	
 	
 	
 	
 	
 	
}










