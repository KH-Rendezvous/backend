package edu.kh.project.admin.model.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.admin.model.dto.Qna;
import edu.kh.project.admin.model.dto.Report;
import edu.kh.project.member.model.dto.MemberProfileRequest;

@Mapper
public interface AdminMapper {
	// 전체 회원 조회
	List<MemberProfileRequest> selectMemberList();

	int updateMemberStatus(Map<String, Object> params);

	List<Report> selectReportList();

	int processReport(Map<String, Object> params);

	int updateReportStatus(Map<String, Object> params);

	int banMember(Map<String, Object> params);
	List<Qna> selectAdminQnaList();

}