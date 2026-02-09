package edu.kh.project.member.controller;

import java.util.List;
import java.util.Map;

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

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService service;

	private final TokenProvider tokenProvider;

	@GetMapping("/logout")
	@ResponseBody
	public int logout(HttpSession session, @RequestParam("memberNo") int memberNo) {

		// 1. 세션 만료 (기존 로직 유지)
		session.invalidate();

		// 2. DB의 리프레시 토큰을 NULL로 업데이트 (이제 memberNo를 아니까 에러 안 남)
		return service.updateRefreshToken(memberNo, null);
	}

	@PostMapping("/signup")
	@ResponseBody
	public int signup(@RequestPart("data") SignupRequest input, // JSON 데이터
			@RequestPart(value = "images", required = false) List<MultipartFile> images // 이미지 파일들
	) {

		return service.signup(input, images);
	}

	@PostMapping("/login")
	@ResponseBody
	public LoginResponse login(@RequestBody LoginRequest inputMember) {

		// 1. 아이디/비번 검사 (기존 서비스 로직)
		Member loginMember = service.login(inputMember);

		// 2. 로그인 실패 시 (null 리턴)
		if (loginMember == null) {
			return LoginResponse.builder().result(0).build();
		}

		// 3. 토큰 생성 (Access, Refresh)
		String accessToken = tokenProvider.generateToken(loginMember.getEmail(), "Access");
		String refreshToken = tokenProvider.generateToken(loginMember.getEmail(), "Refresh");

		// 4. ★ DB에 Refresh Token 저장 (서비스 호출)
		// ※ 주의: MemberService에 이 메서드 없으면 만들어야 함 (아래 참고)
		service.updateRefreshToken(loginMember.getMemberNo(), refreshToken);

		// 5. 응답 (DTO에 담아서 리턴)
		return LoginResponse.builder().result(1).member(loginMember).accessToken(accessToken).refreshToken(refreshToken)
				.build();
	}

	@PostMapping("/refresh")
	@ResponseBody
	public java.util.Map<String, Object> refresh(@RequestBody java.util.Map<String, String> body) {

		java.util.Map<String, Object> map = new java.util.HashMap<>();
		String refreshToken = body.get("refreshToken");

		// 1. Refresh Token 검증
		// (TokenProvider에 validateToken 메서드가 있어야 함)
		if (!tokenProvider.validateToken(refreshToken)) {
			// 유효하지 않은 토큰이면 에러 처리 (프론트에서 로그아웃 시킴)
			throw new RuntimeException("유효하지 않은 Refresh Token입니다.");
		}

		// 2. 새로운 Access Token 생성
		// (Refresh Token 안에 있는 이메일을 꺼내서 다시 만듦)
		String email = tokenProvider.getSubject(refreshToken);
		String newAccessToken = tokenProvider.generateToken(email, "Access");

		// 3. 결과 응답
		map.put("accessToken", newAccessToken);

		return map;
	}

	@GetMapping("/check")
	@ResponseBody
	public int checkDuplicate(@RequestParam("type") String type, @RequestParam("value") String value) {
		// type: "email", "nickname", "phone" 중 하나
		// value: 실제 입력값
		return service.checkDuplicate(type, value);
	}
	    @PostMapping("/update-location")
    @ResponseBody
    public int updateLocation(@RequestBody Map<String, Object> map) {
        
        // 프론트에서 { memberNo: 1, latitude: 37.5, longitude: 127.0 } 이렇게 보냄
        int memberNo = Integer.parseInt(String.valueOf(map.get("memberNo")));
        
        // 위치 정보가 없는 경우 방지
        if(map.get("latitude") == null || map.get("longitude") == null) {
            return 0;
        }

        Double lat = Double.parseDouble(String.valueOf(map.get("latitude")));
        Double lon = Double.parseDouble(String.valueOf(map.get("longitude")));

        return service.updateLocation(memberNo, lat, lon);
    }

}