package edu.kh.project.main.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.kh.project.common.FCMService;
import edu.kh.project.main.model.dto.Support;
import edu.kh.project.main.model.service.MainService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
@PropertySource("classpath:config.properties")
public class MainController {

	private final MainService service;
	private final FCMService fcmService;
	@Value("${project.fcm.admin-token}")
	private String adminToken;

	@PostMapping("support")
	public ResponseEntity<Integer> addSupport(@RequestBody Map<String, String> params) {

		// 1. DB 저장
		int result = service.addSupport(params);

		if (result > 0) {
			System.out.println("문의 내용 DB 저장 완료...");

			// 2. 관리자에게 알림 발송 🔔
			// ★ React 콘솔에서 복사한 토큰을 여기에 넣으세요 (아주 깁니다)

			// params에서 제목(title)을 꺼내서 알림 내용으로 보냅니다.
			// 순서: (토큰, 알림 제목, 알림 내용)
			fcmService.sendNotification(adminToken, "💌 새로운 문의가 도착했습니다!", params.get("title"));

			return ResponseEntity.status(HttpStatus.OK).body(result);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
	}

	@GetMapping("support")
	public ResponseEntity<List<Support>> getSupport() {
		List<Support> allList = service.getSupport();
		return ResponseEntity.status(HttpStatus.OK).body(allList);
	}
}