package kr.or.ddit.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

// 코딩 이해 필요...

@Configuration
@EnableScheduling
public class SchedulingConfig {
	
	@Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);  // 최대 10개의 스레드 사용
        scheduler.setThreadNamePrefix("scheduled-task-");  // 스레드 이름 접두사 설정
        scheduler.setWaitForTasksToCompleteOnShutdown(true); // 서버 종료 시, 대기 중인 작업이 완료될 때까지 대기
        scheduler.setAwaitTerminationSeconds(60); // 서버 종료 후 최대 대기 시간 (초)
        return scheduler;
    }

}
