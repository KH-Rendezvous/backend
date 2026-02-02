package edu.kh.project.matching.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingUserDTO {
    private int memberNo;
    private String email;
    private String nickname;
    private String intro;
    private String birthDate;
    private int age;          
    private String gender;
    private String residence; // CITY 역할
    private String mbti;
    private String distance;  // 가공 데이터 (예: 10km)
    
    // 이미지 관련
    private String photoUrl;  // 대표 이미지
    private List<String> photos; // 전체 이미지 리스트
    
    // 기타 프로필 상세 (필요시 추가)
    private String relIntent; 
    private String education;
}