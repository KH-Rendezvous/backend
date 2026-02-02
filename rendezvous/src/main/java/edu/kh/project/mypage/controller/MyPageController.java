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

	@PostMapping("/profile")
    public Map<String, Object> updateProfile(
            @RequestPart("profileText") MemberProfileRequest profileRequest,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestParam(value = "orders", required = false) List<Integer> orders,
            @RequestParam(value = "deleteList", required = false) List<Integer> deleteList,
            
            // 👇 [중요] required = false 추가 (없어도 에러 안 나게)
            @SessionAttribute(value = "loginMember", required = false) Member loginMember
            ) throws Exception {

        // 👇 로그인 안 됐으면 테스트용 1번 회원으로 설정
        if (loginMember == null) {
            System.out.println("⚠️ [DEBUG] 세션 없음: 테스트용 1번 회원으로 진행함");
            profileRequest.setMemberNo(1);
        } else {
            profileRequest.setMemberNo(loginMember.getMemberNo());
        }

        // 2. 서비스 호출
        int result = myPageService.updateProfile(profileRequest, images, orders, deleteList);

        if (result > 0) return Map.of("result", "success");
        else return Map.of("result", "fail");
    }

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

	// 5. 내 프로필 정보 조회
	@GetMapping("/profile")
	public Map<String, Object> getProfile(
			@SessionAttribute(value = "loginMember", required = false) Member loginMember) {

		// 테스트용 (로그인 안 됨 -> 1번 회원)
		int memberNo = 1;
		if (loginMember != null)
			memberNo = loginMember.getMemberNo();
		else
			System.out.println("⚠️ [MyPage] 조회: 로그인 안 돼서 1번 회원 정보 가져옴");

		// 서비스 호출
		MemberProfileRequest profile = myPageService.getProfile(memberNo);

		Map<String, Object> map = new HashMap<>();
		map.put("data", profile); // "data"라는 이름으로 담아 보냄
		map.put("result", "success");

		return map;
	}

	// 6. 학교 검색
	@GetMapping("/school")
	public Map<String, Object> searchSchool(@RequestParam("keyword") String keyword) {
		List<Map<String, Object>> list = myPageService.searchSchool(keyword);

		Map<String, Object> map = new HashMap<>();
		map.put("list", list);
		map.put("result", "success");
		return map;
	}

	// 7. 지역 검색
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
		// 서비스 가서 SELECT COUNT(*) FROM MEMBER WHERE NICKNAME = #{nickname} AND DEL_FL =
		// 'N' 해오기
		return myPageService.checkNickname(nickname);
	}

	/**
	 * 회원 탈퇴
	 * 
	 * @param param   : 프론트에서 보낸 { memberNo: 1 } JSON 데이터
	 * @param session : 탈퇴 성공 시 로그아웃 처리를 위해 필요
	 * @return result : 1(성공), 0(실패)
	 */
	@PutMapping("/withdraw")
	public int withdraw(@RequestBody Map<String, Integer> param, HttpSession session) {

		// 1. 프론트에서 보낸 memberNo 꺼내기
		int memberNo = param.get("memberNo");

		// 2. 서비스 호출 (DB 업데이트)
		int result = myPageService.withdraw(memberNo);

		// 3. 탈퇴 성공 시, 강제로 로그아웃(세션 만료) 시킴
		if (result > 0) {
			session.invalidate();
		}

		return result;
	}
}