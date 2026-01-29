package edu.kh.project.info.model.service;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiService {

	@Value("${gemini.api.key}")
	private String apiKey;
	@Value("${gemini.api.url}")
	private String apiUrl;
	private final RestTemplate restTemplate = new RestTemplate();

	public Map<String, String> analyzePlace(String placeName, String overview) {
		try {
			String prompt = String.format(
					"장소명: %s, 설명: %s \n 위 정보를 바탕으로 1.해시태그(3~4개), 2.한줄추천멘트(note)를 파이프(|)로 구분해서 텍스트로만 줘. 형식: #태그1 #태그2 | 추천멘트",
					placeName, overview != null ? overview : "");
			GeminiRequest request = new GeminiRequest(prompt);
			GeminiResponse response = restTemplate.postForObject(apiUrl + apiKey, request, GeminiResponse.class);

			if (response != null && !response.getCandidates().isEmpty()) {
				String text = response.getCandidates().get(0).getContent().getParts().get(0).getText();
				return parseResponse(text);
			}
		} catch (Exception e) {
			log.error("AI 분석 실패", e);
		}
		return Map.of("tags", "#신규 #탐색중", "note", "정보 업데이트 중입니다.");
	}

	private Map<String, String> parseResponse(String text) {
		Map<String, String> map = new HashMap<>();
		try {
			String[] parts = text.split("\\|");
			map.put("tags", parts[0].trim());
			map.put("note", parts.length > 1 ? parts[1].trim() : "추천합니다.");
		} catch (Exception e) {
			map.put("tags", "#추천");
			map.put("note", text);
		}
		return map;
	}

	@Data
	@AllArgsConstructor
	static class GeminiRequest {
		private List<Content> contents;

		public GeminiRequest(String text) {
			this.contents = Collections.singletonList(new Content(Collections.singletonList(new Part(text))));
		}
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	static class Content {
		private List<Part> parts;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	static class Part {
		private String text;
	}

	@Data
	@NoArgsConstructor
	static class GeminiResponse {
		private List<Candidate> candidates;
	}

	@Data
	static class Candidate {
		private Content content;
	}
}