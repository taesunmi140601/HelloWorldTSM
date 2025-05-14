package kr.or.ddit.works.approval.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/{companyNo}/content")
public class ContentPopupController {

	@GetMapping
	public String content() {
		return "groupware/approval/dummy";
	}
}
