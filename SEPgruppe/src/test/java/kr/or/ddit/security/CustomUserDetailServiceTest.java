package kr.or.ddit.security;

import static org.junit.jupiter.api.Assertions.*;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import kr.or.ddit.CustomRootContextConfig;
import kr.or.ddit.works.mybatis.mappers.LoginMapper;

@CustomRootContextConfig
class CustomUserDetailServiceTest {
	
	@Inject
	LoginMapper mapper;

	@Test
	void testLoadUserByUsername() {
		mapper.login("test001");
	}

}
