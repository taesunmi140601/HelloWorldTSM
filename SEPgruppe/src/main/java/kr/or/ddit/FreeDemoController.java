package kr.or.ddit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FreeDemoController {
	
	@GetMapping("/freedemo")
	public String freedemo() {
		return "sep:freedemo/freeDemo";
	}
}
