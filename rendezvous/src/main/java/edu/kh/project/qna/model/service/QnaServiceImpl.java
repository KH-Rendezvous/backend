package edu.kh.project.qna.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.qna.model.dto.Qna;
import edu.kh.project.qna.model.mapper.QnaMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class QnaServiceImpl implements QnaService{
	private final QnaMapper mapper;

	public List<Qna> getAdminQnaList() {
		return mapper.selectAdminQnaList();
	}
}
