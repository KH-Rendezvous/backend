package edu.kh.project.qna.model.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.qna.model.dto.Qna;

@Mapper
public interface QnaMapper {
	List<Qna> selectAdminQnaList();
}
