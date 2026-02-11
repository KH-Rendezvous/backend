package edu.kh.project.qna.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QnaDto {
    private int qnaNo;          // PK
    private int memberNo;       // FK
    private String title;       // 프론트에서 보낸 제목 (DB: QNA_TITLE)
    private String content;     // 프론트에서 보낸 내용 (DB: QNA_CONTENT)
    private String qnaDate;     // DB: QNA_DATE
    private String qnaStatus;   // DB: QNA_STATUS (답변 여부 Y/N)
    private String answerContent; // DB: ANSWER_CONTENT (관리자 답변)
}