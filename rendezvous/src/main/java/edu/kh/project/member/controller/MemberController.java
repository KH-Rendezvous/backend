package edu.kh.project.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import edu.kh.project.member.model.service.MemberService; // 서비스 임포트 필수

@Controller
public class MemberController {
	
	@Autowired
	private MemberService service;

}