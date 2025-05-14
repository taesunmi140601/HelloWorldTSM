package kr.or.ddit.works.login.controller;


import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.works.company.vo.CompaniesVO;
import kr.or.ddit.works.login.service.LoginService;
import kr.or.ddit.works.login.vo.AllUserVO;
import kr.or.ddit.works.mybatis.mappers.LoginMapper;

/**
 * 로그인 컨트롤러
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
@RequestMapping("/login")
public class LoginController {

	// 로그인 폼 이동
	@GetMapping("")
	public String loginFormUI(Model model) {
		model.addAttribute("currentPage", "login"); // detailHeader 동적으로 변경
		return "sep:login/loginForm";
	}

	@Autowired
	private LoginService service;

	@Inject
	private PasswordEncoder passwordEncoder;


	// 아이디 찾기
	@GetMapping("findId")
	public String findIdForm() {
		return "sep:login/findId";
	}

	@PostMapping("/findId")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> findIdProcess(@RequestBody CompaniesVO company) {
		Map<String, Object> response = new HashMap<>();

		CompaniesVO foundAccount = service.findAccount(company); // 조회된 계정 정보
		if (foundAccount != null && foundAccount.getContactId() != null) {
			response.put("success", true);
			response.put("contactId", foundAccount.getContactId());
		} else {
			response.put("success", false);
			response.put("message", "해당 정보로 등록된 아이디를 찾을 수 없습니다.");
		}

		return ResponseEntity.ok(response);
	}

	// 비밀번호 찾기
	@GetMapping("findPw")
	public String findPwForm() {
		return "sep:login/findPw";
	}

	@PostMapping("findPw")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> findPwProcess(
		@RequestBody CompaniesVO company
		, HttpSession session
	) {
		Map<String, Object> response = new HashMap<>();

		CompaniesVO foundAccount = service.findAccount(company); // 조회된 계정 정보
		if (foundAccount != null) {
			response.put("success", true);
			response.put("contactId", foundAccount.getContactId());
			session.setAttribute("company", foundAccount);
		} else {
			response.put("success", false);
			response.put("message", "해당 정보로 등록된 아이디를 찾을 수 없습니다.");
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping("/updatePw")
	public String updatePw(
			@RequestParam String contactPw
			, RedirectAttributes redirectAttributes
			, HttpSession session
			, Model model) {
		CompaniesVO company = (CompaniesVO) session.getAttribute("company");
		String encodedPassword = passwordEncoder.encode(contactPw);

		company.setContactPw(encodedPassword);
		boolean isUpdated = service.updateCompany(company);
		session.removeAttribute("company");

		if (isUpdated) {
			redirectAttributes.addFlashAttribute("message", "비밀번호가 성공적으로 변경되었습니다!");
			return "redirect:/login"; // 로그인 페이지로 이동
		} else {
			redirectAttributes.addFlashAttribute("error", "비밀번호 변경에 실패했습니다.");
			return "redirect:/login/findPw"; // 다시 입력 페이지로 이동
		}
	}

}
