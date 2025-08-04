package gmart.gmart.dto.mybatis;

import gmart.gmart.domain.Member;
import gmart.gmart.util.DateFormatUtil;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 *  관리자 회원리스트 조회결과를 응답하기 위한 DTO
 */
@Getter
@Setter
public class MemberListResponseDto {
    private Long id;
    private String loginId;
    private String nickname;
    private String name;
    private String phone;
    private String memberRole;
    private Long suspensionCount;
    private Long reportedCount;
    private Long totalSpent;

    private String createdDate;

    /**
     * [생성 메서드]
     * @param member 회원
     * @return MemberListResponseDto
     */
    public static MemberListResponseDto create (Member member) {
        MemberListResponseDto dto = new MemberListResponseDto();
        dto.setId(member.getId());
        dto.setLoginId(member.getLoginId());
        dto.setNickname(member.getNickname());
        dto.setName(member.getName());
        dto.setPhone(member.getPhoneNumber());
        dto.setMemberRole(member.getMemberRole().toString());
        dto.setSuspensionCount(member.getSuspensionCount());
        dto.setReportedCount(member.getReportedCount());
        dto.setTotalSpent(member.getTotalSpent());
        dto.setCreatedDate(DateFormatUtil.DateFormat(member.getCreatedDate()));
        return dto;

    }

}
