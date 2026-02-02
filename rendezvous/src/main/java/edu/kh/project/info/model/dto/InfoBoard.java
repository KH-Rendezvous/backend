package edu.kh.project.info.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfoBoard {
	private Long infoNo;
	private String contentId;
	private String infoName;
	private String address;
	private String tel;
	private String imgUrl;
	private String thumbUrl;
	private String homepage;
	private String lat;
	private String lng;
	private String openTime;
	private String restDay;
	private String parking;
	private String menu;

	private String infoBody;
	private String aiTags;
	private String aiNote;
	private String locationType; // ★ 필수

	private String createDate;
}