package edu.kh.project.matching.model.mapper;

import edu.kh.project.matching.model.dto.DiscoveryFilterDTO;
import edu.kh.project.matching.model.dto.MatchingUserDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface MatchingMapper {
    
    /** * 탐색 리스트 조회 (나와 반대 성별) 
     * 수정: 파라미터 타입을 DiscoveryFilterDTO로 변경
     */
    List<MatchingUserDTO> selectDiscoveryList(DiscoveryFilterDTO filter);
    
    // 내가 보낸 LIKE 목록 조회
    List<MatchingUserDTO> selectSentLikes(int memberNo);

    // 내가 받은 LIKE 목록 조회
    List<MatchingUserDTO> selectReceivedLikes(int memberNo);

    // LIKE 액션 취소 (MEMBER_ACTION 테이블에서 삭제)
    int deleteLikeAction(Map<String, Object> map);

    int insertMemberAction(Map<String, Object> paramMap);

    // 상세 조회 및 매칭 확인 추가
    MatchingUserDTO selectMemberDetail(int targetNo);
    int checkMatch(Map<String, Object> map);
}