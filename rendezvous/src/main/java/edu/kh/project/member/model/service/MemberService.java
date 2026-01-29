package edu.kh.project.member.model.service;

import java.util.List;

import edu.kh.project.member.model.dto.BlockContact;

public interface MemberService {
	
	// 차단 등록
	int insertBlock(BlockContact blockContact);

	// 차단 목록 조회
	List<BlockContact> selectBlockList(int memberNo);

	// 차단 해제
	int deleteBlock(int blockId, int memberNo);

	

}
