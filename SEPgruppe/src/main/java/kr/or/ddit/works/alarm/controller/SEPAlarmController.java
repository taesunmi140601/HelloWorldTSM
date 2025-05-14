package kr.or.ddit.works.alarm.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.ddit.works.alarm.service.OneSignalService;

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
@Controller
@RequestMapping("/alarm")
public class SEPAlarmController {

	@Autowired
    private OneSignalService oneSignalService;
	
	@GetMapping("")
	public String test() {
		return "test";
	}
	
	 	@GetMapping("/send")
	    public String sendAlarmForm(
	    ) {
	        return "sepgruppe/alarm/alarmSendForm";
	    }
	 	
//	 	@GetMapping("/send")
//	 	@ResponseBody
//	 	public String sendAlarmForm(
//	 			@RequestParam String message,
//	 			@RequestParam String playerId
//	 			) {
//	 		oneSignalService.sendNotification(message, Collections.singletonList(playerId));
//	 		return "푸시 알림을 전송했습니다. 콘솔 로그를 확인하세요!";
//	 	}
}
