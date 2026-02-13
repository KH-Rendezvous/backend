package edu.kh.project.member.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.common.security.jwt.TokenProvider;
import edu.kh.project.member.model.dto.LoginRequest;
import edu.kh.project.member.model.dto.LoginResponse;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.dto.SignupRequest;
import edu.kh.project.member.model.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;
    private final TokenProvider tokenProvider;

    @GetMapping("/logout")
    @ResponseBody
    public int logout(HttpSession session, @RequestParam("memberNo") int memberNo) {
        session.invalidate();
        return service.updateRefreshToken(memberNo, null);
    }

    @PostMapping("/signup")
    @ResponseBody
    public int signup(@RequestPart("data") SignupRequest input,
                      @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        return service.signup(input, images);
    }

    // =========================================================================
    // ★★★ [수정 1] 로그인 : 토큰 생성 시 권한(Authority) 추가
    // =========================================================================
    @PostMapping("/login")
    @ResponseBody
    public LoginResponse login(@RequestBody LoginRequest inputMember) {

        Member loginMember = service.login(inputMember);

        if (loginMember == null) {
            return LoginResponse.builder().result(0).build();
        }

        // 1. Access Token 생성 (이메일, 권한, 타입)
        String accessToken = tokenProvider.generateToken(
                loginMember.getEmail(), 
                loginMember.getAuthority(), // ★ 권한 추가됨
                "Access"
        );

        // 2. Refresh Token 생성 (이메일, 권한, 타입)
        String refreshToken = tokenProvider.generateToken(
                loginMember.getEmail(), 
                loginMember.getAuthority(), // ★ 권한 추가됨
                "Refresh"
        );

        service.updateRefreshToken(loginMember.getMemberNo(), refreshToken);

        return LoginResponse.builder()
                .result(1)
                .member(loginMember)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // =========================================================================
    // ★★★ [수정 2] 토큰 재발급 : 기존 토큰에서 권한 꺼내서 다시 넣기
    // =========================================================================
    @PostMapping("/refresh")
    @ResponseBody
    public Map<String, Object> refresh(@RequestBody Map<String, String> body) {

        Map<String, Object> map = new HashMap<>();
        String refreshToken = body.get("refreshToken");

        if (!tokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("유효하지 않은 Refresh Token입니다.");
        }

        // 1. 이메일 추출
        String email = tokenProvider.getSubject(refreshToken);
        
        // 2. 권한 추출 (TokenProvider에 getAuthority 메서드 있어야 함)
        int authority = tokenProvider.getAuthority(refreshToken); 

        // 3. 새 Access Token 발급 (권한 포함)
        String newAccessToken = tokenProvider.generateToken(email, authority, "Access");

        map.put("accessToken", newAccessToken);

        return map;
    }

    @GetMapping("/check")
    @ResponseBody
    public int checkDuplicate(@RequestParam("type") String type, @RequestParam("value") String value) {
        return service.checkDuplicate(type, value);
    }

    @PostMapping("/update-location")
    @ResponseBody
    public int updateLocation(@RequestBody Map<String, Object> map) {
        int memberNo = Integer.parseInt(String.valueOf(map.get("memberNo")));

        if (map.get("latitude") == null || map.get("longitude") == null) {
            return 0;
        }

        Double lat = Double.parseDouble(String.valueOf(map.get("latitude")));
        Double lon = Double.parseDouble(String.valueOf(map.get("longitude")));

        return service.updateLocation(memberNo, lat, lon);
    }

    @PostMapping("/find-email")
    public ResponseEntity<?> findEmail(@RequestBody Map<String, String> params) {
        log.info("이메일 찾기 요청: {}", params);

        try {
            String email = service.findEmail(params);
            if (email != null) {
                Map<String, String> result = new HashMap<>();
                result.put("email", email);
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("일치하는 회원이 없습니다.");
            }
        } catch (Exception e) {
            log.error("이메일 찾기 중 에러 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 에러");
        }
    }

    @PostMapping("/check-info")
    public ResponseEntity<?> checkInfo(@RequestBody Map<String, String> params) {
        log.info("비번찾기 정보확인 요청: {}", params);
        
        int count = service.checkMemberInfo(params);
        
        if (count > 0) {
            return ResponseEntity.ok(true); 
        } else {
            return ResponseEntity.ok(false); 
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> params) {
        log.info("비번 재설정 요청: {}", params.get("email"));
        
        int result = service.resetPassword(params);
        
        if (result > 0) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("변경 실패");
        }
    }
}