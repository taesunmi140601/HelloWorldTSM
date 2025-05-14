package kr.or.ddit.works.address.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.ddit.works.address.service.AddressbookService;
import kr.or.ddit.works.address.vo.AddressbookVO;

/**
 * 주소록 컨트롤러
 * @author JYS
 * @since 2025. 3. 15.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 15.     	JYS	          최초 생성
 *
 * </pre>
 */
@Controller
@RequestMapping("/{companyNo}/address")
public class AddressbookController {
	
	@Autowired
	private AddressbookService addressbookService;

	/**
	 * 주소록 목록 조회
	 * @param companyNo 고객사 아이디
	 * @param model
	 * @return
	 */
	@GetMapping("")
	public String selectListAllAddress(
			@PathVariable("companyNo") String companyNo
			, Model model
	) {
		List<AddressbookVO> addressList = addressbookService.selectAllListAddress();
	    model.addAttribute("addressList", addressList);
	    model.addAttribute("companyNo", companyNo);
	    return "gw:address/addressList";
	}

	/**
	 * 주소록 상세 조회
	 * @param companyNo 고객사 아이디
	 * @param adbkNo 주소록 번호
	 * @param model
	 * @return
	 */
	@GetMapping("/{adbkNo}")
	public String selectAddress(
			@PathVariable("companyNo") String companyNo
			, @PathVariable("adbkNo") int adbkNo
			, Model model
	) {
		model.addAttribute("companyNo", companyNo);
		model.addAttribute("adbkNo", adbkNo);
		return "gw:address/addressDetail";
	}
	
	
	/**
	 * 관리자 - 주소록 추가 폼
	 * @param companyNo 고객사 번호
	 * @param model
	 * @return
	 */
	@GetMapping("/new")
	public String insertAddressFormUI(
		@PathVariable("companyNo") String companyNo
		, Model model
	) {
		model.addAttribute("companyNo", companyNo);
		
		return "gw:address/addressForm";
		
	}
	
	/**
	 * 관리자 - 주소록 추가 폼
	 * @param companyNo 고객사 번호
	 * @param model
	 * @return
	 */
	/**
	 * 관리자 - 주소록 추가 처리
	 */
	@PostMapping("/new")
	public String insertAddress(
	    @PathVariable("companyNo") String companyNo,
	    AddressbookVO addressbookVO,
	    Model model
	) {
	    model.addAttribute("companyNo", companyNo);

	    // 👉 기본 값들 설정
	    addressbookVO.setCompanyNo(companyNo);  // companyNo 반드시 VO에 세팅
	    addressbookVO.setEmpId("admin");        // ★ 세션 사용자로 교체 예정
	    addressbookVO.setAdbkDelYn("N");        // 삭제 상태 초기화

	    boolean success = addressbookService.insertAddress(addressbookVO);

	    if (success) {
	        return "redirect:/" + companyNo + "/address";  // 성공 → 목록으로
	    } else {
	        model.addAttribute("errorMessage", "등록에 실패했습니다.");
	        return "gw:address/addressForm"; // 실패 → 다시 폼으로
	    }
	}
}
