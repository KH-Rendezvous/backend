package edu.kh.project.common.interceptor; // 패키지명 확인!

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import edu.kh.project.common.security.jwt.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminCheckInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        // 1. 요청 헤더에서 토큰 꺼내기
        String bearerToken = request.getHeader("Authorization");
        

        // 2. 토큰이 있고, Bearer로 시작하는지 확인
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7); // "Bearer " 제거

            // 3. 토큰 유효성 검사 & 권한 확인
            if (tokenProvider.validateToken(token)) {
                
                // ★ 여기가 핵심: 세션이 아니라 '토큰'에서 권한을 꺼냄
                int authority = tokenProvider.getAuthority(token);

                // 권한이 2(관리자)면 통과
                if (authority == 2) { 
                    return true; 
                }
            }
        }

        // 4. 실패 시 (권한 없거나 토큰 이상함)
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 에러
        response.getWriter().write("Forbidden: Admin access only");
        
        return false;
    }
}