package edu.kh.project.common.security.jwt;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        // 1. 요청 헤더에서 "Authorization" 값을 꺼냄
        String bearerToken = request.getHeader("Authorization");

        // 2. 토큰이 있고, "Bearer "로 시작하는지 확인
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            
            // "Bearer " 글자 잘라내고 순수 토큰만 추출
            String token = bearerToken.substring(7);

            // 3. 토큰 유효성 검사 (만료됐거나 위조됐으면 false 반환)
            if (tokenProvider.validateToken(token)) {
                // 통과! (Controller로 진행)
                return true; 
            }
        }

        // 4. 검사 실패 (토큰이 없거나, 만료됨)
        // -> 401 Unauthorized 에러를 응답에 실어 보냄
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 
        response.getWriter().write("Unauthorized");
        
        // Controller로 못 가게 막음
        return false; 
    }
}