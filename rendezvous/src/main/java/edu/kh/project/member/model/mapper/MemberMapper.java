package edu.kh.project.member.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.kh.project.member.model.dto.BlockContact;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.dto.MemberPhoto;
import edu.kh.project.member.model.dto.MemberProfileRequest;

@Mapper
public interface MemberMapper {
	// 차단 등록
    int insertBlockContact(BlockContact blockDto);
    
    // 차단 목록 조회
    List<BlockContact> selectBlockList(int memberNo);
    
    // 차단 해제
    int deleteBlockContact(BlockContact blockContact);
    
    // 프로필 정보 (MemberProfileRequest 사용)
    int insertMemberProfile(MemberProfileRequest profile);

    // 회원 기본 정보
    int insertMember(Member member);

    Integer selectCodeId(@Param("codeName") String interestName, @Param("category") String category);


    void insertMemberInterest(@Param("memberNo") int memberNo, @Param("codeId") Integer codeId);

    // 사진 저장
    void insertMemberPhoto(MemberPhoto photo);
    
    // 로그인
    Member login(String email);
    
    int checkDuplicate(Map<String, Object> map);

}
