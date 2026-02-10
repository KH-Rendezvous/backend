package edu.kh.project.member.model.service;

import java.util.List;
import java.util.Map;

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
	
	// 위치 정보 업데이트
	int updateLocation(int memberNo, Double latitude, Double longitude);
	
	int checkDuplicate(String type, String value);
	
	// 리프레시 토큰 DB 업데이트
	int updateRefreshToken(int memberNo, String refreshToken);
	
	String findEmail(Map<String, String> params);
	
	/* 비밀번호 찾기 시 회원 정보 확인 */
    int checkMemberInfo(Map<String, String> params);

    /* 비밀번호 재설정 */
    int resetPassword(Map<String, String> params);

	

}
