package edu.kh.project.member.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class MemberProfileRequest {
    private int memberNo;           // 세션에서 꺼내서 넣을 거임

    // 1. MEMBER_PROFILE 테이블 데이터
    private String intro;
    private int height;
    private String mbti;
    private String region;
    private String school; 

    // 2. MEMBER 테이블 데이터 (프론트는 글자로 보내니까 String으로 받음)
    private String relationship;
    private String affection;
    private String education;
    private String contact;
    private String zodiac;
    private String exercise;
    private String drinking;
    private String smoking;
    private String social;

    // 3. 관심사 (배열)
    private List<String> interestList; 
}