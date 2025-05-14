package kr.or.ddit.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import kr.or.ddit.CustomRootContextConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CustomRootContextConfig
class PasswordEncoderTest {

	@Autowired
	PasswordEncoder encoder;
	
	@Test
	void test() {
		String plain = "java";
		String encoded = encoder.encode(plain);
		log.info("encoded : {}", encoded);
	}

}
