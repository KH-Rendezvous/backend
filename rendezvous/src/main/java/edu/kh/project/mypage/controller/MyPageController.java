package edu.kh.project.mypage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.member.model.dto.MasterCode;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.dto.MemberProfileRequest;
import edu.kh.project.mypage.model.service.MyPageService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/mypage")
public class MyPageController {

    @Autowired
    private MyPageService myPageService;

    // 1. 프로필 수정
    @PostMapping("/profile")
    public Map<String, Object> updateProfile(
            @RequestPart("profileText") MemberProfileRequest profileRequest,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestParam(value = "orders", required = false) List<Integer> orders,
            @RequestParam(value = "deleteList", required = false) List<Integer> deleteList,
            @SessionAttribute(value = "loginMember", required = false) Member loginMember
            ) throws Exception {

        // [수정] 세션이 있으면 세션 정보 우선, 없으면 프론트에서 보낸 profileRequest의 memberNo 유지
        if (loginMember != null) {
            profileRequest.setMemberNo(loginMember.getMemberNo());
        }
        
        // loginMember가 null일 때 강제로 1번으로 만드는 코드는 삭제함.
        // 이제 프론트에서 보낸 memberNo가 그대로 서비스로 넘어감.

        int result = myPageService.updateProfile(profileRequest, images, orders, deleteList);

        if (result > 0) return Map.of("result", "success");
        else return Map.of("result", "fail");
    }

    // 2. 거리 수정
    @PutMapping("/distance")
    public Map<String, Object> updateDistance(@RequestBody Member member) {
        int result = myPageService.updateDistance(member);
        Map<String, Object> map = new HashMap<>();
        map.put("result", result > 0 ? "success" : "fail");
        return map;
    }

    // 3. 성별 수정
    @PutMapping("/gender")
    public Map<String, Object> updateGender(@RequestBody Member member) {
        int result = myPageService.updateGender(member);
        Map<String, Object> map = new HashMap<>();
        map.put("result", result > 0 ? "success" : "fail");
        return map;
    }

    // 4. 연령대 수정
    @PutMapping("/age")
    public Map<String, Object> updateAgeRange(@RequestBody Member member) {
        int result = myPageService.updateAgeRange(member);
        Map<String, Object> map = new HashMap<>();
        map.put("result", result > 0 ? "success" : "fail");
        return map;
    }

    // 5. 프로필 공개 여부 수정
    @PutMapping("/visibility")
    public Map<String, Object> updateVisibility(@RequestBody Member member) {
        int result = myPageService.updateVisibility(member);
        Map<String, Object> map = new HashMap<>();
        map.put("result", result > 0 ? "success" : "fail");
        return map;
    }

    // 6. 내 프로필 정보 조회
    @GetMapping("/profile")
    public Map<String, Object> getProfile(
            // [수정] 프론트에서 memberNo를 파라미터로 보낼 수 있게 추가
            @RequestParam(value = "memberNo", required = false, defaultValue = "0") int memberNoParam,
            @SessionAttribute(value = "loginMember", required = false) Member loginMember) {

        int targetNo = 0;

        // 우선순위 1: 세션 정보
        if (loginMember != null) {
            targetNo = loginMember.getMemberNo();
        } 
        // 우선순위 2: 프론트에서 보낸 파라미터
        else if (memberNoParam > 0) {
            targetNo = memberNoParam;
        } 
        // 우선순위 3: 둘 다 없으면 테스트용 1번 (개발 중 편의상 유지)
        else {
            targetNo = 1;
            System.out.println("⚠️ [MyPage] 경고: 로그인 정보가 없어 1번 회원 정보를 조회합니다.");
        }

        MemberProfileRequest profile = myPageService.getProfile(targetNo);

        Map<String, Object> map = new HashMap<>();
        map.put("data", profile);
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
        List<Map<String, Object>> list = myPageService.searchRegion(keyword);
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("result", "success");
        return map;
    }

    // 9. 공통 코드 조회
    @GetMapping("/codes")
    public Map<String, Object> getAllCodes() {
        List<MasterCode> list = myPageService.getAllMasterCodes();
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("result", "success");
        return map;
    }

    // 10. 닉네임 중복 검사
    @GetMapping("/nickname/check")
    @ResponseBody
    public int checkNickname(@RequestParam("nickname") String nickname) {
        return myPageService.checkNickname(nickname);
    }

    // 11. 회원 탈퇴
    @PutMapping("/withdraw")
    public int withdraw(@RequestBody Map<String, Integer> param, HttpSession session) {
        int memberNo = param.get("memberNo");
        int result = myPageService.withdraw(memberNo);
        if (result > 0) {
            session.invalidate();
        }
        return result;
    }
}