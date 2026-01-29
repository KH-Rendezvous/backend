package edu.kh.project.member.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.BlockContact;
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
    
    
}
