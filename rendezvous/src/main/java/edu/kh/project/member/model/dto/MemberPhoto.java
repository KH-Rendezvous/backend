package edu.kh.project.member.model.dto;

import lombok.Data;

@Data
public class MemberPhoto {
    private int photoId;        // PHOTO_ID (PK)
    private String originName;  // ORIGIN_NAME
    private String renameName;  // RENAME_NAME
    private String photoUrl;    // PHOTO_URL (웹 접근 경로)
    private int photoOrder;     // PHOTO_ORDER (1~6 순서)
    private int memberNo;       // MEMBER_NO (FK)
}