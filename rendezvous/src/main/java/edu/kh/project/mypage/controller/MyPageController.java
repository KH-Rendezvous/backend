package edu.kh.project.mypage.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.mypage.model.mapper.MyPageMapper;
import edu.kh.project.mypage.model.service.MyPageService;

@RestController
@RequestMapping("/api/mypage")
public class MyPageController {

    @Autowired
    private MyPageMapper myPageMapper;
    
    @Autowired
    private MyPageService myPageService;

    @PutMapping("/distance")
    public Map<String, String> updateDistance(@RequestBody Member member) {
        System.out.println("거리 수정 요청: " + member); 

        int result = myPageMapper.updateDistance(member);
        
        Map<String, String> map = new HashMap<>();
        map.put("result", result > 0 ? "success" : "fail");
        return map;
    }
    
    @PutMapping("/gender")
    public Map<String, String> updateGender(@RequestBody Member member) {
        System.out.println("성별 수정 요청: " + member); 

        int result = myPageService.updateGender(member);
        
        Map<String, String> map = new HashMap<>();
        map.put("result", result > 0 ? "success" : "fail");
        return map;
    }
    
    // 연령대 수정 요청
    @PutMapping("/age")
    public Map<String, String> updateAgeRange(@RequestBody Member member) {
        System.out.println("연령대 수정 요청: " + member); 

        int result = myPageService.updateAgeRange(member);
        
        Map<String, String> map = new HashMap<>();
        map.put("result", result > 0 ? "success" : "fail");
        return map;
    }
    
    // 프로필 공개 여부 수정 (A: 전체, P: 비공개)
    @PutMapping("/visibility")
    public Map<String, String> updateVisibility(@RequestBody Member member) {
        System.out.println("공개범위 수정 요청: " + member); 
        
        // 서비스 호출 (변수명 profileOpen 주의)
        int result = myPageService.updateVisibility(member);
        
        Map<String, String> map = new HashMap<>();
        map.put("result", result > 0 ? "success" : "fail");
        return map;
    }
}