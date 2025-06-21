package gmart.gmart.dto.inquiry;

import gmart.gmart.domain.Inquiry;
import gmart.gmart.util.DateFormatUtil;
import jakarta.persistence.GeneratedValue;
import lombok.Getter;
import lombok.Setter;

/**
 * 문의 단일 상세조회 DTO
 */
@Getter
@Setter
public class InquiryResponseDto {

    private Long memberId;
    private String nickname; //닉네임
    private String loginId; //로그인 아이디(이메일)

    private Long inquiryId;//문의 아이디
    private String title; //제목
    private String content; //내용
    private String answer; //답변
    private String answerStatus; //답변 상태
    private String createdAt; //생성일
    private String updatedAt; //수정일


    /**
     * 생성 메서드
     * @param inquiry 문의 엔티티 객체
     * @return DTO
     */
    public static InquiryResponseDto createDto (Inquiry inquiry) {

        InquiryResponseDto dto = new InquiryResponseDto();

        dto.setMemberId(inquiry.getMember().getId());
        dto.setNickname(inquiry.getMember().getNickname());
        dto.setLoginId(inquiry.getMember().getLoginId());

        dto.setInquiryId(inquiry.getId());
        dto.setTitle(inquiry.getTitle());
        dto.setContent(inquiry.getContent());
        dto.setAnswer(inquiry.getAnswer());
        dto.setAnswerStatus(inquiry.getAnswerStatus().toString());
        dto.setCreatedAt(DateFormatUtil.DateFormat(inquiry.getCreatedDate()));
        dto.setUpdatedAt(DateFormatUtil.DateFormat(inquiry.getUpdatedDate()));

        return dto;
    }
}
