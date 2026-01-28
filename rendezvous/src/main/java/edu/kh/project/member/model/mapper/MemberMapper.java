package edu.kh.project.member.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.kh.project.member.model.dto.BlockContact;
import edu.kh.project.member.model.dto.MemberProfileRequest;

@Mapper
public interface MemberMapper {
    // 차단 등록
    int insertBlockContact(BlockContact blockDto);
    
    // 차단 목록 조회 (필요하면 쓰셈)
    List<BlockContact> selectBlockList(int memberNo);
    
    // 차단 해제
    int deleteBlockContact(BlockContact blockContact);
    
    int updateMemberProfile(MemberProfileRequest req);
    int updateMemberCommon(MemberProfileRequest req);
    int deleteInterests(int memberNo);
    int insertInterest(@Param("memberNo") int memberNo, @Param("interest") String interest);
}
