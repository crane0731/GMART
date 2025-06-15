package gmart.gmart.dto.adminmessage;

import gmart.gmart.domain.AdminMessage;
import gmart.gmart.util.DateFormatUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 관리자 메시지 리스트 응답 DTO
 */
@Getter
@Setter
public class AdminMessageListResponseDto {

    private Long adminMessageId; //관리자 메시지 아이디
    private String content; //메시지 내용
    private String messageType; //메시지 타입

    private Long memberId; // 회원 아이디
    private String memberNickname; //회원 닉네임
    private String memberLoginId; //회원 로그인 아이디

    private String createdAt; //생성일

    /**
     * [생성 메서드]
     * @param adminMessage 관리자 메시지 엔티티
     * @return AdminMessageListResponseDto 응답 DTO
     */
    public static AdminMessageListResponseDto create(AdminMessage adminMessage) {
        AdminMessageListResponseDto dto = new AdminMessageListResponseDto();

        dto.setAdminMessageId(adminMessage.getId());
        dto.setContent(adminMessage.getContent());
        dto.setMessageType(adminMessage.getMessageType().toString());

        dto.setMemberId(adminMessage.getMember().getId());
        dto.setMemberNickname(adminMessage.getMember().getNickname());
        dto.setMemberLoginId(adminMessage.getMember().getLoginId());

        dto.setCreatedAt(DateFormatUtil.DateFormat(adminMessage.getCreatedDate()));
        return dto;
    }
}
