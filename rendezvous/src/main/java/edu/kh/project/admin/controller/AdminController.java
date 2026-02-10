package edu.kh.project.admin.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import edu.kh.project.admin.model.dto.Qna;
import edu.kh.project.admin.model.dto.Report;
import edu.kh.project.admin.model.service.AdminService;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.dto.MemberProfileRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService service;

	/**
	 * 회원 목록 조회
	 * 
	 * @return
	 */
	@GetMapping("/members")
	public List<MemberProfileRequest> selectMemberList() {
		return service.selectMemberList();
	}

	/**
	 * 회원 상태 변경 (승인 <-> 비활성)
	 * 
	 * @param member
	 * @return
	 */
	@PostMapping("/status")
	public int changeMemberStatus(@RequestBody Member member) {
		// params: memberNo, email, nickname, memberStatus
		return service.changeMemberStatus(member);
	}

	/**
	 * 신고 게시글 불러오기
	 * 
	 * @return
	 */
	@GetMapping("/reports")
	public List<Report> selectReportList() {
		return service.selectReportList();
	}

	// 신고 처리 (params: reportNo, targetMemberNo)
	@PostMapping("/report/process")
	public int processReport(@RequestBody Map<String, Object> params) {
		return service.processReport(params);
	}

	// 관리자용 QnA 전체 목록 조회
	@GetMapping("qna")
	public List<Qna> getAdminQnaList() {
		return service.getAdminQnaList();
	}
	
	@PostMapping("/qna/answer")
    public int updateAnswer(@RequestBody Qna qna) {
        return service.updateAnswer(qna);
    }
}