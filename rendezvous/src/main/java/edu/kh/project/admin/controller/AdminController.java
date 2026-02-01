package edu.kh.project.admin.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import edu.kh.project.admin.model.dto.Report;
import edu.kh.project.admin.model.service.AdminService;
import edu.kh.project.member.model.dto.Member;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService service;

	// 1. 회원 목록 조회
	@GetMapping("/members")
	public List<Member> selectMemberList() {
		return service.selectMemberList();
	}

	// 2. 회원 상태 변경 (승인 <-> 비활성)
	@PostMapping("/status")
	public int changeMemberStatus(@RequestBody Member member) {
		// params: memberNo, email, nickname, memberStatus
		return service.changeMemberStatus(member);
	}

	@GetMapping("/reports")
	public List<Report> selectReportList() {
		return service.selectReportList();
	}

	// 신고 처리 (params: reportNo, adminComment)
	@PostMapping("/report/process")
	public int processReport(@RequestBody Map<String, Object> params) {
		return service.processReport(params);
	}
}