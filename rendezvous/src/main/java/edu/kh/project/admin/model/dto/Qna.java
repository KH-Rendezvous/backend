package edu.kh.project.admin.model.dto;

import lombok.Data;

@Data

public class Qna {
	private int qnaNo;
	private String qnaTitle;
	private String qnaContent;
	private String qnaDate;
	private int memberNo;
	private String qnaStatus;
	private String answerContent;
	private String nickname;
	private String email;
}
