package edu.kh.project.member.model.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.common.util.FileUtil;
import edu.kh.project.email.model.mapper.EmailMapper;
import edu.kh.project.member.model.dto.BlockContact;
import edu.kh.project.member.model.dto.LoginRequest;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.dto.MemberPhoto;
import edu.kh.project.member.model.dto.MemberProfileRequest;
import edu.kh.project.member.model.dto.SignupRequest;
import edu.kh.project.member.model.mapper.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberMapper mapper;

	@Autowired
	private EmailMapper emailMapper;

	@Autowired
	private BCryptPasswordEncoder bcrypt;

	@Value("${project.resource.location}")
	private String folderPath;

	@Value("${project.resource.webpath}")
	private String webPath;

	// --- 기존 차단 관련 기능 ---

	// 차단 등록
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int insertBlock(BlockContact blockContact) {
		return mapper.insertBlockContact(blockContact);
	}

	// 차단 목록 조회
	@Override
	public List<BlockContact> selectBlockList(int memberNo) {
		return mapper.selectBlockList(memberNo);
	}

	// 차단 해제
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int deleteBlock(int blockId, int memberNo) {
		BlockContact blockContact = new BlockContact();
		blockContact.setBlockId(blockId);
		blockContact.setMemberNo(memberNo);

		return mapper.deleteBlockContact(blockContact);
	}

	// --- [통합] 회원가입 기능 ---

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int signup(SignupRequest input, List<MultipartFile> images) {

		// 인증번호 검사
		Map<String, String> authMap = new HashMap<>();
		authMap.put("email", input.getEmail());
		authMap.put("authKey", input.getAuthKey());

		int check = emailMapper.checkAuthKey(authMap);

		if (check == 0) {
			// 인증번호 불일치 시 예외 발생시켜서 롤백
			throw new RuntimeException("인증번호가 일치하지 않습니다.");
		}

		Member member = new Member();
		member.setEmail(input.getEmail());
		member.setPassword(bcrypt.encode(input.getPassword()));
		member.setName(input.getName());
		member.setNickname(input.getNickname());
		member.setPhone(input.getPhone());

		// TargetGender 처리
		String target = input.getTargetGender();
		member.setTargetGender("ALL".equals(target) ? "A" : target);

		// 관계(한글) -> ID(숫자) 변환
		member.setRelIntentId(convertRelationToId(input.getRelation()));

		// 필수값 기본 세팅
		member.setLatitude(37.5665);
		member.setLongitude(126.9780);
		member.setSearchDistance(10);

		int result = mapper.insertMember(member);
		if (result == 0)
			return 0;

		int memberNo = member.getMemberNo(); // 생성된 회원 번호

		// 2. MEMBER_PROFILE 테이블 저장
		MemberProfileRequest profile = new MemberProfileRequest();
		profile.setMemberNo(memberNo);
		profile.setGender(input.getGender());
		profile.setHeight(0); // 키 정보 없음(0 처리)

		// 날짜 합쳐서 String으로 전달 (Mapper에서 TO_DATE 처리)
		String birthStr = String.format("%s-%s-%s", input.getBirthYear(), input.getBirthMonth(), input.getBirthDay());
		profile.setBirthDate(birthStr);

		mapper.insertMemberProfile(profile);

		// 3. MEMBER_INTERESTS 테이블 저장 (관심사)
		if (input.getInterests() != null) {
			for (String interestName : input.getInterests()) {
				// DB에서 이름으로 코드 ID 조회 (예: "맛집" -> 202)
				Integer codeId = mapper.selectCodeId(interestName, "관심사");

				if (codeId != null) {
					mapper.insertMemberInterest(memberNo, codeId);
				}
			}
		}

		// 4. MEMBER_PHOTOS 테이블 저장 (이미지)
		if (images != null && !images.isEmpty()) {

			// 폴더가 없으면 생성
			File folder = new File(folderPath);
			if (!folder.exists())
				folder.mkdirs();

			for (int i = 0; i < images.size(); i++) {
				MultipartFile file = images.get(i);
				if (file.isEmpty())
					continue;

				String originName = file.getOriginalFilename();

				// ★ FileUtil을 사용하여 파일명 변경
				String renameName = FileUtil.rename(originName);

				String url = webPath + renameName; // 웹 접근 경로 + 파일명

				// 실제 파일 저장
				try {
					file.transferTo(new File(folderPath + renameName));
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("프로필 이미지 저장 실패");
				}

				// DB 저장
				MemberPhoto photo = new MemberPhoto();
				photo.setMemberNo(memberNo);
				photo.setOriginName(originName);
				photo.setRenameName(renameName);
				photo.setPhotoUrl(url);
				photo.setPhotoOrder(i + 1);

				mapper.insertMemberPhoto(photo);
			}
		}

		return 1;
	}

	private Integer convertRelationToId(String relation) {
		if (relation == null)
			return null;
		switch (relation) {
		case "진지한 연애":
			return 101;
		case "천천히 서로 알아가기":
			return 102;
		case "연애는 부담, 데이트만 선호":
			return 103;
		case "심심할 때 부를 술/밥 친구":
			return 104;
		case "같이 취미 즐길 동네 친구":
			return 105;
		case "아직 모르겠음":
			return 106;
		default:
			return null;
		}
	}
	
	@Override
    public Member login(LoginRequest inputMember) {
        
        // 1. 이메일로 회원 정보 조회 (DB에서 암호화된 비번 가져옴)
        Member loginMember = mapper.login(inputMember.getEmail());

        // 2. 일치하는 이메일이 없으면 null 리턴
        if (loginMember == null) {
            return null;
        }

        // 3. 탈퇴한 회원이면 로그인 불가
        if (loginMember.getDelFl().equals("Y")) {
            return null; // 혹은 예외 처리
        }

        // 4. 비밀번호 확인 (입력받은 쌩비번 vs DB의 암호화된 비번)
        if (!bcrypt.matches(inputMember.getPassword(), loginMember.getPassword())) {
            return null; // 비번 틀림
        }

        // 5. 로그인 성공! (비밀번호는 보안상 null로 밀어버리고 리턴하는 게 국룰)
        loginMember.setPassword(null); 
        return loginMember;
    }
}