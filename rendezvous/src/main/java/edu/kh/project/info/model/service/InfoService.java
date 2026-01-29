package edu.kh.project.info.model.service;

import edu.kh.project.info.model.dto.InfoBoard;
import java.util.List;
import java.util.Map;

public interface InfoService {
	Map<String, List<String>> getFilterOptions();

	List<Map<String, Object>> selectPlaceList(Map<String, Object> params);

	InfoBoard selectPlaceDetail(Long infoNo);
}