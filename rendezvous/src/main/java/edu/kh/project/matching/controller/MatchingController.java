package edu.kh.project.matching.controller;

import edu.kh.project.matching.model.dto.DiscoveryFilterDTO;
import edu.kh.project.matching.model.dto.MatchingUserDTO;
import edu.kh.project.matching.model.service.MatchingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/matching")
public class MatchingController {

    @Autowired
    private MatchingService service;

    /**
     * 탐색 리스트 조회 (나를 제외하고, 내 타겟 성별인 유저들)
     * 수정: memberNo 하나가 아닌 필터 객체(DiscoveryFilterDTO) 전체를 받도록 변경
     */
    @GetMapping("/discovery")
    public List<MatchingUserDTO> getDiscoveryList(@ModelAttribute DiscoveryFilterDTO filter) {
        log.info("조회 요청 필터 데이터: {}", filter); 
        return service.selectDiscoveryList(filter);
    }

    /**
     * 스와이프 액션 저장 (LIKE / DISLIKE)
     * 리액트의 handleSwipe에서 호출함
     */
    @PostMapping("/action")
    public int insertMemberAction(@RequestBody Map<String, Object> paramMap) {
        log.info("액션 발생: {}", paramMap);
        // paramMap: {senderNo, receiverNo, actionType, inputType}
        return service.insertMemberAction(paramMap);
    }

    /**
     * 내가 보낸 LIKE 목록 조회
     */
    @GetMapping("/likes/sent")
    public List<MatchingUserDTO> getSentLikes(@RequestParam("memberNo") int memberNo) {
        return service.selectSentLikes(memberNo);
    }
    
    /**
     * 내가 받은 LIKE 목록 조회
     */
    @GetMapping("/likes/received")
    public List<MatchingUserDTO> getReceivedLikes(@RequestParam("memberNo") int memberNo) {
        return service.selectReceivedLikes(memberNo);
    }

    /**
     * LIKE 취소 (보낸 목록에서 삭제)
     */
    @DeleteMapping("/likes/cancel")
    public int cancelLike(@RequestParam("myNo") int myNo, @RequestParam("targetNo") int targetNo) {
        return service.deleteLikeAction(myNo, targetNo);
    }
    
    /**
     * 회원 상세 프로필 조회
     */
    @GetMapping("/member/{targetNo}")
    public MatchingUserDTO getMemberDetail(@PathVariable("targetNo") int targetNo) {
        return service.selectMemberDetail(targetNo);
    }

    /**
     * 매칭 여부 확인
     */
    @GetMapping("/checkMatch")
    public int checkMatch(@RequestParam("myNo") int myNo, @RequestParam("targetNo") int targetNo) {
        return service.checkMatch(myNo, targetNo);
    }

}