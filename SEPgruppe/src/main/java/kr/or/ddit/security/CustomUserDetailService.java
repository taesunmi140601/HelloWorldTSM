package kr.or.ddit.security;

import javax.inject.Inject;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import kr.or.ddit.works.login.vo.AllUserVO;
import kr.or.ddit.works.mybatis.mappers.LoginMapper;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomUserDetailService implements UserDetailsService{
	
	@Inject
	public LoginMapper mapper;
	
	@Inject
	private PasswordEncoder passwordEncoder; // PasswordEncoder 주입
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AllUserVO user = mapper.login(username);
		if(user==null) throw new UsernameNotFoundException(String.format("%s 사용자 없음.", username));
		
		// 로그 확인
				log.info("🔍 로그인 시도 - ID: {}", username);
				log.info("🔑 DB에서 조회된 비밀번호: {}", user.getUserPw());

				// 비밀번호 암호화 체크 (선택적)
				if (!user.getUserPw().startsWith("{bcrypt}")) {
					log.warn("⚠ DB에 저장된 비밀번호가 암호화되지 않았습니다.");
					user.setUserPw(passwordEncoder.encode(user.getUserPw()));  // 암호화 적용
				}
		
		return new RealUserWrapper(user);
	}

}















