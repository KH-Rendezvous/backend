package edu.kh.project.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody; 

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
}