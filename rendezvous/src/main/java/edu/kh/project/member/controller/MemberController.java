package edu.kh.project.member.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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

}