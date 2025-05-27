package gmart.gmart.dto.member;

import gmart.gmart.domain.Member;
import gmart.gmart.domain.MemberGundamGrade;
import gmart.gmart.dto.AddressDto;
import gmart.gmart.dto.MemberPreferredGundamGradeDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 회원 정보 응답을 위한 DTO
 */
@Getter
@Setter
public class MemberInfoResponseDto {

    private Long memberId; //회원 아이디
    private String loginId; //로그인 아이디
    private String name; //이름
    private String nickname; //닉네임
    private String phone; //전화 번호
    private AddressDto address; //주소
    private String profileImageUrl; //프로필 이미지


    private Long gMoney; //g머니
    private Long gPoint; //g포인트
    private Long mannerPoint; //매너포인트
    private String mannerGrade; //매너등급


    private Long totalSpent; //총 사용 금액
    private Long reportedCount; //신고당한 횟수
    private Long reviewedCount; //리뷰받은 횟수
    private String memberRole; //권한

    private List<MemberPreferredGundamGradeDto> preferredGundamGrades; //회원이 선호하는 건담 등급들


    /**
     * 생성 메서드
     */
    public static MemberInfoResponseDto createDto(Member member){

        AddressDto addressDto = getAddressDto(member);

        List<MemberPreferredGundamGradeDto> memberPreferredGundamGradesDtos = getMemberPreferredGundamGradesDtos(member);


        MemberInfoResponseDto dto = new MemberInfoResponseDto();

        dto.setMemberId(member.getId());
        dto.setLoginId(member.getLoginId());
        dto.setName(member.getName());
        dto.setNickname(member.getNickname());
        dto.setPhone(member.getPhoneNumber());
        dto.setAddress(addressDto);
        dto.setProfileImageUrl(member.getMemberProfileImage().getImageUrl());

        dto.setGMoney(member.getGMoney());
        dto.setGPoint(member.getGPoint());
        dto.setMannerPoint(member.getMannerPoint());
        dto.setMannerGrade(member.getMannerGrade().toString());

        dto.setTotalSpent(member.getTotalSpent());
        dto.setReportedCount(member.getReportedCount());
        dto.setReviewedCount(member.getReviewedCount());
        dto.setMemberRole(member.getMemberRole().toString());

        dto.setPreferredGundamGrades(memberPreferredGundamGradesDtos);

        return dto;


    }

    //==AddressDto 리스트 생성 ==//
    private static AddressDto getAddressDto(Member member) {
        AddressDto addressDto = AddressDto.createDto(member.getAddress());
        return addressDto;
    }



    //==MemberPreferredGundamGradesDto 리스트 생성==//
    private static List<MemberPreferredGundamGradeDto> getMemberPreferredGundamGradesDtos(Member member) {
        List<MemberPreferredGundamGradeDto> memberPreferredGundamGradesDtos = new ArrayList<>();

        for (MemberGundamGrade memberGundamGrade : member.getMemberGundamGrades()) {
            MemberPreferredGundamGradeDto dto = MemberPreferredGundamGradeDto.createDto(memberGundamGrade);
            memberPreferredGundamGradesDtos.add(dto);
        }
        return memberPreferredGundamGradesDtos;
    }

}
