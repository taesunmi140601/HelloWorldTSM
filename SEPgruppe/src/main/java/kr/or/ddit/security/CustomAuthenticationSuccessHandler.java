package kr.or.ddit.security;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import kr.or.ddit.works.login.vo.AllUserVO;
import kr.or.ddit.works.mail.exception.NeedOAuthRedirectException;
import kr.or.ddit.works.mail.service.MailService;
import kr.or.ddit.works.organization.vo.EmployeeVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
	
    private MailService mailService;

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                         Authentication authentication) throws IOException, ServletException {

        RealUserWrapper user = (RealUserWrapper) authentication.getPrincipal();
        AllUserVO allUser = user.getRealUser(); 

        if (allUser instanceof EmployeeVO) {
            String empId = ((EmployeeVO) allUser).getEmpId();

            try {
                // 자동 인증 시도
                mailService.tryAutoAuth(empId);
            } catch (NeedOAuthRedirectException e) {
                // 최초 연동 안 된 경우 Gmail 인증 화면으로 리디렉션
                getRedirectStrategy().sendRedirect(request, response, "/mail/oauth/start?empId=" + empId);
                return;
            }
        }

        // 자동 인증 또는 연동된 사용자만 여기 도달함
        super.onAuthenticationSuccess(request, response, authentication);
    }
	
	@Override
	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
	                                    Authentication authentication) {
	   
	    Object principal = authentication.getPrincipal();

	    if (principal instanceof RealUserWrapper) {
	        RealUserWrapper user = (RealUserWrapper) principal;
	        AllUserVO allUser = user.getRealUser(); 

	        
	        String companyNo = null;
	        if (allUser instanceof EmployeeVO) { 
	            EmployeeVO employee = (EmployeeVO) allUser; 
	            companyNo = employee.getCompanyNo(); 
	            
	        } else {
	            log.warn("로그인한 사용자가 EMPLOYEE가 아니므로 companyNo 없음");
	        }

	        // 권한 확인
	        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
	        boolean isEmployee = false;
	        boolean isCompany = false;
	        boolean isProvider = false;
	        log.info("현재 로그인된 사용자 권한: {}", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
	        for (GrantedAuthority authority : authorities) {
	            if (authority.getAuthority().equals("EMPLOYEE")) isEmployee = true;
	            if (authority.getAuthority().equals("COMPANY")) isCompany = true;
	            if (authority.getAuthority().equals("PROVIDER")) isProvider = true;
	        }
	        
	        log.info(companyNo);
	        if (isEmployee) {
	            return "/"+ companyNo + "/groupware";
	        } else if (isCompany) {
	            return "/";
	        } else if (isProvider) {
	            return "/";
	        }
	    }

	    return "/"; // 기본값
	}
}
