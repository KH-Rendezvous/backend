package edu.kh.project.admin.model.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import edu.kh.project.member.model.dto.Member;

@Mapper
public interface AdminMapper {
	// 전체 회원 조회
	List<Member> selectMemberList();

	// 상태 변경 (Map으로 파라미터 전달)
	int updateMemberStatus(Map<String, Object> params);
}