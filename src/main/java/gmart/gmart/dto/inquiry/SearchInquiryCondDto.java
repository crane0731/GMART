package gmart.gmart.dto.inquiry;

import gmart.gmart.domain.enums.AnswerStatus;
import gmart.gmart.dto.enums.CreatedDateSortType;
import lombok.Getter;
import lombok.Setter;

/**
 * 문의리스트를 조회하기위한 조건을 담은 DTO
 */
@Getter
@Setter
public class SearchInquiryCondDto {

    private String title; //문의 제목

    private AnswerStatus answerStatus; //  답변 상태 -> [UNANSWERED,ANSWERED]

    private CreatedDateSortType createSortType; //최신순, 과거순 ->[CREATE_DATE_ASC, CREATE_DATE_DESC]

}
