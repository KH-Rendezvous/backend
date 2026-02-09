package edu.kh.project.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private int result;           // 로그인 성공 여부 (1:성공, 0:실패)
    private Member member;        // 회원 정보 (닉네임, 프사 등)
    private String accessToken;   // ★ 프론트가 쓸 출입증
    private String refreshToken;  // ★ 재발급용 (DB에도 저장됨)
}