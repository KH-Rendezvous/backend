package edu.kh.project.matching.model.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @ToString
public class DiscoveryFilterDTO {
    private int memberNo;      // 현재 로그인 유저
    private int distance;      // 최대 거리 (km)
    private int minAge;        // 최소 연령
    private int maxAge;        // 최대 연령
    private String gender;     // 선호 성별 (M, F, ALL)
    
    private Integer relIntentId;  // 관계 목적 ID (숫자 타입이 안전)
    
    private List<String> interests; // 선택한 관심사 목록
    
    // 거리 계산을 위해 위도/경도 필드도 추가되어 있어야 합니다! (쿼리에서 #{latitude} 사용 중)
    private Double latitude;
    private Double longitude;
}