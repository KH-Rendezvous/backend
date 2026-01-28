package edu.kh.project.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.dto.MemberProfileRequest;
import edu.kh.project.member.model.service.MemberService; // 서비스 임포트 필수

@Controller
public class MemberController {
	
	@Autowired
	private MemberService service;
	
	@PutMapping("/api/member/profile")
	@ResponseBody
	public String updateProfile(@RequestBody MemberProfileRequest req,
	                            @SessionAttribute(value = "loginMember", required = false) Member loginMember) {
	    
	    // ★ [테스트용] 로그인이 안 되어 있으면, 강제로 1번 회원이라고 뻥침
	    if (loginMember == null) {
	        loginMember = new Member();
	        loginMember.setMemberNo(1); // ※ 중요: DB MEMBER 테이블에 1번 회원이 진짜 있어야 함!
	        System.out.println("⚠️ [TEST MODE] 로그인 세션 없어서 1번 회원으로 강제 테스트 진행함");
	    }

	    // 로그인한 회원 번호 주입
	    req.setMemberNo(loginMember.getMemberNo());
	    
	    int result = service.updateProfile(req);
	    
	    return result > 0 ? "success" : "fail";
	}

}