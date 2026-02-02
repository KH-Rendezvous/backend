package edu.kh.project.admin.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class Report {
	private int reportNo;
	private String reportTitle;
	private String reportContent;
	private String reportDate;
	private String reportStatus; // 'N', 'Y'
	private int memberNo;
	private int targetMemberNo;
	private int reason;

	// JOIN으로 가져올 닉네임들
	private String reporterNickname; // 신고자 닉네임
	private String targetNickname; // 대상자 닉네임
	private String codeName;
	private List<String> imageList;
}
