package edu.kh.project.admin.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.admin.model.dto.Qna;
import edu.kh.project.admin.model.dto.Report;
import edu.kh.project.admin.model.mapper.AdminMapper;
import edu.kh.project.email.model.service.EmailService;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.dto.MemberProfileRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AdminServiceImpl implements AdminService {

	private final AdminMapper mapper;
	private final EmailService emailService;

	@Override
	public List<MemberProfileRequest> selectMemberList() {
		return mapper.selectMemberList();
	}

	@Override
	public int changeMemberStatus(Member member) {
		String currentStatus = member.getMemberStatus();
		String targetStatus = "N".equals(currentStatus) ? "Y" : "N";

		Map<String, Object> params = new HashMap<>();
		params.put("memberNo", member.getMemberNo());
		params.put("memberStatus", targetStatus);

		int result = mapper.updateMemberStatus(params);

		if (result > 0 && "Y".equals(targetStatus)) {
			emailService.sendApproveEmail("account-approval", member.getEmail());
		}

		return result;
	}

	@Override
	public List<Report> selectReportList() {
		return mapper.selectReportList();
	}

	@Override
	public int processReport(Map<String, Object> params) {
		// 1. 신고 테이블의 상태를 'Y'로 변경 (처리 완료)
		int reportResult = mapper.updateReportStatus(params);

		// 2. 신고 대상 회원의 탈퇴 여부(DEL_FL)를 'Y'로 변경 (정지)
		int memberResult = mapper.banMember(params);

		// 둘 다 성공했을 때만 1 반환, 아니면 0 반환 (혹은 예외 발생시켜 롤백 유도 가능)
		if (reportResult > 0 && memberResult > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	public List<Qna> getAdminQnaList() {
		return mapper.selectAdminQnaList();
	}

	@Override
	public int updateAnswer(Qna qna) {

		// EmailService.sendQnaEmail 메서드는 Map<String, String>을 요구하므로 변환
		Map<String, String> params = new HashMap<>();

		// 1. 필수 데이터 담기 (String으로 변환하여 넣기)
		params.put("qnaNo", String.valueOf(qna.getQnaNo())); // 질문 번호
		params.put("email", qna.getEmail()); // 받는 사람 이메일

		// 2. 메일 내용 및 DB 저장용 데이터
		// 프론트에서 title로 보냈으면 getQnaTitle(), 제목은 메일 제목용
		params.put("title", qna.getQnaTitle());

		// 답변 내용은 DB에 저장되고 메일 본문으로도 들어감
		params.put("content", qna.getAnswerContent());

		// 3. EmailService 호출!
		// 이 메서드 안에서 [메일발송 -> DB업데이트(QNA_STATUS='Y')]가 다 수행됩니다.
		// 'inquiry-reply'는 타임리프 템플릿 파일명입니다.
		return emailService.sendQnaEmail("inquiry-reply", params);
	}
}