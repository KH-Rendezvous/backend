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
	@Value("${project.resource.location}")
	private String folderPath;

	@Value("${project.resource.webpath}") 
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
	public int updateProfile(MemberProfileRequest profileRequest, List<MultipartFile> images, List<Integer> orders,
			List<Integer> deleteList) throws Exception {

		// 1. 프로필 정보(MEMBER_PROFILE) 수정 (자기소개, MBTI, 키 등)
		int result = mapper.updateMemberProfile(profileRequest);

		// 2. 닉네임 및 생활/연애 정보(MEMBER) 수정
		if (profileRequest.getNickname() != null) {
			result += mapper.updateMemberCommon(profileRequest);
		}

		// 3. 관심사 수정
		// 관심사 리스트가 null이 아닐 때만 수행 (빈 리스트라도 수행해서 기존 거 지워야 함)
		if (profileRequest.getInterestList() != null) {
			// 기존 관심사 삭제
			mapper.deleteInterests(profileRequest.getMemberNo());

			// 선택한 관심사가 있으면 삽입
			if (!profileRequest.getInterestList().isEmpty()) {
				mapper.insertInterests(profileRequest.getMemberNo(), profileRequest.getInterestList());
			}
		}

		// 4. 이미지 삭제 처리
		if (deleteList != null && !deleteList.isEmpty()) {
			mapper.deleteProfileImages(deleteList);
		}
		
		if (profileRequest.getPreservedImages() != null && !profileRequest.getPreservedImages().isEmpty()) {
            for (MemberProfileRequest.PreservedImage img : profileRequest.getPreservedImages()) {
                mapper.updateProfileImageOrder(img);
            }
        }

		// 5. 이미지 업로드 및 수정 처리
		if (images != null && !images.isEmpty()) {
			for (int i = 0; i < images.size(); i++) {
				MultipartFile file = images.get(i);
				int order = orders.get(i); // 프론트에서 보낸 순서(1~6)

				// 파일이 비어있지 않으면(용량이 있으면) 업로드 진행
				if (file.getSize() > 0) {
					// 파일명 변경
					String originalName = file.getOriginalFilename();
					String rename = FileUtil.rename(originalName);

					// DTO 생성
					MemberPhoto photo = new MemberPhoto();
					photo.setMemberNo(profileRequest.getMemberNo());
					photo.setPhotoOrder(order);
					photo.setOriginName(originalName);
					photo.setRenameName(rename);
					photo.setPhotoUrl(webPath);

					mapper.insertOrUpdateProfileImage(photo);

					// 서버 파일 저장
					file.transferTo(new File(folderPath + rename));

					result++; // 성공 카운트 증가
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

		// 3. 프로필 이미지 목록 가져와서 합치기
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
	@Transactional(rollbackFor = Exception.class)
	public int withdraw(int memberNo) {
		return mapper.withdraw(memberNo);
	}

}