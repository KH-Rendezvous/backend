package edu.kh.project.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // 기본 생성자 (파라미터 없는 거) 강제 생성
@AllArgsConstructor // 모든 필드 다 들어가는 생성자 생성

public class BlockContact {
    private int blockId;         // PK
    private int memberNo;        // 로그인한 유저 번호
    private String targetName;   // 차단할 이름
    private String targetPhone;  // 차단할 번호
    private String targetEmail;  // 차단할 이메일
}