package edu.kh.project.member.model.dto;

import lombok.Data;

@Data
public class MasterCode {
	private int codeId;
    private String category;
    private String codeName;
    private String emoji; 
    private int sortOrder;
}
