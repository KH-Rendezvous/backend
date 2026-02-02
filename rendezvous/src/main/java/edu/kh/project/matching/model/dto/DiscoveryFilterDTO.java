package edu.kh.project.matching.model.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @ToString
public class DiscoveryFilterDTO {
    private int memberNo;      // 현재 로그인 유저
    private int distance = 100; // 기본 거리 100km
    private String gender;     // 보고 싶은 성별 (M, F, ALL)
    private int minAge = 19;   // 최소 연령
    private int maxAge = 50;   // 최대 연령
    private List<String> interests; // 선택한 관심사 목록
}