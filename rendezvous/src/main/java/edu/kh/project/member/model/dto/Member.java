package edu.kh.project.member.model.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // 기본 생성자 (파라미터 없는 거) 강제 생성
@AllArgsConstructor // 모든 필드 다 들어가는 생성자 생성

public class Member {
    // MEMBER 테이블 컬럼 1:1 매칭
    private int memberNo;
    private String name;
    private String email;
    private String password;
    private String nickname;
    private String phone;
    private String delFl;
    private Date createDate;
    
    // 위치
    private double latitude;
    private double longitude;
    
    // 토큰
    private String refreshToken;
    
    // 취향 FK
    private String targetGender;
    private Integer relIntentId;
    private Integer affectId;
    private Integer eduId;
    private Integer commId;
    private Integer zodiacId;
    private Integer exerciseId;
    private Integer drinkId;
    private Integer smokeId;
    private Integer snsId;

    // 이후 추가
    private Integer searchDistance;
    private int targetMinAge; // 최소 나이
    private Integer targetMaxAge; // 최대 나이
    private String profileOpen;	// 프로필 공개 여부
}