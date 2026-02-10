package edu.kh.project.matching.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.matching.model.dto.DiscoveryFilterDTO; // 추가됨
import edu.kh.project.matching.model.dto.MatchingUserDTO;
import edu.kh.project.matching.model.mapper.MatchingMapper;

@Service
@Transactional(rollbackFor = Exception.class) // DML 작업(Insert, Delete) 시 예외 발생하면 롤백 처리
public class MatchingServiceImpl implements MatchingService {

	@Autowired
	private MatchingMapper mapper;

	/**
	 * 탐색 리스트 조회 수정: 파라미터 타입을 int memberNo에서 DiscoveryFilterDTO로 변경
	 */
	@Override
	public List<MatchingUserDTO> selectDiscoveryList(DiscoveryFilterDTO filter) {
		return mapper.selectDiscoveryList(filter);
	}

	/**
	 * 내가 보낸 LIKE 목록 조회
	 */
	@Override
	public List<MatchingUserDTO> selectSentLikes(int memberNo) {
		return mapper.selectSentLikes(memberNo);
	}

	/**
	 * 내가 받은 LIKE 목록 조회
	 */
	@Override
	public List<MatchingUserDTO> selectReceivedLikes(int memberNo) {
		return mapper.selectReceivedLikes(memberNo);
	}

	/**
	 * LIKE 액션 취소 (삭제)
	 */
	@Override
	public int deleteLikeAction(int myNo, int targetNo) {
		Map<String, Object> map = new HashMap<>();
		map.put("myNo", myNo);
		map.put("targetNo", targetNo);
		return mapper.deleteLikeAction(map);
	}

	/**
	 * 스와이프 액션 저장 (LIKE / DISLIKE)
	 */
	@Override
	public int insertMemberAction(Map<String, Object> paramMap) {
		// 성공 시 1, 실패 시 0 반환
		return mapper.insertMemberAction(paramMap);
	}

	/**
	 * 회원 상세 프로필 조회
	 */
	@Override
	public MatchingUserDTO selectMemberDetail(int targetNo) {
		return mapper.selectMemberDetail(targetNo);
	}

	/**
	 * 매칭 여부 확인
	 */
	@Override
	public int checkMatch(int myNo, int targetNo) {
		Map<String, Object> map = new HashMap<>();
		map.put("myNo", myNo);
		map.put("targetNo", targetNo);
		return mapper.checkMatch(map);
	}

}