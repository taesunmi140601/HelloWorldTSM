package kr.or.ddit.works.mail.controller;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.security.RealUserWrapper;
import kr.or.ddit.works.login.vo.AllUserVO;
import kr.or.ddit.works.mail.service.GmailOAuthService;
import kr.or.ddit.works.organization.vo.EmployeeVO;


@Controller
@RequestMapping("/mail/oauth")
public class MailOAuthController {

	@Inject
	private GmailOAuthService gmailOAuthService;
	
    @GetMapping("/start")
    public void redirectToGmailAuth(@RequestParam String empId, HttpServletResponse response) throws IOException {
        String redirectUrl = gmailOAuthService.getAuthorizationUrl(empId);
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/callback")
    public String handleOAuthCallback(@RequestParam String code, @RequestParam String state
    	, Authentication authentication
    ) {
        gmailOAuthService.processOAuthCallback(code, state);
        String companyNo = null;

        if (authentication.getPrincipal() instanceof RealUserWrapper wrapper) {
            AllUserVO user = wrapper.getRealUser();
            if (user instanceof EmployeeVO employee) {
                companyNo = employee.getCompanyNo();
            } 
        }
        return "redirect:/" + companyNo + "/groupware"; // 인증 완료 후 메인으로
    }
}
