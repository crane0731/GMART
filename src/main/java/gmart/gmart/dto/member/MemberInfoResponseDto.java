package gmart.gmart.dto.member;

import gmart.gmart.domain.Member;
import gmart.gmart.dto.AddressDto;
import lombok.Getter;
import lombok.Setter;

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


    private Long totalSpent; //총 사용 금액
    private Long reportedCount; //신고당한 횟수
    private Long reviewedCount; //리뷰받은 횟수
    private Long adminMessageCount; //관리자 메시지 받은 횟수
    private String memberRole; //권한


    /**
     * 생성 메서드
     */
    public static MemberInfoResponseDto createDto(Member member){

        AddressDto addressDto = getAddressDto(member);

        MemberInfoResponseDto dto = new MemberInfoResponseDto();

        dto.setMemberId(member.getId());
        dto.setLoginId(member.getLoginId());
        dto.setName(member.getName());
        dto.setNickname(member.getNickname());
        dto.setPhone(member.getPhoneNumber());
        dto.setAddress(addressDto);
        dto.setProfileImageUrl(member.getProfileImageUrl());

        dto.setGMoney(member.getGMoney());
        dto.setGPoint(member.getGPoint());


        dto.setTotalSpent(member.getTotalSpent());
        dto.setReportedCount(member.getReportedCount());
        dto.setReviewedCount(member.getReviewedCount());
        dto.setAdminMessageCount(member.getAdminMessageCount());
        dto.setMemberRole(member.getMemberRole().toString());

        return dto;


    }

    //==AddressDto 리스트 생성 ==//
    private static AddressDto getAddressDto(Member member) {

        AddressDto addressDto = new AddressDto();

        if(member.getAddress()!=null) {
            addressDto= AddressDto.createDto(member.getAddress());
        }
        else{
            addressDto=null;
        }
        return addressDto;
    }

}
