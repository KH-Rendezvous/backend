package edu.kh.project.mypage.model.service;

import org.springframework.stereotype.Service;

import edu.kh.project.member.model.dto.Member;

@Service
public interface MyPageService {
	
	/** 거리 설정 수정 서비스
	 * @param member (memberNo, searchDistance)
	 * @return result (1:성공, 0:실패)
	 */
	int updateDistance(Member member);
	
	int updateGender(Member member);
	
	int updateAgeRange(Member member);
	
	int updateVisibility(Member member);

}