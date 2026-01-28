package edu.kh.project.member.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.BlockContact;
import edu.kh.project.member.model.dto.MemberProfileRequest;
import edu.kh.project.member.model.mapper.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper mapper;

    // 차단 등록
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertBlock(BlockContact blockContact) {
        return mapper.insertBlockContact(blockContact);
    }

    // 차단 목록 조회
    @Override
    public List<BlockContact> selectBlockList(int memberNo) {
        return mapper.selectBlockList(memberNo);
    }

    // 차단 해제
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteBlock(int blockId, int memberNo) {
        // DTO에 담아서 Mapper로 전달
        BlockContact blockContact = new BlockContact();
        blockContact.setBlockId(blockId);
        blockContact.setMemberNo(memberNo);
        
        return mapper.deleteBlockContact(blockContact);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateProfile(MemberProfileRequest req) {
        
        // 1. MEMBER_PROFILE 테이블 업데이트 (자기소개, 키, MBTI, 지역)
        int result1 = mapper.updateMemberProfile(req);

        // 2. MEMBER 테이블 업데이트 (학력, 흡연, 음주 등 코드값들)
        int result2 = mapper.updateMemberCommon(req);

        // 3. 관심사 리스트 업데이트
        if (req.getInterestList() != null) {
            mapper.deleteInterests(req.getMemberNo());
            
            for (String interest : req.getInterestList()) {
                if(interest != null && !interest.isBlank()) {
                    mapper.insertInterest(req.getMemberNo(), interest);
                }
            }
        }
        
        // 프로필 업데이트만 성공해도 성공으로 처리
        return result1 > 0 ? 1 : 0; 
    }
}
