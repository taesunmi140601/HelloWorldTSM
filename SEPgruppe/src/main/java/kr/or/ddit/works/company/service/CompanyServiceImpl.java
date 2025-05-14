package kr.or.ddit.works.company.service;

import java.io.File;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.security.CustomUserDetailService;
import kr.or.ddit.works.attachFile.vo.AttachFileVO;
import kr.or.ddit.works.company.vo.CompaniesVO;
import kr.or.ddit.works.mybatis.mappers.CompanyMapper;
import kr.or.ddit.works.organization.vo.EmployeeVO;
import lombok.extern.slf4j.Slf4j;

/**
 * 회사 서비스 임플
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
@Slf4j
@Service
public class CompanyServiceImpl implements CompanyService {

	@Inject
	public JavaMailSender javaMailSender;
	private static final String senderEmail= "thszz12345@gmail.com";
	@Inject
	public CompanyMapper mapper;

	// 비밀번호 암호화 Update
	@Inject
	public PasswordEncoder passwordEncoder;

	private static int number;

    @Autowired
    private CustomUserDetailService userDetailService;


	@Override
	public boolean existsByContactId(String contactId) {
		return mapper.countByContactId(contactId) > 0;
	}

	@Override
	public boolean existsByBusinessRegNo(String businessRegNo) {
		return mapper.countByBusinessRegNo(businessRegNo) > 0;
	}

	@Override
	public void insertCompany(CompaniesVO company) {
		String encodedPassword = passwordEncoder.encode(company.getContactPw());
		company.setContactPw(encodedPassword);
		mapper.insertCompany(company);
	}

	public static void createNumber(){
        number = (int)(Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
    }

	public MimeMessage CreateMail(String mail){
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

	@Override
	public int MailAuth(String mail) {
		MimeMessage message = CreateMail(mail);
        javaMailSender.send(message);

        return number;
	}
	// 마이페이지 정보조회
	@Override
	public CompaniesVO selectCompany(String contactId) {
		return mapper.selectCompany(contactId);
	}

	// 마이페이지 정보 수정
	@Override
	public boolean updateCompany(CompaniesVO member) {
		String plain = member.getContactPw();

		if(plain != null && !plain.isEmpty()) {
			String encoded = passwordEncoder.encode(plain);
			member.setContactPw(encoded);
		}else {
			member.setContactPw(member.getContactPw());
		}

		boolean result = mapper.updateCompany(member) > 0;

		if(result) {
			createNewAuthentication();
			return true;
		}else {
			return false;
		}
	}

	// 마이페이지 정보수정 비밀번호 인증
	@Override
	public boolean authenticateMember(String contactId, String contactPw) {
		String realPw = mapper.selectCompany(contactId).getContactPw();
		return passwordEncoder.matches(contactPw, realPw);
	}

	// 변경된 정보로 새로운 인증 생성 (패스워드를 변경할 경우 새롭게 인증정보가 업데이트 되어야 함)
    private void createNewAuthentication() {
        Authentication beforeAuth = SecurityContextHolder.getContext().getAuthentication();

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UserDetails principal = userDetailService.loadUserByUsername(beforeAuth.getName());
        Collection<? extends GrantedAuthority> authorities = principal.getAuthorities();
        Object details = beforeAuth.getDetails();

        // 새로운 인증 토큰 생성 (비밀번호는 null로 설정)
        UsernamePasswordAuthenticationToken newAuthentication =
                UsernamePasswordAuthenticationToken.authenticated(principal, null, authorities);

        newAuthentication.setDetails(details);

        context.setAuthentication(newAuthentication);
        SecurityContextHolder.setContext(context);
    }

	@Override
	public List<CompaniesVO> companyList() {
		return mapper.companyList();
	}


}
