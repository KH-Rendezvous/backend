package edu.kh.project.mypage.model.service;

import java.util.List;
import java.util.Map;

import edu.kh.project.member.model.dto.MasterCode;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.dto.MemberProfileRequest;

public interface MyPageService {
	
	/** 거리 설정 수정 서비스
	 * @param member (memberNo, searchDistance)
	 * @return result (1:성공, 0:실패)
	 */
	int updateDistance(Member member);
	
	int updateGender(Member member);
	
	int updateAgeRange(Member member);
	
	int updateVisibility(Member member);
	
	int updateProfile(MemberProfileRequest req);
	
	MemberProfileRequest getProfile(int memberNo);
	
	List<Map<String, Object>> searchSchool(String keyword);
	
	List<Map<String, Object>> searchRegion(String keyword);
	
	List<MasterCode> getAllMasterCodes();

	int checkNickname(String nickname);

}