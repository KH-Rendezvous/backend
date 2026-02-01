package edu.kh.project.qna.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.kh.project.qna.model.dto.Qna;
import edu.kh.project.qna.model.service.QnaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("qna")
@RequiredArgsConstructor
public class QnaController {
	private final QnaService service;

	// 관리자용 QnA 전체 목록 조회
	@GetMapping("admin")
	public List<Qna> getAdminQnaList() {
		return service.getAdminQnaList();
	}
}
