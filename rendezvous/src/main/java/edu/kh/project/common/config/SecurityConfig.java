package edu.kh.project.common.config; // 패키지명은 본인 프로젝트에 맞게 수정하세요

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				// 1. CSRF 비활성화 (POST 요청이 막히는 주 원인)
				.csrf(AbstractHttpConfigurer::disable)

				// 2. CORS 설정 적용 (아래에서 만든 설정을 사용하겠다고 명시)
				.cors(cors -> cors.configurationSource(configurationSource()))

				// 3. 모든 요청 허용 (로그인 없이도 통신 가능하게)
				.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

		return http.build();
	}

	// CORS 설정을 여기서 통합 관리합니다.
	@Bean
	public CorsConfigurationSource configurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		// 프론트엔드 주소 허용 (포트번호 정확히!)
		configuration.setAllowedOrigins(List.of("http://localhost:5173"));

		// 모든 HTTP 메서드 허용 (GET, POST, OPTIONS 등)
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

		// 모든 헤더 허용
		configuration.setAllowedHeaders(List.of("*"));

		// 자격 증명(쿠키 등) 허용
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		// 모든 경로(/**)에 대해 위 설정을 적용
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}