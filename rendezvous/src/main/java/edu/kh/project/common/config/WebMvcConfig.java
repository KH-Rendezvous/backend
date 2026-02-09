package edu.kh.project.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import edu.kh.project.common.security.jwt.JwtInterceptor;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:config.properties")
public class WebMvcConfig implements WebMvcConfigurer {

	private final JwtInterceptor jwtInterceptor;

	// 설정 파일에서 값 가져오기
	@Value("${project.resource.webpath}")
	private String webPath;

	@Value("${project.resource.location}")
	private String resourceLocation;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// /images/** 로 시작하는 요청이 오면 -> 실제 파일 경로로 연결
		registry.addResourceHandler(webPath + "**").addResourceLocations("file:///" + resourceLocation);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jwtInterceptor).addPathPatterns("/api/**") // 1. /api로 시작하는 모든 요청 검사
				.excludePathPatterns( // 2. 예외 처리 (검사 안 할 목록)
						"/api/member/login", // 로그인
						"/api/member/signup", // 회원가입
						"/api/member/check", // 중복검사
						"/api/member/refresh", // ★ 토큰 재발급 (이거 막으면 갱신 못 함!)
						"/api/member/logout" // 로그아웃
				);
	}
}