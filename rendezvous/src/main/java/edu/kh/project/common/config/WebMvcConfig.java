package edu.kh.project.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import edu.kh.project.common.interceptor.AdminCheckInterceptor;
import edu.kh.project.common.security.jwt.JwtInterceptor;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:config.properties")
public class WebMvcConfig implements WebMvcConfigurer {

	private final JwtInterceptor jwtInterceptor;
	private final AdminCheckInterceptor adminInterceptor;

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
	  
	  // 1. 토큰 검사 제외 경로 (따로 빼서 관리하면 수정할 때 편함)
	  String[] excludePaths = {
	    "/api/member/login", "/api/member/signup", "/api/member/check",
	    "/api/member/refresh", "/api/member/logout", "/api/member/find-email",
	    "/api/member/check-info", "/api/member/reset-password", "/api/email/**",
	    "/api/auth/**", "/api/main/support"
	  };

	  // 2. JWT 인터셉터
	  registry.addInterceptor(jwtInterceptor)
	    .addPathPatterns("/api/**")
	    .excludePathPatterns(excludePaths);

	  // 3. 관리자 인터셉터 (한 칸 띄워서 구분)
	  registry.addInterceptor(adminInterceptor)
	    .addPathPatterns("/api/admin/**");
	}
}