package edu.kh.project.mypage.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.kh.project.member.model.dto.MasterCode;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.dto.MemberPhoto;
import edu.kh.project.member.model.dto.MemberProfileRequest;

@Mapper
public interface MyPageMapper {
    
    // 1. 회원 정보 수정
    int updateMemberProfile(MemberProfileRequest req);
    
    // (참고) XML에 updateMemberCommon 쿼리가 없다면 이 메서드는 호출 시 에러가 날 수 있습니다.
    // 현재 서비스 코드에서는 주석 처리되어 있으니 괜찮습니다.
    int updateMemberCommon(MemberProfileRequest req);  

    // 2. 관심사 관리
    int deleteInterests(int memberNo);

    // 매개변수가 2개 이상일 땐 @Param을 꼭 붙여야 XML에서 #{memberNo}, collection="list"로 인식함
    void insertInterests(@Param("memberNo") int memberNo, @Param("list") List<String> interestList);
    
    // 3. 기타 설정 수정
    int updateDistance(Member member);
    int updateGender(Member member);
    int updateAgeRange(Member member);
    int updateVisibility(Member member);

    // 4. 조회 및 검색
    MemberProfileRequest selectMemberProfile(int memberNo);
    List<String> selectMemberInterests(int memberNo);
    List<Map<String, Object>> searchSchool(String keyword);
    List<Map<String, Object>> searchRegion(String keyword);
    List<MasterCode> selectAllMasterCodes();
    int checkNickname(String nickname);
    
    // 5. 탈퇴
    int withdraw(int memberNo);

    // 6. 이미지 관리 (서비스에서 호출하는 메서드들)
    void deleteProfileImages(List<Integer> deleteList);
    void insertOrUpdateProfileImage(MemberPhoto photo);
    List<MemberPhoto> selectMemberPhotos(int memberNo);
}