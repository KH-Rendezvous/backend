package edu.kh.project.main.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.kh.project.main.model.dto.Support;
import edu.kh.project.main.model.service.MainService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("main")
public class MainController {
	private final MainService service;

	@PostMapping("support")
	public ResponseEntity<Integer> addSupport(@RequestBody Map<String, String> params) {
		int result = service.addSupport(params);
		if (result > 0)
			return ResponseEntity.status(HttpStatus.OK).body(result);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
	}

	@GetMapping("support")
	public ResponseEntity<List<Support>> getSupport() {
		List<Support> allList = service.getSupport();

		return ResponseEntity.status(HttpStatus.OK).body(allList);
	}

}
