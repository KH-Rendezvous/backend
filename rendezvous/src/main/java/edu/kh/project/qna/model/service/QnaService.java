package edu.kh.project.qna.model.service;

import edu.kh.project.qna.model.dto.QnaDto;

public interface QnaService {
    
    /** QnA 등록 */
    int insertQna(QnaDto qna);

}