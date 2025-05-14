package kr.or.ddit.works.provider.controller;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.ddit.works.company.service.CompanyService;
import kr.or.ddit.works.company.vo.CompaniesVO;
import kr.or.ddit.works.subscription.service.PaymentService;
import kr.or.ddit.works.subscription.service.SubScriptionService;
import kr.or.ddit.works.subscription.vo.PaymentsVO;
import kr.or.ddit.works.subscription.vo.SubscriptionsVO;

/**
 * 관리자 컨트롤러
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
@RequestMapping("/provider")
public class ProviderController {

	@Inject
	public PaymentService payService;
	
	@Inject
	public SubScriptionService subService;
	
	@Inject
	public CompanyService comService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	//관리자 페이지 이동
	@GetMapping("")
	public String providerFormUI(HttpSession session, Model model) throws JsonProcessingException {
		session.setAttribute("isAdmin", true); // 상단 메뉴바 동적으로 변경
		model.addAttribute("currentPage", "provider"); // detailHeader 동적으로 변경
		
		List<PaymentsVO> paymentList = payService.paymentList();
		String paymentListJson = objectMapper.writeValueAsString(paymentList);
	    model.addAttribute("paymentListJson", paymentListJson);
	    
	    List<SubscriptionsVO> subscriptionList = subService.subscriptionList();
	    String subscriptionListJson = objectMapper.writeValueAsString(subscriptionList);
	    model.addAttribute("subscriptionListJson", subscriptionListJson);
	    
	    List<CompaniesVO> companyList = comService.companyList();
	    String companyListJson = objectMapper.writeValueAsString(companyList);
	    model.addAttribute("companyListJson", companyListJson);
	    
		return "sep:provider/providerSettingForm";
	}

}
