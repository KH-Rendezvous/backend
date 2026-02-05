package edu.kh.project.member.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.member.model.dto.BlockContact;
import edu.kh.project.member.model.dto.LoginRequest;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.dto.SignupRequest;

public interface MemberService {
	
	// 차단 등록
	int insertBlock(BlockContact blockContact);

	// 차단 목록 조회
	List<BlockContact> selectBlockList(int memberNo);

	// 차단 해제
	int deleteBlock(int blockId, int memberNo);
	
	// 회원 가입
	int signup(SignupRequest input, List<MultipartFile> images);
	
	// 로그인
	Member login(LoginRequest inputMember);

	

}
