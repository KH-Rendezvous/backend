package edu.kh.project.admin.model.service;

import java.util.List;
import java.util.Map;

import edu.kh.project.admin.model.dto.Qna;
import edu.kh.project.admin.model.dto.Report;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.dto.MemberProfileRequest;

public interface AdminService {
	// 회원 목록 조회
	List<MemberProfileRequest> selectMemberList();

	// 상태 변경 (토글)
	int changeMemberStatus(Member member);

	List<Report> selectReportList();

	int processReport(Map<String, Object> params);
	List<Qna> getAdminQnaList();

}