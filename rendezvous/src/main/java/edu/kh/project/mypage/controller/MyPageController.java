package edu.kh.project.mypage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import edu.kh.project.member.model.dto.MasterCode;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.dto.MemberProfileRequest;
import edu.kh.project.mypage.model.service.MyPageService;

@RestController
@RequestMapping("/api/mypage")
public class MyPageController {
    
    @Autowired
    private MyPageService myPageService;

    // 1. 거리 수정
    @PutMapping("/distance")
    public Map<String, Object> updateDistance(@RequestBody Member member) {
        // System.out.println("거리 수정 요청: " + member); 

        int result = myPageService.updateDistance(member);
        
        Map<String, Object> map = new HashMap<>();
        map.put("result", result > 0 ? "success" : "fail");
        return map;
    }
    
    // 2. 성별 수정
    @PutMapping("/gender")
    public Map<String, Object> updateGender(@RequestBody Member member) {
        int result = myPageService.updateGender(member);
        
        Map<String, Object> map = new HashMap<>();
        map.put("result", result > 0 ? "success" : "fail");
        return map;
    }
    
    // 3. 연령대 수정
    @PutMapping("/age")
    public Map<String, Object> updateAgeRange(@RequestBody Member member) {
        int result = myPageService.updateAgeRange(member);
        
        Map<String, Object> map = new HashMap<>();
        map.put("result", result > 0 ? "success" : "fail");
        return map;
    }
    
    // 4. 프로필 공개 여부 수정
    @PutMapping("/visibility")
    public Map<String, Object> updateVisibility(@RequestBody Member member) {
        int result = myPageService.updateVisibility(member);
        
        Map<String, Object> map = new HashMap<>();
        map.put("result", result > 0 ? "success" : "fail");
        return map;
    }

    // 5. 프로필 상세 수정
    @PutMapping("/profile")
    public Map<String, Object> updateProfile(
            @RequestBody MemberProfileRequest req,
            @SessionAttribute(value = "loginMember", required = false) Member loginMember) {
        
        // 1. [테스트용] 로그인이 안 되어 있으면, 강제로 1번 회원이라고 뻥침
        if (loginMember == null) {
            loginMember = new Member();
            loginMember.setMemberNo(1); 
            System.out.println("⚠️ [MyPage] 로그인 세션 없어서 1번 회원으로 강제 진행");
        }

        // 2. 로그인한 회원 번호를 DTO에 주입
        req.setMemberNo(loginMember.getMemberNo());
        
        System.out.println("프로필 수정 요청 데이터: " + req); 

        // 3. 서비스 호출
        int result = myPageService.updateProfile(req);

        Map<String, Object> map = new HashMap<>();
        map.put("result", result > 0 ? "success" : "fail");
        return map;
    }
    
    // 6. 내 프로필 정보 조회
    @GetMapping("/profile")
    public Map<String, Object> getProfile(
            @SessionAttribute(value = "loginMember", required = false) Member loginMember) {
        
        // 테스트용 (로그인 안 됨 -> 1번 회원)
        int memberNo = 1; 
        if(loginMember != null) memberNo = loginMember.getMemberNo();
        else System.out.println("⚠️ [MyPage] 조회: 로그인 안 돼서 1번 회원 정보 가져옴");

        // 서비스 호출
        MemberProfileRequest profile = myPageService.getProfile(memberNo);
        
        Map<String, Object> map = new HashMap<>();
        map.put("data", profile); // "data"라는 이름으로 담아 보냄
        map.put("result", "success");
        
        return map;
    }
    
    // 7. 학교 검색 
    @GetMapping("/school")
    public Map<String, Object> searchSchool(@RequestParam("keyword") String keyword) {
        List<Map<String, Object>> list = myPageService.searchSchool(keyword);
        
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("result", "success");
        return map;
    }
    
    // 8. 지역 검색
    @GetMapping("/region")
    public Map<String, Object> searchRegion(@RequestParam("keyword") String keyword) {
        List<Map<String, Object>> list = myPageService.searchRegion(keyword); // 방금 만든 거 호출
        
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("result", "success");
        return map;
    }
    
    @GetMapping("/codes")
    public Map<String, Object> getAllCodes() {
        // 1. 서비스 호출해서 리스트 받아옴
        List<MasterCode> list = myPageService.getAllMasterCodes();
        
        // 2. 결과 맵핑 (프론트에서 response.data.list로 받음)
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("result", "success");
        
        return map;
    }
    
    @GetMapping("/nickname/check")
    @ResponseBody
    public int checkNickname(@RequestParam("nickname") String nickname) {
        // 서비스 가서 SELECT COUNT(*) FROM MEMBER WHERE NICKNAME = #{nickname} AND DEL_FL = 'N' 해오기
        return myPageService.checkNickname(nickname); 
    }
}