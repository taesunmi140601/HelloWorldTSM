package kr.or.ddit.works.company.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.security.CustomUserDetailService;
import kr.or.ddit.validate.InsertGroup;
import kr.or.ddit.works.company.service.CompanyService;
import kr.or.ddit.works.company.vo.CompaniesVO;
import kr.or.ddit.works.subscription.service.PaymentService;
import kr.or.ddit.works.subscription.service.SubScriptionService;
import kr.or.ddit.works.subscription.vo.PaymentsVO;
import kr.or.ddit.works.subscription.vo.SubscriptionsVO;
import lombok.extern.slf4j.Slf4j;

/**
 * 회사 컨트롤러
 * @author JYS
 * @since 2025. 3. 14.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 14.     	JYS	         	최초 생성
 *  2025. 3. 18.		손현진			회원가입 기능 추가 중..
 *  2025. 4.  9.		SJH				수정 중
 *
 * </pre>
 */
@Slf4j
@Controller
@RequestMapping("/company")
public class CompanyController {

	public static final String MODELNAME = "company";
	private int number;

	@Inject
	CompanyService service;

	@Inject
	public SubScriptionService subService;
	
	@Inject
	private PaymentService paymentService;
	
	@Autowired
	private CustomUserDetailService userDetailsService;



	// provicer의 고객사 관리 - 고객사 전체 목록 조회
	@GetMapping("")
	public String selectListAllCompany(Model model) {
		model.addAttribute("currentPage", "customer"); // detailHeader 동적으로 변경
		
		// 구독 리스트 함께 조회
		List<SubscriptionsVO> subscriptions = subService.subscriptionList();
		
		// 결제 내역을 각 구독 객체에 주입 
	    for (SubscriptionsVO sub : subscriptions) {
	        List<PaymentsVO> payments = paymentService.selectPaymentsBySubscriptionNo(sub.getSubscriptionNo());
	        sub.setPayment(payments);
	    }
		
		// 날짜 파싱 처리
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (SubscriptionsVO vo : subscriptions) {
		    try {
		        if (vo.getSubscriptionStart() != null) {
		            vo.setSubscriptionStartDate(sdf.parse(vo.getSubscriptionStart()));
		        }
		        if (vo.getSubscriptionEnd() != null) {
		            vo.setSubscriptionEndDate(sdf.parse(vo.getSubscriptionEnd()));
		        }
		    } catch (ParseException e) {
		        log.error("날짜 파싱 오류: {}", e.getMessage());
		    }
		}
		
		model.addAttribute("subscriptions", subscriptions);
		
		return "sep:admin/company/companyList";
	}

	// provider 고객사 관리 - 고객사 상세 조회
	@GetMapping("{companyNo}")
	public String selectCompanyDetail(@PathVariable("companyNo") String companyNo) {
		return "sep:admin/company/companyDetail";
	}

	// 고객사 회원가입
	@GetMapping("new")
	public String insertCompanyFormUI() {
		return "/sepgruppe/company/companyForm";
	}

	// 아이디 중복 확인
	@GetMapping("/checkId")
	@ResponseBody
	public boolean checkId(@RequestParam("contactId") String contactId) {
	    return service.existsByContactId(contactId);
	}
	// 사업자등록번호 중복 확인
	@GetMapping("/checkBusinessRegNo")
	@ResponseBody
	public boolean checkBusinessRegNo(@RequestParam("businessRegNo") String businessRegNo) {
	    return service.existsByBusinessRegNo(businessRegNo);
	}

	@PostMapping("new")

	public String insertCompany(
			@Validated(InsertGroup.class) @ModelAttribute(MODELNAME) CompaniesVO company
			, BindingResult errors
			, RedirectAttributes redirectAttributes
	) {
		String logicalName = null;
		boolean valid = !errors.hasErrors();
		if(valid) {
//		2) 검증 통과
//			a) 등록(createMember) 처리
			service.insertCompany(company);
//			b) 등록 성공 : 로그인 페이지로 이동(redirect)
				logicalName = "redirect:/login";
		}else {
//		1) 검증 실패
//			: 가입 양식으로 다시 이동(기존 입력 데이터와 검증 결과 메시지를 전달).
//				dispatch ---> redirect
			redirectAttributes.addFlashAttribute(MODELNAME, company);
			String errorName = BindingResult.MODEL_KEY_PREFIX + MODELNAME;
			redirectAttributes.addFlashAttribute(errorName, errors);

			logicalName = "redirect:/company/new";
		}
		return logicalName;
	}

	@PostMapping("/mailAuth")
	@ResponseBody
    public HashMap<String, Object> mailAuth(String mail) {
        HashMap<String, Object> map = new HashMap<>();

        try {
            number = service.MailAuth(mail);
            String num = String.valueOf(number);

            map.put("success", Boolean.TRUE);
            map.put("number", num);
        } catch (Exception e) {
            map.put("success", Boolean.FALSE);
            map.put("error", e.getMessage());
        }

        return map;
    }

	@GetMapping("/mailCheck")
	@ResponseBody
    public ResponseEntity<?> mailCheck(@RequestParam String userNumber) {

        boolean isMatch = userNumber.equals(String.valueOf(number));

        return ResponseEntity.ok(isMatch);
    }

	@PostMapping("/edit")
	public String editCompany(
		@ModelAttribute("company") CompaniesVO company
		, Model model
	) {

		return null;
	}

	// 고객사정보 수정폼으로 이동
	@GetMapping("/{companyNo}/mypage")
	public String updateCompanyFormUI(
			@PathVariable("companyNo") String companyNo
			, Authentication authentication
			, Model model
		) {
			CompaniesVO member = service.selectCompany(authentication.getName());
			String contactId = authentication.getName();
			SubscriptionsVO subscription = subService.selectSubscription(contactId);
			
			model.addAttribute("member", member);
			model.addAttribute("subscription", subscription);
			log.info("정보수정폼=========================================================");
		return "sep:user/company/companyEdit";
	}

	// 고객사 정보 수정
	@PostMapping("/{companyNo}/edit")
	public String updateCompany(
		Authentication authentication
		, @PathVariable("companyNo") String companyNo
		, @ModelAttribute("member") CompaniesVO member

	) {
		String contactId = authentication.getName();
		member.setContactId(contactId);
		service.updateCompany(member);
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(contactId);

	    UsernamePasswordAuthenticationToken authToken =
	        new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

	    SecurityContextHolder.getContext().setAuthentication(authToken);

		log.info("=====================================정보수정 컨트롤러 ====================");
		return "redirect:/company/{companyNo}/mypage";
	}

	@PostMapping("/mypage/verifyPassword")
	public ResponseEntity<Object> verifyPassword(
			Authentication authentication
			, @RequestBody CompaniesVO company
		) {
	    String empId = authentication.getName();

	    boolean checkPw = service.authenticateMember(empId, company.getContactPw()); // 비밀번호 검증 호출

	        // 비밀번호 인증 로직 호출
	    if(checkPw)
	        return ResponseEntity.ok().build(); // 인증 성공
	    else
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 인증 실패
	}

	// 고객사 탈퇴처리 - 비활성화 (소프트 삭제)
	@PutMapping("/{companyNo}/deactivate")
	public String deactivateCompany(@PathVariable("companyNo") String companyNo) {
		// 탈퇴 후, 마이페이지로 리다이렉트
		return "redirect:/company/mypage";
	}

	// 비활성화 고객사 목록 조회
	@GetMapping("/deactiavateList")
	public String selectDeactivatedCompanies(Model model) {
		// 서비스에서 탈퇴된 고객사 목록을 가져옴
		// List<Company> deactivatedCompanies = companyService.getDeactivatedCompanies();
		// model.addAttribute("deactivatedCompanies", deactivatedCompanies);

		return "sep:admin/company/deactivatedCompanyList";  // 탈퇴된 고객사 목록 페이지
	}

	// 일괄 탈퇴 처리 (스케줄러나 관리자 페이지에서 실행)
	@PostMapping("/delete")
	public String bulkDeactivateCompanies() {
		// 서비스에서 일괄 탈퇴 처리 로직 수행
		// companyService.bulkDeactivateCompanies();

		// 일괄 처리 후, 고객사 목록 페이지로 리다이렉트
		return "redirect:/company";
	}
}
