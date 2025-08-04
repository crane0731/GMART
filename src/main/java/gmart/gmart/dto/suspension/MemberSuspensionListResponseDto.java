package gmart.gmart.dto.suspension;

import gmart.gmart.domain.MemberSuspension;
import gmart.gmart.util.DateFormatUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원 계정 정지 내역 리스트 응답 DTO
 */
@Getter
@Setter
public class MemberSuspensionListResponseDto {

    private Long memberSuspensionId; //회원 활동 정지 아이디
    private Long memberId; //회원 아이디
    private String reason; //사유
    private String activeStatus; //정지 상태
    private String startAt; //정지 시작 날짜
    private String endAt;// 정지 해제 날짜


    /**
     * [생성 메서드]
     * @param memberSuspension 회원 계정 정지
     * @return MemberSuspensionListResponseDto
     */
    public static MemberSuspensionListResponseDto create(MemberSuspension memberSuspension) {
        MemberSuspensionListResponseDto dto = new MemberSuspensionListResponseDto();
        dto.setMemberSuspensionId(memberSuspension.getId());
        dto.setMemberId(memberSuspension.getMember().getId());
        dto.setReason(memberSuspension.getReason());
        dto.setActiveStatus(memberSuspension.getMemberActiveStatus().toString());
        dto.setStartAt(DateFormatUtil.DateFormat(memberSuspension.getStartAt()));
        dto.setEndAt(DateFormatUtil.DateFormat(memberSuspension.getEndAt()));
        return dto;
    }


}
