package edu.kh.project.member.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import edu.kh.project.member.model.dto.BlockContact;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.service.MemberService;

@RestController
@RequestMapping("/api/block")
public class BlockController {

    @Autowired
    private MemberService service;

    /**
     * 1. 연락처 차단 등록
     * POST /api/block/insert
     */
    @PostMapping("/insert")
    public String insertBlock(@RequestBody BlockContact blockContact,
                              @SessionAttribute(value = "loginMember", required = false) Member loginMember) {
        
        // 1. 세션 정보가 있으면 그걸 우선 사용 (보안상 더 안전)
        if (loginMember != null) {
            blockContact.setMemberNo(loginMember.getMemberNo());
        }
        
        // 2. 세션도 없고, 프론트에서도 안 보냈으면 실패 처리
        if (blockContact.getMemberNo() == 0) {
            return "login_required";
        }

        // (기존의 "세션 없으면 1번으로 강제 세팅"하는 테스트 코드는 삭제함)

        int result = service.insertBlock(blockContact);
        return result > 0 ? "success" : "fail";
    }

    /**
     * 2. 차단 목록 조회
     * GET /api/block/list
     * [수정] 파라미터로 memberNo 받을 수 있게 추가
     */
    @GetMapping("/list")
    public List<BlockContact> selectBlockList(
            @RequestParam(value = "memberNo", required = false, defaultValue = "0") int memberNoParam,
            @SessionAttribute(value = "loginMember", required = false) Member loginMember) {
        
        int targetNo = 0;

        // 우선순위 1: 세션 정보
        if (loginMember != null) {
            targetNo = loginMember.getMemberNo();
        } 
        // 우선순위 2: 프론트에서 보낸 파라미터
        else {
            targetNo = memberNoParam;
        }
        
        // 로그인 정보가 없으면 빈 리스트 반환 (혹은 에러 처리)
        if (targetNo == 0) return null;

        return service.selectBlockList(targetNo);
    }

    /**
     * 3. 차단 해제
     * DELETE /api/block/delete
     */
    @DeleteMapping("/delete")
    public String deleteBlock(@RequestBody Map<String, Integer> map,
                              @SessionAttribute(value = "loginMember", required = false) Member loginMember) {
        
        int targetNo = 0;
        
        // 우선순위 1: 세션
        if (loginMember != null) {
            targetNo = loginMember.getMemberNo();
        } 
        // 우선순위 2: Request Body에 담긴 memberNo
        else if (map.containsKey("memberNo")) {
            targetNo = map.get("memberNo");
        }

        if (targetNo == 0) return "fail";

        int blockId = map.get("blockId");
        int result = service.deleteBlock(blockId, targetNo);

        return result > 0 ? "success" : "fail";
    }
}