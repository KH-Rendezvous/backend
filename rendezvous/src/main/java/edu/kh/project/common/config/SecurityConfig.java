package edu.kh.project.common.config; 

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(AbstractHttpConfigurer::disable)
	        .cors(cors -> cors.configurationSource(configurationSource()))
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers(
	                "/api/member/login", 
	                "/api/member/signup", 
	                "/api/member/check",
	                "/api/email/**",
	                "/api/auth/**",
	                "/api/main/support"
	            ).permitAll()
	            .anyRequest().permitAll()
	        );

	    return http.build();
	}

	// CORS 설정을 여기서 통합 관리합니다.
	@Bean
	public CorsConfigurationSource configurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		// 프론트엔드 주소 허용 (포트번호 정확히!)
		configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://192.168.32.8:5173"));

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
	
	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}