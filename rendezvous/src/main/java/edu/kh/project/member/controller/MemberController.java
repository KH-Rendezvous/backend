package edu.kh.project.member.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.member.model.dto.LoginRequest;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.dto.SignupRequest;
import edu.kh.project.member.model.service.MemberService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/member")
public class MemberController {

	@Autowired
	private MemberService service;

	@GetMapping("/logout")
	@ResponseBody
	public int logout(HttpSession session) {
		session.invalidate();
		return 1;
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
    public Map<String, Object> login(@RequestBody LoginRequest inputMember) {
        
        Map<String, Object> map = new HashMap<>();
        
        try {
            Member loginMember = service.login(inputMember);

            if (loginMember != null) {
                // 로그인 성공
                map.put("result", 1);
                map.put("member", loginMember);
                // TODO: 여기서 JWT 토큰 발행 로직 추가하면 됨
                
            } else {
                // 로그인 실패 (아이디 없거나 비번 틀림)
                map.put("result", 0);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", -1); // 에러 발생
        }

        return map;
    }

}