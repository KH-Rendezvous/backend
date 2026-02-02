package edu.kh.project.main.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.main.model.dto.Support;
import edu.kh.project.main.model.mapper.MainMapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {
	private final MainMapper mapper;

	@Override
	public int addSupport(Map<String, String> params) {
		return mapper.addSupport(params);
	}

	@Override
	public List<Support> getSupport() {
		return mapper.getSupport();

	}

}
