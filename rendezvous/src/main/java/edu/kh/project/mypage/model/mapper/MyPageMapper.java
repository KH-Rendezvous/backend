package edu.kh.project.mypage.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.kh.project.member.model.dto.MasterCode;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.dto.MemberProfileRequest;


@Mapper
public interface MyPageMapper {
    
	int updateMemberProfile(MemberProfileRequest req); // MEMBER_PROFILE 테이블
    int updateMemberCommon(MemberProfileRequest req);  // MEMBER 테이블 (닉네임 등)
    int deleteInterests(int memberNo);
    int insertInterest(@Param("memberNo") int memberNo, @Param("interest") String interest);

    int updateDistance(Member member);
    int updateGender(Member member);
    int updateAgeRange(Member member);
    int updateVisibility(Member member);
    MemberProfileRequest selectMemberProfile(int memberNo);
    List<String> selectMemberInterests(int memberNo);
	List<Map<String, Object>> searchSchool(String keyword);
	List<Map<String, Object>> searchRegion(String keyword);
	List<MasterCode> selectAllMasterCodes();
	int checkNickname(String nickname);
    
}