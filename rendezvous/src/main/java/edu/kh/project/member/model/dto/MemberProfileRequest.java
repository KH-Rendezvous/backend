package edu.kh.project.member.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class MemberProfileRequest {
	private int memberNo;
	private String nickname;
	
    private Integer searchDistance; // 거리 설정 (null 가능)
    private Integer targetMinAge;   // 최소 나이
    private Integer targetMaxAge;   // 최대 나이 (null 가능)
    private String targetGender;    // 보고 싶은 성별 (M/F/A)
    private String profileOpen;     // 공개 범위 (A/P)

	// 1. MEMBER_PROFILE 테이블 데이터
	private String intro;
	private Integer height;
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
	private Integer schNo;
	private String schoolName;
	private Integer regionId;
	private String regionName;
	private String gender;
	private Integer age;

	// 3. 관심사 (배열)
	private List<String> interestList;
	private List<MemberPhoto> profileList;


	// 승인 여부 -> 재훈 수정
	private String memberStatus;
	private String birthDate;
	private String email;
	private String createDate;
	private String phone;
}