package edu.kh.project.email.model.service;

import java.util.Map;

public interface EmailService {

	int sendApproveEmail(String htmlName, String email);


	int sendQnaEmail(String htmlName, Map<String, String> params);


	int sendSupplyEmail(String htmlName, Map<String, String> params);
	

}
