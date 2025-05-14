package kr.or.ddit.works.consultation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 상담 컨트롤
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
@RequestMapping("/consultation")
public class ConsultationController {
	
	// 상담 요청 내역 조회
	@GetMapping("request")
	public String selectListAllConsultationRequest() {
		return "sep:admin/consultation/ConsultationRequestList";
	}
	
	// 회사별 상담 기록 조회 ??? 
	 
}
