package kr.or.ddit.logging;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class LoggingTest {
//	final Logger log = LoggerFactory.getLogger(LoggingTest.class);
	
	@Test
	void test() {
		System.out.println("출력 텍스트");
		log.debug("debug 등급으로 기록한 로그");
		int number = 34;
		log.info(" number 의 값 : {}", number);
	}

}
