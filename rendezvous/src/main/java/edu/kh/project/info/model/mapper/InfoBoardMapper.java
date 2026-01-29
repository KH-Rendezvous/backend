package edu.kh.project.info.model.mapper;

import edu.kh.project.info.model.dto.InfoBoard;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface InfoBoardMapper {
	int insertInfoBoard(InfoBoard board);

	int deleteByContentId(String contentId);

	List<String> selectAllAddresses();

	List<InfoBoard> selectPlaceList(Map<String, Object> params);

	InfoBoard selectById(Long infoNo);
}