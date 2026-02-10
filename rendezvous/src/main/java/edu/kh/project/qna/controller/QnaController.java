package edu.kh.project.qna.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.kh.project.qna.model.dto.QnaDto;
import edu.kh.project.qna.model.service.QnaService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/qna")
public class QnaController {

    @Autowired
    private QnaService service;

    // 문의 등록
    @PostMapping("/insert")
    public int insertQna(@RequestBody QnaDto qna) {
        log.info("QnA 등록 요청 들어옴: {}", qna); // 로그로 데이터 확인
        return service.insertQna(qna);
    }
}