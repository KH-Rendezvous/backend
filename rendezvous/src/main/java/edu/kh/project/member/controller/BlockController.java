package edu.kh.project.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import edu.kh.project.member.model.dto.BlockContact;
import edu.kh.project.member.model.dto.Member; // 로그인 멤버 클래스
import edu.kh.project.member.model.service.MemberService;

import java.util.List;
import java.util.Map;

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
        
        // 로그인 체크 (세션 없으면 실패 리턴)
        if (loginMember == null) return "login_required";

        blockContact.setMemberNo(loginMember.getMemberNo()); // 내 번호 세팅

        int result = service.insertBlock(blockContact);
        return result > 0 ? "success" : "fail";
    }

    /**
     * 2. 차단 목록 조회
     * GET /api/block/list
     */
    @GetMapping("/list")
    public List<BlockContact> selectBlockList(@SessionAttribute(value = "loginMember", required = false) Member loginMember) {
        
        if (loginMember == null) return null; // 혹은 빈 리스트 반환
        
        return service.selectBlockList(loginMember.getMemberNo());
    }

    /**
     * 3. 차단 해제
     * DELETE /api/block/delete
     */
    @DeleteMapping("/delete")
    public String deleteBlock(@RequestBody Map<String, Integer> map,
                              @SessionAttribute(value = "loginMember", required = false) Member loginMember) {
        
        if (loginMember == null) return "login_required";

        int blockId = map.get("blockId");
        int result = service.deleteBlock(blockId, loginMember.getMemberNo());

        return result > 0 ? "success" : "fail";
    }
}