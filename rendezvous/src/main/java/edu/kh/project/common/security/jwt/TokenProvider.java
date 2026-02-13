package edu.kh.project.common.security.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value; 
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenProvider {

    // 1. static final 제거하고 인스턴스 변수로 변경
    private final Key key;

    // 만료 시간 (필요하면 얘네도 properties로 뺄 수 있음)
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;           // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일

    // 2. 생성자 주입 방식으로 변경 (@Value 사용)
    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        
        // properties에서 가져온 문자열(secretKey)을 여기서 디코딩
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 토큰 생성
     */
    public String generateToken(String email, int authority, String type) { 
        long expireTime = type.equals("Access") ? ACCESS_TOKEN_EXPIRE_TIME : REFRESH_TOKEN_EXPIRE_TIME;

        return Jwts.builder()
                .setSubject(email)
                .claim("authority", authority) 
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
    
    public int getAuthority(String token) {
        Claims claims = parseClaims(token);
        Object auth = claims.get("authority");
        
        // 없으면 기본값 1(일반유저) 리턴 (에러 방지용)
        if (auth == null) return 1; 
        
        // 안전하게 형변환 (String으로 바꿨다가 int로 파싱)
        return Integer.parseInt(String.valueOf(auth));
    }
    
    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.info("JWT 검증 실패: " + e.getMessage());
        }
        return false;
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}