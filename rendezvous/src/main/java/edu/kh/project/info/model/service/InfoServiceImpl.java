package edu.kh.project.info.model.service;

import edu.kh.project.info.model.dto.InfoBoard;
import edu.kh.project.info.model.mapper.InfoBoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InfoServiceImpl implements InfoService {

	private final InfoBoardMapper mapper;

	// 지역 목록 필터 생성
	@Override
	public Map<String, List<String>> getFilterOptions() {
		List<String> addresses = mapper.selectAllAddresses();
		Map<String, Set<String>> locationMap = new TreeMap<>();

		for (String addr : addresses) {
			if (addr == null)
				continue;
			String[] parts = addr.split(" ");
			if (parts.length >= 2) {
				locationMap.computeIfAbsent(parts[0], k -> new HashSet<>()).add(parts[1]);
			}
		}

		Map<String, List<String>> result = new LinkedHashMap<>();
		for (Map.Entry<String, Set<String>> entry : locationMap.entrySet()) {
			List<String> distList = new ArrayList<>(entry.getValue());
			Collections.sort(distList);
			result.put(entry.getKey(), distList);
		}
		return result;
	}

	// 검색 로직
	@Override
	public List<Map<String, Object>> selectPlaceList(Map<String, Object> params) {
		// MyBatis 호출
		List<InfoBoard> list = mapper.selectPlaceList(params);
		String tag = (String) params.get("tag");

		return list.stream().map(place -> {
			Map<String, Object> map = new HashMap<>();
			map.put("place", place);

			// 매칭 점수 로직
			int score = (tag != null && place.getAiTags() != null && place.getAiTags().contains(tag.replace("#", "")))
					? 98
					: (int) (Math.random() * 15) + 80;
			map.put("matchScore", score);
			return map;
		}).sorted((a, b) -> (int) b.get("matchScore") - (int) a.get("matchScore")).collect(Collectors.toList());
	}

	@Override
	public InfoBoard selectPlaceDetail(Long infoNo) {
		return mapper.selectById(infoNo);
	}
}