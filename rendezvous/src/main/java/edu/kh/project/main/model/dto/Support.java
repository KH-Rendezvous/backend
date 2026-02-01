package edu.kh.project.main.model.dto;

import lombok.Data;

@Data
public class Support {
	private int supportNo;
	private String supportTitle;
	private String email;
	private String supportContent;
	private String supportDate;
	private String supportStatus;
	private String answerContent;
}
