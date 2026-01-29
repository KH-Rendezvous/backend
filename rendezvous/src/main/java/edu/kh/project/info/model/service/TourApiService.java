package edu.kh.project.info.model.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kh.project.info.model.dto.InfoBoard;
import edu.kh.project.info.model.mapper.InfoBoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TourApiService {

	private final InfoBoardMapper mapper;
	private final GeminiService geminiService;
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	@Value("${tour.api.key}")
	private String apiKey;

	private static final String BASE_URL = "http://apis.data.go.kr/B551011/KorService2";

	// ★ 목표: 500개
	private static final int TOTAL_TARGET_COUNT = 500;

	// ★ 전략: 지역별 "카페 15개 + 식당 15개" = 30개씩 수집
	private static final int TARGET_CAFE_PER_REGION = 15;
	private static final int TARGET_FOOD_PER_REGION = 15;

	public void syncPlacesData() {
		log.info("====================================================");
		log.info("🏁 [Final] 전국 500개 수집 (업종 구분 + 이미지 필수)");
		log.info("   - 전략: 지역별 카페 {}개 + 식당 {}개", TARGET_CAFE_PER_REGION, TARGET_FOOD_PER_REGION);
		log.info("====================================================");

		int totalSavedCount = 0;

		try {
			List<String> areaCodes = fetchAreaCodes();
			log.info("📍 전국 지역 코드 확보 완료. 수집 시작...");

			for (String areaCode : areaCodes) {
				if (totalSavedCount >= TOTAL_TARGET_COUNT)
					break;

				// 1. [카페 수집] (cat3 code: A05020900)
				log.info("☕ [지역:{}] 카페 탐색 중...", areaCode);
				int savedCafe = collectData(areaCode, "A05020900", TARGET_CAFE_PER_REGION, totalSavedCount);
				totalSavedCount += savedCafe;

				if (totalSavedCount >= TOTAL_TARGET_COUNT)
					break;

				// 2. [식당 수집] (cat3 code: null)
				log.info("🍴 [지역:{}] 맛집 탐색 중...", areaCode);
				int savedFood = collectData(areaCode, null, TARGET_FOOD_PER_REGION, totalSavedCount);
				totalSavedCount += savedFood;

				log.info("   👉 지역({}) 완료. (누적: {}/500)", areaCode, totalSavedCount);
			}
			log.info("🎉 [Data Complete] 총 {}개의 데이터가 DB에 저장되었습니다!", totalSavedCount);

		} catch (Exception e) {
			log.error("🔥 시스템 에러: {}", e.getMessage());
		}
	}

	private int collectData(String areaCode, String cat3, int limit, int currentTotal) {
		// 후보군 3배수 확보 (이미지 없는 것 대비)
		List<JsonNode> items = fetchRestaurantList(areaCode, cat3, limit * 3);
		int savedCount = 0;

		// ★ 업종 구분 (cat3가 있으면 CAFE, 없으면 RESTAURANT)
		String locationType = (cat3 != null) ? "CAFE" : "RESTAURANT";

		for (JsonNode item : items) {
			if (savedCount >= limit)
				break;
			if (currentTotal + savedCount >= TOTAL_TARGET_COUNT)
				break;

			String title = item.path("title").asText();

			// ★ processPlaceData에 locationType 전달
			Optional<InfoBoard> processed = processPlaceData(item, locationType);

			if (processed.isPresent()) {
				saveToDatabase(processed.get());
				savedCount++;
				String icon = "CAFE".equals(locationType) ? "☕" : "🍴";
				log.info("✅ [저장] {} {} (지역:{})", icon, title, areaCode);
			}
		}
		return savedCount;
	}

	// ★ locationType 매개변수 추가
	private Optional<InfoBoard> processPlaceData(JsonNode item, String locationType) {
		String contentId = item.path("contentid").asText();
		String contentTypeId = "39";
		String title = item.path("title").asText();

		try {
			// [1] 공통 정보 (에러 방지를 위해 파라미터 최소화)
			JsonNode common = fetchDetail(contentId, null, "detailCommon2", Map.of());
			if (common == null)
				return Optional.empty();

			// [2] 소개 정보
			JsonNode intro = fetchDetail(contentId, contentTypeId, "detailIntro2", Map.of());

			String overview = common.has("overview") ? common.path("overview").asText() : "정보 없음";

			// 전화번호 우선순위
			String tel = "";
			if (intro != null && intro.has("infocenterfood") && !intro.path("infocenterfood").asText().isEmpty()) {
				tel = intro.path("infocenterfood").asText();
			} else if (item.has("tel") && !item.path("tel").asText().isEmpty()) {
				tel = item.path("tel").asText();
			} else {
				tel = common.path("tel").asText();
			}

			// 이미지
			String imgUrl = item.has("firstimage") && !item.path("firstimage").asText().isEmpty()
					? item.path("firstimage").asText()
					: common.path("firstimage").asText();

			String thumbUrl = item.has("firstimage2") && !item.path("firstimage2").asText().isEmpty()
					? item.path("firstimage2").asText()
					: common.path("firstimage2").asText();

			if (imgUrl == null || imgUrl.isEmpty())
				return Optional.empty();

			// AI 분석 (오류 발생 시 무시하고 진행)
			Map<String, String> ai = Map.of("tags", "#맛집", "note", "정보 없음");
			try {
				ai = geminiService.analyzePlace(title, overview);
			} catch (Exception e) {
			}

			return Optional
					.of(InfoBoard.builder().contentId(contentId).infoName(title).address(item.path("addr1").asText())
							.tel(tel).imgUrl(imgUrl).thumbUrl(thumbUrl).lat(item.path("mapy").asText())
							.lng(item.path("mapx").asText()).homepage(common.path("homepage").asText(""))
							.infoBody(overview).openTime(intro != null ? intro.path("opentimefood").asText() : "")
							.restDay(intro != null ? intro.path("restdatefood").asText() : "")
							.parking(intro != null ? intro.path("parkingfood").asText() : "")
							.menu(intro != null ? intro.path("treatmenu").asText() : "").aiTags(ai.get("tags"))
							.aiNote(ai.get("note")).locationType(locationType) // ★ 업종 구분 저장
							.build());

		} catch (Exception e) {
			return Optional.empty();
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void saveToDatabase(InfoBoard place) {
		mapper.deleteByContentId(place.getContentId());
		mapper.insertInfoBoard(place);
	}

	private List<String> fetchAreaCodes() {
		String url = BASE_URL + "/areaCode2?serviceKey=" + apiKey
				+ "&numOfRows=30&MobileOS=ETC&MobileApp=AppTest&_type=json";
		JsonNode root = restTemplate.getForObject(URI.create(url), JsonNode.class);
		return root.path("response").path("body").path("items").path("item").findValuesAsText("code");
	}

	private List<JsonNode> fetchRestaurantList(String areaCode, String cat3, int numOfRows) {
		StringBuilder sb = new StringBuilder(BASE_URL + "/areaBasedList2");
		sb.append("?serviceKey=").append(apiKey).append("&numOfRows=").append(numOfRows)
				.append("&MobileOS=ETC&MobileApp=AppTest&_type=json").append("&contentTypeId=39").append("&areaCode=")
				.append(areaCode).append("&arrange=O"); // 이미지 필수

		if (cat3 != null) {
			sb.append("&cat1=A05&cat2=A0502&cat3=").append(cat3);
		}

		List<JsonNode> list = new ArrayList<>();
		try {
			JsonNode root = restTemplate.getForObject(URI.create(sb.toString()), JsonNode.class);
			JsonNode items = root.path("response").path("body").path("items").path("item");

			if (items.isArray())
				items.forEach(list::add);
			else if (items.isObject())
				list.add(items);
		} catch (Exception e) {
		}

		return list;
	}

	private JsonNode fetchDetail(String contentId, String contentTypeId, String endpoint, Map<String, String> params) {
		StringBuilder sb = new StringBuilder(BASE_URL + "/" + endpoint);
		sb.append("?serviceKey=").append(apiKey).append("&MobileOS=ETC&MobileApp=AppTest&_type=json")
				.append("&contentId=").append(contentId).append("&numOfRows=10&pageNo=1");

		if (contentTypeId != null)
			sb.append("&contentTypeId=").append(contentTypeId);
		params.forEach((k, v) -> sb.append("&").append(k).append("=").append(v));

		try {
			JsonNode root = restTemplate.getForObject(URI.create(sb.toString()), JsonNode.class);
			String resultCode = root.path("response").path("header").path("resultCode").asText();

			if (!"0000".equals(resultCode))
				return null;

			JsonNode itemNode = root.path("response").path("body").path("items").path("item");
			if (itemNode.isArray() && !itemNode.isEmpty())
				return itemNode.get(0);
			if (itemNode.isObject() && !itemNode.isMissingNode())
				return itemNode;

		} catch (Exception e) {
		}
		return null;
	}
}