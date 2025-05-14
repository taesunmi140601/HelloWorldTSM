package kr.or.ddit.works.subscription.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.works.subscription.service.SubScriptionService;
import kr.or.ddit.works.subscription.vo.SubscriptionPlansVO;


/**
 * 구독 현황, 신청, 변경 목록 조회 컨트롤러
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
@RequestMapping("/subscription")
public class SubscriptionsController {
	
	@Autowired
	private SubScriptionService service;
	
	// 고객사 구독 현황 조회
	@GetMapping("")
	public String selectListAllSubscription() {
		return "sep:admin/subscription/subscriptionList";
	}
	
	// 고객사 구독 현황 상세 조회
	@GetMapping("/{subscriptionId}")
	public String selectSubscriptionDetail(@PathVariable("subscriptionId") String subscriptionId) {
		// subscriptionId에 해당하는 구독의 상세 정보를 조회하는 로직
		return "sep:admin/subscription/subscriptionDetail";
	}
	
	// 고객사 구독 신청 목록 조회
	@GetMapping("/request")
	public String selectListRequestSubscription() {
		return "sep:admin/subscription/subscriptionRequestList";
	}
		
	// 고객사 구독 변경 목록 조회
	@GetMapping("/change")
	public String selectListChangeSubscription() {
		return "sep:admin/subscription/subscriptionChangeList";
	}
	
	// 고객사 구독 신청 폼
	@GetMapping("/insert")
	public String insertSubscription(Model model, @RequestParam("what") String planType) {
		SubscriptionPlansVO plan = service.planOne(planType);
		model.addAttribute("plan", plan);
		return "sepgruppe/subscription/subscriptionInsertForm";
	}
}
