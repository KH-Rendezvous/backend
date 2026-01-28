package edu.kh.project.mypage.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import edu.kh.project.member.model.dto.Member;


@Mapper
public interface MyPageMapper {
    
    int updateDistance(Member member);
    
    int updateGender(Member member);
    
    int updateAgeRange(Member member);
    
    int updateVisibility(Member member);
}