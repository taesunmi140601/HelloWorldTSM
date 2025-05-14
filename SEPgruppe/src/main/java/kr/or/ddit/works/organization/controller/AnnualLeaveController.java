package kr.or.ddit.works.organization.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.ddit.security.RealUserWrapper;
import kr.or.ddit.works.approval.service.ApprDocService;
import kr.or.ddit.works.approval.vo.AprvlDocVO;
import kr.or.ddit.works.login.vo.AllUserVO;
import kr.or.ddit.works.mybatis.mappers.AnnualLeaveMapper;
import kr.or.ddit.works.organization.service.AnnualLeaveService;
import kr.or.ddit.works.organization.service.EmployeeService;
import kr.or.ddit.works.organization.vo.AnnualLeaveVO;
import kr.or.ddit.works.organization.vo.AnnualVO;
import kr.or.ddit.works.organization.vo.EmployeeVO;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author JSW
 * @since 2025. 3. 19.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 19.     	JSW	          최초 생성
 *
 * </pre>
 */
@Controller
@Slf4j
@RequestMapping("{companyNo}/annual")
public class AnnualLeaveController {

	@Autowired
	private AnnualLeaveService service;

	@Autowired
	private EmployeeService empService;
	
	@Autowired
	private AnnualLeaveMapper annualLeaveMapper;
	
	@Autowired
	private ApprDocService apprDocService;

	@GetMapping
	public String selectListAllAnnual(@PathVariable("companyNo") String companyNo) {
		return "gw:annual/annualList";
	}

	@GetMapping("/myannual")
	public String selectMyAnnual(
			@PathVariable("companyNo") String companyNo
			, Authentication authentication
			, Model model
			) {

			EmployeeVO member = empService.selectEmployee(authentication.getName());

			 // 연차 통계
		    List<AnnualLeaveVO> annualList = service.selectAnnual(member.getEmpId());
		    model.addAttribute("annualList", annualList);

		    // 연차 신청서 문서 중 결재 완료
		    List<AprvlDocVO> usedDocs = service.getApprovedAnnualDocs(member.getEmpId());

		    // 연차 신청서 문서 중 결재 대기 중
		    List<AprvlDocVO> draftDocs = service.getPendingAnnualDocs(member.getEmpId());

		    model.addAttribute("usedDocs", usedDocs);
		    model.addAttribute("draftDocs", draftDocs);

		    // 모델에 등록
		    model.addAttribute("companyNo", companyNo);
		    model.addAttribute("member", member);

	        return "gw:annual/myAnnual";
	}
	
	/**
	 * 연차신청서 옵션 조회
	 * @return
	 */
	@GetMapping("annualOption")
	@ResponseBody
	public List<AnnualVO> selectAnnualOption() {
		List<AnnualVO> option = annualLeaveMapper.selectAnnualOption();
		log.info("option: {}", option);
		return option;
	}
}
