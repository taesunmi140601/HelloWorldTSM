package kr.or.ddit.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import kr.or.ddit.security.CustomAuthenticationSuccessHandler;
import kr.or.ddit.works.mail.service.MailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
    private MailService mailService;
	
	@Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        CustomAuthenticationSuccessHandler handler = new CustomAuthenticationSuccessHandler();
        handler.setMailService(mailService); // ✅ 여기서 꼭 주입
        return handler;
    }
	
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

//	@Bean
//	public UserDetailsService userDetailsService() {
//		UserDetails test001 = User.builder()
//			.username("test001")
//			.password(passwordEncoder().encode("java"))
//			.roles("USER")
//			.build();
//		UserDetails admin001 = User.builder()
//				.username("admin001")
//				.password(passwordEncoder().encode("java"))
//				.roles("USER", "ADMIN")
//				.build();
//
//		return new InMemoryUserDetailsManager(test001, admin001);
//	}

    /**
     * 인증(AuthenticationManager)과 인가(AuthorizationManager)를 지원하는 필터 체인 구조 형성.
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(customizer->customizer.disable())
            .authorizeHttpRequests(authorize -> 
            	authorize
//            		.requestMatchers(new AntPathRequestMatcher("/member/memberInsert.do")).permitAll()
//            		.requestMatchers(new AntPathRequestMatcher("/member/memberList.do")).hasRole("ADMIN")
//            		.requestMatchers(new AntPathRequestMatcher("/member/memberDetail.do")).hasRole("ADMIN")
//            		.requestMatchers(new AntPathRequestMatcher("/member/**")).authenticated()
//            		.requestMatchers(new AntPathRequestMatcher("/mypage")).authenticated()
//            		.requestMatchers(new AntPathRequestMatcher("/prod/**", "POST")).hasRole("ADMIN")
//            		.requestMatchers(new AntPathRequestMatcher("/buyer/**")).hasRole("ADMIN")
                	.anyRequest().permitAll()
            )
            .formLogin(customizer->
            	customizer.loginPage("/login")
            			.loginProcessingUrl("/login/loginProcess")
            			.usernameParameter("userId")
            			.passwordParameter("userPw")
            			.successHandler(customAuthenticationSuccessHandler())
            			.permitAll()
           ).logout(customizer->
           		customizer.logoutUrl("/login/logout")
           				.logoutSuccessUrl("/")
           	)
            ;
        return http.build();
    }

}










