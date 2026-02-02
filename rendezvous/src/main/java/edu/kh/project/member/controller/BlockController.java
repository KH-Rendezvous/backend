package edu.kh.project.member.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
        
        // ★ [테스트용] 로그인 안 되어 있으면 1번 회원으로 강제 진행
        if (loginMember == null) {
            loginMember = new Member();
            loginMember.setMemberNo(1);
            System.out.println("⚠️ [Block] 세션 없어서 1번으로 차단 테스트 진행");
        }

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
        
        // ★ [테스트용]
        if (loginMember == null) {
            loginMember = new Member();
            loginMember.setMemberNo(1);
        }
        
        return service.selectBlockList(loginMember.getMemberNo());
    }

    /**
     * 3. 차단 해제
     * DELETE /api/block/delete
     */
    @DeleteMapping("/delete")
    public String deleteBlock(@RequestBody Map<String, Integer> map,
                              @SessionAttribute(value = "loginMember", required = false) Member loginMember) {
        
        // ★ [테스트용]
        if (loginMember == null) {
            loginMember = new Member();
            loginMember.setMemberNo(1);
        }

        int blockId = map.get("blockId");
        int result = service.deleteBlock(blockId, loginMember.getMemberNo());

        return result > 0 ? "success" : "fail";
    }
}