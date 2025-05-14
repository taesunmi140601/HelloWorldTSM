package kr.or.ddit.works.vote.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/{companyNo}/vote")
public class VoteController {

	/**
	 * 투표 목록 조회
	 * @param companyNo 고객사 관리
	 * @param model
	 * @return
	 */
	@GetMapping("")
	public String selectListAllVote(
		@Param("companyNo") String companyNo
		, Model model
	) {
		model.addAttribute("companyNo", companyNo);
		
		return "gw:vote/voteList";
	}
}
