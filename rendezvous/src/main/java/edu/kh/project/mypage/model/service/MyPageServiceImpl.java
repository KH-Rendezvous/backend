package edu.kh.project.mypage.model.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.common.util.FileUtil;
import edu.kh.project.member.model.dto.MasterCode;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.dto.MemberPhoto;
import edu.kh.project.member.model.dto.MemberProfileRequest;
import edu.kh.project.mypage.model.mapper.MyPageMapper;

@Service
public class MyPageServiceImpl implements MyPageService {

	@Autowired
	private MyPageMapper mapper;

	// config.properties에서 값 가져오기
	@Value("${project.resource.location}") // C:/upload/project/
	private String folderPath;

	@Value("${project.resource.webpath}") // /images/
	private String webPath;

	// 거리 수정
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int updateDistance(Member member) {
		return mapper.updateDistance(member);
	}

	@Override
	public int updateGender(Member member) {
		return mapper.updateGender(member);
	}

	@Override
	public int updateAgeRange(Member member) {
		return mapper.updateAgeRange(member);
	}

	@Override
	public int updateVisibility(Member member) {
		return mapper.updateVisibility(member);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int updateProfile(MemberProfileRequest req, List<MultipartFile> images, List<Integer> orders,
			List<Integer> deleteList) throws Exception {

		// 1. 텍스트 정보 수정 (기존 로직 유지)
		int result = mapper.updateMemberProfile(req);
		// 공통 정보 수정은 필요에 따라 활성화 (쿼리가 나뉘어 있다면)
		// result += mapper.updateMemberCommon(req);

		// 2. 관심사 수정 (삭제 후 재삽입)
		if (req.getInterestList() != null) {
			mapper.deleteInterests(req.getMemberNo()); // 기존 관심사 삭제
			if (!req.getInterestList().isEmpty()) {
				// 리스트 통째로 넘겨서 마이바티스 <foreach>로 한 번에 삽입 (권장)
				mapper.insertInterests(req.getMemberNo(), req.getInterestList());
			}
		}

		// 3. 이미지 삭제 처리 (deleteList에 담긴 ID들 제거)
		if (deleteList != null && !deleteList.isEmpty()) {
			mapper.deleteProfileImages(deleteList);
			// (서버 폴더 내 실제 파일 삭제 로직은 선택 사항)
		}

		// 4. 새 이미지 업로드 및 정보 저장 (MERGE)
		if (images != null && !images.isEmpty()) {
			for (int i = 0; i < images.size(); i++) {
				MultipartFile file = images.get(i);
				int order = orders.get(i); // 해당 파일의 순서(1~6)

				if (file.getSize() > 0) {
					// 4-1. 파일명 변경 및 저장
					String rename = FileUtil.rename(file.getOriginalFilename());
					file.transferTo(new File(folderPath + rename));

					// 4-2. DTO 생성
					MemberPhoto photo = new MemberPhoto();
					photo.setMemberNo(req.getMemberNo());
					photo.setPhotoOrder(order);
					photo.setOriginName(file.getOriginalFilename());
					photo.setRenameName(rename);
					photo.setPhotoUrl(webPath);

					// 4-3. DB 저장 (있으면 수정, 없으면 삽입)
					mapper.insertOrUpdateProfileImage(photo);
				}
			}
		}

		return result;
	}

	@Override
	public MemberProfileRequest getProfile(int memberNo) {

		// 1. 기본 프로필 정보 가져오기
		MemberProfileRequest profile = mapper.selectMemberProfile(memberNo);

		// (방어 코드)
		if (profile == null)
			profile = new MemberProfileRequest();

		// 2. 관심사 목록 가져와서 합치기
		List<String> interests = mapper.selectMemberInterests(memberNo);
		profile.setInterestList(interests);

		// ★ [수정] 3. 프로필 이미지 목록 가져와서 합치기 (이 부분이 누락됨)
		// DTO에 List<MemberPhoto> profileList 필드가 있어야 함
		List<MemberPhoto> photos = mapper.selectMemberPhotos(memberNo);
		profile.setProfileList(photos);

		return profile;
	}

	@Override
	public List<Map<String, Object>> searchSchool(String keyword) {
		return mapper.searchSchool(keyword);
	}

	@Override
	public List<Map<String, Object>> searchRegion(String keyword) {
		return mapper.searchRegion(keyword);
	}

	@Override
	public List<MasterCode> getAllMasterCodes() {
		return mapper.selectAllMasterCodes();
	}

	@Override
	public int checkNickname(String nickname) {
		return mapper.checkNickname(nickname);
	}

	@Override
	@Transactional(rollbackFor = Exception.class) // 트랜잭션 처리 필수
	public int withdraw(int memberNo) {
		return mapper.withdraw(memberNo);
	}



}