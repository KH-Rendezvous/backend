package edu.kh.project.matching.controller;

import edu.kh.project.matching.model.dto.MatchingUserDTO;
import edu.kh.project.matching.model.service.MatchingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/matching")
public class MatchingController {

    @Autowired
    private MatchingService service;

    @GetMapping("/discovery")
    public List<MatchingUserDTO> getDiscoveryList(@RequestParam("memberNo") int memberNo) {
        log.info("조회 요청 회원 번호: {}", memberNo); // 로그로 번호가 잘 들어오는지 확인
        return service.selectDiscoveryList(memberNo);
    }
}