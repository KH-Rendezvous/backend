package edu.kh.project.info.model.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RestConfig {
	// RestTemplate을 Spring Bean으로 등록
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	// ObjectMapper는 Spring Boot가 기본으로 등록해주지만,
	// 명시적으로 제어하고 싶다면 아래와 같이 등록할 수 있습니다.
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
