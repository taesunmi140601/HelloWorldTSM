package kr.or.ddit.works.approval.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
@RequestMapping("/{companyNo}/test")
public class TestController {
	
	@GetMapping("")
	public String selectAddress(
			@PathVariable("companyNo") String companyNo
			, Model model
	) {
		model.addAttribute("companyNo", companyNo);
		return "gw:approval/aprvlLine";
	}
}
