package kr.or.ddit.works.mybatis.mappers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.or.ddit.CustomRootContextConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CustomRootContextConfig
class CommunityMapperTest {

	@Autowired
	private CommunityMapper mapper;

	@Test
	void test() {
		Long no = 1L;
		mapper.boardList(no);
	}

}
