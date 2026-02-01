package edu.kh.project.main.model.service;

import java.util.List;
import java.util.Map;

import edu.kh.project.main.model.dto.Support;

public interface MainService {
	int addSupport(Map<String, String> params);

	List<Support> getSupport();
}
