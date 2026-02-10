package edu.kh.project.qna.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import edu.kh.project.qna.model.dto.QnaDto;

@Mapper
public interface QnaMapper {

    int insertQna(QnaDto qna);

}