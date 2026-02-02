package edu.kh.project.common;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FCMService {
	// 알림 보내기 함수
	public void sendNotification(String token, String title, String body) {
		if (token == null || token.isEmpty()) {
			System.out.println("토큰이 없어서 알림을 못 보냅니다.");
			return;
		}

		try {
			// 1. 메시지 만들기
			Message message = Message.builder().setToken(token) // 누구한테 보낼지 (관리자 토큰)
					.setNotification(Notification.builder().setTitle(title) // 알림 제목
							.setBody(body) // 알림 내용
							.build())
					.build();

			// 2. 전송!
			String response = FirebaseMessaging.getInstance().send(message);
			System.out.println("✅ 알림 전송 성공! 응답 ID: " + response);

		} catch (Exception e) {
			System.out.println("❌ 알림 전송 실패...");
			e.printStackTrace();
		}
	}
}
