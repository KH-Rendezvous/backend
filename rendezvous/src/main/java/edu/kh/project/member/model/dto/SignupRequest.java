package edu.kh.project.member.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class SignupRequest {
    // 1. MEMBER 테이블 입력용
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String phone;
    private String targetGender; 
    
    private String relation; 

    // 2. MEMBER_PROFILE 테이블 입력용
    private String gender;
    private String birthYear;
    private String birthMonth;
    private String birthDay;
    
    // 3. MEMBER_INTERESTS 테이블 입력용 (관심사 문자열 리스트)
    private List<String> interests; 
    
    private String authKey;
}