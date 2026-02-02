package edu.kh.project.common.config;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Configuration
public class FCMConfig {

	@Bean
	public FirebaseApp firebaseApp() throws IOException {
		// 이미 초기화되어 있다면 기존 인스턴스 반환
		List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
		if (firebaseApps != null && !firebaseApps.isEmpty()) {
			for (FirebaseApp app : firebaseApps) {
				if (app.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
					return app;
				}
			}
		}

		// resources 폴더에 있는 JSON 파일 읽기
		ClassPathResource resource = new ClassPathResource("firebase-service-key.json");
		InputStream refreshToken = resource.getInputStream();

		FirebaseOptions options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(refreshToken))
				.build();

		return FirebaseApp.initializeApp(options);
	}
}