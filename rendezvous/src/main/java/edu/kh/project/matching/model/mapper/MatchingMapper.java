package edu.kh.project.matching.model.mapper;

import edu.kh.project.matching.model.dto.MatchingUserDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface MatchingMapper {
    /** 탐색 리스트 조회 (나와 반대 성별) */
    List<MatchingUserDTO> selectDiscoveryList(int memberNo);
}