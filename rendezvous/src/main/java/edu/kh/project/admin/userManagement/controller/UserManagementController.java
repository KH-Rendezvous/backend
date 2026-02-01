package edu.kh.project.admin.userManagement.controller;

import org.springframework.web.bind.annotation.RestController;

import edu.kh.project.admin.userManagement.model.service.UserManagementService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserManagementController {

	private final UserManagementService service;
	
}
