package edu.kh.project.qna.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.qna.model.dto.QnaDto;
import edu.kh.project.qna.model.mapper.QnaMapper;

@Service
public class QnaServiceImpl implements QnaService {

    @Autowired
    private QnaMapper mapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertQna(QnaDto qna) {
        return mapper.insertQna(qna);
    }
}