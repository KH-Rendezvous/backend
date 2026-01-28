package edu.kh.project.mypage.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.Member;
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

}