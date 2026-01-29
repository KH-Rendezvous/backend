package edu.kh.project.mypage.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.MasterCode;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.dto.MemberProfileRequest;
import edu.kh.project.mypage.model.mapper.MyPageMapper;

@Service
public class MyPageServiceImpl implements MyPageService {

	@Autowired
	private MyPageMapper mapper;

	// 거리 수정
	@Transactional(rollbackFor = Exception.class) 
	@Override
	public int updateDistance(Member member) {
		return mapper.updateDistance(member);
	}
	
	@Override
	public int updateGender(Member member) {
	    return mapper.updateGender(member);
	}
	
	@Override
	public int updateAgeRange(Member member) {
	    return mapper.updateAgeRange(member);
	}
	
	@Override
	public int updateVisibility(Member member) {
	    return mapper.updateVisibility(member);
	}
	
	@Transactional(rollbackFor = Exception.class)
    @Override
    public int updateProfile(MemberProfileRequest req) {
        
        // 1. 상세 정보 (자기소개 등)
        int result1 = mapper.updateMemberProfile(req);

        // 2. 공통 정보 (닉네임 등)
        int result2 = mapper.updateMemberCommon(req);

        // 3. 관심사
        if (req.getInterestList() != null) {
            mapper.deleteInterests(req.getMemberNo());
            for (String interest : req.getInterestList()) {
                if(interest != null && !interest.isBlank()) {
                    mapper.insertInterest(req.getMemberNo(), interest);
                }
            }
        }
        
        
        return (result1 > 0 || result2 > 0) ? 1 : 0; 
    }
	
	@Override
    public MemberProfileRequest getProfile(int memberNo) {
        
        // 1. 기본 프로필 정보 가져오기
        MemberProfileRequest profile = mapper.selectMemberProfile(memberNo);
        
        // (방어 코드) 프로필이 아예 없으면 빈 객체라도 리턴
        if(profile == null) profile = new MemberProfileRequest();
        
        // 2. 관심사 목록 가져와서 합치기
        List<String> interests = mapper.selectMemberInterests(memberNo);
        profile.setInterestList(interests);
        
        return profile;
    }
	
	@Override
    public List<Map<String, Object>> searchSchool(String keyword) {
        return mapper.searchSchool(keyword);
    }
	
	@Override
    public List<Map<String, Object>> searchRegion(String keyword) {
        return mapper.searchRegion(keyword);
    }
	
	@Override
    public List<MasterCode> getAllMasterCodes() {
        return mapper.selectAllMasterCodes();
    }

	@Override
	public int checkNickname(String nickname) {
		return mapper.checkNickname(nickname);
	}


}