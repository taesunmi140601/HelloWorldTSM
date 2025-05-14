package kr.or.ddit.works.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/{companyNo}/testProject")
public class TestProjectController {
	@GetMapping("")
	public String selectAddress(
			@PathVariable("companyNo") String companyNo
			, Model model
	) {
		model.addAttribute("companyNo", companyNo);
		return "gw:project/projectForm";
	}
}
