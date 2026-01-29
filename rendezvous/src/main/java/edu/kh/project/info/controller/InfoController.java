package edu.kh.project.info.controller;

import edu.kh.project.info.model.dto.InfoBoard;
import edu.kh.project.info.model.service.InfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // CORS 허용
public class InfoController {

	private final InfoService infoService;

	// 필터 옵션 (지역 목록)
	@GetMapping("/filters")
	public ResponseEntity<Map<String, List<String>>> getFilters() {
		return ResponseEntity.ok(infoService.getFilterOptions());
	}

	// 검색 (다중 조건)
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> getPlaces(
			@RequestParam(value = "type", required = false, defaultValue = "ALL") String type,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "district", required = false) String district,
			@RequestParam(value = "tag", required = false) String tag) {
		Map<String, Object> params = new HashMap<>();
		params.put("type", type);
		params.put("city", city);
		params.put("district", district);
		params.put("tag", tag);

		return ResponseEntity.ok(infoService.selectPlaceList(params));
	}

	@GetMapping("/{infoNo}")
	public ResponseEntity<InfoBoard> getDetail(@PathVariable("infoNo") Long infoNo) {
		return ResponseEntity.ok(infoService.selectPlaceDetail(infoNo));
	}
}