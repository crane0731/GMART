package gmart.gmart.dto.inquiry;

import gmart.gmart.domain.Inquiry;
import gmart.gmart.util.DateFormatUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 문의 리스트 조회 DTO
 */
@Getter
@Setter
public class InquiryListResponseDto {

    private Long inquiryId; //문의 아이디
    private Long memberId; //회원 아이디
    private String nickname; //회원닉네임
    private String title; // 문의 제목
    private String answerStatus; //답변 상태
    private String createAt; //생성일


    /**
     * 생성 메서드
     * @param inquiry 문의 엔티티 객체
     * @return 응답 DTO
     */
    public static InquiryListResponseDto createDto(Inquiry inquiry) {

        InquiryListResponseDto dto = new InquiryListResponseDto();

        dto.setInquiryId(inquiry.getId());
        dto.setMemberId(inquiry.getMember().getId());
        dto.setNickname(inquiry.getMember().getNickname());
        dto.setTitle(inquiry.getTitle());
        dto.setAnswerStatus(inquiry.getAnswerStatus().toString());
        dto.setCreateAt(DateFormatUtil.DateFormat(inquiry.getCreatedDate()));

        return dto;
    }

}
