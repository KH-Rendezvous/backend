package edu.kh.project.admin.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.admin.model.dto.Report;
import edu.kh.project.admin.model.mapper.AdminMapper;
import edu.kh.project.email.model.service.EmailService;
import edu.kh.project.member.model.dto.Member;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AdminServiceImpl implements AdminService {

	private final AdminMapper mapper;
	private final EmailService emailService;

	@Override
	public List<Member> selectMemberList() {
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
//		mapper.selectReportList()
		return null;
	}

	@Override
	public int processReport(Map<String, Object> params) {
//		mapper.processReport(params)
		return 0;
	}
}