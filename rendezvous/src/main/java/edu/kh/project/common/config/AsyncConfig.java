package edu.kh.project.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync // 비동기 기능 활성화
public class AsyncConfig {

    @Bean(name = "mailExecutor")
    public Executor mailExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);    // 기본 스레드 수
        executor.setMaxPoolSize(10);    // 최대 스레드 수
        executor.setQueueCapacity(20);  // 대기열 크기
        executor.setThreadNamePrefix("MailExecutor-");
        executor.initialize();
        return executor;
    }
}