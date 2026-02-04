package edu.kh.project.email.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.kh.project.email.model.service.EmailService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("email")
@RequiredArgsConstructor
public class EmailController {
	private final EmailService service;

	/**
	 * 승인 요청 완료 메일 추후 회원가입 기능 구현 시 승인 상태 변경 로직 -> service에 추가하기
	 * 
	 * @param params
	 * @return
	 */
	@PostMapping("approve")
	public ResponseEntity<Integer> sendApprove(@RequestBody Map<String, String> params) {
		int result = service.sendApproveEmail("account-approval", params.get("email"));
		if (result > 0)
			return ResponseEntity.status(HttpStatus.OK).body(result);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
	}

	@PostMapping("qna")
	public ResponseEntity<Integer> sendQnaAnswer(@RequestBody Map<String, String> params) {
		int result = service.sendQnaEmail("inquiry-reply", params);
		if (result > 0)
			return ResponseEntity.status(HttpStatus.OK).body(result);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
	}

	@PostMapping("support")
	public ResponseEntity<Integer> sendSupportAnswer(@RequestBody Map<String, String> params) {
		int result = service.sendSupplyEmail("inquiry-reply", params);
		if (result > 0)
			return ResponseEntity.status(HttpStatus.OK).body(result);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
	}
	
	@PostMapping("signup")
    public ResponseEntity<Integer> sendAuthEmail(@RequestBody Map<String, String> params) {
        // 템플릿 안 쓸 거면 service에서 처리한 대로 로직 수정
        int result = service.sendAuthEmail("signup-auth", params.get("email"));
        
        if (result > 0)
            return ResponseEntity.status(HttpStatus.OK).body(result);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
    
    @PostMapping("check")
    public ResponseEntity<Integer> checkAuthKey(@RequestBody Map<String, String> params) {
        int result = service.checkAuthKey(params);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
