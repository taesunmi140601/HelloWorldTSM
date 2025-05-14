package kr.or.ddit.works.subscription.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.ddit.works.subscription.service.SubScriptionService;
import kr.or.ddit.works.subscription.vo.SubscriptionPlansVO;

/**
 * 구독 플랜 컨트롤러
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
 *  2025. 4. 9.     	SJH	          구독 플랜 관리 및 변경 저장 처리 추가
 *
 * </pre>
 */
@Controller
@RequestMapping("/subscriptionPlan")
public class SubscriptionPlansController {
	
	@Autowired
	private SubScriptionService service;
	
	// 구독 플랜 목록 조회
	@GetMapping("")
	public String selectListAllSubscriptionPlan(Model model) {
		List<SubscriptionPlansVO> planList = service.readPlanList();
		model.addAttribute("planList", planList);
		model.addAttribute("currentPage", "subscription"); // detailHeader 동적으로 변경
		return "sep:subscription/subscriptionPlanList";
	}

	// 구독 플랜 상세 조회
	@GetMapping("{planType}")
	public String selectSubscriptionPlanDetail(@PathVariable String planType, Model model) {
		SubscriptionPlansVO plan = service.planOne(planType);
		model.addAttribute("plan", plan);
		return "sepgruppe/subscription/subscriptionPlanDetail";
	}

	// 구독 플랜 추가 폼
	@GetMapping("new")
	public String insertSubscriptionPlanFormUI() {
		return "sep:admin:subscription/subscriptionPlanForm";
	}

	// 구독 플랜 추가
	@PostMapping("new")
	public String insertSubscriptionPlan() {
		return "redirect:/subscriptionPlanList"; // 추가 성공 시 목록 조회로 이동
	}

	// 구독 플랜 수정 폼
	@GetMapping("{planNo}/edit")
	public String updateSubscriptionPlanFormUI(@PathVariable String planNo) {
		return "sep:admin:subscription/subscriptionPlanEdit";
	}
	
	// 구독 플랜 관리 
	@GetMapping("manage")
	public String manageSubscriptionPlans(Model model) {
		List<SubscriptionPlansVO> planList = service.readPlanList();
		model.addAttribute("planList", planList);
		return "sep:subscription/subscriptionPlanManage"; // JSP 페이지
	}

	// 구독 플랜 수정
	@PutMapping("{planNo}/edit")
	public String updateSubscriptionPlan(@PathVariable String planNo) {
		return "redirect:/subscriptionPlan/" + planNo; // 수정 후 상세 조회로 이동
	}

	// 구독 플랜 삭제 - 활성화 상태 변경
	@PutMapping("{planNo}/deactivate")
	public String deactivateSubscriptionPlan(@PathVariable String planNo
	) {
	    return "redirect:/subscriptionPlan";
	}
	
	// 구독 플랜 변경 저장 처리
	@PostMapping("/manage/save")
	public String saveSubscriptionPlanChanges(SubscriptionPlansVO wrapper) {
		for (SubscriptionPlansVO plan : wrapper.getPlans()) {
			service.updatePlanInfo(plan); // 가격 및 인원 업데이트
		}
		return "redirect:/subscriptionPlan/manage";
	}
}
